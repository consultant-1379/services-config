/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration.converter;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Desrible JSONStringReader
 * if the POST request body's content-tpye is application/json, then we convert it to an java pojo.
 *
 * @author ezhelao
 * @since 12/2011
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class JSONStringReader<T> implements MessageBodyReader<T> {

    ObjectMapper objectMapper = new ObjectMapper();


    /**
     * we assume that any application/json request body is readable .
     *
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @return
     */
    @Override
    public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return true;
    }

    @Override
    public T readFrom(final Class<T> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException {
        final T object = objectMapper.readValue(entityStream, type);
        return object;
    }
}