// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.objects.extra;

import java.util.Iterator;
import java.util.ArrayList;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.objects.Storable;

public class ExtraStyle extends Storable
{
    public static final int BLACK = -16777216;
    public static final int COLOR_DEFAULT = -1;
    public static final int HOTSPOT_BOTTOM_CENTER = 0;
    public static final int HOTSPOT_CENTER_CENTER = 2;
    public static final int HOTSPOT_TOP_LEFT = 1;
    private static final String TAG = "ExtraStyle";
    public static final int WHITE = -1;
    BalloonStyle balloonStyle;
    IconStyle iconStyle;
    LabelStyle labelStyle;
    LineStyle lineStyle;
    ListStyle listStyle;
    private String mId;
    private String mName;
    PolyStyle polyStyle;
    
    public ExtraStyle() {
        this("");
    }
    
    public ExtraStyle(final String mName) {
        if (mName != null) {
            this.mName = mName;
        }
    }
    
    public ExtraStyle(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        super(dataReaderBigEndian);
    }
    
    public ExtraStyle(final byte[] array) throws IOException {
        super(array);
    }
    
    private static KmlVec2 generateDefaultHotSpot() {
        return new KmlVec2(0.5, KmlVec2.Units.FRACTION, 0.0, KmlVec2.Units.FRACTION);
    }
    
    private void readVersion0(final DataReaderBigEndian dataReaderBigEndian) throws IOException {
        if (dataReaderBigEndian.readBoolean()) {
            this.balloonStyle = new BalloonStyle();
            this.balloonStyle.bgColor = dataReaderBigEndian.readInt();
            this.balloonStyle.textColor = dataReaderBigEndian.readInt();
            this.balloonStyle.text = dataReaderBigEndian.readString();
            final int int1 = dataReaderBigEndian.readInt();
            if (int1 < DisplayMode.values().length) {
                this.balloonStyle.displayMode = DisplayMode.values()[int1];
            }
        }
        if (dataReaderBigEndian.readBoolean()) {
            this.iconStyle = new IconStyle();
            this.iconStyle.color = dataReaderBigEndian.readInt();
            this.iconStyle.setScale(dataReaderBigEndian.readFloat());
            this.iconStyle.heading = dataReaderBigEndian.readFloat();
            this.iconStyle.iconHref = dataReaderBigEndian.readString();
            this.iconStyle.hotSpot = KmlVec2.read(dataReaderBigEndian);
        }
        if (dataReaderBigEndian.readBoolean()) {
            (this.labelStyle = new LabelStyle()).setColor(dataReaderBigEndian.readInt());
            this.labelStyle.setScale(dataReaderBigEndian.readFloat());
        }
        if (dataReaderBigEndian.readBoolean()) {
            this.lineStyle = new LineStyle();
            this.lineStyle.color = dataReaderBigEndian.readInt();
            this.lineStyle.width = dataReaderBigEndian.readFloat();
            this.lineStyle.gxOuterColor = dataReaderBigEndian.readInt();
            this.lineStyle.gxOuterWidth = dataReaderBigEndian.readFloat();
            this.lineStyle.gxPhysicalWidth = dataReaderBigEndian.readFloat();
            this.lineStyle.gxLabelVisibility = dataReaderBigEndian.readBoolean();
            final int int2 = dataReaderBigEndian.readInt();
            if (int2 < ColorStyle.values().length) {
                this.lineStyle.colorStyle = ColorStyle.values()[int2];
            }
            final int int3 = dataReaderBigEndian.readInt();
            if (int3 < Units.values().length) {
                this.lineStyle.units = Units.values()[int3];
            }
        }
        if (dataReaderBigEndian.readBoolean()) {
            this.listStyle = new ListStyle();
            final int int4 = dataReaderBigEndian.readInt();
            if (int4 < ListItemType.values().length) {
                this.listStyle.listItemType = ListItemType.values()[int4];
            }
            this.listStyle.bgColor = dataReaderBigEndian.readInt();
            for (int int5 = dataReaderBigEndian.readInt(), i = 0; i < int5; ++i) {
                final ItemIcon e = new ItemIcon();
                final int int6 = dataReaderBigEndian.readInt();
                if (int6 < State.values().length) {
                    e.state = State.values()[int6];
                }
                e.href = dataReaderBigEndian.readString();
                this.listStyle.itemIcons.add(e);
            }
        }
        if (dataReaderBigEndian.readBoolean()) {
            this.polyStyle = new PolyStyle();
            this.polyStyle.color = dataReaderBigEndian.readInt();
            this.polyStyle.fill = dataReaderBigEndian.readBoolean();
            this.polyStyle.outline = dataReaderBigEndian.readBoolean();
        }
    }
    
    public IconStyle getIconStyle() {
        return this.iconStyle;
    }
    
    public String getIconStyleIconUrl() {
        String iconHref;
        if (this.iconStyle == null) {
            iconHref = null;
        }
        else {
            iconHref = this.iconStyle.iconHref;
        }
        return iconHref;
    }
    
    public String getId() {
        return this.mId;
    }
    
    public LabelStyle getLabelStyle() {
        return this.labelStyle;
    }
    
    public LineStyle getLineStyle() {
        return this.lineStyle;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public PolyStyle getPolyStyle() {
        return this.polyStyle;
    }
    
    @Override
    protected int getVersion() {
        return 1;
    }
    
    @Override
    protected void readObject(final int n, final DataReaderBigEndian ex) throws IOException {
        this.mId = ((DataReaderBigEndian)ex).readString();
        this.mName = ((DataReaderBigEndian)ex).readString();
        if (n == 0) {
            this.readVersion0((DataReaderBigEndian)ex);
        }
        else {
            try {
                if (((DataReaderBigEndian)ex).readBoolean()) {
                    this.balloonStyle = (BalloonStyle)Storable.read(BalloonStyle.class, (DataReaderBigEndian)ex);
                }
                if (((DataReaderBigEndian)ex).readBoolean()) {
                    this.iconStyle = (IconStyle)Storable.read(IconStyle.class, (DataReaderBigEndian)ex);
                }
                if (((DataReaderBigEndian)ex).readBoolean()) {
                    this.labelStyle = (LabelStyle)Storable.read(LabelStyle.class, (DataReaderBigEndian)ex);
                }
                if (((DataReaderBigEndian)ex).readBoolean()) {
                    this.lineStyle = (LineStyle)Storable.read(LineStyle.class, (DataReaderBigEndian)ex);
                }
                if (((DataReaderBigEndian)ex).readBoolean()) {
                    this.listStyle = (ListStyle)Storable.read(ListStyle.class, (DataReaderBigEndian)ex);
                }
                if (((DataReaderBigEndian)ex).readBoolean()) {
                    this.polyStyle = (PolyStyle)Storable.read(PolyStyle.class, (DataReaderBigEndian)ex);
                }
            }
            catch (InstantiationException ex2) {}
            catch (IllegalAccessException ex) {
                goto Label_0150;
            }
        }
    }
    
    public void removeLineStyle() {
        this.lineStyle = null;
    }
    
    public void removePolyStyle() {
        this.polyStyle = null;
    }
    
    @Override
    public void reset() {
        this.mId = "";
        this.mName = "";
        this.balloonStyle = null;
        this.iconStyle = null;
        this.labelStyle = null;
        this.lineStyle = null;
        this.listStyle = null;
        this.polyStyle = null;
    }
    
    public void setIconStyle(final String s, final float n) {
        this.setIconStyle(s, -1, 0.0f, n);
    }
    
    public void setIconStyle(final String iconHref, final int color, final float heading, final float scale) {
        this.iconStyle = new IconStyle();
        this.iconStyle.iconHref = iconHref;
        this.iconStyle.color = color;
        this.iconStyle.heading = heading;
        this.iconStyle.setScale(scale);
        this.setIconStyleHotSpot(0);
    }
    
    public void setIconStyleHotSpot(final int i) {
        if (this.iconStyle == null) {
            Logger.logW("ExtraStyle", "setIconStyleHotSpot(" + i + "), " + "initialize IconStyle before settings hotSpot!");
        }
        else if (i == 1) {
            this.iconStyle.hotSpot = new KmlVec2(0.0, KmlVec2.Units.FRACTION, 1.0, KmlVec2.Units.FRACTION);
        }
        else if (i == 2) {
            this.iconStyle.hotSpot = new KmlVec2(0.5, KmlVec2.Units.FRACTION, 0.5, KmlVec2.Units.FRACTION);
        }
        else {
            this.iconStyle.hotSpot = generateDefaultHotSpot();
        }
    }
    
    public void setIconStyleHotSpot(final KmlVec2 kmlVec2) {
        if (this.iconStyle == null || kmlVec2 == null) {
            Logger.logW("ExtraStyle", "setIconStyleHotSpot(" + kmlVec2 + "), " + "initialize IconStyle before settings hotSpot or hotSpot is null!");
        }
        else {
            this.iconStyle.hotSpot = kmlVec2;
        }
    }
    
    public void setId(final String s) {
        String mId = s;
        if (s == null) {
            mId = "";
        }
        this.mId = mId;
    }
    
    public void setLineOutline(final boolean drawOutline, final int colorOutline) {
        if (this.lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        this.lineStyle.drawOutline = drawOutline;
        this.lineStyle.colorOutline = colorOutline;
    }
    
    public void setLineStyle(final int n, final float n2) {
        this.setLineStyle(ColorStyle.SIMPLE, n, n2, Units.PIXELS);
    }
    
    public void setLineStyle(final ColorStyle colorStyle, final int color, final float width, final Units units) {
        if (this.lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        this.lineStyle.colorStyle = colorStyle;
        this.lineStyle.color = color;
        this.lineStyle.width = width;
        this.lineStyle.units = units;
    }
    
    public void setLineType(final LineType lineType) {
        if (this.lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        this.lineStyle.lineType = lineType;
    }
    
    public void setName(final String s) {
        String mName = s;
        if (s == null) {
            mName = "";
        }
        this.mName = mName;
    }
    
    public void setPolyStyle(final int color, final boolean fill, final boolean outline) {
        this.polyStyle = new PolyStyle();
        this.polyStyle.color = color;
        this.polyStyle.fill = fill;
        this.polyStyle.outline = outline;
    }
    
    @Override
    protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
        dataWriterBigEndian.writeString(this.mId);
        dataWriterBigEndian.writeString(this.mName);
        if (this.balloonStyle == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            this.balloonStyle.write(dataWriterBigEndian);
        }
        if (this.iconStyle == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            this.iconStyle.write(dataWriterBigEndian);
        }
        if (this.labelStyle == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            this.labelStyle.write(dataWriterBigEndian);
        }
        if (this.lineStyle == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            this.lineStyle.write(dataWriterBigEndian);
        }
        if (this.listStyle == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            this.listStyle.write(dataWriterBigEndian);
        }
        if (this.polyStyle == null) {
            dataWriterBigEndian.writeBoolean(false);
        }
        else {
            dataWriterBigEndian.writeBoolean(true);
            this.polyStyle.write(dataWriterBigEndian);
        }
    }
    
    public static class BalloonStyle extends Storable
    {
        public int bgColor;
        public DisplayMode displayMode;
        public String text;
        public int textColor;
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        @Override
        protected void readObject(int int1, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.bgColor = dataReaderBigEndian.readInt();
            this.textColor = dataReaderBigEndian.readInt();
            this.text = dataReaderBigEndian.readString();
            int1 = dataReaderBigEndian.readInt();
            if (int1 < DisplayMode.values().length) {
                this.displayMode = DisplayMode.values()[int1];
            }
        }
        
        @Override
        public void reset() {
            this.bgColor = -1;
            this.textColor = -16777216;
            this.text = "";
            this.displayMode = DisplayMode.DEFAULT;
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeInt(this.bgColor);
            dataWriterBigEndian.writeInt(this.textColor);
            dataWriterBigEndian.writeString(this.text);
            dataWriterBigEndian.writeInt(this.displayMode.ordinal());
        }
        
        public enum DisplayMode
        {
            DEFAULT, 
            HIDE;
        }
    }
    
    public static class IconStyle extends Storable
    {
        public int color;
        public float heading;
        public KmlVec2 hotSpot;
        public Object icon;
        public int iconH;
        public String iconHref;
        public int iconW;
        private float mScale;
        public float scaleCurrent;
        
        public float getScale() {
            return this.mScale;
        }
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        @Override
        protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.color = dataReaderBigEndian.readInt();
            this.mScale = dataReaderBigEndian.readFloat();
            this.heading = dataReaderBigEndian.readFloat();
            this.iconHref = dataReaderBigEndian.readString();
            this.hotSpot = KmlVec2.read(dataReaderBigEndian);
        }
        
        @Override
        public void reset() {
            this.color = -1;
            this.mScale = 1.0f;
            this.heading = 0.0f;
            this.iconHref = null;
            this.hotSpot = generateDefaultHotSpot();
            this.icon = null;
            this.iconW = -1;
            this.iconH = -1;
            this.scaleCurrent = 1.0f;
        }
        
        public void setScale(final float n) {
            if (n != 0.0f) {
                this.mScale = n;
                this.scaleCurrent = n;
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("IconStyle [");
            sb.append("color:").append(this.color);
            sb.append(", scale:").append(this.mScale);
            sb.append(", heading:").append(this.heading);
            sb.append(", iconHref:").append(this.iconHref);
            sb.append(", hotSpot:").append(this.hotSpot);
            sb.append(", icon:").append(this.icon);
            sb.append(", iconW:").append(this.iconW);
            sb.append(", iconH:").append(this.iconH);
            sb.append(", scaleCurrent:").append(this.scaleCurrent);
            sb.append("]");
            return sb.toString();
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeInt(this.color);
            dataWriterBigEndian.writeFloat(this.mScale);
            dataWriterBigEndian.writeFloat(this.heading);
            dataWriterBigEndian.writeString(this.iconHref);
            this.hotSpot.write(dataWriterBigEndian);
        }
    }
    
    public static class LabelStyle extends Storable
    {
        private int mColor;
        private float mScale;
        
        public LabelStyle() {
            this.mColor = -1;
            this.mScale = 1.0f;
        }
        
        public int getColor() {
            return this.mColor;
        }
        
        public float getScale() {
            return this.mScale;
        }
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        @Override
        protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.mColor = dataReaderBigEndian.readInt();
            this.mScale = dataReaderBigEndian.readFloat();
        }
        
        @Override
        public void reset() {
            this.mColor = -1;
            this.mScale = 1.0f;
        }
        
        public void setColor(final int mColor) {
            this.mColor = mColor;
        }
        
        public void setScale(final float n) {
            float mScale = n;
            if (n < 0.0f) {
                mScale = 0.0f;
            }
            this.mScale = mScale;
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeInt(this.mColor);
            dataWriterBigEndian.writeFloat(this.mScale);
        }
    }
    
    public static class LineStyle extends Storable
    {
        public int color;
        public int colorOutline;
        public ColorStyle colorStyle;
        public boolean drawOutline;
        public boolean gxLabelVisibility;
        public int gxOuterColor;
        public float gxOuterWidth;
        @Deprecated
        public float gxPhysicalWidth;
        public LineType lineType;
        public Units units;
        public float width;
        
        @Override
        protected int getVersion() {
            return 1;
        }
        
        @Override
        protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.color = dataReaderBigEndian.readInt();
            this.width = dataReaderBigEndian.readFloat();
            this.gxOuterColor = dataReaderBigEndian.readInt();
            this.gxOuterWidth = dataReaderBigEndian.readFloat();
            this.gxPhysicalWidth = dataReaderBigEndian.readFloat();
            this.gxLabelVisibility = dataReaderBigEndian.readBoolean();
            final int int1 = dataReaderBigEndian.readInt();
            if (int1 < ColorStyle.values().length) {
                this.colorStyle = ColorStyle.values()[int1];
            }
            final int int2 = dataReaderBigEndian.readInt();
            if (int2 < Units.values().length) {
                this.units = Units.values()[int2];
            }
            final int int3 = dataReaderBigEndian.readInt();
            if (int3 < LineType.values().length) {
                this.lineType = LineType.values()[int3];
            }
            if (n >= 1) {
                this.drawOutline = dataReaderBigEndian.readBoolean();
                this.colorOutline = dataReaderBigEndian.readInt();
            }
        }
        
        @Override
        public void reset() {
            this.color = -1;
            this.width = 1.0f;
            this.gxOuterColor = -1;
            this.gxOuterWidth = 0.0f;
            this.gxPhysicalWidth = 0.0f;
            this.gxLabelVisibility = false;
            this.colorStyle = ColorStyle.SIMPLE;
            this.units = Units.PIXELS;
            this.lineType = LineType.NORMAL;
            this.drawOutline = false;
            this.colorOutline = -1;
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeInt(this.color);
            dataWriterBigEndian.writeFloat(this.width);
            dataWriterBigEndian.writeInt(this.gxOuterColor);
            dataWriterBigEndian.writeFloat(this.gxOuterWidth);
            dataWriterBigEndian.writeFloat(this.gxPhysicalWidth);
            dataWriterBigEndian.writeBoolean(this.gxLabelVisibility);
            dataWriterBigEndian.writeInt(this.colorStyle.ordinal());
            dataWriterBigEndian.writeInt(this.units.ordinal());
            dataWriterBigEndian.writeInt(this.lineType.ordinal());
            dataWriterBigEndian.writeBoolean(this.drawOutline);
            dataWriterBigEndian.writeInt(this.colorOutline);
        }
        
        public enum ColorStyle
        {
            BY_ACCURACY, 
            BY_ALTITUDE, 
            BY_ALTITUDE_CHANGE, 
            BY_CADENCE, 
            BY_HRM, 
            BY_SPEED, 
            BY_SPEED_CHANGE, 
            SIMPLE;
        }
        
        public enum LineType
        {
            ARROW_1, 
            ARROW_2, 
            ARROW_3, 
            CROSS_1, 
            CROSS_2, 
            DASHED_1, 
            DASHED_2, 
            DASHED_3, 
            DOTTED, 
            NORMAL, 
            SPECIAL_1, 
            SPECIAL_2, 
            SPECIAL_3;
        }
        
        public enum Units
        {
            METRES, 
            PIXELS;
        }
    }
    
    public static class ListStyle extends Storable
    {
        public int bgColor;
        public ArrayList<ItemIcon> itemIcons;
        public ListItemType listItemType;
        
        public ListStyle() {
            this.listItemType = ListItemType.CHECK;
            this.bgColor = -1;
            this.itemIcons = new ArrayList<ItemIcon>();
        }
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        @Override
        protected void readObject(int i, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            i = dataReaderBigEndian.readInt();
            if (i < ListItemType.values().length) {
                this.listItemType = ListItemType.values()[i];
            }
            this.bgColor = dataReaderBigEndian.readInt();
            int int1;
            ItemIcon e;
            int int2;
            for (int1 = dataReaderBigEndian.readInt(), i = 0; i < int1; ++i) {
                e = new ItemIcon();
                int2 = dataReaderBigEndian.readInt();
                if (int2 < State.values().length) {
                    e.state = State.values()[int2];
                }
                e.href = dataReaderBigEndian.readString();
                this.itemIcons.add(e);
            }
        }
        
        @Override
        public void reset() {
            this.listItemType = ListItemType.CHECK;
            this.bgColor = -1;
            this.itemIcons = new ArrayList<ItemIcon>();
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeBoolean(true);
            dataWriterBigEndian.writeInt(this.listItemType.ordinal());
            dataWriterBigEndian.writeInt(this.bgColor);
            dataWriterBigEndian.writeInt(this.itemIcons.size());
            for (final ItemIcon itemIcon : this.itemIcons) {
                dataWriterBigEndian.writeInt(itemIcon.state.ordinal());
                dataWriterBigEndian.writeString(itemIcon.href);
            }
        }
        
        public static class ItemIcon
        {
            public String href;
            public State state;
            
            public ItemIcon() {
                this.state = State.OPEN;
                this.href = "";
            }
            
            public enum State
            {
                CLOSED, 
                ERROR, 
                FETCHING0, 
                FETCHING1, 
                FETCHING2, 
                OPEN;
            }
        }
        
        public enum ListItemType
        {
            CHECK, 
            CHECK_HIDE_CHILDREN, 
            CHECK_OFF_ONLY, 
            RADIO_FOLDER;
        }
    }
    
    public static class PolyStyle extends Storable
    {
        public int color;
        public boolean fill;
        public boolean outline;
        
        public PolyStyle() {
            this.color = -1;
            this.fill = true;
            this.outline = true;
        }
        
        @Override
        protected int getVersion() {
            return 0;
        }
        
        @Override
        protected void readObject(final int n, final DataReaderBigEndian dataReaderBigEndian) throws IOException {
            this.color = dataReaderBigEndian.readInt();
            this.fill = dataReaderBigEndian.readBoolean();
            this.outline = dataReaderBigEndian.readBoolean();
        }
        
        @Override
        public void reset() {
            this.color = -1;
            this.fill = true;
            this.outline = true;
        }
        
        @Override
        protected void writeObject(final DataWriterBigEndian dataWriterBigEndian) throws IOException {
            dataWriterBigEndian.writeInt(this.color);
            dataWriterBigEndian.writeBoolean(this.fill);
            dataWriterBigEndian.writeBoolean(this.outline);
        }
    }
}
