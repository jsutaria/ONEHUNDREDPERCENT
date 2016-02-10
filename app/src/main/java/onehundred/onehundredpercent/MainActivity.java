package onehundred.onehundredpercent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private LinearLayout linearLayout;
    private ImageView mImageView;

    Canvas canvas;
    Bitmap bitmap;
    Paint paintCurrentRun;
    Paint textDailyRun;
    Paint numbersDailyRun;
    Paint paintTotalRun;
    Paint textWeeklyRun;
    Paint numbersWeeklyRun;
    Paint paintCenter;
    int radius;
    int dailyPadding;
    int totalPadding;

    private int TEXTOFFSETMAIN = 60;
    private int TEXTOFFSETMIN = 25;

    private int TEXTSIZEWORDS = 75;
    private int TEXTSIZENUMBERS = 100;

    Rect text1;
    Rect text2;
    Rect text3;
    Rect text4;

    SharedPreferences sharedPreferences;

    private LocationManager lm;
    private LocationListener locationListener;
    private Location lastLocation;
    private float dailyDistance;
    private float weeklyDistance;

    private int dailyRequirement;
    private int weeklyRequirement;

    private boolean running;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        running = false;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation != null && running) {
                    dailyDistance += location.distanceTo(lastLocation) * 0.000621371f;
                    weeklyDistance += location.distanceTo(lastLocation) * 0.000621371f;
                    UpdateDistance();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("dailyDistance", dailyDistance);
                    editor.putFloat("weeklyDistance", weeklyDistance);
                    editor.apply();
                }
                lastLocation = new Location(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, .5f, locationListener);

        mContext = getApplicationContext();
        sharedPreferences = mContext.getSharedPreferences(getString(R.string.sharedpreferences), Context.MODE_PRIVATE);
        dailyDistance = sharedPreferences.getFloat("dailyDistance", 0);
        weeklyDistance = sharedPreferences.getFloat("weeklyDistance", 0);

        dailyRequirement = sharedPreferences.getInt("dailyRequirement", 0);
        weeklyRequirement = sharedPreferences.getInt("weeklyRequirement", 0);

        linearLayout = (LinearLayout) findViewById(R.id.linear);
        mImageView = (ImageView) findViewById(R.id.completion);
        linearLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                bitmap = Bitmap.createBitmap(linearLayout.getWidth(), linearLayout.getWidth(), Bitmap.Config.ARGB_8888);

                canvas = new Canvas(bitmap);
                canvas.drawColor(Color.TRANSPARENT);

                paintCurrentRun = new Paint();
                paintCurrentRun.setStyle(Paint.Style.FILL);
                paintCurrentRun.setColor(Color.RED);
                paintCurrentRun.setAntiAlias(true);

                paintTotalRun = new Paint();
                paintTotalRun.setStyle(Paint.Style.FILL);
                paintTotalRun.setColor(Color.BLACK);
                paintTotalRun.setAntiAlias(true);

                paintCenter = new Paint();
                paintCenter.setStyle(Paint.Style.FILL);
                paintCenter.setColor(getColor(R.color.background));
                paintCenter.setAntiAlias(true);

                textDailyRun = new Paint();
                textDailyRun.setTextSize(TEXTSIZEWORDS);
                textDailyRun.setTypeface(Typeface.DEFAULT);
                textDailyRun.setColor(Color.RED);
                textDailyRun.setStyle(Paint.Style.FILL);

                numbersDailyRun = new Paint();
                numbersDailyRun.setTextSize(TEXTSIZENUMBERS);
                numbersDailyRun.setTypeface(Typeface.DEFAULT);
                numbersDailyRun.setColor(Color.RED);
                numbersDailyRun.setStyle(Paint.Style.FILL);

                textWeeklyRun = new Paint();
                textWeeklyRun.setTextSize(TEXTSIZEWORDS);
                textWeeklyRun.setTypeface(Typeface.DEFAULT);
                textWeeklyRun.setColor(Color.BLACK);
                textWeeklyRun.setStyle(Paint.Style.FILL);

                numbersWeeklyRun = new Paint();
                numbersWeeklyRun.setTextSize(TEXTSIZENUMBERS);
                numbersWeeklyRun.setTypeface(Typeface.DEFAULT);
                numbersWeeklyRun.setColor(Color.BLACK);
                numbersWeeklyRun.setStyle(Paint.Style.FILL);

                radius = Math.min(canvas.getWidth(), canvas.getHeight() / 2);

                dailyPadding = 20;
                totalPadding = dailyPadding + 10;

                text1 = new Rect();
                text2 = new Rect();
                text3 = new Rect();
                text4 = new Rect();

                UpdateDistance();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://onehundred.onehundredpercent/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("dailyDistance", dailyDistance);
        editor.putFloat("weeklyDistance", weeklyDistance);
        editor.apply();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    private String GetString(int val) {
        switch (val) {
            case 0:
                return getString(R.string.currentRunTitle);

            case 1:
                float distance = (float) (Math.floor(dailyDistance*100) / 100);
                return String.valueOf(distance) + " / " + String.valueOf(dailyRequirement) + " miles";

            case 2:
                return getString(R.string.totalRunTitle);

            case 3:
                float distance1 = (float) (Math.floor(weeklyDistance*100) / 100);
                return String.valueOf(distance1) + " / " + String.valueOf(weeklyRequirement) + " miles";

            default:
                return "Error: Unable to find string";
        }
    }

    private float getAngle(float req1, int req2) {
        if (req2 != 0) {
            return req1 / (float) req2 * 360;
        }
        Log.v("Angle", "LAWD HELP ME");
        return 0;
    }

    private void UpdateDistance() {
        //Outer Arc - Daily completion
        canvas.drawArc(0, 0, canvas.getWidth(), canvas.getHeight(), -90, getAngle(dailyDistance, dailyRequirement), true, paintCurrentRun);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, radius - dailyPadding, paintCenter);

        //Inner Arc - Weekly completion
        canvas.drawArc(dailyPadding, dailyPadding, canvas.getWidth() - dailyPadding, canvas.getHeight() - dailyPadding, -90, getAngle(weeklyDistance, weeklyRequirement), true, paintTotalRun);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, radius - totalPadding, paintCenter);

        String textCurrentRun = GetString(0);
        String textDistanceCurrentRun = GetString(1);
        String textTotalRun = GetString(2);
        String textDistanceTotalRun = GetString(3);

        textDailyRun.getTextBounds(textCurrentRun, 0, textCurrentRun.length(), text1);
        numbersDailyRun.getTextBounds(textDistanceCurrentRun, 0, textDistanceCurrentRun.length(), text2);
        textWeeklyRun.getTextBounds(textTotalRun, 0, textTotalRun.length(), text3);
        numbersWeeklyRun.getTextBounds(textDistanceTotalRun, 0, textDistanceTotalRun.length(), text4);

        canvas.drawText(textCurrentRun, canvas.getWidth() / 2 - text1.width() / 2, canvas.getHeight() / 2 - text2.height() - text1.height() / 2 - TEXTOFFSETMAIN - TEXTOFFSETMIN, textDailyRun);
        canvas.drawText(textDistanceCurrentRun, canvas.getWidth() / 2 - text2.width() / 2, canvas.getHeight() / 2 - text2.height() / 2 - TEXTOFFSETMAIN, numbersDailyRun);
        canvas.drawText(textTotalRun, canvas.getWidth() / 2 - text3.width() / 2, canvas.getHeight() / 2 + text3.height() / 2 + TEXTOFFSETMAIN, textWeeklyRun);
        canvas.drawText(textDistanceTotalRun, canvas.getWidth() / 2 - text4.width() / 2, canvas.getHeight() / 2 + text3.height() + text4.height() / 2 + TEXTOFFSETMAIN + TEXTOFFSETMIN, numbersWeeklyRun);

        mImageView.setImageBitmap(bitmap);
    }

    public void OpenSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("dailyRequirement", dailyRequirement);
        intent.putExtra("weeklyRequirement", weeklyRequirement);
        startActivity(intent);
    }

    public void OpenPaymentSettings(View view) {
        Intent intent = new Intent(this, PaymentSettings.class);
        startActivity(intent);
    }

    public void ToggleTracking(View view) {
        Button button = (Button) findViewById(R.id.togglerunning);
        if (running) {
            running = false;
            button.setText("Start Running");
        } else {
            running = true;
            button.setText("Stop Running");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://onehundred.onehundredpercent/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
}
