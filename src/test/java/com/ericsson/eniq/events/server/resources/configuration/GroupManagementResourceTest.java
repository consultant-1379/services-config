/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration;

import com.distocraft.dc5000.etl.importexport.gpmgt.Groupmgt;
import com.ericsson.eniq.events.server.resources.BaseServiceIntegrationTest;
import com.ericsson.eniq.events.server.resources.configuration.converter.exception.InvalidFormatException;
import com.ericsson.eniq.events.server.test.util.JSONAssertUtils;
import com.ericsson.eniq.events.server.test.util.JSONTestUtils;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import javax.annotation.Resource;

/**
 * Desrible GroupManagementTest
 *
 * @author ezhelao
 * @since 01/2012
 */
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(locations = {"classpath:com/ericsson/eniq/events/server/resources/configuration/config-service-context.xml"})
public class GroupManagementResourceTest extends BaseServiceIntegrationTest {


    protected JSONAssertUtils jsonAssertUtils = new JSONAssertUtils();

    private TestContextManager testContextManager;


    @Before
    public void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }


    @Resource(name = "groupManagementResource")
    private GroupManagementResource groupManagementResource;

    @Test
    @Parameters(source = GroupManagementDataProvider.class)
    public void testEditGroup_add(final Groupmgt groupmgt)
            throws Exception {
        JSONTestUtils.assertJSONSucceeds(groupManagementResource.editGroup("add", groupmgt));
    }

    @Test
    @Parameters(source = GroupManagementDataProvider.class)
    public void testEditGroup_delete(final Groupmgt groupmgt)
            throws Exception {
        JSONTestUtils.assertJSONSucceeds(groupManagementResource.editGroup("delete", groupmgt));
    }

    @Test(expected = InvalidFormatException.class)
    @Parameters(source = GroupManagementInvalidIMSIDataProvider.class)
    public void testEditGroup_add_invalidIMSI(final Groupmgt groupmgt)
            throws Exception {
        groupManagementResource.editGroup("add", groupmgt);
    }

    @Test (expected = InvalidFormatException.class)
    @Parameters(source = GroupManagementInvalidIMSIDataProvider.class)
    public void testEditGroup_delete_invalidIMSI(final Groupmgt groupmgt)
            throws Exception {
        groupManagementResource.editGroup("delete", groupmgt);
    }

    @Test (expected = InvalidFormatException.class)
    @Parameters(source = GroupManagementInvalidTACDataProvider.class)
    public void testEditGroup_delete_invalidTAC(final Groupmgt groupmgt)
            throws Exception {
        groupManagementResource.editGroup("add", groupmgt);
    }

}
