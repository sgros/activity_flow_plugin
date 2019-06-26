// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import java.util.Collection;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.StickerSetGroupInfoCell;
import android.content.SharedPreferences$Editor;
import android.util.SparseArray;
import android.view.ViewPropertyAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.MessagesStorage;
import java.util.HashMap;
import java.util.Arrays;
import org.telegram.tgnet.RequestDelegate;
import android.content.DialogInterface$OnCancelListener;
import org.telegram.tgnet.ConnectionsManager;
import android.content.DialogInterface;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.AlertDialog;
import android.widget.LinearLayout;
import org.telegram.ui.ActionBar.BottomSheet;
import android.view.ViewTreeObserver;
import android.database.DataSetObserver;
import android.view.ViewGroup;
import android.util.SparseIntArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.view.KeyEvent;
import android.text.TextUtils;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Cells.StickerSetNameCell;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Cells.FeaturedStickerSetInfoCell;
import org.telegram.tgnet.TLObject;
import android.content.SharedPreferences;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Cells.ContextLinkCell;
import android.util.Property;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import org.telegram.messenger.MessagesController;
import android.view.View$OnKeyListener;
import android.annotation.SuppressLint;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import androidx.viewpager.widget.PagerAdapter;
import android.view.animation.LayoutAnimationController;
import android.view.ViewConfiguration;
import android.view.VelocityTracker;
import android.view.View$OnTouchListener;
import android.graphics.Rect;
import org.telegram.messenger.FileLoader;
import android.view.View$OnFocusChangeListener;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.EmojiData;
import android.view.ViewGroup$LayoutParams;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.Emoji;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.annotation.TargetApi;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.os.Build$VERSION;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ContentPreviewViewer$ContentPreviewViewerDelegate$_CC;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.widget.PopupWindow;
import androidx.viewpager.widget.ViewPager;
import android.widget.TextView;
import android.util.LongSparseArray;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import androidx.recyclerview.widget.GridLayoutManager;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ContentPreviewViewer;
import android.view.View;
import android.animation.AnimatorSet;
import android.widget.ImageView;
import java.lang.reflect.Field;
import android.view.ViewTreeObserver$OnScrollChangedListener;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class EmojiView extends FrameLayout implements NotificationCenterDelegate
{
    private static final ViewTreeObserver$OnScrollChangedListener NOP;
    private static final Field superListenerField;
    private ImageView backspaceButton;
    private AnimatorSet backspaceButtonAnimation;
    private boolean backspaceOnce;
    private boolean backspacePressed;
    private FrameLayout bottomTabContainer;
    private AnimatorSet bottomTabContainerAnimation;
    private View bottomTabContainerBackground;
    private ContentPreviewViewer.ContentPreviewViewerDelegate contentPreviewViewerDelegate;
    private int currentAccount;
    private int currentBackgroundType;
    private int currentChatId;
    private int currentPage;
    private EmojiViewDelegate delegate;
    private Paint dotPaint;
    private DragListener dragListener;
    private EmojiGridAdapter emojiAdapter;
    private FrameLayout emojiContainer;
    private RecyclerListView emojiGridView;
    private Drawable[] emojiIcons;
    private float emojiLastX;
    private float emojiLastY;
    private GridLayoutManager emojiLayoutManager;
    private int emojiMinusDy;
    private EmojiSearchAdapter emojiSearchAdapter;
    private SearchField emojiSearchField;
    private int emojiSize;
    private AnimatorSet emojiTabShadowAnimator;
    private ScrollSlidingTabStrip emojiTabs;
    private View emojiTabsShadow;
    private String[] emojiTitles;
    private ImageViewEmoji emojiTouchedView;
    private float emojiTouchedX;
    private float emojiTouchedY;
    private int favTabBum;
    private ArrayList<TLRPC.Document> favouriteStickers;
    private int featuredStickersHash;
    private boolean firstEmojiAttach;
    private boolean firstGifAttach;
    private boolean firstStickersAttach;
    private ImageView floatingButton;
    private boolean forseMultiwindowLayout;
    private GifAdapter gifAdapter;
    private FrameLayout gifContainer;
    private RecyclerListView gifGridView;
    private ExtendedGridLayoutManager gifLayoutManager;
    private RecyclerListView.OnItemClickListener gifOnItemClickListener;
    private GifSearchAdapter gifSearchAdapter;
    private SearchField gifSearchField;
    private int groupStickerPackNum;
    private int groupStickerPackPosition;
    private TLRPC.TL_messages_stickerSet groupStickerSet;
    private boolean groupStickersHidden;
    private int hasRecentEmoji;
    private TLRPC.ChatFull info;
    private LongSparseArray<TLRPC.StickerSetCovered> installingStickerSets;
    private boolean isLayout;
    private float lastBottomScrollDy;
    private int lastNotifyHeight;
    private int lastNotifyHeight2;
    private int lastNotifyWidth;
    private String[] lastSearchKeyboardLanguage;
    private int[] location;
    private TextView mediaBanTooltip;
    private boolean needEmojiSearch;
    private Object outlineProvider;
    private ViewPager pager;
    private EmojiColorPickerView pickerView;
    private EmojiPopupWindow pickerViewPopup;
    private int popupHeight;
    private int popupWidth;
    private ArrayList<TLRPC.Document> recentGifs;
    private ArrayList<TLRPC.Document> recentStickers;
    private int recentTabBum;
    private LongSparseArray<TLRPC.StickerSetCovered> removingStickerSets;
    private int scrolledToTrending;
    private AnimatorSet searchAnimation;
    private ImageView searchButton;
    private int searchFieldHeight;
    private View shadowLine;
    private boolean showGifs;
    private Drawable[] stickerIcons;
    private ArrayList<TLRPC.TL_messages_stickerSet> stickerSets;
    private ImageView stickerSettingsButton;
    private AnimatorSet stickersButtonAnimation;
    private FrameLayout stickersContainer;
    private TextView stickersCounter;
    private StickersGridAdapter stickersGridAdapter;
    private RecyclerListView stickersGridView;
    private GridLayoutManager stickersLayoutManager;
    private int stickersMinusDy;
    private RecyclerListView.OnItemClickListener stickersOnItemClickListener;
    private SearchField stickersSearchField;
    private StickersSearchGridAdapter stickersSearchGridAdapter;
    private ScrollSlidingTabStrip stickersTab;
    private int stickersTabOffset;
    private Drawable[] tabIcons;
    private View topShadow;
    private TrendingGridAdapter trendingGridAdapter;
    private RecyclerListView trendingGridView;
    private GridLayoutManager trendingLayoutManager;
    private boolean trendingLoaded;
    private int trendingTabNum;
    private PagerSlidingTabStrip typeTabs;
    private ArrayList<View> views;
    
    static {
        Field declaredField = null;
        while (true) {
            try {
                final Field field = declaredField = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
                field.setAccessible(true);
                declaredField = field;
                superListenerField = declaredField;
                NOP = (ViewTreeObserver$OnScrollChangedListener)_$$Lambda$EmojiView$PY_mfpY1F_JSo9ExUvlHDRxz4bQ.INSTANCE;
            }
            catch (NoSuchFieldException ex) {
                continue;
            }
            break;
        }
    }
    
    public EmojiView(final boolean b, final boolean showGifs, final Context context, final boolean needEmojiSearch, final TLRPC.ChatFull info) {
        super(context);
        this.views = new ArrayList<View>();
        this.firstEmojiAttach = true;
        this.hasRecentEmoji = -1;
        this.firstGifAttach = true;
        this.firstStickersAttach = true;
        this.currentAccount = UserConfig.selectedAccount;
        this.stickerSets = new ArrayList<TLRPC.TL_messages_stickerSet>();
        this.recentGifs = new ArrayList<TLRPC.Document>();
        this.recentStickers = new ArrayList<TLRPC.Document>();
        this.favouriteStickers = new ArrayList<TLRPC.Document>();
        this.installingStickerSets = (LongSparseArray<TLRPC.StickerSetCovered>)new LongSparseArray();
        this.removingStickerSets = (LongSparseArray<TLRPC.StickerSetCovered>)new LongSparseArray();
        this.location = new int[2];
        this.recentTabBum = -2;
        this.favTabBum = -2;
        this.trendingTabNum = -2;
        this.currentBackgroundType = -1;
        this.contentPreviewViewerDelegate = new ContentPreviewViewer.ContentPreviewViewerDelegate() {
            @Override
            public void gifAddedOrDeleted() {
                final EmojiView this$0 = EmojiView.this;
                this$0.recentGifs = DataQuery.getInstance(this$0.currentAccount).getRecentGifs();
                if (EmojiView.this.gifAdapter != null) {
                    EmojiView.this.gifAdapter.notifyDataSetChanged();
                }
            }
            
            @Override
            public boolean needSend() {
                return true;
            }
            
            @Override
            public void openSet(final TLRPC.InputStickerSet set, final boolean b) {
                if (set == null) {
                    return;
                }
                EmojiView.this.delegate.onShowStickerSet(null, set);
            }
            
            @Override
            public void sendGif(final Object o) {
                if (EmojiView.this.gifGridView.getAdapter() == EmojiView.this.gifAdapter) {
                    EmojiView.this.delegate.onGifSelected(o, "gif");
                }
                else if (EmojiView.this.gifGridView.getAdapter() == EmojiView.this.gifSearchAdapter) {
                    EmojiView.this.delegate.onGifSelected(o, EmojiView.this.gifSearchAdapter.bot);
                }
            }
            
            @Override
            public void sendSticker(final TLRPC.Document document, final Object o) {
                EmojiView.this.delegate.onStickerSelected(document, o);
            }
        };
        this.searchFieldHeight = AndroidUtilities.dp(64.0f);
        this.needEmojiSearch = needEmojiSearch;
        this.tabIcons = new Drawable[] { Theme.createEmojiIconSelectorDrawable(context, 2131165852, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(context, 2131165849, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(context, 2131165853, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")) };
        final Drawable emojiIconSelectorDrawable = Theme.createEmojiIconSelectorDrawable(context, 2131165843, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
        final Drawable emojiIconSelectorDrawable2 = Theme.createEmojiIconSelectorDrawable(context, 2131165844, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
        final Drawable emojiIconSelectorDrawable3 = Theme.createEmojiIconSelectorDrawable(context, 2131165836, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
        final Drawable emojiIconSelectorDrawable4 = Theme.createEmojiIconSelectorDrawable(context, 2131165839, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
        final Drawable emojiIconSelectorDrawable5 = Theme.createEmojiIconSelectorDrawable(context, 2131165835, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
        final Drawable emojiIconSelectorDrawable6 = Theme.createEmojiIconSelectorDrawable(context, 2131165845, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
        final int n = 5;
        this.emojiIcons = new Drawable[] { emojiIconSelectorDrawable, emojiIconSelectorDrawable2, emojiIconSelectorDrawable3, emojiIconSelectorDrawable4, emojiIconSelectorDrawable5, emojiIconSelectorDrawable6, Theme.createEmojiIconSelectorDrawable(context, 2131165840, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(context, 2131165841, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(context, 2131165838, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")) };
        this.stickerIcons = new Drawable[] { Theme.createEmojiIconSelectorDrawable(context, 2131165843, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(context, 2131165837, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(context, 2131165846, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")) };
        this.emojiTitles = new String[] { LocaleController.getString("Emoji1", 2131559332), LocaleController.getString("Emoji2", 2131559333), LocaleController.getString("Emoji3", 2131559334), LocaleController.getString("Emoji4", 2131559335), LocaleController.getString("Emoji5", 2131559336), LocaleController.getString("Emoji6", 2131559337), LocaleController.getString("Emoji7", 2131559338), LocaleController.getString("Emoji8", 2131559339) };
        this.showGifs = showGifs;
        this.info = info;
        (this.dotPaint = new Paint(1)).setColor(Theme.getColor("chat_emojiPanelNewTrending"));
        if (Build$VERSION.SDK_INT >= 21) {
            this.outlineProvider = new ViewOutlineProvider() {
                @TargetApi(21)
                public void getOutline(final View view, final Outline outline) {
                    outline.setRoundRect(view.getPaddingLeft(), view.getPaddingTop(), view.getMeasuredWidth() - view.getPaddingRight(), view.getMeasuredHeight() - view.getPaddingBottom(), (float)AndroidUtilities.dp(6.0f));
                }
            };
        }
        this.emojiContainer = new FrameLayout(context);
        this.views.add((View)this.emojiContainer);
        (this.emojiGridView = new RecyclerListView(context) {
            private boolean ignoreLayout;
            
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                if (EmojiView.this.needEmojiSearch && EmojiView.this.firstEmojiAttach) {
                    this.ignoreLayout = true;
                    EmojiView.this.emojiLayoutManager.scrollToPositionWithOffset(1, 0);
                    EmojiView.this.firstEmojiAttach = false;
                    this.ignoreLayout = false;
                }
                super.onLayout(b, n, n2, n3, n4);
                EmojiView.this.checkEmojiSearchFieldScroll(true);
            }
            
            @Override
            protected void onMeasure(final int n, final int n2) {
                this.ignoreLayout = true;
                final int size = View$MeasureSpec.getSize(n);
                final GridLayoutManager access$2100 = EmojiView.this.emojiLayoutManager;
                float n3;
                if (AndroidUtilities.isTablet()) {
                    n3 = 60.0f;
                }
                else {
                    n3 = 45.0f;
                }
                access$2100.setSpanCount(size / AndroidUtilities.dp(n3));
                this.ignoreLayout = false;
                super.onMeasure(n, n2);
            }
            
            @Override
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (EmojiView.this.emojiTouchedView != null) {
                    if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
                        if (motionEvent.getAction() == 2) {
                            boolean b = false;
                            Label_0130: {
                                if (EmojiView.this.emojiTouchedX != -10000.0f) {
                                    if (Math.abs(EmojiView.this.emojiTouchedX - motionEvent.getX()) <= AndroidUtilities.getPixelsInCM(0.2f, true) && Math.abs(EmojiView.this.emojiTouchedY - motionEvent.getY()) <= AndroidUtilities.getPixelsInCM(0.2f, false)) {
                                        b = true;
                                        break Label_0130;
                                    }
                                    EmojiView.this.emojiTouchedX = -10000.0f;
                                    EmojiView.this.emojiTouchedY = -10000.0f;
                                }
                                b = false;
                            }
                            if (!b) {
                                this.getLocationOnScreen(EmojiView.this.location);
                                final float n = (float)EmojiView.this.location[0];
                                final float x = motionEvent.getX();
                                EmojiView.this.pickerView.getLocationOnScreen(EmojiView.this.location);
                                final int n2 = (int)((n + x - (EmojiView.this.location[0] + AndroidUtilities.dp(3.0f))) / (EmojiView.this.emojiSize + AndroidUtilities.dp(4.0f)));
                                int selection;
                                if (n2 < 0) {
                                    selection = 0;
                                }
                                else if ((selection = n2) > 5) {
                                    selection = 5;
                                }
                                EmojiView.this.pickerView.setSelection(selection);
                            }
                        }
                    }
                    else {
                        if (EmojiView.this.pickerViewPopup != null && EmojiView.this.pickerViewPopup.isShowing()) {
                            EmojiView.this.pickerViewPopup.dismiss();
                            final int selection2 = EmojiView.this.pickerView.getSelection();
                            String value;
                            if (selection2 != 1) {
                                if (selection2 != 2) {
                                    if (selection2 != 3) {
                                        if (selection2 != 4) {
                                            if (selection2 != 5) {
                                                value = null;
                                            }
                                            else {
                                                value = "\ud83c\udfff";
                                            }
                                        }
                                        else {
                                            value = "\ud83c\udffe";
                                        }
                                    }
                                    else {
                                        value = "\ud83c\udffd";
                                    }
                                }
                                else {
                                    value = "\ud83c\udffc";
                                }
                            }
                            else {
                                value = "\ud83c\udffb";
                            }
                            final String s = (String)EmojiView.this.emojiTouchedView.getTag();
                            if (!EmojiView.this.emojiTouchedView.isRecent) {
                                String access$1700;
                                if (value != null) {
                                    Emoji.emojiColor.put(s, value);
                                    access$1700 = addColorToCode(s, value);
                                }
                                else {
                                    Emoji.emojiColor.remove(s);
                                    access$1700 = s;
                                }
                                EmojiView.this.emojiTouchedView.setImageDrawable(Emoji.getEmojiBigDrawable(access$1700), EmojiView.this.emojiTouchedView.isRecent);
                                EmojiView.this.emojiTouchedView.sendEmoji(null);
                                Emoji.saveEmojiColors();
                            }
                            else {
                                final String replace = s.replace("\ud83c\udffb", "").replace("\ud83c\udffc", "").replace("\ud83c\udffd", "").replace("\ud83c\udffe", "").replace("\ud83c\udfff", "");
                                if (value != null) {
                                    EmojiView.this.emojiTouchedView.sendEmoji(addColorToCode(replace, value));
                                }
                                else {
                                    EmojiView.this.emojiTouchedView.sendEmoji(replace);
                                }
                            }
                        }
                        EmojiView.this.emojiTouchedView = null;
                        EmojiView.this.emojiTouchedX = -10000.0f;
                        EmojiView.this.emojiTouchedY = -10000.0f;
                    }
                    return true;
                }
                EmojiView.this.emojiLastX = motionEvent.getX();
                EmojiView.this.emojiLastY = motionEvent.getY();
                return super.onTouchEvent(motionEvent);
            }
            
            @Override
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setInstantClick(true);
        this.emojiGridView.setLayoutManager((RecyclerView.LayoutManager)(this.emojiLayoutManager = new GridLayoutManager(context, 8)));
        this.emojiGridView.setTopGlowOffset(AndroidUtilities.dp(38.0f));
        this.emojiGridView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
        this.emojiGridView.setPadding(0, AndroidUtilities.dp(38.0f), 0, 0);
        this.emojiGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
        this.emojiGridView.setClipToPadding(false);
        this.emojiLayoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int n) {
                if (EmojiView.this.emojiGridView.getAdapter() == EmojiView.this.emojiSearchAdapter) {
                    if (n == 0 || (n == 1 && EmojiView.this.emojiSearchAdapter.searchWas && EmojiView.this.emojiSearchAdapter.result.isEmpty())) {
                        return EmojiView.this.emojiLayoutManager.getSpanCount();
                    }
                }
                else if ((EmojiView.this.needEmojiSearch && n == 0) || EmojiView.this.emojiAdapter.positionToSection.indexOfKey(n) >= 0) {
                    return EmojiView.this.emojiLayoutManager.getSpanCount();
                }
                return 1;
            }
        });
        this.emojiGridView.setAdapter(this.emojiAdapter = new EmojiGridAdapter());
        this.emojiSearchAdapter = new EmojiSearchAdapter();
        this.emojiContainer.addView((View)this.emojiGridView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.emojiGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1 && EmojiView.this.emojiSearchField != null) {
                    EmojiView.this.emojiSearchField.hideKeyboard();
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, int n, final int n2) {
                final int firstVisibleItemPosition = EmojiView.this.emojiLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != -1) {
                    n = Emoji.recentEmoji.size() + (EmojiView.this.needEmojiSearch ? 1 : 0);
                    Label_0095: {
                        if (firstVisibleItemPosition >= n) {
                            int n3 = 0;
                            while (true) {
                                final String[][] dataColored = EmojiData.dataColored;
                                if (n3 >= dataColored.length) {
                                    break;
                                }
                                n += dataColored[n3].length + 1;
                                if (firstVisibleItemPosition < n) {
                                    n = ((Emoji.recentEmoji.isEmpty() ^ true) ? 1 : 0) + n3;
                                    break Label_0095;
                                }
                                ++n3;
                            }
                        }
                        n = 0;
                    }
                    EmojiView.this.emojiTabs.onPageScrolled(n, 0);
                }
                EmojiView.this.checkEmojiTabY((View)recyclerView, n2);
                EmojiView.this.checkEmojiSearchFieldScroll(false);
                EmojiView.this.checkBottomTabScroll((float)n2);
            }
        });
        this.emojiGridView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new RecyclerListView.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int n) {
                if (view instanceof ImageViewEmoji) {
                    ((ImageViewEmoji)view).sendEmoji(null);
                }
            }
        });
        this.emojiGridView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new RecyclerListView.OnItemLongClickListener() {
            @Override
            public boolean onItemClick(final View view, int top) {
                final boolean b = view instanceof ImageViewEmoji;
                final int n = 0;
                if (b) {
                    final ImageViewEmoji imageViewEmoji = (ImageViewEmoji)view;
                    final String s = (String)imageViewEmoji.getTag();
                    String s2 = null;
                    String replace = s.replace("\ud83c\udffb", "");
                    if (replace != s) {
                        s2 = "\ud83c\udffb";
                    }
                    String s3;
                    if ((s3 = s2) == null) {
                        final String replace2 = s.replace("\ud83c\udffc", "");
                        s3 = s2;
                        if ((replace = replace2) != s) {
                            s3 = "\ud83c\udffc";
                            replace = replace2;
                        }
                    }
                    String s4;
                    if ((s4 = s3) == null) {
                        final String replace3 = s.replace("\ud83c\udffd", "");
                        s4 = s3;
                        if ((replace = replace3) != s) {
                            s4 = "\ud83c\udffd";
                            replace = replace3;
                        }
                    }
                    String s5;
                    if ((s5 = s4) == null) {
                        final String replace4 = s.replace("\ud83c\udffe", "");
                        s5 = s4;
                        if ((replace = replace4) != s) {
                            s5 = "\ud83c\udffe";
                            replace = replace4;
                        }
                    }
                    String s6;
                    if ((s6 = s5) == null) {
                        final String replace5 = s.replace("\ud83c\udfff", "");
                        s6 = s5;
                        if ((replace = replace5) != s) {
                            s6 = "\ud83c\udfff";
                            replace = replace5;
                        }
                    }
                    if (EmojiData.emojiColoredMap.containsKey(replace)) {
                        EmojiView.this.emojiTouchedView = imageViewEmoji;
                        final EmojiView this$0 = EmojiView.this;
                        this$0.emojiTouchedX = this$0.emojiLastX;
                        final EmojiView this$2 = EmojiView.this;
                        this$2.emojiTouchedY = this$2.emojiLastY;
                        String s7;
                        if ((s7 = s6) == null) {
                            s7 = s6;
                            if (!imageViewEmoji.isRecent) {
                                s7 = Emoji.emojiColor.get(replace);
                            }
                        }
                        final int n2 = 5;
                        if (s7 != null) {
                            Label_0440: {
                                switch (s7.hashCode()) {
                                    case 1773379: {
                                        if (s7.equals("\ud83c\udfff")) {
                                            top = 4;
                                            break Label_0440;
                                        }
                                        break;
                                    }
                                    case 1773378: {
                                        if (s7.equals("\ud83c\udffe")) {
                                            top = 3;
                                            break Label_0440;
                                        }
                                        break;
                                    }
                                    case 1773377: {
                                        if (s7.equals("\ud83c\udffd")) {
                                            top = 2;
                                            break Label_0440;
                                        }
                                        break;
                                    }
                                    case 1773376: {
                                        if (s7.equals("\ud83c\udffc")) {
                                            top = 1;
                                            break Label_0440;
                                        }
                                        break;
                                    }
                                    case 1773375: {
                                        if (s7.equals("\ud83c\udffb")) {
                                            top = 0;
                                            break Label_0440;
                                        }
                                        break;
                                    }
                                }
                                top = -1;
                            }
                            if (top != 0) {
                                if (top != 1) {
                                    if (top != 2) {
                                        if (top != 3) {
                                            if (top == 4) {
                                                EmojiView.this.pickerView.setSelection(5);
                                            }
                                        }
                                        else {
                                            EmojiView.this.pickerView.setSelection(4);
                                        }
                                    }
                                    else {
                                        EmojiView.this.pickerView.setSelection(3);
                                    }
                                }
                                else {
                                    EmojiView.this.pickerView.setSelection(2);
                                }
                            }
                            else {
                                EmojiView.this.pickerView.setSelection(1);
                            }
                        }
                        else {
                            EmojiView.this.pickerView.setSelection(0);
                        }
                        imageViewEmoji.getLocationOnScreen(EmojiView.this.location);
                        final int access$2000 = EmojiView.this.emojiSize;
                        final int selection = EmojiView.this.pickerView.getSelection();
                        final int selection2 = EmojiView.this.pickerView.getSelection();
                        if (AndroidUtilities.isTablet()) {
                            top = n2;
                        }
                        else {
                            top = 1;
                        }
                        final int n3 = access$2000 * selection + AndroidUtilities.dp((float)(selection2 * 4 - top));
                        Label_0747: {
                            if (EmojiView.this.location[0] - n3 < AndroidUtilities.dp(5.0f)) {
                                top = EmojiView.this.location[0] - n3 - AndroidUtilities.dp(5.0f);
                            }
                            else {
                                top = n3;
                                if (EmojiView.this.location[0] - n3 + EmojiView.this.popupWidth <= AndroidUtilities.displaySize.x - AndroidUtilities.dp(5.0f)) {
                                    break Label_0747;
                                }
                                top = EmojiView.this.location[0] - n3 + EmojiView.this.popupWidth - (AndroidUtilities.displaySize.x - AndroidUtilities.dp(5.0f));
                            }
                            top += n3;
                        }
                        final int n4 = -top;
                        top = n;
                        if (imageViewEmoji.getTop() < 0) {
                            top = imageViewEmoji.getTop();
                        }
                        final EmojiColorPickerView access$2001 = EmojiView.this.pickerView;
                        float n5;
                        if (AndroidUtilities.isTablet()) {
                            n5 = 30.0f;
                        }
                        else {
                            n5 = 22.0f;
                        }
                        access$2001.setEmoji(replace, AndroidUtilities.dp(n5) - n4 + (int)AndroidUtilities.dpf2(0.5f));
                        EmojiView.this.pickerViewPopup.setFocusable(true);
                        EmojiView.this.pickerViewPopup.showAsDropDown(view, n4, -view.getMeasuredHeight() - EmojiView.this.popupHeight + (view.getMeasuredHeight() - EmojiView.this.emojiSize) / 2 - top);
                        EmojiView.this.pager.requestDisallowInterceptTouchEvent(true);
                        EmojiView.this.emojiGridView.hideSelector();
                        return true;
                    }
                    if (imageViewEmoji.isRecent) {
                        final RecyclerView.ViewHolder containingViewHolder = EmojiView.this.emojiGridView.findContainingViewHolder(view);
                        if (containingViewHolder != null && containingViewHolder.getAdapterPosition() <= Emoji.recentEmoji.size()) {
                            EmojiView.this.delegate.onClearEmojiRecent();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        this.emojiTabs = new ScrollSlidingTabStrip(context);
        if (needEmojiSearch) {
            this.emojiSearchField = new SearchField(context, 1);
            this.emojiContainer.addView((View)this.emojiSearchField, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, this.searchFieldHeight + AndroidUtilities.getShadowHeight()));
            this.emojiSearchField.searchEditText.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
                public void onFocusChange(final View view, final boolean b) {
                    if (b) {
                        EmojiView.this.lastSearchKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                        DataQuery.getInstance(EmojiView.this.currentAccount).fetchNewEmojiKeywords(EmojiView.this.lastSearchKeyboardLanguage);
                    }
                }
            });
        }
        this.emojiTabs.setShouldExpand(true);
        this.emojiTabs.setIndicatorHeight(-1);
        this.emojiTabs.setUnderlineHeight(-1);
        this.emojiTabs.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
        this.emojiContainer.addView((View)this.emojiTabs, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 38.0f));
        this.emojiTabs.setDelegate((ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate)new ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate() {
            @Override
            public void onPageSelected(final int n) {
                int n2 = n;
                if (!Emoji.recentEmoji.isEmpty()) {
                    if (n == 0) {
                        EmojiView.this.emojiLayoutManager.scrollToPositionWithOffset(EmojiView.this.needEmojiSearch ? 1 : 0, 0);
                        return;
                    }
                    n2 = n - 1;
                }
                EmojiView.this.emojiGridView.stopScroll();
                EmojiView.this.emojiLayoutManager.scrollToPositionWithOffset(EmojiView.this.emojiAdapter.sectionToPosition.get(n2), 0);
                EmojiView.this.checkEmojiTabY(null, 0);
            }
        });
        (this.emojiTabsShadow = new View(context)).setAlpha(0.0f);
        this.emojiTabsShadow.setTag((Object)1);
        this.emojiTabsShadow.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(38.0f);
        this.emojiContainer.addView(this.emojiTabsShadow, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
        if (b) {
            if (showGifs) {
                this.gifContainer = new FrameLayout(context);
                this.views.add((View)this.gifContainer);
                (this.gifGridView = new RecyclerListView(context) {
                    private boolean ignoreLayout;
                    
                    @Override
                    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                        final ContentPreviewViewer instance = ContentPreviewViewer.getInstance();
                        final RecyclerListView access$100 = EmojiView.this.gifGridView;
                        final ContentPreviewViewer.ContentPreviewViewerDelegate access$101 = EmojiView.this.contentPreviewViewerDelegate;
                        boolean b = false;
                        final boolean onInterceptTouchEvent = instance.onInterceptTouchEvent(motionEvent, access$100, 0, access$101);
                        if (super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent) {
                            b = true;
                        }
                        return b;
                    }
                    
                    @Override
                    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                        if (EmojiView.this.firstGifAttach && EmojiView.this.gifAdapter.getItemCount() > 1) {
                            this.ignoreLayout = true;
                            EmojiView.this.gifLayoutManager.scrollToPositionWithOffset(1, 0);
                            EmojiView.this.firstGifAttach = false;
                            this.ignoreLayout = false;
                        }
                        super.onLayout(b, n, n2, n3, n4);
                        EmojiView.this.checkGifSearchFieldScroll(true);
                    }
                    
                    @Override
                    public void requestLayout() {
                        if (this.ignoreLayout) {
                            return;
                        }
                        super.requestLayout();
                    }
                }).setClipToPadding(false);
                this.gifGridView.setLayoutManager((RecyclerView.LayoutManager)(this.gifLayoutManager = new ExtendedGridLayoutManager(context, 100) {
                    private Size size = new Size();
                    
                    @Override
                    protected int getFlowItemCount() {
                        if (EmojiView.this.gifGridView.getAdapter() == EmojiView.this.gifSearchAdapter && EmojiView.this.gifSearchAdapter.results.isEmpty()) {
                            return 0;
                        }
                        return ((RecyclerView.LayoutManager)this).getItemCount() - 1;
                    }
                    
                    @Override
                    protected Size getSizeForItem(int i) {
                        final RecyclerView.Adapter adapter = EmojiView.this.gifGridView.getAdapter();
                        final GifAdapter access$200 = EmojiView.this.gifAdapter;
                        TLRPC.Document document = null;
                        ArrayList<TLRPC.DocumentAttribute> list = null;
                        if (adapter == access$200) {
                            document = EmojiView.this.recentGifs.get(i);
                            list = document.attributes;
                        }
                        else if (!EmojiView.this.gifSearchAdapter.results.isEmpty()) {
                            final TLRPC.BotInlineResult botInlineResult = EmojiView.this.gifSearchAdapter.results.get(i);
                            document = botInlineResult.document;
                            if (document != null) {
                                list = document.attributes;
                            }
                            else {
                                final TLRPC.WebDocument content = botInlineResult.content;
                                if (content != null) {
                                    list = content.attributes;
                                }
                                else {
                                    final TLRPC.WebDocument thumb = botInlineResult.thumb;
                                    if (thumb != null) {
                                        list = thumb.attributes;
                                    }
                                }
                            }
                        }
                        else {
                            list = null;
                        }
                        final Size size = this.size;
                        size.height = 100.0f;
                        size.width = 100.0f;
                        if (document != null) {
                            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                            if (closestPhotoSizeWithSize != null) {
                                final int w = closestPhotoSizeWithSize.w;
                                if (w != 0) {
                                    i = closestPhotoSizeWithSize.h;
                                    if (i != 0) {
                                        final Size size2 = this.size;
                                        size2.width = (float)w;
                                        size2.height = (float)i;
                                    }
                                }
                            }
                        }
                        if (list != null) {
                            TLRPC.DocumentAttribute documentAttribute;
                            Size size3;
                            for (i = 0; i < list.size(); ++i) {
                                documentAttribute = list.get(i);
                                if (documentAttribute instanceof TLRPC.TL_documentAttributeImageSize || documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                                    size3 = this.size;
                                    size3.width = (float)documentAttribute.w;
                                    size3.height = (float)documentAttribute.h;
                                    break;
                                }
                            }
                        }
                        return this.size;
                    }
                }));
                this.gifLayoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(final int n) {
                        if (n != 0 && (EmojiView.this.gifGridView.getAdapter() != EmojiView.this.gifSearchAdapter || !EmojiView.this.gifSearchAdapter.results.isEmpty())) {
                            return EmojiView.this.gifLayoutManager.getSpanSizeForItem(n - 1);
                        }
                        return EmojiView.this.gifLayoutManager.getSpanCount();
                    }
                });
                this.gifGridView.addItemDecoration((RecyclerView.ItemDecoration)new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                        int dp = 0;
                        if (childAdapterPosition != 0) {
                            rect.left = 0;
                            rect.bottom = 0;
                            final ExtendedGridLayoutManager access$5300 = EmojiView.this.gifLayoutManager;
                            --childAdapterPosition;
                            if (!access$5300.isFirstRow(childAdapterPosition)) {
                                rect.top = AndroidUtilities.dp(2.0f);
                            }
                            else {
                                rect.top = 0;
                            }
                            if (!EmojiView.this.gifLayoutManager.isLastInRow(childAdapterPosition)) {
                                dp = AndroidUtilities.dp(2.0f);
                            }
                            rect.right = dp;
                        }
                        else {
                            rect.left = 0;
                            rect.top = 0;
                            rect.bottom = 0;
                            rect.right = 0;
                        }
                    }
                });
                this.gifGridView.setOverScrollMode(2);
                this.gifGridView.setAdapter(this.gifAdapter = new GifAdapter(context));
                this.gifSearchAdapter = new GifSearchAdapter(context);
                this.gifGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                        if (n == 1) {
                            EmojiView.this.gifSearchField.hideKeyboard();
                        }
                    }
                    
                    @Override
                    public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                        EmojiView.this.checkGifSearchFieldScroll(false);
                        EmojiView.this.checkBottomTabScroll((float)n2);
                    }
                });
                this.gifGridView.setOnTouchListener((View$OnTouchListener)new _$$Lambda$EmojiView$l7_k5UkFPpeHpKVTvoFojtQIyNg(this));
                this.gifOnItemClickListener = new _$$Lambda$EmojiView$qDPcvu2cn8NHi3uzHeRPO6CJgew(this);
                this.gifGridView.setOnItemClickListener(this.gifOnItemClickListener);
                this.gifContainer.addView((View)this.gifGridView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                this.gifSearchField = new SearchField(context, 2);
                this.gifContainer.addView((View)this.gifSearchField, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, this.searchFieldHeight + AndroidUtilities.getShadowHeight()));
            }
            this.stickersContainer = new FrameLayout(context);
            DataQuery.getInstance(this.currentAccount).checkStickers(0);
            DataQuery.getInstance(this.currentAccount).checkFeaturedStickers();
            (this.stickersGridView = new RecyclerListView(context) {
                boolean ignoreLayout;
                
                @Override
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    final boolean onInterceptTouchEvent = ContentPreviewViewer.getInstance().onInterceptTouchEvent(motionEvent, EmojiView.this.stickersGridView, EmojiView.this.getMeasuredHeight(), EmojiView.this.contentPreviewViewerDelegate);
                    return super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent;
                }
                
                @Override
                protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                    if (EmojiView.this.firstStickersAttach && EmojiView.this.stickersGridAdapter.getItemCount() > 0) {
                        this.ignoreLayout = true;
                        EmojiView.this.stickersLayoutManager.scrollToPositionWithOffset(1, 0);
                        EmojiView.this.firstStickersAttach = false;
                        this.ignoreLayout = false;
                    }
                    super.onLayout(b, n, n2, n3, n4);
                    EmojiView.this.checkStickersSearchFieldScroll(true);
                }
                
                @Override
                public void requestLayout() {
                    if (this.ignoreLayout) {
                        return;
                    }
                    super.requestLayout();
                }
                
                @Override
                public void setVisibility(final int visibility) {
                    if (EmojiView.this.trendingGridView != null && EmojiView.this.trendingGridView.getVisibility() == 0) {
                        super.setVisibility(8);
                        return;
                    }
                    super.setVisibility(visibility);
                }
            }).setLayoutManager((RecyclerView.LayoutManager)(this.stickersLayoutManager = new GridLayoutManager(context, 5)));
            this.stickersLayoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(final int n) {
                    if (EmojiView.this.stickersGridView.getAdapter() == EmojiView.this.stickersGridAdapter) {
                        if (n == 0) {
                            return EmojiView.this.stickersGridAdapter.stickersPerRow;
                        }
                        if (n != EmojiView.this.stickersGridAdapter.totalItems && (EmojiView.this.stickersGridAdapter.cache.get(n) == null || EmojiView.this.stickersGridAdapter.cache.get(n) instanceof TLRPC.Document)) {
                            return 1;
                        }
                        return EmojiView.this.stickersGridAdapter.stickersPerRow;
                    }
                    else {
                        if (n != EmojiView.this.stickersSearchGridAdapter.totalItems && (EmojiView.this.stickersSearchGridAdapter.cache.get(n) == null || EmojiView.this.stickersSearchGridAdapter.cache.get(n) instanceof TLRPC.Document)) {
                            return 1;
                        }
                        return EmojiView.this.stickersGridAdapter.stickersPerRow;
                    }
                }
            });
            this.stickersGridView.setPadding(0, AndroidUtilities.dp(52.0f), 0, 0);
            this.stickersGridView.setClipToPadding(false);
            this.views.add((View)this.stickersContainer);
            this.stickersSearchGridAdapter = new StickersSearchGridAdapter(context);
            this.stickersGridView.setAdapter(this.stickersGridAdapter = new StickersGridAdapter(context));
            this.stickersGridView.setOnTouchListener((View$OnTouchListener)new _$$Lambda$EmojiView$qCmH2CuXJBuotfYIBsa5LNpa8lQ(this));
            this.stickersOnItemClickListener = new _$$Lambda$EmojiView$sBpUanL5xQpXEXKoK_PK5yrP_k0(this);
            this.stickersGridView.setOnItemClickListener(this.stickersOnItemClickListener);
            this.stickersGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
            this.stickersContainer.addView((View)this.stickersGridView);
            this.stickersTab = new ScrollSlidingTabStrip(context) {
                float downX;
                float downY;
                boolean draggingHorizontally;
                boolean draggingVertically;
                boolean first = true;
                float lastTranslateX;
                float lastX;
                boolean startedScroll;
                final int touchslop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
                VelocityTracker vTracker;
                
                public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                    if (this.getParent() != null) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (motionEvent.getAction() == 0) {
                        this.draggingHorizontally = false;
                        this.draggingVertically = false;
                        this.downX = motionEvent.getRawX();
                        this.downY = motionEvent.getRawY();
                    }
                    else if (!this.draggingVertically && !this.draggingHorizontally && EmojiView.this.dragListener != null && Math.abs(motionEvent.getRawY() - this.downY) >= this.touchslop) {
                        this.draggingVertically = true;
                        this.downY = motionEvent.getRawY();
                        EmojiView.this.dragListener.onDragStart();
                        if (this.startedScroll) {
                            EmojiView.this.pager.endFakeDrag();
                            this.startedScroll = false;
                        }
                        return true;
                    }
                    return super.onInterceptTouchEvent(motionEvent);
                }
                
                public boolean onTouchEvent(final MotionEvent p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: getfield        org/telegram/ui/Components/EmojiView$17.first:Z
                    //     4: istore_2       
                    //     5: iconst_0       
                    //     6: istore_3       
                    //     7: iload_2        
                    //     8: ifeq            24
                    //    11: aload_0        
                    //    12: iconst_0       
                    //    13: putfield        org/telegram/ui/Components/EmojiView$17.first:Z
                    //    16: aload_0        
                    //    17: aload_1        
                    //    18: invokevirtual   android/view/MotionEvent.getX:()F
                    //    21: putfield        org/telegram/ui/Components/EmojiView$17.lastX:F
                    //    24: aload_1        
                    //    25: invokevirtual   android/view/MotionEvent.getAction:()I
                    //    28: ifne            60
                    //    31: aload_0        
                    //    32: iconst_0       
                    //    33: putfield        org/telegram/ui/Components/EmojiView$17.draggingHorizontally:Z
                    //    36: aload_0        
                    //    37: iconst_0       
                    //    38: putfield        org/telegram/ui/Components/EmojiView$17.draggingVertically:Z
                    //    41: aload_0        
                    //    42: aload_1        
                    //    43: invokevirtual   android/view/MotionEvent.getRawX:()F
                    //    46: putfield        org/telegram/ui/Components/EmojiView$17.downX:F
                    //    49: aload_0        
                    //    50: aload_1        
                    //    51: invokevirtual   android/view/MotionEvent.getRawY:()F
                    //    54: putfield        org/telegram/ui/Components/EmojiView$17.downY:F
                    //    57: goto            181
                    //    60: aload_0        
                    //    61: getfield        org/telegram/ui/Components/EmojiView$17.draggingVertically:Z
                    //    64: ifne            181
                    //    67: aload_0        
                    //    68: getfield        org/telegram/ui/Components/EmojiView$17.draggingHorizontally:Z
                    //    71: ifne            181
                    //    74: aload_0        
                    //    75: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //    78: invokestatic    org/telegram/ui/Components/EmojiView.access$6700:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/EmojiView$DragListener;
                    //    81: ifnull          181
                    //    84: aload_1        
                    //    85: invokevirtual   android/view/MotionEvent.getRawX:()F
                    //    88: aload_0        
                    //    89: getfield        org/telegram/ui/Components/EmojiView$17.downX:F
                    //    92: fsub           
                    //    93: invokestatic    java/lang/Math.abs:(F)F
                    //    96: aload_0        
                    //    97: getfield        org/telegram/ui/Components/EmojiView$17.touchslop:I
                    //   100: i2f            
                    //   101: fcmpl          
                    //   102: iflt            113
                    //   105: aload_0        
                    //   106: iconst_1       
                    //   107: putfield        org/telegram/ui/Components/EmojiView$17.draggingHorizontally:Z
                    //   110: goto            181
                    //   113: aload_1        
                    //   114: invokevirtual   android/view/MotionEvent.getRawY:()F
                    //   117: aload_0        
                    //   118: getfield        org/telegram/ui/Components/EmojiView$17.downY:F
                    //   121: fsub           
                    //   122: invokestatic    java/lang/Math.abs:(F)F
                    //   125: aload_0        
                    //   126: getfield        org/telegram/ui/Components/EmojiView$17.touchslop:I
                    //   129: i2f            
                    //   130: fcmpl          
                    //   131: iflt            181
                    //   134: aload_0        
                    //   135: iconst_1       
                    //   136: putfield        org/telegram/ui/Components/EmojiView$17.draggingVertically:Z
                    //   139: aload_0        
                    //   140: aload_1        
                    //   141: invokevirtual   android/view/MotionEvent.getRawY:()F
                    //   144: putfield        org/telegram/ui/Components/EmojiView$17.downY:F
                    //   147: aload_0        
                    //   148: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   151: invokestatic    org/telegram/ui/Components/EmojiView.access$6700:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/EmojiView$DragListener;
                    //   154: invokeinterface org/telegram/ui/Components/EmojiView$DragListener.onDragStart:()V
                    //   159: aload_0        
                    //   160: getfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   163: ifeq            181
                    //   166: aload_0        
                    //   167: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   170: invokestatic    org/telegram/ui/Components/EmojiView.access$4800:(Lorg/telegram/ui/Components/EmojiView;)Landroidx/viewpager/widget/ViewPager;
                    //   173: invokevirtual   androidx/viewpager/widget/ViewPager.endFakeDrag:()V
                    //   176: aload_0        
                    //   177: iconst_0       
                    //   178: putfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   181: aload_0        
                    //   182: getfield        org/telegram/ui/Components/EmojiView$17.draggingVertically:Z
                    //   185: ifeq            341
                    //   188: aload_0        
                    //   189: getfield        org/telegram/ui/Components/EmojiView$17.vTracker:Landroid/view/VelocityTracker;
                    //   192: ifnonnull       202
                    //   195: aload_0        
                    //   196: invokestatic    android/view/VelocityTracker.obtain:()Landroid/view/VelocityTracker;
                    //   199: putfield        org/telegram/ui/Components/EmojiView$17.vTracker:Landroid/view/VelocityTracker;
                    //   202: aload_0        
                    //   203: getfield        org/telegram/ui/Components/EmojiView$17.vTracker:Landroid/view/VelocityTracker;
                    //   206: aload_1        
                    //   207: invokevirtual   android/view/VelocityTracker.addMovement:(Landroid/view/MotionEvent;)V
                    //   210: aload_1        
                    //   211: invokevirtual   android/view/MotionEvent.getAction:()I
                    //   214: iconst_1       
                    //   215: if_icmpeq       256
                    //   218: aload_1        
                    //   219: invokevirtual   android/view/MotionEvent.getAction:()I
                    //   222: iconst_3       
                    //   223: if_icmpne       229
                    //   226: goto            256
                    //   229: aload_0        
                    //   230: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   233: invokestatic    org/telegram/ui/Components/EmojiView.access$6700:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/EmojiView$DragListener;
                    //   236: aload_1        
                    //   237: invokevirtual   android/view/MotionEvent.getRawY:()F
                    //   240: aload_0        
                    //   241: getfield        org/telegram/ui/Components/EmojiView$17.downY:F
                    //   244: fsub           
                    //   245: invokestatic    java/lang/Math.round:(F)I
                    //   248: invokeinterface org/telegram/ui/Components/EmojiView$DragListener.onDrag:(I)V
                    //   253: goto            339
                    //   256: aload_0        
                    //   257: getfield        org/telegram/ui/Components/EmojiView$17.vTracker:Landroid/view/VelocityTracker;
                    //   260: sipush          1000
                    //   263: invokevirtual   android/view/VelocityTracker.computeCurrentVelocity:(I)V
                    //   266: aload_0        
                    //   267: getfield        org/telegram/ui/Components/EmojiView$17.vTracker:Landroid/view/VelocityTracker;
                    //   270: invokevirtual   android/view/VelocityTracker.getYVelocity:()F
                    //   273: fstore          4
                    //   275: aload_0        
                    //   276: getfield        org/telegram/ui/Components/EmojiView$17.vTracker:Landroid/view/VelocityTracker;
                    //   279: invokevirtual   android/view/VelocityTracker.recycle:()V
                    //   282: aload_0        
                    //   283: aconst_null    
                    //   284: putfield        org/telegram/ui/Components/EmojiView$17.vTracker:Landroid/view/VelocityTracker;
                    //   287: aload_1        
                    //   288: invokevirtual   android/view/MotionEvent.getAction:()I
                    //   291: iconst_1       
                    //   292: if_icmpne       312
                    //   295: aload_0        
                    //   296: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   299: invokestatic    org/telegram/ui/Components/EmojiView.access$6700:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/EmojiView$DragListener;
                    //   302: fload           4
                    //   304: invokeinterface org/telegram/ui/Components/EmojiView$DragListener.onDragEnd:(F)V
                    //   309: goto            324
                    //   312: aload_0        
                    //   313: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   316: invokestatic    org/telegram/ui/Components/EmojiView.access$6700:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/EmojiView$DragListener;
                    //   319: invokeinterface org/telegram/ui/Components/EmojiView$DragListener.onDragCancel:()V
                    //   324: aload_0        
                    //   325: iconst_1       
                    //   326: putfield        org/telegram/ui/Components/EmojiView$17.first:Z
                    //   329: aload_0        
                    //   330: iconst_0       
                    //   331: putfield        org/telegram/ui/Components/EmojiView$17.draggingHorizontally:Z
                    //   334: aload_0        
                    //   335: iconst_0       
                    //   336: putfield        org/telegram/ui/Components/EmojiView$17.draggingVertically:Z
                    //   339: iconst_1       
                    //   340: ireturn        
                    //   341: aload_0        
                    //   342: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   345: invokestatic    org/telegram/ui/Components/EmojiView.access$6800:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/ScrollSlidingTabStrip;
                    //   348: invokevirtual   android/widget/HorizontalScrollView.getTranslationX:()F
                    //   351: fstore          4
                    //   353: aload_0        
                    //   354: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   357: invokestatic    org/telegram/ui/Components/EmojiView.access$6800:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/ScrollSlidingTabStrip;
                    //   360: invokevirtual   android/widget/HorizontalScrollView.getScrollX:()I
                    //   363: ifne            478
                    //   366: fload           4
                    //   368: fconst_0       
                    //   369: fcmpl          
                    //   370: ifne            478
                    //   373: aload_0        
                    //   374: getfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   377: ifne            429
                    //   380: aload_0        
                    //   381: getfield        org/telegram/ui/Components/EmojiView$17.lastX:F
                    //   384: aload_1        
                    //   385: invokevirtual   android/view/MotionEvent.getX:()F
                    //   388: fsub           
                    //   389: fconst_0       
                    //   390: fcmpg          
                    //   391: ifge            429
                    //   394: aload_0        
                    //   395: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   398: invokestatic    org/telegram/ui/Components/EmojiView.access$4800:(Lorg/telegram/ui/Components/EmojiView;)Landroidx/viewpager/widget/ViewPager;
                    //   401: invokevirtual   androidx/viewpager/widget/ViewPager.beginFakeDrag:()Z
                    //   404: ifeq            478
                    //   407: aload_0        
                    //   408: iconst_1       
                    //   409: putfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   412: aload_0        
                    //   413: aload_0        
                    //   414: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   417: invokestatic    org/telegram/ui/Components/EmojiView.access$6800:(Lorg/telegram/ui/Components/EmojiView;)Lorg/telegram/ui/Components/ScrollSlidingTabStrip;
                    //   420: invokevirtual   android/widget/HorizontalScrollView.getTranslationX:()F
                    //   423: putfield        org/telegram/ui/Components/EmojiView$17.lastTranslateX:F
                    //   426: goto            478
                    //   429: aload_0        
                    //   430: getfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   433: ifeq            478
                    //   436: aload_0        
                    //   437: getfield        org/telegram/ui/Components/EmojiView$17.lastX:F
                    //   440: aload_1        
                    //   441: invokevirtual   android/view/MotionEvent.getX:()F
                    //   444: fsub           
                    //   445: fconst_0       
                    //   446: fcmpl          
                    //   447: ifle            478
                    //   450: aload_0        
                    //   451: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   454: invokestatic    org/telegram/ui/Components/EmojiView.access$4800:(Lorg/telegram/ui/Components/EmojiView;)Landroidx/viewpager/widget/ViewPager;
                    //   457: invokevirtual   androidx/viewpager/widget/ViewPager.isFakeDragging:()Z
                    //   460: ifeq            478
                    //   463: aload_0        
                    //   464: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   467: invokestatic    org/telegram/ui/Components/EmojiView.access$4800:(Lorg/telegram/ui/Components/EmojiView;)Landroidx/viewpager/widget/ViewPager;
                    //   470: invokevirtual   androidx/viewpager/widget/ViewPager.endFakeDrag:()V
                    //   473: aload_0        
                    //   474: iconst_0       
                    //   475: putfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   478: aload_0        
                    //   479: getfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   482: ifeq            533
                    //   485: aload_1        
                    //   486: invokevirtual   android/view/MotionEvent.getX:()F
                    //   489: pop            
                    //   490: aload_0        
                    //   491: getfield        org/telegram/ui/Components/EmojiView$17.lastX:F
                    //   494: fstore          5
                    //   496: aload_0        
                    //   497: getfield        org/telegram/ui/Components/EmojiView$17.lastTranslateX:F
                    //   500: fstore          5
                    //   502: aload_0        
                    //   503: fload           4
                    //   505: putfield        org/telegram/ui/Components/EmojiView$17.lastTranslateX:F
                    //   508: goto            533
                    //   511: astore          6
                    //   513: aload_0        
                    //   514: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   517: invokestatic    org/telegram/ui/Components/EmojiView.access$4800:(Lorg/telegram/ui/Components/EmojiView;)Landroidx/viewpager/widget/ViewPager;
                    //   520: invokevirtual   androidx/viewpager/widget/ViewPager.endFakeDrag:()V
                    //   523: aload_0        
                    //   524: iconst_0       
                    //   525: putfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   528: aload           6
                    //   530: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
                    //   533: aload_0        
                    //   534: aload_1        
                    //   535: invokevirtual   android/view/MotionEvent.getX:()F
                    //   538: putfield        org/telegram/ui/Components/EmojiView$17.lastX:F
                    //   541: aload_1        
                    //   542: invokevirtual   android/view/MotionEvent.getAction:()I
                    //   545: iconst_3       
                    //   546: if_icmpeq       557
                    //   549: aload_1        
                    //   550: invokevirtual   android/view/MotionEvent.getAction:()I
                    //   553: iconst_1       
                    //   554: if_icmpne       594
                    //   557: aload_0        
                    //   558: iconst_1       
                    //   559: putfield        org/telegram/ui/Components/EmojiView$17.first:Z
                    //   562: aload_0        
                    //   563: iconst_0       
                    //   564: putfield        org/telegram/ui/Components/EmojiView$17.draggingHorizontally:Z
                    //   567: aload_0        
                    //   568: iconst_0       
                    //   569: putfield        org/telegram/ui/Components/EmojiView$17.draggingVertically:Z
                    //   572: aload_0        
                    //   573: getfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   576: ifeq            594
                    //   579: aload_0        
                    //   580: getfield        org/telegram/ui/Components/EmojiView$17.this$0:Lorg/telegram/ui/Components/EmojiView;
                    //   583: invokestatic    org/telegram/ui/Components/EmojiView.access$4800:(Lorg/telegram/ui/Components/EmojiView;)Landroidx/viewpager/widget/ViewPager;
                    //   586: invokevirtual   androidx/viewpager/widget/ViewPager.endFakeDrag:()V
                    //   589: aload_0        
                    //   590: iconst_0       
                    //   591: putfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   594: aload_0        
                    //   595: getfield        org/telegram/ui/Components/EmojiView$17.startedScroll:Z
                    //   598: ifne            609
                    //   601: aload_0        
                    //   602: aload_1        
                    //   603: invokespecial   android/widget/HorizontalScrollView.onTouchEvent:(Landroid/view/MotionEvent;)Z
                    //   606: ifeq            611
                    //   609: iconst_1       
                    //   610: istore_3       
                    //   611: iload_3        
                    //   612: ireturn        
                    //   613: astore          7
                    //   615: goto            523
                    //    Exceptions:
                    //  Try           Handler
                    //  Start  End    Start  End    Type                 
                    //  -----  -----  -----  -----  ---------------------
                    //  502    508    511    533    Ljava/lang/Exception;
                    //  513    523    613    618    Ljava/lang/Exception;
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.IllegalStateException: Expression is linked from several locations: Label_0523:
                    //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
                    //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
                    //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
                    //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:440)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                    //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                    //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                    //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                    //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                    //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
            };
            this.stickersSearchField = new SearchField(context, 0);
            this.stickersContainer.addView((View)this.stickersSearchField, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, this.searchFieldHeight + AndroidUtilities.getShadowHeight()));
            (this.trendingGridView = new RecyclerListView(context)).setItemAnimator(null);
            this.trendingGridView.setLayoutAnimation((LayoutAnimationController)null);
            this.trendingGridView.setLayoutManager((RecyclerView.LayoutManager)(this.trendingLayoutManager = new GridLayoutManager(context, 5) {
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            }));
            this.trendingLayoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(final int n) {
                    if (!(EmojiView.this.trendingGridAdapter.cache.get(n) instanceof Integer) && n != EmojiView.this.trendingGridAdapter.totalItems) {
                        return 1;
                    }
                    return EmojiView.this.trendingGridAdapter.stickersPerRow;
                }
            });
            this.trendingGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                    EmojiView.this.checkStickersTabY((View)recyclerView, n2);
                    EmojiView.this.checkBottomTabScroll((float)n2);
                }
            });
            this.trendingGridView.setClipToPadding(false);
            this.trendingGridView.setPadding(0, AndroidUtilities.dp(48.0f), 0, 0);
            this.trendingGridView.setAdapter(this.trendingGridAdapter = new TrendingGridAdapter(context));
            this.trendingGridView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$EmojiView$dYtQT1qlPrE3oYWHySyt2vGHEgc(this));
            this.trendingGridAdapter.notifyDataSetChanged();
            this.trendingGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
            this.trendingGridView.setVisibility(8);
            this.stickersContainer.addView((View)this.trendingGridView);
            this.stickersTab.setUnderlineHeight(AndroidUtilities.getShadowHeight());
            this.stickersTab.setIndicatorHeight(AndroidUtilities.dp(2.0f));
            this.stickersTab.setIndicatorColor(Theme.getColor("chat_emojiPanelStickerPackSelectorLine"));
            this.stickersTab.setUnderlineColor(Theme.getColor("chat_emojiPanelShadowLine"));
            this.stickersTab.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            this.stickersContainer.addView((View)this.stickersTab, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 51));
            this.updateStickerTabs();
            this.stickersTab.setDelegate((ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate)new _$$Lambda$EmojiView$EeKK6r6yP0jttMlGjDS2ja6SA2o(this));
            this.stickersGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                    if (n == 1) {
                        EmojiView.this.stickersSearchField.hideKeyboard();
                    }
                }
                
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                    EmojiView.this.checkScroll();
                    EmojiView.this.checkStickersTabY((View)recyclerView, n2);
                    EmojiView.this.checkStickersSearchFieldScroll(false);
                    EmojiView.this.checkBottomTabScroll((float)n2);
                }
            });
        }
        (this.pager = new ViewPager(context) {
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (this.getParent() != null) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            @Override
            public void setCurrentItem(final int n, final boolean b) {
                EmojiView.this.startStopVisibleGifs(n == 1);
                if (n == this.getCurrentItem()) {
                    if (n == 0) {
                        EmojiView.this.emojiGridView.smoothScrollToPosition(EmojiView.this.needEmojiSearch ? 1 : 0);
                    }
                    else if (n == 1) {
                        EmojiView.this.gifGridView.smoothScrollToPosition(1);
                    }
                    else {
                        EmojiView.this.stickersGridView.smoothScrollToPosition(1);
                    }
                    return;
                }
                super.setCurrentItem(n, b);
            }
        }).setAdapter(new EmojiPagesAdapter());
        (this.topShadow = new View(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, -1907225));
        this.addView(this.topShadow, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 6.0f));
        (this.backspaceButton = new ImageView(context) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    EmojiView.this.backspacePressed = true;
                    EmojiView.this.backspaceOnce = false;
                    EmojiView.this.postBackspaceRunnable(350);
                }
                else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                    EmojiView.this.backspacePressed = false;
                    if (!EmojiView.this.backspaceOnce && EmojiView.this.delegate != null && EmojiView.this.delegate.onBackspace()) {
                        EmojiView.this.backspaceButton.performHapticFeedback(3);
                    }
                }
                super.onTouchEvent(motionEvent);
                return true;
            }
        }).setImageResource(2131165848);
        this.backspaceButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), PorterDuff$Mode.MULTIPLY));
        this.backspaceButton.setScaleType(ImageView$ScaleType.CENTER);
        this.backspaceButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrBackspace", 2131558414));
        this.backspaceButton.setFocusable(true);
        this.bottomTabContainer = new FrameLayout(context) {
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (this.getParent() != null) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
        };
        (this.shadowLine = new View(context)).setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
        this.bottomTabContainer.addView(this.shadowLine, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight()));
        this.bottomTabContainerBackground = new View(context);
        this.bottomTabContainer.addView(this.bottomTabContainerBackground, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, AndroidUtilities.dp(44.0f), 83));
        final int n2 = 40;
        final int n3 = 44;
        if (needEmojiSearch) {
            this.addView((View)this.bottomTabContainer, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, AndroidUtilities.dp(44.0f) + AndroidUtilities.getShadowHeight(), 83));
            this.bottomTabContainer.addView((View)this.backspaceButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(52, 44, 85));
            (this.stickerSettingsButton = new ImageView(context)).setImageResource(2131165851);
            this.stickerSettingsButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), PorterDuff$Mode.MULTIPLY));
            this.stickerSettingsButton.setScaleType(ImageView$ScaleType.CENTER);
            this.stickerSettingsButton.setFocusable(true);
            this.stickerSettingsButton.setContentDescription((CharSequence)LocaleController.getString("Settings", 2131560738));
            this.bottomTabContainer.addView((View)this.stickerSettingsButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(52, 44, 85));
            this.stickerSettingsButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (EmojiView.this.delegate != null) {
                        EmojiView.this.delegate.onStickersSettingsClick();
                    }
                }
            });
            (this.typeTabs = new PagerSlidingTabStrip(context)).setViewPager(this.pager);
            this.typeTabs.setShouldExpand(false);
            this.typeTabs.setIndicatorHeight(0);
            this.typeTabs.setUnderlineHeight(0);
            this.typeTabs.setTabPaddingLeftRight(AndroidUtilities.dp(10.0f));
            this.bottomTabContainer.addView((View)this.typeTabs, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 44, 81));
            this.typeTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrollStateChanged(final int n) {
                }
                
                @Override
                public void onPageScrolled(int i, final float n, final int n2) {
                    final EmojiView this$0 = EmojiView.this;
                    this$0.onPageScrolled(i, this$0.getMeasuredWidth() - EmojiView.this.getPaddingLeft() - EmojiView.this.getPaddingRight(), n2);
                    EmojiView.this.showBottomTab(true, true);
                    i = EmojiView.this.pager.getCurrentItem();
                    SearchField searchField;
                    if (i == 0) {
                        searchField = EmojiView.this.emojiSearchField;
                    }
                    else if (i == 1) {
                        searchField = EmojiView.this.gifSearchField;
                    }
                    else {
                        searchField = EmojiView.this.stickersSearchField;
                    }
                    final String string = searchField.searchEditText.getText().toString();
                    SearchField searchField2;
                    for (i = 0; i < 3; ++i) {
                        if (i == 0) {
                            searchField2 = EmojiView.this.emojiSearchField;
                        }
                        else if (i == 1) {
                            searchField2 = EmojiView.this.gifSearchField;
                        }
                        else {
                            searchField2 = EmojiView.this.stickersSearchField;
                        }
                        if (searchField2 != null && searchField2 != searchField && searchField2.searchEditText != null) {
                            if (!searchField2.searchEditText.getText().toString().equals(string)) {
                                searchField2.searchEditText.setText((CharSequence)string);
                                searchField2.searchEditText.setSelection(string.length());
                            }
                        }
                    }
                }
                
                @Override
                public void onPageSelected(final int n) {
                    EmojiView.this.saveNewPage();
                    final EmojiView this$0 = EmojiView.this;
                    final boolean b = false;
                    this$0.showBackspaceButton(n == 0, true);
                    final EmojiView this$2 = EmojiView.this;
                    boolean b2 = b;
                    if (n == 2) {
                        b2 = true;
                    }
                    this$2.showStickerSettingsButton(b2, true);
                    if (EmojiView.this.delegate.isSearchOpened()) {
                        if (n == 0) {
                            if (EmojiView.this.emojiSearchField != null) {
                                EmojiView.this.emojiSearchField.searchEditText.requestFocus();
                            }
                        }
                        else if (n == 1) {
                            if (EmojiView.this.gifSearchField != null) {
                                EmojiView.this.gifSearchField.searchEditText.requestFocus();
                            }
                        }
                        else if (EmojiView.this.stickersSearchField != null) {
                            EmojiView.this.stickersSearchField.searchEditText.requestFocus();
                        }
                    }
                }
            });
            (this.searchButton = new ImageView(context)).setImageResource(2131165850);
            this.searchButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), PorterDuff$Mode.MULTIPLY));
            this.searchButton.setScaleType(ImageView$ScaleType.CENTER);
            this.searchButton.setContentDescription((CharSequence)LocaleController.getString("Search", 2131560640));
            this.searchButton.setFocusable(true);
            this.bottomTabContainer.addView((View)this.searchButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(52, 44, 83));
            this.searchButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final int currentItem = EmojiView.this.pager.getCurrentItem();
                    SearchField searchField;
                    if (currentItem == 0) {
                        searchField = EmojiView.this.emojiSearchField;
                    }
                    else if (currentItem == 1) {
                        searchField = EmojiView.this.gifSearchField;
                    }
                    else {
                        searchField = EmojiView.this.stickersSearchField;
                    }
                    if (searchField == null) {
                        return;
                    }
                    searchField.searchEditText.requestFocus();
                    final MotionEvent obtain = MotionEvent.obtain(0L, 0L, 0, 0.0f, 0.0f, 0);
                    searchField.searchEditText.onTouchEvent(obtain);
                    obtain.recycle();
                    final MotionEvent obtain2 = MotionEvent.obtain(0L, 0L, 1, 0.0f, 0.0f, 0);
                    searchField.searchEditText.onTouchEvent(obtain2);
                    obtain2.recycle();
                }
            });
        }
        else {
            final FrameLayout bottomTabContainer = this.bottomTabContainer;
            int n4;
            if (Build$VERSION.SDK_INT >= 21) {
                n4 = 40;
            }
            else {
                n4 = 44;
            }
            int n5;
            if (Build$VERSION.SDK_INT >= 21) {
                n5 = 40;
            }
            else {
                n5 = 44;
            }
            final float n6 = (float)(n5 + 12);
            int n7 = n;
            if (LocaleController.isRTL) {
                n7 = 3;
            }
            this.addView((View)bottomTabContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n4 + 20, n6, n7 | 0x50, 0.0f, 0.0f, 2.0f, 0.0f));
            Drawable simpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chat_emojiPanelBackground"), Theme.getColor("chat_emojiPanelBackground"));
            if (Build$VERSION.SDK_INT < 21) {
                final Drawable mutate = context.getResources().getDrawable(2131165387).mutate();
                mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
                simpleSelectorCircleDrawable = new CombinedDrawable(mutate, simpleSelectorCircleDrawable, 0, 0);
                ((CombinedDrawable)simpleSelectorCircleDrawable).setIconSize(AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            }
            else {
                final StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
                stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
                this.backspaceButton.setStateListAnimator(stateListAnimator);
                this.backspaceButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                    @SuppressLint({ "NewApi" })
                    public void getOutline(final View view, final Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
                    }
                });
            }
            this.backspaceButton.setPadding(0, 0, AndroidUtilities.dp(2.0f), 0);
            this.backspaceButton.setBackgroundDrawable(simpleSelectorCircleDrawable);
            this.backspaceButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrBackspace", 2131558414));
            this.backspaceButton.setFocusable(true);
            final FrameLayout bottomTabContainer2 = this.bottomTabContainer;
            final ImageView backspaceButton = this.backspaceButton;
            int n8;
            if (Build$VERSION.SDK_INT >= 21) {
                n8 = 40;
            }
            else {
                n8 = 44;
            }
            int n9 = n3;
            if (Build$VERSION.SDK_INT >= 21) {
                n9 = 40;
            }
            bottomTabContainer2.addView((View)backspaceButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n8, (float)n9, 51, 10.0f, 0.0f, 10.0f, 0.0f));
            this.shadowLine.setVisibility(8);
            this.bottomTabContainerBackground.setVisibility(8);
        }
        this.addView((View)this.pager, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.mediaBanTooltip = new CorrectlyMeasuringTextView(context)).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor("chat_gifSaveHintBackground")));
        this.mediaBanTooltip.setTextColor(Theme.getColor("chat_gifSaveHintText"));
        this.mediaBanTooltip.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
        this.mediaBanTooltip.setGravity(16);
        this.mediaBanTooltip.setTextSize(1, 14.0f);
        this.mediaBanTooltip.setVisibility(4);
        this.addView((View)this.mediaBanTooltip, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 81, 5.0f, 0.0f, 5.0f, 53.0f));
        float n10;
        if (AndroidUtilities.isTablet()) {
            n10 = 40.0f;
        }
        else {
            n10 = 32.0f;
        }
        this.emojiSize = AndroidUtilities.dp(n10);
        this.pickerView = new EmojiColorPickerView(context);
        final EmojiColorPickerView pickerView = this.pickerView;
        int n11;
        if (AndroidUtilities.isTablet()) {
            n11 = n2;
        }
        else {
            n11 = 32;
        }
        final int dp = AndroidUtilities.dp((float)(n11 * 6 + 10 + 20));
        this.popupWidth = dp;
        float n12;
        if (AndroidUtilities.isTablet()) {
            n12 = 64.0f;
        }
        else {
            n12 = 56.0f;
        }
        final int dp2 = AndroidUtilities.dp(n12);
        this.popupHeight = dp2;
        (this.pickerViewPopup = new EmojiPopupWindow(pickerView, dp, dp2)).setOutsideTouchable(true);
        this.pickerViewPopup.setClippingEnabled(true);
        this.pickerViewPopup.setInputMethodMode(2);
        this.pickerViewPopup.setSoftInputMode(0);
        this.pickerViewPopup.getContentView().setFocusableInTouchMode(true);
        this.pickerViewPopup.getContentView().setOnKeyListener((View$OnKeyListener)new _$$Lambda$EmojiView$YBM1z0ObV6f6L9y0M_Yz0CakSrc(this));
        this.currentPage = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
        Emoji.loadRecentEmoji();
        this.emojiAdapter.notifyDataSetChanged();
        if (this.typeTabs != null) {
            if (this.views.size() == 1 && this.typeTabs.getVisibility() == 0) {
                this.typeTabs.setVisibility(4);
            }
            else if (this.views.size() != 1 && this.typeTabs.getVisibility() != 0) {
                this.typeTabs.setVisibility(0);
            }
        }
    }
    
    private static String addColorToCode(String str, String str2) {
        final int length = str.length();
        String str3;
        if (length > 2 && str.charAt(str.length() - 2) == '\u200d') {
            final String substring = str.substring(str.length() - 2);
            str3 = str.substring(0, str.length() - 2);
            str = substring;
        }
        else if (length > 3 && str.charAt(str.length() - 3) == '\u200d') {
            final String substring2 = str.substring(str.length() - 3);
            str3 = str.substring(0, str.length() - 3);
            str = substring2;
        }
        else {
            final String s = null;
            str3 = str;
            str = s;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str3);
        sb.append(str2);
        final String str4 = str2 = sb.toString();
        if (str != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(str4);
            sb2.append(str);
            str2 = sb2.toString();
        }
        return str2;
    }
    
    private void checkBottomTabScroll(float lastBottomScrollDy) {
        this.lastBottomScrollDy += lastBottomScrollDy;
        int n;
        if (this.pager.getCurrentItem() == 0) {
            n = AndroidUtilities.dp(38.0f);
        }
        else {
            n = AndroidUtilities.dp(48.0f);
        }
        lastBottomScrollDy = this.lastBottomScrollDy;
        if (lastBottomScrollDy >= n) {
            this.showBottomTab(false, true);
        }
        else if (lastBottomScrollDy <= -n) {
            this.showBottomTab(true, true);
        }
        else if ((this.bottomTabContainer.getTag() == null && this.lastBottomScrollDy < 0.0f) || (this.bottomTabContainer.getTag() != null && this.lastBottomScrollDy > 0.0f)) {
            this.lastBottomScrollDy = 0.0f;
        }
    }
    
    private void checkDocuments(final boolean b) {
        if (b) {
            this.recentGifs = DataQuery.getInstance(this.currentAccount).getRecentGifs();
            final GifAdapter gifAdapter = this.gifAdapter;
            if (gifAdapter != null) {
                ((RecyclerView.Adapter)gifAdapter).notifyDataSetChanged();
            }
        }
        else {
            final int size = this.recentStickers.size();
            final int size2 = this.favouriteStickers.size();
            this.recentStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(0);
            this.favouriteStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(2);
            for (int i = 0; i < this.favouriteStickers.size(); ++i) {
                final TLRPC.Document document = this.favouriteStickers.get(i);
                for (int j = 0; j < this.recentStickers.size(); ++j) {
                    final TLRPC.Document document2 = this.recentStickers.get(j);
                    if (document2.dc_id == document.dc_id && document2.id == document.id) {
                        this.recentStickers.remove(j);
                        break;
                    }
                }
            }
            if (size != this.recentStickers.size() || size2 != this.favouriteStickers.size()) {
                this.updateStickerTabs();
            }
            final StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
            if (stickersGridAdapter != null) {
                stickersGridAdapter.notifyDataSetChanged();
            }
            this.checkPanels();
        }
    }
    
    private void checkEmojiSearchFieldScroll(final boolean b) {
        final EmojiViewDelegate delegate = this.delegate;
        boolean b2 = false;
        if (delegate != null && delegate.isSearchOpened()) {
            final RecyclerView.ViewHolder viewHolderForAdapterPosition = this.emojiGridView.findViewHolderForAdapterPosition(0);
            if (viewHolderForAdapterPosition == null) {
                this.emojiSearchField.showShadow(true, b ^ true);
            }
            else {
                this.emojiSearchField.showShadow(viewHolderForAdapterPosition.itemView.getTop() < this.emojiGridView.getPaddingTop(), b ^ true);
            }
            this.showEmojiShadow(false, b ^ true);
            return;
        }
        if (this.emojiSearchField != null) {
            final RecyclerListView emojiGridView = this.emojiGridView;
            if (emojiGridView != null) {
                final RecyclerView.ViewHolder viewHolderForAdapterPosition2 = emojiGridView.findViewHolderForAdapterPosition(0);
                if (viewHolderForAdapterPosition2 != null) {
                    this.emojiSearchField.setTranslationY((float)viewHolderForAdapterPosition2.itemView.getTop());
                }
                else {
                    this.emojiSearchField.setTranslationY((float)(-this.searchFieldHeight));
                }
                this.emojiSearchField.showShadow(false, b ^ true);
                if (viewHolderForAdapterPosition2 == null || viewHolderForAdapterPosition2.itemView.getTop() < AndroidUtilities.dp(38.0f) - this.searchFieldHeight + this.emojiTabs.getTranslationY()) {
                    b2 = true;
                }
                this.showEmojiShadow(b2, b ^ true);
            }
        }
    }
    
    private void checkEmojiTabY(final View view, int emojiMinusDy) {
        if (view == null) {
            final ScrollSlidingTabStrip emojiTabs = this.emojiTabs;
            this.emojiMinusDy = 0;
            emojiTabs.setTranslationY((float)0);
            this.emojiTabsShadow.setTranslationY((float)this.emojiMinusDy);
            return;
        }
        if (view.getVisibility() != 0) {
            return;
        }
        final EmojiViewDelegate delegate = this.delegate;
        if (delegate != null && delegate.isSearchOpened()) {
            return;
        }
        if (emojiMinusDy > 0) {
            final RecyclerListView emojiGridView = this.emojiGridView;
            if (emojiGridView != null && emojiGridView.getVisibility() == 0) {
                final RecyclerView.ViewHolder viewHolderForAdapterPosition = this.emojiGridView.findViewHolderForAdapterPosition(0);
                if (viewHolderForAdapterPosition != null) {
                    final int top = viewHolderForAdapterPosition.itemView.getTop();
                    int searchFieldHeight;
                    if (this.needEmojiSearch) {
                        searchFieldHeight = this.searchFieldHeight;
                    }
                    else {
                        searchFieldHeight = 0;
                    }
                    if (top + searchFieldHeight >= this.emojiGridView.getPaddingTop()) {
                        return;
                    }
                }
            }
        }
        this.emojiMinusDy -= emojiMinusDy;
        emojiMinusDy = this.emojiMinusDy;
        if (emojiMinusDy > 0) {
            this.emojiMinusDy = 0;
        }
        else if (emojiMinusDy < -AndroidUtilities.dp(288.0f)) {
            this.emojiMinusDy = -AndroidUtilities.dp(288.0f);
        }
        this.emojiTabs.setTranslationY((float)Math.max(-AndroidUtilities.dp(38.0f), this.emojiMinusDy));
        this.emojiTabsShadow.setTranslationY(this.emojiTabs.getTranslationY());
    }
    
    private void checkGifSearchFieldScroll(final boolean b) {
        final RecyclerListView gifGridView = this.gifGridView;
        if (gifGridView != null) {
            final RecyclerView.Adapter adapter = gifGridView.getAdapter();
            final GifSearchAdapter gifSearchAdapter = this.gifSearchAdapter;
            if (adapter == gifSearchAdapter && !gifSearchAdapter.searchEndReached && this.gifSearchAdapter.reqId == 0 && !this.gifSearchAdapter.results.isEmpty()) {
                final int lastVisibleItemPosition = this.gifLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition != -1 && lastVisibleItemPosition > ((RecyclerView.LayoutManager)this.gifLayoutManager).getItemCount() - 5) {
                    final GifSearchAdapter gifSearchAdapter2 = this.gifSearchAdapter;
                    gifSearchAdapter2.search(gifSearchAdapter2.lastSearchImageString, this.gifSearchAdapter.nextSearchOffset, true);
                }
            }
        }
        final EmojiViewDelegate delegate = this.delegate;
        boolean b2 = false;
        if (delegate != null && delegate.isSearchOpened()) {
            final RecyclerView.ViewHolder viewHolderForAdapterPosition = this.gifGridView.findViewHolderForAdapterPosition(0);
            if (viewHolderForAdapterPosition == null) {
                this.gifSearchField.showShadow(true, b ^ true);
            }
            else {
                final SearchField gifSearchField = this.gifSearchField;
                if (viewHolderForAdapterPosition.itemView.getTop() < this.gifGridView.getPaddingTop()) {
                    b2 = true;
                }
                gifSearchField.showShadow(b2, b ^ true);
            }
            return;
        }
        if (this.gifSearchField != null) {
            final RecyclerListView gifGridView2 = this.gifGridView;
            if (gifGridView2 != null) {
                final RecyclerView.ViewHolder viewHolderForAdapterPosition2 = gifGridView2.findViewHolderForAdapterPosition(0);
                if (viewHolderForAdapterPosition2 != null) {
                    this.gifSearchField.setTranslationY((float)viewHolderForAdapterPosition2.itemView.getTop());
                }
                else {
                    this.gifSearchField.setTranslationY((float)(-this.searchFieldHeight));
                }
                this.gifSearchField.showShadow(false, b ^ true);
            }
        }
    }
    
    private void checkPanels() {
        if (this.stickersTab == null) {
            return;
        }
        if (this.trendingTabNum == -2) {
            final RecyclerListView trendingGridView = this.trendingGridView;
            if (trendingGridView != null && trendingGridView.getVisibility() == 0) {
                this.trendingGridView.setVisibility(8);
                this.stickersGridView.setVisibility(0);
                this.stickersSearchField.setVisibility(0);
            }
        }
        final RecyclerListView trendingGridView2 = this.trendingGridView;
        if (trendingGridView2 != null && trendingGridView2.getVisibility() == 0) {
            final ScrollSlidingTabStrip stickersTab = this.stickersTab;
            final int trendingTabNum = this.trendingTabNum;
            int n = this.recentTabBum;
            if (n <= 0) {
                n = this.stickersTabOffset;
            }
            stickersTab.onPageScrolled(trendingTabNum, n);
        }
        else {
            final int firstVisibleItemPosition = this.stickersLayoutManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition != -1) {
                int n2 = this.favTabBum;
                if (n2 <= 0) {
                    n2 = this.recentTabBum;
                    if (n2 <= 0) {
                        n2 = this.stickersTabOffset;
                    }
                }
                this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(firstVisibleItemPosition), n2);
            }
        }
    }
    
    private void checkScroll() {
        final int firstVisibleItemPosition = this.stickersLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == -1) {
            return;
        }
        if (this.stickersGridView == null) {
            return;
        }
        int n = this.favTabBum;
        if (n <= 0) {
            n = this.recentTabBum;
            if (n <= 0) {
                n = this.stickersTabOffset;
            }
        }
        this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(firstVisibleItemPosition), n);
    }
    
    private void checkStickersSearchFieldScroll(final boolean b) {
        final EmojiViewDelegate delegate = this.delegate;
        boolean b2 = false;
        if (delegate != null && delegate.isSearchOpened()) {
            final RecyclerView.ViewHolder viewHolderForAdapterPosition = this.stickersGridView.findViewHolderForAdapterPosition(0);
            if (viewHolderForAdapterPosition == null) {
                this.stickersSearchField.showShadow(true, b ^ true);
            }
            else {
                final SearchField stickersSearchField = this.stickersSearchField;
                if (viewHolderForAdapterPosition.itemView.getTop() < this.stickersGridView.getPaddingTop()) {
                    b2 = true;
                }
                stickersSearchField.showShadow(b2, b ^ true);
            }
            return;
        }
        if (this.stickersSearchField != null) {
            final RecyclerListView stickersGridView = this.stickersGridView;
            if (stickersGridView != null) {
                final RecyclerView.ViewHolder viewHolderForAdapterPosition2 = stickersGridView.findViewHolderForAdapterPosition(0);
                if (viewHolderForAdapterPosition2 != null) {
                    this.stickersSearchField.setTranslationY((float)viewHolderForAdapterPosition2.itemView.getTop());
                }
                else {
                    this.stickersSearchField.setTranslationY((float)(-this.searchFieldHeight));
                }
                this.stickersSearchField.showShadow(false, b ^ true);
            }
        }
    }
    
    private void checkStickersTabY(final View view, int stickersMinusDy) {
        if (view == null) {
            final ScrollSlidingTabStrip stickersTab = this.stickersTab;
            this.stickersMinusDy = 0;
            stickersTab.setTranslationY((float)0);
            return;
        }
        if (view.getVisibility() != 0) {
            return;
        }
        final EmojiViewDelegate delegate = this.delegate;
        if (delegate != null && delegate.isSearchOpened()) {
            return;
        }
        if (stickersMinusDy > 0) {
            final RecyclerListView stickersGridView = this.stickersGridView;
            if (stickersGridView != null && stickersGridView.getVisibility() == 0) {
                final RecyclerView.ViewHolder viewHolderForAdapterPosition = this.stickersGridView.findViewHolderForAdapterPosition(0);
                if (viewHolderForAdapterPosition != null && viewHolderForAdapterPosition.itemView.getTop() + this.searchFieldHeight >= this.stickersGridView.getPaddingTop()) {
                    return;
                }
            }
        }
        this.stickersMinusDy -= stickersMinusDy;
        stickersMinusDy = this.stickersMinusDy;
        if (stickersMinusDy > 0) {
            this.stickersMinusDy = 0;
        }
        else if (stickersMinusDy < -AndroidUtilities.dp(288.0f)) {
            this.stickersMinusDy = -AndroidUtilities.dp(288.0f);
        }
        this.stickersTab.setTranslationY((float)Math.max(-AndroidUtilities.dp(48.0f), this.stickersMinusDy));
    }
    
    private void onPageScrolled(int n, int n2, final int n3) {
        final EmojiViewDelegate delegate = this.delegate;
        if (delegate == null) {
            return;
        }
        n2 = 0;
        if (n == 1) {
            n = n2;
            if (n3 != 0) {
                n = 2;
            }
            delegate.onTabOpened(n);
        }
        else if (n == 2) {
            delegate.onTabOpened(3);
        }
        else {
            delegate.onTabOpened(0);
        }
    }
    
    private void openSearch(final SearchField searchField) {
        final AnimatorSet searchAnimation = this.searchAnimation;
        if (searchAnimation != null) {
            searchAnimation.cancel();
            this.searchAnimation = null;
        }
        this.firstStickersAttach = false;
        this.firstGifAttach = false;
        this.firstEmojiAttach = false;
        for (int i = 0; i < 3; ++i) {
            SearchField searchField2;
            RecyclerListView recyclerListView;
            ScrollSlidingTabStrip scrollSlidingTabStrip;
            GridLayoutManager gridLayoutManager;
            if (i == 0) {
                searchField2 = this.emojiSearchField;
                recyclerListView = this.emojiGridView;
                scrollSlidingTabStrip = this.emojiTabs;
                gridLayoutManager = this.emojiLayoutManager;
            }
            else if (i == 1) {
                searchField2 = this.gifSearchField;
                recyclerListView = this.gifGridView;
                gridLayoutManager = this.gifLayoutManager;
                scrollSlidingTabStrip = null;
            }
            else {
                searchField2 = this.stickersSearchField;
                recyclerListView = this.stickersGridView;
                scrollSlidingTabStrip = this.stickersTab;
                gridLayoutManager = this.stickersLayoutManager;
            }
            if (searchField2 != null) {
                if (searchField2 != this.gifSearchField && searchField == searchField2) {
                    final EmojiViewDelegate delegate = this.delegate;
                    if (delegate != null && delegate.isExpanded()) {
                        this.searchAnimation = new AnimatorSet();
                        if (scrollSlidingTabStrip != null) {
                            this.searchAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)scrollSlidingTabStrip, View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(48.0f)) }), (Animator)ObjectAnimator.ofFloat((Object)recyclerListView, View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(48.0f)) }), (Animator)ObjectAnimator.ofFloat((Object)searchField2, View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(0.0f) }) });
                        }
                        else {
                            this.searchAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)recyclerListView, View.TRANSLATION_Y, new float[] { (float)(-AndroidUtilities.dp(48.0f)) }), (Animator)ObjectAnimator.ofFloat((Object)searchField2, View.TRANSLATION_Y, new float[] { (float)AndroidUtilities.dp(0.0f) }) });
                        }
                        this.searchAnimation.setDuration(200L);
                        this.searchAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
                        this.searchAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationCancel(final Animator animator) {
                                if (animator.equals(EmojiView.this.searchAnimation)) {
                                    EmojiView.this.searchAnimation = null;
                                }
                            }
                            
                            public void onAnimationEnd(final Animator animator) {
                                if (animator.equals(EmojiView.this.searchAnimation)) {
                                    recyclerListView.setTranslationY(0.0f);
                                    if (recyclerListView == EmojiView.this.stickersGridView) {
                                        recyclerListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                                    }
                                    else if (recyclerListView == EmojiView.this.emojiGridView) {
                                        recyclerListView.setPadding(0, 0, 0, 0);
                                    }
                                    EmojiView.this.searchAnimation = null;
                                }
                            }
                        });
                        this.searchAnimation.start();
                        continue;
                    }
                }
                searchField2.setTranslationY((float)AndroidUtilities.dp(0.0f));
                if (scrollSlidingTabStrip != null) {
                    scrollSlidingTabStrip.setTranslationY((float)(-AndroidUtilities.dp(48.0f)));
                }
                if (recyclerListView == this.stickersGridView) {
                    recyclerListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                }
                else if (recyclerListView == this.emojiGridView) {
                    recyclerListView.setPadding(0, 0, 0, 0);
                }
                gridLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        }
    }
    
    private void postBackspaceRunnable(final int n) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$Jrw9DaGNjc5_52tMJtvct_h0u6A(this, n), n);
    }
    
    private void reloadStickersAdapter() {
        final StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        if (stickersGridAdapter != null) {
            stickersGridAdapter.notifyDataSetChanged();
        }
        final TrendingGridAdapter trendingGridAdapter = this.trendingGridAdapter;
        if (trendingGridAdapter != null) {
            trendingGridAdapter.notifyDataSetChanged();
        }
        final StickersSearchGridAdapter stickersSearchGridAdapter = this.stickersSearchGridAdapter;
        if (stickersSearchGridAdapter != null) {
            stickersSearchGridAdapter.notifyDataSetChanged();
        }
        if (ContentPreviewViewer.getInstance().isVisible()) {
            ContentPreviewViewer.getInstance().close();
        }
        ContentPreviewViewer.getInstance().reset();
    }
    
    private void saveNewPage() {
        final ViewPager pager = this.pager;
        if (pager == null) {
            return;
        }
        final int currentItem = pager.getCurrentItem();
        int currentPage = 1;
        if (currentItem != 2) {
            if (currentItem == 1) {
                currentPage = 2;
            }
            else {
                currentPage = 0;
            }
        }
        if (this.currentPage != currentPage) {
            this.currentPage = currentPage;
            MessagesController.getGlobalEmojiSettings().edit().putInt("selected_page", currentPage).commit();
        }
    }
    
    private void showBackspaceButton(final boolean b, final boolean b2) {
        if ((b && this.backspaceButton.getTag() == null) || (!b && this.backspaceButton.getTag() != null)) {
            return;
        }
        final AnimatorSet backspaceButtonAnimation = this.backspaceButtonAnimation;
        Object value = null;
        if (backspaceButtonAnimation != null) {
            backspaceButtonAnimation.cancel();
            this.backspaceButtonAnimation = null;
        }
        final ImageView backspaceButton = this.backspaceButton;
        if (!b) {
            value = 1;
        }
        backspaceButton.setTag(value);
        int visibility = 0;
        float scaleY = 1.0f;
        if (b2) {
            if (b) {
                this.backspaceButton.setVisibility(0);
            }
            this.backspaceButtonAnimation = new AnimatorSet();
            final AnimatorSet backspaceButtonAnimation2 = this.backspaceButtonAnimation;
            final ImageView backspaceButton2 = this.backspaceButton;
            final Property alpha = View.ALPHA;
            float n;
            if (b) {
                n = 1.0f;
            }
            else {
                n = 0.0f;
            }
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)backspaceButton2, alpha, new float[] { n });
            final ImageView backspaceButton3 = this.backspaceButton;
            final Property scale_X = View.SCALE_X;
            float n2;
            if (b) {
                n2 = 1.0f;
            }
            else {
                n2 = 0.0f;
            }
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)backspaceButton3, scale_X, new float[] { n2 });
            final ImageView backspaceButton4 = this.backspaceButton;
            final Property scale_Y = View.SCALE_Y;
            if (!b) {
                scaleY = 0.0f;
            }
            backspaceButtonAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ObjectAnimator.ofFloat((Object)backspaceButton4, scale_Y, new float[] { scaleY }) });
            this.backspaceButtonAnimation.setDuration(200L);
            this.backspaceButtonAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
            this.backspaceButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (!b) {
                        EmojiView.this.backspaceButton.setVisibility(4);
                    }
                }
            });
            this.backspaceButtonAnimation.start();
        }
        else {
            final ImageView backspaceButton5 = this.backspaceButton;
            float alpha2;
            if (b) {
                alpha2 = 1.0f;
            }
            else {
                alpha2 = 0.0f;
            }
            backspaceButton5.setAlpha(alpha2);
            final ImageView backspaceButton6 = this.backspaceButton;
            float scaleX;
            if (b) {
                scaleX = 1.0f;
            }
            else {
                scaleX = 0.0f;
            }
            backspaceButton6.setScaleX(scaleX);
            final ImageView backspaceButton7 = this.backspaceButton;
            if (!b) {
                scaleY = 0.0f;
            }
            backspaceButton7.setScaleY(scaleY);
            final ImageView backspaceButton8 = this.backspaceButton;
            if (!b) {
                visibility = 4;
            }
            backspaceButton8.setVisibility(visibility);
        }
    }
    
    private void showBottomTab(final boolean b, final boolean b2) {
        final float n = 0.0f;
        final float n2 = 0.0f;
        this.lastBottomScrollDy = 0.0f;
        if ((!b || this.bottomTabContainer.getTag() != null) && (b || this.bottomTabContainer.getTag() == null)) {
            final EmojiViewDelegate delegate = this.delegate;
            if (delegate == null || !delegate.isSearchOpened()) {
                final AnimatorSet bottomTabContainerAnimation = this.bottomTabContainerAnimation;
                Object value = null;
                if (bottomTabContainerAnimation != null) {
                    bottomTabContainerAnimation.cancel();
                    this.bottomTabContainerAnimation = null;
                }
                final FrameLayout bottomTabContainer = this.bottomTabContainer;
                if (!b) {
                    value = 1;
                }
                bottomTabContainer.setTag(value);
                float n3 = 54.0f;
                if (b2) {
                    this.bottomTabContainerAnimation = new AnimatorSet();
                    final AnimatorSet bottomTabContainerAnimation2 = this.bottomTabContainerAnimation;
                    final FrameLayout bottomTabContainer2 = this.bottomTabContainer;
                    final Property translation_Y = View.TRANSLATION_Y;
                    float n4;
                    if (b) {
                        n4 = 0.0f;
                    }
                    else {
                        if (this.needEmojiSearch) {
                            n3 = 49.0f;
                        }
                        n4 = (float)AndroidUtilities.dp(n3);
                    }
                    final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)bottomTabContainer2, translation_Y, new float[] { n4 });
                    final View shadowLine = this.shadowLine;
                    final Property translation_Y2 = View.TRANSLATION_Y;
                    float n5;
                    if (b) {
                        n5 = n2;
                    }
                    else {
                        n5 = (float)AndroidUtilities.dp(49.0f);
                    }
                    bottomTabContainerAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)shadowLine, translation_Y2, new float[] { n5 }) });
                    this.bottomTabContainerAnimation.setDuration(200L);
                    this.bottomTabContainerAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
                    this.bottomTabContainerAnimation.start();
                }
                else {
                    final FrameLayout bottomTabContainer3 = this.bottomTabContainer;
                    float translationY;
                    if (b) {
                        translationY = 0.0f;
                    }
                    else {
                        if (this.needEmojiSearch) {
                            n3 = 49.0f;
                        }
                        translationY = (float)AndroidUtilities.dp(n3);
                    }
                    bottomTabContainer3.setTranslationY(translationY);
                    final View shadowLine2 = this.shadowLine;
                    float translationY2;
                    if (b) {
                        translationY2 = n;
                    }
                    else {
                        translationY2 = (float)AndroidUtilities.dp(49.0f);
                    }
                    shadowLine2.setTranslationY(translationY2);
                }
            }
        }
    }
    
    private void showEmojiShadow(final boolean b, final boolean b2) {
        if ((b && this.emojiTabsShadow.getTag() == null) || (!b && this.emojiTabsShadow.getTag() != null)) {
            return;
        }
        final AnimatorSet emojiTabShadowAnimator = this.emojiTabShadowAnimator;
        Object value = null;
        if (emojiTabShadowAnimator != null) {
            emojiTabShadowAnimator.cancel();
            this.emojiTabShadowAnimator = null;
        }
        final View emojiTabsShadow = this.emojiTabsShadow;
        if (!b) {
            value = 1;
        }
        emojiTabsShadow.setTag(value);
        float alpha = 1.0f;
        if (b2) {
            this.emojiTabShadowAnimator = new AnimatorSet();
            final AnimatorSet emojiTabShadowAnimator2 = this.emojiTabShadowAnimator;
            final View emojiTabsShadow2 = this.emojiTabsShadow;
            final Property alpha2 = View.ALPHA;
            if (!b) {
                alpha = 0.0f;
            }
            emojiTabShadowAnimator2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)emojiTabsShadow2, alpha2, new float[] { alpha }) });
            this.emojiTabShadowAnimator.setDuration(200L);
            this.emojiTabShadowAnimator.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
            this.emojiTabShadowAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    EmojiView.this.emojiTabShadowAnimator = null;
                }
            });
            this.emojiTabShadowAnimator.start();
        }
        else {
            final View emojiTabsShadow3 = this.emojiTabsShadow;
            if (!b) {
                alpha = 0.0f;
            }
            emojiTabsShadow3.setAlpha(alpha);
        }
    }
    
    private void showStickerSettingsButton(final boolean b, final boolean b2) {
        final ImageView stickerSettingsButton = this.stickerSettingsButton;
        if (stickerSettingsButton == null) {
            return;
        }
        if ((b && stickerSettingsButton.getTag() == null) || (!b && this.stickerSettingsButton.getTag() != null)) {
            return;
        }
        final AnimatorSet stickersButtonAnimation = this.stickersButtonAnimation;
        Object value = null;
        if (stickersButtonAnimation != null) {
            stickersButtonAnimation.cancel();
            this.stickersButtonAnimation = null;
        }
        final ImageView stickerSettingsButton2 = this.stickerSettingsButton;
        if (!b) {
            value = 1;
        }
        stickerSettingsButton2.setTag(value);
        int visibility = 0;
        float scaleY = 1.0f;
        if (b2) {
            if (b) {
                this.stickerSettingsButton.setVisibility(0);
            }
            this.stickersButtonAnimation = new AnimatorSet();
            final AnimatorSet stickersButtonAnimation2 = this.stickersButtonAnimation;
            final ImageView stickerSettingsButton3 = this.stickerSettingsButton;
            final Property alpha = View.ALPHA;
            float n;
            if (b) {
                n = 1.0f;
            }
            else {
                n = 0.0f;
            }
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)stickerSettingsButton3, alpha, new float[] { n });
            final ImageView stickerSettingsButton4 = this.stickerSettingsButton;
            final Property scale_X = View.SCALE_X;
            float n2;
            if (b) {
                n2 = 1.0f;
            }
            else {
                n2 = 0.0f;
            }
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)stickerSettingsButton4, scale_X, new float[] { n2 });
            final ImageView stickerSettingsButton5 = this.stickerSettingsButton;
            final Property scale_Y = View.SCALE_Y;
            if (!b) {
                scaleY = 0.0f;
            }
            stickersButtonAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ObjectAnimator.ofFloat((Object)stickerSettingsButton5, scale_Y, new float[] { scaleY }) });
            this.stickersButtonAnimation.setDuration(200L);
            this.stickersButtonAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
            this.stickersButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (!b) {
                        EmojiView.this.stickerSettingsButton.setVisibility(4);
                    }
                }
            });
            this.stickersButtonAnimation.start();
        }
        else {
            final ImageView stickerSettingsButton6 = this.stickerSettingsButton;
            float alpha2;
            if (b) {
                alpha2 = 1.0f;
            }
            else {
                alpha2 = 0.0f;
            }
            stickerSettingsButton6.setAlpha(alpha2);
            final ImageView stickerSettingsButton7 = this.stickerSettingsButton;
            float scaleX;
            if (b) {
                scaleX = 1.0f;
            }
            else {
                scaleX = 0.0f;
            }
            stickerSettingsButton7.setScaleX(scaleX);
            final ImageView stickerSettingsButton8 = this.stickerSettingsButton;
            if (!b) {
                scaleY = 0.0f;
            }
            stickerSettingsButton8.setScaleY(scaleY);
            final ImageView stickerSettingsButton9 = this.stickerSettingsButton;
            if (!b) {
                visibility = 4;
            }
            stickerSettingsButton9.setVisibility(visibility);
        }
    }
    
    private void showTrendingTab(final boolean b) {
        if (b) {
            this.trendingGridView.setVisibility(0);
            this.stickersGridView.setVisibility(8);
            this.stickersSearchField.setVisibility(8);
            final ScrollSlidingTabStrip stickersTab = this.stickersTab;
            final int trendingTabNum = this.trendingTabNum;
            int n = this.recentTabBum;
            if (n <= 0) {
                n = this.stickersTabOffset;
            }
            stickersTab.onPageScrolled(trendingTabNum, n);
            this.saveNewPage();
        }
        else {
            this.trendingGridView.setVisibility(8);
            this.stickersGridView.setVisibility(0);
            this.stickersSearchField.setVisibility(0);
        }
    }
    
    private void startStopVisibleGifs(final boolean b) {
        final RecyclerListView gifGridView = this.gifGridView;
        if (gifGridView == null) {
            return;
        }
        for (int childCount = gifGridView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.gifGridView.getChildAt(i);
            if (child instanceof ContextLinkCell) {
                final ImageReceiver photoImage = ((ContextLinkCell)child).getPhotoImage();
                if (b) {
                    photoImage.setAllowStartAnimation(true);
                    photoImage.startAnimation();
                }
                else {
                    photoImage.setAllowStartAnimation(false);
                    photoImage.stopAnimation();
                }
            }
        }
    }
    
    private void updateEmojiTabs() {
        final boolean hasRecentEmoji = Emoji.recentEmoji.isEmpty() ^ true;
        final int hasRecentEmoji2 = this.hasRecentEmoji;
        if (hasRecentEmoji2 != -1 && hasRecentEmoji2 == (hasRecentEmoji ? 1 : 0)) {
            return;
        }
        this.hasRecentEmoji = (hasRecentEmoji ? 1 : 0);
        this.emojiTabs.removeTabs();
        final String string = LocaleController.getString("RecentStickers", 2131560538);
        int i = 0;
        final String string2 = LocaleController.getString("Emoji1", 2131559332);
        final String string3 = LocaleController.getString("Emoji2", 2131559333);
        final String string4 = LocaleController.getString("Emoji3", 2131559334);
        final String string5 = LocaleController.getString("Emoji4", 2131559335);
        final String string6 = LocaleController.getString("Emoji5", 2131559336);
        final String string7 = LocaleController.getString("Emoji6", 2131559337);
        final String string8 = LocaleController.getString("Emoji7", 2131559338);
        final String string9 = LocaleController.getString("Emoji8", 2131559339);
        while (i < this.emojiIcons.length) {
            if (i != 0 || !Emoji.recentEmoji.isEmpty()) {
                this.emojiTabs.addIconTab(this.emojiIcons[i]).setContentDescription((CharSequence)(new String[] { string, string2, string3, string4, string5, string6, string7, string8, string9 })[i]);
            }
            ++i;
        }
        this.emojiTabs.updateTabStyles();
    }
    
    private void updateStickerTabs() {
        final ScrollSlidingTabStrip stickersTab = this.stickersTab;
        if (stickersTab == null) {
            return;
        }
        this.recentTabBum = -2;
        this.favTabBum = -2;
        this.trendingTabNum = -2;
        this.stickersTabOffset = 0;
        final int currentPosition = stickersTab.getCurrentPosition();
        this.stickersTab.removeTabs();
        final ArrayList<Long> unreadStickerSets = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
        final TrendingGridAdapter trendingGridAdapter = this.trendingGridAdapter;
        final int n = 2;
        if (trendingGridAdapter != null && trendingGridAdapter.getItemCount() != 0 && !unreadStickerSets.isEmpty()) {
            this.stickersCounter = this.stickersTab.addIconTabWithCounter(this.stickerIcons[2]);
            final int stickersTabOffset = this.stickersTabOffset;
            this.trendingTabNum = stickersTabOffset;
            this.stickersTabOffset = stickersTabOffset + 1;
            this.stickersCounter.setText((CharSequence)String.format("%d", unreadStickerSets.size()));
        }
        int n2;
        if (!this.favouriteStickers.isEmpty()) {
            final int stickersTabOffset2 = this.stickersTabOffset;
            this.favTabBum = stickersTabOffset2;
            this.stickersTabOffset = stickersTabOffset2 + 1;
            this.stickersTab.addIconTab(this.stickerIcons[1]).setContentDescription((CharSequence)LocaleController.getString("FavoriteStickers", 2131559478));
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        if (!this.recentStickers.isEmpty()) {
            final int stickersTabOffset3 = this.stickersTabOffset;
            this.recentTabBum = stickersTabOffset3;
            this.stickersTabOffset = stickersTabOffset3 + 1;
            this.stickersTab.addIconTab(this.stickerIcons[0]).setContentDescription((CharSequence)LocaleController.getString("RecentStickers", 2131560538));
            n2 = 1;
        }
        this.stickerSets.clear();
        TLRPC.TL_messages_stickerSet groupStickerSet = null;
        this.groupStickerSet = null;
        this.groupStickerPackPosition = -1;
        this.groupStickerPackNum = -10;
        final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(this.currentAccount).getStickerSets(0);
        int n3;
        for (int i = 0; i < stickerSets.size(); ++i, n2 = n3) {
            final TLRPC.TL_messages_stickerSet e = stickerSets.get(i);
            n3 = n2;
            if (!e.set.archived) {
                final ArrayList<TLRPC.Document> documents = e.documents;
                n3 = n2;
                if (documents != null) {
                    if (documents.isEmpty()) {
                        n3 = n2;
                    }
                    else {
                        this.stickerSets.add(e);
                        n3 = 1;
                    }
                }
            }
        }
        if (this.info != null) {
            final SharedPreferences emojiSettings = MessagesController.getEmojiSettings(this.currentAccount);
            final StringBuilder sb = new StringBuilder();
            sb.append("group_hide_stickers_");
            sb.append(this.info.id);
            final long long1 = emojiSettings.getLong(sb.toString(), -1L);
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(this.info.id);
            if (chat != null && this.info.stickerset != null && ChatObject.hasAdminRights(chat)) {
                final TLRPC.StickerSet stickerset = this.info.stickerset;
                if (stickerset != null) {
                    this.groupStickersHidden = (long1 == stickerset.id);
                }
            }
            else {
                this.groupStickersHidden = (long1 != -1L);
            }
            final TLRPC.ChatFull info = this.info;
            if (info.stickerset != null) {
                final TLRPC.TL_messages_stickerSet groupStickerSetById = DataQuery.getInstance(this.currentAccount).getGroupStickerSetById(this.info.stickerset);
                if (groupStickerSetById != null) {
                    final ArrayList<TLRPC.Document> documents2 = groupStickerSetById.documents;
                    if (documents2 != null && !documents2.isEmpty() && groupStickerSetById.set != null) {
                        final TLRPC.TL_messages_stickerSet set = new TLRPC.TL_messages_stickerSet();
                        set.documents = groupStickerSetById.documents;
                        set.packs = groupStickerSetById.packs;
                        set.set = groupStickerSetById.set;
                        if (this.groupStickersHidden) {
                            this.groupStickerPackNum = this.stickerSets.size();
                            this.stickerSets.add(set);
                        }
                        else {
                            this.groupStickerPackNum = 0;
                            this.stickerSets.add(0, set);
                        }
                        if (this.info.can_set_stickers) {
                            groupStickerSet = set;
                        }
                        this.groupStickerSet = groupStickerSet;
                    }
                }
            }
            else if (info.can_set_stickers) {
                final TLRPC.TL_messages_stickerSet set2 = new TLRPC.TL_messages_stickerSet();
                if (this.groupStickersHidden) {
                    this.groupStickerPackNum = this.stickerSets.size();
                    this.stickerSets.add(set2);
                }
                else {
                    this.groupStickerPackNum = 0;
                    this.stickerSets.add(0, set2);
                }
            }
        }
        final int n4 = 0;
        int n5 = n2;
        for (int j = n4; j < this.stickerSets.size(); ++j) {
            if (j == this.groupStickerPackNum) {
                final TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(this.info.id);
                if (chat2 == null) {
                    this.stickerSets.remove(0);
                    --j;
                    continue;
                }
                this.stickersTab.addStickerTab(chat2);
            }
            else {
                final TLRPC.TL_messages_stickerSet set3 = this.stickerSets.get(j);
                final TLRPC.Document document = set3.documents.get(0);
                TLObject thumb = set3.set.thumb;
                if (!(thumb instanceof TLRPC.TL_photoSize)) {
                    thumb = document;
                }
                final View addStickerTab = this.stickersTab.addStickerTab(thumb, document, set3);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(set3.set.title);
                sb2.append(", ");
                sb2.append(LocaleController.getString("AccDescrStickerSet", 2131558475));
                addStickerTab.setContentDescription((CharSequence)sb2.toString());
            }
            n5 = 1;
        }
        final TrendingGridAdapter trendingGridAdapter2 = this.trendingGridAdapter;
        if (trendingGridAdapter2 != null && trendingGridAdapter2.getItemCount() != 0 && unreadStickerSets.isEmpty()) {
            this.trendingTabNum = this.stickersTabOffset + this.stickerSets.size();
            this.stickersTab.addIconTab(this.stickerIcons[2]).setContentDescription((CharSequence)LocaleController.getString("FeaturedStickers", 2131559479));
        }
        this.stickersTab.updateTabStyles();
        if (currentPosition != 0) {
            this.stickersTab.onPageScrolled(currentPosition, currentPosition);
        }
        this.checkPanels();
        if ((n5 == 0 || (this.trendingTabNum == 0 && DataQuery.getInstance(this.currentAccount).areAllTrendingStickerSetsUnread())) && this.trendingTabNum >= 0) {
            if (this.scrolledToTrending == 0) {
                this.showTrendingTab(true);
                int scrolledToTrending;
                if (n5 != 0) {
                    scrolledToTrending = n;
                }
                else {
                    scrolledToTrending = 1;
                }
                this.scrolledToTrending = scrolledToTrending;
            }
        }
        else if (this.scrolledToTrending == 1) {
            this.showTrendingTab(false);
            this.checkScroll();
            this.stickersTab.cancelPositionAnimation();
        }
    }
    
    private void updateVisibleTrendingSets() {
        final TrendingGridAdapter trendingGridAdapter = this.trendingGridAdapter;
        if (trendingGridAdapter != null) {
            if (trendingGridAdapter != null) {
                int i = 0;
                RecyclerListView recyclerListView;
                View child;
                FeaturedStickerSetInfoCell featuredStickerSetInfoCell;
                ArrayList<Long> unreadStickerSets;
                TLRPC.StickerSetCovered stickerSet;
                boolean b;
                boolean b2;
                boolean b3;
                boolean b4;
                boolean b5 = false;
                boolean b6 = false;
                boolean drawProgress;
                Label_0040_Outer:Label_0035_Outer:
                while (i < 2) {
                    while (true) {
                        if (i == 0) {
                            try {
                                recyclerListView = this.trendingGridView;
                                while (true) {
                                    for (int childCount = recyclerListView.getChildCount(), j = 0; j < childCount; ++j) {
                                        child = recyclerListView.getChildAt(j);
                                        if (child instanceof FeaturedStickerSetInfoCell) {
                                            if (recyclerListView.getChildViewHolder(child) != null) {
                                                featuredStickerSetInfoCell = (FeaturedStickerSetInfoCell)child;
                                                unreadStickerSets = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
                                                stickerSet = featuredStickerSetInfoCell.getStickerSet();
                                                b = true;
                                                b2 = (unreadStickerSets != null && unreadStickerSets.contains(stickerSet.set.id));
                                                featuredStickerSetInfoCell.setStickerSet(stickerSet, b2);
                                                if (b2) {
                                                    DataQuery.getInstance(this.currentAccount).markFaturedStickersByIdAsRead(stickerSet.set.id);
                                                }
                                                b3 = (this.installingStickerSets.indexOfKey(stickerSet.set.id) >= 0);
                                                b4 = (this.removingStickerSets.indexOfKey(stickerSet.set.id) >= 0);
                                                Label_0340: {
                                                    if (!b3) {
                                                        b5 = b3;
                                                        if (!(b6 = b4)) {
                                                            break Label_0340;
                                                        }
                                                    }
                                                    if (b3 && featuredStickerSetInfoCell.isInstalled()) {
                                                        this.installingStickerSets.remove(stickerSet.set.id);
                                                        b5 = false;
                                                        b6 = b4;
                                                    }
                                                    else {
                                                        b5 = b3;
                                                        if (b6 = b4) {
                                                            b5 = b3;
                                                            b6 = b4;
                                                            if (!featuredStickerSetInfoCell.isInstalled()) {
                                                                this.removingStickerSets.remove(stickerSet.set.id);
                                                                b6 = false;
                                                                b5 = b3;
                                                            }
                                                        }
                                                    }
                                                }
                                                drawProgress = b;
                                                if (!b5) {
                                                    drawProgress = (b6 && b);
                                                }
                                                featuredStickerSetInfoCell.setDrawProgress(drawProgress);
                                            }
                                        }
                                    }
                                    ++i;
                                    continue Label_0040_Outer;
                                    recyclerListView = this.stickersGridView;
                                    continue Label_0035_Outer;
                                }
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                            break;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    public void addEmojiToRecent(final String s) {
        if (!Emoji.isValidEmoji(s)) {
            return;
        }
        Emoji.recentEmoji.size();
        Emoji.addRecentEmoji(s);
        if (this.getVisibility() != 0 || this.pager.getCurrentItem() != 0) {
            Emoji.sortEmoji();
            this.emojiAdapter.notifyDataSetChanged();
        }
        Emoji.saveRecentEmoji();
    }
    
    public void addRecentGif(final TLRPC.Document document) {
        if (document == null) {
            return;
        }
        final boolean empty = this.recentGifs.isEmpty();
        this.recentGifs = DataQuery.getInstance(this.currentAccount).getRecentGifs();
        final GifAdapter gifAdapter = this.gifAdapter;
        if (gifAdapter != null) {
            ((RecyclerView.Adapter)gifAdapter).notifyDataSetChanged();
        }
        if (empty) {
            this.updateStickerTabs();
        }
    }
    
    public void addRecentSticker(final TLRPC.Document document) {
        if (document == null) {
            return;
        }
        DataQuery.getInstance(this.currentAccount).addRecentSticker(0, null, document, (int)(System.currentTimeMillis() / 1000L), false);
        final boolean empty = this.recentStickers.isEmpty();
        this.recentStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(0);
        final StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        if (stickersGridAdapter != null) {
            stickersGridAdapter.notifyDataSetChanged();
        }
        if (empty) {
            this.updateStickerTabs();
        }
    }
    
    public boolean areThereAnyStickers() {
        final StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        return stickersGridAdapter != null && stickersGridAdapter.getItemCount() > 0;
    }
    
    public void clearRecentEmoji() {
        Emoji.clearRecentEmoji();
        this.emojiAdapter.notifyDataSetChanged();
    }
    
    public void closeSearch(final boolean b) {
        this.closeSearch(b, -1L);
    }
    
    public void closeSearch(final boolean b, final long n) {
        final AnimatorSet searchAnimation = this.searchAnimation;
        if (searchAnimation != null) {
            searchAnimation.cancel();
            this.searchAnimation = null;
        }
        final int currentItem = this.pager.getCurrentItem();
        int n2 = 2;
        if (currentItem == 2 && n != -1L) {
            final TLRPC.TL_messages_stickerSet stickerSetById = DataQuery.getInstance(this.currentAccount).getStickerSetById(n);
            if (stickerSetById != null) {
                final int positionForPack = this.stickersGridAdapter.getPositionForPack(stickerSetById);
                if (positionForPack >= 0) {
                    this.stickersLayoutManager.scrollToPositionWithOffset(positionForPack, AndroidUtilities.dp(60.0f));
                }
            }
        }
        int n3;
        for (int i = 0; i < 3; ++i, n2 = n3) {
            SearchField searchField;
            RecyclerListView recyclerListView;
            GridLayoutManager gridLayoutManager;
            ScrollSlidingTabStrip scrollSlidingTabStrip;
            if (i == 0) {
                searchField = this.emojiSearchField;
                recyclerListView = this.emojiGridView;
                gridLayoutManager = this.emojiLayoutManager;
                scrollSlidingTabStrip = this.emojiTabs;
            }
            else if (i == 1) {
                searchField = this.gifSearchField;
                recyclerListView = this.gifGridView;
                gridLayoutManager = this.gifLayoutManager;
                scrollSlidingTabStrip = null;
            }
            else {
                searchField = this.stickersSearchField;
                recyclerListView = this.stickersGridView;
                gridLayoutManager = this.stickersLayoutManager;
                scrollSlidingTabStrip = this.stickersTab;
            }
            if (searchField == null) {
                n3 = n2;
            }
            else {
                searchField.searchEditText.setText((CharSequence)"");
                if (i == currentItem && b) {
                    this.searchAnimation = new AnimatorSet();
                    if (scrollSlidingTabStrip != null) {
                        final AnimatorSet searchAnimation2 = this.searchAnimation;
                        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)scrollSlidingTabStrip, View.TRANSLATION_Y, new float[] { 0.0f });
                        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)recyclerListView, View.TRANSLATION_Y, new float[] { (float)(AndroidUtilities.dp(48.0f) - this.searchFieldHeight) });
                        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)searchField, View.TRANSLATION_Y, new float[] { (float)(AndroidUtilities.dp(48.0f) - this.searchFieldHeight) });
                        n2 = 2;
                        searchAnimation2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3 });
                    }
                    else {
                        final AnimatorSet searchAnimation3 = this.searchAnimation;
                        final Animator[] array = new Animator[n2];
                        array[0] = (Animator)ObjectAnimator.ofFloat((Object)recyclerListView, View.TRANSLATION_Y, new float[] { (float)(-this.searchFieldHeight) });
                        array[1] = (Animator)ObjectAnimator.ofFloat((Object)searchField, View.TRANSLATION_Y, new float[] { (float)(-this.searchFieldHeight) });
                        searchAnimation3.playTogether(array);
                    }
                    this.searchAnimation.setDuration(200L);
                    this.searchAnimation.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT_QUINT);
                    this.searchAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationCancel(final Animator animator) {
                            if (animator.equals(EmojiView.this.searchAnimation)) {
                                EmojiView.this.searchAnimation = null;
                            }
                        }
                        
                        public void onAnimationEnd(final Animator animator) {
                            if (animator.equals(EmojiView.this.searchAnimation)) {
                                gridLayoutManager.findFirstVisibleItemPosition();
                                final int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                                int n;
                                if (firstVisibleItemPosition != -1) {
                                    n = (int)(gridLayoutManager.findViewByPosition(firstVisibleItemPosition).getTop() + recyclerListView.getTranslationY());
                                }
                                else {
                                    n = 0;
                                }
                                recyclerListView.setTranslationY(0.0f);
                                if (recyclerListView == EmojiView.this.stickersGridView) {
                                    recyclerListView.setPadding(0, AndroidUtilities.dp(52.0f), 0, 0);
                                }
                                else if (recyclerListView == EmojiView.this.emojiGridView) {
                                    recyclerListView.setPadding(0, AndroidUtilities.dp(38.0f), 0, 0);
                                }
                                if (recyclerListView == EmojiView.this.gifGridView) {
                                    gridLayoutManager.scrollToPositionWithOffset(1, 0);
                                }
                                else if (firstVisibleItemPosition != -1) {
                                    gridLayoutManager.scrollToPositionWithOffset(firstVisibleItemPosition, n - recyclerListView.getPaddingTop());
                                }
                                EmojiView.this.searchAnimation = null;
                            }
                        }
                    });
                    this.searchAnimation.start();
                    n3 = n2;
                }
                else {
                    gridLayoutManager.scrollToPositionWithOffset(1, 0);
                    searchField.setTranslationY((float)(AndroidUtilities.dp(48.0f) - this.searchFieldHeight));
                    if (scrollSlidingTabStrip != null) {
                        scrollSlidingTabStrip.setTranslationY(0.0f);
                    }
                    if (recyclerListView == this.stickersGridView) {
                        recyclerListView.setPadding(0, AndroidUtilities.dp(52.0f), 0, 0);
                        n3 = n2;
                    }
                    else {
                        n3 = n2;
                        if (recyclerListView == this.emojiGridView) {
                            recyclerListView.setPadding(0, AndroidUtilities.dp(38.0f), 0, 0);
                            n3 = n2;
                        }
                    }
                }
            }
        }
        if (!b) {
            this.delegate.onSearchOpenClose(0);
        }
        this.showBottomTab(true, b);
    }
    
    public void didReceivedNotification(int i, int childCount, final Object... array) {
        final int stickersDidLoad = NotificationCenter.stickersDidLoad;
        final int n = 0;
        childCount = 0;
        if (i == stickersDidLoad) {
            if ((int)array[0] == 0) {
                final TrendingGridAdapter trendingGridAdapter = this.trendingGridAdapter;
                if (trendingGridAdapter != null) {
                    if (this.trendingLoaded) {
                        this.updateVisibleTrendingSets();
                    }
                    else {
                        trendingGridAdapter.notifyDataSetChanged();
                    }
                }
                this.updateStickerTabs();
                this.reloadStickersAdapter();
                this.checkPanels();
            }
        }
        else if (i == NotificationCenter.recentDocumentsDidLoad) {
            final boolean booleanValue = (boolean)array[0];
            i = (int)array[1];
            if (booleanValue || i == 0 || i == 2) {
                this.checkDocuments(booleanValue);
            }
        }
        else if (i == NotificationCenter.featuredStickersDidLoad) {
            if (this.trendingGridAdapter != null) {
                if (this.featuredStickersHash != DataQuery.getInstance(this.currentAccount).getFeaturesStickersHashWithoutUnread()) {
                    this.trendingLoaded = false;
                }
                if (this.trendingLoaded) {
                    this.updateVisibleTrendingSets();
                }
                else {
                    this.trendingGridAdapter.notifyDataSetChanged();
                }
            }
            final PagerSlidingTabStrip typeTabs = this.typeTabs;
            if (typeTabs != null) {
                int childCount2;
                for (childCount2 = typeTabs.getChildCount(), i = childCount; i < childCount2; ++i) {
                    this.typeTabs.getChildAt(i).invalidate();
                }
            }
            this.updateStickerTabs();
        }
        else if (i == NotificationCenter.groupStickersDidLoad) {
            final TLRPC.ChatFull info = this.info;
            if (info != null) {
                final TLRPC.StickerSet stickerset = info.stickerset;
                if (stickerset != null && stickerset.id == (long)array[0]) {
                    this.updateStickerTabs();
                }
            }
        }
        else if (i == NotificationCenter.emojiDidLoad) {
            final RecyclerListView stickersGridView = this.stickersGridView;
            if (stickersGridView != null) {
                View child;
                for (childCount = stickersGridView.getChildCount(), i = n; i < childCount; ++i) {
                    child = this.stickersGridView.getChildAt(i);
                    if (child instanceof StickerSetNameCell || child instanceof StickerEmojiCell) {
                        child.invalidate();
                    }
                }
            }
        }
        else if (i == NotificationCenter.newEmojiSuggestionsAvailable && this.emojiGridView != null && this.needEmojiSearch && (this.emojiSearchField.progressDrawable.isAnimating() || this.emojiGridView.getAdapter() == this.emojiSearchAdapter) && !TextUtils.isEmpty((CharSequence)this.emojiSearchAdapter.lastSearchEmojiString)) {
            final EmojiSearchAdapter emojiSearchAdapter = this.emojiSearchAdapter;
            emojiSearchAdapter.search(emojiSearchAdapter.lastSearchEmojiString);
        }
    }
    
    public int getCurrentPage() {
        return this.currentPage;
    }
    
    public void hideSearchKeyboard() {
        final SearchField stickersSearchField = this.stickersSearchField;
        if (stickersSearchField != null) {
            stickersSearchField.hideKeyboard();
        }
        final SearchField gifSearchField = this.gifSearchField;
        if (gifSearchField != null) {
            gifSearchField.hideKeyboard();
        }
        final SearchField emojiSearchField = this.emojiSearchField;
        if (emojiSearchField != null) {
            emojiSearchField.hideKeyboard();
        }
    }
    
    public void invalidateViews() {
        this.emojiGridView.invalidateViews();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
        if (this.stickersGridAdapter != null) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
            AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$nydI8RFLQx0Boo3VLUW9Gt2FzYg(this));
        }
    }
    
    public void onDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
        if (this.stickersGridAdapter != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentDocumentsDidLoad);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final EmojiPopupWindow pickerViewPopup = this.pickerViewPopup;
        if (pickerViewPopup != null && pickerViewPopup.isShowing()) {
            this.pickerViewPopup.dismiss();
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        final int lastNotifyWidth = this.lastNotifyWidth;
        final int lastNotifyWidth2 = n3 - n;
        if (lastNotifyWidth != lastNotifyWidth2) {
            this.lastNotifyWidth = lastNotifyWidth2;
            this.reloadStickersAdapter();
        }
        final View view = (View)this.getParent();
        if (view != null) {
            final int lastNotifyHeight = n4 - n2;
            final int height = view.getHeight();
            if (this.lastNotifyHeight != lastNotifyHeight || this.lastNotifyHeight2 != height) {
                final EmojiViewDelegate delegate = this.delegate;
                if (delegate != null && delegate.isSearchOpened()) {
                    this.bottomTabContainer.setTranslationY((float)AndroidUtilities.dp(49.0f));
                }
                else if (this.bottomTabContainer.getTag() == null) {
                    if (lastNotifyHeight < this.lastNotifyHeight) {
                        this.bottomTabContainer.setTranslationY(0.0f);
                    }
                    else {
                        this.bottomTabContainer.setTranslationY(-(this.getY() + this.getMeasuredHeight() - view.getHeight()));
                    }
                }
                this.lastNotifyHeight = lastNotifyHeight;
                this.lastNotifyHeight2 = height;
            }
        }
        super.onLayout(b, n, n2, n3, n4);
    }
    
    public void onMeasure(final int n, final int n2) {
        this.isLayout = true;
        if (!AndroidUtilities.isInMultiwindow && !this.forseMultiwindowLayout) {
            if (this.currentBackgroundType != 0) {
                if (Build$VERSION.SDK_INT >= 21) {
                    this.setOutlineProvider((ViewOutlineProvider)null);
                    this.setClipToOutline(false);
                    this.setElevation(0.0f);
                }
                this.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
                if (this.needEmojiSearch) {
                    this.bottomTabContainerBackground.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
                }
                this.currentBackgroundType = 0;
            }
        }
        else if (this.currentBackgroundType != 1) {
            if (Build$VERSION.SDK_INT >= 21) {
                this.setOutlineProvider((ViewOutlineProvider)this.outlineProvider);
                this.setClipToOutline(true);
                this.setElevation((float)AndroidUtilities.dp(2.0f));
            }
            this.setBackgroundResource(2131165847);
            this.getBackground().setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackground"), PorterDuff$Mode.MULTIPLY));
            if (this.needEmojiSearch) {
                this.bottomTabContainerBackground.setBackgroundDrawable((Drawable)null);
            }
            this.currentBackgroundType = 1;
        }
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2), 1073741824));
        this.isLayout = false;
    }
    
    public void onOpen(final boolean b) {
        if (this.currentPage != 0 && this.currentChatId != 0) {
            this.currentPage = 0;
        }
        if (this.currentPage != 0 && !b && this.views.size() != 1) {
            final int currentPage = this.currentPage;
            if (currentPage == 1) {
                this.showBackspaceButton(false, false);
                this.showStickerSettingsButton(true, false);
                if (this.pager.getCurrentItem() != 2) {
                    this.pager.setCurrentItem(2, false);
                }
                if (this.stickersTab != null) {
                    if (this.trendingTabNum == 0 && DataQuery.getInstance(this.currentAccount).areAllTrendingStickerSetsUnread()) {
                        this.showTrendingTab(true);
                    }
                    else {
                        final int recentTabBum = this.recentTabBum;
                        if (recentTabBum >= 0) {
                            this.stickersTab.selectTab(recentTabBum);
                        }
                        else {
                            final int favTabBum = this.favTabBum;
                            if (favTabBum >= 0) {
                                this.stickersTab.selectTab(favTabBum);
                            }
                            else {
                                this.stickersTab.selectTab(this.stickersTabOffset);
                            }
                        }
                    }
                }
            }
            else if (currentPage == 2) {
                this.showBackspaceButton(false, false);
                this.showStickerSettingsButton(false, false);
                if (this.pager.getCurrentItem() != 1) {
                    this.pager.setCurrentItem(1, false);
                }
            }
        }
        else {
            this.showBackspaceButton(true, false);
            this.showStickerSettingsButton(false, false);
            if (this.pager.getCurrentItem() != 0) {
                this.pager.setCurrentItem(0, b ^ true);
            }
        }
    }
    
    public void requestLayout() {
        if (this.isLayout) {
            return;
        }
        super.requestLayout();
    }
    
    public void setChatInfo(final TLRPC.ChatFull info) {
        this.info = info;
        this.updateStickerTabs();
    }
    
    public void setDelegate(final EmojiViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setDragListener(final DragListener dragListener) {
        this.dragListener = dragListener;
    }
    
    public void setForseMultiwindowLayout(final boolean forseMultiwindowLayout) {
        this.forseMultiwindowLayout = forseMultiwindowLayout;
    }
    
    public void setStickersBanned(final boolean b, final int currentChatId) {
        if (this.typeTabs == null) {
            return;
        }
        if (b) {
            this.currentChatId = currentChatId;
        }
        else {
            this.currentChatId = 0;
        }
        final View tab = this.typeTabs.getTab(2);
        if (tab != null) {
            float alpha;
            if (this.currentChatId != 0) {
                alpha = 0.5f;
            }
            else {
                alpha = 1.0f;
            }
            tab.setAlpha(alpha);
            if (this.currentChatId != 0 && this.pager.getCurrentItem() != 0) {
                this.showBackspaceButton(true, true);
                this.showStickerSettingsButton(false, true);
                this.pager.setCurrentItem(0, false);
            }
        }
    }
    
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        if (this.bottomTabContainer.getTag() == null) {
            final EmojiViewDelegate delegate = this.delegate;
            if (delegate == null || !delegate.isSearchOpened()) {
                final View view = (View)this.getParent();
                if (view != null) {
                    final float y = this.getY();
                    final float n = (float)this.getMeasuredHeight();
                    translationY = (float)view.getHeight();
                    this.bottomTabContainer.setTranslationY(-(y + n - translationY));
                }
            }
        }
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        if (visibility != 8) {
            Emoji.sortEmoji();
            this.emojiAdapter.notifyDataSetChanged();
            if (this.stickersGridAdapter != null) {
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentDocumentsDidLoad);
                this.updateStickerTabs();
                this.reloadStickersAdapter();
            }
            final TrendingGridAdapter trendingGridAdapter = this.trendingGridAdapter;
            if (trendingGridAdapter != null) {
                this.trendingLoaded = false;
                trendingGridAdapter.notifyDataSetChanged();
            }
            this.checkDocuments(true);
            this.checkDocuments(false);
            DataQuery.getInstance(this.currentAccount).loadRecents(0, true, true, false);
            DataQuery.getInstance(this.currentAccount).loadRecents(0, false, true, false);
            DataQuery.getInstance(this.currentAccount).loadRecents(2, false, true, false);
        }
    }
    
    public void showSearchField(final boolean b) {
        for (int i = 0; i < 3; ++i) {
            GridLayoutManager gridLayoutManager;
            ScrollSlidingTabStrip scrollSlidingTabStrip;
            if (i == 0) {
                gridLayoutManager = this.emojiLayoutManager;
                scrollSlidingTabStrip = this.emojiTabs;
            }
            else if (i == 1) {
                gridLayoutManager = this.gifLayoutManager;
                scrollSlidingTabStrip = null;
            }
            else {
                gridLayoutManager = this.stickersLayoutManager;
                scrollSlidingTabStrip = this.stickersTab;
            }
            if (gridLayoutManager != null) {
                final int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                if (b) {
                    if (firstVisibleItemPosition == 1 || firstVisibleItemPosition == 2) {
                        gridLayoutManager.scrollToPosition(0);
                        if (scrollSlidingTabStrip != null) {
                            scrollSlidingTabStrip.setTranslationY(0.0f);
                        }
                    }
                }
                else if (firstVisibleItemPosition == 0) {
                    gridLayoutManager.scrollToPositionWithOffset(1, 0);
                }
            }
        }
    }
    
    public void showStickerBanHint(final boolean b) {
        if (this.mediaBanTooltip.getVisibility() == 0) {
            return;
        }
        final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(this.currentChatId);
        if (chat == null) {
            return;
        }
        Label_0233: {
            if (!ChatObject.hasAdminRights(chat)) {
                final TLRPC.TL_chatBannedRights default_banned_rights = chat.default_banned_rights;
                if (default_banned_rights != null && default_banned_rights.send_stickers) {
                    if (b) {
                        this.mediaBanTooltip.setText((CharSequence)LocaleController.getString("GlobalAttachGifRestricted", 2131559590));
                        break Label_0233;
                    }
                    this.mediaBanTooltip.setText((CharSequence)LocaleController.getString("GlobalAttachStickersRestricted", 2131559593));
                    break Label_0233;
                }
            }
            final TLRPC.TL_chatBannedRights banned_rights = chat.banned_rights;
            if (banned_rights == null) {
                return;
            }
            if (AndroidUtilities.isBannedForever(banned_rights)) {
                if (b) {
                    this.mediaBanTooltip.setText((CharSequence)LocaleController.getString("AttachGifRestrictedForever", 2131558718));
                }
                else {
                    this.mediaBanTooltip.setText((CharSequence)LocaleController.getString("AttachStickersRestrictedForever", 2131558732));
                }
            }
            else if (b) {
                this.mediaBanTooltip.setText((CharSequence)LocaleController.formatString("AttachGifRestricted", 2131558717, LocaleController.formatDateForBan(chat.banned_rights.until_date)));
            }
            else {
                this.mediaBanTooltip.setText((CharSequence)LocaleController.formatString("AttachStickersRestricted", 2131558731, LocaleController.formatDateForBan(chat.banned_rights.until_date)));
            }
        }
        this.mediaBanTooltip.setVisibility(0);
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.mediaBanTooltip, View.ALPHA, new float[] { 0.0f, 1.0f }) });
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$34$syxP2tKTMRS9T48q548gWNuUVOM(this), 5000L);
            }
        });
        set.setDuration(300L);
        set.start();
    }
    
    public void switchToGifRecent() {
        this.showBackspaceButton(false, false);
        this.showStickerSettingsButton(false, false);
        this.pager.setCurrentItem(1, false);
    }
    
    public void updateUIColors() {
        if (!AndroidUtilities.isInMultiwindow && !this.forseMultiwindowLayout) {
            this.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            if (this.needEmojiSearch) {
                this.bottomTabContainerBackground.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            }
        }
        else {
            final Drawable background = this.getBackground();
            if (background != null) {
                background.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackground"), PorterDuff$Mode.MULTIPLY));
            }
        }
        final ScrollSlidingTabStrip emojiTabs = this.emojiTabs;
        if (emojiTabs != null) {
            emojiTabs.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            this.emojiTabsShadow.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
        }
        for (int i = 0; i < 3; ++i) {
            SearchField searchField;
            if (i == 0) {
                searchField = this.stickersSearchField;
            }
            else if (i == 1) {
                searchField = this.emojiSearchField;
            }
            else {
                searchField = this.gifSearchField;
            }
            if (searchField != null) {
                searchField.backgroundView.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
                searchField.shadowView.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
                searchField.clearSearchImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), PorterDuff$Mode.MULTIPLY));
                searchField.searchIconImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), PorterDuff$Mode.MULTIPLY));
                Theme.setDrawableColorByKey(searchField.searchBackground.getBackground(), "chat_emojiSearchBackground");
                searchField.searchBackground.invalidate();
                searchField.searchEditText.setHintTextColor(Theme.getColor("chat_emojiSearchIcon"));
                searchField.searchEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            }
        }
        final Paint dotPaint = this.dotPaint;
        if (dotPaint != null) {
            dotPaint.setColor(Theme.getColor("chat_emojiPanelNewTrending"));
        }
        final RecyclerListView emojiGridView = this.emojiGridView;
        if (emojiGridView != null) {
            emojiGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
        }
        final RecyclerListView stickersGridView = this.stickersGridView;
        if (stickersGridView != null) {
            stickersGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
        }
        final RecyclerListView trendingGridView = this.trendingGridView;
        if (trendingGridView != null) {
            trendingGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
        }
        final ScrollSlidingTabStrip stickersTab = this.stickersTab;
        if (stickersTab != null) {
            stickersTab.setIndicatorColor(Theme.getColor("chat_emojiPanelStickerPackSelectorLine"));
            this.stickersTab.setUnderlineColor(Theme.getColor("chat_emojiPanelShadowLine"));
            this.stickersTab.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
        }
        final ImageView backspaceButton = this.backspaceButton;
        if (backspaceButton != null) {
            backspaceButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), PorterDuff$Mode.MULTIPLY));
        }
        final ImageView stickerSettingsButton = this.stickerSettingsButton;
        if (stickerSettingsButton != null) {
            stickerSettingsButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), PorterDuff$Mode.MULTIPLY));
        }
        final ImageView searchButton = this.searchButton;
        if (searchButton != null) {
            searchButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), PorterDuff$Mode.MULTIPLY));
        }
        final View shadowLine = this.shadowLine;
        if (shadowLine != null) {
            shadowLine.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
        }
        final TextView mediaBanTooltip = this.mediaBanTooltip;
        if (mediaBanTooltip != null) {
            ((ShapeDrawable)mediaBanTooltip.getBackground()).getPaint().setColor(Theme.getColor("chat_gifSaveHintBackground"));
            this.mediaBanTooltip.setTextColor(Theme.getColor("chat_gifSaveHintText"));
        }
        final TextView stickersCounter = this.stickersCounter;
        if (stickersCounter != null) {
            stickersCounter.setTextColor(Theme.getColor("chat_emojiPanelBadgeText"));
            Theme.setDrawableColor(this.stickersCounter.getBackground(), Theme.getColor("chat_emojiPanelBadgeBackground"));
            this.stickersCounter.invalidate();
        }
        int n = 0;
        while (true) {
            final Drawable[] tabIcons = this.tabIcons;
            if (n >= tabIcons.length) {
                break;
            }
            Theme.setEmojiDrawableColor(tabIcons[n], Theme.getColor("chat_emojiBottomPanelIcon"), false);
            Theme.setEmojiDrawableColor(this.tabIcons[n], Theme.getColor("chat_emojiPanelIconSelected"), true);
            ++n;
        }
        int n2 = 0;
        while (true) {
            final Drawable[] emojiIcons = this.emojiIcons;
            if (n2 >= emojiIcons.length) {
                break;
            }
            Theme.setEmojiDrawableColor(emojiIcons[n2], Theme.getColor("chat_emojiPanelIcon"), false);
            Theme.setEmojiDrawableColor(this.emojiIcons[n2], Theme.getColor("chat_emojiPanelIconSelected"), true);
            ++n2;
        }
        int n3 = 0;
        while (true) {
            final Drawable[] stickerIcons = this.stickerIcons;
            if (n3 >= stickerIcons.length) {
                break;
            }
            Theme.setEmojiDrawableColor(stickerIcons[n3], Theme.getColor("chat_emojiPanelIcon"), false);
            Theme.setEmojiDrawableColor(this.stickerIcons[n3], Theme.getColor("chat_emojiPanelIconSelected"), true);
            ++n3;
        }
    }
    
    public interface DragListener
    {
        void onDrag(final int p0);
        
        void onDragCancel();
        
        void onDragEnd(final float p0);
        
        void onDragStart();
    }
    
    private class EmojiColorPickerView extends View
    {
        private Drawable arrowDrawable;
        private int arrowX;
        private Drawable backgroundDrawable;
        private String currentEmoji;
        private RectF rect;
        private Paint rectPaint;
        private int selection;
        
        public EmojiColorPickerView(final Context context) {
            super(context);
            this.rectPaint = new Paint(1);
            this.rect = new RectF();
            this.backgroundDrawable = this.getResources().getDrawable(2131165859);
            this.arrowDrawable = this.getResources().getDrawable(2131165860);
        }
        
        public String getEmoji() {
            return this.currentEmoji;
        }
        
        public int getSelection() {
            return this.selection;
        }
        
        protected void onDraw(final Canvas canvas) {
            final Drawable backgroundDrawable = this.backgroundDrawable;
            final int measuredWidth = this.getMeasuredWidth();
            float n;
            if (AndroidUtilities.isTablet()) {
                n = 60.0f;
            }
            else {
                n = 52.0f;
            }
            final int dp = AndroidUtilities.dp(n);
            int i = 0;
            backgroundDrawable.setBounds(0, 0, measuredWidth, dp);
            this.backgroundDrawable.draw(canvas);
            final Drawable arrowDrawable = this.arrowDrawable;
            final int arrowX = this.arrowX;
            final int dp2 = AndroidUtilities.dp(9.0f);
            final boolean tablet = AndroidUtilities.isTablet();
            final float n2 = 55.5f;
            float n3;
            if (tablet) {
                n3 = 55.5f;
            }
            else {
                n3 = 47.5f;
            }
            final int dp3 = AndroidUtilities.dp(n3);
            final int arrowX2 = this.arrowX;
            final int dp4 = AndroidUtilities.dp(9.0f);
            float n4;
            if (AndroidUtilities.isTablet()) {
                n4 = n2;
            }
            else {
                n4 = 47.5f;
            }
            arrowDrawable.setBounds(arrowX - dp2, dp3, arrowX2 + dp4, AndroidUtilities.dp(n4 + 8.0f));
            this.arrowDrawable.draw(canvas);
            if (this.currentEmoji != null) {
                while (i < 6) {
                    final int n5 = EmojiView.this.emojiSize * i + AndroidUtilities.dp((float)(i * 4 + 5));
                    final int dp5 = AndroidUtilities.dp(9.0f);
                    if (this.selection == i) {
                        this.rect.set((float)n5, (float)(dp5 - (int)AndroidUtilities.dpf2(3.5f)), (float)(EmojiView.this.emojiSize + n5), (float)(EmojiView.this.emojiSize + dp5 + AndroidUtilities.dp(3.0f)));
                        canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(4.0f), this.rectPaint);
                    }
                    String s = this.currentEmoji;
                    if (i != 0) {
                        String s2;
                        if (i != 1) {
                            if (i != 2) {
                                if (i != 3) {
                                    if (i != 4) {
                                        if (i != 5) {
                                            s2 = "";
                                        }
                                        else {
                                            s2 = "\ud83c\udfff";
                                        }
                                    }
                                    else {
                                        s2 = "\ud83c\udffe";
                                    }
                                }
                                else {
                                    s2 = "\ud83c\udffd";
                                }
                            }
                            else {
                                s2 = "\ud83c\udffc";
                            }
                        }
                        else {
                            s2 = "\ud83c\udffb";
                        }
                        s = addColorToCode(s, s2);
                    }
                    final Drawable emojiBigDrawable = Emoji.getEmojiBigDrawable(s);
                    if (emojiBigDrawable != null) {
                        emojiBigDrawable.setBounds(n5, dp5, EmojiView.this.emojiSize + n5, EmojiView.this.emojiSize + dp5);
                        emojiBigDrawable.draw(canvas);
                    }
                    ++i;
                }
            }
        }
        
        public void setEmoji(final String currentEmoji, final int arrowX) {
            this.currentEmoji = currentEmoji;
            this.arrowX = arrowX;
            this.rectPaint.setColor(788529152);
            this.invalidate();
        }
        
        public void setSelection(final int selection) {
            if (this.selection == selection) {
                return;
            }
            this.selection = selection;
            this.invalidate();
        }
    }
    
    private class EmojiGridAdapter extends SelectionAdapter
    {
        private int itemCount;
        private SparseIntArray positionToSection;
        private SparseIntArray sectionToPosition;
        
        private EmojiGridAdapter() {
            this.positionToSection = new SparseIntArray();
            this.sectionToPosition = new SparseIntArray();
        }
        
        @Override
        public int getItemCount() {
            return this.itemCount;
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (EmojiView.this.needEmojiSearch && n == 0) {
                return 2;
            }
            if (this.positionToSection.indexOfKey(n) >= 0) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void notifyDataSetChanged() {
            this.positionToSection.clear();
            this.itemCount = Emoji.recentEmoji.size() + (EmojiView.this.needEmojiSearch ? 1 : 0);
            for (int i = 0; i < EmojiData.dataColored.length; ++i) {
                this.positionToSection.put(this.itemCount, i);
                this.sectionToPosition.put(i, this.itemCount);
                this.itemCount += EmojiData.dataColored[i].length + 1;
            }
            EmojiView.this.updateEmojiTabs();
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int size) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = false;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    ((StickerSetNameCell)viewHolder.itemView).setText(EmojiView.this.emojiTitles[this.positionToSection.get(size)], 0);
                }
            }
            else {
                final ImageViewEmoji imageViewEmoji = (ImageViewEmoji)viewHolder.itemView;
                int index = size;
                if (EmojiView.this.needEmojiSearch) {
                    index = size - 1;
                }
                size = Emoji.recentEmoji.size();
                String s = null;
                String access$1700 = null;
                Label_0202: {
                    if (index < size) {
                        access$1700 = (s = Emoji.recentEmoji.get(index));
                        b = true;
                    }
                    else {
                        int n = 0;
                        String s2;
                        while (true) {
                            final String[][] dataColored = EmojiData.dataColored;
                            if (n >= dataColored.length) {
                                s2 = null;
                                break;
                            }
                            final int n2 = dataColored[n].length + 1 + size;
                            if (index < n2) {
                                s = dataColored[n][index - size - 1];
                                final String s3 = Emoji.emojiColor.get(s);
                                s2 = s;
                                if (s3 != null) {
                                    access$1700 = addColorToCode(s, s3);
                                    break Label_0202;
                                }
                                break;
                            }
                            else {
                                ++n;
                                size = n2;
                            }
                        }
                        final String s4 = s2;
                        s = s2;
                        access$1700 = s4;
                    }
                }
                imageViewEmoji.setImageDrawable(Emoji.getEmojiBigDrawable(access$1700), b);
                imageViewEmoji.setTag((Object)s);
                imageViewEmoji.setContentDescription((CharSequence)access$1700);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    o = new View(EmojiView.this.getContext());
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
                }
                else {
                    o = new StickerSetNameCell(EmojiView.this.getContext(), true);
                }
            }
            else {
                final EmojiView this$0 = EmojiView.this;
                o = this$0.new ImageViewEmoji(this$0.getContext());
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class EmojiPagesAdapter extends PagerAdapter implements IconTabProvider
    {
        @Override
        public boolean canScrollToTab(final int n) {
            boolean b = true;
            if ((n == 1 || n == 2) && EmojiView.this.currentChatId != 0) {
                final EmojiView this$0 = EmojiView.this;
                if (n != 1) {
                    b = false;
                }
                this$0.showStickerBanHint(b);
                return false;
            }
            return true;
        }
        
        @Override
        public void customOnDraw(final Canvas canvas, int dp) {
            if (dp == 2 && !DataQuery.getInstance(EmojiView.this.currentAccount).getUnreadStickerSets().isEmpty() && EmojiView.this.dotPaint != null) {
                final int n = canvas.getWidth() / 2;
                dp = AndroidUtilities.dp(9.0f);
                canvas.drawCircle((float)(n + dp), (float)(canvas.getHeight() / 2 - AndroidUtilities.dp(8.0f)), (float)AndroidUtilities.dp(5.0f), EmojiView.this.dotPaint);
            }
        }
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int index, final Object o) {
            viewGroup.removeView((View)EmojiView.this.views.get(index));
        }
        
        @Override
        public int getCount() {
            return EmojiView.this.views.size();
        }
        
        @Override
        public Drawable getPageIconDrawable(final int n) {
            return EmojiView.this.tabIcons[n];
        }
        
        @Override
        public CharSequence getPageTitle(final int n) {
            if (n == 0) {
                return LocaleController.getString("Emoji", 2131559331);
            }
            if (n == 1) {
                return LocaleController.getString("AccDescrGIFs", 2131558434);
            }
            if (n != 2) {
                return null;
            }
            return LocaleController.getString("AccDescrStickers", 2131558476);
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int index) {
            final View view = EmojiView.this.views.get(index);
            viewGroup.addView(view);
            return view;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object o) {
            return view == o;
        }
        
        @Override
        public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
            if (dataSetObserver != null) {
                super.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }
    
    private class EmojiPopupWindow extends PopupWindow
    {
        private ViewTreeObserver$OnScrollChangedListener mSuperScrollListener;
        private ViewTreeObserver mViewTreeObserver;
        
        public EmojiPopupWindow() {
            this.init();
        }
        
        public EmojiPopupWindow(final int n, final int n2) {
            super(n, n2);
            this.init();
        }
        
        public EmojiPopupWindow(final Context context) {
            super(context);
            this.init();
        }
        
        public EmojiPopupWindow(final View view) {
            super(view);
            this.init();
        }
        
        public EmojiPopupWindow(final View view, final int n, final int n2) {
            super(view, n, n2);
            this.init();
        }
        
        public EmojiPopupWindow(final View view, final int n, final int n2, final boolean b) {
            super(view, n, n2, b);
            this.init();
        }
        
        private void init() {
            if (EmojiView.superListenerField != null) {
                try {
                    this.mSuperScrollListener = (ViewTreeObserver$OnScrollChangedListener)EmojiView.superListenerField.get(this);
                    EmojiView.superListenerField.set(this, EmojiView.NOP);
                }
                catch (Exception ex) {
                    this.mSuperScrollListener = null;
                }
            }
        }
        
        private void registerListener(final View view) {
            if (this.mSuperScrollListener != null) {
                ViewTreeObserver viewTreeObserver;
                if (view.getWindowToken() != null) {
                    viewTreeObserver = view.getViewTreeObserver();
                }
                else {
                    viewTreeObserver = null;
                }
                final ViewTreeObserver mViewTreeObserver = this.mViewTreeObserver;
                if (viewTreeObserver != mViewTreeObserver) {
                    if (mViewTreeObserver != null && mViewTreeObserver.isAlive()) {
                        this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                    }
                    if ((this.mViewTreeObserver = viewTreeObserver) != null) {
                        viewTreeObserver.addOnScrollChangedListener(this.mSuperScrollListener);
                    }
                }
            }
        }
        
        private void unregisterListener() {
            if (this.mSuperScrollListener != null) {
                final ViewTreeObserver mViewTreeObserver = this.mViewTreeObserver;
                if (mViewTreeObserver != null) {
                    if (mViewTreeObserver.isAlive()) {
                        this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                    }
                    this.mViewTreeObserver = null;
                }
            }
        }
        
        public void dismiss() {
            this.setFocusable(false);
            while (true) {
                try {
                    super.dismiss();
                    this.unregisterListener();
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
        
        public void showAsDropDown(final View view, final int n, final int n2) {
            try {
                super.showAsDropDown(view, n, n2);
                this.registerListener(view);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        public void showAtLocation(final View view, final int n, final int n2, final int n3) {
            super.showAtLocation(view, n, n2, n3);
            this.unregisterListener();
        }
        
        public void update(final View view, final int n, final int n2) {
            super.update(view, n, n2);
            this.registerListener(view);
        }
        
        public void update(final View view, final int n, final int n2, final int n3, final int n4) {
            super.update(view, n, n2, n3, n4);
            this.registerListener(view);
        }
    }
    
    private class EmojiSearchAdapter extends SelectionAdapter
    {
        private String lastSearchAlias;
        private String lastSearchEmojiString;
        private ArrayList<DataQuery.KeywordResult> result;
        private Runnable searchRunnable;
        private boolean searchWas;
        
        private EmojiSearchAdapter() {
            this.result = new ArrayList<DataQuery.KeywordResult>();
        }
        
        @Override
        public int getItemCount() {
            int n;
            if (this.result.isEmpty() && !this.searchWas) {
                n = Emoji.recentEmoji.size();
            }
            else {
                if (this.result.isEmpty()) {
                    return 2;
                }
                n = this.result.size();
            }
            return n + 1;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 1;
            }
            if (n == 1 && this.searchWas && this.result.isEmpty()) {
                return 2;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            if (viewHolder.getItemViewType() == 0) {
                final ImageViewEmoji imageViewEmoji = (ImageViewEmoji)viewHolder.itemView;
                --n;
                String emoji;
                boolean b;
                if (this.result.isEmpty() && !this.searchWas) {
                    emoji = Emoji.recentEmoji.get(n);
                    b = true;
                }
                else {
                    emoji = this.result.get(n).emoji;
                    b = false;
                }
                imageViewEmoji.setImageDrawable(Emoji.getEmojiBigDrawable(emoji), b);
                imageViewEmoji.setTag((Object)emoji);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    o = new FrameLayout(EmojiView.this.getContext()) {
                        protected void onMeasure(final int n, int dp) {
                            final View view = (View)EmojiView.this.getParent();
                            if (view != null) {
                                dp = (int)(view.getMeasuredHeight() - EmojiView.this.getY());
                            }
                            else {
                                dp = AndroidUtilities.dp(120.0f);
                            }
                            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(dp - EmojiView.this.searchFieldHeight, 1073741824));
                        }
                    };
                    final TextView textView = new TextView(EmojiView.this.getContext());
                    textView.setText((CharSequence)LocaleController.getString("NoEmojiFound", 2131559922));
                    textView.setTextSize(1, 16.0f);
                    textView.setTextColor(Theme.getColor("chat_emojiPanelEmptyText"));
                    ((FrameLayout)o).addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 10.0f, 0.0f, 0.0f));
                    final ImageView imageView = new ImageView(EmojiView.this.getContext());
                    imageView.setScaleType(ImageView$ScaleType.CENTER);
                    imageView.setImageResource(2131165842);
                    imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelEmptyText"), PorterDuff$Mode.MULTIPLY));
                    ((FrameLayout)o).addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 85));
                    imageView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            final boolean[] array = { false };
                            final BottomSheet.Builder builder = new BottomSheet.Builder(EmojiView.this.getContext());
                            final LinearLayout customView = new LinearLayout(EmojiView.this.getContext());
                            customView.setOrientation(1);
                            customView.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                            final ImageView imageView = new ImageView(EmojiView.this.getContext());
                            imageView.setImageResource(2131165833);
                            customView.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 15, 0, 0));
                            final TextView textView = new TextView(EmojiView.this.getContext());
                            textView.setText((CharSequence)LocaleController.getString("EmojiSuggestions", 2131559341));
                            textView.setTextSize(1, 15.0f);
                            textView.setTextColor(Theme.getColor("dialogTextBlue2"));
                            final boolean isRTL = LocaleController.isRTL;
                            final int n = 5;
                            int gravity;
                            if (isRTL) {
                                gravity = 5;
                            }
                            else {
                                gravity = 3;
                            }
                            textView.setGravity(gravity);
                            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                            customView.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 51, 0, 24, 0, 0));
                            final TextView textView2 = new TextView(EmojiView.this.getContext());
                            textView2.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("EmojiSuggestionsInfo", 2131559342)));
                            textView2.setTextSize(1, 15.0f);
                            textView2.setTextColor(Theme.getColor("dialogTextBlack"));
                            int gravity2;
                            if (LocaleController.isRTL) {
                                gravity2 = 5;
                            }
                            else {
                                gravity2 = 3;
                            }
                            textView2.setGravity(gravity2);
                            customView.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 51, 0, 11, 0, 0));
                            final TextView textView3 = new TextView(EmojiView.this.getContext());
                            Object o;
                            if (EmojiSearchAdapter.this.lastSearchAlias != null) {
                                o = EmojiSearchAdapter.this.lastSearchAlias;
                            }
                            else {
                                o = EmojiView.this.lastSearchKeyboardLanguage;
                            }
                            textView3.setText((CharSequence)LocaleController.formatString("EmojiSuggestionsUrl", 2131559343, o));
                            textView3.setTextSize(1, 15.0f);
                            textView3.setTextColor(Theme.getColor("dialogTextLink"));
                            int gravity3;
                            if (LocaleController.isRTL) {
                                gravity3 = n;
                            }
                            else {
                                gravity3 = 3;
                            }
                            textView3.setGravity(gravity3);
                            customView.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 51, 0, 18, 0, 16));
                            textView3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                                public void onClick(final View view) {
                                    final boolean[] val$loadingUrl = array;
                                    if (val$loadingUrl[0]) {
                                        return;
                                    }
                                    val$loadingUrl[0] = true;
                                    final AlertDialog[] array = { new AlertDialog(EmojiView.this.getContext(), 3) };
                                    final TLRPC.TL_messages_getEmojiURL tl_messages_getEmojiURL = new TLRPC.TL_messages_getEmojiURL();
                                    String access$12100;
                                    if (EmojiSearchAdapter.this.lastSearchAlias != null) {
                                        access$12100 = EmojiSearchAdapter.this.lastSearchAlias;
                                    }
                                    else {
                                        access$12100 = EmojiView.this.lastSearchKeyboardLanguage[0];
                                    }
                                    tl_messages_getEmojiURL.lang_code = access$12100;
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$TwKjymEFVh1Ja7xLl_9KmhIF6eg(this, array, ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(tl_messages_getEmojiURL, new _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$vESTLZoOoKr_laMmjTYW6gGhW9U(this, array, builder))), 1000L);
                                }
                            });
                            builder.setCustomView((View)customView);
                            builder.show();
                        }
                    });
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
                }
                else {
                    o = new View(EmojiView.this.getContext());
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
                }
            }
            else {
                final EmojiView this$0 = EmojiView.this;
                o = this$0.new ImageViewEmoji(this$0.getContext());
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        public void search(final String s) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                this.lastSearchEmojiString = null;
                if (EmojiView.this.emojiGridView.getAdapter() != EmojiView.this.emojiAdapter) {
                    EmojiView.this.emojiGridView.setAdapter(EmojiView.this.emojiAdapter);
                    this.searchWas = false;
                }
                this.notifyDataSetChanged();
            }
            else {
                this.lastSearchEmojiString = s.toLowerCase();
            }
            final Runnable searchRunnable = this.searchRunnable;
            if (searchRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(searchRunnable);
            }
            if (!TextUtils.isEmpty((CharSequence)this.lastSearchEmojiString)) {
                AndroidUtilities.runOnUIThread(this.searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        EmojiView.this.emojiSearchField.progressDrawable.startAnimation();
                        final String access$10100 = EmojiSearchAdapter.this.lastSearchEmojiString;
                        final String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                        if (!Arrays.equals(EmojiView.this.lastSearchKeyboardLanguage, currentKeyboardLanguage)) {
                            DataQuery.getInstance(EmojiView.this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
                        }
                        EmojiView.this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
                        DataQuery.getInstance(EmojiView.this.currentAccount).getEmojiSuggestions(EmojiView.this.lastSearchKeyboardLanguage, EmojiSearchAdapter.this.lastSearchEmojiString, false, (DataQuery.KeywordResultCallback)new DataQuery.KeywordResultCallback() {
                            @Override
                            public void run(final ArrayList<KeywordResult> list, final String s) {
                                if (access$10100.equals(EmojiSearchAdapter.this.lastSearchEmojiString)) {
                                    EmojiSearchAdapter.this.lastSearchAlias = s;
                                    EmojiView.this.emojiSearchField.progressDrawable.stopAnimation();
                                    EmojiSearchAdapter.this.searchWas = true;
                                    if (EmojiView.this.emojiGridView.getAdapter() != EmojiView.this.emojiSearchAdapter) {
                                        EmojiView.this.emojiGridView.setAdapter(EmojiView.this.emojiSearchAdapter);
                                    }
                                    EmojiSearchAdapter.this.result = list;
                                    EmojiSearchAdapter.this.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }, 300L);
            }
        }
    }
    
    public interface EmojiViewDelegate
    {
        boolean isExpanded();
        
        boolean isSearchOpened();
        
        boolean onBackspace();
        
        void onClearEmojiRecent();
        
        void onEmojiSelected(final String p0);
        
        void onGifSelected(final Object p0, final Object p1);
        
        void onSearchOpenClose(final int p0);
        
        void onShowStickerSet(final TLRPC.StickerSet p0, final TLRPC.InputStickerSet p1);
        
        void onStickerSelected(final TLRPC.Document p0, final Object p1);
        
        void onStickerSetAdd(final TLRPC.StickerSetCovered p0);
        
        void onStickerSetRemove(final TLRPC.StickerSetCovered p0);
        
        void onStickersGroupClick(final int p0);
        
        void onStickersSettingsClick();
        
        void onTabOpened(final int p0);
    }
    
    private class GifAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public GifAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return EmojiView.this.recentGifs.size() + 1;
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            if (viewHolder.getItemViewType() == 0) {
                final TLRPC.Document document = EmojiView.this.recentGifs.get(n - 1);
                if (document != null) {
                    ((ContextLinkCell)viewHolder.itemView).setGif(document, false);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            View view;
            if (n != 0) {
                view = new View(EmojiView.this.getContext());
                view.setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
            }
            else {
                view = new ContextLinkCell(this.mContext);
                view.setContentDescription((CharSequence)LocaleController.getString("AttachGif", 2131558716));
                ((ContextLinkCell)view).setCanPreviewGif(true);
            }
            return new RecyclerListView.Holder(view);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ContextLinkCell) {
                final ImageReceiver photoImage = ((ContextLinkCell)itemView).getPhotoImage();
                if (EmojiView.this.pager.getCurrentItem() == 1) {
                    photoImage.setAllowStartAnimation(true);
                    photoImage.startAnimation();
                }
                else {
                    photoImage.setAllowStartAnimation(false);
                    photoImage.stopAnimation();
                }
            }
        }
    }
    
    private class GifSearchAdapter extends SelectionAdapter
    {
        private TLRPC.User bot;
        private String lastSearchImageString;
        private Context mContext;
        private String nextSearchOffset;
        private int reqId;
        private ArrayList<TLRPC.BotInlineResult> results;
        private HashMap<String, TLRPC.BotInlineResult> resultsMap;
        private boolean searchEndReached;
        private Runnable searchRunnable;
        private boolean searchingUser;
        
        public GifSearchAdapter(final Context mContext) {
            this.results = new ArrayList<TLRPC.BotInlineResult>();
            this.resultsMap = new HashMap<String, TLRPC.BotInlineResult>();
            this.mContext = mContext;
        }
        
        private void search(final String lastSearchImageString, final String offset, final boolean b) {
            if (this.reqId != 0) {
                ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            this.lastSearchImageString = lastSearchImageString;
            final TLObject userOrChat = MessagesController.getInstance(EmojiView.this.currentAccount).getUserOrChat(MessagesController.getInstance(EmojiView.this.currentAccount).gifSearchBot);
            if (!(userOrChat instanceof TLRPC.User)) {
                if (b) {
                    this.searchBotUser();
                    EmojiView.this.gifSearchField.progressDrawable.startAnimation();
                }
                return;
            }
            if (TextUtils.isEmpty((CharSequence)offset)) {
                EmojiView.this.gifSearchField.progressDrawable.startAnimation();
            }
            this.bot = (TLRPC.User)userOrChat;
            final TLRPC.TL_messages_getInlineBotResults tl_messages_getInlineBotResults = new TLRPC.TL_messages_getInlineBotResults();
            String query;
            if ((query = lastSearchImageString) == null) {
                query = "";
            }
            tl_messages_getInlineBotResults.query = query;
            tl_messages_getInlineBotResults.bot = MessagesController.getInstance(EmojiView.this.currentAccount).getInputUser(this.bot);
            tl_messages_getInlineBotResults.offset = offset;
            tl_messages_getInlineBotResults.peer = new TLRPC.TL_inputPeerEmpty();
            this.reqId = ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(tl_messages_getInlineBotResults, new _$$Lambda$EmojiView$GifSearchAdapter$rHXwwVWZa9pK_feyWosndOy6a0o(this, tl_messages_getInlineBotResults, offset), 2);
        }
        
        private void searchBotUser() {
            if (this.searchingUser) {
                return;
            }
            this.searchingUser = true;
            final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
            tl_contacts_resolveUsername.username = MessagesController.getInstance(EmojiView.this.currentAccount).gifSearchBot;
            ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$EmojiView$GifSearchAdapter$rB6iGH2IIIWpfXKf_CfIBRZyOE8(this));
        }
        
        @Override
        public int getItemCount() {
            int size;
            if (this.results.isEmpty()) {
                size = 1;
            }
            else {
                size = this.results.size();
            }
            return size + 1;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 1;
            }
            if (this.results.isEmpty()) {
                return 2;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            if (viewHolder.getItemViewType() == 0) {
                ((ContextLinkCell)viewHolder.itemView).setLink(this.results.get(n - 1), true, false, false);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    o = new FrameLayout(EmojiView.this.getContext()) {
                        protected void onMeasure(final int n, final int n2) {
                            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec((int)((EmojiView.this.gifGridView.getMeasuredHeight() - EmojiView.this.searchFieldHeight - AndroidUtilities.dp(8.0f)) / 3 * 1.7f), 1073741824));
                        }
                    };
                    final ImageView imageView = new ImageView(EmojiView.this.getContext());
                    imageView.setScaleType(ImageView$ScaleType.CENTER);
                    imageView.setImageResource(2131165391);
                    imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelEmptyText"), PorterDuff$Mode.MULTIPLY));
                    ((FrameLayout)o).addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 59.0f));
                    final TextView textView = new TextView(EmojiView.this.getContext());
                    textView.setText((CharSequence)LocaleController.getString("NoGIFsFound", 2131559924));
                    textView.setTextSize(1, 16.0f);
                    textView.setTextColor(Theme.getColor("chat_emojiPanelEmptyText"));
                    ((FrameLayout)o).addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 9.0f));
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
                }
                else {
                    o = new View(EmojiView.this.getContext());
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
                }
            }
            else {
                o = new ContextLinkCell(this.mContext);
                ((View)o).setContentDescription((CharSequence)LocaleController.getString("AttachGif", 2131558716));
                ((ContextLinkCell)o).setCanPreviewGif(true);
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        public void search(final String s) {
            if (this.reqId != 0) {
                ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            if (TextUtils.isEmpty((CharSequence)s)) {
                this.lastSearchImageString = null;
                if (EmojiView.this.gifGridView.getAdapter() != EmojiView.this.gifAdapter) {
                    EmojiView.this.gifGridView.setAdapter(EmojiView.this.gifAdapter);
                }
                this.notifyDataSetChanged();
            }
            else {
                this.lastSearchImageString = s.toLowerCase();
            }
            final Runnable searchRunnable = this.searchRunnable;
            if (searchRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(searchRunnable);
            }
            if (!TextUtils.isEmpty((CharSequence)this.lastSearchImageString)) {
                AndroidUtilities.runOnUIThread(this.searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        GifSearchAdapter.this.search(s, "", true);
                    }
                }, 300L);
            }
        }
    }
    
    private class ImageViewEmoji extends ImageView
    {
        private boolean isRecent;
        
        public ImageViewEmoji(final Context context) {
            super(context);
            this.setScaleType(ImageView$ScaleType.CENTER);
        }
        
        private void sendEmoji(String access$1700) {
            EmojiView.this.showBottomTab(true, true);
            String key;
            if (access$1700 != null) {
                key = access$1700;
            }
            else {
                key = (String)this.getTag();
            }
            new SpannableStringBuilder().append((CharSequence)key);
            if (access$1700 == null) {
                access$1700 = key;
                if (!this.isRecent) {
                    final String s = Emoji.emojiColor.get(key);
                    access$1700 = key;
                    if (s != null) {
                        access$1700 = addColorToCode(key, s);
                    }
                }
                EmojiView.this.addEmojiToRecent(access$1700);
                if (EmojiView.this.delegate != null) {
                    EmojiView.this.delegate.onEmojiSelected(Emoji.fixEmoji(access$1700));
                }
            }
            else if (EmojiView.this.delegate != null) {
                EmojiView.this.delegate.onEmojiSelected(Emoji.fixEmoji(access$1700));
            }
        }
        
        public void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(View$MeasureSpec.getSize(n), View$MeasureSpec.getSize(n));
        }
        
        public void setImageDrawable(final Drawable imageDrawable, final boolean isRecent) {
            super.setImageDrawable(imageDrawable);
            this.isRecent = isRecent;
        }
    }
    
    private class SearchField extends FrameLayout
    {
        private View backgroundView;
        private ImageView clearSearchImageView;
        private CloseProgressDrawable2 progressDrawable;
        private View searchBackground;
        private EditTextBoldCursor searchEditText;
        private ImageView searchIconImageView;
        private AnimatorSet shadowAnimator;
        private View shadowView;
        
        public SearchField(final Context context, final int n) {
            super(context);
            (this.shadowView = new View(context)).setAlpha(0.0f);
            this.shadowView.setTag((Object)1);
            this.shadowView.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
            this.addView(this.shadowView, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83));
            (this.backgroundView = new View(context)).setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            this.addView(this.backgroundView, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, EmojiView.this.searchFieldHeight));
            (this.searchBackground = new View(context)).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), Theme.getColor("chat_emojiSearchBackground")));
            this.addView(this.searchBackground, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 14.0f, 14.0f, 0.0f));
            (this.searchIconImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.searchIconImageView.setImageResource(2131165834);
            this.searchIconImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), PorterDuff$Mode.MULTIPLY));
            this.addView((View)this.searchIconImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 14.0f, 0.0f, 0.0f));
            (this.clearSearchImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.clearSearchImageView.setImageDrawable((Drawable)(this.progressDrawable = new CloseProgressDrawable2()));
            this.progressDrawable.setSide(AndroidUtilities.dp(7.0f));
            this.clearSearchImageView.setScaleX(0.1f);
            this.clearSearchImageView.setScaleY(0.1f);
            this.clearSearchImageView.setAlpha(0.0f);
            this.clearSearchImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), PorterDuff$Mode.MULTIPLY));
            this.addView((View)this.clearSearchImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 14.0f, 14.0f, 0.0f));
            this.clearSearchImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$EmojiView$SearchField$04evSuS5X_E_yJiP7OOqbVezHUE(this));
            (this.searchEditText = new EditTextBoldCursor(context) {
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0) {
                        if (!EmojiView.this.delegate.isSearchOpened()) {
                            final SearchField this$1 = SearchField.this;
                            EmojiView.this.openSearch(this$1);
                        }
                        final EmojiViewDelegate access$000 = EmojiView.this.delegate;
                        final int val$type = n;
                        int n = 1;
                        if (val$type == 1) {
                            n = 2;
                        }
                        access$000.onSearchOpenClose(n);
                        SearchField.this.searchEditText.requestFocus();
                        AndroidUtilities.showKeyboard((View)SearchField.this.searchEditText);
                        if (EmojiView.this.trendingGridView != null && EmojiView.this.trendingGridView.getVisibility() == 0) {
                            EmojiView.this.showTrendingTab(false);
                        }
                    }
                    return super.onTouchEvent(motionEvent);
                }
            }).setTextSize(1, 16.0f);
            this.searchEditText.setHintTextColor(Theme.getColor("chat_emojiSearchIcon"));
            this.searchEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.searchEditText.setBackgroundDrawable((Drawable)null);
            this.searchEditText.setPadding(0, 0, 0, 0);
            this.searchEditText.setMaxLines(1);
            this.searchEditText.setLines(1);
            this.searchEditText.setSingleLine(true);
            this.searchEditText.setImeOptions(268435459);
            if (n == 0) {
                this.searchEditText.setHint((CharSequence)LocaleController.getString("SearchStickersHint", 2131560656));
            }
            else if (n == 1) {
                this.searchEditText.setHint((CharSequence)LocaleController.getString("SearchEmojiHint", 2131560643));
            }
            else if (n == 2) {
                this.searchEditText.setHint((CharSequence)LocaleController.getString("SearchGifsTitle", 2131560649));
            }
            this.searchEditText.setCursorColor(Theme.getColor("featuredStickers_addedIcon"));
            this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.searchEditText.setCursorWidth(1.5f);
            this.addView((View)this.searchEditText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 12.0f, 46.0f, 0.0f));
            this.searchEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    final int length = SearchField.this.searchEditText.length();
                    boolean b = false;
                    final boolean b2 = length > 0;
                    final float alpha = SearchField.this.clearSearchImageView.getAlpha();
                    float n = 0.0f;
                    if (alpha != 0.0f) {
                        b = true;
                    }
                    if (b2 != b) {
                        final ViewPropertyAnimator animate = SearchField.this.clearSearchImageView.animate();
                        final float n2 = 1.0f;
                        if (b2) {
                            n = 1.0f;
                        }
                        final ViewPropertyAnimator setDuration = animate.alpha(n).setDuration(150L);
                        float n3;
                        if (b2) {
                            n3 = 1.0f;
                        }
                        else {
                            n3 = 0.1f;
                        }
                        final ViewPropertyAnimator scaleX = setDuration.scaleX(n3);
                        float n4;
                        if (b2) {
                            n4 = n2;
                        }
                        else {
                            n4 = 0.1f;
                        }
                        scaleX.scaleY(n4).start();
                    }
                    final int val$type = n;
                    if (val$type == 0) {
                        EmojiView.this.stickersSearchGridAdapter.search(SearchField.this.searchEditText.getText().toString());
                    }
                    else if (val$type == 1) {
                        EmojiView.this.emojiSearchAdapter.search(SearchField.this.searchEditText.getText().toString());
                    }
                    else if (val$type == 2) {
                        EmojiView.this.gifSearchAdapter.search(SearchField.this.searchEditText.getText().toString());
                    }
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
        }
        
        private void showShadow(final boolean b, final boolean b2) {
            if ((b && this.shadowView.getTag() == null) || (!b && this.shadowView.getTag() != null)) {
                return;
            }
            final AnimatorSet shadowAnimator = this.shadowAnimator;
            Object value = null;
            if (shadowAnimator != null) {
                shadowAnimator.cancel();
                this.shadowAnimator = null;
            }
            final View shadowView = this.shadowView;
            if (!b) {
                value = 1;
            }
            shadowView.setTag(value);
            float alpha = 1.0f;
            if (b2) {
                this.shadowAnimator = new AnimatorSet();
                final AnimatorSet shadowAnimator2 = this.shadowAnimator;
                final View shadowView2 = this.shadowView;
                final Property alpha2 = View.ALPHA;
                if (!b) {
                    alpha = 0.0f;
                }
                shadowAnimator2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)shadowView2, alpha2, new float[] { alpha }) });
                this.shadowAnimator.setDuration(200L);
                this.shadowAnimator.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
                this.shadowAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        SearchField.this.shadowAnimator = null;
                    }
                });
                this.shadowAnimator.start();
            }
            else {
                final View shadowView3 = this.shadowView;
                if (!b) {
                    alpha = 0.0f;
                }
                shadowView3.setAlpha(alpha);
            }
        }
        
        public void hideKeyboard() {
            AndroidUtilities.hideKeyboard((View)this.searchEditText);
        }
    }
    
    private class StickersGridAdapter extends SelectionAdapter
    {
        private SparseArray<Object> cache;
        private SparseArray<Object> cacheParents;
        private Context context;
        private HashMap<Object, Integer> packStartPosition;
        private SparseIntArray positionToRow;
        private SparseArray<Object> rowStartPack;
        private int stickersPerRow;
        private int totalItems;
        
        public StickersGridAdapter(final Context context) {
            this.rowStartPack = (SparseArray<Object>)new SparseArray();
            this.packStartPosition = new HashMap<Object, Integer>();
            this.cache = (SparseArray<Object>)new SparseArray();
            this.cacheParents = (SparseArray<Object>)new SparseArray();
            this.positionToRow = new SparseIntArray();
            this.context = context;
        }
        
        public Object getItem(final int n) {
            return this.cache.get(n);
        }
        
        @Override
        public int getItemCount() {
            int totalItems = this.totalItems;
            if (totalItems != 0) {
                ++totalItems;
            }
            else {
                totalItems = 0;
            }
            return totalItems;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 4;
            }
            final Object value = this.cache.get(n);
            if (value == null) {
                return 1;
            }
            if (value instanceof TLRPC.Document) {
                return 0;
            }
            if (value instanceof String) {
                return 3;
            }
            return 2;
        }
        
        public int getPositionForPack(final Object key) {
            final Integer n = this.packStartPosition.get(key);
            if (n == null) {
                return -1;
            }
            return n;
        }
        
        public int getTabForPosition(int n) {
            int n2 = n;
            if (n == 0) {
                n2 = 1;
            }
            if (this.stickersPerRow == 0) {
                if ((n = EmojiView.this.getMeasuredWidth()) == 0) {
                    n = AndroidUtilities.displaySize.x;
                }
                this.stickersPerRow = n / AndroidUtilities.dp(72.0f);
            }
            n = this.positionToRow.get(n2, Integer.MIN_VALUE);
            if (n == Integer.MIN_VALUE) {
                return EmojiView.this.stickerSets.size() - 1 + EmojiView.this.stickersTabOffset;
            }
            final Object value = this.rowStartPack.get(n);
            if (!(value instanceof String)) {
                return EmojiView.this.stickerSets.indexOf(value) + EmojiView.this.stickersTabOffset;
            }
            if ("recent".equals(value)) {
                return EmojiView.this.recentTabBum;
            }
            return EmojiView.this.favTabBum;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void notifyDataSetChanged() {
            int n;
            if ((n = EmojiView.this.getMeasuredWidth()) == 0) {
                n = AndroidUtilities.displaySize.x;
            }
            this.stickersPerRow = n / AndroidUtilities.dp(72.0f);
            EmojiView.this.stickersLayoutManager.setSpanCount(this.stickersPerRow);
            this.rowStartPack.clear();
            this.packStartPosition.clear();
            this.positionToRow.clear();
            this.cache.clear();
            this.totalItems = 0;
            final ArrayList access$10700 = EmojiView.this.stickerSets;
            int i = -3;
            int n2 = 0;
            while (i < access$10700.size()) {
                Label_0695: {
                    if (i == -3) {
                        this.cache.put(this.totalItems++, (Object)"search");
                        ++n2;
                    }
                    else {
                        TLRPC.TL_messages_stickerSet key = null;
                        ArrayList<TLRPC.Document> list;
                        String s;
                        if (i == -2) {
                            list = EmojiView.this.favouriteStickers;
                            this.packStartPosition.put("fav", this.totalItems);
                            s = "fav";
                        }
                        else if (i == -1) {
                            list = EmojiView.this.recentStickers;
                            this.packStartPosition.put("recent", this.totalItems);
                            s = "recent";
                        }
                        else {
                            key = access$10700.get(i);
                            list = key.documents;
                            this.packStartPosition.put(key, this.totalItems);
                            s = null;
                        }
                        if (i == EmojiView.this.groupStickerPackNum) {
                            EmojiView.this.groupStickerPackPosition = this.totalItems;
                            if (list.isEmpty()) {
                                this.rowStartPack.put(n2, (Object)key);
                                final SparseIntArray positionToRow = this.positionToRow;
                                final int totalItems = this.totalItems;
                                final int n3 = n2 + 1;
                                positionToRow.put(totalItems, n2);
                                this.rowStartPack.put(n3, (Object)key);
                                this.positionToRow.put(this.totalItems + 1, n3);
                                this.cache.put(this.totalItems++, (Object)key);
                                this.cache.put(this.totalItems++, (Object)"group");
                                n2 = n3 + 1;
                                break Label_0695;
                            }
                        }
                        if (!list.isEmpty()) {
                            final int n4 = (int)Math.ceil(list.size() / (float)this.stickersPerRow);
                            if (key != null) {
                                this.cache.put(this.totalItems, (Object)key);
                            }
                            else {
                                this.cache.put(this.totalItems, (Object)list);
                            }
                            this.positionToRow.put(this.totalItems, n2);
                            int n5;
                            for (int j = 0; j < list.size(); j = n5) {
                                n5 = j + 1;
                                final int n6 = this.totalItems + n5;
                                this.cache.put(n6, (Object)list.get(j));
                                if (key != null) {
                                    this.cacheParents.put(n6, (Object)key);
                                }
                                else {
                                    this.cacheParents.put(n6, (Object)s);
                                }
                                this.positionToRow.put(this.totalItems + n5, n2 + 1 + j / this.stickersPerRow);
                            }
                            int n7 = 0;
                            int n8;
                            while (true) {
                                n8 = n4 + 1;
                                if (n7 >= n8) {
                                    break;
                                }
                                if (key != null) {
                                    this.rowStartPack.put(n2 + n7, (Object)key);
                                }
                                else {
                                    final SparseArray<Object> rowStartPack = this.rowStartPack;
                                    String s2;
                                    if (i == -1) {
                                        s2 = "recent";
                                    }
                                    else {
                                        s2 = "fav";
                                    }
                                    rowStartPack.put(n2 + n7, (Object)s2);
                                }
                                ++n7;
                            }
                            this.totalItems += n4 * this.stickersPerRow + 1;
                            n2 += n8;
                        }
                    }
                }
                ++i;
            }
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int value) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            boolean isLast = false;
            if (itemViewType != 0) {
                final ArrayList<TLRPC.Document> list = null;
                final TLRPC.Chat chat = null;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType == 3) {
                            final StickerSetGroupInfoCell stickerSetGroupInfoCell = (StickerSetGroupInfoCell)viewHolder.itemView;
                            if (value == this.totalItems - 1) {
                                isLast = true;
                            }
                            stickerSetGroupInfoCell.setIsLast(isLast);
                        }
                    }
                    else {
                        final StickerSetNameCell stickerSetNameCell = (StickerSetNameCell)viewHolder.itemView;
                        if (value == EmojiView.this.groupStickerPackPosition) {
                            if (EmojiView.this.groupStickersHidden && EmojiView.this.groupStickerSet == null) {
                                value = 0;
                            }
                            else if (EmojiView.this.groupStickerSet != null) {
                                value = 2131165865;
                            }
                            else {
                                value = 2131165866;
                            }
                            TLRPC.Chat chat2 = chat;
                            if (EmojiView.this.info != null) {
                                chat2 = MessagesController.getInstance(EmojiView.this.currentAccount).getChat(EmojiView.this.info.id);
                            }
                            String title;
                            if (chat2 != null) {
                                title = chat2.title;
                            }
                            else {
                                title = "Group Stickers";
                            }
                            stickerSetNameCell.setText(LocaleController.formatString("CurrentGroupStickers", 2131559180, title), value);
                        }
                        else {
                            final Object value2 = this.cache.get(value);
                            if (value2 instanceof TLRPC.TL_messages_stickerSet) {
                                final TLRPC.StickerSet set = ((TLRPC.TL_messages_stickerSet)value2).set;
                                if (set != null) {
                                    stickerSetNameCell.setText(set.title, 0);
                                }
                            }
                            else if (value2 == EmojiView.this.recentStickers) {
                                stickerSetNameCell.setText(LocaleController.getString("RecentStickers", 2131560538), 0);
                            }
                            else if (value2 == EmojiView.this.favouriteStickers) {
                                stickerSetNameCell.setText(LocaleController.getString("FavoriteStickers", 2131559478), 0);
                            }
                        }
                    }
                }
                else {
                    final EmptyCell emptyCell = (EmptyCell)viewHolder.itemView;
                    if (value == this.totalItems) {
                        value = this.positionToRow.get(value - 1, Integer.MIN_VALUE);
                        if (value == Integer.MIN_VALUE) {
                            emptyCell.setHeight(1);
                        }
                        else {
                            final Object value3 = this.rowStartPack.get(value);
                            ArrayList<TLRPC.Document> list2;
                            if (value3 instanceof TLRPC.TL_messages_stickerSet) {
                                list2 = ((TLRPC.TL_messages_stickerSet)value3).documents;
                            }
                            else {
                                list2 = list;
                                if (value3 instanceof String) {
                                    if ("recent".equals(value3)) {
                                        list2 = EmojiView.this.recentStickers;
                                    }
                                    else {
                                        list2 = EmojiView.this.favouriteStickers;
                                    }
                                }
                            }
                            if (list2 == null) {
                                emptyCell.setHeight(1);
                            }
                            else if (list2.isEmpty()) {
                                emptyCell.setHeight(AndroidUtilities.dp(8.0f));
                            }
                            else {
                                value = EmojiView.this.pager.getHeight() - (int)Math.ceil(list2.size() / (float)this.stickersPerRow) * AndroidUtilities.dp(82.0f);
                                if (value <= 0) {
                                    value = 1;
                                }
                                emptyCell.setHeight(value);
                            }
                        }
                    }
                    else {
                        emptyCell.setHeight(AndroidUtilities.dp(82.0f));
                    }
                }
            }
            else {
                final TLRPC.Document document = (TLRPC.Document)this.cache.get(value);
                final StickerEmojiCell stickerEmojiCell = (StickerEmojiCell)viewHolder.itemView;
                stickerEmojiCell.setSticker(document, this.cacheParents.get(value), false);
                boolean recent = false;
                Label_0605: {
                    if (!EmojiView.this.recentStickers.contains(document)) {
                        recent = b;
                        if (!EmojiView.this.favouriteStickers.contains(document)) {
                            break Label_0605;
                        }
                    }
                    recent = true;
                }
                stickerEmojiCell.setRecent(recent);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                o = null;
                            }
                            else {
                                o = new View(this.context);
                                ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
                            }
                        }
                        else {
                            o = new StickerSetGroupInfoCell(this.context);
                            ((StickerSetGroupInfoCell)o).setAddOnClickListener((View$OnClickListener)new _$$Lambda$EmojiView$StickersGridAdapter$rEbSNlpXftcal36it1dYPmUfJQ8(this));
                            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
                        }
                    }
                    else {
                        o = new StickerSetNameCell(this.context, false);
                        ((StickerSetNameCell)o).setOnIconClickListener((View$OnClickListener)new _$$Lambda$EmojiView$StickersGridAdapter$LJAPCN9WKc70C6I4z_4Z05VncJg(this));
                    }
                }
                else {
                    o = new EmptyCell(this.context);
                }
            }
            else {
                o = new StickerEmojiCell(this.context) {
                    public void onMeasure(final int n, final int n2) {
                        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                    }
                };
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class StickersSearchGridAdapter extends SelectionAdapter
    {
        private SparseArray<Object> cache;
        private SparseArray<Object> cacheParent;
        boolean cleared;
        private Context context;
        private ArrayList<ArrayList<TLRPC.Document>> emojiArrays;
        private int emojiSearchId;
        private HashMap<ArrayList<TLRPC.Document>, String> emojiStickers;
        private ArrayList<TLRPC.TL_messages_stickerSet> localPacks;
        private HashMap<TLRPC.TL_messages_stickerSet, Integer> localPacksByName;
        private HashMap<TLRPC.TL_messages_stickerSet, Boolean> localPacksByShortName;
        private SparseArray<String> positionToEmoji;
        private SparseIntArray positionToRow;
        private SparseArray<TLRPC.StickerSetCovered> positionsToSets;
        private int reqId;
        private int reqId2;
        private SparseArray<Object> rowStartPack;
        private String searchQuery;
        private Runnable searchRunnable;
        private ArrayList<TLRPC.StickerSetCovered> serverPacks;
        final /* synthetic */ EmojiView this$0;
        private int totalItems;
        
        public StickersSearchGridAdapter(final Context context) {
            this.rowStartPack = (SparseArray<Object>)new SparseArray();
            this.cache = (SparseArray<Object>)new SparseArray();
            this.cacheParent = (SparseArray<Object>)new SparseArray();
            this.positionToRow = new SparseIntArray();
            this.positionToEmoji = (SparseArray<String>)new SparseArray();
            this.serverPacks = new ArrayList<TLRPC.StickerSetCovered>();
            this.localPacks = new ArrayList<TLRPC.TL_messages_stickerSet>();
            this.localPacksByShortName = new HashMap<TLRPC.TL_messages_stickerSet, Boolean>();
            this.localPacksByName = new HashMap<TLRPC.TL_messages_stickerSet, Integer>();
            this.emojiStickers = new HashMap<ArrayList<TLRPC.Document>, String>();
            this.emojiArrays = new ArrayList<ArrayList<TLRPC.Document>>();
            this.positionsToSets = (SparseArray<TLRPC.StickerSetCovered>)new SparseArray();
            this.searchRunnable = new Runnable() {
                private void clear() {
                    final StickersSearchGridAdapter this$1 = StickersSearchGridAdapter.this;
                    if (this$1.cleared) {
                        return;
                    }
                    this$1.cleared = true;
                    this$1.emojiStickers.clear();
                    StickersSearchGridAdapter.this.emojiArrays.clear();
                    StickersSearchGridAdapter.this.localPacks.clear();
                    StickersSearchGridAdapter.this.serverPacks.clear();
                    StickersSearchGridAdapter.this.localPacksByShortName.clear();
                    StickersSearchGridAdapter.this.localPacksByName.clear();
                }
                
                @Override
                public void run() {
                    if (TextUtils.isEmpty((CharSequence)StickersSearchGridAdapter.this.searchQuery)) {
                        return;
                    }
                    EmojiView.this.stickersSearchField.progressDrawable.startAnimation();
                    final StickersSearchGridAdapter this$1 = StickersSearchGridAdapter.this;
                    this$1.cleared = false;
                    final int access$13304 = ++this$1.emojiSearchId;
                    final ArrayList list = new ArrayList(0);
                    final LongSparseArray longSparseArray = new LongSparseArray(0);
                    final HashMap<String, ArrayList<TLRPC.Document>> allStickers = DataQuery.getInstance(EmojiView.this.currentAccount).getAllStickers();
                    if (StickersSearchGridAdapter.this.searchQuery.length() <= 14) {
                        String access$13305 = StickersSearchGridAdapter.this.searchQuery;
                        int n3;
                        int n4;
                        CharSequence charSequence2;
                        for (int length = access$13305.length(), i = 0; i < length; i = n3 + 1, length = n4, access$13305 = (String)charSequence2) {
                            CharSequence charSequence = null;
                            Label_0336: {
                                Label_0270: {
                                    if (i < length - 1) {
                                        Label_0225: {
                                            if (access$13305.charAt(i) == '\ud83c') {
                                                final int n = i + 1;
                                                if (access$13305.charAt(n) >= '\udffb' && access$13305.charAt(n) <= '\udfff') {
                                                    break Label_0225;
                                                }
                                            }
                                            if (access$13305.charAt(i) != '\u200d') {
                                                break Label_0270;
                                            }
                                            final int n2 = i + 1;
                                            if (access$13305.charAt(n2) != '\u2640' && access$13305.charAt(n2) != '\u2642') {
                                                break Label_0270;
                                            }
                                        }
                                        charSequence = TextUtils.concat(new CharSequence[] { access$13305.subSequence(0, i), access$13305.subSequence(i + 2, access$13305.length()) });
                                        length -= 2;
                                        break Label_0336;
                                    }
                                }
                                n3 = i;
                                n4 = length;
                                charSequence2 = access$13305;
                                if (access$13305.charAt(i) != '\ufe0f') {
                                    continue;
                                }
                                charSequence = TextUtils.concat(new CharSequence[] { access$13305.subSequence(0, i), access$13305.subSequence(i + 1, access$13305.length()) });
                                --length;
                            }
                            n3 = i - 1;
                            charSequence2 = charSequence;
                            n4 = length;
                        }
                        ArrayList<TLRPC.Document> c;
                        if (allStickers != null) {
                            c = allStickers.get(access$13305.toString());
                        }
                        else {
                            c = null;
                        }
                        if (c != null && !c.isEmpty()) {
                            this.clear();
                            list.addAll(c);
                            for (int size = c.size(), j = 0; j < size; ++j) {
                                final TLRPC.Document document = c.get(j);
                                longSparseArray.put(document.id, (Object)document);
                            }
                            StickersSearchGridAdapter.this.emojiStickers.put(list, StickersSearchGridAdapter.this.searchQuery);
                            StickersSearchGridAdapter.this.emojiArrays.add(list);
                        }
                    }
                    if (allStickers != null && !allStickers.isEmpty() && StickersSearchGridAdapter.this.searchQuery.length() > 1) {
                        final String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                        if (!Arrays.equals(EmojiView.this.lastSearchKeyboardLanguage, currentKeyboardLanguage)) {
                            DataQuery.getInstance(EmojiView.this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
                        }
                        EmojiView.this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
                        DataQuery.getInstance(EmojiView.this.currentAccount).getEmojiSuggestions(EmojiView.this.lastSearchKeyboardLanguage, StickersSearchGridAdapter.this.searchQuery, false, (DataQuery.KeywordResultCallback)new DataQuery.KeywordResultCallback() {
                            @Override
                            public void run(final ArrayList<KeywordResult> list, final String s) {
                                if (access$13304 != StickersSearchGridAdapter.this.emojiSearchId) {
                                    return;
                                }
                                final int size = list.size();
                                int i = 0;
                                int n = 0;
                                while (i < size) {
                                    final String emoji = list.get(i).emoji;
                                    final HashMap val$allStickers = allStickers;
                                    ArrayList e;
                                    if (val$allStickers != null) {
                                        e = val$allStickers.get(emoji);
                                    }
                                    else {
                                        e = null;
                                    }
                                    int n2 = n;
                                    if (e != null) {
                                        n2 = n;
                                        if (!e.isEmpty()) {
                                            EmojiView$StickersSearchGridAdapter$1.this.clear();
                                            n2 = n;
                                            if (!StickersSearchGridAdapter.this.emojiStickers.containsKey(e)) {
                                                StickersSearchGridAdapter.this.emojiStickers.put(e, emoji);
                                                StickersSearchGridAdapter.this.emojiArrays.add(e);
                                                n2 = 1;
                                            }
                                        }
                                    }
                                    ++i;
                                    n = n2;
                                }
                                if (n != 0) {
                                    StickersSearchGridAdapter.this.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSets(0);
                    for (int size2 = stickerSets.size(), k = 0; k < size2; ++k) {
                        final TLRPC.TL_messages_stickerSet set = stickerSets.get(k);
                        final int index = set.set.title.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                        if (index >= 0) {
                            if (index == 0 || set.set.title.charAt(index - 1) == ' ') {
                                this.clear();
                                StickersSearchGridAdapter.this.localPacks.add(set);
                                StickersSearchGridAdapter.this.localPacksByName.put(set, index);
                            }
                        }
                        else {
                            final String short_name = set.set.short_name;
                            if (short_name != null) {
                                final int index2 = short_name.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                                if (index2 >= 0 && (index2 == 0 || set.set.short_name.charAt(index2 - 1) == ' ')) {
                                    this.clear();
                                    StickersSearchGridAdapter.this.localPacks.add(set);
                                    StickersSearchGridAdapter.this.localPacksByShortName.put(set, true);
                                }
                            }
                        }
                    }
                    final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets2 = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSets(3);
                    for (int size3 = stickerSets2.size(), l = 0; l < size3; ++l) {
                        final TLRPC.TL_messages_stickerSet set2 = stickerSets2.get(l);
                        final int index3 = set2.set.title.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                        if (index3 >= 0) {
                            if (index3 == 0 || set2.set.title.charAt(index3 - 1) == ' ') {
                                this.clear();
                                StickersSearchGridAdapter.this.localPacks.add(set2);
                                StickersSearchGridAdapter.this.localPacksByName.put(set2, index3);
                            }
                        }
                        else {
                            final String short_name2 = set2.set.short_name;
                            if (short_name2 != null) {
                                final int index4 = short_name2.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                                if (index4 >= 0 && (index4 == 0 || set2.set.short_name.charAt(index4 - 1) == ' ')) {
                                    this.clear();
                                    StickersSearchGridAdapter.this.localPacks.add(set2);
                                    StickersSearchGridAdapter.this.localPacksByShortName.put(set2, true);
                                }
                            }
                        }
                    }
                    if ((!StickersSearchGridAdapter.this.localPacks.isEmpty() || !StickersSearchGridAdapter.this.emojiStickers.isEmpty()) && EmojiView.this.stickersGridView.getAdapter() != EmojiView.this.stickersSearchGridAdapter) {
                        EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersSearchGridAdapter);
                    }
                    final TLRPC.TL_messages_searchStickerSets tl_messages_searchStickerSets = new TLRPC.TL_messages_searchStickerSets();
                    tl_messages_searchStickerSets.q = StickersSearchGridAdapter.this.searchQuery;
                    final StickersSearchGridAdapter this$2 = StickersSearchGridAdapter.this;
                    this$2.reqId = ConnectionsManager.getInstance(this$2.this$0.currentAccount).sendRequest(tl_messages_searchStickerSets, new _$$Lambda$EmojiView$StickersSearchGridAdapter$1$PMXaBA9vcwjG4TQSEF8AuHh_dEU(this, tl_messages_searchStickerSets));
                    if (Emoji.isValidEmoji(StickersSearchGridAdapter.this.searchQuery)) {
                        final TLRPC.TL_messages_getStickers tl_messages_getStickers = new TLRPC.TL_messages_getStickers();
                        tl_messages_getStickers.emoticon = StickersSearchGridAdapter.this.searchQuery;
                        tl_messages_getStickers.hash = 0;
                        final StickersSearchGridAdapter this$3 = StickersSearchGridAdapter.this;
                        this$3.reqId2 = ConnectionsManager.getInstance(this$3.this$0.currentAccount).sendRequest(tl_messages_getStickers, new _$$Lambda$EmojiView$StickersSearchGridAdapter$1$D2YaPWN88kYmZJFvtUqCfq785Bw(this, tl_messages_getStickers, list, longSparseArray));
                    }
                    StickersSearchGridAdapter.this.notifyDataSetChanged();
                }
            };
            this.context = context;
        }
        
        public Object getItem(final int n) {
            return this.cache.get(n);
        }
        
        @Override
        public int getItemCount() {
            final int totalItems = this.totalItems;
            if (totalItems != 1) {
                return totalItems + 1;
            }
            return 2;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 4;
            }
            if (n == 1 && this.totalItems == 1) {
                return 5;
            }
            final Object value = this.cache.get(n);
            if (value == null) {
                return 1;
            }
            if (value instanceof TLRPC.Document) {
                return 0;
            }
            if (value instanceof TLRPC.StickerSetCovered) {
                return 3;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void notifyDataSetChanged() {
            this.rowStartPack.clear();
            this.positionToRow.clear();
            this.cache.clear();
            this.positionsToSets.clear();
            this.positionToEmoji.clear();
            this.totalItems = 0;
            int size = this.serverPacks.size();
            final int size2 = this.localPacks.size();
            final int n = (this.emojiArrays.isEmpty() ^ true) ? 1 : 0;
            int i = -1;
            int n2 = 0;
            while (i < size + size2 + n) {
                int n3 = 0;
                Label_0822: {
                    if (i == -1) {
                        this.cache.put(this.totalItems++, (Object)"search");
                        n3 = n2 + 1;
                    }
                    else {
                        TLObject tlObject;
                        ArrayList<TLRPC.Document> list;
                        if (i < size2) {
                            tlObject = this.localPacks.get(i);
                            list = ((TLRPC.TL_messages_stickerSet)tlObject).documents;
                        }
                        else {
                            final int n4 = i - size2;
                            if (n4 < n) {
                                final int size3 = this.emojiArrays.size();
                                String s = "";
                                int j = 0;
                                int k = 0;
                                final int n5 = size;
                                while (j < size3) {
                                    final ArrayList<TLRPC.Document> key = this.emojiArrays.get(j);
                                    final String anObject = this.emojiStickers.get(key);
                                    String s2 = s;
                                    if (anObject != null) {
                                        s2 = s;
                                        if (!s.equals(anObject)) {
                                            this.positionToEmoji.put(this.totalItems + k, (Object)anObject);
                                            s2 = anObject;
                                        }
                                    }
                                    final int size4 = key.size();
                                    int n6 = k;
                                    int l = 0;
                                    final ArrayList<TLRPC.Document> list2 = key;
                                    while (l < size4) {
                                        final int n7 = this.totalItems + n6;
                                        final int n8 = n6 / EmojiView.this.stickersGridAdapter.stickersPerRow;
                                        final TLRPC.Document document = list2.get(l);
                                        this.cache.put(n7, (Object)document);
                                        final TLRPC.TL_messages_stickerSet stickerSetById = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSetById(DataQuery.getStickerSetId(document));
                                        if (stickerSetById != null) {
                                            this.cacheParent.put(n7, (Object)stickerSetById);
                                        }
                                        this.positionToRow.put(n7, n8 + n2);
                                        ++n6;
                                        ++l;
                                    }
                                    ++j;
                                    k = n6;
                                    s = s2;
                                }
                                size = n5;
                                final int n9 = (int)Math.ceil(k / (float)EmojiView.this.stickersGridAdapter.stickersPerRow);
                                for (int n10 = 0; n10 < n9; ++n10) {
                                    this.rowStartPack.put(n2 + n10, (Object)k);
                                }
                                this.totalItems += EmojiView.this.stickersGridAdapter.stickersPerRow * n9;
                                n3 = n2 + n9;
                                break Label_0822;
                            }
                            tlObject = this.serverPacks.get(n4 - n);
                            list = ((TLRPC.StickerSetCovered)tlObject).covers;
                        }
                        if (list.isEmpty()) {
                            n3 = n2;
                        }
                        else {
                            final int n11 = (int)Math.ceil(list.size() / (float)EmojiView.this.stickersGridAdapter.stickersPerRow);
                            this.cache.put(this.totalItems, (Object)tlObject);
                            if (i >= size2 && tlObject instanceof TLRPC.StickerSetCovered) {
                                this.positionsToSets.put(this.totalItems, (Object)tlObject);
                            }
                            this.positionToRow.put(this.totalItems, n2);
                            int n12;
                            for (int size5 = list.size(), index = 0; index < size5; index = n12) {
                                n12 = index + 1;
                                final int n13 = this.totalItems + n12;
                                final int n14 = index / EmojiView.this.stickersGridAdapter.stickersPerRow;
                                this.cache.put(n13, (Object)list.get(index));
                                if (tlObject != null) {
                                    this.cacheParent.put(n13, (Object)tlObject);
                                }
                                this.positionToRow.put(n13, n2 + 1 + n14);
                                if (i >= size2 && tlObject instanceof TLRPC.StickerSetCovered) {
                                    this.positionsToSets.put(n13, (Object)tlObject);
                                }
                            }
                            final int n15 = n11 + 1;
                            for (int n16 = 0; n16 < n15; ++n16) {
                                this.rowStartPack.put(n2 + n16, (Object)tlObject);
                            }
                            this.totalItems += n11 * EmojiView.this.stickersGridAdapter.stickersPerRow + 1;
                            n3 = n2 + n15;
                        }
                    }
                }
                ++i;
                n2 = n3;
            }
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int height) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = true;
            final boolean b2 = true;
            if (itemViewType != 0) {
                final Integer n = null;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType == 3) {
                            final TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered)this.cache.get(height);
                            final FeaturedStickerSetInfoCell featuredStickerSetInfoCell = (FeaturedStickerSetInfoCell)viewHolder.itemView;
                            if (EmojiView.this.installingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0) {
                                height = 1;
                            }
                            else {
                                height = 0;
                            }
                            final boolean b3 = EmojiView.this.removingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0;
                            int n2 = 0;
                            boolean b4 = false;
                            Label_0212: {
                                if (height == 0) {
                                    n2 = height;
                                    if (!(b4 = b3)) {
                                        break Label_0212;
                                    }
                                }
                                if (height != 0 && featuredStickerSetInfoCell.isInstalled()) {
                                    EmojiView.this.installingStickerSets.remove(stickerSetCovered.set.id);
                                    n2 = 0;
                                    b4 = b3;
                                }
                                else {
                                    n2 = height;
                                    if (b4 = b3) {
                                        n2 = height;
                                        b4 = b3;
                                        if (!featuredStickerSetInfoCell.isInstalled()) {
                                            EmojiView.this.removingStickerSets.remove(stickerSetCovered.set.id);
                                            b4 = false;
                                            n2 = height;
                                        }
                                    }
                                }
                            }
                            boolean drawProgress = b2;
                            if (n2 == 0) {
                                drawProgress = (b4 && b2);
                            }
                            featuredStickerSetInfoCell.setDrawProgress(drawProgress);
                            if (TextUtils.isEmpty((CharSequence)this.searchQuery)) {
                                height = -1;
                            }
                            else {
                                height = stickerSetCovered.set.title.toLowerCase().indexOf(this.searchQuery);
                            }
                            if (height >= 0) {
                                featuredStickerSetInfoCell.setStickerSet(stickerSetCovered, false, height, this.searchQuery.length());
                            }
                            else {
                                featuredStickerSetInfoCell.setStickerSet(stickerSetCovered, false);
                                if (!TextUtils.isEmpty((CharSequence)this.searchQuery) && stickerSetCovered.set.short_name.toLowerCase().startsWith(this.searchQuery)) {
                                    featuredStickerSetInfoCell.setUrl(stickerSetCovered.set.short_name, this.searchQuery.length());
                                }
                            }
                        }
                    }
                    else {
                        final StickerSetNameCell stickerSetNameCell = (StickerSetNameCell)viewHolder.itemView;
                        final Object value = this.cache.get(height);
                        if (value instanceof TLRPC.TL_messages_stickerSet) {
                            final TLRPC.TL_messages_stickerSet set = (TLRPC.TL_messages_stickerSet)value;
                            if (!TextUtils.isEmpty((CharSequence)this.searchQuery) && this.localPacksByShortName.containsKey(set)) {
                                final TLRPC.StickerSet set2 = set.set;
                                if (set2 != null) {
                                    stickerSetNameCell.setText(set2.title, 0);
                                }
                                stickerSetNameCell.setUrl(set.set.short_name, this.searchQuery.length());
                            }
                            else {
                                final Integer n3 = this.localPacksByName.get(set);
                                final TLRPC.StickerSet set3 = set.set;
                                if (set3 != null && n3 != null) {
                                    final String title = set3.title;
                                    final int intValue = n3;
                                    if (!TextUtils.isEmpty((CharSequence)this.searchQuery)) {
                                        height = this.searchQuery.length();
                                    }
                                    else {
                                        height = 0;
                                    }
                                    stickerSetNameCell.setText(title, 0, intValue, height);
                                }
                                stickerSetNameCell.setUrl(null, 0);
                            }
                        }
                    }
                }
                else {
                    final EmptyCell emptyCell = (EmptyCell)viewHolder.itemView;
                    if (height == this.totalItems) {
                        height = this.positionToRow.get(height - 1, Integer.MIN_VALUE);
                        if (height == Integer.MIN_VALUE) {
                            emptyCell.setHeight(1);
                        }
                        else {
                            final Object value2 = this.rowStartPack.get(height);
                            Integer value3;
                            if (value2 instanceof TLRPC.TL_messages_stickerSet) {
                                value3 = ((TLRPC.TL_messages_stickerSet)value2).documents.size();
                            }
                            else {
                                value3 = n;
                                if (value2 instanceof Integer) {
                                    value3 = (Integer)value2;
                                }
                            }
                            if (value3 == null) {
                                emptyCell.setHeight(1);
                            }
                            else if (value3 == 0) {
                                emptyCell.setHeight(AndroidUtilities.dp(8.0f));
                            }
                            else {
                                height = EmojiView.this.pager.getHeight() - (int)Math.ceil(value3 / (float)EmojiView.this.stickersGridAdapter.stickersPerRow) * AndroidUtilities.dp(82.0f);
                                if (height <= 0) {
                                    height = 1;
                                }
                                emptyCell.setHeight(height);
                            }
                        }
                    }
                    else {
                        emptyCell.setHeight(AndroidUtilities.dp(82.0f));
                    }
                }
            }
            else {
                final TLRPC.Document document = (TLRPC.Document)this.cache.get(height);
                final StickerEmojiCell stickerEmojiCell = (StickerEmojiCell)viewHolder.itemView;
                stickerEmojiCell.setSticker(document, this.cacheParent.get(height), (String)this.positionToEmoji.get(height), false);
                boolean recent = b;
                if (!EmojiView.this.recentStickers.contains(document)) {
                    recent = (EmojiView.this.favouriteStickers.contains(document) && b);
                }
                stickerEmojiCell.setRecent(recent);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                if (n != 5) {
                                    o = null;
                                }
                                else {
                                    o = new FrameLayout(this.context) {
                                        protected void onMeasure(final int n, final int n2) {
                                            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec((int)((EmojiView.this.stickersGridView.getMeasuredHeight() - EmojiView.this.searchFieldHeight - AndroidUtilities.dp(8.0f)) / 3 * 1.7f), 1073741824));
                                        }
                                    };
                                    final ImageView imageView = new ImageView(this.context);
                                    imageView.setScaleType(ImageView$ScaleType.CENTER);
                                    imageView.setImageResource(2131165864);
                                    imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelEmptyText"), PorterDuff$Mode.MULTIPLY));
                                    ((FrameLayout)o).addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 59.0f));
                                    final TextView textView = new TextView(this.context);
                                    textView.setText((CharSequence)LocaleController.getString("NoStickersFound", 2131559954));
                                    textView.setTextSize(1, 16.0f);
                                    textView.setTextColor(Theme.getColor("chat_emojiPanelEmptyText"));
                                    ((FrameLayout)o).addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 9.0f));
                                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
                                }
                            }
                            else {
                                o = new View(this.context);
                                ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
                            }
                        }
                        else {
                            o = new FeaturedStickerSetInfoCell(this.context, 17);
                            ((FeaturedStickerSetInfoCell)o).setAddOnClickListener((View$OnClickListener)new _$$Lambda$EmojiView$StickersSearchGridAdapter$An1o7aFGx9Hb6YuxBcvVH4f8K_M(this));
                        }
                    }
                    else {
                        o = new StickerSetNameCell(this.context, false);
                    }
                }
                else {
                    o = new EmptyCell(this.context);
                }
            }
            else {
                o = new StickerEmojiCell(this.context) {
                    public void onMeasure(final int n, final int n2) {
                        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                    }
                };
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        public void search(final String s) {
            if (this.reqId != 0) {
                ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            if (this.reqId2 != 0) {
                ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId2, true);
                this.reqId2 = 0;
            }
            if (TextUtils.isEmpty((CharSequence)s)) {
                this.searchQuery = null;
                this.localPacks.clear();
                this.emojiStickers.clear();
                this.serverPacks.clear();
                if (EmojiView.this.stickersGridView.getAdapter() != EmojiView.this.stickersGridAdapter) {
                    EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersGridAdapter);
                }
                this.notifyDataSetChanged();
            }
            else {
                this.searchQuery = s.toLowerCase();
            }
            AndroidUtilities.cancelRunOnUIThread(this.searchRunnable);
            AndroidUtilities.runOnUIThread(this.searchRunnable, 300L);
        }
    }
    
    private class TrendingGridAdapter extends SelectionAdapter
    {
        private SparseArray<Object> cache;
        private Context context;
        private SparseArray<TLRPC.StickerSetCovered> positionsToSets;
        private ArrayList<TLRPC.StickerSetCovered> sets;
        private int stickersPerRow;
        private int totalItems;
        
        public TrendingGridAdapter(final Context context) {
            this.cache = (SparseArray<Object>)new SparseArray();
            this.sets = new ArrayList<TLRPC.StickerSetCovered>();
            this.positionsToSets = (SparseArray<TLRPC.StickerSetCovered>)new SparseArray();
            this.context = context;
        }
        
        public Object getItem(final int n) {
            return this.cache.get(n);
        }
        
        @Override
        public int getItemCount() {
            return this.totalItems;
        }
        
        @Override
        public int getItemViewType(final int n) {
            final Object value = this.cache.get(n);
            if (value == null) {
                return 1;
            }
            if (value instanceof TLRPC.Document) {
                return 0;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void notifyDataSetChanged() {
            int measuredWidth;
            if ((measuredWidth = EmojiView.this.getMeasuredWidth()) == 0) {
                int x2;
                if (AndroidUtilities.isTablet()) {
                    final int x = AndroidUtilities.displaySize.x;
                    int dp;
                    if ((dp = x * 35 / 100) < AndroidUtilities.dp(320.0f)) {
                        dp = AndroidUtilities.dp(320.0f);
                    }
                    x2 = x - dp;
                }
                else {
                    x2 = AndroidUtilities.displaySize.x;
                }
                measuredWidth = x2;
                if (x2 == 0) {
                    measuredWidth = 1080;
                }
            }
            this.stickersPerRow = Math.max(1, measuredWidth / AndroidUtilities.dp(72.0f));
            EmojiView.this.trendingLayoutManager.setSpanCount(this.stickersPerRow);
            if (EmojiView.this.trendingLoaded) {
                return;
            }
            this.cache.clear();
            this.positionsToSets.clear();
            this.sets.clear();
            this.totalItems = 0;
            final ArrayList<TLRPC.StickerSetCovered> featuredStickerSets = DataQuery.getInstance(EmojiView.this.currentAccount).getFeaturedStickerSets();
            int i = 0;
            int j = 0;
            while (i < featuredStickerSets.size()) {
                final TLRPC.StickerSetCovered e = featuredStickerSets.get(i);
                int n = j;
                if (!DataQuery.getInstance(EmojiView.this.currentAccount).isStickerPackInstalled(e.set.id)) {
                    if (e.covers.isEmpty() && e.cover == null) {
                        n = j;
                    }
                    else {
                        this.sets.add(e);
                        this.positionsToSets.put(this.totalItems, (Object)e);
                        this.cache.put(this.totalItems++, (Object)j);
                        final int n2 = this.totalItems / this.stickersPerRow;
                        int n4;
                        if (!e.covers.isEmpty()) {
                            final int n3 = (int)Math.ceil(e.covers.size() / (float)this.stickersPerRow);
                            int index = 0;
                            while (true) {
                                n4 = n3;
                                if (index >= e.covers.size()) {
                                    break;
                                }
                                this.cache.put(this.totalItems + index, (Object)e.covers.get(index));
                                ++index;
                            }
                        }
                        else {
                            this.cache.put(this.totalItems, (Object)e.cover);
                            n4 = 1;
                        }
                        int n5 = 0;
                        int stickersPerRow;
                        while (true) {
                            stickersPerRow = this.stickersPerRow;
                            if (n5 >= n4 * stickersPerRow) {
                                break;
                            }
                            this.positionsToSets.put(this.totalItems + n5, (Object)e);
                            ++n5;
                        }
                        this.totalItems += n4 * stickersPerRow;
                        n = j + 1;
                    }
                }
                ++i;
                j = n;
            }
            if (this.totalItems != 0) {
                EmojiView.this.trendingLoaded = true;
                final EmojiView this$0 = EmojiView.this;
                this$0.featuredStickersHash = DataQuery.getInstance(this$0.currentAccount).getFeaturesStickersHashWithoutUnread();
            }
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType == 2) {
                        final ArrayList<Long> unreadStickerSets = DataQuery.getInstance(EmojiView.this.currentAccount).getUnreadStickerSets();
                        final TLRPC.StickerSetCovered stickerSetCovered = this.sets.get((int)this.cache.get(n));
                        final boolean b2 = unreadStickerSets != null && unreadStickerSets.contains(stickerSetCovered.set.id);
                        final FeaturedStickerSetInfoCell featuredStickerSetInfoCell = (FeaturedStickerSetInfoCell)viewHolder.itemView;
                        featuredStickerSetInfoCell.setStickerSet(stickerSetCovered, b2);
                        if (b2) {
                            DataQuery.getInstance(EmojiView.this.currentAccount).markFaturedStickersByIdAsRead(stickerSetCovered.set.id);
                        }
                        if (EmojiView.this.installingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0) {
                            n = 1;
                        }
                        else {
                            n = 0;
                        }
                        final boolean b3 = EmojiView.this.removingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0;
                        int n2 = 0;
                        boolean b4 = false;
                        Label_0296: {
                            if (n == 0) {
                                n2 = n;
                                if (!(b4 = b3)) {
                                    break Label_0296;
                                }
                            }
                            if (n != 0 && featuredStickerSetInfoCell.isInstalled()) {
                                EmojiView.this.installingStickerSets.remove(stickerSetCovered.set.id);
                                n2 = 0;
                                b4 = b3;
                            }
                            else {
                                n2 = n;
                                if (b4 = b3) {
                                    n2 = n;
                                    b4 = b3;
                                    if (!featuredStickerSetInfoCell.isInstalled()) {
                                        EmojiView.this.removingStickerSets.remove(stickerSetCovered.set.id);
                                        b4 = false;
                                        n2 = n;
                                    }
                                }
                            }
                        }
                        boolean drawProgress = false;
                        Label_0313: {
                            if (n2 == 0) {
                                drawProgress = b;
                                if (!b4) {
                                    break Label_0313;
                                }
                            }
                            drawProgress = true;
                        }
                        featuredStickerSetInfoCell.setDrawProgress(drawProgress);
                    }
                }
                else {
                    ((EmptyCell)viewHolder.itemView).setHeight(AndroidUtilities.dp(82.0f));
                }
            }
            else {
                ((StickerEmojiCell)viewHolder.itemView).setSticker((TLRPC.Document)this.cache.get(n), this.positionsToSets.get(n), false);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        o = null;
                    }
                    else {
                        o = new FeaturedStickerSetInfoCell(this.context, 17);
                        ((FeaturedStickerSetInfoCell)o).setAddOnClickListener((View$OnClickListener)new _$$Lambda$EmojiView$TrendingGridAdapter$KqfE7v9vPbOMyNkHB4d4a59Ij7c(this));
                    }
                }
                else {
                    o = new EmptyCell(this.context);
                }
            }
            else {
                o = new StickerEmojiCell(this.context) {
                    public void onMeasure(final int n, final int n2) {
                        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                    }
                };
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
}
