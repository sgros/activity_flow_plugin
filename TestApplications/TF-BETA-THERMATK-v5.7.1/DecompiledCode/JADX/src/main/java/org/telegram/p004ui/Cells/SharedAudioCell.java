package org.telegram.p004ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.FileDownloadProgressListener;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.CheckBox2;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RadialProgress2;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_photoSize;

/* renamed from: org.telegram.ui.Cells.SharedAudioCell */
public class SharedAudioCell extends FrameLayout implements FileDownloadProgressListener {
    private int TAG;
    private boolean buttonPressed;
    private int buttonState;
    private int buttonX;
    private int buttonY;
    private CheckBox2 checkBox;
    private int currentAccount = UserConfig.selectedAccount;
    private MessageObject currentMessageObject;
    private StaticLayout descriptionLayout;
    private int descriptionY = AndroidUtilities.m26dp(29.0f);
    private int hasMiniProgress;
    private boolean miniButtonPressed;
    private int miniButtonState;
    private boolean needDivider;
    private RadialProgress2 radialProgress;
    private StaticLayout titleLayout;
    private int titleY = AndroidUtilities.m26dp(9.0f);

    /* Access modifiers changed, original: protected */
    public boolean needPlayMessage(MessageObject messageObject) {
        return false;
    }

    public void onProgressUpload(String str, float f, boolean z) {
    }

    public SharedAudioCell(Context context) {
        super(context);
        setFocusable(true);
        this.radialProgress = new RadialProgress2(this);
        this.radialProgress.setColors(Theme.key_chat_inLoader, Theme.key_chat_inLoaderSelected, Theme.key_chat_inMediaIcon, Theme.key_chat_inMediaIconSelected);
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        setWillNotDraw(false);
        this.checkBox = new CheckBox2(context);
        this.checkBox.setVisibility(4);
        this.checkBox.setColor(null, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
        this.checkBox.setSize(21);
        this.checkBox.setDrawUnchecked(false);
        int i = 3;
        this.checkBox.setDrawBackgroundAsArc(3);
        CheckBox2 checkBox2 = this.checkBox;
        if (LocaleController.isRTL) {
            i = 5;
        }
        addView(checkBox2, LayoutHelper.createFrame(24, 24.0f, i | 48, LocaleController.isRTL ? 0.0f : 38.0f, 32.0f, LocaleController.isRTL ? 39.0f : 0.0f, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    @SuppressLint({"DrawAllocation"})
    public void onMeasure(int i, int i2) {
        String musicTitle;
        this.descriptionLayout = null;
        this.titleLayout = null;
        int size = (MeasureSpec.getSize(i) - AndroidUtilities.m26dp((float) AndroidUtilities.leftBaseline)) - AndroidUtilities.m26dp(28.0f);
        try {
            musicTitle = this.currentMessageObject.getMusicTitle();
            this.titleLayout = new StaticLayout(TextUtils.ellipsize(musicTitle.replace(10, ' '), Theme.chat_contextResult_titleTextPaint, (float) Math.min((int) Math.ceil((double) Theme.chat_contextResult_titleTextPaint.measureText(musicTitle)), size), TruncateAt.END), Theme.chat_contextResult_titleTextPaint, size + AndroidUtilities.m26dp(4.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        try {
            musicTitle = this.currentMessageObject.getMusicAuthor();
            this.descriptionLayout = new StaticLayout(TextUtils.ellipsize(musicTitle.replace(10, ' '), Theme.chat_contextResult_descriptionTextPaint, (float) Math.min((int) Math.ceil((double) Theme.chat_contextResult_descriptionTextPaint.measureText(musicTitle)), size), TruncateAt.END), Theme.chat_contextResult_descriptionTextPaint, size + AndroidUtilities.m26dp(4.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } catch (Exception e2) {
            FileLog.m30e(e2);
        }
        setMeasuredDimension(MeasureSpec.getSize(i), AndroidUtilities.m26dp(56.0f) + this.needDivider);
        size = LocaleController.isRTL ? (MeasureSpec.getSize(i) - AndroidUtilities.m26dp(8.0f)) - AndroidUtilities.m26dp(52.0f) : AndroidUtilities.m26dp(8.0f);
        RadialProgress2 radialProgress2 = this.radialProgress;
        int dp = AndroidUtilities.m26dp(4.0f) + size;
        this.buttonX = dp;
        int dp2 = AndroidUtilities.m26dp(6.0f);
        this.buttonY = dp2;
        radialProgress2.setProgressRect(dp, dp2, size + AndroidUtilities.m26dp(48.0f), AndroidUtilities.m26dp(50.0f));
        measureChildWithMargins(this.checkBox, i, 0, i2, 0);
    }

    public void setMessageObject(MessageObject messageObject, boolean z) {
        this.needDivider = z;
        this.currentMessageObject = messageObject;
        Document document = messageObject.getDocument();
        PhotoSize closestPhotoSizeWithSize = document != null ? FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90) : null;
        if (closestPhotoSizeWithSize instanceof TL_photoSize) {
            this.radialProgress.setImageOverlay(closestPhotoSizeWithSize, document, messageObject);
        } else {
            String artworkUrl = messageObject.getArtworkUrl(true);
            if (TextUtils.isEmpty(artworkUrl)) {
                this.radialProgress.setImageOverlay(null, null, null);
            } else {
                this.radialProgress.setImageOverlay(artworkUrl);
            }
        }
        updateButtonState(false, false);
        requestLayout();
    }

    public void setChecked(boolean z, boolean z2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.radialProgress.onAttachedToWindow();
        updateButtonState(false, false);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.radialProgress.onDetachedFromWindow();
    }

    public MessageObject getMessage() {
        return this.currentMessageObject;
    }

    public void initStreamingIcons() {
        this.radialProgress.initMiniIcons();
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0039  */
    private boolean checkAudioMotionEvent(android.view.MotionEvent r9) {
        /*
        r8 = this;
        r0 = r9.getX();
        r0 = (int) r0;
        r1 = r9.getY();
        r1 = (int) r1;
        r2 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = r8.miniButtonState;
        r4 = 1;
        r5 = 0;
        if (r3 < 0) goto L_0x0032;
    L_0x0016:
        r3 = 1104674816; // 0x41d80000 float:27.0 double:5.457818764E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r6 = r8.buttonX;
        r7 = r6 + r3;
        if (r0 < r7) goto L_0x0032;
    L_0x0022:
        r6 = r6 + r3;
        r6 = r6 + r2;
        if (r0 > r6) goto L_0x0032;
    L_0x0026:
        r0 = r8.buttonY;
        r6 = r0 + r3;
        if (r1 < r6) goto L_0x0032;
    L_0x002c:
        r0 = r0 + r3;
        r0 = r0 + r2;
        if (r1 > r0) goto L_0x0032;
    L_0x0030:
        r0 = 1;
        goto L_0x0033;
    L_0x0032:
        r0 = 0;
    L_0x0033:
        r1 = r9.getAction();
        if (r1 != 0) goto L_0x0048;
    L_0x0039:
        if (r0 == 0) goto L_0x0080;
    L_0x003b:
        r8.miniButtonPressed = r4;
        r9 = r8.radialProgress;
        r0 = r8.miniButtonPressed;
        r9.setPressed(r0, r4);
        r8.invalidate();
        goto L_0x0081;
    L_0x0048:
        r1 = r8.miniButtonPressed;
        if (r1 == 0) goto L_0x0080;
    L_0x004c:
        r1 = r9.getAction();
        if (r1 != r4) goto L_0x005e;
    L_0x0052:
        r8.miniButtonPressed = r5;
        r8.playSoundEffect(r5);
        r8.didPressedMiniButton(r4);
        r8.invalidate();
        goto L_0x0079;
    L_0x005e:
        r1 = r9.getAction();
        r2 = 3;
        if (r1 != r2) goto L_0x006b;
    L_0x0065:
        r8.miniButtonPressed = r5;
        r8.invalidate();
        goto L_0x0079;
    L_0x006b:
        r9 = r9.getAction();
        r1 = 2;
        if (r9 != r1) goto L_0x0079;
    L_0x0072:
        if (r0 != 0) goto L_0x0079;
    L_0x0074:
        r8.miniButtonPressed = r5;
        r8.invalidate();
    L_0x0079:
        r9 = r8.radialProgress;
        r0 = r8.miniButtonPressed;
        r9.setPressed(r0, r4);
    L_0x0080:
        r4 = 0;
    L_0x0081:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.SharedAudioCell.checkAudioMotionEvent(android.view.MotionEvent):boolean");
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.currentMessageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        boolean checkAudioMotionEvent = checkAudioMotionEvent(motionEvent);
        if (motionEvent.getAction() == 3) {
            this.miniButtonPressed = false;
            this.buttonPressed = false;
            this.radialProgress.setPressed(this.buttonPressed, false);
            this.radialProgress.setPressed(this.miniButtonPressed, true);
            checkAudioMotionEvent = false;
        }
        return checkAudioMotionEvent;
    }

    private void didPressedMiniButton(boolean z) {
        int i = this.miniButtonState;
        if (i == 0) {
            this.miniButtonState = 1;
            this.radialProgress.setProgress(0.0f, false);
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
            invalidate();
        } else if (i == 1) {
            if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            this.miniButtonState = 0;
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
            this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
            invalidate();
        }
    }

    public void didPressedButton() {
        int i = this.buttonState;
        if (i == 0) {
            if (this.miniButtonState == 0) {
                FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            }
            if (needPlayMessage(this.currentMessageObject)) {
                if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
                    this.miniButtonState = 1;
                    this.radialProgress.setProgress(0.0f, false);
                    this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
                }
                this.buttonState = 1;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
            }
        } else if (i == 1) {
            if (MediaController.getInstance().lambda$startAudioAgain$5$MediaController(this.currentMessageObject)) {
                this.buttonState = 0;
                this.radialProgress.setIcon(getIconForCurrentState(), false, true);
                invalidate();
            }
        } else if (i == 2) {
            this.radialProgress.setProgress(0.0f, false);
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            this.buttonState = 4;
            this.radialProgress.setIcon(getIconForCurrentState(), false, true);
            invalidate();
        } else if (i == 4) {
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
            this.buttonState = 2;
            this.radialProgress.setIcon(getIconForCurrentState(), false, true);
            invalidate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        float f = 8.0f;
        if (this.titleLayout != null) {
            canvas.save();
            canvas.translate((float) AndroidUtilities.m26dp(LocaleController.isRTL ? 8.0f : (float) AndroidUtilities.leftBaseline), (float) this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
            canvas.save();
            if (!LocaleController.isRTL) {
                f = (float) AndroidUtilities.leftBaseline;
            }
            canvas.translate((float) AndroidUtilities.m26dp(f), (float) this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        this.radialProgress.setProgressColor(Theme.getColor(this.buttonPressed ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress));
        this.radialProgress.draw(canvas);
        if (this.needDivider) {
            canvas.drawLine((float) AndroidUtilities.m26dp(72.0f), (float) (getHeight() - 1), (float) (getWidth() - getPaddingRight()), (float) (getHeight() - 1), Theme.dividerPaint);
        }
    }

    private int getMiniIconForCurrentState() {
        int i = this.miniButtonState;
        if (i < 0) {
            return 4;
        }
        return i == 0 ? 2 : 3;
    }

    private int getIconForCurrentState() {
        int i = this.buttonState;
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        return i == 4 ? 3 : 0;
    }

    public void updateButtonState(boolean z, boolean z2) {
        String fileName = this.currentMessageObject.getFileName();
        if (!TextUtils.isEmpty(fileName)) {
            MessageObject messageObject = this.currentMessageObject;
            Object obj = (messageObject.attachPathExists || messageObject.mediaExists) ? 1 : null;
            if (SharedConfig.streamMedia && this.currentMessageObject.isMusic() && ((int) this.currentMessageObject.getDialogId()) != 0) {
                this.hasMiniProgress = obj != null ? 1 : 2;
                obj = 1;
            } else {
                this.hasMiniProgress = 0;
                this.miniButtonState = -1;
            }
            if (this.hasMiniProgress != 0) {
                this.radialProgress.setMiniProgressBackgroundColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outLoader : Theme.key_chat_inLoader));
                boolean isPlayingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (!isPlayingMessage || (isPlayingMessage && MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 0;
                } else {
                    this.buttonState = 1;
                }
                this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
                if (this.hasMiniProgress == 1) {
                    DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                    this.miniButtonState = -1;
                    this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), z, z2);
                } else {
                    DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
                    if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                        this.miniButtonState = 1;
                        this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), z, z2);
                        Float fileProgress = ImageLoader.getInstance().getFileProgress(fileName);
                        if (fileProgress != null) {
                            this.radialProgress.setProgress(fileProgress.floatValue(), z2);
                        } else {
                            this.radialProgress.setProgress(0.0f, z2);
                        }
                    } else {
                        this.miniButtonState = 0;
                        this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), z, z2);
                    }
                }
            } else if (obj != null) {
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                boolean isPlayingMessage2 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (!isPlayingMessage2 || (isPlayingMessage2 && MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 0;
                } else {
                    this.buttonState = 1;
                }
                this.radialProgress.setProgress(1.0f, z2);
                this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
                invalidate();
            } else {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
                if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                    this.buttonState = 4;
                    Float fileProgress2 = ImageLoader.getInstance().getFileProgress(fileName);
                    if (fileProgress2 != null) {
                        this.radialProgress.setProgress(fileProgress2.floatValue(), z2);
                    } else {
                        this.radialProgress.setProgress(0.0f, z2);
                    }
                    this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
                } else {
                    this.buttonState = 2;
                    this.radialProgress.setProgress(0.0f, z2);
                    this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
                }
                invalidate();
            }
        }
    }

    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    public void onSuccessDownload(String str) {
        this.radialProgress.setProgress(1.0f, true);
        updateButtonState(false, true);
    }

    public void onProgressDownload(String str, float f) {
        this.radialProgress.setProgress(f, true);
        if (this.hasMiniProgress != 0) {
            if (this.miniButtonState != 1) {
                updateButtonState(false, true);
            }
        } else if (this.buttonState != 4) {
            updateButtonState(false, true);
        }
    }

    public int getObserverTag() {
        return this.TAG;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.currentMessageObject.isMusic()) {
            accessibilityNodeInfo.setText(LocaleController.formatString("AccDescrMusicInfo", C1067R.string.AccDescrMusicInfo, this.currentMessageObject.getMusicAuthor(), this.currentMessageObject.getMusicTitle()));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.titleLayout.getText());
            stringBuilder.append(", ");
            stringBuilder.append(this.descriptionLayout.getText());
            accessibilityNodeInfo.setText(stringBuilder.toString());
        }
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setChecked(true);
        }
    }
}