// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem
{
    private static int POOL_SIZE = 1000;
    public static Metrics sMetrics;
    private int TABLE_SIZE;
    public boolean graphOptimizer;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Row mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    ArrayRow[] mRows;
    private final Row mTempGoal;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    private ArrayRow[] tempClientsCopy;
    
    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.TABLE_SIZE = 32;
        this.mMaxColumns = this.TABLE_SIZE;
        this.mRows = null;
        this.graphOptimizer = false;
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
        this.mGoal = (Row)new GoalRow(this.mCache);
        this.mTempGoal = (Row)new ArrayRow(this.mCache);
    }
    
    private SolverVariable acquireSolverVariable(final SolverVariable.Type type, final String s) {
        final SolverVariable solverVariable = this.mCache.solverVariablePool.acquire();
        SolverVariable solverVariable3;
        if (solverVariable == null) {
            final SolverVariable solverVariable2 = new SolverVariable(type, s);
            solverVariable2.setType(type, s);
            solverVariable3 = solverVariable2;
        }
        else {
            solverVariable.reset();
            solverVariable.setType(type, s);
            solverVariable3 = solverVariable;
        }
        if (this.mPoolVariablesCount >= LinearSystem.POOL_SIZE) {
            LinearSystem.POOL_SIZE *= 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, LinearSystem.POOL_SIZE);
        }
        return this.mPoolVariables[this.mPoolVariablesCount++] = solverVariable3;
    }
    
    private void addError(final ArrayRow arrayRow) {
        arrayRow.addError(this, 0);
    }
    
    private final void addRow(final ArrayRow arrayRow) {
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        this.mRows[this.mNumRows] = arrayRow;
        arrayRow.variable.definitionId = this.mNumRows;
        ++this.mNumRows;
        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
    }
    
    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            final ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }
    
    public static ArrayRow createRowDimensionPercent(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        if (b) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, n);
    }
    
    private int enforceBFS(final Row row) throws Exception {
        while (true) {
            for (int i = 0; i < this.mNumRows; ++i) {
                if (this.mRows[i].variable.mType != SolverVariable.Type.UNRESTRICTED) {
                    if (this.mRows[i].constantValue < 0.0f) {
                        final boolean b = true;
                        int n;
                        if (b) {
                            int j = 0;
                            n = 0;
                            while (j == 0) {
                                if (LinearSystem.sMetrics != null) {
                                    final Metrics sMetrics = LinearSystem.sMetrics;
                                    ++sMetrics.bfs;
                                }
                                final int n2 = n + 1;
                                int k = 0;
                                int definitionId = -1;
                                int n3 = -1;
                                float n4 = Float.MAX_VALUE;
                                int n5 = 0;
                                while (k < this.mNumRows) {
                                    final ArrayRow arrayRow = this.mRows[k];
                                    int n6;
                                    int n7;
                                    float n8;
                                    int n9;
                                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                                        n6 = definitionId;
                                        n7 = n3;
                                        n8 = n4;
                                        n9 = n5;
                                    }
                                    else if (arrayRow.isSimpleDefinition) {
                                        n6 = definitionId;
                                        n7 = n3;
                                        n8 = n4;
                                        n9 = n5;
                                    }
                                    else {
                                        n6 = definitionId;
                                        n7 = n3;
                                        n8 = n4;
                                        n9 = n5;
                                        if (arrayRow.constantValue < 0.0f) {
                                            int n10 = 1;
                                            while (true) {
                                                n6 = definitionId;
                                                n7 = n3;
                                                n8 = n4;
                                                n9 = n5;
                                                if (n10 >= this.mNumColumns) {
                                                    break;
                                                }
                                                final SolverVariable solverVariable = this.mCache.mIndexedVariables[n10];
                                                final float value = arrayRow.variables.get(solverVariable);
                                                if (value > 0.0f) {
                                                    final int n11 = n5;
                                                    final int n12 = 0;
                                                    int n13 = n3;
                                                    int n14 = definitionId;
                                                    int l = n12;
                                                    int n15 = n11;
                                                    while (l < 7) {
                                                        final float n16 = solverVariable.strengthVector[l] / value;
                                                        int n17;
                                                        if ((n16 < n4 && l == n15) || l > (n17 = n15)) {
                                                            n13 = n10;
                                                            n14 = k;
                                                            n4 = n16;
                                                            n17 = l;
                                                        }
                                                        ++l;
                                                        n15 = n17;
                                                    }
                                                    n3 = n13;
                                                    n5 = n15;
                                                    definitionId = n14;
                                                }
                                                ++n10;
                                            }
                                        }
                                    }
                                    ++k;
                                    definitionId = n6;
                                    n3 = n7;
                                    n4 = n8;
                                    n5 = n9;
                                }
                                if (definitionId != -1) {
                                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                                    arrayRow2.variable.definitionId = -1;
                                    if (LinearSystem.sMetrics != null) {
                                        final Metrics sMetrics2 = LinearSystem.sMetrics;
                                        ++sMetrics2.pivots;
                                    }
                                    arrayRow2.pivot(this.mCache.mIndexedVariables[n3]);
                                    arrayRow2.variable.definitionId = definitionId;
                                    arrayRow2.variable.updateReferencesWithNewDefinition(arrayRow2);
                                }
                                else {
                                    j = 1;
                                }
                                if (n2 > this.mNumColumns / 2) {
                                    j = 1;
                                }
                                n = n2;
                            }
                        }
                        else {
                            n = 0;
                        }
                        return n;
                    }
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    public static Metrics getMetrics() {
        return LinearSystem.sMetrics;
    }
    
    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        this.mCache.mIndexedVariables = Arrays.copyOf(this.mCache.mIndexedVariables, this.TABLE_SIZE);
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mMaxColumns = this.TABLE_SIZE;
        this.mMaxRows = this.TABLE_SIZE;
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.tableSizeIncrease;
            LinearSystem.sMetrics.maxTableSize = Math.max(LinearSystem.sMetrics.maxTableSize, this.TABLE_SIZE);
            LinearSystem.sMetrics.lastTableSize = LinearSystem.sMetrics.maxTableSize;
        }
    }
    
    private final int optimize(final Row row, final boolean b) {
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.optimize;
        }
        for (int i = 0; i < this.mNumColumns; ++i) {
            this.mAlreadyTestedCandidates[i] = false;
        }
        int j = 0;
        int n = 0;
        while (j == 0) {
            if (LinearSystem.sMetrics != null) {
                final Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.iterations;
            }
            final int n2 = n + 1;
            if (n2 >= this.mNumColumns * 2) {
                return n2;
            }
            if (row.getKey() != null) {
                this.mAlreadyTestedCandidates[row.getKey().id] = true;
            }
            final SolverVariable pivotCandidate = row.getPivotCandidate(this, this.mAlreadyTestedCandidates);
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.id]) {
                    return n2;
                }
                this.mAlreadyTestedCandidates[pivotCandidate.id] = true;
            }
            if (pivotCandidate != null) {
                int k = 0;
                int definitionId = -1;
                float n3 = Float.MAX_VALUE;
                while (k < this.mNumRows) {
                    final ArrayRow arrayRow = this.mRows[k];
                    int n4;
                    float n5;
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        n4 = definitionId;
                        n5 = n3;
                    }
                    else if (arrayRow.isSimpleDefinition) {
                        n4 = definitionId;
                        n5 = n3;
                    }
                    else {
                        n4 = definitionId;
                        n5 = n3;
                        if (arrayRow.hasVariable(pivotCandidate)) {
                            final float value = arrayRow.variables.get(pivotCandidate);
                            n4 = definitionId;
                            n5 = n3;
                            if (value < 0.0f) {
                                final float n6 = -arrayRow.constantValue / value;
                                n4 = definitionId;
                                n5 = n3;
                                if (n6 < n3) {
                                    n4 = k;
                                    n5 = n6;
                                }
                            }
                        }
                    }
                    ++k;
                    definitionId = n4;
                    n3 = n5;
                }
                if (definitionId > -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    if (LinearSystem.sMetrics != null) {
                        final Metrics sMetrics3 = LinearSystem.sMetrics;
                        ++sMetrics3.pivots;
                    }
                    arrayRow2.pivot(pivotCandidate);
                    arrayRow2.variable.definitionId = definitionId;
                    arrayRow2.variable.updateReferencesWithNewDefinition(arrayRow2);
                    n = n2;
                    continue;
                }
            }
            j = 1;
            n = n2;
        }
        return n;
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
    
    private final void updateRowFromVariables(final ArrayRow arrayRow) {
        if (this.mNumRows > 0) {
            arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
            if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
            }
        }
    }
    
    public void addCenterPoint(final ConstraintWidget constraintWidget, final ConstraintWidget constraintWidget2, final float n, final int n2) {
        final SolverVariable objectVariable = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT));
        final SolverVariable objectVariable2 = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP));
        final SolverVariable objectVariable3 = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT));
        final SolverVariable objectVariable4 = this.createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM));
        final SolverVariable objectVariable5 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT));
        final SolverVariable objectVariable6 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP));
        final SolverVariable objectVariable7 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT));
        final SolverVariable objectVariable8 = this.createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM));
        final ArrayRow row = this.createRow();
        final double n3 = n;
        final double sin = Math.sin(n3);
        final double n4 = n2;
        row.createRowWithAngle(objectVariable2, objectVariable4, objectVariable6, objectVariable8, (float)(sin * n4));
        this.addConstraint(row);
        final ArrayRow row2 = this.createRow();
        row2.createRowWithAngle(objectVariable, objectVariable3, objectVariable5, objectVariable7, (float)(Math.cos(n3) * n4));
        this.addConstraint(row2);
    }
    
    public void addCentering(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final float n2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final int n3, final int n4) {
        final ArrayRow row = this.createRow();
        row.createRowCentering(solverVariable, solverVariable2, n, n2, solverVariable3, solverVariable4, n3);
        if (n4 != 6) {
            row.addError(this, n4);
        }
        this.addConstraint(row);
    }
    
    public void addConstraint(final ArrayRow arrayRow) {
        if (arrayRow == null) {
            return;
        }
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.constraints;
            if (arrayRow.isSimpleDefinition) {
                final Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.simpleconstraints;
            }
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        int n = 0;
        final int n2 = 0;
        if (!arrayRow.isSimpleDefinition) {
            this.updateRowFromVariables(arrayRow);
            if (arrayRow.isEmpty()) {
                return;
            }
            arrayRow.ensurePositiveConstant();
            n = n2;
            if (arrayRow.chooseSubject(this)) {
                final SolverVariable extraVariable = this.createExtraVariable();
                arrayRow.variable = extraVariable;
                this.addRow(arrayRow);
                this.mTempGoal.initFromRow((Row)arrayRow);
                this.optimize(this.mTempGoal, true);
                if (extraVariable.definitionId == -1) {
                    if (arrayRow.variable == extraVariable) {
                        final SolverVariable pickPivot = arrayRow.pickPivot(extraVariable);
                        if (pickPivot != null) {
                            if (LinearSystem.sMetrics != null) {
                                final Metrics sMetrics3 = LinearSystem.sMetrics;
                                ++sMetrics3.pivots;
                            }
                            arrayRow.pivot(pickPivot);
                        }
                    }
                    if (!arrayRow.isSimpleDefinition) {
                        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
                    }
                    --this.mNumRows;
                }
                n = 1;
            }
            if (!arrayRow.hasKeyVariable()) {
                return;
            }
        }
        if (n == 0) {
            this.addRow(arrayRow);
        }
    }
    
    public ArrayRow addEquality(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        row.createRowEquals(solverVariable, solverVariable2, n);
        if (n2 != 6) {
            row.addError(this, n2);
        }
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
            else if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
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
    
    public void addGreaterBarrier(final SolverVariable solverVariable, final SolverVariable solverVariable2, final boolean b) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, slackVariable.strength = 0);
        if (b) {
            this.addSingleError(row, (int)(row.variables.get(slackVariable) * -1.0f), 1);
        }
        this.addConstraint(row);
    }
    
    public void addGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = 0;
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        if (n2 != 6) {
            this.addSingleError(row, (int)(row.variables.get(slackVariable) * -1.0f), n2);
        }
        this.addConstraint(row);
    }
    
    public void addLowerBarrier(final SolverVariable solverVariable, final SolverVariable solverVariable2, final boolean b) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, slackVariable.strength = 0);
        if (b) {
            this.addSingleError(row, (int)(row.variables.get(slackVariable) * -1.0f), 1);
        }
        this.addConstraint(row);
    }
    
    public void addLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = 0;
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        if (n2 != 6) {
            this.addSingleError(row, (int)(row.variables.get(slackVariable) * -1.0f), n2);
        }
        this.addConstraint(row);
    }
    
    public void addRatio(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final float n, final int n2) {
        final ArrayRow row = this.createRow();
        row.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable3, solverVariable4, n);
        if (n2 != 6) {
            row.addError(this, n2);
        }
        this.addConstraint(row);
    }
    
    void addSingleError(final ArrayRow arrayRow, final int n, final int n2) {
        arrayRow.addSingleError(this.createErrorVariable(n2, null), n);
    }
    
    public SolverVariable createErrorVariable(final int strength, final String s) {
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.errors;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.ERROR, s);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        acquireSolverVariable.strength = strength;
        this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
        this.mGoal.addError(acquireSolverVariable);
        return acquireSolverVariable;
    }
    
    public SolverVariable createExtraVariable() {
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.extravariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
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
        SolverVariable.increaseErrorId();
        return arrayRow;
    }
    
    public SolverVariable createSlackVariable() {
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.slackvariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    public Cache getCache() {
        return this.mCache;
    }
    
    public int getObjectVariableValue(final Object o) {
        final SolverVariable solverVariable = ((ConstraintAnchor)o).getSolverVariable();
        if (solverVariable != null) {
            return (int)(solverVariable.computedValue + 0.5f);
        }
        return 0;
    }
    
    public void minimize() throws Exception {
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.minimize;
        }
        if (this.graphOptimizer) {
            if (LinearSystem.sMetrics != null) {
                final Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.graphOptimizer;
            }
            final int n = 0;
            int i = 0;
            while (true) {
                while (i < this.mNumRows) {
                    if (!this.mRows[i].isSimpleDefinition) {
                        final int n2 = n;
                        if (n2 == 0) {
                            this.minimizeGoal(this.mGoal);
                            return;
                        }
                        if (LinearSystem.sMetrics != null) {
                            final Metrics sMetrics3 = LinearSystem.sMetrics;
                            ++sMetrics3.fullySolved;
                        }
                        this.computeValues();
                        return;
                    }
                    else {
                        ++i;
                    }
                }
                final int n2 = 1;
                continue;
            }
        }
        this.minimizeGoal(this.mGoal);
    }
    
    void minimizeGoal(final Row row) throws Exception {
        if (LinearSystem.sMetrics != null) {
            final Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.minimizeGoal;
            LinearSystem.sMetrics.maxVariables = Math.max(LinearSystem.sMetrics.maxVariables, this.mNumColumns);
            LinearSystem.sMetrics.maxRows = Math.max(LinearSystem.sMetrics.maxRows, this.mNumRows);
        }
        this.updateRowFromVariables((ArrayRow)row);
        this.enforceBFS(row);
        this.optimize(row, false);
        this.computeValues();
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
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (int j = 0; j < this.mNumRows; ++j) {
            this.mRows[j].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }
    
    interface Row
    {
        void addError(final SolverVariable p0);
        
        void clear();
        
        SolverVariable getKey();
        
        SolverVariable getPivotCandidate(final LinearSystem p0, final boolean[] p1);
        
        void initFromRow(final Row p0);
    }
}
