package androidninja.beaconq;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends Activity {

    protected static final String TAG = HomeActivity.class.getSimpleName();

    private BeaconManager beaconManager;
    private TextView tbxTotalTime;
    private TextView tbxStartTime;
    private TextView tbxEndTime;
    private TextView tbxAverage;
    private BeaconQApplication app;
    private Beacon prevBeacon;
    private Boolean orderStarted;
    private LinearLayout layout;
    PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tbxTotalTime = (TextView) findViewById(R.id.orderTotalTime);
        tbxStartTime = (TextView) findViewById(R.id.orderStartTime);
        tbxEndTime = (TextView) findViewById(R.id.orderEndTime);
        tbxAverage = (TextView) findViewById(R.id.tbxAverage);
        layout = (LinearLayout) findViewById(R.id.homeLayout);
        app = (BeaconQApplication) getApplicationContext();
        beaconManager = app.getInstance().getBeaconManager();
        setupRanging();
        orderStarted = false;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
        getAverageWaitTime();
    }

    @Override
    protected void onStart() {
        super.onStart();

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                startRanging();
            }
        });


        startRanging();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            beaconManager.stopRanging(Configs.ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wl.release();
    }

    private void setupRanging() {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

                for (int i = 0; i < beacons.size(); i++) {

                    Beacon curBeacon = beacons.get(i);
                    double distance = Utils.computeAccuracy(curBeacon);
                    if (distance <= Configs.rangeMetersLimit) {
                        if (prevBeacon != null) {
                            if (prevBeacon.getMajor() != curBeacon.getMajor()) {
                                processValidBeacon(curBeacon);
                            }//end if not same beacon
                        } else {
                            processValidBeacon(curBeacon);
                        }
                    }//end if withing range limit
                    Log.d(TAG, "Ranged beacons: " + beacons);
                }
            }
        });
    }


    private void startRanging() {

        try {
            beaconManager.startRanging(Configs.ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot start ranging", e);
        }

    }


    private void processValidBeacon(Beacon beacon) {

        app = (BeaconQApplication) getApplicationContext();
        final BeaconMetaData validBeacon = app.getInstance().findValidBeacon(beacon);
        if (validBeacon != null) {
            prevBeacon = beacon;
            //runOnUiThread(new Runnable() {
                //public void run() {
                    if (validBeacon.getSequenceNumber() == 1) {
                        app.orderStartTime = new Date();
                        tbxStartTime.setText(app.orderStartTime.toString());
                        tbxTotalTime.setText("00:00");
                        tbxEndTime.setText("00:00");
                        orderStarted = true;
                    } else if (validBeacon.getSequenceNumber() == 2 && orderStarted) {
                        app.orderEndTime = new Date();
                        Date startDate = app.orderStartTime;
                        Date endDate = app.orderEndTime;
                        tbxEndTime.setText(endDate.toString());
                        if (startDate != null && endDate != null) {
                            long difference = endDate.getTime() - startDate.getTime();
                            saveOrderTime((int) difference / 1000);
                            String total = toMinutesAndSecondsString(difference);
                            tbxTotalTime.setText(total);
                            orderStarted = false;
                            setActivityBackgroundColor(Color.GREEN);
                        }//end valid date times
                    }
                }
            //});//end ui thread
        }
    //}

    private String toMinutesAndSecondsString(long millis) {
        int seconds=(int)millis/1000;
        long remainderMillis=millis % 1000;
        if(remainderMillis>=500){
            seconds++;
        }

        String time = "";
        if (seconds < 60) {
            time = "00:" + String.valueOf(seconds);
            return time;
        }
        int hours = seconds / 3600;
        int remainder = seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        time = String.valueOf(mins) + ":" + String.valueOf(secs);

        return time;
    }


    private void getAverageWaitTime() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        try {
            ParseCloud.callFunctionInBackground("averageWaitTime", params, new FunctionCallback<Double>() {
                public void done(Double ratings, ParseException e) {
                    if (e == null) {
                        long millis = ratings.intValue() * 1000;
                        String avgText = toMinutesAndSecondsString(millis);
                        tbxAverage.setText(avgText);
                    }
                }
            });

        } catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }
    }

    private void saveOrderTime(int seconds){
        ParseObject orderMetric = new ParseObject("OrderMetrics");
        orderMetric.put("orderNumber", "1337");
        orderMetric.put("waitTime",seconds);
        orderMetric.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                   Log.i(TAG,"Metric Saved");
                } else {
                    Log.i(TAG, "Metric Not Saved");
                }
            }
        });
    }

    public void setActivityBackgroundColor(int color) {
        layout.setBackgroundColor(color);
    }
}
