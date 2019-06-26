// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;
import java.util.HashSet;
import android.support.constraint.solver.SolverVariable;

public class ConstraintAnchor
{
    private static final boolean ALLOW_BINARY = false;
    public static final int ANY_GROUP = Integer.MAX_VALUE;
    public static final int APPLY_GROUP_RESULTS = -2;
    public static final int AUTO_CONSTRAINT_CREATOR = 2;
    public static final int SCOUT_CREATOR = 1;
    private static final int UNSET_GONE_MARGIN = -1;
    public static final int USER_CREATOR = 0;
    public static final boolean USE_CENTER_ANCHOR = false;
    private int mConnectionCreator;
    private ConnectionType mConnectionType;
    int mGoneMargin;
    int mGroup;
    public int mMargin;
    final ConstraintWidget mOwner;
    SolverVariable mSolverVariable;
    private Strength mStrength;
    ConstraintAnchor mTarget;
    final Type mType;
    
    public ConstraintAnchor(final ConstraintWidget mOwner, final Type mType) {
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.NONE;
        this.mConnectionType = ConnectionType.RELAXED;
        this.mConnectionCreator = 0;
        this.mGroup = Integer.MAX_VALUE;
        this.mOwner = mOwner;
        this.mType = mType;
    }
    
    private boolean isConnectionToMe(final ConstraintWidget constraintWidget, final HashSet<ConstraintWidget> set) {
        if (set.contains(constraintWidget)) {
            return false;
        }
        set.add(constraintWidget);
        if (constraintWidget == this.getOwner()) {
            return true;
        }
        final ArrayList<ConstraintAnchor> anchors = constraintWidget.getAnchors();
        for (int size = anchors.size(), i = 0; i < size; ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && this.isConnectionToMe(constraintAnchor.getTarget().getOwner(), set)) {
                return true;
            }
        }
        return false;
    }
    
    private String toString(final HashSet<ConstraintAnchor> set) {
        if (set.add(this)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mOwner.getDebugName());
            sb.append(":");
            sb.append(this.mType.toString());
            String string;
            if (this.mTarget != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(" connected to ");
                sb2.append(this.mTarget.toString(set));
                string = sb2.toString();
            }
            else {
                string = "";
            }
            sb.append(string);
            return sb.toString();
        }
        return "<-";
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, 0, false);
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n, final int n2) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, n2, false);
    }
    
    public boolean connect(final ConstraintAnchor mTarget, final int mMargin, final int mGoneMargin, final Strength mStrength, final int mConnectionCreator, final boolean b) {
        if (mTarget == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            this.mStrength = Strength.NONE;
            this.mConnectionCreator = 2;
            return true;
        }
        if (!b && !this.isValidConnection(mTarget)) {
            return false;
        }
        this.mTarget = mTarget;
        if (mMargin > 0) {
            this.mMargin = mMargin;
        }
        else {
            this.mMargin = 0;
        }
        this.mGoneMargin = mGoneMargin;
        this.mStrength = mStrength;
        this.mConnectionCreator = mConnectionCreator;
        return true;
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n, final Strength strength, final int n2) {
        return this.connect(constraintAnchor, n, -1, strength, n2, false);
    }
    
    public int getConnectionCreator() {
        return this.mConnectionCreator;
    }
    
    public ConnectionType getConnectionType() {
        return this.mConnectionType;
    }
    
    public int getGroup() {
        return this.mGroup;
    }
    
    public int getMargin() {
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        if (this.mGoneMargin > -1 && this.mTarget != null && this.mTarget.mOwner.getVisibility() == 8) {
            return this.mGoneMargin;
        }
        return this.mMargin;
    }
    
    public final ConstraintAnchor getOpposite() {
        switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return null;
            }
            case 5: {
                return this.mOwner.mTop;
            }
            case 4: {
                return this.mOwner.mBottom;
            }
            case 3: {
                return this.mOwner.mLeft;
            }
            case 2: {
                return this.mOwner.mRight;
            }
        }
    }
    
    public ConstraintWidget getOwner() {
        return this.mOwner;
    }
    
    public int getPriorityLevel() {
        switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return 0;
            }
            case 8: {
                return 1;
            }
            case 7: {
                return 0;
            }
            case 6: {
                return 0;
            }
            case 5: {
                return 2;
            }
            case 4: {
                return 2;
            }
            case 3: {
                return 2;
            }
            case 2: {
                return 2;
            }
            case 1: {
                return 2;
            }
        }
    }
    
    public int getSnapPriorityLevel() {
        switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return 0;
            }
            case 8: {
                return 2;
            }
            case 7: {
                return 1;
            }
            case 6: {
                return 0;
            }
            case 5: {
                return 0;
            }
            case 4: {
                return 0;
            }
            case 3: {
                return 1;
            }
            case 2: {
                return 1;
            }
            case 1: {
                return 3;
            }
        }
    }
    
    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }
    
    public Strength getStrength() {
        return this.mStrength;
    }
    
    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }
    
    public Type getType() {
        return this.mType;
    }
    
    public boolean isConnected() {
        return this.mTarget != null;
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget constraintWidget) {
        if (this.isConnectionToMe(constraintWidget, new HashSet<ConstraintWidget>())) {
            return false;
        }
        final ConstraintWidget parent = this.getOwner().getParent();
        return parent == constraintWidget || constraintWidget.getParent() == parent;
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget constraintWidget, final ConstraintAnchor constraintAnchor) {
        return this.isConnectionAllowed(constraintWidget);
    }
    
    public boolean isSideAnchor() {
        switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return false;
            }
            case 2:
            case 3:
            case 4:
            case 5: {
                return true;
            }
        }
    }
    
    public boolean isSimilarDimensionConnection(final ConstraintAnchor constraintAnchor) {
        final Type type = constraintAnchor.getType();
        final Type mType = this.mType;
        final boolean b = true;
        boolean b2 = true;
        final boolean b3 = true;
        if (type == mType) {
            return true;
        }
        switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return false;
            }
            case 4:
            case 5:
            case 7:
            case 8: {
                boolean b4 = b3;
                if (type != Type.TOP) {
                    b4 = b3;
                    if (type != Type.BOTTOM) {
                        b4 = b3;
                        if (type != Type.CENTER_Y) {
                            b4 = (type == Type.BASELINE && b3);
                        }
                    }
                }
                return b4;
            }
            case 2:
            case 3:
            case 6: {
                boolean b5 = b;
                if (type != Type.LEFT) {
                    b5 = b;
                    if (type != Type.RIGHT) {
                        b5 = (type == Type.CENTER_X && b);
                    }
                }
                return b5;
            }
            case 1: {
                if (type == Type.BASELINE) {
                    b2 = false;
                }
                return b2;
            }
        }
    }
    
    public boolean isSnapCompatibleWith(final ConstraintAnchor constraintAnchor) {
        if (this.mType == Type.CENTER) {
            return false;
        }
        if (this.mType == constraintAnchor.getType()) {
            return true;
        }
        switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return false;
            }
            case 7: {
                switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()]) {
                    default: {
                        return false;
                    }
                    case 5: {
                        return true;
                    }
                    case 4: {
                        return true;
                    }
                }
                break;
            }
            case 6: {
                switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()]) {
                    default: {
                        return false;
                    }
                    case 3: {
                        return true;
                    }
                    case 2: {
                        return true;
                    }
                }
                break;
            }
            case 5: {
                final int n = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n == 4 || n == 7;
            }
            case 4: {
                final int n2 = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n2 == 5 || n2 == 7;
            }
            case 3: {
                final int n3 = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n3 == 2 || n3 == 6;
            }
            case 2: {
                final int n4 = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n4 == 3 || n4 == 6;
            }
        }
    }
    
    public boolean isValidConnection(final ConstraintAnchor constraintAnchor) {
        final boolean b = false;
        if (constraintAnchor == null) {
            return false;
        }
        final Type type = constraintAnchor.getType();
        if (type == this.mType) {
            return this.mType != Type.CENTER && (this.mType != Type.BASELINE || (constraintAnchor.getOwner().hasBaseline() && this.getOwner().hasBaseline()));
        }
        switch (ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return false;
            }
            case 4:
            case 5: {
                boolean b2 = type == Type.TOP || type == Type.BOTTOM;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    b2 = (b2 || type == Type.CENTER_Y);
                }
                return b2;
            }
            case 2:
            case 3: {
                boolean b3 = type == Type.LEFT || type == Type.RIGHT;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    b3 = (b3 || type == Type.CENTER_X);
                }
                return b3;
            }
            case 1: {
                boolean b4 = b;
                if (type != Type.BASELINE) {
                    b4 = b;
                    if (type != Type.CENTER_X) {
                        b4 = b;
                        if (type != Type.CENTER_Y) {
                            b4 = true;
                        }
                    }
                }
                return b4;
            }
        }
    }
    
    public boolean isVerticalAnchor() {
        final int n = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()];
        if (n != 6) {
            switch (n) {
                default: {
                    return true;
                }
                case 1:
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return false;
    }
    
    public void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.STRONG;
        this.mConnectionCreator = 0;
        this.mConnectionType = ConnectionType.RELAXED;
    }
    
    public void resetSolverVariable(final Cache cache) {
        if (this.mSolverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED);
        }
        else {
            this.mSolverVariable.reset();
        }
    }
    
    public void setConnectionCreator(final int mConnectionCreator) {
        this.mConnectionCreator = mConnectionCreator;
    }
    
    public void setConnectionType(final ConnectionType mConnectionType) {
        this.mConnectionType = mConnectionType;
    }
    
    public void setGoneMargin(final int mGoneMargin) {
        if (this.isConnected()) {
            this.mGoneMargin = mGoneMargin;
        }
    }
    
    public void setGroup(final int mGroup) {
        this.mGroup = mGroup;
    }
    
    public void setMargin(final int mMargin) {
        if (this.isConnected()) {
            this.mMargin = mMargin;
        }
    }
    
    public void setStrength(final Strength mStrength) {
        if (this.isConnected()) {
            this.mStrength = mStrength;
        }
    }
    
    @Override
    public String toString() {
        final HashSet<ConstraintAnchor> set = new HashSet<ConstraintAnchor>();
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mOwner.getDebugName());
        sb.append(":");
        sb.append(this.mType.toString());
        String string;
        if (this.mTarget != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" connected to ");
            sb2.append(this.mTarget.toString(set));
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        return sb.toString();
    }
    
    public enum ConnectionType
    {
        RELAXED, 
        STRICT;
    }
    
    public enum Strength
    {
        NONE, 
        STRONG, 
        WEAK;
    }
    
    public enum Type
    {
        BASELINE, 
        BOTTOM, 
        CENTER, 
        CENTER_X, 
        CENTER_Y, 
        LEFT, 
        NONE, 
        RIGHT, 
        TOP;
    }
}
