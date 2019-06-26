// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.Point;
import android.text.Layout$Alignment;
import org.telegram.messenger.Emoji;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.LocaleController;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.browser.Browser;
import android.text.style.URLSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.messenger.FileLog;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Path;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.content.Context;
import org.telegram.ui.Components.LinkPath;
import android.text.StaticLayout;
import android.text.style.ClickableSpan;
import android.view.View;

public class BotHelpCell extends View
{
    private BotHelpCellDelegate delegate;
    private int height;
    private String oldText;
    private ClickableSpan pressedLink;
    private StaticLayout textLayout;
    private int textX;
    private int textY;
    private LinkPath urlPath;
    private int width;
    
    public BotHelpCell(final Context context) {
        super(context);
        this.urlPath = new LinkPath();
    }
    
    private void resetPressedLink() {
        if (this.pressedLink != null) {
            this.pressedLink = null;
        }
        this.invalidate();
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = (this.getWidth() - this.width) / 2;
        final int dp = AndroidUtilities.dp(4.0f);
        Theme.chat_msgInMediaShadowDrawable.setBounds(n, dp, this.width + n, this.height + dp);
        Theme.chat_msgInMediaShadowDrawable.draw(canvas);
        Theme.chat_msgInMediaDrawable.setBounds(n, dp, this.width + n, this.height + dp);
        Theme.chat_msgInMediaDrawable.draw(canvas);
        Theme.chat_msgTextPaint.setColor(Theme.getColor("chat_messageTextIn"));
        Theme.chat_msgTextPaint.linkColor = Theme.getColor("chat_messageLinkIn");
        canvas.save();
        final int textX = AndroidUtilities.dp(11.0f) + n;
        this.textX = textX;
        final float n2 = (float)textX;
        final int textY = AndroidUtilities.dp(11.0f) + dp;
        this.textY = textY;
        canvas.translate(n2, (float)textY);
        if (this.pressedLink != null) {
            canvas.drawPath((Path)this.urlPath, Theme.chat_urlPaint);
        }
        final StaticLayout textLayout = this.textLayout;
        if (textLayout != null) {
            textLayout.draw(canvas);
        }
        canvas.restore();
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setText(this.textLayout.getText());
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), this.height + AndroidUtilities.dp(8.0f));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        final StaticLayout textLayout = this.textLayout;
        boolean b = false;
        boolean b2 = false;
        Label_0446: {
            Label_0443: {
                if (textLayout != null) {
                    if (motionEvent.getAction() == 0 || (this.pressedLink != null && motionEvent.getAction() == 1)) {
                        Label_0437: {
                            if (motionEvent.getAction() == 0) {
                                this.resetPressedLink();
                                try {
                                    final int n = (int)(x - this.textX);
                                    final int lineForVertical = this.textLayout.getLineForVertical((int)(y - this.textY));
                                    final StaticLayout textLayout2 = this.textLayout;
                                    final float n2 = (float)n;
                                    final int offsetForHorizontal = textLayout2.getOffsetForHorizontal(lineForVertical, n2);
                                    final float lineLeft = this.textLayout.getLineLeft(lineForVertical);
                                    if (lineLeft <= n2 && lineLeft + this.textLayout.getLineWidth(lineForVertical) >= n2) {
                                        final Spannable spannable = (Spannable)this.textLayout.getText();
                                        final ClickableSpan[] array = (ClickableSpan[])spannable.getSpans(offsetForHorizontal, offsetForHorizontal, (Class)ClickableSpan.class);
                                        if (array.length != 0) {
                                            this.resetPressedLink();
                                            this.pressedLink = array[0];
                                            try {
                                                final int spanStart = spannable.getSpanStart((Object)this.pressedLink);
                                                this.urlPath.setCurrentLayout(this.textLayout, spanStart, 0.0f);
                                                this.textLayout.getSelectionPath(spanStart, spannable.getSpanEnd((Object)this.pressedLink), (Path)this.urlPath);
                                                break Label_0437;
                                            }
                                            catch (Exception ex) {
                                                try {
                                                    FileLog.e(ex);
                                                }
                                                catch (Exception ex2) {
                                                    b2 = true;
                                                }
                                            }
                                        }
                                        this.resetPressedLink();
                                        break Label_0443;
                                    }
                                    this.resetPressedLink();
                                    break Label_0443;
                                }
                                catch (Exception ex2) {
                                    b2 = false;
                                }
                                this.resetPressedLink();
                                final Exception ex2;
                                FileLog.e(ex2);
                                break Label_0446;
                            }
                            final ClickableSpan pressedLink = this.pressedLink;
                            if (pressedLink == null) {
                                break Label_0443;
                            }
                            try {
                                if (pressedLink instanceof URLSpanNoUnderline) {
                                    final String url = ((URLSpanNoUnderline)pressedLink).getURL();
                                    if ((url.startsWith("@") || url.startsWith("#") || url.startsWith("/")) && this.delegate != null) {
                                        this.delegate.didPressUrl(url);
                                    }
                                }
                                else if (pressedLink instanceof URLSpan) {
                                    Browser.openUrl(this.getContext(), ((URLSpan)this.pressedLink).getURL());
                                }
                                else {
                                    pressedLink.onClick((View)this);
                                }
                            }
                            catch (Exception ex3) {
                                FileLog.e(ex3);
                            }
                            this.resetPressedLink();
                        }
                        b2 = true;
                        break Label_0446;
                    }
                    if (motionEvent.getAction() == 3) {
                        this.resetPressedLink();
                    }
                }
            }
            b2 = false;
        }
        if (b2 || super.onTouchEvent(motionEvent)) {
            b = true;
        }
        return b;
    }
    
    public void setDelegate(final BotHelpCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setText(final String oldText) {
        if (oldText == null || oldText.length() == 0) {
            this.setVisibility(8);
            return;
        }
        if (oldText != null && oldText.equals(this.oldText)) {
            return;
        }
        this.oldText = oldText;
        final int n = 0;
        this.setVisibility(0);
        int n2;
        if (AndroidUtilities.isTablet()) {
            n2 = AndroidUtilities.getMinTabletSide();
        }
        else {
            final Point displaySize = AndroidUtilities.displaySize;
            n2 = Math.min(displaySize.x, displaySize.y);
        }
        final int width = (int)(n2 * 0.7f);
        final String[] split = oldText.split("\n");
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        final String string = LocaleController.getString("BotInfoTitle", 2131558851);
        spannableStringBuilder.append((CharSequence)string);
        spannableStringBuilder.append((CharSequence)"\n\n");
        for (int i = 0; i < split.length; ++i) {
            spannableStringBuilder.append((CharSequence)split[i].trim());
            if (i != split.length - 1) {
                spannableStringBuilder.append((CharSequence)"\n");
            }
        }
        MessageObject.addLinks(false, (CharSequence)spannableStringBuilder);
        spannableStringBuilder.setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, string.length(), 33);
        Emoji.replaceEmoji((CharSequence)spannableStringBuilder, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
        try {
            this.textLayout = new StaticLayout((CharSequence)spannableStringBuilder, Theme.chat_msgTextPaint, width, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.width = 0;
            this.height = this.textLayout.getHeight() + AndroidUtilities.dp(22.0f);
            for (int lineCount = this.textLayout.getLineCount(), j = n; j < lineCount; ++j) {
                this.width = (int)Math.ceil(Math.max((float)this.width, this.textLayout.getLineWidth(j) + this.textLayout.getLineLeft(j)));
            }
            if (this.width > width) {
                this.width = width;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        this.width += AndroidUtilities.dp(22.0f);
    }
    
    public interface BotHelpCellDelegate
    {
        void didPressUrl(final String p0);
    }
}
