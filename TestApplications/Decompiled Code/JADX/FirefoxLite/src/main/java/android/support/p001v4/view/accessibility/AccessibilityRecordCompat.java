package android.support.p001v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityRecord;

/* renamed from: android.support.v4.view.accessibility.AccessibilityRecordCompat */
public class AccessibilityRecordCompat {
    private final AccessibilityRecord mRecord;

    public static void setMaxScrollX(AccessibilityRecord accessibilityRecord, int i) {
        if (VERSION.SDK_INT >= 15) {
            accessibilityRecord.setMaxScrollX(i);
        }
    }

    public static void setMaxScrollY(AccessibilityRecord accessibilityRecord, int i) {
        if (VERSION.SDK_INT >= 15) {
            accessibilityRecord.setMaxScrollY(i);
        }
    }

    @Deprecated
    public int hashCode() {
        return this.mRecord == null ? 0 : this.mRecord.hashCode();
    }

    @Deprecated
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccessibilityRecordCompat accessibilityRecordCompat = (AccessibilityRecordCompat) obj;
        if (this.mRecord == null) {
            if (accessibilityRecordCompat.mRecord != null) {
                return false;
            }
        } else if (!this.mRecord.equals(accessibilityRecordCompat.mRecord)) {
            return false;
        }
        return true;
    }
}
