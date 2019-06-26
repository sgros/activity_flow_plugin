package org.telegram.ui.ActionBar;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.CloseProgressDrawable2;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class ActionBarMenuItem extends FrameLayout {
   private int additionalOffset;
   private boolean allowCloseAnimation = true;
   private boolean animateClear = true;
   private boolean animationEnabled = true;
   private ImageView clearButton;
   private ActionBarMenuItem.ActionBarMenuItemDelegate delegate;
   protected ImageView iconView;
   private boolean ignoreOnTextChange;
   private boolean isSearchField;
   private boolean layoutInScreen;
   private ActionBarMenuItem.ActionBarMenuItemSearchListener listener;
   private int[] location;
   private boolean longClickEnabled = true;
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

   public ActionBarMenuItem(Context var1, ActionBarMenu var2, int var3, int var4) {
      super(var1);
      if (var3 != 0) {
         this.setBackgroundDrawable(Theme.createSelectorDrawable(var3));
      }

      this.parentMenu = var2;
      this.iconView = new ImageView(var1);
      this.iconView.setScaleType(ScaleType.CENTER);
      this.addView(this.iconView, LayoutHelper.createFrame(-1, -1.0F));
      if (var4 != 0) {
         this.iconView.setColorFilter(new PorterDuffColorFilter(var4, Mode.MULTIPLY));
      }

   }

   private void createPopupLayout() {
      if (this.popupLayout == null) {
         this.rect = new Rect();
         this.location = new int[2];
         this.popupLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.getContext());
         this.popupLayout.setOnTouchListener(new _$$Lambda$ActionBarMenuItem$NEgOkBBuNcW0duabIaP5FYMgH5w(this));
         this.popupLayout.setDispatchKeyEventListener(new _$$Lambda$ActionBarMenuItem$yNUlqFN00bPmohrcR1AdkblquoQ(this));
      }
   }

   private void updateOrShowPopup(boolean var1, boolean var2) {
      ActionBarMenu var3 = this.parentMenu;
      int var4;
      int var5;
      if (var3 != null) {
         var4 = -var3.parentActionBar.getMeasuredHeight() + this.parentMenu.getTop();
         var5 = this.parentMenu.getPaddingTop();
      } else {
         float var6 = this.getScaleY();
         float var7 = (float)this.getMeasuredHeight();
         float var8;
         if (this.subMenuOpenSide != 2) {
            var8 = this.getTranslationY();
         } else {
            var8 = 0.0F;
         }

         var4 = -((int)(var7 * var6 - var8 / var6));
         var5 = this.additionalOffset;
      }

      var5 = var4 + var5 + this.yOffset;
      if (var1) {
         this.popupLayout.scrollToTop();
      }

      var3 = this.parentMenu;
      if (var3 != null) {
         ActionBar var9 = var3.parentActionBar;
         if (this.subMenuOpenSide == 0) {
            if (var1) {
               this.popupWindow.showAsDropDown(var9, this.getLeft() + this.parentMenu.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth() + (int)this.getTranslationX(), var5);
            }

            if (var2) {
               this.popupWindow.update(var9, this.getLeft() + this.parentMenu.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth() + (int)this.getTranslationX(), var5, -1, -1);
            }
         } else {
            if (var1) {
               this.popupWindow.showAsDropDown(var9, this.getLeft() - AndroidUtilities.dp(8.0F) + (int)this.getTranslationX(), var5);
            }

            if (var2) {
               this.popupWindow.update(var9, this.getLeft() - AndroidUtilities.dp(8.0F) + (int)this.getTranslationX(), var5, -1, -1);
            }
         }
      } else {
         var4 = this.subMenuOpenSide;
         if (var4 == 0) {
            if (this.getParent() != null) {
               View var10 = (View)this.getParent();
               if (var1) {
                  this.popupWindow.showAsDropDown(var10, this.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), var5);
               }

               if (var2) {
                  this.popupWindow.update(var10, this.getLeft() + this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), var5, -1, -1);
               }
            }
         } else if (var4 == 1) {
            if (var1) {
               this.popupWindow.showAsDropDown(this, -AndroidUtilities.dp(8.0F), var5);
            }

            if (var2) {
               this.popupWindow.update(this, -AndroidUtilities.dp(8.0F), var5, -1, -1);
            }
         } else {
            if (var1) {
               this.popupWindow.showAsDropDown(this, this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), var5);
            }

            if (var2) {
               this.popupWindow.update(this, this.getMeasuredWidth() - this.popupLayout.getMeasuredWidth(), var5, -1, -1);
            }
         }
      }

   }

   public TextView addSubItem(int var1, CharSequence var2) {
      this.createPopupLayout();
      TextView var3 = new TextView(this.getContext());
      var3.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
      var3.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      if (!LocaleController.isRTL) {
         var3.setGravity(16);
      } else {
         var3.setGravity(21);
      }

      var3.setPadding(AndroidUtilities.dp(16.0F), 0, AndroidUtilities.dp(16.0F), 0);
      var3.setTextSize(1, 16.0F);
      var3.setMinWidth(AndroidUtilities.dp(196.0F));
      var3.setTag(var1);
      var3.setText(var2);
      this.popupLayout.addView(var3);
      LayoutParams var4 = (LayoutParams)var3.getLayoutParams();
      if (LocaleController.isRTL) {
         var4.gravity = 5;
      }

      var4.width = -1;
      var4.height = AndroidUtilities.dp(48.0F);
      var3.setLayoutParams(var4);
      var3.setOnClickListener(new _$$Lambda$ActionBarMenuItem$2OXLLOK5tdU8k_iUBLX7uda6XhM(this));
      return var3;
   }

   public ActionBarMenuSubItem addSubItem(int var1, int var2, CharSequence var3) {
      this.createPopupLayout();
      ActionBarMenuSubItem var4 = new ActionBarMenuSubItem(this.getContext());
      var4.setTextAndIcon(var3, var2);
      var4.setMinimumWidth(AndroidUtilities.dp(196.0F));
      var4.setTag(var1);
      this.popupLayout.addView(var4);
      LayoutParams var5 = (LayoutParams)var4.getLayoutParams();
      if (LocaleController.isRTL) {
         var5.gravity = 5;
      }

      var5.width = -1;
      var5.height = AndroidUtilities.dp(48.0F);
      var4.setLayoutParams(var5);
      var4.setOnClickListener(new _$$Lambda$ActionBarMenuItem$37smKkAzmohk63TzSJQopQof02I(this));
      return var4;
   }

   public void addSubItem(int var1, View var2, int var3, int var4) {
      this.createPopupLayout();
      var2.setLayoutParams(new LayoutParams(var3, var4));
      this.popupLayout.addView(var2);
      var2.setTag(var1);
      var2.setOnClickListener(new _$$Lambda$ActionBarMenuItem$9Z_bd6EyKyDSvICVkiunmSXpJnY(this));
      var2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
   }

   public void addSubItem(View var1, int var2, int var3) {
      this.createPopupLayout();
      this.popupLayout.addView(var1, new LayoutParams(var2, var3));
   }

   public void clearSearchText() {
      EditTextBoldCursor var1 = this.searchField;
      if (var1 != null) {
         var1.setText("");
      }
   }

   public void closeSubMenu() {
      ActionBarPopupWindow var1 = this.popupWindow;
      if (var1 != null && var1.isShowing()) {
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
      boolean var1;
      if (this.popupLayout != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void hideSubItem(int var1) {
      ActionBarPopupWindow.ActionBarPopupWindowLayout var2 = this.popupLayout;
      if (var2 != null) {
         View var3 = var2.findViewWithTag(var1);
         if (var3 != null && var3.getVisibility() != 8) {
            var3.setVisibility(8);
         }

      }
   }

   public boolean isSearchField() {
      return this.isSearchField;
   }

   public boolean isSubItemVisible(int var1) {
      ActionBarPopupWindow.ActionBarPopupWindowLayout var2 = this.popupLayout;
      boolean var3 = false;
      if (var2 == null) {
         return false;
      } else {
         View var5 = var2.findViewWithTag(var1);
         boolean var4 = var3;
         if (var5 != null) {
            var4 = var3;
            if (var5.getVisibility() == 0) {
               var4 = true;
            }
         }

         return var4;
      }
   }

   // $FF: synthetic method
   public void lambda$addSubItem$3$ActionBarMenuItem(View var1) {
      ActionBarPopupWindow var2 = this.popupWindow;
      if (var2 != null && var2.isShowing()) {
         if (this.processedPopupClick) {
            return;
         }

         this.processedPopupClick = true;
         this.popupWindow.dismiss(this.allowCloseAnimation);
      }

      ActionBarMenu var3 = this.parentMenu;
      if (var3 != null) {
         var3.onItemClick((Integer)var1.getTag());
      } else {
         ActionBarMenuItem.ActionBarMenuItemDelegate var4 = this.delegate;
         if (var4 != null) {
            var4.onItemClick((Integer)var1.getTag());
         }
      }

   }

   // $FF: synthetic method
   public void lambda$addSubItem$4$ActionBarMenuItem(View var1) {
      ActionBarPopupWindow var2 = this.popupWindow;
      if (var2 != null && var2.isShowing()) {
         if (this.processedPopupClick) {
            return;
         }

         this.processedPopupClick = true;
         this.popupWindow.dismiss(this.allowCloseAnimation);
      }

      ActionBarMenu var3 = this.parentMenu;
      if (var3 != null) {
         var3.onItemClick((Integer)var1.getTag());
      } else {
         ActionBarMenuItem.ActionBarMenuItemDelegate var4 = this.delegate;
         if (var4 != null) {
            var4.onItemClick((Integer)var1.getTag());
         }
      }

   }

   // $FF: synthetic method
   public void lambda$addSubItem$5$ActionBarMenuItem(View var1) {
      ActionBarPopupWindow var2 = this.popupWindow;
      if (var2 != null && var2.isShowing()) {
         if (this.processedPopupClick) {
            return;
         }

         this.processedPopupClick = true;
         this.popupWindow.dismiss(this.allowCloseAnimation);
      }

      ActionBarMenu var3 = this.parentMenu;
      if (var3 != null) {
         var3.onItemClick((Integer)var1.getTag());
      } else {
         ActionBarMenuItem.ActionBarMenuItemDelegate var4 = this.delegate;
         if (var4 != null) {
            var4.onItemClick((Integer)var1.getTag());
         }
      }

   }

   // $FF: synthetic method
   public boolean lambda$createPopupLayout$1$ActionBarMenuItem(View var1, MotionEvent var2) {
      if (var2.getActionMasked() == 0) {
         ActionBarPopupWindow var3 = this.popupWindow;
         if (var3 != null && var3.isShowing()) {
            var1.getHitRect(this.rect);
            if (!this.rect.contains((int)var2.getX(), (int)var2.getY())) {
               this.popupWindow.dismiss();
            }
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$createPopupLayout$2$ActionBarMenuItem(KeyEvent var1) {
      if (var1.getKeyCode() == 4 && var1.getRepeatCount() == 0) {
         ActionBarPopupWindow var2 = this.popupWindow;
         if (var2 != null && var2.isShowing()) {
            this.popupWindow.dismiss();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$onTouchEvent$0$ActionBarMenuItem() {
      if (this.getParent() != null) {
         this.getParent().requestDisallowInterceptTouchEvent(true);
      }

      this.toggleSubMenu();
   }

   // $FF: synthetic method
   public boolean lambda$setIsSearchField$7$ActionBarMenuItem(TextView var1, int var2, KeyEvent var3) {
      if (var3 != null && (var3.getAction() == 1 && var3.getKeyCode() == 84 || var3.getAction() == 0 && var3.getKeyCode() == 66)) {
         AndroidUtilities.hideKeyboard(this.searchField);
         ActionBarMenuItem.ActionBarMenuItemSearchListener var4 = this.listener;
         if (var4 != null) {
            var4.onSearchPressed(this.searchField);
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$setIsSearchField$8$ActionBarMenuItem(View var1) {
      if (this.searchField.length() != 0) {
         this.searchField.setText("");
      } else {
         TextView var2 = this.searchFieldCaption;
         if (var2 != null && var2.getVisibility() == 0) {
            this.searchFieldCaption.setVisibility(8);
            ActionBarMenuItem.ActionBarMenuItemSearchListener var3 = this.listener;
            if (var3 != null) {
               var3.onCaptionCleared();
            }
         }
      }

      this.searchField.requestFocus();
      AndroidUtilities.showKeyboard(this.searchField);
   }

   // $FF: synthetic method
   public boolean lambda$toggleSubMenu$6$ActionBarMenuItem(View var1, int var2, KeyEvent var3) {
      if (var2 == 82 && var3.getRepeatCount() == 0 && var3.getAction() == 1) {
         ActionBarPopupWindow var4 = this.popupWindow;
         if (var4 != null && var4.isShowing()) {
            this.popupWindow.dismiss();
            return true;
         }
      }

      return false;
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.ImageButton");
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      ActionBarPopupWindow var6 = this.popupWindow;
      if (var6 != null && var6.isShowing()) {
         this.updateOrShowPopup(false, true);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      ActionBarPopupWindow var2;
      if (var1.getActionMasked() == 0) {
         if (this.longClickEnabled && this.hasSubMenu()) {
            var2 = this.popupWindow;
            if (var2 == null || var2 != null && !var2.isShowing()) {
               this.showMenuRunnable = new _$$Lambda$ActionBarMenuItem$Y4Ro71_Kozj7zLr98hi2EHrr4_A(this);
               AndroidUtilities.runOnUIThread(this.showMenuRunnable, 200L);
            }
         }
      } else {
         View var10;
         if (var1.getActionMasked() == 2) {
            if (this.hasSubMenu()) {
               var2 = this.popupWindow;
               if (var2 == null || var2 != null && !var2.isShowing()) {
                  if (var1.getY() > (float)this.getHeight()) {
                     if (this.getParent() != null) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                     }

                     this.toggleSubMenu();
                     return true;
                  }

                  return super.onTouchEvent(var1);
               }
            }

            var2 = this.popupWindow;
            if (var2 != null && var2.isShowing()) {
               this.getLocationOnScreen(this.location);
               float var3 = var1.getX();
               float var4 = (float)this.location[0];
               float var5 = var1.getY();
               int[] var9 = this.location;
               float var6 = (float)var9[1];
               this.popupLayout.getLocationOnScreen(var9);
               var9 = this.location;
               var3 = var3 + var4 - (float)var9[0];
               var5 = var5 + var6 - (float)var9[1];
               this.selectedMenuView = null;

               for(int var7 = 0; var7 < this.popupLayout.getItemsCount(); ++var7) {
                  var10 = this.popupLayout.getItemAt(var7);
                  var10.getHitRect(this.rect);
                  if ((Integer)var10.getTag() < 100) {
                     if (!this.rect.contains((int)var3, (int)var5)) {
                        var10.setPressed(false);
                        var10.setSelected(false);
                        if (VERSION.SDK_INT == 21) {
                           var10.getBackground().setVisible(false, false);
                        }
                     } else {
                        var10.setPressed(true);
                        var10.setSelected(true);
                        int var8 = VERSION.SDK_INT;
                        if (var8 >= 21) {
                           if (var8 == 21) {
                              var10.getBackground().setVisible(true, false);
                           }

                           var10.drawableHotspotChanged(var3, var5 - (float)var10.getTop());
                        }

                        this.selectedMenuView = var10;
                     }
                  }
               }
            }
         } else {
            var2 = this.popupWindow;
            if (var2 != null && var2.isShowing() && var1.getActionMasked() == 1) {
               var10 = this.selectedMenuView;
               if (var10 != null) {
                  var10.setSelected(false);
                  ActionBarMenu var11 = this.parentMenu;
                  if (var11 != null) {
                     var11.onItemClick((Integer)this.selectedMenuView.getTag());
                  } else {
                     ActionBarMenuItem.ActionBarMenuItemDelegate var12 = this.delegate;
                     if (var12 != null) {
                        var12.onItemClick((Integer)this.selectedMenuView.getTag());
                     }
                  }

                  this.popupWindow.dismiss(this.allowCloseAnimation);
               } else {
                  this.popupWindow.dismiss();
               }
            } else {
               var10 = this.selectedMenuView;
               if (var10 != null) {
                  var10.setSelected(false);
                  this.selectedMenuView = null;
               }
            }
         }
      }

      return super.onTouchEvent(var1);
   }

   public void openSearch(boolean var1) {
      FrameLayout var2 = this.searchContainer;
      if (var2 != null && var2.getVisibility() != 0) {
         ActionBarMenu var3 = this.parentMenu;
         if (var3 != null) {
            var3.parentActionBar.onSearchFieldVisibilityChanged(this.toggleSearch(var1));
         }
      }

   }

   public void redrawPopup(int var1) {
      ActionBarPopupWindow.ActionBarPopupWindowLayout var2 = this.popupLayout;
      if (var2 != null) {
         var2.backgroundDrawable.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
         this.popupLayout.invalidate();
      }

   }

   public ActionBarMenuItem setActionBarMenuItemSearchListener(ActionBarMenuItem.ActionBarMenuItemSearchListener var1) {
      this.listener = var1;
      return this;
   }

   public void setAdditionalOffset(int var1) {
      this.additionalOffset = var1;
   }

   public ActionBarMenuItem setAllowCloseAnimation(boolean var1) {
      this.allowCloseAnimation = var1;
      return this;
   }

   public void setDelegate(ActionBarMenuItem.ActionBarMenuItemDelegate var1) {
      this.delegate = var1;
   }

   public void setIcon(int var1) {
      this.iconView.setImageResource(var1);
   }

   public void setIcon(Drawable var1) {
      this.iconView.setImageDrawable(var1);
   }

   public void setIconColor(int var1) {
      this.iconView.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
      ImageView var2 = this.clearButton;
      if (var2 != null) {
         var2.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
      }

   }

   public void setIgnoreOnTextChange() {
      this.ignoreOnTextChange = true;
   }

   public ActionBarMenuItem setIsSearchField(boolean var1) {
      if (this.parentMenu == null) {
         return this;
      } else {
         if (var1 && this.searchContainer == null) {
            this.searchContainer = new FrameLayout(this.getContext()) {
               protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
                  super.onLayout(var1, var2, var3, var4, var5);
                  var1 = LocaleController.isRTL;
                  var2 = 0;
                  if (!var1 && ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0) {
                     var2 = ActionBarMenuItem.this.searchFieldCaption.getMeasuredWidth();
                     var2 += AndroidUtilities.dp(4.0F);
                  }

                  ActionBarMenuItem.this.searchField.layout(var2, ActionBarMenuItem.this.searchField.getTop(), ActionBarMenuItem.this.searchField.getMeasuredWidth() + var2, ActionBarMenuItem.this.searchField.getBottom());
               }

               protected void onMeasure(int var1, int var2) {
                  this.measureChildWithMargins(ActionBarMenuItem.this.clearButton, var1, 0, var2, 0);
                  int var3;
                  if (ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0) {
                     this.measureChildWithMargins(ActionBarMenuItem.this.searchFieldCaption, var1, MeasureSpec.getSize(var1) / 2, var2, 0);
                     var3 = ActionBarMenuItem.this.searchFieldCaption.getMeasuredWidth() + AndroidUtilities.dp(4.0F);
                  } else {
                     var3 = 0;
                  }

                  this.measureChildWithMargins(ActionBarMenuItem.this.searchField, var1, var3, var2, 0);
                  MeasureSpec.getSize(var1);
                  MeasureSpec.getSize(var2);
                  this.setMeasuredDimension(MeasureSpec.getSize(var1), MeasureSpec.getSize(var2));
               }
            };
            this.parentMenu.addView(this.searchContainer, 0, LayoutHelper.createLinear(0, -1, 1.0F, 6, 0, 0, 0));
            this.searchContainer.setVisibility(8);
            this.searchFieldCaption = new TextView(this.getContext());
            this.searchFieldCaption.setTextSize(1, 18.0F);
            this.searchFieldCaption.setTextColor(Theme.getColor("actionBarDefaultSearch"));
            this.searchFieldCaption.setSingleLine(true);
            this.searchFieldCaption.setEllipsize(TruncateAt.END);
            this.searchFieldCaption.setVisibility(8);
            TextView var2 = this.searchFieldCaption;
            byte var3;
            if (LocaleController.isRTL) {
               var3 = 5;
            } else {
               var3 = 3;
            }

            var2.setGravity(var3);
            this.searchField = new EditTextBoldCursor(this.getContext()) {
               public boolean onKeyDown(int var1, KeyEvent var2) {
                  if (var1 == 67 && ActionBarMenuItem.this.searchField.length() == 0 && ActionBarMenuItem.this.searchFieldCaption.getVisibility() == 0 && ActionBarMenuItem.this.searchFieldCaption.length() > 0) {
                     ActionBarMenuItem.this.clearButton.callOnClick();
                     return true;
                  } else {
                     return super.onKeyDown(var1, var2);
                  }
               }

               public boolean onTouchEvent(MotionEvent var1) {
                  if (var1.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
                     this.clearFocus();
                     this.requestFocus();
                  }

                  return super.onTouchEvent(var1);
               }
            };
            this.searchField.setCursorWidth(1.5F);
            this.searchField.setCursorColor(Theme.getColor("actionBarDefaultSearch"));
            this.searchField.setTextSize(1, 18.0F);
            this.searchField.setHintTextColor(Theme.getColor("actionBarDefaultSearchPlaceholder"));
            this.searchField.setTextColor(Theme.getColor("actionBarDefaultSearch"));
            this.searchField.setSingleLine(true);
            this.searchField.setBackgroundResource(0);
            this.searchField.setPadding(0, 0, 0, 0);
            int var6 = this.searchField.getInputType();
            this.searchField.setInputType(var6 | 524288);
            if (VERSION.SDK_INT < 23) {
               this.searchField.setCustomSelectionActionModeCallback(new Callback() {
                  public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
                     return false;
                  }

                  public boolean onCreateActionMode(ActionMode var1, Menu var2) {
                     return false;
                  }

                  public void onDestroyActionMode(ActionMode var1) {
                  }

                  public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
                     return false;
                  }
               });
            }

            this.searchField.setOnEditorActionListener(new _$$Lambda$ActionBarMenuItem$DSACM5xoXBBb_9TAnJG5eS_F3HQ(this));
            this.searchField.addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               // $FF: synthetic method
               public void lambda$onTextChanged$0$ActionBarMenuItem$4() {
                  ActionBarMenuItem.this.clearButton.setVisibility(4);
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                  if (ActionBarMenuItem.this.ignoreOnTextChange) {
                     ActionBarMenuItem.this.ignoreOnTextChange = false;
                  } else {
                     if (ActionBarMenuItem.this.listener != null) {
                        ActionBarMenuItem.this.listener.onTextChanged(ActionBarMenuItem.this.searchField);
                     }

                     if (ActionBarMenuItem.this.clearButton != null) {
                        if (TextUtils.isEmpty(var1) && (ActionBarMenuItem.this.listener == null || !ActionBarMenuItem.this.listener.forceShowClear()) && (ActionBarMenuItem.this.searchFieldCaption == null || ActionBarMenuItem.this.searchFieldCaption.getVisibility() != 0)) {
                           if (ActionBarMenuItem.this.clearButton.getTag() != null) {
                              ActionBarMenuItem.this.clearButton.setTag((Object)null);
                              ActionBarMenuItem.this.clearButton.clearAnimation();
                              if (ActionBarMenuItem.this.animateClear) {
                                 ActionBarMenuItem.this.clearButton.animate().setInterpolator(new DecelerateInterpolator()).alpha(0.0F).setDuration(180L).scaleY(0.0F).scaleX(0.0F).rotation(45.0F).withEndAction(new _$$Lambda$ActionBarMenuItem$4$WTfAvwLi4l2rOq09wt3w9zvbBZY(this)).start();
                              } else {
                                 ActionBarMenuItem.this.clearButton.setAlpha(0.0F);
                                 ActionBarMenuItem.this.clearButton.setRotation(45.0F);
                                 ActionBarMenuItem.this.clearButton.setScaleX(0.0F);
                                 ActionBarMenuItem.this.clearButton.setScaleY(0.0F);
                                 ActionBarMenuItem.this.clearButton.setVisibility(4);
                                 ActionBarMenuItem.this.animateClear = true;
                              }
                           }
                        } else if (ActionBarMenuItem.this.clearButton.getTag() == null) {
                           ActionBarMenuItem.this.clearButton.setTag(1);
                           ActionBarMenuItem.this.clearButton.clearAnimation();
                           ActionBarMenuItem.this.clearButton.setVisibility(0);
                           if (ActionBarMenuItem.this.animateClear) {
                              ActionBarMenuItem.this.clearButton.animate().setInterpolator(new DecelerateInterpolator()).alpha(1.0F).setDuration(180L).scaleY(1.0F).scaleX(1.0F).rotation(0.0F).start();
                           } else {
                              ActionBarMenuItem.this.clearButton.setAlpha(1.0F);
                              ActionBarMenuItem.this.clearButton.setRotation(0.0F);
                              ActionBarMenuItem.this.clearButton.setScaleX(1.0F);
                              ActionBarMenuItem.this.clearButton.setScaleY(1.0F);
                              ActionBarMenuItem.this.animateClear = true;
                           }
                        }
                     }

                  }
               }
            });
            this.searchField.setImeOptions(33554435);
            this.searchField.setTextIsSelectable(false);
            if (!LocaleController.isRTL) {
               this.searchContainer.addView(this.searchFieldCaption, LayoutHelper.createFrame(-2, 36.0F, 19, 0.0F, 5.5F, 0.0F, 0.0F));
               this.searchContainer.addView(this.searchField, LayoutHelper.createFrame(-1, 36.0F, 16, 0.0F, 0.0F, 48.0F, 0.0F));
            } else {
               this.searchContainer.addView(this.searchField, LayoutHelper.createFrame(-1, 36.0F, 16, 0.0F, 0.0F, 48.0F, 0.0F));
               this.searchContainer.addView(this.searchFieldCaption, LayoutHelper.createFrame(-2, 36.0F, 21, 0.0F, 5.5F, 48.0F, 0.0F));
            }

            this.clearButton = new ImageView(this.getContext()) {
               protected void onDetachedFromWindow() {
                  super.onDetachedFromWindow();
                  this.clearAnimation();
                  if (this.getTag() == null) {
                     ActionBarMenuItem.this.clearButton.setVisibility(4);
                     ActionBarMenuItem.this.clearButton.setAlpha(0.0F);
                     ActionBarMenuItem.this.clearButton.setRotation(45.0F);
                     ActionBarMenuItem.this.clearButton.setScaleX(0.0F);
                     ActionBarMenuItem.this.clearButton.setScaleY(0.0F);
                  } else {
                     ActionBarMenuItem.this.clearButton.setAlpha(1.0F);
                     ActionBarMenuItem.this.clearButton.setRotation(0.0F);
                     ActionBarMenuItem.this.clearButton.setScaleX(1.0F);
                     ActionBarMenuItem.this.clearButton.setScaleY(1.0F);
                  }

               }
            };
            ImageView var4 = this.clearButton;
            CloseProgressDrawable2 var5 = new CloseProgressDrawable2();
            this.progressDrawable = var5;
            var4.setImageDrawable(var5);
            this.clearButton.setColorFilter(new PorterDuffColorFilter(this.parentMenu.parentActionBar.itemsColor, Mode.MULTIPLY));
            this.clearButton.setScaleType(ScaleType.CENTER);
            this.clearButton.setAlpha(0.0F);
            this.clearButton.setRotation(45.0F);
            this.clearButton.setScaleX(0.0F);
            this.clearButton.setScaleY(0.0F);
            this.clearButton.setOnClickListener(new _$$Lambda$ActionBarMenuItem$_MHVU_Pdp5nAX3_6TiPCB165nO8(this));
            this.clearButton.setContentDescription(LocaleController.getString("ClearButton", 2131559102));
            this.searchContainer.addView(this.clearButton, LayoutHelper.createFrame(48, -1, 21));
         }

         this.isSearchField = var1;
         return this;
      }
   }

   public void setLayoutInScreen(boolean var1) {
      this.layoutInScreen = var1;
   }

   public void setLongClickEnabled(boolean var1) {
      this.longClickEnabled = var1;
   }

   public void setMenuYOffset(int var1) {
      this.yOffset = var1;
   }

   public ActionBarMenuItem setOverrideMenuClick(boolean var1) {
      this.overrideMenuClick = var1;
      return this;
   }

   public void setPopupAnimationEnabled(boolean var1) {
      ActionBarPopupWindow var2 = this.popupWindow;
      if (var2 != null) {
         var2.setAnimationEnabled(var1);
      }

      this.animationEnabled = var1;
   }

   public void setPopupItemsColor(int var1, boolean var2) {
      ActionBarPopupWindow.ActionBarPopupWindowLayout var3 = this.popupLayout;
      if (var3 != null) {
         int var4 = var3.linearLayout.getChildCount();

         for(int var5 = 0; var5 < var4; ++var5) {
            View var6 = this.popupLayout.linearLayout.getChildAt(var5);
            if (var6 instanceof TextView) {
               ((TextView)var6).setTextColor(var1);
            } else if (var6 instanceof ActionBarMenuSubItem) {
               if (var2) {
                  ((ActionBarMenuSubItem)var6).setIconColor(var1);
               } else {
                  ((ActionBarMenuSubItem)var6).setTextColor(var1);
               }
            }
         }

      }
   }

   public void setSearchFieldCaption(CharSequence var1) {
      if (this.searchFieldCaption != null) {
         if (TextUtils.isEmpty(var1)) {
            this.searchFieldCaption.setVisibility(8);
         } else {
            this.searchFieldCaption.setVisibility(0);
            this.searchFieldCaption.setText(var1);
         }

      }
   }

   public void setSearchFieldHint(CharSequence var1) {
      if (this.searchFieldCaption != null) {
         this.searchField.setHint(var1);
         this.setContentDescription(var1);
      }
   }

   public void setSearchFieldText(CharSequence var1, boolean var2) {
      if (this.searchFieldCaption != null) {
         this.animateClear = var2;
         this.searchField.setText(var1);
         if (!TextUtils.isEmpty(var1)) {
            this.searchField.setSelection(var1.length());
         }

      }
   }

   public void setShowSearchProgress(boolean var1) {
      CloseProgressDrawable2 var2 = this.progressDrawable;
      if (var2 != null) {
         if (var1) {
            var2.startAnimation();
         } else {
            var2.stopAnimation();
         }

      }
   }

   public void setSubMenuOpenSide(int var1) {
      this.subMenuOpenSide = var1;
   }

   public void showSubItem(int var1) {
      ActionBarPopupWindow.ActionBarPopupWindowLayout var2 = this.popupLayout;
      if (var2 != null) {
         View var3 = var2.findViewWithTag(var1);
         if (var3 != null && var3.getVisibility() != 0) {
            var3.setVisibility(0);
         }

      }
   }

   public boolean toggleSearch(boolean var1) {
      FrameLayout var2 = this.searchContainer;
      if (var2 == null) {
         return false;
      } else {
         ActionBarMenuItem.ActionBarMenuItemSearchListener var3;
         if (var2.getVisibility() != 0) {
            this.searchContainer.setVisibility(0);
            this.setVisibility(8);
            this.searchField.setText("");
            this.searchField.requestFocus();
            if (var1) {
               AndroidUtilities.showKeyboard(this.searchField);
            }

            var3 = this.listener;
            if (var3 != null) {
               var3.onSearchExpand();
            }

            return true;
         } else {
            var3 = this.listener;
            if (var3 == null || var3 != null && var3.canCollapseSearch()) {
               if (var1) {
                  AndroidUtilities.hideKeyboard(this.searchField);
               }

               this.searchField.setText("");
               this.searchContainer.setVisibility(8);
               this.searchField.clearFocus();
               this.setVisibility(0);
               var3 = this.listener;
               if (var3 != null) {
                  var3.onSearchCollapse();
               }
            }

            return false;
         }
      }
   }

   public void toggleSubMenu() {
      if (this.popupLayout != null) {
         ActionBarMenu var1 = this.parentMenu;
         if (var1 != null && var1.isActionMode) {
            ActionBar var3 = var1.parentActionBar;
            if (var3 != null && !var3.isActionModeShowed()) {
               return;
            }
         }

         Runnable var4 = this.showMenuRunnable;
         if (var4 != null) {
            AndroidUtilities.cancelRunOnUIThread(var4);
            this.showMenuRunnable = null;
         }

         ActionBarPopupWindow var5 = this.popupWindow;
         if (var5 != null && var5.isShowing()) {
            this.popupWindow.dismiss();
            return;
         }

         if (this.popupWindow == null) {
            this.popupWindow = new ActionBarPopupWindow(this.popupLayout, -2, -2);
            if (this.animationEnabled && VERSION.SDK_INT >= 19) {
               this.popupWindow.setAnimationStyle(0);
            } else {
               this.popupWindow.setAnimationStyle(2131624110);
            }

            boolean var2 = this.animationEnabled;
            if (!var2) {
               this.popupWindow.setAnimationEnabled(var2);
            }

            this.popupWindow.setOutsideTouchable(true);
            this.popupWindow.setClippingEnabled(true);
            if (this.layoutInScreen) {
               this.popupWindow.setLayoutInScreen(true);
            }

            this.popupWindow.setInputMethodMode(2);
            this.popupWindow.setSoftInputMode(0);
            this.popupLayout.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0F), Integer.MIN_VALUE));
            this.popupWindow.getContentView().setFocusableInTouchMode(true);
            this.popupWindow.getContentView().setOnKeyListener(new _$$Lambda$ActionBarMenuItem$9YLDoQyZnlPz968V4zO2iegYPb0(this));
         }

         this.processedPopupClick = false;
         this.popupWindow.setFocusable(true);
         if (this.popupLayout.getMeasuredWidth() == 0) {
            this.updateOrShowPopup(true, true);
         } else {
            this.updateOrShowPopup(true, false);
         }

         this.popupWindow.startAnimation();
      }

   }

   public interface ActionBarMenuItemDelegate {
      void onItemClick(int var1);
   }

   public static class ActionBarMenuItemSearchListener {
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

      public void onSearchPressed(EditText var1) {
      }

      public void onTextChanged(EditText var1) {
      }
   }
}
