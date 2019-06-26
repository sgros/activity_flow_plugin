package locus.api.objects.geocaching;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.Utils;

public class GeocachingData extends Storable {
   public static final int CACHE_SIZE_HUGE = 5;
   public static final int CACHE_SIZE_LARGE = 4;
   public static final int CACHE_SIZE_MICRO = 1;
   public static final int CACHE_SIZE_NOT_CHOSEN = 0;
   public static final int CACHE_SIZE_OTHER = 6;
   public static final int CACHE_SIZE_REGULAR = 3;
   public static final int CACHE_SIZE_SMALL = 2;
   public static final int CACHE_SOURCE_GEOCACHING_COM = 1;
   public static final int CACHE_SOURCE_GEOCACHING_HU = 2;
   public static final int CACHE_SOURCE_OPENCACHING = 100;
   public static final int CACHE_SOURCE_OPENCACHING_CZ = 110;
   public static final int CACHE_SOURCE_OPENCACHING_DE = 101;
   public static final int CACHE_SOURCE_OPENCACHING_ES = 102;
   public static final int CACHE_SOURCE_OPENCACHING_FR = 103;
   public static final int CACHE_SOURCE_OPENCACHING_IT = 104;
   public static final int CACHE_SOURCE_OPENCACHING_NL = 105;
   public static final int CACHE_SOURCE_OPENCACHING_PL = 106;
   public static final int CACHE_SOURCE_OPENCACHING_RO = 107;
   public static final int CACHE_SOURCE_OPENCACHING_UK = 108;
   public static final int CACHE_SOURCE_OPENCACHING_US = 109;
   public static final int CACHE_SOURCE_UNDEFINED = 0;
   public static final int CACHE_TYPE_BENCHMARK = 14;
   public static final int CACHE_TYPE_CACHE_IN_TRASH_OUT = 10;
   public static final int CACHE_TYPE_EARTH = 4;
   public static final int CACHE_TYPE_EVENT = 8;
   public static final int CACHE_TYPE_GIGA_EVENT = 20;
   public static final int CACHE_TYPE_GPS_ADVENTURE = 11;
   public static final int CACHE_TYPE_GROUNDSPEAK = 17;
   public static final int CACHE_TYPE_LAB_CACHE = 21;
   public static final int CACHE_TYPE_LETTERBOX = 6;
   public static final int CACHE_TYPE_LF_CELEBRATION = 19;
   public static final int CACHE_TYPE_LF_EVENT = 18;
   public static final int CACHE_TYPE_LOCATIONLESS = 13;
   public static final int CACHE_TYPE_MAZE_EXHIBIT = 15;
   public static final int CACHE_TYPE_MEGA_EVENT = 9;
   public static final int CACHE_TYPE_MULTI = 1;
   public static final int CACHE_TYPE_MYSTERY = 2;
   public static final int CACHE_TYPE_PROJECT_APE = 5;
   public static final int CACHE_TYPE_TRADITIONAL = 0;
   public static final int CACHE_TYPE_UNDEFINED = 100;
   public static final int CACHE_TYPE_VIRTUAL = 3;
   public static final int CACHE_TYPE_WAYMARK = 16;
   public static final int CACHE_TYPE_WEBCAM = 12;
   public static final int CACHE_TYPE_WHERIGO = 7;
   private static final String TAG = "GeocachingData";
   public List attributes;
   public List logs;
   private boolean mArchived;
   private boolean mAvailable;
   private String mCacheID;
   private String mCacheUrl;
   private boolean mComputed;
   private int mContainer;
   private String mCountry;
   private long mDateHidden;
   private long mDatePublished;
   private long mDateUpdated;
   private byte[] mDescBytes;
   private float mDifficulty;
   private String mEncodedHints;
   private int mFavoritePoints;
   private boolean mFound;
   private float mGcVoteAverage;
   private int mGcVoteNumOfVotes;
   private float mGcVoteUserVote;
   private long mId;
   private List mImages;
   private double mLatOriginal;
   private double mLonOriginal;
   private String mName;
   private String mNotes;
   private String mOwner;
   private String mPlacedBy;
   private boolean mPremiumOnly;
   private int mShortDescLength;
   private int mSource;
   private String mState;
   private float mTerrain;
   private int mType;
   public List trackables;
   public List waypoints;

   public GeocachingData() {
   }

   public GeocachingData(DataReaderBigEndian var1) throws IOException {
      super(var1);
   }

   public GeocachingData(byte[] var1) throws IOException {
      super(var1);
   }

   private String fixToHtml(String var1) {
      String var2;
      try {
         var2 = var1.replace("\n", "<br>").replace("  ", "&nbsp;&nbsp;");
      } catch (Exception var3) {
         return var1;
      }

      var1 = var2;
      return var1;
   }

   public static int getTypeAsInt(String var0) {
      byte var1 = 100;
      byte var2 = var1;
      if (var0 != null) {
         if (var0.length() == 0) {
            var2 = var1;
         } else {
            String var3 = var0;
            if (var0.startsWith("Geocache|")) {
               var3 = var0.substring("Geocache|".length());
            }

            if (var3.equalsIgnoreCase("Traditional Cache")) {
               var2 = 0;
            } else if (var3.equalsIgnoreCase("Multi-cache")) {
               var2 = 1;
            } else if (!var3.equalsIgnoreCase("Mystery Cache") && !var3.equalsIgnoreCase("Unknown Cache") && !var3.equalsIgnoreCase("Mystery/Puzzle Cache")) {
               if (!var3.equalsIgnoreCase("Project APE Cache") && !var3.equalsIgnoreCase("Project A.P.E. Cache")) {
                  if (!var3.equalsIgnoreCase("Letterbox Hybrid") && !var3.equalsIgnoreCase("Letterbox")) {
                     if (!var3.equalsIgnoreCase("Wherigo") && !var3.equalsIgnoreCase("Wherigo cache")) {
                        if (var3.equalsIgnoreCase("Event Cache")) {
                           var2 = 8;
                        } else if (var3.equalsIgnoreCase("Mega-Event Cache")) {
                           var2 = 9;
                        } else if (var3.equalsIgnoreCase("Cache In Trash Out Event")) {
                           var2 = 10;
                        } else if (var3.equalsIgnoreCase("EarthCache")) {
                           var2 = 4;
                        } else if (var3.toLowerCase().startsWith("gps adventures")) {
                           var2 = 11;
                        } else if (var3.equalsIgnoreCase("Virtual Cache")) {
                           var2 = 3;
                        } else if (var3.equalsIgnoreCase("Webcam Cache")) {
                           var2 = 12;
                        } else if (var3.equalsIgnoreCase("Locationless Cache")) {
                           var2 = 13;
                        } else if (var3.equalsIgnoreCase("Benchmark")) {
                           var2 = 14;
                        } else if (var3.equalsIgnoreCase("Maze Exhibit")) {
                           var2 = 15;
                        } else if (var3.equalsIgnoreCase("Waymark")) {
                           var2 = 16;
                        } else if (var3.equalsIgnoreCase("Groundspeak")) {
                           var2 = 17;
                        } else if (var3.equalsIgnoreCase("L&F Event")) {
                           var2 = 18;
                        } else if (var3.equalsIgnoreCase("L&F Celebration")) {
                           var2 = 19;
                        } else if (var3.equalsIgnoreCase("Giga-Event Cache")) {
                           var2 = 20;
                        } else {
                           var2 = var1;
                           if (var3.equalsIgnoreCase("Lab Cache")) {
                              var2 = 21;
                           }
                        }
                     } else {
                        var2 = 7;
                     }
                  } else {
                     var2 = 6;
                  }
               } else {
                  var2 = 5;
               }
            } else {
               var2 = 2;
            }
         }
      }

      return var2;
   }

   public static String getTypeAsString(int var0) {
      String var1;
      switch(var0) {
      case 0:
         var1 = "Traditional Cache";
         break;
      case 1:
         var1 = "Multi-Cache";
         break;
      case 2:
         var1 = "Unknown Cache";
         break;
      case 3:
         var1 = "Virtual Cache";
         break;
      case 4:
         var1 = "EarthCache";
         break;
      case 5:
         var1 = "Project APE Cache";
         break;
      case 6:
         var1 = "Letterbox";
         break;
      case 7:
         var1 = "Wherigo Cache";
         break;
      case 8:
         var1 = "Event Cache";
         break;
      case 9:
         var1 = "Mega-Event Cache";
         break;
      case 10:
         var1 = "Cache In Trash Out Event";
         break;
      case 11:
         var1 = "GPS Adventure";
         break;
      case 12:
         var1 = "Webcam Cache";
         break;
      case 13:
         var1 = "Location-less";
         break;
      case 14:
         var1 = "Benchmark";
         break;
      case 15:
         var1 = "Maze Exhibit";
         break;
      case 16:
         var1 = "Waymark";
         break;
      case 17:
         var1 = "Groundspeak";
         break;
      case 18:
         var1 = "L&F Event";
         break;
      case 19:
         var1 = "L&F Celebration";
         break;
      case 20:
         var1 = "Giga-Event Cache";
         break;
      case 21:
         var1 = "Lab Cache";
         break;
      default:
         var1 = "Geocache";
      }

      return var1;
   }

   public static boolean isEventCache(int var0) {
      boolean var1;
      if (var0 != 8 && var0 != 9 && var0 != 20 && var0 != 11 && var0 != 10) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void addImage(GeocachingImage var1) {
      this.mImages.add(var1);
   }

   public boolean containsInData(String var1) {
      boolean var2 = false;
      boolean var3 = true;
      boolean var4;
      if (this.mOwner.toLowerCase().contains(var1)) {
         var4 = var3;
      } else {
         var4 = var3;
         if (!this.mCountry.toLowerCase().contains(var1)) {
            var4 = var3;
            if (!this.mState.toLowerCase().contains(var1)) {
               String[] var5 = this.getDescriptions();
               if (!var5[0].toLowerCase().contains(var1)) {
                  var4 = var2;
                  if (!var5[1].toLowerCase().contains(var1)) {
                     return var4;
                  }
               }

               var4 = true;
            }
         }
      }

      return var4;
   }

   public String getCacheID() {
      return this.mCacheID;
   }

   public String getCacheUrl() {
      return this.mCacheUrl;
   }

   public String getCacheUrlFull() {
      String var1;
      if (this.getSource() == 1) {
         var1 = "http://coord.info/" + this.mCacheID;
      } else if (this.mCacheUrl != null && this.mCacheUrl.length() > 0) {
         var1 = this.mCacheUrl;
      } else {
         var1 = "http://www.geocaching.com/seek/cache_details.aspx?wp=" + this.mCacheID;
      }

      return var1;
   }

   public int getContainer() {
      return this.mContainer;
   }

   public String getContainerText() {
      String var1;
      switch(this.mContainer) {
      case 0:
         var1 = "Not chosen";
         break;
      case 1:
         var1 = "Micro";
         break;
      case 2:
         var1 = "Small";
         break;
      case 3:
         var1 = "Regular";
         break;
      case 4:
         var1 = "Large";
         break;
      case 5:
         var1 = "Huge";
         break;
      case 6:
         var1 = "Other";
         break;
      default:
         var1 = null;
      }

      return var1;
   }

   public String getCountry() {
      return this.mCountry;
   }

   public long getDateHidden() {
      return this.mDateHidden;
   }

   public long getDatePublished() {
      return this.mDatePublished;
   }

   public long getDateUpdated() {
      return this.mDateUpdated;
   }

   public String[] getDescriptions() {
      String[] var1 = new String[]{"", ""};
      if (this.mDescBytes != null && this.mDescBytes.length != 0) {
         IOException var10000;
         label46: {
            GZIPInputStream var2;
            byte[] var4;
            ByteArrayOutputStream var13;
            boolean var10001;
            try {
               ByteArrayInputStream var3 = new ByteArrayInputStream(this.mDescBytes);
               var2 = new GZIPInputStream(var3, 10240);
               var13 = new ByteArrayOutputStream();
               var4 = new byte[1024];
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break label46;
            }

            while(true) {
               int var5;
               try {
                  var5 = var2.read(var4);
               } catch (IOException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               if (var5 == -1) {
                  String var11;
                  try {
                     var11 = Utils.doBytesToString(var13.toByteArray());
                     if (this.mShortDescLength > 0) {
                        var1[0] = var11.substring(0, this.mShortDescLength);
                     }
                  } catch (IOException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }

                  try {
                     var1[1] = var11.substring(this.mShortDescLength);
                     return var1;
                  } catch (IOException var6) {
                     var10000 = var6;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var13.write(var4, 0, var5);
               } catch (IOException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }
            }
         }

         IOException var12 = var10000;
         Logger.logE("GeocachingData", "", var12);
         var1[0] = "";
         var1[1] = "";
      }

      return var1;
   }

   public float getDifficulty() {
      return this.mDifficulty;
   }

   public String getEncodedHints() {
      return this.mEncodedHints;
   }

   public int getFavoritePoints() {
      return this.mFavoritePoints;
   }

   public float getGcVoteAverage() {
      return this.mGcVoteAverage;
   }

   public int getGcVoteNumOfVotes() {
      return this.mGcVoteNumOfVotes;
   }

   public float getGcVoteUserVote() {
      return this.mGcVoteUserVote;
   }

   public long getId() {
      return this.mId;
   }

   public Iterator getImages() {
      return this.mImages.iterator();
   }

   public double getLatOriginal() {
      return this.mLatOriginal;
   }

   public double getLonOriginal() {
      return this.mLonOriginal;
   }

   public String getName() {
      return this.mName;
   }

   public String getNotes() {
      return this.mNotes;
   }

   public String getOwner() {
      return this.mOwner;
   }

   public String getPlacedBy() {
      return this.mPlacedBy;
   }

   public int getSource() {
      return this.mSource;
   }

   public String getState() {
      return this.mState;
   }

   public float getTerrain() {
      return this.mTerrain;
   }

   public int getType() {
      return this.mType;
   }

   protected int getVersion() {
      return 3;
   }

   public boolean isArchived() {
      return this.mArchived;
   }

   public boolean isAvailable() {
      return this.mAvailable;
   }

   public boolean isCacheValid() {
      boolean var1;
      if (this.mCacheID.length() > 0 && this.mName.length() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isComputed() {
      return this.mComputed;
   }

   public boolean isFound() {
      return this.mFound;
   }

   public boolean isPremiumOnly() {
      return this.mPremiumOnly;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mId = var2.readLong();
      this.setCacheID(var2.readString());
      this.mAvailable = var2.readBoolean();
      this.mArchived = var2.readBoolean();
      this.mPremiumOnly = var2.readBoolean();
      this.mName = var2.readString();
      this.mDateUpdated = var2.readLong();
      this.mDateHidden = var2.readLong();
      this.mPlacedBy = var2.readString();
      this.mOwner = var2.readString();
      this.mDatePublished = var2.readLong();
      this.mType = var2.readInt();
      this.mContainer = var2.readInt();
      this.mDifficulty = var2.readFloat();
      this.mTerrain = var2.readFloat();
      this.mCountry = var2.readString();
      this.mState = var2.readString();
      int var3 = var2.readInt();
      this.mShortDescLength = var2.readInt();
      if (var3 > 0) {
         this.mDescBytes = var2.readBytes(var3);
      }

      this.mEncodedHints = var2.readString();
      this.attributes = (ArrayList)var2.readListStorable(GeocachingAttribute.class);
      this.logs = (ArrayList)var2.readListStorable(GeocachingLog.class);
      this.trackables = (ArrayList)var2.readListStorable(GeocachingTrackable.class);
      this.waypoints = (ArrayList)var2.readListStorable(GeocachingWaypoint.class);
      this.mNotes = var2.readString();
      this.mComputed = var2.readBoolean();
      this.mFound = var2.readBoolean();
      this.mCacheUrl = var2.readString();
      this.mFavoritePoints = var2.readInt();
      if (var1 >= 1) {
         this.mGcVoteNumOfVotes = var2.readInt();
         this.mGcVoteAverage = var2.readFloat();
         this.mGcVoteUserVote = var2.readFloat();
      }

      if (var1 >= 2) {
         this.mLonOriginal = var2.readDouble();
         this.mLatOriginal = var2.readDouble();
         this.mImages = var2.readListStorable(GeocachingImage.class);
      }

      if (var1 >= 3) {
         this.mSource = var2.readInt();
      }

   }

   public void reset() {
      this.mId = 0L;
      this.mCacheID = "";
      this.mAvailable = true;
      this.mArchived = false;
      this.mPremiumOnly = false;
      this.mName = "";
      this.mDateUpdated = 0L;
      this.mDateHidden = 0L;
      this.mPlacedBy = "";
      this.mOwner = "";
      this.mDatePublished = 0L;
      this.mType = 0;
      this.mContainer = 0;
      this.mDifficulty = -1.0F;
      this.mTerrain = -1.0F;
      this.mCountry = "";
      this.mState = "";
      this.mDescBytes = null;
      this.mShortDescLength = 0;
      this.mEncodedHints = "";
      this.attributes = new ArrayList();
      this.logs = new ArrayList();
      this.trackables = new ArrayList();
      this.waypoints = new ArrayList();
      this.mNotes = "";
      this.mComputed = false;
      this.mFound = false;
      this.mCacheUrl = "";
      this.mFavoritePoints = -1;
      this.mGcVoteNumOfVotes = -1;
      this.mGcVoteAverage = 0.0F;
      this.mGcVoteUserVote = 0.0F;
      this.mLonOriginal = 0.0D;
      this.mLatOriginal = 0.0D;
      this.mImages = new ArrayList();
      this.mSource = 0;
   }

   public void setArchived(boolean var1) {
      this.mArchived = var1;
   }

   public void setAvailable(boolean var1) {
      this.mAvailable = var1;
   }

   public void setCacheID(String var1) {
      if (var1 != null && var1.length() != 0) {
         byte var2 = 0;
         String var3 = var1.trim().toUpperCase();
         if (var3.startsWith("GC")) {
            var2 = 1;
         } else if (var3.startsWith("OB")) {
            var2 = 105;
         } else if (var3.startsWith("OK")) {
            var2 = 108;
         } else if (var3.startsWith("OP")) {
            var2 = 106;
         } else if (var3.startsWith("OU")) {
            var2 = 109;
         } else if (var3.startsWith("OZ")) {
            var2 = 110;
         } else if (var3.startsWith("O")) {
            var2 = 100;
         }

         this.setCacheID(var1, var2);
      } else {
         Logger.logW("GeocachingData", "setCacheId(" + var1 + "), " + "invalid cache ID");
      }

   }

   public void setCacheID(String var1, int var2) {
      if (var1 != null && var1.length() != 0) {
         this.mCacheID = var1;
         this.setSource(var2);
      } else {
         Logger.logW("GeocachingData", "setCacheId(" + var1 + ", " + var2 + "), " + "invalid cache ID");
      }

   }

   public void setCacheUrl(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.mCacheUrl = var1;
      }

   }

   public void setComputed(boolean var1) {
      this.mComputed = var1;
   }

   public void setContainer(int var1) {
      this.mContainer = var1;
   }

   public void setContainer(String var1) {
      if (var1.equalsIgnoreCase("Micro")) {
         this.setContainer(1);
      } else if (var1.equalsIgnoreCase("Small")) {
         this.setContainer(2);
      } else if (var1.equalsIgnoreCase("Regular")) {
         this.setContainer(3);
      } else if (var1.equalsIgnoreCase("Large")) {
         this.setContainer(4);
      } else if (var1.equalsIgnoreCase("Huge")) {
         this.setContainer(5);
      } else if (var1.equalsIgnoreCase("Not chosen")) {
         this.setContainer(0);
      } else if (var1.equalsIgnoreCase("Other")) {
         this.setContainer(6);
      }

   }

   public void setCountry(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.mCountry = var1;
      }

   }

   public void setDateHidden(long var1) {
      this.mDateHidden = var1;
   }

   public void setDatePublished(long var1) {
      this.mDatePublished = var1;
   }

   public void setDateUpdated(long var1) {
      this.mDateUpdated = var1;
   }

   public boolean setDescriptions(String var1, boolean var2, String var3, boolean var4) {
      boolean var5 = false;
      String var6;
      if (var1 == null) {
         var6 = "";
      } else {
         var6 = var1;
         if (var1.length() > 0) {
            var6 = var1;
            if (!var2) {
               var6 = this.fixToHtml(var1);
            }
         }
      }

      if (var3 == null) {
         var1 = "";
      } else {
         var1 = var3;
         if (var3.length() > 0) {
            var1 = var3;
            if (!var4) {
               var1 = this.fixToHtml(var3);
            }
         }
      }

      try {
         ByteArrayOutputStream var9 = new ByteArrayOutputStream();
         GZIPOutputStream var7 = new GZIPOutputStream(var9);
         var7.write(Utils.doStringToBytes(var6));
         var7.write(Utils.doStringToBytes(var1));
         var7.close();
         this.mDescBytes = var9.toByteArray();
         this.mShortDescLength = var6.length();
      } catch (IOException var8) {
         Logger.logE("GeocachingData", "setDescription(" + var6 + ", " + var2 + ", " + var1 + ", " + var4 + ")", var8);
         this.mDescBytes = null;
         this.mShortDescLength = 0;
         var2 = var5;
         return var2;
      }

      var2 = true;
      return var2;
   }

   public void setDifficulty(float var1) {
      this.mDifficulty = var1;
   }

   public void setEncodedHints(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.mEncodedHints = var1;
      }

   }

   public void setFavoritePoints(int var1) {
      this.mFavoritePoints = var1;
   }

   public void setFound(boolean var1) {
      this.mFound = var1;
   }

   public void setGcVoteAverage(float var1) {
      this.mGcVoteAverage = var1;
   }

   public void setGcVoteNumOfVotes(int var1) {
      this.mGcVoteNumOfVotes = var1;
   }

   public void setGcVoteUserVote(float var1) {
      this.mGcVoteUserVote = var1;
   }

   public void setId(long var1) {
      this.mId = var1;
   }

   public void setLatOriginal(double var1) {
      this.mLatOriginal = var1;
   }

   public void setLonOriginal(double var1) {
      this.mLonOriginal = var1;
   }

   public void setName(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.mName = var1;
      }

   }

   public void setNotes(String var1) {
      if (var1 != null) {
         this.mNotes = var1;
      }

   }

   public void setOwner(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.mOwner = var1;
      }

   }

   public void setPlacedBy(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.mPlacedBy = var1;
      }

   }

   public void setPremiumOnly(boolean var1) {
      this.mPremiumOnly = var1;
   }

   public void setSource(int var1) {
      this.mSource = var1;
   }

   public void setState(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.mState = var1;
      }

   }

   public void setTerrain(float var1) {
      this.mTerrain = var1;
   }

   public void setType(int var1) {
      this.mType = var1;
   }

   public void setType(String var1) {
      this.mType = getTypeAsInt(var1);
   }

   public void sortTrackables() {
      if (this.trackables.size() > 1) {
         Collections.sort(this.trackables, new Comparator() {
            public int compare(GeocachingTrackable var1, GeocachingTrackable var2) {
               return var1.getName().compareTo(var2.getName());
            }
         });
      }

   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.mId);
      var1.writeString(this.mCacheID);
      var1.writeBoolean(this.mAvailable);
      var1.writeBoolean(this.mArchived);
      var1.writeBoolean(this.mPremiumOnly);
      var1.writeString(this.mName);
      var1.writeLong(this.mDateUpdated);
      var1.writeLong(this.mDateHidden);
      var1.writeString(this.mPlacedBy);
      var1.writeString(this.mOwner);
      var1.writeLong(this.mDatePublished);
      var1.writeInt(this.mType);
      var1.writeInt(this.mContainer);
      var1.writeFloat(this.mDifficulty);
      var1.writeFloat(this.mTerrain);
      var1.writeString(this.mCountry);
      var1.writeString(this.mState);
      if (this.mDescBytes != null && this.mDescBytes.length != 0) {
         var1.writeInt(this.mDescBytes.length);
         var1.writeInt(this.mShortDescLength);
         var1.write(this.mDescBytes);
      } else {
         var1.writeInt(0);
         var1.writeInt(0);
      }

      var1.writeString(this.mEncodedHints);
      var1.writeListStorable(this.attributes);
      var1.writeListStorable(this.logs);
      var1.writeListStorable(this.trackables);
      var1.writeListStorable(this.waypoints);
      var1.writeString(this.mNotes);
      var1.writeBoolean(this.mComputed);
      var1.writeBoolean(this.mFound);
      var1.writeString(this.mCacheUrl);
      var1.writeInt(this.mFavoritePoints);
      var1.writeInt(this.mGcVoteNumOfVotes);
      var1.writeFloat(this.mGcVoteAverage);
      var1.writeFloat(this.mGcVoteUserVote);
      var1.writeDouble(this.mLonOriginal);
      var1.writeDouble(this.mLatOriginal);
      var1.writeListStorable(this.mImages);
      var1.writeInt(this.mSource);
   }
}
