package com.ericsson.eniq.events.server.serviceprovider.impl;

import static com.ericsson.eniq.events.server.common.UserPreferencesType.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.ericsson.eniq.events.server.common.UserPreferencesType;
import com.ericsson.eniq.events.server.common.exception.InvalidInputDataException;
import com.ericsson.eniq.events.server.query.IQueryGenerator;
import com.ericsson.eniq.events.server.query.QueryGeneratorParameters;
import com.ericsson.eniq.events.server.query.QueryParameter;
import com.ericsson.eniq.events.server.serviceprovider.UserPreferencesService;
import com.ericsson.eniq.events.server.services.DataService;
import com.ericsson.eniq.events.server.utils.json.JSONUtils;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Actual implementation of {@link com.ericsson.eniq.events.service.serviceprovider.UserPreferences}
 * @author ejedmar
 * @since 2011
 *
 */
@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
@Local(UserPreferencesService.class)
public class UserPreferencesServiceImpl implements UserPreferencesService {

    //template.csv query ids

    private static final String INSERT_PREFERENCES_PATH = "INSERT_USER_PREFERENCES";

    private static final String UPDATE_PREFERENCES_PATH = "UPDATE_USER_PREFERENCES";

    private static final String GET_PREFERENCES_BY_USERNAME_PATH = "GET_USER_PREFERENCES_BY_USERNAME";

    @EJB
    private DataService dataService;

    @EJB(beanName = "QueryGenerator")
    private IQueryGenerator queryGenerator;

    /*
     * @see com.ericsson.eniq.events.server.serviceprovider.UserPreferencesService#savePreferences(com.ericsson.eniq.events.server.serviceprovider.UserPreferencesType)
     */
    @Override
    public void savePreferences(final UserPreferencesType userPreferencesType) throws InvalidInputDataException {
        if (isValidUpdateSettingsRequest(userPreferencesType)) {
            String query = null;
            if (dataExists(userPreferencesType)) {
                query = queryGenerator.getQuery(buildQueryParams(UPDATE_PREFERENCES_PATH));
            } else {
                query = queryGenerator.getQuery(buildQueryParams(INSERT_PREFERENCES_PATH));
            }
            dataService.updateUserPreferences(query, buildUserPreferencesRecord(userPreferencesType));
        } else {
            throw new InvalidInputDataException("Invalid input data");
        }
    }

    /*
     * @see com.ericsson.eniq.events.server.serviceprovider.UserPreferencesService#getPreferencesByUsername(java.lang.String)
     */
    @Override
    public UserPreferencesType getPreferencesByUsername(final String username) {
        if (isValidGetSettingsRequest(username)) {
            final Map<String, QueryParameter> queryParams = new HashMap<String, QueryParameter>();
            queryParams.put(USERNAME_COLUMN, QueryParameter.createStringParameter(username));
            return dataService.getUserPreferences(
                    queryGenerator.getQuery(buildQueryParams(GET_PREFERENCES_BY_USERNAME_PATH)), queryParams);
        }
        throw new InvalidInputDataException("Invalid input data");
    }

    private Map<String, QueryParameter> buildUserPreferencesRecord(final UserPreferencesType userPreferencesType) {
        final Map<String, QueryParameter> queryParams = new HashMap<String, QueryParameter>();
        queryParams.put(USERNAME_COLUMN, QueryParameter.createStringParameter(userPreferencesType.getUsername()));
        queryParams.put(VERSION_COLUMN, QueryParameter.createIntParameter(userPreferencesType.getVersion()));
        queryParams.put(SETTINGS_COLUMN,
                QueryParameter.createClobParameter(userPreferencesType.getUserPreferencesData()));
        return queryParams;
    }

    private boolean dataExists(final UserPreferencesType userPreferencesType) {
        return getPreferencesByUsername(userPreferencesType.getUsername()) != null;
    }

    private QueryGeneratorParameters buildQueryParams(final String templatePath) {
        final MultivaluedMap<String, String> requestParameters = new MultivaluedMapImpl();
        final QueryGeneratorParameters queryGeneratorParameters = new QueryGeneratorParameters(templatePath,
                requestParameters, Collections.<String, Object> emptyMap(), null, null, 0, null, null, false, false);
        return queryGeneratorParameters;
    }

    public void setDataService(final DataService dataService) {
        this.dataService = dataService;
    }

    public void setQueryGenerator(final IQueryGenerator queryGenerator) {
        this.queryGenerator = queryGenerator;
    }

    @Override
    public Response getDataAsCSV(final MultivaluedMap<String, String> serviceProviderParameters,
            final HttpServletResponse response) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getData(final MultivaluedMap<String, String> serviceProviderParameters) {
        final UserPreferencesType preferencesByUsername = getPreferencesByUsername(serviceProviderParameters
                .getFirst(USERNAME_COLUMN));
        return preferencesByUsername != null ? preferencesByUsername.getUserPreferencesData() : JSONUtils
                .JSONEmptySuccessResult();
    }

    private boolean isValidGetSettingsRequest(final String username) {
        return StringUtils.isNotEmpty(username);
    }

    private boolean isValidUpdateSettingsRequest(final UserPreferencesType userPreferencesType) {
        return userPreferencesType != null && userPreferencesType.getUsername() != null
                && userPreferencesType.getVersion() != null && userPreferencesType.getUserPreferencesData() != null;
    }
}
