
package com.example.micha.chavrutamatch.Data.Http.APIModels;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyChavrutas {

    @SerializedName("server_response")
    @Expose
    private List<ServerResponse> serverResponse = new ArrayList<>();

    public List<ServerResponse> getMyChavrutasAL() {
        return serverResponse;
    }

    public void setServerResponse(List<ServerResponse> serverResponse) {
        this.serverResponse = serverResponse;
    }

}
