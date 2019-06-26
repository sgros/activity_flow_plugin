package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ArchiveHintCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.DialogMeUrlCell;
import org.telegram.ui.Cells.DialogsEmptyCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class DialogsAdapter extends RecyclerListView.SelectionAdapter {
   private ArchiveHintCell archiveHintCell;
   private int currentAccount;
   private int currentCount;
   private boolean dialogsListFrozen;
   private int dialogsType;
   private int folderId;
   private boolean hasHints;
   private boolean isOnlySelect;
   private boolean isReordering;
   private Context mContext;
   private long openedDialogId;
   private ArrayList selectedDialogs;
   private boolean showArchiveHint;
   private boolean showContacts;

   public DialogsAdapter(Context var1, int var2, int var3, boolean var4) {
      this.currentAccount = UserConfig.selectedAccount;
      this.mContext = var1;
      this.dialogsType = var2;
      this.folderId = var3;
      this.isOnlySelect = var4;
      if (var3 == 0 && var2 == 0 && !var4) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.hasHints = var4;
      this.selectedDialogs = new ArrayList();
      if (this.folderId == 1) {
         SharedPreferences var5 = MessagesController.getGlobalMainSettings();
         this.showArchiveHint = var5.getBoolean("archivehint", true);
         var5.edit().putBoolean("archivehint", false).commit();
         if (this.showArchiveHint) {
            this.archiveHintCell = new ArchiveHintCell(var1);
         }
      }

   }

   public boolean addOrRemoveSelectedDialog(long var1, View var3) {
      if (this.selectedDialogs.contains(var1)) {
         this.selectedDialogs.remove(var1);
         if (var3 instanceof DialogCell) {
            ((DialogCell)var3).setChecked(false, true);
         }

         return false;
      } else {
         this.selectedDialogs.add(var1);
         if (var3 instanceof DialogCell) {
            ((DialogCell)var3).setChecked(true, true);
         }

         return true;
      }
   }

   public int fixPosition(int var1) {
      int var2 = var1;
      if (this.hasHints) {
         var2 = var1 - (MessagesController.getInstance(this.currentAccount).hintDialogs.size() + 2);
      }

      var1 = var2;
      if (this.showArchiveHint) {
         var1 = var2 - 2;
      }

      return var1;
   }

   public ViewPager getArchiveHintCellPager() {
      ArchiveHintCell var1 = this.archiveHintCell;
      ViewPager var2;
      if (var1 != null) {
         var2 = var1.getViewPager();
      } else {
         var2 = null;
      }

      return var2;
   }

   public TLObject getItem(int var1) {
      if (this.showContacts) {
         var1 -= 3;
         return var1 >= 0 && var1 < ContactsController.getInstance(this.currentAccount).contacts.size() ? MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)ContactsController.getInstance(this.currentAccount).contacts.get(var1)).user_id) : null;
      } else {
         int var2 = var1;
         if (this.showArchiveHint) {
            var2 = var1 - 2;
         }

         ArrayList var3 = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, this.dialogsListFrozen);
         var1 = var2;
         if (this.hasHints) {
            var1 = MessagesController.getInstance(this.currentAccount).hintDialogs.size() + 2;
            if (var2 < var1) {
               return (TLObject)MessagesController.getInstance(this.currentAccount).hintDialogs.get(var2 - 1);
            }

            var1 = var2 - var1;
         }

         return var1 >= 0 && var1 < var3.size() ? (TLObject)var3.get(var1) : null;
      }
   }

   public int getItemCount() {
      this.showContacts = false;
      int var1 = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, this.dialogsListFrozen).size();
      if (var1 != 0 || this.folderId == 0 && !MessagesController.getInstance(this.currentAccount).isLoadingDialogs(this.folderId)) {
         int var2;
         if (MessagesController.getInstance(this.currentAccount).isDialogsEndReached(this.folderId) && var1 != 0) {
            var2 = var1;
         } else {
            var2 = var1 + 1;
         }

         int var3;
         if (this.hasHints) {
            var3 = var2 + MessagesController.getInstance(this.currentAccount).hintDialogs.size() + 2;
         } else {
            var3 = var2;
            if (this.dialogsType == 0) {
               var3 = var2;
               if (var1 == 0) {
                  var3 = var2;
                  if (this.folderId == 0) {
                     if (ContactsController.getInstance(this.currentAccount).contacts.isEmpty() && ContactsController.getInstance(this.currentAccount).isLoadingContacts()) {
                        this.currentCount = 0;
                        return 0;
                     }

                     var3 = var2;
                     if (!ContactsController.getInstance(this.currentAccount).contacts.isEmpty()) {
                        var3 = var2 + ContactsController.getInstance(this.currentAccount).contacts.size() + 2;
                        this.showContacts = true;
                     }
                  }
               }
            }
         }

         var2 = var3;
         if (this.folderId == 1) {
            var2 = var3;
            if (this.showArchiveHint) {
               var2 = var3 + 2;
            }
         }

         var3 = var2;
         if (this.folderId == 0) {
            var3 = var2;
            if (var1 != 0) {
               var3 = var2 + 1;
            }
         }

         this.currentCount = var3;
         return var3;
      } else if (this.folderId == 1 && this.showArchiveHint) {
         this.currentCount = 2;
         return 2;
      } else {
         this.currentCount = 0;
         return 0;
      }
   }

   public int getItemViewType(int var1) {
      if (this.showContacts) {
         if (var1 == 0) {
            return 5;
         } else if (var1 == 1) {
            return 8;
         } else {
            return var1 == 2 ? 7 : 6;
         }
      } else {
         int var2;
         if (this.hasHints) {
            var2 = MessagesController.getInstance(this.currentAccount).hintDialogs.size();
            int var3 = var2 + 2;
            if (var1 < var3) {
               if (var1 == 0) {
                  return 2;
               }

               if (var1 == var2 + 1) {
                  return 3;
               }

               return 4;
            }

            var2 = var1 - var3;
         } else {
            var2 = var1;
            if (this.showArchiveHint) {
               if (var1 == 0) {
                  return 9;
               }

               if (var1 == 1) {
                  return 8;
               }

               var2 = var1 - 2;
            }
         }

         var1 = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, this.dialogsListFrozen).size();
         if (var2 == var1) {
            if (!MessagesController.getInstance(this.currentAccount).isDialogsEndReached(this.folderId)) {
               return 1;
            } else {
               return var1 == 0 ? 5 : 10;
            }
         } else {
            return var2 > var1 ? 10 : 0;
         }
      }
   }

   public ArrayList getSelectedDialogs() {
      return this.selectedDialogs;
   }

   public boolean hasSelectedDialogs() {
      ArrayList var1 = this.selectedDialogs;
      boolean var2;
      if (var1 != null && !var1.isEmpty()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isDataSetChanged() {
      int var1 = this.currentCount;
      int var2 = this.getItemCount();
      boolean var3 = true;
      boolean var4 = var3;
      if (var1 == var2) {
         if (var1 == 1) {
            var4 = var3;
         } else {
            var4 = false;
         }
      }

      return var4;
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      int var2 = var1.getItemViewType();
      boolean var3 = true;
      if (var2 == 1 || var2 == 5 || var2 == 3 || var2 == 8 || var2 == 7 || var2 == 9 || var2 == 10) {
         var3 = false;
      }

      return var3;
   }

   // $FF: synthetic method
   public void lambda$onCreateViewHolder$0$DialogsAdapter(View var1) {
      MessagesController.getInstance(this.currentAccount).hintDialogs.clear();
      MessagesController.getGlobalMainSettings().edit().remove("installReferer").commit();
      this.notifyDataSetChanged();
   }

   public void notifyDataSetChanged() {
      boolean var1;
      if (this.folderId == 0 && this.dialogsType == 0 && !this.isOnlySelect && !MessagesController.getInstance(this.currentAccount).hintDialogs.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.hasHints = var1;
      super.notifyDataSetChanged();
   }

   public void notifyItemMoved(int var1, int var2) {
      ArrayList var3 = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, false);
      int var4 = this.fixPosition(var1);
      int var5 = this.fixPosition(var2);
      TLRPC.Dialog var6 = (TLRPC.Dialog)var3.get(var4);
      TLRPC.Dialog var7 = (TLRPC.Dialog)var3.get(var5);
      int var8 = var6.pinnedNum;
      var6.pinnedNum = var7.pinnedNum;
      var7.pinnedNum = var8;
      Collections.swap(var3, var4, var5);
      super.notifyItemMoved(var1, var2);
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      int var3 = var1.getItemViewType();
      if (var3 != 0) {
         if (var3 != 4) {
            if (var3 != 5) {
               if (var3 == 6) {
                  ((UserCell)var1.itemView).setData(MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)ContactsController.getInstance(this.currentAccount).contacts.get(var2 - 3)).user_id), (CharSequence)null, (CharSequence)null, 0);
               }
            } else {
               ((DialogsEmptyCell)var1.itemView).setType(this.showContacts);
            }
         } else {
            ((DialogMeUrlCell)var1.itemView).setRecentMeUrl((TLRPC.RecentMeUrl)this.getItem(var2));
         }
      } else {
         DialogCell var8 = (DialogCell)var1.itemView;
         TLRPC.Dialog var4 = (TLRPC.Dialog)this.getItem(var2);
         TLRPC.Dialog var5 = (TLRPC.Dialog)this.getItem(var2 + 1);
         var3 = this.folderId;
         boolean var6 = true;
         boolean var7;
         if (var3 == 0) {
            if (var2 != this.getItemCount() - 2) {
               var7 = true;
            } else {
               var7 = false;
            }

            var8.useSeparator = var7;
         } else {
            if (var2 != this.getItemCount() - 1) {
               var7 = true;
            } else {
               var7 = false;
            }

            var8.useSeparator = var7;
         }

         if (var4.pinned && var5 != null && !var5.pinned) {
            var7 = true;
         } else {
            var7 = false;
         }

         var8.fullSeparator = var7;
         if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
            if (var4.id == this.openedDialogId) {
               var7 = var6;
            } else {
               var7 = false;
            }

            var8.setDialogSelected(var7);
         }

         var8.setChecked(this.selectedDialogs.contains(var4.id), false);
         var8.setDialog(var4, this.dialogsType, this.folderId);
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var7;
      switch(var2) {
      case 0:
         var7 = new DialogCell(this.mContext, true, false);
         break;
      case 1:
         var7 = new LoadingCell(this.mContext);
         break;
      case 2:
         var7 = new HeaderCell(this.mContext);
         ((HeaderCell)var7).setText(LocaleController.getString("RecentlyViewed", 2131560539));
         TextView var12 = new TextView(this.mContext);
         var12.setTextSize(1, 15.0F);
         var12.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var12.setTextColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
         var12.setText(LocaleController.getString("RecentlyViewedHide", 2131560540));
         boolean var4 = LocaleController.isRTL;
         byte var5 = 3;
         byte var6;
         if (var4) {
            var6 = 3;
         } else {
            var6 = 5;
         }

         var12.setGravity(var6 | 16);
         if (LocaleController.isRTL) {
            var6 = var5;
         } else {
            var6 = 5;
         }

         ((FrameLayout)var7).addView(var12, LayoutHelper.createFrame(-1, -1.0F, var6 | 48, 17.0F, 15.0F, 17.0F, 0.0F));
         var12.setOnClickListener(new _$$Lambda$DialogsAdapter$9rZHEUZrGiCwZ807CAFufwsMqa0(this));
         break;
      case 3:
         var7 = new FrameLayout(this.mContext) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(12.0F), 1073741824));
            }
         };
         ((FrameLayout)var7).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
         View var11 = new View(this.mContext);
         var11.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
         ((FrameLayout)var7).addView(var11, LayoutHelper.createFrame(-1, -1.0F));
         break;
      case 4:
         var7 = new DialogMeUrlCell(this.mContext);
         break;
      case 5:
         var7 = new DialogsEmptyCell(this.mContext);
         break;
      case 6:
         var7 = new UserCell(this.mContext, 8, 0, false);
         break;
      case 7:
         var7 = new HeaderCell(this.mContext);
         ((HeaderCell)var7).setText(LocaleController.getString("YourContacts", 2131561143));
         break;
      case 8:
         var7 = new ShadowSectionCell(this.mContext);
         Drawable var9 = Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow");
         CombinedDrawable var10 = new CombinedDrawable(new ColorDrawable(Theme.getColor("windowBackgroundGray")), var9);
         var10.setFullsize(true);
         ((View)var7).setBackgroundDrawable(var10);
         break;
      case 9:
         ArchiveHintCell var3 = this.archiveHintCell;
         var7 = var3;
         if (var3.getParent() != null) {
            ((ViewGroup)this.archiveHintCell.getParent()).removeView(this.archiveHintCell);
            var7 = var3;
         }
         break;
      default:
         var7 = new View(this.mContext) {
            protected void onMeasure(int var1, int var2) {
               int var3 = DialogsActivity.getDialogsArray(DialogsAdapter.this.currentAccount, DialogsAdapter.this.dialogsType, DialogsAdapter.this.folderId, DialogsAdapter.this.dialogsListFrozen).size();
               Object var4 = MessagesController.getInstance(DialogsAdapter.this.currentAccount).dialogs_dict.get(DialogObject.makeFolderDialogId(1));
               byte var5 = 0;
               boolean var6;
               if (var4 != null) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               int var7 = var5;
               if (var3 != 0) {
                  if (!var6) {
                     var7 = var5;
                  } else {
                     var7 = MeasureSpec.getSize(var2);
                     var2 = var7;
                     if (var7 == 0) {
                        View var9 = (View)this.getParent();
                        var2 = var7;
                        if (var9 != null) {
                           var2 = var9.getMeasuredHeight();
                        }
                     }

                     var7 = var2;
                     int var10;
                     if (var2 == 0) {
                        var7 = AndroidUtilities.displaySize.y;
                        var10 = ActionBar.getCurrentActionBarHeight();
                        if (VERSION.SDK_INT >= 21) {
                           var2 = AndroidUtilities.statusBarHeight;
                        } else {
                           var2 = 0;
                        }

                        var7 = var7 - var10 - var2;
                     }

                     float var8;
                     if (SharedConfig.useThreeLinesLayout) {
                        var8 = 78.0F;
                     } else {
                        var8 = 72.0F;
                     }

                     var2 = AndroidUtilities.dp(var8);
                     var10 = var3 * var2 + (var3 - 1);
                     if (var10 < var7) {
                        var7 = var7 - var10 + var2 + 1;
                     } else {
                        var10 -= var7;
                        ++var2;
                        var7 = var5;
                        if (var10 < var2) {
                           var7 = var2 - var10;
                        }
                     }
                  }
               }

               this.setMeasuredDimension(MeasureSpec.getSize(var1), var7);
            }
         };
      }

      byte var8;
      if (var2 == 5) {
         var8 = -1;
      } else {
         var8 = -2;
      }

      ((View)var7).setLayoutParams(new RecyclerView.LayoutParams(-1, var8));
      return new RecyclerListView.Holder((View)var7);
   }

   public void onReorderStateChanged(boolean var1) {
      this.isReordering = var1;
   }

   public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
      View var2 = var1.itemView;
      if (var2 instanceof DialogCell) {
         DialogCell var3 = (DialogCell)var2;
         var3.onReorderStateChanged(this.isReordering, false);
         var3.setDialogIndex(this.fixPosition(var1.getAdapterPosition()));
         var3.checkCurrentDialogIndex(this.dialogsListFrozen);
         var3.setChecked(this.selectedDialogs.contains(var3.getDialogId()), false);
      }

   }

   public void setDialogsListFrozen(boolean var1) {
      this.dialogsListFrozen = var1;
   }

   public void setOpenedDialogId(long var1) {
      this.openedDialogId = var1;
   }
}
