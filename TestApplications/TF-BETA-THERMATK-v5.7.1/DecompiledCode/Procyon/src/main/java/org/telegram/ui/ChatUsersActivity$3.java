// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;

class ChatUsersActivity$3 implements ContactsAddActivityDelegate
{
    final /* synthetic */ ChatUsersActivity this$0;
    
    ChatUsersActivity$3(final ChatUsersActivity this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void didSelectUsers(final ArrayList<TLRPC.User> list, final int n) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            MessagesController.getInstance(this.this$0.currentAccount).addUserToChat(this.this$0.chatId, list.get(i), null, n, null, this.this$0, null);
        }
    }
    
    @Override
    public void needAddBot(final TLRPC.User user) {
        this.this$0.openRightsEdit(user.id, null, null, null, true, 0, false);
    }
}
