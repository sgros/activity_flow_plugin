// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver;

import java.io.PrintStream;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem
{
    private static final boolean DEBUG = false;
    private static int POOL_SIZE = 1000;
    private int TABLE_SIZE;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Goal mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    private int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    private ArrayRow[] mRows;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    private ArrayRow[] tempClientsCopy;
    
    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.mGoal = new Goal();
        this.TABLE_SIZE = 32;
        this.mMaxColumns = this.TABLE_SIZE;
        this.mRows = null;
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = this.TABLE_SIZE;
        this.mPoolVariables = new SolverVariable[LinearSystem.POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[this.TABLE_SIZE];
        this.mRows = new ArrayRow[this.TABLE_SIZE];
        this.releaseRows();
        this.mCache = new Cache();
    }
    
    private SolverVariable acquireSolverVariable(final SolverVariable.Type type) {
        final SolverVariable solverVariable = this.mCache.solverVariablePool.acquire();
        SolverVariable solverVariable2;
        if (solverVariable == null) {
            solverVariable2 = new SolverVariable(type);
        }
        else {
            solverVariable.reset();
            solverVariable.setType(type);
            solverVariable2 = solverVariable;
        }
        if (this.mPoolVariablesCount >= LinearSystem.POOL_SIZE) {
            LinearSystem.POOL_SIZE *= 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, LinearSystem.POOL_SIZE);
        }
        return this.mPoolVariables[this.mPoolVariablesCount++] = solverVariable2;
    }
    
    private void addError(final ArrayRow arrayRow) {
        arrayRow.addError(this.createErrorVariable(), this.createErrorVariable());
    }
    
    private void addSingleError(final ArrayRow arrayRow, final int n) {
        arrayRow.addSingleError(this.createErrorVariable(), n);
    }
    
    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            final ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }
    
    public static ArrayRow createRowCentering(final LinearSystem linearSystem, SolverVariable errorVariable, final SolverVariable solverVariable, final int n, final float n2, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final int n3, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowCentering(errorVariable, solverVariable, n, n2, solverVariable2, solverVariable3, n3);
        if (b) {
            errorVariable = linearSystem.createErrorVariable();
            final SolverVariable errorVariable2 = linearSystem.createErrorVariable();
            errorVariable.strength = 4;
            errorVariable2.strength = 4;
            row.addError(errorVariable, errorVariable2);
        }
        return row;
    }
    
    public static ArrayRow createRowDimensionPercent(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        if (b) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, n);
    }
    
    public static ArrayRow createRowEquals(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowEquals(solverVariable, solverVariable2, n);
        if (b) {
            linearSystem.addSingleError(row, 1);
        }
        return row;
    }
    
    public static ArrayRow createRowGreaterThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
        }
        return row;
    }
    
    public static ArrayRow createRowLowerThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
        }
        return row;
    }
    
    private SolverVariable createVariable(final String s, final SolverVariable.Type type) {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(type);
        acquireSolverVariable.setName(s);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        this.mVariables.put(s, acquireSolverVariable);
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    private void displayRows() {
        this.displaySolverVariables();
        String string = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(this.mRows[i]);
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append("\n");
            string = sb2.toString();
        }
        String string3 = string;
        if (this.mGoal.variables.size() != 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append(this.mGoal);
            sb3.append("\n");
            string3 = sb3.toString();
        }
        System.out.println(string3);
    }
    
    private void displaySolverVariables() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Display Rows (");
        sb.append(this.mNumRows);
        sb.append("x");
        sb.append(this.mNumColumns);
        sb.append(") :\n\t | C | ");
        String s = sb.toString();
        for (int i = 1; i <= this.mNumColumns; ++i) {
            final SolverVariable obj = this.mCache.mIndexedVariables[i];
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(obj);
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append(" | ");
            s = sb3.toString();
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(s);
        sb4.append("\n");
        System.out.println(sb4.toString());
    }
    
    private int enforceBFS(final Goal goal) throws Exception {
        while (true) {
            for (int i = 0; i < this.mNumRows; ++i) {
                if (this.mRows[i].variable.mType != SolverVariable.Type.UNRESTRICTED) {
                    if (this.mRows[i].constantValue < 0.0f) {
                        final boolean b = true;
                        int n3;
                        if (b) {
                            int n = 0;
                            int n2 = 0;
                            while (true) {
                                n3 = n2;
                                if (n != 0) {
                                    break;
                                }
                                final int n4 = n2 + 1;
                                float n5 = Float.MAX_VALUE;
                                int n6;
                                int definitionId = n6 = -1;
                                int j = 0;
                                int n7 = 0;
                                while (j < this.mNumRows) {
                                    final ArrayRow arrayRow = this.mRows[j];
                                    int n8;
                                    int n9;
                                    float n10;
                                    int n11;
                                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                                        n8 = definitionId;
                                        n9 = n6;
                                        n10 = n5;
                                        n11 = n7;
                                    }
                                    else {
                                        n8 = definitionId;
                                        n9 = n6;
                                        n10 = n5;
                                        n11 = n7;
                                        if (arrayRow.constantValue < 0.0f) {
                                            final int n12 = definitionId;
                                            final int n13 = 1;
                                            int n14 = n7;
                                            int n15 = n12;
                                            for (int k = n13; k < this.mNumColumns; ++k) {
                                                final SolverVariable solverVariable = this.mCache.mIndexedVariables[k];
                                                final float value = arrayRow.variables.get(solverVariable);
                                                if (value > 0.0f) {
                                                    final int n16 = n14;
                                                    int l = 0;
                                                    int n17 = n15;
                                                    int n18 = n16;
                                                    while (l < 6) {
                                                        final float n19 = solverVariable.strengthVector[l] / value;
                                                        int n20;
                                                        if ((n19 < n5 && l == n18) || l > (n20 = n18)) {
                                                            n5 = n19;
                                                            n17 = j;
                                                            n6 = k;
                                                            n20 = l;
                                                        }
                                                        ++l;
                                                        n18 = n20;
                                                    }
                                                    n14 = n18;
                                                    n15 = n17;
                                                }
                                            }
                                            n11 = n14;
                                            n10 = n5;
                                            n9 = n6;
                                            n8 = n15;
                                        }
                                    }
                                    ++j;
                                    definitionId = n8;
                                    n6 = n9;
                                    n5 = n10;
                                    n7 = n11;
                                }
                                if (definitionId != -1) {
                                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                                    arrayRow2.variable.definitionId = -1;
                                    arrayRow2.pivot(this.mCache.mIndexedVariables[n6]);
                                    arrayRow2.variable.definitionId = definitionId;
                                    for (int n21 = 0; n21 < this.mNumRows; ++n21) {
                                        this.mRows[n21].updateRowWithEquation(arrayRow2);
                                    }
                                    goal.updateFromSystem(this);
                                    n2 = n4;
                                }
                                else {
                                    n = 1;
                                    n2 = n4;
                                }
                            }
                        }
                        else {
                            n3 = 0;
                        }
                        for (int n22 = 0; n22 < this.mNumRows; ++n22) {
                            if (this.mRows[n22].variable.mType != SolverVariable.Type.UNRESTRICTED) {
                                if (this.mRows[n22].constantValue < 0.0f) {
                                    break;
                                }
                            }
                        }
                        return n3;
                    }
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    private String getDisplaySize(int i) {
        final int j = i * 4;
        final int k = j / 1024;
        i = k / 1024;
        if (i > 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i);
            sb.append(" Mb");
            return sb.toString();
        }
        if (k > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(k);
            sb2.append(" Kb");
            return sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(j);
        sb3.append(" bytes");
        return sb3.toString();
    }
    
    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        this.mCache.mIndexedVariables = Arrays.copyOf(this.mCache.mIndexedVariables, this.TABLE_SIZE);
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mMaxColumns = this.TABLE_SIZE;
        this.mMaxRows = this.TABLE_SIZE;
        this.mGoal.variables.clear();
    }
    
    private int optimize(final Goal goal) {
        for (int i = 0; i < this.mNumColumns; ++i) {
            this.mAlreadyTestedCandidates[i] = false;
        }
        final int n = 0;
        int n3;
        int n2 = n3 = n;
        int j = n;
        while (j == 0) {
            final int n4 = n3 + 1;
            final SolverVariable pivotCandidate = goal.getPivotCandidate();
            n3 = j;
            int n5 = n2;
            SolverVariable solverVariable;
            if ((solverVariable = pivotCandidate) != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.id]) {
                    solverVariable = null;
                    n3 = j;
                    n5 = n2;
                }
                else {
                    this.mAlreadyTestedCandidates[pivotCandidate.id] = true;
                    ++n2;
                    n3 = j;
                    n5 = n2;
                    solverVariable = pivotCandidate;
                    if (n2 >= this.mNumColumns) {
                        n3 = 1;
                        solverVariable = pivotCandidate;
                        n5 = n2;
                    }
                }
            }
            if (solverVariable != null) {
                float n6 = Float.MAX_VALUE;
                int definitionId = -1;
                int n7;
                float n8;
                for (int k = 0; k < this.mNumRows; ++k, definitionId = n7, n6 = n8) {
                    final ArrayRow arrayRow = this.mRows[k];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        n7 = definitionId;
                        n8 = n6;
                    }
                    else {
                        n7 = definitionId;
                        n8 = n6;
                        if (arrayRow.hasVariable(solverVariable)) {
                            final float value = arrayRow.variables.get(solverVariable);
                            n7 = definitionId;
                            n8 = n6;
                            if (value < 0.0f) {
                                final float n9 = -arrayRow.constantValue / value;
                                n7 = definitionId;
                                n8 = n6;
                                if (n9 < n6) {
                                    n7 = k;
                                    n8 = n9;
                                }
                            }
                        }
                    }
                }
                if (definitionId > -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(solverVariable);
                    arrayRow2.variable.definitionId = definitionId;
                    for (int l = 0; l < this.mNumRows; ++l) {
                        this.mRows[l].updateRowWithEquation(arrayRow2);
                    }
                    goal.updateFromSystem(this);
                    try {
                        this.enforceBFS(goal);
                        j = n3;
                        n3 = n4;
                        n2 = n5;
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        j = n3;
                        n3 = n4;
                        n2 = n5;
                    }
                    continue;
                }
            }
            j = 1;
            n3 = n4;
            n2 = n5;
        }
        return n3;
    }
    
    private void releaseRows() {
        for (int i = 0; i < this.mRows.length; ++i) {
            final ArrayRow arrayRow = this.mRows[i];
            if (arrayRow != null) {
                this.mCache.arrayRowPool.release(arrayRow);
            }
            this.mRows[i] = null;
        }
    }
    
    private void updateRowFromVariables(final ArrayRow arrayRow) {
        if (this.mNumRows > 0) {
            arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
            if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
            }
        }
    }
    
    public void addCentering(SolverVariable errorVariable, SolverVariable errorVariable2, final int n, final float n2, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n3, final int n4) {
        final ArrayRow row = this.createRow();
        row.createRowCentering(errorVariable, errorVariable2, n, n2, solverVariable, solverVariable2, n3);
        errorVariable = this.createErrorVariable();
        errorVariable2 = this.createErrorVariable();
        errorVariable.strength = n4;
        errorVariable2.strength = n4;
        row.addError(errorVariable, errorVariable2);
        this.addConstraint(row);
    }
    
    public void addConstraint(final ArrayRow arrayRow) {
        if (arrayRow == null) {
            return;
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        if (!arrayRow.isSimpleDefinition) {
            this.updateRowFromVariables(arrayRow);
            arrayRow.ensurePositiveConstant();
            arrayRow.pickRowVariable();
            if (!arrayRow.hasKeyVariable()) {
                return;
            }
        }
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        if (!arrayRow.isSimpleDefinition) {
            arrayRow.updateClientEquations();
        }
        this.mRows[this.mNumRows] = arrayRow;
        arrayRow.variable.definitionId = this.mNumRows;
        ++this.mNumRows;
        final int mClientEquationsCount = arrayRow.variable.mClientEquationsCount;
        if (mClientEquationsCount > 0) {
            while (this.tempClientsCopy.length < mClientEquationsCount) {
                this.tempClientsCopy = new ArrayRow[this.tempClientsCopy.length * 2];
            }
            final ArrayRow[] tempClientsCopy = this.tempClientsCopy;
            final int n = 0;
            int n2 = 0;
            int i;
            while (true) {
                i = n;
                if (n2 >= mClientEquationsCount) {
                    break;
                }
                tempClientsCopy[n2] = arrayRow.variable.mClientEquations[n2];
                ++n2;
            }
            while (i < mClientEquationsCount) {
                final ArrayRow arrayRow2 = tempClientsCopy[i];
                if (arrayRow2 != arrayRow) {
                    arrayRow2.variables.updateFromRow(arrayRow2, arrayRow);
                    arrayRow2.updateClientEquations();
                }
                ++i;
            }
        }
    }
    
    public ArrayRow addEquality(SolverVariable errorVariable, SolverVariable errorVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        row.createRowEquals(errorVariable, errorVariable2, n);
        errorVariable = this.createErrorVariable();
        errorVariable2 = this.createErrorVariable();
        errorVariable.strength = n2;
        errorVariable2.strength = n2;
        row.addError(errorVariable, errorVariable2);
        this.addConstraint(row);
        return row;
    }
    
    public void addEquality(final SolverVariable solverVariable, final int n) {
        final int definitionId = solverVariable.definitionId;
        if (solverVariable.definitionId != -1) {
            final ArrayRow arrayRow = this.mRows[definitionId];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = (float)n;
            }
            else {
                final ArrayRow row = this.createRow();
                row.createRowEquals(solverVariable, n);
                this.addConstraint(row);
            }
        }
        else {
            final ArrayRow row2 = this.createRow();
            row2.createRowDefinition(solverVariable, n);
            this.addConstraint(row2);
        }
    }
    
    public void addGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int strength) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = strength;
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        this.addConstraint(row);
    }
    
    public void addLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int strength) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = strength;
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        this.addConstraint(row);
    }
    
    public SolverVariable createErrorVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.ERROR);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    public SolverVariable createObjectVariable(final Object o) {
        SolverVariable solverVariable = null;
        if (o == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        if (o instanceof ConstraintAnchor) {
            final ConstraintAnchor constraintAnchor = (ConstraintAnchor)o;
            SolverVariable solverVariable2;
            if ((solverVariable2 = constraintAnchor.getSolverVariable()) == null) {
                constraintAnchor.resetSolverVariable(this.mCache);
                solverVariable2 = constraintAnchor.getSolverVariable();
            }
            if (solverVariable2.id != -1 && solverVariable2.id <= this.mVariablesID) {
                solverVariable = solverVariable2;
                if (this.mCache.mIndexedVariables[solverVariable2.id] != null) {
                    return solverVariable;
                }
            }
            if (solverVariable2.id != -1) {
                solverVariable2.reset();
            }
            ++this.mVariablesID;
            ++this.mNumColumns;
            solverVariable2.id = this.mVariablesID;
            solverVariable2.mType = SolverVariable.Type.UNRESTRICTED;
            this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable2;
            solverVariable = solverVariable2;
        }
        return solverVariable;
    }
    
    public ArrayRow createRow() {
        ArrayRow arrayRow = this.mCache.arrayRowPool.acquire();
        if (arrayRow == null) {
            arrayRow = new ArrayRow(this.mCache);
        }
        else {
            arrayRow.reset();
        }
        return arrayRow;
    }
    
    public SolverVariable createSlackVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    void displayReadableRows() {
        this.displaySolverVariables();
        String string = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(this.mRows[i].toReadableString());
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append("\n");
            string = sb2.toString();
        }
        String string3 = string;
        if (this.mGoal != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append(this.mGoal);
            sb3.append("\n");
            string3 = sb3.toString();
        }
        System.out.println(string3);
    }
    
    void displaySystemInformations() {
        int n;
        int n2;
        for (int i = n = 0; i < this.TABLE_SIZE; ++i, n = n2) {
            n2 = n;
            if (this.mRows[i] != null) {
                n2 = n + this.mRows[i].sizeInBytes();
            }
        }
        int n3;
        int n4;
        for (int j = n3 = 0; j < this.mNumRows; ++j, n3 = n4) {
            n4 = n3;
            if (this.mRows[j] != null) {
                n4 = n3 + this.mRows[j].sizeInBytes();
            }
        }
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("Linear System -> Table size: ");
        sb.append(this.TABLE_SIZE);
        sb.append(" (");
        sb.append(this.getDisplaySize(this.TABLE_SIZE * this.TABLE_SIZE));
        sb.append(") -- row sizes: ");
        sb.append(this.getDisplaySize(n));
        sb.append(", actual size: ");
        sb.append(this.getDisplaySize(n3));
        sb.append(" rows: ");
        sb.append(this.mNumRows);
        sb.append("/");
        sb.append(this.mMaxRows);
        sb.append(" cols: ");
        sb.append(this.mNumColumns);
        sb.append("/");
        sb.append(this.mMaxColumns);
        sb.append(" ");
        sb.append(0);
        sb.append(" occupied cells, ");
        sb.append(this.getDisplaySize(0));
        out.println(sb.toString());
    }
    
    public void displayVariablesReadableRows() {
        this.displaySolverVariables();
        String s = "";
        String string;
        for (int i = 0; i < this.mNumRows; ++i, s = string) {
            string = s;
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(this.mRows[i].toReadableString());
                final String string2 = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string2);
                sb2.append("\n");
                string = sb2.toString();
            }
        }
        String string3 = s;
        if (this.mGoal.variables.size() != 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(this.mGoal);
            sb3.append("\n");
            string3 = sb3.toString();
        }
        System.out.println(string3);
    }
    
    public Cache getCache() {
        return this.mCache;
    }
    
    Goal getGoal() {
        return this.mGoal;
    }
    
    public int getMemoryUsed() {
        int i = 0;
        int n = 0;
        while (i < this.mNumRows) {
            int n2 = n;
            if (this.mRows[i] != null) {
                n2 = n + this.mRows[i].sizeInBytes();
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    public int getNumEquations() {
        return this.mNumRows;
    }
    
    public int getNumVariables() {
        return this.mVariablesID;
    }
    
    public int getObjectVariableValue(final Object o) {
        final SolverVariable solverVariable = ((ConstraintAnchor)o).getSolverVariable();
        if (solverVariable != null) {
            return (int)(solverVariable.computedValue + 0.5f);
        }
        return 0;
    }
    
    ArrayRow getRow(final int n) {
        return this.mRows[n];
    }
    
    float getValueFor(final String s) {
        final SolverVariable variable = this.getVariable(s, SolverVariable.Type.UNRESTRICTED);
        if (variable == null) {
            return 0.0f;
        }
        return variable.computedValue;
    }
    
    SolverVariable getVariable(final String key, final SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        SolverVariable variable;
        if ((variable = this.mVariables.get(key)) == null) {
            variable = this.createVariable(key, type);
        }
        return variable;
    }
    
    public void minimize() throws Exception {
        this.minimizeGoal(this.mGoal);
    }
    
    void minimizeGoal(final Goal goal) throws Exception {
        goal.updateFromSystem(this);
        this.enforceBFS(goal);
        this.optimize(goal);
        this.computeValues();
    }
    
    void rebuildGoalFromErrors() {
        this.mGoal.updateFromSystem(this);
    }
    
    public void reset() {
        for (int i = 0; i < this.mCache.mIndexedVariables.length; ++i) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
            if (solverVariable != null) {
                solverVariable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        if (this.mVariables != null) {
            this.mVariables.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.variables.clear();
        this.mNumColumns = 1;
        for (int j = 0; j < this.mNumRows; ++j) {
            this.mRows[j].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }
}
