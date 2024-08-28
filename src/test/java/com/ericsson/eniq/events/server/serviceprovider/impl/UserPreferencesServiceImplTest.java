package com.ericsson.eniq.events.server.serviceprovider.impl;

import com.ericsson.eniq.events.server.common.UserPreferencesType;
import com.ericsson.eniq.events.server.common.exception.InvalidInputDataException;
import com.ericsson.eniq.events.server.query.IQueryGenerator;
import com.ericsson.eniq.events.server.query.QueryGeneratorParameters;
import com.ericsson.eniq.events.server.services.DataService;
import com.ericsson.eniq.events.server.test.common.BaseJMockUnitTest;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

public class UserPreferencesServiceImplTest extends BaseJMockUnitTest {

    private final UserPreferencesServiceImpl userPreferencesService = new UserPreferencesServiceImpl();

    @Test
    public void testGetPreferencesByUsernameNotFound() {
        final String username = "username";
        final String query = "sql query";
        final DataService dataServiceMock = mockery.mock(DataService.class);
        final IQueryGenerator queryGeneratorMock = mockery.mock(IQueryGenerator.class);
        mockery.checking(new Expectations() {
            {
                one(queryGeneratorMock).getQuery(with(any(QueryGeneratorParameters.class)));
                will(returnValue(query));
                one(dataServiceMock).getUserPreferences(with(equal(query)), with(any(Map.class)));
                will(returnValue(null));

            }
        });
        userPreferencesService.setQueryGenerator(queryGeneratorMock);
        userPreferencesService.setDataService(dataServiceMock);
        final UserPreferencesType userPreferencesType = userPreferencesService.getPreferencesByUsername(username);
        Assert.assertThat(userPreferencesType, Matchers.nullValue());
    }

    @Test
    public void testGetPreferencesByUsernameFound() {
        final String username = "username";
        final UserPreferencesType userPreferences = new UserPreferencesType(username, 1, "{\"data\":\"some data\"");
        final String query = "sql query";
        final DataService dataServiceMock = mockery.mock(DataService.class);
        final IQueryGenerator queryGeneratorMock = mockery.mock(IQueryGenerator.class);
        mockery.checking(new Expectations() {
            {
                one(queryGeneratorMock).getQuery(with(any(QueryGeneratorParameters.class)));
                will(returnValue(query));
                one(dataServiceMock).getUserPreferences(with(equal(query)), with(any(Map.class)));
                will(returnValue(userPreferences));

            }
        });
        userPreferencesService.setQueryGenerator(queryGeneratorMock);
        userPreferencesService.setDataService(dataServiceMock);
        final UserPreferencesType userPreferencesType = userPreferencesService.getPreferencesByUsername(username);
        Assert.assertThat(userPreferencesType, Matchers.notNullValue());
        Assert.assertThat(userPreferencesType.getUsername(), Matchers.is(username));
    }

    @Test
    public void testGetPreferencesThrowsInvalidInputDataException() {
        try {
            userPreferencesService.getPreferencesByUsername(null);
            Assert.fail();
        } catch (final InvalidInputDataException e) {
        }
    }

    @Test
    public void testGetDataThrowsInvalidInputDataException() {
        try {
            final MultivaluedMap<String, String> params = new MultivaluedMapImpl();
            params.putSingle(UserPreferencesType.USERNAME_COLUMN, null);
            userPreferencesService.getData(params);
            Assert.fail();
        } catch (final InvalidInputDataException e) {
        }
    }

    @Test
    public void testSavePreferencesAsInsert() {
        final String username = "username";
        final UserPreferencesType userPreferences = new UserPreferencesType(username, 1, "{\"data\":\"some data\"");
        final String query = "sql query";
        final String insertStatement = "insert into...";
        final DataService dataServiceMock = mockery.mock(DataService.class);
        final IQueryGenerator queryGeneratorMock = mockery.mock(IQueryGenerator.class);
        mockery.checking(new Expectations() {
            {
                one(queryGeneratorMock).getQuery(with(any(QueryGeneratorParameters.class)));
                will(returnValue(query));
                one(dataServiceMock).getUserPreferences(with(equal(query)), with(any(Map.class)));
                will(returnValue(null));
                one(queryGeneratorMock).getQuery(with(any(QueryGeneratorParameters.class)));
                will(returnValue(insertStatement));
                one(dataServiceMock).updateUserPreferences(with(equal(insertStatement)), with(any(Map.class)));

            }
        });
        userPreferencesService.setQueryGenerator(queryGeneratorMock);
        userPreferencesService.setDataService(dataServiceMock);

        userPreferencesService.savePreferences(userPreferences);
    }

    @Test
    public void testSavePreferencesAsUpdate() {
        final String username = "username";
        final UserPreferencesType userPreferences = new UserPreferencesType(username, 1, "{\"data\":\"some data\"");
        final String query = "sql query";
        final String updateStatement = "update...";
        final DataService dataServiceMock = mockery.mock(DataService.class);
        final IQueryGenerator queryGeneratorMock = mockery.mock(IQueryGenerator.class);
        mockery.checking(new Expectations() {
            {
                one(queryGeneratorMock).getQuery(with(any(QueryGeneratorParameters.class)));
                will(returnValue(query));
                one(dataServiceMock).getUserPreferences(with(equal(query)), with(any(Map.class)));
                will(returnValue(userPreferences));
                one(queryGeneratorMock).getQuery(with(any(QueryGeneratorParameters.class)));
                will(returnValue(updateStatement));
                one(dataServiceMock).updateUserPreferences(with(equal(updateStatement)), with(any(Map.class)));

            }
        });
        userPreferencesService.setQueryGenerator(queryGeneratorMock);
        userPreferencesService.setDataService(dataServiceMock);

        userPreferencesService.savePreferences(userPreferences);
    }

}
