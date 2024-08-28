/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.serviceprovider.impl;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Desrible ImportLogMemoryHandler
 *
 * @author ezhelao
 * @since 02/2012
 */
public class ImportLogMemoryHandler extends Handler {
    private static final String  IMPORT_FAIL_STR="IMPORT FAILED";
    private boolean  isLastOperationFail = false;
    private String errorMsg = "";

    @Override
    public void publish(final LogRecord record) {
        if (record.getLevel()== Level.WARNING)
        {
            final String logMessage =record.getMessage();
            if (logMessage!=null && logMessage.contains(IMPORT_FAIL_STR))
            {
                isLastOperationFail=true;
                errorMsg=logMessage.replaceAll("\r","").replaceAll("\n","");
            }
        }
    }

    @Override
    public void flush() {
        isLastOperationFail=false;
    }


    public void resetHandler ()
    {
        isLastOperationFail =false;
        errorMsg ="";
    }

    @Override
    public void close() throws SecurityException {
    }

    public boolean isLastOperationFail() {
        return isLastOperationFail;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
