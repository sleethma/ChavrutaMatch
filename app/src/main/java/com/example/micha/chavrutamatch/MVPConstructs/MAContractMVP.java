package com.example.micha.chavrutamatch.MVPConstructs;


import com.example.micha.chavrutamatch.Data.HostSessionData;
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

        void setMyChavrutaAdapter(ArrayList<HostSessionData> myChavrutasArrayList);

        void displayRecyclerView();

        void sendHostsConfirmationtoDb(String chavrutaId, String requesterId);


    }

    interface Presenter {
        void setMAView(View view);

        void testMVPToast();

        void setupToolbar();

        void getAccountKit();

        void getJsonChavrutaString();

        void returnAsyncResult(String output);

        //        void setMainActivityListView(RecyclerView listView);
        void onBindToPresenter(MARepoContract holder, int position, int viewType);

        void setViewHolderConfirmations(HostSessionData currentItem, MARepoContract holder, int requestClicked);

        int getItemViewTypeFromPresenter(int position);

    }


    interface Model {
        void putStringDataInSP(String key, String value);

        void getAllUserDetailsFromSP();

        void setAllSPValuesToUserDetails();

        boolean isVerifiedAsLoggedIn();

        void putBooleanDataInSP(String key, boolean value);

        ServerConnect getServerConnectInstance();

        void parseJSONDataToArrayList(String jsonString);

        HostSessionData getMyChavrutasArrayListItem(int position);

        ArrayList<HostSessionData> getMyChavrutasAL();

    }
}
