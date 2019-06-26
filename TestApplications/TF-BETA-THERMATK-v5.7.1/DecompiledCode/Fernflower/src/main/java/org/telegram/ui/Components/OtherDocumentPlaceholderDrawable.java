package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

public class OtherDocumentPlaceholderDrawable extends RecyclableDrawable implements DownloadController.FileDownloadProgressListener {
   private static TextPaint buttonPaint = new TextPaint(1);
   private static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
   private static TextPaint docPaint = new TextPaint(1);
   private static TextPaint namePaint = new TextPaint(1);
   private static TextPaint openPaint = new TextPaint(1);
   private static Paint paint = new Paint();
   private static TextPaint percentPaint = new TextPaint(1);
   private static Paint progressPaint = new Paint(1);
   private static TextPaint sizePaint = new TextPaint(1);
   private int TAG;
   private float animatedAlphaValue = 1.0F;
   private float animatedProgressValue = 0.0F;
   private float animationProgressStart = 0.0F;
   private float currentProgress = 0.0F;
   private long currentProgressTime = 0L;
   private String ext;
   private String fileName;
   private String fileSize;
   private long lastUpdateTime = 0L;
   private boolean loaded;
   private boolean loading;
   private MessageObject parentMessageObject;
   private View parentView;
   private String progress;
   private boolean progressVisible;
   private Drawable thumbDrawable;

   static {
      progressPaint.setStrokeCap(Cap.ROUND);
      paint.setColor(-14209998);
      docPaint.setColor(-1);
      namePaint.setColor(-1);
      sizePaint.setColor(-10327179);
      buttonPaint.setColor(-10327179);
      percentPaint.setColor(-1);
      openPaint.setColor(-1);
      docPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      namePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      buttonPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      percentPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      openPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
   }

   public OtherDocumentPlaceholderDrawable(Context var1, View var2, MessageObject var3) {
      docPaint.setTextSize((float)AndroidUtilities.dp(14.0F));
      namePaint.setTextSize((float)AndroidUtilities.dp(19.0F));
      sizePaint.setTextSize((float)AndroidUtilities.dp(15.0F));
      buttonPaint.setTextSize((float)AndroidUtilities.dp(15.0F));
      percentPaint.setTextSize((float)AndroidUtilities.dp(15.0F));
      openPaint.setTextSize((float)AndroidUtilities.dp(15.0F));
      progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.parentView = var2;
      this.parentMessageObject = var3;
      this.TAG = DownloadController.getInstance(var3.currentAccount).generateObserverTag();
      TLRPC.Document var4 = var3.getDocument();
      if (var4 != null) {
         this.fileName = FileLoader.getDocumentFileName(var3.getDocument());
         if (TextUtils.isEmpty(this.fileName)) {
            this.fileName = "name";
         }

         int var5 = this.fileName.lastIndexOf(46);
         String var6;
         if (var5 == -1) {
            var6 = "";
         } else {
            var6 = this.fileName.substring(var5 + 1).toUpperCase();
         }

         this.ext = var6;
         if ((int)Math.ceil((double)docPaint.measureText(this.ext)) > AndroidUtilities.dp(40.0F)) {
            this.ext = TextUtils.ellipsize(this.ext, docPaint, (float)AndroidUtilities.dp(40.0F), TruncateAt.END).toString();
         }

         this.thumbDrawable = var1.getResources().getDrawable(AndroidUtilities.getThumbForNameOrMime(this.fileName, var3.getDocument().mime_type, true)).mutate();
         this.fileSize = AndroidUtilities.formatFileSize((long)var4.size);
         if ((int)Math.ceil((double)namePaint.measureText(this.fileName)) > AndroidUtilities.dp(320.0F)) {
            this.fileName = TextUtils.ellipsize(this.fileName, namePaint, (float)AndroidUtilities.dp(320.0F), TruncateAt.END).toString();
         }
      }

      this.checkFileExist();
   }

   private void updateAnimation() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      float var5 = this.animatedProgressValue;
      float var6;
      if (var5 != 1.0F) {
         var6 = this.currentProgress;
         if (var5 != var6) {
            float var7 = this.animationProgressStart;
            var5 = var6 - var7;
            if (var5 > 0.0F) {
               this.currentProgressTime += var3;
               var1 = this.currentProgressTime;
               if (var1 >= 300L) {
                  this.animatedProgressValue = var6;
                  this.animationProgressStart = var6;
                  this.currentProgressTime = 0L;
               } else {
                  this.animatedProgressValue = var7 + var5 * decelerateInterpolator.getInterpolation((float)var1 / 300.0F);
               }
            }

            this.parentView.invalidate();
         }
      }

      var6 = this.animatedProgressValue;
      if (var6 >= 1.0F && var6 == 1.0F) {
         var6 = this.animatedAlphaValue;
         if (var6 != 0.0F) {
            this.animatedAlphaValue = var6 - (float)var3 / 200.0F;
            if (this.animatedAlphaValue <= 0.0F) {
               this.animatedAlphaValue = 0.0F;
            }

            this.parentView.invalidate();
         }
      }

   }

   public void checkFileExist() {
      label32: {
         MessageObject var1 = this.parentMessageObject;
         if (var1 != null) {
            TLRPC.Message var3 = var1.messageOwner;
            if (var3.media != null) {
               Float var2;
               String var4;
               label27: {
                  var2 = null;
                  if (!TextUtils.isEmpty(var3.attachPath)) {
                     var4 = var2;
                     if ((new File(this.parentMessageObject.messageOwner.attachPath)).exists()) {
                        break label27;
                     }
                  }

                  var4 = var2;
                  if (!FileLoader.getPathToMessage(this.parentMessageObject.messageOwner).exists()) {
                     var4 = FileLoader.getAttachFileName(this.parentMessageObject.getDocument());
                  }
               }

               this.loaded = false;
               if (var4 == null) {
                  this.progressVisible = false;
                  this.loading = false;
                  this.loaded = true;
                  DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver(this);
               } else {
                  DownloadController.getInstance(this.parentMessageObject.currentAccount).addLoadingFileObserver(var4, this);
                  this.loading = FileLoader.getInstance(this.parentMessageObject.currentAccount).isLoadingFile(var4);
                  if (this.loading) {
                     this.progressVisible = true;
                     var2 = ImageLoader.getInstance().getFileProgress(var4);
                     Float var5 = var2;
                     if (var2 == null) {
                        var5 = 0.0F;
                     }

                     this.setProgress(var5, false);
                  } else {
                     this.progressVisible = false;
                  }
               }
               break label32;
            }
         }

         this.loading = false;
         this.loaded = true;
         this.progressVisible = false;
         this.setProgress(0.0F, false);
         DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver(this);
      }

      this.parentView.invalidate();
   }

   public void draw(Canvas var1) {
      android.graphics.Rect var2 = this.getBounds();
      int var3 = var2.width();
      int var4 = var2.height();
      var1.save();
      var1.translate((float)var2.left, (float)var2.top);
      var1.drawRect(0.0F, 0.0F, (float)var3, (float)var4, paint);
      int var5 = (var4 - AndroidUtilities.dp(240.0F)) / 2;
      var4 = (var3 - AndroidUtilities.dp(48.0F)) / 2;
      this.thumbDrawable.setBounds(var4, var5, AndroidUtilities.dp(48.0F) + var4, AndroidUtilities.dp(48.0F) + var5);
      this.thumbDrawable.draw(var1);
      var4 = (int)Math.ceil((double)docPaint.measureText(this.ext));
      var1.drawText(this.ext, (float)((var3 - var4) / 2), (float)(AndroidUtilities.dp(31.0F) + var5), docPaint);
      var4 = (int)Math.ceil((double)namePaint.measureText(this.fileName));
      var1.drawText(this.fileName, (float)((var3 - var4) / 2), (float)(AndroidUtilities.dp(96.0F) + var5), namePaint);
      var4 = (int)Math.ceil((double)sizePaint.measureText(this.fileSize));
      var1.drawText(this.fileSize, (float)((var3 - var4) / 2), (float)(AndroidUtilities.dp(125.0F) + var5), sizePaint);
      TextPaint var6;
      String var9;
      if (this.loaded) {
         var9 = LocaleController.getString("OpenFile", 2131560113);
         var6 = openPaint;
         var4 = 0;
      } else {
         if (this.loading) {
            var9 = LocaleController.getString("Cancel", 2131558891).toUpperCase();
         } else {
            var9 = LocaleController.getString("TapToDownload", 2131560861);
         }

         var4 = AndroidUtilities.dp(28.0F);
         var6 = buttonPaint;
      }

      var1.drawText(var9, (float)((var3 - (int)Math.ceil((double)var6.measureText(var9))) / 2), (float)(AndroidUtilities.dp(235.0F) + var5 + var4), var6);
      if (this.progressVisible) {
         var9 = this.progress;
         if (var9 != null) {
            var4 = (int)Math.ceil((double)percentPaint.measureText(var9));
            var1.drawText(this.progress, (float)((var3 - var4) / 2), (float)(AndroidUtilities.dp(210.0F) + var5), percentPaint);
         }

         var4 = (var3 - AndroidUtilities.dp(240.0F)) / 2;
         var5 += AndroidUtilities.dp(232.0F);
         progressPaint.setColor(-10327179);
         progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F));
         float var7 = (float)((int)((float)AndroidUtilities.dp(240.0F) * this.animatedProgressValue) + var4);
         float var8 = (float)var5;
         var1.drawRect(var7, var8, (float)(AndroidUtilities.dp(240.0F) + var4), (float)(AndroidUtilities.dp(2.0F) + var5), progressPaint);
         progressPaint.setColor(-1);
         progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F));
         var7 = (float)var4;
         var1.drawRect(var7, var8, var7 + (float)AndroidUtilities.dp(240.0F) * this.animatedProgressValue, (float)(var5 + AndroidUtilities.dp(2.0F)), progressPaint);
         this.updateAnimation();
      }

      var1.restore();
   }

   public float getCurrentProgress() {
      return this.currentProgress;
   }

   public int getIntrinsicHeight() {
      return this.parentView.getMeasuredHeight();
   }

   public int getIntrinsicWidth() {
      return this.parentView.getMeasuredWidth();
   }

   public int getMinimumHeight() {
      return this.parentView.getMeasuredHeight();
   }

   public int getMinimumWidth() {
      return this.parentView.getMeasuredWidth();
   }

   public int getObserverTag() {
      return this.TAG;
   }

   public int getOpacity() {
      return -1;
   }

   public void onFailedDownload(String var1, boolean var2) {
      this.checkFileExist();
   }

   public void onProgressDownload(String var1, float var2) {
      if (!this.progressVisible) {
         this.checkFileExist();
      }

      this.setProgress(var2, true);
   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
   }

   public void onSuccessDownload(String var1) {
      this.setProgress(1.0F, true);
      this.checkFileExist();
   }

   public void recycle() {
      DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver(this);
      this.parentView = null;
      this.parentMessageObject = null;
   }

   public void setAlpha(int var1) {
      Drawable var2 = this.thumbDrawable;
      if (var2 != null) {
         var2.setAlpha(var1);
      }

      paint.setAlpha(var1);
      docPaint.setAlpha(var1);
      namePaint.setAlpha(var1);
      sizePaint.setAlpha(var1);
      buttonPaint.setAlpha(var1);
      percentPaint.setAlpha(var1);
      openPaint.setAlpha(var1);
   }

   public void setColorFilter(ColorFilter var1) {
   }

   public void setProgress(float var1, boolean var2) {
      if (!var2) {
         this.animatedProgressValue = var1;
         this.animationProgressStart = var1;
      } else {
         this.animationProgressStart = this.animatedProgressValue;
      }

      this.progress = String.format("%d%%", (int)(100.0F * var1));
      if (var1 != 1.0F) {
         this.animatedAlphaValue = 1.0F;
      }

      this.currentProgress = var1;
      this.currentProgressTime = 0L;
      this.lastUpdateTime = System.currentTimeMillis();
      this.parentView.invalidate();
   }
}
