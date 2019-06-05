// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import java.util.Arrays;

public class Helper extends ConstraintWidget
{
    protected ConstraintWidget[] mWidgets;
    protected int mWidgetsCount;
    
    public Helper() {
        this.mWidgets = new ConstraintWidget[4];
        this.mWidgetsCount = 0;
    }
    
    public void add(final ConstraintWidget constraintWidget) {
        if (this.mWidgetsCount + 1 > this.mWidgets.length) {
            this.mWidgets = Arrays.copyOf(this.mWidgets, this.mWidgets.length * 2);
        }
        this.mWidgets[this.mWidgetsCount] = constraintWidget;
        ++this.mWidgetsCount;
    }
    
    public void removeAllIds() {
        this.mWidgetsCount = 0;
    }
}
