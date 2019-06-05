// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.content.Intent;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.content.pm.ResolveInfo;
import android.widget.BaseAdapter;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.appcompat.R;
import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import android.view.ViewTreeObserver;
import android.support.v4.view.ActionProvider;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.widget.PopupWindow$OnDismissListener;
import android.database.DataSetObserver;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.view.View;
import android.view.ViewGroup;

public class ActivityChooserView extends ViewGroup
{
    private final View mActivityChooserContent;
    final ActivityChooserViewAdapter mAdapter;
    private final Callbacks mCallbacks;
    private int mDefaultActionButtonContentDescription;
    final FrameLayout mDefaultActivityButton;
    final FrameLayout mExpandActivityOverflowButton;
    private final ImageView mExpandActivityOverflowButtonImage;
    int mInitialActivityCount;
    private boolean mIsAttachedToWindow;
    boolean mIsSelectingDefaultActivity;
    private final int mListPopupMaxWidth;
    private ListPopupWindow mListPopupWindow;
    final DataSetObserver mModelDataSetObserver;
    PopupWindow$OnDismissListener mOnDismissListener;
    private final ViewTreeObserver$OnGlobalLayoutListener mOnGlobalLayoutListener;
    ActionProvider mProvider;
    
    public boolean dismissPopup() {
        if (this.isShowingPopup()) {
            this.getListPopupWindow().dismiss();
            final ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
            }
        }
        return true;
    }
    
    public ActivityChooserModel getDataModel() {
        return this.mAdapter.getDataModel();
    }
    
    ListPopupWindow getListPopupWindow() {
        if (this.mListPopupWindow == null) {
            (this.mListPopupWindow = new ListPopupWindow(this.getContext())).setAdapter((ListAdapter)this.mAdapter);
            this.mListPopupWindow.setAnchorView((View)this);
            this.mListPopupWindow.setModal(true);
            this.mListPopupWindow.setOnItemClickListener((AdapterView$OnItemClickListener)this.mCallbacks);
            this.mListPopupWindow.setOnDismissListener((PopupWindow$OnDismissListener)this.mCallbacks);
        }
        return this.mListPopupWindow;
    }
    
    public boolean isShowingPopup() {
        return this.getListPopupWindow().isShowing();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.registerObserver((Object)this.mModelDataSetObserver);
        }
        this.mIsAttachedToWindow = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.unregisterObserver((Object)this.mModelDataSetObserver);
        }
        final ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
        }
        if (this.isShowingPopup()) {
            this.dismissPopup();
        }
        this.mIsAttachedToWindow = false;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.mActivityChooserContent.layout(0, 0, n3 - n, n4 - n2);
        if (!this.isShowingPopup()) {
            this.dismissPopup();
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final View mActivityChooserContent = this.mActivityChooserContent;
        int measureSpec = n2;
        if (this.mDefaultActivityButton.getVisibility() != 0) {
            measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2), 1073741824);
        }
        this.measureChild(mActivityChooserContent, n, measureSpec);
        this.setMeasuredDimension(mActivityChooserContent.getMeasuredWidth(), mActivityChooserContent.getMeasuredHeight());
    }
    
    public void setActivityChooserModel(final ActivityChooserModel dataModel) {
        this.mAdapter.setDataModel(dataModel);
        if (this.isShowingPopup()) {
            this.dismissPopup();
            this.showPopup();
        }
    }
    
    public void setDefaultActionButtonContentDescription(final int mDefaultActionButtonContentDescription) {
        this.mDefaultActionButtonContentDescription = mDefaultActionButtonContentDescription;
    }
    
    public void setExpandActivityOverflowButtonContentDescription(final int n) {
        this.mExpandActivityOverflowButtonImage.setContentDescription((CharSequence)this.getContext().getString(n));
    }
    
    public void setExpandActivityOverflowButtonDrawable(final Drawable imageDrawable) {
        this.mExpandActivityOverflowButtonImage.setImageDrawable(imageDrawable);
    }
    
    public void setInitialActivityCount(final int mInitialActivityCount) {
        this.mInitialActivityCount = mInitialActivityCount;
    }
    
    public void setOnDismissListener(final PopupWindow$OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    public void setProvider(final ActionProvider mProvider) {
        this.mProvider = mProvider;
    }
    
    public boolean showPopup() {
        if (!this.isShowingPopup() && this.mIsAttachedToWindow) {
            this.mIsSelectingDefaultActivity = false;
            this.showPopupUnchecked(this.mInitialActivityCount);
            return true;
        }
        return false;
    }
    
    void showPopupUnchecked(final int maxActivityCount) {
        if (this.mAdapter.getDataModel() != null) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
            int n;
            if (this.mDefaultActivityButton.getVisibility() == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            final int activityCount = this.mAdapter.getActivityCount();
            if (maxActivityCount != Integer.MAX_VALUE && activityCount > maxActivityCount + n) {
                this.mAdapter.setShowFooterView(true);
                this.mAdapter.setMaxActivityCount(maxActivityCount - 1);
            }
            else {
                this.mAdapter.setShowFooterView(false);
                this.mAdapter.setMaxActivityCount(maxActivityCount);
            }
            final ListPopupWindow listPopupWindow = this.getListPopupWindow();
            if (!listPopupWindow.isShowing()) {
                if (!this.mIsSelectingDefaultActivity && n != 0) {
                    this.mAdapter.setShowDefaultActivity(false, false);
                }
                else {
                    this.mAdapter.setShowDefaultActivity(true, (boolean)(n != 0));
                }
                listPopupWindow.setContentWidth(Math.min(this.mAdapter.measureContentWidth(), this.mListPopupMaxWidth));
                listPopupWindow.show();
                if (this.mProvider != null) {
                    this.mProvider.subUiVisibilityChanged(true);
                }
                listPopupWindow.getListView().setContentDescription((CharSequence)this.getContext().getString(R.string.abc_activitychooserview_choose_application));
                listPopupWindow.getListView().setSelector((Drawable)new ColorDrawable(0));
            }
            return;
        }
        throw new IllegalStateException("No data model. Did you call #setDataModel?");
    }
    
    private class ActivityChooserViewAdapter extends BaseAdapter
    {
        private ActivityChooserModel mDataModel;
        private boolean mHighlightDefaultActivity;
        private int mMaxActivityCount;
        private boolean mShowDefaultActivity;
        private boolean mShowFooterView;
        final /* synthetic */ ActivityChooserView this$0;
        
        public int getActivityCount() {
            return this.mDataModel.getActivityCount();
        }
        
        public int getCount() {
            int activityCount;
            final int n = activityCount = this.mDataModel.getActivityCount();
            if (!this.mShowDefaultActivity) {
                activityCount = n;
                if (this.mDataModel.getDefaultActivity() != null) {
                    activityCount = n - 1;
                }
            }
            int min = Math.min(activityCount, this.mMaxActivityCount);
            if (this.mShowFooterView) {
                ++min;
            }
            return min;
        }
        
        public ActivityChooserModel getDataModel() {
            return this.mDataModel;
        }
        
        public ResolveInfo getDefaultActivity() {
            return this.mDataModel.getDefaultActivity();
        }
        
        public Object getItem(final int n) {
            switch (this.getItemViewType(n)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    return null;
                }
                case 0: {
                    int n2 = n;
                    if (!this.mShowDefaultActivity) {
                        n2 = n;
                        if (this.mDataModel.getDefaultActivity() != null) {
                            n2 = n + 1;
                        }
                    }
                    return this.mDataModel.getActivity(n2);
                }
            }
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public int getItemViewType(final int n) {
            if (this.mShowFooterView && n == this.getCount() - 1) {
                return 1;
            }
            return 0;
        }
        
        public boolean getShowDefaultActivity() {
            return this.mShowDefaultActivity;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            switch (this.getItemViewType(n)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    if (view != null) {
                        final View inflate = view;
                        if (view.getId() == 1) {
                            return inflate;
                        }
                    }
                    final View inflate = LayoutInflater.from(this.this$0.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
                    inflate.setId(1);
                    ((TextView)inflate.findViewById(R.id.title)).setText((CharSequence)this.this$0.getContext().getString(R.string.abc_activity_chooser_view_see_all));
                    return inflate;
                }
                case 0: {
                    View inflate2 = null;
                    Label_0144: {
                        if (view != null) {
                            inflate2 = view;
                            if (view.getId() == R.id.list_item) {
                                break Label_0144;
                            }
                        }
                        inflate2 = LayoutInflater.from(this.this$0.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
                    }
                    final PackageManager packageManager = this.this$0.getContext().getPackageManager();
                    final ImageView imageView = (ImageView)inflate2.findViewById(R.id.icon);
                    final ResolveInfo resolveInfo = (ResolveInfo)this.getItem(n);
                    imageView.setImageDrawable(resolveInfo.loadIcon(packageManager));
                    ((TextView)inflate2.findViewById(R.id.title)).setText(resolveInfo.loadLabel(packageManager));
                    if (this.mShowDefaultActivity && n == 0 && this.mHighlightDefaultActivity) {
                        inflate2.setActivated(true);
                    }
                    else {
                        inflate2.setActivated(false);
                    }
                    return inflate2;
                }
            }
        }
        
        public int getViewTypeCount() {
            return 3;
        }
        
        public int measureContentWidth() {
            final int mMaxActivityCount = this.mMaxActivityCount;
            this.mMaxActivityCount = Integer.MAX_VALUE;
            int i = 0;
            final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
            final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(0, 0);
            final int count = this.getCount();
            View view = null;
            int max = 0;
            while (i < count) {
                view = this.getView(i, view, null);
                view.measure(measureSpec, measureSpec2);
                max = Math.max(max, view.getMeasuredWidth());
                ++i;
            }
            this.mMaxActivityCount = mMaxActivityCount;
            return max;
        }
        
        public void setDataModel(final ActivityChooserModel mDataModel) {
            final ActivityChooserModel dataModel = this.this$0.mAdapter.getDataModel();
            if (dataModel != null && this.this$0.isShown()) {
                dataModel.unregisterObserver((Object)this.this$0.mModelDataSetObserver);
            }
            if ((this.mDataModel = mDataModel) != null && this.this$0.isShown()) {
                mDataModel.registerObserver((Object)this.this$0.mModelDataSetObserver);
            }
            this.notifyDataSetChanged();
        }
        
        public void setMaxActivityCount(final int mMaxActivityCount) {
            if (this.mMaxActivityCount != mMaxActivityCount) {
                this.mMaxActivityCount = mMaxActivityCount;
                this.notifyDataSetChanged();
            }
        }
        
        public void setShowDefaultActivity(final boolean mShowDefaultActivity, final boolean mHighlightDefaultActivity) {
            if (this.mShowDefaultActivity != mShowDefaultActivity || this.mHighlightDefaultActivity != mHighlightDefaultActivity) {
                this.mShowDefaultActivity = mShowDefaultActivity;
                this.mHighlightDefaultActivity = mHighlightDefaultActivity;
                this.notifyDataSetChanged();
            }
        }
        
        public void setShowFooterView(final boolean mShowFooterView) {
            if (this.mShowFooterView != mShowFooterView) {
                this.mShowFooterView = mShowFooterView;
                this.notifyDataSetChanged();
            }
        }
    }
    
    private class Callbacks implements View$OnClickListener, View$OnLongClickListener, AdapterView$OnItemClickListener, PopupWindow$OnDismissListener
    {
        final /* synthetic */ ActivityChooserView this$0;
        
        private void notifyOnDismissListener() {
            if (this.this$0.mOnDismissListener != null) {
                this.this$0.mOnDismissListener.onDismiss();
            }
        }
        
        public void onClick(final View view) {
            if (view == this.this$0.mDefaultActivityButton) {
                this.this$0.dismissPopup();
                final Intent chooseActivity = this.this$0.mAdapter.getDataModel().chooseActivity(this.this$0.mAdapter.getDataModel().getActivityIndex(this.this$0.mAdapter.getDefaultActivity()));
                if (chooseActivity != null) {
                    chooseActivity.addFlags(524288);
                    this.this$0.getContext().startActivity(chooseActivity);
                }
            }
            else {
                if (view != this.this$0.mExpandActivityOverflowButton) {
                    throw new IllegalArgumentException();
                }
                this.this$0.mIsSelectingDefaultActivity = false;
                this.this$0.showPopupUnchecked(this.this$0.mInitialActivityCount);
            }
        }
        
        public void onDismiss() {
            this.notifyOnDismissListener();
            if (this.this$0.mProvider != null) {
                this.this$0.mProvider.subUiVisibilityChanged(false);
            }
        }
        
        public void onItemClick(final AdapterView<?> adapterView, final View view, int defaultActivity, final long n) {
            switch (((ActivityChooserViewAdapter)adapterView.getAdapter()).getItemViewType(defaultActivity)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    this.this$0.showPopupUnchecked(Integer.MAX_VALUE);
                    break;
                }
                case 0: {
                    this.this$0.dismissPopup();
                    if (this.this$0.mIsSelectingDefaultActivity) {
                        if (defaultActivity > 0) {
                            this.this$0.mAdapter.getDataModel().setDefaultActivity(defaultActivity);
                            break;
                        }
                        break;
                    }
                    else {
                        if (!this.this$0.mAdapter.getShowDefaultActivity()) {
                            ++defaultActivity;
                        }
                        final Intent chooseActivity = this.this$0.mAdapter.getDataModel().chooseActivity(defaultActivity);
                        if (chooseActivity != null) {
                            chooseActivity.addFlags(524288);
                            this.this$0.getContext().startActivity(chooseActivity);
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
        }
        
        public boolean onLongClick(final View view) {
            if (view == this.this$0.mDefaultActivityButton) {
                if (this.this$0.mAdapter.getCount() > 0) {
                    this.this$0.mIsSelectingDefaultActivity = true;
                    this.this$0.showPopupUnchecked(this.this$0.mInitialActivityCount);
                }
                return true;
            }
            throw new IllegalArgumentException();
        }
    }
    
    public static class InnerLayout extends LinearLayout
    {
        private static final int[] TINT_ATTRS;
        
        static {
            TINT_ATTRS = new int[] { 16842964 };
        }
        
        public InnerLayout(final Context context, final AttributeSet set) {
            super(context, set);
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, InnerLayout.TINT_ATTRS);
            this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
            obtainStyledAttributes.recycle();
        }
    }
}
