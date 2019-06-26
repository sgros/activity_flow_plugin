// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import java.util.Iterator;
import org.telegram.messenger.FileLog;
import android.content.Intent;
import java.util.Collection;
import org.telegram.messenger.MessagesStorage;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.ImageLocation;
import android.os.Vibrator;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.GroupCreateUserCell;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.StateListAnimator;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.os.Build$VERSION;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.widget.ImageView$ScaleType;
import android.view.View$OnClickListener;
import android.graphics.Paint;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import android.view.View$OnTouchListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.MotionEvent;
import org.telegram.ui.ActionBar.ActionBar;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import org.telegram.ui.Components.ContextProgressView;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.FrameLayout;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.RadialProgressView;
import android.view.View;
import org.telegram.ui.Components.BackupImageView;
import android.widget.ImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.animation.AnimatorSet;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class GroupCreateFinalActivity extends BaseFragment implements NotificationCenterDelegate, ImageUpdaterDelegate
{
    private static final int done_button = 1;
    private GroupCreateAdapter adapter;
    private TLRPC.FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private TLRPC.FileLocation avatarBig;
    private AvatarDrawable avatarDrawable;
    private ImageView avatarEditor;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private int chatType;
    private boolean createAfterUpload;
    private GroupCreateFinalActivityDelegate delegate;
    private AnimatorSet doneItemAnimation;
    private boolean donePressed;
    private EditTextEmoji editText;
    private FrameLayout editTextContainer;
    private FrameLayout floatingButtonContainer;
    private ImageView floatingButtonIcon;
    private ImageUpdater imageUpdater;
    private RecyclerView listView;
    private String nameToSet;
    private ContextProgressView progressView;
    private int reqId;
    private ArrayList<Integer> selectedContacts;
    private Drawable shadowDrawable;
    private TLRPC.InputFile uploadedAvatar;
    
    public GroupCreateFinalActivity(final Bundle bundle) {
        super(bundle);
        this.chatType = bundle.getInt("chatType", 0);
        this.avatarDrawable = new AvatarDrawable();
    }
    
    private void showAvatarProgress(final boolean b, final boolean b2) {
        if (this.avatarEditor == null) {
            return;
        }
        final AnimatorSet avatarAnimation = this.avatarAnimation;
        if (avatarAnimation != null) {
            avatarAnimation.cancel();
            this.avatarAnimation = null;
        }
        if (b2) {
            this.avatarAnimation = new AnimatorSet();
            if (b) {
                this.avatarProgressView.setVisibility(0);
                this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarEditor, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 1.0f }) });
            }
            else {
                this.avatarEditor.setVisibility(0);
                this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarEditor, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 0.0f }) });
            }
            this.avatarAnimation.setDuration(180L);
            this.avatarAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator animator) {
                    GroupCreateFinalActivity.this.avatarAnimation = null;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    if (GroupCreateFinalActivity.this.avatarAnimation != null) {
                        if (GroupCreateFinalActivity.this.avatarEditor != null) {
                            if (b) {
                                GroupCreateFinalActivity.this.avatarEditor.setVisibility(4);
                            }
                            else {
                                GroupCreateFinalActivity.this.avatarProgressView.setVisibility(4);
                            }
                            GroupCreateFinalActivity.this.avatarAnimation = null;
                        }
                    }
                }
            });
            this.avatarAnimation.start();
        }
        else if (b) {
            this.avatarEditor.setAlpha(1.0f);
            this.avatarEditor.setVisibility(4);
            this.avatarProgressView.setAlpha(1.0f);
            this.avatarProgressView.setVisibility(0);
        }
        else {
            this.avatarEditor.setAlpha(1.0f);
            this.avatarEditor.setVisibility(0);
            this.avatarProgressView.setAlpha(0.0f);
            this.avatarProgressView.setVisibility(4);
        }
    }
    
    private void showEditDoneProgress(final boolean b) {
        if (this.floatingButtonIcon == null) {
            return;
        }
        final AnimatorSet doneItemAnimation = this.doneItemAnimation;
        if (doneItemAnimation != null) {
            doneItemAnimation.cancel();
        }
        this.doneItemAnimation = new AnimatorSet();
        if (b) {
            this.progressView.setVisibility(0);
            this.floatingButtonContainer.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 1.0f }) });
        }
        else {
            this.floatingButtonIcon.setVisibility(0);
            this.floatingButtonContainer.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "alpha", new float[] { 1.0f }) });
        }
        this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                if (GroupCreateFinalActivity.this.doneItemAnimation != null && GroupCreateFinalActivity.this.doneItemAnimation.equals(obj)) {
                    GroupCreateFinalActivity.this.doneItemAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                if (GroupCreateFinalActivity.this.doneItemAnimation != null && GroupCreateFinalActivity.this.doneItemAnimation.equals(obj)) {
                    if (!b) {
                        GroupCreateFinalActivity.this.progressView.setVisibility(4);
                    }
                    else {
                        GroupCreateFinalActivity.this.floatingButtonIcon.setVisibility(4);
                    }
                }
            }
        });
        this.doneItemAnimation.setDuration(150L);
        this.doneItemAnimation.start();
    }
    
    @Override
    public View createView(final Context context) {
        final EditTextEmoji editText = this.editText;
        if (editText != null) {
            editText.onDestroy();
        }
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("NewGroup", 2131559900));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    GroupCreateFinalActivity.this.finishFragment();
                }
            }
        });
        final SizeNotifierFrameLayout fragmentView = new SizeNotifierFrameLayout(context) {
            private boolean ignoreLayout;
            
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                final int childCount = this.getChildCount();
                final int keyboardHeight = this.getKeyboardHeight();
                final int dp = AndroidUtilities.dp(20.0f);
                int i = 0;
                int emojiPadding;
                if (keyboardHeight <= dp && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                    emojiPadding = GroupCreateFinalActivity.this.editText.getEmojiPadding();
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
                        int leftMargin = 0;
                        Label_0225: {
                            int n7;
                            int n8;
                            if (n6 != 1) {
                                if (n6 != 5) {
                                    leftMargin = frameLayout$LayoutParams.leftMargin;
                                    break Label_0225;
                                }
                                n7 = n3 - measuredWidth;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            else {
                                n7 = (n3 - n - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            leftMargin = n7 - n8;
                        }
                        int topMargin = 0;
                        Label_0327: {
                            int n9;
                            int n10;
                            if (n5 != 16) {
                                if (n5 == 48) {
                                    topMargin = frameLayout$LayoutParams.topMargin + this.getPaddingTop();
                                    break Label_0327;
                                }
                                if (n5 != 80) {
                                    topMargin = frameLayout$LayoutParams.topMargin;
                                    break Label_0327;
                                }
                                n9 = n4 - emojiPadding - n2 - measuredHeight;
                                n10 = frameLayout$LayoutParams.bottomMargin;
                            }
                            else {
                                n9 = (n4 - emojiPadding - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                                n10 = frameLayout$LayoutParams.bottomMargin;
                            }
                            topMargin = n9 - n10;
                        }
                        int n11 = topMargin;
                        if (GroupCreateFinalActivity.this.editText != null) {
                            n11 = topMargin;
                            if (GroupCreateFinalActivity.this.editText.isPopupView(child)) {
                                int measuredHeight2;
                                int n12;
                                if (AndroidUtilities.isTablet()) {
                                    measuredHeight2 = this.getMeasuredHeight();
                                    n12 = child.getMeasuredHeight();
                                }
                                else {
                                    measuredHeight2 = this.getMeasuredHeight() + this.getKeyboardHeight();
                                    n12 = child.getMeasuredHeight();
                                }
                                n11 = measuredHeight2 - n12;
                            }
                        }
                        child.layout(leftMargin, n11, measuredWidth + leftMargin, measuredHeight + n11);
                    }
                    ++i;
                }
                this.notifyHeightChanged();
            }
            
            protected void onMeasure(final int n, final int n2) {
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(n2);
                this.setMeasuredDimension(size, size2);
                final int n3 = size2 - this.getPaddingTop();
                this.measureChildWithMargins((View)GroupCreateFinalActivity.this.actionBar, n, 0, n2, 0);
                final int keyboardHeight = this.getKeyboardHeight();
                final int dp = AndroidUtilities.dp(20.0f);
                int i = 0;
                if (keyboardHeight > dp) {
                    this.ignoreLayout = true;
                    GroupCreateFinalActivity.this.editText.hideEmojiView();
                    this.ignoreLayout = false;
                }
                while (i < this.getChildCount()) {
                    final View child = this.getChildAt(i);
                    if (child != null && child.getVisibility() != 8) {
                        if (child != GroupCreateFinalActivity.this.actionBar) {
                            if (GroupCreateFinalActivity.this.editText != null && GroupCreateFinalActivity.this.editText.isPopupView(child)) {
                                if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                                }
                                else if (AndroidUtilities.isTablet()) {
                                    final int measureSpec = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                                    float n4;
                                    if (AndroidUtilities.isTablet()) {
                                        n4 = 200.0f;
                                    }
                                    else {
                                        n4 = 320.0f;
                                    }
                                    child.measure(measureSpec, View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(n4), n3 - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                                }
                                else {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n3 - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                                }
                            }
                            else {
                                this.measureChildWithMargins(child, n, 0, n2, 0);
                            }
                        }
                    }
                    ++i;
                }
            }
            
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        (super.fragmentView = (View)fragmentView).setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        super.fragmentView.setOnTouchListener((View$OnTouchListener)_$$Lambda$GroupCreateFinalActivity$zr63HmKznA_wKqWBGncMkUB2788.INSTANCE);
        this.shadowDrawable = context.getResources().getDrawable(2131165396).mutate();
        final LinearLayout linearLayout = new LinearLayout(context) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view == GroupCreateFinalActivity.this.listView && GroupCreateFinalActivity.this.shadowDrawable != null) {
                    final int measuredHeight = GroupCreateFinalActivity.this.editTextContainer.getMeasuredHeight();
                    GroupCreateFinalActivity.this.shadowDrawable.setBounds(0, measuredHeight, this.getMeasuredWidth(), GroupCreateFinalActivity.this.shadowDrawable.getIntrinsicHeight() + measuredHeight);
                    GroupCreateFinalActivity.this.shadowDrawable.draw(canvas);
                }
                return drawChild;
            }
        };
        linearLayout.setOrientation(1);
        fragmentView.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        linearLayout.addView((View)(this.editTextContainer = new FrameLayout(context)), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.avatarImage = new BackupImageView(context) {
            public void invalidate() {
                if (GroupCreateFinalActivity.this.avatarOverlay != null) {
                    GroupCreateFinalActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate();
            }
            
            public void invalidate(final int n, final int n2, final int n3, final int n4) {
                if (GroupCreateFinalActivity.this.avatarOverlay != null) {
                    GroupCreateFinalActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate(n, n2, n3, n4);
            }
        }).setRoundRadius(AndroidUtilities.dp(32.0f));
        this.avatarDrawable.setInfo(5, null, null, this.chatType == 1);
        this.avatarImage.setImageDrawable(this.avatarDrawable);
        this.avatarImage.setContentDescription((CharSequence)LocaleController.getString("ChoosePhoto", 2131559091));
        final FrameLayout editTextContainer = this.editTextContainer;
        final BackupImageView avatarImage = this.avatarImage;
        final boolean isRTL = LocaleController.isRTL;
        int n = 3;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 0.0f;
        }
        else {
            n3 = 16.0f;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 16.0f;
        }
        else {
            n4 = 0.0f;
        }
        editTextContainer.addView((View)avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n2 | 0x30, n3, 16.0f, n4, 16.0f));
        final Paint paint = new Paint(1);
        paint.setColor(1426063360);
        this.avatarOverlay = new View(context) {
            protected void onDraw(final Canvas canvas) {
                if (GroupCreateFinalActivity.this.avatarImage != null && GroupCreateFinalActivity.this.avatarProgressView.getVisibility() == 0) {
                    paint.setAlpha((int)(GroupCreateFinalActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f * GroupCreateFinalActivity.this.avatarProgressView.getAlpha()));
                    canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0f), paint);
                }
            }
        };
        final FrameLayout editTextContainer2 = this.editTextContainer;
        final View avatarOverlay = this.avatarOverlay;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 0.0f;
        }
        else {
            n6 = 16.0f;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 16.0f;
        }
        else {
            n7 = 0.0f;
        }
        editTextContainer2.addView(avatarOverlay, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n5 | 0x30, n6, 16.0f, n7, 16.0f));
        this.avatarOverlay.setOnClickListener((View$OnClickListener)new _$$Lambda$GroupCreateFinalActivity$PfZeWUefXyO_B3uVzcUzv8fJmaM(this));
        (this.avatarEditor = new ImageView(context) {
            public void invalidate() {
                super.invalidate();
                GroupCreateFinalActivity.this.avatarOverlay.invalidate();
            }
            
            public void invalidate(final int n, final int n2, final int n3, final int n4) {
                super.invalidate(n, n2, n3, n4);
                GroupCreateFinalActivity.this.avatarOverlay.invalidate();
            }
        }).setScaleType(ImageView$ScaleType.CENTER);
        this.avatarEditor.setImageResource(2131165276);
        this.avatarEditor.setEnabled(false);
        this.avatarEditor.setClickable(false);
        this.avatarEditor.setPadding(AndroidUtilities.dp(2.0f), 0, 0, 0);
        final FrameLayout editTextContainer3 = this.editTextContainer;
        final ImageView avatarEditor = this.avatarEditor;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        float n9;
        if (LocaleController.isRTL) {
            n9 = 0.0f;
        }
        else {
            n9 = 16.0f;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 16.0f;
        }
        else {
            n10 = 0.0f;
        }
        editTextContainer3.addView((View)avatarEditor, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n8 | 0x30, n9, 16.0f, n10, 16.0f));
        (this.avatarProgressView = new RadialProgressView(context) {
            @Override
            public void setAlpha(final float alpha) {
                super.setAlpha(alpha);
                GroupCreateFinalActivity.this.avatarOverlay.invalidate();
            }
        }).setSize(AndroidUtilities.dp(30.0f));
        this.avatarProgressView.setProgressColor(-1);
        final FrameLayout editTextContainer4 = this.editTextContainer;
        final RadialProgressView avatarProgressView = this.avatarProgressView;
        int n11;
        if (LocaleController.isRTL) {
            n11 = 5;
        }
        else {
            n11 = 3;
        }
        float n12;
        if (LocaleController.isRTL) {
            n12 = 0.0f;
        }
        else {
            n12 = 16.0f;
        }
        float n13;
        if (LocaleController.isRTL) {
            n13 = 16.0f;
        }
        else {
            n13 = 0.0f;
        }
        editTextContainer4.addView((View)avatarProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n11 | 0x30, n12, 16.0f, n13, 16.0f));
        this.showAvatarProgress(false, false);
        this.editText = new EditTextEmoji(context, fragmentView, this, 0);
        final EditTextEmoji editText2 = this.editText;
        final int chatType = this.chatType;
        int n14;
        String s;
        if (chatType != 0 && chatType != 4) {
            n14 = 2131559370;
            s = "EnterListName";
        }
        else {
            n14 = 2131559369;
            s = "EnterGroupNamePlaceholder";
        }
        editText2.setHint(LocaleController.getString(s, n14));
        final String nameToSet = this.nameToSet;
        if (nameToSet != null) {
            this.editText.setText(nameToSet);
            this.nameToSet = null;
        }
        this.editText.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(100) });
        final FrameLayout editTextContainer5 = this.editTextContainer;
        final EditTextEmoji editText3 = this.editText;
        float n15;
        if (LocaleController.isRTL) {
            n15 = 5.0f;
        }
        else {
            n15 = 96.0f;
        }
        float n16;
        if (LocaleController.isRTL) {
            n16 = 96.0f;
        }
        else {
            n16 = 5.0f;
        }
        editTextContainer5.addView((View)editText3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 16, n15, 0.0f, n16, 0.0f));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, 1, false);
        (this.listView = new RecyclerListView(context)).setAdapter((RecyclerView.Adapter)(this.adapter = new GroupCreateAdapter(context)));
        this.listView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        final RecyclerView listView = this.listView;
        int verticalScrollbarPosition;
        if (LocaleController.isRTL) {
            verticalScrollbarPosition = 1;
        }
        else {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        final GroupCreateDividerItemDecoration groupCreateDividerItemDecoration = new GroupCreateDividerItemDecoration();
        groupCreateDividerItemDecoration.setSkipRows(2);
        this.listView.addItemDecoration((RecyclerView.ItemDecoration)groupCreateDividerItemDecoration);
        linearLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1));
        this.listView.setOnScrollListener((RecyclerView.OnScrollListener)new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1) {
                    AndroidUtilities.hideKeyboard((View)GroupCreateFinalActivity.this.editText);
                }
            }
        });
        this.floatingButtonContainer = new FrameLayout(context);
        final float n17 = 56.0f;
        Drawable simpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (Build$VERSION.SDK_INT < 21) {
            final Drawable mutate = context.getResources().getDrawable(2131165387).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
            simpleSelectorCircleDrawable = new CombinedDrawable(mutate, simpleSelectorCircleDrawable, 0, 0);
            ((CombinedDrawable)simpleSelectorCircleDrawable).setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
        this.floatingButtonContainer.setBackgroundDrawable(simpleSelectorCircleDrawable);
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.floatingButtonIcon, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
            this.floatingButtonContainer.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @SuppressLint({ "NewApi" })
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        final FrameLayout floatingButtonContainer = this.floatingButtonContainer;
        int n18;
        if (Build$VERSION.SDK_INT >= 21) {
            n18 = 56;
        }
        else {
            n18 = 60;
        }
        float n19;
        if (Build$VERSION.SDK_INT >= 21) {
            n19 = 56.0f;
        }
        else {
            n19 = 60.0f;
        }
        if (!LocaleController.isRTL) {
            n = 5;
        }
        float n20;
        if (LocaleController.isRTL) {
            n20 = 14.0f;
        }
        else {
            n20 = 0.0f;
        }
        float n21;
        if (LocaleController.isRTL) {
            n21 = 0.0f;
        }
        else {
            n21 = 14.0f;
        }
        fragmentView.addView((View)floatingButtonContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n18, n19, n | 0x50, n20, 0.0f, n21, 14.0f));
        this.floatingButtonContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$GroupCreateFinalActivity$75__1xxdeXhVklExcewj9GeLGnQ(this));
        (this.floatingButtonIcon = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.floatingButtonIcon.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff$Mode.MULTIPLY));
        this.floatingButtonIcon.setImageResource(2131165355);
        this.floatingButtonIcon.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        this.floatingButtonContainer.setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
        final FrameLayout floatingButtonContainer2 = this.floatingButtonContainer;
        final ImageView floatingButtonIcon = this.floatingButtonIcon;
        int n22;
        if (Build$VERSION.SDK_INT >= 21) {
            n22 = 56;
        }
        else {
            n22 = 60;
        }
        float n23;
        if (Build$VERSION.SDK_INT >= 21) {
            n23 = n17;
        }
        else {
            n23 = 60.0f;
        }
        floatingButtonContainer2.addView((View)floatingButtonIcon, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n22, n23));
        (this.progressView = new ContextProgressView(context, 1)).setAlpha(0.0f);
        this.progressView.setScaleX(0.1f);
        this.progressView.setScaleY(0.1f);
        this.progressView.setVisibility(4);
        this.floatingButtonContainer.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int i, int n, final Object... array) {
        final int updateInterfaces = NotificationCenter.updateInterfaces;
        n = 0;
        if (i == updateInterfaces) {
            if (this.listView == null) {
                return;
            }
            final int intValue = (int)array[0];
            if ((intValue & 0x2) != 0x0 || (intValue & 0x1) != 0x0 || (intValue & 0x4) != 0x0) {
                int childCount;
                View child;
                for (childCount = this.listView.getChildCount(), i = n; i < childCount; ++i) {
                    child = this.listView.getChildAt(i);
                    if (child instanceof GroupCreateUserCell) {
                        ((GroupCreateUserCell)child).update(intValue);
                    }
                }
            }
        }
        else if (i == NotificationCenter.chatDidFailCreate) {
            this.reqId = 0;
            this.showEditDoneProgress(this.donePressed = false);
            final EditTextEmoji editText = this.editText;
            if (editText != null) {
                editText.setEnabled(true);
            }
            final GroupCreateFinalActivityDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.didFailChatCreation();
            }
        }
        else if (i == NotificationCenter.chatDidCreated) {
            this.reqId = 0;
            i = (int)array[0];
            final GroupCreateFinalActivityDelegate delegate2 = this.delegate;
            if (delegate2 != null) {
                delegate2.didFinishChatCreation(this, i);
            }
            else {
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                final Bundle bundle = new Bundle();
                bundle.putInt("chat_id", i);
                this.presentFragment(new ChatActivity(bundle), true);
            }
            if (this.uploadedAvatar != null) {
                MessagesController.getInstance(super.currentAccount).changeChatAvatar(i, this.uploadedAvatar, this.avatar, this.avatarBig);
            }
        }
    }
    
    @Override
    public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$GroupCreateFinalActivity$NmRkjRZ6_MrsxxvtnDOrMWsjyWE(this, inputFile, photoSize2, photoSize));
    }
    
    @Override
    public String getInitialSearchString() {
        return this.editText.getText().toString();
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8 $$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8 = new _$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "groupcreate_hintText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "groupcreate_cursor"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { GroupCreateUserCell.class }, new String[] { "textView" }, null, null, null, "groupcreate_sectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { GroupCreateUserCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { GroupCreateUserCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { GroupCreateUserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateFinalActivity$CRety3GO9dgZ2xrvwc73LAKNWX8, "avatar_backgroundPink"), new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressInner2"), new ThemeDescription(this.progressView, 0, null, null, null, null, "contextProgressOuter2"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText") };
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        this.imageUpdater.onActivityResult(n, n2, intent);
    }
    
    @Override
    public boolean onBackPressed() {
        final EditTextEmoji editText = this.editText;
        if (editText != null && editText.isPopupShowing()) {
            this.editText.hidePopup(true);
            return false;
        }
        return true;
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidFailCreate);
        this.imageUpdater = new ImageUpdater();
        final ImageUpdater imageUpdater = this.imageUpdater;
        imageUpdater.parentFragment = this;
        imageUpdater.delegate = (ImageUpdater.ImageUpdaterDelegate)this;
        this.selectedContacts = (ArrayList<Integer>)this.getArguments().getIntegerArrayList("result");
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < this.selectedContacts.size(); ++i) {
            final Integer e = this.selectedContacts.get(i);
            if (MessagesController.getInstance(super.currentAccount).getUser(e) == null) {
                list.add(e);
            }
        }
        if (!list.isEmpty()) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final ArrayList<TLRPC.User> list2 = new ArrayList<TLRPC.User>();
            MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$GroupCreateFinalActivity$eKyq7cJMgDwCwGYOq_2k3JE_l_A(this, list2, list, countDownLatch));
            try {
                countDownLatch.await();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (list.size() != list2.size()) {
                return false;
            }
            if (list2.isEmpty()) {
                return false;
            }
            final Iterator<TLRPC.User> iterator = list2.iterator();
            while (iterator.hasNext()) {
                MessagesController.getInstance(super.currentAccount).putUser(iterator.next(), true);
            }
        }
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidFailCreate);
        this.imageUpdater.clear();
        if (this.reqId != 0) {
            ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.reqId, true);
        }
        final EditTextEmoji editText = this.editText;
        if (editText != null) {
            editText.onDestroy();
        }
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final EditTextEmoji editText = this.editText;
        if (editText != null) {
            editText.onPause();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final EditTextEmoji editText = this.editText;
        if (editText != null) {
            editText.onResume();
        }
        final GroupCreateAdapter adapter = this.adapter;
        if (adapter != null) {
            ((RecyclerView.Adapter)adapter).notifyDataSetChanged();
        }
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            this.editText.openKeyboard();
        }
    }
    
    @Override
    public void restoreSelfArgs(final Bundle bundle) {
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.currentPicturePath = bundle.getString("path");
        }
        final String string = bundle.getString("nameTextView");
        if (string != null) {
            final EditTextEmoji editText = this.editText;
            if (editText != null) {
                editText.setText(string);
            }
            else {
                this.nameToSet = string;
            }
        }
    }
    
    @Override
    public void saveSelfArgs(final Bundle bundle) {
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            final String currentPicturePath = imageUpdater.currentPicturePath;
            if (currentPicturePath != null) {
                bundle.putString("path", currentPicturePath);
            }
        }
        final EditTextEmoji editText = this.editText;
        if (editText != null) {
            final String string = editText.getText().toString();
            if (string != null && string.length() != 0) {
                bundle.putString("nameTextView", string);
            }
        }
    }
    
    public void setDelegate(final GroupCreateFinalActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public class GroupCreateAdapter extends SelectionAdapter
    {
        private Context context;
        
        public GroupCreateAdapter(final Context context) {
            this.context = context;
        }
        
        @Override
        public int getItemCount() {
            return GroupCreateFinalActivity.this.selectedContacts.size() + 2;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 0;
            }
            if (n != 1) {
                return 2;
            }
            return 1;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 1) {
                if (itemViewType == 2) {
                    ((GroupCreateUserCell)viewHolder.itemView).setObject(MessagesController.getInstance(GroupCreateFinalActivity.this.currentAccount).getUser(GroupCreateFinalActivity.this.selectedContacts.get(n - 2)), null, null);
                }
            }
            else {
                ((HeaderCell)viewHolder.itemView).setText(LocaleController.formatPluralString("Members", GroupCreateFinalActivity.this.selectedContacts.size()));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    o = new GroupCreateUserCell(this.context, false, 3);
                }
                else {
                    o = new HeaderCell(this.context);
                    ((HeaderCell)o).setHeight(46);
                }
            }
            else {
                o = new ShadowSectionCell(this.context);
                final CombinedDrawable backgroundDrawable = new CombinedDrawable((Drawable)new ColorDrawable(Theme.getColor("windowBackgroundGray")), Theme.getThemedDrawable(this.context, 2131165396, "windowBackgroundGrayShadow"));
                backgroundDrawable.setFullsize(true);
                ((View)o).setBackgroundDrawable((Drawable)backgroundDrawable);
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewRecycled(final ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 2) {
                ((GroupCreateUserCell)viewHolder.itemView).recycle();
            }
        }
    }
    
    public interface GroupCreateFinalActivityDelegate
    {
        void didFailChatCreation();
        
        void didFinishChatCreation(final GroupCreateFinalActivity p0, final int p1);
        
        void didStartChatCreation();
    }
}
