// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.m4a;

import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.PositionInputStream;

public final class MP4Input extends MP4Box<PositionInputStream>
{
    public MP4Input(final InputStream inputStream) {
        super(new PositionInputStream(inputStream), null, "");
    }
    
    public MP4Atom nextChildUpTo(final String regex) throws IOException {
        MP4Atom nextChild;
        do {
            nextChild = this.nextChild();
        } while (!nextChild.getType().matches(regex));
        return nextChild;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("mp4[pos=");
        sb.append(this.getPosition());
        sb.append("]");
        return sb.toString();
    }
}
