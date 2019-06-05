package android.support.p000v4.p002os;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.UserManager;

/* renamed from: android.support.v4.os.UserManagerCompat */
public class UserManagerCompat {
    private UserManagerCompat() {
    }

    public static boolean isUserUnlocked(Context context) {
        return VERSION.SDK_INT >= 24 ? ((UserManager) context.getSystemService(UserManager.class)).isUserUnlocked() : true;
    }
}
