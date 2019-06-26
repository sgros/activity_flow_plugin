package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ExpirableBitmapDrawable extends BitmapDrawable {
    private static final int[] settableStatuses = new int[]{-2, -3, -4};
    private int[] mState = new int[0];

    public ExpirableBitmapDrawable(Bitmap bitmap) {
        super(bitmap);
    }

    public int[] getState() {
        return this.mState;
    }

    public boolean isStateful() {
        return this.mState.length > 0;
    }

    public boolean setState(int[] iArr) {
        this.mState = iArr;
        return true;
    }

    public static int getState(Drawable drawable) {
        for (int i : drawable.getState()) {
            for (int i2 : settableStatuses) {
                if (i == i2) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void setState(Drawable drawable, int i) {
        drawable.setState(new int[]{i});
    }
}
