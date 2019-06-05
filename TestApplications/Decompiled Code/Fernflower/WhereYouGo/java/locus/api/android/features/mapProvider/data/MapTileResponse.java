package locus.api.android.features.mapProvider.data;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import java.io.IOException;
import locus.api.android.utils.UtilsBitmap;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class MapTileResponse extends Storable {
   public static final int CODE_INTERNAL_ERROR = 4;
   public static final int CODE_INVALID_REQUEST = 2;
   public static final int CODE_NOT_EXISTS = 3;
   public static final int CODE_UNKNOWN = 0;
   public static final int CODE_VALID = 1;
   private Bitmap mImage;
   private int mResultCode;

   public MapTileResponse() {
   }

   public MapTileResponse(byte[] var1) throws IOException {
      super(var1);
   }

   public Bitmap getImage() {
      return this.mImage;
   }

   public int getResultCode() {
      return this.mResultCode;
   }

   protected int getVersion() {
      return 0;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mResultCode = var2.readInt();
      var1 = var2.readInt();
      if (var1 > 0) {
         this.mImage = UtilsBitmap.getBitmap(var2.readBytes(var1));
      } else {
         this.mImage = null;
      }

   }

   public void reset() {
      this.mResultCode = 0;
      this.mImage = null;
   }

   public void setImage(Bitmap var1) {
      this.mImage = var1;
   }

   public void setResultCode(int var1) {
      this.mResultCode = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeInt(this.mResultCode);
      if (this.mImage == null) {
         var1.writeInt(0);
      } else {
         byte[] var2 = UtilsBitmap.getBitmap(this.mImage, CompressFormat.PNG);
         if (var2 != null && var2.length != 0) {
            var1.writeInt(var2.length);
            var1.write(var2);
         } else {
            var1.writeInt(0);
         }
      }

   }
}
