// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.ripple;

import android.annotation.TargetApi;
import android.support.v4.graphics.ColorUtils;
import android.graphics.Color;
import android.util.StateSet;
import android.content.res.ColorStateList;
import android.os.Build$VERSION;

public class RippleUtils
{
    private static final int[] FOCUSED_STATE_SET;
    private static final int[] HOVERED_FOCUSED_STATE_SET;
    private static final int[] HOVERED_STATE_SET;
    private static final int[] PRESSED_STATE_SET;
    private static final int[] SELECTED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_STATE_SET;
    private static final int[] SELECTED_PRESSED_STATE_SET;
    private static final int[] SELECTED_STATE_SET;
    public static final boolean USE_FRAMEWORK_RIPPLE;
    
    static {
        USE_FRAMEWORK_RIPPLE = (Build$VERSION.SDK_INT >= 21);
        PRESSED_STATE_SET = new int[] { 16842919 };
        HOVERED_FOCUSED_STATE_SET = new int[] { 16843623, 16842908 };
        FOCUSED_STATE_SET = new int[] { 16842908 };
        HOVERED_STATE_SET = new int[] { 16843623 };
        SELECTED_PRESSED_STATE_SET = new int[] { 16842913, 16842919 };
        SELECTED_HOVERED_FOCUSED_STATE_SET = new int[] { 16842913, 16843623, 16842908 };
        SELECTED_FOCUSED_STATE_SET = new int[] { 16842913, 16842908 };
        SELECTED_HOVERED_STATE_SET = new int[] { 16842913, 16843623 };
        SELECTED_STATE_SET = new int[] { 16842913 };
    }
    
    public static ColorStateList convertToRippleDrawableColor(final ColorStateList list) {
        if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            return new ColorStateList(new int[][] { RippleUtils.SELECTED_STATE_SET, StateSet.NOTHING }, new int[] { getColorForState(list, RippleUtils.SELECTED_PRESSED_STATE_SET), getColorForState(list, RippleUtils.PRESSED_STATE_SET) });
        }
        return new ColorStateList(new int[][] { RippleUtils.SELECTED_PRESSED_STATE_SET, RippleUtils.SELECTED_HOVERED_FOCUSED_STATE_SET, RippleUtils.SELECTED_FOCUSED_STATE_SET, RippleUtils.SELECTED_HOVERED_STATE_SET, RippleUtils.SELECTED_STATE_SET, RippleUtils.PRESSED_STATE_SET, RippleUtils.HOVERED_FOCUSED_STATE_SET, RippleUtils.FOCUSED_STATE_SET, RippleUtils.HOVERED_STATE_SET, StateSet.NOTHING }, new int[] { getColorForState(list, RippleUtils.SELECTED_PRESSED_STATE_SET), getColorForState(list, RippleUtils.SELECTED_HOVERED_FOCUSED_STATE_SET), getColorForState(list, RippleUtils.SELECTED_FOCUSED_STATE_SET), getColorForState(list, RippleUtils.SELECTED_HOVERED_STATE_SET), 0, getColorForState(list, RippleUtils.PRESSED_STATE_SET), getColorForState(list, RippleUtils.HOVERED_FOCUSED_STATE_SET), getColorForState(list, RippleUtils.FOCUSED_STATE_SET), getColorForState(list, RippleUtils.HOVERED_STATE_SET), 0 });
    }
    
    @TargetApi(21)
    private static int doubleAlpha(final int n) {
        return ColorUtils.setAlphaComponent(n, Math.min(Color.alpha(n) * 2, 255));
    }
    
    private static int getColorForState(final ColorStateList list, final int[] array) {
        int colorForState;
        if (list != null) {
            colorForState = list.getColorForState(array, list.getDefaultColor());
        }
        else {
            colorForState = 0;
        }
        int doubleAlpha = colorForState;
        if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            doubleAlpha = doubleAlpha(colorForState);
        }
        return doubleAlpha;
    }
}
