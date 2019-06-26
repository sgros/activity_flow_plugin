package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PollEditTextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class PollCreateActivity extends BaseFragment {
   private static final int MAX_ANSWER_LENGTH = 100;
   private static final int MAX_QUESTION_LENGTH = 255;
   private static final int done_button = 1;
   private int addAnswerRow;
   private int answerHeaderRow;
   private int answerSectionRow;
   private int answerStartRow;
   private String[] answers = new String[10];
   private int answersCount = 1;
   private PollCreateActivity.PollCreateActivityDelegate delegate;
   private ActionBarMenuItem doneItem;
   private AnimatorSet doneItemAnimation;
   private PollCreateActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private ContextProgressView progressView;
   private int questionHeaderRow;
   private int questionRow;
   private int questionSectionRow;
   private String questionString;
   private int requestFieldFocusAtPosition = -1;
   private int rowCount;

   // $FF: synthetic method
   static int access$1310(PollCreateActivity var0) {
      int var1 = var0.answersCount--;
      return var1;
   }

   private void addNewField() {
      ++this.answersCount;
      if (this.answersCount == this.answers.length) {
         this.listAdapter.notifyItemRemoved(this.addAnswerRow);
      }

      this.listAdapter.notifyItemInserted(this.addAnswerRow);
      this.updateRows();
      this.requestFieldFocusAtPosition = this.answerStartRow + this.answersCount - 1;
      this.listAdapter.notifyItemChanged(this.answerSectionRow);
   }

   private boolean checkDiscard() {
      boolean var1 = TextUtils.isEmpty(this.getFixedString(this.questionString));
      boolean var2 = var1;
      if (var1) {
         int var3 = 0;

         for(var2 = var1; var3 < this.answersCount; ++var3) {
            var2 = TextUtils.isEmpty(this.getFixedString(this.answers[var3]));
            if (!var2) {
               break;
            }
         }
      }

      if (!var2) {
         AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
         var4.setTitle(LocaleController.getString("CancelPollAlertTitle", 2131558899));
         var4.setMessage(LocaleController.getString("CancelPollAlertText", 2131558898));
         var4.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$PollCreateActivity$_DXfIcr2KYyqZPStWFTU7JfUNVo(this));
         var4.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var4.create());
      }

      return var2;
   }

   private void checkDoneButton() {
      boolean var1 = TextUtils.isEmpty(this.getFixedString(this.questionString));
      boolean var2 = false;
      boolean var3 = var2;
      if (!var1) {
         if (this.questionString.length() > 255) {
            var3 = var2;
         } else {
            int var4 = 0;
            int var5 = 0;

            int var7;
            while(true) {
               String[] var6 = this.answers;
               var7 = var5;
               if (var4 >= var6.length) {
                  break;
               }

               var7 = var5;
               if (!TextUtils.isEmpty(this.getFixedString(var6[var4]))) {
                  if (this.answers[var4].length() > 100) {
                     var7 = 0;
                     break;
                  }

                  var7 = var5 + 1;
               }

               ++var4;
               var5 = var7;
            }

            if (var7 < 2) {
               var3 = var2;
            } else {
               var3 = true;
            }
         }
      }

      this.doneItem.setEnabled(var3);
      ActionBarMenuItem var9 = this.doneItem;
      float var8;
      if (var3) {
         var8 = 1.0F;
      } else {
         var8 = 0.5F;
      }

      var9.setAlpha(var8);
   }

   private String getFixedString(String var1) {
      if (TextUtils.isEmpty(var1)) {
         return var1;
      } else {
         var1 = AndroidUtilities.getTrimmedString(var1).toString();

         while(true) {
            String var2 = var1;
            if (!var1.contains("\n\n\n")) {
               while(var2.startsWith("\n\n\n")) {
                  var2 = var2.replace("\n\n\n", "\n\n");
               }

               return var2;
            }

            var1 = var1.replace("\n\n\n", "\n\n");
         }
      }
   }

   private void setTextLeft(View var1, int var2) {
      boolean var3 = var1 instanceof HeaderCell;
      String var4 = "windowBackgroundWhiteRedText5";
      SimpleTextView var7;
      if (var3) {
         HeaderCell var6 = (HeaderCell)var1;
         if (var2 == -1) {
            String var5 = this.questionString;
            if (var5 != null) {
               var2 = var5.length();
            } else {
               var2 = 0;
            }

            var2 = 255 - var2;
            if ((float)var2 <= 76.5F) {
               var6.setText2(String.format("%d", var2));
               var7 = var6.getTextView2();
               if (var2 >= 0) {
                  var4 = "windowBackgroundWhiteGrayText3";
               }

               var7.setTextColor(Theme.getColor(var4));
               var7.setTag(var4);
            } else {
               var6.setText2("");
            }
         } else {
            var6.setText2("");
         }
      } else if (var1 instanceof PollEditTextCell && var2 >= 0) {
         PollEditTextCell var9 = (PollEditTextCell)var1;
         String[] var8 = this.answers;
         if (var8[var2] != null) {
            var2 = var8[var2].length();
         } else {
            var2 = 0;
         }

         var2 = 100 - var2;
         if ((float)var2 <= 30.0F) {
            var9.setText2(String.format("%d", var2));
            var7 = var9.getTextView2();
            if (var2 >= 0) {
               var4 = "windowBackgroundWhiteGrayText3";
            }

            var7.setTextColor(Theme.getColor(var4));
            var7.setTag(var4);
         } else {
            var9.setText2("");
         }
      }

   }

   private void showEditDoneProgress(final boolean var1) {
      AnimatorSet var2 = this.doneItemAnimation;
      if (var2 != null) {
         var2.cancel();
      }

      this.doneItemAnimation = new AnimatorSet();
      if (var1) {
         this.progressView.setVisibility(0);
         this.doneItem.setEnabled(false);
         this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, new float[]{1.0F})});
      } else {
         this.doneItem.getImageView().setVisibility(0);
         this.doneItem.setEnabled(true);
         this.doneItemAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, new float[]{0.1F}), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.doneItem.getImageView(), View.ALPHA, new float[]{1.0F})});
      }

      this.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (PollCreateActivity.this.doneItemAnimation != null && PollCreateActivity.this.doneItemAnimation.equals(var1x)) {
               PollCreateActivity.this.doneItemAnimation = null;
            }

         }

         public void onAnimationEnd(Animator var1x) {
            if (PollCreateActivity.this.doneItemAnimation != null && PollCreateActivity.this.doneItemAnimation.equals(var1x)) {
               if (!var1) {
                  PollCreateActivity.this.progressView.setVisibility(4);
               } else {
                  PollCreateActivity.this.doneItem.getImageView().setVisibility(4);
               }
            }

         }
      });
      this.doneItemAnimation.setDuration(150L);
      this.doneItemAnimation.start();
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.questionHeaderRow = var1;
      var1 = this.rowCount++;
      this.questionRow = var1;
      var1 = this.rowCount++;
      this.questionSectionRow = var1;
      var1 = this.rowCount++;
      this.answerHeaderRow = var1;
      int var2 = this.answersCount;
      if (var2 != 0) {
         var1 = this.rowCount;
         this.answerStartRow = var1;
         this.rowCount = var1 + var2;
      } else {
         this.answerStartRow = -1;
      }

      if (this.answersCount != this.answers.length) {
         var1 = this.rowCount++;
         this.addAnswerRow = var1;
      } else {
         this.addAnswerRow = -1;
      }

      var1 = this.rowCount++;
      this.answerSectionRow = var1;
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setTitle(LocaleController.getString("NewPoll", 2131559908));
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (PollCreateActivity.this.checkDiscard()) {
                  PollCreateActivity.this.finishFragment();
               }
            } else if (var1 == 1) {
               TLRPC.TL_messageMediaPoll var2 = new TLRPC.TL_messageMediaPoll();
               var2.poll = new TLRPC.TL_poll();
               TLRPC.TL_poll var3 = var2.poll;
               PollCreateActivity var4 = PollCreateActivity.this;
               var3.question = var4.getFixedString(var4.questionString);

               for(var1 = 0; var1 < PollCreateActivity.this.answers.length; ++var1) {
                  PollCreateActivity var5 = PollCreateActivity.this;
                  if (!TextUtils.isEmpty(var5.getFixedString(var5.answers[var1]))) {
                     TLRPC.TL_pollAnswer var6 = new TLRPC.TL_pollAnswer();
                     var4 = PollCreateActivity.this;
                     var6.text = var4.getFixedString(var4.answers[var1]);
                     var6.option = new byte[1];
                     var6.option[0] = (byte)((byte)(var2.poll.answers.size() + 48));
                     var2.poll.answers.add(var6);
                  }
               }

               var2.results = new TLRPC.TL_pollResults();
               PollCreateActivity.this.delegate.sendPoll(var2);
               PollCreateActivity.this.finishFragment();
            }

         }
      });
      this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F), LocaleController.getString("Done", 2131559299));
      this.progressView = new ContextProgressView(var1, 1);
      this.progressView.setAlpha(0.0F);
      this.progressView.setScaleX(0.1F);
      this.progressView.setScaleY(0.1F);
      this.progressView.setVisibility(4);
      this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
      this.listAdapter = new PollCreateActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1) {
         protected void onMeasure(int var1, int var2) {
            super.onMeasure(var1, var2);
         }

         public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
            var2.bottom += AndroidUtilities.dp(60.0F);
            return super.requestChildRectangleOnScreen(var1, var2, var3);
         }

         public void requestLayout() {
            super.requestLayout();
         }
      };
      this.listView.setVerticalScrollBarEnabled(false);
      ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      (new ItemTouchHelper(new PollCreateActivity.TouchHelperCallback())).attachToRecyclerView(this.listView);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PollCreateActivity$BSXK_C_StVS1Xs7U8esUPs_UHrg(this)));
      this.checkDoneButton();
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextSettingsCell.class, PollEditTextCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText5");
      ThemeDescription var10 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{HeaderCell.class}, new String[]{"textView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3");
      ThemeDescription var11 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var12 = new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText");
      ThemeDescription var13 = new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{PollEditTextCell.class}, new String[]{"deleteImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText");
      ThemeDescription var14 = new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{PollEditTextCell.class}, new String[]{"deleteImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "stickers_menuSelector");
      ThemeDescription var15 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{PollEditTextCell.class}, new String[]{"textView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText5");
      ThemeDescription var16 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{PollEditTextCell.class}, new String[]{"textView2"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3");
      ThemeDescription var17 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var18 = this.listView;
      Paint var19 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, new ThemeDescription(var18, 0, new Class[]{View.class}, var19, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText")};
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$2$PollCreateActivity(DialogInterface var1, int var2) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$0$PollCreateActivity(View var1, int var2) {
      if (var2 == this.addAnswerRow) {
         this.addNewField();
      }

   }

   // $FF: synthetic method
   public void lambda$onTransitionAnimationEnd$1$PollCreateActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         RecyclerView.ViewHolder var2 = var1.findViewHolderForAdapterPosition(this.questionRow);
         if (var2 != null) {
            EditTextBoldCursor var3 = ((PollEditTextCell)var2.itemView).getTextView();
            var3.requestFocus();
            AndroidUtilities.showKeyboard(var3);
         }
      }

   }

   public boolean onBackPressed() {
      return this.checkDiscard();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.updateRows();
      return true;
   }

   public void onResume() {
      super.onResume();
      PollCreateActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$PollCreateActivity$RBrZqW0OoyKbyRn7oclxtwGFtGc(this), 100L);
      }

   }

   public void setDelegate(PollCreateActivity.PollCreateActivityDelegate var1) {
      this.delegate = var1;
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      // $FF: synthetic method
      static boolean lambda$onCreateViewHolder$2(PollEditTextCell var0, View var1, int var2, KeyEvent var3) {
         EditTextBoldCursor var4 = (EditTextBoldCursor)var1;
         if (var2 == 67 && var3.getAction() == 0 && var4.length() == 0) {
            var0.callOnDelete();
            return true;
         } else {
            return false;
         }
      }

      public int getItemCount() {
         return PollCreateActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != PollCreateActivity.this.questionHeaderRow && var1 != PollCreateActivity.this.answerHeaderRow) {
            if (var1 == PollCreateActivity.this.questionSectionRow) {
               return 1;
            } else if (var1 == PollCreateActivity.this.answerSectionRow) {
               return 2;
            } else if (var1 == PollCreateActivity.this.addAnswerRow) {
               return 3;
            } else {
               return var1 == PollCreateActivity.this.questionRow ? 4 : 5;
            }
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getAdapterPosition() == PollCreateActivity.this.addAnswerRow) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$PollCreateActivity$ListAdapter(View var1) {
         if (var1.getTag() == null) {
            var1.setTag(1);
            RecyclerView.ViewHolder var4 = PollCreateActivity.this.listView.findContainingViewHolder((View)var1.getParent());
            if (var4 != null) {
               int var2 = var4.getAdapterPosition();
               int var3 = var2 - PollCreateActivity.this.answerStartRow;
               PollCreateActivity.this.listAdapter.notifyItemRemoved(var4.getAdapterPosition());
               System.arraycopy(PollCreateActivity.this.answers, var3 + 1, PollCreateActivity.this.answers, var3, PollCreateActivity.this.answers.length - 1 - var3);
               PollCreateActivity.this.answers[PollCreateActivity.this.answers.length - 1] = null;
               PollCreateActivity.access$1310(PollCreateActivity.this);
               if (PollCreateActivity.this.answersCount == PollCreateActivity.this.answers.length - 1) {
                  PollCreateActivity.this.listAdapter.notifyItemInserted(PollCreateActivity.this.answerStartRow + PollCreateActivity.this.answers.length - 1);
               }

               var4 = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(var2 - 1);
               if (var4 != null) {
                  var1 = var4.itemView;
                  if (var1 instanceof PollEditTextCell) {
                     ((PollEditTextCell)var1).getTextView().requestFocus();
                  }
               }

               PollCreateActivity.this.checkDoneButton();
               PollCreateActivity.this.updateRows();
               PollCreateActivity.this.listAdapter.notifyItemChanged(PollCreateActivity.this.answerSectionRow);
            }

         }
      }

      // $FF: synthetic method
      public boolean lambda$onCreateViewHolder$1$PollCreateActivity$ListAdapter(PollEditTextCell var1, TextView var2, int var3, KeyEvent var4) {
         if (var3 != 5) {
            return false;
         } else {
            RecyclerView.ViewHolder var8 = PollCreateActivity.this.listView.findContainingViewHolder(var1);
            if (var8 != null) {
               int var5 = var8.getAdapterPosition();
               var3 = var5 - PollCreateActivity.this.answerStartRow;
               if (var3 == PollCreateActivity.this.answersCount - 1 && PollCreateActivity.this.answersCount < 10) {
                  PollCreateActivity.this.addNewField();
               } else if (var3 == PollCreateActivity.this.answersCount - 1) {
                  AndroidUtilities.hideKeyboard(var1.getTextView());
               } else {
                  RecyclerView.ViewHolder var6 = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(var5 + 1);
                  if (var6 != null) {
                     View var7 = var6.itemView;
                     if (var7 instanceof PollEditTextCell) {
                        ((PollEditTextCell)var7).getTextView().requestFocus();
                     }
                  }
               }
            }

            return true;
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 != 2) {
               if (var3 != 3) {
                  PollEditTextCell var4;
                  if (var3 != 4) {
                     if (var3 == 5) {
                        var4 = (PollEditTextCell)var1.itemView;
                        var4.setTag(1);
                        var3 = PollCreateActivity.this.answerStartRow;
                        var4.setTextAndHint(PollCreateActivity.this.answers[var2 - var3], LocaleController.getString("OptionHint", 2131560122), true);
                        var4.setTag((Object)null);
                        if (PollCreateActivity.this.requestFieldFocusAtPosition == var2) {
                           EditTextBoldCursor var9 = var4.getTextView();
                           var9.requestFocus();
                           AndroidUtilities.showKeyboard(var9);
                           PollCreateActivity.this.requestFieldFocusAtPosition = -1;
                        }

                        PollCreateActivity var10 = PollCreateActivity.this;
                        var10.setTextLeft(var1.itemView, var2 - var10.answerStartRow);
                     }
                  } else {
                     var4 = (PollEditTextCell)var1.itemView;
                     var4.setTag(1);
                     String var5;
                     if (PollCreateActivity.this.questionString != null) {
                        var5 = PollCreateActivity.this.questionString;
                     } else {
                        var5 = "";
                     }

                     var4.setTextAndHint(var5, LocaleController.getString("QuestionHint", 2131560522), false);
                     var4.setTag((Object)null);
                  }
               } else {
                  TextSettingsCell var6 = (TextSettingsCell)var1.itemView;
                  var6.setTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                  var6.setText(LocaleController.getString("AddAnOption", 2131558559), false);
               }
            } else {
               TextInfoPrivacyCell var7 = (TextInfoPrivacyCell)var1.itemView;
               var7.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               if (10 - PollCreateActivity.this.answersCount <= 0) {
                  var7.setText(LocaleController.getString("AddAnOptionInfoMax", 2131558561));
               } else {
                  var7.setText(LocaleController.formatString("AddAnOptionInfo", 2131558560, LocaleController.formatPluralString("Option", 10 - PollCreateActivity.this.answersCount)));
               }
            }
         } else {
            HeaderCell var8 = (HeaderCell)var1.itemView;
            if (var2 == PollCreateActivity.this.questionHeaderRow) {
               var8.setText(LocaleController.getString("Question", 2131560521));
            } else if (var2 == PollCreateActivity.this.answerHeaderRow) {
               var8.setText(LocaleController.getString("PollOptions", 2131560468));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         final Object var4;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        var4 = new PollEditTextCell(this.mContext, new _$$Lambda$PollCreateActivity$ListAdapter$xPCTWTnLONqvpxDTUQukvNxp8wU(this)) {
                           protected boolean drawDivider() {
                              RecyclerView.ViewHolder var1 = PollCreateActivity.this.listView.findContainingViewHolder(this);
                              if (var1 != null) {
                                 int var2 = var1.getAdapterPosition();
                                 if (PollCreateActivity.this.answersCount == 10 && var2 == PollCreateActivity.this.answerStartRow + PollCreateActivity.this.answersCount - 1) {
                                    return false;
                                 }
                              }

                              return true;
                           }
                        };
                        ((FrameLayout)var4).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        ((PollEditTextCell)var4).addTextWatcher(new TextWatcher() {
                           public void afterTextChanged(Editable var1) {
                              RecyclerView.ViewHolder var2 = PollCreateActivity.this.listView.findContainingViewHolder((View)var4);
                              if (var2 != null) {
                                 int var3 = var2.getAdapterPosition() - PollCreateActivity.this.answerStartRow;
                                 if (var3 >= 0 && var3 < PollCreateActivity.this.answers.length) {
                                    PollCreateActivity.this.answers[var3] = var1.toString();
                                    PollCreateActivity.this.setTextLeft((View)var4, var3);
                                    PollCreateActivity.this.checkDoneButton();
                                 }
                              }

                           }

                           public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4x) {
                           }

                           public void onTextChanged(CharSequence var1, int var2, int var3, int var4x) {
                           }
                        });
                        ((PollEditTextCell)var4).setShowNextButton(true);
                        EditTextBoldCursor var3 = ((PollEditTextCell)var4).getTextView();
                        var3.setImeOptions(var3.getImeOptions() | 5);
                        var3.setOnEditorActionListener(new _$$Lambda$PollCreateActivity$ListAdapter$fHXG5XRT2mioX4wywMDd12jepYQ(this, (PollEditTextCell)var4));
                        var3.setOnKeyListener(new _$$Lambda$PollCreateActivity$ListAdapter$0Cdu_jo6aZB372n9nZKgtvUqB_g((PollEditTextCell)var4));
                     } else {
                        var4 = new PollEditTextCell(this.mContext, (android.view.View.OnClickListener)null);
                        ((FrameLayout)var4).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        ((PollEditTextCell)var4).addTextWatcher(new TextWatcher() {
                           public void afterTextChanged(Editable var1) {
                              if (((FrameLayout)var4).getTag() == null) {
                                 PollCreateActivity.this.questionString = var1.toString();
                                 RecyclerView.ViewHolder var2 = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(PollCreateActivity.this.questionHeaderRow);
                                 if (var2 != null) {
                                    PollCreateActivity.this.setTextLeft(var2.itemView, -1);
                                 }

                                 PollCreateActivity.this.checkDoneButton();
                              }
                           }

                           public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4x) {
                           }

                           public void onTextChanged(CharSequence var1, int var2, int var3, int var4x) {
                           }
                        });
                     }
                  } else {
                     var4 = new TextSettingsCell(this.mContext);
                     ((View)var4).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var4 = new TextInfoPrivacyCell(this.mContext);
               }
            } else {
               var4 = new ShadowSectionCell(this.mContext);
            }
         } else {
            var4 = new HeaderCell(this.mContext, false, 21, 15, true);
            ((View)var4).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var4);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         if (var1.getItemViewType() == 0) {
            PollCreateActivity var2 = PollCreateActivity.this;
            View var3 = var1.itemView;
            byte var4;
            if (var1.getAdapterPosition() == PollCreateActivity.this.questionHeaderRow) {
               var4 = -1;
            } else {
               var4 = 0;
            }

            var2.setTextLeft(var3, var4);
         }

      }

      public void swapElements(int var1, int var2) {
         int var3 = var1 - PollCreateActivity.this.answerStartRow;
         int var4 = var2 - PollCreateActivity.this.answerStartRow;
         if (var3 >= 0 && var4 >= 0 && var3 < PollCreateActivity.this.answersCount && var4 < PollCreateActivity.this.answersCount) {
            String var5 = PollCreateActivity.this.answers[var3];
            PollCreateActivity.this.answers[var3] = PollCreateActivity.this.answers[var4];
            PollCreateActivity.this.answers[var4] = var5;
            this.notifyItemMoved(var1, var2);
         }

      }
   }

   public interface PollCreateActivityDelegate {
      void sendPoll(TLRPC.TL_messageMediaPoll var1);
   }

   public class TouchHelperCallback extends ItemTouchHelper.Callback {
      public void clearView(RecyclerView var1, RecyclerView.ViewHolder var2) {
         super.clearView(var1, var2);
         var2.itemView.setPressed(false);
      }

      public int getMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2) {
         return var2.getItemViewType() != 5 ? ItemTouchHelper.Callback.makeMovementFlags(0, 0) : ItemTouchHelper.Callback.makeMovementFlags(3, 0);
      }

      public boolean isLongPressDragEnabled() {
         return true;
      }

      public void onChildDraw(Canvas var1, RecyclerView var2, RecyclerView.ViewHolder var3, float var4, float var5, int var6, boolean var7) {
         super.onChildDraw(var1, var2, var3, var4, var5, var6, var7);
      }

      public boolean onMove(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3) {
         if (var2.getItemViewType() != var3.getItemViewType()) {
            return false;
         } else {
            PollCreateActivity.this.listAdapter.swapElements(var2.getAdapterPosition(), var3.getAdapterPosition());
            return true;
         }
      }

      public void onSelectedChanged(RecyclerView.ViewHolder var1, int var2) {
         if (var2 != 0) {
            PollCreateActivity.this.listView.cancelClickRunnables(false);
            var1.itemView.setPressed(true);
         }

         super.onSelectedChanged(var1, var2);
      }

      public void onSwiped(RecyclerView.ViewHolder var1, int var2) {
      }
   }
}
