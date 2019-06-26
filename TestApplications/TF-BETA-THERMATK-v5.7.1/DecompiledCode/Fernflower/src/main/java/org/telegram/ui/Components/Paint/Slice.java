package org.telegram.ui.Components.Paint;

import android.graphics.RectF;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.zip.Inflater;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;

public class Slice {
   private RectF bounds;
   private File file;

   public Slice(ByteBuffer var1, RectF var2, DispatchQueue var3) {
      this.bounds = var2;

      try {
         this.file = File.createTempFile("paint", ".bin", ApplicationLoader.applicationContext.getCacheDir());
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      if (this.file != null) {
         this.storeData(var1);
      }
   }

   private void storeData(ByteBuffer param1) {
      // $FF: Couldn't be decompiled
   }

   public void cleanResources() {
      File var1 = this.file;
      if (var1 != null) {
         var1.delete();
         this.file = null;
      }

   }

   public RectF getBounds() {
      return new RectF(this.bounds);
   }

   public ByteBuffer getData() {
      Exception var10000;
      label57: {
         byte[] var1;
         byte[] var2;
         FileInputStream var3;
         ByteArrayOutputStream var4;
         Inflater var5;
         boolean var10001;
         try {
            var1 = new byte[1024];
            var2 = new byte[1024];
            var3 = new FileInputStream(this.file);
            var4 = new ByteArrayOutputStream();
            var5 = new Inflater(true);
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label57;
         }

         label54:
         while(true) {
            int var6;
            try {
               var6 = var3.read(var1);
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }

            if (var6 != -1) {
               try {
                  var5.setInput(var1, 0, var6);
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break;
               }
            }

            while(true) {
               try {
                  var6 = var5.inflate(var2, 0, var2.length);
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label54;
               }

               if (var6 == 0) {
                  try {
                     if (var5.finished()) {
                        var5.end();
                        ByteBuffer var14 = ByteBuffer.wrap(var4.toByteArray(), 0, var4.size());
                        var4.close();
                        var3.close();
                        return var14;
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label54;
                  }

                  try {
                     var5.needsInput();
                     break;
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label54;
                  }
               }

               try {
                  var4.write(var2, 0, var6);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label54;
               }
            }
         }
      }

      Exception var15 = var10000;
      FileLog.e((Throwable)var15);
      return null;
   }

   public int getHeight() {
      return (int)this.bounds.height();
   }

   public int getWidth() {
      return (int)this.bounds.width();
   }

   public int getX() {
      return (int)this.bounds.left;
   }

   public int getY() {
      return (int)this.bounds.top;
   }
}
