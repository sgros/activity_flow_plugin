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
   public long id;
   private GeoData.ReadWriteMode mReadWriteMode;
   private byte mState;
   private Hashtable mTags;
   protected String name;
   public ExtraStyle styleHighlight;
   public ExtraStyle styleNormal;
   public Object tag;
   protected long timeCreated;

   public GeoData() {
      this.setBasics();
   }

   public GeoData(DataReaderBigEndian var1) throws IOException {
      super(var1);
      this.setBasics();
   }

   public GeoData(byte[] var1) throws IOException {
      super(var1);
      this.setBasics();
   }

   private boolean afterItemAdded(boolean var1, boolean var2) {
      if (var1) {
         var1 = true;
      } else {
         if (var2) {
            this.extraData = null;
         }

         var1 = false;
      }

      return var1;
   }

   private boolean createExtraData() {
      boolean var1;
      if (this.extraData == null) {
         this.extraData = new ExtraData();
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean isStateValue(int var1) {
      boolean var2 = true;
      if ((this.mState >> var1 & 1) != 1) {
         var2 = false;
      }

      return var2;
   }

   private void setBasics() {
      this.setEnabled(true);
      this.setVisible(true);
      this.setSelected(false);
   }

   private void setState(int var1, boolean var2) {
      if (var2) {
         this.mState = (byte)((byte)(this.mState | 1 << var1));
      } else {
         this.mState = (byte)((byte)(this.mState & ~(1 << var1)));
      }

   }

   public boolean addAttachmentAudio(String var1) {
      boolean var2 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addAudio(var1), var2);
   }

   public boolean addAttachmentOther(String var1) {
      boolean var2 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addOtherFile(var1), var2);
   }

   public boolean addAttachmentPhoto(String var1) {
      boolean var2 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addPhoto(var1), var2);
   }

   public boolean addAttachmentVideo(String var1) {
      boolean var2 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addVideo(var1), var2);
   }

   public void addEmail(String var1) {
      this.addEmail((String)null, var1);
   }

   public void addEmail(String var1, String var2) {
      boolean var3 = this.createExtraData();
      this.afterItemAdded(this.extraData.addEmail(var1, var2), var3);
   }

   public boolean addParameter(int var1, byte var2) {
      boolean var3 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addParameter(var1, var2), var3);
   }

   public boolean addParameter(int var1, int var2) {
      boolean var3 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addParameter(var1, Integer.toString(var2)), var3);
   }

   public boolean addParameter(int var1, String var2) {
      boolean var3 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addParameter(var1, var2), var3);
   }

   public boolean addParameter(int var1, byte[] var2) {
      boolean var3 = this.createExtraData();
      return this.afterItemAdded(this.extraData.addParameter(var1, var2), var3);
   }

   public void addPhone(String var1) {
      this.addPhone((String)null, var1);
   }

   public void addPhone(String var1, String var2) {
      boolean var3 = this.createExtraData();
      this.afterItemAdded(this.extraData.addPhone(var1, var2), var3);
   }

   public void addUrl(String var1) {
      this.addUrl((String)null, var1);
   }

   public void addUrl(String var1, String var2) {
      boolean var3 = this.createExtraData();
      this.afterItemAdded(this.extraData.addUrl(var1, var2), var3);
   }

   public byte[] getExtraData() {
      byte[] var1;
      try {
         DataWriterBigEndian var3 = new DataWriterBigEndian();
         this.writeExtraData(var3);
         var1 = var3.toByteArray();
      } catch (IOException var2) {
         Logger.logE("GeoData", "getExtraDataRaw()", var2);
         var1 = null;
      }

      return var1;
   }

   public long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public int getParamRteIndex() {
      String var1 = this.getParameter(100);
      int var2;
      if (var1 != null) {
         var2 = Utils.parseInt(var1);
      } else {
         var2 = -1;
      }

      return var2;
   }

   public String getParameter(int var1) {
      String var2;
      if (this.extraData == null) {
         var2 = null;
      } else {
         var2 = this.extraData.getParameter(var1);
      }

      return var2;
   }

   public String getParameterDescription() {
      String var1;
      if (this.extraData == null) {
         var1 = "";
      } else {
         var1 = this.extraData.getParameterNotNull(30);
      }

      return var1;
   }

   public byte[] getParameterRaw(int var1) {
      byte[] var2;
      if (this.extraData == null) {
         var2 = null;
      } else {
         var2 = this.extraData.getParameterRaw(var1);
      }

      return var2;
   }

   public byte getParameterSource() {
      byte var1 = 48;
      byte var2;
      if (this.extraData == null) {
         var2 = var1;
      } else {
         byte[] var3 = this.extraData.getParameterRaw(0);
         var2 = var1;
         if (var3 != null) {
            var2 = var1;
            if (var3.length == 1) {
               byte var4 = var3[0];
               var2 = var4;
            }
         }
      }

      return var2;
   }

   public String getParameterStyleName() {
      String var1;
      if (this.extraData == null) {
         var1 = "";
      } else {
         var1 = this.extraData.getParameter(5);
      }

      return var1;
   }

   public GeoData.ReadWriteMode getReadWriteMode() {
      GeoData.ReadWriteMode var1;
      if (this.mReadWriteMode == null) {
         var1 = GeoData.ReadWriteMode.READ_WRITE;
      } else {
         var1 = this.mReadWriteMode;
      }

      return var1;
   }

   public byte[] getStyles() {
      byte[] var1;
      try {
         DataWriterBigEndian var3 = new DataWriterBigEndian();
         this.writeStyles(var3);
         var1 = var3.toByteArray();
      } catch (IOException var2) {
         Logger.logE("GeoData", "getStylesRaw()", var2);
         var1 = null;
      }

      return var1;
   }

   public Object getTag(String var1) {
      Object var2 = null;
      if (var1 != null && var1.length() != 0) {
         if (this.mTags != null) {
            var2 = this.mTags.get(var1);
         }
      } else {
         Logger.logW("GeoData", "getTag(" + var1 + "), " + "invalid key");
      }

      return var2;
   }

   public long getTimeCreated() {
      return this.timeCreated;
   }

   public boolean hasExtraData() {
      boolean var1;
      if (this.extraData != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean hasParameter(int var1) {
      boolean var2;
      if (this.extraData == null) {
         var2 = false;
      } else {
         var2 = this.extraData.hasParameter(var1);
      }

      return var2;
   }

   public boolean hasParameterDescription() {
      boolean var1;
      if (this.getParameterDescription().length() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isEnabled() {
      return this.isStateValue(0);
   }

   public boolean isParameterSource(byte var1) {
      boolean var2;
      if (this.getParameterSource() == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isSelected() {
      boolean var1;
      if (this.isVisible() && this.isStateValue(2)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isVisible() {
      boolean var1 = true;
      if (!this.isEnabled() || !this.isStateValue(1)) {
         var1 = false;
      }

      return var1;
   }

   protected void readExtraData(DataReaderBigEndian var1) throws IOException {
      if (var1.readBoolean()) {
         this.extraData = new ExtraData();
         this.extraData.read(var1);
      }

   }

   protected void readStyles(DataReaderBigEndian var1) throws IOException {
      if (var1.readBoolean()) {
         this.styleNormal = new ExtraStyle(var1);
      }

      if (var1.readBoolean()) {
         this.styleHighlight = new ExtraStyle(var1);
      }

   }

   public String removeParameter(int var1) {
      String var2;
      if (this.extraData == null) {
         var2 = null;
      } else {
         var2 = this.extraData.removeParameter(var1);
      }

      return var2;
   }

   public void removeParameterSource() {
      this.removeParameter(0);
   }

   public void removeParameterStyleName() {
      if (this.extraData != null) {
         this.extraData.removeParameter(5);
      }

   }

   public void setEnabled(boolean var1) {
      this.setState(0, var1);
   }

   public void setExtraData(byte[] var1) {
      try {
         DataReaderBigEndian var2 = new DataReaderBigEndian(var1);
         this.readExtraData(var2);
      } catch (Exception var3) {
         Logger.logE("GeoData", "setExtraData(" + var1 + ")", var3);
         this.extraData = null;
      }

   }

   public void setId(long var1) {
      this.id = var1;
   }

   public void setName(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.name = var1;
      }

   }

   public void setParameterDescription(String var1) {
      this.addParameter(30, (String)var1);
   }

   public void setParameterSource(byte var1) {
      this.addParameter(0, (byte)var1);
   }

   public void setParameterStyleName(String var1) {
      this.addParameter(5, (String)var1);
   }

   public void setReadWriteMode(GeoData.ReadWriteMode var1) {
      this.mReadWriteMode = var1;
   }

   public void setSelected(boolean var1) {
      this.setState(2, var1);
   }

   public void setStyles(byte[] var1) {
      try {
         DataReaderBigEndian var2 = new DataReaderBigEndian(var1);
         this.readStyles(var2);
      } catch (Exception var3) {
         Logger.logE("GeoData", "setExtraStyle(" + var1 + ")", var3);
         this.extraData = null;
      }

   }

   public void setTag(String var1, Object var2) {
      if (var1 != null && var1.length() != 0) {
         if (var2 == null) {
            if (this.mTags != null) {
               this.mTags.remove(var1);
            }
         } else {
            if (this.mTags == null) {
               this.mTags = new Hashtable();
            }

            this.mTags.put(var1, var2);
         }
      } else {
         Logger.logW("GeoData", "setTag(" + var1 + "), " + "invalid key");
      }

   }

   public void setTimeCreated(long var1) {
      this.timeCreated = var1;
   }

   public void setVisible(boolean var1) {
      this.setState(1, var1);
   }

   protected void writeExtraData(DataWriterBigEndian var1) throws IOException {
      if (this.extraData != null && this.extraData.getCount() > 0) {
         var1.writeBoolean(true);
         var1.writeStorable(this.extraData);
      } else {
         var1.writeBoolean(false);
      }

   }

   protected void writeStyles(DataWriterBigEndian var1) throws IOException {
      if (this.styleNormal != null) {
         var1.writeBoolean(true);
         var1.writeStorable(this.styleNormal);
      } else {
         var1.writeBoolean(false);
      }

      if (this.styleHighlight != null) {
         var1.writeBoolean(true);
         var1.writeStorable(this.styleHighlight);
      } else {
         var1.writeBoolean(false);
      }

   }

   public static enum ReadWriteMode {
      READ_ONLY,
      READ_WRITE;
   }
}
