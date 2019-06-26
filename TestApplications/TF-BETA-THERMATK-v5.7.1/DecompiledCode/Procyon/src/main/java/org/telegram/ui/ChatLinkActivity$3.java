// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.ActionBar.BaseFragment;

class ChatLinkActivity$3 implements GroupCreateFinalActivityDelegate
{
    final /* synthetic */ ChatLinkActivity this$0;
    
    ChatLinkActivity$3(final ChatLinkActivity this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void didFailChatCreation() {
    }
    
    @Override
    public void didFinishChatCreation(final GroupCreateFinalActivity groupCreateFinalActivity, final int i) {
        final ChatLinkActivity this$0 = this.this$0;
        this$0.linkChat(this$0.getMessagesController().getChat(i), groupCreateFinalActivity);
    }
    
    @Override
    public void didStartChatCreation() {
    }
}
