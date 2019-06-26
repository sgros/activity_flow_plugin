// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.CheckBoxUserCell;
import org.telegram.ui.Cells.CheckBoxCell;
import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import java.util.regex.Pattern;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.widget.FrameLayout;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.BottomSheet;

public class AdminLogFilterAlert extends BottomSheet
{
    private ListAdapter adapter;
    private int adminsRow;
    private int allAdminsRow;
    private ArrayList<TLRPC.ChannelParticipant> currentAdmins;
    private TLRPC.TL_channelAdminLogEventsFilter currentFilter;
    private AdminLogFilterAlertDelegate delegate;
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
    private BottomSheetCell saveButton;
    private int scrollOffsetY;
    private SparseArray<TLRPC.User> selectedAdmins;
    private Drawable shadowDrawable;
    private Pattern urlPattern;
    
    public AdminLogFilterAlert(final Context context, final TLRPC.TL_channelAdminLogEventsFilter tl_channelAdminLogEventsFilter, final SparseArray<TLRPC.User> sparseArray, final boolean isMegagroup) {
        super(context, false, 0);
        if (tl_channelAdminLogEventsFilter != null) {
            this.currentFilter = new TLRPC.TL_channelAdminLogEventsFilter();
            final TLRPC.TL_channelAdminLogEventsFilter currentFilter = this.currentFilter;
            currentFilter.join = tl_channelAdminLogEventsFilter.join;
            currentFilter.leave = tl_channelAdminLogEventsFilter.leave;
            currentFilter.invite = tl_channelAdminLogEventsFilter.invite;
            currentFilter.ban = tl_channelAdminLogEventsFilter.ban;
            currentFilter.unban = tl_channelAdminLogEventsFilter.unban;
            currentFilter.kick = tl_channelAdminLogEventsFilter.kick;
            currentFilter.unkick = tl_channelAdminLogEventsFilter.unkick;
            currentFilter.promote = tl_channelAdminLogEventsFilter.promote;
            currentFilter.demote = tl_channelAdminLogEventsFilter.demote;
            currentFilter.info = tl_channelAdminLogEventsFilter.info;
            currentFilter.settings = tl_channelAdminLogEventsFilter.settings;
            currentFilter.pinned = tl_channelAdminLogEventsFilter.pinned;
            currentFilter.edit = tl_channelAdminLogEventsFilter.edit;
            currentFilter.delete = tl_channelAdminLogEventsFilter.delete;
        }
        if (sparseArray != null) {
            this.selectedAdmins = (SparseArray<TLRPC.User>)sparseArray.clone();
        }
        this.isMegagroup = isMegagroup;
        int adminsRow;
        if (this.isMegagroup) {
            this.restrictionsRow = 1;
            adminsRow = 2;
        }
        else {
            this.restrictionsRow = -1;
            adminsRow = 1;
        }
        final int membersRow = adminsRow + 1;
        this.adminsRow = adminsRow;
        final int infoRow = membersRow + 1;
        this.membersRow = membersRow;
        final int deleteRow = infoRow + 1;
        this.infoRow = infoRow;
        final int editRow = deleteRow + 1;
        this.deleteRow = deleteRow;
        int n = editRow + 1;
        this.editRow = editRow;
        if (this.isMegagroup) {
            final int n2 = n + 1;
            this.pinnedRow = n;
            n = n2;
        }
        else {
            this.pinnedRow = -1;
        }
        this.leavingRow = n;
        this.allAdminsRow = n + 2;
        (this.shadowDrawable = context.getResources().getDrawable(2131165823).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogBackground"), PorterDuff$Mode.MULTIPLY));
        (super.containerView = (ViewGroup)new FrameLayout(context) {
            protected void onDraw(final Canvas canvas) {
                AdminLogFilterAlert.this.shadowDrawable.setBounds(0, AdminLogFilterAlert.this.scrollOffsetY - AdminLogFilterAlert.this.backgroundPaddingTop, this.getMeasuredWidth(), this.getMeasuredHeight());
                AdminLogFilterAlert.this.shadowDrawable.draw(canvas);
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && AdminLogFilterAlert.this.scrollOffsetY != 0 && motionEvent.getY() < AdminLogFilterAlert.this.scrollOffsetY) {
                    AdminLogFilterAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                AdminLogFilterAlert.this.updateLayout();
            }
            
            protected void onMeasure(final int n, int n2) {
                int size;
                n2 = (size = View$MeasureSpec.getSize(n2));
                if (Build$VERSION.SDK_INT >= 21) {
                    size = n2 - AndroidUtilities.statusBarHeight;
                }
                this.getMeasuredWidth();
                final int dp = AndroidUtilities.dp(48.0f);
                if (AdminLogFilterAlert.this.isMegagroup) {
                    n2 = 9;
                }
                else {
                    n2 = 7;
                }
                int a;
                n2 = (a = dp + n2 * AndroidUtilities.dp(48.0f) + AdminLogFilterAlert.this.backgroundPaddingTop);
                if (AdminLogFilterAlert.this.currentAdmins != null) {
                    a = n2 + ((AdminLogFilterAlert.this.currentAdmins.size() + 1) * AndroidUtilities.dp(48.0f) + AndroidUtilities.dp(20.0f));
                }
                final float n3 = (float)a;
                n2 = size / 5;
                int n4;
                if (n3 < n2 * 3.2f) {
                    n4 = 0;
                }
                else {
                    n4 = n2 * 2;
                }
                n2 = n4;
                if (n4 != 0) {
                    n2 = n4;
                    if (a < size) {
                        n2 = n4 - (size - a);
                    }
                }
                int access$400;
                if ((access$400 = n2) == 0) {
                    access$400 = AdminLogFilterAlert.this.backgroundPaddingTop;
                }
                if (AdminLogFilterAlert.this.listView.getPaddingTop() != access$400) {
                    AdminLogFilterAlert.this.ignoreLayout = true;
                    AdminLogFilterAlert.this.listView.setPadding(0, access$400, 0, 0);
                    AdminLogFilterAlert.this.ignoreLayout = false;
                }
                super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(Math.min(a, size), 1073741824));
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return !AdminLogFilterAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }
            
            public void requestLayout() {
                if (AdminLogFilterAlert.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setWillNotDraw(false);
        final ViewGroup containerView = super.containerView;
        final int backgroundPaddingLeft = super.backgroundPaddingLeft;
        containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
        (this.listView = new RecyclerListView(context) {
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                final ContentPreviewViewer instance = ContentPreviewViewer.getInstance();
                final RecyclerListView access$500 = AdminLogFilterAlert.this.listView;
                boolean b = false;
                final boolean onInterceptTouchEvent = instance.onInterceptTouchEvent(motionEvent, access$500, 0, null);
                if (super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent) {
                    b = true;
                }
                return b;
            }
            
            @Override
            public void requestLayout() {
                if (AdminLogFilterAlert.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(this.getContext(), 1, false));
        this.listView.setAdapter(this.adapter = new ListAdapter(context));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setClipToPadding(false);
        this.listView.setEnabled(true);
        this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                AdminLogFilterAlert.this.updateLayout();
            }
        });
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$AdminLogFilterAlert$PQg5JdNAPilzJKQuE1cFzZ18AW4(this));
        super.containerView.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        final View view = new View(context);
        view.setBackgroundResource(2131165408);
        super.containerView.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        (this.saveButton = new BottomSheetCell(context, 1)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.saveButton.setTextAndIcon(LocaleController.getString("Save", 2131560626).toUpperCase(), 0);
        this.saveButton.setTextColor(Theme.getColor("dialogTextBlue2"));
        this.saveButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AdminLogFilterAlert$P34SvMRGiZ3R_8FDoPqX6OJH8LA(this));
        super.containerView.addView((View)this.saveButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.adapter.notifyDataSetChanged();
    }
    
    @SuppressLint({ "NewApi" })
    private void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            final RecyclerListView listView = this.listView;
            listView.setTopGlowOffset(this.scrollOffsetY = listView.getPaddingTop());
            super.containerView.invalidate();
            return;
        }
        final View child = this.listView.getChildAt(0);
        final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findContainingViewHolder(child);
        int scrollOffsetY = child.getTop() - AndroidUtilities.dp(8.0f);
        if (scrollOffsetY <= 0 || holder == null || ((RecyclerView.ViewHolder)holder).getAdapterPosition() != 0) {
            scrollOffsetY = 0;
        }
        if (this.scrollOffsetY != scrollOffsetY) {
            this.listView.setTopGlowOffset(this.scrollOffsetY = scrollOffsetY);
            super.containerView.invalidate();
        }
    }
    
    @Override
    protected boolean canDismissWithSwipe() {
        return false;
    }
    
    public void setAdminLogFilterAlertDelegate(final AdminLogFilterAlertDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setCurrentAdmins(final ArrayList<TLRPC.ChannelParticipant> currentAdmins) {
        this.currentAdmins = currentAdmins;
        final ListAdapter adapter = this.adapter;
        if (adapter != null) {
            ((RecyclerView.Adapter)adapter).notifyDataSetChanged();
        }
    }
    
    public interface AdminLogFilterAlertDelegate
    {
        void didSelectRights(final TLRPC.TL_channelAdminLogEventsFilter p0, final SparseArray<TLRPC.User> p1);
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context context;
        
        public ListAdapter(final Context context) {
            this.context = context;
        }
        
        @Override
        public int getItemCount() {
            int n;
            if (AdminLogFilterAlert.this.isMegagroup) {
                n = 9;
            }
            else {
                n = 7;
            }
            int n2;
            if (AdminLogFilterAlert.this.currentAdmins != null) {
                n2 = AdminLogFilterAlert.this.currentAdmins.size() + 2;
            }
            else {
                n2 = 0;
            }
            return n + n2;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n < AdminLogFilterAlert.this.allAdminsRow - 1 || n == AdminLogFilterAlert.this.allAdminsRow) {
                return 0;
            }
            if (n == AdminLogFilterAlert.this.allAdminsRow - 1) {
                return 1;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = true;
            if (itemViewType == 1) {
                b = false;
            }
            return b;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = false;
            final boolean b2 = false;
            final boolean b3 = false;
            final boolean b4 = false;
            final boolean b5 = false;
            final boolean b6 = false;
            final boolean b7 = false;
            final boolean b8 = false;
            final boolean b9 = false;
            final boolean b10 = false;
            boolean b11 = false;
            final boolean b12 = true;
            if (itemViewType != 0) {
                if (itemViewType == 2) {
                    final CheckBoxUserCell checkBoxUserCell = (CheckBoxUserCell)viewHolder.itemView;
                    final int user_id = AdminLogFilterAlert.this.currentAdmins.get(n - AdminLogFilterAlert.this.allAdminsRow - 1).user_id;
                    final TLRPC.User user = MessagesController.getInstance(AdminLogFilterAlert.this.currentAccount).getUser(user_id);
                    final boolean b13 = AdminLogFilterAlert.this.selectedAdmins == null || AdminLogFilterAlert.this.selectedAdmins.indexOfKey(user_id) >= 0;
                    if (n != this.getItemCount() - 1) {
                        b11 = true;
                    }
                    checkBoxUserCell.setUser(user, b13, b11);
                }
            }
            else {
                final CheckBoxCell checkBoxCell = (CheckBoxCell)viewHolder.itemView;
                if (n == 0) {
                    final String string = LocaleController.getString("EventLogFilterAll", 2131559410);
                    if (AdminLogFilterAlert.this.currentFilter == null) {
                        b = true;
                    }
                    checkBoxCell.setText(string, "", b, true);
                }
                else if (n == AdminLogFilterAlert.this.restrictionsRow) {
                    final String string2 = LocaleController.getString("EventLogFilterNewRestrictions", 2131559418);
                    boolean b14 = false;
                    Label_0319: {
                        if (AdminLogFilterAlert.this.currentFilter != null) {
                            b14 = b2;
                            if (!AdminLogFilterAlert.this.currentFilter.kick) {
                                break Label_0319;
                            }
                            b14 = b2;
                            if (!AdminLogFilterAlert.this.currentFilter.ban) {
                                break Label_0319;
                            }
                            b14 = b2;
                            if (!AdminLogFilterAlert.this.currentFilter.unkick) {
                                break Label_0319;
                            }
                            b14 = b2;
                            if (!AdminLogFilterAlert.this.currentFilter.unban) {
                                break Label_0319;
                            }
                        }
                        b14 = true;
                    }
                    checkBoxCell.setText(string2, "", b14, true);
                }
                else if (n == AdminLogFilterAlert.this.adminsRow) {
                    final String string3 = LocaleController.getString("EventLogFilterNewAdmins", 2131559416);
                    boolean b15 = false;
                    Label_0400: {
                        if (AdminLogFilterAlert.this.currentFilter != null) {
                            b15 = b3;
                            if (!AdminLogFilterAlert.this.currentFilter.promote) {
                                break Label_0400;
                            }
                            b15 = b3;
                            if (!AdminLogFilterAlert.this.currentFilter.demote) {
                                break Label_0400;
                            }
                        }
                        b15 = true;
                    }
                    checkBoxCell.setText(string3, "", b15, true);
                }
                else if (n == AdminLogFilterAlert.this.membersRow) {
                    final String string4 = LocaleController.getString("EventLogFilterNewMembers", 2131559417);
                    boolean b16 = false;
                    Label_0481: {
                        if (AdminLogFilterAlert.this.currentFilter != null) {
                            b16 = b4;
                            if (!AdminLogFilterAlert.this.currentFilter.invite) {
                                break Label_0481;
                            }
                            b16 = b4;
                            if (!AdminLogFilterAlert.this.currentFilter.join) {
                                break Label_0481;
                            }
                        }
                        b16 = true;
                    }
                    checkBoxCell.setText(string4, "", b16, true);
                }
                else if (n == AdminLogFilterAlert.this.infoRow) {
                    if (AdminLogFilterAlert.this.isMegagroup) {
                        final String string5 = LocaleController.getString("EventLogFilterGroupInfo", 2131559414);
                        boolean b17 = false;
                        Label_0555: {
                            if (AdminLogFilterAlert.this.currentFilter != null) {
                                b17 = b5;
                                if (!AdminLogFilterAlert.this.currentFilter.info) {
                                    break Label_0555;
                                }
                            }
                            b17 = true;
                        }
                        checkBoxCell.setText(string5, "", b17, true);
                    }
                    else {
                        final String string6 = LocaleController.getString("EventLogFilterChannelInfo", 2131559411);
                        boolean b18 = false;
                        Label_0608: {
                            if (AdminLogFilterAlert.this.currentFilter != null) {
                                b18 = b6;
                                if (!AdminLogFilterAlert.this.currentFilter.info) {
                                    break Label_0608;
                                }
                            }
                            b18 = true;
                        }
                        checkBoxCell.setText(string6, "", b18, true);
                    }
                }
                else if (n == AdminLogFilterAlert.this.deleteRow) {
                    final String string7 = LocaleController.getString("EventLogFilterDeletedMessages", 2131559412);
                    boolean b19 = false;
                    Label_0672: {
                        if (AdminLogFilterAlert.this.currentFilter != null) {
                            b19 = b7;
                            if (!AdminLogFilterAlert.this.currentFilter.delete) {
                                break Label_0672;
                            }
                        }
                        b19 = true;
                    }
                    checkBoxCell.setText(string7, "", b19, true);
                }
                else if (n == AdminLogFilterAlert.this.editRow) {
                    final String string8 = LocaleController.getString("EventLogFilterEditedMessages", 2131559413);
                    boolean b20 = false;
                    Label_0736: {
                        if (AdminLogFilterAlert.this.currentFilter != null) {
                            b20 = b8;
                            if (!AdminLogFilterAlert.this.currentFilter.edit) {
                                break Label_0736;
                            }
                        }
                        b20 = true;
                    }
                    checkBoxCell.setText(string8, "", b20, true);
                }
                else if (n == AdminLogFilterAlert.this.pinnedRow) {
                    final String string9 = LocaleController.getString("EventLogFilterPinnedMessages", 2131559419);
                    boolean b21 = false;
                    Label_0800: {
                        if (AdminLogFilterAlert.this.currentFilter != null) {
                            b21 = b9;
                            if (!AdminLogFilterAlert.this.currentFilter.pinned) {
                                break Label_0800;
                            }
                        }
                        b21 = true;
                    }
                    checkBoxCell.setText(string9, "", b21, true);
                }
                else if (n == AdminLogFilterAlert.this.leavingRow) {
                    final String string10 = LocaleController.getString("EventLogFilterLeavingMembers", 2131559415);
                    boolean b22 = b12;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b22 = (AdminLogFilterAlert.this.currentFilter.leave && b12);
                    }
                    checkBoxCell.setText(string10, "", b22, false);
                }
                else if (n == AdminLogFilterAlert.this.allAdminsRow) {
                    final String string11 = LocaleController.getString("EventLogAllAdmins", 2131559384);
                    boolean b23 = b10;
                    if (AdminLogFilterAlert.this.selectedAdmins == null) {
                        b23 = true;
                    }
                    checkBoxCell.setText(string11, "", b23, true);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        o = null;
                    }
                    else {
                        o = new CheckBoxUserCell(this.context, true);
                    }
                }
                else {
                    final ShadowSectionCell shadowSectionCell = new ShadowSectionCell(this.context, 18);
                    o = new FrameLayout(this.context);
                    ((FrameLayout)o).addView((View)shadowSectionCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                    ((FrameLayout)o).setBackgroundColor(Theme.getColor("dialogBackgroundGray"));
                }
            }
            else {
                o = new CheckBoxCell(this.context, 1, 21);
                ((FrameLayout)o).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = true;
            final boolean b2 = true;
            final boolean b3 = true;
            final boolean b4 = true;
            final boolean b5 = true;
            final boolean b6 = true;
            final boolean b7 = true;
            final boolean b8 = true;
            final boolean b9 = true;
            final boolean b10 = true;
            final boolean b11 = true;
            if (itemViewType != 0) {
                if (itemViewType == 2) {
                    final CheckBoxUserCell checkBoxUserCell = (CheckBoxUserCell)viewHolder.itemView;
                    final int user_id = AdminLogFilterAlert.this.currentAdmins.get(adapterPosition - AdminLogFilterAlert.this.allAdminsRow - 1).user_id;
                    boolean b12 = b11;
                    if (AdminLogFilterAlert.this.selectedAdmins != null) {
                        b12 = (AdminLogFilterAlert.this.selectedAdmins.indexOfKey(user_id) >= 0 && b11);
                    }
                    checkBoxUserCell.setChecked(b12, false);
                }
            }
            else {
                final CheckBoxCell checkBoxCell = (CheckBoxCell)viewHolder.itemView;
                if (adapterPosition == 0) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b = false;
                    }
                    checkBoxCell.setChecked(b, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.restrictionsRow) {
                    boolean b13 = b2;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b13 = (AdminLogFilterAlert.this.currentFilter.kick && AdminLogFilterAlert.this.currentFilter.ban && AdminLogFilterAlert.this.currentFilter.unkick && AdminLogFilterAlert.this.currentFilter.unban && b2);
                    }
                    checkBoxCell.setChecked(b13, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.adminsRow) {
                    boolean b14 = b3;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b14 = (AdminLogFilterAlert.this.currentFilter.promote && AdminLogFilterAlert.this.currentFilter.demote && b3);
                    }
                    checkBoxCell.setChecked(b14, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.membersRow) {
                    boolean b15 = b4;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b15 = (AdminLogFilterAlert.this.currentFilter.invite && AdminLogFilterAlert.this.currentFilter.join && b4);
                    }
                    checkBoxCell.setChecked(b15, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.infoRow) {
                    boolean b16 = b5;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b16 = (AdminLogFilterAlert.this.currentFilter.info && b5);
                    }
                    checkBoxCell.setChecked(b16, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.deleteRow) {
                    boolean b17 = b6;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b17 = (AdminLogFilterAlert.this.currentFilter.delete && b6);
                    }
                    checkBoxCell.setChecked(b17, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.editRow) {
                    boolean b18 = b7;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b18 = (AdminLogFilterAlert.this.currentFilter.edit && b7);
                    }
                    checkBoxCell.setChecked(b18, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.pinnedRow) {
                    boolean b19 = b8;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b19 = (AdminLogFilterAlert.this.currentFilter.pinned && b8);
                    }
                    checkBoxCell.setChecked(b19, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.leavingRow) {
                    boolean b20 = b9;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        b20 = (AdminLogFilterAlert.this.currentFilter.leave && b9);
                    }
                    checkBoxCell.setChecked(b20, false);
                }
                else if (adapterPosition == AdminLogFilterAlert.this.allAdminsRow) {
                    checkBoxCell.setChecked(AdminLogFilterAlert.this.selectedAdmins == null && b10, false);
                }
            }
        }
    }
}
