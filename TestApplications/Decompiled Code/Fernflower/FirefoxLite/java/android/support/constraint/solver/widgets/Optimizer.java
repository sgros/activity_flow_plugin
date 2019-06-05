package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;

public class Optimizer {
   static boolean[] flags = new boolean[3];

   static void analyze(int var0, ConstraintWidget var1) {
      var1.updateResolutionNodes();
      ResolutionAnchor var2 = var1.mLeft.getResolutionNode();
      ResolutionAnchor var3 = var1.mTop.getResolutionNode();
      ResolutionAnchor var4 = var1.mRight.getResolutionNode();
      ResolutionAnchor var5 = var1.mBottom.getResolutionNode();
      boolean var7;
      if ((var0 & 8) == 8) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var6;
      if (var1.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(var1, 0)) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var8;
      if (var2.type != 4 && var4.type != 4) {
         if (var1.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED && (!var6 || var1.getVisibility() != 8)) {
            if (var6) {
               var8 = var1.getWidth();
               var2.setType(1);
               var4.setType(1);
               if (var1.mLeft.mTarget == null && var1.mRight.mTarget == null) {
                  if (var7) {
                     var4.dependsOn(var2, 1, var1.getResolutionWidth());
                  } else {
                     var4.dependsOn(var2, var8);
                  }
               } else if (var1.mLeft.mTarget != null && var1.mRight.mTarget == null) {
                  if (var7) {
                     var4.dependsOn(var2, 1, var1.getResolutionWidth());
                  } else {
                     var4.dependsOn(var2, var8);
                  }
               } else if (var1.mLeft.mTarget == null && var1.mRight.mTarget != null) {
                  if (var7) {
                     var2.dependsOn(var4, -1, var1.getResolutionWidth());
                  } else {
                     var2.dependsOn(var4, -var8);
                  }
               } else if (var1.mLeft.mTarget != null && var1.mRight.mTarget != null) {
                  if (var7) {
                     var1.getResolutionWidth().addDependent(var2);
                     var1.getResolutionWidth().addDependent(var4);
                  }

                  if (var1.mDimensionRatio == 0.0F) {
                     var2.setType(3);
                     var4.setType(3);
                     var2.setOpposite(var4, 0.0F);
                     var4.setOpposite(var2, 0.0F);
                  } else {
                     var2.setType(2);
                     var4.setType(2);
                     var2.setOpposite(var4, (float)(-var8));
                     var4.setOpposite(var2, (float)var8);
                     var1.setWidth(var8);
                  }
               }
            }
         } else if (var1.mLeft.mTarget == null && var1.mRight.mTarget == null) {
            var2.setType(1);
            var4.setType(1);
            if (var7) {
               var4.dependsOn(var2, 1, var1.getResolutionWidth());
            } else {
               var4.dependsOn(var2, var1.getWidth());
            }
         } else if (var1.mLeft.mTarget != null && var1.mRight.mTarget == null) {
            var2.setType(1);
            var4.setType(1);
            if (var7) {
               var4.dependsOn(var2, 1, var1.getResolutionWidth());
            } else {
               var4.dependsOn(var2, var1.getWidth());
            }
         } else if (var1.mLeft.mTarget == null && var1.mRight.mTarget != null) {
            var2.setType(1);
            var4.setType(1);
            var2.dependsOn(var4, -var1.getWidth());
            if (var7) {
               var2.dependsOn(var4, -1, var1.getResolutionWidth());
            } else {
               var2.dependsOn(var4, -var1.getWidth());
            }
         } else if (var1.mLeft.mTarget != null && var1.mRight.mTarget != null) {
            var2.setType(2);
            var4.setType(2);
            if (var7) {
               var1.getResolutionWidth().addDependent(var2);
               var1.getResolutionWidth().addDependent(var4);
               var2.setOpposite(var4, -1, var1.getResolutionWidth());
               var4.setOpposite(var2, 1, var1.getResolutionWidth());
            } else {
               var2.setOpposite(var4, (float)(-var1.getWidth()));
               var4.setOpposite(var2, (float)var1.getWidth());
            }
         }
      }

      if (var1.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(var1, 1)) {
         var6 = true;
      } else {
         var6 = false;
      }

      if (var3.type != 4 && var5.type != 4) {
         if (var1.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED && (!var6 || var1.getVisibility() != 8)) {
            if (var6) {
               var8 = var1.getHeight();
               var3.setType(1);
               var5.setType(1);
               if (var1.mTop.mTarget == null && var1.mBottom.mTarget == null) {
                  if (var7) {
                     var5.dependsOn(var3, 1, var1.getResolutionHeight());
                  } else {
                     var5.dependsOn(var3, var8);
                  }
               } else if (var1.mTop.mTarget != null && var1.mBottom.mTarget == null) {
                  if (var7) {
                     var5.dependsOn(var3, 1, var1.getResolutionHeight());
                  } else {
                     var5.dependsOn(var3, var8);
                  }
               } else if (var1.mTop.mTarget == null && var1.mBottom.mTarget != null) {
                  if (var7) {
                     var3.dependsOn(var5, -1, var1.getResolutionHeight());
                  } else {
                     var3.dependsOn(var5, -var8);
                  }
               } else if (var1.mTop.mTarget != null && var1.mBottom.mTarget != null) {
                  if (var7) {
                     var1.getResolutionHeight().addDependent(var3);
                     var1.getResolutionWidth().addDependent(var5);
                  }

                  if (var1.mDimensionRatio == 0.0F) {
                     var3.setType(3);
                     var5.setType(3);
                     var3.setOpposite(var5, 0.0F);
                     var5.setOpposite(var3, 0.0F);
                  } else {
                     var3.setType(2);
                     var5.setType(2);
                     var3.setOpposite(var5, (float)(-var8));
                     var5.setOpposite(var3, (float)var8);
                     var1.setHeight(var8);
                     if (var1.mBaselineDistance > 0) {
                        var1.mBaseline.getResolutionNode().dependsOn(1, var3, var1.mBaselineDistance);
                     }
                  }
               }
            }
         } else if (var1.mTop.mTarget == null && var1.mBottom.mTarget == null) {
            var3.setType(1);
            var5.setType(1);
            if (var7) {
               var5.dependsOn(var3, 1, var1.getResolutionHeight());
            } else {
               var5.dependsOn(var3, var1.getHeight());
            }

            if (var1.mBaseline.mTarget != null) {
               var1.mBaseline.getResolutionNode().setType(1);
               var3.dependsOn(1, var1.mBaseline.getResolutionNode(), -var1.mBaselineDistance);
            }
         } else if (var1.mTop.mTarget != null && var1.mBottom.mTarget == null) {
            var3.setType(1);
            var5.setType(1);
            if (var7) {
               var5.dependsOn(var3, 1, var1.getResolutionHeight());
            } else {
               var5.dependsOn(var3, var1.getHeight());
            }

            if (var1.mBaselineDistance > 0) {
               var1.mBaseline.getResolutionNode().dependsOn(1, var3, var1.mBaselineDistance);
            }
         } else if (var1.mTop.mTarget == null && var1.mBottom.mTarget != null) {
            var3.setType(1);
            var5.setType(1);
            if (var7) {
               var3.dependsOn(var5, -1, var1.getResolutionHeight());
            } else {
               var3.dependsOn(var5, -var1.getHeight());
            }

            if (var1.mBaselineDistance > 0) {
               var1.mBaseline.getResolutionNode().dependsOn(1, var3, var1.mBaselineDistance);
            }
         } else if (var1.mTop.mTarget != null && var1.mBottom.mTarget != null) {
            var3.setType(2);
            var5.setType(2);
            if (var7) {
               var3.setOpposite(var5, -1, var1.getResolutionHeight());
               var5.setOpposite(var3, 1, var1.getResolutionHeight());
               var1.getResolutionHeight().addDependent(var3);
               var1.getResolutionWidth().addDependent(var5);
            } else {
               var3.setOpposite(var5, (float)(-var1.getHeight()));
               var5.setOpposite(var3, (float)var1.getHeight());
            }

            if (var1.mBaselineDistance > 0) {
               var1.mBaseline.getResolutionNode().dependsOn(1, var3, var1.mBaselineDistance);
            }
         }
      }

   }

   static boolean applyChainOptimized(ConstraintWidgetContainer var0, LinearSystem var1, int var2, int var3, ChainHead var4) {
      ConstraintWidget var5;
      ConstraintWidget var6;
      ConstraintWidget var7;
      ConstraintWidget var8;
      float var10;
      boolean var12;
      boolean var13;
      boolean var14;
      boolean var16;
      ConstraintWidget var30;
      label281: {
         label280: {
            var5 = var4.mFirst;
            var6 = var4.mLast;
            var7 = var4.mFirstVisibleWidget;
            var8 = var4.mLastVisibleWidget;
            ConstraintWidget var9 = var4.mHead;
            var10 = var4.mTotalWeight;
            ConstraintWidget var11 = var4.mFirstMatchConstraintWidget;
            var30 = var4.mLastMatchConstraintWidget;
            ConstraintWidget.DimensionBehaviour var10000 = var0.mListDimensionBehaviors[var2];
            ConstraintWidget.DimensionBehaviour var25 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            boolean var15;
            if (var2 == 0) {
               if (var9.mHorizontalChainStyle == 0) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               if (var9.mHorizontalChainStyle == 1) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               var14 = var12;
               var15 = var13;
               if (var9.mHorizontalChainStyle == 2) {
                  break label280;
               }
            } else {
               if (var9.mVerticalChainStyle == 0) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               if (var9.mVerticalChainStyle == 1) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               var14 = var12;
               var15 = var13;
               if (var9.mVerticalChainStyle == 2) {
                  break label280;
               }
            }

            var16 = false;
            var13 = var15;
            break label281;
         }

         var16 = true;
         var14 = var12;
      }

      var30 = var5;
      int var17 = 0;
      int var38 = 0;
      var12 = false;
      float var18 = 0.0F;
      float var19 = 0.0F;

      float var21;
      float var22;
      ConstraintWidget var29;
      while(!var12) {
         int var20 = var17;
         var21 = var18;
         var22 = var19;
         if (var30.getVisibility() != 8) {
            var20 = var17 + 1;
            if (var2 == 0) {
               var22 = var18 + (float)var30.getWidth();
            } else {
               var22 = var18 + (float)var30.getHeight();
            }

            var21 = var22;
            if (var30 != var7) {
               var21 = var22 + (float)var30.mListAnchors[var3].getMargin();
            }

            var22 = var19 + (float)var30.mListAnchors[var3].getMargin() + (float)var30.mListAnchors[var3 + 1].getMargin();
         }

         ConstraintAnchor var39 = var30.mListAnchors[var3];
         int var23 = var38;
         if (var30.getVisibility() != 8) {
            var23 = var38;
            if (var30.mListDimensionBehaviors[var2] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               var23 = var38 + 1;
               if (var2 == 0) {
                  if (var30.mMatchConstraintDefaultWidth != 0) {
                     return false;
                  }

                  if (var30.mMatchConstraintMinWidth != 0 || var30.mMatchConstraintMaxWidth != 0) {
                     return false;
                  }
               } else {
                  if (var30.mMatchConstraintDefaultHeight != 0) {
                     return false;
                  }

                  if (var30.mMatchConstraintMinHeight != 0 || var30.mMatchConstraintMaxHeight != 0) {
                     return false;
                  }
               }
            }
         }

         label249: {
            ConstraintAnchor var34 = var30.mListAnchors[var3 + 1].mTarget;
            if (var34 != null) {
               var29 = var34.mOwner;
               if (var29.mListAnchors[var3].mTarget != null && var29.mListAnchors[var3].mTarget.mOwner == var30) {
                  break label249;
               }
            }

            var29 = null;
         }

         if (var29 != null) {
            var17 = var20;
            var38 = var23;
            var30 = var29;
            var18 = var21;
            var19 = var22;
         } else {
            var12 = true;
            var17 = var20;
            var38 = var23;
            var18 = var21;
            var19 = var22;
         }
      }

      ResolutionAnchor var36 = var5.mListAnchors[var3].getResolutionNode();
      ConstraintAnchor[] var26 = var6.mListAnchors;
      int var37 = var3 + 1;
      ResolutionAnchor var27 = var26[var37].getResolutionNode();
      if (var36.target != null && var27.target != null) {
         if (var36.target.state != 1 && var27.target.state != 1) {
            return false;
         } else if (var38 > 0 && var38 != var17) {
            return false;
         } else {
            if (!var16 && !var14 && !var13) {
               var21 = 0.0F;
            } else {
               if (var7 != null) {
                  var22 = (float)var7.mListAnchors[var3].getMargin();
               } else {
                  var22 = 0.0F;
               }

               var21 = var22;
               if (var8 != null) {
                  var21 = var22 + (float)var8.mListAnchors[var37].getMargin();
               }
            }

            float var24 = var36.target.resolvedOffset;
            var22 = var27.target.resolvedOffset;
            if (var24 < var22) {
               var22 = var22 - var24 - var18;
            } else {
               var22 = var24 - var22 - var18;
            }

            ResolutionAnchor var32;
            Metrics var33;
            if (var38 > 0 && var38 == var17) {
               if (var30.getParent() != null && var30.getParent().mListDimensionBehaviors[var2] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                  return false;
               } else {
                  var18 = var22 + var18 - var19;
                  var22 = var18;
                  if (var14) {
                     var22 = var18 - (var19 - var21);
                  }

                  var21 = var24;
                  var29 = var7;
                  if (var14) {
                     var19 = var24 + (float)var7.mListAnchors[var37].getMargin();
                     var30 = var7.mListNextVisibleWidget[var2];
                     var21 = var19;
                     var29 = var7;
                     if (var30 != null) {
                        var21 = var19 + (float)var30.mListAnchors[var3].getMargin();
                        var29 = var7;
                     }
                  }

                  for(; var29 != null; var29 = var30) {
                     if (LinearSystem.sMetrics != null) {
                        var33 = LinearSystem.sMetrics;
                        --var33.nonresolvedWidgets;
                        var33 = LinearSystem.sMetrics;
                        ++var33.resolvedWidgets;
                        var33 = LinearSystem.sMetrics;
                        ++var33.chainConnectionResolved;
                     }

                     var30 = var29.mListNextVisibleWidget[var2];
                     if (var30 != null || var29 == var8) {
                        var19 = var22 / (float)var38;
                        if (var10 > 0.0F) {
                           var19 = var29.mWeight[var2] * var22 / var10;
                        }

                        var21 += (float)var29.mListAnchors[var3].getMargin();
                        var29.mListAnchors[var3].getResolutionNode().resolve(var36.resolvedTarget, var21);
                        ResolutionAnchor var35 = var29.mListAnchors[var37].getResolutionNode();
                        var32 = var36.resolvedTarget;
                        var21 += var19;
                        var35.resolve(var32, var21);
                        var29.mListAnchors[var3].getResolutionNode().addResolvedValue(var1);
                        var29.mListAnchors[var37].getResolutionNode().addResolvedValue(var1);
                        var21 += (float)var29.mListAnchors[var37].getMargin();
                     }
                  }

                  return true;
               }
            } else if (var22 < var18) {
               return false;
            } else {
               if (var16) {
                  for(var21 = var24 + (var22 - var21) * var5.getHorizontalBiasPercent(); var7 != null; var21 = var22) {
                     if (LinearSystem.sMetrics != null) {
                        Metrics var28 = LinearSystem.sMetrics;
                        --var28.nonresolvedWidgets;
                        var28 = LinearSystem.sMetrics;
                        ++var28.resolvedWidgets;
                        var28 = LinearSystem.sMetrics;
                        ++var28.chainConnectionResolved;
                     }

                     label298: {
                        var29 = var7.mListNextVisibleWidget[var2];
                        if (var29 == null) {
                           var22 = var21;
                           if (var7 != var8) {
                              break label298;
                           }
                        }

                        if (var2 == 0) {
                           var22 = (float)var7.getWidth();
                        } else {
                           var22 = (float)var7.getHeight();
                        }

                        var21 += (float)var7.mListAnchors[var3].getMargin();
                        var7.mListAnchors[var3].getResolutionNode().resolve(var36.resolvedTarget, var21);
                        ResolutionAnchor var31 = var7.mListAnchors[var37].getResolutionNode();
                        var32 = var36.resolvedTarget;
                        var21 += var22;
                        var31.resolve(var32, var21);
                        var7.mListAnchors[var3].getResolutionNode().addResolvedValue(var1);
                        var7.mListAnchors[var37].getResolutionNode().addResolvedValue(var1);
                        var22 = var21 + (float)var7.mListAnchors[var37].getMargin();
                     }

                     var7 = var29;
                  }
               } else if (var14 || var13) {
                  if (var14) {
                     var19 = var22 - var21;
                  } else {
                     var19 = var22;
                     if (var13) {
                        var19 = var22 - var21;
                     }
                  }

                  var21 = var19 / (float)(var17 + 1);
                  if (var13) {
                     if (var17 > 1) {
                        var21 = var19 / (float)(var17 - 1);
                     } else {
                        var21 = var19 / 2.0F;
                     }
                  }

                  var22 = var24 + var21;
                  var19 = var22;
                  if (var13) {
                     var19 = var22;
                     if (var17 > 1) {
                        var19 = (float)var7.mListAnchors[var3].getMargin() + var24;
                     }
                  }

                  var22 = var19;
                  var29 = var7;
                  if (var14) {
                     var22 = var19;
                     var29 = var7;
                     if (var7 != null) {
                        var22 = var19 + (float)var7.mListAnchors[var3].getMargin();
                        var29 = var7;
                     }
                  }

                  while(var29 != null) {
                     if (LinearSystem.sMetrics != null) {
                        var33 = LinearSystem.sMetrics;
                        --var33.nonresolvedWidgets;
                        var33 = LinearSystem.sMetrics;
                        ++var33.resolvedWidgets;
                        var33 = LinearSystem.sMetrics;
                        ++var33.chainConnectionResolved;
                     }

                     label301: {
                        var30 = var29.mListNextVisibleWidget[var2];
                        if (var30 == null) {
                           var19 = var22;
                           if (var29 != var8) {
                              break label301;
                           }
                        }

                        if (var2 == 0) {
                           var19 = (float)var29.getWidth();
                        } else {
                           var19 = (float)var29.getHeight();
                        }

                        var29.mListAnchors[var3].getResolutionNode().resolve(var36.resolvedTarget, var22);
                        var29.mListAnchors[var37].getResolutionNode().resolve(var36.resolvedTarget, var22 + var19);
                        var29.mListAnchors[var3].getResolutionNode().addResolvedValue(var1);
                        var29.mListAnchors[var37].getResolutionNode().addResolvedValue(var1);
                        var19 = var22 + var19 + var21;
                     }

                     var29 = var30;
                     var22 = var19;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   static void checkMatchParent(ConstraintWidgetContainer var0, LinearSystem var1, ConstraintWidget var2) {
      int var3;
      int var4;
      if (var0.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var2.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
         var3 = var2.mLeft.mMargin;
         var4 = var0.getWidth() - var2.mRight.mMargin;
         var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
         var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
         var1.addEquality(var2.mLeft.mSolverVariable, var3);
         var1.addEquality(var2.mRight.mSolverVariable, var4);
         var2.mHorizontalResolution = 2;
         var2.setHorizontalDimension(var3, var4);
      }

      if (var0.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var2.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
         var4 = var2.mTop.mMargin;
         var3 = var0.getHeight() - var2.mBottom.mMargin;
         var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
         var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
         var1.addEquality(var2.mTop.mSolverVariable, var4);
         var1.addEquality(var2.mBottom.mSolverVariable, var3);
         if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
            var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
            var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var4);
         }

         var2.mVerticalResolution = 2;
         var2.setVerticalDimension(var4, var3);
      }

   }

   private static boolean optimizableMatchConstraint(ConstraintWidget var0, int var1) {
      if (var0.mListDimensionBehaviors[var1] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         return false;
      } else {
         float var2 = var0.mDimensionRatio;
         byte var3 = 1;
         if (var2 != 0.0F) {
            ConstraintWidget.DimensionBehaviour[] var4 = var0.mListDimensionBehaviors;
            byte var5;
            if (var1 == 0) {
               var5 = var3;
            } else {
               var5 = 0;
            }

            return var4[var5] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT ? false : false;
         } else {
            if (var1 == 0) {
               if (var0.mMatchConstraintDefaultWidth != 0) {
                  return false;
               }

               if (var0.mMatchConstraintMinWidth != 0 || var0.mMatchConstraintMaxWidth != 0) {
                  return false;
               }
            } else {
               if (var0.mMatchConstraintDefaultHeight != 0) {
                  return false;
               }

               if (var0.mMatchConstraintMinHeight != 0 || var0.mMatchConstraintMaxHeight != 0) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
