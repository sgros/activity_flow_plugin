package org.telegram.ui.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.EmojiReplacementCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Components.RecyclerListView;

public class StickersAdapter extends RecyclerListView.SelectionAdapter implements NotificationCenter.NotificationCenterDelegate {
   private int currentAccount;
   private boolean delayLocalResults;
   private StickersAdapter.StickersAdapterDelegate delegate;
   private ArrayList keywordResults;
   private int lastReqId;
   private String[] lastSearchKeyboardLanguage;
   private String lastSticker;
   private Context mContext;
   private Runnable searchRunnable;
   private ArrayList stickers;
   private HashMap stickersMap;
   private ArrayList stickersParents;
   private ArrayList stickersToLoad;
   private boolean visible;

   public StickersAdapter(Context var1, StickersAdapter.StickersAdapterDelegate var2) {
      this.currentAccount = UserConfig.selectedAccount;
      this.stickersToLoad = new ArrayList();
      this.mContext = var1;
      this.delegate = var2;
      DataQuery.getInstance(this.currentAccount).checkStickers(0);
      DataQuery.getInstance(this.currentAccount).checkStickers(1);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
   }

   private void addStickerToResult(TLRPC.Document var1, Object var2) {
      if (var1 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.dc_id);
         var3.append("_");
         var3.append(var1.id);
         String var4 = var3.toString();
         HashMap var5 = this.stickersMap;
         if (var5 == null || !var5.containsKey(var4)) {
            if (this.stickers == null) {
               this.stickers = new ArrayList();
               this.stickersParents = new ArrayList();
               this.stickersMap = new HashMap();
            }

            this.stickers.add(var1);
            this.stickersParents.add(var2);
            this.stickersMap.put(var4, var1);
         }
      }
   }

   private void addStickersToResult(ArrayList var1, Object var2) {
      if (var1 != null && !var1.isEmpty()) {
         int var3 = var1.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            TLRPC.Document var5 = (TLRPC.Document)var1.get(var4);
            StringBuilder var6 = new StringBuilder();
            var6.append(var5.dc_id);
            var6.append("_");
            var6.append(var5.id);
            String var10 = var6.toString();
            HashMap var7 = this.stickersMap;
            if (var7 == null || !var7.containsKey(var10)) {
               if (this.stickers == null) {
                  this.stickers = new ArrayList();
                  this.stickersParents = new ArrayList();
                  this.stickersMap = new HashMap();
               }

               this.stickers.add(var5);
               int var8 = var5.attributes.size();
               int var9 = 0;

               boolean var12;
               while(true) {
                  if (var9 >= var8) {
                     var12 = false;
                     break;
                  }

                  TLRPC.DocumentAttribute var11 = (TLRPC.DocumentAttribute)var5.attributes.get(var9);
                  if (var11 instanceof TLRPC.TL_documentAttributeSticker) {
                     this.stickersParents.add(var11.stickerset);
                     var12 = true;
                     break;
                  }

                  ++var9;
               }

               if (!var12) {
                  this.stickersParents.add(var2);
               }

               this.stickersMap.put(var10, var5);
            }
         }
      }

   }

   private void cancelEmojiSearch() {
      Runnable var1 = this.searchRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         this.searchRunnable = null;
      }

   }

   private boolean checkStickerFilesExistAndDownload() {
      ArrayList var1 = this.stickers;
      int var2 = 0;
      if (var1 == null) {
         return false;
      } else {
         this.stickersToLoad.clear();

         for(int var3 = Math.min(6, this.stickers.size()); var2 < var3; ++var2) {
            TLRPC.Document var5 = (TLRPC.Document)this.stickers.get(var2);
            TLRPC.PhotoSize var4 = FileLoader.getClosestPhotoSizeWithSize(var5.thumbs, 90);
            if (var4 instanceof TLRPC.TL_photoSize && !FileLoader.getPathToAttach(var4, "webp", true).exists()) {
               this.stickersToLoad.add(FileLoader.getAttachFileName(var4, "webp"));
               FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForDocument(var4, var5), this.stickersParents.get(var2), "webp", 1, 1);
            }
         }

         return this.stickersToLoad.isEmpty();
      }
   }

   private boolean isValidSticker(TLRPC.Document var1, String var2) {
      int var3 = var1.attributes.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         TLRPC.DocumentAttribute var5 = (TLRPC.DocumentAttribute)var1.attributes.get(var4);
         if (var5 instanceof TLRPC.TL_documentAttributeSticker) {
            String var6 = var5.alt;
            if (var6 != null && var6.contains(var2)) {
               return true;
            }
            break;
         }
      }

      return false;
   }

   private void searchEmojiByKeyword() {
      String[] var1 = AndroidUtilities.getCurrentKeyboardLanguage();
      if (!Arrays.equals(var1, this.lastSearchKeyboardLanguage)) {
         DataQuery.getInstance(this.currentAccount).fetchNewEmojiKeywords(var1);
      }

      this.lastSearchKeyboardLanguage = var1;
      String var2 = this.lastSticker;
      this.cancelEmojiSearch();
      this.searchRunnable = new _$$Lambda$StickersAdapter$D58rW58m_jlo8ZaeUedvcgRz2Fw(this, var2);
      ArrayList var3 = this.keywordResults;
      if (var3 != null && !var3.isEmpty()) {
         this.searchRunnable.run();
      } else {
         AndroidUtilities.runOnUIThread(this.searchRunnable, 1000L);
      }

   }

   private void searchServerStickers(String var1, String var2) {
      TLRPC.TL_messages_getStickers var3 = new TLRPC.TL_messages_getStickers();
      var3.emoticon = var2;
      var3.hash = 0;
      this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$StickersAdapter$XAmkAEU9b5B6bSCEO_rxuKc1F8U(this, var1));
   }

   public void clearStickers() {
      if (!this.delayLocalResults && this.lastReqId == 0) {
         this.lastSticker = null;
         this.stickers = null;
         this.stickersParents = null;
         this.stickersMap = null;
         this.keywordResults = null;
         this.stickersToLoad.clear();
         this.notifyDataSetChanged();
         if (this.lastReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
            this.lastReqId = 0;
         }
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      ArrayList var8;
      if (var1 != NotificationCenter.fileDidLoad && var1 != NotificationCenter.fileDidFailedLoad) {
         if (var1 == NotificationCenter.newEmojiSuggestionsAvailable) {
            var8 = this.keywordResults;
            if ((var8 == null || var8.isEmpty()) && !TextUtils.isEmpty(this.lastSticker) && this.getItemCount() == 0) {
               this.searchEmojiByKeyword();
            }
         }
      } else {
         ArrayList var4 = this.stickers;
         if (var4 != null && !var4.isEmpty() && !this.stickersToLoad.isEmpty() && this.visible) {
            boolean var5 = false;
            String var7 = (String)var3[0];
            this.stickersToLoad.remove(var7);
            if (this.stickersToLoad.isEmpty()) {
               var8 = this.stickers;
               boolean var6 = var5;
               if (var8 != null) {
                  var6 = var5;
                  if (!var8.isEmpty()) {
                     var6 = var5;
                     if (this.stickersToLoad.isEmpty()) {
                        var6 = true;
                     }
                  }
               }

               if (var6) {
                  this.keywordResults = null;
               }

               this.delegate.needChangePanelVisibility(var6);
            }
         }
      }

   }

   public Object getItem(int var1) {
      ArrayList var2 = this.keywordResults;
      if (var2 != null && !var2.isEmpty()) {
         return ((DataQuery.KeywordResult)this.keywordResults.get(var1)).emoji;
      } else {
         var2 = this.stickers;
         Object var3;
         if (var2 != null && var1 >= 0 && var1 < var2.size()) {
            var3 = this.stickers.get(var1);
         } else {
            var3 = null;
         }

         return var3;
      }
   }

   public int getItemCount() {
      ArrayList var1 = this.keywordResults;
      if (var1 != null && !var1.isEmpty()) {
         return this.keywordResults.size();
      } else {
         int var2;
         if (!this.delayLocalResults) {
            var1 = this.stickers;
            if (var1 != null) {
               var2 = var1.size();
               return var2;
            }
         }

         var2 = 0;
         return var2;
      }
   }

   public Object getItemParent(int var1) {
      ArrayList var2 = this.keywordResults;
      Object var3 = null;
      if (var2 != null && !var2.isEmpty()) {
         return null;
      } else {
         ArrayList var4 = this.stickersParents;
         Object var5 = var3;
         if (var4 != null) {
            var5 = var3;
            if (var1 >= 0) {
               var5 = var3;
               if (var1 < var4.size()) {
                  var5 = this.stickersParents.get(var1);
               }
            }
         }

         return var5;
      }
   }

   public int getItemViewType(int var1) {
      ArrayList var2 = this.keywordResults;
      return var2 != null && !var2.isEmpty() ? 1 : 0;
   }

   public void hide() {
      if (this.visible) {
         if (this.stickers == null) {
            ArrayList var1 = this.keywordResults;
            if (var1 == null || var1.isEmpty()) {
               return;
            }
         }

         this.visible = false;
         this.delegate.needChangePanelVisibility(false);
      }

   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      return false;
   }

   public boolean isShowingKeywords() {
      ArrayList var1 = this.keywordResults;
      boolean var2;
      if (var1 != null && !var1.isEmpty()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   // $FF: synthetic method
   public void lambda$null$0$StickersAdapter(String var1, ArrayList var2, String var3) {
      if (var1.equals(this.lastSticker)) {
         if (!var2.isEmpty()) {
            this.keywordResults = var2;
         }

         this.notifyDataSetChanged();
         StickersAdapter.StickersAdapterDelegate var5 = this.delegate;
         boolean var4 = var2.isEmpty() ^ true;
         this.visible = var4;
         var5.needChangePanelVisibility(var4);
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$StickersAdapter(String var1, TLObject var2) {
      boolean var3 = false;
      this.lastReqId = 0;
      if (var1.equals(this.lastSticker) && var2 instanceof TLRPC.TL_messages_stickers) {
         this.delayLocalResults = false;
         TLRPC.TL_messages_stickers var9 = (TLRPC.TL_messages_stickers)var2;
         ArrayList var4 = this.stickers;
         int var5;
         if (var4 != null) {
            var5 = var4.size();
         } else {
            var5 = 0;
         }

         var4 = var9.stickers;
         StringBuilder var10 = new StringBuilder();
         var10.append("sticker_search_");
         var10.append(var1);
         this.addStickersToResult(var4, var10.toString());
         ArrayList var8 = this.stickers;
         int var6;
         if (var8 != null) {
            var6 = var8.size();
         } else {
            var6 = 0;
         }

         if (!this.visible) {
            var8 = this.stickers;
            if (var8 != null && !var8.isEmpty()) {
               this.checkStickerFilesExistAndDownload();
               var8 = this.stickers;
               boolean var7 = var3;
               if (var8 != null) {
                  var7 = var3;
                  if (!var8.isEmpty()) {
                     var7 = var3;
                     if (this.stickersToLoad.isEmpty()) {
                        var7 = true;
                     }
                  }
               }

               if (var7) {
                  this.keywordResults = null;
               }

               this.delegate.needChangePanelVisibility(var7);
               this.visible = true;
            }
         }

         if (var5 != var6) {
            this.notifyDataSetChanged();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$searchEmojiByKeyword$1$StickersAdapter(String var1) {
      DataQuery.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, var1, true, new _$$Lambda$StickersAdapter$E0BPvXZpH_mYvi0Rgt_AsiZ8kew(this, var1));
   }

   // $FF: synthetic method
   public void lambda$searchServerStickers$3$StickersAdapter(String var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$StickersAdapter$cGnqxTxY2G2rhm3SXL_mI51dUtI(this, var1, var2));
   }

   public void loadStikersForEmoji(CharSequence var1, boolean var2) {
      boolean var3 = false;
      boolean var4;
      if (var1 != null && var1.length() > 0 && var1.length() <= 14) {
         var4 = true;
      } else {
         var4 = false;
      }

      String var5 = var1.toString();
      int var6 = var1.length();

      int var7;
      int var8;
      CharSequence var10;
      for(var7 = 0; var7 < var6; var1 = var10) {
         int var9;
         label163: {
            label185: {
               if (var7 < var6 - 1) {
                  label184: {
                     label183: {
                        if (var1.charAt(var7) == '\ud83c') {
                           var8 = var7 + 1;
                           if (var1.charAt(var8) >= '\udffb' && var1.charAt(var8) <= '\udfff') {
                              break label183;
                           }
                        }

                        if (var1.charAt(var7) != 8205) {
                           break label184;
                        }

                        var8 = var7 + 1;
                        if (var1.charAt(var8) != 9792 && var1.charAt(var8) != 9794) {
                           break label184;
                        }
                     }

                     var1 = TextUtils.concat(new CharSequence[]{var1.subSequence(0, var7), var1.subSequence(var7 + 2, var1.length())});
                     var6 -= 2;
                     break label185;
                  }
               }

               var9 = var6;
               var10 = var1;
               var8 = var7;
               if (var1.charAt(var7) != '️') {
                  break label163;
               }

               var1 = TextUtils.concat(new CharSequence[]{var1.subSequence(0, var7), var1.subSequence(var7 + 1, var1.length())});
               --var6;
            }

            var8 = var7 - 1;
            var10 = var1;
            var9 = var6;
         }

         var7 = var8 + 1;
         var6 = var9;
      }

      this.lastSticker = var1.toString();
      boolean var16;
      if (!var4 || !Emoji.isValidEmoji(var5) && !Emoji.isValidEmoji(this.lastSticker)) {
         var16 = false;
      } else {
         var16 = true;
      }

      ArrayList var12;
      if (!var2 && SharedConfig.suggestStickers != 2 && var16) {
         this.cancelEmojiSearch();
         this.stickers = null;
         this.stickersParents = null;
         this.stickersMap = null;
         if (this.lastReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
            this.lastReqId = 0;
         }

         this.delayLocalResults = false;
         final ArrayList var17 = DataQuery.getInstance(this.currentAccount).getRecentStickersNoCopy(0);
         final ArrayList var11 = DataQuery.getInstance(this.currentAccount).getRecentStickersNoCopy(2);
         var8 = var17.size();
         var6 = 0;

         TLRPC.Document var13;
         for(int var15 = 0; var6 < var8; var15 = var7) {
            var13 = (TLRPC.Document)var17.get(var6);
            var7 = var15;
            if (this.isValidSticker(var13, this.lastSticker)) {
               this.addStickerToResult(var13, "recent");
               ++var15;
               var7 = var15;
               if (var15 >= 5) {
                  break;
               }
            }

            ++var6;
         }

         var6 = var11.size();

         for(var7 = 0; var7 < var6; ++var7) {
            var13 = (TLRPC.Document)var11.get(var7);
            if (this.isValidSticker(var13, this.lastSticker)) {
               this.addStickerToResult(var13, "fav");
            }
         }

         HashMap var14 = DataQuery.getInstance(this.currentAccount).getAllStickers();
         if (var14 != null) {
            var12 = (ArrayList)var14.get(this.lastSticker);
         } else {
            var12 = null;
         }

         if (var12 != null && !var12.isEmpty()) {
            var12 = new ArrayList(var12);
            if (!var17.isEmpty()) {
               Collections.sort(var12, new Comparator() {
                  private int getIndex(long var1) {
                     byte var3 = 0;
                     int var4 = 0;

                     while(true) {
                        int var5 = var3;
                        if (var4 >= var11.size()) {
                           while(var5 < var17.size()) {
                              if (((TLRPC.Document)var17.get(var5)).id == var1) {
                                 return var5;
                              }

                              ++var5;
                           }

                           return -1;
                        }

                        if (((TLRPC.Document)var11.get(var4)).id == var1) {
                           return var4 + 1000;
                        }

                        ++var4;
                     }
                  }

                  public int compare(TLRPC.Document var1, TLRPC.Document var2) {
                     int var3 = this.getIndex(var1.id);
                     int var4 = this.getIndex(var2.id);
                     if (var3 > var4) {
                        return -1;
                     } else {
                        return var3 < var4 ? 1 : 0;
                     }
                  }
               });
            }

            this.addStickersToResult(var12, (Object)null);
         }

         if (SharedConfig.suggestStickers == 0) {
            this.searchServerStickers(this.lastSticker, var5);
         }

         var12 = this.stickers;
         if (var12 != null && !var12.isEmpty()) {
            if (SharedConfig.suggestStickers == 0 && this.stickers.size() < 5) {
               this.delayLocalResults = true;
               this.delegate.needChangePanelVisibility(false);
               this.visible = false;
            } else {
               this.checkStickerFilesExistAndDownload();
               var12 = this.stickers;
               var2 = var3;
               if (var12 != null) {
                  var2 = var3;
                  if (!var12.isEmpty()) {
                     var2 = var3;
                     if (this.stickersToLoad.isEmpty()) {
                        var2 = true;
                     }
                  }
               }

               if (var2) {
                  this.keywordResults = null;
               }

               this.delegate.needChangePanelVisibility(var2);
               this.visible = true;
            }

            this.notifyDataSetChanged();
         } else if (this.visible) {
            this.delegate.needChangePanelVisibility(false);
            this.visible = false;
         }

      } else {
         if (this.visible) {
            var12 = this.keywordResults;
            if (var12 == null || var12.isEmpty()) {
               this.visible = false;
               this.delegate.needChangePanelVisibility(false);
               this.notifyDataSetChanged();
            }
         }

         if (!var16) {
            this.searchEmojiByKeyword();
         }

      }
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      int var3 = var1.getItemViewType();
      byte var4 = 2;
      if (var3 != 0) {
         if (var3 == 1) {
            if (var2 == 0) {
               if (this.keywordResults.size() != 1) {
                  var4 = -1;
               }
            } else if (var2 == this.keywordResults.size() - 1) {
               var4 = 1;
            } else {
               var4 = 0;
            }

            ((EmojiReplacementCell)var1.itemView).setEmoji(((DataQuery.KeywordResult)this.keywordResults.get(var2)).emoji, var4);
         }
      } else {
         if (var2 == 0) {
            if (this.stickers.size() != 1) {
               var4 = -1;
            }
         } else if (var2 == this.stickers.size() - 1) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         StickerCell var5 = (StickerCell)var1.itemView;
         var5.setSticker((TLRPC.Document)this.stickers.get(var2), this.stickersParents.get(var2), var4);
         var5.setClearsInputField(true);
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var3;
      if (var2 != 0) {
         var3 = new EmojiReplacementCell(this.mContext);
      } else {
         var3 = new StickerCell(this.mContext);
      }

      return new RecyclerListView.Holder((View)var3);
   }

   public void onDestroy() {
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidFailedLoad);
   }

   public interface StickersAdapterDelegate {
      void needChangePanelVisibility(boolean var1);
   }
}
