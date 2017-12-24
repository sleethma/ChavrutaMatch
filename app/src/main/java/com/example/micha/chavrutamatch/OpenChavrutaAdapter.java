package com.example.micha.chavrutamatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.HostSessionData;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.ChavrutaContract;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.OpenChavrutaAdapter;
import com.example.micha.chavrutamatch.Utils.ChavrutaUtils;
import com.example.micha.chavrutamatch.Utils.GlideApp;
import com.example.micha.chavrutamatch.Utils.ImgUtils;

import org.w3c.dom.Text;

import java.security.Timestamp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.host;
import static android.R.attr.resource;
import static android.R.attr.visible;
import static android.content.Context.MODE_PRIVATE;
import static com.example.micha.chavrutamatch.AcctLogin.UserDetails.getUserCustomAvatarBase64ByteArray;
import static com.example.micha.chavrutamatch.R.drawable.not_confirmed_rounded_corners;
import static com.example.micha.chavrutamatch.R.id.user_first_name;
import static com.example.micha.chavrutamatch.R.id.user_last_name;
import static com.example.micha.chavrutamatch.R.id.view;

/**
 * Created by micha on 7/22/2017.
 */

class OpenChavrutaAdapter extends RecyclerView.Adapter<OpenChavrutaAdapter.ViewHolder> {

    //number of views adapter will hold
    private int userAvatarNumberInt;
    private String userAvatarNumberString;
    private Context mContext;
    String userId = UserDetails.getmUserId();
    private Context mainActivityContext;
    private Context hostSelectContext;
    byte[] mUserAvatarByteArray;


    //holds viewType for relevant listItem
    Boolean hostListItemView;
    Boolean awaitingConfirmView;
    Boolean hostSelectView;
    final private int USER_IMG_AVATAR = 999;
    final private String USER_IMG_AVATAR_STRING = "999";

    ArrayList<HostSessionData> mChavrutaSessionsAL;
    List<Integer> avatarList = AvatarImgs.getAllAvatars();
    private static final String LOG_TAG = OpenChavrutaAdapter.class.getSimpleName();

    /**
     * Constructor using the context and the resource
     *
     * @param context the calling context/activity
     */
    public OpenChavrutaAdapter(Context context, ArrayList<HostSessionData> chavrutaSessionsArrayList) {
        this.mContext = context;
        mChavrutaSessionsAL = chavrutaSessionsArrayList;
        mainActivityContext = MainActivity.mContext;
        hostSelectContext = HostSelect.mContext;
        userAvatarNumberString = UserDetails.getmUserAvatarNumberString();
        userAvatarNumberInt = UserDetails.getmUserAvatarNumberString() != null ?
                Integer.parseInt(UserDetails.getmUserAvatarNumberString()) : 0;
        //calls so can access static user vars throughout adapter
//        orderArrayByDate(mChavrutaSessionsAL);
    }

    @Override
    public int getItemViewType(int position) {
        //selects hostId or userId for inflation
        HostSessionData chavrutaData = mChavrutaSessionsAL.get(position);
        String hostId = chavrutaData.getmHostId();
        // @return 1: hostview, else awaitingConfirmView
        if (hostId.equals(userId)) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;
        //user is viewing open classes
        if (context == hostSelectContext) {
            layoutIdForListItem = R.layout.open_host_list_item;
            hostListItemView = false;
            awaitingConfirmView = false;
            hostSelectView = true;

        } else {
            //view is awaiting class confirmation by host
            if (viewType == 0) {
                layoutIdForListItem = R.layout.my_chavruta_list_item;
                hostListItemView = false;
                awaitingConfirmView = true;
                hostSelectView = false;
                //view is user is hosting a class
            } else {
                layoutIdForListItem = R.layout.hosting_chavrutas_list_item;
                hostListItemView = true;
                awaitingConfirmView = false;
                hostSelectView = false;
            }
        }
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OpenChavrutaAdapter.ViewHolder holder, int position) {
        holder.bind(holder, position);
        holder.itemView.setTag(new Integer(position));

    }


    @Override
    public int getItemCount() {
        return mChavrutaSessionsAL.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView hostFirstName, sessionDate, startTime, endTime, sefer, location,
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

        public ViewHolder(View listItemView) {
            super(listItemView);
            hostFirstName = (TextView) listItemView.findViewById(R.id.host_first_name);
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
        }

        void bind(OpenChavrutaAdapter.ViewHolder holder, int listIndex) {
            final int position = listIndex;
            //@requestSlotOpen number of next class slot availiable for requester
            //@requesterAvatar & @requesterName are vars that hold chavrutaInformation for display in hostview
            final String requestSlotOpen;
            final String requesterAvatar;
            final String requesterName;
            final String requesterAvatarColumn;
            final String requesterNameColumn;
            final HostSessionData currentItem = mChavrutaSessionsAL.get(position);

            //view is hosting a class
            if (holder.getItemViewType() == 1 && mContext == mainActivityContext) {
                hostListItemView = true;
                awaitingConfirmView = false;

                if (UserDetails.getmUserAvatarNumberString() != null &&
                        !UserDetails.getmUserAvatarNumberString().equals("999")) {
                    hostAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
                            Integer.parseInt(UserDetails.getmUserAvatarNumberString())));
                } else {
                    //using custom avatar
                    //check to see if custom avatar is in UserDetails
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
                }


                requestSlotOpen = "0";
                requesterAvatar = null;
                requesterName = null;
                requesterAvatarColumn = null;
                requesterNameColumn = null;
                //view is awaiting hosts confirmation
            } else if (holder.getItemViewType() == 0 && mContext == mainActivityContext) {
                awaitingConfirmView = true;
                hostListItemView = false;
                requestSlotOpen = "0";
                requesterAvatar = null;
                requesterName = null;
                requesterAvatarColumn = null;
                requesterNameColumn = null;
            } else {
                //view is from HostSelect Context
                hostListItemView = false;
                awaitingConfirmView = false;
                //gets users current avatar
                if(UserDetails.getUserAvatarBase64String() != null){
                    requesterAvatar = UserDetails.getUserAvatarBase64String();
                }else {
                    requesterAvatar = UserDetails.getmUserAvatarNumberString();
                }
                String userFirstName = UserDetails.getmUserFirstName();
                String userLastName = UserDetails.getmUserLastName();
                requesterName = ChavrutaUtils.createUserFirstLastName(
                        userFirstName, userLastName);

                //check which request slot is availiable and pass name of db column to server for insert
                if (currentItem.getMchavrutaRequest1().length() < 5) {
                    requestSlotOpen = "chavruta_request_1";
                    requesterAvatarColumn = "chavruta_request_1_avatar";
                    requesterNameColumn = "chavruta_request_1_name";
                    holder.addHost.setImageResource(R.drawable.ib_add_match);
                } else if (currentItem.getMchavrutaRequest2().length() < 5) {
                    requestSlotOpen = "chavruta_request_2";
                    requesterAvatarColumn = "chavruta_request_2_avatar";
                    requesterNameColumn = "chavruta_request_2_name";
                    holder.addHost.setImageResource(R.drawable.ib_add_match);
                } else if (currentItem.getMchavrutaRequest3().length() < 5) {
                    requestSlotOpen = "chavruta_request_3";
                    requesterAvatarColumn = "chavruta_request_3_avatar";
                    requesterNameColumn = "chavruta_request_3_name";
                    holder.addHost.setImageResource(R.drawable.ib_add_match);
                } else {
                    requestSlotOpen = "0";
                    holder.addHost.setImageResource(R.drawable.b_class_full);
                    requesterAvatarColumn = null;
                    requesterNameColumn = null;
                }
            }
            //set initial confirmed state for awaiting confirmation list item
            if (awaitingConfirmView) {
                String learnerConfirmed = currentItem.getmConfirmed();
                String currentHostAvatarNumberString = currentItem.getmHostAvatarNumber();
                holder.hostUserName.setText(UserDetails.getmUserName());

                //checks if custom user byte array extant from MA parsing
                if (currentHostAvatarNumberString != null &&
                        currentHostAvatarNumberString.length() > AvatarImgs.avatarImgList.size()) {
                    byte[] customHostAvatar = currentItem.getmHostCustomAvatarByteArray();
                    GlideApp
                            .with(mContext)
                            .asBitmap()
                            .load(customHostAvatar)
                            .placeholder(R.drawable.ic_unknown_user)
                            .centerCrop()
                            .into(holder.hostAvatar);
                    //checks if user using template avatar img
                } else if (!currentHostAvatarNumberString.equals("999")) {
                    holder.hostAvatar.setImageResource(avatarList.get(Integer.parseInt(currentItem.getmHostAvatarNumber())));
                }

                holder.hostAvatar.setBackgroundResource(R.drawable.circle_background);

                if (learnerConfirmed.equals(userId)) {
                    holder.chavrutaConfirmed.setBackgroundResource(R.drawable.confirmed_rounded_corners);
                    holder.chavrutaConfirmed.setText("Matched!");
                } else {
                    holder.chavrutaConfirmed.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    holder.chavrutaConfirmed.setText("Awaiting" + System.getProperty("line.separator") + "Match");
                }
            }
            if (hostSelectView)

            {
                //sets chavrutahosts avatar
                String currentHostAvatarNumberString = currentItem.getmHostAvatarNumber();
                if (currentHostAvatarNumberString != null &&
                        currentHostAvatarNumberString.length() > AvatarImgs.avatarImgList.size()) {
                    byte[] customUserAvatar = currentItem.getByteArrayFromString(currentHostAvatarNumberString);
                    GlideApp
                            .with(mContext)
                            .asBitmap()
                            .load(customUserAvatar)
                            .placeholder(R.drawable.ic_unknown_user)
                            .centerCrop()
                            .into(holder.hostAvatar);
                } else if (!currentHostAvatarNumberString.equals("999")) {
                    holder.hostAvatar.setImageResource(avatarList.get(Integer.parseInt(currentItem.getmHostAvatarNumber())));
                }

                holder.hostAvatar.setBackgroundResource(R.drawable.circle_background);

                //sends requester's info to db as requesting class
                holder.addHost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (requestSlotOpen.equals("0")) {
                            Toast.makeText(mContext, "Class Full:Check Back!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String chavrutaId = currentItem.getmChavrutaId();

                        ServerConnect addHost = new ServerConnect(mContext);
                        addHost.execute("chavruta request", userId, chavrutaId, requestSlotOpen,
                                requesterAvatarColumn, requesterAvatar, requesterNameColumn, requesterName);

                        Intent intent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }
            //hosting a class list item
            if (hostListItemView)

            {
                String idOfConfirmedUser = currentItem.getmConfirmed();
                String request1 = currentItem.getMchavrutaRequest1();
                String request2 = currentItem.getMchavrutaRequest2();
                String request3 = currentItem.getMchavrutaRequest3();
                String chavrutaRequestName1 = currentItem.getmChavrutaRequest1Name();
                String chavrutaRequestName2 = currentItem.getmChavrutaRequest2Name();
                String chavrutaRequestName3 = currentItem.getmChavrutaRequest3Name();

                Boolean isRequest1 = false;
                Boolean isRequest2 = false;
                Boolean isRequest3 = false;


                //sets the initial status of confirmed button to check to indicate confirmed
                if (request1.length() > 5) {
                    isRequest1 = true;
                    holder.pendingRequest_1.setVisibility(View.VISIBLE);
                    holder.underlinePendingRequest_1.setVisibility(View.VISIBLE);
                    if (idOfConfirmedUser.equals(request1) && currentItem.requestOneConfirmed) {
                        mChavrutaSessionsAL.get(position).setRequestOneConfirmed(true);
                        holder.confirmRequest_1.setBackgroundResource(R.drawable.ic_confirm_class);
                        currentItem.setRequestOneConfirmed(true);
                    } else {
                        currentItem.setRequestOneConfirmed(false);
                    }
                    holder.confirmRequestName_1.setText(
                            chavrutaRequestName1);
                    String request1AvatarNumber = currentItem.getmChavrutaRequest1Avatar();
                    if (request1AvatarNumber.length() >= 3) {
                        byte[] decodeRequesterAvatar = Base64.decode(
                                request1AvatarNumber, Base64.DEFAULT);

                        GlideApp
                                .with(mContext)
                                .asBitmap()
                                .load(decodeRequesterAvatar)
                                .into(holder.confirmRequestAvatar_1);
                    } else {
                        holder.confirmRequestAvatar_1.setImageResource(avatarList.get(
                                Integer.parseInt(request1AvatarNumber)));
                    }
                } else {
                    holder.pendingRequest_1.setVisibility(View.GONE);
                    holder.underlinePendingRequest_1.setVisibility(View.GONE);
                    currentItem.setRequestOneConfirmed(false);
                }
                if (request2.length() > 5) {
                    holder.pendingRequest_2.setVisibility(View.VISIBLE);
                    holder.underlinePendingRequest_2.setVisibility(View.VISIBLE);
                    isRequest2 = true;
                    if (idOfConfirmedUser.equals(request2) && currentItem.requestTwoConfirmed) {
                        holder.confirmRequest_2.setBackgroundResource(R.drawable.ic_confirm_class);
                        currentItem.setRequestTwoConfirmed(true);
                    } else {
                        currentItem.setRequestTwoConfirmed(false);
                    }
                    holder.confirmRequestName_2.setText(
                            chavrutaRequestName2);
                    String request2AvatarNumber = currentItem.getmChavrutaRequest2Avatar();
                    if (request2AvatarNumber.length() >= 3) {
                        byte[] decodeRequesterAvatar = Base64.decode(
                                request2AvatarNumber, Base64.DEFAULT);

                        GlideApp
                                .with(mContext)
                                .asBitmap()
                                .load(decodeRequesterAvatar)
                                .into(holder.confirmRequestAvatar_2);
                    } else {
                        holder.confirmRequestAvatar_2.setImageResource(avatarList.get(
                                Integer.parseInt(request2AvatarNumber)));
                    }

                } else {
                    holder.pendingRequest_2.setVisibility(View.GONE);
                    holder.underlinePendingRequest_2.setVisibility(View.GONE);
                    currentItem.setRequestTwoConfirmed(false);
                }
                if (request3.length() > 5) {
                    holder.pendingRequest_3.setVisibility(View.VISIBLE);
                    holder.underlinePendingRequest_3.setVisibility(View.VISIBLE);
                    isRequest3 = true;
                    if (idOfConfirmedUser.equals(request3) && currentItem.requestThreeConfirmed) {
                        holder.confirmRequest_3.setBackgroundResource(R.drawable.ic_confirm_class);
                        currentItem.setRequestThreeConfirmed(true);
                    } else {
                        currentItem.setRequestThreeConfirmed(false);
                    }
                    holder.confirmRequestName_3.setText(
                            chavrutaRequestName3);

                    String request3AvatarNumber = currentItem.getmChavrutaRequest3Avatar();
                    if (request3AvatarNumber.length() >= 3) {
                        byte[] decodeRequesterAvatar = Base64.decode(
                                request3AvatarNumber, Base64.DEFAULT);


                        GlideApp
                                .with(mContext)
                                .asBitmap()
                                .load(decodeRequesterAvatar)
                                .into(holder.confirmRequestAvatar_3);
                    } else {
                        holder.confirmRequestAvatar_3.setImageResource(avatarList.get(
                                Integer.parseInt(request3AvatarNumber)));
                    }
                } else {
                    holder.pendingRequest_3.setVisibility(View.GONE);
                    currentItem.setRequestThreeConfirmed(false);
                    holder.underlinePendingRequest_3.setVisibility(View.GONE);

                }

                //populates view if there is a request
                if (isRequest1 || isRequest2 || isRequest3) {
                    holder.pendingRequestLabel.setVisibility(View.VISIBLE);
                    holder.noRequesterView.setVisibility(View.GONE);

                } else {
                    holder.pendingRequestLabel.setVisibility(View.GONE);
                    holder.noRequesterView.setVisibility(View.VISIBLE);

                }
                //confirms or unconfirms requested view with color change indicator and db update
                if (isRequest1) {
                    holder.confirmRequest_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HostSessionData currentItem = mChavrutaSessionsAL.get(position);
                            currentItem.getMchavrutaRequest1();

                            // sets and sends to db hosts specific request confirmation
                            setViewHolderConfirmations(currentItem, 1);
                        }
                    });
                }

                //sets confirmed state for request 2
                if (isRequest2) {
                    holder.confirmRequest_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HostSessionData currentItem = mChavrutaSessionsAL.get(position);

                            // sets and sends to db hosts specific request confirmation
                            setViewHolderConfirmations(currentItem, 2);
                        }
                    });
                }


                //sets confirmed state for request 3
                if (isRequest3) {
                    holder.confirmRequest_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HostSessionData currentItem = mChavrutaSessionsAL.get(position);

                            // sets and sends to db hosts specific request confirmation
                            setViewHolderConfirmations(currentItem, 3);
                        }
                    });
                }

            }

            holder.hostFirstName.setText(currentItem.getmHostFirstName());
            holder.sessionDate.setText(currentItem.getmSessionDate());
            holder.startTime.setText(currentItem.getmStartTime());
            holder.endTime.setText(currentItem.getmEndTime());
            holder.sefer.setText(currentItem.getmSefer());
            holder.location.setText(currentItem.getmLocation());
            holder.itemView.setTag(position);
        }

        private void setViewHolderConfirmations(HostSessionData currentItem, int requestClicked) {

            switch (requestClicked) {
                case 1:
                    //sets confirmed state for request 1
                    if (!currentItem.requestOneConfirmed) {
                        currentItem.setRequestOneConfirmed(true);
                        currentItem.setmConfirmed(currentItem.getMchavrutaRequest1());
                        confirmRequest_1.setBackgroundResource(R.drawable.ic_confirm_class);

                        //set other request confirmations to false
                        currentItem.setRequestTwoConfirmed(false);
                        currentItem.setRequestThreeConfirmed(false);
                        confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                        confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    } else {
                        currentItem.setRequestOneConfirmed(false);
                        currentItem.setmConfirmed("0");
                        confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    }

                    break;

                case 2:
                    //sets confirmed or not
                    if (!currentItem.requestTwoConfirmed) {
                        currentItem.setRequestTwoConfirmed(true);
                        currentItem.setmConfirmed(currentItem.getMchavrutaRequest2());
                        confirmRequest_2.setBackgroundResource(R.drawable.ic_confirm_class);


                        //set other request confirmations to false
                        currentItem.setRequestOneConfirmed(false);
                        currentItem.setRequestThreeConfirmed(false);
                        confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                        confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    } else {
                        currentItem.setRequestTwoConfirmed(false);
                        currentItem.setmConfirmed("0");
                        confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    }
                    break;

                case 3:
                    //sets confirmed or not
                    if (!currentItem.requestThreeConfirmed) {
                        currentItem.setRequestThreeConfirmed(true);
                        currentItem.setmConfirmed(currentItem.getMchavrutaRequest3());
                        //confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
                        confirmRequest_3.setBackgroundResource(R.drawable.ic_confirm_class);

                        //set other request confirmations to false
                        currentItem.setRequestOneConfirmed(false);
                        currentItem.setRequestTwoConfirmed(false);
                        confirmRequest_1.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                        confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    } else {
                        currentItem.setRequestThreeConfirmed(false);
                        currentItem.setmConfirmed("0");
                        confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    }
                    break;

                default:
                    return;
            }
            String chavrutaId = currentItem.getmChavrutaId();
            sendConfirmationtoDb(chavrutaId, currentItem.getmConfirmed());
        }

    }

    public void add(HostSessionData dataAddedFromJson) {
        mChavrutaSessionsAL.add(dataAddedFromJson);
    }

    public void sendConfirmationtoDb(String chavrutaId, String requesterId) {
        ServerConnect confirmChavrutaRequest = new ServerConnect(mContext);
        confirmChavrutaRequest.execute("confirmChavrutaRequest", chavrutaId, requesterId);
    }

    public void deleteMyChavrutaArrayItemOnSwipe(int indexToDelete) {
        HostSessionData currentItem = mChavrutaSessionsAL.get(indexToDelete);
        //get either HostView or AwaitingConfirmView
        int itemViewType = getItemViewType(indexToDelete);
        //awaiting confirm view db delete
        if (itemViewType == 0) {
            String requesterNumber;
            String requesterAvatarColumn;
            String requesterNameColumn;

            if (userId.equals(currentItem.getMchavrutaRequest1())) {
                requesterNumber = "chavruta_request_1";
                requesterAvatarColumn = "chavruta_request_1_avatar";
                requesterNameColumn = "chavruta_request_1_name";
            } else if (userId.equals(currentItem.getMchavrutaRequest2())) {
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
//        this.notifyItemRemoved(indexToDelete);
        this.notifyDataSetChanged();


        if (mChavrutaSessionsAL.size() == 0) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        }

    }

}




