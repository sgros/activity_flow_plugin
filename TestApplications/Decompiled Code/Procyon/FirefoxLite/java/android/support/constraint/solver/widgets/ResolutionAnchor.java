// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;

public class ResolutionAnchor extends ResolutionNode
{
    float computedValue;
    private ResolutionDimension dimension;
    private int dimensionMultiplier;
    ConstraintAnchor myAnchor;
    float offset;
    private ResolutionAnchor opposite;
    private ResolutionDimension oppositeDimension;
    private int oppositeDimensionMultiplier;
    private float oppositeOffset;
    float resolvedOffset;
    ResolutionAnchor resolvedTarget;
    ResolutionAnchor target;
    int type;
    
    public ResolutionAnchor(final ConstraintAnchor myAnchor) {
        this.type = 0;
        this.dimension = null;
        this.dimensionMultiplier = 1;
        this.oppositeDimension = null;
        this.oppositeDimensionMultiplier = 1;
        this.myAnchor = myAnchor;
    }
    
    void addResolvedValue(final LinearSystem linearSystem) {
        final SolverVariable solverVariable = this.myAnchor.getSolverVariable();
        if (this.resolvedTarget == null) {
            linearSystem.addEquality(solverVariable, (int)this.resolvedOffset);
        }
        else {
            linearSystem.addEquality(solverVariable, linearSystem.createObjectVariable(this.resolvedTarget.myAnchor), (int)this.resolvedOffset, 6);
        }
    }
    
    public void dependsOn(final int type, final ResolutionAnchor target, final int n) {
        this.type = type;
        this.target = target;
        this.offset = (float)n;
        this.target.addDependent(this);
    }
    
    public void dependsOn(final ResolutionAnchor target, final int n) {
        this.target = target;
        this.offset = (float)n;
        this.target.addDependent(this);
    }
    
    public void dependsOn(final ResolutionAnchor target, final int dimensionMultiplier, final ResolutionDimension dimension) {
        (this.target = target).addDependent(this);
        this.dimension = dimension;
        this.dimensionMultiplier = dimensionMultiplier;
        this.dimension.addDependent(this);
    }
    
    public float getResolvedValue() {
        return this.resolvedOffset;
    }
    
    @Override
    public void reset() {
        super.reset();
        this.target = null;
        this.offset = 0.0f;
        this.dimension = null;
        this.dimensionMultiplier = 1;
        this.oppositeDimension = null;
        this.oppositeDimensionMultiplier = 1;
        this.resolvedTarget = null;
        this.resolvedOffset = 0.0f;
        this.computedValue = 0.0f;
        this.opposite = null;
        this.oppositeOffset = 0.0f;
        this.type = 0;
    }
    
    @Override
    public void resolve() {
        final int state = this.state;
        final int n = 1;
        if (state == 1) {
            return;
        }
        if (this.type == 4) {
            return;
        }
        if (this.dimension != null) {
            if (this.dimension.state != 1) {
                return;
            }
            this.offset = this.dimensionMultiplier * this.dimension.value;
        }
        if (this.oppositeDimension != null) {
            if (this.oppositeDimension.state != 1) {
                return;
            }
            this.oppositeOffset = this.oppositeDimensionMultiplier * this.oppositeDimension.value;
        }
        if (this.type == 1 && (this.target == null || this.target.state == 1)) {
            if (this.target == null) {
                this.resolvedTarget = this;
                this.resolvedOffset = this.offset;
            }
            else {
                this.resolvedTarget = this.target.resolvedTarget;
                this.resolvedOffset = this.target.resolvedOffset + this.offset;
            }
            this.didResolve();
        }
        else if (this.type == 2 && this.target != null && this.target.state == 1 && this.opposite != null && this.opposite.target != null && this.opposite.target.state == 1) {
            if (LinearSystem.getMetrics() != null) {
                final Metrics metrics = LinearSystem.getMetrics();
                ++metrics.centerConnectionResolved;
            }
            this.resolvedTarget = this.target.resolvedTarget;
            this.opposite.resolvedTarget = this.opposite.target.resolvedTarget;
            final ConstraintAnchor.Type mType = this.myAnchor.mType;
            final ConstraintAnchor.Type right = ConstraintAnchor.Type.RIGHT;
            int n2 = 0;
            int n3 = n;
            if (mType != right) {
                if (this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
                    n3 = n;
                }
                else {
                    n3 = 0;
                }
            }
            float n4;
            if (n3 != 0) {
                n4 = this.target.resolvedOffset - this.opposite.target.resolvedOffset;
            }
            else {
                n4 = this.opposite.target.resolvedOffset - this.target.resolvedOffset;
            }
            float n5;
            float n6;
            if (this.myAnchor.mType != ConstraintAnchor.Type.LEFT && this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
                n5 = n4 - this.myAnchor.mOwner.getHeight();
                n6 = this.myAnchor.mOwner.mVerticalBiasPercent;
            }
            else {
                n5 = n4 - this.myAnchor.mOwner.getWidth();
                n6 = this.myAnchor.mOwner.mHorizontalBiasPercent;
            }
            final int margin = this.myAnchor.getMargin();
            int margin2 = this.opposite.myAnchor.getMargin();
            if (this.myAnchor.getTarget() == this.opposite.myAnchor.getTarget()) {
                n6 = 0.5f;
                margin2 = 0;
            }
            else {
                n2 = margin;
            }
            final float n7 = (float)n2;
            final float n8 = (float)margin2;
            final float n9 = n5 - n7 - n8;
            if (n3 != 0) {
                this.opposite.resolvedOffset = this.opposite.target.resolvedOffset + n8 + n9 * n6;
                this.resolvedOffset = this.target.resolvedOffset - n7 - n9 * (1.0f - n6);
            }
            else {
                this.resolvedOffset = this.target.resolvedOffset + n7 + n9 * n6;
                this.opposite.resolvedOffset = this.opposite.target.resolvedOffset - n8 - n9 * (1.0f - n6);
            }
            this.didResolve();
            this.opposite.didResolve();
        }
        else if (this.type == 3 && this.target != null && this.target.state == 1 && this.opposite != null && this.opposite.target != null && this.opposite.target.state == 1) {
            if (LinearSystem.getMetrics() != null) {
                final Metrics metrics2 = LinearSystem.getMetrics();
                ++metrics2.matchConnectionResolved;
            }
            this.resolvedTarget = this.target.resolvedTarget;
            this.opposite.resolvedTarget = this.opposite.target.resolvedTarget;
            this.resolvedOffset = this.target.resolvedOffset + this.offset;
            this.opposite.resolvedOffset = this.opposite.target.resolvedOffset + this.opposite.offset;
            this.didResolve();
            this.opposite.didResolve();
        }
        else if (this.type == 5) {
            this.myAnchor.mOwner.resolve();
        }
    }
    
    public void resolve(final ResolutionAnchor resolvedTarget, final float resolvedOffset) {
        if (this.state == 0 || (this.resolvedTarget != resolvedTarget && this.resolvedOffset != resolvedOffset)) {
            this.resolvedTarget = resolvedTarget;
            this.resolvedOffset = resolvedOffset;
            if (this.state == 1) {
                this.invalidate();
            }
            this.didResolve();
        }
    }
    
    String sType(final int n) {
        if (n == 1) {
            return "DIRECT";
        }
        if (n == 2) {
            return "CENTER";
        }
        if (n == 3) {
            return "MATCH";
        }
        if (n == 4) {
            return "CHAIN";
        }
        if (n == 5) {
            return "BARRIER";
        }
        return "UNCONNECTED";
    }
    
    public void setOpposite(final ResolutionAnchor opposite, final float oppositeOffset) {
        this.opposite = opposite;
        this.oppositeOffset = oppositeOffset;
    }
    
    public void setOpposite(final ResolutionAnchor opposite, final int oppositeDimensionMultiplier, final ResolutionDimension oppositeDimension) {
        this.opposite = opposite;
        this.oppositeDimension = oppositeDimension;
        this.oppositeDimensionMultiplier = oppositeDimensionMultiplier;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        if (this.state != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("{ ");
            sb.append(this.myAnchor);
            sb.append(" UNRESOLVED} type: ");
            sb.append(this.sType(this.type));
            return sb.toString();
        }
        if (this.resolvedTarget == this) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("[");
            sb2.append(this.myAnchor);
            sb2.append(", RESOLVED: ");
            sb2.append(this.resolvedOffset);
            sb2.append("]  type: ");
            sb2.append(this.sType(this.type));
            return sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("[");
        sb3.append(this.myAnchor);
        sb3.append(", RESOLVED: ");
        sb3.append(this.resolvedTarget);
        sb3.append(":");
        sb3.append(this.resolvedOffset);
        sb3.append("] type: ");
        sb3.append(this.sType(this.type));
        return sb3.toString();
    }
    
    public void update() {
        final ConstraintAnchor target = this.myAnchor.getTarget();
        if (target == null) {
            return;
        }
        if (target.getTarget() == this.myAnchor) {
            this.type = 4;
            target.getResolutionNode().type = 4;
        }
        final int margin = this.myAnchor.getMargin();
        int n = 0;
        Label_0076: {
            if (this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
                n = margin;
                if (this.myAnchor.mType != ConstraintAnchor.Type.BOTTOM) {
                    break Label_0076;
                }
            }
            n = -margin;
        }
        this.dependsOn(target.getResolutionNode(), n);
    }
}
