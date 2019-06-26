// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.tgnet.TLObject;
import android.view.MotionEvent;
import android.annotation.SuppressLint;
import android.text.TextPaint;
import org.telegram.messenger.ImageLocation;
import java.util.Locale;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.FileLog;
import android.text.Layout$Alignment;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.Emoji;
import java.util.Collection;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import android.graphics.Canvas;
import java.io.File;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Components.LetterDrawable;
import android.text.StaticLayout;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.MessageObject;
import android.view.animation.AccelerateInterpolator;
import org.telegram.messenger.DownloadController;
import android.view.View;

public class ContextLinkCell extends View implements FileDownloadProgressListener
{
    private static final int DOCUMENT_ATTACH_TYPE_AUDIO = 3;
    private static final int DOCUMENT_ATTACH_TYPE_DOCUMENT = 1;
    private static final int DOCUMENT_ATTACH_TYPE_GEO = 8;
    private static final int DOCUMENT_ATTACH_TYPE_GIF = 2;
    private static final int DOCUMENT_ATTACH_TYPE_MUSIC = 5;
    private static final int DOCUMENT_ATTACH_TYPE_NONE = 0;
    private static final int DOCUMENT_ATTACH_TYPE_PHOTO = 7;
    private static final int DOCUMENT_ATTACH_TYPE_STICKER = 6;
    private static final int DOCUMENT_ATTACH_TYPE_VIDEO = 4;
    private static AccelerateInterpolator interpolator;
    private int TAG;
    private boolean buttonPressed;
    private int buttonState;
    private boolean canPreviewGif;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private TLRPC.PhotoSize currentPhotoObject;
    private ContextLinkCellDelegate delegate;
    private StaticLayout descriptionLayout;
    private int descriptionY;
    private TLRPC.Document documentAttach;
    private int documentAttachType;
    private boolean drawLinkImageView;
    private TLRPC.BotInlineResult inlineResult;
    private long lastUpdateTime;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private StaticLayout linkLayout;
    private int linkY;
    private boolean mediaWebpage;
    private boolean needDivider;
    private boolean needShadow;
    private Object parentObject;
    private TLRPC.Photo photoAttach;
    private RadialProgress2 radialProgress;
    private float scale;
    private boolean scaled;
    private StaticLayout titleLayout;
    private int titleY;
    
    static {
        ContextLinkCell.interpolator = new AccelerateInterpolator(0.5f);
    }
    
    public ContextLinkCell(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.titleY = AndroidUtilities.dp(7.0f);
        this.descriptionY = AndroidUtilities.dp(27.0f);
        (this.linkImageView = new ImageReceiver(this)).setUseSharedAnimationQueue(true);
        this.letterDrawable = new LetterDrawable();
        this.radialProgress = new RadialProgress2(this);
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.setFocusable(true);
    }
    
    private void didPressedButton() {
        final int documentAttachType = this.documentAttachType;
        if (documentAttachType == 3 || documentAttachType == 5) {
            final int buttonState = this.buttonState;
            if (buttonState == 0) {
                if (MediaController.getInstance().playMessage(this.currentMessageObject)) {
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
                if (this.documentAttach != null) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, this.inlineResult, 1, 0);
                }
                else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                    FileLoader.getInstance(this.currentAccount).loadFile(WebFile.createWithWebDocument(this.inlineResult.content), 1, 1);
                }
                this.buttonState = 4;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
                this.invalidate();
            }
            else if (buttonState == 4) {
                if (this.documentAttach != null) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                }
                else if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(WebFile.createWithWebDocument(this.inlineResult.content));
                }
                this.buttonState = 2;
                this.radialProgress.setIcon(this.getIconForCurrentState(), false, true);
                this.invalidate();
            }
        }
    }
    
    private int getIconForCurrentState() {
        final int documentAttachType = this.documentAttachType;
        int n = 4;
        if (documentAttachType != 3 && documentAttachType != 5) {
            this.radialProgress.setColors("chat_mediaLoaderPhoto", "chat_mediaLoaderPhotoSelected", "chat_mediaLoaderPhotoIcon", "chat_mediaLoaderPhotoIconSelected");
            if (this.buttonState == 1) {
                n = 10;
            }
            return n;
        }
        this.radialProgress.setColors("chat_inLoader", "chat_inLoaderSelected", "chat_inMediaIcon", "chat_inMediaIconSelected");
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
    
    private void setAttachType() {
        this.currentMessageObject = null;
        this.documentAttachType = 0;
        final TLRPC.Document documentAttach = this.documentAttach;
        if (documentAttach != null) {
            if (MessageObject.isGifDocument(documentAttach)) {
                this.documentAttachType = 2;
            }
            else if (MessageObject.isStickerDocument(this.documentAttach)) {
                this.documentAttachType = 6;
            }
            else if (MessageObject.isMusicDocument(this.documentAttach)) {
                this.documentAttachType = 5;
            }
            else if (MessageObject.isVoiceDocument(this.documentAttach)) {
                this.documentAttachType = 3;
            }
        }
        else {
            final TLRPC.BotInlineResult inlineResult = this.inlineResult;
            if (inlineResult != null) {
                if (inlineResult.photo != null) {
                    this.documentAttachType = 7;
                }
                else if (inlineResult.type.equals("audio")) {
                    this.documentAttachType = 5;
                }
                else if (this.inlineResult.type.equals("voice")) {
                    this.documentAttachType = 3;
                }
            }
        }
        final int documentAttachType = this.documentAttachType;
        if (documentAttachType == 3 || documentAttachType == 5) {
            final TLRPC.TL_message tl_message = new TLRPC.TL_message();
            tl_message.out = true;
            tl_message.id = -Utilities.random.nextInt();
            tl_message.to_id = new TLRPC.TL_peerUser();
            final TLRPC.Peer to_id = tl_message.to_id;
            final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            tl_message.from_id = clientUserId;
            to_id.user_id = clientUserId;
            tl_message.date = (int)(System.currentTimeMillis() / 1000L);
            final String s = "";
            tl_message.message = "";
            tl_message.media = new TLRPC.TL_messageMediaDocument();
            final TLRPC.MessageMedia media = tl_message.media;
            media.flags |= 0x3;
            media.document = new TLRPC.TL_document();
            final TLRPC.MessageMedia media2 = tl_message.media;
            media2.document.file_reference = new byte[0];
            tl_message.flags |= 0x300;
            final TLRPC.Document documentAttach2 = this.documentAttach;
            if (documentAttach2 != null) {
                media2.document = documentAttach2;
                tl_message.attachPath = "";
            }
            else {
                final String url = this.inlineResult.content.url;
                final int documentAttachType2 = this.documentAttachType;
                final String s2 = "mp3";
                String s3;
                if (documentAttachType2 == 5) {
                    s3 = "mp3";
                }
                else {
                    s3 = "ogg";
                }
                final String httpUrlExtension = ImageLoader.getHttpUrlExtension(url, s3);
                final TLRPC.Document document = tl_message.media.document;
                document.id = 0L;
                document.access_hash = 0L;
                document.date = tl_message.date;
                final StringBuilder sb = new StringBuilder();
                sb.append("audio/");
                sb.append(httpUrlExtension);
                document.mime_type = sb.toString();
                final TLRPC.Document document2 = tl_message.media.document;
                document2.size = 0;
                document2.dc_id = 0;
                final TLRPC.TL_documentAttributeAudio e = new TLRPC.TL_documentAttributeAudio();
                e.duration = MessageObject.getInlineResultDuration(this.inlineResult);
                String title = this.inlineResult.title;
                if (title == null) {
                    title = "";
                }
                e.title = title;
                final String description = this.inlineResult.description;
                String performer = s;
                if (description != null) {
                    performer = description;
                }
                e.performer = performer;
                e.flags |= 0x3;
                if (this.documentAttachType == 3) {
                    e.voice = true;
                }
                tl_message.media.document.attributes.add(e);
                final TLRPC.TL_documentAttributeFilename e2 = new TLRPC.TL_documentAttributeFilename();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(Utilities.MD5(this.inlineResult.content.url));
                sb2.append(".");
                final String url2 = this.inlineResult.content.url;
                String s4;
                if (this.documentAttachType == 5) {
                    s4 = "mp3";
                }
                else {
                    s4 = "ogg";
                }
                sb2.append(ImageLoader.getHttpUrlExtension(url2, s4));
                e2.file_name = sb2.toString();
                tl_message.media.document.attributes.add(e2);
                final File directory = FileLoader.getDirectory(4);
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(Utilities.MD5(this.inlineResult.content.url));
                sb3.append(".");
                final String url3 = this.inlineResult.content.url;
                String s5;
                if (this.documentAttachType == 5) {
                    s5 = s2;
                }
                else {
                    s5 = "ogg";
                }
                sb3.append(ImageLoader.getHttpUrlExtension(url3, s5));
                tl_message.attachPath = new File(directory, sb3.toString()).getAbsolutePath();
            }
            this.currentMessageObject = new MessageObject(this.currentAccount, tl_message, false);
        }
    }
    
    public TLRPC.BotInlineResult getBotInlineResult() {
        return this.inlineResult;
    }
    
    public TLRPC.Document getDocument() {
        return this.documentAttach;
    }
    
    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }
    
    public int getObserverTag() {
        return this.TAG;
    }
    
    public ImageReceiver getPhotoImage() {
        return this.linkImageView;
    }
    
    public TLRPC.BotInlineResult getResult() {
        return this.inlineResult;
    }
    
    public boolean isCanPreviewGif() {
        return this.canPreviewGif;
    }
    
    public boolean isGif() {
        return this.documentAttachType == 2 && this.canPreviewGif;
    }
    
    public boolean isSticker() {
        return this.documentAttachType == 6;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView && this.linkImageView.onAttachedToWindow()) {
            this.updateButtonState(false, false);
        }
        this.radialProgress.onAttachedToWindow();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onDetachedFromWindow();
        }
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
                n3 = 8.0f;
            }
            else {
                n3 = (float)AndroidUtilities.leftBaseline;
            }
            canvas.translate((float)AndroidUtilities.dp(n3), (float)this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.linkLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteLinkText"));
            canvas.save();
            float n4;
            if (LocaleController.isRTL) {
                n4 = n;
            }
            else {
                n4 = (float)AndroidUtilities.leftBaseline;
            }
            canvas.translate((float)AndroidUtilities.dp(n4), (float)this.linkY);
            this.linkLayout.draw(canvas);
            canvas.restore();
        }
        if (!this.mediaWebpage) {
            final int documentAttachType = this.documentAttachType;
            if (documentAttachType != 3 && documentAttachType != 5) {
                final TLRPC.BotInlineResult inlineResult = this.inlineResult;
                if (inlineResult != null && inlineResult.type.equals("file")) {
                    final int intrinsicWidth = Theme.chat_inlineResultFile.getIntrinsicWidth();
                    final int intrinsicHeight = Theme.chat_inlineResultFile.getIntrinsicHeight();
                    final int n5 = this.linkImageView.getImageX() + (AndroidUtilities.dp(52.0f) - intrinsicWidth) / 2;
                    final int n6 = this.linkImageView.getImageY() + (AndroidUtilities.dp(52.0f) - intrinsicHeight) / 2;
                    canvas.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f)), (float)(this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f)), LetterDrawable.paint);
                    Theme.chat_inlineResultFile.setBounds(n5, n6, intrinsicWidth + n5, intrinsicHeight + n6);
                    Theme.chat_inlineResultFile.draw(canvas);
                }
                else {
                    final TLRPC.BotInlineResult inlineResult2 = this.inlineResult;
                    if (inlineResult2 != null && (inlineResult2.type.equals("audio") || this.inlineResult.type.equals("voice"))) {
                        final int intrinsicWidth2 = Theme.chat_inlineResultAudio.getIntrinsicWidth();
                        final int intrinsicHeight2 = Theme.chat_inlineResultAudio.getIntrinsicHeight();
                        final int n7 = this.linkImageView.getImageX() + (AndroidUtilities.dp(52.0f) - intrinsicWidth2) / 2;
                        final int n8 = this.linkImageView.getImageY() + (AndroidUtilities.dp(52.0f) - intrinsicHeight2) / 2;
                        canvas.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f)), (float)(this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f)), LetterDrawable.paint);
                        Theme.chat_inlineResultAudio.setBounds(n7, n8, intrinsicWidth2 + n7, intrinsicHeight2 + n8);
                        Theme.chat_inlineResultAudio.draw(canvas);
                    }
                    else {
                        final TLRPC.BotInlineResult inlineResult3 = this.inlineResult;
                        if (inlineResult3 != null && (inlineResult3.type.equals("venue") || this.inlineResult.type.equals("geo"))) {
                            final int intrinsicWidth3 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                            final int intrinsicHeight3 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                            final int n9 = this.linkImageView.getImageX() + (AndroidUtilities.dp(52.0f) - intrinsicWidth3) / 2;
                            final int n10 = this.linkImageView.getImageY() + (AndroidUtilities.dp(52.0f) - intrinsicHeight3) / 2;
                            canvas.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f)), (float)(this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f)), LetterDrawable.paint);
                            Theme.chat_inlineResultLocation.setBounds(n9, n10, intrinsicWidth3 + n9, intrinsicHeight3 + n10);
                            Theme.chat_inlineResultLocation.draw(canvas);
                        }
                        else {
                            this.letterDrawable.draw(canvas);
                        }
                    }
                }
            }
            else {
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
        }
        else {
            final TLRPC.BotInlineResult inlineResult4 = this.inlineResult;
            if (inlineResult4 != null) {
                final TLRPC.BotInlineMessage send_message = inlineResult4.send_message;
                if (send_message instanceof TLRPC.TL_botInlineMessageMediaGeo || send_message instanceof TLRPC.TL_botInlineMessageMediaVenue) {
                    final int intrinsicWidth4 = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                    final int intrinsicHeight4 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                    final int n11 = this.linkImageView.getImageX() + (this.linkImageView.getImageWidth() - intrinsicWidth4) / 2;
                    final int n12 = this.linkImageView.getImageY() + (this.linkImageView.getImageHeight() - intrinsicHeight4) / 2;
                    canvas.drawRect((float)this.linkImageView.getImageX(), (float)this.linkImageView.getImageY(), (float)(this.linkImageView.getImageX() + this.linkImageView.getImageWidth()), (float)(this.linkImageView.getImageY() + this.linkImageView.getImageHeight()), LetterDrawable.paint);
                    Theme.chat_inlineResultLocation.setBounds(n11, n12, intrinsicWidth4 + n11, intrinsicHeight4 + n12);
                    Theme.chat_inlineResultLocation.draw(canvas);
                }
            }
        }
        if (this.drawLinkImageView) {
            final TLRPC.BotInlineResult inlineResult5 = this.inlineResult;
            if (inlineResult5 != null) {
                this.linkImageView.setVisible(PhotoViewer.isShowingImage(inlineResult5) ^ true, false);
            }
            canvas.save();
            if ((this.scaled && this.scale != 0.8f) || (!this.scaled && this.scale != 1.0f)) {
                final long currentTimeMillis = System.currentTimeMillis();
                final long n13 = currentTimeMillis - this.lastUpdateTime;
                this.lastUpdateTime = currentTimeMillis;
                Label_1155: {
                    if (this.scaled) {
                        final float scale = this.scale;
                        if (scale != 0.8f) {
                            this.scale = scale - n13 / 400.0f;
                            if (this.scale < 0.8f) {
                                this.scale = 0.8f;
                            }
                            break Label_1155;
                        }
                    }
                    this.scale += n13 / 400.0f;
                    if (this.scale > 1.0f) {
                        this.scale = 1.0f;
                    }
                }
                this.invalidate();
            }
            final float scale2 = this.scale;
            canvas.scale(scale2, scale2, (float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2));
            this.linkImageView.draw(canvas);
            canvas.restore();
        }
        if (this.mediaWebpage) {
            final int documentAttachType2 = this.documentAttachType;
            if (documentAttachType2 == 7 || documentAttachType2 == 2) {
                this.radialProgress.draw(canvas);
            }
        }
        if (this.needDivider && !this.mediaWebpage) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
            else {
                canvas.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
        }
        if (this.needShadow) {
            Theme.chat_contextResult_shadowUnderSwitchDrawable.setBounds(0, 0, this.getMeasuredWidth(), AndroidUtilities.dp(3.0f));
            Theme.chat_contextResult_shadowUnderSwitchDrawable.draw(canvas);
        }
    }
    
    public void onFailedDownload(final String s, final boolean b) {
        this.updateButtonState(true, b);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        final StringBuilder text = new StringBuilder();
        switch (this.documentAttachType) {
            default: {
                final StaticLayout titleLayout = this.titleLayout;
                if (titleLayout != null && !TextUtils.isEmpty(titleLayout.getText())) {
                    text.append(this.titleLayout.getText());
                }
                final StaticLayout descriptionLayout = this.descriptionLayout;
                if (descriptionLayout != null && !TextUtils.isEmpty(descriptionLayout.getText())) {
                    if (text.length() > 0) {
                        text.append(", ");
                    }
                    text.append(this.descriptionLayout.getText());
                    break;
                }
                break;
            }
            case 8: {
                text.append(LocaleController.getString("AttachLocation", 2131558723));
                break;
            }
            case 7: {
                text.append(LocaleController.getString("AttachPhoto", 2131558727));
                break;
            }
            case 6: {
                text.append(LocaleController.getString("AttachSticker", 2131558730));
                break;
            }
            case 5: {
                text.append(LocaleController.getString("AttachMusic", 2131558726));
                if (this.descriptionLayout != null && this.titleLayout != null) {
                    text.append(", ");
                    text.append(LocaleController.formatString("AccDescrMusicInfo", 2131558447, this.descriptionLayout.getText(), this.titleLayout.getText()));
                    break;
                }
                break;
            }
            case 4: {
                text.append(LocaleController.getString("AttachVideo", 2131558733));
                break;
            }
            case 3: {
                text.append(LocaleController.getString("AttachAudio", 2131558709));
                break;
            }
            case 2: {
                text.append(LocaleController.getString("AttachGif", 2131558716));
                break;
            }
            case 1: {
                text.append(LocaleController.getString("AttachDocument", 2131558714));
                break;
            }
        }
        accessibilityNodeInfo.setText((CharSequence)text);
    }
    
    @SuppressLint({ "DrawAllocation" })
    protected void onMeasure(int n, int n2) {
        final int n3 = 0;
        this.drawLinkImageView = false;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.linkLayout = null;
        this.currentPhotoObject = null;
        this.linkY = AndroidUtilities.dp(27.0f);
        if (this.inlineResult == null && this.documentAttach == null) {
            this.setMeasuredDimension(AndroidUtilities.dp(100.0f), AndroidUtilities.dp(100.0f));
            return;
        }
        final int size = View$MeasureSpec.getSize(n);
        final int n4 = size - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - AndroidUtilities.dp(8.0f);
        final TLRPC.Document documentAttach = this.documentAttach;
        ArrayList<TLRPC.PhotoSize> list = null;
        Label_0117: {
            if (documentAttach != null) {
                list = new ArrayList<TLRPC.PhotoSize>(documentAttach.thumbs);
            }
            else {
                final TLRPC.BotInlineResult inlineResult = this.inlineResult;
                if (inlineResult != null) {
                    final TLRPC.Photo photo = inlineResult.photo;
                    if (photo != null) {
                        list = new ArrayList<TLRPC.PhotoSize>(photo.sizes);
                        break Label_0117;
                    }
                }
                list = null;
            }
        }
        if (!this.mediaWebpage) {
            final TLRPC.BotInlineResult inlineResult2 = this.inlineResult;
            if (inlineResult2 != null) {
                final String title = inlineResult2.title;
                if (title != null) {
                    try {
                        this.titleLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(this.inlineResult.title.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), Theme.chat_contextResult_titleTextPaint, (float)Math.min((int)Math.ceil(Theme.chat_contextResult_titleTextPaint.measureText(title)), n4), TextUtils$TruncateAt.END), Theme.chat_contextResult_titleTextPaint, n4 + AndroidUtilities.dp(4.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    this.letterDrawable.setTitle(this.inlineResult.title);
                }
                CharSequence charSequence = this.inlineResult.description;
                Label_0431: {
                    if (charSequence != null) {
                        try {
                            charSequence = Emoji.replaceEmoji(charSequence, Theme.chat_contextResult_descriptionTextPaint.getFontMetricsInt(), AndroidUtilities.dp(13.0f), false);
                            final TextPaint chat_contextResult_descriptionTextPaint = Theme.chat_contextResult_descriptionTextPaint;
                            try {
                                this.descriptionLayout = ChatMessageCell.generateStaticLayout(charSequence, chat_contextResult_descriptionTextPaint, n4, n4, 0, 3);
                                if (this.descriptionLayout.getLineCount() > 0) {
                                    this.linkY = this.descriptionY + this.descriptionLayout.getLineBottom(this.descriptionLayout.getLineCount() - 1) + AndroidUtilities.dp(1.0f);
                                }
                                break Label_0431;
                            }
                            catch (Exception charSequence) {}
                        }
                        catch (Exception ex2) {}
                        FileLog.e((Throwable)charSequence);
                    }
                }
                Object url = this.inlineResult.url;
                if (url != null) {
                    try {
                        final CharSequence ellipsize = TextUtils.ellipsize((CharSequence)this.inlineResult.url.replace('\n', ' '), Theme.chat_contextResult_descriptionTextPaint, (float)Math.min((int)Math.ceil(Theme.chat_contextResult_descriptionTextPaint.measureText((String)url)), n4), TextUtils$TruncateAt.MIDDLE);
                        url = new(android.text.StaticLayout.class);
                        final TextPaint chat_contextResult_descriptionTextPaint2 = Theme.chat_contextResult_descriptionTextPaint;
                        final Layout$Alignment align_NORMAL = Layout$Alignment.ALIGN_NORMAL;
                        try {
                            new StaticLayout(ellipsize, chat_contextResult_descriptionTextPaint2, n4, align_NORMAL, 1.0f, 0.0f, false);
                            this.linkLayout = (StaticLayout)url;
                        }
                        catch (Exception url) {}
                    }
                    catch (Exception ex3) {}
                    FileLog.e((Throwable)url);
                }
            }
        }
        final TLRPC.Document documentAttach2 = this.documentAttach;
        String s = null;
        TLRPC.PhotoSize photoSize = null;
        Label_0724: {
            TLRPC.PhotoSize closestPhotoSizeWithSize = null;
            Label_0717: {
                if (documentAttach2 != null) {
                    if (MessageObject.isGifDocument(documentAttach2)) {
                        this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                    }
                    else {
                        if (MessageObject.isStickerDocument(this.documentAttach)) {
                            this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                            s = "webp";
                            photoSize = null;
                            break Label_0724;
                        }
                        final int documentAttachType = this.documentAttachType;
                        if (documentAttachType != 5 && documentAttachType != 3) {
                            this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.documentAttach.thumbs, 90);
                        }
                    }
                }
                else {
                    final TLRPC.BotInlineResult inlineResult3 = this.inlineResult;
                    if (inlineResult3 != null && inlineResult3.photo != null) {
                        this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(list, AndroidUtilities.getPhotoSize(), true);
                        if ((closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(list, 80)) != this.currentPhotoObject) {
                            break Label_0717;
                        }
                    }
                }
                closestPhotoSizeWithSize = null;
            }
            s = null;
            photoSize = closestPhotoSizeWithSize;
        }
        final TLRPC.BotInlineResult inlineResult4 = this.inlineResult;
        WebFile webFile;
        String s2;
        if (inlineResult4 != null) {
            TLRPC.TL_webDocument tl_webDocument = null;
            Label_0852: {
                if (inlineResult4.content instanceof TLRPC.TL_webDocument) {
                    final String type = inlineResult4.type;
                    if (type != null) {
                        if (type.startsWith("gif")) {
                            tl_webDocument = (TLRPC.TL_webDocument)this.inlineResult.content;
                            this.documentAttachType = 2;
                            break Label_0852;
                        }
                        if (this.inlineResult.type.equals("photo")) {
                            final TLRPC.BotInlineResult inlineResult5 = this.inlineResult;
                            final TLRPC.WebDocument thumb = inlineResult5.thumb;
                            if (thumb instanceof TLRPC.TL_webDocument) {
                                tl_webDocument = (TLRPC.TL_webDocument)thumb;
                                break Label_0852;
                            }
                            tl_webDocument = (TLRPC.TL_webDocument)inlineResult5.content;
                            break Label_0852;
                        }
                    }
                }
                tl_webDocument = null;
            }
            TLRPC.TL_webDocument tl_webDocument2 = tl_webDocument;
            if (tl_webDocument == null) {
                final TLRPC.WebDocument thumb2 = this.inlineResult.thumb;
                tl_webDocument2 = tl_webDocument;
                if (thumb2 instanceof TLRPC.TL_webDocument) {
                    tl_webDocument2 = (TLRPC.TL_webDocument)thumb2;
                }
            }
            String formapMapUrl = null;
            Label_1039: {
                Label_1036: {
                    if (tl_webDocument2 == null && this.currentPhotoObject == null && photoSize == null) {
                        final TLRPC.BotInlineMessage send_message = this.inlineResult.send_message;
                        if (send_message instanceof TLRPC.TL_botInlineMessageMediaVenue || send_message instanceof TLRPC.TL_botInlineMessageMediaGeo) {
                            final TLRPC.GeoPoint geo = this.inlineResult.send_message.geo;
                            final double lat = geo.lat;
                            final double long1 = geo._long;
                            if (MessagesController.getInstance(this.currentAccount).mapProvider == 2) {
                                webFile = WebFile.createWithGeoPoint(this.inlineResult.send_message.geo, 72, 72, 15, Math.min(2, (int)Math.ceil(AndroidUtilities.density)));
                                formapMapUrl = null;
                                break Label_1039;
                            }
                            formapMapUrl = AndroidUtilities.formapMapUrl(this.currentAccount, lat, long1, 72, 72, true, 15);
                            break Label_1036;
                        }
                    }
                    formapMapUrl = null;
                }
                webFile = null;
            }
            s2 = formapMapUrl;
            if (tl_webDocument2 != null) {
                webFile = WebFile.createWithWebDocument(tl_webDocument2);
                s2 = formapMapUrl;
            }
        }
        else {
            s2 = null;
            webFile = null;
        }
        int n5 = 0;
        int n6 = 0;
        Label_1158: {
            if (this.documentAttach != null) {
                for (int i = 0; i < this.documentAttach.attributes.size(); ++i) {
                    final TLRPC.DocumentAttribute documentAttribute = this.documentAttach.attributes.get(i);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize || documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                        n5 = documentAttribute.w;
                        n6 = documentAttribute.h;
                        break Label_1158;
                    }
                }
            }
            n5 = 0;
            n6 = 0;
        }
        if (n5 == 0 || n6 == 0) {
            if (this.currentPhotoObject != null) {
                if (photoSize != null) {
                    photoSize.size = -1;
                }
                final TLRPC.PhotoSize currentPhotoObject = this.currentPhotoObject;
                n5 = currentPhotoObject.w;
                n6 = currentPhotoObject.h;
            }
            else {
                final TLRPC.BotInlineResult inlineResult6 = this.inlineResult;
                if (inlineResult6 != null) {
                    final int[] inlineResultWidthAndHeight = MessageObject.getInlineResultWidthAndHeight(inlineResult6);
                    n5 = inlineResultWidthAndHeight[0];
                    n6 = inlineResultWidthAndHeight[1];
                }
            }
        }
        int dp;
        if (n5 == 0 || (dp = n6) == 0) {
            n5 = (dp = AndroidUtilities.dp(80.0f));
        }
        if (this.documentAttach != null || this.currentPhotoObject != null || webFile != null || s2 != null) {
            String s3;
            String format;
            if (this.mediaWebpage) {
                final int n7 = (int)(n5 / (dp / (float)AndroidUtilities.dp(80.0f)));
                if (this.documentAttachType == 2) {
                    format = (s3 = String.format(Locale.US, "%d_%d_b", (int)(n7 / AndroidUtilities.density), 80));
                }
                else {
                    format = String.format(Locale.US, "%d_%d", (int)(n7 / AndroidUtilities.density), 80);
                    final StringBuilder sb = new StringBuilder();
                    sb.append(format);
                    sb.append("_b");
                    s3 = sb.toString();
                }
            }
            else {
                s3 = "52_52_b";
                format = "52_52";
            }
            this.linkImageView.setAspectFit(this.documentAttachType == 6);
            if (this.documentAttachType == 2) {
                final TLRPC.Document documentAttach3 = this.documentAttach;
                if (documentAttach3 != null) {
                    this.linkImageView.setImage(ImageLocation.getForDocument(documentAttach3), null, ImageLocation.getForDocument(this.currentPhotoObject, this.documentAttach), format, this.documentAttach.size, s, this.parentObject, 0);
                }
                else if (webFile != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFile), null, ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), format, -1, s, this.parentObject, 1);
                }
                else {
                    this.linkImageView.setImage(ImageLocation.getForPath(s2), null, ImageLocation.getForPhoto(this.currentPhotoObject, this.photoAttach), format, -1, s, this.parentObject, 1);
                }
            }
            else {
                final TLRPC.PhotoSize currentPhotoObject2 = this.currentPhotoObject;
                if (currentPhotoObject2 != null) {
                    final TLRPC.Document documentAttach4 = this.documentAttach;
                    if (documentAttach4 != null) {
                        this.linkImageView.setImage(ImageLocation.getForDocument(currentPhotoObject2, documentAttach4), format, ImageLocation.getForPhoto(photoSize, this.photoAttach), s3, this.currentPhotoObject.size, s, this.parentObject, 0);
                    }
                    else {
                        this.linkImageView.setImage(ImageLocation.getForPhoto(currentPhotoObject2, this.photoAttach), format, ImageLocation.getForPhoto(photoSize, this.photoAttach), s3, this.currentPhotoObject.size, s, this.parentObject, 0);
                    }
                }
                else if (webFile != null) {
                    this.linkImageView.setImage(ImageLocation.getForWebFile(webFile), format, ImageLocation.getForPhoto(photoSize, this.photoAttach), s3, -1, s, this.parentObject, 1);
                }
                else {
                    this.linkImageView.setImage(ImageLocation.getForPath(s2), format, ImageLocation.getForPhoto(photoSize, this.photoAttach), s3, -1, s, this.parentObject, 1);
                }
            }
            this.drawLinkImageView = true;
        }
        if (this.mediaWebpage) {
            n2 = View$MeasureSpec.getSize(n2);
            if ((n = n2) == 0) {
                n = AndroidUtilities.dp(100.0f);
            }
            this.setMeasuredDimension(size, n);
            n2 = (size - AndroidUtilities.dp(24.0f)) / 2;
            final int n8 = (n - AndroidUtilities.dp(24.0f)) / 2;
            this.radialProgress.setProgressRect(n2, n8, AndroidUtilities.dp(24.0f) + n2, AndroidUtilities.dp(24.0f) + n8);
            this.radialProgress.setCircleRadius(AndroidUtilities.dp(12.0f));
            this.linkImageView.setImageCoords(0, 0, size, n);
        }
        else {
            final StaticLayout titleLayout = this.titleLayout;
            int n9 = n3;
            if (titleLayout != null) {
                n9 = n3;
                if (titleLayout.getLineCount() != 0) {
                    final StaticLayout titleLayout2 = this.titleLayout;
                    n9 = 0 + titleLayout2.getLineBottom(titleLayout2.getLineCount() - 1);
                }
            }
            final StaticLayout descriptionLayout = this.descriptionLayout;
            n2 = n9;
            if (descriptionLayout != null) {
                n2 = n9;
                if (descriptionLayout.getLineCount() != 0) {
                    final StaticLayout descriptionLayout2 = this.descriptionLayout;
                    n2 = n9 + descriptionLayout2.getLineBottom(descriptionLayout2.getLineCount() - 1);
                }
            }
            final StaticLayout linkLayout = this.linkLayout;
            int b = n2;
            if (linkLayout != null) {
                b = n2;
                if (linkLayout.getLineCount() > 0) {
                    final StaticLayout linkLayout2 = this.linkLayout;
                    b = n2 + linkLayout2.getLineBottom(linkLayout2.getLineCount() - 1);
                }
            }
            n2 = Math.max(AndroidUtilities.dp(52.0f), b);
            this.setMeasuredDimension(View$MeasureSpec.getSize(n), Math.max(AndroidUtilities.dp(68.0f), n2 + AndroidUtilities.dp(16.0f)) + (this.needDivider ? 1 : 0));
            n2 = AndroidUtilities.dp(52.0f);
            if (LocaleController.isRTL) {
                n = View$MeasureSpec.getSize(n) - AndroidUtilities.dp(8.0f) - n2;
            }
            else {
                n = AndroidUtilities.dp(8.0f);
            }
            this.letterDrawable.setBounds(n, AndroidUtilities.dp(8.0f), n + n2, AndroidUtilities.dp(60.0f));
            this.linkImageView.setImageCoords(n, AndroidUtilities.dp(8.0f), n2, n2);
            n2 = this.documentAttachType;
            if (n2 == 3 || n2 == 5) {
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(24.0f));
                this.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + n, AndroidUtilities.dp(12.0f), n + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(56.0f));
            }
        }
    }
    
    public void onProgressDownload(final String s, final float n) {
        this.radialProgress.setProgress(n, true);
        final int documentAttachType = this.documentAttachType;
        if (documentAttachType != 3 && documentAttachType != 5) {
            if (this.buttonState != 1) {
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
        if (!this.mediaWebpage && this.delegate != null && this.inlineResult != null) {
            final int n = (int)motionEvent.getX();
            final int n2 = (int)motionEvent.getY();
            AndroidUtilities.dp(48.0f);
            final int documentAttachType = this.documentAttachType;
            int n3 = 1;
            Label_0362: {
                if (documentAttachType != 3 && documentAttachType != 5) {
                    final TLRPC.BotInlineResult inlineResult = this.inlineResult;
                    if (inlineResult != null) {
                        final TLRPC.WebDocument content = inlineResult.content;
                        if (content != null && !TextUtils.isEmpty((CharSequence)content.url)) {
                            if (motionEvent.getAction() == 0) {
                                if (this.letterDrawable.getBounds().contains(n, n2)) {
                                    this.buttonPressed = true;
                                    break Label_0362;
                                }
                            }
                            else if (this.buttonPressed) {
                                if (motionEvent.getAction() == 1) {
                                    this.buttonPressed = false;
                                    this.playSoundEffect(0);
                                    this.delegate.didPressedImage(this);
                                }
                                else if (motionEvent.getAction() == 3) {
                                    this.buttonPressed = false;
                                }
                                else if (motionEvent.getAction() == 2 && !this.letterDrawable.getBounds().contains(n, n2)) {
                                    this.buttonPressed = false;
                                }
                            }
                        }
                    }
                }
                else {
                    final boolean contains = this.letterDrawable.getBounds().contains(n, n2);
                    if (motionEvent.getAction() == 0) {
                        if (contains) {
                            this.buttonPressed = true;
                            this.radialProgress.setPressed(this.buttonPressed, false);
                            this.invalidate();
                            break Label_0362;
                        }
                    }
                    else if (this.buttonPressed) {
                        if (motionEvent.getAction() == 1) {
                            this.buttonPressed = false;
                            this.playSoundEffect(0);
                            this.didPressedButton();
                            this.invalidate();
                        }
                        else if (motionEvent.getAction() == 3) {
                            this.buttonPressed = false;
                            this.invalidate();
                        }
                        else if (motionEvent.getAction() == 2 && !contains) {
                            this.buttonPressed = false;
                            this.invalidate();
                        }
                        this.radialProgress.setPressed(this.buttonPressed, false);
                    }
                }
                n3 = 0;
            }
            boolean onTouchEvent = n3 != 0;
            if (n3 == 0) {
                onTouchEvent = super.onTouchEvent(motionEvent);
            }
            return onTouchEvent;
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void setCanPreviewGif(final boolean canPreviewGif) {
        this.canPreviewGif = canPreviewGif;
    }
    
    public void setDelegate(final ContextLinkCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setGif(final TLRPC.Document document, final boolean needDivider) {
        this.needDivider = needDivider;
        this.needShadow = false;
        this.inlineResult = null;
        final StringBuilder sb = new StringBuilder();
        sb.append("gif");
        sb.append(document);
        this.parentObject = sb.toString();
        this.documentAttach = document;
        this.photoAttach = null;
        this.mediaWebpage = true;
        this.setAttachType();
        this.requestLayout();
        this.updateButtonState(false, false);
    }
    
    public void setLink(TLRPC.BotInlineResult inlineResult, final boolean mediaWebpage, final boolean needDivider, final boolean needShadow) {
        this.needDivider = needDivider;
        this.needShadow = needShadow;
        this.inlineResult = inlineResult;
        this.parentObject = inlineResult;
        inlineResult = this.inlineResult;
        if (inlineResult != null) {
            this.documentAttach = inlineResult.document;
            this.photoAttach = inlineResult.photo;
        }
        else {
            this.documentAttach = null;
            this.photoAttach = null;
        }
        this.mediaWebpage = mediaWebpage;
        this.setAttachType();
        this.requestLayout();
        this.updateButtonState(false, false);
    }
    
    public void setScaled(final boolean scaled) {
        this.scaled = scaled;
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public boolean showingBitmap() {
        return this.linkImageView.getBitmap() != null;
    }
    
    public void updateButtonState(final boolean b, final boolean b2) {
        final int documentAttachType = this.documentAttachType;
        String child = null;
        File file = null;
        Label_0502: {
            if (documentAttachType != 5 && documentAttachType != 3) {
                if (this.mediaWebpage) {
                    final TLRPC.BotInlineResult inlineResult = this.inlineResult;
                    if (inlineResult != null) {
                        final TLRPC.Document document = inlineResult.document;
                        if (document instanceof TLRPC.TL_document) {
                            child = FileLoader.getAttachFileName(document);
                            file = FileLoader.getPathToAttach(this.inlineResult.document);
                            break Label_0502;
                        }
                        final TLRPC.Photo photo = inlineResult.photo;
                        if (photo instanceof TLRPC.TL_photo) {
                            this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize(), true);
                            child = FileLoader.getAttachFileName(this.currentPhotoObject);
                            file = FileLoader.getPathToAttach(this.currentPhotoObject);
                            break Label_0502;
                        }
                        if (inlineResult.content instanceof TLRPC.TL_webDocument) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(Utilities.MD5(this.inlineResult.content.url));
                            sb.append(".");
                            sb.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, "jpg"));
                            child = sb.toString();
                            file = new File(FileLoader.getDirectory(4), child);
                            break Label_0502;
                        }
                        if (inlineResult.thumb instanceof TLRPC.TL_webDocument) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append(Utilities.MD5(this.inlineResult.thumb.url));
                            sb2.append(".");
                            sb2.append(ImageLoader.getHttpUrlExtension(this.inlineResult.thumb.url, "jpg"));
                            child = sb2.toString();
                            file = new File(FileLoader.getDirectory(4), child);
                            break Label_0502;
                        }
                    }
                    else {
                        final TLRPC.Document documentAttach = this.documentAttach;
                        if (documentAttach != null) {
                            child = FileLoader.getAttachFileName(documentAttach);
                            file = FileLoader.getPathToAttach(this.documentAttach);
                            break Label_0502;
                        }
                    }
                }
            }
            else {
                final TLRPC.Document documentAttach2 = this.documentAttach;
                if (documentAttach2 != null) {
                    child = FileLoader.getAttachFileName(documentAttach2);
                    file = FileLoader.getPathToAttach(this.documentAttach);
                    break Label_0502;
                }
                if (this.inlineResult.content instanceof TLRPC.TL_webDocument) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(Utilities.MD5(this.inlineResult.content.url));
                    sb3.append(".");
                    final String url = this.inlineResult.content.url;
                    String s;
                    if (this.documentAttachType == 5) {
                        s = "mp3";
                    }
                    else {
                        s = "ogg";
                    }
                    sb3.append(ImageLoader.getHttpUrlExtension(url, s));
                    child = sb3.toString();
                    file = new File(FileLoader.getDirectory(4), child);
                    break Label_0502;
                }
            }
            file = null;
        }
        if (TextUtils.isEmpty((CharSequence)child)) {
            return;
        }
        if (!file.exists()) {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(child, (DownloadController.FileDownloadProgressListener)this);
            final int documentAttachType2 = this.documentAttachType;
            float floatValue = 0.0f;
            if (documentAttachType2 != 5 && documentAttachType2 != 3) {
                this.buttonState = 1;
                final Float fileProgress = ImageLoader.getInstance().getFileProgress(child);
                if (fileProgress != null) {
                    floatValue = fileProgress;
                }
                this.radialProgress.setProgress(floatValue, false);
                this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
            }
            else {
                boolean b3;
                if (this.documentAttach != null) {
                    b3 = FileLoader.getInstance(this.currentAccount).isLoadingFile(child);
                }
                else {
                    b3 = ImageLoader.getInstance().isLoadingHttpFile(child);
                }
                if (!b3) {
                    this.buttonState = 2;
                    this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
                }
                else {
                    this.buttonState = 4;
                    final Float fileProgress2 = ImageLoader.getInstance().getFileProgress(child);
                    if (fileProgress2 != null) {
                        this.radialProgress.setProgress(fileProgress2, b2);
                    }
                    else {
                        this.radialProgress.setProgress(0.0f, b2);
                    }
                    this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
                }
            }
            this.invalidate();
        }
        else {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
            final int documentAttachType3 = this.documentAttachType;
            if (documentAttachType3 != 5 && documentAttachType3 != 3) {
                this.buttonState = -1;
            }
            else {
                final boolean playingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (playingMessage && (!playingMessage || !MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 1;
                }
                else {
                    this.buttonState = 0;
                }
                this.radialProgress.setProgress(1.0f, b2);
            }
            this.radialProgress.setIcon(this.getIconForCurrentState(), b, b2);
            this.invalidate();
        }
    }
    
    public interface ContextLinkCellDelegate
    {
        void didPressedImage(final ContextLinkCell p0);
    }
}
