package locus.api.android.features.geocaching.fieldNotes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;

public class FieldNote extends Storable {
   private String mCacheCode;
   private String mCacheName;
   private boolean mFavorite;
   private long mId;
   private List mImages;
   private boolean mLogged;
   private String mNote;
   private long mTime;
   private int mType;

   public FieldNote() {
   }

   public FieldNote(byte[] var1) throws IOException {
      super(var1);
   }

   public void addImage(FieldNoteImage var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Image not valid");
      } else {
         this.mImages.add(var1);
      }
   }

   public String getCacheCode() {
      return this.mCacheCode;
   }

   public String getCacheName() {
      return this.mCacheName;
   }

   public long getId() {
      return this.mId;
   }

   public Iterator getImages() {
      return this.mImages.iterator();
   }

   public int getImagesCount() {
      return this.mImages.size();
   }

   public String getNote() {
      return this.mNote;
   }

   public long getTime() {
      return this.mTime;
   }

   public int getType() {
      return this.mType;
   }

   protected int getVersion() {
      return 0;
   }

   public boolean isFavorite() {
      return this.mFavorite;
   }

   public boolean isLogged() {
      return this.mLogged;
   }

   protected void readObject(int var1, DataReaderBigEndian var2) throws IOException {
      this.mId = var2.readLong();
      this.mCacheCode = var2.readString();
      this.mCacheName = var2.readString();
      this.mType = var2.readInt();
      this.mTime = var2.readLong();
      this.mNote = var2.readString();
      this.mFavorite = var2.readBoolean();
      this.mLogged = var2.readBoolean();
      this.mImages = var2.readListStorable(FieldNoteImage.class);
   }

   public void reset() {
      this.mId = -1L;
      this.mCacheCode = "";
      this.mCacheName = "";
      this.mType = 0;
      this.mTime = 0L;
      this.mNote = "";
      this.mFavorite = false;
      this.mLogged = false;
      this.mImages = new ArrayList();
   }

   public void setCacheCode(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mCacheCode = var2;
   }

   public void setCacheName(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mCacheName = var2;
   }

   public void setFavorite(boolean var1) {
      this.mFavorite = var1;
   }

   public void setId(long var1) {
      this.mId = var1;
   }

   public void setLogged(boolean var1) {
      this.mLogged = var1;
   }

   public void setNote(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.mNote = var2;
   }

   public void setTime(long var1) {
      this.mTime = var1;
   }

   public void setType(int var1) {
      this.mType = var1;
   }

   protected void writeObject(DataWriterBigEndian var1) throws IOException {
      var1.writeLong(this.mId);
      var1.writeString(this.mCacheCode);
      var1.writeString(this.mCacheName);
      var1.writeInt(this.mType);
      var1.writeLong(this.mTime);
      var1.writeString(this.mNote);
      var1.writeBoolean(this.mFavorite);
      var1.writeBoolean(this.mLogged);
      var1.writeListStorable(this.mImages);
   }
}
