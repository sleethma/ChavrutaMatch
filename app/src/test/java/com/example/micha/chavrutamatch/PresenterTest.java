package com.example.micha.chavrutamatch;

import com.example.micha.chavrutamatch.MVPConstructs.Models.MainActivityModel;
import com.example.micha.chavrutamatch.MVPConstructs.Presenters.MAPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.NotNull;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by micha on 3/22/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {
    @Mock
    MainActivityModel mockMainActivityModel;
    @Mock
    MainActivity mockMainActivityView;

    MAPresenter presenter;
    String fakeHostId = "12345";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        presenter = new MAPresenter(mockMainActivityModel);
        presenter.setMAView(mockMainActivityView);
    }

    @Test
    public void testViewTypeReturn(){
        int fakePosition = 5;
        assert(presenter.getItemViewTypeFromPresenter(5)), is(notNullValue());
    }


}
