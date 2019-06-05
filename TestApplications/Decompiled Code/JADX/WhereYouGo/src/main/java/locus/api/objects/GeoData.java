package locus.api.objects;

import java.io.IOException;
import java.util.Hashtable;
import locus.api.objects.extra.ExtraData;
import locus.api.objects.extra.ExtraStyle;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import locus.api.utils.Utils;

public abstract class GeoData extends Storable {
    private static final String TAG = "GeoData";
    public int dist;
    public ExtraData extraData;
    @Deprecated
    /* renamed from: id */
    public long f80id;
    private ReadWriteMode mReadWriteMode;
    private byte mState;
    private Hashtable<String, Object> mTags;
    protected String name;
    public ExtraStyle styleHighlight;
    public ExtraStyle styleNormal;
    public Object tag;
    protected long timeCreated;

    public enum ReadWriteMode {
        READ_ONLY,
        READ_WRITE
    }

    public GeoData() {
        setBasics();
    }

    public GeoData(DataReaderBigEndian dr) throws IOException {
        super(dr);
        setBasics();
    }

    public GeoData(byte[] data) throws IOException {
        super(data);
        setBasics();
    }

    private void setBasics() {
        setEnabled(true);
        setVisible(true);
        setSelected(false);
    }

    public boolean isEnabled() {
        return isStateValue(0);
    }

    public void setEnabled(boolean enabled) {
        setState(0, enabled);
    }

    public boolean isVisible() {
        return isEnabled() && isStateValue(1);
    }

    public void setVisible(boolean visible) {
        setState(1, visible);
    }

    public boolean isSelected() {
        return isVisible() && isStateValue(2);
    }

    public void setSelected(boolean selected) {
        setState(2, selected);
    }

    private boolean isStateValue(int position) {
        return ((this.mState >> position) & 1) == 1;
    }

    private void setState(int position, boolean value) {
        if (value) {
            this.mState = (byte) (this.mState | (1 << position));
        } else {
            this.mState = (byte) (this.mState & ((1 << position) ^ -1));
        }
    }

    /* Access modifiers changed, original: protected */
    public void readExtraData(DataReaderBigEndian dr) throws IOException {
        if (dr.readBoolean()) {
            this.extraData = new ExtraData();
            this.extraData.read(dr);
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeExtraData(DataWriterBigEndian dw) throws IOException {
        if (this.extraData == null || this.extraData.getCount() <= 0) {
            dw.writeBoolean(false);
            return;
        }
        dw.writeBoolean(true);
        dw.writeStorable(this.extraData);
    }

    /* Access modifiers changed, original: protected */
    public void readStyles(DataReaderBigEndian dr) throws IOException {
        if (dr.readBoolean()) {
            this.styleNormal = new ExtraStyle(dr);
        }
        if (dr.readBoolean()) {
            this.styleHighlight = new ExtraStyle(dr);
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeStyles(DataWriterBigEndian dw) throws IOException {
        if (this.styleNormal != null) {
            dw.writeBoolean(true);
            dw.writeStorable(this.styleNormal);
        } else {
            dw.writeBoolean(false);
        }
        if (this.styleHighlight != null) {
            dw.writeBoolean(true);
            dw.writeStorable(this.styleHighlight);
            return;
        }
        dw.writeBoolean(false);
    }

    public long getId() {
        return this.f80id;
    }

    public void setId(long id) {
        this.f80id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Object getTag(String key) {
        if (key == null || key.length() == 0) {
            Logger.logW(TAG, "getTag(" + key + "), " + "invalid key");
            return null;
        } else if (this.mTags != null) {
            return this.mTags.get(key);
        } else {
            return null;
        }
    }

    public void setTag(String key, Object value) {
        if (key == null || key.length() == 0) {
            Logger.logW(TAG, "setTag(" + key + "), " + "invalid key");
        } else if (value != null) {
            if (this.mTags == null) {
                this.mTags = new Hashtable();
            }
            this.mTags.put(key, value);
        } else if (this.mTags != null) {
            this.mTags.remove(key);
        }
    }

    public boolean hasExtraData() {
        return this.extraData != null;
    }

    public byte[] getExtraData() {
        try {
            DataWriterBigEndian dw = new DataWriterBigEndian();
            writeExtraData(dw);
            return dw.toByteArray();
        } catch (IOException e) {
            Logger.logE(TAG, "getExtraDataRaw()", e);
            return null;
        }
    }

    public void setExtraData(byte[] data) {
        try {
            readExtraData(new DataReaderBigEndian(data));
        } catch (Exception e) {
            Logger.logE(TAG, "setExtraData(" + data + ")", e);
            this.extraData = null;
        }
    }

    public boolean addParameter(int paramId, String param) {
        return afterItemAdded(this.extraData.addParameter(paramId, param), createExtraData());
    }

    public boolean addParameter(int paramId, byte[] param) {
        return afterItemAdded(this.extraData.addParameter(paramId, param), createExtraData());
    }

    public boolean addParameter(int paramId, int value) {
        return afterItemAdded(this.extraData.addParameter(paramId, Integer.toString(value)), createExtraData());
    }

    public boolean addParameter(int paramId, byte param) {
        return afterItemAdded(this.extraData.addParameter(paramId, param), createExtraData());
    }

    public String getParameter(int paramId) {
        if (this.extraData == null) {
            return null;
        }
        return this.extraData.getParameter(paramId);
    }

    public byte[] getParameterRaw(int paramId) {
        if (this.extraData == null) {
            return null;
        }
        return this.extraData.getParameterRaw(paramId);
    }

    public boolean hasParameter(int paramId) {
        if (this.extraData == null) {
            return false;
        }
        return this.extraData.hasParameter(paramId);
    }

    public String removeParameter(int paramId) {
        if (this.extraData == null) {
            return null;
        }
        return this.extraData.removeParameter(paramId);
    }

    public byte getParameterSource() {
        if (this.extraData == null) {
            return ExtraData.SOURCE_UNKNOWN;
        }
        byte[] res = this.extraData.getParameterRaw(0);
        if (res == null || res.length != 1) {
            return ExtraData.SOURCE_UNKNOWN;
        }
        return res[0];
    }

    public void setParameterSource(byte source) {
        addParameter(0, source);
    }

    public boolean isParameterSource(byte expectedSource) {
        return getParameterSource() == expectedSource;
    }

    public void removeParameterSource() {
        removeParameter(0);
    }

    public String getParameterStyleName() {
        if (this.extraData == null) {
            return "";
        }
        return this.extraData.getParameter(5);
    }

    public void setParameterStyleName(String style) {
        addParameter(5, style);
    }

    public void removeParameterStyleName() {
        if (this.extraData != null) {
            this.extraData.removeParameter(5);
        }
    }

    public boolean hasParameterDescription() {
        return getParameterDescription().length() > 0;
    }

    public String getParameterDescription() {
        if (this.extraData == null) {
            return "";
        }
        return this.extraData.getParameterNotNull(30);
    }

    public void setParameterDescription(String desc) {
        addParameter(30, desc);
    }

    public void addEmail(String email) {
        addEmail(null, email);
    }

    public void addEmail(String label, String email) {
        afterItemAdded(this.extraData.addEmail(label, email), createExtraData());
    }

    public void addPhone(String phone) {
        addPhone(null, phone);
    }

    public void addPhone(String label, String phone) {
        afterItemAdded(this.extraData.addPhone(label, phone), createExtraData());
    }

    public void addUrl(String url) {
        addUrl(null, url);
    }

    public void addUrl(String label, String url) {
        afterItemAdded(this.extraData.addUrl(label, url), createExtraData());
    }

    public boolean addAttachmentAudio(String uri) {
        return afterItemAdded(this.extraData.addAudio(uri), createExtraData());
    }

    public boolean addAttachmentPhoto(String uri) {
        return afterItemAdded(this.extraData.addPhoto(uri), createExtraData());
    }

    public boolean addAttachmentVideo(String uri) {
        return afterItemAdded(this.extraData.addVideo(uri), createExtraData());
    }

    public boolean addAttachmentOther(String uri) {
        return afterItemAdded(this.extraData.addOtherFile(uri), createExtraData());
    }

    public int getParamRteIndex() {
        String parIndex = getParameter(100);
        if (parIndex != null) {
            return Utils.parseInt(parIndex);
        }
        return -1;
    }

    public byte[] getStyles() {
        try {
            DataWriterBigEndian dw = new DataWriterBigEndian();
            writeStyles(dw);
            return dw.toByteArray();
        } catch (IOException e) {
            Logger.logE(TAG, "getStylesRaw()", e);
            return null;
        }
    }

    public void setStyles(byte[] data) {
        try {
            readStyles(new DataReaderBigEndian(data));
        } catch (Exception e) {
            Logger.logE(TAG, "setExtraStyle(" + data + ")", e);
            this.extraData = null;
        }
    }

    public ReadWriteMode getReadWriteMode() {
        if (this.mReadWriteMode == null) {
            return ReadWriteMode.READ_WRITE;
        }
        return this.mReadWriteMode;
    }

    public void setReadWriteMode(ReadWriteMode mode) {
        this.mReadWriteMode = mode;
    }

    private boolean createExtraData() {
        if (this.extraData != null) {
            return false;
        }
        this.extraData = new ExtraData();
        return true;
    }

    private boolean afterItemAdded(boolean added, boolean created) {
        if (added) {
            return true;
        }
        if (created) {
            this.extraData = null;
        }
        return false;
    }
}
