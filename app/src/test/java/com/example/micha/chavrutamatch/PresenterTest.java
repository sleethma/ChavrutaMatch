package com.example.micha.chavrutamatch;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.Http.APIModels.MyChavrutas;
import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;
import com.example.micha.chavrutamatch.MVPConstructs.Models.MainActivityModel;
import com.example.micha.chavrutamatch.MVPConstructs.Presenters.MAPresenter;
import com.example.micha.chavrutamatch.MVPConstructs.Repos.MARepoContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by micha on 3/22/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {
    private String fakeUserId = "12345";

    @Mock
    private MainActivityModel mockMainActivityModel;
    @Mock
    private MainActivity mockMainActivityView;
    @Mock
    private UserDetails mockUserDetailsInstance;
    @Mock
    private MyChavrutas mockMyChavrutas;
    @Mock
    private ServerResponse mockServerResponse;
    @Mock
    private MARepoContract mockHolder;

    private MAPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockMainActivityModel.getUserDetailsInstance()).thenReturn(mockUserDetailsInstance);
        presenter = new MAPresenter(mockMainActivityModel);
        presenter.setMAView(mockMainActivityView);
        when(mockMainActivityModel.getMyChavrutasArrayListItem(anyInt())).thenReturn(mockServerResponse);


    }

    @Test
    public void testViewTypeReturnOneIfUserIdSameAsHostId() {
        int fakePosition = 5;
        int expectedValue = 1;
        String fakeHostId = "12345";

        //arrange
        when(mockServerResponse.getHostId()).thenReturn(fakeHostId);
        when(mockUserDetailsInstance.getUserId()).thenReturn(fakeUserId);

        //act
        int actualValue = presenter.getItemViewTypeFromPresenter(fakePosition);

        //assert
        verify(mockMainActivityModel, times(1)).getMyChavrutasArrayListItem(anyInt());
        verify(mockUserDetailsInstance, times(1)).getUserId();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testAssuresViewBoundToHolderCallsAllMethodsNecessary() {
        int fakePosition = 5;
        int viewTypeHost = 1;

        //arrange
        //test sets to return data for requesterConfirmedStatus
        when(mockHostSessionData.getMchavrutaRequest1Id()).thenReturn("none");
        when(mockHostSessionData.getMchavrutaRequest2Id()).thenReturn("none");
        when(mockHostSessionData.getMchavrutaRequest3Id()).thenReturn("none");
        when(mockHostSessionData.getmChavrutaRequest1Name()).thenReturn("none");
        when(mockHostSessionData.getmChavrutaRequest2Name()).thenReturn("none");
        when(mockHostSessionData.getmChavrutaRequest3Name()).thenReturn("none");

        //act
        presenter.onBindToPresenter(mockHolder, fakePosition, viewTypeHost);

        //assert
        verify(mockMainActivityModel, times(1)).getMyChavrutasArrayListItem(fakePosition);
        verify(mockHolder, atLeastOnce()).setUsersFullName(null);
        verify(mockHolder, times(1)).setViewItemData(mockHostSessionData, fakePosition);
    }

    @Test
    public void testSetDataToAwaitingConfirmView() {
        String fakeConfirmedIdNum = "12345";
        String firstName = "first_name";
        String lastName = "last_name";
        //arrange
        when(mockHostSessionData.getmConfirmed()).thenReturn(fakeConfirmedIdNum);
        when(mockUserDetailsInstance.getUserId()).thenReturn(fakeUserId);
        when(mockHostSessionData.getmHostFirstName()).thenReturn(firstName);
        when(mockHostSessionData.getmHostLastName()).thenReturn(lastName);

        //act
        presenter.setDataToAwaitingConfirmView(mockHolder, mockHostSessionData);


        //assert
        assertEquals(fakeConfirmedIdNum, fakeUserId);
        verify(mockHolder, times(1)).setUserConfirmedStatus(true);
        verify(mockHolder, times(1)).setUsersFullName(anyString());
    }

}

