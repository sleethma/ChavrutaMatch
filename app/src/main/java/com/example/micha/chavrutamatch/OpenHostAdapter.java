package com.example.micha.chavrutamatch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.HostSessionData;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.Utils.ChavrutaUtils;
import com.example.micha.chavrutamatch.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micha on 1/11/2018.
 */
class OpenHostAdapter extends RecyclerView.Adapter<OpenHostAdapter.ViewHolder> {

    //number of views adapter will hold
    private Context mContext;
    private String userId;
    //@var used to control swipe on delete Dialogue selection
    final private String USER_IMG_AVATAR_STRING = "999";

    private ArrayList<HostSessionData> mChavrutaSessionsAL;
    private List<Integer> avatarList = AvatarImgs.getAllAvatars();

    private ListItemClickListener mOnClickListener;


    public interface ListItemClickListener {
        void onListItemClick(int clickedIndex, View v);
    }

    public OpenHostAdapter(Context context,
                           ArrayList<HostSessionData> chavrutaSessionsArrayList,
                           ListItemClickListener listItemClickListener, UserDetails userDetailsInstance) {
        this.mContext = context;
        mChavrutaSessionsAL = chavrutaSessionsArrayList;
        mOnClickListener = listItemClickListener;
        userId = userDetailsInstance.getmUserId();

        //calls so can access static user vars throughout adapter
//        orderArrayByDate(mChavrutaSessionsAL);
    }

    @Override
    public void onBindViewHolder(OpenHostAdapter.ViewHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.open_host_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mChavrutaSessionsAL.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hostFullName, sessionDate, startTime, endTime, sefer, location, hostUserName, sessionMessage;
        ImageButton addHost;
        ImageView hostAvatar;

        public ViewHolder(View listItemView) {
            super(listItemView);
            hostFullName = listItemView.findViewById(R.id.host_full_name);
            sessionDate =  listItemView.findViewById(R.id.session_date);
            startTime = listItemView.findViewById(R.id.start_time);
            endTime =  listItemView.findViewById(R.id.end_time);
            sefer = listItemView.findViewById(R.id.session_sefer);
            location = listItemView.findViewById(R.id.location);
            addHost =  listItemView.findViewById(R.id.ib_add_match);
            hostUserName = listItemView.findViewById(R.id.host_user_name);
            hostAvatar = listItemView.findViewById(R.id.iv_host_avatar);
            sessionMessage = listItemView.findViewById(R.id.cardBack);
            listItemView.setOnClickListener(this);
        }

        void bind(OpenHostAdapter.ViewHolder holder, int listIndex) {
            final int position = listIndex;
            //@requestSlotOpen number of next class slot availiable for requester
            //@requesterAvatar & @requesterName are vars that hold chavrutaInformation for display in hostview
            final String requestSlotOpen;
            final String requesterAvatar;
            final String requesterName;
            final String requesterAvatarColumn;
            final String requesterNameColumn;
            final HostSessionData currentItem = mChavrutaSessionsAL.get(position);

            //gets users current avatar
            requesterAvatar = getRequesterAvatar();
            String userFirstName = UserDetails.getmUserFirstName();
            String userLastName = UserDetails.getmUserLastName();
            requesterName = ChavrutaUtils.createUserFirstLastName(
                    userFirstName, userLastName);

            //check which request slot is availiable and pass name of db column to server for insert
            if (currentItem.getMchavrutaRequest1Id().length() < 5) {
                requestSlotOpen = "chavruta_request_1";
                requesterAvatarColumn = "chavruta_request_1_avatar";
                requesterNameColumn = "chavruta_request_1_name";
                holder.addHost.setImageResource(R.drawable.ib_add_match);
            } else if (currentItem.getMchavrutaRequest2Id().length() < 5) {
                requestSlotOpen = "chavruta_request_2";
                requesterAvatarColumn = "chavruta_request_2_avatar";
                requesterNameColumn = "chavruta_request_2_name";
                holder.addHost.setImageResource(R.drawable.ib_add_match);
            } else if (currentItem.getMchavrutaRequest3Id().length() < 5) {
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

            //sets chavrutahosts avatar
            String currentHostAvatarNumberString = currentItem.getmHostAvatarNumber();

            //sets user first last name after concatonation
            holder.hostUserName.setText(ChavrutaUtils.createUserFirstLastName(
                    currentItem.getmHostFirstName(), currentItem.getmHostLastName()));
            if (currentHostAvatarNumberString != null &&
                    currentHostAvatarNumberString.length() > AvatarImgs.avatarImgList.size()) {
                byte[] customUserAvatar = currentItem.getByteArrayFromString(currentHostAvatarNumberString);
                GlideApp
                        .with(mContext)
                        .asBitmap()
                        .load(customUserAvatar)
                        .placeholder(R.drawable.ic_unknown_user)
                        .circleCrop()
                        .into(holder.hostAvatar);
            } else if (!currentHostAvatarNumberString.equals("999")) {
                holder.hostAvatar.setImageResource(avatarList.get(Integer.parseInt(currentItem.getmHostAvatarNumber())));
            }
            holder.sessionDate.setText(currentItem.getmSessionDate());
            holder.startTime.setText(currentItem.getmStartTime());
            holder.endTime.setText(currentItem.getmEndTime());
            holder.sefer.setText(currentItem.getmSefer());
            holder.location.setText(currentItem.getmLocation());
            String seferText = currentItem.getmSefer();
            if(seferText.length() > 30){
                seferText = seferText.substring(0, 27) + "...";
            }
            holder.sefer.setText(seferText);
            holder.sessionMessage.setText(currentItem.getmSessionMessage());

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

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition, v);
        }
    }

    public void add(HostSessionData dataAddedFromJson) {
        mChavrutaSessionsAL.add(dataAddedFromJson);
    }

    public String getRequesterAvatar() {
        String requesterAvatar;
        if (UserDetails.getUserAvatarBase64String() != null &&
                UserDetails.getmUserAvatarNumberString().equals(USER_IMG_AVATAR_STRING)) {
            requesterAvatar = UserDetails.getUserAvatarBase64String();
        } else {
            requesterAvatar = UserDetails.getmUserAvatarNumberString();
        }
        return requesterAvatar;
    }


}




