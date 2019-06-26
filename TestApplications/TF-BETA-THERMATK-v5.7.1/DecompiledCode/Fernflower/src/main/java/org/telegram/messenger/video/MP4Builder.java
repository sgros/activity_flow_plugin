package org.telegram.messenger.video;

import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.DataEntryUrlBox;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.SyncSampleBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.Matrix;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MP4Builder {
   private Mp4Movie currentMp4Movie = null;
   private long dataOffset = 0L;
   private FileChannel fc = null;
   private FileOutputStream fos = null;
   private MP4Builder.InterleaveChunkMdat mdat = null;
   private ByteBuffer sizeBuffer = null;
   private boolean splitMdat;
   private HashMap track2SampleSizes = new HashMap();
   private boolean wasFirstVideoFrame;
   private boolean writeNewMdat = true;
   private long wroteSinceLastMdat = 0L;

   private void flushCurrentMdat() throws Exception {
      long var1 = this.fc.position();
      this.fc.position(this.mdat.getOffset());
      this.mdat.getBox(this.fc);
      this.fc.position(var1);
      this.mdat.setDataOffset(0L);
      this.mdat.setContentSize(0L);
      this.fos.flush();
      this.fos.getFD().sync();
   }

   public static long gcd(long var0, long var2) {
      return var2 == 0L ? var0 : gcd(var2, var0 % var2);
   }

   public int addTrack(MediaFormat var1, boolean var2) {
      return this.currentMp4Movie.addTrack(var1, var2);
   }

   protected void createCtts(Track var1, SampleTableBox var2) {
      int[] var3 = var1.getSampleCompositions();
      if (var3 != null) {
         CompositionTimeToSample.Entry var7 = null;
         ArrayList var4 = new ArrayList();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            int var6 = var3[var5];
            if (var7 != null && var7.getOffset() == var6) {
               var7.setCount(var7.getCount() + 1);
            } else {
               var7 = new CompositionTimeToSample.Entry(1, var6);
               var4.add(var7);
            }
         }

         CompositionTimeToSample var8 = new CompositionTimeToSample();
         var8.setEntries(var4);
         var2.addBox(var8);
      }
   }

   protected FileTypeBox createFileTypeBox() {
      LinkedList var1 = new LinkedList();
      var1.add("isom");
      var1.add("iso2");
      var1.add("avc1");
      var1.add("mp41");
      return new FileTypeBox("isom", 512L, var1);
   }

   public MP4Builder createMovie(Mp4Movie var1, boolean var2) throws Exception {
      this.currentMp4Movie = var1;
      this.fos = new FileOutputStream(var1.getCacheFile());
      this.fc = this.fos.getChannel();
      FileTypeBox var3 = this.createFileTypeBox();
      var3.getBox(this.fc);
      this.dataOffset += var3.getSize();
      this.wroteSinceLastMdat += this.dataOffset;
      this.splitMdat = var2;
      this.mdat = new MP4Builder.InterleaveChunkMdat();
      this.sizeBuffer = ByteBuffer.allocateDirect(4);
      return this;
   }

   protected MovieBox createMovieBox(Mp4Movie var1) {
      MovieBox var2 = new MovieBox();
      MovieHeaderBox var3 = new MovieHeaderBox();
      var3.setCreationTime(new Date());
      var3.setModificationTime(new Date());
      var3.setMatrix(Matrix.ROTATE_0);
      long var4 = this.getTimescale(var1);
      Iterator var6 = var1.getTracks().iterator();
      long var7 = 0L;

      while(var6.hasNext()) {
         Track var9 = (Track)var6.next();
         var9.prepare();
         long var10 = var9.getDuration() * var4 / (long)var9.getTimeScale();
         if (var10 > var7) {
            var7 = var10;
         }
      }

      var3.setDuration(var7);
      var3.setTimescale(var4);
      var3.setNextTrackId((long)(var1.getTracks().size() + 1));
      var2.addBox(var3);
      Iterator var12 = var1.getTracks().iterator();

      while(var12.hasNext()) {
         var2.addBox(this.createTrackBox((Track)var12.next(), var1));
      }

      return var2;
   }

   protected void createSidx(Track var1, SampleTableBox var2) {
   }

   protected Box createStbl(Track var1) {
      SampleTableBox var2 = new SampleTableBox();
      this.createStsd(var1, var2);
      this.createStts(var1, var2);
      this.createCtts(var1, var2);
      this.createStss(var1, var2);
      this.createStsc(var1, var2);
      this.createStsz(var1, var2);
      this.createStco(var1, var2);
      return var2;
   }

   protected void createStco(Track var1, SampleTableBox var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.getSamples().iterator();

      long var7;
      Sample var12;
      for(long var5 = -1L; var4.hasNext(); var5 = var12.getSize() + var7) {
         var12 = (Sample)var4.next();
         var7 = var12.getOffset();
         long var9 = var5;
         if (var5 != -1L) {
            var9 = var5;
            if (var5 != var7) {
               var9 = -1L;
            }
         }

         if (var9 == -1L) {
            var3.add(var7);
         }
      }

      long[] var13 = new long[var3.size()];

      for(int var11 = 0; var11 < var3.size(); ++var11) {
         var13[var11] = (Long)var3.get(var11);
      }

      StaticChunkOffsetBox var14 = new StaticChunkOffsetBox();
      var14.setChunkOffsets(var13);
      var2.addBox(var14);
   }

   protected void createStsc(Track var1, SampleTableBox var2) {
      SampleToChunkBox var3 = new SampleToChunkBox();
      var3.setEntries(new LinkedList());
      int var4 = var1.getSamples().size();
      int var5 = 0;
      int var6 = 0;
      int var7 = -1;

      int var17;
      for(int var8 = 1; var5 < var4; var8 = var17) {
         Sample var9 = (Sample)var1.getSamples().get(var5);
         long var10 = var9.getOffset();
         long var12 = var9.getSize();
         int var14 = var6 + 1;
         boolean var15;
         if (var5 != var4 - 1 && var10 + var12 == ((Sample)var1.getSamples().get(var5 + 1)).getOffset()) {
            var15 = false;
         } else {
            var15 = true;
         }

         var6 = var14;
         int var16 = var7;
         var17 = var8;
         if (var15) {
            if (var7 != var14) {
               var3.getEntries().add(new SampleToChunkBox.Entry((long)var8, (long)var14, 1L));
               var7 = var14;
            }

            var17 = var8 + 1;
            var6 = 0;
            var16 = var7;
         }

         ++var5;
         var7 = var16;
      }

      var2.addBox(var3);
   }

   protected void createStsd(Track var1, SampleTableBox var2) {
      var2.addBox(var1.getSampleDescriptionBox());
   }

   protected void createStss(Track var1, SampleTableBox var2) {
      long[] var3 = var1.getSyncSamples();
      if (var3 != null && var3.length > 0) {
         SyncSampleBox var4 = new SyncSampleBox();
         var4.setSampleNumber(var3);
         var2.addBox(var4);
      }

   }

   protected void createStsz(Track var1, SampleTableBox var2) {
      SampleSizeBox var3 = new SampleSizeBox();
      var3.setSampleSizes((long[])this.track2SampleSizes.get(var1));
      var2.addBox(var3);
   }

   protected void createStts(Track var1, SampleTableBox var2) {
      ArrayList var3 = new ArrayList();
      long[] var4 = var1.getSampleDurations();
      TimeToSampleBox.Entry var8 = null;

      for(int var5 = 0; var5 < var4.length; ++var5) {
         long var6 = var4[var5];
         if (var8 != null && var8.getDelta() == var6) {
            var8.setCount(var8.getCount() + 1L);
         } else {
            var8 = new TimeToSampleBox.Entry(1L, var6);
            var3.add(var8);
         }
      }

      TimeToSampleBox var9 = new TimeToSampleBox();
      var9.setEntries(var3);
      var2.addBox(var9);
   }

   protected TrackBox createTrackBox(Track var1, Mp4Movie var2) {
      TrackBox var3 = new TrackBox();
      TrackHeaderBox var4 = new TrackHeaderBox();
      var4.setEnabled(true);
      var4.setInMovie(true);
      var4.setInPreview(true);
      if (var1.isAudio()) {
         var4.setMatrix(Matrix.ROTATE_0);
      } else {
         var4.setMatrix(var2.getMatrix());
      }

      var4.setAlternateGroup(0);
      var4.setCreationTime(var1.getCreationTime());
      var4.setDuration(var1.getDuration() * this.getTimescale(var2) / (long)var1.getTimeScale());
      var4.setHeight((double)var1.getHeight());
      var4.setWidth((double)var1.getWidth());
      var4.setLayer(0);
      var4.setModificationTime(new Date());
      var4.setTrackId(var1.getTrackId() + 1L);
      var4.setVolume(var1.getVolume());
      var3.addBox(var4);
      MediaBox var11 = new MediaBox();
      var3.addBox(var11);
      MediaHeaderBox var8 = new MediaHeaderBox();
      var8.setCreationTime(var1.getCreationTime());
      var8.setDuration(var1.getDuration());
      var8.setTimescale((long)var1.getTimeScale());
      var8.setLanguage("eng");
      var11.addBox(var8);
      HandlerBox var5 = new HandlerBox();
      String var9;
      if (var1.isAudio()) {
         var9 = "SoundHandle";
      } else {
         var9 = "VideoHandle";
      }

      var5.setName(var9);
      var5.setHandlerType(var1.getHandler());
      var11.addBox(var5);
      MediaInformationBox var6 = new MediaInformationBox();
      var6.addBox(var1.getMediaHeaderBox());
      DataInformationBox var10 = new DataInformationBox();
      DataReferenceBox var12 = new DataReferenceBox();
      var10.addBox(var12);
      DataEntryUrlBox var7 = new DataEntryUrlBox();
      var7.setFlags(1);
      var12.addBox(var7);
      var6.addBox(var10);
      var6.addBox(this.createStbl(var1));
      var11.addBox(var6);
      return var3;
   }

   public void finishMovie() throws Exception {
      if (this.mdat.getContentSize() != 0L) {
         this.flushCurrentMdat();
      }

      Iterator var1 = this.currentMp4Movie.getTracks().iterator();

      while(var1.hasNext()) {
         Track var2 = (Track)var1.next();
         ArrayList var3 = var2.getSamples();
         long[] var4 = new long[var3.size()];

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var4[var5] = ((Sample)var3.get(var5)).getSize();
         }

         this.track2SampleSizes.put(var2, var4);
      }

      this.createMovieBox(this.currentMp4Movie).getBox(this.fc);
      this.fos.flush();
      this.fos.getFD().sync();
      this.fc.close();
      this.fos.close();
   }

   public long getTimescale(Mp4Movie var1) {
      long var2;
      if (!var1.getTracks().isEmpty()) {
         var2 = (long)((Track)var1.getTracks().iterator().next()).getTimeScale();
      } else {
         var2 = 0L;
      }

      for(Iterator var4 = var1.getTracks().iterator(); var4.hasNext(); var2 = gcd((long)((Track)var4.next()).getTimeScale(), var2)) {
      }

      return var2;
   }

   public long writeSampleData(int var1, ByteBuffer var2, BufferInfo var3, boolean var4) throws Exception {
      if (this.writeNewMdat) {
         this.mdat.setContentSize(0L);
         this.mdat.getBox(this.fc);
         this.mdat.setDataOffset(this.dataOffset);
         this.dataOffset += 16L;
         this.wroteSinceLastMdat += 16L;
         this.writeNewMdat = false;
      }

      MP4Builder.InterleaveChunkMdat var5 = this.mdat;
      var5.setContentSize(var5.getContentSize() + (long)var3.size);
      this.wroteSinceLastMdat += (long)var3.size;
      long var6 = this.wroteSinceLastMdat;
      boolean var8 = true;
      if (var6 >= 32768L) {
         if (this.splitMdat) {
            this.flushCurrentMdat();
            this.writeNewMdat = true;
         }

         this.wroteSinceLastMdat = 0L;
      } else {
         var8 = false;
      }

      this.currentMp4Movie.addSample(var1, this.dataOffset, var3);
      if (var4) {
         this.sizeBuffer.position(0);
         this.sizeBuffer.putInt(var3.size - 4);
         this.sizeBuffer.position(0);
         this.fc.write(this.sizeBuffer);
         var2.position(var3.offset + 4);
      } else {
         var2.position(var3.offset);
      }

      var2.limit(var3.offset + var3.size);
      this.fc.write(var2);
      this.dataOffset += (long)var3.size;
      if (var8) {
         this.fos.flush();
         this.fos.getFD().sync();
         return this.fc.position();
      } else {
         return 0L;
      }
   }

   private class InterleaveChunkMdat implements Box {
      private long contentSize;
      private long dataOffset;
      private Container parent;

      private InterleaveChunkMdat() {
         this.contentSize = 1073741824L;
         this.dataOffset = 0L;
      }

      // $FF: synthetic method
      InterleaveChunkMdat(Object var2) {
         this();
      }

      private boolean isSmallBox(long var1) {
         boolean var3;
         if (var1 + 8L < 4294967296L) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void getBox(WritableByteChannel var1) throws IOException {
         ByteBuffer var2 = ByteBuffer.allocate(16);
         long var3 = this.getSize();
         if (this.isSmallBox(var3)) {
            IsoTypeWriter.writeUInt32(var2, var3);
         } else {
            IsoTypeWriter.writeUInt32(var2, 1L);
         }

         var2.put(IsoFile.fourCCtoBytes("mdat"));
         if (this.isSmallBox(var3)) {
            var2.put(new byte[8]);
         } else {
            IsoTypeWriter.writeUInt64(var2, var3);
         }

         var2.rewind();
         var1.write(var2);
      }

      public long getContentSize() {
         return this.contentSize;
      }

      public long getOffset() {
         return this.dataOffset;
      }

      public Container getParent() {
         return this.parent;
      }

      public long getSize() {
         return this.contentSize + 16L;
      }

      public String getType() {
         return "mdat";
      }

      public void parse(DataSource var1, ByteBuffer var2, long var3, BoxParser var5) {
      }

      public void setContentSize(long var1) {
         this.contentSize = var1;
      }

      public void setDataOffset(long var1) {
         this.dataOffset = var1;
      }

      public void setParent(Container var1) {
         this.parent = var1;
      }
   }
}
