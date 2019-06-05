package android.support.design.widget;

import android.content.Context;
import android.support.p001v4.view.AccessibilityDelegateCompat;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.p004v7.appcompat.C0187R;
import android.support.p004v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;

public class CheckableImageButton extends AppCompatImageButton implements Checkable {
    private static final int[] DRAWABLE_STATE_CHECKED = new int[]{16842912};
    private boolean checked;

    /* renamed from: android.support.design.widget.CheckableImageButton$1 */
    class C06101 extends AccessibilityDelegateCompat {
        C06101() {
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setChecked(CheckableImageButton.this.isChecked());
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setCheckable(true);
            accessibilityNodeInfoCompat.setChecked(CheckableImageButton.this.isChecked());
        }
    }

    public CheckableImageButton(Context context) {
        this(context, null);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0187R.attr.imageButtonStyle);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        ViewCompat.setAccessibilityDelegate(this, new C06101());
    }

    public void setChecked(boolean z) {
        if (this.checked != z) {
            this.checked = z;
            refreshDrawableState();
            sendAccessibilityEvent(2048);
        }
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void toggle() {
        setChecked(this.checked ^ 1);
    }

    public int[] onCreateDrawableState(int i) {
        if (this.checked) {
            return mergeDrawableStates(super.onCreateDrawableState(i + DRAWABLE_STATE_CHECKED.length), DRAWABLE_STATE_CHECKED);
        }
        return super.onCreateDrawableState(i);
    }
}
