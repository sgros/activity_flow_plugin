package locus.api.objects.extra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.SparseArrayCompat;
import locus.api.utils.Utils;

public class ExtraData extends Storable {
   public static final int PAR_ADDRESS_CITY = 51;
   public static final int PAR_ADDRESS_COUNTRY = 54;
   public static final int PAR_ADDRESS_POST_CODE = 53;
   public static final int PAR_ADDRESS_REGION = 52;
   public static final int PAR_ADDRESS_STREET = 50;
   public static final int PAR_AREA_SIZE = 12;
   private static final int PAR_AUDIO_MAX = 1599;
   private static final int PAR_AUDIO_MIN = 1500;
   public static final int PAR_COMMENT = 31;
   public static final int PAR_DB_POI_EXTRA_DATA = 13;
   public static final int PAR_DESCRIPTION = 30;
   private static final int PAR_EMAIL_MAX = 1199;
   private static final int PAR_EMAIL_MIN = 1100;
   public static final int PAR_GEOCACHE_CODE = 34;
   public static final int PAR_GOOGLE_PLACES_DETAILS = 17;
   public static final int PAR_GOOGLE_PLACES_RATING = 16;
   public static final int PAR_GOOGLE_PLACES_REFERENCE = 15;
   public static final int PAR_INTENT_EXTRA_CALLBACK = 20;
   public static final int PAR_INTENT_EXTRA_ON_DISPLAY = 21;
   public static final int PAR_KML_TRIP_ID = 14;
   public static final int PAR_OSM_NOTES_CLOSED = 302;
   public static final int PAR_OSM_NOTES_ID = 301;
   private static final int PAR_OTHER_FILES_MAX = 1999;
   private static final int PAR_OTHER_FILES_MIN = 1800;
   private static final int PAR_PHONE_MAX = 1099;
   private static final int PAR_PHONE_MIN = 1000;
   private static final int PAR_PHOTO_MAX = 1399;
   private static final int PAR_PHOTO_MIN = 1300;
   public static final int PAR_RELATIVE_WORKING_DIR = 32;
   public static final int PAR_RTE_COMPUTE_TYPE = 120;
   public static final int PAR_RTE_DISTANCE_F = 101;
   public static final int PAR_RTE_INDEX = 100;
   public static final int PAR_RTE_POINT_ACTION = 110;
   public static final int PAR_RTE_SIMPLE_ROUNDABOUTS = 121;
   public static final int PAR_RTE_SPEED_F = 103;
   public static final int PAR_RTE_STREET = 109;
   public static final int PAR_RTE_TIME_I = 102;
   public static final int PAR_RTE_TURN_COST = 104;
   public static final int PAR_SOURCE = 0;
   public static final int PAR_STYLE_NAME = 5;
   public static final int PAR_TYPE = 33;
   private static final int PAR_URL_MAX = 1299;
   private static final int PAR_URL_MIN = 1200;
   private static final int PAR_VIDEO_MAX = 1499;
   private static final int PAR_VIDEO_MIN = 1400;
   public static final int[] RTE_TYPES_SORTED = new int[]{6, 0, 1, 7, 2, 4, 5, 8, 9, 3, 10, 11};
   public static final byte SOURCE_GEOCACHING_WAYPOINT = 50;
   public static final byte SOURCE_INVISIBLE = 56;
   public static final byte SOURCE_LIVE_TRACKING = 59;
   public static final byte SOURCE_MAP_TEMP = 51;
   public static final byte SOURCE_MUNZEE = 58;
   public static final byte SOURCE_OPENSTREETBUGS = 55;
   public static final byte SOURCE_PARKING_SERVICE = 49;
   public static final byte SOURCE_POI_OSM_DB = 57;
   public static final byte SOURCE_ROUTE_LOCATION = 53;
   public static final byte SOURCE_ROUTE_WAYPOINT = 52;
   public static final byte SOURCE_UNKNOWN = 48;
   private static final String TAG = "ExtraData";
   public static final int VALUE_RTE_ACTION_ARRIVE_DEST = 24;
   public static final int VALUE_RTE_ACTION_ARRIVE_DEST_LEFT = 25;
   public static final int VALUE_RTE_ACTION_ARRIVE_DEST_RIGHT = 26;
   public static final int VALUE_RTE_ACTION_CONTINUE_STRAIGHT = 1;
   public static final int VALUE_RTE_ACTION_ENTER_STATE = 23;
   public static final int VALUE_RTE_ACTION_EXIT_LEFT = 15;
   public static final int VALUE_RTE_ACTION_EXIT_RIGHT = 16;
   public static final int VALUE_RTE_ACTION_LEFT = 4;
   public static final int VALUE_RTE_ACTION_LEFT_SHARP = 5;
   public static final int VALUE_RTE_ACTION_LEFT_SLIGHT = 3;
   public static final int VALUE_RTE_ACTION_MERGE = 22;
   public static final int VALUE_RTE_ACTION_MERGE_LEFT = 20;
   public static final int VALUE_RTE_ACTION_MERGE_RIGHT = 21;
   public static final int VALUE_RTE_ACTION_NO_MANEUVER = 0;
   public static final int VALUE_RTE_ACTION_NO_MANEUVER_NAME_CHANGE = 2;
   public static final int VALUE_RTE_ACTION_PASS_PLACE = 50;
   public static final int VALUE_RTE_ACTION_RAMP_ON_LEFT = 17;
   public static final int VALUE_RTE_ACTION_RAMP_ON_RIGHT = 18;
   public static final int VALUE_RTE_ACTION_RAMP_STRAIGHT = 19;
   public static final int VALUE_RTE_ACTION_RIGHT = 7;
   public static final int VALUE_RTE_ACTION_RIGHT_SHARP = 8;
   public static final int VALUE_RTE_ACTION_RIGHT_SLIGHT = 6;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_1 = 27;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_2 = 28;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_3 = 29;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_4 = 30;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_5 = 31;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_6 = 32;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_7 = 33;
   public static final int VALUE_RTE_ACTION_ROUNDABOUT_EXIT_8 = 34;
   public static final int VALUE_RTE_ACTION_STAY_LEFT = 9;
   public static final int VALUE_RTE_ACTION_STAY_RIGHT = 10;
   public static final int VALUE_RTE_ACTION_STAY_STRAIGHT = 11;
   public static final int VALUE_RTE_ACTION_U_TURN = 12;
   public static final int VALUE_RTE_ACTION_U_TURN_LEFT = 13;
   public static final int VALUE_RTE_ACTION_U_TURN_RIGHT = 14;
   public static final int VALUE_RTE_TYPE_CAR = 6;
   public static final int VALUE_RTE_TYPE_CAR_FAST = 0;
   public static final int VALUE_RTE_TYPE_CAR_SHORT = 1;
   public static final int VALUE_RTE_TYPE_CYCLE = 2;
   public static final int VALUE_RTE_TYPE_CYCLE_FAST = 4;
   public static final int VALUE_RTE_TYPE_CYCLE_MTB = 8;
   public static final int VALUE_RTE_TYPE_CYCLE_RACING = 9;
   public static final int VALUE_RTE_TYPE_CYCLE_SHORT = 5;
   public static final int VALUE_RTE_TYPE_FOOT_01 = 3;
   public static final int VALUE_RTE_TYPE_FOOT_02 = 10;
   public static final int VALUE_RTE_TYPE_FOOT_03 = 11;
   public static final int VALUE_RTE_TYPE_GENERATED = -1;
   public static final int VALUE_RTE_TYPE_MOTORCYCLE = 7;
   SparseArrayCompat parameters;

   public ExtraData() {
   }

   public ExtraData(byte[] var1) throws IOException {
      super(var1);
   }

   private boolean addToStorage(String var1, String var2, int var3, int var4) {
      boolean var5 = false;
      boolean var6 = var5;
      if (var2 != null) {
         if (var2.length() == 0) {
            var6 = var5;
         } else {
            if (var1 != null && var1.length() > 0) {
               var1 = var1 + "|" + var2;
            } else {
               var1 = var2;
            }

            while(true) {
               var6 = var5;
               if (var3 > var4) {
                  break;
               }

               var2 = this.getParameter(var3);
               if (var2 == null) {
                  this.parameters.put(var3, Utils.doStringToBytes(var1));
                  var6 = true;
                  break;
               }

               var6 = var5;
               if (var2.equalsIgnoreCase(var1)) {
                  break;
               }

               ++var3;
            }
         }
      }

      return var6;
   }

   private List convertToTexts(List var1) {
      ArrayList var2 = new ArrayList();
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         var2.add(((ExtraData.LabelTextContainer)var1.get(var3)).text);
      }

      return var2;
   }

   public static String generateCallbackString(String var0, String var1, String var2, String var3, String var4) {
      if (var1 != null && var1.length() != 0 && var2 != null && var2.length() != 0) {
         String var5 = var0;
         if (var0 == null) {
            var5 = "";
         }

         var0 = var3;
         if (var3 == null) {
            var0 = "";
         }

         var3 = var4;
         if (var4 == null) {
            var3 = "";
         }

         var0 = var5 + ";" + var1 + ";" + var2 + ";" + var0 + ";" + var3 + ";";
      } else {
         Logger.logD("ExtraData", "generateCallbackString(" + var0 + ", " + var1 + ", " + var2 + ", " + var3 + ", " + var4 + "), " + "invalid packageName or className parameter");
         var0 = "";
      }

      return var0;
   }

   private List getFromStorage(int var1, int var2) {
      ArrayList var3;
      for(var3 = new ArrayList(); var1 <= var2; ++var1) {
         String var4 = this.getParameter(var1);
         if (var4 != null && var4.length() != 0) {
            var3.add(new ExtraData.LabelTextContainer(var4));
         }
      }

      return var3;
   }

   private void removeAllFromStorage(int var1, int var2) {
      while(var1 <= var2) {
         this.parameters.remove(var1);
         ++var1;
      }

   }

   private boolean removeFromStorage(String var1, int var2, int var3) {
      boolean var4 = false;
      boolean var5 = var4;
      if (var1 != null) {
         if (var1.length() == 0) {
            var5 = var4;
         } else {
            while(true) {
               var5 = var4;
               if (var2 > var3) {
                  break;
               }

               String var6 = this.getParameter(var2);
               if (var6 != null && var6.endsWith(var1)) {
                  this.parameters.remove(var2);
                  var5 = true;
                  break;
               }

               ++var2;
            }
         }
      }

      return var5;
   }

   public boolean addAudio(String var1) {
      return this.addToStorage("", var1, 1500, 1599);
   }

   public boolean addEmail(String var1) {
      return this.addToStorage("", var1, 1100, 1199);
   }

   public boolean addEmail(String var1, String var2) {
      return this.addToStorage(var1, var2, 1100, 1199);
   }

   public boolean addOtherFile(String var1) {
      return this.addToStorage("", var1, 1800, 1999);
   }

   public boolean addParameter(int var1, byte var2) {
      return this.addParameter(var1, new byte[]{var2});
   }

   public boolean addParameter(int var1, String var2) {
      boolean var3 = false;
      boolean var4;
      if (var2 == null) {
         var4 = var3;
      } else {
         this.removeParameter(var1);
         var2 = var2.trim();
         var4 = var3;
         if (var2.length() != 0) {
            if (var1 > 1000 && var1 < 2000) {
               Logger.logW("ExtraData", "addParam(" + var1 + ", " + var2 + "), " + "values 1000 - 1999 reserved!");
               var4 = var3;
            } else {
               this.parameters.put(var1, Utils.doStringToBytes(var2));
               var4 = true;
            }
         }
      }

      return var4;
   }

   public boolean addParameter(int var1, byte[] var2) {
      boolean var3 = false;
      this.removeParameter(var1);
      boolean var4 = var3;
      if (var2 != null) {
         if (var2.length == 0) {
            var4 = var3;
         } else if (var1 > 1000 && var1 < 2000) {
            Logger.logW("ExtraData", "addParam(" + var1 + ", " + Arrays.toString(var2) + "), " + "values 1000 - 1999 reserved!");
            var4 = var3;
         } else {
            this.parameters.put(var1, var2);
            var4 = true;
         }
      }

      return var4;
   }

   public boolean addPhone(String var1) {
      return this.addToStorage("", var1, 1000, 1099);
   }

   public boolean addPhone(String var1, String var2) {
      return this.addToStorage(var1, var2, 1000, 1099);
   }

   public boolean addPhoto(String var1) {
      return this.addToStorage("", var1, 1300, 1399);
   }

   public boolean addUrl(String var1) {
      return this.addToStorage("", var1, 1200, 1299);
   }

   public boolean addUrl(String var1, String var2) {
      return this.addToStorage(var1, var2, 1200, 1299);
   }

   public boolean addVideo(String var1) {
      return this.addToStorage("", var1, 1400, 1499);
   }

   public List getAllAttachments() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.getPhotos());
      var1.addAll(this.getAudios());
      var1.addAll(this.getVideos());
      var1.addAll(this.getOtherFiles());
      return var1;
   }

   public int getAllAttachmentsCount() {
      return this.getAllAttachments().size();
   }

   public List getAudios() {
      return this.convertToTexts(this.getFromStorage(1500, 1599));
   }

   public int getCount() {
      return this.parameters.size();
   }

   public List getEmails() {
      return this.getFromStorage(1100, 1199);
   }

   public List getOtherFiles() {
      return this.convertToTexts(this.getFromStorage(1800, 1999));
   }

   public String getParameter(int var1) {
      byte[] var2 = (byte[])this.parameters.get(var1);
      String var3;
      if (var2 != null) {
         var3 = Utils.doBytesToString(var2);
      } else {
         var3 = null;
      }

      return var3;
   }

   public String getParameterNotNull(int var1) {
      String var2 = this.getParameter(var1);
      String var3 = var2;
      if (var2 == null) {
         var3 = "";
      }

      return var3;
   }

   public byte[] getParameterRaw(int var1) {
      return (byte[])this.parameters.get(var1);
   }

   public List getPhones() {
      return this.getFromStorage(1000, 1099);
   }

   public List getPhotos() {
      return this.convertToTexts(this.getFromStorage(1300, 1399));
   }

   public List getUrls() {
      return this.getFromStorage(1200, 1299);
   }

   protected int getVersion() {
      return 0;
   }

   public List getVideos() {
      return this.convertToTexts(this.getFromStorage(1400, 1499));
   }

   public boolean hasParameter(int var1) {
      boolean var2;
      if (this.parameters.get(var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      int var3 = var2.readInt();
      this.parameters.clear();

      for(var1 = 0; var1 < var3; ++var1) {
         int var4 = var2.readInt();
         this.parameters.put(var4, var2.readBytes(var2.readInt()));
      }

   }

   public void removeAllEmails() {
      this.removeAllFromStorage(1100, 1199);
   }

   public void removeAllPhones() {
      this.removeAllFromStorage(1000, 1099);
   }

   public void removeAllUrls() {
      this.removeAllFromStorage(1200, 1299);
   }

   public boolean removeAudio(String var1) {
      return this.removeFromStorage(var1, 1500, 1599);
   }

   public boolean removeEmail(String var1) {
      return this.removeFromStorage(var1, 1100, 1199);
   }

   public boolean removeOtherFile(String var1) {
      return this.removeFromStorage(var1, 1800, 1999);
   }

   public String removeParameter(int var1) {
      String var2 = this.getParameter(var1);
      this.parameters.remove(var1);
      return var2;
   }

   public boolean removePhone(String var1) {
      return this.removeFromStorage(var1, 1000, 1099);
   }

   public boolean removePhoto(String var1) {
      return this.removeFromStorage(var1, 1300, 1399);
   }

   public boolean removeUrl(String var1) {
      return this.removeFromStorage(var1, 1200, 1299);
   }

   public boolean removeVideo(String var1) {
      return this.removeFromStorage(var1, 1400, 1499);
   }

   public void reset() {
      this.parameters = new SparseArrayCompat();
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeInt(this.parameters.size());
      int var2 = 0;

      for(int var3 = this.parameters.size(); var2 < var3; ++var2) {
         var1.writeInt(this.parameters.keyAt(var2));
         byte[] var4 = (byte[])this.parameters.valueAt(var2);
         var1.writeInt(var4.length);
         if (var4.length > 0) {
            var1.write(var4);
         }
      }

   }

   public static class LabelTextContainer {
      public final String label;
      public final String text;

      public LabelTextContainer(String var1) {
         if (var1.contains("|")) {
            int var2 = var1.indexOf("|");
            this.label = var1.substring(0, var2);
            this.text = var1.substring(var2 + 1);
         } else {
            this.label = "";
            this.text = var1;
         }

      }

      public LabelTextContainer(String var1, String var2) {
         if (var1 == null) {
            this.label = "";
         } else {
            this.label = var1;
         }

         this.text = var2;
      }

      public String getAsText() {
         String var1;
         if (this.label.length() > 0) {
            var1 = this.label + "|" + this.text;
         } else {
            var1 = this.text;
         }

         return var1;
      }

      public String getFormattedAsEmail() {
         String var1;
         if (this.label.length() == 0) {
            var1 = this.text;
         } else {
            var1 = this.label;
         }

         return "<a href=\"mailto:" + this.text + "\">" + var1 + "</a>";
      }

      public String getFormattedAsPhone() {
         String var1;
         if (this.label.length() == 0) {
            var1 = this.text;
         } else {
            var1 = this.label;
         }

         return "<a href=\"tel:" + this.text + "\">" + var1 + "</a>";
      }

      public String getFormattedAsUrl(boolean var1) {
         String var2;
         if (this.label.length() == 0) {
            var2 = this.text;
         } else {
            var2 = this.label;
         }

         String var3 = this.text;
         String var4 = var3;
         if (var1) {
            var4 = var3;
            if (!var3.contains("://")) {
               var4 = "http://" + var3;
            }
         }

         return "<a href=\"" + var4 + "\" target=\"_blank\">" + var2 + "</a>";
      }
   }
}
