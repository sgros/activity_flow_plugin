// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.view.View$OnKeyListener;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.text.TextUtils$TruncateAt;
import android.view.View$MeasureSpec;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View$OnClickListener;
import android.widget.LinearLayout$LayoutParams;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import android.view.View$OnTouchListener;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.ImageView$ScaleType;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.graphics.Rect;
import org.telegram.ui.Components.CloseProgressDrawable2;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class ActionBarMenuItem extends FrameLayout
{
    private int additionalOffset;
    private boolean allowCloseAnimation;
    private boolean animateClear;
    private boolean animationEnabled;
    private ImageView clearButton;
    private ActionBarMenuItemDelegate delegate;
    protected ImageView iconView;
    private boolean ignoreOnTextChange;
    private boolean isSearchField;
    private boolean layoutInScreen;
    private ActionBarMenuItemSearchListener listener;
    private int[] location;
    private boolean longClickEnabled;
    protected boolean overrideMenuClick;
    private ActionBarMenu parentMenu;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout;
    private ActionBarPopupWindow popupWindow;
    private boolean processedPopupClick;
    private CloseProgressDrawable2 progressDrawable;
    private Rect rect;
    private FrameLayout searchContainer;
    private EditTextBoldCursor searchField;
    private TextView searchFieldCaption;
    private View selectedMenuView;
    private Runnable showMenuRunnable;
    private int subMenuOpenSide;
    private int yOffset;
    
    public ActionBarMenuItem(final Context context, final ActionBarMenu parentMenu, final int n, final int n2) {
        super(context);
        this.allowCloseAnimation = true;
        this.animationEnabled = true;
        this.longClickEnabled = true;
        this.animateClear = true;
        if (n != 0) {
            this.setBackgroundDrawable(Theme.createSelectorDrawable(n));
        }
        this.parentMenu = parentMenu;
        (this.iconView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.addView((View)this.iconView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        if (n2 != 0) {
            this.iconView.setColorFilter((ColorFilter)new PorterDuffColorFilter(n2, PorterDuff$Mode.MULTIPLY));
        }
    }
    
    private void createPopupLayout() {
        if (this.popupLayout != null) {
            return;
        }
        this.rect = new Rect();
        this.location = new int[2];
        (this.popupLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.getContext())).setOnTouchListener((View$OnTouchListener)new _$$Lambda$ActionBarMenuItem$NEgOkBBuNcW0duabIaP5FYMgH5w(this));
        this.popupLayout.setDispatchKeyEventListener(new _$$Lambda$ActionBarMenuItem$yNUlqFN00bPmohrcR1AdkblquoQ(this));
    }
    
    private void updateOrShowPopup(final boolean b, final boolean b2) {
        final ActionBarMenu parentMenu = this.parentMenu;
        int n;
        int n2;
        if (parentMenu != null) {
            n = -parentMenu.parentActionBar.getMeasuredHeight() + this.parentMenu.getTop();
            n2 = this.parentMenu.getPaddingTop();
        }
        else {
            final float scaleY = this.getScaleY();
            final float n3 = (float)this.getMeasuredHeight();
            float translationY;
            if (this.subMenuOpenSide != 2) {
                translationY = this.getTranslationY();
            }
            else {
                translationY = 0.0f;
            }
            n = -(int)(n3 * scaleY - translationY / scaleY);
            n2 = this.additionalOffset;
        }
        final int n4 = n + n2 + this.yOffset;
        if (b) {
            this.popupLayout.scrollToTop();
        }
        final ActionBarMenu parentMenu2 = this.parentMenu;
        if (parentMenu2 != null) {
            final ActionBar parentActionBar = parentMenu2.parentActionBar;
            if (this.subMenuOpenSide == 0) {
                if (b) {
                    this.popupWindow.showAsDropDown((View)parentActionBar, this.getLeft() + this.parentMenu.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth() + (int)this.getTranslationX(), n4);
                }
                if (b2) {
                    this.popupWindow.update((View)parentActionBar, this.getLeft() + this.parentMenu.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth() + (int)this.getTranslationX(), n4, -1, -1);
                }
            }
            else {
                if (b) {
                    this.popupWindow.showAsDropDown((View)parentActionBar, this.getLeft() - AndroidUtilities.dp(8.0f) + (int)this.getTranslationX(), n4);
                }
                if (b2) {
                    this.popupWindow.update((View)parentActionBar, this.getLeft() - AndroidUtilities.dp(8.0f) + (int)this.getTranslationX(), n4, -1, -1);
                }
            }
        }
        else {
            final int subMenuOpenSide = this.subMenuOpenSide;
            if (subMenuOpenSide == 0) {
                if (this.getParent() != null) {
                    final View view = (View)this.getParent();
                    if (b) {
                        this.popupWindow.showAsDropDown(view, this.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), n4);
                    }
                    if (b2) {
                        this.popupWindow.update(view, this.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), n4, -1, -1);
                    }
                }
            }
            else if (subMenuOpenSide == 1) {
                if (b) {
                    this.popupWindow.showAsDropDown((View)this, -AndroidUtilities.dp(8.0f), n4);
                }
                if (b2) {
                    this.popupWindow.update((View)this, -AndroidUtilities.dp(8.0f), n4, -1, -1);
                }
            }
            else {
                if (b) {
                    this.popupWindow.showAsDropDown((View)this, this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), n4);
                }
                if (b2) {
                    this.popupWindow.update((View)this, this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), n4, -1, -1);
                }
            }
        }
    }
    
    public TextView addSubItem(final int i, final CharSequence text) {
        this.createPopupLayout();
        final TextView textView = new TextView(this.getContext());
        textView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (!LocaleController.isRTL) {
            textView.setGravity(16);
        }
        else {
            textView.setGravity(21);
        }
        textView.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
        textView.setTextSize(1, 16.0f);
        textView.setMinWidth(AndroidUtilities.dp(196.0f));
        textView.setTag((Object)i);
        textView.setText(text);
        this.popupLayout.addView((View)textView);
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)textView.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(48.0f);
        textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$ActionBarMenuItem$2OXLLOK5tdU8k_iUBLX7uda6XhM(this));
        return textView;
    }
    
    public ActionBarMenuSubItem addSubItem(final int i, final int n, final CharSequence charSequence) {
        this.createPopupLayout();
        final ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(this.getContext());
        actionBarMenuSubItem.setTextAndIcon(charSequence, n);
        actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarMenuSubItem.setTag((Object)i);
        this.popupLayout.addView((View)actionBarMenuSubItem);
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)actionBarMenuSubItem.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(48.0f);
        actionBarMenuSubItem.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        actionBarMenuSubItem.setOnClickListener((View$OnClickListener)new _$$Lambda$ActionBarMenuItem$37smKkAzmohk63TzSJQopQof02I(this));
        return actionBarMenuSubItem;
    }
    
    public void addSubItem(final int i, final View view, final int n, final int n2) {
        this.createPopupLayout();
        view.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(n, n2));
        this.popupLayout.addView(view);
        view.setTag((Object)i);
        view.setOnClickListener((View$OnClickListener)new _$$Lambda$ActionBarMenuItem$9Z_bd6EyKyDSvICVkiunmSXpJnY(this));
        view.setBackgroundDrawable(Theme.getSelectorDrawable(false));
    }
    
    public void addSubItem(final View view, final int n, final int n2) {
        this.createPopupLayout();
        this.popupLayout.addView(view, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(n, n2));
    }
    
    public void clearSearchText() {
        final EditTextBoldCursor searchField = this.searchField;
        if (searchField == null) {
            return;
        }
        searchField.setText((CharSequence)"");
    }
    
    public void closeSubMenu() {
        final ActionBarPopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.popupWindow.dismiss();
        }
    }
    
    public ImageView getImageView() {
        return this.iconView;
    }
    
    public EditTextBoldCursor getSearchField() {
        return this.searchField;
    }
    
    public boolean hasSubMenu() {
        return this.popupLayout != null;
    }
    
    public void hideSubItem(final int i) {
        final ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout = this.popupLayout;
        if (popupLayout == null) {
            return;
        }
        final View viewWithTag = popupLayout.findViewWithTag((Object)i);
        if (viewWithTag != null && viewWithTag.getVisibility() != 8) {
            viewWithTag.setVisibility(8);
        }
    }
    
    public boolean isSearchField() {
        return this.isSearchField;
    }
    
    public boolean isSubItemVisible(final int i) {
        final ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout = this.popupLayout;
        final boolean b = false;
        if (popupLayout == null) {
            return false;
        }
        final View viewWithTag = popupLayout.findViewWithTag((Object)i);
        boolean b2 = b;
        if (viewWithTag != null) {
            b2 = b;
            if (viewWithTag.getVisibility() == 0) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.ImageButton");
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        final ActionBarPopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null && popupWindow.isShowing()) {
            this.updateOrShowPopup(false, true);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            if (this.longClickEnabled && this.hasSubMenu()) {
                final ActionBarPopupWindow popupWindow = this.popupWindow;
                if (popupWindow == null || (popupWindow != null && !popupWindow.isShowing())) {
                    AndroidUtilities.runOnUIThread(this.showMenuRunnable = new _$$Lambda$ActionBarMenuItem$Y4Ro71_Kozj7zLr98hi2EHrr4_A(this), 200L);
                }
            }
        }
        else if (motionEvent.getActionMasked() == 2) {
            if (this.hasSubMenu()) {
                final ActionBarPopupWindow popupWindow2 = this.popupWindow;
                if (popupWindow2 == null || (popupWindow2 != null && !popupWindow2.isShowing())) {
                    if (motionEvent.getY() > this.getHeight()) {
                        if (this.getParent() != null) {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        this.toggleSubMenu();
                        return true;
                    }
                    return super.onTouchEvent(motionEvent);
                }
            }
            final ActionBarPopupWindow popupWindow3 = this.popupWindow;
            if (popupWindow3 != null && popupWindow3.isShowing()) {
                this.getLocationOnScreen(this.location);
                final float x = motionEvent.getX();
                final float n = (float)this.location[0];
                final float y = motionEvent.getY();
                final int[] location = this.location;
                final float n2 = (float)location[1];
                this.popupLayout.getLocationOnScreen(location);
                final int[] location2 = this.location;
                final float n3 = x + n - location2[0];
                final float n4 = y + n2 - location2[1];
                this.selectedMenuView = null;
                for (int i = 0; i < this.popupLayout.getItemsCount(); ++i) {
                    final View item = this.popupLayout.getItemAt(i);
                    item.getHitRect(this.rect);
                    if ((int)item.getTag() < 100) {
                        if (!this.rect.contains((int)n3, (int)n4)) {
                            item.setPressed(false);
                            item.setSelected(false);
                            if (Build$VERSION.SDK_INT == 21) {
                                item.getBackground().setVisible(false, false);
                            }
                        }
                        else {
                            item.setPressed(true);
                            item.setSelected(true);
                            final int sdk_INT = Build$VERSION.SDK_INT;
                            if (sdk_INT >= 21) {
                                if (sdk_INT == 21) {
                                    item.getBackground().setVisible(true, false);
                                }
                                item.drawableHotspotChanged(n3, n4 - item.getTop());
                            }
                            this.selectedMenuView = item;
                        }
                    }
                }
            }
        }
        else {
            final ActionBarPopupWindow popupWindow4 = this.popupWindow;
            if (popupWindow4 != null && popupWindow4.isShowing() && motionEvent.getActionMasked() == 1) {
                final View selectedMenuView = this.selectedMenuView;
                if (selectedMenuView != null) {
                    selectedMenuView.setSelected(false);
                    final ActionBarMenu parentMenu = this.parentMenu;
                    if (parentMenu != null) {
                        parentMenu.onItemClick((int)this.selectedMenuView.getTag());
                    }
                    else {
                        final ActionBarMenuItemDelegate delegate = this.delegate;
                        if (delegate != null) {
                            delegate.onItemClick((int)this.selectedMenuView.getTag());
                        }
                    }
                    this.popupWindow.dismiss(this.allowCloseAnimation);
                }
                else {
                    this.popupWindow.dismiss();
                }
            }
            else {
                final View selectedMenuView2 = this.selectedMenuView;
                if (selectedMenuView2 != null) {
                    selectedMenuView2.setSelected(false);
                    this.selectedMenuView = null;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void openSearch(final boolean b) {
        final FrameLayout searchContainer = this.searchContainer;
        if (searchContainer != null && searchContainer.getVisibility() != 0) {
            final ActionBarMenu parentMenu = this.parentMenu;
            if (parentMenu != null) {
                parentMenu.parentActionBar.onSearchFieldVisibilityChanged(this.toggleSearch(b));
            }
        }
    }
    
    public void redrawPopup(final int n) {
        final ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout = this.popupLayout;
        if (popupLayout != null) {
            popupLayout.backgroundDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
            this.popupLayout.invalidate();
        }
    }
    
    public ActionBarMenuItem setActionBarMenuItemSearchListener(final ActionBarMenuItemSearchListener listener) {
        this.listener = listener;
        return this;
    }
    
    public void setAdditionalOffset(final int additionalOffset) {
        this.additionalOffset = additionalOffset;
    }
    
    public ActionBarMenuItem setAllowCloseAnimation(final boolean allowCloseAnimation) {
        this.allowCloseAnimation = allowCloseAnimation;
        return this;
    }
    
    public void setDelegate(final ActionBarMenuItemDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setIcon(final int imageResource) {
        this.iconView.setImageResource(imageResource);
    }
    
    public void setIcon(final Drawable imageDrawable) {
        this.iconView.setImageDrawable(imageDrawable);
    }
    
    public void setIconColor(final int n) {
        this.iconView.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
        final ImageView clearButton = this.clearButton;
        if (clearButton != null) {
            clearButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
        }
    }
    
    public void setIgnoreOnTextChange() {
        this.ignoreOnTextChange = true;
    }
    
    public ActionBarMenuItem setIsSearchField(final boolean isSearchField) {
        if (this.parentMenu == null) {
            return this;
        }
        if (isSearchField && this.searchContainer == null) {
            this.searchContainer = new FrameLayout(this.getContext()) {
                protected void onLayout(final boolean b, int measuredWidth, final int n, final int n2, final int n3) {
                    super.onLayout(b, measuredWidth, n, n2, n3);
                    final boolean isRTL = LocaleController.isRTL;
                    measuredWidth = 0;
                    if (!isRTL) {
                        if (ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0) {
                            measuredWidth = ActionBarMenuItem.this.searchFieldCaption.getMeasuredWidth();
                            measuredWidth += AndroidUtilities.dp(4.0f);
                        }
                    }
                    ActionBarMenuItem.this.searchField.layout(measuredWidth, ActionBarMenuItem.this.searchField.getTop(), ActionBarMenuItem.this.searchField.getMeasuredWidth() + measuredWidth, ActionBarMenuItem.this.searchField.getBottom());
                }
                
                protected void onMeasure(final int n, final int n2) {
                    this.measureChildWithMargins((View)ActionBarMenuItem.this.clearButton, n, 0, n2, 0);
                    int n3;
                    if (ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0) {
                        this.measureChildWithMargins((View)ActionBarMenuItem.this.searchFieldCaption, n, View$MeasureSpec.getSize(n) / 2, n2, 0);
                        n3 = ActionBarMenuItem.this.searchFieldCaption.getMeasuredWidth() + AndroidUtilities.dp(4.0f);
                    }
                    else {
                        n3 = 0;
                    }
                    this.measureChildWithMargins((View)ActionBarMenuItem.this.searchField, n, n3, n2, 0);
                    View$MeasureSpec.getSize(n);
                    View$MeasureSpec.getSize(n2);
                    this.setMeasuredDimension(View$MeasureSpec.getSize(n), View$MeasureSpec.getSize(n2));
                }
            };
            this.parentMenu.addView((View)this.searchContainer, 0, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, 1.0f, 6, 0, 0, 0));
            this.searchContainer.setVisibility(8);
            (this.searchFieldCaption = new TextView(this.getContext())).setTextSize(1, 18.0f);
            this.searchFieldCaption.setTextColor(Theme.getColor("actionBarDefaultSearch"));
            this.searchFieldCaption.setSingleLine(true);
            this.searchFieldCaption.setEllipsize(TextUtils$TruncateAt.END);
            this.searchFieldCaption.setVisibility(8);
            final TextView searchFieldCaption = this.searchFieldCaption;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            searchFieldCaption.setGravity(gravity);
            (this.searchField = new EditTextBoldCursor(this.getContext()) {
                public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
                    if (n == 67 && ActionBarMenuItem.this.searchField.length() == 0 && ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0 && ActionBarMenuItem.this.searchFieldCaption.length() > 0) {
                        ActionBarMenuItem.this.clearButton.callOnClick();
                        return true;
                    }
                    return super.onKeyDown(n, keyEvent);
                }
                
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard((View)this)) {
                        this.clearFocus();
                        this.requestFocus();
                    }
                    return super.onTouchEvent(motionEvent);
                }
            }).setCursorWidth(1.5f);
            this.searchField.setCursorColor(Theme.getColor("actionBarDefaultSearch"));
            this.searchField.setTextSize(1, 18.0f);
            this.searchField.setHintTextColor(Theme.getColor("actionBarDefaultSearchPlaceholder"));
            this.searchField.setTextColor(Theme.getColor("actionBarDefaultSearch"));
            this.searchField.setSingleLine(true);
            this.searchField.setBackgroundResource(0);
            this.searchField.setPadding(0, 0, 0, 0);
            this.searchField.setInputType(this.searchField.getInputType() | 0x80000);
            if (Build$VERSION.SDK_INT < 23) {
                this.searchField.setCustomSelectionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
                    public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                        return false;
                    }
                    
                    public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                        return false;
                    }
                    
                    public void onDestroyActionMode(final ActionMode actionMode) {
                    }
                    
                    public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                        return false;
                    }
                });
            }
            this.searchField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ActionBarMenuItem$DSACM5xoXBBb_9TAnJG5eS_F3HQ(this));
            this.searchField.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    if (ActionBarMenuItem.this.ignoreOnTextChange) {
                        ActionBarMenuItem.this.ignoreOnTextChange = false;
                        return;
                    }
                    if (ActionBarMenuItem.this.listener != null) {
                        ActionBarMenuItem.this.listener.onTextChanged(ActionBarMenuItem.this.searchField);
                    }
                    if (ActionBarMenuItem.this.clearButton != null) {
                        if (TextUtils.isEmpty(charSequence) && (ActionBarMenuItem.this.listener == null || !ActionBarMenuItem.this.listener.forceShowClear()) && (ActionBarMenuItem.this.searchFieldCaption == null || ActionBarMenuItem.this.searchFieldCaption.getVisibility() != 0)) {
                            if (ActionBarMenuItem.this.clearButton.getTag() != null) {
                                ActionBarMenuItem.this.clearButton.setTag((Object)null);
                                ActionBarMenuItem.this.clearButton.clearAnimation();
                                if (ActionBarMenuItem.this.animateClear) {
                                    ActionBarMenuItem.this.clearButton.animate().setInterpolator((TimeInterpolator)new DecelerateInterpolator()).alpha(0.0f).setDuration(180L).scaleY(0.0f).scaleX(0.0f).rotation(45.0f).withEndAction((Runnable)new _$$Lambda$ActionBarMenuItem$4$WTfAvwLi4l2rOq09wt3w9zvbBZY(this)).start();
                                }
                                else {
                                    ActionBarMenuItem.this.clearButton.setAlpha(0.0f);
                                    ActionBarMenuItem.this.clearButton.setRotation(45.0f);
                                    ActionBarMenuItem.this.clearButton.setScaleX(0.0f);
                                    ActionBarMenuItem.this.clearButton.setScaleY(0.0f);
                                    ActionBarMenuItem.this.clearButton.setVisibility(4);
                                    ActionBarMenuItem.this.animateClear = true;
                                }
                            }
                        }
                        else if (ActionBarMenuItem.this.clearButton.getTag() == null) {
                            ActionBarMenuItem.this.clearButton.setTag((Object)1);
                            ActionBarMenuItem.this.clearButton.clearAnimation();
                            ActionBarMenuItem.this.clearButton.setVisibility(0);
                            if (ActionBarMenuItem.this.animateClear) {
                                ActionBarMenuItem.this.clearButton.animate().setInterpolator((TimeInterpolator)new DecelerateInterpolator()).alpha(1.0f).setDuration(180L).scaleY(1.0f).scaleX(1.0f).rotation(0.0f).start();
                            }
                            else {
                                ActionBarMenuItem.this.clearButton.setAlpha(1.0f);
                                ActionBarMenuItem.this.clearButton.setRotation(0.0f);
                                ActionBarMenuItem.this.clearButton.setScaleX(1.0f);
                                ActionBarMenuItem.this.clearButton.setScaleY(1.0f);
                                ActionBarMenuItem.this.animateClear = true;
                            }
                        }
                    }
                }
            });
            this.searchField.setImeOptions(33554435);
            this.searchField.setTextIsSelectable(false);
            if (!LocaleController.isRTL) {
                this.searchContainer.addView((View)this.searchFieldCaption, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 36.0f, 19, 0.0f, 5.5f, 0.0f, 0.0f));
                this.searchContainer.addView((View)this.searchField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 16, 0.0f, 0.0f, 48.0f, 0.0f));
            }
            else {
                this.searchContainer.addView((View)this.searchField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 16, 0.0f, 0.0f, 48.0f, 0.0f));
                this.searchContainer.addView((View)this.searchFieldCaption, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 36.0f, 21, 0.0f, 5.5f, 48.0f, 0.0f));
            }
            (this.clearButton = new ImageView(this.getContext()) {
                protected void onDetachedFromWindow() {
                    super.onDetachedFromWindow();
                    this.clearAnimation();
                    if (this.getTag() == null) {
                        ActionBarMenuItem.this.clearButton.setVisibility(4);
                        ActionBarMenuItem.this.clearButton.setAlpha(0.0f);
                        ActionBarMenuItem.this.clearButton.setRotation(45.0f);
                        ActionBarMenuItem.this.clearButton.setScaleX(0.0f);
                        ActionBarMenuItem.this.clearButton.setScaleY(0.0f);
                    }
                    else {
                        ActionBarMenuItem.this.clearButton.setAlpha(1.0f);
                        ActionBarMenuItem.this.clearButton.setRotation(0.0f);
                        ActionBarMenuItem.this.clearButton.setScaleX(1.0f);
                        ActionBarMenuItem.this.clearButton.setScaleY(1.0f);
                    }
                }
            }).setImageDrawable((Drawable)(this.progressDrawable = new CloseProgressDrawable2()));
            this.clearButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(this.parentMenu.parentActionBar.itemsColor, PorterDuff$Mode.MULTIPLY));
            this.clearButton.setScaleType(ImageView$ScaleType.CENTER);
            this.clearButton.setAlpha(0.0f);
            this.clearButton.setRotation(45.0f);
            this.clearButton.setScaleX(0.0f);
            this.clearButton.setScaleY(0.0f);
            this.clearButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ActionBarMenuItem$_MHVU_Pdp5nAX3_6TiPCB165nO8(this));
            this.clearButton.setContentDescription((CharSequence)LocaleController.getString("ClearButton", 2131559102));
            this.searchContainer.addView((View)this.clearButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, -1, 21));
        }
        this.isSearchField = isSearchField;
        return this;
    }
    
    public void setLayoutInScreen(final boolean layoutInScreen) {
        this.layoutInScreen = layoutInScreen;
    }
    
    public void setLongClickEnabled(final boolean longClickEnabled) {
        this.longClickEnabled = longClickEnabled;
    }
    
    public void setMenuYOffset(final int yOffset) {
        this.yOffset = yOffset;
    }
    
    public ActionBarMenuItem setOverrideMenuClick(final boolean overrideMenuClick) {
        this.overrideMenuClick = overrideMenuClick;
        return this;
    }
    
    public void setPopupAnimationEnabled(final boolean b) {
        final ActionBarPopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null) {
            popupWindow.setAnimationEnabled(b);
        }
        this.animationEnabled = b;
    }
    
    public void setPopupItemsColor(final int textColor, final boolean b) {
        final ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout = this.popupLayout;
        if (popupLayout == null) {
            return;
        }
        for (int childCount = popupLayout.linearLayout.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.popupLayout.linearLayout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView)child).setTextColor(textColor);
            }
            else if (child instanceof ActionBarMenuSubItem) {
                if (b) {
                    ((ActionBarMenuSubItem)child).setIconColor(textColor);
                }
                else {
                    ((ActionBarMenuSubItem)child).setTextColor(textColor);
                }
            }
        }
    }
    
    public void setSearchFieldCaption(final CharSequence text) {
        if (this.searchFieldCaption == null) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            this.searchFieldCaption.setVisibility(8);
        }
        else {
            this.searchFieldCaption.setVisibility(0);
            this.searchFieldCaption.setText(text);
        }
    }
    
    public void setSearchFieldHint(final CharSequence charSequence) {
        if (this.searchFieldCaption == null) {
            return;
        }
        this.searchField.setHint(charSequence);
        this.setContentDescription(charSequence);
    }
    
    public void setSearchFieldText(final CharSequence text, final boolean animateClear) {
        if (this.searchFieldCaption == null) {
            return;
        }
        this.animateClear = animateClear;
        this.searchField.setText(text);
        if (!TextUtils.isEmpty(text)) {
            this.searchField.setSelection(text.length());
        }
    }
    
    public void setShowSearchProgress(final boolean b) {
        final CloseProgressDrawable2 progressDrawable = this.progressDrawable;
        if (progressDrawable == null) {
            return;
        }
        if (b) {
            progressDrawable.startAnimation();
        }
        else {
            progressDrawable.stopAnimation();
        }
    }
    
    public void setSubMenuOpenSide(final int subMenuOpenSide) {
        this.subMenuOpenSide = subMenuOpenSide;
    }
    
    public void showSubItem(final int i) {
        final ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout = this.popupLayout;
        if (popupLayout == null) {
            return;
        }
        final View viewWithTag = popupLayout.findViewWithTag((Object)i);
        if (viewWithTag != null && viewWithTag.getVisibility() != 0) {
            viewWithTag.setVisibility(0);
        }
    }
    
    public boolean toggleSearch(final boolean b) {
        final FrameLayout searchContainer = this.searchContainer;
        if (searchContainer == null) {
            return false;
        }
        if (searchContainer.getVisibility() == 0) {
            final ActionBarMenuItemSearchListener listener = this.listener;
            if (listener == null || (listener != null && listener.canCollapseSearch())) {
                if (b) {
                    AndroidUtilities.hideKeyboard((View)this.searchField);
                }
                this.searchField.setText((CharSequence)"");
                this.searchContainer.setVisibility(8);
                this.searchField.clearFocus();
                this.setVisibility(0);
                final ActionBarMenuItemSearchListener listener2 = this.listener;
                if (listener2 != null) {
                    listener2.onSearchCollapse();
                }
            }
            return false;
        }
        this.searchContainer.setVisibility(0);
        this.setVisibility(8);
        this.searchField.setText((CharSequence)"");
        this.searchField.requestFocus();
        if (b) {
            AndroidUtilities.showKeyboard((View)this.searchField);
        }
        final ActionBarMenuItemSearchListener listener3 = this.listener;
        if (listener3 != null) {
            listener3.onSearchExpand();
        }
        return true;
    }
    
    public void toggleSubMenu() {
        if (this.popupLayout != null) {
            final ActionBarMenu parentMenu = this.parentMenu;
            if (parentMenu != null && parentMenu.isActionMode) {
                final ActionBar parentActionBar = parentMenu.parentActionBar;
                if (parentActionBar != null && !parentActionBar.isActionModeShowed()) {
                    return;
                }
            }
            final Runnable showMenuRunnable = this.showMenuRunnable;
            if (showMenuRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(showMenuRunnable);
                this.showMenuRunnable = null;
            }
            final ActionBarPopupWindow popupWindow = this.popupWindow;
            if (popupWindow != null && popupWindow.isShowing()) {
                this.popupWindow.dismiss();
                return;
            }
            if (this.popupWindow == null) {
                this.popupWindow = new ActionBarPopupWindow((View)this.popupLayout, -2, -2);
                if (this.animationEnabled && Build$VERSION.SDK_INT >= 19) {
                    this.popupWindow.setAnimationStyle(0);
                }
                else {
                    this.popupWindow.setAnimationStyle(2131624110);
                }
                final boolean animationEnabled = this.animationEnabled;
                if (!animationEnabled) {
                    this.popupWindow.setAnimationEnabled(animationEnabled);
                }
                this.popupWindow.setOutsideTouchable(true);
                this.popupWindow.setClippingEnabled(true);
                if (this.layoutInScreen) {
                    this.popupWindow.setLayoutInScreen(true);
                }
                this.popupWindow.setInputMethodMode(2);
                this.popupWindow.setSoftInputMode(0);
                this.popupLayout.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                this.popupWindow.getContentView().setFocusableInTouchMode(true);
                this.popupWindow.getContentView().setOnKeyListener((View$OnKeyListener)new _$$Lambda$ActionBarMenuItem$9YLDoQyZnlPz968V4zO2iegYPb0(this));
            }
            this.processedPopupClick = false;
            this.popupWindow.setFocusable(true);
            if (this.popupLayout.getMeasuredWidth() == 0) {
                this.updateOrShowPopup(true, true);
            }
            else {
                this.updateOrShowPopup(true, false);
            }
            this.popupWindow.startAnimation();
        }
    }
    
    public interface ActionBarMenuItemDelegate
    {
        void onItemClick(final int p0);
    }
    
    public static class ActionBarMenuItemSearchListener
    {
        public boolean canCollapseSearch() {
            return true;
        }
        
        public boolean forceShowClear() {
            return false;
        }
        
        public void onCaptionCleared() {
        }
        
        public void onSearchCollapse() {
        }
        
        public void onSearchExpand() {
        }
        
        public void onSearchPressed(final EditText editText) {
        }
        
        public void onTextChanged(final EditText editText) {
        }
    }
}
