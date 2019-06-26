package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RadialProgress2;

public class AudioPlayerCell extends View implements DownloadController.FileDownloadProgressListener {
   private int TAG;
   private boolean buttonPressed;
   private int buttonState;
   private int buttonX;
   private int buttonY;
   private int currentAccount;
   private MessageObject currentMessageObject;
   private StaticLayout descriptionLayout;
   private int descriptionY = AndroidUtilities.dp(29.0F);
   private int hasMiniProgress;
   private boolean miniButtonPressed;
   private int miniButtonState;
   private RadialProgress2 radialProgress;
   private StaticLayout titleLayout;
   private int titleY = AndroidUtilities.dp(9.0F);

   public AudioPlayerCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.radialProgress = new RadialProgress2(this);
      this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
      this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
      this.setFocusable(true);
   }

   private boolean checkAudioMotionEvent(MotionEvent var1) {
      boolean var6;
      boolean var8;
      label42: {
         int var2 = (int)var1.getX();
         int var3 = (int)var1.getY();
         int var4 = AndroidUtilities.dp(36.0F);
         int var5 = this.miniButtonState;
         var6 = true;
         if (var5 >= 0) {
            var5 = AndroidUtilities.dp(27.0F);
            int var7 = this.buttonX;
            if (var2 >= var7 + var5 && var2 <= var7 + var5 + var4) {
               var2 = this.buttonY;
               if (var3 >= var2 + var5 && var3 <= var2 + var5 + var4) {
                  var8 = true;
                  break label42;
               }
            }
         }

         var8 = false;
      }

      if (var1.getAction() == 0) {
         if (var8) {
            this.miniButtonPressed = true;
            this.radialProgress.setPressed(this.miniButtonPressed, true);
            this.invalidate();
            return var6;
         }
      } else if (this.miniButtonPressed) {
         if (var1.getAction() == 1) {
            this.miniButtonPressed = false;
            this.playSoundEffect(0);
            this.didPressedMiniButton(true);
            this.invalidate();
         } else if (var1.getAction() == 3) {
            this.miniButtonPressed = false;
            this.invalidate();
         } else if (var1.getAction() == 2 && !var8) {
            this.miniButtonPressed = false;
            this.invalidate();
         }

         this.radialProgress.setPressed(this.miniButtonPressed, true);
      }

      var6 = false;
      return var6;
   }

   private void didPressedMiniButton(boolean var1) {
      int var2 = this.miniButtonState;
      if (var2 == 0) {
         this.miniButtonState = 1;
         this.radialProgress.setProgress(0.0F, false);
         FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
         this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
         this.invalidate();
      } else if (var2 == 1) {
         if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
            MediaController.getInstance().cleanupPlayer(true, true);
         }

         this.miniButtonState = 0;
         FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
         this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
         this.invalidate();
      }

   }

   private int getIconForCurrentState() {
      int var1 = this.buttonState;
      if (var1 == 1) {
         return 1;
      } else if (var1 == 2) {
         return 2;
      } else {
         return var1 == 4 ? 3 : 0;
      }
   }

   private int getMiniIconForCurrentState() {
      int var1 = this.miniButtonState;
      if (var1 < 0) {
         return 4;
      } else {
         return var1 == 0 ? 2 : 3;
      }
   }

   public void didPressedButton() {
      int var1 = this.buttonState;
      if (var1 == 0) {
         if (this.miniButtonState == 0) {
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
         }

         if (MediaController.getInstance().findMessageInPlaylistAndPlay(this.currentMessageObject)) {
            if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
               this.miniButtonState = 1;
               this.radialProgress.setProgress(0.0F, false);
               this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
            }

            this.buttonState = 1;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
            this.invalidate();
         }
      } else if (var1 == 1) {
         if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
            this.buttonState = 0;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
            this.invalidate();
         }
      } else if (var1 == 2) {
         this.radialProgress.setProgress(0.0F, false);
         FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
         this.buttonState = 4;
         this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
         this.invalidate();
      } else if (var1 == 4) {
         FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
         this.buttonState = 2;
         this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
         this.invalidate();
      }

   }

   public MessageObject getMessageObject() {
      return this.currentMessageObject;
   }

   public int getObserverTag() {
      return this.TAG;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.radialProgress.onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.radialProgress.onDetachedFromWindow();
      DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
   }

   protected void onDraw(Canvas var1) {
      StaticLayout var2 = this.titleLayout;
      float var3 = 8.0F;
      float var4;
      if (var2 != null) {
         var1.save();
         if (LocaleController.isRTL) {
            var4 = 8.0F;
         } else {
            var4 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var4), (float)this.titleY);
         this.titleLayout.draw(var1);
         var1.restore();
      }

      if (this.descriptionLayout != null) {
         Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         var1.save();
         if (LocaleController.isRTL) {
            var4 = var3;
         } else {
            var4 = (float)AndroidUtilities.leftBaseline;
         }

         var1.translate((float)AndroidUtilities.dp(var4), (float)this.descriptionY);
         this.descriptionLayout.draw(var1);
         var1.restore();
      }

      RadialProgress2 var5 = this.radialProgress;
      String var6;
      if (this.buttonPressed) {
         var6 = "chat_inAudioSelectedProgress";
      } else {
         var6 = "chat_inAudioProgress";
      }

      var5.setProgressColor(Theme.getColor(var6));
      this.radialProgress.draw(var1);
   }

   public void onFailedDownload(String var1, boolean var2) {
      this.updateButtonState(true, var2);
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      if (this.currentMessageObject.isMusic()) {
         var1.setText(LocaleController.formatString("AccDescrMusicInfo", 2131558447, this.currentMessageObject.getMusicAuthor(), this.currentMessageObject.getMusicTitle()));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(this.titleLayout.getText());
         var2.append(", ");
         var2.append(this.descriptionLayout.getText());
         var1.setText(var2.toString());
      }

   }

   @SuppressLint({"DrawAllocation"})
   protected void onMeasure(int var1, int var2) {
      this.descriptionLayout = null;
      this.titleLayout = null;
      var2 = MeasureSpec.getSize(var1) - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - AndroidUtilities.dp(28.0F);

      String var3;
      int var4;
      StaticLayout var5;
      CharSequence var8;
      try {
         var3 = this.currentMessageObject.getMusicTitle();
         var4 = (int)Math.ceil((double)Theme.chat_contextResult_titleTextPaint.measureText(var3));
         var8 = TextUtils.ellipsize(var3.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint, (float)Math.min(var4, var2), TruncateAt.END);
         var5 = new StaticLayout(var8, Theme.chat_contextResult_titleTextPaint, var2 + AndroidUtilities.dp(4.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.titleLayout = var5;
      } catch (Exception var7) {
         FileLog.e((Throwable)var7);
      }

      try {
         var3 = this.currentMessageObject.getMusicAuthor();
         var4 = (int)Math.ceil((double)Theme.chat_contextResult_descriptionTextPaint.measureText(var3));
         var8 = TextUtils.ellipsize(var3.replace('\n', ' '), Theme.chat_contextResult_descriptionTextPaint, (float)Math.min(var4, var2), TruncateAt.END);
         var5 = new StaticLayout(var8, Theme.chat_contextResult_descriptionTextPaint, var2 + AndroidUtilities.dp(4.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.descriptionLayout = var5;
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(56.0F));
      var2 = AndroidUtilities.dp(52.0F);
      if (LocaleController.isRTL) {
         var1 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(8.0F) - var2;
      } else {
         var1 = AndroidUtilities.dp(8.0F);
      }

      RadialProgress2 var9 = this.radialProgress;
      var4 = AndroidUtilities.dp(4.0F) + var1;
      this.buttonX = var4;
      var2 = AndroidUtilities.dp(6.0F);
      this.buttonY = var2;
      var9.setProgressRect(var4, var2, var1 + AndroidUtilities.dp(48.0F), AndroidUtilities.dp(50.0F));
   }

   public void onProgressDownload(String var1, float var2) {
      this.radialProgress.setProgress(var2, true);
      if (this.hasMiniProgress != 0) {
         if (this.miniButtonState != 1) {
            this.updateButtonState(false, true);
         }
      } else if (this.buttonState != 4) {
         this.updateButtonState(false, true);
      }

   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
   }

   public void onSuccessDownload(String var1) {
      this.radialProgress.setProgress(1.0F, true);
      this.updateButtonState(false, true);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.currentMessageObject == null) {
         return super.onTouchEvent(var1);
      } else {
         boolean var2 = this.checkAudioMotionEvent(var1);
         if (var1.getAction() == 3) {
            this.miniButtonPressed = false;
            this.buttonPressed = false;
            var2 = false;
         }

         return var2;
      }
   }

   public void setMessageObject(MessageObject var1) {
      this.currentMessageObject = var1;
      TLRPC.Document var2 = var1.getDocument();
      TLRPC.PhotoSize var3;
      if (var2 != null) {
         var3 = FileLoader.getClosestPhotoSizeWithSize(var2.thumbs, 90);
      } else {
         var3 = null;
      }

      if (var3 instanceof TLRPC.TL_photoSize) {
         this.radialProgress.setImageOverlay(var3, var2, var1);
      } else {
         String var4 = var1.getArtworkUrl(true);
         if (!TextUtils.isEmpty(var4)) {
            this.radialProgress.setImageOverlay(var4);
         } else {
            this.radialProgress.setImageOverlay((TLRPC.PhotoSize)null, (TLRPC.Document)null, (Object)null);
         }
      }

      this.requestLayout();
      this.updateButtonState(false, false);
   }

   public void updateButtonState(boolean var1, boolean var2) {
      String var3 = this.currentMessageObject.getFileName();
      boolean var4 = TextUtils.isEmpty(this.currentMessageObject.messageOwner.attachPath);
      File var5 = null;
      File var6 = var5;
      if (!var4) {
         var6 = new File(this.currentMessageObject.messageOwner.attachPath);
         if (!var6.exists()) {
            var6 = var5;
         }
      }

      var5 = var6;
      if (var6 == null) {
         var5 = FileLoader.getPathToAttach(this.currentMessageObject.getDocument());
      }

      if (!TextUtils.isEmpty(var3)) {
         if (var5.exists() && var5.length() == 0L) {
            var5.delete();
         }

         var4 = var5.exists();
         if (SharedConfig.streamMedia && (int)this.currentMessageObject.getDialogId() != 0) {
            byte var7;
            if (var4) {
               var7 = 1;
            } else {
               var7 = 2;
            }

            this.hasMiniProgress = var7;
            var4 = true;
         } else {
            this.miniButtonState = -1;
         }

         Float var10;
         if (this.hasMiniProgress != 0) {
            RadialProgress2 var8 = this.radialProgress;
            String var9;
            if (this.currentMessageObject.isOutOwner()) {
               var9 = "chat_outLoader";
            } else {
               var9 = "chat_inLoader";
            }

            var8.setMiniProgressBackgroundColor(Theme.getColor(var9));
            var4 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (!var4 || var4 && MediaController.getInstance().isMessagePaused()) {
               this.buttonState = 0;
            } else {
               this.buttonState = 1;
            }

            this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            if (this.hasMiniProgress == 1) {
               DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
               this.miniButtonState = -1;
               this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), var1, var2);
            } else {
               DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var3, this.currentMessageObject, this);
               if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var3)) {
                  this.miniButtonState = 0;
                  this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), var1, var2);
               } else {
                  this.miniButtonState = 1;
                  this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), var1, var2);
                  var10 = ImageLoader.getInstance().getFileProgress(var3);
                  if (var10 != null) {
                     this.radialProgress.setProgress(var10, var2);
                  } else {
                     this.radialProgress.setProgress(0.0F, var2);
                  }
               }
            }
         } else if (var4) {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            var4 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (!var4 || var4 && MediaController.getInstance().isMessagePaused()) {
               this.buttonState = 0;
            } else {
               this.buttonState = 1;
            }

            this.radialProgress.setProgress(1.0F, var2);
            this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            this.invalidate();
         } else {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var3, this);
            if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var3)) {
               this.buttonState = 2;
               this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            } else {
               this.buttonState = 4;
               var10 = ImageLoader.getInstance().getFileProgress(var3);
               if (var10 != null) {
                  this.radialProgress.setProgress(var10, var2);
               } else {
                  this.radialProgress.setProgress(0.0F, var2);
               }

               this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            }

            this.invalidate();
         }

      }
   }
}
