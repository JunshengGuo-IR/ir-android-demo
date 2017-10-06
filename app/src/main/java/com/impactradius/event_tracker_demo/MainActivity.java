package com.impactradius.event_tracker_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
    private Button deleteItemsButton;
    private TextView infoField;
    private final String note = "\n(For the details of posted data, please check the logcat.)";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemSubmitButton = (Button) findViewById(R.id.bItemSubmit);
        trackInstallButton = (Button) findViewById(R.id.trackInstallButton);
        addItemButton = (Button) findViewById(R.id.bAddItem);
        submitItemsButton = (Button) findViewById(R.id.bSubmitItems);
        infoField = (TextView) findViewById(R.id.editText2);
        deleteItemsButton = (Button) findViewById(R.id.button);

        EventTracker.initialize(getBaseContext(), "YOUR APP ID", "YOUR ACCOUNT SID", "YOUR AUTHORIZATION TOKEN");
        tracker = EventTracker.getInstance();

        // setDebug(boolean) - This will allow you to trace data to the LogCat and also put your Impact Radius account in TEST mode.
        //You MUST set this to OFF before submitting to the Google Play store for attribution to work.
        tracker.setDebug(true);

        // Alternatively you can trace debug data to the LogCat using setTrace(boolean)
        // tracker.setTrace(true);

        event = tracker.event("EventName"); // Conversion instance for multiple item scenarios
        event.setDebug(true);
        populateAllFields(event);

        event.setPostalCode("91423")
            .addItem("234", "sku3234", "3", "3.00").setName("Name1");
        event.addItem("CAT-234234234", "343424", "3", "5.00")
            .addCustom("custom1", "224234234")
            .addCustom("custom2", "22424")
            .setName("Trans Name");

        // One conversion + 2 Items + submit example
        itemSubmitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                tracker.submit(event);
                infoField.setText("Submitted a conversion event with 2 items." + note);

            }
        });

        // Simple tracking methods example
        trackInstallButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                tracker.trackSession();
                infoField.setText("Submitted an event with session info." + note);
            }
        });

        final Event eventWithCustomizedItems = tracker.event("Event With Customized Items");
        eventWithCustomizedItems.setDebug(true);
        populateAllFields(eventWithCustomizedItems);
        // Add multiple items to one conversion example
        addItemButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Random r = new Random();
                int i1 = r.nextInt(1000 - 1) + 1;

                eventWithCustomizedItems.addItem("CAT-" + i1, "" + i1, "1", "15.00")
                    .addCustom("custom" + i1, "" + i1)
                    .addCustom("custom765" + i1, "" + i1)
                    .setName("Name" + i1)
                    .setPromoCode("Item-Promo-" + i1)
                    .setPromoCodeDescription("Item-PromoDesc-" + i1)
                    .setDeliveryType("Ground-" + i1)
                    .setDiscount(String.valueOf(i1 / 4))
                    .setMpn("MPN-" + i1)
                    .setPrice(String.valueOf(i1))
                    .setTotalDiscount(String.valueOf(i1 / 3))
                    .setSubCategory("SUBCAT-" + i1)
                    .setRebate(String.valueOf(i1 / 5));

                infoField.setText("Added 1 item. Total items: " + eventWithCustomizedItems.getItemArray().size());
            }
        });

        submitItemsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                tracker.submit(eventWithCustomizedItems);
                infoField.setText("Submitted an event with "
                    + eventWithCustomizedItems.getItemArray().size()
                    + " items." + note);
            }
        });

        deleteItemsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                eventWithCustomizedItems.getItemArray().clear();
                infoField.setText("All items have been removed.");
            }
        });

    }

    private void populateAllFields(Event event) {
        event.setAppId("demo app")
            .setClickId("TEST_Click_id")
            .setCallerId("8056669999")
            .setCurrency("USD")
            .setCustomerId("CustomerId_Test")
            .setCustomerCity("CustomerCity_Test")
            .setCustomerCountry("CustomerCountry_Test")
            .setCustomerEmail("customer@test.com")
            .setCustomerRegion("CustomerRegion_Test")
            .setCustomerStatus("CustomerStatus_Test")
            .setDate1("Date1_Test")
            .setDate2("Date2_Test")
            .setDate3("Date3_Test")
            .setDispositionCode("289")
            .setGiftPurchase("YES")
            .setHearAboutUs("Google")
            .setMoney1("Money1_Test")
            .setMoney2("Money2_Test")
            .setMoney3("Money3_Test")
            .setLocationId("LocationId_Test")
            .setLocationName("LocationName_Test")
            .setLocationType("LocationType_Test")
            .setNote("Note_Test")
            .setNumeric1("12.5")
            .setNumeric2("13.5")
            .setNumeric3("14.5")
            .setOrderDiscount("0.2")
            .setOrderSubTotalPostDiscount("15")
            .setOrderSubTotalPreDiscount("15.2")
            .setOrderId("Order-Id-Test")
            .setOrderShipping("ground transport")
            .setOrderTax("1.5")
            .setText1("Text1_Test")
            .setText2("Text2_Test")
            .setText3("Text3_Test")
            .setPhoneNumber("8058885555")
            .setPromoCode("PromoCode_Test")
            .setPromoCodeDesc("PromoCodeDesc_Test")
            .setPaymentType("credit")
            .setPostalCode("93101")
            .setRebate("Rebate_Test")
            .setSearchTerm("SearchTerm_Test")
            .setUserAgent("Chrome");
    }
}
