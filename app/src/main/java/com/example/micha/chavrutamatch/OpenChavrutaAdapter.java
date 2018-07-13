package com.example.micha.chavrutamatch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MVPConstructs.Repos.MARepoContract;
import com.example.micha.chavrutamatch.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;


/**
 * Created by micha on 7/22/2017.
 */

public class OpenChavrutaAdapter extends RecyclerView.Adapter<OpenChavrutaAdapter.ViewHolder> {

    //number of views adapter will hold
    private Context mContext;
    private String userId;
    private Context mainActivityContext;
    //@var used to control swipe on delete Dialogue selection
    private static boolean mConfirmed = false;

    //interface for getting MainActivity context
    private ParentView callback = (ParentView) MainActivity.mContext;


    private MAContractMVP.Presenter presenter;
    private UserDetails userDetailsInstance;

    private int requesterNumber;

    //holds viewType for relevant listItem
    Boolean hostListItemView;
    Boolean awaitingConfirmView;
    private static ArrayList<ServerResponse> mChavrutaSessionsAL;
    private List<Integer> avatarList = AvatarImgs.getAllAvatars();

    public OpenChavrutaAdapter(Context context, ArrayList<ServerResponse> chavrutaSessionsArrayList, MAContractMVP.Presenter presenter, UserDetails userDetailsInstance) {
        this.mContext = context;
        this.presenter = presenter;
        mChavrutaSessionsAL = chavrutaSessionsArrayList;
        mainActivityContext = MainActivity.mContext;
        this.userDetailsInstance = userDetailsInstance;
        userId = userDetailsInstance.getUserId();
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
            hostFullName =  listItemView.findViewById(R.id.host_full_name);
            sessionDate =  listItemView.findViewById(R.id.session_date);
            startTime = listItemView.findViewById(R.id.start_time);
            endTime =  listItemView.findViewById(R.id.end_time);
            sefer =  listItemView.findViewById(R.id.session_sefer);
            location = listItemView.findViewById(R.id.location);
            addHost =  listItemView.findViewById(R.id.ib_add_match);
            pendingRequest_1 =  listItemView.findViewById(R.id.ll_requester_viewgroup_1);
            pendingRequest_2 =  listItemView.findViewById(R.id.ll_requester_viewgroup_2);
            pendingRequest_3 =  listItemView.findViewById(R.id.ll_requester_viewgroup_3);
            underlinePendingRequest_1 =  listItemView.findViewById(R.id.v_underline_requester_1);
            underlinePendingRequest_2 =  listItemView.findViewById(R.id.v_underline_requester_2);
            underlinePendingRequest_3 =  listItemView.findViewById(R.id.v_underline_requester_3);
            pendingRequestLabel =  listItemView.findViewById(R.id.tv_requests_label);
            hostUserName = listItemView.findViewById(R.id.host_user_name);
            confirmRequestName_1 =  listItemView.findViewById(R.id.tv_confirm_request_1);
            confirmRequestName_2 = listItemView.findViewById(R.id.tv_confirm_request_2);
            confirmRequestName_3 = listItemView.findViewById(R.id.tv_confirm_request_3);
            confirmRequestAvatar_1 =  listItemView.findViewById(R.id.iv_user_request_1_avatar);
            confirmRequestAvatar_2 =  listItemView.findViewById(R.id.iv_user_request_2_avatar);
            confirmRequestAvatar_3 = listItemView.findViewById(R.id.iv_user_request_3_avatar);
            confirmRequest_1 =  listItemView.findViewById(R.id.b_confirm_request_1);
            confirmRequest_2 = listItemView.findViewById(R.id.b_confirm_request_2);
            confirmRequest_3 =  listItemView.findViewById(R.id.b_confirm_request_3);
            chavrutaConfirmed =  listItemView.findViewById(R.id.b_chavruta_confirmed);
            noRequesterView = listItemView.findViewById(R.id.fl_awaiting_requester);
            hostAvatar = listItemView.findViewById(R.id.iv_host_avatar);
            awaitingHostAvatar = listItemView.findViewById(R.id.iv_awaiting_host_avatar);
            knurling = listItemView.findViewById(R.id.iv_knurling);
        }

        @Nullable
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
        public void setViewItemDataInMyChavruta(ServerResponse chavruta, int position) {
            sessionDate.setText(chavruta.getSessionDate());
            startTime.setText(chavruta.getStartTime());
            endTime.setText(chavruta.getEndTime());
            String seferText = chavruta.getSefer();
            if (seferText.length() >= 30) {
                seferText = seferText.substring(0, 30) + "...";
            }
            sefer.setText(seferText);
            location.setText(chavruta.getLocation());
            itemView.setTag(position);

            knurling.setOnClickListener(v -> callback.getParentView());
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
            hostAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
                    Integer.parseInt(userDetailsInstance.getmUserAvatarNumberString())));
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
            if (userDetailsInstance.getUserAvatarUri() != null) {
                GlideApp
                        .with(mContext)
                        .load(userDetailsInstance.getUserAvatarUri())
                        .centerCrop()
                        .into(hostAvatar);
            } else if (userDetailsInstance.getUserCustomAvatarBase64ByteArray() != null) {
                GlideApp
                        .with(mContext)
                        .asBitmap()
                        .load(userDetailsInstance.getUserCustomAvatarBase64ByteArray())
                        .placeholder(R.drawable.ic_unknown_user)
                        .centerCrop()
                        .into(hostAvatar);
            }
        }

        @Override
        public void setUsersFullName(String text) {
            hostFullName.setText(text);
        }

        @Override
        public void setOnClickListenerOnRequester(final MARepoContract holder, final ServerResponse chavruta, final int requesterNumber) {
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
            requestButtonView.setTag(requestButtonView.getId(), requesterNumber);
            requestButtonView.setOnClickListener(v -> {
               int requesterNumber1 = (int) v.getTag(v.getId());
                // sets and sends to db hosts specific request confirmation
                // setViewHolderConfirmations(currentItem, 3);
                // sets and sends to db hosts specific request confirmation
                presenter.setViewHolderConfirmations(chavruta, holder, requesterNumber1);
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

    public void deleteMyChavrutaArrayItemOnSwipe(int indexToDelete, int viewTypeToDelete) {
        ServerResponse currentItem = mChavrutaSessionsAL.get(indexToDelete);
        String chavrutaId = currentItem.getChavrutaId();
        //get either HostView or AwaitingConfirmView
        int itemViewType = viewTypeToDelete;
        //awaiting confirm view db delete
        if (itemViewType == 0) {

            String requesterNumber;
            String requesterAvatarColumn;
            String requesterNameColumn;

            if (userId.equals(currentItem.getChavrutaRequest1())) {
                requesterNumber = "chavruta_request_1";
                requesterAvatarColumn = "chavruta_request_1_avatar";
                requesterNameColumn = "chavruta_request_1_name";
            } else if (userId.equals(currentItem.getChavrutaRequest2())) {
                requesterNumber = "chavruta_request_2";
                requesterAvatarColumn = "chavruta_request_2_avatar";
                requesterNameColumn = "chavruta_request_2_name";
            } else {
                requesterNumber = "chavruta_request_3";
                requesterAvatarColumn = "chavruta_request_3_avatar";
                requesterNameColumn = "chavruta_request_3_name";
            }
            String awaitingConfirmKey = "chavruta request";
            ServerConnect dbAwaitingConfirmDelete = new ServerConnect(mainActivityContext, userDetailsInstance);
            dbAwaitingConfirmDelete.execute(awaitingConfirmKey, "0", chavrutaId, requesterNumber,
                    requesterAvatarColumn, "0", requesterNameColumn, "0");


        } else {
            String deleteChavrutaKey = "delete chavruta";
            ServerConnect deleteChavruta = new ServerConnect(mainActivityContext, userDetailsInstance);
            deleteChavruta.execute(deleteChavrutaKey, chavrutaId);
        }
        mChavrutaSessionsAL.remove(indexToDelete);
        this.notifyItemRemoved(indexToDelete);

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

    public ArrayList<ServerResponse> getmChavrutaSessionsAL() {
        return mChavrutaSessionsAL;
    }
}