package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import java.util.Arrays;

public class ConstraintWidgetContainer extends WidgetContainer {
   static boolean ALLOW_ROOT_GROUP;
   private static final int CHAIN_FIRST = 0;
   private static final int CHAIN_FIRST_VISIBLE = 2;
   private static final int CHAIN_LAST = 1;
   private static final int CHAIN_LAST_VISIBLE = 3;
   private static final boolean DEBUG = false;
   private static final boolean DEBUG_LAYOUT = false;
   private static final boolean DEBUG_OPTIMIZE = false;
   private static final int FLAG_CHAIN_DANGLING = 1;
   private static final int FLAG_CHAIN_OPTIMIZE = 0;
   private static final int FLAG_RECOMPUTE_BOUNDS = 2;
   private static final int MAX_ITERATIONS = 8;
   public static final int OPTIMIZATION_ALL = 2;
   public static final int OPTIMIZATION_BASIC = 4;
   public static final int OPTIMIZATION_CHAIN = 8;
   public static final int OPTIMIZATION_NONE = 1;
   private static final boolean USE_SNAPSHOT = true;
   private static final boolean USE_THREAD = false;
   private boolean[] flags = new boolean[3];
   protected LinearSystem mBackgroundSystem = null;
   private ConstraintWidget[] mChainEnds = new ConstraintWidget[4];
   private boolean mHeightMeasuredTooSmall = false;
   private ConstraintWidget[] mHorizontalChainsArray = new ConstraintWidget[4];
   private int mHorizontalChainsSize = 0;
   private ConstraintWidget[] mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
   private int mOptimizationLevel = 2;
   int mPaddingBottom;
   int mPaddingLeft;
   int mPaddingRight;
   int mPaddingTop;
   private Snapshot mSnapshot;
   protected LinearSystem mSystem = new LinearSystem();
   private ConstraintWidget[] mVerticalChainsArray = new ConstraintWidget[4];
   private int mVerticalChainsSize = 0;
   private boolean mWidthMeasuredTooSmall = false;
   int mWrapHeight;
   int mWrapWidth;

   public ConstraintWidgetContainer() {
   }

   public ConstraintWidgetContainer(int var1, int var2) {
      super(var1, var2);
   }

   public ConstraintWidgetContainer(int var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   private void addHorizontalChain(ConstraintWidget var1) {
      for(int var2 = 0; var2 < this.mHorizontalChainsSize; ++var2) {
         if (this.mHorizontalChainsArray[var2] == var1) {
            return;
         }
      }

      if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
         this.mHorizontalChainsArray = (ConstraintWidget[])Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
      }

      this.mHorizontalChainsArray[this.mHorizontalChainsSize] = var1;
      ++this.mHorizontalChainsSize;
   }

   private void addVerticalChain(ConstraintWidget var1) {
      for(int var2 = 0; var2 < this.mVerticalChainsSize; ++var2) {
         if (this.mVerticalChainsArray[var2] == var1) {
            return;
         }
      }

      if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
         this.mVerticalChainsArray = (ConstraintWidget[])Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
      }

      this.mVerticalChainsArray[this.mVerticalChainsSize] = var1;
      ++this.mVerticalChainsSize;
   }

   private void applyHorizontalChain(LinearSystem var1) {
      LinearSystem var2 = var1;
      byte var3 = 0;
      int var4 = 0;

      while(true) {
         ConstraintWidgetContainer var5 = this;
         if (var4 >= this.mHorizontalChainsSize) {
            return;
         }

         LinearSystem var33;
         label324: {
            ConstraintWidget var6 = this.mHorizontalChainsArray[var4];
            int var7 = this.countMatchConstraintsChainedWidgets(var2, this.mChainEnds, this.mHorizontalChainsArray[var4], 0, this.flags);
            ConstraintWidget var8 = this.mChainEnds[2];
            byte var9;
            if (var8 == null) {
               var9 = var3;
            } else {
               ConstraintWidget var10;
               int var34;
               if (this.flags[1]) {
                  var34 = var6.getDrawX();

                  while(true) {
                     var9 = var3;
                     if (var8 == null) {
                        break;
                     }

                     var2.addEquality(var8.mLeft.mSolverVariable, var34);
                     var10 = var8.mHorizontalNextWidget;
                     var34 += var8.mLeft.getMargin() + var8.getWidth() + var8.mRight.getMargin();
                     var8 = var10;
                  }
               } else {
                  if (var6.mHorizontalChainStyle == 0) {
                     var9 = 1;
                  } else {
                     var9 = var3;
                  }

                  byte var11;
                  if (var6.mHorizontalChainStyle == 2) {
                     var11 = 1;
                  } else {
                     var11 = var3;
                  }

                  byte var12;
                  if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                     var12 = 1;
                  } else {
                     var12 = var3;
                  }

                  if ((this.mOptimizationLevel == 2 || this.mOptimizationLevel == 8) && this.flags[var3] && var6.mHorizontalChainFixedPosition && var11 == 0 && var12 == 0 && var6.mHorizontalChainStyle == 0) {
                     Optimizer.applyDirectResolutionHorizontalChain(this, var2, var7, var6);
                     var9 = var3;
                  } else {
                     ConstraintWidget var14;
                     ConstraintWidget var15;
                     SolverVariable var27;
                     int var28;
                     int var35;
                     if (var7 == 0 || var11 != 0) {
                        ConstraintWidget var23 = var8;
                        var14 = null;
                        var15 = var14;
                        boolean var30 = false;
                        var33 = var2;
                        ConstraintWidget var20 = var14;

                        while(true) {
                           ConstraintWidget var38 = var23;
                           ConstraintAnchor var39;
                           if (var23 == null) {
                              var28 = var4;
                              LinearSystem var25 = var33;
                              var12 = 0;
                              var33 = var33;
                              var4 = var4;
                              var3 = var12;
                              if (var11 != 0) {
                                 ConstraintAnchor var41 = var8.mLeft;
                                 var39 = var20.mRight;
                                 var34 = var41.getMargin();
                                 var7 = var39.getMargin();
                                 if (var6.mLeft.mTarget != null) {
                                    var27 = var6.mLeft.mTarget.mSolverVariable;
                                 } else {
                                    var27 = null;
                                 }

                                 SolverVariable var24;
                                 if (var20.mRight.mTarget != null) {
                                    var24 = var20.mRight.mTarget.mSolverVariable;
                                 } else {
                                    var24 = null;
                                 }

                                 var33 = var25;
                                 var4 = var28;
                                 var3 = var12;
                                 if (var27 != null) {
                                    var33 = var25;
                                    var4 = var28;
                                    var3 = var12;
                                    if (var24 != null) {
                                       var25.addLowerThan(var39.mSolverVariable, var24, -var7, 1);
                                       var25.addCentering(var41.mSolverVariable, var27, var34, var6.mHorizontalBiasPercent, var24, var39.mSolverVariable, var7, 4);
                                       var3 = var12;
                                       var4 = var28;
                                       var33 = var25;
                                    }
                                 }
                              }
                              break label324;
                           }

                           ConstraintWidget var43 = var23.mHorizontalNextWidget;
                           if (var43 == null) {
                              var23 = this.mChainEnds[1];
                              var30 = true;
                           } else {
                              var23 = var20;
                           }

                           label325: {
                              if (var11 != 0) {
                                 ConstraintAnchor var22 = var38.mLeft;
                                 var7 = var22.getMargin();
                                 var35 = var7;
                                 if (var15 != null) {
                                    var35 = var7 + var15.mRight.getMargin();
                                 }

                                 byte var26;
                                 if (var8 != var38) {
                                    var26 = 3;
                                 } else {
                                    var26 = 1;
                                 }

                                 var33.addGreaterThan(var22.mSolverVariable, var22.mTarget.mSolverVariable, var35, var26);
                                 if (var38.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                    var39 = var38.mRight;
                                    if (var38.mMatchConstraintDefaultWidth == 1) {
                                       var35 = Math.max(var38.mMatchConstraintMinWidth, var38.getWidth());
                                       var33.addEquality(var39.mSolverVariable, var22.mSolverVariable, var35, 3);
                                    } else {
                                       var33.addGreaterThan(var22.mSolverVariable, var22.mTarget.mSolverVariable, var22.mMargin, 3);
                                       var33.addLowerThan(var39.mSolverVariable, var22.mSolverVariable, var38.mMatchConstraintMinWidth, 3);
                                    }
                                 }
                              } else if (var9 == 0 && var30 && var15 != null) {
                                 if (var38.mRight.mTarget == null) {
                                    var33.addEquality(var38.mRight.mSolverVariable, var38.getDrawRight());
                                 } else {
                                    var35 = var38.mRight.getMargin();
                                    var33.addEquality(var38.mRight.mSolverVariable, var23.mRight.mTarget.mSolverVariable, -var35, 5);
                                 }
                              } else {
                                 if (var9 != 0 || var30 || var15 != null) {
                                    ConstraintAnchor var19 = var38.mLeft;
                                    ConstraintAnchor var42 = var38.mRight;
                                    var35 = var19.getMargin();
                                    var7 = var42.getMargin();
                                    var33.addGreaterThan(var19.mSolverVariable, var19.mTarget.mSolverVariable, var35, 1);
                                    var33.addLowerThan(var42.mSolverVariable, var42.mTarget.mSolverVariable, -var7, 1);
                                    SolverVariable var36;
                                    if (var19.mTarget != null) {
                                       var36 = var19.mTarget.mSolverVariable;
                                    } else {
                                       var36 = null;
                                    }

                                    if (var15 == null) {
                                       if (var6.mLeft.mTarget != null) {
                                          var36 = var6.mLeft.mTarget.mSolverVariable;
                                       } else {
                                          var36 = null;
                                       }
                                    }

                                    var20 = var43;
                                    if (var43 == null) {
                                       if (var23.mRight.mTarget != null) {
                                          var20 = var23.mRight.mTarget.mOwner;
                                       } else {
                                          var20 = null;
                                       }
                                    }

                                    if (var20 != null) {
                                       SolverVariable var37 = var20.mLeft.mSolverVariable;
                                       if (var30) {
                                          if (var23.mRight.mTarget != null) {
                                             var37 = var23.mRight.mTarget.mSolverVariable;
                                          } else {
                                             var37 = null;
                                          }
                                       }

                                       if (var36 != null && var37 != null) {
                                          var33.addCentering(var19.mSolverVariable, var36, var35, 0.5F, var37, var42.mSolverVariable, var7, 4);
                                       }
                                    }
                                    break label325;
                                 }

                                 if (var38.mLeft.mTarget == null) {
                                    var33.addEquality(var38.mLeft.mSolverVariable, var38.getDrawX());
                                 } else {
                                    var35 = var38.mLeft.getMargin();
                                    var33.addEquality(var38.mLeft.mSolverVariable, var6.mLeft.mTarget.mSolverVariable, var35, 5);
                                 }
                              }

                              var20 = var43;
                           }

                           if (var30) {
                              var20 = null;
                           }

                           var10 = var23;
                           var15 = var38;
                           var23 = var20;
                           var20 = var10;
                           var33 = var33;
                        }
                     }

                     float var13 = 0.0F;

                     for(var10 = null; var8 != null; var8 = var14) {
                        if (var8.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                           var34 = var8.mLeft.getMargin();
                           var28 = var34;
                           if (var10 != null) {
                              var28 = var34 + var10.mRight.getMargin();
                           }

                           if (var8.mLeft.mTarget.mOwner.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var11 = 2;
                           } else {
                              var11 = 3;
                           }

                           var2.addGreaterThan(var8.mLeft.mSolverVariable, var8.mLeft.mTarget.mSolverVariable, var28, var11);
                           var34 = var8.mRight.getMargin();
                           var28 = var34;
                           if (var8.mRight.mTarget.mOwner.mLeft.mTarget != null) {
                              var28 = var34;
                              if (var8.mRight.mTarget.mOwner.mLeft.mTarget.mOwner == var8) {
                                 var28 = var34 + var8.mRight.mTarget.mOwner.mLeft.getMargin();
                              }
                           }

                           if (var8.mRight.mTarget.mOwner.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var11 = 2;
                           } else {
                              var11 = 3;
                           }

                           var2.addLowerThan(var8.mRight.mSolverVariable, var8.mRight.mTarget.mSolverVariable, -var28, var11);
                        } else {
                           var13 += var8.mHorizontalWeight;
                           if (var8.mRight.mTarget != null) {
                              var34 = var8.mRight.getMargin();
                              var28 = var34;
                              if (var8 != var5.mChainEnds[3]) {
                                 var28 = var34 + var8.mRight.mTarget.mOwner.mLeft.getMargin();
                              }
                           } else {
                              var28 = var3;
                           }

                           var2.addGreaterThan(var8.mRight.mSolverVariable, var8.mLeft.mSolverVariable, var3, 1);
                           var2.addLowerThan(var8.mRight.mSolverVariable, var8.mRight.mTarget.mSolverVariable, -var28, 1);
                        }

                        var14 = var8.mHorizontalNextWidget;
                        var10 = var8;
                     }

                     if (var7 == 1) {
                        var10 = var5.mMatchConstraintsChainedWidgets[var3];
                        var34 = var10.mLeft.getMargin();
                        var28 = var34;
                        if (var10.mLeft.mTarget != null) {
                           var28 = var34 + var10.mLeft.mTarget.getMargin();
                        }

                        var35 = var10.mRight.getMargin();
                        var34 = var35;
                        if (var10.mRight.mTarget != null) {
                           var34 = var35 + var10.mRight.mTarget.getMargin();
                        }

                        var27 = var6.mRight.mTarget.mSolverVariable;
                        if (var10 == var5.mChainEnds[3]) {
                           var27 = var5.mChainEnds[1].mRight.mTarget.mSolverVariable;
                        }

                        if (var10.mMatchConstraintDefaultWidth == 1) {
                           var2.addGreaterThan(var6.mLeft.mSolverVariable, var6.mLeft.mTarget.mSolverVariable, var28, 1);
                           var2.addLowerThan(var6.mRight.mSolverVariable, var27, -var34, 1);
                           var2.addEquality(var6.mRight.mSolverVariable, var6.mLeft.mSolverVariable, var6.getWidth(), 2);
                           var9 = var3;
                        } else {
                           var2.addEquality(var10.mLeft.mSolverVariable, var10.mLeft.mTarget.mSolverVariable, var28, 1);
                           var2.addEquality(var10.mRight.mSolverVariable, var27, -var34, 1);
                           var9 = var3;
                        }
                     } else {
                        var35 = var3;
                        var34 = var7;

                        while(true) {
                           var7 = var34 - 1;
                           var9 = var3;
                           if (var35 >= var7) {
                              break;
                           }

                           var14 = var5.mMatchConstraintsChainedWidgets[var35];
                           ConstraintWidget[] var29 = var5.mMatchConstraintsChainedWidgets;
                           ++var35;
                           var15 = var29[var35];
                           SolverVariable var16 = var14.mLeft.mSolverVariable;
                           SolverVariable var17 = var14.mRight.mSolverVariable;
                           SolverVariable var18 = var15.mLeft.mSolverVariable;
                           var27 = var15.mRight.mSolverVariable;
                           if (var15 == var5.mChainEnds[3]) {
                              var27 = var5.mChainEnds[1].mRight.mSolverVariable;
                           }

                           var28 = var14.mLeft.getMargin();
                           int var21 = var28;
                           if (var14.mLeft.mTarget != null) {
                              var21 = var28;
                              if (var14.mLeft.mTarget.mOwner.mRight.mTarget != null) {
                                 var21 = var28;
                                 if (var14.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == var14) {
                                    var21 = var28 + var14.mLeft.mTarget.mOwner.mRight.getMargin();
                                 }
                              }
                           }

                           var2.addGreaterThan(var16, var14.mLeft.mTarget.mSolverVariable, var21, 2);
                           var28 = var14.mRight.getMargin();
                           var21 = var28;
                           if (var14.mRight.mTarget != null) {
                              var21 = var28;
                              if (var14.mHorizontalNextWidget != null) {
                                 if (var14.mHorizontalNextWidget.mLeft.mTarget != null) {
                                    var21 = var14.mHorizontalNextWidget.mLeft.getMargin();
                                 } else {
                                    var21 = 0;
                                 }

                                 var21 += var28;
                              }
                           }

                           var2.addLowerThan(var17, var14.mRight.mTarget.mSolverVariable, -var21, 2);
                           if (var35 == var7) {
                              var28 = var15.mLeft.getMargin();
                              var21 = var28;
                              if (var15.mLeft.mTarget != null) {
                                 var21 = var28;
                                 if (var15.mLeft.mTarget.mOwner.mRight.mTarget != null) {
                                    var21 = var28;
                                    if (var15.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == var15) {
                                       var21 = var28 + var15.mLeft.mTarget.mOwner.mRight.getMargin();
                                    }
                                 }
                              }

                              var2.addGreaterThan(var18, var15.mLeft.mTarget.mSolverVariable, var21, 2);
                              ConstraintAnchor var31 = var15.mRight;
                              if (var15 == var5.mChainEnds[3]) {
                                 var31 = var5.mChainEnds[1].mRight;
                              }

                              var28 = var31.getMargin();
                              var21 = var28;
                              if (var31.mTarget != null) {
                                 var21 = var28;
                                 if (var31.mTarget.mOwner.mLeft.mTarget != null) {
                                    var21 = var28;
                                    if (var31.mTarget.mOwner.mLeft.mTarget.mOwner == var15) {
                                       var21 = var28 + var31.mTarget.mOwner.mLeft.getMargin();
                                    }
                                 }
                              }

                              var2.addLowerThan(var27, var31.mTarget.mSolverVariable, -var21, 2);
                           }

                           if (var6.mMatchConstraintMaxWidth > 0) {
                              var2.addLowerThan(var17, var16, var6.mMatchConstraintMaxWidth, 2);
                           }

                           ArrayRow var32 = var1.createRow();
                           var32.createRowEqualDimension(var14.mHorizontalWeight, var13, var15.mHorizontalWeight, var16, var14.mLeft.getMargin(), var17, var14.mRight.getMargin(), var18, var15.mLeft.getMargin(), var27, var15.mRight.getMargin());
                           var2.addConstraint(var32);
                           var3 = 0;
                        }
                     }
                  }
               }
            }

            var3 = var9;
            var33 = var2;
         }

         ++var4;
         var2 = var33;
      }
   }

   private void applyVerticalChain(LinearSystem var1) {
      LinearSystem var2 = var1;
      byte var3 = 0;
      int var4 = 0;

      while(true) {
         ConstraintWidgetContainer var5 = this;
         if (var4 >= this.mVerticalChainsSize) {
            return;
         }

         LinearSystem var33;
         label338: {
            ConstraintWidget var6 = this.mVerticalChainsArray[var4];
            int var7 = this.countMatchConstraintsChainedWidgets(var2, this.mChainEnds, this.mVerticalChainsArray[var4], 1, this.flags);
            ConstraintWidget var8 = this.mChainEnds[2];
            byte var9;
            if (var8 == null) {
               var9 = var3;
            } else {
               ConstraintWidget var10;
               int var34;
               if (this.flags[1]) {
                  var34 = var6.getDrawY();

                  while(true) {
                     var9 = var3;
                     if (var8 == null) {
                        break;
                     }

                     var2.addEquality(var8.mTop.mSolverVariable, var34);
                     var10 = var8.mVerticalNextWidget;
                     var34 += var8.mTop.getMargin() + var8.getHeight() + var8.mBottom.getMargin();
                     var8 = var10;
                  }
               } else {
                  if (var6.mVerticalChainStyle == 0) {
                     var9 = 1;
                  } else {
                     var9 = var3;
                  }

                  byte var11;
                  if (var6.mVerticalChainStyle == 2) {
                     var11 = 1;
                  } else {
                     var11 = var3;
                  }

                  byte var12;
                  if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                     var12 = 1;
                  } else {
                     var12 = var3;
                  }

                  if ((this.mOptimizationLevel == 2 || this.mOptimizationLevel == 8) && this.flags[var3] && var6.mVerticalChainFixedPosition && var11 == 0 && var12 == 0 && var6.mVerticalChainStyle == 0) {
                     Optimizer.applyDirectResolutionVerticalChain(this, var2, var7, var6);
                     var9 = var3;
                  } else {
                     ConstraintWidget var14;
                     ConstraintWidget var15;
                     SolverVariable var16;
                     SolverVariable var27;
                     int var28;
                     if (var7 == 0 || var11 != 0) {
                        ConstraintWidget var23 = var8;
                        var14 = null;
                        var15 = var14;
                        boolean var30 = false;
                        var33 = var2;
                        var12 = var9;
                        ConstraintWidget var20 = var14;

                        while(true) {
                           ConstraintWidget var41 = var23;
                           SolverVariable var22;
                           if (var23 == null) {
                              var28 = var4;
                              LinearSystem var25 = var33;
                              var12 = 0;
                              var33 = var33;
                              var4 = var4;
                              var3 = var12;
                              if (var11 != 0) {
                                 ConstraintAnchor var42 = var8.mTop;
                                 ConstraintAnchor var39 = var20.mBottom;
                                 var34 = var42.getMargin();
                                 var7 = var39.getMargin();
                                 if (var6.mTop.mTarget != null) {
                                    var27 = var6.mTop.mTarget.mSolverVariable;
                                 } else {
                                    var27 = null;
                                 }

                                 if (var20.mBottom.mTarget != null) {
                                    var22 = var20.mBottom.mTarget.mSolverVariable;
                                 } else {
                                    var22 = null;
                                 }

                                 var33 = var25;
                                 var4 = var28;
                                 var3 = var12;
                                 if (var27 != null) {
                                    var33 = var25;
                                    var4 = var28;
                                    var3 = var12;
                                    if (var22 != null) {
                                       var25.addLowerThan(var39.mSolverVariable, var22, -var7, 1);
                                       var25.addCentering(var42.mSolverVariable, var27, var34, var6.mVerticalBiasPercent, var22, var39.mSolverVariable, var7, 4);
                                       var3 = var12;
                                       var4 = var28;
                                       var33 = var25;
                                    }
                                 }
                              }
                              break label338;
                           }

                           ConstraintWidget var37 = var23.mVerticalNextWidget;
                           if (var37 == null) {
                              var23 = this.mChainEnds[1];
                              var30 = true;
                           } else {
                              var23 = var20;
                           }

                           label339: {
                              SolverVariable var36;
                              ConstraintAnchor var38;
                              if (var11 != 0) {
                                 var38 = var41.mTop;
                                 var7 = var38.getMargin();
                                 var28 = var7;
                                 if (var15 != null) {
                                    var28 = var7 + var15.mBottom.getMargin();
                                 }

                                 byte var26;
                                 if (var8 != var41) {
                                    var26 = 3;
                                 } else {
                                    var26 = 1;
                                 }

                                 if (var38.mTarget != null) {
                                    var22 = var38.mSolverVariable;
                                    var36 = var38.mTarget.mSolverVariable;
                                 } else if (var41.mBaseline.mTarget != null) {
                                    var22 = var41.mBaseline.mSolverVariable;
                                    var36 = var41.mBaseline.mTarget.mSolverVariable;
                                    var28 -= var38.getMargin();
                                 } else {
                                    var22 = null;
                                    var36 = var22;
                                 }

                                 if (var22 != null && var36 != null) {
                                    var33.addGreaterThan(var22, var36, var28, var26);
                                 }

                                 if (var41.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                    ConstraintAnchor var24 = var41.mBottom;
                                    if (var41.mMatchConstraintDefaultHeight == 1) {
                                       var28 = Math.max(var41.mMatchConstraintMinHeight, var41.getHeight());
                                       var33.addEquality(var24.mSolverVariable, var38.mSolverVariable, var28, 3);
                                    } else {
                                       var33.addGreaterThan(var38.mSolverVariable, var38.mTarget.mSolverVariable, var38.mMargin, 3);
                                       var33.addLowerThan(var24.mSolverVariable, var38.mSolverVariable, var41.mMatchConstraintMinHeight, 3);
                                    }
                                 }
                              } else if (var12 == 0 && var30 && var15 != null) {
                                 if (var41.mBottom.mTarget == null) {
                                    var33.addEquality(var41.mBottom.mSolverVariable, var41.getDrawBottom());
                                 } else {
                                    var28 = var41.mBottom.getMargin();
                                    var33.addEquality(var41.mBottom.mSolverVariable, var23.mBottom.mTarget.mSolverVariable, -var28, 5);
                                 }
                              } else {
                                 if (var12 != 0 || var30 || var15 != null) {
                                    var38 = var41.mTop;
                                    ConstraintAnchor var19 = var41.mBottom;
                                    var28 = var38.getMargin();
                                    var7 = var19.getMargin();
                                    var33.addGreaterThan(var38.mSolverVariable, var38.mTarget.mSolverVariable, var28, 1);
                                    var33.addLowerThan(var19.mSolverVariable, var19.mTarget.mSolverVariable, -var7, 1);
                                    if (var38.mTarget != null) {
                                       var36 = var38.mTarget.mSolverVariable;
                                    } else {
                                       var36 = null;
                                    }

                                    if (var15 == null) {
                                       if (var6.mTop.mTarget != null) {
                                          var36 = var6.mTop.mTarget.mSolverVariable;
                                       } else {
                                          var36 = null;
                                       }
                                    }

                                    var20 = var37;
                                    if (var37 == null) {
                                       if (var23.mBottom.mTarget != null) {
                                          var20 = var23.mBottom.mTarget.mOwner;
                                       } else {
                                          var20 = null;
                                       }
                                    }

                                    if (var20 != null) {
                                       var16 = var20.mTop.mSolverVariable;
                                       if (var30) {
                                          if (var23.mBottom.mTarget != null) {
                                             var16 = var23.mBottom.mTarget.mSolverVariable;
                                          } else {
                                             var16 = null;
                                          }
                                       }

                                       if (var36 != null && var16 != null) {
                                          var33.addCentering(var38.mSolverVariable, var36, var28, 0.5F, var16, var19.mSolverVariable, var7, 4);
                                       }
                                    }
                                    break label339;
                                 }

                                 if (var41.mTop.mTarget == null) {
                                    var33.addEquality(var41.mTop.mSolverVariable, var41.getDrawY());
                                 } else {
                                    var28 = var41.mTop.getMargin();
                                    var33.addEquality(var41.mTop.mSolverVariable, var6.mTop.mTarget.mSolverVariable, var28, 5);
                                 }
                              }

                              var20 = var37;
                           }

                           if (var30) {
                              var20 = null;
                           }

                           var10 = var20;
                           var20 = var23;
                           var23 = var10;
                           var15 = var41;
                           var33 = var33;
                        }
                     }

                     float var13 = 0.0F;

                     for(var10 = null; var8 != null; var8 = var14) {
                        if (var8.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                           var34 = var8.mTop.getMargin();
                           var28 = var34;
                           if (var10 != null) {
                              var28 = var34 + var10.mBottom.getMargin();
                           }

                           if (var8.mTop.mTarget.mOwner.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var11 = 2;
                           } else {
                              var11 = 3;
                           }

                           var2.addGreaterThan(var8.mTop.mSolverVariable, var8.mTop.mTarget.mSolverVariable, var28, var11);
                           var34 = var8.mBottom.getMargin();
                           var28 = var34;
                           if (var8.mBottom.mTarget.mOwner.mTop.mTarget != null) {
                              var28 = var34;
                              if (var8.mBottom.mTarget.mOwner.mTop.mTarget.mOwner == var8) {
                                 var28 = var34 + var8.mBottom.mTarget.mOwner.mTop.getMargin();
                              }
                           }

                           if (var8.mBottom.mTarget.mOwner.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var11 = 2;
                           } else {
                              var11 = 3;
                           }

                           var2.addLowerThan(var8.mBottom.mSolverVariable, var8.mBottom.mTarget.mSolverVariable, -var28, var11);
                        } else {
                           var13 += var8.mVerticalWeight;
                           if (var8.mBottom.mTarget != null) {
                              var34 = var8.mBottom.getMargin();
                              var28 = var34;
                              if (var8 != var5.mChainEnds[3]) {
                                 var28 = var34 + var8.mBottom.mTarget.mOwner.mTop.getMargin();
                              }
                           } else {
                              var28 = var3;
                           }

                           var2.addGreaterThan(var8.mBottom.mSolverVariable, var8.mTop.mSolverVariable, var3, 1);
                           var2.addLowerThan(var8.mBottom.mSolverVariable, var8.mBottom.mTarget.mSolverVariable, -var28, 1);
                        }

                        var14 = var8.mVerticalNextWidget;
                        var10 = var8;
                     }

                     int var35;
                     if (var7 == 1) {
                        var10 = var5.mMatchConstraintsChainedWidgets[var3];
                        var34 = var10.mTop.getMargin();
                        var28 = var34;
                        if (var10.mTop.mTarget != null) {
                           var28 = var34 + var10.mTop.mTarget.getMargin();
                        }

                        var35 = var10.mBottom.getMargin();
                        var34 = var35;
                        if (var10.mBottom.mTarget != null) {
                           var34 = var35 + var10.mBottom.mTarget.getMargin();
                        }

                        var27 = var6.mBottom.mTarget.mSolverVariable;
                        if (var10 == var5.mChainEnds[3]) {
                           var27 = var5.mChainEnds[1].mBottom.mTarget.mSolverVariable;
                        }

                        if (var10.mMatchConstraintDefaultHeight == 1) {
                           var2.addGreaterThan(var6.mTop.mSolverVariable, var6.mTop.mTarget.mSolverVariable, var28, 1);
                           var2.addLowerThan(var6.mBottom.mSolverVariable, var27, -var34, 1);
                           var2.addEquality(var6.mBottom.mSolverVariable, var6.mTop.mSolverVariable, var6.getHeight(), 2);
                           var9 = var3;
                        } else {
                           var2.addEquality(var10.mTop.mSolverVariable, var10.mTop.mTarget.mSolverVariable, var28, 1);
                           var2.addEquality(var10.mBottom.mSolverVariable, var27, -var34, 1);
                           var9 = var3;
                        }
                     } else {
                        var35 = var3;
                        var34 = var7;

                        while(true) {
                           var7 = var34 - 1;
                           var9 = var3;
                           if (var35 >= var7) {
                              break;
                           }

                           var14 = var5.mMatchConstraintsChainedWidgets[var35];
                           ConstraintWidget[] var29 = var5.mMatchConstraintsChainedWidgets;
                           ++var35;
                           var15 = var29[var35];
                           var16 = var14.mTop.mSolverVariable;
                           SolverVariable var17 = var14.mBottom.mSolverVariable;
                           SolverVariable var18 = var15.mTop.mSolverVariable;
                           var27 = var15.mBottom.mSolverVariable;
                           if (var15 == var5.mChainEnds[3]) {
                              var27 = var5.mChainEnds[1].mBottom.mSolverVariable;
                           }

                           var28 = var14.mTop.getMargin();
                           int var21 = var28;
                           if (var14.mTop.mTarget != null) {
                              var21 = var28;
                              if (var14.mTop.mTarget.mOwner.mBottom.mTarget != null) {
                                 var21 = var28;
                                 if (var14.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == var14) {
                                    var21 = var28 + var14.mTop.mTarget.mOwner.mBottom.getMargin();
                                 }
                              }
                           }

                           var2.addGreaterThan(var16, var14.mTop.mTarget.mSolverVariable, var21, 2);
                           var28 = var14.mBottom.getMargin();
                           var21 = var28;
                           if (var14.mBottom.mTarget != null) {
                              var21 = var28;
                              if (var14.mVerticalNextWidget != null) {
                                 if (var14.mVerticalNextWidget.mTop.mTarget != null) {
                                    var21 = var14.mVerticalNextWidget.mTop.getMargin();
                                 } else {
                                    var21 = 0;
                                 }

                                 var21 += var28;
                              }
                           }

                           var2.addLowerThan(var17, var14.mBottom.mTarget.mSolverVariable, -var21, 2);
                           if (var35 == var7) {
                              var28 = var15.mTop.getMargin();
                              var21 = var28;
                              if (var15.mTop.mTarget != null) {
                                 var21 = var28;
                                 if (var15.mTop.mTarget.mOwner.mBottom.mTarget != null) {
                                    var21 = var28;
                                    if (var15.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == var15) {
                                       var21 = var28 + var15.mTop.mTarget.mOwner.mBottom.getMargin();
                                    }
                                 }
                              }

                              var2.addGreaterThan(var18, var15.mTop.mTarget.mSolverVariable, var21, 2);
                              ConstraintAnchor var31 = var15.mBottom;
                              if (var15 == var5.mChainEnds[3]) {
                                 var31 = var5.mChainEnds[1].mBottom;
                              }

                              var28 = var31.getMargin();
                              var21 = var28;
                              if (var31.mTarget != null) {
                                 var21 = var28;
                                 if (var31.mTarget.mOwner.mTop.mTarget != null) {
                                    var21 = var28;
                                    if (var31.mTarget.mOwner.mTop.mTarget.mOwner == var15) {
                                       var21 = var28 + var31.mTarget.mOwner.mTop.getMargin();
                                    }
                                 }
                              }

                              var2.addLowerThan(var27, var31.mTarget.mSolverVariable, -var21, 2);
                           }

                           if (var6.mMatchConstraintMaxHeight > 0) {
                              var2.addLowerThan(var17, var16, var6.mMatchConstraintMaxHeight, 2);
                           }

                           ArrayRow var32 = var1.createRow();
                           var32.createRowEqualDimension(var14.mVerticalWeight, var13, var15.mVerticalWeight, var16, var14.mTop.getMargin(), var17, var14.mBottom.getMargin(), var18, var15.mTop.getMargin(), var27, var15.mBottom.getMargin());
                           var2.addConstraint(var32);
                           var3 = 0;
                        }
                     }
                  }
               }
            }

            var3 = var9;
            var33 = var2;
         }

         ++var4;
         var2 = var33;
      }
   }

   private int countMatchConstraintsChainedWidgets(LinearSystem var1, ConstraintWidget[] var2, ConstraintWidget var3, int var4, boolean[] var5) {
      var5[0] = true;
      var5[1] = false;
      var2[0] = null;
      var2[2] = null;
      var2[1] = null;
      var2[3] = null;
      boolean var6;
      ConstraintWidget var7;
      ConstraintWidget var8;
      ConstraintWidget var9;
      ConstraintWidget var10;
      ConstraintWidget var11;
      ConstraintWidget var12;
      int var13;
      boolean var14;
      if (var4 == 0) {
         if (var3.mLeft.mTarget != null && var3.mLeft.mTarget.mOwner != this) {
            var6 = false;
         } else {
            var6 = true;
         }

         var3.mHorizontalNextWidget = null;
         if (var3.getVisibility() != 8) {
            var7 = var3;
         } else {
            var7 = null;
         }

         var4 = 0;
         var8 = null;
         var9 = var7;
         var10 = var3;

         while(true) {
            var11 = var7;
            var12 = var9;
            var13 = var4;
            if (var10.mRight.mTarget == null) {
               break;
            }

            var10.mHorizontalNextWidget = null;
            if (var10.getVisibility() != 8) {
               var11 = var9;
               if (var9 == null) {
                  var11 = var10;
               }

               if (var7 != null && var7 != var10) {
                  var7.mHorizontalNextWidget = var10;
               }

               var7 = var10;
               var9 = var11;
            } else {
               var1.addEquality(var10.mLeft.mSolverVariable, var10.mLeft.mTarget.mSolverVariable, 0, 5);
               var1.addEquality(var10.mRight.mSolverVariable, var10.mLeft.mSolverVariable, 0, 5);
            }

            var13 = var4;
            if (var10.getVisibility() != 8) {
               var13 = var4;
               if (var10.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  if (var10.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     var5[0] = false;
                  }

                  var13 = var4;
                  if (var10.mDimensionRatio <= 0.0F) {
                     var5[0] = false;
                     var13 = var4 + 1;
                     if (var13 >= this.mMatchConstraintsChainedWidgets.length) {
                        this.mMatchConstraintsChainedWidgets = (ConstraintWidget[])Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                     }

                     this.mMatchConstraintsChainedWidgets[var4] = var10;
                  }
               }
            }

            if (var10.mRight.mTarget.mOwner.mLeft.mTarget == null) {
               var11 = var7;
               var12 = var9;
               break;
            }

            if (var10.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != var10) {
               var11 = var7;
               var12 = var9;
               break;
            }

            if (var10.mRight.mTarget.mOwner == var10) {
               var11 = var7;
               var12 = var9;
               break;
            }

            var8 = var10.mRight.mTarget.mOwner;
            var10 = var8;
            var4 = var13;
         }

         var14 = var6;
         if (var10.mRight.mTarget != null) {
            var14 = var6;
            if (var10.mRight.mTarget.mOwner != this) {
               var14 = false;
            }
         }

         if (var3.mLeft.mTarget == null || var8.mRight.mTarget == null) {
            var5[1] = true;
         }

         var3.mHorizontalChainFixedPosition = var14;
         var8.mHorizontalNextWidget = null;
         var2[0] = var3;
         var2[2] = var12;
         var2[1] = var8;
         var2[3] = var11;
      } else {
         if (var3.mTop.mTarget != null && var3.mTop.mTarget.mOwner != this) {
            var6 = false;
         } else {
            var6 = true;
         }

         var3.mVerticalNextWidget = null;
         if (var3.getVisibility() != 8) {
            var9 = var3;
         } else {
            var9 = null;
         }

         var4 = 0;
         var8 = null;
         var7 = var9;
         var10 = var3;

         while(true) {
            var11 = var9;
            var12 = var7;
            var13 = var4;
            if (var10.mBottom.mTarget == null) {
               break;
            }

            var10.mVerticalNextWidget = null;
            if (var10.getVisibility() != 8) {
               var11 = var9;
               if (var9 == null) {
                  var11 = var10;
               }

               if (var7 != null && var7 != var10) {
                  var7.mVerticalNextWidget = var10;
               }

               var7 = var10;
               var9 = var11;
            } else {
               var1.addEquality(var10.mTop.mSolverVariable, var10.mTop.mTarget.mSolverVariable, 0, 5);
               var1.addEquality(var10.mBottom.mSolverVariable, var10.mTop.mSolverVariable, 0, 5);
            }

            if (var10.getVisibility() != 8 && var10.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               if (var10.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var5[0] = false;
               }

               var13 = var4;
               if (var10.mDimensionRatio <= 0.0F) {
                  var5[0] = false;
                  var13 = var4 + 1;
                  if (var13 >= this.mMatchConstraintsChainedWidgets.length) {
                     this.mMatchConstraintsChainedWidgets = (ConstraintWidget[])Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                  }

                  this.mMatchConstraintsChainedWidgets[var4] = var10;
               }
            } else {
               var13 = var4;
            }

            if (var10.mBottom.mTarget.mOwner.mTop.mTarget == null || var10.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != var10 || var10.mBottom.mTarget.mOwner == var10) {
               var11 = var9;
               var12 = var7;
               break;
            }

            var8 = var10.mBottom.mTarget.mOwner;
            var10 = var8;
            var4 = var13;
         }

         var14 = var6;
         if (var10.mBottom.mTarget != null) {
            var14 = var6;
            if (var10.mBottom.mTarget.mOwner != this) {
               var14 = false;
            }
         }

         if (var3.mTop.mTarget == null || var8.mBottom.mTarget == null) {
            var5[1] = true;
         }

         var3.mVerticalChainFixedPosition = var14;
         var8.mVerticalNextWidget = null;
         var2[0] = var3;
         var2[2] = var11;
         var2[1] = var8;
         var2[3] = var12;
      }

      return var13;
   }

   public static ConstraintWidgetContainer createContainer(ConstraintWidgetContainer var0, String var1, ArrayList var2, int var3) {
      Rectangle var4 = getBounds(var2);
      if (var4.width != 0 && var4.height != 0) {
         int var6;
         if (var3 > 0) {
            int var5 = Math.min(var4.x, var4.y);
            var6 = var3;
            if (var3 > var5) {
               var6 = var5;
            }

            var4.grow(var6, var6);
         }

         var0.setOrigin(var4.x, var4.y);
         var0.setDimension(var4.width, var4.height);
         var0.setDebugName(var1);
         var3 = 0;
         ConstraintWidget var7 = ((ConstraintWidget)var2.get(0)).getParent();

         for(var6 = var2.size(); var3 < var6; ++var3) {
            ConstraintWidget var8 = (ConstraintWidget)var2.get(var3);
            if (var8.getParent() == var7) {
               var0.add(var8);
               var8.setX(var8.getX() - var4.x);
               var8.setY(var8.getY() - var4.y);
            }
         }

         return var0;
      } else {
         return null;
      }
   }

   private boolean optimize(LinearSystem var1) {
      int var2 = this.mChildren.size();

      int var3;
      ConstraintWidget var4;
      for(var3 = 0; var3 < var2; ++var3) {
         var4 = (ConstraintWidget)this.mChildren.get(var3);
         var4.mHorizontalResolution = -1;
         var4.mVerticalResolution = -1;
         if (var4.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || var4.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            var4.mHorizontalResolution = 1;
            var4.mVerticalResolution = 1;
         }
      }

      byte var5 = 0;
      int var6 = var5;

      int var7;
      int var9;
      for(var7 = var5; var5 == 0; var6 = var3) {
         int var8 = 0;
         var9 = var8;

         int var11;
         for(var3 = var8; var8 < var2; var3 = var11) {
            var4 = (ConstraintWidget)this.mChildren.get(var8);
            if (var4.mHorizontalResolution == -1) {
               if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  var4.mHorizontalResolution = 1;
               } else {
                  Optimizer.checkHorizontalSimpleDependency(this, var1, var4);
               }
            }

            if (var4.mVerticalResolution == -1) {
               if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  var4.mVerticalResolution = 1;
               } else {
                  Optimizer.checkVerticalSimpleDependency(this, var1, var4);
               }
            }

            int var10 = var9;
            if (var4.mVerticalResolution == -1) {
               var10 = var9 + 1;
            }

            var11 = var3;
            if (var4.mHorizontalResolution == -1) {
               var11 = var3 + 1;
            }

            ++var8;
            var9 = var10;
         }

         byte var14;
         label110: {
            if (var9 != 0 || var3 != 0) {
               var14 = var5;
               if (var7 != var9) {
                  break label110;
               }

               var14 = var5;
               if (var6 != var3) {
                  break label110;
               }
            }

            var14 = 1;
         }

         var5 = var14;
         var7 = var9;
      }

      var9 = 0;
      var7 = var9;

      int var13;
      for(var13 = var9; var9 < var2; var7 = var6) {
         ConstraintWidget var12;
         label64: {
            var12 = (ConstraintWidget)this.mChildren.get(var9);
            if (var12.mHorizontalResolution != 1) {
               var3 = var13;
               if (var12.mHorizontalResolution != -1) {
                  break label64;
               }
            }

            var3 = var13 + 1;
         }

         label69: {
            if (var12.mVerticalResolution != 1) {
               var6 = var7;
               if (var12.mVerticalResolution != -1) {
                  break label69;
               }
            }

            var6 = var7 + 1;
         }

         ++var9;
         var13 = var3;
      }

      if (var13 == 0 && var7 == 0) {
         return true;
      } else {
         return false;
      }
   }

   private void resetChains() {
      this.mHorizontalChainsSize = 0;
      this.mVerticalChainsSize = 0;
   }

   static int setGroup(ConstraintAnchor var0, int var1) {
      int var2 = var0.mGroup;
      if (var0.mOwner.getParent() == null) {
         return var1;
      } else if (var2 <= var1) {
         return var2;
      } else {
         var0.mGroup = var1;
         ConstraintAnchor var3 = var0.getOpposite();
         ConstraintAnchor var4 = var0.mTarget;
         var2 = var1;
         if (var3 != null) {
            var2 = setGroup(var3, var1);
         }

         var1 = var2;
         if (var4 != null) {
            var1 = setGroup(var4, var2);
         }

         var2 = var1;
         if (var3 != null) {
            var2 = setGroup(var3, var1);
         }

         var0.mGroup = var2;
         return var2;
      }
   }

   void addChain(ConstraintWidget var1, int var2) {
      if (var2 != 0) {
         if (var2 == 1) {
            while(var1.mTop.mTarget != null && var1.mTop.mTarget.mOwner.mBottom.mTarget != null && var1.mTop.mTarget.mOwner.mBottom.mTarget == var1.mTop && var1.mTop.mTarget.mOwner != var1) {
               var1 = var1.mTop.mTarget.mOwner;
            }

            this.addVerticalChain(var1);
         }
      } else {
         while(true) {
            if (var1.mLeft.mTarget == null || var1.mLeft.mTarget.mOwner.mRight.mTarget == null || var1.mLeft.mTarget.mOwner.mRight.mTarget != var1.mLeft || var1.mLeft.mTarget.mOwner == var1) {
               this.addHorizontalChain(var1);
               break;
            }

            var1 = var1.mLeft.mTarget.mOwner;
         }
      }

   }

   public boolean addChildrenToSolver(LinearSystem var1, int var2) {
      this.addToSolver(var1, var2);
      int var3 = this.mChildren.size();
      int var4 = this.mOptimizationLevel;
      int var5 = 0;
      boolean var9;
      if (var4 != 2 && this.mOptimizationLevel != 4) {
         var9 = true;
      } else {
         if (this.optimize(var1)) {
            return false;
         }

         var9 = false;
      }

      for(; var5 < var3; ++var5) {
         ConstraintWidget var6 = (ConstraintWidget)this.mChildren.get(var5);
         if (var6 instanceof ConstraintWidgetContainer) {
            ConstraintWidget.DimensionBehaviour var7 = var6.mHorizontalDimensionBehaviour;
            ConstraintWidget.DimensionBehaviour var8 = var6.mVerticalDimensionBehaviour;
            if (var7 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }

            if (var8 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }

            var6.addToSolver(var1, var2);
            if (var7 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setHorizontalDimensionBehaviour(var7);
            }

            if (var8 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var6.setVerticalDimensionBehaviour(var8);
            }
         } else {
            if (var9) {
               Optimizer.checkMatchParent(this, var1, var6);
            }

            var6.addToSolver(var1, var2);
         }
      }

      if (this.mHorizontalChainsSize > 0) {
         this.applyHorizontalChain(var1);
      }

      if (this.mVerticalChainsSize > 0) {
         this.applyVerticalChain(var1);
      }

      return true;
   }

   public void findHorizontalWrapRecursive(ConstraintWidget var1, boolean[] var2) {
      ConstraintWidget.DimensionBehaviour var3 = var1.mHorizontalDimensionBehaviour;
      ConstraintWidget.DimensionBehaviour var4 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
      int var5 = 0;
      if (var3 == var4 && var1.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mDimensionRatio > 0.0F) {
         var2[0] = false;
      } else {
         int var6 = var1.getOptimizerWrapWidth();
         if (var1.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mDimensionRatio > 0.0F) {
            var2[0] = false;
         } else {
            int var8;
            int var10;
            label179: {
               boolean var7 = true;
               var1.mHorizontalWrapVisited = true;
               if (var1 instanceof Guideline) {
                  Guideline var12 = (Guideline)var1;
                  if (var12.getOrientation() == 1) {
                     if (var12.getRelativeBegin() != -1) {
                        var5 = var12.getRelativeBegin();
                        var8 = 0;
                     } else if (var12.getRelativeEnd() != -1) {
                        var8 = var12.getRelativeEnd();
                     } else {
                        var8 = 0;
                     }
                  } else {
                     var5 = var6;
                     var8 = var6;
                  }
               } else {
                  if (var1.mRight.isConnected() || var1.mLeft.isConnected()) {
                     if (var1.mRight.mTarget != null && var1.mLeft.mTarget != null && (var1.mRight.mTarget == var1.mLeft.mTarget || var1.mRight.mTarget.mOwner == var1.mLeft.mTarget.mOwner && var1.mRight.mTarget.mOwner != var1.mParent)) {
                        var2[0] = false;
                        return;
                     }

                     ConstraintAnchor var14 = var1.mRight.mTarget;
                     ConstraintWidget var13 = null;
                     ConstraintWidget var9;
                     ConstraintWidget var15;
                     if (var14 != null) {
                        var9 = var1.mRight.mTarget.mOwner;
                        var8 = var1.mRight.getMargin() + var6;
                        var15 = var9;
                        var10 = var8;
                        if (!var9.isRoot()) {
                           var15 = var9;
                           var10 = var8;
                           if (!var9.mHorizontalWrapVisited) {
                              this.findHorizontalWrapRecursive(var9, var2);
                              var15 = var9;
                              var10 = var8;
                           }
                        }
                     } else {
                        var10 = var6;
                        var15 = null;
                     }

                     var8 = var6;
                     if (var1.mLeft.mTarget != null) {
                        var9 = var1.mLeft.mTarget.mOwner;
                        var5 = var6 + var1.mLeft.getMargin();
                        var8 = var5;
                        var13 = var9;
                        if (!var9.isRoot()) {
                           var8 = var5;
                           var13 = var9;
                           if (!var9.mHorizontalWrapVisited) {
                              this.findHorizontalWrapRecursive(var9, var2);
                              var13 = var9;
                              var8 = var5;
                           }
                        }
                     }

                     var5 = var10;
                     boolean var11;
                     if (var1.mRight.mTarget != null) {
                        var5 = var10;
                        if (!var15.isRoot()) {
                           if (var1.mRight.mTarget.mType == ConstraintAnchor.Type.RIGHT) {
                              var6 = var10 + (var15.mDistToRight - var15.getOptimizerWrapWidth());
                           } else {
                              var6 = var10;
                              if (var1.mRight.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                                 var6 = var10 + var15.mDistToRight;
                              }
                           }

                           if (!var15.mRightHasCentered && (var15.mLeft.mTarget == null || var15.mRight.mTarget == null || var15.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)) {
                              var11 = false;
                           } else {
                              var11 = true;
                           }

                           var1.mRightHasCentered = var11;
                           var5 = var6;
                           if (var1.mRightHasCentered) {
                              label176: {
                                 if (var15.mLeft.mTarget != null) {
                                    var5 = var6;
                                    if (var15.mLeft.mTarget.mOwner == var1) {
                                       break label176;
                                    }
                                 }

                                 var5 = var6 + (var6 - var15.mDistToRight);
                              }
                           }
                        }
                     }

                     label182: {
                        var10 = var8;
                        if (var1.mLeft.mTarget != null) {
                           var10 = var8;
                           if (!var13.isRoot()) {
                              if (var1.mLeft.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                                 var6 = var8 + (var13.mDistToLeft - var13.getOptimizerWrapWidth());
                              } else {
                                 var6 = var8;
                                 if (var1.mLeft.mTarget.getType() == ConstraintAnchor.Type.RIGHT) {
                                    var6 = var8 + var13.mDistToLeft;
                                 }
                              }

                              var11 = var7;
                              if (!var13.mLeftHasCentered) {
                                 if (var13.mLeft.mTarget != null && var13.mRight.mTarget != null && var13.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                                    var11 = var7;
                                 } else {
                                    var11 = false;
                                 }
                              }

                              var1.mLeftHasCentered = var11;
                              var10 = var6;
                              if (var1.mLeftHasCentered) {
                                 if (var13.mRight.mTarget == null) {
                                    break label182;
                                 }

                                 var10 = var6;
                                 if (var13.mRight.mTarget.mOwner != var1) {
                                    break label182;
                                 }
                              }
                           }
                        }

                        var8 = var10;
                        break label179;
                     }

                     var8 = var6 + (var6 - var13.mDistToLeft);
                     break label179;
                  }

                  var5 = var6 + var1.getX();
                  var8 = var6;
               }

               var6 = var8;
               var8 = var5;
               var5 = var6;
            }

            var10 = var8;
            var6 = var5;
            if (var1.getVisibility() == 8) {
               var10 = var8 - var1.mWidth;
               var6 = var5 - var1.mWidth;
            }

            var1.mDistToLeft = var10;
            var1.mDistToRight = var6;
         }
      }
   }

   public void findVerticalWrapRecursive(ConstraintWidget var1, boolean[] var2) {
      ConstraintWidget.DimensionBehaviour var3 = var1.mVerticalDimensionBehaviour;
      ConstraintWidget.DimensionBehaviour var4 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
      int var5 = 0;
      if (var3 == var4 && var1.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var1.mDimensionRatio > 0.0F) {
         var2[0] = false;
      } else {
         int var6;
         int var8;
         int var10;
         label173: {
            var6 = var1.getOptimizerWrapHeight();
            boolean var7 = true;
            var1.mVerticalWrapVisited = true;
            if (var1 instanceof Guideline) {
               Guideline var13 = (Guideline)var1;
               if (var13.getOrientation() == 0) {
                  if (var13.getRelativeBegin() != -1) {
                     var5 = var13.getRelativeBegin();
                     var6 = 0;
                  } else if (var13.getRelativeEnd() != -1) {
                     var6 = var13.getRelativeEnd();
                  } else {
                     var6 = 0;
                  }
               } else {
                  var5 = var6;
               }
            } else {
               if (var1.mBaseline.mTarget != null || var1.mTop.mTarget != null || var1.mBottom.mTarget != null) {
                  if (var1.mBottom.mTarget != null && var1.mTop.mTarget != null && (var1.mBottom.mTarget == var1.mTop.mTarget || var1.mBottom.mTarget.mOwner == var1.mTop.mTarget.mOwner && var1.mBottom.mTarget.mOwner != var1.mParent)) {
                     var2[0] = false;
                     return;
                  }

                  int var9;
                  ConstraintWidget var14;
                  if (var1.mBaseline.isConnected()) {
                     var14 = var1.mBaseline.mTarget.getOwner();
                     if (!var14.mVerticalWrapVisited) {
                        this.findVerticalWrapRecursive(var14, var2);
                     }

                     var9 = Math.max(var14.mDistToTop - var14.mHeight + var6, var6);
                     var10 = Math.max(var14.mDistToBottom - var14.mHeight + var6, var6);
                     var5 = var10;
                     var6 = var9;
                     if (var1.getVisibility() == 8) {
                        var6 = var9 - var1.mHeight;
                        var5 = var10 - var1.mHeight;
                     }

                     var1.mDistToTop = var6;
                     var1.mDistToBottom = var5;
                     return;
                  }

                  boolean var11 = var1.mTop.isConnected();
                  ConstraintWidget var15 = null;
                  ConstraintWidget var12;
                  if (var11) {
                     var12 = var1.mTop.mTarget.getOwner();
                     var5 = var1.mTop.getMargin() + var6;
                     var14 = var12;
                     var9 = var5;
                     if (!var12.isRoot()) {
                        var14 = var12;
                        var9 = var5;
                        if (!var12.mVerticalWrapVisited) {
                           this.findVerticalWrapRecursive(var12, var2);
                           var14 = var12;
                           var9 = var5;
                        }
                     }
                  } else {
                     var9 = var6;
                     var14 = null;
                  }

                  var5 = var6;
                  if (var1.mBottom.isConnected()) {
                     var12 = var1.mBottom.mTarget.getOwner();
                     var6 += var1.mBottom.getMargin();
                     var5 = var6;
                     var15 = var12;
                     if (!var12.isRoot()) {
                        var5 = var6;
                        var15 = var12;
                        if (!var12.mVerticalWrapVisited) {
                           this.findVerticalWrapRecursive(var12, var2);
                           var15 = var12;
                           var5 = var6;
                        }
                     }
                  }

                  var10 = var9;
                  if (var1.mTop.mTarget != null) {
                     var10 = var9;
                     if (!var14.isRoot()) {
                        if (var1.mTop.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                           var6 = var9 + (var14.mDistToTop - var14.getOptimizerWrapHeight());
                        } else {
                           var6 = var9;
                           if (var1.mTop.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                              var6 = var9 + var14.mDistToTop;
                           }
                        }

                        if (var14.mTopHasCentered || var14.mTop.mTarget != null && var14.mTop.mTarget.mOwner != var1 && var14.mBottom.mTarget != null && var14.mBottom.mTarget.mOwner != var1 && var14.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                           var11 = true;
                        } else {
                           var11 = false;
                        }

                        var1.mTopHasCentered = var11;
                        var10 = var6;
                        if (var1.mTopHasCentered) {
                           label189: {
                              if (var14.mBottom.mTarget != null) {
                                 var10 = var6;
                                 if (var14.mBottom.mTarget.mOwner == var1) {
                                    break label189;
                                 }
                              }

                              var10 = var6 + (var6 - var14.mDistToTop);
                           }
                        }
                     }
                  }

                  var6 = var5;
                  var8 = var10;
                  if (var1.mBottom.mTarget != null) {
                     var6 = var5;
                     var8 = var10;
                     if (!var15.isRoot()) {
                        if (var1.mBottom.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                           var9 = var5 + (var15.mDistToBottom - var15.getOptimizerWrapHeight());
                        } else {
                           var9 = var5;
                           if (var1.mBottom.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                              var9 = var5 + var15.mDistToBottom;
                           }
                        }

                        var11 = var7;
                        if (!var15.mBottomHasCentered) {
                           if (var15.mTop.mTarget != null && var15.mTop.mTarget.mOwner != var1 && var15.mBottom.mTarget != null && var15.mBottom.mTarget.mOwner != var1 && var15.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                              var11 = var7;
                           } else {
                              var11 = false;
                           }
                        }

                        var1.mBottomHasCentered = var11;
                        var6 = var9;
                        var8 = var10;
                        if (var1.mBottomHasCentered) {
                           if (var15.mTop.mTarget != null) {
                              var6 = var9;
                              var8 = var10;
                              if (var15.mTop.mTarget.mOwner == var1) {
                                 break label173;
                              }
                           }

                           var6 = var9 + (var9 - var15.mDistToBottom);
                           var8 = var10;
                        }
                     }
                  }
                  break label173;
               }

               var5 = var6 + var1.getY();
            }

            var8 = var5;
         }

         var10 = var6;
         var5 = var8;
         if (var1.getVisibility() == 8) {
            var5 = var8 - var1.mHeight;
            var10 = var6 - var1.mHeight;
         }

         var1.mDistToTop = var5;
         var1.mDistToBottom = var10;
      }
   }

   public void findWrapSize(ArrayList var1, boolean[] var2) {
      int var3 = var1.size();
      var2[0] = true;
      byte var4 = 0;
      int var10 = var4;
      int var11 = var4;
      int var9 = var4;
      int var8 = var4;
      int var12 = var4;
      int var13 = var4;

      int var5;
      for(int var7 = var4; var7 < var3; ++var7) {
         ConstraintWidget var14 = (ConstraintWidget)var1.get(var7);
         if (!var14.isRoot()) {
            if (!var14.mHorizontalWrapVisited) {
               this.findHorizontalWrapRecursive(var14, var2);
            }

            if (!var14.mVerticalWrapVisited) {
               this.findVerticalWrapRecursive(var14, var2);
            }

            if (!var2[0]) {
               return;
            }

            var5 = var14.mDistToLeft;
            int var15 = var14.mDistToRight;
            int var16 = var14.getWidth();
            int var17 = var14.mDistToTop;
            int var6 = var14.mDistToBottom;
            int var19 = var14.getHeight();
            if (var14.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
               var5 = var14.getWidth() + var14.mLeft.mMargin + var14.mRight.mMargin;
            } else {
               var5 = var5 + var15 - var16;
            }

            if (var14.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
               var6 = var14.getHeight() + var14.mTop.mMargin + var14.mBottom.mMargin;
            } else {
               var6 = var17 + var6 - var19;
            }

            if (var14.getVisibility() == 8) {
               var5 = 0;
               var6 = 0;
            }

            var13 = Math.max(var13, var14.mDistToLeft);
            var12 = Math.max(var12, var14.mDistToRight);
            var11 = Math.max(var11, var14.mDistToBottom);
            var9 = Math.max(var9, var14.mDistToTop);
            var8 = Math.max(var8, var5);
            var10 = Math.max(var10, var6);
         }
      }

      var5 = Math.max(var13, var12);
      this.mWrapWidth = Math.max(this.mMinWidth, Math.max(var5, var8));
      var5 = Math.max(var9, var11);
      this.mWrapHeight = Math.max(this.mMinHeight, Math.max(var5, var10));

      for(var5 = 0; var5 < var3; ++var5) {
         ConstraintWidget var18 = (ConstraintWidget)var1.get(var5);
         var18.mHorizontalWrapVisited = false;
         var18.mVerticalWrapVisited = false;
         var18.mLeftHasCentered = false;
         var18.mRightHasCentered = false;
         var18.mTopHasCentered = false;
         var18.mBottomHasCentered = false;
      }

   }

   public ArrayList getHorizontalGuidelines() {
      ArrayList var1 = new ArrayList();
      int var2 = this.mChildren.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var3);
         if (var4 instanceof Guideline) {
            Guideline var5 = (Guideline)var4;
            if (var5.getOrientation() == 0) {
               var1.add(var5);
            }
         }
      }

      return var1;
   }

   public LinearSystem getSystem() {
      return this.mSystem;
   }

   public String getType() {
      return "ConstraintLayout";
   }

   public ArrayList getVerticalGuidelines() {
      ArrayList var1 = new ArrayList();
      int var2 = this.mChildren.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         ConstraintWidget var4 = (ConstraintWidget)this.mChildren.get(var3);
         if (var4 instanceof Guideline) {
            Guideline var5 = (Guideline)var4;
            if (var5.getOrientation() == 1) {
               var1.add(var5);
            }
         }
      }

      return var1;
   }

   public boolean handlesInternalConstraints() {
      return false;
   }

   public boolean isHeightMeasuredTooSmall() {
      return this.mHeightMeasuredTooSmall;
   }

   public boolean isWidthMeasuredTooSmall() {
      return this.mWidthMeasuredTooSmall;
   }

   public void layout() {
      int var1 = this.mX;
      int var2 = this.mY;
      int var3 = Math.max(0, this.getWidth());
      int var4 = Math.max(0, this.getHeight());
      this.mWidthMeasuredTooSmall = false;
      this.mHeightMeasuredTooSmall = false;
      if (this.mParent != null) {
         if (this.mSnapshot == null) {
            this.mSnapshot = new Snapshot(this);
         }

         this.mSnapshot.updateFrom(this);
         this.setX(this.mPaddingLeft);
         this.setY(this.mPaddingTop);
         this.resetAnchors();
         this.resetSolverVariables(this.mSystem.getCache());
      } else {
         this.mX = 0;
         this.mY = 0;
      }

      ConstraintWidget.DimensionBehaviour var5 = this.mVerticalDimensionBehaviour;
      ConstraintWidget.DimensionBehaviour var6 = this.mHorizontalDimensionBehaviour;
      int var7 = this.mOptimizationLevel;
      byte var8 = 1;
      boolean var9;
      boolean var10;
      if (var7 != 2 || this.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
         var9 = false;
      } else {
         this.findWrapSize(this.mChildren, this.flags);
         var9 = this.flags[0];
         var10 = var9;
         if (var3 > 0) {
            var10 = var9;
            if (var4 > 0) {
               label221: {
                  if (this.mWrapWidth <= var3) {
                     var10 = var9;
                     if (this.mWrapHeight <= var4) {
                        break label221;
                     }
                  }

                  var10 = false;
               }
            }
         }

         var9 = var10;
         if (var10) {
            if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
               if (var3 > 0 && var3 < this.mWrapWidth) {
                  this.mWidthMeasuredTooSmall = true;
                  this.setWidth(var3);
               } else {
                  this.setWidth(Math.max(this.mMinWidth, this.mWrapWidth));
               }
            }

            var9 = var10;
            if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
               if (var4 > 0 && var4 < this.mWrapHeight) {
                  this.mHeightMeasuredTooSmall = true;
                  this.setHeight(var4);
                  var9 = var10;
               } else {
                  this.setHeight(Math.max(this.mMinHeight, this.mWrapHeight));
                  var9 = var10;
               }
            }
         }
      }

      this.resetChains();
      int var11 = this.mChildren.size();

      ConstraintWidget var12;
      for(var7 = 0; var7 < var11; ++var7) {
         var12 = (ConstraintWidget)this.mChildren.get(var7);
         if (var12 instanceof WidgetContainer) {
            ((WidgetContainer)var12).layout();
         }
      }

      int var13 = 0;
      var10 = var9;
      var9 = true;

      int var14;
      for(byte var21 = var8; var9; var13 = var14) {
         var14 = var13 + var21;

         boolean var15;
         label167: {
            label166: {
               Exception var23;
               label217: {
                  try {
                     this.mSystem.reset();
                     var15 = this.addChildrenToSolver(this.mSystem, Integer.MAX_VALUE);
                  } catch (Exception var20) {
                     var23 = var20;
                     break label217;
                  }

                  var9 = var15;
                  if (!var15) {
                     break label167;
                  }

                  try {
                     this.mSystem.minimize();
                     break label166;
                  } catch (Exception var19) {
                     var23 = var19;
                     var9 = var15;
                  }
               }

               var23.printStackTrace();
               break label167;
            }

            var9 = var15;
         }

         label155: {
            if (var9) {
               this.updateChildrenFromSolver(this.mSystem, Integer.MAX_VALUE, this.flags);
            } else {
               this.updateFromSolver(this.mSystem, Integer.MAX_VALUE);

               for(var13 = 0; var13 < var11; ++var13) {
                  var12 = (ConstraintWidget)this.mChildren.get(var13);
                  if (var12.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var12.getWidth() < var12.getWrapWidth()) {
                     this.flags[2] = (boolean)var21;
                     break;
                  }

                  if (var12.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var12.getHeight() < var12.getWrapHeight()) {
                     boolean[] var24 = this.flags;
                     byte var25 = 2;
                     var24[2] = (boolean)var21;
                     var21 = var25;
                     break label155;
                  }
               }
            }

            var21 = 2;
         }

         boolean var16;
         if (var14 < 8 && this.flags[var21]) {
            var13 = 0;
            int var22 = 0;

            for(var7 = 0; var13 < var11; ++var13) {
               var12 = (ConstraintWidget)this.mChildren.get(var13);
               var22 = Math.max(var22, var12.mX + var12.getWidth());
               var7 = Math.max(var7, var12.mY + var12.getHeight());
            }

            var13 = Math.max(this.mMinWidth, var22);
            var7 = Math.max(this.mMinHeight, var7);
            if (var6 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.getWidth() < var13) {
               this.setWidth(var13);
               this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
               var15 = true;
               var16 = true;
            } else {
               var15 = false;
               var16 = var10;
            }

            var9 = var15;
            var10 = var16;
            if (var5 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
               var9 = var15;
               var10 = var16;
               if (this.getHeight() < var7) {
                  this.setHeight(var7);
                  this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                  var9 = true;
                  var10 = true;
               }
            }
         } else {
            var9 = false;
         }

         var7 = Math.max(this.mMinWidth, this.getWidth());
         var15 = var9;
         var9 = var10;
         if (var7 > this.getWidth()) {
            this.setWidth(var7);
            this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
            var15 = true;
            var9 = true;
         }

         var7 = Math.max(this.mMinHeight, this.getHeight());
         var10 = var15;
         if (var7 > this.getHeight()) {
            this.setHeight(var7);
            this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
            var10 = true;
            var9 = true;
         }

         label121: {
            var15 = var10;
            boolean var17 = var9;
            if (!var9) {
               boolean var18 = var10;
               var16 = var9;
               if (this.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  var18 = var10;
                  var16 = var9;
                  if (var3 > 0) {
                     var18 = var10;
                     var16 = var9;
                     if (this.getWidth() > var3) {
                        this.mWidthMeasuredTooSmall = true;
                        this.mHorizontalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                        this.setWidth(var3);
                        var18 = true;
                        var16 = true;
                     }
                  }
               }

               var15 = var18;
               var17 = var16;
               if (this.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  var15 = var18;
                  var17 = var16;
                  if (var4 > 0) {
                     var15 = var18;
                     var17 = var16;
                     if (this.getHeight() > var4) {
                        this.mHeightMeasuredTooSmall = true;
                        this.mVerticalDimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
                        this.setHeight(var4);
                        var15 = true;
                        var10 = var15;
                        break label121;
                     }
                  }
               }
            }

            var10 = var17;
         }

         var21 = 1;
         var9 = var15;
      }

      if (this.mParent != null) {
         var13 = Math.max(this.mMinWidth, this.getWidth());
         var7 = Math.max(this.mMinHeight, this.getHeight());
         this.mSnapshot.applyTo(this);
         this.setWidth(var13 + this.mPaddingLeft + this.mPaddingRight);
         this.setHeight(var7 + this.mPaddingTop + this.mPaddingBottom);
      } else {
         this.mX = var1;
         this.mY = var2;
      }

      if (var10) {
         this.mHorizontalDimensionBehaviour = var6;
         this.mVerticalDimensionBehaviour = var5;
      }

      this.resetSolverVariables(this.mSystem.getCache());
      if (this == this.getRootConstraintContainer()) {
         this.updateDrawPosition();
      }

   }

   public int layoutFindGroups() {
      ConstraintAnchor.Type[] var1 = new ConstraintAnchor.Type[5];
      ConstraintAnchor.Type var2 = ConstraintAnchor.Type.LEFT;
      byte var3 = 0;
      var1[0] = var2;
      var1[1] = ConstraintAnchor.Type.RIGHT;
      var1[2] = ConstraintAnchor.Type.TOP;
      var1[3] = ConstraintAnchor.Type.BASELINE;
      var1[4] = ConstraintAnchor.Type.BOTTOM;
      int var4 = this.mChildren.size();
      int var5 = 0;

      int var6;
      ConstraintAnchor var7;
      int var8;
      int var9;
      ConstraintAnchor var14;
      for(var6 = 1; var5 < var4; ++var5) {
         ConstraintWidget var13 = (ConstraintWidget)this.mChildren.get(var5);
         var7 = var13.mLeft;
         if (var7.mTarget != null) {
            var8 = var6;
            if (setGroup(var7, var6) == var6) {
               var8 = var6 + 1;
            }
         } else {
            var7.mGroup = Integer.MAX_VALUE;
            var8 = var6;
         }

         var7 = var13.mTop;
         if (var7.mTarget != null) {
            var6 = var8;
            if (setGroup(var7, var8) == var8) {
               var6 = var8 + 1;
            }
         } else {
            var7.mGroup = Integer.MAX_VALUE;
            var6 = var8;
         }

         var7 = var13.mRight;
         if (var7.mTarget != null) {
            var8 = var6;
            if (setGroup(var7, var6) == var6) {
               var8 = var6 + 1;
            }
         } else {
            var7.mGroup = Integer.MAX_VALUE;
            var8 = var6;
         }

         var7 = var13.mBottom;
         if (var7.mTarget != null) {
            var9 = var8;
            if (setGroup(var7, var8) == var8) {
               var9 = var8 + 1;
            }
         } else {
            var7.mGroup = Integer.MAX_VALUE;
            var9 = var8;
         }

         var14 = var13.mBaseline;
         if (var14.mTarget != null) {
            var6 = var9;
            if (setGroup(var14, var9) == var9) {
               var6 = var9 + 1;
            }
         } else {
            var14.mGroup = Integer.MAX_VALUE;
            var6 = var9;
         }
      }

      var6 = 1;

      while(var6 != 0) {
         var9 = 0;

         for(var6 = var9; var9 < var4; ++var9) {
            ConstraintWidget var16 = (ConstraintWidget)this.mChildren.get(var9);

            for(var5 = 0; var5 < var1.length; ++var5) {
               ConstraintAnchor.Type var10 = var1[var5];
               var14 = null;
               switch(var10) {
               case LEFT:
                  var14 = var16.mLeft;
                  break;
               case TOP:
                  var14 = var16.mTop;
                  break;
               case RIGHT:
                  var14 = var16.mRight;
                  break;
               case BOTTOM:
                  var14 = var16.mBottom;
                  break;
               case BASELINE:
                  var14 = var16.mBaseline;
               }

               ConstraintAnchor var17 = var14.mTarget;
               if (var17 != null) {
                  var8 = var6;
                  if (var17.mOwner.getParent() != null) {
                     var8 = var6;
                     if (var17.mGroup != var14.mGroup) {
                        if (var14.mGroup > var17.mGroup) {
                           var6 = var17.mGroup;
                        } else {
                           var6 = var14.mGroup;
                        }

                        var14.mGroup = var6;
                        var17.mGroup = var6;
                        var8 = 1;
                     }
                  }

                  var17 = var17.getOpposite();
                  var6 = var8;
                  if (var17 != null) {
                     var6 = var8;
                     if (var17.mGroup != var14.mGroup) {
                        if (var14.mGroup > var17.mGroup) {
                           var6 = var17.mGroup;
                        } else {
                           var6 = var14.mGroup;
                        }

                        var14.mGroup = var6;
                        var17.mGroup = var6;
                        var6 = 1;
                     }
                  }
               }
            }
         }
      }

      int[] var15 = new int[this.mChildren.size() * var1.length + 1];
      Arrays.fill(var15, -1);
      var8 = 0;

      for(var5 = var3; var5 < var4; ++var5) {
         ConstraintWidget var11 = (ConstraintWidget)this.mChildren.get(var5);
         var7 = var11.mLeft;
         var6 = var8;
         if (var7.mGroup != Integer.MAX_VALUE) {
            var9 = var7.mGroup;
            var6 = var8;
            if (var15[var9] == -1) {
               var15[var9] = var8;
               var6 = var8 + 1;
            }

            var7.mGroup = var15[var9];
         }

         var7 = var11.mTop;
         var9 = var6;
         if (var7.mGroup != Integer.MAX_VALUE) {
            var8 = var7.mGroup;
            var9 = var6;
            if (var15[var8] == -1) {
               var15[var8] = var6;
               var9 = var6 + 1;
            }

            var7.mGroup = var15[var8];
         }

         var7 = var11.mRight;
         var8 = var9;
         if (var7.mGroup != Integer.MAX_VALUE) {
            var6 = var7.mGroup;
            var8 = var9;
            if (var15[var6] == -1) {
               var15[var6] = var9;
               var8 = var9 + 1;
            }

            var7.mGroup = var15[var6];
         }

         var7 = var11.mBottom;
         var6 = var8;
         if (var7.mGroup != Integer.MAX_VALUE) {
            var9 = var7.mGroup;
            var6 = var8;
            if (var15[var9] == -1) {
               var15[var9] = var8;
               var6 = var8 + 1;
            }

            var7.mGroup = var15[var9];
         }

         ConstraintAnchor var12 = var11.mBaseline;
         var8 = var6;
         if (var12.mGroup != Integer.MAX_VALUE) {
            var9 = var12.mGroup;
            var8 = var6;
            if (var15[var9] == -1) {
               var15[var9] = var6;
               var8 = var6 + 1;
            }

            var12.mGroup = var15[var9];
         }
      }

      return var8;
   }

   public int layoutFindGroupsSimple() {
      int var1 = this.mChildren.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         ConstraintWidget var3 = (ConstraintWidget)this.mChildren.get(var2);
         var3.mLeft.mGroup = 0;
         var3.mRight.mGroup = 0;
         var3.mTop.mGroup = 1;
         var3.mBottom.mGroup = 1;
         var3.mBaseline.mGroup = 1;
      }

      return 2;
   }

   public void layoutWithGroup(int var1) {
      int var2 = this.mX;
      int var3 = this.mY;
      ConstraintWidget var4 = this.mParent;
      byte var5 = 0;
      if (var4 != null) {
         if (this.mSnapshot == null) {
            this.mSnapshot = new Snapshot(this);
         }

         this.mSnapshot.updateFrom(this);
         this.mX = 0;
         this.mY = 0;
         this.resetAnchors();
         this.resetSolverVariables(this.mSystem.getCache());
      } else {
         this.mX = 0;
         this.mY = 0;
      }

      int var6 = this.mChildren.size();

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         var4 = (ConstraintWidget)this.mChildren.get(var7);
         if (var4 instanceof WidgetContainer) {
            ((WidgetContainer)var4).layout();
         }
      }

      this.mLeft.mGroup = 0;
      this.mRight.mGroup = 0;
      this.mTop.mGroup = 1;
      this.mBottom.mGroup = 1;
      this.mSystem.reset();

      for(var7 = var5; var7 < var1; ++var7) {
         try {
            this.addToSolver(this.mSystem, var7);
            this.mSystem.minimize();
            this.updateFromSolver(this.mSystem, var7);
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         this.updateFromSolver(this.mSystem, -2);
      }

      if (this.mParent != null) {
         var1 = this.getWidth();
         var7 = this.getHeight();
         this.mSnapshot.applyTo(this);
         this.setWidth(var1);
         this.setHeight(var7);
      } else {
         this.mX = var2;
         this.mY = var3;
      }

      if (this == this.getRootConstraintContainer()) {
         this.updateDrawPosition();
      }

   }

   public void reset() {
      this.mSystem.reset();
      this.mPaddingLeft = 0;
      this.mPaddingRight = 0;
      this.mPaddingTop = 0;
      this.mPaddingBottom = 0;
      super.reset();
   }

   public void setOptimizationLevel(int var1) {
      this.mOptimizationLevel = var1;
   }

   public void setPadding(int var1, int var2, int var3, int var4) {
      this.mPaddingLeft = var1;
      this.mPaddingTop = var2;
      this.mPaddingRight = var3;
      this.mPaddingBottom = var4;
   }

   public void updateChildrenFromSolver(LinearSystem var1, int var2, boolean[] var3) {
      int var4 = 0;
      var3[2] = false;
      this.updateFromSolver(var1, var2);

      for(int var5 = this.mChildren.size(); var4 < var5; ++var4) {
         ConstraintWidget var6 = (ConstraintWidget)this.mChildren.get(var4);
         var6.updateFromSolver(var1, var2);
         if (var6.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var6.getWidth() < var6.getWrapWidth()) {
            var3[2] = true;
         }

         if (var6.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var6.getHeight() < var6.getWrapHeight()) {
            var3[2] = true;
         }
      }

   }
}
