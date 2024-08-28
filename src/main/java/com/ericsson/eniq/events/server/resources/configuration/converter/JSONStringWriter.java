package com.ericsson.eniq.events.server.resources.configuration.converter;

import com.ericsson.eniq.events.server.common.exception.GroupDownloadException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * User: ekurshi
 * Date: 13/12/11
 */

public class JSONStringWriter<Groupmgt> implements MessageBodyWriter<Groupmgt> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DOWNLOAD_ERROR_MSG = "Fail to download groups. Please check glassfish log.";


    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(final Groupmgt groupmgt, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final Groupmgt groupmgt, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        httpHeaders.add("Content-Disposition", "attachment; filename=group-definitions.json");
        if (groupmgt == null) {
            throw new GroupDownloadException(DOWNLOAD_ERROR_MSG);
        }
        objectMapper.writeValue(entityStream, groupmgt);
    }
}
