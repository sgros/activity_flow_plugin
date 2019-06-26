// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.ui.Components.ColorSpanUnderline;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import android.view.View$OnClickListener;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils$TruncateAt;
import android.graphics.Paint$Style;
import android.graphics.Paint$Cap;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.graphics.RectF;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.widget.FrameLayout;

public class FeaturedStickerSetInfoCell extends FrameLayout
{
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
    private RectF rect;
    private TLRPC.StickerSetCovered set;
    
    public FeaturedStickerSetInfoCell(final Context context, final int n) {
        super(context);
        this.rect = new RectF();
        this.currentAccount = UserConfig.selectedAccount;
        this.paint = new Paint(1);
        this.delDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), Theme.getColor("featuredStickers_delButton"), Theme.getColor("featuredStickers_delButtonPressed"));
        this.addDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed"));
        (this.botProgressPaint = new Paint(1)).setColor(Theme.getColor("featuredStickers_buttonProgress"));
        this.botProgressPaint.setStrokeCap(Paint$Cap.ROUND);
        this.botProgressPaint.setStyle(Paint$Style.STROKE);
        this.botProgressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("chat_emojiPanelTrendingTitle"));
        this.nameTextView.setTextSize(1, 17.0f);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.nameTextView.setSingleLine(true);
        final TextView nameTextView = this.nameTextView;
        final float n2 = (float)n;
        this.addView((View)nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, n2, 8.0f, 40.0f, 0.0f));
        (this.infoTextView = new TextView(context)).setTextColor(Theme.getColor("chat_emojiPanelTrendingDescription"));
        this.infoTextView.setTextSize(1, 13.0f);
        this.infoTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.infoTextView.setSingleLine(true);
        this.addView((View)this.infoTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, n2, 30.0f, 100.0f, 0.0f));
        (this.addButton = new TextView(context) {
            protected void onDraw(final Canvas canvas) {
                super.onDraw(canvas);
                if (FeaturedStickerSetInfoCell.this.drawProgress || (!FeaturedStickerSetInfoCell.this.drawProgress && FeaturedStickerSetInfoCell.this.progressAlpha != 0.0f)) {
                    FeaturedStickerSetInfoCell.this.botProgressPaint.setAlpha(Math.min(255, (int)(FeaturedStickerSetInfoCell.this.progressAlpha * 255.0f)));
                    final int n = this.getMeasuredWidth() - AndroidUtilities.dp(11.0f);
                    FeaturedStickerSetInfoCell.this.rect.set((float)n, (float)AndroidUtilities.dp(3.0f), (float)(n + AndroidUtilities.dp(8.0f)), (float)AndroidUtilities.dp(11.0f));
                    canvas.drawArc(FeaturedStickerSetInfoCell.this.rect, (float)FeaturedStickerSetInfoCell.this.angle, 220.0f, false, FeaturedStickerSetInfoCell.this.botProgressPaint);
                    this.invalidate((int)FeaturedStickerSetInfoCell.this.rect.left - AndroidUtilities.dp(2.0f), (int)FeaturedStickerSetInfoCell.this.rect.top - AndroidUtilities.dp(2.0f), (int)FeaturedStickerSetInfoCell.this.rect.right + AndroidUtilities.dp(2.0f), (int)FeaturedStickerSetInfoCell.this.rect.bottom + AndroidUtilities.dp(2.0f));
                    final long currentTimeMillis = System.currentTimeMillis();
                    if (Math.abs(FeaturedStickerSetInfoCell.this.lastUpdateTime - System.currentTimeMillis()) < 1000L) {
                        final long n2 = currentTimeMillis - FeaturedStickerSetInfoCell.this.lastUpdateTime;
                        final float n3 = 360L * n2 / 2000.0f;
                        final FeaturedStickerSetInfoCell this$0 = FeaturedStickerSetInfoCell.this;
                        this$0.angle += n3;
                        final FeaturedStickerSetInfoCell this$2 = FeaturedStickerSetInfoCell.this;
                        this$2.angle -= FeaturedStickerSetInfoCell.this.angle / 360 * 360;
                        if (FeaturedStickerSetInfoCell.this.drawProgress) {
                            if (FeaturedStickerSetInfoCell.this.progressAlpha < 1.0f) {
                                final FeaturedStickerSetInfoCell this$3 = FeaturedStickerSetInfoCell.this;
                                this$3.progressAlpha += n2 / 200.0f;
                                if (FeaturedStickerSetInfoCell.this.progressAlpha > 1.0f) {
                                    FeaturedStickerSetInfoCell.this.progressAlpha = 1.0f;
                                }
                            }
                        }
                        else if (FeaturedStickerSetInfoCell.this.progressAlpha > 0.0f) {
                            final FeaturedStickerSetInfoCell this$4 = FeaturedStickerSetInfoCell.this;
                            this$4.progressAlpha -= n2 / 200.0f;
                            if (FeaturedStickerSetInfoCell.this.progressAlpha < 0.0f) {
                                FeaturedStickerSetInfoCell.this.progressAlpha = 0.0f;
                            }
                        }
                    }
                    FeaturedStickerSetInfoCell.this.lastUpdateTime = currentTimeMillis;
                    this.invalidate();
                }
            }
        }).setGravity(17);
        this.addButton.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.addButton.setTextSize(1, 14.0f);
        this.addButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addView((View)this.addButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 28.0f, 53, 0.0f, 16.0f, 14.0f, 0.0f));
        this.setWillNotDraw(false);
    }
    
    public TLRPC.StickerSetCovered getStickerSet() {
        return this.set;
    }
    
    public boolean isInstalled() {
        return this.isInstalled;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.isUnread) {
            this.paint.setColor(Theme.getColor("featuredStickers_unread"));
            canvas.drawCircle((float)(this.nameTextView.getRight() + AndroidUtilities.dp(12.0f)), (float)AndroidUtilities.dp(20.0f), (float)AndroidUtilities.dp(4.0f), this.paint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
        this.measureChildWithMargins((View)this.nameTextView, n, this.addButton.getMeasuredWidth(), n2, 0);
    }
    
    public void setAddOnClickListener(final View$OnClickListener onClickListener) {
        this.hasOnClick = true;
        this.addButton.setOnClickListener(onClickListener);
    }
    
    public void setDrawProgress(final boolean drawProgress) {
        this.drawProgress = drawProgress;
        this.lastUpdateTime = System.currentTimeMillis();
        this.addButton.invalidate();
    }
    
    public void setStickerSet(final TLRPC.StickerSetCovered stickerSetCovered, final boolean b) {
        this.setStickerSet(stickerSetCovered, b, 0, 0);
    }
    
    public void setStickerSet(final TLRPC.StickerSetCovered set, boolean stickerPackInstalled, final int n, final int n2) {
        this.lastUpdateTime = System.currentTimeMillis();
        Label_0070: {
            if (n2 == 0) {
                break Label_0070;
            }
            final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)set.set.title);
            while (true) {
                try {
                    text.setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), n, n2 + n, 33);
                    this.nameTextView.setText((CharSequence)text);
                    while (true) {
                        this.infoTextView.setText((CharSequence)LocaleController.formatPluralString("Stickers", set.set.count));
                        this.isUnread = stickerPackInstalled;
                        if (this.hasOnClick) {
                            this.addButton.setVisibility(0);
                            stickerPackInstalled = DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(set.set.id);
                            if (this.isInstalled = stickerPackInstalled) {
                                this.addButton.setBackgroundDrawable(this.delDrawable);
                                this.addButton.setText((CharSequence)LocaleController.getString("StickersRemove", 2131560811).toUpperCase());
                            }
                            else {
                                this.addButton.setBackgroundDrawable(this.addDrawable);
                                this.addButton.setText((CharSequence)LocaleController.getString("Add", 2131558555).toUpperCase());
                            }
                            this.addButton.setPadding(AndroidUtilities.dp(17.0f), 0, AndroidUtilities.dp(17.0f), 0);
                        }
                        else {
                            this.addButton.setVisibility(8);
                        }
                        this.set = set;
                        return;
                        this.nameTextView.setText((CharSequence)set.set.title);
                        continue;
                    }
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public void setUrl(final CharSequence charSequence, final int n) {
        if (charSequence == null) {
            return;
        }
        final SpannableStringBuilder text = new SpannableStringBuilder(charSequence);
        while (true) {
            try {
                text.setSpan((Object)new ColorSpanUnderline(Theme.getColor("windowBackgroundWhiteBlueText4")), 0, n, 33);
                text.setSpan((Object)new ColorSpanUnderline(Theme.getColor("chat_emojiPanelTrendingDescription")), n, charSequence.length(), 33);
                this.infoTextView.setText((CharSequence)text);
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
}
