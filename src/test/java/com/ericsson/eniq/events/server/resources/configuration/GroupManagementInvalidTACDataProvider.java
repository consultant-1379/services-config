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
public class  GroupManagementInvalidTACDataProvider {


    private  GroupManagementInvalidTACDataProvider(){}
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
        k.setName("TAC");
        k.setValue("sdfsdf");
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("TAC_group1");
        group.setType("TAC");
        return group;

    }

    private static Group creageGroupObject2() {
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("TAC");
        k.setValue("12312312312123123123123"); // more than 18 digits.
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("TAC_GROUP1");
        group.setType("TAC");
        return group;
    }

    private static Group creageGroupObject3() {
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("TAC");
        k.setValue("01234567"); // less than 9 digits.
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("TAC_GROUP1");
        group.setType("TAC");
        return group;
    }

    private static Group creageGroupObject4() {
        final Group group = new Group();
        final GroupElement groupElement = new GroupElement();
        final Key k = new Key();
        k.setName("TAC");
        k.setValue(""); // empty string.
        groupElement.getKey().add(k);
        group.getGroupElement().add(groupElement);
        group.setName("TAC_GROUP1");
        group.setType("TAC");
        return group;
    }


}
