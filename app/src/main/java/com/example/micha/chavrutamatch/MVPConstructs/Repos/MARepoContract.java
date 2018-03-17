package com.example.micha.chavrutamatch.MVPConstructs.Repos;

import com.example.micha.chavrutamatch.Data.HostSessionData;

/**
 * Created by micha on 3/2/2018.
 */

public interface MARepoContract {
void setHostFullName(String text);
    int getItemViewTypeIF(HostSessionData hsdRepo);
    void setListItemHostAvatar();
   void setCustomListItemHostAvatar();void setupHostsRequestersViews(String requesterName, int requesterNumber);
    void setRequestButtonStatus(int requestNumber);
    void setHostRequestAvatar(byte[] customRequesterAvatar, String requester1AvatarNumber, int requesterNumber);
    void setOnClickListenerOnRequester(MARepoContract holder, final HostSessionData repoHSD, int requesterNumber);
    void setButtonToConfirmedState(String confirmButtonAction);
    void setDisplayIfRequesters(boolean hasRequesters);
    void setUserConfirmedStatus(boolean userIsConfirmed);
    void setViewItemData(HostSessionData repoHSD, int position);
}
