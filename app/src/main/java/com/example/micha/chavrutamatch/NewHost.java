package com.example.micha.chavrutamatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

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
    @BindView(R.id.fl_host_pic)
    android.widget.FrameLayout flHostPic;
    @BindView(R.id.iv_host_avatar)
    ImageView ivHostAvatar;
    @BindView(R.id.host_address)
    EditText etHostAddress;
    @BindView(R.id.ib_host_end_time)
    ImageButton ibHostEndTime;
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
    @BindView(R.id.host_it) ImageButton ibHostIt;
    @BindView(R.id.tv_host_user_name) TextView tvAddHost;

    private String format;
    private View.OnClickListener listener;
    private View mTimeDateViewClicked;
    private int hour;
    private int min;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    public static final int DIALOG_ID = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_host_entry);
        ButterKnife.bind(this);

        ibStartTime.setOnClickListener(this);
        ibHostEndTime.setOnClickListener(this);
        bTimeSet.setOnClickListener(this);
        ibDate.setOnClickListener(this);
        ibHostIt.setOnClickListener(this);
        setCurrentDateVars();
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
                    mMonth = month;
                    mYear = year;
                    mDay = dayOfMonth;

                    // set selected date into textview
                    tvDate.setText(new StringBuilder()
                            // Month is 0 based, just add 1
                            .append(mMonth + 1).append("-").append(mDay).append("-")
                            .append(mYear).append(" "));
                }
            };

    public void onClick(View v) {
        // show clock if time related view clicked, DatePicker if not
        if (v == ibStartTime || v == ibHostEndTime) {
            setTimeView();
        } else if (v == ibDate) {
            showDialog(DIALOG_ID);

        } else if (v == bTimeSet) { //when confirm time button click, set values
            confirmTime(v);
            setProfileView();
        } else {
            Log.e(NewHost.class.getSimpleName(), "View " + v + " has no match onClick");
        }
        //save original view clicked to determine which to display when time confirmed button clicked
        mTimeDateViewClicked = v;
    }

    //if confirm button clicked set global date/time
    public void confirmTime(View view) {
        String timeString;
        boolean minZero = true;
        if (mTimeDateViewClicked == ibHostEndTime || mTimeDateViewClicked == ibStartTime) {
            if (Build.VERSION.SDK_INT < 24) {
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
                timeString = hour + " :" + "00 " + format;
            } else {
                timeString = hour + " :" + min + " " + format;
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
        flHostPic.setVisibility(View.GONE);
        etHostTopic.setVisibility(View.GONE);
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
        flHostPic.setVisibility(View.VISIBLE);
        etHostTopic.setVisibility(View.VISIBLE);
        tv_StartTime.setVisibility(View.VISIBLE);
        tvEndTime.setVisibility(View.VISIBLE);
        ibDate.setVisibility(View.VISIBLE);
        tvDate.setVisibility(View.VISIBLE);
        tvHostMessageLabel.setVisibility(View.VISIBLE);
        tvHostClassMessage.setVisibility(View.VISIBLE);
        ibHostIt.setVisibility(View.VISIBLE);

    }

    // display current date
    public void setCurrentDateVars() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

    }

}
