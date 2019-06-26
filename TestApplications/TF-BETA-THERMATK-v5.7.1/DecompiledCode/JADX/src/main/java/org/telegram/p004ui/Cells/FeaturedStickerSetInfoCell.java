package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.exoplayer2.util.NalUnitUtil;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.ColorSpanUnderline;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.TLRPC.StickerSetCovered;

/* renamed from: org.telegram.ui.Cells.FeaturedStickerSetInfoCell */
public class FeaturedStickerSetInfoCell extends FrameLayout {
    private TextView addButton;
    private Drawable addDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.m26dp(4.0f), Theme.getColor(Theme.key_featuredStickers_addButton), Theme.getColor(Theme.key_featuredStickers_addButtonPressed));
    private int angle;
    private Paint botProgressPaint = new Paint(1);
    private int currentAccount = UserConfig.selectedAccount;
    private Drawable delDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.m26dp(4.0f), Theme.getColor(Theme.key_featuredStickers_delButton), Theme.getColor(Theme.key_featuredStickers_delButtonPressed));
    private boolean drawProgress;
    private boolean hasOnClick;
    private TextView infoTextView;
    private boolean isInstalled;
    private boolean isUnread;
    private long lastUpdateTime;
    private TextView nameTextView;
    private Paint paint = new Paint(1);
    private float progressAlpha;
    private RectF rect = new RectF();
    private StickerSetCovered set;

    public FeaturedStickerSetInfoCell(Context context, int i) {
        super(context);
        this.botProgressPaint.setColor(Theme.getColor(Theme.key_featuredStickers_buttonProgress));
        this.botProgressPaint.setStrokeCap(Cap.ROUND);
        this.botProgressPaint.setStyle(Style.STROKE);
        this.botProgressPaint.setStrokeWidth((float) AndroidUtilities.m26dp(2.0f));
        this.nameTextView = new TextView(context);
        this.nameTextView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelTrendingTitle));
        this.nameTextView.setTextSize(1, 17.0f);
        String str = "fonts/rmedium.ttf";
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface(str));
        this.nameTextView.setEllipsize(TruncateAt.END);
        this.nameTextView.setSingleLine(true);
        float f = (float) i;
        addView(this.nameTextView, LayoutHelper.createFrame(-2, -2.0f, 51, f, 8.0f, 40.0f, 0.0f));
        this.infoTextView = new TextView(context);
        this.infoTextView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelTrendingDescription));
        this.infoTextView.setTextSize(1, 13.0f);
        this.infoTextView.setEllipsize(TruncateAt.END);
        this.infoTextView.setSingleLine(true);
        addView(this.infoTextView, LayoutHelper.createFrame(-2, -2.0f, 51, f, 30.0f, 100.0f, 0.0f));
        this.addButton = new TextView(context) {
            /* Access modifiers changed, original: protected */
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (FeaturedStickerSetInfoCell.this.drawProgress || !(FeaturedStickerSetInfoCell.this.drawProgress || FeaturedStickerSetInfoCell.this.progressAlpha == 0.0f)) {
                    FeaturedStickerSetInfoCell.this.botProgressPaint.setAlpha(Math.min(NalUnitUtil.EXTENDED_SAR, (int) (FeaturedStickerSetInfoCell.this.progressAlpha * 255.0f)));
                    int measuredWidth = getMeasuredWidth() - AndroidUtilities.m26dp(11.0f);
                    FeaturedStickerSetInfoCell.this.rect.set((float) measuredWidth, (float) AndroidUtilities.m26dp(3.0f), (float) (measuredWidth + AndroidUtilities.m26dp(8.0f)), (float) AndroidUtilities.m26dp(11.0f));
                    canvas.drawArc(FeaturedStickerSetInfoCell.this.rect, (float) FeaturedStickerSetInfoCell.this.angle, 220.0f, false, FeaturedStickerSetInfoCell.this.botProgressPaint);
                    invalidate(((int) FeaturedStickerSetInfoCell.this.rect.left) - AndroidUtilities.m26dp(2.0f), ((int) FeaturedStickerSetInfoCell.this.rect.top) - AndroidUtilities.m26dp(2.0f), ((int) FeaturedStickerSetInfoCell.this.rect.right) + AndroidUtilities.m26dp(2.0f), ((int) FeaturedStickerSetInfoCell.this.rect.bottom) + AndroidUtilities.m26dp(2.0f));
                    long currentTimeMillis = System.currentTimeMillis();
                    if (Math.abs(FeaturedStickerSetInfoCell.this.lastUpdateTime - System.currentTimeMillis()) < 1000) {
                        long access$500 = currentTimeMillis - FeaturedStickerSetInfoCell.this.lastUpdateTime;
                        float f = ((float) (360 * access$500)) / 2000.0f;
                        FeaturedStickerSetInfoCell featuredStickerSetInfoCell = FeaturedStickerSetInfoCell.this;
                        featuredStickerSetInfoCell.angle = (int) (((float) featuredStickerSetInfoCell.angle) + f);
                        FeaturedStickerSetInfoCell featuredStickerSetInfoCell2 = FeaturedStickerSetInfoCell.this;
                        featuredStickerSetInfoCell2.angle = featuredStickerSetInfoCell2.angle - ((FeaturedStickerSetInfoCell.this.angle / 360) * 360);
                        if (FeaturedStickerSetInfoCell.this.drawProgress) {
                            if (FeaturedStickerSetInfoCell.this.progressAlpha < 1.0f) {
                                featuredStickerSetInfoCell2 = FeaturedStickerSetInfoCell.this;
                                featuredStickerSetInfoCell2.progressAlpha = featuredStickerSetInfoCell2.progressAlpha + (((float) access$500) / 200.0f);
                                if (FeaturedStickerSetInfoCell.this.progressAlpha > 1.0f) {
                                    FeaturedStickerSetInfoCell.this.progressAlpha = 1.0f;
                                }
                            }
                        } else if (FeaturedStickerSetInfoCell.this.progressAlpha > 0.0f) {
                            featuredStickerSetInfoCell2 = FeaturedStickerSetInfoCell.this;
                            featuredStickerSetInfoCell2.progressAlpha = featuredStickerSetInfoCell2.progressAlpha - (((float) access$500) / 200.0f);
                            if (FeaturedStickerSetInfoCell.this.progressAlpha < 0.0f) {
                                FeaturedStickerSetInfoCell.this.progressAlpha = 0.0f;
                            }
                        }
                    }
                    FeaturedStickerSetInfoCell.this.lastUpdateTime = currentTimeMillis;
                    invalidate();
                }
            }
        };
        this.addButton.setGravity(17);
        this.addButton.setTextColor(Theme.getColor(Theme.key_featuredStickers_buttonText));
        this.addButton.setTextSize(1, 14.0f);
        this.addButton.setTypeface(AndroidUtilities.getTypeface(str));
        addView(this.addButton, LayoutHelper.createFrame(-2, 28.0f, 53, 0.0f, 16.0f, 14.0f, 0.0f));
        setWillNotDraw(false);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(60.0f), 1073741824));
        measureChildWithMargins(this.nameTextView, i, this.addButton.getMeasuredWidth(), i2, 0);
    }

    public void setAddOnClickListener(OnClickListener onClickListener) {
        this.hasOnClick = true;
        this.addButton.setOnClickListener(onClickListener);
    }

    public void setStickerSet(StickerSetCovered stickerSetCovered, boolean z) {
        setStickerSet(stickerSetCovered, z, 0, 0);
    }

    public void setStickerSet(StickerSetCovered stickerSetCovered, boolean z, int i, int i2) {
        this.lastUpdateTime = System.currentTimeMillis();
        if (i2 != 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stickerSetCovered.set.title);
            try {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4)), i, i2 + i, 33);
            } catch (Exception unused) {
            }
            this.nameTextView.setText(spannableStringBuilder);
        } else {
            this.nameTextView.setText(stickerSetCovered.set.title);
        }
        this.infoTextView.setText(LocaleController.formatPluralString("Stickers", stickerSetCovered.set.count));
        this.isUnread = z;
        if (this.hasOnClick) {
            this.addButton.setVisibility(0);
            z = DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(stickerSetCovered.set.f466id);
            this.isInstalled = z;
            if (z) {
                this.addButton.setBackgroundDrawable(this.delDrawable);
                this.addButton.setText(LocaleController.getString("StickersRemove", C1067R.string.StickersRemove).toUpperCase());
            } else {
                this.addButton.setBackgroundDrawable(this.addDrawable);
                this.addButton.setText(LocaleController.getString("Add", C1067R.string.Add).toUpperCase());
            }
            this.addButton.setPadding(AndroidUtilities.m26dp(17.0f), 0, AndroidUtilities.m26dp(17.0f), 0);
        } else {
            this.addButton.setVisibility(8);
        }
        this.set = stickerSetCovered;
    }

    public void setUrl(CharSequence charSequence, int i) {
        if (charSequence != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            try {
                spannableStringBuilder.setSpan(new ColorSpanUnderline(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4)), 0, i, 33);
                spannableStringBuilder.setSpan(new ColorSpanUnderline(Theme.getColor(Theme.key_chat_emojiPanelTrendingDescription)), i, charSequence.length(), 33);
            } catch (Exception unused) {
            }
            this.infoTextView.setText(spannableStringBuilder);
        }
    }

    public boolean isInstalled() {
        return this.isInstalled;
    }

    public void setDrawProgress(boolean z) {
        this.drawProgress = z;
        this.lastUpdateTime = System.currentTimeMillis();
        this.addButton.invalidate();
    }

    public StickerSetCovered getStickerSet() {
        return this.set;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.isUnread) {
            this.paint.setColor(Theme.getColor(Theme.key_featuredStickers_unread));
            canvas.drawCircle((float) (this.nameTextView.getRight() + AndroidUtilities.m26dp(12.0f)), (float) AndroidUtilities.m26dp(20.0f), (float) AndroidUtilities.m26dp(4.0f), this.paint);
        }
    }
}
