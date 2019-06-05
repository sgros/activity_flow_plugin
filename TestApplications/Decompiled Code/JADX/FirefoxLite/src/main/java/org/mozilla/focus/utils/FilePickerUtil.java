package org.mozilla.focus.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilePickerUtil {
    public static Intent getFilePickerIntent(Context context, CharSequence charSequence, String[] strArr) {
        List intentsForFilePicker = getIntentsForFilePicker(context, strArr);
        if (intentsForFilePicker.size() == 0) {
            return null;
        }
        Intent intent = (Intent) intentsForFilePicker.remove(0);
        if (intentsForFilePicker.size() == 0) {
            return intent;
        }
        Intent createChooser = Intent.createChooser(intent, charSequence);
        createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) intentsForFilePicker.toArray(new Parcelable[intentsForFilePicker.size()]));
        return createChooser;
    }

    public static List<Intent> getIntentsForFilePicker(Context context, String[] strArr) {
        String str;
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        if (strArr == null) {
            str = "*/*";
        } else {
            str = strArr[0];
        }
        if (!("audio/*".equals(str) || "image/*".equals(str) || "video/*".equals(str))) {
            str = "*/*";
        }
        addActivities(context, createIntent(str), hashMap2, hashMap);
        if (hashMap.size() == 0 && hashMap2.size() == 0) {
            hashMap2.clear();
            addActivities(context, createIntent("*/*"), hashMap, null);
        }
        return new ArrayList(hashMap2.values());
    }

    private static void addActivities(Context context, Intent intent, HashMap<String, Intent> hashMap, HashMap<String, Intent> hashMap2) {
        if (hashMap2 != null) {
            for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
                ComponentName componentName = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name);
                if (!hashMap2.containsKey(componentName.toString())) {
                    Intent intent2 = new Intent(intent);
                    intent2.setComponent(componentName);
                    hashMap.put(componentName.toString(), intent2);
                }
            }
        }
    }

    private static Intent createIntent(String str) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType(str);
        intent.addCategory("android.intent.category.OPENABLE");
        return intent;
    }
}
