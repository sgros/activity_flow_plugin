// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.accessibilityservice;

import android.content.pm.PackageManager;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class AccessibilityServiceInfoCompatJellyBean
{
    public static String loadDescription(final AccessibilityServiceInfo accessibilityServiceInfo, final PackageManager packageManager) {
        return accessibilityServiceInfo.loadDescription(packageManager);
    }
}
