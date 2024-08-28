/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration;

import com.distocraft.dc5000.etl.importexport.gpmgt.Groupmgt;
import com.ericsson.eniq.events.server.serviceprovider.impl.GroupManagementService;
import com.ericsson.eniq.events.server.utils.json.JSONUtils;

import java.util.List;

/**
 * Desrible MockGroupManagementService
 *
 * @author ezhelao
 * @since 01/2012
 */
public class MockGroupManagementService extends GroupManagementService {
    @Override
    public String appendGroups(final Groupmgt groupmgt) {
        if (groupmgt!=null)
        {
            return JSONUtils.JSONEmptySuccessResult();
        }
        return  JSONUtils.createBISSOAPClientJSONErrorResult("error");
    }

    @Override
    public String deleteGroups(final Groupmgt groupmgt) {
        if (groupmgt!=null)
        {
            return JSONUtils.JSONEmptySuccessResult();
        }
        return  JSONUtils.createBISSOAPClientJSONErrorResult("error");
    }

    @Override
    public Groupmgt downloadSortedGroups(final List<String> toExport) {
        return new Groupmgt();
    }
}
