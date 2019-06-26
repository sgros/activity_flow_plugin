package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour;

public class Optimizer {
    static void applyDirectResolutionHorizontalChain(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, ConstraintWidget constraintWidget) {
        int i2;
        int x;
        float f;
        float f2;
        ConstraintWidget constraintWidget2 = constraintWidgetContainer;
        LinearSystem linearSystem2 = linearSystem;
        int i3 = i;
        ConstraintWidget constraintWidget3 = constraintWidget;
        float f3 = 0.0f;
        int i4 = 0;
        int i5 = i4;
        ConstraintWidget constraintWidget4 = null;
        while (true) {
            i2 = 1;
            if (constraintWidget3 == null) {
                break;
            }
            if (constraintWidget3.getVisibility() != 8) {
                i2 = 0;
            }
            if (i2 == 0) {
                i4++;
                if (constraintWidget3.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                    i5 = ((i5 + constraintWidget3.getWidth()) + (constraintWidget3.mLeft.mTarget != null ? constraintWidget3.mLeft.getMargin() : 0)) + (constraintWidget3.mRight.mTarget != null ? constraintWidget3.mRight.getMargin() : 0);
                } else {
                    f3 += constraintWidget3.mHorizontalWeight;
                }
            }
            constraintWidget4 = constraintWidget3.mRight.mTarget != null ? constraintWidget3.mRight.mTarget.mOwner : null;
            if (constraintWidget4 != null && (constraintWidget4.mLeft.mTarget == null || !(constraintWidget4.mLeft.mTarget == null || constraintWidget4.mLeft.mTarget.mOwner == constraintWidget3))) {
                constraintWidget4 = null;
            }
            ConstraintWidget constraintWidget5 = constraintWidget4;
            constraintWidget4 = constraintWidget3;
            constraintWidget3 = constraintWidget5;
        }
        if (constraintWidget4 != null) {
            x = constraintWidget4.mRight.mTarget != null ? constraintWidget4.mRight.mTarget.mOwner.getX() : 0;
            if (constraintWidget4.mRight.mTarget != null && constraintWidget4.mRight.mTarget.mOwner == constraintWidget2) {
                x = constraintWidgetContainer.getRight();
            }
        } else {
            x = 0;
        }
        float f4 = ((float) (x - 0)) - ((float) i5);
        float f5 = f4 / ((float) (i4 + 1));
        if (i3 == 0) {
            f = f5;
            f2 = f;
        } else {
            f = 0.0f;
            f2 = f4 / ((float) i3);
        }
        constraintWidget4 = constraintWidget;
        while (constraintWidget4 != null) {
            i2 = constraintWidget4.mLeft.mTarget != null ? constraintWidget4.mLeft.getMargin() : 0;
            int margin = constraintWidget4.mRight.mTarget != null ? constraintWidget4.mRight.getMargin() : 0;
            if (constraintWidget4.getVisibility() != 8) {
                float f6 = (float) i2;
                f += f6;
                linearSystem2.addEquality(constraintWidget4.mLeft.mSolverVariable, (int) (f + 0.5f));
                if (constraintWidget4.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                    f += (float) constraintWidget4.getWidth();
                } else if (f3 == 0.0f) {
                    f += (f2 - f6) - ((float) margin);
                } else {
                    f += (((constraintWidget4.mHorizontalWeight * f4) / f3) - f6) - ((float) margin);
                }
                linearSystem2.addEquality(constraintWidget4.mRight.mSolverVariable, (int) (0.5f + f));
                if (i3 == 0) {
                    f += f2;
                }
                f += (float) margin;
            } else {
                int i6 = (int) ((f - (f2 / 2.0f)) + 0.5f);
                linearSystem2.addEquality(constraintWidget4.mLeft.mSolverVariable, i6);
                linearSystem2.addEquality(constraintWidget4.mRight.mSolverVariable, i6);
            }
            ConstraintWidget constraintWidget6 = constraintWidget4.mRight.mTarget != null ? constraintWidget4.mRight.mTarget.mOwner : null;
            if (!(constraintWidget6 == null || constraintWidget6.mLeft.mTarget == null || constraintWidget6.mLeft.mTarget.mOwner == constraintWidget4)) {
                constraintWidget6 = null;
            }
            constraintWidget4 = constraintWidget6 == constraintWidget2 ? null : constraintWidget6;
        }
    }

    static void applyDirectResolutionVerticalChain(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, ConstraintWidget constraintWidget) {
        int i2;
        int x;
        float f;
        float f2;
        ConstraintWidget constraintWidget2 = constraintWidgetContainer;
        LinearSystem linearSystem2 = linearSystem;
        int i3 = i;
        ConstraintWidget constraintWidget3 = constraintWidget;
        float f3 = 0.0f;
        int i4 = 0;
        int i5 = i4;
        ConstraintWidget constraintWidget4 = null;
        while (true) {
            i2 = 1;
            if (constraintWidget3 == null) {
                break;
            }
            if (constraintWidget3.getVisibility() != 8) {
                i2 = 0;
            }
            if (i2 == 0) {
                i4++;
                if (constraintWidget3.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                    i5 = ((i5 + constraintWidget3.getHeight()) + (constraintWidget3.mTop.mTarget != null ? constraintWidget3.mTop.getMargin() : 0)) + (constraintWidget3.mBottom.mTarget != null ? constraintWidget3.mBottom.getMargin() : 0);
                } else {
                    f3 += constraintWidget3.mVerticalWeight;
                }
            }
            constraintWidget4 = constraintWidget3.mBottom.mTarget != null ? constraintWidget3.mBottom.mTarget.mOwner : null;
            if (constraintWidget4 != null && (constraintWidget4.mTop.mTarget == null || !(constraintWidget4.mTop.mTarget == null || constraintWidget4.mTop.mTarget.mOwner == constraintWidget3))) {
                constraintWidget4 = null;
            }
            ConstraintWidget constraintWidget5 = constraintWidget4;
            constraintWidget4 = constraintWidget3;
            constraintWidget3 = constraintWidget5;
        }
        if (constraintWidget4 != null) {
            x = constraintWidget4.mBottom.mTarget != null ? constraintWidget4.mBottom.mTarget.mOwner.getX() : 0;
            if (constraintWidget4.mBottom.mTarget != null && constraintWidget4.mBottom.mTarget.mOwner == constraintWidget2) {
                x = constraintWidgetContainer.getBottom();
            }
        } else {
            x = 0;
        }
        float f4 = ((float) (x - 0)) - ((float) i5);
        float f5 = f4 / ((float) (i4 + 1));
        if (i3 == 0) {
            f = f5;
            f2 = f;
        } else {
            f = 0.0f;
            f2 = f4 / ((float) i3);
        }
        constraintWidget4 = constraintWidget;
        while (constraintWidget4 != null) {
            i2 = constraintWidget4.mTop.mTarget != null ? constraintWidget4.mTop.getMargin() : 0;
            int margin = constraintWidget4.mBottom.mTarget != null ? constraintWidget4.mBottom.getMargin() : 0;
            if (constraintWidget4.getVisibility() != 8) {
                float f6 = (float) i2;
                f += f6;
                linearSystem2.addEquality(constraintWidget4.mTop.mSolverVariable, (int) (f + 0.5f));
                if (constraintWidget4.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                    f += (float) constraintWidget4.getHeight();
                } else if (f3 == 0.0f) {
                    f += (f2 - f6) - ((float) margin);
                } else {
                    f += (((constraintWidget4.mVerticalWeight * f4) / f3) - f6) - ((float) margin);
                }
                linearSystem2.addEquality(constraintWidget4.mBottom.mSolverVariable, (int) (0.5f + f));
                if (i3 == 0) {
                    f += f2;
                }
                f += (float) margin;
            } else {
                int i6 = (int) ((f - (f2 / 2.0f)) + 0.5f);
                linearSystem2.addEquality(constraintWidget4.mTop.mSolverVariable, i6);
                linearSystem2.addEquality(constraintWidget4.mBottom.mSolverVariable, i6);
            }
            ConstraintWidget constraintWidget6 = constraintWidget4.mBottom.mTarget != null ? constraintWidget4.mBottom.mTarget.mOwner : null;
            if (!(constraintWidget6 == null || constraintWidget6.mTop.mTarget == null || constraintWidget6.mTop.mTarget.mOwner == constraintWidget4)) {
                constraintWidget6 = null;
            }
            constraintWidget4 = constraintWidget6 == constraintWidget2 ? null : constraintWidget6;
        }
    }

    static void checkMatchParent(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        int i;
        if (constraintWidgetContainer.mHorizontalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            i = constraintWidget.mLeft.mMargin;
            int width = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
            constraintWidget.setHorizontalDimension(i, width);
            constraintWidget.mHorizontalResolution = 2;
        }
        if (constraintWidgetContainer.mVerticalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            i = constraintWidget.mTop.mMargin;
            int height = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i);
            }
            constraintWidget.setVerticalDimension(i, height);
            constraintWidget.mVerticalResolution = 2;
        }
    }

    static void checkHorizontalSimpleDependency(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        int i;
        int width;
        if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            constraintWidget.mHorizontalResolution = 1;
        } else if (constraintWidgetContainer.mHorizontalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            i = constraintWidget.mLeft.mMargin;
            width = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
            constraintWidget.setHorizontalDimension(i, width);
            constraintWidget.mHorizontalResolution = 2;
        } else if (constraintWidget.mLeft.mTarget == null || constraintWidget.mRight.mTarget == null) {
            SolverVariable solverVariable;
            if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer) {
                width = constraintWidget.mLeft.getMargin();
                i = constraintWidget.getWidth() + width;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, width);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, i);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(width, i);
            } else if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                width = constraintWidgetContainer.getWidth() - constraintWidget.mRight.getMargin();
                i = width - constraintWidget.getWidth();
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(i, width);
            } else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mHorizontalResolution == 2) {
                solverVariable = constraintWidget.mLeft.mTarget.mSolverVariable;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                width = (int) ((solverVariable.computedValue + ((float) constraintWidget.mLeft.getMargin())) + 0.5f);
                i = constraintWidget.getWidth() + width;
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, width);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, i);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(width, i);
            } else if (constraintWidget.mRight.mTarget == null || constraintWidget.mRight.mTarget.mOwner.mHorizontalResolution != 2) {
                i = constraintWidget.mLeft.mTarget != null ? 1 : 0;
                int i2 = constraintWidget.mRight.mTarget != null ? 1 : 0;
                if (i == 0 && i2 == 0) {
                    if (constraintWidget instanceof Guideline) {
                        Guideline guideline = (Guideline) constraintWidget;
                        if (guideline.getOrientation() == 1) {
                            float relativeBegin;
                            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                            if (guideline.getRelativeBegin() != -1) {
                                relativeBegin = (float) guideline.getRelativeBegin();
                            } else if (guideline.getRelativeEnd() != -1) {
                                relativeBegin = (float) (constraintWidgetContainer.getWidth() - guideline.getRelativeEnd());
                            } else {
                                relativeBegin = guideline.getRelativePercent() * ((float) constraintWidgetContainer.getWidth());
                            }
                            i = (int) (relativeBegin + 0.5f);
                            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
                            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, i);
                            constraintWidget.mHorizontalResolution = 2;
                            constraintWidget.mVerticalResolution = 2;
                            constraintWidget.setHorizontalDimension(i, i);
                            constraintWidget.setVerticalDimension(0, constraintWidgetContainer.getHeight());
                        }
                    } else {
                        constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                        constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                        width = constraintWidget.getX();
                        i = constraintWidget.getWidth() + width;
                        linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, width);
                        linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, i);
                        constraintWidget.mHorizontalResolution = 2;
                    }
                }
            } else {
                solverVariable = constraintWidget.mRight.mTarget.mSolverVariable;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                width = (int) ((solverVariable.computedValue - ((float) constraintWidget.mRight.getMargin())) + 0.5f);
                i = width - constraintWidget.getWidth();
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(i, width);
            }
        } else if (constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
            i = constraintWidget.mLeft.getMargin();
            int margin = constraintWidget.mRight.getMargin();
            if (constraintWidgetContainer.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                width = constraintWidgetContainer.getWidth() - margin;
            } else {
                i += (int) ((((float) (((constraintWidgetContainer.getWidth() - i) - margin) - constraintWidget.getWidth())) * constraintWidget.mHorizontalBiasPercent) + 0.5f);
                width = constraintWidget.getWidth() + i;
            }
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(i, width);
        } else {
            constraintWidget.mHorizontalResolution = 1;
        }
    }

    static void checkVerticalSimpleDependency(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        int i = 1;
        int i2;
        int height;
        if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
            constraintWidget.mVerticalResolution = 1;
        } else if (constraintWidgetContainer.mVerticalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            i2 = constraintWidget.mTop.mMargin;
            height = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i2);
            }
            constraintWidget.setVerticalDimension(i2, height);
            constraintWidget.mVerticalResolution = 2;
        } else if (constraintWidget.mTop.mTarget == null || constraintWidget.mBottom.mTarget == null) {
            SolverVariable solverVariable;
            if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer) {
                height = constraintWidget.mTop.getMargin();
                i2 = constraintWidget.getHeight() + height;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, height);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, i2);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + height);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(height, i2);
            } else if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                height = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.getMargin();
                i2 = height - constraintWidget.getHeight();
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i2);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i2);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(i2, height);
            } else if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mVerticalResolution == 2) {
                solverVariable = constraintWidget.mTop.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                height = (int) ((solverVariable.computedValue + ((float) constraintWidget.mTop.getMargin())) + 0.5f);
                i2 = constraintWidget.getHeight() + height;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, height);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, i2);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + height);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(height, i2);
            } else if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner.mVerticalResolution == 2) {
                solverVariable = constraintWidget.mBottom.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                height = (int) ((solverVariable.computedValue - ((float) constraintWidget.mBottom.getMargin())) + 0.5f);
                i2 = height - constraintWidget.getHeight();
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i2);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i2);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(i2, height);
            } else if (constraintWidget.mBaseline.mTarget == null || constraintWidget.mBaseline.mTarget.mOwner.mVerticalResolution != 2) {
                i2 = constraintWidget.mBaseline.mTarget != null ? 1 : 0;
                int i3 = constraintWidget.mTop.mTarget != null ? 1 : 0;
                if (constraintWidget.mBottom.mTarget == null) {
                    i = 0;
                }
                if (i2 == 0 && i3 == 0 && i == 0) {
                    if (constraintWidget instanceof Guideline) {
                        Guideline guideline = (Guideline) constraintWidget;
                        if (guideline.getOrientation() == 0) {
                            float relativeBegin;
                            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                            if (guideline.getRelativeBegin() != -1) {
                                relativeBegin = (float) guideline.getRelativeBegin();
                            } else if (guideline.getRelativeEnd() != -1) {
                                relativeBegin = (float) (constraintWidgetContainer.getHeight() - guideline.getRelativeEnd());
                            } else {
                                relativeBegin = guideline.getRelativePercent() * ((float) constraintWidgetContainer.getHeight());
                            }
                            i2 = (int) (relativeBegin + 0.5f);
                            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i2);
                            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, i2);
                            constraintWidget.mVerticalResolution = 2;
                            constraintWidget.mHorizontalResolution = 2;
                            constraintWidget.setVerticalDimension(i2, i2);
                            constraintWidget.setHorizontalDimension(0, constraintWidgetContainer.getWidth());
                        }
                    } else {
                        constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                        constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                        height = constraintWidget.getY();
                        i2 = constraintWidget.getHeight() + height;
                        linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, height);
                        linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, i2);
                        if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                            constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                            linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, height + constraintWidget.mBaselineDistance);
                        }
                        constraintWidget.mVerticalResolution = 2;
                    }
                }
            } else {
                solverVariable = constraintWidget.mBaseline.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                height = (int) ((solverVariable.computedValue - ((float) constraintWidget.mBaselineDistance)) + 0.5f);
                i2 = constraintWidget.getHeight() + height;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, height);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, i2);
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + height);
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(height, i2);
            }
        } else if (constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
            i2 = constraintWidget.mTop.getMargin();
            i = constraintWidget.mBottom.getMargin();
            if (constraintWidgetContainer.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                height = constraintWidget.getHeight() + i2;
            } else {
                i2 = (int) ((((float) i2) + (((float) (((constraintWidgetContainer.getHeight() - i2) - i) - constraintWidget.getHeight())) * constraintWidget.mVerticalBiasPercent)) + 0.5f);
                height = constraintWidget.getHeight() + i2;
            }
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i2);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(i2, height);
        } else {
            constraintWidget.mVerticalResolution = 1;
        }
    }
}
