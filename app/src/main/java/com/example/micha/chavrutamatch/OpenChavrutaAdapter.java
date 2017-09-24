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

class OpenChavrutaAdapter extends RecyclerView.Adapter<OpenChavrutaAdapter.ViewHolder> {

//TODO use RoundedBitmapDrawable
    //RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap); drawable.setCircular(true);

    //number of views adapter will hold
    private int mNumberOfViews;
    private Context mContext;
    String userId =UserDetails.getmUserId();

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
        if (viewType == 0) {
            layoutIdForListItem = R.layout.open_host_list_item;
            hostListItemView = false;
        }else{
            layoutIdForListItem = R.layout.hosting_chavrutas_list_item;
            hostListItemView = true;
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
            HostSessionData currentItem = mChavrutaSessionsAL.get(listIndex);
            if (holder.getItemViewType() == 1) {
                hostListItemView = true;
            } else {
                hostListItemView = false;
            }
            if (hostListItemView) {
                String idOfConfirmedUser = currentItem.getmConfirmed();
                String request1 = currentItem.getMchavrutaRequest1();
                String request2 = currentItem.getMchavrutaRequest2();
                String request3 = currentItem.getMchavrutaRequest3();

                if (request1 != null && currentItem.getMchavrutaRequest1().length() > 5) {
                    holder.pendingRequest_1.setVisibility(View.VISIBLE);
                    if (idOfConfirmedUser.equals(request1)) {
                        holder.confirmRequest_1.setBackgroundColor(Color.parseColor("#10ef2e"));
                    }
                } else {
                    holder.pendingRequest_1.setVisibility(View.GONE);
                }
                if (request2 != null && request2.length() > 5) {
                    holder.pendingRequest_2.setVisibility(View.VISIBLE);
                    if (idOfConfirmedUser.equals(request2)) {
                        holder.confirmRequest_2.setBackgroundColor(Color.parseColor("#10ef2e"));
                    }
                } else {
                    holder.pendingRequest_1.setVisibility(View.GONE);
                }
                if (request3 != null && request3.length() > 5) {
                    holder.pendingRequest_3.setVisibility(View.VISIBLE);
                    if (idOfConfirmedUser.equals(request3)) {
                        holder.confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
                    }
                } else {
                    holder.pendingRequest_3.setVisibility(View.GONE);
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
//hostitem
//            //on confirming chavrutas 1, 2, and three
//            if (holder.confirmRequest_1 != null) {
//                holder.confirmRequest_1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Access the row position here to get the correct data item
//                        int position = getPosition(currentItem);
//                        currentItem = getItem(position);
//                        //sets confirmed
//                        if (!currentItem.requestOneConfirmed) {
//                            currentItem.setRequestOneConfirmed(true);
//                        } else {
//                            currentItem.setRequestOneConfirmed(false);
//                        }
//                        String chavrutaId = hostSessionItemData.getmChavrutaId();
//                        sendConfirmationtoDb(chavrutaId, currentItem.getMchavrutaRequest1());
//                        //holder.confirmRequest_1.setBackgroundColor(Color.parseColor("#10ef2e"));
//
//                    }
//                });
//            }
//            if (holder.confirmRequest_2 != null) {
//                holder.confirmRequest_2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String chavrutaId = currentItem.getmChavrutaId();
//                        sendConfirmationtoDb(chavrutaId, currentItem.getMchavrutaRequest2());
//                        //holder.confirmRequest_2.setBackgroundColor(Color.parseColor("#10ef2e"));
//                    }
//                });
//            }
//            if (holder.confirmRequest_3 != null) {
//                holder.confirmRequest_3.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String chavrutaId = currentItem.getmChavrutaId();
//                        sendConfirmationtoDb(chavrutaId, currentItem.getMchavrutaRequest3());
//                        //holder.confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
//                    }
//                });
//            }






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
//        final ViewHolder viewHolder;
//        View listItemView = convertView;
//        Boolean hostListItemView = true;
//        String userId = UserDetails.getmUserId();
//        final HostSessionData currentItem = getItem(position);
//        final String hostId = currentItem.getmHostId();
//
//
//
//        Context mainActivityContext = MainActivity.getMAContextForAdapter();
//
//

//
//            //look up view for data population
////            viewHolder.hostFirstName = (TextView) listItemView.findViewById(R.id.host_first_name);
////            viewHolder.sessionDate = (TextView) listItemView.findViewById(R.id.session_date);
////            viewHolder.startTime = (TextView) listItemView.findViewById(R.id.start_time);
////            viewHolder.endTime = (TextView) listItemView.findViewById(R.id.end_time);
////            viewHolder.sefer = (TextView) listItemView.findViewById(R.id.session_sefer);
////            viewHolder.location = (TextView) listItemView.findViewById(R.id.location);
////            viewHolder.hostInfo = (ImageButton) listItemView.findViewById(R.id.ib_add_match);
////            viewHolder.pendingRequest_1 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_1);
////            viewHolder.pendingRequest_2 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_2);
////            viewHolder.pendingRequest_3 = (LinearLayout) listItemView.findViewById(R.id.ll_requester_viewgroup_3);
////            viewHolder.confirmRequest_1 = (Button) listItemView.findViewById(R.id.b_confirm_request_1);
////            viewHolder.confirmRequest_2 = (Button) listItemView.findViewById(R.id.b_confirm_request_2);
////            viewHolder.confirmRequest_3 = (Button) listItemView.findViewById(R.id.b_confirm_request_3);
////            viewHolder.chavrutaConfirmed = (Button) listItemView.findViewById(R.id.b_chavruta_confirmed);
//
////            //sets confirm button green to show confirmed in hosting view list item
////            if(hostSessionDatas.requestOneConfirmed) {
////                viewHolder.confirmRequest_1.setBackgroundColor(Color.parseColor("#10ef2e"));
////            }
//
//
//
//            //cache the viewHoslder object inside the fresh view
//            listItemView.setTag(viewHolder);
//
//        } else {
//            //view is already been populated
//            hostListItemView = false;
//            if (hostId.equals(userId) && mContext == mainActivityContext) {
//                hostListItemView = true;
//            }
//            viewHolder = (ViewHolder) listItemView.getTag();
//            String nameOfSefer = currentItem.getmSefer();
//
//            if(hostListItemView) {
//                int visible = viewHolder.pendingRequest_1.getVisibility();
//                Log.i(LOG_TAG, "visibility: " + visible);
//                Log.i(LOG_TAG, "sefer: " + nameOfSefer);
//            }
//        }
//        final HostSessionData hostSessionDatas = getItem(position);

//check if chavruta has been requested by any user and show views with requests
//marks requester as green if confirmed in host view


//        if (hostListItemView) {
//
//            String idOfConfirmedUser = currentItem.getmConfirmed();
//            String request1NullCheck = currentItem.getMchavrutaRequest1();
//            String request2NullCheck = currentItem.getMchavrutaRequest2();
//            String request3NullCheck = currentItem.getMchavrutaRequest3();
//
//            if (request1NullCheck != null && currentItem.getMchavrutaRequest1().length() > 5) {
//                String chavrutaRequestIdOne = currentItem.getMchavrutaRequest1();
//                //todo: AAAAARRRRRGGGG!
////                if(viewHolder.pendingRequest_1 != null) {
//                viewHolder.pendingRequest_1.setVisibility(View.VISIBLE);
////                }
//                if( idOfConfirmedUser.equals(chavrutaRequestIdOne)){
//                    viewHolder.confirmRequest_1.setBackgroundColor(Color.parseColor("#10ef2e"));
//                }
//            }else{viewHolder.pendingRequest_1.setVisibility(View.GONE);}
//            if (request2NullCheck != null && currentItem.getMchavrutaRequest2().length() > 5) {
//                String chavrutaRequestIdTwo = currentItem.getMchavrutaRequest2();
//                viewHolder.pendingRequest_2.setVisibility(View.VISIBLE);
//                if(idOfConfirmedUser.equals(chavrutaRequestIdTwo)){
//                    viewHolder.confirmRequest_2.setBackgroundColor(Color.parseColor("#10ef2e"));
//                }
//            }else{viewHolder.pendingRequest_1.setVisibility(View.GONE);}
//            if (request3NullCheck != null && currentItem.getMchavrutaRequest3().length() > 5) {
//                String chavrutaRequestIdThree = currentItem.getMchavrutaRequest3();
//                viewHolder.pendingRequest_3.setVisibility(View.VISIBLE);
//                if(idOfConfirmedUser.equals(chavrutaRequestIdThree)){
//                    viewHolder.confirmRequest_3.setBackgroundColor(Color.parseColor("#10ef2e"));
//                }
//            }else{viewHolder.pendingRequest_1.setVisibility(View.GONE);}
//            notifyDataSetChanged();
//        }
//
//        viewHolder.hostFirstName.setText(currentItem.getmHostFirstName());
//        viewHolder.sessionDate.setText(currentItem.getmSessionDate());
//        viewHolder.startTime.setText(currentItem.getmStartTime());
//        viewHolder.endTime.setText(currentItem.getmEndTime());
//        viewHolder.sefer.setText(currentItem.getmSefer());
//        viewHolder.location.setText(currentItem.getmLocation());
////        if(viewHolder.confirmRequest_1 != null && confirmed) {
////            viewHolder.confirmRequest_1.setBackgroundColor(Color.parseColor("#13f717"));
////        }
//        //on confirming chavrutas 1, 2, and three
//        if(viewHolder.confirmRequest_1 != null) {
//            viewHolder.confirmRequest_1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Access the row position here to get the correct data item
//                    int position = getPosition(currentItem);
//                    hostSessionItemData = getItem(position);
//                    //sets confirmed
//                    if(!currentItem.requestOneConfirmed) {
//                        currentItem.setRequestOneConfirmed(true);
//                    }else{
//                        currentItem.setRequestOneConfirmed(false);
//                    }
//                    String chavrutaId = hostSessionItemData.getmChavrutaId();
//                    sendConfirmationtoDb(chavrutaId, currentItem.getMchavrutaRequest1());
//                    //viewHolder.confirmRequest_1.setBackgroundColor(Color.parseColor("#10ef2e"));
//
//                }
//            });
//        }
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



