package p008pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.p000v4.internal.view.SupportMenu;
import p008pl.droidsonroids.gif.annotations.Beta;

@Beta
/* renamed from: pl.droidsonroids.gif.GifOptions */
public class GifOptions {
    boolean inIsOpaque;
    char inSampleSize;

    public GifOptions() {
        reset();
    }

    private void reset() {
        this.inSampleSize = 1;
        this.inIsOpaque = false;
    }

    public void setInSampleSize(@IntRange(from = 1, to = 65535) int inSampleSize) {
        if (inSampleSize < 1 || inSampleSize > SupportMenu.USER_MASK) {
            this.inSampleSize = 1;
        } else {
            this.inSampleSize = (char) inSampleSize;
        }
    }

    public void setInIsOpaque(boolean inIsOpaque) {
        this.inIsOpaque = inIsOpaque;
    }

    /* Access modifiers changed, original: 0000 */
    public void setFrom(@Nullable GifOptions source) {
        if (source == null) {
            reset();
            return;
        }
        this.inIsOpaque = source.inIsOpaque;
        this.inSampleSize = source.inSampleSize;
    }
}
