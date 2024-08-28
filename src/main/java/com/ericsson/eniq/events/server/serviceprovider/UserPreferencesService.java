package com.ericsson.eniq.events.server.serviceprovider;

import com.ericsson.eniq.events.server.common.UserPreferencesType;
import com.ericsson.eniq.events.server.common.exception.InvalidInputDataException;

import javax.ejb.Local;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Service for manipulating user preferences(reading/storing)
 * 
 * @author ejedmar
 * @since 2011
 *
 */
@Local
public interface UserPreferencesService extends Service {

    /**
     * Saves user preferences - if there is no data, a new record is created.
     * Otherwise an existing record is updated
     * @param userPreferencesType user preferences
     */
    void savePreferences(UserPreferencesType userPreferencesType) throws InvalidInputDataException;

    /**
     * Gets user preferences by username
     * @param username username whom data is to be retrieved
     * @return user preferences or null if no data found
     */
    UserPreferencesType getPreferencesByUsername(String username) throws InvalidInputDataException;

    @Override
    String getData(final MultivaluedMap<String, String> serviceProviderParameters) throws InvalidInputDataException;
}
