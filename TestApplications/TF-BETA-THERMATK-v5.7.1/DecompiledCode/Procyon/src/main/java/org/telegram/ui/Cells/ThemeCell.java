// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.Utilities;
import java.io.FileInputStream;
import java.io.File;
import android.view.View$OnClickListener;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.FileLog;
import android.graphics.Color;
import android.graphics.Canvas;
import android.widget.ImageView$ScaleType;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.Theme;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class ThemeCell extends FrameLayout
{
    private static byte[] bytes;
    private ImageView checkImage;
    private Theme.ThemeInfo currentThemeInfo;
    private boolean isNightTheme;
    private boolean needDivider;
    private ImageView optionsButton;
    private Paint paint;
    private TextView textView;
    
    static {
        ThemeCell.bytes = new byte[1024];
    }
    
    public ThemeCell(final Context context, final boolean isNightTheme) {
        super(context);
        this.setWillNotDraw(false);
        this.isNightTheme = isNightTheme;
        this.paint = new Paint(1);
        (this.textView = new TextView(context)).setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0f));
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView.setGravity(n2 | 0x10);
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 105.0f;
        }
        else {
            n4 = 60.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 60.0f;
        }
        else {
            n5 = 105.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n4, 0.0f, n5, 0.0f));
        (this.checkImage = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
        this.checkImage.setImageResource(2131165858);
        if (!this.isNightTheme) {
            final ImageView checkImage = this.checkImage;
            int n6;
            if (LocaleController.isRTL) {
                n6 = 3;
            }
            else {
                n6 = 5;
            }
            this.addView((View)checkImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(19, 14.0f, n6 | 0x10, 59.0f, 0.0f, 59.0f, 0.0f));
            (this.optionsButton = new ImageView(context)).setFocusable(false);
            this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
            this.optionsButton.setImageResource(2131165416);
            this.optionsButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("stickers_menu"), PorterDuff$Mode.MULTIPLY));
            this.optionsButton.setScaleType(ImageView$ScaleType.CENTER);
            this.optionsButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
            final ImageView optionsButton = this.optionsButton;
            if (LocaleController.isRTL) {
                n = 3;
            }
            this.addView((View)optionsButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, n | 0x30));
        }
        else {
            final ImageView checkImage2 = this.checkImage;
            if (LocaleController.isRTL) {
                n = 3;
            }
            this.addView((View)checkImage2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(19, 14.0f, n | 0x10, 21.0f, 0.0f, 21.0f, 0.0f));
        }
    }
    
    public Theme.ThemeInfo getCurrentThemeInfo() {
        return this.currentThemeInfo;
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.checkImage.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
        this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            final int color = Theme.dividerPaint.getColor();
            final int alpha = Color.alpha(color);
            int dp = 0;
            FileLog.d(String.format("set color %d %d %d %d", alpha, Color.red(color), Color.green(color), Color.blue(color)));
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp(20.0f);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(20.0f);
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
        int dp2 = AndroidUtilities.dp(31.0f);
        if (LocaleController.isRTL) {
            dp2 = this.getWidth() - dp2;
        }
        canvas.drawCircle((float)dp2, (float)AndroidUtilities.dp(24.0f), (float)AndroidUtilities.dp(11.0f), this.paint);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.setSelected(this.checkImage.getVisibility() == 0);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setOnOptionsClick(final View$OnClickListener onClickListener) {
        this.optionsButton.setOnClickListener(onClickListener);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
    
    public void setTheme(Theme.ThemeInfo currentThemeInfo, final boolean needDivider) {
        this.currentThemeInfo = (Theme.ThemeInfo)currentThemeInfo;
        final String name = ((Theme.ThemeInfo)currentThemeInfo).getName();
        final boolean endsWith = name.endsWith(".attheme");
        int n = 0;
        int color = 0;
        String substring = name;
        if (endsWith) {
            substring = name.substring(0, name.lastIndexOf(46));
        }
        this.textView.setText((CharSequence)substring);
        this.needDivider = needDivider;
        this.updateCurrentThemeCheck();
        Label_0612: {
            if (((Theme.ThemeInfo)currentThemeInfo).pathToFile != null || ((Theme.ThemeInfo)currentThemeInfo).assetName != null) {
                final Throwable t = null;
                Object substring2;
                final Object o = substring2 = null;
                String s = null;
                Throwable t2 = null;
                Label_0631: {
                    Throwable t3 = null;
                    Label_0585: {
                        try {
                            try {
                                File assetFile;
                                if (((Theme.ThemeInfo)currentThemeInfo).assetName != null) {
                                    substring2 = o;
                                    assetFile = Theme.getAssetFile(((Theme.ThemeInfo)currentThemeInfo).assetName);
                                }
                                else {
                                    substring2 = o;
                                    assetFile = new File(((Theme.ThemeInfo)currentThemeInfo).pathToFile);
                                }
                                substring2 = o;
                                currentThemeInfo = new(java.io.FileInputStream.class);
                                substring2 = o;
                                new FileInputStream(assetFile);
                                int n2 = 0;
                                n = 0;
                                color = 0;
                                while (true) {
                                    int n3 = color;
                                    try {
                                        final int read = ((FileInputStream)currentThemeInfo).read(ThemeCell.bytes);
                                        n3 = color;
                                        if (read != -1) {
                                            int n4 = n2;
                                            int i = 0;
                                            int offset = 0;
                                            while (i < read) {
                                                int n5 = n;
                                                int n6 = offset;
                                                int n7 = n4;
                                                n3 = color;
                                                if (ThemeCell.bytes[i] == 10) {
                                                    ++n;
                                                    final int n8 = i - offset + 1;
                                                    n3 = color;
                                                    substring2 = new(java.lang.String.class);
                                                    n3 = color;
                                                    new String(ThemeCell.bytes, offset, n8 - 1, "UTF-8");
                                                    n3 = color;
                                                    if (((String)substring2).startsWith("WPS")) {
                                                        break;
                                                    }
                                                    n3 = color;
                                                    final int index = ((String)substring2).indexOf(61);
                                                    if (index != -1) {
                                                        n3 = color;
                                                        if (((String)substring2).substring(0, index).equals("actionBarDefault")) {
                                                            n3 = color;
                                                            substring2 = ((String)substring2).substring(index + 1);
                                                            n3 = color;
                                                            Label_0415: {
                                                                if (((String)substring2).length() > 0) {
                                                                    n3 = color;
                                                                    if (((String)substring2).charAt(0) == '#') {
                                                                        n3 = color;
                                                                        try {
                                                                            color = Color.parseColor((String)substring2);
                                                                        }
                                                                        catch (Exception ex2) {
                                                                            n3 = color;
                                                                            color = Utilities.parseInt((String)substring2);
                                                                        }
                                                                        break Label_0415;
                                                                    }
                                                                }
                                                                n3 = color;
                                                                color = Utilities.parseInt((String)substring2);
                                                                try {
                                                                    this.paint.setColor(color);
                                                                    color = 1;
                                                                    break;
                                                                }
                                                                catch (Throwable t4) {
                                                                    color = 1;
                                                                    break Label_0585;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    n6 = offset + n8;
                                                    n7 = n4 + n8;
                                                    n5 = n;
                                                }
                                                ++i;
                                                n = n5;
                                                offset = n6;
                                                n4 = n7;
                                            }
                                            n3 = color;
                                            if (n2 != n4) {
                                                if (n >= 500) {
                                                    n3 = color;
                                                }
                                                else {
                                                    n3 = color;
                                                    ((FileInputStream)currentThemeInfo).getChannel().position(n4);
                                                    if (color == 0) {
                                                        n2 = n4;
                                                        continue;
                                                    }
                                                    n3 = color;
                                                }
                                            }
                                        }
                                        n = n3;
                                        try {
                                            ((FileInputStream)currentThemeInfo).close();
                                            n = n3;
                                            break Label_0612;
                                        }
                                        catch (Exception currentThemeInfo) {
                                            FileLog.e(currentThemeInfo);
                                            break Label_0612;
                                        }
                                    }
                                    catch (Throwable t5) {
                                        color = n3;
                                    }
                                }
                            }
                            finally {
                                s = (String)substring2;
                                t2 = t3;
                            }
                            break Label_0631;
                        }
                        catch (Throwable s) {
                            t3 = t;
                        }
                    }
                    FileLog.e((Throwable)s);
                    n = color;
                    if (t3 != null) {
                        ((FileInputStream)t3).close();
                        n = color;
                    }
                    break Label_0612;
                }
                if (s != null) {
                    try {
                        ((FileInputStream)s).close();
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                throw t2;
            }
        }
        if (n == 0) {
            this.paint.setColor(Theme.getDefaultColor("actionBarDefault"));
        }
    }
    
    public void updateCurrentThemeCheck() {
        Theme.ThemeInfo themeInfo;
        if (this.isNightTheme) {
            themeInfo = Theme.getCurrentNightTheme();
        }
        else {
            themeInfo = Theme.getCurrentTheme();
        }
        int visibility;
        if (this.currentThemeInfo == themeInfo) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        if (this.checkImage.getVisibility() != visibility) {
            this.checkImage.setVisibility(visibility);
        }
    }
}
