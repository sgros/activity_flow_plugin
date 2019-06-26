// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.TimerTask;
import java.util.Collection;
import org.telegram.messenger.Utilities;
import java.util.Timer;
import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper;
import android.animation.Animator$AnimatorListener;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.Keep;
import org.telegram.messenger.audioinfo.AudioInfo;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.messenger.FileLog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.net.Uri;
import android.os.Parcelable;
import androidx.core.content.FileProvider;
import org.telegram.messenger.ApplicationLoader;
import android.content.Intent;
import android.text.TextUtils;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.DialogsActivity;
import android.os.Bundle;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.FileLoader;
import java.io.File;
import org.telegram.tgnet.TLRPC;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Cells.AudioPlayerCell;
import android.widget.ImageView$ScaleType;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.text.TextUtils$TruncateAt;
import android.view.View$OnClickListener;
import android.graphics.RectF;
import android.widget.EditText;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ChatActivity;
import android.os.Build$VERSION;
import org.telegram.messenger.AndroidUtilities;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MediaController;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.messenger.MessageObject;
import java.util.ArrayList;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.ui.LaunchActivity;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BottomSheet;

public class AudioPlayerAlert extends BottomSheet implements NotificationCenterDelegate, FileDownloadProgressListener
{
    private int TAG;
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private AnimatorSet animatorSet;
    private TextView authorTextView;
    private ChatAvatarContainer avatarContainer;
    private View[] buttons;
    private TextView durationTextView;
    private float endTranslation;
    private float fullAnimationProgress;
    private int hasNoCover;
    private boolean hasOptions;
    private boolean inFullSize;
    private boolean isInFullMode;
    private int lastTime;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ActionBarMenuItem menuItem;
    private Drawable noCoverDrawable;
    private ActionBarMenuItem optionsButton;
    private Paint paint;
    private float panelEndTranslation;
    private float panelStartTranslation;
    private LaunchActivity parentActivity;
    private BackupImageView placeholderImageView;
    private ImageView playButton;
    private Drawable[] playOrderButtons;
    private FrameLayout playerLayout;
    private ArrayList<MessageObject> playlist;
    private LineProgressView progressView;
    private ImageView repeatButton;
    private int scrollOffsetY;
    private boolean scrollToSong;
    private ActionBarMenuItem searchItem;
    private int searchOpenOffset;
    private int searchOpenPosition;
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
    
    public AudioPlayerAlert(final Context context) {
        super(context, true, 0);
        this.buttons = new View[5];
        this.playOrderButtons = new Drawable[2];
        this.hasOptions = true;
        this.scrollToSong = true;
        this.searchOpenPosition = -1;
        this.paint = new Paint(1);
        this.scrollOffsetY = Integer.MAX_VALUE;
        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null) {
            super.currentAccount = playingMessageObject.currentAccount;
        }
        else {
            super.currentAccount = UserConfig.selectedAccount;
        }
        this.parentActivity = (LaunchActivity)context;
        (this.noCoverDrawable = context.getResources().getDrawable(2131165694).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_placeholder"), PorterDuff$Mode.MULTIPLY));
        this.TAG = DownloadController.getInstance(super.currentAccount).generateObserverTag();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.musicDidLoad);
        (this.shadowDrawable = context.getResources().getDrawable(2131165823).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_background"), PorterDuff$Mode.MULTIPLY));
        this.paint.setColor(Theme.getColor("player_placeholderBackground"));
        (super.containerView = (ViewGroup)new FrameLayout(context) {
            private boolean ignoreLayout = false;
            
            protected void onDraw(final Canvas canvas) {
                AudioPlayerAlert.this.shadowDrawable.setBounds(0, Math.max(AudioPlayerAlert.this.actionBar.getMeasuredHeight(), AudioPlayerAlert.this.scrollOffsetY) - AudioPlayerAlert.this.backgroundPaddingTop, this.getMeasuredWidth(), this.getMeasuredHeight());
                AudioPlayerAlert.this.shadowDrawable.draw(canvas);
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && AudioPlayerAlert.this.scrollOffsetY != 0 && motionEvent.getY() < AudioPlayerAlert.this.scrollOffsetY && AudioPlayerAlert.this.placeholderImageView.getTranslationX() == 0.0f) {
                    AudioPlayerAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            protected void onLayout(final boolean b, int measuredHeight, final int n, final int n2, final int n3) {
                super.onLayout(b, measuredHeight, n, n2, n3);
                measuredHeight = AudioPlayerAlert.this.actionBar.getMeasuredHeight();
                AudioPlayerAlert.this.shadow.layout(AudioPlayerAlert.this.shadow.getLeft(), measuredHeight, AudioPlayerAlert.this.shadow.getRight(), AudioPlayerAlert.this.shadow.getMeasuredHeight() + measuredHeight);
                AudioPlayerAlert.this.updateLayout();
                final AudioPlayerAlert this$0 = AudioPlayerAlert.this;
                this$0.setFullAnimationProgress(this$0.fullAnimationProgress);
            }
            
            protected void onMeasure(int statusBarHeight, int n) {
                final int size = View$MeasureSpec.getSize(n);
                n = AndroidUtilities.dp(178.0f) + AudioPlayerAlert.this.playlist.size() * AndroidUtilities.dp(56.0f) + AudioPlayerAlert.this.backgroundPaddingTop + ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight;
                final int measureSpec = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                final boolean access$400 = AudioPlayerAlert.this.searching;
                final int n2 = 0;
                int n3;
                if (access$400) {
                    n3 = AndroidUtilities.dp(178.0f) + ActionBar.getCurrentActionBarHeight();
                    if (Build$VERSION.SDK_INT >= 21) {
                        n = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        n = 0;
                    }
                }
                else {
                    if (n < size) {
                        n = size - n;
                    }
                    else if (n < size) {
                        n = 0;
                    }
                    else {
                        n = size - size / 5 * 3;
                    }
                    final int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                    int statusBarHeight2;
                    if (Build$VERSION.SDK_INT >= 21) {
                        statusBarHeight2 = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        statusBarHeight2 = 0;
                    }
                    final int n4 = currentActionBarHeight + statusBarHeight2;
                    n3 = n;
                    n = n4;
                }
                n += n3;
                final int paddingTop = AudioPlayerAlert.this.listView.getPaddingTop();
                boolean b = true;
                if (paddingTop != n) {
                    this.ignoreLayout = true;
                    AudioPlayerAlert.this.listView.setPadding(0, n, 0, AndroidUtilities.dp(8.0f));
                    this.ignoreLayout = false;
                }
                super.onMeasure(statusBarHeight, measureSpec);
                final AudioPlayerAlert this$0 = AudioPlayerAlert.this;
                if (this.getMeasuredHeight() < size) {
                    b = false;
                }
                this$0.inFullSize = b;
                n = ActionBar.getCurrentActionBarHeight();
                statusBarHeight = n2;
                if (Build$VERSION.SDK_INT >= 21) {
                    statusBarHeight = AndroidUtilities.statusBarHeight;
                }
                statusBarHeight = size - n - statusBarHeight - AndroidUtilities.dp(120.0f);
                n = Math.max(statusBarHeight, this.getMeasuredWidth());
                AudioPlayerAlert.this.thumbMaxX = (this.getMeasuredWidth() - n) / 2 - AndroidUtilities.dp(17.0f);
                AudioPlayerAlert.this.thumbMaxY = AndroidUtilities.dp(19.0f);
                AudioPlayerAlert.this.panelEndTranslation = (float)(this.getMeasuredHeight() - AudioPlayerAlert.this.playerLayout.getMeasuredHeight());
                final AudioPlayerAlert this$2 = AudioPlayerAlert.this;
                this$2.thumbMaxScale = n / (float)this$2.placeholderImageView.getMeasuredWidth() - 1.0f;
                AudioPlayerAlert.this.endTranslation = (float)(ActionBar.getCurrentActionBarHeight() + (AndroidUtilities.statusBarHeight - AndroidUtilities.dp(19.0f)));
                n = (int)Math.ceil(AudioPlayerAlert.this.placeholderImageView.getMeasuredHeight() * (AudioPlayerAlert.this.thumbMaxScale + 1.0f));
                if (n > statusBarHeight) {
                    final AudioPlayerAlert this$3 = AudioPlayerAlert.this;
                    this$3.endTranslation -= n - statusBarHeight;
                }
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return !AudioPlayerAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }
            
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setWillNotDraw(false);
        final ViewGroup containerView = super.containerView;
        final int backgroundPaddingLeft = super.backgroundPaddingLeft;
        containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
        (this.actionBar = new ActionBar(context)).setBackgroundColor(Theme.getColor("player_actionBar"));
        this.actionBar.setBackButtonImage(2131165409);
        this.actionBar.setItemsColor(Theme.getColor("player_actionBarItems"), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor("player_actionBarSelector"), false);
        this.actionBar.setTitleColor(Theme.getColor("player_actionBarTitle"));
        this.actionBar.setSubtitleColor(Theme.getColor("player_actionBarSubtitle"));
        this.actionBar.setAlpha(0.0f);
        this.actionBar.setTitle("1");
        this.actionBar.setSubtitle("1");
        this.actionBar.getTitleTextView().setAlpha(0.0f);
        this.actionBar.getSubtitleTextView().setAlpha(0.0f);
        (this.avatarContainer = new ChatAvatarContainer(context, null, false)).setEnabled(false);
        this.avatarContainer.setTitleColors(Theme.getColor("player_actionBarTitle"), Theme.getColor("player_actionBarSubtitle"));
        if (playingMessageObject != null) {
            final long dialogId = playingMessageObject.getDialogId();
            final int i = (int)dialogId;
            final int j = (int)(dialogId >> 32);
            if (i != 0) {
                if (i > 0) {
                    final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(i);
                    if (user != null) {
                        this.avatarContainer.setTitle(ContactsController.formatName(user.first_name, user.last_name));
                        this.avatarContainer.setUserAvatar(user);
                    }
                }
                else {
                    final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(-i);
                    if (chat != null) {
                        this.avatarContainer.setTitle(chat.title);
                        this.avatarContainer.setChatAvatar(chat);
                    }
                }
            }
            else {
                final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(super.currentAccount).getEncryptedChat(j);
                if (encryptedChat != null) {
                    final TLRPC.User user2 = MessagesController.getInstance(super.currentAccount).getUser(encryptedChat.user_id);
                    if (user2 != null) {
                        this.avatarContainer.setTitle(ContactsController.formatName(user2.first_name, user2.last_name));
                        this.avatarContainer.setUserAvatar(user2);
                    }
                }
            }
        }
        this.avatarContainer.setSubtitle(LocaleController.getString("AudioTitle", 2131558736));
        this.actionBar.addView((View)this.avatarContainer, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, 51, 56.0f, 0.0f, 40.0f, 0.0f));
        final ActionBarMenu menu = this.actionBar.createMenu();
        menu.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        (this.menuItem = menu.addItem(0, 2131165416)).addSubItem(1, 2131165627, LocaleController.getString("Forward", 2131559504));
        this.menuItem.addSubItem(2, 2131165671, LocaleController.getString("ShareFile", 2131560748));
        this.menuItem.addSubItem(4, 2131165647, LocaleController.getString("ShowInChat", 2131560780));
        this.menuItem.setTranslationX((float)AndroidUtilities.dp(48.0f));
        this.menuItem.setAlpha(0.0f);
        (this.searchItem = menu.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                AudioPlayerAlert.this.avatarContainer.setVisibility(0);
                if (AudioPlayerAlert.this.hasOptions) {
                    AudioPlayerAlert.this.menuItem.setVisibility(4);
                }
                if (AudioPlayerAlert.this.searching) {
                    AudioPlayerAlert.this.searchWas = false;
                    AudioPlayerAlert.this.searching = false;
                    AudioPlayerAlert.this.setAllowNestedScroll(true);
                    AudioPlayerAlert.this.listAdapter.search(null);
                }
            }
            
            @Override
            public void onSearchExpand() {
                final AudioPlayerAlert this$0 = AudioPlayerAlert.this;
                this$0.searchOpenPosition = this$0.layoutManager.findLastVisibleItemPosition();
                final View viewByPosition = AudioPlayerAlert.this.layoutManager.findViewByPosition(AudioPlayerAlert.this.searchOpenPosition);
                final AudioPlayerAlert this$2 = AudioPlayerAlert.this;
                int top;
                if (viewByPosition == null) {
                    top = 0;
                }
                else {
                    top = viewByPosition.getTop();
                }
                this$2.searchOpenOffset = top - AudioPlayerAlert.this.listView.getPaddingTop();
                AudioPlayerAlert.this.avatarContainer.setVisibility(8);
                if (AudioPlayerAlert.this.hasOptions) {
                    AudioPlayerAlert.this.menuItem.setVisibility(8);
                }
                AudioPlayerAlert.this.searching = true;
                AudioPlayerAlert.this.setAllowNestedScroll(false);
                AudioPlayerAlert.this.listAdapter.notifyDataSetChanged();
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                if (editText.length() > 0) {
                    AudioPlayerAlert.this.listAdapter.search(editText.getText().toString());
                }
                else {
                    AudioPlayerAlert.this.searchWas = false;
                    AudioPlayerAlert.this.listAdapter.search(null);
                }
            }
        })).setContentDescription((CharSequence)LocaleController.getString("Search", 2131560640));
        final EditTextBoldCursor searchField = this.searchItem.getSearchField();
        searchField.setHint((CharSequence)LocaleController.getString("Search", 2131560640));
        searchField.setTextColor(Theme.getColor("player_actionBarTitle"));
        searchField.setHintTextColor(Theme.getColor("player_time"));
        searchField.setCursorColor(Theme.getColor("player_actionBarTitle"));
        if (!AndroidUtilities.isTablet()) {
            this.actionBar.showActionModeTop();
            this.actionBar.setActionModeTopColor(Theme.getColor("player_actionBarTop"));
        }
        this.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    AudioPlayerAlert.this.dismiss();
                }
                else {
                    AudioPlayerAlert.this.onSubItemClick(n);
                }
            }
        });
        (this.shadow = new View(context)).setAlpha(0.0f);
        this.shadow.setBackgroundResource(2131165407);
        (this.shadow2 = new View(context)).setAlpha(0.0f);
        this.shadow2.setBackgroundResource(2131165407);
        (this.playerLayout = new FrameLayout(context)).setBackgroundColor(Theme.getColor("player_background"));
        (this.placeholderImageView = new BackupImageView(context) {
            private RectF rect = new RectF();
            
            @Override
            protected void onDraw(final Canvas canvas) {
                if (AudioPlayerAlert.this.hasNoCover == 1 || (AudioPlayerAlert.this.hasNoCover == 2 && (!this.getImageReceiver().hasBitmapImage() || this.getImageReceiver().getCurrentAlpha() != 1.0f))) {
                    this.rect.set(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
                    canvas.drawRoundRect(this.rect, (float)this.getRoundRadius(), (float)this.getRoundRadius(), AudioPlayerAlert.this.paint);
                    final int n = (int)(AndroidUtilities.dp(63.0f) * Math.max(AudioPlayerAlert.this.thumbMaxScale / this.getScaleX() / 3.0f / AudioPlayerAlert.this.thumbMaxScale, 1.0f / AudioPlayerAlert.this.thumbMaxScale));
                    final float centerX = this.rect.centerX();
                    final float n2 = (float)(n / 2);
                    final int n3 = (int)(centerX - n2);
                    final int n4 = (int)(this.rect.centerY() - n2);
                    AudioPlayerAlert.this.noCoverDrawable.setBounds(n3, n4, n3 + n, n + n4);
                    AudioPlayerAlert.this.noCoverDrawable.draw(canvas);
                }
                if (AudioPlayerAlert.this.hasNoCover != 1) {
                    super.onDraw(canvas);
                }
            }
        }).setRoundRadius(AndroidUtilities.dp(20.0f));
        this.placeholderImageView.setPivotX(0.0f);
        this.placeholderImageView.setPivotY(0.0f);
        this.placeholderImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$AudioPlayerAlert$rAc8YAca3eZCLAfei_UTWtnjvbQ(this));
        (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("player_actionBarTitle"));
        this.titleTextView.setTextSize(1, 15.0f);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.titleTextView.setSingleLine(true);
        this.playerLayout.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 72.0f, 18.0f, 60.0f, 0.0f));
        (this.authorTextView = new TextView(context)).setTextColor(Theme.getColor("player_time"));
        this.authorTextView.setTextSize(1, 14.0f);
        this.authorTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.authorTextView.setSingleLine(true);
        this.playerLayout.addView((View)this.authorTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 72.0f, 40.0f, 60.0f, 0.0f));
        (this.optionsButton = new ActionBarMenuItem(context, null, 0, Theme.getColor("player_actionBarItems"))).setLongClickEnabled(false);
        this.optionsButton.setIcon(2131165416);
        this.optionsButton.setAdditionalOffset(-AndroidUtilities.dp(120.0f));
        this.playerLayout.addView((View)this.optionsButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, 19.0f, 10.0f, 0.0f));
        this.optionsButton.addSubItem(1, 2131165627, LocaleController.getString("Forward", 2131559504));
        this.optionsButton.addSubItem(2, 2131165671, LocaleController.getString("ShareFile", 2131560748));
        this.optionsButton.addSubItem(4, 2131165647, LocaleController.getString("ShowInChat", 2131560780));
        this.optionsButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AudioPlayerAlert$L6a3eFAcdzUb5tmijbnBD0GiP9c(this));
        this.optionsButton.setDelegate((ActionBarMenuItem.ActionBarMenuItemDelegate)new _$$Lambda$AudioPlayerAlert$2le4yIg27yXBU0W_K7geAc_qlH0(this));
        this.optionsButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        (this.seekBarView = new SeekBarView(context)).setDelegate((SeekBarView.SeekBarViewDelegate)_$$Lambda$AudioPlayerAlert$pQ_5LDOKmp9BWnrwt0sO_Br_q0g.INSTANCE);
        this.playerLayout.addView((View)this.seekBarView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 30.0f, 51, 8.0f, 62.0f, 8.0f, 0.0f));
        (this.progressView = new LineProgressView(context)).setVisibility(4);
        this.progressView.setBackgroundColor(Theme.getColor("player_progressBackground"));
        this.progressView.setProgressColor(Theme.getColor("player_progress"));
        this.playerLayout.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 2.0f, 51, 20.0f, 78.0f, 20.0f, 0.0f));
        (this.timeTextView = new SimpleTextView(context)).setTextSize(12);
        this.timeTextView.setText("0:00");
        this.timeTextView.setTextColor(Theme.getColor("player_time"));
        this.playerLayout.addView((View)this.timeTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(100, -2.0f, 51, 20.0f, 92.0f, 0.0f, 0.0f));
        (this.durationTextView = new TextView(context)).setTextSize(1, 12.0f);
        this.durationTextView.setTextColor(Theme.getColor("player_time"));
        this.durationTextView.setGravity(17);
        this.playerLayout.addView((View)this.durationTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 90.0f, 20.0f, 0.0f));
        final FrameLayout frameLayout = new FrameLayout(context) {
            protected void onLayout(final boolean b, int i, int n, int n2, int dp) {
                n = (n2 - i - AndroidUtilities.dp(248.0f)) / 4;
                for (i = 0; i < 5; ++i) {
                    n2 = AndroidUtilities.dp((float)(i * 48 + 4)) + n * i;
                    dp = AndroidUtilities.dp(9.0f);
                    AudioPlayerAlert.this.buttons[i].layout(n2, dp, AudioPlayerAlert.this.buttons[i].getMeasuredWidth() + n2, AudioPlayerAlert.this.buttons[i].getMeasuredHeight() + dp);
                }
            }
        };
        this.playerLayout.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 66.0f, 51, 0.0f, 106.0f, 0.0f, 0.0f));
        this.buttons[0] = (View)(this.shuffleButton = new ActionBarMenuItem(context, null, 0, 0));
        this.shuffleButton.setLongClickEnabled(false);
        this.shuffleButton.setAdditionalOffset(-AndroidUtilities.dp(10.0f));
        frameLayout.addView((View)this.shuffleButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 51));
        this.shuffleButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AudioPlayerAlert$vDsQnotDAAYl8VqVL3Roz4WO8JM(this));
        final TextView addSubItem = this.shuffleButton.addSubItem(1, LocaleController.getString("ReverseOrder", 2131560616));
        addSubItem.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(16.0f), 0);
        this.playOrderButtons[0] = context.getResources().getDrawable(2131165684).mutate();
        addSubItem.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        addSubItem.setCompoundDrawablesWithIntrinsicBounds(this.playOrderButtons[0], (Drawable)null, (Drawable)null, (Drawable)null);
        final TextView addSubItem2 = this.shuffleButton.addSubItem(2, LocaleController.getString("Shuffle", 2131560784));
        addSubItem2.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(16.0f), 0);
        this.playOrderButtons[1] = context.getResources().getDrawable(2131165769).mutate();
        addSubItem2.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        addSubItem2.setCompoundDrawablesWithIntrinsicBounds(this.playOrderButtons[1], (Drawable)null, (Drawable)null, (Drawable)null);
        this.shuffleButton.setDelegate((ActionBarMenuItem.ActionBarMenuItemDelegate)new _$$Lambda$AudioPlayerAlert$HopcJEXECX0ZxGGa4UewdstQ_LI(this));
        final View[] buttons = this.buttons;
        final ImageView imageView = new ImageView(context);
        ((ImageView)(buttons[1] = (View)imageView)).setScaleType(ImageView$ScaleType.CENTER);
        imageView.setImageDrawable(Theme.createSimpleSelectorDrawable(context, 2131165766, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
        frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 51));
        imageView.setOnClickListener((View$OnClickListener)_$$Lambda$AudioPlayerAlert$_25s4XwmMXC5vKIS0zNvR6jdxfg.INSTANCE);
        imageView.setContentDescription((CharSequence)LocaleController.getString("AccDescrPrevious", 2131558459));
        this.buttons[2] = (View)(this.playButton = new ImageView(context));
        this.playButton.setScaleType(ImageView$ScaleType.CENTER);
        this.playButton.setImageDrawable(Theme.createSimpleSelectorDrawable(context, 2131165765, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
        frameLayout.addView((View)this.playButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 51));
        this.playButton.setOnClickListener((View$OnClickListener)_$$Lambda$AudioPlayerAlert$TTY43GWhuW3FlUWxplw8ac_chYU.INSTANCE);
        final View[] buttons2 = this.buttons;
        final ImageView imageView2 = new ImageView(context);
        ((ImageView)(buttons2[3] = (View)imageView2)).setScaleType(ImageView$ScaleType.CENTER);
        imageView2.setImageDrawable(Theme.createSimpleSelectorDrawable(context, 2131165763, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
        frameLayout.addView((View)imageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 51));
        imageView2.setOnClickListener((View$OnClickListener)_$$Lambda$AudioPlayerAlert$4X94PKs6A_0UhkXhWsjRM6iEc3I.INSTANCE);
        imageView2.setContentDescription((CharSequence)LocaleController.getString("Next", 2131559911));
        this.buttons[4] = (View)(this.repeatButton = new ImageView(context));
        this.repeatButton.setScaleType(ImageView$ScaleType.CENTER);
        this.repeatButton.setPadding(0, 0, AndroidUtilities.dp(8.0f), 0);
        frameLayout.addView((View)this.repeatButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(50, 48, 51));
        this.repeatButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AudioPlayerAlert$dXFO6JRbR1ZdMBRfhd2XydhikZ0(this));
        (this.listView = new RecyclerListView(context) {
            boolean ignoreLayout;
            
            @Override
            protected boolean allowSelectChildAtPosition(final float n, final float n2) {
                return AudioPlayerAlert.this.playerLayout == null || n2 > AudioPlayerAlert.this.playerLayout.getY() + AudioPlayerAlert.this.playerLayout.getMeasuredHeight();
            }
            
            @Override
            public boolean drawChild(final Canvas canvas, final View view, final long n) {
                canvas.save();
                int measuredHeight;
                if (AudioPlayerAlert.this.actionBar != null) {
                    measuredHeight = AudioPlayerAlert.this.actionBar.getMeasuredHeight();
                }
                else {
                    measuredHeight = 0;
                }
                canvas.clipRect(0, measuredHeight + AndroidUtilities.dp(50.0f), this.getMeasuredWidth(), this.getMeasuredHeight());
                final boolean drawChild = super.drawChild(canvas, view, n);
                canvas.restore();
                return drawChild;
            }
            
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if (AudioPlayerAlert.this.searchOpenPosition != -1 && !AudioPlayerAlert.this.actionBar.isSearchFieldVisible()) {
                    this.ignoreLayout = true;
                    AudioPlayerAlert.this.layoutManager.scrollToPositionWithOffset(AudioPlayerAlert.this.searchOpenPosition, AudioPlayerAlert.this.searchOpenOffset);
                    super.onLayout(false, n, n2, n3, n4);
                    this.ignoreLayout = false;
                    AudioPlayerAlert.this.searchOpenPosition = -1;
                }
                else if (AudioPlayerAlert.this.scrollToSong) {
                    AudioPlayerAlert.this.scrollToSong = false;
                    final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject != null) {
                        final int childCount = AudioPlayerAlert.this.listView.getChildCount();
                        int i = 0;
                        while (true) {
                            while (i < childCount) {
                                final View child = AudioPlayerAlert.this.listView.getChildAt(i);
                                if (child instanceof AudioPlayerCell && ((AudioPlayerCell)child).getMessageObject() == playingMessageObject) {
                                    if (child.getBottom() > this.getMeasuredHeight()) {
                                        break;
                                    }
                                    final boolean b2 = true;
                                    if (b2) {
                                        return;
                                    }
                                    final int index = AudioPlayerAlert.this.playlist.indexOf(playingMessageObject);
                                    if (index >= 0) {
                                        this.ignoreLayout = true;
                                        if (SharedConfig.playOrderReversed) {
                                            AudioPlayerAlert.this.layoutManager.scrollToPosition(index);
                                        }
                                        else {
                                            AudioPlayerAlert.this.layoutManager.scrollToPosition(AudioPlayerAlert.this.playlist.size() - index);
                                        }
                                        super.onLayout(false, n, n2, n3, n4);
                                        this.ignoreLayout = false;
                                    }
                                    return;
                                }
                                else {
                                    ++i;
                                }
                            }
                            final boolean b2 = false;
                            continue;
                        }
                    }
                }
            }
            
            @Override
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
        this.listView.setClipToPadding(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(this.getContext(), 1, false)));
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        super.containerView.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter = new ListAdapter(context));
        this.listView.setGlowColor(Theme.getColor("dialogScrollGlow"));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)_$$Lambda$AudioPlayerAlert$48MEuBkln_g5kGMLm89eP_Rsutc.INSTANCE);
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1 && AudioPlayerAlert.this.searching && AudioPlayerAlert.this.searchWas) {
                    AndroidUtilities.hideKeyboard(AudioPlayerAlert.this.getCurrentFocus());
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                AudioPlayerAlert.this.updateLayout();
            }
        });
        this.playlist = MediaController.getInstance().getPlaylist();
        this.listAdapter.notifyDataSetChanged();
        super.containerView.addView((View)this.playerLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 178.0f));
        super.containerView.addView(this.shadow2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f));
        super.containerView.addView((View)this.placeholderImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, 51, 17.0f, 19.0f, 0.0f, 0.0f));
        super.containerView.addView(this.shadow, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f));
        super.containerView.addView((View)this.actionBar);
        this.updateTitle(false);
        this.updateRepeatButton();
        this.updateShuffleButton();
    }
    
    private void checkIfMusicDownloaded(final MessageObject messageObject) {
        final String attachPath = messageObject.messageOwner.attachPath;
        File file2;
        final File file = file2 = null;
        if (attachPath != null) {
            file2 = file;
            if (attachPath.length() > 0) {
                file2 = new File(messageObject.messageOwner.attachPath);
                if (!file2.exists()) {
                    file2 = file;
                }
            }
        }
        File pathToMessage;
        if ((pathToMessage = file2) == null) {
            pathToMessage = FileLoader.getPathToMessage(messageObject.messageOwner);
        }
        final boolean b = SharedConfig.streamMedia && (int)messageObject.getDialogId() != 0 && messageObject.isMusic();
        if (!pathToMessage.exists() && !b) {
            final String fileName = messageObject.getFileName();
            DownloadController.getInstance(super.currentAccount).addLoadingFileObserver(fileName, (DownloadController.FileDownloadProgressListener)this);
            final Float fileProgress = ImageLoader.getInstance().getFileProgress(fileName);
            final LineProgressView progressView = this.progressView;
            float floatValue;
            if (fileProgress != null) {
                floatValue = fileProgress;
            }
            else {
                floatValue = 0.0f;
            }
            progressView.setProgress(floatValue, false);
            this.progressView.setVisibility(0);
            this.seekBarView.setVisibility(4);
            this.playButton.setEnabled(false);
        }
        else {
            DownloadController.getInstance(super.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
            this.progressView.setVisibility(4);
            this.seekBarView.setVisibility(0);
            this.playButton.setEnabled(true);
        }
    }
    
    private int getCurrentTop() {
        if (this.listView.getChildCount() != 0) {
            final RecyclerListView listView = this.listView;
            final int n = 0;
            final View child = listView.getChildAt(0);
            final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findContainingViewHolder(child);
            if (holder != null) {
                final int paddingTop = this.listView.getPaddingTop();
                int top = n;
                if (((RecyclerView.ViewHolder)holder).getAdapterPosition() == 0) {
                    top = n;
                    if (child.getTop() >= 0) {
                        top = child.getTop();
                    }
                }
                return paddingTop - top;
            }
        }
        return -1000;
    }
    
    private void onSubItemClick(int n) {
        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null) {
            final LaunchActivity parentActivity = this.parentActivity;
            if (parentActivity != null) {
                if (n == 1) {
                    final int selectedAccount = UserConfig.selectedAccount;
                    n = super.currentAccount;
                    if (selectedAccount != n) {
                        parentActivity.switchToAccount(n, true);
                    }
                    final Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putInt("dialogsType", 3);
                    final DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
                    list.add(playingMessageObject);
                    dialogsActivity.setDelegate((DialogsActivity.DialogsActivityDelegate)new _$$Lambda$AudioPlayerAlert$bZwv44or3_083Y0MYIMNRo1ORcQ(this, list));
                    this.parentActivity.presentFragment(dialogsActivity);
                    this.dismiss();
                }
                else if (n == 2) {
                    try {
                        File file;
                        if (TextUtils.isEmpty((CharSequence)playingMessageObject.messageOwner.attachPath) || !(file = new File(playingMessageObject.messageOwner.attachPath)).exists()) {
                            file = null;
                        }
                        File pathToMessage;
                        if ((pathToMessage = file) == null) {
                            pathToMessage = FileLoader.getPathToMessage(playingMessageObject.messageOwner);
                        }
                        if (pathToMessage.exists()) {
                            final Intent intent = new Intent("android.intent.action.SEND");
                            if (playingMessageObject != null) {
                                intent.setType(playingMessageObject.getMimeType());
                            }
                            else {
                                intent.setType("audio/mp3");
                            }
                            n = Build$VERSION.SDK_INT;
                            if (n >= 24) {
                                try {
                                    intent.putExtra("android.intent.extra.STREAM", (Parcelable)FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.provider", pathToMessage));
                                    intent.setFlags(1);
                                }
                                catch (Exception ex2) {
                                    intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(pathToMessage));
                                }
                            }
                            else {
                                intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(pathToMessage));
                            }
                            this.parentActivity.startActivityForResult(Intent.createChooser(intent, (CharSequence)LocaleController.getString("ShareFile", 2131560748)), 500);
                        }
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.parentActivity);
                            builder.setTitle(LocaleController.getString("AppName", 2131558635));
                            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                            builder.setMessage(LocaleController.getString("PleaseDownload", 2131560454));
                            builder.show();
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                else if (n == 4) {
                    final int selectedAccount2 = UserConfig.selectedAccount;
                    n = super.currentAccount;
                    if (selectedAccount2 != n) {
                        parentActivity.switchToAccount(n, true);
                    }
                    final Bundle bundle2 = new Bundle();
                    final long dialogId = playingMessageObject.getDialogId();
                    final int n2 = (int)dialogId;
                    n = (int)(dialogId >> 32);
                    if (n2 != 0) {
                        if (n == 1) {
                            bundle2.putInt("chat_id", n2);
                        }
                        else if (n2 > 0) {
                            bundle2.putInt("user_id", n2);
                        }
                        else if (n2 < 0) {
                            final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(-n2);
                            n = n2;
                            if (chat != null) {
                                n = n2;
                                if (chat.migrated_to != null) {
                                    bundle2.putInt("migrated_to", n2);
                                    n = -chat.migrated_to.channel_id;
                                }
                            }
                            bundle2.putInt("chat_id", -n);
                        }
                    }
                    else {
                        bundle2.putInt("enc_id", n);
                    }
                    bundle2.putInt("message_id", playingMessageObject.getId());
                    NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                    this.parentActivity.presentFragment(new ChatActivity(bundle2), false, false);
                    this.dismiss();
                }
            }
        }
    }
    
    private void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            return;
        }
        final View child = this.listView.getChildAt(0);
        final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findContainingViewHolder(child);
        int top = child.getTop();
        if (top <= 0 || holder == null || ((RecyclerView.ViewHolder)holder).getAdapterPosition() != 0) {
            top = 0;
        }
        if (this.searchWas || this.searching) {
            top = 0;
        }
        if (this.scrollOffsetY != top) {
            this.listView.setTopGlowOffset(this.scrollOffsetY = top);
            this.playerLayout.setTranslationY((float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY));
            this.placeholderImageView.setTranslationY((float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY));
            this.shadow2.setTranslationY((float)(Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY) + this.playerLayout.getMeasuredHeight()));
            super.containerView.invalidate();
            if ((this.inFullSize && this.scrollOffsetY <= this.actionBar.getMeasuredHeight()) || this.searchWas) {
                if (this.actionBar.getTag() == null) {
                    final AnimatorSet actionBarAnimation = this.actionBarAnimation;
                    if (actionBarAnimation != null) {
                        actionBarAnimation.cancel();
                    }
                    this.actionBar.setTag((Object)1);
                    (this.actionBarAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, "alpha", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shadow, "alpha", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shadow2, "alpha", new float[] { 1.0f }) });
                    this.actionBarAnimation.setDuration(180L);
                    this.actionBarAnimation.start();
                }
            }
            else if (this.actionBar.getTag() != null) {
                final AnimatorSet actionBarAnimation2 = this.actionBarAnimation;
                if (actionBarAnimation2 != null) {
                    actionBarAnimation2.cancel();
                }
                this.actionBar.setTag((Object)null);
                (this.actionBarAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shadow, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.shadow2, "alpha", new float[] { 0.0f }) });
                this.actionBarAnimation.setDuration(180L);
                this.actionBarAnimation.start();
            }
        }
        this.startTranslation = (float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY);
        this.panelStartTranslation = (float)Math.max(this.actionBar.getMeasuredHeight(), this.scrollOffsetY);
    }
    
    private void updateProgress(final MessageObject messageObject) {
        final SeekBarView seekBarView = this.seekBarView;
        if (seekBarView != null) {
            if (!seekBarView.isDragging()) {
                this.seekBarView.setProgress(messageObject.audioProgress);
                this.seekBarView.setBufferedProgress(messageObject.bufferedProgress);
            }
            final int lastTime = this.lastTime;
            final int audioProgressSec = messageObject.audioProgressSec;
            if (lastTime != audioProgressSec) {
                this.lastTime = audioProgressSec;
                this.timeTextView.setText(String.format("%d:%02d", audioProgressSec / 60, messageObject.audioProgressSec % 60));
            }
        }
    }
    
    private void updateRepeatButton() {
        final int repeatMode = SharedConfig.repeatMode;
        if (repeatMode == 0) {
            this.repeatButton.setImageResource(2131165767);
            this.repeatButton.setTag((Object)"player_button");
            this.repeatButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_button"), PorterDuff$Mode.MULTIPLY));
            this.repeatButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrRepeatOff", 2131558463));
        }
        else if (repeatMode == 1) {
            this.repeatButton.setImageResource(2131165767);
            this.repeatButton.setTag((Object)"player_buttonActive");
            this.repeatButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), PorterDuff$Mode.MULTIPLY));
            this.repeatButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrRepeatList", 2131558462));
        }
        else if (repeatMode == 2) {
            this.repeatButton.setImageResource(2131165768);
            this.repeatButton.setTag((Object)"player_buttonActive");
            this.repeatButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), PorterDuff$Mode.MULTIPLY));
            this.repeatButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrRepeatOne", 2131558464));
        }
    }
    
    private void updateShuffleButton() {
        final boolean shuffleMusic = SharedConfig.shuffleMusic;
        final String s = "player_button";
        if (shuffleMusic) {
            final Drawable mutate = this.getContext().getResources().getDrawable(2131165769).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), PorterDuff$Mode.MULTIPLY));
            this.shuffleButton.setIcon(mutate);
            this.shuffleButton.setContentDescription((CharSequence)LocaleController.getString("Shuffle", 2131560784));
        }
        else {
            final Drawable mutate2 = this.getContext().getResources().getDrawable(2131165684).mutate();
            if (SharedConfig.playOrderReversed) {
                mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_buttonActive"), PorterDuff$Mode.MULTIPLY));
            }
            else {
                mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("player_button"), PorterDuff$Mode.MULTIPLY));
            }
            this.shuffleButton.setIcon(mutate2);
            this.shuffleButton.setContentDescription((CharSequence)LocaleController.getString("ReverseOrder", 2131560616));
        }
        final Drawable drawable = this.playOrderButtons[0];
        String s2;
        if (SharedConfig.playOrderReversed) {
            s2 = "player_buttonActive";
        }
        else {
            s2 = "player_button";
        }
        drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor(s2), PorterDuff$Mode.MULTIPLY));
        final Drawable drawable2 = this.playOrderButtons[1];
        String s3 = s;
        if (SharedConfig.shuffleMusic) {
            s3 = "player_buttonActive";
        }
        drawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor(s3), PorterDuff$Mode.MULTIPLY));
    }
    
    private void updateTitle(final boolean b) {
        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if ((playingMessageObject == null && b) || (playingMessageObject != null && !playingMessageObject.isMusic())) {
            this.dismiss();
        }
        else {
            if (playingMessageObject == null) {
                return;
            }
            if (playingMessageObject.eventId == 0L && playingMessageObject.getId() > -2000000000) {
                this.hasOptions = true;
                if (!this.actionBar.isSearchFieldVisible()) {
                    this.menuItem.setVisibility(0);
                }
                this.optionsButton.setVisibility(0);
            }
            else {
                this.hasOptions = false;
                this.menuItem.setVisibility(4);
                this.optionsButton.setVisibility(4);
            }
            this.checkIfMusicDownloaded(playingMessageObject);
            this.updateProgress(playingMessageObject);
            if (MediaController.getInstance().isMessagePaused()) {
                final ImageView playButton = this.playButton;
                playButton.setImageDrawable(Theme.createSimpleSelectorDrawable(playButton.getContext(), 2131165765, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
                this.playButton.setContentDescription((CharSequence)LocaleController.getString("AccActionPlay", 2131558409));
            }
            else {
                final ImageView playButton2 = this.playButton;
                playButton2.setImageDrawable(Theme.createSimpleSelectorDrawable(playButton2.getContext(), 2131165764, Theme.getColor("player_button"), Theme.getColor("player_buttonActive")));
                this.playButton.setContentDescription((CharSequence)LocaleController.getString("AccActionPause", 2131558408));
            }
            final String musicTitle = playingMessageObject.getMusicTitle();
            final String musicAuthor = playingMessageObject.getMusicAuthor();
            this.titleTextView.setText((CharSequence)musicTitle);
            this.authorTextView.setText((CharSequence)musicAuthor);
            this.actionBar.setTitle(musicTitle);
            this.actionBar.setSubtitle(musicAuthor);
            final StringBuilder sb = new StringBuilder();
            sb.append(musicAuthor);
            sb.append(" ");
            sb.append(musicTitle);
            sb.toString();
            final AudioInfo audioInfo = MediaController.getInstance().getAudioInfo();
            if (audioInfo != null && audioInfo.getCover() != null) {
                this.hasNoCover = 0;
                this.placeholderImageView.setImageBitmap(audioInfo.getCover());
            }
            else {
                final String artworkUrl = playingMessageObject.getArtworkUrl(false);
                if (!TextUtils.isEmpty((CharSequence)artworkUrl)) {
                    this.placeholderImageView.setImage(artworkUrl, null, null);
                    this.hasNoCover = 2;
                }
                else {
                    this.placeholderImageView.setImageDrawable(null);
                    this.hasNoCover = 1;
                }
                this.placeholderImageView.invalidate();
            }
            if (this.durationTextView != null) {
                final int duration = playingMessageObject.getDuration();
                final TextView durationTextView = this.durationTextView;
                String format;
                if (duration != 0) {
                    format = String.format("%d:%02d", duration / 60, duration % 60);
                }
                else {
                    format = "-:--";
                }
                durationTextView.setText((CharSequence)format);
            }
        }
    }
    
    @Override
    protected boolean canDismissWithSwipe() {
        return false;
    }
    
    @Override
    public void didReceivedNotification(int i, int n, final Object... array) {
        if (i != NotificationCenter.messagePlayingDidStart && i != NotificationCenter.messagePlayingPlayStateChanged && i != NotificationCenter.messagePlayingDidReset) {
            if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject.isMusic()) {
                    this.updateProgress(playingMessageObject);
                }
            }
            else if (i == NotificationCenter.musicDidLoad) {
                this.playlist = MediaController.getInstance().getPlaylist();
                this.listAdapter.notifyDataSetChanged();
            }
        }
        else {
            this.updateTitle(i == NotificationCenter.messagePlayingDidReset && (boolean)array[1]);
            if (i != NotificationCenter.messagePlayingDidReset && i != NotificationCenter.messagePlayingPlayStateChanged) {
                if (i == NotificationCenter.messagePlayingDidStart) {
                    if (((MessageObject)array[0]).eventId != 0L) {
                        return;
                    }
                    View child;
                    AudioPlayerCell audioPlayerCell;
                    MessageObject messageObject;
                    for (n = this.listView.getChildCount(), i = 0; i < n; ++i) {
                        child = this.listView.getChildAt(i);
                        if (child instanceof AudioPlayerCell) {
                            audioPlayerCell = (AudioPlayerCell)child;
                            messageObject = audioPlayerCell.getMessageObject();
                            if (messageObject != null && (messageObject.isVoice() || messageObject.isMusic())) {
                                audioPlayerCell.updateButtonState(false, true);
                            }
                        }
                    }
                }
            }
            else {
                View child2;
                AudioPlayerCell audioPlayerCell2;
                MessageObject messageObject2;
                for (n = this.listView.getChildCount(), i = 0; i < n; ++i) {
                    child2 = this.listView.getChildAt(i);
                    if (child2 instanceof AudioPlayerCell) {
                        audioPlayerCell2 = (AudioPlayerCell)child2;
                        messageObject2 = audioPlayerCell2.getMessageObject();
                        if (messageObject2 != null && (messageObject2.isVoice() || messageObject2.isMusic())) {
                            audioPlayerCell2.updateButtonState(false, true);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.musicDidLoad);
        DownloadController.getInstance(super.currentAccount).removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)this);
    }
    
    @Keep
    public float getFullAnimationProgress() {
        return this.fullAnimationProgress;
    }
    
    @Override
    public int getObserverTag() {
        return this.TAG;
    }
    
    public void onBackPressed() {
        final ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.isSearchFieldVisible()) {
            this.actionBar.closeSearchField();
            return;
        }
        super.onBackPressed();
    }
    
    @Override
    public void onFailedDownload(final String s, final boolean b) {
    }
    
    @Override
    public void onProgressDownload(final String s, final float n) {
        this.progressView.setProgress(n, true);
    }
    
    @Override
    public void onProgressUpload(final String s, final float n, final boolean b) {
    }
    
    @Override
    public void onSuccessDownload(final String s) {
    }
    
    @Keep
    public void setFullAnimationProgress(float scaleY) {
        this.fullAnimationProgress = scaleY;
        this.placeholderImageView.setRoundRadius(AndroidUtilities.dp((1.0f - this.fullAnimationProgress) * 20.0f));
        scaleY = this.thumbMaxScale * this.fullAnimationProgress + 1.0f;
        this.placeholderImageView.setScaleX(scaleY);
        this.placeholderImageView.setScaleY(scaleY);
        this.placeholderImageView.getTranslationY();
        this.placeholderImageView.setTranslationX(this.thumbMaxX * this.fullAnimationProgress);
        final BackupImageView placeholderImageView = this.placeholderImageView;
        scaleY = this.startTranslation;
        placeholderImageView.setTranslationY(scaleY + (this.endTranslation - scaleY) * this.fullAnimationProgress);
        final FrameLayout playerLayout = this.playerLayout;
        scaleY = this.panelStartTranslation;
        playerLayout.setTranslationY(scaleY + (this.panelEndTranslation - scaleY) * this.fullAnimationProgress);
        final View shadow2 = this.shadow2;
        scaleY = this.panelStartTranslation;
        shadow2.setTranslationY(scaleY + (this.panelEndTranslation - scaleY) * this.fullAnimationProgress + this.playerLayout.getMeasuredHeight());
        this.menuItem.setAlpha(this.fullAnimationProgress);
        this.searchItem.setAlpha(1.0f - this.fullAnimationProgress);
        this.avatarContainer.setAlpha(1.0f - this.fullAnimationProgress);
        this.actionBar.getTitleTextView().setAlpha(this.fullAnimationProgress);
        this.actionBar.getSubtitleTextView().setAlpha(this.fullAnimationProgress);
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context context;
        private ArrayList<MessageObject> searchResult;
        private Timer searchTimer;
        
        public ListAdapter(final Context context) {
            this.searchResult = new ArrayList<MessageObject>();
            this.context = context;
        }
        
        private void processSearch(final String s) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$AudioPlayerAlert$ListAdapter$uVY9bQZgLPgCaSv9pJfZ7D2na7c(this, s));
        }
        
        private void updateSearchResults(final ArrayList<MessageObject> list) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$AudioPlayerAlert$ListAdapter$q5JODj0x7FGd36CwY5UAvx5F7pI(this, list));
        }
        
        @Override
        public int getItemCount() {
            if (AudioPlayerAlert.this.searchWas) {
                return this.searchResult.size();
            }
            if (AudioPlayerAlert.this.searching) {
                return AudioPlayerAlert.this.playlist.size();
            }
            return AudioPlayerAlert.this.playlist.size() + 1;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (!AudioPlayerAlert.this.searchWas) {
                if (!AudioPlayerAlert.this.searching) {
                    if (n == 0) {
                        return 0;
                    }
                }
            }
            return 1;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return AudioPlayerAlert.this.searchWas || viewHolder.getAdapterPosition() > 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            if (viewHolder.getItemViewType() == 1) {
                final AudioPlayerCell audioPlayerCell = (AudioPlayerCell)viewHolder.itemView;
                if (AudioPlayerAlert.this.searchWas) {
                    audioPlayerCell.setMessageObject(this.searchResult.get(n));
                }
                else if (AudioPlayerAlert.this.searching) {
                    if (SharedConfig.playOrderReversed) {
                        audioPlayerCell.setMessageObject(AudioPlayerAlert.this.playlist.get(n));
                    }
                    else {
                        audioPlayerCell.setMessageObject(AudioPlayerAlert.this.playlist.get(AudioPlayerAlert.this.playlist.size() - n - 1));
                    }
                }
                else if (n > 0) {
                    if (SharedConfig.playOrderReversed) {
                        audioPlayerCell.setMessageObject(AudioPlayerAlert.this.playlist.get(n - 1));
                    }
                    else {
                        audioPlayerCell.setMessageObject(AudioPlayerAlert.this.playlist.get(AudioPlayerAlert.this.playlist.size() - n));
                    }
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            View view;
            if (n != 0) {
                view = new AudioPlayerCell(this.context);
            }
            else {
                view = new View(this.context);
                view.setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(178.0f)));
            }
            return new RecyclerListView.Holder(view);
        }
        
        public void search(final String s) {
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (s == null) {
                this.searchResult.clear();
                this.notifyDataSetChanged();
            }
            else {
                (this.searchTimer = new Timer()).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            ListAdapter.this.searchTimer.cancel();
                            ListAdapter.this.searchTimer = null;
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        ListAdapter.this.processSearch(s);
                    }
                }, 200L, 300L);
            }
        }
    }
}
