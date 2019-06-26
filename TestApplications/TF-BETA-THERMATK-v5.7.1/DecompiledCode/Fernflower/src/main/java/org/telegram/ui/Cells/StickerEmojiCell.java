package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class StickerEmojiCell extends FrameLayout {
   private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5F);
   private float alpha = 1.0F;
   private boolean changingAlpha;
   private int currentAccount;
   private TextView emojiTextView;
   private BackupImageView imageView;
   private long lastUpdateTime;
   private Object parentObject;
   private boolean recent;
   private float scale;
   private boolean scaled;
   private TLRPC.Document sticker;
   private long time;

   public StickerEmojiCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.imageView = new BackupImageView(var1);
      this.imageView.setAspectFit(true);
      this.addView(this.imageView, LayoutHelper.createFrame(66, 66, 17));
      this.emojiTextView = new TextView(var1);
      this.emojiTextView.setTextSize(1, 16.0F);
      this.addView(this.emojiTextView, LayoutHelper.createFrame(28, 28, 85));
      this.setFocusable(true);
   }

   public void disable() {
      this.changingAlpha = true;
      this.alpha = 0.5F;
      this.time = 0L;
      this.imageView.getImageReceiver().setAlpha(this.alpha);
      this.imageView.invalidate();
      this.lastUpdateTime = System.currentTimeMillis();
      this.invalidate();
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = super.drawChild(var1, var2, var3);
      if (var2 == this.imageView && (this.changingAlpha || this.scaled && this.scale != 0.8F || !this.scaled && this.scale != 1.0F)) {
         long var6 = System.currentTimeMillis();
         var3 = var6 - this.lastUpdateTime;
         this.lastUpdateTime = var6;
         if (this.changingAlpha) {
            this.time += var3;
            if (this.time > 1050L) {
               this.time = 1050L;
            }

            this.alpha = interpolator.getInterpolation((float)this.time / 1050.0F) * 0.5F + 0.5F;
            if (this.alpha >= 1.0F) {
               this.changingAlpha = false;
               this.alpha = 1.0F;
            }

            this.imageView.getImageReceiver().setAlpha(this.alpha);
         } else {
            label53: {
               if (this.scaled) {
                  float var8 = this.scale;
                  if (var8 != 0.8F) {
                     this.scale = var8 - (float)var3 / 400.0F;
                     if (this.scale < 0.8F) {
                        this.scale = 0.8F;
                     }
                     break label53;
                  }
               }

               this.scale += (float)var3 / 400.0F;
               if (this.scale > 1.0F) {
                  this.scale = 1.0F;
               }
            }
         }

         this.imageView.setScaleX(this.scale);
         this.imageView.setScaleY(this.scale);
         this.imageView.invalidate();
         this.invalidate();
      }

      return var5;
   }

   public Object getParentObject() {
      return this.parentObject;
   }

   public TLRPC.Document getSticker() {
      return this.sticker;
   }

   public void invalidate() {
      this.emojiTextView.invalidate();
      super.invalidate();
   }

   public boolean isDisabled() {
      return this.changingAlpha;
   }

   public boolean isRecent() {
      return this.recent;
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      String var2 = LocaleController.getString("AttachSticker", 2131558730);
      int var3 = 0;

      String var4;
      while(true) {
         var4 = var2;
         if (var3 >= this.sticker.attributes.size()) {
            break;
         }

         TLRPC.DocumentAttribute var5 = (TLRPC.DocumentAttribute)this.sticker.attributes.get(var3);
         if (var5 instanceof TLRPC.TL_documentAttributeSticker) {
            String var6 = var5.alt;
            var4 = var2;
            if (var6 != null) {
               var4 = var2;
               if (var6.length() > 0) {
                  TextView var7 = this.emojiTextView;
                  var7.setText(Emoji.replaceEmoji(var5.alt, var7.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0F), false));
                  StringBuilder var8 = new StringBuilder();
                  var8.append(var5.alt);
                  var8.append(" ");
                  var8.append(var2);
                  var4 = var8.toString();
               }
            }
            break;
         }

         ++var3;
      }

      var1.setContentDescription(var4);
      var1.setEnabled(true);
   }

   public void setRecent(boolean var1) {
      this.recent = var1;
   }

   public void setScaled(boolean var1) {
      this.scaled = var1;
      this.lastUpdateTime = System.currentTimeMillis();
      this.invalidate();
   }

   public void setSticker(TLRPC.Document var1, Object var2, String var3, boolean var4) {
      if (var1 != null) {
         this.sticker = var1;
         this.parentObject = var2;
         TLRPC.PhotoSize var8 = FileLoader.getClosestPhotoSizeWithSize(var1.thumbs, 90);
         if (var8 != null) {
            this.imageView.setImage(ImageLocation.getForDocument(var8, var1), (String)null, "webp", (Drawable)null, this.parentObject);
         } else {
            this.imageView.setImage(ImageLocation.getForDocument(var1), (String)null, "webp", (Drawable)null, this.parentObject);
         }

         TextView var6;
         if (var3 != null) {
            var6 = this.emojiTextView;
            var6.setText(Emoji.replaceEmoji(var3, var6.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0F), false));
            this.emojiTextView.setVisibility(0);
         } else if (var4) {
            boolean var10;
            label36: {
               for(int var5 = 0; var5 < var1.attributes.size(); ++var5) {
                  TLRPC.DocumentAttribute var9 = (TLRPC.DocumentAttribute)var1.attributes.get(var5);
                  if (var9 instanceof TLRPC.TL_documentAttributeSticker) {
                     String var7 = var9.alt;
                     if (var7 != null && var7.length() > 0) {
                        var6 = this.emojiTextView;
                        var6.setText(Emoji.replaceEmoji(var9.alt, var6.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0F), false));
                        var10 = true;
                        break label36;
                     }
                     break;
                  }
               }

               var10 = false;
            }

            if (!var10) {
               this.emojiTextView.setText(Emoji.replaceEmoji(DataQuery.getInstance(this.currentAccount).getEmojiForSticker(this.sticker.id), this.emojiTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0F), false));
            }

            this.emojiTextView.setVisibility(0);
         } else {
            this.emojiTextView.setVisibility(4);
         }
      }

   }

   public void setSticker(TLRPC.Document var1, Object var2, boolean var3) {
      this.setSticker(var1, var2, (String)null, var3);
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
