package com.example.micha.chavrutamatch;

import android.content.Context;
import android.graphics.Color;
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
import static com.example.micha.chavrutamatch.R.id.view;

/**
 * Created by micha on 7/22/2017.
 */

public class OpenChavrutaAdapter extends ArrayAdapter<HostSessionData> {

//TODO use RoundedBitmapDrawable
    //RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap); drawable.setCircular(true);
//TODO see if cursor needed only for sqlite local db. If so setup from waitlist app

    // Holds on to the cursor to display the waitlist
    private Context mContext;
    private static final String LOG_TAG = OpenChavrutaAdapter.class.getSimpleName();
    HostSessionData hostSessionItemData;

    /**
     * Constructor using the context and the resource
     *
     * @param context the calling context/activity
     */
    public OpenChavrutaAdapter(Context context, ArrayList<HostSessionData> hostSessionArrayList) {
        super(context, 0, hostSessionArrayList);
        this.mContext = context;
    }

    @Override
    public HostSessionData getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get data item for position
        final HostSessionData hostSessionDatas = getItem(position);
        final ViewHolder viewHolder;
        View listItemView = convertView;
        Boolean hostListItemView = false;
        String userId = UserDetails.getmUserId();
        final String hostId = hostSessionDatas.getmHostId();
        final String mUserRequestId1;
        final String mUserRequestId2;
        final String mUserRequestId3;


        Context mainActivityContext = MainActivity.getMAContextForAdapter();


        if (listItemView == null) {
            viewHolder = new ViewHolder();

            //sets correct listItemView based on Caller's Context
            if (hostId.equals(userId) && mContext == mainActivityContext) {
                hostListItemView = true;
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.hosting_chavrutas_list_item, parent, false);
                listItemView.setBackgroundColor(Color.parseColor("#BF0409"));


            } else if (mContext == mainActivityContext) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_chavruta_list_item, parent, false);
                hostListItemView = false;
            } else {
                //adapter called from HostSelect.class
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.open_host_list_item, parent, false);
                hostListItemView = false;
            }


            //look up view for data population
            viewHolder.hostFirstName = (TextView) listItemView.findViewById(R.id.host_first_name);
            viewHolder.sessionDate = (TextView) listItemView.findViewById(R.id.session_date);
            viewHolder.startTime = (TextView) listItemView.findViewById(R.id.start_time);
            viewHolder.endTime = (TextView) listItemView.findViewById(R.id.end_time);
            viewHolder.sefer = (TextView) listItemView.findViewById(R.id.session_sefer);
            viewHolder.location = (TextView) listItemView.findViewById(R.id.location);
            viewHolder.hostInfo = (ImageButton) listItemView.findViewById(R.id.ib_add_match);
            viewHolder.chavrutaConfirmed = (Button) listItemView.findViewById(R.id.b_chavruta_confirmed);
            viewHolder.pendingRequest_1 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_1);
            viewHolder.pendingRequest_2 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_2);
            viewHolder.pendingRequest_3 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_3);
            viewHolder.confirmRequest_1 = (Button) listItemView.findViewById(R.id.b_confirm_request_1);
            viewHolder.confirmRequest_2 = (Button) listItemView.findViewById(R.id.b_confirm_request_2);
            viewHolder.confirmRequest_3 = (Button) listItemView.findViewById(R.id.b_confirm_request_3);

            //cache the viewHoslder object inside the fresh view
            listItemView.setTag(viewHolder);

        } else {
            //view is already been populated
            viewHolder = (ViewHolder) listItemView.getTag();
        }
        //check if chavruta has been requested by any user and show views with requests
        if (hostListItemView) {

            if (hostSessionDatas.getMchavrutaRequest1().length() > 5) {
                viewHolder.pendingRequest_1.setVisibility(View.VISIBLE);

            }
            if (hostSessionDatas.getMchavrutaRequest2().length() > 5) {
                viewHolder.pendingRequest_2.setVisibility(View.VISIBLE);
            }
            if (hostSessionDatas.getMchavrutaRequest3().length() > 5) {
                viewHolder.pendingRequest_3.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.hostFirstName.setText(hostSessionDatas.getmHostFirstName());
        viewHolder.sessionDate.setText(hostSessionDatas.getmSessionDate());
        viewHolder.startTime.setText(hostSessionDatas.getmStartTime());
        viewHolder.endTime.setText(hostSessionDatas.getmEndTime());
        viewHolder.sefer.setText(hostSessionDatas.getmSefer());
        viewHolder.location.setText(hostSessionDatas.getmLocation());
//        if(viewHolder.confirmRequest_1 != null && confirmed) {
//            viewHolder.confirmRequest_1.setBackgroundColor(Color.parseColor("#13f717"));
//        }


        //set listener for confirm requests
        if (hostListItemView) {
        }


        //todo send host selected user's id to confirmed field in db
        //on confirming chavrutas 1, 2, and three
        if(viewHolder.confirmRequest_1 != null) {
            viewHolder.confirmRequest_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Access the row position here to get the correct data item
                    int position = getPosition(hostSessionDatas);
                    hostSessionItemData = getItem(position);
                    String chavrutaId = hostSessionItemData.getmChavrutaId();
                    sendConfirmationtoDb(chavrutaId, hostSessionDatas.getMchavrutaRequest1());
                    viewHolder.confirmRequest_1.setBackgroundColor(Color.parseColor("#10ef2e"));
                }
            });
        }
        if(viewHolder.confirmRequest_2 != null) {
            viewHolder.confirmRequest_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chavrutaId = hostSessionDatas.getmChavrutaId();
                    sendConfirmationtoDb(chavrutaId, hostSessionDatas.getMchavrutaRequest2());
                    viewHolder.confirmRequest_2.setBackgroundColor(Color.parseColor("#10ef2e"));

                }
            });
        }
        if(viewHolder.confirmRequest_3 != null) {
            viewHolder.confirmRequest_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chavrutaId = hostSessionDatas.getmChavrutaId();
                    sendConfirmationtoDb(chavrutaId,hostSessionDatas.getMchavrutaRequest3());
                    viewHolder.confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
                }
            });
        }
        //on buttonClick selecting host
        viewHolder.hostInfo.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                //Access the row position here to get the correct data item
                int position = getPosition(hostSessionDatas);
                hostSessionItemData = getItem(position);

                //TODO: incorporate request date into db on select
                long requestDate = System.currentTimeMillis();

                String userId = UserDetails.getmUserId();
                String chavrutaId = hostSessionItemData.getmChavrutaId();
                String chavrutaRequest = "chavruta request";
                ServerConnect attemptChavrutaRequest = new ServerConnect(mContext);
                attemptChavrutaRequest.execute(chavrutaRequest, userId, chavrutaId);

                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);

            }
        });

        return listItemView;
    }

    private void setClicked(int requestPositionClicked, View listItemView){
        listItemView.setBackgroundColor(Color.parseColor("#10ef2e"));
    }
    private static class ViewHolder {
        TextView hostFirstName, sessionDate, startTime, endTime, sefer, location;
        ImageButton hostInfo;
        LinearLayout pendingRequest_1;
        LinearLayout pendingRequest_2;
        LinearLayout pendingRequest_3;
        Button confirmRequest_1;
        Button confirmRequest_2;
        Button confirmRequest_3;
        Button chavrutaConfirmed;

    }



    public void sendConfirmationtoDb(String chavrutaId, String requesterId){
        ServerConnect confirmChavrutaRequest = new ServerConnect(mContext);
        confirmChavrutaRequest.execute("confirmChavrutaRequest", chavrutaId, requesterId);

    }
}

