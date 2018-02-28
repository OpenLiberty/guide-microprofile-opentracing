//tag::copyright[]
/*******************************************************************************
* Copyright (c) 2017, 2018 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     IBM Corporation - Initial implementation
*******************************************************************************/
//end::copyright[]
package io.openliberty.guides.inventory;

import java.util.Properties;
import io.openliberty.guides.inventory.client.SystemClient;
import io.openliberty.guides.inventory.model.InventoryList;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.opentracing.Traced;

import io.opentracing.Span;
import io.opentracing.Tracer;

@ApplicationScoped
public class InventoryManager {

    private InventoryList invList = new InventoryList();
    private SystemClient systemClient = new SystemClient();

    // tag::inject-tracer[]
    // @Inject Tracer tracer;
    // end::inject-tracer[]

    public Properties get(String hostname) {
        systemClient.init(hostname, 9080);
        
        Properties properties = systemClient.getProperties();
        if (properties != null) {
            // tag::inject-tracer[]
            // Span childSpan = tracer.buildSpan("forEach-span").start();
            // end::inject-tracer[]
            invList.addToInventoryList(hostname, properties);
            // tag::inject-tracer[]
            // childSpan.finish();
            // end::inject-tracer[]
        }
        return properties;
    }

    @Traced(value = false, operationName = "InventoryManager.list")
    public InventoryList list() {
        return invList;
    }
    
}
