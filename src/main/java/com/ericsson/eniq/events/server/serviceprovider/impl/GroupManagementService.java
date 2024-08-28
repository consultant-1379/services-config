/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.serviceprovider.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ssc.rockfactory.RockException;

import com.distocraft.dc5000.etl.importexport.gpmgt.*;
import com.distocraft.dc5000.etl.importexport.gpmgt.exception.GroupMgtException;
import com.ericsson.eniq.events.server.common.exception.GroupDownloadException;
import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.resources.configuration.GroupManagementResource;
import com.ericsson.eniq.events.server.utils.json.JSONUtils;

/**
 * Desrible GroupManagementService
 * 
 * @author ezhelao
 * @since 12/2011
 */
@Stateless
public class GroupManagementService {

    private static final String INTERNAL_ERROR_MSG = "internal error,please check glassfish log";

    private static final String SQL_ERROR_MSG = "error when trying to access the database";

    private static final String IO_ERROR_MSG = "import export module can not access files";

    private static final String GROUP_MANAGEMENT_ERROR_MSG = "group management module error";

    private static final String UNKNOWN_ERROR_MSG = "unknown error";

    private static final String FILTERS_PARSING_ERROR_MSG = "Invalid group filter.";

    @EJB
    protected GroupManagementModuleWrapper groupManagementModuleWrapper;

    public String appendGroups(final Groupmgt groupmgt) {
        try {
            groupManagementModuleWrapper.appendGroups(groupmgt);
            if (groupManagementModuleWrapper.isLastOperationFail()) {
                return JSONUtils.createJSONErrorResult(groupManagementModuleWrapper.getErrorMsg());
            }
            return JSONUtils.JSONEmptySuccessResult();
        } catch (final RockException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(INTERNAL_ERROR_MSG);
        } catch (final NoSuchAlgorithmException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(INTERNAL_ERROR_MSG);
        } catch (final SQLException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(SQL_ERROR_MSG);
        } catch (final IOException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(IO_ERROR_MSG);
        } catch (final GroupMgtException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(GROUP_MANAGEMENT_ERROR_MSG);
        } catch (final Exception e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(UNKNOWN_ERROR_MSG);
        }

    }

    public String deleteGroups(final Groupmgt groupmgt) {
        try {
            groupManagementModuleWrapper.deleteGroups(groupmgt);
            if (groupManagementModuleWrapper.isLastOperationFail()) {
                return JSONUtils.createJSONErrorResult(groupManagementModuleWrapper.getErrorMsg());
            }
            return JSONUtils.JSONEmptySuccessResult();
        } catch (final RockException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "deleteGroup", e);
            return JSONUtils.createJSONErrorResult(INTERNAL_ERROR_MSG);
        } catch (final NoSuchAlgorithmException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "deleteGroup", e);
            return JSONUtils.createJSONErrorResult(INTERNAL_ERROR_MSG);
        } catch (final SQLException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "deleteGroup", e);
            return JSONUtils.createJSONErrorResult(SQL_ERROR_MSG);
        } catch (final IOException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "deleteGroup", e);
            return JSONUtils.createJSONErrorResult(IO_ERROR_MSG);
        } catch (final GroupMgtException e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(GROUP_MANAGEMENT_ERROR_MSG);
        } catch (final Exception e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "appendGroup", e);
            return JSONUtils.createJSONErrorResult(UNKNOWN_ERROR_MSG);
        }

    }

    public Groupmgt downloadSortedGroups(final List<String> toExport) {
        try {
            final Map<String, List<String>> groupFilters = convertFilter(toExport);
            final Groupmgt groupmgt = groupManagementModuleWrapper.exportGroups(groupFilters);
            sortGroupAndGroupElement(groupmgt);
            return filterGroup(groupFilters, groupmgt);
        } catch (final Exception e) {
            ServicesLogger.error(GroupManagementService.class.getName(), "downloadSortedGroups", e);
        }
        return null;
    }

    private Groupmgt filterGroup(final Map<String, List<String>> groupFilter, final Groupmgt groupmgt) {
        final Groupmgt newGroupmgt = new Groupmgt();
        for (final Group g : groupmgt.getGroup()) {
            final Set<String> types = groupFilter.keySet();

            try {
                if (types.contains(g.getType()) && groupFilter.get(g.getType()).contains(g.getName())) {
                    newGroupmgt.getGroup().add(g);
                }
            } catch (final Exception ex) {
                newGroupmgt.getGroup().add(g);
            }
        }
        return newGroupmgt;
    }

    /**
     * this function will do the param conversion job .
     * 
     * @param filters a list of string from url param. for example ?type=APN&type=IMSI
     * @return the type that the group management module expected
     */
    private Map<String, List<String>> convertFilter(final List<String> filters) {
        final Map<String, List<String>> toExport = new HashMap<String, List<String>>();
        try {
            if (filters != null && filters.size() > 0) {
                for (final String filter : filters) {
                    final int pre = filter.indexOf(':');
                    String type;
                    final List<String> gpNames = new ArrayList<String>();//NOPMD
                    if (pre == -1) {
                        type = filter.trim().toUpperCase(Locale.getDefault());
                    } else {
                        type = filter.substring(0, pre).trim().toUpperCase(Locale.getDefault());
                        final String names = filter.substring(pre + 1);
                        final StringTokenizer st = new StringTokenizer(names, ",");//NOPMD
                        while (st.hasMoreTokens()) {
                            gpNames.add(st.nextToken().trim());
                        }
                    }
                    if (toExport.containsKey(type)) {
                        toExport.get(type).addAll(gpNames);
                    } else {
                        toExport.put(type, gpNames);
                    }
                }
            }
            return toExport;
        } catch (final Exception e) {
            ServicesLogger.error(GroupManagementResource.class.getName(), "convertFilter", FILTERS_PARSING_ERROR_MSG);
            throw new GroupDownloadException(FILTERS_PARSING_ERROR_MSG, e);
        }

    }

    /**
     * to sort all group and all element in group in alphabetical order.
     */
    private void sortGroupAndGroupElement(final Groupmgt groupmgt) {
        final Iterator<Group> groupIterator = groupmgt.getGroup().iterator();
        while (groupIterator.hasNext()) {
            final Group group = groupIterator.next();
            Collections.sort(group.getGroupElement(), new GroupElementSortImpl());//NOPMD
        }
        Collections.sort(groupmgt.getGroup(), new GroupSortImpl());
    }

    class GroupSortImpl implements Comparator<com.distocraft.dc5000.etl.importexport.gpmgt.Group> {
        @Override
        public int compare(final Group g1, final Group g2) {
            if (g1 != null && g2 != null && g1.getName() != null && g2.getName() != null) {
                return g1.getName().compareTo(g2.getName());
            }
            return 0;
        }
    }

    /**
     * group element comparator
     */
    class GroupElementSortImpl implements Comparator<GroupElement> {

        @Override
        public int compare(final GroupElement element1, final GroupElement element2) {

            final List<Key> keysElement1 = element1.getKey();
            final List<Key> keysElement2 = element2.getKey();
            if (keysElement1 != null && keysElement2 != null) {
                final int smallerIndex = keysElement1.size() < keysElement2.size() ? keysElement1.size() : keysElement2.size();
                for (int i = 0; i < smallerIndex; i++) {
                    final String nameKeyElement1 = keysElement1.get(i).getValue();
                    final String nameKeyElement2 = keysElement2.get(i).getValue();
                    if (areNotEqual(nameKeyElement1, nameKeyElement2)) {
                        return nameKeyElement1.compareTo(nameKeyElement2);
                    }
                }
                return keysElement1.size() - keysElement2.size();
            }
            return 0;
        }

        private boolean areNotEqual(final String nameKeyElement1, final String nameKeyElement2) {
            return nameKeyElement1 != null && nameKeyElement2 != null && nameKeyElement1.compareTo(nameKeyElement2) != 0;
        }
    }
}
