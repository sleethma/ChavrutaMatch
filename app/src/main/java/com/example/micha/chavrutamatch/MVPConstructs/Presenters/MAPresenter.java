package com.example.micha.chavrutamatch.MVPConstructs.Presenters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Repos.MARepoContract;
import com.facebook.accountkit.AccountKit;


/**
 * Created by micha on 2/26/2018.
 */

public class MAPresenter implements MAContractMVP.Presenter {

    private final int HOST_VIEW = 1;
    private final int TEMPLATE_AVATAR_LIST_SIZE = 10;
    private final String CUSTOM_AVATAR_NUM = "999";
    private MAContractMVP.Model mainActivityModel;
    private MAContractMVP.View mainActivityView;
    private ServerConnect serverConnectInstance;
    private String jsonString;
    private String userIdInSP;
    private UserDetails userDetailsInstance;

    public MAPresenter(MAContractMVP.Model mainActivityModel) {
        this.mainActivityModel = mainActivityModel;
        this.mainActivityModel.setUserDataFromSPToModel();
        this.mainActivityModel.setAllSPValuesToUserDetails();
        userDetailsInstance = mainActivityModel.getUserDetailsInstance();
    }

    public void replaceSPDataWithCurrentUserData() {
        //todo: if currentUser is different from user in SP, pull down their data to device and store in SP
    }

    @Override
    public void setMAView(MAContractMVP.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void testMVPToast() {
        if (mainActivityView != null) {
            mainActivityView.sendToast("Fake Message");
        }
    }

    @Override
    public void setupToolbar() {
        mainActivityView.setToolbarUnderline();
        mainActivityView.setOptionsMenu();
        mainActivityView.setUserAvatar();
    }

    @Override
    public void activateAccountKit() {
    mainActivityModel.initAccountKit();
    }

    @Override
    public boolean isCurrentUserLoggedInToAccountKit() {
        String currentUserLoginId;
        String lastUserLoginId = userDetailsInstance.getmUserId();

        boolean currentUserIsLoggedIn = false;
        try{
            currentUserLoginId = AccountKit.getCurrentAccessToken().getAccountId().toString();
            if (lastUserLoginId.equals(currentUserLoginId)) {
                storeCurrentUserDataInSP();
                currentUserIsLoggedIn = true;
            }
        }catch (NullPointerException e){
            Log.e(MAPresenter.class.getSimpleName().toString(), "user not logged in");
            currentUserIsLoggedIn = false;
            //do db query and parse in below EMPTY method
        }
        return currentUserIsLoggedIn;
    }

    private void storeCurrentUserDataInSP() {
        mainActivityModel.putStringDataInSP("user account id key", userDetailsInstance.getmUserId());
        mainActivityModel.putBooleanDataInSP("new_user_key", userDetailsInstance.getNewUserKey());
        mainActivityModel.putStringDataInSP("user phone number key", userDetailsInstance.getmUserPhoneNumber());
        mainActivityModel.putStringDataInSP("user email key", userDetailsInstance.getmUserEmail());
    }

    @Override
    public void getJsonChavrutaString() {
        //todo: not needed?
        if (ChavrutaMatch.getMyChavrutaJsonString() != null) {
            jsonString = ChavrutaMatch.getMyChavrutaJsonString();
            jsonStringToArrayList();
        } else {
            getJsonFromServer();
        }
    }

    @Override
    public void setGsonInModel() {
        mainActivityModel.observableRequestMyChavrutas();
        mainActivityModel.setCallbackToPresenter(this);
    }

    @Override
    public void setMainActivityListView(RecyclerView listView) {

    }

    private void jsonStringToArrayList() {
            getJsonFromServer();
    }

    public void getJsonFromServer() {
        String myChavrutasKey = "my chavrutas";
        serverConnectInstance = mainActivityModel.getServerConnectInstance();
//        mainActivityModel.observableRequestMyChavrutas();
        serverConnectInstance.callback = this;
        mainActivityModel.setCallbackToPresenter(this);
        serverConnectInstance.execute(myChavrutasKey);
    }


    @Override
    public void returnAsyncResult(String jsonString) {
        this.jsonString = jsonString;
        mainActivityModel.parseJSONDataToArrayList(jsonString);
        mainActivityView.setOldAdapter(mainActivityModel.getOldAL());
        if (mainActivityModel.getOldAL() != null && mainActivityModel.getMyChavrutasAL().size() > 0)
            mainActivityView.displayRecyclerView();
    }

    @Override
    public void setMyChavrutaData(){
        mainActivityView.setMyChavrutaAdapter(mainActivityModel.getMyChavrutasAL());
        if (mainActivityModel.getMyChavrutasAL() != null && mainActivityModel.getMyChavrutasAL().size() > 0)
            mainActivityView.displayRecyclerView();
    }

    @Override
    public void onBindToPresenter(MARepoContract holder, int position, int viewType) {
        ServerResponse chavruta = mainActivityModel.getMyChavrutasItem(position);
        holder.setUsersFullName(chavruta.getHostFirstName());

        // @return 1: hostview, else awaitingConfirmView
        if (viewType == HOST_VIEW) {
            createHostView(holder, chavruta);
            //view is awaiting hosts confirmation
        } else {
            setDataToAwaitingConfirmView(holder, chavruta);
        }
        holder.setViewItemDataInMyChavruta(chavruta, position);
    }

    @Override
    public int getItemViewTypeFromPresenter(int position) {
        //selects hostId or userId for inflation
        String hostId = mainActivityModel.getMyChavrutasItem(position).getHostId();
        String userId = userDetailsInstance.getmUserId();
        // @return 1: hostview, else awaitingConfirmView
        if (hostId.equals(userId)) {
            return 1;
        } else {
            return 0;
        }
    }


    public void setDataToAwaitingConfirmView(MARepoContract holder, ServerResponse serverResponse) {
        String learnerConfirmed = serverResponse.getConfirmed();
        String userId = userDetailsInstance.getmUserId();
        boolean userIsConfirmed = learnerConfirmed.equals(userId);
        holder.setUserConfirmedStatus(userIsConfirmed);

        //sets user first last name after concatonation
        String hostNameConcat = createUserFirstLastName(
                serverResponse.getHostFirstName(), serverResponse.getHostLastName());
        holder.setUsersFullName(hostNameConcat);
        setAwaitingHostAvatar(holder, serverResponse);
    }

    public String createUserFirstLastName(String userFirstName, String userLastName) {
        String lastInitial = userLastName.substring(0, 1);
        String userFirstLastName = userFirstName + " " + lastInitial + ".";
        return userFirstLastName;
    }


    public void createHostView(MARepoContract holder, ServerResponse chavruta) {
        holder.setUsersFullName(UserDetails.getmUserName());
        boolean hasRequesters = configureRequestersConfirmedStatus(holder, chavruta);
        holder.setDisplayIfRequesters(hasRequesters);
        setHostsAvatar(holder);
    }


    private void setAwaitingHostAvatar(MARepoContract holder, ServerResponse chavruta) {
        String awaitingHostAvatarNumber;
        if (chavruta.getHostAvatarNumber() != null) {
            awaitingHostAvatarNumber = chavruta.getHostAvatarNumber();
        } else {
            return;
        }
        if (awaitingHostAvatarNumber.length() < TEMPLATE_AVATAR_LIST_SIZE) {
            holder.setTemplateListItemAwaitingHostAvatar(awaitingHostAvatarNumber);
        } else {
            byte[] awaitingHostCustomAvatar = chavruta.getHostCustomAvatarByteArray();
            holder.setCustomListItemAwaitingHostAvatar(awaitingHostCustomAvatar);
        }

    }

    private void setHostsAvatar(MARepoContract holder) {
        if (UserDetails.getmUserAvatarNumberString() == null) return;
        String userAvatarNumberString = UserDetails.getmUserAvatarNumberString();
        if (userAvatarNumberString.length() < TEMPLATE_AVATAR_LIST_SIZE && !userAvatarNumberString.equals(CUSTOM_AVATAR_NUM)) {
            holder.setTemplateListItemHostAvatar();
        } else {
            holder.setCustomListItemHostAvatar();
        }
    }


    public boolean configureRequestersConfirmedStatus(MARepoContract holder, ServerResponse chavruta) {
        boolean hasRequesters = false;
        String request1 = chavruta.getChavrutaRequest1();
        String request2 = chavruta.getChavrutaRequest2();
        String request3 = chavruta.getChavrutaRequest3();
        String chavrutaRequestName1 = chavruta.getChavrutaRequest1Name();
        String chavrutaRequestName2 = chavruta.getChavrutaRequest2Name();
        String chavrutaRequestName3 = chavruta.getChavrutaRequest3Name();

        Boolean isRequest1 = request1.length() > 5 ? true : false;
        Boolean isRequest2 = request2.length() > 5 ? true : false;
        Boolean isRequest3 = request3.length() > 5 ? true : false;


        if (isRequest1) {
            holder.setupHostsRequestersViews(chavrutaRequestName1, 1);
            setHostsRequestItem(holder, chavruta, request1, 1);
            holder.setOnClickListenerOnRequester(holder, chavruta, 1);
            hasRequesters = true;
        }
        if (isRequest2) {
            holder.setupHostsRequestersViews(chavrutaRequestName2, 2);
            setHostsRequestItem(holder, chavruta, request2, 2);
            holder.setOnClickListenerOnRequester(holder, chavruta, 2);
            hasRequesters = true;
        }
        if (isRequest3) {
            holder.setupHostsRequestersViews(chavrutaRequestName3, 3);
            setHostsRequestItem(holder, chavruta, request3, 3);
            holder.setOnClickListenerOnRequester(holder, chavruta, 3);
            hasRequesters = true;
        }
        return hasRequesters;
    }

    private void setHostsRequestItem(MARepoContract holder, ServerResponse chavruta, String requestNumbersId, int requestNumber) {
        String idOfConfirmedUser = chavruta.getConfirmed();
        byte[] customRequesterAvatar = null;

        //if confirmed id of requester matches the chavrutas confirmed requester field
        if (idOfConfirmedUser.equals(requestNumbersId)) {
            holder.setRequestButtonStatus(requestNumber);
            chavruta.setRequestConfirmed(true, requestNumber);
        } else {
            chavruta.setRequestConfirmed(false, requestNumber);
        }

        String requestAvatarNumber = chavruta.getmChavrutaRequestAvatar(requestNumber);
        if (requestAvatarNumber.length() >= 3) {
            customRequesterAvatar = HostSessionData.getByteArrayFromString(requestAvatarNumber);
        }
        holder.setHostRequestAvatar(customRequesterAvatar, requestAvatarNumber, requestNumber);
    }



    //called when requester confirm button clicked
    @Override
    public void setViewHolderConfirmations(ServerResponse repoHSD, MARepoContract holder, int requestClicked) {
        switch (requestClicked) {
            case 1:
                //sets confirmed state for request 1
                if (!repoHSD.requestOneConfirmed) {
                    repoHSD.setRequestConfirmed(true, 1);
                    repoHSD.setConfirmed(repoHSD.getChavrutaRequest1());

                    //set other request confirmations to false
                    repoHSD.setRequestConfirmed(false, 2);
                    repoHSD.setRequestConfirmed(false, 3);

                    holder.setButtonToConfirmedState("Requester 1 Confirmed");
                    //sets appropriate views as visible
                } else {
                    repoHSD.setRequestConfirmed(false, 1);
                    repoHSD.setConfirmed("0");
                    holder.setButtonToConfirmedState("Requester 1 Not Confirmed");
                }
                break;

            case 2:
                //sets confirmed or not
                if (!repoHSD.requestTwoConfirmed) {
                    repoHSD.setRequestConfirmed(true, 2);
                    repoHSD.setConfirmed(repoHSD.getChavrutaRequest2());


                    //set other request confirmations to false
                    repoHSD.setRequestConfirmed(false, 1);
                    repoHSD.setRequestConfirmed(false, 3);
                    holder.setButtonToConfirmedState("Requester 2 Confirmed");

                } else {
                    repoHSD.setRequestConfirmed(false, 2);
                    repoHSD.setConfirmed("0");
                    holder.setButtonToConfirmedState("Requester 2 Not Confirmed");
                }
                break;

            case 3:
                //sets confirmed or not
                if (!repoHSD.requestThreeConfirmed) {
                    repoHSD.setRequestConfirmed(true, 3);
                    repoHSD.setConfirmed(repoHSD.getChavrutaRequest3());
                    //confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));

                    //set other request confirmations to false
                    repoHSD.setRequestConfirmed(false, 1);
                    repoHSD.setRequestConfirmed(false, 2);
                    holder.setButtonToConfirmedState("Requester 3 Confirmed");
                } else {
                    repoHSD.setRequestConfirmed(false, 3);
                    repoHSD.setConfirmed("0");
                    holder.setButtonToConfirmedState("Requester 3 Not Confirmed");
                }
                break;

            default:
                return;
        }
        //notify db of change in requester status for chavruta
        String chavrutaId = repoHSD.getChavrutaId();
        //todo: refactor with RxJava to circumvent Async using dagger issues
        //sent to view as multiple Async cannot be injected here
        mainActivityView.sendHostsConfirmationtoDb(chavrutaId, repoHSD.getConfirmed());
    }
}
//            myChavrutasArrayList = new ArrayList<>();
//
//            //add and remove views to display myChavrutas
//            if (!jsonString.isEmpty()) {
//
//                //inorder to resize mychavruta recyclerview
//                myChavrutaListView.requestLayout();

//                //todo: inject adapter using: https://android.jlelse.eu/recyclerview-in-mvp-passive-views-approach-8dd74633158
//                //attaches data source to adapter and displays list
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//                myChavrutaListView.setLayoutManager(linearLayoutManager);
//
//                //add ItemDecoration
//                myChavrutaListView.addItemDecoration(new RecyclerViewListDecor(VERTICAL_LIST_ITEM_SPACE));
//
//                myChavrutaListView.setHasFixedSize(true);
//                mAdapter = new OpenChavrutaAdapter(this, myChavrutasArrayList);
//                myChavrutaListView.setAdapter(mAdapter);
//                noMatchView.setVisibility(View.GONE);
//                myChavrutaListView.setVisibility(View.VISIBLE);
//            } else {
//                //sets empty array list view
//                myChavrutaListView.setVisibility(View.GONE);
//                noMatchView.setVisibility(View.VISIBLE);
//
//            }
//            //checks to ensure db has data after parsing
//            if (myChavrutasArrayList.size() < 1) {
//                myChavrutaListView.setVisibility(View.GONE);
//                noMatchView.setVisibility(View.VISIBLE);
//            }
//            Toolbar toolbar = findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
//        } else {
//            //if db not yet accessed, gets all chavrutas that user has requested
//            //@var sp: sets userId to UserDetails for server calls
//            accountId = mainActivityModel.getStringDataFromSP(getString(R.string.user_account_id_key));
//            UserDetails.setmUserId(accountId);
//            //check user has a stored accountkit id on device and fetch their chavruta data from db
//            if (accountId != null) {
//                String myChavrutasKey = "my chavrutas";
//                ServerConnect getMyChavrutas = new ServerConnect(this, myChavrutaListView);
//                getMyChavrutas.execute(myChavrutasKey);
//            } else {
//                //device is not logged in
//                AccountKit.logOut();
//                launchLoginActivity();
//            }
//        }
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadOnSelectActivity(view);
//            }
//        });
//
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//            // COMPLETED (4) Override onMove and simply return false inside
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                //do nothing, we only swipe needed
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                // Inside, get the viewHolder's itemView's tag and store in a long variable id
//                //get the id of the item being swiped
//                int id = (int) viewHolder.itemView.getTag();
//                int currentItemViewType = viewHolder.getItemViewType();
//                notifyUserBeforeDelete(id, currentItemViewType);
//
//            }
//        }).attachToRecyclerView(myChavrutaListView);


