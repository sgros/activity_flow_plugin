// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Canvas;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.view.View$OnKeyListener;
import android.widget.TextView$OnEditorActionListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.widget.TextView;
import android.view.KeyEvent;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import android.graphics.Rect;
import android.widget.FrameLayout;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.Cells.PollEditTextCell;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.messenger.AndroidUtilities;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.RecyclerListView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;

public class PollCreateActivity extends BaseFragment
{
    private static final int MAX_ANSWER_LENGTH = 100;
    private static final int MAX_QUESTION_LENGTH = 255;
    private static final int done_button = 1;
    private int addAnswerRow;
    private int answerHeaderRow;
    private int answerSectionRow;
    private int answerStartRow;
    private String[] answers;
    private int answersCount;
    private PollCreateActivityDelegate delegate;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ContextProgressView progressView;
    private int questionHeaderRow;
    private int questionRow;
    private int questionSectionRow;
    private String questionString;
    private int requestFieldFocusAtPosition;
    private int rowCount;
    
    public PollCreateActivity() {
        this.answers = new String[10];
        this.answersCount = 1;
        this.requestFieldFocusAtPosition = -1;
    }
    
    private void addNewField() {
        ++this.answersCount;
        if (this.answersCount == this.answers.length) {
            this.listAdapter.notifyItemRemoved(this.addAnswerRow);
        }
        this.listAdapter.notifyItemInserted(this.addAnswerRow);
        this.updateRows();
        this.requestFieldFocusAtPosition = this.answerStartRow + this.answersCount - 1;
        this.listAdapter.notifyItemChanged(this.answerSectionRow);
    }
    
    private boolean checkDiscard() {
        boolean b2;
        final boolean b = b2 = TextUtils.isEmpty((CharSequence)this.getFixedString(this.questionString));
        if (b) {
            int i = 0;
            b2 = b;
            while (i < this.answersCount) {
                b2 = TextUtils.isEmpty((CharSequence)this.getFixedString(this.answers[i]));
                if (!b2) {
                    break;
                }
                ++i;
            }
        }
        if (!b2) {
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            builder.setTitle(LocaleController.getString("CancelPollAlertTitle", 2131558899));
            builder.setMessage(LocaleController.getString("CancelPollAlertText", 2131558898));
            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$PollCreateActivity$_DXfIcr2KYyqZPStWFTU7JfUNVo(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            this.showDialog(builder.create());
        }
        return b2;
    }
    
    private void checkDoneButton() {
        final boolean empty = TextUtils.isEmpty((CharSequence)this.getFixedString(this.questionString));
        boolean enabled;
        final boolean b = enabled = false;
        if (!empty) {
            if (this.questionString.length() > 255) {
                enabled = b;
            }
            else {
                int n = 0;
                int n2 = 0;
                int n3;
                while (true) {
                    final String[] answers = this.answers;
                    n3 = n2;
                    if (n >= answers.length) {
                        break;
                    }
                    int n4 = n2;
                    if (!TextUtils.isEmpty((CharSequence)this.getFixedString(answers[n]))) {
                        if (this.answers[n].length() > 100) {
                            n3 = 0;
                            break;
                        }
                        n4 = n2 + 1;
                    }
                    ++n;
                    n2 = n4;
                }
                enabled = (n3 >= 2 || b);
            }
        }
        this.doneItem.setEnabled(enabled);
        final ActionBarMenuItem doneItem = this.doneItem;
        float alpha;
        if (enabled) {
            alpha = 1.0f;
        }
        else {
            alpha = 0.5f;
        }
        doneItem.setAlpha(alpha);
    }
    
    private String getFixedString(String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return s;
        }
        s = AndroidUtilities.getTrimmedString(s).toString();
        String replace;
        while (true) {
            replace = s;
            if (!s.contains("\n\n\n")) {
                break;
            }
            s = s.replace("\n\n\n", "\n\n");
        }
        while (replace.startsWith("\n\n\n")) {
            replace = replace.replace("\n\n\n", "\n\n");
        }
        return replace;
    }
    
    private void setTextLeft(final View view, int n) {
        final boolean b = view instanceof HeaderCell;
        String s = "windowBackgroundWhiteRedText5";
        if (b) {
            final HeaderCell headerCell = (HeaderCell)view;
            if (n == -1) {
                final String questionString = this.questionString;
                if (questionString != null) {
                    n = questionString.length();
                }
                else {
                    n = 0;
                }
                n = 255 - n;
                if (n <= 76.5f) {
                    headerCell.setText2(String.format("%d", n));
                    final SimpleTextView textView2 = headerCell.getTextView2();
                    if (n >= 0) {
                        s = "windowBackgroundWhiteGrayText3";
                    }
                    textView2.setTextColor(Theme.getColor(s));
                    textView2.setTag((Object)s);
                }
                else {
                    headerCell.setText2("");
                }
            }
            else {
                headerCell.setText2("");
            }
        }
        else if (view instanceof PollEditTextCell && n >= 0) {
            final PollEditTextCell pollEditTextCell = (PollEditTextCell)view;
            final String[] answers = this.answers;
            if (answers[n] != null) {
                n = answers[n].length();
            }
            else {
                n = 0;
            }
            n = 100 - n;
            if (n <= 30.0f) {
                pollEditTextCell.setText2(String.format("%d", n));
                final SimpleTextView textView3 = pollEditTextCell.getTextView2();
                if (n >= 0) {
                    s = "windowBackgroundWhiteGrayText3";
                }
                textView3.setTextColor(Theme.getColor(s));
                textView3.setTag((Object)s);
            }
            else {
                pollEditTextCell.setText2("");
            }
        }
    }
    
    private void showEditDoneProgress(final boolean b) {
        final AnimatorSet doneItemAnimation = this.doneItemAnimation;
        if (doneItemAnimation != null) {
            doneItemAnimation.cancel();
        }
        this.doneItemAnimation = new AnimatorSet();
        if (b) {
            this.progressView.setVisibility(0);
            this.doneItem.setEnabled(false);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.ALPHA, new float[] { 1.0f }) });
        }
        else {
            this.doneItem.getImageView().setVisibility(0);
            this.doneItem.setEnabled(true);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_X, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.SCALE_Y, new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.progressView, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), View.ALPHA, new float[] { 1.0f }) });
        }
        this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                if (PollCreateActivity.this.doneItemAnimation != null && PollCreateActivity.this.doneItemAnimation.equals(obj)) {
                    PollCreateActivity.this.doneItemAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                if (PollCreateActivity.this.doneItemAnimation != null && PollCreateActivity.this.doneItemAnimation.equals(obj)) {
                    if (!b) {
                        PollCreateActivity.this.progressView.setVisibility(4);
                    }
                    else {
                        PollCreateActivity.this.doneItem.getImageView().setVisibility(4);
                    }
                }
            }
        });
        this.doneItemAnimation.setDuration(150L);
        this.doneItemAnimation.start();
    }
    
    private void updateRows() {
        this.rowCount = 0;
        this.questionHeaderRow = this.rowCount++;
        this.questionRow = this.rowCount++;
        this.questionSectionRow = this.rowCount++;
        this.answerHeaderRow = this.rowCount++;
        final int answersCount = this.answersCount;
        if (answersCount != 0) {
            final int rowCount = this.rowCount;
            this.answerStartRow = rowCount;
            this.rowCount = rowCount + answersCount;
        }
        else {
            this.answerStartRow = -1;
        }
        if (this.answersCount != this.answers.length) {
            this.addAnswerRow = this.rowCount++;
        }
        else {
            this.addAnswerRow = -1;
        }
        this.answerSectionRow = this.rowCount++;
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setTitle(LocaleController.getString("NewPoll", 2131559908));
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int i) {
                if (i == -1) {
                    if (PollCreateActivity.this.checkDiscard()) {
                        PollCreateActivity.this.finishFragment();
                    }
                }
                else if (i == 1) {
                    final TLRPC.TL_messageMediaPoll tl_messageMediaPoll = new TLRPC.TL_messageMediaPoll();
                    tl_messageMediaPoll.poll = new TLRPC.TL_poll();
                    final TLRPC.TL_poll poll = tl_messageMediaPoll.poll;
                    final PollCreateActivity this$0 = PollCreateActivity.this;
                    poll.question = this$0.getFixedString(this$0.questionString);
                    PollCreateActivity this$2;
                    TLRPC.TL_pollAnswer e;
                    PollCreateActivity this$3;
                    for (i = 0; i < PollCreateActivity.this.answers.length; ++i) {
                        this$2 = PollCreateActivity.this;
                        if (!TextUtils.isEmpty((CharSequence)this$2.getFixedString(this$2.answers[i]))) {
                            e = new TLRPC.TL_pollAnswer();
                            this$3 = PollCreateActivity.this;
                            e.text = this$3.getFixedString(this$3.answers[i]);
                            (e.option = new byte[1])[0] = (byte)(tl_messageMediaPoll.poll.answers.size() + 48);
                            tl_messageMediaPoll.poll.answers.add(e);
                        }
                    }
                    tl_messageMediaPoll.results = new TLRPC.TL_pollResults();
                    PollCreateActivity.this.delegate.sendPoll(tl_messageMediaPoll);
                    PollCreateActivity.this.finishFragment();
                }
            }
        });
        this.doneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131559299));
        (this.progressView = new ContextProgressView(context, 1)).setAlpha(0.0f);
        this.progressView.setScaleX(0.1f);
        this.progressView.setScaleY(0.1f);
        this.progressView.setVisibility(4);
        this.doneItem.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listAdapter = new ListAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context) {
            @Override
            protected void onMeasure(final int n, final int n2) {
                super.onMeasure(n, n2);
            }
            
            @Override
            public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                rect.bottom += AndroidUtilities.dp(60.0f);
                return super.requestChildRectangleOnScreen(view, rect, b);
            }
            
            @Override
            public void requestLayout() {
                super.requestLayout();
            }
        }).setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        new ItemTouchHelper((ItemTouchHelper.Callback)new TouchHelperCallback()).attachToRecyclerView(this.listView);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PollCreateActivity$BSXK_C_StVS1Xs7U8esUPs_UHrg(this));
        this.checkDoneButton();
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { HeaderCell.class, TextSettingsCell.class, PollEditTextCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { HeaderCell.class }, new String[] { "textView2" }, null, null, null, "windowBackgroundWhiteRedText5"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { HeaderCell.class }, new String[] { "textView2" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { PollEditTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[] { PollEditTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[] { PollEditTextCell.class }, new String[] { "deleteImageView" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[] { PollEditTextCell.class }, new String[] { "deleteImageView" }, null, null, null, "stickers_menuSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { PollEditTextCell.class }, new String[] { "textView2" }, null, null, null, "windowBackgroundWhiteRedText5"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { PollEditTextCell.class }, new String[] { "textView2" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteHintText") };
    }
    
    @Override
    public boolean onBackPressed() {
        return this.checkDiscard();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.updateRows();
        return true;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PollCreateActivity$RBrZqW0OoyKbyRn7oclxtwGFtGc(this), 100L);
        }
    }
    
    public void setDelegate(final PollCreateActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return PollCreateActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == PollCreateActivity.this.questionHeaderRow || n == PollCreateActivity.this.answerHeaderRow) {
                return 0;
            }
            if (n == PollCreateActivity.this.questionSectionRow) {
                return 1;
            }
            if (n == PollCreateActivity.this.answerSectionRow) {
                return 2;
            }
            if (n == PollCreateActivity.this.addAnswerRow) {
                return 3;
            }
            if (n == PollCreateActivity.this.questionRow) {
                return 4;
            }
            return 5;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() == PollCreateActivity.this.addAnswerRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 2) {
                    if (itemViewType != 3) {
                        if (itemViewType != 4) {
                            if (itemViewType == 5) {
                                final PollEditTextCell pollEditTextCell = (PollEditTextCell)viewHolder.itemView;
                                pollEditTextCell.setTag((Object)1);
                                pollEditTextCell.setTextAndHint(PollCreateActivity.this.answers[n - PollCreateActivity.this.answerStartRow], LocaleController.getString("OptionHint", 2131560122), true);
                                pollEditTextCell.setTag((Object)null);
                                if (PollCreateActivity.this.requestFieldFocusAtPosition == n) {
                                    final EditTextBoldCursor textView = pollEditTextCell.getTextView();
                                    textView.requestFocus();
                                    AndroidUtilities.showKeyboard((View)textView);
                                    PollCreateActivity.this.requestFieldFocusAtPosition = -1;
                                }
                                final PollCreateActivity this$0 = PollCreateActivity.this;
                                this$0.setTextLeft(viewHolder.itemView, n - this$0.answerStartRow);
                            }
                        }
                        else {
                            final PollEditTextCell pollEditTextCell2 = (PollEditTextCell)viewHolder.itemView;
                            pollEditTextCell2.setTag((Object)1);
                            String access$300;
                            if (PollCreateActivity.this.questionString != null) {
                                access$300 = PollCreateActivity.this.questionString;
                            }
                            else {
                                access$300 = "";
                            }
                            pollEditTextCell2.setTextAndHint(access$300, LocaleController.getString("QuestionHint", 2131560522), false);
                            pollEditTextCell2.setTag((Object)null);
                        }
                    }
                    else {
                        final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                        textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                        textSettingsCell.setText(LocaleController.getString("AddAnOption", 2131558559), false);
                    }
                }
                else {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    if (10 - PollCreateActivity.this.answersCount <= 0) {
                        textInfoPrivacyCell.setText(LocaleController.getString("AddAnOptionInfoMax", 2131558561));
                    }
                    else {
                        textInfoPrivacyCell.setText(LocaleController.formatString("AddAnOptionInfo", 2131558560, LocaleController.formatPluralString("Option", 10 - PollCreateActivity.this.answersCount)));
                    }
                }
            }
            else {
                final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                if (n == PollCreateActivity.this.questionHeaderRow) {
                    headerCell.setText(LocaleController.getString("Question", 2131560521));
                }
                else if (n == PollCreateActivity.this.answerHeaderRow) {
                    headerCell.setText(LocaleController.getString("PollOptions", 2131560468));
                }
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
                                o = new PollEditTextCell(this.mContext, new _$$Lambda$PollCreateActivity$ListAdapter$xPCTWTnLONqvpxDTUQukvNxp8wU(this)) {
                                    @Override
                                    protected boolean drawDivider() {
                                        final RecyclerView.ViewHolder containingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder((View)this);
                                        if (containingViewHolder != null) {
                                            final int adapterPosition = containingViewHolder.getAdapterPosition();
                                            if (PollCreateActivity.this.answersCount == 10 && adapterPosition == PollCreateActivity.this.answerStartRow + PollCreateActivity.this.answersCount - 1) {
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                };
                                ((FrameLayout)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                ((PollEditTextCell)o).addTextWatcher((TextWatcher)new TextWatcher() {
                                    public void afterTextChanged(final Editable editable) {
                                        final RecyclerView.ViewHolder containingViewHolder = PollCreateActivity.this.listView.findContainingViewHolder((View)o);
                                        if (containingViewHolder != null) {
                                            final int n = containingViewHolder.getAdapterPosition() - PollCreateActivity.this.answerStartRow;
                                            if (n >= 0) {
                                                if (n < PollCreateActivity.this.answers.length) {
                                                    PollCreateActivity.this.answers[n] = editable.toString();
                                                    PollCreateActivity.this.setTextLeft((View)o, n);
                                                    PollCreateActivity.this.checkDoneButton();
                                                }
                                            }
                                        }
                                    }
                                    
                                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                                    }
                                    
                                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                                    }
                                });
                                ((PollEditTextCell)o).setShowNextButton(true);
                                final EditTextBoldCursor textView = ((PollEditTextCell)o).getTextView();
                                textView.setImeOptions(textView.getImeOptions() | 0x5);
                                textView.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$PollCreateActivity$ListAdapter$fHXG5XRT2mioX4wywMDd12jepYQ(this, (PollEditTextCell)o));
                                textView.setOnKeyListener((View$OnKeyListener)new _$$Lambda$PollCreateActivity$ListAdapter$0Cdu_jo6aZB372n9nZKgtvUqB_g((PollEditTextCell)o));
                            }
                            else {
                                o = new PollEditTextCell(this.mContext, null);
                                ((FrameLayout)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                ((PollEditTextCell)o).addTextWatcher((TextWatcher)new TextWatcher() {
                                    public void afterTextChanged(final Editable editable) {
                                        if (((FrameLayout)o).getTag() != null) {
                                            return;
                                        }
                                        PollCreateActivity.this.questionString = editable.toString();
                                        final RecyclerView.ViewHolder viewHolderForAdapterPosition = PollCreateActivity.this.listView.findViewHolderForAdapterPosition(PollCreateActivity.this.questionHeaderRow);
                                        if (viewHolderForAdapterPosition != null) {
                                            PollCreateActivity.this.setTextLeft(viewHolderForAdapterPosition.itemView, -1);
                                        }
                                        PollCreateActivity.this.checkDoneButton();
                                    }
                                    
                                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                                    }
                                    
                                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                                    }
                                });
                            }
                        }
                        else {
                            o = new TextSettingsCell(this.mContext);
                            ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                    }
                    else {
                        o = new TextInfoPrivacyCell(this.mContext);
                    }
                }
                else {
                    o = new ShadowSectionCell(this.mContext);
                }
            }
            else {
                o = new HeaderCell(this.mContext, false, 21, 15, true);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 0) {
                final PollCreateActivity this$0 = PollCreateActivity.this;
                final View itemView = viewHolder.itemView;
                int n;
                if (viewHolder.getAdapterPosition() == PollCreateActivity.this.questionHeaderRow) {
                    n = -1;
                }
                else {
                    n = 0;
                }
                this$0.setTextLeft(itemView, n);
            }
        }
        
        public void swapElements(final int n, final int n2) {
            final int n3 = n - PollCreateActivity.this.answerStartRow;
            final int n4 = n2 - PollCreateActivity.this.answerStartRow;
            if (n3 >= 0 && n4 >= 0 && n3 < PollCreateActivity.this.answersCount) {
                if (n4 < PollCreateActivity.this.answersCount) {
                    final String s = PollCreateActivity.this.answers[n3];
                    PollCreateActivity.this.answers[n3] = PollCreateActivity.this.answers[n4];
                    PollCreateActivity.this.answers[n4] = s;
                    this.notifyItemMoved(n, n2);
                }
            }
        }
    }
    
    public interface PollCreateActivityDelegate
    {
        void sendPoll(final TLRPC.TL_messageMediaPoll p0);
    }
    
    public class TouchHelperCallback extends Callback
    {
        @Override
        public void clearView(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
        
        @Override
        public int getMovementFlags(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 5) {
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }
            return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }
        
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
        
        @Override
        public void onChildDraw(final Canvas canvas, final RecyclerView recyclerView, final ViewHolder viewHolder, final float n, final float n2, final int n3, final boolean b) {
            super.onChildDraw(canvas, recyclerView, viewHolder, n, n2, n3, b);
        }
        
        @Override
        public boolean onMove(final RecyclerView recyclerView, final ViewHolder viewHolder, final ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            PollCreateActivity.this.listAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }
        
        @Override
        public void onSelectedChanged(final ViewHolder viewHolder, final int n) {
            if (n != 0) {
                PollCreateActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, n);
        }
        
        @Override
        public void onSwiped(final ViewHolder viewHolder, final int n) {
        }
    }
}
