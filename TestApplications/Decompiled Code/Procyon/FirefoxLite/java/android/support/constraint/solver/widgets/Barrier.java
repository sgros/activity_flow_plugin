// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class Barrier extends Helper
{
    private boolean mAllowsGoneWidget;
    private int mBarrierType;
    private ArrayList<ResolutionAnchor> mNodes;
    
    public Barrier() {
        this.mBarrierType = 0;
        this.mNodes = new ArrayList<ResolutionAnchor>(4);
        this.mAllowsGoneWidget = true;
    }
    
    @Override
    public void addToSolver(final LinearSystem linearSystem) {
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (int i = 0; i < this.mListAnchors.length; ++i) {
            this.mListAnchors[i].mSolverVariable = linearSystem.createObjectVariable(this.mListAnchors[i]);
        }
        if (this.mBarrierType >= 0 && this.mBarrierType < 4) {
            final ConstraintAnchor constraintAnchor = this.mListAnchors[this.mBarrierType];
            while (true) {
                for (int j = 0; j < this.mWidgetsCount; ++j) {
                    final ConstraintWidget constraintWidget = this.mWidgets[j];
                    if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                        if (((this.mBarrierType == 0 || this.mBarrierType == 1) && constraintWidget.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) || ((this.mBarrierType == 2 || this.mBarrierType == 3) && constraintWidget.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)) {
                            boolean b = true;
                            Label_0258: {
                                if (this.mBarrierType != 0 && this.mBarrierType != 1) {
                                    if (this.getParent().getVerticalDimensionBehaviour() != DimensionBehaviour.WRAP_CONTENT) {
                                        break Label_0258;
                                    }
                                }
                                else if (this.getParent().getHorizontalDimensionBehaviour() != DimensionBehaviour.WRAP_CONTENT) {
                                    break Label_0258;
                                }
                                b = false;
                            }
                            for (int k = 0; k < this.mWidgetsCount; ++k) {
                                final ConstraintWidget constraintWidget2 = this.mWidgets[k];
                                if (this.mAllowsGoneWidget || constraintWidget2.allowedInBarrier()) {
                                    final SolverVariable objectVariable = linearSystem.createObjectVariable(constraintWidget2.mListAnchors[this.mBarrierType]);
                                    constraintWidget2.mListAnchors[this.mBarrierType].mSolverVariable = objectVariable;
                                    if (this.mBarrierType != 0 && this.mBarrierType != 2) {
                                        linearSystem.addGreaterBarrier(constraintAnchor.mSolverVariable, objectVariable, b);
                                    }
                                    else {
                                        linearSystem.addLowerBarrier(constraintAnchor.mSolverVariable, objectVariable, b);
                                    }
                                }
                            }
                            if (this.mBarrierType == 0) {
                                linearSystem.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 6);
                                if (!b) {
                                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 5);
                                }
                            }
                            else if (this.mBarrierType == 1) {
                                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 6);
                                if (!b) {
                                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 5);
                                }
                            }
                            else if (this.mBarrierType == 2) {
                                linearSystem.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 6);
                                if (!b) {
                                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 5);
                                }
                            }
                            else if (this.mBarrierType == 3) {
                                linearSystem.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 6);
                                if (!b) {
                                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 5);
                                }
                            }
                            return;
                        }
                    }
                }
                boolean b = false;
                continue;
            }
        }
    }
    
    @Override
    public boolean allowedInBarrier() {
        return true;
    }
    
    @Override
    public void analyze(int i) {
        if (this.mParent == null) {
            return;
        }
        if (!((ConstraintWidgetContainer)this.mParent).optimizeFor(2)) {
            return;
        }
        ResolutionAnchor resolutionAnchor = null;
        switch (this.mBarrierType) {
            default: {
                return;
            }
            case 3: {
                resolutionAnchor = this.mBottom.getResolutionNode();
                break;
            }
            case 2: {
                resolutionAnchor = this.mTop.getResolutionNode();
                break;
            }
            case 1: {
                resolutionAnchor = this.mRight.getResolutionNode();
                break;
            }
            case 0: {
                resolutionAnchor = this.mLeft.getResolutionNode();
                break;
            }
        }
        resolutionAnchor.setType(5);
        if (this.mBarrierType != 0 && this.mBarrierType != 1) {
            this.mLeft.getResolutionNode().resolve(null, 0.0f);
            this.mRight.getResolutionNode().resolve(null, 0.0f);
        }
        else {
            this.mTop.getResolutionNode().resolve(null, 0.0f);
            this.mBottom.getResolutionNode().resolve(null, 0.0f);
        }
        this.mNodes.clear();
        ConstraintWidget constraintWidget;
        ResolutionAnchor e = null;
        for (i = 0; i < this.mWidgetsCount; ++i) {
            constraintWidget = this.mWidgets[i];
            if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                switch (this.mBarrierType) {
                    default: {
                        e = null;
                        break;
                    }
                    case 3: {
                        e = constraintWidget.mBottom.getResolutionNode();
                        break;
                    }
                    case 2: {
                        e = constraintWidget.mTop.getResolutionNode();
                        break;
                    }
                    case 1: {
                        e = constraintWidget.mRight.getResolutionNode();
                        break;
                    }
                    case 0: {
                        e = constraintWidget.mLeft.getResolutionNode();
                        break;
                    }
                }
                if (e != null) {
                    this.mNodes.add(e);
                    e.addDependent(resolutionAnchor);
                }
            }
        }
    }
    
    @Override
    public void resetResolutionNodes() {
        super.resetResolutionNodes();
        this.mNodes.clear();
    }
    
    @Override
    public void resolve() {
        final int mBarrierType = this.mBarrierType;
        float n = Float.MAX_VALUE;
        ResolutionAnchor resolutionAnchor = null;
        Label_0084: {
            switch (mBarrierType) {
                default: {
                    return;
                }
                case 3: {
                    resolutionAnchor = this.mBottom.getResolutionNode();
                    break;
                }
                case 2: {
                    resolutionAnchor = this.mTop.getResolutionNode();
                    break Label_0084;
                }
                case 1: {
                    resolutionAnchor = this.mRight.getResolutionNode();
                    break;
                }
                case 0: {
                    resolutionAnchor = this.mLeft.getResolutionNode();
                    break Label_0084;
                }
            }
            n = 0.0f;
        }
        final int size = this.mNodes.size();
        ResolutionAnchor resolvedTarget = null;
        int i = 0;
        float resolvedOffset = n;
        while (i < size) {
            final ResolutionAnchor resolutionAnchor2 = this.mNodes.get(i);
            if (resolutionAnchor2.state != 1) {
                return;
            }
            float n2;
            if (this.mBarrierType != 0 && this.mBarrierType != 2) {
                n2 = resolvedOffset;
                if (resolutionAnchor2.resolvedOffset > resolvedOffset) {
                    n2 = resolutionAnchor2.resolvedOffset;
                    resolvedTarget = resolutionAnchor2.resolvedTarget;
                }
            }
            else {
                n2 = resolvedOffset;
                if (resolutionAnchor2.resolvedOffset < resolvedOffset) {
                    n2 = resolutionAnchor2.resolvedOffset;
                    resolvedTarget = resolutionAnchor2.resolvedTarget;
                }
            }
            ++i;
            resolvedOffset = n2;
        }
        if (LinearSystem.getMetrics() != null) {
            final Metrics metrics = LinearSystem.getMetrics();
            ++metrics.barrierConnectionResolved;
        }
        resolutionAnchor.resolvedTarget = resolvedTarget;
        resolutionAnchor.resolvedOffset = resolvedOffset;
        resolutionAnchor.didResolve();
        switch (this.mBarrierType) {
            default: {}
            case 3: {
                this.mTop.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
                break;
            }
            case 2: {
                this.mBottom.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
                break;
            }
            case 1: {
                this.mLeft.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
                break;
            }
            case 0: {
                this.mRight.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
                break;
            }
        }
    }
    
    public void setAllowsGoneWidget(final boolean mAllowsGoneWidget) {
        this.mAllowsGoneWidget = mAllowsGoneWidget;
    }
    
    public void setBarrierType(final int mBarrierType) {
        this.mBarrierType = mBarrierType;
    }
}
