package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.app.ActionBar$Tab;
import androidx.appcompat.view.ActionBarPolicy;

public class ScrollingTabContainerView extends HorizontalScrollView implements OnItemSelectedListener {
    private static final Interpolator sAlphaInterpolator = new DecelerateInterpolator();
    private boolean mAllowCollapse;
    private int mContentHeight;
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    private TabClickListener mTabClickListener;
    LinearLayoutCompat mTabLayout;
    Runnable mTabSelector;
    private Spinner mTabSpinner;

    private class TabAdapter extends BaseAdapter {
        public long getItemId(int i) {
            return (long) i;
        }

        TabAdapter() {
        }

        public int getCount() {
            return ScrollingTabContainerView.this.mTabLayout.getChildCount();
        }

        public Object getItem(int i) {
            return ((TabView) ScrollingTabContainerView.this.mTabLayout.getChildAt(i)).getTab();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                return ScrollingTabContainerView.this.createTabView((ActionBar$Tab) getItem(i), true);
            }
            ((TabView) view).bindTab((ActionBar$Tab) getItem(i));
            return view;
        }
    }

    private class TabClickListener implements OnClickListener {
        TabClickListener() {
        }

        public void onClick(View view) {
            ((TabView) view).getTab().select();
            int childCount = ScrollingTabContainerView.this.mTabLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ScrollingTabContainerView.this.mTabLayout.getChildAt(i);
                childAt.setSelected(childAt == view);
            }
        }
    }

    private class TabView extends LinearLayout {
        private final int[] BG_ATTRS = new int[]{16842964};
        private View mCustomView;
        private ImageView mIconView;
        private ActionBar$Tab mTab;
        private TextView mTextView;

        public TabView(Context context, ActionBar$Tab actionBar$Tab, boolean z) {
            super(context, null, R$attr.actionBarTabStyle);
            this.mTab = actionBar$Tab;
            TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, null, this.BG_ATTRS, R$attr.actionBarTabStyle, 0);
            if (obtainStyledAttributes.hasValue(0)) {
                setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
            }
            obtainStyledAttributes.recycle();
            if (z) {
                setGravity(8388627);
            }
            update();
        }

        public void bindTab(ActionBar$Tab actionBar$Tab) {
            this.mTab = actionBar$Tab;
            update();
        }

        public void setSelected(boolean z) {
            Object obj = isSelected() != z ? 1 : null;
            super.setSelected(z);
            if (obj != null && z) {
                sendAccessibilityEvent(4);
            }
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName(ActionBar$Tab.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName(ActionBar$Tab.class.getName());
        }

        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            if (ScrollingTabContainerView.this.mMaxTabWidth > 0) {
                i = getMeasuredWidth();
                int i3 = ScrollingTabContainerView.this.mMaxTabWidth;
                if (i > i3) {
                    super.onMeasure(MeasureSpec.makeMeasureSpec(i3, 1073741824), i2);
                }
            }
        }

        public void update() {
            ActionBar$Tab actionBar$Tab = this.mTab;
            View customView = actionBar$Tab.getCustomView();
            CharSequence charSequence = null;
            if (customView != null) {
                TabView parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        parent.removeView(customView);
                    }
                    addView(customView);
                }
                this.mCustomView = customView;
                TextView textView = this.mTextView;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.mIconView;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                    return;
                }
                return;
            }
            customView = this.mCustomView;
            if (customView != null) {
                removeView(customView);
                this.mCustomView = null;
            }
            Drawable icon = actionBar$Tab.getIcon();
            CharSequence text = actionBar$Tab.getText();
            if (icon != null) {
                if (this.mIconView == null) {
                    AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
                    LayoutParams layoutParams = new LayoutParams(-2, -2);
                    layoutParams.gravity = 16;
                    appCompatImageView.setLayoutParams(layoutParams);
                    addView(appCompatImageView, 0);
                    this.mIconView = appCompatImageView;
                }
                this.mIconView.setImageDrawable(icon);
                this.mIconView.setVisibility(0);
            } else {
                ImageView imageView2 = this.mIconView;
                if (imageView2 != null) {
                    imageView2.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
            }
            int isEmpty = TextUtils.isEmpty(text) ^ 1;
            if (isEmpty != 0) {
                if (this.mTextView == null) {
                    AppCompatTextView appCompatTextView = new AppCompatTextView(getContext(), null, R$attr.actionBarTabTextStyle);
                    appCompatTextView.setEllipsize(TruncateAt.END);
                    LayoutParams layoutParams2 = new LayoutParams(-2, -2);
                    layoutParams2.gravity = 16;
                    appCompatTextView.setLayoutParams(layoutParams2);
                    addView(appCompatTextView);
                    this.mTextView = appCompatTextView;
                }
                this.mTextView.setText(text);
                this.mTextView.setVisibility(0);
            } else {
                TextView textView2 = this.mTextView;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                    this.mTextView.setText(null);
                }
            }
            ImageView imageView3 = this.mIconView;
            if (imageView3 != null) {
                imageView3.setContentDescription(actionBar$Tab.getContentDescription());
            }
            if (isEmpty == 0) {
                charSequence = actionBar$Tab.getContentDescription();
            }
            TooltipCompat.setTooltipText(this, charSequence);
        }

        public ActionBar$Tab getTab() {
            return this.mTab;
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onMeasure(int i, int i2) {
        i2 = MeasureSpec.getMode(i);
        Object obj = 1;
        boolean z = i2 == 1073741824;
        setFillViewport(z);
        int childCount = this.mTabLayout.getChildCount();
        if (childCount <= 1 || !(i2 == 1073741824 || i2 == Integer.MIN_VALUE)) {
            this.mMaxTabWidth = -1;
        } else {
            if (childCount > 2) {
                this.mMaxTabWidth = (int) (((float) MeasureSpec.getSize(i)) * 0.4f);
            } else {
                this.mMaxTabWidth = MeasureSpec.getSize(i) / 2;
            }
            this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        }
        i2 = MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
        if (z || !this.mAllowCollapse) {
            obj = null;
        }
        if (obj != null) {
            this.mTabLayout.measure(0, i2);
            if (this.mTabLayout.getMeasuredWidth() > MeasureSpec.getSize(i)) {
                performCollapse();
            } else {
                performExpand();
            }
        } else {
            performExpand();
        }
        int measuredWidth = getMeasuredWidth();
        super.onMeasure(i, i2);
        i = getMeasuredWidth();
        if (z && measuredWidth != i) {
            setTabSelected(this.mSelectedTabIndex);
        }
    }

    private boolean isCollapsed() {
        Spinner spinner = this.mTabSpinner;
        return spinner != null && spinner.getParent() == this;
    }

    public void setAllowCollapse(boolean z) {
        this.mAllowCollapse = z;
    }

    private void performCollapse() {
        if (!isCollapsed()) {
            if (this.mTabSpinner == null) {
                this.mTabSpinner = createSpinner();
            }
            removeView(this.mTabLayout);
            addView(this.mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
            if (this.mTabSpinner.getAdapter() == null) {
                this.mTabSpinner.setAdapter(new TabAdapter());
            }
            Runnable runnable = this.mTabSelector;
            if (runnable != null) {
                removeCallbacks(runnable);
                this.mTabSelector = null;
            }
            this.mTabSpinner.setSelection(this.mSelectedTabIndex);
        }
    }

    private boolean performExpand() {
        if (!isCollapsed()) {
            return false;
        }
        removeView(this.mTabSpinner);
        addView(this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
        setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }

    public void setTabSelected(int i) {
        this.mSelectedTabIndex = i;
        int childCount = this.mTabLayout.getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            View childAt = this.mTabLayout.getChildAt(i2);
            boolean z = i2 == i;
            childAt.setSelected(z);
            if (z) {
                animateToTab(i);
            }
            i2++;
        }
        Spinner spinner = this.mTabSpinner;
        if (spinner != null && i >= 0) {
            spinner.setSelection(i);
        }
    }

    public void setContentHeight(int i) {
        this.mContentHeight = i;
        requestLayout();
    }

    private Spinner createSpinner() {
        AppCompatSpinner appCompatSpinner = new AppCompatSpinner(getContext(), null, R$attr.actionDropDownStyle);
        appCompatSpinner.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
        appCompatSpinner.setOnItemSelectedListener(this);
        return appCompatSpinner;
    }

    /* Access modifiers changed, original: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(getContext());
        setContentHeight(actionBarPolicy.getTabContainerHeight());
        this.mStackedTabMaxWidth = actionBarPolicy.getStackedTabMaxWidth();
    }

    public void animateToTab(int i) {
        final View childAt = this.mTabLayout.getChildAt(i);
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        this.mTabSelector = new Runnable() {
            public void run() {
                ScrollingTabContainerView.this.smoothScrollTo(childAt.getLeft() - ((ScrollingTabContainerView.this.getWidth() - childAt.getWidth()) / 2), 0);
                ScrollingTabContainerView.this.mTabSelector = null;
            }
        };
        post(this.mTabSelector);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            post(runnable);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public TabView createTabView(ActionBar$Tab actionBar$Tab, boolean z) {
        TabView tabView = new TabView(getContext(), actionBar$Tab, z);
        if (z) {
            tabView.setBackgroundDrawable(null);
            tabView.setLayoutParams(new AbsListView.LayoutParams(-1, this.mContentHeight));
        } else {
            tabView.setFocusable(true);
            if (this.mTabClickListener == null) {
                this.mTabClickListener = new TabClickListener();
            }
            tabView.setOnClickListener(this.mTabClickListener);
        }
        return tabView;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        ((TabView) view).getTab().select();
    }
}
