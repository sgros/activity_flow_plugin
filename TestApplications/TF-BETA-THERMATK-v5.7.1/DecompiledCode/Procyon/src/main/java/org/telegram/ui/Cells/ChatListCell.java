// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.Paint;
import org.telegram.messenger.LocaleController;
import android.graphics.Color;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import org.telegram.messenger.SharedConfig;
import android.text.TextPaint;
import android.graphics.RectF;
import org.telegram.ui.Components.RadioButton;
import android.widget.FrameLayout;
import android.view.View$MeasureSpec;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.LinearLayout;

public class ChatListCell extends LinearLayout
{
    private ListView[] listView;
    
    public ChatListCell(final Context context) {
        super(context);
        this.listView = new ListView[2];
        this.setOrientation(0);
        this.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(21.0f), 0);
        for (int i = 0; i < this.listView.length; ++i) {
            final boolean b = i == 1;
            this.listView[i] = new ListView(context, b);
            final ListView listView = this.listView[i];
            int n;
            if (i == 1) {
                n = 10;
            }
            else {
                n = 0;
            }
            this.addView((View)listView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1, 0.5f, n, 0, 0, 0));
            this.listView[i].setOnClickListener((View$OnClickListener)new _$$Lambda$ChatListCell$iynS8JCANgqC1uqLhRG8bhggO74(this, b));
        }
    }
    
    protected void didSelectChatType(final boolean b) {
    }
    
    public void invalidate() {
        super.invalidate();
        int n = 0;
        while (true) {
            final ListView[] listView = this.listView;
            if (n >= listView.length) {
                break;
            }
            listView[n].invalidate();
            ++n;
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(123.0f), 1073741824));
    }
    
    private class ListView extends FrameLayout
    {
        private RadioButton button;
        private boolean isThreeLines;
        private RectF rect;
        private TextPaint textPaint;
        
        public ListView(final Context context, final boolean isThreeLines) {
            super(context);
            this.rect = new RectF();
            final boolean b = true;
            this.textPaint = new TextPaint(1);
            this.setWillNotDraw(false);
            this.isThreeLines = isThreeLines;
            this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0f));
            (this.button = new RadioButton(context) {
                public void invalidate() {
                    super.invalidate();
                    ListView.this.invalidate();
                }
            }).setSize(AndroidUtilities.dp(20.0f));
            this.addView((View)this.button, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, 53, 0.0f, 26.0f, 10.0f, 0.0f));
            final RadioButton button = this.button;
            boolean b2 = false;
            Label_0150: {
                if (this.isThreeLines) {
                    b2 = b;
                    if (SharedConfig.useThreeLinesLayout) {
                        break Label_0150;
                    }
                }
                b2 = (!this.isThreeLines && !SharedConfig.useThreeLinesLayout && b);
            }
            button.setChecked(b2, false);
        }
        
        protected void onDraw(final Canvas canvas) {
            final int color = Theme.getColor("switchTrack");
            final int red = Color.red(color);
            final int green = Color.green(color);
            final int blue = Color.blue(color);
            this.button.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
            this.rect.set((float)AndroidUtilities.dp(1.0f), (float)AndroidUtilities.dp(1.0f), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(1.0f)), (float)AndroidUtilities.dp(73.0f));
            Theme.chat_instantViewRectPaint.setColor(Color.argb((int)(this.button.getProgress() * 43.0f), red, green, blue));
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0f), (float)AndroidUtilities.dp(6.0f), Theme.chat_instantViewRectPaint);
            this.rect.set(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(74.0f));
            Theme.dialogs_onlineCirclePaint.setColor(Color.argb((int)((1.0f - this.button.getProgress()) * 31.0f), red, green, blue));
            canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0f), (float)AndroidUtilities.dp(6.0f), Theme.dialogs_onlineCirclePaint);
            int n;
            String s;
            if (this.isThreeLines) {
                n = 2131559041;
                s = "ChatListExpanded";
            }
            else {
                n = 2131559040;
                s = "ChatListDefault";
            }
            final String string = LocaleController.getString(s, n);
            final int n2 = (int)Math.ceil(this.textPaint.measureText(string));
            this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            canvas.drawText(string, (float)((this.getMeasuredWidth() - n2) / 2), (float)AndroidUtilities.dp(96.0f), (Paint)this.textPaint);
            for (int i = 0; i < 2; ++i) {
                float n3;
                if (i == 0) {
                    n3 = 21.0f;
                }
                else {
                    n3 = 53.0f;
                }
                final int dp = AndroidUtilities.dp(n3);
                final Paint dialogs_onlineCirclePaint = Theme.dialogs_onlineCirclePaint;
                int n4;
                if (i == 0) {
                    n4 = 204;
                }
                else {
                    n4 = 90;
                }
                dialogs_onlineCirclePaint.setColor(Color.argb(n4, red, green, blue));
                canvas.drawCircle((float)AndroidUtilities.dp(22.0f), (float)dp, (float)AndroidUtilities.dp(11.0f), Theme.dialogs_onlineCirclePaint);
                int n5 = 0;
                while (true) {
                    int n6;
                    if (this.isThreeLines) {
                        n6 = 3;
                    }
                    else {
                        n6 = 2;
                    }
                    if (n5 >= n6) {
                        break;
                    }
                    final Paint dialogs_onlineCirclePaint2 = Theme.dialogs_onlineCirclePaint;
                    int n7;
                    if (n5 == 0) {
                        n7 = 204;
                    }
                    else {
                        n7 = 90;
                    }
                    dialogs_onlineCirclePaint2.setColor(Color.argb(n7, red, green, blue));
                    final boolean isThreeLines = this.isThreeLines;
                    float n8 = 72.0f;
                    if (isThreeLines) {
                        final RectF rect = this.rect;
                        final float n9 = (float)AndroidUtilities.dp(41.0f);
                        final float n10 = (float)(n5 * 7);
                        final float n11 = (float)(dp - AndroidUtilities.dp(8.3f - n10));
                        final int measuredWidth = this.getMeasuredWidth();
                        if (n5 != 0) {
                            n8 = 48.0f;
                        }
                        rect.set(n9, n11, (float)(measuredWidth - AndroidUtilities.dp(n8)), (float)(dp - AndroidUtilities.dp(5.3f - n10)));
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dpf2(1.5f), AndroidUtilities.dpf2(1.5f), Theme.dialogs_onlineCirclePaint);
                    }
                    else {
                        final RectF rect2 = this.rect;
                        final float n12 = (float)AndroidUtilities.dp(41.0f);
                        final int n13 = n5 * 10;
                        final float n14 = (float)(dp - AndroidUtilities.dp((float)(7 - n13)));
                        final int measuredWidth2 = this.getMeasuredWidth();
                        if (n5 != 0) {
                            n8 = 48.0f;
                        }
                        rect2.set(n12, n14, (float)(measuredWidth2 - AndroidUtilities.dp(n8)), (float)(dp - AndroidUtilities.dp((float)(3 - n13))));
                        canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                    }
                    ++n5;
                }
            }
        }
    }
}
