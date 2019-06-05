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
    public static final byte SOURCE_GEOCACHING_WAYPOINT = (byte) 50;
    public static final byte SOURCE_INVISIBLE = (byte) 56;
    public static final byte SOURCE_LIVE_TRACKING = (byte) 59;
    public static final byte SOURCE_MAP_TEMP = (byte) 51;
    public static final byte SOURCE_MUNZEE = (byte) 58;
    public static final byte SOURCE_OPENSTREETBUGS = (byte) 55;
    public static final byte SOURCE_PARKING_SERVICE = (byte) 49;
    public static final byte SOURCE_POI_OSM_DB = (byte) 57;
    public static final byte SOURCE_ROUTE_LOCATION = (byte) 53;
    public static final byte SOURCE_ROUTE_WAYPOINT = (byte) 52;
    public static final byte SOURCE_UNKNOWN = (byte) 48;
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

    public static class LabelTextContainer {
        public final String label;
        public final String text;

        public LabelTextContainer(String value) {
            if (value.contains("|")) {
                int index = value.indexOf("|");
                this.label = value.substring(0, index);
                this.text = value.substring(index + 1);
                return;
            }
            this.label = "";
            this.text = value;
        }

        public LabelTextContainer(String label, String text) {
            if (label == null) {
                this.label = "";
            } else {
                this.label = label;
            }
            this.text = text;
        }

        public String getAsText() {
            if (this.label.length() > 0) {
                return this.label + "|" + this.text;
            }
            return this.text;
        }

        public String getFormattedAsEmail() {
            return "<a href=\"mailto:" + this.text + "\">" + (this.label.length() == 0 ? this.text : this.label) + "</a>";
        }

        public String getFormattedAsPhone() {
            return "<a href=\"tel:" + this.text + "\">" + (this.label.length() == 0 ? this.text : this.label) + "</a>";
        }

        public String getFormattedAsUrl(boolean checkProtocol) {
            String lab = this.label.length() == 0 ? this.text : this.label;
            String url = this.text;
            if (checkProtocol && !url.contains("://")) {
                url = "http://" + url;
            }
            return "<a href=\"" + url + "\" target=\"_blank\">" + lab + "</a>";
        }
    }

    public ExtraData(byte[] data) throws IOException {
        super(data);
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        int size = dr.readInt();
        this.parameters.clear();
        for (int i = 0; i < size; i++) {
            this.parameters.put(dr.readInt(), dr.readBytes(dr.readInt()));
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeInt(this.parameters.size());
        int m = this.parameters.size();
        for (int i = 0; i < m; i++) {
            dw.writeInt(this.parameters.keyAt(i));
            byte[] data = (byte[]) this.parameters.valueAt(i);
            dw.writeInt(data.length);
            if (data.length > 0) {
                dw.write(data);
            }
        }
    }

    public void reset() {
        this.parameters = new SparseArrayCompat();
    }

    public boolean addParameter(int key, String value) {
        if (value == null) {
            return false;
        }
        removeParameter(key);
        value = value.trim();
        if (value.length() == 0) {
            return false;
        }
        if (key <= 1000 || key >= 2000) {
            this.parameters.put(key, Utils.doStringToBytes(value));
            return true;
        }
        Logger.logW(TAG, "addParam(" + key + ", " + value + "), " + "values 1000 - 1999 reserved!");
        return false;
    }

    public boolean addParameter(int key, byte value) {
        return addParameter(key, new byte[]{value});
    }

    public boolean addParameter(int key, byte[] value) {
        removeParameter(key);
        if (value == null || value.length == 0) {
            return false;
        }
        if (key <= 1000 || key >= 2000) {
            this.parameters.put(key, value);
            return true;
        }
        Logger.logW(TAG, "addParam(" + key + ", " + Arrays.toString(value) + "), " + "values 1000 - 1999 reserved!");
        return false;
    }

    public String getParameter(int key) {
        byte[] data = (byte[]) this.parameters.get(key);
        if (data != null) {
            return Utils.doBytesToString(data);
        }
        return null;
    }

    public byte[] getParameterRaw(int key) {
        return (byte[]) this.parameters.get(key);
    }

    public String getParameterNotNull(int key) {
        String par = getParameter(key);
        if (par == null) {
            return "";
        }
        return par;
    }

    public boolean hasParameter(int key) {
        return this.parameters.get(key) != null;
    }

    public String removeParameter(int key) {
        String value = getParameter(key);
        this.parameters.remove(key);
        return value;
    }

    public int getCount() {
        return this.parameters.size();
    }

    public boolean addPhone(String phone) {
        return addToStorage("", phone, 1000, PAR_PHONE_MAX);
    }

    public boolean addPhone(String label, String phone) {
        return addToStorage(label, phone, 1000, PAR_PHONE_MAX);
    }

    public List<LabelTextContainer> getPhones() {
        return getFromStorage(1000, PAR_PHONE_MAX);
    }

    public boolean removePhone(String phone) {
        return removeFromStorage(phone, 1000, PAR_PHONE_MAX);
    }

    public void removeAllPhones() {
        removeAllFromStorage(1000, PAR_PHONE_MAX);
    }

    public boolean addEmail(String email) {
        return addToStorage("", email, PAR_EMAIL_MIN, PAR_EMAIL_MAX);
    }

    public boolean addEmail(String label, String email) {
        return addToStorage(label, email, PAR_EMAIL_MIN, PAR_EMAIL_MAX);
    }

    public List<LabelTextContainer> getEmails() {
        return getFromStorage(PAR_EMAIL_MIN, PAR_EMAIL_MAX);
    }

    public boolean removeEmail(String email) {
        return removeFromStorage(email, PAR_EMAIL_MIN, PAR_EMAIL_MAX);
    }

    public void removeAllEmails() {
        removeAllFromStorage(PAR_EMAIL_MIN, PAR_EMAIL_MAX);
    }

    public boolean addUrl(String url) {
        return addToStorage("", url, PAR_URL_MIN, PAR_URL_MAX);
    }

    public boolean addUrl(String label, String url) {
        return addToStorage(label, url, PAR_URL_MIN, PAR_URL_MAX);
    }

    public List<LabelTextContainer> getUrls() {
        return getFromStorage(PAR_URL_MIN, PAR_URL_MAX);
    }

    public boolean removeUrl(String url) {
        return removeFromStorage(url, PAR_URL_MIN, PAR_URL_MAX);
    }

    public void removeAllUrls() {
        removeAllFromStorage(PAR_URL_MIN, PAR_URL_MAX);
    }

    public boolean addPhoto(String photo) {
        return addToStorage("", photo, PAR_PHOTO_MIN, PAR_PHOTO_MAX);
    }

    public List<String> getPhotos() {
        return convertToTexts(getFromStorage(PAR_PHOTO_MIN, PAR_PHOTO_MAX));
    }

    public boolean removePhoto(String photo) {
        return removeFromStorage(photo, PAR_PHOTO_MIN, PAR_PHOTO_MAX);
    }

    public boolean addVideo(String video) {
        return addToStorage("", video, PAR_VIDEO_MIN, PAR_VIDEO_MAX);
    }

    public List<String> getVideos() {
        return convertToTexts(getFromStorage(PAR_VIDEO_MIN, PAR_VIDEO_MAX));
    }

    public boolean removeVideo(String video) {
        return removeFromStorage(video, PAR_VIDEO_MIN, PAR_VIDEO_MAX);
    }

    public boolean addAudio(String audio) {
        return addToStorage("", audio, PAR_AUDIO_MIN, PAR_AUDIO_MAX);
    }

    public List<String> getAudios() {
        return convertToTexts(getFromStorage(PAR_AUDIO_MIN, PAR_AUDIO_MAX));
    }

    public boolean removeAudio(String audio) {
        return removeFromStorage(audio, PAR_AUDIO_MIN, PAR_AUDIO_MAX);
    }

    public boolean addOtherFile(String filpath) {
        return addToStorage("", filpath, PAR_OTHER_FILES_MIN, PAR_OTHER_FILES_MAX);
    }

    public List<String> getOtherFiles() {
        return convertToTexts(getFromStorage(PAR_OTHER_FILES_MIN, PAR_OTHER_FILES_MAX));
    }

    public boolean removeOtherFile(String filpath) {
        return removeFromStorage(filpath, PAR_OTHER_FILES_MIN, PAR_OTHER_FILES_MAX);
    }

    public List<String> getAllAttachments() {
        List<String> result = new ArrayList();
        result.addAll(getPhotos());
        result.addAll(getAudios());
        result.addAll(getVideos());
        result.addAll(getOtherFiles());
        return result;
    }

    public int getAllAttachmentsCount() {
        return getAllAttachments().size();
    }

    private boolean addToStorage(String label, String text, int rangeFrom, int rangeTo) {
        if (text == null || text.length() == 0) {
            return false;
        }
        String item;
        if (label == null || label.length() <= 0) {
            item = text;
        } else {
            item = label + "|" + text;
        }
        int key = rangeFrom;
        while (key <= rangeTo) {
            String value = getParameter(key);
            if (value == null) {
                this.parameters.put(key, Utils.doStringToBytes(item));
                return true;
            } else if (value.equalsIgnoreCase(item)) {
                return false;
            } else {
                key++;
            }
        }
        return false;
    }

    private List<LabelTextContainer> getFromStorage(int rangeFrom, int rangeTo) {
        List<LabelTextContainer> data = new ArrayList();
        for (int key = rangeFrom; key <= rangeTo; key++) {
            String value = getParameter(key);
            if (!(value == null || value.length() == 0)) {
                data.add(new LabelTextContainer(value));
            }
        }
        return data;
    }

    private List<String> convertToTexts(List<LabelTextContainer> data) {
        List<String> result = new ArrayList();
        int m = data.size();
        for (int i = 0; i < m; i++) {
            result.add(((LabelTextContainer) data.get(i)).text);
        }
        return result;
    }

    private boolean removeFromStorage(String item, int rangeFrom, int rangeTo) {
        if (item == null || item.length() == 0) {
            return false;
        }
        for (int key = rangeFrom; key <= rangeTo; key++) {
            String value = getParameter(key);
            if (value != null && value.endsWith(item)) {
                this.parameters.remove(key);
                return true;
            }
        }
        return false;
    }

    private void removeAllFromStorage(int rangeFrom, int rangeTo) {
        for (int i = rangeFrom; i <= rangeTo; i++) {
            this.parameters.remove(i);
        }
    }

    public static String generateCallbackString(String name, String packageName, String className, String returnDataName, String returnDataValue) {
        if (packageName == null || packageName.length() == 0 || className == null || className.length() == 0) {
            Logger.logD(TAG, "generateCallbackString(" + name + ", " + packageName + ", " + className + ", " + returnDataName + ", " + returnDataValue + "), " + "invalid packageName or className parameter");
            return "";
        }
        if (name == null) {
            name = "";
        }
        if (returnDataName == null) {
            returnDataName = "";
        }
        if (returnDataValue == null) {
            returnDataValue = "";
        }
        return name + ";" + packageName + ";" + className + ";" + returnDataName + ";" + returnDataValue + ";";
    }
}
