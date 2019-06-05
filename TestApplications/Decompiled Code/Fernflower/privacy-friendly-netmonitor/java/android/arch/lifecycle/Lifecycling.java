package android.arch.lifecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class Lifecycling {
   private static Map sCallbackCache;
   private static Constructor sREFLECTIVE;

   static {
      try {
         sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(Object.class);
      } catch (NoSuchMethodException var1) {
      }

      sCallbackCache = new HashMap();
   }

   static String getAdapterName(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.replace(".", "_"));
      var1.append("_LifecycleAdapter");
      return var1.toString();
   }

   @NonNull
   static GenericLifecycleObserver getCallback(Object var0) {
      if (var0 instanceof GenericLifecycleObserver) {
         return (GenericLifecycleObserver)var0;
      } else {
         IllegalAccessException var27;
         label83: {
            InstantiationException var26;
            label82: {
               InvocationTargetException var10000;
               label81: {
                  Class var1;
                  Constructor var2;
                  boolean var10001;
                  try {
                     var1 = var0.getClass();
                     var2 = (Constructor)sCallbackCache.get(var1);
                  } catch (IllegalAccessException var19) {
                     var27 = var19;
                     var10001 = false;
                     break label83;
                  } catch (InstantiationException var20) {
                     var26 = var20;
                     var10001 = false;
                     break label82;
                  } catch (InvocationTargetException var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label81;
                  }

                  if (var2 != null) {
                     try {
                        return (GenericLifecycleObserver)var2.newInstance(var0);
                     } catch (IllegalAccessException var4) {
                        var27 = var4;
                        var10001 = false;
                        break label83;
                     } catch (InstantiationException var5) {
                        var26 = var5;
                        var10001 = false;
                        break label82;
                     } catch (InvocationTargetException var6) {
                        var10000 = var6;
                        var10001 = false;
                     }
                  } else {
                     label88: {
                        Constructor var3;
                        try {
                           var3 = getGeneratedAdapterConstructor(var1);
                        } catch (IllegalAccessException var16) {
                           var27 = var16;
                           var10001 = false;
                           break label83;
                        } catch (InstantiationException var17) {
                           var26 = var17;
                           var10001 = false;
                           break label82;
                        } catch (InvocationTargetException var18) {
                           var10000 = var18;
                           var10001 = false;
                           break label88;
                        }

                        if (var3 != null) {
                           label89: {
                              var2 = var3;

                              try {
                                 if (var3.isAccessible()) {
                                    break label89;
                                 }

                                 var3.setAccessible(true);
                              } catch (IllegalAccessException var13) {
                                 var27 = var13;
                                 var10001 = false;
                                 break label83;
                              } catch (InstantiationException var14) {
                                 var26 = var14;
                                 var10001 = false;
                                 break label82;
                              } catch (InvocationTargetException var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label88;
                              }

                              var2 = var3;
                           }
                        } else {
                           try {
                              var2 = sREFLECTIVE;
                           } catch (IllegalAccessException var10) {
                              var27 = var10;
                              var10001 = false;
                              break label83;
                           } catch (InstantiationException var11) {
                              var26 = var11;
                              var10001 = false;
                              break label82;
                           } catch (InvocationTargetException var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label88;
                           }
                        }

                        try {
                           sCallbackCache.put(var1, var2);
                           GenericLifecycleObserver var25 = (GenericLifecycleObserver)var2.newInstance(var0);
                           return var25;
                        } catch (IllegalAccessException var7) {
                           var27 = var7;
                           var10001 = false;
                           break label83;
                        } catch (InstantiationException var8) {
                           var26 = var8;
                           var10001 = false;
                           break label82;
                        } catch (InvocationTargetException var9) {
                           var10000 = var9;
                           var10001 = false;
                        }
                     }
                  }
               }

               InvocationTargetException var22 = var10000;
               throw new RuntimeException(var22);
            }

            InstantiationException var23 = var26;
            throw new RuntimeException(var23);
         }

         IllegalAccessException var24 = var27;
         throw new RuntimeException(var24);
      }
   }

   @Nullable
   private static Constructor getGeneratedAdapterConstructor(Class var0) {
      Package var1 = var0.getPackage();
      String var11;
      if (var1 != null) {
         var11 = var1.getName();
      } else {
         var11 = "";
      }

      String var2 = var0.getCanonicalName();
      if (var2 == null) {
         return null;
      } else {
         if (!var11.isEmpty()) {
            var2 = var2.substring(var11.length() + 1);
         }

         var2 = getAdapterName(var2);

         label53: {
            NoSuchMethodException var10000;
            label52: {
               boolean var10001;
               label51: {
                  label50: {
                     try {
                        if (var11.isEmpty()) {
                           break label50;
                        }
                     } catch (ClassNotFoundException var8) {
                        var10001 = false;
                        break label53;
                     } catch (NoSuchMethodException var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label52;
                     }

                     try {
                        StringBuilder var3 = new StringBuilder();
                        var3.append(var11);
                        var3.append(".");
                        var3.append(var2);
                        var11 = var3.toString();
                        break label51;
                     } catch (ClassNotFoundException var6) {
                        var10001 = false;
                        break label53;
                     } catch (NoSuchMethodException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label52;
                     }
                  }

                  var11 = var2;
               }

               try {
                  Constructor var12 = Class.forName(var11).getDeclaredConstructor(var0);
                  return var12;
               } catch (ClassNotFoundException var4) {
                  var10001 = false;
                  break label53;
               } catch (NoSuchMethodException var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            NoSuchMethodException var10 = var10000;
            throw new RuntimeException(var10);
         }

         var0 = var0.getSuperclass();
         return var0 != null ? getGeneratedAdapterConstructor(var0) : null;
      }
   }
}
