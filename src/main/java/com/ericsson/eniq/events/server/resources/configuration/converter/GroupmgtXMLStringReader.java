/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration.converter;

import com.distocraft.dc5000.etl.importexport.GroupMgtHelper;
import com.ericsson.eniq.events.server.resources.configuration.converter.exception.InvalidFormatException;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Desrible XMLStringReader
 * if the POST request body's content-type is set to application/xml then we unmarshall it to a JAXB
 * JAVA type.
 *
 * @author ezhelao
 * @since 12/2011
 */
@Provider
@Consumes({MediaType.APPLICATION_XML, MediaType.MULTIPART_FORM_DATA})
public class GroupmgtXMLStringReader<Groupmgt> implements MessageBodyReader<Groupmgt> {

    private static final String  INVALID_XML_ERROR_MSG="Invalid Content Format";

    @Override
    public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return true;
    }

    @Override
    public Groupmgt readFrom(final Class<Groupmgt> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException, WebApplicationException {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(GroupMgtHelper.SCHEMA_PACKAGE);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Groupmgt) unmarshaller.unmarshal(new ByteArrayInputStream(getStringFromInputStream(entityStream).getBytes()));

        } catch (JAXBException e) {
            throw new InvalidFormatException(INVALID_XML_ERROR_MSG);

        }
    }

    private String getStringFromInputStream (final InputStream stream) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        final StringBuffer sb = new StringBuffer();
        String temp=null;
        while( (temp=br.readLine())!=null)
        {
            sb.append(temp);
        }
        return sb.toString();
    }
}
