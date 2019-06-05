// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

public class AutoTransition extends TransitionSet
{
    public AutoTransition() {
        this.init();
    }
    
    private void init() {
        this.setOrdering(1);
        this.addTransition(new Fade(2)).addTransition(new ChangeBounds()).addTransition(new Fade(1));
    }
}
