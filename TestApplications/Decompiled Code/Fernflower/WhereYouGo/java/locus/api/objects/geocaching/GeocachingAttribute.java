package locus.api.objects.geocaching;

import java.io.IOException;
import java.util.Hashtable;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class GeocachingAttribute extends Storable {
   private static final Hashtable mAttrIds = new Hashtable();
   private int mId;

   static {
      mAttrIds.put("dogs", 1);
      mAttrIds.put("fee", 2);
      mAttrIds.put("rappelling", 3);
      mAttrIds.put("boat", 4);
      mAttrIds.put("scuba", 5);
      mAttrIds.put("kids", 6);
      mAttrIds.put("onehour", 7);
      mAttrIds.put("scenic", 8);
      mAttrIds.put("hiking", 9);
      mAttrIds.put("climbing", 10);
      mAttrIds.put("wading", 11);
      mAttrIds.put("swimming", 12);
      mAttrIds.put("available", 13);
      mAttrIds.put("night", 14);
      mAttrIds.put("winter", 15);
      mAttrIds.put("camping", 16);
      mAttrIds.put("poisonoak", 17);
      mAttrIds.put("snakes", 18);
      mAttrIds.put("ticks", 19);
      mAttrIds.put("mine", 20);
      mAttrIds.put("cliff", 21);
      mAttrIds.put("hunting", 22);
      mAttrIds.put("danger", 23);
      mAttrIds.put("wheelchair", 24);
      mAttrIds.put("parking", 25);
      mAttrIds.put("public", 26);
      mAttrIds.put("water", 27);
      mAttrIds.put("restrooms", 28);
      mAttrIds.put("phone", 29);
      mAttrIds.put("picnic", 30);
      mAttrIds.put("camping", 31);
      mAttrIds.put("bicycles", 32);
      mAttrIds.put("motorcycles", 33);
      mAttrIds.put("quads", 34);
      mAttrIds.put("jeeps", 35);
      mAttrIds.put("snowmobiles", 36);
      mAttrIds.put("horses", 37);
      mAttrIds.put("campfires", 38);
      mAttrIds.put("thorn", 39);
      mAttrIds.put("stealth", 40);
      mAttrIds.put("stroller", 41);
      mAttrIds.put("firstaid", 42);
      mAttrIds.put("cow", 43);
      mAttrIds.put("flashlight", 44);
      mAttrIds.put("landf", 45);
      mAttrIds.put("rv", 46);
      mAttrIds.put("field_puzzle", 47);
      mAttrIds.put("UV", 48);
      mAttrIds.put("snowshoes", 49);
      mAttrIds.put("skiis", 50);
      mAttrIds.put("s-tool", 51);
      mAttrIds.put("nightcache", 52);
      mAttrIds.put("parkngrab", 53);
      mAttrIds.put("AbandonedBuilding", 54);
      mAttrIds.put("hike_short", 55);
      mAttrIds.put("hike_med", 56);
      mAttrIds.put("hike_long", 57);
      mAttrIds.put("fuel", 58);
      mAttrIds.put("food", 59);
      mAttrIds.put("wirelessbeacon", 60);
      mAttrIds.put("partnership", 61);
      mAttrIds.put("seasonal", 62);
      mAttrIds.put("touristOK", 63);
      mAttrIds.put("treeclimbing", 64);
      mAttrIds.put("frontyard", 65);
      mAttrIds.put("teamwork", 66);
   }

   public GeocachingAttribute() {
   }

   public GeocachingAttribute(int var1, boolean var2) {
      if (!var2) {
         this.mId = var1;
      } else {
         this.mId = var1 + 100;
      }

   }

   public GeocachingAttribute(String var1) {
      if (var1 != null && var1.length() > 0) {
         String var2 = var1.substring(var1.lastIndexOf("/1"), var1.lastIndexOf("-"));
         this.mId = (Integer)mAttrIds.get(var2);
         if (var1.contains("-yes.")) {
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

   protected int getVersion() {
      return 0;
   }

   public boolean isPositive() {
      boolean var1;
      if (this.mId > 100) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mId = var2.readInt();
   }

   public void reset() {
      this.mId = -1;
   }

   public void setId(int var1) {
      this.mId = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeInt(this.mId);
   }
}
