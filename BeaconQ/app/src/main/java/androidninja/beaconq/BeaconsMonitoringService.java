package androidninja.beaconq;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;


import java.util.List;

/**
 * Created by nikolas on 14-11-21.
 */
public class BeaconsMonitoringService  extends Service {

    private static final String TAG = "BeaconQService";
    private BeaconManager beaconManager;

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onCreate() {
        // Configure BeaconManager.
        Log.d(TAG, "Beacons monitoring service created");
        //Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Beacons monitoring service destroyed");
        Toast.makeText(this, "Beacons monitoring service done", Toast.LENGTH_SHORT).show();
        Notification noti = new Notification.Builder(BeaconsMonitoringService.this)
                .setContentTitle("Stopped")
                .setContentText("See you!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        BeaconQApplication app = (BeaconQApplication)getApplication();
//        beaconManager = app.getInstance().getBeaconManager();
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override public void onServiceReady() {
//                try {
//                    Log.d(TAG, "serviceReady");
//                    beaconManager.startMonitoring(Configs.ALL_ESTIMOTE_BEACONS);
//                } catch (RemoteException e) {
//                    Log.e(TAG, "Cannot start ranging", e);
//                }
//            }
//        });
//
//        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
//            @Override public void onEnteredRegion(Region region, List<Beacon> beacons) {
//                Log.d(TAG, "entered");
//                Toast.makeText(BeaconsMonitoringService.this, "Entered", Toast.LENGTH_LONG).show();
//
//                Intent notificationIntent = new Intent(BeaconsMonitoringService.this, HomeActivity.class);
//                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                PendingIntent intent = PendingIntent.getActivity(BeaconsMonitoringService.this, 0,
//                        notificationIntent, 0);
//
//                Notification noti = new Notification.Builder(BeaconsMonitoringService.this)
//                        .setContentTitle("Entered")
//                        .setContentText("You're home!")
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentIntent(intent)
//                        .build();
//
//
//                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                mNotificationManager.cancel(2);
//                mNotificationManager.notify(1, noti);
//            }
//            @Override public void onExitedRegion(Region region) {
//                Log.d(TAG, "exited");
//                Toast.makeText(BeaconsMonitoringService.this, "Exited", Toast.LENGTH_LONG).show();
//                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                mNotificationManager.cancel(1);
//
//                Notification noti = new Notification.Builder(BeaconsMonitoringService.this)
//                        .setContentTitle("Exited")
//                        .setContentText("See you!")
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .build();
//
//                mNotificationManager.notify(2, noti);
//            }
//        });
//
//        Log.d(TAG, "Beacons monitoring service starting");
//        Toast.makeText(this, "Beacons monitoring service starting", Toast.LENGTH_SHORT).show();
//
//        Notification noti = new Notification.Builder(this)
//                .setContentTitle("Started")
//                .setContentText("Here we go")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .build();
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancel(2);
//        mNotificationManager.notify(1, noti);
//
//
//        // If we get killed, after returning from here, restart
        return START_STICKY;
    }
}
