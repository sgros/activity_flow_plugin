package org.mozilla.focus.activity;

import android.app.Activity;

public class TextActionActivity extends Activity {
    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0072  */
    public void onCreate(android.os.Bundle r5) {
        /*
        r4 = this;
        super.onCreate(r5);
        r5 = new org.mozilla.focus.utils.SafeIntent;
        r0 = r4.getIntent();
        r5.<init>(r0);
        r0 = r5.getAction();
        r1 = r0.hashCode();
        r2 = 1703997026; // 0x6590ee62 float:8.555227E22 double:8.41886391E-315;
        r3 = 1;
        if (r1 == r2) goto L_0x002a;
    L_0x001a:
        r2 = 1937529752; // 0x737c5b98 float:1.9993844E31 double:9.572668883E-315;
        if (r1 == r2) goto L_0x0020;
    L_0x001f:
        goto L_0x0034;
    L_0x0020:
        r1 = "android.intent.action.WEB_SEARCH";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0034;
    L_0x0028:
        r0 = 1;
        goto L_0x0035;
    L_0x002a:
        r1 = "android.intent.action.PROCESS_TEXT";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0034;
    L_0x0032:
        r0 = 0;
        goto L_0x0035;
    L_0x0034:
        r0 = -1;
    L_0x0035:
        switch(r0) {
            case 0: goto L_0x0049;
            case 1: goto L_0x003c;
            default: goto L_0x0038;
        };
    L_0x0038:
        r5 = "";
        r0 = 0;
        goto L_0x0055;
    L_0x003c:
        r0 = "query";
        r5 = r5.getStringExtra(r0);
        r0 = org.mozilla.rocket.component.LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_WEB_SEARCH;
        r0 = r0.getValue();
        goto L_0x0055;
    L_0x0049:
        r0 = "android.intent.extra.PROCESS_TEXT";
        r5 = r5.getCharSequenceExtra(r0);
        r0 = org.mozilla.rocket.component.LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_TEXT_SELECTION;
        r0 = r0.getValue();
    L_0x0055:
        r5 = r5.toString();
        r5 = org.mozilla.focus.utils.SearchUtils.createSearchUrl(r4, r5);
        r1 = new android.content.Intent;
        r1.<init>();
        r2 = "org.mozilla.rocket.activity.MainActivity";
        r1.setClassName(r4, r2);
        r2 = "android.intent.action.VIEW";
        r1.setAction(r2);
        r2 = android.text.TextUtils.isEmpty(r0);
        if (r2 != 0) goto L_0x0075;
    L_0x0072:
        r1.putExtra(r0, r3);
    L_0x0075:
        r5 = android.net.Uri.parse(r5);
        r1.setData(r5);
        r4.startActivity(r1);
        r4.finish();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.activity.TextActionActivity.onCreate(android.os.Bundle):void");
    }
}
