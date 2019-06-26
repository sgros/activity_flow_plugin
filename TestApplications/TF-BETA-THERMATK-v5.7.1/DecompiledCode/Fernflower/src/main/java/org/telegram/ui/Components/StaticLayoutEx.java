package org.telegram.ui.Components;

import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.StaticLayout.Builder;
import android.text.TextUtils.TruncateAt;
import java.lang.reflect.Constructor;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;

public class StaticLayoutEx {
   private static final String TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics";
   private static final String TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic";
   private static final String TEXT_DIR_FIRSTSTRONG_LTR = "FIRSTSTRONG_LTR";
   private static boolean initialized;
   private static Constructor sConstructor;
   private static Object[] sConstructorArgs;
   private static Object sTextDirection;

   public static StaticLayout createStaticLayout(CharSequence var0, int var1, int var2, TextPaint var3, int var4, Alignment var5, float var6, float var7, boolean var8, TruncateAt var9, int var10, int var11, boolean var12) {
      Exception var27;
      label115: {
         float var13;
         Exception var10000;
         boolean var10001;
         StaticLayout var28;
         if (var11 == 1) {
            label117: {
               var13 = (float)var10;

               try {
                  var0 = TextUtils.ellipsize(var0, var3, var13, TruncateAt.END);
                  var1 = var0.length();
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label117;
               }

               try {
                  var28 = new StaticLayout(var0, 0, var1, var3, var4, var5, var6, var7, var8);
                  return var28;
               } catch (Exception var15) {
                  var27 = var15;
                  break label115;
               }
            }
         } else {
            label113: {
               label118: {
                  StaticLayout var31;
                  label111: {
                     try {
                        if (VERSION.SDK_INT >= 23) {
                           var31 = Builder.obtain(var0, 0, var0.length(), var3, var4).setAlignment(var5).setLineSpacing(var7, var6).setIncludePad(var8).setEllipsize((TruncateAt)null).setEllipsizedWidth(var10).setMaxLines(var11).setBreakStrategy(1).setHyphenationFrequency(0).build();
                           break label111;
                        }
                     } catch (Exception var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label113;
                     }

                     try {
                        var31 = new StaticLayout(var0, var3, var4, var5, var6, var7, var8);
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label118;
                     }
                  }

                  try {
                     if (var31.getLineCount() <= var11) {
                        return var31;
                     }
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label118;
                  }

                  var1 = var11 - 1;

                  float var14;
                  try {
                     var14 = var31.getLineLeft(var1);
                     var13 = var31.getLineWidth(var1);
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label118;
                  }

                  if (var14 != 0.0F) {
                     try {
                        var1 = var31.getOffsetForHorizontal(var1, var14);
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label118;
                     }
                  } else {
                     try {
                        var1 = var31.getOffsetForHorizontal(var1, var13);
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label118;
                     }
                  }

                  var2 = var1;

                  label81: {
                     try {
                        if (var13 >= (float)(var10 - AndroidUtilities.dp(10.0F))) {
                           break label81;
                        }
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label118;
                     }

                     var2 = var1 + 3;
                  }

                  Builder var29;
                  label75: {
                     SpannableStringBuilder var32;
                     try {
                        var32 = new SpannableStringBuilder(var0.subSequence(0, Math.max(0, var2 - 3)));
                        var32.append("…");
                        if (VERSION.SDK_INT >= 23) {
                           var29 = Builder.obtain(var32, 0, var32.length(), var3, var4).setAlignment(var5).setLineSpacing(var7, var6).setIncludePad(var8).setEllipsize(TruncateAt.END).setEllipsizedWidth(var10).setMaxLines(var11);
                           break label75;
                        }
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label118;
                     }

                     try {
                        var28 = new StaticLayout(var32, var3, var4, var5, var6, var7, var8);
                        return var28;
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label118;
                     }
                  }

                  byte var30;
                  if (var12) {
                     var30 = 1;
                  } else {
                     var30 = 0;
                  }

                  try {
                     return var29.setBreakStrategy(var30).setHyphenationFrequency(0).build();
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                  }
               }

               var27 = var10000;
               break label115;
            }
         }

         var27 = var10000;
      }

      FileLog.e((Throwable)var27);
      return null;
   }

   public static StaticLayout createStaticLayout(CharSequence var0, TextPaint var1, int var2, Alignment var3, float var4, float var5, boolean var6, TruncateAt var7, int var8, int var9) {
      return createStaticLayout(var0, 0, var0.length(), var1, var2, var3, var4, var5, var6, var7, var8, var9, true);
   }

   public static StaticLayout createStaticLayout(CharSequence var0, TextPaint var1, int var2, Alignment var3, float var4, float var5, boolean var6, TruncateAt var7, int var8, int var9, boolean var10) {
      return createStaticLayout(var0, 0, var0.length(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static StaticLayout createStaticLayout2(CharSequence var0, TextPaint var1, int var2, Alignment var3, float var4, float var5, boolean var6, TruncateAt var7, int var8, int var9) {
      return VERSION.SDK_INT >= 23 ? Builder.obtain(var0, 0, var0.length(), var1, var8).setAlignment(var3).setLineSpacing(var5, var4).setIncludePad(var6).setEllipsize(TruncateAt.END).setEllipsizedWidth(var8).setMaxLines(var9).setBreakStrategy(1).setHyphenationFrequency(0).build() : createStaticLayout(var0, 0, var0.length(), var1, var2, var3, var4, var5, var6, var7, var8, var9, true);
   }

   public static void init() {
      if (!initialized) {
         Throwable var10000;
         label76: {
            Class var0;
            boolean var10001;
            label77: {
               label69: {
                  try {
                     if (VERSION.SDK_INT >= 18) {
                        break label69;
                     }
                  } catch (Throwable var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label76;
                  }

                  try {
                     ClassLoader var1 = StaticLayoutEx.class.getClassLoader();
                     var0 = var1.loadClass("android.text.TextDirectionHeuristic");
                     Class var11 = var1.loadClass("android.text.TextDirectionHeuristics");
                     sTextDirection = var11.getField("FIRSTSTRONG_LTR").get(var11);
                     break label77;
                  } catch (Throwable var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label76;
                  }
               }

               var0 = TextDirectionHeuristic.class;

               try {
                  sTextDirection = TextDirectionHeuristics.FIRSTSTRONG_LTR;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label76;
               }
            }

            Class[] var12;
            try {
               var12 = new Class[13];
            } catch (Throwable var6) {
               var10000 = var6;
               var10001 = false;
               break label76;
            }

            var12[0] = CharSequence.class;

            try {
               var12[1] = Integer.TYPE;
               var12[2] = Integer.TYPE;
            } catch (Throwable var5) {
               var10000 = var5;
               var10001 = false;
               break label76;
            }

            var12[3] = TextPaint.class;

            try {
               var12[4] = Integer.TYPE;
            } catch (Throwable var4) {
               var10000 = var4;
               var10001 = false;
               break label76;
            }

            var12[5] = Alignment.class;
            var12[6] = var0;

            try {
               var12[7] = Float.TYPE;
               var12[8] = Float.TYPE;
               var12[9] = Boolean.TYPE;
            } catch (Throwable var3) {
               var10000 = var3;
               var10001 = false;
               break label76;
            }

            var12[10] = TruncateAt.class;

            try {
               var12[11] = Integer.TYPE;
               var12[12] = Integer.TYPE;
               sConstructor = StaticLayout.class.getDeclaredConstructor(var12);
               sConstructor.setAccessible(true);
               sConstructorArgs = new Object[var12.length];
               initialized = true;
               return;
            } catch (Throwable var2) {
               var10000 = var2;
               var10001 = false;
            }
         }

         Throwable var10 = var10000;
         FileLog.e(var10);
      }
   }
}
