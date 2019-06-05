package android.arch.lifecycle;

import android.app.Application;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
   private final ViewModelProvider.Factory mFactory;
   private final ViewModelStore mViewModelStore;

   public ViewModelProvider(ViewModelStore var1, ViewModelProvider.Factory var2) {
      this.mFactory = var2;
      this.mViewModelStore = var1;
   }

   public ViewModel get(Class var1) {
      String var2 = var1.getCanonicalName();
      if (var2 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append("android.arch.lifecycle.ViewModelProvider.DefaultKey:");
         var3.append(var2);
         return this.get(var3.toString(), var1);
      } else {
         throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
      }
   }

   public ViewModel get(String var1, Class var2) {
      ViewModel var3 = this.mViewModelStore.get(var1);
      if (var2.isInstance(var3)) {
         return var3;
      } else {
         ViewModel var4 = this.mFactory.create(var2);
         this.mViewModelStore.put(var1, var4);
         return var4;
      }
   }

   public static class AndroidViewModelFactory extends ViewModelProvider.NewInstanceFactory {
      private static ViewModelProvider.AndroidViewModelFactory sInstance;
      private Application mApplication;

      public AndroidViewModelFactory(Application var1) {
         this.mApplication = var1;
      }

      public static ViewModelProvider.AndroidViewModelFactory getInstance(Application var0) {
         if (sInstance == null) {
            sInstance = new ViewModelProvider.AndroidViewModelFactory(var0);
         }

         return sInstance;
      }

      public ViewModel create(Class var1) {
         if (AndroidViewModel.class.isAssignableFrom(var1)) {
            StringBuilder var3;
            try {
               ViewModel var8 = (ViewModel)var1.getConstructor(Application.class).newInstance(this.mApplication);
               return var8;
            } catch (NoSuchMethodException var4) {
               var3 = new StringBuilder();
               var3.append("Cannot create an instance of ");
               var3.append(var1);
               throw new RuntimeException(var3.toString(), var4);
            } catch (IllegalAccessException var5) {
               StringBuilder var2 = new StringBuilder();
               var2.append("Cannot create an instance of ");
               var2.append(var1);
               throw new RuntimeException(var2.toString(), var5);
            } catch (InstantiationException var6) {
               var3 = new StringBuilder();
               var3.append("Cannot create an instance of ");
               var3.append(var1);
               throw new RuntimeException(var3.toString(), var6);
            } catch (InvocationTargetException var7) {
               var3 = new StringBuilder();
               var3.append("Cannot create an instance of ");
               var3.append(var1);
               throw new RuntimeException(var3.toString(), var7);
            }
         } else {
            return super.create(var1);
         }
      }
   }

   public interface Factory {
      ViewModel create(Class var1);
   }

   public static class NewInstanceFactory implements ViewModelProvider.Factory {
      public ViewModel create(Class var1) {
         try {
            ViewModel var6 = (ViewModel)var1.newInstance();
            return var6;
         } catch (InstantiationException var4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Cannot create an instance of ");
            var3.append(var1);
            throw new RuntimeException(var3.toString(), var4);
         } catch (IllegalAccessException var5) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Cannot create an instance of ");
            var2.append(var1);
            throw new RuntimeException(var2.toString(), var5);
         }
      }
   }
}
