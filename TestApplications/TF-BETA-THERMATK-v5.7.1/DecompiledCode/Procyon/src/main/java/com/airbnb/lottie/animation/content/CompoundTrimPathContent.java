// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.utils.Utils;
import android.graphics.Path;
import java.util.ArrayList;
import java.util.List;

public class CompoundTrimPathContent
{
    private List<TrimPathContent> contents;
    
    public CompoundTrimPathContent() {
        this.contents = new ArrayList<TrimPathContent>();
    }
    
    void addTrimPath(final TrimPathContent trimPathContent) {
        this.contents.add(trimPathContent);
    }
    
    public void apply(final Path path) {
        for (int i = this.contents.size() - 1; i >= 0; --i) {
            Utils.applyTrimPathIfNeeded(path, this.contents.get(i));
        }
    }
}
