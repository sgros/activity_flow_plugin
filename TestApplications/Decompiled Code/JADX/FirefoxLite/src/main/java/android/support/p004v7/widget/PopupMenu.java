package android.support.p004v7.widget;

import android.content.Context;
import android.support.p004v7.appcompat.C0187R;
import android.support.p004v7.view.SupportMenuInflater;
import android.support.p004v7.view.menu.MenuBuilder;
import android.support.p004v7.view.menu.MenuBuilder.Callback;
import android.support.p004v7.view.menu.MenuPopupHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow.OnDismissListener;

/* renamed from: android.support.v7.widget.PopupMenu */
public class PopupMenu {
    private final View mAnchor;
    private final Context mContext;
    private final MenuBuilder mMenu;
    OnMenuItemClickListener mMenuItemClickListener;
    OnDismissListener mOnDismissListener;
    final MenuPopupHelper mPopup;

    /* renamed from: android.support.v7.widget.PopupMenu$2 */
    class C02292 implements OnDismissListener {
        C02292() {
        }

        public void onDismiss() {
            if (PopupMenu.this.mOnDismissListener != null) {
                PopupMenu.this.mOnDismissListener.onDismiss(PopupMenu.this);
            }
        }
    }

    /* renamed from: android.support.v7.widget.PopupMenu$OnDismissListener */
    public interface OnDismissListener {
        void onDismiss(PopupMenu popupMenu);
    }

    /* renamed from: android.support.v7.widget.PopupMenu$OnMenuItemClickListener */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    /* renamed from: android.support.v7.widget.PopupMenu$1 */
    class C02301 implements Callback {
        public void onMenuModeChange(MenuBuilder menuBuilder) {
        }

        C02301() {
        }

        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            return PopupMenu.this.mMenuItemClickListener != null ? PopupMenu.this.mMenuItemClickListener.onMenuItemClick(menuItem) : false;
        }
    }

    public PopupMenu(Context context, View view) {
        this(context, view, 0);
    }

    public PopupMenu(Context context, View view, int i) {
        this(context, view, i, C0187R.attr.popupMenuStyle, 0);
    }

    public PopupMenu(Context context, View view, int i, int i2, int i3) {
        this.mContext = context;
        this.mAnchor = view;
        this.mMenu = new MenuBuilder(context);
        this.mMenu.setCallback(new C02301());
        this.mPopup = new MenuPopupHelper(context, this.mMenu, view, false, i2, i3);
        this.mPopup.setGravity(i);
        this.mPopup.setOnDismissListener(new C02292());
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContext);
    }

    public void inflate(int i) {
        getMenuInflater().inflate(i, this.mMenu);
    }

    public void show() {
        this.mPopup.show();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
    }
}
