// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

public class DialogObject
{
    public static long getPeerDialogId(final TLRPC.InputPeer inputPeer) {
        if (inputPeer == null) {
            return 0L;
        }
        final int user_id = inputPeer.user_id;
        if (user_id != 0) {
            return user_id;
        }
        final int chat_id = inputPeer.chat_id;
        int n;
        if (chat_id != 0) {
            n = -chat_id;
        }
        else {
            n = -inputPeer.channel_id;
        }
        return n;
    }
    
    public static long getPeerDialogId(final TLRPC.Peer peer) {
        if (peer == null) {
            return 0L;
        }
        final int user_id = peer.user_id;
        if (user_id != 0) {
            return user_id;
        }
        final int chat_id = peer.chat_id;
        int n;
        if (chat_id != 0) {
            n = -chat_id;
        }
        else {
            n = -peer.channel_id;
        }
        return n;
    }
    
    public static void initDialog(final TLRPC.Dialog dialog) {
        if (dialog != null) {
            if (dialog.id == 0L) {
                if (dialog instanceof TLRPC.TL_dialog) {
                    final TLRPC.Peer peer = dialog.peer;
                    if (peer == null) {
                        return;
                    }
                    final int user_id = peer.user_id;
                    if (user_id != 0) {
                        dialog.id = user_id;
                    }
                    else {
                        final int chat_id = peer.chat_id;
                        if (chat_id != 0) {
                            dialog.id = -chat_id;
                        }
                        else {
                            dialog.id = -peer.channel_id;
                        }
                    }
                }
                else if (dialog instanceof TLRPC.TL_dialogFolder) {
                    dialog.id = makeFolderDialogId(((TLRPC.TL_dialogFolder)dialog).folder.id);
                }
            }
        }
    }
    
    public static boolean isChannel(final TLRPC.Dialog dialog) {
        boolean b = true;
        if (dialog == null || (dialog.flags & 0x1) == 0x0) {
            b = false;
        }
        return b;
    }
    
    public static boolean isFolderDialogId(final long n) {
        final int n2 = (int)n;
        final int n3 = (int)(n >> 32);
        return n2 != 0 && n3 == 2;
    }
    
    public static boolean isPeerDialogId(final long n) {
        final int n2 = (int)n;
        final int n3 = (int)(n >> 32);
        boolean b = true;
        if (n2 == 0 || n3 == 2 || n3 == 1) {
            b = false;
        }
        return b;
    }
    
    public static boolean isSecretDialogId(final long n) {
        return (int)n == 0;
    }
    
    public static long makeFolderDialogId(final int n) {
        return (long)n | 0x200000000L;
    }
    
    public static long makeSecretDialogId(final int n) {
        return (long)n << 32;
    }
}
