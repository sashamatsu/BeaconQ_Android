package androidninja.beaconq;

/**
 * Created by alexmatsukevich on 7/22/15.
 */
public class BeaconMetaData {

    private String major="0";
    private String minor="0";
    private int sequenceNumber=0;
    private String regionID="";

    public BeaconMetaData(String regionID,String major,String minor,int sequenceNumber){
        this.regionID=regionID;
        this.major=major;
        this.minor=minor;
        this.sequenceNumber=sequenceNumber;
    }

    public String getMajor() {
        return this.major;
    }

    public String getMinor() {
        return this.minor;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public String getRegionID() {
        return this.regionID;
    }

}
