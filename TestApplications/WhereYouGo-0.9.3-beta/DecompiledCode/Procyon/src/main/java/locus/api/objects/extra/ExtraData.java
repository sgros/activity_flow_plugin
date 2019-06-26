// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.DataReaderBigEndian;
import java.util.Collection;
import java.util.Arrays;
import locus.api.utils.Logger;
import java.util.ArrayList;
import java.util.List;
import locus.api.utils.Utils;
import java.io.IOException;
import locus.api.utils.SparseArrayCompat;
import locus.api.objects.Storable;

public class ExtraData extends Storable
{
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
    public static final int[] RTE_TYPES_SORTED;
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
    SparseArrayCompat<byte[]> parameters;
    
    static {
        RTE_TYPES_SORTED = new int[] { 6, 0, 1, 7, 2, 4, 5, 8, 9, 3, 10, 11 };
    }
    
    public ExtraData() {
    }
    
    public ExtraData(final byte[] array) throws IOException {
        super(array);
    }
    
    private boolean addToStorage(String string, String parameter, int n, final int n2) {
        boolean b2;
        final boolean b = b2 = false;
        if (parameter != null) {
            if (parameter.length() == 0) {
                b2 = b;
            }
            else {
                if (string != null && string.length() > 0) {
                    string = string + "|" + parameter;
                }
                else {
                    string = parameter;
                }
                while (true) {
                    b2 = b;
                    if (n > n2) {
                        return b2;
                    }
                    parameter = this.getParameter(n);
                    if (parameter == null) {
                        break;
                    }
                    b2 = b;
                    if (parameter.equalsIgnoreCase(string)) {
                        return b2;
                    }
                    ++n;
                }
                this.parameters.put(n, Utils.doStringToBytes(string));
                b2 = true;
            }
        }
        return b2;
    }
    
    private List<String> convertToTexts(final List<LabelTextContainer> list) {
        final ArrayList<String> list2 = new ArrayList<String>();
        for (int i = 0; i < list.size(); ++i) {
            list2.add(list.get(i).text);
        }
        return list2;
    }
    
    public static String generateCallbackString(String string, final String s, final String s2, String s3, final String str) {
        if (s == null || s.length() == 0 || s2 == null || s2.length() == 0) {
            Logger.logD("ExtraData", "generateCallbackString(" + string + ", " + s + ", " + s2 + ", " + s3 + ", " + str + "), " + "invalid packageName or className parameter");
            string = "";
        }
        else {
            String str2;
            if ((str2 = string) == null) {
                str2 = "";
            }
            if ((string = s3) == null) {
                string = "";
            }
            if ((s3 = str) == null) {
                s3 = "";
            }
            string = str2 + ";" + s + ";" + s2 + ";" + string + ";" + s3 + ";";
        }
        return string;
    }
    
    private List<LabelTextContainer> getFromStorage(int i, final int n) {
        final ArrayList<LabelTextContainer> list = new ArrayList<LabelTextContainer>();
        while (i <= n) {
            final String parameter = this.getParameter(i);
            if (parameter != null && parameter.length() != 0) {
                list.add(new LabelTextContainer(parameter));
            }
            ++i;
        }
        return list;
    }
    
    private void removeAllFromStorage(int i, final int n) {
        while (i <= n) {
            this.parameters.remove(i);
            ++i;
        }
    }
    
    private boolean removeFromStorage(final String suffix, int n, final int n2) {
        boolean b2;
        final boolean b = b2 = false;
        if (suffix != null) {
            if (suffix.length() == 0) {
                b2 = b;
            }
            else {
                while (true) {
                    b2 = b;
                    if (n > n2) {
                        return b2;
                    }
                    final String parameter = this.getParameter(n);
                    if (parameter != null && parameter.endsWith(suffix)) {
                        break;
                    }
                    ++n;
                }
                this.parameters.remove(n);
                b2 = true;
            }
        }
        return b2;
    }
    
    public boolean addAudio(final String s) {
        return this.addToStorage("", s, 1500, 1599);
    }
    
    public boolean addEmail(final String s) {
        return this.addToStorage("", s, 1100, 1199);
    }
    
    public boolean addEmail(final String s, final String s2) {
        return this.addToStorage(s, s2, 1100, 1199);
    }
    
    public boolean addOtherFile(final String s) {
        return this.addToStorage("", s, 1800, 1999);
    }
    
    public boolean addParameter(final int n, final byte b) {
        return this.addParameter(n, new byte[] { b });
    }
    
    public boolean addParameter(final int i, String trim) {
        final boolean b = false;
        boolean b2;
        if (trim == null) {
            b2 = b;
        }
        else {
            this.removeParameter(i);
            trim = trim.trim();
            b2 = b;
            if (trim.length() != 0) {
                if (i > 1000 && i < 2000) {
                    Logger.logW("ExtraData", "addParam(" + i + ", " + trim + "), " + "values 1000 - 1999 reserved!");
                    b2 = b;
                }
                else {
                    this.parameters.put(i, Utils.doStringToBytes(trim));
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public boolean addParameter(final int i, final byte[] a) {
        final boolean b = false;
        this.removeParameter(i);
        boolean b2 = b;
        if (a != null) {
            if (a.length == 0) {
                b2 = b;
            }
            else if (i > 1000 && i < 2000) {
                Logger.logW("ExtraData", "addParam(" + i + ", " + Arrays.toString(a) + "), " + "values 1000 - 1999 reserved!");
                b2 = b;
            }
            else {
                this.parameters.put(i, a);
                b2 = true;
            }
        }
        return b2;
    }
    
    public boolean addPhone(final String s) {
        return this.addToStorage("", s, 1000, 1099);
    }
    
    public boolean addPhone(final String s, final String s2) {
        return this.addToStorage(s, s2, 1000, 1099);
    }
    
    public boolean addPhoto(final String s) {
        return this.addToStorage("", s, 1300, 1399);
    }
    
    public boolean addUrl(final String s) {
        return this.addToStorage("", s, 1200, 1299);
    }
    
    public boolean addUrl(final String s, final String s2) {
        return this.addToStorage(s, s2, 1200, 1299);
    }
    
    public boolean addVideo(final String s) {
        return this.addToStorage("", s, 1400, 1499);
    }
    
    public List<String> getAllAttachments() {
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<String>();
        list.addAll(this.getPhotos());
        list.addAll(this.getAudios());
        list.addAll(this.getVideos());
        list.addAll(this.getOtherFiles());
        return (List<String>)list;
    }
    
    public int getAllAttachmentsCount() {
        return this.getAllAttachments().size();
    }
    
    public List<String> getAudios() {
        return this.convertToTexts(this.getFromStorage(1500, 1599));
    }
    
    public int getCount() {
        return this.parameters.size();
    }
    
    public List<LabelTextContainer> getEmails() {
        return this.getFromStorage(1100, 1199);
    }
    
    public List<String> getOtherFiles() {
        return this.convertToTexts(this.getFromStorage(1800, 1999));
    }
    
    public String getParameter(final int n) {
        final byte[] array = this.parameters.get(n);
        String doBytesToString;
        if (array != null) {
            doBytesToString = Utils.doBytesToString(array);
        }
        else {
            doBytesToString = null;
        }
        return doBytesToString;
    }
    
    public String getParameterNotNull(final int n) {
        String parameter;
        if ((parameter = this.getParameter(n)) == null) {
            parameter = "";
        }
        return parameter;
    }
    
    public byte[] getParameterRaw(final int n) {
        return this.parameters.get(n);
    }
    
    public List<LabelTextContainer> getPhones() {
        return this.getFromStorage(1000, 1099);
    }
    
    public List<String> getPhotos() {
        return this.convertToTexts(this.getFromStorage(1300, 1399));
    }
    
    public List<LabelTextContainer> getUrls() {
        return this.getFromStorage(1200, 1299);
    }
    
    @Override
    protected int getVersion() {
        return 0;
    }
    
    public List<String> getVideos() {
        return this.convertToTexts(this.getFromStorage(1400, 1499));
    }
    
    public boolean hasParameter(final int n) {
        return this.parameters.get(n) != null;
    }
    
    @Override
    protected void readObject(int i, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        final int int1 = dataReaderBigEndian.readInt();
        this.parameters.clear();
        for (i = 0; i < int1; ++i) {
            this.parameters.put(dataReaderBigEndian.readInt(), dataReaderBigEndian.readBytes(dataReaderBigEndian.readInt()));
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
    
    public boolean removeAudio(final String s) {
        return this.removeFromStorage(s, 1500, 1599);
    }
    
    public boolean removeEmail(final String s) {
        return this.removeFromStorage(s, 1100, 1199);
    }
    
    public boolean removeOtherFile(final String s) {
        return this.removeFromStorage(s, 1800, 1999);
    }
    
    public String removeParameter(final int n) {
        final String parameter = this.getParameter(n);
        this.parameters.remove(n);
        return parameter;
    }
    
    public boolean removePhone(final String s) {
        return this.removeFromStorage(s, 1000, 1099);
    }
    
    public boolean removePhoto(final String s) {
        return this.removeFromStorage(s, 1300, 1399);
    }
    
    public boolean removeUrl(final String s) {
        return this.removeFromStorage(s, 1200, 1299);
    }
    
    public boolean removeVideo(final String s) {
        return this.removeFromStorage(s, 1400, 1499);
    }
    
    @Override
    public void reset() {
        this.parameters = new SparseArrayCompat<byte[]>();
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeInt(this.parameters.size());
        for (int i = 0; i < this.parameters.size(); ++i) {
            dataWriterBigEndian.writeInt(this.parameters.keyAt(i));
            final byte[] array = this.parameters.valueAt(i);
            dataWriterBigEndian.writeInt(array.length);
            if (array.length > 0) {
                dataWriterBigEndian.write(array);
            }
        }
    }
    
    public static class LabelTextContainer
    {
        public final String label;
        public final String text;
        
        public LabelTextContainer(final String text) {
            if (text.contains("|")) {
                final int index = text.indexOf("|");
                this.label = text.substring(0, index);
                this.text = text.substring(index + 1);
            }
            else {
                this.label = "";
                this.text = text;
            }
        }
        
        public LabelTextContainer(final String label, final String text) {
            if (label == null) {
                this.label = "";
            }
            else {
                this.label = label;
            }
            this.text = text;
        }
        
        public String getAsText() {
            String s;
            if (this.label.length() > 0) {
                s = this.label + "|" + this.text;
            }
            else {
                s = this.text;
            }
            return s;
        }
        
        public String getFormattedAsEmail() {
            String str;
            if (this.label.length() == 0) {
                str = this.text;
            }
            else {
                str = this.label;
            }
            return "<a href=\"mailto:" + this.text + "\">" + str + "</a>";
        }
        
        public String getFormattedAsPhone() {
            String str;
            if (this.label.length() == 0) {
                str = this.text;
            }
            else {
                str = this.label;
            }
            return "<a href=\"tel:" + this.text + "\">" + str + "</a>";
        }
        
        public String getFormattedAsUrl(final boolean b) {
            String str;
            if (this.label.length() == 0) {
                str = this.text;
            }
            else {
                str = this.label;
            }
            String str3;
            final String str2 = str3 = this.text;
            if (b) {
                str3 = str2;
                if (!str2.contains("://")) {
                    str3 = "http://" + str2;
                }
            }
            return "<a href=\"" + str3 + "\" target=\"_blank\">" + str + "</a>";
        }
    }
}
