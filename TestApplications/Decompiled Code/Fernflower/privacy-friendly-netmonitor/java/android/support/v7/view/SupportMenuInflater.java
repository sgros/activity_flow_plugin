package android.support.v7.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.support.v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SupportMenuInflater extends MenuInflater {
   static final Class[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
   static final Class[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class};
   static final String LOG_TAG = "SupportMenuInflater";
   static final int NO_ID = 0;
   private static final String XML_GROUP = "group";
   private static final String XML_ITEM = "item";
   private static final String XML_MENU = "menu";
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
         return var1 instanceof ContextWrapper ? this.findRealOwner(((ContextWrapper)var1).getBaseContext()) : var1;
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
      boolean var8 = var14;

      boolean var10;
      for(int var9 = var6; !var14; var14 = var10) {
         boolean var15;
         switch(var9) {
         case 1:
            throw new RuntimeException("Unexpected end of document");
         case 2:
            if (var8) {
               var15 = var8;
               var13 = var7;
               var10 = var14;
            } else {
               var13 = var1.getName();
               if (var13.equals("group")) {
                  var4.readGroup(var2);
                  var15 = var8;
                  var13 = var7;
                  var10 = var14;
               } else if (var13.equals("item")) {
                  var4.readItem(var2);
                  var15 = var8;
                  var13 = var7;
                  var10 = var14;
               } else if (var13.equals("menu")) {
                  this.parseMenu(var1, var2, var4.addSubMenuItem());
                  var15 = var8;
                  var13 = var7;
                  var10 = var14;
               } else {
                  var15 = true;
                  var10 = var14;
               }
            }
            break;
         case 3:
            String var11 = var1.getName();
            if (var8 && var11.equals(var7)) {
               var13 = null;
               var15 = false;
               var10 = var14;
            } else if (var11.equals("group")) {
               var4.resetGroup();
               var15 = var8;
               var13 = var7;
               var10 = var14;
            } else if (var11.equals("item")) {
               var15 = var8;
               var13 = var7;
               var10 = var14;
               if (!var4.hasAddedItem()) {
                  if (var4.itemActionProvider != null && var4.itemActionProvider.hasSubMenu()) {
                     var4.addSubMenuItem();
                     var15 = var8;
                     var13 = var7;
                     var10 = var14;
                  } else {
                     var4.addItem();
                     var15 = var8;
                     var13 = var7;
                     var10 = var14;
                  }
               }
            } else {
               var15 = var8;
               var13 = var7;
               var10 = var14;
               if (var11.equals("menu")) {
                  var10 = true;
                  var15 = var8;
                  var13 = var7;
               }
            }
            break;
         default:
            var15 = var8;
            var13 = var7;
            var10 = var14;
         }

         var9 = var1.next();
         var8 = var15;
         var7 = var13;
      }

   }

   Object getRealOwner() {
      if (this.mRealOwner == null) {
         this.mRealOwner = this.findRealOwner(this.mContext);
      }

      return this.mRealOwner;
   }

   public void inflate(int var1, Menu var2) {
      if (!(var2 instanceof SupportMenu)) {
         super.inflate(var1, var2);
      } else {
         InflateException var3 = null;
         Object var4 = null;
         XmlResourceParser var5 = null;

         XmlResourceParser var120;
         label889: {
            Throwable var10000;
            label890: {
               IOException var6;
               boolean var10001;
               XmlResourceParser var115;
               label881: {
                  label891: {
                     XmlPullParserException var119;
                     label892: {
                        label878: {
                           try {
                              try {
                                 var120 = this.mContext.getResources().getLayout(var1);
                                 break label878;
                              } catch (XmlPullParserException var112) {
                                 var119 = var112;
                              } catch (IOException var113) {
                                 var6 = var113;
                                 break label891;
                              }
                           } catch (Throwable var114) {
                              var10000 = var114;
                              var10001 = false;
                              break label890;
                           }

                           var115 = (XmlResourceParser)var4;
                           break label892;
                        }

                        XmlPullParserException var118;
                        label870: {
                           IOException var117;
                           try {
                              this.parseMenu(var120, Xml.asAttributeSet(var120), var2);
                              break label889;
                           } catch (XmlPullParserException var109) {
                              var118 = var109;
                              break label870;
                           } catch (IOException var110) {
                              var117 = var110;
                           } finally {
                              ;
                           }

                           var115 = var120;
                           var6 = var117;
                           break label881;
                        }

                        var115 = var120;
                        var119 = var118;
                     }

                     var5 = var115;

                     try {
                        var3 = new InflateException;
                     } catch (Throwable var108) {
                        var10000 = var108;
                        var10001 = false;
                        break label890;
                     }

                     var5 = var115;

                     try {
                        var3.<init>("Error inflating menu XML", var119);
                     } catch (Throwable var107) {
                        var10000 = var107;
                        var10001 = false;
                        break label890;
                     }

                     var5 = var115;

                     try {
                        throw var3;
                     } catch (Throwable var104) {
                        var10000 = var104;
                        var10001 = false;
                        break label890;
                     }
                  }

                  var115 = var3;
               }

               var5 = var115;

               try {
                  var3 = new InflateException;
               } catch (Throwable var106) {
                  var10000 = var106;
                  var10001 = false;
                  break label890;
               }

               var5 = var115;

               try {
                  var3.<init>("Error inflating menu XML", var6);
               } catch (Throwable var105) {
                  var10000 = var105;
                  var10001 = false;
                  break label890;
               }

               var5 = var115;

               label843:
               try {
                  throw var3;
               } catch (Throwable var103) {
                  var10000 = var103;
                  var10001 = false;
                  break label843;
               }
            }

            Throwable var116 = var10000;
            if (var5 != null) {
               var5.close();
            }

            throw var116;
         }

         if (var120 != null) {
            var120.close();
         }

      }
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
      private static final int defaultGroupId = 0;
      private static final int defaultItemCategory = 0;
      private static final int defaultItemCheckable = 0;
      private static final boolean defaultItemChecked = false;
      private static final boolean defaultItemEnabled = true;
      private static final int defaultItemId = 0;
      private static final int defaultItemOrder = 0;
      private static final boolean defaultItemVisible = true;
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
            Constructor var5 = SupportMenuInflater.this.mContext.getClassLoader().loadClass(var1).getConstructor(var2);
            var5.setAccessible(true);
            Object var6 = var5.newInstance(var3);
            return var6;
         } catch (Exception var4) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Cannot instantiate class: ");
            var7.append(var1);
            Log.w("SupportMenuInflater", var7.toString(), var4);
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
         if (this.itemShowAsAction >= 0) {
            var1.setShowAsAction(this.itemShowAsAction);
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

         if (this.itemActionViewClassName != null) {
            var1.setActionView((View)this.newInstance(this.itemActionViewClassName, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
            var4 = true;
         }

         if (this.itemActionViewLayout > 0) {
            if (!var4) {
               var1.setActionView(this.itemActionViewLayout);
            } else {
               Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
            }
         }

         if (this.itemActionProvider != null) {
            MenuItemCompat.setActionProvider(var1, this.itemActionProvider);
         }

         MenuItemCompat.setContentDescription(var1, this.itemContentDescription);
         MenuItemCompat.setTooltipText(var1, this.itemTooltipText);
         MenuItemCompat.setAlphabeticShortcut(var1, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
         MenuItemCompat.setNumericShortcut(var1, this.itemNumericShortcut, this.itemNumericModifiers);
         if (this.itemIconTintMode != null) {
            MenuItemCompat.setIconTintMode(var1, this.itemIconTintMode);
         }

         if (this.itemIconTintList != null) {
            MenuItemCompat.setIconTintList(var1, this.itemIconTintList);
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
         TypedArray var2 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R.styleable.MenuGroup);
         this.groupId = var2.getResourceId(R.styleable.MenuGroup_android_id, 0);
         this.groupCategory = var2.getInt(R.styleable.MenuGroup_android_menuCategory, 0);
         this.groupOrder = var2.getInt(R.styleable.MenuGroup_android_orderInCategory, 0);
         this.groupCheckable = var2.getInt(R.styleable.MenuGroup_android_checkableBehavior, 0);
         this.groupVisible = var2.getBoolean(R.styleable.MenuGroup_android_visible, true);
         this.groupEnabled = var2.getBoolean(R.styleable.MenuGroup_android_enabled, true);
         var2.recycle();
      }

      public void readItem(AttributeSet var1) {
         TypedArray var3 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R.styleable.MenuItem);
         this.itemId = var3.getResourceId(R.styleable.MenuItem_android_id, 0);
         this.itemCategoryOrder = var3.getInt(R.styleable.MenuItem_android_menuCategory, this.groupCategory) & -65536 | var3.getInt(R.styleable.MenuItem_android_orderInCategory, this.groupOrder) & '\uffff';
         this.itemTitle = var3.getText(R.styleable.MenuItem_android_title);
         this.itemTitleCondensed = var3.getText(R.styleable.MenuItem_android_titleCondensed);
         this.itemIconResId = var3.getResourceId(R.styleable.MenuItem_android_icon, 0);
         this.itemAlphabeticShortcut = this.getShortcut(var3.getString(R.styleable.MenuItem_android_alphabeticShortcut));
         this.itemAlphabeticModifiers = var3.getInt(R.styleable.MenuItem_alphabeticModifiers, 4096);
         this.itemNumericShortcut = this.getShortcut(var3.getString(R.styleable.MenuItem_android_numericShortcut));
         this.itemNumericModifiers = var3.getInt(R.styleable.MenuItem_numericModifiers, 4096);
         if (var3.hasValue(R.styleable.MenuItem_android_checkable)) {
            this.itemCheckable = var3.getBoolean(R.styleable.MenuItem_android_checkable, false);
         } else {
            this.itemCheckable = this.groupCheckable;
         }

         this.itemChecked = var3.getBoolean(R.styleable.MenuItem_android_checked, false);
         this.itemVisible = var3.getBoolean(R.styleable.MenuItem_android_visible, this.groupVisible);
         this.itemEnabled = var3.getBoolean(R.styleable.MenuItem_android_enabled, this.groupEnabled);
         this.itemShowAsAction = var3.getInt(R.styleable.MenuItem_showAsAction, -1);
         this.itemListenerMethodName = var3.getString(R.styleable.MenuItem_android_onClick);
         this.itemActionViewLayout = var3.getResourceId(R.styleable.MenuItem_actionLayout, 0);
         this.itemActionViewClassName = var3.getString(R.styleable.MenuItem_actionViewClass);
         this.itemActionProviderClassName = var3.getString(R.styleable.MenuItem_actionProviderClass);
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

         this.itemContentDescription = var3.getText(R.styleable.MenuItem_contentDescription);
         this.itemTooltipText = var3.getText(R.styleable.MenuItem_tooltipText);
         if (var3.hasValue(R.styleable.MenuItem_iconTintMode)) {
            this.itemIconTintMode = DrawableUtils.parseTintMode(var3.getInt(R.styleable.MenuItem_iconTintMode, -1), this.itemIconTintMode);
         } else {
            this.itemIconTintMode = null;
         }

         if (var3.hasValue(R.styleable.MenuItem_iconTint)) {
            this.itemIconTintList = var3.getColorStateList(R.styleable.MenuItem_iconTint);
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
