// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.AlertDialog;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.FeaturedStickerSetInfoCell;
import android.util.SparseArray;
import org.telegram.messenger.FileRefController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.Emoji;
import android.graphics.Point;
import android.annotation.SuppressLint;
import java.util.regex.Matcher;
import android.text.method.MovementMethod;
import android.text.SpannableStringBuilder;
import android.util.Property;
import org.telegram.messenger.FileLog;
import android.widget.Toast;
import org.telegram.messenger.ApplicationLoader;
import android.app.Dialog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.DataQuery;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.text.TextUtils$TruncateAt;
import android.view.View$OnTouchListener;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.graphics.Color;
import android.os.Build$VERSION;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ContentPreviewViewer$ContentPreviewViewerDelegate$_CC;
import android.content.Context;
import java.util.regex.Pattern;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import android.animation.AnimatorSet;
import android.view.View;
import org.telegram.ui.ContentPreviewViewer;
import android.widget.TextView;
import org.telegram.ui.ActionBar.BaseFragment;
import android.app.Activity;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import androidx.recyclerview.widget.GridLayoutManager;
import org.telegram.tgnet.TLRPC;
import android.widget.FrameLayout;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BottomSheet;

public class StickersAlert extends BottomSheet implements NotificationCenterDelegate
{
    private GridAdapter adapter;
    private boolean clearsInputField;
    private StickersAlertDelegate delegate;
    private FrameLayout emptyView;
    private RecyclerListView gridView;
    private boolean ignoreLayout;
    private TLRPC.InputStickerSet inputStickerSet;
    private StickersAlertInstallDelegate installDelegate;
    private int itemSize;
    private GridLayoutManager layoutManager;
    private ActionBarMenuItem optionsButton;
    private Activity parentActivity;
    private BaseFragment parentFragment;
    private TextView pickerBottomLayout;
    private ContentPreviewViewer.ContentPreviewViewerDelegate previewDelegate;
    private TextView previewSendButton;
    private View previewSendButtonShadow;
    private int reqId;
    private int scrollOffsetY;
    private TLRPC.Document selectedSticker;
    private View[] shadow;
    private AnimatorSet[] shadowAnimation;
    private Drawable shadowDrawable;
    private boolean showEmoji;
    private TextView stickerEmojiTextView;
    private BackupImageView stickerImageView;
    private FrameLayout stickerPreviewLayout;
    private TLRPC.TL_messages_stickerSet stickerSet;
    private ArrayList<TLRPC.StickerSetCovered> stickerSetCovereds;
    private RecyclerListView.OnItemClickListener stickersOnItemClickListener;
    private TextView titleTextView;
    private Pattern urlPattern;
    
    public StickersAlert(final Context context, final Object o, final TLRPC.Photo photo) {
        super(context, false, 1);
        this.shadowAnimation = new AnimatorSet[2];
        this.shadow = new View[2];
        this.previewDelegate = new ContentPreviewViewer.ContentPreviewViewerDelegate() {
            @Override
            public boolean needOpen() {
                return false;
            }
            
            @Override
            public boolean needSend() {
                return StickersAlert.this.previewSendButton.getVisibility() == 0;
            }
            
            @Override
            public void openSet(final TLRPC.InputStickerSet set, final boolean b) {
            }
            
            @Override
            public void sendSticker(final TLRPC.Document document, final Object o) {
                StickersAlert.this.delegate.onStickerSelected(document, o, StickersAlert.this.clearsInputField);
                StickersAlert.this.dismiss();
            }
        };
        this.parentActivity = (Activity)context;
        final TLRPC.TL_messages_getAttachedStickers tl_messages_getAttachedStickers = new TLRPC.TL_messages_getAttachedStickers();
        final TLRPC.TL_inputStickeredMediaPhoto media = new TLRPC.TL_inputStickeredMediaPhoto();
        media.id = new TLRPC.TL_inputPhoto();
        final TLRPC.InputPhoto id = media.id;
        id.id = photo.id;
        id.access_hash = photo.access_hash;
        id.file_reference = photo.file_reference;
        if (id.file_reference == null) {
            id.file_reference = new byte[0];
        }
        tl_messages_getAttachedStickers.media = media;
        this.reqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getAttachedStickers, new _$$Lambda$StickersAlert$70AzTfODxLbeSRDlB8gJ7WKPlnE(this, o, tl_messages_getAttachedStickers, new _$$Lambda$StickersAlert$R4T4Ne_ypM57cFmDbPuLpbHC0Ro(this, tl_messages_getAttachedStickers)));
        this.init(context);
    }
    
    public StickersAlert(final Context context, final BaseFragment parentFragment, final TLRPC.InputStickerSet inputStickerSet, final TLRPC.TL_messages_stickerSet stickerSet, final StickersAlertDelegate delegate) {
        super(context, false, 1);
        this.shadowAnimation = new AnimatorSet[2];
        this.shadow = new View[2];
        this.previewDelegate = new ContentPreviewViewer.ContentPreviewViewerDelegate() {
            @Override
            public boolean needOpen() {
                return false;
            }
            
            @Override
            public boolean needSend() {
                return StickersAlert.this.previewSendButton.getVisibility() == 0;
            }
            
            @Override
            public void openSet(final TLRPC.InputStickerSet set, final boolean b) {
            }
            
            @Override
            public void sendSticker(final TLRPC.Document document, final Object o) {
                StickersAlert.this.delegate.onStickerSelected(document, o, StickersAlert.this.clearsInputField);
                StickersAlert.this.dismiss();
            }
        };
        this.delegate = delegate;
        this.inputStickerSet = inputStickerSet;
        this.stickerSet = stickerSet;
        this.parentFragment = parentFragment;
        this.loadStickerSet();
        this.init(context);
    }
    
    private void hidePreview() {
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.stickerPreviewLayout, View.ALPHA, new float[] { 0.0f }) });
        set.setDuration(200L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                StickersAlert.this.stickerPreviewLayout.setVisibility(8);
            }
        });
        set.start();
    }
    
    private void init(final Context context) {
        (this.shadowDrawable = context.getResources().getDrawable(2131165824).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogBackground"), PorterDuff$Mode.MULTIPLY));
        (super.containerView = (ViewGroup)new FrameLayout(context) {
            private boolean fullHeight;
            private int lastNotifyWidth;
            private RectF rect = new RectF();
            
            protected void onDraw(final Canvas canvas) {
                final int n = StickersAlert.this.scrollOffsetY - StickersAlert.this.backgroundPaddingTop + AndroidUtilities.dp(6.0f);
                final int n2 = StickersAlert.this.scrollOffsetY - StickersAlert.this.backgroundPaddingTop - AndroidUtilities.dp(13.0f);
                final int n3 = this.getMeasuredHeight() + AndroidUtilities.dp(15.0f) + StickersAlert.this.backgroundPaddingTop;
                int n4 = n;
                int n5 = n2;
                int n6 = n3;
                int n7 = 0;
                int n8 = 0;
                float n10 = 0.0f;
                int n12 = 0;
                Label_0286: {
                    float n11 = 0.0f;
                    Label_0273: {
                        if (Build$VERSION.SDK_INT >= 21) {
                            final int statusBarHeight = AndroidUtilities.statusBarHeight;
                            n7 = n2 + statusBarHeight;
                            n8 = n + statusBarHeight;
                            int n9 = n3 - statusBarHeight;
                            n4 = n8;
                            n5 = n7;
                            n6 = n9;
                            if (this.fullHeight) {
                                final int access$2100 = StickersAlert.this.backgroundPaddingTop;
                                final int statusBarHeight2 = AndroidUtilities.statusBarHeight;
                                if (access$2100 + n7 < statusBarHeight2 * 2) {
                                    final int min = Math.min(statusBarHeight2, statusBarHeight2 * 2 - n7 - StickersAlert.this.backgroundPaddingTop);
                                    n7 -= min;
                                    n9 += min;
                                    n10 = 1.0f - Math.min(1.0f, min * 2 / (float)AndroidUtilities.statusBarHeight);
                                }
                                else {
                                    n10 = 1.0f;
                                }
                                final int access$2101 = StickersAlert.this.backgroundPaddingTop;
                                final int statusBarHeight3 = AndroidUtilities.statusBarHeight;
                                n4 = n8;
                                n5 = n7;
                                n6 = n9;
                                n11 = n10;
                                if (access$2101 + n7 < statusBarHeight3) {
                                    final int min2 = Math.min(statusBarHeight3, statusBarHeight3 - n7 - StickersAlert.this.backgroundPaddingTop);
                                    n6 = n9;
                                    n12 = min2;
                                    break Label_0286;
                                }
                                break Label_0273;
                            }
                        }
                        n11 = 1.0f;
                    }
                    n12 = 0;
                    n10 = n11;
                    n7 = n5;
                    n8 = n4;
                }
                StickersAlert.this.shadowDrawable.setBounds(0, n7, this.getMeasuredWidth(), n6);
                StickersAlert.this.shadowDrawable.draw(canvas);
                if (n10 != 1.0f) {
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("dialogBackground"));
                    this.rect.set((float)StickersAlert.this.backgroundPaddingLeft, (float)(StickersAlert.this.backgroundPaddingTop + n7), (float)(this.getMeasuredWidth() - StickersAlert.this.backgroundPaddingLeft), (float)(StickersAlert.this.backgroundPaddingTop + n7 + AndroidUtilities.dp(24.0f)));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * n10, AndroidUtilities.dp(12.0f) * n10, Theme.dialogs_onlineCirclePaint);
                }
                final int dp = AndroidUtilities.dp(36.0f);
                this.rect.set((float)((this.getMeasuredWidth() - dp) / 2), (float)n8, (float)((this.getMeasuredWidth() + dp) / 2), (float)(n8 + AndroidUtilities.dp(4.0f)));
                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("key_sheet_scrollUp"));
                canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                if (n12 > 0) {
                    final int color = Theme.getColor("dialogBackground");
                    Theme.dialogs_onlineCirclePaint.setColor(Color.argb(255, (int)(Color.red(color) * 0.8f), (int)(Color.green(color) * 0.8f), (int)(Color.blue(color) * 0.8f)));
                    canvas.drawRect((float)StickersAlert.this.backgroundPaddingLeft, (float)(AndroidUtilities.statusBarHeight - n12), (float)(this.getMeasuredWidth() - StickersAlert.this.backgroundPaddingLeft), (float)AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
                }
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && StickersAlert.this.scrollOffsetY != 0 && motionEvent.getY() < StickersAlert.this.scrollOffsetY) {
                    StickersAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                final int lastNotifyWidth = this.lastNotifyWidth;
                final int lastNotifyWidth2 = n3 - n;
                if (lastNotifyWidth != lastNotifyWidth2) {
                    this.lastNotifyWidth = lastNotifyWidth2;
                    if (StickersAlert.this.adapter != null && StickersAlert.this.stickerSetCovereds != null) {
                        StickersAlert.this.adapter.notifyDataSetChanged();
                    }
                }
                super.onLayout(b, n, n2, n3, n4);
                StickersAlert.this.updateLayout();
            }
            
            protected void onMeasure(final int n, int b) {
                final int size = View$MeasureSpec.getSize(b);
                b = Build$VERSION.SDK_INT;
                boolean fullHeight = true;
                if (b >= 21) {
                    StickersAlert.this.ignoreLayout = true;
                    this.setPadding(StickersAlert.this.backgroundPaddingLeft, AndroidUtilities.statusBarHeight, StickersAlert.this.backgroundPaddingLeft, 0);
                    StickersAlert.this.ignoreLayout = false;
                }
                this.getMeasuredWidth();
                StickersAlert.this.itemSize = (View$MeasureSpec.getSize(n) - AndroidUtilities.dp(36.0f)) / 5;
                int n2;
                if (StickersAlert.this.stickerSetCovereds != null) {
                    n2 = AndroidUtilities.dp(56.0f) + AndroidUtilities.dp(60.0f) * StickersAlert.this.stickerSetCovereds.size() + StickersAlert.this.adapter.stickersRowCount * AndroidUtilities.dp(82.0f) + StickersAlert.this.backgroundPaddingTop;
                    b = AndroidUtilities.dp(24.0f);
                }
                else {
                    final int dp = AndroidUtilities.dp(96.0f);
                    if (StickersAlert.this.stickerSet != null) {
                        b = (int)Math.ceil(StickersAlert.this.stickerSet.documents.size() / 5.0f);
                    }
                    else {
                        b = 0;
                    }
                    n2 = dp + Math.max(3, b) * AndroidUtilities.dp(82.0f) + StickersAlert.this.backgroundPaddingTop;
                    b = AndroidUtilities.statusBarHeight;
                }
                final int a = n2 + b;
                final double n3 = a;
                b = size / 5;
                final double v = b;
                Double.isNaN(v);
                int n4;
                if (n3 < v * 3.2) {
                    n4 = 0;
                }
                else {
                    n4 = b * 2;
                }
                b = n4;
                if (n4 != 0) {
                    b = n4;
                    if (a < size) {
                        b = n4 - (size - a);
                    }
                }
                int access$1400;
                if ((access$1400 = b) == 0) {
                    access$1400 = StickersAlert.this.backgroundPaddingTop;
                }
                b = access$1400;
                if (StickersAlert.this.stickerSetCovereds != null) {
                    b = access$1400 + AndroidUtilities.dp(8.0f);
                }
                if (StickersAlert.this.gridView.getPaddingTop() != b) {
                    StickersAlert.this.ignoreLayout = true;
                    StickersAlert.this.gridView.setPadding(AndroidUtilities.dp(10.0f), b, AndroidUtilities.dp(10.0f), 0);
                    StickersAlert.this.emptyView.setPadding(0, b, 0, 0);
                    StickersAlert.this.ignoreLayout = false;
                }
                if (a < size) {
                    fullHeight = false;
                }
                this.fullHeight = fullHeight;
                super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(Math.min(a, size), 1073741824));
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return !StickersAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }
            
            public void requestLayout() {
                if (StickersAlert.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setWillNotDraw(false);
        final ViewGroup containerView = super.containerView;
        final int backgroundPaddingLeft = super.backgroundPaddingLeft;
        containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(48.0f);
        (this.shadow[0] = new View(context)).setBackgroundColor(Theme.getColor("dialogShadowLine"));
        this.shadow[0].setAlpha(0.0f);
        this.shadow[0].setVisibility(4);
        this.shadow[0].setTag((Object)1);
        super.containerView.addView(this.shadow[0], (ViewGroup$LayoutParams)frameLayout$LayoutParams);
        (this.gridView = new RecyclerListView(context) {
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                final ContentPreviewViewer instance = ContentPreviewViewer.getInstance();
                final RecyclerListView access$1500 = StickersAlert.this.gridView;
                final ContentPreviewViewer.ContentPreviewViewerDelegate access$1501 = StickersAlert.this.previewDelegate;
                boolean b = false;
                final boolean onInterceptTouchEvent = instance.onInterceptTouchEvent(motionEvent, access$1500, 0, access$1501);
                if (super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent) {
                    b = true;
                }
                return b;
            }
            
            @Override
            public void requestLayout() {
                if (StickersAlert.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        }).setTag((Object)14);
        this.gridView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new GridLayoutManager(this.getContext(), 5)));
        this.layoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int n) {
                if ((StickersAlert.this.stickerSetCovereds != null && StickersAlert.this.adapter.cache.get(n) instanceof Integer) || n == StickersAlert.this.adapter.totalItems) {
                    return StickersAlert.this.adapter.stickersPerRow;
                }
                return 1;
            }
        });
        this.gridView.setAdapter(this.adapter = new GridAdapter(context));
        this.gridView.setVerticalScrollBarEnabled(false);
        this.gridView.addItemDecoration((RecyclerView.ItemDecoration)new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
                rect.left = 0;
                rect.right = 0;
                rect.bottom = 0;
                rect.top = 0;
            }
        });
        this.gridView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
        this.gridView.setClipToPadding(false);
        this.gridView.setEnabled(true);
        this.gridView.setGlowColor(Theme.getColor("dialogScrollGlow"));
        this.gridView.setOnTouchListener((View$OnTouchListener)new _$$Lambda$StickersAlert$_nwA8t_8QJfVh6HJUgTR9__hiGc(this));
        this.gridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                StickersAlert.this.updateLayout();
            }
        });
        this.stickersOnItemClickListener = new _$$Lambda$StickersAlert$pqY8OpOZq71Xrxq3rh8h2TYv4Gk(this);
        this.gridView.setOnItemClickListener(this.stickersOnItemClickListener);
        super.containerView.addView((View)this.gridView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 48.0f, 0.0f, 48.0f));
        this.emptyView = new FrameLayout(context) {
            public void requestLayout() {
                if (StickersAlert.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        super.containerView.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        this.gridView.setEmptyView((View)this.emptyView);
        this.emptyView.setOnTouchListener((View$OnTouchListener)_$$Lambda$StickersAlert$x4wnZ8iSeVTFndyEhtM4m12j_W8.INSTANCE);
        (this.titleTextView = new TextView(context)).setLines(1);
        this.titleTextView.setSingleLine(true);
        this.titleTextView.setTextColor(Theme.getColor("dialogTextBlack"));
        this.titleTextView.setTextSize(1, 20.0f);
        this.titleTextView.setLinkTextColor(Theme.getColor("dialogTextLink"));
        this.titleTextView.setHighlightColor(Theme.getColor("dialogLinkSelection"));
        this.titleTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.titleTextView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        this.titleTextView.setGravity(16);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        super.containerView.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 50.0f, 51, 0.0f, 0.0f, 40.0f, 0.0f));
        (this.optionsButton = new ActionBarMenuItem(context, null, 0, Theme.getColor("key_sheet_other"))).setLongClickEnabled(false);
        this.optionsButton.setSubMenuOpenSide(2);
        this.optionsButton.setIcon(2131165416);
        this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("player_actionBarSelector"), 1));
        super.containerView.addView((View)this.optionsButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, 5.0f, 5.0f, 0.0f));
        this.optionsButton.addSubItem(1, 2131165671, LocaleController.getString("StickersShare", 2131560813));
        this.optionsButton.addSubItem(2, 2131165640, LocaleController.getString("CopyLink", 2131559164));
        this.optionsButton.setOnClickListener((View$OnClickListener)new _$$Lambda$StickersAlert$7978RXQKfGxYvNzfCRh5mokiXDU(this));
        this.optionsButton.setDelegate((ActionBarMenuItem.ActionBarMenuItemDelegate)new _$$Lambda$StickersAlert$9rn6i7qVo7Tr6wYcNUHGgSRyKIM(this));
        this.optionsButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        final ActionBarMenuItem optionsButton = this.optionsButton;
        int visibility;
        if (this.inputStickerSet != null) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        optionsButton.setVisibility(visibility);
        this.emptyView.addView((View)new RadialProgressView(context), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        final FrameLayout$LayoutParams frameLayout$LayoutParams2 = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
        frameLayout$LayoutParams2.bottomMargin = AndroidUtilities.dp(48.0f);
        (this.shadow[1] = new View(context)).setBackgroundColor(Theme.getColor("dialogShadowLine"));
        super.containerView.addView(this.shadow[1], (ViewGroup$LayoutParams)frameLayout$LayoutParams2);
        (this.pickerBottomLayout = new TextView(context)).setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("dialogBackground"), Theme.getColor("listSelectorSDK21")));
        this.pickerBottomLayout.setTextColor(Theme.getColor("dialogTextBlue2"));
        this.pickerBottomLayout.setTextSize(1, 14.0f);
        this.pickerBottomLayout.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        this.pickerBottomLayout.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.pickerBottomLayout.setGravity(17);
        super.containerView.addView((View)this.pickerBottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        (this.stickerPreviewLayout = new FrameLayout(context)).setBackgroundColor(Theme.getColor("dialogBackground") & 0xDFFFFFFF);
        this.stickerPreviewLayout.setVisibility(8);
        this.stickerPreviewLayout.setSoundEffectsEnabled(false);
        super.containerView.addView((View)this.stickerPreviewLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.stickerPreviewLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$StickersAlert$nsigcALZ_A8eXyilWi1cmTZCn2o(this));
        (this.stickerImageView = new BackupImageView(context)).setAspectFit(true);
        this.stickerPreviewLayout.addView((View)this.stickerImageView);
        (this.stickerEmojiTextView = new TextView(context)).setTextSize(1, 30.0f);
        this.stickerEmojiTextView.setGravity(85);
        this.stickerPreviewLayout.addView((View)this.stickerEmojiTextView);
        (this.previewSendButton = new TextView(context)).setTextSize(1, 14.0f);
        this.previewSendButton.setTextColor(Theme.getColor("dialogTextBlue2"));
        this.previewSendButton.setGravity(17);
        this.previewSendButton.setBackgroundColor(Theme.getColor("dialogBackground"));
        this.previewSendButton.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
        this.previewSendButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.stickerPreviewLayout.addView((View)this.previewSendButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.previewSendButton.setOnClickListener((View$OnClickListener)new _$$Lambda$StickersAlert$BjajSs__vXEFUr5UZfKatrtPlkU(this));
        final FrameLayout$LayoutParams frameLayout$LayoutParams3 = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
        frameLayout$LayoutParams3.bottomMargin = AndroidUtilities.dp(48.0f);
        (this.previewSendButtonShadow = new View(context)).setBackgroundColor(Theme.getColor("dialogShadowLine"));
        this.stickerPreviewLayout.addView(this.previewSendButtonShadow, (ViewGroup$LayoutParams)frameLayout$LayoutParams3);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        this.updateFields();
        this.updateSendButton();
        this.adapter.notifyDataSetChanged();
    }
    
    private void loadStickerSet() {
        final TLRPC.InputStickerSet inputStickerSet = this.inputStickerSet;
        if (inputStickerSet != null) {
            if (this.stickerSet == null && inputStickerSet.short_name != null) {
                this.stickerSet = DataQuery.getInstance(super.currentAccount).getStickerSetByName(this.inputStickerSet.short_name);
            }
            if (this.stickerSet == null) {
                this.stickerSet = DataQuery.getInstance(super.currentAccount).getStickerSetById(this.inputStickerSet.id);
            }
            if (this.stickerSet == null) {
                final TLRPC.TL_messages_getStickerSet set = new TLRPC.TL_messages_getStickerSet();
                set.stickerset = this.inputStickerSet;
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(set, new _$$Lambda$StickersAlert$dpylIMqKi3ssdFN3dGRn2NGUFco(this));
            }
            else if (this.adapter != null) {
                this.updateSendButton();
                this.updateFields();
                this.adapter.notifyDataSetChanged();
            }
        }
        final TLRPC.TL_messages_stickerSet stickerSet = this.stickerSet;
        if (stickerSet != null) {
            this.showEmoji = (stickerSet.set.masks ^ true);
        }
    }
    
    private void onSubItemClick(final int n) {
        if (this.stickerSet == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
        sb.append("/addstickers/");
        sb.append(this.stickerSet.set.short_name);
        final String string = sb.toString();
        if (n == 1) {
            final ShareAlert shareAlert = new ShareAlert(this.getContext(), null, string, false, string, false);
            final BaseFragment parentFragment = this.parentFragment;
            if (parentFragment != null) {
                parentFragment.showDialog(shareAlert);
            }
            else {
                shareAlert.show();
            }
        }
        else if (n == 2) {
            try {
                AndroidUtilities.addToClipboard(string);
                Toast.makeText(ApplicationLoader.applicationContext, (CharSequence)LocaleController.getString("LinkCopied", 2131559751), 0).show();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    private void runShadowAnimation(final int n, final boolean b) {
        if (this.stickerSetCovereds != null) {
            return;
        }
        if ((b && this.shadow[n].getTag() != null) || (!b && this.shadow[n].getTag() == null)) {
            final View view = this.shadow[n];
            Object value;
            if (b) {
                value = null;
            }
            else {
                value = 1;
            }
            view.setTag(value);
            if (b) {
                this.shadow[n].setVisibility(0);
            }
            final AnimatorSet[] shadowAnimation = this.shadowAnimation;
            if (shadowAnimation[n] != null) {
                shadowAnimation[n].cancel();
            }
            this.shadowAnimation[n] = new AnimatorSet();
            final AnimatorSet set = this.shadowAnimation[n];
            final View view2 = this.shadow[n];
            final Property alpha = View.ALPHA;
            float n2;
            if (b) {
                n2 = 1.0f;
            }
            else {
                n2 = 0.0f;
            }
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)view2, alpha, new float[] { n2 }) });
            this.shadowAnimation[n].setDuration(150L);
            this.shadowAnimation[n].addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (StickersAlert.this.shadowAnimation[n] != null && StickersAlert.this.shadowAnimation[n].equals(obj)) {
                        StickersAlert.this.shadowAnimation[n] = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (StickersAlert.this.shadowAnimation[n] != null && StickersAlert.this.shadowAnimation[n].equals(obj)) {
                        if (!b) {
                            StickersAlert.this.shadow[n].setVisibility(4);
                        }
                        StickersAlert.this.shadowAnimation[n] = null;
                    }
                }
            });
            this.shadowAnimation[n].start();
        }
    }
    
    private void setButton(final View$OnClickListener onClickListener, final String s, final int textColor) {
        this.pickerBottomLayout.setTextColor(textColor);
        this.pickerBottomLayout.setText((CharSequence)s.toUpperCase());
        this.pickerBottomLayout.setOnClickListener(onClickListener);
    }
    
    private void updateFields() {
        if (this.titleTextView == null) {
            return;
        }
        if (this.stickerSet != null) {
            Object title = null;
            Label_0258: {
                Exception ex = null;
                Object o2;
                try {
                    if (this.urlPattern == null) {
                        this.urlPattern = Pattern.compile("@[a-zA-Z\\d_]{1,32}");
                    }
                    final Matcher matcher = this.urlPattern.matcher(this.stickerSet.set.title);
                    Object o = null;
                    while (true) {
                        Object movementMethod = o;
                        title = o;
                        try {
                            if (matcher.find()) {
                                SpannableStringBuilder spannableStringBuilder;
                                if ((spannableStringBuilder = (SpannableStringBuilder)o) == null) {
                                    movementMethod = o;
                                    spannableStringBuilder = new(android.text.SpannableStringBuilder.class);
                                    movementMethod = o;
                                    new SpannableStringBuilder((CharSequence)this.stickerSet.set.title);
                                    try {
                                        final TextView titleTextView = this.titleTextView;
                                        movementMethod = new LinkMovementMethodMy();
                                        titleTextView.setMovementMethod((MovementMethod)movementMethod);
                                    }
                                    catch (Exception ex2) {
                                        movementMethod = spannableStringBuilder;
                                        ex = ex2;
                                        o2 = movementMethod;
                                        break;
                                    }
                                }
                                movementMethod = spannableStringBuilder;
                                final int start = matcher.start();
                                movementMethod = spannableStringBuilder;
                                final int end = matcher.end();
                                int n = start;
                                movementMethod = spannableStringBuilder;
                                if (this.stickerSet.set.title.charAt(start) != '@') {
                                    n = start + 1;
                                }
                                movementMethod = spannableStringBuilder;
                                movementMethod = spannableStringBuilder;
                                final URLSpanNoUnderline urlSpanNoUnderline = new URLSpanNoUnderline(this.stickerSet.set.title.subSequence(n + 1, end).toString()) {
                                    @Override
                                    public void onClick(final View view) {
                                        MessagesController.getInstance(StickersAlert.this.currentAccount).openByUserName(this.getURL(), StickersAlert.this.parentFragment, 1);
                                        StickersAlert.this.dismiss();
                                    }
                                };
                                movementMethod = spannableStringBuilder;
                                spannableStringBuilder.setSpan((Object)urlSpanNoUnderline, n, end, 0);
                                o = spannableStringBuilder;
                                continue;
                            }
                            break Label_0258;
                        }
                        catch (Exception ex) {
                            o2 = movementMethod;
                        }
                    }
                }
                catch (Exception ex) {
                    o2 = null;
                }
                FileLog.e(ex);
                title = o2;
            }
            final TextView titleTextView2 = this.titleTextView;
            if (title == null) {
                title = this.stickerSet.set.title;
            }
            titleTextView2.setText((CharSequence)title);
            if (this.stickerSet.set != null && DataQuery.getInstance(super.currentAccount).isStickerPackInstalled(this.stickerSet.set.id)) {
                final TLRPC.TL_messages_stickerSet stickerSet = this.stickerSet;
                String s;
                if (stickerSet.set.masks) {
                    s = LocaleController.formatString("RemoveStickersCount", 2131560554, LocaleController.formatPluralString("MasksCount", stickerSet.documents.size())).toUpperCase();
                }
                else {
                    s = LocaleController.formatString("RemoveStickersCount", 2131560554, LocaleController.formatPluralString("Stickers", stickerSet.documents.size())).toUpperCase();
                }
                if (this.stickerSet.set.official) {
                    this.setButton((View$OnClickListener)new _$$Lambda$StickersAlert$DGFO8T79ri5mrUocraZaq3nn2Bk(this), s, Theme.getColor("dialogTextRed"));
                }
                else {
                    this.setButton((View$OnClickListener)new _$$Lambda$StickersAlert$2Q0RkK5JO2x7zurWeDAdNpHEWcg(this), s, Theme.getColor("dialogTextRed"));
                }
            }
            else {
                final TLRPC.TL_messages_stickerSet stickerSet2 = this.stickerSet;
                String s2;
                if (stickerSet2.set.masks) {
                    s2 = LocaleController.formatString("AddStickersCount", 2131558585, LocaleController.formatPluralString("MasksCount", stickerSet2.documents.size())).toUpperCase();
                }
                else {
                    s2 = LocaleController.formatString("AddStickersCount", 2131558585, LocaleController.formatPluralString("Stickers", stickerSet2.documents.size())).toUpperCase();
                }
                this.setButton((View$OnClickListener)new _$$Lambda$StickersAlert$TDKayuR9rZfu7Rq3IHs_62RtgkQ(this), s2, Theme.getColor("dialogTextBlue2"));
            }
            this.adapter.notifyDataSetChanged();
        }
        else {
            this.setButton((View$OnClickListener)new _$$Lambda$StickersAlert$v5kDf2Uds3Mnng7wUMVzZRK7fiE(this), LocaleController.getString("Close", 2131559117).toUpperCase(), Theme.getColor("dialogTextBlue2"));
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void updateLayout() {
        if (this.gridView.getChildCount() <= 0) {
            final RecyclerListView gridView = this.gridView;
            gridView.setTopGlowOffset(this.scrollOffsetY = gridView.getPaddingTop());
            if (this.stickerSetCovereds == null) {
                this.titleTextView.setTranslationY((float)this.scrollOffsetY);
                this.optionsButton.setTranslationY((float)this.scrollOffsetY);
                this.shadow[0].setTranslationY((float)this.scrollOffsetY);
            }
            super.containerView.invalidate();
            return;
        }
        final View child = this.gridView.getChildAt(0);
        final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.gridView.findContainingViewHolder(child);
        int top = child.getTop();
        if (top >= 0 && holder != null && ((RecyclerView.ViewHolder)holder).getAdapterPosition() == 0) {
            this.runShadowAnimation(0, false);
        }
        else {
            this.runShadowAnimation(0, true);
            top = 0;
        }
        if (this.scrollOffsetY != top) {
            this.gridView.setTopGlowOffset(this.scrollOffsetY = top);
            if (this.stickerSetCovereds == null) {
                this.titleTextView.setTranslationY((float)this.scrollOffsetY);
                this.optionsButton.setTranslationY((float)this.scrollOffsetY);
                this.shadow[0].setTranslationY((float)this.scrollOffsetY);
            }
            super.containerView.invalidate();
        }
    }
    
    private void updateSendButton() {
        final Point displaySize = AndroidUtilities.displaySize;
        final int n = (int)(Math.min(displaySize.x, displaySize.y) / 2 / AndroidUtilities.density);
        if (this.delegate != null) {
            final TLRPC.TL_messages_stickerSet stickerSet = this.stickerSet;
            if (stickerSet == null || !stickerSet.set.masks) {
                this.previewSendButton.setText((CharSequence)LocaleController.getString("SendSticker", 2131560707).toUpperCase());
                final BackupImageView stickerImageView = this.stickerImageView;
                final float n2 = (float)n;
                stickerImageView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(n, n2, 17, 0.0f, 0.0f, 0.0f, 30.0f));
                this.stickerEmojiTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(n, n2, 17, 0.0f, 0.0f, 0.0f, 30.0f));
                this.previewSendButton.setVisibility(0);
                this.previewSendButtonShadow.setVisibility(0);
                return;
            }
        }
        this.previewSendButton.setText((CharSequence)LocaleController.getString("Close", 2131559117).toUpperCase());
        this.stickerImageView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(n, n, 17));
        this.stickerEmojiTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(n, n, 17));
        this.previewSendButton.setVisibility(8);
        this.previewSendButtonShadow.setVisibility(8);
    }
    
    @Override
    protected boolean canDismissWithSwipe() {
        return false;
    }
    
    @Override
    public void didReceivedNotification(int i, int childCount, final Object... array) {
        if (i == NotificationCenter.emojiDidLoad) {
            final RecyclerListView gridView = this.gridView;
            if (gridView != null) {
                for (childCount = gridView.getChildCount(), i = 0; i < childCount; ++i) {
                    this.gridView.getChildAt(i).invalidate();
                }
            }
            if (ContentPreviewViewer.getInstance().isVisible()) {
                ContentPreviewViewer.getInstance().close();
            }
            ContentPreviewViewer.getInstance().reset();
        }
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
        if (this.reqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
    }
    
    public boolean isClearsInputField() {
        return this.clearsInputField;
    }
    
    public void setClearsInputField(final boolean clearsInputField) {
        this.clearsInputField = clearsInputField;
    }
    
    public void setInstallDelegate(final StickersAlertInstallDelegate installDelegate) {
        this.installDelegate = installDelegate;
    }
    
    private class GridAdapter extends SelectionAdapter
    {
        private SparseArray<Object> cache;
        private Context context;
        private SparseArray<TLRPC.StickerSetCovered> positionsToSets;
        private int stickersPerRow;
        private int stickersRowCount;
        private int totalItems;
        
        public GridAdapter(final Context context) {
            this.cache = (SparseArray<Object>)new SparseArray();
            this.positionsToSets = (SparseArray<TLRPC.StickerSetCovered>)new SparseArray();
            this.context = context;
        }
        
        @Override
        public int getItemCount() {
            return this.totalItems;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (StickersAlert.this.stickerSetCovereds == null) {
                return 0;
            }
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
            final ArrayList access$800 = StickersAlert.this.stickerSetCovereds;
            int size = 0;
            if (access$800 != null) {
                int n;
                if ((n = StickersAlert.this.gridView.getMeasuredWidth()) == 0) {
                    n = AndroidUtilities.displaySize.x;
                }
                this.stickersPerRow = n / AndroidUtilities.dp(72.0f);
                StickersAlert.this.layoutManager.setSpanCount(this.stickersPerRow);
                this.cache.clear();
                this.positionsToSets.clear();
                this.totalItems = 0;
                this.stickersRowCount = 0;
                for (int i = 0; i < StickersAlert.this.stickerSetCovereds.size(); ++i) {
                    final TLRPC.StickerSetCovered stickerSetCovered = StickersAlert.this.stickerSetCovereds.get(i);
                    if (!stickerSetCovered.covers.isEmpty() || stickerSetCovered.cover != null) {
                        final double v = this.stickersRowCount;
                        final double ceil = Math.ceil(StickersAlert.this.stickerSetCovereds.size() / (float)this.stickersPerRow);
                        Double.isNaN(v);
                        this.stickersRowCount = (int)(v + ceil);
                        this.positionsToSets.put(this.totalItems, (Object)stickerSetCovered);
                        this.cache.put(this.totalItems++, (Object)i);
                        final int n2 = this.totalItems / this.stickersPerRow;
                        int n4;
                        if (!stickerSetCovered.covers.isEmpty()) {
                            final int n3 = (int)Math.ceil(stickerSetCovered.covers.size() / (float)this.stickersPerRow);
                            int index = 0;
                            while (true) {
                                n4 = n3;
                                if (index >= stickerSetCovered.covers.size()) {
                                    break;
                                }
                                this.cache.put(this.totalItems + index, (Object)stickerSetCovered.covers.get(index));
                                ++index;
                            }
                        }
                        else {
                            this.cache.put(this.totalItems, (Object)stickerSetCovered.cover);
                            n4 = 1;
                        }
                        int n5 = 0;
                        int stickersPerRow;
                        while (true) {
                            stickersPerRow = this.stickersPerRow;
                            if (n5 >= n4 * stickersPerRow) {
                                break;
                            }
                            this.positionsToSets.put(this.totalItems + n5, (Object)stickerSetCovered);
                            ++n5;
                        }
                        this.totalItems += n4 * stickersPerRow;
                    }
                }
            }
            else {
                if (StickersAlert.this.stickerSet != null) {
                    size = StickersAlert.this.stickerSet.documents.size();
                }
                this.totalItems = size;
            }
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
            if (StickersAlert.this.stickerSetCovereds != null) {
                final int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != 0) {
                    if (itemViewType != 1) {
                        if (itemViewType == 2) {
                            ((FeaturedStickerSetInfoCell)viewHolder.itemView).setStickerSet(StickersAlert.this.stickerSetCovereds.get((int)this.cache.get(index)), false);
                        }
                    }
                    else {
                        ((EmptyCell)viewHolder.itemView).setHeight(AndroidUtilities.dp(82.0f));
                    }
                }
                else {
                    ((StickerEmojiCell)viewHolder.itemView).setSticker((TLRPC.Document)this.cache.get(index), this.positionsToSets.get(index), false);
                }
            }
            else {
                ((StickerEmojiCell)viewHolder.itemView).setSticker(StickersAlert.this.stickerSet.documents.get(index), StickersAlert.this.stickerSet, StickersAlert.this.showEmoji);
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
                        o = new FeaturedStickerSetInfoCell(this.context, 8);
                    }
                }
                else {
                    o = new EmptyCell(this.context);
                }
            }
            else {
                o = new StickerEmojiCell(this.context) {
                    public void onMeasure(final int n, final int n2) {
                        super.onMeasure(View$MeasureSpec.makeMeasureSpec(StickersAlert.this.itemSize, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                    }
                };
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private static class LinkMovementMethodMy extends LinkMovementMethod
    {
        public boolean onTouchEvent(final TextView textView, final Spannable spannable, final MotionEvent motionEvent) {
            try {
                final boolean onTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    Selection.removeSelection(spannable);
                }
                return onTouchEvent;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return false;
            }
        }
    }
    
    public interface StickersAlertDelegate
    {
        void onStickerSelected(final TLRPC.Document p0, final Object p1, final boolean p2);
    }
    
    public interface StickersAlertInstallDelegate
    {
        void onStickerSetInstalled();
        
        void onStickerSetUninstalled();
    }
}
