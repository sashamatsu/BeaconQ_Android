package androidninja.beaconq;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


public class HomeActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = HomeActivity.class.getSimpleName();
    private BeaconManager beaconManager;
    private TextView tbxTotalTime;
    private TextView tbxStartTime;
    private TextView tbxEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupBeaconManager();
        tbxTotalTime=(TextView)findViewById(R.id.orderTotalTime);
        tbxStartTime=(TextView)findViewById(R.id.orderStartTime);
        tbxEndTime=(TextView)findViewById(R.id.orderEndTime);
    }


    private void setupBeaconManager(){
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Iterator iter=beacons.iterator();
                 while(iter.hasNext()) {
                     Beacon curBeacon=(Beacon)iter.next();
                     if (curBeacon.getDistance() <= Configs.rangeMetersLimit) {
                         final BeaconQApplication app = (BeaconQApplication) getApplicationContext();
                         final BeaconMetaData  validBeacon = app.getInstance().findValidBeacon(curBeacon);
                         if (validBeacon != null) {

                             runOnUiThread(new Runnable() {
                                 public void run() {
                                     if (validBeacon.getSequenceNumber() == 1) {
                                         app.orderStartTime = new Date();
                                         tbxStartTime.setText(app.orderStartTime.toString());
                                     } else if (validBeacon.getSequenceNumber() == 2) {
                                         app.orderEndTime = new Date();
                                         Date startDate = app.orderStartTime;
                                         Date endDate = app.orderEndTime;
                                         tbxEndTime.setText(endDate.toString());
                                         if (startDate != null && endDate != null) {
                                             long difference = endDate.getTime() - startDate.getTime();
                                             String total = String.valueOf(difference / 1000);
                                             tbxTotalTime.setText(total);
                                         }//end valid date times
                                     }
                                 }
                             });//end ui thread
                         }
                     }//end if withing range limit
                 }//end while
                    Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                }
            }
        });

        try {
            beaconManager.setBackgroundScanPeriod(1000);
            beaconManager.setForegroundScanPeriod(1000);
            beaconManager.startRangingBeaconsInRegion(new Region("androidninja.beaconq", Identifier.parse(Configs.REGION_ID), null, null));
        } catch (RemoteException e) {    }
    }
}
