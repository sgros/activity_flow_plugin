package org.telegram.messenger;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MediaController$hrz-cghaZ1kTzzeIoiWSaviEy-E */
public final /* synthetic */ class C0568-$$Lambda$MediaController$hrz-cghaZ1kTzzeIoiWSaviEy-E implements OnCancelListener {
    private final /* synthetic */ boolean[] f$0;

    public /* synthetic */ C0568-$$Lambda$MediaController$hrz-cghaZ1kTzzeIoiWSaviEy-E(boolean[] zArr) {
        this.f$0 = zArr;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0[0] = true;
    }
}
