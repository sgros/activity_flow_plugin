// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver;

public class ArrayRow
{
    private static final boolean DEBUG = false;
    float constantValue;
    boolean isSimpleDefinition;
    boolean used;
    SolverVariable variable;
    final ArrayLinkedVariables variables;
    
    public ArrayRow(final Cache cache) {
        this.variable = null;
        this.constantValue = 0.0f;
        this.used = false;
        this.isSimpleDefinition = false;
        this.variables = new ArrayLinkedVariables(this, cache);
    }
    
    public ArrayRow addError(final SolverVariable solverVariable, final SolverVariable solverVariable2) {
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        return this;
    }
    
    ArrayRow addSingleError(final SolverVariable solverVariable, final int n) {
        this.variables.put(solverVariable, (float)n);
        return this;
    }
    
    ArrayRow createRowCentering(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final float n2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final int n3) {
        if (solverVariable2 == solverVariable3) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable2, -2.0f);
            return this;
        }
        if (n2 == 0.5f) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            if (n > 0 || n3 > 0) {
                this.constantValue = (float)(-n + n3);
            }
        }
        else if (n2 <= 0.0f) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.constantValue = (float)n;
        }
        else if (n2 >= 1.0f) {
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.constantValue = (float)n3;
        }
        else {
            final ArrayLinkedVariables variables = this.variables;
            final float n4 = 1.0f - n2;
            variables.put(solverVariable, 1.0f * n4);
            this.variables.put(solverVariable2, -1.0f * n4);
            this.variables.put(solverVariable3, -1.0f * n2);
            this.variables.put(solverVariable4, 1.0f * n2);
            if (n > 0 || n3 > 0) {
                this.constantValue = -n * n4 + n3 * n2;
            }
        }
        return this;
    }
    
    ArrayRow createRowDefinition(final SolverVariable variable, final int n) {
        this.variable = variable;
        final float n2 = (float)n;
        variable.computedValue = n2;
        this.constantValue = n2;
        this.isSimpleDefinition = true;
        return this;
    }
    
    ArrayRow createRowDimensionPercent(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f - n);
        this.variables.put(solverVariable3, n);
        return this;
    }
    
    public ArrayRow createRowDimensionRatio(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f);
        this.variables.put(solverVariable3, n);
        this.variables.put(solverVariable4, -n);
        return this;
    }
    
    public ArrayRow createRowEqualDimension(float n, final float n2, final float n3, final SolverVariable solverVariable, final int n4, final SolverVariable solverVariable2, final int n5, final SolverVariable solverVariable3, final int n6, final SolverVariable solverVariable4, final int n7) {
        if (n2 != 0.0f && n != n3) {
            n = n / n2 / (n3 / n2);
            this.constantValue = -n4 - n5 + n6 * n + n7 * n;
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, n);
            this.variables.put(solverVariable3, -n);
        }
        else {
            this.constantValue = (float)(-n4 - n5 + n6 + n7);
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final int n) {
        if (n < 0) {
            this.constantValue = (float)(-1 * n);
            this.variables.put(solverVariable, 1.0f);
        }
        else {
            this.constantValue = (float)n;
            this.variables.put(solverVariable, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            n2 = n3;
            int n4;
            if ((n4 = n) < 0) {
                n4 = n * -1;
                n2 = 1;
            }
            this.constantValue = (float)n4;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
        }
        else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            n2 = n3;
            int n4;
            if ((n4 = n) < 0) {
                n4 = n * -1;
                n2 = 1;
            }
            this.constantValue = (float)n4;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, 1.0f);
        }
        else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            n2 = n3;
            int n4;
            if ((n4 = n) < 0) {
                n4 = n * -1;
                n2 = 1;
            }
            this.constantValue = (float)n4;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, 1.0f);
        }
        return this;
    }
    
    void ensurePositiveConstant() {
        if (this.constantValue < 0.0f) {
            this.constantValue *= -1.0f;
            this.variables.invert();
        }
    }
    
    boolean hasAtLeastOnePositiveVariable() {
        return this.variables.hasAtLeastOnePositiveVariable();
    }
    
    boolean hasKeyVariable() {
        return this.variable != null && (this.variable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f);
    }
    
    boolean hasVariable(final SolverVariable solverVariable) {
        return this.variables.containsKey(solverVariable);
    }
    
    void pickRowVariable() {
        final SolverVariable pickPivotCandidate = this.variables.pickPivotCandidate();
        if (pickPivotCandidate != null) {
            this.pivot(pickPivotCandidate);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
        }
    }
    
    void pivot(final SolverVariable variable) {
        if (this.variable != null) {
            this.variables.put(this.variable, -1.0f);
            this.variable = null;
        }
        final float n = this.variables.remove(variable) * -1.0f;
        this.variable = variable;
        if (n == 1.0f) {
            return;
        }
        this.constantValue /= n;
        this.variables.divideByAmount(n);
    }
    
    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }
    
    int sizeInBytes() {
        int n;
        if (this.variable != null) {
            n = 4;
        }
        else {
            n = 0;
        }
        return n + 4 + 4 + this.variables.sizeInBytes();
    }
    
    String toReadableString() {
        String str;
        if (this.variable == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append("0");
            str = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(this.variable);
            str = sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(" = ");
        String str2 = sb3.toString();
        final float constantValue = this.constantValue;
        int i = 0;
        int n;
        if (constantValue != 0.0f) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(this.constantValue);
            str2 = sb4.toString();
            n = 1;
        }
        else {
            n = 0;
        }
        while (i < this.variables.currentSize) {
            final SolverVariable variable = this.variables.getVariable(i);
            if (variable != null) {
                final float variableValue = this.variables.getVariableValue(i);
                final String string = variable.toString();
                String s;
                float f;
                if (n == 0) {
                    s = str2;
                    f = variableValue;
                    if (variableValue < 0.0f) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(str2);
                        sb5.append("- ");
                        s = sb5.toString();
                        f = variableValue * -1.0f;
                    }
                }
                else if (variableValue > 0.0f) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(str2);
                    sb6.append(" + ");
                    s = sb6.toString();
                    f = variableValue;
                }
                else {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append(str2);
                    sb7.append(" - ");
                    s = sb7.toString();
                    f = variableValue * -1.0f;
                }
                if (f == 1.0f) {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append(s);
                    sb8.append(string);
                    str2 = sb8.toString();
                }
                else {
                    final StringBuilder sb9 = new StringBuilder();
                    sb9.append(s);
                    sb9.append(f);
                    sb9.append(" ");
                    sb9.append(string);
                    str2 = sb9.toString();
                }
                n = 1;
            }
            ++i;
        }
        String string2 = str2;
        if (n == 0) {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(str2);
            sb10.append("0.0");
            string2 = sb10.toString();
        }
        return string2;
    }
    
    @Override
    public String toString() {
        return this.toReadableString();
    }
    
    void updateClientEquations() {
        this.variables.updateClientEquations(this);
    }
    
    boolean updateRowWithEquation(final ArrayRow arrayRow) {
        this.variables.updateFromRow(this, arrayRow);
        return true;
    }
}
