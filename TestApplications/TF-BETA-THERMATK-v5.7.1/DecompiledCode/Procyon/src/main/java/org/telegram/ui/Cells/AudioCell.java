// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import org.telegram.messenger.MessageObject;
import java.util.ArrayList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.widget.ImageView;
import org.telegram.ui.Components.CheckBox;
import android.widget.TextView;
import org.telegram.messenger.MediaController;
import android.widget.FrameLayout;

public class AudioCell extends FrameLayout
{
    private MediaController.AudioEntry audioEntry;
    private TextView authorTextView;
    private CheckBox checkBox;
    private int currentAccount;
    private AudioCellDelegate delegate;
    private TextView genreTextView;
    private boolean needDivider;
    private ImageView playButton;
    private TextView timeTextView;
    private TextView titleTextView;
    
    public AudioCell(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.playButton = new ImageView(context);
        final ImageView playButton = this.playButton;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
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
            n3 = 13.0f;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 13.0f;
        }
        else {
            n4 = 0.0f;
        }
        this.addView((View)playButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(46, 46.0f, n2 | 0x30, n3, 13.0f, n4, 0.0f));
        this.playButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AudioCell$4mzKXwu8KpZzCgq5bTdFt_gHiq4(this));
        (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.titleTextView.setTextSize(1, 16.0f);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setLines(1);
        this.titleTextView.setMaxLines(1);
        this.titleTextView.setSingleLine(true);
        this.titleTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView titleTextView = this.titleTextView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        titleTextView.setGravity(n5 | 0x30);
        final TextView titleTextView2 = this.titleTextView;
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 50.0f;
        }
        else {
            n7 = 72.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 72.0f;
        }
        else {
            n8 = 50.0f;
        }
        this.addView((View)titleTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n6 | 0x30, n7, 7.0f, n8, 0.0f));
        (this.genreTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.genreTextView.setTextSize(1, 14.0f);
        this.genreTextView.setLines(1);
        this.genreTextView.setMaxLines(1);
        this.genreTextView.setSingleLine(true);
        this.genreTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView genreTextView = this.genreTextView;
        int n9;
        if (LocaleController.isRTL) {
            n9 = 5;
        }
        else {
            n9 = 3;
        }
        genreTextView.setGravity(n9 | 0x30);
        final TextView genreTextView2 = this.genreTextView;
        int n10;
        if (LocaleController.isRTL) {
            n10 = 5;
        }
        else {
            n10 = 3;
        }
        float n11;
        if (LocaleController.isRTL) {
            n11 = 50.0f;
        }
        else {
            n11 = 72.0f;
        }
        float n12;
        if (LocaleController.isRTL) {
            n12 = 72.0f;
        }
        else {
            n12 = 50.0f;
        }
        this.addView((View)genreTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n10 | 0x30, n11, 28.0f, n12, 0.0f));
        (this.authorTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.authorTextView.setTextSize(1, 14.0f);
        this.authorTextView.setLines(1);
        this.authorTextView.setMaxLines(1);
        this.authorTextView.setSingleLine(true);
        this.authorTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView authorTextView = this.authorTextView;
        int n13;
        if (LocaleController.isRTL) {
            n13 = 5;
        }
        else {
            n13 = 3;
        }
        authorTextView.setGravity(n13 | 0x30);
        final TextView authorTextView2 = this.authorTextView;
        int n14;
        if (LocaleController.isRTL) {
            n14 = 5;
        }
        else {
            n14 = 3;
        }
        float n15;
        if (LocaleController.isRTL) {
            n15 = 50.0f;
        }
        else {
            n15 = 72.0f;
        }
        float n16;
        if (LocaleController.isRTL) {
            n16 = 72.0f;
        }
        else {
            n16 = 50.0f;
        }
        this.addView((View)authorTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n14 | 0x30, n15, 44.0f, n16, 0.0f));
        (this.timeTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.timeTextView.setTextSize(1, 13.0f);
        this.timeTextView.setLines(1);
        this.timeTextView.setMaxLines(1);
        this.timeTextView.setSingleLine(true);
        this.timeTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView timeTextView = this.timeTextView;
        int n17;
        if (LocaleController.isRTL) {
            n17 = 3;
        }
        else {
            n17 = 5;
        }
        timeTextView.setGravity(n17 | 0x30);
        final TextView timeTextView2 = this.timeTextView;
        int n18;
        if (LocaleController.isRTL) {
            n18 = 3;
        }
        else {
            n18 = 5;
        }
        float n19;
        if (LocaleController.isRTL) {
            n19 = 18.0f;
        }
        else {
            n19 = 0.0f;
        }
        float n20;
        if (LocaleController.isRTL) {
            n20 = 0.0f;
        }
        else {
            n20 = 18.0f;
        }
        this.addView((View)timeTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n18 | 0x30, n19, 11.0f, n20, 0.0f));
        (this.checkBox = new CheckBox(context, 2131165802)).setVisibility(0);
        this.checkBox.setColor(Theme.getColor("musicPicker_checkbox"), Theme.getColor("musicPicker_checkboxCheck"));
        final CheckBox checkBox = this.checkBox;
        int n21 = n;
        if (LocaleController.isRTL) {
            n21 = 3;
        }
        float n22;
        if (LocaleController.isRTL) {
            n22 = 18.0f;
        }
        else {
            n22 = 0.0f;
        }
        float n23;
        if (LocaleController.isRTL) {
            n23 = 0.0f;
        }
        else {
            n23 = 18.0f;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, n21 | 0x30, n22, 39.0f, n23, 0.0f));
    }
    
    private void setPlayDrawable(final boolean b) {
        final Drawable simpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(46.0f), Theme.getColor("musicPicker_buttonBackground"), Theme.getColor("musicPicker_buttonBackground"));
        final Resources resources = this.getResources();
        int n;
        if (b) {
            n = 2131165296;
        }
        else {
            n = 2131165297;
        }
        final Drawable drawable = resources.getDrawable(n);
        drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("musicPicker_buttonIcon"), PorterDuff$Mode.MULTIPLY));
        final CombinedDrawable backgroundDrawable = new CombinedDrawable(simpleSelectorCircleDrawable, drawable);
        backgroundDrawable.setCustomSize(AndroidUtilities.dp(46.0f), AndroidUtilities.dp(46.0f));
        this.playButton.setBackgroundDrawable((Drawable)backgroundDrawable);
        final ImageView playButton = this.playButton;
        int n2;
        String s;
        if (b) {
            n2 = 2131558408;
            s = "AccActionPause";
        }
        else {
            n2 = 2131558409;
            s = "AccActionPlay";
        }
        playButton.setContentDescription((CharSequence)LocaleController.getString(s, n2));
    }
    
    public MediaController.AudioEntry getAudioEntry() {
        return this.audioEntry;
    }
    
    public TextView getAuthorTextView() {
        return this.authorTextView;
    }
    
    public CheckBox getCheckBox() {
        return this.checkBox;
    }
    
    public TextView getGenreTextView() {
        return this.genreTextView;
    }
    
    public ImageView getPlayButton() {
        return this.playButton;
    }
    
    public TextView getTimeTextView() {
        return this.timeTextView;
    }
    
    public TextView getTitleTextView() {
        return this.titleTextView;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine((float)AndroidUtilities.dp(72.0f), (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(72.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setAudio(final MediaController.AudioEntry audioEntry, final boolean needDivider, final boolean b) {
        this.audioEntry = audioEntry;
        this.titleTextView.setText((CharSequence)this.audioEntry.title);
        this.genreTextView.setText((CharSequence)this.audioEntry.genre);
        this.authorTextView.setText((CharSequence)this.audioEntry.author);
        this.timeTextView.setText((CharSequence)String.format("%d:%02d", this.audioEntry.duration / 60, this.audioEntry.duration % 60));
        this.setPlayDrawable(MediaController.getInstance().isPlayingMessage(this.audioEntry.messageObject) && !MediaController.getInstance().isMessagePaused());
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
        this.checkBox.setChecked(b, false);
    }
    
    public void setChecked(final boolean b) {
        this.checkBox.setChecked(b, true);
    }
    
    public void setDelegate(final AudioCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface AudioCellDelegate
    {
        void startedPlayingAudio(final MessageObject p0);
    }
}
