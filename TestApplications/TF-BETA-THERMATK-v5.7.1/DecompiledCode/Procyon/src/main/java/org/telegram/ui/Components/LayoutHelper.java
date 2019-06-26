// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.widget.RelativeLayout$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.widget.FrameLayout$LayoutParams;

public class LayoutHelper
{
    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;
    
    public static FrameLayout$LayoutParams createFrame(final float n, final float n2, final int n3) {
        return new FrameLayout$LayoutParams(getSize(n), getSize(n2), n3);
    }
    
    public static FrameLayout$LayoutParams createFrame(final int n, final float n2) {
        return new FrameLayout$LayoutParams(getSize((float)n), getSize(n2));
    }
    
    public static FrameLayout$LayoutParams createFrame(final int n, final float n2, final int n3, final float n4, final float n5, final float n6, final float n7) {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(getSize((float)n), getSize(n2), n3);
        frameLayout$LayoutParams.setMargins(AndroidUtilities.dp(n4), AndroidUtilities.dp(n5), AndroidUtilities.dp(n6), AndroidUtilities.dp(n7));
        return frameLayout$LayoutParams;
    }
    
    public static FrameLayout$LayoutParams createFrame(final int n, final int n2, final int n3) {
        return new FrameLayout$LayoutParams(getSize((float)n), getSize((float)n2), n3);
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2) {
        return new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2));
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2, final float n3) {
        return new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2), n3);
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2, final float n3, final float n4, final float n5, final float n6) {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2));
        linearLayout$LayoutParams.setMargins(AndroidUtilities.dp(n3), AndroidUtilities.dp(n4), AndroidUtilities.dp(n5), AndroidUtilities.dp(n6));
        return linearLayout$LayoutParams;
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2, final float n3, final int gravity) {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2), n3);
        linearLayout$LayoutParams.gravity = gravity;
        return linearLayout$LayoutParams;
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2, final float n3, final int n4, final int n5, final int n6, final int n7) {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2), n3);
        linearLayout$LayoutParams.setMargins(AndroidUtilities.dp((float)n4), AndroidUtilities.dp((float)n5), AndroidUtilities.dp((float)n6), AndroidUtilities.dp((float)n7));
        return linearLayout$LayoutParams;
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2, final float n3, final int gravity, final int n4, final int n5, final int n6, final int n7) {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2), n3);
        linearLayout$LayoutParams.setMargins(AndroidUtilities.dp((float)n4), AndroidUtilities.dp((float)n5), AndroidUtilities.dp((float)n6), AndroidUtilities.dp((float)n7));
        linearLayout$LayoutParams.gravity = gravity;
        return linearLayout$LayoutParams;
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2, final int gravity) {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2));
        linearLayout$LayoutParams.gravity = gravity;
        return linearLayout$LayoutParams;
    }
    
    public static LinearLayout$LayoutParams createLinear(final int n, final int n2, final int gravity, final int n3, final int n4, final int n5, final int n6) {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(getSize((float)n), getSize((float)n2));
        linearLayout$LayoutParams.setMargins(AndroidUtilities.dp((float)n3), AndroidUtilities.dp((float)n4), AndroidUtilities.dp((float)n5), AndroidUtilities.dp((float)n6));
        linearLayout$LayoutParams.gravity = gravity;
        return linearLayout$LayoutParams;
    }
    
    public static RelativeLayout$LayoutParams createRelative(final float n, final float n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        return createRelative(n, n2, n3, n4, n5, n6, -1, n7, n8);
    }
    
    public static RelativeLayout$LayoutParams createRelative(final float n, final float n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9) {
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(getSize(n), getSize(n2));
        if (n7 >= 0) {
            relativeLayout$LayoutParams.addRule(n7);
        }
        if (n8 >= 0 && n9 >= 0) {
            relativeLayout$LayoutParams.addRule(n8, n9);
        }
        relativeLayout$LayoutParams.leftMargin = AndroidUtilities.dp((float)n3);
        relativeLayout$LayoutParams.topMargin = AndroidUtilities.dp((float)n4);
        relativeLayout$LayoutParams.rightMargin = AndroidUtilities.dp((float)n5);
        relativeLayout$LayoutParams.bottomMargin = AndroidUtilities.dp((float)n6);
        return relativeLayout$LayoutParams;
    }
    
    public static RelativeLayout$LayoutParams createRelative(final int n, final int n2) {
        return createRelative((float)n, (float)n2, 0, 0, 0, 0, -1, -1, -1);
    }
    
    public static RelativeLayout$LayoutParams createRelative(final int n, final int n2, final int n3) {
        return createRelative((float)n, (float)n2, 0, 0, 0, 0, n3, -1, -1);
    }
    
    public static RelativeLayout$LayoutParams createRelative(final int n, final int n2, final int n3, final int n4) {
        return createRelative((float)n, (float)n2, 0, 0, 0, 0, -1, n3, n4);
    }
    
    public static RelativeLayout$LayoutParams createRelative(final int n, final int n2, final int n3, final int n4, final int n5) {
        return createRelative((float)n, (float)n2, 0, 0, 0, 0, n3, n4, n5);
    }
    
    public static RelativeLayout$LayoutParams createRelative(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return createRelative((float)n, (float)n2, n3, n4, n5, n6, -1, -1, -1);
    }
    
    public static RelativeLayout$LayoutParams createRelative(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        return createRelative((float)n, (float)n2, n3, n4, n5, n6, n7, -1, -1);
    }
    
    public static FrameLayout$LayoutParams createScroll(final int n, final int n2, final int n3) {
        return new FrameLayout$LayoutParams(getSize((float)n), getSize((float)n2), n3);
    }
    
    public static FrameLayout$LayoutParams createScroll(final int n, final int n2, final int n3, final float n4, final float n5, final float n6, final float n7) {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(getSize((float)n), getSize((float)n2), n3);
        frameLayout$LayoutParams.leftMargin = AndroidUtilities.dp(n4);
        frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(n5);
        frameLayout$LayoutParams.rightMargin = AndroidUtilities.dp(n6);
        frameLayout$LayoutParams.bottomMargin = AndroidUtilities.dp(n7);
        return frameLayout$LayoutParams;
    }
    
    private static int getSize(float n) {
        if (n >= 0.0f) {
            n = (float)AndroidUtilities.dp(n);
        }
        return (int)n;
    }
}
