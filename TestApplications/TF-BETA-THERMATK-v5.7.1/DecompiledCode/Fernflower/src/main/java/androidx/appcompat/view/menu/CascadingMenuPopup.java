package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import androidx.appcompat.R$dimen;
import androidx.appcompat.R$layout;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, OnKeyListener, OnDismissListener {
   private static final int ITEM_LAYOUT;
   private View mAnchorView;
   private final OnAttachStateChangeListener mAttachStateChangeListener = new OnAttachStateChangeListener() {
      public void onViewAttachedToWindow(View var1) {
      }

      public void onViewDetachedFromWindow(View var1) {
         ViewTreeObserver var2 = CascadingMenuPopup.this.mTreeObserver;
         if (var2 != null) {
            if (!var2.isAlive()) {
               CascadingMenuPopup.this.mTreeObserver = var1.getViewTreeObserver();
            }

            CascadingMenuPopup var3 = CascadingMenuPopup.this;
            var3.mTreeObserver.removeGlobalOnLayoutListener(var3.mGlobalLayoutListener);
         }

         var1.removeOnAttachStateChangeListener(this);
      }
   };
   private final Context mContext;
   private int mDropDownGravity = 0;
   private boolean mForceShowIcon;
   final OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener() {
      public void onGlobalLayout() {
         if (CascadingMenuPopup.this.isShowing() && CascadingMenuPopup.this.mShowingMenus.size() > 0 && !((CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(0)).window.isModal()) {
            View var1 = CascadingMenuPopup.this.mShownAnchorView;
            if (var1 != null && var1.isShown()) {
               Iterator var2 = CascadingMenuPopup.this.mShowingMenus.iterator();

               while(var2.hasNext()) {
                  ((CascadingMenuPopup.CascadingMenuInfo)var2.next()).window.show();
               }
            } else {
               CascadingMenuPopup.this.dismiss();
            }
         }

      }
   };
   private boolean mHasXOffset;
   private boolean mHasYOffset;
   private int mLastPosition;
   private final MenuItemHoverListener mMenuItemHoverListener = new MenuItemHoverListener() {
      public void onItemHoverEnter(final MenuBuilder var1, final MenuItem var2) {
         Handler var3 = CascadingMenuPopup.this.mSubMenuHoverHandler;
         final CascadingMenuPopup.CascadingMenuInfo var4 = null;
         var3.removeCallbacksAndMessages((Object)null);
         int var5 = CascadingMenuPopup.this.mShowingMenus.size();
         int var6 = 0;

         while(true) {
            if (var6 >= var5) {
               var6 = -1;
               break;
            }

            if (var1 == ((CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(var6)).menu) {
               break;
            }

            ++var6;
         }

         if (var6 != -1) {
            ++var6;
            if (var6 < CascadingMenuPopup.this.mShowingMenus.size()) {
               var4 = (CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(var6);
            }

            Runnable var9 = new Runnable() {
               public void run() {
                  CascadingMenuPopup.CascadingMenuInfo var1x = var4;
                  if (var1x != null) {
                     CascadingMenuPopup.this.mShouldCloseImmediately = true;
                     var1x.menu.close(false);
                     CascadingMenuPopup.this.mShouldCloseImmediately = false;
                  }

                  if (var2.isEnabled() && var2.hasSubMenu()) {
                     var1.performItemAction(var2, 4);
                  }

               }
            };
            long var7 = SystemClock.uptimeMillis();
            CascadingMenuPopup.this.mSubMenuHoverHandler.postAtTime(var9, var1, var7 + 200L);
         }
      }

      public void onItemHoverExit(MenuBuilder var1, MenuItem var2) {
         CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(var1);
      }
   };
   private final int mMenuMaxWidth;
   private OnDismissListener mOnDismissListener;
   private final boolean mOverflowOnly;
   private final List mPendingMenus = new ArrayList();
   private final int mPopupStyleAttr;
   private final int mPopupStyleRes;
   private MenuPresenter.Callback mPresenterCallback;
   private int mRawDropDownGravity = 0;
   boolean mShouldCloseImmediately;
   private boolean mShowTitle;
   final List mShowingMenus = new ArrayList();
   View mShownAnchorView;
   final Handler mSubMenuHoverHandler;
   ViewTreeObserver mTreeObserver;
   private int mXOffset;
   private int mYOffset;

   static {
      ITEM_LAYOUT = R$layout.abc_cascading_menu_item_layout;
   }

   public CascadingMenuPopup(Context var1, View var2, int var3, int var4, boolean var5) {
      this.mContext = var1;
      this.mAnchorView = var2;
      this.mPopupStyleAttr = var3;
      this.mPopupStyleRes = var4;
      this.mOverflowOnly = var5;
      this.mForceShowIcon = false;
      this.mLastPosition = this.getInitialMenuPosition();
      Resources var6 = var1.getResources();
      this.mMenuMaxWidth = Math.max(var6.getDisplayMetrics().widthPixels / 2, var6.getDimensionPixelSize(R$dimen.abc_config_prefDialogWidth));
      this.mSubMenuHoverHandler = new Handler();
   }

   private MenuPopupWindow createPopupWindow() {
      MenuPopupWindow var1 = new MenuPopupWindow(this.mContext, (AttributeSet)null, this.mPopupStyleAttr, this.mPopupStyleRes);
      var1.setHoverListener(this.mMenuItemHoverListener);
      var1.setOnItemClickListener(this);
      var1.setOnDismissListener(this);
      var1.setAnchorView(this.mAnchorView);
      var1.setDropDownGravity(this.mDropDownGravity);
      var1.setModal(true);
      var1.setInputMethodMode(2);
      return var1;
   }

   private int findIndexOfAddedMenu(MenuBuilder var1) {
      int var2 = this.mShowingMenus.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1 == ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var3)).menu) {
            return var3;
         }
      }

      return -1;
   }

   private MenuItem findMenuItemForSubmenu(MenuBuilder var1, MenuBuilder var2) {
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         MenuItem var5 = var1.getItem(var4);
         if (var5.hasSubMenu() && var2 == var5.getSubMenu()) {
            return var5;
         }
      }

      return null;
   }

   private View findParentViewForSubmenu(CascadingMenuPopup.CascadingMenuInfo var1, MenuBuilder var2) {
      MenuItem var11 = this.findMenuItemForSubmenu(var1.menu, var2);
      if (var11 == null) {
         return null;
      } else {
         ListView var3 = var1.getListView();
         ListAdapter var8 = var3.getAdapter();
         boolean var4 = var8 instanceof HeaderViewListAdapter;
         int var5 = 0;
         int var6;
         MenuAdapter var10;
         if (var4) {
            HeaderViewListAdapter var9 = (HeaderViewListAdapter)var8;
            var6 = var9.getHeadersCount();
            var10 = (MenuAdapter)var9.getWrappedAdapter();
         } else {
            var10 = (MenuAdapter)var8;
            var6 = 0;
         }

         int var7 = var10.getCount();

         while(true) {
            if (var5 >= var7) {
               var5 = -1;
               break;
            }

            if (var11 == var10.getItem(var5)) {
               break;
            }

            ++var5;
         }

         if (var5 == -1) {
            return null;
         } else {
            var5 = var5 + var6 - var3.getFirstVisiblePosition();
            return var5 >= 0 && var5 < var3.getChildCount() ? var3.getChildAt(var5) : null;
         }
      }
   }

   private int getInitialMenuPosition() {
      int var1 = ViewCompat.getLayoutDirection(this.mAnchorView);
      byte var2 = 1;
      if (var1 == 1) {
         var2 = 0;
      }

      return var2;
   }

   private int getNextMenuPosition(int var1) {
      List var2 = this.mShowingMenus;
      ListView var3 = ((CascadingMenuPopup.CascadingMenuInfo)var2.get(var2.size() - 1)).getListView();
      int[] var4 = new int[2];
      var3.getLocationOnScreen(var4);
      Rect var5 = new Rect();
      this.mShownAnchorView.getWindowVisibleDisplayFrame(var5);
      if (this.mLastPosition == 1) {
         return var4[0] + var3.getWidth() + var1 > var5.right ? 0 : 1;
      } else {
         return var4[0] - var1 < 0 ? 1 : 0;
      }
   }

   private void showMenu(MenuBuilder var1) {
      LayoutInflater var2 = LayoutInflater.from(this.mContext);
      MenuAdapter var3 = new MenuAdapter(var1, var2, this.mOverflowOnly, ITEM_LAYOUT);
      if (!this.isShowing() && this.mForceShowIcon) {
         var3.setForceShowIcon(true);
      } else if (this.isShowing()) {
         var3.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(var1));
      }

      int var4 = MenuPopup.measureIndividualMenuWidth(var3, (ViewGroup)null, this.mContext, this.mMenuMaxWidth);
      MenuPopupWindow var5 = this.createPopupWindow();
      var5.setAdapter(var3);
      var5.setContentWidth(var4);
      var5.setDropDownGravity(this.mDropDownGravity);
      Object var6;
      CascadingMenuPopup.CascadingMenuInfo var14;
      if (this.mShowingMenus.size() > 0) {
         List var13 = this.mShowingMenus;
         var14 = (CascadingMenuPopup.CascadingMenuInfo)var13.get(var13.size() - 1);
         var6 = this.findParentViewForSubmenu(var14, var1);
      } else {
         var14 = null;
         var6 = var14;
      }

      if (var6 != null) {
         var5.setTouchModal(false);
         var5.setEnterTransition((Object)null);
         int var7 = this.getNextMenuPosition(var4);
         boolean var8;
         if (var7 == 1) {
            var8 = true;
         } else {
            var8 = false;
         }

         this.mLastPosition = var7;
         int var9;
         if (VERSION.SDK_INT >= 26) {
            var5.setAnchorView((View)var6);
            var7 = 0;
            var9 = 0;
         } else {
            int[] var10 = new int[2];
            this.mAnchorView.getLocationOnScreen(var10);
            int[] var11 = new int[2];
            ((View)var6).getLocationOnScreen(var11);
            if ((this.mDropDownGravity & 7) == 5) {
               var10[0] += this.mAnchorView.getWidth();
               var11[0] += ((View)var6).getWidth();
            }

            var9 = var11[0] - var10[0];
            var7 = var11[1] - var10[1];
         }

         label62: {
            label61: {
               if ((this.mDropDownGravity & 5) == 5) {
                  if (var8) {
                     break label61;
                  }

                  var4 = ((View)var6).getWidth();
               } else if (var8) {
                  var4 = ((View)var6).getWidth();
                  break label61;
               }

               var4 = var9 - var4;
               break label62;
            }

            var4 += var9;
         }

         var5.setHorizontalOffset(var4);
         var5.setOverlapAnchor(true);
         var5.setVerticalOffset(var7);
      } else {
         if (this.mHasXOffset) {
            var5.setHorizontalOffset(this.mXOffset);
         }

         if (this.mHasYOffset) {
            var5.setVerticalOffset(this.mYOffset);
         }

         var5.setEpicenterBounds(this.getEpicenterBounds());
      }

      CascadingMenuPopup.CascadingMenuInfo var16 = new CascadingMenuPopup.CascadingMenuInfo(var5, var1, this.mLastPosition);
      this.mShowingMenus.add(var16);
      var5.show();
      ListView var17 = var5.getListView();
      var17.setOnKeyListener(this);
      if (var14 == null && this.mShowTitle && var1.getHeaderTitle() != null) {
         FrameLayout var15 = (FrameLayout)var2.inflate(R$layout.abc_popup_menu_header_item_layout, var17, false);
         TextView var12 = (TextView)var15.findViewById(16908310);
         var15.setEnabled(false);
         var12.setText(var1.getHeaderTitle());
         var17.addHeaderView(var15, (Object)null, false);
         var5.show();
      }

   }

   public void addMenu(MenuBuilder var1) {
      var1.addMenuPresenter(this, this.mContext);
      if (this.isShowing()) {
         this.showMenu(var1);
      } else {
         this.mPendingMenus.add(var1);
      }

   }

   protected boolean closeMenuOnSubMenuOpened() {
      return false;
   }

   public void dismiss() {
      int var1 = this.mShowingMenus.size();
      if (var1 > 0) {
         CascadingMenuPopup.CascadingMenuInfo[] var2 = (CascadingMenuPopup.CascadingMenuInfo[])this.mShowingMenus.toArray(new CascadingMenuPopup.CascadingMenuInfo[var1]);
         --var1;

         for(; var1 >= 0; --var1) {
            CascadingMenuPopup.CascadingMenuInfo var3 = var2[var1];
            if (var3.window.isShowing()) {
               var3.window.dismiss();
            }
         }
      }

   }

   public boolean flagActionItems() {
      return false;
   }

   public ListView getListView() {
      ListView var1;
      if (this.mShowingMenus.isEmpty()) {
         var1 = null;
      } else {
         List var2 = this.mShowingMenus;
         var1 = ((CascadingMenuPopup.CascadingMenuInfo)var2.get(var2.size() - 1)).getListView();
      }

      return var1;
   }

   public boolean isShowing() {
      int var1 = this.mShowingMenus.size();
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 > 0) {
         var3 = var2;
         if (((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(0)).window.isShowing()) {
            var3 = true;
         }
      }

      return var3;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      int var3 = this.findIndexOfAddedMenu(var1);
      if (var3 >= 0) {
         int var4 = var3 + 1;
         if (var4 < this.mShowingMenus.size()) {
            ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var4)).menu.close(false);
         }

         CascadingMenuPopup.CascadingMenuInfo var5 = (CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.remove(var3);
         var5.menu.removeMenuPresenter(this);
         if (this.mShouldCloseImmediately) {
            var5.window.setExitTransition((Object)null);
            var5.window.setAnimationStyle(0);
         }

         var5.window.dismiss();
         var4 = this.mShowingMenus.size();
         if (var4 > 0) {
            this.mLastPosition = ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var4 - 1)).position;
         } else {
            this.mLastPosition = this.getInitialMenuPosition();
         }

         if (var4 == 0) {
            this.dismiss();
            MenuPresenter.Callback var7 = this.mPresenterCallback;
            if (var7 != null) {
               var7.onCloseMenu(var1, true);
            }

            ViewTreeObserver var6 = this.mTreeObserver;
            if (var6 != null) {
               if (var6.isAlive()) {
                  this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
               }

               this.mTreeObserver = null;
            }

            this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
            this.mOnDismissListener.onDismiss();
         } else if (var2) {
            ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(0)).menu.close(false);
         }

      }
   }

   public void onDismiss() {
      int var1 = this.mShowingMenus.size();
      int var2 = 0;

      CascadingMenuPopup.CascadingMenuInfo var3;
      while(true) {
         if (var2 >= var1) {
            var3 = null;
            break;
         }

         var3 = (CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var2);
         if (!var3.window.isShowing()) {
            break;
         }

         ++var2;
      }

      if (var3 != null) {
         var3.menu.close(false);
      }

   }

   public boolean onKey(View var1, int var2, KeyEvent var3) {
      if (var3.getAction() == 1 && var2 == 82) {
         this.dismiss();
         return true;
      } else {
         return false;
      }
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      Iterator var2 = this.mShowingMenus.iterator();

      CascadingMenuPopup.CascadingMenuInfo var3;
      do {
         if (!var2.hasNext()) {
            if (var1.hasVisibleItems()) {
               this.addMenu(var1);
               MenuPresenter.Callback var4 = this.mPresenterCallback;
               if (var4 != null) {
                  var4.onOpenSubMenu(var1);
               }

               return true;
            }

            return false;
         }

         var3 = (CascadingMenuPopup.CascadingMenuInfo)var2.next();
      } while(var1 != var3.menu);

      var3.getListView().requestFocus();
      return true;
   }

   public void setAnchorView(View var1) {
      if (this.mAnchorView != var1) {
         this.mAnchorView = var1;
         this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView));
      }

   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
   }

   public void setGravity(int var1) {
      if (this.mRawDropDownGravity != var1) {
         this.mRawDropDownGravity = var1;
         this.mDropDownGravity = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this.mAnchorView));
      }

   }

   public void setHorizontalOffset(int var1) {
      this.mHasXOffset = true;
      this.mXOffset = var1;
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setShowTitle(boolean var1) {
      this.mShowTitle = var1;
   }

   public void setVerticalOffset(int var1) {
      this.mHasYOffset = true;
      this.mYOffset = var1;
   }

   public void show() {
      if (!this.isShowing()) {
         Iterator var1 = this.mPendingMenus.iterator();

         while(var1.hasNext()) {
            this.showMenu((MenuBuilder)var1.next());
         }

         this.mPendingMenus.clear();
         this.mShownAnchorView = this.mAnchorView;
         if (this.mShownAnchorView != null) {
            boolean var2;
            if (this.mTreeObserver == null) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            if (var2) {
               this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }

            this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
         }

      }
   }

   public void updateMenuView(boolean var1) {
      Iterator var2 = this.mShowingMenus.iterator();

      while(var2.hasNext()) {
         MenuPopup.toMenuAdapter(((CascadingMenuPopup.CascadingMenuInfo)var2.next()).getListView().getAdapter()).notifyDataSetChanged();
      }

   }

   private static class CascadingMenuInfo {
      public final MenuBuilder menu;
      public final int position;
      public final MenuPopupWindow window;

      public CascadingMenuInfo(MenuPopupWindow var1, MenuBuilder var2, int var3) {
         this.window = var1;
         this.menu = var2;
         this.position = var3;
      }

      public ListView getListView() {
         return this.window.getListView();
      }
   }
}
