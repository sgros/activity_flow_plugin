package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract class AppCompatDelegate {
   private static int sDefaultNightMode;

   AppCompatDelegate() {
   }

   public static AppCompatDelegate create(Activity var0, AppCompatCallback var1) {
      return new AppCompatDelegateImpl(var0, var0.getWindow(), var1);
   }

   public static AppCompatDelegate create(Dialog var0, AppCompatCallback var1) {
      return new AppCompatDelegateImpl(var0.getContext(), var0.getWindow(), var1);
   }

   public static int getDefaultNightMode() {
      return sDefaultNightMode;
   }

   public abstract void addContentView(View var1, LayoutParams var2);

   public abstract boolean applyDayNight();

   public abstract View findViewById(int var1);

   public abstract MenuInflater getMenuInflater();

   public abstract ActionBar getSupportActionBar();

   public abstract void installViewFactory();

   public abstract void invalidateOptionsMenu();

   public abstract void onConfigurationChanged(Configuration var1);

   public abstract void onCreate(Bundle var1);

   public abstract void onDestroy();

   public abstract void onPostCreate(Bundle var1);

   public abstract void onPostResume();

   public abstract void onSaveInstanceState(Bundle var1);

   public abstract void onStart();

   public abstract void onStop();

   public abstract boolean requestWindowFeature(int var1);

   public abstract void setContentView(int var1);

   public abstract void setContentView(View var1);

   public abstract void setContentView(View var1, LayoutParams var2);

   public abstract void setSupportActionBar(Toolbar var1);

   public abstract void setTitle(CharSequence var1);
}
