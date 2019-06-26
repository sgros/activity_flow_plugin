package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;

public class SharedPhotoVideoCell extends FrameLayout {
   private Paint backgroundPaint = new Paint();
   private int currentAccount;
   private SharedPhotoVideoCell.SharedPhotoVideoCellDelegate delegate;
   private boolean ignoreLayout;
   private int[] indeces;
   private boolean isFirst;
   private int itemsCount;
   private MessageObject[] messageObjects;
   private SharedPhotoVideoCell.PhotoVideoView[] photoVideoViews;

   public SharedPhotoVideoCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.backgroundPaint.setColor(Theme.getColor("sharedMedia_photoPlaceholder"));
      this.messageObjects = new MessageObject[6];
      this.photoVideoViews = new SharedPhotoVideoCell.PhotoVideoView[6];
      this.indeces = new int[6];

      for(int var2 = 0; var2 < 6; ++var2) {
         this.photoVideoViews[var2] = new SharedPhotoVideoCell.PhotoVideoView(var1);
         this.addView(this.photoVideoViews[var2]);
         this.photoVideoViews[var2].setVisibility(4);
         this.photoVideoViews[var2].setTag(var2);
         this.photoVideoViews[var2].setOnClickListener(new _$$Lambda$SharedPhotoVideoCell$h2sL_iIl8gMm2fSeDVCPJaJSH54(this));
         this.photoVideoViews[var2].setOnLongClickListener(new _$$Lambda$SharedPhotoVideoCell$tNwom3pFU6ZmuNehI10sy_pOFqA(this));
      }

   }

   public BackupImageView getImageView(int var1) {
      return var1 >= this.itemsCount ? null : this.photoVideoViews[var1].imageView;
   }

   public MessageObject getMessageObject(int var1) {
      return var1 >= this.itemsCount ? null : this.messageObjects[var1];
   }

   // $FF: synthetic method
   public void lambda$new$0$SharedPhotoVideoCell(View var1) {
      if (this.delegate != null) {
         int var2 = (Integer)var1.getTag();
         this.delegate.didClickItem(this, this.indeces[var2], this.messageObjects[var2], var2);
      }

   }

   // $FF: synthetic method
   public boolean lambda$new$1$SharedPhotoVideoCell(View var1) {
      if (this.delegate != null) {
         int var2 = (Integer)var1.getTag();
         return this.delegate.didLongClickItem(this, this.indeces[var2], this.messageObjects[var2], var2);
      } else {
         return false;
      }
   }

   protected void onMeasure(int var1, int var2) {
      if (AndroidUtilities.isTablet()) {
         var2 = (AndroidUtilities.dp(490.0F) - (this.itemsCount - 1) * AndroidUtilities.dp(2.0F)) / this.itemsCount;
      } else {
         var2 = (AndroidUtilities.displaySize.x - (this.itemsCount - 1) * AndroidUtilities.dp(2.0F)) / this.itemsCount;
      }

      this.ignoreLayout = true;
      byte var3 = 0;

      int var4;
      for(var4 = 0; var4 < this.itemsCount; ++var4) {
         LayoutParams var5 = (LayoutParams)this.photoVideoViews[var4].getLayoutParams();
         int var6;
         if (this.isFirst) {
            var6 = 0;
         } else {
            var6 = AndroidUtilities.dp(2.0F);
         }

         var5.topMargin = var6;
         var5.leftMargin = (AndroidUtilities.dp(2.0F) + var2) * var4;
         if (var4 == this.itemsCount - 1) {
            if (AndroidUtilities.isTablet()) {
               var5.width = AndroidUtilities.dp(490.0F) - (this.itemsCount - 1) * (AndroidUtilities.dp(2.0F) + var2);
            } else {
               var5.width = AndroidUtilities.displaySize.x - (this.itemsCount - 1) * (AndroidUtilities.dp(2.0F) + var2);
            }
         } else {
            var5.width = var2;
         }

         var5.height = var2;
         var5.gravity = 51;
         this.photoVideoViews[var4].setLayoutParams(var5);
      }

      this.ignoreLayout = false;
      if (this.isFirst) {
         var4 = var3;
      } else {
         var4 = AndroidUtilities.dp(2.0F);
      }

      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(var4 + var2, 1073741824));
   }

   public void requestLayout() {
      if (!this.ignoreLayout) {
         super.requestLayout();
      }
   }

   public void setChecked(int var1, boolean var2, boolean var3) {
      this.photoVideoViews[var1].setChecked(var2, var3);
   }

   public void setDelegate(SharedPhotoVideoCell.SharedPhotoVideoCellDelegate var1) {
      this.delegate = var1;
   }

   public void setIsFirst(boolean var1) {
      this.isFirst = var1;
   }

   public void setItem(int var1, int var2, MessageObject var3) {
      this.messageObjects[var1] = var3;
      this.indeces[var1] = var2;
      if (var3 != null) {
         this.photoVideoViews[var1].setVisibility(0);
         this.photoVideoViews[var1].setMessageObject(var3);
      } else {
         this.photoVideoViews[var1].clearAnimation();
         this.photoVideoViews[var1].setVisibility(4);
         this.messageObjects[var1] = null;
      }

   }

   public void setItemsCount(int var1) {
      int var2 = 0;

      while(true) {
         SharedPhotoVideoCell.PhotoVideoView[] var3 = this.photoVideoViews;
         if (var2 >= var3.length) {
            this.itemsCount = var1;
            return;
         }

         var3[var2].clearAnimation();
         SharedPhotoVideoCell.PhotoVideoView var5 = this.photoVideoViews[var2];
         byte var4;
         if (var2 < var1) {
            var4 = 0;
         } else {
            var4 = 4;
         }

         var5.setVisibility(var4);
         ++var2;
      }
   }

   public void updateCheckboxColor() {
      for(int var1 = 0; var1 < 6; ++var1) {
         this.photoVideoViews[var1].checkBox.invalidate();
      }

   }

   private class PhotoVideoView extends FrameLayout {
      private AnimatorSet animator;
      private CheckBox2 checkBox;
      private FrameLayout container;
      private MessageObject currentMessageObject;
      private BackupImageView imageView;
      private View selector;
      private FrameLayout videoInfoContainer;
      private TextView videoTextView;

      public PhotoVideoView(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.container = new FrameLayout(var2);
         this.addView(this.container, LayoutHelper.createFrame(-1, -1.0F));
         this.imageView = new BackupImageView(var2);
         this.imageView.getImageReceiver().setNeedsQualityThumb(true);
         this.imageView.getImageReceiver().setShouldGenerateQualityThumb(true);
         this.container.addView(this.imageView, LayoutHelper.createFrame(-1, -1.0F));
         this.videoInfoContainer = new FrameLayout(var2) {
            private RectF rect = new RectF();

            protected void onDraw(Canvas var1) {
               this.rect.set(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), Theme.chat_timeBackgroundPaint);
            }
         };
         this.videoInfoContainer.setWillNotDraw(false);
         this.videoInfoContainer.setPadding(AndroidUtilities.dp(5.0F), 0, AndroidUtilities.dp(5.0F), 0);
         this.container.addView(this.videoInfoContainer, LayoutHelper.createFrame(-2, 17.0F, 83, 4.0F, 0.0F, 0.0F, 4.0F));
         ImageView var3 = new ImageView(var2);
         var3.setImageResource(2131165772);
         this.videoInfoContainer.addView(var3, LayoutHelper.createFrame(-2, -2, 19));
         this.videoTextView = new TextView(var2);
         this.videoTextView.setTextColor(-1);
         this.videoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.videoTextView.setTextSize(1, 12.0F);
         this.videoTextView.setImportantForAccessibility(2);
         this.videoInfoContainer.addView(this.videoTextView, LayoutHelper.createFrame(-2, -2.0F, 19, 13.0F, -0.7F, 0.0F, 0.0F));
         this.selector = new View(var2);
         this.selector.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.addView(this.selector, LayoutHelper.createFrame(-1, -1.0F));
         this.checkBox = new CheckBox2(var2);
         this.checkBox.setVisibility(4);
         this.checkBox.setColor((String)null, "sharedMedia_photoPlaceholder", "checkboxCheck");
         this.checkBox.setSize(21);
         this.checkBox.setDrawUnchecked(false);
         this.checkBox.setDrawBackgroundAsArc(1);
         this.addView(this.checkBox, LayoutHelper.createFrame(24, 24.0F, 53, 0.0F, 1.0F, 1.0F, 0.0F));
      }

      public void clearAnimation() {
         super.clearAnimation();
         AnimatorSet var1 = this.animator;
         if (var1 != null) {
            var1.cancel();
            this.animator = null;
         }

      }

      protected void onDraw(Canvas var1) {
         if (this.checkBox.isChecked() || !this.imageView.getImageReceiver().hasBitmapImage() || this.imageView.getImageReceiver().getCurrentAlpha() != 1.0F || PhotoViewer.isShowingImage(this.currentMessageObject)) {
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), SharedPhotoVideoCell.this.backgroundPaint);
         }

      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         if (this.currentMessageObject.isVideo()) {
            StringBuilder var2 = new StringBuilder();
            var2.append(LocaleController.getString("AttachVideo", 2131558733));
            var2.append(", ");
            var2.append(LocaleController.formatCallDuration(this.currentMessageObject.getDuration()));
            var1.setText(var2.toString());
         } else {
            var1.setText(LocaleController.getString("AttachPhoto", 2131558727));
         }

         if (this.checkBox.isChecked()) {
            var1.setCheckable(true);
            var1.setChecked(true);
         }

      }

      public boolean onTouchEvent(MotionEvent var1) {
         if (VERSION.SDK_INT >= 21) {
            this.selector.drawableHotspotChanged(var1.getX(), var1.getY());
         }

         return super.onTouchEvent(var1);
      }

      public void setChecked(final boolean var1, boolean var2) {
         if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
         }

         this.checkBox.setChecked(var1, var2);
         AnimatorSet var3 = this.animator;
         if (var3 != null) {
            var3.cancel();
            this.animator = null;
         }

         float var4 = 1.0F;
         float var6;
         if (var2) {
            this.animator = new AnimatorSet();
            var3 = this.animator;
            FrameLayout var5 = this.container;
            if (var1) {
               var6 = 0.81F;
            } else {
               var6 = 1.0F;
            }

            ObjectAnimator var9 = ObjectAnimator.ofFloat(var5, "scaleX", new float[]{var6});
            FrameLayout var7 = this.container;
            if (var1) {
               var4 = 0.81F;
            }

            var3.playTogether(new Animator[]{var9, ObjectAnimator.ofFloat(var7, "scaleY", new float[]{var4})});
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1x) {
                  if (PhotoVideoView.this.animator != null && PhotoVideoView.this.animator.equals(var1x)) {
                     PhotoVideoView.this.animator = null;
                  }

               }

               public void onAnimationEnd(Animator var1x) {
                  if (PhotoVideoView.this.animator != null && PhotoVideoView.this.animator.equals(var1x)) {
                     PhotoVideoView.this.animator = null;
                     if (!var1) {
                        PhotoVideoView.this.setBackgroundColor(0);
                     }
                  }

               }
            });
            this.animator.start();
         } else {
            FrameLayout var8 = this.container;
            if (var1) {
               var6 = 0.85F;
            } else {
               var6 = 1.0F;
            }

            var8.setScaleX(var6);
            var8 = this.container;
            if (var1) {
               var4 = 0.85F;
            }

            var8.setScaleY(var4);
         }

      }

      public void setMessageObject(MessageObject var1) {
         this.currentMessageObject = var1;
         this.imageView.getImageReceiver().setVisible(PhotoViewer.isShowingImage(var1) ^ true, false);
         boolean var2 = var1.isVideo();
         TLRPC.PhotoSize var3 = null;
         TLRPC.PhotoSize var4 = null;
         int var5;
         if (var2) {
            this.videoInfoContainer.setVisibility(0);
            var5 = var1.getDuration();
            int var6 = var5 / 60;
            this.videoTextView.setText(String.format("%d:%02d", var6, var5 - var6 * 60));
            TLRPC.Document var7 = var1.getDocument();
            TLRPC.PhotoSize var8 = FileLoader.getClosestPhotoSizeWithSize(var7.thumbs, 50);
            var3 = FileLoader.getClosestPhotoSizeWithSize(var7.thumbs, 320);
            if (var8 != var3) {
               var4 = var3;
            }

            if (var8 != null) {
               this.imageView.setImage(ImageLocation.getForDocument(var4, var7), "100_100", ImageLocation.getForDocument(var8, var7), "b", ApplicationLoader.applicationContext.getResources().getDrawable(2131165748), (Bitmap)null, (String)null, 0, var1);
            } else {
               this.imageView.setImageResource(2131165748);
            }
         } else {
            TLRPC.MessageMedia var10 = var1.messageOwner.media;
            if (var10 instanceof TLRPC.TL_messageMediaPhoto && var10.photo != null && !var1.photoThumbs.isEmpty()) {
               this.videoInfoContainer.setVisibility(4);
               TLRPC.PhotoSize var13 = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, 320);
               var4 = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, 50);
               if (!var1.mediaExists && !DownloadController.getInstance(SharedPhotoVideoCell.this.currentAccount).canDownloadMedia(var1)) {
                  this.imageView.setImage((ImageLocation)null, (String)null, ImageLocation.getForObject(var4, var1.photoThumbsObject), "b", ApplicationLoader.applicationContext.getResources().getDrawable(2131165748), (Bitmap)null, (String)null, 0, var1);
               } else {
                  if (var13 == var4) {
                     var4 = var3;
                  }

                  ImageReceiver var14 = this.imageView.getImageReceiver();
                  ImageLocation var9 = ImageLocation.getForObject(var13, var1.photoThumbsObject);
                  ImageLocation var11 = ImageLocation.getForObject(var4, var1.photoThumbsObject);
                  var5 = var13.size;
                  byte var12;
                  if (var1.shouldEncryptPhotoOrVideo()) {
                     var12 = 2;
                  } else {
                     var12 = 1;
                  }

                  var14.setImage(var9, "100_100", var11, "b", var5, (String)null, var1, var12);
               }
            } else {
               this.videoInfoContainer.setVisibility(4);
               this.imageView.setImageResource(2131165748);
            }
         }

      }
   }

   public interface SharedPhotoVideoCellDelegate {
      void didClickItem(SharedPhotoVideoCell var1, int var2, MessageObject var3, int var4);

      boolean didLongClickItem(SharedPhotoVideoCell var1, int var2, MessageObject var3, int var4);
   }
}
