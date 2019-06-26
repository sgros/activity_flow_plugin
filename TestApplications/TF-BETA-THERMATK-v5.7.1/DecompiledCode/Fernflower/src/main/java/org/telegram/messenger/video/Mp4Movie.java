package org.telegram.messenger.video;

import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import com.googlecode.mp4parser.util.Matrix;
import java.io.File;
import java.util.ArrayList;

public class Mp4Movie {
   private File cacheFile;
   private int height;
   private Matrix matrix;
   private ArrayList tracks;
   private int width;

   public Mp4Movie() {
      this.matrix = Matrix.ROTATE_0;
      this.tracks = new ArrayList();
   }

   public void addSample(int var1, long var2, BufferInfo var4) {
      if (var1 >= 0 && var1 < this.tracks.size()) {
         ((Track)this.tracks.get(var1)).addSample(var2, var4);
      }

   }

   public int addTrack(MediaFormat var1, boolean var2) {
      ArrayList var3 = this.tracks;
      var3.add(new Track(var3.size(), var1, var2));
      return this.tracks.size() - 1;
   }

   public File getCacheFile() {
      return this.cacheFile;
   }

   public int getHeight() {
      return this.height;
   }

   public Matrix getMatrix() {
      return this.matrix;
   }

   public ArrayList getTracks() {
      return this.tracks;
   }

   public int getWidth() {
      return this.width;
   }

   public void setCacheFile(File var1) {
      this.cacheFile = var1;
   }

   public void setRotation(int var1) {
      if (var1 == 0) {
         this.matrix = Matrix.ROTATE_0;
      } else if (var1 == 90) {
         this.matrix = Matrix.ROTATE_90;
      } else if (var1 == 180) {
         this.matrix = Matrix.ROTATE_180;
      } else if (var1 == 270) {
         this.matrix = Matrix.ROTATE_270;
      }

   }

   public void setSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }
}
