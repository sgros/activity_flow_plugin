package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.CheckBoxUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;

public class AdminLogFilterAlert extends BottomSheet {
   private AdminLogFilterAlert.ListAdapter adapter;
   private int adminsRow;
   private int allAdminsRow;
   private ArrayList currentAdmins;
   private TLRPC.TL_channelAdminLogEventsFilter currentFilter;
   private AdminLogFilterAlert.AdminLogFilterAlertDelegate delegate;
   private int deleteRow;
   private int editRow;
   private boolean ignoreLayout;
   private int infoRow;
   private boolean isMegagroup;
   private int leavingRow;
   private RecyclerListView listView;
   private int membersRow;
   private FrameLayout pickerBottomLayout;
   private int pinnedRow;
   private int reqId;
   private int restrictionsRow;
   private BottomSheet.BottomSheetCell saveButton;
   private int scrollOffsetY;
   private SparseArray selectedAdmins;
   private Drawable shadowDrawable;
   private Pattern urlPattern;

   public AdminLogFilterAlert(Context var1, TLRPC.TL_channelAdminLogEventsFilter var2, SparseArray var3, boolean var4) {
      super(var1, false, 0);
      if (var2 != null) {
         this.currentFilter = new TLRPC.TL_channelAdminLogEventsFilter();
         TLRPC.TL_channelAdminLogEventsFilter var5 = this.currentFilter;
         var5.join = var2.join;
         var5.leave = var2.leave;
         var5.invite = var2.invite;
         var5.ban = var2.ban;
         var5.unban = var2.unban;
         var5.kick = var2.kick;
         var5.unkick = var2.unkick;
         var5.promote = var2.promote;
         var5.demote = var2.demote;
         var5.info = var2.info;
         var5.settings = var2.settings;
         var5.pinned = var2.pinned;
         var5.edit = var2.edit;
         var5.delete = var2.delete;
      }

      if (var3 != null) {
         this.selectedAdmins = var3.clone();
      }

      this.isMegagroup = var4;
      byte var6;
      if (this.isMegagroup) {
         this.restrictionsRow = 1;
         var6 = 2;
      } else {
         this.restrictionsRow = -1;
         var6 = 1;
      }

      int var7 = var6 + 1;
      this.adminsRow = var6;
      int var8 = var7 + 1;
      this.membersRow = var7;
      int var13 = var8 + 1;
      this.infoRow = var8;
      var8 = var13 + 1;
      this.deleteRow = var13;
      var13 = var8 + 1;
      this.editRow = var8;
      if (this.isMegagroup) {
         var8 = var13 + 1;
         this.pinnedRow = var13;
         var13 = var8;
      } else {
         this.pinnedRow = -1;
      }

      this.leavingRow = var13;
      this.allAdminsRow = var13 + 2;
      this.shadowDrawable = var1.getResources().getDrawable(2131165823).mutate();
      this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogBackground"), Mode.MULTIPLY));
      super.containerView = new FrameLayout(var1) {
         protected void onDraw(Canvas var1) {
            AdminLogFilterAlert.this.shadowDrawable.setBounds(0, AdminLogFilterAlert.this.scrollOffsetY - AdminLogFilterAlert.access$800(AdminLogFilterAlert.this), this.getMeasuredWidth(), this.getMeasuredHeight());
            AdminLogFilterAlert.this.shadowDrawable.draw(var1);
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0 && AdminLogFilterAlert.this.scrollOffsetY != 0 && var1.getY() < (float)AdminLogFilterAlert.this.scrollOffsetY) {
               AdminLogFilterAlert.this.dismiss();
               return true;
            } else {
               return super.onInterceptTouchEvent(var1);
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            AdminLogFilterAlert.this.updateLayout();
         }

         protected void onMeasure(int var1, int var2) {
            var2 = MeasureSpec.getSize(var2);
            int var3 = var2;
            if (VERSION.SDK_INT >= 21) {
               var3 = var2 - AndroidUtilities.statusBarHeight;
            }

            this.getMeasuredWidth();
            int var4 = AndroidUtilities.dp(48.0F);
            byte var7;
            if (AdminLogFilterAlert.this.isMegagroup) {
               var7 = 9;
            } else {
               var7 = 7;
            }

            var2 = var4 + var7 * AndroidUtilities.dp(48.0F) + AdminLogFilterAlert.access$200(AdminLogFilterAlert.this);
            int var5 = var2;
            if (AdminLogFilterAlert.this.currentAdmins != null) {
               var5 = var2 + (AdminLogFilterAlert.this.currentAdmins.size() + 1) * AndroidUtilities.dp(48.0F) + AndroidUtilities.dp(20.0F);
            }

            float var6 = (float)var5;
            var2 = var3 / 5;
            if (var6 < (float)var2 * 3.2F) {
               var4 = 0;
            } else {
               var4 = var2 * 2;
            }

            var2 = var4;
            if (var4 != 0) {
               var2 = var4;
               if (var5 < var3) {
                  var2 = var4 - (var3 - var5);
               }
            }

            var4 = var2;
            if (var2 == 0) {
               var4 = AdminLogFilterAlert.access$400(AdminLogFilterAlert.this);
            }

            if (AdminLogFilterAlert.this.listView.getPaddingTop() != var4) {
               AdminLogFilterAlert.this.ignoreLayout = true;
               AdminLogFilterAlert.this.listView.setPadding(0, var4, 0, 0);
               AdminLogFilterAlert.this.ignoreLayout = false;
            }

            super.onMeasure(var1, MeasureSpec.makeMeasureSpec(Math.min(var5, var3), 1073741824));
         }

         public boolean onTouchEvent(MotionEvent var1) {
            boolean var2;
            if (!AdminLogFilterAlert.this.isDismissed() && super.onTouchEvent(var1)) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }

         public void requestLayout() {
            if (!AdminLogFilterAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      super.containerView.setWillNotDraw(false);
      ViewGroup var9 = super.containerView;
      var13 = super.backgroundPaddingLeft;
      var9.setPadding(var13, 0, var13, 0);
      this.listView = new RecyclerListView(var1) {
         public boolean onInterceptTouchEvent(MotionEvent var1) {
            ContentPreviewViewer var2 = ContentPreviewViewer.getInstance();
            RecyclerListView var3 = AdminLogFilterAlert.this.listView;
            boolean var4 = false;
            boolean var5 = var2.onInterceptTouchEvent(var1, var3, 0, (ContentPreviewViewer.ContentPreviewViewerDelegate)null);
            if (super.onInterceptTouchEvent(var1) || var5) {
               var4 = true;
            }

            return var4;
         }

         public void requestLayout() {
            if (!AdminLogFilterAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      this.listView.setLayoutManager(new LinearLayoutManager(this.getContext(), 1, false));
      RecyclerListView var12 = this.listView;
      AdminLogFilterAlert.ListAdapter var10 = new AdminLogFilterAlert.ListAdapter(var1);
      this.adapter = var10;
      var12.setAdapter(var10);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setClipToPadding(false);
      this.listView.setEnabled(true);
      this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            AdminLogFilterAlert.this.updateLayout();
         }
      });
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$AdminLogFilterAlert$PQg5JdNAPilzJKQuE1cFzZ18AW4(this)));
      super.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      View var11 = new View(var1);
      var11.setBackgroundResource(2131165408);
      super.containerView.addView(var11, LayoutHelper.createFrame(-1, 3.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
      this.saveButton = new BottomSheet.BottomSheetCell(var1, 1);
      this.saveButton.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      this.saveButton.setTextAndIcon(LocaleController.getString("Save", 2131560626).toUpperCase(), 0);
      this.saveButton.setTextColor(Theme.getColor("dialogTextBlue2"));
      this.saveButton.setOnClickListener(new _$$Lambda$AdminLogFilterAlert$P34SvMRGiZ3R_8FDoPqX6OJH8LA(this));
      super.containerView.addView(this.saveButton, LayoutHelper.createFrame(-1, 48, 83));
      this.adapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   static int access$200(AdminLogFilterAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2100(AdminLogFilterAlert var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$400(AdminLogFilterAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$800(AdminLogFilterAlert var0) {
      return var0.backgroundPaddingTop;
   }

   @SuppressLint({"NewApi"})
   private void updateLayout() {
      int var2;
      RecyclerListView var4;
      if (this.listView.getChildCount() <= 0) {
         var4 = this.listView;
         var2 = var4.getPaddingTop();
         this.scrollOffsetY = var2;
         var4.setTopGlowOffset(var2);
         super.containerView.invalidate();
      } else {
         View var3 = this.listView.getChildAt(0);
         RecyclerListView.Holder var1 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var3);
         var2 = var3.getTop() - AndroidUtilities.dp(8.0F);
         if (var2 <= 0 || var1 == null || var1.getAdapterPosition() != 0) {
            var2 = 0;
         }

         if (this.scrollOffsetY != var2) {
            var4 = this.listView;
            this.scrollOffsetY = var2;
            var4.setTopGlowOffset(var2);
            super.containerView.invalidate();
         }

      }
   }

   protected boolean canDismissWithSwipe() {
      return false;
   }

   // $FF: synthetic method
   public void lambda$new$0$AdminLogFilterAlert(View var1, int var2) {
      boolean var3 = var1 instanceof CheckBoxCell;
      byte var4 = 0;
      RecyclerView.ViewHolder var11;
      if (var3) {
         CheckBoxCell var7 = (CheckBoxCell)var1;
         var3 = var7.isChecked();
         var7.setChecked(var3 ^ true, true);
         TLRPC.TL_channelAdminLogEventsFilter var8;
         int var10;
         if (var2 == 0) {
            if (var3) {
               this.currentFilter = new TLRPC.TL_channelAdminLogEventsFilter();
               var8 = this.currentFilter;
               var8.delete = false;
               var8.edit = false;
               var8.pinned = false;
               var8.settings = false;
               var8.info = false;
               var8.demote = false;
               var8.promote = false;
               var8.unkick = false;
               var8.kick = false;
               var8.unban = false;
               var8.ban = false;
               var8.invite = false;
               var8.leave = false;
               var8.join = false;
            } else {
               this.currentFilter = null;
            }

            var10 = this.listView.getChildCount();

            for(var2 = 0; var2 < var10; ++var2) {
               var1 = this.listView.getChildAt(var2);
               var11 = this.listView.findContainingViewHolder(var1);
               int var6 = var11.getAdapterPosition();
               if (var11.getItemViewType() == 0 && var6 > 0 && var6 < this.allAdminsRow - 1) {
                  ((CheckBoxCell)var1).setChecked(var3 ^ true, true);
               }
            }
         } else {
            RecyclerView.ViewHolder var9;
            if (var2 == this.allAdminsRow) {
               if (var3) {
                  this.selectedAdmins = new SparseArray();
               } else {
                  this.selectedAdmins = null;
               }

               var10 = this.listView.getChildCount();

               for(var2 = 0; var2 < var10; ++var2) {
                  View var5 = this.listView.getChildAt(var2);
                  var9 = this.listView.findContainingViewHolder(var5);
                  var9.getAdapterPosition();
                  if (var9.getItemViewType() == 2) {
                     ((CheckBoxUserCell)var5).setChecked(var3 ^ true, true);
                  }
               }
            } else {
               if (this.currentFilter == null) {
                  this.currentFilter = new TLRPC.TL_channelAdminLogEventsFilter();
                  var8 = this.currentFilter;
                  var8.delete = true;
                  var8.edit = true;
                  var8.pinned = true;
                  var8.settings = true;
                  var8.info = true;
                  var8.demote = true;
                  var8.promote = true;
                  var8.unkick = true;
                  var8.kick = true;
                  var8.unban = true;
                  var8.ban = true;
                  var8.invite = true;
                  var8.leave = true;
                  var8.join = true;
                  var9 = this.listView.findViewHolderForAdapterPosition(0);
                  if (var9 != null) {
                     ((CheckBoxCell)var9.itemView).setChecked(false, true);
                  }
               }

               if (var2 == this.restrictionsRow) {
                  var8 = this.currentFilter;
                  var3 = var8.kick ^ true;
                  var8.unban = var3;
                  var8.unkick = var3;
                  var8.ban = var3;
                  var8.kick = var3;
               } else if (var2 == this.adminsRow) {
                  var8 = this.currentFilter;
                  var3 = var8.demote ^ true;
                  var8.demote = var3;
                  var8.promote = var3;
               } else if (var2 == this.membersRow) {
                  var8 = this.currentFilter;
                  var3 = var8.join ^ true;
                  var8.join = var3;
                  var8.invite = var3;
               } else if (var2 == this.infoRow) {
                  var8 = this.currentFilter;
                  var3 = var8.info ^ true;
                  var8.settings = var3;
                  var8.info = var3;
               } else if (var2 == this.deleteRow) {
                  var8 = this.currentFilter;
                  var8.delete ^= true;
               } else if (var2 == this.editRow) {
                  var8 = this.currentFilter;
                  var8.edit ^= true;
               } else if (var2 == this.pinnedRow) {
                  var8 = this.currentFilter;
                  var8.pinned ^= true;
               } else if (var2 == this.leavingRow) {
                  var8 = this.currentFilter;
                  var8.leave ^= true;
               }
            }
         }

         var8 = this.currentFilter;
         if (var8 != null && !var8.join) {
            var3 = var8.leave;
            if (!var3 && !var3 && !var8.invite && !var8.ban && !var8.unban && !var8.kick && !var8.unkick && !var8.promote && !var8.demote && !var8.info && !var8.settings && !var8.pinned && !var8.edit && !var8.delete) {
               this.saveButton.setEnabled(false);
               this.saveButton.setAlpha(0.5F);
               return;
            }
         }

         this.saveButton.setEnabled(true);
         this.saveButton.setAlpha(1.0F);
      } else if (var1 instanceof CheckBoxUserCell) {
         CheckBoxUserCell var13 = (CheckBoxUserCell)var1;
         TLRPC.User var12;
         if (this.selectedAdmins == null) {
            this.selectedAdmins = new SparseArray();
            var11 = this.listView.findViewHolderForAdapterPosition(this.allAdminsRow);
            var2 = var4;
            if (var11 != null) {
               ((CheckBoxCell)var11.itemView).setChecked(false, true);
               var2 = var4;
            }

            while(var2 < this.currentAdmins.size()) {
               var12 = MessagesController.getInstance(super.currentAccount).getUser(((TLRPC.ChannelParticipant)this.currentAdmins.get(var2)).user_id);
               this.selectedAdmins.put(var12.id, var12);
               ++var2;
            }
         }

         var3 = var13.isChecked();
         var12 = var13.getCurrentUser();
         if (var3) {
            this.selectedAdmins.remove(var12.id);
         } else {
            this.selectedAdmins.put(var12.id, var12);
         }

         var13.setChecked(var3 ^ true, true);
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$AdminLogFilterAlert(View var1) {
      this.delegate.didSelectRights(this.currentFilter, this.selectedAdmins);
      this.dismiss();
   }

   public void setAdminLogFilterAlertDelegate(AdminLogFilterAlert.AdminLogFilterAlertDelegate var1) {
      this.delegate = var1;
   }

   public void setCurrentAdmins(ArrayList var1) {
      this.currentAdmins = var1;
      AdminLogFilterAlert.ListAdapter var2 = this.adapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

   }

   public interface AdminLogFilterAlertDelegate {
      void didSelectRights(TLRPC.TL_channelAdminLogEventsFilter var1, SparseArray var2);
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;

      public ListAdapter(Context var2) {
         this.context = var2;
      }

      public int getItemCount() {
         byte var1;
         if (AdminLogFilterAlert.this.isMegagroup) {
            var1 = 9;
         } else {
            var1 = 7;
         }

         int var2;
         if (AdminLogFilterAlert.this.currentAdmins != null) {
            var2 = AdminLogFilterAlert.this.currentAdmins.size() + 2;
         } else {
            var2 = 0;
         }

         return var1 + var2;
      }

      public int getItemViewType(int var1) {
         if (var1 >= AdminLogFilterAlert.this.allAdminsRow - 1 && var1 != AdminLogFilterAlert.this.allAdminsRow) {
            return var1 == AdminLogFilterAlert.this.allAdminsRow - 1 ? 1 : 2;
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3 = true;
         if (var2 == 1) {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;
         boolean var11 = false;
         boolean var12 = false;
         boolean var13 = false;
         boolean var14 = false;
         boolean var15 = true;
         if (var3 != 0) {
            if (var3 == 2) {
               CheckBoxUserCell var16 = (CheckBoxUserCell)var1.itemView;
               var3 = ((TLRPC.ChannelParticipant)AdminLogFilterAlert.this.currentAdmins.get(var2 - AdminLogFilterAlert.this.allAdminsRow - 1)).user_id;
               TLRPC.User var17 = MessagesController.getInstance(AdminLogFilterAlert.access$2100(AdminLogFilterAlert.this)).getUser(var3);
               if (AdminLogFilterAlert.this.selectedAdmins != null && AdminLogFilterAlert.this.selectedAdmins.indexOfKey(var3) < 0) {
                  var4 = false;
               } else {
                  var4 = true;
               }

               if (var2 != this.getItemCount() - 1) {
                  var14 = true;
               }

               var16.setUser(var17, var4, var14);
            }
         } else {
            CheckBoxCell var18 = (CheckBoxCell)var1.itemView;
            String var19;
            if (var2 == 0) {
               var19 = LocaleController.getString("EventLogFilterAll", 2131559410);
               if (AdminLogFilterAlert.this.currentFilter == null) {
                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            } else if (var2 == AdminLogFilterAlert.this.restrictionsRow) {
               label147: {
                  var19 = LocaleController.getString("EventLogFilterNewRestrictions", 2131559418);
                  if (AdminLogFilterAlert.this.currentFilter != null) {
                     var4 = var5;
                     if (!AdminLogFilterAlert.this.currentFilter.kick) {
                        break label147;
                     }

                     var4 = var5;
                     if (!AdminLogFilterAlert.this.currentFilter.ban) {
                        break label147;
                     }

                     var4 = var5;
                     if (!AdminLogFilterAlert.this.currentFilter.unkick) {
                        break label147;
                     }

                     var4 = var5;
                     if (!AdminLogFilterAlert.this.currentFilter.unban) {
                        break label147;
                     }
                  }

                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            } else if (var2 == AdminLogFilterAlert.this.adminsRow) {
               label135: {
                  var19 = LocaleController.getString("EventLogFilterNewAdmins", 2131559416);
                  if (AdminLogFilterAlert.this.currentFilter != null) {
                     var4 = var6;
                     if (!AdminLogFilterAlert.this.currentFilter.promote) {
                        break label135;
                     }

                     var4 = var6;
                     if (!AdminLogFilterAlert.this.currentFilter.demote) {
                        break label135;
                     }
                  }

                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            } else if (var2 == AdminLogFilterAlert.this.membersRow) {
               label127: {
                  var19 = LocaleController.getString("EventLogFilterNewMembers", 2131559417);
                  if (AdminLogFilterAlert.this.currentFilter != null) {
                     var4 = var7;
                     if (!AdminLogFilterAlert.this.currentFilter.invite) {
                        break label127;
                     }

                     var4 = var7;
                     if (!AdminLogFilterAlert.this.currentFilter.join) {
                        break label127;
                     }
                  }

                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            } else if (var2 == AdminLogFilterAlert.this.infoRow) {
               if (AdminLogFilterAlert.this.isMegagroup) {
                  label118: {
                     var19 = LocaleController.getString("EventLogFilterGroupInfo", 2131559414);
                     if (AdminLogFilterAlert.this.currentFilter != null) {
                        var4 = var8;
                        if (!AdminLogFilterAlert.this.currentFilter.info) {
                           break label118;
                        }
                     }

                     var4 = true;
                  }

                  var18.setText(var19, "", var4, true);
               } else {
                  label113: {
                     var19 = LocaleController.getString("EventLogFilterChannelInfo", 2131559411);
                     if (AdminLogFilterAlert.this.currentFilter != null) {
                        var4 = var9;
                        if (!AdminLogFilterAlert.this.currentFilter.info) {
                           break label113;
                        }
                     }

                     var4 = true;
                  }

                  var18.setText(var19, "", var4, true);
               }
            } else if (var2 == AdminLogFilterAlert.this.deleteRow) {
               label107: {
                  var19 = LocaleController.getString("EventLogFilterDeletedMessages", 2131559412);
                  if (AdminLogFilterAlert.this.currentFilter != null) {
                     var4 = var10;
                     if (!AdminLogFilterAlert.this.currentFilter.delete) {
                        break label107;
                     }
                  }

                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            } else if (var2 == AdminLogFilterAlert.this.editRow) {
               label101: {
                  var19 = LocaleController.getString("EventLogFilterEditedMessages", 2131559413);
                  if (AdminLogFilterAlert.this.currentFilter != null) {
                     var4 = var11;
                     if (!AdminLogFilterAlert.this.currentFilter.edit) {
                        break label101;
                     }
                  }

                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            } else if (var2 == AdminLogFilterAlert.this.pinnedRow) {
               label95: {
                  var19 = LocaleController.getString("EventLogFilterPinnedMessages", 2131559419);
                  if (AdminLogFilterAlert.this.currentFilter != null) {
                     var4 = var12;
                     if (!AdminLogFilterAlert.this.currentFilter.pinned) {
                        break label95;
                     }
                  }

                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            } else if (var2 == AdminLogFilterAlert.this.leavingRow) {
               var19 = LocaleController.getString("EventLogFilterLeavingMembers", 2131559415);
               var4 = var15;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.leave) {
                     var4 = var15;
                  } else {
                     var4 = false;
                  }
               }

               var18.setText(var19, "", var4, false);
            } else if (var2 == AdminLogFilterAlert.this.allAdminsRow) {
               var19 = LocaleController.getString("EventLogAllAdmins", 2131559384);
               var4 = var13;
               if (AdminLogFilterAlert.this.selectedAdmins == null) {
                  var4 = true;
               }

               var18.setText(var19, "", var4, true);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  var4 = null;
               } else {
                  var4 = new CheckBoxUserCell(this.context, true);
               }
            } else {
               ShadowSectionCell var3 = new ShadowSectionCell(this.context, 18);
               var4 = new FrameLayout(this.context);
               ((FrameLayout)var4).addView(var3, LayoutHelper.createFrame(-1, -1.0F));
               ((FrameLayout)var4).setBackgroundColor(Theme.getColor("dialogBackgroundGray"));
            }
         } else {
            var4 = new CheckBoxCell(this.context, 1, 21);
            ((FrameLayout)var4).setBackgroundDrawable(Theme.getSelectorDrawable(false));
         }

         return new RecyclerListView.Holder((View)var4);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         int var3 = var1.getItemViewType();
         boolean var4 = true;
         boolean var5 = true;
         boolean var6 = true;
         boolean var7 = true;
         boolean var8 = true;
         boolean var9 = true;
         boolean var10 = true;
         boolean var11 = true;
         boolean var12 = true;
         boolean var13 = true;
         boolean var14 = true;
         if (var3 != 0) {
            if (var3 == 2) {
               CheckBoxUserCell var15 = (CheckBoxUserCell)var1.itemView;
               var2 = ((TLRPC.ChannelParticipant)AdminLogFilterAlert.this.currentAdmins.get(var2 - AdminLogFilterAlert.this.allAdminsRow - 1)).user_id;
               var4 = var14;
               if (AdminLogFilterAlert.this.selectedAdmins != null) {
                  if (AdminLogFilterAlert.this.selectedAdmins.indexOfKey(var2) >= 0) {
                     var4 = var14;
                  } else {
                     var4 = false;
                  }
               }

               var15.setChecked(var4, false);
            }
         } else {
            CheckBoxCell var16 = (CheckBoxCell)var1.itemView;
            if (var2 == 0) {
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  var4 = false;
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.restrictionsRow) {
               var4 = var5;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.kick && AdminLogFilterAlert.this.currentFilter.ban && AdminLogFilterAlert.this.currentFilter.unkick && AdminLogFilterAlert.this.currentFilter.unban) {
                     var4 = var5;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.adminsRow) {
               var4 = var6;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.promote && AdminLogFilterAlert.this.currentFilter.demote) {
                     var4 = var6;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.membersRow) {
               var4 = var7;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.invite && AdminLogFilterAlert.this.currentFilter.join) {
                     var4 = var7;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.infoRow) {
               var4 = var8;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.info) {
                     var4 = var8;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.deleteRow) {
               var4 = var9;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.delete) {
                     var4 = var9;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.editRow) {
               var4 = var10;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.edit) {
                     var4 = var10;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.pinnedRow) {
               var4 = var11;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.pinned) {
                     var4 = var11;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.leavingRow) {
               var4 = var12;
               if (AdminLogFilterAlert.this.currentFilter != null) {
                  if (AdminLogFilterAlert.this.currentFilter.leave) {
                     var4 = var12;
                  } else {
                     var4 = false;
                  }
               }

               var16.setChecked(var4, false);
            } else if (var2 == AdminLogFilterAlert.this.allAdminsRow) {
               if (AdminLogFilterAlert.this.selectedAdmins == null) {
                  var4 = var13;
               } else {
                  var4 = false;
               }

               var16.setChecked(var4, false);
            }
         }

      }
   }
}
