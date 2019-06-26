// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.cea;

import android.text.Layout$Alignment;
import com.google.android.exoplayer2.text.Cue;

final class Cea708Cue extends Cue implements Comparable<Cea708Cue>
{
    public final int priority;
    
    public Cea708Cue(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final float n, final int n2, final int n3, final float n4, final int n5, final float n6, final boolean b, final int n7, final int priority) {
        super(charSequence, layout$Alignment, n, n2, n3, n4, n5, n6, b, n7);
        this.priority = priority;
    }
    
    @Override
    public int compareTo(final Cea708Cue cea708Cue) {
        final int priority = cea708Cue.priority;
        final int priority2 = this.priority;
        if (priority < priority2) {
            return -1;
        }
        if (priority > priority2) {
            return 1;
        }
        return 0;
    }
}
