package org.osmdroid.tileprovider.tilesource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.util.Random;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;

public abstract class BitmapTileSourceBase implements ITileSource {
   private static int globalOrdinal;
   protected String mCopyright;
   protected final String mImageFilenameEnding;
   private final int mMaximumZoomLevel;
   private final int mMinimumZoomLevel;
   protected String mName;
   private final int mOrdinal;
   private final int mTileSizePixels;
   protected final Random random = new Random();

   public BitmapTileSourceBase(String var1, int var2, int var3, int var4, String var5, String var6) {
      int var7 = globalOrdinal++;
      this.mOrdinal = var7;
      this.mName = var1;
      this.mMinimumZoomLevel = var2;
      this.mMaximumZoomLevel = var3;
      this.mTileSizePixels = var4;
      this.mImageFilenameEnding = var5;
      this.mCopyright = var6;
   }

   public Drawable getDrawable(InputStream param1) throws BitmapTileSourceBase.LowMemoryException {
      // $FF: Couldn't be decompiled
   }

   public Drawable getDrawable(String var1) throws BitmapTileSourceBase.LowMemoryException {
      StringBuilder var18;
      OutOfMemoryError var23;
      label92: {
         Exception var10000;
         label76: {
            Bitmap var17;
            boolean var10001;
            label75: {
               Options var2;
               try {
                  var2 = new Options();
                  BitmapPool.getInstance().applyReusableOptions(var2, this.mTileSizePixels, this.mTileSizePixels);
                  if (VERSION.SDK_INT == 15) {
                     var17 = BitmapFactory.decodeFile(var1);
                     break label75;
                  }
               } catch (OutOfMemoryError var15) {
                  var23 = var15;
                  var10001 = false;
                  break label92;
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label76;
               }

               try {
                  var17 = BitmapFactory.decodeFile(var1, var2);
               } catch (OutOfMemoryError var13) {
                  var23 = var13;
                  var10001 = false;
                  break label92;
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label76;
               }
            }

            if (var17 != null) {
               try {
                  return new ReusableBitmapDrawable(var17);
               } catch (OutOfMemoryError var4) {
                  var23 = var4;
                  var10001 = false;
                  break label92;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            } else {
               label66: {
                  File var19;
                  StringBuilder var20;
                  label65: {
                     try {
                        var19 = new File(var1);
                        if (var19.exists()) {
                           var20 = new StringBuilder();
                           var20.append(var1);
                           var20.append(" is an invalid image file, deleting...");
                           Log.d("OsmDroid", var20.toString());
                           break label65;
                        }
                     } catch (OutOfMemoryError var11) {
                        var23 = var11;
                        var10001 = false;
                        break label92;
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label66;
                     }

                     try {
                        var20 = new StringBuilder();
                        var20.append("Request tile: ");
                        var20.append(var1);
                        var20.append(" does not exist");
                        Log.d("OsmDroid", var20.toString());
                        return null;
                     } catch (OutOfMemoryError var9) {
                        var23 = var9;
                        var10001 = false;
                        break label92;
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label66;
                     }
                  }

                  try {
                     var19 = new File(var1);
                     var19.delete();
                     return null;
                  } catch (Throwable var8) {
                     Throwable var3 = var8;

                     try {
                        var20 = new StringBuilder();
                        var20.append("Error deleting invalid file: ");
                        var20.append(var1);
                        Log.e("OsmDroid", var20.toString(), var3);
                        return null;
                     } catch (OutOfMemoryError var6) {
                        var23 = var6;
                        var10001 = false;
                        break label92;
                     } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                     }
                  }
               }
            }
         }

         Exception var21 = var10000;
         var18 = new StringBuilder();
         var18.append("Unexpected error loading bitmap: ");
         var18.append(var1);
         Log.e("OsmDroid", var18.toString(), var21);
         ++Counters.tileDownloadErrors;
         System.gc();
         return null;
      }

      OutOfMemoryError var22 = var23;
      var18 = new StringBuilder();
      var18.append("OutOfMemoryError loading bitmap: ");
      var18.append(var1);
      Log.e("OsmDroid", var18.toString());
      System.gc();
      throw new BitmapTileSourceBase.LowMemoryException(var22);
   }

   public int getMaximumZoomLevel() {
      return this.mMaximumZoomLevel;
   }

   public int getMinimumZoomLevel() {
      return this.mMinimumZoomLevel;
   }

   public String getTileRelativeFilenameString(long var1) {
      StringBuilder var3 = new StringBuilder();
      var3.append(this.pathBase());
      var3.append('/');
      var3.append(MapTileIndex.getZoom(var1));
      var3.append('/');
      var3.append(MapTileIndex.getX(var1));
      var3.append('/');
      var3.append(MapTileIndex.getY(var1));
      var3.append(this.imageFilenameEnding());
      return var3.toString();
   }

   public int getTileSizePixels() {
      return this.mTileSizePixels;
   }

   public String imageFilenameEnding() {
      return this.mImageFilenameEnding;
   }

   public String name() {
      return this.mName;
   }

   public String pathBase() {
      return this.mName;
   }

   public String toString() {
      return this.name();
   }

   public static final class LowMemoryException extends Exception {
      public LowMemoryException(Throwable var1) {
         super(var1);
      }
   }
}
