package com.impactradius.event_tracker_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.impactradius.Event;
import com.impactradius.EventTracker;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EventTracker tracker;
    private Event event;
    private Button itemSubmitButton;
    private Button trackInstallButton;
    private Button addItemButton;
    private Button submitItemsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemSubmitButton = (Button) findViewById(R.id.bItemSubmit);
        trackInstallButton = (Button) findViewById(R.id.trackInstallButton);
        addItemButton = (Button) findViewById(R.id.bAddItem);
        submitItemsButton = (Button) findViewById(R.id.bSubmitItems);

        EventTracker.initialize(getBaseContext(), "YOUR APP ID", "YOUR ACCOUNT SID", "YOUR AUTHORIZATION TOKEN");
        tracker = EventTracker.getInstance();

        // setDebug(boolean) - This will allow you to trace data to the LogCat and also put your Impact Radius account in TEST mode.
        //You MUST set this to OFF before submitting to the Google Play store for attribution to work.
        tracker.setDebug(true);

        // Alternatively you can trace debug data to the LogCat using setTrace(boolean)
        // tracker.setTrace(true);

        event = tracker.event("EventName"); // Conversion instance for multiple item scenarios

        // One conversion + 2 Items + submit example
        itemSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                event.setPostalCode("91423")
                    .addItem("234", "sku3234", "3", "3.00").setName("Name1");

                event.setDebug(true);

                event.addItem("CAT-234234234", "343424", "3", "5.00")
                    .addCustom("custom1", "224234234")
                    .addCustom("custom2", "22424")
                    .setName("Trans Name");

                tracker.submit(event);

            }
        });

        // Simple tracking methods example
        trackInstallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tracker.trackSession();
            }
        });

        // Add multiple items to one conversion example
        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Random r = new Random();
                int i1 = r.nextInt(1000 - 1) + 1;

                event.addItem("CAT-" + i1, "" + i1, "1", "15.00")
                    .addCustom("custom" + i1, "" + i1)
                    .addCustom("custom765" + i1, "" + i1)
                    .setName("Name" + i1);

            }
        });

        submitItemsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tracker.submit(event);
            }
        });

    }
}

