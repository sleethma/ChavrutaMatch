package com.example.micha.chavrutamatch.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.AddBio;
import com.example.micha.chavrutamatch.ChavrutaMatch;
import com.example.micha.chavrutamatch.DI.Components.MAComponent;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.HostSelect;
import com.example.micha.chavrutamatch.MVPConstructs.MAContractMVP;
import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.R;
import com.example.micha.chavrutamatch.Utils.ConnCheckUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.inject.Inject;

/**
 * Created by micha on 7/29/2017.
 */

public class ServerConnect extends AsyncTask<String, Void, String> {
    Context mContextRegister;
    public MAContractMVP.Presenter callback;
    private Boolean myChavruta;
    public static String jsonString;

//    @Inject
    public Context context;

//    @Inject
    public UserDetails userDetailsInstance;
    //postExecuteResponse: 0= no click/error; 1=registration; 2=get JSON
    int postExecuteResponse = 0;

    @Inject
    public ServerConnect(Context context, UserDetails userDetailsInstance) {
        this.mContextRegister = context;
        this.userDetailsInstance = userDetailsInstance;
        if(mContextRegister == null){
            mContextRegister = MainActivity.mContext;
        }
    }

    private ProgressDialog pDialog;

    /**
     * Before starting background thread Show Progress Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        boolean isConnectedToNetwork = ConnCheckUtil.isConnected(mContextRegister);
        pDialog = new ProgressDialog(mContextRegister);
        pDialog.setMessage("Loading Matches. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        if (isConnectedToNetwork) {
            pDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        //checks which ServerConnect instance was sent
        String chosenBkgdTaskCheck = params[0];
//todo: change all HttpURLConnection(s) --> HttpsUrlConnection(s)
        //retrieves user details from db if user exists
        if (chosenBkgdTaskCheck.equals("get UserDetails")) {
            try {
                HttpURLConnection httpURLConnection;
                String userID = userDetailsInstance.getmUserId();
                String getUserDetailsUrlString = "http://brightlightproductions.online/secure_get_json_user_details.php?user_id=" +
                        userID;
                URL getUserDetailsUrl = new URL(getUserDetailsUrlString);
                httpURLConnection = (HttpURLConnection) getUserDetailsUrl.openConnection();

                httpURLConnection.setRequestMethod("GET");
                //create inputstream
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((jsonString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jsonString + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                postExecuteResponse = 6;

                jsonString = stringBuilder.toString().trim();

                return jsonString;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //assures register button sent this background call
        if (chosenBkgdTaskCheck.equals("new host")) {
            String reg_url = "http://brightlightproductions.online/secure_chavruta_session_add.php";
            //get params
            String hostFirstName = params[1];
            String hostLastName = params[2];
            String hostAvatarNumber = params[3];
            String sessionMessage = params[4];
            String sessionDate = params[5];
            String startTime = params[6];
            String endTime = params[7];
            String sefer = params[8];
            String location = params[9];
            String hostCityState = params[10];
            String hostId = params[11];
            String chavrutaRequest1 = params[12];
            String chavrutaRequest2 = params[13];
            String chavrutaRequest3 = params[14];
            String confirmed = params[15];

            //establish connection
            try {
                URL regUrl = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) regUrl.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //make object of output stream
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("host_first_name", "UTF-8") + "=" + URLEncoder.encode(
                                hostFirstName, "UTF-8") + "&" +
                                URLEncoder.encode("host_last_name", "UTF-8") + "=" + URLEncoder.encode(
                                hostLastName, "UTF-8") + "&" +
                                URLEncoder.encode("host_avatar_number", "UTF-8") + "=" + URLEncoder.encode(
                                hostAvatarNumber, "UTF-8") + "&" +
                                URLEncoder.encode("session_message", "UTF-8") + "=" + URLEncoder.encode(
                                sessionMessage, "UTF-8") + "&" +
                                URLEncoder.encode("session_date", "UTF-8") + "=" + URLEncoder.encode(
                                sessionDate, "UTF-8") + "&" +
                                URLEncoder.encode("start_time", "UTF-8") + "=" + URLEncoder.encode(
                                startTime, "UTF-8") + "&" +
                                URLEncoder.encode("end_time", "UTF-8") + "=" + URLEncoder.encode(
                                endTime, "UTF-8") + "&" +
                                URLEncoder.encode("sefer", "UTF-8") + "=" + URLEncoder.encode(
                                sefer, "UTF-8") + "&" +
                                URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(
                                location, "UTF-8") + "&" +
                                URLEncoder.encode("host_city_state", "UTF-8") + "=" + URLEncoder.encode(
                                hostCityState, "UTF-8") + "&" +
                                URLEncoder.encode("host_id", "UTF-8") + "=" + URLEncoder.encode(
                                hostId, "UTF-8") + "&" +
                                URLEncoder.encode("chavruta_request_1", "UTF-8") + "=" + URLEncoder.encode(
                                chavrutaRequest1, "UTF-8") + "&" +
                                URLEncoder.encode("chavruta_request_2", "UTF-8") + "=" + URLEncoder.encode(
                                chavrutaRequest2, "UTF-8") + "&" +
                                URLEncoder.encode("chavruta_request_3", "UTF-8") + "=" + URLEncoder.encode(
                                chavrutaRequest3, "UTF-8") + "&" +
                                URLEncoder.encode("not_confirmed", "UTF-8") + "=" + URLEncoder.encode(
                                confirmed, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                postExecuteResponse = 1;
                return "Chavruta Registered!";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //determines which data set is needed to populate open host list or MainActivity my chavruta list
        if (chosenBkgdTaskCheck.equals("getJSONKey") || chosenBkgdTaskCheck.equals("my chavrutas")) {

            try {
                HttpURLConnection httpURLConnection;
                if (chosenBkgdTaskCheck.equals("my chavrutas")) {
                    //designates caller from MA
                    myChavruta = true;
                    String userID = userDetailsInstance.getmUserId();


                    String my_chavrutas_url = "http://brightlightproductions.online/secure_get_json_my_chavrutas.php?user_id=" +
                            userID;
                    URL jsonMyChavrutasURL = new URL(my_chavrutas_url);
                    httpURLConnection = (HttpURLConnection) jsonMyChavrutasURL.openConnection();
                } else {
                    String hostCityState = userDetailsInstance.getUserCallFormattedCityState();
                    String json_url = "http://brightlightproductions.online/secure_get_chavrutaJSON.php?host_city_state=" + hostCityState;
                    URL jsonURL = new URL(json_url);
                    httpURLConnection = (HttpURLConnection) jsonURL.openConnection();
                    myChavruta = false;

                }

                httpURLConnection.setRequestMethod("GET");
                //create inputstream
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((jsonString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jsonString + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                postExecuteResponse = 2 ;
                jsonString = stringBuilder.toString().trim();
                return jsonString;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //stores new user in user profile db
        if (chosenBkgdTaskCheck.equals("user post")) {

            //get params
            String userId = params[1];
            String userName = params[2];
            String userAvatarNumber = params[3];
            String userFirstName = params[4];
            String userLastName = params[5];
            String userPhoneNumber = params[6];
            String userEmail = params[7];
            String userBio = params[8];
            String userCityState = params[9];
            //@var postType: either "update user post" or "new user post"
            String postType = params[10];
            //string of user avatar byte[]
            String customAvatarString = "";

            //establish connection
            try {
                URL userPostUrl;
                if (postType.equals("new user post")) {
                    String new_user_url = "http://brightlightproductions.online/secure_chavruta_user_profiles_add.php";
                    userPostUrl = new URL(new_user_url);
                } else {
                    String update_user_url = "http://brightlightproductions.online/secure_chavruta_user_profiles_update.php";
                    userPostUrl = new URL(update_user_url);
                    customAvatarString = params[11];
                }
                HttpURLConnection httpURLConnection = (HttpURLConnection) userPostUrl.openConnection();


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //make object of output stream
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(
                                userId, "UTF-8") + "&" +
                                URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(
                                userName, "UTF-8") + "&" +
                                URLEncoder.encode("user_avatar_number", "UTF-8") + "=" + URLEncoder.encode(
                                userAvatarNumber, "UTF-8") + "&" +
                                URLEncoder.encode("user_first_name", "UTF-8") + "=" + URLEncoder.encode(
                                userFirstName, "UTF-8") + "&" +
                                URLEncoder.encode("user_last_name", "UTF-8") + "=" + URLEncoder.encode(
                                userLastName, "UTF-8") + "&" +
                                URLEncoder.encode("user_phone_number", "UTF-8") + "=" + URLEncoder.encode(
                                userPhoneNumber, "UTF-8") + "&" +
                                URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(
                                userEmail, "UTF-8") + "&" +
                                URLEncoder.encode("user_city_state", "UTF-8") + "=" + URLEncoder.encode(
                                userCityState, "UTF-8") + "&" +
                                URLEncoder.encode("user_bio", "UTF-8") + "=" + URLEncoder.encode(
                                userBio, "UTF-8") + "&" +
                                URLEncoder.encode("custom_avatar_string", "UTF-8") + "=" + URLEncoder.encode(
                                customAvatarString, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                postExecuteResponse = 3;
                return mContextRegister.getString(R.string.new_user_registered);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //stores in db class request by user in hosts chavruta
        //requesterSlotOpen is the first open availiable slot open in db out of 3
        //chavrutaId is the auto_inc column of db
        //requesterAvatar is the integer key to the bitmap user selected avatar chosen
        if (chosenBkgdTaskCheck.equals("chavruta request")) {
            String chavruta_request_update_url =
                    "http://brightlightproductions.online/secure_initial_chavruta_request_update.php";
            //get params
            String userId = params[1];
            String chavrutaId = params[2];
            String requestSlotOpen = params[3];
            String requesterAvatarColumn = params[4];
            String requesterAvatar = params[5];
            String requesterNameColumn = params[6];
            String requesterName = params[7];


            //establish connection
            try {
                URL chavrutaRequestUrl = new URL(chavruta_request_update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) chavrutaRequestUrl.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //make object of output stream
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(
                                userId, "UTF-8") + "&" +
                                URLEncoder.encode("open_request_slot", "UTF-8") + "=" + URLEncoder.encode(
                                requestSlotOpen, "UTF-8") + "&" +
                                URLEncoder.encode("requester_avatar_column", "UTF-8") + "=" + URLEncoder.encode(
                                requesterAvatarColumn, "UTF-8") + "&" +
                                URLEncoder.encode("requester_name_column", "UTF-8") + "=" + URLEncoder.encode(
                                requesterNameColumn, "UTF-8") + "&" +
                                URLEncoder.encode("requester_avatar", "UTF-8") + "=" + URLEncoder.encode(
                                requesterAvatar, "UTF-8") + "&" +
                                URLEncoder.encode("requester_name", "UTF-8") + "=" + URLEncoder.encode(
                                requesterName, "UTF-8") + "&" +
                                URLEncoder.encode("chavruta_id", "UTF-8") + "=" + URLEncoder.encode(
                                chavrutaId, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                postExecuteResponse = 4;
                return "Request Sent to Db";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //stores selected requesters chavruta by host in db
        if (chosenBkgdTaskCheck.equals("confirmChavrutaRequest")) {
            String chavrutaId = params[1];
            String requesterId = params[2];
            //send chavruta request to host
            //establish connection
            try {
                URL confirmChavrutaRequest = new URL("http://brightlightproductions.online/" +
                        "secure_confirm_chavruta_request.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) confirmChavrutaRequest.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //make object of output stream
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


                String data =

                        URLEncoder.encode("chavruta_id", "UTF-8") + "=" + URLEncoder.encode(
                                chavrutaId, "UTF-8") + "&" +
                                URLEncoder.encode("requester_id", "UTF-8") + "=" + URLEncoder.encode(
                                requesterId, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                postExecuteResponse = 5;
                return "Chavruta status changed in DB";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //delete chavruta from db on swipe in MA
        if (chosenBkgdTaskCheck.equals("delete chavruta")) {
            String chavrutaId = params[1];
            //establish connection
            try {
                URL unregChavruta = new URL("http://brightlightproductions.online/" +
                        "secure_delete_chavruta.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) unregChavruta.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //make object of output stream
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("chavruta_id", "UTF-8") + "=" + URLEncoder.encode(
                                chavrutaId, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                postExecuteResponse = 7;
                pDialog.dismiss();
                return "chavruta ID: " + chavrutaId + "deleted!";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        switch (postExecuteResponse) {
            case 0:
                //incorrect key sent to ServerConnect.class or internet connection false
//                String checkCon = "Please Check Internet Connection";
//                Toast.makeText(mContextRegister, checkCon,
//                        Toast.LENGTH_LONG).show();
//                alertUserToCheckConn();
                break;
            case 1:
                //register new host successful
                String newClassReg = mContextRegister.getString(R.string.new_class_reg);
                Toast.makeText(mContextRegister, newClassReg, Toast.LENGTH_LONG).show();
                break;
            case 2:
                //return jsonString to HostSelect.class if it is caller, else return it to MA
                if (myChavruta) {
                    ChavrutaMatch.setMyChavrutaJsonString(jsonString);
                    callback.returnAsyncResult(result);

                } else {
                    ChavrutaMatch.setOpenHostsJsonString(jsonString);
                    Intent intent = new Intent(this.mContextRegister, HostSelect.class);
                    intent.putExtra("jsonKey", "json set");
                    mContextRegister.startActivity(intent);
                }
                pDialog.dismiss();
                break;
            case 3:
                //new user data input successful
                pDialog.dismiss();
                break;
            case 4:
                //user request sent to host
                String requestSent = mContextRegister.getString(R.string.requestSent);
                Toast.makeText(mContextRegister, requestSent, Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                break;
            case 5:
                //host confirmed user's request
                String confirmedRequest = mContextRegister.getString(R.string.confirmed_request);
                Toast.makeText(mContextRegister, confirmedRequest, Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                break;
            case 6:
                Intent intent = new Intent(this.mContextRegister, AddBio.class);
                intent.putExtra("user_data_json_string", jsonString);
                mContextRegister.startActivity(intent);
                pDialog.dismiss();
                break;
            case 7:
                //chavruta id deleted
                String classUnreg = mContextRegister.getString(R.string.class_unreg);
                Toast.makeText(mContextRegister, classUnreg, Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                break;
        }
        postExecuteResponse = 0;
    }
}
