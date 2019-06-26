package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

public class DialogObject {
   public static long getPeerDialogId(TLRPC.InputPeer var0) {
      if (var0 == null) {
         return 0L;
      } else {
         int var1 = var0.user_id;
         if (var1 != 0) {
            return (long)var1;
         } else {
            var1 = var0.chat_id;
            if (var1 != 0) {
               var1 = -var1;
            } else {
               var1 = -var0.channel_id;
            }

            return (long)var1;
         }
      }
   }

   public static long getPeerDialogId(TLRPC.Peer var0) {
      if (var0 == null) {
         return 0L;
      } else {
         int var1 = var0.user_id;
         if (var1 != 0) {
            return (long)var1;
         } else {
            var1 = var0.chat_id;
            if (var1 != 0) {
               var1 = -var1;
            } else {
               var1 = -var0.channel_id;
            }

            return (long)var1;
         }
      }
   }

   public static void initDialog(TLRPC.Dialog var0) {
      if (var0 != null && var0.id == 0L) {
         if (var0 instanceof TLRPC.TL_dialog) {
            TLRPC.Peer var1 = var0.peer;
            if (var1 == null) {
               return;
            }

            int var2 = var1.user_id;
            if (var2 != 0) {
               var0.id = (long)var2;
            } else {
               var2 = var1.chat_id;
               if (var2 != 0) {
                  var0.id = (long)(-var2);
               } else {
                  var0.id = (long)(-var1.channel_id);
               }
            }
         } else if (var0 instanceof TLRPC.TL_dialogFolder) {
            var0.id = makeFolderDialogId(((TLRPC.TL_dialogFolder)var0).folder.id);
         }
      }

   }

   public static boolean isChannel(TLRPC.Dialog var0) {
      boolean var1 = true;
      if (var0 == null || (var0.flags & 1) == 0) {
         var1 = false;
      }

      return var1;
   }

   public static boolean isFolderDialogId(long var0) {
      int var2 = (int)var0;
      int var3 = (int)(var0 >> 32);
      boolean var4;
      if (var2 != 0 && var3 == 2) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public static boolean isPeerDialogId(long var0) {
      int var2 = (int)var0;
      int var3 = (int)(var0 >> 32);
      boolean var4 = true;
      if (var2 == 0 || var3 == 2 || var3 == 1) {
         var4 = false;
      }

      return var4;
   }

   public static boolean isSecretDialogId(long var0) {
      boolean var2;
      if ((int)var0 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static long makeFolderDialogId(int var0) {
      return (long)var0 | 8589934592L;
   }

   public static long makeSecretDialogId(int var0) {
      return (long)var0 << 32;
   }
}
