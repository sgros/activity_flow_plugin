// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import android.view.View$OnClickListener;
import android.view.MotionEvent;
import org.telegram.messenger.AndroidUtilities;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
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
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Switch;
import android.widget.FrameLayout;

public class ArchivedStickerSetCell extends FrameLayout
{
    private Switch checkBox;
    private BackupImageView imageView;
    private boolean needDivider;
    private Switch.OnCheckedChangeListener onCheckedChangeListener;
    private Rect rect;
    private TLRPC.StickerSetCovered stickersSet;
    private TextView textView;
    private TextView valueTextView;
    
    public ArchivedStickerSetCell(final Context context, final boolean b) {
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
        final int n = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        final TextView textView2 = this.textView;
        int n2;
        if (LocaleController.isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        float n3;
        if (b) {
            n3 = 71.0f;
        }
        else {
            n3 = 21.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n2, 71.0f, 10.0f, n3, 0.0f));
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
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        float n5;
        if (b) {
            n5 = 71.0f;
        }
        else {
            n5 = 21.0f;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n4, 71.0f, 35.0f, n5, 0.0f));
        (this.imageView = new BackupImageView(context)).setAspectFit(true);
        final BackupImageView imageView = this.imageView;
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 0.0f;
        }
        else {
            n7 = 12.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 12.0f;
        }
        else {
            n8 = 0.0f;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n6 | 0x30, n7, 8.0f, n8, 0.0f));
        if (b) {
            (this.checkBox = new Switch(context)).setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
            final Switch checkBox = this.checkBox;
            int n9 = n;
            if (LocaleController.isRTL) {
                n9 = 3;
            }
            this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(37, 40.0f, n9 | 0x10, 16.0f, 0.0f, 16.0f, 0.0f));
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
        final Switch checkBox = this.checkBox;
        return checkBox != null && checkBox.isChecked();
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
        final Switch checkBox = this.checkBox;
        if (checkBox != null) {
            checkBox.getHitRect(this.rect);
            if (this.rect.contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                motionEvent.offsetLocation(-this.checkBox.getX(), -this.checkBox.getY());
                return this.checkBox.onTouchEvent(motionEvent);
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void setChecked(final boolean b) {
        this.checkBox.setOnCheckedChangeListener(null);
        this.checkBox.setChecked(b, true);
        this.checkBox.setOnCheckedChangeListener(this.onCheckedChangeListener);
    }
    
    public void setOnCheckClick(final Switch.OnCheckedChangeListener onCheckedChangeListener) {
        this.checkBox.setOnCheckedChangeListener(this.onCheckedChangeListener = onCheckedChangeListener);
        this.checkBox.setOnClickListener((View$OnClickListener)new _$$Lambda$ArchivedStickerSetCell$9Rmaru_mwDl6BO3ARD8hu93oKu8(this));
    }
    
    public void setStickersSet(final TLRPC.StickerSetCovered stickersSet, final boolean needDivider) {
        this.needDivider = needDivider;
        this.stickersSet = stickersSet;
        this.setWillNotDraw(this.needDivider ^ true);
        this.textView.setText((CharSequence)this.stickersSet.set.title);
        this.valueTextView.setText((CharSequence)LocaleController.formatPluralString("Stickers", stickersSet.set.count));
        final TLRPC.Document cover = stickersSet.cover;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        if (cover != null) {
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(cover.thumbs, 90);
        }
        else {
            closestPhotoSizeWithSize = null;
        }
        if (closestPhotoSizeWithSize != null && closestPhotoSizeWithSize.location != null) {
            this.imageView.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize, stickersSet.cover), null, "webp", null, stickersSet);
        }
        else if (!stickersSet.covers.isEmpty()) {
            final TLRPC.Document document = stickersSet.covers.get(0);
            this.imageView.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document), null, "webp", null, stickersSet);
        }
        else {
            this.imageView.setImage(null, null, "webp", null, stickersSet);
        }
    }
}
