// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.ViewConfiguration;
import org.telegram.messenger.browser.Browser;
import android.view.MotionEvent;
import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.Locale;
import org.telegram.messenger.ImageLocation;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.FileLoader;
import android.text.Layout$Alignment;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.FileLog;
import android.net.Uri;
import org.telegram.tgnet.TLRPC;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Path;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.Components.LinkPath;
import org.telegram.messenger.MessageObject;
import java.util.ArrayList;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Components.LetterDrawable;
import android.text.TextPaint;
import android.text.StaticLayout;
import org.telegram.ui.Components.CheckBox2;
import android.widget.FrameLayout;

public class SharedLinkCell extends FrameLayout
{
    private CheckBox2 checkBox;
    private boolean checkingForLongPress;
    private SharedLinkCellDelegate delegate;
    private int description2Y;
    private StaticLayout descriptionLayout;
    private StaticLayout descriptionLayout2;
    private TextPaint descriptionTextPaint;
    private int descriptionY;
    private boolean drawLinkImageView;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private ArrayList<StaticLayout> linkLayout;
    private boolean linkPreviewPressed;
    private int linkY;
    ArrayList<String> links;
    private MessageObject message;
    private boolean needDivider;
    private CheckForLongPress pendingCheckForLongPress;
    private CheckForTap pendingCheckForTap;
    private int pressCount;
    private int pressedLink;
    private StaticLayout titleLayout;
    private TextPaint titleTextPaint;
    private int titleY;
    private LinkPath urlPath;
    
    public SharedLinkCell(final Context context) {
        super(context);
        this.checkingForLongPress = false;
        this.pendingCheckForLongPress = null;
        this.pressCount = 0;
        this.pendingCheckForTap = null;
        this.links = new ArrayList<String>();
        this.linkLayout = new ArrayList<StaticLayout>();
        this.titleY = AndroidUtilities.dp(10.0f);
        this.descriptionY = AndroidUtilities.dp(30.0f);
        this.description2Y = AndroidUtilities.dp(30.0f);
        this.setFocusable(true);
        (this.urlPath = new LinkPath()).setUseRoundRect(true);
        (this.titleTextPaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.descriptionTextPaint = new TextPaint(1);
        this.titleTextPaint.setTextSize((float)AndroidUtilities.dp(14.0f));
        this.descriptionTextPaint.setTextSize((float)AndroidUtilities.dp(14.0f));
        this.setWillNotDraw(false);
        (this.linkImageView = new ImageReceiver((View)this)).setRoundRadius(AndroidUtilities.dp(4.0f));
        this.letterDrawable = new LetterDrawable();
        (this.checkBox = new CheckBox2(context)).setVisibility(4);
        this.checkBox.setColor(null, "windowBackgroundWhite", "checkboxCheck");
        this.checkBox.setSize(21);
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(2);
        final CheckBox2 checkBox = this.checkBox;
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        float n2;
        if (LocaleController.isRTL) {
            n2 = 0.0f;
        }
        else {
            n2 = 44.0f;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 44.0f;
        }
        else {
            n3 = 0.0f;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, n | 0x30, n2, 44.0f, n3, 0.0f));
    }
    
    protected void cancelCheckLongPress() {
        this.checkingForLongPress = false;
        final CheckForLongPress pendingCheckForLongPress = this.pendingCheckForLongPress;
        if (pendingCheckForLongPress != null) {
            this.removeCallbacks((Runnable)pendingCheckForLongPress);
        }
        final CheckForTap pendingCheckForTap = this.pendingCheckForTap;
        if (pendingCheckForTap != null) {
            this.removeCallbacks((Runnable)pendingCheckForTap);
        }
    }
    
    public String getLink(final int index) {
        if (index >= 0 && index < this.links.size()) {
            return this.links.get(index);
        }
        return null;
    }
    
    public MessageObject getMessage() {
        return this.message;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onAttachedToWindow();
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onDetachedFromWindow();
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.titleLayout != null) {
            canvas.save();
            float n;
            if (LocaleController.isRTL) {
                n = 8.0f;
            }
            else {
                n = (float)AndroidUtilities.leftBaseline;
            }
            canvas.translate((float)AndroidUtilities.dp(n), (float)this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            this.descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            canvas.save();
            float n2;
            if (LocaleController.isRTL) {
                n2 = 8.0f;
            }
            else {
                n2 = (float)AndroidUtilities.leftBaseline;
            }
            canvas.translate((float)AndroidUtilities.dp(n2), (float)this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout2 != null) {
            this.descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            canvas.save();
            float n3;
            if (LocaleController.isRTL) {
                n3 = 8.0f;
            }
            else {
                n3 = (float)AndroidUtilities.leftBaseline;
            }
            canvas.translate((float)AndroidUtilities.dp(n3), (float)this.description2Y);
            this.descriptionLayout2.draw(canvas);
            canvas.restore();
        }
        if (!this.linkLayout.isEmpty()) {
            this.descriptionTextPaint.setColor(Theme.getColor("windowBackgroundWhiteLinkText"));
            int i = 0;
            int n4 = 0;
            while (i < this.linkLayout.size()) {
                final StaticLayout staticLayout = this.linkLayout.get(i);
                int n5 = n4;
                if (staticLayout.getLineCount() > 0) {
                    canvas.save();
                    float n6;
                    if (LocaleController.isRTL) {
                        n6 = 8.0f;
                    }
                    else {
                        n6 = (float)AndroidUtilities.leftBaseline;
                    }
                    canvas.translate((float)AndroidUtilities.dp(n6), (float)(this.linkY + n4));
                    if (this.pressedLink == i) {
                        canvas.drawPath((Path)this.urlPath, Theme.linkSelectionPaint);
                    }
                    staticLayout.draw(canvas);
                    canvas.restore();
                    n5 = n4 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
                }
                ++i;
                n4 = n5;
            }
        }
        this.letterDrawable.draw(canvas);
        if (this.drawLinkImageView) {
            this.linkImageView.draw(canvas);
        }
        if (this.needDivider) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
            else {
                canvas.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        final StringBuilder sb = new StringBuilder();
        final StaticLayout titleLayout = this.titleLayout;
        if (titleLayout != null) {
            sb.append(titleLayout.getText());
        }
        if (this.descriptionLayout != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout.getText());
        }
        if (this.descriptionLayout2 != null) {
            sb.append(", ");
            sb.append(this.descriptionLayout2.getText());
        }
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setChecked(true);
            accessibilityNodeInfo.setCheckable(true);
        }
    }
    
    @SuppressLint({ "DrawAllocation" })
    protected void onMeasure(final int n, int n2) {
        final int n3 = 0;
        this.drawLinkImageView = false;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.descriptionLayout2 = null;
        this.linkLayout.clear();
        this.links.clear();
        final int b = View$MeasureSpec.getSize(n) - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - AndroidUtilities.dp(8.0f);
        final MessageObject message = this.message;
        final TLRPC.MessageMedia media = message.messageOwner.media;
        String s = null;
        String description = null;
        CharSequence url = null;
        Label_0196: {
            if (media instanceof TLRPC.TL_messageMediaWebPage) {
                final TLRPC.WebPage webpage = media.webpage;
                if (webpage instanceof TLRPC.TL_webPage) {
                    if (message.photoThumbs == null && webpage.photo != null) {
                        message.generateThumbs(true);
                    }
                    if (webpage.photo != null && this.message.photoThumbs != null) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                    if ((s = webpage.title) == null) {
                        s = webpage.site_name;
                    }
                    description = webpage.description;
                    url = webpage.url;
                    break Label_0196;
                }
            }
            url = null;
            s = (description = (String)url);
            n2 = 0;
        }
        final MessageObject message2 = this.message;
        Serializable title;
        Serializable s2;
        Serializable s3;
        if (message2 != null && !message2.messageOwner.entities.isEmpty()) {
            final StringBuilder sb = null;
            title = s;
            int i = 0;
            s2 = description;
            s3 = sb;
            while (i < this.message.messageOwner.entities.size()) {
                final TLRPC.MessageEntity messageEntity = this.message.messageOwner.entities.get(i);
                Serializable s4 = s3;
                Serializable s5 = title;
                Serializable s6 = s2;
                if (messageEntity.length > 0) {
                    final int offset = messageEntity.offset;
                    s4 = s3;
                    s5 = title;
                    s6 = s2;
                    if (offset >= 0) {
                        if (offset >= this.message.messageOwner.message.length()) {
                            s4 = s3;
                            s5 = title;
                            s6 = s2;
                        }
                        else {
                            if (messageEntity.offset + messageEntity.length > this.message.messageOwner.message.length()) {
                                messageEntity.length = this.message.messageOwner.message.length() - messageEntity.offset;
                            }
                            Serializable s7 = s3;
                            Label_0509: {
                                if (i == 0) {
                                    s7 = s3;
                                    if (url != null) {
                                        if (messageEntity.offset == 0) {
                                            s7 = s3;
                                            if (messageEntity.length == this.message.messageOwner.message.length()) {
                                                break Label_0509;
                                            }
                                        }
                                        if (this.message.messageOwner.entities.size() == 1) {
                                            s7 = s3;
                                            if (s2 == null) {
                                                s7 = this.message.messageOwner.message;
                                            }
                                        }
                                        else {
                                            s7 = this.message.messageOwner.message;
                                        }
                                    }
                                }
                            }
                            Serializable substring = title;
                            Throwable t = (Throwable)s2;
                            Serializable s10 = null;
                            Label_1440: {
                                Throwable t2 = null;
                                try {
                                    Label_1215: {
                                        if (!(messageEntity instanceof TLRPC.TL_messageEntityTextUrl)) {
                                            substring = title;
                                            t = (Throwable)s2;
                                            if (!(messageEntity instanceof TLRPC.TL_messageEntityUrl)) {
                                                substring = title;
                                                t = (Throwable)s2;
                                                Label_0809: {
                                                    if (messageEntity instanceof TLRPC.TL_messageEntityEmail) {
                                                        if (title != null) {
                                                            substring = title;
                                                            t = (Throwable)s2;
                                                            if (((String)title).length() != 0) {
                                                                break Label_0809;
                                                            }
                                                        }
                                                        substring = title;
                                                        t = (Throwable)s2;
                                                        substring = title;
                                                        t = (Throwable)s2;
                                                        final StringBuilder sb2 = new StringBuilder();
                                                        substring = title;
                                                        t = (Throwable)s2;
                                                        sb2.append("mailto:");
                                                        substring = title;
                                                        t = (Throwable)s2;
                                                        sb2.append(this.message.messageOwner.message.substring(messageEntity.offset, messageEntity.offset + messageEntity.length));
                                                        substring = title;
                                                        t = (Throwable)s2;
                                                        final String string = sb2.toString();
                                                        substring = title;
                                                        t = (Throwable)s2;
                                                        s4 = (substring = this.message.messageOwner.message.substring(messageEntity.offset, messageEntity.offset + messageEntity.length));
                                                        t = (Throwable)s2;
                                                        if (messageEntity.offset == 0) {
                                                            final Serializable s8 = string;
                                                            title = s4;
                                                            final Serializable s9 = s2;
                                                            substring = s4;
                                                            t = (Throwable)s2;
                                                            if (messageEntity.length == this.message.messageOwner.message.length()) {
                                                                break Label_1215;
                                                            }
                                                        }
                                                        substring = s4;
                                                        t = (Throwable)s2;
                                                        final Serializable s9 = this.message.messageOwner.message;
                                                        final Serializable s8 = string;
                                                        title = s4;
                                                        break Label_1215;
                                                    }
                                                }
                                                final Serializable s8 = null;
                                                final Serializable s9 = s2;
                                                break Label_1215;
                                            }
                                        }
                                        substring = title;
                                        t = (Throwable)s2;
                                        if (messageEntity instanceof TLRPC.TL_messageEntityUrl) {
                                            substring = title;
                                            t = (Throwable)s2;
                                            s4 = this.message.messageOwner.message.substring(messageEntity.offset, messageEntity.offset + messageEntity.length);
                                        }
                                        else {
                                            substring = title;
                                            t = (Throwable)s2;
                                            s4 = messageEntity.url;
                                        }
                                        if (title != null) {
                                            substring = title;
                                            t = (Throwable)s2;
                                            final int length = ((String)title).length();
                                            final Serializable s8 = s4;
                                            final Serializable s9 = s2;
                                            if (length != 0) {
                                                break Label_1215;
                                            }
                                        }
                                        try {
                                            Serializable host = Uri.parse((String)s4).getHost();
                                            if (host == null) {
                                                host = s4;
                                            }
                                            Serializable string2;
                                            if ((string2 = host) != null) {
                                                t = (Throwable)s2;
                                                final int lastIndex = ((String)host).lastIndexOf(46);
                                                string2 = host;
                                                if (lastIndex >= 0) {
                                                    t = (Throwable)s2;
                                                    final String substring2 = ((String)host).substring(0, lastIndex);
                                                    t = (Throwable)s2;
                                                    final int lastIndex2 = substring2.lastIndexOf(46);
                                                    String substring3 = substring2;
                                                    if (lastIndex2 >= 0) {
                                                        t = (Throwable)s2;
                                                        substring3 = substring2.substring(lastIndex2 + 1);
                                                    }
                                                    t = (Throwable)s2;
                                                    t = (Throwable)s2;
                                                    final StringBuilder sb3 = new StringBuilder();
                                                    t = (Throwable)s2;
                                                    sb3.append(substring3.substring(0, 1).toUpperCase());
                                                    t = (Throwable)s2;
                                                    sb3.append(substring3.substring(1));
                                                    t = (Throwable)s2;
                                                    string2 = sb3.toString();
                                                }
                                            }
                                            t = (Throwable)s2;
                                            if (messageEntity.offset == 0) {
                                                final Serializable s8 = s4;
                                                title = string2;
                                                final Serializable s9 = s2;
                                                t = (Throwable)s2;
                                                if (messageEntity.length == this.message.messageOwner.message.length()) {
                                                    break Label_1215;
                                                }
                                            }
                                            t = (Throwable)s2;
                                            final Serializable s9 = this.message.messageOwner.message;
                                            title = string2;
                                            final Serializable s8 = s4;
                                            s2 = title;
                                            s10 = s9;
                                            if (s8 != null) {
                                                t = (Throwable)s9;
                                                if (((String)s8).toLowerCase().indexOf("http") != 0) {
                                                    t = (Throwable)s9;
                                                    if (((String)s8).toLowerCase().indexOf("mailto") != 0) {
                                                        t = (Throwable)s9;
                                                        s4 = this.links;
                                                        t = (Throwable)s9;
                                                        s2 = new(java.lang.StringBuilder.class);
                                                        t = (Throwable)s9;
                                                        new StringBuilder();
                                                        t = (Throwable)s9;
                                                        ((StringBuilder)s2).append("http://");
                                                        t = (Throwable)s9;
                                                        ((StringBuilder)s2).append((String)s8);
                                                        t = (Throwable)s9;
                                                        ((ArrayList<String>)s4).add(((StringBuilder)s2).toString());
                                                        s2 = title;
                                                        s10 = s9;
                                                        break Label_1440;
                                                    }
                                                }
                                                t = (Throwable)s9;
                                                this.links.add((String)s8);
                                                s2 = title;
                                                s10 = s9;
                                            }
                                            break Label_1440;
                                        }
                                        catch (Exception substring) {
                                            t2 = (Throwable)s4;
                                        }
                                    }
                                }
                                catch (Exception ex) {
                                    s2 = t;
                                    t2 = (Throwable)substring;
                                    substring = ex;
                                }
                                FileLog.e((Throwable)substring);
                                s10 = s2;
                                s2 = t2;
                            }
                            s4 = s7;
                            s6 = s10;
                            s5 = s2;
                        }
                    }
                }
                ++i;
                s3 = s4;
                title = s5;
                s2 = s6;
            }
        }
        else {
            title = s;
            s2 = description;
            s3 = null;
        }
        if (url != null && this.links.isEmpty()) {
            this.links.add((String)url);
        }
        if (title != null) {
            try {
                this.titleLayout = ChatMessageCell.generateStaticLayout((CharSequence)title, this.titleTextPaint, b, b, 0, 3);
                if (this.titleLayout.getLineCount() > 0) {
                    this.descriptionY = this.titleY + this.titleLayout.getLineBottom(this.titleLayout.getLineCount() - 1) + AndroidUtilities.dp(4.0f);
                }
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
            this.letterDrawable.setTitle((String)title);
        }
        this.description2Y = this.descriptionY;
        final StaticLayout titleLayout = this.titleLayout;
        int lineCount;
        if (titleLayout != null) {
            lineCount = titleLayout.getLineCount();
        }
        else {
            lineCount = 0;
        }
        final int max = Math.max(1, 4 - lineCount);
        if (s2 != null) {
            try {
                this.descriptionLayout = ChatMessageCell.generateStaticLayout((CharSequence)s2, this.descriptionTextPaint, b, b, 0, max);
                if (this.descriptionLayout.getLineCount() > 0) {
                    this.description2Y = this.descriptionY + this.descriptionLayout.getLineBottom(this.descriptionLayout.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                }
            }
            catch (Exception ex3) {
                FileLog.e(ex3);
            }
        }
        if (s3 != null) {
            try {
                this.descriptionLayout2 = ChatMessageCell.generateStaticLayout((CharSequence)s3, this.descriptionTextPaint, b, b, 0, max);
                if (this.descriptionLayout != null) {
                    this.description2Y += AndroidUtilities.dp(10.0f);
                }
            }
            catch (Exception ex4) {
                FileLog.e(ex4);
            }
        }
        if (!this.links.isEmpty()) {
            for (int j = 0; j < this.links.size(); ++j) {
                try {
                    final String s11 = this.links.get(j);
                    final StaticLayout e = new StaticLayout(TextUtils.ellipsize((CharSequence)s11.replace('\n', ' '), this.descriptionTextPaint, (float)Math.min((int)Math.ceil(this.descriptionTextPaint.measureText(s11)), b), TextUtils$TruncateAt.MIDDLE), this.descriptionTextPaint, b, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.linkY = this.description2Y;
                    if (this.descriptionLayout2 != null && this.descriptionLayout2.getLineCount() != 0) {
                        this.linkY += this.descriptionLayout2.getLineBottom(this.descriptionLayout2.getLineCount() - 1) + AndroidUtilities.dp(5.0f);
                    }
                    this.linkLayout.add(e);
                }
                catch (Exception ex5) {
                    FileLog.e(ex5);
                }
            }
        }
        final int dp = AndroidUtilities.dp(52.0f);
        int dp2;
        if (LocaleController.isRTL) {
            dp2 = View$MeasureSpec.getSize(n) - AndroidUtilities.dp(10.0f) - dp;
        }
        else {
            dp2 = AndroidUtilities.dp(10.0f);
        }
        this.letterDrawable.setBounds(dp2, AndroidUtilities.dp(11.0f), dp2 + dp, AndroidUtilities.dp(63.0f));
        if (n2 != 0) {
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, dp, true);
            TLRPC.PhotoSize closestPhotoSizeWithSize2;
            if ((closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 80)) == closestPhotoSizeWithSize) {
                closestPhotoSizeWithSize2 = null;
            }
            closestPhotoSizeWithSize.size = -1;
            if (closestPhotoSizeWithSize2 != null) {
                closestPhotoSizeWithSize2.size = -1;
            }
            this.linkImageView.setImageCoords(dp2, AndroidUtilities.dp(11.0f), dp, dp);
            FileLoader.getAttachFileName(closestPhotoSizeWithSize);
            this.linkImageView.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, this.message.photoThumbsObject), String.format(Locale.US, "%d_%d", dp, dp), ImageLocation.getForObject(closestPhotoSizeWithSize2, this.message.photoThumbsObject), String.format(Locale.US, "%d_%d_b", dp, dp), 0, null, this.message, 0);
            this.drawLinkImageView = true;
        }
        final StaticLayout titleLayout2 = this.titleLayout;
        if (titleLayout2 != null && titleLayout2.getLineCount() != 0) {
            final StaticLayout titleLayout3 = this.titleLayout;
            n2 = titleLayout3.getLineBottom(titleLayout3.getLineCount() - 1) + AndroidUtilities.dp(4.0f) + 0;
        }
        else {
            n2 = 0;
        }
        final StaticLayout descriptionLayout = this.descriptionLayout;
        int n4 = n2;
        if (descriptionLayout != null) {
            n4 = n2;
            if (descriptionLayout.getLineCount() != 0) {
                final StaticLayout descriptionLayout2 = this.descriptionLayout;
                n4 = n2 + (descriptionLayout2.getLineBottom(descriptionLayout2.getLineCount() - 1) + AndroidUtilities.dp(5.0f));
            }
        }
        final StaticLayout descriptionLayout3 = this.descriptionLayout2;
        n2 = n4;
        int k = n3;
        if (descriptionLayout3 != null) {
            n2 = n4;
            k = n3;
            if (descriptionLayout3.getLineCount() != 0) {
                final StaticLayout descriptionLayout4 = this.descriptionLayout2;
                final int n5 = n2 = n4 + (descriptionLayout4.getLineBottom(descriptionLayout4.getLineCount() - 1) + AndroidUtilities.dp(5.0f));
                k = n3;
                if (this.descriptionLayout != null) {
                    n2 = n5 + AndroidUtilities.dp(10.0f);
                    k = n3;
                }
            }
        }
        while (k < this.linkLayout.size()) {
            final StaticLayout staticLayout = this.linkLayout.get(k);
            int n6 = n2;
            if (staticLayout.getLineCount() > 0) {
                n6 = n2 + staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
            }
            ++k;
            n2 = n6;
        }
        this.checkBox.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), Math.max(AndroidUtilities.dp(76.0f), n2 + AndroidUtilities.dp(17.0f)) + (this.needDivider ? 1 : 0));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final MessageObject message = this.message;
        final boolean b = true;
        boolean b4 = false;
        Label_0484: {
            Label_0481: {
                if (message != null && !this.linkLayout.isEmpty()) {
                    final SharedLinkCellDelegate delegate = this.delegate;
                    if (delegate != null && delegate.canPerformActions()) {
                        if (motionEvent.getAction() == 0 || (this.linkPreviewPressed && motionEvent.getAction() == 1)) {
                            final int n = (int)motionEvent.getX();
                            final int n2 = (int)motionEvent.getY();
                            int i = 0;
                            int n3 = 0;
                            while (true) {
                                while (i < this.linkLayout.size()) {
                                    final StaticLayout staticLayout = this.linkLayout.get(i);
                                    int n4 = n3;
                                    if (staticLayout.getLineCount() > 0) {
                                        final int lineBottom = staticLayout.getLineBottom(staticLayout.getLineCount() - 1);
                                        float n5;
                                        if (LocaleController.isRTL) {
                                            n5 = 8.0f;
                                        }
                                        else {
                                            n5 = (float)AndroidUtilities.leftBaseline;
                                        }
                                        final int dp = AndroidUtilities.dp(n5);
                                        final float n6 = (float)n;
                                        final float n7 = (float)dp;
                                        if (n6 >= staticLayout.getLineLeft(0) + n7 && n6 <= n7 + staticLayout.getLineWidth(0)) {
                                            final int linkY = this.linkY;
                                            if (n2 >= linkY + n3 && n2 <= linkY + n3 + lineBottom) {
                                                boolean b2 = false;
                                                boolean b3 = false;
                                                Label_0457: {
                                                    if (motionEvent.getAction() == 0) {
                                                        this.resetPressedLink();
                                                        this.pressedLink = i;
                                                        this.linkPreviewPressed = true;
                                                        this.startCheckLongPress();
                                                        try {
                                                            this.urlPath.setCurrentLayout(staticLayout, 0, 0.0f);
                                                            staticLayout.getSelectionPath(0, staticLayout.getText().length(), (Path)this.urlPath);
                                                        }
                                                        catch (Exception ex) {
                                                            FileLog.e(ex);
                                                        }
                                                    }
                                                    else {
                                                        if (!this.linkPreviewPressed) {
                                                            b2 = true;
                                                            b3 = false;
                                                            break Label_0457;
                                                        }
                                                        try {
                                                            TLRPC.WebPage webpage;
                                                            if (this.pressedLink == 0 && this.message.messageOwner.media != null) {
                                                                webpage = this.message.messageOwner.media.webpage;
                                                            }
                                                            else {
                                                                webpage = null;
                                                            }
                                                            if (webpage != null && webpage.embed_url != null && webpage.embed_url.length() != 0) {
                                                                this.delegate.needOpenWebView(webpage);
                                                            }
                                                            else {
                                                                Browser.openUrl(this.getContext(), this.links.get(this.pressedLink));
                                                            }
                                                        }
                                                        catch (Exception ex2) {
                                                            FileLog.e(ex2);
                                                        }
                                                        this.resetPressedLink();
                                                    }
                                                    b2 = true;
                                                    b3 = true;
                                                }
                                                b4 = b3;
                                                if (!b2) {
                                                    this.resetPressedLink();
                                                    b4 = b3;
                                                }
                                                break Label_0484;
                                            }
                                        }
                                        n4 = n3 + lineBottom;
                                    }
                                    ++i;
                                    n3 = n4;
                                }
                                boolean b2 = false;
                                continue;
                            }
                        }
                        if (motionEvent.getAction() == 3) {
                            this.resetPressedLink();
                        }
                        break Label_0481;
                    }
                }
                this.resetPressedLink();
            }
            b4 = false;
        }
        boolean b5 = b;
        if (!b4) {
            b5 = (super.onTouchEvent(motionEvent) && b);
        }
        return b5;
    }
    
    protected void resetPressedLink() {
        this.pressedLink = -1;
        this.linkPreviewPressed = false;
        this.cancelCheckLongPress();
        this.invalidate();
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(b, b2);
    }
    
    public void setDelegate(final SharedLinkCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setLink(final MessageObject message, final boolean needDivider) {
        this.needDivider = needDivider;
        this.resetPressedLink();
        this.message = message;
        this.requestLayout();
    }
    
    protected void startCheckLongPress() {
        if (this.checkingForLongPress) {
            return;
        }
        this.checkingForLongPress = true;
        if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new CheckForTap();
        }
        this.postDelayed((Runnable)this.pendingCheckForTap, (long)ViewConfiguration.getTapTimeout());
    }
    
    class CheckForLongPress implements Runnable
    {
        public int currentPressCount;
        
        @Override
        public void run() {
            if (SharedLinkCell.this.checkingForLongPress && SharedLinkCell.this.getParent() != null && this.currentPressCount == SharedLinkCell.this.pressCount) {
                SharedLinkCell.this.checkingForLongPress = false;
                SharedLinkCell.this.performHapticFeedback(0);
                if (SharedLinkCell.this.pressedLink >= 0) {
                    final SharedLinkCellDelegate access$400 = SharedLinkCell.this.delegate;
                    final SharedLinkCell this$0 = SharedLinkCell.this;
                    access$400.onLinkLongPress(this$0.links.get(this$0.pressedLink));
                }
                final MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                SharedLinkCell.this.onTouchEvent(obtain);
                obtain.recycle();
            }
        }
    }
    
    private final class CheckForTap implements Runnable
    {
        @Override
        public void run() {
            if (SharedLinkCell.this.pendingCheckForLongPress == null) {
                final SharedLinkCell this$0 = SharedLinkCell.this;
                this$0.pendingCheckForLongPress = this$0.new CheckForLongPress();
            }
            SharedLinkCell.this.pendingCheckForLongPress.currentPressCount = ++SharedLinkCell.this.pressCount;
            final SharedLinkCell this$2 = SharedLinkCell.this;
            this$2.postDelayed((Runnable)this$2.pendingCheckForLongPress, (long)(ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
        }
    }
    
    public interface SharedLinkCellDelegate
    {
        boolean canPerformActions();
        
        void needOpenWebView(final TLRPC.WebPage p0);
        
        void onLinkLongPress(final String p0);
    }
}
