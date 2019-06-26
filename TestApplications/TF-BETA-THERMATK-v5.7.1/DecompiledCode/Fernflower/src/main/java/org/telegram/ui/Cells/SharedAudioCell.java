package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
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
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgress2;

public class SharedAudioCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
   private int TAG;
   private boolean buttonPressed;
   private int buttonState;
   private int buttonX;
   private int buttonY;
   private CheckBox2 checkBox;
   private int currentAccount;
   private MessageObject currentMessageObject;
   private StaticLayout descriptionLayout;
   private int descriptionY = AndroidUtilities.dp(29.0F);
   private int hasMiniProgress;
   private boolean miniButtonPressed;
   private int miniButtonState;
   private boolean needDivider;
   private RadialProgress2 radialProgress;
   private StaticLayout titleLayout;
   private int titleY = AndroidUtilities.dp(9.0F);

   public SharedAudioCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.setFocusable(true);
      this.radialProgress = new RadialProgress2(this);
      this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
      this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
      this.setWillNotDraw(false);
      this.checkBox = new CheckBox2(var1);
      this.checkBox.setVisibility(4);
      this.checkBox.setColor((String)null, "windowBackgroundWhite", "checkboxCheck");
      this.checkBox.setSize(21);
      this.checkBox.setDrawUnchecked(false);
      CheckBox2 var5 = this.checkBox;
      byte var2 = 3;
      var5.setDrawBackgroundAsArc(3);
      var5 = this.checkBox;
      if (LocaleController.isRTL) {
         var2 = 5;
      }

      float var3;
      if (LocaleController.isRTL) {
         var3 = 0.0F;
      } else {
         var3 = 38.0F;
      }

      float var4;
      if (LocaleController.isRTL) {
         var4 = 39.0F;
      } else {
         var4 = 0.0F;
      }

      this.addView(var5, LayoutHelper.createFrame(24, 24.0F, var2 | 48, var3, 32.0F, var4, 0.0F));
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

         if (this.needPlayMessage(this.currentMessageObject)) {
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

   public MessageObject getMessage() {
      return this.currentMessageObject;
   }

   public int getObserverTag() {
      return this.TAG;
   }

   public void initStreamingIcons() {
      this.radialProgress.initMiniIcons();
   }

   protected boolean needPlayMessage(MessageObject var1) {
      return false;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.radialProgress.onAttachedToWindow();
      this.updateButtonState(false, false);
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
      this.radialProgress.onDetachedFromWindow();
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
      if (this.needDivider) {
         var1.drawLine((float)AndroidUtilities.dp(72.0F), (float)(this.getHeight() - 1), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

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

      if (this.checkBox.isChecked()) {
         var1.setCheckable(true);
         var1.setChecked(true);
      }

   }

   @SuppressLint({"DrawAllocation"})
   protected void onMeasure(int var1, int var2) {
      this.descriptionLayout = null;
      this.titleLayout = null;
      int var3 = MeasureSpec.getSize(var1) - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - AndroidUtilities.dp(28.0F);

      String var4;
      int var5;
      try {
         var4 = this.currentMessageObject.getMusicTitle();
         var5 = (int)Math.ceil((double)Theme.chat_contextResult_titleTextPaint.measureText(var4));
         CharSequence var6 = TextUtils.ellipsize(var4.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint, (float)Math.min(var5, var3), TruncateAt.END);
         StaticLayout var10 = new StaticLayout(var6, Theme.chat_contextResult_titleTextPaint, var3 + AndroidUtilities.dp(4.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.titleLayout = var10;
      } catch (Exception var9) {
         FileLog.e((Throwable)var9);
      }

      try {
         var4 = this.currentMessageObject.getMusicAuthor();
         var5 = (int)Math.ceil((double)Theme.chat_contextResult_descriptionTextPaint.measureText(var4));
         CharSequence var11 = TextUtils.ellipsize(var4.replace('\n', ' '), Theme.chat_contextResult_descriptionTextPaint, (float)Math.min(var5, var3), TruncateAt.END);
         StaticLayout var13 = new StaticLayout(var11, Theme.chat_contextResult_descriptionTextPaint, var3 + AndroidUtilities.dp(4.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.descriptionLayout = var13;
      } catch (Exception var8) {
         FileLog.e((Throwable)var8);
      }

      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(56.0F) + this.needDivider);
      var3 = AndroidUtilities.dp(52.0F);
      if (LocaleController.isRTL) {
         var3 = MeasureSpec.getSize(var1) - AndroidUtilities.dp(8.0F) - var3;
      } else {
         var3 = AndroidUtilities.dp(8.0F);
      }

      RadialProgress2 var12 = this.radialProgress;
      var5 = AndroidUtilities.dp(4.0F) + var3;
      this.buttonX = var5;
      int var7 = AndroidUtilities.dp(6.0F);
      this.buttonY = var7;
      var12.setProgressRect(var5, var7, var3 + AndroidUtilities.dp(48.0F), AndroidUtilities.dp(50.0F));
      this.measureChildWithMargins(this.checkBox, var1, 0, var2, 0);
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
            this.radialProgress.setPressed(this.buttonPressed, false);
            this.radialProgress.setPressed(this.miniButtonPressed, true);
            var2 = false;
         }

         return var2;
      }
   }

   public void setChecked(boolean var1, boolean var2) {
      if (this.checkBox.getVisibility() != 0) {
         this.checkBox.setVisibility(0);
      }

      this.checkBox.setChecked(var1, var2);
   }

   public void setMessageObject(MessageObject var1, boolean var2) {
      this.needDivider = var2;
      this.currentMessageObject = var1;
      TLRPC.Document var3 = var1.getDocument();
      TLRPC.PhotoSize var4;
      if (var3 != null) {
         var4 = FileLoader.getClosestPhotoSizeWithSize(var3.thumbs, 90);
      } else {
         var4 = null;
      }

      if (var4 instanceof TLRPC.TL_photoSize) {
         this.radialProgress.setImageOverlay(var4, var3, var1);
      } else {
         String var5 = var1.getArtworkUrl(true);
         if (!TextUtils.isEmpty(var5)) {
            this.radialProgress.setImageOverlay(var5);
         } else {
            this.radialProgress.setImageOverlay((TLRPC.PhotoSize)null, (TLRPC.Document)null, (Object)null);
         }
      }

      this.updateButtonState(false, false);
      this.requestLayout();
   }

   public void updateButtonState(boolean var1, boolean var2) {
      String var3 = this.currentMessageObject.getFileName();
      if (!TextUtils.isEmpty(var3)) {
         MessageObject var4 = this.currentMessageObject;
         boolean var5;
         if (!var4.attachPathExists && !var4.mediaExists) {
            var5 = false;
         } else {
            var5 = true;
         }

         if (SharedConfig.streamMedia && this.currentMessageObject.isMusic() && (int)this.currentMessageObject.getDialogId() != 0) {
            byte var10;
            if (var5) {
               var10 = 1;
            } else {
               var10 = 2;
            }

            this.hasMiniProgress = var10;
            var5 = true;
         } else {
            this.hasMiniProgress = 0;
            this.miniButtonState = -1;
         }

         boolean var7;
         Float var9;
         if (this.hasMiniProgress != 0) {
            RadialProgress2 var6 = this.radialProgress;
            String var8;
            if (this.currentMessageObject.isOutOwner()) {
               var8 = "chat_outLoader";
            } else {
               var8 = "chat_inLoader";
            }

            var6.setMiniProgressBackgroundColor(Theme.getColor(var8));
            var7 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (!var7 || var7 && MediaController.getInstance().isMessagePaused()) {
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
                  var9 = ImageLoader.getInstance().getFileProgress(var3);
                  if (var9 != null) {
                     this.radialProgress.setProgress(var9, var2);
                  } else {
                     this.radialProgress.setProgress(0.0F, var2);
                  }
               }
            }
         } else if (var5) {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            var7 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (!var7 || var7 && MediaController.getInstance().isMessagePaused()) {
               this.buttonState = 0;
            } else {
               this.buttonState = 1;
            }

            this.radialProgress.setProgress(1.0F, var2);
            this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            this.invalidate();
         } else {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var3, this.currentMessageObject, this);
            if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var3)) {
               this.buttonState = 2;
               this.radialProgress.setProgress(0.0F, var2);
               this.radialProgress.setIcon(this.getIconForCurrentState(), var1, var2);
            } else {
               this.buttonState = 4;
               var9 = ImageLoader.getInstance().getFileProgress(var3);
               if (var9 != null) {
                  this.radialProgress.setProgress(var9, var2);
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
