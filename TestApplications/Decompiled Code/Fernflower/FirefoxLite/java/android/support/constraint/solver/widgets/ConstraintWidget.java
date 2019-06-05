package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class ConstraintWidget {
   public static float DEFAULT_BIAS;
   protected ArrayList mAnchors;
   ConstraintAnchor mBaseline;
   int mBaselineDistance;
   ConstraintAnchor mBottom;
   ConstraintAnchor mCenter;
   ConstraintAnchor mCenterX;
   ConstraintAnchor mCenterY;
   private float mCircleConstraintAngle = 0.0F;
   private Object mCompanionWidget;
   private int mContainerItemSkip;
   private String mDebugName;
   protected float mDimensionRatio;
   protected int mDimensionRatioSide;
   private int mDrawHeight;
   private int mDrawWidth;
   private int mDrawX;
   private int mDrawY;
   int mHeight;
   float mHorizontalBiasPercent;
   boolean mHorizontalChainFixedPosition;
   int mHorizontalChainStyle;
   ConstraintWidget mHorizontalNextWidget;
   public int mHorizontalResolution = -1;
   boolean mHorizontalWrapVisited;
   boolean mIsHeightWrapContent;
   boolean mIsWidthWrapContent;
   ConstraintAnchor mLeft;
   protected ConstraintAnchor[] mListAnchors;
   protected ConstraintWidget.DimensionBehaviour[] mListDimensionBehaviors;
   protected ConstraintWidget[] mListNextMatchConstraintsWidget;
   protected ConstraintWidget[] mListNextVisibleWidget;
   int mMatchConstraintDefaultHeight = 0;
   int mMatchConstraintDefaultWidth = 0;
   int mMatchConstraintMaxHeight = 0;
   int mMatchConstraintMaxWidth = 0;
   int mMatchConstraintMinHeight = 0;
   int mMatchConstraintMinWidth = 0;
   float mMatchConstraintPercentHeight = 1.0F;
   float mMatchConstraintPercentWidth = 1.0F;
   private int[] mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
   protected int mMinHeight;
   protected int mMinWidth;
   protected int mOffsetX;
   protected int mOffsetY;
   ConstraintWidget mParent;
   ResolutionDimension mResolutionHeight;
   ResolutionDimension mResolutionWidth;
   float mResolvedDimensionRatio = 1.0F;
   int mResolvedDimensionRatioSide = -1;
   int[] mResolvedMatchConstraintDefault = new int[2];
   ConstraintAnchor mRight;
   ConstraintAnchor mTop;
   private String mType;
   float mVerticalBiasPercent;
   boolean mVerticalChainFixedPosition;
   int mVerticalChainStyle;
   ConstraintWidget mVerticalNextWidget;
   public int mVerticalResolution = -1;
   boolean mVerticalWrapVisited;
   private int mVisibility;
   float[] mWeight;
   int mWidth;
   private int mWrapHeight;
   private int mWrapWidth;
   protected int mX;
   protected int mY;

   public ConstraintWidget() {
      this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
      this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
      this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
      this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
      this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
      this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
      this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
      this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
      this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
      this.mAnchors = new ArrayList();
      this.mListDimensionBehaviors = new ConstraintWidget.DimensionBehaviour[]{ConstraintWidget.DimensionBehaviour.FIXED, ConstraintWidget.DimensionBehaviour.FIXED};
      this.mParent = null;
      this.mWidth = 0;
      this.mHeight = 0;
      this.mDimensionRatio = 0.0F;
      this.mDimensionRatioSide = -1;
      this.mX = 0;
      this.mY = 0;
      this.mDrawX = 0;
      this.mDrawY = 0;
      this.mDrawWidth = 0;
      this.mDrawHeight = 0;
      this.mOffsetX = 0;
      this.mOffsetY = 0;
      this.mBaselineDistance = 0;
      this.mHorizontalBiasPercent = DEFAULT_BIAS;
      this.mVerticalBiasPercent = DEFAULT_BIAS;
      this.mContainerItemSkip = 0;
      this.mVisibility = 0;
      this.mDebugName = null;
      this.mType = null;
      this.mHorizontalChainStyle = 0;
      this.mVerticalChainStyle = 0;
      this.mWeight = new float[]{-1.0F, -1.0F};
      this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
      this.mListNextVisibleWidget = new ConstraintWidget[]{null, null};
      this.mHorizontalNextWidget = null;
      this.mVerticalNextWidget = null;
      this.addAnchors();
   }

   private void addAnchors() {
      this.mAnchors.add(this.mLeft);
      this.mAnchors.add(this.mTop);
      this.mAnchors.add(this.mRight);
      this.mAnchors.add(this.mBottom);
      this.mAnchors.add(this.mCenterX);
      this.mAnchors.add(this.mCenterY);
      this.mAnchors.add(this.mCenter);
      this.mAnchors.add(this.mBaseline);
   }

   private void applyConstraints(LinearSystem var1, boolean var2, SolverVariable var3, SolverVariable var4, ConstraintWidget.DimensionBehaviour var5, boolean var6, ConstraintAnchor var7, ConstraintAnchor var8, int var9, int var10, int var11, int var12, float var13, boolean var14, boolean var15, int var16, int var17, int var18, float var19, boolean var20) {
      SolverVariable var21 = var1.createObjectVariable(var7);
      SolverVariable var22 = var1.createObjectVariable(var8);
      SolverVariable var23 = var1.createObjectVariable(var7.getTarget());
      SolverVariable var24 = var1.createObjectVariable(var8.getTarget());
      if (var1.graphOptimizer && var7.getResolutionNode().state == 1 && var8.getResolutionNode().state == 1) {
         if (LinearSystem.getMetrics() != null) {
            Metrics var32 = LinearSystem.getMetrics();
            ++var32.resolvedWidgets;
         }

         var7.getResolutionNode().addResolvedValue(var1);
         var8.getResolutionNode().addResolvedValue(var1);
         if (!var15 && var2) {
            var1.addGreaterThan(var4, var22, 0, 6);
         }

      } else {
         if (LinearSystem.getMetrics() != null) {
            Metrics var25 = LinearSystem.getMetrics();
            ++var25.nonresolvedWidgets;
         }

         boolean var26 = var7.isConnected();
         boolean var27 = var8.isConnected();
         boolean var28 = this.mCenter.isConnected();
         byte var29;
         if (var26) {
            var29 = 1;
         } else {
            var29 = 0;
         }

         int var30 = var29;
         if (var27) {
            var30 = var29 + 1;
         }

         int var42 = var30;
         if (var28) {
            var42 = var30 + 1;
         }

         if (var14) {
            var30 = 3;
         } else {
            var30 = var16;
         }

         boolean var40;
         switch(var5) {
         case MATCH_CONSTRAINT:
            if (var30 != 4) {
               var40 = true;
               break;
            }
         case FIXED:
         case WRAP_CONTENT:
         case MATCH_PARENT:
         default:
            var40 = false;
         }

         if (this.mVisibility == 8) {
            var10 = 0;
            var40 = false;
         }

         if (var20) {
            if (!var26 && !var27 && !var28) {
               var1.addEquality(var21, var9);
            } else if (var26 && !var27) {
               var1.addEquality(var21, var23, var7.getMargin(), 6);
            }
         }

         boolean var37;
         if (!var40) {
            if (var6) {
               var1.addEquality(var22, var21, 0, 3);
               if (var11 > 0) {
                  var1.addGreaterThan(var22, var21, var11, 6);
               }

               if (var12 < Integer.MAX_VALUE) {
                  var1.addLowerThan(var22, var21, var12, 6);
               }
            } else {
               var1.addEquality(var22, var21, var10, 6);
            }

            var9 = var17;
            var17 = var18;
            var18 = var9;
         } else {
            if (var17 == -2) {
               var9 = var10;
            } else {
               var9 = var17;
            }

            var12 = var18;
            if (var18 == -2) {
               var12 = var10;
            }

            if (var9 > 0) {
               if (var2) {
                  var1.addGreaterThan(var22, var21, var9, 6);
               } else {
                  var1.addGreaterThan(var22, var21, var9, 6);
               }

               var10 = Math.max(var10, var9);
            }

            int var31 = var10;
            if (var12 > 0) {
               if (var2) {
                  var1.addLowerThan(var22, var21, var12, 1);
               } else {
                  var1.addLowerThan(var22, var21, var12, 6);
               }

               var31 = Math.min(var10, var12);
            }

            label250: {
               if (var30 == 1) {
                  if (var2) {
                     var1.addEquality(var22, var21, var31, 6);
                  } else if (var15) {
                     var1.addEquality(var22, var21, var31, 4);
                  } else {
                     var1.addEquality(var22, var21, var31, 1);
                  }
               } else if (var30 == 2) {
                  SolverVariable var33;
                  SolverVariable var41;
                  if (var7.getType() != ConstraintAnchor.Type.TOP && var7.getType() != ConstraintAnchor.Type.BOTTOM) {
                     var33 = var1.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.LEFT));
                     var41 = var1.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                  } else {
                     var33 = var1.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.TOP));
                     var41 = var1.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.BOTTOM));
                  }

                  var1.addConstraint(var1.createRow().createRowDimensionRatio(var22, var21, var41, var33, var19));
                  var37 = false;
                  break label250;
               }

               var37 = var40;
            }

            var17 = var12;
            var18 = var9;
            var40 = var37;
            if (var37) {
               var17 = var12;
               var18 = var9;
               var40 = var37;
               if (var42 != 2) {
                  var17 = var12;
                  var18 = var9;
                  var40 = var37;
                  if (!var14) {
                     var16 = Math.max(var9, var31);
                     var10 = var16;
                     if (var12 > 0) {
                        var10 = Math.min(var12, var16);
                     }

                     var1.addEquality(var22, var21, var10, 6);
                     var40 = false;
                     var18 = var9;
                     var17 = var12;
                  }
               }
            }
         }

         if (var20 && !var15) {
            byte var38 = 5;
            if (!var26 && !var27 && !var28) {
               if (var2) {
                  var1.addGreaterThan(var4, var22, 0, 5);
               }
            } else {
               byte var34 = 4;
               if (var26 && !var27) {
                  if (var2) {
                     var1.addGreaterThan(var4, var22, 0, 5);
                  }
               } else if (!var26 && var27) {
                  var1.addEquality(var22, var24, -var8.getMargin(), 6);
                  if (var2) {
                     var1.addGreaterThan(var21, var3, 0, 5);
                  }
               } else if (var26 && var27) {
                  boolean var35;
                  label286: {
                     if (var40) {
                        label283: {
                           if (var2 && var11 == 0) {
                              var1.addGreaterThan(var22, var21, 0, 6);
                           }

                           boolean var36;
                           byte var39;
                           if (var30 == 0) {
                              if (var17 <= 0 && var18 <= 0) {
                                 var35 = false;
                                 var39 = 6;
                              } else {
                                 var36 = true;
                                 var39 = var34;
                                 var35 = var36;
                              }

                              var1.addEquality(var21, var23, var7.getMargin(), var39);
                              var1.addEquality(var22, var24, -var8.getMargin(), var39);
                              if (var17 <= 0 && var18 <= 0) {
                                 var37 = false;
                              } else {
                                 var37 = true;
                              }

                              var36 = var35;
                              var35 = var37;
                              var37 = var36;
                              break label286;
                           }

                           var36 = true;
                           if (var30 == 1) {
                              var34 = 6;
                           } else {
                              if (var30 != 3) {
                                 var35 = false;
                                 break label283;
                              }

                              if (!var14) {
                                 var39 = var34;
                                 if (this.mResolvedDimensionRatioSide != -1) {
                                    var39 = var34;
                                    if (var17 <= 0) {
                                       var39 = 6;
                                    }
                                 }
                              } else {
                                 var39 = var34;
                              }

                              var1.addEquality(var21, var23, var7.getMargin(), var39);
                              var1.addEquality(var22, var24, -var8.getMargin(), var39);
                              var34 = var38;
                           }

                           var37 = true;
                           var38 = var34;
                           var35 = var36;
                           break label286;
                        }
                     } else {
                        var37 = true;
                        var35 = var37;
                        if (var2) {
                           var1.addGreaterThan(var21, var23, var7.getMargin(), 5);
                           var1.addLowerThan(var22, var24, -var8.getMargin(), 5);
                           var35 = var37;
                        }
                     }

                     var37 = false;
                  }

                  if (var35) {
                     var1.addCentering(var21, var23, var7.getMargin(), var13, var24, var22, var8.getMargin(), var38);
                  }

                  if (var37) {
                     var1.addGreaterThan(var21, var23, var7.getMargin(), 6);
                     var1.addLowerThan(var22, var24, -var8.getMargin(), 6);
                  }

                  if (var2) {
                     var1.addGreaterThan(var21, var3, 0, 6);
                  }
               }
            }

            if (var2) {
               var1.addGreaterThan(var4, var22, 0, 6);
            }

         } else {
            if (var42 < 2 && var2) {
               var1.addGreaterThan(var21, var3, 0, 6);
               var1.addGreaterThan(var4, var22, 0, 6);
            }

         }
      }
   }

   public void addToSolver(LinearSystem var1) {
      SolverVariable var2 = var1.createObjectVariable(this.mLeft);
      SolverVariable var3 = var1.createObjectVariable(this.mRight);
      SolverVariable var4 = var1.createObjectVariable(this.mTop);
      SolverVariable var5 = var1.createObjectVariable(this.mBottom);
      SolverVariable var6 = var1.createObjectVariable(this.mBaseline);
      boolean var7;
      boolean var8;
      boolean var9;
      boolean var10;
      boolean var11;
      boolean var12;
      if (this.mParent != null) {
         if (this.mParent != null && this.mParent.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (this.mParent != null && this.mParent.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            var8 = true;
         } else {
            var8 = false;
         }

         if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget != this.mLeft && this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight) {
            ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
         }

         if ((this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != this.mLeft) && (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != this.mRight)) {
            var9 = false;
         } else {
            var9 = true;
         }

         if (this.mTop.mTarget != null && this.mTop.mTarget.mTarget != this.mTop && this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom) {
            ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
         }

         if ((this.mTop.mTarget == null || this.mTop.mTarget.mTarget != this.mTop) && (this.mBottom.mTarget == null || this.mBottom.mTarget.mTarget != this.mBottom)) {
            var10 = false;
         } else {
            var10 = true;
         }

         if (var7 && this.mVisibility != 8 && this.mLeft.mTarget == null && this.mRight.mTarget == null) {
            var1.addGreaterThan(var1.createObjectVariable(this.mParent.mRight), var3, 0, 1);
         }

         if (var8 && this.mVisibility != 8 && this.mTop.mTarget == null && this.mBottom.mTarget == null && this.mBaseline == null) {
            var1.addGreaterThan(var1.createObjectVariable(this.mParent.mBottom), var5, 0, 1);
         }

         var11 = var8;
         var8 = var9;
         var12 = var10;
         var9 = var7;
         var7 = var11;
         var10 = var8;
         var8 = var12;
      } else {
         var9 = false;
         var7 = false;
         var10 = false;
         var8 = false;
      }

      int var13 = this.mWidth;
      int var14 = var13;
      if (var13 < this.mMinWidth) {
         var14 = this.mMinWidth;
      }

      int var15 = this.mHeight;
      var13 = var15;
      if (var15 < this.mMinHeight) {
         var13 = this.mMinHeight;
      }

      if (this.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         var11 = true;
      } else {
         var11 = false;
      }

      if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         var12 = true;
      } else {
         var12 = false;
      }

      int var16;
      int var18;
      int var19;
      boolean var23;
      label243: {
         label297: {
            this.mResolvedDimensionRatioSide = this.mDimensionRatioSide;
            this.mResolvedDimensionRatio = this.mDimensionRatio;
            var16 = this.mMatchConstraintDefaultWidth;
            int var17 = this.mMatchConstraintDefaultHeight;
            if (this.mDimensionRatio > 0.0F && this.mVisibility != 8) {
               label287: {
                  var15 = var16;
                  if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     var15 = var16;
                     if (var16 == 0) {
                        var15 = 3;
                     }
                  }

                  var16 = var17;
                  if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     var16 = var17;
                     if (var17 == 0) {
                        var16 = 3;
                     }
                  }

                  label301: {
                     if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var15 == 3 && var16 == 3) {
                        this.setupDimensionRatio(var9, var7, var11, var12);
                     } else {
                        if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var15 == 3) {
                           this.mResolvedDimensionRatioSide = 0;
                           var17 = (int)(this.mResolvedDimensionRatio * (float)this.mHeight);
                           if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var15 = var17;
                              var17 = var16;
                              var16 = var13;
                              var14 = 4;
                              var13 = var17;
                              break label287;
                           }
                           break label301;
                        }

                        if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var16 == 3) {
                           this.mResolvedDimensionRatioSide = 1;
                           if (this.mDimensionRatioSide == -1) {
                              this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
                           }

                           var17 = (int)(this.mResolvedDimensionRatio * (float)this.mWidth);
                           if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var13 = var14;
                              var14 = var17;
                              break label297;
                           }

                           var16 = var17;
                           var17 = var14;
                           var14 = var15;
                           var13 = 4;
                           var15 = var17;
                           break label287;
                        }
                     }

                     var17 = var14;
                  }

                  var14 = var13;
                  var13 = var17;
                  break label297;
               }
            } else {
               var19 = var16;
               var16 = var13;
               var15 = var14;
               var13 = var17;
               var14 = var19;
            }

            var23 = false;
            var19 = var15;
            var18 = var13;
            var15 = var14;
            break label243;
         }

         var23 = true;
         var18 = var16;
         var19 = var13;
         var16 = var14;
      }

      this.mResolvedMatchConstraintDefault[0] = var15;
      this.mResolvedMatchConstraintDefault[1] = var18;
      if (!var23 || this.mResolvedDimensionRatioSide != 0 && this.mResolvedDimensionRatioSide != -1) {
         var11 = false;
      } else {
         var11 = true;
      }

      if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer) {
         var12 = true;
      } else {
         var12 = false;
      }

      boolean var20 = this.mCenter.isConnected() ^ true;
      SolverVariable var21;
      if (this.mHorizontalResolution != 2) {
         if (this.mParent != null) {
            var21 = var1.createObjectVariable(this.mParent.mRight);
         } else {
            var21 = null;
         }

         SolverVariable var22;
         if (this.mParent != null) {
            var22 = var1.createObjectVariable(this.mParent.mLeft);
         } else {
            var22 = null;
         }

         this.applyConstraints(var1, var9, var22, var21, this.mListDimensionBehaviors[0], var12, this.mLeft, this.mRight, this.mX, var19, this.mMinWidth, this.mMaxDimension[0], this.mHorizontalBiasPercent, var11, var10, var15, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth, this.mMatchConstraintPercentWidth, var20);
      }

      if (this.mVerticalResolution != 2) {
         if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer) {
            var9 = true;
         } else {
            var9 = false;
         }

         if (!var23 || this.mResolvedDimensionRatioSide != 1 && this.mResolvedDimensionRatioSide != -1) {
            var10 = false;
         } else {
            var10 = true;
         }

         label181: {
            if (this.mBaselineDistance > 0) {
               if (this.mBaseline.getResolutionNode().state == 1) {
                  this.mBaseline.getResolutionNode().addResolvedValue(var1);
               } else {
                  var1.addEquality(var6, var4, this.getBaselineDistance(), 6);
                  if (this.mBaseline.mTarget != null) {
                     var1.addEquality(var6, var1.createObjectVariable(this.mBaseline.mTarget), 0, 6);
                     var11 = false;
                     break label181;
                  }
               }
            }

            var11 = var20;
         }

         if (this.mParent != null) {
            var6 = var1.createObjectVariable(this.mParent.mBottom);
         } else {
            var6 = null;
         }

         if (this.mParent != null) {
            var21 = var1.createObjectVariable(this.mParent.mTop);
         } else {
            var21 = null;
         }

         this.applyConstraints(var1, var7, var21, var6, this.mListDimensionBehaviors[1], var9, this.mTop, this.mBottom, this.mY, var16, this.mMinHeight, this.mMaxDimension[1], this.mVerticalBiasPercent, var10, var8, var18, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight, this.mMatchConstraintPercentHeight, var11);
         if (var23) {
            if (this.mResolvedDimensionRatioSide == 1) {
               var1.addRatio(var5, var4, var3, var2, this.mResolvedDimensionRatio, 6);
            } else {
               var1.addRatio(var3, var2, var5, var4, this.mResolvedDimensionRatio, 6);
            }
         }

         if (this.mCenter.isConnected()) {
            var1.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float)Math.toRadians((double)(this.mCircleConstraintAngle + 90.0F)), this.mCenter.getMargin());
         }

      }
   }

   public boolean allowedInBarrier() {
      boolean var1;
      if (this.mVisibility != 8) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void analyze(int var1) {
      Optimizer.analyze(var1, this);
   }

   public void connectCircularConstraint(ConstraintWidget var1, float var2, int var3) {
      this.immediateConnect(ConstraintAnchor.Type.CENTER, var1, ConstraintAnchor.Type.CENTER, var3, 0);
      this.mCircleConstraintAngle = var2;
   }

   public void createObjectVariables(LinearSystem var1) {
      var1.createObjectVariable(this.mLeft);
      var1.createObjectVariable(this.mTop);
      var1.createObjectVariable(this.mRight);
      var1.createObjectVariable(this.mBottom);
      if (this.mBaselineDistance > 0) {
         var1.createObjectVariable(this.mBaseline);
      }

   }

   public ConstraintAnchor getAnchor(ConstraintAnchor.Type var1) {
      switch(var1) {
      case LEFT:
         return this.mLeft;
      case TOP:
         return this.mTop;
      case RIGHT:
         return this.mRight;
      case BOTTOM:
         return this.mBottom;
      case BASELINE:
         return this.mBaseline;
      case CENTER:
         return this.mCenter;
      case CENTER_X:
         return this.mCenterX;
      case CENTER_Y:
         return this.mCenterY;
      case NONE:
         return null;
      default:
         throw new AssertionError(var1.name());
      }
   }

   public ArrayList getAnchors() {
      return this.mAnchors;
   }

   public int getBaselineDistance() {
      return this.mBaselineDistance;
   }

   public int getBottom() {
      return this.getY() + this.mHeight;
   }

   public Object getCompanionWidget() {
      return this.mCompanionWidget;
   }

   public String getDebugName() {
      return this.mDebugName;
   }

   public int getDrawX() {
      return this.mDrawX + this.mOffsetX;
   }

   public int getDrawY() {
      return this.mDrawY + this.mOffsetY;
   }

   public int getHeight() {
      return this.mVisibility == 8 ? 0 : this.mHeight;
   }

   public float getHorizontalBiasPercent() {
      return this.mHorizontalBiasPercent;
   }

   public ConstraintWidget.DimensionBehaviour getHorizontalDimensionBehaviour() {
      return this.mListDimensionBehaviors[0];
   }

   public ConstraintWidget getParent() {
      return this.mParent;
   }

   public ResolutionDimension getResolutionHeight() {
      if (this.mResolutionHeight == null) {
         this.mResolutionHeight = new ResolutionDimension();
      }

      return this.mResolutionHeight;
   }

   public ResolutionDimension getResolutionWidth() {
      if (this.mResolutionWidth == null) {
         this.mResolutionWidth = new ResolutionDimension();
      }

      return this.mResolutionWidth;
   }

   public int getRight() {
      return this.getX() + this.mWidth;
   }

   protected int getRootX() {
      return this.mX + this.mOffsetX;
   }

   protected int getRootY() {
      return this.mY + this.mOffsetY;
   }

   public ConstraintWidget.DimensionBehaviour getVerticalDimensionBehaviour() {
      return this.mListDimensionBehaviors[1];
   }

   public int getVisibility() {
      return this.mVisibility;
   }

   public int getWidth() {
      return this.mVisibility == 8 ? 0 : this.mWidth;
   }

   public int getWrapHeight() {
      return this.mWrapHeight;
   }

   public int getWrapWidth() {
      return this.mWrapWidth;
   }

   public int getX() {
      return this.mX;
   }

   public int getY() {
      return this.mY;
   }

   public boolean hasBaseline() {
      boolean var1;
      if (this.mBaselineDistance > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void immediateConnect(ConstraintAnchor.Type var1, ConstraintWidget var2, ConstraintAnchor.Type var3, int var4, int var5) {
      this.getAnchor(var1).connect(var2.getAnchor(var3), var4, var5, ConstraintAnchor.Strength.STRONG, 0, true);
   }

   public boolean isSpreadHeight() {
      int var1 = this.mMatchConstraintDefaultHeight;
      boolean var2 = true;
      if (var1 != 0 || this.mDimensionRatio != 0.0F || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         var2 = false;
      }

      return var2;
   }

   public boolean isSpreadWidth() {
      int var1 = this.mMatchConstraintDefaultWidth;
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 == 0) {
         var3 = var2;
         if (this.mDimensionRatio == 0.0F) {
            var3 = var2;
            if (this.mMatchConstraintMinWidth == 0) {
               var3 = var2;
               if (this.mMatchConstraintMaxWidth == 0) {
                  var3 = var2;
                  if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     var3 = true;
                  }
               }
            }
         }
      }

      return var3;
   }

   public void reset() {
      this.mLeft.reset();
      this.mTop.reset();
      this.mRight.reset();
      this.mBottom.reset();
      this.mBaseline.reset();
      this.mCenterX.reset();
      this.mCenterY.reset();
      this.mCenter.reset();
      this.mParent = null;
      this.mCircleConstraintAngle = 0.0F;
      this.mWidth = 0;
      this.mHeight = 0;
      this.mDimensionRatio = 0.0F;
      this.mDimensionRatioSide = -1;
      this.mX = 0;
      this.mY = 0;
      this.mDrawX = 0;
      this.mDrawY = 0;
      this.mDrawWidth = 0;
      this.mDrawHeight = 0;
      this.mOffsetX = 0;
      this.mOffsetY = 0;
      this.mBaselineDistance = 0;
      this.mMinWidth = 0;
      this.mMinHeight = 0;
      this.mWrapWidth = 0;
      this.mWrapHeight = 0;
      this.mHorizontalBiasPercent = DEFAULT_BIAS;
      this.mVerticalBiasPercent = DEFAULT_BIAS;
      this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
      this.mCompanionWidget = null;
      this.mContainerItemSkip = 0;
      this.mVisibility = 0;
      this.mType = null;
      this.mHorizontalWrapVisited = false;
      this.mVerticalWrapVisited = false;
      this.mHorizontalChainStyle = 0;
      this.mVerticalChainStyle = 0;
      this.mHorizontalChainFixedPosition = false;
      this.mVerticalChainFixedPosition = false;
      this.mWeight[0] = -1.0F;
      this.mWeight[1] = -1.0F;
      this.mHorizontalResolution = -1;
      this.mVerticalResolution = -1;
      this.mMaxDimension[0] = Integer.MAX_VALUE;
      this.mMaxDimension[1] = Integer.MAX_VALUE;
      this.mMatchConstraintDefaultWidth = 0;
      this.mMatchConstraintDefaultHeight = 0;
      this.mMatchConstraintPercentWidth = 1.0F;
      this.mMatchConstraintPercentHeight = 1.0F;
      this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
      this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
      this.mMatchConstraintMinWidth = 0;
      this.mMatchConstraintMinHeight = 0;
      this.mResolvedDimensionRatioSide = -1;
      this.mResolvedDimensionRatio = 1.0F;
      if (this.mResolutionWidth != null) {
         this.mResolutionWidth.reset();
      }

      if (this.mResolutionHeight != null) {
         this.mResolutionHeight.reset();
      }

   }

   public void resetAnchors() {
      ConstraintWidget var1 = this.getParent();
      if (var1 == null || !(var1 instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
         int var2 = 0;

         for(int var3 = this.mAnchors.size(); var2 < var3; ++var2) {
            ((ConstraintAnchor)this.mAnchors.get(var2)).reset();
         }

      }
   }

   public void resetResolutionNodes() {
      for(int var1 = 0; var1 < 6; ++var1) {
         this.mListAnchors[var1].getResolutionNode().reset();
      }

   }

   public void resetSolverVariables(Cache var1) {
      this.mLeft.resetSolverVariable(var1);
      this.mTop.resetSolverVariable(var1);
      this.mRight.resetSolverVariable(var1);
      this.mBottom.resetSolverVariable(var1);
      this.mBaseline.resetSolverVariable(var1);
      this.mCenter.resetSolverVariable(var1);
      this.mCenterX.resetSolverVariable(var1);
      this.mCenterY.resetSolverVariable(var1);
   }

   public void resolve() {
   }

   public void setBaselineDistance(int var1) {
      this.mBaselineDistance = var1;
   }

   public void setCompanionWidget(Object var1) {
      this.mCompanionWidget = var1;
   }

   public void setDebugName(String var1) {
      this.mDebugName = var1;
   }

   public void setDimensionRatio(String var1) {
      if (var1 != null && var1.length() != 0) {
         byte var2 = -1;
         int var3 = var1.length();
         int var4 = var1.indexOf(44);
         byte var5 = 0;
         byte var6 = var2;
         int var7 = var5;
         String var8;
         if (var4 > 0) {
            var6 = var2;
            var7 = var5;
            if (var4 < var3 - 1) {
               var8 = var1.substring(0, var4);
               if (var8.equalsIgnoreCase("W")) {
                  var6 = 0;
               } else {
                  var6 = var2;
                  if (var8.equalsIgnoreCase("H")) {
                     var6 = 1;
                  }
               }

               var7 = var4 + 1;
            }
         }

         float var9;
         label88: {
            int var15 = var1.indexOf(58);
            boolean var10001;
            if (var15 >= 0 && var15 < var3 - 1) {
               var8 = var1.substring(var7, var15);
               var1 = var1.substring(var15 + 1);
               if (var8.length() > 0 && var1.length() > 0) {
                  label89: {
                     float var10;
                     try {
                        var9 = Float.parseFloat(var8);
                        var10 = Float.parseFloat(var1);
                     } catch (NumberFormatException var14) {
                        var10001 = false;
                        break label89;
                     }

                     if (var9 > 0.0F && var10 > 0.0F) {
                        if (var6 == 1) {
                           try {
                              var9 = Math.abs(var10 / var9);
                              break label88;
                           } catch (NumberFormatException var11) {
                              var10001 = false;
                           }
                        } else {
                           try {
                              var9 = Math.abs(var9 / var10);
                              break label88;
                           } catch (NumberFormatException var12) {
                              var10001 = false;
                           }
                        }
                     }
                  }
               }
            } else {
               var1 = var1.substring(var7);
               if (var1.length() > 0) {
                  try {
                     var9 = Float.parseFloat(var1);
                     break label88;
                  } catch (NumberFormatException var13) {
                     var10001 = false;
                  }
               }
            }

            var9 = 0.0F;
         }

         if (var9 > 0.0F) {
            this.mDimensionRatio = var9;
            this.mDimensionRatioSide = var6;
         }

      } else {
         this.mDimensionRatio = 0.0F;
      }
   }

   public void setFrame(int var1, int var2, int var3, int var4) {
      int var5 = var3 - var1;
      var3 = var4 - var2;
      this.mX = var1;
      this.mY = var2;
      if (this.mVisibility == 8) {
         this.mWidth = 0;
         this.mHeight = 0;
      } else {
         var1 = var5;
         if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED) {
            var1 = var5;
            if (var5 < this.mWidth) {
               var1 = this.mWidth;
            }
         }

         var2 = var3;
         if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED) {
            var2 = var3;
            if (var3 < this.mHeight) {
               var2 = this.mHeight;
            }
         }

         this.mWidth = var1;
         this.mHeight = var2;
         if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
         }

         if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
         }

      }
   }

   public void setHeight(int var1) {
      this.mHeight = var1;
      if (this.mHeight < this.mMinHeight) {
         this.mHeight = this.mMinHeight;
      }

   }

   public void setHeightWrapContent(boolean var1) {
      this.mIsHeightWrapContent = var1;
   }

   public void setHorizontalBiasPercent(float var1) {
      this.mHorizontalBiasPercent = var1;
   }

   public void setHorizontalChainStyle(int var1) {
      this.mHorizontalChainStyle = var1;
   }

   public void setHorizontalDimension(int var1, int var2) {
      this.mX = var1;
      this.mWidth = var2 - var1;
      if (this.mWidth < this.mMinWidth) {
         this.mWidth = this.mMinWidth;
      }

   }

   public void setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour var1) {
      this.mListDimensionBehaviors[0] = var1;
      if (var1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
         this.setWidth(this.mWrapWidth);
      }

   }

   public void setHorizontalMatchStyle(int var1, int var2, int var3, float var4) {
      this.mMatchConstraintDefaultWidth = var1;
      this.mMatchConstraintMinWidth = var2;
      this.mMatchConstraintMaxWidth = var3;
      this.mMatchConstraintPercentWidth = var4;
      if (var4 < 1.0F && this.mMatchConstraintDefaultWidth == 0) {
         this.mMatchConstraintDefaultWidth = 2;
      }

   }

   public void setHorizontalWeight(float var1) {
      this.mWeight[0] = var1;
   }

   public void setMaxHeight(int var1) {
      this.mMaxDimension[1] = var1;
   }

   public void setMaxWidth(int var1) {
      this.mMaxDimension[0] = var1;
   }

   public void setMinHeight(int var1) {
      if (var1 < 0) {
         this.mMinHeight = 0;
      } else {
         this.mMinHeight = var1;
      }

   }

   public void setMinWidth(int var1) {
      if (var1 < 0) {
         this.mMinWidth = 0;
      } else {
         this.mMinWidth = var1;
      }

   }

   public void setOffset(int var1, int var2) {
      this.mOffsetX = var1;
      this.mOffsetY = var2;
   }

   public void setOrigin(int var1, int var2) {
      this.mX = var1;
      this.mY = var2;
   }

   public void setParent(ConstraintWidget var1) {
      this.mParent = var1;
   }

   public void setVerticalBiasPercent(float var1) {
      this.mVerticalBiasPercent = var1;
   }

   public void setVerticalChainStyle(int var1) {
      this.mVerticalChainStyle = var1;
   }

   public void setVerticalDimension(int var1, int var2) {
      this.mY = var1;
      this.mHeight = var2 - var1;
      if (this.mHeight < this.mMinHeight) {
         this.mHeight = this.mMinHeight;
      }

   }

   public void setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour var1) {
      this.mListDimensionBehaviors[1] = var1;
      if (var1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
         this.setHeight(this.mWrapHeight);
      }

   }

   public void setVerticalMatchStyle(int var1, int var2, int var3, float var4) {
      this.mMatchConstraintDefaultHeight = var1;
      this.mMatchConstraintMinHeight = var2;
      this.mMatchConstraintMaxHeight = var3;
      this.mMatchConstraintPercentHeight = var4;
      if (var4 < 1.0F && this.mMatchConstraintDefaultHeight == 0) {
         this.mMatchConstraintDefaultHeight = 2;
      }

   }

   public void setVerticalWeight(float var1) {
      this.mWeight[1] = var1;
   }

   public void setVisibility(int var1) {
      this.mVisibility = var1;
   }

   public void setWidth(int var1) {
      this.mWidth = var1;
      if (this.mWidth < this.mMinWidth) {
         this.mWidth = this.mMinWidth;
      }

   }

   public void setWidthWrapContent(boolean var1) {
      this.mIsWidthWrapContent = var1;
   }

   public void setWrapHeight(int var1) {
      this.mWrapHeight = var1;
   }

   public void setWrapWidth(int var1) {
      this.mWrapWidth = var1;
   }

   public void setX(int var1) {
      this.mX = var1;
   }

   public void setY(int var1) {
      this.mY = var1;
   }

   public void setupDimensionRatio(boolean var1, boolean var2, boolean var3, boolean var4) {
      if (this.mResolvedDimensionRatioSide == -1) {
         if (var3 && !var4) {
            this.mResolvedDimensionRatioSide = 0;
         } else if (!var3 && var4) {
            this.mResolvedDimensionRatioSide = 1;
            if (this.mDimensionRatioSide == -1) {
               this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
            }
         }
      }

      if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
         this.mResolvedDimensionRatioSide = 1;
      } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
         this.mResolvedDimensionRatioSide = 0;
      }

      if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
         if (this.mTop.isConnected() && this.mBottom.isConnected()) {
            this.mResolvedDimensionRatioSide = 0;
         } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
            this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
         }
      }

      if (this.mResolvedDimensionRatioSide == -1) {
         if (var1 && !var2) {
            this.mResolvedDimensionRatioSide = 0;
         } else if (!var1 && var2) {
            this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
         }
      }

      if (this.mResolvedDimensionRatioSide == -1) {
         if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
            this.mResolvedDimensionRatioSide = 0;
         } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
            this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
         }
      }

      if (this.mResolvedDimensionRatioSide == -1 && var1 && var2) {
         this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
         this.mResolvedDimensionRatioSide = 1;
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      StringBuilder var2;
      String var3;
      if (this.mType != null) {
         var2 = new StringBuilder();
         var2.append("type: ");
         var2.append(this.mType);
         var2.append(" ");
         var3 = var2.toString();
      } else {
         var3 = "";
      }

      var1.append(var3);
      if (this.mDebugName != null) {
         var2 = new StringBuilder();
         var2.append("id: ");
         var2.append(this.mDebugName);
         var2.append(" ");
         var3 = var2.toString();
      } else {
         var3 = "";
      }

      var1.append(var3);
      var1.append("(");
      var1.append(this.mX);
      var1.append(", ");
      var1.append(this.mY);
      var1.append(") - (");
      var1.append(this.mWidth);
      var1.append(" x ");
      var1.append(this.mHeight);
      var1.append(") wrap: (");
      var1.append(this.mWrapWidth);
      var1.append(" x ");
      var1.append(this.mWrapHeight);
      var1.append(")");
      return var1.toString();
   }

   public void updateDrawPosition() {
      int var1 = this.mX;
      int var2 = this.mY;
      int var3 = this.mX;
      int var4 = this.mWidth;
      int var5 = this.mY;
      int var6 = this.mHeight;
      this.mDrawX = var1;
      this.mDrawY = var2;
      this.mDrawWidth = var3 + var4 - var1;
      this.mDrawHeight = var5 + var6 - var2;
   }

   public void updateFromSolver(LinearSystem var1) {
      int var2;
      int var3;
      int var4;
      int var6;
      label27: {
         var2 = var1.getObjectVariableValue(this.mLeft);
         var3 = var1.getObjectVariableValue(this.mTop);
         var4 = var1.getObjectVariableValue(this.mRight);
         int var5 = var1.getObjectVariableValue(this.mBottom);
         if (var4 - var2 >= 0 && var5 - var3 >= 0 && var2 != Integer.MIN_VALUE && var2 != Integer.MAX_VALUE && var3 != Integer.MIN_VALUE && var3 != Integer.MAX_VALUE && var4 != Integer.MIN_VALUE && var4 != Integer.MAX_VALUE && var5 != Integer.MIN_VALUE) {
            var6 = var5;
            if (var5 != Integer.MAX_VALUE) {
               break label27;
            }
         }

         var6 = 0;
         var2 = 0;
         var3 = 0;
         var4 = 0;
      }

      this.setFrame(var2, var3, var4, var6);
   }

   public void updateResolutionNodes() {
      for(int var1 = 0; var1 < 6; ++var1) {
         this.mListAnchors[var1].getResolutionNode().update();
      }

   }

   public static enum DimensionBehaviour {
      FIXED,
      MATCH_CONSTRAINT,
      MATCH_PARENT,
      WRAP_CONTENT;
   }
}
