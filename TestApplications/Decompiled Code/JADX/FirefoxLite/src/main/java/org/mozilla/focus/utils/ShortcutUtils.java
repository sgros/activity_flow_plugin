package org.mozilla.focus.utils;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.p001v4.content.p002pm.ShortcutInfoCompat;
import android.support.p001v4.content.p002pm.ShortcutInfoCompat.Builder;
import android.support.p001v4.content.p002pm.ShortcutManagerCompat;
import android.support.p001v4.graphics.drawable.IconCompat;
import android.text.TextUtils;
import java.util.ArrayList;
import org.mozilla.icon.FavIconUtils;

public class ShortcutUtils {
    public static void requestPinShortcut(Context context, Intent intent, String str, String str2, Bitmap bitmap) {
        CharSequence str3;
        Resources resources = context.getResources();
        char representativeCharacter = FavIconUtils.getRepresentativeCharacter(str2);
        if (bitmap == null) {
            bitmap = DimenUtils.getInitialBitmap(resources, null, representativeCharacter);
        } else {
            bitmap = DimenUtils.getRefinedShortcutIcon(resources, bitmap, representativeCharacter);
        }
        if (TextUtils.isEmpty(str3)) {
            str3 = str2;
        }
        ShortcutInfoCompat build = new Builder(context, str2).setShortLabel(str3).setIcon(IconCompat.createWithBitmap(bitmap)).setIntent(intent).build();
        Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.addCategory("android.intent.category.HOME");
        intent2.setFlags(268435456);
        IntentSender intentSender = PendingIntent.getActivity(context, 0, intent2, 134217728).getIntentSender();
        updateShortcut26(context, build);
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            ShortcutManagerCompat.requestPinShortcut(context, build, intentSender);
        }
    }

    @TargetApi(26)
    private static void updateShortcut26(Context context, ShortcutInfoCompat shortcutInfoCompat) {
        if (VERSION.SDK_INT >= 26) {
            ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(ShortcutManager.class);
            if (shortcutManager != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(shortcutInfoCompat.toShortcutInfo());
                shortcutManager.updateShortcuts(arrayList);
            }
        }
    }
}
