package com.moyinoluwa.gdgngevents.Organizers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.firebase.client.Firebase;
import com.moyinoluwa.gdgngevents.NetworkConnection;
import com.moyinoluwa.gdgngevents.R;

import java.util.HashMap;
import java.util.Map;

public class CreateEventsActivity extends AppCompatActivity implements
        CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {
    private Firebase firebaseRef;
    private NetworkConnection networkConnection;
    private EditText eventName;
    private EditText shortDescription;
    private EditText longDescription;
    private EditText venue;
    private TextView timeText;
    private TextView dateText;
    private EditText duration;
    private EditText speakers;
    private EditText eventTags;
    private Button createEvent;
    private Button dateButton;
    private Button timeButton;
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventName = (EditText) findViewById(R.id.eventname);
        shortDescription = (EditText) findViewById(R.id.short_description);
        longDescription = (EditText) findViewById(R.id.long_description);
        venue = (EditText) findViewById(R.id.venue);
        duration = (EditText) findViewById(R.id.duration);
        speakers = (EditText) findViewById(R.id.speakers);
        eventTags = (EditText) findViewById(R.id.event_tags);
        createEvent = (Button) findViewById(R.id.button);
        dateButton = (Button) findViewById(R.id.date_button);
        timeButton = (Button) findViewById(R.id.time_button);
        dateText = (TextView) findViewById(R.id.event_date);
        timeText = (TextView) findViewById(R.id.event_time);
        firebaseRef = new Firebase(getResources().getString(R.string.event_firebase_url));
        networkConnection = new NetworkConnection(this);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the date picker
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(CreateEventsActivity.this);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the time picker
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(CreateEventsActivity.this)
                        .setForced12hFormat();
                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gdg = getIntent().getStringExtra("organizer_gdg");

                // Checks for empty fields before sending the event to the server
                if (eventName.getText().toString().isEmpty() ||
                        shortDescription.getText().toString().isEmpty() ||
                        longDescription.getText().toString().isEmpty() ||
                        venue.getText().toString().isEmpty() ||
                        duration.getText().toString().isEmpty() ||
                        speakers.getText().toString().isEmpty() ||
                        eventTags.getText().toString().isEmpty() ||
                        dateText.getText().toString().isEmpty() ||
                        timeText.getText().toString().isEmpty()) {
                    Toast.makeText(CreateEventsActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (networkConnection.isInternetOn()) {
                        Map<String, String> event = new HashMap<String, String>();
                        event.put("event_name", eventName.getText().toString());
                        event.put("short_description", shortDescription.getText().toString());
                        event.put("long_description", longDescription.getText().toString());
                        event.put("venue", venue.getText().toString());
                        event.put("event_date", dateText.getText().toString());
                        event.put("event_time", timeText.getText().toString());
                        event.put("duration", duration.getText().toString());
                        event.put("speakers", speakers.getText().toString());
                        event.put("event_tags", eventTags.getText().toString());
                        firebaseRef.child(gdg).push().setValue(event);

                        Toast.makeText(CreateEventsActivity.this, "Your event has been uploaded successfully.", Toast.LENGTH_SHORT).show();

                        // Clears the fields for the next set of inputs
                        eventName.setText("");
                        shortDescription.setText("");
                        longDescription.setText("");
                        venue.setText("");
                        duration.setText("");
                        speakers.setText("");
                        eventTags.setText("");
                        dateText.setText("");
                        timeText.setText("");
                    } else {
                        Toast.makeText(CreateEventsActivity.this, getResources().getString(R.string.no_network_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        dateText.setText(formattedDate(year, monthOfYear, dayOfMonth));
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        timeText.setText(formattedTime(hourOfDay, minute));
    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = (CalendarDatePickerDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialogFragment != null) {
            calendarDatePickerDialogFragment.setOnDateSetListener(this);
        }

        RadialTimePickerDialogFragment rtpd = (RadialTimePickerDialogFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_TIME_PICKER);
        if (rtpd != null) {
            rtpd.setOnTimeSetListener(this);
        }
    }

    /**
     * Ensures the hour and minutes have two digits each and concatenates them
     **/
    private String formattedTime(int hour, int minute) {
        String newHour = String.valueOf(hour);
        if (newHour.length() == 1) {
            newHour = 0 + newHour;
        }

        String min = String.valueOf(minute);
        if (min.length() == 1) {
            min = 0 + min;
        }

        return newHour + ":" + min;
    }

    /**
     * Ensures the day and month have two digits each and concatenates them
     **/
    private String formattedDate(int year, int monthOfYear, int dayOfMonth) {
        String day = String.valueOf(dayOfMonth);
        // The month was returned as 0 indexed. Added 1 to change this
        String month = String.valueOf(monthOfYear + 1);

        if (day.length() == 1) {
            day = 0 + day;
        }

        if (month.length() == 1) {
            month = 0 + month;
        }

        return day + "-" + month + "-" + year;
    }
}
