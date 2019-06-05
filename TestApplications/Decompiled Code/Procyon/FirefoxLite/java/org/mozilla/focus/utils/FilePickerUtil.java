// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcelable;
import java.util.Iterator;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;

public class FilePickerUtil
{
    private static void addActivities(final Context context, final Intent intent, final HashMap<String, Intent> hashMap, final HashMap<String, Intent> hashMap2) {
        if (hashMap2 == null) {
            return;
        }
        for (final ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            final ComponentName component = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name);
            if (!hashMap2.containsKey(component.toString())) {
                final Intent value = new Intent(intent);
                value.setComponent(component);
                hashMap.put(component.toString(), value);
            }
        }
    }
    
    private static Intent createIntent(final String type) {
        final Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType(type);
        intent.addCategory("android.intent.category.OPENABLE");
        return intent;
    }
    
    public static Intent getFilePickerIntent(final Context context, final CharSequence charSequence, final String[] array) {
        final List<Intent> intentsForFilePicker = getIntentsForFilePicker(context, array);
        if (intentsForFilePicker.size() == 0) {
            return null;
        }
        final Intent intent = intentsForFilePicker.remove(0);
        if (intentsForFilePicker.size() == 0) {
            return intent;
        }
        final Intent chooser = Intent.createChooser(intent, charSequence);
        chooser.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[])intentsForFilePicker.toArray(new Parcelable[intentsForFilePicker.size()]));
        return chooser;
    }
    
    public static List<Intent> getIntentsForFilePicker(final Context context, final String[] array) {
        final HashMap<String, Intent> hashMap = new HashMap<String, Intent>();
        final HashMap<String, Intent> hashMap2 = new HashMap<String, Intent>();
        String anObject;
        if (array == null) {
            anObject = "*/*";
        }
        else {
            anObject = array[0];
        }
        String s = anObject;
        if (!"audio/*".equals(anObject)) {
            s = anObject;
            if (!"image/*".equals(anObject)) {
                if ("video/*".equals(anObject)) {
                    s = anObject;
                }
                else {
                    s = "*/*";
                }
            }
        }
        addActivities(context, createIntent(s), hashMap2, hashMap);
        if (hashMap.size() == 0 && hashMap2.size() == 0) {
            hashMap2.clear();
            addActivities(context, createIntent("*/*"), hashMap, null);
        }
        return new ArrayList<Intent>(hashMap2.values());
    }
}
