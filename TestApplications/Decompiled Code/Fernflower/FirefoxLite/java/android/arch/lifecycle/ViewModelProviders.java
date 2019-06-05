package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class ViewModelProviders {
   private static Activity checkActivity(Fragment var0) {
      FragmentActivity var1 = var0.getActivity();
      if (var1 != null) {
         return var1;
      } else {
         throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
      }
   }

   private static Application checkApplication(Activity var0) {
      Application var1 = var0.getApplication();
      if (var1 != null) {
         return var1;
      } else {
         throw new IllegalStateException("Your activity/fragment is not yet attached to Application. You can't request ViewModel before onCreate call.");
      }
   }

   public static ViewModelProvider of(Fragment var0) {
      return of((Fragment)var0, (ViewModelProvider.Factory)null);
   }

   public static ViewModelProvider of(Fragment var0, ViewModelProvider.Factory var1) {
      Application var2 = checkApplication(checkActivity(var0));
      Object var3 = var1;
      if (var1 == null) {
         var3 = ViewModelProvider.AndroidViewModelFactory.getInstance(var2);
      }

      return new ViewModelProvider(ViewModelStores.of(var0), (ViewModelProvider.Factory)var3);
   }

   public static ViewModelProvider of(FragmentActivity var0) {
      return of((FragmentActivity)var0, (ViewModelProvider.Factory)null);
   }

   public static ViewModelProvider of(FragmentActivity var0, ViewModelProvider.Factory var1) {
      Application var2 = checkApplication(var0);
      Object var3 = var1;
      if (var1 == null) {
         var3 = ViewModelProvider.AndroidViewModelFactory.getInstance(var2);
      }

      return new ViewModelProvider(ViewModelStores.of(var0), (ViewModelProvider.Factory)var3);
   }
}
