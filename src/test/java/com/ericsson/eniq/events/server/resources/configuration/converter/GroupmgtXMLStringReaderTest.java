/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration.converter;

import com.distocraft.dc5000.etl.importexport.gpmgt.Groupmgt;
import org.junit.Before;
import org.junit.Test;
import ssc.rockfactory.RockException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Desrible GroupmgtXMLStringReaderTest
 *
 * @author ezhelao
 * @since 12/2011
 */
public class GroupmgtXMLStringReaderTest {


    GroupmgtXMLStringReader<Groupmgt> groupmgtXMLStringReader;

    @Before
    public void setUp() {
        groupmgtXMLStringReader = new GroupmgtXMLStringReader();
    }

    @Test
    public void testUnmarshallToObject() throws IOException, RockException, SQLException, NoSuchAlgorithmException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(GROUP_DEFINITION_XML.getBytes());
        //Groupmgt groupmgt= groupmgtXMLStringReader.readFrom(null,null,null, MediaType.APPLICATION_XML_TYPE,null,byteArrayInputStream);
        final Groupmgt groupmgt = groupmgtXMLStringReader.readFrom(null, null, null, null, null, byteArrayInputStream);
        assertEquals(3, groupmgt.getGroup().get(0).getGroupElement().size());
        assertEquals("groupname1", groupmgt.getGroup().get(0).getName());
        assertEquals("typeapn", groupmgt.getGroup().get(0).getType());

    }

    @Test
    public void testUnmarshallToObject_NewLine() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(GROUP_DEFINITION_XML_1.getBytes());
        //Groupmgt groupmgt= groupmgtXMLStringReader.readFrom(null,null,null, MediaType.APPLICATION_XML_TYPE,null,byteArrayInputStream);
        final Groupmgt groupmgt = groupmgtXMLStringReader.readFrom(null, null, null, null, null, byteArrayInputStream);
        assertEquals(3, groupmgt.getGroup().get(0).getGroupElement().size());
        assertEquals("APN_1",groupmgt.getGroup().get(0).getGroupElement().get(0).getKey().get(0).getValue());
        assertEquals("APN_2",groupmgt.getGroup().get(0).getGroupElement().get(1).getKey().get(0).getValue());
        assertEquals("APN_3",groupmgt.getGroup().get(0).getGroupElement().get(2).getKey().get(0).getValue());
        assertFalse(groupmgt.getGroup().get(0).getGroupElement().get(0).getKey().get(0).getValue().contains("\n"));

    }


    @Test
    public void testUnmarshallToObject_SpecialChar() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(GROUP_DEFINITION_XML_2.getBytes());
        //Groupmgt groupmgt= groupmgtXMLStringReader.readFrom(null,null,null, MediaType.APPLICATION_XML_TYPE,null,byteArrayInputStream);
        final Groupmgt groupmgt = groupmgtXMLStringReader.readFrom(null, null, null, null, null, byteArrayInputStream);
        assertEquals(3, groupmgt.getGroup().get(0).getGroupElement().size());
        assertEquals("\\APN_1",groupmgt.getGroup().get(0).getGroupElement().get(0).getKey().get(0).getValue());

    }


    private static final String GROUP_DEFINITION_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<groupmgt>\n" +
            "\t<group name=\"groupname1\" type=\"typeapn\">\n" +
            "\t\t<group-element>\n" +
            "\t\t\t<key value=\"[APN value]\" name=\"APN\"/>\n" +
            "\t\t</group-element>\n" +
            "\t\t<group-element>\n" +
            "\t\t\t<key value=\"[Another APN value]\" name=\"APN\"/>\n" +
            "\t\t</group-element>\n" +
            "\t\t<group-element>\n" +
            "\t\t\t<key value=\"[Another APN value]\" name=\"APN\"/>\n" +
            "\t\t</group-element>\n" +
            "\t</group>\n" +
            "</groupmgt>";

     private static final String GROUP_DEFINITION_XML_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<groupmgt>\n" +
            "\t<group name=\"groupname1\" type=\"typeapn\">\n" +
            "\t\t<group-element>\n" +
            "\t\t\t<key value=\"\n\rAPN_1\n\r\" name=\"APN\"/>\n" +
            "\t\t</group-element>\n" +
            "\t\t<group-element>\n" +
            "\t\t\t<key value=\"APN_\n2\" name=\"APN\"/>\n" +
            "\t\t</group-element>\n" +
            "\t\t<group-element>\n" +
            "\t\t\t<key value=\"APN_\r3\" name=\"APN\"/>\n" +
            "\t\t</group-element>\n" +
            "\t</group>\n" +
            "</groupmgt>";

    private static final String GROUP_DEFINITION_XML_2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
           "<groupmgt>\n" +
           "\t<group name=\"groupname1\" type=\"typeapn\">\n" +
           "\t\t<group-element>\n" +
           "\t\t\t<key value=\"\\APN_1\" name=\"APN\"/>\n" +
           "\t\t</group-element>\n" +
           "\t\t<group-element>\n" +
           "\t\t\t<key value=\"APN_\n2\" name=\"APN\"/>\n" +
           "\t\t</group-element>\n" +
           "\t\t<group-element>\n" +
           "\t\t\t<key value=\"APN_\r3\" name=\"APN\"/>\n" +
           "\t\t</group-element>\n" +
           "\t</group>\n" +
           "</groupmgt>";

}
