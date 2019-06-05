package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

class Chain {
   static void applyChainConstraints(ConstraintWidgetContainer var0, LinearSystem var1, int var2) {
      int var3 = 0;
      int var4;
      ChainHead[] var5;
      byte var6;
      if (var2 == 0) {
         var4 = var0.mHorizontalChainsSize;
         var5 = var0.mHorizontalChainsArray;
         var6 = 0;
      } else {
         var6 = 2;
         var4 = var0.mVerticalChainsSize;
         var5 = var0.mVerticalChainsArray;
      }

      for(; var3 < var4; ++var3) {
         ChainHead var7 = var5[var3];
         var7.define();
         if (var0.optimizeFor(4)) {
            if (!Optimizer.applyChainOptimized(var0, var1, var2, var6, var7)) {
               applyChainConstraints(var0, var1, var2, var6, var7);
            }
         } else {
            applyChainConstraints(var0, var1, var2, var6, var7);
         }
      }

   }

   static void applyChainConstraints(ConstraintWidgetContainer var0, LinearSystem var1, int var2, int var3, ChainHead var4) {
      ConstraintWidget var5 = var4.mFirst;
      ConstraintWidget var6 = var4.mLast;
      ConstraintWidget var7 = var4.mFirstVisibleWidget;
      ConstraintWidget var8 = var4.mLastVisibleWidget;
      ConstraintWidget var9 = var4.mHead;
      float var10 = var4.mTotalWeight;
      ConstraintWidget var11 = var4.mFirstMatchConstraintWidget;
      var11 = var4.mLastMatchConstraintWidget;
      boolean var12;
      if (var0.mListDimensionBehaviors[var2] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
         var12 = true;
      } else {
         var12 = false;
      }

      boolean var13;
      boolean var14;
      boolean var15;
      boolean var16;
      label421: {
         label420: {
            if (var2 == 0) {
               if (var9.mHorizontalChainStyle == 0) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               if (var9.mHorizontalChainStyle == 1) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               var15 = var14;
               var16 = var13;
               if (var9.mHorizontalChainStyle == 2) {
                  var15 = var14;
                  break label420;
               }
            } else {
               if (var9.mVerticalChainStyle == 0) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               if (var9.mVerticalChainStyle == 1) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               var15 = var14;
               var16 = var13;
               if (var9.mVerticalChainStyle == 2) {
                  var15 = var14;
                  break label420;
               }
            }

            var14 = false;
            break label421;
         }

         var14 = true;
         var16 = var13;
      }

      ConstraintWidget var17 = var5;
      var13 = false;
      boolean var18 = var14;

      while(true) {
         SolverVariable var19 = null;
         int var21;
         ConstraintAnchor var38;
         if (var13) {
            ConstraintAnchor[] var40;
            int var42;
            if (var8 != null) {
               var40 = var6.mListAnchors;
               var42 = var3 + 1;
               if (var40[var42].mTarget != null) {
                  var38 = var8.mListAnchors[var42];
                  var1.addLowerThan(var38.mSolverVariable, var6.mListAnchors[var42].mTarget.mSolverVariable, -var38.getMargin(), 5);
               }
            }

            ConstraintAnchor[] var27;
            if (var12) {
               var27 = var0.mListAnchors;
               var42 = var3 + 1;
               var1.addGreaterThan(var27[var42].mSolverVariable, var6.mListAnchors[var42].mSolverVariable, var6.mListAnchors[var42].getMargin(), 6);
            }

            ArrayList var28 = var4.mWeightedMatchConstraintsWidgets;
            SolverVariable var25;
            SolverVariable var26;
            int var39;
            int var49;
            SolverVariable var54;
            if (var28 != null) {
               var49 = var28.size();
               if (var49 > 1) {
                  float var23;
                  if (var4.mHasUndefinedWeights && !var4.mHasComplexMatchWeights) {
                     var23 = (float)var4.mWidgetsMatchCount;
                  } else {
                     var23 = var10;
                  }

                  var11 = null;
                  var42 = 0;

                  for(float var24 = 0.0F; var42 < var49; ++var42) {
                     var17 = (ConstraintWidget)var28.get(var42);
                     var10 = var17.mWeight[var2];
                     if (var10 < 0.0F) {
                        if (var4.mHasComplexMatchWeights) {
                           var1.addEquality(var17.mListAnchors[var3 + 1].mSolverVariable, var17.mListAnchors[var3].mSolverVariable, 0, 4);
                           continue;
                        }

                        var10 = 1.0F;
                     }

                     if (var10 == 0.0F) {
                        var1.addEquality(var17.mListAnchors[var3 + 1].mSolverVariable, var17.mListAnchors[var3].mSolverVariable, 0, 6);
                     } else {
                        if (var11 != null) {
                           var19 = var11.mListAnchors[var3].mSolverVariable;
                           var40 = var11.mListAnchors;
                           var39 = var3 + 1;
                           var25 = var40[var39].mSolverVariable;
                           var54 = var17.mListAnchors[var3].mSolverVariable;
                           var26 = var17.mListAnchors[var39].mSolverVariable;
                           ArrayRow var41 = var1.createRow();
                           var41.createRowEqualMatchDimensions(var24, var23, var10, var19, var25, var54, var26);
                           var1.addConstraint(var41);
                        }

                        var11 = var17;
                        var24 = var10;
                     }
                  }
               }
            }

            SolverVariable var29;
            ConstraintAnchor var31;
            SolverVariable var32;
            ConstraintAnchor var33;
            ConstraintAnchor var46;
            if (var7 == null || var7 != var8 && !var18) {
               ConstraintWidget var30;
               int var48;
               byte var51;
               if (var16 && var7 != null) {
                  if (var4.mWidgetsMatchCount > 0 && var4.mWidgetsCount == var4.mWidgetsMatchCount) {
                     var12 = true;
                  } else {
                     var12 = false;
                  }

                  ConstraintWidget var37 = var7;

                  for(var9 = var7; var37 != null; var37 = var30) {
                     var17 = var37.mListNextVisibleWidget[var2];
                     if (var17 == null && var37 != var8) {
                        var30 = var17;
                     } else {
                        ConstraintAnchor var52 = var37.mListAnchors[var3];
                        var26 = var52.mSolverVariable;
                        SolverVariable var45;
                        if (var52.mTarget != null) {
                           var45 = var52.mTarget.mSolverVariable;
                        } else {
                           var45 = null;
                        }

                        if (var9 != var37) {
                           var29 = var9.mListAnchors[var3 + 1].mSolverVariable;
                        } else {
                           var29 = var45;
                           if (var37 == var7) {
                              var29 = var45;
                              if (var9 == var37) {
                                 if (var5.mListAnchors[var3].mTarget != null) {
                                    var29 = var5.mListAnchors[var3].mTarget.mSolverVariable;
                                 } else {
                                    var29 = null;
                                 }
                              }
                           }
                        }

                        var48 = var52.getMargin();
                        var40 = var37.mListAnchors;
                        var21 = var3 + 1;
                        var49 = var40[var21].getMargin();
                        ConstraintAnchor var55;
                        if (var17 != null) {
                           var55 = var17.mListAnchors[var3];
                           var54 = var55.mSolverVariable;
                           var19 = var37.mListAnchors[var21].mSolverVariable;
                        } else {
                           var55 = var6.mListAnchors[var21].mTarget;
                           if (var55 != null) {
                              var45 = var55.mSolverVariable;
                           } else {
                              var45 = null;
                           }

                           var19 = var37.mListAnchors[var21].mSolverVariable;
                           var54 = var45;
                        }

                        var42 = var49;
                        if (var55 != null) {
                           var42 = var49 + var55.getMargin();
                        }

                        var49 = var48;
                        if (var9 != null) {
                           var49 = var48 + var9.mListAnchors[var21].getMargin();
                        }

                        if (var26 != null && var29 != null && var54 != null && var19 != null) {
                           if (var37 == var7) {
                              var49 = var7.mListAnchors[var3].getMargin();
                           }

                           if (var37 == var8) {
                              var42 = var8.mListAnchors[var21].getMargin();
                           }

                           if (var12) {
                              var51 = 6;
                           } else {
                              var51 = 4;
                           }

                           var1.addCentering(var26, var29, var49, 0.5F, var54, var19, var42, var51);
                           var30 = var17;
                        } else {
                           var30 = var17;
                        }
                     }

                     var9 = var37;
                  }
               } else if (var15 && var7 != null) {
                  if (var4.mWidgetsMatchCount > 0 && var4.mWidgetsCount == var4.mWidgetsMatchCount) {
                     var13 = true;
                  } else {
                     var13 = false;
                  }

                  Object var34 = var7;

                  ConstraintAnchor var35;
                  for(Object var36 = var7; var34 != null; var34 = var30) {
                     var30 = ((ConstraintWidget)var34).mListNextVisibleWidget[var2];
                     if (var34 != var7 && var34 != var8 && var30 != null) {
                        if (var30 == var8) {
                           var30 = null;
                        }

                        var35 = ((ConstraintWidget)var34).mListAnchors[var3];
                        var25 = var35.mSolverVariable;
                        if (var35.mTarget != null) {
                           SolverVariable var47 = var35.mTarget.mSolverVariable;
                        }

                        ConstraintAnchor[] var50 = ((ConstraintWidget)var36).mListAnchors;
                        var21 = var3 + 1;
                        var26 = var50[var21].mSolverVariable;
                        var48 = var35.getMargin();
                        var39 = ((ConstraintWidget)var34).mListAnchors[var21].getMargin();
                        if (var30 != null) {
                           var46 = var30.mListAnchors[var3];
                           var54 = var46.mSolverVariable;
                           if (var46.mTarget != null) {
                              var32 = var46.mTarget.mSolverVariable;
                           } else {
                              var32 = null;
                           }

                           var19 = var32;
                           var34 = var46;
                        } else {
                           var46 = ((ConstraintWidget)var34).mListAnchors[var21].mTarget;
                           if (var46 != null) {
                              var32 = var46.mSolverVariable;
                           } else {
                              var32 = null;
                           }

                           var19 = ((ConstraintWidget)var34).mListAnchors[var21].mSolverVariable;
                           var54 = var32;
                           var34 = var46;
                        }

                        var49 = var39;
                        if (var34 != null) {
                           var49 = var39 + ((ConstraintAnchor)var34).getMargin();
                        }

                        var39 = var48;
                        if (var36 != null) {
                           var39 = var48 + ((ConstraintWidget)var36).mListAnchors[var21].getMargin();
                        }

                        if (var13) {
                           var51 = 6;
                        } else {
                           var51 = 4;
                        }

                        if (var25 != null && var26 != null && var54 != null && var19 != null) {
                           var1.addCentering(var25, var26, var39, 0.5F, var54, var19, var49, var51);
                        }
                     }

                     var36 = var34;
                  }

                  var35 = var7.mListAnchors[var3];
                  var33 = var5.mListAnchors[var3].mTarget;
                  var27 = var8.mListAnchors;
                  var2 = var3 + 1;
                  var38 = var27[var2];
                  var31 = var6.mListAnchors[var2].mTarget;
                  if (var33 != null) {
                     if (var7 != var8) {
                        var1.addEquality(var35.mSolverVariable, var33.mSolverVariable, var35.getMargin(), 5);
                     } else if (var31 != null) {
                        var1.addCentering(var35.mSolverVariable, var33.mSolverVariable, var35.getMargin(), 0.5F, var38.mSolverVariable, var31.mSolverVariable, var38.getMargin(), 5);
                     }
                  }

                  if (var31 != null && var7 != var8) {
                     var1.addEquality(var38.mSolverVariable, var31.mSolverVariable, -var38.getMargin(), 5);
                  }
               }
            } else {
               var46 = var5.mListAnchors[var3];
               var27 = var6.mListAnchors;
               var42 = var3 + 1;
               var38 = var27[var42];
               if (var5.mListAnchors[var3].mTarget != null) {
                  var29 = var5.mListAnchors[var3].mTarget.mSolverVariable;
               } else {
                  var29 = null;
               }

               if (var6.mListAnchors[var42].mTarget != null) {
                  var32 = var6.mListAnchors[var42].mTarget.mSolverVariable;
               } else {
                  var32 = null;
               }

               var33 = var46;
               if (var7 == var8) {
                  var33 = var7.mListAnchors[var3];
                  var38 = var7.mListAnchors[var42];
               }

               if (var29 != null && var32 != null) {
                  if (var2 == 0) {
                     var10 = var9.mHorizontalBiasPercent;
                  } else {
                     var10 = var9.mVerticalBiasPercent;
                  }

                  var42 = var33.getMargin();
                  var2 = var38.getMargin();
                  var1.addCentering(var33.mSolverVariable, var29, var42, var10, var32, var38.mSolverVariable, var2, 5);
               }
            }

            if ((var16 || var15) && var7 != null) {
               var38 = var7.mListAnchors[var3];
               var27 = var8.mListAnchors;
               var2 = var3 + 1;
               var33 = var27[var2];
               if (var38.mTarget != null) {
                  var32 = var38.mTarget.mSolverVariable;
               } else {
                  var32 = null;
               }

               if (var33.mTarget != null) {
                  var29 = var33.mTarget.mSolverVariable;
               } else {
                  var29 = null;
               }

               if (var6 != var8) {
                  var31 = var6.mListAnchors[var2];
                  if (var31.mTarget != null) {
                     var29 = var31.mTarget.mSolverVariable;
                  } else {
                     var29 = null;
                  }
               }

               if (var7 == var8) {
                  var38 = var7.mListAnchors[var3];
                  var33 = var7.mListAnchors[var2];
               }

               if (var32 != null && var29 != null) {
                  var3 = var38.getMargin();
                  if (var8 == null) {
                     var7 = var6;
                  } else {
                     var7 = var8;
                  }

                  var2 = var7.mListAnchors[var2].getMargin();
                  var1.addCentering(var38.mSolverVariable, var32, var3, 0.5F, var29, var33.mSolverVariable, var2, 5);
               }
            }

            return;
         }

         var38 = var17.mListAnchors[var3];
         byte var44;
         if (!var12 && !var18) {
            var44 = 4;
         } else {
            var44 = 1;
         }

         int var20 = var38.getMargin();
         var21 = var20;
         if (var38.mTarget != null) {
            var21 = var20;
            if (var17 != var5) {
               var21 = var20 + var38.mTarget.getMargin();
            }
         }

         if (var18 && var17 != var5 && var17 != var7) {
            var44 = 6;
         } else if (var16 && var12) {
            var44 = 4;
         }

         if (var38.mTarget != null) {
            if (var17 == var7) {
               var1.addGreaterThan(var38.mSolverVariable, var38.mTarget.mSolverVariable, var21, 5);
            } else {
               var1.addGreaterThan(var38.mSolverVariable, var38.mTarget.mSolverVariable, var21, 6);
            }

            var1.addEquality(var38.mSolverVariable, var38.mTarget.mSolverVariable, var21, var44);
         }

         if (var12) {
            if (var17.getVisibility() != 8 && var17.mListDimensionBehaviors[var2] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               var1.addGreaterThan(var17.mListAnchors[var3 + 1].mSolverVariable, var17.mListAnchors[var3].mSolverVariable, 0, 5);
            }

            var1.addGreaterThan(var17.mListAnchors[var3].mSolverVariable, var0.mListAnchors[var3].mSolverVariable, 0, 6);
         }

         ConstraintAnchor var22 = var17.mListAnchors[var3 + 1].mTarget;
         var11 = var19;
         if (var22 != null) {
            ConstraintWidget var53 = var22.mOwner;
            var11 = var19;
            if (var53.mListAnchors[var3].mTarget != null) {
               if (var53.mListAnchors[var3].mTarget.mOwner != var17) {
                  var11 = var19;
               } else {
                  var11 = var53;
               }
            }
         }

         if (var11 != null) {
            var17 = var11;
         } else {
            var13 = true;
         }
      }
   }
}
