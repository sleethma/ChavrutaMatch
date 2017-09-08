//package com.example.micha.chavrutamatch;
//
//import android.content.Context;
//import com.example.micha.chavrutamatch.Data.HostSessionData;
//import android.widget.ArrayAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
///**
// * Created by micha on 9/4/2017.
// */
//
//public class MyChavrutasAdapter extends ArrayAdapter<HostSessionData> {
//    private Context mContext;
//
//    public MyChavrutasAdapter(Context context, ArrayList<HostSessionData> myChavrutaSessionsDataArrayList){
//        super(context,R.layout.activity_main , myChavrutaSessionsDataArrayList);
//        this.mContext = context;
//
//    }
//
//    @Override
//    public HostSessionData getItem(int position) {
//        return super.getItem(position);
//    }
//
//    @Override
//    public int getCount() {
//        return super.getCount();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        //get data item for position
//        final HostSessionData hostSessionDatas = getItem(position);
//        final MyChavrutasAdapter.ViewHolder viewHolder;
//        View listItemView = convertView;
//
//
//        //attach listener to listItemViewButton
//
//        if (listItemView == null) {
//            viewHolder = new  ViewHolder();
//            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.open_host_list_item, parent, false);
//            //look up view for data population
//            viewHolder.hostFirstName = (TextView) listItemView.findViewById(R.id.host_first_name);
//            viewHolder.sessionDate = (TextView) listItemView.findViewById(R.id.session_date);
//            viewHolder.startTime = (TextView) listItemView.findViewById(R.id.start_time);
//            viewHolder.endTime = (TextView) listItemView.findViewById(R.id.end_time);
//            viewHolder.sefer = (TextView) listItemView.findViewById(R.id.session_sefer);
//            viewHolder.location = (TextView) listItemView.findViewById(R.id.location);
//            viewHolder.hostInfo = (ImageButton) listItemView.findViewById(R.id.ib_add_match);
//
//            //cache the viewHoslder object inside the fresh view
//            listItemView.setTag(viewHolder);
//        } else {
//            viewHolder = (MyChavrutasAdapter.ViewHolder) listItemView.getTag();
//        }
//
//        viewHolder.hostFirstName.setText(hostSessionDatas.getmHostFirstName());
//        viewHolder.sessionDate.setText(hostSessionDatas.getmSessionDate());
//        viewHolder.startTime.setText(hostSessionDatas.getmStartTime());
//        viewHolder.endTime.setText(hostSessionDatas.getmEndTime());
//        viewHolder.sefer.setText(hostSessionDatas.getmSefer());
//        viewHolder.location.setText(hostSessionDatas.getmLocation());
//
//        //cache row position inside the button using 'setTag'
//        viewHolder.hostInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Access the row position here to get the correct data item
//                int position = getPosition(hostSessionDatas);
//                HostSessionData hostSessionItemData = getItem(position);
//                String[] hostSessionData = hostSessionItemData.getAllHostDataForMyChavruta();
//                long requestDate = System.currentTimeMillis();
//
//                //creates MyChavruta object joining user and host data
//                MyChavrutas chavruta = new MyChavrutas(requestDate, hostSessionData);
//
////                Intent intent = new Intent(mContext, AddBio.class);
////                mContext.startActivity(intent);
//
////                //Access the row position here to get the correct data item
////                HostSessionData hostSessionItemData = getItem(position);
//            }
//        });
//        return listItemView;
//    }
//
//    private static class ViewHolder {
//        TextView hostFirstName, sessionDate, startTime, endTime, sefer, location;
//        ImageButton hostInfo;
//
//    }
//
//}
