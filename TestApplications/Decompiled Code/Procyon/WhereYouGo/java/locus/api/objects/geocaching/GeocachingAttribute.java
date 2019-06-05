// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.geocaching;

import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import java.util.Hashtable;
import locus.api.objects.Storable;

public class GeocachingAttribute extends Storable
{
    private static final Hashtable<String, Integer> mAttrIds;
    private int mId;
    
    static {
        (mAttrIds = new Hashtable<String, Integer>()).put("dogs", 1);
        GeocachingAttribute.mAttrIds.put("fee", 2);
        GeocachingAttribute.mAttrIds.put("rappelling", 3);
        GeocachingAttribute.mAttrIds.put("boat", 4);
        GeocachingAttribute.mAttrIds.put("scuba", 5);
        GeocachingAttribute.mAttrIds.put("kids", 6);
        GeocachingAttribute.mAttrIds.put("onehour", 7);
        GeocachingAttribute.mAttrIds.put("scenic", 8);
        GeocachingAttribute.mAttrIds.put("hiking", 9);
        GeocachingAttribute.mAttrIds.put("climbing", 10);
        GeocachingAttribute.mAttrIds.put("wading", 11);
        GeocachingAttribute.mAttrIds.put("swimming", 12);
        GeocachingAttribute.mAttrIds.put("available", 13);
        GeocachingAttribute.mAttrIds.put("night", 14);
        GeocachingAttribute.mAttrIds.put("winter", 15);
        GeocachingAttribute.mAttrIds.put("camping", 16);
        GeocachingAttribute.mAttrIds.put("poisonoak", 17);
        GeocachingAttribute.mAttrIds.put("snakes", 18);
        GeocachingAttribute.mAttrIds.put("ticks", 19);
        GeocachingAttribute.mAttrIds.put("mine", 20);
        GeocachingAttribute.mAttrIds.put("cliff", 21);
        GeocachingAttribute.mAttrIds.put("hunting", 22);
        GeocachingAttribute.mAttrIds.put("danger", 23);
        GeocachingAttribute.mAttrIds.put("wheelchair", 24);
        GeocachingAttribute.mAttrIds.put("parking", 25);
        GeocachingAttribute.mAttrIds.put("public", 26);
        GeocachingAttribute.mAttrIds.put("water", 27);
        GeocachingAttribute.mAttrIds.put("restrooms", 28);
        GeocachingAttribute.mAttrIds.put("phone", 29);
        GeocachingAttribute.mAttrIds.put("picnic", 30);
        GeocachingAttribute.mAttrIds.put("camping", 31);
        GeocachingAttribute.mAttrIds.put("bicycles", 32);
        GeocachingAttribute.mAttrIds.put("motorcycles", 33);
        GeocachingAttribute.mAttrIds.put("quads", 34);
        GeocachingAttribute.mAttrIds.put("jeeps", 35);
        GeocachingAttribute.mAttrIds.put("snowmobiles", 36);
        GeocachingAttribute.mAttrIds.put("horses", 37);
        GeocachingAttribute.mAttrIds.put("campfires", 38);
        GeocachingAttribute.mAttrIds.put("thorn", 39);
        GeocachingAttribute.mAttrIds.put("stealth", 40);
        GeocachingAttribute.mAttrIds.put("stroller", 41);
        GeocachingAttribute.mAttrIds.put("firstaid", 42);
        GeocachingAttribute.mAttrIds.put("cow", 43);
        GeocachingAttribute.mAttrIds.put("flashlight", 44);
        GeocachingAttribute.mAttrIds.put("landf", 45);
        GeocachingAttribute.mAttrIds.put("rv", 46);
        GeocachingAttribute.mAttrIds.put("field_puzzle", 47);
        GeocachingAttribute.mAttrIds.put("UV", 48);
        GeocachingAttribute.mAttrIds.put("snowshoes", 49);
        GeocachingAttribute.mAttrIds.put("skiis", 50);
        GeocachingAttribute.mAttrIds.put("s-tool", 51);
        GeocachingAttribute.mAttrIds.put("nightcache", 52);
        GeocachingAttribute.mAttrIds.put("parkngrab", 53);
        GeocachingAttribute.mAttrIds.put("AbandonedBuilding", 54);
        GeocachingAttribute.mAttrIds.put("hike_short", 55);
        GeocachingAttribute.mAttrIds.put("hike_med", 56);
        GeocachingAttribute.mAttrIds.put("hike_long", 57);
        GeocachingAttribute.mAttrIds.put("fuel", 58);
        GeocachingAttribute.mAttrIds.put("food", 59);
        GeocachingAttribute.mAttrIds.put("wirelessbeacon", 60);
        GeocachingAttribute.mAttrIds.put("partnership", 61);
        GeocachingAttribute.mAttrIds.put("seasonal", 62);
        GeocachingAttribute.mAttrIds.put("touristOK", 63);
        GeocachingAttribute.mAttrIds.put("treeclimbing", 64);
        GeocachingAttribute.mAttrIds.put("frontyard", 65);
        GeocachingAttribute.mAttrIds.put("teamwork", 66);
    }
    
    public GeocachingAttribute() {
    }
    
    public GeocachingAttribute(final int mId, final boolean b) {
        if (!b) {
            this.mId = mId;
        }
        else {
            this.mId = mId + 100;
        }
    }
    
    public GeocachingAttribute(final String s) {
        if (s != null && s.length() > 0) {
            this.mId = GeocachingAttribute.mAttrIds.get(s.substring(s.lastIndexOf("/1"), s.lastIndexOf("-")));
            if (s.contains("-yes.")) {
                this.mId += 100;
            }
        }
    }
    
    public int getId() {
        return this.mId;
    }
    
    public int getIdReal() {
        return this.mId % 100;
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    public boolean isPositive() {
        return this.mId > 100;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mId = dataReaderBigEndian.readInt();
    }
    
    @Override
    public void reset() {
        this.mId = -1;
    }
    
    public void setId(final int mId) {
        this.mId = mId;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeInt(this.mId);
    }
}
