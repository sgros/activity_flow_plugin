package android.support.p004v7.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.p001v4.view.ActionProvider;
import android.support.p004v7.appcompat.C0187R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/* renamed from: android.support.v7.widget.ActivityChooserView */
public class ActivityChooserView extends ViewGroup {
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
    OnDismissListener mOnDismissListener;
    private final OnGlobalLayoutListener mOnGlobalLayoutListener;
    ActionProvider mProvider;

    /* renamed from: android.support.v7.widget.ActivityChooserView$ActivityChooserViewAdapter */
    private class ActivityChooserViewAdapter extends BaseAdapter {
        private ActivityChooserModel mDataModel;
        private boolean mHighlightDefaultActivity;
        private int mMaxActivityCount;
        private boolean mShowDefaultActivity;
        private boolean mShowFooterView;
        final /* synthetic */ ActivityChooserView this$0;

        public long getItemId(int i) {
            return (long) i;
        }

        public int getViewTypeCount() {
            return 3;
        }

        public void setDataModel(ActivityChooserModel activityChooserModel) {
            ActivityChooserModel dataModel = this.this$0.mAdapter.getDataModel();
            if (dataModel != null && this.this$0.isShown()) {
                dataModel.unregisterObserver(this.this$0.mModelDataSetObserver);
            }
            this.mDataModel = activityChooserModel;
            if (activityChooserModel != null && this.this$0.isShown()) {
                activityChooserModel.registerObserver(this.this$0.mModelDataSetObserver);
            }
            notifyDataSetChanged();
        }

        public int getItemViewType(int i) {
            return (this.mShowFooterView && i == getCount() - 1) ? 1 : 0;
        }

        public int getCount() {
            int activityCount = this.mDataModel.getActivityCount();
            if (!(this.mShowDefaultActivity || this.mDataModel.getDefaultActivity() == null)) {
                activityCount--;
            }
            activityCount = Math.min(activityCount, this.mMaxActivityCount);
            return this.mShowFooterView ? activityCount + 1 : activityCount;
        }

        public Object getItem(int i) {
            switch (getItemViewType(i)) {
                case 0:
                    if (!(this.mShowDefaultActivity || this.mDataModel.getDefaultActivity() == null)) {
                        i++;
                    }
                    return this.mDataModel.getActivity(i);
                case 1:
                    return null;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            switch (getItemViewType(i)) {
                case 0:
                    if (view == null || view.getId() != C0187R.C0186id.list_item) {
                        view = LayoutInflater.from(this.this$0.getContext()).inflate(C0187R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
                    }
                    PackageManager packageManager = this.this$0.getContext().getPackageManager();
                    ResolveInfo resolveInfo = (ResolveInfo) getItem(i);
                    ((ImageView) view.findViewById(C0187R.C0186id.icon)).setImageDrawable(resolveInfo.loadIcon(packageManager));
                    ((TextView) view.findViewById(C0187R.C0186id.title)).setText(resolveInfo.loadLabel(packageManager));
                    if (this.mShowDefaultActivity && i == 0 && this.mHighlightDefaultActivity) {
                        view.setActivated(true);
                    } else {
                        view.setActivated(false);
                    }
                    return view;
                case 1:
                    if (view == null || view.getId() != 1) {
                        view = LayoutInflater.from(this.this$0.getContext()).inflate(C0187R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
                        view.setId(1);
                        ((TextView) view.findViewById(C0187R.C0186id.title)).setText(this.this$0.getContext().getString(C0187R.string.abc_activity_chooser_view_see_all));
                    }
                    return view;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public int measureContentWidth() {
            int i = this.mMaxActivityCount;
            this.mMaxActivityCount = Integer.MAX_VALUE;
            int i2 = 0;
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
            int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(0, 0);
            int count = getCount();
            View view = null;
            int i3 = 0;
            while (i2 < count) {
                view = getView(i2, view, null);
                view.measure(makeMeasureSpec, makeMeasureSpec2);
                i3 = Math.max(i3, view.getMeasuredWidth());
                i2++;
            }
            this.mMaxActivityCount = i;
            return i3;
        }

        public void setMaxActivityCount(int i) {
            if (this.mMaxActivityCount != i) {
                this.mMaxActivityCount = i;
                notifyDataSetChanged();
            }
        }

        public ResolveInfo getDefaultActivity() {
            return this.mDataModel.getDefaultActivity();
        }

        public void setShowFooterView(boolean z) {
            if (this.mShowFooterView != z) {
                this.mShowFooterView = z;
                notifyDataSetChanged();
            }
        }

        public int getActivityCount() {
            return this.mDataModel.getActivityCount();
        }

        public ActivityChooserModel getDataModel() {
            return this.mDataModel;
        }

        public void setShowDefaultActivity(boolean z, boolean z2) {
            if (this.mShowDefaultActivity != z || this.mHighlightDefaultActivity != z2) {
                this.mShowDefaultActivity = z;
                this.mHighlightDefaultActivity = z2;
                notifyDataSetChanged();
            }
        }

        public boolean getShowDefaultActivity() {
            return this.mShowDefaultActivity;
        }
    }

    /* renamed from: android.support.v7.widget.ActivityChooserView$Callbacks */
    private class Callbacks implements OnClickListener, OnLongClickListener, OnItemClickListener, OnDismissListener {
        final /* synthetic */ ActivityChooserView this$0;

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            switch (((ActivityChooserViewAdapter) adapterView.getAdapter()).getItemViewType(i)) {
                case 0:
                    this.this$0.dismissPopup();
                    if (!this.this$0.mIsSelectingDefaultActivity) {
                        if (!this.this$0.mAdapter.getShowDefaultActivity()) {
                            i++;
                        }
                        Intent chooseActivity = this.this$0.mAdapter.getDataModel().chooseActivity(i);
                        if (chooseActivity != null) {
                            chooseActivity.addFlags(524288);
                            this.this$0.getContext().startActivity(chooseActivity);
                            return;
                        }
                        return;
                    } else if (i > 0) {
                        this.this$0.mAdapter.getDataModel().setDefaultActivity(i);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    this.this$0.showPopupUnchecked(Integer.MAX_VALUE);
                    return;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public void onClick(View view) {
            if (view == this.this$0.mDefaultActivityButton) {
                this.this$0.dismissPopup();
                Intent chooseActivity = this.this$0.mAdapter.getDataModel().chooseActivity(this.this$0.mAdapter.getDataModel().getActivityIndex(this.this$0.mAdapter.getDefaultActivity()));
                if (chooseActivity != null) {
                    chooseActivity.addFlags(524288);
                    this.this$0.getContext().startActivity(chooseActivity);
                }
            } else if (view == this.this$0.mExpandActivityOverflowButton) {
                this.this$0.mIsSelectingDefaultActivity = false;
                this.this$0.showPopupUnchecked(this.this$0.mInitialActivityCount);
            } else {
                throw new IllegalArgumentException();
            }
        }

        public boolean onLongClick(View view) {
            if (view == this.this$0.mDefaultActivityButton) {
                if (this.this$0.mAdapter.getCount() > 0) {
                    this.this$0.mIsSelectingDefaultActivity = true;
                    this.this$0.showPopupUnchecked(this.this$0.mInitialActivityCount);
                }
                return true;
            }
            throw new IllegalArgumentException();
        }

        public void onDismiss() {
            notifyOnDismissListener();
            if (this.this$0.mProvider != null) {
                this.this$0.mProvider.subUiVisibilityChanged(false);
            }
        }

        private void notifyOnDismissListener() {
            if (this.this$0.mOnDismissListener != null) {
                this.this$0.mOnDismissListener.onDismiss();
            }
        }
    }

    /* renamed from: android.support.v7.widget.ActivityChooserView$InnerLayout */
    public static class InnerLayout extends LinearLayout {
        private static final int[] TINT_ATTRS = new int[]{16842964};

        public InnerLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, TINT_ATTRS);
            setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
            obtainStyledAttributes.recycle();
        }
    }

    public void setActivityChooserModel(ActivityChooserModel activityChooserModel) {
        this.mAdapter.setDataModel(activityChooserModel);
        if (isShowingPopup()) {
            dismissPopup();
            showPopup();
        }
    }

    public void setExpandActivityOverflowButtonDrawable(Drawable drawable) {
        this.mExpandActivityOverflowButtonImage.setImageDrawable(drawable);
    }

    public void setExpandActivityOverflowButtonContentDescription(int i) {
        this.mExpandActivityOverflowButtonImage.setContentDescription(getContext().getString(i));
    }

    public void setProvider(ActionProvider actionProvider) {
        this.mProvider = actionProvider;
    }

    public boolean showPopup() {
        if (isShowingPopup() || !this.mIsAttachedToWindow) {
            return false;
        }
        this.mIsSelectingDefaultActivity = false;
        showPopupUnchecked(this.mInitialActivityCount);
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void showPopupUnchecked(int i) {
        if (this.mAdapter.getDataModel() != null) {
            getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
            int i2 = this.mDefaultActivityButton.getVisibility() == 0 ? 1 : 0;
            int activityCount = this.mAdapter.getActivityCount();
            if (i == Integer.MAX_VALUE || activityCount <= i + i2) {
                this.mAdapter.setShowFooterView(false);
                this.mAdapter.setMaxActivityCount(i);
            } else {
                this.mAdapter.setShowFooterView(true);
                this.mAdapter.setMaxActivityCount(i - 1);
            }
            ListPopupWindow listPopupWindow = getListPopupWindow();
            if (!listPopupWindow.isShowing()) {
                if (this.mIsSelectingDefaultActivity || i2 == 0) {
                    this.mAdapter.setShowDefaultActivity(true, i2);
                } else {
                    this.mAdapter.setShowDefaultActivity(false, false);
                }
                listPopupWindow.setContentWidth(Math.min(this.mAdapter.measureContentWidth(), this.mListPopupMaxWidth));
                listPopupWindow.show();
                if (this.mProvider != null) {
                    this.mProvider.subUiVisibilityChanged(true);
                }
                listPopupWindow.getListView().setContentDescription(getContext().getString(C0187R.string.abc_activitychooserview_choose_application));
                listPopupWindow.getListView().setSelector(new ColorDrawable(0));
                return;
            }
            return;
        }
        throw new IllegalStateException("No data model. Did you call #setDataModel?");
    }

    public boolean dismissPopup() {
        if (isShowingPopup()) {
            getListPopupWindow().dismiss();
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
            }
        }
        return true;
    }

    public boolean isShowingPopup() {
        return getListPopupWindow().isShowing();
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.registerObserver(this.mModelDataSetObserver);
        }
        this.mIsAttachedToWindow = true;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.unregisterObserver(this.mModelDataSetObserver);
        }
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
        }
        if (isShowingPopup()) {
            dismissPopup();
        }
        this.mIsAttachedToWindow = false;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        View view = this.mActivityChooserContent;
        if (this.mDefaultActivityButton.getVisibility() != 0) {
            i2 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i2), 1073741824);
        }
        measureChild(view, i, i2);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mActivityChooserContent.layout(0, 0, i3 - i, i4 - i2);
        if (!isShowingPopup()) {
            dismissPopup();
        }
    }

    public ActivityChooserModel getDataModel() {
        return this.mAdapter.getDataModel();
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setInitialActivityCount(int i) {
        this.mInitialActivityCount = i;
    }

    public void setDefaultActionButtonContentDescription(int i) {
        this.mDefaultActionButtonContentDescription = i;
    }

    /* Access modifiers changed, original: 0000 */
    public ListPopupWindow getListPopupWindow() {
        if (this.mListPopupWindow == null) {
            this.mListPopupWindow = new ListPopupWindow(getContext());
            this.mListPopupWindow.setAdapter(this.mAdapter);
            this.mListPopupWindow.setAnchorView(this);
            this.mListPopupWindow.setModal(true);
            this.mListPopupWindow.setOnItemClickListener(this.mCallbacks);
            this.mListPopupWindow.setOnDismissListener(this.mCallbacks);
        }
        return this.mListPopupWindow;
    }
}
