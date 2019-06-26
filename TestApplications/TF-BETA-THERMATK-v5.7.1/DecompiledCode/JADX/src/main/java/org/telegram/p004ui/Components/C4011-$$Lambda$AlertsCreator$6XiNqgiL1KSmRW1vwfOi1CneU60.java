package org.telegram.p004ui.Components;

import android.util.SparseArray;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageObject.GroupedMessages;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$6XiNqgiL1KSmRW1vwfOi1CneU60 */
public final /* synthetic */ class C4011-$$Lambda$AlertsCreator$6XiNqgiL1KSmRW1vwfOi1CneU60 implements RequestDelegate {
    private final /* synthetic */ AlertDialog[] f$0;
    private final /* synthetic */ BaseFragment f$1;
    private final /* synthetic */ Runnable f$10;
    private final /* synthetic */ User f$2;
    private final /* synthetic */ Chat f$3;
    private final /* synthetic */ EncryptedChat f$4;
    private final /* synthetic */ ChatFull f$5;
    private final /* synthetic */ long f$6;
    private final /* synthetic */ MessageObject f$7;
    private final /* synthetic */ SparseArray[] f$8;
    private final /* synthetic */ GroupedMessages f$9;

    public /* synthetic */ C4011-$$Lambda$AlertsCreator$6XiNqgiL1KSmRW1vwfOi1CneU60(AlertDialog[] alertDialogArr, BaseFragment baseFragment, User user, Chat chat, EncryptedChat encryptedChat, ChatFull chatFull, long j, MessageObject messageObject, SparseArray[] sparseArrayArr, GroupedMessages groupedMessages, Runnable runnable) {
        this.f$0 = alertDialogArr;
        this.f$1 = baseFragment;
        this.f$2 = user;
        this.f$3 = chat;
        this.f$4 = encryptedChat;
        this.f$5 = chatFull;
        this.f$6 = j;
        this.f$7 = messageObject;
        this.f$8 = sparseArrayArr;
        this.f$9 = groupedMessages;
        this.f$10 = runnable;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C2498-$$Lambda$AlertsCreator$qKrVsguRKMu04xHOh5j6MH-69nE(this.f$0, tLObject, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10));
    }
}
