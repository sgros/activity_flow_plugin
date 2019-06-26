// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.Bitmap;
import org.telegram.ui.PhotoViewer;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.browser.Browser;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import org.telegram.messenger.FileLog;
import android.text.Layout$Alignment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.AndroidUtilities;
import android.view.View;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.text.StaticLayout;
import android.text.style.URLSpan;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.Components.AvatarDrawable;

public class ChatActionCell extends BaseCell
{
    private AvatarDrawable avatarDrawable;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private int customDate;
    private CharSequence customText;
    private ChatActionCellDelegate delegate;
    private boolean hasReplyMessage;
    private boolean imagePressed;
    private ImageReceiver imageReceiver;
    private float lastTouchX;
    private float lastTouchY;
    private URLSpan pressedLink;
    private int previousWidth;
    private int textHeight;
    private StaticLayout textLayout;
    private int textWidth;
    private int textX;
    private int textXLeft;
    private int textY;
    private boolean wasLayout;
    
    public ChatActionCell(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        (this.imageReceiver = new ImageReceiver((View)this)).setRoundRadius(AndroidUtilities.dp(32.0f));
        this.avatarDrawable = new AvatarDrawable();
    }
    
    private void buildLayout() {
        final MessageObject currentMessageObject = this.currentMessageObject;
        CharSequence charSequence = null;
        Label_0100: {
            if (currentMessageObject != null) {
                final TLRPC.Message messageOwner = currentMessageObject.messageOwner;
                if (messageOwner != null) {
                    final TLRPC.MessageMedia media = messageOwner.media;
                    if (media != null && media.ttl_seconds != 0) {
                        if (media.photo instanceof TLRPC.TL_photoEmpty) {
                            charSequence = LocaleController.getString("AttachPhotoExpired", 2131558728);
                            break Label_0100;
                        }
                        if (media.document instanceof TLRPC.TL_documentEmpty) {
                            charSequence = LocaleController.getString("AttachVideoExpired", 2131558734);
                            break Label_0100;
                        }
                        charSequence = currentMessageObject.messageText;
                        break Label_0100;
                    }
                }
                charSequence = this.currentMessageObject.messageText;
            }
            else {
                charSequence = this.customText;
            }
        }
        this.createLayout(charSequence, this.previousWidth);
        final MessageObject currentMessageObject2 = this.currentMessageObject;
        if (currentMessageObject2 != null && currentMessageObject2.type == 11) {
            this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.dp(64.0f)) / 2, this.textHeight + AndroidUtilities.dp(15.0f), AndroidUtilities.dp(64.0f), AndroidUtilities.dp(64.0f));
        }
    }
    
    private void createLayout(final CharSequence charSequence, final int n) {
        final int n2 = n - AndroidUtilities.dp(30.0f);
        this.textLayout = new StaticLayout(charSequence, Theme.chat_actionTextPaint, n2, Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        int i = 0;
        this.textHeight = 0;
        this.textWidth = 0;
        try {
            while (i < this.textLayout.getLineCount()) {
                try {
                    final float lineWidth = this.textLayout.getLineWidth(i);
                    final float n3 = (float)n2;
                    float n4 = lineWidth;
                    if (lineWidth > n3) {
                        n4 = n3;
                    }
                    this.textHeight = (int)Math.max(this.textHeight, Math.ceil(this.textLayout.getLineBottom(i)));
                    this.textWidth = (int)Math.max(this.textWidth, Math.ceil(n4));
                    ++i;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    return;
                }
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        this.textX = (n - this.textWidth) / 2;
        this.textY = AndroidUtilities.dp(7.0f);
        this.textXLeft = (n - this.textLayout.getWidth()) / 2;
    }
    
    private int findMaxWidthAroundLine(int i) {
        int n = (int)Math.ceil(this.textLayout.getLineWidth(i));
        for (int lineCount = this.textLayout.getLineCount(), j = i + 1; j < lineCount; ++j) {
            final int a = (int)Math.ceil(this.textLayout.getLineWidth(j));
            if (Math.abs(a - n) >= AndroidUtilities.dp(10.0f)) {
                break;
            }
            n = Math.max(a, n);
        }
        --i;
        while (i >= 0) {
            final int a2 = (int)Math.ceil(this.textLayout.getLineWidth(i));
            if (Math.abs(a2 - n) >= AndroidUtilities.dp(10.0f)) {
                break;
            }
            n = Math.max(a2, n);
            --i;
        }
        return n;
    }
    
    private boolean isLineBottom(final int n, int n2, final int n3, final int n4, final int n5) {
        final boolean b = true;
        n2 = n4 - 1;
        boolean b2 = b;
        if (n3 != n2) {
            b2 = (n3 >= 0 && n3 <= n2 && this.findMaxWidthAroundLine(n3 + 1) + n5 * 3 < n && b);
        }
        return b2;
    }
    
    private boolean isLineTop(final int n, final int n2, final int n3, final int n4, final int n5) {
        boolean b = true;
        if (n3 != 0) {
            b = (n3 >= 0 && n3 < n4 && this.findMaxWidthAroundLine(n3 - 1) + n5 * 3 < n && b);
        }
        return b;
    }
    
    public int getCustomDate() {
        return this.customDate;
    }
    
    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }
    
    public ImageReceiver getPhotoImage() {
        return this.imageReceiver;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.wasLayout = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        Canvas canvas2 = canvas;
        final MessageObject currentMessageObject = this.currentMessageObject;
        if (currentMessageObject != null && currentMessageObject.type == 11) {
            this.imageReceiver.draw(canvas2);
        }
        final StaticLayout textLayout = this.textLayout;
        if (textLayout != null) {
            final int lineCount = textLayout.getLineCount();
            int dp = AndroidUtilities.dp(11.0f);
            final int dp2 = AndroidUtilities.dp(6.0f);
            final int n = dp - dp2;
            final int dp3 = AndroidUtilities.dp(8.0f);
            int dp4 = AndroidUtilities.dp(7.0f);
            int n2 = 0;
            int n3 = 0;
            int lineBottom;
            int n42;
            int n43;
            int n80;
            for (int i = 0; i < lineCount; i = n43 + 1, n2 = lineBottom, dp = n42, n3 = n80) {
                final int maxWidthAroundLine = this.findMaxWidthAroundLine(i);
                final int n4 = (this.getMeasuredWidth() - maxWidthAroundLine - n) / 2;
                final int n5 = maxWidthAroundLine + n;
                lineBottom = this.textLayout.getLineBottom(i);
                final int n6 = lineBottom - n2;
                final boolean b = i == lineCount - 1;
                final boolean b2 = i == 0;
                int n7 = n6;
                int n8 = dp4;
                if (b2) {
                    n8 = dp4 - AndroidUtilities.dp(3.0f);
                    n7 = n6 + AndroidUtilities.dp(3.0f);
                }
                int n9 = n7;
                if (b) {
                    n9 = n7 + AndroidUtilities.dp(3.0f);
                }
                int n13 = 0;
                int n14 = 0;
                boolean b3 = false;
                Label_0339: {
                    if (!b) {
                        final int n10 = i + 1;
                        if (n10 < lineCount) {
                            final int n11 = this.findMaxWidthAroundLine(n10) + n;
                            final int n12 = n * 2;
                            if (n11 + n12 < n5) {
                                n13 = n11;
                                n14 = 1;
                                b3 = true;
                                break Label_0339;
                            }
                            if (n5 + n12 < n11) {
                                n13 = n11;
                                n14 = 2;
                                b3 = b;
                                break Label_0339;
                            }
                            n13 = n11;
                            n14 = 3;
                            b3 = b;
                            break Label_0339;
                        }
                    }
                    n14 = 0;
                    n13 = 0;
                    b3 = b;
                }
                int n17;
                int n18;
                boolean b4;
                if (!b2 && i > 0) {
                    final int n15 = this.findMaxWidthAroundLine(i - 1) + n;
                    final int n16 = n * 2;
                    if (n15 + n16 < n5) {
                        n17 = n15;
                        n18 = 1;
                        b4 = true;
                    }
                    else if (n5 + n16 < n15) {
                        n17 = n15;
                        n18 = 2;
                        b4 = b2;
                    }
                    else {
                        n17 = n15;
                        n18 = 3;
                        b4 = b2;
                    }
                }
                else {
                    n18 = 0;
                    n17 = 0;
                    b4 = b2;
                }
                int n29;
                int n30;
                if (n14 != 0) {
                    if (n14 == 1) {
                        final int n19 = (this.getMeasuredWidth() - n13) / 2;
                        final int dp5 = AndroidUtilities.dp(3.0f);
                        final int n20 = n8;
                        final int n21 = n5;
                        if (this.isLineBottom(n13, n5, i + 1, lineCount, n)) {
                            final float n22 = (float)(n4 + dp2);
                            final int n23 = n20 + n9;
                            final float n24 = (float)n23;
                            canvas.drawRect(n22, n24, (float)(n19 - n), (float)(AndroidUtilities.dp(3.0f) + n23), Theme.chat_actionBackgroundPaint);
                            canvas.drawRect((float)(n19 + n13 + n), n24, (float)(n4 + n21 - dp2), (float)(n23 + AndroidUtilities.dp(3.0f)), Theme.chat_actionBackgroundPaint);
                        }
                        else {
                            final float n25 = (float)(n4 + dp2);
                            final int n26 = n20 + n9;
                            final float n27 = (float)n26;
                            canvas.drawRect(n25, n27, (float)n19, (float)(AndroidUtilities.dp(3.0f) + n26), Theme.chat_actionBackgroundPaint);
                            canvas.drawRect((float)(n19 + n13), n27, (float)(n4 + n21 - dp2), (float)(n26 + AndroidUtilities.dp(3.0f)), Theme.chat_actionBackgroundPaint);
                        }
                        final int n28 = dp5;
                        canvas2 = canvas;
                        n29 = n18;
                        n30 = n28;
                    }
                    else {
                        final int n31 = dp;
                        if (n14 == 2) {
                            final int dp6 = AndroidUtilities.dp(3.0f);
                            final int n32 = n8 + n9 - AndroidUtilities.dp(11.0f);
                            int n34;
                            final int n33 = n34 = n4 - dp3;
                            if (n18 != 2) {
                                n34 = n33;
                                if (n18 != 3) {
                                    n34 = n33 - n;
                                }
                            }
                            if (b4 || b3) {
                                final int n35 = n34 + dp3;
                                canvas.drawRect((float)n35, (float)(AndroidUtilities.dp(3.0f) + n32), (float)(n35 + n31), (float)(n32 + n31), Theme.chat_actionBackgroundPaint);
                            }
                            final int n36 = n18;
                            final Drawable drawable = Theme.chat_cornerInner[2];
                            final int n37 = n32 + dp3;
                            drawable.setBounds(n34, n32, n34 + dp3, n37);
                            final Drawable drawable2 = Theme.chat_cornerInner[2];
                            canvas2 = canvas;
                            drawable2.draw(canvas2);
                            int n39;
                            final int n38 = n39 = n4 + n5;
                            if (n36 != 2) {
                                n39 = n38;
                                if (n36 != 3) {
                                    n39 = n38 + n;
                                }
                            }
                            if (b4 || b3) {
                                canvas.drawRect((float)(n39 - n31), (float)(AndroidUtilities.dp(3.0f) + n32), (float)n39, (float)(n32 + n31), Theme.chat_actionBackgroundPaint);
                            }
                            n29 = n36;
                            Theme.chat_cornerInner[3].setBounds(n39, n32, n39 + dp3, n37);
                            Theme.chat_cornerInner[3].draw(canvas2);
                            n30 = dp6;
                        }
                        else {
                            canvas2 = canvas;
                            final int dp7 = AndroidUtilities.dp(6.0f);
                            n29 = n18;
                            n30 = dp7;
                        }
                    }
                }
                else {
                    n29 = n18;
                    n30 = 0;
                }
                final int n40 = n4;
                final int n41 = n3;
                n42 = dp;
                n43 = i;
                final int n44 = n14;
                int n46;
                int n47;
                if (n29 != 0) {
                    if (n29 == 1) {
                        final int n45 = (this.getMeasuredWidth() - n17) / 2;
                        n46 = n8 - AndroidUtilities.dp(3.0f);
                        n47 = n9 + AndroidUtilities.dp(3.0f);
                        if (this.isLineTop(n17, n5, n43 - 1, lineCount, n)) {
                            final float n48 = (float)(n40 + dp2);
                            final float n49 = (float)n46;
                            canvas.drawRect(n48, n49, (float)(n45 - n), (float)(AndroidUtilities.dp(3.0f) + n46), Theme.chat_actionBackgroundPaint);
                            canvas.drawRect((float)(n45 + n17 + n), n49, (float)(n40 + n5 - dp2), (float)(AndroidUtilities.dp(3.0f) + n46), Theme.chat_actionBackgroundPaint);
                        }
                        else {
                            final float n50 = (float)(n40 + dp2);
                            final float n51 = (float)n46;
                            canvas.drawRect(n50, n51, (float)n45, (float)(AndroidUtilities.dp(3.0f) + n46), Theme.chat_actionBackgroundPaint);
                            canvas.drawRect((float)(n45 + n17), n51, (float)(n40 + n5 - dp2), (float)(AndroidUtilities.dp(3.0f) + n46), Theme.chat_actionBackgroundPaint);
                        }
                    }
                    else if (n29 == 2) {
                        final int n52 = n8 - AndroidUtilities.dp(3.0f);
                        n47 = n9 + AndroidUtilities.dp(3.0f);
                        int n54;
                        final int n53 = n54 = n40 - dp3;
                        if (n44 != 2) {
                            n54 = n53;
                            if (n44 != 3) {
                                n54 = n53 - n;
                            }
                        }
                        if (b4 || b3) {
                            final int n55 = n54 + dp3;
                            canvas.drawRect((float)n55, (float)(AndroidUtilities.dp(3.0f) + n52), (float)(n55 + n42), (float)(n52 + AndroidUtilities.dp(11.0f)), Theme.chat_actionBackgroundPaint);
                        }
                        final int n56 = n44;
                        final Drawable drawable3 = Theme.chat_cornerInner[0];
                        final int n57 = n41 + dp3;
                        drawable3.setBounds(n54, n41, n54 + dp3, n57);
                        Theme.chat_cornerInner[0].draw(canvas2);
                        int n59;
                        final int n58 = n59 = n40 + n5;
                        if (n56 != 2) {
                            n59 = n58;
                            if (n56 != 3) {
                                n59 = n58 + n;
                            }
                        }
                        if (b4 || b3) {
                            canvas.drawRect((float)(n59 - n42), (float)(AndroidUtilities.dp(3.0f) + n52), (float)n59, (float)(AndroidUtilities.dp(11.0f) + n52), Theme.chat_actionBackgroundPaint);
                        }
                        Theme.chat_cornerInner[1].setBounds(n59, n41, n59 + dp3, n57);
                        Theme.chat_cornerInner[1].draw(canvas2);
                        n46 = n52;
                    }
                    else {
                        final int dp8 = AndroidUtilities.dp(6.0f);
                        n47 = n9 + AndroidUtilities.dp(6.0f);
                        n46 = n8 - dp8;
                    }
                }
                else {
                    n46 = n8;
                    n47 = n9;
                }
                if (!b4 && !b3) {
                    final int n60 = n40;
                    canvas.drawRect((float)n60, (float)n8, (float)(n60 + n5), (float)(n8 + n9), Theme.chat_actionBackgroundPaint);
                }
                else {
                    final int n61 = n40;
                    canvas.drawRect((float)(n61 + dp2), (float)n8, (float)(n61 + n5 - dp2), (float)(n8 + n9), Theme.chat_actionBackgroundPaint);
                }
                final int n62 = n40 - n;
                final int n63 = n40 + n5 - dp2;
                if (b4 && !b3 && n44 != 2) {
                    final float n64 = (float)n62;
                    final float n65 = (float)(n46 + n42);
                    final float n66 = (float)(n62 + n42);
                    final int n67 = n46 + n47 + n30;
                    canvas.drawRect(n64, n65, n66, (float)(n67 - AndroidUtilities.dp(6.0f)), Theme.chat_actionBackgroundPaint);
                    canvas.drawRect((float)n63, n65, (float)(n63 + n42), (float)(n67 - AndroidUtilities.dp(6.0f)), Theme.chat_actionBackgroundPaint);
                }
                else if (b3 && !b4 && n29 != 2) {
                    final float n68 = (float)n62;
                    final int n69 = n46 + n42;
                    final float n70 = (float)(n69 - AndroidUtilities.dp(5.0f));
                    final float n71 = (float)(n62 + n42);
                    final float n72 = (float)(n46 + n47 + n30 - n42);
                    canvas.drawRect(n68, n70, n71, n72, Theme.chat_actionBackgroundPaint);
                    canvas.drawRect((float)n63, (float)(n69 - AndroidUtilities.dp(5.0f)), (float)(n63 + n42), n72, Theme.chat_actionBackgroundPaint);
                }
                else if (b4 || b3) {
                    final float n73 = (float)n62;
                    final float n74 = (float)(n46 + n42);
                    final float n75 = (float)(n62 + n42);
                    final float n76 = (float)(n46 + n47 + n30 - n42);
                    canvas.drawRect(n73, n74, n75, n76, Theme.chat_actionBackgroundPaint);
                    canvas.drawRect((float)n63, n74, (float)(n63 + n42), n76, Theme.chat_actionBackgroundPaint);
                }
                if (b4) {
                    final Drawable drawable4 = Theme.chat_cornerOuter[0];
                    final int n77 = n46 + n42;
                    drawable4.setBounds(n62, n46, n62 + n42, n77);
                    Theme.chat_cornerOuter[0].draw(canvas2);
                    Theme.chat_cornerOuter[1].setBounds(n63, n46, n63 + n42, n77);
                    Theme.chat_cornerOuter[1].draw(canvas2);
                }
                if (b3) {
                    final int n78 = n46 + n47 + n30 - n42;
                    final Drawable drawable5 = Theme.chat_cornerOuter[2];
                    final int n79 = n78 + n42;
                    drawable5.setBounds(n63, n78, n63 + n42, n79);
                    Theme.chat_cornerOuter[2].draw(canvas2);
                    Theme.chat_cornerOuter[3].setBounds(n62, n78, n62 + n42, n79);
                    Theme.chat_cornerOuter[3].draw(canvas2);
                }
                dp4 = n46 + n47;
                n80 = dp4 + n30;
            }
            canvas.save();
            canvas2.translate((float)this.textXLeft, (float)this.textY);
            this.textLayout.draw(canvas2);
            canvas.restore();
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (TextUtils.isEmpty(this.customText) && this.currentMessageObject == null) {
            return;
        }
        CharSequence text;
        if (!TextUtils.isEmpty(this.customText)) {
            text = this.customText;
        }
        else {
            text = this.currentMessageObject.messageText;
        }
        accessibilityNodeInfo.setText(text);
        accessibilityNodeInfo.setEnabled(true);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
    }
    
    @Override
    protected void onLongPress() {
        final ChatActionCellDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.didLongPress(this, this.lastTouchX, this.lastTouchY);
        }
    }
    
    protected void onMeasure(int n, int textHeight) {
        if (this.currentMessageObject == null && this.customText == null) {
            this.setMeasuredDimension(View$MeasureSpec.getSize(n), this.textHeight + AndroidUtilities.dp(14.0f));
            return;
        }
        final int max = Math.max(AndroidUtilities.dp(30.0f), View$MeasureSpec.getSize(n));
        if (this.previousWidth != max) {
            this.wasLayout = true;
            this.previousWidth = max;
            this.buildLayout();
        }
        textHeight = this.textHeight;
        final MessageObject currentMessageObject = this.currentMessageObject;
        if (currentMessageObject != null && currentMessageObject.type == 11) {
            n = 70;
        }
        else {
            n = 0;
        }
        this.setMeasuredDimension(max, textHeight + AndroidUtilities.dp((float)(14 + n)));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.currentMessageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        final float x = motionEvent.getX();
        this.lastTouchX = x;
        final float y = motionEvent.getY();
        this.lastTouchY = y;
        final int action = motionEvent.getAction();
        final boolean b = true;
        int n2 = 0;
        Label_0215: {
            if (action == 0) {
                if (this.delegate != null) {
                    int n;
                    if (this.currentMessageObject.type == 11 && this.imageReceiver.isInsideImage(x, y)) {
                        this.imagePressed = true;
                        n = 1;
                    }
                    else {
                        n = 0;
                    }
                    n2 = n;
                    if (n != 0) {
                        this.startCheckLongPress();
                        n2 = n;
                    }
                    break Label_0215;
                }
            }
            else {
                if (motionEvent.getAction() != 2) {
                    this.cancelCheckLongPress();
                }
                if (this.imagePressed) {
                    if (motionEvent.getAction() == 1) {
                        this.imagePressed = false;
                        final ChatActionCellDelegate delegate = this.delegate;
                        if (delegate != null) {
                            delegate.didClickImage(this);
                            this.playSoundEffect(0);
                        }
                    }
                    else if (motionEvent.getAction() == 3) {
                        this.imagePressed = false;
                    }
                    else if (motionEvent.getAction() == 2 && !this.imageReceiver.isInsideImage(x, y)) {
                        this.imagePressed = false;
                    }
                }
            }
            n2 = 0;
        }
        int n3 = n2;
        Label_0599: {
            if (n2 == 0) {
                if (motionEvent.getAction() != 0) {
                    n3 = n2;
                    if (this.pressedLink == null) {
                        break Label_0599;
                    }
                    n3 = n2;
                    if (motionEvent.getAction() != 1) {
                        break Label_0599;
                    }
                }
                final int textX = this.textX;
                if (x >= textX) {
                    final int textY = this.textY;
                    if (y >= textY && x <= textX + this.textWidth && y <= this.textHeight + textY) {
                        final float n4 = (float)textY;
                        final float n5 = x - this.textXLeft;
                        final int lineForVertical = this.textLayout.getLineForVertical((int)(y - n4));
                        final int offsetForHorizontal = this.textLayout.getOffsetForHorizontal(lineForVertical, n5);
                        final float lineLeft = this.textLayout.getLineLeft(lineForVertical);
                        if (lineLeft <= n5 && lineLeft + this.textLayout.getLineWidth(lineForVertical) >= n5) {
                            final CharSequence messageText = this.currentMessageObject.messageText;
                            if (messageText instanceof Spannable) {
                                final URLSpan[] array = (URLSpan[])((Spannable)messageText).getSpans(offsetForHorizontal, offsetForHorizontal, (Class)URLSpan.class);
                                if (array.length != 0) {
                                    if (motionEvent.getAction() == 0) {
                                        this.pressedLink = array[0];
                                        n2 = (b ? 1 : 0);
                                    }
                                    else if (array[0] == this.pressedLink) {
                                        n2 = (b ? 1 : 0);
                                        if (this.delegate != null) {
                                            final String url = array[0].getURL();
                                            if (url.startsWith("game")) {
                                                this.delegate.didPressReplyMessage(this, this.currentMessageObject.messageOwner.reply_to_msg_id);
                                                n2 = (b ? 1 : 0);
                                            }
                                            else if (url.startsWith("http")) {
                                                Browser.openUrl(this.getContext(), url);
                                                n2 = (b ? 1 : 0);
                                            }
                                            else {
                                                this.delegate.needOpenUserProfile(Integer.parseInt(url));
                                                n2 = (b ? 1 : 0);
                                            }
                                        }
                                    }
                                }
                                else {
                                    this.pressedLink = null;
                                }
                                n3 = n2;
                                break Label_0599;
                            }
                        }
                        this.pressedLink = null;
                        n3 = n2;
                        break Label_0599;
                    }
                }
                this.pressedLink = null;
                n3 = n2;
            }
        }
        int onTouchEvent;
        if ((onTouchEvent = n3) == 0) {
            onTouchEvent = (super.onTouchEvent(motionEvent) ? 1 : 0);
        }
        return onTouchEvent != 0;
    }
    
    public void setCustomDate(final int customDate) {
        if (this.customDate == customDate) {
            return;
        }
        final String formatDateChat = LocaleController.formatDateChat(customDate);
        final CharSequence customText = this.customText;
        if (customText != null && TextUtils.equals((CharSequence)formatDateChat, customText)) {
            return;
        }
        this.customDate = customDate;
        this.customText = formatDateChat;
        if (this.getMeasuredWidth() != 0) {
            this.createLayout(this.customText, this.getMeasuredWidth());
            this.invalidate();
        }
        if (!this.wasLayout) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$W3vgGEA8PP4iykiyHwC5GJEFtAc(this));
        }
        else {
            this.buildLayout();
        }
    }
    
    public void setDelegate(final ChatActionCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMessageObject(MessageObject currentMessageObject) {
        if (this.currentMessageObject == currentMessageObject && (this.hasReplyMessage || currentMessageObject.replyMessageObject == null)) {
            return;
        }
        this.currentMessageObject = currentMessageObject;
        this.hasReplyMessage = (currentMessageObject.replyMessageObject != null);
        this.previousWidth = 0;
        if (this.currentMessageObject.type == 11) {
            final TLRPC.Peer to_id = currentMessageObject.messageOwner.to_id;
            int n;
            if (to_id != null) {
                n = to_id.chat_id;
                if (n == 0) {
                    n = to_id.channel_id;
                    if (n == 0) {
                        n = to_id.user_id;
                        if (n == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            n = currentMessageObject.messageOwner.from_id;
                        }
                    }
                }
            }
            else {
                n = 0;
            }
            this.avatarDrawable.setInfo(n, null, null, false);
            currentMessageObject = this.currentMessageObject;
            if (currentMessageObject.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                this.imageReceiver.setImage(null, null, this.avatarDrawable, null, currentMessageObject, 0);
            }
            else {
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(currentMessageObject.photoThumbs, AndroidUtilities.dp(64.0f));
                if (closestPhotoSizeWithSize != null) {
                    this.imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, this.currentMessageObject.photoThumbsObject), "50_50", this.avatarDrawable, null, this.currentMessageObject, 0);
                }
                else {
                    this.imageReceiver.setImageBitmap(this.avatarDrawable);
                }
            }
            this.imageReceiver.setVisible(PhotoViewer.isShowingImage(this.currentMessageObject) ^ true, false);
        }
        else {
            this.imageReceiver.setImageBitmap((Bitmap)null);
        }
        this.requestLayout();
    }
    
    public interface ChatActionCellDelegate
    {
        void didClickImage(final ChatActionCell p0);
        
        void didLongPress(final ChatActionCell p0, final float p1, final float p2);
        
        void didPressBotButton(final MessageObject p0, final TLRPC.KeyboardButton p1);
        
        void didPressReplyMessage(final ChatActionCell p0, final int p1);
        
        void needOpenUserProfile(final int p0);
    }
}
