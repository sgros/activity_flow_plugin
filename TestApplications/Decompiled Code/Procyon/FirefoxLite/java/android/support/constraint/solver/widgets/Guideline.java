// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;

public class Guideline extends ConstraintWidget
{
    private ConstraintAnchor mAnchor;
    private Rectangle mHead;
    private int mHeadSize;
    private boolean mIsPositionRelaxed;
    private int mMinimumPosition;
    private int mOrientation;
    protected int mRelativeBegin;
    protected int mRelativeEnd;
    protected float mRelativePercent;
    
    public Guideline() {
        this.mRelativePercent = -1.0f;
        this.mRelativeBegin = -1;
        this.mRelativeEnd = -1;
        this.mAnchor = this.mTop;
        int i = 0;
        this.mOrientation = 0;
        this.mIsPositionRelaxed = false;
        this.mMinimumPosition = 0;
        this.mHead = new Rectangle();
        this.mHeadSize = 8;
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
        while (i < this.mListAnchors.length) {
            this.mListAnchors[i] = this.mAnchor;
            ++i;
        }
    }
    
    @Override
    public void addToSolver(final LinearSystem linearSystem) {
        final ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)this.getParent();
        if (constraintWidgetContainer == null) {
            return;
        }
        ConstraintAnchor constraintAnchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor constraintAnchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
        boolean b = this.mParent != null && this.mParent.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
        if (this.mOrientation == 0) {
            constraintAnchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
            constraintAnchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
            b = (this.mParent != null && this.mParent.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT);
        }
        if (this.mRelativeBegin != -1) {
            final SolverVariable objectVariable = linearSystem.createObjectVariable(this.mAnchor);
            linearSystem.addEquality(objectVariable, linearSystem.createObjectVariable(constraintAnchor), this.mRelativeBegin, 6);
            if (b) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(constraintAnchor2), objectVariable, 0, 5);
            }
        }
        else if (this.mRelativeEnd != -1) {
            final SolverVariable objectVariable2 = linearSystem.createObjectVariable(this.mAnchor);
            final SolverVariable objectVariable3 = linearSystem.createObjectVariable(constraintAnchor2);
            linearSystem.addEquality(objectVariable2, objectVariable3, -this.mRelativeEnd, 6);
            if (b) {
                linearSystem.addGreaterThan(objectVariable2, linearSystem.createObjectVariable(constraintAnchor), 0, 5);
                linearSystem.addGreaterThan(objectVariable3, objectVariable2, 0, 5);
            }
        }
        else if (this.mRelativePercent != -1.0f) {
            linearSystem.addConstraint(LinearSystem.createRowDimensionPercent(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(constraintAnchor), linearSystem.createObjectVariable(constraintAnchor2), this.mRelativePercent, this.mIsPositionRelaxed));
        }
    }
    
    @Override
    public boolean allowedInBarrier() {
        return true;
    }
    
    @Override
    public void analyze(int n) {
        final ConstraintWidget parent = this.getParent();
        if (parent == null) {
            return;
        }
        if (this.getOrientation() == 1) {
            this.mTop.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), 0);
            this.mBottom.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), 0);
            if (this.mRelativeBegin != -1) {
                this.mLeft.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), this.mRelativeBegin);
                this.mRight.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), this.mRelativeBegin);
            }
            else if (this.mRelativeEnd != -1) {
                this.mLeft.getResolutionNode().dependsOn(1, parent.mRight.getResolutionNode(), -this.mRelativeEnd);
                this.mRight.getResolutionNode().dependsOn(1, parent.mRight.getResolutionNode(), -this.mRelativeEnd);
            }
            else if (this.mRelativePercent != -1.0f && parent.getHorizontalDimensionBehaviour() == DimensionBehaviour.FIXED) {
                n = (int)(parent.mWidth * this.mRelativePercent);
                this.mLeft.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), n);
                this.mRight.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), n);
            }
        }
        else {
            this.mLeft.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), 0);
            this.mRight.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), 0);
            if (this.mRelativeBegin != -1) {
                this.mTop.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), this.mRelativeBegin);
                this.mBottom.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), this.mRelativeBegin);
            }
            else if (this.mRelativeEnd != -1) {
                this.mTop.getResolutionNode().dependsOn(1, parent.mBottom.getResolutionNode(), -this.mRelativeEnd);
                this.mBottom.getResolutionNode().dependsOn(1, parent.mBottom.getResolutionNode(), -this.mRelativeEnd);
            }
            else if (this.mRelativePercent != -1.0f && parent.getVerticalDimensionBehaviour() == DimensionBehaviour.FIXED) {
                n = (int)(parent.mHeight * this.mRelativePercent);
                this.mTop.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), n);
                this.mBottom.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), n);
            }
        }
    }
    
    @Override
    public ConstraintAnchor getAnchor(final ConstraintAnchor.Type type) {
        switch (Guideline$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
                return null;
            }
            case 3:
            case 4: {
                if (this.mOrientation == 0) {
                    return this.mAnchor;
                }
                break;
            }
            case 1:
            case 2: {
                if (this.mOrientation == 1) {
                    return this.mAnchor;
                }
                break;
            }
        }
        throw new AssertionError((Object)type.name());
    }
    
    @Override
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public void setGuideBegin(final int mRelativeBegin) {
        if (mRelativeBegin > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = mRelativeBegin;
            this.mRelativeEnd = -1;
        }
    }
    
    public void setGuideEnd(final int mRelativeEnd) {
        if (mRelativeEnd > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = mRelativeEnd;
        }
    }
    
    public void setGuidePercent(final float mRelativePercent) {
        if (mRelativePercent > -1.0f) {
            this.mRelativePercent = mRelativePercent;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
        }
    }
    
    public void setOrientation(int i) {
        if (this.mOrientation == i) {
            return;
        }
        this.mOrientation = i;
        this.mAnchors.clear();
        if (this.mOrientation == 1) {
            this.mAnchor = this.mLeft;
        }
        else {
            this.mAnchor = this.mTop;
        }
        this.mAnchors.add(this.mAnchor);
        int length;
        for (length = this.mListAnchors.length, i = 0; i < length; ++i) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }
    
    @Override
    public void updateFromSolver(final LinearSystem linearSystem) {
        if (this.getParent() == null) {
            return;
        }
        final int objectVariableValue = linearSystem.getObjectVariableValue(this.mAnchor);
        if (this.mOrientation == 1) {
            this.setX(objectVariableValue);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
        }
        else {
            this.setX(0);
            this.setY(objectVariableValue);
            this.setWidth(this.getParent().getWidth());
            this.setHeight(0);
        }
    }
}
