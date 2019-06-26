package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.EncryptedChat;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$LG3SyEdI54mzlZQ5Dr0sHARNI2g */
public final /* synthetic */ class C2475-$$Lambda$AlertsCreator$LG3SyEdI54mzlZQ5Dr0sHARNI2g implements OnClickListener {
    private final /* synthetic */ EncryptedChat f$0;
    private final /* synthetic */ NumberPicker f$1;

    public /* synthetic */ C2475-$$Lambda$AlertsCreator$LG3SyEdI54mzlZQ5Dr0sHARNI2g(EncryptedChat encryptedChat, NumberPicker numberPicker) {
        this.f$0 = encryptedChat;
        this.f$1 = numberPicker;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createTTLAlert$39(this.f$0, this.f$1, dialogInterface, i);
    }
}
