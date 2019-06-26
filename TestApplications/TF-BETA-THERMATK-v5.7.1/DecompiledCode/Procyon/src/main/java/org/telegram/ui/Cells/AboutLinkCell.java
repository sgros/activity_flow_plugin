// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.Emoji;
import org.telegram.messenger.MessageObject;
import android.text.TextUtils;
import org.telegram.messenger.browser.Browser;
import android.text.style.URLSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import android.text.Spannable;
import android.view.MotionEvent;
import android.annotation.SuppressLint;
import android.text.Layout$Alignment;
import android.text.StaticLayout$Builder;
import android.os.Build$VERSION;
import android.view.View$MeasureSpec;
import org.telegram.messenger.FileLog;
import android.graphics.Path;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.LinkPath;
import android.text.StaticLayout;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.widget.FrameLayout;

public class AboutLinkCell extends FrameLayout
{
    private String oldText;
    private ClickableSpan pressedLink;
    private SpannableStringBuilder stringBuilder;
    private StaticLayout textLayout;
    private int textX;
    private int textY;
    private LinkPath urlPath;
    private TextView valueTextView;
    
    public AboutLinkCell(final Context context) {
        super(context);
        this.urlPath = new LinkPath();
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        final TextView valueTextView = this.valueTextView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        valueTextView.setGravity(gravity);
        final TextView valueTextView2 = this.valueTextView;
        int n2;
        if (LocaleController.isRTL) {
            n2 = n;
        }
        else {
            n2 = 3;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n2 | 0x50, 23.0f, 0.0f, 23.0f, 10.0f));
        this.setWillNotDraw(false);
    }
    
    private void resetPressedLink() {
        if (this.pressedLink != null) {
            this.pressedLink = null;
        }
        this.invalidate();
    }
    
    protected void didPressUrl(final String s) {
    }
    
    protected void onDraw(final Canvas canvas) {
        canvas.save();
        final int dp = AndroidUtilities.dp(23.0f);
        this.textX = dp;
        final float n = (float)dp;
        final int dp2 = AndroidUtilities.dp(8.0f);
        this.textY = dp2;
        canvas.translate(n, (float)dp2);
        if (this.pressedLink != null) {
            canvas.drawPath((Path)this.urlPath, Theme.linkSelectionPaint);
        }
        try {
            if (this.textLayout != null) {
                this.textLayout.draw(canvas);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        canvas.restore();
    }
    
    @SuppressLint({ "DrawAllocation" })
    protected void onMeasure(final int n, int n2) {
        if (this.stringBuilder != null) {
            n2 = View$MeasureSpec.getSize(n) - AndroidUtilities.dp(46.0f);
            if (Build$VERSION.SDK_INT >= 24) {
                final SpannableStringBuilder stringBuilder = this.stringBuilder;
                final StaticLayout$Builder setHyphenationFrequency = StaticLayout$Builder.obtain((CharSequence)stringBuilder, 0, stringBuilder.length(), Theme.profile_aboutTextPaint, n2).setBreakStrategy(1).setHyphenationFrequency(0);
                Layout$Alignment alignment;
                if (LocaleController.isRTL) {
                    alignment = Layout$Alignment.ALIGN_RIGHT;
                }
                else {
                    alignment = Layout$Alignment.ALIGN_LEFT;
                }
                this.textLayout = setHyphenationFrequency.setAlignment(alignment).build();
            }
            else {
                this.textLayout = new StaticLayout((CharSequence)this.stringBuilder, Theme.profile_aboutTextPaint, n2, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        }
        final StaticLayout textLayout = this.textLayout;
        if (textLayout != null) {
            n2 = textLayout.getHeight();
        }
        else {
            n2 = AndroidUtilities.dp(20.0f);
        }
        final int n3 = n2 += AndroidUtilities.dp(16.0f);
        if (this.valueTextView.getVisibility() == 0) {
            n2 = n3 + AndroidUtilities.dp(23.0f);
        }
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(n2, 1073741824));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        final StaticLayout textLayout = this.textLayout;
        boolean b = false;
        boolean b2 = false;
        Label_0438: {
            Label_0435: {
                if (textLayout != null) {
                    if (motionEvent.getAction() == 0 || (this.pressedLink != null && motionEvent.getAction() == 1)) {
                        Label_0429: {
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
                                                break Label_0429;
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
                                        break Label_0435;
                                    }
                                    this.resetPressedLink();
                                    break Label_0435;
                                }
                                catch (Exception ex2) {
                                    b2 = false;
                                }
                                this.resetPressedLink();
                                final Exception ex2;
                                FileLog.e(ex2);
                                break Label_0438;
                            }
                            final ClickableSpan pressedLink = this.pressedLink;
                            if (pressedLink == null) {
                                break Label_0435;
                            }
                            try {
                                if (pressedLink instanceof URLSpanNoUnderline) {
                                    final String url = ((URLSpanNoUnderline)pressedLink).getURL();
                                    if (url.startsWith("@") || url.startsWith("#") || url.startsWith("/")) {
                                        this.didPressUrl(url);
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
                        break Label_0438;
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
    
    public void setText(final String s, final boolean b) {
        this.setTextAndValue(s, null, b);
    }
    
    public void setTextAndValue(final String oldText, final String text, final boolean b) {
        if (!TextUtils.isEmpty((CharSequence)oldText)) {
            if (oldText != null) {
                final String oldText2 = this.oldText;
                if (oldText2 != null && oldText.equals(oldText2)) {
                    return;
                }
            }
            this.oldText = oldText;
            this.stringBuilder = new SpannableStringBuilder((CharSequence)this.oldText);
            if (b) {
                MessageObject.addLinks(false, (CharSequence)this.stringBuilder, false);
            }
            Emoji.replaceEmoji((CharSequence)this.stringBuilder, Theme.profile_aboutTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            if (TextUtils.isEmpty((CharSequence)text)) {
                this.valueTextView.setVisibility(8);
            }
            else {
                this.valueTextView.setText((CharSequence)text);
                this.valueTextView.setVisibility(0);
            }
            this.requestLayout();
        }
    }
}
