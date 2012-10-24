/*
 * (C) Copyright 2006-2012 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Laurent Doguin
 */

package org.nuxeo.async.listener;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

/**
 * Following Listener will forward events to Vertx event bus.
 * 
 * @author ldoguin
 */
public class SendEventToVertxBus implements EventListener {

    public static final String JSON_MESSAGE = "{\"docid\":\"%s\",\"eventName\":\"%s\",\"eventDate\":\"%s\",\"userId\":\"%s\"}";

    public static final String NX_IN_ENDPOINT = Framework.getProperty("nxin.endpoint.url");

    public void handleEvent(Event event) throws ClientException {
        EventContext ec = event.getContext();
        if (ec instanceof DocumentEventContext) {
            DocumentEventContext dec = (DocumentEventContext) ec;
            DocumentModel source = dec.getSourceDocument();
            try {
                String message = String.format(JSON_MESSAGE, source.getId(),
                        event.getName(), ""+event.getTime(),
                        dec.getPrincipal().getName());
                PostMethod post = new PostMethod(NX_IN_ENDPOINT);
                StringRequestEntity input = new StringRequestEntity(message,
                        "application/json", "utf-8");
                post.setRequestEntity(input);
                HttpClient client = new HttpClient();
                int status = client.executeMethod(post);
                String response = post.getResponseBodyAsString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
