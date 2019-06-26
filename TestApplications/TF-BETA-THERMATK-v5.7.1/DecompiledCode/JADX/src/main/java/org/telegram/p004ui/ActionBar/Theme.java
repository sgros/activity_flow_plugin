package org.telegram.p004ui.ActionBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.StateSet;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.Components.CombinedDrawable;
import org.telegram.p004ui.Components.ScamDrawable;

/* renamed from: org.telegram.ui.ActionBar.Theme */
public class Theme {
    public static final int ACTION_BAR_AUDIO_SELECTOR_COLOR = 788529152;
    public static final int ACTION_BAR_MEDIA_PICKER_COLOR = -13421773;
    public static final int ACTION_BAR_PHOTO_VIEWER_COLOR = 2130706432;
    public static final int ACTION_BAR_PICKER_SELECTOR_COLOR = -12763843;
    public static final int ACTION_BAR_PLAYER_COLOR = -1;
    public static final int ACTION_BAR_VIDEO_EDIT_COLOR = -16777216;
    public static final int ACTION_BAR_WHITE_SELECTOR_COLOR = 1090519039;
    public static final int ARTICLE_VIEWER_MEDIA_PROGRESS_COLOR = -1;
    public static final int AUTO_NIGHT_TYPE_AUTOMATIC = 2;
    public static final int AUTO_NIGHT_TYPE_NONE = 0;
    public static final int AUTO_NIGHT_TYPE_SCHEDULED = 1;
    private static Field BitmapDrawable_mColorFilter = null;
    public static final long DEFAULT_BACKGROUND_ID = 1000001;
    private static final int LIGHT_SENSOR_THEME_SWITCH_DELAY = 1800;
    private static final int LIGHT_SENSOR_THEME_SWITCH_NEAR_DELAY = 12000;
    private static final int LIGHT_SENSOR_THEME_SWITCH_NEAR_THRESHOLD = 12000;
    private static final float MAXIMUM_LUX_BREAKPOINT = 500.0f;
    private static Method StateListDrawable_getStateDrawableMethod = null;
    public static final long THEME_BACKGROUND_ID = -2;
    private static SensorEventListener ambientSensorListener = new C22297();
    private static HashMap<String, Integer> animatingColors = null;
    public static float autoNightBrighnessThreshold = 0.0f;
    public static String autoNightCityName = null;
    public static int autoNightDayEndTime = 0;
    public static int autoNightDayStartTime = 0;
    public static int autoNightLastSunCheckDay = 0;
    public static double autoNightLocationLatitude = 0.0d;
    public static double autoNightLocationLongitude = 0.0d;
    public static boolean autoNightScheduleByLocation = false;
    public static int autoNightSunriseTime = 0;
    public static int autoNightSunsetTime = 0;
    public static Paint avatar_backgroundPaint = null;
    public static Drawable avatar_broadcastDrawable = null;
    public static Drawable avatar_savedDrawable = null;
    public static Drawable calllog_msgCallDownGreenDrawable = null;
    public static Drawable calllog_msgCallDownRedDrawable = null;
    public static Drawable calllog_msgCallUpGreenDrawable = null;
    public static Drawable calllog_msgCallUpRedDrawable = null;
    private static boolean canStartHolidayAnimation = false;
    public static Paint chat_actionBackgroundPaint = null;
    public static TextPaint chat_actionTextPaint = null;
    public static TextPaint chat_adminPaint = null;
    public static Drawable[] chat_attachButtonDrawables = new Drawable[10];
    public static TextPaint chat_audioPerformerPaint = null;
    public static TextPaint chat_audioTimePaint = null;
    public static TextPaint chat_audioTitlePaint = null;
    public static TextPaint chat_botButtonPaint = null;
    public static Drawable chat_botInlineDrawable = null;
    public static Drawable chat_botLinkDrawalbe = null;
    public static Paint chat_botProgressPaint = null;
    public static Paint chat_composeBackgroundPaint = null;
    public static Drawable chat_composeShadowDrawable = null;
    public static Drawable[] chat_contactDrawable = new Drawable[2];
    public static TextPaint chat_contactNamePaint = null;
    public static TextPaint chat_contactPhonePaint = null;
    public static TextPaint chat_contextResult_descriptionTextPaint = null;
    public static Drawable chat_contextResult_shadowUnderSwitchDrawable = null;
    public static TextPaint chat_contextResult_titleTextPaint = null;
    public static Drawable[] chat_cornerInner = new Drawable[4];
    public static Drawable[] chat_cornerOuter = new Drawable[4];
    public static Paint chat_deleteProgressPaint = null;
    public static Paint chat_docBackPaint = null;
    public static TextPaint chat_docNamePaint = null;
    public static TextPaint chat_durationPaint = null;
    public static Drawable chat_fileIcon = null;
    public static CombinedDrawable[][] chat_fileMiniStatesDrawable = ((CombinedDrawable[][]) Array.newInstance(CombinedDrawable.class, new int[]{6, 2}));
    public static Drawable[][] chat_fileStatesDrawable = ((Drawable[][]) Array.newInstance(Drawable.class, new int[]{10, 2}));
    public static Drawable chat_flameIcon = null;
    public static TextPaint chat_forwardNamePaint = null;
    public static TextPaint chat_gamePaint = null;
    public static Drawable chat_gifIcon = null;
    public static Drawable chat_goIconDrawable = null;
    public static TextPaint chat_infoPaint = null;
    public static Drawable chat_inlineResultAudio = null;
    public static Drawable chat_inlineResultFile = null;
    public static Drawable chat_inlineResultLocation = null;
    public static TextPaint chat_instantViewPaint = null;
    public static Paint chat_instantViewRectPaint = null;
    public static TextPaint chat_livePaint = null;
    public static TextPaint chat_locationAddressPaint = null;
    public static Drawable[] chat_locationDrawable = new Drawable[2];
    public static TextPaint chat_locationTitlePaint = null;
    public static Drawable chat_lockIconDrawable = null;
    public static Drawable chat_msgAvatarLiveLocationDrawable = null;
    public static TextPaint chat_msgBotButtonPaint = null;
    public static Drawable chat_msgBroadcastDrawable = null;
    public static Drawable chat_msgBroadcastMediaDrawable = null;
    public static Drawable chat_msgCallDownGreenDrawable = null;
    public static Drawable chat_msgCallDownRedDrawable = null;
    public static Drawable chat_msgCallUpGreenDrawable = null;
    public static Drawable chat_msgErrorDrawable = null;
    public static Paint chat_msgErrorPaint = null;
    public static TextPaint chat_msgGameTextPaint = null;
    public static Drawable chat_msgInCallDrawable = null;
    public static Drawable chat_msgInCallSelectedDrawable = null;
    public static Drawable chat_msgInClockDrawable = null;
    public static Drawable chat_msgInDrawable = null;
    public static Drawable chat_msgInInstantDrawable = null;
    public static Drawable chat_msgInMediaDrawable = null;
    public static Drawable chat_msgInMediaSelectedDrawable = null;
    public static Drawable chat_msgInMediaShadowDrawable = null;
    public static Drawable chat_msgInMenuDrawable = null;
    public static Drawable chat_msgInMenuSelectedDrawable = null;
    public static Drawable chat_msgInSelectedClockDrawable = null;
    public static Drawable chat_msgInSelectedDrawable = null;
    public static Drawable chat_msgInShadowDrawable = null;
    public static Drawable chat_msgInViewsDrawable = null;
    public static Drawable chat_msgInViewsSelectedDrawable = null;
    public static Drawable chat_msgMediaBroadcastDrawable = null;
    public static Drawable chat_msgMediaCheckDrawable = null;
    public static Drawable chat_msgMediaClockDrawable = null;
    public static Drawable chat_msgMediaHalfCheckDrawable = null;
    public static Drawable chat_msgMediaMenuDrawable = null;
    public static Drawable chat_msgMediaViewsDrawable = null;
    public static Drawable chat_msgNoSoundDrawable = null;
    public static Drawable chat_msgOutBroadcastDrawable = null;
    public static Drawable chat_msgOutCallDrawable = null;
    public static Drawable chat_msgOutCallSelectedDrawable = null;
    public static Drawable chat_msgOutCheckDrawable = null;
    public static Drawable chat_msgOutCheckSelectedDrawable = null;
    public static Drawable chat_msgOutClockDrawable = null;
    public static Drawable chat_msgOutDrawable = null;
    public static Drawable chat_msgOutHalfCheckDrawable = null;
    public static Drawable chat_msgOutHalfCheckSelectedDrawable = null;
    public static Drawable chat_msgOutInstantDrawable = null;
    public static Drawable chat_msgOutLocationDrawable = null;
    public static Drawable chat_msgOutMediaDrawable = null;
    public static Drawable chat_msgOutMediaSelectedDrawable = null;
    public static Drawable chat_msgOutMediaShadowDrawable = null;
    public static Drawable chat_msgOutMenuDrawable = null;
    public static Drawable chat_msgOutMenuSelectedDrawable = null;
    public static Drawable chat_msgOutSelectedClockDrawable = null;
    public static Drawable chat_msgOutSelectedDrawable = null;
    public static Drawable chat_msgOutShadowDrawable = null;
    public static Drawable chat_msgOutViewsDrawable = null;
    public static Drawable chat_msgOutViewsSelectedDrawable = null;
    public static Drawable chat_msgStickerCheckDrawable = null;
    public static Drawable chat_msgStickerClockDrawable = null;
    public static Drawable chat_msgStickerHalfCheckDrawable = null;
    public static Drawable chat_msgStickerViewsDrawable = null;
    public static TextPaint chat_msgTextPaint = null;
    public static TextPaint chat_msgTextPaintOneEmoji = null;
    public static TextPaint chat_msgTextPaintThreeEmoji = null;
    public static TextPaint chat_msgTextPaintTwoEmoji = null;
    public static Drawable chat_muteIconDrawable = null;
    public static TextPaint chat_namePaint = null;
    public static Drawable[][] chat_photoStatesDrawables = ((Drawable[][]) Array.newInstance(Drawable.class, new int[]{13, 2}));
    public static Paint chat_radialProgress2Paint = null;
    public static Paint chat_radialProgressPaint = null;
    public static Drawable chat_redLocationIcon = null;
    public static Drawable chat_replyIconDrawable = null;
    public static Paint chat_replyLinePaint = null;
    public static TextPaint chat_replyNamePaint = null;
    public static TextPaint chat_replyTextPaint = null;
    public static Drawable chat_roundVideoShadow = null;
    public static Drawable chat_shareDrawable = null;
    public static Drawable chat_shareIconDrawable = null;
    public static TextPaint chat_shipmentPaint = null;
    public static Paint chat_statusPaint = null;
    public static Paint chat_statusRecordPaint = null;
    public static Drawable chat_systemDrawable = null;
    public static Paint chat_textSearchSelectionPaint = null;
    public static Paint chat_timeBackgroundPaint = null;
    public static TextPaint chat_timePaint = null;
    public static Paint chat_urlPaint = null;
    public static Paint checkboxSquare_backgroundPaint = null;
    public static Paint checkboxSquare_checkPaint = null;
    public static Paint checkboxSquare_eraserPaint = null;
    public static PorterDuffColorFilter colorFilter = null;
    public static PorterDuffColorFilter colorFilter2 = null;
    public static PorterDuffColorFilter colorPressedFilter = null;
    public static PorterDuffColorFilter colorPressedFilter2 = null;
    private static int currentColor = 0;
    private static HashMap<String, Integer> currentColors = new HashMap();
    private static ThemeInfo currentDayTheme = null;
    private static ThemeInfo currentNightTheme = null;
    private static int currentSelectedColor = 0;
    private static ColorFilter currentShareColorFilter = null;
    private static int currentShareColorFilterColor = 0;
    private static ColorFilter currentShareSelectedColorFilter = null;
    private static int currentShareSelectedColorFilterColor = 0;
    private static ThemeInfo currentTheme = null;
    private static HashMap<String, Integer> defaultColors = new HashMap();
    private static ThemeInfo defaultTheme = null;
    public static LottieDrawable dialogs_archiveAvatarDrawable = null;
    public static boolean dialogs_archiveAvatarDrawableRecolored = false;
    public static Drawable dialogs_archiveDrawable = null;
    public static boolean dialogs_archiveDrawableRecolored = false;
    public static TextPaint dialogs_archiveTextPaint = null;
    public static Drawable dialogs_botDrawable = null;
    public static Drawable dialogs_broadcastDrawable = null;
    public static Drawable dialogs_checkDrawable = null;
    public static Drawable dialogs_clockDrawable = null;
    public static Paint dialogs_countGrayPaint = null;
    public static Paint dialogs_countPaint = null;
    public static TextPaint dialogs_countTextPaint = null;
    public static Drawable dialogs_errorDrawable = null;
    public static Paint dialogs_errorPaint = null;
    public static Drawable dialogs_groupDrawable = null;
    public static Drawable dialogs_halfCheckDrawable = null;
    public static Drawable dialogs_holidayDrawable = null;
    private static int dialogs_holidayDrawableOffsetX = 0;
    private static int dialogs_holidayDrawableOffsetY = 0;
    public static Drawable dialogs_lockDrawable = null;
    public static Drawable dialogs_mentionDrawable = null;
    public static TextPaint dialogs_messageNamePaint = null;
    public static TextPaint dialogs_messagePaint = null;
    public static TextPaint dialogs_messagePrintingPaint = null;
    public static Drawable dialogs_muteDrawable = null;
    public static TextPaint dialogs_nameEncryptedPaint = null;
    public static TextPaint dialogs_namePaint = null;
    public static TextPaint dialogs_offlinePaint = null;
    public static Paint dialogs_onlineCirclePaint = null;
    public static TextPaint dialogs_onlinePaint = null;
    public static Drawable dialogs_pinArchiveDrawable = null;
    public static Drawable dialogs_pinnedDrawable = null;
    public static Paint dialogs_pinnedPaint = null;
    public static Drawable dialogs_reorderDrawable = null;
    public static ScamDrawable dialogs_scamDrawable = null;
    public static TextPaint dialogs_searchNameEncryptedPaint = null;
    public static TextPaint dialogs_searchNamePaint = null;
    public static Paint dialogs_tabletSeletedPaint = null;
    public static TextPaint dialogs_timePaint = null;
    public static Drawable dialogs_unarchiveDrawable = null;
    public static Drawable dialogs_unpinArchiveDrawable = null;
    public static Drawable dialogs_verifiedCheckDrawable = null;
    public static Drawable dialogs_verifiedDrawable = null;
    public static Paint dividerPaint = null;
    private static HashMap<String, String> fallbackKeys = new HashMap();
    private static boolean isCustomTheme = false;
    private static boolean isPatternWallpaper = false;
    private static boolean isWallpaperMotion = false;
    public static final String key_actionBarActionModeDefault = "actionBarActionModeDefault";
    public static final String key_actionBarActionModeDefaultIcon = "actionBarActionModeDefaultIcon";
    public static final String key_actionBarActionModeDefaultSelector = "actionBarActionModeDefaultSelector";
    public static final String key_actionBarActionModeDefaultTop = "actionBarActionModeDefaultTop";
    public static final String key_actionBarDefault = "actionBarDefault";
    public static final String key_actionBarDefaultArchived = "actionBarDefaultArchived";
    public static final String key_actionBarDefaultArchivedIcon = "actionBarDefaultArchivedIcon";
    public static final String key_actionBarDefaultArchivedSearch = "actionBarDefaultArchivedSearch";
    public static final String key_actionBarDefaultArchivedSearchPlaceholder = "actionBarDefaultSearchArchivedPlaceholder";
    public static final String key_actionBarDefaultArchivedSelector = "actionBarDefaultArchivedSelector";
    public static final String key_actionBarDefaultArchivedTitle = "actionBarDefaultArchivedTitle";
    public static final String key_actionBarDefaultIcon = "actionBarDefaultIcon";
    public static final String key_actionBarDefaultSearch = "actionBarDefaultSearch";
    public static final String key_actionBarDefaultSearchPlaceholder = "actionBarDefaultSearchPlaceholder";
    public static final String key_actionBarDefaultSelector = "actionBarDefaultSelector";
    public static final String key_actionBarDefaultSubmenuBackground = "actionBarDefaultSubmenuBackground";
    public static final String key_actionBarDefaultSubmenuItem = "actionBarDefaultSubmenuItem";
    public static final String key_actionBarDefaultSubmenuItemIcon = "actionBarDefaultSubmenuItemIcon";
    public static final String key_actionBarDefaultSubtitle = "actionBarDefaultSubtitle";
    public static final String key_actionBarDefaultTitle = "actionBarDefaultTitle";
    public static final String key_actionBarTabActiveText = "actionBarTabActiveText";
    public static final String key_actionBarTabLine = "actionBarTabLine";
    public static final String key_actionBarTabSelector = "actionBarTabSelector";
    public static final String key_actionBarTabUnactiveText = "actionBarTabUnactiveText";
    public static final String key_actionBarWhiteSelector = "actionBarWhiteSelector";
    public static final String key_avatar_actionBarIconBlue = "avatar_actionBarIconBlue";
    public static final String key_avatar_actionBarSelectorBlue = "avatar_actionBarSelectorBlue";
    public static final String key_avatar_backgroundActionBarBlue = "avatar_backgroundActionBarBlue";
    public static final String key_avatar_backgroundArchived = "avatar_backgroundArchived";
    public static final String key_avatar_backgroundArchivedHidden = "avatar_backgroundArchivedHidden";
    public static final String key_avatar_backgroundBlue = "avatar_backgroundBlue";
    public static final String key_avatar_backgroundCyan = "avatar_backgroundCyan";
    public static final String key_avatar_backgroundGreen = "avatar_backgroundGreen";
    public static final String key_avatar_backgroundGroupCreateSpanBlue = "avatar_backgroundGroupCreateSpanBlue";
    public static final String key_avatar_backgroundInProfileBlue = "avatar_backgroundInProfileBlue";
    public static final String key_avatar_backgroundOrange = "avatar_backgroundOrange";
    public static final String key_avatar_backgroundPink = "avatar_backgroundPink";
    public static final String key_avatar_backgroundRed = "avatar_backgroundRed";
    public static final String key_avatar_backgroundSaved = "avatar_backgroundSaved";
    public static final String key_avatar_backgroundViolet = "avatar_backgroundViolet";
    public static final String key_avatar_nameInMessageBlue = "avatar_nameInMessageBlue";
    public static final String key_avatar_nameInMessageCyan = "avatar_nameInMessageCyan";
    public static final String key_avatar_nameInMessageGreen = "avatar_nameInMessageGreen";
    public static final String key_avatar_nameInMessageOrange = "avatar_nameInMessageOrange";
    public static final String key_avatar_nameInMessagePink = "avatar_nameInMessagePink";
    public static final String key_avatar_nameInMessageRed = "avatar_nameInMessageRed";
    public static final String key_avatar_nameInMessageViolet = "avatar_nameInMessageViolet";
    public static final String key_avatar_subtitleInProfileBlue = "avatar_subtitleInProfileBlue";
    public static final String key_avatar_text = "avatar_text";
    public static final String key_calls_callReceivedGreenIcon = "calls_callReceivedGreenIcon";
    public static final String key_calls_callReceivedRedIcon = "calls_callReceivedRedIcon";
    public static final String key_changephoneinfo_changeText = "key_changephoneinfo_changeText";
    public static final String key_changephoneinfo_image = "changephoneinfo_image";
    public static final String key_chat_addContact = "chat_addContact";
    public static final String key_chat_adminSelectedText = "chat_adminSelectedText";
    public static final String key_chat_adminText = "chat_adminText";
    public static final String key_chat_attachAudioBackground = "chat_attachAudioBackground";
    public static final String key_chat_attachAudioIcon = "chat_attachAudioIcon";
    public static final String key_chat_attachCameraIcon1 = "chat_attachCameraIcon1";
    public static final String key_chat_attachCameraIcon2 = "chat_attachCameraIcon2";
    public static final String key_chat_attachCameraIcon3 = "chat_attachCameraIcon3";
    public static final String key_chat_attachCameraIcon4 = "chat_attachCameraIcon4";
    public static final String key_chat_attachCameraIcon5 = "chat_attachCameraIcon5";
    public static final String key_chat_attachCameraIcon6 = "chat_attachCameraIcon6";
    public static final String key_chat_attachContactBackground = "chat_attachContactBackground";
    public static final String key_chat_attachContactIcon = "chat_attachContactIcon";
    public static final String key_chat_attachFileBackground = "chat_attachFileBackground";
    public static final String key_chat_attachFileIcon = "chat_attachFileIcon";
    public static final String key_chat_attachGalleryBackground = "chat_attachGalleryBackground";
    public static final String key_chat_attachGalleryIcon = "chat_attachGalleryIcon";
    public static final String key_chat_attachHideBackground = "chat_attachHideBackground";
    public static final String key_chat_attachHideIcon = "chat_attachHideIcon";
    public static final String key_chat_attachLocationBackground = "chat_attachLocationBackground";
    public static final String key_chat_attachLocationIcon = "chat_attachLocationIcon";
    public static final String key_chat_attachMediaBanBackground = "chat_attachMediaBanBackground";
    public static final String key_chat_attachMediaBanText = "chat_attachMediaBanText";
    public static final String key_chat_attachPollBackground = "chat_attachPollBackground";
    public static final String key_chat_attachPollIcon = "chat_attachPollIcon";
    public static final String key_chat_attachSendBackground = "chat_attachSendBackground";
    public static final String key_chat_attachSendIcon = "chat_attachSendIcon";
    public static final String key_chat_attachVideoBackground = "chat_attachVideoBackground";
    public static final String key_chat_attachVideoIcon = "chat_attachVideoIcon";
    public static final String key_chat_botButtonText = "chat_botButtonText";
    public static final String key_chat_botKeyboardButtonBackground = "chat_botKeyboardButtonBackground";
    public static final String key_chat_botKeyboardButtonBackgroundPressed = "chat_botKeyboardButtonBackgroundPressed";
    public static final String key_chat_botKeyboardButtonText = "chat_botKeyboardButtonText";
    public static final String key_chat_botProgress = "chat_botProgress";
    public static final String key_chat_botSwitchToInlineText = "chat_botSwitchToInlineText";
    public static final String key_chat_emojiBottomPanelIcon = "chat_emojiBottomPanelIcon";
    public static final String key_chat_emojiPanelBackground = "chat_emojiPanelBackground";
    public static final String key_chat_emojiPanelBackspace = "chat_emojiPanelBackspace";
    public static final String key_chat_emojiPanelBadgeBackground = "chat_emojiPanelBadgeBackground";
    public static final String key_chat_emojiPanelBadgeText = "chat_emojiPanelBadgeText";
    public static final String key_chat_emojiPanelEmptyText = "chat_emojiPanelEmptyText";
    public static final String key_chat_emojiPanelIcon = "chat_emojiPanelIcon";
    public static final String key_chat_emojiPanelIconSelected = "chat_emojiPanelIconSelected";
    public static final String key_chat_emojiPanelMasksIcon = "chat_emojiPanelMasksIcon";
    public static final String key_chat_emojiPanelMasksIconSelected = "chat_emojiPanelMasksIconSelected";
    public static final String key_chat_emojiPanelNewTrending = "chat_emojiPanelNewTrending";
    public static final String key_chat_emojiPanelShadowLine = "chat_emojiPanelShadowLine";
    public static final String key_chat_emojiPanelStickerPackSelector = "chat_emojiPanelStickerPackSelector";
    public static final String key_chat_emojiPanelStickerPackSelectorLine = "chat_emojiPanelStickerPackSelectorLine";
    public static final String key_chat_emojiPanelStickerSetName = "chat_emojiPanelStickerSetName";
    public static final String key_chat_emojiPanelStickerSetNameHighlight = "chat_emojiPanelStickerSetNameHighlight";
    public static final String key_chat_emojiPanelStickerSetNameIcon = "chat_emojiPanelStickerSetNameIcon";
    public static final String key_chat_emojiPanelTrendingDescription = "chat_emojiPanelTrendingDescription";
    public static final String key_chat_emojiPanelTrendingTitle = "chat_emojiPanelTrendingTitle";
    public static final String key_chat_emojiSearchBackground = "chat_emojiSearchBackground";
    public static final String key_chat_emojiSearchIcon = "chat_emojiSearchIcon";
    public static final String key_chat_fieldOverlayText = "chat_fieldOverlayText";
    public static final String key_chat_gifSaveHintBackground = "chat_gifSaveHintBackground";
    public static final String key_chat_gifSaveHintText = "chat_gifSaveHintText";
    public static final String key_chat_goDownButton = "chat_goDownButton";
    public static final String key_chat_goDownButtonCounter = "chat_goDownButtonCounter";
    public static final String key_chat_goDownButtonCounterBackground = "chat_goDownButtonCounterBackground";
    public static final String key_chat_goDownButtonIcon = "chat_goDownButtonIcon";
    public static final String key_chat_goDownButtonShadow = "chat_goDownButtonShadow";
    public static final String key_chat_inAudioCacheSeekbar = "chat_inAudioCacheSeekbar";
    public static final String key_chat_inAudioDurationSelectedText = "chat_inAudioDurationSelectedText";
    public static final String key_chat_inAudioDurationText = "chat_inAudioDurationText";
    public static final String key_chat_inAudioPerformerSelectedText = "chat_inAudioPerfomerSelectedText";
    public static final String key_chat_inAudioPerformerText = "chat_inAudioPerfomerText";
    public static final String key_chat_inAudioProgress = "chat_inAudioProgress";
    public static final String key_chat_inAudioSeekbar = "chat_inAudioSeekbar";
    public static final String key_chat_inAudioSeekbarFill = "chat_inAudioSeekbarFill";
    public static final String key_chat_inAudioSeekbarSelected = "chat_inAudioSeekbarSelected";
    public static final String key_chat_inAudioSelectedProgress = "chat_inAudioSelectedProgress";
    public static final String key_chat_inAudioTitleText = "chat_inAudioTitleText";
    public static final String key_chat_inBubble = "chat_inBubble";
    public static final String key_chat_inBubbleSelected = "chat_inBubbleSelected";
    public static final String key_chat_inBubbleShadow = "chat_inBubbleShadow";
    public static final String key_chat_inContactBackground = "chat_inContactBackground";
    public static final String key_chat_inContactIcon = "chat_inContactIcon";
    public static final String key_chat_inContactNameText = "chat_inContactNameText";
    public static final String key_chat_inContactPhoneSelectedText = "chat_inContactPhoneSelectedText";
    public static final String key_chat_inContactPhoneText = "chat_inContactPhoneText";
    public static final String key_chat_inFileBackground = "chat_inFileBackground";
    public static final String key_chat_inFileBackgroundSelected = "chat_inFileBackgroundSelected";
    public static final String key_chat_inFileIcon = "chat_inFileIcon";
    public static final String key_chat_inFileInfoSelectedText = "chat_inFileInfoSelectedText";
    public static final String key_chat_inFileInfoText = "chat_inFileInfoText";
    public static final String key_chat_inFileNameText = "chat_inFileNameText";
    public static final String key_chat_inFileProgress = "chat_inFileProgress";
    public static final String key_chat_inFileProgressSelected = "chat_inFileProgressSelected";
    public static final String key_chat_inFileSelectedIcon = "chat_inFileSelectedIcon";
    public static final String key_chat_inForwardedNameText = "chat_inForwardedNameText";
    public static final String key_chat_inGreenCall = "chat_inDownCall";
    public static final String key_chat_inInstant = "chat_inInstant";
    public static final String key_chat_inInstantSelected = "chat_inInstantSelected";
    public static final String key_chat_inLoader = "chat_inLoader";
    public static final String key_chat_inLoaderPhoto = "chat_inLoaderPhoto";
    public static final String key_chat_inLoaderPhotoIcon = "chat_inLoaderPhotoIcon";
    public static final String key_chat_inLoaderPhotoIconSelected = "chat_inLoaderPhotoIconSelected";
    public static final String key_chat_inLoaderPhotoSelected = "chat_inLoaderPhotoSelected";
    public static final String key_chat_inLoaderSelected = "chat_inLoaderSelected";
    public static final String key_chat_inLocationBackground = "chat_inLocationBackground";
    public static final String key_chat_inLocationIcon = "chat_inLocationIcon";
    public static final String key_chat_inMediaIcon = "chat_inMediaIcon";
    public static final String key_chat_inMediaIconSelected = "chat_inMediaIconSelected";
    public static final String key_chat_inMenu = "chat_inMenu";
    public static final String key_chat_inMenuSelected = "chat_inMenuSelected";
    public static final String key_chat_inPreviewInstantSelectedText = "chat_inPreviewInstantSelectedText";
    public static final String key_chat_inPreviewInstantText = "chat_inPreviewInstantText";
    public static final String key_chat_inPreviewLine = "chat_inPreviewLine";
    public static final String key_chat_inRedCall = "chat_inUpCall";
    public static final String key_chat_inReplyLine = "chat_inReplyLine";
    public static final String key_chat_inReplyMediaMessageSelectedText = "chat_inReplyMediaMessageSelectedText";
    public static final String key_chat_inReplyMediaMessageText = "chat_inReplyMediaMessageText";
    public static final String key_chat_inReplyMessageText = "chat_inReplyMessageText";
    public static final String key_chat_inReplyNameText = "chat_inReplyNameText";
    public static final String key_chat_inSentClock = "chat_inSentClock";
    public static final String key_chat_inSentClockSelected = "chat_inSentClockSelected";
    public static final String key_chat_inSiteNameText = "chat_inSiteNameText";
    public static final String key_chat_inTimeSelectedText = "chat_inTimeSelectedText";
    public static final String key_chat_inTimeText = "chat_inTimeText";
    public static final String key_chat_inVenueInfoSelectedText = "chat_inVenueInfoSelectedText";
    public static final String key_chat_inVenueInfoText = "chat_inVenueInfoText";
    public static final String key_chat_inViaBotNameText = "chat_inViaBotNameText";
    public static final String key_chat_inViews = "chat_inViews";
    public static final String key_chat_inViewsSelected = "chat_inViewsSelected";
    public static final String key_chat_inVoiceSeekbar = "chat_inVoiceSeekbar";
    public static final String key_chat_inVoiceSeekbarFill = "chat_inVoiceSeekbarFill";
    public static final String key_chat_inVoiceSeekbarSelected = "chat_inVoiceSeekbarSelected";
    public static final String key_chat_inlineResultIcon = "chat_inlineResultIcon";
    public static final String key_chat_linkSelectBackground = "chat_linkSelectBackground";
    public static final String key_chat_lockIcon = "chat_lockIcon";
    public static final String key_chat_mediaBroadcast = "chat_mediaBroadcast";
    public static final String key_chat_mediaInfoText = "chat_mediaInfoText";
    public static final String key_chat_mediaLoaderPhoto = "chat_mediaLoaderPhoto";
    public static final String key_chat_mediaLoaderPhotoIcon = "chat_mediaLoaderPhotoIcon";
    public static final String key_chat_mediaLoaderPhotoIconSelected = "chat_mediaLoaderPhotoIconSelected";
    public static final String key_chat_mediaLoaderPhotoSelected = "chat_mediaLoaderPhotoSelected";
    public static final String key_chat_mediaMenu = "chat_mediaMenu";
    public static final String key_chat_mediaProgress = "chat_mediaProgress";
    public static final String key_chat_mediaSentCheck = "chat_mediaSentCheck";
    public static final String key_chat_mediaSentClock = "chat_mediaSentClock";
    public static final String key_chat_mediaTimeBackground = "chat_mediaTimeBackground";
    public static final String key_chat_mediaTimeText = "chat_mediaTimeText";
    public static final String key_chat_mediaViews = "chat_mediaViews";
    public static final String key_chat_messageLinkIn = "chat_messageLinkIn";
    public static final String key_chat_messageLinkOut = "chat_messageLinkOut";
    public static final String key_chat_messagePanelBackground = "chat_messagePanelBackground";
    public static final String key_chat_messagePanelCancelInlineBot = "chat_messagePanelCancelInlineBot";
    public static final String key_chat_messagePanelHint = "chat_messagePanelHint";
    public static final String key_chat_messagePanelIcons = "chat_messagePanelIcons";
    public static final String key_chat_messagePanelSend = "chat_messagePanelSend";
    public static final String key_chat_messagePanelShadow = "chat_messagePanelShadow";
    public static final String key_chat_messagePanelText = "chat_messagePanelText";
    public static final String key_chat_messagePanelVoiceBackground = "chat_messagePanelVoiceBackground";
    public static final String key_chat_messagePanelVoiceDelete = "chat_messagePanelVoiceDelete";
    public static final String key_chat_messagePanelVoiceDuration = "chat_messagePanelVoiceDuration";
    public static final String key_chat_messagePanelVoiceLock = "key_chat_messagePanelVoiceLock";
    public static final String key_chat_messagePanelVoiceLockBackground = "key_chat_messagePanelVoiceLockBackground";
    public static final String key_chat_messagePanelVoiceLockShadow = "key_chat_messagePanelVoiceLockShadow";
    public static final String key_chat_messagePanelVoicePressed = "chat_messagePanelVoicePressed";
    public static final String key_chat_messagePanelVoiceShadow = "chat_messagePanelVoiceShadow";
    public static final String key_chat_messageTextIn = "chat_messageTextIn";
    public static final String key_chat_messageTextOut = "chat_messageTextOut";
    public static final String key_chat_muteIcon = "chat_muteIcon";
    public static final String key_chat_outAudioCacheSeekbar = "chat_outAudioCacheSeekbar";
    public static final String key_chat_outAudioDurationSelectedText = "chat_outAudioDurationSelectedText";
    public static final String key_chat_outAudioDurationText = "chat_outAudioDurationText";
    public static final String key_chat_outAudioPerformerSelectedText = "chat_outAudioPerfomerSelectedText";
    public static final String key_chat_outAudioPerformerText = "chat_outAudioPerfomerText";
    public static final String key_chat_outAudioProgress = "chat_outAudioProgress";
    public static final String key_chat_outAudioSeekbar = "chat_outAudioSeekbar";
    public static final String key_chat_outAudioSeekbarFill = "chat_outAudioSeekbarFill";
    public static final String key_chat_outAudioSeekbarSelected = "chat_outAudioSeekbarSelected";
    public static final String key_chat_outAudioSelectedProgress = "chat_outAudioSelectedProgress";
    public static final String key_chat_outAudioTitleText = "chat_outAudioTitleText";
    public static final String key_chat_outBroadcast = "chat_outBroadcast";
    public static final String key_chat_outBubble = "chat_outBubble";
    public static final String key_chat_outBubbleSelected = "chat_outBubbleSelected";
    public static final String key_chat_outBubbleShadow = "chat_outBubbleShadow";
    public static final String key_chat_outContactBackground = "chat_outContactBackground";
    public static final String key_chat_outContactIcon = "chat_outContactIcon";
    public static final String key_chat_outContactNameText = "chat_outContactNameText";
    public static final String key_chat_outContactPhoneSelectedText = "chat_outContactPhoneSelectedText";
    public static final String key_chat_outContactPhoneText = "chat_outContactPhoneText";
    public static final String key_chat_outFileBackground = "chat_outFileBackground";
    public static final String key_chat_outFileBackgroundSelected = "chat_outFileBackgroundSelected";
    public static final String key_chat_outFileIcon = "chat_outFileIcon";
    public static final String key_chat_outFileInfoSelectedText = "chat_outFileInfoSelectedText";
    public static final String key_chat_outFileInfoText = "chat_outFileInfoText";
    public static final String key_chat_outFileNameText = "chat_outFileNameText";
    public static final String key_chat_outFileProgress = "chat_outFileProgress";
    public static final String key_chat_outFileProgressSelected = "chat_outFileProgressSelected";
    public static final String key_chat_outFileSelectedIcon = "chat_outFileSelectedIcon";
    public static final String key_chat_outForwardedNameText = "chat_outForwardedNameText";
    public static final String key_chat_outGreenCall = "chat_outUpCall";
    public static final String key_chat_outInstant = "chat_outInstant";
    public static final String key_chat_outInstantSelected = "chat_outInstantSelected";
    public static final String key_chat_outLoader = "chat_outLoader";
    public static final String key_chat_outLoaderPhoto = "chat_outLoaderPhoto";
    public static final String key_chat_outLoaderPhotoIcon = "chat_outLoaderPhotoIcon";
    public static final String key_chat_outLoaderPhotoIconSelected = "chat_outLoaderPhotoIconSelected";
    public static final String key_chat_outLoaderPhotoSelected = "chat_outLoaderPhotoSelected";
    public static final String key_chat_outLoaderSelected = "chat_outLoaderSelected";
    public static final String key_chat_outLocationBackground = "chat_outLocationBackground";
    public static final String key_chat_outLocationIcon = "chat_outLocationIcon";
    public static final String key_chat_outMediaIcon = "chat_outMediaIcon";
    public static final String key_chat_outMediaIconSelected = "chat_outMediaIconSelected";
    public static final String key_chat_outMenu = "chat_outMenu";
    public static final String key_chat_outMenuSelected = "chat_outMenuSelected";
    public static final String key_chat_outPreviewInstantSelectedText = "chat_outPreviewInstantSelectedText";
    public static final String key_chat_outPreviewInstantText = "chat_outPreviewInstantText";
    public static final String key_chat_outPreviewLine = "chat_outPreviewLine";
    public static final String key_chat_outReplyLine = "chat_outReplyLine";
    public static final String key_chat_outReplyMediaMessageSelectedText = "chat_outReplyMediaMessageSelectedText";
    public static final String key_chat_outReplyMediaMessageText = "chat_outReplyMediaMessageText";
    public static final String key_chat_outReplyMessageText = "chat_outReplyMessageText";
    public static final String key_chat_outReplyNameText = "chat_outReplyNameText";
    public static final String key_chat_outSentCheck = "chat_outSentCheck";
    public static final String key_chat_outSentCheckSelected = "chat_outSentCheckSelected";
    public static final String key_chat_outSentClock = "chat_outSentClock";
    public static final String key_chat_outSentClockSelected = "chat_outSentClockSelected";
    public static final String key_chat_outSiteNameText = "chat_outSiteNameText";
    public static final String key_chat_outTimeSelectedText = "chat_outTimeSelectedText";
    public static final String key_chat_outTimeText = "chat_outTimeText";
    public static final String key_chat_outVenueInfoSelectedText = "chat_outVenueInfoSelectedText";
    public static final String key_chat_outVenueInfoText = "chat_outVenueInfoText";
    public static final String key_chat_outViaBotNameText = "chat_outViaBotNameText";
    public static final String key_chat_outViews = "chat_outViews";
    public static final String key_chat_outViewsSelected = "chat_outViewsSelected";
    public static final String key_chat_outVoiceSeekbar = "chat_outVoiceSeekbar";
    public static final String key_chat_outVoiceSeekbarFill = "chat_outVoiceSeekbarFill";
    public static final String key_chat_outVoiceSeekbarSelected = "chat_outVoiceSeekbarSelected";
    public static final String key_chat_previewDurationText = "chat_previewDurationText";
    public static final String key_chat_previewGameText = "chat_previewGameText";
    public static final String key_chat_recordTime = "chat_recordTime";
    public static final String key_chat_recordVoiceCancel = "chat_recordVoiceCancel";
    public static final String key_chat_recordedVoiceBackground = "chat_recordedVoiceBackground";
    public static final String key_chat_recordedVoiceDot = "chat_recordedVoiceDot";
    public static final String key_chat_recordedVoicePlayPause = "chat_recordedVoicePlayPause";
    public static final String key_chat_recordedVoicePlayPausePressed = "chat_recordedVoicePlayPausePressed";
    public static final String key_chat_recordedVoiceProgress = "chat_recordedVoiceProgress";
    public static final String key_chat_recordedVoiceProgressInner = "chat_recordedVoiceProgressInner";
    public static final String key_chat_replyPanelClose = "chat_replyPanelClose";
    public static final String key_chat_replyPanelIcons = "chat_replyPanelIcons";
    public static final String key_chat_replyPanelLine = "chat_replyPanelLine";
    public static final String key_chat_replyPanelMessage = "chat_replyPanelMessage";
    public static final String key_chat_replyPanelName = "chat_replyPanelName";
    public static final String key_chat_reportSpam = "chat_reportSpam";
    public static final String key_chat_searchPanelIcons = "chat_searchPanelIcons";
    public static final String key_chat_searchPanelText = "chat_searchPanelText";
    public static final String key_chat_secretChatStatusText = "chat_secretChatStatusText";
    public static final String key_chat_secretTimeText = "chat_secretTimeText";
    public static final String key_chat_secretTimerBackground = "chat_secretTimerBackground";
    public static final String key_chat_secretTimerText = "chat_secretTimerText";
    public static final String key_chat_selectedBackground = "chat_selectedBackground";
    public static final String key_chat_sentError = "chat_sentError";
    public static final String key_chat_sentErrorIcon = "chat_sentErrorIcon";
    public static final String key_chat_serviceBackground = "chat_serviceBackground";
    public static final String key_chat_serviceBackgroundSelected = "chat_serviceBackgroundSelected";
    public static final String key_chat_serviceIcon = "chat_serviceIcon";
    public static final String key_chat_serviceLink = "chat_serviceLink";
    public static final String key_chat_serviceText = "chat_serviceText";
    public static final String key_chat_shareBackground = "chat_shareBackground";
    public static final String key_chat_shareBackgroundSelected = "chat_shareBackgroundSelected";
    public static final String key_chat_status = "chat_status";
    public static final String key_chat_stickerNameText = "chat_stickerNameText";
    public static final String key_chat_stickerReplyLine = "chat_stickerReplyLine";
    public static final String key_chat_stickerReplyMessageText = "chat_stickerReplyMessageText";
    public static final String key_chat_stickerReplyNameText = "chat_stickerReplyNameText";
    public static final String key_chat_stickerViaBotNameText = "chat_stickerViaBotNameText";
    public static final String key_chat_stickersHintPanel = "chat_stickersHintPanel";
    public static final String key_chat_textSelectBackground = "chat_textSelectBackground";
    public static final String key_chat_topPanelBackground = "chat_topPanelBackground";
    public static final String key_chat_topPanelClose = "chat_topPanelClose";
    public static final String key_chat_topPanelLine = "chat_topPanelLine";
    public static final String key_chat_topPanelMessage = "chat_topPanelMessage";
    public static final String key_chat_topPanelTitle = "chat_topPanelTitle";
    public static final String key_chat_unreadMessagesStartArrowIcon = "chat_unreadMessagesStartArrowIcon";
    public static final String key_chat_unreadMessagesStartBackground = "chat_unreadMessagesStartBackground";
    public static final String key_chat_unreadMessagesStartText = "chat_unreadMessagesStartText";
    public static final String key_chat_wallpaper = "chat_wallpaper";
    public static final String key_chats_actionBackground = "chats_actionBackground";
    public static final String key_chats_actionIcon = "chats_actionIcon";
    public static final String key_chats_actionMessage = "chats_actionMessage";
    public static final String key_chats_actionPressedBackground = "chats_actionPressedBackground";
    public static final String key_chats_actionUnreadBackground = "chats_actionUnreadBackground";
    public static final String key_chats_actionUnreadIcon = "chats_actionUnreadIcon";
    public static final String key_chats_actionUnreadPressedBackground = "chats_actionUnreadPressedBackground";
    public static final String key_chats_archiveBackground = "chats_archiveBackground";
    public static final String key_chats_archiveIcon = "chats_archiveIcon";
    public static final String key_chats_archivePinBackground = "chats_archivePinBackground";
    public static final String key_chats_archiveText = "chats_archiveText";
    public static final String key_chats_attachMessage = "chats_attachMessage";
    public static final String key_chats_date = "chats_date";
    public static final String key_chats_draft = "chats_draft";
    public static final String key_chats_mentionIcon = "chats_mentionIcon";
    public static final String key_chats_menuBackground = "chats_menuBackground";
    public static final String key_chats_menuCloud = "chats_menuCloud";
    public static final String key_chats_menuCloudBackgroundCats = "chats_menuCloudBackgroundCats";
    public static final String key_chats_menuItemCheck = "chats_menuItemCheck";
    public static final String key_chats_menuItemIcon = "chats_menuItemIcon";
    public static final String key_chats_menuItemText = "chats_menuItemText";
    public static final String key_chats_menuName = "chats_menuName";
    public static final String key_chats_menuPhone = "chats_menuPhone";
    public static final String key_chats_menuPhoneCats = "chats_menuPhoneCats";
    public static final String key_chats_menuTopBackground = "chats_menuTopBackground";
    public static final String key_chats_menuTopBackgroundCats = "chats_menuTopBackgroundCats";
    public static final String key_chats_menuTopShadow = "chats_menuTopShadow";
    public static final String key_chats_menuTopShadowCats = "chats_menuTopShadowCats";
    public static final String key_chats_message = "chats_message";
    public static final String key_chats_messageArchived = "chats_messageArchived";
    public static final String key_chats_message_threeLines = "chats_message_threeLines";
    public static final String key_chats_muteIcon = "chats_muteIcon";
    public static final String key_chats_name = "chats_name";
    public static final String key_chats_nameArchived = "chats_nameArchived";
    public static final String key_chats_nameIcon = "chats_nameIcon";
    public static final String key_chats_nameMessage = "chats_nameMessage";
    public static final String key_chats_nameMessageArchived = "chats_nameMessageArchived";
    public static final String key_chats_nameMessageArchived_threeLines = "chats_nameMessageArchived_threeLines";
    public static final String key_chats_nameMessage_threeLines = "chats_nameMessage_threeLines";
    public static final String key_chats_onlineCircle = "chats_onlineCircle";
    public static final String key_chats_pinnedIcon = "chats_pinnedIcon";
    public static final String key_chats_pinnedOverlay = "chats_pinnedOverlay";
    public static final String key_chats_secretIcon = "chats_secretIcon";
    public static final String key_chats_secretName = "chats_secretName";
    public static final String key_chats_sentCheck = "chats_sentCheck";
    public static final String key_chats_sentClock = "chats_sentClock";
    public static final String key_chats_sentError = "chats_sentError";
    public static final String key_chats_sentErrorIcon = "chats_sentErrorIcon";
    public static final String key_chats_tabletSelectedOverlay = "chats_tabletSelectedOverlay";
    public static final String key_chats_unreadCounter = "chats_unreadCounter";
    public static final String key_chats_unreadCounterMuted = "chats_unreadCounterMuted";
    public static final String key_chats_unreadCounterText = "chats_unreadCounterText";
    public static final String key_chats_verifiedBackground = "chats_verifiedBackground";
    public static final String key_chats_verifiedCheck = "chats_verifiedCheck";
    public static final String key_checkbox = "checkbox";
    public static final String key_checkboxCheck = "checkboxCheck";
    public static final String key_checkboxDisabled = "checkboxDisabled";
    public static final String key_checkboxSquareBackground = "checkboxSquareBackground";
    public static final String key_checkboxSquareCheck = "checkboxSquareCheck";
    public static final String key_checkboxSquareDisabled = "checkboxSquareDisabled";
    public static final String key_checkboxSquareUnchecked = "checkboxSquareUnchecked";
    public static final String key_contacts_inviteBackground = "contacts_inviteBackground";
    public static final String key_contacts_inviteText = "contacts_inviteText";
    public static final String key_contextProgressInner1 = "contextProgressInner1";
    public static final String key_contextProgressInner2 = "contextProgressInner2";
    public static final String key_contextProgressInner3 = "contextProgressInner3";
    public static final String key_contextProgressInner4 = "contextProgressInner4";
    public static final String key_contextProgressOuter1 = "contextProgressOuter1";
    public static final String key_contextProgressOuter2 = "contextProgressOuter2";
    public static final String key_contextProgressOuter3 = "contextProgressOuter3";
    public static final String key_contextProgressOuter4 = "contextProgressOuter4";
    public static final String key_dialogBackground = "dialogBackground";
    public static final String key_dialogBackgroundGray = "dialogBackgroundGray";
    public static final String key_dialogBadgeBackground = "dialogBadgeBackground";
    public static final String key_dialogBadgeText = "dialogBadgeText";
    public static final String key_dialogButton = "dialogButton";
    public static final String key_dialogButtonSelector = "dialogButtonSelector";
    public static final String key_dialogCameraIcon = "dialogCameraIcon";
    public static final String key_dialogCheckboxSquareBackground = "dialogCheckboxSquareBackground";
    public static final String key_dialogCheckboxSquareCheck = "dialogCheckboxSquareCheck";
    public static final String key_dialogCheckboxSquareDisabled = "dialogCheckboxSquareDisabled";
    public static final String key_dialogCheckboxSquareUnchecked = "dialogCheckboxSquareUnchecked";
    public static final String key_dialogFloatingButton = "dialogFloatingButton";
    public static final String key_dialogFloatingButtonPressed = "dialogFloatingButtonPressed";
    public static final String key_dialogFloatingIcon = "dialogFloatingIcon";
    public static final String key_dialogGrayLine = "dialogGrayLine";
    public static final String key_dialogIcon = "dialogIcon";
    public static final String key_dialogInputField = "dialogInputField";
    public static final String key_dialogInputFieldActivated = "dialogInputFieldActivated";
    public static final String key_dialogLineProgress = "dialogLineProgress";
    public static final String key_dialogLineProgressBackground = "dialogLineProgressBackground";
    public static final String key_dialogLinkSelection = "dialogLinkSelection";
    public static final String key_dialogProgressCircle = "dialogProgressCircle";
    public static final String key_dialogRadioBackground = "dialogRadioBackground";
    public static final String key_dialogRadioBackgroundChecked = "dialogRadioBackgroundChecked";
    public static final String key_dialogRedIcon = "dialogRedIcon";
    public static final String key_dialogRoundCheckBox = "dialogRoundCheckBox";
    public static final String key_dialogRoundCheckBoxCheck = "dialogRoundCheckBoxCheck";
    public static final String key_dialogScrollGlow = "dialogScrollGlow";
    public static final String key_dialogSearchBackground = "dialogSearchBackground";
    public static final String key_dialogSearchHint = "dialogSearchHint";
    public static final String key_dialogSearchIcon = "dialogSearchIcon";
    public static final String key_dialogSearchText = "dialogSearchText";
    public static final String key_dialogShadowLine = "dialogShadowLine";
    public static final String key_dialogTextBlack = "dialogTextBlack";
    public static final String key_dialogTextBlue = "dialogTextBlue";
    public static final String key_dialogTextBlue2 = "dialogTextBlue2";
    public static final String key_dialogTextBlue3 = "dialogTextBlue3";
    public static final String key_dialogTextBlue4 = "dialogTextBlue4";
    public static final String key_dialogTextGray = "dialogTextGray";
    public static final String key_dialogTextGray2 = "dialogTextGray2";
    public static final String key_dialogTextGray3 = "dialogTextGray3";
    public static final String key_dialogTextGray4 = "dialogTextGray4";
    public static final String key_dialogTextHint = "dialogTextHint";
    public static final String key_dialogTextLink = "dialogTextLink";
    public static final String key_dialogTextRed = "dialogTextRed";
    public static final String key_dialogTextRed2 = "dialogTextRed2";
    public static final String key_dialogTopBackground = "dialogTopBackground";
    public static final String key_dialog_inlineProgress = "dialog_inlineProgress";
    public static final String key_dialog_inlineProgressBackground = "dialog_inlineProgressBackground";
    public static final String key_dialog_liveLocationProgress = "dialog_liveLocationProgress";
    public static final String key_divider = "divider";
    public static final String key_emptyListPlaceholder = "emptyListPlaceholder";
    public static final String key_fastScrollActive = "fastScrollActive";
    public static final String key_fastScrollInactive = "fastScrollInactive";
    public static final String key_fastScrollText = "fastScrollText";
    public static final String key_featuredStickers_addButton = "featuredStickers_addButton";
    public static final String key_featuredStickers_addButtonPressed = "featuredStickers_addButtonPressed";
    public static final String key_featuredStickers_addedIcon = "featuredStickers_addedIcon";
    public static final String key_featuredStickers_buttonProgress = "featuredStickers_buttonProgress";
    public static final String key_featuredStickers_buttonText = "featuredStickers_buttonText";
    public static final String key_featuredStickers_delButton = "featuredStickers_delButton";
    public static final String key_featuredStickers_delButtonPressed = "featuredStickers_delButtonPressed";
    public static final String key_featuredStickers_unread = "featuredStickers_unread";
    public static final String key_files_folderIcon = "files_folderIcon";
    public static final String key_files_folderIconBackground = "files_folderIconBackground";
    public static final String key_files_iconText = "files_iconText";
    public static final String key_graySection = "graySection";
    public static final String key_graySectionText = "key_graySectionText";
    public static final String key_groupcreate_cursor = "groupcreate_cursor";
    public static final String key_groupcreate_hintText = "groupcreate_hintText";
    public static final String key_groupcreate_sectionShadow = "groupcreate_sectionShadow";
    public static final String key_groupcreate_sectionText = "groupcreate_sectionText";
    public static final String key_groupcreate_spanBackground = "groupcreate_spanBackground";
    public static final String key_groupcreate_spanDelete = "groupcreate_spanDelete";
    public static final String key_groupcreate_spanText = "groupcreate_spanText";
    public static final String key_inappPlayerBackground = "inappPlayerBackground";
    public static final String key_inappPlayerClose = "inappPlayerClose";
    public static final String key_inappPlayerPerformer = "inappPlayerPerformer";
    public static final String key_inappPlayerPlayPause = "inappPlayerPlayPause";
    public static final String key_inappPlayerTitle = "inappPlayerTitle";
    public static final String key_listSelector = "listSelectorSDK21";
    public static final String key_location_liveLocationProgress = "location_liveLocationProgress";
    public static final String key_location_markerX = "location_markerX";
    public static final String key_location_placeLocationBackground = "location_placeLocationBackground";
    public static final String key_location_sendLiveLocationBackground = "location_sendLiveLocationBackground";
    public static final String key_location_sendLiveLocationIcon = "location_sendLiveLocationIcon";
    public static final String key_location_sendLocationBackground = "location_sendLocationBackground";
    public static final String key_location_sendLocationIcon = "location_sendLocationIcon";
    public static final String key_login_progressInner = "login_progressInner";
    public static final String key_login_progressOuter = "login_progressOuter";
    public static final String key_musicPicker_buttonBackground = "musicPicker_buttonBackground";
    public static final String key_musicPicker_buttonIcon = "musicPicker_buttonIcon";
    public static final String key_musicPicker_checkbox = "musicPicker_checkbox";
    public static final String key_musicPicker_checkboxCheck = "musicPicker_checkboxCheck";
    public static final String key_passport_authorizeBackground = "passport_authorizeBackground";
    public static final String key_passport_authorizeBackgroundSelected = "passport_authorizeBackgroundSelected";
    public static final String key_passport_authorizeText = "passport_authorizeText";
    public static final String key_picker_badge = "picker_badge";
    public static final String key_picker_badgeText = "picker_badgeText";
    public static final String key_picker_disabledButton = "picker_disabledButton";
    public static final String key_picker_enabledButton = "picker_enabledButton";
    public static final String key_player_actionBar = "player_actionBar";
    public static final String key_player_actionBarItems = "player_actionBarItems";
    public static final String key_player_actionBarSelector = "player_actionBarSelector";
    public static final String key_player_actionBarSubtitle = "player_actionBarSubtitle";
    public static final String key_player_actionBarTitle = "player_actionBarTitle";
    public static final String key_player_actionBarTop = "player_actionBarTop";
    public static final String key_player_background = "player_background";
    public static final String key_player_button = "player_button";
    public static final String key_player_buttonActive = "player_buttonActive";
    public static final String key_player_placeholder = "player_placeholder";
    public static final String key_player_placeholderBackground = "player_placeholderBackground";
    public static final String key_player_progress = "player_progress";
    public static final String key_player_progressBackground = "player_progressBackground";
    public static final String key_player_progressCachedBackground = "key_player_progressCachedBackground";
    public static final String key_player_time = "player_time";
    public static final String key_profile_actionBackground = "profile_actionBackground";
    public static final String key_profile_actionIcon = "profile_actionIcon";
    public static final String key_profile_actionPressedBackground = "profile_actionPressedBackground";
    public static final String key_profile_creatorIcon = "profile_creatorIcon";
    public static final String key_profile_status = "profile_status";
    public static final String key_profile_title = "profile_title";
    public static final String key_profile_verifiedBackground = "profile_verifiedBackground";
    public static final String key_profile_verifiedCheck = "profile_verifiedCheck";
    public static final String key_progressCircle = "progressCircle";
    public static final String key_radioBackground = "radioBackground";
    public static final String key_radioBackgroundChecked = "radioBackgroundChecked";
    public static final String key_returnToCallBackground = "returnToCallBackground";
    public static final String key_returnToCallText = "returnToCallText";
    public static final String key_sessions_devicesImage = "sessions_devicesImage";
    public static final String key_sharedMedia_actionMode = "sharedMedia_actionMode";
    public static final String key_sharedMedia_linkPlaceholder = "sharedMedia_linkPlaceholder";
    public static final String key_sharedMedia_linkPlaceholderText = "sharedMedia_linkPlaceholderText";
    public static final String key_sharedMedia_photoPlaceholder = "sharedMedia_photoPlaceholder";
    public static final String key_sharedMedia_startStopLoadIcon = "sharedMedia_startStopLoadIcon";
    public static final String key_sheet_other = "key_sheet_other";
    public static final String key_sheet_scrollUp = "key_sheet_scrollUp";
    public static final String key_stickers_menu = "stickers_menu";
    public static final String key_stickers_menuSelector = "stickers_menuSelector";
    public static final String key_switch2Track = "switch2Track";
    public static final String key_switch2TrackChecked = "switch2TrackChecked";
    public static final String key_switchTrack = "switchTrack";
    public static final String key_switchTrackBlue = "switchTrackBlue";
    public static final String key_switchTrackBlueChecked = "switchTrackBlueChecked";
    public static final String key_switchTrackBlueSelector = "switchTrackBlueSelector";
    public static final String key_switchTrackBlueSelectorChecked = "switchTrackBlueSelectorChecked";
    public static final String key_switchTrackBlueThumb = "switchTrackBlueThumb";
    public static final String key_switchTrackBlueThumbChecked = "switchTrackBlueThumbChecked";
    public static final String key_switchTrackChecked = "switchTrackChecked";
    public static final String key_undo_background = "undo_background";
    public static final String key_undo_cancelColor = "undo_cancelColor";
    public static final String key_undo_infoColor = "undo_infoColor";
    public static final String key_windowBackgroundCheckText = "windowBackgroundCheckText";
    public static final String key_windowBackgroundChecked = "windowBackgroundChecked";
    public static final String key_windowBackgroundGray = "windowBackgroundGray";
    public static final String key_windowBackgroundGrayShadow = "windowBackgroundGrayShadow";
    public static final String key_windowBackgroundUnchecked = "windowBackgroundUnchecked";
    public static final String key_windowBackgroundWhite = "windowBackgroundWhite";
    public static final String key_windowBackgroundWhiteBlackText = "windowBackgroundWhiteBlackText";
    public static final String key_windowBackgroundWhiteBlueButton = "windowBackgroundWhiteBlueButton";
    public static final String key_windowBackgroundWhiteBlueHeader = "windowBackgroundWhiteBlueHeader";
    public static final String key_windowBackgroundWhiteBlueIcon = "windowBackgroundWhiteBlueIcon";
    public static final String key_windowBackgroundWhiteBlueText = "windowBackgroundWhiteBlueText";
    public static final String key_windowBackgroundWhiteBlueText2 = "windowBackgroundWhiteBlueText2";
    public static final String key_windowBackgroundWhiteBlueText3 = "windowBackgroundWhiteBlueText3";
    public static final String key_windowBackgroundWhiteBlueText4 = "windowBackgroundWhiteBlueText4";
    public static final String key_windowBackgroundWhiteBlueText5 = "windowBackgroundWhiteBlueText5";
    public static final String key_windowBackgroundWhiteBlueText6 = "windowBackgroundWhiteBlueText6";
    public static final String key_windowBackgroundWhiteBlueText7 = "windowBackgroundWhiteBlueText7";
    public static final String key_windowBackgroundWhiteGrayIcon = "windowBackgroundWhiteGrayIcon";
    public static final String key_windowBackgroundWhiteGrayLine = "windowBackgroundWhiteGrayLine";
    public static final String key_windowBackgroundWhiteGrayText = "windowBackgroundWhiteGrayText";
    public static final String key_windowBackgroundWhiteGrayText2 = "windowBackgroundWhiteGrayText2";
    public static final String key_windowBackgroundWhiteGrayText3 = "windowBackgroundWhiteGrayText3";
    public static final String key_windowBackgroundWhiteGrayText4 = "windowBackgroundWhiteGrayText4";
    public static final String key_windowBackgroundWhiteGrayText5 = "windowBackgroundWhiteGrayText5";
    public static final String key_windowBackgroundWhiteGrayText6 = "windowBackgroundWhiteGrayText6";
    public static final String key_windowBackgroundWhiteGrayText7 = "windowBackgroundWhiteGrayText7";
    public static final String key_windowBackgroundWhiteGrayText8 = "windowBackgroundWhiteGrayText8";
    public static final String key_windowBackgroundWhiteGreenText = "windowBackgroundWhiteGreenText";
    public static final String key_windowBackgroundWhiteGreenText2 = "windowBackgroundWhiteGreenText2";
    public static final String key_windowBackgroundWhiteHintText = "windowBackgroundWhiteHintText";
    public static final String key_windowBackgroundWhiteInputField = "windowBackgroundWhiteInputField";
    public static final String key_windowBackgroundWhiteInputFieldActivated = "windowBackgroundWhiteInputFieldActivated";
    public static final String key_windowBackgroundWhiteLinkSelection = "windowBackgroundWhiteLinkSelection";
    public static final String key_windowBackgroundWhiteLinkText = "windowBackgroundWhiteLinkText";
    public static final String key_windowBackgroundWhiteRedText = "windowBackgroundWhiteRedText";
    public static final String key_windowBackgroundWhiteRedText2 = "windowBackgroundWhiteRedText2";
    public static final String key_windowBackgroundWhiteRedText3 = "windowBackgroundWhiteRedText3";
    public static final String key_windowBackgroundWhiteRedText4 = "windowBackgroundWhiteRedText4";
    public static final String key_windowBackgroundWhiteRedText5 = "windowBackgroundWhiteRedText5";
    public static final String key_windowBackgroundWhiteRedText6 = "windowBackgroundWhiteRedText6";
    public static final String key_windowBackgroundWhiteValueText = "windowBackgroundWhiteValueText";
    public static String[] keys_avatar_background = new String[]{key_avatar_backgroundRed, key_avatar_backgroundOrange, key_avatar_backgroundViolet, key_avatar_backgroundGreen, key_avatar_backgroundCyan, key_avatar_backgroundBlue, key_avatar_backgroundPink};
    public static String[] keys_avatar_nameInMessage = new String[]{key_avatar_nameInMessageRed, key_avatar_nameInMessageOrange, key_avatar_nameInMessageViolet, key_avatar_nameInMessageGreen, key_avatar_nameInMessageCyan, key_avatar_nameInMessageBlue, key_avatar_nameInMessagePink};
    private static float lastBrightnessValue = 1.0f;
    private static long lastHolidayCheckTime;
    private static long lastThemeSwitchTime;
    private static Sensor lightSensor;
    private static boolean lightSensorRegistered;
    public static Paint linkSelectionPaint;
    public static Drawable listSelector;
    private static Paint maskPaint = new Paint(1);
    public static Drawable moveUpDrawable;
    private static ArrayList<ThemeInfo> otherThemes = new ArrayList();
    private static ThemeInfo previousTheme;
    public static TextPaint profile_aboutTextPaint;
    public static Drawable profile_verifiedCheckDrawable;
    public static Drawable profile_verifiedDrawable;
    public static int selectedAutoNightType;
    private static int selectedColor;
    private static SensorManager sensorManager;
    private static int serviceMessage2Color;
    private static int serviceMessageColor;
    public static int serviceMessageColorBackup;
    private static int serviceSelectedMessage2Color;
    private static int serviceSelectedMessageColor;
    public static int serviceSelectedMessageColorBackup;
    private static Runnable switchDayBrightnessRunnable = new C22231();
    private static boolean switchDayRunnableScheduled;
    private static Runnable switchNightBrightnessRunnable = new C22242();
    private static boolean switchNightRunnableScheduled;
    private static final Object sync = new Object();
    private static Drawable themedWallpaper;
    private static int themedWallpaperFileOffset;
    public static ArrayList<ThemeInfo> themes = new ArrayList();
    private static HashMap<String, ThemeInfo> themesDict = new HashMap();
    private static Drawable wallpaper;
    private static final Object wallpaperSync = new Object();

    /* renamed from: org.telegram.ui.ActionBar.Theme$1 */
    static class C22231 implements Runnable {
        C22231() {
        }

        public void run() {
            Theme.switchDayRunnableScheduled = false;
            Theme.applyDayNightThemeMaybe(false);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.Theme$2 */
    static class C22242 implements Runnable {
        C22242() {
        }

        public void run() {
            Theme.switchNightRunnableScheduled = false;
            Theme.applyDayNightThemeMaybe(true);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.Theme$3 */
    static class C22253 extends StateListDrawable {
        C22253() {
        }

        public boolean selectDrawable(int i) {
            if (VERSION.SDK_INT >= 21) {
                return super.selectDrawable(i);
            }
            Drawable access$300 = Theme.getStateDrawable(this, i);
            ColorFilter colorFilter = null;
            if (access$300 instanceof BitmapDrawable) {
                colorFilter = ((BitmapDrawable) access$300).getPaint().getColorFilter();
            } else if (access$300 instanceof NinePatchDrawable) {
                colorFilter = ((NinePatchDrawable) access$300).getPaint().getColorFilter();
            }
            boolean selectDrawable = super.selectDrawable(i);
            if (colorFilter != null) {
                access$300.setColorFilter(colorFilter);
            }
            return selectDrawable;
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.Theme$4 */
    static class C22264 extends StateListDrawable {
        C22264() {
        }

        public boolean selectDrawable(int i) {
            if (VERSION.SDK_INT >= 21) {
                return super.selectDrawable(i);
            }
            Drawable access$300 = Theme.getStateDrawable(this, i);
            ColorFilter colorFilter = null;
            if (access$300 instanceof BitmapDrawable) {
                colorFilter = ((BitmapDrawable) access$300).getPaint().getColorFilter();
            } else if (access$300 instanceof NinePatchDrawable) {
                colorFilter = ((NinePatchDrawable) access$300).getPaint().getColorFilter();
            }
            boolean selectDrawable = super.selectDrawable(i);
            if (colorFilter != null) {
                access$300.setColorFilter(colorFilter);
            }
            return selectDrawable;
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.Theme$5 */
    static class C22275 extends StateListDrawable {
        C22275() {
        }

        public boolean selectDrawable(int i) {
            if (VERSION.SDK_INT >= 21) {
                return super.selectDrawable(i);
            }
            Drawable access$300 = Theme.getStateDrawable(this, i);
            ColorFilter colorFilter = null;
            if (access$300 instanceof BitmapDrawable) {
                colorFilter = ((BitmapDrawable) access$300).getPaint().getColorFilter();
            } else if (access$300 instanceof NinePatchDrawable) {
                colorFilter = ((NinePatchDrawable) access$300).getPaint().getColorFilter();
            }
            boolean selectDrawable = super.selectDrawable(i);
            if (colorFilter != null) {
                access$300.setColorFilter(colorFilter);
            }
            return selectDrawable;
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.Theme$7 */
    static class C22297 implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        C22297() {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            float f = sensorEvent.values[0];
            if (f <= 0.0f) {
                f = 0.1f;
            }
            if (!ApplicationLoader.mainInterfacePaused && ApplicationLoader.isScreenOn) {
                if (f > Theme.MAXIMUM_LUX_BREAKPOINT) {
                    Theme.lastBrightnessValue = 1.0f;
                } else {
                    Theme.lastBrightnessValue = ((float) Math.ceil((Math.log((double) f) * 9.932299613952637d) + 27.05900001525879d)) / 100.0f;
                }
                if (Theme.lastBrightnessValue > Theme.autoNightBrighnessThreshold) {
                    if (Theme.switchNightRunnableScheduled) {
                        Theme.switchNightRunnableScheduled = false;
                        AndroidUtilities.cancelRunOnUIThread(Theme.switchNightBrightnessRunnable);
                    }
                    if (!Theme.switchDayRunnableScheduled) {
                        Theme.switchDayRunnableScheduled = true;
                        AndroidUtilities.runOnUIThread(Theme.switchDayBrightnessRunnable, Theme.getAutoNightSwitchThemeDelay());
                    }
                } else if (!MediaController.getInstance().isRecordingOrListeningByProximity()) {
                    if (Theme.switchDayRunnableScheduled) {
                        Theme.switchDayRunnableScheduled = false;
                        AndroidUtilities.cancelRunOnUIThread(Theme.switchDayBrightnessRunnable);
                    }
                    if (!Theme.switchNightRunnableScheduled) {
                        Theme.switchNightRunnableScheduled = true;
                        AndroidUtilities.runOnUIThread(Theme.switchNightBrightnessRunnable, Theme.getAutoNightSwitchThemeDelay());
                    }
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.Theme$AttachCameraDrawable */
    private static class AttachCameraDrawable extends Drawable {
        private Paint paint = new Paint(1);
        private Path segment;

        public int getOpacity() {
            return -2;
        }

        public void setAlpha(int i) {
        }

        public AttachCameraDrawable() {
            float dp = (float) AndroidUtilities.m26dp(54.0f);
            RectF rectF = new RectF(0.0f, 0.0f, dp, dp);
            this.segment = new Path();
            this.segment.moveTo((float) AndroidUtilities.m26dp(23.0f), (float) AndroidUtilities.m26dp(20.0f));
            this.segment.lineTo((float) AndroidUtilities.m26dp(23.0f), 0.0f);
            this.segment.arcTo(rectF, -98.0f, 50.0f, false);
            this.segment.close();
        }

        public void draw(Canvas canvas) {
            canvas.save();
            float dp = (float) AndroidUtilities.m26dp(27.0f);
            canvas.rotate(-90.0f, dp, dp);
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
                    this.paint.setColor(Theme.getColor(Theme.key_chat_attachCameraIcon1));
                } else if (i == 1) {
                    this.paint.setColor(Theme.getColor(Theme.key_chat_attachCameraIcon2));
                } else if (i == 2) {
                    this.paint.setColor(Theme.getColor(Theme.key_chat_attachCameraIcon3));
                } else if (i == 3) {
                    this.paint.setColor(Theme.getColor(Theme.key_chat_attachCameraIcon4));
                } else if (i == 4) {
                    this.paint.setColor(Theme.getColor(Theme.key_chat_attachCameraIcon5));
                } else if (i == 5) {
                    this.paint.setColor(Theme.getColor(Theme.key_chat_attachCameraIcon6));
                }
                canvas.rotate(60.0f, dp, dp);
                canvas.drawPath(this.segment, this.paint);
            }
            canvas.restore();
        }

        public void setColorFilter(ColorFilter colorFilter) {
            invalidateSelf();
        }

        public int getIntrinsicWidth() {
            return AndroidUtilities.m26dp(54.0f);
        }

        public int getIntrinsicHeight() {
            return AndroidUtilities.m26dp(54.0f);
        }

        public int getMinimumWidth() {
            return AndroidUtilities.m26dp(54.0f);
        }

        public int getMinimumHeight() {
            return AndroidUtilities.m26dp(54.0f);
        }
    }

    /* renamed from: org.telegram.ui.ActionBar.Theme$ThemeInfo */
    public static class ThemeInfo {
        public String assetName;
        public String name;
        public String pathToFile;
        public int previewBackgroundColor;
        public int previewInColor;
        public int previewOutColor;
        public int sortIndex;

        public JSONObject getSaveJson() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", this.name);
                jSONObject.put("path", this.pathToFile);
                return jSONObject;
            } catch (Exception e) {
                FileLog.m30e(e);
                return null;
            }
        }

        public String getName() {
            String str = "Default";
            if (str.equals(this.name)) {
                return LocaleController.getString(str, C1067R.string.Default);
            }
            if ("Blue".equals(this.name)) {
                return LocaleController.getString("ThemeBlue", C1067R.string.ThemeBlue);
            }
            if ("Dark".equals(this.name)) {
                return LocaleController.getString("ThemeDark", C1067R.string.ThemeDark);
            }
            if ("Dark Blue".equals(this.name)) {
                return LocaleController.getString("ThemeDarkBlue", C1067R.string.ThemeDarkBlue);
            }
            if ("Graphite".equals(this.name)) {
                return LocaleController.getString("ThemeGraphite", C1067R.string.ThemeGraphite);
            }
            if ("Arctic Blue".equals(this.name)) {
                return LocaleController.getString("ThemeArcticBlue", C1067R.string.ThemeArcticBlue);
            }
            return this.name;
        }

        public boolean isDark() {
            if (!"Dark".equals(this.name)) {
                if (!"Dark Blue".equals(this.name)) {
                    if (!"Graphite".equals(this.name)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean isLight() {
            return this.pathToFile == null && !isDark();
        }

        public static ThemeInfo createWithJson(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            try {
                ThemeInfo themeInfo = new ThemeInfo();
                themeInfo.name = jSONObject.getString("name");
                themeInfo.pathToFile = jSONObject.getString("path");
                return themeInfo;
            } catch (Exception e) {
                FileLog.m30e(e);
                return null;
            }
        }

        public static ThemeInfo createWithString(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            String[] split = str.split("\\|");
            if (split.length != 2) {
                return null;
            }
            ThemeInfo themeInfo = new ThemeInfo();
            themeInfo.name = split[0];
            themeInfo.pathToFile = split[1];
            return themeInfo;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:38:0x0c32 in {5, 17, 18, 20, 23, 25, 26, 27, 32, 33, 37} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public static void createChatResources(android.content.Context r14, boolean r15) {
        /*
        r0 = sync;
        monitor-enter(r0);
        r1 = chat_msgTextPaint;	 Catch:{ all -> 0x0c2f }
        r2 = 1;	 Catch:{ all -> 0x0c2f }
        if (r1 != 0) goto L_0x003d;	 Catch:{ all -> 0x0c2f }
        r1 = new android.text.TextPaint;	 Catch:{ all -> 0x0c2f }
        r1.<init>(r2);	 Catch:{ all -> 0x0c2f }
        chat_msgTextPaint = r1;	 Catch:{ all -> 0x0c2f }
        r1 = new android.text.TextPaint;	 Catch:{ all -> 0x0c2f }
        r1.<init>(r2);	 Catch:{ all -> 0x0c2f }
        chat_msgGameTextPaint = r1;	 Catch:{ all -> 0x0c2f }
        r1 = new android.text.TextPaint;	 Catch:{ all -> 0x0c2f }
        r1.<init>(r2);	 Catch:{ all -> 0x0c2f }
        chat_msgTextPaintOneEmoji = r1;	 Catch:{ all -> 0x0c2f }
        r1 = new android.text.TextPaint;	 Catch:{ all -> 0x0c2f }
        r1.<init>(r2);	 Catch:{ all -> 0x0c2f }
        chat_msgTextPaintTwoEmoji = r1;	 Catch:{ all -> 0x0c2f }
        r1 = new android.text.TextPaint;	 Catch:{ all -> 0x0c2f }
        r1.<init>(r2);	 Catch:{ all -> 0x0c2f }
        chat_msgTextPaintThreeEmoji = r1;	 Catch:{ all -> 0x0c2f }
        r1 = new android.text.TextPaint;	 Catch:{ all -> 0x0c2f }
        r1.<init>(r2);	 Catch:{ all -> 0x0c2f }
        chat_msgBotButtonPaint = r1;	 Catch:{ all -> 0x0c2f }
        r1 = chat_msgBotButtonPaint;	 Catch:{ all -> 0x0c2f }
        r3 = "fonts/rmedium.ttf";	 Catch:{ all -> 0x0c2f }
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);	 Catch:{ all -> 0x0c2f }
        r1.setTypeface(r3);	 Catch:{ all -> 0x0c2f }
        monitor-exit(r0);	 Catch:{ all -> 0x0c2f }
        r0 = 2;
        if (r15 != 0) goto L_0x0a95;
        r1 = chat_msgInDrawable;
        if (r1 != 0) goto L_0x0a95;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_infoPaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_docNamePaint = r1;
        r1 = chat_docNamePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_docBackPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_deleteProgressPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_botProgressPaint = r1;
        r1 = chat_botProgressPaint;
        r3 = android.graphics.Paint.Cap.ROUND;
        r1.setStrokeCap(r3);
        r1 = chat_botProgressPaint;
        r3 = android.graphics.Paint.Style.STROKE;
        r1.setStyle(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_locationTitlePaint = r1;
        r1 = chat_locationTitlePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_locationAddressPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>();
        chat_urlPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>();
        chat_textSearchSelectionPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_radialProgressPaint = r1;
        r1 = chat_radialProgressPaint;
        r3 = android.graphics.Paint.Cap.ROUND;
        r1.setStrokeCap(r3);
        r1 = chat_radialProgressPaint;
        r3 = android.graphics.Paint.Style.STROKE;
        r1.setStyle(r3);
        r1 = chat_radialProgressPaint;
        r3 = -1610612737; // 0xffffffff9fffffff float:-1.0842021E-19 double:NaN;
        r1.setColor(r3);
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_radialProgress2Paint = r1;
        r1 = chat_radialProgress2Paint;
        r3 = android.graphics.Paint.Cap.ROUND;
        r1.setStrokeCap(r3);
        r1 = chat_radialProgress2Paint;
        r3 = android.graphics.Paint.Style.STROKE;
        r1.setStyle(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_audioTimePaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_livePaint = r1;
        r1 = chat_livePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_audioTitlePaint = r1;
        r1 = chat_audioTitlePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_audioPerformerPaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_botButtonPaint = r1;
        r1 = chat_botButtonPaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_contactNamePaint = r1;
        r1 = chat_contactNamePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_contactPhonePaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_durationPaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_gamePaint = r1;
        r1 = chat_gamePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_shipmentPaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_timePaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_adminPaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_namePaint = r1;
        r1 = chat_namePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_forwardNamePaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_replyNamePaint = r1;
        r1 = chat_replyNamePaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_replyTextPaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_instantViewPaint = r1;
        r1 = chat_instantViewPaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_instantViewRectPaint = r1;
        r1 = chat_instantViewRectPaint;
        r3 = android.graphics.Paint.Style.STROKE;
        r1.setStyle(r3);
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_replyLinePaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_msgErrorPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_statusPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_statusRecordPaint = r1;
        r1 = chat_statusRecordPaint;
        r3 = android.graphics.Paint.Style.STROKE;
        r1.setStyle(r3);
        r1 = chat_statusRecordPaint;
        r3 = android.graphics.Paint.Cap.ROUND;
        r1.setStrokeCap(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_actionTextPaint = r1;
        r1 = chat_actionTextPaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_actionBackgroundPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>(r2);
        chat_timeBackgroundPaint = r1;
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_contextResult_titleTextPaint = r1;
        r1 = chat_contextResult_titleTextPaint;
        r3 = "fonts/rmedium.ttf";
        r3 = org.telegram.messenger.AndroidUtilities.getTypeface(r3);
        r1.setTypeface(r3);
        r1 = new android.text.TextPaint;
        r1.<init>(r2);
        chat_contextResult_descriptionTextPaint = r1;
        r1 = new android.graphics.Paint;
        r1.<init>();
        chat_composeBackgroundPaint = r1;
        r1 = r14.getResources();
        r3 = 2131165635; // 0x7f0701c3 float:1.7945493E38 double:1.052935726E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInDrawable = r3;
        r3 = 2131165635; // 0x7f0701c3 float:1.7945493E38 double:1.052935726E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInSelectedDrawable = r3;
        r3 = 2131165907; // 0x7f0702d3 float:1.7946044E38 double:1.05293586E-314;
        r3 = r1.getDrawable(r3);
        chat_msgNoSoundDrawable = r3;
        r3 = 2131165650; // 0x7f0701d2 float:1.7945523E38 double:1.0529357333E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutDrawable = r3;
        r3 = 2131165650; // 0x7f0701d2 float:1.7945523E38 double:1.0529357333E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutSelectedDrawable = r3;
        r3 = 2131165655; // 0x7f0701d7 float:1.7945533E38 double:1.0529357357E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInMediaDrawable = r3;
        r3 = 2131165655; // 0x7f0701d7 float:1.7945533E38 double:1.0529357357E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInMediaSelectedDrawable = r3;
        r3 = 2131165655; // 0x7f0701d7 float:1.7945533E38 double:1.0529357357E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutMediaDrawable = r3;
        r3 = 2131165655; // 0x7f0701d7 float:1.7945533E38 double:1.0529357357E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutMediaSelectedDrawable = r3;
        r3 = 2131165618; // 0x7f0701b2 float:1.7945458E38 double:1.0529357175E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutCheckDrawable = r3;
        r3 = 2131165618; // 0x7f0701b2 float:1.7945458E38 double:1.0529357175E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutCheckSelectedDrawable = r3;
        r3 = 2131165618; // 0x7f0701b2 float:1.7945458E38 double:1.0529357175E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgMediaCheckDrawable = r3;
        r3 = 2131165618; // 0x7f0701b2 float:1.7945458E38 double:1.0529357175E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgStickerCheckDrawable = r3;
        r3 = 2131165632; // 0x7f0701c0 float:1.7945487E38 double:1.0529357244E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutHalfCheckDrawable = r3;
        r3 = 2131165632; // 0x7f0701c0 float:1.7945487E38 double:1.0529357244E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutHalfCheckSelectedDrawable = r3;
        r3 = 2131165632; // 0x7f0701c0 float:1.7945487E38 double:1.0529357244E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgMediaHalfCheckDrawable = r3;
        r3 = 2131165632; // 0x7f0701c0 float:1.7945487E38 double:1.0529357244E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgStickerHalfCheckDrawable = r3;
        r3 = 2131165620; // 0x7f0701b4 float:1.7945462E38 double:1.0529357184E-314;
        r4 = r1.getDrawable(r3);
        r4 = r4.mutate();
        chat_msgOutClockDrawable = r4;
        r4 = r1.getDrawable(r3);
        r4 = r4.mutate();
        chat_msgOutSelectedClockDrawable = r4;
        r4 = r1.getDrawable(r3);
        r4 = r4.mutate();
        chat_msgInClockDrawable = r4;
        r4 = r1.getDrawable(r3);
        r4 = r4.mutate();
        chat_msgInSelectedClockDrawable = r4;
        r4 = r1.getDrawable(r3);
        r4 = r4.mutate();
        chat_msgMediaClockDrawable = r4;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgStickerClockDrawable = r3;
        r3 = 2131165682; // 0x7f0701f2 float:1.7945588E38 double:1.052935749E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInViewsDrawable = r3;
        r3 = 2131165682; // 0x7f0701f2 float:1.7945588E38 double:1.052935749E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInViewsSelectedDrawable = r3;
        r3 = 2131165682; // 0x7f0701f2 float:1.7945588E38 double:1.052935749E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutViewsDrawable = r3;
        r3 = 2131165682; // 0x7f0701f2 float:1.7945588E38 double:1.052935749E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutViewsSelectedDrawable = r3;
        r3 = 2131165682; // 0x7f0701f2 float:1.7945588E38 double:1.052935749E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgMediaViewsDrawable = r3;
        r3 = 2131165682; // 0x7f0701f2 float:1.7945588E38 double:1.052935749E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgStickerViewsDrawable = r3;
        r3 = 2131165610; // 0x7f0701aa float:1.7945442E38 double:1.0529357135E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInMenuDrawable = r3;
        r3 = 2131165610; // 0x7f0701aa float:1.7945442E38 double:1.0529357135E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInMenuSelectedDrawable = r3;
        r3 = 2131165610; // 0x7f0701aa float:1.7945442E38 double:1.0529357135E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutMenuDrawable = r3;
        r3 = 2131165610; // 0x7f0701aa float:1.7945442E38 double:1.0529357135E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutMenuSelectedDrawable = r3;
        r3 = 2131165900; // 0x7f0702cc float:1.794603E38 double:1.052935857E-314;
        r3 = r1.getDrawable(r3);
        chat_msgMediaMenuDrawable = r3;
        r3 = 2131165637; // 0x7f0701c5 float:1.7945497E38 double:1.052935727E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInInstantDrawable = r3;
        r3 = 2131165637; // 0x7f0701c5 float:1.7945497E38 double:1.052935727E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutInstantDrawable = r3;
        r3 = 2131165683; // 0x7f0701f3 float:1.794559E38 double:1.0529357496E-314;
        r3 = r1.getDrawable(r3);
        chat_msgErrorDrawable = r3;
        r3 = 2131165528; // 0x7f070158 float:1.7945276E38 double:1.052935673E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_muteIconDrawable = r3;
        r3 = 2131165447; // 0x7f070107 float:1.7945111E38 double:1.052935633E-314;
        r3 = r1.getDrawable(r3);
        chat_lockIconDrawable = r3;
        r3 = 2131165325; // 0x7f07008d float:1.7944864E38 double:1.0529355727E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgBroadcastDrawable = r3;
        r3 = 2131165325; // 0x7f07008d float:1.7944864E38 double:1.0529355727E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgBroadcastMediaDrawable = r3;
        r3 = 2131165429; // 0x7f0700f5 float:1.7945075E38 double:1.052935624E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInCallDrawable = r3;
        r3 = 2131165429; // 0x7f0700f5 float:1.7945075E38 double:1.052935624E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgInCallSelectedDrawable = r3;
        r3 = 2131165429; // 0x7f0700f5 float:1.7945075E38 double:1.052935624E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutCallDrawable = r3;
        r3 = 2131165429; // 0x7f0700f5 float:1.7945075E38 double:1.052935624E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgOutCallSelectedDrawable = r3;
        r3 = 2131165432; // 0x7f0700f8 float:1.794508E38 double:1.0529356256E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgCallUpGreenDrawable = r3;
        r3 = 2131165435; // 0x7f0700fb float:1.7945087E38 double:1.052935627E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgCallDownRedDrawable = r3;
        r3 = 2131165435; // 0x7f0700fb float:1.7945087E38 double:1.052935627E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgCallDownGreenDrawable = r3;
        r3 = 2131165432; // 0x7f0700f8 float:1.794508E38 double:1.0529356256E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        calllog_msgCallUpRedDrawable = r3;
        r3 = 2131165432; // 0x7f0700f8 float:1.794508E38 double:1.0529356256E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        calllog_msgCallUpGreenDrawable = r3;
        r3 = 2131165435; // 0x7f0700fb float:1.7945087E38 double:1.052935627E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        calllog_msgCallDownRedDrawable = r3;
        r3 = 2131165435; // 0x7f0700fb float:1.7945087E38 double:1.052935627E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        calllog_msgCallDownGreenDrawable = r3;
        r3 = 2131165536; // 0x7f070160 float:1.7945292E38 double:1.052935677E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_msgAvatarLiveLocationDrawable = r3;
        r3 = 2131165316; // 0x7f070084 float:1.7944846E38 double:1.052935568E-314;
        r3 = r1.getDrawable(r3);
        chat_inlineResultFile = r3;
        r3 = 2131165320; // 0x7f070088 float:1.7944854E38 double:1.05293557E-314;
        r3 = r1.getDrawable(r3);
        chat_inlineResultAudio = r3;
        r3 = 2131165319; // 0x7f070087 float:1.7944852E38 double:1.0529355697E-314;
        r3 = r1.getDrawable(r3);
        chat_inlineResultLocation = r3;
        r3 = 2131165551; // 0x7f07016f float:1.7945322E38 double:1.0529356843E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_redLocationIcon = r3;
        r3 = 2131165636; // 0x7f0701c4 float:1.7945495E38 double:1.0529357263E-314;
        r3 = r1.getDrawable(r3);
        chat_msgInShadowDrawable = r3;
        r3 = 2131165651; // 0x7f0701d3 float:1.7945525E38 double:1.052935734E-314;
        r3 = r1.getDrawable(r3);
        chat_msgOutShadowDrawable = r3;
        r3 = 2131165656; // 0x7f0701d8 float:1.7945535E38 double:1.052935736E-314;
        r3 = r1.getDrawable(r3);
        chat_msgInMediaShadowDrawable = r3;
        r3 = 2131165656; // 0x7f0701d8 float:1.7945535E38 double:1.052935736E-314;
        r3 = r1.getDrawable(r3);
        chat_msgOutMediaShadowDrawable = r3;
        r3 = 2131165318; // 0x7f070086 float:1.794485E38 double:1.052935569E-314;
        r3 = r1.getDrawable(r3);
        chat_botLinkDrawalbe = r3;
        r3 = 2131165317; // 0x7f070085 float:1.7944848E38 double:1.0529355687E-314;
        r3 = r1.getDrawable(r3);
        chat_botInlineDrawable = r3;
        r3 = 2131165871; // 0x7f0702af float:1.7945971E38 double:1.0529358425E-314;
        r3 = r1.getDrawable(r3);
        chat_systemDrawable = r3;
        r3 = 2131165407; // 0x7f0700df float:1.794503E38 double:1.052935613E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_contextResult_shadowUnderSwitchDrawable = r3;
        r3 = chat_attachButtonDrawables;
        r4 = new org.telegram.ui.ActionBar.Theme$AttachCameraDrawable;
        r4.<init>();
        r5 = 0;
        r3[r5] = r4;
        r3 = chat_attachButtonDrawables;
        r4 = 1113063424; // 0x42580000 float:54.0 double:5.499263994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r7 = 2131165289; // 0x7f070069 float:1.794479E38 double:1.052935555E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r7);
        r3[r2] = r6;
        r3 = chat_attachButtonDrawables;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r7 = 2131165293; // 0x7f07006d float:1.79448E38 double:1.052935557E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r7);
        r3[r0] = r6;
        r3 = chat_attachButtonDrawables;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r7 = 2131165285; // 0x7f070065 float:1.7944783E38 double:1.052935553E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r7);
        r7 = 3;
        r3[r7] = r6;
        r3 = chat_attachButtonDrawables;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r8 = 2131165288; // 0x7f070068 float:1.7944789E38 double:1.0529355544E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r8);
        r8 = 4;
        r3[r8] = r6;
        r3 = chat_attachButtonDrawables;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r9 = 2131165287; // 0x7f070067 float:1.7944787E38 double:1.052935554E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r9);
        r9 = 5;
        r3[r9] = r6;
        r3 = chat_attachButtonDrawables;
        r6 = 6;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r11 = 2131165290; // 0x7f07006a float:1.7944793E38 double:1.0529355554E-314;
        r10 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r10, r11);
        r3[r6] = r10;
        r3 = chat_attachButtonDrawables;
        r6 = 7;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r11 = 2131165286; // 0x7f070066 float:1.7944785E38 double:1.0529355534E-314;
        r10 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r10, r11);
        r3[r6] = r10;
        r3 = chat_attachButtonDrawables;
        r6 = 8;
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r11 = 2131165292; // 0x7f07006c float:1.7944797E38 double:1.0529355564E-314;
        r10 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r10, r11);
        r3[r6] = r10;
        r3 = chat_attachButtonDrawables;
        r6 = 9;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165291; // 0x7f07006b float:1.7944795E38 double:1.052935556E-314;
        r4 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r4, r10);
        r3[r6] = r4;
        r3 = chat_cornerOuter;
        r4 = 2131165370; // 0x7f0700ba float:1.7944955E38 double:1.052935595E-314;
        r4 = r1.getDrawable(r4);
        r3[r5] = r4;
        r3 = chat_cornerOuter;
        r4 = 2131165371; // 0x7f0700bb float:1.7944957E38 double:1.0529355954E-314;
        r4 = r1.getDrawable(r4);
        r3[r2] = r4;
        r3 = chat_cornerOuter;
        r4 = 2131165369; // 0x7f0700b9 float:1.7944953E38 double:1.0529355944E-314;
        r4 = r1.getDrawable(r4);
        r3[r0] = r4;
        r3 = chat_cornerOuter;
        r4 = 2131165368; // 0x7f0700b8 float:1.7944951E38 double:1.052935594E-314;
        r4 = r1.getDrawable(r4);
        r3[r7] = r4;
        r3 = chat_cornerInner;
        r4 = 2131165367; // 0x7f0700b7 float:1.794495E38 double:1.0529355934E-314;
        r4 = r1.getDrawable(r4);
        r3[r5] = r4;
        r3 = chat_cornerInner;
        r4 = 2131165366; // 0x7f0700b6 float:1.7944947E38 double:1.052935593E-314;
        r4 = r1.getDrawable(r4);
        r3[r2] = r4;
        r3 = chat_cornerInner;
        r4 = 2131165365; // 0x7f0700b5 float:1.7944945E38 double:1.0529355925E-314;
        r4 = r1.getDrawable(r4);
        r3[r0] = r4;
        r3 = chat_cornerInner;
        r4 = 2131165364; // 0x7f0700b4 float:1.7944943E38 double:1.052935592E-314;
        r4 = r1.getDrawable(r4);
        r3[r7] = r4;
        r3 = 2131165820; // 0x7f07027c float:1.7945868E38 double:1.0529358173E-314;
        r3 = r1.getDrawable(r3);
        chat_shareDrawable = r3;
        r3 = 2131165819; // 0x7f07027b float:1.7945866E38 double:1.052935817E-314;
        r3 = r1.getDrawable(r3);
        chat_shareIconDrawable = r3;
        r3 = 2131165377; // 0x7f0700c1 float:1.794497E38 double:1.0529355984E-314;
        r3 = r1.getDrawable(r3);
        chat_replyIconDrawable = r3;
        r3 = 2131165601; // 0x7f0701a1 float:1.7945424E38 double:1.052935709E-314;
        r3 = r1.getDrawable(r3);
        chat_goIconDrawable = r3;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r5];
        r4 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165294; // 0x7f07006e float:1.7944801E38 double:1.0529355574E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r5];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r2];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165295; // 0x7f07006f float:1.7944803E38 double:1.052935558E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r2];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r0];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165294; // 0x7f07006e float:1.7944801E38 double:1.0529355574E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r0];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r7];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165295; // 0x7f07006f float:1.7944803E38 double:1.052935558E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r7];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r8];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165904; // 0x7f0702d0 float:1.7946038E38 double:1.052935859E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r8];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r9];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165905; // 0x7f0702d1 float:1.794604E38 double:1.0529358592E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileMiniStatesDrawable;
        r3 = r3[r9];
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = 2131165905; // 0x7f0702d1 float:1.794604E38 double:1.0529358592E-314;
        r4 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r4, r6);
        r3[r2] = r4;
        r3 = 2131165663; // 0x7f0701df float:1.794555E38 double:1.0529357397E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_fileIcon = r3;
        r3 = 2131165329; // 0x7f070091 float:1.7944872E38 double:1.0529355747E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_flameIcon = r3;
        r3 = 2131165664; // 0x7f0701e0 float:1.7945551E38 double:1.05293574E-314;
        r3 = r1.getDrawable(r3);
        r3 = r3.mutate();
        chat_gifIcon = r3;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r5];
        r4 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165667; // 0x7f0701e3 float:1.7945558E38 double:1.0529357417E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r5];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r2];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165666; // 0x7f0701e2 float:1.7945556E38 double:1.052935741E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r2];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r0];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = 2131165665; // 0x7f0701e1 float:1.7945554E38 double:1.0529357407E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r0];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r7];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r11 = 2131165663; // 0x7f0701df float:1.794555E38 double:1.0529357397E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r11);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r7];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r11);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r8];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r11 = 2131165662; // 0x7f0701de float:1.7945547E38 double:1.052935739E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r11);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r8];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r11);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r9];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r12 = 2131165667; // 0x7f0701e3 float:1.7945558E38 double:1.0529357417E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r12);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r3 = r3[r9];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r12);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 6;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r12 = 2131165666; // 0x7f0701e2 float:1.7945556E38 double:1.052935741E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r12);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 6;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r12);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 7;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 7;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r10);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 8;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r12 = 2131165663; // 0x7f0701df float:1.794555E38 double:1.0529357397E-314;
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r12);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 8;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r12);
        r3[r2] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 9;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r11);
        r3[r5] = r6;
        r3 = chat_fileStatesDrawable;
        r6 = 9;
        r3 = r3[r6];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r11);
        r3[r2] = r6;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r5];
        r6 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r12 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r12, r10);
        r3[r5] = r12;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r5];
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r12 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r12, r10);
        r3[r2] = r12;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r2];
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r12 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r12, r11);
        r3[r5] = r12;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r2];
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r12 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r12, r11);
        r3[r2] = r12;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r0];
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r13 = 2131165664; // 0x7f0701e0 float:1.7945551E38 double:1.05293574E-314;
        r12 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r12, r13);
        r3[r5] = r12;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r0];
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r12 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r12, r13);
        r3[r2] = r12;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r7];
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r13 = 2131165667; // 0x7f0701e3 float:1.7945558E38 double:1.0529357417E-314;
        r12 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r12, r13);
        r3[r5] = r12;
        r3 = chat_photoStatesDrawables;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r12 = 2131165667; // 0x7f0701e3 float:1.7945558E38 double:1.0529357417E-314;
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r12);
        r3[r2] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = r3[r8];
        r3 = r3[r8];
        r8 = 2131165329; // 0x7f070091 float:1.7944872E38 double:1.0529355747E-314;
        r8 = r1.getDrawable(r8);
        r3[r2] = r8;
        r7[r5] = r8;
        r3 = chat_photoStatesDrawables;
        r7 = r3[r9];
        r3 = r3[r9];
        r8 = 2131165356; // 0x7f0700ac float:1.7944927E38 double:1.052935588E-314;
        r8 = r1.getDrawable(r8);
        r3[r2] = r8;
        r7[r5] = r8;
        r3 = chat_photoStatesDrawables;
        r7 = 6;
        r7 = r3[r7];
        r8 = 6;
        r3 = r3[r8];
        r8 = 2131165755; // 0x7f07023b float:1.7945736E38 double:1.052935785E-314;
        r8 = r1.getDrawable(r8);
        r3[r2] = r8;
        r7[r5] = r8;
        r3 = chat_photoStatesDrawables;
        r7 = 7;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r10);
        r3[r5] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 7;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r10);
        r3[r2] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 8;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r11);
        r3[r5] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 8;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r11);
        r3[r2] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 9;
        r3 = r3[r7];
        r7 = 2131165374; // 0x7f0700be float:1.7944963E38 double:1.052935597E-314;
        r7 = r1.getDrawable(r7);
        r7 = r7.mutate();
        r3[r5] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 9;
        r3 = r3[r7];
        r7 = 2131165374; // 0x7f0700be float:1.7944963E38 double:1.052935597E-314;
        r7 = r1.getDrawable(r7);
        r7 = r7.mutate();
        r3[r2] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 10;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r10);
        r3[r5] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 10;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r10);
        r3[r2] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 11;
        r3 = r3[r7];
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r7, r11);
        r3[r5] = r7;
        r3 = chat_photoStatesDrawables;
        r7 = 11;
        r3 = r3[r7];
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r6 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r6, r11);
        r3[r2] = r6;
        r3 = chat_photoStatesDrawables;
        r6 = 12;
        r3 = r3[r6];
        r6 = 2131165374; // 0x7f0700be float:1.7944963E38 double:1.052935597E-314;
        r6 = r1.getDrawable(r6);
        r6 = r6.mutate();
        r3[r5] = r6;
        r3 = chat_photoStatesDrawables;
        r6 = 12;
        r3 = r3[r6];
        r6 = 2131165374; // 0x7f0700be float:1.7944963E38 double:1.052935597E-314;
        r1 = r1.getDrawable(r6);
        r1 = r1.mutate();
        r3[r2] = r1;
        r1 = chat_contactDrawable;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r6 = 2131165621; // 0x7f0701b5 float:1.7945464E38 double:1.052935719E-314;
        r3 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r3, r6);
        r1[r5] = r3;
        r1 = chat_contactDrawable;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r4 = 2131165621; // 0x7f0701b5 float:1.7945464E38 double:1.052935719E-314;
        r3 = org.telegram.p004ui.ActionBar.Theme.createCircleDrawableWithIcon(r3, r4);
        r1[r2] = r3;
        r1 = chat_locationDrawable;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = 2131165641; // 0x7f0701c9 float:1.7945505E38 double:1.052935729E-314;
        r3 = org.telegram.p004ui.ActionBar.Theme.createRoundRectDrawableWithIcon(r3, r4);
        r1[r5] = r3;
        r1 = chat_locationDrawable;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = org.telegram.p004ui.ActionBar.Theme.createRoundRectDrawableWithIcon(r3, r4);
        r1[r2] = r3;
        r14 = r14.getResources();
        r1 = 2131165361; // 0x7f0700b1 float:1.7944937E38 double:1.0529355905E-314;
        r14 = r14.getDrawable(r1);
        chat_composeShadowDrawable = r14;
        r14 = org.telegram.messenger.AndroidUtilities.roundMessageSize;	 Catch:{ Throwable -> 0x0a92 }
        r1 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;	 Catch:{ Throwable -> 0x0a92 }
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);	 Catch:{ Throwable -> 0x0a92 }
        r14 = r14 + r1;	 Catch:{ Throwable -> 0x0a92 }
        r1 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x0a92 }
        r1 = android.graphics.Bitmap.createBitmap(r14, r14, r1);	 Catch:{ Throwable -> 0x0a92 }
        r3 = new android.graphics.Canvas;	 Catch:{ Throwable -> 0x0a92 }
        r3.<init>(r1);	 Catch:{ Throwable -> 0x0a92 }
        r4 = new android.graphics.Paint;	 Catch:{ Throwable -> 0x0a92 }
        r4.<init>(r2);	 Catch:{ Throwable -> 0x0a92 }
        r4.setColor(r5);	 Catch:{ Throwable -> 0x0a92 }
        r6 = android.graphics.Paint.Style.FILL;	 Catch:{ Throwable -> 0x0a92 }
        r4.setStyle(r6);	 Catch:{ Throwable -> 0x0a92 }
        r6 = new android.graphics.PorterDuffXfermode;	 Catch:{ Throwable -> 0x0a92 }
        r7 = android.graphics.PorterDuff.Mode.CLEAR;	 Catch:{ Throwable -> 0x0a92 }
        r6.<init>(r7);	 Catch:{ Throwable -> 0x0a92 }
        r4.setXfermode(r6);	 Catch:{ Throwable -> 0x0a92 }
        r6 = new android.graphics.Paint;	 Catch:{ Throwable -> 0x0a92 }
        r6.<init>(r2);	 Catch:{ Throwable -> 0x0a92 }
        r2 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;	 Catch:{ Throwable -> 0x0a92 }
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);	 Catch:{ Throwable -> 0x0a92 }
        r2 = (float) r2;	 Catch:{ Throwable -> 0x0a92 }
        r7 = 0;	 Catch:{ Throwable -> 0x0a92 }
        r8 = 0;	 Catch:{ Throwable -> 0x0a92 }
        r9 = 1593835520; // 0x5f000000 float:9.223372E18 double:7.874593756E-315;	 Catch:{ Throwable -> 0x0a92 }
        r6.setShadowLayer(r2, r7, r8, r9);	 Catch:{ Throwable -> 0x0a92 }
        if (r5 >= r0) goto L_0x0a87;	 Catch:{ Throwable -> 0x0a92 }
        r2 = r14 / 2;	 Catch:{ Throwable -> 0x0a92 }
        r2 = (float) r2;	 Catch:{ Throwable -> 0x0a92 }
        r7 = r14 / 2;	 Catch:{ Throwable -> 0x0a92 }
        r7 = (float) r7;	 Catch:{ Throwable -> 0x0a92 }
        r8 = org.telegram.messenger.AndroidUtilities.roundMessageSize;	 Catch:{ Throwable -> 0x0a92 }
        r8 = r8 / r0;	 Catch:{ Throwable -> 0x0a92 }
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Throwable -> 0x0a92 }
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);	 Catch:{ Throwable -> 0x0a92 }
        r8 = r8 - r9;	 Catch:{ Throwable -> 0x0a92 }
        r8 = (float) r8;	 Catch:{ Throwable -> 0x0a92 }
        if (r5 != 0) goto L_0x0a80;	 Catch:{ Throwable -> 0x0a92 }
        r9 = r6;	 Catch:{ Throwable -> 0x0a92 }
        goto L_0x0a81;	 Catch:{ Throwable -> 0x0a92 }
        r9 = r4;	 Catch:{ Throwable -> 0x0a92 }
        r3.drawCircle(r2, r7, r8, r9);	 Catch:{ Throwable -> 0x0a92 }
        r5 = r5 + 1;
        goto L_0x0a69;
        r14 = 0;
        r3.setBitmap(r14);	 Catch:{ Exception -> 0x0a8b }
    L_0x0a8b:
        r14 = new android.graphics.drawable.BitmapDrawable;	 Catch:{ Throwable -> 0x0a92 }
        r14.<init>(r1);	 Catch:{ Throwable -> 0x0a92 }
        chat_roundVideoShadow = r14;	 Catch:{ Throwable -> 0x0a92 }
        org.telegram.p004ui.ActionBar.Theme.applyChatTheme(r15);
        r14 = chat_msgTextPaintOneEmoji;
        r1 = 1105199104; // 0x41e00000 float:28.0 double:5.46040909E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = (float) r1;
        r14.setTextSize(r1);
        r14 = chat_msgTextPaintTwoEmoji;
        r1 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = (float) r1;
        r14.setTextSize(r1);
        r14 = chat_msgTextPaintThreeEmoji;
        r1 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = (float) r1;
        r14.setTextSize(r1);
        r14 = chat_msgTextPaint;
        r1 = org.telegram.messenger.SharedConfig.fontSize;
        r1 = (float) r1;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = (float) r1;
        r14.setTextSize(r1);
        r14 = chat_msgGameTextPaint;
        r1 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r1 = (float) r1;
        r14.setTextSize(r1);
        r14 = chat_msgBotButtonPaint;
        r1 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r2 = (float) r2;
        r14.setTextSize(r2);
        if (r15 != 0) goto L_0x0c2e;
        r14 = chat_botProgressPaint;
        if (r14 == 0) goto L_0x0c2e;
        r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r15 = (float) r15;
        r14.setStrokeWidth(r15);
        r14 = chat_infoPaint;
        r15 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r15 = (float) r15;
        r14.setTextSize(r15);
        r14 = chat_docNamePaint;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r15 = (float) r15;
        r14.setTextSize(r15);
        r14 = chat_locationTitlePaint;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r15 = (float) r15;
        r14.setTextSize(r15);
        r14 = chat_locationAddressPaint;
        r15 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_audioTimePaint;
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_livePaint;
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_audioTitlePaint;
        r2 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_audioPerformerPaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_botButtonPaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_contactNamePaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_contactPhonePaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_durationPaint;
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_timePaint;
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_adminPaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_namePaint;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_forwardNamePaint;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_replyNamePaint;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_replyTextPaint;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_gamePaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_shipmentPaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_instantViewPaint;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r2 = (float) r2;
        r14.setTextSize(r2);
        r14 = chat_instantViewRectPaint;
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setStrokeWidth(r2);
        r14 = chat_statusRecordPaint;
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r14.setStrokeWidth(r2);
        r14 = chat_actionTextPaint;
        r2 = 16;
        r3 = org.telegram.messenger.SharedConfig.fontSize;
        r2 = java.lang.Math.max(r2, r3);
        r2 = r2 - r0;
        r0 = (float) r2;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r0 = (float) r0;
        r14.setTextSize(r0);
        r14 = chat_contextResult_titleTextPaint;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
        r0 = (float) r0;
        r14.setTextSize(r0);
        r14 = chat_contextResult_descriptionTextPaint;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r15 = (float) r15;
        r14.setTextSize(r15);
        r14 = chat_radialProgressPaint;
        r15 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r15 = (float) r15;
        r14.setStrokeWidth(r15);
        r14 = chat_radialProgress2Paint;
        r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r15 = (float) r15;
        r14.setStrokeWidth(r15);
        return;
        r14 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0c2f }
        throw r14;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.createChatResources(android.content.Context, boolean):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:67:0x00bc in {4, 18, 28, 30, 31, 32, 33, 34, 36, 39, 40, 43, 45, 47, 49, 50, 56, 58, 59, 63, 65, 66} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private static java.util.HashMap<java.lang.String, java.lang.Integer> getThemeFileValues(java.io.File r14, java.lang.String r15) {
        /*
        r0 = new java.util.HashMap;
        r0.<init>();
        r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r2 = 0;
        r1 = new byte[r1];	 Catch:{ Throwable -> 0x00a2 }
        if (r15 == 0) goto L_0x0010;	 Catch:{ Throwable -> 0x00a2 }
        r14 = org.telegram.p004ui.ActionBar.Theme.getAssetFile(r15);	 Catch:{ Throwable -> 0x00a2 }
        r15 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x00a2 }
        r15.<init>(r14);	 Catch:{ Throwable -> 0x00a2 }
        r14 = -1;
        themedWallpaperFileOffset = r14;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r2 = 0;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r3 = 0;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r4 = 0;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r5 = r15.read(r1);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r5 == r14) goto L_0x0096;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r8 = r3;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r6 = 0;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r7 = 0;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = 1;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r6 >= r5) goto L_0x0086;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r10 = r1[r6];	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r11 = 10;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r10 != r11) goto L_0x0083;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r10 = r6 - r7;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r10 = r10 + r9;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r11 = new java.lang.String;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r12 = r10 + -1;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r11.<init>(r1, r7, r12);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r12 = "WPS";	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r12 = r11.startsWith(r12);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r12 == 0) goto L_0x0044;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r10 = r10 + r8;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        themedWallpaperFileOffset = r10;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r4 = 1;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        goto L_0x0086;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = 61;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = r11.indexOf(r9);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r9 == r14) goto L_0x0081;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r12 = r11.substring(r2, r9);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = r9 + 1;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = r11.substring(r9);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r11 = r9.length();	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r11 <= 0) goto L_0x0072;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r11 = r9.charAt(r2);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r13 = 35;
        if (r11 != r13) goto L_0x0072;
        r9 = android.graphics.Color.parseColor(r9);	 Catch:{ Exception -> 0x0069 }
        goto L_0x007a;
    L_0x0069:
        r9 = org.telegram.messenger.Utilities.parseInt(r9);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = r9.intValue();	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        goto L_0x007a;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = org.telegram.messenger.Utilities.parseInt(r9);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = r9.intValue();	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r9 = java.lang.Integer.valueOf(r9);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r0.put(r12, r9);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r7 = r7 + r10;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r8 = r8 + r10;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r6 = r6 + 1;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        goto L_0x0024;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r3 != r8) goto L_0x0089;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        goto L_0x0096;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r3 = r15.getChannel();	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r5 = (long) r8;	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        r3.position(r5);	 Catch:{ Throwable -> 0x009c, all -> 0x009a }
        if (r4 == 0) goto L_0x0094;
        goto L_0x0096;
        r3 = r8;
        goto L_0x001b;
        r15.close();	 Catch:{ Exception -> 0x00ac }
        goto L_0x00b0;
        r14 = move-exception;
        goto L_0x00b1;
        r14 = move-exception;
        r2 = r15;
        goto L_0x00a3;
        r14 = move-exception;
        r15 = r2;
        goto L_0x00b1;
        r14 = move-exception;
        org.telegram.messenger.FileLog.m30e(r14);	 Catch:{ all -> 0x009f }
        if (r2 == 0) goto L_0x00b0;
        r2.close();	 Catch:{ Exception -> 0x00ac }
        goto L_0x00b0;
        r14 = move-exception;
        org.telegram.messenger.FileLog.m30e(r14);
        return r0;
        if (r15 == 0) goto L_0x00bb;
        r15.close();	 Catch:{ Exception -> 0x00b7 }
        goto L_0x00bb;
        r15 = move-exception;
        org.telegram.messenger.FileLog.m30e(r15);
        throw r14;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.getThemeFileValues(java.io.File, java.lang.String):java.util.HashMap");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:96:0x0151 in {5, 6, 11, 17, 22, 23, 28, 31, 32, 35, 37, 39, 41, 42, 48, 50, 54, 57, 58, 66, 72, 76, 79, 81, 82, 88, 89, 92, 95} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    static /* synthetic */ void lambda$loadWallpaper$3() {
        /*
        r0 = wallpaperSync;
        monitor-enter(r0);
        r1 = org.telegram.messenger.MessagesController.getGlobalMainSettings();	 Catch:{ all -> 0x014e }
        r2 = "overrideThemeWallpaper";	 Catch:{ all -> 0x014e }
        r3 = 0;	 Catch:{ all -> 0x014e }
        r2 = r1.getBoolean(r2, r3);	 Catch:{ all -> 0x014e }
        r4 = "selectedBackgroundMotion";	 Catch:{ all -> 0x014e }
        r4 = r1.getBoolean(r4, r3);	 Catch:{ all -> 0x014e }
        isWallpaperMotion = r4;	 Catch:{ all -> 0x014e }
        r4 = "selectedPattern";	 Catch:{ all -> 0x014e }
        r5 = 0;	 Catch:{ all -> 0x014e }
        r7 = r1.getLong(r4, r5);	 Catch:{ all -> 0x014e }
        r4 = 1;	 Catch:{ all -> 0x014e }
        r9 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1));	 Catch:{ all -> 0x014e }
        if (r9 == 0) goto L_0x0025;	 Catch:{ all -> 0x014e }
        r7 = 1;	 Catch:{ all -> 0x014e }
        goto L_0x0026;	 Catch:{ all -> 0x014e }
        r7 = 0;	 Catch:{ all -> 0x014e }
        isPatternWallpaper = r7;	 Catch:{ all -> 0x014e }
        if (r2 != 0) goto L_0x00b6;	 Catch:{ all -> 0x014e }
        r2 = currentColors;	 Catch:{ all -> 0x014e }
        r7 = "chat_wallpaper";	 Catch:{ all -> 0x014e }
        r2 = r2.get(r7);	 Catch:{ all -> 0x014e }
        r2 = (java.lang.Integer) r2;	 Catch:{ all -> 0x014e }
        if (r2 == 0) goto L_0x0045;	 Catch:{ all -> 0x014e }
        r7 = new android.graphics.drawable.ColorDrawable;	 Catch:{ all -> 0x014e }
        r2 = r2.intValue();	 Catch:{ all -> 0x014e }
        r7.<init>(r2);	 Catch:{ all -> 0x014e }
        wallpaper = r7;	 Catch:{ all -> 0x014e }
        isCustomTheme = r4;	 Catch:{ all -> 0x014e }
        goto L_0x00b6;	 Catch:{ all -> 0x014e }
        r2 = themedWallpaperFileOffset;	 Catch:{ all -> 0x014e }
        if (r2 <= 0) goto L_0x00b6;	 Catch:{ all -> 0x014e }
        r2 = currentTheme;	 Catch:{ all -> 0x014e }
        r2 = r2.pathToFile;	 Catch:{ all -> 0x014e }
        if (r2 != 0) goto L_0x0055;	 Catch:{ all -> 0x014e }
        r2 = currentTheme;	 Catch:{ all -> 0x014e }
        r2 = r2.assetName;	 Catch:{ all -> 0x014e }
        if (r2 == 0) goto L_0x00b6;
        r2 = 0;
        r7 = currentTheme;	 Catch:{ Throwable -> 0x009f }
        r7 = r7.assetName;	 Catch:{ Throwable -> 0x009f }
        if (r7 == 0) goto L_0x0065;	 Catch:{ Throwable -> 0x009f }
        r7 = currentTheme;	 Catch:{ Throwable -> 0x009f }
        r7 = r7.assetName;	 Catch:{ Throwable -> 0x009f }
        r7 = org.telegram.p004ui.ActionBar.Theme.getAssetFile(r7);	 Catch:{ Throwable -> 0x009f }
        goto L_0x006e;	 Catch:{ Throwable -> 0x009f }
        r7 = new java.io.File;	 Catch:{ Throwable -> 0x009f }
        r8 = currentTheme;	 Catch:{ Throwable -> 0x009f }
        r8 = r8.pathToFile;	 Catch:{ Throwable -> 0x009f }
        r7.<init>(r8);	 Catch:{ Throwable -> 0x009f }
        r8 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x009f }
        r8.<init>(r7);	 Catch:{ Throwable -> 0x009f }
        r2 = r8.getChannel();	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        r7 = themedWallpaperFileOffset;	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        r9 = (long) r7;	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        r2.position(r9);	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        r2 = android.graphics.BitmapFactory.decodeStream(r8);	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        if (r2 == 0) goto L_0x008e;	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        r7 = new android.graphics.drawable.BitmapDrawable;	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        r7.<init>(r2);	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        wallpaper = r7;	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        themedWallpaper = r7;	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        isCustomTheme = r4;	 Catch:{ Throwable -> 0x009a, all -> 0x0097 }
        r8.close();	 Catch:{ Exception -> 0x0092 }
        goto L_0x00b6;
        r2 = move-exception;
        org.telegram.messenger.FileLog.m30e(r2);	 Catch:{ all -> 0x014e }
        goto L_0x00b6;
        r1 = move-exception;
        r2 = r8;
        goto L_0x00ab;
        r7 = move-exception;
        r2 = r8;
        goto L_0x00a0;
        r1 = move-exception;
        goto L_0x00ab;
        r7 = move-exception;
        org.telegram.messenger.FileLog.m30e(r7);	 Catch:{ all -> 0x009d }
        if (r2 == 0) goto L_0x00b6;
        r2.close();	 Catch:{ Exception -> 0x00a9 }
        goto L_0x00b6;
        r2 = move-exception;
        goto L_0x0093;
        if (r2 == 0) goto L_0x00b5;
        r2.close();	 Catch:{ Exception -> 0x00b1 }
        goto L_0x00b5;
        r2 = move-exception;
        org.telegram.messenger.FileLog.m30e(r2);	 Catch:{ all -> 0x014e }
        throw r1;	 Catch:{ all -> 0x014e }
        r2 = wallpaper;	 Catch:{ all -> 0x014e }
        if (r2 != 0) goto L_0x0142;
        r7 = org.telegram.p004ui.ActionBar.Theme.getSelectedBackgroundId();	 Catch:{ Throwable -> 0x0131 }
        r2 = "selectedPattern";	 Catch:{ Throwable -> 0x0131 }
        r9 = r1.getLong(r2, r5);	 Catch:{ Throwable -> 0x0131 }
        r2 = "selectedColor";	 Catch:{ Throwable -> 0x0131 }
        r1 = r1.getInt(r2, r3);	 Catch:{ Throwable -> 0x0131 }
        r11 = 1000001; // 0xf4241 float:1.4013E-39 double:4.94066E-318;
        r2 = 2131165299; // 0x7f070073 float:1.7944811E38 double:1.05293556E-314;
        r13 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1));
        if (r13 != 0) goto L_0x00e3;
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0132 }
        r5 = r5.getResources();	 Catch:{ Throwable -> 0x0132 }
        r2 = r5.getDrawable(r2);	 Catch:{ Throwable -> 0x0132 }
        wallpaper = r2;	 Catch:{ Throwable -> 0x0132 }
        isCustomTheme = r3;	 Catch:{ Throwable -> 0x0132 }
        goto L_0x0132;	 Catch:{ Throwable -> 0x0132 }
        r11 = -1;	 Catch:{ Throwable -> 0x0132 }
        r13 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1));	 Catch:{ Throwable -> 0x0132 }
        if (r13 == 0) goto L_0x00f3;	 Catch:{ Throwable -> 0x0132 }
        r11 = -100;	 Catch:{ Throwable -> 0x0132 }
        r13 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1));	 Catch:{ Throwable -> 0x0132 }
        if (r13 < 0) goto L_0x00f3;	 Catch:{ Throwable -> 0x0132 }
        r11 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1));	 Catch:{ Throwable -> 0x0132 }
        if (r11 <= 0) goto L_0x0132;	 Catch:{ Throwable -> 0x0132 }
        if (r1 == 0) goto L_0x0101;	 Catch:{ Throwable -> 0x0132 }
        r7 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1));	 Catch:{ Throwable -> 0x0132 }
        if (r7 != 0) goto L_0x0101;	 Catch:{ Throwable -> 0x0132 }
        r2 = new android.graphics.drawable.ColorDrawable;	 Catch:{ Throwable -> 0x0132 }
        r2.<init>(r1);	 Catch:{ Throwable -> 0x0132 }
        wallpaper = r2;	 Catch:{ Throwable -> 0x0132 }
        goto L_0x0132;	 Catch:{ Throwable -> 0x0132 }
        r5 = new java.io.File;	 Catch:{ Throwable -> 0x0132 }
        r6 = org.telegram.messenger.ApplicationLoader.getFilesDirFixed();	 Catch:{ Throwable -> 0x0132 }
        r7 = "wallpaper.jpg";	 Catch:{ Throwable -> 0x0132 }
        r5.<init>(r6, r7);	 Catch:{ Throwable -> 0x0132 }
        r5.length();	 Catch:{ Throwable -> 0x0132 }
        r6 = r5.exists();	 Catch:{ Throwable -> 0x0132 }
        if (r6 == 0) goto L_0x0122;	 Catch:{ Throwable -> 0x0132 }
        r2 = r5.getAbsolutePath();	 Catch:{ Throwable -> 0x0132 }
        r2 = android.graphics.drawable.Drawable.createFromPath(r2);	 Catch:{ Throwable -> 0x0132 }
        wallpaper = r2;	 Catch:{ Throwable -> 0x0132 }
        isCustomTheme = r4;	 Catch:{ Throwable -> 0x0132 }
        goto L_0x0132;	 Catch:{ Throwable -> 0x0132 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0132 }
        r5 = r5.getResources();	 Catch:{ Throwable -> 0x0132 }
        r2 = r5.getDrawable(r2);	 Catch:{ Throwable -> 0x0132 }
        wallpaper = r2;	 Catch:{ Throwable -> 0x0132 }
        isCustomTheme = r3;	 Catch:{ Throwable -> 0x0132 }
        goto L_0x0132;
        r1 = 0;
    L_0x0132:
        r2 = wallpaper;	 Catch:{ all -> 0x014e }
        if (r2 != 0) goto L_0x0142;	 Catch:{ all -> 0x014e }
        if (r1 != 0) goto L_0x013b;	 Catch:{ all -> 0x014e }
        r1 = -2693905; // 0xffffffffffd6e4ef float:NaN double:NaN;	 Catch:{ all -> 0x014e }
        r2 = new android.graphics.drawable.ColorDrawable;	 Catch:{ all -> 0x014e }
        r2.<init>(r1);	 Catch:{ all -> 0x014e }
        wallpaper = r2;	 Catch:{ all -> 0x014e }
        r1 = wallpaper;	 Catch:{ all -> 0x014e }
        org.telegram.p004ui.ActionBar.Theme.calcBackgroundColor(r1, r4);	 Catch:{ all -> 0x014e }
        r1 = org.telegram.p004ui.ActionBar.C2184-$$Lambda$Theme$TMhHMPfcji-vj3thFJwRUoKkxRA.INSTANCE;	 Catch:{ all -> 0x014e }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r1);	 Catch:{ all -> 0x014e }
        monitor-exit(r0);	 Catch:{ all -> 0x014e }
        return;	 Catch:{ all -> 0x014e }
        r1 = move-exception;	 Catch:{ all -> 0x014e }
        monitor-exit(r0);	 Catch:{ all -> 0x014e }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.lambda$loadWallpaper$3():void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:48:0x0110 in {3, 12, 14, 17, 20, 24, 26, 28, 30, 31, 37, 39, 40, 44, 46, 47} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public static void saveCurrentTheme(java.lang.String r12, boolean r13) {
        /*
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = currentColors;
        r1 = r1.entrySet();
        r1 = r1.iterator();
        r2 = r1.hasNext();
        if (r2 == 0) goto L_0x0036;
        r2 = r1.next();
        r2 = (java.util.Map.Entry) r2;
        r3 = r2.getKey();
        r3 = (java.lang.String) r3;
        r0.append(r3);
        r3 = "=";
        r0.append(r3);
        r2 = r2.getValue();
        r0.append(r2);
        r2 = "\n";
        r0.append(r2);
        goto L_0x000f;
        r1 = new java.io.File;
        r2 = org.telegram.messenger.ApplicationLoader.getFilesDirFixed();
        r1.<init>(r2, r12);
        r2 = 0;
        r3 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00f6 }
        r3.<init>(r1);	 Catch:{ Exception -> 0x00f6 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = org.telegram.messenger.AndroidUtilities.getStringBytes(r0);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r3.write(r0);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = themedWallpaper;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = r0 instanceof android.graphics.drawable.BitmapDrawable;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        if (r0 == 0) goto L_0x009c;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = themedWallpaper;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = (android.graphics.drawable.BitmapDrawable) r0;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = r0.getBitmap();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r2 = 2;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        if (r0 == 0) goto L_0x0091;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r4 = 4;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r5 = new byte[r4];	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r6 = 0;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r7 = 87;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r5[r6] = r7;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r8 = 80;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r9 = 1;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r5[r9] = r8;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r10 = 83;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r5[r2] = r10;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r10 = 3;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r11 = 10;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r5[r10] = r11;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r3.write(r5);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r5 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0.compress(r5, r7, r3);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = 5;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = new byte[r0];	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0[r6] = r11;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0[r9] = r7;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0[r2] = r8;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r5 = 69;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0[r10] = r5;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0[r4] = r11;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r3.write(r0);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        if (r13 == 0) goto L_0x009c;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = themedWallpaper;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        wallpaper = r13;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = wallpaper;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        org.telegram.p004ui.ActionBar.Theme.calcBackgroundColor(r13, r2);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = themesDict;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = r13.get(r12);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = (org.telegram.p004ui.ActionBar.Theme.ThemeInfo) r13;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        if (r13 != 0) goto L_0x00ca;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = new org.telegram.ui.ActionBar.Theme$ThemeInfo;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13.<init>();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = r1.getAbsolutePath();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13.pathToFile = r0;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13.name = r12;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12 = themes;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12.add(r13);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12 = themesDict;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = r13.name;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12.put(r0, r13);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12 = otherThemes;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12.add(r13);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        org.telegram.p004ui.ActionBar.Theme.saveOtherThemes();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        org.telegram.p004ui.ActionBar.Theme.sortThemes();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        currentTheme = r13;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12 = currentTheme;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = currentNightTheme;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        if (r12 == r13) goto L_0x00d6;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12 = currentTheme;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        currentDayTheme = r12;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12 = org.telegram.messenger.MessagesController.getGlobalMainSettings();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12 = r12.edit();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r13 = "theme";	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = currentDayTheme;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r0 = r0.name;	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12.putString(r13, r0);	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r12.commit();	 Catch:{ Exception -> 0x00f0, all -> 0x00ee }
        r3.close();	 Catch:{ Exception -> 0x0100 }
        goto L_0x0104;
        r12 = move-exception;
        goto L_0x0105;
        r12 = move-exception;
        r2 = r3;
        goto L_0x00f7;
        r12 = move-exception;
        r3 = r2;
        goto L_0x0105;
        r12 = move-exception;
        org.telegram.messenger.FileLog.m30e(r12);	 Catch:{ all -> 0x00f3 }
        if (r2 == 0) goto L_0x0104;
        r2.close();	 Catch:{ Exception -> 0x0100 }
        goto L_0x0104;
        r12 = move-exception;
        org.telegram.messenger.FileLog.m30e(r12);
        return;
        if (r3 == 0) goto L_0x010f;
        r3.close();	 Catch:{ Exception -> 0x010b }
        goto L_0x010f;
        r13 = move-exception;
        org.telegram.messenger.FileLog.m30e(r13);
        throw r12;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.saveCurrentTheme(java.lang.String, boolean):void");
    }

    static {
        ThemeInfo createWithJson;
        selectedAutoNightType = 0;
        autoNightBrighnessThreshold = 0.25f;
        autoNightDayStartTime = 1320;
        autoNightDayEndTime = 480;
        autoNightSunsetTime = 1320;
        Integer valueOf = Integer.valueOf(-1);
        autoNightLastSunCheckDay = -1;
        autoNightSunriseTime = 480;
        autoNightCityName = "";
        autoNightLocationLatitude = 10000.0d;
        autoNightLocationLongitude = 10000.0d;
        defaultColors.put(key_dialogBackground, valueOf);
        defaultColors.put(key_dialogBackgroundGray, Integer.valueOf(-986896));
        HashMap hashMap = defaultColors;
        Integer valueOf2 = Integer.valueOf(-14540254);
        hashMap.put(key_dialogTextBlack, valueOf2);
        defaultColors.put(key_dialogTextLink, Integer.valueOf(-14255946));
        defaultColors.put(key_dialogLinkSelection, Integer.valueOf(862104035));
        defaultColors.put(key_dialogTextRed, Integer.valueOf(-3319206));
        defaultColors.put(key_dialogTextRed2, Integer.valueOf(-2213318));
        defaultColors.put(key_dialogTextBlue, Integer.valueOf(-13660983));
        defaultColors.put(key_dialogTextBlue2, Integer.valueOf(-12937771));
        defaultColors.put(key_dialogTextBlue3, Integer.valueOf(-12664327));
        defaultColors.put(key_dialogTextBlue4, Integer.valueOf(-15095832));
        defaultColors.put(key_dialogTextGray, Integer.valueOf(-13333567));
        defaultColors.put(key_dialogTextGray2, Integer.valueOf(-9079435));
        defaultColors.put(key_dialogTextGray3, Integer.valueOf(-6710887));
        defaultColors.put(key_dialogTextGray4, Integer.valueOf(-5000269));
        defaultColors.put(key_dialogTextHint, Integer.valueOf(-6842473));
        defaultColors.put(key_dialogIcon, Integer.valueOf(-9999504));
        defaultColors.put(key_dialogRedIcon, Integer.valueOf(-2011827));
        defaultColors.put(key_dialogGrayLine, Integer.valueOf(-2960686));
        defaultColors.put(key_dialogTopBackground, Integer.valueOf(-9456923));
        defaultColors.put(key_dialogInputField, Integer.valueOf(-2368549));
        defaultColors.put(key_dialogInputFieldActivated, Integer.valueOf(-13129232));
        defaultColors.put(key_dialogCheckboxSquareBackground, Integer.valueOf(-12345121));
        defaultColors.put(key_dialogCheckboxSquareCheck, valueOf);
        defaultColors.put(key_dialogCheckboxSquareUnchecked, Integer.valueOf(-9211021));
        defaultColors.put(key_dialogCheckboxSquareDisabled, Integer.valueOf(-5197648));
        defaultColors.put(key_dialogRadioBackground, Integer.valueOf(-5000269));
        defaultColors.put(key_dialogRadioBackgroundChecked, Integer.valueOf(-13129232));
        defaultColors.put(key_dialogProgressCircle, Integer.valueOf(-11371101));
        defaultColors.put(key_dialogLineProgress, Integer.valueOf(-11371101));
        defaultColors.put(key_dialogLineProgressBackground, Integer.valueOf(-2368549));
        defaultColors.put(key_dialogButton, Integer.valueOf(-11955764));
        defaultColors.put(key_dialogButtonSelector, Integer.valueOf(251658240));
        defaultColors.put(key_dialogScrollGlow, Integer.valueOf(-657673));
        defaultColors.put(key_dialogRoundCheckBox, Integer.valueOf(-11750155));
        defaultColors.put(key_dialogRoundCheckBoxCheck, valueOf);
        defaultColors.put(key_dialogBadgeBackground, Integer.valueOf(-12664327));
        defaultColors.put(key_dialogBadgeText, valueOf);
        defaultColors.put(key_dialogCameraIcon, valueOf);
        defaultColors.put(key_dialog_inlineProgressBackground, Integer.valueOf(-151981323));
        defaultColors.put(key_dialog_inlineProgress, Integer.valueOf(-9735304));
        defaultColors.put(key_dialogSearchBackground, Integer.valueOf(-854795));
        defaultColors.put(key_dialogSearchHint, Integer.valueOf(-6774617));
        defaultColors.put(key_dialogSearchIcon, Integer.valueOf(-6182737));
        defaultColors.put(key_dialogSearchText, valueOf2);
        defaultColors.put(key_dialogFloatingButton, Integer.valueOf(-11750155));
        defaultColors.put(key_dialogFloatingButtonPressed, Integer.valueOf(-11750155));
        defaultColors.put(key_dialogFloatingIcon, valueOf);
        defaultColors.put(key_dialogShadowLine, Integer.valueOf(301989888));
        defaultColors.put(key_windowBackgroundWhite, valueOf);
        defaultColors.put(key_windowBackgroundUnchecked, Integer.valueOf(-6445135));
        defaultColors.put(key_windowBackgroundChecked, Integer.valueOf(-11034919));
        defaultColors.put(key_windowBackgroundCheckText, valueOf);
        defaultColors.put(key_progressCircle, Integer.valueOf(-11371101));
        defaultColors.put(key_windowBackgroundWhiteGrayIcon, Integer.valueOf(-8288629));
        defaultColors.put(key_windowBackgroundWhiteBlueText, Integer.valueOf(-12545331));
        defaultColors.put(key_windowBackgroundWhiteBlueText2, Integer.valueOf(-12937771));
        defaultColors.put(key_windowBackgroundWhiteBlueText3, Integer.valueOf(-14255946));
        defaultColors.put(key_windowBackgroundWhiteBlueText4, Integer.valueOf(-11697229));
        defaultColors.put(key_windowBackgroundWhiteBlueText5, Integer.valueOf(-11759926));
        hashMap = defaultColors;
        Integer valueOf3 = Integer.valueOf(-12940081);
        hashMap.put(key_windowBackgroundWhiteBlueText6, valueOf3);
        defaultColors.put(key_windowBackgroundWhiteBlueText7, Integer.valueOf(-13141330));
        defaultColors.put(key_windowBackgroundWhiteBlueButton, Integer.valueOf(-14776109));
        defaultColors.put(key_windowBackgroundWhiteBlueIcon, Integer.valueOf(-13132315));
        defaultColors.put(key_windowBackgroundWhiteGreenText, Integer.valueOf(-14248148));
        defaultColors.put(key_windowBackgroundWhiteGreenText2, Integer.valueOf(-13129704));
        defaultColors.put(key_windowBackgroundWhiteRedText, Integer.valueOf(-3319206));
        defaultColors.put(key_windowBackgroundWhiteRedText2, Integer.valueOf(-2404015));
        defaultColors.put(key_windowBackgroundWhiteRedText3, Integer.valueOf(-2995895));
        defaultColors.put(key_windowBackgroundWhiteRedText4, Integer.valueOf(-3198928));
        defaultColors.put(key_windowBackgroundWhiteRedText5, Integer.valueOf(-1230535));
        defaultColors.put(key_windowBackgroundWhiteRedText6, Integer.valueOf(-39322));
        defaultColors.put(key_windowBackgroundWhiteGrayText, Integer.valueOf(-8156010));
        defaultColors.put(key_windowBackgroundWhiteGrayText2, Integer.valueOf(-7697782));
        defaultColors.put(key_windowBackgroundWhiteGrayText3, Integer.valueOf(-6710887));
        defaultColors.put(key_windowBackgroundWhiteGrayText4, Integer.valueOf(-8355712));
        defaultColors.put(key_windowBackgroundWhiteGrayText5, Integer.valueOf(-6052957));
        defaultColors.put(key_windowBackgroundWhiteGrayText6, Integer.valueOf(-9079435));
        defaultColors.put(key_windowBackgroundWhiteGrayText7, Integer.valueOf(-3750202));
        defaultColors.put(key_windowBackgroundWhiteGrayText8, Integer.valueOf(-9605774));
        defaultColors.put(key_windowBackgroundWhiteGrayLine, Integer.valueOf(-2368549));
        defaultColors.put(key_windowBackgroundWhiteBlackText, valueOf2);
        defaultColors.put(key_windowBackgroundWhiteHintText, Integer.valueOf(-5723992));
        defaultColors.put(key_windowBackgroundWhiteValueText, Integer.valueOf(-12937771));
        defaultColors.put(key_windowBackgroundWhiteLinkText, Integer.valueOf(-14255946));
        defaultColors.put(key_windowBackgroundWhiteLinkSelection, Integer.valueOf(862104035));
        defaultColors.put(key_windowBackgroundWhiteBlueHeader, Integer.valueOf(-12937771));
        defaultColors.put(key_windowBackgroundWhiteInputField, Integer.valueOf(-2368549));
        defaultColors.put(key_windowBackgroundWhiteInputFieldActivated, Integer.valueOf(-13129232));
        defaultColors.put(key_switchTrack, Integer.valueOf(-5196358));
        defaultColors.put(key_switchTrackChecked, Integer.valueOf(-11358743));
        defaultColors.put(key_switchTrackBlue, Integer.valueOf(-8221031));
        defaultColors.put(key_switchTrackBlueChecked, Integer.valueOf(-12810041));
        defaultColors.put(key_switchTrackBlueThumb, valueOf);
        defaultColors.put(key_switchTrackBlueThumbChecked, valueOf);
        defaultColors.put(key_switchTrackBlueSelector, Integer.valueOf(390089299));
        defaultColors.put(key_switchTrackBlueSelectorChecked, Integer.valueOf(553797505));
        defaultColors.put(key_switch2Track, Integer.valueOf(-688514));
        defaultColors.put(key_switch2TrackChecked, Integer.valueOf(-11358743));
        defaultColors.put(key_checkboxSquareBackground, Integer.valueOf(-12345121));
        defaultColors.put(key_checkboxSquareCheck, valueOf);
        defaultColors.put(key_checkboxSquareUnchecked, Integer.valueOf(-9211021));
        defaultColors.put(key_checkboxSquareDisabled, Integer.valueOf(-5197648));
        defaultColors.put(key_listSelector, Integer.valueOf(251658240));
        defaultColors.put(key_radioBackground, Integer.valueOf(-5000269));
        defaultColors.put(key_radioBackgroundChecked, Integer.valueOf(-13129232));
        defaultColors.put(key_windowBackgroundGray, Integer.valueOf(-986896));
        hashMap = defaultColors;
        Integer valueOf4 = Integer.valueOf(ACTION_BAR_VIDEO_EDIT_COLOR);
        hashMap.put(key_windowBackgroundGrayShadow, valueOf4);
        defaultColors.put(key_emptyListPlaceholder, Integer.valueOf(-6974059));
        defaultColors.put(key_divider, Integer.valueOf(-2500135));
        defaultColors.put(key_graySection, Integer.valueOf(-1117195));
        defaultColors.put(key_graySectionText, Integer.valueOf(-8418927));
        defaultColors.put(key_contextProgressInner1, Integer.valueOf(-4202506));
        defaultColors.put(key_contextProgressOuter1, Integer.valueOf(-13920542));
        defaultColors.put(key_contextProgressInner2, Integer.valueOf(-4202506));
        defaultColors.put(key_contextProgressOuter2, valueOf);
        defaultColors.put(key_contextProgressInner3, Integer.valueOf(-5000269));
        defaultColors.put(key_contextProgressOuter3, valueOf);
        defaultColors.put(key_contextProgressInner4, Integer.valueOf(-3486256));
        hashMap = defaultColors;
        Integer valueOf5 = Integer.valueOf(-13683656);
        hashMap.put(key_contextProgressOuter4, valueOf5);
        defaultColors.put(key_fastScrollActive, Integer.valueOf(-11361317));
        defaultColors.put(key_fastScrollInactive, Integer.valueOf(-3551791));
        defaultColors.put(key_fastScrollText, valueOf);
        defaultColors.put(key_avatar_text, valueOf);
        defaultColors.put(key_avatar_backgroundSaved, Integer.valueOf(-10043398));
        defaultColors.put(key_avatar_backgroundArchived, Integer.valueOf(-5654847));
        defaultColors.put(key_avatar_backgroundArchivedHidden, Integer.valueOf(-3749428));
        defaultColors.put(key_avatar_backgroundRed, Integer.valueOf(-1743531));
        defaultColors.put(key_avatar_backgroundOrange, Integer.valueOf(-881592));
        defaultColors.put(key_avatar_backgroundViolet, Integer.valueOf(-7436818));
        defaultColors.put(key_avatar_backgroundGreen, Integer.valueOf(-8992691));
        defaultColors.put(key_avatar_backgroundCyan, Integer.valueOf(-10502443));
        defaultColors.put(key_avatar_backgroundBlue, Integer.valueOf(-11232035));
        defaultColors.put(key_avatar_backgroundPink, Integer.valueOf(-887654));
        defaultColors.put(key_avatar_backgroundGroupCreateSpanBlue, Integer.valueOf(-1642505));
        defaultColors.put(key_avatar_backgroundInProfileBlue, Integer.valueOf(-11500111));
        defaultColors.put(key_avatar_backgroundActionBarBlue, Integer.valueOf(-10907718));
        defaultColors.put(key_avatar_subtitleInProfileBlue, Integer.valueOf(-2626822));
        defaultColors.put(key_avatar_actionBarSelectorBlue, Integer.valueOf(-11959891));
        defaultColors.put(key_avatar_actionBarIconBlue, valueOf);
        defaultColors.put(key_avatar_nameInMessageRed, Integer.valueOf(-3516848));
        defaultColors.put(key_avatar_nameInMessageOrange, Integer.valueOf(-2589911));
        defaultColors.put(key_avatar_nameInMessageViolet, Integer.valueOf(-11627828));
        defaultColors.put(key_avatar_nameInMessageGreen, Integer.valueOf(-11488718));
        defaultColors.put(key_avatar_nameInMessageCyan, Integer.valueOf(-13132104));
        defaultColors.put(key_avatar_nameInMessageBlue, Integer.valueOf(-11627828));
        defaultColors.put(key_avatar_nameInMessagePink, Integer.valueOf(-11627828));
        defaultColors.put(key_actionBarDefault, Integer.valueOf(-11371101));
        defaultColors.put(key_actionBarDefaultIcon, valueOf);
        defaultColors.put(key_actionBarActionModeDefault, valueOf);
        defaultColors.put(key_actionBarActionModeDefaultTop, Integer.valueOf(268435456));
        defaultColors.put(key_actionBarActionModeDefaultIcon, Integer.valueOf(-9999761));
        defaultColors.put(key_actionBarDefaultTitle, valueOf);
        defaultColors.put(key_actionBarDefaultSubtitle, Integer.valueOf(-2758409));
        defaultColors.put(key_actionBarDefaultSelector, Integer.valueOf(-12554860));
        defaultColors.put(key_actionBarWhiteSelector, Integer.valueOf(ACTION_BAR_AUDIO_SELECTOR_COLOR));
        defaultColors.put(key_actionBarDefaultSearch, valueOf);
        defaultColors.put(key_actionBarDefaultSearchPlaceholder, Integer.valueOf(-1996488705));
        defaultColors.put(key_actionBarDefaultSubmenuItem, valueOf2);
        defaultColors.put(key_actionBarDefaultSubmenuItemIcon, Integer.valueOf(-9999504));
        defaultColors.put(key_actionBarDefaultSubmenuBackground, valueOf);
        defaultColors.put(key_actionBarActionModeDefaultSelector, Integer.valueOf(-986896));
        defaultColors.put(key_actionBarTabActiveText, valueOf);
        defaultColors.put(key_actionBarTabUnactiveText, Integer.valueOf(-2758409));
        defaultColors.put(key_actionBarTabLine, valueOf);
        defaultColors.put(key_actionBarTabSelector, Integer.valueOf(-12554860));
        defaultColors.put(key_actionBarDefaultArchived, Integer.valueOf(-9471353));
        defaultColors.put(key_actionBarDefaultArchivedSelector, Integer.valueOf(-10590350));
        defaultColors.put(key_actionBarDefaultArchivedIcon, valueOf);
        defaultColors.put(key_actionBarDefaultArchivedTitle, valueOf);
        defaultColors.put(key_actionBarDefaultArchivedSearch, valueOf);
        defaultColors.put(key_actionBarDefaultArchivedSearchPlaceholder, Integer.valueOf(-1996488705));
        defaultColors.put(key_chats_onlineCircle, Integer.valueOf(-11810020));
        defaultColors.put(key_chats_unreadCounter, Integer.valueOf(-11613090));
        defaultColors.put(key_chats_unreadCounterMuted, Integer.valueOf(-3749428));
        defaultColors.put(key_chats_unreadCounterText, valueOf);
        defaultColors.put(key_chats_archiveBackground, Integer.valueOf(-10049056));
        defaultColors.put(key_chats_archivePinBackground, Integer.valueOf(-6313293));
        defaultColors.put(key_chats_archiveIcon, valueOf);
        defaultColors.put(key_chats_archiveText, valueOf);
        defaultColors.put(key_chats_name, valueOf2);
        defaultColors.put(key_chats_nameArchived, Integer.valueOf(-11382190));
        defaultColors.put(key_chats_secretName, Integer.valueOf(-16734706));
        defaultColors.put(key_chats_secretIcon, Integer.valueOf(-15093466));
        defaultColors.put(key_chats_nameIcon, Integer.valueOf(-14408668));
        defaultColors.put(key_chats_pinnedIcon, Integer.valueOf(-5723992));
        defaultColors.put(key_chats_message, Integer.valueOf(-7631473));
        defaultColors.put(key_chats_messageArchived, Integer.valueOf(-7237231));
        defaultColors.put(key_chats_message_threeLines, Integer.valueOf(-7434095));
        defaultColors.put(key_chats_draft, Integer.valueOf(-2274503));
        defaultColors.put(key_chats_nameMessage, Integer.valueOf(-12812624));
        defaultColors.put(key_chats_nameMessageArchived, Integer.valueOf(-7631473));
        defaultColors.put(key_chats_nameMessage_threeLines, Integer.valueOf(-12434359));
        defaultColors.put(key_chats_nameMessageArchived_threeLines, Integer.valueOf(-10592674));
        defaultColors.put(key_chats_attachMessage, Integer.valueOf(-12812624));
        defaultColors.put(key_chats_actionMessage, Integer.valueOf(-12812624));
        defaultColors.put(key_chats_date, Integer.valueOf(-6973028));
        defaultColors.put(key_chats_pinnedOverlay, Integer.valueOf(134217728));
        defaultColors.put(key_chats_tabletSelectedOverlay, Integer.valueOf(251658240));
        defaultColors.put(key_chats_sentCheck, Integer.valueOf(-12146122));
        defaultColors.put(key_chats_sentClock, Integer.valueOf(-9061026));
        defaultColors.put(key_chats_sentError, Integer.valueOf(-2796974));
        defaultColors.put(key_chats_sentErrorIcon, valueOf);
        defaultColors.put(key_chats_verifiedBackground, Integer.valueOf(-13391642));
        defaultColors.put(key_chats_verifiedCheck, valueOf);
        defaultColors.put(key_chats_muteIcon, Integer.valueOf(-4341308));
        defaultColors.put(key_chats_mentionIcon, valueOf);
        defaultColors.put(key_chats_menuBackground, valueOf);
        defaultColors.put(key_chats_menuItemText, Integer.valueOf(-12303292));
        defaultColors.put(key_chats_menuItemCheck, Integer.valueOf(-10907718));
        defaultColors.put(key_chats_menuItemIcon, Integer.valueOf(-7827048));
        defaultColors.put(key_chats_menuName, valueOf);
        defaultColors.put(key_chats_menuPhone, valueOf);
        defaultColors.put(key_chats_menuPhoneCats, Integer.valueOf(-4004353));
        defaultColors.put(key_chats_menuCloud, valueOf);
        defaultColors.put(key_chats_menuCloudBackgroundCats, Integer.valueOf(-12420183));
        defaultColors.put(key_chats_actionIcon, valueOf);
        defaultColors.put(key_chats_actionBackground, Integer.valueOf(-10114592));
        defaultColors.put(key_chats_actionPressedBackground, Integer.valueOf(-11100714));
        defaultColors.put(key_chats_actionUnreadIcon, Integer.valueOf(-9211021));
        defaultColors.put(key_chats_actionUnreadBackground, valueOf);
        defaultColors.put(key_chats_actionUnreadPressedBackground, Integer.valueOf(-855310));
        defaultColors.put(key_chats_menuTopBackgroundCats, Integer.valueOf(-10907718));
        defaultColors.put(key_chat_attachCameraIcon1, Integer.valueOf(-33488));
        defaultColors.put(key_chat_attachCameraIcon2, Integer.valueOf(-1353648));
        defaultColors.put(key_chat_attachCameraIcon3, Integer.valueOf(-12342798));
        defaultColors.put(key_chat_attachCameraIcon4, Integer.valueOf(-4958752));
        defaultColors.put(key_chat_attachCameraIcon5, Integer.valueOf(-10366879));
        defaultColors.put(key_chat_attachCameraIcon6, Integer.valueOf(-81627));
        defaultColors.put(key_chat_attachMediaBanBackground, Integer.valueOf(-12171706));
        defaultColors.put(key_chat_attachMediaBanText, valueOf);
        defaultColors.put(key_chat_attachGalleryBackground, Integer.valueOf(-5997863));
        defaultColors.put(key_chat_attachGalleryIcon, valueOf);
        defaultColors.put(key_chat_attachVideoBackground, Integer.valueOf(-1871495));
        defaultColors.put(key_chat_attachVideoIcon, valueOf);
        defaultColors.put(key_chat_attachAudioBackground, Integer.valueOf(-620719));
        defaultColors.put(key_chat_attachAudioIcon, valueOf);
        defaultColors.put(key_chat_attachFileBackground, Integer.valueOf(-13328140));
        defaultColors.put(key_chat_attachFileIcon, valueOf);
        defaultColors.put(key_chat_attachContactBackground, Integer.valueOf(-12664838));
        defaultColors.put(key_chat_attachContactIcon, valueOf);
        defaultColors.put(key_chat_attachLocationBackground, Integer.valueOf(-12597126));
        defaultColors.put(key_chat_attachLocationIcon, valueOf);
        defaultColors.put(key_chat_attachHideBackground, Integer.valueOf(-5330248));
        defaultColors.put(key_chat_attachHideIcon, valueOf);
        defaultColors.put(key_chat_attachSendBackground, Integer.valueOf(-12664838));
        defaultColors.put(key_chat_attachPollBackground, Integer.valueOf(-670899));
        defaultColors.put(key_chat_attachPollIcon, valueOf);
        defaultColors.put(key_chat_status, Integer.valueOf(-2758409));
        defaultColors.put(key_chat_inGreenCall, Integer.valueOf(-16725933));
        defaultColors.put(key_chat_inRedCall, Integer.valueOf(-47032));
        defaultColors.put(key_chat_outGreenCall, Integer.valueOf(-16725933));
        defaultColors.put(key_chat_attachSendIcon, valueOf);
        defaultColors.put(key_chat_shareBackground, Integer.valueOf(1718783910));
        defaultColors.put(key_chat_shareBackgroundSelected, Integer.valueOf(-1720545370));
        defaultColors.put(key_chat_lockIcon, valueOf);
        defaultColors.put(key_chat_muteIcon, Integer.valueOf(-5124893));
        defaultColors.put(key_chat_inBubble, valueOf);
        defaultColors.put(key_chat_inBubbleSelected, Integer.valueOf(-1247235));
        defaultColors.put(key_chat_inBubbleShadow, Integer.valueOf(-14862509));
        defaultColors.put(key_chat_outBubble, Integer.valueOf(-1048610));
        defaultColors.put(key_chat_outBubbleSelected, Integer.valueOf(-2492475));
        defaultColors.put(key_chat_outBubbleShadow, Integer.valueOf(-14781172));
        defaultColors.put(key_chat_inMediaIcon, valueOf);
        defaultColors.put(key_chat_inMediaIconSelected, Integer.valueOf(-1050370));
        defaultColors.put(key_chat_outMediaIcon, Integer.valueOf(-1048610));
        defaultColors.put(key_chat_outMediaIconSelected, Integer.valueOf(-1967921));
        defaultColors.put(key_chat_messageTextIn, valueOf4);
        defaultColors.put(key_chat_messageTextOut, valueOf4);
        defaultColors.put(key_chat_messageLinkIn, Integer.valueOf(-14255946));
        defaultColors.put(key_chat_messageLinkOut, Integer.valueOf(-14255946));
        defaultColors.put(key_chat_serviceText, valueOf);
        defaultColors.put(key_chat_serviceLink, valueOf);
        defaultColors.put(key_chat_serviceIcon, valueOf);
        defaultColors.put(key_chat_mediaTimeBackground, Integer.valueOf(1711276032));
        defaultColors.put(key_chat_outSentCheck, Integer.valueOf(-10637232));
        defaultColors.put(key_chat_outSentCheckSelected, Integer.valueOf(-10637232));
        defaultColors.put(key_chat_outSentClock, Integer.valueOf(-9061026));
        defaultColors.put(key_chat_outSentClockSelected, Integer.valueOf(-9061026));
        hashMap = defaultColors;
        Integer valueOf6 = Integer.valueOf(-6182221);
        hashMap.put(key_chat_inSentClock, valueOf6);
        defaultColors.put(key_chat_inSentClockSelected, Integer.valueOf(-7094838));
        defaultColors.put(key_chat_mediaSentCheck, valueOf);
        defaultColors.put(key_chat_mediaSentClock, valueOf);
        defaultColors.put(key_chat_inViews, valueOf6);
        defaultColors.put(key_chat_inViewsSelected, Integer.valueOf(-7094838));
        defaultColors.put(key_chat_outViews, Integer.valueOf(-9522601));
        defaultColors.put(key_chat_outViewsSelected, Integer.valueOf(-9522601));
        defaultColors.put(key_chat_mediaViews, valueOf);
        defaultColors.put(key_chat_inMenu, Integer.valueOf(-4801083));
        defaultColors.put(key_chat_inMenuSelected, Integer.valueOf(-6766130));
        defaultColors.put(key_chat_outMenu, Integer.valueOf(-7221634));
        defaultColors.put(key_chat_outMenuSelected, Integer.valueOf(-7221634));
        defaultColors.put(key_chat_mediaMenu, valueOf);
        hashMap = defaultColors;
        Integer valueOf7 = Integer.valueOf(-11162801);
        hashMap.put(key_chat_outInstant, valueOf7);
        defaultColors.put(key_chat_outInstantSelected, Integer.valueOf(-12019389));
        defaultColors.put(key_chat_inInstant, valueOf3);
        defaultColors.put(key_chat_inInstantSelected, Integer.valueOf(-13600331));
        defaultColors.put(key_chat_sentError, Integer.valueOf(-2411211));
        defaultColors.put(key_chat_sentErrorIcon, valueOf);
        defaultColors.put(key_chat_selectedBackground, Integer.valueOf(671781104));
        defaultColors.put(key_chat_previewDurationText, valueOf);
        defaultColors.put(key_chat_previewGameText, valueOf);
        defaultColors.put(key_chat_inPreviewInstantText, valueOf3);
        defaultColors.put(key_chat_outPreviewInstantText, valueOf7);
        defaultColors.put(key_chat_inPreviewInstantSelectedText, Integer.valueOf(-13600331));
        defaultColors.put(key_chat_outPreviewInstantSelectedText, Integer.valueOf(-12019389));
        defaultColors.put(key_chat_secretTimeText, Integer.valueOf(-1776928));
        defaultColors.put(key_chat_stickerNameText, valueOf);
        defaultColors.put(key_chat_botButtonText, valueOf);
        defaultColors.put(key_chat_botProgress, valueOf);
        defaultColors.put(key_chat_inForwardedNameText, Integer.valueOf(-13072697));
        defaultColors.put(key_chat_outForwardedNameText, valueOf7);
        defaultColors.put(key_chat_inViaBotNameText, valueOf3);
        defaultColors.put(key_chat_outViaBotNameText, valueOf7);
        defaultColors.put(key_chat_stickerViaBotNameText, valueOf);
        defaultColors.put(key_chat_inReplyLine, Integer.valueOf(-10903592));
        defaultColors.put(key_chat_outReplyLine, Integer.valueOf(-9520791));
        defaultColors.put(key_chat_stickerReplyLine, valueOf);
        defaultColors.put(key_chat_inReplyNameText, valueOf3);
        defaultColors.put(key_chat_outReplyNameText, valueOf7);
        defaultColors.put(key_chat_stickerReplyNameText, valueOf);
        defaultColors.put(key_chat_inReplyMessageText, valueOf4);
        defaultColors.put(key_chat_outReplyMessageText, valueOf4);
        defaultColors.put(key_chat_inReplyMediaMessageText, valueOf6);
        defaultColors.put(key_chat_outReplyMediaMessageText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_inReplyMediaMessageSelectedText, Integer.valueOf(-7752511));
        defaultColors.put(key_chat_outReplyMediaMessageSelectedText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_stickerReplyMessageText, valueOf);
        defaultColors.put(key_chat_inPreviewLine, Integer.valueOf(-9390872));
        defaultColors.put(key_chat_outPreviewLine, Integer.valueOf(-7812741));
        defaultColors.put(key_chat_inSiteNameText, valueOf3);
        defaultColors.put(key_chat_outSiteNameText, valueOf7);
        defaultColors.put(key_chat_inContactNameText, Integer.valueOf(-11625772));
        defaultColors.put(key_chat_outContactNameText, valueOf7);
        defaultColors.put(key_chat_inContactPhoneText, valueOf5);
        defaultColors.put(key_chat_inContactPhoneSelectedText, valueOf5);
        defaultColors.put(key_chat_outContactPhoneText, Integer.valueOf(-13286860));
        defaultColors.put(key_chat_outContactPhoneSelectedText, Integer.valueOf(-13286860));
        defaultColors.put(key_chat_mediaProgress, valueOf);
        defaultColors.put(key_chat_inAudioProgress, valueOf);
        defaultColors.put(key_chat_outAudioProgress, Integer.valueOf(-1048610));
        defaultColors.put(key_chat_inAudioSelectedProgress, Integer.valueOf(-1050370));
        defaultColors.put(key_chat_outAudioSelectedProgress, Integer.valueOf(-1967921));
        defaultColors.put(key_chat_mediaTimeText, valueOf);
        defaultColors.put(key_chat_inTimeText, valueOf6);
        defaultColors.put(key_chat_outTimeText, Integer.valueOf(-9391780));
        defaultColors.put(key_chat_adminText, Integer.valueOf(-4143413));
        defaultColors.put(key_chat_adminSelectedText, Integer.valueOf(-7752511));
        defaultColors.put(key_chat_inTimeSelectedText, Integer.valueOf(-7752511));
        defaultColors.put(key_chat_outTimeSelectedText, Integer.valueOf(-9391780));
        defaultColors.put(key_chat_inAudioPerformerText, valueOf5);
        defaultColors.put(key_chat_inAudioPerformerSelectedText, valueOf5);
        defaultColors.put(key_chat_outAudioPerformerText, Integer.valueOf(-13286860));
        defaultColors.put(key_chat_outAudioPerformerSelectedText, Integer.valueOf(-13286860));
        defaultColors.put(key_chat_inAudioTitleText, Integer.valueOf(-11625772));
        defaultColors.put(key_chat_outAudioTitleText, valueOf7);
        defaultColors.put(key_chat_inAudioDurationText, valueOf6);
        defaultColors.put(key_chat_outAudioDurationText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_inAudioDurationSelectedText, Integer.valueOf(-7752511));
        defaultColors.put(key_chat_outAudioDurationSelectedText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_inAudioSeekbar, Integer.valueOf(-1774864));
        defaultColors.put(key_chat_inAudioCacheSeekbar, Integer.valueOf(1071966960));
        defaultColors.put(key_chat_outAudioSeekbar, Integer.valueOf(-4463700));
        defaultColors.put(key_chat_outAudioCacheSeekbar, Integer.valueOf(1069278124));
        defaultColors.put(key_chat_inAudioSeekbarSelected, Integer.valueOf(-4399384));
        defaultColors.put(key_chat_outAudioSeekbarSelected, Integer.valueOf(-5644906));
        defaultColors.put(key_chat_inAudioSeekbarFill, Integer.valueOf(-9259544));
        defaultColors.put(key_chat_outAudioSeekbarFill, Integer.valueOf(-8863118));
        defaultColors.put(key_chat_inVoiceSeekbar, Integer.valueOf(-2169365));
        defaultColors.put(key_chat_outVoiceSeekbar, Integer.valueOf(-4463700));
        defaultColors.put(key_chat_inVoiceSeekbarSelected, Integer.valueOf(-4399384));
        defaultColors.put(key_chat_outVoiceSeekbarSelected, Integer.valueOf(-5644906));
        defaultColors.put(key_chat_inVoiceSeekbarFill, Integer.valueOf(-9259544));
        defaultColors.put(key_chat_outVoiceSeekbarFill, Integer.valueOf(-8863118));
        defaultColors.put(key_chat_inFileProgress, Integer.valueOf(-1314571));
        defaultColors.put(key_chat_outFileProgress, Integer.valueOf(-2427453));
        defaultColors.put(key_chat_inFileProgressSelected, Integer.valueOf(-3413258));
        defaultColors.put(key_chat_outFileProgressSelected, Integer.valueOf(-3806041));
        defaultColors.put(key_chat_inFileNameText, Integer.valueOf(-11625772));
        defaultColors.put(key_chat_outFileNameText, valueOf7);
        defaultColors.put(key_chat_inFileInfoText, valueOf6);
        defaultColors.put(key_chat_outFileInfoText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_inFileInfoSelectedText, Integer.valueOf(-7752511));
        defaultColors.put(key_chat_outFileInfoSelectedText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_inFileBackground, Integer.valueOf(-1314571));
        defaultColors.put(key_chat_outFileBackground, Integer.valueOf(-2427453));
        defaultColors.put(key_chat_inFileBackgroundSelected, Integer.valueOf(-3413258));
        defaultColors.put(key_chat_outFileBackgroundSelected, Integer.valueOf(-3806041));
        defaultColors.put(key_chat_inVenueInfoText, valueOf6);
        defaultColors.put(key_chat_outVenueInfoText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_inVenueInfoSelectedText, Integer.valueOf(-7752511));
        defaultColors.put(key_chat_outVenueInfoSelectedText, Integer.valueOf(-10112933));
        defaultColors.put(key_chat_mediaInfoText, valueOf);
        defaultColors.put(key_chat_linkSelectBackground, Integer.valueOf(862104035));
        defaultColors.put(key_chat_textSelectBackground, Integer.valueOf(1717742051));
        defaultColors.put(key_chat_emojiPanelBackground, Integer.valueOf(-986379));
        defaultColors.put(key_chat_emojiPanelBadgeBackground, Integer.valueOf(-11688214));
        defaultColors.put(key_chat_emojiPanelBadgeText, valueOf);
        defaultColors.put(key_chat_emojiSearchBackground, Integer.valueOf(-1709586));
        defaultColors.put(key_chat_emojiSearchIcon, Integer.valueOf(-7036497));
        defaultColors.put(key_chat_emojiPanelShadowLine, Integer.valueOf(301989888));
        defaultColors.put(key_chat_emojiPanelEmptyText, Integer.valueOf(-7038047));
        defaultColors.put(key_chat_emojiPanelIcon, Integer.valueOf(-6445909));
        defaultColors.put(key_chat_emojiBottomPanelIcon, Integer.valueOf(-7564905));
        defaultColors.put(key_chat_emojiPanelIconSelected, Integer.valueOf(-13920286));
        defaultColors.put(key_chat_emojiPanelStickerPackSelector, Integer.valueOf(-1907225));
        defaultColors.put(key_chat_emojiPanelStickerPackSelectorLine, Integer.valueOf(-11097104));
        defaultColors.put(key_chat_emojiPanelBackspace, Integer.valueOf(-7564905));
        defaultColors.put(key_chat_emojiPanelMasksIcon, valueOf);
        defaultColors.put(key_chat_emojiPanelMasksIconSelected, Integer.valueOf(-10305560));
        defaultColors.put(key_chat_emojiPanelTrendingTitle, valueOf2);
        defaultColors.put(key_chat_emojiPanelStickerSetName, Integer.valueOf(-8221804));
        defaultColors.put(key_chat_emojiPanelStickerSetNameHighlight, Integer.valueOf(-14184997));
        defaultColors.put(key_chat_emojiPanelStickerSetNameIcon, Integer.valueOf(-5130564));
        defaultColors.put(key_chat_emojiPanelTrendingDescription, Integer.valueOf(-7697782));
        defaultColors.put(key_chat_botKeyboardButtonText, Integer.valueOf(-13220017));
        defaultColors.put(key_chat_botKeyboardButtonBackground, Integer.valueOf(-1775639));
        defaultColors.put(key_chat_botKeyboardButtonBackgroundPressed, Integer.valueOf(-3354156));
        defaultColors.put(key_chat_unreadMessagesStartArrowIcon, Integer.valueOf(-6113849));
        defaultColors.put(key_chat_unreadMessagesStartText, Integer.valueOf(-11102772));
        defaultColors.put(key_chat_unreadMessagesStartBackground, valueOf);
        defaultColors.put(key_chat_inFileIcon, Integer.valueOf(-6113849));
        defaultColors.put(key_chat_inFileSelectedIcon, Integer.valueOf(-7883067));
        defaultColors.put(key_chat_outFileIcon, Integer.valueOf(-8011912));
        defaultColors.put(key_chat_outFileSelectedIcon, Integer.valueOf(-8011912));
        defaultColors.put(key_chat_inLocationBackground, Integer.valueOf(-1314571));
        defaultColors.put(key_chat_inLocationIcon, Integer.valueOf(-6113849));
        defaultColors.put(key_chat_outLocationBackground, Integer.valueOf(-2427453));
        defaultColors.put(key_chat_outLocationIcon, Integer.valueOf(-7880840));
        defaultColors.put(key_chat_inContactBackground, Integer.valueOf(-9259544));
        defaultColors.put(key_chat_inContactIcon, valueOf);
        defaultColors.put(key_chat_outContactBackground, Integer.valueOf(-8863118));
        defaultColors.put(key_chat_outContactIcon, Integer.valueOf(-1048610));
        defaultColors.put(key_chat_outBroadcast, Integer.valueOf(-12146122));
        defaultColors.put(key_chat_mediaBroadcast, valueOf);
        defaultColors.put(key_chat_searchPanelIcons, Integer.valueOf(-9999761));
        defaultColors.put(key_chat_searchPanelText, Integer.valueOf(-9999761));
        defaultColors.put(key_chat_secretChatStatusText, Integer.valueOf(-8421505));
        defaultColors.put(key_chat_fieldOverlayText, valueOf3);
        defaultColors.put(key_chat_stickersHintPanel, valueOf);
        defaultColors.put(key_chat_replyPanelIcons, Integer.valueOf(-11032346));
        defaultColors.put(key_chat_replyPanelClose, Integer.valueOf(-7432805));
        defaultColors.put(key_chat_replyPanelName, valueOf3);
        defaultColors.put(key_chat_replyPanelMessage, valueOf2);
        defaultColors.put(key_chat_replyPanelLine, Integer.valueOf(-1513240));
        defaultColors.put(key_chat_messagePanelBackground, valueOf);
        defaultColors.put(key_chat_messagePanelText, valueOf4);
        defaultColors.put(key_chat_messagePanelHint, Integer.valueOf(-5985101));
        defaultColors.put(key_chat_messagePanelShadow, valueOf4);
        defaultColors.put(key_chat_messagePanelIcons, Integer.valueOf(-7432805));
        defaultColors.put(key_chat_recordedVoicePlayPause, valueOf);
        defaultColors.put(key_chat_recordedVoicePlayPausePressed, Integer.valueOf(-2495749));
        defaultColors.put(key_chat_recordedVoiceDot, Integer.valueOf(-2468275));
        defaultColors.put(key_chat_recordedVoiceBackground, Integer.valueOf(-11165981));
        defaultColors.put(key_chat_recordedVoiceProgress, Integer.valueOf(-6107400));
        defaultColors.put(key_chat_recordedVoiceProgressInner, valueOf);
        defaultColors.put(key_chat_recordVoiceCancel, Integer.valueOf(-6710887));
        defaultColors.put(key_chat_messagePanelSend, Integer.valueOf(-10309397));
        defaultColors.put(key_chat_messagePanelVoiceLock, Integer.valueOf(-5987164));
        defaultColors.put(key_chat_messagePanelVoiceLockBackground, valueOf);
        defaultColors.put(key_chat_messagePanelVoiceLockShadow, valueOf4);
        defaultColors.put(key_chat_recordTime, Integer.valueOf(-11711413));
        defaultColors.put(key_chat_emojiPanelNewTrending, Integer.valueOf(-11688214));
        defaultColors.put(key_chat_gifSaveHintText, valueOf);
        defaultColors.put(key_chat_gifSaveHintBackground, Integer.valueOf(-871296751));
        defaultColors.put(key_chat_goDownButton, valueOf);
        defaultColors.put(key_chat_goDownButtonShadow, valueOf4);
        defaultColors.put(key_chat_goDownButtonIcon, Integer.valueOf(-7432805));
        defaultColors.put(key_chat_goDownButtonCounter, valueOf);
        defaultColors.put(key_chat_goDownButtonCounterBackground, Integer.valueOf(-11689240));
        defaultColors.put(key_chat_messagePanelCancelInlineBot, Integer.valueOf(-5395027));
        defaultColors.put(key_chat_messagePanelVoicePressed, valueOf);
        defaultColors.put(key_chat_messagePanelVoiceBackground, Integer.valueOf(-11037236));
        defaultColors.put(key_chat_messagePanelVoiceShadow, Integer.valueOf(218103808));
        defaultColors.put(key_chat_messagePanelVoiceDelete, Integer.valueOf(-9211021));
        defaultColors.put(key_chat_messagePanelVoiceDuration, valueOf);
        defaultColors.put(key_chat_inlineResultIcon, Integer.valueOf(-11037236));
        defaultColors.put(key_chat_topPanelBackground, valueOf);
        defaultColors.put(key_chat_topPanelClose, Integer.valueOf(-5723992));
        defaultColors.put(key_chat_topPanelLine, Integer.valueOf(-9658414));
        defaultColors.put(key_chat_topPanelTitle, valueOf3);
        defaultColors.put(key_chat_topPanelMessage, Integer.valueOf(-6710887));
        defaultColors.put(key_chat_reportSpam, Integer.valueOf(-3188393));
        defaultColors.put(key_chat_addContact, Integer.valueOf(-11894091));
        defaultColors.put(key_chat_inLoader, Integer.valueOf(-9259544));
        defaultColors.put(key_chat_inLoaderSelected, Integer.valueOf(-10114080));
        defaultColors.put(key_chat_outLoader, Integer.valueOf(-8863118));
        defaultColors.put(key_chat_outLoaderSelected, Integer.valueOf(-9783964));
        defaultColors.put(key_chat_inLoaderPhoto, Integer.valueOf(-6113080));
        defaultColors.put(key_chat_inLoaderPhotoSelected, Integer.valueOf(-6113849));
        defaultColors.put(key_chat_inLoaderPhotoIcon, Integer.valueOf(-197380));
        defaultColors.put(key_chat_inLoaderPhotoIconSelected, Integer.valueOf(-1314571));
        defaultColors.put(key_chat_outLoaderPhoto, Integer.valueOf(-8011912));
        defaultColors.put(key_chat_outLoaderPhotoSelected, Integer.valueOf(-8538000));
        defaultColors.put(key_chat_outLoaderPhotoIcon, Integer.valueOf(-2427453));
        defaultColors.put(key_chat_outLoaderPhotoIconSelected, Integer.valueOf(-4134748));
        defaultColors.put(key_chat_mediaLoaderPhoto, Integer.valueOf(1711276032));
        defaultColors.put(key_chat_mediaLoaderPhotoSelected, Integer.valueOf(ACTION_BAR_PHOTO_VIEWER_COLOR));
        defaultColors.put(key_chat_mediaLoaderPhotoIcon, valueOf);
        defaultColors.put(key_chat_mediaLoaderPhotoIconSelected, Integer.valueOf(-2500135));
        defaultColors.put(key_chat_secretTimerBackground, Integer.valueOf(-868326258));
        defaultColors.put(key_chat_secretTimerText, valueOf);
        defaultColors.put(key_profile_creatorIcon, Integer.valueOf(-12937771));
        defaultColors.put(key_profile_actionIcon, Integer.valueOf(-8288630));
        defaultColors.put(key_profile_actionBackground, valueOf);
        defaultColors.put(key_profile_actionPressedBackground, Integer.valueOf(-855310));
        defaultColors.put(key_profile_verifiedBackground, Integer.valueOf(-5056776));
        defaultColors.put(key_profile_verifiedCheck, Integer.valueOf(-11959368));
        defaultColors.put(key_profile_title, valueOf);
        defaultColors.put(key_profile_status, Integer.valueOf(-2626822));
        defaultColors.put(key_player_actionBar, valueOf);
        defaultColors.put(key_player_actionBarSelector, Integer.valueOf(251658240));
        defaultColors.put(key_player_actionBarTitle, valueOf5);
        defaultColors.put(key_player_actionBarTop, Integer.valueOf(-1728053248));
        defaultColors.put(key_player_actionBarSubtitle, Integer.valueOf(-7697782));
        defaultColors.put(key_player_actionBarItems, Integer.valueOf(-7697782));
        defaultColors.put(key_player_background, valueOf);
        defaultColors.put(key_player_time, Integer.valueOf(-7564650));
        defaultColors.put(key_player_progressBackground, Integer.valueOf(-1445899));
        defaultColors.put(key_player_progressCachedBackground, Integer.valueOf(-1445899));
        defaultColors.put(key_player_progress, Integer.valueOf(-11821085));
        defaultColors.put(key_player_placeholder, Integer.valueOf(-5723992));
        defaultColors.put(key_player_placeholderBackground, Integer.valueOf(-986896));
        defaultColors.put(key_player_button, Integer.valueOf(ACTION_BAR_MEDIA_PICKER_COLOR));
        defaultColors.put(key_player_buttonActive, Integer.valueOf(-11753238));
        defaultColors.put(key_sheet_scrollUp, Integer.valueOf(-1973016));
        defaultColors.put(key_sheet_other, Integer.valueOf(-3551789));
        defaultColors.put(key_files_folderIcon, Integer.valueOf(-6710887));
        defaultColors.put(key_files_folderIconBackground, Integer.valueOf(-986896));
        defaultColors.put(key_files_iconText, valueOf);
        defaultColors.put(key_sessions_devicesImage, Integer.valueOf(-6908266));
        defaultColors.put(key_passport_authorizeBackground, Integer.valueOf(-12211217));
        defaultColors.put(key_passport_authorizeBackgroundSelected, Integer.valueOf(-12542501));
        defaultColors.put(key_passport_authorizeText, valueOf);
        defaultColors.put(key_location_markerX, Integer.valueOf(-8355712));
        defaultColors.put(key_location_sendLocationBackground, Integer.valueOf(-9592620));
        defaultColors.put(key_location_sendLiveLocationBackground, Integer.valueOf(-39836));
        defaultColors.put(key_location_sendLocationIcon, valueOf);
        defaultColors.put(key_location_sendLiveLocationIcon, valueOf);
        defaultColors.put(key_location_liveLocationProgress, Integer.valueOf(-13262875));
        defaultColors.put(key_location_placeLocationBackground, Integer.valueOf(-11753238));
        defaultColors.put(key_dialog_liveLocationProgress, Integer.valueOf(-13262875));
        defaultColors.put(key_calls_callReceivedGreenIcon, Integer.valueOf(-16725933));
        defaultColors.put(key_calls_callReceivedRedIcon, Integer.valueOf(-47032));
        defaultColors.put(key_featuredStickers_addedIcon, Integer.valueOf(-11491093));
        defaultColors.put(key_featuredStickers_buttonProgress, valueOf);
        defaultColors.put(key_featuredStickers_addButton, Integer.valueOf(-11491093));
        defaultColors.put(key_featuredStickers_addButtonPressed, Integer.valueOf(-12346402));
        defaultColors.put(key_featuredStickers_delButton, Integer.valueOf(-2533545));
        defaultColors.put(key_featuredStickers_delButtonPressed, Integer.valueOf(-3782327));
        defaultColors.put(key_featuredStickers_buttonText, valueOf);
        defaultColors.put(key_featuredStickers_unread, Integer.valueOf(-11688214));
        defaultColors.put(key_inappPlayerPerformer, valueOf5);
        defaultColors.put(key_inappPlayerTitle, valueOf5);
        defaultColors.put(key_inappPlayerBackground, valueOf);
        defaultColors.put(key_inappPlayerPlayPause, Integer.valueOf(-10309397));
        defaultColors.put(key_inappPlayerClose, Integer.valueOf(-5723992));
        defaultColors.put(key_returnToCallBackground, Integer.valueOf(-12279325));
        defaultColors.put(key_returnToCallText, valueOf);
        defaultColors.put(key_sharedMedia_startStopLoadIcon, Integer.valueOf(-13196562));
        defaultColors.put(key_sharedMedia_linkPlaceholder, Integer.valueOf(-986123));
        defaultColors.put(key_sharedMedia_linkPlaceholderText, Integer.valueOf(-4735293));
        defaultColors.put(key_sharedMedia_photoPlaceholder, Integer.valueOf(-1182729));
        defaultColors.put(key_sharedMedia_actionMode, Integer.valueOf(-12154957));
        defaultColors.put(key_checkbox, Integer.valueOf(-10567099));
        defaultColors.put(key_checkboxCheck, valueOf);
        defaultColors.put(key_checkboxDisabled, Integer.valueOf(-5195326));
        defaultColors.put(key_stickers_menu, Integer.valueOf(-4801083));
        defaultColors.put(key_stickers_menuSelector, Integer.valueOf(251658240));
        defaultColors.put(key_changephoneinfo_image, Integer.valueOf(-5723992));
        defaultColors.put(key_changephoneinfo_changeText, Integer.valueOf(-11697229));
        defaultColors.put(key_groupcreate_hintText, valueOf6);
        defaultColors.put(key_groupcreate_cursor, Integer.valueOf(-11361317));
        defaultColors.put(key_groupcreate_sectionShadow, valueOf4);
        defaultColors.put(key_groupcreate_sectionText, Integer.valueOf(-8617336));
        defaultColors.put(key_groupcreate_spanText, valueOf2);
        defaultColors.put(key_groupcreate_spanBackground, Integer.valueOf(-855310));
        defaultColors.put(key_groupcreate_spanDelete, valueOf);
        defaultColors.put(key_contacts_inviteBackground, Integer.valueOf(-11157919));
        defaultColors.put(key_contacts_inviteText, valueOf);
        defaultColors.put(key_login_progressInner, Integer.valueOf(-1971470));
        defaultColors.put(key_login_progressOuter, Integer.valueOf(-10313520));
        defaultColors.put(key_musicPicker_checkbox, Integer.valueOf(-14043401));
        defaultColors.put(key_musicPicker_checkboxCheck, valueOf);
        defaultColors.put(key_musicPicker_buttonBackground, Integer.valueOf(-10702870));
        defaultColors.put(key_musicPicker_buttonIcon, valueOf);
        defaultColors.put(key_picker_enabledButton, Integer.valueOf(-15095832));
        defaultColors.put(key_picker_disabledButton, Integer.valueOf(-6710887));
        defaultColors.put(key_picker_badge, Integer.valueOf(-14043401));
        defaultColors.put(key_picker_badgeText, valueOf);
        defaultColors.put(key_chat_botSwitchToInlineText, Integer.valueOf(-12348980));
        defaultColors.put(key_undo_background, Integer.valueOf(-366530760));
        defaultColors.put(key_undo_cancelColor, Integer.valueOf(-8008961));
        defaultColors.put(key_undo_infoColor, valueOf);
        fallbackKeys.put(key_chat_adminText, key_chat_inTimeText);
        fallbackKeys.put(key_chat_adminSelectedText, key_chat_inTimeSelectedText);
        fallbackKeys.put(key_player_progressCachedBackground, key_player_progressBackground);
        fallbackKeys.put(key_chat_inAudioCacheSeekbar, key_chat_inAudioSeekbar);
        fallbackKeys.put(key_chat_outAudioCacheSeekbar, key_chat_outAudioSeekbar);
        fallbackKeys.put(key_chat_emojiSearchBackground, key_chat_emojiPanelStickerPackSelector);
        fallbackKeys.put(key_location_sendLiveLocationIcon, key_location_sendLocationIcon);
        fallbackKeys.put(key_changephoneinfo_changeText, key_windowBackgroundWhiteBlueText4);
        fallbackKeys.put(key_graySectionText, key_windowBackgroundWhiteGrayText2);
        fallbackKeys.put(key_chat_inMediaIcon, key_chat_inBubble);
        fallbackKeys.put(key_chat_outMediaIcon, key_chat_outBubble);
        fallbackKeys.put(key_chat_inMediaIconSelected, key_chat_inBubbleSelected);
        fallbackKeys.put(key_chat_outMediaIconSelected, key_chat_outBubbleSelected);
        fallbackKeys.put(key_chats_actionUnreadIcon, key_profile_actionIcon);
        fallbackKeys.put(key_chats_actionUnreadBackground, key_profile_actionBackground);
        fallbackKeys.put(key_chats_actionUnreadPressedBackground, key_profile_actionPressedBackground);
        fallbackKeys.put(key_dialog_inlineProgressBackground, key_windowBackgroundGray);
        fallbackKeys.put(key_dialog_inlineProgress, key_chats_menuItemIcon);
        fallbackKeys.put(key_groupcreate_spanDelete, key_chats_actionIcon);
        fallbackKeys.put(key_sharedMedia_photoPlaceholder, key_windowBackgroundGray);
        fallbackKeys.put(key_chat_attachPollBackground, key_chat_attachAudioBackground);
        fallbackKeys.put(key_chat_attachPollIcon, key_chat_attachAudioIcon);
        fallbackKeys.put(key_chats_onlineCircle, key_windowBackgroundWhiteBlueText);
        fallbackKeys.put(key_windowBackgroundWhiteBlueButton, key_windowBackgroundWhiteValueText);
        fallbackKeys.put(key_windowBackgroundWhiteBlueIcon, key_windowBackgroundWhiteValueText);
        fallbackKeys.put(key_undo_background, key_chat_gifSaveHintBackground);
        fallbackKeys.put(key_undo_cancelColor, key_chat_gifSaveHintText);
        fallbackKeys.put(key_undo_infoColor, key_chat_gifSaveHintText);
        fallbackKeys.put(key_windowBackgroundUnchecked, key_windowBackgroundWhite);
        fallbackKeys.put(key_windowBackgroundChecked, key_windowBackgroundWhite);
        fallbackKeys.put(key_switchTrackBlue, key_switchTrack);
        fallbackKeys.put(key_switchTrackBlueChecked, key_switchTrackChecked);
        fallbackKeys.put(key_switchTrackBlueThumb, key_windowBackgroundWhite);
        fallbackKeys.put(key_switchTrackBlueThumbChecked, key_windowBackgroundWhite);
        fallbackKeys.put(key_windowBackgroundCheckText, key_windowBackgroundWhiteBlackText);
        fallbackKeys.put(key_contextProgressInner4, key_contextProgressInner1);
        fallbackKeys.put(key_contextProgressOuter4, key_contextProgressOuter1);
        fallbackKeys.put(key_switchTrackBlueSelector, key_listSelector);
        fallbackKeys.put(key_switchTrackBlueSelectorChecked, key_listSelector);
        fallbackKeys.put(key_chat_emojiBottomPanelIcon, key_chat_emojiPanelIcon);
        fallbackKeys.put(key_chat_emojiSearchIcon, key_chat_emojiPanelIcon);
        fallbackKeys.put(key_chat_emojiPanelStickerSetNameHighlight, key_windowBackgroundWhiteBlueText4);
        fallbackKeys.put(key_chat_emojiPanelStickerPackSelectorLine, key_chat_emojiPanelIconSelected);
        fallbackKeys.put(key_sharedMedia_actionMode, key_actionBarDefault);
        fallbackKeys.put(key_sheet_scrollUp, key_chat_emojiPanelStickerPackSelector);
        fallbackKeys.put(key_sheet_other, key_player_actionBarItems);
        fallbackKeys.put(key_dialogSearchBackground, key_chat_emojiPanelStickerPackSelector);
        fallbackKeys.put(key_dialogSearchHint, key_chat_emojiPanelIcon);
        fallbackKeys.put(key_dialogSearchIcon, key_chat_emojiPanelIcon);
        fallbackKeys.put(key_dialogSearchText, key_windowBackgroundWhiteBlackText);
        fallbackKeys.put(key_dialogFloatingButton, key_dialogRoundCheckBox);
        fallbackKeys.put(key_dialogFloatingButtonPressed, key_dialogRoundCheckBox);
        fallbackKeys.put(key_dialogFloatingIcon, key_dialogRoundCheckBoxCheck);
        fallbackKeys.put(key_dialogShadowLine, key_chat_emojiPanelShadowLine);
        fallbackKeys.put(key_actionBarDefaultArchived, key_actionBarDefault);
        fallbackKeys.put(key_actionBarDefaultArchivedSelector, key_actionBarDefaultSelector);
        fallbackKeys.put(key_actionBarDefaultArchivedIcon, key_actionBarDefaultIcon);
        fallbackKeys.put(key_actionBarDefaultArchivedTitle, key_actionBarDefaultTitle);
        fallbackKeys.put(key_actionBarDefaultArchivedSearch, key_actionBarDefaultSearch);
        fallbackKeys.put(key_actionBarDefaultArchivedSearchPlaceholder, key_actionBarDefaultSearchPlaceholder);
        fallbackKeys.put(key_chats_message_threeLines, key_chats_message);
        fallbackKeys.put(key_chats_nameMessage_threeLines, key_chats_nameMessage);
        fallbackKeys.put(key_chats_nameArchived, key_chats_name);
        fallbackKeys.put(key_chats_nameMessageArchived, key_chats_nameMessage);
        fallbackKeys.put(key_chats_nameMessageArchived_threeLines, key_chats_nameMessage);
        fallbackKeys.put(key_chats_messageArchived, key_chats_message);
        fallbackKeys.put(key_avatar_backgroundArchived, key_chats_unreadCounterMuted);
        fallbackKeys.put(key_avatar_backgroundArchivedHidden, key_chats_unreadCounterMuted);
        fallbackKeys.put(key_chats_archiveBackground, key_chats_actionBackground);
        fallbackKeys.put(key_chats_archivePinBackground, key_chats_unreadCounterMuted);
        fallbackKeys.put(key_chats_archiveIcon, key_chats_actionIcon);
        fallbackKeys.put(key_chats_archiveText, key_chats_actionIcon);
        fallbackKeys.put(key_actionBarDefaultSubmenuItemIcon, key_dialogIcon);
        fallbackKeys.put(key_checkboxDisabled, key_chats_unreadCounterMuted);
        fallbackKeys.put(key_chat_status, key_actionBarDefaultSubtitle);
        fallbackKeys.put(key_chat_inGreenCall, key_calls_callReceivedGreenIcon);
        fallbackKeys.put(key_chat_inRedCall, key_calls_callReceivedRedIcon);
        fallbackKeys.put(key_chat_outGreenCall, key_calls_callReceivedGreenIcon);
        fallbackKeys.put(key_actionBarTabActiveText, key_actionBarDefaultTitle);
        fallbackKeys.put(key_actionBarTabUnactiveText, key_actionBarDefaultSubtitle);
        fallbackKeys.put(key_actionBarTabLine, key_actionBarDefaultTitle);
        fallbackKeys.put(key_actionBarTabSelector, key_actionBarDefaultSelector);
        fallbackKeys.put(key_profile_status, key_avatar_subtitleInProfileBlue);
        fallbackKeys.put(key_chats_menuTopBackgroundCats, key_avatar_backgroundActionBarBlue);
        ThemeInfo themeInfo = new ThemeInfo();
        themeInfo.name = "Default";
        themeInfo.previewBackgroundColor = -3155485;
        themeInfo.previewInColor = -1;
        themeInfo.previewOutColor = -983328;
        themeInfo.sortIndex = 0;
        ArrayList arrayList = themes;
        defaultTheme = themeInfo;
        currentTheme = themeInfo;
        currentDayTheme = themeInfo;
        arrayList.add(themeInfo);
        themesDict.put("Default", defaultTheme);
        themeInfo = new ThemeInfo();
        themeInfo.name = "Dark";
        themeInfo.assetName = "dark.attheme";
        themeInfo.previewBackgroundColor = -10855071;
        themeInfo.previewInColor = -9143676;
        themeInfo.previewOutColor = -8214301;
        themeInfo.sortIndex = 3;
        themes.add(themeInfo);
        themesDict.put("Dark", themeInfo);
        themeInfo = new ThemeInfo();
        themeInfo.name = "Blue";
        themeInfo.assetName = "bluebubbles.attheme";
        themeInfo.previewBackgroundColor = -6963476;
        themeInfo.previewInColor = -1;
        themeInfo.previewOutColor = -3086593;
        themeInfo.sortIndex = 1;
        themes.add(themeInfo);
        themesDict.put("Blue", themeInfo);
        themeInfo = new ThemeInfo();
        themeInfo.name = "Dark Blue";
        themeInfo.assetName = "darkblue.attheme";
        themeInfo.previewBackgroundColor = -10523006;
        themeInfo.previewInColor = -9009508;
        themeInfo.previewOutColor = -8214301;
        themeInfo.sortIndex = 2;
        themes.add(themeInfo);
        HashMap hashMap2 = themesDict;
        currentNightTheme = themeInfo;
        hashMap2.put("Dark Blue", themeInfo);
        if (BuildVars.DEBUG_VERSION) {
            themeInfo = new ThemeInfo();
            themeInfo.name = "Graphite";
            themeInfo.assetName = "graphite.attheme";
            themeInfo.previewBackgroundColor = -8749431;
            themeInfo.previewInColor = -6775901;
            themeInfo.previewOutColor = -5980167;
            themeInfo.sortIndex = 4;
            themes.add(themeInfo);
            themesDict.put("Graphite", themeInfo);
        }
        themeInfo = new ThemeInfo();
        themeInfo.name = "Arctic Blue";
        themeInfo.assetName = "arctic.attheme";
        themeInfo.previewBackgroundColor = -1;
        themeInfo.previewInColor = -1315084;
        themeInfo.previewOutColor = -8604930;
        themeInfo.sortIndex = 5;
        themes.add(themeInfo);
        themesDict.put("Arctic Blue", themeInfo);
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        String string = sharedPreferences.getString("themes2", null);
        if (TextUtils.isEmpty(string)) {
            string = sharedPreferences.getString("themes", null);
            if (!TextUtils.isEmpty(string)) {
                String[] split = string.split("&");
                for (String createWithString : split) {
                    ThemeInfo createWithString2 = ThemeInfo.createWithString(createWithString);
                    if (createWithString2 != null) {
                        otherThemes.add(createWithString2);
                        themes.add(createWithString2);
                        themesDict.put(createWithString2.name, createWithString2);
                    }
                }
            }
            Theme.saveOtherThemes();
            sharedPreferences.edit().remove("themes").commit();
        } else {
            try {
                JSONArray jSONArray = new JSONArray(string);
                for (int i = 0; i < jSONArray.length(); i++) {
                    createWithJson = ThemeInfo.createWithJson(jSONArray.getJSONObject(i));
                    if (createWithJson != null) {
                        otherThemes.add(createWithJson);
                        themes.add(createWithJson);
                        themesDict.put(createWithJson.name, createWithJson);
                    }
                }
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
        Theme.sortThemes();
        ThemeInfo themeInfo2 = null;
        try {
            sharedPreferences = MessagesController.getGlobalMainSettings();
            String string2 = sharedPreferences.getString("theme", null);
            if (string2 != null) {
                themeInfo2 = (ThemeInfo) themesDict.get(string2);
            }
            string2 = sharedPreferences.getString("nighttheme", null);
            if (string2 != null) {
                createWithJson = (ThemeInfo) themesDict.get(string2);
                if (createWithJson != null) {
                    currentNightTheme = createWithJson;
                }
            }
            selectedAutoNightType = sharedPreferences.getInt("selectedAutoNightType", 0);
            autoNightScheduleByLocation = sharedPreferences.getBoolean("autoNightScheduleByLocation", false);
            autoNightBrighnessThreshold = sharedPreferences.getFloat("autoNightBrighnessThreshold", 0.25f);
            autoNightDayStartTime = sharedPreferences.getInt("autoNightDayStartTime", 1320);
            autoNightDayEndTime = sharedPreferences.getInt("autoNightDayEndTime", 480);
            autoNightSunsetTime = sharedPreferences.getInt("autoNightSunsetTime", 1320);
            autoNightSunriseTime = sharedPreferences.getInt("autoNightSunriseTime", 480);
            autoNightCityName = sharedPreferences.getString("autoNightCityName", "");
            long j = sharedPreferences.getLong("autoNightLocationLatitude3", 10000);
            if (j != 10000) {
                autoNightLocationLatitude = Double.longBitsToDouble(j);
            } else {
                autoNightLocationLatitude = 10000.0d;
            }
            j = sharedPreferences.getLong("autoNightLocationLongitude3", 10000);
            if (j != 10000) {
                autoNightLocationLongitude = Double.longBitsToDouble(j);
            } else {
                autoNightLocationLongitude = 10000.0d;
            }
            autoNightLastSunCheckDay = sharedPreferences.getInt("autoNightLastSunCheckDay", -1);
        } catch (Exception e2) {
            FileLog.m30e(e2);
        }
        if (themeInfo2 == null) {
            themeInfo2 = defaultTheme;
        } else {
            currentDayTheme = themeInfo2;
        }
        Theme.applyTheme(themeInfo2, false, false, false);
        AndroidUtilities.runOnUIThread(C2182-$$Lambda$RQB0Jwr1FTqp6hrbGUHuOs-9k1I.INSTANCE);
    }

    public static void saveAutoNightThemeConfig() {
        Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("selectedAutoNightType", selectedAutoNightType);
        edit.putBoolean("autoNightScheduleByLocation", autoNightScheduleByLocation);
        edit.putFloat("autoNightBrighnessThreshold", autoNightBrighnessThreshold);
        edit.putInt("autoNightDayStartTime", autoNightDayStartTime);
        edit.putInt("autoNightDayEndTime", autoNightDayEndTime);
        edit.putInt("autoNightSunriseTime", autoNightSunriseTime);
        edit.putString("autoNightCityName", autoNightCityName);
        edit.putInt("autoNightSunsetTime", autoNightSunsetTime);
        edit.putLong("autoNightLocationLatitude3", Double.doubleToRawLongBits(autoNightLocationLatitude));
        edit.putLong("autoNightLocationLongitude3", Double.doubleToRawLongBits(autoNightLocationLongitude));
        edit.putInt("autoNightLastSunCheckDay", autoNightLastSunCheckDay);
        ThemeInfo themeInfo = currentNightTheme;
        String str = "nighttheme";
        if (themeInfo != null) {
            edit.putString(str, themeInfo.name);
        } else {
            edit.remove(str);
        }
        edit.commit();
    }

    @SuppressLint({"PrivateApi"})
    private static Drawable getStateDrawable(Drawable drawable, int i) {
        if (StateListDrawable_getStateDrawableMethod == null) {
            try {
                StateListDrawable_getStateDrawableMethod = StateListDrawable.class.getDeclaredMethod("getStateDrawable", new Class[]{Integer.TYPE});
            } catch (Throwable unused) {
            }
        }
        Method method = StateListDrawable_getStateDrawableMethod;
        if (method == null) {
            return null;
        }
        try {
            return (Drawable) method.invoke(drawable, new Object[]{Integer.valueOf(i)});
        } catch (Exception unused2) {
            return null;
        }
    }

    public static Drawable createEmojiIconSelectorDrawable(Context context, int i, int i2, int i3) {
        Resources resources = context.getResources();
        Drawable mutate = resources.getDrawable(i).mutate();
        if (i2 != 0) {
            mutate.setColorFilter(new PorterDuffColorFilter(i2, Mode.MULTIPLY));
        }
        Drawable mutate2 = resources.getDrawable(i).mutate();
        if (i3 != 0) {
            mutate2.setColorFilter(new PorterDuffColorFilter(i3, Mode.MULTIPLY));
        }
        C22253 c22253 = new C22253();
        c22253.setEnterFadeDuration(1);
        c22253.setExitFadeDuration(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        c22253.addState(new int[]{16842913}, mutate2);
        c22253.addState(new int[0], mutate);
        return c22253;
    }

    public static Drawable createEditTextDrawable(Context context, boolean z) {
        Resources resources = context.getResources();
        Drawable mutate = resources.getDrawable(C1067R.C1065drawable.search_dark).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(z ? key_dialogInputField : key_windowBackgroundWhiteInputField), Mode.MULTIPLY));
        Drawable mutate2 = resources.getDrawable(C1067R.C1065drawable.search_dark_activated).mutate();
        mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(z ? key_dialogInputFieldActivated : key_windowBackgroundWhiteInputFieldActivated), Mode.MULTIPLY));
        C22264 c22264 = new C22264();
        c22264.addState(new int[]{16842910, 16842908}, mutate2);
        c22264.addState(new int[]{16842908}, mutate2);
        c22264.addState(StateSet.WILD_CARD, mutate);
        return c22264;
    }

    public static boolean canStartHolidayAnimation() {
        return canStartHolidayAnimation;
    }

    public static int getEventType() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        int i = instance.get(2);
        int i2 = instance.get(5);
        instance.get(12);
        instance.get(11);
        return ((i != 11 || i2 < 24 || i2 > 31) && !(i == 0 && i2 == 1)) ? -1 : 0;
    }

    /* JADX WARNING: Missing block: B:18:0x0059, code skipped:
            if (r2 <= 31) goto L_0x005f;
     */
    /* JADX WARNING: Missing block: B:20:0x005d, code skipped:
            if (r2 == 1) goto L_0x005f;
     */
    /* JADX WARNING: Missing block: B:21:0x005f, code skipped:
            dialogs_holidayDrawable = org.telegram.messenger.ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.C1067R.C1065drawable.newyear);
            dialogs_holidayDrawableOffsetX = -org.telegram.messenger.AndroidUtilities.m26dp(3.0f);
            dialogs_holidayDrawableOffsetY = -org.telegram.messenger.AndroidUtilities.m26dp(1.0f);
     */
    public static android.graphics.drawable.Drawable getCurrentHolidayDrawable() {
        /*
        r0 = java.lang.System.currentTimeMillis();
        r2 = lastHolidayCheckTime;
        r0 = r0 - r2;
        r2 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 < 0) goto L_0x0080;
    L_0x000e:
        r0 = java.lang.System.currentTimeMillis();
        lastHolidayCheckTime = r0;
        r0 = java.util.Calendar.getInstance();
        r1 = java.lang.System.currentTimeMillis();
        r0.setTimeInMillis(r1);
        r1 = 2;
        r1 = r0.get(r1);
        r2 = 5;
        r2 = r0.get(r2);
        r3 = 12;
        r3 = r0.get(r3);
        r4 = 11;
        r0 = r0.get(r4);
        r5 = 1;
        if (r1 != 0) goto L_0x0043;
    L_0x0038:
        if (r2 != r5) goto L_0x0043;
    L_0x003a:
        r6 = 10;
        if (r3 > r6) goto L_0x0043;
    L_0x003e:
        if (r0 != 0) goto L_0x0043;
    L_0x0040:
        canStartHolidayAnimation = r5;
        goto L_0x0046;
    L_0x0043:
        r0 = 0;
        canStartHolidayAnimation = r0;
    L_0x0046:
        r0 = dialogs_holidayDrawable;
        if (r0 != 0) goto L_0x0080;
    L_0x004a:
        if (r1 != r4) goto L_0x005b;
    L_0x004c:
        r0 = org.telegram.messenger.BuildVars.DEBUG_PRIVATE_VERSION;
        r3 = 31;
        if (r0 == 0) goto L_0x0055;
    L_0x0052:
        r0 = 29;
        goto L_0x0057;
    L_0x0055:
        r0 = 31;
    L_0x0057:
        if (r2 < r0) goto L_0x005b;
    L_0x0059:
        if (r2 <= r3) goto L_0x005f;
    L_0x005b:
        if (r1 != 0) goto L_0x0080;
    L_0x005d:
        if (r2 != r5) goto L_0x0080;
    L_0x005f:
        r0 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r0 = r0.getResources();
        r1 = 2131165689; // 0x7f0701f9 float:1.7945602E38 double:1.0529357525E-314;
        r0 = r0.getDrawable(r1);
        dialogs_holidayDrawable = r0;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r0 = -r0;
        dialogs_holidayDrawableOffsetX = r0;
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r0 = -r0;
        dialogs_holidayDrawableOffsetY = r0;
    L_0x0080:
        r0 = dialogs_holidayDrawable;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.getCurrentHolidayDrawable():android.graphics.drawable.Drawable");
    }

    public static int getCurrentHolidayDrawableXOffset() {
        return dialogs_holidayDrawableOffsetX;
    }

    public static int getCurrentHolidayDrawableYOffset() {
        return dialogs_holidayDrawableOffsetY;
    }

    public static Drawable createSimpleSelectorDrawable(Context context, int i, int i2, int i3) {
        Resources resources = context.getResources();
        Drawable mutate = resources.getDrawable(i).mutate();
        if (i2 != 0) {
            mutate.setColorFilter(new PorterDuffColorFilter(i2, Mode.MULTIPLY));
        }
        Drawable mutate2 = resources.getDrawable(i).mutate();
        if (i3 != 0) {
            mutate2.setColorFilter(new PorterDuffColorFilter(i3, Mode.MULTIPLY));
        }
        C22275 c22275 = new C22275();
        c22275.addState(new int[]{16842919}, mutate2);
        c22275.addState(new int[]{16842913}, mutate2);
        c22275.addState(StateSet.WILD_CARD, mutate);
        return c22275;
    }

    public static Drawable createCircleDrawable(int i, int i2) {
        OvalShape ovalShape = new OvalShape();
        float f = (float) i;
        ovalShape.resize(f, f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(i2);
        return shapeDrawable;
    }

    public static CombinedDrawable createCircleDrawableWithIcon(int i, int i2) {
        return Theme.createCircleDrawableWithIcon(i, i2, 0);
    }

    public static CombinedDrawable createCircleDrawableWithIcon(int i, int i2, int i3) {
        return Theme.createCircleDrawableWithIcon(i, i2 != 0 ? ApplicationLoader.applicationContext.getResources().getDrawable(i2).mutate() : null, i3);
    }

    public static CombinedDrawable createCircleDrawableWithIcon(int i, Drawable drawable, int i2) {
        OvalShape ovalShape = new OvalShape();
        float f = (float) i;
        ovalShape.resize(f, f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(-1);
        if (i2 == 1) {
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth((float) AndroidUtilities.m26dp(2.0f));
        } else if (i2 == 2) {
            paint.setAlpha(0);
        }
        CombinedDrawable combinedDrawable = new CombinedDrawable(shapeDrawable, drawable);
        combinedDrawable.setCustomSize(i, i);
        return combinedDrawable;
    }

    public static Drawable createRoundRectDrawableWithIcon(int i, int i2) {
        r2 = new float[8];
        float f = (float) i;
        r2[0] = f;
        r2[1] = f;
        r2[2] = f;
        r2[3] = f;
        r2[4] = f;
        r2[5] = f;
        r2[6] = f;
        r2[7] = f;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(r2, null, null));
        shapeDrawable.getPaint().setColor(-1);
        return new CombinedDrawable(shapeDrawable, ApplicationLoader.applicationContext.getResources().getDrawable(i2).mutate());
    }

    public static void setCombinedDrawableColor(Drawable drawable, int i, boolean z) {
        if (drawable instanceof CombinedDrawable) {
            if (z) {
                drawable = ((CombinedDrawable) drawable).getIcon();
            } else {
                drawable = ((CombinedDrawable) drawable).getBackground();
            }
            if (drawable instanceof ColorDrawable) {
                ((ColorDrawable) drawable).setColor(i);
            } else {
                drawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
            }
        }
    }

    public static Drawable createSimpleSelectorCircleDrawable(int i, int i2, int i3) {
        OvalShape ovalShape = new OvalShape();
        float f = (float) i;
        ovalShape.resize(f, f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(i2);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(ovalShape);
        if (VERSION.SDK_INT >= 21) {
            shapeDrawable2.getPaint().setColor(-1);
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i3}), shapeDrawable, shapeDrawable2);
        }
        shapeDrawable2.getPaint().setColor(i3);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable2);
        stateListDrawable.addState(new int[]{16842908}, shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, shapeDrawable);
        return stateListDrawable;
    }

    public static Drawable createRoundRectDrawable(int i, int i2) {
        r2 = new float[8];
        float f = (float) i;
        r2[0] = f;
        r2[1] = f;
        r2[2] = f;
        r2[3] = f;
        r2[4] = f;
        r2[5] = f;
        r2[6] = f;
        r2[7] = f;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(r2, null, null));
        shapeDrawable.getPaint().setColor(i2);
        return shapeDrawable;
    }

    public static Drawable createSimpleSelectorRoundRectDrawable(int i, int i2, int i3) {
        r3 = new float[8];
        float f = (float) i;
        r3[0] = f;
        r3[1] = f;
        r3[2] = f;
        r3[3] = f;
        r3[4] = f;
        r3[5] = f;
        r3[6] = f;
        r3[7] = f;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(r3, null, null));
        shapeDrawable.getPaint().setColor(i2);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
        shapeDrawable2.getPaint().setColor(i3);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable2);
        stateListDrawable.addState(new int[]{16842913}, shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, shapeDrawable);
        return stateListDrawable;
    }

    public static Drawable getRoundRectSelectorDrawable(int i) {
        if (VERSION.SDK_INT >= 21) {
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{(i & 16777215) | 419430400}), null, Theme.createRoundRectDrawable(AndroidUtilities.m26dp(3.0f), -1));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        i = (i & 16777215) | 419430400;
        stateListDrawable.addState(new int[]{16842919}, Theme.createRoundRectDrawable(AndroidUtilities.m26dp(3.0f), i));
        stateListDrawable.addState(new int[]{16842913}, Theme.createRoundRectDrawable(AndroidUtilities.m26dp(3.0f), i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static Drawable createSelectorWithBackgroundDrawable(int i, int i2) {
        if (VERSION.SDK_INT >= 21) {
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i2}), new ColorDrawable(i), new ColorDrawable(i));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i2));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i2));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(i));
        return stateListDrawable;
    }

    public static Drawable getSelectorDrawable(boolean z) {
        return Theme.getSelectorDrawable(Theme.getColor(key_listSelector), z);
    }

    public static Drawable getSelectorDrawable(int i, boolean z) {
        if (!z) {
            return Theme.createSelectorDrawable(i, 2);
        }
        int i2 = VERSION.SDK_INT;
        String str = key_windowBackgroundWhite;
        if (i2 >= 21) {
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), new ColorDrawable(Theme.getColor(str)), new ColorDrawable(-1));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(Theme.getColor(str)));
        return stateListDrawable;
    }

    public static Drawable createSelectorDrawable(int i) {
        return Theme.createSelectorDrawable(i, 1);
    }

    public static Drawable createSelectorDrawable(int i, final int i2) {
        int i3 = VERSION.SDK_INT;
        if (i3 >= 21) {
            Drawable c22286;
            RippleDrawable rippleDrawable;
            if (i2 != 1 || i3 < 23) {
                if (i2 == 1 || i2 == 3 || i2 == 4) {
                    maskPaint.setColor(-1);
                    c22286 = new Drawable() {
                        public int getOpacity() {
                            return 0;
                        }

                        public void setAlpha(int i) {
                        }

                        public void setColorFilter(ColorFilter colorFilter) {
                        }

                        public void draw(Canvas canvas) {
                            Rect bounds = getBounds();
                            int i = i2;
                            if (i == 1) {
                                i = AndroidUtilities.m26dp(20.0f);
                            } else if (i == 3) {
                                i = Math.max(bounds.width(), bounds.height()) / 2;
                            } else {
                                i = (int) Math.ceil(Math.sqrt((double) (((bounds.left - bounds.centerX()) * (bounds.left - bounds.centerX())) + ((bounds.top - bounds.centerY()) * (bounds.top - bounds.centerY())))));
                            }
                            canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), (float) i, Theme.maskPaint);
                        }
                    };
                    rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, c22286);
                    if (i2 == 1 && VERSION.SDK_INT >= 23) {
                        rippleDrawable.setRadius(AndroidUtilities.m26dp(20.0f));
                    }
                    return rippleDrawable;
                } else if (i2 == 2) {
                    c22286 = new ColorDrawable(-1);
                    rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, c22286);
                    rippleDrawable.setRadius(AndroidUtilities.m26dp(20.0f));
                    return rippleDrawable;
                }
            }
            c22286 = null;
            rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, c22286);
            rippleDrawable.setRadius(AndroidUtilities.m26dp(20.0f));
            return rippleDrawable;
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static void applyPreviousTheme() {
        ThemeInfo themeInfo = previousTheme;
        if (themeInfo != null) {
            Theme.applyTheme(themeInfo, true, false, false);
            previousTheme = null;
            Theme.checkAutoNightThemeConditions();
        }
    }

    private static void sortThemes() {
        Collections.sort(themes, C2183-$$Lambda$Theme$CDAxGNnEyNa6tkvecQwKSXq77-I.INSTANCE);
    }

    static /* synthetic */ int lambda$sortThemes$0(ThemeInfo themeInfo, ThemeInfo themeInfo2) {
        if (themeInfo.pathToFile == null && themeInfo.assetName == null) {
            return -1;
        }
        if (themeInfo2.pathToFile == null && themeInfo2.assetName == null) {
            return 1;
        }
        return themeInfo.name.compareTo(themeInfo2.name);
    }

    public static ThemeInfo applyThemeFile(File file, String str, boolean z) {
        try {
            if (!(str.equals("Default") || str.equals("Dark") || str.equals("Blue") || str.equals("Dark Blue") || str.equals("Graphite"))) {
                if (!str.equals("Arctic Blue")) {
                    File file2 = new File(ApplicationLoader.getFilesDirFixed(), str);
                    if (!AndroidUtilities.copyFile(file, file2)) {
                        return null;
                    }
                    Object obj;
                    Object obj2;
                    ThemeInfo themeInfo = (ThemeInfo) themesDict.get(str);
                    if (themeInfo == null) {
                        themeInfo = new ThemeInfo();
                        themeInfo.name = str;
                        themeInfo.pathToFile = file2.getAbsolutePath();
                        obj = themeInfo;
                        obj2 = 1;
                    } else {
                        obj = themeInfo;
                        obj2 = null;
                    }
                    if (z) {
                        previousTheme = currentTheme;
                    } else {
                        previousTheme = null;
                        if (obj2 != null) {
                            themes.add(obj);
                            themesDict.put(obj.name, obj);
                            otherThemes.add(obj);
                            Theme.sortThemes();
                            Theme.saveOtherThemes();
                        }
                    }
                    Theme.applyTheme(obj, !z, true, false);
                    return obj;
                }
            }
            return null;
        } catch (Exception e) {
            FileLog.m30e(e);
            return null;
        }
    }

    public static void applyTheme(ThemeInfo themeInfo) {
        Theme.applyTheme(themeInfo, true, true, false);
    }

    public static void applyTheme(ThemeInfo themeInfo, boolean z) {
        Theme.applyTheme(themeInfo, true, true, z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0075 A:{Catch:{ Exception -> 0x0091 }} */
    public static void applyTheme(org.telegram.p004ui.ActionBar.Theme.ThemeInfo r5, boolean r6, boolean r7, boolean r8) {
        /*
        if (r5 != 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        r0 = org.telegram.p004ui.Components.ThemeEditorView.getInstance();
        if (r0 == 0) goto L_0x000c;
    L_0x0009:
        r0.destroy();
    L_0x000c:
        r0 = r5.pathToFile;	 Catch:{ Exception -> 0x0091 }
        r1 = "overrideThemeWallpaper";
        r2 = 0;
        r3 = "theme";
        r4 = 0;
        if (r0 != 0) goto L_0x003e;
    L_0x0016:
        r0 = r5.assetName;	 Catch:{ Exception -> 0x0091 }
        if (r0 == 0) goto L_0x001b;
    L_0x001a:
        goto L_0x003e;
    L_0x001b:
        if (r8 != 0) goto L_0x0032;
    L_0x001d:
        if (r6 == 0) goto L_0x0032;
    L_0x001f:
        r6 = org.telegram.messenger.MessagesController.getGlobalMainSettings();	 Catch:{ Exception -> 0x0091 }
        r6 = r6.edit();	 Catch:{ Exception -> 0x0091 }
        r6.remove(r3);	 Catch:{ Exception -> 0x0091 }
        if (r7 == 0) goto L_0x002f;
    L_0x002c:
        r6.remove(r1);	 Catch:{ Exception -> 0x0091 }
    L_0x002f:
        r6.commit();	 Catch:{ Exception -> 0x0091 }
    L_0x0032:
        r6 = currentColors;	 Catch:{ Exception -> 0x0091 }
        r6.clear();	 Catch:{ Exception -> 0x0091 }
        themedWallpaperFileOffset = r2;	 Catch:{ Exception -> 0x0091 }
        wallpaper = r4;	 Catch:{ Exception -> 0x0091 }
        themedWallpaper = r4;	 Catch:{ Exception -> 0x0091 }
        goto L_0x0071;
    L_0x003e:
        if (r8 != 0) goto L_0x0057;
    L_0x0040:
        if (r6 == 0) goto L_0x0057;
    L_0x0042:
        r6 = org.telegram.messenger.MessagesController.getGlobalMainSettings();	 Catch:{ Exception -> 0x0091 }
        r6 = r6.edit();	 Catch:{ Exception -> 0x0091 }
        r0 = r5.name;	 Catch:{ Exception -> 0x0091 }
        r6.putString(r3, r0);	 Catch:{ Exception -> 0x0091 }
        if (r7 == 0) goto L_0x0054;
    L_0x0051:
        r6.remove(r1);	 Catch:{ Exception -> 0x0091 }
    L_0x0054:
        r6.commit();	 Catch:{ Exception -> 0x0091 }
    L_0x0057:
        r6 = r5.assetName;	 Catch:{ Exception -> 0x0091 }
        if (r6 == 0) goto L_0x0064;
    L_0x005b:
        r6 = r5.assetName;	 Catch:{ Exception -> 0x0091 }
        r6 = org.telegram.p004ui.ActionBar.Theme.getThemeFileValues(r4, r6);	 Catch:{ Exception -> 0x0091 }
        currentColors = r6;	 Catch:{ Exception -> 0x0091 }
        goto L_0x0071;
    L_0x0064:
        r6 = new java.io.File;	 Catch:{ Exception -> 0x0091 }
        r7 = r5.pathToFile;	 Catch:{ Exception -> 0x0091 }
        r6.<init>(r7);	 Catch:{ Exception -> 0x0091 }
        r6 = org.telegram.p004ui.ActionBar.Theme.getThemeFileValues(r6, r4);	 Catch:{ Exception -> 0x0091 }
        currentColors = r6;	 Catch:{ Exception -> 0x0091 }
    L_0x0071:
        currentTheme = r5;	 Catch:{ Exception -> 0x0091 }
        if (r8 != 0) goto L_0x0079;
    L_0x0075:
        r5 = currentTheme;	 Catch:{ Exception -> 0x0091 }
        currentDayTheme = r5;	 Catch:{ Exception -> 0x0091 }
    L_0x0079:
        org.telegram.p004ui.ActionBar.Theme.reloadWallpaper();	 Catch:{ Exception -> 0x0091 }
        org.telegram.p004ui.ActionBar.Theme.applyCommonTheme();	 Catch:{ Exception -> 0x0091 }
        org.telegram.p004ui.ActionBar.Theme.applyDialogsTheme();	 Catch:{ Exception -> 0x0091 }
        org.telegram.p004ui.ActionBar.Theme.applyProfileTheme();	 Catch:{ Exception -> 0x0091 }
        org.telegram.p004ui.ActionBar.Theme.applyChatTheme(r2);	 Catch:{ Exception -> 0x0091 }
        r5 = new org.telegram.ui.ActionBar.-$$Lambda$Theme$U5dmA2RnRuUehj9EUY9kmkkhUlE;	 Catch:{ Exception -> 0x0091 }
        r5.<init>(r8);	 Catch:{ Exception -> 0x0091 }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r5);	 Catch:{ Exception -> 0x0091 }
        goto L_0x0095;
    L_0x0091:
        r5 = move-exception;
        org.telegram.messenger.FileLog.m30e(r5);
    L_0x0095:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.applyTheme(org.telegram.ui.ActionBar.Theme$ThemeInfo, boolean, boolean, boolean):void");
    }

    private static void saveOtherThemes() {
        int i = 0;
        Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
        JSONArray jSONArray = new JSONArray();
        while (i < otherThemes.size()) {
            JSONObject saveJson = ((ThemeInfo) otherThemes.get(i)).getSaveJson();
            if (saveJson != null) {
                jSONArray.put(saveJson);
            }
            i++;
        }
        edit.putString("themes2", jSONArray.toString());
        edit.commit();
    }

    public static HashMap<String, Integer> getDefaultColors() {
        return defaultColors;
    }

    public static String getCurrentThemeName() {
        String name = currentDayTheme.getName();
        return name.toLowerCase().endsWith(".attheme") ? name.substring(0, name.lastIndexOf(46)) : name;
    }

    public static String getCurrentNightThemeName() {
        ThemeInfo themeInfo = currentNightTheme;
        if (themeInfo == null) {
            return "";
        }
        String name = themeInfo.getName();
        if (name.toLowerCase().endsWith(".attheme")) {
            name = name.substring(0, name.lastIndexOf(46));
        }
        return name;
    }

    public static ThemeInfo getCurrentTheme() {
        ThemeInfo themeInfo = currentDayTheme;
        return themeInfo != null ? themeInfo : defaultTheme;
    }

    public static ThemeInfo getCurrentNightTheme() {
        return currentNightTheme;
    }

    public static boolean isCurrentThemeNight() {
        return currentTheme == currentNightTheme;
    }

    public static boolean isCurrentThemeDefault() {
        return currentTheme == defaultTheme;
    }

    private static long getAutoNightSwitchThemeDelay() {
        return Math.abs(lastThemeSwitchTime - SystemClock.elapsedRealtime()) >= 12000 ? 1800 : 12000;
    }

    public static void setCurrentNightTheme(ThemeInfo themeInfo) {
        Object obj = currentTheme == currentNightTheme ? 1 : null;
        currentNightTheme = themeInfo;
        if (obj != null) {
            Theme.applyDayNightThemeMaybe(true);
        }
    }

    public static void checkAutoNightThemeConditions() {
        Theme.checkAutoNightThemeConditions(false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:67:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0117  */
    /* JADX WARNING: Missing block: B:59:0x00ff, code skipped:
            if (switchNightRunnableScheduled == false) goto L_0x0101;
     */
    /* JADX WARNING: Missing block: B:62:0x0105, code skipped:
            if (switchDayRunnableScheduled == false) goto L_0x010a;
     */
    /* JADX WARNING: Missing block: B:63:0x0108, code skipped:
            if (r1 == 0) goto L_0x010a;
     */
    public static void checkAutoNightThemeConditions(boolean r12) {
        /*
        r0 = previousTheme;
        if (r0 == 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = 0;
        if (r12 == 0) goto L_0x001e;
    L_0x0008:
        r1 = switchNightRunnableScheduled;
        if (r1 == 0) goto L_0x0013;
    L_0x000c:
        switchNightRunnableScheduled = r0;
        r1 = switchNightBrightnessRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r1);
    L_0x0013:
        r1 = switchDayRunnableScheduled;
        if (r1 == 0) goto L_0x001e;
    L_0x0017:
        switchDayRunnableScheduled = r0;
        r1 = switchDayBrightnessRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r1);
    L_0x001e:
        r1 = selectedAutoNightType;
        r2 = 2;
        if (r1 == r2) goto L_0x0055;
    L_0x0023:
        r1 = switchNightRunnableScheduled;
        if (r1 == 0) goto L_0x002e;
    L_0x0027:
        switchNightRunnableScheduled = r0;
        r1 = switchNightBrightnessRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r1);
    L_0x002e:
        r1 = switchDayRunnableScheduled;
        if (r1 == 0) goto L_0x0039;
    L_0x0032:
        switchDayRunnableScheduled = r0;
        r1 = switchDayBrightnessRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r1);
    L_0x0039:
        r1 = lightSensorRegistered;
        if (r1 == 0) goto L_0x0055;
    L_0x003d:
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        lastBrightnessValue = r1;
        r1 = sensorManager;
        r3 = ambientSensorListener;
        r4 = lightSensor;
        r1.unregisterListener(r3, r4);
        lightSensorRegistered = r0;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x0055;
    L_0x0050:
        r1 = "light sensor unregistered";
        org.telegram.messenger.FileLog.m27d(r1);
    L_0x0055:
        r1 = selectedAutoNightType;
        r3 = 5;
        r4 = 1;
        if (r1 != r4) goto L_0x00be;
    L_0x005b:
        r1 = java.util.Calendar.getInstance();
        r5 = java.lang.System.currentTimeMillis();
        r1.setTimeInMillis(r5);
        r5 = 11;
        r5 = r1.get(r5);
        r5 = r5 * 60;
        r6 = 12;
        r6 = r1.get(r6);
        r5 = r5 + r6;
        r6 = autoNightScheduleByLocation;
        if (r6 == 0) goto L_0x00a8;
    L_0x0079:
        r1 = r1.get(r3);
        r3 = autoNightLastSunCheckDay;
        if (r3 == r1) goto L_0x00a3;
    L_0x0081:
        r6 = autoNightLocationLatitude;
        r8 = 4666723172467343360; // 0x40c3880000000000 float:0.0 double:10000.0;
        r3 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r3 == 0) goto L_0x00a3;
    L_0x008c:
        r10 = autoNightLocationLongitude;
        r3 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r3 == 0) goto L_0x00a3;
    L_0x0092:
        r3 = org.telegram.messenger.time.SunDate.calculateSunriseSunset(r6, r10);
        r6 = r3[r0];
        autoNightSunriseTime = r6;
        r3 = r3[r4];
        autoNightSunsetTime = r3;
        autoNightLastSunCheckDay = r1;
        org.telegram.p004ui.ActionBar.Theme.saveAutoNightThemeConfig();
    L_0x00a3:
        r1 = autoNightSunsetTime;
        r3 = autoNightSunriseTime;
        goto L_0x00ac;
    L_0x00a8:
        r1 = autoNightDayStartTime;
        r3 = autoNightDayEndTime;
    L_0x00ac:
        if (r1 >= r3) goto L_0x00b3;
    L_0x00ae:
        if (r1 > r5) goto L_0x010a;
    L_0x00b0:
        if (r5 > r3) goto L_0x010a;
    L_0x00b2:
        goto L_0x00bd;
    L_0x00b3:
        if (r1 > r5) goto L_0x00b9;
    L_0x00b5:
        r1 = 1440; // 0x5a0 float:2.018E-42 double:7.115E-321;
        if (r5 <= r1) goto L_0x0101;
    L_0x00b9:
        if (r5 < 0) goto L_0x010a;
    L_0x00bb:
        if (r5 > r3) goto L_0x010a;
    L_0x00bd:
        goto L_0x0101;
    L_0x00be:
        if (r1 != r2) goto L_0x0108;
    L_0x00c0:
        r1 = lightSensor;
        if (r1 != 0) goto L_0x00d8;
    L_0x00c4:
        r1 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r5 = "sensor";
        r1 = r1.getSystemService(r5);
        r1 = (android.hardware.SensorManager) r1;
        sensorManager = r1;
        r1 = sensorManager;
        r1 = r1.getDefaultSensor(r3);
        lightSensor = r1;
    L_0x00d8:
        r1 = lightSensorRegistered;
        if (r1 != 0) goto L_0x00f5;
    L_0x00dc:
        r1 = lightSensor;
        if (r1 == 0) goto L_0x00f5;
    L_0x00e0:
        r3 = sensorManager;
        r5 = ambientSensorListener;
        r6 = 500000; // 0x7a120 float:7.00649E-40 double:2.47033E-318;
        r3.registerListener(r5, r1, r6);
        lightSensorRegistered = r4;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x00f5;
    L_0x00f0:
        r1 = "light sensor registered";
        org.telegram.messenger.FileLog.m27d(r1);
    L_0x00f5:
        r1 = lastBrightnessValue;
        r3 = autoNightBrighnessThreshold;
        r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r1 > 0) goto L_0x0103;
    L_0x00fd:
        r1 = switchNightRunnableScheduled;
        if (r1 != 0) goto L_0x010c;
    L_0x0101:
        r1 = 2;
        goto L_0x010d;
    L_0x0103:
        r1 = switchDayRunnableScheduled;
        if (r1 != 0) goto L_0x010c;
    L_0x0107:
        goto L_0x010a;
    L_0x0108:
        if (r1 != 0) goto L_0x010c;
    L_0x010a:
        r1 = 1;
        goto L_0x010d;
    L_0x010c:
        r1 = 0;
    L_0x010d:
        if (r1 == 0) goto L_0x0115;
    L_0x010f:
        if (r1 != r2) goto L_0x0112;
    L_0x0111:
        r0 = 1;
    L_0x0112:
        org.telegram.p004ui.ActionBar.Theme.applyDayNightThemeMaybe(r0);
    L_0x0115:
        if (r12 == 0) goto L_0x011b;
    L_0x0117:
        r0 = 0;
        lastThemeSwitchTime = r0;
    L_0x011b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.checkAutoNightThemeConditions(boolean):void");
    }

    private static void applyDayNightThemeMaybe(boolean z) {
        Boolean valueOf = Boolean.valueOf(true);
        if (z) {
            if (currentTheme != currentNightTheme) {
                lastThemeSwitchTime = SystemClock.elapsedRealtime();
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentNightTheme, valueOf);
            }
        } else if (currentTheme != currentDayTheme) {
            lastThemeSwitchTime = SystemClock.elapsedRealtime();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentDayTheme, valueOf);
        }
    }

    public static boolean deleteTheme(ThemeInfo themeInfo) {
        boolean z = false;
        if (themeInfo.pathToFile == null) {
            return false;
        }
        if (currentTheme == themeInfo) {
            Theme.applyTheme(defaultTheme, true, false, false);
            z = true;
        }
        otherThemes.remove(themeInfo);
        themesDict.remove(themeInfo.name);
        themes.remove(themeInfo);
        new File(themeInfo.pathToFile).delete();
        Theme.saveOtherThemes();
        return z;
    }

    public static File getAssetFile(String str) {
        File file = new File(ApplicationLoader.getFilesDirFixed(), str);
        long available;
        try {
            InputStream open = ApplicationLoader.applicationContext.getAssets().open(str);
            available = (long) open.available();
            open.close();
        } catch (Exception e) {
            FileLog.m30e(e);
            available = 0;
        }
        if (!(file.exists() && (available == 0 || file.length() == available))) {
            InputStream open2;
            try {
                open2 = ApplicationLoader.applicationContext.getAssets().open(str);
                AndroidUtilities.copyFile(open2, file);
                if (open2 != null) {
                    open2.close();
                }
            } catch (Exception e2) {
                FileLog.m30e(e2);
            } catch (Throwable unused) {
            }
        }
        return file;
    }

    public static void createCommonResources(Context context) {
        if (dividerPaint == null) {
            dividerPaint = new Paint();
            dividerPaint.setStrokeWidth(1.0f);
            avatar_backgroundPaint = new Paint(1);
            checkboxSquare_checkPaint = new Paint(1);
            checkboxSquare_checkPaint.setStyle(Style.STROKE);
            checkboxSquare_checkPaint.setStrokeWidth((float) AndroidUtilities.m26dp(2.0f));
            checkboxSquare_eraserPaint = new Paint(1);
            checkboxSquare_eraserPaint.setColor(0);
            checkboxSquare_eraserPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            checkboxSquare_backgroundPaint = new Paint(1);
            linkSelectionPaint = new Paint();
            Resources resources = context.getResources();
            avatar_broadcastDrawable = resources.getDrawable(C1067R.C1065drawable.broadcast_w);
            avatar_savedDrawable = resources.getDrawable(C1067R.C1065drawable.chats_saved);
            dialogs_archiveAvatarDrawable = new LottieDrawable();
            dialogs_archiveAvatarDrawable.setComposition((LottieComposition) LottieCompositionFactory.fromRawResSync(context, C1067R.raw.chats_archiveavatar).getValue());
            if (VERSION.SDK_INT == 24) {
                dialogs_archiveDrawable = resources.getDrawable(C1067R.C1065drawable.chats_archive);
                dialogs_unarchiveDrawable = resources.getDrawable(C1067R.C1065drawable.chats_unarchive);
                dialogs_pinArchiveDrawable = resources.getDrawable(C1067R.C1065drawable.chats_archive_hide);
                dialogs_unpinArchiveDrawable = resources.getDrawable(C1067R.C1065drawable.chats_archive_show);
            } else {
                LottieDrawable lottieDrawable = new LottieDrawable();
                lottieDrawable.setComposition((LottieComposition) LottieCompositionFactory.fromRawResSync(context, C1067R.raw.chats_archive).getValue());
                dialogs_archiveDrawable = lottieDrawable;
                lottieDrawable = new LottieDrawable();
                lottieDrawable.setComposition((LottieComposition) LottieCompositionFactory.fromRawResSync(context, C1067R.raw.chats_unarchive).getValue());
                dialogs_unarchiveDrawable = lottieDrawable;
                lottieDrawable = new LottieDrawable();
                lottieDrawable.setComposition((LottieComposition) LottieCompositionFactory.fromRawResSync(context, C1067R.raw.chats_hide).getValue());
                dialogs_pinArchiveDrawable = lottieDrawable;
                lottieDrawable = new LottieDrawable();
                lottieDrawable.setComposition((LottieComposition) LottieCompositionFactory.fromRawResSync(context, C1067R.raw.chats_unhide).getValue());
                dialogs_unpinArchiveDrawable = lottieDrawable;
            }
            Theme.applyCommonTheme();
        }
    }

    public static void applyCommonTheme() {
        Paint paint = dividerPaint;
        if (paint != null) {
            paint.setColor(Theme.getColor(key_divider));
            linkSelectionPaint.setColor(Theme.getColor(key_windowBackgroundWhiteLinkSelection));
            Drawable drawable = avatar_broadcastDrawable;
            String str = key_avatar_text;
            Theme.setDrawableColorByKey(drawable, str);
            Theme.setDrawableColorByKey(avatar_savedDrawable, str);
            LottieDrawable lottieDrawable = dialogs_archiveAvatarDrawable;
            String[] strArr = new String[1];
            String str2 = "**";
            strArr[0] = str2;
            lottieDrawable.addValueCallback(new KeyPath(strArr), LottieProperty.COLOR_FILTER, null);
            lottieDrawable = dialogs_archiveAvatarDrawable;
            r8 = new String[2];
            r8[0] = "Arrow1";
            r8[1] = str2;
            KeyPath keyPath = new KeyPath(r8);
            ColorFilter colorFilter = LottieProperty.COLOR_FILTER;
            String str3 = key_avatar_backgroundArchived;
            lottieDrawable.addValueCallback(keyPath, colorFilter, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str3))));
            lottieDrawable = dialogs_archiveAvatarDrawable;
            r8 = new String[2];
            r8[0] = "Arrow2";
            r8[1] = str2;
            lottieDrawable.addValueCallback(new KeyPath(r8), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str3))));
            lottieDrawable = dialogs_archiveAvatarDrawable;
            r8 = new String[2];
            r8[0] = "Box2";
            r8[1] = str2;
            lottieDrawable.addValueCallback(new KeyPath(r8), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str))));
            lottieDrawable = dialogs_archiveAvatarDrawable;
            r8 = new String[2];
            r8[0] = "Box1";
            r8[1] = str2;
            lottieDrawable.addValueCallback(new KeyPath(r8), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str))));
            dialogs_archiveAvatarDrawableRecolored = false;
            drawable = dialogs_pinArchiveDrawable;
            boolean z = drawable instanceof LottieDrawable;
            String str4 = "Line";
            String str5 = "Arrow";
            String str6 = key_chats_archiveIcon;
            if (z) {
                lottieDrawable = (LottieDrawable) drawable;
                lottieDrawable.addValueCallback(new KeyPath(str2), LottieProperty.COLOR_FILTER, null);
                lottieDrawable.addValueCallback(new KeyPath(str5, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
                lottieDrawable.addValueCallback(new KeyPath(str4, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
            } else {
                Theme.setDrawableColorByKey(drawable, str6);
            }
            drawable = dialogs_unpinArchiveDrawable;
            if (drawable instanceof LottieDrawable) {
                lottieDrawable = (LottieDrawable) drawable;
                lottieDrawable.addValueCallback(new KeyPath(str2), LottieProperty.COLOR_FILTER, null);
                lottieDrawable.addValueCallback(new KeyPath(str5, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
                lottieDrawable.addValueCallback(new KeyPath(str4, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
            } else {
                Theme.setDrawableColorByKey(drawable, str6);
            }
            drawable = dialogs_archiveDrawable;
            if (drawable instanceof LottieDrawable) {
                lottieDrawable = (LottieDrawable) drawable;
                lottieDrawable.addValueCallback(new KeyPath(str2), LottieProperty.COLOR_FILTER, null);
                lottieDrawable.addValueCallback(new KeyPath(str5, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(key_chats_archiveBackground))));
                lottieDrawable.addValueCallback(new KeyPath(r11, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
                lottieDrawable.addValueCallback(new KeyPath(str3, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
                dialogs_archiveDrawableRecolored = false;
            } else {
                Theme.setDrawableColorByKey(drawable, str6);
            }
            drawable = dialogs_unarchiveDrawable;
            if (drawable instanceof LottieDrawable) {
                lottieDrawable = (LottieDrawable) drawable;
                lottieDrawable.addValueCallback(new KeyPath(str2), LottieProperty.COLOR_FILTER, null);
                lottieDrawable.addValueCallback(new KeyPath(r9, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
                lottieDrawable.addValueCallback(new KeyPath(r10, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(key_chats_archivePinBackground))));
                lottieDrawable.addValueCallback(new KeyPath(r11, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
                lottieDrawable.addValueCallback(new KeyPath(str3, str2), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor(str6))));
            } else {
                Theme.setDrawableColorByKey(drawable, str6);
            }
        }
    }

    public static void createDialogsResources(Context context) {
        Theme.createCommonResources(context);
        if (dialogs_namePaint == null) {
            Resources resources = context.getResources();
            dialogs_namePaint = new TextPaint(1);
            String str = "fonts/rmedium.ttf";
            dialogs_namePaint.setTypeface(AndroidUtilities.getTypeface(str));
            dialogs_nameEncryptedPaint = new TextPaint(1);
            dialogs_nameEncryptedPaint.setTypeface(AndroidUtilities.getTypeface(str));
            dialogs_searchNamePaint = new TextPaint(1);
            dialogs_searchNamePaint.setTypeface(AndroidUtilities.getTypeface(str));
            dialogs_searchNameEncryptedPaint = new TextPaint(1);
            dialogs_searchNameEncryptedPaint.setTypeface(AndroidUtilities.getTypeface(str));
            dialogs_messagePaint = new TextPaint(1);
            dialogs_messageNamePaint = new TextPaint(1);
            dialogs_messageNamePaint.setTypeface(AndroidUtilities.getTypeface(str));
            dialogs_messagePrintingPaint = new TextPaint(1);
            dialogs_timePaint = new TextPaint(1);
            dialogs_countTextPaint = new TextPaint(1);
            dialogs_countTextPaint.setTypeface(AndroidUtilities.getTypeface(str));
            dialogs_archiveTextPaint = new TextPaint(1);
            dialogs_archiveTextPaint.setTypeface(AndroidUtilities.getTypeface(str));
            dialogs_onlinePaint = new TextPaint(1);
            dialogs_offlinePaint = new TextPaint(1);
            dialogs_tabletSeletedPaint = new Paint();
            dialogs_pinnedPaint = new Paint(1);
            dialogs_onlineCirclePaint = new Paint(1);
            dialogs_countPaint = new Paint(1);
            dialogs_countGrayPaint = new Paint(1);
            dialogs_errorPaint = new Paint(1);
            dialogs_lockDrawable = resources.getDrawable(C1067R.C1065drawable.list_secret);
            dialogs_checkDrawable = resources.getDrawable(C1067R.C1065drawable.list_check);
            dialogs_halfCheckDrawable = resources.getDrawable(C1067R.C1065drawable.list_halfcheck);
            dialogs_clockDrawable = resources.getDrawable(C1067R.C1065drawable.msg_clock).mutate();
            dialogs_errorDrawable = resources.getDrawable(C1067R.C1065drawable.list_warning_sign);
            dialogs_reorderDrawable = resources.getDrawable(C1067R.C1065drawable.list_reorder);
            dialogs_groupDrawable = resources.getDrawable(C1067R.C1065drawable.list_group);
            dialogs_broadcastDrawable = resources.getDrawable(C1067R.C1065drawable.list_broadcast);
            dialogs_muteDrawable = resources.getDrawable(C1067R.C1065drawable.list_mute).mutate();
            dialogs_verifiedDrawable = resources.getDrawable(C1067R.C1065drawable.verified_area);
            dialogs_scamDrawable = new ScamDrawable(11);
            dialogs_verifiedCheckDrawable = resources.getDrawable(C1067R.C1065drawable.verified_check);
            dialogs_mentionDrawable = resources.getDrawable(C1067R.C1065drawable.mentionchatslist);
            dialogs_botDrawable = resources.getDrawable(C1067R.C1065drawable.list_bot);
            dialogs_pinnedDrawable = resources.getDrawable(C1067R.C1065drawable.list_pin);
            moveUpDrawable = resources.getDrawable(C1067R.C1065drawable.preview_open);
            Theme.applyDialogsTheme();
        }
        dialogs_messageNamePaint.setTextSize((float) AndroidUtilities.m26dp(14.0f));
        dialogs_timePaint.setTextSize((float) AndroidUtilities.m26dp(13.0f));
        dialogs_countTextPaint.setTextSize((float) AndroidUtilities.m26dp(13.0f));
        dialogs_archiveTextPaint.setTextSize((float) AndroidUtilities.m26dp(13.0f));
        dialogs_onlinePaint.setTextSize((float) AndroidUtilities.m26dp(15.0f));
        dialogs_offlinePaint.setTextSize((float) AndroidUtilities.m26dp(15.0f));
        dialogs_searchNamePaint.setTextSize((float) AndroidUtilities.m26dp(16.0f));
        dialogs_searchNameEncryptedPaint.setTextSize((float) AndroidUtilities.m26dp(16.0f));
    }

    public static void applyDialogsTheme() {
        TextPaint textPaint = dialogs_namePaint;
        if (textPaint != null) {
            String str = key_chats_name;
            textPaint.setColor(Theme.getColor(str));
            textPaint = dialogs_nameEncryptedPaint;
            String str2 = key_chats_secretName;
            textPaint.setColor(Theme.getColor(str2));
            dialogs_searchNamePaint.setColor(Theme.getColor(str));
            dialogs_searchNameEncryptedPaint.setColor(Theme.getColor(str2));
            textPaint = dialogs_messagePaint;
            int color = Theme.getColor(key_chats_message);
            textPaint.linkColor = color;
            textPaint.setColor(color);
            textPaint = dialogs_messageNamePaint;
            color = Theme.getColor(key_chats_nameMessage_threeLines);
            textPaint.linkColor = color;
            textPaint.setColor(color);
            dialogs_tabletSeletedPaint.setColor(Theme.getColor(key_chats_tabletSelectedOverlay));
            dialogs_pinnedPaint.setColor(Theme.getColor(key_chats_pinnedOverlay));
            dialogs_timePaint.setColor(Theme.getColor(key_chats_date));
            dialogs_countTextPaint.setColor(Theme.getColor(key_chats_unreadCounterText));
            dialogs_archiveTextPaint.setColor(Theme.getColor(key_chats_archiveText));
            dialogs_messagePrintingPaint.setColor(Theme.getColor(key_chats_actionMessage));
            dialogs_countPaint.setColor(Theme.getColor(key_chats_unreadCounter));
            dialogs_countGrayPaint.setColor(Theme.getColor(key_chats_unreadCounterMuted));
            dialogs_errorPaint.setColor(Theme.getColor(key_chats_sentError));
            dialogs_onlinePaint.setColor(Theme.getColor(key_windowBackgroundWhiteBlueText3));
            dialogs_offlinePaint.setColor(Theme.getColor(key_windowBackgroundWhiteGrayText3));
            Theme.setDrawableColorByKey(dialogs_lockDrawable, key_chats_secretIcon);
            Drawable drawable = dialogs_checkDrawable;
            str = key_chats_sentCheck;
            Theme.setDrawableColorByKey(drawable, str);
            Theme.setDrawableColorByKey(dialogs_halfCheckDrawable, str);
            Theme.setDrawableColorByKey(dialogs_clockDrawable, key_chats_sentClock);
            Theme.setDrawableColorByKey(dialogs_errorDrawable, key_chats_sentErrorIcon);
            drawable = dialogs_groupDrawable;
            str = key_chats_nameIcon;
            Theme.setDrawableColorByKey(drawable, str);
            Theme.setDrawableColorByKey(dialogs_broadcastDrawable, str);
            Theme.setDrawableColorByKey(dialogs_botDrawable, str);
            drawable = dialogs_pinnedDrawable;
            str = key_chats_pinnedIcon;
            Theme.setDrawableColorByKey(drawable, str);
            Theme.setDrawableColorByKey(dialogs_reorderDrawable, str);
            Theme.setDrawableColorByKey(dialogs_muteDrawable, key_chats_muteIcon);
            Theme.setDrawableColorByKey(dialogs_mentionDrawable, key_chats_mentionIcon);
            Theme.setDrawableColorByKey(dialogs_verifiedDrawable, key_chats_verifiedBackground);
            Theme.setDrawableColorByKey(dialogs_verifiedCheckDrawable, key_chats_verifiedCheck);
            Theme.setDrawableColorByKey(dialogs_holidayDrawable, key_actionBarDefaultTitle);
            Theme.setDrawableColorByKey(dialogs_scamDrawable, key_chats_draft);
        }
    }

    public static void destroyResources() {
        int i = 0;
        while (true) {
            Drawable[] drawableArr = chat_attachButtonDrawables;
            if (i < drawableArr.length) {
                if (drawableArr[i] != null) {
                    drawableArr[i].setCallback(null);
                }
                i++;
            } else {
                return;
            }
        }
    }

    public static void reloadAllResources(Context context) {
        Theme.destroyResources();
        if (chat_msgInDrawable != null) {
            chat_msgInDrawable = null;
            currentColor = 0;
            currentSelectedColor = 0;
            Theme.createChatResources(context, false);
        }
        if (dialogs_namePaint != null) {
            dialogs_namePaint = null;
            Theme.createDialogsResources(context);
        }
        if (profile_verifiedDrawable != null) {
            profile_verifiedDrawable = null;
            Theme.createProfileResources(context);
        }
    }

    public static void applyChatTheme(boolean z) {
        if (!(chat_msgTextPaint == null || chat_msgInDrawable == null || z)) {
            int i;
            chat_gamePaint.setColor(Theme.getColor(key_chat_previewGameText));
            chat_durationPaint.setColor(Theme.getColor(key_chat_previewDurationText));
            chat_botButtonPaint.setColor(Theme.getColor(key_chat_botButtonText));
            chat_urlPaint.setColor(Theme.getColor(key_chat_linkSelectBackground));
            chat_botProgressPaint.setColor(Theme.getColor(key_chat_botProgress));
            chat_deleteProgressPaint.setColor(Theme.getColor(key_chat_secretTimeText));
            chat_textSearchSelectionPaint.setColor(Theme.getColor(key_chat_textSelectBackground));
            chat_msgErrorPaint.setColor(Theme.getColor(key_chat_sentError));
            Paint paint = chat_statusPaint;
            String str = key_chat_status;
            paint.setColor(Theme.getColor(str));
            chat_statusRecordPaint.setColor(Theme.getColor(str));
            TextPaint textPaint = chat_actionTextPaint;
            str = key_chat_serviceText;
            textPaint.setColor(Theme.getColor(str));
            chat_actionTextPaint.linkColor = Theme.getColor(key_chat_serviceLink);
            chat_contextResult_titleTextPaint.setColor(Theme.getColor(key_windowBackgroundWhiteBlackText));
            chat_composeBackgroundPaint.setColor(Theme.getColor(key_chat_messagePanelBackground));
            chat_timeBackgroundPaint.setColor(Theme.getColor(key_chat_mediaTimeBackground));
            Theme.setDrawableColorByKey(chat_msgNoSoundDrawable, key_chat_mediaTimeText);
            Drawable drawable = chat_msgInDrawable;
            String str2 = key_chat_inBubble;
            Theme.setDrawableColorByKey(drawable, str2);
            drawable = chat_msgInSelectedDrawable;
            String str3 = key_chat_inBubbleSelected;
            Theme.setDrawableColorByKey(drawable, str3);
            drawable = chat_msgInShadowDrawable;
            String str4 = key_chat_inBubbleShadow;
            Theme.setDrawableColorByKey(drawable, str4);
            drawable = chat_msgOutDrawable;
            String str5 = key_chat_outBubble;
            Theme.setDrawableColorByKey(drawable, str5);
            Theme.setDrawableColorByKey(chat_msgOutSelectedDrawable, key_chat_outBubbleSelected);
            Theme.setDrawableColorByKey(chat_msgOutShadowDrawable, key_chat_outBubbleShadow);
            Theme.setDrawableColorByKey(chat_msgInMediaDrawable, str2);
            Theme.setDrawableColorByKey(chat_msgInMediaSelectedDrawable, str3);
            Theme.setDrawableColorByKey(chat_msgInMediaShadowDrawable, str4);
            Theme.setDrawableColorByKey(chat_msgOutMediaDrawable, str5);
            Theme.setDrawableColorByKey(chat_msgOutMediaSelectedDrawable, key_chat_outBubbleSelected);
            Theme.setDrawableColorByKey(chat_msgOutMediaShadowDrawable, key_chat_outBubbleShadow);
            Theme.setDrawableColorByKey(chat_msgOutCheckDrawable, key_chat_outSentCheck);
            Theme.setDrawableColorByKey(chat_msgOutCheckSelectedDrawable, key_chat_outSentCheckSelected);
            Theme.setDrawableColorByKey(chat_msgOutHalfCheckDrawable, key_chat_outSentCheck);
            Theme.setDrawableColorByKey(chat_msgOutHalfCheckSelectedDrawable, key_chat_outSentCheckSelected);
            Theme.setDrawableColorByKey(chat_msgOutClockDrawable, key_chat_outSentClock);
            Theme.setDrawableColorByKey(chat_msgOutSelectedClockDrawable, key_chat_outSentClockSelected);
            Theme.setDrawableColorByKey(chat_msgInClockDrawable, key_chat_inSentClock);
            Theme.setDrawableColorByKey(chat_msgInSelectedClockDrawable, key_chat_inSentClockSelected);
            Theme.setDrawableColorByKey(chat_msgMediaCheckDrawable, key_chat_mediaSentCheck);
            Theme.setDrawableColorByKey(chat_msgMediaHalfCheckDrawable, key_chat_mediaSentCheck);
            Theme.setDrawableColorByKey(chat_msgMediaClockDrawable, key_chat_mediaSentClock);
            Theme.setDrawableColorByKey(chat_msgStickerCheckDrawable, str);
            Theme.setDrawableColorByKey(chat_msgStickerHalfCheckDrawable, str);
            Theme.setDrawableColorByKey(chat_msgStickerClockDrawable, str);
            Theme.setDrawableColorByKey(chat_msgStickerViewsDrawable, str);
            drawable = chat_shareIconDrawable;
            str = key_chat_serviceIcon;
            Theme.setDrawableColorByKey(drawable, str);
            Theme.setDrawableColorByKey(chat_replyIconDrawable, str);
            Theme.setDrawableColorByKey(chat_goIconDrawable, str);
            Theme.setDrawableColorByKey(chat_botInlineDrawable, str);
            Theme.setDrawableColorByKey(chat_botLinkDrawalbe, str);
            Theme.setDrawableColorByKey(chat_msgInViewsDrawable, key_chat_inViews);
            Theme.setDrawableColorByKey(chat_msgInViewsSelectedDrawable, key_chat_inViewsSelected);
            Theme.setDrawableColorByKey(chat_msgOutViewsDrawable, key_chat_outViews);
            Theme.setDrawableColorByKey(chat_msgOutViewsSelectedDrawable, key_chat_outViewsSelected);
            Theme.setDrawableColorByKey(chat_msgMediaViewsDrawable, key_chat_mediaViews);
            Theme.setDrawableColorByKey(chat_msgInMenuDrawable, key_chat_inMenu);
            Theme.setDrawableColorByKey(chat_msgInMenuSelectedDrawable, key_chat_inMenuSelected);
            Theme.setDrawableColorByKey(chat_msgOutMenuDrawable, key_chat_outMenu);
            Theme.setDrawableColorByKey(chat_msgOutMenuSelectedDrawable, key_chat_outMenuSelected);
            Theme.setDrawableColorByKey(chat_msgMediaMenuDrawable, key_chat_mediaMenu);
            Theme.setDrawableColorByKey(chat_msgOutInstantDrawable, key_chat_outInstant);
            Theme.setDrawableColorByKey(chat_msgInInstantDrawable, key_chat_inInstant);
            Theme.setDrawableColorByKey(chat_msgErrorDrawable, key_chat_sentErrorIcon);
            Theme.setDrawableColorByKey(chat_muteIconDrawable, key_chat_muteIcon);
            Theme.setDrawableColorByKey(chat_lockIconDrawable, key_chat_lockIcon);
            Theme.setDrawableColorByKey(chat_msgBroadcastDrawable, key_chat_outBroadcast);
            Theme.setDrawableColorByKey(chat_msgBroadcastMediaDrawable, key_chat_mediaBroadcast);
            drawable = chat_inlineResultFile;
            str = key_chat_inlineResultIcon;
            Theme.setDrawableColorByKey(drawable, str);
            Theme.setDrawableColorByKey(chat_inlineResultAudio, str);
            Theme.setDrawableColorByKey(chat_inlineResultLocation, str);
            Theme.setDrawableColorByKey(chat_msgInCallDrawable, key_chat_inInstant);
            Theme.setDrawableColorByKey(chat_msgInCallSelectedDrawable, key_chat_inInstantSelected);
            Theme.setDrawableColorByKey(chat_msgOutCallDrawable, key_chat_outInstant);
            Theme.setDrawableColorByKey(chat_msgOutCallSelectedDrawable, key_chat_outInstantSelected);
            Theme.setDrawableColorByKey(chat_msgCallUpGreenDrawable, key_chat_outGreenCall);
            Theme.setDrawableColorByKey(chat_msgCallDownRedDrawable, key_chat_inRedCall);
            Theme.setDrawableColorByKey(chat_msgCallDownGreenDrawable, key_chat_inGreenCall);
            Theme.setDrawableColorByKey(calllog_msgCallUpRedDrawable, key_calls_callReceivedRedIcon);
            Theme.setDrawableColorByKey(calllog_msgCallUpGreenDrawable, key_calls_callReceivedGreenIcon);
            Theme.setDrawableColorByKey(calllog_msgCallDownRedDrawable, key_calls_callReceivedRedIcon);
            Theme.setDrawableColorByKey(calllog_msgCallDownGreenDrawable, key_calls_callReceivedGreenIcon);
            for (i = 0; i < 2; i++) {
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i][0], Theme.getColor(key_chat_outLoader), false);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i][0], Theme.getColor(key_chat_outMediaIcon), true);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i][1], Theme.getColor(key_chat_outLoaderSelected), false);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i][1], Theme.getColor(key_chat_outMediaIconSelected), true);
                int i2 = i + 2;
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][0], Theme.getColor(key_chat_inLoader), false);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][0], Theme.getColor(key_chat_inMediaIcon), true);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][1], Theme.getColor(key_chat_inLoaderSelected), false);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][1], Theme.getColor(key_chat_inMediaIconSelected), true);
                i2 = i + 4;
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][0], Theme.getColor(key_chat_mediaLoaderPhoto), false);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][0], Theme.getColor(key_chat_mediaLoaderPhotoIcon), true);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][1], Theme.getColor(key_chat_mediaLoaderPhotoSelected), false);
                Theme.setCombinedDrawableColor(chat_fileMiniStatesDrawable[i2][1], Theme.getColor(key_chat_mediaLoaderPhotoIconSelected), true);
            }
            for (i = 0; i < 5; i++) {
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i][0], Theme.getColor(key_chat_outLoader), false);
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i][0], Theme.getColor(key_chat_outMediaIcon), true);
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i][1], Theme.getColor(key_chat_outLoaderSelected), false);
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i][1], Theme.getColor(key_chat_outMediaIconSelected), true);
                int i3 = i + 5;
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i3][0], Theme.getColor(key_chat_inLoader), false);
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i3][0], Theme.getColor(key_chat_inMediaIcon), true);
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i3][1], Theme.getColor(key_chat_inLoaderSelected), false);
                Theme.setCombinedDrawableColor(chat_fileStatesDrawable[i3][1], Theme.getColor(key_chat_inMediaIconSelected), true);
            }
            for (i = 0; i < 4; i++) {
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i][0], Theme.getColor(key_chat_mediaLoaderPhoto), false);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i][0], Theme.getColor(key_chat_mediaLoaderPhotoIcon), true);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i][1], Theme.getColor(key_chat_mediaLoaderPhotoSelected), false);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i][1], Theme.getColor(key_chat_mediaLoaderPhotoIconSelected), true);
            }
            for (i = 0; i < 2; i++) {
                int i4 = i + 7;
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][0], Theme.getColor(key_chat_outLoaderPhoto), false);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][0], Theme.getColor(key_chat_outLoaderPhotoIcon), true);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][1], Theme.getColor(key_chat_outLoaderPhotoSelected), false);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][1], Theme.getColor(key_chat_outLoaderPhotoIconSelected), true);
                i4 = i + 10;
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][0], Theme.getColor(key_chat_inLoaderPhoto), false);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][0], Theme.getColor(key_chat_inLoaderPhotoIcon), true);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][1], Theme.getColor(key_chat_inLoaderPhotoSelected), false);
                Theme.setCombinedDrawableColor(chat_photoStatesDrawables[i4][1], Theme.getColor(key_chat_inLoaderPhotoIconSelected), true);
            }
            Theme.setDrawableColorByKey(chat_photoStatesDrawables[9][0], key_chat_outFileIcon);
            Theme.setDrawableColorByKey(chat_photoStatesDrawables[9][1], key_chat_outFileSelectedIcon);
            Theme.setDrawableColorByKey(chat_photoStatesDrawables[12][0], key_chat_inFileIcon);
            Theme.setDrawableColorByKey(chat_photoStatesDrawables[12][1], key_chat_inFileSelectedIcon);
            Theme.setCombinedDrawableColor(chat_contactDrawable[0], Theme.getColor(key_chat_inContactBackground), false);
            Theme.setCombinedDrawableColor(chat_contactDrawable[0], Theme.getColor(key_chat_inContactIcon), true);
            Theme.setCombinedDrawableColor(chat_contactDrawable[1], Theme.getColor(key_chat_outContactBackground), false);
            Theme.setCombinedDrawableColor(chat_contactDrawable[1], Theme.getColor(key_chat_outContactIcon), true);
            Theme.setCombinedDrawableColor(chat_locationDrawable[0], Theme.getColor(key_chat_inLocationBackground), false);
            Theme.setCombinedDrawableColor(chat_locationDrawable[0], Theme.getColor(key_chat_inLocationIcon), true);
            Theme.setCombinedDrawableColor(chat_locationDrawable[1], Theme.getColor(key_chat_outLocationBackground), false);
            Theme.setCombinedDrawableColor(chat_locationDrawable[1], Theme.getColor(key_chat_outLocationIcon), true);
            Theme.setDrawableColorByKey(chat_composeShadowDrawable, key_chat_messagePanelShadow);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[1], Theme.getColor(key_chat_attachGalleryBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[1], Theme.getColor(key_chat_attachGalleryIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[2], Theme.getColor(key_chat_attachVideoBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[2], Theme.getColor(key_chat_attachVideoIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[3], Theme.getColor(key_chat_attachAudioBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[3], Theme.getColor(key_chat_attachAudioIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[4], Theme.getColor(key_chat_attachFileBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[4], Theme.getColor(key_chat_attachFileIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[5], Theme.getColor(key_chat_attachContactBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[5], Theme.getColor(key_chat_attachContactIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[6], Theme.getColor(key_chat_attachLocationBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[6], Theme.getColor(key_chat_attachLocationIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[7], Theme.getColor(key_chat_attachHideBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[7], Theme.getColor(key_chat_attachHideIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[8], Theme.getColor(key_chat_attachSendBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[8], Theme.getColor(key_chat_attachSendIcon), true);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[9], Theme.getColor(key_chat_attachPollBackground), false);
            Theme.setCombinedDrawableColor(chat_attachButtonDrawables[9], Theme.getColor(key_chat_attachPollIcon), true);
            Theme.applyChatServiceMessageColor();
        }
    }

    public static void applyChatServiceMessageColor() {
        Theme.applyChatServiceMessageColor(null);
    }

    public static void applyChatServiceMessageColor(int[] iArr) {
        if (chat_actionBackgroundPaint != null) {
            Integer num;
            Integer num2;
            Integer valueOf;
            Integer valueOf2;
            serviceMessageColor = serviceMessageColorBackup;
            serviceSelectedMessageColor = serviceSelectedMessageColorBackup;
            int i = 0;
            if (iArr == null || iArr.length < 2) {
                num = (Integer) currentColors.get(key_chat_serviceBackground);
                num2 = (Integer) currentColors.get(key_chat_serviceBackgroundSelected);
            } else {
                num = Integer.valueOf(iArr[0]);
                num2 = Integer.valueOf(iArr[1]);
                serviceMessageColor = iArr[0];
                serviceSelectedMessageColor = iArr[1];
            }
            if (num == null) {
                num = Integer.valueOf(serviceMessageColor);
                valueOf = Integer.valueOf(serviceMessage2Color);
            } else {
                valueOf = num;
            }
            if (num2 == null) {
                num2 = Integer.valueOf(serviceSelectedMessageColor);
                valueOf2 = Integer.valueOf(serviceSelectedMessage2Color);
            } else {
                valueOf2 = num2;
            }
            if (currentColor != num.intValue()) {
                chat_actionBackgroundPaint.setColor(num.intValue());
                colorFilter = new PorterDuffColorFilter(num.intValue(), Mode.MULTIPLY);
                colorFilter2 = new PorterDuffColorFilter(valueOf.intValue(), Mode.MULTIPLY);
                currentColor = num.intValue();
                if (chat_cornerOuter[0] != null) {
                    while (i < 4) {
                        chat_cornerOuter[i].setColorFilter(colorFilter);
                        chat_cornerInner[i].setColorFilter(colorFilter);
                        i++;
                    }
                }
            }
            if (currentSelectedColor != num2.intValue()) {
                currentSelectedColor = num2.intValue();
                colorPressedFilter = new PorterDuffColorFilter(num2.intValue(), Mode.MULTIPLY);
                colorPressedFilter2 = new PorterDuffColorFilter(valueOf2.intValue(), Mode.MULTIPLY);
            }
        }
    }

    public static void createProfileResources(Context context) {
        if (profile_verifiedDrawable == null) {
            profile_aboutTextPaint = new TextPaint(1);
            Resources resources = context.getResources();
            profile_verifiedDrawable = resources.getDrawable(C1067R.C1065drawable.verified_area).mutate();
            profile_verifiedCheckDrawable = resources.getDrawable(C1067R.C1065drawable.verified_check).mutate();
            Theme.applyProfileTheme();
        }
        profile_aboutTextPaint.setTextSize((float) AndroidUtilities.m26dp(16.0f));
    }

    public static ColorFilter getShareColorFilter(int i, boolean z) {
        if (z) {
            if (currentShareSelectedColorFilter == null || currentShareSelectedColorFilterColor != i) {
                currentShareSelectedColorFilterColor = i;
                currentShareSelectedColorFilter = new PorterDuffColorFilter(i, Mode.MULTIPLY);
            }
            return currentShareSelectedColorFilter;
        }
        if (currentShareColorFilter == null || currentShareColorFilterColor != i) {
            currentShareColorFilterColor = i;
            currentShareColorFilter = new PorterDuffColorFilter(i, Mode.MULTIPLY);
        }
        return currentShareColorFilter;
    }

    public static void applyProfileTheme() {
        if (profile_verifiedDrawable != null) {
            profile_aboutTextPaint.setColor(Theme.getColor(key_windowBackgroundWhiteBlackText));
            profile_aboutTextPaint.linkColor = Theme.getColor(key_windowBackgroundWhiteLinkText);
            Theme.setDrawableColorByKey(profile_verifiedDrawable, key_profile_verifiedBackground);
            Theme.setDrawableColorByKey(profile_verifiedCheckDrawable, key_profile_verifiedCheck);
        }
    }

    public static Drawable getThemedDrawable(Context context, int i, String str) {
        return Theme.getThemedDrawable(context, i, Theme.getColor(str));
    }

    public static Drawable getThemedDrawable(Context context, int i, int i2) {
        if (context == null) {
            return null;
        }
        Drawable mutate = context.getResources().getDrawable(i).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(i2, Mode.MULTIPLY));
        return mutate;
    }

    public static int getDefaultColor(String str) {
        Integer num = (Integer) defaultColors.get(str);
        if (num == null) {
            return str.equals(key_chats_menuTopShadow) ? 0 : -65536;
        } else {
            return num.intValue();
        }
    }

    public static boolean hasThemeKey(String str) {
        return currentColors.containsKey(str);
    }

    public static Integer getColorOrNull(String str) {
        Integer num = (Integer) currentColors.get(str);
        if (num != null) {
            return num;
        }
        if (((String) fallbackKeys.get(str)) != null) {
            num = (Integer) currentColors.get(str);
        }
        return num == null ? (Integer) defaultColors.get(str) : num;
    }

    public static void setAnimatingColor(boolean z) {
        animatingColors = z ? new HashMap() : null;
    }

    public static boolean isAnimatingColor() {
        return animatingColors != null;
    }

    public static void setAnimatedColor(String str, int i) {
        HashMap hashMap = animatingColors;
        if (hashMap != null) {
            hashMap.put(str, Integer.valueOf(i));
        }
    }

    public static int getColor(String str) {
        return Theme.getColor(str, null);
    }

    public static int getColor(String str, boolean[] zArr) {
        Integer num;
        HashMap hashMap = animatingColors;
        if (hashMap != null) {
            num = (Integer) hashMap.get(str);
            if (num != null) {
                return num.intValue();
            }
        }
        boolean isCurrentThemeDefault = Theme.isCurrentThemeDefault();
        String str2 = key_chat_serviceBackgroundSelected;
        String str3 = key_chat_serviceBackground;
        if (!isCurrentThemeDefault) {
            num = (Integer) currentColors.get(str);
            if (num == null) {
                String str4 = (String) fallbackKeys.get(str);
                if (str4 != null) {
                    num = (Integer) currentColors.get(str4);
                }
                if (num == null) {
                    if (zArr != null) {
                        zArr[0] = true;
                    }
                    if (str.equals(str3)) {
                        return serviceMessageColor;
                    }
                    if (str.equals(str2)) {
                        return serviceSelectedMessageColor;
                    }
                    return Theme.getDefaultColor(str);
                }
            }
            return num.intValue();
        } else if (str.equals(str3)) {
            return serviceMessageColor;
        } else {
            if (str.equals(str2)) {
                return serviceSelectedMessageColor;
            }
            return Theme.getDefaultColor(str);
        }
    }

    public static void setColor(String str, int i, boolean z) {
        String str2 = key_chat_wallpaper;
        if (str.equals(str2)) {
            i |= ACTION_BAR_VIDEO_EDIT_COLOR;
        }
        if (z) {
            currentColors.remove(str);
        } else {
            currentColors.put(str, Integer.valueOf(i));
        }
        if (str.equals(key_chat_serviceBackground) || str.equals(key_chat_serviceBackgroundSelected)) {
            Theme.applyChatServiceMessageColor();
        } else if (str.equals(str2)) {
            Theme.reloadWallpaper();
        }
    }

    public static void setThemeWallpaper(String str, Bitmap bitmap, File file) {
        currentColors.remove(key_chat_wallpaper);
        MessagesController.getGlobalMainSettings().edit().remove("overrideThemeWallpaper").commit();
        if (bitmap != null) {
            themedWallpaper = new BitmapDrawable(bitmap);
            Theme.saveCurrentTheme(str, false);
            Theme.calcBackgroundColor(themedWallpaper, 0);
            Theme.applyChatServiceMessageColor();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
            return;
        }
        themedWallpaper = null;
        wallpaper = null;
        Theme.saveCurrentTheme(str, false);
        Theme.reloadWallpaper();
    }

    public static void setDrawableColor(Drawable drawable, int i) {
        if (drawable != null) {
            if (drawable instanceof ShapeDrawable) {
                ((ShapeDrawable) drawable).getPaint().setColor(i);
            } else if (drawable instanceof ScamDrawable) {
                ((ScamDrawable) drawable).setColor(i);
            } else {
                drawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
            }
        }
    }

    public static void setDrawableColorByKey(Drawable drawable, String str) {
        if (str != null) {
            Theme.setDrawableColor(drawable, Theme.getColor(str));
        }
    }

    public static void setEmojiDrawableColor(Drawable drawable, int i, boolean z) {
        if (!(drawable instanceof StateListDrawable)) {
            return;
        }
        if (z) {
            try {
                drawable = Theme.getStateDrawable(drawable, 0);
                if (drawable instanceof ShapeDrawable) {
                    ((ShapeDrawable) drawable).getPaint().setColor(i);
                    return;
                } else {
                    drawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
                    return;
                }
            } catch (Throwable unused) {
                return;
            }
        }
        drawable = Theme.getStateDrawable(drawable, 1);
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(i);
        } else {
            drawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
        }
    }

    public static void setSelectorDrawableColor(Drawable drawable, int i, boolean z) {
        if (drawable instanceof StateListDrawable) {
            if (z) {
                try {
                    Drawable stateDrawable = Theme.getStateDrawable(drawable, 0);
                    if (stateDrawable instanceof ShapeDrawable) {
                        ((ShapeDrawable) stateDrawable).getPaint().setColor(i);
                    } else {
                        stateDrawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
                    }
                    drawable = Theme.getStateDrawable(drawable, 1);
                    if (drawable instanceof ShapeDrawable) {
                        ((ShapeDrawable) drawable).getPaint().setColor(i);
                        return;
                    } else {
                        drawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
                        return;
                    }
                } catch (Throwable unused) {
                    return;
                }
            }
            drawable = Theme.getStateDrawable(drawable, 2);
            if (drawable instanceof ShapeDrawable) {
                ((ShapeDrawable) drawable).getPaint().setColor(i);
            } else {
                drawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
            }
        } else if (VERSION.SDK_INT >= 21 && (drawable instanceof RippleDrawable)) {
            RippleDrawable rippleDrawable = (RippleDrawable) drawable;
            if (z) {
                rippleDrawable.setColor(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}));
            } else if (rippleDrawable.getNumberOfLayers() > 0) {
                drawable = rippleDrawable.getDrawable(0);
                if (drawable instanceof ShapeDrawable) {
                    ((ShapeDrawable) drawable).getPaint().setColor(i);
                } else {
                    drawable.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
                }
            }
        }
    }

    public static boolean hasWallpaperFromTheme() {
        return currentColors.containsKey(key_chat_wallpaper) || themedWallpaperFileOffset > 0;
    }

    public static boolean isCustomTheme() {
        return isCustomTheme;
    }

    public static int getSelectedColor() {
        return selectedColor;
    }

    public static void reloadWallpaper() {
        wallpaper = null;
        themedWallpaper = null;
        Theme.loadWallpaper();
    }

    private static void calcBackgroundColor(Drawable drawable, int i) {
        if (i != 2) {
            int[] calcDrawableColor = AndroidUtilities.calcDrawableColor(drawable);
            i = calcDrawableColor[0];
            serviceMessageColorBackup = i;
            serviceMessageColor = i;
            i = calcDrawableColor[1];
            serviceSelectedMessageColorBackup = i;
            serviceSelectedMessageColor = i;
            serviceMessage2Color = calcDrawableColor[2];
            serviceSelectedMessage2Color = calcDrawableColor[3];
        }
    }

    public static int getServiceMessageColor() {
        Integer num = (Integer) currentColors.get(key_chat_serviceBackground);
        return num == null ? serviceMessageColor : num.intValue();
    }

    public static void loadWallpaper() {
        if (wallpaper == null) {
            Utilities.searchQueue.postRunnable(C2186-$$Lambda$Theme$g9IkSg8DwYzdCeZKlfMvOXD2f9I.INSTANCE);
        }
    }

    static /* synthetic */ void lambda$null$2() {
        Theme.applyChatServiceMessageColor();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x009b A:{SYNTHETIC, Splitter:B:43:0x009b} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a7 A:{SYNTHETIC, Splitter:B:49:0x00a7} */
    public static android.graphics.drawable.Drawable getThemedWallpaper(boolean r8) {
        /*
        r0 = currentColors;
        r1 = "chat_wallpaper";
        r0 = r0.get(r1);
        r0 = (java.lang.Integer) r0;
        if (r0 == 0) goto L_0x0016;
    L_0x000c:
        r8 = new android.graphics.drawable.ColorDrawable;
        r0 = r0.intValue();
        r8.<init>(r0);
        return r8;
    L_0x0016:
        r0 = themedWallpaperFileOffset;
        r1 = 0;
        if (r0 <= 0) goto L_0x00b0;
    L_0x001b:
        r0 = currentTheme;
        r2 = r0.pathToFile;
        if (r2 != 0) goto L_0x0025;
    L_0x0021:
        r0 = r0.assetName;
        if (r0 == 0) goto L_0x00b0;
    L_0x0025:
        r0 = currentTheme;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r0 = r0.assetName;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        if (r0 == 0) goto L_0x0034;
    L_0x002b:
        r0 = currentTheme;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r0 = r0.assetName;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r0 = org.telegram.p004ui.ActionBar.Theme.getAssetFile(r0);	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        goto L_0x003d;
    L_0x0034:
        r0 = new java.io.File;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r2 = currentTheme;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r2 = r2.pathToFile;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r0.<init>(r2);	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
    L_0x003d:
        r2 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r2.<init>(r0);	 Catch:{ Throwable -> 0x0094, all -> 0x0091 }
        r0 = r2.getChannel();	 Catch:{ Throwable -> 0x008f }
        r3 = themedWallpaperFileOffset;	 Catch:{ Throwable -> 0x008f }
        r3 = (long) r3;	 Catch:{ Throwable -> 0x008f }
        r0.position(r3);	 Catch:{ Throwable -> 0x008f }
        r0 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x008f }
        r0.<init>();	 Catch:{ Throwable -> 0x008f }
        r3 = 1;
        if (r8 == 0) goto L_0x0072;
    L_0x0054:
        r0.inJustDecodeBounds = r3;	 Catch:{ Throwable -> 0x008f }
        r8 = r0.outWidth;	 Catch:{ Throwable -> 0x008f }
        r8 = (float) r8;	 Catch:{ Throwable -> 0x008f }
        r4 = r0.outHeight;	 Catch:{ Throwable -> 0x008f }
        r4 = (float) r4;	 Catch:{ Throwable -> 0x008f }
        r5 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);	 Catch:{ Throwable -> 0x008f }
    L_0x0062:
        r6 = (float) r5;	 Catch:{ Throwable -> 0x008f }
        r7 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
        if (r7 > 0) goto L_0x006b;
    L_0x0067:
        r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r6 <= 0) goto L_0x0072;
    L_0x006b:
        r3 = r3 * 2;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = r8 / r6;
        r4 = r4 / r6;
        goto L_0x0062;
    L_0x0072:
        r8 = 0;
        r0.inJustDecodeBounds = r8;	 Catch:{ Throwable -> 0x008f }
        r0.inSampleSize = r3;	 Catch:{ Throwable -> 0x008f }
        r8 = android.graphics.BitmapFactory.decodeStream(r2, r1, r0);	 Catch:{ Throwable -> 0x008f }
        if (r8 == 0) goto L_0x008b;
    L_0x007d:
        r0 = new android.graphics.drawable.BitmapDrawable;	 Catch:{ Throwable -> 0x008f }
        r0.<init>(r8);	 Catch:{ Throwable -> 0x008f }
        r2.close();	 Catch:{ Exception -> 0x0086 }
        goto L_0x008a;
    L_0x0086:
        r8 = move-exception;
        org.telegram.messenger.FileLog.m30e(r8);
    L_0x008a:
        return r0;
    L_0x008b:
        r2.close();	 Catch:{ Exception -> 0x009f }
        goto L_0x00b0;
    L_0x008f:
        r8 = move-exception;
        goto L_0x0096;
    L_0x0091:
        r8 = move-exception;
        r2 = r1;
        goto L_0x00a5;
    L_0x0094:
        r8 = move-exception;
        r2 = r1;
    L_0x0096:
        org.telegram.messenger.FileLog.m30e(r8);	 Catch:{ all -> 0x00a4 }
        if (r2 == 0) goto L_0x00b0;
    L_0x009b:
        r2.close();	 Catch:{ Exception -> 0x009f }
        goto L_0x00b0;
    L_0x009f:
        r8 = move-exception;
        org.telegram.messenger.FileLog.m30e(r8);
        goto L_0x00b0;
    L_0x00a4:
        r8 = move-exception;
    L_0x00a5:
        if (r2 == 0) goto L_0x00af;
    L_0x00a7:
        r2.close();	 Catch:{ Exception -> 0x00ab }
        goto L_0x00af;
    L_0x00ab:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x00af:
        throw r8;
    L_0x00b0:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ActionBar.Theme.getThemedWallpaper(boolean):android.graphics.drawable.Drawable");
    }

    public static long getSelectedBackgroundId() {
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        String str = "selectedBackground";
        long j = (long) globalMainSettings.getInt(str, 1000001);
        String str2 = "selectedBackground2";
        if (j != DEFAULT_BACKGROUND_ID) {
            globalMainSettings.edit().putLong(str2, j).remove(str).commit();
        }
        long j2 = globalMainSettings.getLong(str2, DEFAULT_BACKGROUND_ID);
        if (Theme.hasWallpaperFromTheme() && !globalMainSettings.getBoolean("overrideThemeWallpaper", false)) {
            return -2;
        }
        if (j2 == -2) {
            return DEFAULT_BACKGROUND_ID;
        }
        return j2;
    }

    public static Drawable getCachedWallpaper() {
        synchronized (wallpaperSync) {
            Drawable drawable;
            if (themedWallpaper != null) {
                drawable = themedWallpaper;
                return drawable;
            }
            drawable = wallpaper;
            return drawable;
        }
    }

    public static Drawable getCachedWallpaperNonBlocking() {
        Drawable drawable = themedWallpaper;
        if (drawable != null) {
            return drawable;
        }
        return wallpaper;
    }

    public static boolean isWallpaperMotion() {
        return isWallpaperMotion;
    }

    public static boolean isPatternWallpaper() {
        return isPatternWallpaper;
    }
}
