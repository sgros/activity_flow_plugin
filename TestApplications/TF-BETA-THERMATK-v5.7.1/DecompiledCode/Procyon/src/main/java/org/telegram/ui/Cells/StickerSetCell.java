// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.text.TextUtils;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import android.view.View$OnClickListener;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.tgnet.TLRPC;
import android.graphics.Rect;
import org.telegram.ui.Components.RadialProgressView;
import android.widget.ImageView;
import org.telegram.ui.Components.BackupImageView;
import android.widget.FrameLayout;

public class StickerSetCell extends FrameLayout
{
    private BackupImageView imageView;
    private boolean needDivider;
    private ImageView optionsButton;
    private RadialProgressView progressView;
    private Rect rect;
    private TLRPC.TL_messages_stickerSet stickersSet;
    private TextView textView;
    private TextView valueTextView;
    
    public StickerSetCell(final Context context, int n) {
        super(context);
        this.rect = new Rect();
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        int n2 = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 40.0f;
        }
        else {
            n4 = 71.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 71.0f;
        }
        else {
            n5 = 40.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n3, n4, 10.0f, n5, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        final TextView valueTextView = this.valueTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        valueTextView.setGravity(gravity2);
        final TextView valueTextView2 = this.valueTextView;
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 40.0f;
        }
        else {
            n7 = 71.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 71.0f;
        }
        else {
            n8 = 40.0f;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n6, n7, 35.0f, n8, 0.0f));
        (this.imageView = new BackupImageView(context)).setAspectFit(true);
        final BackupImageView imageView = this.imageView;
        int n9;
        if (LocaleController.isRTL) {
            n9 = 5;
        }
        else {
            n9 = 3;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 0.0f;
        }
        else {
            n10 = 12.0f;
        }
        float n11;
        if (LocaleController.isRTL) {
            n11 = 12.0f;
        }
        else {
            n11 = 0.0f;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n9 | 0x30, n10, 8.0f, n11, 0.0f));
        if (n == 2) {
            (this.progressView = new RadialProgressView(this.getContext())).setProgressColor(Theme.getColor("dialogProgressCircle"));
            this.progressView.setSize(AndroidUtilities.dp(30.0f));
            final RadialProgressView progressView = this.progressView;
            if (!LocaleController.isRTL) {
                n2 = 3;
            }
            float n12;
            if (LocaleController.isRTL) {
                n12 = 0.0f;
            }
            else {
                n12 = 12.0f;
            }
            float n13;
            if (LocaleController.isRTL) {
                n13 = 12.0f;
            }
            else {
                n13 = 0.0f;
            }
            this.addView((View)progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n2 | 0x30, n12, 8.0f, n13, 0.0f));
        }
        else if (n != 0) {
            this.optionsButton = new ImageView(context);
            final ImageView optionsButton = this.optionsButton;
            final int n14 = 0;
            optionsButton.setFocusable(false);
            this.optionsButton.setScaleType(ImageView$ScaleType.CENTER);
            this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
            if (n == 1) {
                this.optionsButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("stickers_menu"), PorterDuff$Mode.MULTIPLY));
                this.optionsButton.setImageResource(2131165610);
                final ImageView optionsButton2 = this.optionsButton;
                if (LocaleController.isRTL) {
                    n2 = 3;
                }
                this.addView((View)optionsButton2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40, n2 | 0x30));
            }
            else if (n == 3) {
                this.optionsButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
                this.optionsButton.setImageResource(2131165858);
                final ImageView optionsButton3 = this.optionsButton;
                if (LocaleController.isRTL) {
                    n2 = 3;
                }
                if (LocaleController.isRTL) {
                    n = 10;
                }
                else {
                    n = 0;
                }
                final float n15 = (float)n;
                if (LocaleController.isRTL) {
                    n = n14;
                }
                else {
                    n = 10;
                }
                this.addView((View)optionsButton3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n2 | 0x30, n15, 12.0f, (float)n, 0.0f));
            }
        }
    }
    
    public TLRPC.TL_messages_stickerSet getStickersSet() {
        return this.stickersSet;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(0.0f, (float)(this.getHeight() - 1), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (Build$VERSION.SDK_INT >= 21 && this.getBackground() != null) {
            final ImageView optionsButton = this.optionsButton;
            if (optionsButton != null) {
                optionsButton.getHitRect(this.rect);
                if (this.rect.contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                    return true;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void setChecked(final boolean b) {
        final ImageView optionsButton = this.optionsButton;
        if (optionsButton == null) {
            return;
        }
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        optionsButton.setVisibility(visibility);
    }
    
    public void setOnOptionsClick(final View$OnClickListener onClickListener) {
        final ImageView optionsButton = this.optionsButton;
        if (optionsButton == null) {
            return;
        }
        optionsButton.setOnClickListener(onClickListener);
    }
    
    public void setStickersSet(final TLRPC.TL_messages_stickerSet stickersSet, final boolean needDivider) {
        this.needDivider = needDivider;
        this.stickersSet = stickersSet;
        this.imageView.setVisibility(0);
        final RadialProgressView progressView = this.progressView;
        if (progressView != null) {
            progressView.setVisibility(4);
        }
        this.textView.setTranslationY(0.0f);
        this.textView.setText((CharSequence)this.stickersSet.set.title);
        if (this.stickersSet.set.archived) {
            this.textView.setAlpha(0.5f);
            this.valueTextView.setAlpha(0.5f);
            this.imageView.setAlpha(0.5f);
        }
        else {
            this.textView.setAlpha(1.0f);
            this.valueTextView.setAlpha(1.0f);
            this.imageView.setAlpha(1.0f);
        }
        final ArrayList<TLRPC.Document> documents = stickersSet.documents;
        if (documents != null && !documents.isEmpty()) {
            this.valueTextView.setText((CharSequence)LocaleController.formatPluralString("Stickers", documents.size()));
            final TLRPC.Document document = documents.get(0);
            this.imageView.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document), null, "webp", null, stickersSet);
        }
        else {
            this.valueTextView.setText((CharSequence)LocaleController.formatPluralString("Stickers", 0));
        }
    }
    
    public void setText(final String text, final String text2, final int n, final boolean needDivider) {
        this.needDivider = needDivider;
        this.stickersSet = null;
        this.textView.setText((CharSequence)text);
        this.valueTextView.setText((CharSequence)text2);
        if (TextUtils.isEmpty((CharSequence)text2)) {
            this.textView.setTranslationY((float)AndroidUtilities.dp(10.0f));
        }
        else {
            this.textView.setTranslationY(0.0f);
        }
        if (n != 0) {
            this.imageView.setImageResource(n, Theme.getColor("windowBackgroundWhiteGrayIcon"));
            this.imageView.setVisibility(0);
            final RadialProgressView progressView = this.progressView;
            if (progressView != null) {
                progressView.setVisibility(4);
            }
        }
        else {
            this.imageView.setVisibility(4);
            final RadialProgressView progressView2 = this.progressView;
            if (progressView2 != null) {
                progressView2.setVisibility(0);
            }
        }
    }
}
