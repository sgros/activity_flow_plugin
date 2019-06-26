// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextDirectionHeuristics;
import android.text.TextDirectionHeuristic;
import org.telegram.messenger.FileLog;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.AndroidUtilities;
import android.text.StaticLayout$Builder;
import android.os.Build$VERSION;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import android.text.Layout$Alignment;
import android.text.TextPaint;
import android.text.StaticLayout;
import java.lang.reflect.Constructor;

public class StaticLayoutEx
{
    private static final String TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics";
    private static final String TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic";
    private static final String TEXT_DIR_FIRSTSTRONG_LTR = "FIRSTSTRONG_LTR";
    private static boolean initialized;
    private static Constructor<StaticLayout> sConstructor;
    private static Object[] sConstructorArgs;
    private static Object sTextDirection;
    
    public static StaticLayout createStaticLayout(CharSequence ellipsize, int breakStrategy, int n, final TextPaint textPaint, final int n2, final Layout$Alignment layout$Alignment, final float n3, final float n4, final boolean b, final TextUtils$TruncateAt textUtils$TruncateAt, final int n5, final int n6, final boolean b2) {
        while (true) {
            if (n6 == 1) {
                final float n7 = (float)n5;
                Label_0371: {
                    try {
                        ellipsize = TextUtils.ellipsize((CharSequence)ellipsize, textPaint, n7, TextUtils$TruncateAt.END);
                        breakStrategy = ((CharSequence)ellipsize).length();
                        try {
                            return new StaticLayout((CharSequence)ellipsize, 0, breakStrategy, textPaint, n2, layout$Alignment, n3, n4, b);
                        }
                        catch (Exception ellipsize) {
                            break Label_0371;
                        }
                        Label_0147: {
                            if (Build$VERSION.SDK_INT >= 23) {
                                final StaticLayout build = StaticLayout$Builder.obtain((CharSequence)ellipsize, 0, ((CharSequence)ellipsize).length(), textPaint, n2).setAlignment(layout$Alignment).setLineSpacing(n4, n3).setIncludePad(b).setEllipsize((TextUtils$TruncateAt)null).setEllipsizedWidth(n5).setMaxLines(n6).setBreakStrategy(1).setHyphenationFrequency(0).build();
                                break Label_0147;
                            }
                            try {
                                final StaticLayout build = new StaticLayout((CharSequence)ellipsize, textPaint, n2, layout$Alignment, n3, n4, b);
                                if (build.getLineCount() <= n6) {
                                    return build;
                                }
                                breakStrategy = n6 - 1;
                                final float lineLeft = build.getLineLeft(breakStrategy);
                                final float lineWidth = build.getLineWidth(breakStrategy);
                                if (lineLeft != 0.0f) {
                                    breakStrategy = build.getOffsetForHorizontal(breakStrategy, lineLeft);
                                }
                                else {
                                    breakStrategy = build.getOffsetForHorizontal(breakStrategy, lineWidth);
                                }
                                n = breakStrategy;
                                if (lineWidth < n5 - AndroidUtilities.dp(10.0f)) {
                                    n = breakStrategy + 3;
                                }
                                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(((CharSequence)ellipsize).subSequence(0, Math.max(0, n - 3)));
                                spannableStringBuilder.append((CharSequence)"\u2026");
                                if (Build$VERSION.SDK_INT >= 23) {
                                    final StaticLayout$Builder setMaxLines = StaticLayout$Builder.obtain((CharSequence)spannableStringBuilder, 0, spannableStringBuilder.length(), textPaint, n2).setAlignment(layout$Alignment).setLineSpacing(n4, n3).setIncludePad(b).setEllipsize(TextUtils$TruncateAt.END).setEllipsizedWidth(n5).setMaxLines(n6);
                                    if (b2) {
                                        breakStrategy = 1;
                                    }
                                    else {
                                        breakStrategy = 0;
                                    }
                                    return setMaxLines.setBreakStrategy(breakStrategy).setHyphenationFrequency(0).build();
                                }
                                return new StaticLayout((CharSequence)spannableStringBuilder, textPaint, n2, layout$Alignment, n3, n4, b);
                            }
                            catch (Exception ellipsize) {}
                        }
                    }
                    catch (Exception ex) {}
                }
                FileLog.e((Throwable)ellipsize);
                return null;
            }
            continue;
        }
    }
    
    public static StaticLayout createStaticLayout(final CharSequence charSequence, final TextPaint textPaint, final int n, final Layout$Alignment layout$Alignment, final float n2, final float n3, final boolean b, final TextUtils$TruncateAt textUtils$TruncateAt, final int n4, final int n5) {
        return createStaticLayout(charSequence, 0, charSequence.length(), textPaint, n, layout$Alignment, n2, n3, b, textUtils$TruncateAt, n4, n5, true);
    }
    
    public static StaticLayout createStaticLayout(final CharSequence charSequence, final TextPaint textPaint, final int n, final Layout$Alignment layout$Alignment, final float n2, final float n3, final boolean b, final TextUtils$TruncateAt textUtils$TruncateAt, final int n4, final int n5, final boolean b2) {
        return createStaticLayout(charSequence, 0, charSequence.length(), textPaint, n, layout$Alignment, n2, n3, b, textUtils$TruncateAt, n4, n5, b2);
    }
    
    public static StaticLayout createStaticLayout2(final CharSequence charSequence, final TextPaint textPaint, final int n, final Layout$Alignment alignment, final float n2, final float n3, final boolean includePad, final TextUtils$TruncateAt textUtils$TruncateAt, final int ellipsizedWidth, final int maxLines) {
        if (Build$VERSION.SDK_INT >= 23) {
            return StaticLayout$Builder.obtain(charSequence, 0, charSequence.length(), textPaint, ellipsizedWidth).setAlignment(alignment).setLineSpacing(n3, n2).setIncludePad(includePad).setEllipsize(TextUtils$TruncateAt.END).setEllipsizedWidth(ellipsizedWidth).setMaxLines(maxLines).setBreakStrategy(1).setHyphenationFrequency(0).build();
        }
        return createStaticLayout(charSequence, 0, charSequence.length(), textPaint, n, alignment, n2, n3, includePad, textUtils$TruncateAt, ellipsizedWidth, maxLines, true);
    }
    
    public static void init() {
        if (StaticLayoutEx.initialized) {
            return;
        }
        try {
            Class<?> loadClass;
            if (Build$VERSION.SDK_INT >= 18) {
                loadClass = TextDirectionHeuristic.class;
                StaticLayoutEx.sTextDirection = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            }
            else {
                final ClassLoader classLoader = StaticLayoutEx.class.getClassLoader();
                loadClass = classLoader.loadClass("android.text.TextDirectionHeuristic");
                final Class<?> loadClass2 = classLoader.loadClass("android.text.TextDirectionHeuristics");
                StaticLayoutEx.sTextDirection = loadClass2.getField("FIRSTSTRONG_LTR").get(loadClass2);
            }
            final Class[] parameterTypes = { CharSequence.class, Integer.TYPE, Integer.TYPE, TextPaint.class, Integer.TYPE, Layout$Alignment.class, loadClass, Float.TYPE, Float.TYPE, Boolean.TYPE, TextUtils$TruncateAt.class, Integer.TYPE, Integer.TYPE };
            (StaticLayoutEx.sConstructor = StaticLayout.class.getDeclaredConstructor((Class<?>[])parameterTypes)).setAccessible(true);
            StaticLayoutEx.sConstructorArgs = new Object[parameterTypes.length];
            StaticLayoutEx.initialized = true;
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
}
