package com.example.micha.chavrutamatch;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.DI.Components.MAComponent;
import com.example.micha.chavrutamatch.DI.Modules.MAModule;
import com.example.micha.chavrutamatch.Data.AvatarImgs;
import com.example.micha.chavrutamatch.Data.ServerConnect;
import com.example.micha.chavrutamatch.Utils.ChavrutaTextValidation;
import com.example.micha.chavrutamatch.Utils.ChavrutaUtils;
import com.example.micha.chavrutamatch.Utils.GlideApp;
import com.example.micha.chavrutamatch.Utils.TimeStampConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by micha on 7/10/2017.
 */

public class NewHost extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tp_start_time)
    TimePicker tpStartTime;
    @BindView(R.id.tv_start_time)
    TextView tv_StartTime;
    @BindView(R.id.b_confirm_time_set)
    ImageButton bTimeSet;
    @BindView(R.id.ib_host_start_time)
    ImageButton ibStartTime;
    @BindView(R.id.et_host_topic)
    EditText etHostTopic;
    @BindView(R.id.til_chavruta_sefer)
    TextInputLayout tilTopic;
    @BindView(R.id.iv_host_avatar)
    ImageView ivHostAvatar;
    @BindView(R.id.host_address)
    EditText etHostAddress;
    @BindView(R.id.ib_host_end_time)
    ImageButton ibHostEndTime;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.ib_date)
    ImageButton ibDate;
    @BindView(R.id.tv_Date)
    TextView tvDate;
    @BindView(R.id.tv_host_message_label)
    TextView tvHostMessageLabel;
    @BindView(R.id.et_host_class_message)
    EditText tvHostClassMessage;
    @BindView(R.id.host_it)
    ImageButton ibHostIt;
    @BindView(R.id.ac_city_state)
    AutoCompleteTextView acCityState;
    @BindView(R.id.til_city_state)
    TextInputLayout tilCityState;
    @BindView(R.id.til_chavruta_address)
    TextInputLayout tilChavrutaAddress;
    @BindView(R.id.v_no_elevation_underline)
    View underlineToolbar;

//todo:delete all commented out flhostpic & tvaddhost references

    //host strings to db
    private String mHostFirstName, mHostLastName, mHostAvatarNumber, mSessionMessage, mSessionDate,
            mStartTime, mEndTime, mSefer, mLocation, mHostId, mHostCityState;
    private List<Integer> allAvatars;
    boolean avatarIsUserImage = false;
    private String format;
    private View.OnClickListener listener;
    private View mTimeDateViewClicked;
    private int hour;
    private int min;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    public static final int DIALOG_ID = 999;
    private boolean isBelowSDK24 = Build.VERSION.SDK_INT < 24;
    SharedPreferences sp;
    String NO_DATE = "Date?";
    private Context context;

    @Inject
    public UserDetails userDetailsInstance;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_host_entry);
        ButterKnife.bind(this);

        context = this;
        (ChavrutaMatch.get(this).getApplicationComponent()).inject(this);

        //autopopulate location from SP
        sp = getSharedPreferences(getString(R.string.user_data_file), MODE_PRIVATE);
        if (sp.getString(getString(R.string.user_city_state_key), null) != null) {
            acCityState.setText(sp.getString(getString(R.string.user_city_state_key), null));
        }
        if (userDetailsInstance.getmUserAvatarNumberString() != null &&
                !userDetailsInstance.getmUserAvatarNumberString().equals("999")) {
            ivHostAvatar.setImageResource(AvatarImgs.getAvatarNumberResId(
                    Integer.parseInt(userDetailsInstance.getmUserAvatarNumberString())));
        } else {
            try {
                GlideApp
                        .with(this)
                        .load(userDetailsInstance.getHostAvatarUri())
                        .placeholder(R.drawable.ic_unknown_user)
                        .circleCrop()
                        .into(ivHostAvatar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ibStartTime.setOnClickListener(this);
        ibHostEndTime.setOnClickListener(this);
        bTimeSet.setOnClickListener(this);
        ibDate.setOnClickListener(this);
        ibHostIt.setOnClickListener(this);
        setCurrentDateVars();
        mHostFirstName = userDetailsInstance.getmUserFirstName();
        mHostLastName = userDetailsInstance.getmUserLastName();

        //auto moves edittext when softkeyboard called
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //set auto-complete for closest US city
        ChavrutaUtils cu = new ChavrutaUtils();
        String jsonFileString = cu.getJsonFileFromResource(this);
        List<String> cities = cu.parseCityName(jsonFileString);

// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        acCityState.setAdapter(adapter);

        //remove toolbar underline if elevation supported
        if(isBelowSDK24)underlineToolbar.setVisibility(View.VISIBLE);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay);
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    String currentDateString = new SimpleDateFormat(
                            "yyyy-MM-dd").format(new Date());

                    String setTimeString = year + "-" + (month + 1) + "-" + dayOfMonth;

                    //determine a class date is in future
                    boolean classDatePassed = TimeStampConverter.classIsPassed(
                            currentDateString, setTimeString);
                    if (classDatePassed) {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                "Please select a future date.",
                                Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        // set selected date into textview
                        mMonth = month;
                        mYear = year;
                        mDay = dayOfMonth;
                        tvDate.setText(new StringBuilder()
                                .append(mMonth + 1).append("-").append(mDay).append("-")
                                .append(mYear));
                        setProfileView();
                    }
                }
            };

    public void onClick(View v) {
        //dismiss soft keyboard on button click
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // show clock if time related view clicked, DatePicker if not
        if (v == ibStartTime || v == ibHostEndTime) {
            setTimeView();
            tilCityState.setVisibility(View.GONE);
            tilChavrutaAddress.setVisibility(View.GONE);

            // Check if no view has focus:
            View focusView = this.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        } else if (v == ibDate) {
            View focusView = this.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                showDialog(DIALOG_ID);
            }

        } else if (v == bTimeSet) {
            confirmTime(v);
            setProfileView();
        } else if (v == ibHostIt) {
            if (tvDate.getText().equals(NO_DATE)) {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Please select a date.",
                        Snackbar.LENGTH_LONG)
                        .show();
            } else if (etHostTopic.getText().length() <= 3) {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Sefer/Topic must be greater than 3 letters.",
                        Snackbar.LENGTH_LONG)
                        .show();
            } else {
                //when confirm host button click, set values
                setProfileView();
                //animate button
                animateHostIt(v);
                postNewHostSession();
            }
        } else {
            Log.e(NewHost.class.getSimpleName(), "View " + v + " has no match onClick");
        }
        //save original view clicked to determine which to display when time confirmed button clicked
        mTimeDateViewClicked = v;
    }


    //if confirm button clicked set global date/time
    public void confirmTime(View view) {
        String timeString;
        if (mTimeDateViewClicked == ibHostEndTime || mTimeDateViewClicked == ibStartTime) {
            if (isBelowSDK24) {
                hour = tpStartTime.getCurrentHour();
                min = tpStartTime.getCurrentMinute();
            } else {
                hour = tpStartTime.getHour();
                min = tpStartTime.getMinute();
            }
            //if (min == 0)
            if (hour == 0) {
                hour += 12;
                format = "AM";
            } else if (hour == 12) {
                format = "PM";
            } else if (hour > 12) {
                hour -= 12;
                format = "PM";
            } else {
                format = "AM";
            }
            //}
            //if min = 0 add 00 to min
            if (min == 0) {
                timeString = hour + ":" + "00 " + format;
            } else if (min < 10) {
                timeString = hour + ":0" + min + " " + format;
            } else {
                timeString = hour + ":" + min + " " + format;
            }

            //check which imagebutton view clicked to set selected date/time to correct view
            if (mTimeDateViewClicked == ibStartTime) {
                tv_StartTime.setText(timeString);

            } else if (mTimeDateViewClicked == ibHostEndTime) {
                tvEndTime.setText(timeString);
            }
            setProfileView();
        }
    }


    public void setTimeView() {
        tpStartTime.setVisibility(View.VISIBLE);
        bTimeSet.setVisibility(View.VISIBLE);
        ibHostEndTime.setVisibility(View.GONE);
        ibStartTime.setVisibility(View.GONE);
        etHostAddress.setVisibility(View.GONE);
        ivHostAvatar.setVisibility(View.GONE);
        tilTopic.setVisibility(View.GONE);
        tv_StartTime.setVisibility(View.GONE);
        tvEndTime.setVisibility(View.GONE);
        ibDate.setVisibility(View.INVISIBLE);
        tvDate.setVisibility(View.INVISIBLE);
        tvHostMessageLabel.setVisibility(View.INVISIBLE);
        tvHostClassMessage.setVisibility(View.INVISIBLE);
        ibHostIt.setVisibility(View.INVISIBLE);
    }


    public void setProfileView() {
        tpStartTime.setVisibility(View.GONE);
        bTimeSet.setVisibility(View.GONE);
        ibHostEndTime.setVisibility(View.VISIBLE);
        ibStartTime.setVisibility(View.VISIBLE);
        etHostAddress.setVisibility(View.VISIBLE);
        ivHostAvatar.setVisibility(View.VISIBLE);
        etHostTopic.setVisibility(View.VISIBLE);
        tv_StartTime.setVisibility(View.VISIBLE);
        tvEndTime.setVisibility(View.VISIBLE);
        ibDate.setVisibility(View.VISIBLE);
        tvDate.setVisibility(View.VISIBLE);
        tvHostMessageLabel.setVisibility(View.VISIBLE);
        tvHostClassMessage.setVisibility(View.VISIBLE);
        ibHostIt.setVisibility(View.VISIBLE);
        tilCityState.setVisibility(View.VISIBLE);
        tilChavrutaAddress.setVisibility(View.VISIBLE);
        tilTopic.setVisibility(View.VISIBLE);

    }

    // display current date
    public void setCurrentDateVars() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

    }

    public void postNewHostSession() {
        ChavrutaTextValidation textValidator = new ChavrutaTextValidation();
        //checks if all text validated and appropriate
        Boolean textValidationPass = true;
        mSessionMessage = tvHostClassMessage.getText().toString();
        mSessionDate = tvDate.getText().toString();
        mStartTime = tv_StartTime.getText().toString();
        mEndTime = tvEndTime.getText().toString();
        mSefer = etHostTopic.getText().toString();
        textValidationPass = textValidator.validateSeferInAddHost(mSefer);
        mLocation = etHostAddress.getText().toString();
        mHostCityState = acCityState.getText().toString();
        mHostId = userDetailsInstance.getmUserId();
        mHostAvatarNumber = userDetailsInstance.getmUserAvatarNumberString();
        //@hostAvatarNumber: asign base64array for custom image, or avatar template number if template image
        if (mHostAvatarNumber.equals("999"))
            mHostAvatarNumber = userDetailsInstance.getUserAvatarBase64String();

        String confirmed = "not confirmed";
        String chavrutaRequest1 = "None", chavrutaRequest2 = "None", chavrutaRequest3 = "None";
        String newHost = "new host";
        if (textValidationPass) {
            ServerConnect postToServer = new ServerConnect(this, userDetailsInstance);
            postToServer.execute(newHost, mHostFirstName, mHostLastName, mHostAvatarNumber, mSessionMessage, mSessionDate,
                    mStartTime, mEndTime, mSefer, mLocation, mHostCityState, mHostId, chavrutaRequest1, chavrutaRequest2, chavrutaRequest3, confirmed);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please input sefer of 3-30 characters", Toast.LENGTH_LONG).show();
        }
    }

    //    Animations
    private void animateHostIt(View view) {
        ObjectAnimator.ofFloat(
                view, "translationY", 130f)
                .setDuration(1000)
                .start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AddSelect.class);
        startActivity(intent);
    }
}
