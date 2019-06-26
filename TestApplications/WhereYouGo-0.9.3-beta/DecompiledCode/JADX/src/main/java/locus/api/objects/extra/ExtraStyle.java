package locus.api.objects.extra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import locus.api.objects.Storable;
import locus.api.objects.extra.KmlVec2.Units;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class ExtraStyle extends Storable {
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

    public static class BalloonStyle extends Storable {
        public int bgColor;
        public DisplayMode displayMode;
        public String text;
        public int textColor;

        public enum DisplayMode {
            DEFAULT,
            HIDE
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.bgColor = -1;
            this.textColor = -16777216;
            this.text = "";
            this.displayMode = DisplayMode.DEFAULT;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dr) throws IOException {
            this.bgColor = dr.readInt();
            this.textColor = dr.readInt();
            this.text = dr.readString();
            int mode = dr.readInt();
            if (mode < DisplayMode.values().length) {
                this.displayMode = DisplayMode.values()[mode];
            }
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(this.bgColor);
            dw.writeInt(this.textColor);
            dw.writeString(this.text);
            dw.writeInt(this.displayMode.ordinal());
        }
    }

    public static class IconStyle extends Storable {
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

        public void setScale(float scale) {
            if (scale != 0.0f) {
                this.mScale = scale;
                this.scaleCurrent = scale;
            }
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.color = -1;
            this.mScale = 1.0f;
            this.heading = 0.0f;
            this.iconHref = null;
            this.hotSpot = ExtraStyle.generateDefaultHotSpot();
            this.icon = null;
            this.iconW = -1;
            this.iconH = -1;
            this.scaleCurrent = 1.0f;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dr) throws IOException {
            this.color = dr.readInt();
            this.mScale = dr.readFloat();
            this.heading = dr.readFloat();
            this.iconHref = dr.readString();
            this.hotSpot = KmlVec2.read(dr);
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(this.color);
            dw.writeFloat(this.mScale);
            dw.writeFloat(this.heading);
            dw.writeString(this.iconHref);
            this.hotSpot.write(dw);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
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
    }

    public static class LabelStyle extends Storable {
        private int mColor = -1;
        private float mScale = 1.0f;

        public int getColor() {
            return this.mColor;
        }

        public void setColor(int color) {
            this.mColor = color;
        }

        public float getScale() {
            return this.mScale;
        }

        public void setScale(float scale) {
            if (scale < 0.0f) {
                scale = 0.0f;
            }
            this.mScale = scale;
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.mColor = -1;
            this.mScale = 1.0f;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dr) throws IOException {
            this.mColor = dr.readInt();
            this.mScale = dr.readFloat();
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(this.mColor);
            dw.writeFloat(this.mScale);
        }
    }

    public static class LineStyle extends Storable {
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

        public enum ColorStyle {
            SIMPLE,
            BY_SPEED,
            BY_ALTITUDE,
            BY_ACCURACY,
            BY_SPEED_CHANGE,
            BY_ALTITUDE_CHANGE,
            BY_HRM,
            BY_CADENCE
        }

        public enum LineType {
            NORMAL,
            DOTTED,
            DASHED_1,
            DASHED_2,
            DASHED_3,
            SPECIAL_1,
            SPECIAL_2,
            SPECIAL_3,
            ARROW_1,
            ARROW_2,
            ARROW_3,
            CROSS_1,
            CROSS_2
        }

        public enum Units {
            PIXELS,
            METRES
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 1;
        }

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

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dis) throws IOException {
            this.color = dis.readInt();
            this.width = dis.readFloat();
            this.gxOuterColor = dis.readInt();
            this.gxOuterWidth = dis.readFloat();
            this.gxPhysicalWidth = dis.readFloat();
            this.gxLabelVisibility = dis.readBoolean();
            int cs = dis.readInt();
            if (cs < ColorStyle.values().length) {
                this.colorStyle = ColorStyle.values()[cs];
            }
            int un = dis.readInt();
            if (un < Units.values().length) {
                this.units = Units.values()[un];
            }
            int lt = dis.readInt();
            if (lt < LineType.values().length) {
                this.lineType = LineType.values()[lt];
            }
            if (version >= 1) {
                this.drawOutline = dis.readBoolean();
                this.colorOutline = dis.readInt();
            }
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(this.color);
            dw.writeFloat(this.width);
            dw.writeInt(this.gxOuterColor);
            dw.writeFloat(this.gxOuterWidth);
            dw.writeFloat(this.gxPhysicalWidth);
            dw.writeBoolean(this.gxLabelVisibility);
            dw.writeInt(this.colorStyle.ordinal());
            dw.writeInt(this.units.ordinal());
            dw.writeInt(this.lineType.ordinal());
            dw.writeBoolean(this.drawOutline);
            dw.writeInt(this.colorOutline);
        }
    }

    public static class ListStyle extends Storable {
        public int bgColor = -1;
        public ArrayList<ItemIcon> itemIcons = new ArrayList();
        public ListItemType listItemType = ListItemType.CHECK;

        public static class ItemIcon {
            public String href = "";
            public State state = State.OPEN;

            public enum State {
                OPEN,
                CLOSED,
                ERROR,
                FETCHING0,
                FETCHING1,
                FETCHING2
            }
        }

        public enum ListItemType {
            CHECK,
            CHECK_OFF_ONLY,
            CHECK_HIDE_CHILDREN,
            RADIO_FOLDER
        }

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.listItemType = ListItemType.CHECK;
            this.bgColor = -1;
            this.itemIcons = new ArrayList();
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dis) throws IOException {
            int style = dis.readInt();
            if (style < ListItemType.values().length) {
                this.listItemType = ListItemType.values()[style];
            }
            this.bgColor = dis.readInt();
            int itemsCount = dis.readInt();
            for (int i = 0; i < itemsCount; i++) {
                ItemIcon itemIcon = new ItemIcon();
                int iconStyle = dis.readInt();
                if (iconStyle < State.values().length) {
                    itemIcon.state = State.values()[iconStyle];
                }
                itemIcon.href = dis.readString();
                this.itemIcons.add(itemIcon);
            }
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeBoolean(true);
            dw.writeInt(this.listItemType.ordinal());
            dw.writeInt(this.bgColor);
            dw.writeInt(this.itemIcons.size());
            Iterator it = this.itemIcons.iterator();
            while (it.hasNext()) {
                ItemIcon itemIcon = (ItemIcon) it.next();
                dw.writeInt(itemIcon.state.ordinal());
                dw.writeString(itemIcon.href);
            }
        }
    }

    public static class PolyStyle extends Storable {
        public int color = -1;
        public boolean fill = true;
        public boolean outline = true;

        /* Access modifiers changed, original: protected */
        public int getVersion() {
            return 0;
        }

        public void reset() {
            this.color = -1;
            this.fill = true;
            this.outline = true;
        }

        /* Access modifiers changed, original: protected */
        public void readObject(int version, DataReaderBigEndian dis) throws IOException {
            this.color = dis.readInt();
            this.fill = dis.readBoolean();
            this.outline = dis.readBoolean();
        }

        /* Access modifiers changed, original: protected */
        public void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(this.color);
            dw.writeBoolean(this.fill);
            dw.writeBoolean(this.outline);
        }
    }

    public ExtraStyle() {
        this("");
    }

    public ExtraStyle(String name) {
        if (name != null) {
            this.mName = name;
        }
    }

    public ExtraStyle(DataReaderBigEndian dr) throws IOException {
        super(dr);
    }

    public ExtraStyle(byte[] data) throws IOException {
        super(data);
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        if (id == null) {
            id = "";
        }
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.mName = name;
    }

    public IconStyle getIconStyle() {
        return this.iconStyle;
    }

    public String getIconStyleIconUrl() {
        if (this.iconStyle == null) {
            return null;
        }
        return this.iconStyle.iconHref;
    }

    public void setIconStyle(String iconUrl, float scale) {
        setIconStyle(iconUrl, -1, 0.0f, scale);
    }

    public void setIconStyle(String iconUrl, int color, float heading, float scale) {
        this.iconStyle = new IconStyle();
        this.iconStyle.iconHref = iconUrl;
        this.iconStyle.color = color;
        this.iconStyle.heading = heading;
        this.iconStyle.setScale(scale);
        setIconStyleHotSpot(0);
    }

    public void setIconStyleHotSpot(int hotspot) {
        if (this.iconStyle == null) {
            Logger.logW(TAG, "setIconStyleHotSpot(" + hotspot + "), " + "initialize IconStyle before settings hotSpot!");
        } else if (hotspot == 1) {
            this.iconStyle.hotSpot = new KmlVec2(0.0d, Units.FRACTION, 1.0d, Units.FRACTION);
        } else if (hotspot == 2) {
            this.iconStyle.hotSpot = new KmlVec2(0.5d, Units.FRACTION, 0.5d, Units.FRACTION);
        } else {
            this.iconStyle.hotSpot = generateDefaultHotSpot();
        }
    }

    private static KmlVec2 generateDefaultHotSpot() {
        return new KmlVec2(0.5d, Units.FRACTION, 0.0d, Units.FRACTION);
    }

    public void setIconStyleHotSpot(KmlVec2 vec2) {
        if (this.iconStyle == null || vec2 == null) {
            Logger.logW(TAG, "setIconStyleHotSpot(" + vec2 + "), " + "initialize IconStyle before settings hotSpot or hotSpot is null!");
        } else {
            this.iconStyle.hotSpot = vec2;
        }
    }

    public LineStyle getLineStyle() {
        return this.lineStyle;
    }

    public void removeLineStyle() {
        this.lineStyle = null;
    }

    public void setLineStyle(int color, float width) {
        setLineStyle(ColorStyle.SIMPLE, color, width, Units.PIXELS);
    }

    public void setLineStyle(ColorStyle style, int color, float width, Units units) {
        if (this.lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        this.lineStyle.colorStyle = style;
        this.lineStyle.color = color;
        this.lineStyle.width = width;
        this.lineStyle.units = units;
    }

    public void setLineType(LineType type) {
        if (this.lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        this.lineStyle.lineType = type;
    }

    public void setLineOutline(boolean drawOutline, int colorOutline) {
        if (this.lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        this.lineStyle.drawOutline = drawOutline;
        this.lineStyle.colorOutline = colorOutline;
    }

    public PolyStyle getPolyStyle() {
        return this.polyStyle;
    }

    public void setPolyStyle(int color, boolean fill, boolean outline) {
        this.polyStyle = new PolyStyle();
        this.polyStyle.color = color;
        this.polyStyle.fill = fill;
        this.polyStyle.outline = outline;
    }

    public void removePolyStyle() {
        this.polyStyle = null;
    }

    public LabelStyle getLabelStyle() {
        return this.labelStyle;
    }

    /* Access modifiers changed, original: protected */
    public int getVersion() {
        return 1;
    }

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

    /* Access modifiers changed, original: protected */
    public void readObject(int version, DataReaderBigEndian dr) throws IOException {
        this.mId = dr.readString();
        this.mName = dr.readString();
        if (version == 0) {
            readVersion0(dr);
            return;
        }
        try {
            if (dr.readBoolean()) {
                this.balloonStyle = (BalloonStyle) Storable.read(BalloonStyle.class, dr);
            }
            if (dr.readBoolean()) {
                this.iconStyle = (IconStyle) Storable.read(IconStyle.class, dr);
            }
            if (dr.readBoolean()) {
                this.labelStyle = (LabelStyle) Storable.read(LabelStyle.class, dr);
            }
            if (dr.readBoolean()) {
                this.lineStyle = (LineStyle) Storable.read(LineStyle.class, dr);
            }
            if (dr.readBoolean()) {
                this.listStyle = (ListStyle) Storable.read(ListStyle.class, dr);
            }
            if (dr.readBoolean()) {
                this.polyStyle = (PolyStyle) Storable.read(PolyStyle.class, dr);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void readVersion0(DataReaderBigEndian dr) throws IOException {
        if (dr.readBoolean()) {
            this.balloonStyle = new BalloonStyle();
            this.balloonStyle.bgColor = dr.readInt();
            this.balloonStyle.textColor = dr.readInt();
            this.balloonStyle.text = dr.readString();
            int displayMode = dr.readInt();
            if (displayMode < DisplayMode.values().length) {
                this.balloonStyle.displayMode = DisplayMode.values()[displayMode];
            }
        }
        if (dr.readBoolean()) {
            this.iconStyle = new IconStyle();
            this.iconStyle.color = dr.readInt();
            this.iconStyle.setScale(dr.readFloat());
            this.iconStyle.heading = dr.readFloat();
            this.iconStyle.iconHref = dr.readString();
            this.iconStyle.hotSpot = KmlVec2.read(dr);
        }
        if (dr.readBoolean()) {
            this.labelStyle = new LabelStyle();
            this.labelStyle.setColor(dr.readInt());
            this.labelStyle.setScale(dr.readFloat());
        }
        if (dr.readBoolean()) {
            this.lineStyle = new LineStyle();
            this.lineStyle.color = dr.readInt();
            this.lineStyle.width = dr.readFloat();
            this.lineStyle.gxOuterColor = dr.readInt();
            this.lineStyle.gxOuterWidth = dr.readFloat();
            this.lineStyle.gxPhysicalWidth = dr.readFloat();
            this.lineStyle.gxLabelVisibility = dr.readBoolean();
            int colorStyle = dr.readInt();
            if (colorStyle < ColorStyle.values().length) {
                this.lineStyle.colorStyle = ColorStyle.values()[colorStyle];
            }
            int units = dr.readInt();
            if (units < Units.values().length) {
                this.lineStyle.units = Units.values()[units];
            }
        }
        if (dr.readBoolean()) {
            this.listStyle = new ListStyle();
            int listItemStyle = dr.readInt();
            if (listItemStyle < ListItemType.values().length) {
                this.listStyle.listItemType = ListItemType.values()[listItemStyle];
            }
            this.listStyle.bgColor = dr.readInt();
            int itemsCount = dr.readInt();
            for (int i = 0; i < itemsCount; i++) {
                ItemIcon itemIcon = new ItemIcon();
                int iconStyle = dr.readInt();
                if (iconStyle < State.values().length) {
                    itemIcon.state = State.values()[iconStyle];
                }
                itemIcon.href = dr.readString();
                this.listStyle.itemIcons.add(itemIcon);
            }
        }
        if (dr.readBoolean()) {
            this.polyStyle = new PolyStyle();
            this.polyStyle.color = dr.readInt();
            this.polyStyle.fill = dr.readBoolean();
            this.polyStyle.outline = dr.readBoolean();
        }
    }

    /* Access modifiers changed, original: protected */
    public void writeObject(DataWriterBigEndian dw) throws IOException {
        dw.writeString(this.mId);
        dw.writeString(this.mName);
        if (this.balloonStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            this.balloonStyle.write(dw);
        }
        if (this.iconStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            this.iconStyle.write(dw);
        }
        if (this.labelStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            this.labelStyle.write(dw);
        }
        if (this.lineStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            this.lineStyle.write(dw);
        }
        if (this.listStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            this.listStyle.write(dw);
        }
        if (this.polyStyle == null) {
            dw.writeBoolean(false);
            return;
        }
        dw.writeBoolean(true);
        this.polyStyle.write(dw);
    }
}
