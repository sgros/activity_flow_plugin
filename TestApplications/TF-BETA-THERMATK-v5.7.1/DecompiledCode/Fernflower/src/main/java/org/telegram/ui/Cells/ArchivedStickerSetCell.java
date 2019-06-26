package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Switch;

public class ArchivedStickerSetCell extends FrameLayout {
   private Switch checkBox;
   private BackupImageView imageView;
   private boolean needDivider;
   private Switch.OnCheckedChangeListener onCheckedChangeListener;
   private Rect rect = new Rect();
   private TLRPC.StickerSetCovered stickersSet;
   private TextView textView;
   private TextView valueTextView;

   public ArchivedStickerSetCell(Context var1, boolean var2) {
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
      if (var2) {
         var7 = 71.0F;
      } else {
         var7 = 21.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, var6, 71.0F, 10.0F, var7, 0.0F));
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

      if (var2) {
         var7 = 71.0F;
      } else {
         var7 = 21.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, var6, 71.0F, 35.0F, var7, 0.0F));
      this.imageView = new BackupImageView(var1);
      this.imageView.setAspectFit(true);
      BackupImageView var10 = this.imageView;
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

      float var8;
      if (LocaleController.isRTL) {
         var8 = 12.0F;
      } else {
         var8 = 0.0F;
      }

      this.addView(var10, LayoutHelper.createFrame(48, 48.0F, var6 | 48, var7, 8.0F, var8, 0.0F));
      if (var2) {
         this.checkBox = new Switch(var1);
         this.checkBox.setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
         Switch var9 = this.checkBox;
         var6 = var5;
         if (LocaleController.isRTL) {
            var6 = 3;
         }

         this.addView(var9, LayoutHelper.createFrame(37, 40.0F, var6 | 16, 16.0F, 0.0F, 16.0F, 0.0F));
      }

   }

   public Switch getCheckBox() {
      return this.checkBox;
   }

   public TLRPC.StickerSetCovered getStickersSet() {
      return this.stickersSet;
   }

   public TextView getTextView() {
      return this.textView;
   }

   public TextView getValueTextView() {
      return this.valueTextView;
   }

   public boolean isChecked() {
      Switch var1 = this.checkBox;
      boolean var2;
      if (var1 != null && var1.isChecked()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   // $FF: synthetic method
   public void lambda$setOnCheckClick$0$ArchivedStickerSetCell(View var1) {
      Switch var2 = this.checkBox;
      var2.setChecked(var2.isChecked() ^ true, true);
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
      Switch var2 = this.checkBox;
      if (var2 != null) {
         var2.getHitRect(this.rect);
         if (this.rect.contains((int)var1.getX(), (int)var1.getY())) {
            var1.offsetLocation(-this.checkBox.getX(), -this.checkBox.getY());
            return this.checkBox.onTouchEvent(var1);
         }
      }

      return super.onTouchEvent(var1);
   }

   public void setChecked(boolean var1) {
      this.checkBox.setOnCheckedChangeListener((Switch.OnCheckedChangeListener)null);
      this.checkBox.setChecked(var1, true);
      this.checkBox.setOnCheckedChangeListener(this.onCheckedChangeListener);
   }

   public void setOnCheckClick(Switch.OnCheckedChangeListener var1) {
      Switch var2 = this.checkBox;
      this.onCheckedChangeListener = var1;
      var2.setOnCheckedChangeListener(var1);
      this.checkBox.setOnClickListener(new _$$Lambda$ArchivedStickerSetCell$9Rmaru_mwDl6BO3ARD8hu93oKu8(this));
   }

   public void setStickersSet(TLRPC.StickerSetCovered var1, boolean var2) {
      this.needDivider = var2;
      this.stickersSet = var1;
      this.setWillNotDraw(this.needDivider ^ true);
      this.textView.setText(this.stickersSet.set.title);
      this.valueTextView.setText(LocaleController.formatPluralString("Stickers", var1.set.count));
      TLRPC.Document var3 = var1.cover;
      TLRPC.PhotoSize var5;
      if (var3 != null) {
         var5 = FileLoader.getClosestPhotoSizeWithSize(var3.thumbs, 90);
      } else {
         var5 = null;
      }

      if (var5 != null && var5.location != null) {
         this.imageView.setImage(ImageLocation.getForDocument(var5, var1.cover), (String)null, "webp", (Drawable)null, var1);
      } else if (!var1.covers.isEmpty()) {
         TLRPC.Document var4 = (TLRPC.Document)var1.covers.get(0);
         var5 = FileLoader.getClosestPhotoSizeWithSize(var4.thumbs, 90);
         this.imageView.setImage(ImageLocation.getForDocument(var5, var4), (String)null, "webp", (Drawable)null, var1);
      } else {
         this.imageView.setImage((ImageLocation)null, (String)null, "webp", (Drawable)null, var1);
      }

   }
}
