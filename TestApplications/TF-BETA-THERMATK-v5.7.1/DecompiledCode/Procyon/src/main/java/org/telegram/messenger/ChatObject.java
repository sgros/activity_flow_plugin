// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

public class ChatObject
{
    public static final int ACTION_ADD_ADMINS = 4;
    public static final int ACTION_BLOCK_USERS = 2;
    public static final int ACTION_CHANGE_INFO = 1;
    public static final int ACTION_DELETE_MESSAGES = 13;
    public static final int ACTION_EDIT_MESSAGES = 12;
    public static final int ACTION_EMBED_LINKS = 9;
    public static final int ACTION_INVITE = 3;
    public static final int ACTION_PIN = 0;
    public static final int ACTION_POST = 5;
    public static final int ACTION_SEND = 6;
    public static final int ACTION_SEND_MEDIA = 7;
    public static final int ACTION_SEND_POLLS = 10;
    public static final int ACTION_SEND_STICKERS = 8;
    public static final int ACTION_VIEW = 11;
    public static final int CHAT_TYPE_BROADCAST = 1;
    public static final int CHAT_TYPE_CHANNEL = 2;
    public static final int CHAT_TYPE_CHAT = 0;
    public static final int CHAT_TYPE_MEGAGROUP = 4;
    public static final int CHAT_TYPE_USER = 3;
    
    public static boolean canAddAdmins(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 4);
    }
    
    public static boolean canAddUsers(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 3);
    }
    
    public static boolean canBlockUsers(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 2);
    }
    
    public static boolean canChangeChatInfo(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 1);
    }
    
    public static boolean canPinMessages(final TLRPC.Chat chat) {
        final boolean b = false;
        if (!canUserDoAction(chat, 0)) {
            boolean b2 = b;
            if (!isChannel(chat)) {
                return b2;
            }
            b2 = b;
            if (chat.megagroup) {
                return b2;
            }
            final TLRPC.TL_chatAdminRights admin_rights = chat.admin_rights;
            b2 = b;
            if (admin_rights == null) {
                return b2;
            }
            b2 = b;
            if (!admin_rights.edit_messages) {
                return b2;
            }
        }
        return true;
    }
    
    public static boolean canPost(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 5);
    }
    
    public static boolean canSendEmbed(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 9);
    }
    
    public static boolean canSendMedia(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 7);
    }
    
    public static boolean canSendMessages(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 6);
    }
    
    public static boolean canSendPolls(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 10);
    }
    
    public static boolean canSendStickers(final TLRPC.Chat chat) {
        return canUserDoAction(chat, 8);
    }
    
    public static boolean canUserDoAction(final TLRPC.Chat chat, final int n) {
        if (chat == null) {
            return true;
        }
        if (canUserDoAdminAction(chat, n)) {
            return true;
        }
        if (getBannedRight(chat.banned_rights, n)) {
            return false;
        }
        if (isBannableAction(n)) {
            if (chat.admin_rights != null && !isAdminAction(n)) {
                return true;
            }
            if (chat.default_banned_rights == null && (chat instanceof TLRPC.TL_chat_layer92 || chat instanceof TLRPC.TL_chat_old || chat instanceof TLRPC.TL_chat_old2 || chat instanceof TLRPC.TL_channel_layer92 || chat instanceof TLRPC.TL_channel_layer77 || chat instanceof TLRPC.TL_channel_layer72 || chat instanceof TLRPC.TL_channel_layer67 || chat instanceof TLRPC.TL_channel_layer48 || chat instanceof TLRPC.TL_channel_old)) {
                return true;
            }
            final TLRPC.TL_chatBannedRights default_banned_rights = chat.default_banned_rights;
            if (default_banned_rights != null) {
                if (!getBannedRight(default_banned_rights, n)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean canUserDoAdminAction(final TLRPC.Chat chat, final int n) {
        if (chat == null) {
            return false;
        }
        if (chat.creator) {
            return true;
        }
        final TLRPC.TL_chatAdminRights admin_rights = chat.admin_rights;
        if (admin_rights != null) {
            boolean b;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                if (n != 5) {
                                    if (n != 12) {
                                        b = (n == 13 && admin_rights.delete_messages);
                                    }
                                    else {
                                        b = admin_rights.edit_messages;
                                    }
                                }
                                else {
                                    b = admin_rights.post_messages;
                                }
                            }
                            else {
                                b = admin_rights.add_admins;
                            }
                        }
                        else {
                            b = admin_rights.invite_users;
                        }
                    }
                    else {
                        b = admin_rights.ban_users;
                    }
                }
                else {
                    b = admin_rights.change_info;
                }
            }
            else {
                b = admin_rights.pin_messages;
            }
            if (b) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canWriteToChat(final TLRPC.Chat chat) {
        if (isChannel(chat) && !chat.creator) {
            final TLRPC.TL_chatAdminRights admin_rights = chat.admin_rights;
            if (admin_rights == null || !admin_rights.post_messages) {
                if (chat.broadcast) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean getBannedRight(final TLRPC.TL_chatBannedRights tl_chatBannedRights, final int n) {
        if (tl_chatBannedRights == null) {
            return false;
        }
        if (n == 0) {
            return tl_chatBannedRights.pin_messages;
        }
        if (n == 1) {
            return tl_chatBannedRights.change_info;
        }
        if (n == 3) {
            return tl_chatBannedRights.invite_users;
        }
        switch (n) {
            default: {
                return false;
            }
            case 11: {
                return tl_chatBannedRights.view_messages;
            }
            case 10: {
                return tl_chatBannedRights.send_polls;
            }
            case 9: {
                return tl_chatBannedRights.embed_links;
            }
            case 8: {
                return tl_chatBannedRights.send_stickers;
            }
            case 7: {
                return tl_chatBannedRights.send_media;
            }
            case 6: {
                return tl_chatBannedRights.send_messages;
            }
        }
    }
    
    public static String getBannedRightsString(final TLRPC.TL_chatBannedRights tl_chatBannedRights) {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(tl_chatBannedRights.view_messages ? 1 : 0);
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(tl_chatBannedRights.send_messages ? 1 : 0);
        final String string2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string2);
        sb3.append(tl_chatBannedRights.send_media ? 1 : 0);
        final String string3 = sb3.toString();
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(string3);
        sb4.append(tl_chatBannedRights.send_stickers ? 1 : 0);
        final String string4 = sb4.toString();
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(string4);
        sb5.append(tl_chatBannedRights.send_gifs ? 1 : 0);
        final String string5 = sb5.toString();
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(string5);
        sb6.append(tl_chatBannedRights.send_games ? 1 : 0);
        final String string6 = sb6.toString();
        final StringBuilder sb7 = new StringBuilder();
        sb7.append(string6);
        sb7.append(tl_chatBannedRights.send_inline ? 1 : 0);
        final String string7 = sb7.toString();
        final StringBuilder sb8 = new StringBuilder();
        sb8.append(string7);
        sb8.append(tl_chatBannedRights.embed_links ? 1 : 0);
        final String string8 = sb8.toString();
        final StringBuilder sb9 = new StringBuilder();
        sb9.append(string8);
        sb9.append(tl_chatBannedRights.send_polls ? 1 : 0);
        final String string9 = sb9.toString();
        final StringBuilder sb10 = new StringBuilder();
        sb10.append(string9);
        sb10.append(tl_chatBannedRights.invite_users ? 1 : 0);
        final String string10 = sb10.toString();
        final StringBuilder sb11 = new StringBuilder();
        sb11.append(string10);
        sb11.append(tl_chatBannedRights.change_info ? 1 : 0);
        final String string11 = sb11.toString();
        final StringBuilder sb12 = new StringBuilder();
        sb12.append(string11);
        sb12.append(tl_chatBannedRights.pin_messages ? 1 : 0);
        final String string12 = sb12.toString();
        final StringBuilder sb13 = new StringBuilder();
        sb13.append(string12);
        sb13.append(tl_chatBannedRights.until_date);
        return sb13.toString();
    }
    
    public static TLRPC.Chat getChatByDialog(final long n, final int n2) {
        final int n3 = (int)n;
        if (n3 < 0) {
            return MessagesController.getInstance(n2).getChat(-n3);
        }
        return null;
    }
    
    public static boolean hasAdminRights(final TLRPC.Chat chat) {
        if (chat != null) {
            if (!chat.creator) {
                final TLRPC.TL_chatAdminRights admin_rights = chat.admin_rights;
                if (admin_rights == null || admin_rights.flags == 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static boolean isActionBannedByDefault(final TLRPC.Chat chat, final int n) {
        return !getBannedRight(chat.banned_rights, n) && getBannedRight(chat.default_banned_rights, n);
    }
    
    private static boolean isAdminAction(final int n) {
        return n == 0 || n == 1 || n == 2 || n == 3 || n == 4 || n == 5 || n == 12 || n == 13;
    }
    
    private static boolean isBannableAction(final int n) {
        if (n != 0 && n != 1 && n != 3) {
            switch (n) {
                default: {
                    return false;
                }
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11: {
                    break;
                }
            }
        }
        return true;
    }
    
    public static boolean isCanWriteToChannel(final int i, final int n) {
        final TLRPC.Chat chat = MessagesController.getInstance(n).getChat(i);
        return canSendMessages(chat) || (chat != null && chat.megagroup);
    }
    
    public static boolean isChannel(final int i, final int n) {
        final TLRPC.Chat chat = MessagesController.getInstance(n).getChat(i);
        return chat instanceof TLRPC.TL_channel || chat instanceof TLRPC.TL_channelForbidden;
    }
    
    public static boolean isChannel(final TLRPC.Chat chat) {
        return chat instanceof TLRPC.TL_channel || chat instanceof TLRPC.TL_channelForbidden;
    }
    
    public static boolean isKickedFromChat(final TLRPC.Chat chat) {
        if (chat != null && !(chat instanceof TLRPC.TL_chatEmpty) && !(chat instanceof TLRPC.TL_chatForbidden) && !(chat instanceof TLRPC.TL_channelForbidden) && !chat.kicked && !chat.deactivated) {
            final TLRPC.TL_chatBannedRights banned_rights = chat.banned_rights;
            if (banned_rights == null || !banned_rights.view_messages) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isLeftFromChat(final TLRPC.Chat chat) {
        return chat == null || chat instanceof TLRPC.TL_chatEmpty || chat instanceof TLRPC.TL_chatForbidden || chat instanceof TLRPC.TL_channelForbidden || chat.left || chat.deactivated;
    }
    
    public static boolean isMegagroup(final TLRPC.Chat chat) {
        return (chat instanceof TLRPC.TL_channel || chat instanceof TLRPC.TL_channelForbidden) && chat.megagroup;
    }
    
    public static boolean isNotInChat(final TLRPC.Chat chat) {
        return chat == null || chat instanceof TLRPC.TL_chatEmpty || chat instanceof TLRPC.TL_chatForbidden || chat instanceof TLRPC.TL_channelForbidden || chat.left || chat.kicked || chat.deactivated;
    }
}
