package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;
import android.view.View;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.tgnet.TLRPC;

public class TextPaintImageReceiverSpan extends ReplacementSpan {
   private boolean alignTop;
   private int height;
   private ImageReceiver imageReceiver;
   private int width;

   public TextPaintImageReceiverSpan(View var1, TLRPC.Document var2, Object var3, int var4, int var5, boolean var6, boolean var7) {
      String var8 = String.format(Locale.US, "%d_%d_i", var4, var5);
      this.width = var4;
      this.height = var5;
      this.imageReceiver = new ImageReceiver(var1);
      this.imageReceiver.setInvalidateAll(true);
      if (var7) {
         this.imageReceiver.setDelegate(_$$Lambda$TextPaintImageReceiverSpan$Cb0mzcqNIfBx1iovVDp8PZkH5ug.INSTANCE);
      }

      TLRPC.PhotoSize var9 = FileLoader.getClosestPhotoSizeWithSize(var2.thumbs, 90);
      this.imageReceiver.setImage(ImageLocation.getForDocument(var2), var8, ImageLocation.getForDocument(var9, var2), var8, -1, (String)null, var3, 1);
      this.alignTop = var6;
   }

   // $FF: synthetic method
   static void lambda$new$0(ImageReceiver var0, boolean var1, boolean var2) {
      if (var0.canInvertBitmap()) {
         var0.setColorFilter(new ColorMatrixColorFilter(new float[]{-1.0F, 0.0F, 0.0F, 0.0F, 255.0F, 0.0F, -1.0F, 0.0F, 0.0F, 255.0F, 0.0F, 0.0F, -1.0F, 0.0F, 255.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F}));
      }
   }

   public void draw(Canvas var1, CharSequence var2, int var3, int var4, float var5, int var6, int var7, int var8, Paint var9) {
      var1.save();
      if (this.alignTop) {
         this.imageReceiver.setImageCoords((int)var5, var6 - 1, this.width, this.height);
      } else {
         var4 = AndroidUtilities.dp(4.0F);
         ImageReceiver var10 = this.imageReceiver;
         var7 = (int)var5;
         var3 = this.height;
         var10.setImageCoords(var7, var6 + (var8 - var4 - var6 - var3) / 2, this.width, var3);
      }

      this.imageReceiver.draw(var1);
      var1.restore();
   }

   public int getSize(Paint var1, CharSequence var2, int var3, int var4, FontMetricsInt var5) {
      if (var5 != null) {
         if (this.alignTop) {
            var3 = var5.descent - var5.ascent - AndroidUtilities.dp(4.0F);
            var4 = this.height - var3;
            var5.descent = var4;
            var5.bottom = var4;
            var3 = 0 - var3;
            var5.ascent = var3;
            var5.top = var3;
         } else {
            var3 = -this.height / 2 - AndroidUtilities.dp(4.0F);
            var5.ascent = var3;
            var5.top = var3;
            var3 = this.height;
            var3 = var3 - var3 / 2 - AndroidUtilities.dp(4.0F);
            var5.descent = var3;
            var5.bottom = var3;
         }
      }

      return this.width;
   }
}
