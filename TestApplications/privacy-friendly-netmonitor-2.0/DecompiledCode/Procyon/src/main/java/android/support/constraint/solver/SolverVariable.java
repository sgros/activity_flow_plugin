// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable
{
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 6;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueId = 1;
    public float computedValue;
    int definitionId;
    public int id;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    private String mName;
    Type mType;
    public int strength;
    float[] strengthVector;
    
    public SolverVariable(final Type mType) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[6];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mType = mType;
    }
    
    public SolverVariable(final String mName, final Type mType) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[6];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mName = mName;
        this.mType = mType;
    }
    
    private static String getUniqueName(final Type type) {
        ++SolverVariable.uniqueId;
        switch (SolverVariable$1.$SwitchMap$android$support$constraint$solver$SolverVariable$Type[type.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("V");
                sb.append(SolverVariable.uniqueId);
                return sb.toString();
            }
            case 4: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("e");
                sb2.append(SolverVariable.uniqueId);
                return sb2.toString();
            }
            case 3: {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("S");
                sb3.append(SolverVariable.uniqueId);
                return sb3.toString();
            }
            case 2: {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("C");
                sb4.append(SolverVariable.uniqueId);
                return sb4.toString();
            }
            case 1: {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("U");
                sb5.append(SolverVariable.uniqueId);
                return sb5.toString();
            }
        }
    }
    
    void addClientEquation(final ArrayRow arrayRow) {
        for (int i = 0; i < this.mClientEquationsCount; ++i) {
            if (this.mClientEquations[i] == arrayRow) {
                return;
            }
        }
        if (this.mClientEquationsCount >= this.mClientEquations.length) {
            this.mClientEquations = Arrays.copyOf(this.mClientEquations, this.mClientEquations.length * 2);
        }
        this.mClientEquations[this.mClientEquationsCount] = arrayRow;
        ++this.mClientEquationsCount;
    }
    
    void clearStrengths() {
        for (int i = 0; i < 6; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }
    
    public String getName() {
        return this.mName;
    }
    
    void removeClientEquation(final ArrayRow arrayRow) {
        int i = 0;
        for (int j = 0; j < this.mClientEquationsCount; ++j) {
            if (this.mClientEquations[j] == arrayRow) {
                while (i < this.mClientEquationsCount - j - 1) {
                    final ArrayRow[] mClientEquations = this.mClientEquations;
                    final int n = j + i;
                    mClientEquations[n] = this.mClientEquations[n + 1];
                    ++i;
                }
                --this.mClientEquationsCount;
                return;
            }
        }
    }
    
    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
    }
    
    public void setName(final String mName) {
        this.mName = mName;
    }
    
    public void setType(final Type mType) {
        this.mType = mType;
    }
    
    String strengthsToString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.append("[");
        String str = sb.toString();
        for (int i = 0; i < this.strengthVector.length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(this.strengthVector[i]);
            final String string = sb2.toString();
            if (i < this.strengthVector.length - 1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(string);
                sb3.append(", ");
                str = sb3.toString();
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string);
                sb4.append("] ");
                str = sb4.toString();
            }
        }
        return str;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mName);
        return sb.toString();
    }
    
    public enum Type
    {
        CONSTANT, 
        ERROR, 
        SLACK, 
        UNKNOWN, 
        UNRESTRICTED;
    }
}
