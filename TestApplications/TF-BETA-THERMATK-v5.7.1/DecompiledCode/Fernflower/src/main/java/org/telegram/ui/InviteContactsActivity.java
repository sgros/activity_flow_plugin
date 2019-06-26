package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import org.telegram.ui.Cells.InviteTextCell;
import org.telegram.ui.Cells.InviteUserCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class InviteContactsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, OnClickListener {
   private InviteContactsActivity.InviteAdapter adapter;
   private ArrayList allSpans = new ArrayList();
   private int containerHeight;
   private TextView counterTextView;
   private FrameLayout counterView;
   private GroupCreateSpan currentDeletingSpan;
   private GroupCreateDividerItemDecoration decoration;
   private EditTextBoldCursor editText;
   private EmptyTextProgressView emptyView;
   private int fieldY;
   private boolean ignoreScrollEvent;
   private TextView infoTextView;
   private RecyclerListView listView;
   private ArrayList phoneBookContacts;
   private ScrollView scrollView;
   private boolean searchWas;
   private boolean searching;
   private HashMap selectedContacts = new HashMap();
   private InviteContactsActivity.SpansContainer spansContainer;
   private TextView textView;

   // $FF: synthetic method
   static ActionBarLayout access$1500(InviteContactsActivity var0) {
      return var0.parentLayout;
   }

   private void checkVisibleRows() {
      int var1 = this.listView.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.listView.getChildAt(var2);
         if (var3 instanceof InviteUserCell) {
            InviteUserCell var4 = (InviteUserCell)var3;
            ContactsController.Contact var5 = var4.getContact();
            if (var5 != null) {
               var4.setChecked(this.selectedContacts.containsKey(var5.key), true);
            }
         }
      }

   }

   private void closeSearch() {
      this.searching = false;
      this.searchWas = false;
      this.adapter.setSearching(false);
      this.adapter.searchDialogs((String)null);
      this.listView.setFastScrollVisible(true);
      this.listView.setVerticalScrollBarEnabled(false);
      this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
   }

   private void fetchContacts() {
      this.phoneBookContacts = new ArrayList(ContactsController.getInstance(super.currentAccount).phoneBookContacts);
      Collections.sort(this.phoneBookContacts, _$$Lambda$InviteContactsActivity$r58ALapXATHsxuXB3Kf3_z6GjIA.INSTANCE);
      EmptyTextProgressView var1 = this.emptyView;
      if (var1 != null) {
         var1.showTextView();
      }

      InviteContactsActivity.InviteAdapter var2 = this.adapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   static int lambda$fetchContacts$2(ContactsController.Contact var0, ContactsController.Contact var1) {
      int var2 = var0.imported;
      int var3 = var1.imported;
      if (var2 > var3) {
         return -1;
      } else {
         return var2 < var3 ? 1 : 0;
      }
   }

   private void updateHint() {
      if (this.selectedContacts.isEmpty()) {
         this.infoTextView.setVisibility(0);
         this.counterView.setVisibility(4);
      } else {
         this.infoTextView.setVisibility(4);
         this.counterView.setVisibility(0);
         this.counterTextView.setText(String.format("%d", this.selectedContacts.size()));
      }

   }

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      this.allSpans.clear();
      this.selectedContacts.clear();
      this.currentDeletingSpan = null;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("InviteFriends", 2131559677));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               InviteContactsActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new ViewGroup(var1) {
         protected boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 == InviteContactsActivity.this.listView || var2 == InviteContactsActivity.this.emptyView) {
               InviteContactsActivity.access$1500(InviteContactsActivity.this).drawHeaderShadow(var1, InviteContactsActivity.this.scrollView.getMeasuredHeight());
            }

            return var5;
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            InviteContactsActivity.this.scrollView.layout(0, 0, InviteContactsActivity.this.scrollView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight());
            InviteContactsActivity.this.listView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight(), InviteContactsActivity.this.listView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.listView.getMeasuredHeight());
            InviteContactsActivity.this.emptyView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight() + AndroidUtilities.dp(72.0F), InviteContactsActivity.this.emptyView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.emptyView.getMeasuredHeight());
            var2 = var5 - var3;
            var3 = var2 - InviteContactsActivity.this.infoTextView.getMeasuredHeight();
            InviteContactsActivity.this.infoTextView.layout(0, var3, InviteContactsActivity.this.infoTextView.getMeasuredWidth(), InviteContactsActivity.this.infoTextView.getMeasuredHeight() + var3);
            var2 -= InviteContactsActivity.this.counterView.getMeasuredHeight();
            InviteContactsActivity.this.counterView.layout(0, var2, InviteContactsActivity.this.counterView.getMeasuredWidth(), InviteContactsActivity.this.counterView.getMeasuredHeight() + var2);
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var1);
            int var4 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var4);
            if (!AndroidUtilities.isTablet() && var4 <= var3) {
               var1 = AndroidUtilities.dp(56.0F);
            } else {
               var1 = AndroidUtilities.dp(144.0F);
            }

            InviteContactsActivity.this.infoTextView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
            InviteContactsActivity.this.counterView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
            if (InviteContactsActivity.this.infoTextView.getVisibility() == 0) {
               var2 = InviteContactsActivity.this.infoTextView.getMeasuredHeight();
            } else {
               var2 = InviteContactsActivity.this.counterView.getMeasuredHeight();
            }

            InviteContactsActivity.this.scrollView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
            InviteContactsActivity.this.listView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4 - InviteContactsActivity.this.scrollView.getMeasuredHeight() - var2, 1073741824));
            InviteContactsActivity.this.emptyView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4 - InviteContactsActivity.this.scrollView.getMeasuredHeight() - AndroidUtilities.dp(72.0F), 1073741824));
         }
      };
      ViewGroup var2 = (ViewGroup)super.fragmentView;
      this.scrollView = new ScrollView(var1) {
         public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
            if (InviteContactsActivity.this.ignoreScrollEvent) {
               InviteContactsActivity.this.ignoreScrollEvent = false;
               return false;
            } else {
               var2.offset(var1.getLeft() - var1.getScrollX(), var1.getTop() - var1.getScrollY());
               var2.top += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(20.0F);
               var2.bottom += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(50.0F);
               return super.requestChildRectangleOnScreen(var1, var2, var3);
            }
         }
      };
      this.scrollView.setVerticalScrollBarEnabled(false);
      AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("windowBackgroundWhite"));
      var2.addView(this.scrollView);
      this.spansContainer = new InviteContactsActivity.SpansContainer(var1);
      this.scrollView.addView(this.spansContainer, LayoutHelper.createFrame(-1, -2.0F));
      this.editText = new EditTextBoldCursor(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (InviteContactsActivity.this.currentDeletingSpan != null) {
               InviteContactsActivity.this.currentDeletingSpan.cancelDeleteAnimation();
               InviteContactsActivity.this.currentDeletingSpan = null;
            }

            if (var1.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
               this.clearFocus();
               this.requestFocus();
            }

            return super.onTouchEvent(var1);
         }
      };
      this.editText.setTextSize(1, 18.0F);
      this.editText.setHintColor(Theme.getColor("groupcreate_hintText"));
      this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.editText.setCursorColor(Theme.getColor("groupcreate_cursor"));
      this.editText.setCursorWidth(1.5F);
      this.editText.setInputType(655536);
      this.editText.setSingleLine(true);
      this.editText.setBackgroundDrawable((Drawable)null);
      this.editText.setVerticalScrollBarEnabled(false);
      this.editText.setHorizontalScrollBarEnabled(false);
      this.editText.setTextIsSelectable(false);
      this.editText.setPadding(0, 0, 0, 0);
      this.editText.setImeOptions(268435462);
      EditTextBoldCursor var3 = this.editText;
      byte var4;
      if (LocaleController.isRTL) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var3.setGravity(var4 | 16);
      this.spansContainer.addView(this.editText);
      this.editText.setHintText(LocaleController.getString("SearchFriends", 2131560646));
      this.editText.setCustomSelectionActionModeCallback(new Callback() {
         public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
            return false;
         }

         public boolean onCreateActionMode(ActionMode var1, Menu var2) {
            return false;
         }

         public void onDestroyActionMode(ActionMode var1) {
         }

         public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
            return false;
         }
      });
      this.editText.setOnKeyListener(new OnKeyListener() {
         private boolean wasEmpty;

         public boolean onKey(View var1, int var2, KeyEvent var3) {
            var2 = var3.getAction();
            boolean var4 = true;
            if (var2 == 0) {
               if (InviteContactsActivity.this.editText.length() != 0) {
                  var4 = false;
               }

               this.wasEmpty = var4;
            } else if (var3.getAction() == 1 && this.wasEmpty && !InviteContactsActivity.this.allSpans.isEmpty()) {
               InviteContactsActivity.this.spansContainer.removeSpan((GroupCreateSpan)InviteContactsActivity.this.allSpans.get(InviteContactsActivity.this.allSpans.size() - 1));
               InviteContactsActivity.this.updateHint();
               InviteContactsActivity.this.checkVisibleRows();
               return true;
            }

            return false;
         }
      });
      this.editText.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
            if (InviteContactsActivity.this.editText.length() != 0) {
               InviteContactsActivity.this.searching = true;
               InviteContactsActivity.this.searchWas = true;
               InviteContactsActivity.this.adapter.setSearching(true);
               InviteContactsActivity.this.adapter.searchDialogs(InviteContactsActivity.this.editText.getText().toString());
               InviteContactsActivity.this.listView.setFastScrollVisible(false);
               InviteContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
               InviteContactsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
            } else {
               InviteContactsActivity.this.closeSearch();
            }

         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      this.emptyView = new EmptyTextProgressView(var1);
      if (ContactsController.getInstance(super.currentAccount).isLoadingContacts()) {
         this.emptyView.showProgress();
      } else {
         this.emptyView.showTextView();
      }

      this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
      var2.addView(this.emptyView);
      LinearLayoutManager var5 = new LinearLayoutManager(var1, 1, false);
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      RecyclerListView var8 = this.listView;
      InviteContactsActivity.InviteAdapter var6 = new InviteContactsActivity.InviteAdapter(var1);
      this.adapter = var6;
      var8.setAdapter(var6);
      this.listView.setLayoutManager(var5);
      this.listView.setVerticalScrollBarEnabled(true);
      var8 = this.listView;
      if (LocaleController.isRTL) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      var8.setVerticalScrollbarPosition(var4);
      var8 = this.listView;
      GroupCreateDividerItemDecoration var9 = new GroupCreateDividerItemDecoration();
      this.decoration = var9;
      var8.addItemDecoration(var9);
      var2.addView(this.listView);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$InviteContactsActivity$2CAh12ObsNHUdlUsHSs_VZmRL0I(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1) {
               AndroidUtilities.hideKeyboard(InviteContactsActivity.this.editText);
            }

         }
      });
      this.infoTextView = new TextView(var1);
      this.infoTextView.setBackgroundColor(Theme.getColor("contacts_inviteBackground"));
      this.infoTextView.setTextColor(Theme.getColor("contacts_inviteText"));
      this.infoTextView.setGravity(17);
      this.infoTextView.setText(LocaleController.getString("InviteFriendsHelp", 2131559678));
      this.infoTextView.setTextSize(1, 13.0F);
      this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.infoTextView.setPadding(AndroidUtilities.dp(17.0F), AndroidUtilities.dp(9.0F), AndroidUtilities.dp(17.0F), AndroidUtilities.dp(9.0F));
      var2.addView(this.infoTextView, LayoutHelper.createFrame(-1, -2, 83));
      this.counterView = new FrameLayout(var1);
      this.counterView.setBackgroundColor(Theme.getColor("contacts_inviteBackground"));
      this.counterView.setVisibility(4);
      var2.addView(this.counterView, LayoutHelper.createFrame(-1, 48, 83));
      this.counterView.setOnClickListener(new _$$Lambda$InviteContactsActivity$nq_MZzgy9JlkAxS_tdx9DVT5pQg(this));
      LinearLayout var7 = new LinearLayout(var1);
      var7.setOrientation(0);
      this.counterView.addView(var7, LayoutHelper.createFrame(-2, -1, 17));
      this.counterTextView = new TextView(var1);
      this.counterTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.counterTextView.setTextSize(1, 14.0F);
      this.counterTextView.setTextColor(Theme.getColor("contacts_inviteBackground"));
      this.counterTextView.setGravity(17);
      this.counterTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0F), Theme.getColor("contacts_inviteText")));
      this.counterTextView.setMinWidth(AndroidUtilities.dp(20.0F));
      this.counterTextView.setPadding(AndroidUtilities.dp(6.0F), 0, AndroidUtilities.dp(6.0F), AndroidUtilities.dp(1.0F));
      var7.addView(this.counterTextView, LayoutHelper.createLinear(-2, 20, 16, 0, 0, 10, 0));
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 14.0F);
      this.textView.setTextColor(Theme.getColor("contacts_inviteText"));
      this.textView.setGravity(17);
      this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0F));
      this.textView.setText(LocaleController.getString("InviteToTelegram", 2131559690).toUpperCase());
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var7.addView(this.textView, LayoutHelper.createLinear(-2, -2, 16));
      this.updateHint();
      this.adapter.notifyDataSetChanged();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.contactsImported) {
         this.fetchContacts();
      }

   }

   public int getContainerHeight() {
      return this.containerHeight;
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI var1 = new _$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI(this);
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      ThemeDescription var10 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollActive");
      ThemeDescription var11 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollInactive");
      ThemeDescription var12 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollText");
      RecyclerListView var13 = this.listView;
      Paint var14 = Theme.dividerPaint;
      ThemeDescription var15 = new ThemeDescription(var13, 0, new Class[]{View.class}, var14, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var16 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder");
      ThemeDescription var17 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle");
      ThemeDescription var31 = new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var30 = new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_hintText");
      ThemeDescription var18 = new ThemeDescription(this.editText, ThemeDescription.FLAG_CURSORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_cursor");
      ThemeDescription var19 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GroupCreateSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection");
      ThemeDescription var20 = new ThemeDescription(this.listView, 0, new Class[]{GroupCreateSectionCell.class}, new String[]{"drawable"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_sectionShadow");
      ThemeDescription var21 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateSectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_sectionText");
      ThemeDescription var22 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_sectionText");
      ThemeDescription var23 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkbox");
      ThemeDescription var24 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxCheck");
      ThemeDescription var25 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{InviteUserCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText");
      ThemeDescription var26 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{InviteUserCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText");
      RecyclerListView var27 = this.listView;
      Drawable var28 = Theme.avatar_broadcastDrawable;
      Drawable var29 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var15, var16, var17, var31, var30, var18, var19, var20, var21, var22, var23, var24, var25, var26, new ThemeDescription(var27, 0, new Class[]{InviteUserCell.class}, (Paint)null, new Drawable[]{var28, var29}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.listView, 0, new Class[]{InviteTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{InviteTextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundGroupCreateSpanBlue"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_spanBackground"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_spanText"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_spanDelete"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundBlue"), new ThemeDescription(this.infoTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contacts_inviteText"), new ThemeDescription(this.infoTextView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contacts_inviteBackground"), new ThemeDescription(this.counterView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contacts_inviteBackground"), new ThemeDescription(this.counterTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contacts_inviteBackground"), new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contacts_inviteText"), new ThemeDescription(this.counterTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contacts_inviteText")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$InviteContactsActivity(View var1, int var2) {
      if (var2 == 0 && !this.searching) {
         try {
            Intent var7 = new Intent("android.intent.action.SEND");
            var7.setType("text/plain");
            String var9 = ContactsController.getInstance(super.currentAccount).getInviteText(0);
            var7.putExtra("android.intent.extra.TEXT", var9);
            this.getParentActivity().startActivityForResult(Intent.createChooser(var7, var9), 500);
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

      } else if (var1 instanceof InviteUserCell) {
         InviteUserCell var6 = (InviteUserCell)var1;
         ContactsController.Contact var3 = var6.getContact();
         if (var3 != null) {
            boolean var4 = this.selectedContacts.containsKey(var3.key);
            GroupCreateSpan var8;
            if (var4) {
               var8 = (GroupCreateSpan)this.selectedContacts.get(var3.key);
               this.spansContainer.removeSpan(var8);
            } else {
               var8 = new GroupCreateSpan(this.editText.getContext(), var3);
               this.spansContainer.addSpan(var8);
               var8.setOnClickListener(this);
            }

            this.updateHint();
            if (!this.searching && !this.searchWas) {
               var6.setChecked(var4 ^ true, true);
            } else {
               AndroidUtilities.showKeyboard(this.editText);
            }

            if (this.editText.length() > 0) {
               this.editText.setText((CharSequence)null);
            }

         }
      }
   }

   // $FF: synthetic method
   public void lambda$createView$1$InviteContactsActivity(View var1) {
      label55: {
         Exception var10000;
         label57: {
            StringBuilder var12;
            boolean var10001;
            try {
               var12 = new StringBuilder();
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label57;
            }

            int var2 = 0;
            int var3 = 0;

            while(true) {
               ContactsController.Contact var4;
               try {
                  if (var2 >= this.allSpans.size()) {
                     break;
                  }

                  var4 = ((GroupCreateSpan)this.allSpans.get(var2)).getContact();
                  if (var12.length() != 0) {
                     var12.append(';');
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label57;
               }

               try {
                  var12.append((String)var4.phones.get(0));
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label57;
               }

               int var5 = var3;
               if (var2 == 0) {
                  var5 = var3;

                  try {
                     if (this.allSpans.size() == 1) {
                        var5 = var4.imported;
                     }
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label57;
                  }
               }

               ++var2;
               var3 = var5;
            }

            try {
               StringBuilder var6 = new StringBuilder();
               var6.append("smsto:");
               var6.append(var12.toString());
               Intent var14 = new Intent("android.intent.action.SENDTO", Uri.parse(var6.toString()));
               var14.putExtra("sms_body", ContactsController.getInstance(super.currentAccount).getInviteText(var3));
               this.getParentActivity().startActivityForResult(var14, 500);
               break label55;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         Exception var13 = var10000;
         FileLog.e((Throwable)var13);
      }

      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$3$InviteContactsActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof InviteUserCell) {
               ((InviteUserCell)var4).update(0);
            }
         }
      }

   }

   public void onClick(View var1) {
      GroupCreateSpan var2 = (GroupCreateSpan)var1;
      if (var2.isDeleting()) {
         this.currentDeletingSpan = null;
         this.spansContainer.removeSpan(var2);
         this.updateHint();
         this.checkVisibleRows();
      } else {
         GroupCreateSpan var3 = this.currentDeletingSpan;
         if (var3 != null) {
            var3.cancelDeleteAnimation();
         }

         this.currentDeletingSpan = var2;
         var2.startDeleteAnimation();
      }

   }

   public boolean onFragmentCreate() {
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsImported);
      this.fetchContacts();
      if (!UserConfig.getInstance(super.currentAccount).contactsReimported) {
         ContactsController.getInstance(super.currentAccount).forceImportContacts();
         UserConfig.getInstance(super.currentAccount).contactsReimported = true;
         UserConfig.getInstance(super.currentAccount).saveConfig(false);
      }

      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsImported);
   }

   public void onResume() {
      super.onResume();
      EditTextBoldCursor var1 = this.editText;
      if (var1 != null) {
         var1.requestFocus();
      }

   }

   @Keep
   public void setContainerHeight(int var1) {
      this.containerHeight = var1;
      InviteContactsActivity.SpansContainer var2 = this.spansContainer;
      if (var2 != null) {
         var2.requestLayout();
      }

   }

   public class InviteAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;
      private ArrayList searchResult = new ArrayList();
      private ArrayList searchResultNames = new ArrayList();
      private Timer searchTimer;
      private boolean searching;

      public InviteAdapter(Context var2) {
         this.context = var2;
      }

      private void updateSearchResults(ArrayList var1, ArrayList var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$InviteContactsActivity$InviteAdapter$ZxgmfjRluVB9wR7WmXECALc6XVE(this, var1, var2));
      }

      public int getItemCount() {
         return this.searching ? this.searchResult.size() : InviteContactsActivity.this.phoneBookContacts.size() + 1;
      }

      public int getItemViewType(int var1) {
         return !this.searching && var1 == 0 ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return true;
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$0$InviteContactsActivity$InviteAdapter(ArrayList var1, ArrayList var2) {
         this.searchResult = var1;
         this.searchResultNames = var2;
         this.notifyDataSetChanged();
      }

      public void notifyDataSetChanged() {
         super.notifyDataSetChanged();
         int var1 = this.getItemCount();
         EmptyTextProgressView var2 = InviteContactsActivity.this.emptyView;
         boolean var3 = false;
         byte var4;
         if (var1 == 1) {
            var4 = 0;
         } else {
            var4 = 4;
         }

         var2.setVisibility(var4);
         GroupCreateDividerItemDecoration var5 = InviteContactsActivity.this.decoration;
         if (var1 == 1) {
            var3 = true;
         }

         var5.setSingle(var3);
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            InviteUserCell var3 = (InviteUserCell)var1.itemView;
            ContactsController.Contact var4;
            CharSequence var5;
            if (this.searching) {
               var4 = (ContactsController.Contact)this.searchResult.get(var2);
               var5 = (CharSequence)this.searchResultNames.get(var2);
            } else {
               var4 = (ContactsController.Contact)InviteContactsActivity.this.phoneBookContacts.get(var2 - 1);
               var5 = null;
            }

            var3.setUser(var4, var5);
            var3.setChecked(InviteContactsActivity.this.selectedContacts.containsKey(var4.key), false);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 1) {
            var3 = new InviteUserCell(this.context, true);
         } else {
            var3 = new InviteTextCell(this.context);
            ((InviteTextCell)var3).setTextAndIcon(LocaleController.getString("ShareTelegram", 2131560753), 2131165818);
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof InviteUserCell) {
            ((InviteUserCell)var2).recycle();
         }

      }

      public void searchDialogs(final String var1) {
         try {
            if (this.searchTimer != null) {
               this.searchTimer.cancel();
            }
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         if (var1 == null) {
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.notifyDataSetChanged();
         } else {
            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
               // $FF: synthetic method
               public void lambda$null$0$InviteContactsActivity$InviteAdapter$1(String var1x) {
                  String var2 = var1x.trim().toLowerCase();
                  if (var2.length() == 0) {
                     InviteAdapter.this.updateSearchResults(new ArrayList(), new ArrayList());
                  } else {
                     String var3;
                     label62: {
                        var3 = LocaleController.getInstance().getTranslitString(var2);
                        if (!var2.equals(var3)) {
                           var1x = var3;
                           if (var3.length() != 0) {
                              break label62;
                           }
                        }

                        var1x = null;
                     }

                     byte var4;
                     if (var1x != null) {
                        var4 = 1;
                     } else {
                        var4 = 0;
                     }

                     String[] var5 = new String[var4 + 1];
                     var5[0] = var2;
                     if (var1x != null) {
                        var5[1] = var1x;
                     }

                     ArrayList var6 = new ArrayList();
                     ArrayList var14 = new ArrayList();

                     for(int var15 = 0; var15 < InviteContactsActivity.this.phoneBookContacts.size(); ++var15) {
                        ContactsController.Contact var7 = (ContactsController.Contact)InviteContactsActivity.this.phoneBookContacts.get(var15);
                        String var8 = ContactsController.formatName(var7.first_name, var7.last_name).toLowerCase();
                        var3 = LocaleController.getInstance().getTranslitString(var8);
                        var1x = var3;
                        if (var8.equals(var3)) {
                           var1x = null;
                        }

                        int var9 = var5.length;
                        int var10 = 0;

                        boolean var13;
                        for(boolean var11 = false; var10 < var9; var11 = var13) {
                           label51: {
                              var3 = var5[var10];
                              if (!var8.startsWith(var3)) {
                                 StringBuilder var12 = new StringBuilder();
                                 var12.append(" ");
                                 var12.append(var3);
                                 if (!var8.contains(var12.toString())) {
                                    var13 = var11;
                                    if (var1x == null) {
                                       break label51;
                                    }

                                    if (!var1x.startsWith(var3)) {
                                       var12 = new StringBuilder();
                                       var12.append(" ");
                                       var12.append(var3);
                                       var13 = var11;
                                       if (!var1x.contains(var12.toString())) {
                                          break label51;
                                       }
                                    }
                                 }
                              }

                              var13 = true;
                           }

                           if (var13) {
                              var14.add(AndroidUtilities.generateSearchName(var7.first_name, var7.last_name, var3));
                              var6.add(var7);
                              break;
                           }

                           ++var10;
                        }
                     }

                     InviteAdapter.this.updateSearchResults(var6, var14);
                  }
               }

               // $FF: synthetic method
               public void lambda$run$1$InviteContactsActivity$InviteAdapter$1(String var1x) {
                  Utilities.searchQueue.postRunnable(new _$$Lambda$InviteContactsActivity$InviteAdapter$1$Acay2VbdoDOg6RSwRkjeUNE5RJs(this, var1x));
               }

               public void run() {
                  try {
                     InviteAdapter.this.searchTimer.cancel();
                     InviteAdapter.this.searchTimer = null;
                  } catch (Exception var2) {
                     FileLog.e((Throwable)var2);
                  }

                  AndroidUtilities.runOnUIThread(new _$$Lambda$InviteContactsActivity$InviteAdapter$1$puDDzs3DCPG3FhnDL1PVs4vd3QI(this, var1));
               }
            }, 200L, 300L);
         }

      }

      public void setSearching(boolean var1) {
         if (this.searching != var1) {
            this.searching = var1;
            this.notifyDataSetChanged();
         }
      }
   }

   private class SpansContainer extends ViewGroup {
      private View addingSpan;
      private boolean animationStarted;
      private ArrayList animators = new ArrayList();
      private AnimatorSet currentAnimation;
      private View removingSpan;

      public SpansContainer(Context var2) {
         super(var2);
      }

      public void addSpan(GroupCreateSpan var1) {
         InviteContactsActivity.this.allSpans.add(var1);
         InviteContactsActivity.this.selectedContacts.put(var1.getKey(), var1);
         InviteContactsActivity.this.editText.setHintVisible(false);
         AnimatorSet var2 = this.currentAnimation;
         if (var2 != null) {
            var2.setupEndValues();
            this.currentAnimation.cancel();
         }

         this.animationStarted = false;
         this.currentAnimation = new AnimatorSet();
         this.currentAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               SpansContainer.this.addingSpan = null;
               SpansContainer.this.currentAnimation = null;
               SpansContainer.this.animationStarted = false;
               InviteContactsActivity.this.editText.setAllowDrawCursor(true);
            }
         });
         this.currentAnimation.setDuration(150L);
         this.addingSpan = var1;
         this.animators.clear();
         this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "scaleX", new float[]{0.01F, 1.0F}));
         this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "scaleY", new float[]{0.01F, 1.0F}));
         this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "alpha", new float[]{0.0F, 1.0F}));
         this.addView(var1);
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         var3 = this.getChildCount();

         for(var2 = 0; var2 < var3; ++var2) {
            View var6 = this.getChildAt(var2);
            var6.layout(0, 0, var6.getMeasuredWidth(), var6.getMeasuredHeight());
         }

      }

      protected void onMeasure(int var1, int var2) {
         int var3 = this.getChildCount();
         int var4 = MeasureSpec.getSize(var1);
         int var5 = var4 - AndroidUtilities.dp(32.0F);
         int var6 = AndroidUtilities.dp(12.0F);
         var1 = AndroidUtilities.dp(12.0F);
         int var7 = 0;
         var2 = 0;

         int var8;
         int var10;
         float var12;
         float var13;
         for(var8 = 0; var7 < var3; var1 = var10) {
            View var9 = this.getChildAt(var7);
            if (!(var9 instanceof GroupCreateSpan)) {
               var10 = var1;
            } else {
               var9.measure(MeasureSpec.makeMeasureSpec(var4, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0F), 1073741824));
               if (var9 != this.removingSpan && var9.getMeasuredWidth() + var2 > var5) {
                  var6 += var9.getMeasuredHeight() + AndroidUtilities.dp(12.0F);
                  var2 = 0;
               }

               if (var9.getMeasuredWidth() + var8 > var5) {
                  var10 = var1 + var9.getMeasuredHeight() + AndroidUtilities.dp(12.0F);
                  var8 = 0;
               } else {
                  var10 = var1;
               }

               var1 = AndroidUtilities.dp(16.0F) + var2;
               if (!this.animationStarted) {
                  View var11 = this.removingSpan;
                  if (var9 == var11) {
                     var9.setTranslationX((float)(AndroidUtilities.dp(16.0F) + var8));
                     var9.setTranslationY((float)var10);
                  } else if (var11 != null) {
                     var12 = var9.getTranslationX();
                     var13 = (float)var1;
                     if (var12 != var13) {
                        this.animators.add(ObjectAnimator.ofFloat(var9, "translationX", new float[]{var13}));
                     }

                     var12 = var9.getTranslationY();
                     var13 = (float)var6;
                     if (var12 != var13) {
                        this.animators.add(ObjectAnimator.ofFloat(var9, "translationY", new float[]{var13}));
                     }
                  } else {
                     var9.setTranslationX((float)var1);
                     var9.setTranslationY((float)var6);
                  }
               }

               var1 = var2;
               if (var9 != this.removingSpan) {
                  var1 = var2 + var9.getMeasuredWidth() + AndroidUtilities.dp(9.0F);
               }

               var8 += var9.getMeasuredWidth() + AndroidUtilities.dp(9.0F);
               var2 = var1;
            }

            ++var7;
         }

         if (AndroidUtilities.isTablet()) {
            var7 = AndroidUtilities.dp(366.0F) / 3;
         } else {
            Point var14 = AndroidUtilities.displaySize;
            var7 = (Math.min(var14.x, var14.y) - AndroidUtilities.dp(164.0F)) / 3;
         }

         var3 = var2;
         var10 = var6;
         if (var5 - var2 < var7) {
            var10 = var6 + AndroidUtilities.dp(44.0F);
            var3 = 0;
         }

         var2 = var1;
         if (var5 - var8 < var7) {
            var2 = var1 + AndroidUtilities.dp(44.0F);
         }

         InviteContactsActivity.this.editText.measure(MeasureSpec.makeMeasureSpec(var5 - var3, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0F), 1073741824));
         if (!this.animationStarted) {
            var6 = AndroidUtilities.dp(44.0F);
            var1 = var3 + AndroidUtilities.dp(16.0F);
            InviteContactsActivity.this.fieldY = var10;
            if (this.currentAnimation != null) {
               var2 = var10 + AndroidUtilities.dp(44.0F);
               if (InviteContactsActivity.this.containerHeight != var2) {
                  this.animators.add(ObjectAnimator.ofInt(InviteContactsActivity.this, "containerHeight", new int[]{var2}));
               }

               var13 = InviteContactsActivity.this.editText.getTranslationX();
               var12 = (float)var1;
               if (var13 != var12) {
                  this.animators.add(ObjectAnimator.ofFloat(InviteContactsActivity.this.editText, "translationX", new float[]{var12}));
               }

               if (InviteContactsActivity.this.editText.getTranslationY() != (float)InviteContactsActivity.this.fieldY) {
                  this.animators.add(ObjectAnimator.ofFloat(InviteContactsActivity.this.editText, "translationY", new float[]{(float)InviteContactsActivity.this.fieldY}));
               }

               InviteContactsActivity.this.editText.setAllowDrawCursor(false);
               this.currentAnimation.playTogether(this.animators);
               this.currentAnimation.start();
               this.animationStarted = true;
            } else {
               InviteContactsActivity.this.containerHeight = var2 + var6;
               InviteContactsActivity.this.editText.setTranslationX((float)var1);
               InviteContactsActivity.this.editText.setTranslationY((float)InviteContactsActivity.this.fieldY);
            }
         } else if (this.currentAnimation != null && !InviteContactsActivity.this.ignoreScrollEvent && this.removingSpan == null) {
            InviteContactsActivity.this.editText.bringPointIntoView(InviteContactsActivity.this.editText.getSelectionStart());
         }

         this.setMeasuredDimension(var4, InviteContactsActivity.this.containerHeight);
      }

      public void removeSpan(final GroupCreateSpan var1) {
         InviteContactsActivity.this.ignoreScrollEvent = true;
         InviteContactsActivity.this.selectedContacts.remove(var1.getKey());
         InviteContactsActivity.this.allSpans.remove(var1);
         var1.setOnClickListener((OnClickListener)null);
         AnimatorSet var2 = this.currentAnimation;
         if (var2 != null) {
            var2.setupEndValues();
            this.currentAnimation.cancel();
         }

         this.animationStarted = false;
         this.currentAnimation = new AnimatorSet();
         this.currentAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               SpansContainer.this.removeView(var1);
               SpansContainer.this.removingSpan = null;
               SpansContainer.this.currentAnimation = null;
               SpansContainer.this.animationStarted = false;
               InviteContactsActivity.this.editText.setAllowDrawCursor(true);
               if (InviteContactsActivity.this.allSpans.isEmpty()) {
                  InviteContactsActivity.this.editText.setHintVisible(true);
               }

            }
         });
         this.currentAnimation.setDuration(150L);
         this.removingSpan = var1;
         this.animators.clear();
         this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "scaleX", new float[]{1.0F, 0.01F}));
         this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "scaleY", new float[]{1.0F, 0.01F}));
         this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "alpha", new float[]{1.0F, 0.0F}));
         this.requestLayout();
      }
   }
}
