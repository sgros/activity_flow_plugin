package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour;

public class Optimizer {
    static boolean[] flags = new boolean[3];

    static void checkMatchParent(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        int i;
        if (constraintWidgetContainer.mListDimensionBehaviors[0] != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_PARENT) {
            i = constraintWidget.mLeft.mMargin;
            int width = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(i, width);
        }
        if (constraintWidgetContainer.mListDimensionBehaviors[1] != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_PARENT) {
            i = constraintWidget.mTop.mMargin;
            int height = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, height);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(i, height);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x003e A:{RETURN} */
    private static boolean optimizableMatchConstraint(android.support.constraint.solver.widgets.ConstraintWidget r4, int r5) {
        /*
        r0 = r4.mListDimensionBehaviors;
        r0 = r0[r5];
        r1 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        r2 = 0;
        if (r0 == r1) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r0 = r4.mDimensionRatio;
        r1 = 0;
        r3 = 1;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r0 == 0) goto L_0x0020;
    L_0x0012:
        r4 = r4.mListDimensionBehaviors;
        if (r5 != 0) goto L_0x0017;
    L_0x0016:
        goto L_0x0018;
    L_0x0017:
        r3 = 0;
    L_0x0018:
        r4 = r4[r3];
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r4 != r5) goto L_0x001f;
    L_0x001e:
        return r2;
    L_0x001f:
        return r2;
    L_0x0020:
        if (r5 != 0) goto L_0x0030;
    L_0x0022:
        r5 = r4.mMatchConstraintDefaultWidth;
        if (r5 == 0) goto L_0x0027;
    L_0x0026:
        return r2;
    L_0x0027:
        r5 = r4.mMatchConstraintMinWidth;
        if (r5 != 0) goto L_0x002f;
    L_0x002b:
        r4 = r4.mMatchConstraintMaxWidth;
        if (r4 == 0) goto L_0x003e;
    L_0x002f:
        return r2;
    L_0x0030:
        r5 = r4.mMatchConstraintDefaultHeight;
        if (r5 == 0) goto L_0x0035;
    L_0x0034:
        return r2;
    L_0x0035:
        r5 = r4.mMatchConstraintMinHeight;
        if (r5 != 0) goto L_0x003f;
    L_0x0039:
        r4 = r4.mMatchConstraintMaxHeight;
        if (r4 == 0) goto L_0x003e;
    L_0x003d:
        goto L_0x003f;
    L_0x003e:
        return r3;
    L_0x003f:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Optimizer.optimizableMatchConstraint(android.support.constraint.solver.widgets.ConstraintWidget, int):boolean");
    }

    static void analyze(int i, ConstraintWidget constraintWidget) {
        ConstraintWidget constraintWidget2 = constraintWidget;
        constraintWidget.updateResolutionNodes();
        ResolutionAnchor resolutionNode = constraintWidget2.mLeft.getResolutionNode();
        ResolutionAnchor resolutionNode2 = constraintWidget2.mTop.getResolutionNode();
        ResolutionAnchor resolutionNode3 = constraintWidget2.mRight.getResolutionNode();
        ResolutionAnchor resolutionNode4 = constraintWidget2.mBottom.getResolutionNode();
        Object obj = (i & 8) == 8 ? 1 : null;
        Object obj2 = (constraintWidget2.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget2, 0)) ? 1 : null;
        if (!(resolutionNode.type == 4 || resolutionNode3.type == 4)) {
            if (constraintWidget2.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED || (obj2 != null && constraintWidget.getVisibility() == 8)) {
                if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null) {
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    if (obj != null) {
                        resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                    }
                } else if (constraintWidget2.mLeft.mTarget != null && constraintWidget2.mRight.mTarget == null) {
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    if (obj != null) {
                        resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                    }
                } else if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget != null) {
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                    if (obj != null) {
                        resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                    }
                } else if (!(constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget == null)) {
                    resolutionNode.setType(2);
                    resolutionNode3.setType(2);
                    if (obj != null) {
                        constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                        constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                        resolutionNode.setOpposite(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                        resolutionNode3.setOpposite(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode.setOpposite(resolutionNode3, (float) (-constraintWidget.getWidth()));
                        resolutionNode3.setOpposite(resolutionNode, (float) constraintWidget.getWidth());
                    }
                }
            } else if (obj2 != null) {
                int width = constraintWidget.getWidth();
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null) {
                    if (obj != null) {
                        resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode3.dependsOn(resolutionNode, width);
                    }
                } else if (constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget != null) {
                    if (constraintWidget2.mLeft.mTarget != null || constraintWidget2.mRight.mTarget == null) {
                        if (!(constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget == null)) {
                            if (obj != null) {
                                constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                                constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                            }
                            if (constraintWidget2.mDimensionRatio == 0.0f) {
                                resolutionNode.setType(3);
                                resolutionNode3.setType(3);
                                resolutionNode.setOpposite(resolutionNode3, 0.0f);
                                resolutionNode3.setOpposite(resolutionNode, 0.0f);
                            } else {
                                resolutionNode.setType(2);
                                resolutionNode3.setType(2);
                                resolutionNode.setOpposite(resolutionNode3, (float) (-width));
                                resolutionNode3.setOpposite(resolutionNode, (float) width);
                                constraintWidget2.setWidth(width);
                            }
                        }
                    } else if (obj != null) {
                        resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                    } else {
                        resolutionNode.dependsOn(resolutionNode3, -width);
                    }
                } else if (obj != null) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionNode3.dependsOn(resolutionNode, width);
                }
            }
        }
        Object obj3 = (constraintWidget2.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget2, 1)) ? 1 : null;
        if (resolutionNode2.type != 4 && resolutionNode4.type != 4) {
            if (constraintWidget2.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED || (obj3 != null && constraintWidget.getVisibility() == 8)) {
                if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null) {
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (obj != null) {
                        resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaseline.mTarget != null) {
                        constraintWidget2.mBaseline.getResolutionNode().setType(1);
                        resolutionNode2.dependsOn(1, constraintWidget2.mBaseline.getResolutionNode(), -constraintWidget2.mBaselineDistance);
                    }
                } else if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget == null) {
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (obj != null) {
                        resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaselineDistance > 0) {
                        constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                    }
                } else if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget != null) {
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (obj != null) {
                        resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode2.dependsOn(resolutionNode4, -constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaselineDistance > 0) {
                        constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                    }
                } else if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget != null) {
                    resolutionNode2.setType(2);
                    resolutionNode4.setType(2);
                    if (obj != null) {
                        resolutionNode2.setOpposite(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                        resolutionNode4.setOpposite(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                        constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                        constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                    } else {
                        resolutionNode2.setOpposite(resolutionNode4, (float) (-constraintWidget.getHeight()));
                        resolutionNode4.setOpposite(resolutionNode2, (float) constraintWidget.getHeight());
                    }
                    if (constraintWidget2.mBaselineDistance > 0) {
                        constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                    }
                }
            } else if (obj3 != null) {
                int height = constraintWidget.getHeight();
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null) {
                    if (obj != null) {
                        resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode4.dependsOn(resolutionNode2, height);
                    }
                } else if (constraintWidget2.mTop.mTarget == null || constraintWidget2.mBottom.mTarget != null) {
                    if (constraintWidget2.mTop.mTarget != null || constraintWidget2.mBottom.mTarget == null) {
                        if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget != null) {
                            if (obj != null) {
                                constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                                constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                            }
                            if (constraintWidget2.mDimensionRatio == 0.0f) {
                                resolutionNode2.setType(3);
                                resolutionNode4.setType(3);
                                resolutionNode2.setOpposite(resolutionNode4, 0.0f);
                                resolutionNode4.setOpposite(resolutionNode2, 0.0f);
                                return;
                            }
                            resolutionNode2.setType(2);
                            resolutionNode4.setType(2);
                            resolutionNode2.setOpposite(resolutionNode4, (float) (-height));
                            resolutionNode4.setOpposite(resolutionNode2, (float) height);
                            constraintWidget2.setHeight(height);
                            if (constraintWidget2.mBaselineDistance > 0) {
                                constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                            }
                        }
                    } else if (obj != null) {
                        resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                    } else {
                        resolutionNode2.dependsOn(resolutionNode4, -height);
                    }
                } else if (obj != null) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionNode4.dependsOn(resolutionNode2, height);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:68:0x00ef  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00e9  */
    /* JADX WARNING: Missing block: B:11:0x0030, code skipped:
            if (r6.mHorizontalChainStyle == 2) goto L_0x0032;
     */
    /* JADX WARNING: Missing block: B:13:0x0034, code skipped:
            r1 = null;
     */
    /* JADX WARNING: Missing block: B:23:0x0046, code skipped:
            if (r6.mVerticalChainStyle == 2) goto L_0x0032;
     */
    static boolean applyChainOptimized(android.support.constraint.solver.widgets.ConstraintWidgetContainer r20, android.support.constraint.solver.LinearSystem r21, int r22, int r23, android.support.constraint.solver.widgets.ChainHead r24) {
        /*
        r0 = r21;
        r1 = r24;
        r2 = r1.mFirst;
        r3 = r1.mLast;
        r4 = r1.mFirstVisibleWidget;
        r5 = r1.mLastVisibleWidget;
        r6 = r1.mHead;
        r7 = r1.mTotalWeight;
        r8 = r1.mFirstMatchConstraintWidget;
        r1 = r1.mLastMatchConstraintWidget;
        r8 = r20;
        r1 = r8.mListDimensionBehaviors;
        r1 = r1[r22];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        r1 = 2;
        r9 = 1;
        if (r22 != 0) goto L_0x0036;
    L_0x0020:
        r10 = r6.mHorizontalChainStyle;
        if (r10 != 0) goto L_0x0026;
    L_0x0024:
        r10 = 1;
        goto L_0x0027;
    L_0x0026:
        r10 = 0;
    L_0x0027:
        r11 = r6.mHorizontalChainStyle;
        if (r11 != r9) goto L_0x002d;
    L_0x002b:
        r11 = 1;
        goto L_0x002e;
    L_0x002d:
        r11 = 0;
    L_0x002e:
        r6 = r6.mHorizontalChainStyle;
        if (r6 != r1) goto L_0x0034;
    L_0x0032:
        r1 = 1;
        goto L_0x0049;
    L_0x0034:
        r1 = 0;
        goto L_0x0049;
    L_0x0036:
        r10 = r6.mVerticalChainStyle;
        if (r10 != 0) goto L_0x003c;
    L_0x003a:
        r10 = 1;
        goto L_0x003d;
    L_0x003c:
        r10 = 0;
    L_0x003d:
        r11 = r6.mVerticalChainStyle;
        if (r11 != r9) goto L_0x0043;
    L_0x0041:
        r11 = 1;
        goto L_0x0044;
    L_0x0043:
        r11 = 0;
    L_0x0044:
        r6 = r6.mVerticalChainStyle;
        if (r6 != r1) goto L_0x0034;
    L_0x0048:
        goto L_0x0032;
    L_0x0049:
        r13 = r2;
        r6 = 0;
        r9 = 0;
        r12 = 0;
        r14 = 0;
        r15 = 0;
    L_0x004f:
        if (r12 != 0) goto L_0x00f2;
    L_0x0051:
        r8 = r13.getVisibility();
        r16 = r12;
        r12 = 8;
        if (r8 == r12) goto L_0x008e;
    L_0x005b:
        r6 = r6 + 1;
        if (r22 != 0) goto L_0x0066;
    L_0x005f:
        r8 = r13.getWidth();
        r8 = (float) r8;
        r14 = r14 + r8;
        goto L_0x006c;
    L_0x0066:
        r8 = r13.getHeight();
        r8 = (float) r8;
        r14 = r14 + r8;
    L_0x006c:
        if (r13 == r4) goto L_0x0078;
    L_0x006e:
        r8 = r13.mListAnchors;
        r8 = r8[r23];
        r8 = r8.getMargin();
        r8 = (float) r8;
        r14 = r14 + r8;
    L_0x0078:
        r8 = r13.mListAnchors;
        r8 = r8[r23];
        r8 = r8.getMargin();
        r8 = (float) r8;
        r15 = r15 + r8;
        r8 = r13.mListAnchors;
        r17 = r23 + 1;
        r8 = r8[r17];
        r8 = r8.getMargin();
        r8 = (float) r8;
        r15 = r15 + r8;
    L_0x008e:
        r8 = r13.mListAnchors;
        r8 = r8[r23];
        r8 = r13.getVisibility();
        if (r8 == r12) goto L_0x00c3;
    L_0x0098:
        r8 = r13.mListDimensionBehaviors;
        r8 = r8[r22];
        r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r8 != r12) goto L_0x00c3;
    L_0x00a0:
        r9 = r9 + 1;
        if (r22 != 0) goto L_0x00b4;
    L_0x00a4:
        r8 = r13.mMatchConstraintDefaultWidth;
        if (r8 == 0) goto L_0x00aa;
    L_0x00a8:
        r8 = 0;
        return r8;
    L_0x00aa:
        r8 = 0;
        r12 = r13.mMatchConstraintMinWidth;
        if (r12 != 0) goto L_0x00b3;
    L_0x00af:
        r12 = r13.mMatchConstraintMaxWidth;
        if (r12 == 0) goto L_0x00c3;
    L_0x00b3:
        return r8;
    L_0x00b4:
        r8 = 0;
        r12 = r13.mMatchConstraintDefaultHeight;
        if (r12 == 0) goto L_0x00ba;
    L_0x00b9:
        return r8;
    L_0x00ba:
        r12 = r13.mMatchConstraintMinHeight;
        if (r12 != 0) goto L_0x00c2;
    L_0x00be:
        r12 = r13.mMatchConstraintMaxHeight;
        if (r12 == 0) goto L_0x00c3;
    L_0x00c2:
        return r8;
    L_0x00c3:
        r8 = r13.mListAnchors;
        r12 = r23 + 1;
        r8 = r8[r12];
        r8 = r8.mTarget;
        if (r8 == 0) goto L_0x00e5;
    L_0x00cd:
        r8 = r8.mOwner;
        r12 = r8.mListAnchors;
        r12 = r12[r23];
        r12 = r12.mTarget;
        if (r12 == 0) goto L_0x00e5;
    L_0x00d7:
        r12 = r8.mListAnchors;
        r12 = r12[r23];
        r12 = r12.mTarget;
        r12 = r12.mOwner;
        if (r12 == r13) goto L_0x00e2;
    L_0x00e1:
        goto L_0x00e5;
    L_0x00e2:
        r18 = r8;
        goto L_0x00e7;
    L_0x00e5:
        r18 = 0;
    L_0x00e7:
        if (r18 == 0) goto L_0x00ef;
    L_0x00e9:
        r12 = r16;
        r13 = r18;
        goto L_0x004f;
    L_0x00ef:
        r12 = 1;
        goto L_0x004f;
    L_0x00f2:
        r8 = r2.mListAnchors;
        r8 = r8[r23];
        r8 = r8.getResolutionNode();
        r3 = r3.mListAnchors;
        r12 = r23 + 1;
        r3 = r3[r12];
        r3 = r3.getResolutionNode();
        r19 = r2;
        r2 = r8.target;
        if (r2 == 0) goto L_0x0352;
    L_0x010a:
        r2 = r3.target;
        if (r2 != 0) goto L_0x0110;
    L_0x010e:
        goto L_0x0352;
    L_0x0110:
        r2 = r8.target;
        r2 = r2.state;
        r0 = 1;
        if (r2 == r0) goto L_0x011f;
    L_0x0117:
        r2 = r3.target;
        r2 = r2.state;
        if (r2 == r0) goto L_0x011f;
    L_0x011d:
        r0 = 0;
        return r0;
    L_0x011f:
        r0 = 0;
        if (r9 <= 0) goto L_0x0125;
    L_0x0122:
        if (r9 == r6) goto L_0x0125;
    L_0x0124:
        return r0;
    L_0x0125:
        if (r1 != 0) goto L_0x012e;
    L_0x0127:
        if (r10 != 0) goto L_0x012e;
    L_0x0129:
        if (r11 == 0) goto L_0x012c;
    L_0x012b:
        goto L_0x012e;
    L_0x012c:
        r0 = 0;
        goto L_0x0147;
    L_0x012e:
        if (r4 == 0) goto L_0x013a;
    L_0x0130:
        r0 = r4.mListAnchors;
        r0 = r0[r23];
        r0 = r0.getMargin();
        r0 = (float) r0;
        goto L_0x013b;
    L_0x013a:
        r0 = 0;
    L_0x013b:
        if (r5 == 0) goto L_0x0147;
    L_0x013d:
        r2 = r5.mListAnchors;
        r2 = r2[r12];
        r2 = r2.getMargin();
        r2 = (float) r2;
        r0 = r0 + r2;
    L_0x0147:
        r2 = r8.target;
        r2 = r2.resolvedOffset;
        r3 = r3.target;
        r3 = r3.resolvedOffset;
        r16 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r16 >= 0) goto L_0x0156;
    L_0x0153:
        r3 = r3 - r2;
        r3 = r3 - r14;
        goto L_0x0159;
    L_0x0156:
        r3 = r2 - r3;
        r3 = r3 - r14;
    L_0x0159:
        r16 = 1;
        if (r9 <= 0) goto L_0x021b;
    L_0x015d:
        if (r9 != r6) goto L_0x021b;
    L_0x015f:
        r1 = r13.getParent();
        if (r1 == 0) goto L_0x0173;
    L_0x0165:
        r1 = r13.getParent();
        r1 = r1.mListDimensionBehaviors;
        r1 = r1[r22];
        r6 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r1 != r6) goto L_0x0173;
    L_0x0171:
        r1 = 0;
        return r1;
    L_0x0173:
        r3 = r3 + r14;
        r3 = r3 - r15;
        if (r10 == 0) goto L_0x0179;
    L_0x0177:
        r15 = r15 - r0;
        r3 = r3 - r15;
    L_0x0179:
        if (r10 == 0) goto L_0x0195;
    L_0x017b:
        r0 = r4.mListAnchors;
        r0 = r0[r12];
        r0 = r0.getMargin();
        r0 = (float) r0;
        r2 = r2 + r0;
        r0 = r4.mListNextVisibleWidget;
        r0 = r0[r22];
        if (r0 == 0) goto L_0x0195;
    L_0x018b:
        r0 = r0.mListAnchors;
        r0 = r0[r23];
        r0 = r0.getMargin();
        r0 = (float) r0;
        r2 = r2 + r0;
    L_0x0195:
        if (r4 == 0) goto L_0x0219;
    L_0x0197:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        if (r0 == 0) goto L_0x01b3;
    L_0x019b:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r10 = r0.nonresolvedWidgets;
        r10 = r10 - r16;
        r0.nonresolvedWidgets = r10;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r10 = r0.resolvedWidgets;
        r10 = r10 + r16;
        r0.resolvedWidgets = r10;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r10 = r0.chainConnectionResolved;
        r10 = r10 + r16;
        r0.chainConnectionResolved = r10;
    L_0x01b3:
        r0 = r4.mListNextVisibleWidget;
        r0 = r0[r22];
        if (r0 != 0) goto L_0x01c0;
    L_0x01b9:
        if (r4 != r5) goto L_0x01bc;
    L_0x01bb:
        goto L_0x01c0;
    L_0x01bc:
        r6 = 0;
        r13 = r21;
        goto L_0x0216;
    L_0x01c0:
        r1 = (float) r9;
        r1 = r3 / r1;
        r6 = 0;
        r10 = (r7 > r6 ? 1 : (r7 == r6 ? 0 : -1));
        if (r10 <= 0) goto L_0x01cf;
    L_0x01c8:
        r1 = r4.mWeight;
        r1 = r1[r22];
        r1 = r1 * r3;
        r1 = r1 / r7;
    L_0x01cf:
        r10 = r4.mListAnchors;
        r10 = r10[r23];
        r10 = r10.getMargin();
        r10 = (float) r10;
        r2 = r2 + r10;
        r10 = r4.mListAnchors;
        r10 = r10[r23];
        r10 = r10.getResolutionNode();
        r11 = r8.resolvedTarget;
        r10.resolve(r11, r2);
        r10 = r4.mListAnchors;
        r10 = r10[r12];
        r10 = r10.getResolutionNode();
        r11 = r8.resolvedTarget;
        r2 = r2 + r1;
        r10.resolve(r11, r2);
        r1 = r4.mListAnchors;
        r1 = r1[r23];
        r1 = r1.getResolutionNode();
        r13 = r21;
        r1.addResolvedValue(r13);
        r1 = r4.mListAnchors;
        r1 = r1[r12];
        r1 = r1.getResolutionNode();
        r1.addResolvedValue(r13);
        r1 = r4.mListAnchors;
        r1 = r1[r12];
        r1 = r1.getMargin();
        r1 = (float) r1;
        r2 = r2 + r1;
    L_0x0216:
        r4 = r0;
        goto L_0x0195;
    L_0x0219:
        r0 = 1;
        return r0;
    L_0x021b:
        r13 = r21;
        r7 = (r3 > r14 ? 1 : (r3 == r14 ? 0 : -1));
        if (r7 >= 0) goto L_0x0223;
    L_0x0221:
        r7 = 0;
        return r7;
    L_0x0223:
        if (r1 == 0) goto L_0x02aa;
    L_0x0225:
        r3 = r3 - r0;
        r0 = r19.getHorizontalBiasPercent();
        r3 = r3 * r0;
        r2 = r2 + r3;
    L_0x022d:
        if (r4 == 0) goto L_0x02a7;
    L_0x022f:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        if (r0 == 0) goto L_0x024b;
    L_0x0233:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r0.nonresolvedWidgets;
        r6 = r6 - r16;
        r0.nonresolvedWidgets = r6;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r0.resolvedWidgets;
        r6 = r6 + r16;
        r0.resolvedWidgets = r6;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r0.chainConnectionResolved;
        r6 = r6 + r16;
        r0.chainConnectionResolved = r6;
    L_0x024b:
        r0 = r4.mListNextVisibleWidget;
        r0 = r0[r22];
        if (r0 != 0) goto L_0x0253;
    L_0x0251:
        if (r4 != r5) goto L_0x02a5;
    L_0x0253:
        if (r22 != 0) goto L_0x025b;
    L_0x0255:
        r1 = r4.getWidth();
        r1 = (float) r1;
        goto L_0x0260;
    L_0x025b:
        r1 = r4.getHeight();
        r1 = (float) r1;
    L_0x0260:
        r3 = r4.mListAnchors;
        r3 = r3[r23];
        r3 = r3.getMargin();
        r3 = (float) r3;
        r2 = r2 + r3;
        r3 = r4.mListAnchors;
        r3 = r3[r23];
        r3 = r3.getResolutionNode();
        r6 = r8.resolvedTarget;
        r3.resolve(r6, r2);
        r3 = r4.mListAnchors;
        r3 = r3[r12];
        r3 = r3.getResolutionNode();
        r6 = r8.resolvedTarget;
        r2 = r2 + r1;
        r3.resolve(r6, r2);
        r1 = r4.mListAnchors;
        r1 = r1[r23];
        r1 = r1.getResolutionNode();
        r1.addResolvedValue(r13);
        r1 = r4.mListAnchors;
        r1 = r1[r12];
        r1 = r1.getResolutionNode();
        r1.addResolvedValue(r13);
        r1 = r4.mListAnchors;
        r1 = r1[r12];
        r1 = r1.getMargin();
        r1 = (float) r1;
        r2 = r2 + r1;
    L_0x02a5:
        r4 = r0;
        goto L_0x022d;
    L_0x02a7:
        r0 = 1;
        goto L_0x0351;
    L_0x02aa:
        if (r10 != 0) goto L_0x02ae;
    L_0x02ac:
        if (r11 == 0) goto L_0x02a7;
    L_0x02ae:
        if (r10 == 0) goto L_0x02b2;
    L_0x02b0:
        r3 = r3 - r0;
        goto L_0x02b5;
    L_0x02b2:
        if (r11 == 0) goto L_0x02b5;
    L_0x02b4:
        r3 = r3 - r0;
    L_0x02b5:
        r0 = r6 + 1;
        r0 = (float) r0;
        r0 = r3 / r0;
        if (r11 == 0) goto L_0x02c9;
    L_0x02bc:
        r1 = 1;
        if (r6 <= r1) goto L_0x02c5;
    L_0x02bf:
        r0 = r6 + -1;
        r0 = (float) r0;
        r0 = r3 / r0;
        goto L_0x02c9;
    L_0x02c5:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r3 / r0;
    L_0x02c9:
        r1 = r2 + r0;
        if (r11 == 0) goto L_0x02da;
    L_0x02cd:
        r3 = 1;
        if (r6 <= r3) goto L_0x02da;
    L_0x02d0:
        r1 = r4.mListAnchors;
        r1 = r1[r23];
        r1 = r1.getMargin();
        r1 = (float) r1;
        r1 = r1 + r2;
    L_0x02da:
        if (r10 == 0) goto L_0x02e8;
    L_0x02dc:
        if (r4 == 0) goto L_0x02e8;
    L_0x02de:
        r2 = r4.mListAnchors;
        r2 = r2[r23];
        r2 = r2.getMargin();
        r2 = (float) r2;
        r1 = r1 + r2;
    L_0x02e8:
        if (r4 == 0) goto L_0x02a7;
    L_0x02ea:
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        if (r2 == 0) goto L_0x0306;
    L_0x02ee:
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r2.nonresolvedWidgets;
        r6 = r6 - r16;
        r2.nonresolvedWidgets = r6;
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r2.resolvedWidgets;
        r6 = r6 + r16;
        r2.resolvedWidgets = r6;
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r2.chainConnectionResolved;
        r6 = r6 + r16;
        r2.chainConnectionResolved = r6;
    L_0x0306:
        r2 = r4.mListNextVisibleWidget;
        r2 = r2[r22];
        if (r2 != 0) goto L_0x030e;
    L_0x030c:
        if (r4 != r5) goto L_0x034f;
    L_0x030e:
        if (r22 != 0) goto L_0x0316;
    L_0x0310:
        r3 = r4.getWidth();
        r3 = (float) r3;
        goto L_0x031b;
    L_0x0316:
        r3 = r4.getHeight();
        r3 = (float) r3;
    L_0x031b:
        r6 = r4.mListAnchors;
        r6 = r6[r23];
        r6 = r6.getResolutionNode();
        r7 = r8.resolvedTarget;
        r6.resolve(r7, r1);
        r6 = r4.mListAnchors;
        r6 = r6[r12];
        r6 = r6.getResolutionNode();
        r7 = r8.resolvedTarget;
        r9 = r1 + r3;
        r6.resolve(r7, r9);
        r6 = r4.mListAnchors;
        r6 = r6[r23];
        r6 = r6.getResolutionNode();
        r6.addResolvedValue(r13);
        r4 = r4.mListAnchors;
        r4 = r4[r12];
        r4 = r4.getResolutionNode();
        r4.addResolvedValue(r13);
        r3 = r3 + r0;
        r1 = r1 + r3;
    L_0x034f:
        r4 = r2;
        goto L_0x02e8;
    L_0x0351:
        return r0;
    L_0x0352:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Optimizer.applyChainOptimized(android.support.constraint.solver.widgets.ConstraintWidgetContainer, android.support.constraint.solver.LinearSystem, int, int, android.support.constraint.solver.widgets.ChainHead):boolean");
    }
}
