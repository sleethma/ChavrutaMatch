package com.example.micha.chavrutamatch.Data;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micha.chavrutamatch.HostSelect;
import com.example.micha.chavrutamatch.MainActivity;

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

/**
 * Created by micha on 7/29/2017.
 */

public class ServerConnect extends AsyncTask<String, Void, String> {
    Context mContextRegister;

    public static String jsonString;

    //creates textview constructor for thread access
    //TODO create object to hold db info for activity delivery
    TextView jsonTextView = null;


    //postExecuteResponse: 0= no click/error; 1=registration; 2=get JSON
    int postExecuteResponse = 0;


    public ServerConnect(Context context, TextView textView) {
        this.mContextRegister = context;
        if (textView != null) {
            this.jsonTextView = textView;
        }
    }

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
        String json_url = "http://brightlightproductions.online/get_chavrutaJSON.php";
        String new_user_url = "http://brightlightproductions.online/chavruta_user_profiles_add.php";


        //checks which button was clicked
        String chosenBkgdTaskCheck = params[0];
        //assures register button sent this background call
        if (chosenBkgdTaskCheck.equals("new host")) {
            //get params
            String hostFirstName = params[1];
            String hostLastName = params[2];
            String sessionMessage = params[3];
            String sessionDate = params[4];
            String startTime = params[5];
            String endTime = params[6];
            String sefer = params[7];
            String location = params[8];
            String hostId = params[9];


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
                                URLEncoder.encode("host_id", "UTF-8") + "=" + URLEncoder.encode(
                                hostId, "UTF-8");

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
        if (chosenBkgdTaskCheck.equals("getJSONKey")) {
            try {
                URL jsonURL = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) jsonURL.openConnection();
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

        if (chosenBkgdTaskCheck.equals("new user post")) {
            //get params
            String userId = params[1];
            String userName = params[2];
            String userAvatarNumber = params[3];
            String userFirstName = params[4];
            String userLastName = params[5];
            String userPhoneNumber = params[6];
            String userEmail = params[7];
            String userBio = params[8];

            //establish connection
            try {
                URL newUserUrl = new URL(new_user_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) newUserUrl.openConnection();
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
        return null;
    }


    @Override
    protected void onPostExecute(String result){
        super.
                onPostExecute(result);

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
                //return jsonString to HostSelect.class
                //TODO: IS ELSE NECESSARY?
                if (jsonString != null) {
                    Intent intent = new Intent(this.mContextRegister, HostSelect.class);
                    intent.putExtra("jsonKey", jsonString);
                    mContextRegister.startActivity(intent);

                } else {
                    Intent intent = new Intent(this.mContextRegister, HostSelect.class);
                    intent.putExtra("jsonKey", jsonString);
                    mContextRegister.startActivity(intent);
                    break;
                }
                break;
            case 3:
                //new user data input successful
                Toast.makeText(mContextRegister, result, Toast.LENGTH_LONG).show();
                break;
        }
        postExecuteResponse = 0;
    }
}
