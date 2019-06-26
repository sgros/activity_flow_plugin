package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;

public class Optimizer {
   static void applyDirectResolutionHorizontalChain(ConstraintWidgetContainer var0, LinearSystem var1, int var2, ConstraintWidget var3) {
      ConstraintWidget var4 = var3;
      float var5 = 0.0F;
      int var6 = 0;
      int var7 = var6;
      ConstraintWidget var8 = null;

      while(true) {
         boolean var9 = true;
         int var10;
         int var11;
         float var12;
         ConstraintWidget var13;
         if (var4 == null) {
            if (var8 != null) {
               if (var8.mRight.mTarget != null) {
                  var11 = var8.mRight.mTarget.mOwner.getX();
               } else {
                  var11 = 0;
               }

               var10 = var11;
               if (var8.mRight.mTarget != null) {
                  var10 = var11;
                  if (var8.mRight.mTarget.mOwner == var0) {
                     var10 = var0.getRight();
                  }
               }
            } else {
               var10 = 0;
            }

            float var14 = (float)(var10 - 0) - (float)var7;
            var12 = var14 / (float)(var6 + 1);
            float var15;
            if (var2 == 0) {
               var15 = var12;
            } else {
               var15 = var14 / (float)var2;
               var12 = 0.0F;
            }

            while(var3 != null) {
               if (var3.mLeft.mTarget != null) {
                  var10 = var3.mLeft.getMargin();
               } else {
                  var10 = 0;
               }

               if (var3.mRight.mTarget != null) {
                  var11 = var3.mRight.getMargin();
               } else {
                  var11 = 0;
               }

               float var16;
               if (var3.getVisibility() != 8) {
                  var16 = (float)var10;
                  var12 += var16;
                  var1.addEquality(var3.mLeft.mSolverVariable, (int)(var12 + 0.5F));
                  if (var3.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     if (var5 == 0.0F) {
                        var12 += var15 - var16 - (float)var11;
                     } else {
                        var12 += var3.mHorizontalWeight * var14 / var5 - var16 - (float)var11;
                     }
                  } else {
                     var12 += (float)var3.getWidth();
                  }

                  var1.addEquality(var3.mRight.mSolverVariable, (int)(0.5F + var12));
                  var16 = var12;
                  if (var2 == 0) {
                     var16 = var12 + var15;
                  }

                  var12 = var16 + (float)var11;
               } else {
                  var16 = var15 / 2.0F;
                  SolverVariable var17 = var3.mLeft.mSolverVariable;
                  var10 = (int)(var12 - var16 + 0.5F);
                  var1.addEquality(var17, var10);
                  var1.addEquality(var3.mRight.mSolverVariable, var10);
               }

               if (var3.mRight.mTarget != null) {
                  var13 = var3.mRight.mTarget.mOwner;
               } else {
                  var13 = null;
               }

               var4 = var13;
               if (var13 != null) {
                  var4 = var13;
                  if (var13.mLeft.mTarget != null) {
                     var4 = var13;
                     if (var13.mLeft.mTarget.mOwner != var3) {
                        var4 = null;
                     }
                  }
               }

               if (var4 == var0) {
                  var3 = null;
               } else {
                  var3 = var4;
               }
            }

            return;
         }

         if (var4.getVisibility() != 8) {
            var9 = false;
         }

         var10 = var6;
         var11 = var7;
         var12 = var5;
         if (!var9) {
            ++var6;
            if (var4.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               int var18 = var4.getWidth();
               if (var4.mLeft.mTarget != null) {
                  var10 = var4.mLeft.getMargin();
               } else {
                  var10 = 0;
               }

               if (var4.mRight.mTarget != null) {
                  var11 = var4.mRight.getMargin();
               } else {
                  var11 = 0;
               }

               var11 += var7 + var18 + var10;
               var10 = var6;
               var12 = var5;
            } else {
               var12 = var5 + var4.mHorizontalWeight;
               var11 = var7;
               var10 = var6;
            }
         }

         if (var4.mRight.mTarget != null) {
            var8 = var4.mRight.mTarget.mOwner;
         } else {
            var8 = null;
         }

         var13 = var8;
         if (var8 != null) {
            label129: {
               if (var8.mLeft.mTarget != null) {
                  var13 = var8;
                  if (var8.mLeft.mTarget == null) {
                     break label129;
                  }

                  var13 = var8;
                  if (var8.mLeft.mTarget.mOwner == var4) {
                     break label129;
                  }
               }

               var13 = null;
            }
         }

         var8 = var4;
         var4 = var13;
         var6 = var10;
         var7 = var11;
         var5 = var12;
      }
   }

   static void applyDirectResolutionVerticalChain(ConstraintWidgetContainer var0, LinearSystem var1, int var2, ConstraintWidget var3) {
      ConstraintWidget var4 = var3;
      float var5 = 0.0F;
      int var6 = 0;
      int var7 = var6;
      ConstraintWidget var8 = null;

      while(true) {
         boolean var9 = true;
         int var10;
         int var11;
         float var12;
         ConstraintWidget var13;
         if (var4 == null) {
            if (var8 != null) {
               if (var8.mBottom.mTarget != null) {
                  var11 = var8.mBottom.mTarget.mOwner.getX();
               } else {
                  var11 = 0;
               }

               var10 = var11;
               if (var8.mBottom.mTarget != null) {
                  var10 = var11;
                  if (var8.mBottom.mTarget.mOwner == var0) {
                     var10 = var0.getBottom();
                  }
               }
            } else {
               var10 = 0;
            }

            float var14 = (float)(var10 - 0) - (float)var7;
            var12 = var14 / (float)(var6 + 1);
            float var15;
            if (var2 == 0) {
               var15 = var12;
            } else {
               var15 = var14 / (float)var2;
               var12 = 0.0F;
            }

            while(var3 != null) {
               if (var3.mTop.mTarget != null) {
                  var11 = var3.mTop.getMargin();
               } else {
                  var11 = 0;
               }

               if (var3.mBottom.mTarget != null) {
                  var10 = var3.mBottom.getMargin();
               } else {
                  var10 = 0;
               }

               float var16;
               if (var3.getVisibility() != 8) {
                  var16 = (float)var11;
                  var12 += var16;
                  var1.addEquality(var3.mTop.mSolverVariable, (int)(var12 + 0.5F));
                  if (var3.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                     if (var5 == 0.0F) {
                        var12 += var15 - var16 - (float)var10;
                     } else {
                        var12 += var3.mVerticalWeight * var14 / var5 - var16 - (float)var10;
                     }
                  } else {
                     var12 += (float)var3.getHeight();
                  }

                  var1.addEquality(var3.mBottom.mSolverVariable, (int)(0.5F + var12));
                  var16 = var12;
                  if (var2 == 0) {
                     var16 = var12 + var15;
                  }

                  var12 = var16 + (float)var10;
               } else {
                  var16 = var15 / 2.0F;
                  SolverVariable var17 = var3.mTop.mSolverVariable;
                  var11 = (int)(var12 - var16 + 0.5F);
                  var1.addEquality(var17, var11);
                  var1.addEquality(var3.mBottom.mSolverVariable, var11);
               }

               if (var3.mBottom.mTarget != null) {
                  var4 = var3.mBottom.mTarget.mOwner;
               } else {
                  var4 = null;
               }

               var13 = var4;
               if (var4 != null) {
                  var13 = var4;
                  if (var4.mTop.mTarget != null) {
                     var13 = var4;
                     if (var4.mTop.mTarget.mOwner != var3) {
                        var13 = null;
                     }
                  }
               }

               if (var13 == var0) {
                  var3 = null;
               } else {
                  var3 = var13;
               }
            }

            return;
         }

         if (var4.getVisibility() != 8) {
            var9 = false;
         }

         var10 = var6;
         var11 = var7;
         var12 = var5;
         if (!var9) {
            ++var6;
            if (var4.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
               int var18 = var4.getHeight();
               if (var4.mTop.mTarget != null) {
                  var11 = var4.mTop.getMargin();
               } else {
                  var11 = 0;
               }

               if (var4.mBottom.mTarget != null) {
                  var10 = var4.mBottom.getMargin();
               } else {
                  var10 = 0;
               }

               var11 = var7 + var18 + var11 + var10;
               var10 = var6;
               var12 = var5;
            } else {
               var12 = var5 + var4.mVerticalWeight;
               var11 = var7;
               var10 = var6;
            }
         }

         if (var4.mBottom.mTarget != null) {
            var8 = var4.mBottom.mTarget.mOwner;
         } else {
            var8 = null;
         }

         var13 = var8;
         if (var8 != null) {
            label129: {
               if (var8.mTop.mTarget != null) {
                  var13 = var8;
                  if (var8.mTop.mTarget == null) {
                     break label129;
                  }

                  var13 = var8;
                  if (var8.mTop.mTarget.mOwner == var4) {
                     break label129;
                  }
               }

               var13 = null;
            }
         }

         var8 = var4;
         var4 = var13;
         var6 = var10;
         var7 = var11;
         var5 = var12;
      }
   }

   static void checkHorizontalSimpleDependency(ConstraintWidgetContainer var0, LinearSystem var1, ConstraintWidget var2) {
      if (var2.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
         var2.mHorizontalResolution = 1;
      } else {
         int var9;
         int var10;
         if (var0.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var2.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
            var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
            var9 = var2.mLeft.mMargin;
            var10 = var0.getWidth() - var2.mRight.mMargin;
            var1.addEquality(var2.mLeft.mSolverVariable, var9);
            var1.addEquality(var2.mRight.mSolverVariable, var10);
            var2.setHorizontalDimension(var9, var10);
            var2.mHorizontalResolution = 2;
         } else if (var2.mLeft.mTarget != null && var2.mRight.mTarget != null) {
            if (var2.mLeft.mTarget.mOwner == var0 && var2.mRight.mTarget.mOwner == var0) {
               var10 = var2.mLeft.getMargin();
               int var5 = var2.mRight.getMargin();
               if (var0.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var9 = var0.getWidth() - var5;
               } else {
                  var9 = var2.getWidth();
                  var10 += (int)((float)(var0.getWidth() - var10 - var5 - var9) * var2.mHorizontalBiasPercent + 0.5F);
                  var9 = var2.getWidth() + var10;
               }

               var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
               var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
               var1.addEquality(var2.mLeft.mSolverVariable, var10);
               var1.addEquality(var2.mRight.mSolverVariable, var9);
               var2.mHorizontalResolution = 2;
               var2.setHorizontalDimension(var10, var9);
            } else {
               var2.mHorizontalResolution = 1;
            }
         } else {
            if (var2.mLeft.mTarget != null && var2.mLeft.mTarget.mOwner == var0) {
               var10 = var2.mLeft.getMargin();
               var9 = var2.getWidth() + var10;
               var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
               var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
               var1.addEquality(var2.mLeft.mSolverVariable, var10);
               var1.addEquality(var2.mRight.mSolverVariable, var9);
               var2.mHorizontalResolution = 2;
               var2.setHorizontalDimension(var10, var9);
            } else if (var2.mRight.mTarget != null && var2.mRight.mTarget.mOwner == var0) {
               var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
               var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
               var9 = var0.getWidth() - var2.mRight.getMargin();
               var10 = var9 - var2.getWidth();
               var1.addEquality(var2.mLeft.mSolverVariable, var10);
               var1.addEquality(var2.mRight.mSolverVariable, var9);
               var2.mHorizontalResolution = 2;
               var2.setHorizontalDimension(var10, var9);
            } else {
               SolverVariable var8;
               if (var2.mLeft.mTarget != null && var2.mLeft.mTarget.mOwner.mHorizontalResolution == 2) {
                  var8 = var2.mLeft.mTarget.mSolverVariable;
                  var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
                  var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
                  var10 = (int)(var8.computedValue + (float)var2.mLeft.getMargin() + 0.5F);
                  var9 = var2.getWidth() + var10;
                  var1.addEquality(var2.mLeft.mSolverVariable, var10);
                  var1.addEquality(var2.mRight.mSolverVariable, var9);
                  var2.mHorizontalResolution = 2;
                  var2.setHorizontalDimension(var10, var9);
               } else if (var2.mRight.mTarget != null && var2.mRight.mTarget.mOwner.mHorizontalResolution == 2) {
                  var8 = var2.mRight.mTarget.mSolverVariable;
                  var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
                  var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
                  var10 = (int)(var8.computedValue - (float)var2.mRight.getMargin() + 0.5F);
                  var9 = var10 - var2.getWidth();
                  var1.addEquality(var2.mLeft.mSolverVariable, var9);
                  var1.addEquality(var2.mRight.mSolverVariable, var10);
                  var2.mHorizontalResolution = 2;
                  var2.setHorizontalDimension(var9, var10);
               } else {
                  boolean var3;
                  if (var2.mLeft.mTarget != null) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  boolean var4;
                  if (var2.mRight.mTarget != null) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  if (!var3 && !var4) {
                     if (var2 instanceof Guideline) {
                        Guideline var6 = (Guideline)var2;
                        if (var6.getOrientation() == 1) {
                           var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
                           var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
                           float var7;
                           if (var6.getRelativeBegin() != -1) {
                              var7 = (float)var6.getRelativeBegin();
                           } else if (var6.getRelativeEnd() != -1) {
                              var7 = (float)(var0.getWidth() - var6.getRelativeEnd());
                           } else {
                              var7 = (float)var0.getWidth();
                              var7 = var6.getRelativePercent() * var7;
                           }

                           var9 = (int)(var7 + 0.5F);
                           var1.addEquality(var2.mLeft.mSolverVariable, var9);
                           var1.addEquality(var2.mRight.mSolverVariable, var9);
                           var2.mHorizontalResolution = 2;
                           var2.mVerticalResolution = 2;
                           var2.setHorizontalDimension(var9, var9);
                           var2.setVerticalDimension(0, var0.getHeight());
                        }
                     } else {
                        var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
                        var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
                        var9 = var2.getX();
                        var10 = var2.getWidth();
                        var1.addEquality(var2.mLeft.mSolverVariable, var9);
                        var1.addEquality(var2.mRight.mSolverVariable, var10 + var9);
                        var2.mHorizontalResolution = 2;
                     }
                  }
               }
            }

         }
      }
   }

   static void checkMatchParent(ConstraintWidgetContainer var0, LinearSystem var1, ConstraintWidget var2) {
      int var3;
      int var4;
      if (var0.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var2.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
         var2.mLeft.mSolverVariable = var1.createObjectVariable(var2.mLeft);
         var2.mRight.mSolverVariable = var1.createObjectVariable(var2.mRight);
         var3 = var2.mLeft.mMargin;
         var4 = var0.getWidth() - var2.mRight.mMargin;
         var1.addEquality(var2.mLeft.mSolverVariable, var3);
         var1.addEquality(var2.mRight.mSolverVariable, var4);
         var2.setHorizontalDimension(var3, var4);
         var2.mHorizontalResolution = 2;
      }

      if (var0.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var2.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
         var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
         var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
         var3 = var2.mTop.mMargin;
         var4 = var0.getHeight() - var2.mBottom.mMargin;
         var1.addEquality(var2.mTop.mSolverVariable, var3);
         var1.addEquality(var2.mBottom.mSolverVariable, var4);
         if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
            var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
            var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var3);
         }

         var2.setVerticalDimension(var3, var4);
         var2.mVerticalResolution = 2;
      }

   }

   static void checkVerticalSimpleDependency(ConstraintWidgetContainer var0, LinearSystem var1, ConstraintWidget var2) {
      ConstraintWidget.DimensionBehaviour var3 = var2.mVerticalDimensionBehaviour;
      ConstraintWidget.DimensionBehaviour var4 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
      boolean var5 = true;
      if (var3 == var4) {
         var2.mVerticalResolution = 1;
      } else {
         int var13;
         int var14;
         if (var0.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var2.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
            var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
            var13 = var2.mTop.mMargin;
            var14 = var0.getHeight() - var2.mBottom.mMargin;
            var1.addEquality(var2.mTop.mSolverVariable, var13);
            var1.addEquality(var2.mBottom.mSolverVariable, var14);
            if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
               var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
               var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var13);
            }

            var2.setVerticalDimension(var13, var14);
            var2.mVerticalResolution = 2;
         } else if (var2.mTop.mTarget != null && var2.mBottom.mTarget != null) {
            if (var2.mTop.mTarget.mOwner == var0 && var2.mBottom.mTarget.mOwner == var0) {
               var13 = var2.mTop.getMargin();
               var14 = var2.mBottom.getMargin();
               if (var0.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                  var14 = var2.getHeight() + var13;
               } else {
                  int var12 = var2.getHeight();
                  int var8 = var0.getHeight();
                  var13 = (int)((float)var13 + (float)(var8 - var13 - var14 - var12) * var2.mVerticalBiasPercent + 0.5F);
                  var14 = var2.getHeight() + var13;
               }

               var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
               var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
               var1.addEquality(var2.mTop.mSolverVariable, var13);
               var1.addEquality(var2.mBottom.mSolverVariable, var14);
               if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
                  var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
                  var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var13);
               }

               var2.mVerticalResolution = 2;
               var2.setVerticalDimension(var13, var14);
            } else {
               var2.mVerticalResolution = 1;
            }
         } else {
            if (var2.mTop.mTarget != null && var2.mTop.mTarget.mOwner == var0) {
               var13 = var2.mTop.getMargin();
               var14 = var2.getHeight() + var13;
               var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
               var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
               var1.addEquality(var2.mTop.mSolverVariable, var13);
               var1.addEquality(var2.mBottom.mSolverVariable, var14);
               if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
                  var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
                  var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var13);
               }

               var2.mVerticalResolution = 2;
               var2.setVerticalDimension(var13, var14);
            } else if (var2.mBottom.mTarget != null && var2.mBottom.mTarget.mOwner == var0) {
               var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
               var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
               var14 = var0.getHeight() - var2.mBottom.getMargin();
               var13 = var14 - var2.getHeight();
               var1.addEquality(var2.mTop.mSolverVariable, var13);
               var1.addEquality(var2.mBottom.mSolverVariable, var14);
               if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
                  var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
                  var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var13);
               }

               var2.mVerticalResolution = 2;
               var2.setVerticalDimension(var13, var14);
            } else {
               SolverVariable var10;
               if (var2.mTop.mTarget != null && var2.mTop.mTarget.mOwner.mVerticalResolution == 2) {
                  var10 = var2.mTop.mTarget.mSolverVariable;
                  var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
                  var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
                  var14 = (int)(var10.computedValue + (float)var2.mTop.getMargin() + 0.5F);
                  var13 = var2.getHeight() + var14;
                  var1.addEquality(var2.mTop.mSolverVariable, var14);
                  var1.addEquality(var2.mBottom.mSolverVariable, var13);
                  if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
                     var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
                     var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var14);
                  }

                  var2.mVerticalResolution = 2;
                  var2.setVerticalDimension(var14, var13);
               } else if (var2.mBottom.mTarget != null && var2.mBottom.mTarget.mOwner.mVerticalResolution == 2) {
                  var10 = var2.mBottom.mTarget.mSolverVariable;
                  var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
                  var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
                  var13 = (int)(var10.computedValue - (float)var2.mBottom.getMargin() + 0.5F);
                  var14 = var13 - var2.getHeight();
                  var1.addEquality(var2.mTop.mSolverVariable, var14);
                  var1.addEquality(var2.mBottom.mSolverVariable, var13);
                  if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
                     var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
                     var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var14);
                  }

                  var2.mVerticalResolution = 2;
                  var2.setVerticalDimension(var14, var13);
               } else if (var2.mBaseline.mTarget != null && var2.mBaseline.mTarget.mOwner.mVerticalResolution == 2) {
                  var10 = var2.mBaseline.mTarget.mSolverVariable;
                  var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
                  var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
                  var13 = (int)(var10.computedValue - (float)var2.mBaselineDistance + 0.5F);
                  var14 = var2.getHeight() + var13;
                  var1.addEquality(var2.mTop.mSolverVariable, var13);
                  var1.addEquality(var2.mBottom.mSolverVariable, var14);
                  var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
                  var1.addEquality(var2.mBaseline.mSolverVariable, var2.mBaselineDistance + var13);
                  var2.mVerticalResolution = 2;
                  var2.setVerticalDimension(var13, var14);
               } else {
                  boolean var6;
                  if (var2.mBaseline.mTarget != null) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  boolean var7;
                  if (var2.mTop.mTarget != null) {
                     var7 = true;
                  } else {
                     var7 = false;
                  }

                  if (var2.mBottom.mTarget == null) {
                     var5 = false;
                  }

                  if (!var6 && !var7 && !var5) {
                     if (var2 instanceof Guideline) {
                        Guideline var11 = (Guideline)var2;
                        if (var11.getOrientation() == 0) {
                           var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
                           var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
                           float var9;
                           if (var11.getRelativeBegin() != -1) {
                              var9 = (float)var11.getRelativeBegin();
                           } else if (var11.getRelativeEnd() != -1) {
                              var9 = (float)(var0.getHeight() - var11.getRelativeEnd());
                           } else {
                              var9 = (float)var0.getHeight();
                              var9 = var11.getRelativePercent() * var9;
                           }

                           var13 = (int)(var9 + 0.5F);
                           var1.addEquality(var2.mTop.mSolverVariable, var13);
                           var1.addEquality(var2.mBottom.mSolverVariable, var13);
                           var2.mVerticalResolution = 2;
                           var2.mHorizontalResolution = 2;
                           var2.setVerticalDimension(var13, var13);
                           var2.setHorizontalDimension(0, var0.getWidth());
                        }
                     } else {
                        var2.mTop.mSolverVariable = var1.createObjectVariable(var2.mTop);
                        var2.mBottom.mSolverVariable = var1.createObjectVariable(var2.mBottom);
                        var14 = var2.getY();
                        var13 = var2.getHeight();
                        var1.addEquality(var2.mTop.mSolverVariable, var14);
                        var1.addEquality(var2.mBottom.mSolverVariable, var13 + var14);
                        if (var2.mBaselineDistance > 0 || var2.getVisibility() == 8) {
                           var2.mBaseline.mSolverVariable = var1.createObjectVariable(var2.mBaseline);
                           var1.addEquality(var2.mBaseline.mSolverVariable, var14 + var2.mBaselineDistance);
                        }

                        var2.mVerticalResolution = 2;
                     }
                  }
               }
            }

         }
      }
   }
}
