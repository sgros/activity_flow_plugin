package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.TL_documentAttributeSticker;

/* renamed from: org.telegram.ui.Cells.StickerCell */
public class StickerCell extends FrameLayout {
    private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5f);
    private boolean clearsInputField;
    private BackupImageView imageView;
    private long lastUpdateTime;
    private float scale;
    private boolean scaled;
    private Document sticker;
    private long time = 0;

    public StickerCell(Context context) {
        super(context);
        this.imageView = new BackupImageView(context);
        this.imageView.setAspectFit(true);
        addView(this.imageView, LayoutHelper.createFrame(66, 66.0f, 1, 0.0f, 5.0f, 0.0f, 0.0f));
        setFocusable(true);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec((AndroidUtilities.m26dp(76.0f) + getPaddingLeft()) + getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(78.0f), 1073741824));
    }

    public void setPressed(boolean z) {
        if (this.imageView.getImageReceiver().getPressed() != z) {
            this.imageView.getImageReceiver().setPressed(z);
            this.imageView.invalidate();
        }
        super.setPressed(z);
    }

    public void setClearsInputField(boolean z) {
        this.clearsInputField = z;
    }

    public boolean isClearsInputField() {
        return this.clearsInputField;
    }

    public void setSticker(Document document, Object obj, int i) {
        if (document != null) {
            this.imageView.setImage(ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document), null, "webp", null, obj);
        }
        this.sticker = document;
        if (i == -1) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_left);
            setPadding(AndroidUtilities.m26dp(7.0f), 0, 0, 0);
        } else if (i == 0) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_center);
            setPadding(0, 0, 0, 0);
        } else if (i == 1) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_right);
            setPadding(0, 0, AndroidUtilities.m26dp(7.0f), 0);
        } else if (i == 2) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_all);
            setPadding(AndroidUtilities.m26dp(3.0f), 0, AndroidUtilities.m26dp(3.0f), 0);
        }
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(230);
            background.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_stickersHintPanel), Mode.MULTIPLY));
        }
    }

    public Document getSticker() {
        return this.sticker;
    }

    public void setScaled(boolean z) {
        this.scaled = z;
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public boolean showingBitmap() {
        return this.imageView.getImageReceiver().getBitmap() != null;
    }

    /* Access modifiers changed, original: protected */
    public boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild = super.drawChild(canvas, view, j);
        if (view == this.imageView && ((this.scaled && this.scale != 0.8f) || !(this.scaled || this.scale == 1.0f))) {
            long currentTimeMillis = System.currentTimeMillis();
            long j2 = currentTimeMillis - this.lastUpdateTime;
            this.lastUpdateTime = currentTimeMillis;
            if (this.scaled) {
                float f = this.scale;
                if (f != 0.8f) {
                    this.scale = f - (((float) j2) / 400.0f);
                    if (this.scale < 0.8f) {
                        this.scale = 0.8f;
                    }
                    this.imageView.setScaleX(this.scale);
                    this.imageView.setScaleY(this.scale);
                    this.imageView.invalidate();
                    invalidate();
                }
            }
            this.scale += ((float) j2) / 400.0f;
            if (this.scale > 1.0f) {
                this.scale = 1.0f;
            }
            this.imageView.setScaleX(this.scale);
            this.imageView.setScaleY(this.scale);
            this.imageView.invalidate();
            invalidate();
        }
        return drawChild;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.sticker != null) {
            String str = null;
            for (int i = 0; i < this.sticker.attributes.size(); i++) {
                DocumentAttribute documentAttribute = (DocumentAttribute) this.sticker.attributes.get(i);
                if (documentAttribute instanceof TL_documentAttributeSticker) {
                    str = documentAttribute.alt;
                    str = (str == null || str.length() <= 0) ? null : documentAttribute.alt;
                }
            }
            String str2 = "AttachSticker";
            if (str != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(" ");
                stringBuilder.append(LocaleController.getString(str2, C1067R.string.AttachSticker));
                accessibilityNodeInfo.setText(stringBuilder.toString());
            } else {
                accessibilityNodeInfo.setText(LocaleController.getString(str2, C1067R.string.AttachSticker));
            }
            accessibilityNodeInfo.setEnabled(true);
        }
    }
}