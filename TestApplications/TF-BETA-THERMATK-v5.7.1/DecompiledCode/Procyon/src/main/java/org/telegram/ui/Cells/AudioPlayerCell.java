// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.TLObject;
import java.io.File;
import org.telegram.tgnet.TLRPC;
import android.annotation.SuppressLint;
import org.telegram.messenger.FileLog;
import android.text.Layout$Alignment;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import android.graphics.Canvas;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.FileLoader;
import android.view.MotionEvent;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.Components.RadialProgress2;
import android.text.StaticLayout;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.DownloadController;
import android.view.View;

public class AudioPlayerCell extends View implements FileDownloadProgressListener
{
    private int TAG;
    private boolean buttonPressed;
    private int buttonState;
    private int buttonX;
    private int buttonY;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private StaticLayout descriptionLayout;
    private int descriptionY;
    private int hasMiniProgress;
    private boolean miniButtonPressed;
    private int miniButtonState;
    private RadialProgress2 radialProgress;
    private StaticLayout titleLayout;
    private int titleY;
    
    public AudioPlayerCell(final Context context) {
        super(context);
        this.titleY = AndroidUtilities.dp(9.0f);
        this.descriptionY = AndroidUtilities.dp(29.0f);
        this.currentAccount = UserConfig.selectedAccount;
        (this.radialProgress = new RadialProgress2(this)).setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.setFocusable(true);
    }
    
    private boolean checkAudioMotionEvent(final MotionEvent motionEvent) {
        final int n = (int)motionEvent.getX();
        final int n2 = (int)motionEvent.getY();
        final int dp = AndroidUtilities.dp(36.0f);
        final int miniButtonState = this.miniButtonState;
        boolean b = true;
        boolean b2 = false;
        Label_0098: {
            if (miniButtonState >= 0) {
                final int dp2 = AndroidUtilities.dp(27.0f);
                final int buttonX = this.buttonX;
                if (n >= buttonX + dp2 && n <= buttonX + dp2 + dp) {
                    final int buttonY = this.buttonY;
                    if (n2 >= buttonY + dp2 && n2 <= buttonY + dp2 + dp) {
                        b2 = true;
                        break Label_0098;
                    }
                }
            }
            b2 = false;
        }
        if (motionEvent.getAction() == 0) {
            if (b2) {
                this.miniButtonPressed = true;
                this.radialProgress.setPressed(this.miniButtonPressed, true);
                this.invalidate();
                return b;
            }
        }
        else if (this.miniButtonPressed) {
            if (motionEvent.getAction() == 1) {
                this.miniButtonPressed = false;
                this.playSoundEffect(0);
                this.didPressedMiniButton(true);
                this.invalidate();
            }
            else if (motionEvent.getAction() == 3) {
                this.miniButtonPressed = false;
                this.invalidate();
            }
            else if (motionEvent.getAction() == 2 && !b2) {
                this.miniButtonPressed = false;
                this.invalidate();
            }
            this.radialProgress.setPressed(this.miniButtonPressed, true);
        }
        b = false;
        return b;
    }
    
    private void didPressedMiniButton(final boolean b) {
        final int miniButtonState = this.miniButtonState;
        if (miniButtonState == 0) {
            this.miniButtonState = 1;
            this.radialProgress.setProgress(0.0f, false);
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
            this.invalidate();
        }
        else if (miniButtonState == 1) {
            if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            this.miniButtonState = 0;
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
            this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
            this.invalidate();
        }
    }
    
    private int getIconForCurrentState() {
        final int buttonState = this.buttonState;
        if (buttonState == 1) {
            return 1;
        }
        if (buttonState == 2) {
            return 2;
        }
        if (buttonState == 4) {
            return 3;
        }
        return 0;
    }
    
    private int getMiniIconForCurrentState() {
        final int miniButtonState = this.miniButtonState;
        if (miniButtonState < 0) {
            return 4;
        }
        if (miniButtonState == 0) {
            return 2;
        }
        return 3;
    }
    
    public void didPressedButton() {
        final int buttonState = this.buttonState;
        if (buttonState == 0) {
            if (this.miniButtonState == 0) {
                FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            }
            if (MediaController.getInstance().findMessageInPlaylistAndPlay(this.currentMessageObject)) {
                if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
                    this.miniButtonState = 1;
                    this.radialProgress.setProgress(0.0f, false);
                    this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), false, true);
                }
                this.buttonState = 1;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
                this.invalidate();
            }
        }
        else if (buttonState == 1) {
            if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
                this.buttonState = 0;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
                this.invalidate();
            }
        }
        else if (buttonState == 2) {
            this.radialProgress.setProgress(0.0f, false);
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            this.buttonState = 4;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
            this.invalidate();
        }
        else if (buttonState == 4) {
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
            this.buttonState = 2;
            this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
            this.invalidate();
        }
    }
    
    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }
    
    public int getObserverTag() {
        return this.TAG;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.radialProgress.onAttachedToWindow();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.radialProgress.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
    }
    
    protected void onDraw(final Canvas canvas) {
        final StaticLayout titleLayout = this.titleLayout;
        final float n = 8.0f;
        if (titleLayout != null) {
            canvas.save();
            float n2;
            if (LocaleController.isRTL) {
                n2 = 8.0f;
            }
            else {
                n2 = (float)AndroidUtilities.leftBaseline;
            }
            canvas.translate((float)AndroidUtilities.dp(n2), (float)this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            canvas.save();
            float n3;
            if (LocaleController.isRTL) {
                n3 = n;
            }
            else {
                n3 = (float)AndroidUtilities.leftBaseline;
            }
            canvas.translate((float)AndroidUtilities.dp(n3), (float)this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        final RadialProgress2 radialProgress = this.radialProgress;
        String s;
        if (this.buttonPressed) {
            s = "chat_inAudioSelectedProgress";
        }
        else {
            s = "chat_inAudioProgress";
        }
        radialProgress.setProgressColor(Theme.getColor(s));
        this.radialProgress.draw(canvas);
    }
    
    public void onFailedDownload(final String s, final boolean b) {
        this.updateButtonState(true, b);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.currentMessageObject.isMusic()) {
            accessibilityNodeInfo.setText((CharSequence)LocaleController.formatString("AccDescrMusicInfo", 2131558447, this.currentMessageObject.getMusicAuthor(), this.currentMessageObject.getMusicTitle()));
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append((Object)this.titleLayout.getText());
            sb.append(", ");
            sb.append((Object)this.descriptionLayout.getText());
            accessibilityNodeInfo.setText((CharSequence)sb.toString());
        }
    }
    
    @SuppressLint({ "DrawAllocation" })
    protected void onMeasure(int dp, int buttonY) {
        this.descriptionLayout = null;
        this.titleLayout = null;
        buttonY = View$MeasureSpec.getSize(dp) - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - AndroidUtilities.dp(28.0f);
        try {
            final String musicTitle = this.currentMessageObject.getMusicTitle();
            this.titleLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)musicTitle.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint, (float)Math.min((int)Math.ceil(Theme.chat_contextResult_titleTextPaint.measureText(musicTitle)), buttonY), TextUtils$TruncateAt.END), Theme.chat_contextResult_titleTextPaint, buttonY + AndroidUtilities.dp(4.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            final String musicAuthor = this.currentMessageObject.getMusicAuthor();
            this.descriptionLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)musicAuthor.replace('\n', ' '), Theme.chat_contextResult_descriptionTextPaint, (float)Math.min((int)Math.ceil(Theme.chat_contextResult_descriptionTextPaint.measureText(musicAuthor)), buttonY), TextUtils$TruncateAt.END), Theme.chat_contextResult_descriptionTextPaint, buttonY + AndroidUtilities.dp(4.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        this.setMeasuredDimension(View$MeasureSpec.getSize(dp), AndroidUtilities.dp(56.0f));
        buttonY = AndroidUtilities.dp(52.0f);
        if (LocaleController.isRTL) {
            dp = View$MeasureSpec.getSize(dp) - AndroidUtilities.dp(8.0f) - buttonY;
        }
        else {
            dp = AndroidUtilities.dp(8.0f);
        }
        final RadialProgress2 radialProgress = this.radialProgress;
        final int buttonX = AndroidUtilities.dp(4.0f) + dp;
        this.buttonX = buttonX;
        buttonY = AndroidUtilities.dp(6.0f);
        radialProgress.setProgressRect(buttonX, this.buttonY = buttonY, dp + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(50.0f));
    }
    
    public void onProgressDownload(final String s, final float n) {
        this.radialProgress.setProgress(n, true);
        if (this.hasMiniProgress != 0) {
            if (this.miniButtonState != 1) {
                this.updateButtonState(false, true);
            }
        }
        else if (this.buttonState != 4) {
            this.updateButtonState(false, true);
        }
    }
    
    public void onProgressUpload(final String s, final float n, final boolean b) {
    }
    
    public void onSuccessDownload(final String s) {
        this.radialProgress.setProgress(1.0f, true);
        this.updateButtonState(false, true);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.currentMessageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        boolean checkAudioMotionEvent = this.checkAudioMotionEvent(motionEvent);
        if (motionEvent.getAction() == 3) {
            this.miniButtonPressed = false;
            this.buttonPressed = false;
            checkAudioMotionEvent = false;
        }
        return checkAudioMotionEvent;
    }
    
    public void setMessageObject(final MessageObject currentMessageObject) {
        this.currentMessageObject = currentMessageObject;
        final TLRPC.Document document = currentMessageObject.getDocument();
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        if (document != null) {
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
        }
        else {
            closestPhotoSizeWithSize = null;
        }
        if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize) {
            this.radialProgress.setImageOverlay(closestPhotoSizeWithSize, document, currentMessageObject);
        }
        else {
            final String artworkUrl = currentMessageObject.getArtworkUrl(true);
            if (!TextUtils.isEmpty((CharSequence)artworkUrl)) {
                this.radialProgress.setImageOverlay(artworkUrl);
            }
            else {
                this.radialProgress.setImageOverlay(null, null, null);
            }
        }
        this.requestLayout();
        this.updateButtonState(false, false);
    }
    
    public void updateButtonState(final boolean b, final boolean b2) {
        final String fileName = this.currentMessageObject.getFileName();
        final boolean empty = TextUtils.isEmpty((CharSequence)this.currentMessageObject.messageOwner.attachPath);
        File file2;
        final File file = file2 = null;
        if (!empty) {
            file2 = new File(this.currentMessageObject.messageOwner.attachPath);
            if (!file2.exists()) {
                file2 = file;
            }
        }
        File pathToAttach;
        if ((pathToAttach = file2) == null) {
            pathToAttach = FileLoader.getPathToAttach(this.currentMessageObject.getDocument());
        }
        if (TextUtils.isEmpty((CharSequence)fileName)) {
            return;
        }
        if (pathToAttach.exists() && pathToAttach.length() == 0L) {
            pathToAttach.delete();
        }
        int exists = pathToAttach.exists() ? 1 : 0;
        if (SharedConfig.streamMedia && (int)this.currentMessageObject.getDialogId() != 0) {
            int hasMiniProgress;
            if (exists != 0) {
                hasMiniProgress = 1;
            }
            else {
                hasMiniProgress = 2;
            }
            this.hasMiniProgress = hasMiniProgress;
            exists = 1;
        }
        else {
            this.miniButtonState = -1;
        }
        if (this.hasMiniProgress != 0) {
            final RadialProgress2 radialProgress = this.radialProgress;
            String s;
            if (this.currentMessageObject.isOutOwner()) {
                s = "chat_outLoader";
            }
            else {
                s = "chat_inLoader";
            }
            radialProgress.setMiniProgressBackgroundColor(Theme.getColor(s));
            final boolean playingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (playingMessage && (!playingMessage || !MediaController.getInstance().isMessagePaused())) {
                this.buttonState = 1;
            }
            else {
                this.buttonState = 0;
            }
            this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
            if (this.hasMiniProgress == 1) {
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
                this.miniButtonState = -1;
                this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), b, b2);
            }
            else {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, (DownloadController.FileDownloadProgressListener)this);
                if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                    this.miniButtonState = 0;
                    this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), b, b2);
                }
                else {
                    this.miniButtonState = 1;
                    this.radialProgress.setMiniIcon(this.getMiniIconForCurrentState(), b, b2);
                    final Float fileProgress = ImageLoader.getInstance().getFileProgress(fileName);
                    if (fileProgress != null) {
                        this.radialProgress.setProgress(fileProgress, b2);
                    }
                    else {
                        this.radialProgress.setProgress(0.0f, b2);
                    }
                }
            }
        }
        else if (exists != 0) {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
            final boolean playingMessage2 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
            if (playingMessage2 && (!playingMessage2 || !MediaController.getInstance().isMessagePaused())) {
                this.buttonState = 1;
            }
            else {
                this.buttonState = 0;
            }
            this.radialProgress.setProgress(1.0f, b2);
            this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
            this.invalidate();
        }
        else {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, (DownloadController.FileDownloadProgressListener)this);
            if (!FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                this.buttonState = 2;
                this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
            }
            else {
                this.buttonState = 4;
                final Float fileProgress2 = ImageLoader.getInstance().getFileProgress(fileName);
                if (fileProgress2 != null) {
                    this.radialProgress.setProgress(fileProgress2, b2);
                }
                else {
                    this.radialProgress.setProgress(0.0f, b2);
                }
                this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
            }
            this.invalidate();
        }
    }
}
