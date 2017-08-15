package com.example.micha.chavrutamatch;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.micha.chavrutamatch.Data.ChavrutaContract;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.OpenChavrutaAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.R.attr.resource;

/**
 * Created by micha on 7/22/2017.
 */

public class OpenChavrutaAdapter extends ArrayAdapter {

//TODO use RoundedBitmapDrawable
    //RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap); drawable.setCircular(true);
//TODO see if cursor needed only for sqlite local db. If so setup from waitlist app

    // Holds on to the cursor to display the waitlist
    private Context mContext;
    public List hostList = new ArrayList();


    /**
     * Constructor using the context and the resource
     *
     * @param context the calling context/activity
     */
    public OpenChavrutaAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return hostList.size();
    }

    @Override
    public Object getItem(int position) {
        return hostList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        HostDataHolder hostDataHolder;
        if(listItemView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            listItemView = inflater.inflate(R.layout.open_host_list_item, parent, false);
            //initialize and set views in UserDataHolder.class
            hostDataHolder = new HostDataHolder();
            hostDataHolder.hostFirstName = (TextView) listItemView.findViewById(R.id.host_user_name);
            hostDataHolder.sessionDate = (TextView) listItemView.findViewById(R.id.session_date);
            hostDataHolder.startTime = (TextView) listItemView.findViewById(R.id.start_time);
            hostDataHolder.endTime= (TextView) listItemView.findViewById(R.id.end_time);
            hostDataHolder.sefer = (TextView) listItemView.findViewById(R.id.session_sefer);
            hostDataHolder.location = (TextView) listItemView.findViewById(R.id.location);

            //TODO look up what setTag does
            listItemView.setTag(hostDataHolder);
        }else{
            hostDataHolder = (HostDataHolder) listItemView.getTag();
        }
        HostSessionData hostSessionData = (HostSessionData) this.getItem(position);
        hostDataHolder.hostFirstName.setText(hostSessionData.getmHostFirstName());
        hostDataHolder.sessionDate.setText(hostSessionData.getmSessionDate());
        hostDataHolder.startTime.setText(hostSessionData.getmStartTime());
        hostDataHolder.endTime.setText(hostSessionData.getmEndTime());
        hostDataHolder.sefer.setText(hostSessionData.getmSefer());
        hostDataHolder.location.setText(hostSessionData.getmLocation());

        return listItemView;
    }

    public void add(HostSessionData object) {
        super.add(object);
        hostList.add(object);
    }

    static class HostDataHolder{
        TextView hostFirstName, sessionDate, startTime, endTime, sefer, location;
    }
}

