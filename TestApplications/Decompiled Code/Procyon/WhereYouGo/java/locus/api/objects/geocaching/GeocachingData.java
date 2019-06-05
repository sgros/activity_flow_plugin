// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.geocaching;

import locus.api.utils.DataWriterBigEndian;
import java.util.Collections;
import java.util.Comparator;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import locus.api.utils.Utils;
import locus.api.utils.Logger;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import java.util.List;
import locus.api.objects.Storable;

public class GeocachingData extends Storable
{
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
    
    public GeocachingData() {
    }
    
    public GeocachingData(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        super(dataReaderBigEndian);
    }
    
    public GeocachingData(final byte[] array) throws IOException {
        super(array);
    }
    
    private String fixToHtml(String replace) {
        try {
            replace = replace.replace("\n", "<br>").replace("  ", "&nbsp;&nbsp;");
            return replace;
        }
        catch (Exception ex) {
            return replace;
        }
    }
    
    public static int getTypeAsInt(final String s) {
        int n2;
        final int n = n2 = 100;
        if (s != null) {
            if (s.length() == 0) {
                n2 = n;
            }
            else {
                String substring = s;
                if (s.startsWith("Geocache|")) {
                    substring = s.substring("Geocache|".length());
                }
                if (substring.equalsIgnoreCase("Traditional Cache")) {
                    n2 = 0;
                }
                else if (substring.equalsIgnoreCase("Multi-cache")) {
                    n2 = 1;
                }
                else if (substring.equalsIgnoreCase("Mystery Cache") || substring.equalsIgnoreCase("Unknown Cache") || substring.equalsIgnoreCase("Mystery/Puzzle Cache")) {
                    n2 = 2;
                }
                else if (substring.equalsIgnoreCase("Project APE Cache") || substring.equalsIgnoreCase("Project A.P.E. Cache")) {
                    n2 = 5;
                }
                else if (substring.equalsIgnoreCase("Letterbox Hybrid") || substring.equalsIgnoreCase("Letterbox")) {
                    n2 = 6;
                }
                else if (substring.equalsIgnoreCase("Wherigo") || substring.equalsIgnoreCase("Wherigo cache")) {
                    n2 = 7;
                }
                else if (substring.equalsIgnoreCase("Event Cache")) {
                    n2 = 8;
                }
                else if (substring.equalsIgnoreCase("Mega-Event Cache")) {
                    n2 = 9;
                }
                else if (substring.equalsIgnoreCase("Cache In Trash Out Event")) {
                    n2 = 10;
                }
                else if (substring.equalsIgnoreCase("EarthCache")) {
                    n2 = 4;
                }
                else if (substring.toLowerCase().startsWith("gps adventures")) {
                    n2 = 11;
                }
                else if (substring.equalsIgnoreCase("Virtual Cache")) {
                    n2 = 3;
                }
                else if (substring.equalsIgnoreCase("Webcam Cache")) {
                    n2 = 12;
                }
                else if (substring.equalsIgnoreCase("Locationless Cache")) {
                    n2 = 13;
                }
                else if (substring.equalsIgnoreCase("Benchmark")) {
                    n2 = 14;
                }
                else if (substring.equalsIgnoreCase("Maze Exhibit")) {
                    n2 = 15;
                }
                else if (substring.equalsIgnoreCase("Waymark")) {
                    n2 = 16;
                }
                else if (substring.equalsIgnoreCase("Groundspeak")) {
                    n2 = 17;
                }
                else if (substring.equalsIgnoreCase("L&F Event")) {
                    n2 = 18;
                }
                else if (substring.equalsIgnoreCase("L&F Celebration")) {
                    n2 = 19;
                }
                else if (substring.equalsIgnoreCase("Giga-Event Cache")) {
                    n2 = 20;
                }
                else {
                    n2 = n;
                    if (substring.equalsIgnoreCase("Lab Cache")) {
                        n2 = 21;
                    }
                }
            }
        }
        return n2;
    }
    
    public static String getTypeAsString(final int n) {
        String s = null;
        switch (n) {
            default: {
                s = "Geocache";
                break;
            }
            case 0: {
                s = "Traditional Cache";
                break;
            }
            case 1: {
                s = "Multi-Cache";
                break;
            }
            case 2: {
                s = "Unknown Cache";
                break;
            }
            case 3: {
                s = "Virtual Cache";
                break;
            }
            case 4: {
                s = "EarthCache";
                break;
            }
            case 5: {
                s = "Project APE Cache";
                break;
            }
            case 6: {
                s = "Letterbox";
                break;
            }
            case 7: {
                s = "Wherigo Cache";
                break;
            }
            case 8: {
                s = "Event Cache";
                break;
            }
            case 9: {
                s = "Mega-Event Cache";
                break;
            }
            case 10: {
                s = "Cache In Trash Out Event";
                break;
            }
            case 11: {
                s = "GPS Adventure";
                break;
            }
            case 12: {
                s = "Webcam Cache";
                break;
            }
            case 13: {
                s = "Location-less";
                break;
            }
            case 14: {
                s = "Benchmark";
                break;
            }
            case 15: {
                s = "Maze Exhibit";
                break;
            }
            case 16: {
                s = "Waymark";
                break;
            }
            case 17: {
                s = "Groundspeak";
                break;
            }
            case 18: {
                s = "L&F Event";
                break;
            }
            case 19: {
                s = "L&F Celebration";
                break;
            }
            case 20: {
                s = "Giga-Event Cache";
                break;
            }
            case 21: {
                s = "Lab Cache";
                break;
            }
        }
        return s;
    }
    
    public static boolean isEventCache(final int n) {
        return n == 8 || n == 9 || n == 20 || n == 11 || n == 10;
    }
    
    public void addImage(final GeocachingImage geocachingImage) {
        this.mImages.add(geocachingImage);
    }
    
    public boolean containsInData(final String s) {
        final boolean b = false;
        final boolean b2 = true;
        boolean b3;
        if (this.mOwner.toLowerCase().contains(s)) {
            b3 = b2;
        }
        else {
            b3 = b2;
            if (!this.mCountry.toLowerCase().contains(s)) {
                b3 = b2;
                if (!this.mState.toLowerCase().contains(s)) {
                    final String[] descriptions = this.getDescriptions();
                    if (!descriptions[0].toLowerCase().contains(s)) {
                        b3 = b;
                        if (!descriptions[1].toLowerCase().contains(s)) {
                            return b3;
                        }
                    }
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public String getCacheID() {
        return this.mCacheID;
    }
    
    public String getCacheUrl() {
        return this.mCacheUrl;
    }
    
    public String getCacheUrlFull() {
        String s;
        if (this.getSource() == 1) {
            s = "http://coord.info/" + this.mCacheID;
        }
        else if (this.mCacheUrl != null && this.mCacheUrl.length() > 0) {
            s = this.mCacheUrl;
        }
        else {
            s = "http://www.geocaching.com/seek/cache_details.aspx?wp=" + this.mCacheID;
        }
        return s;
    }
    
    public int getContainer() {
        return this.mContainer;
    }
    
    public String getContainerText() {
        String s = null;
        switch (this.mContainer) {
            default: {
                s = null;
                break;
            }
            case 1: {
                s = "Micro";
                break;
            }
            case 2: {
                s = "Small";
                break;
            }
            case 3: {
                s = "Regular";
                break;
            }
            case 4: {
                s = "Large";
                break;
            }
            case 5: {
                s = "Huge";
                break;
            }
            case 0: {
                s = "Not chosen";
                break;
            }
            case 6: {
                s = "Other";
                break;
            }
        }
        return s;
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
        final String[] array = { "", "" };
        if (this.mDescBytes != null && this.mDescBytes.length != 0) {
            ByteArrayOutputStream byteArrayOutputStream;
            try {
                final GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(this.mDescBytes), 10240);
                byteArrayOutputStream = new ByteArrayOutputStream();
                final byte[] array2 = new byte[1024];
                while (true) {
                    final int read = gzipInputStream.read(array2);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(array2, 0, read);
                }
            }
            catch (IOException ex) {
                Logger.logE("GeocachingData", "", ex);
                array[1] = (array[0] = "");
                return array;
            }
            final String doBytesToString = Utils.doBytesToString(byteArrayOutputStream.toByteArray());
            if (this.mShortDescLength > 0) {
                array[0] = doBytesToString.substring(0, this.mShortDescLength);
            }
            array[1] = doBytesToString.substring(this.mShortDescLength);
        }
        return array;
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
    
    public Iterator<GeocachingImage> getImages() {
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
    
    @Override
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
        return this.mCacheID.length() > 0 && this.mName.length() > 0;
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
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        this.mId = dataReaderBigEndian.readLong();
        this.setCacheID(dataReaderBigEndian.readString());
        this.mAvailable = dataReaderBigEndian.readBoolean();
        this.mArchived = dataReaderBigEndian.readBoolean();
        this.mPremiumOnly = dataReaderBigEndian.readBoolean();
        this.mName = dataReaderBigEndian.readString();
        this.mDateUpdated = dataReaderBigEndian.readLong();
        this.mDateHidden = dataReaderBigEndian.readLong();
        this.mPlacedBy = dataReaderBigEndian.readString();
        this.mOwner = dataReaderBigEndian.readString();
        this.mDatePublished = dataReaderBigEndian.readLong();
        this.mType = dataReaderBigEndian.readInt();
        this.mContainer = dataReaderBigEndian.readInt();
        this.mDifficulty = dataReaderBigEndian.readFloat();
        this.mTerrain = dataReaderBigEndian.readFloat();
        this.mCountry = dataReaderBigEndian.readString();
        this.mState = dataReaderBigEndian.readString();
        final int int1 = dataReaderBigEndian.readInt();
        this.mShortDescLength = dataReaderBigEndian.readInt();
        if (int1 > 0) {
            this.mDescBytes = dataReaderBigEndian.readBytes(int1);
        }
        this.mEncodedHints = dataReaderBigEndian.readString();
        this.attributes = (List<GeocachingAttribute>)(ArrayList)dataReaderBigEndian.readListStorable(GeocachingAttribute.class);
        this.logs = (List<GeocachingLog>)(ArrayList)dataReaderBigEndian.readListStorable(GeocachingLog.class);
        this.trackables = (List<GeocachingTrackable>)(ArrayList)dataReaderBigEndian.readListStorable(GeocachingTrackable.class);
        this.waypoints = (List<GeocachingWaypoint>)(ArrayList)dataReaderBigEndian.readListStorable(GeocachingWaypoint.class);
        this.mNotes = dataReaderBigEndian.readString();
        this.mComputed = dataReaderBigEndian.readBoolean();
        this.mFound = dataReaderBigEndian.readBoolean();
        this.mCacheUrl = dataReaderBigEndian.readString();
        this.mFavoritePoints = dataReaderBigEndian.readInt();
        if (n >= 1) {
            this.mGcVoteNumOfVotes = dataReaderBigEndian.readInt();
            this.mGcVoteAverage = dataReaderBigEndian.readFloat();
            this.mGcVoteUserVote = dataReaderBigEndian.readFloat();
        }
        if (n >= 2) {
            this.mLonOriginal = dataReaderBigEndian.readDouble();
            this.mLatOriginal = dataReaderBigEndian.readDouble();
            this.mImages = (List<GeocachingImage>)dataReaderBigEndian.readListStorable(GeocachingImage.class);
        }
        if (n >= 3) {
            this.mSource = dataReaderBigEndian.readInt();
        }
    }
    
    @Override
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
        this.mDifficulty = -1.0f;
        this.mTerrain = -1.0f;
        this.mCountry = "";
        this.mState = "";
        this.mDescBytes = null;
        this.mShortDescLength = 0;
        this.mEncodedHints = "";
        this.attributes = new ArrayList<GeocachingAttribute>();
        this.logs = new ArrayList<GeocachingLog>();
        this.trackables = new ArrayList<GeocachingTrackable>();
        this.waypoints = new ArrayList<GeocachingWaypoint>();
        this.mNotes = "";
        this.mComputed = false;
        this.mFound = false;
        this.mCacheUrl = "";
        this.mFavoritePoints = -1;
        this.mGcVoteNumOfVotes = -1;
        this.mGcVoteAverage = 0.0f;
        this.mGcVoteUserVote = 0.0f;
        this.mLonOriginal = 0.0;
        this.mLatOriginal = 0.0;
        this.mImages = new ArrayList<GeocachingImage>();
        this.mSource = 0;
    }
    
    public void setArchived(final boolean mArchived) {
        this.mArchived = mArchived;
    }
    
    public void setAvailable(final boolean mAvailable) {
        this.mAvailable = mAvailable;
    }
    
    public void setCacheID(final String str) {
        if (str == null || str.length() == 0) {
            Logger.logW("GeocachingData", "setCacheId(" + str + "), " + "invalid cache ID");
        }
        else {
            int n = 0;
            final String upperCase = str.trim().toUpperCase();
            if (upperCase.startsWith("GC")) {
                n = 1;
            }
            else if (upperCase.startsWith("OB")) {
                n = 105;
            }
            else if (upperCase.startsWith("OK")) {
                n = 108;
            }
            else if (upperCase.startsWith("OP")) {
                n = 106;
            }
            else if (upperCase.startsWith("OU")) {
                n = 109;
            }
            else if (upperCase.startsWith("OZ")) {
                n = 110;
            }
            else if (upperCase.startsWith("O")) {
                n = 100;
            }
            this.setCacheID(str, n);
        }
    }
    
    public void setCacheID(final String s, final int n) {
        if (s == null || s.length() == 0) {
            Logger.logW("GeocachingData", "setCacheId(" + s + ", " + n + "), " + "invalid cache ID");
        }
        else {
            this.mCacheID = s;
            this.setSource(n);
        }
    }
    
    public void setCacheUrl(final String mCacheUrl) {
        if (mCacheUrl != null && mCacheUrl.length() > 0) {
            this.mCacheUrl = mCacheUrl;
        }
    }
    
    public void setComputed(final boolean mComputed) {
        this.mComputed = mComputed;
    }
    
    public void setContainer(final int mContainer) {
        this.mContainer = mContainer;
    }
    
    public void setContainer(final String s) {
        if (s.equalsIgnoreCase("Micro")) {
            this.setContainer(1);
        }
        else if (s.equalsIgnoreCase("Small")) {
            this.setContainer(2);
        }
        else if (s.equalsIgnoreCase("Regular")) {
            this.setContainer(3);
        }
        else if (s.equalsIgnoreCase("Large")) {
            this.setContainer(4);
        }
        else if (s.equalsIgnoreCase("Huge")) {
            this.setContainer(5);
        }
        else if (s.equalsIgnoreCase("Not chosen")) {
            this.setContainer(0);
        }
        else if (s.equalsIgnoreCase("Other")) {
            this.setContainer(6);
        }
    }
    
    public void setCountry(final String mCountry) {
        if (mCountry != null && mCountry.length() > 0) {
            this.mCountry = mCountry;
        }
    }
    
    public void setDateHidden(final long mDateHidden) {
        this.mDateHidden = mDateHidden;
    }
    
    public void setDatePublished(final long mDatePublished) {
        this.mDatePublished = mDatePublished;
    }
    
    public void setDateUpdated(final long mDateUpdated) {
        this.mDateUpdated = mDateUpdated;
    }
    
    public boolean setDescriptions(String fixToHtml, boolean b, final String s, final boolean b2) {
        final boolean b3 = false;
        Label_0083: {
            if (fixToHtml != null) {
                break Label_0083;
            }
            String fixToHtml2 = "";
            ByteArrayOutputStream out;
            GZIPOutputStream gzipOutputStream;
            Label_0020_Outer:Block_7_Outer:Block_6_Outer:Block_5_Outer:
            while (true) {
                Label_0110: {
                    if (s != null) {
                        break Label_0110;
                    }
                    fixToHtml = "";
                    try {
                        // iftrue(Label_0012:, fixToHtml.length() <= 0)
                        // iftrue(Label_0020:, b2)
                        // iftrue(Label_0020:, s.length() <= 0)
                        while (true) {
                            Block_4: {
                                while (true) {
                                    while (true) {
                                        while (true) {
                                            out = new ByteArrayOutputStream();
                                            gzipOutputStream = new GZIPOutputStream(out);
                                            gzipOutputStream.write(Utils.doStringToBytes(fixToHtml2));
                                            gzipOutputStream.write(Utils.doStringToBytes(fixToHtml));
                                            gzipOutputStream.close();
                                            this.mDescBytes = out.toByteArray();
                                            this.mShortDescLength = fixToHtml2.length();
                                            b = true;
                                            return b;
                                            fixToHtml = this.fixToHtml(s);
                                            continue Block_7_Outer;
                                        }
                                        fixToHtml2 = fixToHtml;
                                        break Block_4;
                                        fixToHtml = s;
                                        continue Block_6_Outer;
                                    }
                                    fixToHtml = s;
                                    continue Block_5_Outer;
                                }
                                fixToHtml2 = this.fixToHtml(fixToHtml);
                                continue Label_0020_Outer;
                            }
                            fixToHtml2 = fixToHtml;
                            continue;
                        }
                    }
                    // iftrue(Label_0012:, b != false)
                    catch (IOException ex) {
                        Logger.logE("GeocachingData", "setDescription(" + fixToHtml2 + ", " + b + ", " + fixToHtml + ", " + b2 + ")", ex);
                        this.mDescBytes = null;
                        this.mShortDescLength = 0;
                        b = b3;
                        return b;
                    }
                }
                break;
            }
        }
    }
    
    public void setDifficulty(final float mDifficulty) {
        this.mDifficulty = mDifficulty;
    }
    
    public void setEncodedHints(final String mEncodedHints) {
        if (mEncodedHints != null && mEncodedHints.length() > 0) {
            this.mEncodedHints = mEncodedHints;
        }
    }
    
    public void setFavoritePoints(final int mFavoritePoints) {
        this.mFavoritePoints = mFavoritePoints;
    }
    
    public void setFound(final boolean mFound) {
        this.mFound = mFound;
    }
    
    public void setGcVoteAverage(final float mGcVoteAverage) {
        this.mGcVoteAverage = mGcVoteAverage;
    }
    
    public void setGcVoteNumOfVotes(final int mGcVoteNumOfVotes) {
        this.mGcVoteNumOfVotes = mGcVoteNumOfVotes;
    }
    
    public void setGcVoteUserVote(final float mGcVoteUserVote) {
        this.mGcVoteUserVote = mGcVoteUserVote;
    }
    
    public void setId(final long mId) {
        this.mId = mId;
    }
    
    public void setLatOriginal(final double mLatOriginal) {
        this.mLatOriginal = mLatOriginal;
    }
    
    public void setLonOriginal(final double mLonOriginal) {
        this.mLonOriginal = mLonOriginal;
    }
    
    public void setName(final String mName) {
        if (mName != null && mName.length() > 0) {
            this.mName = mName;
        }
    }
    
    public void setNotes(final String mNotes) {
        if (mNotes != null) {
            this.mNotes = mNotes;
        }
    }
    
    public void setOwner(final String mOwner) {
        if (mOwner != null && mOwner.length() > 0) {
            this.mOwner = mOwner;
        }
    }
    
    public void setPlacedBy(final String mPlacedBy) {
        if (mPlacedBy != null && mPlacedBy.length() > 0) {
            this.mPlacedBy = mPlacedBy;
        }
    }
    
    public void setPremiumOnly(final boolean mPremiumOnly) {
        this.mPremiumOnly = mPremiumOnly;
    }
    
    public void setSource(final int mSource) {
        this.mSource = mSource;
    }
    
    public void setState(final String mState) {
        if (mState != null && mState.length() > 0) {
            this.mState = mState;
        }
    }
    
    public void setTerrain(final float mTerrain) {
        this.mTerrain = mTerrain;
    }
    
    public void setType(final int mType) {
        this.mType = mType;
    }
    
    public void setType(final String s) {
        this.mType = getTypeAsInt(s);
    }
    
    public void sortTrackables() {
        if (this.trackables.size() > 1) {
            Collections.sort(this.trackables, new Comparator<GeocachingTrackable>() {
                @Override
                public int compare(final GeocachingTrackable geocachingTrackable, final GeocachingTrackable geocachingTrackable2) {
                    return geocachingTrackable.getName().compareTo(geocachingTrackable2.getName());
                }
            });
        }
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeLong(this.mId);
        dataWriterBigEndian.writeString(this.mCacheID);
        dataWriterBigEndian.writeBoolean(this.mAvailable);
        dataWriterBigEndian.writeBoolean(this.mArchived);
        dataWriterBigEndian.writeBoolean(this.mPremiumOnly);
        dataWriterBigEndian.writeString(this.mName);
        dataWriterBigEndian.writeLong(this.mDateUpdated);
        dataWriterBigEndian.writeLong(this.mDateHidden);
        dataWriterBigEndian.writeString(this.mPlacedBy);
        dataWriterBigEndian.writeString(this.mOwner);
        dataWriterBigEndian.writeLong(this.mDatePublished);
        dataWriterBigEndian.writeInt(this.mType);
        dataWriterBigEndian.writeInt(this.mContainer);
        dataWriterBigEndian.writeFloat(this.mDifficulty);
        dataWriterBigEndian.writeFloat(this.mTerrain);
        dataWriterBigEndian.writeString(this.mCountry);
        dataWriterBigEndian.writeString(this.mState);
        if (this.mDescBytes == null || this.mDescBytes.length == 0) {
            dataWriterBigEndian.writeInt(0);
            dataWriterBigEndian.writeInt(0);
        }
        else {
            dataWriterBigEndian.writeInt(this.mDescBytes.length);
            dataWriterBigEndian.writeInt(this.mShortDescLength);
            dataWriterBigEndian.write(this.mDescBytes);
        }
        dataWriterBigEndian.writeString(this.mEncodedHints);
        dataWriterBigEndian.writeListStorable(this.attributes);
        dataWriterBigEndian.writeListStorable(this.logs);
        dataWriterBigEndian.writeListStorable(this.trackables);
        dataWriterBigEndian.writeListStorable(this.waypoints);
        dataWriterBigEndian.writeString(this.mNotes);
        dataWriterBigEndian.writeBoolean(this.mComputed);
        dataWriterBigEndian.writeBoolean(this.mFound);
        dataWriterBigEndian.writeString(this.mCacheUrl);
        dataWriterBigEndian.writeInt(this.mFavoritePoints);
        dataWriterBigEndian.writeInt(this.mGcVoteNumOfVotes);
        dataWriterBigEndian.writeFloat(this.mGcVoteAverage);
        dataWriterBigEndian.writeFloat(this.mGcVoteUserVote);
        dataWriterBigEndian.writeDouble(this.mLonOriginal);
        dataWriterBigEndian.writeDouble(this.mLatOriginal);
        dataWriterBigEndian.writeListStorable(this.mImages);
        dataWriterBigEndian.writeInt(this.mSource);
    }
}
