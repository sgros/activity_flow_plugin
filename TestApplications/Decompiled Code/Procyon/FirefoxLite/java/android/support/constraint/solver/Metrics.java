// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver;

import java.util.ArrayList;

public class Metrics
{
    public long additionalMeasures;
    public long barrierConnectionResolved;
    public long bfs;
    public long centerConnectionResolved;
    public long chainConnectionResolved;
    public long constraints;
    public long errors;
    public long extravariables;
    public long fullySolved;
    public long graphOptimizer;
    public long iterations;
    public long lastTableSize;
    public long matchConnectionResolved;
    public long maxRows;
    public long maxTableSize;
    public long maxVariables;
    public long measures;
    public long minimize;
    public long minimizeGoal;
    public long nonresolvedWidgets;
    public long oldresolvedWidgets;
    public long optimize;
    public long pivots;
    public ArrayList<String> problematicLayouts;
    public long resolutions;
    public long resolvedWidgets;
    public long simpleconstraints;
    public long slackvariables;
    public long tableSizeIncrease;
    public long variables;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\n*** Metrics ***\nmeasures: ");
        sb.append(this.measures);
        sb.append("\nadditionalMeasures: ");
        sb.append(this.additionalMeasures);
        sb.append("\nresolutions passes: ");
        sb.append(this.resolutions);
        sb.append("\ntable increases: ");
        sb.append(this.tableSizeIncrease);
        sb.append("\nmaxTableSize: ");
        sb.append(this.maxTableSize);
        sb.append("\nmaxVariables: ");
        sb.append(this.maxVariables);
        sb.append("\nmaxRows: ");
        sb.append(this.maxRows);
        sb.append("\n\nminimize: ");
        sb.append(this.minimize);
        sb.append("\nminimizeGoal: ");
        sb.append(this.minimizeGoal);
        sb.append("\nconstraints: ");
        sb.append(this.constraints);
        sb.append("\nsimpleconstraints: ");
        sb.append(this.simpleconstraints);
        sb.append("\noptimize: ");
        sb.append(this.optimize);
        sb.append("\niterations: ");
        sb.append(this.iterations);
        sb.append("\npivots: ");
        sb.append(this.pivots);
        sb.append("\nbfs: ");
        sb.append(this.bfs);
        sb.append("\nvariables: ");
        sb.append(this.variables);
        sb.append("\nerrors: ");
        sb.append(this.errors);
        sb.append("\nslackvariables: ");
        sb.append(this.slackvariables);
        sb.append("\nextravariables: ");
        sb.append(this.extravariables);
        sb.append("\nfullySolved: ");
        sb.append(this.fullySolved);
        sb.append("\ngraphOptimizer: ");
        sb.append(this.graphOptimizer);
        sb.append("\nresolvedWidgets: ");
        sb.append(this.resolvedWidgets);
        sb.append("\noldresolvedWidgets: ");
        sb.append(this.oldresolvedWidgets);
        sb.append("\nnonresolvedWidgets: ");
        sb.append(this.nonresolvedWidgets);
        sb.append("\ncenterConnectionResolved: ");
        sb.append(this.centerConnectionResolved);
        sb.append("\nmatchConnectionResolved: ");
        sb.append(this.matchConnectionResolved);
        sb.append("\nchainConnectionResolved: ");
        sb.append(this.chainConnectionResolved);
        sb.append("\nbarrierConnectionResolved: ");
        sb.append(this.barrierConnectionResolved);
        sb.append("\nproblematicsLayouts: ");
        sb.append(this.problematicLayouts);
        sb.append("\n");
        return sb.toString();
    }
}
