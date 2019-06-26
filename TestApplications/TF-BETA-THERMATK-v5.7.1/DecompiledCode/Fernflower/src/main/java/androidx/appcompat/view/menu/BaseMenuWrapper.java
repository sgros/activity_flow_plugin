package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.collection.ArrayMap;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.internal.view.SupportSubMenu;
import java.util.Iterator;
import java.util.Map;

abstract class BaseMenuWrapper extends BaseWrapper {
   final Context mContext;
   private Map mMenuItems;
   private Map mSubMenus;

   BaseMenuWrapper(Context var1, Object var2) {
      super(var2);
      this.mContext = var1;
   }

   final MenuItem getMenuItemWrapper(MenuItem var1) {
      MenuItem var2 = var1;
      if (var1 instanceof SupportMenuItem) {
         SupportMenuItem var3 = (SupportMenuItem)var1;
         if (this.mMenuItems == null) {
            this.mMenuItems = new ArrayMap();
         }

         var1 = (MenuItem)this.mMenuItems.get(var1);
         var2 = var1;
         if (var1 == null) {
            var2 = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, var3);
            this.mMenuItems.put(var3, var2);
         }
      }

      return var2;
   }

   final SubMenu getSubMenuWrapper(SubMenu var1) {
      if (var1 instanceof SupportSubMenu) {
         SupportSubMenu var2 = (SupportSubMenu)var1;
         if (this.mSubMenus == null) {
            this.mSubMenus = new ArrayMap();
         }

         SubMenu var3 = (SubMenu)this.mSubMenus.get(var2);
         var1 = var3;
         if (var3 == null) {
            var1 = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, var2);
            this.mSubMenus.put(var2, var1);
         }

         return var1;
      } else {
         return var1;
      }
   }

   final void internalClear() {
      Map var1 = this.mMenuItems;
      if (var1 != null) {
         var1.clear();
      }

      var1 = this.mSubMenus;
      if (var1 != null) {
         var1.clear();
      }

   }

   final void internalRemoveGroup(int var1) {
      Map var2 = this.mMenuItems;
      if (var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            if (var1 == ((MenuItem)var3.next()).getGroupId()) {
               var3.remove();
            }
         }

      }
   }

   final void internalRemoveItem(int var1) {
      Map var2 = this.mMenuItems;
      if (var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            if (var1 == ((MenuItem)var3.next()).getItemId()) {
               var3.remove();
               break;
            }
         }

      }
   }
}
