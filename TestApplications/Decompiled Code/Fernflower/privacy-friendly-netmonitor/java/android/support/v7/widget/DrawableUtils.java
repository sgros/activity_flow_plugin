package android.support.v7.widget;

import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.util.Log;
import java.lang.reflect.Field;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DrawableUtils {
   public static final Rect INSETS_NONE = new Rect();
   private static final String TAG = "DrawableUtils";
   private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
   private static Class sInsetsClazz;

   static {
      if (VERSION.SDK_INT >= 18) {
         try {
            sInsetsClazz = Class.forName("android.graphics.Insets");
         } catch (ClassNotFoundException var1) {
         }
      }

   }

   private DrawableUtils() {
   }

   public static boolean canSafelyMutateDrawable(@NonNull Drawable var0) {
      if (VERSION.SDK_INT < 15 && var0 instanceof InsetDrawable) {
         return false;
      } else if (VERSION.SDK_INT < 15 && var0 instanceof GradientDrawable) {
         return false;
      } else if (VERSION.SDK_INT < 17 && var0 instanceof LayerDrawable) {
         return false;
      } else {
         if (var0 instanceof DrawableContainer) {
            ConstantState var3 = var0.getConstantState();
            if (var3 instanceof DrawableContainerState) {
               Drawable[] var4 = ((DrawableContainerState)var3).getChildren();
               int var1 = var4.length;

               for(int var2 = 0; var2 < var1; ++var2) {
                  if (!canSafelyMutateDrawable(var4[var2])) {
                     return false;
                  }
               }
            }
         } else {
            if (var0 instanceof DrawableWrapper) {
               return canSafelyMutateDrawable(((DrawableWrapper)var0).getWrappedDrawable());
            }

            if (var0 instanceof android.support.v7.graphics.drawable.DrawableWrapper) {
               return canSafelyMutateDrawable(((android.support.v7.graphics.drawable.DrawableWrapper)var0).getWrappedDrawable());
            }

            if (var0 instanceof ScaleDrawable) {
               return canSafelyMutateDrawable(((ScaleDrawable)var0).getDrawable());
            }
         }

         return true;
      }
   }

   static void fixDrawable(@NonNull Drawable var0) {
      if (VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName())) {
         fixVectorDrawableTinting(var0);
      }

   }

   private static void fixVectorDrawableTinting(Drawable var0) {
      int[] var1 = var0.getState();
      if (var1 != null && var1.length != 0) {
         var0.setState(ThemeUtils.EMPTY_STATE_SET);
      } else {
         var0.setState(ThemeUtils.CHECKED_STATE_SET);
      }

      var0.setState(var1);
   }

   public static Rect getOpticalBounds(Drawable var0) {
      if (sInsetsClazz != null) {
         label119: {
            boolean var10001;
            Object var19;
            try {
               var0 = DrawableCompat.unwrap(var0);
               var19 = var0.getClass().getMethod("getOpticalInsets").invoke(var0);
            } catch (Exception var18) {
               var10001 = false;
               break label119;
            }

            if (var19 == null) {
               return INSETS_NONE;
            }

            Rect var1;
            Field[] var2;
            int var3;
            try {
               var1 = new Rect();
               var2 = sInsetsClazz.getFields();
               var3 = var2.length;
            } catch (Exception var17) {
               var10001 = false;
               break label119;
            }

            int var4 = 0;

            label105:
            while(true) {
               if (var4 >= var3) {
                  return var1;
               }

               Field var5 = var2[var4];

               String var6;
               int var7;
               try {
                  var6 = var5.getName();
                  var7 = var6.hashCode();
               } catch (Exception var12) {
                  var10001 = false;
                  break;
               }

               byte var20;
               label102: {
                  label101: {
                     label100: {
                        label124: {
                           if (var7 != -1383228885) {
                              if (var7 != 115029) {
                                 if (var7 != 3317767) {
                                    if (var7 == 108511772) {
                                       label122: {
                                          try {
                                             if (!var6.equals("right")) {
                                                break label122;
                                             }
                                          } catch (Exception var16) {
                                             var10001 = false;
                                             break;
                                          }

                                          var20 = 2;
                                          break label102;
                                       }
                                    }
                                 } else {
                                    try {
                                       if (var6.equals("left")) {
                                          break label101;
                                       }
                                    } catch (Exception var13) {
                                       var10001 = false;
                                       break;
                                    }
                                 }
                              } else {
                                 try {
                                    if (var6.equals("top")) {
                                       break label100;
                                    }
                                 } catch (Exception var14) {
                                    var10001 = false;
                                    break;
                                 }
                              }
                           } else {
                              try {
                                 if (var6.equals("bottom")) {
                                    break label124;
                                 }
                              } catch (Exception var15) {
                                 var10001 = false;
                                 break;
                              }
                           }

                           var20 = -1;
                           break label102;
                        }

                        var20 = 3;
                        break label102;
                     }

                     var20 = 1;
                     break label102;
                  }

                  var20 = 0;
               }

               switch(var20) {
               case 0:
                  try {
                     var1.left = var5.getInt(var19);
                     break;
                  } catch (Exception var8) {
                     var10001 = false;
                     break label105;
                  }
               case 1:
                  try {
                     var1.top = var5.getInt(var19);
                     break;
                  } catch (Exception var9) {
                     var10001 = false;
                     break label105;
                  }
               case 2:
                  try {
                     var1.right = var5.getInt(var19);
                     break;
                  } catch (Exception var10) {
                     var10001 = false;
                     break label105;
                  }
               case 3:
                  try {
                     var1.bottom = var5.getInt(var19);
                  } catch (Exception var11) {
                     var10001 = false;
                     break label105;
                  }
               }

               ++var4;
            }
         }

         Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
      }

      return INSETS_NONE;
   }

   public static Mode parseTintMode(int var0, Mode var1) {
      if (var0 != 3) {
         if (var0 != 5) {
            if (var0 != 9) {
               switch(var0) {
               case 14:
                  return Mode.MULTIPLY;
               case 15:
                  return Mode.SCREEN;
               case 16:
                  if (VERSION.SDK_INT >= 11) {
                     var1 = Mode.valueOf("ADD");
                  }

                  return var1;
               default:
                  return var1;
               }
            } else {
               return Mode.SRC_ATOP;
            }
         } else {
            return Mode.SRC_IN;
         }
      } else {
         return Mode.SRC_OVER;
      }
   }
}
