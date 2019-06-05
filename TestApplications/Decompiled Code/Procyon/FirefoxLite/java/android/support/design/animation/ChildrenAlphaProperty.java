// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.support.design.R;
import android.view.ViewGroup;
import android.util.Property;

public class ChildrenAlphaProperty extends Property<ViewGroup, Float>
{
    public static final Property<ViewGroup, Float> CHILDREN_ALPHA;
    
    static {
        CHILDREN_ALPHA = new ChildrenAlphaProperty("childrenAlpha");
    }
    
    private ChildrenAlphaProperty(final String s) {
        super((Class)Float.class, s);
    }
    
    public Float get(final ViewGroup viewGroup) {
        final Float n = (Float)viewGroup.getTag(R.id.mtrl_internal_children_alpha_tag);
        if (n != null) {
            return n;
        }
        return 1.0f;
    }
    
    public void set(final ViewGroup viewGroup, final Float n) {
        final float floatValue = n;
        viewGroup.setTag(R.id.mtrl_internal_children_alpha_tag, (Object)floatValue);
        for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
            viewGroup.getChildAt(i).setAlpha(floatValue);
        }
    }
}
