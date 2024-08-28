package com.ericsson.eniq.events.server.resources.user;

import static com.ericsson.eniq.events.server.common.ApplicationConstants.*;
import static com.ericsson.eniq.events.server.utils.json.JSONUtils.*;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.ericsson.eniq.events.server.common.UserPreferencesType;
import com.ericsson.eniq.events.server.common.exception.InvalidInputDataException;
import com.ericsson.eniq.events.server.resources.AbstractResource;
import com.ericsson.eniq.events.server.serviceprovider.Service;
import com.ericsson.eniq.events.server.serviceprovider.UserPreferencesService;
import com.ericsson.eniq.events.server.utils.json.JSONUtils;
import com.sun.jersey.api.representation.Form;

/**
 * Restful resource for manipulating user preferences.
 * 
 * @author ejedmar
 * @since 2011
 *
 */
@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class UserPreferencesResource extends AbstractResource {

    @EJB
    private UserPreferencesService userPreferencesService;

    @Override
    protected Service getService() {
        return userPreferencesService;
    }

    /** 
     * Updates user preferences
     * @param form input parameters(post)
     * @return a success JSON response or an error JSON response if something went wrong
     */
    @Path(USER_SETTINGS)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUserPreferences(final Form form) {
        try {
            userPreferencesService.savePreferences(buildUserPreferencesType(form));
            return JSONUtils.JSONEmptySuccessResult();
        } catch (final InvalidInputDataException e) {
            return jsonErrorInputMsg();
        } catch (final Exception e) {
            return JSONUtils.createJSONErrorResult(e.getMessage());
        }
    }

    /**
     * Gets user preferences
     * @see com.ericsson.eniq.events.server.resources.AbstractResource#getData()
     */
    @Override
    @GET
    @Path(USER_SETTINGS)
    @Produces(MediaType.APPLICATION_JSON)
    public String getData() throws WebApplicationException {
        try {
            return super.getData();
        } catch (final InvalidInputDataException e) {
            throw new WebApplicationException(e);
        }
    }

    private UserPreferencesType buildUserPreferencesType(final Form form) {
        final String version = form.getFirst(UserPreferencesType.VERSION_COLUMN);
        final Integer versionInt = version != null ? Integer.valueOf(version) : null;
        final UserPreferencesType userPreferencesType = new UserPreferencesType(
                form.getFirst(UserPreferencesType.USERNAME_COLUMN), versionInt,
                form.getFirst(UserPreferencesType.SETTINGS_COLUMN));
        return userPreferencesType;
    }

}
