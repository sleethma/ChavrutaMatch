package com.example.micha.chavrutamatch;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.micha.chavrutamatch.Data.ChavrutaContract;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.OpenChavrutaAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.resource;

/**
 * Created by micha on 7/22/2017.
 */

public class OpenChavrutaAdapter extends ArrayAdapter<HostSessionData> {

//TODO use RoundedBitmapDrawable
    //RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap); drawable.setCircular(true);
//TODO see if cursor needed only for sqlite local db. If so setup from waitlist app

    // Holds on to the cursor to display the waitlist
    private Context mContext;


    /**
     * Constructor using the context and the resource
     *
     * @param context the calling context/activity
     */
    public OpenChavrutaAdapter(Context context, ArrayList<HostSessionData> hostSessionArrayList) {
        super(context,0, hostSessionArrayList);
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
        HostSessionData hostSessionDatas = getItem(position);
        View listItemView = convertView;


        //attach listener to listItemViewButton

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.open_host_list_item, parent, false);
        }
        ImageButton hostInfo = (ImageButton) listItemView.findViewById(R.id.ib_add_match);

        //cache row position inside the button using 'setTag'
        hostInfo.setTag(position);
        hostInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AddBio.class);
                mContext.startActivity(intent);

//                int position = (Integer) hostInfo.getTag();
//                //Access the row position here to get the correct data item
//                HostSessionData hostSessionItemData = getItem(position);
            }
        });
        TextView hostFirstName, sessionDate, startTime, endTime, sefer, location;
        //look up view for data population
            hostFirstName = (TextView) listItemView.findViewById(R.id.host_first_name);
            sessionDate = (TextView) listItemView.findViewById(R.id.session_date);
            startTime = (TextView) listItemView.findViewById(R.id.start_time);
            endTime= (TextView) listItemView.findViewById(R.id.end_time);
            sefer = (TextView) listItemView.findViewById(R.id.session_sefer);
            location = (TextView) listItemView.findViewById(R.id.location);

        hostFirstName.setText(hostSessionDatas.getmHostFirstName());
        sessionDate.setText(hostSessionDatas.getmSessionDate());
        startTime.setText(hostSessionDatas.getmStartTime());
        endTime.setText(hostSessionDatas.getmEndTime());
       sefer.setText(hostSessionDatas.getmSefer());
        location.setText(hostSessionDatas.getmLocation());

        return listItemView;
    }
}

