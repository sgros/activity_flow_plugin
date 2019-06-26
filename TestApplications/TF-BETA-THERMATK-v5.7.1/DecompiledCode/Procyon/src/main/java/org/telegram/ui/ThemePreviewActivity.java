// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.ui.Cells.BaseCell;
import org.telegram.ui.Cells.ChatActionCell$ChatActionCellDelegate$_CC;
import android.text.style.CharacterStyle;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.Cells.LoadingCell;
import java.util.ArrayList;
import org.telegram.ui.Cells.DialogCell;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.graphics.Paint;
import android.database.DataSetObserver;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ActionBar.BackDrawable;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.os.Build$VERSION;
import org.telegram.messenger.AndroidUtilities;
import android.widget.ImageView$ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.messenger.LocaleController;
import android.widget.EditText;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.content.Context;
import org.telegram.ui.ActionBar.ActionBarLayout;
import java.io.File;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import android.widget.FrameLayout;
import org.telegram.ui.Components.RecyclerListView;
import android.widget.ImageView;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ThemePreviewActivity extends BaseFragment implements NotificationCenterDelegate
{
    private ActionBar actionBar2;
    private boolean applied;
    private Theme.ThemeInfo applyingTheme;
    private DialogsAdapter dialogsAdapter;
    private View dotsContainer;
    private ImageView floatingButton;
    private RecyclerListView listView;
    private RecyclerListView listView2;
    private MessagesAdapter messagesAdapter;
    private FrameLayout page1;
    private SizeNotifierFrameLayout page2;
    private File themeFile;
    
    public ThemePreviewActivity(final File themeFile, final Theme.ThemeInfo applyingTheme) {
        super.swipeBackEnabled = false;
        this.applyingTheme = applyingTheme;
        this.themeFile = themeFile;
    }
    
    @Override
    public View createView(final Context context) {
        this.page1 = new FrameLayout(context);
        super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public boolean canCollapseSearch() {
                return true;
            }
            
            @Override
            public void onSearchCollapse() {
            }
            
            @Override
            public void onSearchExpand() {
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
            }
        }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        super.actionBar.setBackButtonDrawable(new MenuDrawable());
        super.actionBar.setAddToContainer(false);
        super.actionBar.setTitle(LocaleController.getString("ThemePreview", 2131560896));
        (this.page1 = new FrameLayout(context) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view == ThemePreviewActivity.this.actionBar && ThemePreviewActivity.this.parentLayout != null) {
                    final ActionBarLayout access$900 = ThemePreviewActivity.this.parentLayout;
                    int measuredHeight;
                    if (ThemePreviewActivity.this.actionBar.getVisibility() == 0) {
                        measuredHeight = ThemePreviewActivity.this.actionBar.getMeasuredHeight();
                    }
                    else {
                        measuredHeight = 0;
                    }
                    access$900.drawHeaderShadow(canvas, measuredHeight);
                }
                return drawChild;
            }
            
            protected void onMeasure(final int n, final int n2) {
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(n2);
                this.setMeasuredDimension(size, size2);
                this.measureChildWithMargins((View)ThemePreviewActivity.this.actionBar, n, 0, n2, 0);
                final int measuredHeight = ThemePreviewActivity.this.actionBar.getMeasuredHeight();
                int n3 = size2;
                if (ThemePreviewActivity.this.actionBar.getVisibility() == 0) {
                    n3 = size2 - measuredHeight;
                }
                ((FrameLayout$LayoutParams)ThemePreviewActivity.this.listView.getLayoutParams()).topMargin = measuredHeight;
                ThemePreviewActivity.this.listView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n3, 1073741824));
                this.measureChildWithMargins((View)ThemePreviewActivity.this.floatingButton, n, 0, n2, 0);
            }
        }).addView((View)super.actionBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(true);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        final RecyclerListView listView = this.listView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 2;
        int verticalScrollbarPosition;
        if (isRTL) {
            verticalScrollbarPosition = 1;
        }
        else {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        this.page1.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.floatingButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        CombinedDrawable simpleSelectorCircleDrawable;
        final Drawable drawable = simpleSelectorCircleDrawable = (CombinedDrawable)Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (Build$VERSION.SDK_INT < 21) {
            final Drawable mutate = context.getResources().getDrawable(2131165387).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
            simpleSelectorCircleDrawable = new CombinedDrawable(mutate, drawable, 0, 0);
            simpleSelectorCircleDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
        this.floatingButton.setBackgroundDrawable((Drawable)simpleSelectorCircleDrawable);
        this.floatingButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff$Mode.MULTIPLY));
        this.floatingButton.setImageResource(2131165386);
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            this.floatingButton.setStateListAnimator(stateListAnimator);
            this.floatingButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @SuppressLint({ "NewApi" })
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        final FrameLayout page1 = this.page1;
        final ImageView floatingButton = this.floatingButton;
        int n2;
        if (Build$VERSION.SDK_INT >= 21) {
            n2 = 56;
        }
        else {
            n2 = 60;
        }
        float n3;
        if (Build$VERSION.SDK_INT >= 21) {
            n3 = 56.0f;
        }
        else {
            n3 = 60.0f;
        }
        int n4;
        if (LocaleController.isRTL) {
            n4 = 3;
        }
        else {
            n4 = 5;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 14.0f;
        }
        else {
            n5 = 0.0f;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 0.0f;
        }
        else {
            n6 = 14.0f;
        }
        page1.addView((View)floatingButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n2, n3, n4 | 0x50, n5, 0.0f, n6, 14.0f));
        this.dialogsAdapter = new DialogsAdapter(context);
        this.listView.setAdapter(this.dialogsAdapter);
        (this.page2 = new SizeNotifierFrameLayout(context) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view == ThemePreviewActivity.this.actionBar2 && ThemePreviewActivity.this.parentLayout != null) {
                    final ActionBarLayout access$1300 = ThemePreviewActivity.this.parentLayout;
                    int measuredHeight;
                    if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                        measuredHeight = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
                    }
                    else {
                        measuredHeight = 0;
                    }
                    access$1300.drawHeaderShadow(canvas, measuredHeight);
                }
                return drawChild;
            }
            
            protected void onMeasure(int n, int measuredHeight) {
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(measuredHeight);
                this.setMeasuredDimension(size, size2);
                this.measureChildWithMargins((View)ThemePreviewActivity.this.actionBar2, n, 0, measuredHeight, 0);
                measuredHeight = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
                n = size2;
                if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                    n = size2 - measuredHeight;
                }
                ((FrameLayout$LayoutParams)ThemePreviewActivity.this.listView2.getLayoutParams()).topMargin = measuredHeight;
                ThemePreviewActivity.this.listView2.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            }
        }).setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
        this.messagesAdapter = new MessagesAdapter(context);
        (this.actionBar2 = this.createActionBar(context)).setBackButtonDrawable(new BackDrawable(false));
        if (this.messagesAdapter.showSecretMessages) {
            this.actionBar2.setTitle("Telegram Beta Chat");
            this.actionBar2.setSubtitle(LocaleController.formatPluralString("Members", 505));
        }
        else {
            this.actionBar2.setTitle("Reinhardt");
            this.actionBar2.setSubtitle(LocaleController.formatDateOnline(System.currentTimeMillis() / 1000L - 3600L));
        }
        this.page2.addView((View)this.actionBar2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        (this.listView2 = new RecyclerListView(context) {
            @Override
            public boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view instanceof ChatMessageCell) {
                    final ChatMessageCell chatMessageCell = (ChatMessageCell)view;
                    chatMessageCell.getMessageObject();
                    final ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                    if (avatarImage != null) {
                        final int top = view.getTop();
                        if (chatMessageCell.isPinnedBottom()) {
                            final ViewHolder childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view);
                            if (childViewHolder != null && ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                avatarImage.draw(canvas);
                                return drawChild;
                            }
                        }
                        float translationX = chatMessageCell.getTranslationX();
                        final int n2 = view.getTop() + chatMessageCell.getLayoutHeight();
                        final int n3 = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                        int n4;
                        if ((n4 = n2) > n3) {
                            n4 = n3;
                        }
                        int n5 = top;
                        float n6 = translationX;
                        Label_0361: {
                            if (chatMessageCell.isPinnedTop()) {
                                ViewHolder viewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view);
                                n5 = top;
                                n6 = translationX;
                                if (viewHolder != null) {
                                    int i = 0;
                                    n5 = top;
                                    while (i < 20) {
                                        ++i;
                                        viewHolder = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition() + 1);
                                        n6 = translationX;
                                        if (viewHolder == null) {
                                            break Label_0361;
                                        }
                                        final int top2 = viewHolder.itemView.getTop();
                                        float min = translationX;
                                        if (n4 - AndroidUtilities.dp(48.0f) < viewHolder.itemView.getBottom()) {
                                            min = Math.min(viewHolder.itemView.getTranslationX(), translationX);
                                        }
                                        final View itemView = viewHolder.itemView;
                                        n5 = top2;
                                        n6 = min;
                                        if (!(itemView instanceof ChatMessageCell)) {
                                            break Label_0361;
                                        }
                                        n5 = top2;
                                        translationX = min;
                                        if (!((ChatMessageCell)itemView).isPinnedTop()) {
                                            n6 = min;
                                            n5 = top2;
                                            break Label_0361;
                                        }
                                    }
                                    n6 = translationX;
                                }
                            }
                        }
                        int n7 = n4;
                        if (n4 - AndroidUtilities.dp(48.0f) < n5) {
                            n7 = n5 + AndroidUtilities.dp(48.0f);
                        }
                        if (n6 != 0.0f) {
                            canvas.save();
                            canvas.translate(n6, 0.0f);
                        }
                        avatarImage.setImageY(n7 - AndroidUtilities.dp(44.0f));
                        avatarImage.draw(canvas);
                        if (n6 != 0.0f) {
                            canvas.restore();
                        }
                    }
                }
                return drawChild;
            }
        }).setVerticalScrollBarEnabled(true);
        this.listView2.setItemAnimator(null);
        this.listView2.setLayoutAnimation((LayoutAnimationController)null);
        this.listView2.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
        this.listView2.setClipToPadding(false);
        this.listView2.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, true));
        final RecyclerListView listView2 = this.listView2;
        int verticalScrollbarPosition2 = n;
        if (LocaleController.isRTL) {
            verticalScrollbarPosition2 = 1;
        }
        listView2.setVerticalScrollbarPosition(verticalScrollbarPosition2);
        this.page2.addView((View)this.listView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView2.setAdapter(this.messagesAdapter);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        final ViewPager viewPager = new ViewPager(context);
        viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
            }
            
            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
            }
            
            @Override
            public void onPageSelected(final int n) {
                ThemePreviewActivity.this.dotsContainer.invalidate();
            }
        });
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
                viewGroup.removeView((View)o);
            }
            
            @Override
            public int getCount() {
                return 2;
            }
            
            @Override
            public int getItemPosition(final Object o) {
                return -1;
            }
            
            @Override
            public Object instantiateItem(final ViewGroup viewGroup, final int n) {
                FrameLayout frameLayout;
                if (n == 0) {
                    frameLayout = ThemePreviewActivity.this.page1;
                }
                else {
                    frameLayout = ThemePreviewActivity.this.page2;
                }
                viewGroup.addView((View)frameLayout);
                return frameLayout;
            }
            
            @Override
            public boolean isViewFromObject(final View view, final Object o) {
                return o == view;
            }
            
            @Override
            public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
                if (dataSetObserver != null) {
                    super.unregisterDataSetObserver(dataSetObserver);
                }
            }
        });
        AndroidUtilities.setViewPagerEdgeEffectColor(viewPager, Theme.getColor("actionBarDefault"));
        frameLayout.addView((View)viewPager, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        final View view = new View(context);
        view.setBackgroundResource(2131165408);
        frameLayout.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        final FrameLayout frameLayout2 = new FrameLayout(context);
        frameLayout2.setBackgroundColor(-1);
        frameLayout.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        frameLayout2.addView(this.dotsContainer = new View(context) {
            private Paint paint = new Paint(1);
            
            protected void onDraw(final Canvas canvas) {
                final int currentItem = viewPager.getCurrentItem();
                for (int i = 0; i < 2; ++i) {
                    final Paint paint = this.paint;
                    int color;
                    if (i == currentItem) {
                        color = -6710887;
                    }
                    else {
                        color = -3355444;
                    }
                    paint.setColor(color);
                    canvas.drawCircle((float)AndroidUtilities.dp((float)(i * 15 + 3)), (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(3.0f), this.paint);
                }
            }
        }, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 8, 17));
        final TextView textView = new TextView(context);
        textView.setTextSize(1, 14.0f);
        textView.setTextColor(-15095832);
        textView.setGravity(17);
        textView.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
        textView.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
        textView.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout2.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemePreviewActivity$VGx4sgeoasvuNaWlUta0WpzoZKY(this));
        final TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setTextColor(-15095832);
        textView2.setGravity(17);
        textView2.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
        textView2.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
        textView2.setText((CharSequence)LocaleController.getString("ApplyTheme", 2131558639).toUpperCase());
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout2.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
        textView2.setOnClickListener((View$OnClickListener)new _$$Lambda$ThemePreviewActivity$YPhE_lyYHwkD5fNafYq_tQN7oD0(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int i, int childCount, final Object... array) {
        if (i == NotificationCenter.emojiDidLoad) {
            final RecyclerListView listView = this.listView;
            if (listView == null) {
                return;
            }
            View child;
            for (childCount = listView.getChildCount(), i = 0; i < childCount; ++i) {
                child = this.listView.getChildAt(i);
                if (child instanceof DialogCell) {
                    ((DialogCell)child).update(0);
                }
            }
        }
    }
    
    @Override
    public boolean onBackPressed() {
        Theme.applyPreviousTheme();
        super.parentLayout.rebuildAllFragmentViews(false, false);
        return super.onBackPressed();
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        super.onFragmentDestroy();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final SizeNotifierFrameLayout page2 = this.page2;
        if (page2 != null) {
            page2.onResume();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final DialogsAdapter dialogsAdapter = this.dialogsAdapter;
        if (dialogsAdapter != null) {
            ((RecyclerView.Adapter)dialogsAdapter).notifyDataSetChanged();
        }
        final MessagesAdapter messagesAdapter = this.messagesAdapter;
        if (messagesAdapter != null) {
            ((RecyclerView.Adapter)messagesAdapter).notifyDataSetChanged();
        }
        final SizeNotifierFrameLayout page2 = this.page2;
        if (page2 != null) {
            page2.onResume();
        }
    }
    
    public class DialogsAdapter extends SelectionAdapter
    {
        private ArrayList<DialogCell.CustomDialog> dialogs;
        private Context mContext;
        
        public DialogsAdapter(final Context mContext) {
            this.mContext = mContext;
            this.dialogs = new ArrayList<DialogCell.CustomDialog>();
            final int date = (int)(System.currentTimeMillis() / 1000L);
            final DialogCell.CustomDialog e = new DialogCell.CustomDialog();
            e.name = "Eva Summer";
            e.message = "Reminds me of a Chinese prove...";
            e.id = 0;
            e.unread_count = 0;
            e.pinned = true;
            e.muted = false;
            e.type = 0;
            e.date = date;
            e.verified = false;
            e.isMedia = false;
            e.sent = true;
            this.dialogs.add(e);
            final DialogCell.CustomDialog e2 = new DialogCell.CustomDialog();
            e2.name = "Your inner Competition";
            e2.message = "hey, I've updated the source code.";
            e2.id = 1;
            e2.unread_count = 2;
            e2.pinned = false;
            e2.muted = false;
            e2.type = 0;
            e2.date = date - 3600;
            e2.verified = false;
            e2.isMedia = false;
            e2.sent = false;
            this.dialogs.add(e2);
            final DialogCell.CustomDialog e3 = new DialogCell.CustomDialog();
            e3.name = "Mike Apple";
            e3.message = "\ud83e\udd37\u200d\u2642\ufe0f Sticker";
            e3.id = 2;
            e3.unread_count = 3;
            e3.pinned = false;
            e3.muted = true;
            e3.type = 0;
            e3.date = date - 7200;
            e3.verified = false;
            e3.isMedia = true;
            e3.sent = false;
            this.dialogs.add(e3);
            final DialogCell.CustomDialog e4 = new DialogCell.CustomDialog();
            e4.name = "Paul Newman";
            e4.message = "Any ideas?";
            e4.id = 3;
            e4.unread_count = 0;
            e4.pinned = false;
            e4.muted = false;
            e4.type = 2;
            e4.date = date - 10800;
            e4.verified = false;
            e4.isMedia = false;
            e4.sent = false;
            this.dialogs.add(e4);
            final DialogCell.CustomDialog e5 = new DialogCell.CustomDialog();
            e5.name = "Old Pirates";
            e5.message = "Yo-ho-ho!";
            e5.id = 4;
            e5.unread_count = 0;
            e5.pinned = false;
            e5.muted = false;
            e5.type = 1;
            e5.date = date - 14400;
            e5.verified = false;
            e5.isMedia = false;
            e5.sent = true;
            this.dialogs.add(e5);
            final DialogCell.CustomDialog e6 = new DialogCell.CustomDialog();
            e6.name = "Kate Bright";
            e6.message = "Hola!";
            e6.id = 5;
            e6.unread_count = 0;
            e6.pinned = false;
            e6.muted = false;
            e6.type = 0;
            e6.date = date - 18000;
            e6.verified = false;
            e6.isMedia = false;
            e6.sent = false;
            this.dialogs.add(e6);
            final DialogCell.CustomDialog e7 = new DialogCell.CustomDialog();
            e7.name = "Nick K";
            e7.message = "These are not the droids you are looking for";
            e7.id = 6;
            e7.unread_count = 0;
            e7.pinned = false;
            e7.muted = false;
            e7.type = 0;
            e7.date = date - 21600;
            e7.verified = true;
            e7.isMedia = false;
            e7.sent = false;
            this.dialogs.add(e7);
            final DialogCell.CustomDialog e8 = new DialogCell.CustomDialog();
            e8.name = "Adler Toberg";
            e8.message = "Did someone say peanut butter?";
            e8.id = 0;
            e8.unread_count = 0;
            e8.pinned = false;
            e8.muted = false;
            e8.type = 0;
            e8.date = date - 25200;
            e8.verified = true;
            e8.isMedia = false;
            e8.sent = false;
            this.dialogs.add(e8);
        }
        
        @Override
        public int getItemCount() {
            return this.dialogs.size();
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == this.dialogs.size()) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = true;
            if (itemViewType == 1) {
                b = false;
            }
            return b;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
            if (viewHolder.getItemViewType() == 0) {
                final DialogCell dialogCell = (DialogCell)viewHolder.itemView;
                final int itemCount = this.getItemCount();
                boolean useSeparator = true;
                if (index == itemCount - 1) {
                    useSeparator = false;
                }
                dialogCell.useSeparator = useSeparator;
                dialogCell.setDialog((DialogCell.CustomDialog)this.dialogs.get(index));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n == 0) {
                o = new DialogCell(this.mContext, false, false);
            }
            else if (n == 1) {
                o = new LoadingCell(this.mContext);
            }
            else {
                o = null;
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    public class MessagesAdapter extends SelectionAdapter
    {
        private Context mContext;
        private ArrayList<MessageObject> messages;
        private boolean showSecretMessages;
        
        public MessagesAdapter(final Context mContext) {
            final int nextInt = Utilities.random.nextInt(100);
            int n;
            if (BuildVars.DEBUG_VERSION) {
                n = 5;
            }
            else {
                n = 1;
            }
            this.showSecretMessages = (nextInt <= n);
            this.mContext = mContext;
            this.messages = new ArrayList<MessageObject>();
            final int n2 = (int)(System.currentTimeMillis() / 1000L) - 3600;
            if (this.showSecretMessages) {
                final TLRPC.TL_user e = new TLRPC.TL_user();
                e.id = Integer.MAX_VALUE;
                e.first_name = "Me";
                final TLRPC.TL_user e2 = new TLRPC.TL_user();
                e2.id = 2147483646;
                e2.first_name = "Serj";
                final ArrayList<TLRPC.User> list = new ArrayList<TLRPC.User>();
                list.add(e);
                list.add(e2);
                MessagesController.getInstance(ThemePreviewActivity.this.currentAccount).putUsers(list, true);
                final TLRPC.TL_message tl_message = new TLRPC.TL_message();
                tl_message.message = "Guess why Half-Life 3 was never released.";
                final int n3 = n2 + 960;
                tl_message.date = n3;
                tl_message.dialog_id = -1L;
                tl_message.flags = 259;
                tl_message.id = 2147483646;
                tl_message.media = new TLRPC.TL_messageMediaEmpty();
                tl_message.out = false;
                tl_message.to_id = new TLRPC.TL_peerChat();
                tl_message.to_id.chat_id = 1;
                tl_message.from_id = e2.id;
                this.messages.add(new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message, true));
                final TLRPC.TL_message tl_message2 = new TLRPC.TL_message();
                tl_message2.message = "No.\nAnd every unnecessary ping of the dev delays the release for 10 days.\nEvery request for ETA delays the release for 2 weeks.";
                tl_message2.date = n3;
                tl_message2.dialog_id = -1L;
                tl_message2.flags = 259;
                tl_message2.id = 1;
                tl_message2.media = new TLRPC.TL_messageMediaEmpty();
                tl_message2.out = false;
                tl_message2.to_id = new TLRPC.TL_peerChat();
                tl_message2.to_id.chat_id = 1;
                tl_message2.from_id = e2.id;
                this.messages.add(new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message2, true));
                final TLRPC.TL_message tl_message3 = new TLRPC.TL_message();
                tl_message3.message = "Is source code for Android coming anytime soon?";
                tl_message3.date = n2 + 600;
                tl_message3.dialog_id = -1L;
                tl_message3.flags = 259;
                tl_message3.id = 1;
                tl_message3.media = new TLRPC.TL_messageMediaEmpty();
                tl_message3.out = false;
                tl_message3.to_id = new TLRPC.TL_peerChat();
                tl_message3.to_id.chat_id = 1;
                tl_message3.from_id = e.id;
                this.messages.add(new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message3, true));
            }
            else {
                final TLRPC.TL_message tl_message4 = new TLRPC.TL_message();
                tl_message4.message = "Reinhardt, we need to find you some new tunes \ud83c\udfb6.";
                final int n4 = n2 + 60;
                tl_message4.date = n4;
                tl_message4.dialog_id = 1L;
                tl_message4.flags = 259;
                tl_message4.from_id = UserConfig.getInstance(ThemePreviewActivity.this.currentAccount).getClientUserId();
                tl_message4.id = 1;
                tl_message4.media = new TLRPC.TL_messageMediaEmpty();
                tl_message4.out = true;
                tl_message4.to_id = new TLRPC.TL_peerUser();
                tl_message4.to_id.user_id = 0;
                final MessageObject messageObject = new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message4, true);
                final TLRPC.TL_message tl_message5 = new TLRPC.TL_message();
                tl_message5.message = "I can't even take you seriously right now.";
                tl_message5.date = n2 + 960;
                tl_message5.dialog_id = 1L;
                tl_message5.flags = 259;
                tl_message5.from_id = UserConfig.getInstance(ThemePreviewActivity.this.currentAccount).getClientUserId();
                tl_message5.id = 1;
                tl_message5.media = new TLRPC.TL_messageMediaEmpty();
                tl_message5.out = true;
                tl_message5.to_id = new TLRPC.TL_peerUser();
                tl_message5.to_id.user_id = 0;
                this.messages.add(new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message5, true));
                final TLRPC.TL_message tl_message6 = new TLRPC.TL_message();
                tl_message6.date = n2 + 130;
                tl_message6.dialog_id = 1L;
                tl_message6.flags = 259;
                tl_message6.from_id = 0;
                tl_message6.id = 5;
                tl_message6.media = new TLRPC.TL_messageMediaDocument();
                final TLRPC.MessageMedia media = tl_message6.media;
                media.flags |= 0x3;
                media.document = new TLRPC.TL_document();
                final TLRPC.Document document = tl_message6.media.document;
                document.mime_type = "audio/mp4";
                document.file_reference = new byte[0];
                final TLRPC.TL_documentAttributeAudio e3 = new TLRPC.TL_documentAttributeAudio();
                e3.duration = 243;
                e3.performer = "David Hasselhoff";
                e3.title = "True Survivor";
                tl_message6.media.document.attributes.add(e3);
                tl_message6.out = false;
                tl_message6.to_id = new TLRPC.TL_peerUser();
                tl_message6.to_id.user_id = UserConfig.getInstance(ThemePreviewActivity.this.currentAccount).getClientUserId();
                this.messages.add(new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message6, true));
                final TLRPC.TL_message tl_message7 = new TLRPC.TL_message();
                tl_message7.message = "Ah, you kids today with techno music! You should enjoy the classics, like Hasselhoff!";
                tl_message7.date = n4;
                tl_message7.dialog_id = 1L;
                tl_message7.flags = 265;
                tl_message7.from_id = 0;
                tl_message7.id = 1;
                tl_message7.reply_to_msg_id = 5;
                tl_message7.media = new TLRPC.TL_messageMediaEmpty();
                tl_message7.out = false;
                tl_message7.to_id = new TLRPC.TL_peerUser();
                tl_message7.to_id.user_id = UserConfig.getInstance(ThemePreviewActivity.this.currentAccount).getClientUserId();
                final MessageObject e4 = new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message7, true);
                e4.customReplyName = "Lucio";
                e4.replyMessageObject = messageObject;
                this.messages.add(e4);
                final TLRPC.TL_message tl_message8 = new TLRPC.TL_message();
                tl_message8.date = n2 + 120;
                tl_message8.dialog_id = 1L;
                tl_message8.flags = 259;
                tl_message8.from_id = UserConfig.getInstance(ThemePreviewActivity.this.currentAccount).getClientUserId();
                tl_message8.id = 1;
                tl_message8.media = new TLRPC.TL_messageMediaDocument();
                final TLRPC.MessageMedia media2 = tl_message8.media;
                media2.flags |= 0x3;
                media2.document = new TLRPC.TL_document();
                final TLRPC.Document document2 = tl_message8.media.document;
                document2.mime_type = "audio/ogg";
                document2.file_reference = new byte[0];
                final TLRPC.TL_documentAttributeAudio e5 = new TLRPC.TL_documentAttributeAudio();
                e5.flags = 1028;
                e5.duration = 3;
                e5.voice = true;
                e5.waveform = new byte[] { 0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1 };
                tl_message8.media.document.attributes.add(e5);
                tl_message8.out = true;
                tl_message8.to_id = new TLRPC.TL_peerUser();
                tl_message8.to_id.user_id = 0;
                final MessageObject e6 = new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message8, true);
                e6.audioProgressSec = 1;
                e6.audioProgress = 0.3f;
                e6.useCustomPhoto = true;
                this.messages.add(e6);
                this.messages.add(messageObject);
                final TLRPC.TL_message tl_message9 = new TLRPC.TL_message();
                tl_message9.date = n2 + 10;
                tl_message9.dialog_id = 1L;
                tl_message9.flags = 257;
                tl_message9.from_id = 0;
                tl_message9.id = 1;
                tl_message9.media = new TLRPC.TL_messageMediaPhoto();
                final TLRPC.MessageMedia media3 = tl_message9.media;
                media3.flags |= 0x3;
                media3.photo = new TLRPC.TL_photo();
                final TLRPC.Photo photo = tl_message9.media.photo;
                photo.file_reference = new byte[0];
                photo.has_stickers = false;
                photo.id = 1L;
                photo.access_hash = 0L;
                photo.date = n2;
                final TLRPC.TL_photoSize e7 = new TLRPC.TL_photoSize();
                e7.size = 0;
                e7.w = 500;
                e7.h = 302;
                e7.type = "s";
                e7.location = new TLRPC.TL_fileLocationUnavailable();
                tl_message9.media.photo.sizes.add(e7);
                tl_message9.message = "Bring it on! I LIVE for this!";
                tl_message9.out = false;
                tl_message9.to_id = new TLRPC.TL_peerUser();
                tl_message9.to_id.user_id = UserConfig.getInstance(ThemePreviewActivity.this.currentAccount).getClientUserId();
                final MessageObject e8 = new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message9, true);
                e8.useCustomPhoto = true;
                this.messages.add(e8);
            }
            final TLRPC.TL_message tl_message10 = new TLRPC.TL_message();
            tl_message10.message = LocaleController.formatDateChat(n2);
            tl_message10.id = 0;
            tl_message10.date = n2;
            final MessageObject e9 = new MessageObject(ThemePreviewActivity.this.currentAccount, tl_message10, false);
            e9.type = 10;
            e9.contentType = 1;
            e9.isDateObject = true;
            this.messages.add(e9);
        }
        
        @Override
        public int getItemCount() {
            return this.messages.size();
        }
        
        @Override
        public int getItemViewType(final int index) {
            if (index >= 0 && index < this.messages.size()) {
                return this.messages.get(index).contentType;
            }
            return 4;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int itemViewType) {
            final MessageObject messageObject = this.messages.get(itemViewType);
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ChatMessageCell) {
                final ChatMessageCell chatMessageCell = (ChatMessageCell)itemView;
                final boolean b = false;
                chatMessageCell.isChat = false;
                final int index = itemViewType - 1;
                final int itemViewType2 = this.getItemViewType(index);
                final int index2 = itemViewType + 1;
                itemViewType = this.getItemViewType(index2);
                boolean b2 = false;
                Label_0149: {
                    if (!(messageObject.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && itemViewType2 == viewHolder.getItemViewType()) {
                        final MessageObject messageObject2 = this.messages.get(index);
                        if (messageObject2.isOutOwner() == messageObject.isOutOwner() && Math.abs(messageObject2.messageOwner.date - messageObject.messageOwner.date) <= 300) {
                            b2 = true;
                            break Label_0149;
                        }
                    }
                    b2 = false;
                }
                boolean b3 = b;
                if (itemViewType == viewHolder.getItemViewType()) {
                    final MessageObject messageObject3 = this.messages.get(index2);
                    b3 = b;
                    if (!(messageObject3.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
                        b3 = b;
                        if (messageObject3.isOutOwner() == messageObject.isOutOwner()) {
                            b3 = b;
                            if (Math.abs(messageObject3.messageOwner.date - messageObject.messageOwner.date) <= 300) {
                                b3 = true;
                            }
                        }
                    }
                }
                chatMessageCell.isChat = this.showSecretMessages;
                chatMessageCell.setFullyDraw(true);
                chatMessageCell.setMessageObject(messageObject, null, b2, b3);
            }
            else if (itemView instanceof ChatActionCell) {
                final ChatActionCell chatActionCell = (ChatActionCell)itemView;
                chatActionCell.setMessageObject(messageObject);
                chatActionCell.setAlpha(1.0f);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            BaseCell baseCell;
            if (n == 0) {
                baseCell = new ChatMessageCell(this.mContext);
                ((ChatMessageCell)baseCell).setDelegate((ChatMessageCell.ChatMessageCellDelegate)new ChatMessageCell.ChatMessageCellDelegate() {});
            }
            else if (n == 1) {
                baseCell = new ChatActionCell(this.mContext);
                ((ChatActionCell)baseCell).setDelegate((ChatActionCell.ChatActionCellDelegate)new ChatActionCell.ChatActionCellDelegate() {});
            }
            else {
                baseCell = null;
            }
            ((View)baseCell).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)baseCell);
        }
    }
}
