// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.Layout$Alignment;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ImageLoader;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.text.TextPaint;
import android.text.StaticLayout;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.DownloadController;
import org.telegram.ui.Cells.BaseCell;

public class PopupAudioView extends BaseCell implements SeekBarDelegate, FileDownloadProgressListener
{
    private int TAG;
    private int buttonPressed;
    private int buttonState;
    private int buttonX;
    private int buttonY;
    private int currentAccount;
    protected MessageObject currentMessageObject;
    private String lastTimeString;
    private ProgressView progressView;
    private SeekBar seekBar;
    private int seekBarX;
    private int seekBarY;
    private StaticLayout timeLayout;
    private TextPaint timePaint;
    int timeWidth;
    private int timeX;
    private boolean wasLayout;
    
    public PopupAudioView(final Context context) {
        super(context);
        this.wasLayout = false;
        this.buttonState = 0;
        this.buttonPressed = 0;
        this.timeWidth = 0;
        this.lastTimeString = null;
        (this.timePaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(16.0f));
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        (this.seekBar = new SeekBar(this.getContext())).setDelegate((SeekBar.SeekBarDelegate)this);
        this.progressView = new ProgressView();
    }
    
    private void didPressedButton() {
        final int buttonState = this.buttonState;
        if (buttonState == 0) {
            final boolean playMessage = MediaController.getInstance().playMessage(this.currentMessageObject);
            if (!this.currentMessageObject.isOut() && this.currentMessageObject.isContentUnread() && this.currentMessageObject.messageOwner.to_id.channel_id == 0) {
                MessagesController.getInstance(this.currentAccount).markMessageContentAsRead(this.currentMessageObject);
                this.currentMessageObject.setContentIsRead();
            }
            if (playMessage) {
                this.buttonState = 1;
                this.invalidate();
            }
        }
        else if (buttonState == 1) {
            if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
                this.buttonState = 0;
                this.invalidate();
            }
        }
        else if (buttonState == 2) {
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            this.buttonState = 4;
            this.invalidate();
        }
        else if (buttonState == 3) {
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
            this.buttonState = 2;
            this.invalidate();
        }
    }
    
    public void downloadAudioIfNeed() {
        if (this.buttonState == 2) {
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            this.buttonState = 3;
            this.invalidate();
        }
    }
    
    public final MessageObject getMessageObject() {
        return this.currentMessageObject;
    }
    
    @Override
    public int getObserverTag() {
        return this.TAG;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.currentMessageObject == null) {
            return;
        }
        if (!this.wasLayout) {
            this.requestLayout();
            return;
        }
        BaseCell.setDrawableBounds(Theme.chat_msgInMediaDrawable, 0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
        Theme.chat_msgInMediaDrawable.draw(canvas);
        if (this.currentMessageObject == null) {
            return;
        }
        canvas.save();
        final int buttonState = this.buttonState;
        if (buttonState != 0 && buttonState != 1) {
            canvas.translate((float)(this.seekBarX + AndroidUtilities.dp(12.0f)), (float)this.seekBarY);
            this.progressView.draw(canvas);
        }
        else {
            canvas.translate((float)this.seekBarX, (float)this.seekBarY);
            this.seekBar.draw(canvas);
        }
        canvas.restore();
        final int buttonState2 = this.buttonState;
        this.timePaint.setColor(-6182221);
        final Drawable drawable = Theme.chat_fileStatesDrawable[buttonState2 + 5][this.buttonPressed];
        final int dp = AndroidUtilities.dp(36.0f);
        BaseCell.setDrawableBounds(drawable, (dp - drawable.getIntrinsicWidth()) / 2 + this.buttonX, (dp - drawable.getIntrinsicHeight()) / 2 + this.buttonY);
        drawable.draw(canvas);
        canvas.save();
        canvas.translate((float)this.timeX, (float)AndroidUtilities.dp(18.0f));
        this.timeLayout.draw(canvas);
        canvas.restore();
    }
    
    @Override
    public void onFailedDownload(final String s, final boolean b) {
        this.updateButtonState();
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.currentMessageObject == null) {
            return;
        }
        this.seekBarX = AndroidUtilities.dp(54.0f);
        this.buttonX = AndroidUtilities.dp(10.0f);
        this.timeX = this.getMeasuredWidth() - this.timeWidth - AndroidUtilities.dp(16.0f);
        this.seekBar.setSize(this.getMeasuredWidth() - AndroidUtilities.dp(70.0f) - this.timeWidth, AndroidUtilities.dp(30.0f));
        this.progressView.width = this.getMeasuredWidth() - AndroidUtilities.dp(94.0f) - this.timeWidth;
        this.progressView.height = AndroidUtilities.dp(30.0f);
        this.seekBarY = AndroidUtilities.dp(13.0f);
        this.buttonY = AndroidUtilities.dp(10.0f);
        this.updateProgress();
        if (b || !this.wasLayout) {
            this.wasLayout = true;
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(56.0f));
    }
    
    @Override
    public void onProgressDownload(final String s, final float progress) {
        this.progressView.setProgress(progress);
        if (this.buttonState != 3) {
            this.updateButtonState();
        }
        this.invalidate();
    }
    
    @Override
    public void onProgressUpload(final String s, final float n, final boolean b) {
    }
    
    @Override
    public void onSeekBarDrag(final float audioProgress) {
        final MessageObject currentMessageObject = this.currentMessageObject;
        if (currentMessageObject == null) {
            return;
        }
        currentMessageObject.audioProgress = audioProgress;
        MediaController.getInstance().seekToProgress(this.currentMessageObject, audioProgress);
    }
    
    @Override
    public void onSuccessDownload(final String s) {
        this.updateButtonState();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        boolean b = this.seekBar.onTouch(motionEvent.getAction(), motionEvent.getX() - this.seekBarX, motionEvent.getY() - this.seekBarY);
        if (b) {
            if (motionEvent.getAction() == 0) {
                this.getParent().requestDisallowInterceptTouchEvent(true);
            }
            this.invalidate();
        }
        else {
            final int dp = AndroidUtilities.dp(36.0f);
            boolean b2 = false;
            Label_0315: {
                if (motionEvent.getAction() == 0) {
                    final int buttonX = this.buttonX;
                    b2 = b;
                    if (x >= buttonX) {
                        b2 = b;
                        if (x <= buttonX + dp) {
                            final int buttonY = this.buttonY;
                            b2 = b;
                            if (y >= buttonY) {
                                b2 = b;
                                if (y <= buttonY + dp) {
                                    this.buttonPressed = 1;
                                    this.invalidate();
                                    b2 = true;
                                }
                            }
                        }
                    }
                }
                else {
                    b2 = b;
                    if (this.buttonPressed == 1) {
                        if (motionEvent.getAction() == 1) {
                            this.playSoundEffect(this.buttonPressed = 0);
                            this.didPressedButton();
                            this.invalidate();
                            b2 = b;
                        }
                        else if (motionEvent.getAction() == 3) {
                            this.buttonPressed = 0;
                            this.invalidate();
                            b2 = b;
                        }
                        else {
                            b2 = b;
                            if (motionEvent.getAction() == 2) {
                                final int buttonX2 = this.buttonX;
                                if (x >= buttonX2 && x <= buttonX2 + dp) {
                                    final int buttonY2 = this.buttonY;
                                    if (y >= buttonY2) {
                                        b2 = b;
                                        if (y <= buttonY2 + dp) {
                                            break Label_0315;
                                        }
                                    }
                                }
                                this.buttonPressed = 0;
                                this.invalidate();
                                b2 = b;
                            }
                        }
                    }
                }
            }
            if (!(b = b2)) {
                b = super.onTouchEvent(motionEvent);
            }
        }
        return b;
    }
    
    public void setMessageObject(final MessageObject currentMessageObject) {
        if (this.currentMessageObject != currentMessageObject) {
            this.currentAccount = currentMessageObject.currentAccount;
            this.seekBar.setColors(Theme.getColor("chat_inAudioSeekbar"), Theme.getColor("chat_inAudioSeekbar"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarFill"), Theme.getColor("chat_inAudioSeekbarSelected"));
            this.progressView.setProgressColors(-2497813, -7944712);
            this.currentMessageObject = currentMessageObject;
            this.wasLayout = false;
            this.requestLayout();
        }
        this.updateButtonState();
    }
    
    public void updateButtonState() {
        final String fileName = this.currentMessageObject.getFileName();
        if (FileLoader.getPathToMessage(this.currentMessageObject.messageOwner).exists()) {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
            final boolean playingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (playingMessage && (!playingMessage || !MediaController.getInstance().isMessagePaused())) {
                this.buttonState = 1;
            }
            else {
                this.buttonState = 0;
            }
            this.progressView.setProgress(0.0f);
        }
        else {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, (DownloadController.FileDownloadProgressListener)this);
            if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                this.buttonState = 2;
                this.progressView.setProgress(0.0f);
            }
            else {
                this.buttonState = 3;
                final Float fileProgress = ImageLoader.getInstance().getFileProgress(fileName);
                if (fileProgress != null) {
                    this.progressView.setProgress(fileProgress);
                }
                else {
                    this.progressView.setProgress(0.0f);
                }
            }
        }
        this.updateProgress();
    }
    
    public void updateProgress() {
        if (this.currentMessageObject == null) {
            return;
        }
        if (!this.seekBar.isDragging()) {
            this.seekBar.setProgress(this.currentMessageObject.audioProgress);
        }
        int n = 0;
        Label_0116: {
            if (!MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                for (int i = 0; i < this.currentMessageObject.getDocument().attributes.size(); ++i) {
                    final TLRPC.DocumentAttribute documentAttribute = this.currentMessageObject.getDocument().attributes.get(i);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                        n = documentAttribute.duration;
                        break Label_0116;
                    }
                }
                n = 0;
            }
            else {
                n = this.currentMessageObject.audioProgressSec;
            }
        }
        final String format = String.format("%02d:%02d", n / 60, n % 60);
        final String lastTimeString = this.lastTimeString;
        if (lastTimeString == null || (lastTimeString != null && !lastTimeString.equals(format))) {
            this.timeWidth = (int)Math.ceil(this.timePaint.measureText(format));
            this.timeLayout = new StaticLayout((CharSequence)format, this.timePaint, this.timeWidth, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        this.invalidate();
    }
}
