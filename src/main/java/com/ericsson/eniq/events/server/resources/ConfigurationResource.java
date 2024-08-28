/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.ericsson.eniq.events.server.resources.configuration.GroupManagementResource;

/**
 * Desrible ConfigurationResource
 *
 * @author ezhelao
 * @since 12/2011
 */
@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class ConfigurationResource {

    @EJB
    protected GroupManagementResource groupConfigurationResource;

    @Path(GROUP)
    public GroupManagementResource getGroupConfigurationResource() {
        return this.groupConfigurationResource;
    }

}
