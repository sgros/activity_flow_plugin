// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Collections;
import java.util.List;

public class AdaptationSet
{
    public final List<Descriptor> accessibilityDescriptors;
    public final int id;
    public final List<Representation> representations;
    public final List<Descriptor> supplementalProperties;
    public final int type;
    
    public AdaptationSet(final int id, final int type, final List<Representation> list, final List<Descriptor> list2, final List<Descriptor> list3) {
        this.id = id;
        this.type = type;
        this.representations = Collections.unmodifiableList((List<? extends Representation>)list);
        List<Descriptor> accessibilityDescriptors;
        if (list2 == null) {
            accessibilityDescriptors = Collections.emptyList();
        }
        else {
            accessibilityDescriptors = Collections.unmodifiableList((List<? extends Descriptor>)list2);
        }
        this.accessibilityDescriptors = accessibilityDescriptors;
        List<Descriptor> supplementalProperties;
        if (list3 == null) {
            supplementalProperties = Collections.emptyList();
        }
        else {
            supplementalProperties = Collections.unmodifiableList((List<? extends Descriptor>)list3);
        }
        this.supplementalProperties = supplementalProperties;
    }
}
