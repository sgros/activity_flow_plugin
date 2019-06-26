package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class StickerCell extends FrameLayout {
   private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5F);
   private boolean clearsInputField;
   private BackupImageView imageView;
   private long lastUpdateTime;
   private float scale;
   private boolean scaled;
   private TLRPC.Document sticker;
   private long time = 0L;

   public StickerCell(Context var1) {
      super(var1);
      this.imageView = new BackupImageView(var1);
      this.imageView.setAspectFit(true);
      this.addView(this.imageView, LayoutHelper.createFrame(66, 66.0F, 1, 0.0F, 5.0F, 0.0F, 0.0F));
      this.setFocusable(true);
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = super.drawChild(var1, var2, var3);
      if (var2 == this.imageView && (this.scaled && this.scale != 0.8F || !this.scaled && this.scale != 1.0F)) {
         label22: {
            long var6 = System.currentTimeMillis();
            var3 = var6 - this.lastUpdateTime;
            this.lastUpdateTime = var6;
            if (this.scaled) {
               float var8 = this.scale;
               if (var8 != 0.8F) {
                  this.scale = var8 - (float)var3 / 400.0F;
                  if (this.scale < 0.8F) {
                     this.scale = 0.8F;
                  }
                  break label22;
               }
            }

            this.scale += (float)var3 / 400.0F;
            if (this.scale > 1.0F) {
               this.scale = 1.0F;
            }
         }

         this.imageView.setScaleX(this.scale);
         this.imageView.setScaleY(this.scale);
         this.imageView.invalidate();
         this.invalidate();
      }

      return var5;
   }

   public TLRPC.Document getSticker() {
      return this.sticker;
   }

   public boolean isClearsInputField() {
      return this.clearsInputField;
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      if (this.sticker != null) {
         int var2 = 0;

         String var3;
         for(var3 = null; var2 < this.sticker.attributes.size(); ++var2) {
            TLRPC.DocumentAttribute var4 = (TLRPC.DocumentAttribute)this.sticker.attributes.get(var2);
            if (var4 instanceof TLRPC.TL_documentAttributeSticker) {
               var3 = var4.alt;
               if (var3 != null && var3.length() > 0) {
                  var3 = var4.alt;
               } else {
                  var3 = null;
               }
            }
         }

         if (var3 != null) {
            StringBuilder var5 = new StringBuilder();
            var5.append(var3);
            var5.append(" ");
            var5.append(LocaleController.getString("AttachSticker", 2131558730));
            var1.setText(var5.toString());
         } else {
            var1.setText(LocaleController.getString("AttachSticker", 2131558730));
         }

         var1.setEnabled(true);
      }
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(76.0F) + this.getPaddingLeft() + this.getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(78.0F), 1073741824));
   }

   public void setClearsInputField(boolean var1) {
      this.clearsInputField = var1;
   }

   public void setPressed(boolean var1) {
      if (this.imageView.getImageReceiver().getPressed() != var1) {
         this.imageView.getImageReceiver().setPressed(var1);
         this.imageView.invalidate();
      }

      super.setPressed((boolean)var1);
   }

   public void setScaled(boolean var1) {
      this.scaled = var1;
      this.lastUpdateTime = System.currentTimeMillis();
      this.invalidate();
   }

   public void setSticker(TLRPC.Document var1, Object var2, int var3) {
      if (var1 != null) {
         TLRPC.PhotoSize var4 = FileLoader.getClosestPhotoSizeWithSize(var1.thumbs, 90);
         this.imageView.setImage(ImageLocation.getForDocument(var4, var1), (String)null, "webp", (Drawable)null, var2);
      }

      this.sticker = var1;
      if (var3 == -1) {
         this.setBackgroundResource(2131165862);
         this.setPadding(AndroidUtilities.dp(7.0F), 0, 0, 0);
      } else if (var3 == 0) {
         this.setBackgroundResource(2131165861);
         this.setPadding(0, 0, 0, 0);
      } else if (var3 == 1) {
         this.setBackgroundResource(2131165863);
         this.setPadding(0, 0, AndroidUtilities.dp(7.0F), 0);
      } else if (var3 == 2) {
         this.setBackgroundResource(2131165859);
         this.setPadding(AndroidUtilities.dp(3.0F), 0, AndroidUtilities.dp(3.0F), 0);
      }

      Drawable var5 = this.getBackground();
      if (var5 != null) {
         var5.setAlpha(230);
         var5.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel"), Mode.MULTIPLY));
      }

   }

   public boolean showingBitmap() {
      boolean var1;
      if (this.imageView.getImageReceiver().getBitmap() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
