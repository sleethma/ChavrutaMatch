package com.example.micha.chavrutamatch.MVPConstructs.Repos;

import com.example.micha.chavrutamatch.Data.Http.APIModels.ServerResponse;

/**
 * Created by micha on 3/2/2018.
 */

public interface MARepoContract {
    void setUsersFullName(String text);


    void setTemplateListItemAwaitingHostAvatar(String hostAvatarNumber);

    void setCustomListItemAwaitingHostAvatar(byte[] hostCustomAvatar);

    void setTemplateListItemHostAvatar();

    void setCustomListItemHostAvatar();

    void setupHostsRequestersViews(String requesterName, int requesterNumber);

    void setRequestButtonStatus(int requestNumber);

    void setHostRequestAvatar(byte[] customRequesterAvatar, String requester1AvatarNumber, int requesterNumber);

    void setOnClickListenerOnRequester(MARepoContract holder, final ServerResponse chavruta, int requesterNumber);

    void setButtonToConfirmedState(String confirmButtonAction);

    void setDisplayIfRequesters(boolean hasRequesters);

    void setUserConfirmedStatus(boolean userIsConfirmed);

    void setViewItemDataInMyChavruta(ServerResponse chavruta, int position);

}
