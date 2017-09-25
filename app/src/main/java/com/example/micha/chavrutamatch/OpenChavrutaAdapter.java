package com.example.micha.chavrutamatch;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.micha.chavrutamatch.Data.HostSessionData;

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

import java.security.Timestamp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.host;
import static android.R.attr.resource;
import static android.content.Context.MODE_PRIVATE;
import static com.example.micha.chavrutamatch.R.drawable.not_confirmed_rounded_corners;
import static com.example.micha.chavrutamatch.R.id.view;

/**
 * Created by micha on 7/22/2017.
 */

class OpenChavrutaAdapter extends RecyclerView.Adapter<OpenChavrutaAdapter.ViewHolder> {

//TODO use RoundedBitmapDrawable
    //RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap); drawable.setCircular(true);

    //number of views adapter will hold
    private int mNumberOfViews;
    private Context mContext;
    String userId =UserDetails.getmUserId();

    Context mainActivityContext;
    Context hostSelectContext;

    //holds viewType for relevant listItem
    Boolean hostListItemView;
    ArrayList<HostSessionData> mChavrutaSessionsAL;
    private static final String LOG_TAG = OpenChavrutaAdapter.class.getSimpleName();

    /**
     * Constructor using the context and the resource
     *
     * @param context the calling context/activity
     */
    public OpenChavrutaAdapter(Context context, ArrayList<HostSessionData> chavrutaSessionsArrayList) {
        this.mContext = context;
        mNumberOfViews = chavrutaSessionsArrayList.size();
        mChavrutaSessionsAL = chavrutaSessionsArrayList;
        mainActivityContext = MainActivity.mContext;
        hostSelectContext = HostSelect.mContext;
    }

    @Override
    public int getItemViewType(int position) {
        //selects hostId or userId for inflation
         HostSessionData chavrutaData = mChavrutaSessionsAL.get(position);
        String hostId = chavrutaData.getmHostId();
        if(hostId.equals(userId)){
            return 1;
        }else{return 0;}
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;

        if(context == hostSelectContext){
            layoutIdForListItem = R.layout.open_host_list_item;
            hostListItemView = false;
        }else {
            //returns requested views based upon calling activity
            if (viewType == 0) {
                layoutIdForListItem = R.layout.open_host_list_item;
                hostListItemView = false;
            } else {
                layoutIdForListItem = R.layout.hosting_chavrutas_list_item;
                hostListItemView = true;

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

    }


    @Override
    public int getItemCount() {
        return mNumberOfViews;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView hostFirstName, sessionDate, startTime, endTime, sefer, location;
        ImageButton hostInfo;
        LinearLayout pendingRequest_1;
        LinearLayout pendingRequest_2;
        LinearLayout pendingRequest_3;
        Button confirmRequest_1;
        Button confirmRequest_2;
        Button confirmRequest_3;
        Button chavrutaConfirmed;

        public ViewHolder(View listItemView) {
            super(listItemView);
            hostFirstName = (TextView) listItemView.findViewById(R.id.host_first_name);
            sessionDate = (TextView) listItemView.findViewById(R.id.session_date);
            startTime = (TextView) listItemView.findViewById(R.id.start_time);
            endTime = (TextView) listItemView.findViewById(R.id.end_time);
            sefer = (TextView) listItemView.findViewById(R.id.session_sefer);
            location = (TextView) listItemView.findViewById(R.id.location);
            hostInfo = (ImageButton) listItemView.findViewById(R.id.ib_add_match);
            pendingRequest_1 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_1);
            pendingRequest_2 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_2);
            pendingRequest_3 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_3);
            confirmRequest_1 = (Button) listItemView.findViewById(R.id.b_confirm_request_1);
            confirmRequest_2 = (Button) listItemView.findViewById(R.id.b_confirm_request_2);
            confirmRequest_3 = (Button) listItemView.findViewById(R.id.b_confirm_request_3);
            chavrutaConfirmed = (Button) listItemView.findViewById(R.id.b_chavruta_confirmed);
        }

        void bind(OpenChavrutaAdapter.ViewHolder holder, int listIndex) {
            final int position = listIndex;
            HostSessionData currentItem = mChavrutaSessionsAL.get(position);
            if (holder.getItemViewType() == 1 && mContext == mainActivityContext) {
                hostListItemView = true;
            } else {
                hostListItemView = false;
            }
            if (hostListItemView) {
                String idOfConfirmedUser = currentItem.getmConfirmed();
                String request1 = currentItem.getMchavrutaRequest1();
                String request2 = currentItem.getMchavrutaRequest2();
                String request3 = currentItem.getMchavrutaRequest3();
                Boolean isRequest1 = false;
                Boolean isRequest2 = false;
                Boolean isRequest3 = false;


                //sets the color of confirmed button to green to indicate confirmed
                if (request1 != null && currentItem.getMchavrutaRequest1().length() > 5) {
                    isRequest1=true;
                    holder.pendingRequest_1.setVisibility(View.VISIBLE);
                    if (idOfConfirmedUser.equals(request1)) {
                        holder.confirmRequest_1.setBackgroundColor(Color.parseColor("#10ef2e"));
                        currentItem.setRequestOneConfirmed(true);
                    }else{
                        currentItem.setRequestOneConfirmed(false);
                    }
                } else {
                    holder.pendingRequest_1.setVisibility(View.GONE);
                }
                if (request2 != null && request2.length() > 5) {
                    holder.pendingRequest_2.setVisibility(View.VISIBLE);
                    isRequest2 = true;
                    if (idOfConfirmedUser.equals(request2)) {
                        holder.confirmRequest_2.setBackgroundColor(Color.parseColor("#10ef2e"));
                        currentItem.setRequestTwoConfirmed(true);
                    }else{
                        currentItem.setRequestTwoConfirmed(false);
                    }
                } else {
                    holder.pendingRequest_1.setVisibility(View.GONE);
                }
                if (request3 != null && request3.length() > 5) {
                    holder.pendingRequest_3.setVisibility(View.VISIBLE);
                    isRequest3 = true;
                    if (idOfConfirmedUser.equals(request3)) {
                        holder.confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
                        currentItem.setRequestThreeConfirmed(true);
                    }else{
                        currentItem.setRequestThreeConfirmed(false);
                    }
                } else {
                    holder.pendingRequest_3.setVisibility(View.GONE);
                }

                //confirms or unconfirms requested view with color change indicator and db update
        if(isRequest1) {
            holder.confirmRequest_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HostSessionData currentItem = mChavrutaSessionsAL.get(position);

                    //sets confirmed or not
                    if(!currentItem.requestOneConfirmed) {
                        currentItem.setRequestOneConfirmed(true);
                        currentItem.setmConfirmed(currentItem.getMchavrutaRequest1());
                        v.setBackgroundColor(Color.parseColor("#10ef2e"));

                        //set other request confirmations to false
                        currentItem.setRequestTwoConfirmed(false);
                        currentItem.setRequestThreeConfirmed(false);
                        confirmRequest_2.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                        confirmRequest_3.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    }else{
                        currentItem.setRequestOneConfirmed(false);
                        currentItem.setmConfirmed("0");
                        v.setBackgroundResource(R.drawable.not_confirmed_rounded_corners);
                    }
                    String chavrutaId = currentItem.getmChavrutaId();
                    sendConfirmationtoDb(chavrutaId, currentItem.getmConfirmed());
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
        }
        //1934693520112171

    }

    public void add(HostSessionData dataAddedFromJson) {
        mChavrutaSessionsAL.add(dataAddedFromJson);
    }


    public void sendConfirmationtoDb(String chavrutaId, String requesterId) {
        ServerConnect confirmChavrutaRequest = new ServerConnect(mContext);
        confirmChavrutaRequest.execute("confirmChavrutaRequest", chavrutaId, requesterId);
    }
}



//
//        if(viewHolder.confirmRequest_2 != null) {
//            viewHolder.confirmRequest_2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String chavrutaId = currentItem.getmChavrutaId();
//                    sendConfirmationtoDb(chavrutaId, currentItem.getMchavrutaRequest2());
//                    //viewHolder.confirmRequest_2.setBackgroundColor(Color.parseColor("#10ef2e"));
//                }
//            });
//        }
//        if(viewHolder.confirmRequest_3 != null) {
//            viewHolder.confirmRequest_3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String chavrutaId = currentItem.getmChavrutaId();
//                    sendConfirmationtoDb(chavrutaId,currentItem.getMchavrutaRequest3());
//                    //viewHolder.confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
//                }
//            });
//        }


//        //on buttonClick selecting host
//        viewHolder.hostInfo.setOnClickListener(new View.OnClickListener()
//
//        {
//            @Override
//            public void onClick(View v) {
//
//                //Access the row position here to get the correct data item
//                int position = getPosition(currentItem);
//                hostSessionItemData = getItem(position);
//
//                //TODO: incorporate request date into db on select
//                long requestDate = System.currentTimeMillis();
//
//                String userId = UserDetails.getmUserId();
//                String chavrutaId = hostSessionItemData.getmChavrutaId();
//                String chavrutaRequest = "chavruta request";
//                ServerConnect attemptChavrutaRequest = new ServerConnect(mContext);
//                attemptChavrutaRequest.execute(chavrutaRequest, userId, chavrutaId);
//
//                Intent intent = new Intent(mContext, MainActivity.class);
//                mContext.startActivity(intent);
//
//            }
//        });
//
//        return listItemView;
//    }



