package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.StickerSetCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class GroupStickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int done_button = 1;
   private int chatId;
   private ActionBarMenuItem doneItem;
   private AnimatorSet doneItemAnimation;
   private boolean donePressed;
   private EditText editText;
   private ImageView eraseImageView;
   private int headerRow;
   private boolean ignoreTextChanges;
   private TLRPC.ChatFull info;
   private int infoRow;
   private LinearLayoutManager layoutManager;
   private GroupStickersActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private LinearLayout nameContainer;
   private int nameRow;
   private ContextProgressView progressView;
   private Runnable queryRunnable;
   private int reqId;
   private int rowCount;
   private boolean searchWas;
   private boolean searching;
   private int selectedStickerRow;
   private TLRPC.TL_messages_stickerSet selectedStickerSet;
   private int stickersEndRow;
   private int stickersShadowRow;
   private int stickersStartRow;
   private EditTextBoldCursor usernameTextView;

   public GroupStickersActivity(int var1) {
      this.chatId = var1;
   }

   // $FF: synthetic method
   static int access$1300(GroupStickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1700(GroupStickersActivity var0) {
      return var0.currentAccount;
   }

   private void resolveStickerSet() {
      if (this.listAdapter != null) {
         if (this.reqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
         }

         Runnable var1 = this.queryRunnable;
         if (var1 != null) {
            AndroidUtilities.cancelRunOnUIThread(var1);
            this.queryRunnable = null;
         }

         this.selectedStickerSet = null;
         if (this.usernameTextView.length() <= 0) {
            this.searching = false;
            this.searchWas = false;
            if (this.selectedStickerRow != -1) {
               this.updateRows();
            }

         } else {
            this.searching = true;
            this.searchWas = true;
            String var2 = this.usernameTextView.getText().toString();
            TLRPC.TL_messages_stickerSet var4 = DataQuery.getInstance(super.currentAccount).getStickerSetByName(var2);
            if (var4 != null) {
               this.selectedStickerSet = var4;
            }

            int var3 = this.selectedStickerRow;
            if (var3 == -1) {
               this.updateRows();
            } else {
               this.listAdapter.notifyItemChanged(var3);
            }

            if (var4 != null) {
               this.searching = false;
            } else {
               _$$Lambda$GroupStickersActivity$4Rqel5HptCXt4RyUWgtwIaVXIfk var5 = new _$$Lambda$GroupStickersActivity$4Rqel5HptCXt4RyUWgtwIaVXIfk(this, var2);
               this.queryRunnable = var5;
               AndroidUtilities.runOnUIThread(var5, 500L);
            }
         }
      }
   }

   private void saveStickerSet() {
      TLRPC.ChatFull var1 = this.info;
      if (var1 != null) {
         label33: {
            TLRPC.StickerSet var2 = var1.stickerset;
            if (var2 != null) {
               TLRPC.TL_messages_stickerSet var4 = this.selectedStickerSet;
               if (var4 != null && var4.set.id == var2.id) {
                  break label33;
               }
            }

            if (this.info.stickerset != null || this.selectedStickerSet != null) {
               this.showEditDoneProgress(true);
               TLRPC.TL_channels_setStickers var5 = new TLRPC.TL_channels_setStickers();
               var5.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chatId);
               if (this.selectedStickerSet == null) {
                  var5.stickerset = new TLRPC.TL_inputStickerSetEmpty();
               } else {
                  Editor var3 = MessagesController.getEmojiSettings(super.currentAccount).edit();
                  StringBuilder var6 = new StringBuilder();
                  var6.append("group_hide_stickers_");
                  var6.append(this.info.id);
                  var3.remove(var6.toString()).commit();
                  var5.stickerset = new TLRPC.TL_inputStickerSetID();
                  TLRPC.InputStickerSet var7 = var5.stickerset;
                  var2 = this.selectedStickerSet.set;
                  var7.id = var2.id;
                  var7.access_hash = var2.access_hash;
               }

               ConnectionsManager.getInstance(super.currentAccount).sendRequest(var5, new _$$Lambda$GroupStickersActivity$SODwEnuUWhbL0_5dEEd38j4JKu8(this));
               return;
            }
         }
      }

      this.finishFragment();
   }

   private void showEditDoneProgress(final boolean var1) {
      if (this.doneItem != null) {
         AnimatorSet var2 = this.doneItemAnimation;
         if (var2 != null) {
            var2.cancel();
         }

         this.doneItemAnimation = new AnimatorSet();
         if (var1) {
            this.progressView.setVisibility(0);
            this.doneItem.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{1.0F})});
         } else {
            this.doneItem.getImageView().setVisibility(0);
            this.doneItem.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), "alpha", new float[]{1.0F})});
         }

         this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (GroupStickersActivity.this.doneItemAnimation != null && GroupStickersActivity.this.doneItemAnimation.equals(var1x)) {
                  GroupStickersActivity.this.doneItemAnimation = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (GroupStickersActivity.this.doneItemAnimation != null && GroupStickersActivity.this.doneItemAnimation.equals(var1x)) {
                  if (!var1) {
                     GroupStickersActivity.this.progressView.setVisibility(4);
                  } else {
                     GroupStickersActivity.this.doneItem.getImageView().setVisibility(4);
                  }
               }

            }
         });
         this.doneItemAnimation.setDuration(150L);
         this.doneItemAnimation.start();
      }
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.nameRow = var1;
      if (this.selectedStickerSet == null && !this.searchWas) {
         this.selectedStickerRow = -1;
      } else {
         var1 = this.rowCount++;
         this.selectedStickerRow = var1;
      }

      var1 = this.rowCount++;
      this.infoRow = var1;
      ArrayList var2 = DataQuery.getInstance(super.currentAccount).getStickerSets(0);
      if (!var2.isEmpty()) {
         var1 = this.rowCount++;
         this.headerRow = var1;
         var1 = this.rowCount;
         this.stickersStartRow = var1;
         this.stickersEndRow = var1 + var2.size();
         this.rowCount += var2.size();
         var1 = this.rowCount++;
         this.stickersShadowRow = var1;
      } else {
         this.headerRow = -1;
         this.stickersStartRow = -1;
         this.stickersEndRow = -1;
         this.stickersShadowRow = -1;
      }

      LinearLayout var3 = this.nameContainer;
      if (var3 != null) {
         var3.invalidate();
      }

      GroupStickersActivity.ListAdapter var4 = this.listAdapter;
      if (var4 != null) {
         var4.notifyDataSetChanged();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("GroupStickers", 2131559615));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               GroupStickersActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (GroupStickersActivity.this.donePressed) {
                  return;
               }

               GroupStickersActivity.this.donePressed = true;
               if (GroupStickersActivity.this.searching) {
                  GroupStickersActivity.this.showEditDoneProgress(true);
                  return;
               }

               GroupStickersActivity.this.saveStickerSet();
            }

         }
      });
      this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      this.progressView = new ContextProgressView(var1, 1);
      this.progressView.setAlpha(0.0F);
      this.progressView.setScaleX(0.1F);
      this.progressView.setScaleY(0.1F);
      this.progressView.setVisibility(4);
      this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
      this.nameContainer = new LinearLayout(var1) {
         protected void onDraw(Canvas var1) {
            if (GroupStickersActivity.this.selectedStickerSet != null) {
               var1.drawLine(0.0F, (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
            }

         }

         protected void onMeasure(int var1, int var2) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0F), 1073741824));
         }
      };
      this.nameContainer.setWeightSum(1.0F);
      this.nameContainer.setWillNotDraw(false);
      this.nameContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      this.nameContainer.setOrientation(0);
      this.nameContainer.setPadding(AndroidUtilities.dp(17.0F), 0, AndroidUtilities.dp(14.0F), 0);
      this.editText = new EditText(var1);
      EditText var2 = this.editText;
      StringBuilder var3 = new StringBuilder();
      var3.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
      var3.append("/addstickers/");
      var2.setText(var3.toString());
      this.editText.setTextSize(1, 17.0F);
      this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.editText.setMaxLines(1);
      this.editText.setLines(1);
      this.editText.setEnabled(false);
      this.editText.setFocusable(false);
      this.editText.setBackgroundDrawable((Drawable)null);
      this.editText.setPadding(0, 0, 0, 0);
      this.editText.setGravity(16);
      this.editText.setSingleLine(true);
      this.editText.setInputType(163840);
      this.editText.setImeOptions(6);
      this.nameContainer.addView(this.editText, LayoutHelper.createLinear(-2, 42));
      this.usernameTextView = new EditTextBoldCursor(var1);
      this.usernameTextView.setTextSize(1, 17.0F);
      this.usernameTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.usernameTextView.setCursorSize(AndroidUtilities.dp(20.0F));
      this.usernameTextView.setCursorWidth(1.5F);
      this.usernameTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.usernameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.usernameTextView.setMaxLines(1);
      this.usernameTextView.setLines(1);
      this.usernameTextView.setBackgroundDrawable((Drawable)null);
      this.usernameTextView.setPadding(0, 0, 0, 0);
      this.usernameTextView.setSingleLine(true);
      this.usernameTextView.setGravity(16);
      this.usernameTextView.setInputType(163872);
      this.usernameTextView.setImeOptions(6);
      this.usernameTextView.setHint(LocaleController.getString("ChooseStickerSetPlaceholder", 2131559096));
      this.usernameTextView.addTextChangedListener(new TextWatcher() {
         boolean ignoreTextChange;

         public void afterTextChanged(Editable var1) {
            if (GroupStickersActivity.this.eraseImageView != null) {
               ImageView var2 = GroupStickersActivity.this.eraseImageView;
               byte var3;
               if (var1.length() > 0) {
                  var3 = 0;
               } else {
                  var3 = 4;
               }

               var2.setVisibility(var3);
            }

            if (!this.ignoreTextChange && !GroupStickersActivity.this.ignoreTextChanges) {
               if (var1.length() > 5) {
                  this.ignoreTextChange = true;

                  label54: {
                     boolean var10001;
                     Uri var6;
                     try {
                        var6 = Uri.parse(var1.toString());
                     } catch (Exception var5) {
                        var10001 = false;
                        break label54;
                     }

                     if (var6 != null) {
                        try {
                           List var7 = var6.getPathSegments();
                           if (var7.size() == 2 && ((String)var7.get(0)).toLowerCase().equals("addstickers")) {
                              GroupStickersActivity.this.usernameTextView.setText((CharSequence)var7.get(1));
                              GroupStickersActivity.this.usernameTextView.setSelection(GroupStickersActivity.this.usernameTextView.length());
                           }
                        } catch (Exception var4) {
                           var10001 = false;
                        }
                     }
                  }

                  this.ignoreTextChange = false;
               }

               GroupStickersActivity.this.resolveStickerSet();
            }

         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      this.nameContainer.addView(this.usernameTextView, LayoutHelper.createLinear(0, 42, 1.0F));
      this.eraseImageView = new ImageView(var1);
      this.eraseImageView.setScaleType(ScaleType.CENTER);
      this.eraseImageView.setImageResource(2131165437);
      this.eraseImageView.setPadding(AndroidUtilities.dp(16.0F), 0, 0, 0);
      this.eraseImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), Mode.MULTIPLY));
      this.eraseImageView.setVisibility(4);
      this.eraseImageView.setOnClickListener(new _$$Lambda$GroupStickersActivity$0gmuCbw_2WYqittTzpHTcYQ1s5I(this));
      this.nameContainer.addView(this.eraseImageView, LayoutHelper.createLinear(42, 42, 0.0F));
      TLRPC.ChatFull var4 = this.info;
      if (var4 != null) {
         TLRPC.StickerSet var5 = var4.stickerset;
         if (var5 != null) {
            this.ignoreTextChanges = true;
            this.usernameTextView.setText(var5.short_name);
            EditTextBoldCursor var6 = this.usernameTextView;
            var6.setSelection(var6.length());
            this.ignoreTextChanges = false;
         }
      }

      this.listAdapter = new GroupStickersActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var7 = (FrameLayout)super.fragmentView;
      var7.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      this.listView.setFocusable(true);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.layoutManager = new LinearLayoutManager(var1) {
         public boolean requestChildRectangleOnScreen(RecyclerView var1, View var2, Rect var3, boolean var4, boolean var5) {
            return false;
         }

         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager.setOrientation(1);
      this.listView.setLayoutManager(this.layoutManager);
      var7.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$GroupStickersActivity$cr2TDaX3YsSRoO_CaDIAKHsP5zc(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1) {
               AndroidUtilities.hideKeyboard(GroupStickersActivity.this.getParentActivity().getCurrentFocus());
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
         }
      });
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.stickersDidLoad) {
         if ((Integer)var3[0] == 0) {
            this.updateRows();
         }
      } else {
         TLRPC.ChatFull var4;
         if (var1 == NotificationCenter.chatInfoDidLoad) {
            var4 = (TLRPC.ChatFull)var3[0];
            if (var4.id == this.chatId) {
               if (this.info == null && var4.stickerset != null) {
                  this.selectedStickerSet = DataQuery.getInstance(super.currentAccount).getGroupStickerSetById(var4.stickerset);
               }

               this.info = var4;
               this.updateRows();
            }
         } else if (var1 == NotificationCenter.groupStickersDidLoad) {
            (Long)var3[0];
            var4 = this.info;
            if (var4 != null) {
               TLRPC.StickerSet var5 = var4.stickerset;
               if (var5 != null && var5.id == (long)var1) {
                  this.updateRows();
               }
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{StickerSetCell.class, TextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.nameContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "stickers_menuSelector"), new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "stickers_menu")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$GroupStickersActivity(View var1) {
      this.searchWas = false;
      this.selectedStickerSet = null;
      this.usernameTextView.setText("");
      this.updateRows();
   }

   // $FF: synthetic method
   public void lambda$createView$1$GroupStickersActivity(View var1, int var2) {
      if (this.getParentActivity() != null) {
         int var3 = this.selectedStickerRow;
         if (var2 == var3) {
            if (this.selectedStickerSet == null) {
               return;
            }

            this.showDialog(new StickersAlert(this.getParentActivity(), this, (TLRPC.InputStickerSet)null, this.selectedStickerSet, (StickersAlert.StickersAlertDelegate)null));
         } else if (var2 >= this.stickersStartRow && var2 < this.stickersEndRow) {
            boolean var8;
            if (var3 == -1) {
               var8 = true;
            } else {
               var8 = false;
            }

            int var4 = this.layoutManager.findFirstVisibleItemPosition();
            RecyclerListView.Holder var6 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(var4);
            int var5;
            if (var6 != null) {
               var5 = var6.itemView.getTop();
            } else {
               var5 = Integer.MAX_VALUE;
            }

            this.selectedStickerSet = (TLRPC.TL_messages_stickerSet)DataQuery.getInstance(super.currentAccount).getStickerSets(0).get(var2 - this.stickersStartRow);
            this.ignoreTextChanges = true;
            this.usernameTextView.setText(this.selectedStickerSet.set.short_name);
            EditTextBoldCursor var7 = this.usernameTextView;
            var7.setSelection(var7.length());
            this.ignoreTextChanges = false;
            AndroidUtilities.hideKeyboard(this.usernameTextView);
            this.updateRows();
            if (var8 && var5 != Integer.MAX_VALUE) {
               this.layoutManager.scrollToPositionWithOffset(var4 + 1, var5);
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$2$GroupStickersActivity(TLObject var1) {
      this.searching = false;
      int var2;
      if (var1 instanceof TLRPC.TL_messages_stickerSet) {
         this.selectedStickerSet = (TLRPC.TL_messages_stickerSet)var1;
         if (this.donePressed) {
            this.saveStickerSet();
         } else {
            var2 = this.selectedStickerRow;
            if (var2 != -1) {
               this.listAdapter.notifyItemChanged(var2);
            } else {
               this.updateRows();
            }
         }
      } else {
         var2 = this.selectedStickerRow;
         if (var2 != -1) {
            this.listAdapter.notifyItemChanged(var2);
         }

         if (this.donePressed) {
            this.donePressed = false;
            this.showEditDoneProgress(false);
            if (this.getParentActivity() != null) {
               Toast.makeText(this.getParentActivity(), LocaleController.getString("AddStickersNotFound", 2131558587), 0).show();
            }
         }
      }

      this.reqId = 0;
   }

   // $FF: synthetic method
   public void lambda$null$3$GroupStickersActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$GroupStickersActivity$k9BjjbmWE3K4PF660XiggMGiLuI(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$6$GroupStickersActivity(TLRPC.TL_error var1) {
      if (var1 == null) {
         TLRPC.TL_messages_stickerSet var4 = this.selectedStickerSet;
         if (var4 == null) {
            this.info.stickerset = null;
         } else {
            this.info.stickerset = var4.set;
            DataQuery.getInstance(super.currentAccount).putGroupStickerSet(this.selectedStickerSet);
         }

         TLRPC.ChatFull var5 = this.info;
         if (var5.stickerset == null) {
            var5.flags |= 256;
         } else {
            var5.flags &= -257;
         }

         MessagesStorage.getInstance(super.currentAccount).updateChatInfo(this.info, false);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, this.info, 0, true, null);
         this.finishFragment();
      } else {
         Activity var2 = this.getParentActivity();
         StringBuilder var3 = new StringBuilder();
         var3.append(LocaleController.getString("ErrorOccurred", 2131559375));
         var3.append("\n");
         var3.append(var1.text);
         Toast.makeText(var2, var3.toString(), 0).show();
         this.donePressed = false;
         this.showEditDoneProgress(false);
      }

   }

   // $FF: synthetic method
   public void lambda$onTransitionAnimationEnd$5$GroupStickersActivity() {
      EditTextBoldCursor var1 = this.usernameTextView;
      if (var1 != null) {
         var1.requestFocus();
         AndroidUtilities.showKeyboard(this.usernameTextView);
      }

   }

   // $FF: synthetic method
   public void lambda$resolveStickerSet$4$GroupStickersActivity(String var1) {
      if (this.queryRunnable != null) {
         TLRPC.TL_messages_getStickerSet var2 = new TLRPC.TL_messages_getStickerSet();
         var2.stickerset = new TLRPC.TL_inputStickerSetShortName();
         var2.stickerset.short_name = var1;
         this.reqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$GroupStickersActivity$oURnGCXrOgZUJRKYNWwDhi5jZEA(this));
      }
   }

   // $FF: synthetic method
   public void lambda$saveStickerSet$7$GroupStickersActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$GroupStickersActivity$Z_OfWFCln7TEpsXJNMwvYTvczmA(this, var2));
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      DataQuery.getInstance(super.currentAccount).checkStickers(0);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
      this.updateRows();
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
   }

   public void onResume() {
      super.onResume();
      GroupStickersActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
         this.usernameTextView.requestFocus();
         AndroidUtilities.showKeyboard(this.usernameTextView);
      }

   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$GroupStickersActivity$r9HzrnDTDEDApQN3gt_U8hwAtPA(this), 100L);
      }

   }

   public void setInfo(TLRPC.ChatFull var1) {
      this.info = var1;
      var1 = this.info;
      if (var1 != null && var1.stickerset != null) {
         this.selectedStickerSet = DataQuery.getInstance(super.currentAccount).getGroupStickerSetById(this.info.stickerset);
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return GroupStickersActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 >= GroupStickersActivity.this.stickersStartRow && var1 < GroupStickersActivity.this.stickersEndRow) {
            return 0;
         } else if (var1 == GroupStickersActivity.this.infoRow) {
            return 1;
         } else if (var1 == GroupStickersActivity.this.nameRow) {
            return 2;
         } else if (var1 == GroupStickersActivity.this.stickersShadowRow) {
            return 3;
         } else if (var1 == GroupStickersActivity.this.headerRow) {
            return 4;
         } else {
            return var1 == GroupStickersActivity.this.selectedStickerRow ? 5 : 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3;
         if (var2 != 0 && var2 != 2 && var2 != 5) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = true;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 4) {
                  if (var3 == 5) {
                     StickerSetCell var12 = (StickerSetCell)var1.itemView;
                     if (GroupStickersActivity.this.selectedStickerSet != null) {
                        var12.setStickersSet(GroupStickersActivity.this.selectedStickerSet, false);
                     } else if (GroupStickersActivity.this.searching) {
                        var12.setText(LocaleController.getString("Loading", 2131559768), (String)null, 0, false);
                     } else {
                        var12.setText(LocaleController.getString("ChooseStickerSetNotFound", 2131559094), LocaleController.getString("ChooseStickerSetNotFoundInfo", 2131559095), 2131165471, false);
                     }
                  }
               } else {
                  ((HeaderCell)var1.itemView).setText(LocaleController.getString("ChooseFromYourStickers", 2131559089));
               }
            } else if (var2 == GroupStickersActivity.this.infoRow) {
               String var5 = LocaleController.getString("ChooseStickerSetMy", 2131559093);
               var2 = var5.indexOf("@stickers");
               if (var2 != -1) {
                  try {
                     SpannableStringBuilder var6 = new SpannableStringBuilder(var5);
                     URLSpanNoUnderline var7 = new URLSpanNoUnderline("@stickers") {
                        public void onClick(View var1) {
                           MessagesController.getInstance(GroupStickersActivity.access$1700(GroupStickersActivity.this)).openByUserName("stickers", GroupStickersActivity.this, 1);
                        }
                     };
                     var6.setSpan(var7, var2, var2 + 9, 18);
                     ((TextInfoPrivacyCell)var1.itemView).setText(var6);
                  } catch (Exception var11) {
                     FileLog.e((Throwable)var11);
                     ((TextInfoPrivacyCell)var1.itemView).setText(var5);
                  }
               } else {
                  ((TextInfoPrivacyCell)var1.itemView).setText(var5);
               }
            }
         } else {
            ArrayList var14 = DataQuery.getInstance(GroupStickersActivity.access$1300(GroupStickersActivity.this)).getStickerSets(0);
            var2 -= GroupStickersActivity.this.stickersStartRow;
            StickerSetCell var15 = (StickerSetCell)var1.itemView;
            TLRPC.TL_messages_stickerSet var13 = (TLRPC.TL_messages_stickerSet)var14.get(var2);
            TLRPC.TL_messages_stickerSet var16 = (TLRPC.TL_messages_stickerSet)var14.get(var2);
            boolean var8;
            if (var2 != var14.size() - 1) {
               var8 = true;
            } else {
               var8 = false;
            }

            var15.setStickersSet(var16, var8);
            long var9;
            if (GroupStickersActivity.this.selectedStickerSet != null) {
               var9 = GroupStickersActivity.this.selectedStickerSet.set.id;
            } else if (GroupStickersActivity.this.info != null && GroupStickersActivity.this.info.stickerset != null) {
               var9 = GroupStickersActivity.this.info.stickerset.id;
            } else {
               var9 = 0L;
            }

            if (var13.set.id == var9) {
               var8 = var4;
            } else {
               var8 = false;
            }

            var15.setChecked(var8);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var5;
         label35: {
            byte var3 = 3;
            if (var2 != 0) {
               if (var2 == 1) {
                  var5 = new TextInfoPrivacyCell(this.mContext);
                  ((View)var5).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  break label35;
               }

               if (var2 == 2) {
                  var5 = GroupStickersActivity.this.nameContainer;
                  break label35;
               }

               if (var2 == 3) {
                  var5 = new ShadowSectionCell(this.mContext);
                  ((View)var5).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  break label35;
               }

               if (var2 == 4) {
                  var5 = new HeaderCell(this.mContext);
                  ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  break label35;
               }

               if (var2 != 5) {
                  var5 = null;
                  break label35;
               }
            }

            Context var4 = this.mContext;
            byte var6;
            if (var2 == 0) {
               var6 = var3;
            } else {
               var6 = 2;
            }

            var5 = new StickerSetCell(var4, var6);
            ((View)var5).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         ((View)var5).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var5);
      }
   }
}
