// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Collection;

public class TileStates
{
    private boolean mDone;
    private int mExpired;
    private int mNotFound;
    private Collection<Runnable> mRunAfters;
    private int mScaled;
    private int mTotal;
    private int mUpToDate;
    
    public TileStates() {
        this.mRunAfters = new LinkedHashSet<Runnable>();
    }
    
    public void finaliseLoop() {
        this.mDone = true;
        for (final Runnable runnable : this.mRunAfters) {
            if (runnable != null) {
                runnable.run();
            }
        }
    }
    
    public void handleTile(final Drawable drawable) {
        ++this.mTotal;
        if (drawable == null) {
            ++this.mNotFound;
        }
        else {
            final int state = ExpirableBitmapDrawable.getState(drawable);
            if (state != -4) {
                if (state != -3) {
                    if (state != -2) {
                        if (state != -1) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown state: ");
                            sb.append(state);
                            throw new IllegalArgumentException(sb.toString());
                        }
                        ++this.mUpToDate;
                    }
                    else {
                        ++this.mExpired;
                    }
                }
                else {
                    ++this.mScaled;
                }
            }
            else {
                ++this.mNotFound;
            }
        }
    }
    
    public void initialiseLoop() {
        this.mDone = false;
        this.mTotal = 0;
        this.mUpToDate = 0;
        this.mExpired = 0;
        this.mScaled = 0;
        this.mNotFound = 0;
    }
    
    @Override
    public String toString() {
        if (this.mDone) {
            final StringBuilder sb = new StringBuilder();
            sb.append("TileStates: ");
            sb.append(this.mTotal);
            sb.append(" = ");
            sb.append(this.mUpToDate);
            sb.append("(U) + ");
            sb.append(this.mExpired);
            sb.append("(E) + ");
            sb.append(this.mScaled);
            sb.append("(S) + ");
            sb.append(this.mNotFound);
            sb.append("(N)");
            return sb.toString();
        }
        return "TileStates";
    }
}
