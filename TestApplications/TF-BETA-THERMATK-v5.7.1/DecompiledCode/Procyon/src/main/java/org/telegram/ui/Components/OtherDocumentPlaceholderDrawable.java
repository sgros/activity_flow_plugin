// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Rect;
import org.telegram.messenger.LocaleController;
import android.graphics.Canvas;
import org.telegram.messenger.ImageLoader;
import org.telegram.tgnet.TLObject;
import java.io.File;
import org.telegram.tgnet.TLRPC;
import android.text.TextUtils$TruncateAt;
import android.text.TextUtils;
import org.telegram.messenger.FileLoader;
import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Cap;
import android.graphics.drawable.Drawable;
import android.view.View;
import org.telegram.messenger.MessageObject;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.text.TextPaint;
import org.telegram.messenger.DownloadController;

public class OtherDocumentPlaceholderDrawable extends RecyclableDrawable implements FileDownloadProgressListener
{
    private static TextPaint buttonPaint;
    private static DecelerateInterpolator decelerateInterpolator;
    private static TextPaint docPaint;
    private static TextPaint namePaint;
    private static TextPaint openPaint;
    private static Paint paint;
    private static TextPaint percentPaint;
    private static Paint progressPaint;
    private static TextPaint sizePaint;
    private int TAG;
    private float animatedAlphaValue;
    private float animatedProgressValue;
    private float animationProgressStart;
    private float currentProgress;
    private long currentProgressTime;
    private String ext;
    private String fileName;
    private String fileSize;
    private long lastUpdateTime;
    private boolean loaded;
    private boolean loading;
    private MessageObject parentMessageObject;
    private View parentView;
    private String progress;
    private boolean progressVisible;
    private Drawable thumbDrawable;
    
    static {
        OtherDocumentPlaceholderDrawable.paint = new Paint();
        OtherDocumentPlaceholderDrawable.progressPaint = new Paint(1);
        OtherDocumentPlaceholderDrawable.docPaint = new TextPaint(1);
        OtherDocumentPlaceholderDrawable.namePaint = new TextPaint(1);
        OtherDocumentPlaceholderDrawable.sizePaint = new TextPaint(1);
        OtherDocumentPlaceholderDrawable.buttonPaint = new TextPaint(1);
        OtherDocumentPlaceholderDrawable.percentPaint = new TextPaint(1);
        OtherDocumentPlaceholderDrawable.openPaint = new TextPaint(1);
        OtherDocumentPlaceholderDrawable.decelerateInterpolator = new DecelerateInterpolator();
        OtherDocumentPlaceholderDrawable.progressPaint.setStrokeCap(Paint$Cap.ROUND);
        OtherDocumentPlaceholderDrawable.paint.setColor(-14209998);
        OtherDocumentPlaceholderDrawable.docPaint.setColor(-1);
        OtherDocumentPlaceholderDrawable.namePaint.setColor(-1);
        OtherDocumentPlaceholderDrawable.sizePaint.setColor(-10327179);
        OtherDocumentPlaceholderDrawable.buttonPaint.setColor(-10327179);
        OtherDocumentPlaceholderDrawable.percentPaint.setColor(-1);
        OtherDocumentPlaceholderDrawable.openPaint.setColor(-1);
        OtherDocumentPlaceholderDrawable.docPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        OtherDocumentPlaceholderDrawable.namePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        OtherDocumentPlaceholderDrawable.buttonPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        OtherDocumentPlaceholderDrawable.percentPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        OtherDocumentPlaceholderDrawable.openPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
    }
    
    public OtherDocumentPlaceholderDrawable(final Context context, final View parentView, final MessageObject parentMessageObject) {
        this.lastUpdateTime = 0L;
        this.currentProgress = 0.0f;
        this.animationProgressStart = 0.0f;
        this.currentProgressTime = 0L;
        this.animatedProgressValue = 0.0f;
        this.animatedAlphaValue = 1.0f;
        OtherDocumentPlaceholderDrawable.docPaint.setTextSize((float)AndroidUtilities.dp(14.0f));
        OtherDocumentPlaceholderDrawable.namePaint.setTextSize((float)AndroidUtilities.dp(19.0f));
        OtherDocumentPlaceholderDrawable.sizePaint.setTextSize((float)AndroidUtilities.dp(15.0f));
        OtherDocumentPlaceholderDrawable.buttonPaint.setTextSize((float)AndroidUtilities.dp(15.0f));
        OtherDocumentPlaceholderDrawable.percentPaint.setTextSize((float)AndroidUtilities.dp(15.0f));
        OtherDocumentPlaceholderDrawable.openPaint.setTextSize((float)AndroidUtilities.dp(15.0f));
        OtherDocumentPlaceholderDrawable.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.parentView = parentView;
        this.parentMessageObject = parentMessageObject;
        this.TAG = DownloadController.getInstance(parentMessageObject.currentAccount).generateObserverTag();
        final TLRPC.Document document = parentMessageObject.getDocument();
        if (document != null) {
            this.fileName = FileLoader.getDocumentFileName(parentMessageObject.getDocument());
            if (TextUtils.isEmpty((CharSequence)this.fileName)) {
                this.fileName = "name";
            }
            final int lastIndex = this.fileName.lastIndexOf(46);
            String upperCase;
            if (lastIndex == -1) {
                upperCase = "";
            }
            else {
                upperCase = this.fileName.substring(lastIndex + 1).toUpperCase();
            }
            this.ext = upperCase;
            if ((int)Math.ceil(OtherDocumentPlaceholderDrawable.docPaint.measureText(this.ext)) > AndroidUtilities.dp(40.0f)) {
                this.ext = TextUtils.ellipsize((CharSequence)this.ext, OtherDocumentPlaceholderDrawable.docPaint, (float)AndroidUtilities.dp(40.0f), TextUtils$TruncateAt.END).toString();
            }
            this.thumbDrawable = context.getResources().getDrawable(AndroidUtilities.getThumbForNameOrMime(this.fileName, parentMessageObject.getDocument().mime_type, true)).mutate();
            this.fileSize = AndroidUtilities.formatFileSize(document.size);
            if ((int)Math.ceil(OtherDocumentPlaceholderDrawable.namePaint.measureText(this.fileName)) > AndroidUtilities.dp(320.0f)) {
                this.fileName = TextUtils.ellipsize((CharSequence)this.fileName, OtherDocumentPlaceholderDrawable.namePaint, (float)AndroidUtilities.dp(320.0f), TextUtils$TruncateAt.END).toString();
            }
        }
        this.checkFileExist();
    }
    
    private void updateAnimation() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final float animatedProgressValue = this.animatedProgressValue;
        if (animatedProgressValue != 1.0f) {
            final float currentProgress = this.currentProgress;
            if (animatedProgressValue != currentProgress) {
                final float animationProgressStart = this.animationProgressStart;
                final float n2 = currentProgress - animationProgressStart;
                if (n2 > 0.0f) {
                    this.currentProgressTime += n;
                    final long currentProgressTime = this.currentProgressTime;
                    if (currentProgressTime >= 300L) {
                        this.animatedProgressValue = currentProgress;
                        this.animationProgressStart = currentProgress;
                        this.currentProgressTime = 0L;
                    }
                    else {
                        this.animatedProgressValue = animationProgressStart + n2 * OtherDocumentPlaceholderDrawable.decelerateInterpolator.getInterpolation(currentProgressTime / 300.0f);
                    }
                }
                this.parentView.invalidate();
            }
        }
        final float animatedProgressValue2 = this.animatedProgressValue;
        if (animatedProgressValue2 >= 1.0f && animatedProgressValue2 == 1.0f) {
            final float animatedAlphaValue = this.animatedAlphaValue;
            if (animatedAlphaValue != 0.0f) {
                this.animatedAlphaValue = animatedAlphaValue - n / 200.0f;
                if (this.animatedAlphaValue <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                }
                this.parentView.invalidate();
            }
        }
    }
    
    public void checkFileExist() {
        final MessageObject parentMessageObject = this.parentMessageObject;
        Label_0247: {
            if (parentMessageObject != null) {
                final TLRPC.Message messageOwner = parentMessageObject.messageOwner;
                if (messageOwner.media != null) {
                    final String s = null;
                    String attachFileName = null;
                    Label_0087: {
                        if (!TextUtils.isEmpty((CharSequence)messageOwner.attachPath)) {
                            attachFileName = s;
                            if (new File(this.parentMessageObject.messageOwner.attachPath).exists()) {
                                break Label_0087;
                            }
                        }
                        attachFileName = s;
                        if (!FileLoader.getPathToMessage(this.parentMessageObject.messageOwner).exists()) {
                            attachFileName = FileLoader.getAttachFileName(this.parentMessageObject.getDocument());
                        }
                    }
                    this.loaded = false;
                    if (attachFileName == null) {
                        this.progressVisible = false;
                        this.loading = false;
                        this.loaded = true;
                        DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
                        break Label_0247;
                    }
                    DownloadController.getInstance(this.parentMessageObject.currentAccount).addLoadingFileObserver(attachFileName, (DownloadController.FileDownloadProgressListener)this);
                    this.loading = FileLoader.getInstance(this.parentMessageObject.currentAccount).isLoadingFile(attachFileName);
                    if (this.loading) {
                        this.progressVisible = true;
                        Float n;
                        if ((n = ImageLoader.getInstance().getFileProgress(attachFileName)) == null) {
                            n = 0.0f;
                        }
                        this.setProgress(n, false);
                        break Label_0247;
                    }
                    this.progressVisible = false;
                    break Label_0247;
                }
            }
            this.loading = false;
            this.loaded = true;
            this.setProgress(0.0f, this.progressVisible = false);
            DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
        }
        this.parentView.invalidate();
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        final int width = bounds.width();
        final int height = bounds.height();
        canvas.save();
        canvas.translate((float)bounds.left, (float)bounds.top);
        canvas.drawRect(0.0f, 0.0f, (float)width, (float)height, OtherDocumentPlaceholderDrawable.paint);
        final int n = (height - AndroidUtilities.dp(240.0f)) / 2;
        final int n2 = (width - AndroidUtilities.dp(48.0f)) / 2;
        this.thumbDrawable.setBounds(n2, n, AndroidUtilities.dp(48.0f) + n2, AndroidUtilities.dp(48.0f) + n);
        this.thumbDrawable.draw(canvas);
        canvas.drawText(this.ext, (float)((width - (int)Math.ceil(OtherDocumentPlaceholderDrawable.docPaint.measureText(this.ext))) / 2), (float)(AndroidUtilities.dp(31.0f) + n), (Paint)OtherDocumentPlaceholderDrawable.docPaint);
        canvas.drawText(this.fileName, (float)((width - (int)Math.ceil(OtherDocumentPlaceholderDrawable.namePaint.measureText(this.fileName))) / 2), (float)(AndroidUtilities.dp(96.0f) + n), (Paint)OtherDocumentPlaceholderDrawable.namePaint);
        canvas.drawText(this.fileSize, (float)((width - (int)Math.ceil(OtherDocumentPlaceholderDrawable.sizePaint.measureText(this.fileSize))) / 2), (float)(AndroidUtilities.dp(125.0f) + n), (Paint)OtherDocumentPlaceholderDrawable.sizePaint);
        String s;
        TextPaint textPaint;
        int dp;
        if (this.loaded) {
            s = LocaleController.getString("OpenFile", 2131560113);
            textPaint = OtherDocumentPlaceholderDrawable.openPaint;
            dp = 0;
        }
        else {
            if (this.loading) {
                s = LocaleController.getString("Cancel", 2131558891).toUpperCase();
            }
            else {
                s = LocaleController.getString("TapToDownload", 2131560861);
            }
            dp = AndroidUtilities.dp(28.0f);
            textPaint = OtherDocumentPlaceholderDrawable.buttonPaint;
        }
        canvas.drawText(s, (float)((width - (int)Math.ceil(textPaint.measureText(s))) / 2), (float)(AndroidUtilities.dp(235.0f) + n + dp), (Paint)textPaint);
        if (this.progressVisible) {
            final String progress = this.progress;
            if (progress != null) {
                canvas.drawText(this.progress, (float)((width - (int)Math.ceil(OtherDocumentPlaceholderDrawable.percentPaint.measureText(progress))) / 2), (float)(AndroidUtilities.dp(210.0f) + n), (Paint)OtherDocumentPlaceholderDrawable.percentPaint);
            }
            final int n3 = (width - AndroidUtilities.dp(240.0f)) / 2;
            final int n4 = n + AndroidUtilities.dp(232.0f);
            OtherDocumentPlaceholderDrawable.progressPaint.setColor(-10327179);
            OtherDocumentPlaceholderDrawable.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f));
            final float n5 = (float)((int)(AndroidUtilities.dp(240.0f) * this.animatedProgressValue) + n3);
            final float n6 = (float)n4;
            canvas.drawRect(n5, n6, (float)(AndroidUtilities.dp(240.0f) + n3), (float)(AndroidUtilities.dp(2.0f) + n4), OtherDocumentPlaceholderDrawable.progressPaint);
            OtherDocumentPlaceholderDrawable.progressPaint.setColor(-1);
            OtherDocumentPlaceholderDrawable.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f));
            final float n7 = (float)n3;
            canvas.drawRect(n7, n6, n7 + AndroidUtilities.dp(240.0f) * this.animatedProgressValue, (float)(n4 + AndroidUtilities.dp(2.0f)), OtherDocumentPlaceholderDrawable.progressPaint);
            this.updateAnimation();
        }
        canvas.restore();
    }
    
    public float getCurrentProgress() {
        return this.currentProgress;
    }
    
    public int getIntrinsicHeight() {
        return this.parentView.getMeasuredHeight();
    }
    
    public int getIntrinsicWidth() {
        return this.parentView.getMeasuredWidth();
    }
    
    public int getMinimumHeight() {
        return this.parentView.getMeasuredHeight();
    }
    
    public int getMinimumWidth() {
        return this.parentView.getMeasuredWidth();
    }
    
    @Override
    public int getObserverTag() {
        return this.TAG;
    }
    
    public int getOpacity() {
        return -1;
    }
    
    @Override
    public void onFailedDownload(final String s, final boolean b) {
        this.checkFileExist();
    }
    
    @Override
    public void onProgressDownload(final String s, final float n) {
        if (!this.progressVisible) {
            this.checkFileExist();
        }
        this.setProgress(n, true);
    }
    
    @Override
    public void onProgressUpload(final String s, final float n, final boolean b) {
    }
    
    @Override
    public void onSuccessDownload(final String s) {
        this.setProgress(1.0f, true);
        this.checkFileExist();
    }
    
    @Override
    public void recycle() {
        DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
        this.parentView = null;
        this.parentMessageObject = null;
    }
    
    public void setAlpha(final int n) {
        final Drawable thumbDrawable = this.thumbDrawable;
        if (thumbDrawable != null) {
            thumbDrawable.setAlpha(n);
        }
        OtherDocumentPlaceholderDrawable.paint.setAlpha(n);
        OtherDocumentPlaceholderDrawable.docPaint.setAlpha(n);
        OtherDocumentPlaceholderDrawable.namePaint.setAlpha(n);
        OtherDocumentPlaceholderDrawable.sizePaint.setAlpha(n);
        OtherDocumentPlaceholderDrawable.buttonPaint.setAlpha(n);
        OtherDocumentPlaceholderDrawable.percentPaint.setAlpha(n);
        OtherDocumentPlaceholderDrawable.openPaint.setAlpha(n);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    public void setProgress(final float currentProgress, final boolean b) {
        if (!b) {
            this.animatedProgressValue = currentProgress;
            this.animationProgressStart = currentProgress;
        }
        else {
            this.animationProgressStart = this.animatedProgressValue;
        }
        this.progress = String.format("%d%%", (int)(100.0f * currentProgress));
        if (currentProgress != 1.0f) {
            this.animatedAlphaValue = 1.0f;
        }
        this.currentProgress = currentProgress;
        this.currentProgressTime = 0L;
        this.lastUpdateTime = System.currentTimeMillis();
        this.parentView.invalidate();
    }
}
