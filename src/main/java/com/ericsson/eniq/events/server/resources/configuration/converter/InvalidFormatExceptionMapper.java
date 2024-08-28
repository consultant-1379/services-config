/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration.converter;

import com.ericsson.eniq.events.server.resources.configuration.converter.exception.InvalidFormatException;
import com.ericsson.eniq.events.server.utils.json.JSONUtils;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author ezhelao
 * @since 02/2012
 */
@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException>{
    @Override
    public Response toResponse(final InvalidFormatException exception) {
        final GenericEntity<String> stringBody = new GenericEntity<String>(JSONUtils.createJSONErrorResult(exception.getMessage()), String.class);
        return Response.ok(stringBody).build();
    }
}
