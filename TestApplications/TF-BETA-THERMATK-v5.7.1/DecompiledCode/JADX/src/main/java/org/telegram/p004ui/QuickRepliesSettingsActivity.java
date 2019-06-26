package org.telegram.p004ui;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.EditTextSettingsCell;
import org.telegram.p004ui.Cells.TextCheckCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;

/* renamed from: org.telegram.ui.QuickRepliesSettingsActivity */
public class QuickRepliesSettingsActivity extends BaseFragment {
    private int explanationRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int reply1Row;
    private int reply2Row;
    private int reply3Row;
    private int reply4Row;
    private int rowCount;
    private EditTextSettingsCell[] textCells = new EditTextSettingsCell[4];

    /* renamed from: org.telegram.ui.QuickRepliesSettingsActivity$1 */
    class C43201 extends ActionBarMenuOnItemClick {
        C43201() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                QuickRepliesSettingsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.QuickRepliesSettingsActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return QuickRepliesSettingsActivity.this.rowCount;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                textInfoPrivacyCell.setText(LocaleController.getString("VoipQuickRepliesExplain", C1067R.string.VoipQuickRepliesExplain));
            } else if (itemViewType != 1) {
                String str = "mainconfig";
                if (itemViewType != 4) {
                    switch (itemViewType) {
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            String str2;
                            EditTextSettingsCell editTextSettingsCell = (EditTextSettingsCell) viewHolder.itemView;
                            String str3 = null;
                            if (i == QuickRepliesSettingsActivity.this.reply1Row) {
                                str3 = LocaleController.getString("QuickReplyDefault1", C1067R.string.QuickReplyDefault1);
                                str2 = "quick_reply_msg1";
                            } else if (i == QuickRepliesSettingsActivity.this.reply2Row) {
                                str3 = LocaleController.getString("QuickReplyDefault2", C1067R.string.QuickReplyDefault2);
                                str2 = "quick_reply_msg2";
                            } else if (i == QuickRepliesSettingsActivity.this.reply3Row) {
                                str3 = LocaleController.getString("QuickReplyDefault3", C1067R.string.QuickReplyDefault3);
                                str2 = "quick_reply_msg3";
                            } else if (i == QuickRepliesSettingsActivity.this.reply4Row) {
                                str3 = LocaleController.getString("QuickReplyDefault4", C1067R.string.QuickReplyDefault4);
                                str2 = "quick_reply_msg4";
                            } else {
                                str2 = null;
                            }
                            editTextSettingsCell.setTextAndHint(QuickRepliesSettingsActivity.this.getParentActivity().getSharedPreferences(str, 0).getString(str2, ""), str3, true);
                            return;
                        default:
                            return;
                    }
                }
                ((TextCheckCell) viewHolder.itemView).setTextAndCheck(LocaleController.getString("AllowCustomQuickReply", C1067R.string.AllowCustomQuickReply), QuickRepliesSettingsActivity.this.getParentActivity().getSharedPreferences(str, 0).getBoolean("quick_reply_allow_custom", true), false);
            } else {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            }
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == QuickRepliesSettingsActivity.this.reply1Row || adapterPosition == QuickRepliesSettingsActivity.this.reply2Row || adapterPosition == QuickRepliesSettingsActivity.this.reply3Row || adapterPosition == QuickRepliesSettingsActivity.this.reply4Row;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textSettingsCell;
            if (i != 0) {
                String str = Theme.key_windowBackgroundWhite;
                if (i == 1) {
                    textSettingsCell = new TextSettingsCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                } else if (i != 4) {
                    switch (i) {
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            textSettingsCell = new EditTextSettingsCell(this.mContext);
                            textSettingsCell.setBackgroundColor(Theme.getColor(str));
                            QuickRepliesSettingsActivity.this.textCells[i - 9] = textSettingsCell;
                            break;
                        default:
                            textSettingsCell = null;
                            break;
                    }
                } else {
                    textSettingsCell = new TextCheckCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                }
            } else {
                textSettingsCell = new TextInfoPrivacyCell(this.mContext);
            }
            textSettingsCell.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(textSettingsCell);
        }

        public int getItemViewType(int i) {
            if (i == QuickRepliesSettingsActivity.this.explanationRow) {
                return 0;
            }
            if (i == QuickRepliesSettingsActivity.this.reply1Row || i == QuickRepliesSettingsActivity.this.reply2Row || i == QuickRepliesSettingsActivity.this.reply3Row || i == QuickRepliesSettingsActivity.this.reply4Row) {
                return (i - QuickRepliesSettingsActivity.this.reply1Row) + 9;
            }
            return 1;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.reply1Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.reply2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.reply3Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.reply4Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.explanationRow = i;
        return true;
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString("VoipQuickReplies", C1067R.string.VoipQuickReplies));
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new C43201());
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.listView = new RecyclerListView(context);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        return this.fragmentView;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        int i = 0;
        Editor edit = getParentActivity().getSharedPreferences("mainconfig", 0).edit();
        while (true) {
            EditTextSettingsCell[] editTextSettingsCellArr = this.textCells;
            if (i < editTextSettingsCellArr.length) {
                if (editTextSettingsCellArr[i] != null) {
                    String obj = editTextSettingsCellArr[i].getTextView().getText().toString();
                    String str = "quick_reply_msg";
                    if (TextUtils.isEmpty(obj)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(i + 1);
                        edit.remove(stringBuilder.toString());
                    } else {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str);
                        stringBuilder2.append(i + 1);
                        edit.putString(stringBuilder2.toString(), obj);
                    }
                }
                i++;
            } else {
                edit.commit();
                return;
            }
        }
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[13];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, EditTextSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        View view = this.listView;
        int i = ThemeDescription.FLAG_TEXTCOLOR;
        Class[] clsArr = new Class[]{EditTextSettingsCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        r1[7] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[8] = new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        r1[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[10] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[11] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[12] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        return r1;
    }
}