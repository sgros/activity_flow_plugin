// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.os;

import android.os.UserManager;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(24)
@RequiresApi(24)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class UserManagerCompatApi24
{
    public static boolean isUserUnlocked(final Context context) {
        return ((UserManager)context.getSystemService((Class)UserManager.class)).isUserUnlocked();
    }
}
