package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.AudioPlayerCell;

public class AudioPlayerAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate, DownloadController.FileDownloadProgressListener {
   private int TAG;
   private ActionBar actionBar;
   private AnimatorSet actionBarAnimation;
   private AnimatorSet animatorSet;
   private TextView authorTextView;
   private ChatAvatarContainer avatarContainer;
   private View[] buttons = new View[5];
   private TextView durationTextView;
   private float endTranslation;
   private float fullAnimationProgress;
   private int hasNoCover;
   private boolean hasOptions = true;
   private boolean inFullSize;
   private boolean isInFullMode;
   private int lastTime;
   private LinearLayoutManager layoutManager;
   private AudioPlayerAlert.ListAdapter listAdapter;
   private RecyclerListView listView;
   private ActionBarMenuItem menuItem;
   private Drawable noCoverDrawable;
   private ActionBarMenuItem optionsButton;
   private Paint paint = new Paint(1);
   private float panelEndTranslation;
   private float panelStartTranslation;
   private LaunchActivity parentActivity;
   private BackupImageView placeholderImageView;
   private ImageView playButton;
   private Drawable[] playOrderButtons = new Drawable[2];
   private FrameLayout playerLayout;
   private ArrayList playlist;
   private LineProgressView progressView;
   private ImageView repeatButton;
   private int scrollOffsetY = Integer.MAX_VALUE;
   private boolean scrollToSong = true;
   private ActionBarMenuItem searchItem;
   private int searchOpenOffset;
   private int searchOpenPosition = -1;
   private boolean searchWas;
   private boolean searching;
   private SeekBarView seekBarView;
   private View shadow;
   private View shadow2;
   private Drawable shadowDrawable;
   private ActionBarMenuItem shuffleButton;
   private float startTranslation;
   private float thumbMaxScale;
   private int thumbMaxX;
   private int thumbMaxY;
   private SimpleTextView timeTextView;
   private TextView titleTextView;
   private int topBeforeSwitch;

   public AudioPlayerAlert(Context var1) {
      super(var1, true, 0);
      MessageObject var2 = MediaController.getInstance().getPlayingMessageObject();
      if (var2 != null) {
         super.currentAccount = var2.currentAccount;
      } else {
         super.currentAccount = UserConfig.selectedAccount;
      }

      this.parentActivity = (LaunchActivity)var1;
      this.noCoverDrawable = var1.getResources().getDrawable(2131165694).mutate();
      this.noCoverDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_placeholder"), Mode.MULTIPLY));
      this.TAG = DownloadController.getInstance(super.currentAccount).generateObserverTag();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.musicDidLoad);
      this.shadowDrawable = var1.getResources().getDrawable(2131165823).mutate();
      this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_background"), Mode.MULTIPLY));
      this.paint.setColor(Theme.getColor("player_placeholderBackground"));
      super.containerView = new FrameLayout(var1) {
         private boolean ignoreLayout = false;

         protected void onDraw(Canvas var1) {
            AudioPlayerAlert.this.shadowDrawable.setBounds(0, Math.max(AudioPlayerAlert.this.actionBar.getMeasuredHeight(), AudioPlayerAlert.this.scrollOffsetY) - AudioPlayerAlert.access$1700(AudioPlayerAlert.this), this.getMeasuredWidth(), this.getMeasuredHeight());
            AudioPlayerAlert.this.shadowDrawable.draw(var1);
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0 && AudioPlayerAlert.this.scrollOffsetY != 0 && var1.getY() < (float)AudioPlayerAlert.this.scrollOffsetY && AudioPlayerAlert.this.placeholderImageView.getTranslationX() == 0.0F) {
               AudioPlayerAlert.this.dismiss();
               return true;
            } else {
               return super.onInterceptTouchEvent(var1);
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            var2 = AudioPlayerAlert.this.actionBar.getMeasuredHeight();
            AudioPlayerAlert.this.shadow.layout(AudioPlayerAlert.this.shadow.getLeft(), var2, AudioPlayerAlert.this.shadow.getRight(), AudioPlayerAlert.this.shadow.getMeasuredHeight() + var2);
            AudioPlayerAlert.this.updateLayout();
            AudioPlayerAlert var6 = AudioPlayerAlert.this;
            var6.setFullAnimationProgress(var6.fullAnimationProgress);
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var2);
            var2 = AndroidUtilities.dp(178.0F) + AudioPlayerAlert.this.playlist.size() * AndroidUtilities.dp(56.0F) + AudioPlayerAlert.access$300(AudioPlayerAlert.this) + ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight;
            int var4 = MeasureSpec.makeMeasureSpec(var3, 1073741824);
            boolean var5 = AudioPlayerAlert.this.searching;
            byte var6 = 0;
            int var7;
            if (var5) {
               var7 = AndroidUtilities.dp(178.0F) + ActionBar.getCurrentActionBarHeight();
               if (VERSION.SDK_INT >= 21) {
                  var2 = AndroidUtilities.statusBarHeight;
               } else {
                  var2 = 0;
               }
            } else {
               if (var2 < var3) {
                  var2 = var3 - var2;
               } else if (var2 < var3) {
                  var2 = 0;
               } else {
                  var2 = var3 - var3 / 5 * 3;
               }

               int var8 = ActionBar.getCurrentActionBarHeight();
               if (VERSION.SDK_INT >= 21) {
                  var7 = AndroidUtilities.statusBarHeight;
               } else {
                  var7 = 0;
               }

               var8 += var7;
               var7 = var2;
               var2 = var8;
            }

            var2 += var7;
            var7 = AudioPlayerAlert.this.listView.getPaddingTop();
            var5 = true;
            if (var7 != var2) {
               this.ignoreLayout = true;
               AudioPlayerAlert.this.listView.setPadding(0, var2, 0, AndroidUtilities.dp(8.0F));
               this.ignoreLayout = false;
            }

            super.onMeasure(var1, var4);
            AudioPlayerAlert var9 = AudioPlayerAlert.this;
            if (this.getMeasuredHeight() < var3) {
               var5 = false;
            }

            var9.inFullSize = var5;
            var2 = ActionBar.getCurrentActionBarHeight();
            var1 = var6;
            if (VERSION.SDK_INT >= 21) {
               var1 = AndroidUtilities.statusBarHeight;
            }

            var1 = var3 - var2 - var1 - AndroidUtilities.dp(120.0F);
            var2 = Math.max(var1, this.getMeasuredWidth());
            AudioPlayerAlert.this.thumbMaxX = (this.getMeasuredWidth() - var2) / 2 - AndroidUtilities.dp(17.0F);
            AudioPlayerAlert.this.thumbMaxY = AndroidUtilities.dp(19.0F);
            AudioPlayerAlert.this.panelEndTranslation = (float)(this.getMeasuredHeight() - AudioPlayerAlert.this.playerLayout.getMeasuredHeight());
            var9 = AudioPlayerAlert.this;
            var9.thumbMaxScale = (float)var2 / (float)var9.placeholderImageView.getMeasuredWidth() - 1.0F;
            AudioPlayerAlert.this.endTranslation = (float)(ActionBar.getCurrentActionBarHeight() + (AndroidUtilities.statusBarHeight - AndroidUtilities.dp(19.0F)));
            var2 = (int)Math.ceil((double)((float)AudioPlayerAlert.this.placeholderImageView.getMeasuredHeight() * (AudioPlayerAlert.this.thumbMaxScale + 1.0F)));
            if (var2 > var1) {
               var9 = AudioPlayerAlert.this;
               var9.endTranslation = var9.endTranslation - (float)(var2 - var1);
            }

         }

         public boolean onTouchEvent(MotionEvent var1) {
            boolean var2;
            if (!AudioPlayerAlert.this.isDismissed() && super.onTouchEvent(var1)) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }

         public void requestLayout() {
            if (!this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      super.containerView.setWillNotDraw(false);
      ViewGroup var3 = super.containerView;
      int var4 = super.backgroundPaddingLeft;
      var3.setPadding(var4, 0, var4, 0);
      this.actionBar = new ActionBar(var1);
      this.actionBar.setBackgroundColor(Theme.getColor("player_actionBar"));
      this.actionBar.setBackButtonImage(2131165409);
      this.actionBar.setItemsColor(Theme.getColor("player_actionBarItems"), false);
      this.actionBar.setItemsBackgroundColor(Theme.getColor("player_actionBarSelector"), false);
      this.actionBar.setTitleColor(Theme.getColor("player_actionBarTitle"));
      this.actionBar.setSubtitleColor(Theme.getColor("player_actionBarSubtitle"));
      this.actionBar.setAlpha(0.0F);
      this.actionBar.setTitle("1");
      this.actionBar.setSubtitle("1");
      this.actionBar.getTitleTextView().setAlpha(0.0F);
      this.actionBar.getSubtitleTextView().setAlpha(0.0F);
      this.avatarContainer = new ChatAvatarContainer(var1, (ChatActivity)null, false);
      this.avatarContainer.setEnabled(false);
      this.avatarContainer.setTitleColors(Theme.getColor("player_actionBarTitle"), Theme.getColor("player_actionBarSubtitle"));
      if (var2 != null) {
         long var5 = var2.getDialogId();
         int var7 = (int)var5;
         var4 = (int)(var5 >> 32);
         TLRPC.User var12;
         if (var7 != 0) {
            if (var7 > 0) {
               var12 = MessagesController.getInstance(super.currentAccount).getUser(var7);
               if (var12 != null) {
                  this.avatarContainer.setTitle(ContactsController.formatName(var12.first_name, var12.last_name));
                  this.avatarContainer.setUserAvatar(var12);
               }
            } else {
               TLRPC.Chat var14 = MessagesController.getInstance(super.currentAccount).getChat(-var7);
               if (var14 != null) {
                  this.avatarContainer.setTitle(var14.title);
                  this.avatarContainer.setChatAvatar(var14);
               }
            }
         } else {
            TLRPC.EncryptedChat var15 = MessagesController.getInstance(super.currentAccount).getEncryptedChat(var4);
            if (var15 != null) {
               var12 = MessagesController.getInstance(super.currentAccount).getUser(var15.user_id);
               if (var12 != null) {
                  this.avatarContainer.setTitle(ContactsController.formatName(var12.first_name, var12.last_name));
                  this.avatarContainer.setUserAvatar(var12);
               }
            }
         }
      }

      this.avatarContainer.setSubtitle(LocaleController.getString("AudioTitle", 2131558736));
      this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0F, 51, 56.0F, 0.0F, 40.0F, 0.0F));
      ActionBarMenu var16 = this.actionBar.createMenu();
      var16.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
      this.menuItem = var16.addItem(0, 2131165416);
      this.menuItem.addSubItem(1, 2131165627, LocaleController.getString("Forward", 2131559504));
      this.menuItem.addSubItem(2, 2131165671, LocaleController.getString("ShareFile", 2131560748));
      this.menuItem.addSubItem(4, 2131165647, LocaleController.getString("ShowInChat", 2131560780));
      this.menuItem.setTranslationX((float)AndroidUtilities.dp(48.0F));
      this.menuItem.setAlpha(0.0F);
      this.searchItem = var16.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            AudioPlayerAlert.this.avatarContainer.setVisibility(0);
            if (AudioPlayerAlert.this.hasOptions) {
               AudioPlayerAlert.this.menuItem.setVisibility(4);
            }

            if (AudioPlayerAlert.this.searching) {
               AudioPlayerAlert.this.searchWas = false;
               AudioPlayerAlert.this.searching = false;
               AudioPlayerAlert.this.setAllowNestedScroll(true);
               AudioPlayerAlert.this.listAdapter.search((String)null);
            }

         }

         public void onSearchExpand() {
            AudioPlayerAlert var1 = AudioPlayerAlert.this;
            var1.searchOpenPosition = var1.layoutManager.findLastVisibleItemPosition();
            View var2 = AudioPlayerAlert.this.layoutManager.findViewByPosition(AudioPlayerAlert.this.searchOpenPosition);
            var1 = AudioPlayerAlert.this;
            int var3;
            if (var2 == null) {
               var3 = 0;
            } else {
               var3 = var2.getTop();
            }

            var1.searchOpenOffset = var3 - AudioPlayerAlert.this.listView.getPaddingTop();
            AudioPlayerAlert.this.avatarContainer.setVisibility(8);
            if (AudioPlayerAlert.this.hasOptions) {
               AudioPlayerAlert.this.menuItem.setVisibility(8);
            }

            AudioPlayerAlert.this.searching = true;
            AudioPlayerAlert.this.setAllowNestedScroll(false);
            AudioPlayerAlert.this.listAdapter.notifyDataSetChanged();
         }

         public void onTextChanged(EditText var1) {
            if (var1.length() > 0) {
               AudioPlayerAlert.this.listAdapter.search(var1.getText().toString());
            } else {
               AudioPlayerAlert.this.searchWas = false;
               AudioPlayerAlert.this.listAdapter.search((String)null);
            }

         }
      });
      this.searchItem.setContentDescription(LocaleController.getString("Search", 2131560640));
      EditTextBoldCursor var18 = this.searchItem.getSearchField();
      var18.setHint(LocaleController.getString("Search", 2131560640));
      var18.setTextColor(Theme.getColor("player_actionBarTitle"));
      var18.setHintTextColor(Theme.getColor("player_time"));
      var18.setCursorColor(Theme.getColor("player_actionBarTitle"));
      if (!AndroidUtilities.isTablet()) {
         this.actionBar.showActionModeTop();
         this.actionBar.setActionModeTopColor(Theme.getColor("player_actionBarTop"));
      }

      this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               AudioPlayerAlert.this.dismiss();
            } else {
               AudioPlayerAlert.this.onSubItemClick(var1);
            }

         }
      });
      this.shadow = new View(var1);
      this.shadow.setAlpha(0.0F);
      this.shadow.setBackgroundResource(2131165407);
      this.shadow2 = new View(var1);
      this.shadow2.setAlpha(0.0F);
      this.shadow2.setBackgroundResource(2131165407);
      this.playerLayout = new FrameLayout(var1);
      this.playerLayout.setBackgroundColor(Theme.getColor("player_background"));
      this.placeholderImageView = new BackupImageView(var1) {
         private RectF rect = new RectF();

         protected void onDraw(Canvas var1) {
            if (AudioPlayerAlert.this.hasNoCover == 1 || AudioPlayerAlert.this.hasNoCover == 2 && (!this.getImageReceiver().hasBitmapImage() || this.getImageReceiver().getCurrentAlpha() != 1.0F)) {
               this.rect.set(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
               var1.drawRoundRect(this.rect, (float)this.getRoundRadius(), (float)this.getRoundRadius(), AudioPlayerAlert.this.paint);
               float var2 = AudioPlayerAlert.this.thumbMaxScale / this.getScaleX() / 3.0F;
               int var3 = (int)((float)AndroidUtilities.dp(63.0F) * Math.max(var2 / AudioPlayerAlert.this.thumbMaxScale, 1.0F / AudioPlayerAlert.this.thumbMaxScale));
               float var4 = this.rect.centerX();
               var2 = (float)(var3 / 2);
               int var5 = (int)(var4 - var2);
               int var6 = (int)(this.rect.centerY() - var2);
               AudioPlayerAlert.this.noCoverDrawable.setBounds(var5, var6, var5 + var3, var3 + var6);
               AudioPlayerAlert.this.noCoverDrawable.draw(var1);
            }

            if (AudioPlayerAlert.this.hasNoCover != 1) {
               super.onDraw(var1);
            }

         }
      };
      this.placeholderImageView.setRoundRadius(AndroidUtilities.dp(20.0F));
      this.placeholderImageView.setPivotX(0.0F);
      this.placeholderImageView.setPivotY(0.0F);
      this.placeholderImageView.setOnClickListener(new _$$Lambda$AudioPlayerAlert$rAc8YAca3eZCLAfei_UTWtnjvbQ(this));
      this.titleTextView = new TextView(var1);
      this.titleTextView.setTextColor(Theme.getColor("player_actionBarTitle"));
      this.titleTextView.setTextSize(1, 15.0F);
      this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.titleTextView.setEllipsize(TruncateAt.END);
      this.titleTextView.setSingleLine(true);
      this.playerLayout.addView(this.titleTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 72.0F, 18.0F, 60.0F, 0.0F));
      this.authorTextView = new TextView(var1);
      this.authorTextView.setTextColor(Theme.getColor("player_time"));
      this.authorTextView.setTextSize(1, 14.0F);
      this.authorTextView.setEllipsize(TruncateAt.END);
      this.authorTextView.setSingleLine(true);
      this.playerLayout.addView(this.authorTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 72.0F, 40.0F, 60.0F, 0.0F));
      this.optionsButton = new ActionBarMenuItem(var1, (ActionBarMenu)null, 0, Theme.getColor("player_actionBarItems"));
      this.optionsButton.setLongClickEnabled(false);
      this.optionsButton.setIcon(2131165416);
      this.optionsButton.setAdditionalOffset(-AndroidUtilities.dp(120.0F));
      this.playerLayout.addView(this.optionsButton, LayoutHelper.createFrame(40, 40.0F, 53, 0.0F, 19.0F, 10.0F, 0.0F));
      this.optionsButton.addSubItem(1, 2131165627, LocaleController.getString("Forward", 2131559504));
      this.optionsButton.addSubItem(2, 2131165671, LocaleController.getString("ShareFile", 2131560748));
      this.optionsButton.addSubItem(4, 2131165647, LocaleController.getString("ShowInChat", 2131560780));
      this.optionsButton.setOnClickListener(new _$$Lambda$AudioPlayerAlert$L6a3eFAcdzUb5tmijbnBD0GiP9c(this));
      this.optionsButton.setDelegate(new _$$Lambda$AudioPlayerAlert$2le4yIg27yXBU0W_K7geAc_qlH0(this));
      this.optionsButton.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
      this.seekBarView = new SeekBarView(var1);
      this.seekBarView.setDelegate(_$$Lambda$AudioPlayerAlert$pQ_5LDOKmp9BWnrwt0sO_Br_q0g.INSTANCE);
      this.playerLayout.addView(this.seekBarView, LayoutHelper.createFrame(-1, 30.0F, 51, 8.0F, 62.0F, 8.0F, 0.0F));
      this.progressView = new LineProgressView(var1);
      this.progressView.setVisibility(4);
      this.progressView.setBackgroundColor(Theme.getColor("player_progressBackground"));
      this.progressView.setProgressColor(Theme.getColor("player_progress"));
      this.playerLayout.addView(this.progressView, LayoutHelper.createFrame(-1, 2.0F, 51, 20.0F, 78.0F, 20.0F, 0.0F));
      this.timeTextView = new SimpleTextView(var1);
      this.timeTextView.setTextSize(12);
      this.timeTextView.setText("0:00");
      this.timeTextView.setTextColor(Theme.getColor("player_time"));
      this.playerLayout.addView(this.timeTextView, LayoutHelper.createFrame(100, -2.0F, 51, 20.0F, 92.0F, 0.0F, 0.0F));
      this.durationTextView = new TextView(var1);
      this.durationTextView.setTextSize(1, 12.0F);
      this.durationTextView.setTextColor(Theme.getColor("player_time"));
      this.durationTextView.setGravity(17);
      this.playerLayout.addView(this.durationTextView, LayoutHelper.createFrame(-2, -2.0F, 53, 0.0F, 90.0F, 20.0F, 0.0F));
      FrameLayout var19 = new FrameLayout(var1) {
         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            var3 = (var4 - var2 - AndroidUtilities.dp(248.0F)) / 4;

            for(var2 = 0; var2 < 5; ++var2) {
               var4 = AndroidUtilities.dp((float)(var2 * 48 + 4)) + var3 * var2;
               var5 = AndroidUtilities.dp(9.0F);
               AudioPlayerAlert.this.buttons[var2].layout(var4, var5, AudioPlayerAlert.this.buttons[var2].getMeasuredWidth() + var4, AudioPlayerAlert.this.buttons[var2].getMeasuredHeight() + var5);
            }

         }
      };
      this.playerLayout.addView(var19, LayoutHelper.createFrame(-1, 66.0F, 51, 0.0F, 106.0F, 0.0F, 0.0F));
      View[] var10 = this.buttons;
      ActionBarMenuItem var8 = new ActionBarMenuItem(var1, (ActionBarMenu)null, 0, 0);
      this.shuffleButton = var8;
      var10[0] = var8;
      this.shuffleButton.setLongClickEnabled(false);
      this.shuffleButton.setAdditionalOffset(-AndroidUtilities.dp(10.0F));
      var19.addView(this.shuffleButton, LayoutHelper.createFrame(48, 48, 51));
      this.shuffleButton.setOnClickListener(new _$$Lambda$AudioPlayerAlert$vDsQnotDAAYl8VqVL3Roz4WO8JM(this));
      TextView var11 = this.shuffleButton.addSubItem(1, LocaleController.getString("ReverseOrder", 2131560616));
      var11.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(16.0F), 0);
      this.playOrderButtons[0] = var1.getResources().getDrawable(2131165684).mutate();
      var11.setCompoundDrawablePadding(AndroidUtilities.dp(8.0F));
      var11.setCompoundDrawablesWithIntrinsicBounds(this.playOrderButtons[0], (Drawable)null, (Drawable)null, (Drawable)null);
      var11 = this.shuffleButton.addSubItem(2, LocaleController.getString("Shuffle", 2131560784));
      var11.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(16.0F), 0);
      this.playOrderButtons[1] = var1.getResources().getDrawable(2131165769).mutate();
      var11.setCompoundDrawablePadding(AndroidUtilities.dp(8.0F));
      var11.setCompoundDrawablesWithIntrinsicBounds(this.playOrderButtons[1], (Drawable)null, (Drawable)null, (Drawable)null);
      this.shuffleButton.setDelegate(new _$$Lambda$AudioPlayerAlert$HopcJEXECX0ZxGGa4UewdstQ_LI(this));
      var10 = this.buttons;
      ImageView var21 = new ImageView(var1);
      var10[1] = var21;
      var21.setScaleType(ScaleType.CENTER);
      var21.setImageDrawable(Theme.createSimpleSelectorDrawable(var1, 2131165766, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
      var19.addView(var21, LayoutHelper.createFrame(48, 48, 51));
      var21.setOnClickListener(_$$Lambda$AudioPlayerAlert$_25s4XwmMXC5vKIS0zNvR6jdxfg.INSTANCE);
      var21.setContentDescription(LocaleController.getString("AccDescrPrevious", 2131558459));
      View[] var22 = this.buttons;
      ImageView var13 = new ImageView(var1);
      this.playButton = var13;
      var22[2] = var13;
      this.playButton.setScaleType(ScaleType.CENTER);
      this.playButton.setImageDrawable(Theme.createSimpleSelectorDrawable(var1, 2131165765, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
      var19.addView(this.playButton, LayoutHelper.createFrame(48, 48, 51));
      this.playButton.setOnClickListener(_$$Lambda$AudioPlayerAlert$TTY43GWhuW3FlUWxplw8ac_chYU.INSTANCE);
      var22 = this.buttons;
      var13 = new ImageView(var1);
      var22[3] = var13;
      var13.setScaleType(ScaleType.CENTER);
      var13.setImageDrawable(Theme.createSimpleSelectorDrawable(var1, 2131165763, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
      var19.addView(var13, LayoutHelper.createFrame(48, 48, 51));
      var13.setOnClickListener(_$$Lambda$AudioPlayerAlert$4X94PKs6A_0UhkXhWsjRM6iEc3I.INSTANCE);
      var13.setContentDescription(LocaleController.getString("Next", 2131559911));
      var10 = this.buttons;
      var21 = new ImageView(var1);
      this.repeatButton = var21;
      var10[4] = var21;
      this.repeatButton.setScaleType(ScaleType.CENTER);
      this.repeatButton.setPadding(0, 0, AndroidUtilities.dp(8.0F), 0);
      var19.addView(this.repeatButton, LayoutHelper.createFrame(50, 48, 51));
      this.repeatButton.setOnClickListener(new _$$Lambda$AudioPlayerAlert$dXFO6JRbR1ZdMBRfhd2XydhikZ0(this));
      this.listView = new RecyclerListView(var1) {
         boolean ignoreLayout;

         protected boolean allowSelectChildAtPosition(float var1, float var2) {
            boolean var3;
            if (AudioPlayerAlert.this.playerLayout != null && var2 <= AudioPlayerAlert.this.playerLayout.getY() + (float)AudioPlayerAlert.this.playerLayout.getMeasuredHeight()) {
               var3 = false;
            } else {
               var3 = true;
            }

            return var3;
         }

         public boolean drawChild(Canvas var1, View var2, long var3) {
            var1.save();
            int var5;
            if (AudioPlayerAlert.this.actionBar != null) {
               var5 = AudioPlayerAlert.this.actionBar.getMeasuredHeight();
            } else {
               var5 = 0;
            }

            var1.clipRect(0, var5 + AndroidUtilities.dp(50.0F), this.getMeasuredWidth(), this.getMeasuredHeight());
            boolean var6 = super.drawChild(var1, var2, var3);
            var1.restore();
            return var6;
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if (AudioPlayerAlert.this.searchOpenPosition != -1 && !AudioPlayerAlert.this.actionBar.isSearchFieldVisible()) {
               this.ignoreLayout = true;
               AudioPlayerAlert.this.layoutManager.scrollToPositionWithOffset(AudioPlayerAlert.this.searchOpenPosition, AudioPlayerAlert.this.searchOpenOffset);
               super.onLayout(false, var2, var3, var4, var5);
               this.ignoreLayout = false;
               AudioPlayerAlert.this.searchOpenPosition = -1;
            } else if (AudioPlayerAlert.this.scrollToSong) {
               AudioPlayerAlert.this.scrollToSong = false;
               MessageObject var6 = MediaController.getInstance().getPlayingMessageObject();
               if (var6 != null) {
                  int var7 = AudioPlayerAlert.this.listView.getChildCount();

                  int var8;
                  boolean var10;
                  label37: {
                     for(var8 = 0; var8 < var7; ++var8) {
                        View var9 = AudioPlayerAlert.this.listView.getChildAt(var8);
                        if (var9 instanceof AudioPlayerCell && ((AudioPlayerCell)var9).getMessageObject() == var6) {
                           if (var9.getBottom() <= this.getMeasuredHeight()) {
                              var10 = true;
                              break label37;
                           }
                           break;
                        }
                     }

                     var10 = false;
                  }

                  if (!var10) {
                     var8 = AudioPlayerAlert.this.playlist.indexOf(var6);
                     if (var8 >= 0) {
                        this.ignoreLayout = true;
                        if (SharedConfig.playOrderReversed) {
                           AudioPlayerAlert.this.layoutManager.scrollToPosition(var8);
                        } else {
                           AudioPlayerAlert.this.layoutManager.scrollToPosition(AudioPlayerAlert.this.playlist.size() - var8);
                        }

                        super.onLayout(false, var2, var3, var4, var5);
                        this.ignoreLayout = false;
                     }
                  }
               }
            }

         }

         public void requestLayout() {
            if (!this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(8.0F));
      this.listView.setClipToPadding(false);
      RecyclerListView var20 = this.listView;
      LinearLayoutManager var17 = new LinearLayoutManager(this.getContext(), 1, false);
      this.layoutManager = var17;
      var20.setLayoutManager(var17);
      this.listView.setHorizontalScrollBarEnabled(false);
      this.listView.setVerticalScrollBarEnabled(false);
      super.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      var20 = this.listView;
      AudioPlayerAlert.ListAdapter var9 = new AudioPlayerAlert.ListAdapter(var1);
      this.listAdapter = var9;
      var20.setAdapter(var9);
      this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)_$$Lambda$AudioPlayerAlert$48MEuBkln_g5kGMLm89eP_Rsutc.INSTANCE);
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1 && AudioPlayerAlert.this.searching && AudioPlayerAlert.this.searchWas) {
               AndroidUtilities.hideKeyboard(AudioPlayerAlert.this.getCurrentFocus());
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            AudioPlayerAlert.this.updateLayout();
         }
      });
      this.playlist = MediaController.getInstance().getPlaylist();
      this.listAdapter.notifyDataSetChanged();
      super.containerView.addView(this.playerLayout, LayoutHelper.createFrame(-1, 178.0F));
      super.containerView.addView(this.shadow2, LayoutHelper.createFrame(-1, 3.0F));
      super.containerView.addView(this.placeholderImageView, LayoutHelper.createFrame(40, 40.0F, 51, 17.0F, 19.0F, 0.0F, 0.0F));
      super.containerView.addView(this.shadow, LayoutHelper.createFrame(-1, 3.0F));
      super.containerView.addView(this.actionBar);
      this.updateTitle(false);
      this.updateRepeatButton();
      this.updateShuffleButton();
   }

   // $FF: synthetic method
   static int access$1700(AudioPlayerAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$300(AudioPlayerAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static AnimatorSet access$3500(AudioPlayerAlert var0) {
      return var0.animatorSet;
   }

   // $FF: synthetic method
   static AnimatorSet access$3502(AudioPlayerAlert var0, AnimatorSet var1) {
      var0.animatorSet = var1;
      return var1;
   }

   // $FF: synthetic method
   static boolean access$3600(AudioPlayerAlert var0) {
      return var0.isInFullMode;
   }

   // $FF: synthetic method
   static ActionBarMenuItem access$3700(AudioPlayerAlert var0) {
      return var0.searchItem;
   }

   private void checkIfMusicDownloaded(MessageObject var1) {
      String var2 = var1.messageOwner.attachPath;
      File var3 = null;
      File var4 = var3;
      if (var2 != null) {
         var4 = var3;
         if (var2.length() > 0) {
            var4 = new File(var1.messageOwner.attachPath);
            if (!var4.exists()) {
               var4 = var3;
            }
         }
      }

      var3 = var4;
      if (var4 == null) {
         var3 = FileLoader.getPathToMessage(var1.messageOwner);
      }

      boolean var5;
      if (SharedConfig.streamMedia && (int)var1.getDialogId() != 0 && var1.isMusic()) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (!var3.exists() && !var5) {
         String var7 = var1.getFileName();
         DownloadController.getInstance(super.currentAccount).addLoadingFileObserver(var7, this);
         Float var8 = ImageLoader.getInstance().getFileProgress(var7);
         LineProgressView var9 = this.progressView;
         float var6;
         if (var8 != null) {
            var6 = var8;
         } else {
            var6 = 0.0F;
         }

         var9.setProgress(var6, false);
         this.progressView.setVisibility(0);
         this.seekBarView.setVisibility(4);
         this.playButton.setEnabled(false);
      } else {
         DownloadController.getInstance(super.currentAccount).removeLoadingFileObserver(this);
         this.progressView.setVisibility(4);
         this.seekBarView.setVisibility(0);
         this.playButton.setEnabled(true);
      }

   }

   private int getCurrentTop() {
      if (this.listView.getChildCount() != 0) {
         RecyclerListView var1 = this.listView;
         byte var2 = 0;
         View var3 = var1.getChildAt(0);
         RecyclerListView.Holder var6 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var3);
         if (var6 != null) {
            int var4 = this.listView.getPaddingTop();
            int var5 = var2;
            if (var6.getAdapterPosition() == 0) {
               var5 = var2;
               if (var3.getTop() >= 0) {
                  var5 = var3.getTop();
               }
            }

            return var4 - var5;
         }
      }

      return -1000;
   }

   // $FF: synthetic method
   public static void lambda$2le4yIg27yXBU0W_K7geAc_qlH0/* $FF was: lambda$2le4yIg27yXBU0W-K7geAc_qlH0*/(AudioPlayerAlert var0, int var1) {
      var0.onSubItemClick(var1);
   }

   // $FF: synthetic method
   static void lambda$new$2(float var0) {
      MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), var0);
   }

   // $FF: synthetic method
   static void lambda$new$5(View var0) {
      MediaController.getInstance().playPreviousMessage();
   }

   // $FF: synthetic method
   static void lambda$new$6(View var0) {
      if (!MediaController.getInstance().isDownloadingCurrentMessage()) {
         if (MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
         } else {
            MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
         }

      }
   }

   // $FF: synthetic method
   static void lambda$new$7(View var0) {
      MediaController.getInstance().playNextMessage();
   }

   // $FF: synthetic method
   static void lambda$new$9(View var0, int var1) {
      if (var0 instanceof AudioPlayerCell) {
         ((AudioPlayerCell)var0).didPressedButton();
      }

   }

   private void onSubItemClick(int var1) {
      MessageObject var2 = MediaController.getInstance().getPlayingMessageObject();
      if (var2 != null) {
         LaunchActivity var3 = this.parentActivity;
         if (var3 != null) {
            int var4;
            if (var1 == 1) {
               var4 = UserConfig.selectedAccount;
               var1 = super.currentAccount;
               if (var4 != var1) {
                  var3.switchToAccount(var1, true);
               }

               Bundle var20 = new Bundle();
               var20.putBoolean("onlySelect", true);
               var20.putInt("dialogsType", 3);
               DialogsActivity var5 = new DialogsActivity(var20);
               ArrayList var21 = new ArrayList();
               var21.add(var2);
               var5.setDelegate(new _$$Lambda$AudioPlayerAlert$bZwv44or3_083Y0MYIMNRo1ORcQ(this, var21));
               this.parentActivity.presentFragment(var5);
               this.dismiss();
            } else if (var1 == 2) {
               Exception var10000;
               label142: {
                  boolean var10001;
                  File var22;
                  File var25;
                  label134: {
                     label143: {
                        try {
                           if (TextUtils.isEmpty(var2.messageOwner.attachPath)) {
                              break label143;
                           }

                           var25 = new File(var2.messageOwner.attachPath);
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label142;
                        }

                        var22 = var25;

                        try {
                           if (var25.exists()) {
                              break label134;
                           }
                        } catch (Exception var18) {
                           var10000 = var18;
                           var10001 = false;
                           break label142;
                        }
                     }

                     var22 = null;
                  }

                  var25 = var22;
                  if (var22 == null) {
                     try {
                        var25 = FileLoader.getPathToMessage(var2.messageOwner);
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label142;
                     }
                  }

                  label144: {
                     Intent var23;
                     try {
                        if (!var25.exists()) {
                           break label144;
                        }

                        var23 = new Intent("android.intent.action.SEND");
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label142;
                     }

                     if (var2 != null) {
                        try {
                           var23.setType(var2.getMimeType());
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break label142;
                        }
                     } else {
                        try {
                           var23.setType("audio/mp3");
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label142;
                        }
                     }

                     try {
                        var1 = VERSION.SDK_INT;
                     } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label142;
                     }

                     if (var1 >= 24) {
                        try {
                           var23.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.provider", var25));
                           var23.setFlags(1);
                        } catch (Exception var11) {
                           try {
                              var23.putExtra("android.intent.extra.STREAM", Uri.fromFile(var25));
                           } catch (Exception var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label142;
                           }
                        }
                     } else {
                        try {
                           var23.putExtra("android.intent.extra.STREAM", Uri.fromFile(var25));
                        } catch (Exception var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label142;
                        }
                     }

                     try {
                        this.parentActivity.startActivityForResult(Intent.createChooser(var23, LocaleController.getString("ShareFile", 2131560748)), 500);
                        return;
                     } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label142;
                     }
                  }

                  try {
                     AlertDialog.Builder var26 = new AlertDialog.Builder(this.parentActivity);
                     var26.setTitle(LocaleController.getString("AppName", 2131558635));
                     var26.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                     var26.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
                     var26.show();
                     return;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                  }
               }

               Exception var24 = var10000;
               FileLog.e((Throwable)var24);
            } else if (var1 == 4) {
               var4 = UserConfig.selectedAccount;
               var1 = super.currentAccount;
               if (var4 != var1) {
                  var3.switchToAccount(var1, true);
               }

               Bundle var28 = new Bundle();
               long var6 = var2.getDialogId();
               var4 = (int)var6;
               var1 = (int)(var6 >> 32);
               if (var4 != 0) {
                  if (var1 == 1) {
                     var28.putInt("chat_id", var4);
                  } else if (var4 > 0) {
                     var28.putInt("user_id", var4);
                  } else if (var4 < 0) {
                     TLRPC.Chat var27 = MessagesController.getInstance(super.currentAccount).getChat(-var4);
                     var1 = var4;
                     if (var27 != null) {
                        var1 = var4;
                        if (var27.migrated_to != null) {
                           var28.putInt("migrated_to", var4);
                           var1 = -var27.migrated_to.channel_id;
                        }
                     }

                     var28.putInt("chat_id", -var1);
                  }
               } else {
                  var28.putInt("enc_id", var1);
               }

               var28.putInt("message_id", var2.getId());
               NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
               this.parentActivity.presentFragment(new ChatActivity(var28), false, false);
               this.dismiss();
            }
         }
      }

   }

   private void updateLayout() {
      if (this.listView.getChildCount() > 0) {
         View var1 = this.listView.getChildAt(0);
         RecyclerListView.Holder var2 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var1);
         int var3 = var1.getTop();
         if (var3 <= 0 || var2 == null || var2.getAdapterPosition() != 0) {
            var3 = 0;
         }

         if (this.searchWas || this.searching) {
            var3 = 0;
         }

         if (this.scrollOffsetY != var3) {
            RecyclerListView var4 = this.listView;
            this.scrollOffsetY = var3;
            var4.setTopGlowOffset(var3);
            this.playerLayout.setTranslationY((float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY));
            this.placeholderImageView.setTranslationY((float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY));
            this.shadow2.setTranslationY((float)(Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY) + this.playerLayout.getMeasuredHeight()));
            super.containerView.invalidate();
            AnimatorSet var5;
            if ((!this.inFullSize || this.scrollOffsetY > this.actionBar.getMeasuredHeight()) && !this.searchWas) {
               if (this.actionBar.getTag() != null) {
                  var5 = this.actionBarAnimation;
                  if (var5 != null) {
                     var5.cancel();
                  }

                  this.actionBar.setTag((Object)null);
                  this.actionBarAnimation = new AnimatorSet();
                  this.actionBarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.actionBar, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.shadow, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.shadow2, "alpha", new float[]{0.0F})});
                  this.actionBarAnimation.setDuration(180L);
                  this.actionBarAnimation.start();
               }
            } else if (this.actionBar.getTag() == null) {
               var5 = this.actionBarAnimation;
               if (var5 != null) {
                  var5.cancel();
               }

               this.actionBar.setTag(1);
               this.actionBarAnimation = new AnimatorSet();
               this.actionBarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.actionBar, "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(this.shadow, "alpha", new float[]{1.0F}), ObjectAnimator.ofFloat(this.shadow2, "alpha", new float[]{1.0F})});
               this.actionBarAnimation.setDuration(180L);
               this.actionBarAnimation.start();
            }
         }

         this.startTranslation = (float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY);
         this.panelStartTranslation = (float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY);
      }
   }

   private void updateProgress(MessageObject var1) {
      SeekBarView var2 = this.seekBarView;
      if (var2 != null) {
         if (!var2.isDragging()) {
            this.seekBarView.setProgress(var1.audioProgress);
            this.seekBarView.setBufferedProgress(var1.bufferedProgress);
         }

         int var3 = this.lastTime;
         int var4 = var1.audioProgressSec;
         if (var3 != var4) {
            this.lastTime = var4;
            this.timeTextView.setText(String.format("%d:%02d", var4 / 60, var1.audioProgressSec % 60));
         }
      }

   }

   private void updateRepeatButton() {
      int var1 = SharedConfig.repeatMode;
      if (var1 == 0) {
         this.repeatButton.setImageResource(2131165767);
         this.repeatButton.setTag("player_button");
         this.repeatButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_button"), Mode.MULTIPLY));
         this.repeatButton.setContentDescription(LocaleController.getString("AccDescrRepeatOff", 2131558463));
      } else if (var1 == 1) {
         this.repeatButton.setImageResource(2131165767);
         this.repeatButton.setTag("player_buttonActive");
         this.repeatButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), Mode.MULTIPLY));
         this.repeatButton.setContentDescription(LocaleController.getString("AccDescrRepeatList", 2131558462));
      } else if (var1 == 2) {
         this.repeatButton.setImageResource(2131165768);
         this.repeatButton.setTag("player_buttonActive");
         this.repeatButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), Mode.MULTIPLY));
         this.repeatButton.setContentDescription(LocaleController.getString("AccDescrRepeatOne", 2131558464));
      }

   }

   private void updateShuffleButton() {
      boolean var1 = SharedConfig.shuffleMusic;
      String var2 = "player_button";
      Drawable var3;
      if (var1) {
         var3 = this.getContext().getResources().getDrawable(2131165769).mutate();
         var3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), Mode.MULTIPLY));
         this.shuffleButton.setIcon(var3);
         this.shuffleButton.setContentDescription(LocaleController.getString("Shuffle", 2131560784));
      } else {
         var3 = this.getContext().getResources().getDrawable(2131165684).mutate();
         if (SharedConfig.playOrderReversed) {
            var3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), Mode.MULTIPLY));
         } else {
            var3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("player_button"), Mode.MULTIPLY));
         }

         this.shuffleButton.setIcon(var3);
         this.shuffleButton.setContentDescription(LocaleController.getString("ReverseOrder", 2131560616));
      }

      Drawable var4 = this.playOrderButtons[0];
      String var5;
      if (SharedConfig.playOrderReversed) {
         var5 = "player_buttonActive";
      } else {
         var5 = "player_button";
      }

      var4.setColorFilter(new PorterDuffColorFilter(Theme.getColor(var5), Mode.MULTIPLY));
      var4 = this.playOrderButtons[1];
      var5 = var2;
      if (SharedConfig.shuffleMusic) {
         var5 = "player_buttonActive";
      }

      var4.setColorFilter(new PorterDuffColorFilter(Theme.getColor(var5), Mode.MULTIPLY));
   }

   private void updateTitle(boolean var1) {
      MessageObject var2 = MediaController.getInstance().getPlayingMessageObject();
      if ((var2 != null || !var1) && (var2 == null || var2.isMusic())) {
         if (var2 == null) {
            return;
         }

         if (var2.eventId == 0L && var2.getId() > -2000000000) {
            this.hasOptions = true;
            if (!this.actionBar.isSearchFieldVisible()) {
               this.menuItem.setVisibility(0);
            }

            this.optionsButton.setVisibility(0);
         } else {
            this.hasOptions = false;
            this.menuItem.setVisibility(4);
            this.optionsButton.setVisibility(4);
         }

         this.checkIfMusicDownloaded(var2);
         this.updateProgress(var2);
         ImageView var3;
         if (MediaController.getInstance().isMessagePaused()) {
            var3 = this.playButton;
            var3.setImageDrawable(Theme.createSimpleSelectorDrawable(var3.getContext(), 2131165765, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
            this.playButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131558409));
         } else {
            var3 = this.playButton;
            var3.setImageDrawable(Theme.createSimpleSelectorDrawable(var3.getContext(), 2131165764, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
            this.playButton.setContentDescription(LocaleController.getString("AccActionPause", 2131558408));
         }

         String var4 = var2.getMusicTitle();
         String var5 = var2.getMusicAuthor();
         this.titleTextView.setText(var4);
         this.authorTextView.setText(var5);
         this.actionBar.setTitle(var4);
         this.actionBar.setSubtitle(var5);
         StringBuilder var8 = new StringBuilder();
         var8.append(var5);
         var8.append(" ");
         var8.append(var4);
         var8.toString();
         AudioInfo var9 = MediaController.getInstance().getAudioInfo();
         if (var9 != null && var9.getCover() != null) {
            this.hasNoCover = 0;
            this.placeholderImageView.setImageBitmap(var9.getCover());
         } else {
            String var10 = var2.getArtworkUrl(false);
            if (!TextUtils.isEmpty(var10)) {
               this.placeholderImageView.setImage(var10, (String)null, (Drawable)null);
               this.hasNoCover = 2;
            } else {
               this.placeholderImageView.setImageDrawable((Drawable)null);
               this.hasNoCover = 1;
            }

            this.placeholderImageView.invalidate();
         }

         if (this.durationTextView != null) {
            int var6 = var2.getDuration();
            TextView var11 = this.durationTextView;
            String var7;
            if (var6 != 0) {
               var7 = String.format("%d:%02d", var6 / 60, var6 % 60);
            } else {
               var7 = "-:--";
            }

            var11.setText(var7);
         }
      } else {
         this.dismiss();
      }

   }

   protected boolean canDismissWithSwipe() {
      return false;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      MessageObject var7;
      if (var1 != NotificationCenter.messagePlayingDidStart && var1 != NotificationCenter.messagePlayingPlayStateChanged && var1 != NotificationCenter.messagePlayingDidReset) {
         if (var1 == NotificationCenter.messagePlayingProgressDidChanged) {
            var7 = MediaController.getInstance().getPlayingMessageObject();
            if (var7 != null && var7.isMusic()) {
               this.updateProgress(var7);
            }
         } else if (var1 == NotificationCenter.musicDidLoad) {
            this.playlist = MediaController.getInstance().getPlaylist();
            this.listAdapter.notifyDataSetChanged();
         }
      } else {
         boolean var4;
         if (var1 == NotificationCenter.messagePlayingDidReset && (Boolean)var3[1]) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.updateTitle(var4);
         View var6;
         if (var1 != NotificationCenter.messagePlayingDidReset && var1 != NotificationCenter.messagePlayingPlayStateChanged) {
            if (var1 == NotificationCenter.messagePlayingDidStart) {
               if (((MessageObject)var3[0]).eventId != 0L) {
                  return;
               }

               var2 = this.listView.getChildCount();

               for(var1 = 0; var1 < var2; ++var1) {
                  var6 = this.listView.getChildAt(var1);
                  if (var6 instanceof AudioPlayerCell) {
                     AudioPlayerCell var8 = (AudioPlayerCell)var6;
                     MessageObject var9 = var8.getMessageObject();
                     if (var9 != null && (var9.isVoice() || var9.isMusic())) {
                        var8.updateButtonState(false, true);
                     }
                  }
               }
            }
         } else {
            var2 = this.listView.getChildCount();

            for(var1 = 0; var1 < var2; ++var1) {
               var6 = this.listView.getChildAt(var1);
               if (var6 instanceof AudioPlayerCell) {
                  AudioPlayerCell var5 = (AudioPlayerCell)var6;
                  var7 = var5.getMessageObject();
                  if (var7 != null && (var7.isVoice() || var7.isMusic())) {
                     var5.updateButtonState(false, true);
                  }
               }
            }
         }
      }

   }

   public void dismiss() {
      super.dismiss();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.musicDidLoad);
      DownloadController.getInstance(super.currentAccount).removeLoadingFileObserver(this);
   }

   @Keep
   public float getFullAnimationProgress() {
      return this.fullAnimationProgress;
   }

   public int getObserverTag() {
      return this.TAG;
   }

   // $FF: synthetic method
   public void lambda$new$0$AudioPlayerAlert(View var1) {
      AnimatorSet var10 = this.animatorSet;
      if (var10 != null) {
         var10.cancel();
         this.animatorSet = null;
      }

      this.animatorSet = new AnimatorSet();
      int var2 = this.scrollOffsetY;
      int var3 = this.actionBar.getMeasuredHeight();
      float var4 = 0.0F;
      float var5 = 0.0F;
      if (var2 <= var3) {
         var10 = this.animatorSet;
         if (!this.isInFullMode) {
            var5 = 1.0F;
         }

         var10.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "fullAnimationProgress", new float[]{var5})});
      } else {
         var10 = this.animatorSet;
         if (this.isInFullMode) {
            var5 = 0.0F;
         } else {
            var5 = 1.0F;
         }

         ObjectAnimator var6 = ObjectAnimator.ofFloat(this, "fullAnimationProgress", new float[]{var5});
         ActionBar var7 = this.actionBar;
         if (this.isInFullMode) {
            var5 = 0.0F;
         } else {
            var5 = 1.0F;
         }

         ObjectAnimator var11 = ObjectAnimator.ofFloat(var7, "alpha", new float[]{var5});
         View var8 = this.shadow;
         if (this.isInFullMode) {
            var5 = 0.0F;
         } else {
            var5 = 1.0F;
         }

         ObjectAnimator var12 = ObjectAnimator.ofFloat(var8, "alpha", new float[]{var5});
         View var9 = this.shadow2;
         if (this.isInFullMode) {
            var5 = var4;
         } else {
            var5 = 1.0F;
         }

         var10.playTogether(new Animator[]{var6, var11, var12, ObjectAnimator.ofFloat(var9, "alpha", new float[]{var5})});
      }

      this.animatorSet.setInterpolator(new DecelerateInterpolator());
      this.animatorSet.setDuration(250L);
      this.animatorSet.addListener(new AudioPlayerAlert$5(this));
      this.animatorSet.start();
      if (this.hasOptions) {
         this.menuItem.setVisibility(0);
      }

      this.searchItem.setVisibility(0);
      this.isInFullMode ^= true;
      this.listView.setScrollEnabled(false);
      if (this.isInFullMode) {
         this.shuffleButton.setAdditionalOffset(-AndroidUtilities.dp(68.0F));
      } else {
         this.shuffleButton.setAdditionalOffset(-AndroidUtilities.dp(10.0F));
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$AudioPlayerAlert(View var1) {
      this.optionsButton.toggleSubMenu();
   }

   // $FF: synthetic method
   public void lambda$new$3$AudioPlayerAlert(View var1) {
      this.shuffleButton.toggleSubMenu();
   }

   // $FF: synthetic method
   public void lambda$new$4$AudioPlayerAlert(int var1) {
      MediaController.getInstance().toggleShuffleMusic(var1);
      this.updateShuffleButton();
      this.listAdapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   public void lambda$new$8$AudioPlayerAlert(View var1) {
      SharedConfig.toggleRepeatMode();
      this.updateRepeatButton();
   }

   // $FF: synthetic method
   public void lambda$onSubItemClick$10$AudioPlayerAlert(ArrayList var1, DialogsActivity var2, ArrayList var3, CharSequence var4, boolean var5) {
      int var7 = var3.size();
      byte var8 = 0;
      int var9 = var8;
      long var10;
      if (var7 <= 1) {
         var9 = var8;
         if ((Long)var3.get(0) != (long)UserConfig.getInstance(super.currentAccount).getClientUserId()) {
            if (var4 == null) {
               var10 = (Long)var3.get(0);
               int var14 = (int)var10;
               var9 = (int)(var10 >> 32);
               Bundle var12 = new Bundle();
               var12.putBoolean("scrollToTopOnResume", true);
               if (var14 != 0) {
                  if (var14 > 0) {
                     var12.putInt("user_id", var14);
                  } else if (var14 < 0) {
                     var12.putInt("chat_id", -var14);
                  }
               } else {
                  var12.putInt("enc_id", var9);
               }

               NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
               ChatActivity var13 = new ChatActivity(var12);
               if (this.parentActivity.presentFragment(var13, true, false)) {
                  var13.showFieldPanelForForward(true, var1);
               } else {
                  var2.finishFragment();
               }

               return;
            }

            var9 = var8;
         }
      }

      while(var9 < var3.size()) {
         var10 = (Long)var3.get(var9);
         if (var4 != null) {
            SendMessagesHelper.getInstance(super.currentAccount).sendMessage(var4.toString(), var10, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
         }

         SendMessagesHelper.getInstance(super.currentAccount).sendMessage(var1, var10);
         ++var9;
      }

      var2.finishFragment();
   }

   public void onBackPressed() {
      ActionBar var1 = this.actionBar;
      if (var1 != null && var1.isSearchFieldVisible()) {
         this.actionBar.closeSearchField();
      } else {
         super.onBackPressed();
      }
   }

   public void onFailedDownload(String var1, boolean var2) {
   }

   public void onProgressDownload(String var1, float var2) {
      this.progressView.setProgress(var2, true);
   }

   public void onProgressUpload(String var1, float var2, boolean var3) {
   }

   public void onSuccessDownload(String var1) {
   }

   @Keep
   public void setFullAnimationProgress(float var1) {
      this.fullAnimationProgress = var1;
      this.placeholderImageView.setRoundRadius(AndroidUtilities.dp((1.0F - this.fullAnimationProgress) * 20.0F));
      var1 = this.thumbMaxScale * this.fullAnimationProgress + 1.0F;
      this.placeholderImageView.setScaleX(var1);
      this.placeholderImageView.setScaleY(var1);
      this.placeholderImageView.getTranslationY();
      this.placeholderImageView.setTranslationX((float)this.thumbMaxX * this.fullAnimationProgress);
      BackupImageView var2 = this.placeholderImageView;
      var1 = this.startTranslation;
      var2.setTranslationY(var1 + (this.endTranslation - var1) * this.fullAnimationProgress);
      FrameLayout var3 = this.playerLayout;
      var1 = this.panelStartTranslation;
      var3.setTranslationY(var1 + (this.panelEndTranslation - var1) * this.fullAnimationProgress);
      View var4 = this.shadow2;
      var1 = this.panelStartTranslation;
      var4.setTranslationY(var1 + (this.panelEndTranslation - var1) * this.fullAnimationProgress + (float)this.playerLayout.getMeasuredHeight());
      this.menuItem.setAlpha(this.fullAnimationProgress);
      this.searchItem.setAlpha(1.0F - this.fullAnimationProgress);
      this.avatarContainer.setAlpha(1.0F - this.fullAnimationProgress);
      this.actionBar.getTitleTextView().setAlpha(this.fullAnimationProgress);
      this.actionBar.getSubtitleTextView().setAlpha(this.fullAnimationProgress);
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;
      private ArrayList searchResult = new ArrayList();
      private Timer searchTimer;

      public ListAdapter(Context var2) {
         this.context = var2;
      }

      private void processSearch(String var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$AudioPlayerAlert$ListAdapter$uVY9bQZgLPgCaSv9pJfZ7D2na7c(this, var1));
      }

      private void updateSearchResults(ArrayList var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$AudioPlayerAlert$ListAdapter$q5JODj0x7FGd36CwY5UAvx5F7pI(this, var1));
      }

      public int getItemCount() {
         if (AudioPlayerAlert.this.searchWas) {
            return this.searchResult.size();
         } else {
            return AudioPlayerAlert.this.searching ? AudioPlayerAlert.this.playlist.size() : AudioPlayerAlert.this.playlist.size() + 1;
         }
      }

      public int getItemViewType(int var1) {
         return !AudioPlayerAlert.this.searchWas && !AudioPlayerAlert.this.searching && var1 == 0 ? 0 : 1;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (!AudioPlayerAlert.this.searchWas && var1.getAdapterPosition() <= 0) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      // $FF: synthetic method
      public void lambda$null$0$AudioPlayerAlert$ListAdapter(String var1, ArrayList var2) {
         String var3 = var1.trim().toLowerCase();
         if (var3.length() == 0) {
            this.updateSearchResults(new ArrayList());
         } else {
            label84: {
               String var4 = LocaleController.getInstance().getTranslitString(var3);
               if (!var3.equals(var4)) {
                  var1 = var4;
                  if (var4.length() != 0) {
                     break label84;
                  }
               }

               var1 = null;
            }

            byte var5;
            if (var1 != null) {
               var5 = 1;
            } else {
               var5 = 0;
            }

            String[] var13 = new String[var5 + 1];
            var13[0] = var3;
            if (var1 != null) {
               var13[1] = var1;
            }

            ArrayList var6 = new ArrayList();

            for(int var14 = 0; var14 < var2.size(); ++var14) {
               MessageObject var7 = (MessageObject)var2.get(var14);

               for(int var8 = 0; var8 < var13.length; ++var8) {
                  var3 = var13[var8];
                  var1 = var7.getDocumentName();
                  if (var1 != null && var1.length() != 0) {
                     if (var1.toLowerCase().contains(var3)) {
                        var6.add(var7);
                        break;
                     }

                     TLRPC.Document var12;
                     if (var7.type == 0) {
                        var12 = var7.messageOwner.media.webpage.document;
                     } else {
                        var12 = var7.messageOwner.media.document;
                     }

                     int var9 = 0;

                     boolean var11;
                     while(true) {
                        if (var9 >= var12.attributes.size()) {
                           var11 = false;
                           break;
                        }

                        TLRPC.DocumentAttribute var10 = (TLRPC.DocumentAttribute)var12.attributes.get(var9);
                        if (var10 instanceof TLRPC.TL_documentAttributeAudio) {
                           var1 = var10.performer;
                           if (var1 != null) {
                              var11 = var1.toLowerCase().contains(var3);
                           } else {
                              var11 = false;
                           }

                           if (!var11) {
                              var1 = var10.title;
                              if (var1 != null) {
                                 var11 = var1.toLowerCase().contains(var3);
                              }
                           }
                           break;
                        }

                        ++var9;
                     }

                     if (var11) {
                        var6.add(var7);
                        break;
                     }
                  }
               }
            }

            this.updateSearchResults(var6);
         }
      }

      // $FF: synthetic method
      public void lambda$processSearch$1$AudioPlayerAlert$ListAdapter(String var1) {
         ArrayList var2 = new ArrayList(AudioPlayerAlert.this.playlist);
         Utilities.searchQueue.postRunnable(new _$$Lambda$AudioPlayerAlert$ListAdapter$FXPaVUN_2Qhq8rdrH0D2ehP2yJM(this, var1, var2));
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$2$AudioPlayerAlert$ListAdapter(ArrayList var1) {
         AudioPlayerAlert.this.searchWas = true;
         this.searchResult = var1;
         this.notifyDataSetChanged();
         AudioPlayerAlert.this.layoutManager.scrollToPosition(0);
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 1) {
            AudioPlayerCell var3 = (AudioPlayerCell)var1.itemView;
            if (AudioPlayerAlert.this.searchWas) {
               var3.setMessageObject((MessageObject)this.searchResult.get(var2));
            } else if (AudioPlayerAlert.this.searching) {
               if (SharedConfig.playOrderReversed) {
                  var3.setMessageObject((MessageObject)AudioPlayerAlert.this.playlist.get(var2));
               } else {
                  var3.setMessageObject((MessageObject)AudioPlayerAlert.this.playlist.get(AudioPlayerAlert.this.playlist.size() - var2 - 1));
               }
            } else if (var2 > 0) {
               if (SharedConfig.playOrderReversed) {
                  var3.setMessageObject((MessageObject)AudioPlayerAlert.this.playlist.get(var2 - 1));
               } else {
                  var3.setMessageObject((MessageObject)AudioPlayerAlert.this.playlist.get(AudioPlayerAlert.this.playlist.size() - var2));
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new AudioPlayerCell(this.context);
         } else {
            var3 = new View(this.context);
            ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(178.0F)));
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void search(final String var1) {
         try {
            if (this.searchTimer != null) {
               this.searchTimer.cancel();
            }
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         if (var1 == null) {
            this.searchResult.clear();
            this.notifyDataSetChanged();
         } else {
            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
               public void run() {
                  try {
                     ListAdapter.this.searchTimer.cancel();
                     ListAdapter.this.searchTimer = null;
                  } catch (Exception var2) {
                     FileLog.e((Throwable)var2);
                  }

                  ListAdapter.this.processSearch(var1);
               }
            }, 200L, 300L);
         }

      }
   }
}
