package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Date;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LineProgressView;

public class SharedDocumentCell extends FrameLayout implements DownloadController.FileDownloadProgressListener {
   private int TAG;
   private CheckBox2 checkBox;
   private int currentAccount;
   private TextView dateTextView;
   private TextView extTextView;
   private boolean loaded;
   private boolean loading;
   private MessageObject message;
   private TextView nameTextView;
   private boolean needDivider;
   private ImageView placeholderImageView;
   private LineProgressView progressView;
   private ImageView statusImageView;
   private BackupImageView thumbImageView;

   public SharedDocumentCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
      this.placeholderImageView = new ImageView(var1);
      ImageView var2 = this.placeholderImageView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 12.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 12.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(40, 40.0F, var5 | 48, var6, 8.0F, var7, 0.0F));
      this.extTextView = new TextView(var1);
      this.extTextView.setTextColor(Theme.getColor("files_iconText"));
      this.extTextView.setTextSize(1, 14.0F);
      this.extTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.extTextView.setLines(1);
      this.extTextView.setMaxLines(1);
      this.extTextView.setSingleLine(true);
      this.extTextView.setGravity(17);
      this.extTextView.setEllipsize(TruncateAt.END);
      this.extTextView.setImportantForAccessibility(2);
      TextView var9 = this.extTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 16.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 16.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(32, -2.0F, var5 | 48, var6, 22.0F, var7, 0.0F));
      this.thumbImageView = new BackupImageView(var1) {
         protected void onDraw(Canvas var1) {
            boolean var2 = SharedDocumentCell.this.thumbImageView.getImageReceiver().hasBitmapImage();
            float var3 = 1.0F;
            if (var2) {
               var3 = 1.0F - SharedDocumentCell.this.thumbImageView.getImageReceiver().getCurrentAlpha();
            }

            SharedDocumentCell.this.extTextView.setAlpha(var3);
            SharedDocumentCell.this.placeholderImageView.setAlpha(var3);
            super.onDraw(var1);
         }
      };
      this.thumbImageView.setRoundRadius(AndroidUtilities.dp(4.0F));
      BackupImageView var10 = this.thumbImageView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 12.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 12.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var10, LayoutHelper.createFrame(40, 40.0F, var5 | 48, var6, 8.0F, var7, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(1, 16.0F);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setMaxLines(2);
      this.nameTextView.setEllipsize(TruncateAt.END);
      var9 = this.nameTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 16);
      var9 = this.nameTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 8.0F;
      } else {
         var6 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 72.0F;
      } else {
         var7 = 8.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 5.0F, var7, 0.0F));
      this.statusImageView = new ImageView(var1);
      this.statusImageView.setVisibility(4);
      this.statusImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("sharedMedia_startStopLoadIcon"), Mode.MULTIPLY));
      var2 = this.statusImageView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 8.0F;
      } else {
         var6 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 72.0F;
      } else {
         var7 = 8.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var6, 35.0F, var7, 0.0F));
      this.dateTextView = new TextView(var1);
      this.dateTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.dateTextView.setTextSize(1, 14.0F);
      this.dateTextView.setLines(1);
      this.dateTextView.setMaxLines(1);
      this.dateTextView.setSingleLine(true);
      this.dateTextView.setEllipsize(TruncateAt.END);
      var9 = this.dateTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var9.setGravity(var5 | 16);
      var9 = this.dateTextView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 8.0F;
      } else {
         var6 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 72.0F;
      } else {
         var7 = 8.0F;
      }

      this.addView(var9, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 30.0F, var7, 0.0F));
      this.progressView = new LineProgressView(var1);
      this.progressView.setProgressColor(Theme.getColor("sharedMedia_startStopLoadIcon"));
      LineProgressView var11 = this.progressView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 72.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 72.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var11, LayoutHelper.createFrame(-1, 2.0F, var5 | 48, var6, 54.0F, var7, 0.0F));
      this.checkBox = new CheckBox2(var1);
      this.checkBox.setVisibility(4);
      this.checkBox.setColor((String)null, "windowBackgroundWhite", "checkboxCheck");
      this.checkBox.setSize(21);
      this.checkBox.setDrawUnchecked(false);
      this.checkBox.setDrawBackgroundAsArc(2);
      CheckBox2 var8 = this.checkBox;
      if (LocaleController.isRTL) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      if (LocaleController.isRTL) {
         var6 = 0.0F;
      } else {
         var6 = 33.0F;
      }

      if (LocaleController.isRTL) {
         var7 = 33.0F;
      } else {
         var7 = 0.0F;
      }

      this.addView(var8, LayoutHelper.createFrame(24, 24.0F, var5 | 48, var6, 28.0F, var7, 0.0F));
   }

   public BackupImageView getImageView() {
      return this.thumbImageView;
   }

   public MessageObject getMessage() {
      return this.message;
   }

   public int getObserverTag() {
      return this.TAG;
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public boolean isLoading() {
      return this.loading;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.progressView.getVisibility() == 0) {
         this.updateFileExistIcon();
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         var1.drawLine((float)AndroidUtilities.dp(72.0F), (float)(this.getHeight() - 1), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

   }

   public void onFailedDownload(String var1, boolean var2) {
      this.updateFileExistIcon();
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      if (this.checkBox.isChecked()) {
         var1.setCheckable(true);
         var1.setChecked(true);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (this.nameTextView.getLineCount() > 1) {
         var2 = this.nameTextView.getMeasuredHeight() - AndroidUtilities.dp(22.0F);
         TextView var6 = this.dateTextView;
         var6.layout(var6.getLeft(), this.dateTextView.getTop() + var2, this.dateTextView.getRight(), this.dateTextView.getBottom() + var2);
         ImageView var7 = this.statusImageView;
         var7.layout(var7.getLeft(), this.statusImageView.getTop() + var2, this.statusImageView.getRight(), var2 + this.statusImageView.getBottom());
         LineProgressView var8 = this.progressView;
         var8.layout(var8.getLeft(), this.getMeasuredHeight() - this.progressView.getMeasuredHeight() - this.needDivider, this.progressView.getRight(), this.getMeasuredHeight() - this.needDivider);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0F), 1073741824));
      this.setMeasuredDimension(this.getMeasuredWidth(), AndroidUtilities.dp(34.0F) + this.nameTextView.getMeasuredHeight() + this.needDivider);
   }

   public void onProgressDownload(String var1, float var2) {
      if (this.progressView.getVisibility() != 0) {
         this.updateFileExistIcon();
      }

      this.progressView.setProgress(var2, true);
   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
   }

   public void onSuccessDownload(String var1) {
      this.progressView.setProgress(1.0F, true);
      this.updateFileExistIcon();
   }

   public void setChecked(boolean var1, boolean var2) {
      if (this.checkBox.getVisibility() != 0) {
         this.checkBox.setVisibility(0);
      }

      this.checkBox.setChecked(var1, var2);
   }

   public void setDocument(MessageObject var1, boolean var2) {
      this.needDivider = var2;
      this.message = var1;
      this.loaded = false;
      this.loading = false;
      TLRPC.Document var3 = var1.getDocument();
      String var4 = "";
      if (var1 != null && var3 != null) {
         String var5;
         int var6;
         String var7;
         String var14;
         if (!var1.isMusic()) {
            var7 = null;
         } else {
            var5 = null;
            var6 = 0;

            while(true) {
               var7 = var5;
               if (var6 >= var3.attributes.size()) {
                  break;
               }

               TLRPC.DocumentAttribute var8 = (TLRPC.DocumentAttribute)var3.attributes.get(var6);
               var7 = var5;
               if (var8 instanceof TLRPC.TL_documentAttributeAudio) {
                  label90: {
                     var7 = var8.performer;
                     if (var7 == null || var7.length() == 0) {
                        var14 = var8.title;
                        var7 = var5;
                        if (var14 == null) {
                           break label90;
                        }

                        var7 = var5;
                        if (var14.length() == 0) {
                           break label90;
                        }
                     }

                     StringBuilder var12 = new StringBuilder();
                     var12.append(var1.getMusicAuthor());
                     var12.append(" - ");
                     var12.append(var1.getMusicTitle());
                     var7 = var12.toString();
                  }
               }

               ++var6;
               var5 = var7;
            }
         }

         var14 = FileLoader.getDocumentFileName(var3);
         var5 = var7;
         if (var7 == null) {
            var5 = var14;
         }

         this.nameTextView.setText(var5);
         this.placeholderImageView.setVisibility(0);
         this.extTextView.setVisibility(0);
         this.placeholderImageView.setImageResource(AndroidUtilities.getThumbForNameOrMime(var14, var3.mime_type, false));
         TextView var15 = this.extTextView;
         var6 = var14.lastIndexOf(46);
         if (var6 == -1) {
            var5 = var4;
         } else {
            var5 = var14.substring(var6 + 1).toLowerCase();
         }

         var15.setText(var5);
         TLRPC.PhotoSize var16 = FileLoader.getClosestPhotoSizeWithSize(var3.thumbs, 320);
         TLRPC.PhotoSize var11 = FileLoader.getClosestPhotoSizeWithSize(var3.thumbs, 40);
         TLRPC.PhotoSize var13 = var16;
         if (var11 == var16) {
            var13 = null;
         }

         if (!(var11 instanceof TLRPC.TL_photoSizeEmpty) && var11 != null) {
            ImageReceiver var17 = this.thumbImageView.getImageReceiver();
            if (var13 == null) {
               var2 = true;
            } else {
               var2 = false;
            }

            var17.setNeedsQualityThumb(var2);
            var17 = this.thumbImageView.getImageReceiver();
            if (var13 == null) {
               var2 = true;
            } else {
               var2 = false;
            }

            var17.setShouldGenerateQualityThumb(var2);
            this.thumbImageView.setVisibility(0);
            this.thumbImageView.setImage(ImageLocation.getForDocument(var13, var3), "40_40", ImageLocation.getForDocument(var11, var3), "40_40_b", (String)null, 0, 1, var1);
         } else {
            this.thumbImageView.setVisibility(4);
            this.thumbImageView.setImageBitmap((Bitmap)null);
            this.extTextView.setAlpha(1.0F);
            this.placeholderImageView.setAlpha(1.0F);
         }

         long var9 = (long)var1.messageOwner.date * 1000L;
         this.dateTextView.setText(String.format("%s, %s", AndroidUtilities.formatFileSize((long)var3.size), LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(new Date(var9)), LocaleController.getInstance().formatterDay.format(new Date(var9)))));
      } else {
         this.nameTextView.setText("");
         this.extTextView.setText("");
         this.dateTextView.setText("");
         this.placeholderImageView.setVisibility(0);
         this.extTextView.setVisibility(0);
         this.extTextView.setAlpha(1.0F);
         this.placeholderImageView.setAlpha(1.0F);
         this.thumbImageView.setVisibility(4);
         this.thumbImageView.setImageBitmap((Bitmap)null);
      }

      this.setWillNotDraw(this.needDivider ^ true);
      this.progressView.setProgress(0.0F, false);
      this.updateFileExistIcon();
   }

   public void setTextAndValueAndTypeAndThumb(String var1, String var2, String var3, String var4, int var5) {
      this.nameTextView.setText(var1);
      this.dateTextView.setText(var2);
      if (var3 != null) {
         this.extTextView.setVisibility(0);
         this.extTextView.setText(var3);
      } else {
         this.extTextView.setVisibility(4);
      }

      if (var5 == 0) {
         this.placeholderImageView.setImageResource(AndroidUtilities.getThumbForNameOrMime(var1, var3, false));
         this.placeholderImageView.setVisibility(0);
      } else {
         this.placeholderImageView.setVisibility(4);
      }

      if (var4 == null && var5 == 0) {
         this.extTextView.setAlpha(1.0F);
         this.placeholderImageView.setAlpha(1.0F);
         this.thumbImageView.setImageBitmap((Bitmap)null);
         this.thumbImageView.setVisibility(4);
      } else {
         if (var4 != null) {
            this.thumbImageView.setImage(var4, "40_40", (Drawable)null);
         } else {
            CombinedDrawable var6 = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(40.0F), var5);
            Theme.setCombinedDrawableColor(var6, Theme.getColor("files_folderIconBackground"), false);
            Theme.setCombinedDrawableColor(var6, Theme.getColor("files_folderIcon"), true);
            this.thumbImageView.setImageDrawable(var6);
         }

         this.thumbImageView.setVisibility(0);
      }

   }

   public void updateFileExistIcon() {
      MessageObject var1 = this.message;
      if (var1 != null && var1.messageOwner.media != null) {
         this.loaded = false;
         if (!var1.attachPathExists && !var1.mediaExists) {
            String var5 = FileLoader.getAttachFileName(var1.getDocument());
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(var5, this.message, this);
            this.loading = FileLoader.getInstance(this.currentAccount).isLoadingFile(var5);
            this.statusImageView.setVisibility(0);
            ImageView var2 = this.statusImageView;
            int var3;
            if (this.loading) {
               var3 = 2131165559;
            } else {
               var3 = 2131165558;
            }

            var2.setImageResource(var3);
            TextView var7 = this.dateTextView;
            if (LocaleController.isRTL) {
               var3 = 0;
            } else {
               var3 = AndroidUtilities.dp(14.0F);
            }

            int var4;
            if (LocaleController.isRTL) {
               var4 = AndroidUtilities.dp(14.0F);
            } else {
               var4 = 0;
            }

            var7.setPadding(var3, 0, var4, 0);
            if (this.loading) {
               this.progressView.setVisibility(0);
               Float var8 = ImageLoader.getInstance().getFileProgress(var5);
               Float var6 = var8;
               if (var8 == null) {
                  var6 = 0.0F;
               }

               this.progressView.setProgress(var6, false);
            } else {
               this.progressView.setVisibility(4);
            }
         } else {
            this.statusImageView.setVisibility(4);
            this.progressView.setVisibility(4);
            this.dateTextView.setPadding(0, 0, 0, 0);
            this.loading = false;
            this.loaded = true;
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
         }
      } else {
         this.loading = false;
         this.loaded = true;
         this.progressView.setVisibility(4);
         this.progressView.setProgress(0.0F, false);
         this.statusImageView.setVisibility(4);
         this.dateTextView.setPadding(0, 0, 0, 0);
         DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
      }

   }
}
