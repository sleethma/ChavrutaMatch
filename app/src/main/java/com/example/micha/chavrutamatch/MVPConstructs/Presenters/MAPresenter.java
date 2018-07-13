package com.example.micha.chavrutamatch.MVPConstructs.Presenters;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Repos.MARepoContract;



/**
 * Created by micha on 2/26/2018.
 */

public class MAPresenter implements MAContractMVP.Presenter {

    private final int TEMPLATE_AVATAR_LIST_SIZE = 10;
    private final String CUSTOM_AVATAR_NUM = "999";
    private MAContractMVP.Model mainActivityModel;
    private MAContractMVP.View mainActivityView;
    private UserDetails userDetailsInstance;

    public MAPresenter(MAContractMVP.Model mainActivityModel) {
        this.mainActivityModel = mainActivityModel;
        userDetailsInstance = mainActivityModel.getUserDetailsInstance();
    }

    @Override
    public void setUserDataToSession(){
        mainActivityModel.setUserDataFromSPToModel();
        mainActivityModel.setMAModelValuesToUserDetails();
    }

    @Override
    public void setMAView(MAContractMVP.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void setupToolbar() {
        mainActivityView.setToolbarUnderline();
        mainActivityView.setOptionsMenu();
        mainActivityView.setUserAvatar();
    }

    @Override
    public void setCurrentUserAccountKit() {
        mainActivityModel.initAccountKit();
    }

    @Override
    public void getUserDataAttempt() {
        mainActivityModel.checkModelForUserData();
    }

    @Override
    public void refreshMaAvatarIfNeeded() {
        //todo: below commented out because need write to only replace if userAvatarNum in UD in SP !equal UD && UD!=null
//        String avatarNumFromSP = mainActivityModel.getStringDataFromSP("user avatar number key");
//        String avatarNumFromSession = userDetailsInstance.getmUserAvatarNumberString();
//        boolean exchangeAvatarNeededInUI = avatarNumFromSP !=null && avatarNumFromSession !=null &&  !avatarNumFromSession.equals(avatarNumFromSP);
//        if(exchangeAvatarNeededInUI){
//        mainActivityView.replaceAvatar();
//        }
                mainActivityView.replaceAvatar();
    }

    @Override
    public boolean isCurrentUserLoggedInToAccountKit() {
        boolean currentUserIsLoggedIn = false;
        if (userDetailsInstance.getUserId() != null) {
            currentUserIsLoggedIn = true;
        }


        //todo: delete if below assurance is not necessary
//        String lastUserLoginId = UserDetails.getmUserId();
//
//        boolean currentUserIsLoggedIn = false;
//        try {
//            if (AccountKit.getCurrentAccessToken().getAccountId() != null) {
//                currentUserLoginId = AccountKit.getCurrentAccessToken().getAccountId();
//                if (lastUserLoginId.equals(currentUserLoginId)) {
//                    storeCurrentUserDataInSP();
//                    currentUserIsLoggedIn = true;
//                }
//            }
//        } catch (NullPointerException e) {
//            Log.e(MAPresenter.class.getSimpleName().toString(), "user not logged in");
//            currentUserIsLoggedIn = false;
//            //do db query and parse in below EMPTY method
//        }
        return currentUserIsLoggedIn;
    }

    private void storeCurrentUserDataInSP() {
        mainActivityModel.putStringDataInSP("user account id key", userDetailsInstance.getUserId());
        //todo: delete @getNewUserKey() below
//        mainActivityModel.putBooleanDataInSP("new_user_key", userDetailsInstance.getNewUserKey());
        mainActivityModel.putStringDataInSP("user phone number key", userDetailsInstance.getmUserPhoneNumber());
        mainActivityModel.putStringDataInSP("user email key", userDetailsInstance.getmUserEmail());
    }


    @Override
    public void setGsonInModel() {
        mainActivityModel.setCallbackToPresenter(this);
        mainActivityModel.observableRequestMyChavrutas();
    }

    @Override
    public void setMyChavrutaData() {
        mainActivityView.setMyChavrutaAdapter(mainActivityModel.getMyChavrutasAL());
        if (mainActivityModel.getMyChavrutasAL() != null && mainActivityModel.getMyChavrutasAL().size() > 0)
            mainActivityView.displayRecyclerView();
    }

    @Override
    public void onBindToPresenter(MARepoContract holder, int position, int viewType) {
        final int HOST_VIEW = 1;

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
        String userId = userDetailsInstance.getUserId();
        // @return 1: hostview, else awaitingConfirmView
        if (hostId.equals(userId)) {
            return 1;
        } else {
            return 0;
        }
    }


    public void setDataToAwaitingConfirmView(MARepoContract holder, ServerResponse serverResponse) {
        String learnerConfirmed = serverResponse.getConfirmed();
        String userId = userDetailsInstance.getUserId();
        boolean userIsConfirmed = learnerConfirmed.equals(userId);
        holder.setUserConfirmedStatus(userIsConfirmed);

        //sets user first last name after concatonation
        String hostNameConcat = createUserFirstLastName(
                serverResponse.getHostFirstName(), serverResponse.getHostLastName());
        holder.setUsersFullName(hostNameConcat);
        setAwaitingHostAvatar(holder, serverResponse);
    }

    private String createUserFirstLastName(String userFirstName, String userLastName) {
        String lastInitial = userLastName.substring(0, 1);
        return userFirstName + " " + lastInitial + ".";
    }


    private void createHostView(MARepoContract holder, ServerResponse chavruta) {
        holder.setUsersFullName(userDetailsInstance.getmUserName());
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


    private boolean configureRequestersConfirmedStatus(MARepoContract holder, ServerResponse chavruta) {
        boolean hasRequesters = false;
        String request1 = chavruta.getChavrutaRequest1();
        String request2 = chavruta.getChavrutaRequest2();
        String request3 = chavruta.getChavrutaRequest3();
        String chavrutaRequestName1 = chavruta.getChavrutaRequest1Name();
        String chavrutaRequestName2 = chavruta.getChavrutaRequest2Name();
        String chavrutaRequestName3 = chavruta.getChavrutaRequest3Name();

        Boolean isRequest1 = request1.length() > 5;
        Boolean isRequest2 = request2.length() > 5;
        Boolean isRequest3 = request3.length() > 5;


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

    //todo: @appCleanUp remove if not necessary
    @Override
    public void appCleanUp() {
        userDetailsInstance.setUserId(null);
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
        if (requestAvatarNumber == null) {
            requestAvatarNumber = "0";
        } else if (requestAvatarNumber.length() >= 3) {
            customRequesterAvatar = ServerResponse.getByteArrayFromString(requestAvatarNumber);
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