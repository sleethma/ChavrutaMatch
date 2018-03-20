package com.example.micha.chavrutamatch.MVPConstructs.Presenters;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Repos.MARepoContract;
import com.example.micha.chavrutamatch.Utils.ChavrutaUtils;


/**
 * Created by micha on 2/26/2018.
 */

public class MAPresenter implements MAContractMVP.Presenter {

    private final int HOST_VIEW = 1;
    private final int TEMPLATE_AVATAR_LIST_SIZE = 10;
    private final String CUSTOM_AVATAR_NUM = "999";
    private MAContractMVP.Model mainActivityModel;
    private MAContractMVP.View mainActivityView;
    private MAContractMVP.View mainActivityListView;
    private ServerConnect serverConnectInstance;
    private String jsonString;

    public MAPresenter(MAContractMVP.Model mainActivityModel) {
        this.mainActivityModel = mainActivityModel;
        this.mainActivityModel.getAllUserDetailsFromSP();
        this.mainActivityModel.setAllSPValuesToUserDetails();
    }

    @Override
    public void setMAView(MAContractMVP.View view) {
        this.mainActivityView = view;
    }

    //todo: delete below if not necessary
//    @Override
//    public void setMainActivityListView(RecyclerView listView) {
//        mainActivityListView = listView;
//    }


    @Override
    public void testMVPToast() {
        if (mainActivityView != null) {
            mainActivityView.sendToast("Fake Message");
        }
    }

    @Override
    public void setupToolbar() {
        mainActivityView.setToolbarUnderline();
        mainActivityView.setUserAvatar();
    }

    @Override
    public void getAccountKit() {
        if (mainActivityModel.isVerifiedAsLoggedIn()) {
            mainActivityModel.putStringDataInSP("user account id key", UserDetails.getmUserId());
            mainActivityModel.putBooleanDataInSP("new_user_key", UserDetails.getNewUserKey());
            mainActivityModel.putStringDataInSP("user phone number key", UserDetails.getmUserPhoneNumber());
            mainActivityModel.putStringDataInSP("user email key", UserDetails.getmUserEmail());
        } else {
            if (mainActivityView != null) {
                mainActivityView.sendToast("Fake Message");
            }
        }
    }

    @Override
    public void getJsonChavrutaString() {
        if (ChavrutaMatch.getMyChavrutaJsonString() != null) {
            jsonString = ChavrutaMatch.getMyChavrutaJsonString();
            jsonStringToArrayList();
        } else {
            getJsonFromServer();
        }
    }

    private void jsonStringToArrayList() {
        mainActivityModel.parseJSONDataToArrayList(jsonString);
        mainActivityView.setMyChavrutaAdapter(mainActivityModel.getMyChavrutasAL());
        if (mainActivityModel.getMyChavrutasAL() != null && mainActivityModel.getMyChavrutasAL().size() > 0)
            mainActivityView.displayRecyclerView();
    }

    private void getJsonFromServer() {
        String myChavrutasKey = "my chavrutas";
        serverConnectInstance = mainActivityModel.getServerConnectInstance();
        serverConnectInstance.callback = this;
        serverConnectInstance.execute(myChavrutasKey);
    }


    @Override
    public void returnAsyncResult(String jsonString) {
        this.jsonString = jsonString;
        mainActivityModel.parseJSONDataToArrayList(jsonString);
        mainActivityView.setMyChavrutaAdapter(mainActivityModel.getMyChavrutasAL());
        if (mainActivityModel.getMyChavrutasAL() != null && mainActivityModel.getMyChavrutasAL().size() > 0)
            mainActivityView.displayRecyclerView();
    }

    @Override
    public void onBindToPresenter(MARepoContract holder, int position, int viewType) {
        HostSessionData repoHSD = mainActivityModel.getMyChavrutasArrayListItem(position);
        holder.setUsersFullName(repoHSD.getmHostFirstName());

        // @return 1: hostview, else awaitingConfirmView
//        int viewType = holder.getItemViewType(repoHSD.getmHostId(), UserDetails.getmUserId());

        if (viewType == HOST_VIEW) {
            createHostView(holder, repoHSD);
            //view is awaiting hosts confirmation
        } else {
            createAwaitingConfirmView(holder, repoHSD);
        }

        holder.setViewItemData(repoHSD, position);
    }

    @Override
    public int getItemViewTypeFromPresenter(int position) {
        //selects hostId or userId for inflation
        String hostId = mainActivityModel.getMyChavrutasArrayListItem(position).getmHostId();
        String userId = UserDetails.getmUserId();
        // @return 1: hostview, else awaitingConfirmView
        if (hostId.equals(userId)) {
            return 1;
        } else {
            return 0;
        }
    }


    private void createAwaitingConfirmView(MARepoContract holder, HostSessionData repoHSD) {
        String learnerConfirmed = repoHSD.getmConfirmed();
        String userId = UserDetails.getmUserId();
        boolean userIsConfirmed = learnerConfirmed.equals(userId);
        holder.setUserConfirmedStatus(userIsConfirmed);

        //sets user first last name after concatonation
        String hostNameConcat = ChavrutaUtils.createUserFirstLastName(
                repoHSD.getmHostFirstName(), repoHSD.getmHostLastName());
        holder.setUsersFullName(hostNameConcat);
        setAwaitingHostAvatar(holder, repoHSD);
    }

    private void createHostView(MARepoContract holder, HostSessionData repoHSD) {
        holder.setUsersFullName(UserDetails.getmUserName());
        boolean hasRequesters = configureRequestersConfirmedStatus(holder, repoHSD);
        holder.setDisplayIfRequesters(hasRequesters);
        setHostsAvatar(holder);
    }


    private void setAwaitingHostAvatar(MARepoContract holder, HostSessionData repoHSD) {
        String hostAvatarNumber;
        if (repoHSD.getmHostAvatarNumber() != null) {
            hostAvatarNumber = repoHSD.getmHostAvatarNumber();
        } else {
            return;
        }
        if (hostAvatarNumber.length() < TEMPLATE_AVATAR_LIST_SIZE) {
            holder.setTemplateListItemAwaitingHostAvatar(hostAvatarNumber);
        } else {
            byte[] hostCustomAvatar = repoHSD.getmHostCustomAvatarByteArray();
            holder.setCustomListItemAwaitingHostAvatar(hostCustomAvatar);
        }

    }

    private void setHostsAvatar(MARepoContract holder) {
        if (UserDetails.getmUserAvatarNumberString() != null) return;
        if (UserDetails.getmUserAvatarNumberString().length() < TEMPLATE_AVATAR_LIST_SIZE) {
            holder.setTemplateListItemHostAvatar();
        } else {
            holder.setCustomListItemHostAvatar();
        }
    }

    private boolean configureRequestersConfirmedStatus(MARepoContract holder, HostSessionData repoHSD) {
        boolean hasRequesters = false;
        String request1 = repoHSD.getMchavrutaRequest1Id();
        String request2 = repoHSD.getMchavrutaRequest2Id();
        String request3 = repoHSD.getMchavrutaRequest3Id();
        String chavrutaRequestName1 = repoHSD.getmChavrutaRequest1Name();
        String chavrutaRequestName2 = repoHSD.getmChavrutaRequest2Name();
        String chavrutaRequestName3 = repoHSD.getmChavrutaRequest3Name();

        Boolean isRequest1 = request1.length() > 5 ? true : false;
        Boolean isRequest2 = request2.length() > 5 ? true : false;
        Boolean isRequest3 = request3.length() > 5 ? true : false;


        if (isRequest1) {
            holder.setupHostsRequestersViews(chavrutaRequestName1, 1);
            setHostsRequestItem(holder, repoHSD, request1, 1);
            holder.setOnClickListenerOnRequester(holder, repoHSD, 1);
            hasRequesters = true;
        }
        if (isRequest2) {
            holder.setupHostsRequestersViews(chavrutaRequestName2, 2);
            setHostsRequestItem(holder, repoHSD, request2, 2);
            holder.setOnClickListenerOnRequester(holder, repoHSD, 2);
            hasRequesters = true;
        }
        if (isRequest3) {
            holder.setupHostsRequestersViews(chavrutaRequestName3, 3);
            setHostsRequestItem(holder, repoHSD, request3, 3);
            holder.setOnClickListenerOnRequester(holder, repoHSD, 3);
            hasRequesters = true;
        }
        return hasRequesters;
    }


    private void setHostsRequestItem(MARepoContract holder, HostSessionData repoHSD, String requestNumbersId, int requestNumber) {
        String idOfConfirmedUser = repoHSD.getmConfirmed();
        byte[] customRequesterAvatar = null;

        //if confirmed id of requester matches the chavrutas confirmed requester field
        if (idOfConfirmedUser.equals(requestNumbersId)) {
            holder.setRequestButtonStatus(requestNumber);
            repoHSD.setRequestConfirmed(true, requestNumber);
        } else {
            repoHSD.setRequestConfirmed(false, requestNumber);
        }

        String requestAvatarNumber = repoHSD.getmChavrutaRequestAvatar(requestNumber);
        if (requestAvatarNumber.length() >= 3) {
            customRequesterAvatar = HostSessionData.getByteArrayFromString(requestAvatarNumber);
        }
        holder.setHostRequestAvatar(customRequesterAvatar, requestAvatarNumber, requestNumber);
    }

    //called when requester confirm button clicked
    @Override
    public void setViewHolderConfirmations(HostSessionData repoHSD, MARepoContract holder, int requestClicked) {
        switch (requestClicked) {
            case 1:
                //sets confirmed state for request 1
                if (!repoHSD.requestOneConfirmed) {
                    repoHSD.setRequestConfirmed(true, 1);
                    repoHSD.setmConfirmed(repoHSD.getMchavrutaRequest1Id());

                    //set other request confirmations to false
                    repoHSD.setRequestConfirmed(false, 2);
                    repoHSD.setRequestConfirmed(false, 3);

                    holder.setButtonToConfirmedState("Requester 1 Confirmed");
                    //sets appropriate views as visible
                } else {
                    repoHSD.setRequestConfirmed(false, 1);
                    repoHSD.setmConfirmed("0");
                    holder.setButtonToConfirmedState("Requester 1 Not Confirmed");
                }
                break;

            case 2:
                //sets confirmed or not
                if (!repoHSD.requestTwoConfirmed) {
                    repoHSD.setRequestConfirmed(true, 2);
                    repoHSD.setmConfirmed(repoHSD.getMchavrutaRequest2Id());


                    //set other request confirmations to false
                    repoHSD.setRequestConfirmed(false, 1);
                    repoHSD.setRequestConfirmed(false, 3);
                    holder.setButtonToConfirmedState("Requester 2 Confirmed");

                } else {
                    repoHSD.setRequestConfirmed(false, 2);
                    repoHSD.setmConfirmed("0");
                    holder.setButtonToConfirmedState("Requester 2 Not Confirmed");
                }
                break;

            case 3:
                //sets confirmed or not
                if (!repoHSD.requestThreeConfirmed) {
                    repoHSD.setRequestConfirmed(true, 3);
                    repoHSD.setmConfirmed(repoHSD.getMchavrutaRequest3Id());
                    //confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));

                    //set other request confirmations to false
                    repoHSD.setRequestConfirmed(false, 1);
                    repoHSD.setRequestConfirmed(false, 2);
                    holder.setButtonToConfirmedState("Requester 3 Confirmed");
                } else {
                    repoHSD.setRequestConfirmed(false, 3);
                    repoHSD.setmConfirmed("0");
                    holder.setButtonToConfirmedState("Requester 3 Not Confirmed");
                }
                break;

            default:
                return;
        }
        //notify db of change in requester status for chavruta
        String chavrutaId = repoHSD.getmChavrutaId();
        //todo: refactor with RxJava to circumvent Async using dagger issues
        //sent to view as multiple Async cannot be injected here
        mainActivityView.sendHostsConfirmationtoDb(chavrutaId, repoHSD.getmConfirmed());
    }
}
//
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


