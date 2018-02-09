// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
 // end::copyright[]
package io.openliberty.guides.inventory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.eclipse.microprofile.opentracing.Traced;

import io.openliberty.guides.common.JsonMessages;
import io.openliberty.guides.inventory.util.InventoryUtil;

import io.opentracing.Span;
import io.opentracing.Tracer;

@ApplicationScoped
public class InventoryManager {

    private ConcurrentMap<String, JsonObject> inv = new ConcurrentHashMap<>();
    // tag::inject-tracer[]
//    @Inject private Tracer tracer;
    // end::inject-tracer[]

    public JsonObject get(String hostname) {
        if (InventoryUtil.responseOk(hostname)) {
            JsonObject properties = InventoryUtil.getProperties(hostname);
            inv.putIfAbsent(hostname, properties);
            return properties;
        } else {
            return JsonMessages.SERVICE_UNREACHABLE.getJson();
        }
    }

    @Traced(value = true, operationName = "InventoryManager.list")
    public JsonObject list() {
        JsonObjectBuilder systems = Json.createObjectBuilder();
        // tag::inject-tracer[]
//        Span childSpan = tracer.buildSpan("forEach-span").asChildOf(tracer.activeSpan().context()).start();
        // end::inject-tracer[]
        inv.forEach((host, props) -> {
            JsonObject systemProps = Json.createObjectBuilder()
                                         .add("os.name", props.getString("os.name"))
                                         .add("user.name", props.getString("user.name"))
                                         .build();
            systems.add(host, systemProps);
        });
        // tag::inject-tracer[]
//        childSpan.finish();
        // end::inject-tracer[]
        systems.add("hosts", systems);
        systems.add("total", inv.size());
        return systems.build();
    }
}
