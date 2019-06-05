// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.annotation.TargetApi;
import java.util.List;
import android.content.pm.ShortcutInfo;
import java.util.ArrayList;
import android.content.pm.ShortcutManager;
import android.os.Build$VERSION;
import android.content.IntentSender;
import android.content.res.Resources;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.app.PendingIntent;
import android.support.v4.graphics.drawable.IconCompat;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.text.TextUtils;
import org.mozilla.icon.FavIconUtils;
import android.graphics.Bitmap;
import android.content.Intent;
import android.content.Context;

public class ShortcutUtils
{
    public static void requestPinShortcut(final Context context, final Intent intent, final String s, final String s2, Bitmap bitmap) {
        final Resources resources = context.getResources();
        final char representativeCharacter = FavIconUtils.getRepresentativeCharacter(s2);
        if (bitmap == null) {
            bitmap = DimenUtils.getInitialBitmap(resources, null, representativeCharacter);
        }
        else {
            bitmap = DimenUtils.getRefinedShortcutIcon(resources, bitmap, representativeCharacter);
        }
        String shortLabel = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            shortLabel = s2;
        }
        final ShortcutInfoCompat build = new ShortcutInfoCompat.Builder(context, s2).setShortLabel(shortLabel).setIcon(IconCompat.createWithBitmap(bitmap)).setIntent(intent).build();
        final Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.addCategory("android.intent.category.HOME");
        intent2.setFlags(268435456);
        final IntentSender intentSender = PendingIntent.getActivity(context, 0, intent2, 134217728).getIntentSender();
        updateShortcut26(context, build);
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            ShortcutManagerCompat.requestPinShortcut(context, build, intentSender);
        }
    }
    
    @TargetApi(26)
    private static void updateShortcut26(final Context context, final ShortcutInfoCompat shortcutInfoCompat) {
        if (Build$VERSION.SDK_INT >= 26) {
            final ShortcutManager shortcutManager = (ShortcutManager)context.getSystemService((Class)ShortcutManager.class);
            if (shortcutManager != null) {
                final ArrayList<ShortcutInfo> list = new ArrayList<ShortcutInfo>();
                list.add(shortcutInfoCompat.toShortcutInfo());
                shortcutManager.updateShortcuts((List)list);
            }
        }
    }
}
