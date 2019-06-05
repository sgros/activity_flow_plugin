package android.support.v4.app;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
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

            boolean var8;
            boolean var9;
            boolean var10;
            boolean var13;
            label145: {
               label132: {
                  label131: {
                     label130: {
                        label129: {
                           label128: {
                              label141: {
                                 var8 = false;
                                 if (var7 != 1) {
                                    switch(var7) {
                                    case 3:
                                    case 6:
                                       if (var4) {
                                          if (!var5.mAdded && var5.mView != null && var5.mView.getVisibility() == 0 && var5.mPostponedAlpha >= 0.0F) {
                                             break label128;
                                          }
                                       } else if (var5.mAdded && !var5.mHidden) {
                                          break label128;
                                       }
                                       break label141;
                                    case 4:
                                       if (var4) {
                                          if (var5.mHiddenChanged && var5.mAdded && var5.mHidden) {
                                             break label128;
                                          }
                                       } else if (var5.mAdded && !var5.mHidden) {
                                          break label128;
                                       }
                                       break label141;
                                    case 5:
                                       if (!var4) {
                                          var8 = var5.mHidden;
                                          break label131;
                                       }

                                       if (var5.mHiddenChanged && !var5.mHidden && var5.mAdded) {
                                          break label130;
                                       }
                                       break label129;
                                    case 7:
                                       break;
                                    default:
                                       var13 = false;
                                       var9 = var13;
                                       var10 = var13;
                                       break label145;
                                    }
                                 }

                                 if (var4) {
                                    var8 = var5.mIsNewlyAdded;
                                    break label131;
                                 }

                                 if (!var5.mAdded && !var5.mHidden) {
                                    break label130;
                                 }
                                 break label129;
                              }

                              var13 = false;
                              break label132;
                           }

                           var13 = true;
                           break label132;
                        }

                        var8 = false;
                        break label131;
                     }

                     var8 = true;
                  }

                  var9 = false;
                  var10 = var9;
                  var13 = true;
                  break label145;
               }

               var10 = var13;
               var13 = false;
               var9 = true;
            }

            FragmentTransition.FragmentContainerTransition var11 = (FragmentTransition.FragmentContainerTransition)var2.get(var6);
            FragmentTransition.FragmentContainerTransition var12 = var11;
            if (var8) {
               var12 = ensureContainer(var11, var2, var6);
               var12.lastIn = var5;
               var12.lastInIsPop = var3;
               var12.lastInTransaction = var0;
            }

            if (!var4 && var13) {
               if (var12 != null && var12.firstOut == var5) {
                  var12.firstOut = null;
               }

               FragmentManagerImpl var14 = var0.mManager;
               if (var5.mState < 1 && var14.mCurState >= 1 && !var0.mReorderingAllowed) {
                  var14.makeActive(var5);
                  var14.moveToState(var5, 1, 0, 0, false);
               }
            }

            var11 = var12;
            if (var10) {
               label85: {
                  if (var12 != null) {
                     var11 = var12;
                     if (var12.firstOut != null) {
                        break label85;
                     }
                  }

                  var11 = ensureContainer(var12, var2, var6);
                  var11.firstOut = var5;
                  var11.firstOutIsPop = var3;
                  var11.firstOutTransaction = var0;
               }
            }

            if (!var4 && var9 && var11 != null && var11.lastIn == var5) {
               var11.lastIn = null;
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
                  String var14 = (String)var9.get(var11);
                  String var13 = (String)var5.remove(var14);
                  if (var13 != null) {
                     var5.put(var12, var13);
                  } else {
                     var5.put(var12, var14);
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

   private static void callSharedElementStartEnd(Fragment var0, Fragment var1, boolean var2, ArrayMap var3, boolean var4) {
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

   @RequiresApi(21)
   private static ArrayMap captureInSharedElements(ArrayMap var0, Object var1, FragmentTransition.FragmentContainerTransition var2) {
      Fragment var3 = var2.lastIn;
      View var4 = var3.getView();
      if (!var0.isEmpty() && var1 != null && var4 != null) {
         ArrayMap var5 = new ArrayMap();
         FragmentTransitionCompat21.findNamedViews(var5, var4);
         BackStackRecord var7 = var2.lastInTransaction;
         ArrayList var8;
         SharedElementCallback var9;
         if (var2.lastInIsPop) {
            var9 = var3.getExitTransitionCallback();
            var8 = var7.mSharedElementSourceNames;
         } else {
            var9 = var3.getEnterTransitionCallback();
            var8 = var7.mSharedElementTargetNames;
         }

         if (var8 != null) {
            var5.retainAll(var8);
         }

         if (var9 != null) {
            var9.onMapSharedElements(var8, var5);

            for(int var6 = var8.size() - 1; var6 >= 0; --var6) {
               String var10 = (String)var8.get(var6);
               View var11 = (View)var5.get(var10);
               if (var11 == null) {
                  String var12 = findKeyForValue(var0, var10);
                  if (var12 != null) {
                     var0.remove(var12);
                  }
               } else if (!var10.equals(ViewCompat.getTransitionName(var11))) {
                  var10 = findKeyForValue(var0, var10);
                  if (var10 != null) {
                     var0.put(var10, ViewCompat.getTransitionName(var11));
                  }
               }
            }
         } else {
            retainValues(var0, var5);
         }

         return var5;
      } else {
         var0.clear();
         return null;
      }
   }

   @RequiresApi(21)
   private static ArrayMap captureOutSharedElements(ArrayMap var0, Object var1, FragmentTransition.FragmentContainerTransition var2) {
      if (!var0.isEmpty() && var1 != null) {
         Fragment var3 = var2.firstOut;
         ArrayMap var4 = new ArrayMap();
         FragmentTransitionCompat21.findNamedViews(var4, var3.getView());
         BackStackRecord var6 = var2.firstOutTransaction;
         ArrayList var7;
         SharedElementCallback var8;
         if (var2.firstOutIsPop) {
            var8 = var3.getEnterTransitionCallback();
            var7 = var6.mSharedElementTargetNames;
         } else {
            var8 = var3.getExitTransitionCallback();
            var7 = var6.mSharedElementSourceNames;
         }

         var4.retainAll(var7);
         if (var8 != null) {
            var8.onMapSharedElements(var7, var4);

            for(int var5 = var7.size() - 1; var5 >= 0; --var5) {
               String var9 = (String)var7.get(var5);
               View var10 = (View)var4.get(var9);
               if (var10 == null) {
                  var0.remove(var9);
               } else if (!var9.equals(ViewCompat.getTransitionName(var10))) {
                  var9 = (String)var0.remove(var9);
                  var0.put(ViewCompat.getTransitionName(var10), var9);
               }
            }
         } else {
            var0.retainAll(var4.keySet());
         }

         return var4;
      } else {
         var0.clear();
         return null;
      }
   }

   @RequiresApi(21)
   private static ArrayList configureEnteringExitingViews(Object var0, Fragment var1, ArrayList var2, View var3) {
      ArrayList var6;
      if (var0 != null) {
         ArrayList var4 = new ArrayList();
         View var5 = var1.getView();
         if (var5 != null) {
            FragmentTransitionCompat21.captureTransitioningViews(var4, var5);
         }

         if (var2 != null) {
            var4.removeAll(var2);
         }

         var6 = var4;
         if (!var4.isEmpty()) {
            var4.add(var3);
            FragmentTransitionCompat21.addTargets(var0, var4);
            var6 = var4;
         }
      } else {
         var6 = null;
      }

      return var6;
   }

   @RequiresApi(21)
   private static Object configureSharedElementsOrdered(ViewGroup var0, final View var1, ArrayMap var2, final FragmentTransition.FragmentContainerTransition var3, final ArrayList var4, final ArrayList var5, final Object var6, Object var7) {
      final Fragment var8 = var3.lastIn;
      final Fragment var9 = var3.firstOut;
      Object var10 = null;
      if (var8 != null && var9 != null) {
         final boolean var11 = var3.lastInIsPop;
         Object var12;
         if (var2.isEmpty()) {
            var12 = null;
         } else {
            var12 = getSharedElementTransition(var8, var9, var11);
         }

         final ArrayMap var13 = var2;
         ArrayMap var14 = captureOutSharedElements(var2, var12, var3);
         final Object var15;
         if (var2.isEmpty()) {
            var15 = null;
         } else {
            var4.addAll(var14.values());
            var15 = var12;
         }

         if (var6 == null && var7 == null && var15 == null) {
            return null;
         } else {
            callSharedElementStartEnd(var8, var9, var11, var14, true);
            final Rect var17;
            if (var15 != null) {
               Rect var16 = new Rect();
               FragmentTransitionCompat21.setSharedElementTargets(var15, var1, var4);
               setOutEpicenter(var15, var7, var14, var3.firstOutIsPop, var3.firstOutTransaction);
               var17 = var16;
               if (var6 != null) {
                  FragmentTransitionCompat21.setEpicenter(var6, var16);
                  var17 = var16;
               }
            } else {
               var17 = (Rect)var10;
            }

            OneShotPreDrawListener.add(var0, new Runnable() {
               public void run() {
                  ArrayMap var1x = FragmentTransition.captureInSharedElements(var13, var15, var3);
                  if (var1x != null) {
                     var5.addAll(var1x.values());
                     var5.add(var1);
                  }

                  FragmentTransition.callSharedElementStartEnd(var8, var9, var11, var1x, false);
                  if (var15 != null) {
                     FragmentTransitionCompat21.swapSharedElementTargets(var15, var4, var5);
                     View var2 = FragmentTransition.getInEpicenterView(var1x, var3, var6, var11);
                     if (var2 != null) {
                        FragmentTransitionCompat21.getBoundsOnScreen(var2, var17);
                     }
                  }

               }
            });
            return var15;
         }
      } else {
         return null;
      }
   }

   @RequiresApi(21)
   private static Object configureSharedElementsReordered(ViewGroup var0, View var1, ArrayMap var2, FragmentTransition.FragmentContainerTransition var3, ArrayList var4, ArrayList var5, Object var6, Object var7) {
      final Fragment var8 = var3.lastIn;
      final Fragment var9 = var3.firstOut;
      if (var8 != null) {
         var8.getView().setVisibility(0);
      }

      if (var8 != null && var9 != null) {
         final boolean var10 = var3.lastInIsPop;
         Object var11;
         if (var2.isEmpty()) {
            var11 = null;
         } else {
            var11 = getSharedElementTransition(var8, var9, var10);
         }

         ArrayMap var12 = captureOutSharedElements(var2, var11, var3);
         final ArrayMap var13 = captureInSharedElements(var2, var11, var3);
         if (var2.isEmpty()) {
            if (var12 != null) {
               var12.clear();
            }

            if (var13 != null) {
               var13.clear();
            }

            var11 = null;
         } else {
            addSharedElementsWithMatchingNames(var4, var12, var2.keySet());
            addSharedElementsWithMatchingNames(var5, var13, var2.values());
         }

         if (var6 == null && var7 == null && var11 == null) {
            return null;
         } else {
            callSharedElementStartEnd(var8, var9, var10, var12, true);
            final Object var14;
            final View var15;
            if (var11 != null) {
               var5.add(var1);
               FragmentTransitionCompat21.setSharedElementTargets(var11, var1, var4);
               setOutEpicenter(var11, var7, var12, var3.firstOutIsPop, var3.firstOutTransaction);
               var14 = new Rect();
               var15 = getInEpicenterView(var13, var3, var6, var10);
               if (var15 != null) {
                  FragmentTransitionCompat21.setEpicenter(var6, (Rect)var14);
               }
            } else {
               var15 = null;
               var14 = var15;
            }

            OneShotPreDrawListener.add(var0, new Runnable() {
               public void run() {
                  FragmentTransition.callSharedElementStartEnd(var8, var9, var10, var13, false);
                  if (var15 != null) {
                     FragmentTransitionCompat21.getBoundsOnScreen(var15, (Rect)var14);
                  }

               }
            });
            return var11;
         }
      } else {
         return null;
      }
   }

   @RequiresApi(21)
   private static void configureTransitionsOrdered(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var14;
      if (var0.mContainer.onHasView()) {
         var14 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var14 = null;
      }

      if (var14 != null) {
         Fragment var5 = var2.lastIn;
         Fragment var6 = var2.firstOut;
         boolean var7 = var2.lastInIsPop;
         boolean var8 = var2.firstOutIsPop;
         Object var9 = getEnterTransition(var5, var7);
         Object var10 = getExitTransition(var6, var8);
         ArrayList var11 = new ArrayList();
         ArrayList var12 = new ArrayList();
         Object var13 = configureSharedElementsOrdered(var14, var3, var4, var2, var11, var12, var9, var10);
         if (var9 != null || var13 != null || var10 != null) {
            var11 = configureEnteringExitingViews(var10, var6, var11, var3);
            if (var11 == null || var11.isEmpty()) {
               var10 = null;
            }

            FragmentTransitionCompat21.addTarget(var9, var3);
            Object var16 = mergeTransitions(var9, var10, var13, var5, var2.lastInIsPop);
            if (var16 != null) {
               ArrayList var15 = new ArrayList();
               FragmentTransitionCompat21.scheduleRemoveTargets(var16, var9, var15, var10, var11, var13, var12);
               scheduleTargetChange(var14, var5, var3, var12, var9, var15, var10, var11);
               FragmentTransitionCompat21.setNameOverridesOrdered(var14, var12, var4);
               FragmentTransitionCompat21.beginDelayedTransition(var14, var16);
               FragmentTransitionCompat21.scheduleNameReset(var14, var12, var4);
            }

         }
      }
   }

   @RequiresApi(21)
   private static void configureTransitionsReordered(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var14;
      if (var0.mContainer.onHasView()) {
         var14 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      } else {
         var14 = null;
      }

      if (var14 != null) {
         Fragment var5 = var2.lastIn;
         Fragment var6 = var2.firstOut;
         boolean var7 = var2.lastInIsPop;
         boolean var8 = var2.firstOutIsPop;
         ArrayList var9 = new ArrayList();
         ArrayList var10 = new ArrayList();
         Object var11 = getEnterTransition(var5, var7);
         Object var12 = getExitTransition(var6, var8);
         Object var13 = configureSharedElementsReordered(var14, var3, var4, var2, var10, var9, var11, var12);
         if (var11 != null || var13 != null || var12 != null) {
            Object var16 = var12;
            ArrayList var15 = configureEnteringExitingViews(var12, var6, var10, var3);
            ArrayList var17 = configureEnteringExitingViews(var11, var5, var9, var3);
            setViewVisibility(var17, 4);
            Object var18 = mergeTransitions(var11, var16, var13, var5, var7);
            if (var18 != null) {
               replaceHide(var16, var6, var15);
               ArrayList var19 = FragmentTransitionCompat21.prepareSetNameOverridesReordered(var9);
               FragmentTransitionCompat21.scheduleRemoveTargets(var18, var11, var17, var16, var15, var13, var9);
               FragmentTransitionCompat21.beginDelayedTransition(var14, var18);
               FragmentTransitionCompat21.setNameOverridesReordered(var14, var10, var9, var19, var4);
               setViewVisibility(var17, 0);
               FragmentTransitionCompat21.swapSharedElementTargets(var13, var10, var9);
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

   @RequiresApi(21)
   private static Object getEnterTransition(Fragment var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         Object var2;
         if (var1) {
            var2 = var0.getReenterTransition();
         } else {
            var2 = var0.getEnterTransition();
         }

         return FragmentTransitionCompat21.cloneTransition(var2);
      }
   }

   @RequiresApi(21)
   private static Object getExitTransition(Fragment var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         Object var2;
         if (var1) {
            var2 = var0.getReturnTransition();
         } else {
            var2 = var0.getExitTransition();
         }

         return FragmentTransitionCompat21.cloneTransition(var2);
      }
   }

   private static View getInEpicenterView(ArrayMap var0, FragmentTransition.FragmentContainerTransition var1, Object var2, boolean var3) {
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

   @RequiresApi(21)
   private static Object getSharedElementTransition(Fragment var0, Fragment var1, boolean var2) {
      if (var0 != null && var1 != null) {
         Object var3;
         if (var2) {
            var3 = var1.getSharedElementReturnTransition();
         } else {
            var3 = var0.getSharedElementEnterTransition();
         }

         return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(var3));
      } else {
         return null;
      }
   }

   @RequiresApi(21)
   private static Object mergeTransitions(Object var0, Object var1, Object var2, Fragment var3, boolean var4) {
      if (var0 != null && var1 != null && var3 != null) {
         if (var4) {
            var4 = var3.getAllowReturnTransitionOverlap();
         } else {
            var4 = var3.getAllowEnterTransitionOverlap();
         }
      } else {
         var4 = true;
      }

      if (var4) {
         var0 = FragmentTransitionCompat21.mergeTransitionsTogether(var1, var0, var2);
      } else {
         var0 = FragmentTransitionCompat21.mergeTransitionsInSequence(var1, var0, var2);
      }

      return var0;
   }

   @RequiresApi(21)
   private static void replaceHide(Object var0, Fragment var1, final ArrayList var2) {
      if (var1 != null && var0 != null && var1.mAdded && var1.mHidden && var1.mHiddenChanged) {
         var1.setHideReplaced(true);
         FragmentTransitionCompat21.scheduleHideFragmentView(var0, var1.getView(), var2);
         OneShotPreDrawListener.add(var1.mContainer, new Runnable() {
            public void run() {
               FragmentTransition.setViewVisibility(var2, 4);
            }
         });
      }

   }

   private static void retainValues(ArrayMap var0, ArrayMap var1) {
      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         if (!var1.containsKey((String)var0.valueAt(var2))) {
            var0.removeAt(var2);
         }
      }

   }

   @RequiresApi(21)
   private static void scheduleTargetChange(ViewGroup var0, final Fragment var1, final View var2, final ArrayList var3, final Object var4, final ArrayList var5, final Object var6, final ArrayList var7) {
      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            ArrayList var1x;
            if (var4 != null) {
               FragmentTransitionCompat21.removeTarget(var4, var2);
               var1x = FragmentTransition.configureEnteringExitingViews(var4, var1, var3, var2);
               var5.addAll(var1x);
            }

            if (var7 != null) {
               if (var6 != null) {
                  var1x = new ArrayList();
                  var1x.add(var2);
                  FragmentTransitionCompat21.replaceTargets(var6, var7, var1x);
               }

               var7.clear();
               var7.add(var2);
            }

         }
      });
   }

   @RequiresApi(21)
   private static void setOutEpicenter(Object var0, Object var1, ArrayMap var2, boolean var3, BackStackRecord var4) {
      if (var4.mSharedElementSourceNames != null && !var4.mSharedElementSourceNames.isEmpty()) {
         String var6;
         if (var3) {
            var6 = (String)var4.mSharedElementTargetNames.get(0);
         } else {
            var6 = (String)var4.mSharedElementSourceNames.get(0);
         }

         View var5 = (View)var2.get(var6);
         FragmentTransitionCompat21.setEpicenter(var0, var5);
         if (var1 != null) {
            FragmentTransitionCompat21.setEpicenter(var1, var5);
         }
      }

   }

   private static void setViewVisibility(ArrayList var0, int var1) {
      if (var0 != null) {
         for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
            ((View)var0.get(var2)).setVisibility(var1);
         }

      }
   }

   static void startTransitions(FragmentManagerImpl var0, ArrayList var1, ArrayList var2, int var3, int var4, boolean var5) {
      if (var0.mCurState >= 1) {
         if (VERSION.SDK_INT >= 21) {
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
               View var13 = new View(var0.mHost.getContext());
               int var9 = var6.size();

               for(var7 = 0; var7 < var9; ++var7) {
                  int var10 = var6.keyAt(var7);
                  ArrayMap var11 = calculateNameOverrides(var10, var1, var2, var3, var4);
                  FragmentTransition.FragmentContainerTransition var12 = (FragmentTransition.FragmentContainerTransition)var6.valueAt(var7);
                  if (var5) {
                     configureTransitionsReordered(var0, var10, var12, var13, var11);
                  } else {
                     configureTransitionsOrdered(var0, var10, var12, var13, var11);
                  }
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
