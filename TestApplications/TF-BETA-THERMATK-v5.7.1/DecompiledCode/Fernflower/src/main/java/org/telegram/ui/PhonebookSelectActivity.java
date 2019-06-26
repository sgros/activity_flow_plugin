package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.PhonebookAdapter;
import org.telegram.ui.Adapters.PhonebookSearchAdapter;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class PhonebookSelectActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int search_button = 0;
   private PhonebookSelectActivity.PhonebookSelectActivityDelegate delegate;
   private EmptyTextProgressView emptyView;
   private RecyclerListView listView;
   private PhonebookAdapter listViewAdapter;
   private PhonebookSearchAdapter searchListViewAdapter;
   private boolean searchWas;
   private boolean searching;

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("SelectContact", 2131560680));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PhonebookSelectActivity.this.finishFragment();
            }

         }
      });
      super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            PhonebookSelectActivity.this.searchListViewAdapter.search((String)null);
            PhonebookSelectActivity.this.searching = false;
            PhonebookSelectActivity.this.searchWas = false;
            PhonebookSelectActivity.this.listView.setAdapter(PhonebookSelectActivity.this.listViewAdapter);
            PhonebookSelectActivity.this.listView.setSectionsType(1);
            PhonebookSelectActivity.this.listViewAdapter.notifyDataSetChanged();
            PhonebookSelectActivity.this.listView.setFastScrollVisible(true);
            PhonebookSelectActivity.this.listView.setVerticalScrollBarEnabled(false);
            PhonebookSelectActivity.this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
         }

         public void onSearchExpand() {
            PhonebookSelectActivity.this.searching = true;
         }

         public void onTextChanged(EditText var1) {
            if (PhonebookSelectActivity.this.searchListViewAdapter != null) {
               String var2 = var1.getText().toString();
               if (var2.length() != 0) {
                  PhonebookSelectActivity.this.searchWas = true;
               }

               PhonebookSelectActivity.this.searchListViewAdapter.search(var2);
            }
         }
      }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      this.searchListViewAdapter = new PhonebookSearchAdapter(var1) {
         protected void onUpdateSearchResults(String var1) {
            if (!TextUtils.isEmpty(var1) && PhonebookSelectActivity.this.listView != null && PhonebookSelectActivity.this.listView.getAdapter() != PhonebookSelectActivity.this.searchListViewAdapter) {
               PhonebookSelectActivity.this.listView.setAdapter(PhonebookSelectActivity.this.searchListViewAdapter);
               PhonebookSelectActivity.this.listView.setSectionsType(0);
               PhonebookSelectActivity.this.searchListViewAdapter.notifyDataSetChanged();
               PhonebookSelectActivity.this.listView.setFastScrollVisible(false);
               PhonebookSelectActivity.this.listView.setVerticalScrollBarEnabled(true);
               PhonebookSelectActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
            }

         }
      };
      this.listViewAdapter = new PhonebookAdapter(var1) {
         public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (PhonebookSelectActivity.this.listView.getAdapter() == this) {
               int var1 = super.getItemCount();
               RecyclerListView var2 = PhonebookSelectActivity.this.listView;
               boolean var3;
               if (var1 != 0) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var2.setFastScrollVisible(var3);
            }

         }
      };
      super.fragmentView = new FrameLayout(var1) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if (PhonebookSelectActivity.this.listView.getAdapter() == PhonebookSelectActivity.this.listViewAdapter) {
               if (PhonebookSelectActivity.this.emptyView.getVisibility() == 0) {
                  PhonebookSelectActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(74.0F));
               }
            } else {
               PhonebookSelectActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(0.0F));
            }

         }
      };
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.setShowAtCenter(true);
      this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
      this.emptyView.showTextView();
      var2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setSectionsType(1);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setFastScrollEnabled();
      this.listView.setEmptyView(this.emptyView);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setAdapter(this.listViewAdapter);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PhonebookSelectActivity$l53X2AdH9xoalnpyzoOuRLqxMRM(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1 && PhonebookSelectActivity.this.searching && PhonebookSelectActivity.this.searchWas) {
               AndroidUtilities.hideKeyboard(PhonebookSelectActivity.this.getParentActivity().getCurrentFocus());
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            super.onScrolled(var1, var2, var3);
         }
      });
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.contactsDidLoad) {
         PhonebookAdapter var4 = this.listViewAdapter;
         if (var4 != null) {
            var4.notifyDataSetChanged();
         }
      } else if (var1 == NotificationCenter.closeChats) {
         this.removeSelfFromStack();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc var1 = new _$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc(this);
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder");
      ThemeDescription var10 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      ThemeDescription var11 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{LetterSectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4");
      RecyclerListView var12 = this.listView;
      Paint var13 = Theme.dividerPaint;
      ThemeDescription var14 = new ThemeDescription(var12, 0, new Class[]{View.class}, var13, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var15 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder");
      ThemeDescription var16 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollActive");
      ThemeDescription var23 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollInactive");
      ThemeDescription var17 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollText");
      ThemeDescription var24 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var18 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var19 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var20 = this.listView;
      Drawable var21 = Theme.avatar_broadcastDrawable;
      Drawable var22 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var14, var15, var16, var23, var17, var24, var18, var19, new ThemeDescription(var20, 0, new Class[]{UserCell.class}, (Paint)null, new Drawable[]{var21, var22}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$PhonebookSelectActivity(View var1, int var2) {
      Object var5;
      if (this.searching && this.searchWas) {
         var5 = this.searchListViewAdapter.getItem(var2);
      } else {
         int var3 = this.listViewAdapter.getSectionForPosition(var2);
         var2 = this.listViewAdapter.getPositionInSectionForPosition(var2);
         if (var2 < 0 || var3 < 0) {
            return;
         }

         var5 = this.listViewAdapter.getItem(var3, var2);
      }

      if (var5 != null) {
         ContactsController.Contact var4;
         TLRPC.User var6;
         String var7;
         if (var5 instanceof ContactsController.Contact) {
            var4 = (ContactsController.Contact)var5;
            var6 = var4.user;
            if (var6 != null) {
               var7 = ContactsController.formatName(var6.first_name, var6.last_name);
            } else {
               var7 = "";
            }
         } else {
            var6 = (TLRPC.User)var5;
            var4 = new ContactsController.Contact();
            var4.first_name = var6.first_name;
            var4.last_name = var6.last_name;
            var4.phones.add(var6.phone);
            var4.user = var6;
            var7 = ContactsController.formatName(var4.first_name, var4.last_name);
         }

         PhonebookShareActivity var8 = new PhonebookShareActivity(var4, (Uri)null, (File)null, var7);
         var8.setDelegate(new _$$Lambda$PhonebookSelectActivity$4oi9dmdaHeu97U36787Z9FtySPo(this));
         this.presentFragment(var8);
      }

   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$2$PhonebookSelectActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof UserCell) {
               ((UserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$PhonebookSelectActivity(TLRPC.User var1) {
      this.removeSelfFromStack();
      this.delegate.didSelectContact(var1);
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
   }

   public void onPause() {
      super.onPause();
      ActionBar var1 = super.actionBar;
      if (var1 != null) {
         var1.closeSearchField();
      }

   }

   public void onResume() {
      super.onResume();
      PhonebookAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   public void setDelegate(PhonebookSelectActivity.PhonebookSelectActivityDelegate var1) {
      this.delegate = var1;
   }

   public interface PhonebookSelectActivityDelegate {
      void didSelectContact(TLRPC.User var1);
   }
}
