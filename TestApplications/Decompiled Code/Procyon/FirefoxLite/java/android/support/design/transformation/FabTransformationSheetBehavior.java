// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.transformation;

import android.support.design.animation.Positioning;
import android.support.design.animation.MotionSpec;
import android.support.design.R;
import android.view.ViewParent;
import android.support.v4.view.ViewCompat;
import java.util.HashMap;
import android.os.Build$VERSION;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.util.Map;

public class FabTransformationSheetBehavior extends FabTransformationBehavior
{
    private Map<View, Integer> importantForAccessibilityMap;
    
    public FabTransformationSheetBehavior() {
    }
    
    public FabTransformationSheetBehavior(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    private void updateImportantForAccessibility(final View view, final boolean b) {
        final ViewParent parent = view.getParent();
        if (!(parent instanceof CoordinatorLayout)) {
            return;
        }
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)parent;
        final int childCount = coordinatorLayout.getChildCount();
        if (Build$VERSION.SDK_INT >= 16 && b) {
            this.importantForAccessibilityMap = new HashMap<View, Integer>(childCount);
        }
        for (int i = 0; i < childCount; ++i) {
            final View child = coordinatorLayout.getChildAt(i);
            final boolean b2 = child.getLayoutParams() instanceof LayoutParams && ((LayoutParams)child.getLayoutParams()).getBehavior() instanceof FabTransformationScrimBehavior;
            if (child != view) {
                if (!b2) {
                    if (!b) {
                        if (this.importantForAccessibilityMap != null && this.importantForAccessibilityMap.containsKey(child)) {
                            ViewCompat.setImportantForAccessibility(child, this.importantForAccessibilityMap.get(child));
                        }
                    }
                    else {
                        if (Build$VERSION.SDK_INT >= 16) {
                            this.importantForAccessibilityMap.put(child, child.getImportantForAccessibility());
                        }
                        ViewCompat.setImportantForAccessibility(child, 4);
                    }
                }
            }
        }
        if (!b) {
            this.importantForAccessibilityMap = null;
        }
    }
    
    @Override
    protected FabTransformationSpec onCreateMotionSpec(final Context context, final boolean b) {
        int n;
        if (b) {
            n = R.animator.mtrl_fab_transformation_sheet_expand_spec;
        }
        else {
            n = R.animator.mtrl_fab_transformation_sheet_collapse_spec;
        }
        final FabTransformationSpec fabTransformationSpec = new FabTransformationSpec();
        fabTransformationSpec.timings = MotionSpec.createFromResource(context, n);
        fabTransformationSpec.positioning = new Positioning(17, 0.0f, 0.0f);
        return fabTransformationSpec;
    }
    
    @Override
    protected boolean onExpandedStateChange(final View view, final View view2, final boolean b, final boolean b2) {
        this.updateImportantForAccessibility(view2, b);
        return super.onExpandedStateChange(view, view2, b, b2);
    }
}
