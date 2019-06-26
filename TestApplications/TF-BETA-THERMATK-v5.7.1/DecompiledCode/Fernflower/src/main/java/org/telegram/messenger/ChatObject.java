package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

public class ChatObject {
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

   public static boolean canAddAdmins(TLRPC.Chat var0) {
      return canUserDoAction(var0, 4);
   }

   public static boolean canAddUsers(TLRPC.Chat var0) {
      return canUserDoAction(var0, 3);
   }

   public static boolean canBlockUsers(TLRPC.Chat var0) {
      return canUserDoAction(var0, 2);
   }

   public static boolean canChangeChatInfo(TLRPC.Chat var0) {
      return canUserDoAction(var0, 1);
   }

   public static boolean canPinMessages(TLRPC.Chat var0) {
      boolean var1 = false;
      boolean var2;
      if (!canUserDoAction(var0, 0)) {
         var2 = var1;
         if (!isChannel(var0)) {
            return var2;
         }

         var2 = var1;
         if (var0.megagroup) {
            return var2;
         }

         TLRPC.TL_chatAdminRights var3 = var0.admin_rights;
         var2 = var1;
         if (var3 == null) {
            return var2;
         }

         var2 = var1;
         if (!var3.edit_messages) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   public static boolean canPost(TLRPC.Chat var0) {
      return canUserDoAction(var0, 5);
   }

   public static boolean canSendEmbed(TLRPC.Chat var0) {
      return canUserDoAction(var0, 9);
   }

   public static boolean canSendMedia(TLRPC.Chat var0) {
      return canUserDoAction(var0, 7);
   }

   public static boolean canSendMessages(TLRPC.Chat var0) {
      return canUserDoAction(var0, 6);
   }

   public static boolean canSendPolls(TLRPC.Chat var0) {
      return canUserDoAction(var0, 10);
   }

   public static boolean canSendStickers(TLRPC.Chat var0) {
      return canUserDoAction(var0, 8);
   }

   public static boolean canUserDoAction(TLRPC.Chat var0, int var1) {
      if (var0 == null) {
         return true;
      } else if (canUserDoAdminAction(var0, var1)) {
         return true;
      } else if (getBannedRight(var0.banned_rights, var1)) {
         return false;
      } else {
         if (isBannableAction(var1)) {
            if (var0.admin_rights != null && !isAdminAction(var1)) {
               return true;
            }

            if (var0.default_banned_rights == null && (var0 instanceof TLRPC.TL_chat_layer92 || var0 instanceof TLRPC.TL_chat_old || var0 instanceof TLRPC.TL_chat_old2 || var0 instanceof TLRPC.TL_channel_layer92 || var0 instanceof TLRPC.TL_channel_layer77 || var0 instanceof TLRPC.TL_channel_layer72 || var0 instanceof TLRPC.TL_channel_layer67 || var0 instanceof TLRPC.TL_channel_layer48 || var0 instanceof TLRPC.TL_channel_old)) {
               return true;
            }

            TLRPC.TL_chatBannedRights var2 = var0.default_banned_rights;
            if (var2 != null && !getBannedRight(var2, var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean canUserDoAdminAction(TLRPC.Chat var0, int var1) {
      if (var0 == null) {
         return false;
      } else if (var0.creator) {
         return true;
      } else {
         TLRPC.TL_chatAdminRights var3 = var0.admin_rights;
         if (var3 != null) {
            boolean var2;
            if (var1 != 0) {
               if (var1 != 1) {
                  if (var1 != 2) {
                     if (var1 != 3) {
                        if (var1 != 4) {
                           if (var1 != 5) {
                              if (var1 != 12) {
                                 if (var1 != 13) {
                                    var2 = false;
                                 } else {
                                    var2 = var3.delete_messages;
                                 }
                              } else {
                                 var2 = var3.edit_messages;
                              }
                           } else {
                              var2 = var3.post_messages;
                           }
                        } else {
                           var2 = var3.add_admins;
                        }
                     } else {
                        var2 = var3.invite_users;
                     }
                  } else {
                     var2 = var3.ban_users;
                  }
               } else {
                  var2 = var3.change_info;
               }
            } else {
               var2 = var3.pin_messages;
            }

            if (var2) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean canWriteToChat(TLRPC.Chat var0) {
      boolean var2;
      if (isChannel(var0) && !var0.creator) {
         TLRPC.TL_chatAdminRights var1 = var0.admin_rights;
         if ((var1 == null || !var1.post_messages) && var0.broadcast) {
            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   private static boolean getBannedRight(TLRPC.TL_chatBannedRights var0, int var1) {
      if (var0 == null) {
         return false;
      } else if (var1 != 0) {
         if (var1 != 1) {
            if (var1 != 3) {
               switch(var1) {
               case 6:
                  return var0.send_messages;
               case 7:
                  return var0.send_media;
               case 8:
                  return var0.send_stickers;
               case 9:
                  return var0.embed_links;
               case 10:
                  return var0.send_polls;
               case 11:
                  return var0.view_messages;
               default:
                  return false;
               }
            } else {
               return var0.invite_users;
            }
         } else {
            return var0.change_info;
         }
      } else {
         return var0.pin_messages;
      }
   }

   public static String getBannedRightsString(TLRPC.TL_chatBannedRights var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(var0.view_messages);
      String var2 = var1.toString();
      var1 = new StringBuilder();
      var1.append(var2);
      var1.append(var0.send_messages);
      var2 = var1.toString();
      var1 = new StringBuilder();
      var1.append(var2);
      var1.append(var0.send_media);
      String var3 = var1.toString();
      StringBuilder var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.send_stickers);
      var2 = var4.toString();
      var1 = new StringBuilder();
      var1.append(var2);
      var1.append(var0.send_gifs);
      var3 = var1.toString();
      var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.send_games);
      var3 = var4.toString();
      var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.send_inline);
      var3 = var4.toString();
      var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.embed_links);
      var3 = var4.toString();
      var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.send_polls);
      var2 = var4.toString();
      var1 = new StringBuilder();
      var1.append(var2);
      var1.append(var0.invite_users);
      var3 = var1.toString();
      var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.change_info);
      var3 = var4.toString();
      var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.pin_messages);
      var3 = var4.toString();
      var4 = new StringBuilder();
      var4.append(var3);
      var4.append(var0.until_date);
      return var4.toString();
   }

   public static TLRPC.Chat getChatByDialog(long var0, int var2) {
      int var3 = (int)var0;
      return var3 < 0 ? MessagesController.getInstance(var2).getChat(-var3) : null;
   }

   public static boolean hasAdminRights(TLRPC.Chat var0) {
      boolean var1;
      label25: {
         if (var0 != null) {
            if (var0.creator) {
               break label25;
            }

            TLRPC.TL_chatAdminRights var2 = var0.admin_rights;
            if (var2 != null && var2.flags != 0) {
               break label25;
            }
         }

         var1 = false;
         return var1;
      }

      var1 = true;
      return var1;
   }

   public static boolean isActionBannedByDefault(TLRPC.Chat var0, int var1) {
      return getBannedRight(var0.banned_rights, var1) ? false : getBannedRight(var0.default_banned_rights, var1);
   }

   private static boolean isAdminAction(int var0) {
      return var0 == 0 || var0 == 1 || var0 == 2 || var0 == 3 || var0 == 4 || var0 == 5 || var0 == 12 || var0 == 13;
   }

   private static boolean isBannableAction(int var0) {
      if (var0 != 0 && var0 != 1 && var0 != 3) {
         switch(var0) {
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
            break;
         default:
            return false;
         }
      }

      return true;
   }

   public static boolean isCanWriteToChannel(int var0, int var1) {
      TLRPC.Chat var2 = MessagesController.getInstance(var1).getChat(var0);
      boolean var3;
      if (canSendMessages(var2) || var2 != null && var2.megagroup) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public static boolean isChannel(int var0, int var1) {
      TLRPC.Chat var2 = MessagesController.getInstance(var1).getChat(var0);
      boolean var3;
      if (!(var2 instanceof TLRPC.TL_channel) && !(var2 instanceof TLRPC.TL_channelForbidden)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   public static boolean isChannel(TLRPC.Chat var0) {
      boolean var1;
      if (!(var0 instanceof TLRPC.TL_channel) && !(var0 instanceof TLRPC.TL_channelForbidden)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isKickedFromChat(TLRPC.Chat var0) {
      boolean var1;
      if (var0 != null && !(var0 instanceof TLRPC.TL_chatEmpty) && !(var0 instanceof TLRPC.TL_chatForbidden) && !(var0 instanceof TLRPC.TL_channelForbidden) && !var0.kicked && !var0.deactivated) {
         TLRPC.TL_chatBannedRights var2 = var0.banned_rights;
         if (var2 == null || !var2.view_messages) {
            var1 = false;
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public static boolean isLeftFromChat(TLRPC.Chat var0) {
      boolean var1;
      if (var0 != null && !(var0 instanceof TLRPC.TL_chatEmpty) && !(var0 instanceof TLRPC.TL_chatForbidden) && !(var0 instanceof TLRPC.TL_channelForbidden) && !var0.left && !var0.deactivated) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isMegagroup(TLRPC.Chat var0) {
      boolean var1;
      if ((var0 instanceof TLRPC.TL_channel || var0 instanceof TLRPC.TL_channelForbidden) && var0.megagroup) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isNotInChat(TLRPC.Chat var0) {
      boolean var1;
      if (var0 != null && !(var0 instanceof TLRPC.TL_chatEmpty) && !(var0 instanceof TLRPC.TL_chatForbidden) && !(var0 instanceof TLRPC.TL_channelForbidden) && !var0.left && !var0.kicked && !var0.deactivated) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }
}
