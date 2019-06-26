package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ColorSpanUnderline;
import org.telegram.ui.Components.LayoutHelper;

public class FeaturedStickerSetInfoCell extends FrameLayout {
   private TextView addButton;
   private Drawable addDrawable;
   private int angle;
   private Paint botProgressPaint;
   private int currentAccount;
   private Drawable delDrawable;
   private boolean drawProgress;
   private boolean hasOnClick;
   private TextView infoTextView;
   private boolean isInstalled;
   private boolean isUnread;
   private long lastUpdateTime;
   private TextView nameTextView;
   private Paint paint;
   private float progressAlpha;
   private RectF rect = new RectF();
   private TLRPC.StickerSetCovered set;

   public FeaturedStickerSetInfoCell(Context var1, int var2) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.paint = new Paint(1);
      this.delDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0F), Theme.getColor("featuredStickers_delButton"), Theme.getColor("featuredStickers_delButtonPressed"));
      this.addDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0F), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed"));
      this.botProgressPaint = new Paint(1);
      this.botProgressPaint.setColor(Theme.getColor("featuredStickers_buttonProgress"));
      this.botProgressPaint.setStrokeCap(Cap.ROUND);
      this.botProgressPaint.setStyle(Style.STROKE);
      this.botProgressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("chat_emojiPanelTrendingTitle"));
      this.nameTextView.setTextSize(1, 17.0F);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.nameTextView.setSingleLine(true);
      TextView var3 = this.nameTextView;
      float var4 = (float)var2;
      this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, 51, var4, 8.0F, 40.0F, 0.0F));
      this.infoTextView = new TextView(var1);
      this.infoTextView.setTextColor(Theme.getColor("chat_emojiPanelTrendingDescription"));
      this.infoTextView.setTextSize(1, 13.0F);
      this.infoTextView.setEllipsize(TruncateAt.END);
      this.infoTextView.setSingleLine(true);
      this.addView(this.infoTextView, LayoutHelper.createFrame(-2, -2.0F, 51, var4, 30.0F, 100.0F, 0.0F));
      this.addButton = new TextView(var1) {
         protected void onDraw(Canvas var1) {
            super.onDraw(var1);
            if (FeaturedStickerSetInfoCell.this.drawProgress || !FeaturedStickerSetInfoCell.this.drawProgress && FeaturedStickerSetInfoCell.this.progressAlpha != 0.0F) {
               FeaturedStickerSetInfoCell.this.botProgressPaint.setAlpha(Math.min(255, (int)(FeaturedStickerSetInfoCell.this.progressAlpha * 255.0F)));
               int var2 = this.getMeasuredWidth() - AndroidUtilities.dp(11.0F);
               FeaturedStickerSetInfoCell.this.rect.set((float)var2, (float)AndroidUtilities.dp(3.0F), (float)(var2 + AndroidUtilities.dp(8.0F)), (float)AndroidUtilities.dp(11.0F));
               var1.drawArc(FeaturedStickerSetInfoCell.this.rect, (float)FeaturedStickerSetInfoCell.this.angle, 220.0F, false, FeaturedStickerSetInfoCell.this.botProgressPaint);
               this.invalidate((int)FeaturedStickerSetInfoCell.this.rect.left - AndroidUtilities.dp(2.0F), (int)FeaturedStickerSetInfoCell.this.rect.top - AndroidUtilities.dp(2.0F), (int)FeaturedStickerSetInfoCell.this.rect.right + AndroidUtilities.dp(2.0F), (int)FeaturedStickerSetInfoCell.this.rect.bottom + AndroidUtilities.dp(2.0F));
               long var3 = System.currentTimeMillis();
               if (Math.abs(FeaturedStickerSetInfoCell.this.lastUpdateTime - System.currentTimeMillis()) < 1000L) {
                  long var5 = var3 - FeaturedStickerSetInfoCell.this.lastUpdateTime;
                  float var7 = (float)(360L * var5) / 2000.0F;
                  FeaturedStickerSetInfoCell var8 = FeaturedStickerSetInfoCell.this;
                  var8.angle = (int)((float)var8.angle + var7);
                  var8 = FeaturedStickerSetInfoCell.this;
                  var8.angle = var8.angle - FeaturedStickerSetInfoCell.this.angle / 360 * 360;
                  if (FeaturedStickerSetInfoCell.this.drawProgress) {
                     if (FeaturedStickerSetInfoCell.this.progressAlpha < 1.0F) {
                        var8 = FeaturedStickerSetInfoCell.this;
                        var8.progressAlpha = var8.progressAlpha + (float)var5 / 200.0F;
                        if (FeaturedStickerSetInfoCell.this.progressAlpha > 1.0F) {
                           FeaturedStickerSetInfoCell.this.progressAlpha = 1.0F;
                        }
                     }
                  } else if (FeaturedStickerSetInfoCell.this.progressAlpha > 0.0F) {
                     var8 = FeaturedStickerSetInfoCell.this;
                     var8.progressAlpha = var8.progressAlpha - (float)var5 / 200.0F;
                     if (FeaturedStickerSetInfoCell.this.progressAlpha < 0.0F) {
                        FeaturedStickerSetInfoCell.this.progressAlpha = 0.0F;
                     }
                  }
               }

               FeaturedStickerSetInfoCell.this.lastUpdateTime = var3;
               this.invalidate();
            }

         }
      };
      this.addButton.setGravity(17);
      this.addButton.setTextColor(Theme.getColor("featuredStickers_buttonText"));
      this.addButton.setTextSize(1, 14.0F);
      this.addButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.addView(this.addButton, LayoutHelper.createFrame(-2, 28.0F, 53, 0.0F, 16.0F, 14.0F, 0.0F));
      this.setWillNotDraw(false);
   }

   public TLRPC.StickerSetCovered getStickerSet() {
      return this.set;
   }

   public boolean isInstalled() {
      return this.isInstalled;
   }

   protected void onDraw(Canvas var1) {
      if (this.isUnread) {
         this.paint.setColor(Theme.getColor("featuredStickers_unread"));
         var1.drawCircle((float)(this.nameTextView.getRight() + AndroidUtilities.dp(12.0F)), (float)AndroidUtilities.dp(20.0F), (float)AndroidUtilities.dp(4.0F), this.paint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0F), 1073741824));
      this.measureChildWithMargins(this.nameTextView, var1, this.addButton.getMeasuredWidth(), var2, 0);
   }

   public void setAddOnClickListener(OnClickListener var1) {
      this.hasOnClick = true;
      this.addButton.setOnClickListener(var1);
   }

   public void setDrawProgress(boolean var1) {
      this.drawProgress = var1;
      this.lastUpdateTime = System.currentTimeMillis();
      this.addButton.invalidate();
   }

   public void setStickerSet(TLRPC.StickerSetCovered var1, boolean var2) {
      this.setStickerSet(var1, var2, 0, 0);
   }

   public void setStickerSet(TLRPC.StickerSetCovered var1, boolean var2, int var3, int var4) {
      this.lastUpdateTime = System.currentTimeMillis();
      if (var4 != 0) {
         SpannableStringBuilder var5 = new SpannableStringBuilder(var1.set.title);

         try {
            ForegroundColorSpan var6 = new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4"));
            var5.setSpan(var6, var3, var4 + var3, 33);
         } catch (Exception var7) {
         }

         this.nameTextView.setText(var5);
      } else {
         this.nameTextView.setText(var1.set.title);
      }

      this.infoTextView.setText(LocaleController.formatPluralString("Stickers", var1.set.count));
      this.isUnread = var2;
      if (this.hasOnClick) {
         this.addButton.setVisibility(0);
         var2 = DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(var1.set.id);
         this.isInstalled = var2;
         if (var2) {
            this.addButton.setBackgroundDrawable(this.delDrawable);
            this.addButton.setText(LocaleController.getString("StickersRemove", 2131560811).toUpperCase());
         } else {
            this.addButton.setBackgroundDrawable(this.addDrawable);
            this.addButton.setText(LocaleController.getString("Add", 2131558555).toUpperCase());
         }

         this.addButton.setPadding(AndroidUtilities.dp(17.0F), 0, AndroidUtilities.dp(17.0F), 0);
      } else {
         this.addButton.setVisibility(8);
      }

      this.set = var1;
   }

   public void setUrl(CharSequence var1, int var2) {
      if (var1 != null) {
         SpannableStringBuilder var3 = new SpannableStringBuilder(var1);

         try {
            ColorSpanUnderline var4 = new ColorSpanUnderline(Theme.getColor("windowBackgroundWhiteBlueText4"));
            var3.setSpan(var4, 0, var2, 33);
            var4 = new ColorSpanUnderline(Theme.getColor("chat_emojiPanelTrendingDescription"));
            var3.setSpan(var4, var2, var1.length(), 33);
         } catch (Exception var5) {
         }

         this.infoTextView.setText(var3);
      }

   }
}
