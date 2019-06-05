// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver;

import java.util.Arrays;

public class ArrayLinkedVariables
{
    private int ROW_SIZE;
    private SolverVariable candidate;
    int currentSize;
    private int[] mArrayIndices;
    private int[] mArrayNextIndices;
    private float[] mArrayValues;
    private final Cache mCache;
    private boolean mDidFillOnce;
    private int mHead;
    private int mLast;
    private final ArrayRow mRow;
    
    ArrayLinkedVariables(final ArrayRow mRow, final Cache mCache) {
        this.currentSize = 0;
        this.ROW_SIZE = 8;
        this.candidate = null;
        this.mArrayIndices = new int[this.ROW_SIZE];
        this.mArrayNextIndices = new int[this.ROW_SIZE];
        this.mArrayValues = new float[this.ROW_SIZE];
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.mRow = mRow;
        this.mCache = mCache;
    }
    
    private boolean isNew(final SolverVariable solverVariable, final LinearSystem linearSystem) {
        final int usageInRowCount = solverVariable.usageInRowCount;
        boolean b = true;
        if (usageInRowCount > 1) {
            b = false;
        }
        return b;
    }
    
    final void add(final SolverVariable solverVariable, final float n, final boolean b) {
        if (n == 0.0f) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[this.mHead] = n;
            this.mArrayIndices[this.mHead] = solverVariable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++solverVariable.usageInRowCount;
            solverVariable.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                if (this.mLast >= this.mArrayIndices.length) {
                    this.mDidFillOnce = true;
                    this.mLast = this.mArrayIndices.length - 1;
                }
            }
            return;
        }
        int mHead = this.mHead;
        int n2 = 0;
        int n3 = -1;
        while (mHead != -1 && n2 < this.currentSize) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                final float[] mArrayValues = this.mArrayValues;
                mArrayValues[mHead] += n;
                if (this.mArrayValues[mHead] == 0.0f) {
                    if (mHead == this.mHead) {
                        this.mHead = this.mArrayNextIndices[mHead];
                    }
                    else {
                        this.mArrayNextIndices[n3] = this.mArrayNextIndices[mHead];
                    }
                    if (b) {
                        solverVariable.removeFromRow(this.mRow);
                    }
                    if (this.mDidFillOnce) {
                        this.mLast = mHead;
                    }
                    --solverVariable.usageInRowCount;
                    --this.currentSize;
                }
                return;
            }
            if (this.mArrayIndices[mHead] < solverVariable.id) {
                n3 = mHead;
            }
            mHead = this.mArrayNextIndices[mHead];
            ++n2;
        }
        int n4 = this.mLast + 1;
        if (this.mDidFillOnce) {
            if (this.mArrayIndices[this.mLast] == -1) {
                n4 = this.mLast;
            }
            else {
                n4 = this.mArrayIndices.length;
            }
        }
        int n5;
        if ((n5 = n4) >= this.mArrayIndices.length) {
            n5 = n4;
            if (this.currentSize < this.mArrayIndices.length) {
                int n6 = 0;
                while (true) {
                    n5 = n4;
                    if (n6 >= this.mArrayIndices.length) {
                        break;
                    }
                    if (this.mArrayIndices[n6] == -1) {
                        n5 = n6;
                        break;
                    }
                    ++n6;
                }
            }
        }
        int length;
        if ((length = n5) >= this.mArrayIndices.length) {
            length = this.mArrayIndices.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = length - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[length] = solverVariable.id;
        this.mArrayValues[length] = n;
        if (n3 != -1) {
            this.mArrayNextIndices[length] = this.mArrayNextIndices[n3];
            this.mArrayNextIndices[n3] = length;
        }
        else {
            this.mArrayNextIndices[length] = this.mHead;
            this.mHead = length;
        }
        ++solverVariable.usageInRowCount;
        solverVariable.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (this.mLast >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
            this.mLast = this.mArrayIndices.length - 1;
        }
    }
    
    SolverVariable chooseSubject(final LinearSystem linearSystem) {
        int mHead = this.mHead;
        SolverVariable solverVariable = null;
        int n = 0;
        SolverVariable solverVariable2 = null;
        float n2 = 0.0f;
        int n3 = 0;
        float n4 = 0.0f;
        int n5 = 0;
        while (mHead != -1 && n < this.currentSize) {
            final float n6 = this.mArrayValues[mHead];
            final SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            float n7 = 0.0f;
            Label_0135: {
                if (n6 < 0.0f) {
                    n7 = n6;
                    if (n6 <= -0.001f) {
                        break Label_0135;
                    }
                    this.mArrayValues[mHead] = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                }
                else {
                    n7 = n6;
                    if (n6 >= 0.001f) {
                        break Label_0135;
                    }
                    this.mArrayValues[mHead] = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                }
                n7 = 0.0f;
            }
            SolverVariable solverVariable4 = solverVariable;
            SolverVariable solverVariable5 = solverVariable2;
            float n8 = n2;
            int n9 = n3;
            float n10 = n4;
            int n11 = n5;
            Label_0519: {
                if (n7 != 0.0f) {
                    if (solverVariable3.mType == SolverVariable.Type.UNRESTRICTED) {
                        if (solverVariable == null) {
                            n9 = (this.isNew(solverVariable3, linearSystem) ? 1 : 0);
                        }
                        else if (n2 > n7) {
                            n9 = (this.isNew(solverVariable3, linearSystem) ? 1 : 0);
                        }
                        else {
                            solverVariable4 = solverVariable;
                            solverVariable5 = solverVariable2;
                            n8 = n2;
                            n9 = n3;
                            n10 = n4;
                            n11 = n5;
                            if (n3 != 0) {
                                break Label_0519;
                            }
                            solverVariable4 = solverVariable;
                            solverVariable5 = solverVariable2;
                            n8 = n2;
                            n9 = n3;
                            n10 = n4;
                            n11 = n5;
                            if (this.isNew(solverVariable3, linearSystem)) {
                                n9 = 1;
                                solverVariable4 = solverVariable3;
                                solverVariable5 = solverVariable2;
                                n8 = n7;
                                n10 = n4;
                                n11 = n5;
                            }
                            break Label_0519;
                        }
                        solverVariable4 = solverVariable3;
                        solverVariable5 = solverVariable2;
                        n8 = n7;
                        n10 = n4;
                        n11 = n5;
                    }
                    else {
                        solverVariable4 = solverVariable;
                        solverVariable5 = solverVariable2;
                        n8 = n2;
                        n9 = n3;
                        n10 = n4;
                        n11 = n5;
                        if (solverVariable == null) {
                            solverVariable4 = solverVariable;
                            solverVariable5 = solverVariable2;
                            n8 = n2;
                            n9 = n3;
                            n10 = n4;
                            n11 = n5;
                            if (n7 < 0.0f) {
                                boolean b;
                                if (solverVariable2 == null) {
                                    b = this.isNew(solverVariable3, linearSystem);
                                }
                                else if (n4 > n7) {
                                    b = this.isNew(solverVariable3, linearSystem);
                                }
                                else {
                                    solverVariable4 = solverVariable;
                                    solverVariable5 = solverVariable2;
                                    n8 = n2;
                                    n9 = n3;
                                    n10 = n4;
                                    if ((n11 = n5) != 0) {
                                        break Label_0519;
                                    }
                                    solverVariable4 = solverVariable;
                                    solverVariable5 = solverVariable2;
                                    n8 = n2;
                                    n9 = n3;
                                    n10 = n4;
                                    n11 = n5;
                                    if (this.isNew(solverVariable3, linearSystem)) {
                                        n11 = 1;
                                        n10 = n7;
                                        n9 = n3;
                                        n8 = n2;
                                        solverVariable5 = solverVariable3;
                                        solverVariable4 = solverVariable;
                                    }
                                    break Label_0519;
                                }
                                n11 = (b ? 1 : 0);
                                solverVariable4 = solverVariable;
                                solverVariable5 = solverVariable3;
                                n8 = n2;
                                n9 = n3;
                                n10 = n7;
                            }
                        }
                    }
                }
            }
            mHead = this.mArrayNextIndices[mHead];
            ++n;
            solverVariable = solverVariable4;
            solverVariable2 = solverVariable5;
            n2 = n8;
            n3 = n9;
            n4 = n10;
            n5 = n11;
        }
        if (solverVariable != null) {
            return solverVariable;
        }
        return solverVariable2;
    }
    
    public final void clear() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            if (solverVariable != null) {
                solverVariable.removeFromRow(this.mRow);
            }
        }
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }
    
    final boolean containsKey(final SolverVariable solverVariable) {
        if (this.mHead == -1) {
            return false;
        }
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                return true;
            }
        }
        return false;
    }
    
    void divideByAmount(final float n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] /= n;
        }
    }
    
    public final float get(final SolverVariable solverVariable) {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    SolverVariable getPivotCandidate(final boolean[] array, final SolverVariable solverVariable) {
        int mHead = this.mHead;
        int n = 0;
        SolverVariable solverVariable2 = null;
        float n2 = 0.0f;
        while (mHead != -1 && n < this.currentSize) {
            SolverVariable solverVariable3 = solverVariable2;
            float n3 = n2;
            Label_0161: {
                if (this.mArrayValues[mHead] < 0.0f) {
                    final SolverVariable solverVariable4 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
                    if (array != null) {
                        solverVariable3 = solverVariable2;
                        n3 = n2;
                        if (array[solverVariable4.id]) {
                            break Label_0161;
                        }
                    }
                    solverVariable3 = solverVariable2;
                    n3 = n2;
                    if (solverVariable4 != solverVariable) {
                        if (solverVariable4.mType != SolverVariable.Type.SLACK) {
                            solverVariable3 = solverVariable2;
                            n3 = n2;
                            if (solverVariable4.mType != SolverVariable.Type.ERROR) {
                                break Label_0161;
                            }
                        }
                        final float n4 = this.mArrayValues[mHead];
                        solverVariable3 = solverVariable2;
                        n3 = n2;
                        if (n4 < n2) {
                            solverVariable3 = solverVariable4;
                            n3 = n4;
                        }
                    }
                }
            }
            mHead = this.mArrayNextIndices[mHead];
            ++n;
            solverVariable2 = solverVariable3;
            n2 = n3;
        }
        return solverVariable2;
    }
    
    final SolverVariable getVariable(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            }
        }
        return null;
    }
    
    final float getVariableValue(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    void invert() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] *= -1.0f;
        }
    }
    
    public final void put(final SolverVariable solverVariable, final float n) {
        if (n == 0.0f) {
            this.remove(solverVariable, true);
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[this.mHead] = n;
            this.mArrayIndices[this.mHead] = solverVariable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++solverVariable.usageInRowCount;
            solverVariable.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                if (this.mLast >= this.mArrayIndices.length) {
                    this.mDidFillOnce = true;
                    this.mLast = this.mArrayIndices.length - 1;
                }
            }
            return;
        }
        int mHead = this.mHead;
        int n2 = 0;
        int n3 = -1;
        while (mHead != -1 && n2 < this.currentSize) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                this.mArrayValues[mHead] = n;
                return;
            }
            if (this.mArrayIndices[mHead] < solverVariable.id) {
                n3 = mHead;
            }
            mHead = this.mArrayNextIndices[mHead];
            ++n2;
        }
        int n4 = this.mLast + 1;
        if (this.mDidFillOnce) {
            if (this.mArrayIndices[this.mLast] == -1) {
                n4 = this.mLast;
            }
            else {
                n4 = this.mArrayIndices.length;
            }
        }
        int n5;
        if ((n5 = n4) >= this.mArrayIndices.length) {
            n5 = n4;
            if (this.currentSize < this.mArrayIndices.length) {
                int n6 = 0;
                while (true) {
                    n5 = n4;
                    if (n6 >= this.mArrayIndices.length) {
                        break;
                    }
                    if (this.mArrayIndices[n6] == -1) {
                        n5 = n6;
                        break;
                    }
                    ++n6;
                }
            }
        }
        int length;
        if ((length = n5) >= this.mArrayIndices.length) {
            length = this.mArrayIndices.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = length - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[length] = solverVariable.id;
        this.mArrayValues[length] = n;
        if (n3 != -1) {
            this.mArrayNextIndices[length] = this.mArrayNextIndices[n3];
            this.mArrayNextIndices[n3] = length;
        }
        else {
            this.mArrayNextIndices[length] = this.mHead;
            this.mHead = length;
        }
        ++solverVariable.usageInRowCount;
        solverVariable.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
        }
        if (this.mLast >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
            this.mLast = this.mArrayIndices.length - 1;
        }
    }
    
    public final float remove(final SolverVariable solverVariable, final boolean b) {
        if (this.candidate == solverVariable) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int mHead = this.mHead;
        int n = 0;
        int n2 = -1;
        while (mHead != -1 && n < this.currentSize) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                if (mHead == this.mHead) {
                    this.mHead = this.mArrayNextIndices[mHead];
                }
                else {
                    this.mArrayNextIndices[n2] = this.mArrayNextIndices[mHead];
                }
                if (b) {
                    solverVariable.removeFromRow(this.mRow);
                }
                --solverVariable.usageInRowCount;
                --this.currentSize;
                this.mArrayIndices[mHead] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = mHead;
                }
                return this.mArrayValues[mHead];
            }
            final int n3 = this.mArrayNextIndices[mHead];
            ++n;
            n2 = mHead;
            mHead = n3;
        }
        return 0.0f;
    }
    
    @Override
    public String toString() {
        String string = "";
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(" -> ");
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(this.mArrayValues[mHead]);
            sb2.append(" : ");
            final String string3 = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string3);
            sb3.append(this.mCache.mIndexedVariables[this.mArrayIndices[mHead]]);
            string = sb3.toString();
        }
        return string;
    }
    
    final void updateFromRow(final ArrayRow arrayRow, final ArrayRow arrayRow2, final boolean b) {
        int n = this.mHead;
    Label_0006:
        while (true) {
            for (int n2 = 0; n != -1 && n2 < this.currentSize; n = this.mArrayNextIndices[n], ++n2) {
                if (this.mArrayIndices[n] == arrayRow2.variable.id) {
                    final float n3 = this.mArrayValues[n];
                    this.remove(arrayRow2.variable, b);
                    final ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                    for (int mHead = arrayLinkedVariables.mHead, n4 = 0; mHead != -1 && n4 < arrayLinkedVariables.currentSize; mHead = arrayLinkedVariables.mArrayNextIndices[mHead], ++n4) {
                        this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[mHead]], arrayLinkedVariables.mArrayValues[mHead] * n3, b);
                    }
                    arrayRow.constantValue += arrayRow2.constantValue * n3;
                    if (b) {
                        arrayRow2.variable.removeFromRow(arrayRow);
                    }
                    n = this.mHead;
                    continue Label_0006;
                }
            }
            break;
        }
    }
    
    void updateFromSystem(final ArrayRow arrayRow, final ArrayRow[] array) {
        int n = this.mHead;
    Label_0005:
        while (true) {
            for (int n2 = 0; n != -1 && n2 < this.currentSize; n = this.mArrayNextIndices[n], ++n2) {
                final SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
                if (solverVariable.definitionId != -1) {
                    final float n3 = this.mArrayValues[n];
                    this.remove(solverVariable, true);
                    final ArrayRow arrayRow2 = array[solverVariable.definitionId];
                    if (!arrayRow2.isSimpleDefinition) {
                        final ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                        for (int mHead = arrayLinkedVariables.mHead, n4 = 0; mHead != -1 && n4 < arrayLinkedVariables.currentSize; mHead = arrayLinkedVariables.mArrayNextIndices[mHead], ++n4) {
                            this.add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[mHead]], arrayLinkedVariables.mArrayValues[mHead] * n3, true);
                        }
                    }
                    arrayRow.constantValue += arrayRow2.constantValue * n3;
                    arrayRow2.variable.removeFromRow(arrayRow);
                    n = this.mHead;
                    continue Label_0005;
                }
            }
            break;
        }
    }
}
