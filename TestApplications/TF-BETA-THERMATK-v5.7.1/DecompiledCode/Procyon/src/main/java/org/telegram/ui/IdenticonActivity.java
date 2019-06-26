// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.FileLog;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import org.telegram.messenger.Emoji;
import android.content.res.Configuration;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.messenger.EmojiData;
import org.telegram.messenger.Utilities;
import android.text.SpannableStringBuilder;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.IdenticonDrawable;
import org.telegram.messenger.MessagesController;
import android.text.method.MovementMethod;
import android.graphics.Typeface;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.View$OnTouchListener;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import org.telegram.messenger.ApplicationLoader;
import android.view.WindowManager;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.view.View;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.animation.AnimatorSet;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class IdenticonActivity extends BaseFragment implements NotificationCenterDelegate
{
    private AnimatorSet animatorSet;
    private int chat_id;
    private TextView codeTextView;
    private FrameLayout container;
    private boolean emojiSelected;
    private String emojiText;
    private TextView emojiTextView;
    private AnimatorSet hintAnimatorSet;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout1;
    private TextView textView;
    private int textWidth;
    
    public IdenticonActivity(final Bundle bundle) {
        super(bundle);
    }
    
    private void fixLayout() {
        super.fragmentView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
            public boolean onPreDraw() {
                if (IdenticonActivity.this.fragmentView == null) {
                    return true;
                }
                IdenticonActivity.this.fragmentView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                final int rotation = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
                if (rotation != 3 && rotation != 1) {
                    IdenticonActivity.this.linearLayout.setOrientation(1);
                }
                else {
                    IdenticonActivity.this.linearLayout.setOrientation(0);
                }
                IdenticonActivity.this.fragmentView.setPadding(IdenticonActivity.this.fragmentView.getPaddingLeft(), 0, IdenticonActivity.this.fragmentView.getPaddingRight(), IdenticonActivity.this.fragmentView.getPaddingBottom());
                return true;
            }
        });
    }
    
    private void updateEmojiButton(final boolean b) {
        final AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animatorSet = null;
        }
        float scaleY = 1.0f;
        if (b) {
            this.animatorSet = new AnimatorSet();
            final AnimatorSet animatorSet2 = this.animatorSet;
            final TextView emojiTextView = this.emojiTextView;
            float n;
            if (this.emojiSelected) {
                n = 1.0f;
            }
            else {
                n = 0.0f;
            }
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)emojiTextView, "alpha", new float[] { n });
            final TextView codeTextView = this.codeTextView;
            float n2;
            if (this.emojiSelected) {
                n2 = 0.0f;
            }
            else {
                n2 = 1.0f;
            }
            final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)codeTextView, "alpha", new float[] { n2 });
            final TextView emojiTextView2 = this.emojiTextView;
            float n3;
            if (this.emojiSelected) {
                n3 = 1.0f;
            }
            else {
                n3 = 0.0f;
            }
            final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)emojiTextView2, "scaleX", new float[] { n3 });
            final TextView emojiTextView3 = this.emojiTextView;
            float n4;
            if (this.emojiSelected) {
                n4 = 1.0f;
            }
            else {
                n4 = 0.0f;
            }
            final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)emojiTextView3, "scaleY", new float[] { n4 });
            final TextView codeTextView2 = this.codeTextView;
            float n5;
            if (this.emojiSelected) {
                n5 = 0.0f;
            }
            else {
                n5 = 1.0f;
            }
            final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)codeTextView2, "scaleX", new float[] { n5 });
            final TextView codeTextView3 = this.codeTextView;
            if (this.emojiSelected) {
                scaleY = 0.0f;
            }
            animatorSet2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3, (Animator)ofFloat4, (Animator)ofFloat5, (Animator)ObjectAnimator.ofFloat((Object)codeTextView3, "scaleY", new float[] { scaleY }) });
            this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (animator.equals(IdenticonActivity.this.animatorSet)) {
                        IdenticonActivity.this.animatorSet = null;
                    }
                }
            });
            this.animatorSet.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
            this.animatorSet.setDuration(150L);
            this.animatorSet.start();
        }
        else {
            final TextView emojiTextView4 = this.emojiTextView;
            float alpha;
            if (this.emojiSelected) {
                alpha = 1.0f;
            }
            else {
                alpha = 0.0f;
            }
            emojiTextView4.setAlpha(alpha);
            final TextView codeTextView4 = this.codeTextView;
            float alpha2;
            if (this.emojiSelected) {
                alpha2 = 0.0f;
            }
            else {
                alpha2 = 1.0f;
            }
            codeTextView4.setAlpha(alpha2);
            final TextView emojiTextView5 = this.emojiTextView;
            float scaleX;
            if (this.emojiSelected) {
                scaleX = 1.0f;
            }
            else {
                scaleX = 0.0f;
            }
            emojiTextView5.setScaleX(scaleX);
            final TextView emojiTextView6 = this.emojiTextView;
            float scaleY2;
            if (this.emojiSelected) {
                scaleY2 = 1.0f;
            }
            else {
                scaleY2 = 0.0f;
            }
            emojiTextView6.setScaleY(scaleY2);
            final TextView codeTextView5 = this.codeTextView;
            float scaleX2;
            if (this.emojiSelected) {
                scaleX2 = 0.0f;
            }
            else {
                scaleX2 = 1.0f;
            }
            codeTextView5.setScaleX(scaleX2);
            final TextView codeTextView6 = this.codeTextView;
            if (this.emojiSelected) {
                scaleY = 0.0f;
            }
            codeTextView6.setScaleY(scaleY);
        }
        final TextView emojiTextView7 = this.emojiTextView;
        String tag;
        if (!this.emojiSelected) {
            tag = "chat_emojiPanelIcon";
        }
        else {
            tag = "chat_emojiPanelIconSelected";
        }
        emojiTextView7.setTag((Object)tag);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("EncryptionKey", 2131559360));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    IdenticonActivity.this.finishFragment();
                }
            }
        });
        super.fragmentView = (View)new FrameLayout(context);
        final View fragmentView = super.fragmentView;
        final FrameLayout frameLayout = (FrameLayout)fragmentView;
        fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        super.fragmentView.setOnTouchListener((View$OnTouchListener)_$$Lambda$IdenticonActivity$Yvzzx489TCib4oTluPwFgAoS_54.INSTANCE);
        (this.linearLayout = new LinearLayout(context)).setOrientation(1);
        this.linearLayout.setWeightSum(100.0f);
        frameLayout.addView((View)this.linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        final FrameLayout frameLayout2 = new FrameLayout(context);
        frameLayout2.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(20.0f));
        this.linearLayout.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1, 50.0f));
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView$ScaleType.FIT_XY);
        frameLayout2.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.container = new FrameLayout(context) {
            protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if (IdenticonActivity.this.codeTextView != null) {
                    n = IdenticonActivity.this.codeTextView.getLeft() + IdenticonActivity.this.codeTextView.getMeasuredWidth() / 2 - IdenticonActivity.this.emojiTextView.getMeasuredWidth() / 2;
                    n2 = (IdenticonActivity.this.codeTextView.getMeasuredHeight() - IdenticonActivity.this.emojiTextView.getMeasuredHeight()) / 2 + IdenticonActivity.this.linearLayout1.getTop() - AndroidUtilities.dp(16.0f);
                    IdenticonActivity.this.emojiTextView.layout(n, n2, IdenticonActivity.this.emojiTextView.getMeasuredWidth() + n, IdenticonActivity.this.emojiTextView.getMeasuredHeight() + n2);
                }
            }
        }).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout.addView((View)this.container, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1, 50.0f));
        (this.linearLayout1 = new LinearLayout(context)).setOrientation(1);
        this.linearLayout1.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
        this.container.addView((View)this.linearLayout1, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        (this.codeTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        this.codeTextView.setGravity(17);
        this.codeTextView.setTypeface(Typeface.MONOSPACE);
        this.codeTextView.setTextSize(1, 16.0f);
        this.linearLayout1.addView((View)this.codeTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        this.textView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLinksClickable(true);
        this.textView.setClickable(true);
        this.textView.setGravity(17);
        this.textView.setMovementMethod((MovementMethod)new LinkMovementMethodMy());
        this.linearLayout1.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 1));
        (this.emojiTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        this.emojiTextView.setGravity(17);
        this.emojiTextView.setTextSize(1, 32.0f);
        this.container.addView((View)this.emojiTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f));
        final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(super.currentAccount).getEncryptedChat(this.chat_id);
        if (encryptedChat != null) {
            final IdenticonDrawable imageDrawable = new IdenticonDrawable();
            imageView.setImageDrawable((Drawable)imageDrawable);
            imageDrawable.setEncryptedChat(encryptedChat);
            final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(encryptedChat.user_id);
            final SpannableStringBuilder text = new SpannableStringBuilder();
            final StringBuilder sb = new StringBuilder();
            final byte[] key_hash = encryptedChat.key_hash;
            if (key_hash.length > 16) {
                final String bytesToHex = Utilities.bytesToHex(key_hash);
                for (int i = 0; i < 32; ++i) {
                    if (i != 0) {
                        if (i % 8 == 0) {
                            text.append('\n');
                        }
                        else if (i % 4 == 0) {
                            text.append(' ');
                        }
                    }
                    final int beginIndex = i * 2;
                    text.append((CharSequence)bytesToHex.substring(beginIndex, beginIndex + 2));
                    text.append(' ');
                }
                text.append((CharSequence)"\n");
                for (int j = 0; j < 5; ++j) {
                    final byte[] key_hash2 = encryptedChat.key_hash;
                    final int n = j * 4 + 16;
                    final byte b = key_hash2[n];
                    final byte b2 = key_hash2[n + 1];
                    final byte b3 = key_hash2[n + 2];
                    final byte b4 = key_hash2[n + 3];
                    if (j != 0) {
                        sb.append(" ");
                    }
                    final String[] emojiSecret = EmojiData.emojiSecret;
                    sb.append(emojiSecret[((b4 & 0xFF) | ((b & 0x7F) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8)) % emojiSecret.length]);
                }
                this.emojiText = sb.toString();
            }
            this.codeTextView.setText((CharSequence)text.toString());
            text.clear();
            final String first_name = user.first_name;
            text.append((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("EncryptionKeyDescription", 2131559361, first_name, first_name)));
            final int index = text.toString().indexOf("telegram.org");
            if (index != -1) {
                text.setSpan((Object)new URLSpanReplacement(LocaleController.getString("EncryptionKeyLink", 2131559362)), index, index + 12, 33);
            }
            this.textView.setText((CharSequence)text);
        }
        this.updateEmojiButton(false);
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.emojiDidLoad) {
            final TextView emojiTextView = this.emojiTextView;
            if (emojiTextView != null) {
                emojiTextView.invalidate();
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.container, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.codeTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.textView, ThemeDescription.FLAG_LINKCOLOR, null, null, null, null, "windowBackgroundWhiteLinkText") };
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.chat_id = this.getArguments().getInt("chat_id");
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.fixLayout();
    }
    
    @Override
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && !b2) {
            final String emojiText = this.emojiText;
            if (emojiText != null) {
                final TextView emojiTextView = this.emojiTextView;
                emojiTextView.setText(Emoji.replaceEmoji(emojiText, emojiTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(32.0f), false));
            }
        }
    }
    
    private static class LinkMovementMethodMy extends LinkMovementMethod
    {
        public boolean onTouchEvent(final TextView textView, final Spannable spannable, final MotionEvent motionEvent) {
            try {
                return super.onTouchEvent(textView, spannable, motionEvent);
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return false;
            }
        }
    }
}
