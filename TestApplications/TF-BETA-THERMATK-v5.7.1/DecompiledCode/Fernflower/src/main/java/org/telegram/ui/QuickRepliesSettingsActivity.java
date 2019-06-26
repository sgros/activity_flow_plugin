package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class QuickRepliesSettingsActivity extends BaseFragment {
   private int explanationRow;
   private QuickRepliesSettingsActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int reply1Row;
   private int reply2Row;
   private int reply3Row;
   private int reply4Row;
   private int rowCount;
   private EditTextSettingsCell[] textCells = new EditTextSettingsCell[4];

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setTitle(LocaleController.getString("VoipQuickReplies", 2131561086));
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               QuickRepliesSettingsActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new QuickRepliesSettingsActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, EditTextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText");
      ThemeDescription var10 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var11 = this.listView;
      Paint var12 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, new ThemeDescription(var11, 0, new Class[]{View.class}, var12, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText")};
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.reply1Row = var1;
      var1 = this.rowCount++;
      this.reply2Row = var1;
      var1 = this.rowCount++;
      this.reply3Row = var1;
      var1 = this.rowCount++;
      this.reply4Row = var1;
      var1 = this.rowCount++;
      this.explanationRow = var1;
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      Activity var1 = this.getParentActivity();
      int var2 = 0;
      Editor var5 = var1.getSharedPreferences("mainconfig", 0).edit();

      while(true) {
         EditTextSettingsCell[] var3 = this.textCells;
         if (var2 >= var3.length) {
            var5.commit();
            return;
         }

         if (var3[var2] != null) {
            String var4 = var3[var2].getTextView().getText().toString();
            StringBuilder var6;
            if (!TextUtils.isEmpty(var4)) {
               var6 = new StringBuilder();
               var6.append("quick_reply_msg");
               var6.append(var2 + 1);
               var5.putString(var6.toString(), var4);
            } else {
               var6 = new StringBuilder();
               var6.append("quick_reply_msg");
               var6.append(var2 + 1);
               var5.remove(var6.toString());
            }
         }

         ++var2;
      }
   }

   public void onResume() {
      super.onResume();
      QuickRepliesSettingsActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return QuickRepliesSettingsActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == QuickRepliesSettingsActivity.this.explanationRow) {
            return 0;
         } else {
            return var1 != QuickRepliesSettingsActivity.this.reply1Row && var1 != QuickRepliesSettingsActivity.this.reply2Row && var1 != QuickRepliesSettingsActivity.this.reply3Row && var1 != QuickRepliesSettingsActivity.this.reply4Row ? 1 : var1 - QuickRepliesSettingsActivity.this.reply1Row + 9;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 != QuickRepliesSettingsActivity.this.reply1Row && var2 != QuickRepliesSettingsActivity.this.reply2Row && var2 != QuickRepliesSettingsActivity.this.reply3Row && var2 != QuickRepliesSettingsActivity.this.reply4Row) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 4) {
                  switch(var3) {
                  case 9:
                  case 10:
                  case 11:
                  case 12:
                     EditTextSettingsCell var4 = (EditTextSettingsCell)var1.itemView;
                     var3 = QuickRepliesSettingsActivity.this.reply1Row;
                     String var6 = null;
                     String var5;
                     if (var2 == var3) {
                        var6 = LocaleController.getString("QuickReplyDefault1", 2131560524);
                        var5 = "quick_reply_msg1";
                     } else if (var2 == QuickRepliesSettingsActivity.this.reply2Row) {
                        var6 = LocaleController.getString("QuickReplyDefault2", 2131560525);
                        var5 = "quick_reply_msg2";
                     } else if (var2 == QuickRepliesSettingsActivity.this.reply3Row) {
                        var6 = LocaleController.getString("QuickReplyDefault3", 2131560526);
                        var5 = "quick_reply_msg3";
                     } else if (var2 == QuickRepliesSettingsActivity.this.reply4Row) {
                        var6 = LocaleController.getString("QuickReplyDefault4", 2131560527);
                        var5 = "quick_reply_msg4";
                     } else {
                        var5 = null;
                     }

                     var4.setTextAndHint(QuickRepliesSettingsActivity.this.getParentActivity().getSharedPreferences("mainconfig", 0).getString(var5, ""), var6, true);
                  }
               } else {
                  ((TextCheckCell)var1.itemView).setTextAndCheck(LocaleController.getString("AllowCustomQuickReply", 2131558605), QuickRepliesSettingsActivity.this.getParentActivity().getSharedPreferences("mainconfig", 0).getBoolean("quick_reply_allow_custom", true), false);
               }
            } else {
               TextSettingsCell var7 = (TextSettingsCell)var1.itemView;
            }
         } else {
            TextInfoPrivacyCell var8 = (TextInfoPrivacyCell)var1.itemView;
            var8.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            var8.setText(LocaleController.getString("VoipQuickRepliesExplain", 2131561087));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 4) {
                  switch(var2) {
                  case 9:
                  case 10:
                  case 11:
                  case 12:
                     var3 = new EditTextSettingsCell(this.mContext);
                     ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     QuickRepliesSettingsActivity.this.textCells[var2 - 9] = (EditTextSettingsCell)var3;
                     break;
                  default:
                     var3 = null;
                  }
               } else {
                  var3 = new TextCheckCell(this.mContext);
                  ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextSettingsCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new TextInfoPrivacyCell(this.mContext);
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }
}
