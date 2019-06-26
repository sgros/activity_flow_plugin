// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects;

import locus.api.utils.Utils;
import locus.api.utils.Logger;
import locus.api.utils.DataWriterBigEndian;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.extra.ExtraStyle;
import java.util.Hashtable;
import locus.api.objects.extra.ExtraData;

public abstract class GeoData extends Storable
{
    private static final String TAG = "GeoData";
    public int dist;
    public ExtraData extraData;
    @Deprecated
    public long id;
    private ReadWriteMode mReadWriteMode;
    private byte mState;
    private Hashtable<String, Object> mTags;
    protected String name;
    public ExtraStyle styleHighlight;
    public ExtraStyle styleNormal;
    public Object tag;
    protected long timeCreated;
    
    public GeoData() {
        this.setBasics();
    }
    
    public GeoData(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        super(dataReaderBigEndian);
        this.setBasics();
    }
    
    public GeoData(final byte[] array) throws IOException {
        super(array);
        this.setBasics();
    }
    
    private boolean afterItemAdded(final boolean b, final boolean b2) {
        boolean b3;
        if (b) {
            b3 = true;
        }
        else {
            if (b2) {
                this.extraData = null;
            }
            b3 = false;
        }
        return b3;
    }
    
    private boolean createExtraData() {
        boolean b;
        if (this.extraData == null) {
            this.extraData = new ExtraData();
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private boolean isStateValue(final int n) {
        boolean b = true;
        if ((this.mState >> n & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    private void setBasics() {
        this.setEnabled(true);
        this.setVisible(true);
        this.setSelected(false);
    }
    
    private void setState(final int n, final boolean b) {
        if (b) {
            this.mState |= (byte)(1 << n);
        }
        else {
            this.mState &= (byte)~(1 << n);
        }
    }
    
    public boolean addAttachmentAudio(final String s) {
        return this.afterItemAdded(this.extraData.addAudio(s), this.createExtraData());
    }
    
    public boolean addAttachmentOther(final String s) {
        return this.afterItemAdded(this.extraData.addOtherFile(s), this.createExtraData());
    }
    
    public boolean addAttachmentPhoto(final String s) {
        return this.afterItemAdded(this.extraData.addPhoto(s), this.createExtraData());
    }
    
    public boolean addAttachmentVideo(final String s) {
        return this.afterItemAdded(this.extraData.addVideo(s), this.createExtraData());
    }
    
    public void addEmail(final String s) {
        this.addEmail(null, s);
    }
    
    public void addEmail(final String s, final String s2) {
        this.afterItemAdded(this.extraData.addEmail(s, s2), this.createExtraData());
    }
    
    public boolean addParameter(final int n, final byte b) {
        return this.afterItemAdded(this.extraData.addParameter(n, b), this.createExtraData());
    }
    
    public boolean addParameter(final int n, final int i) {
        return this.afterItemAdded(this.extraData.addParameter(n, Integer.toString(i)), this.createExtraData());
    }
    
    public boolean addParameter(final int n, final String s) {
        return this.afterItemAdded(this.extraData.addParameter(n, s), this.createExtraData());
    }
    
    public boolean addParameter(final int n, final byte[] array) {
        return this.afterItemAdded(this.extraData.addParameter(n, array), this.createExtraData());
    }
    
    public void addPhone(final String s) {
        this.addPhone(null, s);
    }
    
    public void addPhone(final String s, final String s2) {
        this.afterItemAdded(this.extraData.addPhone(s, s2), this.createExtraData());
    }
    
    public void addUrl(final String s) {
        this.addUrl(null, s);
    }
    
    public void addUrl(final String s, final String s2) {
        this.afterItemAdded(this.extraData.addUrl(s, s2), this.createExtraData());
    }
    
    public byte[] getExtraData() {
        try {
            final DataWriterBigEndian dataWriterBigEndian = new DataWriterBigEndian();
            this.writeExtraData(dataWriterBigEndian);
            return dataWriterBigEndian.toByteArray();
        }
        catch (IOException ex) {
            Logger.logE("GeoData", "getExtraDataRaw()", ex);
            return null;
        }
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getParamRteIndex() {
        final String parameter = this.getParameter(100);
        int int1;
        if (parameter != null) {
            int1 = Utils.parseInt(parameter);
        }
        else {
            int1 = -1;
        }
        return int1;
    }
    
    public String getParameter(final int n) {
        String parameter;
        if (this.extraData == null) {
            parameter = null;
        }
        else {
            parameter = this.extraData.getParameter(n);
        }
        return parameter;
    }
    
    public String getParameterDescription() {
        String parameterNotNull;
        if (this.extraData == null) {
            parameterNotNull = "";
        }
        else {
            parameterNotNull = this.extraData.getParameterNotNull(30);
        }
        return parameterNotNull;
    }
    
    public byte[] getParameterRaw(final int n) {
        byte[] parameterRaw;
        if (this.extraData == null) {
            parameterRaw = null;
        }
        else {
            parameterRaw = this.extraData.getParameterRaw(n);
        }
        return parameterRaw;
    }
    
    public byte getParameterSource() {
        final byte b = 48;
        byte b2;
        if (this.extraData == null) {
            b2 = b;
        }
        else {
            final byte[] parameterRaw = this.extraData.getParameterRaw(0);
            b2 = b;
            if (parameterRaw != null) {
                b2 = b;
                if (parameterRaw.length == 1) {
                    b2 = parameterRaw[0];
                }
            }
        }
        return b2;
    }
    
    public String getParameterStyleName() {
        String parameter;
        if (this.extraData == null) {
            parameter = "";
        }
        else {
            parameter = this.extraData.getParameter(5);
        }
        return parameter;
    }
    
    public ReadWriteMode getReadWriteMode() {
        ReadWriteMode readWriteMode;
        if (this.mReadWriteMode == null) {
            readWriteMode = ReadWriteMode.READ_WRITE;
        }
        else {
            readWriteMode = this.mReadWriteMode;
        }
        return readWriteMode;
    }
    
    public byte[] getStyles() {
        try {
            final DataWriterBigEndian dataWriterBigEndian = new DataWriterBigEndian();
            this.writeStyles(dataWriterBigEndian);
            return dataWriterBigEndian.toByteArray();
        }
        catch (IOException ex) {
            Logger.logE("GeoData", "getStylesRaw()", ex);
            return null;
        }
    }
    
    public Object getTag(final String s) {
        Object value = null;
        if (s == null || s.length() == 0) {
            Logger.logW("GeoData", "getTag(" + s + "), " + "invalid key");
        }
        else if (this.mTags != null) {
            value = this.mTags.get(s);
        }
        return value;
    }
    
    public long getTimeCreated() {
        return this.timeCreated;
    }
    
    public boolean hasExtraData() {
        return this.extraData != null;
    }
    
    public boolean hasParameter(final int n) {
        return this.extraData != null && this.extraData.hasParameter(n);
    }
    
    public boolean hasParameterDescription() {
        return this.getParameterDescription().length() > 0;
    }
    
    public boolean isEnabled() {
        return this.isStateValue(0);
    }
    
    public boolean isParameterSource(final byte b) {
        return this.getParameterSource() == b;
    }
    
    public boolean isSelected() {
        return this.isVisible() && this.isStateValue(2);
    }
    
    public boolean isVisible() {
        boolean b = true;
        if (!this.isEnabled() || !this.isStateValue(1)) {
            b = false;
        }
        return b;
    }
    
    protected void readExtraData(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        if (dataReaderBigEndian.readBoolean()) {
            (this.extraData = new ExtraData()).read(dataReaderBigEndian);
        }
    }
    
    protected void readStyles(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        if (dataReaderBigEndian.readBoolean()) {
            this.styleNormal = new ExtraStyle(dataReaderBigEndian);
        }
        if (dataReaderBigEndian.readBoolean()) {
            this.styleHighlight = new ExtraStyle(dataReaderBigEndian);
        }
    }
    
    public String removeParameter(final int n) {
        String removeParameter;
        if (this.extraData == null) {
            removeParameter = null;
        }
        else {
            removeParameter = this.extraData.removeParameter(n);
        }
        return removeParameter;
    }
    
    public void removeParameterSource() {
        this.removeParameter(0);
    }
    
    public void removeParameterStyleName() {
        if (this.extraData != null) {
            this.extraData.removeParameter(5);
        }
    }
    
    public void setEnabled(final boolean b) {
        this.setState(0, b);
    }
    
    public void setExtraData(final byte[] obj) {
        try {
            this.readExtraData(new DataReaderBigEndian(obj));
        }
        catch (Exception ex) {
            Logger.logE("GeoData", "setExtraData(" + obj + ")", ex);
            this.extraData = null;
        }
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }
    
    public void setParameterDescription(final String s) {
        this.addParameter(30, s);
    }
    
    public void setParameterSource(final byte b) {
        this.addParameter(0, b);
    }
    
    public void setParameterStyleName(final String s) {
        this.addParameter(5, s);
    }
    
    public void setReadWriteMode(final ReadWriteMode mReadWriteMode) {
        this.mReadWriteMode = mReadWriteMode;
    }
    
    public void setSelected(final boolean b) {
        this.setState(2, b);
    }
    
    public void setStyles(final byte[] obj) {
        try {
            this.readStyles(new DataReaderBigEndian(obj));
        }
        catch (Exception ex) {
            Logger.logE("GeoData", "setExtraStyle(" + obj + ")", ex);
            this.extraData = null;
        }
    }
    
    public void setTag(final String key, final Object value) {
        if (key == null || key.length() == 0) {
            Logger.logW("GeoData", "setTag(" + key + "), " + "invalid key");
        }
        else if (value == null) {
            if (this.mTags != null) {
                this.mTags.remove(key);
            }
        }
        else {
            if (this.mTags == null) {
                this.mTags = new Hashtable<String, Object>();
            }
            this.mTags.put(key, value);
        }
    }
    
    public void setTimeCreated(final long timeCreated) {
        this.timeCreated = timeCreated;
    }
    
    public void setVisible(final boolean b) {
        this.setState(1, b);
    }
    
    protected void writeExtraData(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        if (this.extraData != null && this.extraData.getCount() > 0) {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeStorable(this.extraData);
        }
        else {
            dataWriterBigEndian.writeBoolean(false);
        }
    }
    
    protected void writeStyles(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        if (this.styleNormal != null) {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeStorable(this.styleNormal);
        }
        else {
            dataWriterBigEndian.writeBoolean(false);
        }
        if (this.styleHighlight != null) {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeStorable(this.styleHighlight);
        }
        else {
            dataWriterBigEndian.writeBoolean(false);
        }
    }
    
    public enum ReadWriteMode
    {
        READ_ONLY, 
        READ_WRITE;
    }
}
