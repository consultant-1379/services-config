/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.resources.configuration;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.common.ParameterPatternConstants.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.ejb.*;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jackson.map.ObjectMapper;

import com.distocraft.dc5000.etl.importexport.gpmgt.*;
import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.resources.AbstractResource;
import com.ericsson.eniq.events.server.resources.configuration.converter.exception.InvalidFormatException;
import com.ericsson.eniq.events.server.serviceprovider.Service;
import com.ericsson.eniq.events.server.serviceprovider.impl.GroupManagementService;
import com.ericsson.eniq.events.server.utils.RATDescriptionMappingUtils;
import com.ericsson.eniq.events.server.utils.json.JSONUtils;
import com.sun.jersey.core.header.ContentDisposition;

/**
 * Desrible GroupManagementResource
 * 
 * @author ezhelao
 * @since 12/2011
 */
@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class GroupManagementResource extends AbstractResource {

    private static final String ADD_ACTION = "add";

    private static final String DELETE_ACTION = "delete";

    private static final String INVALID_QUERY_PARAM_ERROR_MSG = "Query param is invalid";

    private static final String QUERY_PARAM_ACTION = "action";

    private static final String QUERY_PARAM_TYPE = "type";

    private static final String JSON_FILE_NAME = "group-definition.json";

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String XML_FILE = "XML";

    private static final String ANY_STRING_PATTERN = ".*";

    private static final String JSON_FILE = "JSON";

    private static final String NULL_POINTER_ERROR_MSG = "Error while checking the uploaded content";

    private static final String INVALID_PATTERN_ERROR_MSG = "Error while checking the uploaded content";

    private static final String INVALID_IMSI_ERROR_MSG = "Invalid IMSI value found. IMSI can be digits with maximum length of 18.";

    private static final String INVALID_TAC_ERROR_MSG = "Invalid TAC value found. TAC can be digits with maxiumn length of 9";

    private static final String INVALID_GROUPNAME_ERROR_MSG = "Each group must start with an alphabetic character; it can only contain alphanumeric character, underscores and/or hyphens with a maximum with name length of 35 characters.";

    private static final String INVALID_KEY_ERROR_MSG = "Key Name or Key Value attribute can contain any combination "
            + "of up to 26 alphanumerical characters, can include any of the 4 following symbols (: . _ - ).";

    @EJB
    protected GroupManagementService groupManagementService;

    @EJB
    //used by some of the JSON transformers
    protected RATDescriptionMappingUtils ratDescriptionMappingsUtils;

    @Override
    protected Service getService() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getData() throws WebApplicationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response getDataAsCSV() throws WebApplicationException {
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String editGroup(@QueryParam(QUERY_PARAM_ACTION) final String action, final Groupmgt groupmgt) throws InvalidFormatException {
        validateGroupValue(groupmgt);
        //The name entered in UI should be stored in datatbase .Hence commenting the below line
        //convertGroupNameToLowerCase(groupmgt);
        convertRATIdToRATDesc(groupmgt);
        if (action.equals(ADD_ACTION)) {
            return groupManagementService.appendGroups(groupmgt);
        } else if (action.equals(DELETE_ACTION)) {
            return groupManagementService.deleteGroups(groupmgt);
        }
        return JSONUtils.createJSONErrorResult(INVALID_QUERY_PARAM_ERROR_MSG);
    }

    @GET
    @Path(JSON_FILE)
    public Response downloadGroupsJSON(@QueryParam(QUERY_PARAM_TYPE) final List<String> filters, @Context final HttpServletResponse response1)
            throws IOException {
        final Groupmgt groupmgt = groupManagementService.downloadSortedGroups(filters);
        convertRATDescToRATId(groupmgt);
        final OutputStream outputStream = response1.getOutputStream();
        objectMapper.writeValue(outputStream, groupmgt);
        if (outputStream != null) {
            outputStream.close();
        }
        final ContentDisposition contentDisposition = ContentDisposition.type("application").fileName(JSON_FILE_NAME).build();
        return Response.ok(null).header("Content-Disposition", contentDisposition).header("Cache-control", "private")
                .header("Cache-Control", "must-revalidate, post-check=0, pre-check=0").header("Content-Description", "File Transfer")
                .header("Expires", "0").build();

    }

    @GET
    @Path(XML_FILE)
    @Produces(MediaType.APPLICATION_XML)
    public Groupmgt downloadGroupsXML(@QueryParam(QUERY_PARAM_TYPE) final List<String> filters) {
        return groupManagementService.downloadSortedGroups(filters);
    }

    /**
     * given a groupmgt object, we iterator over all the elements of keys of groups. Update the rat value from RATDescription to RAT integer
     * 
     * @param groupmgt
     */
    private void convertRATIdToRATDesc(final Groupmgt groupmgt) {
        final Iterator<Group> groupIterator = groupmgt.getGroup().iterator();
        while (groupIterator.hasNext()) {
            final Group g = groupIterator.next();
            if (g.getType().equals(GROUP_TYPE_HIER1) || g.getType().equals(GROUP_TYPE_HIER3)) {
                final Iterator<GroupElement> groupElementIterator = g.getGroupElement().iterator();
                while (groupElementIterator.hasNext()) {
                    final GroupElement groupElement = groupElementIterator.next();
                    final Iterator<Key> keyIterator = groupElement.getKey().iterator();
                    while (keyIterator.hasNext()) {
                        final Key key = keyIterator.next();
                        if (key.getName().equals(RAT_COLUMN_NAME)) {
                            key.setValue(ratDescriptionMappingsUtils.getRATIntegerValue(key.getValue()));
                        }
                    }
                }
            }
        }
    }

    /**
     * given a groupmgt object, we iterator over all the elements of keys of groups. Update the rat value from RAT Integer to RAT Desc
     * 
     * @param groupmgt
     */
    private void convertRATDescToRATId(final Groupmgt groupmgt) {
        final Iterator<Group> groupIterator = groupmgt.getGroup().iterator();
        while (groupIterator.hasNext()) {
            final Group g = groupIterator.next();
            if (g.getType().equals(GROUP_TYPE_HIER1) || g.getType().equals(GROUP_TYPE_HIER3)) {
                final Iterator<GroupElement> groupElementIterator = g.getGroupElement().iterator();
                while (groupElementIterator.hasNext()) {
                    final GroupElement groupElement = groupElementIterator.next();
                    final Iterator<Key> keyIterator = groupElement.getKey().iterator();
                    while (keyIterator.hasNext()) {
                        final Key key = keyIterator.next();
                        if (key.getName().equals(RAT_COLUMN_NAME)) {
                            key.setValue(ratDescriptionMappingsUtils.getRATDescription(key.getValue()));
                        }
                    }
                }
            }
        }
    }

    /*

    */
    private void validateGroupValue(final Groupmgt groupmgt) throws InvalidFormatException {

        for (Group group : groupmgt.getGroup()) {
            if (group.getName() != null) {
                if (!group.getName().matches(GROUPNAME_PATTERN)) {
                    throw new InvalidFormatException(INVALID_GROUPNAME_ERROR_MSG);
                }
            }
        }

        try {
            try {
                checkGroupElement(groupmgt, ANY_STRING_PATTERN, ANY_STRING_PATTERN, IMPORTED_FILE_KEY_PATTERN, IMPORTED_FILE_KEY_PATTERN);
            } catch (InvalidFormatException ex) {
                throw getExceptionToThrow(INVALID_KEY_ERROR_MSG);
            }

            try {
                checkGroupElement(groupmgt, TYPE_IMSI, TYPE_IMSI, ANY_STRING_PATTERN, IMSI_PATTERN);
            } catch (InvalidFormatException ex) {
                throw getExceptionToThrow(INVALID_IMSI_ERROR_MSG);
            }

            try {
                checkGroupElement(groupmgt, TYPE_TAC, TYPE_TAC, ANY_STRING_PATTERN, TAC_GROUP_ELEMENT_PATTERN);
            } catch (InvalidFormatException ex) {
                throw getExceptionToThrow(INVALID_TAC_ERROR_MSG);
            }
        } catch (NullPointerException ex) {
            ServicesLogger.error("GroupManagementResource", "validateGroupValue", ex);
            throw getExceptionToThrow(NULL_POINTER_ERROR_MSG);

        } catch (PatternSyntaxException ex) {
            ServicesLogger.error("GroupManagementResource", "validateGroupValue", ex);
            throw getExceptionToThrow(INVALID_PATTERN_ERROR_MSG);

        }

    }

    /*
     * Method that creates a new InvalidFormatException, including the String message
     */
    private InvalidFormatException getExceptionToThrow(final String message) {
        return new InvalidFormatException(message);
    }

    /**
     * @param groupmgt
     * @param groupTypeFilter filter by group name , groups that have the desired type are checked.
     * @param keyNameFilter filter by key name . keys that have the disired name are checked.
     * @param keyNameRegex check key name against the pattern.
     * @param keyValueRegex check key value aginst the pattern.
     * @throws InvalidFormatException
     */
    private void checkGroupElement(final Groupmgt groupmgt, final String groupTypeFilter, final String keyNameFilter, final String keyNameRegex,
                                   final String keyValueRegex) throws InvalidFormatException {
        for (Group group : groupmgt.getGroup()) {
            if (group.getType().toLowerCase().matches(groupTypeFilter.toLowerCase())) {
                for (GroupElement groupElement : group.getGroupElement()) {
                    for (Key key : groupElement.getKey()) {
                        if (key.getName().toLowerCase().matches(keyNameFilter.toLowerCase())) {
                            if (!key.getName().matches(keyNameRegex)) {
                                throw new InvalidFormatException("Key name");

                            }
                            if (!key.getValue().matches(keyValueRegex)) {
                                throw new InvalidFormatException("Key name");

                            }
                        }
                    }
                }
            }
        }
    }

}
