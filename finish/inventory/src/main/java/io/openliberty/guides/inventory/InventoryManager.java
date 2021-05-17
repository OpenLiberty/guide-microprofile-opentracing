// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2021 IBM Corporation and others.
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

import java.util.ArrayList;
import java.util.Properties;
import io.openliberty.guides.inventory.client.SystemClient;
import io.openliberty.guides.inventory.model.InventoryList;
import io.openliberty.guides.inventory.model.SystemData;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Collections;

import org.eclipse.microprofile.opentracing.Traced;

import io.opentracing.Scope;
import io.opentracing.Tracer;

@ApplicationScoped
// tag::InventoryManager[]
public class InventoryManager {

    private List<SystemData> systems = Collections.synchronizedList(new ArrayList<>());
    private SystemClient systemClient = new SystemClient();
    // tag::customTracer[]
    @Inject Tracer tracer;
    // end::customTracer[]

    public Properties get(String hostname) {
        systemClient.init(hostname, 9080);
        Properties properties = systemClient.getProperties();
        return properties;
    }

    public void add(String hostname, Properties systemProps) {
        Properties props = new Properties();
        props.setProperty("os.name", systemProps.getProperty("os.name"));
        props.setProperty("user.name", systemProps.getProperty("user.name"));

        SystemData system = new SystemData(hostname, props);
        // tag::Add[]
        if (!systems.contains(system)) {
            // tag::Try[]
            // tag::addSpan[]
            try (Scope childScope = tracer.activateSpan(
                    tracer.buildSpan("add() Span").start())) {
            // end::addSpan[]
                // tag::addToInvList[]
                systems.add(system);
                // end::addToInvList[]
            }
            // end::Try[]
        }
        // end::Add[]
    }

    // tag::Traced[]
    @Traced(value = true, operationName = "InventoryManager.list")
    // end::Traced[]
    // tag::list[]
    public InventoryList list() {
        return new InventoryList(systems);
    }
    // end::list[]
}
// end::InventoryManager[]
