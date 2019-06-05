package android.support.v4.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build.VERSION;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v4.util.Preconditions;
import android.text.Editable;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode.Callback;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TextViewCompat {
   private static Field sMaxModeField;
   private static boolean sMaxModeFieldFetched;
   private static Field sMaximumField;
   private static boolean sMaximumFieldFetched;

   public static Drawable[] getCompoundDrawablesRelative(TextView var0) {
      if (VERSION.SDK_INT >= 18) {
         return var0.getCompoundDrawablesRelative();
      } else if (VERSION.SDK_INT >= 17) {
         int var1 = var0.getLayoutDirection();
         boolean var2 = true;
         if (var1 != 1) {
            var2 = false;
         }

         Drawable[] var5 = var0.getCompoundDrawables();
         if (var2) {
            Drawable var3 = var5[2];
            Drawable var4 = var5[0];
            var5[0] = var3;
            var5[2] = var4;
         }

         return var5;
      } else {
         return var0.getCompoundDrawables();
      }
   }

   public static int getFirstBaselineToTopHeight(TextView var0) {
      return var0.getPaddingTop() - var0.getPaint().getFontMetricsInt().top;
   }

   public static int getLastBaselineToBottomHeight(TextView var0) {
      return var0.getPaddingBottom() + var0.getPaint().getFontMetricsInt().bottom;
   }

   public static int getMaxLines(TextView var0) {
      if (VERSION.SDK_INT >= 16) {
         return var0.getMaxLines();
      } else {
         if (!sMaxModeFieldFetched) {
            sMaxModeField = retrieveField("mMaxMode");
            sMaxModeFieldFetched = true;
         }

         if (sMaxModeField != null && retrieveIntFromField(sMaxModeField, var0) == 1) {
            if (!sMaximumFieldFetched) {
               sMaximumField = retrieveField("mMaximum");
               sMaximumFieldFetched = true;
            }

            if (sMaximumField != null) {
               return retrieveIntFromField(sMaximumField, var0);
            }
         }

         return -1;
      }
   }

   private static int getTextDirection(TextDirectionHeuristic var0) {
      if (var0 == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
         return 1;
      } else if (var0 == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
         return 1;
      } else if (var0 == TextDirectionHeuristics.ANYRTL_LTR) {
         return 2;
      } else if (var0 == TextDirectionHeuristics.LTR) {
         return 3;
      } else if (var0 == TextDirectionHeuristics.RTL) {
         return 4;
      } else if (var0 == TextDirectionHeuristics.LOCALE) {
         return 5;
      } else if (var0 == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
         return 6;
      } else {
         return var0 == TextDirectionHeuristics.FIRSTSTRONG_RTL ? 7 : 1;
      }
   }

   private static TextDirectionHeuristic getTextDirectionHeuristic(TextView var0) {
      if (var0.getTransformationMethod() instanceof PasswordTransformationMethod) {
         return TextDirectionHeuristics.LTR;
      } else {
         int var1 = VERSION.SDK_INT;
         boolean var2 = false;
         if (var1 >= 28 && (var0.getInputType() & 15) == 3) {
            byte var4 = Character.getDirectionality(DecimalFormatSymbols.getInstance(var0.getTextLocale()).getDigitStrings()[0].codePointAt(0));
            return var4 != 1 && var4 != 2 ? TextDirectionHeuristics.LTR : TextDirectionHeuristics.RTL;
         } else {
            if (var0.getLayoutDirection() == 1) {
               var2 = true;
            }

            switch(var0.getTextDirection()) {
            case 2:
               return TextDirectionHeuristics.ANYRTL_LTR;
            case 3:
               return TextDirectionHeuristics.LTR;
            case 4:
               return TextDirectionHeuristics.RTL;
            case 5:
               return TextDirectionHeuristics.LOCALE;
            case 6:
               return TextDirectionHeuristics.FIRSTSTRONG_LTR;
            case 7:
               return TextDirectionHeuristics.FIRSTSTRONG_RTL;
            default:
               TextDirectionHeuristic var3;
               if (var2) {
                  var3 = TextDirectionHeuristics.FIRSTSTRONG_RTL;
               } else {
                  var3 = TextDirectionHeuristics.FIRSTSTRONG_LTR;
               }

               return var3;
            }
         }
      }
   }

   public static PrecomputedTextCompat.Params getTextMetricsParams(TextView var0) {
      if (VERSION.SDK_INT >= 28) {
         return new PrecomputedTextCompat.Params(var0.getTextMetricsParams());
      } else {
         PrecomputedTextCompat.Params.Builder var1 = new PrecomputedTextCompat.Params.Builder(new TextPaint(var0.getPaint()));
         if (VERSION.SDK_INT >= 23) {
            var1.setBreakStrategy(var0.getBreakStrategy());
            var1.setHyphenationFrequency(var0.getHyphenationFrequency());
         }

         if (VERSION.SDK_INT >= 18) {
            var1.setTextDirection(getTextDirectionHeuristic(var0));
         }

         return var1.build();
      }
   }

   private static Field retrieveField(String var0) {
      Field var1;
      label19: {
         try {
            var1 = TextView.class.getDeclaredField(var0);
         } catch (NoSuchFieldException var4) {
            var1 = null;
            break label19;
         }

         try {
            var1.setAccessible(true);
            return var1;
         } catch (NoSuchFieldException var3) {
         }
      }

      StringBuilder var2 = new StringBuilder();
      var2.append("Could not retrieve ");
      var2.append(var0);
      var2.append(" field.");
      Log.e("TextViewCompat", var2.toString());
      return var1;
   }

   private static int retrieveIntFromField(Field var0, TextView var1) {
      try {
         int var2 = var0.getInt(var1);
         return var2;
      } catch (IllegalAccessException var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Could not retrieve value of ");
         var4.append(var0.getName());
         var4.append(" field.");
         Log.d("TextViewCompat", var4.toString());
         return -1;
      }
   }

   public static void setCompoundDrawablesRelative(TextView var0, Drawable var1, Drawable var2, Drawable var3, Drawable var4) {
      if (VERSION.SDK_INT >= 18) {
         var0.setCompoundDrawablesRelative(var1, var2, var3, var4);
      } else if (VERSION.SDK_INT >= 17) {
         int var5 = var0.getLayoutDirection();
         boolean var6 = true;
         if (var5 != 1) {
            var6 = false;
         }

         Drawable var7;
         if (var6) {
            var7 = var3;
         } else {
            var7 = var1;
         }

         if (!var6) {
            var1 = var3;
         }

         var0.setCompoundDrawables(var7, var2, var1, var4);
      } else {
         var0.setCompoundDrawables(var1, var2, var3, var4);
      }

   }

   public static void setFirstBaselineToTopHeight(TextView var0, int var1) {
      Preconditions.checkArgumentNonnegative(var1);
      if (VERSION.SDK_INT >= 28) {
         var0.setFirstBaselineToTopHeight(var1);
      } else {
         FontMetricsInt var2 = var0.getPaint().getFontMetricsInt();
         int var3;
         if (VERSION.SDK_INT >= 16 && !var0.getIncludeFontPadding()) {
            var3 = var2.ascent;
         } else {
            var3 = var2.top;
         }

         if (var1 > Math.abs(var3)) {
            var3 = -var3;
            var0.setPadding(var0.getPaddingLeft(), var1 - var3, var0.getPaddingRight(), var0.getPaddingBottom());
         }

      }
   }

   public static void setLastBaselineToBottomHeight(TextView var0, int var1) {
      Preconditions.checkArgumentNonnegative(var1);
      FontMetricsInt var2 = var0.getPaint().getFontMetricsInt();
      int var3;
      if (VERSION.SDK_INT >= 16 && !var0.getIncludeFontPadding()) {
         var3 = var2.descent;
      } else {
         var3 = var2.bottom;
      }

      if (var1 > Math.abs(var3)) {
         var0.setPadding(var0.getPaddingLeft(), var0.getPaddingTop(), var0.getPaddingRight(), var1 - var3);
      }

   }

   public static void setLineHeight(TextView var0, int var1) {
      Preconditions.checkArgumentNonnegative(var1);
      int var2 = var0.getPaint().getFontMetricsInt((FontMetricsInt)null);
      if (var1 != var2) {
         var0.setLineSpacing((float)(var1 - var2), 1.0F);
      }

   }

   public static void setPrecomputedText(TextView var0, PrecomputedTextCompat var1) {
      if (VERSION.SDK_INT >= 28) {
         var0.setText(var1.getPrecomputedText());
      } else {
         if (!getTextMetricsParams(var0).equals(var1.getParams())) {
            throw new IllegalArgumentException("Given text can not be applied to TextView.");
         }

         var0.setText(var1);
      }

   }

   public static void setTextAppearance(TextView var0, int var1) {
      if (VERSION.SDK_INT >= 23) {
         var0.setTextAppearance(var1);
      } else {
         var0.setTextAppearance(var0.getContext(), var1);
      }

   }

   public static void setTextMetricsParams(TextView var0, PrecomputedTextCompat.Params var1) {
      if (VERSION.SDK_INT >= 18) {
         var0.setTextDirection(getTextDirection(var1.getTextDirection()));
      }

      if (VERSION.SDK_INT < 23) {
         float var2 = var1.getTextPaint().getTextScaleX();
         var0.getPaint().set(var1.getTextPaint());
         if (var2 == var0.getTextScaleX()) {
            var0.setTextScaleX(var2 / 2.0F + 1.0F);
         }

         var0.setTextScaleX(var2);
      } else {
         var0.getPaint().set(var1.getTextPaint());
         var0.setBreakStrategy(var1.getBreakStrategy());
         var0.setHyphenationFrequency(var1.getHyphenationFrequency());
      }

   }

   public static Callback wrapCustomSelectionActionModeCallback(TextView var0, Callback var1) {
      return (Callback)(VERSION.SDK_INT >= 26 && VERSION.SDK_INT <= 27 && !(var1 instanceof TextViewCompat.OreoCallback) ? new TextViewCompat.OreoCallback(var1, var0) : var1);
   }

   private static class OreoCallback implements Callback {
      private final Callback mCallback;
      private boolean mCanUseMenuBuilderReferences;
      private boolean mInitializedMenuBuilderReferences;
      private Class mMenuBuilderClass;
      private Method mMenuBuilderRemoveItemAtMethod;
      private final TextView mTextView;

      OreoCallback(Callback var1, TextView var2) {
         this.mCallback = var1;
         this.mTextView = var2;
         this.mInitializedMenuBuilderReferences = false;
      }

      private Intent createProcessTextIntent() {
         return (new Intent()).setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
      }

      private Intent createProcessTextIntentForResolveInfo(ResolveInfo var1, TextView var2) {
         return this.createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", this.isEditable(var2) ^ true).setClassName(var1.activityInfo.packageName, var1.activityInfo.name);
      }

      private List getSupportedActivities(Context var1, PackageManager var2) {
         ArrayList var3 = new ArrayList();
         if (!(var1 instanceof Activity)) {
            return var3;
         } else {
            Iterator var4 = var2.queryIntentActivities(this.createProcessTextIntent(), 0).iterator();

            while(var4.hasNext()) {
               ResolveInfo var5 = (ResolveInfo)var4.next();
               if (this.isSupportedActivity(var5, var1)) {
                  var3.add(var5);
               }
            }

            return var3;
         }
      }

      private boolean isEditable(TextView var1) {
         boolean var2;
         if (var1 instanceof Editable && var1.onCheckIsTextEditor() && var1.isEnabled()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      private boolean isSupportedActivity(ResolveInfo var1, Context var2) {
         boolean var3 = var2.getPackageName().equals(var1.activityInfo.packageName);
         boolean var4 = true;
         if (var3) {
            return true;
         } else if (!var1.activityInfo.exported) {
            return false;
         } else {
            var3 = var4;
            if (var1.activityInfo.permission != null) {
               if (var2.checkSelfPermission(var1.activityInfo.permission) == 0) {
                  var3 = var4;
               } else {
                  var3 = false;
               }
            }

            return var3;
         }
      }

      private void recomputeProcessTextMenuItems(Menu var1) {
         Context var2 = this.mTextView.getContext();
         PackageManager var3 = var2.getPackageManager();
         if (!this.mInitializedMenuBuilderReferences) {
            this.mInitializedMenuBuilderReferences = true;

            try {
               this.mMenuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");
               this.mMenuBuilderRemoveItemAtMethod = this.mMenuBuilderClass.getDeclaredMethod("removeItemAt", Integer.TYPE);
               this.mCanUseMenuBuilderReferences = true;
            } catch (NoSuchMethodException | ClassNotFoundException var7) {
               this.mMenuBuilderClass = null;
               this.mMenuBuilderRemoveItemAtMethod = null;
               this.mCanUseMenuBuilderReferences = false;
            }
         }

         boolean var10001;
         Method var4;
         label67: {
            try {
               if (this.mCanUseMenuBuilderReferences && this.mMenuBuilderClass.isInstance(var1)) {
                  var4 = this.mMenuBuilderRemoveItemAtMethod;
                  break label67;
               }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var11) {
               var10001 = false;
               return;
            }

            try {
               var4 = var1.getClass().getDeclaredMethod("removeItemAt", Integer.TYPE);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var10) {
               var10001 = false;
               return;
            }
         }

         int var5;
         try {
            var5 = var1.size() - 1;
         } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var9) {
            var10001 = false;
            return;
         }

         for(; var5 >= 0; --var5) {
            try {
               MenuItem var6 = var1.getItem(var5);
               if (var6.getIntent() != null && "android.intent.action.PROCESS_TEXT".equals(var6.getIntent().getAction())) {
                  var4.invoke(var1, var5);
               }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var8) {
               var10001 = false;
               return;
            }
         }

         List var12 = this.getSupportedActivities(var2, var3);

         for(var5 = 0; var5 < var12.size(); ++var5) {
            ResolveInfo var13 = (ResolveInfo)var12.get(var5);
            var1.add(0, 0, var5 + 100, var13.loadLabel(var3)).setIntent(this.createProcessTextIntentForResolveInfo(var13, this.mTextView)).setShowAsAction(1);
         }

      }

      public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
         return this.mCallback.onActionItemClicked(var1, var2);
      }

      public boolean onCreateActionMode(ActionMode var1, Menu var2) {
         return this.mCallback.onCreateActionMode(var1, var2);
      }

      public void onDestroyActionMode(ActionMode var1) {
         this.mCallback.onDestroyActionMode(var1);
      }

      public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
         this.recomputeProcessTextMenuItems(var2);
         return this.mCallback.onPrepareActionMode(var1, var2);
      }
   }
}
