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
    public List<GeocachingAttribute> attributes;
    public List<GeocachingLog> logs;
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
    private List<GeocachingImage> mImages;
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
    public List<GeocachingTrackable> trackables;
    public List<GeocachingWaypoint> waypoints;

    /* renamed from: locus.api.objects.geocaching.GeocachingData$1 */
    class C02491 implements Comparator<GeocachingTrackable> {
        C02491() {
        }

        public int compare(GeocachingTrackable object1, GeocachingTrackable object2) {
            return object1.getName().compareTo(object2.getName());
        }
    }

    public GeocachingData(byte[] data) throws IOException {
        super(data);
    }

    public GeocachingData(DataReaderBigEndian dr) throws IOException {
        super(dr);
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getCacheID() {
        return this.mCacheID;
    }

    public void setCacheID(String cacheID) {
        if (cacheID == null || cacheID.length() == 0) {
            Logger.logW(TAG, "setCacheId(" + cacheID + "), " + "invalid cache ID");
            return;
        }
        int source = 0;
        String testCode = cacheID.trim().toUpperCase();
        if (testCode.startsWith("GC")) {
            source = 1;
        } else if (testCode.startsWith("OB")) {
            source = CACHE_SOURCE_OPENCACHING_NL;
        } else if (testCode.startsWith("OK")) {
            source = CACHE_SOURCE_OPENCACHING_UK;
        } else if (testCode.startsWith("OP")) {
            source = CACHE_SOURCE_OPENCACHING_PL;
        } else if (testCode.startsWith("OU")) {
            source = 109;
        } else if (testCode.startsWith("OZ")) {
            source = 110;
        } else if (testCode.startsWith("O")) {
            source = 100;
        }
        setCacheID(cacheID, source);
    }

    public void setCacheID(String cacheID, int source) {
        if (cacheID == null || cacheID.length() == 0) {
            Logger.logW(TAG, "setCacheId(" + cacheID + ", " + source + "), " + "invalid cache ID");
            return;
        }
        this.mCacheID = cacheID;
        setSource(source);
    }

    public int getSource() {
        return this.mSource;
    }

    public void setSource(int source) {
        this.mSource = source;
    }

    public boolean isAvailable() {
        return this.mAvailable;
    }

    public void setAvailable(boolean available) {
        this.mAvailable = available;
    }

    public boolean isArchived() {
        return this.mArchived;
    }

    public void setArchived(boolean archived) {
        this.mArchived = archived;
    }

    public boolean isPremiumOnly() {
        return this.mPremiumOnly;
    }

    public void setPremiumOnly(boolean premiumOnly) {
        this.mPremiumOnly = premiumOnly;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.mName = name;
        }
    }

    public String getPlacedBy() {
        return this.mPlacedBy;
    }

    public void setPlacedBy(String placedBy) {
        if (placedBy != null && placedBy.length() > 0) {
            this.mPlacedBy = placedBy;
        }
    }

    public String getOwner() {
        return this.mOwner;
    }

    public void setOwner(String owner) {
        if (owner != null && owner.length() > 0) {
            this.mOwner = owner;
        }
    }

    public long getDateHidden() {
        return this.mDateHidden;
    }

    public void setDateHidden(long dateHidden) {
        this.mDateHidden = dateHidden;
    }

    public long getDatePublished() {
        return this.mDatePublished;
    }

    public void setDatePublished(long hidden) {
        this.mDatePublished = hidden;
    }

    public long getDateUpdated() {
        return this.mDateUpdated;
    }

    public void setDateUpdated(long lastUpdated) {
        this.mDateUpdated = lastUpdated;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public void setType(String type) {
        this.mType = getTypeAsInt(type);
    }

    public int getContainer() {
        return this.mContainer;
    }

    public String getContainerText() {
        switch (this.mContainer) {
            case 0:
                return "Not chosen";
            case 1:
                return "Micro";
            case 2:
                return "Small";
            case 3:
                return "Regular";
            case 4:
                return "Large";
            case 5:
                return "Huge";
            case 6:
                return "Other";
            default:
                return null;
        }
    }

    public void setContainer(int container) {
        this.mContainer = container;
    }

    public void setContainer(String container) {
        if (container.equalsIgnoreCase("Micro")) {
            setContainer(1);
        } else if (container.equalsIgnoreCase("Small")) {
            setContainer(2);
        } else if (container.equalsIgnoreCase("Regular")) {
            setContainer(3);
        } else if (container.equalsIgnoreCase("Large")) {
            setContainer(4);
        } else if (container.equalsIgnoreCase("Huge")) {
            setContainer(5);
        } else if (container.equalsIgnoreCase("Not chosen")) {
            setContainer(0);
        } else if (container.equalsIgnoreCase("Other")) {
            setContainer(6);
        }
    }

    public float getDifficulty() {
        return this.mDifficulty;
    }

    public void setDifficulty(float difficulty) {
        this.mDifficulty = difficulty;
    }

    public float getTerrain() {
        return this.mTerrain;
    }

    public void setTerrain(float terrain) {
        this.mTerrain = terrain;
    }

    public String getCountry() {
        return this.mCountry;
    }

    public void setCountry(String country) {
        if (country != null && country.length() > 0) {
            this.mCountry = country;
        }
    }

    public String getState() {
        return this.mState;
    }

    public void setState(String state) {
        if (state != null && state.length() > 0) {
            this.mState = state;
        }
    }

    public String[] getDescriptions() {
        String[] res = new String[]{"", ""};
        if (!(this.mDescBytes == null || this.mDescBytes.length == 0)) {
            try {
                GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(this.mDescBytes), 10240);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while (true) {
                    int dataRead = zis.read(buffer);
                    if (dataRead == -1) {
                        break;
                    }
                    baos.write(buffer, 0, dataRead);
                }
                String result = Utils.doBytesToString(baos.toByteArray());
                if (this.mShortDescLength > 0) {
                    res[0] = result.substring(0, this.mShortDescLength);
                }
                res[1] = result.substring(this.mShortDescLength);
            } catch (IOException e) {
                Logger.logE(TAG, "", e);
                res[0] = "";
                res[1] = "";
            }
        }
        return res;
    }

    public boolean setDescriptions(String shortDesc, boolean shortInHtml, String longDesc, boolean longInHtml) {
        if (shortDesc == null) {
            shortDesc = "";
        } else if (shortDesc.length() > 0 && !shortInHtml) {
            shortDesc = fixToHtml(shortDesc);
        }
        if (longDesc == null) {
            longDesc = "";
        } else if (longDesc.length() > 0 && !longInHtml) {
            longDesc = fixToHtml(longDesc);
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream zos = new GZIPOutputStream(baos);
            zos.write(Utils.doStringToBytes(shortDesc));
            zos.write(Utils.doStringToBytes(longDesc));
            zos.close();
            this.mDescBytes = baos.toByteArray();
            this.mShortDescLength = shortDesc.length();
            return true;
        } catch (IOException e) {
            Logger.logE(TAG, "setDescription(" + shortDesc + ", " + shortInHtml + ", " + longDesc + ", " + longInHtml + ")", e);
            this.mDescBytes = null;
            this.mShortDescLength = 0;
            return false;
        }
    }

    public String getEncodedHints() {
        return this.mEncodedHints;
    }

    public void setEncodedHints(String hints) {
        if (hints != null && hints.length() > 0) {
            this.mEncodedHints = hints;
        }
    }

    public String getNotes() {
        return this.mNotes;
    }

    public void setNotes(String notes) {
        if (notes != null) {
            this.mNotes = notes;
        }
    }

    public boolean isComputed() {
        return this.mComputed;
    }

    public void setComputed(boolean computed) {
        this.mComputed = computed;
    }

    public boolean isFound() {
        return this.mFound;
    }

    public void setFound(boolean found) {
        this.mFound = found;
    }

    public String getCacheUrl() {
        return this.mCacheUrl;
    }

    public void setCacheUrl(String url) {
        if (url != null && url.length() > 0) {
            this.mCacheUrl = url;
        }
    }

    public String getCacheUrlFull() {
        if (getSource() == 1) {
            return "http://coord.info/" + this.mCacheID;
        }
        if (this.mCacheUrl == null || this.mCacheUrl.length() <= 0) {
            return "http://www.geocaching.com/seek/cache_details.aspx?wp=" + this.mCacheID;
        }
        return this.mCacheUrl;
    }

    public int getFavoritePoints() {
        return this.mFavoritePoints;
    }

    public void setFavoritePoints(int favoritePoints) {
        this.mFavoritePoints = favoritePoints;
    }

    public int getGcVoteNumOfVotes() {
        return this.mGcVoteNumOfVotes;
    }

    public void setGcVoteNumOfVotes(int gcVoteNumOfVotes) {
        this.mGcVoteNumOfVotes = gcVoteNumOfVotes;
    }

    public float getGcVoteAverage() {
        return this.mGcVoteAverage;
    }

    public void setGcVoteAverage(float gcVoteAverage) {
        this.mGcVoteAverage = gcVoteAverage;
    }

    public float getGcVoteUserVote() {
        return this.mGcVoteUserVote;
    }

    public void setGcVoteUserVote(float gcVoteUserVote) {
        this.mGcVoteUserVote = gcVoteUserVote;
    }

    public double getLonOriginal() {
        return this.mLonOriginal;
    }

    public void setLonOriginal(double lonOriginal) {
        this.mLonOriginal = lonOriginal;
    }

    public double getLatOriginal() {
        return this.mLatOriginal;
    }

    public void setLatOriginal(double latOriginal) {
        this.mLatOriginal = latOriginal;
    }

    public void addImage(GeocachingImage image) {
        this.mImages.add(image);
    }

    public Iterator<GeocachingImage> getImages() {
        return this.mImages.iterator();
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 3;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mId = dr.readLong();
        setCacheID(dr.readString());
        this.mAvailable = dr.readBoolean();
        this.mArchived = dr.readBoolean();
        this.mPremiumOnly = dr.readBoolean();
        this.mName = dr.readString();
        this.mDateUpdated = dr.readLong();
        this.mDateHidden = dr.readLong();
        this.mPlacedBy = dr.readString();
        this.mOwner = dr.readString();
        this.mDatePublished = dr.readLong();
        this.mType = dr.readInt();
        this.mContainer = dr.readInt();
        this.mDifficulty = dr.readFloat();
        this.mTerrain = dr.readFloat();
        this.mCountry = dr.readString();
        this.mState = dr.readString();
        int size = dr.readInt();
        this.mShortDescLength = dr.readInt();
        if (size > 0) {
            this.mDescBytes = dr.readBytes(size);
        }
        this.mEncodedHints = dr.readString();
        this.attributes = (ArrayList) dr.readListStorable(GeocachingAttribute.class);
        this.logs = (ArrayList) dr.readListStorable(GeocachingLog.class);
        this.trackables = (ArrayList) dr.readListStorable(GeocachingTrackable.class);
        this.waypoints = (ArrayList) dr.readListStorable(GeocachingWaypoint.class);
        this.mNotes = dr.readString();
        this.mComputed = dr.readBoolean();
        this.mFound = dr.readBoolean();
        this.mCacheUrl = dr.readString();
        this.mFavoritePoints = dr.readInt();
        if (version >= 1) {
            this.mGcVoteNumOfVotes = dr.readInt();
            this.mGcVoteAverage = dr.readFloat();
            this.mGcVoteUserVote = dr.readFloat();
        }
        if (version >= 2) {
            this.mLonOriginal = dr.readDouble();
            this.mLatOriginal = dr.readDouble();
            this.mImages = dr.readListStorable(GeocachingImage.class);
        }
        if (version >= 3) {
            this.mSource = dr.readInt();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeLong(this.mId);
        dw.writeString(this.mCacheID);
        dw.writeBoolean(this.mAvailable);
        dw.writeBoolean(this.mArchived);
        dw.writeBoolean(this.mPremiumOnly);
        dw.writeString(this.mName);
        dw.writeLong(this.mDateUpdated);
        dw.writeLong(this.mDateHidden);
        dw.writeString(this.mPlacedBy);
        dw.writeString(this.mOwner);
        dw.writeLong(this.mDatePublished);
        dw.writeInt(this.mType);
        dw.writeInt(this.mContainer);
        dw.writeFloat(this.mDifficulty);
        dw.writeFloat(this.mTerrain);
        dw.writeString(this.mCountry);
        dw.writeString(this.mState);
        if (this.mDescBytes == null || this.mDescBytes.length == 0) {
            dw.writeInt(0);
            dw.writeInt(0);
        } else {
            dw.writeInt(this.mDescBytes.length);
            dw.writeInt(this.mShortDescLength);
            dw.write(this.mDescBytes);
        }
        dw.writeString(this.mEncodedHints);
        dw.writeListStorable(this.attributes);
        dw.writeListStorable(this.logs);
        dw.writeListStorable(this.trackables);
        dw.writeListStorable(this.waypoints);
        dw.writeString(this.mNotes);
        dw.writeBoolean(this.mComputed);
        dw.writeBoolean(this.mFound);
        dw.writeString(this.mCacheUrl);
        dw.writeInt(this.mFavoritePoints);
        dw.writeInt(this.mGcVoteNumOfVotes);
        dw.writeFloat(this.mGcVoteAverage);
        dw.writeFloat(this.mGcVoteUserVote);
        dw.writeDouble(this.mLonOriginal);
        dw.writeDouble(this.mLatOriginal);
        dw.writeListStorable(this.mImages);
        dw.writeInt(this.mSource);
    }

    public void reset() {
        this.mId = 0;
        this.mCacheID = "";
        this.mAvailable = true;
        this.mArchived = false;
        this.mPremiumOnly = false;
        this.mName = "";
        this.mDateUpdated = 0;
        this.mDateHidden = 0;
        this.mPlacedBy = "";
        this.mOwner = "";
        this.mDatePublished = 0;
        this.mType = 0;
        this.mContainer = 0;
        this.mDifficulty = -1.0f;
        this.mTerrain = -1.0f;
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
        this.mGcVoteAverage = 0.0f;
        this.mGcVoteUserVote = 0.0f;
        this.mLonOriginal = 0.0d;
        this.mLatOriginal = 0.0d;
        this.mImages = new ArrayList();
        this.mSource = 0;
    }

    private String fixToHtml(String text) {
        try {
            return text.replace("\n", "<br>").replace("  ", "&nbsp;&nbsp;");
        } catch (Exception e) {
            return text;
        }
    }

    public boolean isCacheValid() {
        return this.mCacheID.length() > 0 && this.mName.length() > 0;
    }

    public void sortTrackables() {
        if (this.trackables.size() > 1) {
            Collections.sort(this.trackables, new C02491());
        }
    }

    public static String getTypeAsString(int type) {
        switch (type) {
            case 0:
                return "Traditional Cache";
            case 1:
                return "Multi-Cache";
            case 2:
                return "Unknown Cache";
            case 3:
                return "Virtual Cache";
            case 4:
                return "EarthCache";
            case 5:
                return "Project APE Cache";
            case 6:
                return "Letterbox";
            case 7:
                return "Wherigo Cache";
            case 8:
                return "Event Cache";
            case 9:
                return "Mega-Event Cache";
            case 10:
                return "Cache In Trash Out Event";
            case 11:
                return "GPS Adventure";
            case 12:
                return "Webcam Cache";
            case 13:
                return "Location-less";
            case 14:
                return "Benchmark";
            case 15:
                return "Maze Exhibit";
            case 16:
                return "Waymark";
            case 17:
                return "Groundspeak";
            case 18:
                return "L&F Event";
            case 19:
                return "L&F Celebration";
            case 20:
                return "Giga-Event Cache";
            case 21:
                return "Lab Cache";
            default:
                return "Geocache";
        }
    }

    public static int getTypeAsInt(String type) {
        if (type == null || type.length() == 0) {
            return 100;
        }
        if (type.startsWith("Geocache|")) {
            type = type.substring("Geocache|".length());
        }
        if (type.equalsIgnoreCase("Traditional Cache")) {
            return 0;
        }
        if (type.equalsIgnoreCase("Multi-cache")) {
            return 1;
        }
        if (type.equalsIgnoreCase("Mystery Cache") || type.equalsIgnoreCase("Unknown Cache") || type.equalsIgnoreCase("Mystery/Puzzle Cache")) {
            return 2;
        }
        if (type.equalsIgnoreCase("Project APE Cache") || type.equalsIgnoreCase("Project A.P.E. Cache")) {
            return 5;
        }
        if (type.equalsIgnoreCase("Letterbox Hybrid") || type.equalsIgnoreCase("Letterbox")) {
            return 6;
        }
        if (type.equalsIgnoreCase("Wherigo") || type.equalsIgnoreCase("Wherigo cache")) {
            return 7;
        }
        if (type.equalsIgnoreCase("Event Cache")) {
            return 8;
        }
        if (type.equalsIgnoreCase("Mega-Event Cache")) {
            return 9;
        }
        if (type.equalsIgnoreCase("Cache In Trash Out Event")) {
            return 10;
        }
        if (type.equalsIgnoreCase("EarthCache")) {
            return 4;
        }
        if (type.toLowerCase().startsWith("gps adventures")) {
            return 11;
        }
        if (type.equalsIgnoreCase("Virtual Cache")) {
            return 3;
        }
        if (type.equalsIgnoreCase("Webcam Cache")) {
            return 12;
        }
        if (type.equalsIgnoreCase("Locationless Cache")) {
            return 13;
        }
        if (type.equalsIgnoreCase("Benchmark")) {
            return 14;
        }
        if (type.equalsIgnoreCase("Maze Exhibit")) {
            return 15;
        }
        if (type.equalsIgnoreCase("Waymark")) {
            return 16;
        }
        if (type.equalsIgnoreCase("Groundspeak")) {
            return 17;
        }
        if (type.equalsIgnoreCase("L&F Event")) {
            return 18;
        }
        if (type.equalsIgnoreCase("L&F Celebration")) {
            return 19;
        }
        if (type.equalsIgnoreCase("Giga-Event Cache")) {
            return 20;
        }
        if (type.equalsIgnoreCase("Lab Cache")) {
            return 21;
        }
        return 100;
    }

    public static boolean isEventCache(int type) {
        return type == 8 || type == 9 || type == 20 || type == 11 || type == 10;
    }

    public boolean containsInData(String text) {
        boolean z = false;
        if (this.mOwner.toLowerCase().contains(text) || this.mCountry.toLowerCase().contains(text) || this.mState.toLowerCase().contains(text)) {
            return true;
        }
        String[] desc = getDescriptions();
        if (desc[0].toLowerCase().contains(text) || desc[1].toLowerCase().contains(text)) {
            z = true;
        }
        return z;
    }
}
