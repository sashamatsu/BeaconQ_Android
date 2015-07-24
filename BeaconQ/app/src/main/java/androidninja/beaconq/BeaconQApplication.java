package androidninja.beaconq;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.parse.Parse;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Date;


/**
 * Created by alexmatsukevich on 7/22/15.
 */
public class BeaconQApplication extends Application implements BootstrapNotifier {

    private static final String TAG = "BeaconQApplication";
    private static BeaconQApplication singleton;
    private RegionBootstrap regionBootstrap;
    public BeaconMetaData[] validBeacons=new BeaconMetaData[0];
    public Date orderStartTime;
    public Date orderEndTime;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;
        //Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "QRR1SBCTaWQ6alB3PqBEpQQkm9fkvWJExrslsFae", "6SpSDNHexYcQIQAXzTbCh77RBXWfapdPTfm6YZPX");

        //Beacons
        Region region = new Region("androidninja.beaconq",
                Identifier.parse(Configs.REGION_ID), null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);


        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        retrieveBeacons();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void didEnterRegion(Region arg0) {
        Log.d(TAG, "did enter region.");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Important:  make sure to add android:launchMode="singleInstance" in the manifest
        // to keep multiple copies of this activity from getting created if the user has
        // already manually launched the app.


        this.startActivity(intent);
    }


    @Override
    public void didDetermineStateForRegion(int arg0, Region arg1) {
        // Don't care
    }


    @Override
    public void didExitRegion(Region arg0) {
        // Don't care
    }

    public BeaconQApplication getInstance(){
        return singleton;
    }

    private void retrieveBeacons(){
        BeaconMetaData beacon1=new BeaconMetaData("b9407f30-f5f8-466e-aff9-25556b57fe6d","36539","2597",2);
        BeaconMetaData beacon2=new BeaconMetaData("b9407f30-f5f8-466e-aff9-25556b57fe6d","25313","31136",1);
        validBeacons=new BeaconMetaData[2];
        validBeacons[0]=beacon1;
        validBeacons[1]=beacon2;
    }

    public BeaconMetaData findValidBeacon(Beacon beacon){

        for(int i=0;i<this.validBeacons.length;i++){
            BeaconMetaData curMetadataBeacon=this.validBeacons[i];
            String major=beacon.getId2().toString();
            String minor=beacon.getId3().toString();

            String validMajor=curMetadataBeacon.getMajor();
            String validMinor=curMetadataBeacon.getMinor();

            if(validMajor.equals(major)){
                return curMetadataBeacon;
//                if(validMinor.equals(minor)) {
//                    return curMetadataBeacon;
//                }
            }//end if match
        }

        return  null;
    }
}
