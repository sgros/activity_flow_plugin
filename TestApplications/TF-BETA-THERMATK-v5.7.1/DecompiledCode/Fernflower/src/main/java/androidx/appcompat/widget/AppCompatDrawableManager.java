package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$color;
import androidx.appcompat.R$drawable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.LruCache;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class AppCompatDrawableManager {
   private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
   private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
   private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
   private static final AppCompatDrawableManager.ColorFilterLruCache COLOR_FILTER_CACHE;
   private static final Mode DEFAULT_MODE;
   private static AppCompatDrawableManager INSTANCE;
   private static final int[] TINT_CHECKABLE_BUTTON_LIST;
   private static final int[] TINT_COLOR_CONTROL_NORMAL;
   private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
   private ArrayMap mDelegates;
   private final WeakHashMap mDrawableCaches = new WeakHashMap(0);
   private boolean mHasCheckedVectorDrawableSetup;
   private SparseArrayCompat mKnownDrawableIdTags;
   private WeakHashMap mTintLists;
   private TypedValue mTypedValue;

   static {
      DEFAULT_MODE = Mode.SRC_IN;
      COLOR_FILTER_CACHE = new AppCompatDrawableManager.ColorFilterLruCache(6);
      COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{R$drawable.abc_textfield_search_default_mtrl_alpha, R$drawable.abc_textfield_default_mtrl_alpha, R$drawable.abc_ab_share_pack_mtrl_alpha};
      TINT_COLOR_CONTROL_NORMAL = new int[]{R$drawable.abc_ic_commit_search_api_mtrl_alpha, R$drawable.abc_seekbar_tick_mark_material, R$drawable.abc_ic_menu_share_mtrl_alpha, R$drawable.abc_ic_menu_copy_mtrl_am_alpha, R$drawable.abc_ic_menu_cut_mtrl_alpha, R$drawable.abc_ic_menu_selectall_mtrl_alpha, R$drawable.abc_ic_menu_paste_mtrl_am_alpha};
      COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{R$drawable.abc_textfield_activated_mtrl_alpha, R$drawable.abc_textfield_search_activated_mtrl_alpha, R$drawable.abc_cab_background_top_mtrl_alpha, R$drawable.abc_text_cursor_material, R$drawable.abc_text_select_handle_left_mtrl_dark, R$drawable.abc_text_select_handle_middle_mtrl_dark, R$drawable.abc_text_select_handle_right_mtrl_dark, R$drawable.abc_text_select_handle_left_mtrl_light, R$drawable.abc_text_select_handle_middle_mtrl_light, R$drawable.abc_text_select_handle_right_mtrl_light};
      COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{R$drawable.abc_popup_background_mtrl_mult, R$drawable.abc_cab_background_internal_bg, R$drawable.abc_menu_hardkey_panel_mtrl_mult};
      TINT_COLOR_CONTROL_STATE_LIST = new int[]{R$drawable.abc_tab_indicator_material, R$drawable.abc_textfield_search_material};
      TINT_CHECKABLE_BUTTON_LIST = new int[]{R$drawable.abc_btn_check_material, R$drawable.abc_btn_radio_material};
   }

   private void addDelegate(String var1, AppCompatDrawableManager.InflateDelegate var2) {
      if (this.mDelegates == null) {
         this.mDelegates = new ArrayMap();
      }

      this.mDelegates.put(var1, var2);
   }

   private boolean addDrawableToCache(Context var1, long var2, Drawable var4) {
      synchronized(this){}

      Throwable var10000;
      label190: {
         ConstantState var5;
         boolean var10001;
         try {
            var5 = var4.getConstantState();
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label190;
         }

         if (var5 == null) {
            return false;
         }

         LongSparseArray var6;
         try {
            var6 = (LongSparseArray)this.mDrawableCaches.get(var1);
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label190;
         }

         LongSparseArray var29 = var6;
         if (var6 == null) {
            try {
               var29 = new LongSparseArray();
               this.mDrawableCaches.put(var1, var29);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label190;
            }
         }

         try {
            WeakReference var28 = new WeakReference(var5);
            var29.put(var2, var28);
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label190;
         }

         return true;
      }

      Throwable var27 = var10000;
      throw var27;
   }

   private void addTintListToCache(Context var1, int var2, ColorStateList var3) {
      if (this.mTintLists == null) {
         this.mTintLists = new WeakHashMap();
      }

      SparseArrayCompat var4 = (SparseArrayCompat)this.mTintLists.get(var1);
      SparseArrayCompat var5 = var4;
      if (var4 == null) {
         var5 = new SparseArrayCompat();
         this.mTintLists.put(var1, var5);
      }

      var5.append(var2, var3);
   }

   private static boolean arrayContains(int[] var0, int var1) {
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0[var3] == var1) {
            return true;
         }
      }

      return false;
   }

   private void checkVectorDrawableSetup(Context var1) {
      if (!this.mHasCheckedVectorDrawableSetup) {
         this.mHasCheckedVectorDrawableSetup = true;
         Drawable var2 = this.getDrawable(var1, R$drawable.abc_vector_test);
         if (var2 == null || !isVectorDrawable(var2)) {
            this.mHasCheckedVectorDrawableSetup = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
         }
      }
   }

   private ColorStateList createBorderlessButtonColorStateList(Context var1) {
      return this.createButtonColorStateList(var1, 0);
   }

   private ColorStateList createButtonColorStateList(Context var1, int var2) {
      int var3 = ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlHighlight);
      int var4 = ThemeUtils.getDisabledThemeAttrColor(var1, R$attr.colorButtonNormal);
      int[] var8 = ThemeUtils.DISABLED_STATE_SET;
      int[] var5 = ThemeUtils.PRESSED_STATE_SET;
      int var6 = ColorUtils.compositeColors(var3, var2);
      int[] var7 = ThemeUtils.FOCUSED_STATE_SET;
      var3 = ColorUtils.compositeColors(var3, var2);
      return new ColorStateList(new int[][]{var8, var5, var7, ThemeUtils.EMPTY_STATE_SET}, new int[]{var4, var6, var3, var2});
   }

   private static long createCacheKey(TypedValue var0) {
      return (long)var0.assetCookie << 32 | (long)var0.data;
   }

   private ColorStateList createColoredButtonColorStateList(Context var1) {
      return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, R$attr.colorAccent));
   }

   private ColorStateList createDefaultButtonColorStateList(Context var1) {
      return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, R$attr.colorButtonNormal));
   }

   private Drawable createDrawableIfNeeded(Context var1, int var2) {
      if (this.mTypedValue == null) {
         this.mTypedValue = new TypedValue();
      }

      TypedValue var3 = this.mTypedValue;
      var1.getResources().getValue(var2, var3, true);
      long var4 = createCacheKey(var3);
      Object var6 = this.getCachedDrawable(var1, var4);
      if (var6 != null) {
         return (Drawable)var6;
      } else {
         if (var2 == R$drawable.abc_cab_background_top_material) {
            var6 = new LayerDrawable(new Drawable[]{this.getDrawable(var1, R$drawable.abc_cab_background_internal_bg), this.getDrawable(var1, R$drawable.abc_cab_background_top_mtrl_alpha)});
         }

         if (var6 != null) {
            ((Drawable)var6).setChangingConfigurations(var3.changingConfigurations);
            this.addDrawableToCache(var1, var4, (Drawable)var6);
         }

         return (Drawable)var6;
      }
   }

   private ColorStateList createSwitchThumbColorStateList(Context var1) {
      int[][] var2 = new int[3][];
      int[] var3 = new int[3];
      ColorStateList var4 = ThemeUtils.getThemeAttrColorStateList(var1, R$attr.colorSwitchThumbNormal);
      if (var4 != null && var4.isStateful()) {
         var2[0] = ThemeUtils.DISABLED_STATE_SET;
         var3[0] = var4.getColorForState(var2[0], 0);
         var2[1] = ThemeUtils.CHECKED_STATE_SET;
         var3[1] = ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated);
         var2[2] = ThemeUtils.EMPTY_STATE_SET;
         var3[2] = var4.getDefaultColor();
      } else {
         var2[0] = ThemeUtils.DISABLED_STATE_SET;
         var3[0] = ThemeUtils.getDisabledThemeAttrColor(var1, R$attr.colorSwitchThumbNormal);
         var2[1] = ThemeUtils.CHECKED_STATE_SET;
         var3[1] = ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated);
         var2[2] = ThemeUtils.EMPTY_STATE_SET;
         var3[2] = ThemeUtils.getThemeAttrColor(var1, R$attr.colorSwitchThumbNormal);
      }

      return new ColorStateList(var2, var3);
   }

   private static PorterDuffColorFilter createTintFilter(ColorStateList var0, Mode var1, int[] var2) {
      return var0 != null && var1 != null ? getPorterDuffColorFilter(var0.getColorForState(var2, 0), var1) : null;
   }

   public static AppCompatDrawableManager get() {
      synchronized(AppCompatDrawableManager.class){}

      AppCompatDrawableManager var0;
      try {
         if (INSTANCE == null) {
            var0 = new AppCompatDrawableManager();
            INSTANCE = var0;
            installDefaultInflateDelegates(INSTANCE);
         }

         var0 = INSTANCE;
      } finally {
         ;
      }

      return var0;
   }

   private Drawable getCachedDrawable(Context var1, long var2) {
      synchronized(this){}

      Throwable var10000;
      label269: {
         LongSparseArray var4;
         boolean var10001;
         try {
            var4 = (LongSparseArray)this.mDrawableCaches.get(var1);
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label269;
         }

         if (var4 == null) {
            return null;
         }

         WeakReference var5;
         try {
            var5 = (WeakReference)var4.get(var2);
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label269;
         }

         if (var5 != null) {
            ConstantState var38;
            try {
               var38 = (ConstantState)var5.get();
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label269;
            }

            if (var38 != null) {
               Drawable var37;
               try {
                  var37 = var38.newDrawable(var1.getResources());
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label269;
               }

               return var37;
            }

            try {
               var4.delete(var2);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label269;
            }
         }

         return null;
      }

      Throwable var36 = var10000;
      throw var36;
   }

   public static PorterDuffColorFilter getPorterDuffColorFilter(int var0, Mode var1) {
      synchronized(AppCompatDrawableManager.class){}

      PorterDuffColorFilter var3;
      label71: {
         Throwable var10000;
         label75: {
            boolean var10001;
            PorterDuffColorFilter var2;
            try {
               var2 = COLOR_FILTER_CACHE.get(var0, var1);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label75;
            }

            var3 = var2;
            if (var2 != null) {
               break label71;
            }

            label66:
            try {
               var3 = new PorterDuffColorFilter(var0, var1);
               COLOR_FILTER_CACHE.put(var0, var1, var3);
               break label71;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label66;
            }
         }

         Throwable var10 = var10000;
         throw var10;
      }

      return var3;
   }

   private ColorStateList getTintListFromCache(Context var1, int var2) {
      WeakHashMap var3 = this.mTintLists;
      Object var4 = null;
      ColorStateList var5 = (ColorStateList)var4;
      if (var3 != null) {
         SparseArrayCompat var6 = (SparseArrayCompat)var3.get(var1);
         var5 = (ColorStateList)var4;
         if (var6 != null) {
            var5 = (ColorStateList)var6.get(var2);
         }
      }

      return var5;
   }

   static Mode getTintMode(int var0) {
      Mode var1;
      if (var0 == R$drawable.abc_switch_thumb_material) {
         var1 = Mode.MULTIPLY;
      } else {
         var1 = null;
      }

      return var1;
   }

   private static void installDefaultInflateDelegates(AppCompatDrawableManager var0) {
      if (VERSION.SDK_INT < 24) {
         var0.addDelegate("vector", new AppCompatDrawableManager.VdcInflateDelegate());
         var0.addDelegate("animated-vector", new AppCompatDrawableManager.AvdcInflateDelegate());
         var0.addDelegate("animated-selector", new AppCompatDrawableManager.AsldcInflateDelegate());
      }

   }

   private static boolean isVectorDrawable(Drawable var0) {
      boolean var1;
      if (!(var0 instanceof VectorDrawableCompat) && !"android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private Drawable loadDrawableFromDelegates(Context var1, int var2) {
      ArrayMap var3 = this.mDelegates;
      if (var3 != null && !var3.isEmpty()) {
         SparseArrayCompat var27 = this.mKnownDrawableIdTags;
         String var28;
         if (var27 != null) {
            var28 = (String)var27.get(var2);
            if ("appcompat_skip_skip".equals(var28) || var28 != null && this.mDelegates.get(var28) == null) {
               return null;
            }
         } else {
            this.mKnownDrawableIdTags = new SparseArrayCompat();
         }

         if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
         }

         TypedValue var4 = this.mTypedValue;
         Resources var29 = var1.getResources();
         var29.getValue(var2, var4, true);
         long var5 = createCacheKey(var4);
         Drawable var7 = this.getCachedDrawable(var1, var5);
         if (var7 != null) {
            return var7;
         } else {
            CharSequence var8 = var4.string;
            Drawable var9 = var7;
            if (var8 != null) {
               var9 = var7;
               if (var8.toString().endsWith(".xml")) {
                  label161: {
                     var9 = var7;

                     Exception var10000;
                     label162: {
                        boolean var10001;
                        XmlResourceParser var31;
                        try {
                           var31 = var29.getXml(var2);
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break label162;
                        }

                        var9 = var7;

                        AttributeSet var10;
                        try {
                           var10 = Xml.asAttributeSet(var31);
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label162;
                        }

                        int var11;
                        do {
                           var9 = var7;

                           try {
                              var11 = var31.next();
                           } catch (Exception var22) {
                              var10000 = var22;
                              var10001 = false;
                              break label162;
                           }
                        } while(var11 != 2 && var11 != 1);

                        if (var11 == 2) {
                           label164: {
                              var9 = var7;

                              try {
                                 var28 = var31.getName();
                              } catch (Exception var21) {
                                 var10000 = var21;
                                 var10001 = false;
                                 break label164;
                              }

                              var9 = var7;

                              try {
                                 this.mKnownDrawableIdTags.append(var2, var28);
                              } catch (Exception var20) {
                                 var10000 = var20;
                                 var10001 = false;
                                 break label164;
                              }

                              var9 = var7;

                              AppCompatDrawableManager.InflateDelegate var12;
                              try {
                                 var12 = (AppCompatDrawableManager.InflateDelegate)this.mDelegates.get(var28);
                              } catch (Exception var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break label164;
                              }

                              Drawable var30 = var7;
                              if (var12 != null) {
                                 var9 = var7;

                                 try {
                                    var30 = var12.createFromXmlInner(var1, var31, var10, var1.getTheme());
                                 } catch (Exception var18) {
                                    var10000 = var18;
                                    var10001 = false;
                                    break label164;
                                 }
                              }

                              var9 = var30;
                              if (var30 == null) {
                                 break label161;
                              }

                              var9 = var30;

                              try {
                                 var30.setChangingConfigurations(var4.changingConfigurations);
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label164;
                              }

                              var9 = var30;

                              try {
                                 this.addDrawableToCache(var1, var5, var30);
                              } catch (Exception var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break label164;
                              }

                              var9 = var30;
                              break label161;
                           }
                        } else {
                           label165: {
                              var9 = var7;

                              XmlPullParserException var25;
                              try {
                                 var25 = new XmlPullParserException;
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label165;
                              }

                              var9 = var7;

                              try {
                                 var25.<init>("No start tag found");
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break label165;
                              }

                              var9 = var7;

                              try {
                                 throw var25;
                              } catch (Exception var13) {
                                 var10000 = var13;
                                 var10001 = false;
                              }
                           }
                        }
                     }

                     Exception var26 = var10000;
                     Log.e("AppCompatDrawableManag", "Exception while inflating drawable", var26);
                  }
               }
            }

            if (var9 == null) {
               this.mKnownDrawableIdTags.append(var2, "appcompat_skip_skip");
            }

            return var9;
         }
      } else {
         return null;
      }
   }

   private static void setPorterDuffColorFilter(Drawable var0, int var1, Mode var2) {
      Drawable var3 = var0;
      if (DrawableUtils.canSafelyMutateDrawable(var0)) {
         var3 = var0.mutate();
      }

      Mode var4 = var2;
      if (var2 == null) {
         var4 = DEFAULT_MODE;
      }

      var3.setColorFilter(getPorterDuffColorFilter(var1, var4));
   }

   private Drawable tintDrawable(Context var1, int var2, boolean var3, Drawable var4) {
      ColorStateList var5 = this.getTintList(var1, var2);
      Drawable var8;
      if (var5 != null) {
         Drawable var6 = var4;
         if (DrawableUtils.canSafelyMutateDrawable(var4)) {
            var6 = var4.mutate();
         }

         var6 = DrawableCompat.wrap(var6);
         DrawableCompat.setTintList(var6, var5);
         Mode var7 = getTintMode(var2);
         var8 = var6;
         if (var7 != null) {
            DrawableCompat.setTintMode(var6, var7);
            var8 = var6;
         }
      } else {
         LayerDrawable var9;
         if (var2 == R$drawable.abc_seekbar_track_material) {
            var9 = (LayerDrawable)var4;
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated), DEFAULT_MODE);
            var8 = var4;
         } else if (var2 != R$drawable.abc_ratingbar_material && var2 != R$drawable.abc_ratingbar_indicator_material && var2 != R$drawable.abc_ratingbar_small_material) {
            var8 = var4;
            if (!tintDrawableUsingColorFilter(var1, var2, var4)) {
               var8 = var4;
               if (var3) {
                  var8 = null;
               }
            }
         } else {
            var9 = (LayerDrawable)var4;
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(var1, R$attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated), DEFAULT_MODE);
            setPorterDuffColorFilter(var9.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated), DEFAULT_MODE);
            var8 = var4;
         }
      }

      return var8;
   }

   static void tintDrawable(Drawable var0, TintInfo var1, int[] var2) {
      if (DrawableUtils.canSafelyMutateDrawable(var0) && var0.mutate() != var0) {
         Log.d("AppCompatDrawableManag", "Mutated drawable is not the same instance as the input.");
      } else {
         if (!var1.mHasTintList && !var1.mHasTintMode) {
            var0.clearColorFilter();
         } else {
            ColorStateList var3;
            if (var1.mHasTintList) {
               var3 = var1.mTintList;
            } else {
               var3 = null;
            }

            Mode var4;
            if (var1.mHasTintMode) {
               var4 = var1.mTintMode;
            } else {
               var4 = DEFAULT_MODE;
            }

            var0.setColorFilter(createTintFilter(var3, var4, var2));
         }

         if (VERSION.SDK_INT <= 23) {
            var0.invalidateSelf();
         }

      }
   }

   static boolean tintDrawableUsingColorFilter(Context var0, int var1, Drawable var2) {
      Mode var3;
      int var5;
      boolean var6;
      label37: {
         var3 = DEFAULT_MODE;
         boolean var4 = arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, var1);
         var5 = 16842801;
         if (var4) {
            var1 = R$attr.colorControlNormal;
         } else if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, var1)) {
            var1 = R$attr.colorControlActivated;
         } else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, var1)) {
            var3 = Mode.MULTIPLY;
            var1 = var5;
         } else {
            if (var1 == R$drawable.abc_list_divider_mtrl_alpha) {
               var1 = 16842800;
               var5 = Math.round(40.8F);
               var6 = true;
               break label37;
            }

            if (var1 != R$drawable.abc_dialog_material_background) {
               var6 = false;
               var5 = -1;
               var1 = 0;
               break label37;
            }

            var1 = var5;
         }

         var6 = true;
         var5 = -1;
      }

      if (var6) {
         Drawable var7 = var2;
         if (DrawableUtils.canSafelyMutateDrawable(var2)) {
            var7 = var2.mutate();
         }

         var7.setColorFilter(getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(var0, var1), var3));
         if (var5 != -1) {
            var7.setAlpha(var5);
         }

         return true;
      } else {
         return false;
      }
   }

   public Drawable getDrawable(Context var1, int var2) {
      synchronized(this){}

      Drawable var5;
      try {
         var5 = this.getDrawable(var1, var2, false);
      } finally {
         ;
      }

      return var5;
   }

   Drawable getDrawable(Context var1, int var2, boolean var3) {
      synchronized(this){}

      Throwable var10000;
      label276: {
         Drawable var4;
         boolean var10001;
         try {
            this.checkVectorDrawableSetup(var1);
            var4 = this.loadDrawableFromDelegates(var1, var2);
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label276;
         }

         Drawable var5 = var4;
         if (var4 == null) {
            try {
               var5 = this.createDrawableIfNeeded(var1, var2);
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label276;
            }
         }

         var4 = var5;
         if (var5 == null) {
            try {
               var4 = ContextCompat.getDrawable(var1, var2);
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label276;
            }
         }

         var5 = var4;
         if (var4 != null) {
            try {
               var5 = this.tintDrawable(var1, var2, var3, var4);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label276;
            }
         }

         if (var5 == null) {
            return var5;
         }

         label255:
         try {
            DrawableUtils.fixDrawable(var5);
            return var5;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label255;
         }
      }

      Throwable var36 = var10000;
      throw var36;
   }

   ColorStateList getTintList(Context var1, int var2) {
      synchronized(this){}

      ColorStateList var3;
      ColorStateList var4;
      label2070: {
         Throwable var10000;
         label2075: {
            boolean var10001;
            try {
               var3 = this.getTintListFromCache(var1, var2);
            } catch (Throwable var214) {
               var10000 = var214;
               var10001 = false;
               break label2075;
            }

            var4 = var3;
            if (var3 != null) {
               return var4;
            }

            label2076: {
               try {
                  if (var2 == R$drawable.abc_edit_text_material) {
                     var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_edittext);
                     break label2076;
                  }
               } catch (Throwable var213) {
                  var10000 = var213;
                  var10001 = false;
                  break label2075;
               }

               try {
                  if (var2 == R$drawable.abc_switch_track_mtrl_alpha) {
                     var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_switch_track);
                     break label2076;
                  }
               } catch (Throwable var212) {
                  var10000 = var212;
                  var10001 = false;
                  break label2075;
               }

               try {
                  if (var2 == R$drawable.abc_switch_thumb_material) {
                     var3 = this.createSwitchThumbColorStateList(var1);
                     break label2076;
                  }
               } catch (Throwable var211) {
                  var10000 = var211;
                  var10001 = false;
                  break label2075;
               }

               try {
                  if (var2 == R$drawable.abc_btn_default_mtrl_shape) {
                     var3 = this.createDefaultButtonColorStateList(var1);
                     break label2076;
                  }
               } catch (Throwable var210) {
                  var10000 = var210;
                  var10001 = false;
                  break label2075;
               }

               try {
                  if (var2 == R$drawable.abc_btn_borderless_material) {
                     var3 = this.createBorderlessButtonColorStateList(var1);
                     break label2076;
                  }
               } catch (Throwable var209) {
                  var10000 = var209;
                  var10001 = false;
                  break label2075;
               }

               try {
                  if (var2 == R$drawable.abc_btn_colored_material) {
                     var3 = this.createColoredButtonColorStateList(var1);
                     break label2076;
                  }
               } catch (Throwable var208) {
                  var10000 = var208;
                  var10001 = false;
                  break label2075;
               }

               label2078: {
                  label2025:
                  try {
                     if (var2 != R$drawable.abc_spinner_mtrl_am_alpha && var2 != R$drawable.abc_spinner_textfield_background_material) {
                        break label2025;
                     }
                     break label2078;
                  } catch (Throwable var207) {
                     var10000 = var207;
                     var10001 = false;
                     break label2075;
                  }

                  try {
                     if (arrayContains(TINT_COLOR_CONTROL_NORMAL, var2)) {
                        var3 = ThemeUtils.getThemeAttrColorStateList(var1, R$attr.colorControlNormal);
                        break label2076;
                     }
                  } catch (Throwable var206) {
                     var10000 = var206;
                     var10001 = false;
                     break label2075;
                  }

                  try {
                     if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, var2)) {
                        var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_default);
                        break label2076;
                     }
                  } catch (Throwable var205) {
                     var10000 = var205;
                     var10001 = false;
                     break label2075;
                  }

                  try {
                     if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, var2)) {
                        var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_btn_checkable);
                        break label2076;
                     }
                  } catch (Throwable var204) {
                     var10000 = var204;
                     var10001 = false;
                     break label2075;
                  }

                  try {
                     if (var2 == R$drawable.abc_seekbar_thumb_material) {
                        var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_seek_thumb);
                     }
                     break label2076;
                  } catch (Throwable var202) {
                     var10000 = var202;
                     var10001 = false;
                     break label2075;
                  }
               }

               try {
                  var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_spinner);
               } catch (Throwable var203) {
                  var10000 = var203;
                  var10001 = false;
                  break label2075;
               }
            }

            var4 = var3;
            if (var3 == null) {
               return var4;
            }

            label1996:
            try {
               this.addTintListToCache(var1, var2, var3);
               break label2070;
            } catch (Throwable var201) {
               var10000 = var201;
               var10001 = false;
               break label1996;
            }
         }

         Throwable var215 = var10000;
         throw var215;
      }

      var4 = var3;
      return var4;
   }

   Drawable onDrawableLoadedFromResources(Context var1, VectorEnabledTintResources var2, int var3) {
      synchronized(this){}

      Throwable var10000;
      label132: {
         Drawable var4;
         boolean var10001;
         try {
            var4 = this.loadDrawableFromDelegates(var1, var3);
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label132;
         }

         Drawable var5 = var4;
         if (var4 == null) {
            try {
               var5 = var2.superGetDrawable(var3);
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label132;
            }
         }

         if (var5 == null) {
            return null;
         }

         Drawable var19;
         try {
            var19 = this.tintDrawable(var1, var3, false, var5);
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label132;
         }

         return var19;
      }

      Throwable var18 = var10000;
      throw var18;
   }

   static class AsldcInflateDelegate implements AppCompatDrawableManager.InflateDelegate {
      public Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4) {
         try {
            AnimatedStateListDrawableCompat var6 = AnimatedStateListDrawableCompat.createFromXmlInner(var1, var1.getResources(), var2, var3, var4);
            return var6;
         } catch (Exception var5) {
            Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", var5);
            return null;
         }
      }
   }

   private static class AvdcInflateDelegate implements AppCompatDrawableManager.InflateDelegate {
      AvdcInflateDelegate() {
      }

      public Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4) {
         try {
            AnimatedVectorDrawableCompat var6 = AnimatedVectorDrawableCompat.createFromXmlInner(var1, var1.getResources(), var2, var3, var4);
            return var6;
         } catch (Exception var5) {
            Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", var5);
            return null;
         }
      }
   }

   private static class ColorFilterLruCache extends LruCache {
      public ColorFilterLruCache(int var1) {
         super(var1);
      }

      private static int generateCacheKey(int var0, Mode var1) {
         return (var0 + 31) * 31 + var1.hashCode();
      }

      PorterDuffColorFilter get(int var1, Mode var2) {
         return (PorterDuffColorFilter)this.get(generateCacheKey(var1, var2));
      }

      PorterDuffColorFilter put(int var1, Mode var2, PorterDuffColorFilter var3) {
         return (PorterDuffColorFilter)this.put(generateCacheKey(var1, var2), var3);
      }
   }

   private interface InflateDelegate {
      Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4);
   }

   private static class VdcInflateDelegate implements AppCompatDrawableManager.InflateDelegate {
      VdcInflateDelegate() {
      }

      public Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4) {
         try {
            VectorDrawableCompat var6 = VectorDrawableCompat.createFromXmlInner(var1.getResources(), var2, var3, var4);
            return var6;
         } catch (Exception var5) {
            Log.e("VdcInflateDelegate", "Exception while inflating <vector>", var5);
            return null;
         }
      }
   }
}
