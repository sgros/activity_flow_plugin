package android.support.constraint.solver;

import java.util.ArrayList;

public class Metrics {
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

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n*** Metrics ***\nmeasures: ");
        stringBuilder.append(this.measures);
        stringBuilder.append("\nadditionalMeasures: ");
        stringBuilder.append(this.additionalMeasures);
        stringBuilder.append("\nresolutions passes: ");
        stringBuilder.append(this.resolutions);
        stringBuilder.append("\ntable increases: ");
        stringBuilder.append(this.tableSizeIncrease);
        stringBuilder.append("\nmaxTableSize: ");
        stringBuilder.append(this.maxTableSize);
        stringBuilder.append("\nmaxVariables: ");
        stringBuilder.append(this.maxVariables);
        stringBuilder.append("\nmaxRows: ");
        stringBuilder.append(this.maxRows);
        stringBuilder.append("\n\nminimize: ");
        stringBuilder.append(this.minimize);
        stringBuilder.append("\nminimizeGoal: ");
        stringBuilder.append(this.minimizeGoal);
        stringBuilder.append("\nconstraints: ");
        stringBuilder.append(this.constraints);
        stringBuilder.append("\nsimpleconstraints: ");
        stringBuilder.append(this.simpleconstraints);
        stringBuilder.append("\noptimize: ");
        stringBuilder.append(this.optimize);
        stringBuilder.append("\niterations: ");
        stringBuilder.append(this.iterations);
        stringBuilder.append("\npivots: ");
        stringBuilder.append(this.pivots);
        stringBuilder.append("\nbfs: ");
        stringBuilder.append(this.bfs);
        stringBuilder.append("\nvariables: ");
        stringBuilder.append(this.variables);
        stringBuilder.append("\nerrors: ");
        stringBuilder.append(this.errors);
        stringBuilder.append("\nslackvariables: ");
        stringBuilder.append(this.slackvariables);
        stringBuilder.append("\nextravariables: ");
        stringBuilder.append(this.extravariables);
        stringBuilder.append("\nfullySolved: ");
        stringBuilder.append(this.fullySolved);
        stringBuilder.append("\ngraphOptimizer: ");
        stringBuilder.append(this.graphOptimizer);
        stringBuilder.append("\nresolvedWidgets: ");
        stringBuilder.append(this.resolvedWidgets);
        stringBuilder.append("\noldresolvedWidgets: ");
        stringBuilder.append(this.oldresolvedWidgets);
        stringBuilder.append("\nnonresolvedWidgets: ");
        stringBuilder.append(this.nonresolvedWidgets);
        stringBuilder.append("\ncenterConnectionResolved: ");
        stringBuilder.append(this.centerConnectionResolved);
        stringBuilder.append("\nmatchConnectionResolved: ");
        stringBuilder.append(this.matchConnectionResolved);
        stringBuilder.append("\nchainConnectionResolved: ");
        stringBuilder.append(this.chainConnectionResolved);
        stringBuilder.append("\nbarrierConnectionResolved: ");
        stringBuilder.append(this.barrierConnectionResolved);
        stringBuilder.append("\nproblematicsLayouts: ");
        stringBuilder.append(this.problematicLayouts);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}