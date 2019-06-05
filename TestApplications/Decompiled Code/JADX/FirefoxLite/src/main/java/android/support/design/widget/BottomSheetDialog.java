package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.C0028R;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.p001v4.view.AccessibilityDelegateCompat;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.p004v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;

public class BottomSheetDialog extends AppCompatDialog {
    private BottomSheetBehavior<FrameLayout> behavior;
    private BottomSheetCallback bottomSheetCallback = new C06094();
    boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;
    private boolean canceledOnTouchOutsideSet;

    /* renamed from: android.support.design.widget.BottomSheetDialog$1 */
    class C00661 implements OnClickListener {
        C00661() {
        }

        public void onClick(View view) {
            if (BottomSheetDialog.this.cancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside()) {
                BottomSheetDialog.this.cancel();
            }
        }
    }

    /* renamed from: android.support.design.widget.BottomSheetDialog$3 */
    class C00673 implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }

        C00673() {
        }
    }

    /* renamed from: android.support.design.widget.BottomSheetDialog$2 */
    class C06082 extends AccessibilityDelegateCompat {
        C06082() {
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (BottomSheetDialog.this.cancelable) {
                accessibilityNodeInfoCompat.addAction(1048576);
                accessibilityNodeInfoCompat.setDismissable(true);
                return;
            }
            accessibilityNodeInfoCompat.setDismissable(false);
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != 1048576 || !BottomSheetDialog.this.cancelable) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            BottomSheetDialog.this.cancel();
            return true;
        }
    }

    /* renamed from: android.support.design.widget.BottomSheetDialog$4 */
    class C06094 extends BottomSheetCallback {
        public void onSlide(View view, float f) {
        }

        C06094() {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                BottomSheetDialog.this.cancel();
            }
        }
    }

    public BottomSheetDialog(Context context, int i) {
        super(context, getThemeResId(context, i));
        supportRequestWindowFeature(1);
    }

    public void setContentView(int i) {
        super.setContentView(wrapInBottomSheet(i, null, null));
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            if (VERSION.SDK_INT >= 21) {
                window.clearFlags(67108864);
                window.addFlags(Integer.MIN_VALUE);
            }
            window.setLayout(-1, -1);
        }
    }

    public void setContentView(View view) {
        super.setContentView(wrapInBottomSheet(0, view, null));
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        super.setContentView(wrapInBottomSheet(0, view, layoutParams));
    }

    public void setCancelable(boolean z) {
        super.setCancelable(z);
        if (this.cancelable != z) {
            this.cancelable = z;
            if (this.behavior != null) {
                this.behavior.setHideable(z);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        super.onStart();
        if (this.behavior != null && this.behavior.getState() == 5) {
            this.behavior.setState(4);
        }
    }

    public void setCanceledOnTouchOutside(boolean z) {
        super.setCanceledOnTouchOutside(z);
        if (z && !this.cancelable) {
            this.cancelable = true;
        }
        this.canceledOnTouchOutside = z;
        this.canceledOnTouchOutsideSet = true;
    }

    private View wrapInBottomSheet(int i, View view, LayoutParams layoutParams) {
        FrameLayout frameLayout = (FrameLayout) View.inflate(getContext(), C0028R.layout.design_bottom_sheet_dialog, null);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) frameLayout.findViewById(C0028R.C0027id.coordinator);
        if (i != 0 && view == null) {
            view = getLayoutInflater().inflate(i, coordinatorLayout, false);
        }
        FrameLayout frameLayout2 = (FrameLayout) coordinatorLayout.findViewById(C0028R.C0027id.design_bottom_sheet);
        this.behavior = BottomSheetBehavior.from(frameLayout2);
        this.behavior.setBottomSheetCallback(this.bottomSheetCallback);
        this.behavior.setHideable(this.cancelable);
        if (layoutParams == null) {
            frameLayout2.addView(view);
        } else {
            frameLayout2.addView(view, layoutParams);
        }
        coordinatorLayout.findViewById(C0028R.C0027id.touch_outside).setOnClickListener(new C00661());
        ViewCompat.setAccessibilityDelegate(frameLayout2, new C06082());
        frameLayout2.setOnTouchListener(new C00673());
        return frameLayout;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldWindowCloseOnTouchOutside() {
        if (!this.canceledOnTouchOutsideSet) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16843611});
            this.canceledOnTouchOutside = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
            this.canceledOnTouchOutsideSet = true;
        }
        return this.canceledOnTouchOutside;
    }

    private static int getThemeResId(Context context, int i) {
        if (i != 0) {
            return i;
        }
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(C0028R.attr.bottomSheetDialogTheme, typedValue, true)) {
            return typedValue.resourceId;
        }
        return C0028R.style.Theme_Design_Light_BottomSheetDialog;
    }
}
