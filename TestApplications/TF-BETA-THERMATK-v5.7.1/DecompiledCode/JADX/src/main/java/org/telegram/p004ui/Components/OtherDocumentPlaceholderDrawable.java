package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.FileDownloadProgressListener;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.Message;

/* renamed from: org.telegram.ui.Components.OtherDocumentPlaceholderDrawable */
public class OtherDocumentPlaceholderDrawable extends RecyclableDrawable implements FileDownloadProgressListener {
    private static TextPaint buttonPaint = new TextPaint(1);
    private static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private static TextPaint docPaint = new TextPaint(1);
    private static TextPaint namePaint = new TextPaint(1);
    private static TextPaint openPaint = new TextPaint(1);
    private static Paint paint = new Paint();
    private static TextPaint percentPaint = new TextPaint(1);
    private static Paint progressPaint = new Paint(1);
    private static TextPaint sizePaint = new TextPaint(1);
    private int TAG;
    private float animatedAlphaValue = 1.0f;
    private float animatedProgressValue = 0.0f;
    private float animationProgressStart = 0.0f;
    private float currentProgress = 0.0f;
    private long currentProgressTime = 0;
    private String ext;
    private String fileName;
    private String fileSize;
    private long lastUpdateTime = 0;
    private boolean loaded;
    private boolean loading;
    private MessageObject parentMessageObject;
    private View parentView;
    private String progress;
    private boolean progressVisible;
    private Drawable thumbDrawable;

    public int getOpacity() {
        return -1;
    }

    public void onProgressUpload(String str, float f, boolean z) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    static {
        progressPaint.setStrokeCap(Cap.ROUND);
        paint.setColor(-14209998);
        docPaint.setColor(-1);
        namePaint.setColor(-1);
        sizePaint.setColor(-10327179);
        buttonPaint.setColor(-10327179);
        percentPaint.setColor(-1);
        openPaint.setColor(-1);
        String str = "fonts/rmedium.ttf";
        docPaint.setTypeface(AndroidUtilities.getTypeface(str));
        namePaint.setTypeface(AndroidUtilities.getTypeface(str));
        buttonPaint.setTypeface(AndroidUtilities.getTypeface(str));
        percentPaint.setTypeface(AndroidUtilities.getTypeface(str));
        openPaint.setTypeface(AndroidUtilities.getTypeface(str));
    }

    public OtherDocumentPlaceholderDrawable(Context context, View view, MessageObject messageObject) {
        docPaint.setTextSize((float) AndroidUtilities.m26dp(14.0f));
        namePaint.setTextSize((float) AndroidUtilities.m26dp(19.0f));
        sizePaint.setTextSize((float) AndroidUtilities.m26dp(15.0f));
        buttonPaint.setTextSize((float) AndroidUtilities.m26dp(15.0f));
        percentPaint.setTextSize((float) AndroidUtilities.m26dp(15.0f));
        openPaint.setTextSize((float) AndroidUtilities.m26dp(15.0f));
        progressPaint.setStrokeWidth((float) AndroidUtilities.m26dp(2.0f));
        this.parentView = view;
        this.parentMessageObject = messageObject;
        this.TAG = DownloadController.getInstance(messageObject.currentAccount).generateObserverTag();
        Document document = messageObject.getDocument();
        if (document != null) {
            this.fileName = FileLoader.getDocumentFileName(messageObject.getDocument());
            if (TextUtils.isEmpty(this.fileName)) {
                this.fileName = "name";
            }
            int lastIndexOf = this.fileName.lastIndexOf(46);
            this.ext = lastIndexOf == -1 ? "" : this.fileName.substring(lastIndexOf + 1).toUpperCase();
            if (((int) Math.ceil((double) docPaint.measureText(this.ext))) > AndroidUtilities.m26dp(40.0f)) {
                this.ext = TextUtils.ellipsize(this.ext, docPaint, (float) AndroidUtilities.m26dp(40.0f), TruncateAt.END).toString();
            }
            this.thumbDrawable = context.getResources().getDrawable(AndroidUtilities.getThumbForNameOrMime(this.fileName, messageObject.getDocument().mime_type, true)).mutate();
            this.fileSize = AndroidUtilities.formatFileSize((long) document.size);
            if (((int) Math.ceil((double) namePaint.measureText(this.fileName))) > AndroidUtilities.m26dp(320.0f)) {
                this.fileName = TextUtils.ellipsize(this.fileName, namePaint, (float) AndroidUtilities.m26dp(320.0f), TruncateAt.END).toString();
            }
        }
        checkFileExist();
    }

    public void setAlpha(int i) {
        Drawable drawable = this.thumbDrawable;
        if (drawable != null) {
            drawable.setAlpha(i);
        }
        paint.setAlpha(i);
        docPaint.setAlpha(i);
        namePaint.setAlpha(i);
        sizePaint.setAlpha(i);
        buttonPaint.setAlpha(i);
        percentPaint.setAlpha(i);
        openPaint.setAlpha(i);
    }

    public void draw(Canvas canvas) {
        String string;
        TextPaint textPaint;
        int i;
        Rect bounds = getBounds();
        int width = bounds.width();
        int height = bounds.height();
        canvas.save();
        canvas.translate((float) bounds.left, (float) bounds.top);
        canvas.drawRect(0.0f, 0.0f, (float) width, (float) height, paint);
        height = (height - AndroidUtilities.m26dp(240.0f)) / 2;
        int dp = (width - AndroidUtilities.m26dp(48.0f)) / 2;
        this.thumbDrawable.setBounds(dp, height, AndroidUtilities.m26dp(48.0f) + dp, AndroidUtilities.m26dp(48.0f) + height);
        this.thumbDrawable.draw(canvas);
        canvas.drawText(this.ext, (float) ((width - ((int) Math.ceil((double) docPaint.measureText(this.ext)))) / 2), (float) (AndroidUtilities.m26dp(31.0f) + height), docPaint);
        canvas.drawText(this.fileName, (float) ((width - ((int) Math.ceil((double) namePaint.measureText(this.fileName)))) / 2), (float) (AndroidUtilities.m26dp(96.0f) + height), namePaint);
        canvas.drawText(this.fileSize, (float) ((width - ((int) Math.ceil((double) sizePaint.measureText(this.fileSize)))) / 2), (float) (AndroidUtilities.m26dp(125.0f) + height), sizePaint);
        if (this.loaded) {
            string = LocaleController.getString("OpenFile", C1067R.string.OpenFile);
            textPaint = openPaint;
            i = 0;
        } else {
            if (this.loading) {
                string = LocaleController.getString("Cancel", C1067R.string.Cancel).toUpperCase();
            } else {
                string = LocaleController.getString("TapToDownload", C1067R.string.TapToDownload);
            }
            i = AndroidUtilities.m26dp(28.0f);
            textPaint = buttonPaint;
        }
        canvas.drawText(string, (float) ((width - ((int) Math.ceil((double) textPaint.measureText(string)))) / 2), (float) ((AndroidUtilities.m26dp(235.0f) + height) + i), textPaint);
        if (this.progressVisible) {
            string = this.progress;
            if (string != null) {
                canvas.drawText(this.progress, (float) ((width - ((int) Math.ceil((double) percentPaint.measureText(string)))) / 2), (float) (AndroidUtilities.m26dp(210.0f) + height), percentPaint);
            }
            width = (width - AndroidUtilities.m26dp(240.0f)) / 2;
            height += AndroidUtilities.m26dp(232.0f);
            progressPaint.setColor(-10327179);
            progressPaint.setAlpha((int) (this.animatedAlphaValue * 255.0f));
            float f = (float) height;
            Canvas canvas2 = canvas;
            canvas2.drawRect((float) (((int) (((float) AndroidUtilities.m26dp(240.0f)) * this.animatedProgressValue)) + width), f, (float) (AndroidUtilities.m26dp(240.0f) + width), (float) (AndroidUtilities.m26dp(2.0f) + height), progressPaint);
            progressPaint.setColor(-1);
            progressPaint.setAlpha((int) (this.animatedAlphaValue * 255.0f));
            float f2 = (float) width;
            canvas.drawRect(f2, f, f2 + (((float) AndroidUtilities.m26dp(240.0f)) * this.animatedProgressValue), (float) (height + AndroidUtilities.m26dp(2.0f)), progressPaint);
            updateAnimation();
        }
        canvas.restore();
    }

    public int getIntrinsicWidth() {
        return this.parentView.getMeasuredWidth();
    }

    public int getIntrinsicHeight() {
        return this.parentView.getMeasuredHeight();
    }

    public int getMinimumWidth() {
        return this.parentView.getMeasuredWidth();
    }

    public int getMinimumHeight() {
        return this.parentView.getMeasuredHeight();
    }

    public void onFailedDownload(String str, boolean z) {
        checkFileExist();
    }

    public void onSuccessDownload(String str) {
        setProgress(1.0f, true);
        checkFileExist();
    }

    public void onProgressDownload(String str, float f) {
        if (!this.progressVisible) {
            checkFileExist();
        }
        setProgress(f, true);
    }

    public int getObserverTag() {
        return this.TAG;
    }

    public void recycle() {
        DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver(this);
        this.parentView = null;
        this.parentMessageObject = null;
    }

    public void checkFileExist() {
        MessageObject messageObject = this.parentMessageObject;
        if (messageObject != null) {
            Message message = messageObject.messageOwner;
            if (message.media != null) {
                String str = null;
                if ((TextUtils.isEmpty(message.attachPath) || !new File(this.parentMessageObject.messageOwner.attachPath).exists()) && !FileLoader.getPathToMessage(this.parentMessageObject.messageOwner).exists()) {
                    str = FileLoader.getAttachFileName(this.parentMessageObject.getDocument());
                }
                this.loaded = false;
                if (str == null) {
                    this.progressVisible = false;
                    this.loading = false;
                    this.loaded = true;
                    DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver(this);
                } else {
                    DownloadController.getInstance(this.parentMessageObject.currentAccount).addLoadingFileObserver(str, this);
                    this.loading = FileLoader.getInstance(this.parentMessageObject.currentAccount).isLoadingFile(str);
                    if (this.loading) {
                        this.progressVisible = true;
                        Float fileProgress = ImageLoader.getInstance().getFileProgress(str);
                        if (fileProgress == null) {
                            fileProgress = Float.valueOf(0.0f);
                        }
                        setProgress(fileProgress.floatValue(), false);
                    } else {
                        this.progressVisible = false;
                    }
                }
                this.parentView.invalidate();
            }
        }
        this.loading = false;
        this.loaded = true;
        this.progressVisible = false;
        setProgress(0.0f, false);
        DownloadController.getInstance(this.parentMessageObject.currentAccount).removeLoadingFileObserver(this);
        this.parentView.invalidate();
    }

    private void updateAnimation() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        float f = this.animatedProgressValue;
        if (f != 1.0f) {
            float f2 = this.currentProgress;
            if (f != f2) {
                f = this.animationProgressStart;
                float f3 = f2 - f;
                if (f3 > 0.0f) {
                    this.currentProgressTime += j;
                    long j2 = this.currentProgressTime;
                    if (j2 >= 300) {
                        this.animatedProgressValue = f2;
                        this.animationProgressStart = f2;
                        this.currentProgressTime = 0;
                    } else {
                        this.animatedProgressValue = f + (f3 * decelerateInterpolator.getInterpolation(((float) j2) / 300.0f));
                    }
                }
                this.parentView.invalidate();
            }
        }
        f = this.animatedProgressValue;
        if (f >= 1.0f && f == 1.0f) {
            f = this.animatedAlphaValue;
            if (f != 0.0f) {
                this.animatedAlphaValue = f - (((float) j) / 200.0f);
                if (this.animatedAlphaValue <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                }
                this.parentView.invalidate();
            }
        }
    }

    public void setProgress(float f, boolean z) {
        if (z) {
            this.animationProgressStart = this.animatedProgressValue;
        } else {
            this.animatedProgressValue = f;
            this.animationProgressStart = f;
        }
        this.progress = String.format("%d%%", new Object[]{Integer.valueOf((int) (100.0f * f))});
        if (f != 1.0f) {
            this.animatedAlphaValue = 1.0f;
        }
        this.currentProgress = f;
        this.currentProgressTime = 0;
        this.lastUpdateTime = System.currentTimeMillis();
        this.parentView.invalidate();
    }

    public float getCurrentProgress() {
        return this.currentProgress;
    }
}