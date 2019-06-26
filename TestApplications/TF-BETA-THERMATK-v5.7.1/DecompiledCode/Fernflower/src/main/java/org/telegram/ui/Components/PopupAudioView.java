package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.BaseCell;

public class PopupAudioView extends BaseCell implements SeekBar.SeekBarDelegate, DownloadController.FileDownloadProgressListener {
   private int TAG;
   private int buttonPressed = 0;
   private int buttonState = 0;
   private int buttonX;
   private int buttonY;
   private int currentAccount;
   protected MessageObject currentMessageObject;
   private String lastTimeString = null;
   private ProgressView progressView;
   private SeekBar seekBar;
   private int seekBarX;
   private int seekBarY;
   private StaticLayout timeLayout;
   private TextPaint timePaint = new TextPaint(1);
   int timeWidth = 0;
   private int timeX;
   private boolean wasLayout = false;

   public PopupAudioView(Context var1) {
      super(var1);
      this.timePaint.setTextSize((float)AndroidUtilities.dp(16.0F));
      this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
      this.seekBar = new SeekBar(this.getContext());
      this.seekBar.setDelegate(this);
      this.progressView = new ProgressView();
   }

   private void didPressedButton() {
      int var1 = this.buttonState;
      if (var1 == 0) {
         boolean var2 = MediaController.getInstance().playMessage(this.currentMessageObject);
         if (!this.currentMessageObject.isOut() && this.currentMessageObject.isContentUnread() && this.currentMessageObject.messageOwner.to_id.channel_id == 0) {
            MessagesController.getInstance(this.currentAccount).markMessageContentAsRead(this.currentMessageObject);
            this.currentMessageObject.setContentIsRead();
         }

         if (var2) {
            this.buttonState = 1;
            this.invalidate();
         }
      } else if (var1 == 1) {
         if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
            this.buttonState = 0;
            this.invalidate();
         }
      } else if (var1 == 2) {
         FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
         this.buttonState = 4;
         this.invalidate();
      } else if (var1 == 3) {
         FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
         this.buttonState = 2;
         this.invalidate();
      }

   }

   public void downloadAudioIfNeed() {
      if (this.buttonState == 2) {
         FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
         this.buttonState = 3;
         this.invalidate();
      }

   }

   public final MessageObject getMessageObject() {
      return this.currentMessageObject;
   }

   public int getObserverTag() {
      return this.TAG;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
   }

   protected void onDraw(Canvas var1) {
      if (this.currentMessageObject != null) {
         if (!this.wasLayout) {
            this.requestLayout();
         } else {
            BaseCell.setDrawableBounds(Theme.chat_msgInMediaDrawable, 0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            Theme.chat_msgInMediaDrawable.draw(var1);
            if (this.currentMessageObject != null) {
               var1.save();
               int var2 = this.buttonState;
               if (var2 != 0 && var2 != 1) {
                  var1.translate((float)(this.seekBarX + AndroidUtilities.dp(12.0F)), (float)this.seekBarY);
                  this.progressView.draw(var1);
               } else {
                  var1.translate((float)this.seekBarX, (float)this.seekBarY);
                  this.seekBar.draw(var1);
               }

               var1.restore();
               var2 = this.buttonState;
               this.timePaint.setColor(-6182221);
               Drawable var3 = Theme.chat_fileStatesDrawable[var2 + 5][this.buttonPressed];
               int var4 = AndroidUtilities.dp(36.0F);
               var2 = (var4 - var3.getIntrinsicWidth()) / 2;
               var4 = (var4 - var3.getIntrinsicHeight()) / 2;
               BaseCell.setDrawableBounds(var3, var2 + this.buttonX, var4 + this.buttonY);
               var3.draw(var1);
               var1.save();
               var1.translate((float)this.timeX, (float)AndroidUtilities.dp(18.0F));
               this.timeLayout.draw(var1);
               var1.restore();
            }
         }
      }
   }

   public void onFailedDownload(String var1, boolean var2) {
      this.updateButtonState();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.currentMessageObject != null) {
         this.seekBarX = AndroidUtilities.dp(54.0F);
         this.buttonX = AndroidUtilities.dp(10.0F);
         this.timeX = this.getMeasuredWidth() - this.timeWidth - AndroidUtilities.dp(16.0F);
         this.seekBar.setSize(this.getMeasuredWidth() - AndroidUtilities.dp(70.0F) - this.timeWidth, AndroidUtilities.dp(30.0F));
         this.progressView.width = this.getMeasuredWidth() - AndroidUtilities.dp(94.0F) - this.timeWidth;
         this.progressView.height = AndroidUtilities.dp(30.0F);
         this.seekBarY = AndroidUtilities.dp(13.0F);
         this.buttonY = AndroidUtilities.dp(10.0F);
         this.updateProgress();
         if (var1 || !this.wasLayout) {
            this.wasLayout = true;
         }

      }
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(56.0F));
   }

   public void onProgressDownload(String var1, float var2) {
      this.progressView.setProgress(var2);
      if (this.buttonState != 3) {
         this.updateButtonState();
      }

      this.invalidate();
   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
   }

   public void onSeekBarDrag(float var1) {
      MessageObject var2 = this.currentMessageObject;
      if (var2 != null) {
         var2.audioProgress = var1;
         MediaController.getInstance().seekToProgress(this.currentMessageObject, var1);
      }
   }

   public void onSuccessDownload(String var1) {
      this.updateButtonState();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      float var2 = var1.getX();
      float var3 = var1.getY();
      boolean var4 = this.seekBar.onTouch(var1.getAction(), var1.getX() - (float)this.seekBarX, var1.getY() - (float)this.seekBarY);
      if (var4) {
         if (var1.getAction() == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
         }

         this.invalidate();
      } else {
         int var5 = AndroidUtilities.dp(36.0F);
         int var6;
         boolean var7;
         if (var1.getAction() == 0) {
            var6 = this.buttonX;
            var7 = var4;
            if (var2 >= (float)var6) {
               var7 = var4;
               if (var2 <= (float)(var6 + var5)) {
                  var6 = this.buttonY;
                  var7 = var4;
                  if (var3 >= (float)var6) {
                     var7 = var4;
                     if (var3 <= (float)(var6 + var5)) {
                        this.buttonPressed = 1;
                        this.invalidate();
                        var7 = true;
                     }
                  }
               }
            }
         } else {
            var7 = var4;
            if (this.buttonPressed == 1) {
               if (var1.getAction() == 1) {
                  this.buttonPressed = 0;
                  this.playSoundEffect(0);
                  this.didPressedButton();
                  this.invalidate();
                  var7 = var4;
               } else if (var1.getAction() == 3) {
                  this.buttonPressed = 0;
                  this.invalidate();
                  var7 = var4;
               } else {
                  var7 = var4;
                  if (var1.getAction() == 2) {
                     label59: {
                        var6 = this.buttonX;
                        if (var2 >= (float)var6 && var2 <= (float)(var6 + var5)) {
                           var6 = this.buttonY;
                           if (var3 >= (float)var6) {
                              var7 = var4;
                              if (var3 <= (float)(var6 + var5)) {
                                 break label59;
                              }
                           }
                        }

                        this.buttonPressed = 0;
                        this.invalidate();
                        var7 = var4;
                     }
                  }
               }
            }
         }

         var4 = var7;
         if (!var7) {
            var4 = super.onTouchEvent(var1);
         }
      }

      return var4;
   }

   public void setMessageObject(MessageObject var1) {
      if (this.currentMessageObject != var1) {
         this.currentAccount = var1.currentAccount;
         this.seekBar.setColors(Theme.getColor("chat_inAudioSeekbar"), Theme.getColor("chat_inAudioSeekbar"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarSelected"));
         this.progressView.setProgressColors(-2497813, -7944712);
         this.currentMessageObject = var1;
         this.wasLayout = false;
         this.requestLayout();
      }

      this.updateButtonState();
   }

   public void updateButtonState() {
      String var1 = this.currentMessageObject.getFileName();
      if (FileLoader.getPathToMessage(this.currentMessageObject.messageOwner).exists()) {
         DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
         boolean var2 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
         if (!var2 || var2 && MediaController.getInstance().isMessagePaused()) {
            this.buttonState = 0;
         } else {
            this.buttonState = 1;
         }

         this.progressView.setProgress(0.0F);
      } else {
         DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var1, this);
         if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(var1)) {
            this.buttonState = 2;
            this.progressView.setProgress(0.0F);
         } else {
            this.buttonState = 3;
            Float var3 = ImageLoader.getInstance().getFileProgress(var1);
            if (var3 != null) {
               this.progressView.setProgress(var3);
            } else {
               this.progressView.setProgress(0.0F);
            }
         }
      }

      this.updateProgress();
   }

   public void updateProgress() {
      if (this.currentMessageObject != null) {
         if (!this.seekBar.isDragging()) {
            this.seekBar.setProgress(this.currentMessageObject.audioProgress);
         }

         int var1;
         if (!MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
            var1 = 0;

            while(true) {
               if (var1 >= this.currentMessageObject.getDocument().attributes.size()) {
                  var1 = 0;
                  break;
               }

               TLRPC.DocumentAttribute var2 = (TLRPC.DocumentAttribute)this.currentMessageObject.getDocument().attributes.get(var1);
               if (var2 instanceof TLRPC.TL_documentAttributeAudio) {
                  var1 = var2.duration;
                  break;
               }

               ++var1;
            }
         } else {
            var1 = this.currentMessageObject.audioProgressSec;
         }

         String var4 = String.format("%02d:%02d", var1 / 60, var1 % 60);
         String var3 = this.lastTimeString;
         if (var3 == null || var3 != null && !var3.equals(var4)) {
            this.timeWidth = (int)Math.ceil((double)this.timePaint.measureText(var4));
            this.timeLayout = new StaticLayout(var4, this.timePaint, this.timeWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         }

         this.invalidate();
      }
   }
}
