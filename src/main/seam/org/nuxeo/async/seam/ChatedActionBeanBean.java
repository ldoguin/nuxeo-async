/*
 * Copyright (c) 2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent Doguin
 */
package org.nuxeo.async.seam;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.runtime.api.Framework;

@Name("chatedActionBean")
@Scope(ScopeType.APPLICATION)
public class ChatedActionBeanBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(ChatedActionBeanBean.class);

    public static final String CHATED_ENDPOINT = Framework.getProperty("chated.endpoint.url");

    @In(create = true, required = false)
    protected NuxeoPrincipal currentNuxeoPrincipal;

    @Factory(value="chatedEndpoint", scope=ScopeType.APPLICATION)
    public String getChatedEndpoint() throws Exception {
        return CHATED_ENDPOINT;
    }

}
