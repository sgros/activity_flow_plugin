// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.LinearSystem;

public class Optimizer
{
    static boolean[] flags;
    
    static {
        Optimizer.flags = new boolean[3];
    }
    
    static void analyze(int n, final ConstraintWidget constraintWidget) {
        constraintWidget.updateResolutionNodes();
        final ResolutionAnchor resolutionNode = constraintWidget.mLeft.getResolutionNode();
        final ResolutionAnchor resolutionNode2 = constraintWidget.mTop.getResolutionNode();
        final ResolutionAnchor resolutionNode3 = constraintWidget.mRight.getResolutionNode();
        final ResolutionAnchor resolutionNode4 = constraintWidget.mBottom.getResolutionNode();
        if ((n & 0x8) == 0x8) {
            n = 1;
        }
        else {
            n = 0;
        }
        final boolean b = constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget, 0);
        if (resolutionNode.type != 4 && resolutionNode3.type != 4) {
            if (constraintWidget.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED && (!b || constraintWidget.getVisibility() != 8)) {
                if (b) {
                    final int width = constraintWidget.getWidth();
                    resolutionNode.setType(1);
                    resolutionNode3.setType(1);
                    if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) {
                        if (n != 0) {
                            resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                        }
                        else {
                            resolutionNode3.dependsOn(resolutionNode, width);
                        }
                    }
                    else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget == null) {
                        if (n != 0) {
                            resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                        }
                        else {
                            resolutionNode3.dependsOn(resolutionNode, width);
                        }
                    }
                    else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget != null) {
                        if (n != 0) {
                            resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                        }
                        else {
                            resolutionNode.dependsOn(resolutionNode3, -width);
                        }
                    }
                    else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                        if (n != 0) {
                            constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                            constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                        }
                        if (constraintWidget.mDimensionRatio == 0.0f) {
                            resolutionNode.setType(3);
                            resolutionNode3.setType(3);
                            resolutionNode.setOpposite(resolutionNode3, 0.0f);
                            resolutionNode3.setOpposite(resolutionNode, 0.0f);
                        }
                        else {
                            resolutionNode.setType(2);
                            resolutionNode3.setType(2);
                            resolutionNode.setOpposite(resolutionNode3, (float)(-width));
                            resolutionNode3.setOpposite(resolutionNode, (float)width);
                            constraintWidget.setWidth(width);
                        }
                    }
                }
            }
            else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (n != 0) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                }
            }
            else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget == null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (n != 0) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                }
            }
            else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget != null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                if (n != 0) {
                    resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                }
            }
            else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                resolutionNode.setType(2);
                resolutionNode3.setType(2);
                if (n != 0) {
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                    resolutionNode.setOpposite(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                    resolutionNode3.setOpposite(resolutionNode, 1, constraintWidget.getResolutionWidth());
                }
                else {
                    resolutionNode.setOpposite(resolutionNode3, (float)(-constraintWidget.getWidth()));
                    resolutionNode3.setOpposite(resolutionNode, (float)constraintWidget.getWidth());
                }
            }
        }
        final boolean b2 = constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget, 1);
        if (resolutionNode2.type != 4 && resolutionNode4.type != 4) {
            if (constraintWidget.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED && (!b2 || constraintWidget.getVisibility() != 8)) {
                if (b2) {
                    final int height = constraintWidget.getHeight();
                    resolutionNode2.setType(1);
                    resolutionNode4.setType(1);
                    if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                        if (n != 0) {
                            resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                        }
                        else {
                            resolutionNode4.dependsOn(resolutionNode2, height);
                        }
                    }
                    else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget == null) {
                        if (n != 0) {
                            resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                        }
                        else {
                            resolutionNode4.dependsOn(resolutionNode2, height);
                        }
                    }
                    else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget != null) {
                        if (n != 0) {
                            resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                        }
                        else {
                            resolutionNode2.dependsOn(resolutionNode4, -height);
                        }
                    }
                    else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                        if (n != 0) {
                            constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                            constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                        }
                        if (constraintWidget.mDimensionRatio == 0.0f) {
                            resolutionNode2.setType(3);
                            resolutionNode4.setType(3);
                            resolutionNode2.setOpposite(resolutionNode4, 0.0f);
                            resolutionNode4.setOpposite(resolutionNode2, 0.0f);
                        }
                        else {
                            resolutionNode2.setType(2);
                            resolutionNode4.setType(2);
                            resolutionNode2.setOpposite(resolutionNode4, (float)(-height));
                            resolutionNode4.setOpposite(resolutionNode2, (float)height);
                            constraintWidget.setHeight(height);
                            if (constraintWidget.mBaselineDistance > 0) {
                                constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                            }
                        }
                    }
                }
            }
            else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (n != 0) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                }
                else {
                    resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                }
                if (constraintWidget.mBaseline.mTarget != null) {
                    constraintWidget.mBaseline.getResolutionNode().setType(1);
                    resolutionNode2.dependsOn(1, constraintWidget.mBaseline.getResolutionNode(), -constraintWidget.mBaselineDistance);
                }
            }
            else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget == null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (n != 0) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                }
                else {
                    resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                }
            }
            else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget != null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (n != 0) {
                    resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                }
                else {
                    resolutionNode2.dependsOn(resolutionNode4, -constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                }
            }
            else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                resolutionNode2.setType(2);
                resolutionNode4.setType(2);
                if (n != 0) {
                    resolutionNode2.setOpposite(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                    resolutionNode4.setOpposite(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                }
                else {
                    resolutionNode2.setOpposite(resolutionNode4, (float)(-constraintWidget.getHeight()));
                    resolutionNode4.setOpposite(resolutionNode2, (float)constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget.mBaselineDistance);
                }
            }
        }
    }
    
    static boolean applyChainOptimized(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, final int n2, final ChainHead chainHead) {
        final ConstraintWidget mFirst = chainHead.mFirst;
        final ConstraintWidget mLast = chainHead.mLast;
        ConstraintWidget mFirstVisibleWidget = chainHead.mFirstVisibleWidget;
        final ConstraintWidget mLastVisibleWidget = chainHead.mLastVisibleWidget;
        final ConstraintWidget mHead = chainHead.mHead;
        final float mTotalWeight = chainHead.mTotalWeight;
        final ConstraintWidget mFirstMatchConstraintWidget = chainHead.mFirstMatchConstraintWidget;
        final ConstraintWidget mLastMatchConstraintWidget = chainHead.mLastMatchConstraintWidget;
        final ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidgetContainer.mListDimensionBehaviors[n];
        final ConstraintWidget.DimensionBehaviour wrap_CONTENT = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        int n4 = 0;
        int n5 = 0;
        boolean b = false;
        Label_0198: {
            int n6 = 0;
            Label_0133: {
                int n3;
                if (n == 0) {
                    if (mHead.mHorizontalChainStyle == 0) {
                        n3 = 1;
                    }
                    else {
                        n3 = 0;
                    }
                    if (mHead.mHorizontalChainStyle == 1) {
                        n4 = 1;
                    }
                    else {
                        n4 = 0;
                    }
                    n5 = n3;
                    n6 = n4;
                    if (mHead.mHorizontalChainStyle != 2) {
                        break Label_0133;
                    }
                }
                else {
                    if (mHead.mVerticalChainStyle == 0) {
                        n3 = 1;
                    }
                    else {
                        n3 = 0;
                    }
                    if (mHead.mVerticalChainStyle == 1) {
                        n4 = 1;
                    }
                    else {
                        n4 = 0;
                    }
                    n5 = n3;
                    n6 = n4;
                    if (mHead.mVerticalChainStyle != 2) {
                        break Label_0133;
                    }
                }
                b = true;
                n5 = n3;
                break Label_0198;
            }
            b = false;
            n4 = n6;
        }
        ConstraintWidget constraintWidget = mFirst;
        int n7 = 0;
        int n8 = 0;
        int i = 0;
        float n9 = 0.0f;
        float n10 = 0.0f;
        while (i == 0) {
            int n11 = n7;
            float n12 = n9;
            float n13 = n10;
            if (constraintWidget.getVisibility() != 8) {
                n11 = n7 + 1;
                float n14;
                if (n == 0) {
                    n14 = n9 + constraintWidget.getWidth();
                }
                else {
                    n14 = n9 + constraintWidget.getHeight();
                }
                n12 = n14;
                if (constraintWidget != mFirstVisibleWidget) {
                    n12 = n14 + constraintWidget.mListAnchors[n2].getMargin();
                }
                n13 = n10 + constraintWidget.mListAnchors[n2].getMargin() + constraintWidget.mListAnchors[n2 + 1].getMargin();
            }
            final ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[n2];
            int n15 = n8;
            if (constraintWidget.getVisibility() != 8) {
                n15 = n8;
                if (constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    n15 = n8 + 1;
                    if (n == 0) {
                        if (constraintWidget.mMatchConstraintDefaultWidth != 0) {
                            return false;
                        }
                        if (constraintWidget.mMatchConstraintMinWidth != 0 || constraintWidget.mMatchConstraintMaxWidth != 0) {
                            return false;
                        }
                    }
                    else {
                        if (constraintWidget.mMatchConstraintDefaultHeight != 0) {
                            return false;
                        }
                        if (constraintWidget.mMatchConstraintMinHeight != 0 || constraintWidget.mMatchConstraintMaxHeight != 0) {
                            return false;
                        }
                    }
                }
            }
            final ConstraintAnchor mTarget = constraintWidget.mListAnchors[n2 + 1].mTarget;
            ConstraintWidget mOwner = null;
            Label_0500: {
                if (mTarget != null) {
                    mOwner = mTarget.mOwner;
                    if (mOwner.mListAnchors[n2].mTarget != null) {
                        if (mOwner.mListAnchors[n2].mTarget.mOwner == constraintWidget) {
                            break Label_0500;
                        }
                    }
                }
                mOwner = null;
            }
            if (mOwner != null) {
                n7 = n11;
                n8 = n15;
                constraintWidget = mOwner;
                n9 = n12;
                n10 = n13;
            }
            else {
                i = 1;
                n7 = n11;
                n8 = n15;
                n9 = n12;
                n10 = n13;
            }
        }
        final ResolutionAnchor resolutionNode = mFirst.mListAnchors[n2].getResolutionNode();
        final ConstraintAnchor[] mListAnchors = mLast.mListAnchors;
        final int n16 = n2 + 1;
        final ResolutionAnchor resolutionNode2 = mListAnchors[n16].getResolutionNode();
        if (resolutionNode.target == null || resolutionNode2.target == null) {
            return false;
        }
        if (resolutionNode.target.state != 1 && resolutionNode2.target.state != 1) {
            return false;
        }
        if (n8 > 0 && n8 != n7) {
            return false;
        }
        float n17;
        if (!b && n5 == 0 && n4 == 0) {
            n17 = 0.0f;
        }
        else {
            float n18;
            if (mFirstVisibleWidget != null) {
                n18 = (float)mFirstVisibleWidget.mListAnchors[n2].getMargin();
            }
            else {
                n18 = 0.0f;
            }
            n17 = n18;
            if (mLastVisibleWidget != null) {
                n17 = n18 + mLastVisibleWidget.mListAnchors[n16].getMargin();
            }
        }
        final float resolvedOffset = resolutionNode.target.resolvedOffset;
        final float resolvedOffset2 = resolutionNode2.target.resolvedOffset;
        float n19;
        if (resolvedOffset < resolvedOffset2) {
            n19 = resolvedOffset2 - resolvedOffset - n9;
        }
        else {
            n19 = resolvedOffset - resolvedOffset2 - n9;
        }
        if (n8 > 0 && n8 == n7) {
            if (constraintWidget.getParent() != null && constraintWidget.getParent().mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                return false;
            }
            float n21;
            final float n20 = n21 = n19 + n9 - n10;
            if (n5 != 0) {
                n21 = n20 - (n10 - n17);
            }
            float n22 = resolvedOffset;
            ConstraintWidget constraintWidget2 = mFirstVisibleWidget;
            if (n5 != 0) {
                final float n23 = resolvedOffset + mFirstVisibleWidget.mListAnchors[n16].getMargin();
                final ConstraintWidget constraintWidget3 = mFirstVisibleWidget.mListNextVisibleWidget[n];
                n22 = n23;
                constraintWidget2 = mFirstVisibleWidget;
                if (constraintWidget3 != null) {
                    n22 = n23 + constraintWidget3.mListAnchors[n2].getMargin();
                    constraintWidget2 = mFirstVisibleWidget;
                }
            }
            while (constraintWidget2 != null) {
                if (LinearSystem.sMetrics != null) {
                    final Metrics sMetrics = LinearSystem.sMetrics;
                    --sMetrics.nonresolvedWidgets;
                    final Metrics sMetrics2 = LinearSystem.sMetrics;
                    ++sMetrics2.resolvedWidgets;
                    final Metrics sMetrics3 = LinearSystem.sMetrics;
                    ++sMetrics3.chainConnectionResolved;
                }
                final ConstraintWidget constraintWidget4 = constraintWidget2.mListNextVisibleWidget[n];
                if (constraintWidget4 != null || constraintWidget2 == mLastVisibleWidget) {
                    float n24 = n21 / n8;
                    if (mTotalWeight > 0.0f) {
                        n24 = constraintWidget2.mWeight[n] * n21 / mTotalWeight;
                    }
                    final float n25 = n22 + constraintWidget2.mListAnchors[n2].getMargin();
                    constraintWidget2.mListAnchors[n2].getResolutionNode().resolve(resolutionNode.resolvedTarget, n25);
                    final ResolutionAnchor resolutionNode3 = constraintWidget2.mListAnchors[n16].getResolutionNode();
                    final ResolutionAnchor resolvedTarget = resolutionNode.resolvedTarget;
                    final float n26 = n25 + n24;
                    resolutionNode3.resolve(resolvedTarget, n26);
                    constraintWidget2.mListAnchors[n2].getResolutionNode().addResolvedValue(linearSystem);
                    constraintWidget2.mListAnchors[n16].getResolutionNode().addResolvedValue(linearSystem);
                    n22 = n26 + constraintWidget2.mListAnchors[n16].getMargin();
                }
                constraintWidget2 = constraintWidget4;
            }
            return true;
        }
        else {
            if (n19 < n9) {
                return false;
            }
            if (b) {
                float n27 = resolvedOffset + (n19 - n17) * mFirst.getHorizontalBiasPercent();
                while (mFirstVisibleWidget != null) {
                    if (LinearSystem.sMetrics != null) {
                        final Metrics sMetrics4 = LinearSystem.sMetrics;
                        --sMetrics4.nonresolvedWidgets;
                        final Metrics sMetrics5 = LinearSystem.sMetrics;
                        ++sMetrics5.resolvedWidgets;
                        final Metrics sMetrics6 = LinearSystem.sMetrics;
                        ++sMetrics6.chainConnectionResolved;
                    }
                    final ConstraintWidget constraintWidget5 = mFirstVisibleWidget.mListNextVisibleWidget[n];
                    float n28 = 0.0f;
                    Label_1379: {
                        if (constraintWidget5 == null) {
                            n28 = n27;
                            if (mFirstVisibleWidget != mLastVisibleWidget) {
                                break Label_1379;
                            }
                        }
                        float n29;
                        if (n == 0) {
                            n29 = (float)mFirstVisibleWidget.getWidth();
                        }
                        else {
                            n29 = (float)mFirstVisibleWidget.getHeight();
                        }
                        final float n30 = n27 + mFirstVisibleWidget.mListAnchors[n2].getMargin();
                        mFirstVisibleWidget.mListAnchors[n2].getResolutionNode().resolve(resolutionNode.resolvedTarget, n30);
                        final ResolutionAnchor resolutionNode4 = mFirstVisibleWidget.mListAnchors[n16].getResolutionNode();
                        final ResolutionAnchor resolvedTarget2 = resolutionNode.resolvedTarget;
                        final float n31 = n30 + n29;
                        resolutionNode4.resolve(resolvedTarget2, n31);
                        mFirstVisibleWidget.mListAnchors[n2].getResolutionNode().addResolvedValue(linearSystem);
                        mFirstVisibleWidget.mListAnchors[n16].getResolutionNode().addResolvedValue(linearSystem);
                        n28 = n31 + mFirstVisibleWidget.mListAnchors[n16].getMargin();
                    }
                    mFirstVisibleWidget = constraintWidget5;
                    n27 = n28;
                }
            }
            else if (n5 != 0 || n4 != 0) {
                float n32;
                if (n5 != 0) {
                    n32 = n19 - n17;
                }
                else {
                    n32 = n19;
                    if (n4 != 0) {
                        n32 = n19 - n17;
                    }
                }
                float n33 = n32 / (n7 + 1);
                if (n4 != 0) {
                    if (n7 > 1) {
                        n33 = n32 / (n7 - 1);
                    }
                    else {
                        n33 = n32 / 2.0f;
                    }
                }
                float n35;
                final float n34 = n35 = resolvedOffset + n33;
                if (n4 != 0) {
                    n35 = n34;
                    if (n7 > 1) {
                        n35 = mFirstVisibleWidget.mListAnchors[n2].getMargin() + resolvedOffset;
                    }
                }
                float n36 = n35;
                ConstraintWidget constraintWidget6 = mFirstVisibleWidget;
                if (n5 != 0) {
                    n36 = n35;
                    if ((constraintWidget6 = mFirstVisibleWidget) != null) {
                        n36 = n35 + mFirstVisibleWidget.mListAnchors[n2].getMargin();
                        constraintWidget6 = mFirstVisibleWidget;
                    }
                }
                while (constraintWidget6 != null) {
                    if (LinearSystem.sMetrics != null) {
                        final Metrics sMetrics7 = LinearSystem.sMetrics;
                        --sMetrics7.nonresolvedWidgets;
                        final Metrics sMetrics8 = LinearSystem.sMetrics;
                        ++sMetrics8.resolvedWidgets;
                        final Metrics sMetrics9 = LinearSystem.sMetrics;
                        ++sMetrics9.chainConnectionResolved;
                    }
                    final ConstraintWidget constraintWidget7 = constraintWidget6.mListNextVisibleWidget[n];
                    float n37 = 0.0f;
                    Label_1742: {
                        if (constraintWidget7 == null) {
                            n37 = n36;
                            if (constraintWidget6 != mLastVisibleWidget) {
                                break Label_1742;
                            }
                        }
                        float n38;
                        if (n == 0) {
                            n38 = (float)constraintWidget6.getWidth();
                        }
                        else {
                            n38 = (float)constraintWidget6.getHeight();
                        }
                        constraintWidget6.mListAnchors[n2].getResolutionNode().resolve(resolutionNode.resolvedTarget, n36);
                        constraintWidget6.mListAnchors[n16].getResolutionNode().resolve(resolutionNode.resolvedTarget, n36 + n38);
                        constraintWidget6.mListAnchors[n2].getResolutionNode().addResolvedValue(linearSystem);
                        constraintWidget6.mListAnchors[n16].getResolutionNode().addResolvedValue(linearSystem);
                        n37 = n36 + (n38 + n33);
                    }
                    constraintWidget6 = constraintWidget7;
                    n36 = n37;
                }
            }
            return true;
        }
    }
    
    static void checkMatchParent(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int mMargin = constraintWidget.mLeft.mMargin;
            final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(mMargin, n);
        }
        if (constraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            final int mMargin2 = constraintWidget.mTop.mMargin;
            final int n2 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin2);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(mMargin2, n2);
        }
    }
    
    private static boolean optimizableMatchConstraint(final ConstraintWidget constraintWidget, int n) {
        if (constraintWidget.mListDimensionBehaviors[n] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            return false;
        }
        final float mDimensionRatio = constraintWidget.mDimensionRatio;
        final int n2 = 1;
        if (mDimensionRatio != 0.0f) {
            final ConstraintWidget.DimensionBehaviour[] mListDimensionBehaviors = constraintWidget.mListDimensionBehaviors;
            if (n == 0) {
                n = n2;
            }
            else {
                n = 0;
            }
            return mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && false;
        }
        if (n == 0) {
            if (constraintWidget.mMatchConstraintDefaultWidth != 0) {
                return false;
            }
            if (constraintWidget.mMatchConstraintMinWidth != 0 || constraintWidget.mMatchConstraintMaxWidth != 0) {
                return false;
            }
        }
        else {
            if (constraintWidget.mMatchConstraintDefaultHeight != 0) {
                return false;
            }
            if (constraintWidget.mMatchConstraintMinHeight != 0 || constraintWidget.mMatchConstraintMaxHeight != 0) {
                return false;
            }
        }
        return true;
    }
}
