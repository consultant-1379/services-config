/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration;

import com.distocraft.dc5000.etl.importexport.gpmgt.Group;
import com.distocraft.dc5000.etl.importexport.gpmgt.GroupElement;
import com.distocraft.dc5000.etl.importexport.gpmgt.Groupmgt;
import com.distocraft.dc5000.etl.importexport.gpmgt.Key;

import java.util.ArrayList;
import java.util.List;

/**
 * Desrible GroupManagementDataProvider
 *
 * @author ezhelao
 * @since 03/2012
 */
public class GroupManagementInvalidIMSIDataProvider {


    private  GroupManagementInvalidIMSIDataProvider(){}
    public static Object[] provideGroupmgtObject() {
        final List<Groupmgt> groupmgtList = new ArrayList<Groupmgt>();
        groupmgtList.add(createGroupmgtObject1());

        return groupmgtList.toArray();
    }

    private static Groupmgt createGroupmgtObject1() {
        final Groupmgt groupmgt = new Groupmgt();
        groupmgt.getGroup().add(creageGroupObject1());
        groupmgt.getGroup().add(creageGroupObject2());
        groupmgt.getGroup().add(creageGroupObject3());
        groupmgt.getGroup().add(creageGroupObject4());
        return groupmgt;
    }

    private static Group creageGroupObject1() {
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("TA232");
        k.setValue("sdfsdf");
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("IMSI_GROUP1");
        group.setType("IMSI");
        return group;

    }

    private static Group creageGroupObject2() {
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("IMSI");
        k.setValue("12312312312123123123123"); // more than 18 digits.
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("IMSI_GROUP1");
        group.setType("IMSI");
        return group;
    }

    private static Group creageGroupObject3() {
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("IMSI");
        k.setValue("12312312313"); // less than 18 digits.
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("IMSI_GROUP1");
        group.setType("IMSI");
        return group;
    }

    private static Group creageGroupObject4() {
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("IMSI");
        k.setValue(""); // empty string.
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("IMSI_GROUP1");
        group.setType("IMSI");
        return group;
    }


}
