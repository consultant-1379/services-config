/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration.converter.exception;

import java.io.IOException;

/**
 * Describe InvalidFormatException
 * 
 * @author ezhelao
 * @since 02/2012
 */
public class InvalidFormatException extends IOException {

    private static final long serialVersionUID = 1L;

    public InvalidFormatException(final String msg) {
        super(msg);
    }
}
