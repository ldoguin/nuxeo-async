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

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Json;

/**
 * Following Listener will forward events to Vertx event bus.
 * 
 * @author ldoguin
 */
public class SendEventToVertxBus implements EventListener {

	public void handleEvent(Event event) throws ClientException {
		EventContext ec = event.getContext();
		if (ec instanceof DocumentEventContext) {
			DocumentEventContext dec = (DocumentEventContext) ec;
			DocumentModel source = dec.getSourceDocument();
			Buffer buff = new Buffer();
			JsonObject message = new JsonObject().putString("docId",
					source.getId()).putString("docTitle", source.getTitle()).putString("type", "publish")
					.putString("address", "nuxeo.eventbus");
			JsonObject obj = new JsonObject().putString("type", "publish")
					.putString("address", "nuxeo.eventbus")
					.putObject("body", message);	
			buff.appendString(Json.encode(obj));
			HttpClient client = Vertx.newVertx().createHttpClient()
					.setHost("localhost").setPort(8082);
			HttpClientRequest request = client.post("/nuxeoevent/",
					new Handler<HttpClientResponse>() {
						public void handle(HttpClientResponse resp) {

						}
					});
			
			request.end(buff);
			client.close();

		}
	}

}
