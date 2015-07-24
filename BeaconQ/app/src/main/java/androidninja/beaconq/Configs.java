package androidninja.beaconq;

import com.estimote.sdk.Region;

/**
 * Created by alexmatsukevich on 7/22/15.
 */
public class Configs {

    public static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final Region ALL_ESTIMOTE_BEACONS = new Region("androidninja.beaconq", ESTIMOTE_PROXIMITY_UUID, null, null);
    public static final double rangeMetersLimit=1.0;
}
