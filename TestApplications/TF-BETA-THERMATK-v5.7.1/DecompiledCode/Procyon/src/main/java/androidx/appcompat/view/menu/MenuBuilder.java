// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

import androidx.core.view.ActionProvider;
import java.util.Collection;
import android.view.KeyCharacterMap$KeyData;
import android.view.KeyEvent;
import android.view.SubMenu;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.content.ComponentName;
import android.view.MenuItem;
import androidx.core.view.ViewConfigurationCompat;
import android.view.ViewConfiguration;
import androidx.core.content.ContextCompat;
import java.util.Iterator;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu$ContextMenuInfo;
import android.content.Context;
import java.util.ArrayList;
import androidx.core.internal.view.SupportMenu;

public class MenuBuilder implements SupportMenu
{
    private static final int[] sCategoryToOrder;
    private ArrayList<MenuItemImpl> mActionItems;
    private Callback mCallback;
    private final Context mContext;
    private ContextMenu$ContextMenuInfo mCurrentMenuInfo;
    private int mDefaultShowAsAction;
    private MenuItemImpl mExpandedItem;
    private boolean mGroupDividerEnabled;
    Drawable mHeaderIcon;
    CharSequence mHeaderTitle;
    View mHeaderView;
    private boolean mIsActionItemsStale;
    private boolean mIsClosing;
    private boolean mIsVisibleItemsStale;
    private ArrayList<MenuItemImpl> mItems;
    private boolean mItemsChangedWhileDispatchPrevented;
    private ArrayList<MenuItemImpl> mNonActionItems;
    private boolean mOptionalIconsVisible;
    private boolean mOverrideVisibleItems;
    private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters;
    private boolean mPreventDispatchingItemsChanged;
    private boolean mQwertyMode;
    private final Resources mResources;
    private boolean mShortcutsVisible;
    private boolean mStructureChangedWhileDispatchPrevented;
    private ArrayList<MenuItemImpl> mTempShortcutItemList;
    private ArrayList<MenuItemImpl> mVisibleItems;
    
    static {
        sCategoryToOrder = new int[] { 1, 4, 5, 3, 2, 0 };
    }
    
    public MenuBuilder(final Context mContext) {
        this.mDefaultShowAsAction = 0;
        this.mPreventDispatchingItemsChanged = false;
        this.mItemsChangedWhileDispatchPrevented = false;
        this.mStructureChangedWhileDispatchPrevented = false;
        this.mOptionalIconsVisible = false;
        this.mIsClosing = false;
        this.mTempShortcutItemList = new ArrayList<MenuItemImpl>();
        this.mPresenters = new CopyOnWriteArrayList<WeakReference<MenuPresenter>>();
        this.mGroupDividerEnabled = false;
        this.mContext = mContext;
        this.mResources = mContext.getResources();
        this.mItems = new ArrayList<MenuItemImpl>();
        this.mVisibleItems = new ArrayList<MenuItemImpl>();
        this.mIsVisibleItemsStale = true;
        this.mActionItems = new ArrayList<MenuItemImpl>();
        this.mNonActionItems = new ArrayList<MenuItemImpl>();
        this.setShortcutsVisibleInner(this.mIsActionItemsStale = true);
    }
    
    private MenuItemImpl createNewMenuItem(final int n, final int n2, final int n3, final int n4, final CharSequence charSequence, final int n5) {
        return new MenuItemImpl(this, n, n2, n3, n4, charSequence, n5);
    }
    
    private void dispatchPresenterUpdate(final boolean b) {
        if (this.mPresenters.isEmpty()) {
            return;
        }
        this.stopDispatchingItemsChanged();
        for (final WeakReference<MenuPresenter> o : this.mPresenters) {
            final MenuPresenter menuPresenter = o.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(o);
            }
            else {
                menuPresenter.updateMenuView(b);
            }
        }
        this.startDispatchingItemsChanged();
    }
    
    private boolean dispatchSubMenuSelected(final SubMenuBuilder subMenuBuilder, final MenuPresenter menuPresenter) {
        final boolean empty = this.mPresenters.isEmpty();
        boolean b = false;
        if (empty) {
            return false;
        }
        if (menuPresenter != null) {
            b = menuPresenter.onSubMenuSelected(subMenuBuilder);
        }
        for (final WeakReference<MenuPresenter> o : this.mPresenters) {
            final MenuPresenter menuPresenter2 = o.get();
            if (menuPresenter2 == null) {
                this.mPresenters.remove(o);
            }
            else {
                if (b) {
                    continue;
                }
                b = menuPresenter2.onSubMenuSelected(subMenuBuilder);
            }
        }
        return b;
    }
    
    private static int findInsertIndex(final ArrayList<MenuItemImpl> list, final int n) {
        for (int i = list.size() - 1; i >= 0; --i) {
            if (list.get(i).getOrdering() <= n) {
                return i + 1;
            }
        }
        return 0;
    }
    
    private static int getOrdering(final int n) {
        final int n2 = (0xFFFF0000 & n) >> 16;
        if (n2 >= 0) {
            final int[] sCategoryToOrder = MenuBuilder.sCategoryToOrder;
            if (n2 < sCategoryToOrder.length) {
                return (n & 0xFFFF) | sCategoryToOrder[n2] << 16;
            }
        }
        throw new IllegalArgumentException("order does not contain a valid category.");
    }
    
    private void removeItemAtInt(final int index, final boolean b) {
        if (index >= 0) {
            if (index < this.mItems.size()) {
                this.mItems.remove(index);
                if (b) {
                    this.onItemsChanged(true);
                }
            }
        }
    }
    
    private void setHeaderInternal(final int n, final CharSequence mHeaderTitle, final int n2, final Drawable mHeaderIcon, final View mHeaderView) {
        final Resources resources = this.getResources();
        if (mHeaderView != null) {
            this.mHeaderView = mHeaderView;
            this.mHeaderTitle = null;
            this.mHeaderIcon = null;
        }
        else {
            if (n > 0) {
                this.mHeaderTitle = resources.getText(n);
            }
            else if (mHeaderTitle != null) {
                this.mHeaderTitle = mHeaderTitle;
            }
            if (n2 > 0) {
                this.mHeaderIcon = ContextCompat.getDrawable(this.getContext(), n2);
            }
            else if (mHeaderIcon != null) {
                this.mHeaderIcon = mHeaderIcon;
            }
            this.mHeaderView = null;
        }
        this.onItemsChanged(false);
    }
    
    private void setShortcutsVisibleInner(final boolean b) {
        final boolean b2 = true;
        this.mShortcutsVisible = (b && this.mResources.getConfiguration().keyboard != 1 && ViewConfigurationCompat.shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration.get(this.mContext), this.mContext) && b2);
    }
    
    public MenuItem add(final int n) {
        return this.addInternal(0, 0, 0, this.mResources.getString(n));
    }
    
    public MenuItem add(final int n, final int n2, final int n3, final int n4) {
        return this.addInternal(n, n2, n3, this.mResources.getString(n4));
    }
    
    public MenuItem add(final int n, final int n2, final int n3, final CharSequence charSequence) {
        return this.addInternal(n, n2, n3, charSequence);
    }
    
    public MenuItem add(final CharSequence charSequence) {
        return this.addInternal(0, 0, 0, charSequence);
    }
    
    public int addIntentOptions(final int n, final int n2, final int n3, final ComponentName componentName, final Intent[] array, final Intent intent, int n4, final MenuItem[] array2) {
        final PackageManager packageManager = this.mContext.getPackageManager();
        final int n5 = 0;
        final List queryIntentActivityOptions = packageManager.queryIntentActivityOptions(componentName, array, intent, 0);
        int size;
        if (queryIntentActivityOptions != null) {
            size = queryIntentActivityOptions.size();
        }
        else {
            size = 0;
        }
        int i = n5;
        if ((n4 & 0x1) == 0x0) {
            this.removeGroup(n);
            i = n5;
        }
        while (i < size) {
            final ResolveInfo resolveInfo = queryIntentActivityOptions.get(i);
            n4 = resolveInfo.specificIndex;
            Intent intent2;
            if (n4 < 0) {
                intent2 = intent;
            }
            else {
                intent2 = array[n4];
            }
            final Intent intent3 = new Intent(intent2);
            intent3.setComponent(new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name));
            final MenuItem setIntent = this.add(n, n2, n3, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent(intent3);
            if (array2 != null) {
                n4 = resolveInfo.specificIndex;
                if (n4 >= 0) {
                    array2[n4] = setIntent;
                }
            }
            ++i;
        }
        return size;
    }
    
    protected MenuItem addInternal(final int n, final int n2, final int n3, final CharSequence charSequence) {
        final int ordering = getOrdering(n3);
        final MenuItemImpl newMenuItem = this.createNewMenuItem(n, n2, n3, ordering, charSequence, this.mDefaultShowAsAction);
        final ContextMenu$ContextMenuInfo mCurrentMenuInfo = this.mCurrentMenuInfo;
        if (mCurrentMenuInfo != null) {
            newMenuItem.setMenuInfo(mCurrentMenuInfo);
        }
        final ArrayList<MenuItemImpl> mItems = this.mItems;
        mItems.add(findInsertIndex(mItems, ordering), newMenuItem);
        this.onItemsChanged(true);
        return (MenuItem)newMenuItem;
    }
    
    public void addMenuPresenter(final MenuPresenter referent, final Context context) {
        this.mPresenters.add(new WeakReference<MenuPresenter>(referent));
        referent.initForMenu(context, this);
        this.mIsActionItemsStale = true;
    }
    
    public SubMenu addSubMenu(final int n) {
        return this.addSubMenu(0, 0, 0, this.mResources.getString(n));
    }
    
    public SubMenu addSubMenu(final int n, final int n2, final int n3, final int n4) {
        return this.addSubMenu(n, n2, n3, this.mResources.getString(n4));
    }
    
    public SubMenu addSubMenu(final int n, final int n2, final int n3, final CharSequence charSequence) {
        final MenuItemImpl menuItemImpl = (MenuItemImpl)this.addInternal(n, n2, n3, charSequence);
        final SubMenuBuilder subMenu = new SubMenuBuilder(this.mContext, this, menuItemImpl);
        menuItemImpl.setSubMenu(subMenu);
        return (SubMenu)subMenu;
    }
    
    public SubMenu addSubMenu(final CharSequence charSequence) {
        return this.addSubMenu(0, 0, 0, charSequence);
    }
    
    public void changeMenuMode() {
        final Callback mCallback = this.mCallback;
        if (mCallback != null) {
            mCallback.onMenuModeChange(this);
        }
    }
    
    public void clear() {
        final MenuItemImpl mExpandedItem = this.mExpandedItem;
        if (mExpandedItem != null) {
            this.collapseItemActionView(mExpandedItem);
        }
        this.mItems.clear();
        this.onItemsChanged(true);
    }
    
    public void clearHeader() {
        this.mHeaderIcon = null;
        this.mHeaderTitle = null;
        this.mHeaderView = null;
        this.onItemsChanged(false);
    }
    
    public void close() {
        this.close(true);
    }
    
    public final void close(final boolean b) {
        if (this.mIsClosing) {
            return;
        }
        this.mIsClosing = true;
        for (final WeakReference<MenuPresenter> o : this.mPresenters) {
            final MenuPresenter menuPresenter = o.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(o);
            }
            else {
                menuPresenter.onCloseMenu(this, b);
            }
        }
        this.mIsClosing = false;
    }
    
    public boolean collapseItemActionView(final MenuItemImpl menuItemImpl) {
        final boolean empty = this.mPresenters.isEmpty();
        final boolean b = false;
        final int n = 0;
        boolean b2 = b;
        if (!empty) {
            if (this.mExpandedItem != menuItemImpl) {
                b2 = b;
            }
            else {
                this.stopDispatchingItemsChanged();
                final Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
                boolean collapseItemActionView = n != 0;
                boolean b3;
                while (true) {
                    b3 = collapseItemActionView;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    final WeakReference<MenuPresenter> o = iterator.next();
                    final MenuPresenter menuPresenter = o.get();
                    if (menuPresenter == null) {
                        this.mPresenters.remove(o);
                    }
                    else {
                        b3 = (collapseItemActionView = menuPresenter.collapseItemActionView(this, menuItemImpl));
                        if (b3) {
                            break;
                        }
                        continue;
                    }
                }
                this.startDispatchingItemsChanged();
                b2 = b3;
                if (b3) {
                    this.mExpandedItem = null;
                    b2 = b3;
                }
            }
        }
        return b2;
    }
    
    boolean dispatchMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        final Callback mCallback = this.mCallback;
        return mCallback != null && mCallback.onMenuItemSelected(menuBuilder, menuItem);
    }
    
    public boolean expandItemActionView(final MenuItemImpl mExpandedItem) {
        final boolean empty = this.mPresenters.isEmpty();
        boolean expandItemActionView = false;
        if (empty) {
            return false;
        }
        this.stopDispatchingItemsChanged();
        final Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
        boolean b;
        while (true) {
            b = expandItemActionView;
            if (!iterator.hasNext()) {
                break;
            }
            final WeakReference<MenuPresenter> o = iterator.next();
            final MenuPresenter menuPresenter = o.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(o);
            }
            else {
                b = (expandItemActionView = menuPresenter.expandItemActionView(this, mExpandedItem));
                if (b) {
                    break;
                }
                continue;
            }
        }
        this.startDispatchingItemsChanged();
        if (b) {
            this.mExpandedItem = mExpandedItem;
        }
        return b;
    }
    
    public int findGroupIndex(final int n) {
        return this.findGroupIndex(n, 0);
    }
    
    public int findGroupIndex(final int n, final int n2) {
        final int size = this.size();
        int i = n2;
        if (n2 < 0) {
            i = 0;
        }
        while (i < size) {
            if (this.mItems.get(i).getGroupId() == n) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public MenuItem findItem(final int n) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getItemId() == n) {
                return (MenuItem)menuItemImpl;
            }
            if (menuItemImpl.hasSubMenu()) {
                final MenuItem item = menuItemImpl.getSubMenu().findItem(n);
                if (item != null) {
                    return item;
                }
            }
        }
        return null;
    }
    
    public int findItemIndex(final int n) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.mItems.get(i).getItemId() == n) {
                return i;
            }
        }
        return -1;
    }
    
    MenuItemImpl findItemWithShortcutForKey(final int n, final KeyEvent keyEvent) {
        final ArrayList<MenuItemImpl> mTempShortcutItemList = this.mTempShortcutItemList;
        mTempShortcutItemList.clear();
        this.findItemsWithShortcutForKey(mTempShortcutItemList, n, keyEvent);
        if (mTempShortcutItemList.isEmpty()) {
            return null;
        }
        final int metaState = keyEvent.getMetaState();
        final KeyCharacterMap$KeyData keyCharacterMap$KeyData = new KeyCharacterMap$KeyData();
        keyEvent.getKeyData(keyCharacterMap$KeyData);
        final int size = mTempShortcutItemList.size();
        if (size == 1) {
            return mTempShortcutItemList.get(0);
        }
        final boolean qwertyMode = this.isQwertyMode();
        for (int i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = mTempShortcutItemList.get(i);
            char c;
            if (qwertyMode) {
                c = menuItemImpl.getAlphabeticShortcut();
            }
            else {
                c = menuItemImpl.getNumericShortcut();
            }
            if ((c == keyCharacterMap$KeyData.meta[0] && (metaState & 0x2) == 0x0) || (c == keyCharacterMap$KeyData.meta[2] && (metaState & 0x2) != 0x0) || (qwertyMode && c == '\b' && n == 67)) {
                return menuItemImpl;
            }
        }
        return null;
    }
    
    void findItemsWithShortcutForKey(final List<MenuItemImpl> list, final int n, final KeyEvent keyEvent) {
        final boolean qwertyMode = this.isQwertyMode();
        final int modifiers = keyEvent.getModifiers();
        final KeyCharacterMap$KeyData keyCharacterMap$KeyData = new KeyCharacterMap$KeyData();
        if (!keyEvent.getKeyData(keyCharacterMap$KeyData) && n != 67) {
            return;
        }
        for (int size = this.mItems.size(), i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.hasSubMenu()) {
                ((MenuBuilder)menuItemImpl.getSubMenu()).findItemsWithShortcutForKey(list, n, keyEvent);
            }
            char c;
            if (qwertyMode) {
                c = menuItemImpl.getAlphabeticShortcut();
            }
            else {
                c = menuItemImpl.getNumericShortcut();
            }
            int n2;
            if (qwertyMode) {
                n2 = menuItemImpl.getAlphabeticModifiers();
            }
            else {
                n2 = menuItemImpl.getNumericModifiers();
            }
            if ((modifiers & 0x1100F) == (n2 & 0x1100F) && c != '\0') {
                final char[] meta = keyCharacterMap$KeyData.meta;
                if ((c == meta[0] || c == meta[2] || (qwertyMode && c == '\b' && n == 67)) && menuItemImpl.isEnabled()) {
                    list.add(menuItemImpl);
                }
            }
        }
    }
    
    public void flagActionItems() {
        final ArrayList<MenuItemImpl> visibleItems = this.getVisibleItems();
        if (!this.mIsActionItemsStale) {
            return;
        }
        final Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final WeakReference<MenuPresenter> o = iterator.next();
            final MenuPresenter menuPresenter = o.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(o);
            }
            else {
                b |= menuPresenter.flagActionItems();
            }
        }
        if (b) {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            for (int size = visibleItems.size(), i = 0; i < size; ++i) {
                final MenuItemImpl menuItemImpl = visibleItems.get(i);
                if (menuItemImpl.isActionButton()) {
                    this.mActionItems.add(menuItemImpl);
                }
                else {
                    this.mNonActionItems.add(menuItemImpl);
                }
            }
        }
        else {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            this.mNonActionItems.addAll(this.getVisibleItems());
        }
        this.mIsActionItemsStale = false;
    }
    
    public ArrayList<MenuItemImpl> getActionItems() {
        this.flagActionItems();
        return this.mActionItems;
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public MenuItemImpl getExpandedItem() {
        return this.mExpandedItem;
    }
    
    public CharSequence getHeaderTitle() {
        return this.mHeaderTitle;
    }
    
    public MenuItem getItem(final int index) {
        return (MenuItem)this.mItems.get(index);
    }
    
    public ArrayList<MenuItemImpl> getNonActionItems() {
        this.flagActionItems();
        return this.mNonActionItems;
    }
    
    boolean getOptionalIconsVisible() {
        return this.mOptionalIconsVisible;
    }
    
    Resources getResources() {
        return this.mResources;
    }
    
    public MenuBuilder getRootMenu() {
        return this;
    }
    
    public ArrayList<MenuItemImpl> getVisibleItems() {
        if (!this.mIsVisibleItemsStale) {
            return this.mVisibleItems;
        }
        this.mVisibleItems.clear();
        for (int size = this.mItems.size(), i = 0; i < size; ++i) {
            final MenuItemImpl e = this.mItems.get(i);
            if (e.isVisible()) {
                this.mVisibleItems.add(e);
            }
        }
        this.mIsVisibleItemsStale = false;
        this.mIsActionItemsStale = true;
        return this.mVisibleItems;
    }
    
    public boolean hasVisibleItems() {
        if (this.mOverrideVisibleItems) {
            return true;
        }
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.mItems.get(i).isVisible()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isGroupDividerEnabled() {
        return this.mGroupDividerEnabled;
    }
    
    boolean isQwertyMode() {
        return this.mQwertyMode;
    }
    
    public boolean isShortcutKey(final int n, final KeyEvent keyEvent) {
        return this.findItemWithShortcutForKey(n, keyEvent) != null;
    }
    
    public boolean isShortcutsVisible() {
        return this.mShortcutsVisible;
    }
    
    void onItemActionRequestChanged(final MenuItemImpl menuItemImpl) {
        this.onItemsChanged(this.mIsActionItemsStale = true);
    }
    
    void onItemVisibleChanged(final MenuItemImpl menuItemImpl) {
        this.onItemsChanged(this.mIsVisibleItemsStale = true);
    }
    
    public void onItemsChanged(final boolean b) {
        if (!this.mPreventDispatchingItemsChanged) {
            if (b) {
                this.mIsVisibleItemsStale = true;
                this.mIsActionItemsStale = true;
            }
            this.dispatchPresenterUpdate(b);
        }
        else {
            this.mItemsChangedWhileDispatchPrevented = true;
            if (b) {
                this.mStructureChangedWhileDispatchPrevented = true;
            }
        }
    }
    
    public boolean performIdentifierAction(final int n, final int n2) {
        return this.performItemAction(this.findItem(n), n2);
    }
    
    public boolean performItemAction(final MenuItem menuItem, final int n) {
        return this.performItemAction(menuItem, null, n);
    }
    
    public boolean performItemAction(final MenuItem menuItem, final MenuPresenter menuPresenter, final int n) {
        final MenuItemImpl menuItemImpl = (MenuItemImpl)menuItem;
        if (menuItemImpl != null && menuItemImpl.isEnabled()) {
            final boolean invoke = menuItemImpl.invoke();
            final ActionProvider supportActionProvider = menuItemImpl.getSupportActionProvider();
            final boolean b = supportActionProvider != null && supportActionProvider.hasSubMenu();
            boolean b3;
            if (menuItemImpl.hasCollapsibleActionView()) {
                final boolean b2 = b3 = (invoke | menuItemImpl.expandActionView());
                if (b2) {
                    this.close(true);
                    b3 = b2;
                }
            }
            else if (!menuItemImpl.hasSubMenu() && !b) {
                b3 = invoke;
                if ((n & 0x1) == 0x0) {
                    this.close(true);
                    b3 = invoke;
                }
            }
            else {
                if ((n & 0x4) == 0x0) {
                    this.close(false);
                }
                if (!menuItemImpl.hasSubMenu()) {
                    menuItemImpl.setSubMenu(new SubMenuBuilder(this.getContext(), this, menuItemImpl));
                }
                final SubMenuBuilder subMenuBuilder = (SubMenuBuilder)menuItemImpl.getSubMenu();
                if (b) {
                    supportActionProvider.onPrepareSubMenu((SubMenu)subMenuBuilder);
                }
                final boolean b4 = b3 = (invoke | this.dispatchSubMenuSelected(subMenuBuilder, menuPresenter));
                if (!b4) {
                    this.close(true);
                    b3 = b4;
                }
            }
            return b3;
        }
        return false;
    }
    
    public boolean performShortcut(final int n, final KeyEvent keyEvent, final int n2) {
        final MenuItemImpl itemWithShortcutForKey = this.findItemWithShortcutForKey(n, keyEvent);
        final boolean b = itemWithShortcutForKey != null && this.performItemAction((MenuItem)itemWithShortcutForKey, n2);
        if ((n2 & 0x2) != 0x0) {
            this.close(true);
        }
        return b;
    }
    
    public void removeGroup(final int n) {
        final int groupIndex = this.findGroupIndex(n);
        if (groupIndex >= 0) {
            for (int size = this.mItems.size(), n2 = 0; n2 < size - groupIndex && this.mItems.get(groupIndex).getGroupId() == n; ++n2) {
                this.removeItemAtInt(groupIndex, false);
            }
            this.onItemsChanged(true);
        }
    }
    
    public void removeItem(final int n) {
        this.removeItemAtInt(this.findItemIndex(n), true);
    }
    
    public void removeMenuPresenter(final MenuPresenter menuPresenter) {
        for (final WeakReference<MenuPresenter> o : this.mPresenters) {
            final MenuPresenter menuPresenter2 = o.get();
            if (menuPresenter2 == null || menuPresenter2 == menuPresenter) {
                this.mPresenters.remove(o);
            }
        }
    }
    
    public void setCallback(final Callback mCallback) {
        this.mCallback = mCallback;
    }
    
    void setExclusiveItemChecked(final MenuItem menuItem) {
        final int groupId = menuItem.getGroupId();
        final int size = this.mItems.size();
        this.stopDispatchingItemsChanged();
        for (int i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == groupId) {
                if (menuItemImpl.isExclusiveCheckable()) {
                    if (menuItemImpl.isCheckable()) {
                        menuItemImpl.setCheckedInt(menuItemImpl == menuItem);
                    }
                }
            }
        }
        this.startDispatchingItemsChanged();
    }
    
    public void setGroupCheckable(final int n, final boolean checkable, final boolean exclusiveCheckable) {
        for (int size = this.mItems.size(), i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == n) {
                menuItemImpl.setExclusiveCheckable(exclusiveCheckable);
                menuItemImpl.setCheckable(checkable);
            }
        }
    }
    
    public void setGroupDividerEnabled(final boolean mGroupDividerEnabled) {
        this.mGroupDividerEnabled = mGroupDividerEnabled;
    }
    
    public void setGroupEnabled(final int n, final boolean enabled) {
        for (int size = this.mItems.size(), i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == n) {
                menuItemImpl.setEnabled(enabled);
            }
        }
    }
    
    public void setGroupVisible(final int n, final boolean visibleInt) {
        final int size = this.mItems.size();
        int i = 0;
        int n2 = 0;
        while (i < size) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            int n3 = n2;
            if (menuItemImpl.getGroupId() == n) {
                n3 = n2;
                if (menuItemImpl.setVisibleInt(visibleInt)) {
                    n3 = 1;
                }
            }
            ++i;
            n2 = n3;
        }
        if (n2 != 0) {
            this.onItemsChanged(true);
        }
    }
    
    protected MenuBuilder setHeaderIconInt(final int n) {
        this.setHeaderInternal(0, null, n, null, null);
        return this;
    }
    
    protected MenuBuilder setHeaderIconInt(final Drawable drawable) {
        this.setHeaderInternal(0, null, 0, drawable, null);
        return this;
    }
    
    protected MenuBuilder setHeaderTitleInt(final int n) {
        this.setHeaderInternal(n, null, 0, null, null);
        return this;
    }
    
    protected MenuBuilder setHeaderTitleInt(final CharSequence charSequence) {
        this.setHeaderInternal(0, charSequence, 0, null, null);
        return this;
    }
    
    protected MenuBuilder setHeaderViewInt(final View view) {
        this.setHeaderInternal(0, null, 0, null, view);
        return this;
    }
    
    public void setQwertyMode(final boolean mQwertyMode) {
        this.mQwertyMode = mQwertyMode;
        this.onItemsChanged(false);
    }
    
    public int size() {
        return this.mItems.size();
    }
    
    public void startDispatchingItemsChanged() {
        this.mPreventDispatchingItemsChanged = false;
        if (this.mItemsChangedWhileDispatchPrevented) {
            this.mItemsChangedWhileDispatchPrevented = false;
            this.onItemsChanged(this.mStructureChangedWhileDispatchPrevented);
        }
    }
    
    public void stopDispatchingItemsChanged() {
        if (!this.mPreventDispatchingItemsChanged) {
            this.mPreventDispatchingItemsChanged = true;
            this.mItemsChangedWhileDispatchPrevented = false;
            this.mStructureChangedWhileDispatchPrevented = false;
        }
    }
    
    public interface Callback
    {
        boolean onMenuItemSelected(final MenuBuilder p0, final MenuItem p1);
        
        void onMenuModeChange(final MenuBuilder p0);
    }
    
    public interface ItemInvoker
    {
        boolean invokeItem(final MenuItemImpl p0);
    }
}
