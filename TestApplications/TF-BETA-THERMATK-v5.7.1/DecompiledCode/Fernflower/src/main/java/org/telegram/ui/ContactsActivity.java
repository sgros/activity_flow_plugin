package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Property;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.ContactsAdapter;
import org.telegram.ui.Adapters.SearchAdapter;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class ContactsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int search_button = 0;
   private static final int sort_button = 1;
   private boolean allowBots = true;
   private boolean allowUsernameSearch = true;
   private boolean askAboutContacts = true;
   private int channelId;
   private int chatId;
   private boolean checkPermission = true;
   private boolean createSecretChat;
   private boolean creatingChat;
   private ContactsActivity.ContactsActivityDelegate delegate;
   private boolean destroyAfterSelect;
   private EmptyTextProgressView emptyView;
   private ImageView floatingButton;
   private FrameLayout floatingButtonContainer;
   private boolean floatingHidden;
   private AccelerateDecelerateInterpolator floatingInterpolator = new AccelerateDecelerateInterpolator();
   private SparseArray ignoreUsers;
   private LinearLayoutManager layoutManager;
   private RecyclerListView listView;
   private ContactsAdapter listViewAdapter;
   private boolean needFinishFragment = true;
   private boolean needForwardCount = true;
   private boolean needPhonebook;
   private boolean onlyUsers;
   private AlertDialog permissionDialog;
   private int prevPosition;
   private int prevTop;
   private boolean returnAsResult;
   private boolean scrollUpdated;
   private SearchAdapter searchListViewAdapter;
   private boolean searchWas;
   private boolean searching;
   private String selectAlertString = null;
   private boolean sortByName;
   private ActionBarMenuItem sortItem;

   public ContactsActivity(Bundle var1) {
      super(var1);
   }

   @TargetApi(23)
   private void askForPermissons(boolean var1) {
      Activity var2 = this.getParentActivity();
      if (var2 != null && UserConfig.getInstance(super.currentAccount).syncContacts && var2.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
         if (var1 && this.askAboutContacts) {
            this.showDialog(AlertsCreator.createContactsPermissionDialog(var2, new _$$Lambda$ContactsActivity$mwsX5pHa6b8khBUhCBIBphXHFEY(this)).create());
            return;
         }

         ArrayList var3 = new ArrayList();
         var3.add("android.permission.READ_CONTACTS");
         var3.add("android.permission.WRITE_CONTACTS");
         var3.add("android.permission.GET_ACCOUNTS");
         var2.requestPermissions((String[])var3.toArray(new String[0]), 1);
      }

   }

   private void didSelectResult(TLRPC.User var1, boolean var2, String var3) {
      if (var2 && this.selectAlertString != null) {
         if (this.getParentActivity() == null) {
            return;
         }

         if (var1.bot) {
            if (var1.bot_nochats) {
               try {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("BotCantJoinGroups", 2131558849), 0).show();
               } catch (Exception var7) {
                  FileLog.e((Throwable)var7);
               }

               return;
            }

            if (this.channelId != 0) {
               TLRPC.Chat var10 = MessagesController.getInstance(super.currentAccount).getChat(this.channelId);
               AlertDialog.Builder var12 = new AlertDialog.Builder(this.getParentActivity());
               if (ChatObject.canAddAdmins(var10)) {
                  var12.setTitle(LocaleController.getString("AppName", 2131558635));
                  var12.setMessage(LocaleController.getString("AddBotAsAdmin", 2131558565));
                  var12.setPositiveButton(LocaleController.getString("MakeAdmin", 2131559795), new _$$Lambda$ContactsActivity$mmdvMVULktgHXYl_8FoV1fI5DjI(this, var1, var3));
                  var12.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               } else {
                  var12.setMessage(LocaleController.getString("CantAddBotAsAdmin", 2131558902));
                  var12.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
               }

               this.showDialog(var12.create());
               return;
            }
         }

         AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
         var4.setTitle(LocaleController.getString("AppName", 2131558635));
         String var11 = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(var1));
         final EditText var9;
         if (!var1.bot && this.needForwardCount) {
            var11 = String.format("%s\n\n%s", var11, LocaleController.getString("AddToTheGroupForwardCount", 2131558595));
            var9 = new EditText(this.getParentActivity());
            var9.setTextSize(1, 18.0F);
            var9.setText("50");
            var9.setTextColor(Theme.getColor("dialogTextBlack"));
            var9.setGravity(17);
            var9.setInputType(2);
            var9.setImeOptions(6);
            var9.setBackgroundDrawable(Theme.createEditTextDrawable(this.getParentActivity(), true));
            var9.addTextChangedListener(new TextWatcher() {
               public void afterTextChanged(Editable var1) {
                  Exception var10000;
                  label40: {
                     int var2;
                     String var8;
                     boolean var10001;
                     try {
                        var8 = var1.toString();
                        if (var8.length() == 0) {
                           return;
                        }

                        var2 = Utilities.parseInt(var8);
                     } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label40;
                     }

                     if (var2 < 0) {
                        try {
                           var9.setText("0");
                           var9.setSelection(var9.length());
                           return;
                        } catch (Exception var4) {
                           var10000 = var4;
                           var10001 = false;
                        }
                     } else if (var2 > 300) {
                        try {
                           var9.setText("300");
                           var9.setSelection(var9.length());
                           return;
                        } catch (Exception var5) {
                           var10000 = var5;
                           var10001 = false;
                        }
                     } else {
                        try {
                           StringBuilder var3 = new StringBuilder();
                           var3.append("");
                           var3.append(var2);
                           if (!var8.equals(var3.toString())) {
                              EditText var10 = var9;
                              var3 = new StringBuilder();
                              var3.append("");
                              var3.append(var2);
                              var10.setText(var3.toString());
                              var9.setSelection(var9.length());
                           }

                           return;
                        } catch (Exception var6) {
                           var10000 = var6;
                           var10001 = false;
                        }
                     }
                  }

                  Exception var9x = var10000;
                  FileLog.e((Throwable)var9x);
               }

               public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }

               public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
               }
            });
            var4.setView(var9);
         } else {
            var9 = null;
         }

         var4.setMessage(var11);
         var4.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ContactsActivity$YV_dxfbBQkZGaBy_yZbP6YhG1n0(this, var1, var9));
         var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var4.create());
         if (var9 != null) {
            MarginLayoutParams var8 = (MarginLayoutParams)var9.getLayoutParams();
            if (var8 != null) {
               if (var8 instanceof LayoutParams) {
                  ((LayoutParams)var8).gravity = 1;
               }

               int var6 = AndroidUtilities.dp(24.0F);
               var8.leftMargin = var6;
               var8.rightMargin = var6;
               var8.height = AndroidUtilities.dp(36.0F);
               var9.setLayoutParams(var8);
            }

            var9.setSelection(var9.getText().length());
         }
      } else {
         ContactsActivity.ContactsActivityDelegate var5 = this.delegate;
         if (var5 != null) {
            var5.didSelectContact(var1, var3, this);
            this.delegate = null;
         }

         if (this.needFinishFragment) {
            this.finishFragment();
         }
      }

   }

   private void hideFloatingButton(boolean var1) {
      if (this.floatingHidden != var1) {
         this.floatingHidden = var1;
         AnimatorSet var2 = new AnimatorSet();
         FrameLayout var3 = this.floatingButtonContainer;
         Property var4 = View.TRANSLATION_Y;
         int var5;
         if (this.floatingHidden) {
            var5 = AndroidUtilities.dp(100.0F);
         } else {
            var5 = 0;
         }

         var2.playTogether(new Animator[]{ObjectAnimator.ofFloat(var3, var4, new float[]{(float)var5})});
         var2.setDuration(300L);
         var2.setInterpolator(this.floatingInterpolator);
         this.floatingButtonContainer.setClickable(var1 ^ true);
         var2.start();
      }
   }

   private void updateVisibleRows(int var1) {
      RecyclerListView var2 = this.listView;
      if (var2 != null) {
         int var3 = var2.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var5 = this.listView.getChildAt(var4);
            if (var5 instanceof UserCell) {
               ((UserCell)var5).update(var1);
            }
         }
      }

   }

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      if (this.destroyAfterSelect) {
         if (this.returnAsResult) {
            super.actionBar.setTitle(LocaleController.getString("SelectContact", 2131560680));
         } else if (this.createSecretChat) {
            super.actionBar.setTitle(LocaleController.getString("NewSecretChat", 2131559909));
         } else {
            super.actionBar.setTitle(LocaleController.getString("NewMessageTitle", 2131559901));
         }
      } else {
         super.actionBar.setTitle(LocaleController.getString("Contacts", 2131559149));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ContactsActivity.this.finishFragment();
            } else {
               byte var2 = 1;
               if (var1 == 1) {
                  SharedConfig.toggleSortContactsByName();
                  ContactsActivity.this.sortByName = SharedConfig.sortContactsByName;
                  ContactsAdapter var3 = ContactsActivity.this.listViewAdapter;
                  byte var4;
                  if (ContactsActivity.this.sortByName) {
                     var4 = var2;
                  } else {
                     var4 = 2;
                  }

                  var3.setSortType(var4);
                  ActionBarMenuItem var5 = ContactsActivity.this.sortItem;
                  if (ContactsActivity.this.sortByName) {
                     var1 = 2131165363;
                  } else {
                     var1 = 2131165362;
                  }

                  var5.setIcon(var1);
               }
            }

         }
      });
      ActionBarMenu var2 = super.actionBar.createMenu();
      ActionBarMenuItem var3 = var2.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            ContactsActivity.this.searchListViewAdapter.searchDialogs((String)null);
            ContactsActivity.this.searching = false;
            ContactsActivity.this.searchWas = false;
            ContactsActivity.this.listView.setAdapter(ContactsActivity.this.listViewAdapter);
            ContactsActivity.this.listView.setSectionsType(1);
            ContactsActivity.this.listViewAdapter.notifyDataSetChanged();
            ContactsActivity.this.listView.setFastScrollVisible(true);
            ContactsActivity.this.listView.setVerticalScrollBarEnabled(false);
            ContactsActivity.this.listView.setEmptyView((View)null);
            ContactsActivity.this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
            if (ContactsActivity.this.floatingButtonContainer != null) {
               ContactsActivity.this.floatingButtonContainer.setVisibility(0);
               ContactsActivity.this.floatingHidden = true;
               ContactsActivity.this.floatingButtonContainer.setTranslationY((float)AndroidUtilities.dp(100.0F));
               ContactsActivity.this.hideFloatingButton(false);
            }

            if (ContactsActivity.this.sortItem != null) {
               ContactsActivity.this.sortItem.setVisibility(0);
            }

         }

         public void onSearchExpand() {
            ContactsActivity.this.searching = true;
            if (ContactsActivity.this.floatingButtonContainer != null) {
               ContactsActivity.this.floatingButtonContainer.setVisibility(8);
            }

            if (ContactsActivity.this.sortItem != null) {
               ContactsActivity.this.sortItem.setVisibility(8);
            }

         }

         public void onTextChanged(EditText var1) {
            if (ContactsActivity.this.searchListViewAdapter != null) {
               String var2 = var1.getText().toString();
               if (var2.length() != 0) {
                  ContactsActivity.this.searchWas = true;
                  if (ContactsActivity.this.listView != null) {
                     ContactsActivity.this.listView.setAdapter(ContactsActivity.this.searchListViewAdapter);
                     ContactsActivity.this.listView.setSectionsType(0);
                     ContactsActivity.this.searchListViewAdapter.notifyDataSetChanged();
                     ContactsActivity.this.listView.setFastScrollVisible(false);
                     ContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                  }

                  if (ContactsActivity.this.emptyView != null) {
                     ContactsActivity.this.listView.setEmptyView(ContactsActivity.this.emptyView);
                     ContactsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                  }
               }

               ContactsActivity.this.searchListViewAdapter.searchDialogs(var2);
            }
         }
      });
      var3.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      var3.setContentDescription(LocaleController.getString("Search", 2131560640));
      int var4;
      if (!this.createSecretChat && !this.returnAsResult) {
         if (this.sortByName) {
            var4 = 2131165363;
         } else {
            var4 = 2131165362;
         }

         this.sortItem = var2.addItem(1, var4);
         this.sortItem.setContentDescription(LocaleController.getString("AccDescrContactSorting", 2131558429));
      }

      byte var5;
      byte var6;
      byte var23;
      label100: {
         this.searchListViewAdapter = new SearchAdapter(var1, this.ignoreUsers, this.allowUsernameSearch, false, false, this.allowBots, 0);
         var4 = this.chatId;
         var5 = 3;
         if (var4 != 0) {
            var23 = ChatObject.canUserDoAdminAction(MessagesController.getInstance(super.currentAccount).getChat(this.chatId), 3);
         } else {
            if (this.channelId == 0) {
               var6 = 0;
               break label100;
            }

            TLRPC.Chat var14 = MessagesController.getInstance(super.currentAccount).getChat(this.channelId);
            if (ChatObject.canUserDoAdminAction(var14, 3) && TextUtils.isEmpty(var14.username)) {
               var23 = 2;
            } else {
               var23 = 0;
            }
         }

         var6 = var23;
      }

      this.listViewAdapter = new ContactsAdapter(var1, this.onlyUsers, this.needPhonebook, this.ignoreUsers, var6) {
         public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (ContactsActivity.this.listView != null && ContactsActivity.this.listView.getAdapter() == this) {
               int var1 = super.getItemCount();
               boolean var2 = ContactsActivity.this.needPhonebook;
               boolean var3 = true;
               boolean var4 = true;
               byte var5 = 8;
               EmptyTextProgressView var6;
               RecyclerListView var7;
               if (var2) {
                  var6 = ContactsActivity.this.emptyView;
                  if (var1 == 2) {
                     var5 = 0;
                  }

                  var6.setVisibility(var5);
                  var7 = ContactsActivity.this.listView;
                  if (var1 == 2) {
                     var4 = false;
                  }

                  var7.setFastScrollVisible(var4);
               } else {
                  var6 = ContactsActivity.this.emptyView;
                  if (var1 == 0) {
                     var5 = 0;
                  }

                  var6.setVisibility(var5);
                  var7 = ContactsActivity.this.listView;
                  if (var1 != 0) {
                     var4 = var3;
                  } else {
                     var4 = false;
                  }

                  var7.setFastScrollVisible(var4);
               }
            }

         }
      };
      ContactsAdapter var15 = this.listViewAdapter;
      if (this.sortItem != null) {
         if (this.sortByName) {
            var23 = 1;
         } else {
            var23 = 2;
         }
      } else {
         var23 = 0;
      }

      var15.setSortType(var23);
      super.fragmentView = new FrameLayout(var1) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if (ContactsActivity.this.listView.getAdapter() == ContactsActivity.this.listViewAdapter) {
               if (ContactsActivity.this.emptyView.getVisibility() == 0) {
                  ContactsActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(74.0F));
               }
            } else {
               ContactsActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(0.0F));
            }

         }
      };
      FrameLayout var16 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.setShowAtCenter(true);
      this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
      this.emptyView.showTextView();
      var16.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setSectionsType(1);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setFastScrollEnabled();
      RecyclerListView var17 = this.listView;
      LinearLayoutManager var7 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var7;
      var17.setLayoutManager(var7);
      this.listView.setAdapter(this.listViewAdapter);
      var16.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ContactsActivity$gnTnCjocys4rzqdEYocufNT6sRA(this, var6)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         private boolean scrollingManually;

         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1) {
               if (ContactsActivity.this.searching && ContactsActivity.this.searchWas) {
                  AndroidUtilities.hideKeyboard(ContactsActivity.this.getParentActivity().getCurrentFocus());
               }

               this.scrollingManually = true;
            } else {
               this.scrollingManually = false;
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            super.onScrolled(var1, var2, var3);
            if (ContactsActivity.this.floatingButtonContainer != null && ContactsActivity.this.floatingButtonContainer.getVisibility() != 8) {
               int var4 = ContactsActivity.this.layoutManager.findFirstVisibleItemPosition();
               boolean var9 = false;
               View var8 = var1.getChildAt(0);
               if (var8 != null) {
                  var2 = var8.getTop();
               } else {
                  var2 = 0;
               }

               boolean var7;
               label45: {
                  boolean var6;
                  if (ContactsActivity.this.prevPosition == var4) {
                     int var5 = ContactsActivity.this.prevTop;
                     if (var2 < ContactsActivity.this.prevTop) {
                        var6 = true;
                     } else {
                        var6 = false;
                     }

                     var7 = var6;
                     if (Math.abs(var5 - var2) <= 1) {
                        break label45;
                     }
                  } else if (var4 > ContactsActivity.this.prevPosition) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  var9 = true;
                  var7 = var6;
               }

               if (var9 && ContactsActivity.this.scrollUpdated && (var7 || !var7 && this.scrollingManually)) {
                  ContactsActivity.this.hideFloatingButton(var7);
               }

               ContactsActivity.this.prevPosition = var4;
               ContactsActivity.this.prevTop = var2;
               ContactsActivity.this.scrollUpdated = true;
            }

         }
      });
      if (!this.createSecretChat && !this.returnAsResult) {
         this.floatingButtonContainer = new FrameLayout(var1);
         FrameLayout var19 = this.floatingButtonContainer;
         if (VERSION.SDK_INT >= 21) {
            var23 = 56;
         } else {
            var23 = 60;
         }

         if (VERSION.SDK_INT >= 21) {
            var6 = 56;
         } else {
            var6 = 60;
         }

         float var8 = (float)(var6 + 14);
         if (LocaleController.isRTL) {
            var6 = var5;
         } else {
            var6 = 5;
         }

         float var9;
         if (LocaleController.isRTL) {
            var9 = 4.0F;
         } else {
            var9 = 0.0F;
         }

         float var10;
         if (LocaleController.isRTL) {
            var10 = 0.0F;
         } else {
            var10 = 4.0F;
         }

         var16.addView(var19, LayoutHelper.createFrame(var23 + 20, var8, var6 | 80, var9, 0.0F, var10, 0.0F));
         this.floatingButtonContainer.setOnClickListener(new _$$Lambda$ContactsActivity$XQCfC9ukjnkgkb2J_v7QLg6mf6E(this));
         this.floatingButton = new ImageView(var1);
         this.floatingButton.setScaleType(ScaleType.CENTER);
         Drawable var20 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
         Object var18 = var20;
         if (VERSION.SDK_INT < 21) {
            Drawable var11 = var1.getResources().getDrawable(2131165387).mutate();
            var11.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
            var18 = new CombinedDrawable(var11, var20, 0, 0);
            ((CombinedDrawable)var18).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
         }

         this.floatingButton.setBackgroundDrawable((Drawable)var18);
         this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), Mode.MULTIPLY));
         this.floatingButton.setImageResource(2131165280);
         this.floatingButtonContainer.setContentDescription(LocaleController.getString("CreateNewContact", 2131559170));
         if (VERSION.SDK_INT >= 21) {
            StateListAnimator var12 = new StateListAnimator();
            ObjectAnimator var21 = ObjectAnimator.ofFloat(this.floatingButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
            var12.addState(new int[]{16842919}, var21);
            var21 = ObjectAnimator.ofFloat(this.floatingButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
            var12.addState(new int[0], var21);
            this.floatingButton.setStateListAnimator(var12);
            this.floatingButton.setOutlineProvider(new ViewOutlineProvider() {
               @SuppressLint({"NewApi"})
               public void getOutline(View var1, Outline var2) {
                  var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
               }
            });
         }

         FrameLayout var13 = this.floatingButtonContainer;
         ImageView var22 = this.floatingButton;
         if (VERSION.SDK_INT >= 21) {
            var23 = 56;
         } else {
            var23 = 60;
         }

         if (VERSION.SDK_INT >= 21) {
            var6 = 56;
         } else {
            var6 = 60;
         }

         var13.addView(var22, LayoutHelper.createFrame(var23, (float)var6, 51, 10.0F, 0.0F, 10.0F, 0.0F));
      }

      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      ContactsAdapter var5;
      if (var1 == NotificationCenter.contactsDidLoad) {
         var5 = this.listViewAdapter;
         if (var5 != null) {
            var5.notifyDataSetChanged();
         }
      } else if (var1 == NotificationCenter.updateInterfaces) {
         var1 = (Integer)var3[0];
         if ((var1 & 2) != 0 || (var1 & 1) != 0 || (var1 & 4) != 0) {
            this.updateVisibleRows(var1);
         }

         if ((var1 & 4) != 0 && !this.sortByName) {
            var5 = this.listViewAdapter;
            if (var5 != null) {
               var5.sortOnlineContacts();
            }
         }
      } else if (var1 == NotificationCenter.encryptedChatCreated) {
         if (this.createSecretChat && this.creatingChat) {
            TLRPC.EncryptedChat var4 = (TLRPC.EncryptedChat)var3[0];
            Bundle var6 = new Bundle();
            var6.putInt("enc_id", var4.id);
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
            this.presentFragment(new ChatActivity(var6), true);
         }
      } else if (var1 == NotificationCenter.closeChats && !this.creatingChat) {
         this.removeSelfFromStack();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY var1 = new _$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY(this);
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
      ThemeDescription var45 = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder");
      ThemeDescription var44 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollActive");
      ThemeDescription var15 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollInactive");
      ThemeDescription var16 = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "fastScrollText");
      ThemeDescription var17 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var18 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var19 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var20 = this.listView;
      Drawable var21 = Theme.avatar_broadcastDrawable;
      Drawable var22 = Theme.avatar_savedDrawable;
      ThemeDescription var23 = new ThemeDescription(var20, 0, new Class[]{UserCell.class}, (Paint)null, new Drawable[]{var21, var22}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text");
      ThemeDescription var24 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed");
      ThemeDescription var47 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange");
      ThemeDescription var48 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet");
      ThemeDescription var25 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen");
      ThemeDescription var26 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan");
      ThemeDescription var46 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue");
      ThemeDescription var27 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink");
      ThemeDescription var28 = new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var29 = new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon");
      ThemeDescription var30 = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionIcon");
      ThemeDescription var31 = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionBackground");
      ThemeDescription var32 = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_actionPressedBackground");
      ThemeDescription var33 = new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText");
      ThemeDescription var43 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection");
      RecyclerListView var34 = this.listView;
      Drawable var35 = Theme.dialogs_groupDrawable;
      Drawable var36 = Theme.dialogs_broadcastDrawable;
      Drawable var37 = Theme.dialogs_botDrawable;
      ThemeDescription var49 = new ThemeDescription(var34, 0, new Class[]{ProfileSearchCell.class}, (Paint)null, new Drawable[]{var35, var36, var37}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_nameIcon");
      RecyclerListView var50 = this.listView;
      var36 = Theme.dialogs_verifiedCheckDrawable;
      ThemeDescription var52 = new ThemeDescription(var50, 0, new Class[]{ProfileSearchCell.class}, (Paint)null, new Drawable[]{var36}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_verifiedCheck");
      RecyclerListView var53 = this.listView;
      var35 = Theme.dialogs_verifiedDrawable;
      ThemeDescription var51 = new ThemeDescription(var53, 0, new Class[]{ProfileSearchCell.class}, (Paint)null, new Drawable[]{var35}, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_verifiedBackground");
      RecyclerListView var38 = this.listView;
      TextPaint var54 = Theme.dialogs_offlinePaint;
      ThemeDescription var55 = new ThemeDescription(var38, 0, new Class[]{ProfileSearchCell.class}, var54, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3");
      RecyclerListView var39 = this.listView;
      TextPaint var56 = Theme.dialogs_onlinePaint;
      ThemeDescription var57 = new ThemeDescription(var39, 0, new Class[]{ProfileSearchCell.class}, var56, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText3");
      var39 = this.listView;
      TextPaint var40 = Theme.dialogs_namePaint;
      TextPaint var41 = Theme.dialogs_searchNamePaint;
      ThemeDescription var58 = new ThemeDescription(var39, 0, new Class[]{ProfileSearchCell.class}, (String[])null, new Paint[]{var40, var41}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_name");
      var39 = this.listView;
      TextPaint var42 = Theme.dialogs_nameEncryptedPaint;
      var41 = Theme.dialogs_searchNameEncryptedPaint;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var14, var45, var44, var15, var16, var17, var18, var19, var23, var24, var47, var48, var25, var26, var46, var27, var28, var29, var30, var31, var32, var33, var43, var49, var52, var51, var55, var57, var58, new ThemeDescription(var39, 0, new Class[]{ProfileSearchCell.class}, (String[])null, new Paint[]{var42, var41}, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "chats_secretName")};
   }

   // $FF: synthetic method
   public void lambda$askForPermissons$6$ContactsActivity(int var1) {
      boolean var2;
      if (var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.askAboutContacts = var2;
      if (var1 != 0) {
         this.askForPermissons(false);
      }
   }

   // $FF: synthetic method
   public void lambda$createView$1$ContactsActivity(int var1, View var2, int var3) {
      SparseArray var4;
      TLRPC.User var7;
      Bundle var10;
      if (this.searching && this.searchWas) {
         var7 = (TLRPC.User)this.searchListViewAdapter.getItem(var3);
         if (var7 == null) {
            return;
         }

         if (this.searchListViewAdapter.isGlobalSearch(var3)) {
            ArrayList var14 = new ArrayList();
            var14.add(var7);
            MessagesController.getInstance(super.currentAccount).putUsers(var14, false);
            MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(var14, (ArrayList)null, false, true);
         }

         if (this.returnAsResult) {
            var4 = this.ignoreUsers;
            if (var4 != null && var4.indexOfKey(var7.id) >= 0) {
               return;
            }

            this.didSelectResult(var7, true, (String)null);
         } else if (this.createSecretChat) {
            if (var7.id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
               return;
            }

            this.creatingChat = true;
            SecretChatHelper.getInstance(super.currentAccount).startSecretChat(this.getParentActivity(), var7);
         } else {
            var10 = new Bundle();
            var10.putInt("user_id", var7.id);
            if (MessagesController.getInstance(super.currentAccount).checkCanOpenChat(var10, this)) {
               this.presentFragment(new ChatActivity(var10), true);
            }
         }
      } else {
         int var5 = this.listViewAdapter.getSectionForPosition(var3);
         var3 = this.listViewAdapter.getPositionInSectionForPosition(var3);
         if (var3 >= 0 && var5 >= 0) {
            if ((!this.onlyUsers || var1 != 0) && var5 == 0) {
               if (this.needPhonebook) {
                  if (var3 == 0) {
                     this.presentFragment(new InviteContactsActivity());
                  }
               } else if (var1 != 0) {
                  if (var3 == 0) {
                     var1 = this.chatId;
                     if (var1 == 0) {
                        var1 = this.channelId;
                     }

                     this.presentFragment(new GroupInviteActivity(var1));
                  }
               } else if (var3 == 0) {
                  this.presentFragment(new GroupCreateActivity(new Bundle()), false);
               } else {
                  Bundle var11;
                  if (var3 == 1) {
                     var11 = new Bundle();
                     var11.putBoolean("onlyUsers", true);
                     var11.putBoolean("destroyAfterSelect", true);
                     var11.putBoolean("createSecretChat", true);
                     var11.putBoolean("allowBots", false);
                     this.presentFragment(new ContactsActivity(var11), false);
                  } else if (var3 == 2) {
                     SharedPreferences var13 = MessagesController.getGlobalMainSettings();
                     if (!BuildVars.DEBUG_VERSION && var13.getBoolean("channel_intro", false)) {
                        var11 = new Bundle();
                        var11.putInt("step", 0);
                        this.presentFragment(new ChannelCreateActivity(var11));
                     } else {
                        this.presentFragment(new ChannelIntroActivity());
                        var13.edit().putBoolean("channel_intro", true).commit();
                     }
                  }
               }
            } else {
               Object var6 = this.listViewAdapter.getItem(var5, var3);
               if (var6 instanceof TLRPC.User) {
                  var7 = (TLRPC.User)var6;
                  if (this.returnAsResult) {
                     var4 = this.ignoreUsers;
                     if (var4 != null && var4.indexOfKey(var7.id) >= 0) {
                        return;
                     }

                     this.didSelectResult(var7, true, (String)null);
                  } else if (this.createSecretChat) {
                     this.creatingChat = true;
                     SecretChatHelper.getInstance(super.currentAccount).startSecretChat(this.getParentActivity(), var7);
                  } else {
                     var10 = new Bundle();
                     var10.putInt("user_id", var7.id);
                     if (MessagesController.getInstance(super.currentAccount).checkCanOpenChat(var10, this)) {
                        this.presentFragment(new ChatActivity(var10), true);
                     }
                  }
               } else if (var6 instanceof ContactsController.Contact) {
                  ContactsController.Contact var8 = (ContactsController.Contact)var6;
                  String var9;
                  if (!var8.phones.isEmpty()) {
                     var9 = (String)var8.phones.get(0);
                  } else {
                     var9 = null;
                  }

                  if (var9 != null && this.getParentActivity() != null) {
                     AlertDialog.Builder var12 = new AlertDialog.Builder(this.getParentActivity());
                     var12.setMessage(LocaleController.getString("InviteUser", 2131559691));
                     var12.setTitle(LocaleController.getString("AppName", 2131558635));
                     var12.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ContactsActivity$UtCNWtIF8nw25DCjjOFgiXVoPRQ(this, var9));
                     var12.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     this.showDialog(var12.create());
                  }
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$2$ContactsActivity(View var1) {
      this.presentFragment(new NewContactActivity());
   }

   // $FF: synthetic method
   public void lambda$didSelectResult$3$ContactsActivity(TLRPC.User var1, String var2, DialogInterface var3, int var4) {
      ContactsActivity.ContactsActivityDelegate var5 = this.delegate;
      if (var5 != null) {
         var5.didSelectContact(var1, var2, this);
         this.delegate = null;
      }

   }

   // $FF: synthetic method
   public void lambda$didSelectResult$4$ContactsActivity(TLRPC.User var1, EditText var2, DialogInterface var3, int var4) {
      String var5;
      if (var2 != null) {
         var5 = var2.getText().toString();
      } else {
         var5 = "0";
      }

      this.didSelectResult(var1, false, var5);
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$7$ContactsActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof UserCell) {
               ((UserCell)var4).update(0);
            } else if (var4 instanceof ProfileSearchCell) {
               ((ProfileSearchCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$ContactsActivity(String var1, DialogInterface var2, int var3) {
      try {
         Intent var5 = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", var1, (String)null));
         var5.putExtra("sms_body", ContactsController.getInstance(super.currentAccount).getInviteText(1));
         this.getParentActivity().startActivityForResult(var5, 500);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$onResume$5$ContactsActivity(int var1) {
      boolean var2;
      if (var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.askAboutContacts = var2;
      if (var1 != 0) {
         this.askForPermissons(false);
      }
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      FrameLayout var2 = this.floatingButtonContainer;
      if (var2 != null) {
         var2.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
               FrameLayout var1 = ContactsActivity.this.floatingButtonContainer;
               int var2;
               if (ContactsActivity.this.floatingHidden) {
                  var2 = AndroidUtilities.dp(100.0F);
               } else {
                  var2 = 0;
               }

               var1.setTranslationY((float)var2);
               ContactsActivity.this.floatingButtonContainer.setClickable(ContactsActivity.this.floatingHidden ^ true);
               if (ContactsActivity.this.floatingButtonContainer != null) {
                  ContactsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
               }

            }
         });
      }

   }

   protected void onDialogDismiss(Dialog var1) {
      super.onDialogDismiss(var1);
      AlertDialog var2 = this.permissionDialog;
      if (var2 != null && var1 == var2 && this.getParentActivity() != null && this.askAboutContacts) {
         this.askForPermissons(false);
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.encryptedChatCreated);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
      this.checkPermission = UserConfig.getInstance(super.currentAccount).syncContacts;
      if (super.arguments != null) {
         this.onlyUsers = this.getArguments().getBoolean("onlyUsers", false);
         this.destroyAfterSelect = super.arguments.getBoolean("destroyAfterSelect", false);
         this.returnAsResult = super.arguments.getBoolean("returnAsResult", false);
         this.createSecretChat = super.arguments.getBoolean("createSecretChat", false);
         this.selectAlertString = super.arguments.getString("selectAlertString");
         this.allowUsernameSearch = super.arguments.getBoolean("allowUsernameSearch", true);
         this.needForwardCount = super.arguments.getBoolean("needForwardCount", true);
         this.allowBots = super.arguments.getBoolean("allowBots", true);
         this.channelId = super.arguments.getInt("channelId", 0);
         this.needFinishFragment = super.arguments.getBoolean("needFinishFragment", true);
         this.chatId = super.arguments.getInt("chat_id", 0);
      } else {
         this.needPhonebook = true;
      }

      if (!this.createSecretChat && !this.returnAsResult) {
         this.sortByName = SharedConfig.sortContactsByName;
      }

      ContactsController.getInstance(super.currentAccount).checkInviteText();
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.encryptedChatCreated);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      this.delegate = null;
   }

   public void onPause() {
      super.onPause();
      ActionBar var1 = super.actionBar;
      if (var1 != null) {
         var1.closeSearchField();
      }

   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 1) {
         for(var1 = 0; var1 < var2.length; ++var1) {
            if (var3.length > var1 && "android.permission.READ_CONTACTS".equals(var2[var1])) {
               if (var3[var1] == 0) {
                  ContactsController.getInstance(super.currentAccount).forceImportContacts();
               } else {
                  Editor var4 = MessagesController.getGlobalNotificationsSettings().edit();
                  this.askAboutContacts = false;
                  var4.putBoolean("askAboutContacts", false).commit();
               }
            }
         }
      }

   }

   public void onResume() {
      super.onResume();
      ContactsAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      if (this.checkPermission && VERSION.SDK_INT >= 23) {
         Activity var2 = this.getParentActivity();
         if (var2 != null) {
            this.checkPermission = false;
            if (var2.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
               if (var2.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
                  AlertDialog var3 = AlertsCreator.createContactsPermissionDialog(var2, new _$$Lambda$ContactsActivity$xFVgeouE3JWG_nldltsKWVnF4Hg(this)).create();
                  this.permissionDialog = var3;
                  this.showDialog(var3);
               } else {
                  this.askForPermissons(true);
               }
            }
         }
      }

   }

   public void setDelegate(ContactsActivity.ContactsActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setIgnoreUsers(SparseArray var1) {
      this.ignoreUsers = var1;
   }

   public interface ContactsActivityDelegate {
      void didSelectContact(TLRPC.User var1, String var2, ContactsActivity var3);
   }
}
