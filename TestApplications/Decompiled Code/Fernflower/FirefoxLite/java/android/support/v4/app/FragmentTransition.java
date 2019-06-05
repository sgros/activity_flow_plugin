package android.support.v4.app;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition {
   private static final int[] INVERSE_OPS = new int[]{0, 3, 0, 1, 5, 4, 7, 6, 9, 8};
   private static final FragmentTransitionImpl PLATFORM_IMPL;
   private static final FragmentTransitionImpl SUPPORT_IMPL;

   static {
      FragmentTransitionCompat21 var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = new FragmentTransitionCompat21();
      } else {
         var0 = null;
      }

      PLATFORM_IMPL = var0;
      SUPPORT_IMPL = resolveSupportImpl();
   }

   private static void addSharedElementsWithMatchingNames(ArrayList var0, ArrayMap var1, Collection var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         View var4 = (View)var1.valueAt(var3);
         if (var2.contains(ViewCompat.getTransitionName(var4))) {
            var0.add(var4);
         }
      }

   }

   private static void addToFirstInLastOut(BackStackRecord var0, BackStackRecord.Op var1, SparseArray var2, boolean var3, boolean var4) {
      Fragment var5 = var1.fragment;
      if (var5 != null) {
         int var6 = var5.mContainerId;
         if (var6 != 0) {
            int var7;
            if (var3) {
               var7 = INVERSE_OPS[var1.cmd];
            } else {
               var7 = var1.cmd;
            }

            boolean var9;
            boolean var10;
            boolean var11;
            boolean var14;
            label152: {
               boolean var8;
               label151: {
                  label167: {
                     label148: {
                        label168: {
                           label169: {
                              label143: {
                                 label159: {
                                    var8 = false;
                                    var9 = false;
                                    if (var7 != 1) {
                                       switch(var7) {
                                       case 3:
                                       case 6:
                                          if (var4) {
                                             if (var5.mAdded || var5.mView == null || var5.mView.getVisibility() != 0 || var5.mPostponedAlpha < 0.0F) {
                                                break label148;
                                             }
                                          } else if (!var5.mAdded || var5.mHidden) {
                                             break label148;
                                          }
                                          break label169;
                                       case 4:
                                          if (var4) {
                                             if (!var5.mHiddenChanged || !var5.mAdded || !var5.mHidden) {
                                                break label148;
                                             }
                                          } else if (!var5.mAdded || var5.mHidden) {
                                             break label148;
                                          }
                                          break label169;
                                       case 5:
                                          if (!var4) {
                                             var9 = var5.mHidden;
                                             break label168;
                                          }

                                          if (var5.mHiddenChanged && !var5.mHidden && var5.mAdded) {
                                             break label143;
                                          }
                                          break label159;
                                       case 7:
                                          break;
                                       default:
                                          var14 = false;
                                          break label167;
                                       }
                                    }

                                    if (var4) {
                                       var9 = var5.mIsNewlyAdded;
                                       break label168;
                                    }

                                    if (!var5.mAdded && !var5.mHidden) {
                                       break label143;
                                    }
                                 }

                                 var9 = false;
                                 break label168;
                              }

                              var9 = true;
                              break label168;
                           }

                           var14 = true;
                           break label151;
                        }

                        var14 = true;
                        break label167;
                     }

                     var14 = false;
                     break label151;
                  }

                  var10 = false;
                  var11 = false;
                  break label152;
               }

               var11 = var14;
               var14 = false;
               var10 = true;
               var9 = var8;
            }

            FragmentTransition.FragmentContainerTransition var12 = (FragmentTransition.FragmentContainerTransition)var2.get(var6);
            FragmentTransition.FragmentContainerTransition var13 = var12;
            if (var9) {
               var13 = ensureContainer(var12, var2, var6);
               var13.lastIn = var5;
               var13.lastInIsPop = var3;
               var13.lastInTransaction = var0;
            }

            if (!var4 && var14) {
               if (var13 != null && var13.firstOut == var5) {
                  var13.firstOut = null;
               }

               FragmentManagerImpl var15 = var0.mManager;
               if (var5.mState < 1 && var15.mCurState >= 1 && !var0.mReorderingAllowed) {
                  var15.makeActive(var5);
                  var15.moveToState(var5, 1, 0, 0, false);
               }
            }

            var12 = var13;
            if (var11) {
               label86: {
                  if (var13 != null) {
                     var12 = var13;
                     if (var13.firstOut != null) {
                        break label86;
                     }
                  }

                  var12 = ensureContainer(var13, var2, var6);
                  var12.firstOut = var5;
                  var12.firstOutIsPop = var3;
                  var12.firstOutTransaction = var0;
               }
            }

            if (!var4 && var10 && var12 != null && var12.lastIn == var5) {
               var12.lastIn = null;
            }

         }
      }
   }

   public static void calculateFragments(BackStackRecord var0, SparseArray var1, boolean var2) {
      int var3 = var0.mOps.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         addToFirstInLastOut(var0, (BackStackRecord.Op)var0.mOps.get(var4), var1, false, var2);
      }

   }

   private static ArrayMap calculateNameOverrides(int var0, ArrayList var1, ArrayList var2, int var3, int var4) {
      ArrayMap var5 = new ArrayMap();
      --var4;

      for(; var4 >= var3; --var4) {
         BackStackRecord var6 = (BackStackRecord)var1.get(var4);
         if (var6.interactsWith(var0)) {
            boolean var7 = (Boolean)var2.get(var4);
            if (var6.mSharedElementSourceNames != null) {
               int var8 = var6.mSharedElementSourceNames.size();
               ArrayList var9;
               ArrayList var10;
               if (var7) {
                  var9 = var6.mSharedElementSourceNames;
                  var10 = var6.mSharedElementTargetNames;
               } else {
                  var10 = var6.mSharedElementSourceNames;
                  var9 = var6.mSharedElementTargetNames;
               }

               for(int var11 = 0; var11 < var8; ++var11) {
                  String var12 = (String)var10.get(var11);
                  String var13 = (String)var9.get(var11);
                  String var14 = (String)var5.remove(var13);
                  if (var14 != null) {
                     var5.put(var12, var14);
                  } else {
                     var5.put(var12, var13);
                  }
               }
            }
         }
      }

      return var5;
   }

   public static void calculatePopFragments(BackStackRecord var0, SparseArray var1, boolean var2) {
      if (var0.mManager.mContainer.onHasView()) {
         for(int var3 = var0.mOps.size() - 1; var3 >= 0; --var3) {
            addToFirstInLastOut(var0, (BackStackRecord.Op)var0.mOps.get(var3), var1, true, var2);
         }

      }
   }

   static void callSharedElementStartEnd(Fragment var0, Fragment var1, boolean var2, ArrayMap var3, boolean var4) {
      SharedElementCallback var8;
      if (var2) {
         var8 = var1.getEnterTransitionCallback();
      } else {
         var8 = var0.getEnterTransitionCallback();
      }

      if (var8 != null) {
         ArrayList var9 = new ArrayList();
         ArrayList var5 = new ArrayList();
         int var6 = 0;
         int var7;
         if (var3 == null) {
            var7 = 0;
         } else {
            var7 = var3.size();
         }

         while(var6 < var7) {
            var5.add(var3.keyAt(var6));
            var9.add(var3.valueAt(var6));
            ++var6;
         }

         if (var4) {
            var8.onSharedElementStart(var5, var9, (List)null);
         } else {
            var8.onSharedElementEnd(var5, var9, (List)null);
         }
      }

   }

   private static boolean canHandleAll(FragmentTransitionImpl var0, List var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (!var0.canHandle(var1.get(var3))) {
            return false;
         }
      }

      return true;
   }

   static ArrayMap captureInSharedElements(FragmentTransitionImpl var0, ArrayMap var1, Object var2, FragmentTransition.FragmentContainerTransition var3) {
      Fragment var4 = var3.lastIn;
      View var5 = var4.getView();
      if (!var1.isEmpty() && var2 != null && var5 != null) {
         ArrayMap var6 = new ArrayMap();
         var0.findNamedViews(var6, var5);
         BackStackRecord var8 = var3.lastInTransaction;
         ArrayList var9;
         SharedElementCallback var10;
         if (var3.lastInIsPop) {
            var10 = var4.getExitTransitionCallback();
            var9 = var8.mSharedElementSourceNames;
         } else {
            var10 = var4.getEnterTransitionCallback();
            var9 = var8.mSharedElementTargetNames;
         }

         if (var9 != null) {
            var6.retainAll(var9);
            var6.retainAll(var1.values());
         }

         if (var10 != null) {
            var10.onMapSharedElements(var9, var6);

            for(int var7 = var9.size() - 1; var7 >= 0; --var7) {
               String var11 = (String)var9.get(var7);
               View var12 = (View)var6.get(var11);
               if (var12 == null) {
                  String var13 = findKeyForValue(var1, var11);
                  if (var13 != null) {
                     var1.remove(var13);
                  }
               } else if (!var11.equals(ViewCompat.getTransitionName(var12))) {
                  var11 = findKeyForValue(var1, var11);
                  if (var11 != null) {
                     var1.put(var11, ViewCompat.getTransitionName(var12));
                  }
               }
            }
         } else {
            retainValues(var1, var6);
         }

         return var6;
      } else {
         var1.clear();
         return null;
      }
   }

   private static ArrayMap captureOutSharedElements(FragmentTransitionImpl var0, ArrayMap var1, Object var2, FragmentTransition.FragmentContainerTransition var3) {
      if (!var1.isEmpty() && var2 != null) {
         Fragment var8 = var3.firstOut;
         ArrayMap var4 = new ArrayMap();
         var0.findNamedViews(var4, var8.getView());
         BackStackRecord var6 = var3.firstOutTransaction;
         ArrayList var7;
         SharedElementCallback var9;
         if (var3.firstOutIsPop) {
            var9 = var8.getEnterTransitionCallback();
            var7 = var6.mSharedElementTargetNames;
         } else {
            var9 = var8.getExitTransitionCallback();
            var7 = var6.mSharedElementSourceNames;
         }

         var4.retainAll(var7);
         if (var9 != null) {
            var9.onMapSharedElements(var7, var4);

            for(int var5 = var7.size() - 1; var5 >= 0; --var5) {
               String var10 = (String)var7.get(var5);
               View var11 = (View)var4.get(var10);
               if (var11 == null) {
                  var1.remove(var10);
               } else if (!var10.equals(ViewCompat.getTransitionName(var11))) {
                  var10 = (String)var1.remove(var10);
                  var1.put(ViewCompat.getTransitionName(var11), var10);
               }
            }
         } else {
            var1.retainAll(var4.keySet());
         }

         return var4;
      } else {
         var1.clear();
         return null;
      }
   }

   private static FragmentTransitionImpl chooseImpl(Fragment var0, Fragment var1) {
      ArrayList var2 = new ArrayList();
      Object var4;
      if (var0 != null) {
         Object var3 = var0.getExitTransition();
         if (var3 != null) {
            var2.add(var3);
         }

         var3 = var0.getReturnTransition();
         if (var3 != null) {
            var2.add(var3);
         }

         var4 = var0.getSharedElementReturnTransition();
         if (var4 != null) {
            var2.add(var4);
         }
      }

      if (var1 != null) {
         var4 = var1.getEnterTransition();
         if (var4 != null) {
            var2.add(var4);
         }

         var4 = var1.getReenterTransition();
         if (var4 != null) {
            var2.add(var4);
         }

         var4 = var1.getSharedElementEnterTransition();
         if (var4 != null) {
            var2.add(var4);
         }
      }

      if (var2.isEmpty()) {
         return null;
      } else if (PLATFORM_IMPL != null && canHandleAll(PLATFORM_IMPL, var2)) {
         return PLATFORM_IMPL;
      } else if (SUPPORT_IMPL != null && canHandleAll(SUPPORT_IMPL, var2)) {
         return SUPPORT_IMPL;
      } else if (PLATFORM_IMPL == null && SUPPORT_IMPL == null) {
         return null;
      } else {
         throw new IllegalArgumentException("Invalid Transition types");
      }
   }

   static ArrayList configureEnteringExitingViews(FragmentTransitionImpl var0, Object var1, Fragment var2, ArrayList var3, View var4) {
      ArrayList var7;
      if (var1 != null) {
         ArrayList var5 = new ArrayList();
         View var6 = var2.getView();
         if (var6 != null) {
            var0.captureTransitioningViews(var5, var6);
         }

         if (var3 != null) {
            var5.removeAll(var3);
         }

         var7 = var5;
         if (!var5.isEmpty()) {
            var5.add(var4);
            var0.addTargets(var1, var5);
            var7 = var5;
         }
      } else {
         var7 = null;
      }

      return var7;
   }

   private static Object configureSharedElementsOrdered(final FragmentTransitionImpl var0, ViewGroup var1, final View var2, final ArrayMap var3, final FragmentTransition.FragmentContainerTransition var4, final ArrayList var5, final ArrayList var6, final Object var7, Object var8) {
      final Fragment var9 = var4.lastIn;
      final Fragment var10 = var4.firstOut;
      if (var9 != null && var10 != null) {
         final boolean var11 = var4.lastInIsPop;
         final Object var12;
         if (var3.isEmpty()) {
            var12 = null;
         } else {
            var12 = getSharedElementTransition(var0, var9, var10, var11);
         }

         ArrayMap var13 = captureOutSharedElements(var0, var3, var12, var4);
         if (var3.isEmpty()) {
            var12 = null;
         } else {
            var5.addAll(var13.values());
         }

         if (var7 == null && var8 == null && var12 == null) {
            return null;
         } else {
            callSharedElementStartEnd(var9, var10, var11, var13, true);
            final Rect var15;
            if (var12 != null) {
               Rect var14 = new Rect();
               var0.setSharedElementTargets(var12, var2, var5);
               setOutEpicenter(var0, var12, var8, var13, var4.firstOutIsPop, var4.firstOutTransaction);
               var15 = var14;
               if (var7 != null) {
                  var0.setEpicenter(var7, var14);
                  var15 = var14;
               }
            } else {
               var15 = null;
            }

            OneShotPreDrawListener.add(var1, new Runnable() {
               public void run() {
                  ArrayMap var1 = FragmentTransition.captureInSharedElements(var0, var3, var12, var4);
                  if (var1 != null) {
                     var6.addAll(var1.values());
                     var6.add(var2);
                  }

                  FragmentTransition.callSharedElementStartEnd(var9, var10, var11, var1, false);
                  if (var12 != null) {
                     var0.swapSharedElementTargets(var12, var5, var6);
                     View var2x = FragmentTransition.getInEpicenterView(var1, var4, var7, var11);
                     if (var2x != null) {
                        var0.getBoundsOnScreen(var2x, var15);
                     }
                  }

               }
            });
            return var12;
         }
      } else {
         return null;
      }
   }

   private static Object configureSharedElementsReordered(final FragmentTransitionImpl var0, ViewGroup var1, View var2, ArrayMap var3, FragmentTransition.FragmentContainerTransition var4, ArrayList var5, ArrayList var6, Object var7, Object var8) {
      final Fragment var9 = var4.lastIn;
      final Fragment var10 = var4.firstOut;
      if (var9 != null) {
         var9.getView().setVisibility(0);
      }

      if (var9 != null && var10 != null) {
         final boolean var11 = var4.lastInIsPop;
         Object var12;
         if (var3.isEmpty()) {
            var12 = null;
         } else {
            var12 = getSharedElementTransition(var0, var9, var10, var11);
         }

         ArrayMap var13 = captureOutSharedElements(var0, var3, var12, var4);
         final ArrayMap var14 = captureInSharedElements(var0, var3, var12, var4);
         Object var16;
         if (var3.isEmpty()) {
            if (var13 != null) {
               var13.clear();
            }

            if (var14 != null) {
               var14.clear();
            }

            var16 = null;
         } else {
            addSharedElementsWithMatchingNames(var5, var13, var3.keySet());
            addSharedElementsWithMatchingNames(var6, var14, var3.values());
            var16 = var12;
         }

         if (var7 == null && var8 == null && var16 == null) {
            return null;
         } else {
            callSharedElementStartEnd(var9, var10, var11, var13, true);
            final Object var15;
            final View var17;
            if (var16 != null) {
               var6.add(var2);
               var0.setSharedElementTargets(var16, var2, var5);
               setOutEpicenter(var0, var16, var8, var13, var4.firstOutIsPop, var4.firstOutTransaction);
               var15 = new Rect();
               var17 = getInEpicenterView(var14, var4, var7, var11);
               if (var17 != null) {
                  var0.setEpicenter(var7, (Rect)var15);
               }
            } else {
               var17 = null;
               var15 = var17;
            }

            OneShotPreDrawListener.add(var1, new Runnable() {
               public void run() {
                  FragmentTransition.callSharedElementStartEnd(var9, var10, var11, var14, false);
                  if (var17 != null) {
                     var0.getBoundsOnScreen(var17, (Rect)var15);
                  }

               }
            });
            return var16;
         }
      } else {
         return null;
      }
   }

   private static void configureTransitionsOrdered(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var15;
      if (var0.mContainer.onHasView()) {
         var15 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var15 = null;
      }

      if (var15 != null) {
         Fragment var5 = var2.lastIn;
         Fragment var6 = var2.firstOut;
         FragmentTransitionImpl var7 = chooseImpl(var6, var5);
         if (var7 != null) {
            boolean var8 = var2.lastInIsPop;
            boolean var9 = var2.firstOutIsPop;
            Object var10 = getEnterTransition(var7, var5, var8);
            Object var11 = getExitTransition(var7, var6, var9);
            ArrayList var12 = new ArrayList();
            ArrayList var13 = new ArrayList();
            Object var14 = configureSharedElementsOrdered(var7, var15, var3, var4, var2, var12, var13, var10, var11);
            if (var10 != null || var14 != null || var11 != null) {
               var12 = configureEnteringExitingViews(var7, var11, var6, var12, var3);
               if (var12 == null || var12.isEmpty()) {
                  var11 = null;
               }

               var7.addTarget(var10, var3);
               Object var17 = mergeTransitions(var7, var10, var11, var14, var5, var2.lastInIsPop);
               if (var17 != null) {
                  ArrayList var16 = new ArrayList();
                  var7.scheduleRemoveTargets(var17, var10, var16, var11, var12, var14, var13);
                  scheduleTargetChange(var7, var15, var5, var3, var13, var10, var16, var11, var12);
                  var7.setNameOverridesOrdered(var15, var13, var4);
                  var7.beginDelayedTransition(var15, var17);
                  var7.scheduleNameReset(var15, var13, var4);
               }

            }
         }
      }
   }

   private static void configureTransitionsReordered(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var15;
      if (var0.mContainer.onHasView()) {
         var15 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var15 = null;
      }

      if (var15 != null) {
         Fragment var5 = var2.lastIn;
         Fragment var6 = var2.firstOut;
         FragmentTransitionImpl var7 = chooseImpl(var6, var5);
         if (var7 != null) {
            boolean var8 = var2.lastInIsPop;
            boolean var9 = var2.firstOutIsPop;
            ArrayList var10 = new ArrayList();
            ArrayList var11 = new ArrayList();
            Object var12 = getEnterTransition(var7, var5, var8);
            Object var13 = getExitTransition(var7, var6, var9);
            Object var14 = configureSharedElementsReordered(var7, var15, var3, var4, var2, var11, var10, var12, var13);
            if (var12 != null || var14 != null || var13 != null) {
               Object var16 = var13;
               ArrayList var17 = configureEnteringExitingViews(var7, var13, var6, var11, var3);
               ArrayList var18 = configureEnteringExitingViews(var7, var12, var5, var10, var3);
               setViewVisibility(var18, 4);
               Object var19 = mergeTransitions(var7, var12, var16, var14, var5, var8);
               if (var19 != null) {
                  replaceHide(var7, var16, var6, var17);
                  ArrayList var20 = var7.prepareSetNameOverridesReordered(var10);
                  var7.scheduleRemoveTargets(var19, var12, var18, var16, var17, var14, var10);
                  var7.beginDelayedTransition(var15, var19);
                  var7.setNameOverridesReordered(var15, var11, var10, var20, var4);
                  setViewVisibility(var18, 0);
                  var7.swapSharedElementTargets(var14, var11, var10);
               }

            }
         }
      }
   }

   private static FragmentTransition.FragmentContainerTransition ensureContainer(FragmentTransition.FragmentContainerTransition var0, SparseArray var1, int var2) {
      FragmentTransition.FragmentContainerTransition var3 = var0;
      if (var0 == null) {
         var3 = new FragmentTransition.FragmentContainerTransition();
         var1.put(var2, var3);
      }

      return var3;
   }

   private static String findKeyForValue(ArrayMap var0, String var1) {
      int var2 = var0.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1.equals(var0.valueAt(var3))) {
            return (String)var0.keyAt(var3);
         }
      }

      return null;
   }

   private static Object getEnterTransition(FragmentTransitionImpl var0, Fragment var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         Object var3;
         if (var2) {
            var3 = var1.getReenterTransition();
         } else {
            var3 = var1.getEnterTransition();
         }

         return var0.cloneTransition(var3);
      }
   }

   private static Object getExitTransition(FragmentTransitionImpl var0, Fragment var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         Object var3;
         if (var2) {
            var3 = var1.getReturnTransition();
         } else {
            var3 = var1.getExitTransition();
         }

         return var0.cloneTransition(var3);
      }
   }

   static View getInEpicenterView(ArrayMap var0, FragmentTransition.FragmentContainerTransition var1, Object var2, boolean var3) {
      BackStackRecord var4 = var1.lastInTransaction;
      if (var2 != null && var0 != null && var4.mSharedElementSourceNames != null && !var4.mSharedElementSourceNames.isEmpty()) {
         String var5;
         if (var3) {
            var5 = (String)var4.mSharedElementSourceNames.get(0);
         } else {
            var5 = (String)var4.mSharedElementTargetNames.get(0);
         }

         return (View)var0.get(var5);
      } else {
         return null;
      }
   }

   private static Object getSharedElementTransition(FragmentTransitionImpl var0, Fragment var1, Fragment var2, boolean var3) {
      if (var1 != null && var2 != null) {
         Object var4;
         if (var3) {
            var4 = var2.getSharedElementReturnTransition();
         } else {
            var4 = var1.getSharedElementEnterTransition();
         }

         return var0.wrapTransitionInSet(var0.cloneTransition(var4));
      } else {
         return null;
      }
   }

   private static Object mergeTransitions(FragmentTransitionImpl var0, Object var1, Object var2, Object var3, Fragment var4, boolean var5) {
      if (var1 != null && var2 != null && var4 != null) {
         if (var5) {
            var5 = var4.getAllowReturnTransitionOverlap();
         } else {
            var5 = var4.getAllowEnterTransitionOverlap();
         }
      } else {
         var5 = true;
      }

      Object var6;
      if (var5) {
         var6 = var0.mergeTransitionsTogether(var2, var1, var3);
      } else {
         var6 = var0.mergeTransitionsInSequence(var2, var1, var3);
      }

      return var6;
   }

   private static void replaceHide(FragmentTransitionImpl var0, Object var1, Fragment var2, final ArrayList var3) {
      if (var2 != null && var1 != null && var2.mAdded && var2.mHidden && var2.mHiddenChanged) {
         var2.setHideReplaced(true);
         var0.scheduleHideFragmentView(var1, var2.getView(), var3);
         OneShotPreDrawListener.add(var2.mContainer, new Runnable() {
            public void run() {
               FragmentTransition.setViewVisibility(var3, 4);
            }
         });
      }

   }

   private static FragmentTransitionImpl resolveSupportImpl() {
      try {
         FragmentTransitionImpl var0 = (FragmentTransitionImpl)Class.forName("android.support.transition.FragmentTransitionSupport").getDeclaredConstructor().newInstance();
         return var0;
      } catch (Exception var1) {
         return null;
      }
   }

   private static void retainValues(ArrayMap var0, ArrayMap var1) {
      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         if (!var1.containsKey((String)var0.valueAt(var2))) {
            var0.removeAt(var2);
         }
      }

   }

   private static void scheduleTargetChange(final FragmentTransitionImpl var0, ViewGroup var1, final Fragment var2, final View var3, final ArrayList var4, final Object var5, final ArrayList var6, final Object var7, final ArrayList var8) {
      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            ArrayList var1;
            if (var5 != null) {
               var0.removeTarget(var5, var3);
               var1 = FragmentTransition.configureEnteringExitingViews(var0, var5, var2, var4, var3);
               var6.addAll(var1);
            }

            if (var8 != null) {
               if (var7 != null) {
                  var1 = new ArrayList();
                  var1.add(var3);
                  var0.replaceTargets(var7, var8, var1);
               }

               var8.clear();
               var8.add(var3);
            }

         }
      });
   }

   private static void setOutEpicenter(FragmentTransitionImpl var0, Object var1, Object var2, ArrayMap var3, boolean var4, BackStackRecord var5) {
      if (var5.mSharedElementSourceNames != null && !var5.mSharedElementSourceNames.isEmpty()) {
         String var6;
         if (var4) {
            var6 = (String)var5.mSharedElementTargetNames.get(0);
         } else {
            var6 = (String)var5.mSharedElementSourceNames.get(0);
         }

         View var7 = (View)var3.get(var6);
         var0.setEpicenter(var1, var7);
         if (var2 != null) {
            var0.setEpicenter(var2, var7);
         }
      }

   }

   static void setViewVisibility(ArrayList var0, int var1) {
      if (var0 != null) {
         for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
            ((View)var0.get(var2)).setVisibility(var1);
         }

      }
   }

   static void startTransitions(FragmentManagerImpl var0, ArrayList var1, ArrayList var2, int var3, int var4, boolean var5) {
      if (var0.mCurState >= 1) {
         SparseArray var6 = new SparseArray();

         int var7;
         for(var7 = var3; var7 < var4; ++var7) {
            BackStackRecord var8 = (BackStackRecord)var1.get(var7);
            if ((Boolean)var2.get(var7)) {
               calculatePopFragments(var8, var6, var5);
            } else {
               calculateFragments(var8, var6, var5);
            }
         }

         if (var6.size() != 0) {
            View var9 = new View(var0.mHost.getContext());
            int var10 = var6.size();

            for(var7 = 0; var7 < var10; ++var7) {
               int var11 = var6.keyAt(var7);
               ArrayMap var12 = calculateNameOverrides(var11, var1, var2, var3, var4);
               FragmentTransition.FragmentContainerTransition var13 = (FragmentTransition.FragmentContainerTransition)var6.valueAt(var7);
               if (var5) {
                  configureTransitionsReordered(var0, var11, var13, var9, var12);
               } else {
                  configureTransitionsOrdered(var0, var11, var13, var9, var12);
               }
            }
         }

      }
   }

   static class FragmentContainerTransition {
      public Fragment firstOut;
      public boolean firstOutIsPop;
      public BackStackRecord firstOutTransaction;
      public Fragment lastIn;
      public boolean lastInIsPop;
      public BackStackRecord lastInTransaction;
   }
}
