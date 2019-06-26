// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.content.SharedPreferences$Editor;
import android.app.Activity;
import android.text.TextUtils;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ActionBar.BaseFragment;

public class QuickRepliesSettingsActivity extends BaseFragment
{
    private int explanationRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int reply1Row;
    private int reply2Row;
    private int reply3Row;
    private int reply4Row;
    private int rowCount;
    private EditTextSettingsCell[] textCells;
    
    public QuickRepliesSettingsActivity() {
        this.textCells = new EditTextSettingsCell[4];
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setTitle(LocaleController.getString("VoipQuickReplies", 2131561086));
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    QuickRepliesSettingsActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, TextCheckCell.class, EditTextSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { EditTextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[] { EditTextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        this.reply1Row = this.rowCount++;
        this.reply2Row = this.rowCount++;
        this.reply3Row = this.rowCount++;
        this.reply4Row = this.rowCount++;
        this.explanationRow = this.rowCount++;
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        final Activity parentActivity = this.getParentActivity();
        int n = 0;
        final SharedPreferences$Editor edit = parentActivity.getSharedPreferences("mainconfig", 0).edit();
        while (true) {
            final EditTextSettingsCell[] textCells = this.textCells;
            if (n >= textCells.length) {
                break;
            }
            if (textCells[n] != null) {
                final String string = textCells[n].getTextView().getText().toString();
                if (!TextUtils.isEmpty((CharSequence)string)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("quick_reply_msg");
                    sb.append(n + 1);
                    edit.putString(sb.toString(), string);
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("quick_reply_msg");
                    sb2.append(n + 1);
                    edit.remove(sb2.toString());
                }
            }
            ++n;
        }
        edit.commit();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return QuickRepliesSettingsActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == QuickRepliesSettingsActivity.this.explanationRow) {
                return 0;
            }
            if (n != QuickRepliesSettingsActivity.this.reply1Row && n != QuickRepliesSettingsActivity.this.reply2Row && n != QuickRepliesSettingsActivity.this.reply3Row && n != QuickRepliesSettingsActivity.this.reply4Row) {
                return 1;
            }
            return n - QuickRepliesSettingsActivity.this.reply1Row + 9;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == QuickRepliesSettingsActivity.this.reply1Row || adapterPosition == QuickRepliesSettingsActivity.this.reply2Row || adapterPosition == QuickRepliesSettingsActivity.this.reply3Row || adapterPosition == QuickRepliesSettingsActivity.this.reply4Row;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 4) {
                        switch (itemViewType) {
                            case 9:
                            case 10:
                            case 11:
                            case 12: {
                                final EditTextSettingsCell editTextSettingsCell = (EditTextSettingsCell)viewHolder.itemView;
                                final int access$100 = QuickRepliesSettingsActivity.this.reply1Row;
                                String s = null;
                                String s2;
                                if (n == access$100) {
                                    s = LocaleController.getString("QuickReplyDefault1", 2131560524);
                                    s2 = "quick_reply_msg1";
                                }
                                else if (n == QuickRepliesSettingsActivity.this.reply2Row) {
                                    s = LocaleController.getString("QuickReplyDefault2", 2131560525);
                                    s2 = "quick_reply_msg2";
                                }
                                else if (n == QuickRepliesSettingsActivity.this.reply3Row) {
                                    s = LocaleController.getString("QuickReplyDefault3", 2131560526);
                                    s2 = "quick_reply_msg3";
                                }
                                else if (n == QuickRepliesSettingsActivity.this.reply4Row) {
                                    s = LocaleController.getString("QuickReplyDefault4", 2131560527);
                                    s2 = "quick_reply_msg4";
                                }
                                else {
                                    s2 = null;
                                }
                                editTextSettingsCell.setTextAndHint(QuickRepliesSettingsActivity.this.getParentActivity().getSharedPreferences("mainconfig", 0).getString(s2, ""), s, true);
                                break;
                            }
                        }
                    }
                    else {
                        ((TextCheckCell)viewHolder.itemView).setTextAndCheck(LocaleController.getString("AllowCustomQuickReply", 2131558605), QuickRepliesSettingsActivity.this.getParentActivity().getSharedPreferences("mainconfig", 0).getBoolean("quick_reply_allow_custom", true), false);
                    }
                }
                else {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                }
            }
            else {
                final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                textInfoPrivacyCell.setText(LocaleController.getString("VoipQuickRepliesExplain", 2131561087));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    if (n != 4) {
                        switch (n) {
                            default: {
                                frameLayout = null;
                                break;
                            }
                            case 9:
                            case 10:
                            case 11:
                            case 12: {
                                frameLayout = new EditTextSettingsCell(this.mContext);
                                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                QuickRepliesSettingsActivity.this.textCells[n - 9] = (EditTextSettingsCell)frameLayout;
                                break;
                            }
                        }
                    }
                    else {
                        frameLayout = new TextCheckCell(this.mContext);
                        ((TextCheckCell)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    frameLayout = new TextSettingsCell(this.mContext);
                    ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                frameLayout = new TextInfoPrivacyCell(this.mContext);
            }
            ((View)frameLayout).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}
