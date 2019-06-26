// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.ui.Cells.AudioCell;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.View$OnClickListener;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.messenger.Utilities;
import android.view.View;
import android.util.LongSparseArray;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.messenger.MediaController;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class AudioSelectActivity extends BaseFragment implements NotificationCenterDelegate
{
    private ArrayList<MediaController.AudioEntry> audioEntries;
    private PickerBottomLayout bottomLayout;
    private AudioSelectActivityDelegate delegate;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loadingAudio;
    private MessageObject playingAudio;
    private EmptyTextProgressView progressView;
    private LongSparseArray<MediaController.AudioEntry> selectedAudios;
    private View shadow;
    
    public AudioSelectActivity() {
        this.audioEntries = new ArrayList<MediaController.AudioEntry>();
        this.selectedAudios = (LongSparseArray<MediaController.AudioEntry>)new LongSparseArray();
    }
    
    private void loadAudio() {
        this.loadingAudio = true;
        final EmptyTextProgressView progressView = this.progressView;
        if (progressView != null) {
            progressView.showProgress();
        }
        Utilities.globalQueue.postRunnable(new _$$Lambda$AudioSelectActivity$GEv7u1xk_gK3KYDecS_JYS1z0Do(this));
    }
    
    private void updateBottomLayoutCount() {
        this.bottomLayout.updateSelectedCount(this.selectedAudios.size(), true);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        final ActionBar actionBar = super.actionBar;
        int verticalScrollbarPosition = 1;
        actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("AttachMusic", 2131558726));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    AudioSelectActivity.this.finishFragment();
                }
            }
        });
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.progressView = new EmptyTextProgressView(context)).setText(LocaleController.getString("NoAudio", 2131559912));
        frameLayout.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.progressView);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setAdapter(this.listViewAdapter = new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        if (!LocaleController.isRTL) {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$AudioSelectActivity$t7_oOYYhOZXWc6t1x4gHrcrcMM0(this));
        frameLayout.addView((View)(this.bottomLayout = new PickerBottomLayout(context, false)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 80));
        this.bottomLayout.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AudioSelectActivity$1kNFEVrMBVP4AVP1qEJ7AqtPUlc(this));
        this.bottomLayout.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AudioSelectActivity$B7w290PyNFItb_f7pzlG0yYvSco(this));
        final View view = new View(context);
        view.setBackgroundResource(2131165408);
        frameLayout.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        if (this.loadingAudio) {
            this.progressView.showProgress();
        }
        else {
            this.progressView.showTextView();
        }
        this.updateBottomLayoutCount();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.closeChats) {
            this.removeSelfFromStack();
        }
        else if (n == NotificationCenter.messagePlayingDidReset || n == NotificationCenter.messagePlayingDidStart || n == NotificationCenter.messagePlayingPlayStateChanged) {
            final ListAdapter listViewAdapter = this.listViewAdapter;
            if (listViewAdapter != null) {
                ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.progressView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { AudioCell.class }, new String[] { "titleTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { AudioCell.class }, new String[] { "genreTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { AudioCell.class }, new String[] { "authorTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { AudioCell.class }, new String[] { "timeTextView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[] { AudioCell.class }, new String[] { "checkBox" }, null, null, null, "musicPicker_checkbox"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { AudioCell.class }, new String[] { "checkBox" }, null, null, null, "musicPicker_checkboxCheck"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[] { AudioCell.class }, new String[] { "playButton" }, null, null, null, "musicPicker_buttonIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[] { AudioCell.class }, new String[] { "playButton" }, null, null, null, "musicPicker_buttonBackground"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { PickerBottomLayout.class }, new String[] { "cancelButton" }, null, null, null, "picker_enabledButton"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { PickerBottomLayout.class }, new String[] { "doneButtonTextView" }, null, null, null, "picker_enabledButton"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { PickerBottomLayout.class }, new String[] { "doneButtonTextView" }, null, null, null, "picker_disabledButton"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { PickerBottomLayout.class }, new String[] { "doneButtonBadgeTextView" }, null, null, null, "picker_badgeText"), new ThemeDescription((View)this.bottomLayout, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[] { PickerBottomLayout.class }, new String[] { "doneButtonBadgeTextView" }, null, null, null, "picker_badge") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        this.loadAudio();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        if (this.playingAudio != null && MediaController.getInstance().isPlayingMessage(this.playingAudio)) {
            MediaController.getInstance().cleanupPlayer(true, true);
        }
    }
    
    public void setDelegate(final AudioSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface AudioSelectActivityDelegate
    {
        void didSelectAudio(final ArrayList<MessageObject> p0);
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        public Object getItem(final int index) {
            return AudioSelectActivity.this.audioEntries.get(index);
        }
        
        @Override
        public int getItemCount() {
            return AudioSelectActivity.this.audioEntries.size();
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int n) {
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return true;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final MediaController.AudioEntry audioEntry = AudioSelectActivity.this.audioEntries.get(n);
            final AudioCell audioCell = (AudioCell)viewHolder.itemView;
            final MediaController.AudioEntry audioEntry2 = AudioSelectActivity.this.audioEntries.get(n);
            final int size = AudioSelectActivity.this.audioEntries.size();
            boolean b = true;
            final boolean b2 = n != size - 1;
            if (AudioSelectActivity.this.selectedAudios.indexOfKey(audioEntry.id) < 0) {
                b = false;
            }
            audioCell.setAudio(audioEntry2, b2, b);
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            final AudioCell audioCell = new AudioCell(this.mContext);
            audioCell.setDelegate((AudioCell.AudioCellDelegate)new _$$Lambda$AudioSelectActivity$ListAdapter$_RHioXAj8YlHkv8lhkCbfXzL_JQ(this));
            return new RecyclerListView.Holder((View)audioCell);
        }
    }
}
