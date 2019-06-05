package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.graphics.Path;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Transition implements Cloneable {
   private static final int[] DEFAULT_MATCH_ORDER = new int[]{2, 1, 3, 4};
   private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() {
      public Path getPath(float var1, float var2, float var3, float var4) {
         Path var5 = new Path();
         var5.moveTo(var1, var2);
         var5.lineTo(var3, var4);
         return var5;
      }
   };
   private static ThreadLocal sRunningAnimators = new ThreadLocal();
   private ArrayList mAnimators;
   boolean mCanRemoveViews;
   ArrayList mCurrentAnimators;
   long mDuration = -1L;
   private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
   private ArrayList mEndValuesList;
   private boolean mEnded;
   private Transition.EpicenterCallback mEpicenterCallback;
   private TimeInterpolator mInterpolator = null;
   private ArrayList mListeners;
   private int[] mMatchOrder;
   private String mName = this.getClass().getName();
   private ArrayMap mNameOverrides;
   private int mNumInstances;
   TransitionSet mParent = null;
   private PathMotion mPathMotion;
   private boolean mPaused;
   TransitionPropagation mPropagation;
   private ViewGroup mSceneRoot;
   private long mStartDelay = -1L;
   private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
   private ArrayList mStartValuesList;
   private ArrayList mTargetChildExcludes = null;
   private ArrayList mTargetExcludes = null;
   private ArrayList mTargetIdChildExcludes = null;
   private ArrayList mTargetIdExcludes = null;
   ArrayList mTargetIds = new ArrayList();
   private ArrayList mTargetNameExcludes = null;
   private ArrayList mTargetNames = null;
   private ArrayList mTargetTypeChildExcludes = null;
   private ArrayList mTargetTypeExcludes = null;
   private ArrayList mTargetTypes = null;
   ArrayList mTargets = new ArrayList();

   public Transition() {
      this.mMatchOrder = DEFAULT_MATCH_ORDER;
      this.mSceneRoot = null;
      this.mCanRemoveViews = false;
      this.mCurrentAnimators = new ArrayList();
      this.mNumInstances = 0;
      this.mPaused = false;
      this.mEnded = false;
      this.mListeners = null;
      this.mAnimators = new ArrayList();
      this.mPathMotion = STRAIGHT_PATH_MOTION;
   }

   private void addUnmatched(ArrayMap var1, ArrayMap var2) {
      byte var3 = 0;
      int var4 = 0;

      while(true) {
         int var5 = var3;
         if (var4 >= var1.size()) {
            for(; var5 < var2.size(); ++var5) {
               TransitionValues var7 = (TransitionValues)var2.valueAt(var5);
               if (this.isValidTarget(var7.view)) {
                  this.mEndValuesList.add(var7);
                  this.mStartValuesList.add((Object)null);
               }
            }

            return;
         }

         TransitionValues var6 = (TransitionValues)var1.valueAt(var4);
         if (this.isValidTarget(var6.view)) {
            this.mStartValuesList.add(var6);
            this.mEndValuesList.add((Object)null);
         }

         ++var4;
      }
   }

   private static void addViewValues(TransitionValuesMaps var0, View var1, TransitionValues var2) {
      var0.mViewValues.put(var1, var2);
      int var3 = var1.getId();
      if (var3 >= 0) {
         if (var0.mIdValues.indexOfKey(var3) >= 0) {
            var0.mIdValues.put(var3, (Object)null);
         } else {
            var0.mIdValues.put(var3, var1);
         }
      }

      String var6 = ViewCompat.getTransitionName(var1);
      if (var6 != null) {
         if (var0.mNameValues.containsKey(var6)) {
            var0.mNameValues.put(var6, (Object)null);
         } else {
            var0.mNameValues.put(var6, var1);
         }
      }

      if (var1.getParent() instanceof ListView) {
         ListView var7 = (ListView)var1.getParent();
         if (var7.getAdapter().hasStableIds()) {
            long var4 = var7.getItemIdAtPosition(var7.getPositionForView(var1));
            if (var0.mItemIdValues.indexOfKey(var4) >= 0) {
               var1 = (View)var0.mItemIdValues.get(var4);
               if (var1 != null) {
                  ViewCompat.setHasTransientState(var1, false);
                  var0.mItemIdValues.put(var4, (Object)null);
               }
            } else {
               ViewCompat.setHasTransientState(var1, true);
               var0.mItemIdValues.put(var4, var1);
            }
         }
      }

   }

   private void captureHierarchy(View var1, boolean var2) {
      if (var1 != null) {
         int var3 = var1.getId();
         if (this.mTargetIdExcludes == null || !this.mTargetIdExcludes.contains(var3)) {
            if (this.mTargetExcludes == null || !this.mTargetExcludes.contains(var1)) {
               ArrayList var4 = this.mTargetTypeExcludes;
               byte var5 = 0;
               int var6;
               int var7;
               if (var4 != null) {
                  var6 = this.mTargetTypeExcludes.size();

                  for(var7 = 0; var7 < var6; ++var7) {
                     if (((Class)this.mTargetTypeExcludes.get(var7)).isInstance(var1)) {
                        return;
                     }
                  }
               }

               if (var1.getParent() instanceof ViewGroup) {
                  TransitionValues var9 = new TransitionValues();
                  var9.view = var1;
                  if (var2) {
                     this.captureStartValues(var9);
                  } else {
                     this.captureEndValues(var9);
                  }

                  var9.mTargetedTransitions.add(this);
                  this.capturePropagationValues(var9);
                  if (var2) {
                     addViewValues(this.mStartValues, var1, var9);
                  } else {
                     addViewValues(this.mEndValues, var1, var9);
                  }
               }

               if (var1 instanceof ViewGroup) {
                  if (this.mTargetIdChildExcludes != null && this.mTargetIdChildExcludes.contains(var3)) {
                     return;
                  }

                  if (this.mTargetChildExcludes != null && this.mTargetChildExcludes.contains(var1)) {
                     return;
                  }

                  if (this.mTargetTypeChildExcludes != null) {
                     var6 = this.mTargetTypeChildExcludes.size();

                     for(var7 = 0; var7 < var6; ++var7) {
                        if (((Class)this.mTargetTypeChildExcludes.get(var7)).isInstance(var1)) {
                           return;
                        }
                     }
                  }

                  ViewGroup var8 = (ViewGroup)var1;

                  for(var7 = var5; var7 < var8.getChildCount(); ++var7) {
                     this.captureHierarchy(var8.getChildAt(var7), var2);
                  }
               }

            }
         }
      }
   }

   private static ArrayMap getRunningAnimators() {
      ArrayMap var0 = (ArrayMap)sRunningAnimators.get();
      ArrayMap var1 = var0;
      if (var0 == null) {
         var1 = new ArrayMap();
         sRunningAnimators.set(var1);
      }

      return var1;
   }

   private static boolean isValueChanged(TransitionValues var0, TransitionValues var1, String var2) {
      Object var5 = var0.values.get(var2);
      Object var6 = var1.values.get(var2);
      boolean var3 = true;
      boolean var4;
      if (var5 == null && var6 == null) {
         var4 = false;
      } else {
         var4 = var3;
         if (var5 != null) {
            if (var6 == null) {
               var4 = var3;
            } else {
               var4 = true ^ var5.equals(var6);
            }
         }
      }

      return var4;
   }

   private void matchIds(ArrayMap var1, ArrayMap var2, SparseArray var3, SparseArray var4) {
      int var5 = var3.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         View var7 = (View)var3.valueAt(var6);
         if (var7 != null && this.isValidTarget(var7)) {
            View var8 = (View)var4.get(var3.keyAt(var6));
            if (var8 != null && this.isValidTarget(var8)) {
               TransitionValues var9 = (TransitionValues)var1.get(var7);
               TransitionValues var10 = (TransitionValues)var2.get(var8);
               if (var9 != null && var10 != null) {
                  this.mStartValuesList.add(var9);
                  this.mEndValuesList.add(var10);
                  var1.remove(var7);
                  var2.remove(var8);
               }
            }
         }
      }

   }

   private void matchInstances(ArrayMap var1, ArrayMap var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         View var4 = (View)var1.keyAt(var3);
         if (var4 != null && this.isValidTarget(var4)) {
            TransitionValues var5 = (TransitionValues)var2.remove(var4);
            if (var5 != null && var5.view != null && this.isValidTarget(var5.view)) {
               TransitionValues var6 = (TransitionValues)var1.removeAt(var3);
               this.mStartValuesList.add(var6);
               this.mEndValuesList.add(var5);
            }
         }
      }

   }

   private void matchItemIds(ArrayMap var1, ArrayMap var2, LongSparseArray var3, LongSparseArray var4) {
      int var5 = var3.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         View var7 = (View)var3.valueAt(var6);
         if (var7 != null && this.isValidTarget(var7)) {
            View var8 = (View)var4.get(var3.keyAt(var6));
            if (var8 != null && this.isValidTarget(var8)) {
               TransitionValues var9 = (TransitionValues)var1.get(var7);
               TransitionValues var10 = (TransitionValues)var2.get(var8);
               if (var9 != null && var10 != null) {
                  this.mStartValuesList.add(var9);
                  this.mEndValuesList.add(var10);
                  var1.remove(var7);
                  var2.remove(var8);
               }
            }
         }
      }

   }

   private void matchNames(ArrayMap var1, ArrayMap var2, ArrayMap var3, ArrayMap var4) {
      int var5 = var3.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         View var7 = (View)var3.valueAt(var6);
         if (var7 != null && this.isValidTarget(var7)) {
            View var8 = (View)var4.get(var3.keyAt(var6));
            if (var8 != null && this.isValidTarget(var8)) {
               TransitionValues var9 = (TransitionValues)var1.get(var7);
               TransitionValues var10 = (TransitionValues)var2.get(var8);
               if (var9 != null && var10 != null) {
                  this.mStartValuesList.add(var9);
                  this.mEndValuesList.add(var10);
                  var1.remove(var7);
                  var2.remove(var8);
               }
            }
         }
      }

   }

   private void matchStartAndEnd(TransitionValuesMaps var1, TransitionValuesMaps var2) {
      ArrayMap var3 = new ArrayMap(var1.mViewValues);
      ArrayMap var4 = new ArrayMap(var2.mViewValues);

      for(int var5 = 0; var5 < this.mMatchOrder.length; ++var5) {
         switch(this.mMatchOrder[var5]) {
         case 1:
            this.matchInstances(var3, var4);
            break;
         case 2:
            this.matchNames(var3, var4, var1.mNameValues, var2.mNameValues);
            break;
         case 3:
            this.matchIds(var3, var4, var1.mIdValues, var2.mIdValues);
            break;
         case 4:
            this.matchItemIds(var3, var4, var1.mItemIdValues, var2.mItemIdValues);
         }
      }

      this.addUnmatched(var3, var4);
   }

   private void runAnimator(Animator var1, final ArrayMap var2) {
      if (var1 != null) {
         var1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               var2.remove(var1);
               Transition.this.mCurrentAnimators.remove(var1);
            }

            public void onAnimationStart(Animator var1) {
               Transition.this.mCurrentAnimators.add(var1);
            }
         });
         this.animate(var1);
      }

   }

   public Transition addListener(Transition.TransitionListener var1) {
      if (this.mListeners == null) {
         this.mListeners = new ArrayList();
      }

      this.mListeners.add(var1);
      return this;
   }

   public Transition addTarget(View var1) {
      this.mTargets.add(var1);
      return this;
   }

   protected void animate(Animator var1) {
      if (var1 == null) {
         this.end();
      } else {
         if (this.getDuration() >= 0L) {
            var1.setDuration(this.getDuration());
         }

         if (this.getStartDelay() >= 0L) {
            var1.setStartDelay(this.getStartDelay());
         }

         if (this.getInterpolator() != null) {
            var1.setInterpolator(this.getInterpolator());
         }

         var1.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               Transition.this.end();
               var1.removeListener(this);
            }
         });
         var1.start();
      }

   }

   public abstract void captureEndValues(TransitionValues var1);

   void capturePropagationValues(TransitionValues var1) {
      if (this.mPropagation != null && !var1.values.isEmpty()) {
         String[] var2 = this.mPropagation.getPropagationProperties();
         if (var2 == null) {
            return;
         }

         boolean var3 = false;
         int var4 = 0;

         boolean var5;
         while(true) {
            if (var4 >= var2.length) {
               var5 = true;
               break;
            }

            if (!var1.values.containsKey(var2[var4])) {
               var5 = var3;
               break;
            }

            ++var4;
         }

         if (!var5) {
            this.mPropagation.captureValues(var1);
         }
      }

   }

   public abstract void captureStartValues(TransitionValues var1);

   void captureValues(ViewGroup var1, boolean var2) {
      this.clearValues(var2);
      int var3 = this.mTargetIds.size();
      byte var4 = 0;
      View var12;
      if ((var3 > 0 || this.mTargets.size() > 0) && (this.mTargetNames == null || this.mTargetNames.isEmpty()) && (this.mTargetTypes == null || this.mTargetTypes.isEmpty())) {
         for(var3 = 0; var3 < this.mTargetIds.size(); ++var3) {
            View var5 = var1.findViewById((Integer)this.mTargetIds.get(var3));
            if (var5 != null) {
               TransitionValues var6 = new TransitionValues();
               var6.view = var5;
               if (var2) {
                  this.captureStartValues(var6);
               } else {
                  this.captureEndValues(var6);
               }

               var6.mTargetedTransitions.add(this);
               this.capturePropagationValues(var6);
               if (var2) {
                  addViewValues(this.mStartValues, var5, var6);
               } else {
                  addViewValues(this.mEndValues, var5, var6);
               }
            }
         }

         for(var3 = 0; var3 < this.mTargets.size(); ++var3) {
            var12 = (View)this.mTargets.get(var3);
            TransitionValues var9 = new TransitionValues();
            var9.view = var12;
            if (var2) {
               this.captureStartValues(var9);
            } else {
               this.captureEndValues(var9);
            }

            var9.mTargetedTransitions.add(this);
            this.capturePropagationValues(var9);
            if (var2) {
               addViewValues(this.mStartValues, var12, var9);
            } else {
               addViewValues(this.mEndValues, var12, var9);
            }
         }
      } else {
         this.captureHierarchy(var1, var2);
      }

      if (!var2 && this.mNameOverrides != null) {
         int var7 = this.mNameOverrides.size();
         ArrayList var10 = new ArrayList(var7);
         int var8 = 0;

         while(true) {
            var3 = var4;
            if (var8 >= var7) {
               for(; var3 < var7; ++var3) {
                  var12 = (View)var10.get(var3);
                  if (var12 != null) {
                     String var11 = (String)this.mNameOverrides.valueAt(var3);
                     this.mStartValues.mNameValues.put(var11, var12);
                  }
               }
               break;
            }

            String var13 = (String)this.mNameOverrides.keyAt(var8);
            var10.add(this.mStartValues.mNameValues.remove(var13));
            ++var8;
         }
      }

   }

   void clearValues(boolean var1) {
      if (var1) {
         this.mStartValues.mViewValues.clear();
         this.mStartValues.mIdValues.clear();
         this.mStartValues.mItemIdValues.clear();
      } else {
         this.mEndValues.mViewValues.clear();
         this.mEndValues.mIdValues.clear();
         this.mEndValues.mItemIdValues.clear();
      }

   }

   public Transition clone() {
      try {
         Transition var1 = (Transition)super.clone();
         ArrayList var2 = new ArrayList();
         var1.mAnimators = var2;
         TransitionValuesMaps var4 = new TransitionValuesMaps();
         var1.mStartValues = var4;
         var4 = new TransitionValuesMaps();
         var1.mEndValues = var4;
         var1.mStartValuesList = null;
         var1.mEndValuesList = null;
         return var1;
      } catch (CloneNotSupportedException var3) {
         return null;
      }
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      return null;
   }

   protected void createAnimators(ViewGroup var1, TransitionValuesMaps var2, TransitionValuesMaps var3, ArrayList var4, ArrayList var5) {
      ArrayMap var6 = getRunningAnimators();
      SparseIntArray var7 = new SparseIntArray();
      int var8 = var4.size();
      long var9 = Long.MAX_VALUE;

      int var11;
      long var15;
      int var27;
      for(var11 = 0; var11 < var8; var9 = var15) {
         TransitionValues var12 = (TransitionValues)var4.get(var11);
         TransitionValues var23 = (TransitionValues)var5.get(var11);
         TransitionValues var13 = var12;
         if (var12 != null) {
            var13 = var12;
            if (!var12.mTargetedTransitions.contains(this)) {
               var13 = null;
            }
         }

         TransitionValues var14 = var23;
         if (var23 != null) {
            var14 = var23;
            if (!var23.mTargetedTransitions.contains(this)) {
               var14 = null;
            }
         }

         label120: {
            if (var13 != null || var14 != null) {
               boolean var17;
               if (var13 != null && var14 != null && !this.isTransitionRequired(var13, var14)) {
                  var17 = false;
               } else {
                  var17 = true;
               }

               if (var17) {
                  Animator var24 = this.createAnimator(var1, var13, var14);
                  if (var24 != null) {
                     TransitionValues var20;
                     View var26;
                     if (var14 != null) {
                        View var18 = var14.view;
                        String[] var19 = this.getTransitionProperties();
                        if (var18 != null && var19 != null && var19.length > 0) {
                           var20 = new TransitionValues();
                           var20.view = var18;
                           var12 = (TransitionValues)var3.mViewValues.get(var18);
                           var27 = var11;
                           int var21;
                           if (var12 != null) {
                              var21 = 0;

                              while(true) {
                                 var27 = var11;
                                 if (var21 >= var19.length) {
                                    break;
                                 }

                                 var20.values.put(var19[var21], var12.values.get(var19[var21]));
                                 ++var21;
                              }
                           }

                           var11 = var27;
                           var21 = var6.size();
                           var27 = 0;

                           while(true) {
                              if (var27 >= var21) {
                                 var12 = var20;
                                 break;
                              }

                              Transition.AnimationInfo var25 = (Transition.AnimationInfo)var6.get((Animator)var6.keyAt(var27));
                              if (var25.mValues != null && var25.mView == var18 && var25.mName.equals(this.getName()) && var25.mValues.equals(var20)) {
                                 var24 = null;
                                 var12 = var20;
                                 break;
                              }

                              ++var27;
                           }
                        } else {
                           var12 = null;
                        }

                        var20 = var12;
                        var26 = var18;
                     } else {
                        var26 = var13.view;
                        var20 = null;
                     }

                     var15 = var9;
                     var27 = var11;
                     if (var24 != null) {
                        var15 = var9;
                        if (this.mPropagation != null) {
                           var15 = this.mPropagation.getStartDelay(var1, this, var13, var14);
                           var7.put(this.mAnimators.size(), (int)var15);
                           var15 = Math.min(var15, var9);
                        }

                        var6.put(var24, new Transition.AnimationInfo(var26, this.getName(), this, ViewUtils.getWindowId(var1), var20));
                        this.mAnimators.add(var24);
                        var27 = var11;
                     }
                     break label120;
                  }
               }
            }

            var15 = var9;
            var27 = var11;
         }

         var11 = var27 + 1;
      }

      if (var9 != 0L) {
         for(var11 = 0; var11 < var7.size(); ++var11) {
            var27 = var7.keyAt(var11);
            Animator var22 = (Animator)this.mAnimators.get(var27);
            var22.setStartDelay((long)var7.valueAt(var11) - var9 + var22.getStartDelay());
         }
      }

   }

   protected void end() {
      --this.mNumInstances;
      if (this.mNumInstances == 0) {
         int var3;
         if (this.mListeners != null && this.mListeners.size() > 0) {
            ArrayList var1 = (ArrayList)this.mListeners.clone();
            int var2 = var1.size();

            for(var3 = 0; var3 < var2; ++var3) {
               ((Transition.TransitionListener)var1.get(var3)).onTransitionEnd(this);
            }
         }

         View var4;
         for(var3 = 0; var3 < this.mStartValues.mItemIdValues.size(); ++var3) {
            var4 = (View)this.mStartValues.mItemIdValues.valueAt(var3);
            if (var4 != null) {
               ViewCompat.setHasTransientState(var4, false);
            }
         }

         for(var3 = 0; var3 < this.mEndValues.mItemIdValues.size(); ++var3) {
            var4 = (View)this.mEndValues.mItemIdValues.valueAt(var3);
            if (var4 != null) {
               ViewCompat.setHasTransientState(var4, false);
            }
         }

         this.mEnded = true;
      }

   }

   public long getDuration() {
      return this.mDuration;
   }

   public Transition.EpicenterCallback getEpicenterCallback() {
      return this.mEpicenterCallback;
   }

   public TimeInterpolator getInterpolator() {
      return this.mInterpolator;
   }

   TransitionValues getMatchedTransitionValues(View var1, boolean var2) {
      if (this.mParent != null) {
         return this.mParent.getMatchedTransitionValues(var1, var2);
      } else {
         ArrayList var3;
         if (var2) {
            var3 = this.mStartValuesList;
         } else {
            var3 = this.mEndValuesList;
         }

         Object var4 = null;
         if (var3 == null) {
            return null;
         } else {
            int var5 = var3.size();
            byte var6 = -1;
            int var7 = 0;

            int var8;
            while(true) {
               var8 = var6;
               if (var7 >= var5) {
                  break;
               }

               TransitionValues var9 = (TransitionValues)var3.get(var7);
               if (var9 == null) {
                  return null;
               }

               if (var9.view == var1) {
                  var8 = var7;
                  break;
               }

               ++var7;
            }

            TransitionValues var10 = (TransitionValues)var4;
            if (var8 >= 0) {
               ArrayList var11;
               if (var2) {
                  var11 = this.mEndValuesList;
               } else {
                  var11 = this.mStartValuesList;
               }

               var10 = (TransitionValues)var11.get(var8);
            }

            return var10;
         }
      }
   }

   public String getName() {
      return this.mName;
   }

   public PathMotion getPathMotion() {
      return this.mPathMotion;
   }

   public TransitionPropagation getPropagation() {
      return this.mPropagation;
   }

   public long getStartDelay() {
      return this.mStartDelay;
   }

   public List getTargetIds() {
      return this.mTargetIds;
   }

   public List getTargetNames() {
      return this.mTargetNames;
   }

   public List getTargetTypes() {
      return this.mTargetTypes;
   }

   public List getTargets() {
      return this.mTargets;
   }

   public String[] getTransitionProperties() {
      return null;
   }

   public TransitionValues getTransitionValues(View var1, boolean var2) {
      if (this.mParent != null) {
         return this.mParent.getTransitionValues(var1, var2);
      } else {
         TransitionValuesMaps var3;
         if (var2) {
            var3 = this.mStartValues;
         } else {
            var3 = this.mEndValues;
         }

         return (TransitionValues)var3.mViewValues.get(var1);
      }
   }

   public boolean isTransitionRequired(TransitionValues var1, TransitionValues var2) {
      boolean var3 = false;
      boolean var4 = var3;
      if (var1 != null) {
         var4 = var3;
         if (var2 != null) {
            String[] var5 = this.getTransitionProperties();
            if (var5 != null) {
               int var6 = var5.length;
               int var7 = 0;

               while(true) {
                  var4 = var3;
                  if (var7 >= var6) {
                     return var4;
                  }

                  if (isValueChanged(var1, var2, var5[var7])) {
                     break;
                  }

                  ++var7;
               }
            } else {
               Iterator var8 = var1.values.keySet().iterator();

               do {
                  var4 = var3;
                  if (!var8.hasNext()) {
                     return var4;
                  }
               } while(!isValueChanged(var1, var2, (String)var8.next()));
            }

            var4 = true;
         }
      }

      return var4;
   }

   boolean isValidTarget(View var1) {
      int var2 = var1.getId();
      if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains(var2)) {
         return false;
      } else if (this.mTargetExcludes != null && this.mTargetExcludes.contains(var1)) {
         return false;
      } else {
         int var4;
         if (this.mTargetTypeExcludes != null) {
            int var3 = this.mTargetTypeExcludes.size();

            for(var4 = 0; var4 < var3; ++var4) {
               if (((Class)this.mTargetTypeExcludes.get(var4)).isInstance(var1)) {
                  return false;
               }
            }
         }

         if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(var1) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(var1))) {
            return false;
         } else if (this.mTargetIds.size() != 0 || this.mTargets.size() != 0 || this.mTargetTypes != null && !this.mTargetTypes.isEmpty() || this.mTargetNames != null && !this.mTargetNames.isEmpty()) {
            if (!this.mTargetIds.contains(var2) && !this.mTargets.contains(var1)) {
               if (this.mTargetNames != null && this.mTargetNames.contains(ViewCompat.getTransitionName(var1))) {
                  return true;
               } else {
                  if (this.mTargetTypes != null) {
                     for(var4 = 0; var4 < this.mTargetTypes.size(); ++var4) {
                        if (((Class)this.mTargetTypes.get(var4)).isInstance(var1)) {
                           return true;
                        }
                     }
                  }

                  return false;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      }
   }

   public void pause(View var1) {
      if (!this.mEnded) {
         ArrayMap var2 = getRunningAnimators();
         int var3 = var2.size();
         WindowIdImpl var6 = ViewUtils.getWindowId(var1);
         --var3;

         for(; var3 >= 0; --var3) {
            Transition.AnimationInfo var4 = (Transition.AnimationInfo)var2.valueAt(var3);
            if (var4.mView != null && var6.equals(var4.mWindowId)) {
               AnimatorUtils.pause((Animator)var2.keyAt(var3));
            }
         }

         if (this.mListeners != null && this.mListeners.size() > 0) {
            ArrayList var7 = (ArrayList)this.mListeners.clone();
            int var5 = var7.size();

            for(var3 = 0; var3 < var5; ++var3) {
               ((Transition.TransitionListener)var7.get(var3)).onTransitionPause(this);
            }
         }

         this.mPaused = true;
      }

   }

   void playTransition(ViewGroup var1) {
      this.mStartValuesList = new ArrayList();
      this.mEndValuesList = new ArrayList();
      this.matchStartAndEnd(this.mStartValues, this.mEndValues);
      ArrayMap var2 = getRunningAnimators();
      int var3 = var2.size();
      WindowIdImpl var4 = ViewUtils.getWindowId(var1);
      --var3;

      for(; var3 >= 0; --var3) {
         Animator var5 = (Animator)var2.keyAt(var3);
         if (var5 != null) {
            Transition.AnimationInfo var6 = (Transition.AnimationInfo)var2.get(var5);
            if (var6 != null && var6.mView != null && var4.equals(var6.mWindowId)) {
               TransitionValues var7 = var6.mValues;
               View var8 = var6.mView;
               TransitionValues var9 = this.getTransitionValues(var8, true);
               TransitionValues var11 = this.getMatchedTransitionValues(var8, true);
               boolean var10;
               if ((var9 != null || var11 != null) && var6.mTransition.isTransitionRequired(var7, var11)) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               if (var10) {
                  if (!var5.isRunning() && !var5.isStarted()) {
                     var2.remove(var5);
                  } else {
                     var5.cancel();
                  }
               }
            }
         }
      }

      this.createAnimators(var1, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
      this.runAnimators();
   }

   public Transition removeListener(Transition.TransitionListener var1) {
      if (this.mListeners == null) {
         return this;
      } else {
         this.mListeners.remove(var1);
         if (this.mListeners.size() == 0) {
            this.mListeners = null;
         }

         return this;
      }
   }

   public Transition removeTarget(View var1) {
      this.mTargets.remove(var1);
      return this;
   }

   public void resume(View var1) {
      if (this.mPaused) {
         if (!this.mEnded) {
            ArrayMap var2 = getRunningAnimators();
            int var3 = var2.size();
            WindowIdImpl var4 = ViewUtils.getWindowId(var1);
            --var3;

            for(; var3 >= 0; --var3) {
               Transition.AnimationInfo var6 = (Transition.AnimationInfo)var2.valueAt(var3);
               if (var6.mView != null && var4.equals(var6.mWindowId)) {
                  AnimatorUtils.resume((Animator)var2.keyAt(var3));
               }
            }

            if (this.mListeners != null && this.mListeners.size() > 0) {
               ArrayList var7 = (ArrayList)this.mListeners.clone();
               int var5 = var7.size();

               for(var3 = 0; var3 < var5; ++var3) {
                  ((Transition.TransitionListener)var7.get(var3)).onTransitionResume(this);
               }
            }
         }

         this.mPaused = false;
      }

   }

   protected void runAnimators() {
      this.start();
      ArrayMap var1 = getRunningAnimators();
      Iterator var2 = this.mAnimators.iterator();

      while(var2.hasNext()) {
         Animator var3 = (Animator)var2.next();
         if (var1.containsKey(var3)) {
            this.start();
            this.runAnimator(var3, var1);
         }
      }

      this.mAnimators.clear();
      this.end();
   }

   public Transition setDuration(long var1) {
      this.mDuration = var1;
      return this;
   }

   public void setEpicenterCallback(Transition.EpicenterCallback var1) {
      this.mEpicenterCallback = var1;
   }

   public Transition setInterpolator(TimeInterpolator var1) {
      this.mInterpolator = var1;
      return this;
   }

   public void setPathMotion(PathMotion var1) {
      if (var1 == null) {
         this.mPathMotion = STRAIGHT_PATH_MOTION;
      } else {
         this.mPathMotion = var1;
      }

   }

   public void setPropagation(TransitionPropagation var1) {
      this.mPropagation = var1;
   }

   public Transition setStartDelay(long var1) {
      this.mStartDelay = var1;
      return this;
   }

   protected void start() {
      if (this.mNumInstances == 0) {
         if (this.mListeners != null && this.mListeners.size() > 0) {
            ArrayList var1 = (ArrayList)this.mListeners.clone();
            int var2 = var1.size();

            for(int var3 = 0; var3 < var2; ++var3) {
               ((Transition.TransitionListener)var1.get(var3)).onTransitionStart(this);
            }
         }

         this.mEnded = false;
      }

      ++this.mNumInstances;
   }

   public String toString() {
      return this.toString("");
   }

   String toString(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1);
      var2.append(this.getClass().getSimpleName());
      var2.append("@");
      var2.append(Integer.toHexString(this.hashCode()));
      var2.append(": ");
      var1 = var2.toString();
      String var6 = var1;
      if (this.mDuration != -1L) {
         var2 = new StringBuilder();
         var2.append(var1);
         var2.append("dur(");
         var2.append(this.mDuration);
         var2.append(") ");
         var6 = var2.toString();
      }

      var1 = var6;
      StringBuilder var5;
      if (this.mStartDelay != -1L) {
         var5 = new StringBuilder();
         var5.append(var6);
         var5.append("dly(");
         var5.append(this.mStartDelay);
         var5.append(") ");
         var1 = var5.toString();
      }

      var6 = var1;
      if (this.mInterpolator != null) {
         var2 = new StringBuilder();
         var2.append(var1);
         var2.append("interp(");
         var2.append(this.mInterpolator);
         var2.append(") ");
         var6 = var2.toString();
      }

      if (this.mTargetIds.size() <= 0) {
         var1 = var6;
         if (this.mTargets.size() <= 0) {
            return var1;
         }
      }

      var5 = new StringBuilder();
      var5.append(var6);
      var5.append("tgts(");
      var6 = var5.toString();
      int var3 = this.mTargetIds.size();
      byte var4 = 0;
      var1 = var6;
      if (var3 > 0) {
         var1 = var6;

         for(var3 = 0; var3 < this.mTargetIds.size(); ++var3) {
            var6 = var1;
            if (var3 > 0) {
               var2 = new StringBuilder();
               var2.append(var1);
               var2.append(", ");
               var6 = var2.toString();
            }

            var5 = new StringBuilder();
            var5.append(var6);
            var5.append(this.mTargetIds.get(var3));
            var1 = var5.toString();
         }
      }

      var6 = var1;
      if (this.mTargets.size() > 0) {
         var3 = var4;

         while(true) {
            var6 = var1;
            if (var3 >= this.mTargets.size()) {
               break;
            }

            var6 = var1;
            if (var3 > 0) {
               var2 = new StringBuilder();
               var2.append(var1);
               var2.append(", ");
               var6 = var2.toString();
            }

            var5 = new StringBuilder();
            var5.append(var6);
            var5.append(this.mTargets.get(var3));
            var1 = var5.toString();
            ++var3;
         }
      }

      var5 = new StringBuilder();
      var5.append(var6);
      var5.append(")");
      var1 = var5.toString();
      return var1;
   }

   private static class AnimationInfo {
      String mName;
      Transition mTransition;
      TransitionValues mValues;
      View mView;
      WindowIdImpl mWindowId;

      AnimationInfo(View var1, String var2, Transition var3, WindowIdImpl var4, TransitionValues var5) {
         this.mView = var1;
         this.mName = var2;
         this.mValues = var5;
         this.mWindowId = var4;
         this.mTransition = var3;
      }
   }

   public abstract static class EpicenterCallback {
   }

   public interface TransitionListener {
      void onTransitionEnd(Transition var1);

      void onTransitionPause(Transition var1);

      void onTransitionResume(Transition var1);

      void onTransitionStart(Transition var1);
   }
}
