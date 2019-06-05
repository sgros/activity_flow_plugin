package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
   SparseArray mChildrenByIds = new SparseArray();
   private ArrayList mConstraintHelpers = new ArrayList(4);
   private ConstraintSet mConstraintSet = null;
   private int mConstraintSetId = -1;
   private HashMap mDesignIds = new HashMap();
   private boolean mDirtyHierarchy = true;
   private int mLastMeasureHeight = -1;
   int mLastMeasureHeightMode = 0;
   int mLastMeasureHeightSize = -1;
   private int mLastMeasureWidth = -1;
   int mLastMeasureWidthMode = 0;
   int mLastMeasureWidthSize = -1;
   ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
   private int mMaxHeight = Integer.MAX_VALUE;
   private int mMaxWidth = Integer.MAX_VALUE;
   private Metrics mMetrics;
   private int mMinHeight = 0;
   private int mMinWidth = 0;
   private int mOptimizationLevel = 3;
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

   private void init(AttributeSet var1) {
      this.mLayoutWidget.setCompanionWidget(this);
      this.mChildrenByIds.put(this.getId(), this);
      this.mConstraintSet = null;
      if (var1 != null) {
         TypedArray var7 = this.getContext().obtainStyledAttributes(var1, R.styleable.ConstraintLayout_Layout);
         int var2 = var7.getIndexCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = var7.getIndex(var3);
            if (var4 == R.styleable.ConstraintLayout_Layout_android_minWidth) {
               this.mMinWidth = var7.getDimensionPixelOffset(var4, this.mMinWidth);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_android_minHeight) {
               this.mMinHeight = var7.getDimensionPixelOffset(var4, this.mMinHeight);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
               this.mMaxWidth = var7.getDimensionPixelOffset(var4, this.mMaxWidth);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
               this.mMaxHeight = var7.getDimensionPixelOffset(var4, this.mMaxHeight);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
               this.mOptimizationLevel = var7.getInt(var4, this.mOptimizationLevel);
            } else if (var4 == R.styleable.ConstraintLayout_Layout_constraintSet) {
               var4 = var7.getResourceId(var4, 0);

               try {
                  ConstraintSet var5 = new ConstraintSet();
                  this.mConstraintSet = var5;
                  this.mConstraintSet.load(this.getContext(), var4);
               } catch (NotFoundException var6) {
                  this.mConstraintSet = null;
               }

               this.mConstraintSetId = var4;
            }
         }

         var7.recycle();
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
            if (!var8.isGuideline && !var8.isHelper) {
               var9.setVisibility(var7.getVisibility());
               int var10 = var8.width;
               int var11 = var8.height;
               boolean var12;
               if (var8.horizontalDimensionFixed || var8.verticalDimensionFixed || !var8.horizontalDimensionFixed && var8.matchConstraintDefaultWidth == 1 || var8.width == -1 || !var8.verticalDimensionFixed && (var8.matchConstraintDefaultHeight == 1 || var8.height == -1)) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               int var13;
               int var14;
               boolean var15;
               if (var12) {
                  if (var10 == 0) {
                     var13 = getChildMeasureSpec(var1, var4, -2);
                     var12 = true;
                  } else if (var10 == -1) {
                     var13 = getChildMeasureSpec(var1, var4, -1);
                     var12 = false;
                  } else {
                     if (var10 == -2) {
                        var12 = true;
                     } else {
                        var12 = false;
                     }

                     var13 = getChildMeasureSpec(var1, var4, var10);
                  }

                  if (var11 == 0) {
                     var14 = getChildMeasureSpec(var2, var3, -2);
                     var15 = true;
                  } else if (var11 == -1) {
                     var14 = getChildMeasureSpec(var2, var3, -1);
                     var15 = false;
                  } else {
                     if (var11 == -2) {
                        var15 = true;
                     } else {
                        var15 = false;
                     }

                     var14 = getChildMeasureSpec(var2, var3, var11);
                  }

                  var7.measure(var13, var14);
                  if (this.mMetrics != null) {
                     Metrics var16 = this.mMetrics;
                     ++var16.measures;
                  }

                  boolean var17;
                  if (var10 == -2) {
                     var17 = true;
                  } else {
                     var17 = false;
                  }

                  var9.setWidthWrapContent(var17);
                  if (var11 == -2) {
                     var17 = true;
                  } else {
                     var17 = false;
                  }

                  var9.setHeightWrapContent(var17);
                  var14 = var7.getMeasuredWidth();
                  var13 = var7.getMeasuredHeight();
               } else {
                  var12 = false;
                  var15 = false;
                  var13 = var11;
                  var14 = var10;
               }

               var9.setWidth(var14);
               var9.setHeight(var13);
               if (var12) {
                  var9.setWrapWidth(var14);
               }

               if (var15) {
                  var9.setWrapHeight(var13);
               }

               if (var8.needsBaseline) {
                  int var18 = var7.getBaseline();
                  if (var18 != -1) {
                     var9.setBaselineDistance(var18);
                  }
               }
            }
         }
      }

   }

   private void internalMeasureDimensions(int var1, int var2) {
      int var3 = this.getPaddingTop() + this.getPaddingBottom();
      int var4 = this.getPaddingLeft() + this.getPaddingRight();
      int var5 = this.getChildCount();
      int var6 = 0;

      while(true) {
         long var7 = 1L;
         int var13;
         boolean var14;
         int var15;
         boolean var18;
         if (var6 >= var5) {
            this.mLayoutWidget.solveGraph();

            for(int var19 = 0; var19 < var5; ++var19) {
               View var36 = this.getChildAt(var19);
               if (var36.getVisibility() != 8) {
                  ConstraintLayout.LayoutParams var29 = (ConstraintLayout.LayoutParams)var36.getLayoutParams();
                  ConstraintWidget var30 = var29.widget;
                  if (!var29.isGuideline && !var29.isHelper) {
                     var30.setVisibility(var36.getVisibility());
                     var13 = var29.width;
                     var15 = var29.height;
                     if (var13 == 0 || var15 == 0) {
                        ResolutionAnchor var20 = var30.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
                        ResolutionAnchor var21 = var30.getAnchor(ConstraintAnchor.Type.RIGHT).getResolutionNode();
                        if (var30.getAnchor(ConstraintAnchor.Type.LEFT).getTarget() != null && var30.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget() != null) {
                           var14 = true;
                        } else {
                           var14 = false;
                        }

                        ResolutionAnchor var22 = var30.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
                        ResolutionAnchor var31 = var30.getAnchor(ConstraintAnchor.Type.BOTTOM).getResolutionNode();
                        boolean var23;
                        if (var30.getAnchor(ConstraintAnchor.Type.TOP).getTarget() != null && var30.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget() != null) {
                           var23 = true;
                        } else {
                           var23 = false;
                        }

                        if (var13 == 0 && var15 == 0 && var14 && var23) {
                           var7 = 1L;
                        } else {
                           boolean var33;
                           if (this.mLayoutWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                              var33 = true;
                           } else {
                              var33 = false;
                           }

                           boolean var28;
                           if (this.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                              var28 = true;
                           } else {
                              var28 = false;
                           }

                           if (!var33) {
                              var30.getResolutionWidth().invalidate();
                           }

                           if (!var28) {
                              var30.getResolutionHeight().invalidate();
                           }

                           boolean var24;
                           int var25;
                           int var35;
                           label267: {
                              if (var13 == 0) {
                                 if (!var33 || !var30.isSpreadWidth() || !var14 || !var20.isResolved() || !var21.isResolved()) {
                                    var35 = getChildMeasureSpec(var1, var4, -2);
                                    var24 = false;
                                    var14 = true;
                                    var25 = var13;
                                    break label267;
                                 }

                                 var13 = (int)(var21.getResolvedValue() - var20.getResolvedValue());
                                 var30.getResolutionWidth().resolve(var13);
                                 var35 = getChildMeasureSpec(var1, var4, var13);
                              } else {
                                 if (var13 != -1) {
                                    if (var13 == -2) {
                                       var14 = true;
                                    } else {
                                       var14 = false;
                                    }

                                    var35 = getChildMeasureSpec(var1, var4, var13);
                                    var25 = var13;
                                    var24 = var33;
                                    break label267;
                                 }

                                 var35 = getChildMeasureSpec(var1, var4, -1);
                              }

                              var14 = false;
                              var24 = var33;
                              var25 = var13;
                           }

                           label200: {
                              if (var15 == 0) {
                                 if (!var28 || !var30.isSpreadHeight() || !var23 || !var22.isResolved() || !var31.isResolved()) {
                                    var13 = getChildMeasureSpec(var2, var3, -2);
                                    var28 = false;
                                    var33 = true;
                                    break label200;
                                 }

                                 var15 = (int)(var31.getResolvedValue() - var22.getResolvedValue());
                                 var30.getResolutionHeight().resolve(var15);
                                 var13 = getChildMeasureSpec(var2, var3, var15);
                              } else {
                                 if (var15 != -1) {
                                    if (var15 == -2) {
                                       var33 = true;
                                    } else {
                                       var33 = false;
                                    }

                                    var13 = getChildMeasureSpec(var2, var3, var15);
                                    break label200;
                                 }

                                 var13 = getChildMeasureSpec(var2, var3, -1);
                              }

                              var33 = false;
                           }

                           var36.measure(var35, var13);
                           if (this.mMetrics != null) {
                              Metrics var32 = this.mMetrics;
                              ++var32.measures;
                           }

                           long var26 = 1L;
                           if (var25 == -2) {
                              var18 = true;
                           } else {
                              var18 = false;
                           }

                           var30.setWidthWrapContent(var18);
                           if (var15 == -2) {
                              var18 = true;
                           } else {
                              var18 = false;
                           }

                           var30.setHeightWrapContent(var18);
                           var35 = var36.getMeasuredWidth();
                           var13 = var36.getMeasuredHeight();
                           var30.setWidth(var35);
                           var30.setHeight(var13);
                           if (var14) {
                              var30.setWrapWidth(var35);
                           }

                           if (var33) {
                              var30.setWrapHeight(var13);
                           }

                           if (var24) {
                              var30.getResolutionWidth().resolve(var35);
                           } else {
                              var30.getResolutionWidth().remove();
                           }

                           if (var28) {
                              var30.getResolutionHeight().resolve(var13);
                           } else {
                              var30.getResolutionHeight().remove();
                           }

                           if (var29.needsBaseline) {
                              var6 = var36.getBaseline();
                              if (var6 != -1) {
                                 var30.setBaselineDistance(var6);
                              }
                           }
                        }
                     }
                  }
               }
            }

            return;
         }

         View var9 = this.getChildAt(var6);
         if (var9.getVisibility() != 8) {
            ConstraintLayout.LayoutParams var10 = (ConstraintLayout.LayoutParams)var9.getLayoutParams();
            ConstraintWidget var11 = var10.widget;
            if (!var10.isGuideline && !var10.isHelper) {
               var11.setVisibility(var9.getVisibility());
               int var12 = var10.width;
               var13 = var10.height;
               if (var12 != 0 && var13 != 0) {
                  if (var12 == -2) {
                     var14 = true;
                  } else {
                     var14 = false;
                  }

                  var15 = getChildMeasureSpec(var1, var4, var12);
                  boolean var16;
                  if (var13 == -2) {
                     var16 = true;
                  } else {
                     var16 = false;
                  }

                  var9.measure(var15, getChildMeasureSpec(var2, var3, var13));
                  if (this.mMetrics != null) {
                     Metrics var17 = this.mMetrics;
                     ++var17.measures;
                  }

                  if (var12 == -2) {
                     var18 = true;
                  } else {
                     var18 = false;
                  }

                  var11.setWidthWrapContent(var18);
                  if (var13 == -2) {
                     var18 = true;
                  } else {
                     var18 = false;
                  }

                  var11.setHeightWrapContent(var18);
                  var12 = var9.getMeasuredWidth();
                  var13 = var9.getMeasuredHeight();
                  var11.setWidth(var12);
                  var11.setHeight(var13);
                  if (var14) {
                     var11.setWrapWidth(var12);
                  }

                  if (var16) {
                     var11.setWrapHeight(var13);
                  }

                  if (var10.needsBaseline) {
                     int var34 = var9.getBaseline();
                     if (var34 != -1) {
                        var11.setBaselineDistance(var34);
                     }
                  }

                  if (var10.horizontalDimensionFixed && var10.verticalDimensionFixed) {
                     var11.getResolutionWidth().resolve(var12);
                     var11.getResolutionHeight().resolve(var13);
                  }
               } else {
                  var11.getResolutionWidth().invalidate();
                  var11.getResolutionHeight().invalidate();
               }
            }
         }

         ++var6;
      }
   }

   private void setChildrenConstraints() {
      boolean var1 = this.isInEditMode();
      int var2 = this.getChildCount();
      byte var3 = 0;
      int var4;
      View var5;
      int var7;
      if (var1) {
         for(var4 = 0; var4 < var2; ++var4) {
            var5 = this.getChildAt(var4);

            String var6;
            boolean var10001;
            try {
               var6 = this.getResources().getResourceName(var5.getId());
               this.setDesignInformation(0, var6, var5.getId());
               var7 = var6.indexOf(47);
            } catch (NotFoundException var20) {
               var10001 = false;
               continue;
            }

            String var8 = var6;
            if (var7 != -1) {
               try {
                  var8 = var6.substring(var7 + 1);
               } catch (NotFoundException var19) {
                  var10001 = false;
                  continue;
               }
            }

            try {
               this.getTargetWidget(var5.getId()).setDebugName(var8);
            } catch (NotFoundException var18) {
               var10001 = false;
            }
         }
      }

      for(var4 = 0; var4 < var2; ++var4) {
         ConstraintWidget var26 = this.getViewWidget(this.getChildAt(var4));
         if (var26 != null) {
            var26.reset();
         }
      }

      View var27;
      if (this.mConstraintSetId != -1) {
         for(var4 = 0; var4 < var2; ++var4) {
            var27 = this.getChildAt(var4);
            if (var27.getId() == this.mConstraintSetId && var27 instanceof Constraints) {
               this.mConstraintSet = ((Constraints)var27).getConstraintSet();
            }
         }
      }

      if (this.mConstraintSet != null) {
         this.mConstraintSet.applyToInternal(this);
      }

      this.mLayoutWidget.removeAllChildren();
      var7 = this.mConstraintHelpers.size();
      if (var7 > 0) {
         for(var4 = 0; var4 < var7; ++var4) {
            ((ConstraintHelper)this.mConstraintHelpers.get(var4)).updatePreLayout(this);
         }
      }

      for(var4 = 0; var4 < var2; ++var4) {
         var27 = this.getChildAt(var4);
         if (var27 instanceof Placeholder) {
            ((Placeholder)var27).updatePreLayout(this);
         }
      }

      var7 = 0;

      byte var9;
      for(byte var25 = var3; var7 < var2; var25 = var9) {
         var5 = this.getChildAt(var7);
         ConstraintWidget var23 = this.getViewWidget(var5);
         if (var23 == null) {
            var9 = var25;
         } else {
            ConstraintLayout.LayoutParams var29 = (ConstraintLayout.LayoutParams)var5.getLayoutParams();
            var29.validate();
            if (var29.helped) {
               var29.helped = (boolean)var25;
            } else if (var1) {
               try {
                  String var10 = this.getResources().getResourceName(var5.getId());
                  this.setDesignInformation(var25, var10, var5.getId());
                  var10 = var10.substring(var10.indexOf("id/") + 3);
                  this.getTargetWidget(var5.getId()).setDebugName(var10);
               } catch (NotFoundException var17) {
               }
            }

            var23.setVisibility(var5.getVisibility());
            if (var29.isInPlaceholder) {
               var23.setVisibility(8);
            }

            var23.setCompanionWidget(var5);
            this.mLayoutWidget.add(var23);
            if (!var29.verticalDimensionFixed || !var29.horizontalDimensionFixed) {
               this.mVariableDimensionsWidgets.add(var23);
            }

            float var11;
            int var21;
            int var28;
            if (var29.isGuideline) {
               android.support.constraint.solver.widgets.Guideline var24 = (android.support.constraint.solver.widgets.Guideline)var23;
               var28 = var29.resolvedGuideBegin;
               var21 = var29.resolvedGuideEnd;
               var11 = var29.resolvedGuidePercent;
               if (VERSION.SDK_INT < 17) {
                  var28 = var29.guideBegin;
                  var21 = var29.guideEnd;
                  var11 = var29.guidePercent;
               }

               if (var11 != -1.0F) {
                  var24.setGuidePercent(var11);
                  var9 = var25;
               } else if (var28 != -1) {
                  var24.setGuideBegin(var28);
                  var9 = var25;
               } else {
                  var9 = var25;
                  if (var21 != -1) {
                     var24.setGuideEnd(var21);
                     var9 = var25;
                  }
               }
            } else {
               label332: {
                  if (var29.leftToLeft == -1 && var29.leftToRight == -1 && var29.rightToLeft == -1 && var29.rightToRight == -1 && var29.startToStart == -1 && var29.startToEnd == -1 && var29.endToStart == -1 && var29.endToEnd == -1 && var29.topToTop == -1 && var29.topToBottom == -1 && var29.bottomToTop == -1 && var29.bottomToBottom == -1 && var29.baselineToBaseline == -1 && var29.editorAbsoluteX == -1 && var29.editorAbsoluteY == -1 && var29.circleConstraint == -1 && var29.width != -1) {
                     var9 = var25;
                     if (var29.height != -1) {
                        break label332;
                     }
                  }

                  int var12 = var29.resolvedLeftToLeft;
                  int var13 = var29.resolvedLeftToRight;
                  var21 = var29.resolvedRightToLeft;
                  var4 = var29.resolvedRightToRight;
                  int var14 = var29.resolveGoneLeftMargin;
                  var28 = var29.resolveGoneRightMargin;
                  var11 = var29.resolvedHorizontalBias;
                  if (VERSION.SDK_INT < 17) {
                     var28 = var29.leftToLeft;
                     var13 = var29.leftToRight;
                     int var15 = var29.rightToLeft;
                     var12 = var29.rightToRight;
                     var14 = var29.goneLeftMargin;
                     int var16 = var29.goneRightMargin;
                     var11 = var29.horizontalBias;
                     var21 = var28;
                     var4 = var13;
                     if (var28 == -1) {
                        var21 = var28;
                        var4 = var13;
                        if (var13 == -1) {
                           if (var29.startToStart != -1) {
                              var21 = var29.startToStart;
                              var4 = var13;
                           } else {
                              var21 = var28;
                              var4 = var13;
                              if (var29.startToEnd != -1) {
                                 var4 = var29.startToEnd;
                                 var21 = var28;
                              }
                           }
                        }
                     }

                     var28 = var21;
                     var13 = var4;
                     var21 = var15;
                     var4 = var12;
                     if (var15 == -1) {
                        var21 = var15;
                        var4 = var12;
                        if (var12 == -1) {
                           if (var29.endToStart != -1) {
                              var21 = var29.endToStart;
                              var4 = var12;
                           } else {
                              var21 = var15;
                              var4 = var12;
                              if (var29.endToEnd != -1) {
                                 var4 = var29.endToEnd;
                                 var21 = var15;
                              }
                           }
                        }
                     }

                     var12 = var28;
                     var28 = var16;
                  }

                  ConstraintWidget var22;
                  if (var29.circleConstraint != -1) {
                     var22 = this.getTargetWidget(var29.circleConstraint);
                     if (var22 != null) {
                        var23.connectCircularConstraint(var22, var29.circleAngle, var29.circleRadius);
                     }
                  } else {
                     if (var12 != -1) {
                        var22 = this.getTargetWidget(var12);
                        if (var22 != null) {
                           var23.immediateConnect(ConstraintAnchor.Type.LEFT, var22, ConstraintAnchor.Type.LEFT, var29.leftMargin, var14);
                        }

                        var12 = var4;
                     } else {
                        var12 = var4;
                        if (var13 != -1) {
                           var22 = this.getTargetWidget(var13);
                           var12 = var4;
                           if (var22 != null) {
                              var23.immediateConnect(ConstraintAnchor.Type.LEFT, var22, ConstraintAnchor.Type.RIGHT, var29.leftMargin, var14);
                              var12 = var4;
                           }
                        }
                     }

                     if (var21 != -1) {
                        var22 = this.getTargetWidget(var21);
                        if (var22 != null) {
                           var23.immediateConnect(ConstraintAnchor.Type.RIGHT, var22, ConstraintAnchor.Type.LEFT, var29.rightMargin, var28);
                        }
                     } else if (var12 != -1) {
                        var22 = this.getTargetWidget(var12);
                        if (var22 != null) {
                           var23.immediateConnect(ConstraintAnchor.Type.RIGHT, var22, ConstraintAnchor.Type.RIGHT, var29.rightMargin, var28);
                        }
                     }

                     if (var29.topToTop != -1) {
                        var22 = this.getTargetWidget(var29.topToTop);
                        if (var22 != null) {
                           var23.immediateConnect(ConstraintAnchor.Type.TOP, var22, ConstraintAnchor.Type.TOP, var29.topMargin, var29.goneTopMargin);
                        }
                     } else if (var29.topToBottom != -1) {
                        var22 = this.getTargetWidget(var29.topToBottom);
                        if (var22 != null) {
                           var23.immediateConnect(ConstraintAnchor.Type.TOP, var22, ConstraintAnchor.Type.BOTTOM, var29.topMargin, var29.goneTopMargin);
                        }
                     }

                     if (var29.bottomToTop != -1) {
                        var22 = this.getTargetWidget(var29.bottomToTop);
                        if (var22 != null) {
                           var23.immediateConnect(ConstraintAnchor.Type.BOTTOM, var22, ConstraintAnchor.Type.TOP, var29.bottomMargin, var29.goneBottomMargin);
                        }
                     } else if (var29.bottomToBottom != -1) {
                        var22 = this.getTargetWidget(var29.bottomToBottom);
                        if (var22 != null) {
                           var23.immediateConnect(ConstraintAnchor.Type.BOTTOM, var22, ConstraintAnchor.Type.BOTTOM, var29.bottomMargin, var29.goneBottomMargin);
                        }
                     }

                     if (var29.baselineToBaseline != -1) {
                        View var30 = (View)this.mChildrenByIds.get(var29.baselineToBaseline);
                        var22 = this.getTargetWidget(var29.baselineToBaseline);
                        if (var22 != null && var30 != null && var30.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
                           ConstraintLayout.LayoutParams var31 = (ConstraintLayout.LayoutParams)var30.getLayoutParams();
                           var29.needsBaseline = true;
                           var31.needsBaseline = true;
                           var23.getAnchor(ConstraintAnchor.Type.BASELINE).connect(var22.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                           var23.getAnchor(ConstraintAnchor.Type.TOP).reset();
                           var23.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                        }
                     }

                     if (var11 >= 0.0F && var11 != 0.5F) {
                        var23.setHorizontalBiasPercent(var11);
                     }

                     if (var29.verticalBias >= 0.0F && var29.verticalBias != 0.5F) {
                        var23.setVerticalBiasPercent(var29.verticalBias);
                     }
                  }

                  if (var1 && (var29.editorAbsoluteX != -1 || var29.editorAbsoluteY != -1)) {
                     var23.setOrigin(var29.editorAbsoluteX, var29.editorAbsoluteY);
                  }

                  if (!var29.horizontalDimensionFixed) {
                     if (var29.width == -1) {
                        var23.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                        var23.getAnchor(ConstraintAnchor.Type.LEFT).mMargin = var29.leftMargin;
                        var23.getAnchor(ConstraintAnchor.Type.RIGHT).mMargin = var29.rightMargin;
                     } else {
                        var23.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                        var23.setWidth(0);
                     }
                  } else {
                     var23.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                     var23.setWidth(var29.width);
                  }

                  if (!var29.verticalDimensionFixed) {
                     if (var29.height == -1) {
                        var23.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                        var23.getAnchor(ConstraintAnchor.Type.TOP).mMargin = var29.topMargin;
                        var23.getAnchor(ConstraintAnchor.Type.BOTTOM).mMargin = var29.bottomMargin;
                     } else {
                        var23.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                        var23.setHeight(0);
                     }
                  } else {
                     var23.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                     var23.setHeight(var29.height);
                  }

                  var9 = 0;
                  if (var29.dimensionRatio != null) {
                     var23.setDimensionRatio(var29.dimensionRatio);
                  }

                  var23.setHorizontalWeight(var29.horizontalWeight);
                  var23.setVerticalWeight(var29.verticalWeight);
                  var23.setHorizontalChainStyle(var29.horizontalChainStyle);
                  var23.setVerticalChainStyle(var29.verticalChainStyle);
                  var23.setHorizontalMatchStyle(var29.matchConstraintDefaultWidth, var29.matchConstraintMinWidth, var29.matchConstraintMaxWidth, var29.matchConstraintPercentWidth);
                  var23.setVerticalMatchStyle(var29.matchConstraintDefaultHeight, var29.matchConstraintMinHeight, var29.matchConstraintMaxHeight, var29.matchConstraintPercentHeight);
               }
            }
         }

         ++var7;
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

   private void updatePostMeasures() {
      int var1 = this.getChildCount();
      byte var2 = 0;

      int var3;
      for(var3 = 0; var3 < var1; ++var3) {
         View var4 = this.getChildAt(var3);
         if (var4 instanceof Placeholder) {
            ((Placeholder)var4).updatePostMeasure(this);
         }
      }

      var1 = this.mConstraintHelpers.size();
      if (var1 > 0) {
         for(var3 = var2; var3 < var1; ++var3) {
            ((ConstraintHelper)this.mConstraintHelpers.get(var3)).updatePostMeasure(this);
         }
      }

   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      super.addView(var1, var2, var3);
      if (VERSION.SDK_INT < 14) {
         this.onViewAdded(var1);
      }

   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof ConstraintLayout.LayoutParams;
   }

   public void dispatchDraw(Canvas var1) {
      super.dispatchDraw(var1);
      if (this.isInEditMode()) {
         int var2 = this.getChildCount();
         float var3 = (float)this.getWidth();
         float var4 = (float)this.getHeight();

         for(int var5 = 0; var5 < var2; ++var5) {
            View var6 = this.getChildAt(var5);
            if (var6.getVisibility() != 8) {
               Object var15 = var6.getTag();
               if (var15 != null && var15 instanceof String) {
                  String[] var16 = ((String)var15).split(",");
                  if (var16.length == 4) {
                     int var7 = Integer.parseInt(var16[0]);
                     int var8 = Integer.parseInt(var16[1]);
                     int var9 = Integer.parseInt(var16[2]);
                     int var10 = Integer.parseInt(var16[3]);
                     var7 = (int)((float)var7 / 1080.0F * var3);
                     var8 = (int)((float)var8 / 1920.0F * var4);
                     var9 = (int)((float)var9 / 1080.0F * var3);
                     var10 = (int)((float)var10 / 1920.0F * var4);
                     Paint var17 = new Paint();
                     var17.setColor(-65536);
                     float var11 = (float)var7;
                     float var12 = (float)var8;
                     float var13 = (float)(var7 + var9);
                     var1.drawLine(var11, var12, var13, var12, var17);
                     float var14 = (float)(var8 + var10);
                     var1.drawLine(var13, var12, var13, var14, var17);
                     var1.drawLine(var13, var14, var11, var14, var17);
                     var1.drawLine(var11, var14, var11, var12, var17);
                     var17.setColor(-16711936);
                     var1.drawLine(var11, var12, var13, var14, var17);
                     var1.drawLine(var11, var14, var13, var12, var17);
                  }
               }
            }
         }
      }

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

   public Object getDesignInformation(int var1, Object var2) {
      if (var1 == 0 && var2 instanceof String) {
         String var3 = (String)var2;
         if (this.mDesignIds != null && this.mDesignIds.containsKey(var3)) {
            return this.mDesignIds.get(var3);
         }
      }

      return null;
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

   public int getOptimizationLevel() {
      return this.mLayoutWidget.getOptimizationLevel();
   }

   public final ConstraintWidget getViewWidget(View var1) {
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

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var4 = this.getChildCount();
      var1 = this.isInEditMode();
      byte var12 = 0;

      for(var2 = 0; var2 < var4; ++var2) {
         View var6 = this.getChildAt(var2);
         ConstraintLayout.LayoutParams var7 = (ConstraintLayout.LayoutParams)var6.getLayoutParams();
         ConstraintWidget var8 = var7.widget;
         if ((var6.getVisibility() != 8 || var7.isGuideline || var7.isHelper || var1) && !var7.isInPlaceholder) {
            int var9 = var8.getDrawX();
            var5 = var8.getDrawY();
            int var10 = var8.getWidth() + var9;
            int var11 = var8.getHeight() + var5;
            var6.layout(var9, var5, var10, var11);
            if (var6 instanceof Placeholder) {
               View var13 = ((Placeholder)var6).getContent();
               if (var13 != null) {
                  var13.setVisibility(0);
                  var13.layout(var9, var5, var10, var11);
               }
            }
         }
      }

      var4 = this.mConstraintHelpers.size();
      if (var4 > 0) {
         for(var2 = var12; var2 < var4; ++var2) {
            ((ConstraintHelper)this.mConstraintHelpers.get(var2)).updatePostLayout(this);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      System.currentTimeMillis();
      int var3 = MeasureSpec.getMode(var1);
      int var4 = MeasureSpec.getSize(var1);
      int var5 = MeasureSpec.getMode(var2);
      int var6 = MeasureSpec.getSize(var2);
      int var7;
      if (this.mLastMeasureWidth != -1) {
         var7 = this.mLastMeasureHeight;
      }

      if (var3 == 1073741824 && var5 == 1073741824 && var4 == this.mLastMeasureWidth) {
         var7 = this.mLastMeasureHeight;
      }

      boolean var27;
      if (var3 == this.mLastMeasureWidthMode && var5 == this.mLastMeasureHeightMode) {
         var27 = true;
      } else {
         var27 = false;
      }

      int var8;
      if (var27 && var4 == this.mLastMeasureWidthSize) {
         var8 = this.mLastMeasureHeightSize;
      }

      if (var27 && var3 == Integer.MIN_VALUE && var5 == 1073741824 && var4 >= this.mLastMeasureWidth) {
         var8 = this.mLastMeasureHeight;
      }

      if (var27 && var3 == 1073741824 && var5 == Integer.MIN_VALUE && var4 == this.mLastMeasureWidth) {
         var7 = this.mLastMeasureHeight;
      }

      this.mLastMeasureWidthMode = var3;
      this.mLastMeasureHeightMode = var5;
      this.mLastMeasureWidthSize = var4;
      this.mLastMeasureHeightSize = var6;
      var7 = this.getPaddingLeft();
      var4 = this.getPaddingTop();
      this.mLayoutWidget.setX(var7);
      this.mLayoutWidget.setY(var4);
      this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
      this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
      if (VERSION.SDK_INT >= 17) {
         ConstraintWidgetContainer var9 = this.mLayoutWidget;
         boolean var10;
         if (this.getLayoutDirection() == 1) {
            var10 = true;
         } else {
            var10 = false;
         }

         var9.setRtl(var10);
      }

      this.setSelfDimensionBehaviour(var1, var2);
      int var11 = this.mLayoutWidget.getWidth();
      int var12 = this.mLayoutWidget.getHeight();
      if (this.mDirtyHierarchy) {
         this.mDirtyHierarchy = false;
         this.updateHierarchy();
      }

      boolean var29;
      if ((this.mOptimizationLevel & 8) == 8) {
         var29 = true;
      } else {
         var29 = false;
      }

      if (var29) {
         this.mLayoutWidget.preOptimize();
         this.mLayoutWidget.optimizeForDimensions(var11, var12);
         this.internalMeasureDimensions(var1, var2);
      } else {
         this.internalMeasureChildren(var1, var2);
      }

      this.updatePostMeasures();
      if (this.getChildCount() > 0) {
         this.solveLinearSystem("First pass");
      }

      int var13 = this.mVariableDimensionsWidgets.size();
      int var14 = var4 + this.getPaddingBottom();
      int var15 = var7 + this.getPaddingRight();
      if (var13 > 0) {
         boolean var16;
         if (this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var16 = true;
         } else {
            var16 = false;
         }

         boolean var17;
         if (this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var17 = true;
         } else {
            var17 = false;
         }

         var3 = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
         var7 = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
         int var18 = 0;
         boolean var25 = false;

         View var19;
         ConstraintWidget var30;
         for(var4 = 0; var18 < var13; var3 = var6) {
            boolean var24;
            label224: {
               var30 = (ConstraintWidget)this.mVariableDimensionsWidgets.get(var18);
               var19 = (View)var30.getCompanionWidget();
               if (var19 != null) {
                  ConstraintLayout.LayoutParams var20 = (ConstraintLayout.LayoutParams)var19.getLayoutParams();
                  if (!var20.isHelper && !var20.isGuideline && var19.getVisibility() != 8 && (!var29 || !var30.getResolutionWidth().isResolved() || !var30.getResolutionHeight().isResolved())) {
                     if (var20.width == -2 && var20.horizontalDimensionFixed) {
                        var6 = getChildMeasureSpec(var1, var15, var20.width);
                     } else {
                        var6 = MeasureSpec.makeMeasureSpec(var30.getWidth(), 1073741824);
                     }

                     int var21;
                     if (var20.height == -2 && var20.verticalDimensionFixed) {
                        var21 = getChildMeasureSpec(var2, var14, var20.height);
                     } else {
                        var21 = MeasureSpec.makeMeasureSpec(var30.getHeight(), 1073741824);
                     }

                     var19.measure(var6, var21);
                     if (this.mMetrics != null) {
                        Metrics var22 = this.mMetrics;
                        ++var22.additionalMeasures;
                     }

                     int var23 = var19.getMeasuredWidth();
                     var21 = var19.getMeasuredHeight();
                     boolean var28 = var25;
                     var5 = var3;
                     if (var23 != var30.getWidth()) {
                        var30.setWidth(var23);
                        if (var29) {
                           var30.getResolutionWidth().resolve(var23);
                        }

                        var5 = var3;
                        if (var16) {
                           var5 = var3;
                           if (var30.getRight() > var3) {
                              var5 = Math.max(var3, var30.getRight() + var30.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                           }
                        }

                        var28 = true;
                     }

                     if (var21 != var30.getHeight()) {
                        var30.setHeight(var21);
                        if (var29) {
                           var30.getResolutionHeight().resolve(var21);
                        }

                        if (var17) {
                           var6 = var30.getBottom();
                           if (var6 > var7) {
                              var7 = Math.max(var7, var30.getBottom() + var30.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                           }
                        }

                        var28 = true;
                     }

                     if (var20.needsBaseline) {
                        var21 = var19.getBaseline();
                        var24 = var28;
                        if (var21 != -1) {
                           var24 = var28;
                           if (var21 != var30.getBaselineDistance()) {
                              var30.setBaselineDistance(var21);
                              var24 = true;
                           }
                        }
                     } else {
                        var24 = var28;
                     }

                     if (VERSION.SDK_INT >= 11) {
                        var4 = combineMeasuredStates(var4, var19.getMeasuredState());
                        var6 = var5;
                     } else {
                        var6 = var5;
                     }
                     break label224;
                  }
               }

               var6 = var3;
               var24 = var25;
            }

            ++var18;
            var25 = var24;
         }

         var6 = var4;
         if (var25) {
            this.mLayoutWidget.setWidth(var11);
            this.mLayoutWidget.setHeight(var12);
            if (var29) {
               this.mLayoutWidget.solveGraph();
            }

            this.solveLinearSystem("2nd pass");
            boolean var26;
            if (this.mLayoutWidget.getWidth() < var3) {
               this.mLayoutWidget.setWidth(var3);
               var26 = true;
            } else {
               var26 = false;
            }

            if (this.mLayoutWidget.getHeight() < var7) {
               this.mLayoutWidget.setHeight(var7);
               var27 = true;
            } else {
               var27 = var26;
            }

            if (var27) {
               this.solveLinearSystem("3rd pass");
            }
         }

         var4 = 0;

         while(true) {
            var7 = var6;
            if (var4 >= var13) {
               break;
            }

            var30 = (ConstraintWidget)this.mVariableDimensionsWidgets.get(var4);
            var19 = (View)var30.getCompanionWidget();
            if (var19 != null && (var19.getMeasuredWidth() != var30.getWidth() || var19.getMeasuredHeight() != var30.getHeight()) && var30.getVisibility() != 8) {
               var19.measure(MeasureSpec.makeMeasureSpec(var30.getWidth(), 1073741824), MeasureSpec.makeMeasureSpec(var30.getHeight(), 1073741824));
               if (this.mMetrics != null) {
                  Metrics var31 = this.mMetrics;
                  ++var31.additionalMeasures;
               }
            }

            ++var4;
         }
      } else {
         var7 = 0;
      }

      var3 = this.mLayoutWidget.getWidth() + var15;
      var4 = this.mLayoutWidget.getHeight() + var14;
      if (VERSION.SDK_INT >= 11) {
         var1 = resolveSizeAndState(var3, var1, var7);
         var7 = resolveSizeAndState(var4, var2, var7 << 16);
         var2 = Math.min(this.mMaxWidth, var1 & 16777215);
         var7 = Math.min(this.mMaxHeight, var7 & 16777215);
         var1 = var2;
         if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
            var1 = var2 | 16777216;
         }

         var2 = var7;
         if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
            var2 = var7 | 16777216;
         }

         this.setMeasuredDimension(var1, var2);
         this.mLastMeasureWidth = var1;
         this.mLastMeasureHeight = var2;
      } else {
         this.setMeasuredDimension(var3, var4);
         this.mLastMeasureWidth = var3;
         this.mLastMeasureHeight = var4;
      }

   }

   public void onViewAdded(View var1) {
      if (VERSION.SDK_INT >= 14) {
         super.onViewAdded(var1);
      }

      ConstraintWidget var2 = this.getViewWidget(var1);
      if (var1 instanceof Guideline && !(var2 instanceof android.support.constraint.solver.widgets.Guideline)) {
         ConstraintLayout.LayoutParams var3 = (ConstraintLayout.LayoutParams)var1.getLayoutParams();
         var3.widget = new android.support.constraint.solver.widgets.Guideline();
         var3.isGuideline = true;
         ((android.support.constraint.solver.widgets.Guideline)var3.widget).setOrientation(var3.orientation);
      }

      if (var1 instanceof ConstraintHelper) {
         ConstraintHelper var4 = (ConstraintHelper)var1;
         var4.validateParams();
         ((ConstraintLayout.LayoutParams)var1.getLayoutParams()).isHelper = true;
         if (!this.mConstraintHelpers.contains(var4)) {
            this.mConstraintHelpers.add(var4);
         }
      }

      this.mChildrenByIds.put(var1.getId(), var1);
      this.mDirtyHierarchy = true;
   }

   public void onViewRemoved(View var1) {
      if (VERSION.SDK_INT >= 14) {
         super.onViewRemoved(var1);
      }

      this.mChildrenByIds.remove(var1.getId());
      ConstraintWidget var2 = this.getViewWidget(var1);
      this.mLayoutWidget.remove(var2);
      this.mConstraintHelpers.remove(var1);
      this.mVariableDimensionsWidgets.remove(var2);
      this.mDirtyHierarchy = true;
   }

   public void removeView(View var1) {
      super.removeView(var1);
      if (VERSION.SDK_INT < 14) {
         this.onViewRemoved(var1);
      }

   }

   public void requestLayout() {
      super.requestLayout();
      this.mDirtyHierarchy = true;
      this.mLastMeasureWidth = -1;
      this.mLastMeasureHeight = -1;
      this.mLastMeasureWidthSize = -1;
      this.mLastMeasureHeightSize = -1;
      this.mLastMeasureWidthMode = 0;
      this.mLastMeasureHeightMode = 0;
   }

   public void setConstraintSet(ConstraintSet var1) {
      this.mConstraintSet = var1;
   }

   public void setDesignInformation(int var1, Object var2, Object var3) {
      if (var1 == 0 && var2 instanceof String && var3 instanceof Integer) {
         if (this.mDesignIds == null) {
            this.mDesignIds = new HashMap();
         }

         String var4 = (String)var2;
         var1 = var4.indexOf("/");
         String var5 = var4;
         if (var1 != -1) {
            var5 = var4.substring(var1 + 1);
         }

         var1 = (Integer)var3;
         this.mDesignIds.put(var5, var1);
      }

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

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   protected void solveLinearSystem(String var1) {
      this.mLayoutWidget.layout();
      if (this.mMetrics != null) {
         Metrics var2 = this.mMetrics;
         ++var2.resolutions;
      }

   }

   public static class LayoutParams extends MarginLayoutParams {
      public int baselineToBaseline = -1;
      public int bottomToBottom = -1;
      public int bottomToTop = -1;
      public float circleAngle = 0.0F;
      public int circleConstraint = -1;
      public int circleRadius = 0;
      public boolean constrainedHeight = false;
      public boolean constrainedWidth = false;
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
      public boolean helped = false;
      public float horizontalBias = 0.5F;
      public int horizontalChainStyle = 0;
      boolean horizontalDimensionFixed = true;
      public float horizontalWeight = -1.0F;
      boolean isGuideline = false;
      boolean isHelper = false;
      boolean isInPlaceholder = false;
      public int leftToLeft = -1;
      public int leftToRight = -1;
      public int matchConstraintDefaultHeight = 0;
      public int matchConstraintDefaultWidth = 0;
      public int matchConstraintMaxHeight = 0;
      public int matchConstraintMaxWidth = 0;
      public int matchConstraintMinHeight = 0;
      public int matchConstraintMinWidth = 0;
      public float matchConstraintPercentHeight = 1.0F;
      public float matchConstraintPercentWidth = 1.0F;
      boolean needsBaseline = false;
      public int orientation = -1;
      int resolveGoneLeftMargin = -1;
      int resolveGoneRightMargin = -1;
      int resolvedGuideBegin;
      int resolvedGuideEnd;
      float resolvedGuidePercent;
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
      public float verticalWeight = -1.0F;
      ConstraintWidget widget = new ConstraintWidget();

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var19 = var1.obtainStyledAttributes(var2, R.styleable.ConstraintLayout_Layout);
         int var3 = var19.getIndexCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = var19.getIndex(var4);
            switch(ConstraintLayout.LayoutParams.Table.map.get(var5)) {
            case 0:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            default:
               break;
            case 1:
               this.orientation = var19.getInt(var5, this.orientation);
               break;
            case 2:
               this.circleConstraint = var19.getResourceId(var5, this.circleConstraint);
               if (this.circleConstraint == -1) {
                  this.circleConstraint = var19.getInt(var5, -1);
               }
               break;
            case 3:
               this.circleRadius = var19.getDimensionPixelSize(var5, this.circleRadius);
               break;
            case 4:
               this.circleAngle = var19.getFloat(var5, this.circleAngle) % 360.0F;
               if (this.circleAngle < 0.0F) {
                  this.circleAngle = (360.0F - this.circleAngle) % 360.0F;
               }
               break;
            case 5:
               this.guideBegin = var19.getDimensionPixelOffset(var5, this.guideBegin);
               break;
            case 6:
               this.guideEnd = var19.getDimensionPixelOffset(var5, this.guideEnd);
               break;
            case 7:
               this.guidePercent = var19.getFloat(var5, this.guidePercent);
               break;
            case 8:
               this.leftToLeft = var19.getResourceId(var5, this.leftToLeft);
               if (this.leftToLeft == -1) {
                  this.leftToLeft = var19.getInt(var5, -1);
               }
               break;
            case 9:
               this.leftToRight = var19.getResourceId(var5, this.leftToRight);
               if (this.leftToRight == -1) {
                  this.leftToRight = var19.getInt(var5, -1);
               }
               break;
            case 10:
               this.rightToLeft = var19.getResourceId(var5, this.rightToLeft);
               if (this.rightToLeft == -1) {
                  this.rightToLeft = var19.getInt(var5, -1);
               }
               break;
            case 11:
               this.rightToRight = var19.getResourceId(var5, this.rightToRight);
               if (this.rightToRight == -1) {
                  this.rightToRight = var19.getInt(var5, -1);
               }
               break;
            case 12:
               this.topToTop = var19.getResourceId(var5, this.topToTop);
               if (this.topToTop == -1) {
                  this.topToTop = var19.getInt(var5, -1);
               }
               break;
            case 13:
               this.topToBottom = var19.getResourceId(var5, this.topToBottom);
               if (this.topToBottom == -1) {
                  this.topToBottom = var19.getInt(var5, -1);
               }
               break;
            case 14:
               this.bottomToTop = var19.getResourceId(var5, this.bottomToTop);
               if (this.bottomToTop == -1) {
                  this.bottomToTop = var19.getInt(var5, -1);
               }
               break;
            case 15:
               this.bottomToBottom = var19.getResourceId(var5, this.bottomToBottom);
               if (this.bottomToBottom == -1) {
                  this.bottomToBottom = var19.getInt(var5, -1);
               }
               break;
            case 16:
               this.baselineToBaseline = var19.getResourceId(var5, this.baselineToBaseline);
               if (this.baselineToBaseline == -1) {
                  this.baselineToBaseline = var19.getInt(var5, -1);
               }
               break;
            case 17:
               this.startToEnd = var19.getResourceId(var5, this.startToEnd);
               if (this.startToEnd == -1) {
                  this.startToEnd = var19.getInt(var5, -1);
               }
               break;
            case 18:
               this.startToStart = var19.getResourceId(var5, this.startToStart);
               if (this.startToStart == -1) {
                  this.startToStart = var19.getInt(var5, -1);
               }
               break;
            case 19:
               this.endToStart = var19.getResourceId(var5, this.endToStart);
               if (this.endToStart == -1) {
                  this.endToStart = var19.getInt(var5, -1);
               }
               break;
            case 20:
               this.endToEnd = var19.getResourceId(var5, this.endToEnd);
               if (this.endToEnd == -1) {
                  this.endToEnd = var19.getInt(var5, -1);
               }
               break;
            case 21:
               this.goneLeftMargin = var19.getDimensionPixelSize(var5, this.goneLeftMargin);
               break;
            case 22:
               this.goneTopMargin = var19.getDimensionPixelSize(var5, this.goneTopMargin);
               break;
            case 23:
               this.goneRightMargin = var19.getDimensionPixelSize(var5, this.goneRightMargin);
               break;
            case 24:
               this.goneBottomMargin = var19.getDimensionPixelSize(var5, this.goneBottomMargin);
               break;
            case 25:
               this.goneStartMargin = var19.getDimensionPixelSize(var5, this.goneStartMargin);
               break;
            case 26:
               this.goneEndMargin = var19.getDimensionPixelSize(var5, this.goneEndMargin);
               break;
            case 27:
               this.constrainedWidth = var19.getBoolean(var5, this.constrainedWidth);
               break;
            case 28:
               this.constrainedHeight = var19.getBoolean(var5, this.constrainedHeight);
               break;
            case 29:
               this.horizontalBias = var19.getFloat(var5, this.horizontalBias);
               break;
            case 30:
               this.verticalBias = var19.getFloat(var5, this.verticalBias);
               break;
            case 31:
               this.matchConstraintDefaultWidth = var19.getInt(var5, 0);
               if (this.matchConstraintDefaultWidth == 1) {
                  Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
               }
               break;
            case 32:
               this.matchConstraintDefaultHeight = var19.getInt(var5, 0);
               if (this.matchConstraintDefaultHeight == 1) {
                  Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
               }
               break;
            case 33:
               try {
                  this.matchConstraintMinWidth = var19.getDimensionPixelSize(var5, this.matchConstraintMinWidth);
               } catch (Exception var14) {
                  if (var19.getInt(var5, this.matchConstraintMinWidth) == -2) {
                     this.matchConstraintMinWidth = -2;
                  }
               }
               break;
            case 34:
               try {
                  this.matchConstraintMaxWidth = var19.getDimensionPixelSize(var5, this.matchConstraintMaxWidth);
               } catch (Exception var13) {
                  if (var19.getInt(var5, this.matchConstraintMaxWidth) == -2) {
                     this.matchConstraintMaxWidth = -2;
                  }
               }
               break;
            case 35:
               this.matchConstraintPercentWidth = Math.max(0.0F, var19.getFloat(var5, this.matchConstraintPercentWidth));
               break;
            case 36:
               try {
                  this.matchConstraintMinHeight = var19.getDimensionPixelSize(var5, this.matchConstraintMinHeight);
               } catch (Exception var12) {
                  if (var19.getInt(var5, this.matchConstraintMinHeight) == -2) {
                     this.matchConstraintMinHeight = -2;
                  }
               }
               break;
            case 37:
               try {
                  this.matchConstraintMaxHeight = var19.getDimensionPixelSize(var5, this.matchConstraintMaxHeight);
               } catch (Exception var11) {
                  if (var19.getInt(var5, this.matchConstraintMaxHeight) == -2) {
                     this.matchConstraintMaxHeight = -2;
                  }
               }
               break;
            case 38:
               this.matchConstraintPercentHeight = Math.max(0.0F, var19.getFloat(var5, this.matchConstraintPercentHeight));
               break;
            case 44:
               this.dimensionRatio = var19.getString(var5);
               this.dimensionRatioValue = Float.NaN;
               this.dimensionRatioSide = -1;
               if (this.dimensionRatio == null) {
                  break;
               }

               int var6 = this.dimensionRatio.length();
               var5 = this.dimensionRatio.indexOf(44);
               String var20;
               if (var5 > 0 && var5 < var6 - 1) {
                  var20 = this.dimensionRatio.substring(0, var5);
                  if (var20.equalsIgnoreCase("W")) {
                     this.dimensionRatioSide = 0;
                  } else if (var20.equalsIgnoreCase("H")) {
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
                  var20 = this.dimensionRatio.substring(var7 + 1);
                  if (var8.length() > 0 && var20.length() > 0) {
                     float var9;
                     float var10;
                     try {
                        var9 = Float.parseFloat(var8);
                        var10 = Float.parseFloat(var20);
                     } catch (NumberFormatException var17) {
                        var10001 = false;
                        break;
                     }

                     if (var9 > 0.0F && var10 > 0.0F) {
                        try {
                           if (this.dimensionRatioSide == 1) {
                              this.dimensionRatioValue = Math.abs(var10 / var9);
                              break;
                           }
                        } catch (NumberFormatException var18) {
                           var10001 = false;
                           break;
                        }

                        try {
                           this.dimensionRatioValue = Math.abs(var9 / var10);
                        } catch (NumberFormatException var15) {
                           var10001 = false;
                        }
                     }
                  }
               } else {
                  var20 = this.dimensionRatio.substring(var5);
                  if (var20.length() > 0) {
                     try {
                        this.dimensionRatioValue = Float.parseFloat(var20);
                     } catch (NumberFormatException var16) {
                        var10001 = false;
                     }
                  }
               }
               break;
            case 45:
               this.horizontalWeight = var19.getFloat(var5, this.horizontalWeight);
               break;
            case 46:
               this.verticalWeight = var19.getFloat(var5, this.verticalWeight);
               break;
            case 47:
               this.horizontalChainStyle = var19.getInt(var5, 0);
               break;
            case 48:
               this.verticalChainStyle = var19.getInt(var5, 0);
               break;
            case 49:
               this.editorAbsoluteX = var19.getDimensionPixelOffset(var5, this.editorAbsoluteX);
               break;
            case 50:
               this.editorAbsoluteY = var19.getDimensionPixelOffset(var5, this.editorAbsoluteY);
            }
         }

         var19.recycle();
         this.validate();
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      @TargetApi(17)
      public void resolveLayoutDirection(int var1) {
         int var2 = this.leftMargin;
         int var3 = this.rightMargin;
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
         this.resolvedGuideBegin = this.guideBegin;
         this.resolvedGuideEnd = this.guideEnd;
         this.resolvedGuidePercent = this.guidePercent;
         var1 = this.getLayoutDirection();
         boolean var4 = false;
         boolean var5;
         if (1 == var1) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var5) {
            label113: {
               if (this.startToEnd != -1) {
                  this.resolvedRightToLeft = this.startToEnd;
               } else {
                  var5 = var4;
                  if (this.startToStart == -1) {
                     break label113;
                  }

                  this.resolvedRightToRight = this.startToStart;
               }

               var5 = true;
            }

            if (this.endToStart != -1) {
               this.resolvedLeftToRight = this.endToStart;
               var5 = true;
            }

            if (this.endToEnd != -1) {
               this.resolvedLeftToLeft = this.endToEnd;
               var5 = true;
            }

            if (this.goneStartMargin != -1) {
               this.resolveGoneRightMargin = this.goneStartMargin;
            }

            if (this.goneEndMargin != -1) {
               this.resolveGoneLeftMargin = this.goneEndMargin;
            }

            if (var5) {
               this.resolvedHorizontalBias = 1.0F - this.horizontalBias;
            }

            if (this.isGuideline && this.orientation == 1) {
               if (this.guidePercent != -1.0F) {
                  this.resolvedGuidePercent = 1.0F - this.guidePercent;
                  this.resolvedGuideBegin = -1;
                  this.resolvedGuideEnd = -1;
               } else if (this.guideBegin != -1) {
                  this.resolvedGuideEnd = this.guideBegin;
                  this.resolvedGuideBegin = -1;
                  this.resolvedGuidePercent = -1.0F;
               } else if (this.guideEnd != -1) {
                  this.resolvedGuideBegin = this.guideEnd;
                  this.resolvedGuideEnd = -1;
                  this.resolvedGuidePercent = -1.0F;
               }
            }
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

         if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
            if (this.rightToLeft != -1) {
               this.resolvedRightToLeft = this.rightToLeft;
               if (this.rightMargin <= 0 && var3 > 0) {
                  this.rightMargin = var3;
               }
            } else if (this.rightToRight != -1) {
               this.resolvedRightToRight = this.rightToRight;
               if (this.rightMargin <= 0 && var3 > 0) {
                  this.rightMargin = var3;
               }
            }

            if (this.leftToLeft != -1) {
               this.resolvedLeftToLeft = this.leftToLeft;
               if (this.leftMargin <= 0 && var2 > 0) {
                  this.leftMargin = var2;
               }
            } else if (this.leftToRight != -1) {
               this.resolvedLeftToRight = this.leftToRight;
               if (this.leftMargin <= 0 && var2 > 0) {
                  this.leftMargin = var2;
               }
            }
         }

      }

      public void validate() {
         this.isGuideline = false;
         this.horizontalDimensionFixed = true;
         this.verticalDimensionFixed = true;
         if (this.width == -2 && this.constrainedWidth) {
            this.horizontalDimensionFixed = false;
            this.matchConstraintDefaultWidth = 1;
         }

         if (this.height == -2 && this.constrainedHeight) {
            this.verticalDimensionFixed = false;
            this.matchConstraintDefaultHeight = 1;
         }

         if (this.width == 0 || this.width == -1) {
            this.horizontalDimensionFixed = false;
            if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
               this.width = -2;
               this.constrainedWidth = true;
            }
         }

         if (this.height == 0 || this.height == -1) {
            this.verticalDimensionFixed = false;
            if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
               this.height = -2;
               this.constrainedHeight = true;
            }
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

      private static class Table {
         public static final SparseIntArray map = new SparseIntArray();

         static {
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
            map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
            map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
            map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
            map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
            map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
            map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
            map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
            map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
            map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
            map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
         }
      }
   }
}
