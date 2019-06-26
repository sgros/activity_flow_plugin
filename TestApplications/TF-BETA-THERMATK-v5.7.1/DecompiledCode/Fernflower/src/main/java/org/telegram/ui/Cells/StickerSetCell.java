package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;

public class StickerSetCell extends FrameLayout {
   private BackupImageView imageView;
   private boolean needDivider;
   private ImageView optionsButton;
   private RadialProgressView progressView;
   private Rect rect = new Rect();
   private TLRPC.TL_messages_stickerSet stickersSet;
   private TextView textView;
   private TextView valueTextView;

   public StickerSetCell(Context var1, int var2) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var3 = this.textView;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var3.setGravity(var6);
      var3 = this.textView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 40.0F;
      } else {
         var7 = 71.0F;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 71.0F;
      } else {
         var8 = 40.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, var6, var7, 10.0F, var8, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.valueTextView.setTextSize(1, 13.0F);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      var3 = this.valueTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var3.setGravity(var6);
      var3 = this.valueTextView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 40.0F;
      } else {
         var7 = 71.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 71.0F;
      } else {
         var8 = 40.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, var6, var7, 35.0F, var8, 0.0F));
      this.imageView = new BackupImageView(var1);
      this.imageView.setAspectFit(true);
      BackupImageView var12 = this.imageView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 12.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 12.0F;
      } else {
         var8 = 0.0F;
      }

      this.addView(var12, LayoutHelper.createFrame(48, 48.0F, var6 | 48, var7, 8.0F, var8, 0.0F));
      if (var2 == 2) {
         this.progressView = new RadialProgressView(this.getContext());
         this.progressView.setProgressColor(Theme.getColor("dialogProgressCircle"));
         this.progressView.setSize(AndroidUtilities.dp(30.0F));
         RadialProgressView var9 = this.progressView;
         if (!LocaleController.isRTL) {
            var5 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = 12.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 12.0F;
         } else {
            var8 = 0.0F;
         }

         this.addView(var9, LayoutHelper.createFrame(48, 48.0F, var5 | 48, var7, 8.0F, var8, 0.0F));
      } else if (var2 != 0) {
         this.optionsButton = new ImageView(var1);
         ImageView var10 = this.optionsButton;
         var6 = 0;
         var10.setFocusable(false);
         this.optionsButton.setScaleType(ScaleType.CENTER);
         this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
         if (var2 == 1) {
            this.optionsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("stickers_menu"), Mode.MULTIPLY));
            this.optionsButton.setImageResource(2131165610);
            var10 = this.optionsButton;
            if (LocaleController.isRTL) {
               var5 = 3;
            }

            this.addView(var10, LayoutHelper.createFrame(40, 40, var5 | 48));
         } else if (var2 == 3) {
            this.optionsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
            this.optionsButton.setImageResource(2131165858);
            var10 = this.optionsButton;
            if (LocaleController.isRTL) {
               var5 = 3;
            }

            byte var11;
            if (LocaleController.isRTL) {
               var11 = 10;
            } else {
               var11 = 0;
            }

            var7 = (float)var11;
            if (LocaleController.isRTL) {
               var11 = var6;
            } else {
               var11 = 10;
            }

            this.addView(var10, LayoutHelper.createFrame(40, 40.0F, var5 | 48, var7, 12.0F, (float)var11, 0.0F));
         }
      }

   }

   public TLRPC.TL_messages_stickerSet getStickersSet() {
      return this.stickersSet;
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         var1.drawLine(0.0F, (float)(this.getHeight() - 1), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + this.needDivider, 1073741824));
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (VERSION.SDK_INT >= 21 && this.getBackground() != null) {
         ImageView var2 = this.optionsButton;
         if (var2 != null) {
            var2.getHitRect(this.rect);
            if (this.rect.contains((int)var1.getX(), (int)var1.getY())) {
               return true;
            }
         }
      }

      return super.onTouchEvent(var1);
   }

   public void setChecked(boolean var1) {
      ImageView var2 = this.optionsButton;
      if (var2 != null) {
         byte var3;
         if (var1) {
            var3 = 0;
         } else {
            var3 = 4;
         }

         var2.setVisibility(var3);
      }
   }

   public void setOnOptionsClick(OnClickListener var1) {
      ImageView var2 = this.optionsButton;
      if (var2 != null) {
         var2.setOnClickListener(var1);
      }
   }

   public void setStickersSet(TLRPC.TL_messages_stickerSet var1, boolean var2) {
      this.needDivider = var2;
      this.stickersSet = var1;
      this.imageView.setVisibility(0);
      RadialProgressView var3 = this.progressView;
      if (var3 != null) {
         var3.setVisibility(4);
      }

      this.textView.setTranslationY(0.0F);
      this.textView.setText(this.stickersSet.set.title);
      if (this.stickersSet.set.archived) {
         this.textView.setAlpha(0.5F);
         this.valueTextView.setAlpha(0.5F);
         this.imageView.setAlpha(0.5F);
      } else {
         this.textView.setAlpha(1.0F);
         this.valueTextView.setAlpha(1.0F);
         this.imageView.setAlpha(1.0F);
      }

      ArrayList var5 = var1.documents;
      if (var5 != null && !var5.isEmpty()) {
         this.valueTextView.setText(LocaleController.formatPluralString("Stickers", var5.size()));
         TLRPC.Document var4 = (TLRPC.Document)var5.get(0);
         TLRPC.PhotoSize var6 = FileLoader.getClosestPhotoSizeWithSize(var4.thumbs, 90);
         this.imageView.setImage(ImageLocation.getForDocument(var6, var4), (String)null, "webp", (Drawable)null, var1);
      } else {
         this.valueTextView.setText(LocaleController.formatPluralString("Stickers", 0));
      }

   }

   public void setText(String var1, String var2, int var3, boolean var4) {
      this.needDivider = var4;
      this.stickersSet = null;
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      if (TextUtils.isEmpty(var2)) {
         this.textView.setTranslationY((float)AndroidUtilities.dp(10.0F));
      } else {
         this.textView.setTranslationY(0.0F);
      }

      RadialProgressView var5;
      if (var3 != 0) {
         this.imageView.setImageResource(var3, Theme.getColor("windowBackgroundWhiteGrayIcon"));
         this.imageView.setVisibility(0);
         var5 = this.progressView;
         if (var5 != null) {
            var5.setVisibility(4);
         }
      } else {
         this.imageView.setVisibility(4);
         var5 = this.progressView;
         if (var5 != null) {
            var5.setVisibility(0);
         }
      }

   }
}
