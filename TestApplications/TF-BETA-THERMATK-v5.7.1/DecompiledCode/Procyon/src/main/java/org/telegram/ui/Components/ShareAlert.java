// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.Utilities;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.SQLite.SQLiteCursor;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.tgnet.AbstractSerializedData;
import android.text.TextUtils;
import java.util.Locale;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.ChatObject;
import android.view.KeyEvent;
import android.widget.TextView$OnEditorActionListener;
import android.view.ViewPropertyAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.OvershootInterpolator;
import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.ui.Cells.ShareDialogCell;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.Collection;
import android.util.Property;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.messenger.FileLog;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import android.view.ViewGroup;
import org.telegram.messenger.ContactsController;
import org.telegram.ui.DialogsActivity;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import org.telegram.ui.ActionBar.BaseFragment;
import android.view.View$OnTouchListener;
import android.view.View$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.MotionEvent;
import android.graphics.Color;
import android.os.Build$VERSION;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.view.View$MeasureSpec;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.text.TextPaint;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.MessageObject;
import java.util.ArrayList;
import android.util.LongSparseArray;
import android.view.View;
import android.graphics.RectF;
import android.widget.TextView;
import android.graphics.Paint;
import androidx.recyclerview.widget.GridLayoutManager;
import android.widget.FrameLayout;
import org.telegram.tgnet.TLRPC;
import android.animation.AnimatorSet;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BottomSheet;

public class ShareAlert extends BottomSheet implements NotificationCenterDelegate
{
    private AnimatorSet animatorSet;
    private EditTextEmoji commentTextView;
    private boolean copyLinkOnEnd;
    private int currentAccount;
    private TLRPC.TL_exportedMessageLink exportedMessageLink;
    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;
    private RecyclerListView gridView;
    private boolean isChannel;
    private GridLayoutManager layoutManager;
    private String linkToCopy;
    private ShareDialogsAdapter listAdapter;
    private boolean loadingLink;
    private Paint paint;
    private TextView pickerBottomLayout;
    private RectF rect;
    private int scrollOffsetY;
    private ShareSearchAdapter searchAdapter;
    private EmptyTextProgressView searchEmptyView;
    private View selectedCountView;
    private LongSparseArray<TLRPC.Dialog> selectedDialogs;
    private ArrayList<MessageObject> sendingMessageObjects;
    private String sendingText;
    private View[] shadow;
    private AnimatorSet[] shadowAnimation;
    private Drawable shadowDrawable;
    private TextPaint textPaint;
    private int topBeforeSwitch;
    private FrameLayout writeButtonContainer;
    
    public ShareAlert(final Context context, final ArrayList<MessageObject> sendingMessageObjects, final String sendingText, final boolean isChannel, final String linkToCopy, final boolean isFullscreen) {
        super(context, true, 1);
        this.shadow = new View[2];
        this.shadowAnimation = new AnimatorSet[2];
        this.selectedDialogs = (LongSparseArray<TLRPC.Dialog>)new LongSparseArray();
        this.rect = new RectF();
        this.paint = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.currentAccount = UserConfig.selectedAccount;
        (this.shadowDrawable = context.getResources().getDrawable(2131165824).mutate()).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogBackground"), PorterDuff$Mode.MULTIPLY));
        super.isFullscreen = isFullscreen;
        this.linkToCopy = linkToCopy;
        this.sendingMessageObjects = sendingMessageObjects;
        this.searchAdapter = new ShareSearchAdapter(context);
        this.isChannel = isChannel;
        this.sendingText = sendingText;
        if (isChannel) {
            this.loadingLink = true;
            final TLRPC.TL_channels_exportMessageLink tl_channels_exportMessageLink = new TLRPC.TL_channels_exportMessageLink();
            tl_channels_exportMessageLink.id = sendingMessageObjects.get(0).getId();
            tl_channels_exportMessageLink.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(sendingMessageObjects.get(0).messageOwner.to_id.channel_id);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_exportMessageLink, new _$$Lambda$ShareAlert$_zt1k1mc1Pf5YI45UjblkHkDCiI(this, context));
        }
        final SizeNotifierFrameLayout containerView = new SizeNotifierFrameLayout(context) {
            private boolean fullHeight;
            private boolean ignoreLayout = false;
            private RectF rect1 = new RectF();
            
            private void onMeasureInternal(final int n, int n2) {
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(n2);
                this.setMeasuredDimension(size, size2);
                final int n3 = size - ShareAlert.this.backgroundPaddingLeft * 2;
                final int keyboardHeight = this.getKeyboardHeight();
                final int dp = AndroidUtilities.dp(20.0f);
                float alpha = 1.0f;
                int n5;
                if (keyboardHeight <= dp) {
                    int measureSpec;
                    if (!AndroidUtilities.isInMultiwindow) {
                        n2 = size2 - ShareAlert.this.commentTextView.getEmojiPadding();
                        measureSpec = View$MeasureSpec.makeMeasureSpec(n2, 1073741824);
                    }
                    else {
                        final int n4 = n2;
                        n2 = size2;
                        measureSpec = n4;
                    }
                    this.ignoreLayout = true;
                    int visibility;
                    if (ShareAlert.this.commentTextView.isPopupShowing()) {
                        visibility = 8;
                    }
                    else {
                        visibility = 0;
                    }
                    if (ShareAlert.this.pickerBottomLayout != null) {
                        ShareAlert.this.pickerBottomLayout.setVisibility(visibility);
                        final View view = ShareAlert.this.shadow[1];
                        float alpha2 = alpha;
                        if (ShareAlert.this.frameLayout2.getVisibility() != 0) {
                            if (visibility == 0) {
                                alpha2 = alpha;
                            }
                            else {
                                alpha2 = 0.0f;
                            }
                        }
                        view.setAlpha(alpha2);
                    }
                    this.ignoreLayout = false;
                    n5 = measureSpec;
                }
                else {
                    this.ignoreLayout = true;
                    ShareAlert.this.commentTextView.hideEmojiView();
                    if (ShareAlert.this.pickerBottomLayout != null) {
                        ShareAlert.this.pickerBottomLayout.setVisibility(8);
                        final View view2 = ShareAlert.this.shadow[1];
                        if (ShareAlert.this.frameLayout2.getVisibility() != 0) {
                            alpha = 0.0f;
                        }
                        view2.setAlpha(alpha);
                    }
                    this.ignoreLayout = false;
                    n5 = n2;
                    n2 = size2;
                }
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    if (child != null) {
                        if (child.getVisibility() != 8) {
                            if (ShareAlert.this.commentTextView != null && ShareAlert.this.commentTextView.isPopupView(child)) {
                                if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(n3, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                                }
                                else if (AndroidUtilities.isTablet()) {
                                    final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(n3, 1073741824);
                                    float n6;
                                    if (AndroidUtilities.isTablet()) {
                                        n6 = 200.0f;
                                    }
                                    else {
                                        n6 = 320.0f;
                                    }
                                    child.measure(measureSpec2, View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(n6), n2 - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                                }
                                else {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(n3, 1073741824), View$MeasureSpec.makeMeasureSpec(n2 - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                                }
                            }
                            else {
                                this.measureChildWithMargins(child, n, 0, n5, 0);
                            }
                        }
                    }
                }
            }
            
            @Override
            protected void onDraw(final Canvas canvas) {
                final int n = ShareAlert.this.scrollOffsetY - ShareAlert.this.backgroundPaddingTop + AndroidUtilities.dp(6.0f);
                final int n2 = ShareAlert.this.scrollOffsetY - ShareAlert.this.backgroundPaddingTop - AndroidUtilities.dp(13.0f);
                final int n3 = this.getMeasuredHeight() + AndroidUtilities.dp(30.0f) + ShareAlert.this.backgroundPaddingTop;
                int n4 = n;
                int n5 = n2;
                int n6 = n3;
                int n7 = 0;
                int n8 = 0;
                float n10 = 0.0f;
                int n12 = 0;
                Label_0306: {
                    float n11 = 0.0f;
                    Label_0293: {
                        if (!ShareAlert.this.isFullscreen) {
                            n4 = n;
                            n5 = n2;
                            n6 = n3;
                            if (Build$VERSION.SDK_INT >= 21) {
                                final int statusBarHeight = AndroidUtilities.statusBarHeight;
                                n7 = n2 + statusBarHeight;
                                n8 = n + statusBarHeight;
                                int n9 = n3 - statusBarHeight;
                                n4 = n8;
                                n5 = n7;
                                n6 = n9;
                                if (this.fullHeight) {
                                    final int access$2500 = ShareAlert.this.backgroundPaddingTop;
                                    final int statusBarHeight2 = AndroidUtilities.statusBarHeight;
                                    if (access$2500 + n7 < statusBarHeight2 * 2) {
                                        final int min = Math.min(statusBarHeight2, statusBarHeight2 * 2 - n7 - ShareAlert.this.backgroundPaddingTop);
                                        n7 -= min;
                                        n9 += min;
                                        n10 = 1.0f - Math.min(1.0f, min * 2 / (float)AndroidUtilities.statusBarHeight);
                                    }
                                    else {
                                        n10 = 1.0f;
                                    }
                                    final int access$2501 = ShareAlert.this.backgroundPaddingTop;
                                    final int statusBarHeight3 = AndroidUtilities.statusBarHeight;
                                    n4 = n8;
                                    n5 = n7;
                                    n6 = n9;
                                    n11 = n10;
                                    if (access$2501 + n7 < statusBarHeight3) {
                                        final int min2 = Math.min(statusBarHeight3, statusBarHeight3 - n7 - ShareAlert.this.backgroundPaddingTop);
                                        n6 = n9;
                                        n12 = min2;
                                        break Label_0306;
                                    }
                                    break Label_0293;
                                }
                            }
                        }
                        n11 = 1.0f;
                    }
                    n12 = 0;
                    n10 = n11;
                    n7 = n5;
                    n8 = n4;
                }
                ShareAlert.this.shadowDrawable.setBounds(0, n7, this.getMeasuredWidth(), n6);
                ShareAlert.this.shadowDrawable.draw(canvas);
                if (n10 != 1.0f) {
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("dialogBackground"));
                    this.rect1.set((float)ShareAlert.this.backgroundPaddingLeft, (float)(ShareAlert.this.backgroundPaddingTop + n7), (float)(this.getMeasuredWidth() - ShareAlert.this.backgroundPaddingLeft), (float)(ShareAlert.this.backgroundPaddingTop + n7 + AndroidUtilities.dp(24.0f)));
                    canvas.drawRoundRect(this.rect1, AndroidUtilities.dp(12.0f) * n10, AndroidUtilities.dp(12.0f) * n10, Theme.dialogs_onlineCirclePaint);
                }
                final int dp = AndroidUtilities.dp(36.0f);
                this.rect1.set((float)((this.getMeasuredWidth() - dp) / 2), (float)n8, (float)((this.getMeasuredWidth() + dp) / 2), (float)(n8 + AndroidUtilities.dp(4.0f)));
                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("key_sheet_scrollUp"));
                canvas.drawRoundRect(this.rect1, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                if (n12 > 0) {
                    final int color = Theme.getColor("dialogBackground");
                    Theme.dialogs_onlineCirclePaint.setColor(Color.argb(255, (int)(Color.red(color) * 0.8f), (int)(Color.green(color) * 0.8f), (int)(Color.blue(color) * 0.8f)));
                    canvas.drawRect((float)ShareAlert.this.backgroundPaddingLeft, (float)(AndroidUtilities.statusBarHeight - n12), (float)(this.getMeasuredWidth() - ShareAlert.this.backgroundPaddingLeft), (float)AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
                }
            }
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && ShareAlert.this.scrollOffsetY != 0 && motionEvent.getY() < ShareAlert.this.scrollOffsetY - AndroidUtilities.dp(30.0f)) {
                    ShareAlert.this.dismiss();
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                final int childCount = this.getChildCount();
                final int keyboardHeight = this.getKeyboardHeight();
                final int dp = AndroidUtilities.dp(20.0f);
                int i = 0;
                int emojiPadding;
                if (keyboardHeight <= dp && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                    emojiPadding = ShareAlert.this.commentTextView.getEmojiPadding();
                }
                else {
                    emojiPadding = 0;
                }
                this.setBottomClip(emojiPadding);
                while (i < childCount) {
                    final View child = this.getChildAt(i);
                    if (child.getVisibility() != 8) {
                        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                        final int measuredWidth = child.getMeasuredWidth();
                        final int measuredHeight = child.getMeasuredHeight();
                        int gravity;
                        if ((gravity = frameLayout$LayoutParams.gravity) == -1) {
                            gravity = 51;
                        }
                        final int n5 = gravity & 0x70;
                        final int n6 = gravity & 0x7 & 0x7;
                        int n7 = 0;
                        Label_0245: {
                            int n8;
                            int n9;
                            if (n6 != 1) {
                                if (n6 != 5) {
                                    n7 = frameLayout$LayoutParams.leftMargin + this.getPaddingLeft();
                                    break Label_0245;
                                }
                                n8 = n3 - n - measuredWidth - frameLayout$LayoutParams.rightMargin - this.getPaddingRight();
                                n9 = ShareAlert.this.backgroundPaddingLeft;
                            }
                            else {
                                n8 = (n3 - n - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                                n9 = frameLayout$LayoutParams.rightMargin;
                            }
                            n7 = n8 - n9;
                        }
                        int topMargin = 0;
                        Label_0347: {
                            int n10;
                            int n11;
                            if (n5 != 16) {
                                if (n5 == 48) {
                                    topMargin = frameLayout$LayoutParams.topMargin + this.getPaddingTop();
                                    break Label_0347;
                                }
                                if (n5 != 80) {
                                    topMargin = frameLayout$LayoutParams.topMargin;
                                    break Label_0347;
                                }
                                n10 = n4 - emojiPadding - n2 - measuredHeight;
                                n11 = frameLayout$LayoutParams.bottomMargin;
                            }
                            else {
                                n10 = (n4 - emojiPadding - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                                n11 = frameLayout$LayoutParams.bottomMargin;
                            }
                            topMargin = n10 - n11;
                        }
                        int n12 = topMargin;
                        if (ShareAlert.this.commentTextView != null) {
                            n12 = topMargin;
                            if (ShareAlert.this.commentTextView.isPopupView(child)) {
                                int measuredHeight2;
                                int n13;
                                if (AndroidUtilities.isTablet()) {
                                    measuredHeight2 = this.getMeasuredHeight();
                                    n13 = child.getMeasuredHeight();
                                }
                                else {
                                    measuredHeight2 = this.getMeasuredHeight() + this.getKeyboardHeight();
                                    n13 = child.getMeasuredHeight();
                                }
                                n12 = measuredHeight2 - n13;
                            }
                        }
                        child.layout(n7, n12, measuredWidth + n7, measuredHeight + n12);
                    }
                    ++i;
                }
                this.notifyHeightChanged();
                ShareAlert.this.updateLayout();
            }
            
            protected void onMeasure(final int n, int sdk_INT) {
                final int size = View$MeasureSpec.getSize(sdk_INT);
                sdk_INT = Build$VERSION.SDK_INT;
                boolean fullHeight = true;
                if (sdk_INT >= 21 && !ShareAlert.this.isFullscreen) {
                    this.ignoreLayout = true;
                    this.setPadding(ShareAlert.this.backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ShareAlert.this.backgroundPaddingLeft, 0);
                    this.ignoreLayout = false;
                }
                final int n2 = size - this.getPaddingTop();
                final int keyboardHeight = this.getKeyboardHeight();
                sdk_INT = n2;
                if (!AndroidUtilities.isInMultiwindow) {
                    sdk_INT = n2;
                    if (keyboardHeight <= AndroidUtilities.dp(20.0f)) {
                        sdk_INT = n2 - ShareAlert.this.commentTextView.getEmojiPadding();
                    }
                }
                final int a = AndroidUtilities.dp(48.0f) + Math.max(3, (int)Math.ceil(Math.max(ShareAlert.this.searchAdapter.getItemCount(), ShareAlert.this.listAdapter.getItemCount()) / 4.0f)) * AndroidUtilities.dp(103.0f) + ShareAlert.this.backgroundPaddingTop;
                if (a < sdk_INT) {
                    sdk_INT = 0;
                }
                else {
                    sdk_INT -= sdk_INT / 5 * 3;
                }
                sdk_INT += AndroidUtilities.dp(8.0f);
                if (ShareAlert.this.gridView.getPaddingTop() != sdk_INT) {
                    this.ignoreLayout = true;
                    ShareAlert.this.gridView.setPadding(0, sdk_INT, 0, AndroidUtilities.dp(48.0f));
                    this.ignoreLayout = false;
                }
                if (a < size) {
                    fullHeight = false;
                }
                this.fullHeight = fullHeight;
                this.onMeasureInternal(n, View$MeasureSpec.makeMeasureSpec(Math.min(a, size), 1073741824));
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return !ShareAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }
            
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        (super.containerView = (ViewGroup)containerView).setWillNotDraw(false);
        final ViewGroup containerView2 = super.containerView;
        final int backgroundPaddingLeft = super.backgroundPaddingLeft;
        containerView2.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
        (this.frameLayout = new FrameLayout(context)).setBackgroundColor(Theme.getColor("dialogBackground"));
        final SearchField searchField = new SearchField(context);
        this.frameLayout.addView((View)searchField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.gridView = new RecyclerListView(context) {
            @Override
            protected boolean allowSelectChildAtPosition(final float n, final float n2) {
                final int access$2000 = ShareAlert.this.scrollOffsetY;
                final int dp = AndroidUtilities.dp(48.0f);
                final int sdk_INT = Build$VERSION.SDK_INT;
                boolean b = false;
                int statusBarHeight;
                if (sdk_INT >= 21) {
                    statusBarHeight = AndroidUtilities.statusBarHeight;
                }
                else {
                    statusBarHeight = 0;
                }
                if (n2 >= access$2000 + dp + statusBarHeight) {
                    b = true;
                }
                return b;
            }
        }).setTag((Object)13);
        this.gridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0f));
        this.gridView.setClipToPadding(false);
        this.gridView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new GridLayoutManager(this.getContext(), 4)));
        this.layoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int n) {
                if (n == 0) {
                    return ShareAlert.this.layoutManager.getSpanCount();
                }
                return 1;
            }
        });
        this.gridView.setHorizontalScrollBarEnabled(false);
        this.gridView.setVerticalScrollBarEnabled(false);
        this.gridView.addItemDecoration((RecyclerView.ItemDecoration)new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
                final RecyclerListView.Holder holder = (RecyclerListView.Holder)recyclerView.getChildViewHolder(view);
                if (holder != null) {
                    final int n = ((RecyclerView.ViewHolder)holder).getAdapterPosition() % 4;
                    final int n2 = 0;
                    int dp;
                    if (n == 0) {
                        dp = 0;
                    }
                    else {
                        dp = AndroidUtilities.dp(4.0f);
                    }
                    rect.left = dp;
                    int dp2;
                    if (n == 3) {
                        dp2 = n2;
                    }
                    else {
                        dp2 = AndroidUtilities.dp(4.0f);
                    }
                    rect.right = dp2;
                }
                else {
                    rect.left = AndroidUtilities.dp(4.0f);
                    rect.right = AndroidUtilities.dp(4.0f);
                }
            }
        });
        super.containerView.addView((View)this.gridView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        this.gridView.setAdapter(this.listAdapter = new ShareDialogsAdapter(context));
        this.gridView.setGlowColor(Theme.getColor("dialogScrollGlow"));
        this.gridView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ShareAlert$W5k8hWsyZg9DfQTgiUPSh2_mvaQ(this, searchField));
        this.gridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                ShareAlert.this.updateLayout();
            }
        });
        (this.searchEmptyView = new EmptyTextProgressView(context)).setShowAtCenter(true);
        this.searchEmptyView.showTextView();
        this.searchEmptyView.setText(LocaleController.getString("NoChats", 2131559918));
        this.gridView.setEmptyView((View)this.searchEmptyView);
        super.containerView.addView((View)this.searchEmptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 52.0f, 0.0f, 0.0f));
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
        frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(58.0f);
        (this.shadow[0] = new View(context)).setBackgroundColor(Theme.getColor("dialogShadowLine"));
        this.shadow[0].setAlpha(0.0f);
        this.shadow[0].setTag((Object)1);
        super.containerView.addView(this.shadow[0], (ViewGroup$LayoutParams)frameLayout$LayoutParams);
        super.containerView.addView((View)this.frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 58, 51));
        final FrameLayout$LayoutParams frameLayout$LayoutParams2 = new FrameLayout$LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
        frameLayout$LayoutParams2.bottomMargin = AndroidUtilities.dp(48.0f);
        (this.shadow[1] = new View(context)).setBackgroundColor(Theme.getColor("dialogShadowLine"));
        super.containerView.addView(this.shadow[1], (ViewGroup$LayoutParams)frameLayout$LayoutParams2);
        if (!this.isChannel && this.linkToCopy == null) {
            this.shadow[1].setAlpha(0.0f);
        }
        else {
            (this.pickerBottomLayout = new TextView(context)).setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("dialogBackground"), Theme.getColor("listSelectorSDK21")));
            this.pickerBottomLayout.setTextColor(Theme.getColor("dialogTextBlue2"));
            this.pickerBottomLayout.setTextSize(1, 14.0f);
            this.pickerBottomLayout.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            this.pickerBottomLayout.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.pickerBottomLayout.setGravity(17);
            this.pickerBottomLayout.setText((CharSequence)LocaleController.getString("CopyLink", 2131559164).toUpperCase());
            this.pickerBottomLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$ShareAlert$Y4YbUqwWCug5T2hNA6sxQRQyLJ0(this));
            super.containerView.addView((View)this.pickerBottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        }
        (this.frameLayout2 = new FrameLayout(context)).setBackgroundColor(Theme.getColor("dialogBackground"));
        this.frameLayout2.setAlpha(0.0f);
        this.frameLayout2.setVisibility(4);
        super.containerView.addView((View)this.frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.frameLayout2.setOnTouchListener((View$OnTouchListener)_$$Lambda$ShareAlert$e30oQsfKYZ5WdxxCC6xZg65lanw.INSTANCE);
        (this.commentTextView = new EditTextEmoji(context, containerView, null, 1)).setHint(LocaleController.getString("ShareComment", 2131560746));
        this.commentTextView.onResume();
        final EditTextBoldCursor editText = this.commentTextView.getEditText();
        editText.setMaxLines(1);
        editText.setSingleLine(true);
        this.frameLayout2.addView((View)this.commentTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 84.0f, 0.0f));
        (this.writeButtonContainer = new FrameLayout(context)).setVisibility(4);
        this.writeButtonContainer.setScaleX(0.2f);
        this.writeButtonContainer.setScaleY(0.2f);
        this.writeButtonContainer.setAlpha(0.0f);
        this.writeButtonContainer.setContentDescription((CharSequence)LocaleController.getString("Send", 2131560685));
        super.containerView.addView((View)this.writeButtonContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 6.0f, 10.0f));
        this.writeButtonContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$ShareAlert$7m2JWNxMvSKoRyVHsLYwsIAMWC0(this));
        final ImageView imageView = new ImageView(context);
        Drawable simpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("dialogFloatingButton"), Theme.getColor("dialogFloatingButtonPressed"));
        if (Build$VERSION.SDK_INT < 21) {
            final Drawable mutate = context.getResources().getDrawable(2131165388).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
            simpleSelectorCircleDrawable = new CombinedDrawable(mutate, simpleSelectorCircleDrawable, 0, 0);
            ((CombinedDrawable)simpleSelectorCircleDrawable).setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
        imageView.setBackgroundDrawable(simpleSelectorCircleDrawable);
        imageView.setImageResource(2131165292);
        imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogFloatingIcon"), PorterDuff$Mode.MULTIPLY));
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        if (Build$VERSION.SDK_INT >= 21) {
            imageView.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @SuppressLint({ "NewApi" })
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        final FrameLayout writeButtonContainer = this.writeButtonContainer;
        int n;
        if (Build$VERSION.SDK_INT >= 21) {
            n = 56;
        }
        else {
            n = 60;
        }
        float n2;
        if (Build$VERSION.SDK_INT >= 21) {
            n2 = 56.0f;
        }
        else {
            n2 = 60.0f;
        }
        float n3;
        if (Build$VERSION.SDK_INT >= 21) {
            n3 = 2.0f;
        }
        else {
            n3 = 0.0f;
        }
        writeButtonContainer.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n, n2, 51, n3, 0.0f, 0.0f, 0.0f));
        this.textPaint.setTextSize((float)AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        (this.selectedCountView = new View(context) {
            protected void onDraw(final Canvas canvas) {
                final String format = String.format("%d", Math.max(1, ShareAlert.this.selectedDialogs.size()));
                final int n = (int)Math.ceil(ShareAlert.this.textPaint.measureText(format));
                final int max = Math.max(AndroidUtilities.dp(16.0f) + n, AndroidUtilities.dp(24.0f));
                final int n2 = this.getMeasuredWidth() / 2;
                final int n3 = this.getMeasuredHeight() / 2;
                ShareAlert.this.textPaint.setColor(Theme.getColor("dialogRoundCheckBoxCheck"));
                ShareAlert.this.paint.setColor(Theme.getColor("dialogBackground"));
                final RectF access$3900 = ShareAlert.this.rect;
                final int n4 = max / 2;
                final int n5 = n2 - n4;
                final float n6 = (float)n5;
                final int n7 = n4 + n2;
                access$3900.set(n6, 0.0f, (float)n7, (float)this.getMeasuredHeight());
                canvas.drawRoundRect(ShareAlert.this.rect, (float)AndroidUtilities.dp(12.0f), (float)AndroidUtilities.dp(12.0f), ShareAlert.this.paint);
                ShareAlert.this.paint.setColor(Theme.getColor("dialogRoundCheckBox"));
                ShareAlert.this.rect.set((float)(n5 + AndroidUtilities.dp(2.0f)), (float)AndroidUtilities.dp(2.0f), (float)(n7 - AndroidUtilities.dp(2.0f)), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(2.0f)));
                canvas.drawRoundRect(ShareAlert.this.rect, (float)AndroidUtilities.dp(10.0f), (float)AndroidUtilities.dp(10.0f), ShareAlert.this.paint);
                canvas.drawText(format, (float)(n2 - n / 2), (float)AndroidUtilities.dp(16.2f), (Paint)ShareAlert.this.textPaint);
            }
        }).setAlpha(0.0f);
        this.selectedCountView.setScaleX(0.2f);
        this.selectedCountView.setScaleY(0.2f);
        super.containerView.addView(this.selectedCountView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 24.0f, 85, 0.0f, 0.0f, -8.0f, 9.0f));
        this.updateSelectedCount(0);
        final boolean[] dialogsLoaded = DialogsActivity.dialogsLoaded;
        final int currentAccount = this.currentAccount;
        if (!dialogsLoaded[currentAccount]) {
            MessagesController.getInstance(currentAccount).loadDialogs(0, 0, 100, true);
            ContactsController.getInstance(this.currentAccount).checkInviteText();
            DialogsActivity.dialogsLoaded[this.currentAccount] = true;
        }
        if (this.listAdapter.dialogs.isEmpty()) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogsNeedReload);
        }
    }
    
    private void copyLink(final Context context) {
        if (this.exportedMessageLink == null && this.linkToCopy == null) {
            return;
        }
        try {
            final ClipboardManager clipboardManager = (ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard");
            String s;
            if (this.linkToCopy != null) {
                s = this.linkToCopy;
            }
            else {
                s = this.exportedMessageLink.link;
            }
            clipboardManager.setPrimaryClip(ClipData.newPlainText((CharSequence)"label", (CharSequence)s));
            if (this.exportedMessageLink != null && this.exportedMessageLink.link.contains("/c/")) {
                Toast.makeText(ApplicationLoader.applicationContext, (CharSequence)LocaleController.getString("LinkCopiedPrivate", 2131559752), 0).show();
            }
            else {
                Toast.makeText(ApplicationLoader.applicationContext, (CharSequence)LocaleController.getString("LinkCopied", 2131559751), 0).show();
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static ShareAlert createShareAlert(final Context context, final MessageObject e, final String s, final boolean b, final String s2, final boolean b2) {
        ArrayList<MessageObject> list2;
        if (e != null) {
            final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
            list.add(e);
            list2 = list;
        }
        else {
            list2 = null;
        }
        return new ShareAlert(context, list2, s, b, s2, b2);
    }
    
    private int getCurrentTop() {
        if (this.gridView.getChildCount() != 0) {
            final RecyclerListView gridView = this.gridView;
            final int n = 0;
            final View child = gridView.getChildAt(0);
            final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.gridView.findContainingViewHolder(child);
            if (holder != null) {
                final int paddingTop = this.gridView.getPaddingTop();
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
    
    private void runShadowAnimation(final int n, final boolean b) {
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
                    if (ShareAlert.this.shadowAnimation[n] != null && ShareAlert.this.shadowAnimation[n].equals(obj)) {
                        ShareAlert.this.shadowAnimation[n] = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (ShareAlert.this.shadowAnimation[n] != null && ShareAlert.this.shadowAnimation[n].equals(obj)) {
                        if (!b) {
                            ShareAlert.this.shadow[n].setVisibility(4);
                        }
                        ShareAlert.this.shadowAnimation[n] = null;
                    }
                }
            });
            this.shadowAnimation[n].start();
        }
    }
    
    private boolean showCommentTextView(final boolean b) {
        if (b == (this.frameLayout2.getTag() != null)) {
            return false;
        }
        final AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        final FrameLayout frameLayout2 = this.frameLayout2;
        Integer value;
        if (b) {
            value = 1;
        }
        else {
            value = null;
        }
        frameLayout2.setTag((Object)value);
        if (this.commentTextView.getEditText().isFocused()) {
            AndroidUtilities.hideKeyboard((View)this.commentTextView.getEditText());
        }
        this.commentTextView.hidePopup(true);
        if (b) {
            this.frameLayout2.setVisibility(0);
            this.writeButtonContainer.setVisibility(0);
        }
        this.animatorSet = new AnimatorSet();
        final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
        final FrameLayout frameLayout3 = this.frameLayout2;
        final Property alpha = View.ALPHA;
        final float n = 0.0f;
        float n2;
        if (b) {
            n2 = 1.0f;
        }
        else {
            n2 = 0.0f;
        }
        list.add(ObjectAnimator.ofFloat((Object)frameLayout3, alpha, new float[] { n2 }));
        final FrameLayout writeButtonContainer = this.writeButtonContainer;
        final Property scale_X = View.SCALE_X;
        final float n3 = 0.2f;
        float n4;
        if (b) {
            n4 = 1.0f;
        }
        else {
            n4 = 0.2f;
        }
        list.add(ObjectAnimator.ofFloat((Object)writeButtonContainer, scale_X, new float[] { n4 }));
        final FrameLayout writeButtonContainer2 = this.writeButtonContainer;
        final Property scale_Y = View.SCALE_Y;
        float n5;
        if (b) {
            n5 = 1.0f;
        }
        else {
            n5 = 0.2f;
        }
        list.add(ObjectAnimator.ofFloat((Object)writeButtonContainer2, scale_Y, new float[] { n5 }));
        final FrameLayout writeButtonContainer3 = this.writeButtonContainer;
        final Property alpha2 = View.ALPHA;
        float n6;
        if (b) {
            n6 = 1.0f;
        }
        else {
            n6 = 0.0f;
        }
        list.add(ObjectAnimator.ofFloat((Object)writeButtonContainer3, alpha2, new float[] { n6 }));
        final View selectedCountView = this.selectedCountView;
        final Property scale_X2 = View.SCALE_X;
        float n7;
        if (b) {
            n7 = 1.0f;
        }
        else {
            n7 = 0.2f;
        }
        list.add(ObjectAnimator.ofFloat((Object)selectedCountView, scale_X2, new float[] { n7 }));
        final View selectedCountView2 = this.selectedCountView;
        final Property scale_Y2 = View.SCALE_Y;
        float n8 = n3;
        if (b) {
            n8 = 1.0f;
        }
        list.add(ObjectAnimator.ofFloat((Object)selectedCountView2, scale_Y2, new float[] { n8 }));
        final View selectedCountView3 = this.selectedCountView;
        final Property alpha3 = View.ALPHA;
        float n9;
        if (b) {
            n9 = 1.0f;
        }
        else {
            n9 = 0.0f;
        }
        list.add(ObjectAnimator.ofFloat((Object)selectedCountView3, alpha3, new float[] { n9 }));
        final TextView pickerBottomLayout = this.pickerBottomLayout;
        if (pickerBottomLayout == null || pickerBottomLayout.getVisibility() != 0) {
            final View view = this.shadow[1];
            final Property alpha4 = View.ALPHA;
            float n10 = n;
            if (b) {
                n10 = 1.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)view, alpha4, new float[] { n10 }));
        }
        this.animatorSet.playTogether((Collection)list);
        this.animatorSet.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        this.animatorSet.setDuration(180L);
        this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                if (animator.equals(ShareAlert.this.animatorSet)) {
                    ShareAlert.this.animatorSet = null;
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(ShareAlert.this.animatorSet)) {
                    if (!b) {
                        ShareAlert.this.frameLayout2.setVisibility(4);
                        ShareAlert.this.writeButtonContainer.setVisibility(4);
                    }
                    ShareAlert.this.animatorSet = null;
                }
            }
        });
        this.animatorSet.start();
        return true;
    }
    
    @SuppressLint({ "NewApi" })
    private void updateLayout() {
        if (this.gridView.getChildCount() <= 0) {
            return;
        }
        final View child = this.gridView.getChildAt(0);
        final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.gridView.findContainingViewHolder(child);
        int scrollOffsetY = child.getTop() - AndroidUtilities.dp(8.0f);
        int n;
        if (scrollOffsetY > 0 && holder != null && ((RecyclerView.ViewHolder)holder).getAdapterPosition() == 0) {
            n = scrollOffsetY;
        }
        else {
            n = 0;
        }
        if (scrollOffsetY >= 0 && holder != null && ((RecyclerView.ViewHolder)holder).getAdapterPosition() == 0) {
            this.runShadowAnimation(0, false);
        }
        else {
            this.runShadowAnimation(0, true);
            scrollOffsetY = n;
        }
        if (this.scrollOffsetY != scrollOffsetY) {
            this.gridView.setTopGlowOffset(this.scrollOffsetY = scrollOffsetY);
            this.frameLayout.setTranslationY((float)this.scrollOffsetY);
            this.searchEmptyView.setTranslationY((float)this.scrollOffsetY);
            super.containerView.invalidate();
        }
    }
    
    @Override
    protected boolean canDismissWithSwipe() {
        return false;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.dialogsNeedReload) {
            final ShareDialogsAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.fetchDialogs();
            }
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
        }
    }
    
    @Override
    public void dismiss() {
        final EditTextEmoji commentTextView = this.commentTextView;
        if (commentTextView != null) {
            AndroidUtilities.hideKeyboard((View)commentTextView.getEditText());
        }
        super.dismiss();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
    }
    
    @Override
    public void dismissInternal() {
        super.dismissInternal();
        final EditTextEmoji commentTextView = this.commentTextView;
        if (commentTextView != null) {
            commentTextView.onDestroy();
        }
    }
    
    public void onBackPressed() {
        final EditTextEmoji commentTextView = this.commentTextView;
        if (commentTextView != null && commentTextView.isPopupShowing()) {
            this.commentTextView.hidePopup(true);
            return;
        }
        super.onBackPressed();
    }
    
    public void updateSelectedCount(final int n) {
        if (this.selectedDialogs.size() == 0) {
            this.selectedCountView.setPivotX(0.0f);
            this.selectedCountView.setPivotY(0.0f);
            this.showCommentTextView(false);
        }
        else {
            this.selectedCountView.invalidate();
            if (n != 0 && !this.showCommentTextView(true)) {
                this.selectedCountView.setPivotX((float)AndroidUtilities.dp(21.0f));
                this.selectedCountView.setPivotY((float)AndroidUtilities.dp(12.0f));
                final AnimatorSet set = new AnimatorSet();
                final View selectedCountView = this.selectedCountView;
                final Property scale_X = View.SCALE_X;
                final float n2 = 1.1f;
                float n3;
                if (n == 1) {
                    n3 = 1.1f;
                }
                else {
                    n3 = 0.9f;
                }
                final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)selectedCountView, scale_X, new float[] { n3, 1.0f });
                final View selectedCountView2 = this.selectedCountView;
                final Property scale_Y = View.SCALE_Y;
                float n4;
                if (n == 1) {
                    n4 = n2;
                }
                else {
                    n4 = 0.9f;
                }
                set.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ObjectAnimator.ofFloat((Object)selectedCountView2, scale_Y, new float[] { n4, 1.0f }) });
                set.setInterpolator((TimeInterpolator)new OvershootInterpolator());
                set.setDuration(180L);
                set.start();
            }
            else {
                this.selectedCountView.setPivotX(0.0f);
                this.selectedCountView.setPivotY(0.0f);
            }
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
        
        public SearchField(final Context context) {
            super(context);
            (this.searchBackground = new View(context)).setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), Theme.getColor("dialogSearchBackground")));
            this.addView(this.searchBackground, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 11.0f, 14.0f, 0.0f));
            (this.searchIconImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.searchIconImageView.setImageResource(2131165834);
            this.searchIconImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogSearchIcon"), PorterDuff$Mode.MULTIPLY));
            this.addView((View)this.searchIconImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 51, 16.0f, 11.0f, 0.0f, 0.0f));
            (this.clearSearchImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.clearSearchImageView.setImageDrawable((Drawable)(this.progressDrawable = new CloseProgressDrawable2()));
            this.progressDrawable.setSide(AndroidUtilities.dp(7.0f));
            this.clearSearchImageView.setScaleX(0.1f);
            this.clearSearchImageView.setScaleY(0.1f);
            this.clearSearchImageView.setAlpha(0.0f);
            this.clearSearchImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogSearchIcon"), PorterDuff$Mode.MULTIPLY));
            this.addView((View)this.clearSearchImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 11.0f, 14.0f, 0.0f));
            this.clearSearchImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$ShareAlert$SearchField$Io_3RrJcNRJnH5Q8y3dN5bJYiIw(this));
            (this.searchEditText = new EditTextBoldCursor(context) {
                public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
                    final MotionEvent obtain = MotionEvent.obtain(motionEvent);
                    obtain.setLocation(obtain.getRawX(), obtain.getRawY() - ShareAlert.this.containerView.getTranslationY());
                    ShareAlert.this.gridView.dispatchTouchEvent(obtain);
                    obtain.recycle();
                    return super.dispatchTouchEvent(motionEvent);
                }
            }).setTextSize(1, 16.0f);
            this.searchEditText.setHintTextColor(Theme.getColor("dialogSearchHint"));
            this.searchEditText.setTextColor(Theme.getColor("dialogSearchText"));
            this.searchEditText.setBackgroundDrawable((Drawable)null);
            this.searchEditText.setPadding(0, 0, 0, 0);
            this.searchEditText.setMaxLines(1);
            this.searchEditText.setLines(1);
            this.searchEditText.setSingleLine(true);
            this.searchEditText.setImeOptions(268435459);
            this.searchEditText.setHint((CharSequence)LocaleController.getString("ShareSendTo", 2131560752));
            this.searchEditText.setCursorColor(Theme.getColor("featuredStickers_addedIcon"));
            this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.searchEditText.setCursorWidth(1.5f);
            this.addView((View)this.searchEditText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 40.0f, 51, 54.0f, 9.0f, 46.0f, 0.0f));
            this.searchEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    final int length = SearchField.this.searchEditText.length();
                    boolean b = true;
                    final boolean b2 = length > 0;
                    final float alpha = SearchField.this.clearSearchImageView.getAlpha();
                    float n = 0.0f;
                    if (alpha == 0.0f) {
                        b = false;
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
                    final String string = SearchField.this.searchEditText.getText().toString();
                    if (string.length() != 0) {
                        if (ShareAlert.this.searchEmptyView != null) {
                            ShareAlert.this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
                        }
                    }
                    else if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.listAdapter) {
                        final int access$600 = ShareAlert.this.getCurrentTop();
                        ShareAlert.this.searchEmptyView.setText(LocaleController.getString("NoChats", 2131559918));
                        ShareAlert.this.searchEmptyView.showTextView();
                        ShareAlert.this.gridView.setAdapter(ShareAlert.this.listAdapter);
                        ShareAlert.this.listAdapter.notifyDataSetChanged();
                        if (access$600 > 0) {
                            ShareAlert.this.layoutManager.scrollToPositionWithOffset(0, -access$600);
                        }
                    }
                    if (ShareAlert.this.searchAdapter != null) {
                        ShareAlert.this.searchAdapter.searchDialogs(string);
                    }
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
            this.searchEditText.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ShareAlert$SearchField$18q3qLqc5Z_Zq5YApOn8ryeRHg4(this));
        }
        
        public void hideKeyboard() {
            AndroidUtilities.hideKeyboard((View)this.searchEditText);
        }
        
        public void requestDisallowInterceptTouchEvent(final boolean b) {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }
    
    private class ShareDialogsAdapter extends SelectionAdapter
    {
        private Context context;
        private int currentCount;
        private ArrayList<TLRPC.Dialog> dialogs;
        private LongSparseArray<TLRPC.Dialog> dialogsMap;
        
        public ShareDialogsAdapter(final Context context) {
            this.dialogs = new ArrayList<TLRPC.Dialog>();
            this.dialogsMap = (LongSparseArray<TLRPC.Dialog>)new LongSparseArray();
            this.context = context;
            this.fetchDialogs();
        }
        
        public void fetchDialogs() {
            this.dialogs.clear();
            this.dialogsMap.clear();
            final int clientUserId = UserConfig.getInstance(ShareAlert.this.currentAccount).clientUserId;
            final boolean empty = MessagesController.getInstance(ShareAlert.this.currentAccount).dialogsForward.isEmpty();
            int i = 0;
            if (!empty) {
                final TLRPC.Dialog e = MessagesController.getInstance(ShareAlert.this.currentAccount).dialogsForward.get(0);
                this.dialogs.add(e);
                this.dialogsMap.put(e.id, (Object)e);
            }
            for (ArrayList<TLRPC.Dialog> allDialogs = MessagesController.getInstance(ShareAlert.this.currentAccount).getAllDialogs(); i < allDialogs.size(); ++i) {
                final TLRPC.Dialog dialog = allDialogs.get(i);
                if (dialog instanceof TLRPC.TL_dialog) {
                    final long id = dialog.id;
                    final int n = (int)id;
                    if (n != clientUserId) {
                        final int n2 = (int)(id >> 32);
                        if (n != 0 && n2 != 1) {
                            if (n > 0) {
                                this.dialogs.add(dialog);
                                this.dialogsMap.put(dialog.id, (Object)dialog);
                            }
                            else {
                                final TLRPC.Chat chat = MessagesController.getInstance(ShareAlert.this.currentAccount).getChat(-n);
                                if (chat != null && !ChatObject.isNotInChat(chat)) {
                                    if (ChatObject.isChannel(chat) && !chat.creator) {
                                        final TLRPC.TL_chatAdminRights admin_rights = chat.admin_rights;
                                        if ((admin_rights == null || !admin_rights.post_messages) && !chat.megagroup) {
                                            continue;
                                        }
                                    }
                                    this.dialogs.add(dialog);
                                    this.dialogsMap.put(dialog.id, (Object)dialog);
                                }
                            }
                        }
                    }
                }
            }
            this.notifyDataSetChanged();
        }
        
        public TLRPC.Dialog getItem(int index) {
            if (--index >= 0 && index < this.dialogs.size()) {
                return this.dialogs.get(index);
            }
            return null;
        }
        
        @Override
        public int getItemCount() {
            int size;
            final int n = size = this.dialogs.size();
            if (n != 0) {
                size = n + 1;
            }
            return size;
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
            return viewHolder.getItemViewType() != 1;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            if (viewHolder.getItemViewType() == 0) {
                final ShareDialogCell shareDialogCell = (ShareDialogCell)viewHolder.itemView;
                final TLRPC.Dialog item = this.getItem(n);
                n = (int)item.id;
                shareDialogCell.setDialog(n, ShareAlert.this.selectedDialogs.indexOfKey(item.id) >= 0, null);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                o = new View(this.context);
                ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
            }
            else {
                o = new ShareDialogCell(this.context);
                ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0f)));
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    public class ShareSearchAdapter extends SelectionAdapter
    {
        private Context context;
        private int lastReqId;
        private int lastSearchId;
        private String lastSearchText;
        private int reqId;
        private ArrayList<DialogSearchResult> searchResult;
        private Runnable searchRunnable;
        
        public ShareSearchAdapter(final Context context) {
            this.searchResult = new ArrayList<DialogSearchResult>();
            this.context = context;
        }
        
        private void searchDialogsInternal(final String s, final int n) {
            MessagesStorage.getInstance(ShareAlert.this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ShareAlert$ShareSearchAdapter$_0pnpfSXHTiImTPZ850FgfY7mkY(this, s, n));
        }
        
        private void updateSearchResults(final ArrayList<DialogSearchResult> list, final int n) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ShareAlert$ShareSearchAdapter$HTelFIImAnZsj4Mdi1ZJ7VKMmtA(this, n, list));
        }
        
        public TLRPC.Dialog getItem(int index) {
            if (--index >= 0 && index < this.searchResult.size()) {
                return this.searchResult.get(index).dialog;
            }
            return null;
        }
        
        @Override
        public int getItemCount() {
            int size;
            final int n = size = this.searchResult.size();
            if (n != 0) {
                size = n + 1;
            }
            return size;
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
            return viewHolder.getItemViewType() != 1;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            if (viewHolder.getItemViewType() == 0) {
                final ShareDialogCell shareDialogCell = (ShareDialogCell)viewHolder.itemView;
                final ArrayList<DialogSearchResult> searchResult = this.searchResult;
                boolean b = true;
                final DialogSearchResult dialogSearchResult = searchResult.get(n - 1);
                n = (int)dialogSearchResult.dialog.id;
                if (ShareAlert.this.selectedDialogs.indexOfKey(dialogSearchResult.dialog.id) < 0) {
                    b = false;
                }
                shareDialogCell.setDialog(n, b, dialogSearchResult.name);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                o = new View(this.context);
                ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0f)));
            }
            else {
                o = new ShareDialogCell(this.context);
                ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0f)));
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        public void searchDialogs(final String lastSearchText) {
            if (lastSearchText != null && lastSearchText.equals(this.lastSearchText)) {
                return;
            }
            this.lastSearchText = lastSearchText;
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (lastSearchText != null && lastSearchText.length() != 0) {
                final int lastSearchId = this.lastSearchId + 1;
                this.lastSearchId = lastSearchId;
                this.searchRunnable = new _$$Lambda$ShareAlert$ShareSearchAdapter$JorHI9Z_U3Y0Qlm94ObAFcE_y04(this, lastSearchText, lastSearchId);
                Utilities.searchQueue.postRunnable(this.searchRunnable, 300L);
            }
            else {
                this.searchResult.clear();
                final ShareAlert this$0 = ShareAlert.this;
                this$0.topBeforeSwitch = this$0.getCurrentTop();
                this.lastSearchId = -1;
                this.notifyDataSetChanged();
            }
        }
        
        private class DialogSearchResult
        {
            public int date;
            public TLRPC.Dialog dialog;
            public CharSequence name;
            public TLObject object;
            
            private DialogSearchResult() {
                this.dialog = new TLRPC.TL_dialog();
            }
        }
    }
}
