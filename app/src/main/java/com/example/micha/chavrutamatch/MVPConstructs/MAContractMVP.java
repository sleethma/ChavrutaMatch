package com.example.micha.chavrutamatch.MVPConstructs;


import android.support.v7.widget.RecyclerView;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.Repos.MARepoContract;

import java.util.ArrayList;

/**
 * Created by micha on 2/26/2018.
 */

public interface MAContractMVP {

    interface View {
        void sendToast(String message);

        void setToolbarUnderline();

        void setUserAvatar();

        void setMyChavrutaAdapter(ArrayList<ServerResponse> myChavrutasArrayList);

        void displayRecyclerView();

        void sendHostsConfirmationtoDb(String chavrutaId, String requesterId);

        void setOptionsMenu();

//        void logout();


    }

    interface Presenter {
        void setMAView(View view);

        void setCurrentUserAccountKit();

//        void getJsonFromServer();

        void testMVPToast();

        void setupToolbar();

        boolean isCurrentUserLoggedInToAccountKit();

        void setGsonInModel();

        void setMyChavrutaData();

        void onBindToPresenter(MARepoContract holder, int position, int viewType);

        void setViewHolderConfirmations(ServerResponse currentItem, MARepoContract holder, int requestClicked);

        int getItemViewTypeFromPresenter(int position);

        void appCleanUp();
    }


    interface Model {
        void putStringDataInSP(String key, String value);

        void requestMyChavrutas();

        void observableRequestMyChavrutas();

        boolean verifyCurrentUserDataSavedInSP();

        UserDetails getUserDetailsInstance();

        String getStringDataFromSP(String key);

        void setUserDataFromSPToModel();

        void setAllSPValuesToUserDetails();

        void initAccountKit();

        void putBooleanDataInSP(String key, boolean value);

        ServerConnect getServerConnectInstance();

        void parseJSONDataToArrayList(String jsonString);

        ServerResponse getMyChavrutasItem(int position);

        ArrayList<ServerResponse> getMyChavrutasAL();

        void setCallbackToPresenter(MAContractMVP.Presenter presenter);
    }
}
