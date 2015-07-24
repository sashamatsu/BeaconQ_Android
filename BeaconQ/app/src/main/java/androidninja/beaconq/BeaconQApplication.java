package androidninja.beaconq;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.parse.Parse;

import java.util.Date;


/**
 * Created by alexmatsukevich on 7/22/15.
 */
public class BeaconQApplication extends Application {

    private static final String TAG = "BeaconQApplication";
    private static BeaconQApplication singleton;
    private BeaconManager beaconManager = null;
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
        startService(new Intent(getApplicationContext(), BeaconsMonitoringService.class));


        //Estimote


        retrieveBeacons();
    }

    public BeaconQApplication getInstance(){
        return singleton;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public BeaconManager getBeaconManager() {
        if (beaconManager == null) {
            beaconManager = new BeaconManager(this);
        }
        return beaconManager;
    }

    public void setBeaconManager(BeaconManager beaconManager) {
        this.beaconManager = beaconManager;
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
            String major=String.valueOf(beacon.getMajor());
            String minor=String.valueOf(beacon.getMinor());

            String validMajor=curMetadataBeacon.getMajor();
            String validMinor=curMetadataBeacon.getMinor();

            if(validMajor.equals(major)){
                if(validMinor.equals(minor)) {
                    return curMetadataBeacon;
                }
            }//end if match
        }

        return  null;
    }
}
