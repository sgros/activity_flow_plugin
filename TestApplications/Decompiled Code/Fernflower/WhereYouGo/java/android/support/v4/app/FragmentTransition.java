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
   private static final int[] INVERSE_OPS = new int[]{0, 3, 0, 1, 5, 4, 7, 6};

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
      int var6 = var5.mContainerId;
      if (var6 != 0) {
         int var7;
         if (var3) {
            var7 = INVERSE_OPS[var1.cmd];
         } else {
            var7 = var1.cmd;
         }

         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;
         boolean var11 = false;
         boolean var12 = var10;
         boolean var13 = var8;
         boolean var14 = var11;
         boolean var15 = var9;
         switch(var7) {
         case 1:
         case 7:
            if (var4) {
               var13 = var5.mIsNewlyAdded;
            } else if (!var5.mAdded && !var5.mHidden) {
               var13 = true;
            } else {
               var13 = false;
            }

            var14 = true;
            var12 = var10;
            var15 = var9;
         case 2:
            break;
         case 3:
         case 6:
            if (var4) {
               if (!var5.mAdded && var5.mView != null && var5.mView.getVisibility() == 0 && var5.mPostponedAlpha >= 0.0F) {
                  var12 = true;
               } else {
                  var12 = false;
               }
            } else if (var5.mAdded && !var5.mHidden) {
               var12 = true;
            } else {
               var12 = false;
            }

            var15 = true;
            var13 = var8;
            var14 = var11;
            break;
         case 4:
            if (var4) {
               if (var5.mHiddenChanged && var5.mAdded && var5.mHidden) {
                  var12 = true;
               } else {
                  var12 = false;
               }
            } else if (var5.mAdded && !var5.mHidden) {
               var12 = true;
            } else {
               var12 = false;
            }

            var15 = true;
            var13 = var8;
            var14 = var11;
            break;
         case 5:
            if (var4) {
               if (var5.mHiddenChanged && !var5.mHidden && var5.mAdded) {
                  var13 = true;
               } else {
                  var13 = false;
               }
            } else {
               var13 = var5.mHidden;
            }

            var14 = true;
            var12 = var10;
            var15 = var9;
            break;
         default:
            var15 = var9;
            var14 = var11;
            var13 = var8;
            var12 = var10;
         }

         FragmentTransition.FragmentContainerTransition var16 = (FragmentTransition.FragmentContainerTransition)var2.get(var6);
         FragmentTransition.FragmentContainerTransition var17 = var16;
         if (var13) {
            var17 = ensureContainer(var16, var2, var6);
            var17.lastIn = var5;
            var17.lastInIsPop = var3;
            var17.lastInTransaction = var0;
         }

         if (!var4 && var14) {
            if (var17 != null && var17.firstOut == var5) {
               var17.firstOut = null;
            }

            FragmentManagerImpl var18 = var0.mManager;
            if (var5.mState < 1 && var18.mCurState >= 1 && !var0.mAllowOptimization) {
               var18.makeActive(var5);
               var18.moveToState(var5, 1, 0, 0, false);
            }
         }

         var16 = var17;
         if (var12) {
            label145: {
               if (var17 != null) {
                  var16 = var17;
                  if (var17.firstOut != null) {
                     break label145;
                  }
               }

               var16 = ensureContainer(var17, var2, var6);
               var16.firstOut = var5;
               var16.firstOutIsPop = var3;
               var16.firstOutTransaction = var0;
            }
         }

         if (!var4 && var15 && var16 != null && var16.lastIn == var5) {
            var16.lastIn = null;
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
                  String var14 = (String)var10.get(var11);
                  String var12 = (String)var9.get(var11);
                  String var13 = (String)var5.remove(var12);
                  if (var13 != null) {
                     var5.put(var14, var13);
                  } else {
                     var5.put(var14, var12);
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
         ArrayList var5 = new ArrayList();
         ArrayList var9 = new ArrayList();
         int var6;
         if (var3 == null) {
            var6 = 0;
         } else {
            var6 = var3.size();
         }

         for(int var7 = 0; var7 < var6; ++var7) {
            var9.add(var3.keyAt(var7));
            var5.add(var3.valueAt(var7));
         }

         if (var4) {
            var8.onSharedElementStart(var9, var5, (List)null);
         } else {
            var8.onSharedElementEnd(var9, var5, (List)null);
         }
      }

   }

   private static ArrayMap captureInSharedElements(ArrayMap var0, Object var1, FragmentTransition.FragmentContainerTransition var2) {
      Fragment var3 = var2.lastIn;
      View var4 = var3.getView();
      ArrayMap var9;
      if (!var0.isEmpty() && var1 != null && var4 != null) {
         ArrayMap var5 = new ArrayMap();
         FragmentTransitionCompat21.findNamedViews(var5, var4);
         BackStackRecord var7 = var2.lastInTransaction;
         ArrayList var8;
         SharedElementCallback var10;
         if (var2.lastInIsPop) {
            var10 = var3.getExitTransitionCallback();
            var8 = var7.mSharedElementSourceNames;
         } else {
            var10 = var3.getEnterTransitionCallback();
            var8 = var7.mSharedElementTargetNames;
         }

         if (var8 != null) {
            var5.retainAll(var8);
         }

         if (var10 != null) {
            var10.onMapSharedElements(var8, var5);
            int var6 = var8.size() - 1;

            while(true) {
               var9 = var5;
               if (var6 < 0) {
                  break;
               }

               String var11 = (String)var8.get(var6);
               View var12 = (View)var5.get(var11);
               if (var12 == null) {
                  String var13 = findKeyForValue(var0, var11);
                  if (var13 != null) {
                     var0.remove(var13);
                  }
               } else if (!var11.equals(ViewCompat.getTransitionName(var12))) {
                  var11 = findKeyForValue(var0, var11);
                  if (var11 != null) {
                     var0.put(var11, ViewCompat.getTransitionName(var12));
                  }
               }

               --var6;
            }
         } else {
            retainValues(var0, var5);
            var9 = var5;
         }
      } else {
         var0.clear();
         var9 = null;
      }

      return var9;
   }

   private static ArrayMap captureOutSharedElements(ArrayMap var0, Object var1, FragmentTransition.FragmentContainerTransition var2) {
      ArrayMap var8;
      if (!var0.isEmpty() && var1 != null) {
         Fragment var3 = var2.firstOut;
         ArrayMap var4 = new ArrayMap();
         FragmentTransitionCompat21.findNamedViews(var4, var3.getView());
         BackStackRecord var6 = var2.firstOutTransaction;
         ArrayList var7;
         SharedElementCallback var9;
         if (var2.firstOutIsPop) {
            var9 = var3.getEnterTransitionCallback();
            var7 = var6.mSharedElementTargetNames;
         } else {
            var9 = var3.getExitTransitionCallback();
            var7 = var6.mSharedElementSourceNames;
         }

         var4.retainAll(var7);
         if (var9 != null) {
            var9.onMapSharedElements(var7, var4);
            int var5 = var7.size() - 1;

            while(true) {
               var8 = var4;
               if (var5 < 0) {
                  break;
               }

               String var10 = (String)var7.get(var5);
               View var11 = (View)var4.get(var10);
               if (var11 == null) {
                  var0.remove(var10);
               } else if (!var10.equals(ViewCompat.getTransitionName(var11))) {
                  var10 = (String)var0.remove(var10);
                  var0.put(ViewCompat.getTransitionName(var11), var10);
               }

               --var5;
            }
         } else {
            var0.retainAll(var4.keySet());
            var8 = var4;
         }
      } else {
         var0.clear();
         var8 = null;
      }

      return var8;
   }

   private static ArrayList configureEnteringExitingViews(Object var0, Fragment var1, ArrayList var2, View var3) {
      ArrayList var4 = null;
      if (var0 != null) {
         ArrayList var5 = new ArrayList();
         View var6 = var1.getView();
         if (var6 != null) {
            FragmentTransitionCompat21.captureTransitioningViews(var5, var6);
         }

         if (var2 != null) {
            var5.removeAll(var2);
         }

         var4 = var5;
         if (!var5.isEmpty()) {
            var5.add(var3);
            FragmentTransitionCompat21.addTargets(var0, var5);
            var4 = var5;
         }
      }

      return var4;
   }

   private static Object configureSharedElementsOptimized(ViewGroup var0, final View var1, ArrayMap var2, FragmentTransition.FragmentContainerTransition var3, ArrayList var4, ArrayList var5, Object var6, Object var7) {
      final Fragment var8 = var3.lastIn;
      final Fragment var9 = var3.firstOut;
      if (var8 != null) {
         var8.getView().setVisibility(0);
      }

      Object var14;
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
            var11 = null;
            if (var12 != null) {
               var12.clear();
            }

            var14 = var11;
            if (var13 != null) {
               var13.clear();
               var14 = var11;
            }
         } else {
            addSharedElementsWithMatchingNames(var4, var12, var2.keySet());
            addSharedElementsWithMatchingNames(var5, var13, var2.values());
            var14 = var11;
         }

         if (var6 == null && var7 == null && var14 == null) {
            var14 = null;
         } else {
            callSharedElementStartEnd(var8, var9, var10, var12, true);
            final Rect var15;
            if (var14 != null) {
               var5.add(var1);
               FragmentTransitionCompat21.setSharedElementTargets(var14, var1, var4);
               setOutEpicenter(var14, var7, var12, var3.firstOutIsPop, var3.firstOutTransaction);
               Rect var16 = new Rect();
               View var17 = getInEpicenterView(var13, var3, var6, var10);
               var1 = var17;
               var15 = var16;
               if (var17 != null) {
                  FragmentTransitionCompat21.setEpicenter(var6, var16);
                  var15 = var16;
                  var1 = var17;
               }
            } else {
               var15 = null;
               var1 = null;
            }

            OneShotPreDrawListener.add(var0, new Runnable() {
               public void run() {
                  FragmentTransition.callSharedElementStartEnd(var8, var9, var10, var13, false);
                  if (var1 != null) {
                     FragmentTransitionCompat21.getBoundsOnScreen(var1, var15);
                  }

               }
            });
         }
      } else {
         var14 = null;
      }

      return var14;
   }

   private static Object configureSharedElementsUnoptimized(ViewGroup var0, final View var1, final ArrayMap var2, final FragmentTransition.FragmentContainerTransition var3, final ArrayList var4, final ArrayList var5, final Object var6, Object var7) {
      final Fragment var8 = var3.lastIn;
      final Fragment var9 = var3.firstOut;
      final Object var10;
      if (var8 != null && var9 != null) {
         final boolean var11 = var3.lastInIsPop;
         if (var2.isEmpty()) {
            var10 = null;
         } else {
            var10 = getSharedElementTransition(var8, var9, var11);
         }

         ArrayMap var12 = captureOutSharedElements(var2, var10, var3);
         if (var2.isEmpty()) {
            var10 = null;
         } else {
            var4.addAll(var12.values());
         }

         if (var6 == null && var7 == null && var10 == null) {
            var10 = null;
         } else {
            callSharedElementStartEnd(var8, var9, var11, var12, true);
            final Rect var14;
            if (var10 != null) {
               Rect var13 = new Rect();
               FragmentTransitionCompat21.setSharedElementTargets(var10, var1, var4);
               setOutEpicenter(var10, var7, var12, var3.firstOutIsPop, var3.firstOutTransaction);
               var14 = var13;
               if (var6 != null) {
                  FragmentTransitionCompat21.setEpicenter(var6, var13);
                  var14 = var13;
               }
            } else {
               var14 = null;
            }

            OneShotPreDrawListener.add(var0, new Runnable() {
               public void run() {
                  ArrayMap var1x = FragmentTransition.captureInSharedElements(var2, var10, var3);
                  if (var1x != null) {
                     var5.addAll(var1x.values());
                     var5.add(var1);
                  }

                  FragmentTransition.callSharedElementStartEnd(var8, var9, var11, var1x, false);
                  if (var10 != null) {
                     FragmentTransitionCompat21.swapSharedElementTargets(var10, var4, var5);
                     View var2x = FragmentTransition.getInEpicenterView(var1x, var3, var6, var11);
                     if (var2x != null) {
                        FragmentTransitionCompat21.getBoundsOnScreen(var2x, var14);
                     }
                  }

               }
            });
         }
      } else {
         var10 = null;
      }

      return var10;
   }

   private static void configureTransitionsOptimized(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var5 = null;
      if (var0.mContainer.onHasView()) {
         var5 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      }

      if (var5 != null) {
         Fragment var6 = var2.lastIn;
         Fragment var7 = var2.firstOut;
         boolean var8 = var2.lastInIsPop;
         boolean var9 = var2.firstOutIsPop;
         ArrayList var10 = new ArrayList();
         ArrayList var11 = new ArrayList();
         Object var12 = getEnterTransition(var6, var8);
         Object var14 = getExitTransition(var7, var9);
         Object var13 = configureSharedElementsOptimized(var5, var3, var4, var2, var11, var10, var12, var14);
         if (var12 != null || var13 != null || var14 != null) {
            ArrayList var15 = configureEnteringExitingViews(var14, var7, var11, var3);
            ArrayList var16 = configureEnteringExitingViews(var12, var6, var10, var3);
            setViewVisibility(var16, 4);
            Object var17 = mergeTransitions(var12, var14, var13, var6, var8);
            if (var17 != null) {
               replaceHide(var14, var7, var15);
               ArrayList var18 = FragmentTransitionCompat21.prepareSetNameOverridesOptimized(var10);
               FragmentTransitionCompat21.scheduleRemoveTargets(var17, var12, var16, var14, var15, var13, var10);
               FragmentTransitionCompat21.beginDelayedTransition(var5, var17);
               FragmentTransitionCompat21.setNameOverridesOptimized(var5, var11, var10, var18, var4);
               setViewVisibility(var16, 0);
               FragmentTransitionCompat21.swapSharedElementTargets(var13, var11, var10);
            }
         }
      }

   }

   private static void configureTransitionsUnoptimized(FragmentManagerImpl var0, int var1, FragmentTransition.FragmentContainerTransition var2, View var3, ArrayMap var4) {
      ViewGroup var5 = null;
      if (var0.mContainer.onHasView()) {
         var5 = (ViewGroup)var0.mContainer.onFindViewById(var1);
      }

      if (var5 != null) {
         Fragment var6 = var2.lastIn;
         Fragment var7 = var2.firstOut;
         boolean var8 = var2.lastInIsPop;
         boolean var9 = var2.firstOutIsPop;
         Object var10 = getEnterTransition(var6, var8);
         Object var14 = getExitTransition(var7, var9);
         ArrayList var11 = new ArrayList();
         ArrayList var12 = new ArrayList();
         Object var13 = configureSharedElementsUnoptimized(var5, var3, var4, var2, var11, var12, var10, var14);
         if (var10 != null || var13 != null || var14 != null) {
            ArrayList var16 = configureEnteringExitingViews(var14, var7, var11, var3);
            if (var16 == null || var16.isEmpty()) {
               var14 = null;
            }

            FragmentTransitionCompat21.addTarget(var10, var3);
            Object var15 = mergeTransitions(var10, var14, var13, var6, var2.lastInIsPop);
            if (var15 != null) {
               var11 = new ArrayList();
               FragmentTransitionCompat21.scheduleRemoveTargets(var15, var10, var11, var14, var16, var13, var12);
               scheduleTargetChange(var5, var6, var3, var12, var10, var11, var14, var16);
               FragmentTransitionCompat21.setNameOverridesUnoptimized(var5, var12, var4);
               FragmentTransitionCompat21.beginDelayedTransition(var5, var15);
               FragmentTransitionCompat21.scheduleNameReset(var5, var12, var4);
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
      int var3 = 0;

      String var4;
      while(true) {
         if (var3 >= var2) {
            var4 = null;
            break;
         }

         if (var1.equals(var0.valueAt(var3))) {
            var4 = (String)var0.keyAt(var3);
            break;
         }

         ++var3;
      }

      return var4;
   }

   private static Object getEnterTransition(Fragment var0, boolean var1) {
      Object var2;
      if (var0 == null) {
         var2 = null;
      } else {
         if (var1) {
            var2 = var0.getReenterTransition();
         } else {
            var2 = var0.getEnterTransition();
         }

         var2 = FragmentTransitionCompat21.cloneTransition(var2);
      }

      return var2;
   }

   private static Object getExitTransition(Fragment var0, boolean var1) {
      Object var2;
      if (var0 == null) {
         var2 = null;
      } else {
         if (var1) {
            var2 = var0.getReturnTransition();
         } else {
            var2 = var0.getExitTransition();
         }

         var2 = FragmentTransitionCompat21.cloneTransition(var2);
      }

      return var2;
   }

   private static View getInEpicenterView(ArrayMap var0, FragmentTransition.FragmentContainerTransition var1, Object var2, boolean var3) {
      BackStackRecord var5 = var1.lastInTransaction;
      View var4;
      if (var2 != null && var0 != null && var5.mSharedElementSourceNames != null && !var5.mSharedElementSourceNames.isEmpty()) {
         String var6;
         if (var3) {
            var6 = (String)var5.mSharedElementSourceNames.get(0);
         } else {
            var6 = (String)var5.mSharedElementTargetNames.get(0);
         }

         var4 = (View)var0.get(var6);
      } else {
         var4 = null;
      }

      return var4;
   }

   private static Object getSharedElementTransition(Fragment var0, Fragment var1, boolean var2) {
      Object var3;
      if (var0 != null && var1 != null) {
         if (var2) {
            var3 = var1.getSharedElementReturnTransition();
         } else {
            var3 = var0.getSharedElementEnterTransition();
         }

         var3 = FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(var3));
      } else {
         var3 = null;
      }

      return var3;
   }

   private static Object mergeTransitions(Object var0, Object var1, Object var2, Fragment var3, boolean var4) {
      boolean var5 = true;
      boolean var6 = var5;
      if (var0 != null) {
         var6 = var5;
         if (var1 != null) {
            var6 = var5;
            if (var3 != null) {
               if (var4) {
                  var6 = var3.getAllowReturnTransitionOverlap();
               } else {
                  var6 = var3.getAllowEnterTransitionOverlap();
               }
            }
         }
      }

      if (var6) {
         var0 = FragmentTransitionCompat21.mergeTransitionsTogether(var1, var0, var2);
      } else {
         var0 = FragmentTransitionCompat21.mergeTransitionsInSequence(var1, var0, var2);
      }

      return var0;
   }

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
      if (var0.mCurState >= 1 && VERSION.SDK_INT >= 21) {
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
               ArrayMap var13 = calculateNameOverrides(var11, var1, var2, var3, var4);
               FragmentTransition.FragmentContainerTransition var12 = (FragmentTransition.FragmentContainerTransition)var6.valueAt(var7);
               if (var5) {
                  configureTransitionsOptimized(var0, var11, var12, var9, var13);
               } else {
                  configureTransitionsUnoptimized(var0, var11, var12, var9, var13);
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
