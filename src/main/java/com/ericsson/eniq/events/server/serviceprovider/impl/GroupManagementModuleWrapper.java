/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.serviceprovider.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBException;

import ssc.rockfactory.RockException;

import com.distocraft.dc5000.etl.importexport.GroupMgtExporter;
import com.distocraft.dc5000.etl.importexport.GroupMgtImporter;
import com.distocraft.dc5000.etl.importexport.gpmgt.Groupmgt;

/**
 * Desrible GroupManagementModuleWrapper
 *
 * @author ezhelao
 * @since 01/2012
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.WRITE)
public class GroupManagementModuleWrapper {
    private static final String GROUP_MANAGEMENT_LOGGER_NAME = "gpmgt.import";

    final private Logger logger = Logger.getLogger(GROUP_MANAGEMENT_LOGGER_NAME);

    private ImportLogMemoryHandler handler;

    private boolean isLastOperationFail;

    private String errorMsg;

    public void appendGroups(final Groupmgt groupmgt) throws RockException, IOException, SQLException,
            NoSuchAlgorithmException {
        handler = new ImportLogMemoryHandler();
        resetFlags();
        handler.resetHandler();
        logger.addHandler(handler);
        GroupMgtImporter.appendGroups(null, groupmgt);
        isLastOperationFail = handler.isLastOperationFail();
        errorMsg = handler.getErrorMsg();
        logger.removeHandler(handler);
    }

    public void deleteGroups(final Groupmgt groupmgt) throws RockException, IOException, SQLException,
            NoSuchAlgorithmException {
        handler = new ImportLogMemoryHandler();
        resetFlags();
        handler.resetHandler();
        logger.addHandler(handler);
        GroupMgtImporter.deleteGroups(null, groupmgt);
        isLastOperationFail = handler.isLastOperationFail();
        errorMsg = handler.getErrorMsg();
        logger.removeHandler(handler);
    }

    public Groupmgt exportGroups(final Map<String, List<String>> groupFilters) throws JAXBException, RockException,
            IOException, SQLException, NoSuchAlgorithmException {
        handler = new ImportLogMemoryHandler();
        resetFlags();
        handler.resetHandler();
        logger.addHandler(handler);
        final Groupmgt groupmgt = GroupMgtExporter.exportGroups(null, groupFilters);
        isLastOperationFail = handler.isLastOperationFail();
        errorMsg = handler.getErrorMsg();
        logger.removeHandler(handler);
        return groupmgt;
    }

    @Lock(LockType.READ)
    public boolean isLastOperationFail() {
        return isLastOperationFail;
    }

    @Lock(LockType.READ)
    public String getErrorMsg() {
        return errorMsg;
    }

    private void resetFlags() {
        isLastOperationFail = false;
        errorMsg = "";
    }

}
