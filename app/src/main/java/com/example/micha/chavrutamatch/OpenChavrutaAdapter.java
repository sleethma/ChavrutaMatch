package com.example.micha.chavrutamatch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Repos.MARepoContract;
import com.example.micha.chavrutamatch.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import static com.example.micha.chavrutamatch.AcctLogin.UserDetails.LOG_TAG;
import static com.example.micha.chavrutamatch.AcctLogin.UserDetails.getUserCustomAvatarBase64ByteArray;

/**
 * Created by micha on 7/22/2017.
 */

public class OpenChavrutaAdapter extends RecyclerView.Adapter<OpenChavrutaAdapter.ViewHolder> {

    //number of views adapter will hold
    private Context mContext;
    String userId = UserDetails.getmUserId();
    private Context mainActivityContext;
    //@var used to control swipe on delete Dialogue selection
    private static boolean mConfirmed = false;
    //interface for getting MainActivity context
    private ParentView callback = (ParentView) MainActivity.mContext;
    private MAContractMVP.Presenter presenter;

    private int requesterNumber;

    //holds viewType for relevant listItem
    Boolean hostListItemView;
    Boolean awaitingConfirmView;
    private static ArrayList<HostSessionData> mChavrutaSessionsAL;
    private List<Integer> avatarList = AvatarImgs.getAllAvatars();

    public OpenChavrutaAdapter(Context context, ArrayList<HostSessionData> chavrutaSessionsArrayList, MAContractMVP.Presenter presenter) {
        this.mContext = context;
        this.presenter = presenter;
        mChavrutaSessionsAL = chavrutaSessionsArrayList;
        mainActivityContext = MainActivity.mContext;
        //calls so can access static user vars throughout adapter
//        orderArrayByDate(mChavrutaSessionsAL);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;
        //user is viewing open classes
        //view is awaiting class confirmation by host
        if (viewType == 0) {
            layoutIdForListItem = R.layout.my_chavruta_list_item;
            //view: user is hosting a class
        } else {
            layoutIdForListItem = R.layout.hosting_chavrutas_list_item;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getItemViewTypeFromPresenter(position);
    }

    @Override
    public void onBindViewHolder(OpenChavrutaAdapter.ViewHolder holder, int position) {
        presenter.onBindToPresenter(holder, position, holder.getItemViewType());
    }

    @Override
    public int getItemCount() {
        return mChavrutaSessionsAL.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements MARepoContract {
        TextView hostFullName, sessionDate, startTime, endTime, sefer, location,
                confirmRequestName_1, confirmRequestName_2, confirmRequestName_3;
        LinearLayout pendingRequest_1;
        LinearLayout pendingRequest_2;
        LinearLayout pendingRequest_3;
        View underlinePendingRequest_1;
        View underlinePendingRequest_2;
        View underlinePendingRequest_3;
        TextView pendingRequestLabel;
        TextView hostUserName;
        ImageView confirmRequestAvatar_1;
        ImageView confirmRequestAvatar_2;
        ImageView confirmRequestAvatar_3;
        Button confirmRequest_1;
        Button confirmRequest_2;
        Button confirmRequest_3;
        Button chavrutaConfirmed;
        ImageButton addHost;
        FrameLayout noRequesterView;
        ImageView hostAvatar;
        ImageView awaitingHostAvatar;
        ImageView knurling;

        public ViewHolder(View listItemView) {
            super(listItemView);
            hostFullName = (TextView) listItemView.findViewById(R.id.host_full_name);
            sessionDate = (TextView) listItemView.findViewById(R.id.session_date);
            startTime = (TextView) listItemView.findViewById(R.id.start_time);
            endTime = (TextView) listItemView.findViewById(R.id.end_time);
            sefer = (TextView) listItemView.findViewById(R.id.session_sefer);
            location = (TextView) listItemView.findViewById(R.id.location);
            addHost = (ImageButton) listItemView.findViewById(R.id.ib_add_match);
            pendingRequest_1 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_1);
            pendingRequest_2 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_2);
            pendingRequest_3 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_3);
            underlinePendingRequest_1 = (View) listItemView.findViewById(R.id.v_underline_requester_1);
            underlinePendingRequest_2 = (View) listItemView.findViewById(R.id.v_underline_requester_2);
            underlinePendingRequest_3 = (View) listItemView.findViewById(R.id.v_underline_requester_3);
            pendingRequestLabel = (TextView) listItemView.findViewById(R.id.tv_requests_label);
            hostUserName = listItemView.findViewById(R.id.host_user_name);
            confirmRequestName_1 = (TextView) listItemView.findViewById(R.id.tv_confirm_request_1);
            confirmRequestName_2 = (TextView) listItemView.findViewById(R.id.tv_confirm_request_2);
            confirmRequestName_3 = (TextView) listItemView.findViewById(R.id.tv_confirm_request_3);
            confirmRequestAvatar_1 = (ImageView) listItemView.findViewById(R.id.iv_user_request_1_avatar);
            confirmRequestAvatar_2 = (ImageView) listItemView.findViewById(R.id.iv_user_request_2_avatar);
            confirmRequestAvatar_3 = (ImageView) listItemView.findViewById(R.id.iv_user_request_3_avatar);
            confirmRequest_1 = (Button) listItemView.findViewById(R.id.b_confirm_request_1);
            confirmRequest_2 = (Button) listItemView.findViewById(R.id.b_confirm_request_2);
            confirmRequest_3 = (Button) listItemView.findViewById(R.id.b_confirm_request_3);
            chavrutaConfirmed = (Button) listItemView.findViewById(R.id.b_chavruta_confirmed);
            noRequesterView = (FrameLayout) listItemView.findViewById(R.id.fl_awaiting_requester);
            hostAvatar = (ImageView) listItemView.findViewById(R.id.iv_host_avatar);
            awaitingHostAvatar = listItemView.findViewById(R.id.iv_awaiting_host_avatar);
            knurling = listItemView.findViewById(R.id.iv_knurling);
        }

        @Override
        public void setHostRequestAvatar(byte[] customRequesterAvatar, String requestAvatarNumber, int requesterNumber) {
            ImageView currentAvatarView;
            if (requesterNumber == 1) {
                currentAvatarView = confirmRequestAvatar_1;
            } else if (requesterNumber == 2) {
                currentAvatarView = confirmRequestAvatar_2;
            } else {
                currentAvatarView = confirmRequestAvatar_3;
            }

            if (customRequesterAvatar != null) {
                GlideApp
                        .with(mContext)
                        .asBitmap()
                        .load(customRequesterAvatar)
                        .circleCrop()
                        .into(currentAvatarView);
            } else {
                currentAvatarView.setImageResource(avatarList.get(
                        Integer.parseInt(requestAvatarNumber)));
            }
        }

        @Override
        public void setViewItemData(HostSessionData repoHSD, int position) {
            sessionDate.setText(repoHSD.getmSessionDate());
            startTime.setText(repoHSD.getmStartTime());
            endTime.setText(repoHSD.getmEndTime());
            String seferText = repoHSD.getmSefer();
            if (seferText.length() >= 30) {
                seferText = seferText.substring(0, 30) + "...";
            }
            sefer.setText(seferText);
            location.setText(repoHSD.getmLocation());
            itemView.setTag(position);


            knurling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.getParentView();
                }
            });
        }

        @Override
        public void setupHostsRequestersViews(String requesterName, int requesterNumber) {
            if (requesterNumber == 1) {
                pendingRequest_1.setVisibility(View.VISIBLE);
                underlinePendingRequest_1.setVisibility(View.VISIBLE);
                confirmRequestName_1.setText(
                        requesterName);
            }
            if (requesterNumber == 2) {
                pendingRequest_2.setVisibility(View.VISIBLE);
                underlinePendingRequest_2.setVisibility(View.VISIBLE);
                confirmRequestName_2.setText(
                        requesterName);
            }
            if (requesterNumber == 3) {
                pendingRequest_3.setVisibility(View.VISIBLE);
                underlinePendingRequest_3.setVisibility(View.VISIBLE);
                confirmRequestName_3.setText(
                        requesterName);
            }
        }

        @Override
        public void setRequestButtonStatus(int requestNumber) {
            confirmRequest_1.setBackgroundResource(R.drawable.ic_confirm_class);
        }

        @Override
        public void setUserConfirmedStatus(boolean userIsConfirmed) {
            if (userIsConfirmed) {
                chavrutaConfirmed.setBackgroundResource(R.drawable.confirmed_rounded_corners);
                chavrutaConfirmed.setText("Matched!");
            } else {
                chavrutaConfirmed.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                chavrutaConfirmed.setText("Awaiting" + System.getProperty("line.separator") + "Match");
            }
        }

        @Override
        public void setDisplayIfRequesters(boolean hasRequesters) {
            //populates view if there is a request
            if (hasRequesters) {
                pendingRequestLabel.setVisibility(View.VISIBLE);
                noRequesterView.setVisibility(View.GONE);

            } else {
                pendingRequestLabel.setVisibility(View.GONE);
                noRequesterView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void setTemplateListItemHostAvatar() {
            awaitingHostAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
                    Integer.parseInt(UserDetails.getmUserAvatarNumberString())));
        }

        @Override
        public void setCustomListItemAwaitingHostAvatar(byte[] hostCustomAvatar) {
            GlideApp
                    .with(mContext)
                    .asBitmap()
                    .load(hostCustomAvatar)
                    .placeholder(R.drawable.ic_unknown_user)
                    .centerCrop()
                    .into(awaitingHostAvatar);
        }

        @Override
        public void setTemplateListItemAwaitingHostAvatar(String hostAvatarNumber) {
            awaitingHostAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
                    Integer.parseInt(hostAvatarNumber)));
        }

        @Override
        public void setCustomListItemHostAvatar() {
            if (UserDetails.getHostAvatarUri() != null) {
                GlideApp
                        .with(mContext)
                        .load(UserDetails.getHostAvatarUri())
                        .centerCrop()
                        .into(hostAvatar);
            } else if (getUserCustomAvatarBase64ByteArray() != null) {
                GlideApp
                        .with(mContext)
                        .asBitmap()
                        .load(getUserCustomAvatarBase64ByteArray())
                        .placeholder(R.drawable.ic_unknown_user)
                        .centerCrop()
                        .into(hostAvatar);
                //otherwise use xml unknown profile image
            }
            hostAvatar.setVisibility(View.VISIBLE);
        }

        @Override
        public void setUsersFullName(String text) {
            hostFullName.setText(text);
        }

        @Override
        public int getItemViewTypeIF(HostSessionData hsdRepo) {
            //selects hostId or userId for inflation
            String hostId = hsdRepo.getmHostId();
            // @return 1: hostview, else awaitingConfirmView
            if (hostId.equals(userId)) {
                return 1;
            } else {
                return 0;
            }
        }


        @Override
        public void setOnClickListenerOnRequester(final MARepoContract holder, final HostSessionData repoHSD, final int requesterNumber) {
            //confirms or unconfirms requested view with color change indicator and db update
            Button requestButtonView;
            setRequesterNumber(requesterNumber);

            switch (requesterNumber) {
                case 1:
                    requestButtonView = confirmRequest_1;
                    break;
                case 2:
                    requestButtonView = confirmRequest_2;
                    break;
                case 3:
                    requestButtonView = confirmRequest_3;
                    break;
                default:
                    return;
            }
//            Integer mrequesterNumber = (Integer) requesterNumber;
            requestButtonView.setTag(requestButtonView.getId(), requesterNumber);
            requestButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int requesterNumber = (int) v.getTag(v.getId());
//
//                            // sets and sends to db hosts specific request confirmation
//                            setViewHolderConfirmations(currentItem, 3);
                    // sets and sends to db hosts specific request confirmation
                    presenter.setViewHolderConfirmations(repoHSD, holder, requesterNumber);
                }
            });
        }

        @Override
        public void setButtonToConfirmedState(String confirmButtonAction) {
            switch (confirmButtonAction) {
                case "Requester 1 Confirmed":
                    confirmRequest_1.setBackgroundResource(R.drawable.ic_confirm_class);
                    confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    break;
                case "Requester 1 Not Confirmed":
                    confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    break;
                case "Requester 2 Confirmed":
                    confirmRequest_2.setBackgroundResource(R.drawable.ic_confirm_class);
                    confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    break;
                case "Requester 2 Not Confirmed":
                    confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    break;
                case "Requester 3 Confirmed":
                    confirmRequest_3.setBackgroundResource(R.drawable.ic_confirm_class);
                    confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    break;
                case "Requester 3 Not Confirmed":
                    confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    break;
            }
        }
    }

    public int getRequesterNumber() {
        return requesterNumber;
    }

    public void setRequesterNumber(int requesterNumber) {
        this.requesterNumber = requesterNumber;
    }

    public void add(HostSessionData dataAddedFromJson) {
        mChavrutaSessionsAL.add(dataAddedFromJson);
    }


    public void deleteMyChavrutaArrayItemOnSwipe(int indexToDelete, int viewTypeToDelete) {
        HostSessionData currentItem = mChavrutaSessionsAL.get(indexToDelete);
        //get either HostView or AwaitingConfirmView
        int itemViewType = viewTypeToDelete;
        //awaiting confirm view db delete
        if (itemViewType == 0) {

            String requesterNumber;
            String requesterAvatarColumn;
            String requesterNameColumn;

            if (userId.equals(currentItem.getMchavrutaRequest1Id())) {
                requesterNumber = "chavruta_request_1";
                requesterAvatarColumn = "chavruta_request_1_avatar";
                requesterNameColumn = "chavruta_request_1_name";
            } else if (userId.equals(currentItem.getMchavrutaRequest2Id())) {
                requesterNumber = "chavruta_request_2";
                requesterAvatarColumn = "chavruta_request_2_avatar";
                requesterNameColumn = "chavruta_request_2_name";
            } else {
                requesterNumber = "chavruta_request_3";
                requesterAvatarColumn = "chavruta_request_3_avatar";
                requesterNameColumn = "chavruta_request_3_name";
            }
            String chavrutaId = currentItem.getmChavrutaId();
            String awaitingConfirmKey = "chavruta request";
            ServerConnect dbAwaitingConfirmDelete = new ServerConnect(mainActivityContext);
            dbAwaitingConfirmDelete.execute(awaitingConfirmKey, "0", chavrutaId, requesterNumber,
                    requesterAvatarColumn, "0", requesterNameColumn, "0");


        } else {
            String deleteChavrutaKey = "delete chavruta";
            String chavrutaId = currentItem.getmChavrutaId();
            ServerConnect deleteChavruta = new ServerConnect(mainActivityContext);
            deleteChavruta.execute(deleteChavrutaKey, chavrutaId);
        }
        mChavrutaSessionsAL.remove(indexToDelete);
        this.notifyDataSetChanged();

        if (mChavrutaSessionsAL.size() == 0) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        }
    }

    public static void setConfirmedDeleteStatus(boolean confirmed) {
        mConfirmed = confirmed;
    }

    interface ParentView {
        void getParentView();
    }

    public ArrayList<HostSessionData> getmChavrutaSessionsAL() {
        return mChavrutaSessionsAL;
    }
}

//@Override
//        public void populateRequestDataToHolder(HostSessionData repoHSD) {
//            String idOfConfirmedUser = repoHSD.getmConfirmed();
//            String request1 = repoHSD.getMchavrutaRequest1Id();
//            String request2 = repoHSD.getMchavrutaRequest2Id();
//            String request3 = repoHSD.getMchavrutaRequest3Id();
//            String chavrutaRequestName1 = repoHSD.getmChavrutaRequest1Name();
//            String chavrutaRequestName2 = repoHSD.getmChavrutaRequest2Name();
//            String chavrutaRequestName3 = repoHSD.getmChavrutaRequest3Name();
//
//            Boolean isRequest1 = false;
//            Boolean isRequest2 = false;
//            Boolean isRequest3 = false;

//            if (request1.length() > 5) {
//                isRequest1 = true;
//                holder.pendingRequest_1.setVisibility(View.VISIBLE);
//                holder.underlinePendingRequest_1.setVisibility(View.VISIBLE);
//                if (idOfConfirmedUser.equals(request1) && repoHSD.requestOneConfirmed) {
//                    mChavrutaSessionsAL.get(position).setRequestOneConfirmed(true);
//                    holder.confirmRequest_1.setBackgroundResource(R.drawable.ic_confirm_class);
//                    repoHSD.setRequestOneConfirmed(true);
//                } else {
//                    repoHSD.setRequestOneConfirmed(false);
//                }
//                holder.confirmRequestName_1.setText(
//                        chavrutaRequestName1);
//                String request1AvatarNumber = repoHSD.getmChavrutaRequest1Avatar();
//                if (request1AvatarNumber.length() >= 3) {
//                    byte[] decodeRequesterAvatar = Base64.decode(
//                            request1AvatarNumber, Base64.DEFAULT);
//
//                    GlideApp
//                            .with(mContext)
//                            .asBitmap()
//                            .load(decodeRequesterAvatar)
//                            .circleCrop()
//                            .into(holder.confirmRequestAvatar_1);
//                } else {
//                    holder.confirmRequestAvatar_1.setImageResource(avatarList.get(
//                            Integer.parseInt(request1AvatarNumber)));
//                }
//        } else
//
//        {
//            holder.pendingRequest_1.setVisibility(View.GONE);
//            holder.underlinePendingRequest_1.setVisibility(View.GONE);
//            repoHSD.setRequestOneConfirmed(false);
//        }
//            if(request2.length()>5)
//
//        {
//            holder.pendingRequest_2.setVisibility(View.VISIBLE);
//            holder.underlinePendingRequest_2.setVisibility(View.VISIBLE);
//            isRequest2 = true;
//            if (idOfConfirmedUser.equals(request2) && repoHSD.requestTwoConfirmed) {
//                holder.confirmRequest_2.setBackgroundResource(R.drawable.ic_confirm_class);
//                repoHSD.setRequestTwoConfirmed(true);
//            } else {
//                repoHSD.setRequestTwoConfirmed(false);
//            }
//            holder.confirmRequestName_2.setText(
//                    chavrutaRequestName2);
//            String request2AvatarNumber = repoHSD.getmChavrutaRequest2Avatar();
//            if (request2AvatarNumber.length() >= 3) {
//                byte[] decodeRequesterAvatar = Base64.decode(
//                        request2AvatarNumber, Base64.DEFAULT);
//
//                GlideApp
//                        .with(mContext)
//                        .asBitmap()
//                        .load(decodeRequesterAvatar)
//                        .circleCrop()
//                        .into(holder.confirmRequestAvatar_2);
//            } else {
//                holder.confirmRequestAvatar_2.setImageResource(avatarList.get(
//                        Integer.parseInt(request2AvatarNumber)));
//            }
//
//        } else
//
//        {
//            holder.pendingRequest_2.setVisibility(View.GONE);
//            holder.underlinePendingRequest_2.setVisibility(View.GONE);
//            repoHSD.setRequestTwoConfirmed(false);
//        }
//            if(request3.length()>5)
//
//        {
//            holder.pendingRequest_3.setVisibility(View.VISIBLE);
//            holder.underlinePendingRequest_3.setVisibility(View.VISIBLE);
//            isRequest3 = true;
//            if (idOfConfirmedUser.equals(request3) && repoHSD.requestThreeConfirmed) {
//                holder.confirmRequest_3.setBackgroundResource(R.drawable.ic_confirm_class);
//                repoHSD.setRequestThreeConfirmed(true);
//            } else {
//                repoHSD.setRequestThreeConfirmed(false);
//            }
//            holder.confirmRequestName_3.setText(
//                    chavrutaRequestName3);
//
//            String request3AvatarNumber = repoHSD.getmChavrutaRequest3Avatar();
//            if (request3AvatarNumber.length() >= 3) {
//                byte[] decodeRequesterAvatar = Base64.decode(
//                        request3AvatarNumber, Base64.DEFAULT);
//
//
//                GlideApp
//                        .with(mContext)
//                        .asBitmap()
//                        .load(decodeRequesterAvatar)
//                        .circleCrop()
//                        .into(holder.confirmRequestAvatar_3);
//            } else {
//                holder.confirmRequestAvatar_3.setImageResource(avatarList.get(
//                        Integer.parseInt(request3AvatarNumber)));
//            }
//        } else{
//            holder.pendingRequest_3.setVisibility(View.GONE);
//            repoHSD.setRequestThreeConfirmed(false);
//            holder.underlinePendingRequest_3.setVisibility(View.GONE);
//        }
//    }

//    public void sendConfirmationtoDb(String chavrutaId, String requesterId) {
//        ServerConnect confirmChavrutaRequest = new ServerConnect(mContext);
//        confirmChavrutaRequest.execute("confirmChavrutaRequest", chavrutaId, requesterId);
//    }

//    private void setViewHolderConfirmations(HostSessionData currentItem, int requestClicked) {
//
//        switch (requestClicked) {
//            case 1:
//                //sets confirmed state for request 1
//                if (!currentItem.requestOneConfirmed) {
//                    currentItem.setRequestConfirmed(true, );
//                    currentItem.setmConfirmed(currentItem.getMchavrutaRequest1Id());
//                    confirmRequest_1.setBackgroundResource(R.drawable.ic_confirm_class);
//
//                    //set other request confirmations to false
//                    currentItem.setRequestTwoConfirmed(false);
//                    currentItem.setRequestThreeConfirmed(false);
//                    confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                    confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                } else {
//                    currentItem.setRequestConfirmed(false, );
//                    currentItem.setmConfirmed("0");
//                    confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                }
//
//                break;
//
//            case 2:
//                //sets confirmed or not
//                if (!currentItem.requestTwoConfirmed) {
//                    currentItem.setRequestTwoConfirmed(true);
//                    currentItem.setmConfirmed(currentItem.getMchavrutaRequest2Id());
//                    confirmRequest_2.setBackgroundResource(R.drawable.ic_confirm_class);
//
//
//                    //set other request confirmations to false
//                    currentItem.setRequestConfirmed(false, );
//                    currentItem.setRequestThreeConfirmed(false);
//                    confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                    confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                } else {
//                    currentItem.setRequestTwoConfirmed(false);
//                    currentItem.setmConfirmed("0");
//                    confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                }
//                break;
//
//            case 3:
//                //sets confirmed or not
//                if (!currentItem.requestThreeConfirmed) {
//                    currentItem.setRequestThreeConfirmed(true);
//                    currentItem.setmConfirmed(currentItem.getMchavrutaRequest3Id());
//                    //confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
//                    confirmRequest_3.setBackgroundResource(R.drawable.ic_confirm_class);
//
//                    //set other request confirmations to false
//                    currentItem.setRequestConfirmed(false, );
//                    currentItem.setRequestTwoConfirmed(false);
//                    confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                    confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                } else {
//                    currentItem.setRequestThreeConfirmed(false);
//                    currentItem.setmConfirmed("0");
//                    confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                }
//                break;
//
//            default:
//                return;
//        }
//        String chavrutaId = currentItem.getmChavrutaId();
//        sendConfirmationtoDb(chavrutaId, currentItem.getmConfirmed());
//    }


//    void bind(OpenChavrutaAdapter.ViewHolder holder, int listIndex) {
//            final int position = listIndex;
//            //@requestSlotOpen number of next class slot availiable for requester
//            //@requesterAvatar & @requesterName are vars that hold chavrutaInformation for display in hostview
//
//            final HostSessionData currentItem = mChavrutaSessionsAL.get(position);

//view is hosting a class
//            if (holder.getItemViewType() == 1 ) {
//                hostListItemView = true;
//                awaitingConfirmView = false;
//                holder.hostFullName.setText(UserDetails.getmUserName());
//
//                if (UserDetails.getmUserAvatarNumberString() != null &&
//                        !UserDetails.getmUserAvatarNumberString().equals("999")) {
//                    hostAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
//                            Integer.parseInt(UserDetails.getmUserAvatarNumberString())));
//                } else {
//                    //using custom avatar
//                    //check to see if custom avatar is in UserDetails
//                    if (UserDetails.getHostAvatarUri() != null) {
//                        GlideApp
//                                .with(mContext)
//                                .load(UserDetails.getHostAvatarUri())
//                                .centerCrop()
//                                .into(hostAvatar);
//                    } else if (getUserCustomAvatarBase64ByteArray() != null) {
//                        GlideApp
//                                .with(mContext)
//                                .asBitmap()
//                                .load(getUserCustomAvatarBase64ByteArray())
//                                .placeholder(R.drawable.ic_unknown_user)
//                                .centerCrop()
//                                .into(hostAvatar);
//                        //otherwise use xml unknown profile image
//                    }
//                }
//
//                //view is awaiting hosts confirmation
//            } else {
//                awaitingConfirmView = true;
//                hostListItemView = false;
//            }
//            //set initial confirmed state for awaiting confirmation list item
//            if (awaitingConfirmView) {
//                String learnerConfirmed = currentItem.getmConfirmed();
//                String currentHostAvatarNumberString = currentItem.getmHostAvatarNumber();
//
//                //sets user first last name after concatonation
//                String hostNameConcat = ChavrutaUtils.createUserFirstLastName(
//                        currentItem.getmHostFirstName(), currentItem.getmHostLastName());
//                holder.hostFullName.setText(hostNameConcat);
//
//                //checks if custom user byte array extant from MA parsing
//                if (currentHostAvatarNumberString != null &&
//                        currentHostAvatarNumberString.length() > AvatarImgs.avatarImgList.size()) {
//                    byte[] customHostAvatar = currentItem.getmHostCustomAvatarByteArray();
//                    GlideApp
//                            .with(mContext)
//                            .asBitmap()
//                            .load(customHostAvatar)
//                            .placeholder(R.drawable.ic_unknown_user)
//                            .centerCrop()
//                            .into(holder.hostAvatar);
//                    //checks if user using template avatar img
//                } else if (!currentHostAvatarNumberString.equals("999")) {
//                    holder.hostAvatar.setImageResource(avatarList.get(Integer.parseInt(currentItem.getmHostAvatarNumber())));
//                }
//
//                if (learnerConfirmed.equals(userId)) {
//                    holder.chavrutaConfirmed.setBackgroundResource(R.drawable.confirmed_rounded_corners);
//                    holder.chavrutaConfirmed.setText("Matched!");
//                } else {
//                    holder.chavrutaConfirmed.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
//                    holder.chavrutaConfirmed.setText("Awaiting" + System.getProperty("line.separator") + "Match");
//                }
//            }
//            //hosting a class list item
//            if (hostListItemView) {
//                String idOfConfirmedUser = currentItem.getmConfirmed();
//                String request1 = currentItem.getMchavrutaRequest1Id();
//                String request2 = currentItem.getMchavrutaRequest2Id();
//                String request3 = currentItem.getMchavrutaRequest3Id();
//                String chavrutaRequestName1 = currentItem.getmChavrutaRequest1Name();
//                String chavrutaRequestName2 = currentItem.getmChavrutaRequest2Name();
//                String chavrutaRequestName3 = currentItem.getmChavrutaRequest3Name();
//
//                Boolean isRequest1 = false;
//                Boolean isRequest2 = false;
//                Boolean isRequest3 = false;
//
//
//                //sets the initial status of confirmed button to check to indicate confirmed
//                if (request1.length() > 5) {
//                    isRequest1 = true;
//                    holder.pendingRequest_1.setVisibility(View.VISIBLE);
//                    holder.underlinePendingRequest_1.setVisibility(View.VISIBLE);
//                    if (idOfConfirmedUser.equals(request1) && currentItem.requestOneConfirmed) {
//                        mChavrutaSessionsAL.get(position).setRequestOneConfirmed(true);
//                        holder.confirmRequest_1.setBackgroundResource(R.drawable.ic_confirm_class);
//                        currentItem.setRequestOneConfirmed(true);
//                    } else {
//                        currentItem.setRequestOneConfirmed(false);
//                    }
//                    holder.confirmRequestName_1.setText(
//                            chavrutaRequestName1);
//                    String request1AvatarNumber = currentItem.getmChavrutaRequest1Avatar();
//                    if (request1AvatarNumber.length() >= 3) {
//                        byte[] decodeRequesterAvatar = Base64.decode(
//                                request1AvatarNumber, Base64.DEFAULT);
//
//                        GlideApp
//                                .with(mContext)
//                                .asBitmap()
//                                .load(decodeRequesterAvatar)
//                                .circleCrop()
//                                .into(holder.confirmRequestAvatar_1);
//                    } else {
//                        holder.confirmRequestAvatar_1.setImageResource(avatarList.get(
//                                Integer.parseInt(request1AvatarNumber)));
//                    }
//                } else {
//                    holder.pendingRequest_1.setVisibility(View.GONE);
//                    holder.underlinePendingRequest_1.setVisibility(View.GONE);
//                    currentItem.setRequestOneConfirmed(false);
//                }
//                if (request2.length() > 5) {
//                    holder.pendingRequest_2.setVisibility(View.VISIBLE);
//                    holder.underlinePendingRequest_2.setVisibility(View.VISIBLE);
//                    isRequest2 = true;
//                    if (idOfConfirmedUser.equals(request2) && currentItem.requestTwoConfirmed) {
//                        holder.confirmRequest_2.setBackgroundResource(R.drawable.ic_confirm_class);
//                        currentItem.setRequestTwoConfirmed(true);
//                    } else {
//                        currentItem.setRequestTwoConfirmed(false);
//                    }
//                    holder.confirmRequestName_2.setText(
//                            chavrutaRequestName2);
//                    String request2AvatarNumber = currentItem.getmChavrutaRequest2Avatar();
//                    if (request2AvatarNumber.length() >= 3) {
//                        byte[] decodeRequesterAvatar = Base64.decode(
//                                request2AvatarNumber, Base64.DEFAULT);
//
//                        GlideApp
//                                .with(mContext)
//                                .asBitmap()
//                                .load(decodeRequesterAvatar)
//                                .circleCrop()
//                                .into(holder.confirmRequestAvatar_2);
//                    } else {
//                        holder.confirmRequestAvatar_2.setImageResource(avatarList.get(
//                                Integer.parseInt(request2AvatarNumber)));
//                    }
//
//                } else {
//                    holder.pendingRequest_2.setVisibility(View.GONE);
//                    holder.underlinePendingRequest_2.setVisibility(View.GONE);
//                    currentItem.setRequestTwoConfirmed(false);
//                }
//                if (request3.length() > 5) {
//                    holder.pendingRequest_3.setVisibility(View.VISIBLE);
//                    holder.underlinePendingRequest_3.setVisibility(View.VISIBLE);
//                    isRequest3 = true;
//                    if (idOfConfirmedUser.equals(request3) && currentItem.requestThreeConfirmed) {
//                        holder.confirmRequest_3.setBackgroundResource(R.drawable.ic_confirm_class);
//                        currentItem.setRequestThreeConfirmed(true);
//                    } else {
//                        currentItem.setRequestThreeConfirmed(false);
//                    }
//                    holder.confirmRequestName_3.setText(
//                            chavrutaRequestName3);
//
//                    String request3AvatarNumber = currentItem.getmChavrutaRequest3Avatar();
//                    if (request3AvatarNumber.length() >= 3) {
//                        byte[] decodeRequesterAvatar = Base64.decode(
//                                request3AvatarNumber, Base64.DEFAULT);
//
//
//                        GlideApp
//                                .with(mContext)
//                                .asBitmap()
//                                .load(decodeRequesterAvatar)
//                                .circleCrop()
//                                .into(holder.confirmRequestAvatar_3);
//                    } else {
//                        holder.confirmRequestAvatar_3.setImageResource(avatarList.get(
//                                Integer.parseInt(request3AvatarNumber)));
//                    }
//                } else {
//                    holder.pendingRequest_3.setVisibility(View.GONE);
//                    currentItem.setRequestThreeConfirmed(false);
//                    holder.underlinePendingRequest_3.setVisibility(View.GONE);
//
//                }
//
//                //populates view if there is a request
//                if (isRequest1 || isRequest2 || isRequest3) {
//                    holder.pendingRequestLabel.setVisibility(View.VISIBLE);
//                    holder.noRequesterView.setVisibility(View.GONE);
//
//                } else {
//                    holder.pendingRequestLabel.setVisibility(View.GONE);
//                    holder.noRequesterView.setVisibility(View.VISIBLE);
//
//                }
//                //confirms or unconfirms requested view with color change indicator and db update
//                if (isRequest1) {
//                    holder.confirmRequest_1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            HostSessionData currentItem = mChavrutaSessionsAL.get(position);
//                            currentItem.getMchavrutaRequest1Id();
//
//                            // sets and sends to db hosts specific request confirmation
//                            setViewHolderConfirmations(currentItem, 1);
//                        }
//                    });
//                }
//
//                //sets confirmed state for request 2
//                if (isRequest2) {
//                    holder.confirmRequest_2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            HostSessionData currentItem = mChavrutaSessionsAL.get(position);
//
//                            // sets and sends to db hosts specific request confirmation
//                            setViewHolderConfirmations(currentItem, 2);
//                        }
//                    });
//                }
//
//
//                //sets confirmed state for request 3
//                if (isRequest3) {
//                    holder.confirmRequest_3.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            HostSessionData currentItem = mChavrutaSessionsAL.get(position);
//
//                            // sets and sends to db hosts specific request confirmation
//                            setViewHolderConfirmations(currentItem, 3);
//                        }
//                    });
//                }
//
//            }
//            holder.sessionDate.setText(currentItem.getmSessionDate());
//            holder.startTime.setText(currentItem.getmStartTime());
//            holder.endTime.setText(currentItem.getmEndTime());
//            String seferText = currentItem.getmSefer();
//            if(seferText.length() >= 30){
//                seferText = seferText.substring(0, 30) + "...";
//            }
//            holder.sefer.setText(seferText);
//            holder.location.setText(currentItem.getmLocation());
//            holder.itemView.setTag(position);
//
//
//            holder.knurling.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callback.getParentView();
//                }
//            });
//    }




