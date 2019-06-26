package locus.api.objects.geocaching;

import java.io.IOException;
import java.util.Hashtable;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class GeocachingAttribute extends Storable {
    private static final Hashtable<String, Integer> mAttrIds = new Hashtable();
    private int mId;

    public GeocachingAttribute(int id, boolean positive) {
        if (positive) {
            this.mId = id + 100;
        } else {
            this.mId = id;
        }
    }

    public GeocachingAttribute(String url) {
        if (url != null && url.length() > 0) {
            this.mId = ((Integer) mAttrIds.get(url.substring(url.lastIndexOf("/1"), url.lastIndexOf("-")))).intValue();
            if (url.contains("-yes.")) {
                this.mId += 100;
            }
        }
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getIdReal() {
        return this.mId % 100;
    }

    public boolean isPositive() {
        return this.mId > 100;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mId = dr.readInt();
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeInt(this.mId);
    }

    public void reset() {
        this.mId = -1;
    }

    static {
        mAttrIds.put("dogs", Integer.valueOf(1));
        mAttrIds.put("fee", Integer.valueOf(2));
        mAttrIds.put("rappelling", Integer.valueOf(3));
        mAttrIds.put("boat", Integer.valueOf(4));
        mAttrIds.put("scuba", Integer.valueOf(5));
        mAttrIds.put("kids", Integer.valueOf(6));
        mAttrIds.put("onehour", Integer.valueOf(7));
        mAttrIds.put("scenic", Integer.valueOf(8));
        mAttrIds.put("hiking", Integer.valueOf(9));
        mAttrIds.put("climbing", Integer.valueOf(10));
        mAttrIds.put("wading", Integer.valueOf(11));
        mAttrIds.put("swimming", Integer.valueOf(12));
        mAttrIds.put("available", Integer.valueOf(13));
        mAttrIds.put("night", Integer.valueOf(14));
        mAttrIds.put("winter", Integer.valueOf(15));
        mAttrIds.put("camping", Integer.valueOf(16));
        mAttrIds.put("poisonoak", Integer.valueOf(17));
        mAttrIds.put("snakes", Integer.valueOf(18));
        mAttrIds.put("ticks", Integer.valueOf(19));
        mAttrIds.put("mine", Integer.valueOf(20));
        mAttrIds.put("cliff", Integer.valueOf(21));
        mAttrIds.put("hunting", Integer.valueOf(22));
        mAttrIds.put("danger", Integer.valueOf(23));
        mAttrIds.put("wheelchair", Integer.valueOf(24));
        mAttrIds.put("parking", Integer.valueOf(25));
        mAttrIds.put("public", Integer.valueOf(26));
        mAttrIds.put("water", Integer.valueOf(27));
        mAttrIds.put("restrooms", Integer.valueOf(28));
        mAttrIds.put("phone", Integer.valueOf(29));
        mAttrIds.put("picnic", Integer.valueOf(30));
        mAttrIds.put("camping", Integer.valueOf(31));
        mAttrIds.put("bicycles", Integer.valueOf(32));
        mAttrIds.put("motorcycles", Integer.valueOf(33));
        mAttrIds.put("quads", Integer.valueOf(34));
        mAttrIds.put("jeeps", Integer.valueOf(35));
        mAttrIds.put("snowmobiles", Integer.valueOf(36));
        mAttrIds.put("horses", Integer.valueOf(37));
        mAttrIds.put("campfires", Integer.valueOf(38));
        mAttrIds.put("thorn", Integer.valueOf(39));
        mAttrIds.put("stealth", Integer.valueOf(40));
        mAttrIds.put("stroller", Integer.valueOf(41));
        mAttrIds.put("firstaid", Integer.valueOf(42));
        mAttrIds.put("cow", Integer.valueOf(43));
        mAttrIds.put("flashlight", Integer.valueOf(44));
        mAttrIds.put("landf", Integer.valueOf(45));
        mAttrIds.put("rv", Integer.valueOf(46));
        mAttrIds.put("field_puzzle", Integer.valueOf(47));
        mAttrIds.put("UV", Integer.valueOf(48));
        mAttrIds.put("snowshoes", Integer.valueOf(49));
        mAttrIds.put("skiis", Integer.valueOf(50));
        mAttrIds.put("s-tool", Integer.valueOf(51));
        mAttrIds.put("nightcache", Integer.valueOf(52));
        mAttrIds.put("parkngrab", Integer.valueOf(53));
        mAttrIds.put("AbandonedBuilding", Integer.valueOf(54));
        mAttrIds.put("hike_short", Integer.valueOf(55));
        mAttrIds.put("hike_med", Integer.valueOf(56));
        mAttrIds.put("hike_long", Integer.valueOf(57));
        mAttrIds.put("fuel", Integer.valueOf(58));
        mAttrIds.put("food", Integer.valueOf(59));
        mAttrIds.put("wirelessbeacon", Integer.valueOf(60));
        mAttrIds.put("partnership", Integer.valueOf(61));
        mAttrIds.put("seasonal", Integer.valueOf(62));
        mAttrIds.put("touristOK", Integer.valueOf(63));
        mAttrIds.put("treeclimbing", Integer.valueOf(64));
        mAttrIds.put("frontyard", Integer.valueOf(65));
        mAttrIds.put("teamwork", Integer.valueOf(66));
    }
}
