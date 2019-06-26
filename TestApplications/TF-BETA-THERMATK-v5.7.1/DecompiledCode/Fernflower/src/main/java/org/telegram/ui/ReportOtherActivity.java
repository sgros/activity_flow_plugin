package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class ReportOtherActivity extends BaseFragment {
   private static final int done_button = 1;
   private long dialog_id = this.getArguments().getLong("dialog_id", 0L);
   private View doneButton;
   private EditTextBoldCursor firstNameField;
   private View headerLabelView;
   private int message_id = this.getArguments().getInt("message_id", 0);

   public ReportOtherActivity(Bundle var1) {
      super(var1);
   }

   // $FF: synthetic method
   static int access$300(ReportOtherActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$400(ReportOtherActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static boolean lambda$createView$0(View var0, MotionEvent var1) {
      return true;
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("ReportChat", 2131560568));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         static void lambda$onItemClick$0(TLObject var0, TLRPC.TL_error var1) {
         }

         public void onItemClick(int var1) {
            if (var1 == -1) {
               ReportOtherActivity.this.finishFragment();
            } else if (var1 == 1 && ReportOtherActivity.this.firstNameField.getText().length() != 0) {
               TLRPC.InputPeer var2 = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer((int)ReportOtherActivity.this.dialog_id);
               Object var3;
               TLRPC.TL_inputReportReasonOther var4;
               if (ReportOtherActivity.this.message_id != 0) {
                  var3 = new TLRPC.TL_messages_report();
                  ((TLRPC.TL_messages_report)var3).peer = var2;
                  ((TLRPC.TL_messages_report)var3).id.add(ReportOtherActivity.this.message_id);
                  var4 = new TLRPC.TL_inputReportReasonOther();
                  var4.text = ReportOtherActivity.this.firstNameField.getText().toString();
                  ((TLRPC.TL_messages_report)var3).reason = var4;
               } else {
                  var3 = new TLRPC.TL_account_reportPeer();
                  ((TLRPC.TL_account_reportPeer)var3).peer = MessagesController.getInstance(ReportOtherActivity.access$300(ReportOtherActivity.this)).getInputPeer((int)ReportOtherActivity.this.dialog_id);
                  var4 = new TLRPC.TL_inputReportReasonOther();
                  var4.text = ReportOtherActivity.this.firstNameField.getText().toString();
                  ((TLRPC.TL_account_reportPeer)var3).reason = var4;
               }

               ConnectionsManager.getInstance(ReportOtherActivity.access$400(ReportOtherActivity.this)).sendRequest((TLObject)var3, _$$Lambda$ReportOtherActivity$1$PbLFyQbNnsMkC_qS1TkzMcffkwA.INSTANCE);
               if (ReportOtherActivity.this.getParentActivity() != null) {
                  Toast.makeText(ReportOtherActivity.this.getParentActivity(), LocaleController.getString("ReportChatSent", 2131560573), 0).show();
               }

               ReportOtherActivity.this.finishFragment();
            }

         }
      });
      this.doneButton = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
      LinearLayout var2 = new LinearLayout(var1);
      super.fragmentView = var2;
      super.fragmentView.setLayoutParams(new LayoutParams(-1, -1));
      ((LinearLayout)super.fragmentView).setOrientation(1);
      super.fragmentView.setOnTouchListener(_$$Lambda$ReportOtherActivity$VcwTn_4nik4XOSC4IbcsIN4IckE.INSTANCE);
      this.firstNameField = new EditTextBoldCursor(var1);
      this.firstNameField.setTextSize(1, 18.0F);
      this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(var1, false));
      EditTextBoldCursor var5 = this.firstNameField;
      byte var3 = 3;
      var5.setMaxLines(3);
      this.firstNameField.setPadding(0, 0, 0, 0);
      var5 = this.firstNameField;
      byte var4;
      if (LocaleController.isRTL) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var5.setGravity(var4);
      this.firstNameField.setInputType(180224);
      this.firstNameField.setImeOptions(6);
      var5 = this.firstNameField;
      var4 = var3;
      if (LocaleController.isRTL) {
         var4 = 5;
      }

      var5.setGravity(var4);
      this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0F));
      this.firstNameField.setCursorWidth(1.5F);
      this.firstNameField.setOnEditorActionListener(new _$$Lambda$ReportOtherActivity$JRCa_EXPGvX6N9BVVFwqPlLZM80(this));
      var2.addView(this.firstNameField, LayoutHelper.createLinear(-1, 36, 24.0F, 24.0F, 24.0F, 0.0F));
      this.firstNameField.setHint(LocaleController.getString("ReportChatDescription", 2131560570));
      var5 = this.firstNameField;
      var5.setSelection(var5.length());
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated")};
   }

   // $FF: synthetic method
   public boolean lambda$createView$1$ReportOtherActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         View var4 = this.doneButton;
         if (var4 != null) {
            var4.performClick();
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$onTransitionAnimationEnd$2$ReportOtherActivity() {
      EditTextBoldCursor var1 = this.firstNameField;
      if (var1 != null) {
         var1.requestFocus();
         AndroidUtilities.showKeyboard(this.firstNameField);
      }

   }

   public void onResume() {
      super.onResume();
      if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
         this.firstNameField.requestFocus();
         AndroidUtilities.showKeyboard(this.firstNameField);
      }

   }

   public void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ReportOtherActivity$djrT4sV5rD__jM1owBrq9EJKfOk(this), 100L);
      }

   }
}
