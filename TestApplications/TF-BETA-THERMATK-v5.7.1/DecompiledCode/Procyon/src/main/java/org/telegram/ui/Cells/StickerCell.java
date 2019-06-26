// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackupImageView;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

public class StickerCell extends FrameLayout
{
    private static AccelerateInterpolator interpolator;
    private boolean clearsInputField;
    private BackupImageView imageView;
    private long lastUpdateTime;
    private float scale;
    private boolean scaled;
    private TLRPC.Document sticker;
    private long time;
    
    static {
        StickerCell.interpolator = new AccelerateInterpolator(0.5f);
    }
    
    public StickerCell(final Context context) {
        super(context);
        this.time = 0L;
        (this.imageView = new BackupImageView(context)).setAspectFit(true);
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(66, 66.0f, 1, 0.0f, 5.0f, 0.0f, 0.0f));
        this.setFocusable(true);
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, long n) {
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (view == this.imageView && ((this.scaled && this.scale != 0.8f) || (!this.scaled && this.scale != 1.0f))) {
            final long currentTimeMillis = System.currentTimeMillis();
            n = currentTimeMillis - this.lastUpdateTime;
            this.lastUpdateTime = currentTimeMillis;
            Label_0149: {
                if (this.scaled) {
                    final float scale = this.scale;
                    if (scale != 0.8f) {
                        this.scale = scale - n / 400.0f;
                        if (this.scale < 0.8f) {
                            this.scale = 0.8f;
                        }
                        break Label_0149;
                    }
                }
                this.scale += n / 400.0f;
                if (this.scale > 1.0f) {
                    this.scale = 1.0f;
                }
            }
            this.imageView.setScaleX(this.scale);
            this.imageView.setScaleY(this.scale);
            this.imageView.invalidate();
            this.invalidate();
        }
        return drawChild;
    }
    
    public TLRPC.Document getSticker() {
        return this.sticker;
    }
    
    public boolean isClearsInputField() {
        return this.clearsInputField;
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.sticker == null) {
            return;
        }
        int i = 0;
        String alt = null;
        while (i < this.sticker.attributes.size()) {
            final TLRPC.DocumentAttribute documentAttribute = this.sticker.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final String alt2 = documentAttribute.alt;
                if (alt2 != null && alt2.length() > 0) {
                    alt = documentAttribute.alt;
                }
                else {
                    alt = null;
                }
            }
            ++i;
        }
        if (alt != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(alt);
            sb.append(" ");
            sb.append(LocaleController.getString("AttachSticker", 2131558730));
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
        else {
            accessibilityNodeInfo.setText((CharSequence)LocaleController.getString("AttachSticker", 2131558730));
        }
        accessibilityNodeInfo.setEnabled(true);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(76.0f) + this.getPaddingLeft() + this.getPaddingRight(), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(78.0f), 1073741824));
    }
    
    public void setClearsInputField(final boolean clearsInputField) {
        this.clearsInputField = clearsInputField;
    }
    
    public void setPressed(final boolean b) {
        if (this.imageView.getImageReceiver().getPressed() != b) {
            this.imageView.getImageReceiver().setPressed(b ? 1 : 0);
            this.imageView.invalidate();
        }
        super.setPressed(b);
    }
    
    public void setScaled(final boolean scaled) {
        this.scaled = scaled;
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public void setSticker(final TLRPC.Document sticker, final Object o, final int n) {
        if (sticker != null) {
            this.imageView.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(sticker.thumbs, 90), sticker), null, "webp", null, o);
        }
        this.sticker = sticker;
        if (n == -1) {
            this.setBackgroundResource(2131165862);
            this.setPadding(AndroidUtilities.dp(7.0f), 0, 0, 0);
        }
        else if (n == 0) {
            this.setBackgroundResource(2131165861);
            this.setPadding(0, 0, 0, 0);
        }
        else if (n == 1) {
            this.setBackgroundResource(2131165863);
            this.setPadding(0, 0, AndroidUtilities.dp(7.0f), 0);
        }
        else if (n == 2) {
            this.setBackgroundResource(2131165859);
            this.setPadding(AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f), 0);
        }
        final Drawable background = this.getBackground();
        if (background != null) {
            background.setAlpha(230);
            background.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel"), PorterDuff$Mode.MULTIPLY));
        }
    }
    
    public boolean showingBitmap() {
        return this.imageView.getImageReceiver().getBitmap() != null;
    }
}
