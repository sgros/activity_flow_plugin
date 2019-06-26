// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.appcompat.view.menu.ListMenuItemView;
import android.view.KeyEvent;
import android.widget.ListAdapter;
import androidx.appcompat.view.menu.MenuAdapter;
import android.widget.HeaderViewListAdapter;
import android.view.MotionEvent;
import android.content.res.Configuration;
import android.transition.Transition;
import android.os.Build$VERSION;
import android.view.MenuItem;
import androidx.appcompat.view.menu.MenuBuilder;
import android.util.AttributeSet;
import android.content.Context;
import android.util.Log;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public class MenuPopupWindow extends ListPopupWindow implements MenuItemHoverListener
{
    private static Method sSetTouchModalMethod;
    private MenuItemHoverListener mHoverListener;
    
    static {
        try {
            MenuPopupWindow.sSetTouchModalMethod = PopupWindow.class.getDeclaredMethod("setTouchModal", Boolean.TYPE);
        }
        catch (NoSuchMethodException ex) {
            Log.i("MenuPopupWindow", "Could not find method setTouchModal() on PopupWindow. Oh well.");
        }
    }
    
    public MenuPopupWindow(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
    }
    
    @Override
    DropDownListView createDropDownListView(final Context context, final boolean b) {
        final MenuDropDownListView menuDropDownListView = new MenuDropDownListView(context, b);
        menuDropDownListView.setHoverListener(this);
        return menuDropDownListView;
    }
    
    @Override
    public void onItemHoverEnter(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        final MenuItemHoverListener mHoverListener = this.mHoverListener;
        if (mHoverListener != null) {
            mHoverListener.onItemHoverEnter(menuBuilder, menuItem);
        }
    }
    
    @Override
    public void onItemHoverExit(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        final MenuItemHoverListener mHoverListener = this.mHoverListener;
        if (mHoverListener != null) {
            mHoverListener.onItemHoverExit(menuBuilder, menuItem);
        }
    }
    
    public void setEnterTransition(final Object o) {
        if (Build$VERSION.SDK_INT >= 23) {
            super.mPopup.setEnterTransition((Transition)o);
        }
    }
    
    public void setExitTransition(final Object o) {
        if (Build$VERSION.SDK_INT >= 23) {
            super.mPopup.setExitTransition((Transition)o);
        }
    }
    
    public void setHoverListener(final MenuItemHoverListener mHoverListener) {
        this.mHoverListener = mHoverListener;
    }
    
    public void setTouchModal(final boolean b) {
        final Method sSetTouchModalMethod = MenuPopupWindow.sSetTouchModalMethod;
        if (sSetTouchModalMethod != null) {
            try {
                sSetTouchModalMethod.invoke(super.mPopup, b);
            }
            catch (Exception ex) {
                Log.i("MenuPopupWindow", "Could not invoke setTouchModal() on PopupWindow. Oh well.");
            }
        }
    }
    
    public static class MenuDropDownListView extends DropDownListView
    {
        final int mAdvanceKey;
        private MenuItemHoverListener mHoverListener;
        private MenuItem mHoveredMenuItem;
        final int mRetreatKey;
        
        public MenuDropDownListView(final Context context, final boolean b) {
            super(context, b);
            final Configuration configuration = context.getResources().getConfiguration();
            if (Build$VERSION.SDK_INT >= 17 && 1 == configuration.getLayoutDirection()) {
                this.mAdvanceKey = 21;
                this.mRetreatKey = 22;
            }
            else {
                this.mAdvanceKey = 22;
                this.mRetreatKey = 21;
            }
        }
        
        @Override
        public boolean onHoverEvent(final MotionEvent motionEvent) {
            if (this.mHoverListener != null) {
                final ListAdapter adapter = this.getAdapter();
                int headersCount;
                MenuAdapter menuAdapter;
                if (adapter instanceof HeaderViewListAdapter) {
                    final HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter)adapter;
                    headersCount = headerViewListAdapter.getHeadersCount();
                    menuAdapter = (MenuAdapter)headerViewListAdapter.getWrappedAdapter();
                }
                else {
                    headersCount = 0;
                    menuAdapter = (MenuAdapter)adapter;
                }
                Object item;
                final Object o = item = null;
                if (motionEvent.getAction() != 10) {
                    final int pointToPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
                    item = o;
                    if (pointToPosition != -1) {
                        final int n = pointToPosition - headersCount;
                        item = o;
                        if (n >= 0) {
                            item = o;
                            if (n < menuAdapter.getCount()) {
                                item = menuAdapter.getItem(n);
                            }
                        }
                    }
                }
                final MenuItem mHoveredMenuItem = this.mHoveredMenuItem;
                if (mHoveredMenuItem != item) {
                    final MenuBuilder adapterMenu = menuAdapter.getAdapterMenu();
                    if (mHoveredMenuItem != null) {
                        this.mHoverListener.onItemHoverExit(adapterMenu, mHoveredMenuItem);
                    }
                    if ((this.mHoveredMenuItem = (MenuItem)item) != null) {
                        this.mHoverListener.onItemHoverEnter(adapterMenu, (MenuItem)item);
                    }
                }
            }
            return super.onHoverEvent(motionEvent);
        }
        
        public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
            final ListMenuItemView listMenuItemView = (ListMenuItemView)this.getSelectedView();
            if (listMenuItemView != null && n == this.mAdvanceKey) {
                if (listMenuItemView.isEnabled() && listMenuItemView.getItemData().hasSubMenu()) {
                    this.performItemClick((View)listMenuItemView, this.getSelectedItemPosition(), this.getSelectedItemId());
                }
                return true;
            }
            if (listMenuItemView != null && n == this.mRetreatKey) {
                this.setSelection(-1);
                ((MenuAdapter)this.getAdapter()).getAdapterMenu().close(false);
                return true;
            }
            return super.onKeyDown(n, keyEvent);
        }
        
        public void setHoverListener(final MenuItemHoverListener mHoverListener) {
            this.mHoverListener = mHoverListener;
        }
    }
}
