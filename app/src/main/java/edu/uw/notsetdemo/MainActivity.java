package edu.uw.notsetdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "**Main**";

    public static final String ACTION_SMS_SENT = "edu.uw.notsetdemo.ACTION_SMS_SENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View launchButton = findViewById(R.id.btnLaunch);
        launchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "Launch button pressed");

                //explicit intent
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("edu.uw.notsetdemo.message", "Hello from MainActivity!");

                startActivity(intent);
            }
        });

    }

    public void callNumber(View v) {
        Log.v(TAG, "Call button pressed");

        //implicit intent
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:206-685-1622"));

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);

    }

    private static int REQUEST_PICTURE_CODE = 1;

    public void takePicture(View v) {
        Log.v(TAG, "Camera button pressed");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_PICTURE_CODE);
        }

    }

    public static final int SMS_SEND_CODE = 2;

    public void sendMessage(View v) {
        Log.v(TAG, "Message button pressed");

        SmsManager smsManager = SmsManager.getDefault();

        Intent smsIntent = new Intent(ACTION_SMS_SENT);    // Implicit - delivered to anyone who
                                                        // can respond to the action we made

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SMS_SEND_CODE, smsIntent, 0);

        smsManager.sendTextMessage("5554", null, "This is a message!", pendingIntent, null);
    }

    public static final int NOTIFY_ID = 1;

    private int notifyCount = 0;

    public void notify(View v){
        Log.v(TAG, "Notify button pressed");
        notifyCount++;

        // Build the notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("NOTICE NOTICE NOTICE")
                .setContentText("This is notification " + notifyCount);

        // Must be >= HIGH to pop up at the top of the screen
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        // Must ALSO make the phone vibrate or play sound to pop up
        mBuilder.setVibrate(new long[]{0, 500, 200, 500});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent intent = new Intent(this, SecondActivity.class);

        // We want to make it so back takes us to the previous activity in the app
        // after clicking on a notification
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SecondActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                            // Flag updates the existing intent if it's the same
                                            // Ex: notification of 1 email replaced with 2 if you
                                            // receive another

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // ID specifies that notifications are the same so should override each other
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_PICTURE_CODE && resultCode == RESULT_OK){
            //I got picture!!
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");

            ImageView imageView = (ImageView)findViewById(R.id.imgThumbnail);
            imageView.setImageBitmap(imageBitmap);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_notify:
                notify(null);
                return true;
            case R.id.menu_item_prefs:
                Log.v(TAG, "Settings button pressed");
                return true;
            case R.id.menu_item_click:
                Log.v(TAG, "Extra button pressed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
