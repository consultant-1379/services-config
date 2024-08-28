package com.ericsson.eniq.events.server.resources.configuration.converter;

import com.distocraft.dc5000.etl.importexport.GroupMgtHelper;
import com.ericsson.eniq.events.server.common.exception.GroupDownloadException;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * User: ekurshi
 * Date: 13/12/11
 */
@Provider
@Produces(MediaType.APPLICATION_XML)
public class XMLStringWriter<Groupmgt> implements MessageBodyWriter<Groupmgt> {
    private static final String WRITE_ERROR_MSG = "Fail to write to file.";
    private static final String DOWNLOAD_ERROR_MSG = "Fail to download groups. Please check glassfish log.";


    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return true;
    }


    /*
         returning -1 means we can't determine the size of the file.
     */
    @Override
    public long getSize(final Groupmgt groupmgt, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final Groupmgt groupmgt, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        httpHeaders.add("Content-Disposition", "attachment; filename=group-definitions.xml");

        try {
            if (groupmgt == null) {
                throw new GroupDownloadException(DOWNLOAD_ERROR_MSG);
            }
            final JAXBContext jaxbContext = JAXBContext.newInstance(GroupMgtHelper.SCHEMA_PACKAGE);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(groupmgt, entityStream);
        } catch (JAXBException e) {
            throw new GroupDownloadException(WRITE_ERROR_MSG, e);
        }


    }
}
