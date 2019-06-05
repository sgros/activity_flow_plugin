// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.util.Iterator;
import java.util.HashMap;
import android.view.View;
import java.util.Map;
import java.util.ArrayList;

public class TransitionValues
{
    final ArrayList<Transition> mTargetedTransitions;
    public final Map<String, Object> values;
    public View view;
    
    public TransitionValues() {
        this.values = new HashMap<String, Object>();
        this.mTargetedTransitions = new ArrayList<Transition>();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof TransitionValues) {
            final View view = this.view;
            final TransitionValues transitionValues = (TransitionValues)o;
            if (view == transitionValues.view && this.values.equals(transitionValues.values)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.view.hashCode() * 31 + this.values.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TransitionValues@");
        sb.append(Integer.toHexString(this.hashCode()));
        sb.append(":\n");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append("    view = ");
        sb2.append(this.view);
        sb2.append("\n");
        final String string2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string2);
        sb3.append("    values:");
        String str = sb3.toString();
        for (final String str2 : this.values.keySet()) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append("    ");
            sb4.append(str2);
            sb4.append(": ");
            sb4.append(this.values.get(str2));
            sb4.append("\n");
            str = sb4.toString();
        }
        return str;
    }
}
