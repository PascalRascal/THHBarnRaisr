package stohio.barnraisr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Button;

import com.facebook.Profile;

import java.util.Calendar;

public class EventCreate extends AppCompatActivity {
    private EditText createTitle, createDesc;
    private static Button timePicker, datePicker;
    NumberPicker np;
    static String time = "";
    static String date = "";
    int maxpart = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);




        timePicker = (Button)findViewById(R.id.createTime);
        setButtonTime(hour, min);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        datePicker = (Button)findViewById(R.id.createDate);
        setButtonDate(year, month, day);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                System.out.print("");
            }
        });

        np = (NumberPicker) findViewById(R.id.numberPicker1);

        np.setMaxValue(20);
        np.setMinValue(0);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                maxpart = newVal;
            }
        });


        createTitle = (EditText) findViewById(R.id.createTitle);
        createDesc = (EditText) findViewById(R.id.createDesc);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 {

                }
                createEvent();
                finish();
            }
        });
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setButtonTime(hourOfDay, minute);

        }
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c= Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            int year = selectedYear;
            int month = selectedMonth;
            int day = selectedDay;

            // set selected date into Button
            setButtonDate(year,month,day);


        }
    };



    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static void setButtonTime(int hour, int min){
        String format;
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

        time = new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format).toString();

        timePicker.setText(time);
    }
    public static void setButtonDate(int year, int month, int day){
        date = new StringBuilder().append(month + 1)
                .append("-").append(day).append("-").append(year)
                .append(" ").toString();
        datePicker.setText(date);
    }

    private Event createEvent(){
        Long tsLong = System.currentTimeMillis();
        String timestamp = tsLong.toString();
        String longi = "";
        String lati = "";
        Location loc = null;
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        final LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

            }

            public void onStatusChanged(String s, int i, Bundle b){

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,10,locationListener);
            if(loc ==null)
            {
                longi = "" + -81.9329870;
                lati =  "" + 41.4685770;
            }
            if(loc != null){
                double longitude = loc.getLongitude();
                double latitude = loc.getLatitude();
                longi = "" + longitude;
                lati = "" + latitude;
                System.out.println("NOGF " + longi);
                System.out.println("nogf1 " + lati);
            }
        }catch(SecurityException e){
            e.printStackTrace();
        }


        Event event = new Event("0",createTitle.getText().toString(), createDesc.getText().toString(), date, time, timestamp,longi,lati, Profile.getCurrentProfile().getId(), maxpart);
        event.post();
        return event;
    }



}
