// 
// Decompiled by Procyon v0.5.34
// 

package android.support.constraint.solver.widgets;

import java.util.Iterator;
import java.util.HashSet;

public class ResolutionNode
{
    HashSet<ResolutionNode> dependents;
    int state;
    
    public ResolutionNode() {
        this.dependents = new HashSet<ResolutionNode>(2);
        this.state = 0;
    }
    
    public void addDependent(final ResolutionNode e) {
        this.dependents.add(e);
    }
    
    public void didResolve() {
        this.state = 1;
        final Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().resolve();
        }
    }
    
    public void invalidate() {
        this.state = 0;
        final Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidate();
        }
    }
    
    public boolean isResolved() {
        final int state = this.state;
        boolean b = true;
        if (state != 1) {
            b = false;
        }
        return b;
    }
    
    public void reset() {
        this.state = 0;
        this.dependents.clear();
    }
    
    public void resolve() {
    }
}
