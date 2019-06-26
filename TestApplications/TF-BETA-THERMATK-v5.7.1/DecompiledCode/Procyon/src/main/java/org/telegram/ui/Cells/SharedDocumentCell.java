// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ImageLoader;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.drawable.Drawable;
import java.util.Date;
import android.graphics.Bitmap;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Canvas;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LineProgressView;
import android.widget.ImageView;
import org.telegram.messenger.MessageObject;
import android.widget.TextView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.messenger.DownloadController;
import android.widget.FrameLayout;

public class SharedDocumentCell extends FrameLayout implements FileDownloadProgressListener
{
    private int TAG;
    private CheckBox2 checkBox;
    private int currentAccount;
    private TextView dateTextView;
    private TextView extTextView;
    private boolean loaded;
    private boolean loading;
    private MessageObject message;
    private TextView nameTextView;
    private boolean needDivider;
    private ImageView placeholderImageView;
    private LineProgressView progressView;
    private ImageView statusImageView;
    private BackupImageView thumbImageView;
    
    public SharedDocumentCell(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.placeholderImageView = new ImageView(context);
        final ImageView placeholderImageView = this.placeholderImageView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 0.0f;
        }
        else {
            n3 = 12.0f;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 12.0f;
        }
        else {
            n4 = 0.0f;
        }
        this.addView((View)placeholderImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n2 | 0x30, n3, 8.0f, n4, 0.0f));
        (this.extTextView = new TextView(context)).setTextColor(Theme.getColor("files_iconText"));
        this.extTextView.setTextSize(1, 14.0f);
        this.extTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.extTextView.setLines(1);
        this.extTextView.setMaxLines(1);
        this.extTextView.setSingleLine(true);
        this.extTextView.setGravity(17);
        this.extTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.extTextView.setImportantForAccessibility(2);
        final TextView extTextView = this.extTextView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 0.0f;
        }
        else {
            n6 = 16.0f;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 16.0f;
        }
        else {
            n7 = 0.0f;
        }
        this.addView((View)extTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(32, -2.0f, n5 | 0x30, n6, 22.0f, n7, 0.0f));
        (this.thumbImageView = new BackupImageView(context) {
            @Override
            protected void onDraw(final Canvas canvas) {
                final boolean hasBitmapImage = SharedDocumentCell.this.thumbImageView.getImageReceiver().hasBitmapImage();
                float n = 1.0f;
                if (hasBitmapImage) {
                    n = 1.0f - SharedDocumentCell.this.thumbImageView.getImageReceiver().getCurrentAlpha();
                }
                SharedDocumentCell.this.extTextView.setAlpha(n);
                SharedDocumentCell.this.placeholderImageView.setAlpha(n);
                super.onDraw(canvas);
            }
        }).setRoundRadius(AndroidUtilities.dp(4.0f));
        final BackupImageView thumbImageView = this.thumbImageView;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        float n9;
        if (LocaleController.isRTL) {
            n9 = 0.0f;
        }
        else {
            n9 = 12.0f;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 12.0f;
        }
        else {
            n10 = 0.0f;
        }
        this.addView((View)thumbImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n8 | 0x30, n9, 8.0f, n10, 0.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(1, 16.0f);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setMaxLines(2);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView nameTextView = this.nameTextView;
        int n11;
        if (LocaleController.isRTL) {
            n11 = 5;
        }
        else {
            n11 = 3;
        }
        nameTextView.setGravity(n11 | 0x10);
        final TextView nameTextView2 = this.nameTextView;
        int n12;
        if (LocaleController.isRTL) {
            n12 = 5;
        }
        else {
            n12 = 3;
        }
        float n13;
        if (LocaleController.isRTL) {
            n13 = 8.0f;
        }
        else {
            n13 = 72.0f;
        }
        float n14;
        if (LocaleController.isRTL) {
            n14 = 72.0f;
        }
        else {
            n14 = 8.0f;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n12 | 0x30, n13, 5.0f, n14, 0.0f));
        (this.statusImageView = new ImageView(context)).setVisibility(4);
        this.statusImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("sharedMedia_startStopLoadIcon"), PorterDuff$Mode.MULTIPLY));
        final ImageView statusImageView = this.statusImageView;
        int n15;
        if (LocaleController.isRTL) {
            n15 = 5;
        }
        else {
            n15 = 3;
        }
        float n16;
        if (LocaleController.isRTL) {
            n16 = 8.0f;
        }
        else {
            n16 = 72.0f;
        }
        float n17;
        if (LocaleController.isRTL) {
            n17 = 72.0f;
        }
        else {
            n17 = 8.0f;
        }
        this.addView((View)statusImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n15 | 0x30, n16, 35.0f, n17, 0.0f));
        (this.dateTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.dateTextView.setTextSize(1, 14.0f);
        this.dateTextView.setLines(1);
        this.dateTextView.setMaxLines(1);
        this.dateTextView.setSingleLine(true);
        this.dateTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView dateTextView = this.dateTextView;
        int n18;
        if (LocaleController.isRTL) {
            n18 = 5;
        }
        else {
            n18 = 3;
        }
        dateTextView.setGravity(n18 | 0x10);
        final TextView dateTextView2 = this.dateTextView;
        int n19;
        if (LocaleController.isRTL) {
            n19 = 5;
        }
        else {
            n19 = 3;
        }
        float n20;
        if (LocaleController.isRTL) {
            n20 = 8.0f;
        }
        else {
            n20 = 72.0f;
        }
        float n21;
        if (LocaleController.isRTL) {
            n21 = 72.0f;
        }
        else {
            n21 = 8.0f;
        }
        this.addView((View)dateTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n19 | 0x30, n20, 30.0f, n21, 0.0f));
        (this.progressView = new LineProgressView(context)).setProgressColor(Theme.getColor("sharedMedia_startStopLoadIcon"));
        final LineProgressView progressView = this.progressView;
        int n22;
        if (LocaleController.isRTL) {
            n22 = 5;
        }
        else {
            n22 = 3;
        }
        float n23;
        if (LocaleController.isRTL) {
            n23 = 0.0f;
        }
        else {
            n23 = 72.0f;
        }
        float n24;
        if (LocaleController.isRTL) {
            n24 = 72.0f;
        }
        else {
            n24 = 0.0f;
        }
        this.addView((View)progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 2.0f, n22 | 0x30, n23, 54.0f, n24, 0.0f));
        (this.checkBox = new CheckBox2(context)).setVisibility(4);
        this.checkBox.setColor(null, "windowBackgroundWhite", "checkboxCheck");
        this.checkBox.setSize(21);
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(2);
        final CheckBox2 checkBox = this.checkBox;
        int n25;
        if (LocaleController.isRTL) {
            n25 = n;
        }
        else {
            n25 = 3;
        }
        float n26;
        if (LocaleController.isRTL) {
            n26 = 0.0f;
        }
        else {
            n26 = 33.0f;
        }
        float n27;
        if (LocaleController.isRTL) {
            n27 = 33.0f;
        }
        else {
            n27 = 0.0f;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, n25 | 0x30, n26, 28.0f, n27, 0.0f));
    }
    
    public BackupImageView getImageView() {
        return this.thumbImageView;
    }
    
    public MessageObject getMessage() {
        return this.message;
    }
    
    public int getObserverTag() {
        return this.TAG;
    }
    
    public boolean isLoaded() {
        return this.loaded;
    }
    
    public boolean isLoading() {
        return this.loading;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.progressView.getVisibility() == 0) {
            this.updateFileExistIcon();
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine((float)AndroidUtilities.dp(72.0f), (float)(this.getHeight() - 1), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - 1), Theme.dividerPaint);
        }
    }
    
    public void onFailedDownload(final String s, final boolean b) {
        this.updateFileExistIcon();
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setChecked(true);
        }
    }
    
    protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.nameTextView.getLineCount() > 1) {
            n = this.nameTextView.getMeasuredHeight() - AndroidUtilities.dp(22.0f);
            final TextView dateTextView = this.dateTextView;
            dateTextView.layout(dateTextView.getLeft(), this.dateTextView.getTop() + n, this.dateTextView.getRight(), this.dateTextView.getBottom() + n);
            final ImageView statusImageView = this.statusImageView;
            statusImageView.layout(statusImageView.getLeft(), this.statusImageView.getTop() + n, this.statusImageView.getRight(), n + this.statusImageView.getBottom());
            final LineProgressView progressView = this.progressView;
            progressView.layout(progressView.getLeft(), this.getMeasuredHeight() - this.progressView.getMeasuredHeight() - (this.needDivider ? 1 : 0), this.progressView.getRight(), this.getMeasuredHeight() - (this.needDivider ? 1 : 0));
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), 1073741824));
        this.setMeasuredDimension(this.getMeasuredWidth(), AndroidUtilities.dp(34.0f) + this.nameTextView.getMeasuredHeight() + (this.needDivider ? 1 : 0));
    }
    
    public void onProgressDownload(final String s, final float n) {
        if (this.progressView.getVisibility() != 0) {
            this.updateFileExistIcon();
        }
        this.progressView.setProgress(n, true);
    }
    
    public void onProgressUpload(final String s, final float n, final boolean b) {
    }
    
    public void onSuccessDownload(final String s) {
        this.progressView.setProgress(1.0f, true);
        this.updateFileExistIcon();
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(b, b2);
    }
    
    public void setDocument(final MessageObject message, final boolean needDivider) {
        this.needDivider = needDivider;
        this.message = message;
        this.loaded = false;
        this.loading = false;
        final TLRPC.Document document = message.getDocument();
        final String s = "";
        if (message != null && document != null) {
            String s3;
            if (message.isMusic()) {
                String s2 = null;
                int index = 0;
                while (true) {
                    s3 = s2;
                    if (index >= document.attributes.size()) {
                        break;
                    }
                    final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(index);
                    String string = s2;
                    Label_0186: {
                        if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                            final String performer = documentAttribute.performer;
                            if (performer == null || performer.length() == 0) {
                                final String title = documentAttribute.title;
                                string = s2;
                                if (title == null) {
                                    break Label_0186;
                                }
                                string = s2;
                                if (title.length() == 0) {
                                    break Label_0186;
                                }
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append(message.getMusicAuthor());
                            sb.append(" - ");
                            sb.append(message.getMusicTitle());
                            string = sb.toString();
                        }
                    }
                    ++index;
                    s2 = string;
                }
            }
            else {
                s3 = null;
            }
            final String documentFileName = FileLoader.getDocumentFileName(document);
            String text;
            if ((text = s3) == null) {
                text = documentFileName;
            }
            this.nameTextView.setText((CharSequence)text);
            this.placeholderImageView.setVisibility(0);
            this.extTextView.setVisibility(0);
            this.placeholderImageView.setImageResource(AndroidUtilities.getThumbForNameOrMime(documentFileName, document.mime_type, false));
            final TextView extTextView = this.extTextView;
            final int lastIndex = documentFileName.lastIndexOf(46);
            String lowerCase;
            if (lastIndex == -1) {
                lowerCase = s;
            }
            else {
                lowerCase = documentFileName.substring(lastIndex + 1).toLowerCase();
            }
            extTextView.setText((CharSequence)lowerCase);
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 320);
            final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 40);
            TLRPC.PhotoSize photoSize = closestPhotoSizeWithSize;
            if (closestPhotoSizeWithSize2 == closestPhotoSizeWithSize) {
                photoSize = null;
            }
            if (!(closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoSizeEmpty) && closestPhotoSizeWithSize2 != null) {
                this.thumbImageView.getImageReceiver().setNeedsQualityThumb(photoSize == null);
                this.thumbImageView.getImageReceiver().setShouldGenerateQualityThumb(photoSize == null);
                this.thumbImageView.setVisibility(0);
                this.thumbImageView.setImage(ImageLocation.getForDocument(photoSize, document), "40_40", ImageLocation.getForDocument(closestPhotoSizeWithSize2, document), "40_40_b", null, 0, 1, message);
            }
            else {
                this.thumbImageView.setVisibility(4);
                this.thumbImageView.setImageBitmap(null);
                this.extTextView.setAlpha(1.0f);
                this.placeholderImageView.setAlpha(1.0f);
            }
            final long n = message.messageOwner.date * 1000L;
            this.dateTextView.setText((CharSequence)String.format("%s, %s", AndroidUtilities.formatFileSize(document.size), LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(new Date(n)), LocaleController.getInstance().formatterDay.format(new Date(n)))));
        }
        else {
            this.nameTextView.setText((CharSequence)"");
            this.extTextView.setText((CharSequence)"");
            this.dateTextView.setText((CharSequence)"");
            this.placeholderImageView.setVisibility(0);
            this.extTextView.setVisibility(0);
            this.extTextView.setAlpha(1.0f);
            this.placeholderImageView.setAlpha(1.0f);
            this.thumbImageView.setVisibility(4);
            this.thumbImageView.setImageBitmap(null);
        }
        this.setWillNotDraw(this.needDivider ^ true);
        this.progressView.setProgress(0.0f, false);
        this.updateFileExistIcon();
    }
    
    public void setTextAndValueAndTypeAndThumb(final String text, final String text2, final String text3, final String s, final int n) {
        this.nameTextView.setText((CharSequence)text);
        this.dateTextView.setText((CharSequence)text2);
        if (text3 != null) {
            this.extTextView.setVisibility(0);
            this.extTextView.setText((CharSequence)text3);
        }
        else {
            this.extTextView.setVisibility(4);
        }
        if (n == 0) {
            this.placeholderImageView.setImageResource(AndroidUtilities.getThumbForNameOrMime(text, text3, false));
            this.placeholderImageView.setVisibility(0);
        }
        else {
            this.placeholderImageView.setVisibility(4);
        }
        if (s == null && n == 0) {
            this.extTextView.setAlpha(1.0f);
            this.placeholderImageView.setAlpha(1.0f);
            this.thumbImageView.setImageBitmap(null);
            this.thumbImageView.setVisibility(4);
        }
        else {
            if (s != null) {
                this.thumbImageView.setImage(s, "40_40", null);
            }
            else {
                final CombinedDrawable circleDrawableWithIcon = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(40.0f), n);
                Theme.setCombinedDrawableColor(circleDrawableWithIcon, Theme.getColor("files_folderIconBackground"), false);
                Theme.setCombinedDrawableColor(circleDrawableWithIcon, Theme.getColor("files_folderIcon"), true);
                this.thumbImageView.setImageDrawable(circleDrawableWithIcon);
            }
            this.thumbImageView.setVisibility(0);
        }
    }
    
    public void updateFileExistIcon() {
        final MessageObject message = this.message;
        if (message != null && message.messageOwner.media != null) {
            this.loaded = false;
            if (!message.attachPathExists && !message.mediaExists) {
                final String attachFileName = FileLoader.getAttachFileName(message.getDocument());
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(attachFileName, this.message, (DownloadController.FileDownloadProgressListener)this);
                this.loading = FileLoader.getInstance(this.currentAccount).isLoadingFile(attachFileName);
                this.statusImageView.setVisibility(0);
                final ImageView statusImageView = this.statusImageView;
                int imageResource;
                if (this.loading) {
                    imageResource = 2131165559;
                }
                else {
                    imageResource = 2131165558;
                }
                statusImageView.setImageResource(imageResource);
                final TextView dateTextView = this.dateTextView;
                int dp;
                if (LocaleController.isRTL) {
                    dp = 0;
                }
                else {
                    dp = AndroidUtilities.dp(14.0f);
                }
                int dp2;
                if (LocaleController.isRTL) {
                    dp2 = AndroidUtilities.dp(14.0f);
                }
                else {
                    dp2 = 0;
                }
                dateTextView.setPadding(dp, 0, dp2, 0);
                if (this.loading) {
                    this.progressView.setVisibility(0);
                    Float n;
                    if ((n = ImageLoader.getInstance().getFileProgress(attachFileName)) == null) {
                        n = 0.0f;
                    }
                    this.progressView.setProgress(n, false);
                }
                else {
                    this.progressView.setVisibility(4);
                }
            }
            else {
                this.statusImageView.setVisibility(4);
                this.progressView.setVisibility(4);
                this.dateTextView.setPadding(0, 0, 0, 0);
                this.loading = false;
                this.loaded = true;
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
            }
        }
        else {
            this.loading = false;
            this.loaded = true;
            this.progressView.setVisibility(4);
            this.progressView.setProgress(0.0f, false);
            this.statusImageView.setVisibility(4);
            this.dateTextView.setPadding(0, 0, 0, 0);
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
        }
    }
}
