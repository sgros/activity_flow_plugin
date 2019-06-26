package androidx.core.view.inputmethod;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {

    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0051  */
    static boolean handlePerformPrivateCommand(java.lang.String r7, android.os.Bundle r8, androidx.core.view.inputmethod.InputConnectionCompat.OnCommitContentListener r9) {
        /*
        r0 = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
        r7 = android.text.TextUtils.equals(r0, r7);
        r0 = 0;
        if (r7 != 0) goto L_0x000a;
    L_0x0009:
        return r0;
    L_0x000a:
        if (r8 != 0) goto L_0x000d;
    L_0x000c:
        return r0;
    L_0x000d:
        r7 = 0;
        r1 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
        r1 = r8.getParcelable(r1);	 Catch:{ all -> 0x004d }
        r1 = (android.os.ResultReceiver) r1;	 Catch:{ all -> 0x004d }
        r2 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
        r2 = r8.getParcelable(r2);	 Catch:{ all -> 0x004b }
        r2 = (android.net.Uri) r2;	 Catch:{ all -> 0x004b }
        r3 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
        r3 = r8.getParcelable(r3);	 Catch:{ all -> 0x004b }
        r3 = (android.content.ClipDescription) r3;	 Catch:{ all -> 0x004b }
        r4 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
        r4 = r8.getParcelable(r4);	 Catch:{ all -> 0x004b }
        r4 = (android.net.Uri) r4;	 Catch:{ all -> 0x004b }
        r5 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
        r5 = r8.getInt(r5);	 Catch:{ all -> 0x004b }
        r6 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
        r8 = r8.getParcelable(r6);	 Catch:{ all -> 0x004b }
        r8 = (android.os.Bundle) r8;	 Catch:{ all -> 0x004b }
        r6 = new androidx.core.view.inputmethod.InputContentInfoCompat;	 Catch:{ all -> 0x004b }
        r6.<init>(r2, r3, r4);	 Catch:{ all -> 0x004b }
        r8 = r9.onCommitContent(r6, r5, r8);	 Catch:{ all -> 0x004b }
        if (r1 == 0) goto L_0x004a;
    L_0x0047:
        r1.send(r8, r7);
    L_0x004a:
        return r8;
    L_0x004b:
        r8 = move-exception;
        goto L_0x004f;
    L_0x004d:
        r8 = move-exception;
        r1 = r7;
    L_0x004f:
        if (r1 == 0) goto L_0x0054;
    L_0x0051:
        r1.send(r0, r7);
    L_0x0054:
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.view.inputmethod.InputConnectionCompat.handlePerformPrivateCommand(java.lang.String, android.os.Bundle, androidx.core.view.inputmethod.InputConnectionCompat$OnCommitContentListener):boolean");
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        } else if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        } else if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        } else if (VERSION.SDK_INT >= 25) {
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean commitContent(InputContentInfo inputContentInfo, int i, Bundle bundle) {
                    if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), i, bundle)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, i, bundle);
                }
            };
        } else {
            if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                return inputConnection;
            }
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean performPrivateCommand(String str, Bundle bundle) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(str, bundle, onCommitContentListener)) {
                        return true;
                    }
                    return super.performPrivateCommand(str, bundle);
                }
            };
        }
    }
}
