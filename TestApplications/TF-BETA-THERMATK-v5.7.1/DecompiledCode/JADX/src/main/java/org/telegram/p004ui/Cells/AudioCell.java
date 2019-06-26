package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaController.AudioEntry;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.CheckBox;
import org.telegram.p004ui.Components.CombinedDrawable;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.AudioCell */
public class AudioCell extends FrameLayout {
    private AudioEntry audioEntry;
    private TextView authorTextView;
    private CheckBox checkBox;
    private int currentAccount = UserConfig.selectedAccount;
    private AudioCellDelegate delegate;
    private TextView genreTextView;
    private boolean needDivider;
    private ImageView playButton;
    private TextView timeTextView;
    private TextView titleTextView;

    /* renamed from: org.telegram.ui.Cells.AudioCell$AudioCellDelegate */
    public interface AudioCellDelegate {
        void startedPlayingAudio(MessageObject messageObject);
    }

    public AudioCell(Context context) {
        Context context2 = context;
        super(context);
        this.playButton = new ImageView(context2);
        int i = 5;
        addView(this.playButton, LayoutHelper.createFrame(46, 46.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 13.0f, 13.0f, LocaleController.isRTL ? 13.0f : 0.0f, 0.0f));
        this.playButton.setOnClickListener(new C2329-$$Lambda$AudioCell$4mzKXwu8KpZzCgq5bTdFt-gHiq4(this));
        this.titleTextView = new TextView(context2);
        this.titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.titleTextView.setTextSize(1, 16.0f);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setLines(1);
        this.titleTextView.setMaxLines(1);
        this.titleTextView.setSingleLine(true);
        this.titleTextView.setEllipsize(TruncateAt.END);
        this.titleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.titleTextView, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 50.0f : 72.0f, 7.0f, LocaleController.isRTL ? 72.0f : 50.0f, 0.0f));
        this.genreTextView = new TextView(context2);
        TextView textView = this.genreTextView;
        String str = Theme.key_windowBackgroundWhiteGrayText2;
        textView.setTextColor(Theme.getColor(str));
        this.genreTextView.setTextSize(1, 14.0f);
        this.genreTextView.setLines(1);
        this.genreTextView.setMaxLines(1);
        this.genreTextView.setSingleLine(true);
        this.genreTextView.setEllipsize(TruncateAt.END);
        this.genreTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.genreTextView, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 50.0f : 72.0f, 28.0f, LocaleController.isRTL ? 72.0f : 50.0f, 0.0f));
        this.authorTextView = new TextView(context2);
        this.authorTextView.setTextColor(Theme.getColor(str));
        this.authorTextView.setTextSize(1, 14.0f);
        this.authorTextView.setLines(1);
        this.authorTextView.setMaxLines(1);
        this.authorTextView.setSingleLine(true);
        this.authorTextView.setEllipsize(TruncateAt.END);
        this.authorTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        addView(this.authorTextView, LayoutHelper.createFrame(-1, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 50.0f : 72.0f, 44.0f, LocaleController.isRTL ? 72.0f : 50.0f, 0.0f));
        this.timeTextView = new TextView(context2);
        this.timeTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        this.timeTextView.setTextSize(1, 13.0f);
        this.timeTextView.setLines(1);
        this.timeTextView.setMaxLines(1);
        this.timeTextView.setSingleLine(true);
        this.timeTextView.setEllipsize(TruncateAt.END);
        this.timeTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 48);
        addView(this.timeTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 48, LocaleController.isRTL ? 18.0f : 0.0f, 11.0f, LocaleController.isRTL ? 0.0f : 18.0f, 0.0f));
        this.checkBox = new CheckBox(context2, C1067R.C1065drawable.round_check2);
        this.checkBox.setVisibility(0);
        this.checkBox.setColor(Theme.getColor(Theme.key_musicPicker_checkbox), Theme.getColor(Theme.key_musicPicker_checkboxCheck));
        CheckBox checkBox = this.checkBox;
        if (LocaleController.isRTL) {
            i = 3;
        }
        addView(checkBox, LayoutHelper.createFrame(22, 22.0f, i | 48, LocaleController.isRTL ? 18.0f : 0.0f, 39.0f, LocaleController.isRTL ? 0.0f : 18.0f, 0.0f));
    }

    public /* synthetic */ void lambda$new$0$AudioCell(View view) {
        if (this.audioEntry == null) {
            return;
        }
        if (!MediaController.getInstance().isPlayingMessage(this.audioEntry.messageObject) || MediaController.getInstance().isMessagePaused()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.audioEntry.messageObject);
            if (MediaController.getInstance().setPlaylist(arrayList, this.audioEntry.messageObject)) {
                setPlayDrawable(true);
                AudioCellDelegate audioCellDelegate = this.delegate;
                if (audioCellDelegate != null) {
                    audioCellDelegate.startedPlayingAudio(this.audioEntry.messageObject);
                    return;
                }
                return;
            }
            return;
        }
        MediaController.getInstance().lambda$startAudioAgain$5$MediaController(this.audioEntry.messageObject);
        setPlayDrawable(false);
    }

    private void setPlayDrawable(boolean z) {
        int i;
        String str;
        int dp = AndroidUtilities.m26dp(46.0f);
        String str2 = Theme.key_musicPicker_buttonBackground;
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(dp, Theme.getColor(str2), Theme.getColor(str2));
        Drawable drawable = getResources().getDrawable(z ? C1067R.C1065drawable.audiosend_pause : C1067R.C1065drawable.audiosend_play);
        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_musicPicker_buttonIcon), Mode.MULTIPLY));
        CombinedDrawable combinedDrawable = new CombinedDrawable(createSimpleSelectorCircleDrawable, drawable);
        combinedDrawable.setCustomSize(AndroidUtilities.m26dp(46.0f), AndroidUtilities.m26dp(46.0f));
        this.playButton.setBackgroundDrawable(combinedDrawable);
        ImageView imageView = this.playButton;
        if (z) {
            i = C1067R.string.AccActionPause;
            str = "AccActionPause";
        } else {
            i = C1067R.string.AccActionPlay;
            str = "AccActionPlay";
        }
        imageView.setContentDescription(LocaleController.getString(str, i));
    }

    public ImageView getPlayButton() {
        return this.playButton;
    }

    public TextView getTitleTextView() {
        return this.titleTextView;
    }

    public TextView getGenreTextView() {
        return this.genreTextView;
    }

    public TextView getTimeTextView() {
        return this.timeTextView;
    }

    public TextView getAuthorTextView() {
        return this.authorTextView;
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(72.0f) + this.needDivider, 1073741824));
    }

    public void setAudio(AudioEntry audioEntry, boolean z, boolean z2) {
        this.audioEntry = audioEntry;
        this.titleTextView.setText(this.audioEntry.title);
        this.genreTextView.setText(this.audioEntry.genre);
        this.authorTextView.setText(this.audioEntry.author);
        this.timeTextView.setText(String.format("%d:%02d", new Object[]{Integer.valueOf(this.audioEntry.duration / 60), Integer.valueOf(this.audioEntry.duration % 60)}));
        boolean z3 = MediaController.getInstance().isPlayingMessage(this.audioEntry.messageObject) && !MediaController.getInstance().isMessagePaused();
        setPlayDrawable(z3);
        this.needDivider = z;
        setWillNotDraw(z ^ 1);
        this.checkBox.setChecked(z2, false);
    }

    public void setChecked(boolean z) {
        this.checkBox.setChecked(z, true);
    }

    public void setDelegate(AudioCellDelegate audioCellDelegate) {
        this.delegate = audioCellDelegate;
    }

    public AudioEntry getAudioEntry() {
        return this.audioEntry;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine((float) AndroidUtilities.m26dp(72.0f), (float) (getHeight() - 1), (float) getWidth(), (float) (getHeight() - 1), Theme.dividerPaint);
        }
    }
}
