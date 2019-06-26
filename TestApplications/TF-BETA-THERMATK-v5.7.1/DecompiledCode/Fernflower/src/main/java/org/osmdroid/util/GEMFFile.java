package org.osmdroid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class GEMFFile {
   private int mCurrentSource;
   private final List mFileNames;
   private final List mFileSizes;
   private final List mFiles;
   private final String mLocation;
   private final List mRangeData;
   private boolean mSourceLimited;
   private final LinkedHashMap mSources;

   public GEMFFile(File var1) throws FileNotFoundException, IOException {
      this(var1.getAbsolutePath());
   }

   public GEMFFile(String var1) throws FileNotFoundException, IOException {
      this.mFiles = new ArrayList();
      this.mFileNames = new ArrayList();
      this.mRangeData = new ArrayList();
      this.mFileSizes = new ArrayList();
      this.mSources = new LinkedHashMap();
      this.mSourceLimited = false;
      this.mCurrentSource = 0;
      this.mLocation = var1;
      this.openFiles();
      this.readHeader();
   }

   private void openFiles() throws FileNotFoundException {
      File var1 = new File(this.mLocation);
      this.mFiles.add(new RandomAccessFile(var1, "r"));
      this.mFileNames.add(var1.getPath());
      int var2 = 0;

      while(true) {
         ++var2;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.mLocation);
         var3.append("-");
         var3.append(var2);
         var1 = new File(var3.toString());
         if (!var1.exists()) {
            return;
         }

         this.mFiles.add(new RandomAccessFile(var1, "r"));
         this.mFileNames.add(var1.getPath());
      }
   }

   private void readHeader() throws IOException {
      List var1 = this.mFiles;
      byte var2 = 0;
      RandomAccessFile var9 = (RandomAccessFile)var1.get(0);
      Iterator var3 = this.mFiles.iterator();

      while(var3.hasNext()) {
         RandomAccessFile var4 = (RandomAccessFile)var3.next();
         this.mFileSizes.add(var4.length());
      }

      int var5 = var9.readInt();
      StringBuilder var10;
      if (var5 != 4) {
         var10 = new StringBuilder();
         var10.append("Bad file version: ");
         var10.append(var5);
         throw new IOException(var10.toString());
      } else {
         var5 = var9.readInt();
         if (var5 != 256) {
            var10 = new StringBuilder();
            var10.append("Bad tile size: ");
            var10.append(var5);
            throw new IOException(var10.toString());
         } else {
            int var6 = var9.readInt();

            for(var5 = 0; var5 < var6; ++var5) {
               int var7 = var9.readInt();
               int var8 = var9.readInt();
               byte[] var11 = new byte[var8];
               var9.read(var11, 0, var8);
               String var12 = new String(var11);
               this.mSources.put(new Integer(var7), var12);
            }

            var6 = var9.readInt();

            for(var5 = var2; var5 < var6; ++var5) {
               GEMFFile.GEMFRange var13 = new GEMFFile.GEMFRange();
               var13.zoom = var9.readInt();
               var13.xMin = var9.readInt();
               var13.xMax = var9.readInt();
               var13.yMin = var9.readInt();
               var13.yMax = var9.readInt();
               var13.sourceIndex = var9.readInt();
               var13.offset = var9.readLong();
               this.mRangeData.add(var13);
            }

         }
      }
   }

   public void close() throws IOException {
      Iterator var1 = this.mFiles.iterator();

      while(var1.hasNext()) {
         ((RandomAccessFile)var1.next()).close();
      }

   }

   public InputStream getInputStream(int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public String getName() {
      return this.mLocation;
   }

   class GEMFInputStream extends InputStream {
      RandomAccessFile raf;
      int remainingBytes;

      GEMFInputStream(String var2, long var3, int var5) throws IOException {
         this.raf = new RandomAccessFile(var2, "r");
         this.raf.seek(var3);
         this.remainingBytes = var5;
      }

      public int available() {
         return this.remainingBytes;
      }

      public void close() throws IOException {
         this.raf.close();
      }

      public boolean markSupported() {
         return false;
      }

      public int read() throws IOException {
         int var1 = this.remainingBytes;
         if (var1 > 0) {
            this.remainingBytes = var1 - 1;
            return this.raf.read();
         } else {
            throw new IOException("End of stream");
         }
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         RandomAccessFile var4 = this.raf;
         int var5 = this.remainingBytes;
         int var6 = var3;
         if (var3 > var5) {
            var6 = var5;
         }

         var2 = var4.read(var1, var2, var6);
         this.remainingBytes -= var2;
         return var2;
      }

      public long skip(long var1) {
         return 0L;
      }
   }

   private class GEMFRange {
      Long offset;
      Integer sourceIndex;
      Integer xMax;
      Integer xMin;
      Integer yMax;
      Integer yMin;
      Integer zoom;

      private GEMFRange() {
      }

      // $FF: synthetic method
      GEMFRange(Object var2) {
         this();
      }

      public String toString() {
         return String.format("GEMF Range: source=%d, zoom=%d, x=%d-%d, y=%d-%d, offset=0x%08X", this.sourceIndex, this.zoom, this.xMin, this.xMax, this.yMin, this.yMax, this.offset);
      }
   }
}
