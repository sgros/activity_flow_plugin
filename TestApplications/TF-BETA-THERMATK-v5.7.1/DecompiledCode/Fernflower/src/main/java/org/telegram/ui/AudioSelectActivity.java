package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AudioCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Components.RecyclerListView;

public class AudioSelectActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private ArrayList audioEntries = new ArrayList();
   private PickerBottomLayout bottomLayout;
   private AudioSelectActivity.AudioSelectActivityDelegate delegate;
   private RecyclerListView listView;
   private AudioSelectActivity.ListAdapter listViewAdapter;
   private boolean loadingAudio;
   private MessageObject playingAudio;
   private EmptyTextProgressView progressView;
   private LongSparseArray selectedAudios = new LongSparseArray();
   private View shadow;

   private void loadAudio() {
      this.loadingAudio = true;
      EmptyTextProgressView var1 = this.progressView;
      if (var1 != null) {
         var1.showProgress();
      }

      Utilities.globalQueue.postRunnable(new _$$Lambda$AudioSelectActivity$GEv7u1xk_gK3KYDecS_JYS1z0Do(this));
   }

   private void updateBottomLayoutCount() {
      this.bottomLayout.updateSelectedCount(this.selectedAudios.size(), true);
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      ActionBar var2 = super.actionBar;
      byte var3 = 1;
      var2.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("AttachMusic", 2131558726));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               AudioSelectActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var7 = (FrameLayout)super.fragmentView;
      this.progressView = new EmptyTextProgressView(var1);
      this.progressView.setText(LocaleController.getString("NoAudio", 2131559912));
      var7.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.progressView);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      RecyclerListView var4 = this.listView;
      AudioSelectActivity.ListAdapter var5 = new AudioSelectActivity.ListAdapter(var1);
      this.listViewAdapter = var5;
      var4.setAdapter(var5);
      var4 = this.listView;
      if (!LocaleController.isRTL) {
         var3 = 2;
      }

      var4.setVerticalScrollbarPosition(var3);
      var7.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$AudioSelectActivity$t7_oOYYhOZXWc6t1x4gHrcrcMM0(this)));
      this.bottomLayout = new PickerBottomLayout(var1, false);
      var7.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
      this.bottomLayout.cancelButton.setOnClickListener(new _$$Lambda$AudioSelectActivity$1kNFEVrMBVP4AVP1qEJ7AqtPUlc(this));
      this.bottomLayout.doneButton.setOnClickListener(new _$$Lambda$AudioSelectActivity$B7w290PyNFItb_f7pzlG0yYvSco(this));
      View var6 = new View(var1);
      var6.setBackgroundResource(2131165408);
      var7.addView(var6, LayoutHelper.createFrame(-1, 3.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
      if (this.loadingAudio) {
         this.progressView.showProgress();
      } else {
         this.progressView.showTextView();
      }

      this.updateBottomLayoutCount();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.closeChats) {
         this.removeSelfFromStack();
      } else if (var1 == NotificationCenter.messagePlayingDidReset || var1 == NotificationCenter.messagePlayingDidStart || var1 == NotificationCenter.messagePlayingPlayStateChanged) {
         AudioSelectActivity.ListAdapter var4 = this.listViewAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var3 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var7 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var8 = this.listView;
      Paint var9 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, new ThemeDescription(var8, 0, new Class[]{View.class}, var9, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.progressView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"), new ThemeDescription(this.progressView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"titleTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"genreTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"authorTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"timeTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{AudioCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "musicPicker_checkbox"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{AudioCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "musicPicker_checkboxCheck"), new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{AudioCell.class}, new String[]{"playButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "musicPicker_buttonIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{AudioCell.class}, new String[]{"playButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "musicPicker_buttonBackground"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PickerBottomLayout.class}, new String[]{"cancelButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "picker_enabledButton"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "picker_enabledButton"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "picker_disabledButton"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonBadgeTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "picker_badgeText"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonBadgeTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "picker_badge")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$AudioSelectActivity(View var1, int var2) {
      AudioCell var4 = (AudioCell)var1;
      MediaController.AudioEntry var3 = var4.getAudioEntry();
      if (this.selectedAudios.indexOfKey(var3.id) >= 0) {
         this.selectedAudios.remove(var3.id);
         var4.setChecked(false);
      } else {
         this.selectedAudios.put(var3.id, var3);
         var4.setChecked(true);
      }

      this.updateBottomLayoutCount();
   }

   // $FF: synthetic method
   public void lambda$createView$1$AudioSelectActivity(View var1) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$2$AudioSelectActivity(View var1) {
      if (this.delegate != null) {
         ArrayList var3 = new ArrayList();

         for(int var2 = 0; var2 < this.selectedAudios.size(); ++var2) {
            var3.add(((MediaController.AudioEntry)this.selectedAudios.valueAt(var2)).messageObject);
         }

         this.delegate.didSelectAudio(var3);
      }

      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$loadAudio$4$AudioSelectActivity() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$null$3$AudioSelectActivity(ArrayList var1) {
      this.audioEntries = var1;
      this.progressView.showTextView();
      this.listViewAdapter.notifyDataSetChanged();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      this.loadAudio();
      return true;
   }

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

   public void setDelegate(AudioSelectActivity.AudioSelectActivityDelegate var1) {
      this.delegate = var1;
   }

   public interface AudioSelectActivityDelegate {
      void didSelectAudio(ArrayList var1);
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public Object getItem(int var1) {
         return AudioSelectActivity.this.audioEntries.get(var1);
      }

      public int getItemCount() {
         return AudioSelectActivity.this.audioEntries.size();
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         return 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return true;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$AudioSelectActivity$ListAdapter(MessageObject var1) {
         AudioSelectActivity.this.playingAudio = var1;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         MediaController.AudioEntry var3 = (MediaController.AudioEntry)AudioSelectActivity.this.audioEntries.get(var2);
         AudioCell var8 = (AudioCell)var1.itemView;
         MediaController.AudioEntry var4 = (MediaController.AudioEntry)AudioSelectActivity.this.audioEntries.get(var2);
         int var5 = AudioSelectActivity.this.audioEntries.size();
         boolean var6 = true;
         boolean var7;
         if (var2 != var5 - 1) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (AudioSelectActivity.this.selectedAudios.indexOfKey(var3.id) < 0) {
            var6 = false;
         }

         var8.setAudio(var4, var7, var6);
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         AudioCell var3 = new AudioCell(this.mContext);
         var3.setDelegate(new _$$Lambda$AudioSelectActivity$ListAdapter$_RHioXAj8YlHkv8lhkCbfXzL_JQ(this));
         return new RecyclerListView.Holder(var3);
      }
   }
}
