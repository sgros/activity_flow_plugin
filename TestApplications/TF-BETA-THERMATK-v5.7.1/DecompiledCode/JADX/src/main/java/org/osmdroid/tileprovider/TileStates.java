package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class TileStates {
    private boolean mDone;
    private int mExpired;
    private int mNotFound;
    private Collection<Runnable> mRunAfters = new LinkedHashSet();
    private int mScaled;
    private int mTotal;
    private int mUpToDate;

    public void initialiseLoop() {
        this.mDone = false;
        this.mTotal = 0;
        this.mUpToDate = 0;
        this.mExpired = 0;
        this.mScaled = 0;
        this.mNotFound = 0;
    }

    public void finaliseLoop() {
        this.mDone = true;
        for (Runnable runnable : this.mRunAfters) {
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public void handleTile(Drawable drawable) {
        this.mTotal++;
        if (drawable == null) {
            this.mNotFound++;
            return;
        }
        int state = ExpirableBitmapDrawable.getState(drawable);
        if (state == -4) {
            this.mNotFound++;
        } else if (state == -3) {
            this.mScaled++;
        } else if (state == -2) {
            this.mExpired++;
        } else if (state == -1) {
            this.mUpToDate++;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown state: ");
            stringBuilder.append(state);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public String toString() {
        if (!this.mDone) {
            return "TileStates";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TileStates: ");
        stringBuilder.append(this.mTotal);
        stringBuilder.append(" = ");
        stringBuilder.append(this.mUpToDate);
        stringBuilder.append("(U) + ");
        stringBuilder.append(this.mExpired);
        stringBuilder.append("(E) + ");
        stringBuilder.append(this.mScaled);
        stringBuilder.append("(S) + ");
        stringBuilder.append(this.mNotFound);
        stringBuilder.append("(N)");
        return stringBuilder.toString();
    }
}
