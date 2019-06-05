package locus.api.android.objects;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.utils.UtilsBitmap;
import locus.api.objects.Storable;
import locus.api.objects.extra.ExtraStyle;
import locus.api.objects.extra.Waypoint;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class PackWaypoints extends Storable {
   private Bitmap mBitmap;
   private String mName;
   private ExtraStyle mStyle;
   private List mWpts;

   public PackWaypoints() {
      this("");
   }

   public PackWaypoints(String var1) {
      this.mName = var1;
   }

   public PackWaypoints(byte[] var1) throws IOException {
      super(var1);
   }

   public void addWaypoint(Waypoint var1) {
      this.mWpts.add(var1);
   }

   public Bitmap getBitmap() {
      return this.mBitmap;
   }

   public ExtraStyle getExtraStyle() {
      return this.mStyle;
   }

   public String getName() {
      return this.mName;
   }

   protected int getVersion() {
      return 0;
   }

   public List getWaypoints() {
      return this.mWpts;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mName = var2.readString();
      if (var2.readBoolean()) {
         this.mStyle = new ExtraStyle(var2);
      }

      this.mBitmap = UtilsBitmap.readBitmap(var2);
      this.mWpts = var2.readListStorable(Waypoint.class);
   }

   public void reset() {
      this.mName = null;
      this.mStyle = null;
      this.mWpts = new ArrayList();
   }

   public void setBitmap(Bitmap var1) {
      this.mBitmap = var1;
   }

   public void setExtraStyle(ExtraStyle var1) {
      this.mStyle = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeString(this.mName);
      if (this.mStyle == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeStorable(this.mStyle);
      }

      UtilsBitmap.writeBitmap(var1, this.mBitmap, CompressFormat.PNG);
      var1.writeListStorable(this.mWpts);
   }
}
