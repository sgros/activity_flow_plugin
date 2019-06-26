package locus.api.android.features.geocaching.fieldNotes;

import java.io.IOException;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class FieldNoteImage extends Storable {
   private String mCaption;
   private String mDescription;
   private long mFieldNoteId;
   private long mId;
   private byte[] mImage;

   public String getCaption() {
      return this.mCaption;
   }

   public String getDescription() {
      return this.mDescription;
   }

   public long getFieldNoteId() {
      return this.mFieldNoteId;
   }

   public long getId() {
      return this.mId;
   }

   public byte[] getImage() {
      return this.mImage;
   }

   protected int getVersion() {
      return 0;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mId = var2.readLong();
      this.mFieldNoteId = var2.readLong();
      this.mCaption = var2.readString();
      this.mDescription = var2.readString();
      var1 = var2.readInt();
      if (var1 > 0) {
         this.mImage = new byte[var1];
         var2.readBytes(this.mImage);
      }

   }

   public void reset() {
      this.mId = -1L;
      this.mFieldNoteId = -1L;
      this.mCaption = "";
      this.mDescription = "";
      this.mImage = null;
   }

   public void setCaption(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mCaption = var2;
   }

   public void setDescription(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mDescription = var2;
   }

   public void setFieldNoteId(long var1) {
      this.mFieldNoteId = var1;
   }

   public void setId(long var1) {
      this.mId = var1;
   }

   public void setImage(byte[] var1) {
      this.mImage = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.mId);
      var1.writeLong(this.mFieldNoteId);
      var1.writeString(this.mCaption);
      var1.writeString(this.mDescription);
      if (this.mImage != null && this.mImage.length > 0) {
         var1.writeInt(this.mImage.length);
         var1.write(this.mImage);
      } else {
         var1.writeInt(0);
      }

   }
}
