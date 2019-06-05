// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.Checkable;
import android.support.v7.widget.AppCompatImageButton;

public class CheckableImageButton extends AppCompatImageButton implements Checkable
{
    private static final int[] DRAWABLE_STATE_CHECKED;
    private boolean checked;
    
    static {
        DRAWABLE_STATE_CHECKED = new int[] { 16842912 };
    }
    
    public CheckableImageButton(final Context context) {
        this(context, null);
    }
    
    public CheckableImageButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.imageButtonStyle);
    }
    
    public CheckableImageButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
                super.onInitializeAccessibilityEvent(view, accessibilityEvent);
                accessibilityEvent.setChecked(CheckableImageButton.this.isChecked());
            }
            
            @Override
            public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCheckable(true);
                accessibilityNodeInfoCompat.setChecked(CheckableImageButton.this.isChecked());
            }
        });
    }
    
    public boolean isChecked() {
        return this.checked;
    }
    
    public int[] onCreateDrawableState(final int n) {
        if (this.checked) {
            return mergeDrawableStates(super.onCreateDrawableState(n + CheckableImageButton.DRAWABLE_STATE_CHECKED.length), CheckableImageButton.DRAWABLE_STATE_CHECKED);
        }
        return super.onCreateDrawableState(n);
    }
    
    public void setChecked(final boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            this.refreshDrawableState();
            this.sendAccessibilityEvent(2048);
        }
    }
    
    public void toggle() {
        this.setChecked(this.checked ^ true);
    }
}
