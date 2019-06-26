// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.metadata.emsg.EventMessage;

public final class EventStream
{
    public final EventMessage[] events;
    public final long[] presentationTimesUs;
    public final String schemeIdUri;
    public final long timescale;
    public final String value;
    
    public EventStream(final String schemeIdUri, final String value, final long timescale, final long[] presentationTimesUs, final EventMessage[] events) {
        this.schemeIdUri = schemeIdUri;
        this.value = value;
        this.timescale = timescale;
        this.presentationTimesUs = presentationTimesUs;
        this.events = events;
    }
    
    public String id() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.schemeIdUri);
        sb.append("/");
        sb.append(this.value);
        return sb.toString();
    }
}
