package com.example.micha.chavrutamatch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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


        //attach listener to listItemViewButton

        if (listItemView == null) {
            viewHolder = new ViewHolder();


            //sets correct listItemView based on Caller's Context
            if (mContext == MainActivity.getMAContextForAdapter()) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_chavruta_list_item, parent, false);
            } else {
                //adapter called from HostSelect.class
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.open_host_list_item, parent, false);

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

            //cache the viewHoslder object inside the fresh view
            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }
        String userId = UserDetails.getmUserId();
        String hostId = hostSessionDatas.getmHostId();
        if(hostId.equals(userId)){
            listItemView.setBackgroundColor(Color.parseColor("#BF0409"));
        }
        viewHolder.hostFirstName.setText(hostSessionDatas.getmHostFirstName());
        viewHolder.sessionDate.setText(hostSessionDatas.getmSessionDate());
        viewHolder.startTime.setText(hostSessionDatas.getmStartTime());
        viewHolder.endTime.setText(hostSessionDatas.getmEndTime());
        viewHolder.sefer.setText(hostSessionDatas.getmSefer());
        viewHolder.location.setText(hostSessionDatas.getmLocation());

        //on buttonClick selecting host
        viewHolder.hostInfo.setOnClickListener(new View.OnClickListener() {
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

                //TODO: delete below object
                //creates MyChavruta object joining user and host data
//                MyChavrutas chavruta = new MyChavrutas(requestDate, hostSessionData);

                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);

//                //Access the row position here to get the correct data item
//                HostSessionData hostSessionItemData = getItem(position);
            }
        });
        return listItemView;
    }

    private static class ViewHolder {
        TextView hostFirstName, sessionDate, startTime, endTime, sefer, location;
        ImageButton hostInfo;
        Button chavrutaConfirmed;

    }
}

