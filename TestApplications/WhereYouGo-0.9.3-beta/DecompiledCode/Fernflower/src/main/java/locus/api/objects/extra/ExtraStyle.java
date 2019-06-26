package locus.api.objects.extra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import locus.api.objects.Storable;
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
   ExtraStyle.BalloonStyle balloonStyle;
   ExtraStyle.IconStyle iconStyle;
   ExtraStyle.LabelStyle labelStyle;
   ExtraStyle.LineStyle lineStyle;
   ExtraStyle.ListStyle listStyle;
   private String mId;
   private String mName;
   ExtraStyle.PolyStyle polyStyle;

   public ExtraStyle() {
      this("");
   }

   public ExtraStyle(String var1) {
      if (var1 != null) {
         this.mName = var1;
      }

   }

   public ExtraStyle(DataReaderBigEndian var1) throws IOException {
      super(var1);
   }

   public ExtraStyle(byte[] var1) throws IOException {
      super(var1);
   }

   private static KmlVec2 generateDefaultHotSpot() {
      return new KmlVec2(0.5D, KmlVec2.Units.FRACTION, 0.0D, KmlVec2.Units.FRACTION);
   }

   private void readVersion0(DataReaderBigEndian var1) throws IOException {
      int var2;
      if (var1.readBoolean()) {
         this.balloonStyle = new ExtraStyle.BalloonStyle();
         this.balloonStyle.bgColor = var1.readInt();
         this.balloonStyle.textColor = var1.readInt();
         this.balloonStyle.text = var1.readString();
         var2 = var1.readInt();
         if (var2 < ExtraStyle.BalloonStyle.DisplayMode.values().length) {
            this.balloonStyle.displayMode = ExtraStyle.BalloonStyle.DisplayMode.values()[var2];
         }
      }

      if (var1.readBoolean()) {
         this.iconStyle = new ExtraStyle.IconStyle();
         this.iconStyle.color = var1.readInt();
         this.iconStyle.setScale(var1.readFloat());
         this.iconStyle.heading = var1.readFloat();
         this.iconStyle.iconHref = var1.readString();
         this.iconStyle.hotSpot = KmlVec2.read(var1);
      }

      if (var1.readBoolean()) {
         this.labelStyle = new ExtraStyle.LabelStyle();
         this.labelStyle.setColor(var1.readInt());
         this.labelStyle.setScale(var1.readFloat());
      }

      if (var1.readBoolean()) {
         this.lineStyle = new ExtraStyle.LineStyle();
         this.lineStyle.color = var1.readInt();
         this.lineStyle.width = var1.readFloat();
         this.lineStyle.gxOuterColor = var1.readInt();
         this.lineStyle.gxOuterWidth = var1.readFloat();
         this.lineStyle.gxPhysicalWidth = var1.readFloat();
         this.lineStyle.gxLabelVisibility = var1.readBoolean();
         var2 = var1.readInt();
         if (var2 < ExtraStyle.LineStyle.ColorStyle.values().length) {
            this.lineStyle.colorStyle = ExtraStyle.LineStyle.ColorStyle.values()[var2];
         }

         var2 = var1.readInt();
         if (var2 < ExtraStyle.LineStyle.Units.values().length) {
            this.lineStyle.units = ExtraStyle.LineStyle.Units.values()[var2];
         }
      }

      if (var1.readBoolean()) {
         this.listStyle = new ExtraStyle.ListStyle();
         var2 = var1.readInt();
         if (var2 < ExtraStyle.ListStyle.ListItemType.values().length) {
            this.listStyle.listItemType = ExtraStyle.ListStyle.ListItemType.values()[var2];
         }

         this.listStyle.bgColor = var1.readInt();
         int var3 = var1.readInt();

         for(var2 = 0; var2 < var3; ++var2) {
            ExtraStyle.ListStyle.ItemIcon var4 = new ExtraStyle.ListStyle.ItemIcon();
            int var5 = var1.readInt();
            if (var5 < ExtraStyle.ListStyle.ItemIcon.State.values().length) {
               var4.state = ExtraStyle.ListStyle.ItemIcon.State.values()[var5];
            }

            var4.href = var1.readString();
            this.listStyle.itemIcons.add(var4);
         }
      }

      if (var1.readBoolean()) {
         this.polyStyle = new ExtraStyle.PolyStyle();
         this.polyStyle.color = var1.readInt();
         this.polyStyle.fill = var1.readBoolean();
         this.polyStyle.outline = var1.readBoolean();
      }

   }

   public ExtraStyle.IconStyle getIconStyle() {
      return this.iconStyle;
   }

   public String getIconStyleIconUrl() {
      String var1;
      if (this.iconStyle == null) {
         var1 = null;
      } else {
         var1 = this.iconStyle.iconHref;
      }

      return var1;
   }

   public String getId() {
      return this.mId;
   }

   public ExtraStyle.LabelStyle getLabelStyle() {
      return this.labelStyle;
   }

   public ExtraStyle.LineStyle getLineStyle() {
      return this.lineStyle;
   }

   public String getName() {
      return this.mName;
   }

   public ExtraStyle.PolyStyle getPolyStyle() {
      return this.polyStyle;
   }

   protected int getVersion() {
      return 1;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mId = var2.readString();
      this.mName = var2.readString();
      if (var1 == 0) {
         this.readVersion0(var2);
      } else {
         Object var5;
         try {
            if (var2.readBoolean()) {
               this.balloonStyle = (ExtraStyle.BalloonStyle)Storable.read(ExtraStyle.BalloonStyle.class, var2);
            }

            if (var2.readBoolean()) {
               this.iconStyle = (ExtraStyle.IconStyle)Storable.read(ExtraStyle.IconStyle.class, var2);
            }

            if (var2.readBoolean()) {
               this.labelStyle = (ExtraStyle.LabelStyle)Storable.read(ExtraStyle.LabelStyle.class, var2);
            }

            if (var2.readBoolean()) {
               this.lineStyle = (ExtraStyle.LineStyle)Storable.read(ExtraStyle.LineStyle.class, var2);
            }

            if (var2.readBoolean()) {
               this.listStyle = (ExtraStyle.ListStyle)Storable.read(ExtraStyle.ListStyle.class, var2);
            }

            if (var2.readBoolean()) {
               this.polyStyle = (ExtraStyle.PolyStyle)Storable.read(ExtraStyle.PolyStyle.class, var2);
            }

            return;
         } catch (InstantiationException var3) {
            var5 = var3;
         } catch (IllegalAccessException var4) {
            var5 = var4;
         }

         ((ReflectiveOperationException)var5).printStackTrace();
      }

   }

   public void removeLineStyle() {
      this.lineStyle = null;
   }

   public void removePolyStyle() {
      this.polyStyle = null;
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

   public void setIconStyle(String var1, float var2) {
      this.setIconStyle(var1, -1, 0.0F, var2);
   }

   public void setIconStyle(String var1, int var2, float var3, float var4) {
      this.iconStyle = new ExtraStyle.IconStyle();
      this.iconStyle.iconHref = var1;
      this.iconStyle.color = var2;
      this.iconStyle.heading = var3;
      this.iconStyle.setScale(var4);
      this.setIconStyleHotSpot(0);
   }

   public void setIconStyleHotSpot(int var1) {
      if (this.iconStyle == null) {
         Logger.logW("ExtraStyle", "setIconStyleHotSpot(" + var1 + "), " + "initialize IconStyle before settings hotSpot!");
      } else if (var1 == 1) {
         this.iconStyle.hotSpot = new KmlVec2(0.0D, KmlVec2.Units.FRACTION, 1.0D, KmlVec2.Units.FRACTION);
      } else if (var1 == 2) {
         this.iconStyle.hotSpot = new KmlVec2(0.5D, KmlVec2.Units.FRACTION, 0.5D, KmlVec2.Units.FRACTION);
      } else {
         this.iconStyle.hotSpot = generateDefaultHotSpot();
      }

   }

   public void setIconStyleHotSpot(KmlVec2 var1) {
      if (this.iconStyle != null && var1 != null) {
         this.iconStyle.hotSpot = var1;
      } else {
         Logger.logW("ExtraStyle", "setIconStyleHotSpot(" + var1 + "), " + "initialize IconStyle before settings hotSpot or hotSpot is null!");
      }

   }

   public void setId(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mId = var2;
   }

   public void setLineOutline(boolean var1, int var2) {
      if (this.lineStyle == null) {
         this.lineStyle = new ExtraStyle.LineStyle();
      }

      this.lineStyle.drawOutline = var1;
      this.lineStyle.colorOutline = var2;
   }

   public void setLineStyle(int var1, float var2) {
      this.setLineStyle(ExtraStyle.LineStyle.ColorStyle.SIMPLE, var1, var2, ExtraStyle.LineStyle.Units.PIXELS);
   }

   public void setLineStyle(ExtraStyle.LineStyle.ColorStyle var1, int var2, float var3, ExtraStyle.LineStyle.Units var4) {
      if (this.lineStyle == null) {
         this.lineStyle = new ExtraStyle.LineStyle();
      }

      this.lineStyle.colorStyle = var1;
      this.lineStyle.color = var2;
      this.lineStyle.width = var3;
      this.lineStyle.units = var4;
   }

   public void setLineType(ExtraStyle.LineStyle.LineType var1) {
      if (this.lineStyle == null) {
         this.lineStyle = new ExtraStyle.LineStyle();
      }

      this.lineStyle.lineType = var1;
   }

   public void setName(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mName = var2;
   }

   public void setPolyStyle(int var1, boolean var2, boolean var3) {
      this.polyStyle = new ExtraStyle.PolyStyle();
      this.polyStyle.color = var1;
      this.polyStyle.fill = var2;
      this.polyStyle.outline = var3;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeString(this.mId);
      var1.writeString(this.mName);
      if (this.balloonStyle == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.balloonStyle.write(var1);
      }

      if (this.iconStyle == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.iconStyle.write(var1);
      }

      if (this.labelStyle == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.labelStyle.write(var1);
      }

      if (this.lineStyle == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.lineStyle.write(var1);
      }

      if (this.listStyle == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.listStyle.write(var1);
      }

      if (this.polyStyle == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.polyStyle.write(var1);
      }

   }

   public static class BalloonStyle extends Storable {
      public int bgColor;
      public ExtraStyle.BalloonStyle.DisplayMode displayMode;
      public String text;
      public int textColor;

      protected int getVersion() {
         return 0;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.bgColor = var2.readInt();
         this.textColor = var2.readInt();
         this.text = var2.readString();
         var1 = var2.readInt();
         if (var1 < ExtraStyle.BalloonStyle.DisplayMode.values().length) {
            this.displayMode = ExtraStyle.BalloonStyle.DisplayMode.values()[var1];
         }

      }

      public void reset() {
         this.bgColor = -1;
         this.textColor = -16777216;
         this.text = "";
         this.displayMode = ExtraStyle.BalloonStyle.DisplayMode.DEFAULT;
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeInt(this.bgColor);
         var1.writeInt(this.textColor);
         var1.writeString(this.text);
         var1.writeInt(this.displayMode.ordinal());
      }

      public static enum DisplayMode {
         DEFAULT,
         HIDE;
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

      protected int getVersion() {
         return 0;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.color = var2.readInt();
         this.mScale = var2.readFloat();
         this.heading = var2.readFloat();
         this.iconHref = var2.readString();
         this.hotSpot = KmlVec2.read(var2);
      }

      public void reset() {
         this.color = -1;
         this.mScale = 1.0F;
         this.heading = 0.0F;
         this.iconHref = null;
         this.hotSpot = ExtraStyle.generateDefaultHotSpot();
         this.icon = null;
         this.iconW = -1;
         this.iconH = -1;
         this.scaleCurrent = 1.0F;
      }

      public void setScale(float var1) {
         if (var1 != 0.0F) {
            this.mScale = var1;
            this.scaleCurrent = var1;
         }

      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("IconStyle [");
         var1.append("color:").append(this.color);
         var1.append(", scale:").append(this.mScale);
         var1.append(", heading:").append(this.heading);
         var1.append(", iconHref:").append(this.iconHref);
         var1.append(", hotSpot:").append(this.hotSpot);
         var1.append(", icon:").append(this.icon);
         var1.append(", iconW:").append(this.iconW);
         var1.append(", iconH:").append(this.iconH);
         var1.append(", scaleCurrent:").append(this.scaleCurrent);
         var1.append("]");
         return var1.toString();
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeInt(this.color);
         var1.writeFloat(this.mScale);
         var1.writeFloat(this.heading);
         var1.writeString(this.iconHref);
         this.hotSpot.write(var1);
      }
   }

   public static class LabelStyle extends Storable {
      private int mColor = -1;
      private float mScale = 1.0F;

      public int getColor() {
         return this.mColor;
      }

      public float getScale() {
         return this.mScale;
      }

      protected int getVersion() {
         return 0;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.mColor = var2.readInt();
         this.mScale = var2.readFloat();
      }

      public void reset() {
         this.mColor = -1;
         this.mScale = 1.0F;
      }

      public void setColor(int var1) {
         this.mColor = var1;
      }

      public void setScale(float var1) {
         float var2 = var1;
         if (var1 < 0.0F) {
            var2 = 0.0F;
         }

         this.mScale = var2;
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeInt(this.mColor);
         var1.writeFloat(this.mScale);
      }
   }

   public static class LineStyle extends Storable {
      public int color;
      public int colorOutline;
      public ExtraStyle.LineStyle.ColorStyle colorStyle;
      public boolean drawOutline;
      public boolean gxLabelVisibility;
      public int gxOuterColor;
      public float gxOuterWidth;
      @Deprecated
      public float gxPhysicalWidth;
      public ExtraStyle.LineStyle.LineType lineType;
      public ExtraStyle.LineStyle.Units units;
      public float width;

      protected int getVersion() {
         return 1;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.color = var2.readInt();
         this.width = var2.readFloat();
         this.gxOuterColor = var2.readInt();
         this.gxOuterWidth = var2.readFloat();
         this.gxPhysicalWidth = var2.readFloat();
         this.gxLabelVisibility = var2.readBoolean();
         int var3 = var2.readInt();
         if (var3 < ExtraStyle.LineStyle.ColorStyle.values().length) {
            this.colorStyle = ExtraStyle.LineStyle.ColorStyle.values()[var3];
         }

         var3 = var2.readInt();
         if (var3 < ExtraStyle.LineStyle.Units.values().length) {
            this.units = ExtraStyle.LineStyle.Units.values()[var3];
         }

         var3 = var2.readInt();
         if (var3 < ExtraStyle.LineStyle.LineType.values().length) {
            this.lineType = ExtraStyle.LineStyle.LineType.values()[var3];
         }

         if (var1 >= 1) {
            this.drawOutline = var2.readBoolean();
            this.colorOutline = var2.readInt();
         }

      }

      public void reset() {
         this.color = -1;
         this.width = 1.0F;
         this.gxOuterColor = -1;
         this.gxOuterWidth = 0.0F;
         this.gxPhysicalWidth = 0.0F;
         this.gxLabelVisibility = false;
         this.colorStyle = ExtraStyle.LineStyle.ColorStyle.SIMPLE;
         this.units = ExtraStyle.LineStyle.Units.PIXELS;
         this.lineType = ExtraStyle.LineStyle.LineType.NORMAL;
         this.drawOutline = false;
         this.colorOutline = -1;
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeInt(this.color);
         var1.writeFloat(this.width);
         var1.writeInt(this.gxOuterColor);
         var1.writeFloat(this.gxOuterWidth);
         var1.writeFloat(this.gxPhysicalWidth);
         var1.writeBoolean(this.gxLabelVisibility);
         var1.writeInt(this.colorStyle.ordinal());
         var1.writeInt(this.units.ordinal());
         var1.writeInt(this.lineType.ordinal());
         var1.writeBoolean(this.drawOutline);
         var1.writeInt(this.colorOutline);
      }

      public static enum ColorStyle {
         BY_ACCURACY,
         BY_ALTITUDE,
         BY_ALTITUDE_CHANGE,
         BY_CADENCE,
         BY_HRM,
         BY_SPEED,
         BY_SPEED_CHANGE,
         SIMPLE;
      }

      public static enum LineType {
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

      public static enum Units {
         METRES,
         PIXELS;
      }
   }

   public static class ListStyle extends Storable {
      public int bgColor;
      public ArrayList itemIcons;
      public ExtraStyle.ListStyle.ListItemType listItemType;

      public ListStyle() {
         this.listItemType = ExtraStyle.ListStyle.ListItemType.CHECK;
         this.bgColor = -1;
         this.itemIcons = new ArrayList();
      }

      protected int getVersion() {
         return 0;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         var1 = var2.readInt();
         if (var1 < ExtraStyle.ListStyle.ListItemType.values().length) {
            this.listItemType = ExtraStyle.ListStyle.ListItemType.values()[var1];
         }

         this.bgColor = var2.readInt();
         int var3 = var2.readInt();

         for(var1 = 0; var1 < var3; ++var1) {
            ExtraStyle.ListStyle.ItemIcon var4 = new ExtraStyle.ListStyle.ItemIcon();
            int var5 = var2.readInt();
            if (var5 < ExtraStyle.ListStyle.ItemIcon.State.values().length) {
               var4.state = ExtraStyle.ListStyle.ItemIcon.State.values()[var5];
            }

            var4.href = var2.readString();
            this.itemIcons.add(var4);
         }

      }

      public void reset() {
         this.listItemType = ExtraStyle.ListStyle.ListItemType.CHECK;
         this.bgColor = -1;
         this.itemIcons = new ArrayList();
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeBoolean(true);
         var1.writeInt(this.listItemType.ordinal());
         var1.writeInt(this.bgColor);
         var1.writeInt(this.itemIcons.size());
         Iterator var2 = this.itemIcons.iterator();

         while(var2.hasNext()) {
            ExtraStyle.ListStyle.ItemIcon var3 = (ExtraStyle.ListStyle.ItemIcon)var2.next();
            var1.writeInt(var3.state.ordinal());
            var1.writeString(var3.href);
         }

      }

      public static class ItemIcon {
         public String href;
         public ExtraStyle.ListStyle.ItemIcon.State state;

         public ItemIcon() {
            this.state = ExtraStyle.ListStyle.ItemIcon.State.OPEN;
            this.href = "";
         }

         public static enum State {
            CLOSED,
            ERROR,
            FETCHING0,
            FETCHING1,
            FETCHING2,
            OPEN;
         }
      }

      public static enum ListItemType {
         CHECK,
         CHECK_HIDE_CHILDREN,
         CHECK_OFF_ONLY,
         RADIO_FOLDER;
      }
   }

   public static class PolyStyle extends Storable {
      public int color = -1;
      public boolean fill = true;
      public boolean outline = true;

      protected int getVersion() {
         return 0;
      }

      protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
         this.color = var2.readInt();
         this.fill = var2.readBoolean();
         this.outline = var2.readBoolean();
      }

      public void reset() {
         this.color = -1;
         this.fill = true;
         this.outline = true;
      }

      protected void writeObject(DataWriterBigEndian var1) throws IOException {
         var1.writeInt(this.color);
         var1.writeBoolean(this.fill);
         var1.writeBoolean(this.outline);
      }
   }
}
