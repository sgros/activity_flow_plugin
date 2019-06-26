package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Adapters.SearchAdapterHelper$SearchAdapterHelperDelegate$_CC;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.TypefaceSpan;

public class GroupCreateActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, OnClickListener {
   private static final int done_button = 1;
   private GroupCreateActivity.GroupCreateAdapter adapter;
   private boolean addToGroup;
   private ArrayList allSpans;
   private int channelId;
   private int chatId;
   private int chatType;
   private int containerHeight;
   private GroupCreateSpan currentDeletingSpan;
   private AnimatorSet currentDoneButtonAnimation;
   private GroupCreateActivity.GroupCreateActivityDelegate delegate;
   private GroupCreateActivity.ContactsAddActivityDelegate delegate2;
   private boolean doneButtonVisible;
   private EditTextBoldCursor editText;
   private EmptyTextProgressView emptyView;
   private int fieldY;
   private ImageView floatingButton;
   private boolean ignoreScrollEvent;
   private SparseArray ignoreUsers;
   private boolean isAlwaysShare;
   private boolean isGroup;
   private boolean isNeverShare;
   private GroupCreateDividerItemDecoration itemDecoration;
   private RecyclerListView listView;
   private int maxCount;
   private ScrollView scrollView;
   private boolean searchWas;
   private boolean searching;
   private SparseArray selectedContacts;
   private GroupCreateActivity.SpansContainer spansContainer;

   public GroupCreateActivity() {
      this.maxCount = MessagesController.getInstance(super.currentAccount).maxMegagroupCount;
      this.chatType = 0;
      this.selectedContacts = new SparseArray();
      this.allSpans = new ArrayList();
   }

   public GroupCreateActivity(Bundle var1) {
      super(var1);
      this.maxCount = MessagesController.getInstance(super.currentAccount).maxMegagroupCount;
      this.chatType = 0;
      this.selectedContacts = new SparseArray();
      this.allSpans = new ArrayList();
      this.chatType = var1.getInt("chatType", 0);
      this.isAlwaysShare = var1.getBoolean("isAlwaysShare", false);
      this.isNeverShare = var1.getBoolean("isNeverShare", false);
      this.addToGroup = var1.getBoolean("addToGroup", false);
      this.isGroup = var1.getBoolean("isGroup", false);
      this.chatId = var1.getInt("chatId");
      this.channelId = var1.getInt("channelId");
      if (!this.isAlwaysShare && !this.isNeverShare && !this.addToGroup) {
         int var2;
         if (this.chatType == 0) {
            var2 = MessagesController.getInstance(super.currentAccount).maxMegagroupCount;
         } else {
            var2 = MessagesController.getInstance(super.currentAccount).maxBroadcastCount;
         }

         this.maxCount = var2;
      } else {
         this.maxCount = 0;
      }

   }

   // $FF: synthetic method
   static ActionBarLayout access$1500(GroupCreateActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$2600(GroupCreateActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(GroupCreateActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(GroupCreateActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3600(GroupCreateActivity var0) {
      return var0.currentAccount;
   }

   private void checkVisibleRows() {
      int var1 = this.listView.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.listView.getChildAt(var2);
         if (var3 instanceof GroupCreateUserCell) {
            GroupCreateUserCell var7 = (GroupCreateUserCell)var3;
            TLObject var4 = var7.getObject();
            int var5;
            if (var4 instanceof TLRPC.User) {
               var5 = ((TLRPC.User)var4).id;
            } else if (var4 instanceof TLRPC.Chat) {
               var5 = -((TLRPC.Chat)var4).id;
            } else {
               var5 = 0;
            }

            if (var5 != 0) {
               SparseArray var8 = this.ignoreUsers;
               if (var8 != null && var8.indexOfKey(var5) >= 0) {
                  var7.setChecked(true, false);
                  var7.setCheckBoxEnabled(false);
               } else {
                  boolean var6;
                  if (this.selectedContacts.indexOfKey(var5) >= 0) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  var7.setChecked(var6, true);
                  var7.setCheckBoxEnabled(true);
               }
            }
         }
      }

   }

   private void closeSearch() {
      this.searching = false;
      this.searchWas = false;
      this.itemDecoration.setSearching(false);
      this.adapter.setSearching(false);
      this.adapter.searchDialogs((String)null);
      this.listView.setFastScrollVisible(true);
      this.listView.setVerticalScrollBarEnabled(false);
      this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
   }

   // $FF: synthetic method
   static void lambda$onDonePressed$5(CheckBoxCell[] var0, View var1) {
      var0[0].setChecked(var0[0].isChecked() ^ true, true);
   }

   private void onAddToGroupDone(int var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.selectedContacts.size(); ++var3) {
         var2.add(this.getMessagesController().getUser(this.selectedContacts.keyAt(var3)));
      }

      GroupCreateActivity.ContactsAddActivityDelegate var4 = this.delegate2;
      if (var4 != null) {
         var4.didSelectUsers(var2, var1);
      }

      this.finishFragment();
   }

   private boolean onDonePressed(boolean var1) {
      int var2 = this.selectedContacts.size();
      int var3 = 0;
      if (var2 == 0) {
         return false;
      } else {
         if (var1 && this.addToGroup) {
            if (this.getParentActivity() == null) {
               return false;
            }

            AlertDialog.Builder var9 = new AlertDialog.Builder(this.getParentActivity());
            if (this.selectedContacts.size() == 1) {
               var9.setTitle(LocaleController.getString("AddOneMemberAlertTitle", 2131558579));
            } else {
               var9.setTitle(LocaleController.formatString("AddMembersAlertTitle", 2131558576, LocaleController.formatPluralString("Members", this.selectedContacts.size())));
            }

            StringBuilder var12 = new StringBuilder();

            for(var3 = 0; var3 < this.selectedContacts.size(); ++var3) {
               var2 = this.selectedContacts.keyAt(var3);
               TLRPC.User var6 = this.getMessagesController().getUser(var2);
               if (var6 != null) {
                  if (var12.length() > 0) {
                     var12.append(", ");
                  }

                  var12.append("**");
                  var12.append(ContactsController.formatName(var6.first_name, var6.last_name));
                  var12.append("**");
               }
            }

            MessagesController var13 = this.getMessagesController();
            var3 = this.chatId;
            if (var3 == 0) {
               var3 = this.channelId;
            }

            TLRPC.Chat var15 = var13.getChat(var3);
            if (this.selectedContacts.size() > 5) {
               SpannableStringBuilder var14 = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", 2131558575, LocaleController.formatPluralString("Members", this.selectedContacts.size()), var15.title)));
               String var7 = String.format("%d", this.selectedContacts.size());
               var3 = TextUtils.indexOf(var14, var7);
               if (var3 >= 0) {
                  var14.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), var3, var7.length() + var3, 33);
               }

               var9.setMessage(var14);
            } else {
               var9.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", 2131558575, var12, var15.title)));
            }

            CheckBoxCell[] var16 = new CheckBoxCell[1];
            if (!ChatObject.isChannel(var15)) {
               LinearLayout var17 = new LinearLayout(this.getParentActivity());
               var17.setOrientation(1);
               var16[0] = new CheckBoxCell(this.getParentActivity(), 1);
               var16[0].setBackgroundDrawable(Theme.getSelectorDrawable(false));
               var16[0].setMultiline(true);
               if (this.selectedContacts.size() == 1) {
                  TLRPC.User var18 = this.getMessagesController().getUser(this.selectedContacts.keyAt(0));
                  var16[0].setText(AndroidUtilities.replaceTags(LocaleController.formatString("AddOneMemberForwardMessages", 2131558580, UserObject.getFirstName(var18))), "", true, false);
               } else {
                  var16[0].setText(LocaleController.getString("AddMembersForwardMessages", 2131558577), "", true, false);
               }

               CheckBoxCell var19 = var16[0];
               if (LocaleController.isRTL) {
                  var3 = AndroidUtilities.dp(16.0F);
               } else {
                  var3 = AndroidUtilities.dp(8.0F);
               }

               if (LocaleController.isRTL) {
                  var2 = AndroidUtilities.dp(8.0F);
               } else {
                  var2 = AndroidUtilities.dp(16.0F);
               }

               var19.setPadding(var3, 0, var2, 0);
               var17.addView(var16[0], LayoutHelper.createLinear(-1, -2));
               var16[0].setOnClickListener(new _$$Lambda$GroupCreateActivity$3XN987OICBmldOkyTJTx1cRwAQA(var16));
               var9.setCustomViewOffset(12);
               var9.setView(var17);
            }

            var9.setPositiveButton(LocaleController.getString("Add", 2131558555), new _$$Lambda$GroupCreateActivity$R31kPKuAiH7RQVcTaHcTSTgxjkI(this, var16));
            var9.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (android.content.DialogInterface.OnClickListener)null);
            this.showDialog(var9.create());
         } else {
            ArrayList var4;
            if (this.chatType == 2) {
               var4 = new ArrayList();

               for(var3 = 0; var3 < this.selectedContacts.size(); ++var3) {
                  TLRPC.InputUser var11 = MessagesController.getInstance(super.currentAccount).getInputUser(MessagesController.getInstance(super.currentAccount).getUser(this.selectedContacts.keyAt(var3)));
                  if (var11 != null) {
                     var4.add(var11);
                  }
               }

               MessagesController.getInstance(super.currentAccount).addUsersToChannel(this.chatId, var4, (BaseFragment)null);
               NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
               Bundle var8 = new Bundle();
               var8.putInt("chat_id", this.chatId);
               this.presentFragment(new ChatActivity(var8), true);
            } else {
               if (!this.doneButtonVisible || this.selectedContacts.size() == 0) {
                  return false;
               }

               if (this.addToGroup) {
                  this.onAddToGroupDone(0);
               } else {
                  for(var4 = new ArrayList(); var3 < this.selectedContacts.size(); ++var3) {
                     var4.add(this.selectedContacts.keyAt(var3));
                  }

                  if (!this.isAlwaysShare && !this.isNeverShare) {
                     Bundle var10 = new Bundle();
                     var10.putIntegerArrayList("result", var4);
                     var10.putInt("chatType", this.chatType);
                     this.presentFragment(new GroupCreateFinalActivity(var10));
                  } else {
                     GroupCreateActivity.GroupCreateActivityDelegate var5 = this.delegate;
                     if (var5 != null) {
                        var5.didSelectUsers(var4);
                     }

                     this.finishFragment();
                  }
               }
            }
         }

         return true;
      }
   }

   private void updateHint() {
      if (!this.isAlwaysShare && !this.isNeverShare && !this.addToGroup) {
         if (this.chatType == 2) {
            super.actionBar.setSubtitle(LocaleController.formatPluralString("Members", this.selectedContacts.size()));
         } else if (this.selectedContacts.size() == 0) {
            super.actionBar.setSubtitle(LocaleController.formatString("MembersCountZero", 2131559838, LocaleController.formatPluralString("Members", this.maxCount)));
         } else {
            super.actionBar.setSubtitle(LocaleController.formatString("MembersCount", 2131559837, this.selectedContacts.size(), this.maxCount));
         }
      }

      if (this.chatType != 2) {
         AnimatorSet var1;
         if (this.doneButtonVisible && this.allSpans.isEmpty()) {
            var1 = this.currentDoneButtonAnimation;
            if (var1 != null) {
               var1.cancel();
            }

            this.currentDoneButtonAnimation = new AnimatorSet();
            this.currentDoneButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.floatingButton, "scaleX", new float[]{0.0F}), ObjectAnimator.ofFloat(this.floatingButton, "scaleY", new float[]{0.0F}), ObjectAnimator.ofFloat(this.floatingButton, "alpha", new float[]{0.0F})});
            this.currentDoneButtonAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  GroupCreateActivity.this.floatingButton.setVisibility(4);
               }
            });
            this.currentDoneButtonAnimation.setDuration(180L);
            this.currentDoneButtonAnimation.start();
            this.doneButtonVisible = false;
         } else if (!this.doneButtonVisible && !this.allSpans.isEmpty()) {
            var1 = this.currentDoneButtonAnimation;
            if (var1 != null) {
               var1.cancel();
            }

            this.currentDoneButtonAnimation = new AnimatorSet();
            this.floatingButton.setVisibility(0);
            this.currentDoneButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.floatingButton, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.floatingButton, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.floatingButton, "alpha", new float[]{1.0F})});
            this.currentDoneButtonAnimation.setDuration(180L);
            this.currentDoneButtonAnimation.start();
            this.doneButtonVisible = true;
         }
      }

   }

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      this.allSpans.clear();
      this.selectedContacts.clear();
      this.currentDeletingSpan = null;
      boolean var2;
      if (this.chatType == 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.doneButtonVisible = var2;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      int var3 = this.chatType;
      if (var3 == 2) {
         super.actionBar.setTitle(LocaleController.getString("ChannelAddMembers", 2131558920));
      } else if (this.addToGroup) {
         super.actionBar.setTitle(LocaleController.getString("SelectContacts", 2131560681));
      } else if (this.isAlwaysShare) {
         if (this.isGroup) {
            super.actionBar.setTitle(LocaleController.getString("AlwaysAllow", 2131558611));
         } else {
            super.actionBar.setTitle(LocaleController.getString("AlwaysShareWithTitle", 2131558613));
         }
      } else if (this.isNeverShare) {
         if (this.isGroup) {
            super.actionBar.setTitle(LocaleController.getString("NeverAllow", 2131559894));
         } else {
            super.actionBar.setTitle(LocaleController.getString("NeverShareWithTitle", 2131559896));
         }
      } else {
         ActionBar var4 = super.actionBar;
         String var5;
         if (var3 == 0) {
            var3 = 2131559900;
            var5 = "NewGroup";
         } else {
            var3 = 2131559897;
            var5 = "NewBroadcastList";
         }

         var4.setTitle(LocaleController.getString(var5, var3));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               GroupCreateActivity.this.finishFragment();
            } else if (var1 == 1) {
               GroupCreateActivity.this.onDonePressed(true);
            }

         }
      });
      super.fragmentView = new ViewGroup(var1) {
         protected boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 == GroupCreateActivity.this.listView || var2 == GroupCreateActivity.this.emptyView) {
               GroupCreateActivity.access$1500(GroupCreateActivity.this).drawHeaderShadow(var1, GroupCreateActivity.this.scrollView.getMeasuredHeight());
            }

            return var5;
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            GroupCreateActivity.this.scrollView.layout(0, 0, GroupCreateActivity.this.scrollView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight());
            GroupCreateActivity.this.listView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.listView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.listView.getMeasuredHeight());
            GroupCreateActivity.this.emptyView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.emptyView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.emptyView.getMeasuredHeight());
            if (GroupCreateActivity.this.floatingButton != null) {
               if (LocaleController.isRTL) {
                  var2 = AndroidUtilities.dp(14.0F);
               } else {
                  var2 = var4 - var2 - AndroidUtilities.dp(14.0F) - GroupCreateActivity.this.floatingButton.getMeasuredWidth();
               }

               var3 = var5 - var3 - AndroidUtilities.dp(14.0F) - GroupCreateActivity.this.floatingButton.getMeasuredHeight();
               GroupCreateActivity.this.floatingButton.layout(var2, var3, GroupCreateActivity.this.floatingButton.getMeasuredWidth() + var2, GroupCreateActivity.this.floatingButton.getMeasuredHeight() + var3);
            }

         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var1);
            var2 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var2);
            boolean var4 = AndroidUtilities.isTablet();
            float var5 = 56.0F;
            if (!var4 && var2 <= var3) {
               var1 = AndroidUtilities.dp(56.0F);
            } else {
               var1 = AndroidUtilities.dp(144.0F);
            }

            GroupCreateActivity.this.scrollView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
            GroupCreateActivity.this.listView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
            GroupCreateActivity.this.emptyView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var2 - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
            if (GroupCreateActivity.this.floatingButton != null) {
               if (VERSION.SDK_INT < 21) {
                  var5 = 60.0F;
               }

               var1 = AndroidUtilities.dp(var5);
               GroupCreateActivity.this.floatingButton.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var1, 1073741824));
            }

         }
      };
      ViewGroup var6 = (ViewGroup)super.fragmentView;
      this.scrollView = new ScrollView(var1) {
         public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
            if (GroupCreateActivity.this.ignoreScrollEvent) {
               GroupCreateActivity.this.ignoreScrollEvent = false;
               return false;
            } else {
               var2.offset(var1.getLeft() - var1.getScrollX(), var1.getTop() - var1.getScrollY());
               var2.top += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(20.0F);
               var2.bottom += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(50.0F);
               return super.requestChildRectangleOnScreen(var1, var2, var3);
            }
         }
      };
      this.scrollView.setVerticalScrollBarEnabled(false);
      AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("windowBackgroundWhite"));
      var6.addView(this.scrollView);
      this.spansContainer = new GroupCreateActivity.SpansContainer(var1);
      this.scrollView.addView(this.spansContainer, LayoutHelper.createFrame(-1, -2.0F));
      this.spansContainer.setOnClickListener(new _$$Lambda$GroupCreateActivity$eUWXrWcPgL8v_iB3AHPMNXDrIzs(this));
      this.editText = new EditTextBoldCursor(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (GroupCreateActivity.this.currentDeletingSpan != null) {
               GroupCreateActivity.this.currentDeletingSpan.cancelDeleteAnimation();
               GroupCreateActivity.this.currentDeletingSpan = null;
            }

            if (var1.getAction() == 0 && !AndroidUtilities.showKeyboard(this)) {
               this.clearFocus();
               this.requestFocus();
            }

            return super.onTouchEvent(var1);
         }
      };
      this.editText.setTextSize(1, 16.0F);
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
      EditTextBoldCursor var15 = this.editText;
      byte var11;
      if (LocaleController.isRTL) {
         var11 = 5;
      } else {
         var11 = 3;
      }

      var15.setGravity(var11 | 16);
      this.spansContainer.addView(this.editText);
      if (this.chatType == 2) {
         this.editText.setHintText(LocaleController.getString("AddMutual", 2131558578));
      } else if (this.addToGroup) {
         this.editText.setHintText(LocaleController.getString("SearchForPeople", 2131560644));
      } else if (!this.isAlwaysShare && !this.isNeverShare) {
         this.editText.setHintText(LocaleController.getString("SendMessageTo", 2131560703));
      } else {
         this.editText.setHintText(LocaleController.getString("SearchForPeopleAndGroups", 2131560645));
      }

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
      this.editText.setOnEditorActionListener(new _$$Lambda$GroupCreateActivity$gD5dQDpB0G04wuqUX611txNyFto(this));
      this.editText.setOnKeyListener(new OnKeyListener() {
         private boolean wasEmpty;

         public boolean onKey(View var1, int var2, KeyEvent var3) {
            if (var2 == 67) {
               var2 = var3.getAction();
               boolean var4 = true;
               if (var2 == 0) {
                  if (GroupCreateActivity.this.editText.length() != 0) {
                     var4 = false;
                  }

                  this.wasEmpty = var4;
               } else if (var3.getAction() == 1 && this.wasEmpty && !GroupCreateActivity.this.allSpans.isEmpty()) {
                  GroupCreateActivity.this.spansContainer.removeSpan((GroupCreateSpan)GroupCreateActivity.this.allSpans.get(GroupCreateActivity.this.allSpans.size() - 1));
                  GroupCreateActivity.this.updateHint();
                  GroupCreateActivity.this.checkVisibleRows();
                  return true;
               }
            }

            return false;
         }
      });
      this.editText.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
            if (GroupCreateActivity.this.editText.length() != 0) {
               if (!GroupCreateActivity.this.adapter.searching) {
                  GroupCreateActivity.this.searching = true;
                  GroupCreateActivity.this.searchWas = true;
                  GroupCreateActivity.this.adapter.setSearching(true);
                  GroupCreateActivity.this.itemDecoration.setSearching(true);
                  GroupCreateActivity.this.listView.setFastScrollVisible(false);
                  GroupCreateActivity.this.listView.setVerticalScrollBarEnabled(true);
                  GroupCreateActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                  GroupCreateActivity.this.emptyView.showProgress();
               }

               GroupCreateActivity.this.adapter.searchDialogs(GroupCreateActivity.this.editText.getText().toString());
            } else {
               GroupCreateActivity.this.closeSearch();
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

      this.emptyView.setShowAtCenter(true);
      this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
      var6.addView(this.emptyView);
      LinearLayoutManager var7 = new LinearLayoutManager(var1, 1, false);
      this.listView = new RecyclerListView(var1);
      this.listView.setFastScrollEnabled();
      this.listView.setEmptyView(this.emptyView);
      RecyclerListView var16 = this.listView;
      GroupCreateActivity.GroupCreateAdapter var12 = new GroupCreateActivity.GroupCreateAdapter(var1);
      this.adapter = var12;
      var16.setAdapter(var12);
      this.listView.setLayoutManager(var7);
      this.listView.setVerticalScrollBarEnabled(false);
      var16 = this.listView;
      if (LocaleController.isRTL) {
         var11 = 1;
      } else {
         var11 = 2;
      }

      var16.setVerticalScrollbarPosition(var11);
      var16 = this.listView;
      GroupCreateDividerItemDecoration var13 = new GroupCreateDividerItemDecoration();
      this.itemDecoration = var13;
      var16.addItemDecoration(var13);
      var6.addView(this.listView);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$GroupCreateActivity$6yZ3Pg9mYqhNWQKcnf5So14xft8(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1) {
               AndroidUtilities.hideKeyboard(GroupCreateActivity.this.editText);
            }

         }
      });
      this.floatingButton = new ImageView(var1);
      this.floatingButton.setScaleType(ScaleType.CENTER);
      Drawable var14 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
      Object var17 = var14;
      if (VERSION.SDK_INT < 21) {
         Drawable var8 = var1.getResources().getDrawable(2131165387).mutate();
         var8.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var17 = new CombinedDrawable(var8, var14, 0, 0);
         ((CombinedDrawable)var17).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      this.floatingButton.setBackgroundDrawable((Drawable)var17);
      this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), Mode.MULTIPLY));
      if (!this.isNeverShare && !this.isAlwaysShare && !this.addToGroup) {
         BackDrawable var9 = new BackDrawable(false);
         var9.setArrowRotation(180);
         this.floatingButton.setImageDrawable(var9);
      } else {
         this.floatingButton.setImageResource(2131165384);
      }

      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var10 = new StateListAnimator();
         ObjectAnimator var18 = ObjectAnimator.ofFloat(this.floatingButton, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var10.addState(new int[]{16842919}, var18);
         var18 = ObjectAnimator.ofFloat(this.floatingButton, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var10.addState(new int[0], var18);
         this.floatingButton.setStateListAnimator(var10);
         this.floatingButton.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      var6.addView(this.floatingButton);
      this.floatingButton.setOnClickListener(new _$$Lambda$GroupCreateActivity$Lid5KkPKTwmHxkGJugR_qNCJxvA(this));
      if (this.chatType != 2) {
         this.floatingButton.setVisibility(4);
         this.floatingButton.setScaleX(0.0F);
         this.floatingButton.setScaleY(0.0F);
         this.floatingButton.setAlpha(0.0F);
      }

      this.floatingButton.setContentDescription(LocaleController.getString("Next", 2131559911));
      this.updateHint();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.contactsDidLoad) {
         EmptyTextProgressView var7 = this.emptyView;
         if (var7 != null) {
            var7.showTextView();
         }

         GroupCreateActivity.GroupCreateAdapter var8 = this.adapter;
         if (var8 != null) {
            var8.notifyDataSetChanged();
         }
      } else if (var1 == NotificationCenter.updateInterfaces) {
         if (this.listView != null) {
            byte var6 = 0;
            int var4 = (Integer)var3[0];
            int var5 = this.listView.getChildCount();
            var1 = var6;
            if ((var4 & 2) == 0) {
               var1 = var6;
               if ((var4 & 1) == 0) {
                  if ((var4 & 4) == 0) {
                     return;
                  }

                  var1 = var6;
               }
            }

            for(; var1 < var5; ++var1) {
               View var9 = this.listView.getChildAt(var1);
               if (var9 instanceof GroupCreateUserCell) {
                  ((GroupCreateUserCell)var9).update(var4);
               }
            }
         }
      } else if (var1 == NotificationCenter.chatDidCreated) {
         this.removeSelfFromStack();
      }

   }

   public int getContainerHeight() {
      return this.containerHeight;
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE var1 = new _$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE(this);
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
      ThemeDescription var18 = new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var19 = new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_hintText");
      ThemeDescription var20 = new ThemeDescription(this.editText, ThemeDescription.FLAG_CURSORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_cursor");
      ThemeDescription var32 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GroupCreateSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection");
      ThemeDescription var31 = new ThemeDescription(this.listView, 0, new Class[]{GroupCreateSectionCell.class}, new String[]{"drawable"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_sectionShadow");
      ThemeDescription var21 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateSectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_sectionText");
      ThemeDescription var22 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_sectionText");
      ThemeDescription var23 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkbox");
      ThemeDescription var24 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxDisabled");
      ThemeDescription var25 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateUserCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxCheck");
      ThemeDescription var26 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText");
      ThemeDescription var27 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{GroupCreateUserCell.class}, new String[]{"statusTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText");
      RecyclerListView var28 = this.listView;
      Drawable var29 = Theme.avatar_broadcastDrawable;
      Drawable var30 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var15, var16, var17, var18, var19, var20, var32, var31, var21, var22, var23, var24, var25, var26, var27, new ThemeDescription(var28, 0, new Class[]{GroupCreateUserCell.class}, (Paint)null, new Drawable[]{var29, var30}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundGroupCreateSpanBlue"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_spanBackground"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_spanText"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "groupcreate_spanDelete"), new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundBlue")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$GroupCreateActivity(View var1) {
      this.editText.clearFocus();
      this.editText.requestFocus();
      AndroidUtilities.showKeyboard(this.editText);
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$GroupCreateActivity(TextView var1, int var2, KeyEvent var3) {
      boolean var4 = true;
      if (var2 != 6 || !this.onDonePressed(true)) {
         var4 = false;
      }

      return var4;
   }

   // $FF: synthetic method
   public void lambda$createView$3$GroupCreateActivity(View var1, int var2) {
      if (var2 == 0 && this.adapter.inviteViaLink != 0 && !this.adapter.searching) {
         var2 = this.chatId;
         if (var2 == 0) {
            var2 = this.channelId;
         }

         this.presentFragment(new GroupInviteActivity(var2));
      } else if (var1 instanceof GroupCreateUserCell) {
         GroupCreateUserCell var8 = (GroupCreateUserCell)var1;
         TLObject var3 = var8.getObject();
         boolean var4 = var3 instanceof TLRPC.User;
         if (var4) {
            var2 = ((TLRPC.User)var3).id;
         } else {
            if (!(var3 instanceof TLRPC.Chat)) {
               return;
            }

            var2 = -((TLRPC.Chat)var3).id;
         }

         SparseArray var5 = this.ignoreUsers;
         if (var5 != null && var5.indexOfKey(var2) >= 0) {
            return;
         }

         boolean var6;
         if (this.selectedContacts.indexOfKey(var2) >= 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         GroupCreateSpan var12;
         if (var6) {
            var12 = (GroupCreateSpan)this.selectedContacts.get(var2);
            this.spansContainer.removeSpan(var12);
         } else {
            if (this.maxCount != 0 && this.selectedContacts.size() == this.maxCount) {
               return;
            }

            if (this.chatType == 0 && this.selectedContacts.size() == MessagesController.getInstance(super.currentAccount).maxGroupCount) {
               AlertDialog.Builder var10 = new AlertDialog.Builder(this.getParentActivity());
               var10.setTitle(LocaleController.getString("AppName", 2131558635));
               var10.setMessage(LocaleController.getString("SoftUserLimitAlert", 2131560794));
               var10.setPositiveButton(LocaleController.getString("OK", 2131560097), (android.content.DialogInterface.OnClickListener)null);
               this.showDialog(var10.create());
               return;
            }

            if (var4) {
               TLRPC.User var13 = (TLRPC.User)var3;
               if (this.addToGroup && var13.bot) {
                  if (var13.bot_nochats) {
                     try {
                        Toast.makeText(this.getParentActivity(), LocaleController.getString("BotCantJoinGroups", 2131558849), 0).show();
                     } catch (Exception var7) {
                        FileLog.e((Throwable)var7);
                     }

                     return;
                  }

                  if (this.channelId != 0) {
                     TLRPC.Chat var9 = MessagesController.getInstance(super.currentAccount).getChat(this.channelId);
                     AlertDialog.Builder var11 = new AlertDialog.Builder(this.getParentActivity());
                     if (ChatObject.canAddAdmins(var9)) {
                        var11.setTitle(LocaleController.getString("AppName", 2131558635));
                        var11.setMessage(LocaleController.getString("AddBotAsAdmin", 2131558565));
                        var11.setPositiveButton(LocaleController.getString("MakeAdmin", 2131559795), new _$$Lambda$GroupCreateActivity$bXnsoXnrEPbjWVw2M7_hHqrkQF8(this, var13));
                        var11.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (android.content.DialogInterface.OnClickListener)null);
                     } else {
                        var11.setMessage(LocaleController.getString("CantAddBotAsAdmin", 2131558902));
                        var11.setPositiveButton(LocaleController.getString("OK", 2131560097), (android.content.DialogInterface.OnClickListener)null);
                     }

                     this.showDialog(var11.create());
                     return;
                  }
               }

               MessagesController.getInstance(super.currentAccount).putUser(var13, this.searching ^ true);
            } else if (var3 instanceof TLRPC.Chat) {
               TLRPC.Chat var14 = (TLRPC.Chat)var3;
               MessagesController.getInstance(super.currentAccount).putChat(var14, this.searching ^ true);
            }

            var12 = new GroupCreateSpan(this.editText.getContext(), var3);
            this.spansContainer.addSpan(var12);
            var12.setOnClickListener(this);
         }

         this.updateHint();
         if (!this.searching && !this.searchWas) {
            var8.setChecked(var6 ^ true, true);
         } else {
            AndroidUtilities.showKeyboard(this.editText);
         }

         if (this.editText.length() > 0) {
            this.editText.setText((CharSequence)null);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$4$GroupCreateActivity(View var1) {
      this.onDonePressed(true);
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$7$GroupCreateActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof GroupCreateUserCell) {
               ((GroupCreateUserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$GroupCreateActivity(TLRPC.User var1, DialogInterface var2, int var3) {
      this.delegate2.needAddBot(var1);
      if (this.editText.length() > 0) {
         this.editText.setText((CharSequence)null);
      }

   }

   // $FF: synthetic method
   public void lambda$onDonePressed$6$GroupCreateActivity(CheckBoxCell[] var1, DialogInterface var2, int var3) {
      byte var4 = 0;
      byte var5 = var4;
      if (var1[0] != null) {
         var5 = var4;
         if (var1[0].isChecked()) {
            var5 = 100;
         }
      }

      this.onAddToGroupDone(var5);
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
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
   }

   public void onResume() {
      super.onResume();
      EditTextBoldCursor var1 = this.editText;
      if (var1 != null) {
         var1.requestFocus();
      }

      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
   }

   @Keep
   public void setContainerHeight(int var1) {
      this.containerHeight = var1;
      GroupCreateActivity.SpansContainer var2 = this.spansContainer;
      if (var2 != null) {
         var2.requestLayout();
      }

   }

   public void setDelegate(GroupCreateActivity.ContactsAddActivityDelegate var1) {
      this.delegate2 = var1;
   }

   public void setDelegate(GroupCreateActivity.GroupCreateActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setIgnoreUsers(SparseArray var1) {
      this.ignoreUsers = var1;
   }

   public interface ContactsAddActivityDelegate {
      void didSelectUsers(ArrayList var1, int var2);

      void needAddBot(TLRPC.User var1);
   }

   public interface GroupCreateActivityDelegate {
      void didSelectUsers(ArrayList var1);
   }

   public class GroupCreateAdapter extends RecyclerListView.FastScrollAdapter {
      private ArrayList contacts = new ArrayList();
      private Context context;
      private int inviteViaLink;
      private SearchAdapterHelper searchAdapterHelper;
      private ArrayList searchResult = new ArrayList();
      private ArrayList searchResultNames = new ArrayList();
      private Timer searchTimer;
      private boolean searching;
      private int usersStartRow;

      public GroupCreateAdapter(Context var2) {
         this.context = var2;
         ArrayList var3 = ContactsController.getInstance(GroupCreateActivity.access$2600(GroupCreateActivity.this)).contacts;

         int var4;
         for(var4 = 0; var4 < var3.size(); ++var4) {
            TLRPC.User var7 = MessagesController.getInstance(GroupCreateActivity.access$2700(GroupCreateActivity.this)).getUser(((TLRPC.TL_contact)var3.get(var4)).user_id);
            if (var7 != null && !var7.self && !var7.deleted) {
               this.contacts.add(var7);
            }
         }

         if (GroupCreateActivity.this.isNeverShare || GroupCreateActivity.this.isAlwaysShare) {
            var3 = GroupCreateActivity.this.getMessagesController().getAllDialogs();
            int var5 = var3.size();

            for(var4 = 0; var4 < var5; ++var4) {
               int var6 = (int)((TLRPC.Dialog)var3.get(var4)).id;
               if (var6 < 0) {
                  TLRPC.Chat var8 = GroupCreateActivity.this.getMessagesController().getChat(-var6);
                  if (var8 != null && var8.migrated_to == null && (!ChatObject.isChannel(var8) || var8.megagroup)) {
                     this.contacts.add(var8);
                  }
               }
            }

            Collections.sort(this.contacts, new Comparator() {
               private String getName(TLObject var1) {
                  if (var1 instanceof TLRPC.User) {
                     TLRPC.User var2 = (TLRPC.User)var1;
                     return ContactsController.formatName(var2.first_name, var2.last_name);
                  } else {
                     return ((TLRPC.Chat)var1).title;
                  }
               }

               public int compare(TLObject var1, TLObject var2) {
                  return this.getName(var1).compareTo(this.getName(var2));
               }
            });
         }

         this.searchAdapterHelper = new SearchAdapterHelper(false);
         this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() {
            // $FF: synthetic method
            public SparseArray getExcludeUsers() {
               return SearchAdapterHelper$SearchAdapterHelperDelegate$_CC.$default$getExcludeUsers(this);
            }

            public void onDataSetChanged() {
               GroupCreateAdapter.this.notifyDataSetChanged();
            }

            public void onSetHashtags(ArrayList var1, HashMap var2) {
            }
         });
      }

      private void updateSearchResults(ArrayList var1, ArrayList var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$GroupCreateActivity$GroupCreateAdapter$C0i_zDLg66DiKGKME02xepSowZQ(this, var1, var2));
      }

      public int getItemCount() {
         int var1;
         int var2;
         if (this.searching) {
            var1 = this.searchResult.size();
            var2 = this.searchAdapterHelper.getLocalServerSearch().size();
            int var6 = this.searchAdapterHelper.getGlobalSearch().size();
            var1 += var2;
            var2 = var1;
            if (var6 != 0) {
               var2 = var1 + var6 + 1;
            }

            return var2;
         } else {
            var1 = this.contacts.size();
            var2 = var1;
            if (GroupCreateActivity.this.addToGroup) {
               if (GroupCreateActivity.this.chatId != 0) {
                  this.inviteViaLink = ChatObject.canUserDoAdminAction(MessagesController.getInstance(GroupCreateActivity.access$3400(GroupCreateActivity.this)).getChat(GroupCreateActivity.this.chatId), 3);
               } else {
                  var2 = GroupCreateActivity.this.channelId;
                  byte var3 = 0;
                  if (var2 != 0) {
                     TLRPC.Chat var4 = MessagesController.getInstance(GroupCreateActivity.access$3600(GroupCreateActivity.this)).getChat(GroupCreateActivity.this.channelId);
                     byte var5 = var3;
                     if (ChatObject.canUserDoAdminAction(var4, 3)) {
                        var5 = var3;
                        if (TextUtils.isEmpty(var4.username)) {
                           var5 = 2;
                        }
                     }

                     this.inviteViaLink = var5;
                  } else {
                     this.inviteViaLink = 0;
                  }
               }

               var2 = var1;
               if (this.inviteViaLink != 0) {
                  this.usersStartRow = 1;
                  var2 = var1 + 1;
               }
            }

            return var2;
         }
      }

      public int getItemViewType(int var1) {
         if (this.searching) {
            return var1 == this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size() ? 0 : 1;
         } else {
            return this.inviteViaLink != 0 && var1 == 0 ? 2 : 1;
         }
      }

      public String getLetter(int var1) {
         if (!this.searching && var1 >= this.usersStartRow) {
            int var2 = this.contacts.size();
            int var3 = this.usersStartRow;
            if (var1 < var2 + var3) {
               TLObject var4 = (TLObject)this.contacts.get(var1 - var3);
               String var6;
               String var7;
               if (var4 instanceof TLRPC.User) {
                  TLRPC.User var5 = (TLRPC.User)var4;
                  var6 = var5.first_name;
                  var7 = var5.last_name;
               } else {
                  var6 = ((TLRPC.Chat)var4).title;
                  var7 = "";
               }

               if (LocaleController.nameDisplayOrder == 1) {
                  if (!TextUtils.isEmpty(var6)) {
                     return var6.substring(0, 1).toUpperCase();
                  }

                  if (!TextUtils.isEmpty(var7)) {
                     return var7.substring(0, 1).toUpperCase();
                  }
               } else {
                  if (!TextUtils.isEmpty(var7)) {
                     return var7.substring(0, 1).toUpperCase();
                  }

                  if (!TextUtils.isEmpty(var6)) {
                     return var6.substring(0, 1).toUpperCase();
                  }
               }

               return "";
            }
         }

         return null;
      }

      public int getPositionForScrollProgress(float var1) {
         return (int)((float)this.getItemCount() * var1);
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         SparseArray var2 = GroupCreateActivity.this.ignoreUsers;
         boolean var3 = true;
         boolean var4 = var3;
         if (var2 != null) {
            View var5 = var1.itemView;
            var4 = var3;
            if (var5 instanceof GroupCreateUserCell) {
               TLObject var6 = ((GroupCreateUserCell)var5).getObject();
               var4 = var3;
               if (var6 instanceof TLRPC.User) {
                  TLRPC.User var7 = (TLRPC.User)var6;
                  if (GroupCreateActivity.this.ignoreUsers.indexOfKey(var7.id) < 0) {
                     var4 = var3;
                  } else {
                     var4 = false;
                  }
               }
            }
         }

         return var4;
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$0$GroupCreateActivity$GroupCreateAdapter(ArrayList var1, ArrayList var2) {
         this.searchResult = var1;
         this.searchResultNames = var2;
         this.searchAdapterHelper.mergeResults(this.searchResult);
         if (this.searching && !this.searchAdapterHelper.isSearchInProgress()) {
            GroupCreateActivity.this.emptyView.showTextView();
         }

         this.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  TextCell var19 = (TextCell)var1.itemView;
                  if (this.inviteViaLink == 2) {
                     var19.setTextAndIcon(LocaleController.getString("ChannelInviteViaLink", 2131558953), 2131165787, false);
                  } else {
                     var19.setTextAndIcon(LocaleController.getString("InviteToGroupByLink", 2131559688), 2131165787, false);
                  }
               }
            } else {
               GroupCreateUserCell var4;
               boolean var5;
               TLObject var12;
               Object var23;
               CharSequence var24;
               label122: {
                  var4 = (GroupCreateUserCell)var1.itemView;
                  var5 = this.searching;
                  Object var6 = null;
                  TLObject var9;
                  if (var5) {
                     var3 = this.searchResult.size();
                     int var7 = this.searchAdapterHelper.getGlobalSearch().size();
                     int var8 = this.searchAdapterHelper.getLocalServerSearch().size();
                     TLObject var20;
                     if (var2 >= 0 && var2 < var3) {
                        var20 = (TLObject)this.searchResult.get(var2);
                     } else if (var2 >= var3 && var2 < var8 + var3) {
                        var20 = (TLObject)this.searchAdapterHelper.getLocalServerSearch().get(var2 - var3);
                     } else if (var2 > var3 + var8 && var2 <= var7 + var3 + var8) {
                        var20 = (TLObject)this.searchAdapterHelper.getGlobalSearch().get(var2 - var3 - var8 - 1);
                     } else {
                        var20 = null;
                     }

                     var9 = var20;
                     if (var20 != null) {
                        String var10;
                        if (var20 instanceof TLRPC.User) {
                           var10 = ((TLRPC.User)var20).username;
                        } else {
                           var10 = ((TLRPC.Chat)var20).username;
                        }

                        if (var2 < var3) {
                           CharSequence var11 = (CharSequence)this.searchResultNames.get(var2);
                           var23 = var6;
                           var12 = var20;
                           var24 = var11;
                           if (var11 != null) {
                              var23 = var6;
                              var12 = var20;
                              var24 = var11;
                              if (!TextUtils.isEmpty(var10)) {
                                 String var14 = var11.toString();
                                 StringBuilder var15 = new StringBuilder();
                                 var15.append("@");
                                 var15.append(var10);
                                 var23 = var6;
                                 var12 = var20;
                                 var24 = var11;
                                 if (var14.startsWith(var15.toString())) {
                                    var23 = var11;
                                    var24 = null;
                                    var12 = var20;
                                 }
                              }
                           }
                           break label122;
                        }

                        var9 = var20;
                        if (var2 > var3) {
                           var9 = var20;
                           if (!TextUtils.isEmpty(var10)) {
                              String var22 = this.searchAdapterHelper.getLastFoundUsername();
                              String var13 = var22;
                              if (var22.startsWith("@")) {
                                 var13 = var22.substring(1);
                              }

                              label102: {
                                 label131: {
                                    boolean var10001;
                                    try {
                                       var23 = new SpannableStringBuilder();
                                       ((SpannableStringBuilder)var23).append("@");
                                       ((SpannableStringBuilder)var23).append(var10);
                                       var3 = var10.toLowerCase().indexOf(var13);
                                    } catch (Exception var18) {
                                       var10001 = false;
                                       break label131;
                                    }

                                    if (var3 == -1) {
                                       break label102;
                                    }

                                    try {
                                       var2 = var13.length();
                                    } catch (Exception var17) {
                                       var10001 = false;
                                       break label131;
                                    }

                                    if (var3 == 0) {
                                       ++var2;
                                    } else {
                                       ++var3;
                                    }

                                    try {
                                       ForegroundColorSpan var25 = new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4"));
                                       ((SpannableStringBuilder)var23).setSpan(var25, var3, var2 + var3, 33);
                                       break label102;
                                    } catch (Exception var16) {
                                       var10001 = false;
                                    }
                                 }

                                 var24 = null;
                                 var23 = var10;
                                 var12 = var20;
                                 break label122;
                              }

                              var24 = null;
                              var12 = var20;
                              break label122;
                           }
                        }
                     }
                  } else {
                     var9 = (TLObject)this.contacts.get(var2 - this.usersStartRow);
                  }

                  var24 = null;
                  var12 = var9;
                  var23 = var6;
               }

               var4.setObject(var12, var24, (CharSequence)var23);
               if (var12 instanceof TLRPC.User) {
                  var2 = ((TLRPC.User)var12).id;
               } else if (var12 instanceof TLRPC.Chat) {
                  var2 = -((TLRPC.Chat)var12).id;
               } else {
                  var2 = 0;
               }

               if (var2 != 0) {
                  if (GroupCreateActivity.this.ignoreUsers != null && GroupCreateActivity.this.ignoreUsers.indexOfKey(var2) >= 0) {
                     var4.setChecked(true, false);
                     var4.setCheckBoxEnabled(false);
                  } else {
                     if (GroupCreateActivity.this.selectedContacts.indexOfKey(var2) >= 0) {
                        var5 = true;
                     } else {
                        var5 = false;
                     }

                     var4.setChecked(var5, false);
                     var4.setCheckBoxEnabled(true);
                  }
               }
            }
         } else {
            GroupCreateSectionCell var21 = (GroupCreateSectionCell)var1.itemView;
            if (this.searching) {
               var21.setText(LocaleController.getString("GlobalSearch", 2131559594));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new TextCell(this.context);
            } else {
               var3 = new GroupCreateUserCell(this.context, true, 0);
            }
         } else {
            var3 = new GroupCreateSectionCell(this.context);
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof GroupCreateUserCell) {
            ((GroupCreateUserCell)var2).recycle();
         }

      }

      public void searchDialogs(final String var1) {
         try {
            if (this.searchTimer != null) {
               this.searchTimer.cancel();
            }
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         if (var1 == null) {
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults((ArrayList)null);
            SearchAdapterHelper var5 = this.searchAdapterHelper;
            boolean var3;
            if (!GroupCreateActivity.this.isAlwaysShare && !GroupCreateActivity.this.isNeverShare) {
               var3 = false;
            } else {
               var3 = true;
            }

            var5.queryServerSearch((String)null, true, var3, false, false, 0, 0);
            this.notifyDataSetChanged();
         } else {
            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
               // $FF: synthetic method
               public void lambda$null$0$GroupCreateActivity$GroupCreateAdapter$3(String var1x) {
                  String var2 = var1x.trim().toLowerCase();
                  if (var2.length() == 0) {
                     GroupCreateAdapter.this.updateSearchResults(new ArrayList(), new ArrayList());
                  } else {
                     String var3;
                     label85: {
                        var3 = LocaleController.getInstance().getTranslitString(var2);
                        if (!var2.equals(var3)) {
                           var1x = var3;
                           if (var3.length() != 0) {
                              break label85;
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
                     ArrayList var7 = new ArrayList();

                     for(int var8 = 0; var8 < GroupCreateAdapter.this.contacts.size(); ++var8) {
                        TLObject var9 = (TLObject)GroupCreateAdapter.this.contacts.get(var8);
                        boolean var10 = var9 instanceof TLRPC.User;
                        TLRPC.User var16;
                        if (var10) {
                           var16 = (TLRPC.User)var9;
                           var3 = ContactsController.formatName(var16.first_name, var16.last_name).toLowerCase();
                           var1x = var16.username;
                        } else {
                           TLRPC.Chat var17 = (TLRPC.Chat)var9;
                           var3 = var17.title;
                           var1x = var17.username;
                        }

                        String var11 = LocaleController.getInstance().getTranslitString(var3);
                        var2 = var11;
                        if (var3.equals(var11)) {
                           var2 = null;
                        }

                        int var12 = var5.length;
                        int var13 = 0;

                        boolean var19;
                        for(boolean var14 = false; var13 < var12; var14 = var19) {
                           label74: {
                              label73: {
                                 var11 = var5[var13];
                                 if (!var3.startsWith(var11)) {
                                    StringBuilder var15 = new StringBuilder();
                                    var15.append(" ");
                                    var15.append(var11);
                                    if (!var3.contains(var15.toString())) {
                                       if (var2 == null) {
                                          break label73;
                                       }

                                       if (!var2.startsWith(var11)) {
                                          var15 = new StringBuilder();
                                          var15.append(" ");
                                          var15.append(var11);
                                          if (!var2.contains(var15.toString())) {
                                             break label73;
                                          }
                                       }
                                    }
                                 }

                                 var19 = true;
                                 break label74;
                              }

                              var19 = var14;
                              if (var1x != null) {
                                 var19 = var14;
                                 if (var1x.startsWith(var11)) {
                                    var19 = true;
                                 }
                              }
                           }

                           if (var19) {
                              if (var19) {
                                 if (var10) {
                                    var16 = (TLRPC.User)var9;
                                    var7.add(AndroidUtilities.generateSearchName(var16.first_name, var16.last_name, var11));
                                 } else {
                                    var7.add(AndroidUtilities.generateSearchName(((TLRPC.Chat)var9).title, (String)null, var11));
                                 }
                              } else {
                                 StringBuilder var18 = new StringBuilder();
                                 var18.append("@");
                                 var18.append(var1x);
                                 var1x = var18.toString();
                                 var18 = new StringBuilder();
                                 var18.append("@");
                                 var18.append(var11);
                                 var7.add(AndroidUtilities.generateSearchName(var1x, (String)null, var18.toString()));
                              }

                              var6.add(var9);
                              break;
                           }

                           ++var13;
                        }
                     }

                     GroupCreateAdapter.this.updateSearchResults(var6, var7);
                  }
               }

               // $FF: synthetic method
               public void lambda$run$1$GroupCreateActivity$GroupCreateAdapter$3(String var1x) {
                  SearchAdapterHelper var2 = GroupCreateAdapter.this.searchAdapterHelper;
                  boolean var3;
                  if (!GroupCreateActivity.this.isAlwaysShare && !GroupCreateActivity.this.isNeverShare) {
                     var3 = false;
                  } else {
                     var3 = true;
                  }

                  var2.queryServerSearch(var1x, true, var3, true, false, 0, 0);
                  Utilities.searchQueue.postRunnable(new _$$Lambda$GroupCreateActivity$GroupCreateAdapter$3$N0OOEZ27_yY58u6OB8iaEcy4ZUI(this, var1x));
               }

               public void run() {
                  try {
                     GroupCreateAdapter.this.searchTimer.cancel();
                     GroupCreateAdapter.this.searchTimer = null;
                  } catch (Exception var2) {
                     FileLog.e((Throwable)var2);
                  }

                  AndroidUtilities.runOnUIThread(new _$$Lambda$GroupCreateActivity$GroupCreateAdapter$3$wWcLpY_du4_rpNgvonXl87vzFTU(this, var1));
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
         GroupCreateActivity.this.allSpans.add(var1);
         GroupCreateActivity.this.selectedContacts.put(var1.getUid(), var1);
         GroupCreateActivity.this.editText.setHintVisible(false);
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
               GroupCreateActivity.this.editText.setAllowDrawCursor(true);
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
         int var5 = var4 - AndroidUtilities.dp(26.0F);
         var2 = AndroidUtilities.dp(10.0F);
         var1 = AndroidUtilities.dp(10.0F);
         int var6 = 0;
         int var7 = 0;

         int var8;
         int var10;
         int var11;
         float var13;
         float var14;
         for(var8 = 0; var6 < var3; var1 = var10) {
            View var9 = this.getChildAt(var6);
            if (!(var9 instanceof GroupCreateSpan)) {
               var10 = var1;
            } else {
               var9.measure(MeasureSpec.makeMeasureSpec(var4, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0F), 1073741824));
               var10 = var7;
               var11 = var2;
               if (var9 != this.removingSpan) {
                  var10 = var7;
                  var11 = var2;
                  if (var9.getMeasuredWidth() + var7 > var5) {
                     var11 = var2 + var9.getMeasuredHeight() + AndroidUtilities.dp(8.0F);
                     var10 = 0;
                  }
               }

               var7 = var8;
               var2 = var1;
               if (var9.getMeasuredWidth() + var8 > var5) {
                  var2 = var1 + var9.getMeasuredHeight() + AndroidUtilities.dp(8.0F);
                  var7 = 0;
               }

               var1 = AndroidUtilities.dp(13.0F) + var10;
               if (!this.animationStarted) {
                  View var12 = this.removingSpan;
                  if (var9 == var12) {
                     var9.setTranslationX((float)(AndroidUtilities.dp(13.0F) + var7));
                     var9.setTranslationY((float)var2);
                  } else if (var12 != null) {
                     var13 = var9.getTranslationX();
                     var14 = (float)var1;
                     if (var13 != var14) {
                        this.animators.add(ObjectAnimator.ofFloat(var9, "translationX", new float[]{var14}));
                     }

                     var14 = var9.getTranslationY();
                     var13 = (float)var11;
                     if (var14 != var13) {
                        this.animators.add(ObjectAnimator.ofFloat(var9, "translationY", new float[]{var13}));
                     }
                  } else {
                     var9.setTranslationX((float)var1);
                     var9.setTranslationY((float)var11);
                  }
               }

               var1 = var10;
               if (var9 != this.removingSpan) {
                  var1 = var10 + var9.getMeasuredWidth() + AndroidUtilities.dp(9.0F);
               }

               var8 = var7 + var9.getMeasuredWidth() + AndroidUtilities.dp(9.0F);
               var10 = var2;
               var2 = var11;
               var7 = var1;
            }

            ++var6;
         }

         if (AndroidUtilities.isTablet()) {
            var10 = AndroidUtilities.dp(372.0F) / 3;
         } else {
            Point var15 = AndroidUtilities.displaySize;
            var10 = (Math.min(var15.x, var15.y) - AndroidUtilities.dp(158.0F)) / 3;
         }

         var6 = var7;
         var11 = var2;
         if (var5 - var7 < var10) {
            var11 = var2 + AndroidUtilities.dp(40.0F);
            var6 = 0;
         }

         var2 = var1;
         if (var5 - var8 < var10) {
            var2 = var1 + AndroidUtilities.dp(40.0F);
         }

         GroupCreateActivity.this.editText.measure(MeasureSpec.makeMeasureSpec(var5 - var6, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0F), 1073741824));
         if (!this.animationStarted) {
            var1 = AndroidUtilities.dp(42.0F);
            var7 = var6 + AndroidUtilities.dp(16.0F);
            GroupCreateActivity.this.fieldY = var11;
            if (this.currentAnimation != null) {
               var1 = var11 + AndroidUtilities.dp(42.0F);
               if (GroupCreateActivity.this.containerHeight != var1) {
                  this.animators.add(ObjectAnimator.ofInt(GroupCreateActivity.this, "containerHeight", new int[]{var1}));
               }

               var14 = GroupCreateActivity.this.editText.getTranslationX();
               var13 = (float)var7;
               if (var14 != var13) {
                  this.animators.add(ObjectAnimator.ofFloat(GroupCreateActivity.this.editText, "translationX", new float[]{var13}));
               }

               if (GroupCreateActivity.this.editText.getTranslationY() != (float)GroupCreateActivity.this.fieldY) {
                  this.animators.add(ObjectAnimator.ofFloat(GroupCreateActivity.this.editText, "translationY", new float[]{(float)GroupCreateActivity.this.fieldY}));
               }

               GroupCreateActivity.this.editText.setAllowDrawCursor(false);
               this.currentAnimation.playTogether(this.animators);
               this.currentAnimation.start();
               this.animationStarted = true;
            } else {
               GroupCreateActivity.this.containerHeight = var2 + var1;
               GroupCreateActivity.this.editText.setTranslationX((float)var7);
               GroupCreateActivity.this.editText.setTranslationY((float)GroupCreateActivity.this.fieldY);
            }
         } else if (this.currentAnimation != null && !GroupCreateActivity.this.ignoreScrollEvent && this.removingSpan == null) {
            GroupCreateActivity.this.editText.bringPointIntoView(GroupCreateActivity.this.editText.getSelectionStart());
         }

         this.setMeasuredDimension(var4, GroupCreateActivity.this.containerHeight);
      }

      public void removeSpan(final GroupCreateSpan var1) {
         GroupCreateActivity.this.ignoreScrollEvent = true;
         GroupCreateActivity.this.selectedContacts.remove(var1.getUid());
         GroupCreateActivity.this.allSpans.remove(var1);
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
               GroupCreateActivity.this.editText.setAllowDrawCursor(true);
               if (GroupCreateActivity.this.allSpans.isEmpty()) {
                  GroupCreateActivity.this.editText.setHintVisible(true);
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
