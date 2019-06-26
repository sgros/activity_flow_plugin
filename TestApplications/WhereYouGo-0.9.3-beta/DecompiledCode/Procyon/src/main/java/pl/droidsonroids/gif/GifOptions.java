// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import pl.droidsonroids.gif.annotations.Beta;

@Beta
public class GifOptions
{
    boolean inIsOpaque;
    char inSampleSize;
    
    public GifOptions() {
        this.reset();
    }
    
    private void reset() {
        this.inSampleSize = 1;
        this.inIsOpaque = false;
    }
    
    void setFrom(@Nullable final GifOptions gifOptions) {
        if (gifOptions == null) {
            this.reset();
        }
        else {
            this.inIsOpaque = gifOptions.inIsOpaque;
            this.inSampleSize = gifOptions.inSampleSize;
        }
    }
    
    public void setInIsOpaque(final boolean inIsOpaque) {
        this.inIsOpaque = inIsOpaque;
    }
    
    public void setInSampleSize(@IntRange(from = 1L, to = 65535L) final int n) {
        if (n < 1 || n > 65535) {
            this.inSampleSize = 1;
        }
        else {
            this.inSampleSize = (char)n;
        }
    }
}
