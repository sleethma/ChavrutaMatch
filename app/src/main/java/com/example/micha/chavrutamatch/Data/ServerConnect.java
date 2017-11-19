package com.example.micha.chavrutamatch.Data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.AddBio;
import com.example.micha.chavrutamatch.AddSelect;
import com.example.micha.chavrutamatch.HostSelect;
import com.example.micha.chavrutamatch.MainActivity;
import com.example.micha.chavrutamatch.R;
import com.facebook.share.Share;

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
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by micha on 7/29/2017.
 */

public class ServerConnect extends AsyncTask<String, Void, String> {
    Context mContextRegister;
    Boolean myChavruta;

    public static String jsonString;

    //creates textview constructor for thread access
    //TODO create object to hold db info for activity delivery
    TextView jsonTextView = null;


    //postExecuteResponse: 0= no click/error; 1=registration; 2=get JSON
    int postExecuteResponse = 0;

    public ServerConnect(Context context) {
        this.mContextRegister = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        //stores path of db php registration script on host: server_IPv4/db_folder/file
        String reg_url = "http://brightlightproductions.online/chavruta_session_add.php";
        String new_user_url = "http://brightlightproductions.online/chavruta_user_profiles_add.php";
        String update_user_url = "http://brightlightproductions.online/chavruta_user_profiles_update.php";

        String chavruta_request_update_url =
                "http://brightlightproductions.online/initial_chavruta_request_update.php";
        //@ my_chavrutas_url is changed in line to send individual user id
        String my_chavrutas_url = "http://brightlightproductions.online/get_json_my_chavrutas.php";

        //checks which ServerConnect instance was sent
        String chosenBkgdTaskCheck = params[0];

        //retrieves user details from db if user exists
        if (chosenBkgdTaskCheck.equals("get UserDetails")){
            try {
                HttpURLConnection httpURLConnection;
                    String userID = UserDetails.getmUserId();
                    String getUserDetailsUrlString = "http://brightlightproductions.online/get_json_user_details.php?user_id=" +
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
                    String userID = UserDetails.getmUserId();


                    my_chavrutas_url = "http://brightlightproductions.online/get_json_my_chavrutas.php?user_id=" +
                            userID;
                    URL jsonMyChavrutasURL = new URL(my_chavrutas_url);
                    httpURLConnection = (HttpURLConnection) jsonMyChavrutasURL.openConnection();
                } else {
                    String hostCityState = UserDetails.getUserCityState();
                    String json_url = "http://brightlightproductions.online/" +
                            "get_chavrutaJSON.php?host_city_state=" + hostCityState;
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
                postExecuteResponse = 2;

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

            //establish connection
            try {
                URL userPostUrl;
                if(postType.equals("new user post")){
                    userPostUrl = new URL(new_user_url);
                }else{
                    userPostUrl = new URL(update_user_url);
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
                                userBio, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                postExecuteResponse = 3;
                return "New User Registered!";
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
                        "confirm_chavruta_request.php");
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
                return "chavruta Confirmed in Db";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //delete chavruta from db on swipe in MA
        if(chosenBkgdTaskCheck.equals("delete chavruta")){
            String chavrutaId = params[1];
            //establish connection
            try {
                URL confirmChavrutaRequest = new URL("http://brightlightproductions.online/" +
                        "delete_chavruta.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) confirmChavrutaRequest.openConnection();
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
                //incorrect key sent to ServerConnect.class
                Toast.makeText(mContextRegister, "no matched action in " + getClass().getSimpleName()
                        + " class", Toast.LENGTH_LONG).show();
                break;
            case 1:
                //register new host successful
                Toast.makeText(mContextRegister, result, Toast.LENGTH_LONG).show();
                break;
            case 2:
                //return jsonString to HostSelect.class if it is caller, else return it to MA
                //TODO: CHECK IF THE ELSE NECESSARY?
                if (myChavruta) {
                    Intent intent = new Intent(this.mContextRegister, MainActivity.class);
                    intent.putExtra("myChavrutaKey", jsonString);
                    mContextRegister.startActivity(intent);

                } else {
                    Intent intent = new Intent(this.mContextRegister, HostSelect.class);
                    intent.putExtra("jsonKey", jsonString);
                    mContextRegister.startActivity(intent);
                }
                break;
            case 3:
                //new user data input successful
                Toast.makeText(mContextRegister, result, Toast.LENGTH_LONG).show();
                break;
            case 4:
                //user request sent to host
                Toast.makeText(mContextRegister, result, Toast.LENGTH_LONG).show();
                break;
            case 5:
                //user request sent to host
                Toast.makeText(mContextRegister, result, Toast.LENGTH_LONG).show();
                break;
            case 6:
                Intent intent = new Intent(this.mContextRegister, AddBio.class);
                intent.putExtra("user_data_json_string", jsonString);
                mContextRegister.startActivity(intent);
                break;
            case 7:
                //chavruta id deleted
                Toast.makeText(mContextRegister, result, Toast.LENGTH_LONG).show();
                break;
        }
        postExecuteResponse = 0;
    }
}
