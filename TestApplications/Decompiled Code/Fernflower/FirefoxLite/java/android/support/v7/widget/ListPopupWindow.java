package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.ShowableListMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import java.lang.reflect.Method;

public class ListPopupWindow implements ShowableListMenu {
   private static Method sClipToWindowEnabledMethod;
   private static Method sGetMaxAvailableHeightMethod;
   private static Method sSetEpicenterBoundsMethod;
   private ListAdapter mAdapter;
   private Context mContext;
   private boolean mDropDownAlwaysVisible;
   private View mDropDownAnchorView;
   private int mDropDownGravity;
   private int mDropDownHeight;
   private int mDropDownHorizontalOffset;
   DropDownListView mDropDownList;
   private Drawable mDropDownListHighlight;
   private int mDropDownVerticalOffset;
   private boolean mDropDownVerticalOffsetSet;
   private int mDropDownWidth;
   private int mDropDownWindowLayoutType;
   private Rect mEpicenterBounds;
   private boolean mForceIgnoreOutsideTouch;
   final Handler mHandler;
   private final ListPopupWindow.ListSelectorHider mHideSelector;
   private boolean mIsAnimatedFromAnchor;
   private OnItemClickListener mItemClickListener;
   private OnItemSelectedListener mItemSelectedListener;
   int mListItemExpandMaximum;
   private boolean mModal;
   private DataSetObserver mObserver;
   private boolean mOverlapAnchor;
   private boolean mOverlapAnchorSet;
   PopupWindow mPopup;
   private int mPromptPosition;
   private View mPromptView;
   final ListPopupWindow.ResizePopupRunnable mResizePopupRunnable;
   private final ListPopupWindow.PopupScrollListener mScrollListener;
   private Runnable mShowDropDownRunnable;
   private final Rect mTempRect;
   private final ListPopupWindow.PopupTouchInterceptor mTouchInterceptor;

   static {
      try {
         sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", Boolean.TYPE);
      } catch (NoSuchMethodException var3) {
         Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
      }

      try {
         sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", View.class, Integer.TYPE, Boolean.TYPE);
      } catch (NoSuchMethodException var2) {
         Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
      }

      try {
         sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", Rect.class);
      } catch (NoSuchMethodException var1) {
         Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
      }

   }

   public ListPopupWindow(Context var1) {
      this(var1, (AttributeSet)null, R.attr.listPopupWindowStyle);
   }

   public ListPopupWindow(Context var1, AttributeSet var2, int var3) {
      this(var1, var2, var3, 0);
   }

   public ListPopupWindow(Context var1, AttributeSet var2, int var3, int var4) {
      this.mDropDownHeight = -2;
      this.mDropDownWidth = -2;
      this.mDropDownWindowLayoutType = 1002;
      this.mIsAnimatedFromAnchor = true;
      this.mDropDownGravity = 0;
      this.mDropDownAlwaysVisible = false;
      this.mForceIgnoreOutsideTouch = false;
      this.mListItemExpandMaximum = Integer.MAX_VALUE;
      this.mPromptPosition = 0;
      this.mResizePopupRunnable = new ListPopupWindow.ResizePopupRunnable();
      this.mTouchInterceptor = new ListPopupWindow.PopupTouchInterceptor();
      this.mScrollListener = new ListPopupWindow.PopupScrollListener();
      this.mHideSelector = new ListPopupWindow.ListSelectorHider();
      this.mTempRect = new Rect();
      this.mContext = var1;
      this.mHandler = new Handler(var1.getMainLooper());
      TypedArray var5 = var1.obtainStyledAttributes(var2, R.styleable.ListPopupWindow, var3, var4);
      this.mDropDownHorizontalOffset = var5.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
      this.mDropDownVerticalOffset = var5.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
      if (this.mDropDownVerticalOffset != 0) {
         this.mDropDownVerticalOffsetSet = true;
      }

      var5.recycle();
      this.mPopup = new AppCompatPopupWindow(var1, var2, var3, var4);
      this.mPopup.setInputMethodMode(1);
   }

   private int buildDropDown() {
      DropDownListView var1 = this.mDropDownList;
      boolean var2 = true;
      int var6;
      int var7;
      if (var1 == null) {
         Context var10 = this.mContext;
         this.mShowDropDownRunnable = new Runnable() {
            public void run() {
               View var1 = ListPopupWindow.this.getAnchorView();
               if (var1 != null && var1.getWindowToken() != null) {
                  ListPopupWindow.this.show();
               }

            }
         };
         this.mDropDownList = this.createDropDownListView(var10, this.mModal ^ true);
         if (this.mDropDownListHighlight != null) {
            this.mDropDownList.setSelector(this.mDropDownListHighlight);
         }

         this.mDropDownList.setAdapter(this.mAdapter);
         this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
         this.mDropDownList.setFocusable(true);
         this.mDropDownList.setFocusableInTouchMode(true);
         this.mDropDownList.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
               if (var3 != -1) {
                  DropDownListView var6 = ListPopupWindow.this.mDropDownList;
                  if (var6 != null) {
                     var6.setListSelectionHidden(false);
                  }
               }

            }

            public void onNothingSelected(AdapterView var1) {
            }
         });
         this.mDropDownList.setOnScrollListener(this.mScrollListener);
         if (this.mItemSelectedListener != null) {
            this.mDropDownList.setOnItemSelectedListener(this.mItemSelectedListener);
         }

         DropDownListView var3 = this.mDropDownList;
         View var4 = this.mPromptView;
         Object var11;
         if (var4 != null) {
            var11 = new LinearLayout(var10);
            ((LinearLayout)var11).setOrientation(1);
            LayoutParams var5 = new LayoutParams(-1, 0, 1.0F);
            switch(this.mPromptPosition) {
            case 0:
               ((LinearLayout)var11).addView(var4);
               ((LinearLayout)var11).addView(var3, var5);
               break;
            case 1:
               ((LinearLayout)var11).addView(var3, var5);
               ((LinearLayout)var11).addView(var4);
               break;
            default:
               StringBuilder var15 = new StringBuilder();
               var15.append("Invalid hint position ");
               var15.append(this.mPromptPosition);
               Log.e("ListPopupWindow", var15.toString());
            }

            if (this.mDropDownWidth >= 0) {
               var6 = this.mDropDownWidth;
               var7 = Integer.MIN_VALUE;
            } else {
               var6 = 0;
               var7 = 0;
            }

            var4.measure(MeasureSpec.makeMeasureSpec(var6, var7), 0);
            LayoutParams var16 = (LayoutParams)var4.getLayoutParams();
            var7 = var4.getMeasuredHeight() + var16.topMargin + var16.bottomMargin;
         } else {
            var7 = 0;
            var11 = var3;
         }

         this.mPopup.setContentView((View)var11);
      } else {
         ViewGroup var12 = (ViewGroup)this.mPopup.getContentView();
         View var17 = this.mPromptView;
         if (var17 != null) {
            LayoutParams var13 = (LayoutParams)var17.getLayoutParams();
            var7 = var17.getMeasuredHeight() + var13.topMargin + var13.bottomMargin;
         } else {
            var7 = 0;
         }
      }

      Drawable var14 = this.mPopup.getBackground();
      int var8;
      if (var14 != null) {
         var14.getPadding(this.mTempRect);
         var6 = this.mTempRect.top + this.mTempRect.bottom;
         var8 = var6;
         if (!this.mDropDownVerticalOffsetSet) {
            this.mDropDownVerticalOffset = -this.mTempRect.top;
            var8 = var6;
         }
      } else {
         this.mTempRect.setEmpty();
         var8 = 0;
      }

      if (this.mPopup.getInputMethodMode() != 2) {
         var2 = false;
      }

      int var9 = this.getMaxAvailableHeight(this.getAnchorView(), this.mDropDownVerticalOffset, var2);
      if (!this.mDropDownAlwaysVisible && this.mDropDownHeight != -1) {
         int var10001;
         switch(this.mDropDownWidth) {
         case -2:
            var10001 = this.mTempRect.left + this.mTempRect.right;
            var6 = MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - var10001, Integer.MIN_VALUE);
            break;
         case -1:
            var10001 = this.mTempRect.left + this.mTempRect.right;
            var6 = MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - var10001, 1073741824);
            break;
         default:
            var6 = MeasureSpec.makeMeasureSpec(this.mDropDownWidth, 1073741824);
         }

         var9 = this.mDropDownList.measureHeightOfChildrenCompat(var6, 0, -1, var9 - var7, -1);
         var6 = var7;
         if (var9 > 0) {
            var6 = var7 + var8 + this.mDropDownList.getPaddingTop() + this.mDropDownList.getPaddingBottom();
         }

         return var9 + var6;
      } else {
         return var9 + var8;
      }
   }

   private int getMaxAvailableHeight(View var1, int var2, boolean var3) {
      if (sGetMaxAvailableHeightMethod != null) {
         try {
            int var4 = (Integer)sGetMaxAvailableHeightMethod.invoke(this.mPopup, var1, var2, var3);
            return var4;
         } catch (Exception var6) {
            Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
         }
      }

      return this.mPopup.getMaxAvailableHeight(var1, var2);
   }

   private void removePromptView() {
      if (this.mPromptView != null) {
         ViewParent var1 = this.mPromptView.getParent();
         if (var1 instanceof ViewGroup) {
            ((ViewGroup)var1).removeView(this.mPromptView);
         }
      }

   }

   private void setPopupClipToScreenEnabled(boolean var1) {
      if (sClipToWindowEnabledMethod != null) {
         try {
            sClipToWindowEnabledMethod.invoke(this.mPopup, var1);
         } catch (Exception var3) {
            Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
         }
      }

   }

   public void clearListSelection() {
      DropDownListView var1 = this.mDropDownList;
      if (var1 != null) {
         var1.setListSelectionHidden(true);
         var1.requestLayout();
      }

   }

   DropDownListView createDropDownListView(Context var1, boolean var2) {
      return new DropDownListView(var1, var2);
   }

   public void dismiss() {
      this.mPopup.dismiss();
      this.removePromptView();
      this.mPopup.setContentView((View)null);
      this.mDropDownList = null;
      this.mHandler.removeCallbacks(this.mResizePopupRunnable);
   }

   public View getAnchorView() {
      return this.mDropDownAnchorView;
   }

   public Drawable getBackground() {
      return this.mPopup.getBackground();
   }

   public int getHorizontalOffset() {
      return this.mDropDownHorizontalOffset;
   }

   public ListView getListView() {
      return this.mDropDownList;
   }

   public int getVerticalOffset() {
      return !this.mDropDownVerticalOffsetSet ? 0 : this.mDropDownVerticalOffset;
   }

   public int getWidth() {
      return this.mDropDownWidth;
   }

   public boolean isInputMethodNotNeeded() {
      boolean var1;
      if (this.mPopup.getInputMethodMode() == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isModal() {
      return this.mModal;
   }

   public boolean isShowing() {
      return this.mPopup.isShowing();
   }

   public void setAdapter(ListAdapter var1) {
      if (this.mObserver == null) {
         this.mObserver = new ListPopupWindow.PopupDataSetObserver();
      } else if (this.mAdapter != null) {
         this.mAdapter.unregisterDataSetObserver(this.mObserver);
      }

      this.mAdapter = var1;
      if (var1 != null) {
         var1.registerDataSetObserver(this.mObserver);
      }

      if (this.mDropDownList != null) {
         this.mDropDownList.setAdapter(this.mAdapter);
      }

   }

   public void setAnchorView(View var1) {
      this.mDropDownAnchorView = var1;
   }

   public void setAnimationStyle(int var1) {
      this.mPopup.setAnimationStyle(var1);
   }

   public void setBackgroundDrawable(Drawable var1) {
      this.mPopup.setBackgroundDrawable(var1);
   }

   public void setContentWidth(int var1) {
      Drawable var2 = this.mPopup.getBackground();
      if (var2 != null) {
         var2.getPadding(this.mTempRect);
         this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + var1;
      } else {
         this.setWidth(var1);
      }

   }

   public void setDropDownGravity(int var1) {
      this.mDropDownGravity = var1;
   }

   public void setEpicenterBounds(Rect var1) {
      this.mEpicenterBounds = var1;
   }

   public void setHorizontalOffset(int var1) {
      this.mDropDownHorizontalOffset = var1;
   }

   public void setInputMethodMode(int var1) {
      this.mPopup.setInputMethodMode(var1);
   }

   public void setModal(boolean var1) {
      this.mModal = var1;
      this.mPopup.setFocusable(var1);
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mPopup.setOnDismissListener(var1);
   }

   public void setOnItemClickListener(OnItemClickListener var1) {
      this.mItemClickListener = var1;
   }

   public void setOverlapAnchor(boolean var1) {
      this.mOverlapAnchorSet = true;
      this.mOverlapAnchor = var1;
   }

   public void setPromptPosition(int var1) {
      this.mPromptPosition = var1;
   }

   public void setSelection(int var1) {
      DropDownListView var2 = this.mDropDownList;
      if (this.isShowing() && var2 != null) {
         var2.setListSelectionHidden(false);
         var2.setSelection(var1);
         if (var2.getChoiceMode() != 0) {
            var2.setItemChecked(var1, true);
         }
      }

   }

   public void setVerticalOffset(int var1) {
      this.mDropDownVerticalOffset = var1;
      this.mDropDownVerticalOffsetSet = true;
   }

   public void setWidth(int var1) {
      this.mDropDownWidth = var1;
   }

   public void show() {
      int var1 = this.buildDropDown();
      boolean var2 = this.isInputMethodNotNeeded();
      PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
      boolean var3 = this.mPopup.isShowing();
      boolean var4 = true;
      int var5;
      PopupWindow var6;
      if (var3) {
         if (!ViewCompat.isAttachedToWindow(this.getAnchorView())) {
            return;
         }

         if (this.mDropDownWidth == -1) {
            var5 = -1;
         } else if (this.mDropDownWidth == -2) {
            var5 = this.getAnchorView().getWidth();
         } else {
            var5 = this.mDropDownWidth;
         }

         if (this.mDropDownHeight == -1) {
            if (!var2) {
               var1 = -1;
            }

            byte var7;
            if (var2) {
               var6 = this.mPopup;
               if (this.mDropDownWidth == -1) {
                  var7 = -1;
               } else {
                  var7 = 0;
               }

               var6.setWidth(var7);
               this.mPopup.setHeight(0);
            } else {
               var6 = this.mPopup;
               if (this.mDropDownWidth == -1) {
                  var7 = -1;
               } else {
                  var7 = 0;
               }

               var6.setWidth(var7);
               this.mPopup.setHeight(-1);
            }
         } else if (this.mDropDownHeight != -2) {
            var1 = this.mDropDownHeight;
         }

         var6 = this.mPopup;
         if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
            var4 = false;
         }

         var6.setOutsideTouchable(var4);
         var6 = this.mPopup;
         View var8 = this.getAnchorView();
         int var11 = this.mDropDownHorizontalOffset;
         int var9 = this.mDropDownVerticalOffset;
         if (var5 < 0) {
            var5 = -1;
         }

         if (var1 < 0) {
            var1 = -1;
         }

         var6.update(var8, var11, var9, var5, var1);
      } else {
         if (this.mDropDownWidth == -1) {
            var5 = -1;
         } else if (this.mDropDownWidth == -2) {
            var5 = this.getAnchorView().getWidth();
         } else {
            var5 = this.mDropDownWidth;
         }

         if (this.mDropDownHeight == -1) {
            var1 = -1;
         } else if (this.mDropDownHeight != -2) {
            var1 = this.mDropDownHeight;
         }

         this.mPopup.setWidth(var5);
         this.mPopup.setHeight(var1);
         this.setPopupClipToScreenEnabled(true);
         var6 = this.mPopup;
         if (!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible) {
            var4 = true;
         } else {
            var4 = false;
         }

         var6.setOutsideTouchable(var4);
         this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
         if (this.mOverlapAnchorSet) {
            PopupWindowCompat.setOverlapAnchor(this.mPopup, this.mOverlapAnchor);
         }

         if (sSetEpicenterBoundsMethod != null) {
            try {
               sSetEpicenterBoundsMethod.invoke(this.mPopup, this.mEpicenterBounds);
            } catch (Exception var10) {
               Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", var10);
            }
         }

         PopupWindowCompat.showAsDropDown(this.mPopup, this.getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
         this.mDropDownList.setSelection(-1);
         if (!this.mModal || this.mDropDownList.isInTouchMode()) {
            this.clearListSelection();
         }

         if (!this.mModal) {
            this.mHandler.post(this.mHideSelector);
         }
      }

   }

   private class ListSelectorHider implements Runnable {
      ListSelectorHider() {
      }

      public void run() {
         ListPopupWindow.this.clearListSelection();
      }
   }

   private class PopupDataSetObserver extends DataSetObserver {
      PopupDataSetObserver() {
      }

      public void onChanged() {
         if (ListPopupWindow.this.isShowing()) {
            ListPopupWindow.this.show();
         }

      }

      public void onInvalidated() {
         ListPopupWindow.this.dismiss();
      }
   }

   private class PopupScrollListener implements OnScrollListener {
      PopupScrollListener() {
      }

      public void onScroll(AbsListView var1, int var2, int var3, int var4) {
      }

      public void onScrollStateChanged(AbsListView var1, int var2) {
         if (var2 == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
            ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
            ListPopupWindow.this.mResizePopupRunnable.run();
         }

      }
   }

   private class PopupTouchInterceptor implements OnTouchListener {
      PopupTouchInterceptor() {
      }

      public boolean onTouch(View var1, MotionEvent var2) {
         int var3 = var2.getAction();
         int var4 = (int)var2.getX();
         int var5 = (int)var2.getY();
         if (var3 == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && var4 >= 0 && var4 < ListPopupWindow.this.mPopup.getWidth() && var5 >= 0 && var5 < ListPopupWindow.this.mPopup.getHeight()) {
            ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250L);
         } else if (var3 == 1) {
            ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
         }

         return false;
      }
   }

   private class ResizePopupRunnable implements Runnable {
      ResizePopupRunnable() {
      }

      public void run() {
         if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow(ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
            ListPopupWindow.this.mPopup.setInputMethodMode(2);
            ListPopupWindow.this.show();
         }

      }
   }
}
