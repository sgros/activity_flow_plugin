package androidx.appcompat.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.widget.DrawableUtils;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SupportMenuInflater extends MenuInflater {
   static final Class[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
   static final Class[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class};
   final Object[] mActionProviderConstructorArguments;
   final Object[] mActionViewConstructorArguments;
   Context mContext;
   private Object mRealOwner;

   static {
      ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
   }

   public SupportMenuInflater(Context var1) {
      super(var1);
      this.mContext = var1;
      this.mActionViewConstructorArguments = new Object[]{var1};
      this.mActionProviderConstructorArguments = this.mActionViewConstructorArguments;
   }

   private Object findRealOwner(Object var1) {
      if (var1 instanceof Activity) {
         return var1;
      } else {
         Object var2 = var1;
         if (var1 instanceof ContextWrapper) {
            var2 = this.findRealOwner(((ContextWrapper)var1).getBaseContext());
         }

         return var2;
      }
   }

   private void parseMenu(XmlPullParser var1, AttributeSet var2, Menu var3) throws XmlPullParserException, IOException {
      SupportMenuInflater.MenuState var4 = new SupportMenuInflater.MenuState(var3);
      int var5 = var1.getEventType();

      int var6;
      String var13;
      do {
         if (var5 == 2) {
            var13 = var1.getName();
            if (!var13.equals("menu")) {
               StringBuilder var12 = new StringBuilder();
               var12.append("Expecting menu, got ");
               var12.append(var13);
               throw new RuntimeException(var12.toString());
            }

            var6 = var1.next();
            break;
         }

         var6 = var1.next();
         var5 = var6;
      } while(var6 != 1);

      String var7 = null;
      boolean var14 = false;
      boolean var8 = false;

      boolean var10;
      for(int var9 = var6; !var14; var14 = var10) {
         if (var9 == 1) {
            throw new RuntimeException("Unexpected end of document");
         }

         boolean var16;
         if (var9 != 2) {
            if (var9 != 3) {
               var16 = var8;
               var13 = var7;
               var10 = var14;
            } else {
               String var11 = var1.getName();
               if (var8 && var11.equals(var7)) {
                  var13 = null;
                  var16 = false;
                  var10 = var14;
               } else if (var11.equals("group")) {
                  var4.resetGroup();
                  var16 = var8;
                  var13 = var7;
                  var10 = var14;
               } else if (var11.equals("item")) {
                  var16 = var8;
                  var13 = var7;
                  var10 = var14;
                  if (!var4.hasAddedItem()) {
                     ActionProvider var15 = var4.itemActionProvider;
                     if (var15 != null && var15.hasSubMenu()) {
                        var4.addSubMenuItem();
                        var16 = var8;
                        var13 = var7;
                        var10 = var14;
                     } else {
                        var4.addItem();
                        var16 = var8;
                        var13 = var7;
                        var10 = var14;
                     }
                  }
               } else {
                  var16 = var8;
                  var13 = var7;
                  var10 = var14;
                  if (var11.equals("menu")) {
                     var10 = true;
                     var16 = var8;
                     var13 = var7;
                  }
               }
            }
         } else if (var8) {
            var16 = var8;
            var13 = var7;
            var10 = var14;
         } else {
            var13 = var1.getName();
            if (var13.equals("group")) {
               var4.readGroup(var2);
               var16 = var8;
               var13 = var7;
               var10 = var14;
            } else if (var13.equals("item")) {
               var4.readItem(var2);
               var16 = var8;
               var13 = var7;
               var10 = var14;
            } else if (var13.equals("menu")) {
               this.parseMenu(var1, var2, var4.addSubMenuItem());
               var16 = var8;
               var13 = var7;
               var10 = var14;
            } else {
               var16 = true;
               var10 = var14;
            }
         }

         var9 = var1.next();
         var8 = var16;
         var7 = var13;
      }

   }

   Object getRealOwner() {
      if (this.mRealOwner == null) {
         this.mRealOwner = this.findRealOwner(this.mContext);
      }

      return this.mRealOwner;
   }

   public void inflate(int param1, Menu param2) {
      // $FF: Couldn't be decompiled
   }

   private static class InflatedOnMenuItemClickListener implements OnMenuItemClickListener {
      private static final Class[] PARAM_TYPES = new Class[]{MenuItem.class};
      private Method mMethod;
      private Object mRealOwner;

      public InflatedOnMenuItemClickListener(Object var1, String var2) {
         this.mRealOwner = var1;
         Class var3 = var1.getClass();

         try {
            this.mMethod = var3.getMethod(var2, PARAM_TYPES);
         } catch (Exception var5) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Couldn't resolve menu item onClick handler ");
            var4.append(var2);
            var4.append(" in class ");
            var4.append(var3.getName());
            InflateException var6 = new InflateException(var4.toString());
            var6.initCause(var5);
            throw var6;
         }
      }

      public boolean onMenuItemClick(MenuItem var1) {
         try {
            if (this.mMethod.getReturnType() == Boolean.TYPE) {
               return (Boolean)this.mMethod.invoke(this.mRealOwner, var1);
            } else {
               this.mMethod.invoke(this.mRealOwner, var1);
               return true;
            }
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   private class MenuState {
      private int groupCategory;
      private int groupCheckable;
      private boolean groupEnabled;
      private int groupId;
      private int groupOrder;
      private boolean groupVisible;
      ActionProvider itemActionProvider;
      private String itemActionProviderClassName;
      private String itemActionViewClassName;
      private int itemActionViewLayout;
      private boolean itemAdded;
      private int itemAlphabeticModifiers;
      private char itemAlphabeticShortcut;
      private int itemCategoryOrder;
      private int itemCheckable;
      private boolean itemChecked;
      private CharSequence itemContentDescription;
      private boolean itemEnabled;
      private int itemIconResId;
      private ColorStateList itemIconTintList = null;
      private Mode itemIconTintMode = null;
      private int itemId;
      private String itemListenerMethodName;
      private int itemNumericModifiers;
      private char itemNumericShortcut;
      private int itemShowAsAction;
      private CharSequence itemTitle;
      private CharSequence itemTitleCondensed;
      private CharSequence itemTooltipText;
      private boolean itemVisible;
      private Menu menu;

      public MenuState(Menu var2) {
         this.menu = var2;
         this.resetGroup();
      }

      private char getShortcut(String var1) {
         return var1 == null ? '\u0000' : var1.charAt(0);
      }

      private Object newInstance(String var1, Class[] var2, Object[] var3) {
         try {
            Constructor var6 = SupportMenuInflater.this.mContext.getClassLoader().loadClass(var1).getConstructor(var2);
            var6.setAccessible(true);
            Object var7 = var6.newInstance(var3);
            return var7;
         } catch (Exception var4) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Cannot instantiate class: ");
            var5.append(var1);
            Log.w("SupportMenuInflater", var5.toString(), var4);
            return null;
         }
      }

      private void setItem(MenuItem var1) {
         MenuItem var2 = var1.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled);
         int var3 = this.itemCheckable;
         boolean var4 = false;
         boolean var5;
         if (var3 >= 1) {
            var5 = true;
         } else {
            var5 = false;
         }

         var2.setCheckable(var5).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId);
         var3 = this.itemShowAsAction;
         if (var3 >= 0) {
            var1.setShowAsAction(var3);
         }

         if (this.itemListenerMethodName != null) {
            if (SupportMenuInflater.this.mContext.isRestricted()) {
               throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
            }

            var1.setOnMenuItemClickListener(new SupportMenuInflater.InflatedOnMenuItemClickListener(SupportMenuInflater.this.getRealOwner(), this.itemListenerMethodName));
         }

         var5 = var1 instanceof MenuItemImpl;
         if (var5) {
            MenuItemImpl var6 = (MenuItemImpl)var1;
         }

         if (this.itemCheckable >= 2) {
            if (var5) {
               ((MenuItemImpl)var1).setExclusiveCheckable(true);
            } else if (var1 instanceof MenuItemWrapperICS) {
               ((MenuItemWrapperICS)var1).setExclusiveCheckable(true);
            }
         }

         String var7 = this.itemActionViewClassName;
         if (var7 != null) {
            var1.setActionView((View)this.newInstance(var7, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
            var4 = true;
         }

         var3 = this.itemActionViewLayout;
         if (var3 > 0) {
            if (!var4) {
               var1.setActionView(var3);
            } else {
               Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
            }
         }

         ActionProvider var8 = this.itemActionProvider;
         if (var8 != null) {
            MenuItemCompat.setActionProvider(var1, var8);
         }

         MenuItemCompat.setContentDescription(var1, this.itemContentDescription);
         MenuItemCompat.setTooltipText(var1, this.itemTooltipText);
         MenuItemCompat.setAlphabeticShortcut(var1, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
         MenuItemCompat.setNumericShortcut(var1, this.itemNumericShortcut, this.itemNumericModifiers);
         Mode var9 = this.itemIconTintMode;
         if (var9 != null) {
            MenuItemCompat.setIconTintMode(var1, var9);
         }

         ColorStateList var10 = this.itemIconTintList;
         if (var10 != null) {
            MenuItemCompat.setIconTintList(var1, var10);
         }

      }

      public void addItem() {
         this.itemAdded = true;
         this.setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
      }

      public SubMenu addSubMenuItem() {
         this.itemAdded = true;
         SubMenu var1 = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
         this.setItem(var1.getItem());
         return var1;
      }

      public boolean hasAddedItem() {
         return this.itemAdded;
      }

      public void readGroup(AttributeSet var1) {
         TypedArray var2 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R$styleable.MenuGroup);
         this.groupId = var2.getResourceId(R$styleable.MenuGroup_android_id, 0);
         this.groupCategory = var2.getInt(R$styleable.MenuGroup_android_menuCategory, 0);
         this.groupOrder = var2.getInt(R$styleable.MenuGroup_android_orderInCategory, 0);
         this.groupCheckable = var2.getInt(R$styleable.MenuGroup_android_checkableBehavior, 0);
         this.groupVisible = var2.getBoolean(R$styleable.MenuGroup_android_visible, true);
         this.groupEnabled = var2.getBoolean(R$styleable.MenuGroup_android_enabled, true);
         var2.recycle();
      }

      public void readItem(AttributeSet var1) {
         TypedArray var3 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R$styleable.MenuItem);
         this.itemId = var3.getResourceId(R$styleable.MenuItem_android_id, 0);
         this.itemCategoryOrder = var3.getInt(R$styleable.MenuItem_android_menuCategory, this.groupCategory) & -65536 | var3.getInt(R$styleable.MenuItem_android_orderInCategory, this.groupOrder) & '\uffff';
         this.itemTitle = var3.getText(R$styleable.MenuItem_android_title);
         this.itemTitleCondensed = var3.getText(R$styleable.MenuItem_android_titleCondensed);
         this.itemIconResId = var3.getResourceId(R$styleable.MenuItem_android_icon, 0);
         this.itemAlphabeticShortcut = this.getShortcut(var3.getString(R$styleable.MenuItem_android_alphabeticShortcut));
         this.itemAlphabeticModifiers = var3.getInt(R$styleable.MenuItem_alphabeticModifiers, 4096);
         this.itemNumericShortcut = this.getShortcut(var3.getString(R$styleable.MenuItem_android_numericShortcut));
         this.itemNumericModifiers = var3.getInt(R$styleable.MenuItem_numericModifiers, 4096);
         if (var3.hasValue(R$styleable.MenuItem_android_checkable)) {
            this.itemCheckable = var3.getBoolean(R$styleable.MenuItem_android_checkable, false);
         } else {
            this.itemCheckable = this.groupCheckable;
         }

         this.itemChecked = var3.getBoolean(R$styleable.MenuItem_android_checked, false);
         this.itemVisible = var3.getBoolean(R$styleable.MenuItem_android_visible, this.groupVisible);
         this.itemEnabled = var3.getBoolean(R$styleable.MenuItem_android_enabled, this.groupEnabled);
         this.itemShowAsAction = var3.getInt(R$styleable.MenuItem_showAsAction, -1);
         this.itemListenerMethodName = var3.getString(R$styleable.MenuItem_android_onClick);
         this.itemActionViewLayout = var3.getResourceId(R$styleable.MenuItem_actionLayout, 0);
         this.itemActionViewClassName = var3.getString(R$styleable.MenuItem_actionViewClass);
         this.itemActionProviderClassName = var3.getString(R$styleable.MenuItem_actionProviderClass);
         boolean var2;
         if (this.itemActionProviderClassName != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2 && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
            this.itemActionProvider = (ActionProvider)this.newInstance(this.itemActionProviderClassName, SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionProviderConstructorArguments);
         } else {
            if (var2) {
               Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
            }

            this.itemActionProvider = null;
         }

         this.itemContentDescription = var3.getText(R$styleable.MenuItem_contentDescription);
         this.itemTooltipText = var3.getText(R$styleable.MenuItem_tooltipText);
         if (var3.hasValue(R$styleable.MenuItem_iconTintMode)) {
            this.itemIconTintMode = DrawableUtils.parseTintMode(var3.getInt(R$styleable.MenuItem_iconTintMode, -1), this.itemIconTintMode);
         } else {
            this.itemIconTintMode = null;
         }

         if (var3.hasValue(R$styleable.MenuItem_iconTint)) {
            this.itemIconTintList = var3.getColorStateList(R$styleable.MenuItem_iconTint);
         } else {
            this.itemIconTintList = null;
         }

         var3.recycle();
         this.itemAdded = false;
      }

      public void resetGroup() {
         this.groupId = 0;
         this.groupCategory = 0;
         this.groupOrder = 0;
         this.groupCheckable = 0;
         this.groupVisible = true;
         this.groupEnabled = true;
      }
   }
}
