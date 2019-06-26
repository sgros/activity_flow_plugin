// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;

public class Optimizer
{
    static void applyDirectResolutionHorizontalChain(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, ConstraintWidget constraintWidget) {
        ConstraintWidget constraintWidget2 = constraintWidget;
        float n2 = 0.0f;
        int n4;
        int n3 = n4 = 0;
        ConstraintWidget constraintWidget3 = null;
        while (true) {
            boolean b = true;
            if (constraintWidget2 == null) {
                break;
            }
            if (constraintWidget2.getVisibility() != 8) {
                b = false;
            }
            int n5 = n3;
            int n6 = n4;
            float n7 = n2;
            if (!b) {
                ++n3;
                if (constraintWidget2.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    final int width = constraintWidget2.getWidth();
                    int margin;
                    if (constraintWidget2.mLeft.mTarget != null) {
                        margin = constraintWidget2.mLeft.getMargin();
                    }
                    else {
                        margin = 0;
                    }
                    int margin2;
                    if (constraintWidget2.mRight.mTarget != null) {
                        margin2 = constraintWidget2.mRight.getMargin();
                    }
                    else {
                        margin2 = 0;
                    }
                    n6 = n4 + width + margin + margin2;
                    n5 = n3;
                    n7 = n2;
                }
                else {
                    n7 = n2 + constraintWidget2.mHorizontalWeight;
                    n6 = n4;
                    n5 = n3;
                }
            }
            ConstraintWidget mOwner;
            if (constraintWidget2.mRight.mTarget != null) {
                mOwner = constraintWidget2.mRight.mTarget.mOwner;
            }
            else {
                mOwner = null;
            }
            ConstraintWidget constraintWidget4 = mOwner;
            Label_0262: {
                if (mOwner != null) {
                    if (mOwner.mLeft.mTarget != null) {
                        constraintWidget4 = mOwner;
                        if (mOwner.mLeft.mTarget == null) {
                            break Label_0262;
                        }
                        constraintWidget4 = mOwner;
                        if (mOwner.mLeft.mTarget.mOwner == constraintWidget2) {
                            break Label_0262;
                        }
                    }
                    constraintWidget4 = null;
                }
            }
            constraintWidget3 = constraintWidget2;
            constraintWidget2 = constraintWidget4;
            n3 = n5;
            n4 = n6;
            n2 = n7;
        }
        int right;
        if (constraintWidget3 != null) {
            int x;
            if (constraintWidget3.mRight.mTarget != null) {
                x = constraintWidget3.mRight.mTarget.mOwner.getX();
            }
            else {
                x = 0;
            }
            right = x;
            if (constraintWidget3.mRight.mTarget != null) {
                right = x;
                if (constraintWidget3.mRight.mTarget.mOwner == constraintWidgetContainer) {
                    right = constraintWidgetContainer.getRight();
                }
            }
        }
        else {
            right = 0;
        }
        final float n8 = right - 0 - (float)n4;
        float n9 = n8 / (n3 + 1);
        float n10;
        if (n == 0) {
            n10 = n9;
        }
        else {
            n10 = n8 / n;
            n9 = 0.0f;
        }
        while (constraintWidget != null) {
            int margin3;
            if (constraintWidget.mLeft.mTarget != null) {
                margin3 = constraintWidget.mLeft.getMargin();
            }
            else {
                margin3 = 0;
            }
            int margin4;
            if (constraintWidget.mRight.mTarget != null) {
                margin4 = constraintWidget.mRight.getMargin();
            }
            else {
                margin4 = 0;
            }
            if (constraintWidget.getVisibility() != 8) {
                final float n11 = (float)margin3;
                final float n12 = n9 + n11;
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, (int)(n12 + 0.5f));
                float n13;
                if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (n2 == 0.0f) {
                        n13 = n12 + (n10 - n11 - margin4);
                    }
                    else {
                        n13 = n12 + (constraintWidget.mHorizontalWeight * n8 / n2 - n11 - margin4);
                    }
                }
                else {
                    n13 = n12 + constraintWidget.getWidth();
                }
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, (int)(0.5f + n13));
                float n14 = n13;
                if (n == 0) {
                    n14 = n13 + n10;
                }
                n9 = n14 + margin4;
            }
            else {
                final float n15 = n10 / 2.0f;
                final SolverVariable mSolverVariable = constraintWidget.mLeft.mSolverVariable;
                final int n16 = (int)(n9 - n15 + 0.5f);
                linearSystem.addEquality(mSolverVariable, n16);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n16);
            }
            ConstraintWidget mOwner2;
            if (constraintWidget.mRight.mTarget != null) {
                mOwner2 = constraintWidget.mRight.mTarget.mOwner;
            }
            else {
                mOwner2 = null;
            }
            ConstraintWidget constraintWidget5 = mOwner2;
            if (mOwner2 != null) {
                constraintWidget5 = mOwner2;
                if (mOwner2.mLeft.mTarget != null) {
                    constraintWidget5 = mOwner2;
                    if (mOwner2.mLeft.mTarget.mOwner != constraintWidget) {
                        constraintWidget5 = null;
                    }
                }
            }
            if (constraintWidget5 == constraintWidgetContainer) {
                constraintWidget = null;
            }
            else {
                constraintWidget = constraintWidget5;
            }
        }
    }
    
    static void applyDirectResolutionVerticalChain(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, ConstraintWidget constraintWidget) {
        ConstraintWidget constraintWidget2 = constraintWidget;
        float n2 = 0.0f;
        int n4;
        int n3 = n4 = 0;
        ConstraintWidget constraintWidget3 = null;
        while (true) {
            boolean b = true;
            if (constraintWidget2 == null) {
                break;
            }
            if (constraintWidget2.getVisibility() != 8) {
                b = false;
            }
            int n5 = n3;
            int n6 = n4;
            float n7 = n2;
            if (!b) {
                ++n3;
                if (constraintWidget2.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    final int height = constraintWidget2.getHeight();
                    int margin;
                    if (constraintWidget2.mTop.mTarget != null) {
                        margin = constraintWidget2.mTop.getMargin();
                    }
                    else {
                        margin = 0;
                    }
                    int margin2;
                    if (constraintWidget2.mBottom.mTarget != null) {
                        margin2 = constraintWidget2.mBottom.getMargin();
                    }
                    else {
                        margin2 = 0;
                    }
                    n6 = n4 + height + margin + margin2;
                    n5 = n3;
                    n7 = n2;
                }
                else {
                    n7 = n2 + constraintWidget2.mVerticalWeight;
                    n6 = n4;
                    n5 = n3;
                }
            }
            ConstraintWidget mOwner;
            if (constraintWidget2.mBottom.mTarget != null) {
                mOwner = constraintWidget2.mBottom.mTarget.mOwner;
            }
            else {
                mOwner = null;
            }
            ConstraintWidget constraintWidget4 = mOwner;
            Label_0262: {
                if (mOwner != null) {
                    if (mOwner.mTop.mTarget != null) {
                        constraintWidget4 = mOwner;
                        if (mOwner.mTop.mTarget == null) {
                            break Label_0262;
                        }
                        constraintWidget4 = mOwner;
                        if (mOwner.mTop.mTarget.mOwner == constraintWidget2) {
                            break Label_0262;
                        }
                    }
                    constraintWidget4 = null;
                }
            }
            constraintWidget3 = constraintWidget2;
            constraintWidget2 = constraintWidget4;
            n3 = n5;
            n4 = n6;
            n2 = n7;
        }
        int bottom;
        if (constraintWidget3 != null) {
            int x;
            if (constraintWidget3.mBottom.mTarget != null) {
                x = constraintWidget3.mBottom.mTarget.mOwner.getX();
            }
            else {
                x = 0;
            }
            bottom = x;
            if (constraintWidget3.mBottom.mTarget != null) {
                bottom = x;
                if (constraintWidget3.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                    bottom = constraintWidgetContainer.getBottom();
                }
            }
        }
        else {
            bottom = 0;
        }
        final float n8 = bottom - 0 - (float)n4;
        float n9 = n8 / (n3 + 1);
        float n10;
        if (n == 0) {
            n10 = n9;
        }
        else {
            n10 = n8 / n;
            n9 = 0.0f;
        }
        while (constraintWidget != null) {
            int margin3;
            if (constraintWidget.mTop.mTarget != null) {
                margin3 = constraintWidget.mTop.getMargin();
            }
            else {
                margin3 = 0;
            }
            int margin4;
            if (constraintWidget.mBottom.mTarget != null) {
                margin4 = constraintWidget.mBottom.getMargin();
            }
            else {
                margin4 = 0;
            }
            if (constraintWidget.getVisibility() != 8) {
                final float n11 = (float)margin3;
                final float n12 = n9 + n11;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, (int)(n12 + 0.5f));
                float n13;
                if (constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (n2 == 0.0f) {
                        n13 = n12 + (n10 - n11 - margin4);
                    }
                    else {
                        n13 = n12 + (constraintWidget.mVerticalWeight * n8 / n2 - n11 - margin4);
                    }
                }
                else {
                    n13 = n12 + constraintWidget.getHeight();
                }
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, (int)(0.5f + n13));
                float n14 = n13;
                if (n == 0) {
                    n14 = n13 + n10;
                }
                n9 = n14 + margin4;
            }
            else {
                final float n15 = n10 / 2.0f;
                final SolverVariable mSolverVariable = constraintWidget.mTop.mSolverVariable;
                final int n16 = (int)(n9 - n15 + 0.5f);
                linearSystem.addEquality(mSolverVariable, n16);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n16);
            }
            ConstraintWidget mOwner2;
            if (constraintWidget.mBottom.mTarget != null) {
                mOwner2 = constraintWidget.mBottom.mTarget.mOwner;
            }
            else {
                mOwner2 = null;
            }
            ConstraintWidget constraintWidget5 = mOwner2;
            if (mOwner2 != null) {
                constraintWidget5 = mOwner2;
                if (mOwner2.mTop.mTarget != null) {
                    constraintWidget5 = mOwner2;
                    if (mOwner2.mTop.mTarget.mOwner != constraintWidget) {
                        constraintWidget5 = null;
                    }
                }
            }
            if (constraintWidget5 == constraintWidgetContainer) {
                constraintWidget = null;
            }
            else {
                constraintWidget = constraintWidget5;
            }
        }
    }
    
    static void checkHorizontalSimpleDependency(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            constraintWidget.mHorizontalResolution = 1;
            return;
        }
        if (constraintWidgetContainer.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            final int mMargin = constraintWidget.mLeft.mMargin;
            final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.setHorizontalDimension(mMargin, n);
            constraintWidget.mHorizontalResolution = 2;
            return;
        }
        if (constraintWidget.mLeft.mTarget == null || constraintWidget.mRight.mTarget == null) {
            if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer) {
                final int margin = constraintWidget.mLeft.getMargin();
                final int n2 = constraintWidget.getWidth() + margin;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, margin);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n2);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(margin, n2);
            }
            else if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                final int n3 = constraintWidgetContainer.getWidth() - constraintWidget.mRight.getMargin();
                final int n4 = n3 - constraintWidget.getWidth();
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n4);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n3);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(n4, n3);
            }
            else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mHorizontalResolution == 2) {
                final SolverVariable mSolverVariable = constraintWidget.mLeft.mTarget.mSolverVariable;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                final int n5 = (int)(mSolverVariable.computedValue + constraintWidget.mLeft.getMargin() + 0.5f);
                final int n6 = constraintWidget.getWidth() + n5;
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n5);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n6);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(n5, n6);
            }
            else if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner.mHorizontalResolution == 2) {
                final SolverVariable mSolverVariable2 = constraintWidget.mRight.mTarget.mSolverVariable;
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                final int n7 = (int)(mSolverVariable2.computedValue - constraintWidget.mRight.getMargin() + 0.5f);
                final int n8 = n7 - constraintWidget.getWidth();
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n8);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n7);
                constraintWidget.mHorizontalResolution = 2;
                constraintWidget.setHorizontalDimension(n8, n7);
            }
            else {
                final boolean b = constraintWidget.mLeft.mTarget != null;
                final boolean b2 = constraintWidget.mRight.mTarget != null;
                if (!b && !b2) {
                    if (constraintWidget instanceof Guideline) {
                        final Guideline guideline = (Guideline)constraintWidget;
                        if (guideline.getOrientation() == 1) {
                            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                            float n9;
                            if (guideline.getRelativeBegin() != -1) {
                                n9 = (float)guideline.getRelativeBegin();
                            }
                            else if (guideline.getRelativeEnd() != -1) {
                                n9 = (float)(constraintWidgetContainer.getWidth() - guideline.getRelativeEnd());
                            }
                            else {
                                n9 = guideline.getRelativePercent() * constraintWidgetContainer.getWidth();
                            }
                            final int n10 = (int)(n9 + 0.5f);
                            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n10);
                            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n10);
                            constraintWidget.mHorizontalResolution = 2;
                            constraintWidget.mVerticalResolution = 2;
                            constraintWidget.setHorizontalDimension(n10, n10);
                            constraintWidget.setVerticalDimension(0, constraintWidgetContainer.getHeight());
                        }
                    }
                    else {
                        constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                        constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                        final int x = constraintWidget.getX();
                        final int width = constraintWidget.getWidth();
                        linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, x);
                        linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width + x);
                        constraintWidget.mHorizontalResolution = 2;
                    }
                }
            }
            return;
        }
        if (constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
            int margin2 = constraintWidget.mLeft.getMargin();
            final int margin3 = constraintWidget.mRight.getMargin();
            int n11;
            if (constraintWidgetContainer.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                n11 = constraintWidgetContainer.getWidth() - margin3;
            }
            else {
                margin2 += (int)((constraintWidgetContainer.getWidth() - margin2 - margin3 - constraintWidget.getWidth()) * constraintWidget.mHorizontalBiasPercent + 0.5f);
                n11 = constraintWidget.getWidth() + margin2;
            }
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, margin2);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n11);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(margin2, n11);
            return;
        }
        constraintWidget.mHorizontalResolution = 1;
    }
    
    static void checkMatchParent(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidgetContainer.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            final int mMargin = constraintWidget.mLeft.mMargin;
            final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.setHorizontalDimension(mMargin, n);
            constraintWidget.mHorizontalResolution = 2;
        }
        if (constraintWidgetContainer.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            final int mMargin2 = constraintWidget.mTop.mMargin;
            final int n2 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin2);
            }
            constraintWidget.setVerticalDimension(mMargin2, n2);
            constraintWidget.mVerticalResolution = 2;
        }
    }
    
    static void checkVerticalSimpleDependency(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        final ConstraintWidget.DimensionBehaviour mVerticalDimensionBehaviour = constraintWidget.mVerticalDimensionBehaviour;
        final ConstraintWidget.DimensionBehaviour match_CONSTRAINT = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean b = true;
        if (mVerticalDimensionBehaviour == match_CONSTRAINT) {
            constraintWidget.mVerticalResolution = 1;
            return;
        }
        if (constraintWidgetContainer.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            final int mMargin = constraintWidget.mTop.mMargin;
            final int n = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin);
            }
            constraintWidget.setVerticalDimension(mMargin, n);
            constraintWidget.mVerticalResolution = 2;
            return;
        }
        if (constraintWidget.mTop.mTarget == null || constraintWidget.mBottom.mTarget == null) {
            if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer) {
                final int margin = constraintWidget.mTop.getMargin();
                final int n2 = constraintWidget.getHeight() + margin;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, margin);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + margin);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(margin, n2);
            }
            else if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n3 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.getMargin();
                final int n4 = n3 - constraintWidget.getHeight();
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n4);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n3);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n4);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n4, n3);
            }
            else if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mVerticalResolution == 2) {
                final SolverVariable mSolverVariable = constraintWidget.mTop.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n5 = (int)(mSolverVariable.computedValue + constraintWidget.mTop.getMargin() + 0.5f);
                final int n6 = constraintWidget.getHeight() + n5;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n5);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n6);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n5);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n5, n6);
            }
            else if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner.mVerticalResolution == 2) {
                final SolverVariable mSolverVariable2 = constraintWidget.mBottom.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n7 = (int)(mSolverVariable2.computedValue - constraintWidget.mBottom.getMargin() + 0.5f);
                final int n8 = n7 - constraintWidget.getHeight();
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n8);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n7);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n8);
                }
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n8, n7);
            }
            else if (constraintWidget.mBaseline.mTarget != null && constraintWidget.mBaseline.mTarget.mOwner.mVerticalResolution == 2) {
                final SolverVariable mSolverVariable3 = constraintWidget.mBaseline.mTarget.mSolverVariable;
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int n9 = (int)(mSolverVariable3.computedValue - constraintWidget.mBaselineDistance + 0.5f);
                final int n10 = constraintWidget.getHeight() + n9;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n9);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n10);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n9);
                constraintWidget.mVerticalResolution = 2;
                constraintWidget.setVerticalDimension(n9, n10);
            }
            else {
                final boolean b2 = constraintWidget.mBaseline.mTarget != null;
                final boolean b3 = constraintWidget.mTop.mTarget != null;
                if (constraintWidget.mBottom.mTarget == null) {
                    b = false;
                }
                if (!b2 && !b3 && !b) {
                    if (constraintWidget instanceof Guideline) {
                        final Guideline guideline = (Guideline)constraintWidget;
                        if (guideline.getOrientation() == 0) {
                            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                            float n11;
                            if (guideline.getRelativeBegin() != -1) {
                                n11 = (float)guideline.getRelativeBegin();
                            }
                            else if (guideline.getRelativeEnd() != -1) {
                                n11 = (float)(constraintWidgetContainer.getHeight() - guideline.getRelativeEnd());
                            }
                            else {
                                n11 = guideline.getRelativePercent() * constraintWidgetContainer.getHeight();
                            }
                            final int n12 = (int)(n11 + 0.5f);
                            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n12);
                            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n12);
                            constraintWidget.mVerticalResolution = 2;
                            constraintWidget.mHorizontalResolution = 2;
                            constraintWidget.setVerticalDimension(n12, n12);
                            constraintWidget.setHorizontalDimension(0, constraintWidgetContainer.getWidth());
                        }
                    }
                    else {
                        constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                        constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                        final int y = constraintWidget.getY();
                        final int height = constraintWidget.getHeight();
                        linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, y);
                        linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height + y);
                        if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                            linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), y + constraintWidget.mBaselineDistance);
                        }
                        constraintWidget.mVerticalResolution = 2;
                    }
                }
            }
            return;
        }
        if (constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
            int margin2 = constraintWidget.mTop.getMargin();
            final int margin3 = constraintWidget.mBottom.getMargin();
            int n13;
            if (constraintWidgetContainer.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                n13 = constraintWidget.getHeight() + margin2;
            }
            else {
                margin2 = (int)(margin2 + (constraintWidgetContainer.getHeight() - margin2 - margin3 - constraintWidget.getHeight()) * constraintWidget.mVerticalBiasPercent + 0.5f);
                n13 = constraintWidget.getHeight() + margin2;
            }
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, margin2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n13);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + margin2);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(margin2, n13);
            return;
        }
        constraintWidget.mVerticalResolution = 1;
    }
}
