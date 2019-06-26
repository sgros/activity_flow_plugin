// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.DataQuery;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackupImageView;
import android.widget.TextView;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

public class StickerEmojiCell extends FrameLayout
{
    private static AccelerateInterpolator interpolator;
    private float alpha;
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
    
    static {
        StickerEmojiCell.interpolator = new AccelerateInterpolator(0.5f);
    }
    
    public StickerEmojiCell(final Context context) {
        super(context);
        this.alpha = 1.0f;
        this.currentAccount = UserConfig.selectedAccount;
        (this.imageView = new BackupImageView(context)).setAspectFit(true);
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(66, 66, 17));
        (this.emojiTextView = new TextView(context)).setTextSize(1, 16.0f);
        this.addView((View)this.emojiTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(28, 28, 85));
        this.setFocusable(true);
    }
    
    public void disable() {
        this.changingAlpha = true;
        this.alpha = 0.5f;
        this.time = 0L;
        this.imageView.getImageReceiver().setAlpha(this.alpha);
        this.imageView.invalidate();
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, long n) {
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (view == this.imageView && (this.changingAlpha || (this.scaled && this.scale != 0.8f) || (!this.scaled && this.scale != 1.0f))) {
            final long currentTimeMillis = System.currentTimeMillis();
            n = currentTimeMillis - this.lastUpdateTime;
            this.lastUpdateTime = currentTimeMillis;
            Label_0251: {
                if (this.changingAlpha) {
                    this.time += n;
                    if (this.time > 1050L) {
                        this.time = 1050L;
                    }
                    this.alpha = StickerEmojiCell.interpolator.getInterpolation(this.time / 1050.0f) * 0.5f + 0.5f;
                    if (this.alpha >= 1.0f) {
                        this.changingAlpha = false;
                        this.alpha = 1.0f;
                    }
                    this.imageView.getImageReceiver().setAlpha(this.alpha);
                }
                else {
                    if (this.scaled) {
                        final float scale = this.scale;
                        if (scale != 0.8f) {
                            this.scale = scale - n / 400.0f;
                            if (this.scale < 0.8f) {
                                this.scale = 0.8f;
                            }
                            break Label_0251;
                        }
                    }
                    this.scale += n / 400.0f;
                    if (this.scale > 1.0f) {
                        this.scale = 1.0f;
                    }
                }
            }
            this.imageView.setScaleX(this.scale);
            this.imageView.setScaleY(this.scale);
            this.imageView.invalidate();
            this.invalidate();
        }
        return drawChild;
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
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        final String string = LocaleController.getString("AttachSticker", 2131558730);
        int index = 0;
        String string2;
        while (true) {
            string2 = string;
            if (index >= this.sticker.attributes.size()) {
                break;
            }
            final TLRPC.DocumentAttribute documentAttribute = this.sticker.attributes.get(index);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final String alt = documentAttribute.alt;
                string2 = string;
                if (alt == null) {
                    break;
                }
                string2 = string;
                if (alt.length() > 0) {
                    final TextView emojiTextView = this.emojiTextView;
                    emojiTextView.setText(Emoji.replaceEmoji(documentAttribute.alt, emojiTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false));
                    final StringBuilder sb = new StringBuilder();
                    sb.append(documentAttribute.alt);
                    sb.append(" ");
                    sb.append(string);
                    string2 = sb.toString();
                    break;
                }
                break;
            }
            else {
                ++index;
            }
        }
        accessibilityNodeInfo.setContentDescription((CharSequence)string2);
        accessibilityNodeInfo.setEnabled(true);
    }
    
    public void setRecent(final boolean recent) {
        this.recent = recent;
    }
    
    public void setScaled(final boolean scaled) {
        this.scaled = scaled;
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public void setSticker(final TLRPC.Document sticker, final Object parentObject, final String s, final boolean b) {
        if (sticker != null) {
            this.sticker = sticker;
            this.parentObject = parentObject;
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(sticker.thumbs, 90);
            if (closestPhotoSizeWithSize != null) {
                this.imageView.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize, sticker), null, "webp", null, this.parentObject);
            }
            else {
                this.imageView.setImage(ImageLocation.getForDocument(sticker), null, "webp", null, this.parentObject);
            }
            if (s != null) {
                final TextView emojiTextView = this.emojiTextView;
                emojiTextView.setText(Emoji.replaceEmoji(s, emojiTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false));
                this.emojiTextView.setVisibility(0);
            }
            else {
                if (b) {
                    int i = 0;
                    while (true) {
                        while (i < sticker.attributes.size()) {
                            final TLRPC.DocumentAttribute documentAttribute = sticker.attributes.get(i);
                            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                                final String alt = documentAttribute.alt;
                                if (alt != null && alt.length() > 0) {
                                    final TextView emojiTextView2 = this.emojiTextView;
                                    emojiTextView2.setText(Emoji.replaceEmoji(documentAttribute.alt, emojiTextView2.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false));
                                    final boolean b2 = true;
                                    if (!b2) {
                                        this.emojiTextView.setText(Emoji.replaceEmoji(DataQuery.getInstance(this.currentAccount).getEmojiForSticker(this.sticker.id), this.emojiTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false));
                                    }
                                    this.emojiTextView.setVisibility(0);
                                    return;
                                }
                                break;
                            }
                            else {
                                ++i;
                            }
                        }
                        final boolean b2 = false;
                        continue;
                    }
                }
                this.emojiTextView.setVisibility(4);
            }
        }
    }
    
    public void setSticker(final TLRPC.Document document, final Object o, final boolean b) {
        this.setSticker(document, o, null, b);
    }
    
    public boolean showingBitmap() {
        return this.imageView.getImageReceiver().getBitmap() != null;
    }
}
