/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration;

import java.util.ArrayList;
import java.util.List;

import com.distocraft.dc5000.etl.importexport.gpmgt.Group;
import com.distocraft.dc5000.etl.importexport.gpmgt.GroupElement;
import com.distocraft.dc5000.etl.importexport.gpmgt.Groupmgt;
import com.distocraft.dc5000.etl.importexport.gpmgt.Key;

/**
 * Desrible GroupManagementDataProvider
 *
 * @author ezhelao
 * @since 01/2012
 */
public class GroupManagementDataProvider {
    public static Object[] provideGroupmgtObject() {
        final Groupmgt groupmgt = new Groupmgt();
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("APN");
        k.setValue("blackberry.net");
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("APN_GROUP");
        group.setType("APN");
        groupmgt.getGroup().add(group);
        final List<Groupmgt> groupmgtList = new ArrayList<Groupmgt>();
        groupmgtList.add(groupmgt);
        return groupmgtList.toArray();
    }
}
