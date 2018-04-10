package com.example.sameer.geofencingapp;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GeofencingClient mGeofencingClient;
    ArrayList<Geofence> mGeofenceList;
    PendingIntent mGeofencePendingIntent;
    Button mAddGeofenceButton;
    SQLiteDatabase sqLiteDatabase;
    Spinner spinner;
    List<Offer> offers;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("ID");
        if (null != id) {
            Intent intent = new Intent(MainActivity.this, DealsActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);

        sqLiteDatabase = openOrCreateDatabase("kajsda.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE  IF NOT EXISTS  register (name VARCHAR(20));");
        mAddGeofenceButton = (Button) findViewById(R.id.add_geofence_btn);

        offers = new ArrayList<>();
        offers.add(new Offer(13.353260, 74.7934509, 300, "Academic Block 5"));
        offers.add(new Offer(13.3529115, 74.7898083, 300, "Mc Donald's"));
        offers.add(new Offer(13.353260, 74.7934509, 300, "Kentucky Fried Chicken"));


        final TextInputEditText xcord = findViewById(R.id.xcord);
        final TextInputEditText ycord = findViewById(R.id.ycord);
        final TextInputEditText radius = findViewById(R.id.radius);

        spinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        for (Offer o : offers) {
            adapter.add(o.name);
        }

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xcord.setText(String.valueOf(offers.get(position).x));
                ycord.setText(String.valueOf(offers.get(position).y));
                radius.setText(String.valueOf(offers.get(position).r));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mAddGeofenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItem() == null) {
                    Toast.makeText(MainActivity.this, "Select some item first", Toast.LENGTH_SHORT).show();
                    return;
                }
                addGeofencing();
            }
        });

        Button button = findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() == null) {
                    Toast.makeText(MainActivity.this, "Select any spinner item", Toast.LENGTH_SHORT).show();
                } else {
                    sendNotification(offers.get(spinner.getSelectedItemPosition()).name);
                }
            }
        });
        Button button1 = findViewById(R.id.showRegisterd);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabase.close();
                Intent intent = new Intent(MainActivity.this, RegisteredActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sqLiteDatabase = openOrCreateDatabase("kajsda.db", MODE_PRIVATE, null);

    }

    class Offer {
        double x;
        double y;
        double r;
        String name;

        public Offer(double x, double y, double r, String name) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.name = name;
        }
    }

    private void addGeofencing() {
        Offer offer = offers.get(spinner.getSelectedItemPosition());
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceList = new ArrayList<Geofence>();
        sqLiteDatabase.execSQL("INSERT INTO register VALUES('" + offer.name + "');");

        mGeofenceList.add(new Geofence.Builder().setRequestId(offer.name).setCircularRegion(offer.x, offer.y, (float) offer.r).setExpirationDuration(3600000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        Toast.makeText(getApplicationContext(), "Geofence added succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        Toast.makeText(getApplicationContext(), "Geofence could not be added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }


    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("ID", notificationDetails);
        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher_foreground))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }
}
