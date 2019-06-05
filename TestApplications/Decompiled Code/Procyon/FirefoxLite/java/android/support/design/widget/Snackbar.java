// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.widget.TextView;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.widget.Button;
import android.text.TextUtils;
import android.view.View$OnClickListener;
import android.view.LayoutInflater;
import android.content.res.TypedArray;
import android.content.Context;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.support.design.snackbar.ContentViewCallback;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.R;
import android.view.accessibility.AccessibilityManager;

public final class Snackbar extends BaseTransientBottomBar<Snackbar>
{
    private static final int[] SNACKBAR_BUTTON_STYLE_ATTR;
    private final AccessibilityManager accessibilityManager;
    private boolean hasAction;
    
    static {
        SNACKBAR_BUTTON_STYLE_ATTR = new int[] { R.attr.snackbarButtonStyle };
    }
    
    private Snackbar(final ViewGroup viewGroup, final View view, final ContentViewCallback contentViewCallback) {
        super(viewGroup, view, contentViewCallback);
        this.accessibilityManager = (AccessibilityManager)viewGroup.getContext().getSystemService("accessibility");
    }
    
    private static ViewGroup findSuitableParent(View view) {
        ViewGroup viewGroup = null;
        View view2 = view;
        while (!(view2 instanceof CoordinatorLayout)) {
            ViewGroup viewGroup2 = viewGroup;
            if (view2 instanceof FrameLayout) {
                if (view2.getId() == 16908290) {
                    return (ViewGroup)view2;
                }
                viewGroup2 = (ViewGroup)view2;
            }
            if ((view = view2) != null) {
                final ViewParent parent = view2.getParent();
                if (parent instanceof View) {
                    view = (View)parent;
                }
                else {
                    view = null;
                }
            }
            viewGroup = viewGroup2;
            if ((view2 = view) == null) {
                return viewGroup2;
            }
        }
        return (ViewGroup)view2;
    }
    
    protected static boolean hasSnackbarButtonStyleAttr(final Context context) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Snackbar.SNACKBAR_BUTTON_STYLE_ATTR);
        boolean b = false;
        final int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        if (resourceId != -1) {
            b = true;
        }
        return b;
    }
    
    public static Snackbar make(final View view, final int n, final int n2) {
        return make(view, view.getResources().getText(n), n2);
    }
    
    public static Snackbar make(final View view, final CharSequence text, final int duration) {
        final ViewGroup suitableParent = findSuitableParent(view);
        if (suitableParent != null) {
            final LayoutInflater from = LayoutInflater.from(suitableParent.getContext());
            int n;
            if (hasSnackbarButtonStyleAttr(suitableParent.getContext())) {
                n = R.layout.mtrl_layout_snackbar_include;
            }
            else {
                n = R.layout.design_layout_snackbar_include;
            }
            final SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout)from.inflate(n, suitableParent, false);
            final Snackbar snackbar = new Snackbar(suitableParent, (View)snackbarContentLayout, snackbarContentLayout);
            snackbar.setText(text);
            snackbar.setDuration(duration);
            return snackbar;
        }
        throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
    }
    
    @Override
    public int getDuration() {
        int duration;
        if (this.hasAction && this.accessibilityManager.isTouchExplorationEnabled()) {
            duration = -2;
        }
        else {
            duration = super.getDuration();
        }
        return duration;
    }
    
    public Snackbar setAction(final int n, final View$OnClickListener view$OnClickListener) {
        return this.setAction(this.getContext().getText(n), view$OnClickListener);
    }
    
    public Snackbar setAction(final CharSequence text, final View$OnClickListener view$OnClickListener) {
        final Button actionView = ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView();
        if (!TextUtils.isEmpty(text) && view$OnClickListener != null) {
            this.hasAction = true;
            ((TextView)actionView).setVisibility(0);
            ((TextView)actionView).setText(text);
            ((TextView)actionView).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    view$OnClickListener.onClick(view);
                    Snackbar.this.dispatchDismiss(1);
                }
            });
        }
        else {
            ((TextView)actionView).setVisibility(8);
            ((TextView)actionView).setOnClickListener((View$OnClickListener)null);
            this.hasAction = false;
        }
        return this;
    }
    
    public Snackbar setText(final CharSequence text) {
        ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setText(text);
        return this;
    }
    
    @Override
    public void show() {
        super.show();
    }
    
    public static class Callback extends BaseCallback<Snackbar>
    {
        public void onDismissed(final Snackbar snackbar, final int n) {
        }
        
        public void onShown(final Snackbar snackbar) {
        }
    }
    
    public static final class SnackbarLayout extends SnackbarBaseLayout
    {
        public SnackbarLayout(final Context context) {
            super(context);
        }
        
        public SnackbarLayout(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        protected void onMeasure(int i, int paddingRight) {
            super.onMeasure(i, paddingRight);
            final int childCount = this.getChildCount();
            final int measuredWidth = this.getMeasuredWidth();
            final int paddingLeft = this.getPaddingLeft();
            paddingRight = this.getPaddingRight();
            View child;
            for (i = 0; i < childCount; ++i) {
                child = this.getChildAt(i);
                if (child.getLayoutParams().width == -1) {
                    child.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth - paddingLeft - paddingRight, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), 1073741824));
                }
            }
        }
    }
}
