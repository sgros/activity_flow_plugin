package android.support.v7.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class ActivityChooserView extends ViewGroup {
   private final View mActivityChooserContent;
   final ActivityChooserView.ActivityChooserViewAdapter mAdapter;
   private final ActivityChooserView.Callbacks mCallbacks;
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

   public boolean dismissPopup() {
      if (this.isShowingPopup()) {
         this.getListPopupWindow().dismiss();
         ViewTreeObserver var1 = this.getViewTreeObserver();
         if (var1.isAlive()) {
            var1.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
         }
      }

      return true;
   }

   public ActivityChooserModel getDataModel() {
      return this.mAdapter.getDataModel();
   }

   ListPopupWindow getListPopupWindow() {
      if (this.mListPopupWindow == null) {
         this.mListPopupWindow = new ListPopupWindow(this.getContext());
         this.mListPopupWindow.setAdapter(this.mAdapter);
         this.mListPopupWindow.setAnchorView(this);
         this.mListPopupWindow.setModal(true);
         this.mListPopupWindow.setOnItemClickListener(this.mCallbacks);
         this.mListPopupWindow.setOnDismissListener(this.mCallbacks);
      }

      return this.mListPopupWindow;
   }

   public boolean isShowingPopup() {
      return this.getListPopupWindow().isShowing();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      ActivityChooserModel var1 = this.mAdapter.getDataModel();
      if (var1 != null) {
         var1.registerObserver(this.mModelDataSetObserver);
      }

      this.mIsAttachedToWindow = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      ActivityChooserModel var1 = this.mAdapter.getDataModel();
      if (var1 != null) {
         var1.unregisterObserver(this.mModelDataSetObserver);
      }

      ViewTreeObserver var2 = this.getViewTreeObserver();
      if (var2.isAlive()) {
         var2.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
      }

      if (this.isShowingPopup()) {
         this.dismissPopup();
      }

      this.mIsAttachedToWindow = false;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.mActivityChooserContent.layout(0, 0, var4 - var2, var5 - var3);
      if (!this.isShowingPopup()) {
         this.dismissPopup();
      }

   }

   protected void onMeasure(int var1, int var2) {
      View var3 = this.mActivityChooserContent;
      int var4 = var2;
      if (this.mDefaultActivityButton.getVisibility() != 0) {
         var4 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var2), 1073741824);
      }

      this.measureChild(var3, var1, var4);
      this.setMeasuredDimension(var3.getMeasuredWidth(), var3.getMeasuredHeight());
   }

   public void setActivityChooserModel(ActivityChooserModel var1) {
      this.mAdapter.setDataModel(var1);
      if (this.isShowingPopup()) {
         this.dismissPopup();
         this.showPopup();
      }

   }

   public void setDefaultActionButtonContentDescription(int var1) {
      this.mDefaultActionButtonContentDescription = var1;
   }

   public void setExpandActivityOverflowButtonContentDescription(int var1) {
      String var2 = this.getContext().getString(var1);
      this.mExpandActivityOverflowButtonImage.setContentDescription(var2);
   }

   public void setExpandActivityOverflowButtonDrawable(Drawable var1) {
      this.mExpandActivityOverflowButtonImage.setImageDrawable(var1);
   }

   public void setInitialActivityCount(int var1) {
      this.mInitialActivityCount = var1;
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setProvider(ActionProvider var1) {
      this.mProvider = var1;
   }

   public boolean showPopup() {
      if (!this.isShowingPopup() && this.mIsAttachedToWindow) {
         this.mIsSelectingDefaultActivity = false;
         this.showPopupUnchecked(this.mInitialActivityCount);
         return true;
      } else {
         return false;
      }
   }

   void showPopupUnchecked(int var1) {
      if (this.mAdapter.getDataModel() != null) {
         this.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
         byte var2;
         if (this.mDefaultActivityButton.getVisibility() == 0) {
            var2 = 1;
         } else {
            var2 = 0;
         }

         int var3 = this.mAdapter.getActivityCount();
         if (var1 != Integer.MAX_VALUE && var3 > var1 + var2) {
            this.mAdapter.setShowFooterView(true);
            this.mAdapter.setMaxActivityCount(var1 - 1);
         } else {
            this.mAdapter.setShowFooterView(false);
            this.mAdapter.setMaxActivityCount(var1);
         }

         ListPopupWindow var4 = this.getListPopupWindow();
         if (!var4.isShowing()) {
            if (!this.mIsSelectingDefaultActivity && var2 != 0) {
               this.mAdapter.setShowDefaultActivity(false, false);
            } else {
               this.mAdapter.setShowDefaultActivity(true, (boolean)var2);
            }

            var4.setContentWidth(Math.min(this.mAdapter.measureContentWidth(), this.mListPopupMaxWidth));
            var4.show();
            if (this.mProvider != null) {
               this.mProvider.subUiVisibilityChanged(true);
            }

            var4.getListView().setContentDescription(this.getContext().getString(R.string.abc_activitychooserview_choose_application));
            var4.getListView().setSelector(new ColorDrawable(0));
         }

      } else {
         throw new IllegalStateException("No data model. Did you call #setDataModel?");
      }
   }

   private class ActivityChooserViewAdapter extends BaseAdapter {
      private ActivityChooserModel mDataModel;
      private boolean mHighlightDefaultActivity;
      private int mMaxActivityCount;
      private boolean mShowDefaultActivity;
      private boolean mShowFooterView;
      // $FF: synthetic field
      final ActivityChooserView this$0;

      public int getActivityCount() {
         return this.mDataModel.getActivityCount();
      }

      public int getCount() {
         int var1 = this.mDataModel.getActivityCount();
         int var2 = var1;
         if (!this.mShowDefaultActivity) {
            var2 = var1;
            if (this.mDataModel.getDefaultActivity() != null) {
               var2 = var1 - 1;
            }
         }

         var1 = Math.min(var2, this.mMaxActivityCount);
         var2 = var1;
         if (this.mShowFooterView) {
            var2 = var1 + 1;
         }

         return var2;
      }

      public ActivityChooserModel getDataModel() {
         return this.mDataModel;
      }

      public ResolveInfo getDefaultActivity() {
         return this.mDataModel.getDefaultActivity();
      }

      public Object getItem(int var1) {
         switch(this.getItemViewType(var1)) {
         case 0:
            int var2 = var1;
            if (!this.mShowDefaultActivity) {
               var2 = var1;
               if (this.mDataModel.getDefaultActivity() != null) {
                  var2 = var1 + 1;
               }
            }

            return this.mDataModel.getActivity(var2);
         case 1:
            return null;
         default:
            throw new IllegalArgumentException();
         }
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         return this.mShowFooterView && var1 == this.getCount() - 1 ? 1 : 0;
      }

      public boolean getShowDefaultActivity() {
         return this.mShowDefaultActivity;
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         View var4;
         switch(this.getItemViewType(var1)) {
         case 0:
            label36: {
               if (var2 != null) {
                  var4 = var2;
                  if (var2.getId() == R.id.list_item) {
                     break label36;
                  }
               }

               var4 = LayoutInflater.from(this.this$0.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, var3, false);
            }

            PackageManager var7 = this.this$0.getContext().getPackageManager();
            ImageView var5 = (ImageView)var4.findViewById(R.id.icon);
            ResolveInfo var6 = (ResolveInfo)this.getItem(var1);
            var5.setImageDrawable(var6.loadIcon(var7));
            ((TextView)var4.findViewById(R.id.title)).setText(var6.loadLabel(var7));
            if (this.mShowDefaultActivity && var1 == 0 && this.mHighlightDefaultActivity) {
               var4.setActivated(true);
            } else {
               var4.setActivated(false);
            }

            return var4;
         case 1:
            if (var2 != null) {
               var4 = var2;
               if (var2.getId() == 1) {
                  return var4;
               }
            }

            var4 = LayoutInflater.from(this.this$0.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, var3, false);
            var4.setId(1);
            ((TextView)var4.findViewById(R.id.title)).setText(this.this$0.getContext().getString(R.string.abc_activity_chooser_view_see_all));
            return var4;
         default:
            throw new IllegalArgumentException();
         }
      }

      public int getViewTypeCount() {
         return 3;
      }

      public int measureContentWidth() {
         int var1 = this.mMaxActivityCount;
         this.mMaxActivityCount = Integer.MAX_VALUE;
         int var2 = 0;
         int var3 = MeasureSpec.makeMeasureSpec(0, 0);
         int var4 = MeasureSpec.makeMeasureSpec(0, 0);
         int var5 = this.getCount();
         View var6 = null;

         int var7;
         for(var7 = 0; var2 < var5; ++var2) {
            var6 = this.getView(var2, var6, (ViewGroup)null);
            var6.measure(var3, var4);
            var7 = Math.max(var7, var6.getMeasuredWidth());
         }

         this.mMaxActivityCount = var1;
         return var7;
      }

      public void setDataModel(ActivityChooserModel var1) {
         ActivityChooserModel var2 = this.this$0.mAdapter.getDataModel();
         if (var2 != null && this.this$0.isShown()) {
            var2.unregisterObserver(this.this$0.mModelDataSetObserver);
         }

         this.mDataModel = var1;
         if (var1 != null && this.this$0.isShown()) {
            var1.registerObserver(this.this$0.mModelDataSetObserver);
         }

         this.notifyDataSetChanged();
      }

      public void setMaxActivityCount(int var1) {
         if (this.mMaxActivityCount != var1) {
            this.mMaxActivityCount = var1;
            this.notifyDataSetChanged();
         }

      }

      public void setShowDefaultActivity(boolean var1, boolean var2) {
         if (this.mShowDefaultActivity != var1 || this.mHighlightDefaultActivity != var2) {
            this.mShowDefaultActivity = var1;
            this.mHighlightDefaultActivity = var2;
            this.notifyDataSetChanged();
         }

      }

      public void setShowFooterView(boolean var1) {
         if (this.mShowFooterView != var1) {
            this.mShowFooterView = var1;
            this.notifyDataSetChanged();
         }

      }
   }

   private class Callbacks implements OnClickListener, OnLongClickListener, OnItemClickListener, OnDismissListener {
      // $FF: synthetic field
      final ActivityChooserView this$0;

      private void notifyOnDismissListener() {
         if (this.this$0.mOnDismissListener != null) {
            this.this$0.mOnDismissListener.onDismiss();
         }

      }

      public void onClick(View var1) {
         if (var1 == this.this$0.mDefaultActivityButton) {
            this.this$0.dismissPopup();
            ResolveInfo var3 = this.this$0.mAdapter.getDefaultActivity();
            int var2 = this.this$0.mAdapter.getDataModel().getActivityIndex(var3);
            Intent var4 = this.this$0.mAdapter.getDataModel().chooseActivity(var2);
            if (var4 != null) {
               var4.addFlags(524288);
               this.this$0.getContext().startActivity(var4);
            }
         } else {
            if (var1 != this.this$0.mExpandActivityOverflowButton) {
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

      public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
         switch(((ActivityChooserView.ActivityChooserViewAdapter)var1.getAdapter()).getItemViewType(var3)) {
         case 0:
            this.this$0.dismissPopup();
            if (this.this$0.mIsSelectingDefaultActivity) {
               if (var3 > 0) {
                  this.this$0.mAdapter.getDataModel().setDefaultActivity(var3);
               }
            } else {
               if (!this.this$0.mAdapter.getShowDefaultActivity()) {
                  ++var3;
               }

               Intent var6 = this.this$0.mAdapter.getDataModel().chooseActivity(var3);
               if (var6 != null) {
                  var6.addFlags(524288);
                  this.this$0.getContext().startActivity(var6);
               }
            }
            break;
         case 1:
            this.this$0.showPopupUnchecked(Integer.MAX_VALUE);
            break;
         default:
            throw new IllegalArgumentException();
         }

      }

      public boolean onLongClick(View var1) {
         if (var1 == this.this$0.mDefaultActivityButton) {
            if (this.this$0.mAdapter.getCount() > 0) {
               this.this$0.mIsSelectingDefaultActivity = true;
               this.this$0.showPopupUnchecked(this.this$0.mInitialActivityCount);
            }

            return true;
         } else {
            throw new IllegalArgumentException();
         }
      }
   }

   public static class InnerLayout extends LinearLayout {
      private static final int[] TINT_ATTRS = new int[]{16842964};

      public InnerLayout(Context var1, AttributeSet var2) {
         super(var1, var2);
         TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(var1, var2, TINT_ATTRS);
         this.setBackgroundDrawable(var3.getDrawable(0));
         var3.recycle();
      }
   }
}
