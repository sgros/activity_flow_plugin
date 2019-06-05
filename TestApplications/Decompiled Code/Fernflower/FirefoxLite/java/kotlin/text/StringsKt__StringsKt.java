package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

class StringsKt__StringsKt extends StringsKt__StringsJVMKt {
   public static final boolean contains(CharSequence var0, CharSequence var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "other");
      boolean var3 = var1 instanceof String;
      boolean var4 = false;
      if (var3) {
         if (StringsKt.indexOf$default(var0, (String)var1, 0, var2, 2, (Object)null) < 0) {
            return var4;
         }
      } else if (indexOf$StringsKt__StringsKt$default(var0, var1, 0, var0.length(), var2, false, 16, (Object)null) < 0) {
         return var4;
      }

      var4 = true;
      return var4;
   }

   // $FF: synthetic method
   public static boolean contains$default(CharSequence var0, CharSequence var1, boolean var2, int var3, Object var4) {
      if ((var3 & 2) != 0) {
         var2 = false;
      }

      return StringsKt.contains(var0, var1, var2);
   }

   private static final Pair findAnyOf$StringsKt__StringsKt(CharSequence var0, Collection var1, int var2, boolean var3, boolean var4) {
      IntProgression var5 = null;
      if (!var3 && var1.size() == 1) {
         String var11 = (String)CollectionsKt.single((Iterable)var1);
         if (!var4) {
            var2 = StringsKt.indexOf$default(var0, var11, var2, false, 4, (Object)null);
         } else {
            var2 = StringsKt.lastIndexOf$default(var0, var11, var2, false, 4, (Object)null);
         }

         Pair var10;
         if (var2 < 0) {
            var10 = var5;
         } else {
            var10 = TuplesKt.to(var2, var11);
         }

         return var10;
      } else {
         if (!var4) {
            var5 = (IntProgression)(new IntRange(RangesKt.coerceAtLeast(var2, 0), var0.length()));
         } else {
            var5 = RangesKt.downTo(RangesKt.coerceAtMost(var2, StringsKt.getLastIndex(var0)), 0);
         }

         int var6;
         int var7;
         Object var12;
         String var13;
         if (var0 instanceof String) {
            var2 = var5.getFirst();
            var6 = var5.getLast();
            var7 = var5.getStep();
            if (var7 > 0) {
               if (var2 > var6) {
                  return null;
               }
            } else if (var2 < var6) {
               return null;
            }

            while(true) {
               Iterator var8 = ((Iterable)var1).iterator();

               String var9;
               do {
                  if (!var8.hasNext()) {
                     var12 = null;
                     break;
                  }

                  var12 = var8.next();
                  var9 = (String)var12;
               } while(!StringsKt.regionMatches(var9, 0, (String)var0, var2, var9.length(), var3));

               var13 = (String)var12;
               if (var13 != null) {
                  return TuplesKt.to(var2, var13);
               }

               if (var2 == var6) {
                  break;
               }

               var2 += var7;
            }
         } else {
            var2 = var5.getFirst();
            var7 = var5.getLast();
            var6 = var5.getStep();
            if (var6 > 0) {
               if (var2 > var7) {
                  return null;
               }
            } else if (var2 < var7) {
               return null;
            }

            while(true) {
               Iterator var15 = ((Iterable)var1).iterator();

               String var14;
               do {
                  if (!var15.hasNext()) {
                     var12 = null;
                     break;
                  }

                  var12 = var15.next();
                  var14 = (String)var12;
               } while(!StringsKt.regionMatchesImpl((CharSequence)var14, 0, var0, var2, var14.length(), var3));

               var13 = (String)var12;
               if (var13 != null) {
                  return TuplesKt.to(var2, var13);
               }

               if (var2 == var7) {
                  break;
               }

               var2 += var6;
            }
         }

         return null;
      }
   }

   public static final IntRange getIndices(CharSequence var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return new IntRange(0, var0.length() - 1);
   }

   public static final int getLastIndex(CharSequence var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return var0.length() - 1;
   }

   public static final int indexOf(CharSequence var0, char var1, int var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      if (!var3 && var0 instanceof String) {
         var2 = ((String)var0).indexOf(var1, var2);
      } else {
         var2 = StringsKt.indexOfAny(var0, new char[]{var1}, var2, var3);
      }

      return var2;
   }

   public static final int indexOf(CharSequence var0, String var1, int var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "string");
      if (!var3 && var0 instanceof String) {
         var2 = ((String)var0).indexOf(var1, var2);
      } else {
         var2 = indexOf$StringsKt__StringsKt$default(var0, (CharSequence)var1, var2, var0.length(), var3, false, 16, (Object)null);
      }

      return var2;
   }

   private static final int indexOf$StringsKt__StringsKt(CharSequence var0, CharSequence var1, int var2, int var3, boolean var4, boolean var5) {
      IntProgression var6;
      if (!var5) {
         var6 = (IntProgression)(new IntRange(RangesKt.coerceAtLeast(var2, 0), RangesKt.coerceAtMost(var3, var0.length())));
      } else {
         var6 = RangesKt.downTo(RangesKt.coerceAtMost(var2, StringsKt.getLastIndex(var0)), RangesKt.coerceAtLeast(var3, 0));
      }

      int var7;
      if (var0 instanceof String && var1 instanceof String) {
         var2 = var6.getFirst();
         var3 = var6.getLast();
         var7 = var6.getStep();
         if (var7 > 0) {
            if (var2 > var3) {
               return -1;
            }
         } else if (var2 < var3) {
            return -1;
         }

         while(true) {
            if (StringsKt.regionMatches((String)var1, 0, (String)var0, var2, var1.length(), var4)) {
               return var2;
            }

            if (var2 == var3) {
               break;
            }

            var2 += var7;
         }
      } else {
         var2 = var6.getFirst();
         var3 = var6.getLast();
         var7 = var6.getStep();
         if (var7 > 0) {
            if (var2 > var3) {
               return -1;
            }
         } else if (var2 < var3) {
            return -1;
         }

         while(true) {
            if (StringsKt.regionMatchesImpl(var1, 0, var0, var2, var1.length(), var4)) {
               return var2;
            }

            if (var2 == var3) {
               break;
            }

            var2 += var7;
         }
      }

      return -1;
   }

   // $FF: synthetic method
   static int indexOf$StringsKt__StringsKt$default(CharSequence var0, CharSequence var1, int var2, int var3, boolean var4, boolean var5, int var6, Object var7) {
      if ((var6 & 16) != 0) {
         var5 = false;
      }

      return indexOf$StringsKt__StringsKt(var0, var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public static int indexOf$default(CharSequence var0, char var1, int var2, boolean var3, int var4, Object var5) {
      if ((var4 & 2) != 0) {
         var2 = 0;
      }

      if ((var4 & 4) != 0) {
         var3 = false;
      }

      return StringsKt.indexOf(var0, var1, var2, var3);
   }

   // $FF: synthetic method
   public static int indexOf$default(CharSequence var0, String var1, int var2, boolean var3, int var4, Object var5) {
      if ((var4 & 2) != 0) {
         var2 = 0;
      }

      if ((var4 & 4) != 0) {
         var3 = false;
      }

      return StringsKt.indexOf(var0, var1, var2, var3);
   }

   public static final int indexOfAny(CharSequence var0, char[] var1, int var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "chars");
      if (!var3 && var1.length == 1 && var0 instanceof String) {
         char var9 = ArraysKt.single(var1);
         return ((String)var0).indexOf(var9, var2);
      } else {
         var2 = RangesKt.coerceAtLeast(var2, 0);
         int var5 = StringsKt.getLastIndex(var0);
         if (var2 <= var5) {
            while(true) {
               char var6 = var0.charAt(var2);
               int var7 = var1.length;
               int var4 = 0;

               boolean var8;
               while(true) {
                  if (var4 >= var7) {
                     var8 = false;
                     break;
                  }

                  if (CharsKt.equals(var1[var4], var6, var3)) {
                     var8 = true;
                     break;
                  }

                  ++var4;
               }

               if (var8) {
                  return var2;
               }

               if (var2 == var5) {
                  break;
               }

               ++var2;
            }
         }

         return -1;
      }
   }

   public static final int lastIndexOf(CharSequence var0, String var1, int var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "string");
      if (!var3 && var0 instanceof String) {
         var2 = ((String)var0).lastIndexOf(var1, var2);
      } else {
         var2 = indexOf$StringsKt__StringsKt(var0, (CharSequence)var1, var2, 0, var3, true);
      }

      return var2;
   }

   // $FF: synthetic method
   public static int lastIndexOf$default(CharSequence var0, String var1, int var2, boolean var3, int var4, Object var5) {
      if ((var4 & 2) != 0) {
         var2 = StringsKt.getLastIndex(var0);
      }

      if ((var4 & 4) != 0) {
         var3 = false;
      }

      return StringsKt.lastIndexOf(var0, var1, var2, var3);
   }

   private static final Sequence rangesDelimitedBy$StringsKt__StringsKt(CharSequence var0, String[] var1, int var2, final boolean var3, int var4) {
      boolean var5;
      if (var4 >= 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var5) {
         return (Sequence)(new DelimitedRangesSequence(var0, var2, var4, (Function2)(new Function2(ArraysKt.asList(var1)) {
            // $FF: synthetic field
            final List $delimitersList;

            {
               this.$delimitersList = var1;
            }

            public final Pair invoke(CharSequence var1, int var2) {
               Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
               Pair var3x = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(var1, (Collection)this.$delimitersList, var2, var3, false);
               if (var3x != null) {
                  var3x = TuplesKt.to(var3x.getFirst(), ((String)var3x.getSecond()).length());
               } else {
                  var3x = null;
               }

               return var3x;
            }
         })));
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("Limit must be non-negative, but was ");
         var6.append(var4);
         var6.append('.');
         throw (Throwable)(new IllegalArgumentException(var6.toString().toString()));
      }
   }

   // $FF: synthetic method
   static Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence var0, String[] var1, int var2, boolean var3, int var4, int var5, Object var6) {
      if ((var5 & 2) != 0) {
         var2 = 0;
      }

      if ((var5 & 4) != 0) {
         var3 = false;
      }

      if ((var5 & 8) != 0) {
         var4 = 0;
      }

      return rangesDelimitedBy$StringsKt__StringsKt(var0, var1, var2, var3, var4);
   }

   public static final boolean regionMatchesImpl(CharSequence var0, int var1, CharSequence var2, int var3, int var4, boolean var5) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var2, "other");
      if (var3 >= 0 && var1 >= 0 && var1 <= var0.length() - var4 && var3 <= var2.length() - var4) {
         for(int var6 = 0; var6 < var4; ++var6) {
            if (!CharsKt.equals(var0.charAt(var1 + var6), var2.charAt(var3 + var6), var5)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static final List split(CharSequence var0, String[] var1, boolean var2, int var3) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "delimiters");
      if (var1.length == 1) {
         boolean var4 = false;
         String var5 = var1[0];
         if (((CharSequence)var5).length() == 0) {
            var4 = true;
         }

         if (!var4) {
            return split$StringsKt__StringsKt(var0, var5, var2, var3);
         }
      }

      Iterable var7 = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default(var0, var1, 0, var2, var3, 2, (Object)null));
      Collection var6 = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault(var7, 10)));
      Iterator var8 = var7.iterator();

      while(var8.hasNext()) {
         var6.add(StringsKt.substring(var0, (IntRange)var8.next()));
      }

      return (List)var6;
   }

   private static final List split$StringsKt__StringsKt(CharSequence var0, String var1, boolean var2, int var3) {
      int var4 = 0;
      boolean var5;
      if (var3 >= 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (!var5) {
         StringBuilder var10 = new StringBuilder();
         var10.append("Limit must be non-negative, but was ");
         var10.append(var3);
         var10.append('.');
         throw (Throwable)(new IllegalArgumentException(var10.toString().toString()));
      } else {
         int var6 = StringsKt.indexOf(var0, var1, 0, var2);
         if (var6 != -1 && var3 != 1) {
            if (var3 > 0) {
               var5 = true;
            } else {
               var5 = false;
            }

            int var7 = 10;
            if (var5) {
               var7 = RangesKt.coerceAtMost(var3, 10);
            }

            ArrayList var8 = new ArrayList(var7);
            var7 = var6;

            int var9;
            do {
               var8.add(var0.subSequence(var4, var7).toString());
               var6 = var1.length() + var7;
               if (var5 && var8.size() == var3 - 1) {
                  break;
               }

               var9 = StringsKt.indexOf(var0, var1, var6, var2);
               var4 = var6;
               var7 = var9;
            } while(var9 != -1);

            var8.add(var0.subSequence(var6, var0.length()).toString());
            return (List)var8;
         } else {
            return CollectionsKt.listOf(var0.toString());
         }
      }
   }

   // $FF: synthetic method
   public static List split$default(CharSequence var0, String[] var1, boolean var2, int var3, int var4, Object var5) {
      if ((var4 & 2) != 0) {
         var2 = false;
      }

      if ((var4 & 4) != 0) {
         var3 = 0;
      }

      return StringsKt.split(var0, var1, var2, var3);
   }

   public static final String substring(CharSequence var0, IntRange var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "range");
      return var0.subSequence(var1.getStart(), var1.getEndInclusive() + 1).toString();
   }
}
