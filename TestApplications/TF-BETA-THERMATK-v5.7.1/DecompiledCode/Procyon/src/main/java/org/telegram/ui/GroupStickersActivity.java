// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Components.URLSpanNoUnderline;
import android.text.SpannableStringBuilder;
import org.telegram.ui.Cells.HeaderCell;
import android.app.Activity;
import org.telegram.messenger.MessagesStorage;
import android.widget.Toast;
import android.app.Dialog;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.StickerSetCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.graphics.Rect;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.FrameLayout;
import android.view.View$OnClickListener;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import java.util.List;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import java.util.ArrayList;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.content.SharedPreferences$Editor;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.ContextProgressView;
import android.widget.LinearLayout;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.tgnet.TLRPC;
import android.widget.ImageView;
import android.widget.EditText;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class GroupStickersActivity extends BaseFragment implements NotificationCenterDelegate
{
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
    private ListAdapter listAdapter;
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
    
    public GroupStickersActivity(final int chatId) {
        this.chatId = chatId;
    }
    
    private void resolveStickerSet() {
        if (this.listAdapter == null) {
            return;
        }
        if (this.reqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
        }
        final Runnable queryRunnable = this.queryRunnable;
        if (queryRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(queryRunnable);
            this.queryRunnable = null;
        }
        this.selectedStickerSet = null;
        if (this.usernameTextView.length() <= 0) {
            this.searching = false;
            this.searchWas = false;
            if (this.selectedStickerRow != -1) {
                this.updateRows();
            }
            return;
        }
        this.searching = true;
        this.searchWas = true;
        final String string = this.usernameTextView.getText().toString();
        final TLRPC.TL_messages_stickerSet stickerSetByName = DataQuery.getInstance(super.currentAccount).getStickerSetByName(string);
        if (stickerSetByName != null) {
            this.selectedStickerSet = stickerSetByName;
        }
        final int selectedStickerRow = this.selectedStickerRow;
        if (selectedStickerRow == -1) {
            this.updateRows();
        }
        else {
            this.listAdapter.notifyItemChanged(selectedStickerRow);
        }
        if (stickerSetByName != null) {
            this.searching = false;
            return;
        }
        AndroidUtilities.runOnUIThread(this.queryRunnable = new _$$Lambda$GroupStickersActivity$4Rqel5HptCXt4RyUWgtwIaVXIfk(this, string), 500L);
    }
    
    private void saveStickerSet() {
        final TLRPC.ChatFull info = this.info;
        Label_0232: {
            if (info != null) {
                final TLRPC.StickerSet stickerset = info.stickerset;
                if (stickerset != null) {
                    final TLRPC.TL_messages_stickerSet selectedStickerSet = this.selectedStickerSet;
                    if (selectedStickerSet != null && selectedStickerSet.set.id == stickerset.id) {
                        break Label_0232;
                    }
                }
                if (this.info.stickerset != null || this.selectedStickerSet != null) {
                    this.showEditDoneProgress(true);
                    final TLRPC.TL_channels_setStickers tl_channels_setStickers = new TLRPC.TL_channels_setStickers();
                    tl_channels_setStickers.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chatId);
                    if (this.selectedStickerSet == null) {
                        tl_channels_setStickers.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                    }
                    else {
                        final SharedPreferences$Editor edit = MessagesController.getEmojiSettings(super.currentAccount).edit();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("group_hide_stickers_");
                        sb.append(this.info.id);
                        edit.remove(sb.toString()).commit();
                        tl_channels_setStickers.stickerset = new TLRPC.TL_inputStickerSetID();
                        final TLRPC.InputStickerSet stickerset2 = tl_channels_setStickers.stickerset;
                        final TLRPC.StickerSet set = this.selectedStickerSet.set;
                        stickerset2.id = set.id;
                        stickerset2.access_hash = set.access_hash;
                    }
                    ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_setStickers, new _$$Lambda$GroupStickersActivity$SODwEnuUWhbL0_5dEEd38j4JKu8(this));
                    return;
                }
            }
        }
        this.finishFragment();
    }
    
    private void showEditDoneProgress(final boolean b) {
        if (this.doneItem == null) {
            return;
        }
        final AnimatorSet doneItemAnimation = this.doneItemAnimation;
        if (doneItemAnimation != null) {
            doneItemAnimation.cancel();
        }
        this.doneItemAnimation = new AnimatorSet();
        if (b) {
            this.progressView.setVisibility(0);
            this.doneItem.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 1.0f }) });
        }
        else {
            this.doneItem.getImageView().setVisibility(0);
            this.doneItem.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 1.0f }) });
        }
        this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                if (GroupStickersActivity.this.doneItemAnimation != null && GroupStickersActivity.this.doneItemAnimation.equals(obj)) {
                    GroupStickersActivity.this.doneItemAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                if (GroupStickersActivity.this.doneItemAnimation != null && GroupStickersActivity.this.doneItemAnimation.equals(obj)) {
                    if (!b) {
                        GroupStickersActivity.this.progressView.setVisibility(4);
                    }
                    else {
                        GroupStickersActivity.this.doneItem.getImageView().setVisibility(4);
                    }
                }
            }
        });
        this.doneItemAnimation.setDuration(150L);
        this.doneItemAnimation.start();
    }
    
    private void updateRows() {
        this.rowCount = 0;
        this.nameRow = this.rowCount++;
        if (this.selectedStickerSet == null && !this.searchWas) {
            this.selectedStickerRow = -1;
        }
        else {
            this.selectedStickerRow = this.rowCount++;
        }
        this.infoRow = this.rowCount++;
        final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(super.currentAccount).getStickerSets(0);
        if (!stickerSets.isEmpty()) {
            this.headerRow = this.rowCount++;
            final int rowCount = this.rowCount;
            this.stickersStartRow = rowCount;
            this.stickersEndRow = rowCount + stickerSets.size();
            this.rowCount += stickerSets.size();
            this.stickersShadowRow = this.rowCount++;
        }
        else {
            this.headerRow = -1;
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersShadowRow = -1;
        }
        final LinearLayout nameContainer = this.nameContainer;
        if (nameContainer != null) {
            nameContainer.invalidate();
        }
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("GroupStickers", 2131559615));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    GroupStickersActivity.this.finishFragment();
                }
                else if (n == 1) {
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
        this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        (this.progressView = new ContextProgressView(context, 1)).setAlpha(0.0f);
        this.progressView.setScaleX(0.1f);
        this.progressView.setScaleY(0.1f);
        this.progressView.setVisibility(4);
        this.doneItem.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.nameContainer = new LinearLayout(context) {
            protected void onDraw(final Canvas canvas) {
                if (GroupStickersActivity.this.selectedStickerSet != null) {
                    canvas.drawLine(0.0f, (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
                }
            }
            
            protected void onMeasure(final int n, final int n2) {
                super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
            }
        }).setWeightSum(1.0f);
        this.nameContainer.setWillNotDraw(false);
        this.nameContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.nameContainer.setOrientation(0);
        this.nameContainer.setPadding(AndroidUtilities.dp(17.0f), 0, AndroidUtilities.dp(14.0f), 0);
        this.editText = new EditText(context);
        final EditText editText = this.editText;
        final StringBuilder sb = new StringBuilder();
        sb.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
        sb.append("/addstickers/");
        editText.setText((CharSequence)sb.toString());
        this.editText.setTextSize(1, 17.0f);
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
        this.nameContainer.addView((View)this.editText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 42));
        (this.usernameTextView = new EditTextBoldCursor(context)).setTextSize(1, 17.0f);
        this.usernameTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.usernameTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        this.usernameTextView.setCursorWidth(1.5f);
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
        this.usernameTextView.setHint((CharSequence)LocaleController.getString("ChooseStickerSetPlaceholder", 2131559096));
        this.usernameTextView.addTextChangedListener((TextWatcher)new TextWatcher() {
            boolean ignoreTextChange;
            
            public void afterTextChanged(final Editable editable) {
                if (GroupStickersActivity.this.eraseImageView != null) {
                    final ImageView access$500 = GroupStickersActivity.this.eraseImageView;
                    int visibility;
                    if (editable.length() > 0) {
                        visibility = 0;
                    }
                    else {
                        visibility = 4;
                    }
                    access$500.setVisibility(visibility);
                }
                if (this.ignoreTextChange) {
                    return;
                }
                if (GroupStickersActivity.this.ignoreTextChanges) {
                    return;
                }
                Label_0167: {
                    if (editable.length() <= 5) {
                        break Label_0167;
                    }
                    this.ignoreTextChange = true;
                    while (true) {
                        try {
                            final Uri parse = Uri.parse(editable.toString());
                            if (parse != null) {
                                final List pathSegments = parse.getPathSegments();
                                if (pathSegments.size() == 2 && pathSegments.get(0).toLowerCase().equals("addstickers")) {
                                    GroupStickersActivity.this.usernameTextView.setText((CharSequence)pathSegments.get(1));
                                    GroupStickersActivity.this.usernameTextView.setSelection(GroupStickersActivity.this.usernameTextView.length());
                                }
                            }
                            this.ignoreTextChange = false;
                            GroupStickersActivity.this.resolveStickerSet();
                        }
                        catch (Exception ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.nameContainer.addView((View)this.usernameTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, 42, 1.0f));
        (this.eraseImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.eraseImageView.setImageResource(2131165437);
        this.eraseImageView.setPadding(AndroidUtilities.dp(16.0f), 0, 0, 0);
        this.eraseImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), PorterDuff$Mode.MULTIPLY));
        this.eraseImageView.setVisibility(4);
        this.eraseImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$GroupStickersActivity$0gmuCbw_2WYqittTzpHTcYQ1s5I(this));
        this.nameContainer.addView((View)this.eraseImageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(42, 42, 0.0f));
        final TLRPC.ChatFull info = this.info;
        if (info != null) {
            final TLRPC.StickerSet stickerset = info.stickerset;
            if (stickerset != null) {
                this.ignoreTextChanges = true;
                this.usernameTextView.setText((CharSequence)stickerset.short_name);
                final EditTextBoldCursor usernameTextView = this.usernameTextView;
                usernameTextView.setSelection(usernameTextView.length());
                this.ignoreTextChanges = false;
            }
        }
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.listView = new RecyclerListView(context)).setFocusable(true);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        (this.layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean requestChildRectangleOnScreen(final RecyclerView recyclerView, final View view, final Rect rect, final boolean b, final boolean b2) {
                return false;
            }
            
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }).setOrientation(1);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)this.layoutManager);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$GroupStickersActivity$cr2TDaX3YsSRoO_CaDIAKHsP5zc(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1) {
                    AndroidUtilities.hideKeyboard(GroupStickersActivity.this.getParentActivity().getCurrentFocus());
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.stickersDidLoad) {
            if ((int)array[0] == 0) {
                this.updateRows();
            }
        }
        else if (n == NotificationCenter.chatInfoDidLoad) {
            final TLRPC.ChatFull info = (TLRPC.ChatFull)array[0];
            if (info.id == this.chatId) {
                if (this.info == null && info.stickerset != null) {
                    this.selectedStickerSet = DataQuery.getInstance(super.currentAccount).getGroupStickerSetById(info.stickerset);
                }
                this.info = info;
                this.updateRows();
            }
        }
        else if (n == NotificationCenter.groupStickersDidLoad) {
            (long)array[0];
            final TLRPC.ChatFull info2 = this.info;
            if (info2 != null) {
                final TLRPC.StickerSet stickerset = info2.stickerset;
                if (stickerset != null && stickerset.id == n) {
                    this.updateRows();
                }
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { StickerSetCell.class, TextSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.usernameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.usernameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteLinkText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.nameContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.listView, 0, new Class[] { StickerSetCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { StickerSetCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[] { StickerSetCell.class }, new String[] { "optionsButton" }, null, null, null, "stickers_menuSelector"), new ThemeDescription((View)this.listView, 0, new Class[] { StickerSetCell.class }, new String[] { "optionsButton" }, null, null, null, "stickers_menu") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DataQuery.getInstance(super.currentAccount).checkStickers(0);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
        this.updateRows();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            this.usernameTextView.requestFocus();
            AndroidUtilities.showKeyboard((View)this.usernameTextView);
        }
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$GroupStickersActivity$r9HzrnDTDEDApQN3gt_U8hwAtPA(this), 100L);
        }
    }
    
    public void setInfo(TLRPC.ChatFull info) {
        this.info = info;
        info = this.info;
        if (info != null && info.stickerset != null) {
            this.selectedStickerSet = DataQuery.getInstance(super.currentAccount).getGroupStickerSetById(this.info.stickerset);
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return GroupStickersActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n >= GroupStickersActivity.this.stickersStartRow && n < GroupStickersActivity.this.stickersEndRow) {
                return 0;
            }
            if (n == GroupStickersActivity.this.infoRow) {
                return 1;
            }
            if (n == GroupStickersActivity.this.nameRow) {
                return 2;
            }
            if (n == GroupStickersActivity.this.stickersShadowRow) {
                return 3;
            }
            if (n == GroupStickersActivity.this.headerRow) {
                return 4;
            }
            if (n == GroupStickersActivity.this.selectedStickerRow) {
                return 5;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 2 || itemViewType == 5;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int index) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = true;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 4) {
                        if (itemViewType == 5) {
                            final StickerSetCell stickerSetCell = (StickerSetCell)viewHolder.itemView;
                            if (GroupStickersActivity.this.selectedStickerSet != null) {
                                stickerSetCell.setStickersSet(GroupStickersActivity.this.selectedStickerSet, false);
                            }
                            else if (GroupStickersActivity.this.searching) {
                                stickerSetCell.setText(LocaleController.getString("Loading", 2131559768), null, 0, false);
                            }
                            else {
                                stickerSetCell.setText(LocaleController.getString("ChooseStickerSetNotFound", 2131559094), LocaleController.getString("ChooseStickerSetNotFoundInfo", 2131559095), 2131165471, false);
                            }
                        }
                    }
                    else {
                        ((HeaderCell)viewHolder.itemView).setText(LocaleController.getString("ChooseFromYourStickers", 2131559089));
                    }
                }
                else if (index == GroupStickersActivity.this.infoRow) {
                    final String string = LocaleController.getString("ChooseStickerSetMy", 2131559093);
                    index = string.indexOf("@stickers");
                    if (index != -1) {
                        try {
                            final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)string);
                            text.setSpan((Object)new URLSpanNoUnderline("@stickers") {
                                @Override
                                public void onClick(final View view) {
                                    MessagesController.getInstance(GroupStickersActivity.this.currentAccount).openByUserName("stickers", GroupStickersActivity.this, 1);
                                }
                            }, index, index + 9, 18);
                            ((TextInfoPrivacyCell)viewHolder.itemView).setText((CharSequence)text);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                            ((TextInfoPrivacyCell)viewHolder.itemView).setText(string);
                        }
                    }
                    else {
                        ((TextInfoPrivacyCell)viewHolder.itemView).setText(string);
                    }
                }
            }
            else {
                final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(GroupStickersActivity.this.currentAccount).getStickerSets(0);
                index -= GroupStickersActivity.this.stickersStartRow;
                final StickerSetCell stickerSetCell2 = (StickerSetCell)viewHolder.itemView;
                final TLRPC.TL_messages_stickerSet set = stickerSets.get(index);
                stickerSetCell2.setStickersSet(stickerSets.get(index), index != stickerSets.size() - 1);
                long n;
                if (GroupStickersActivity.this.selectedStickerSet != null) {
                    n = GroupStickersActivity.this.selectedStickerSet.set.id;
                }
                else if (GroupStickersActivity.this.info != null && GroupStickersActivity.this.info.stickerset != null) {
                    n = GroupStickersActivity.this.info.stickerset.id;
                }
                else {
                    n = 0L;
                }
                stickerSetCell2.setChecked(set.set.id == n && b);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int n) {
            final int n2 = 3;
            Object access$1800 = null;
            Label_0166: {
                if (n != 0) {
                    if (n == 1) {
                        access$1800 = new TextInfoPrivacyCell(this.mContext);
                        ((View)access$1800).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        break Label_0166;
                    }
                    if (n == 2) {
                        access$1800 = GroupStickersActivity.this.nameContainer;
                        break Label_0166;
                    }
                    if (n == 3) {
                        access$1800 = new ShadowSectionCell(this.mContext);
                        ((View)access$1800).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        break Label_0166;
                    }
                    if (n == 4) {
                        access$1800 = new HeaderCell(this.mContext);
                        ((View)access$1800).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        break Label_0166;
                    }
                    if (n != 5) {
                        access$1800 = null;
                        break Label_0166;
                    }
                }
                final Context mContext = this.mContext;
                if (n == 0) {
                    n = n2;
                }
                else {
                    n = 2;
                }
                access$1800 = new StickerSetCell(mContext, n);
                ((View)access$1800).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            ((View)access$1800).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)access$1800);
        }
    }
}
