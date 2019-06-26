package org.telegram.p004ui;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.time.SunDate;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.BottomSheet;
import org.telegram.p004ui.ActionBar.BottomSheet.BottomSheetCell;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.Theme.ThemeInfo;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.BrightnessControlCell;
import org.telegram.p004ui.Cells.ChatListCell;
import org.telegram.p004ui.Cells.ChatMessageCell;
import org.telegram.p004ui.Cells.ChatMessageCell.ChatMessageCellDelegate;
import org.telegram.p004ui.Cells.ChatMessageCell.ChatMessageCellDelegate.C2348-CC;
import org.telegram.p004ui.Cells.CheckBoxCell;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.NotificationsCheckCell;
import org.telegram.p004ui.Cells.ShadowSectionCell;
import org.telegram.p004ui.Cells.TextCheckCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Cells.ThemeCell;
import org.telegram.p004ui.Cells.ThemeTypeCell;
import org.telegram.p004ui.Components.EditTextBoldCursor;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RadioButton;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.Components.SeekBarView;
import org.telegram.p004ui.Components.ThemeEditorView;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_pollAnswer;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.ThemeActivity */
public class ThemeActivity extends BaseFragment implements NotificationCenterDelegate {
    public static final int THEME_TYPE_ALL = 2;
    public static final int THEME_TYPE_BASIC = 0;
    public static final int THEME_TYPE_NIGHT = 1;
    private static final int create_theme = 1;
    private int automaticBrightnessInfoRow;
    private int automaticBrightnessRow;
    private int automaticHeaderRow;
    private int backgroundRow;
    private int chatListHeaderRow;
    private int chatListInfoRow;
    private int chatListRow;
    private int contactsReimportRow;
    private int contactsSortRow;
    private int currentType;
    private int customTabsRow;
    private ArrayList<ThemeInfo> darkThemes = new ArrayList();
    private ArrayList<ThemeInfo> defaultThemes = new ArrayList();
    private int directShareRow;
    private int emojiRow;
    private int enableAnimationsRow;
    private GpsLocationListener gpsLocationListener = new GpsLocationListener(this, null);
    boolean hasCustomThemes;
    private RecyclerListView innerListView;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private GpsLocationListener networkLocationListener = new GpsLocationListener(this, null);
    private int newThemeInfoRow;
    private int nightAutomaticRow;
    private int nightDisabledRow;
    private int nightScheduledRow;
    private int nightThemeRow;
    private int nightTypeInfoRow;
    private int preferedHeaderRow;
    private boolean previousByLocation;
    private int previousUpdatedType;
    private int raiseToSpeakRow;
    private int rowCount;
    private int saveToGalleryRow;
    private int scheduleFromRow;
    private int scheduleFromToInfoRow;
    private int scheduleHeaderRow;
    private int scheduleLocationInfoRow;
    private int scheduleLocationRow;
    private int scheduleToRow;
    private int scheduleUpdateLocationRow;
    private int sendByEnterRow;
    private int settings2Row;
    private int settingsRow;
    private int showThemesRows;
    private int stickersRow;
    private int stickersSection2Row;
    private int textSizeHeaderRow;
    private int textSizeRow;
    private int themeEnd2Row;
    private int themeEndRow;
    private int themeHeader2Row;
    private int themeHeaderRow;
    private int themeInfo2Row;
    private int themeInfoRow;
    private int themeListRow;
    private int themeStart2Row;
    private int themeStartRow;
    private boolean updatingLocation;

    /* renamed from: org.telegram.ui.ThemeActivity$GpsLocationListener */
    private class GpsLocationListener implements LocationListener {
        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        private GpsLocationListener() {
        }

        /* synthetic */ GpsLocationListener(ThemeActivity themeActivity, C43361 c43361) {
            this();
        }

        public void onLocationChanged(Location location) {
            if (location != null) {
                ThemeActivity.this.stopLocationUpdate();
                ThemeActivity.this.updateSunTime(location, false);
            }
        }
    }

    /* renamed from: org.telegram.ui.ThemeActivity$InnerThemeView */
    private class InnerThemeView extends FrameLayout {
        private RadioButton button;
        private Drawable inDrawable;
        private boolean isFirst;
        private boolean isLast;
        private Drawable outDrawable;
        private Paint paint = new Paint(1);
        private RectF rect = new RectF();
        private TextPaint textPaint = new TextPaint(1);
        private ThemeInfo themeInfo;

        public InnerThemeView(Context context) {
            super(context);
            setWillNotDraw(false);
            this.inDrawable = context.getResources().getDrawable(C1067R.C1065drawable.minibubble_in).mutate();
            this.outDrawable = context.getResources().getDrawable(C1067R.C1065drawable.minibubble_out).mutate();
            this.textPaint.setTextSize((float) AndroidUtilities.m26dp(13.0f));
            this.button = new RadioButton(context, ThemeActivity.this) {
                public void invalidate() {
                    super.invalidate();
                }
            };
            this.button.setSize(AndroidUtilities.m26dp(20.0f));
            this.button.setColor(1728053247, -1);
            addView(this.button, LayoutHelper.createFrame(22, 22.0f, 51, 27.0f, 75.0f, 0.0f, 0.0f));
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int i, int i2) {
            i2 = 22;
            i = (this.isLast ? 22 : 15) + 76;
            if (!this.isFirst) {
                i2 = 0;
            }
            super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp((float) (i + i2)), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(148.0f), 1073741824));
        }

        public void setTheme(ThemeInfo themeInfo, boolean z, boolean z2) {
            this.themeInfo = themeInfo;
            this.isFirst = z2;
            this.isLast = z;
            LayoutParams layoutParams = (LayoutParams) this.button.getLayoutParams();
            layoutParams.leftMargin = AndroidUtilities.m26dp(this.isFirst ? 49.0f : 27.0f);
            this.button.setLayoutParams(layoutParams);
            this.inDrawable.setColorFilter(new PorterDuffColorFilter(themeInfo.previewInColor, Mode.MULTIPLY));
            this.outDrawable.setColorFilter(new PorterDuffColorFilter(themeInfo.previewOutColor, Mode.MULTIPLY));
        }

        /* Access modifiers changed, original: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.button.setChecked(this.themeInfo == Theme.getCurrentTheme(), false);
        }

        public void updateCurrentThemeCheck() {
            this.button.setChecked(this.themeInfo == Theme.getCurrentTheme(), true);
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            int blue;
            this.paint.setColor(this.themeInfo.previewBackgroundColor);
            int dp = this.isFirst ? AndroidUtilities.m26dp(22.0f) : 0;
            this.rect.set((float) dp, (float) AndroidUtilities.m26dp(11.0f), (float) (AndroidUtilities.m26dp(76.0f) + dp), (float) AndroidUtilities.m26dp(108.0f));
            canvas.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(6.0f), (float) AndroidUtilities.m26dp(6.0f), this.paint);
            if ("Arctic Blue".equals(this.themeInfo.name)) {
                int red = Color.red(-5196358);
                int green = Color.green(-5196358);
                blue = Color.blue(-5196358);
                this.button.setColor(-5000269, -13129232);
                Theme.chat_instantViewRectPaint.setColor(Color.argb(43, red, green, blue));
                canvas.drawRoundRect(this.rect, (float) AndroidUtilities.m26dp(6.0f), (float) AndroidUtilities.m26dp(6.0f), Theme.chat_instantViewRectPaint);
            } else {
                this.button.setColor(1728053247, -1);
            }
            this.inDrawable.setBounds(AndroidUtilities.m26dp(6.0f) + dp, AndroidUtilities.m26dp(22.0f), AndroidUtilities.m26dp(49.0f) + dp, AndroidUtilities.m26dp(36.0f));
            this.inDrawable.draw(canvas);
            this.outDrawable.setBounds(AndroidUtilities.m26dp(27.0f) + dp, AndroidUtilities.m26dp(41.0f), AndroidUtilities.m26dp(70.0f) + dp, AndroidUtilities.m26dp(55.0f));
            this.outDrawable.draw(canvas);
            String charSequence = TextUtils.ellipsize(this.themeInfo.getName(), this.textPaint, (float) (getMeasuredWidth() - AndroidUtilities.m26dp(10.0f)), TruncateAt.END).toString();
            blue = (int) Math.ceil((double) this.textPaint.measureText(charSequence));
            this.textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            canvas.drawText(charSequence, (float) (dp + ((AndroidUtilities.m26dp(76.0f) - blue) / 2)), (float) AndroidUtilities.m26dp(131.0f), this.textPaint);
        }
    }

    /* renamed from: org.telegram.ui.ThemeActivity$SizeChooseViewDelegate */
    public interface SizeChooseViewDelegate {
        void onSizeChanged();
    }

    /* renamed from: org.telegram.ui.ThemeActivity$TextSizeCell */
    private class TextSizeCell extends FrameLayout {
        private ChatMessageCell[] cells = new ChatMessageCell[2];
        private int endFontSize = 30;
        private int lastWidth;
        private LinearLayout messagesContainer;
        private Drawable shadowDrawable;
        private SeekBarView sizeBar;
        private int startFontSize = 12;
        private TextPaint textPaint;

        public TextSizeCell(Context context) {
            super(context);
            setWillNotDraw(false);
            this.textPaint = new TextPaint(1);
            this.textPaint.setTextSize((float) AndroidUtilities.m26dp(16.0f));
            this.shadowDrawable = Theme.getThemedDrawable(context, (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
            this.sizeBar = new SeekBarView(context);
            this.sizeBar.setReportChanges(true);
            this.sizeBar.setDelegate(new C3875-$$Lambda$ThemeActivity$TextSizeCell$Ci0_0LdqTC4U6xi9evAA0pUhylM(this));
            addView(this.sizeBar, LayoutHelper.createFrame(-1, 38.0f, 51, 9.0f, 5.0f, 43.0f, 0.0f));
            this.messagesContainer = new LinearLayout(context, ThemeActivity.this) {
                private Drawable backgroundDrawable;
                private Drawable oldBackgroundDrawable;

                /* Access modifiers changed, original: protected */
                public void dispatchSetPressed(boolean z) {
                }

                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    return false;
                }

                public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                    return false;
                }

                public boolean onTouchEvent(MotionEvent motionEvent) {
                    return false;
                }

                /* Access modifiers changed, original: protected */
                public void onDraw(Canvas canvas) {
                    Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
                    if (!(cachedWallpaperNonBlocking == this.backgroundDrawable || cachedWallpaperNonBlocking == null)) {
                        if (Theme.isAnimatingColor()) {
                            this.oldBackgroundDrawable = this.backgroundDrawable;
                        }
                        this.backgroundDrawable = cachedWallpaperNonBlocking;
                    }
                    float themeAnimationValue = ThemeActivity.this.parentLayout.getThemeAnimationValue();
                    int i = 0;
                    while (i < 2) {
                        Drawable drawable = i == 0 ? this.oldBackgroundDrawable : this.backgroundDrawable;
                        if (drawable != null) {
                            if (i != 1 || this.oldBackgroundDrawable == null || ThemeActivity.this.parentLayout == null) {
                                drawable.setAlpha(NalUnitUtil.EXTENDED_SAR);
                            } else {
                                drawable.setAlpha((int) (255.0f * themeAnimationValue));
                            }
                            if (drawable instanceof ColorDrawable) {
                                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                drawable.draw(canvas);
                            } else if (drawable instanceof BitmapDrawable) {
                                if (((BitmapDrawable) drawable).getTileModeX() == TileMode.REPEAT) {
                                    canvas.save();
                                    float f = 2.0f / AndroidUtilities.density;
                                    canvas.scale(f, f);
                                    drawable.setBounds(0, 0, (int) Math.ceil((double) (((float) getMeasuredWidth()) / f)), (int) Math.ceil((double) (((float) getMeasuredHeight()) / f)));
                                    drawable.draw(canvas);
                                    canvas.restore();
                                } else {
                                    int measuredHeight = getMeasuredHeight();
                                    float measuredWidth = ((float) getMeasuredWidth()) / ((float) drawable.getIntrinsicWidth());
                                    float intrinsicHeight = ((float) measuredHeight) / ((float) drawable.getIntrinsicHeight());
                                    if (measuredWidth < intrinsicHeight) {
                                        measuredWidth = intrinsicHeight;
                                    }
                                    int ceil = (int) Math.ceil((double) (((float) drawable.getIntrinsicWidth()) * measuredWidth));
                                    int ceil2 = (int) Math.ceil((double) (((float) drawable.getIntrinsicHeight()) * measuredWidth));
                                    int measuredWidth2 = (getMeasuredWidth() - ceil) / 2;
                                    measuredHeight = (measuredHeight - ceil2) / 2;
                                    canvas.save();
                                    canvas.clipRect(0, 0, ceil, getMeasuredHeight());
                                    drawable.setBounds(measuredWidth2, measuredHeight, ceil + measuredWidth2, ceil2 + measuredHeight);
                                    drawable.draw(canvas);
                                    canvas.restore();
                                }
                            }
                            if (i == 0 && this.oldBackgroundDrawable != null && themeAnimationValue >= 1.0f) {
                                this.oldBackgroundDrawable = null;
                            }
                        }
                        i++;
                    }
                    TextSizeCell.this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    TextSizeCell.this.shadowDrawable.draw(canvas);
                }
            };
            this.messagesContainer.setOrientation(1);
            this.messagesContainer.setWillNotDraw(false);
            this.messagesContainer.setPadding(0, AndroidUtilities.m26dp(11.0f), 0, AndroidUtilities.m26dp(11.0f));
            addView(this.messagesContainer, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 53.0f, 0.0f, 0.0f));
            int currentTimeMillis = ((int) (System.currentTimeMillis() / 1000)) - 3600;
            TL_message tL_message = new TL_message();
            tL_message.message = LocaleController.getString("FontSizePreviewReply", C1067R.string.FontSizePreviewReply);
            int i = currentTimeMillis + 60;
            tL_message.date = i;
            tL_message.dialog_id = 1;
            tL_message.flags = 259;
            tL_message.from_id = UserConfig.getInstance(ThemeActivity.this.currentAccount).getClientUserId();
            tL_message.f461id = 1;
            tL_message.media = new TL_messageMediaEmpty();
            tL_message.out = true;
            tL_message.to_id = new TL_peerUser();
            tL_message.to_id.user_id = 0;
            MessageObject messageObject = new MessageObject(ThemeActivity.this.currentAccount, tL_message, true);
            tL_message = new TL_message();
            tL_message.message = LocaleController.getString("FontSizePreviewLine2", C1067R.string.FontSizePreviewLine2);
            tL_message.date = currentTimeMillis + 960;
            tL_message.dialog_id = 1;
            tL_message.flags = 259;
            tL_message.from_id = UserConfig.getInstance(ThemeActivity.this.currentAccount).getClientUserId();
            tL_message.f461id = 1;
            tL_message.media = new TL_messageMediaEmpty();
            tL_message.out = true;
            tL_message.to_id = new TL_peerUser();
            tL_message.to_id.user_id = 0;
            MessageObject messageObject2 = new MessageObject(ThemeActivity.this.currentAccount, tL_message, true);
            messageObject2.resetLayout();
            messageObject2.eventId = 1;
            tL_message = new TL_message();
            tL_message.message = LocaleController.getString("FontSizePreviewLine1", C1067R.string.FontSizePreviewLine1);
            tL_message.date = i;
            tL_message.dialog_id = 1;
            tL_message.flags = 265;
            tL_message.from_id = 0;
            tL_message.f461id = 1;
            tL_message.reply_to_msg_id = 5;
            tL_message.media = new TL_messageMediaEmpty();
            tL_message.out = false;
            tL_message.to_id = new TL_peerUser();
            tL_message.to_id.user_id = UserConfig.getInstance(ThemeActivity.this.currentAccount).getClientUserId();
            MessageObject messageObject3 = new MessageObject(ThemeActivity.this.currentAccount, tL_message, true);
            messageObject3.customReplyName = LocaleController.getString("FontSizePreviewName", C1067R.string.FontSizePreviewName);
            messageObject3.eventId = 1;
            messageObject3.resetLayout();
            messageObject3.replyMessageObject = messageObject;
            int i2 = 0;
            while (true) {
                ChatMessageCell[] chatMessageCellArr = this.cells;
                if (i2 < chatMessageCellArr.length) {
                    chatMessageCellArr[i2] = new ChatMessageCell(context);
                    this.cells[i2].setDelegate(new ChatMessageCellDelegate(ThemeActivity.this) {
                        public /* synthetic */ boolean canPerformActions() {
                            return C2348-CC.$default$canPerformActions(this);
                        }

                        public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell, float f, float f2) {
                            C2348-CC.$default$didLongPress(this, chatMessageCell, f, f2);
                        }

                        public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell, KeyboardButton keyboardButton) {
                            C2348-CC.$default$didPressBotButton(this, chatMessageCell, keyboardButton);
                        }

                        public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell) {
                            C2348-CC.$default$didPressCancelSendButton(this, chatMessageCell);
                        }

                        public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell, Chat chat, int i, float f, float f2) {
                            C2348-CC.$default$didPressChannelAvatar(this, chatMessageCell, chat, i, f, f2);
                        }

                        public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell) {
                            C2348-CC.$default$didPressHiddenForward(this, chatMessageCell);
                        }

                        public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell, float f, float f2) {
                            C2348-CC.$default$didPressImage(this, chatMessageCell, f, f2);
                        }

                        public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell, int i) {
                            C2348-CC.$default$didPressInstantButton(this, chatMessageCell, i);
                        }

                        public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell, float f, float f2) {
                            C2348-CC.$default$didPressOther(this, chatMessageCell, f, f2);
                        }

                        public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell, int i) {
                            C2348-CC.$default$didPressReplyMessage(this, chatMessageCell, i);
                        }

                        public /* synthetic */ void didPressShare(ChatMessageCell chatMessageCell) {
                            C2348-CC.$default$didPressShare(this, chatMessageCell);
                        }

                        public /* synthetic */ void didPressUrl(MessageObject messageObject, CharacterStyle characterStyle, boolean z) {
                            C2348-CC.$default$didPressUrl(this, messageObject, characterStyle, z);
                        }

                        public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell, User user, float f, float f2) {
                            C2348-CC.$default$didPressUserAvatar(this, chatMessageCell, user, f, f2);
                        }

                        public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell, String str) {
                            C2348-CC.$default$didPressViaBot(this, chatMessageCell, str);
                        }

                        public /* synthetic */ void didPressVoteButton(ChatMessageCell chatMessageCell, TL_pollAnswer tL_pollAnswer) {
                            C2348-CC.$default$didPressVoteButton(this, chatMessageCell, tL_pollAnswer);
                        }

                        public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                            C2348-CC.$default$didStartVideoStream(this, messageObject);
                        }

                        public /* synthetic */ boolean isChatAdminCell(int i) {
                            return C2348-CC.$default$isChatAdminCell(this, i);
                        }

                        public /* synthetic */ void needOpenWebView(String str, String str2, String str3, String str4, int i, int i2) {
                            C2348-CC.$default$needOpenWebView(this, str, str2, str3, str4, i, i2);
                        }

                        public /* synthetic */ boolean needPlayMessage(MessageObject messageObject) {
                            return C2348-CC.$default$needPlayMessage(this, messageObject);
                        }

                        public /* synthetic */ void videoTimerReached() {
                            C2348-CC.$default$videoTimerReached(this);
                        }
                    });
                    chatMessageCellArr = this.cells;
                    chatMessageCellArr[i2].isChat = false;
                    chatMessageCellArr[i2].setFullyDraw(true);
                    this.cells[i2].setMessageObject(i2 == 0 ? messageObject3 : messageObject2, null, false, false);
                    this.messagesContainer.addView(this.cells[i2], LayoutHelper.createLinear(-1, -2));
                    i2++;
                } else {
                    return;
                }
            }
        }

        public /* synthetic */ void lambda$new$0$ThemeActivity$TextSizeCell(float f) {
            int i = this.startFontSize;
            int round = Math.round(((float) i) + (((float) (this.endFontSize - i)) * f));
            if (round != SharedConfig.fontSize) {
                SharedConfig.fontSize = round;
                Editor edit = MessagesController.getGlobalMainSettings().edit();
                edit.putInt("fons_size", SharedConfig.fontSize);
                edit.commit();
                Theme.chat_msgTextPaint.setTextSize((float) AndroidUtilities.m26dp((float) SharedConfig.fontSize));
                round = 0;
                while (true) {
                    ChatMessageCell[] chatMessageCellArr = this.cells;
                    if (round < chatMessageCellArr.length) {
                        chatMessageCellArr[round].getMessageObject().resetLayout();
                        this.cells[round].requestLayout();
                        round++;
                    } else {
                        return;
                    }
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            this.textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(SharedConfig.fontSize);
            canvas.drawText(stringBuilder.toString(), (float) (getMeasuredWidth() - AndroidUtilities.m26dp(39.0f)), (float) AndroidUtilities.m26dp(28.0f), this.textPaint);
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            i = MeasureSpec.getSize(i);
            if (this.lastWidth != i) {
                SeekBarView seekBarView = this.sizeBar;
                int i3 = SharedConfig.fontSize;
                int i4 = this.startFontSize;
                seekBarView.setProgress(((float) (i3 - i4)) / ((float) (this.endFontSize - i4)));
                this.lastWidth = i;
            }
        }

        public void invalidate() {
            super.invalidate();
            this.messagesContainer.invalidate();
            this.sizeBar.invalidate();
            int i = 0;
            while (true) {
                ChatMessageCell[] chatMessageCellArr = this.cells;
                if (i < chatMessageCellArr.length) {
                    chatMessageCellArr[i].invalidate();
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.ThemeActivity$1 */
    class C43361 extends ActionBarMenuOnItemClick {
        C43361() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                ThemeActivity.this.finishFragment();
            } else if (i == 1 && ThemeActivity.this.getParentActivity() != null) {
                Builder builder = new Builder(ThemeActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("NewTheme", C1067R.string.NewTheme));
                builder.setMessage(LocaleController.getString("CreateNewThemeAlert", C1067R.string.CreateNewThemeAlert));
                builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                builder.setPositiveButton(LocaleController.getString("CreateTheme", C1067R.string.CreateTheme), new C2081-$$Lambda$ThemeActivity$1$ZQnhOSOAx8cfjiv91xqtf3q-RU0(this));
                ThemeActivity.this.showDialog(builder.create());
            }
        }

        public /* synthetic */ void lambda$onItemClick$0$ThemeActivity$1(DialogInterface dialogInterface, int i) {
            ThemeActivity.this.openThemeCreate();
        }
    }

    /* renamed from: org.telegram.ui.ThemeActivity$InnerListAdapter */
    private class InnerListAdapter extends SelectionAdapter {
        private Context mContext;

        public int getItemViewType(int i) {
            return 0;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return false;
        }

        public InnerListAdapter(Context context) {
            this.mContext = context;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Holder(new InnerThemeView(this.mContext));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            InnerThemeView innerThemeView = (InnerThemeView) viewHolder.itemView;
            ThemeInfo themeInfo = (ThemeInfo) ThemeActivity.this.defaultThemes.get(i);
            boolean z = true;
            boolean z2 = i == ThemeActivity.this.defaultThemes.size() - 1;
            if (i != 0) {
                z = false;
            }
            innerThemeView.setTheme(themeInfo, z2, z);
        }

        public int getItemCount() {
            return ThemeActivity.this.defaultThemes.size();
        }
    }

    /* renamed from: org.telegram.ui.ThemeActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return ThemeActivity.this.rowCount;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 1 || itemViewType == 4 || itemViewType == 7 || itemViewType == 10 || itemViewType == 11;
        }

        private void showOptionsForTheme(ThemeInfo themeInfo) {
            if (ThemeActivity.this.getParentActivity() != null) {
                BottomSheet.Builder builder = new BottomSheet.Builder(ThemeActivity.this.getParentActivity());
                String str = "ShareFile";
                builder.setItems(themeInfo.pathToFile == null ? new CharSequence[]{LocaleController.getString(str, C1067R.string.ShareFile)} : new CharSequence[]{LocaleController.getString(str, C1067R.string.ShareFile), LocaleController.getString("Edit", C1067R.string.Edit), LocaleController.getString("Delete", C1067R.string.Delete)}, new C2087-$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU(this, themeInfo));
                ThemeActivity.this.showDialog(builder.create());
            }
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:50:0x00d1 */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x00aa A:{Catch:{ Exception -> 0x00f6 }} */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x00a9 A:{RETURN, Catch:{ Exception -> 0x00f6 }} */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x0071 A:{SYNTHETIC, Splitter:B:26:0x0071} */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x00a9 A:{RETURN, Catch:{ Exception -> 0x00f6 }} */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x00aa A:{Catch:{ Exception -> 0x00f6 }} */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x007c A:{SYNTHETIC, Splitter:B:31:0x007c} */
        /* JADX WARNING: Can't wrap try/catch for region: R(4:48|49|50|51) */
        public /* synthetic */ void lambda$showOptionsForTheme$1$ThemeActivity$ListAdapter(org.telegram.p004ui.ActionBar.Theme.ThemeInfo r4, android.content.DialogInterface r5, int r6) {
            /*
            r3 = this;
            r5 = 0;
            r0 = 1;
            if (r6 != 0) goto L_0x00fc;
        L_0x0004:
            r6 = r4.pathToFile;
            if (r6 != 0) goto L_0x0085;
        L_0x0008:
            r6 = r4.assetName;
            if (r6 != 0) goto L_0x0085;
        L_0x000c:
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r6 = org.telegram.p004ui.ActionBar.Theme.getDefaultColors();
            r6 = r6.entrySet();
            r6 = r6.iterator();
        L_0x001d:
            r1 = r6.hasNext();
            if (r1 == 0) goto L_0x0044;
        L_0x0023:
            r1 = r6.next();
            r1 = (java.util.Map.Entry) r1;
            r2 = r1.getKey();
            r2 = (java.lang.String) r2;
            r4.append(r2);
            r2 = "=";
            r4.append(r2);
            r1 = r1.getValue();
            r4.append(r1);
            r1 = "\n";
            r4.append(r1);
            goto L_0x001d;
        L_0x0044:
            r6 = new java.io.File;
            r1 = org.telegram.messenger.ApplicationLoader.getFilesDirFixed();
            r2 = "default_theme.attheme";
            r6.<init>(r1, r2);
            r1 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x006b }
            r1.<init>(r6);	 Catch:{ Exception -> 0x006b }
            r4 = r4.toString();	 Catch:{ Exception -> 0x0066, all -> 0x0063 }
            r4 = org.telegram.messenger.AndroidUtilities.getStringBytes(r4);	 Catch:{ Exception -> 0x0066, all -> 0x0063 }
            r1.write(r4);	 Catch:{ Exception -> 0x0066, all -> 0x0063 }
            r1.close();	 Catch:{ Exception -> 0x0075 }
            goto L_0x0095;
        L_0x0063:
            r4 = move-exception;
            r5 = r1;
            goto L_0x007a;
        L_0x0066:
            r4 = move-exception;
            r5 = r1;
            goto L_0x006c;
        L_0x0069:
            r4 = move-exception;
            goto L_0x007a;
        L_0x006b:
            r4 = move-exception;
        L_0x006c:
            org.telegram.messenger.FileLog.m30e(r4);	 Catch:{ all -> 0x0069 }
            if (r5 == 0) goto L_0x0095;
        L_0x0071:
            r5.close();	 Catch:{ Exception -> 0x0075 }
            goto L_0x0095;
        L_0x0075:
            r4 = move-exception;
            org.telegram.messenger.FileLog.m30e(r4);
            goto L_0x0095;
        L_0x007a:
            if (r5 == 0) goto L_0x0084;
        L_0x007c:
            r5.close();	 Catch:{ Exception -> 0x0080 }
            goto L_0x0084;
        L_0x0080:
            r5 = move-exception;
            org.telegram.messenger.FileLog.m30e(r5);
        L_0x0084:
            throw r4;
        L_0x0085:
            r5 = r4.assetName;
            if (r5 == 0) goto L_0x008e;
        L_0x0089:
            r6 = org.telegram.p004ui.ActionBar.Theme.getAssetFile(r5);
            goto L_0x0095;
        L_0x008e:
            r6 = new java.io.File;
            r4 = r4.pathToFile;
            r6.<init>(r4);
        L_0x0095:
            r4 = new java.io.File;
            r5 = 4;
            r5 = org.telegram.messenger.FileLoader.getDirectory(r5);
            r1 = r6.getName();
            r4.<init>(r5, r1);
            r5 = org.telegram.messenger.AndroidUtilities.copyFile(r6, r4);	 Catch:{ Exception -> 0x00f6 }
            if (r5 != 0) goto L_0x00aa;
        L_0x00a9:
            return;
        L_0x00aa:
            r5 = new android.content.Intent;	 Catch:{ Exception -> 0x00f6 }
            r6 = "android.intent.action.SEND";
            r5.<init>(r6);	 Catch:{ Exception -> 0x00f6 }
            r6 = "text/xml";
            r5.setType(r6);	 Catch:{ Exception -> 0x00f6 }
            r6 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x00f6 }
            r1 = 24;
            r2 = "android.intent.extra.STREAM";
            if (r6 < r1) goto L_0x00d9;
        L_0x00be:
            r6 = org.telegram.p004ui.ThemeActivity.this;	 Catch:{ Exception -> 0x00d1 }
            r6 = r6.getParentActivity();	 Catch:{ Exception -> 0x00d1 }
            r1 = "org.telegram.messenger.provider";
            r6 = androidx.core.content.FileProvider.getUriForFile(r6, r1, r4);	 Catch:{ Exception -> 0x00d1 }
            r5.putExtra(r2, r6);	 Catch:{ Exception -> 0x00d1 }
            r5.setFlags(r0);	 Catch:{ Exception -> 0x00d1 }
            goto L_0x00e0;
        L_0x00d1:
            r4 = android.net.Uri.fromFile(r4);	 Catch:{ Exception -> 0x00f6 }
            r5.putExtra(r2, r4);	 Catch:{ Exception -> 0x00f6 }
            goto L_0x00e0;
        L_0x00d9:
            r4 = android.net.Uri.fromFile(r4);	 Catch:{ Exception -> 0x00f6 }
            r5.putExtra(r2, r4);	 Catch:{ Exception -> 0x00f6 }
        L_0x00e0:
            r4 = org.telegram.p004ui.ThemeActivity.this;	 Catch:{ Exception -> 0x00f6 }
            r6 = "ShareFile";
            r0 = 2131560748; // 0x7f0d092c float:1.8746877E38 double:1.0531309376E-314;
            r6 = org.telegram.messenger.LocaleController.getString(r6, r0);	 Catch:{ Exception -> 0x00f6 }
            r5 = android.content.Intent.createChooser(r5, r6);	 Catch:{ Exception -> 0x00f6 }
            r6 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            r4.startActivityForResult(r5, r6);	 Catch:{ Exception -> 0x00f6 }
            goto L_0x0175;
        L_0x00f6:
            r4 = move-exception;
            org.telegram.messenger.FileLog.m30e(r4);
            goto L_0x0175;
        L_0x00fc:
            if (r6 != r0) goto L_0x0123;
        L_0x00fe:
            r5 = org.telegram.p004ui.ThemeActivity.this;
            r5 = r5.parentLayout;
            if (r5 == 0) goto L_0x0175;
        L_0x0106:
            org.telegram.p004ui.ActionBar.Theme.applyTheme(r4);
            r5 = org.telegram.p004ui.ThemeActivity.this;
            r5 = r5.parentLayout;
            r5.rebuildAllFragmentViews(r0, r0);
            r5 = new org.telegram.ui.Components.ThemeEditorView;
            r5.<init>();
            r6 = org.telegram.p004ui.ThemeActivity.this;
            r6 = r6.getParentActivity();
            r4 = r4.name;
            r5.show(r6, r4);
            goto L_0x0175;
        L_0x0123:
            r6 = org.telegram.p004ui.ThemeActivity.this;
            r6 = r6.getParentActivity();
            if (r6 != 0) goto L_0x012c;
        L_0x012b:
            return;
        L_0x012c:
            r6 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
            r0 = org.telegram.p004ui.ThemeActivity.this;
            r0 = r0.getParentActivity();
            r6.<init>(r0);
            r0 = 2131559260; // 0x7f0d035c float:1.874386E38 double:1.0531302024E-314;
            r1 = "DeleteThemeAlert";
            r0 = org.telegram.messenger.LocaleController.getString(r1, r0);
            r6.setMessage(r0);
            r0 = 2131558635; // 0x7f0d00eb float:1.8742591E38 double:1.0531298936E-314;
            r1 = "AppName";
            r0 = org.telegram.messenger.LocaleController.getString(r1, r0);
            r6.setTitle(r0);
            r0 = 2131559227; // 0x7f0d033b float:1.8743792E38 double:1.053130186E-314;
            r1 = "Delete";
            r0 = org.telegram.messenger.LocaleController.getString(r1, r0);
            r1 = new org.telegram.ui.-$$Lambda$ThemeActivity$ListAdapter$HjGrFd2877SP2gFmUC42vuRyOmw;
            r1.<init>(r3, r4);
            r6.setPositiveButton(r0, r1);
            r4 = 2131558891; // 0x7f0d01eb float:1.874311E38 double:1.05313002E-314;
            r0 = "Cancel";
            r4 = org.telegram.messenger.LocaleController.getString(r0, r4);
            r6.setNegativeButton(r4, r5);
            r4 = org.telegram.p004ui.ThemeActivity.this;
            r5 = r6.create();
            r4.showDialog(r5);
        L_0x0175:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ThemeActivity$ListAdapter.lambda$showOptionsForTheme$1$ThemeActivity$ListAdapter(org.telegram.ui.ActionBar.Theme$ThemeInfo, android.content.DialogInterface, int):void");
        }

        public /* synthetic */ void lambda$null$0$ThemeActivity$ListAdapter(ThemeInfo themeInfo, DialogInterface dialogInterface, int i) {
            if (Theme.deleteTheme(themeInfo)) {
                ThemeActivity.this.parentLayout.rebuildAllFragmentViews(true, true);
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeListUpdated, new Object[0]);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$2$ThemeActivity$ListAdapter(View view) {
            showOptionsForTheme(((ThemeCell) view.getParent()).getCurrentThemeInfo());
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View themeCell;
            View textSettingsCell;
            boolean z = false;
            String str = Theme.key_windowBackgroundWhite;
            switch (i) {
                case 0:
                    Context context = this.mContext;
                    if (ThemeActivity.this.currentType == 1) {
                        z = true;
                    }
                    themeCell = new ThemeCell(context, z);
                    themeCell.setBackgroundColor(Theme.getColor(str));
                    if (ThemeActivity.this.currentType != 1) {
                        themeCell.setOnOptionsClick(new C2088-$$Lambda$ThemeActivity$ListAdapter$pjEslbWZHQ4g-Rxni-i-jc6xbJY(this));
                        break;
                    }
                    break;
                case 1:
                    textSettingsCell = new TextSettingsCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                case 2:
                    textSettingsCell = new TextInfoPrivacyCell(this.mContext);
                    textSettingsCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 3:
                    textSettingsCell = new ShadowSectionCell(this.mContext);
                    break;
                case 4:
                    textSettingsCell = new ThemeTypeCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                case 5:
                    textSettingsCell = new HeaderCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                case 6:
                    textSettingsCell = new BrightnessControlCell(this.mContext) {
                        /* Access modifiers changed, original: protected */
                        public void didChangedValue(float f) {
                            int i = (int) (Theme.autoNightBrighnessThreshold * 100.0f);
                            int i2 = (int) (f * 100.0f);
                            Theme.autoNightBrighnessThreshold = f;
                            if (i != i2) {
                                Holder holder = (Holder) ThemeActivity.this.listView.findViewHolderForAdapterPosition(ThemeActivity.this.automaticBrightnessInfoRow);
                                if (holder != null) {
                                    ((TextInfoPrivacyCell) holder.itemView).setText(LocaleController.formatString("AutoNightBrightnessInfo", C1067R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (Theme.autoNightBrighnessThreshold * 100.0f))));
                                }
                                Theme.checkAutoNightThemeConditions(true);
                            }
                        }
                    };
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                case 7:
                    textSettingsCell = new TextCheckCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                case 8:
                    textSettingsCell = new TextSizeCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                case 9:
                    textSettingsCell = new ChatListCell(this.mContext) {
                        /* Access modifiers changed, original: protected */
                        public void didSelectChatType(boolean z) {
                            SharedConfig.setUseThreeLinesLayout(z);
                        }
                    };
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                case 10:
                    textSettingsCell = new NotificationsCheckCell(this.mContext, 21, 64);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                    break;
                default:
                    themeCell = new RecyclerListView(this.mContext) {
                        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                            if (!(getParent() == null || getParent().getParent() == null)) {
                                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            return super.onInterceptTouchEvent(motionEvent);
                        }

                        public void onDraw(Canvas canvas) {
                            super.onDraw(canvas);
                            if (ThemeActivity.this.hasCustomThemes) {
                                canvas.drawLine(LocaleController.isRTL ? 0.0f : (float) AndroidUtilities.m26dp(20.0f), (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.m26dp(20.0f) : 0)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
                            }
                        }

                        public void setBackgroundColor(int i) {
                            super.setBackgroundColor(i);
                            invalidateViews();
                        }
                    };
                    themeCell.setBackgroundColor(Theme.getColor(str));
                    themeCell.setItemAnimator(null);
                    themeCell.setLayoutAnimation(null);
                    C43394 c43394 = new LinearLayoutManager(this.mContext) {
                        public boolean supportsPredictiveItemAnimations() {
                            return false;
                        }
                    };
                    themeCell.setPadding(0, 0, 0, 0);
                    themeCell.setClipToPadding(false);
                    c43394.setOrientation(0);
                    themeCell.setLayoutManager(c43394);
                    themeCell.setAdapter(new InnerListAdapter(this.mContext));
                    themeCell.setOnItemClickListener(new C3873-$$Lambda$ThemeActivity$ListAdapter$dquXXwWPa2MGvu2xAYM60dhzabo(this));
                    themeCell.setOnItemLongClickListener(new C3874-$$Lambda$ThemeActivity$ListAdapter$pvw4GcZiIzN9zYxDAOmGDBqZDj0(this));
                    ThemeActivity.this.innerListView = themeCell;
                    themeCell.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.m26dp(148.0f)));
                    break;
            }
            textSettingsCell = themeCell;
            return new Holder(textSettingsCell);
        }

        public /* synthetic */ void lambda$onCreateViewHolder$3$ThemeActivity$ListAdapter(View view, int i) {
            ThemeInfo access$6400 = ((InnerThemeView) view).themeInfo;
            if (access$6400 != Theme.getCurrentTheme()) {
                NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                int i2 = NotificationCenter.needSetDayNightTheme;
                r1 = new Object[2];
                int i3 = 0;
                r1[0] = access$6400;
                r1[1] = Boolean.valueOf(false);
                globalInstance.postNotificationName(i2, r1);
                int childCount = ThemeActivity.this.innerListView.getChildCount();
                while (i3 < childCount) {
                    View childAt = ThemeActivity.this.innerListView.getChildAt(i3);
                    if (childAt instanceof InnerThemeView) {
                        ((InnerThemeView) childAt).updateCurrentThemeCheck();
                    }
                    i3++;
                }
            }
        }

        public /* synthetic */ boolean lambda$onCreateViewHolder$4$ThemeActivity$ListAdapter(View view, int i) {
            showOptionsForTheme(((InnerThemeView) view).themeInfo);
            return true;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            String str = "AutoNightAdaptive";
            String str2 = "AutoNightScheduled";
            String str3 = "AutoNightThemeOff";
            String str4 = "AutoNightTheme";
            boolean z = false;
            boolean z2 = true;
            if (itemViewType != 10) {
                String string;
                switch (itemViewType) {
                    case 0:
                        ArrayList access$2000;
                        if (ThemeActivity.this.themeStart2Row < 0 || i < ThemeActivity.this.themeStart2Row) {
                            i -= ThemeActivity.this.themeStartRow;
                            if (ThemeActivity.this.currentType == 1) {
                                access$2000 = ThemeActivity.this.darkThemes;
                            } else if (ThemeActivity.this.currentType == 2) {
                                access$2000 = ThemeActivity.this.defaultThemes;
                            } else {
                                access$2000 = Theme.themes;
                            }
                        } else {
                            i -= ThemeActivity.this.themeStart2Row;
                            access$2000 = ThemeActivity.this.darkThemes;
                        }
                        ThemeInfo themeInfo = (ThemeInfo) access$2000.get(i);
                        ThemeCell themeCell = (ThemeCell) viewHolder.itemView;
                        if (i != access$2000.size() - 1 || ThemeActivity.this.hasCustomThemes) {
                            z = true;
                        }
                        themeCell.setTheme(themeInfo, z);
                        return;
                    case 1:
                        TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                        if (i != ThemeActivity.this.nightThemeRow) {
                            String str5 = "%02d:%02d";
                            if (i == ThemeActivity.this.scheduleFromRow) {
                                i = Theme.autoNightDayStartTime;
                                i -= (i / 60) * 60;
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightFrom", C1067R.string.AutoNightFrom), String.format(str5, new Object[]{Integer.valueOf(itemViewType), Integer.valueOf(i)}), true);
                                return;
                            } else if (i == ThemeActivity.this.scheduleToRow) {
                                i = Theme.autoNightDayEndTime;
                                i -= (i / 60) * 60;
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightTo", C1067R.string.AutoNightTo), String.format(str5, new Object[]{Integer.valueOf(itemViewType), Integer.valueOf(i)}), false);
                                return;
                            } else if (i == ThemeActivity.this.scheduleUpdateLocationRow) {
                                textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", C1067R.string.AutoNightUpdateLocation), Theme.autoNightCityName, false);
                                return;
                            } else if (i == ThemeActivity.this.contactsSortRow) {
                                i = MessagesController.getGlobalMainSettings().getInt("sortContactsBy", 0);
                                if (i == 0) {
                                    string = LocaleController.getString("Default", C1067R.string.Default);
                                } else if (i == 1) {
                                    string = LocaleController.getString("FirstName", C1067R.string.SortFirstName);
                                } else {
                                    string = LocaleController.getString("LastName", C1067R.string.SortLastName);
                                }
                                textSettingsCell.setTextAndValue(LocaleController.getString("SortBy", C1067R.string.SortBy), string, true);
                                return;
                            } else if (i == ThemeActivity.this.backgroundRow) {
                                textSettingsCell.setText(LocaleController.getString("ChatBackground", C1067R.string.ChatBackground), false);
                                return;
                            } else if (i == ThemeActivity.this.contactsReimportRow) {
                                textSettingsCell.setText(LocaleController.getString("ImportContacts", C1067R.string.ImportContacts), true);
                                return;
                            } else if (i == ThemeActivity.this.stickersRow) {
                                textSettingsCell.setText(LocaleController.getString("StickersAndMasks", C1067R.string.StickersAndMasks), false);
                                return;
                            } else if (i == ThemeActivity.this.emojiRow) {
                                textSettingsCell.setText(LocaleController.getString("Emoji", C1067R.string.Emoji), true);
                                return;
                            } else if (i == ThemeActivity.this.showThemesRows) {
                                textSettingsCell.setText(LocaleController.getString("ShowAllThemes", C1067R.string.ShowAllThemes), false);
                                return;
                            } else {
                                return;
                            }
                        } else if (Theme.selectedAutoNightType == 0 || Theme.getCurrentNightTheme() == null) {
                            textSettingsCell.setTextAndValue(LocaleController.getString(str4, C1067R.string.AutoNightTheme), LocaleController.getString(str3, C1067R.string.AutoNightThemeOff), false);
                            return;
                        } else {
                            textSettingsCell.setTextAndValue(LocaleController.getString(str4, C1067R.string.AutoNightTheme), Theme.getCurrentNightThemeName(), false);
                            return;
                        }
                    case 2:
                        TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                        if (i == ThemeActivity.this.automaticBrightnessInfoRow) {
                            textInfoPrivacyCell.setText(LocaleController.formatString("AutoNightBrightnessInfo", C1067R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (Theme.autoNightBrighnessThreshold * 100.0f))));
                            return;
                        } else if (i == ThemeActivity.this.scheduleLocationInfoRow) {
                            textInfoPrivacyCell.setText(ThemeActivity.this.getLocationSunString());
                            return;
                        } else {
                            return;
                        }
                    case 3:
                        itemViewType = ThemeActivity.this.stickersSection2Row;
                        String str6 = Theme.key_windowBackgroundGrayShadow;
                        if (i == itemViewType || i == ThemeActivity.this.themeInfo2Row || ((i == ThemeActivity.this.nightTypeInfoRow && ThemeActivity.this.themeInfoRow == -1) || (i == ThemeActivity.this.themeInfoRow && ThemeActivity.this.nightTypeInfoRow != -1))) {
                            viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, str6));
                            return;
                        } else {
                            viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str6));
                            return;
                        }
                    case 4:
                        ThemeTypeCell themeTypeCell = (ThemeTypeCell) viewHolder.itemView;
                        if (i == ThemeActivity.this.nightDisabledRow) {
                            string = LocaleController.getString("AutoNightDisabled", C1067R.string.AutoNightDisabled);
                            if (Theme.selectedAutoNightType == 0) {
                                z = true;
                            }
                            themeTypeCell.setValue(string, z, true);
                            return;
                        } else if (i == ThemeActivity.this.nightScheduledRow) {
                            string = LocaleController.getString(str2, C1067R.string.AutoNightScheduled);
                            if (Theme.selectedAutoNightType == 1) {
                                z = true;
                            }
                            themeTypeCell.setValue(string, z, true);
                            return;
                        } else if (i == ThemeActivity.this.nightAutomaticRow) {
                            string = LocaleController.getString(str, C1067R.string.AutoNightAdaptive);
                            if (Theme.selectedAutoNightType != 2) {
                                z2 = false;
                            }
                            themeTypeCell.setValue(string, z2, false);
                            return;
                        } else {
                            return;
                        }
                    case 5:
                        HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                        if (i == ThemeActivity.this.scheduleHeaderRow) {
                            headerCell.setText(LocaleController.getString("AutoNightSchedule", C1067R.string.AutoNightSchedule));
                            return;
                        } else if (i == ThemeActivity.this.automaticHeaderRow) {
                            headerCell.setText(LocaleController.getString("AutoNightBrightness", C1067R.string.AutoNightBrightness));
                            return;
                        } else if (i == ThemeActivity.this.preferedHeaderRow) {
                            headerCell.setText(LocaleController.getString("AutoNightPreferred", C1067R.string.AutoNightPreferred));
                            return;
                        } else if (i == ThemeActivity.this.settingsRow) {
                            headerCell.setText(LocaleController.getString("SETTINGS", C1067R.string.SETTINGS));
                            return;
                        } else if (i == ThemeActivity.this.themeHeaderRow) {
                            if (ThemeActivity.this.currentType == 2) {
                                headerCell.setText(LocaleController.getString("BuiltInThemes", C1067R.string.BuiltInThemes));
                                return;
                            } else {
                                headerCell.setText(LocaleController.getString("ColorTheme", C1067R.string.ColorTheme));
                                return;
                            }
                        } else if (i == ThemeActivity.this.textSizeHeaderRow) {
                            headerCell.setText(LocaleController.getString("TextSizeHeader", C1067R.string.TextSizeHeader));
                            return;
                        } else if (i == ThemeActivity.this.chatListHeaderRow) {
                            headerCell.setText(LocaleController.getString("ChatList", C1067R.string.ChatList));
                            return;
                        } else if (i == ThemeActivity.this.themeHeader2Row) {
                            headerCell.setText(LocaleController.getString("CustomThemes", C1067R.string.CustomThemes));
                            return;
                        } else {
                            return;
                        }
                    case 6:
                        ((BrightnessControlCell) viewHolder.itemView).setProgress(Theme.autoNightBrighnessThreshold);
                        return;
                    case 7:
                        TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                        if (i == ThemeActivity.this.scheduleLocationRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("AutoNightLocation", C1067R.string.AutoNightLocation), Theme.autoNightScheduleByLocation, true);
                            return;
                        } else if (i == ThemeActivity.this.enableAnimationsRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("EnableAnimations", C1067R.string.EnableAnimations), MessagesController.getGlobalMainSettings().getBoolean("view_animations", true), true);
                            return;
                        } else if (i == ThemeActivity.this.sendByEnterRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("SendByEnter", C1067R.string.SendByEnter), MessagesController.getGlobalMainSettings().getBoolean("send_by_enter", false), true);
                            return;
                        } else if (i == ThemeActivity.this.saveToGalleryRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("SaveToGallerySettings", C1067R.string.SaveToGallerySettings), SharedConfig.saveToGallery, false);
                            return;
                        } else if (i == ThemeActivity.this.raiseToSpeakRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("RaiseToSpeak", C1067R.string.RaiseToSpeak), SharedConfig.raiseToSpeak, true);
                            return;
                        } else if (i == ThemeActivity.this.customTabsRow) {
                            textCheckCell.setTextAndValueAndCheck(LocaleController.getString("ChromeCustomTabs", C1067R.string.ChromeCustomTabs), LocaleController.getString("ChromeCustomTabsInfo", C1067R.string.ChromeCustomTabsInfo), SharedConfig.customTabs, false, true);
                            return;
                        } else if (i == ThemeActivity.this.directShareRow) {
                            textCheckCell.setTextAndValueAndCheck(LocaleController.getString("DirectShare", C1067R.string.DirectShare), LocaleController.getString("DirectShareInfo", C1067R.string.DirectShareInfo), SharedConfig.directShare, false, true);
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
            NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) viewHolder.itemView;
            if (i == ThemeActivity.this.nightThemeRow) {
                if (Theme.selectedAutoNightType != 0) {
                    z = true;
                }
                CharSequence currentNightThemeName = z ? Theme.getCurrentNightThemeName() : LocaleController.getString(str3, C1067R.string.AutoNightThemeOff);
                if (z) {
                    String string2 = Theme.selectedAutoNightType == 1 ? LocaleController.getString(str2, C1067R.string.AutoNightScheduled) : LocaleController.getString(str, C1067R.string.AutoNightAdaptive);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(string2);
                    stringBuilder.append(" ");
                    stringBuilder.append(currentNightThemeName);
                    currentNightThemeName = stringBuilder.toString();
                }
                notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString(str4, C1067R.string.AutoNightTheme), currentNightThemeName, z, true);
            }
        }

        public void onViewAttachedToWindow(ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 4) {
                ((ThemeTypeCell) viewHolder.itemView).setTypeChecked(viewHolder.getAdapterPosition() == Theme.selectedAutoNightType);
            } else if (itemViewType == 0) {
                ((ThemeCell) viewHolder.itemView).updateCurrentThemeCheck();
            }
            if (itemViewType != 2 && itemViewType != 3) {
                viewHolder.itemView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }

        public int getItemViewType(int i) {
            if (i == ThemeActivity.this.scheduleFromRow || i == ThemeActivity.this.emojiRow || i == ThemeActivity.this.showThemesRows || i == ThemeActivity.this.scheduleToRow || i == ThemeActivity.this.scheduleUpdateLocationRow || i == ThemeActivity.this.backgroundRow || i == ThemeActivity.this.contactsReimportRow || i == ThemeActivity.this.contactsSortRow || i == ThemeActivity.this.stickersRow) {
                return 1;
            }
            if (i == ThemeActivity.this.automaticBrightnessInfoRow || i == ThemeActivity.this.scheduleLocationInfoRow) {
                return 2;
            }
            if (i == ThemeActivity.this.themeInfoRow || i == ThemeActivity.this.nightTypeInfoRow || i == ThemeActivity.this.scheduleFromToInfoRow || i == ThemeActivity.this.stickersSection2Row || i == ThemeActivity.this.settings2Row || i == ThemeActivity.this.newThemeInfoRow || i == ThemeActivity.this.chatListInfoRow || i == ThemeActivity.this.themeInfo2Row) {
                return 3;
            }
            if (i == ThemeActivity.this.nightDisabledRow || i == ThemeActivity.this.nightScheduledRow || i == ThemeActivity.this.nightAutomaticRow) {
                return 4;
            }
            if (i == ThemeActivity.this.scheduleHeaderRow || i == ThemeActivity.this.automaticHeaderRow || i == ThemeActivity.this.preferedHeaderRow || i == ThemeActivity.this.settingsRow || i == ThemeActivity.this.themeHeaderRow || i == ThemeActivity.this.textSizeHeaderRow || i == ThemeActivity.this.chatListHeaderRow || i == ThemeActivity.this.themeHeader2Row) {
                return 5;
            }
            if (i == ThemeActivity.this.automaticBrightnessRow) {
                return 6;
            }
            if (i == ThemeActivity.this.scheduleLocationRow || i == ThemeActivity.this.enableAnimationsRow || i == ThemeActivity.this.sendByEnterRow || i == ThemeActivity.this.saveToGalleryRow || i == ThemeActivity.this.raiseToSpeakRow || i == ThemeActivity.this.customTabsRow || i == ThemeActivity.this.directShareRow) {
                return 7;
            }
            if (i == ThemeActivity.this.textSizeRow) {
                return 8;
            }
            if (i == ThemeActivity.this.chatListRow) {
                return 9;
            }
            if (i == ThemeActivity.this.nightThemeRow) {
                return 10;
            }
            return i == ThemeActivity.this.themeListRow ? 11 : 0;
        }
    }

    static /* synthetic */ void lambda$openThemeCreate$6(DialogInterface dialogInterface, int i) {
    }

    public ThemeActivity(int i) {
        this.currentType = i;
        updateRows();
    }

    private void updateRows() {
        int i;
        int i2 = this.rowCount;
        this.rowCount = 0;
        this.emojiRow = -1;
        this.contactsReimportRow = -1;
        this.contactsSortRow = -1;
        this.scheduleLocationRow = -1;
        this.scheduleUpdateLocationRow = -1;
        this.scheduleLocationInfoRow = -1;
        this.nightDisabledRow = -1;
        this.nightScheduledRow = -1;
        this.nightAutomaticRow = -1;
        this.nightTypeInfoRow = -1;
        this.scheduleHeaderRow = -1;
        this.nightThemeRow = -1;
        this.newThemeInfoRow = -1;
        this.scheduleFromRow = -1;
        this.scheduleToRow = -1;
        this.scheduleFromToInfoRow = -1;
        this.themeStartRow = -1;
        this.themeHeader2Row = -1;
        this.themeInfo2Row = -1;
        this.themeStart2Row = -1;
        this.themeEnd2Row = -1;
        this.themeListRow = -1;
        this.themeEndRow = -1;
        this.showThemesRows = -1;
        this.themeInfoRow = -1;
        this.preferedHeaderRow = -1;
        this.automaticHeaderRow = -1;
        this.automaticBrightnessRow = -1;
        this.automaticBrightnessInfoRow = -1;
        this.textSizeHeaderRow = -1;
        this.themeHeaderRow = -1;
        this.chatListHeaderRow = -1;
        this.chatListRow = -1;
        this.chatListInfoRow = -1;
        this.textSizeRow = -1;
        this.backgroundRow = -1;
        this.settingsRow = -1;
        this.customTabsRow = -1;
        this.directShareRow = -1;
        this.enableAnimationsRow = -1;
        this.raiseToSpeakRow = -1;
        this.sendByEnterRow = -1;
        this.saveToGalleryRow = -1;
        this.settings2Row = -1;
        this.stickersRow = -1;
        this.stickersSection2Row = -1;
        int i3 = this.currentType;
        int i4 = 2;
        ThemeInfo themeInfo;
        if (i3 == 0) {
            this.hasCustomThemes = false;
            this.defaultThemes.clear();
            i3 = Theme.themes.size();
            for (i = 0; i < i3; i++) {
                themeInfo = (ThemeInfo) Theme.themes.get(i);
                if (themeInfo.pathToFile == null) {
                    this.defaultThemes.add(themeInfo);
                } else {
                    this.hasCustomThemes = true;
                }
            }
            Collections.sort(this.defaultThemes, C2093-$$Lambda$ThemeActivity$cs0N3OVBAa2T6bewE_YVZM-eTCA.INSTANCE);
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.textSizeHeaderRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.textSizeRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.backgroundRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.newThemeInfoRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.themeHeaderRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.themeListRow = i3;
            if (this.hasCustomThemes) {
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.showThemesRows = i3;
            }
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.themeInfoRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.chatListHeaderRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.chatListRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.chatListInfoRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.settingsRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.nightThemeRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.customTabsRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.directShareRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.enableAnimationsRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.raiseToSpeakRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.sendByEnterRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.saveToGalleryRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.settings2Row = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.stickersRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.stickersSection2Row = i3;
        } else if (i3 == 2) {
            this.darkThemes.clear();
            this.defaultThemes.clear();
            i3 = Theme.themes.size();
            for (i = 0; i < i3; i++) {
                themeInfo = (ThemeInfo) Theme.themes.get(i);
                if (themeInfo.pathToFile != null) {
                    this.darkThemes.add(themeInfo);
                } else {
                    this.defaultThemes.add(themeInfo);
                }
            }
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.themeHeaderRow = i3;
            i3 = this.rowCount;
            this.themeStartRow = i3;
            this.rowCount = i3 + this.defaultThemes.size();
            i3 = this.rowCount;
            this.themeEndRow = i3;
            this.rowCount = i3 + 1;
            this.themeInfoRow = i3;
            if (!this.darkThemes.isEmpty()) {
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.themeHeader2Row = i3;
                i3 = this.rowCount;
                this.themeStart2Row = i3;
                this.rowCount = i3 + this.darkThemes.size();
                i3 = this.rowCount;
                this.themeEnd2Row = i3;
                this.rowCount = i3 + 1;
                this.themeInfo2Row = i3;
            }
        } else {
            this.darkThemes.clear();
            i3 = Theme.themes.size();
            for (i = 0; i < i3; i++) {
                themeInfo = (ThemeInfo) Theme.themes.get(i);
                if (!themeInfo.isLight()) {
                    this.darkThemes.add(themeInfo);
                }
            }
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.nightDisabledRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.nightScheduledRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.nightAutomaticRow = i3;
            i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.nightTypeInfoRow = i3;
            i3 = Theme.selectedAutoNightType;
            if (i3 == 1) {
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.scheduleHeaderRow = i3;
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.scheduleLocationRow = i3;
                if (Theme.autoNightScheduleByLocation) {
                    i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.scheduleUpdateLocationRow = i3;
                    i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.scheduleLocationInfoRow = i3;
                } else {
                    i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.scheduleFromRow = i3;
                    i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.scheduleToRow = i3;
                    i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.scheduleFromToInfoRow = i3;
                }
            } else if (i3 == 2) {
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.automaticHeaderRow = i3;
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.automaticBrightnessRow = i3;
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.automaticBrightnessInfoRow = i3;
            }
            if (Theme.selectedAutoNightType != 0) {
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.preferedHeaderRow = i3;
                i3 = this.rowCount;
                this.themeStartRow = i3;
                this.rowCount = i3 + this.darkThemes.size();
                i3 = this.rowCount;
                this.themeEndRow = i3;
                this.rowCount = i3 + 1;
                this.themeInfoRow = i3;
            }
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            if (this.currentType == 1) {
                i = this.previousUpdatedType;
                if (i != -1) {
                    int i5 = this.nightTypeInfoRow + 1;
                    if (i != Theme.selectedAutoNightType) {
                        i3 = 0;
                        while (i3 < 3) {
                            Holder holder = (Holder) this.listView.findViewHolderForAdapterPosition(i3);
                            if (holder != null) {
                                ((ThemeTypeCell) holder.itemView).setTypeChecked(i3 == Theme.selectedAutoNightType);
                            }
                            i3++;
                        }
                        int i6 = Theme.selectedAutoNightType;
                        if (i6 == 0) {
                            this.listAdapter.notifyItemRangeRemoved(i5, i2 - i5);
                        } else {
                            i2 = 4;
                            ListAdapter listAdapter2;
                            if (i6 == 1) {
                                i6 = this.previousUpdatedType;
                                if (i6 == 0) {
                                    this.listAdapter.notifyItemRangeInserted(i5, this.rowCount - i5);
                                } else if (i6 == 2) {
                                    this.listAdapter.notifyItemRangeRemoved(i5, 3);
                                    listAdapter2 = this.listAdapter;
                                    if (!Theme.autoNightScheduleByLocation) {
                                        i2 = 5;
                                    }
                                    listAdapter2.notifyItemRangeInserted(i5, i2);
                                }
                            } else if (i6 == 2) {
                                i6 = this.previousUpdatedType;
                                if (i6 == 0) {
                                    this.listAdapter.notifyItemRangeInserted(i5, this.rowCount - i5);
                                } else if (i6 == 1) {
                                    listAdapter2 = this.listAdapter;
                                    if (!Theme.autoNightScheduleByLocation) {
                                        i2 = 5;
                                    }
                                    listAdapter2.notifyItemRangeRemoved(i5, i2);
                                    this.listAdapter.notifyItemRangeInserted(i5, 3);
                                }
                            }
                        }
                    } else {
                        boolean z = this.previousByLocation;
                        boolean z2 = Theme.autoNightScheduleByLocation;
                        if (z != z2) {
                            i5 += 2;
                            listAdapter.notifyItemRangeRemoved(i5, z2 ? 3 : 2);
                            ListAdapter listAdapter3 = this.listAdapter;
                            if (!Theme.autoNightScheduleByLocation) {
                                i4 = 3;
                            }
                            listAdapter3.notifyItemRangeInserted(i5, i4);
                        }
                    }
                }
            }
            this.listAdapter.notifyDataSetChanged();
        }
        if (this.currentType == 1) {
            this.previousByLocation = Theme.autoNightScheduleByLocation;
            this.previousUpdatedType = Theme.selectedAutoNightType;
        }
    }

    static /* synthetic */ int lambda$updateRows$0(ThemeInfo themeInfo, ThemeInfo themeInfo2) {
        int i = themeInfo.sortIndex;
        int i2 = themeInfo2.sortIndex;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.themeListUpdated);
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        stopLocationUpdate();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.themeListUpdated);
        Theme.saveAutoNightThemeConfig();
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.locationPermissionGranted) {
            updateSunTime(null, true);
        } else if (i == NotificationCenter.didSetNewWallpapper) {
            RecyclerListView recyclerListView = this.listView;
            if (recyclerListView != null) {
                recyclerListView.invalidateViews();
            }
        } else if (i == NotificationCenter.themeListUpdated) {
            updateRows();
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        int i = this.currentType;
        if (i == 0) {
            this.actionBar.setTitle(LocaleController.getString("ChatSettings", C1067R.string.ChatSettings));
            ActionBarMenuItem addItem = this.actionBar.createMenu().addItem(0, (int) C1067R.C1065drawable.ic_ab_other);
            addItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", C1067R.string.AccDescrMoreOptions));
            addItem.addSubItem(1, (int) C1067R.C1065drawable.menu_palette, LocaleController.getString("CreateNewThemeMenu", C1067R.string.CreateNewThemeMenu));
        } else if (i == 2) {
            this.actionBar.setTitle(LocaleController.getString("ColorThemes", C1067R.string.ColorThemes));
        } else {
            this.actionBar.setTitle(LocaleController.getString("AutoNightTheme", C1067R.string.AutoNightTheme));
        }
        this.actionBar.setActionBarMenuOnItemClick(new C43361());
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.fragmentView = frameLayout;
        this.listView = new RecyclerListView(context);
        RecyclerListView recyclerListView = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new C3872-$$Lambda$ThemeActivity$6AbNGVXM3fzlqnkK4ORVG2-WTt4(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$5$ThemeActivity(View view, int i, float f, float f2) {
        SharedPreferences globalMainSettings;
        String str;
        boolean z;
        Editor edit;
        if (i == this.enableAnimationsRow) {
            globalMainSettings = MessagesController.getGlobalMainSettings();
            str = "view_animations";
            z = globalMainSettings.getBoolean(str, true);
            edit = globalMainSettings.edit();
            edit.putBoolean(str, z ^ 1);
            edit.commit();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(z ^ 1);
            }
        } else {
            boolean z2 = false;
            if (i == this.backgroundRow) {
                presentFragment(new WallpapersListActivity(0));
            } else if (i == this.sendByEnterRow) {
                globalMainSettings = MessagesController.getGlobalMainSettings();
                str = "send_by_enter";
                z = globalMainSettings.getBoolean(str, false);
                edit = globalMainSettings.edit();
                edit.putBoolean(str, z ^ 1);
                edit.commit();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(z ^ 1);
                }
            } else if (i == this.raiseToSpeakRow) {
                SharedConfig.toogleRaiseToSpeak();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(SharedConfig.raiseToSpeak);
                }
            } else if (i == this.saveToGalleryRow) {
                SharedConfig.toggleSaveToGallery();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(SharedConfig.saveToGallery);
                }
            } else if (i == this.customTabsRow) {
                SharedConfig.toggleCustomTabs();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(SharedConfig.customTabs);
                }
            } else if (i == this.directShareRow) {
                SharedConfig.toggleDirectShare();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(SharedConfig.directShare);
                }
            } else if (i != this.contactsReimportRow) {
                int i2;
                if (i == this.contactsSortRow) {
                    if (getParentActivity() != null) {
                        Builder builder = new Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString("SortBy", C1067R.string.SortBy));
                        builder.setItems(new CharSequence[]{LocaleController.getString("Default", C1067R.string.Default), LocaleController.getString("SortFirstName", C1067R.string.SortFirstName), LocaleController.getString("SortLastName", C1067R.string.SortLastName)}, new C2091-$$Lambda$ThemeActivity$Ur1wYdChFFO5JBnOUCuxyfGs_Qw(this, i));
                        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                        showDialog(builder.create());
                    }
                } else if (i == this.stickersRow) {
                    presentFragment(new StickersActivity(0));
                } else if (i == this.showThemesRows) {
                    presentFragment(new ThemeActivity(2));
                } else if (i == this.emojiRow) {
                    if (getParentActivity() != null) {
                        boolean[] zArr = new boolean[2];
                        BottomSheet.Builder builder2 = new BottomSheet.Builder(getParentActivity());
                        builder2.setApplyTopPadding(false);
                        builder2.setApplyBottomPadding(false);
                        LinearLayout linearLayout = new LinearLayout(getParentActivity());
                        linearLayout.setOrientation(1);
                        int i3 = 0;
                        while (true) {
                            if (i3 >= (VERSION.SDK_INT >= 19 ? 2 : 1)) {
                                break;
                            }
                            CharSequence string;
                            if (i3 == 0) {
                                zArr[i3] = SharedConfig.allowBigEmoji;
                                string = LocaleController.getString("EmojiBigSize", C1067R.string.EmojiBigSize);
                            } else if (i3 == 1) {
                                zArr[i3] = SharedConfig.useSystemEmoji;
                                string = LocaleController.getString("EmojiUseDefault", C1067R.string.EmojiUseDefault);
                            } else {
                                string = null;
                            }
                            CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1, 21);
                            checkBoxCell.setTag(Integer.valueOf(i3));
                            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            linearLayout.addView(checkBoxCell, LayoutHelper.createLinear(-1, 50));
                            checkBoxCell.setText(string, "", zArr[i3], true);
                            checkBoxCell.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                            checkBoxCell.setOnClickListener(new C2084-$$Lambda$ThemeActivity$EM3ewToEngglZwU9SAL1G0JUpz0(zArr));
                            i3++;
                        }
                        BottomSheetCell bottomSheetCell = new BottomSheetCell(getParentActivity(), 1);
                        bottomSheetCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        bottomSheetCell.setTextAndIcon(LocaleController.getString("Save", C1067R.string.Save).toUpperCase(), 0);
                        bottomSheetCell.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2));
                        bottomSheetCell.setOnClickListener(new C2090-$$Lambda$ThemeActivity$Qmfg6ZM5i1uh-yBWU4bHsWNdC00(this, zArr, i));
                        linearLayout.addView(bottomSheetCell, LayoutHelper.createLinear(-1, 50));
                        builder2.setCustomView(linearLayout);
                        showDialog(builder2.create());
                    }
                } else if ((i >= this.themeStartRow && i < this.themeEndRow) || (i >= this.themeStart2Row && i < this.themeEnd2Row)) {
                    ArrayList arrayList;
                    int i4 = this.themeStart2Row;
                    if (i4 < 0 || i < i4) {
                        i -= this.themeStartRow;
                        i4 = this.currentType;
                        if (i4 == 1) {
                            arrayList = this.darkThemes;
                        } else if (i4 == 2) {
                            arrayList = this.defaultThemes;
                        } else {
                            arrayList = Theme.themes;
                        }
                    } else {
                        i -= i4;
                        arrayList = this.darkThemes;
                    }
                    if (i >= 0 && i < arrayList.size()) {
                        ThemeInfo themeInfo = (ThemeInfo) arrayList.get(i);
                        if (this.currentType == 1) {
                            Theme.setCurrentNightTheme(themeInfo);
                        } else if (themeInfo != Theme.getCurrentTheme()) {
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, themeInfo, Boolean.valueOf(false));
                        } else {
                            return;
                        }
                        i4 = this.listView.getChildCount();
                        int i5;
                        while (i5 < i4) {
                            View childAt = this.listView.getChildAt(i5);
                            if (childAt instanceof ThemeCell) {
                                ((ThemeCell) childAt).updateCurrentThemeCheck();
                            }
                            i5++;
                        }
                    }
                } else if (i == this.nightThemeRow) {
                    if ((!LocaleController.isRTL || f > ((float) AndroidUtilities.m26dp(76.0f))) && (LocaleController.isRTL || f < ((float) (view.getMeasuredWidth() - AndroidUtilities.m26dp(76.0f))))) {
                        presentFragment(new ThemeActivity(1));
                    } else {
                        NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                        if (Theme.selectedAutoNightType == 0) {
                            Theme.selectedAutoNightType = 2;
                            notificationsCheckCell.setChecked(true);
                        } else {
                            Theme.selectedAutoNightType = 0;
                            notificationsCheckCell.setChecked(false);
                        }
                        Theme.saveAutoNightThemeConfig();
                        Theme.checkAutoNightThemeConditions();
                        if (Theme.selectedAutoNightType != 0) {
                            z2 = true;
                        }
                        CharSequence currentNightThemeName = z2 ? Theme.getCurrentNightThemeName() : LocaleController.getString("AutoNightThemeOff", C1067R.string.AutoNightThemeOff);
                        if (z2) {
                            String str2;
                            if (Theme.selectedAutoNightType == 1) {
                                i2 = C1067R.string.AutoNightScheduled;
                                str2 = "AutoNightScheduled";
                            } else {
                                i2 = C1067R.string.AutoNightAdaptive;
                                str2 = "AutoNightAdaptive";
                            }
                            str = LocaleController.getString(str2, i2);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(str);
                            stringBuilder.append(" ");
                            stringBuilder.append(currentNightThemeName);
                            currentNightThemeName = stringBuilder.toString();
                        }
                        notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("AutoNightTheme", C1067R.string.AutoNightTheme), currentNightThemeName, z2, true);
                    }
                } else if (i == this.nightDisabledRow) {
                    Theme.selectedAutoNightType = 0;
                    updateRows();
                    Theme.checkAutoNightThemeConditions();
                } else if (i == this.nightScheduledRow) {
                    Theme.selectedAutoNightType = 1;
                    if (Theme.autoNightScheduleByLocation) {
                        updateSunTime(null, true);
                    }
                    updateRows();
                    Theme.checkAutoNightThemeConditions();
                } else if (i == this.nightAutomaticRow) {
                    Theme.selectedAutoNightType = 2;
                    updateRows();
                    Theme.checkAutoNightThemeConditions();
                } else if (i == this.scheduleLocationRow) {
                    Theme.autoNightScheduleByLocation ^= 1;
                    ((TextCheckCell) view).setChecked(Theme.autoNightScheduleByLocation);
                    updateRows();
                    if (Theme.autoNightScheduleByLocation) {
                        updateSunTime(null, true);
                    }
                    Theme.checkAutoNightThemeConditions();
                } else if (i == this.scheduleFromRow || i == this.scheduleToRow) {
                    if (getParentActivity() != null) {
                        int i6;
                        if (i == this.scheduleFromRow) {
                            i2 = Theme.autoNightDayStartTime;
                            i6 = i2 / 60;
                        } else {
                            i2 = Theme.autoNightDayEndTime;
                            i6 = i2 / 60;
                        }
                        showDialog(new TimePickerDialog(getParentActivity(), new C2089-$$Lambda$ThemeActivity$NM7fAI0FGrIygn_Tl1Tnvhrr91Y(this, i, (TextSettingsCell) view), i6, i2 - (i6 * 60), true));
                    }
                } else if (i == this.scheduleUpdateLocationRow) {
                    updateSunTime(null, true);
                }
            }
        }
    }

    public /* synthetic */ void lambda$null$1$ThemeActivity(int i, DialogInterface dialogInterface, int i2) {
        Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("sortContactsBy", i2);
        edit.commit();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(i);
        }
    }

    static /* synthetic */ void lambda$null$2(boolean[] zArr, View view) {
        CheckBoxCell checkBoxCell = (CheckBoxCell) view;
        int intValue = ((Integer) checkBoxCell.getTag()).intValue();
        zArr[intValue] = zArr[intValue] ^ 1;
        checkBoxCell.setChecked(zArr[intValue], true);
    }

    public /* synthetic */ void lambda$null$3$ThemeActivity(boolean[] zArr, int i, View view) {
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        Editor edit = MessagesController.getGlobalMainSettings().edit();
        boolean z = zArr[0];
        SharedConfig.allowBigEmoji = z;
        edit.putBoolean("allowBigEmoji", z);
        boolean z2 = zArr[1];
        SharedConfig.useSystemEmoji = z2;
        edit.putBoolean("useSystemEmoji", z2);
        edit.commit();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(i);
        }
    }

    public /* synthetic */ void lambda$null$4$ThemeActivity(int i, TextSettingsCell textSettingsCell, TimePicker timePicker, int i2, int i3) {
        int i4 = (i2 * 60) + i3;
        String str = "%02d:%02d";
        if (i == this.scheduleFromRow) {
            Theme.autoNightDayStartTime = i4;
            textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightFrom", C1067R.string.AutoNightFrom), String.format(str, new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}), true);
            return;
        }
        Theme.autoNightDayEndTime = i4;
        textSettingsCell.setTextAndValue(LocaleController.getString("AutoNightTo", C1067R.string.AutoNightTo), String.format(str, new Object[]{Integer.valueOf(i2), Integer.valueOf(i3)}), true);
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void openThemeCreate() {
        EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(getParentActivity());
        editTextBoldCursor.setBackgroundDrawable(Theme.createEditTextDrawable(getParentActivity(), true));
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("NewTheme", C1067R.string.NewTheme));
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), C2085-$$Lambda$ThemeActivity$Fq-Zy67h0RlXwDUh_E75MeVk8fE.INSTANCE);
        LinearLayout linearLayout = new LinearLayout(getParentActivity());
        linearLayout.setOrientation(1);
        builder.setView(linearLayout);
        TextView textView = new TextView(getParentActivity());
        textView.setText(LocaleController.formatString("EnterThemeName", C1067R.string.EnterThemeName, new Object[0]));
        textView.setTextSize(16.0f);
        textView.setPadding(AndroidUtilities.m26dp(23.0f), AndroidUtilities.m26dp(12.0f), AndroidUtilities.m26dp(23.0f), AndroidUtilities.m26dp(6.0f));
        String str = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(str));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
        editTextBoldCursor.setTextSize(1, 16.0f);
        editTextBoldCursor.setTextColor(Theme.getColor(str));
        editTextBoldCursor.setMaxLines(1);
        editTextBoldCursor.setLines(1);
        editTextBoldCursor.setInputType(16385);
        editTextBoldCursor.setGravity(51);
        editTextBoldCursor.setSingleLine(true);
        editTextBoldCursor.setImeOptions(6);
        editTextBoldCursor.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        editTextBoldCursor.setCursorSize(AndroidUtilities.m26dp(20.0f));
        editTextBoldCursor.setCursorWidth(1.5f);
        editTextBoldCursor.setPadding(0, AndroidUtilities.m26dp(4.0f), 0, 0);
        linearLayout.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, 36, 51, 24, 6, 24, 0));
        editTextBoldCursor.setOnEditorActionListener(C2096-$$Lambda$ThemeActivity$oWMEMCEfOWz5AenMBirLxMHUSLA.INSTANCE);
        AlertDialog create = builder.create();
        create.setOnShowListener(new C2097-$$Lambda$ThemeActivity$pMIDVqVy4Cqc6NEOajbkSuFQwZA(editTextBoldCursor));
        showDialog(create);
        create.getButton(-1).setOnClickListener(new C2083-$$Lambda$ThemeActivity$9RPKhWFFO8KxhnQ8K0AhpMvKhFA(this, editTextBoldCursor, create));
    }

    static /* synthetic */ void lambda$null$8(EditTextBoldCursor editTextBoldCursor) {
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    public /* synthetic */ void lambda$openThemeCreate$10$ThemeActivity(EditTextBoldCursor editTextBoldCursor, AlertDialog alertDialog, View view) {
        if (editTextBoldCursor.length() == 0) {
            Vibrator vibrator = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200);
            }
            AndroidUtilities.shakeView(editTextBoldCursor, 2.0f, 0);
            return;
        }
        ThemeEditorView themeEditorView = new ThemeEditorView();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(editTextBoldCursor.getText().toString());
        stringBuilder.append(".attheme");
        String stringBuilder2 = stringBuilder.toString();
        themeEditorView.show(getParentActivity(), stringBuilder2);
        Theme.saveCurrentTheme(stringBuilder2, true);
        updateRows();
        alertDialog.dismiss();
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        String str = "themehint";
        if (!globalMainSettings.getBoolean(str, false)) {
            globalMainSettings.edit().putBoolean(str, true).commit();
            try {
                Toast.makeText(getParentActivity(), LocaleController.getString("CreateNewThemeHelp", C1067R.string.CreateNewThemeHelp), 1).show();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    private void updateSunTime(Location location, boolean z) {
        String str = "location";
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService(str);
        if (VERSION.SDK_INT >= 23) {
            Activity parentActivity = getParentActivity();
            if (parentActivity != null) {
                if (parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
                    return;
                }
            }
        }
        String str2 = "gps";
        if (getParentActivity() != null) {
            if (getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                try {
                    if (!((LocationManager) ApplicationLoader.applicationContext.getSystemService(str)).isProviderEnabled(str2)) {
                        Builder builder = new Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                        builder.setMessage(LocaleController.getString("GpsDisabledAlert", C1067R.string.GpsDisabledAlert));
                        builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", C1067R.string.ConnectingToProxyEnable), new C2094-$$Lambda$ThemeActivity$fvRbp4i9JBIdpZn90Y9NJP-7GVc(this));
                        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                        showDialog(builder.create());
                        return;
                    }
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
            } else {
                return;
            }
        }
        try {
            location = locationManager.getLastKnownLocation(str2);
            if (location == null) {
                location = locationManager.getLastKnownLocation("network");
            }
            if (location == null) {
                location = locationManager.getLastKnownLocation("passive");
            }
        } catch (Exception e2) {
            FileLog.m30e(e2);
        }
        if (location == null || z) {
            startLocationUpdate();
            if (location == null) {
                return;
            }
        }
        Theme.autoNightLocationLatitude = location.getLatitude();
        Theme.autoNightLocationLongitude = location.getLongitude();
        int[] calculateSunriseSunset = SunDate.calculateSunriseSunset(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude);
        Theme.autoNightSunriseTime = calculateSunriseSunset[0];
        Theme.autoNightSunsetTime = calculateSunriseSunset[1];
        Theme.autoNightCityName = null;
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        Theme.autoNightLastSunCheckDay = instance.get(5);
        Utilities.globalQueue.postRunnable(new C2092-$$Lambda$ThemeActivity$bw9bigoIiGmqIM9XZH7TFdPN3XE(this));
        Holder holder = (Holder) this.listView.findViewHolderForAdapterPosition(this.scheduleLocationInfoRow);
        if (holder != null) {
            View view = holder.itemView;
            if (view instanceof TextInfoPrivacyCell) {
                ((TextInfoPrivacyCell) view).setText(getLocationSunString());
            }
        }
        if (Theme.autoNightScheduleByLocation && Theme.selectedAutoNightType == 1) {
            Theme.checkAutoNightThemeConditions();
        }
    }

    public /* synthetic */ void lambda$updateSunTime$11$ThemeActivity(DialogInterface dialogInterface, int i) {
        if (getParentActivity() != null) {
            try {
                getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            } catch (Exception unused) {
            }
        }
    }

    public /* synthetic */ void lambda$updateSunTime$13$ThemeActivity() {
        String str = null;
        try {
            List fromLocation = new Geocoder(ApplicationLoader.applicationContext, Locale.getDefault()).getFromLocation(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude, 1);
            if (fromLocation.size() > 0) {
                str = ((Address) fromLocation.get(0)).getLocality();
            }
        } catch (Exception unused) {
        }
        AndroidUtilities.runOnUIThread(new C2082-$$Lambda$ThemeActivity$5EVizJzluGYCke-mYmlNF_TBd-Q(this, str));
    }

    public /* synthetic */ void lambda$null$12$ThemeActivity(String str) {
        Theme.autoNightCityName = str;
        if (Theme.autoNightCityName == null) {
            Theme.autoNightCityName = String.format("(%.06f, %.06f)", new Object[]{Double.valueOf(Theme.autoNightLocationLatitude), Double.valueOf(Theme.autoNightLocationLongitude)});
        }
        Theme.saveAutoNightThemeConfig();
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            Holder holder = (Holder) recyclerListView.findViewHolderForAdapterPosition(this.scheduleUpdateLocationRow);
            if (holder != null) {
                View view = holder.itemView;
                if (view instanceof TextSettingsCell) {
                    ((TextSettingsCell) view).setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", C1067R.string.AutoNightUpdateLocation), Theme.autoNightCityName, false);
                }
            }
        }
    }

    private void startLocationUpdate() {
        if (!this.updatingLocation) {
            this.updatingLocation = true;
            LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
            try {
                locationManager.requestLocationUpdates("gps", 1, 0.0f, this.gpsLocationListener);
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            try {
                locationManager.requestLocationUpdates("network", 1, 0.0f, this.networkLocationListener);
            } catch (Exception e2) {
                FileLog.m30e(e2);
            }
        }
    }

    private void stopLocationUpdate() {
        this.updatingLocation = false;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        locationManager.removeUpdates(this.gpsLocationListener);
        locationManager.removeUpdates(this.networkLocationListener);
    }

    private void showPermissionAlert(boolean z) {
        if (getParentActivity() != null) {
            Builder builder = new Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            if (z) {
                builder.setMessage(LocaleController.getString("PermissionNoLocationPosition", C1067R.string.PermissionNoLocationPosition));
            } else {
                builder.setMessage(LocaleController.getString("PermissionNoLocation", C1067R.string.PermissionNoLocation));
            }
            builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", C1067R.string.PermissionOpenSettings), new C2098-$$Lambda$ThemeActivity$xPe7XQuRsh6WWeEQTpdUdkf0Mig(this));
            builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
            showDialog(builder.create());
        }
    }

    public /* synthetic */ void lambda$showPermissionAlert$14$ThemeActivity(DialogInterface dialogInterface, int i) {
        if (getParentActivity() != null) {
            try {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("package:");
                stringBuilder.append(ApplicationLoader.applicationContext.getPackageName());
                intent.setData(Uri.parse(stringBuilder.toString()));
                getParentActivity().startActivity(intent);
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    private String getLocationSunString() {
        int i = Theme.autoNightSunriseTime;
        i -= (i / 60) * 60;
        Object[] objArr = new Object[]{Integer.valueOf(r1), Integer.valueOf(i)};
        String format = String.format("%02d:%02d", objArr);
        int i2 = Theme.autoNightSunsetTime;
        i2 -= (i2 / 60) * 60;
        Object[] objArr2 = new Object[]{Integer.valueOf(r6), Integer.valueOf(i2)};
        return LocaleController.formatString("AutoNightUpdateLocationInfo", C1067R.string.AutoNightUpdateLocationInfo, String.format("%02d:%02d", objArr2), format);
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[62];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, BrightnessControlCell.class, ThemeTypeCell.class, ThemeCell.class, TextSizeCell.class, ChatListCell.class, NotificationsCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.innerListView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground);
        themeDescriptionArr[9] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem);
        themeDescriptionArr[10] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubmenuItemIcon);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        View view = this.listView;
        Class[] clsArr = new Class[]{ThemeCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        themeDescriptionArr[13] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        clsArr = new Class[]{ThemeCell.class};
        strArr = new String[1];
        strArr[0] = "checkImage";
        themeDescriptionArr[14] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_featuredStickers_addedIcon);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{ThemeCell.class}, new String[]{"optionsButton"}, null, null, null, Theme.key_stickers_menu);
        view = this.listView;
        View view2 = view;
        themeDescriptionArr[16] = new ThemeDescription(view2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        view = this.listView;
        view2 = view;
        themeDescriptionArr[17] = new ThemeDescription(view2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[19] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        clsArr = new Class[]{TextSettingsCell.class};
        strArr = new String[1];
        strArr[0] = "valueTextView";
        themeDescriptionArr[20] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[21] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[22] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        clsArr = new Class[]{TextCheckCell.class};
        strArr = new String[1];
        strArr[0] = "checkBox";
        themeDescriptionArr[23] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[24] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        view = this.listView;
        view2 = view;
        themeDescriptionArr[25] = new ThemeDescription(view2, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"leftImageView"}, null, null, null, Theme.key_profile_actionIcon);
        view = this.listView;
        view2 = view;
        themeDescriptionArr[26] = new ThemeDescription(view2, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"rightImageView"}, null, null, null, Theme.key_profile_actionIcon);
        view = this.listView;
        clsArr = new Class[]{BrightnessControlCell.class};
        strArr = new String[1];
        strArr[0] = "seekBarView";
        themeDescriptionArr[27] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_player_progressBackground);
        view = this.listView;
        view2 = view;
        themeDescriptionArr[28] = new ThemeDescription(view2, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, null, null, null, Theme.key_player_progress);
        themeDescriptionArr[29] = new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[30] = new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"checkImage"}, null, null, null, Theme.key_featuredStickers_addedIcon);
        view = this.listView;
        view2 = view;
        themeDescriptionArr[31] = new ThemeDescription(view2, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{TextSizeCell.class}, new String[]{"sizeBar"}, null, null, null, Theme.key_player_progress);
        themeDescriptionArr[32] = new ThemeDescription(this.listView, 0, new Class[]{TextSizeCell.class}, new String[]{"sizeBar"}, null, null, null, Theme.key_player_progressBackground);
        themeDescriptionArr[33] = new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, null, null, null, Theme.key_radioBackground);
        themeDescriptionArr[34] = new ThemeDescription(this.listView, 0, new Class[]{ChatListCell.class}, null, null, null, Theme.key_radioBackgroundChecked);
        themeDescriptionArr[35] = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[36] = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[37] = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[38] = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        themeDescriptionArr[39] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubble);
        themeDescriptionArr[40] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, Theme.key_chat_inBubbleSelected);
        themeDescriptionArr[41] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable}, null, Theme.key_chat_inBubbleShadow);
        themeDescriptionArr[42] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubble);
        themeDescriptionArr[43] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, Theme.key_chat_outBubbleSelected);
        themeDescriptionArr[44] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable}, null, Theme.key_chat_outBubbleShadow);
        themeDescriptionArr[45] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_messageTextIn);
        themeDescriptionArr[46] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_messageTextOut);
        themeDescriptionArr[47] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, Theme.key_chat_outSentCheck);
        themeDescriptionArr[48] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckSelected);
        themeDescriptionArr[49] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, Theme.key_chat_mediaSentCheck);
        themeDescriptionArr[50] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyLine);
        themeDescriptionArr[51] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyLine);
        themeDescriptionArr[52] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyNameText);
        themeDescriptionArr[53] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyNameText);
        themeDescriptionArr[54] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyMessageText);
        themeDescriptionArr[55] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyMessageText);
        themeDescriptionArr[56] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyMediaMessageSelectedText);
        themeDescriptionArr[57] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyMediaMessageSelectedText);
        themeDescriptionArr[58] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inTimeText);
        themeDescriptionArr[59] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outTimeText);
        themeDescriptionArr[60] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inTimeSelectedText);
        themeDescriptionArr[61] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outTimeSelectedText);
        return themeDescriptionArr;
    }
}
