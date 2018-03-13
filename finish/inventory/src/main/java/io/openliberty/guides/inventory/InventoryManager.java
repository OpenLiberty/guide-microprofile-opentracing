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
import javax.inject.Inject;

import org.eclipse.microprofile.opentracing.Traced;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@ApplicationScoped
public class InventoryManager {

    private InventoryList invList = new InventoryList();
    private SystemClient systemClient = new SystemClient();

    // tag::custom-tracer[]
    @Inject Tracer tracer;
    // end::custom-tracer[]

    public Properties get(String hostname) {
        systemClient.init(hostname, 9080);
        
        Properties properties = systemClient.getProperties();
        if (properties != null) {
            // tag::custom-tracer[]
            try (ActiveSpan childSpan = tracer.buildSpan("addToInventory() Span").startActive()) {
                // tag::addToInvList[]
                invList.addToInventoryList(hostname, properties);
                // end::addToInvList[]
            }
            // end::custom-tracer[]
        }
        return properties;
    }

    @Traced(value = true, operationName = "InventoryManager.list")
    public InventoryList list() {
        return invList;
    }
    
}
