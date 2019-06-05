package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import java.util.ArrayList;

public class ConstraintLayout extends ViewGroup {
   static final boolean ALLOWS_EMBEDDED = false;
   private static final boolean SIMPLE_LAYOUT = true;
   private static final String TAG = "ConstraintLayout";
   public static final String VERSION = "ConstraintLayout-1.0.0";
   SparseArray mChildrenByIds = new SparseArray();
   private ConstraintSet mConstraintSet = null;
   private boolean mDirtyHierarchy = true;
   ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
   private int mMaxHeight = Integer.MAX_VALUE;
   private int mMaxWidth = Integer.MAX_VALUE;
   private int mMinHeight = 0;
   private int mMinWidth = 0;
   private int mOptimizationLevel = 2;
   private final ArrayList mVariableDimensionsWidgets = new ArrayList(100);

   public ConstraintLayout(Context var1) {
      super(var1);
      this.init((AttributeSet)null);
   }

   public ConstraintLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var2);
   }

   public ConstraintLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var2);
   }

   private final ConstraintWidget getTargetWidget(int var1) {
      if (var1 == 0) {
         return this.mLayoutWidget;
      } else {
         View var2 = (View)this.mChildrenByIds.get(var1);
         if (var2 == this) {
            return this.mLayoutWidget;
         } else {
            ConstraintWidget var3;
            if (var2 == null) {
               var3 = null;
            } else {
               var3 = ((ConstraintLayout.LayoutParams)var2.getLayoutParams()).widget;
            }

            return var3;
         }
      }
   }

   private final ConstraintWidget getViewWidget(View var1) {
      if (var1 == this) {
         return this.mLayoutWidget;
      } else {
         ConstraintWidget var2;
         if (var1 == null) {
            var2 = null;
         } else {
            var2 = ((ConstraintLayout.LayoutParams)var1.getLayoutParams()).widget;
         }

         return var2;
      }
   }

   private void init(AttributeSet var1) {
      this.mLayoutWidget.setCompanionWidget(this);
      this.mChildrenByIds.put(this.getId(), this);
      this.mConstraintSet = null;
      if (var1 != null) {
         TypedArray var5 = this.getContext().obtainStyledAttributes(var1, R.styleable.ConstraintLayout_Layout);
         int var2 = var5.getIndexCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = var5.getIndex(var3);
            if (var4 == R.styleable.ConstraintLayout_Layout_android_minWidth) {
               this.mMinWidth = var5.getDimensionPixelOffset(var4, this.mMinWidth);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_android_minHeight) {
               this.mMinHeight = var5.getDimensionPixelOffset(var4, this.mMinHeight);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
               this.mMaxWidth = var5.getDimensionPixelOffset(var4, this.mMaxWidth);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
               this.mMaxHeight = var5.getDimensionPixelOffset(var4, this.mMaxHeight);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
               this.mOptimizationLevel = var5.getInt(var4, this.mOptimizationLevel);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_constraintSet) {
               var4 = var5.getResourceId(var4, 0);
               this.mConstraintSet = new ConstraintSet();
               this.mConstraintSet.load(this.getContext(), var4);
            }
         }

         var5.recycle();
      }

      this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
   }

   private void internalMeasureChildren(int var1, int var2) {
      int var3 = this.getPaddingTop() + this.getPaddingBottom();
      int var4 = this.getPaddingLeft() + this.getPaddingRight();
      int var5 = this.getChildCount();

      for(int var6 = 0; var6 < var5; ++var6) {
         View var7 = this.getChildAt(var6);
         if (var7.getVisibility() != 8) {
            ConstraintLayout.LayoutParams var8 = (ConstraintLayout.LayoutParams)var7.getLayoutParams();
            ConstraintWidget var9 = var8.widget;
            if (!var8.isGuideline) {
               int var10 = var8.width;
               int var11 = var8.height;
               boolean var12 = var8.horizontalDimensionFixed;
               boolean var13 = true;
               boolean var14;
               if (var12 || var8.verticalDimensionFixed || !var8.horizontalDimensionFixed && var8.matchConstraintDefaultWidth == 1 || var8.width == -1 || !var8.verticalDimensionFixed && (var8.matchConstraintDefaultHeight == 1 || var8.height == -1)) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               if (var14) {
                  if (var10 != 0 && var10 != -1) {
                     var10 = getChildMeasureSpec(var1, var4, var10);
                     var14 = false;
                  } else {
                     var10 = getChildMeasureSpec(var1, var4, -2);
                     var14 = true;
                  }

                  if (var11 != 0 && var11 != -1) {
                     var11 = getChildMeasureSpec(var2, var3, var11);
                     var13 = false;
                  } else {
                     var11 = getChildMeasureSpec(var2, var3, -2);
                  }

                  var7.measure(var10, var11);
                  var10 = var7.getMeasuredWidth();
                  var11 = var7.getMeasuredHeight();
               } else {
                  var13 = false;
                  var14 = var13;
               }

               var9.setWidth(var10);
               var9.setHeight(var11);
               if (var14) {
                  var9.setWrapWidth(var10);
               }

               if (var13) {
                  var9.setWrapHeight(var11);
               }

               if (var8.needsBaseline) {
                  int var15 = var7.getBaseline();
                  if (var15 != -1) {
                     var9.setBaselineDistance(var15);
                  }
               }
            }
         }
      }

   }

   private void setChildrenConstraints() {
      if (this.mConstraintSet != null) {
         this.mConstraintSet.applyToInternal(this);
      }

      int var1 = this.getChildCount();
      this.mLayoutWidget.removeAllChildren();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         ConstraintWidget var4 = this.getViewWidget(var3);
         if (var4 != null) {
            ConstraintLayout.LayoutParams var5 = (ConstraintLayout.LayoutParams)var3.getLayoutParams();
            var4.reset();
            var4.setVisibility(var3.getVisibility());
            var4.setCompanionWidget(var3);
            this.mLayoutWidget.add(var4);
            if (!var5.verticalDimensionFixed || !var5.horizontalDimensionFixed) {
               this.mVariableDimensionsWidgets.add(var4);
            }

            if (var5.isGuideline) {
               android.support.constraint.solver.widgets.Guideline var22 = (android.support.constraint.solver.widgets.Guideline)var4;
               if (var5.guideBegin != -1) {
                  var22.setGuideBegin(var5.guideBegin);
               }

               if (var5.guideEnd != -1) {
                  var22.setGuideEnd(var5.guideEnd);
               }

               if (var5.guidePercent != -1.0F) {
                  var22.setGuidePercent(var5.guidePercent);
               }
            } else if (var5.resolvedLeftToLeft != -1 || var5.resolvedLeftToRight != -1 || var5.resolvedRightToLeft != -1 || var5.resolvedRightToRight != -1 || var5.topToTop != -1 || var5.topToBottom != -1 || var5.bottomToTop != -1 || var5.bottomToBottom != -1 || var5.baselineToBaseline != -1 || var5.editorAbsoluteX != -1 || var5.editorAbsoluteY != -1 || var5.width == -1 || var5.height == -1) {
               int var6 = var5.resolvedLeftToLeft;
               int var7 = var5.resolvedLeftToRight;
               int var8 = var5.resolvedRightToLeft;
               int var9 = var5.resolvedRightToRight;
               int var10 = var5.resolveGoneLeftMargin;
               int var11 = var5.resolveGoneRightMargin;
               float var12 = var5.resolvedHorizontalBias;
               if (android.os.Build.VERSION.SDK_INT < 17) {
                  var8 = var5.leftToLeft;
                  var9 = var5.leftToRight;
                  int var13 = var5.rightToLeft;
                  int var14 = var5.rightToRight;
                  int var15 = var5.goneLeftMargin;
                  int var16 = var5.goneRightMargin;
                  float var17 = var5.horizontalBias;
                  int var18 = var8;
                  int var19 = var9;
                  if (var8 == -1) {
                     var18 = var8;
                     var19 = var9;
                     if (var9 == -1) {
                        if (var5.startToStart != -1) {
                           var18 = var5.startToStart;
                           var19 = var9;
                        } else {
                           var18 = var8;
                           var19 = var9;
                           if (var5.startToEnd != -1) {
                              var19 = var5.startToEnd;
                              var18 = var8;
                           }
                        }
                     }
                  }

                  var6 = var18;
                  var7 = var19;
                  var8 = var13;
                  var9 = var14;
                  var10 = var15;
                  var11 = var16;
                  var12 = var17;
                  if (var13 == -1) {
                     var6 = var18;
                     var7 = var19;
                     var8 = var13;
                     var9 = var14;
                     var10 = var15;
                     var11 = var16;
                     var12 = var17;
                     if (var14 == -1) {
                        if (var5.endToStart != -1) {
                           var8 = var5.endToStart;
                           var6 = var18;
                           var7 = var19;
                           var9 = var14;
                           var10 = var15;
                           var11 = var16;
                           var12 = var17;
                        } else {
                           var6 = var18;
                           var7 = var19;
                           var8 = var13;
                           var9 = var14;
                           var10 = var15;
                           var11 = var16;
                           var12 = var17;
                           if (var5.endToEnd != -1) {
                              var9 = var5.endToEnd;
                              var12 = var17;
                              var11 = var16;
                              var10 = var15;
                              var8 = var13;
                              var7 = var19;
                              var6 = var18;
                           }
                        }
                     }
                  }
               }

               ConstraintWidget var21;
               if (var6 != -1) {
                  var21 = this.getTargetWidget(var6);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.LEFT, var21, ConstraintAnchor.Type.LEFT, var5.leftMargin, var10);
                  }
               } else if (var7 != -1) {
                  var21 = this.getTargetWidget(var7);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.LEFT, var21, ConstraintAnchor.Type.RIGHT, var5.leftMargin, var10);
                  }
               }

               if (var8 != -1) {
                  var21 = this.getTargetWidget(var8);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.RIGHT, var21, ConstraintAnchor.Type.LEFT, var5.rightMargin, var11);
                  }
               } else if (var9 != -1) {
                  var21 = this.getTargetWidget(var9);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.RIGHT, var21, ConstraintAnchor.Type.RIGHT, var5.rightMargin, var11);
                  }
               }

               if (var5.topToTop != -1) {
                  var21 = this.getTargetWidget(var5.topToTop);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.TOP, var21, ConstraintAnchor.Type.TOP, var5.topMargin, var5.goneTopMargin);
                  }
               } else if (var5.topToBottom != -1) {
                  var21 = this.getTargetWidget(var5.topToBottom);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.TOP, var21, ConstraintAnchor.Type.BOTTOM, var5.topMargin, var5.goneTopMargin);
                  }
               }

               if (var5.bottomToTop != -1) {
                  var21 = this.getTargetWidget(var5.bottomToTop);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.BOTTOM, var21, ConstraintAnchor.Type.TOP, var5.bottomMargin, var5.goneBottomMargin);
                  }
               } else if (var5.bottomToBottom != -1) {
                  var21 = this.getTargetWidget(var5.bottomToBottom);
                  if (var21 != null) {
                     var4.immediateConnect(ConstraintAnchor.Type.BOTTOM, var21, ConstraintAnchor.Type.BOTTOM, var5.bottomMargin, var5.goneBottomMargin);
                  }
               }

               if (var5.baselineToBaseline != -1) {
                  View var20 = (View)this.mChildrenByIds.get(var5.baselineToBaseline);
                  var21 = this.getTargetWidget(var5.baselineToBaseline);
                  if (var21 != null && var20 != null && var20.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
                     ConstraintLayout.LayoutParams var23 = (ConstraintLayout.LayoutParams)var20.getLayoutParams();
                     var5.needsBaseline = true;
                     var23.needsBaseline = true;
                     var4.getAnchor(ConstraintAnchor.Type.BASELINE).connect(var21.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                     var4.getAnchor(ConstraintAnchor.Type.TOP).reset();
                     var4.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                  }
               }

               if (var12 >= 0.0F && var12 != 0.5F) {
                  var4.setHorizontalBiasPercent(var12);
               }

               if (var5.verticalBias >= 0.0F && var5.verticalBias != 0.5F) {
                  var4.setVerticalBiasPercent(var5.verticalBias);
               }

               if (this.isInEditMode() && (var5.editorAbsoluteX != -1 || var5.editorAbsoluteY != -1)) {
                  var4.setOrigin(var5.editorAbsoluteX, var5.editorAbsoluteY);
               }

               if (!var5.horizontalDimensionFixed) {
                  if (var5.width == -1) {
                     var4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                     var4.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = var5.leftMargin;
                     var4.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = var5.rightMargin;
                  } else {
                     var4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                     var4.setWidth(0);
                  }
               } else {
                  var4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                  var4.setWidth(var5.width);
               }

               if (!var5.verticalDimensionFixed) {
                  if (var5.height == -1) {
                     var4.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                     var4.getAnchor(ConstraintAnchor.Type.TOP).mMargin = var5.topMargin;
                     var4.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = var5.bottomMargin;
                  } else {
                     var4.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                     var4.setHeight(0);
                  }
               } else {
                  var4.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                  var4.setHeight(var5.height);
               }

               if (var5.dimensionRatio != null) {
                  var4.setDimensionRatio(var5.dimensionRatio);
               }

               var4.setHorizontalWeight(var5.horizontalWeight);
               var4.setVerticalWeight(var5.verticalWeight);
               var4.setHorizontalChainStyle(var5.horizontalChainStyle);
               var4.setVerticalChainStyle(var5.verticalChainStyle);
               var4.setHorizontalMatchStyle(var5.matchConstraintDefaultWidth, var5.matchConstraintMinWidth, var5.matchConstraintMaxWidth);
               var4.setVerticalMatchStyle(var5.matchConstraintDefaultHeight, var5.matchConstraintMinHeight, var5.matchConstraintMaxHeight);
            }
         }
      }

   }

   private void setSelfDimensionBehaviour(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      int var4 = MeasureSpec.getMode(var2);
      var2 = MeasureSpec.getSize(var2);
      int var5 = this.getPaddingTop();
      int var6 = this.getPaddingBottom();
      int var7 = this.getPaddingLeft();
      int var8 = this.getPaddingRight();
      ConstraintWidget.DimensionBehaviour var9 = ConstraintWidget.DimensionBehaviour.FIXED;
      ConstraintWidget.DimensionBehaviour var10 = ConstraintWidget.DimensionBehaviour.FIXED;
      this.getLayoutParams();
      if (var3 != Integer.MIN_VALUE) {
         label28: {
            if (var3 != 0) {
               if (var3 == 1073741824) {
                  var1 = Math.min(this.mMaxWidth, var1) - (var7 + var8);
                  break label28;
               }
            } else {
               var9 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            }

            var1 = 0;
         }
      } else {
         var9 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
      }

      if (var4 != Integer.MIN_VALUE) {
         label22: {
            if (var4 != 0) {
               if (var4 == 1073741824) {
                  var2 = Math.min(this.mMaxHeight, var2) - (var5 + var6);
                  break label22;
               }
            } else {
               var10 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            }

            var2 = 0;
         }
      } else {
         var10 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
      }

      this.mLayoutWidget.setMinWidth(0);
      this.mLayoutWidget.setMinHeight(0);
      this.mLayoutWidget.setHorizontalDimensionBehaviour(var9);
      this.mLayoutWidget.setWidth(var1);
      this.mLayoutWidget.setVerticalDimensionBehaviour(var10);
      this.mLayoutWidget.setHeight(var2);
      this.mLayoutWidget.setMinWidth(this.mMinWidth - this.getPaddingLeft() - this.getPaddingRight());
      this.mLayoutWidget.setMinHeight(this.mMinHeight - this.getPaddingTop() - this.getPaddingBottom());
   }

   private void updateHierarchy() {
      int var1 = this.getChildCount();
      boolean var2 = false;
      int var3 = 0;

      boolean var4;
      while(true) {
         var4 = var2;
         if (var3 >= var1) {
            break;
         }

         if (this.getChildAt(var3).isLayoutRequested()) {
            var4 = true;
            break;
         }

         ++var3;
      }

      if (var4) {
         this.mVariableDimensionsWidgets.clear();
         this.setChildrenConstraints();
      }

   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      super.addView(var1, var2, var3);
      if (android.os.Build.VERSION.SDK_INT < 14) {
         this.onViewAdded(var1);
      }

   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof ConstraintLayout.LayoutParams;
   }

   protected ConstraintLayout.LayoutParams generateDefaultLayoutParams() {
      return new ConstraintLayout.LayoutParams(-2, -2);
   }

   public ConstraintLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ConstraintLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new ConstraintLayout.LayoutParams(var1);
   }

   public int getMaxHeight() {
      return this.mMaxHeight;
   }

   public int getMaxWidth() {
      return this.mMaxWidth;
   }

   public int getMinHeight() {
      return this.mMinHeight;
   }

   public int getMinWidth() {
      return this.mMinWidth;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var3 = this.getChildCount();
      var1 = this.isInEditMode();

      for(var2 = 0; var2 < var3; ++var2) {
         View var6 = this.getChildAt(var2);
         ConstraintLayout.LayoutParams var7 = (ConstraintLayout.LayoutParams)var6.getLayoutParams();
         if (var6.getVisibility() != 8 || var7.isGuideline || var1) {
            ConstraintWidget var8 = var7.widget;
            var4 = var8.getDrawX();
            var5 = var8.getDrawY();
            var6.layout(var4, var5, var8.getWidth() + var4, var8.getHeight() + var5);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = this.getPaddingLeft();
      int var4 = this.getPaddingTop();
      this.mLayoutWidget.setX(var3);
      this.mLayoutWidget.setY(var4);
      this.setSelfDimensionBehaviour(var1, var2);
      boolean var5 = this.mDirtyHierarchy;
      int var6 = 0;
      if (var5) {
         this.mDirtyHierarchy = false;
         this.updateHierarchy();
      }

      this.internalMeasureChildren(var1, var2);
      if (this.getChildCount() > 0) {
         this.solveLinearSystem();
      }

      int var7 = this.mVariableDimensionsWidgets.size();
      int var8 = var4 + this.getPaddingBottom();
      int var9 = var3 + this.getPaddingRight();
      int var15;
      if (var7 > 0) {
         boolean var10;
         if (this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var10 = true;
         } else {
            var10 = false;
         }

         boolean var11;
         if (this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var11 = true;
         } else {
            var11 = false;
         }

         byte var17 = 0;

         for(var3 = var17; var6 < var7; var3 = var15) {
            byte var14;
            label94: {
               ConstraintWidget var12 = (ConstraintWidget)this.mVariableDimensionsWidgets.get(var6);
               if (!(var12 instanceof android.support.constraint.solver.widgets.Guideline)) {
                  View var13 = (View)var12.getCompanionWidget();
                  if (var13 != null && var13.getVisibility() != 8) {
                     ConstraintLayout.LayoutParams var16 = (ConstraintLayout.LayoutParams)var13.getLayoutParams();
                     if (var16.width == -2) {
                        var15 = getChildMeasureSpec(var1, var9, var16.width);
                     } else {
                        var15 = MeasureSpec.makeMeasureSpec(var12.getWidth(), 1073741824);
                     }

                     int var18;
                     if (var16.height == -2) {
                        var18 = getChildMeasureSpec(var2, var8, var16.height);
                     } else {
                        var18 = MeasureSpec.makeMeasureSpec(var12.getHeight(), 1073741824);
                     }

                     var13.measure(var15, var18);
                     var15 = var13.getMeasuredWidth();
                     var18 = var13.getMeasuredHeight();
                     if (var15 != var12.getWidth()) {
                        var12.setWidth(var15);
                        if (var10 && var12.getRight() > this.mLayoutWidget.getWidth()) {
                           var4 = var12.getRight();
                           var15 = var12.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin();
                           this.mLayoutWidget.setWidth(Math.max(this.mMinWidth, var4 + var15));
                        }

                        var17 = 1;
                     }

                     byte var19 = var17;
                     if (var18 != var12.getHeight()) {
                        var12.setHeight(var18);
                        if (var11 && var12.getBottom() > this.mLayoutWidget.getHeight()) {
                           var15 = var12.getBottom();
                           var4 = var12.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin();
                           this.mLayoutWidget.setHeight(Math.max(this.mMinHeight, var15 + var4));
                        }

                        var19 = 1;
                     }

                     var17 = var19;
                     if (var16.needsBaseline) {
                        var18 = var13.getBaseline();
                        var17 = var19;
                        if (var18 != -1) {
                           var17 = var19;
                           if (var18 != var12.getBaselineDistance()) {
                              var12.setBaselineDistance(var18);
                              var17 = 1;
                           }
                        }
                     }

                     var14 = var17;
                     var15 = var3;
                     if (android.os.Build.VERSION.SDK_INT >= 11) {
                        var15 = combineMeasuredStates(var3, var13.getMeasuredState());
                        var14 = var17;
                     }
                     break label94;
                  }
               }

               var14 = var17;
               var15 = var3;
            }

            ++var6;
            var17 = var14;
         }

         var15 = var3;
         if (var17 != 0) {
            this.solveLinearSystem();
            var15 = var3;
         }
      } else {
         var15 = 0;
      }

      var4 = this.mLayoutWidget.getWidth() + var9;
      var3 = this.mLayoutWidget.getHeight() + var8;
      if (android.os.Build.VERSION.SDK_INT >= 11) {
         var1 = resolveSizeAndState(var4, var1, var15);
         var3 = resolveSizeAndState(var3, var2, var15 << 16);
         var2 = Math.min(this.mMaxWidth, var1);
         var1 = Math.min(this.mMaxHeight, var3);
         var2 &= 16777215;
         var3 = var1 & 16777215;
         var1 = var2;
         if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
            var1 = var2 | 16777216;
         }

         var2 = var3;
         if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
            var2 = var3 | 16777216;
         }

         this.setMeasuredDimension(var1, var2);
      } else {
         this.setMeasuredDimension(var4, var3);
      }

   }

   public void onViewAdded(View var1) {
      if (android.os.Build.VERSION.SDK_INT >= 14) {
         super.onViewAdded(var1);
      }

      ConstraintWidget var2 = this.getViewWidget(var1);
      if (var1 instanceof Guideline && !(var2 instanceof android.support.constraint.solver.widgets.Guideline)) {
         ConstraintLayout.LayoutParams var3 = (ConstraintLayout.LayoutParams)var1.getLayoutParams();
         var3.widget = new android.support.constraint.solver.widgets.Guideline();
         var3.isGuideline = true;
         ((android.support.constraint.solver.widgets.Guideline)var3.widget).setOrientation(var3.orientation);
         var2 = var3.widget;
      }

      this.mChildrenByIds.put(var1.getId(), var1);
      this.mDirtyHierarchy = true;
   }

   public void onViewRemoved(View var1) {
      if (android.os.Build.VERSION.SDK_INT >= 14) {
         super.onViewRemoved(var1);
      }

      this.mChildrenByIds.remove(var1.getId());
      this.mLayoutWidget.remove(this.getViewWidget(var1));
      this.mDirtyHierarchy = true;
   }

   public void removeView(View var1) {
      super.removeView(var1);
      if (android.os.Build.VERSION.SDK_INT < 14) {
         this.onViewRemoved(var1);
      }

   }

   public void requestLayout() {
      super.requestLayout();
      this.mDirtyHierarchy = true;
   }

   public void setConstraintSet(ConstraintSet var1) {
      this.mConstraintSet = var1;
   }

   public void setId(int var1) {
      this.mChildrenByIds.remove(this.getId());
      super.setId(var1);
      this.mChildrenByIds.put(this.getId(), this);
   }

   public void setMaxHeight(int var1) {
      if (var1 != this.mMaxHeight) {
         this.mMaxHeight = var1;
         this.requestLayout();
      }
   }

   public void setMaxWidth(int var1) {
      if (var1 != this.mMaxWidth) {
         this.mMaxWidth = var1;
         this.requestLayout();
      }
   }

   public void setMinHeight(int var1) {
      if (var1 != this.mMinHeight) {
         this.mMinHeight = var1;
         this.requestLayout();
      }
   }

   public void setMinWidth(int var1) {
      if (var1 != this.mMinWidth) {
         this.mMinWidth = var1;
         this.requestLayout();
      }
   }

   public void setOptimizationLevel(int var1) {
      this.mLayoutWidget.setOptimizationLevel(var1);
   }

   protected void solveLinearSystem() {
      this.mLayoutWidget.layout();
   }

   public static class LayoutParams extends MarginLayoutParams {
      public static final int BASELINE = 5;
      public static final int BOTTOM = 4;
      public static final int CHAIN_PACKED = 2;
      public static final int CHAIN_SPREAD = 0;
      public static final int CHAIN_SPREAD_INSIDE = 1;
      public static final int END = 7;
      public static final int HORIZONTAL = 0;
      public static final int LEFT = 1;
      public static final int MATCH_CONSTRAINT = 0;
      public static final int MATCH_CONSTRAINT_SPREAD = 0;
      public static final int MATCH_CONSTRAINT_WRAP = 1;
      public static final int PARENT_ID = 0;
      public static final int RIGHT = 2;
      public static final int START = 6;
      public static final int TOP = 3;
      public static final int UNSET = -1;
      public static final int VERTICAL = 1;
      public int baselineToBaseline = -1;
      public int bottomToBottom = -1;
      public int bottomToTop = -1;
      public String dimensionRatio = null;
      int dimensionRatioSide = 1;
      float dimensionRatioValue = 0.0F;
      public int editorAbsoluteX = -1;
      public int editorAbsoluteY = -1;
      public int endToEnd = -1;
      public int endToStart = -1;
      public int goneBottomMargin = -1;
      public int goneEndMargin = -1;
      public int goneLeftMargin = -1;
      public int goneRightMargin = -1;
      public int goneStartMargin = -1;
      public int goneTopMargin = -1;
      public int guideBegin = -1;
      public int guideEnd = -1;
      public float guidePercent = -1.0F;
      public float horizontalBias = 0.5F;
      public int horizontalChainStyle = 0;
      boolean horizontalDimensionFixed = true;
      public float horizontalWeight = 0.0F;
      boolean isGuideline = false;
      public int leftToLeft = -1;
      public int leftToRight = -1;
      public int matchConstraintDefaultHeight = 0;
      public int matchConstraintDefaultWidth = 0;
      public int matchConstraintMaxHeight = 0;
      public int matchConstraintMaxWidth = 0;
      public int matchConstraintMinHeight = 0;
      public int matchConstraintMinWidth = 0;
      boolean needsBaseline = false;
      public int orientation = -1;
      int resolveGoneLeftMargin = -1;
      int resolveGoneRightMargin = -1;
      float resolvedHorizontalBias = 0.5F;
      int resolvedLeftToLeft = -1;
      int resolvedLeftToRight = -1;
      int resolvedRightToLeft = -1;
      int resolvedRightToRight = -1;
      public int rightToLeft = -1;
      public int rightToRight = -1;
      public int startToEnd = -1;
      public int startToStart = -1;
      public int topToBottom = -1;
      public int topToTop = -1;
      public float verticalBias = 0.5F;
      public int verticalChainStyle = 0;
      boolean verticalDimensionFixed = true;
      public float verticalWeight = 0.0F;
      ConstraintWidget widget = new ConstraintWidget();

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var15 = var1.obtainStyledAttributes(var2, R.styleable.ConstraintLayout_Layout);
         int var3 = var15.getIndexCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = var15.getIndex(var4);
            if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf) {
               this.leftToLeft = var15.getResourceId(var5, this.leftToLeft);
               if (this.leftToLeft == -1) {
                  this.leftToLeft = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf) {
               this.leftToRight = var15.getResourceId(var5, this.leftToRight);
               if (this.leftToRight == -1) {
                  this.leftToRight = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf) {
               this.rightToLeft = var15.getResourceId(var5, this.rightToLeft);
               if (this.rightToLeft == -1) {
                  this.rightToLeft = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf) {
               this.rightToRight = var15.getResourceId(var5, this.rightToRight);
               if (this.rightToRight == -1) {
                  this.rightToRight = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf) {
               this.topToTop = var15.getResourceId(var5, this.topToTop);
               if (this.topToTop == -1) {
                  this.topToTop = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf) {
               this.topToBottom = var15.getResourceId(var5, this.topToBottom);
               if (this.topToBottom == -1) {
                  this.topToBottom = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf) {
               this.bottomToTop = var15.getResourceId(var5, this.bottomToTop);
               if (this.bottomToTop == -1) {
                  this.bottomToTop = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf) {
               this.bottomToBottom = var15.getResourceId(var5, this.bottomToBottom);
               if (this.bottomToBottom == -1) {
                  this.bottomToBottom = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf) {
               this.baselineToBaseline = var15.getResourceId(var5, this.baselineToBaseline);
               if (this.baselineToBaseline == -1) {
                  this.baselineToBaseline = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX) {
               this.editorAbsoluteX = var15.getDimensionPixelOffset(var5, this.editorAbsoluteX);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY) {
               this.editorAbsoluteY = var15.getDimensionPixelOffset(var5, this.editorAbsoluteY);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin) {
               this.guideBegin = var15.getDimensionPixelOffset(var5, this.guideBegin);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end) {
               this.guideEnd = var15.getDimensionPixelOffset(var5, this.guideEnd);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent) {
               this.guidePercent = var15.getFloat(var5, this.guidePercent);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_android_orientation) {
               this.orientation = var15.getInt(var5, this.orientation);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf) {
               this.startToEnd = var15.getResourceId(var5, this.startToEnd);
               if (this.startToEnd == -1) {
                  this.startToEnd = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf) {
               this.startToStart = var15.getResourceId(var5, this.startToStart);
               if (this.startToStart == -1) {
                  this.startToStart = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf) {
               this.endToStart = var15.getResourceId(var5, this.endToStart);
               if (this.endToStart == -1) {
                  this.endToStart = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf) {
               this.endToEnd = var15.getResourceId(var5, this.endToEnd);
               if (this.endToEnd == -1) {
                  this.endToEnd = var15.getInt(var5, -1);
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft) {
               this.goneLeftMargin = var15.getDimensionPixelSize(var5, this.goneLeftMargin);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_goneMarginTop) {
               this.goneTopMargin = var15.getDimensionPixelSize(var5, this.goneTopMargin);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_goneMarginRight) {
               this.goneRightMargin = var15.getDimensionPixelSize(var5, this.goneRightMargin);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom) {
               this.goneBottomMargin = var15.getDimensionPixelSize(var5, this.goneBottomMargin);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_goneMarginStart) {
               this.goneStartMargin = var15.getDimensionPixelSize(var5, this.goneStartMargin);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd) {
               this.goneEndMargin = var15.getDimensionPixelSize(var5, this.goneEndMargin);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias) {
               this.horizontalBias = var15.getFloat(var5, this.horizontalBias);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias) {
               this.verticalBias = var15.getFloat(var5, this.verticalBias);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio) {
               this.dimensionRatio = var15.getString(var5);
               this.dimensionRatioValue = Float.NaN;
               this.dimensionRatioSide = -1;
               if (this.dimensionRatio != null) {
                  int var6 = this.dimensionRatio.length();
                  var5 = this.dimensionRatio.indexOf(44);
                  String var16;
                  if (var5 > 0 && var5 < var6 - 1) {
                     var16 = this.dimensionRatio.substring(0, var5);
                     if (var16.equalsIgnoreCase("W")) {
                        this.dimensionRatioSide = 0;
                     } else if (var16.equalsIgnoreCase("H")) {
                        this.dimensionRatioSide = 1;
                     }

                     ++var5;
                  } else {
                     var5 = 0;
                  }

                  int var7 = this.dimensionRatio.indexOf(58);
                  boolean var10001;
                  if (var7 >= 0 && var7 < var6 - 1) {
                     String var8 = this.dimensionRatio.substring(var5, var7);
                     var16 = this.dimensionRatio.substring(var7 + 1);
                     if (var8.length() > 0 && var16.length() > 0) {
                        float var9;
                        float var10;
                        try {
                           var9 = Float.parseFloat(var8);
                           var10 = Float.parseFloat(var16);
                        } catch (NumberFormatException var13) {
                           var10001 = false;
                           continue;
                        }

                        if (var9 > 0.0F && var10 > 0.0F) {
                           try {
                              if (this.dimensionRatioSide == 1) {
                                 this.dimensionRatioValue = Math.abs(var10 / var9);
                                 continue;
                              }
                           } catch (NumberFormatException var14) {
                              var10001 = false;
                              continue;
                           }

                           try {
                              this.dimensionRatioValue = Math.abs(var9 / var10);
                           } catch (NumberFormatException var11) {
                              var10001 = false;
                           }
                        }
                     }
                  } else {
                     var16 = this.dimensionRatio.substring(var5);
                     if (var16.length() > 0) {
                        try {
                           this.dimensionRatioValue = Float.parseFloat(var16);
                        } catch (NumberFormatException var12) {
                           var10001 = false;
                        }
                     }
                  }
               }
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight) {
               this.horizontalWeight = var15.getFloat(var5, 0.0F);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight) {
               this.verticalWeight = var15.getFloat(var5, 0.0F);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle) {
               this.horizontalChainStyle = var15.getInt(var5, 0);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle) {
               this.verticalChainStyle = var15.getInt(var5, 0);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default) {
               this.matchConstraintDefaultWidth = var15.getInt(var5, 0);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default) {
               this.matchConstraintDefaultHeight = var15.getInt(var5, 0);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min) {
               this.matchConstraintMinWidth = var15.getDimensionPixelSize(var5, this.matchConstraintMinWidth);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max) {
               this.matchConstraintMaxWidth = var15.getDimensionPixelSize(var5, this.matchConstraintMaxWidth);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min) {
               this.matchConstraintMinHeight = var15.getDimensionPixelSize(var5, this.matchConstraintMinHeight);
            } else if (var5 == R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max) {
               this.matchConstraintMaxHeight = var15.getDimensionPixelSize(var5, this.matchConstraintMaxHeight);
            } else if (var5 != R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator && var5 != R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator && var5 != R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator && var5 != R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator) {
               var5 = R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator;
            }
         }

         var15.recycle();
         this.validate();
      }

      public LayoutParams(ConstraintLayout.LayoutParams var1) {
         super(var1);
         this.guideBegin = var1.guideBegin;
         this.guideEnd = var1.guideEnd;
         this.guidePercent = var1.guidePercent;
         this.leftToLeft = var1.leftToLeft;
         this.leftToRight = var1.leftToRight;
         this.rightToLeft = var1.rightToLeft;
         this.rightToRight = var1.rightToRight;
         this.topToTop = var1.topToTop;
         this.topToBottom = var1.topToBottom;
         this.bottomToTop = var1.bottomToTop;
         this.bottomToBottom = var1.bottomToBottom;
         this.baselineToBaseline = var1.baselineToBaseline;
         this.startToEnd = var1.startToEnd;
         this.startToStart = var1.startToStart;
         this.endToStart = var1.endToStart;
         this.endToEnd = var1.endToEnd;
         this.goneLeftMargin = var1.goneLeftMargin;
         this.goneTopMargin = var1.goneTopMargin;
         this.goneRightMargin = var1.goneRightMargin;
         this.goneBottomMargin = var1.goneBottomMargin;
         this.goneStartMargin = var1.goneStartMargin;
         this.goneEndMargin = var1.goneEndMargin;
         this.horizontalBias = var1.horizontalBias;
         this.verticalBias = var1.verticalBias;
         this.dimensionRatio = var1.dimensionRatio;
         this.dimensionRatioValue = var1.dimensionRatioValue;
         this.dimensionRatioSide = var1.dimensionRatioSide;
         this.horizontalWeight = var1.horizontalWeight;
         this.verticalWeight = var1.verticalWeight;
         this.horizontalChainStyle = var1.horizontalChainStyle;
         this.verticalChainStyle = var1.verticalChainStyle;
         this.matchConstraintDefaultWidth = var1.matchConstraintDefaultWidth;
         this.matchConstraintDefaultHeight = var1.matchConstraintDefaultHeight;
         this.matchConstraintMinWidth = var1.matchConstraintMinWidth;
         this.matchConstraintMaxWidth = var1.matchConstraintMaxWidth;
         this.matchConstraintMinHeight = var1.matchConstraintMinHeight;
         this.matchConstraintMaxHeight = var1.matchConstraintMaxHeight;
         this.editorAbsoluteX = var1.editorAbsoluteX;
         this.editorAbsoluteY = var1.editorAbsoluteY;
         this.orientation = var1.orientation;
         this.horizontalDimensionFixed = var1.horizontalDimensionFixed;
         this.verticalDimensionFixed = var1.verticalDimensionFixed;
         this.needsBaseline = var1.needsBaseline;
         this.isGuideline = var1.isGuideline;
         this.resolvedLeftToLeft = var1.resolvedLeftToLeft;
         this.resolvedLeftToRight = var1.resolvedLeftToRight;
         this.resolvedRightToLeft = var1.resolvedRightToLeft;
         this.resolvedRightToRight = var1.resolvedRightToRight;
         this.resolveGoneLeftMargin = var1.resolveGoneLeftMargin;
         this.resolveGoneRightMargin = var1.resolveGoneRightMargin;
         this.resolvedHorizontalBias = var1.resolvedHorizontalBias;
         this.widget = var1.widget;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      @TargetApi(17)
      public void resolveLayoutDirection(int var1) {
         super.resolveLayoutDirection(var1);
         this.resolvedRightToLeft = -1;
         this.resolvedRightToRight = -1;
         this.resolvedLeftToLeft = -1;
         this.resolvedLeftToRight = -1;
         this.resolveGoneLeftMargin = -1;
         this.resolveGoneRightMargin = -1;
         this.resolveGoneLeftMargin = this.goneLeftMargin;
         this.resolveGoneRightMargin = this.goneRightMargin;
         this.resolvedHorizontalBias = this.horizontalBias;
         int var2 = this.getLayoutDirection();
         boolean var3 = true;
         if (1 != var2) {
            var3 = false;
         }

         if (var3) {
            if (this.startToEnd != -1) {
               this.resolvedRightToLeft = this.startToEnd;
            } else if (this.startToStart != -1) {
               this.resolvedRightToRight = this.startToStart;
            }

            if (this.endToStart != -1) {
               this.resolvedLeftToRight = this.endToStart;
            }

            if (this.endToEnd != -1) {
               this.resolvedLeftToLeft = this.endToEnd;
            }

            if (this.goneStartMargin != -1) {
               this.resolveGoneRightMargin = this.goneStartMargin;
            }

            if (this.goneEndMargin != -1) {
               this.resolveGoneLeftMargin = this.goneEndMargin;
            }

            this.resolvedHorizontalBias = 1.0F - this.horizontalBias;
         } else {
            if (this.startToEnd != -1) {
               this.resolvedLeftToRight = this.startToEnd;
            }

            if (this.startToStart != -1) {
               this.resolvedLeftToLeft = this.startToStart;
            }

            if (this.endToStart != -1) {
               this.resolvedRightToLeft = this.endToStart;
            }

            if (this.endToEnd != -1) {
               this.resolvedRightToRight = this.endToEnd;
            }

            if (this.goneStartMargin != -1) {
               this.resolveGoneLeftMargin = this.goneStartMargin;
            }

            if (this.goneEndMargin != -1) {
               this.resolveGoneRightMargin = this.goneEndMargin;
            }
         }

         if (this.endToStart == -1 && this.endToEnd == -1) {
            if (this.rightToLeft != -1) {
               this.resolvedRightToLeft = this.rightToLeft;
            } else if (this.rightToRight != -1) {
               this.resolvedRightToRight = this.rightToRight;
            }
         }

         if (this.startToStart == -1 && this.startToEnd == -1) {
            if (this.leftToLeft != -1) {
               this.resolvedLeftToLeft = this.leftToLeft;
            } else if (this.leftToRight != -1) {
               this.resolvedLeftToRight = this.leftToRight;
            }
         }

      }

      public void validate() {
         this.isGuideline = false;
         this.horizontalDimensionFixed = true;
         this.verticalDimensionFixed = true;
         if (this.width == 0 || this.width == -1) {
            this.horizontalDimensionFixed = false;
         }

         if (this.height == 0 || this.height == -1) {
            this.verticalDimensionFixed = false;
         }

         if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
            this.isGuideline = true;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (!(this.widget instanceof android.support.constraint.solver.widgets.Guideline)) {
               this.widget = new android.support.constraint.solver.widgets.Guideline();
            }

            ((android.support.constraint.solver.widgets.Guideline)this.widget).setOrientation(this.orientation);
         }

      }
   }
}
