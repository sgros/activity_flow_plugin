// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.content.res.TypedArray;
import android.view.Window;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup$LayoutParams;
import android.support.design.R;
import android.util.TypedValue;
import android.view.View;
import android.content.Context;
import android.widget.FrameLayout;
import android.support.v7.app.AppCompatDialog;

public class BottomSheetDialog extends AppCompatDialog
{
    private BottomSheetBehavior<FrameLayout> behavior;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback;
    boolean cancelable;
    private boolean canceledOnTouchOutside;
    private boolean canceledOnTouchOutsideSet;
    
    public BottomSheetDialog(final Context context, final int n) {
        super(context, getThemeResId(context, n));
        this.cancelable = true;
        this.canceledOnTouchOutside = true;
        this.bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onSlide(final View view, final float n) {
            }
            
            @Override
            public void onStateChanged(final View view, final int n) {
                if (n == 5) {
                    BottomSheetDialog.this.cancel();
                }
            }
        };
        this.supportRequestWindowFeature(1);
    }
    
    private static int getThemeResId(final Context context, final int n) {
        int n2 = n;
        if (n == 0) {
            final TypedValue typedValue = new TypedValue();
            if (context.getTheme().resolveAttribute(R.attr.bottomSheetDialogTheme, typedValue, true)) {
                n2 = typedValue.resourceId;
            }
            else {
                n2 = R.style.Theme_Design_Light_BottomSheetDialog;
            }
        }
        return n2;
    }
    
    private View wrapInBottomSheet(final int n, final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        final FrameLayout frameLayout = (FrameLayout)View.inflate(this.getContext(), R.layout.design_bottom_sheet_dialog, (ViewGroup)null);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)frameLayout.findViewById(R.id.coordinator);
        View inflate = view;
        if (n != 0 && (inflate = view) == null) {
            inflate = this.getLayoutInflater().inflate(n, (ViewGroup)coordinatorLayout, false);
        }
        final FrameLayout frameLayout2 = (FrameLayout)coordinatorLayout.findViewById(R.id.design_bottom_sheet);
        (this.behavior = BottomSheetBehavior.from(frameLayout2)).setBottomSheetCallback(this.bottomSheetCallback);
        this.behavior.setHideable(this.cancelable);
        if (viewGroup$LayoutParams == null) {
            frameLayout2.addView(inflate);
        }
        else {
            frameLayout2.addView(inflate, viewGroup$LayoutParams);
        }
        coordinatorLayout.findViewById(R.id.touch_outside).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (BottomSheetDialog.this.cancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside()) {
                    BottomSheetDialog.this.cancel();
                }
            }
        });
        ViewCompat.setAccessibilityDelegate((View)frameLayout2, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                if (BottomSheetDialog.this.cancelable) {
                    accessibilityNodeInfoCompat.addAction(1048576);
                    accessibilityNodeInfoCompat.setDismissable(true);
                }
                else {
                    accessibilityNodeInfoCompat.setDismissable(false);
                }
            }
            
            @Override
            public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
                if (n == 1048576 && BottomSheetDialog.this.cancelable) {
                    BottomSheetDialog.this.cancel();
                    return true;
                }
                return super.performAccessibilityAction(view, n, bundle);
            }
        });
        frameLayout2.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return true;
            }
        });
        return (View)frameLayout;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final Window window = this.getWindow();
        if (window != null) {
            if (Build$VERSION.SDK_INT >= 21) {
                window.clearFlags(67108864);
                window.addFlags(Integer.MIN_VALUE);
            }
            window.setLayout(-1, -1);
        }
    }
    
    protected void onStart() {
        super.onStart();
        if (this.behavior != null && this.behavior.getState() == 5) {
            this.behavior.setState(4);
        }
    }
    
    public void setCancelable(final boolean hideable) {
        super.setCancelable(hideable);
        if (this.cancelable != hideable) {
            this.cancelable = hideable;
            if (this.behavior != null) {
                this.behavior.setHideable(hideable);
            }
        }
    }
    
    public void setCanceledOnTouchOutside(final boolean b) {
        super.setCanceledOnTouchOutside(b);
        if (b && !this.cancelable) {
            this.cancelable = true;
        }
        this.canceledOnTouchOutside = b;
        this.canceledOnTouchOutsideSet = true;
    }
    
    @Override
    public void setContentView(final int n) {
        super.setContentView(this.wrapInBottomSheet(n, null, null));
    }
    
    @Override
    public void setContentView(final View view) {
        super.setContentView(this.wrapInBottomSheet(0, view, null));
    }
    
    @Override
    public void setContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super.setContentView(this.wrapInBottomSheet(0, view, viewGroup$LayoutParams));
    }
    
    boolean shouldWindowCloseOnTouchOutside() {
        if (!this.canceledOnTouchOutsideSet) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(new int[] { 16843611 });
            this.canceledOnTouchOutside = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
            this.canceledOnTouchOutsideSet = true;
        }
        return this.canceledOnTouchOutside;
    }
}
