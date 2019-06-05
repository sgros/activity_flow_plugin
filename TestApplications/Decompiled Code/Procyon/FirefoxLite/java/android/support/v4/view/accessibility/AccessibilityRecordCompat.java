// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view.accessibility;

import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityRecord;

public class AccessibilityRecordCompat
{
    private final AccessibilityRecord mRecord;
    
    public static void setMaxScrollX(final AccessibilityRecord accessibilityRecord, final int maxScrollX) {
        if (Build$VERSION.SDK_INT >= 15) {
            accessibilityRecord.setMaxScrollX(maxScrollX);
        }
    }
    
    public static void setMaxScrollY(final AccessibilityRecord accessibilityRecord, final int maxScrollY) {
        if (Build$VERSION.SDK_INT >= 15) {
            accessibilityRecord.setMaxScrollY(maxScrollY);
        }
    }
    
    @Deprecated
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final AccessibilityRecordCompat accessibilityRecordCompat = (AccessibilityRecordCompat)o;
        if (this.mRecord == null) {
            if (accessibilityRecordCompat.mRecord != null) {
                return false;
            }
        }
        else if (!this.mRecord.equals(accessibilityRecordCompat.mRecord)) {
            return false;
        }
        return true;
    }
    
    @Deprecated
    @Override
    public int hashCode() {
        int hashCode;
        if (this.mRecord == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.mRecord.hashCode();
        }
        return hashCode;
    }
}
