// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import org.telegram.messenger.LocaleController;
import android.graphics.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.json.JSONObject;
import android.graphics.Bitmap;
import java.util.Iterator;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.FileOutputStream;
import java.util.Map;
import org.telegram.messenger.Utilities;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.RippleDrawable;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.StateSet;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.content.res.Resources;
import android.os.Build$VERSION;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieComposition;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint$Style;
import android.graphics.drawable.shapes.Shape;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.content.Context;
import org.telegram.messenger.time.SunDate;
import java.util.Calendar;
import android.content.SharedPreferences$Editor;
import java.io.File;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.messenger.NotificationCenter;
import android.os.SystemClock;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import android.graphics.PorterDuff$Mode;
import android.content.SharedPreferences;
import org.telegram.messenger.MediaController;
import android.hardware.SensorEvent;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.FileLog;
import org.json.JSONArray;
import android.text.TextUtils;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import android.hardware.SensorManager;
import java.util.ArrayList;
import android.hardware.Sensor;
import org.telegram.ui.Components.ScamDrawable;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import org.telegram.ui.Components.CombinedDrawable;
import android.text.TextPaint;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import java.util.HashMap;
import android.hardware.SensorEventListener;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class Theme
{
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
    private static Field BitmapDrawable_mColorFilter;
    public static final long DEFAULT_BACKGROUND_ID = 1000001L;
    private static final int LIGHT_SENSOR_THEME_SWITCH_DELAY = 1800;
    private static final int LIGHT_SENSOR_THEME_SWITCH_NEAR_DELAY = 12000;
    private static final int LIGHT_SENSOR_THEME_SWITCH_NEAR_THRESHOLD = 12000;
    private static final float MAXIMUM_LUX_BREAKPOINT = 500.0f;
    private static Method StateListDrawable_getStateDrawableMethod;
    public static final long THEME_BACKGROUND_ID = -2L;
    private static SensorEventListener ambientSensorListener;
    private static HashMap<String, Integer> animatingColors;
    public static float autoNightBrighnessThreshold = 0.0f;
    public static String autoNightCityName;
    public static int autoNightDayEndTime = 0;
    public static int autoNightDayStartTime = 0;
    public static int autoNightLastSunCheckDay = 0;
    public static double autoNightLocationLatitude = 0.0;
    public static double autoNightLocationLongitude = 0.0;
    public static boolean autoNightScheduleByLocation = false;
    public static int autoNightSunriseTime = 0;
    public static int autoNightSunsetTime = 0;
    public static Paint avatar_backgroundPaint;
    public static Drawable avatar_broadcastDrawable;
    public static Drawable avatar_savedDrawable;
    public static Drawable calllog_msgCallDownGreenDrawable;
    public static Drawable calllog_msgCallDownRedDrawable;
    public static Drawable calllog_msgCallUpGreenDrawable;
    public static Drawable calllog_msgCallUpRedDrawable;
    private static boolean canStartHolidayAnimation = false;
    public static Paint chat_actionBackgroundPaint;
    public static TextPaint chat_actionTextPaint;
    public static TextPaint chat_adminPaint;
    public static Drawable[] chat_attachButtonDrawables;
    public static TextPaint chat_audioPerformerPaint;
    public static TextPaint chat_audioTimePaint;
    public static TextPaint chat_audioTitlePaint;
    public static TextPaint chat_botButtonPaint;
    public static Drawable chat_botInlineDrawable;
    public static Drawable chat_botLinkDrawalbe;
    public static Paint chat_botProgressPaint;
    public static Paint chat_composeBackgroundPaint;
    public static Drawable chat_composeShadowDrawable;
    public static Drawable[] chat_contactDrawable;
    public static TextPaint chat_contactNamePaint;
    public static TextPaint chat_contactPhonePaint;
    public static TextPaint chat_contextResult_descriptionTextPaint;
    public static Drawable chat_contextResult_shadowUnderSwitchDrawable;
    public static TextPaint chat_contextResult_titleTextPaint;
    public static Drawable[] chat_cornerInner;
    public static Drawable[] chat_cornerOuter;
    public static Paint chat_deleteProgressPaint;
    public static Paint chat_docBackPaint;
    public static TextPaint chat_docNamePaint;
    public static TextPaint chat_durationPaint;
    public static Drawable chat_fileIcon;
    public static CombinedDrawable[][] chat_fileMiniStatesDrawable;
    public static Drawable[][] chat_fileStatesDrawable;
    public static Drawable chat_flameIcon;
    public static TextPaint chat_forwardNamePaint;
    public static TextPaint chat_gamePaint;
    public static Drawable chat_gifIcon;
    public static Drawable chat_goIconDrawable;
    public static TextPaint chat_infoPaint;
    public static Drawable chat_inlineResultAudio;
    public static Drawable chat_inlineResultFile;
    public static Drawable chat_inlineResultLocation;
    public static TextPaint chat_instantViewPaint;
    public static Paint chat_instantViewRectPaint;
    public static TextPaint chat_livePaint;
    public static TextPaint chat_locationAddressPaint;
    public static Drawable[] chat_locationDrawable;
    public static TextPaint chat_locationTitlePaint;
    public static Drawable chat_lockIconDrawable;
    public static Drawable chat_msgAvatarLiveLocationDrawable;
    public static TextPaint chat_msgBotButtonPaint;
    public static Drawable chat_msgBroadcastDrawable;
    public static Drawable chat_msgBroadcastMediaDrawable;
    public static Drawable chat_msgCallDownGreenDrawable;
    public static Drawable chat_msgCallDownRedDrawable;
    public static Drawable chat_msgCallUpGreenDrawable;
    public static Drawable chat_msgErrorDrawable;
    public static Paint chat_msgErrorPaint;
    public static TextPaint chat_msgGameTextPaint;
    public static Drawable chat_msgInCallDrawable;
    public static Drawable chat_msgInCallSelectedDrawable;
    public static Drawable chat_msgInClockDrawable;
    public static Drawable chat_msgInDrawable;
    public static Drawable chat_msgInInstantDrawable;
    public static Drawable chat_msgInMediaDrawable;
    public static Drawable chat_msgInMediaSelectedDrawable;
    public static Drawable chat_msgInMediaShadowDrawable;
    public static Drawable chat_msgInMenuDrawable;
    public static Drawable chat_msgInMenuSelectedDrawable;
    public static Drawable chat_msgInSelectedClockDrawable;
    public static Drawable chat_msgInSelectedDrawable;
    public static Drawable chat_msgInShadowDrawable;
    public static Drawable chat_msgInViewsDrawable;
    public static Drawable chat_msgInViewsSelectedDrawable;
    public static Drawable chat_msgMediaBroadcastDrawable;
    public static Drawable chat_msgMediaCheckDrawable;
    public static Drawable chat_msgMediaClockDrawable;
    public static Drawable chat_msgMediaHalfCheckDrawable;
    public static Drawable chat_msgMediaMenuDrawable;
    public static Drawable chat_msgMediaViewsDrawable;
    public static Drawable chat_msgNoSoundDrawable;
    public static Drawable chat_msgOutBroadcastDrawable;
    public static Drawable chat_msgOutCallDrawable;
    public static Drawable chat_msgOutCallSelectedDrawable;
    public static Drawable chat_msgOutCheckDrawable;
    public static Drawable chat_msgOutCheckSelectedDrawable;
    public static Drawable chat_msgOutClockDrawable;
    public static Drawable chat_msgOutDrawable;
    public static Drawable chat_msgOutHalfCheckDrawable;
    public static Drawable chat_msgOutHalfCheckSelectedDrawable;
    public static Drawable chat_msgOutInstantDrawable;
    public static Drawable chat_msgOutLocationDrawable;
    public static Drawable chat_msgOutMediaDrawable;
    public static Drawable chat_msgOutMediaSelectedDrawable;
    public static Drawable chat_msgOutMediaShadowDrawable;
    public static Drawable chat_msgOutMenuDrawable;
    public static Drawable chat_msgOutMenuSelectedDrawable;
    public static Drawable chat_msgOutSelectedClockDrawable;
    public static Drawable chat_msgOutSelectedDrawable;
    public static Drawable chat_msgOutShadowDrawable;
    public static Drawable chat_msgOutViewsDrawable;
    public static Drawable chat_msgOutViewsSelectedDrawable;
    public static Drawable chat_msgStickerCheckDrawable;
    public static Drawable chat_msgStickerClockDrawable;
    public static Drawable chat_msgStickerHalfCheckDrawable;
    public static Drawable chat_msgStickerViewsDrawable;
    public static TextPaint chat_msgTextPaint;
    public static TextPaint chat_msgTextPaintOneEmoji;
    public static TextPaint chat_msgTextPaintThreeEmoji;
    public static TextPaint chat_msgTextPaintTwoEmoji;
    public static Drawable chat_muteIconDrawable;
    public static TextPaint chat_namePaint;
    public static Drawable[][] chat_photoStatesDrawables;
    public static Paint chat_radialProgress2Paint;
    public static Paint chat_radialProgressPaint;
    public static Drawable chat_redLocationIcon;
    public static Drawable chat_replyIconDrawable;
    public static Paint chat_replyLinePaint;
    public static TextPaint chat_replyNamePaint;
    public static TextPaint chat_replyTextPaint;
    public static Drawable chat_roundVideoShadow;
    public static Drawable chat_shareDrawable;
    public static Drawable chat_shareIconDrawable;
    public static TextPaint chat_shipmentPaint;
    public static Paint chat_statusPaint;
    public static Paint chat_statusRecordPaint;
    public static Drawable chat_systemDrawable;
    public static Paint chat_textSearchSelectionPaint;
    public static Paint chat_timeBackgroundPaint;
    public static TextPaint chat_timePaint;
    public static Paint chat_urlPaint;
    public static Paint checkboxSquare_backgroundPaint;
    public static Paint checkboxSquare_checkPaint;
    public static Paint checkboxSquare_eraserPaint;
    public static PorterDuffColorFilter colorFilter;
    public static PorterDuffColorFilter colorFilter2;
    public static PorterDuffColorFilter colorPressedFilter;
    public static PorterDuffColorFilter colorPressedFilter2;
    private static int currentColor = 0;
    private static HashMap<String, Integer> currentColors;
    private static ThemeInfo currentDayTheme;
    private static ThemeInfo currentNightTheme;
    private static int currentSelectedColor = 0;
    private static ColorFilter currentShareColorFilter;
    private static int currentShareColorFilterColor = 0;
    private static ColorFilter currentShareSelectedColorFilter;
    private static int currentShareSelectedColorFilterColor = 0;
    private static ThemeInfo currentTheme;
    private static HashMap<String, Integer> defaultColors;
    private static ThemeInfo defaultTheme;
    public static LottieDrawable dialogs_archiveAvatarDrawable;
    public static boolean dialogs_archiveAvatarDrawableRecolored = false;
    public static Drawable dialogs_archiveDrawable;
    public static boolean dialogs_archiveDrawableRecolored = false;
    public static TextPaint dialogs_archiveTextPaint;
    public static Drawable dialogs_botDrawable;
    public static Drawable dialogs_broadcastDrawable;
    public static Drawable dialogs_checkDrawable;
    public static Drawable dialogs_clockDrawable;
    public static Paint dialogs_countGrayPaint;
    public static Paint dialogs_countPaint;
    public static TextPaint dialogs_countTextPaint;
    public static Drawable dialogs_errorDrawable;
    public static Paint dialogs_errorPaint;
    public static Drawable dialogs_groupDrawable;
    public static Drawable dialogs_halfCheckDrawable;
    public static Drawable dialogs_holidayDrawable;
    private static int dialogs_holidayDrawableOffsetX = 0;
    private static int dialogs_holidayDrawableOffsetY = 0;
    public static Drawable dialogs_lockDrawable;
    public static Drawable dialogs_mentionDrawable;
    public static TextPaint dialogs_messageNamePaint;
    public static TextPaint dialogs_messagePaint;
    public static TextPaint dialogs_messagePrintingPaint;
    public static Drawable dialogs_muteDrawable;
    public static TextPaint dialogs_nameEncryptedPaint;
    public static TextPaint dialogs_namePaint;
    public static TextPaint dialogs_offlinePaint;
    public static Paint dialogs_onlineCirclePaint;
    public static TextPaint dialogs_onlinePaint;
    public static Drawable dialogs_pinArchiveDrawable;
    public static Drawable dialogs_pinnedDrawable;
    public static Paint dialogs_pinnedPaint;
    public static Drawable dialogs_reorderDrawable;
    public static ScamDrawable dialogs_scamDrawable;
    public static TextPaint dialogs_searchNameEncryptedPaint;
    public static TextPaint dialogs_searchNamePaint;
    public static Paint dialogs_tabletSeletedPaint;
    public static TextPaint dialogs_timePaint;
    public static Drawable dialogs_unarchiveDrawable;
    public static Drawable dialogs_unpinArchiveDrawable;
    public static Drawable dialogs_verifiedCheckDrawable;
    public static Drawable dialogs_verifiedDrawable;
    public static Paint dividerPaint;
    private static HashMap<String, String> fallbackKeys;
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
    public static String[] keys_avatar_background;
    public static String[] keys_avatar_nameInMessage;
    private static float lastBrightnessValue;
    private static long lastHolidayCheckTime;
    private static long lastThemeSwitchTime;
    private static Sensor lightSensor;
    private static boolean lightSensorRegistered;
    public static Paint linkSelectionPaint;
    public static Drawable listSelector;
    private static Paint maskPaint;
    public static Drawable moveUpDrawable;
    private static ArrayList<ThemeInfo> otherThemes;
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
    private static Runnable switchDayBrightnessRunnable;
    private static boolean switchDayRunnableScheduled;
    private static Runnable switchNightBrightnessRunnable;
    private static boolean switchNightRunnableScheduled;
    private static final Object sync;
    private static Drawable themedWallpaper;
    private static int themedWallpaperFileOffset;
    public static ArrayList<ThemeInfo> themes;
    private static HashMap<String, ThemeInfo> themesDict;
    private static Drawable wallpaper;
    private static final Object wallpaperSync;
    
    static {
        sync = new Object();
        wallpaperSync = new Object();
        Theme.lastBrightnessValue = 1.0f;
        Theme.switchDayBrightnessRunnable = new Runnable() {
            @Override
            public void run() {
                Theme.switchDayRunnableScheduled = false;
                applyDayNightThemeMaybe(false);
            }
        };
        Theme.switchNightBrightnessRunnable = new Runnable() {
            @Override
            public void run() {
                Theme.switchNightRunnableScheduled = false;
                applyDayNightThemeMaybe(true);
            }
        };
        Theme.selectedAutoNightType = 0;
        Theme.autoNightBrighnessThreshold = 0.25f;
        Theme.autoNightDayStartTime = 1320;
        Theme.autoNightDayEndTime = 480;
        Theme.autoNightSunsetTime = 1320;
        final Integer value = -1;
        Theme.autoNightLastSunCheckDay = -1;
        Theme.autoNightSunriseTime = 480;
        Theme.autoNightCityName = "";
        Theme.autoNightLocationLatitude = 10000.0;
        Theme.autoNightLocationLongitude = 10000.0;
        Theme.maskPaint = new Paint(1);
        Theme.chat_attachButtonDrawables = new Drawable[10];
        Theme.chat_locationDrawable = new Drawable[2];
        Theme.chat_contactDrawable = new Drawable[2];
        Theme.chat_cornerOuter = new Drawable[4];
        Theme.chat_cornerInner = new Drawable[4];
        Theme.chat_fileStatesDrawable = new Drawable[10][2];
        Theme.chat_fileMiniStatesDrawable = new CombinedDrawable[6][2];
        Theme.chat_photoStatesDrawables = new Drawable[13][2];
        Theme.keys_avatar_background = new String[] { "avatar_backgroundRed", "avatar_backgroundOrange", "avatar_backgroundViolet", "avatar_backgroundGreen", "avatar_backgroundCyan", "avatar_backgroundBlue", "avatar_backgroundPink" };
        Theme.keys_avatar_nameInMessage = new String[] { "avatar_nameInMessageRed", "avatar_nameInMessageOrange", "avatar_nameInMessageViolet", "avatar_nameInMessageGreen", "avatar_nameInMessageCyan", "avatar_nameInMessageBlue", "avatar_nameInMessagePink" };
        Theme.defaultColors = new HashMap<String, Integer>();
        Theme.fallbackKeys = new HashMap<String, String>();
        Theme.defaultColors.put("dialogBackground", value);
        Theme.defaultColors.put("dialogBackgroundGray", -986896);
        final HashMap<String, Integer> defaultColors = Theme.defaultColors;
        final Integer value2 = -14540254;
        defaultColors.put("dialogTextBlack", value2);
        Theme.defaultColors.put("dialogTextLink", -14255946);
        Theme.defaultColors.put("dialogLinkSelection", 862104035);
        Theme.defaultColors.put("dialogTextRed", -3319206);
        Theme.defaultColors.put("dialogTextRed2", -2213318);
        Theme.defaultColors.put("dialogTextBlue", -13660983);
        Theme.defaultColors.put("dialogTextBlue2", -12937771);
        Theme.defaultColors.put("dialogTextBlue3", -12664327);
        Theme.defaultColors.put("dialogTextBlue4", -15095832);
        Theme.defaultColors.put("dialogTextGray", -13333567);
        Theme.defaultColors.put("dialogTextGray2", -9079435);
        Theme.defaultColors.put("dialogTextGray3", -6710887);
        Theme.defaultColors.put("dialogTextGray4", -5000269);
        Theme.defaultColors.put("dialogTextHint", -6842473);
        Theme.defaultColors.put("dialogIcon", -9999504);
        Theme.defaultColors.put("dialogRedIcon", -2011827);
        Theme.defaultColors.put("dialogGrayLine", -2960686);
        Theme.defaultColors.put("dialogTopBackground", -9456923);
        Theme.defaultColors.put("dialogInputField", -2368549);
        Theme.defaultColors.put("dialogInputFieldActivated", -13129232);
        Theme.defaultColors.put("dialogCheckboxSquareBackground", -12345121);
        Theme.defaultColors.put("dialogCheckboxSquareCheck", value);
        Theme.defaultColors.put("dialogCheckboxSquareUnchecked", -9211021);
        Theme.defaultColors.put("dialogCheckboxSquareDisabled", -5197648);
        Theme.defaultColors.put("dialogRadioBackground", -5000269);
        Theme.defaultColors.put("dialogRadioBackgroundChecked", -13129232);
        Theme.defaultColors.put("dialogProgressCircle", -11371101);
        Theme.defaultColors.put("dialogLineProgress", -11371101);
        Theme.defaultColors.put("dialogLineProgressBackground", -2368549);
        Theme.defaultColors.put("dialogButton", -11955764);
        Theme.defaultColors.put("dialogButtonSelector", 251658240);
        Theme.defaultColors.put("dialogScrollGlow", -657673);
        Theme.defaultColors.put("dialogRoundCheckBox", -11750155);
        Theme.defaultColors.put("dialogRoundCheckBoxCheck", value);
        Theme.defaultColors.put("dialogBadgeBackground", -12664327);
        Theme.defaultColors.put("dialogBadgeText", value);
        Theme.defaultColors.put("dialogCameraIcon", value);
        Theme.defaultColors.put("dialog_inlineProgressBackground", -151981323);
        Theme.defaultColors.put("dialog_inlineProgress", -9735304);
        Theme.defaultColors.put("dialogSearchBackground", -854795);
        Theme.defaultColors.put("dialogSearchHint", -6774617);
        Theme.defaultColors.put("dialogSearchIcon", -6182737);
        Theme.defaultColors.put("dialogSearchText", value2);
        Theme.defaultColors.put("dialogFloatingButton", -11750155);
        Theme.defaultColors.put("dialogFloatingButtonPressed", -11750155);
        Theme.defaultColors.put("dialogFloatingIcon", value);
        Theme.defaultColors.put("dialogShadowLine", 301989888);
        Theme.defaultColors.put("windowBackgroundWhite", value);
        Theme.defaultColors.put("windowBackgroundUnchecked", -6445135);
        Theme.defaultColors.put("windowBackgroundChecked", -11034919);
        Theme.defaultColors.put("windowBackgroundCheckText", value);
        Theme.defaultColors.put("progressCircle", -11371101);
        Theme.defaultColors.put("windowBackgroundWhiteGrayIcon", -8288629);
        Theme.defaultColors.put("windowBackgroundWhiteBlueText", -12545331);
        Theme.defaultColors.put("windowBackgroundWhiteBlueText2", -12937771);
        Theme.defaultColors.put("windowBackgroundWhiteBlueText3", -14255946);
        Theme.defaultColors.put("windowBackgroundWhiteBlueText4", -11697229);
        Theme.defaultColors.put("windowBackgroundWhiteBlueText5", -11759926);
        final HashMap<String, Integer> defaultColors2 = Theme.defaultColors;
        final Integer value3 = -12940081;
        defaultColors2.put("windowBackgroundWhiteBlueText6", value3);
        Theme.defaultColors.put("windowBackgroundWhiteBlueText7", -13141330);
        Theme.defaultColors.put("windowBackgroundWhiteBlueButton", -14776109);
        Theme.defaultColors.put("windowBackgroundWhiteBlueIcon", -13132315);
        Theme.defaultColors.put("windowBackgroundWhiteGreenText", -14248148);
        Theme.defaultColors.put("windowBackgroundWhiteGreenText2", -13129704);
        Theme.defaultColors.put("windowBackgroundWhiteRedText", -3319206);
        Theme.defaultColors.put("windowBackgroundWhiteRedText2", -2404015);
        Theme.defaultColors.put("windowBackgroundWhiteRedText3", -2995895);
        Theme.defaultColors.put("windowBackgroundWhiteRedText4", -3198928);
        Theme.defaultColors.put("windowBackgroundWhiteRedText5", -1230535);
        Theme.defaultColors.put("windowBackgroundWhiteRedText6", -39322);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText", -8156010);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText2", -7697782);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText3", -6710887);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText4", -8355712);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText5", -6052957);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText6", -9079435);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText7", -3750202);
        Theme.defaultColors.put("windowBackgroundWhiteGrayText8", -9605774);
        Theme.defaultColors.put("windowBackgroundWhiteGrayLine", -2368549);
        Theme.defaultColors.put("windowBackgroundWhiteBlackText", value2);
        Theme.defaultColors.put("windowBackgroundWhiteHintText", -5723992);
        Theme.defaultColors.put("windowBackgroundWhiteValueText", -12937771);
        Theme.defaultColors.put("windowBackgroundWhiteLinkText", -14255946);
        Theme.defaultColors.put("windowBackgroundWhiteLinkSelection", 862104035);
        Theme.defaultColors.put("windowBackgroundWhiteBlueHeader", -12937771);
        Theme.defaultColors.put("windowBackgroundWhiteInputField", -2368549);
        Theme.defaultColors.put("windowBackgroundWhiteInputFieldActivated", -13129232);
        Theme.defaultColors.put("switchTrack", -5196358);
        Theme.defaultColors.put("switchTrackChecked", -11358743);
        Theme.defaultColors.put("switchTrackBlue", -8221031);
        Theme.defaultColors.put("switchTrackBlueChecked", -12810041);
        Theme.defaultColors.put("switchTrackBlueThumb", value);
        Theme.defaultColors.put("switchTrackBlueThumbChecked", value);
        Theme.defaultColors.put("switchTrackBlueSelector", 390089299);
        Theme.defaultColors.put("switchTrackBlueSelectorChecked", 553797505);
        Theme.defaultColors.put("switch2Track", -688514);
        Theme.defaultColors.put("switch2TrackChecked", -11358743);
        Theme.defaultColors.put("checkboxSquareBackground", -12345121);
        Theme.defaultColors.put("checkboxSquareCheck", value);
        Theme.defaultColors.put("checkboxSquareUnchecked", -9211021);
        Theme.defaultColors.put("checkboxSquareDisabled", -5197648);
        Theme.defaultColors.put("listSelectorSDK21", 251658240);
        Theme.defaultColors.put("radioBackground", -5000269);
        Theme.defaultColors.put("radioBackgroundChecked", -13129232);
        Theme.defaultColors.put("windowBackgroundGray", -986896);
        final HashMap<String, Integer> defaultColors3 = Theme.defaultColors;
        final Integer value4 = -16777216;
        defaultColors3.put("windowBackgroundGrayShadow", value4);
        Theme.defaultColors.put("emptyListPlaceholder", -6974059);
        Theme.defaultColors.put("divider", -2500135);
        Theme.defaultColors.put("graySection", -1117195);
        Theme.defaultColors.put("key_graySectionText", -8418927);
        Theme.defaultColors.put("contextProgressInner1", -4202506);
        Theme.defaultColors.put("contextProgressOuter1", -13920542);
        Theme.defaultColors.put("contextProgressInner2", -4202506);
        Theme.defaultColors.put("contextProgressOuter2", value);
        Theme.defaultColors.put("contextProgressInner3", -5000269);
        Theme.defaultColors.put("contextProgressOuter3", value);
        Theme.defaultColors.put("contextProgressInner4", -3486256);
        final HashMap<String, Integer> defaultColors4 = Theme.defaultColors;
        final Integer value5 = -13683656;
        defaultColors4.put("contextProgressOuter4", value5);
        Theme.defaultColors.put("fastScrollActive", -11361317);
        Theme.defaultColors.put("fastScrollInactive", -3551791);
        Theme.defaultColors.put("fastScrollText", value);
        Theme.defaultColors.put("avatar_text", value);
        Theme.defaultColors.put("avatar_backgroundSaved", -10043398);
        Theme.defaultColors.put("avatar_backgroundArchived", -5654847);
        Theme.defaultColors.put("avatar_backgroundArchivedHidden", -3749428);
        Theme.defaultColors.put("avatar_backgroundRed", -1743531);
        Theme.defaultColors.put("avatar_backgroundOrange", -881592);
        Theme.defaultColors.put("avatar_backgroundViolet", -7436818);
        Theme.defaultColors.put("avatar_backgroundGreen", -8992691);
        Theme.defaultColors.put("avatar_backgroundCyan", -10502443);
        Theme.defaultColors.put("avatar_backgroundBlue", -11232035);
        Theme.defaultColors.put("avatar_backgroundPink", -887654);
        Theme.defaultColors.put("avatar_backgroundGroupCreateSpanBlue", -1642505);
        Theme.defaultColors.put("avatar_backgroundInProfileBlue", -11500111);
        Theme.defaultColors.put("avatar_backgroundActionBarBlue", -10907718);
        Theme.defaultColors.put("avatar_subtitleInProfileBlue", -2626822);
        Theme.defaultColors.put("avatar_actionBarSelectorBlue", -11959891);
        Theme.defaultColors.put("avatar_actionBarIconBlue", value);
        Theme.defaultColors.put("avatar_nameInMessageRed", -3516848);
        Theme.defaultColors.put("avatar_nameInMessageOrange", -2589911);
        Theme.defaultColors.put("avatar_nameInMessageViolet", -11627828);
        Theme.defaultColors.put("avatar_nameInMessageGreen", -11488718);
        Theme.defaultColors.put("avatar_nameInMessageCyan", -13132104);
        Theme.defaultColors.put("avatar_nameInMessageBlue", -11627828);
        Theme.defaultColors.put("avatar_nameInMessagePink", -11627828);
        Theme.defaultColors.put("actionBarDefault", -11371101);
        Theme.defaultColors.put("actionBarDefaultIcon", value);
        Theme.defaultColors.put("actionBarActionModeDefault", value);
        Theme.defaultColors.put("actionBarActionModeDefaultTop", 268435456);
        Theme.defaultColors.put("actionBarActionModeDefaultIcon", -9999761);
        Theme.defaultColors.put("actionBarDefaultTitle", value);
        Theme.defaultColors.put("actionBarDefaultSubtitle", -2758409);
        Theme.defaultColors.put("actionBarDefaultSelector", -12554860);
        Theme.defaultColors.put("actionBarWhiteSelector", 788529152);
        Theme.defaultColors.put("actionBarDefaultSearch", value);
        Theme.defaultColors.put("actionBarDefaultSearchPlaceholder", -1996488705);
        Theme.defaultColors.put("actionBarDefaultSubmenuItem", value2);
        Theme.defaultColors.put("actionBarDefaultSubmenuItemIcon", -9999504);
        Theme.defaultColors.put("actionBarDefaultSubmenuBackground", value);
        Theme.defaultColors.put("actionBarActionModeDefaultSelector", -986896);
        Theme.defaultColors.put("actionBarTabActiveText", value);
        Theme.defaultColors.put("actionBarTabUnactiveText", -2758409);
        Theme.defaultColors.put("actionBarTabLine", value);
        Theme.defaultColors.put("actionBarTabSelector", -12554860);
        Theme.defaultColors.put("actionBarDefaultArchived", -9471353);
        Theme.defaultColors.put("actionBarDefaultArchivedSelector", -10590350);
        Theme.defaultColors.put("actionBarDefaultArchivedIcon", value);
        Theme.defaultColors.put("actionBarDefaultArchivedTitle", value);
        Theme.defaultColors.put("actionBarDefaultArchivedSearch", value);
        Theme.defaultColors.put("actionBarDefaultSearchArchivedPlaceholder", -1996488705);
        Theme.defaultColors.put("chats_onlineCircle", -11810020);
        Theme.defaultColors.put("chats_unreadCounter", -11613090);
        Theme.defaultColors.put("chats_unreadCounterMuted", -3749428);
        Theme.defaultColors.put("chats_unreadCounterText", value);
        Theme.defaultColors.put("chats_archiveBackground", -10049056);
        Theme.defaultColors.put("chats_archivePinBackground", -6313293);
        Theme.defaultColors.put("chats_archiveIcon", value);
        Theme.defaultColors.put("chats_archiveText", value);
        Theme.defaultColors.put("chats_name", value2);
        Theme.defaultColors.put("chats_nameArchived", -11382190);
        Theme.defaultColors.put("chats_secretName", -16734706);
        Theme.defaultColors.put("chats_secretIcon", -15093466);
        Theme.defaultColors.put("chats_nameIcon", -14408668);
        Theme.defaultColors.put("chats_pinnedIcon", -5723992);
        Theme.defaultColors.put("chats_message", -7631473);
        Theme.defaultColors.put("chats_messageArchived", -7237231);
        Theme.defaultColors.put("chats_message_threeLines", -7434095);
        Theme.defaultColors.put("chats_draft", -2274503);
        Theme.defaultColors.put("chats_nameMessage", -12812624);
        Theme.defaultColors.put("chats_nameMessageArchived", -7631473);
        Theme.defaultColors.put("chats_nameMessage_threeLines", -12434359);
        Theme.defaultColors.put("chats_nameMessageArchived_threeLines", -10592674);
        Theme.defaultColors.put("chats_attachMessage", -12812624);
        Theme.defaultColors.put("chats_actionMessage", -12812624);
        Theme.defaultColors.put("chats_date", -6973028);
        Theme.defaultColors.put("chats_pinnedOverlay", 134217728);
        Theme.defaultColors.put("chats_tabletSelectedOverlay", 251658240);
        Theme.defaultColors.put("chats_sentCheck", -12146122);
        Theme.defaultColors.put("chats_sentClock", -9061026);
        Theme.defaultColors.put("chats_sentError", -2796974);
        Theme.defaultColors.put("chats_sentErrorIcon", value);
        Theme.defaultColors.put("chats_verifiedBackground", -13391642);
        Theme.defaultColors.put("chats_verifiedCheck", value);
        Theme.defaultColors.put("chats_muteIcon", -4341308);
        Theme.defaultColors.put("chats_mentionIcon", value);
        Theme.defaultColors.put("chats_menuBackground", value);
        Theme.defaultColors.put("chats_menuItemText", -12303292);
        Theme.defaultColors.put("chats_menuItemCheck", -10907718);
        Theme.defaultColors.put("chats_menuItemIcon", -7827048);
        Theme.defaultColors.put("chats_menuName", value);
        Theme.defaultColors.put("chats_menuPhone", value);
        Theme.defaultColors.put("chats_menuPhoneCats", -4004353);
        Theme.defaultColors.put("chats_menuCloud", value);
        Theme.defaultColors.put("chats_menuCloudBackgroundCats", -12420183);
        Theme.defaultColors.put("chats_actionIcon", value);
        Theme.defaultColors.put("chats_actionBackground", -10114592);
        Theme.defaultColors.put("chats_actionPressedBackground", -11100714);
        Theme.defaultColors.put("chats_actionUnreadIcon", -9211021);
        Theme.defaultColors.put("chats_actionUnreadBackground", value);
        Theme.defaultColors.put("chats_actionUnreadPressedBackground", -855310);
        Theme.defaultColors.put("chats_menuTopBackgroundCats", -10907718);
        Theme.defaultColors.put("chat_attachCameraIcon1", -33488);
        Theme.defaultColors.put("chat_attachCameraIcon2", -1353648);
        Theme.defaultColors.put("chat_attachCameraIcon3", -12342798);
        Theme.defaultColors.put("chat_attachCameraIcon4", -4958752);
        Theme.defaultColors.put("chat_attachCameraIcon5", -10366879);
        Theme.defaultColors.put("chat_attachCameraIcon6", -81627);
        Theme.defaultColors.put("chat_attachMediaBanBackground", -12171706);
        Theme.defaultColors.put("chat_attachMediaBanText", value);
        Theme.defaultColors.put("chat_attachGalleryBackground", -5997863);
        Theme.defaultColors.put("chat_attachGalleryIcon", value);
        Theme.defaultColors.put("chat_attachVideoBackground", -1871495);
        Theme.defaultColors.put("chat_attachVideoIcon", value);
        Theme.defaultColors.put("chat_attachAudioBackground", -620719);
        Theme.defaultColors.put("chat_attachAudioIcon", value);
        Theme.defaultColors.put("chat_attachFileBackground", -13328140);
        Theme.defaultColors.put("chat_attachFileIcon", value);
        Theme.defaultColors.put("chat_attachContactBackground", -12664838);
        Theme.defaultColors.put("chat_attachContactIcon", value);
        Theme.defaultColors.put("chat_attachLocationBackground", -12597126);
        Theme.defaultColors.put("chat_attachLocationIcon", value);
        Theme.defaultColors.put("chat_attachHideBackground", -5330248);
        Theme.defaultColors.put("chat_attachHideIcon", value);
        Theme.defaultColors.put("chat_attachSendBackground", -12664838);
        Theme.defaultColors.put("chat_attachPollBackground", -670899);
        Theme.defaultColors.put("chat_attachPollIcon", value);
        Theme.defaultColors.put("chat_status", -2758409);
        Theme.defaultColors.put("chat_inDownCall", -16725933);
        Theme.defaultColors.put("chat_inUpCall", -47032);
        Theme.defaultColors.put("chat_outUpCall", -16725933);
        Theme.defaultColors.put("chat_attachSendIcon", value);
        Theme.defaultColors.put("chat_shareBackground", 1718783910);
        Theme.defaultColors.put("chat_shareBackgroundSelected", -1720545370);
        Theme.defaultColors.put("chat_lockIcon", value);
        Theme.defaultColors.put("chat_muteIcon", -5124893);
        Theme.defaultColors.put("chat_inBubble", value);
        Theme.defaultColors.put("chat_inBubbleSelected", -1247235);
        Theme.defaultColors.put("chat_inBubbleShadow", -14862509);
        Theme.defaultColors.put("chat_outBubble", -1048610);
        Theme.defaultColors.put("chat_outBubbleSelected", -2492475);
        Theme.defaultColors.put("chat_outBubbleShadow", -14781172);
        Theme.defaultColors.put("chat_inMediaIcon", value);
        Theme.defaultColors.put("chat_inMediaIconSelected", -1050370);
        Theme.defaultColors.put("chat_outMediaIcon", -1048610);
        Theme.defaultColors.put("chat_outMediaIconSelected", -1967921);
        Theme.defaultColors.put("chat_messageTextIn", value4);
        Theme.defaultColors.put("chat_messageTextOut", value4);
        Theme.defaultColors.put("chat_messageLinkIn", -14255946);
        Theme.defaultColors.put("chat_messageLinkOut", -14255946);
        Theme.defaultColors.put("chat_serviceText", value);
        Theme.defaultColors.put("chat_serviceLink", value);
        Theme.defaultColors.put("chat_serviceIcon", value);
        Theme.defaultColors.put("chat_mediaTimeBackground", 1711276032);
        Theme.defaultColors.put("chat_outSentCheck", -10637232);
        Theme.defaultColors.put("chat_outSentCheckSelected", -10637232);
        Theme.defaultColors.put("chat_outSentClock", -9061026);
        Theme.defaultColors.put("chat_outSentClockSelected", -9061026);
        final HashMap<String, Integer> defaultColors5 = Theme.defaultColors;
        final Integer value6 = -6182221;
        defaultColors5.put("chat_inSentClock", value6);
        Theme.defaultColors.put("chat_inSentClockSelected", -7094838);
        Theme.defaultColors.put("chat_mediaSentCheck", value);
        Theme.defaultColors.put("chat_mediaSentClock", value);
        Theme.defaultColors.put("chat_inViews", value6);
        Theme.defaultColors.put("chat_inViewsSelected", -7094838);
        Theme.defaultColors.put("chat_outViews", -9522601);
        Theme.defaultColors.put("chat_outViewsSelected", -9522601);
        Theme.defaultColors.put("chat_mediaViews", value);
        Theme.defaultColors.put("chat_inMenu", -4801083);
        Theme.defaultColors.put("chat_inMenuSelected", -6766130);
        Theme.defaultColors.put("chat_outMenu", -7221634);
        Theme.defaultColors.put("chat_outMenuSelected", -7221634);
        Theme.defaultColors.put("chat_mediaMenu", value);
        final HashMap<String, Integer> defaultColors6 = Theme.defaultColors;
        final Integer value7 = -11162801;
        defaultColors6.put("chat_outInstant", value7);
        Theme.defaultColors.put("chat_outInstantSelected", -12019389);
        Theme.defaultColors.put("chat_inInstant", value3);
        Theme.defaultColors.put("chat_inInstantSelected", -13600331);
        Theme.defaultColors.put("chat_sentError", -2411211);
        Theme.defaultColors.put("chat_sentErrorIcon", value);
        Theme.defaultColors.put("chat_selectedBackground", 671781104);
        Theme.defaultColors.put("chat_previewDurationText", value);
        Theme.defaultColors.put("chat_previewGameText", value);
        Theme.defaultColors.put("chat_inPreviewInstantText", value3);
        Theme.defaultColors.put("chat_outPreviewInstantText", value7);
        Theme.defaultColors.put("chat_inPreviewInstantSelectedText", -13600331);
        Theme.defaultColors.put("chat_outPreviewInstantSelectedText", -12019389);
        Theme.defaultColors.put("chat_secretTimeText", -1776928);
        Theme.defaultColors.put("chat_stickerNameText", value);
        Theme.defaultColors.put("chat_botButtonText", value);
        Theme.defaultColors.put("chat_botProgress", value);
        Theme.defaultColors.put("chat_inForwardedNameText", -13072697);
        Theme.defaultColors.put("chat_outForwardedNameText", value7);
        Theme.defaultColors.put("chat_inViaBotNameText", value3);
        Theme.defaultColors.put("chat_outViaBotNameText", value7);
        Theme.defaultColors.put("chat_stickerViaBotNameText", value);
        Theme.defaultColors.put("chat_inReplyLine", -10903592);
        Theme.defaultColors.put("chat_outReplyLine", -9520791);
        Theme.defaultColors.put("chat_stickerReplyLine", value);
        Theme.defaultColors.put("chat_inReplyNameText", value3);
        Theme.defaultColors.put("chat_outReplyNameText", value7);
        Theme.defaultColors.put("chat_stickerReplyNameText", value);
        Theme.defaultColors.put("chat_inReplyMessageText", value4);
        Theme.defaultColors.put("chat_outReplyMessageText", value4);
        Theme.defaultColors.put("chat_inReplyMediaMessageText", value6);
        Theme.defaultColors.put("chat_outReplyMediaMessageText", -10112933);
        Theme.defaultColors.put("chat_inReplyMediaMessageSelectedText", -7752511);
        Theme.defaultColors.put("chat_outReplyMediaMessageSelectedText", -10112933);
        Theme.defaultColors.put("chat_stickerReplyMessageText", value);
        Theme.defaultColors.put("chat_inPreviewLine", -9390872);
        Theme.defaultColors.put("chat_outPreviewLine", -7812741);
        Theme.defaultColors.put("chat_inSiteNameText", value3);
        Theme.defaultColors.put("chat_outSiteNameText", value7);
        Theme.defaultColors.put("chat_inContactNameText", -11625772);
        Theme.defaultColors.put("chat_outContactNameText", value7);
        Theme.defaultColors.put("chat_inContactPhoneText", value5);
        Theme.defaultColors.put("chat_inContactPhoneSelectedText", value5);
        Theme.defaultColors.put("chat_outContactPhoneText", -13286860);
        Theme.defaultColors.put("chat_outContactPhoneSelectedText", -13286860);
        Theme.defaultColors.put("chat_mediaProgress", value);
        Theme.defaultColors.put("chat_inAudioProgress", value);
        Theme.defaultColors.put("chat_outAudioProgress", -1048610);
        Theme.defaultColors.put("chat_inAudioSelectedProgress", -1050370);
        Theme.defaultColors.put("chat_outAudioSelectedProgress", -1967921);
        Theme.defaultColors.put("chat_mediaTimeText", value);
        Theme.defaultColors.put("chat_inTimeText", value6);
        Theme.defaultColors.put("chat_outTimeText", -9391780);
        Theme.defaultColors.put("chat_adminText", -4143413);
        Theme.defaultColors.put("chat_adminSelectedText", -7752511);
        Theme.defaultColors.put("chat_inTimeSelectedText", -7752511);
        Theme.defaultColors.put("chat_outTimeSelectedText", -9391780);
        Theme.defaultColors.put("chat_inAudioPerfomerText", value5);
        Theme.defaultColors.put("chat_inAudioPerfomerSelectedText", value5);
        Theme.defaultColors.put("chat_outAudioPerfomerText", -13286860);
        Theme.defaultColors.put("chat_outAudioPerfomerSelectedText", -13286860);
        Theme.defaultColors.put("chat_inAudioTitleText", -11625772);
        Theme.defaultColors.put("chat_outAudioTitleText", value7);
        Theme.defaultColors.put("chat_inAudioDurationText", value6);
        Theme.defaultColors.put("chat_outAudioDurationText", -10112933);
        Theme.defaultColors.put("chat_inAudioDurationSelectedText", -7752511);
        Theme.defaultColors.put("chat_outAudioDurationSelectedText", -10112933);
        Theme.defaultColors.put("chat_inAudioSeekbar", -1774864);
        Theme.defaultColors.put("chat_inAudioCacheSeekbar", 1071966960);
        Theme.defaultColors.put("chat_outAudioSeekbar", -4463700);
        Theme.defaultColors.put("chat_outAudioCacheSeekbar", 1069278124);
        Theme.defaultColors.put("chat_inAudioSeekbarSelected", -4399384);
        Theme.defaultColors.put("chat_outAudioSeekbarSelected", -5644906);
        Theme.defaultColors.put("chat_inAudioSeekbarFill", -9259544);
        Theme.defaultColors.put("chat_outAudioSeekbarFill", -8863118);
        Theme.defaultColors.put("chat_inVoiceSeekbar", -2169365);
        Theme.defaultColors.put("chat_outVoiceSeekbar", -4463700);
        Theme.defaultColors.put("chat_inVoiceSeekbarSelected", -4399384);
        Theme.defaultColors.put("chat_outVoiceSeekbarSelected", -5644906);
        Theme.defaultColors.put("chat_inVoiceSeekbarFill", -9259544);
        Theme.defaultColors.put("chat_outVoiceSeekbarFill", -8863118);
        Theme.defaultColors.put("chat_inFileProgress", -1314571);
        Theme.defaultColors.put("chat_outFileProgress", -2427453);
        Theme.defaultColors.put("chat_inFileProgressSelected", -3413258);
        Theme.defaultColors.put("chat_outFileProgressSelected", -3806041);
        Theme.defaultColors.put("chat_inFileNameText", -11625772);
        Theme.defaultColors.put("chat_outFileNameText", value7);
        Theme.defaultColors.put("chat_inFileInfoText", value6);
        Theme.defaultColors.put("chat_outFileInfoText", -10112933);
        Theme.defaultColors.put("chat_inFileInfoSelectedText", -7752511);
        Theme.defaultColors.put("chat_outFileInfoSelectedText", -10112933);
        Theme.defaultColors.put("chat_inFileBackground", -1314571);
        Theme.defaultColors.put("chat_outFileBackground", -2427453);
        Theme.defaultColors.put("chat_inFileBackgroundSelected", -3413258);
        Theme.defaultColors.put("chat_outFileBackgroundSelected", -3806041);
        Theme.defaultColors.put("chat_inVenueInfoText", value6);
        Theme.defaultColors.put("chat_outVenueInfoText", -10112933);
        Theme.defaultColors.put("chat_inVenueInfoSelectedText", -7752511);
        Theme.defaultColors.put("chat_outVenueInfoSelectedText", -10112933);
        Theme.defaultColors.put("chat_mediaInfoText", value);
        Theme.defaultColors.put("chat_linkSelectBackground", 862104035);
        Theme.defaultColors.put("chat_textSelectBackground", 1717742051);
        Theme.defaultColors.put("chat_emojiPanelBackground", -986379);
        Theme.defaultColors.put("chat_emojiPanelBadgeBackground", -11688214);
        Theme.defaultColors.put("chat_emojiPanelBadgeText", value);
        Theme.defaultColors.put("chat_emojiSearchBackground", -1709586);
        Theme.defaultColors.put("chat_emojiSearchIcon", -7036497);
        Theme.defaultColors.put("chat_emojiPanelShadowLine", 301989888);
        Theme.defaultColors.put("chat_emojiPanelEmptyText", -7038047);
        Theme.defaultColors.put("chat_emojiPanelIcon", -6445909);
        Theme.defaultColors.put("chat_emojiBottomPanelIcon", -7564905);
        Theme.defaultColors.put("chat_emojiPanelIconSelected", -13920286);
        Theme.defaultColors.put("chat_emojiPanelStickerPackSelector", -1907225);
        Theme.defaultColors.put("chat_emojiPanelStickerPackSelectorLine", -11097104);
        Theme.defaultColors.put("chat_emojiPanelBackspace", -7564905);
        Theme.defaultColors.put("chat_emojiPanelMasksIcon", value);
        Theme.defaultColors.put("chat_emojiPanelMasksIconSelected", -10305560);
        Theme.defaultColors.put("chat_emojiPanelTrendingTitle", value2);
        Theme.defaultColors.put("chat_emojiPanelStickerSetName", -8221804);
        Theme.defaultColors.put("chat_emojiPanelStickerSetNameHighlight", -14184997);
        Theme.defaultColors.put("chat_emojiPanelStickerSetNameIcon", -5130564);
        Theme.defaultColors.put("chat_emojiPanelTrendingDescription", -7697782);
        Theme.defaultColors.put("chat_botKeyboardButtonText", -13220017);
        Theme.defaultColors.put("chat_botKeyboardButtonBackground", -1775639);
        Theme.defaultColors.put("chat_botKeyboardButtonBackgroundPressed", -3354156);
        Theme.defaultColors.put("chat_unreadMessagesStartArrowIcon", -6113849);
        Theme.defaultColors.put("chat_unreadMessagesStartText", -11102772);
        Theme.defaultColors.put("chat_unreadMessagesStartBackground", value);
        Theme.defaultColors.put("chat_inFileIcon", -6113849);
        Theme.defaultColors.put("chat_inFileSelectedIcon", -7883067);
        Theme.defaultColors.put("chat_outFileIcon", -8011912);
        Theme.defaultColors.put("chat_outFileSelectedIcon", -8011912);
        Theme.defaultColors.put("chat_inLocationBackground", -1314571);
        Theme.defaultColors.put("chat_inLocationIcon", -6113849);
        Theme.defaultColors.put("chat_outLocationBackground", -2427453);
        Theme.defaultColors.put("chat_outLocationIcon", -7880840);
        Theme.defaultColors.put("chat_inContactBackground", -9259544);
        Theme.defaultColors.put("chat_inContactIcon", value);
        Theme.defaultColors.put("chat_outContactBackground", -8863118);
        Theme.defaultColors.put("chat_outContactIcon", -1048610);
        Theme.defaultColors.put("chat_outBroadcast", -12146122);
        Theme.defaultColors.put("chat_mediaBroadcast", value);
        Theme.defaultColors.put("chat_searchPanelIcons", -9999761);
        Theme.defaultColors.put("chat_searchPanelText", -9999761);
        Theme.defaultColors.put("chat_secretChatStatusText", -8421505);
        Theme.defaultColors.put("chat_fieldOverlayText", value3);
        Theme.defaultColors.put("chat_stickersHintPanel", value);
        Theme.defaultColors.put("chat_replyPanelIcons", -11032346);
        Theme.defaultColors.put("chat_replyPanelClose", -7432805);
        Theme.defaultColors.put("chat_replyPanelName", value3);
        Theme.defaultColors.put("chat_replyPanelMessage", value2);
        Theme.defaultColors.put("chat_replyPanelLine", -1513240);
        Theme.defaultColors.put("chat_messagePanelBackground", value);
        Theme.defaultColors.put("chat_messagePanelText", value4);
        Theme.defaultColors.put("chat_messagePanelHint", -5985101);
        Theme.defaultColors.put("chat_messagePanelShadow", value4);
        Theme.defaultColors.put("chat_messagePanelIcons", -7432805);
        Theme.defaultColors.put("chat_recordedVoicePlayPause", value);
        Theme.defaultColors.put("chat_recordedVoicePlayPausePressed", -2495749);
        Theme.defaultColors.put("chat_recordedVoiceDot", -2468275);
        Theme.defaultColors.put("chat_recordedVoiceBackground", -11165981);
        Theme.defaultColors.put("chat_recordedVoiceProgress", -6107400);
        Theme.defaultColors.put("chat_recordedVoiceProgressInner", value);
        Theme.defaultColors.put("chat_recordVoiceCancel", -6710887);
        Theme.defaultColors.put("chat_messagePanelSend", -10309397);
        Theme.defaultColors.put("key_chat_messagePanelVoiceLock", -5987164);
        Theme.defaultColors.put("key_chat_messagePanelVoiceLockBackground", value);
        Theme.defaultColors.put("key_chat_messagePanelVoiceLockShadow", value4);
        Theme.defaultColors.put("chat_recordTime", -11711413);
        Theme.defaultColors.put("chat_emojiPanelNewTrending", -11688214);
        Theme.defaultColors.put("chat_gifSaveHintText", value);
        Theme.defaultColors.put("chat_gifSaveHintBackground", -871296751);
        Theme.defaultColors.put("chat_goDownButton", value);
        Theme.defaultColors.put("chat_goDownButtonShadow", value4);
        Theme.defaultColors.put("chat_goDownButtonIcon", -7432805);
        Theme.defaultColors.put("chat_goDownButtonCounter", value);
        Theme.defaultColors.put("chat_goDownButtonCounterBackground", -11689240);
        Theme.defaultColors.put("chat_messagePanelCancelInlineBot", -5395027);
        Theme.defaultColors.put("chat_messagePanelVoicePressed", value);
        Theme.defaultColors.put("chat_messagePanelVoiceBackground", -11037236);
        Theme.defaultColors.put("chat_messagePanelVoiceShadow", 218103808);
        Theme.defaultColors.put("chat_messagePanelVoiceDelete", -9211021);
        Theme.defaultColors.put("chat_messagePanelVoiceDuration", value);
        Theme.defaultColors.put("chat_inlineResultIcon", -11037236);
        Theme.defaultColors.put("chat_topPanelBackground", value);
        Theme.defaultColors.put("chat_topPanelClose", -5723992);
        Theme.defaultColors.put("chat_topPanelLine", -9658414);
        Theme.defaultColors.put("chat_topPanelTitle", value3);
        Theme.defaultColors.put("chat_topPanelMessage", -6710887);
        Theme.defaultColors.put("chat_reportSpam", -3188393);
        Theme.defaultColors.put("chat_addContact", -11894091);
        Theme.defaultColors.put("chat_inLoader", -9259544);
        Theme.defaultColors.put("chat_inLoaderSelected", -10114080);
        Theme.defaultColors.put("chat_outLoader", -8863118);
        Theme.defaultColors.put("chat_outLoaderSelected", -9783964);
        Theme.defaultColors.put("chat_inLoaderPhoto", -6113080);
        Theme.defaultColors.put("chat_inLoaderPhotoSelected", -6113849);
        Theme.defaultColors.put("chat_inLoaderPhotoIcon", -197380);
        Theme.defaultColors.put("chat_inLoaderPhotoIconSelected", -1314571);
        Theme.defaultColors.put("chat_outLoaderPhoto", -8011912);
        Theme.defaultColors.put("chat_outLoaderPhotoSelected", -8538000);
        Theme.defaultColors.put("chat_outLoaderPhotoIcon", -2427453);
        Theme.defaultColors.put("chat_outLoaderPhotoIconSelected", -4134748);
        Theme.defaultColors.put("chat_mediaLoaderPhoto", 1711276032);
        Theme.defaultColors.put("chat_mediaLoaderPhotoSelected", 2130706432);
        Theme.defaultColors.put("chat_mediaLoaderPhotoIcon", value);
        Theme.defaultColors.put("chat_mediaLoaderPhotoIconSelected", -2500135);
        Theme.defaultColors.put("chat_secretTimerBackground", -868326258);
        Theme.defaultColors.put("chat_secretTimerText", value);
        Theme.defaultColors.put("profile_creatorIcon", -12937771);
        Theme.defaultColors.put("profile_actionIcon", -8288630);
        Theme.defaultColors.put("profile_actionBackground", value);
        Theme.defaultColors.put("profile_actionPressedBackground", -855310);
        Theme.defaultColors.put("profile_verifiedBackground", -5056776);
        Theme.defaultColors.put("profile_verifiedCheck", -11959368);
        Theme.defaultColors.put("profile_title", value);
        Theme.defaultColors.put("profile_status", -2626822);
        Theme.defaultColors.put("player_actionBar", value);
        Theme.defaultColors.put("player_actionBarSelector", 251658240);
        Theme.defaultColors.put("player_actionBarTitle", value5);
        Theme.defaultColors.put("player_actionBarTop", -1728053248);
        Theme.defaultColors.put("player_actionBarSubtitle", -7697782);
        Theme.defaultColors.put("player_actionBarItems", -7697782);
        Theme.defaultColors.put("player_background", value);
        Theme.defaultColors.put("player_time", -7564650);
        Theme.defaultColors.put("player_progressBackground", -1445899);
        Theme.defaultColors.put("key_player_progressCachedBackground", -1445899);
        Theme.defaultColors.put("player_progress", -11821085);
        Theme.defaultColors.put("player_placeholder", -5723992);
        Theme.defaultColors.put("player_placeholderBackground", -986896);
        Theme.defaultColors.put("player_button", -13421773);
        Theme.defaultColors.put("player_buttonActive", -11753238);
        Theme.defaultColors.put("key_sheet_scrollUp", -1973016);
        Theme.defaultColors.put("key_sheet_other", -3551789);
        Theme.defaultColors.put("files_folderIcon", -6710887);
        Theme.defaultColors.put("files_folderIconBackground", -986896);
        Theme.defaultColors.put("files_iconText", value);
        Theme.defaultColors.put("sessions_devicesImage", -6908266);
        Theme.defaultColors.put("passport_authorizeBackground", -12211217);
        Theme.defaultColors.put("passport_authorizeBackgroundSelected", -12542501);
        Theme.defaultColors.put("passport_authorizeText", value);
        Theme.defaultColors.put("location_markerX", -8355712);
        Theme.defaultColors.put("location_sendLocationBackground", -9592620);
        Theme.defaultColors.put("location_sendLiveLocationBackground", -39836);
        Theme.defaultColors.put("location_sendLocationIcon", value);
        Theme.defaultColors.put("location_sendLiveLocationIcon", value);
        Theme.defaultColors.put("location_liveLocationProgress", -13262875);
        Theme.defaultColors.put("location_placeLocationBackground", -11753238);
        Theme.defaultColors.put("dialog_liveLocationProgress", -13262875);
        Theme.defaultColors.put("calls_callReceivedGreenIcon", -16725933);
        Theme.defaultColors.put("calls_callReceivedRedIcon", -47032);
        Theme.defaultColors.put("featuredStickers_addedIcon", -11491093);
        Theme.defaultColors.put("featuredStickers_buttonProgress", value);
        Theme.defaultColors.put("featuredStickers_addButton", -11491093);
        Theme.defaultColors.put("featuredStickers_addButtonPressed", -12346402);
        Theme.defaultColors.put("featuredStickers_delButton", -2533545);
        Theme.defaultColors.put("featuredStickers_delButtonPressed", -3782327);
        Theme.defaultColors.put("featuredStickers_buttonText", value);
        Theme.defaultColors.put("featuredStickers_unread", -11688214);
        Theme.defaultColors.put("inappPlayerPerformer", value5);
        Theme.defaultColors.put("inappPlayerTitle", value5);
        Theme.defaultColors.put("inappPlayerBackground", value);
        Theme.defaultColors.put("inappPlayerPlayPause", -10309397);
        Theme.defaultColors.put("inappPlayerClose", -5723992);
        Theme.defaultColors.put("returnToCallBackground", -12279325);
        Theme.defaultColors.put("returnToCallText", value);
        Theme.defaultColors.put("sharedMedia_startStopLoadIcon", -13196562);
        Theme.defaultColors.put("sharedMedia_linkPlaceholder", -986123);
        Theme.defaultColors.put("sharedMedia_linkPlaceholderText", -4735293);
        Theme.defaultColors.put("sharedMedia_photoPlaceholder", -1182729);
        Theme.defaultColors.put("sharedMedia_actionMode", -12154957);
        Theme.defaultColors.put("checkbox", -10567099);
        Theme.defaultColors.put("checkboxCheck", value);
        Theme.defaultColors.put("checkboxDisabled", -5195326);
        Theme.defaultColors.put("stickers_menu", -4801083);
        Theme.defaultColors.put("stickers_menuSelector", 251658240);
        Theme.defaultColors.put("changephoneinfo_image", -5723992);
        Theme.defaultColors.put("key_changephoneinfo_changeText", -11697229);
        Theme.defaultColors.put("groupcreate_hintText", value6);
        Theme.defaultColors.put("groupcreate_cursor", -11361317);
        Theme.defaultColors.put("groupcreate_sectionShadow", value4);
        Theme.defaultColors.put("groupcreate_sectionText", -8617336);
        Theme.defaultColors.put("groupcreate_spanText", value2);
        Theme.defaultColors.put("groupcreate_spanBackground", -855310);
        Theme.defaultColors.put("groupcreate_spanDelete", value);
        Theme.defaultColors.put("contacts_inviteBackground", -11157919);
        Theme.defaultColors.put("contacts_inviteText", value);
        Theme.defaultColors.put("login_progressInner", -1971470);
        Theme.defaultColors.put("login_progressOuter", -10313520);
        Theme.defaultColors.put("musicPicker_checkbox", -14043401);
        Theme.defaultColors.put("musicPicker_checkboxCheck", value);
        Theme.defaultColors.put("musicPicker_buttonBackground", -10702870);
        Theme.defaultColors.put("musicPicker_buttonIcon", value);
        Theme.defaultColors.put("picker_enabledButton", -15095832);
        Theme.defaultColors.put("picker_disabledButton", -6710887);
        Theme.defaultColors.put("picker_badge", -14043401);
        Theme.defaultColors.put("picker_badgeText", value);
        Theme.defaultColors.put("chat_botSwitchToInlineText", -12348980);
        Theme.defaultColors.put("undo_background", -366530760);
        Theme.defaultColors.put("undo_cancelColor", -8008961);
        Theme.defaultColors.put("undo_infoColor", value);
        Theme.fallbackKeys.put("chat_adminText", "chat_inTimeText");
        Theme.fallbackKeys.put("chat_adminSelectedText", "chat_inTimeSelectedText");
        Theme.fallbackKeys.put("key_player_progressCachedBackground", "player_progressBackground");
        Theme.fallbackKeys.put("chat_inAudioCacheSeekbar", "chat_inAudioSeekbar");
        Theme.fallbackKeys.put("chat_outAudioCacheSeekbar", "chat_outAudioSeekbar");
        Theme.fallbackKeys.put("chat_emojiSearchBackground", "chat_emojiPanelStickerPackSelector");
        Theme.fallbackKeys.put("location_sendLiveLocationIcon", "location_sendLocationIcon");
        Theme.fallbackKeys.put("key_changephoneinfo_changeText", "windowBackgroundWhiteBlueText4");
        Theme.fallbackKeys.put("key_graySectionText", "windowBackgroundWhiteGrayText2");
        Theme.fallbackKeys.put("chat_inMediaIcon", "chat_inBubble");
        Theme.fallbackKeys.put("chat_outMediaIcon", "chat_outBubble");
        Theme.fallbackKeys.put("chat_inMediaIconSelected", "chat_inBubbleSelected");
        Theme.fallbackKeys.put("chat_outMediaIconSelected", "chat_outBubbleSelected");
        Theme.fallbackKeys.put("chats_actionUnreadIcon", "profile_actionIcon");
        Theme.fallbackKeys.put("chats_actionUnreadBackground", "profile_actionBackground");
        Theme.fallbackKeys.put("chats_actionUnreadPressedBackground", "profile_actionPressedBackground");
        Theme.fallbackKeys.put("dialog_inlineProgressBackground", "windowBackgroundGray");
        Theme.fallbackKeys.put("dialog_inlineProgress", "chats_menuItemIcon");
        Theme.fallbackKeys.put("groupcreate_spanDelete", "chats_actionIcon");
        Theme.fallbackKeys.put("sharedMedia_photoPlaceholder", "windowBackgroundGray");
        Theme.fallbackKeys.put("chat_attachPollBackground", "chat_attachAudioBackground");
        Theme.fallbackKeys.put("chat_attachPollIcon", "chat_attachAudioIcon");
        Theme.fallbackKeys.put("chats_onlineCircle", "windowBackgroundWhiteBlueText");
        Theme.fallbackKeys.put("windowBackgroundWhiteBlueButton", "windowBackgroundWhiteValueText");
        Theme.fallbackKeys.put("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteValueText");
        Theme.fallbackKeys.put("undo_background", "chat_gifSaveHintBackground");
        Theme.fallbackKeys.put("undo_cancelColor", "chat_gifSaveHintText");
        Theme.fallbackKeys.put("undo_infoColor", "chat_gifSaveHintText");
        Theme.fallbackKeys.put("windowBackgroundUnchecked", "windowBackgroundWhite");
        Theme.fallbackKeys.put("windowBackgroundChecked", "windowBackgroundWhite");
        Theme.fallbackKeys.put("switchTrackBlue", "switchTrack");
        Theme.fallbackKeys.put("switchTrackBlueChecked", "switchTrackChecked");
        Theme.fallbackKeys.put("switchTrackBlueThumb", "windowBackgroundWhite");
        Theme.fallbackKeys.put("switchTrackBlueThumbChecked", "windowBackgroundWhite");
        Theme.fallbackKeys.put("windowBackgroundCheckText", "windowBackgroundWhiteBlackText");
        Theme.fallbackKeys.put("contextProgressInner4", "contextProgressInner1");
        Theme.fallbackKeys.put("contextProgressOuter4", "contextProgressOuter1");
        Theme.fallbackKeys.put("switchTrackBlueSelector", "listSelectorSDK21");
        Theme.fallbackKeys.put("switchTrackBlueSelectorChecked", "listSelectorSDK21");
        Theme.fallbackKeys.put("chat_emojiBottomPanelIcon", "chat_emojiPanelIcon");
        Theme.fallbackKeys.put("chat_emojiSearchIcon", "chat_emojiPanelIcon");
        Theme.fallbackKeys.put("chat_emojiPanelStickerSetNameHighlight", "windowBackgroundWhiteBlueText4");
        Theme.fallbackKeys.put("chat_emojiPanelStickerPackSelectorLine", "chat_emojiPanelIconSelected");
        Theme.fallbackKeys.put("sharedMedia_actionMode", "actionBarDefault");
        Theme.fallbackKeys.put("key_sheet_scrollUp", "chat_emojiPanelStickerPackSelector");
        Theme.fallbackKeys.put("key_sheet_other", "player_actionBarItems");
        Theme.fallbackKeys.put("dialogSearchBackground", "chat_emojiPanelStickerPackSelector");
        Theme.fallbackKeys.put("dialogSearchHint", "chat_emojiPanelIcon");
        Theme.fallbackKeys.put("dialogSearchIcon", "chat_emojiPanelIcon");
        Theme.fallbackKeys.put("dialogSearchText", "windowBackgroundWhiteBlackText");
        Theme.fallbackKeys.put("dialogFloatingButton", "dialogRoundCheckBox");
        Theme.fallbackKeys.put("dialogFloatingButtonPressed", "dialogRoundCheckBox");
        Theme.fallbackKeys.put("dialogFloatingIcon", "dialogRoundCheckBoxCheck");
        Theme.fallbackKeys.put("dialogShadowLine", "chat_emojiPanelShadowLine");
        Theme.fallbackKeys.put("actionBarDefaultArchived", "actionBarDefault");
        Theme.fallbackKeys.put("actionBarDefaultArchivedSelector", "actionBarDefaultSelector");
        Theme.fallbackKeys.put("actionBarDefaultArchivedIcon", "actionBarDefaultIcon");
        Theme.fallbackKeys.put("actionBarDefaultArchivedTitle", "actionBarDefaultTitle");
        Theme.fallbackKeys.put("actionBarDefaultArchivedSearch", "actionBarDefaultSearch");
        Theme.fallbackKeys.put("actionBarDefaultSearchArchivedPlaceholder", "actionBarDefaultSearchPlaceholder");
        Theme.fallbackKeys.put("chats_message_threeLines", "chats_message");
        Theme.fallbackKeys.put("chats_nameMessage_threeLines", "chats_nameMessage");
        Theme.fallbackKeys.put("chats_nameArchived", "chats_name");
        Theme.fallbackKeys.put("chats_nameMessageArchived", "chats_nameMessage");
        Theme.fallbackKeys.put("chats_nameMessageArchived_threeLines", "chats_nameMessage");
        Theme.fallbackKeys.put("chats_messageArchived", "chats_message");
        Theme.fallbackKeys.put("avatar_backgroundArchived", "chats_unreadCounterMuted");
        Theme.fallbackKeys.put("avatar_backgroundArchivedHidden", "chats_unreadCounterMuted");
        Theme.fallbackKeys.put("chats_archiveBackground", "chats_actionBackground");
        Theme.fallbackKeys.put("chats_archivePinBackground", "chats_unreadCounterMuted");
        Theme.fallbackKeys.put("chats_archiveIcon", "chats_actionIcon");
        Theme.fallbackKeys.put("chats_archiveText", "chats_actionIcon");
        Theme.fallbackKeys.put("actionBarDefaultSubmenuItemIcon", "dialogIcon");
        Theme.fallbackKeys.put("checkboxDisabled", "chats_unreadCounterMuted");
        Theme.fallbackKeys.put("chat_status", "actionBarDefaultSubtitle");
        Theme.fallbackKeys.put("chat_inDownCall", "calls_callReceivedGreenIcon");
        Theme.fallbackKeys.put("chat_inUpCall", "calls_callReceivedRedIcon");
        Theme.fallbackKeys.put("chat_outUpCall", "calls_callReceivedGreenIcon");
        Theme.fallbackKeys.put("actionBarTabActiveText", "actionBarDefaultTitle");
        Theme.fallbackKeys.put("actionBarTabUnactiveText", "actionBarDefaultSubtitle");
        Theme.fallbackKeys.put("actionBarTabLine", "actionBarDefaultTitle");
        Theme.fallbackKeys.put("actionBarTabSelector", "actionBarDefaultSelector");
        Theme.fallbackKeys.put("profile_status", "avatar_subtitleInProfileBlue");
        Theme.fallbackKeys.put("chats_menuTopBackgroundCats", "avatar_backgroundActionBarBlue");
        Theme.themes = new ArrayList<ThemeInfo>();
        Theme.otherThemes = new ArrayList<ThemeInfo>();
        Theme.themesDict = new HashMap<String, ThemeInfo>();
        Theme.currentColors = new HashMap<String, Integer>();
        final ThemeInfo currentDayTheme = new ThemeInfo();
        currentDayTheme.name = "Default";
        currentDayTheme.previewBackgroundColor = -3155485;
        currentDayTheme.previewInColor = -1;
        currentDayTheme.previewOutColor = -983328;
        currentDayTheme.sortIndex = 0;
        final ArrayList<ThemeInfo> themes = Theme.themes;
        Theme.defaultTheme = currentDayTheme;
        Theme.currentTheme = currentDayTheme;
        themes.add(Theme.currentDayTheme = currentDayTheme);
        Theme.themesDict.put("Default", Theme.defaultTheme);
        final ThemeInfo themeInfo = new ThemeInfo();
        themeInfo.name = "Dark";
        themeInfo.assetName = "dark.attheme";
        themeInfo.previewBackgroundColor = -10855071;
        themeInfo.previewInColor = -9143676;
        themeInfo.previewOutColor = -8214301;
        themeInfo.sortIndex = 3;
        Theme.themes.add(themeInfo);
        Theme.themesDict.put("Dark", themeInfo);
        final ThemeInfo themeInfo2 = new ThemeInfo();
        themeInfo2.name = "Blue";
        themeInfo2.assetName = "bluebubbles.attheme";
        themeInfo2.previewBackgroundColor = -6963476;
        themeInfo2.previewInColor = -1;
        themeInfo2.previewOutColor = -3086593;
        themeInfo2.sortIndex = 1;
        Theme.themes.add(themeInfo2);
        Theme.themesDict.put("Blue", themeInfo2);
        final ThemeInfo themeInfo3 = new ThemeInfo();
        themeInfo3.name = "Dark Blue";
        themeInfo3.assetName = "darkblue.attheme";
        themeInfo3.previewBackgroundColor = -10523006;
        themeInfo3.previewInColor = -9009508;
        themeInfo3.previewOutColor = -8214301;
        themeInfo3.sortIndex = 2;
        Theme.themes.add(themeInfo3);
        Theme.themesDict.put("Dark Blue", Theme.currentNightTheme = themeInfo3);
        if (BuildVars.DEBUG_VERSION) {
            final ThemeInfo themeInfo4 = new ThemeInfo();
            themeInfo4.name = "Graphite";
            themeInfo4.assetName = "graphite.attheme";
            themeInfo4.previewBackgroundColor = -8749431;
            themeInfo4.previewInColor = -6775901;
            themeInfo4.previewOutColor = -5980167;
            themeInfo4.sortIndex = 4;
            Theme.themes.add(themeInfo4);
            Theme.themesDict.put("Graphite", themeInfo4);
        }
        final ThemeInfo themeInfo5 = new ThemeInfo();
        themeInfo5.name = "Arctic Blue";
        themeInfo5.assetName = "arctic.attheme";
        themeInfo5.previewBackgroundColor = -1;
        themeInfo5.previewInColor = -1315084;
        themeInfo5.previewOutColor = -8604930;
        themeInfo5.sortIndex = 5;
        Theme.themes.add(themeInfo5);
        Theme.themesDict.put("Arctic Blue", themeInfo5);
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        final String string = sharedPreferences.getString("themes2", (String)null);
        Label_10849: {
            if (!TextUtils.isEmpty((CharSequence)string)) {
                try {
                    final JSONArray jsonArray = new JSONArray(string);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        final ThemeInfo withJson = ThemeInfo.createWithJson(jsonArray.getJSONObject(i));
                        if (withJson != null) {
                            Theme.otherThemes.add(withJson);
                            Theme.themes.add(withJson);
                            Theme.themesDict.put(withJson.name, withJson);
                        }
                    }
                    break Label_10849;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    break Label_10849;
                }
            }
            final String string2 = sharedPreferences.getString("themes", (String)null);
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                final String[] split = string2.split("&");
                for (int j = 0; j < split.length; ++j) {
                    final ThemeInfo withString = ThemeInfo.createWithString(split[j]);
                    if (withString != null) {
                        Theme.otherThemes.add(withString);
                        Theme.themes.add(withString);
                        Theme.themesDict.put(withString.name, withString);
                    }
                }
            }
            saveOtherThemes();
            sharedPreferences.edit().remove("themes").commit();
        }
        sortThemes();
        final ThemeInfo themeInfo6 = null;
        ThemeInfo defaultTheme = null;
        ThemeInfo themeInfo7 = themeInfo6;
        try {
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            themeInfo7 = themeInfo6;
            final String string3 = globalMainSettings.getString("theme", (String)null);
            if (string3 != null) {
                themeInfo7 = themeInfo6;
                defaultTheme = Theme.themesDict.get(string3);
            }
            themeInfo7 = defaultTheme;
            final String string4 = globalMainSettings.getString("nighttheme", (String)null);
            if (string4 != null) {
                themeInfo7 = defaultTheme;
                final ThemeInfo currentNightTheme = Theme.themesDict.get(string4);
                if (currentNightTheme != null) {
                    themeInfo7 = defaultTheme;
                    Theme.currentNightTheme = currentNightTheme;
                }
            }
            themeInfo7 = defaultTheme;
            Theme.selectedAutoNightType = globalMainSettings.getInt("selectedAutoNightType", 0);
            themeInfo7 = defaultTheme;
            Theme.autoNightScheduleByLocation = globalMainSettings.getBoolean("autoNightScheduleByLocation", false);
            themeInfo7 = defaultTheme;
            Theme.autoNightBrighnessThreshold = globalMainSettings.getFloat("autoNightBrighnessThreshold", 0.25f);
            themeInfo7 = defaultTheme;
            Theme.autoNightDayStartTime = globalMainSettings.getInt("autoNightDayStartTime", 1320);
            themeInfo7 = defaultTheme;
            Theme.autoNightDayEndTime = globalMainSettings.getInt("autoNightDayEndTime", 480);
            themeInfo7 = defaultTheme;
            Theme.autoNightSunsetTime = globalMainSettings.getInt("autoNightSunsetTime", 1320);
            themeInfo7 = defaultTheme;
            Theme.autoNightSunriseTime = globalMainSettings.getInt("autoNightSunriseTime", 480);
            themeInfo7 = defaultTheme;
            Theme.autoNightCityName = globalMainSettings.getString("autoNightCityName", "");
            themeInfo7 = defaultTheme;
            final long long1 = globalMainSettings.getLong("autoNightLocationLatitude3", 10000L);
            if (long1 != 10000L) {
                themeInfo7 = defaultTheme;
                Theme.autoNightLocationLatitude = Double.longBitsToDouble(long1);
            }
            else {
                themeInfo7 = defaultTheme;
                Theme.autoNightLocationLatitude = 10000.0;
            }
            themeInfo7 = defaultTheme;
            final long long2 = globalMainSettings.getLong("autoNightLocationLongitude3", 10000L);
            if (long2 != 10000L) {
                themeInfo7 = defaultTheme;
                Theme.autoNightLocationLongitude = Double.longBitsToDouble(long2);
            }
            else {
                themeInfo7 = defaultTheme;
                Theme.autoNightLocationLongitude = 10000.0;
            }
            themeInfo7 = defaultTheme;
            Theme.autoNightLastSunCheckDay = globalMainSettings.getInt("autoNightLastSunCheckDay", -1);
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
            defaultTheme = themeInfo7;
        }
        if (defaultTheme == null) {
            defaultTheme = Theme.defaultTheme;
        }
        else {
            Theme.currentDayTheme = defaultTheme;
        }
        applyTheme(defaultTheme, false, false, false);
        AndroidUtilities.runOnUIThread((Runnable)_$$Lambda$RQB0Jwr1FTqp6hrbGUHuOs_9k1I.INSTANCE);
        Theme.ambientSensorListener = (SensorEventListener)new SensorEventListener() {
            public void onAccuracyChanged(final Sensor sensor, final int n) {
            }
            
            public void onSensorChanged(final SensorEvent sensorEvent) {
                float n;
                if ((n = sensorEvent.values[0]) <= 0.0f) {
                    n = 0.1f;
                }
                if (!ApplicationLoader.mainInterfacePaused) {
                    if (ApplicationLoader.isScreenOn) {
                        if (n > 500.0f) {
                            Theme.lastBrightnessValue = 1.0f;
                        }
                        else {
                            Theme.lastBrightnessValue = (float)Math.ceil(Math.log(n) * 9.932299613952637 + 27.05900001525879) / 100.0f;
                        }
                        if (Theme.lastBrightnessValue <= Theme.autoNightBrighnessThreshold) {
                            if (!MediaController.getInstance().isRecordingOrListeningByProximity()) {
                                if (Theme.switchDayRunnableScheduled) {
                                    Theme.switchDayRunnableScheduled = false;
                                    AndroidUtilities.cancelRunOnUIThread(Theme.switchDayBrightnessRunnable);
                                }
                                if (!Theme.switchNightRunnableScheduled) {
                                    Theme.switchNightRunnableScheduled = true;
                                    AndroidUtilities.runOnUIThread(Theme.switchNightBrightnessRunnable, getAutoNightSwitchThemeDelay());
                                }
                            }
                        }
                        else {
                            if (Theme.switchNightRunnableScheduled) {
                                Theme.switchNightRunnableScheduled = false;
                                AndroidUtilities.cancelRunOnUIThread(Theme.switchNightBrightnessRunnable);
                            }
                            if (!Theme.switchDayRunnableScheduled) {
                                Theme.switchDayRunnableScheduled = true;
                                AndroidUtilities.runOnUIThread(Theme.switchDayBrightnessRunnable, getAutoNightSwitchThemeDelay());
                            }
                        }
                    }
                }
            }
        };
    }
    
    public static void applyChatServiceMessageColor() {
        applyChatServiceMessageColor(null);
    }
    
    public static void applyChatServiceMessageColor(final int[] array) {
        if (Theme.chat_actionBackgroundPaint == null) {
            return;
        }
        Theme.serviceMessageColor = Theme.serviceMessageColorBackup;
        Theme.serviceSelectedMessageColor = Theme.serviceSelectedMessageColorBackup;
        int i = 0;
        Integer n;
        Integer value2;
        if (array != null && array.length >= 2) {
            n = array[0];
            final Integer value = array[1];
            Theme.serviceMessageColor = array[0];
            Theme.serviceSelectedMessageColor = array[1];
            value2 = value;
        }
        else {
            n = Theme.currentColors.get("chat_serviceBackground");
            value2 = Theme.currentColors.get("chat_serviceBackgroundSelected");
        }
        Integer value3;
        if (n == null) {
            n = Theme.serviceMessageColor;
            value3 = Theme.serviceMessage2Color;
        }
        else {
            value3 = n;
        }
        Integer value4;
        if (value2 == null) {
            value2 = Theme.serviceSelectedMessageColor;
            value4 = Theme.serviceSelectedMessage2Color;
        }
        else {
            value4 = value2;
        }
        if (Theme.currentColor != n) {
            Theme.chat_actionBackgroundPaint.setColor((int)n);
            Theme.colorFilter = new PorterDuffColorFilter((int)n, PorterDuff$Mode.MULTIPLY);
            Theme.colorFilter2 = new PorterDuffColorFilter((int)value3, PorterDuff$Mode.MULTIPLY);
            Theme.currentColor = n;
            if (Theme.chat_cornerOuter[0] != null) {
                while (i < 4) {
                    Theme.chat_cornerOuter[i].setColorFilter((ColorFilter)Theme.colorFilter);
                    Theme.chat_cornerInner[i].setColorFilter((ColorFilter)Theme.colorFilter);
                    ++i;
                }
            }
        }
        if (Theme.currentSelectedColor != value2) {
            Theme.currentSelectedColor = value2;
            Theme.colorPressedFilter = new PorterDuffColorFilter((int)value2, PorterDuff$Mode.MULTIPLY);
            Theme.colorPressedFilter2 = new PorterDuffColorFilter((int)value4, PorterDuff$Mode.MULTIPLY);
        }
    }
    
    public static void applyChatTheme(final boolean b) {
        if (Theme.chat_msgTextPaint == null) {
            return;
        }
        if (Theme.chat_msgInDrawable != null && !b) {
            Theme.chat_gamePaint.setColor(getColor("chat_previewGameText"));
            Theme.chat_durationPaint.setColor(getColor("chat_previewDurationText"));
            Theme.chat_botButtonPaint.setColor(getColor("chat_botButtonText"));
            Theme.chat_urlPaint.setColor(getColor("chat_linkSelectBackground"));
            Theme.chat_botProgressPaint.setColor(getColor("chat_botProgress"));
            Theme.chat_deleteProgressPaint.setColor(getColor("chat_secretTimeText"));
            Theme.chat_textSearchSelectionPaint.setColor(getColor("chat_textSelectBackground"));
            Theme.chat_msgErrorPaint.setColor(getColor("chat_sentError"));
            Theme.chat_statusPaint.setColor(getColor("chat_status"));
            Theme.chat_statusRecordPaint.setColor(getColor("chat_status"));
            Theme.chat_actionTextPaint.setColor(getColor("chat_serviceText"));
            Theme.chat_actionTextPaint.linkColor = getColor("chat_serviceLink");
            Theme.chat_contextResult_titleTextPaint.setColor(getColor("windowBackgroundWhiteBlackText"));
            Theme.chat_composeBackgroundPaint.setColor(getColor("chat_messagePanelBackground"));
            Theme.chat_timeBackgroundPaint.setColor(getColor("chat_mediaTimeBackground"));
            setDrawableColorByKey(Theme.chat_msgNoSoundDrawable, "chat_mediaTimeText");
            setDrawableColorByKey(Theme.chat_msgInDrawable, "chat_inBubble");
            setDrawableColorByKey(Theme.chat_msgInSelectedDrawable, "chat_inBubbleSelected");
            setDrawableColorByKey(Theme.chat_msgInShadowDrawable, "chat_inBubbleShadow");
            setDrawableColorByKey(Theme.chat_msgOutDrawable, "chat_outBubble");
            setDrawableColorByKey(Theme.chat_msgOutSelectedDrawable, "chat_outBubbleSelected");
            setDrawableColorByKey(Theme.chat_msgOutShadowDrawable, "chat_outBubbleShadow");
            setDrawableColorByKey(Theme.chat_msgInMediaDrawable, "chat_inBubble");
            setDrawableColorByKey(Theme.chat_msgInMediaSelectedDrawable, "chat_inBubbleSelected");
            setDrawableColorByKey(Theme.chat_msgInMediaShadowDrawable, "chat_inBubbleShadow");
            setDrawableColorByKey(Theme.chat_msgOutMediaDrawable, "chat_outBubble");
            setDrawableColorByKey(Theme.chat_msgOutMediaSelectedDrawable, "chat_outBubbleSelected");
            setDrawableColorByKey(Theme.chat_msgOutMediaShadowDrawable, "chat_outBubbleShadow");
            setDrawableColorByKey(Theme.chat_msgOutCheckDrawable, "chat_outSentCheck");
            setDrawableColorByKey(Theme.chat_msgOutCheckSelectedDrawable, "chat_outSentCheckSelected");
            setDrawableColorByKey(Theme.chat_msgOutHalfCheckDrawable, "chat_outSentCheck");
            setDrawableColorByKey(Theme.chat_msgOutHalfCheckSelectedDrawable, "chat_outSentCheckSelected");
            setDrawableColorByKey(Theme.chat_msgOutClockDrawable, "chat_outSentClock");
            setDrawableColorByKey(Theme.chat_msgOutSelectedClockDrawable, "chat_outSentClockSelected");
            setDrawableColorByKey(Theme.chat_msgInClockDrawable, "chat_inSentClock");
            setDrawableColorByKey(Theme.chat_msgInSelectedClockDrawable, "chat_inSentClockSelected");
            setDrawableColorByKey(Theme.chat_msgMediaCheckDrawable, "chat_mediaSentCheck");
            setDrawableColorByKey(Theme.chat_msgMediaHalfCheckDrawable, "chat_mediaSentCheck");
            setDrawableColorByKey(Theme.chat_msgMediaClockDrawable, "chat_mediaSentClock");
            setDrawableColorByKey(Theme.chat_msgStickerCheckDrawable, "chat_serviceText");
            setDrawableColorByKey(Theme.chat_msgStickerHalfCheckDrawable, "chat_serviceText");
            setDrawableColorByKey(Theme.chat_msgStickerClockDrawable, "chat_serviceText");
            setDrawableColorByKey(Theme.chat_msgStickerViewsDrawable, "chat_serviceText");
            setDrawableColorByKey(Theme.chat_shareIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(Theme.chat_replyIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(Theme.chat_goIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(Theme.chat_botInlineDrawable, "chat_serviceIcon");
            setDrawableColorByKey(Theme.chat_botLinkDrawalbe, "chat_serviceIcon");
            setDrawableColorByKey(Theme.chat_msgInViewsDrawable, "chat_inViews");
            setDrawableColorByKey(Theme.chat_msgInViewsSelectedDrawable, "chat_inViewsSelected");
            setDrawableColorByKey(Theme.chat_msgOutViewsDrawable, "chat_outViews");
            setDrawableColorByKey(Theme.chat_msgOutViewsSelectedDrawable, "chat_outViewsSelected");
            setDrawableColorByKey(Theme.chat_msgMediaViewsDrawable, "chat_mediaViews");
            setDrawableColorByKey(Theme.chat_msgInMenuDrawable, "chat_inMenu");
            setDrawableColorByKey(Theme.chat_msgInMenuSelectedDrawable, "chat_inMenuSelected");
            setDrawableColorByKey(Theme.chat_msgOutMenuDrawable, "chat_outMenu");
            setDrawableColorByKey(Theme.chat_msgOutMenuSelectedDrawable, "chat_outMenuSelected");
            setDrawableColorByKey(Theme.chat_msgMediaMenuDrawable, "chat_mediaMenu");
            setDrawableColorByKey(Theme.chat_msgOutInstantDrawable, "chat_outInstant");
            setDrawableColorByKey(Theme.chat_msgInInstantDrawable, "chat_inInstant");
            setDrawableColorByKey(Theme.chat_msgErrorDrawable, "chat_sentErrorIcon");
            setDrawableColorByKey(Theme.chat_muteIconDrawable, "chat_muteIcon");
            setDrawableColorByKey(Theme.chat_lockIconDrawable, "chat_lockIcon");
            setDrawableColorByKey(Theme.chat_msgBroadcastDrawable, "chat_outBroadcast");
            setDrawableColorByKey(Theme.chat_msgBroadcastMediaDrawable, "chat_mediaBroadcast");
            setDrawableColorByKey(Theme.chat_inlineResultFile, "chat_inlineResultIcon");
            setDrawableColorByKey(Theme.chat_inlineResultAudio, "chat_inlineResultIcon");
            setDrawableColorByKey(Theme.chat_inlineResultLocation, "chat_inlineResultIcon");
            setDrawableColorByKey(Theme.chat_msgInCallDrawable, "chat_inInstant");
            setDrawableColorByKey(Theme.chat_msgInCallSelectedDrawable, "chat_inInstantSelected");
            setDrawableColorByKey(Theme.chat_msgOutCallDrawable, "chat_outInstant");
            setDrawableColorByKey(Theme.chat_msgOutCallSelectedDrawable, "chat_outInstantSelected");
            setDrawableColorByKey(Theme.chat_msgCallUpGreenDrawable, "chat_outUpCall");
            setDrawableColorByKey(Theme.chat_msgCallDownRedDrawable, "chat_inUpCall");
            setDrawableColorByKey(Theme.chat_msgCallDownGreenDrawable, "chat_inDownCall");
            setDrawableColorByKey(Theme.calllog_msgCallUpRedDrawable, "calls_callReceivedRedIcon");
            setDrawableColorByKey(Theme.calllog_msgCallUpGreenDrawable, "calls_callReceivedGreenIcon");
            setDrawableColorByKey(Theme.calllog_msgCallDownRedDrawable, "calls_callReceivedRedIcon");
            setDrawableColorByKey(Theme.calllog_msgCallDownGreenDrawable, "calls_callReceivedGreenIcon");
            for (int i = 0; i < 2; ++i) {
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[i][0], getColor("chat_outLoader"), false);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[i][0], getColor("chat_outMediaIcon"), true);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[i][1], getColor("chat_outLoaderSelected"), false);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[i][1], getColor("chat_outMediaIconSelected"), true);
                final CombinedDrawable[][] chat_fileMiniStatesDrawable = Theme.chat_fileMiniStatesDrawable;
                final int n = i + 2;
                setCombinedDrawableColor(chat_fileMiniStatesDrawable[n][0], getColor("chat_inLoader"), false);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[n][0], getColor("chat_inMediaIcon"), true);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[n][1], getColor("chat_inLoaderSelected"), false);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[n][1], getColor("chat_inMediaIconSelected"), true);
                final CombinedDrawable[][] chat_fileMiniStatesDrawable2 = Theme.chat_fileMiniStatesDrawable;
                final int n2 = i + 4;
                setCombinedDrawableColor(chat_fileMiniStatesDrawable2[n2][0], getColor("chat_mediaLoaderPhoto"), false);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[n2][0], getColor("chat_mediaLoaderPhotoIcon"), true);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[n2][1], getColor("chat_mediaLoaderPhotoSelected"), false);
                setCombinedDrawableColor(Theme.chat_fileMiniStatesDrawable[n2][1], getColor("chat_mediaLoaderPhotoIconSelected"), true);
            }
            for (int j = 0; j < 5; ++j) {
                setCombinedDrawableColor(Theme.chat_fileStatesDrawable[j][0], getColor("chat_outLoader"), false);
                setCombinedDrawableColor(Theme.chat_fileStatesDrawable[j][0], getColor("chat_outMediaIcon"), true);
                setCombinedDrawableColor(Theme.chat_fileStatesDrawable[j][1], getColor("chat_outLoaderSelected"), false);
                setCombinedDrawableColor(Theme.chat_fileStatesDrawable[j][1], getColor("chat_outMediaIconSelected"), true);
                final Drawable[][] chat_fileStatesDrawable = Theme.chat_fileStatesDrawable;
                final int n3 = j + 5;
                setCombinedDrawableColor(chat_fileStatesDrawable[n3][0], getColor("chat_inLoader"), false);
                setCombinedDrawableColor(Theme.chat_fileStatesDrawable[n3][0], getColor("chat_inMediaIcon"), true);
                setCombinedDrawableColor(Theme.chat_fileStatesDrawable[n3][1], getColor("chat_inLoaderSelected"), false);
                setCombinedDrawableColor(Theme.chat_fileStatesDrawable[n3][1], getColor("chat_inMediaIconSelected"), true);
            }
            for (int k = 0; k < 4; ++k) {
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[k][0], getColor("chat_mediaLoaderPhoto"), false);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[k][0], getColor("chat_mediaLoaderPhotoIcon"), true);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[k][1], getColor("chat_mediaLoaderPhotoSelected"), false);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[k][1], getColor("chat_mediaLoaderPhotoIconSelected"), true);
            }
            for (int l = 0; l < 2; ++l) {
                final Drawable[][] chat_photoStatesDrawables = Theme.chat_photoStatesDrawables;
                final int n4 = l + 7;
                setCombinedDrawableColor(chat_photoStatesDrawables[n4][0], getColor("chat_outLoaderPhoto"), false);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[n4][0], getColor("chat_outLoaderPhotoIcon"), true);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[n4][1], getColor("chat_outLoaderPhotoSelected"), false);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[n4][1], getColor("chat_outLoaderPhotoIconSelected"), true);
                final Drawable[][] chat_photoStatesDrawables2 = Theme.chat_photoStatesDrawables;
                final int n5 = l + 10;
                setCombinedDrawableColor(chat_photoStatesDrawables2[n5][0], getColor("chat_inLoaderPhoto"), false);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[n5][0], getColor("chat_inLoaderPhotoIcon"), true);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[n5][1], getColor("chat_inLoaderPhotoSelected"), false);
                setCombinedDrawableColor(Theme.chat_photoStatesDrawables[n5][1], getColor("chat_inLoaderPhotoIconSelected"), true);
            }
            setDrawableColorByKey(Theme.chat_photoStatesDrawables[9][0], "chat_outFileIcon");
            setDrawableColorByKey(Theme.chat_photoStatesDrawables[9][1], "chat_outFileSelectedIcon");
            setDrawableColorByKey(Theme.chat_photoStatesDrawables[12][0], "chat_inFileIcon");
            setDrawableColorByKey(Theme.chat_photoStatesDrawables[12][1], "chat_inFileSelectedIcon");
            setCombinedDrawableColor(Theme.chat_contactDrawable[0], getColor("chat_inContactBackground"), false);
            setCombinedDrawableColor(Theme.chat_contactDrawable[0], getColor("chat_inContactIcon"), true);
            setCombinedDrawableColor(Theme.chat_contactDrawable[1], getColor("chat_outContactBackground"), false);
            setCombinedDrawableColor(Theme.chat_contactDrawable[1], getColor("chat_outContactIcon"), true);
            setCombinedDrawableColor(Theme.chat_locationDrawable[0], getColor("chat_inLocationBackground"), false);
            setCombinedDrawableColor(Theme.chat_locationDrawable[0], getColor("chat_inLocationIcon"), true);
            setCombinedDrawableColor(Theme.chat_locationDrawable[1], getColor("chat_outLocationBackground"), false);
            setCombinedDrawableColor(Theme.chat_locationDrawable[1], getColor("chat_outLocationIcon"), true);
            setDrawableColorByKey(Theme.chat_composeShadowDrawable, "chat_messagePanelShadow");
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[1], getColor("chat_attachGalleryBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[1], getColor("chat_attachGalleryIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[2], getColor("chat_attachVideoBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[2], getColor("chat_attachVideoIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[3], getColor("chat_attachAudioBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[3], getColor("chat_attachAudioIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[4], getColor("chat_attachFileBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[4], getColor("chat_attachFileIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[5], getColor("chat_attachContactBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[5], getColor("chat_attachContactIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[6], getColor("chat_attachLocationBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[6], getColor("chat_attachLocationIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[7], getColor("chat_attachHideBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[7], getColor("chat_attachHideIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[8], getColor("chat_attachSendBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[8], getColor("chat_attachSendIcon"), true);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[9], getColor("chat_attachPollBackground"), false);
            setCombinedDrawableColor(Theme.chat_attachButtonDrawables[9], getColor("chat_attachPollIcon"), true);
            applyChatServiceMessageColor();
        }
    }
    
    public static void applyCommonTheme() {
        final Paint dividerPaint = Theme.dividerPaint;
        if (dividerPaint == null) {
            return;
        }
        dividerPaint.setColor(getColor("divider"));
        Theme.linkSelectionPaint.setColor(getColor("windowBackgroundWhiteLinkSelection"));
        setDrawableColorByKey(Theme.avatar_broadcastDrawable, "avatar_text");
        setDrawableColorByKey(Theme.avatar_savedDrawable, "avatar_text");
        Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "**" }), LottieProperty.COLOR_FILTER, null);
        Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Arrow1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("avatar_backgroundArchived"))));
        Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Arrow2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("avatar_backgroundArchived"))));
        Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Box2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("avatar_text"))));
        Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Box1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("avatar_text"))));
        Theme.dialogs_archiveAvatarDrawableRecolored = false;
        final Drawable dialogs_pinArchiveDrawable = Theme.dialogs_pinArchiveDrawable;
        if (dialogs_pinArchiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable = (LottieDrawable)dialogs_pinArchiveDrawable;
            lottieDrawable.addValueCallback(new KeyPath(new String[] { "**" }), LottieProperty.COLOR_FILTER, null);
            lottieDrawable.addValueCallback(new KeyPath(new String[] { "Arrow", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
            lottieDrawable.addValueCallback(new KeyPath(new String[] { "Line", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
        }
        else {
            setDrawableColorByKey(dialogs_pinArchiveDrawable, "chats_archiveIcon");
        }
        final Drawable dialogs_unpinArchiveDrawable = Theme.dialogs_unpinArchiveDrawable;
        if (dialogs_unpinArchiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable2 = (LottieDrawable)dialogs_unpinArchiveDrawable;
            lottieDrawable2.addValueCallback(new KeyPath(new String[] { "**" }), LottieProperty.COLOR_FILTER, null);
            lottieDrawable2.addValueCallback(new KeyPath(new String[] { "Arrow", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
            lottieDrawable2.addValueCallback(new KeyPath(new String[] { "Line", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
        }
        else {
            setDrawableColorByKey(dialogs_unpinArchiveDrawable, "chats_archiveIcon");
        }
        final Drawable dialogs_archiveDrawable = Theme.dialogs_archiveDrawable;
        if (dialogs_archiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable3 = (LottieDrawable)dialogs_archiveDrawable;
            lottieDrawable3.addValueCallback(new KeyPath(new String[] { "**" }), LottieProperty.COLOR_FILTER, null);
            lottieDrawable3.addValueCallback(new KeyPath(new String[] { "Arrow", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveBackground"))));
            lottieDrawable3.addValueCallback(new KeyPath(new String[] { "Box2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
            lottieDrawable3.addValueCallback(new KeyPath(new String[] { "Box1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
            Theme.dialogs_archiveDrawableRecolored = false;
        }
        else {
            setDrawableColorByKey(dialogs_archiveDrawable, "chats_archiveIcon");
        }
        final Drawable dialogs_unarchiveDrawable = Theme.dialogs_unarchiveDrawable;
        if (dialogs_unarchiveDrawable instanceof LottieDrawable) {
            final LottieDrawable lottieDrawable4 = (LottieDrawable)dialogs_unarchiveDrawable;
            lottieDrawable4.addValueCallback(new KeyPath(new String[] { "**" }), LottieProperty.COLOR_FILTER, null);
            lottieDrawable4.addValueCallback(new KeyPath(new String[] { "Arrow1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
            lottieDrawable4.addValueCallback(new KeyPath(new String[] { "Arrow2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archivePinBackground"))));
            lottieDrawable4.addValueCallback(new KeyPath(new String[] { "Box2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
            lottieDrawable4.addValueCallback(new KeyPath(new String[] { "Box1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(getColor("chats_archiveIcon"))));
        }
        else {
            setDrawableColorByKey(dialogs_unarchiveDrawable, "chats_archiveIcon");
        }
    }
    
    private static void applyDayNightThemeMaybe(final boolean b) {
        final Boolean value = true;
        if (b) {
            if (Theme.currentTheme != Theme.currentNightTheme) {
                Theme.lastThemeSwitchTime = SystemClock.elapsedRealtime();
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, Theme.currentNightTheme, value);
            }
        }
        else if (Theme.currentTheme != Theme.currentDayTheme) {
            Theme.lastThemeSwitchTime = SystemClock.elapsedRealtime();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, Theme.currentDayTheme, value);
        }
    }
    
    public static void applyDialogsTheme() {
        final TextPaint dialogs_namePaint = Theme.dialogs_namePaint;
        if (dialogs_namePaint == null) {
            return;
        }
        dialogs_namePaint.setColor(getColor("chats_name"));
        Theme.dialogs_nameEncryptedPaint.setColor(getColor("chats_secretName"));
        Theme.dialogs_searchNamePaint.setColor(getColor("chats_name"));
        Theme.dialogs_searchNameEncryptedPaint.setColor(getColor("chats_secretName"));
        final TextPaint dialogs_messagePaint = Theme.dialogs_messagePaint;
        dialogs_messagePaint.setColor(dialogs_messagePaint.linkColor = getColor("chats_message"));
        final TextPaint dialogs_messageNamePaint = Theme.dialogs_messageNamePaint;
        dialogs_messageNamePaint.setColor(dialogs_messageNamePaint.linkColor = getColor("chats_nameMessage_threeLines"));
        Theme.dialogs_tabletSeletedPaint.setColor(getColor("chats_tabletSelectedOverlay"));
        Theme.dialogs_pinnedPaint.setColor(getColor("chats_pinnedOverlay"));
        Theme.dialogs_timePaint.setColor(getColor("chats_date"));
        Theme.dialogs_countTextPaint.setColor(getColor("chats_unreadCounterText"));
        Theme.dialogs_archiveTextPaint.setColor(getColor("chats_archiveText"));
        Theme.dialogs_messagePrintingPaint.setColor(getColor("chats_actionMessage"));
        Theme.dialogs_countPaint.setColor(getColor("chats_unreadCounter"));
        Theme.dialogs_countGrayPaint.setColor(getColor("chats_unreadCounterMuted"));
        Theme.dialogs_errorPaint.setColor(getColor("chats_sentError"));
        Theme.dialogs_onlinePaint.setColor(getColor("windowBackgroundWhiteBlueText3"));
        Theme.dialogs_offlinePaint.setColor(getColor("windowBackgroundWhiteGrayText3"));
        setDrawableColorByKey(Theme.dialogs_lockDrawable, "chats_secretIcon");
        setDrawableColorByKey(Theme.dialogs_checkDrawable, "chats_sentCheck");
        setDrawableColorByKey(Theme.dialogs_halfCheckDrawable, "chats_sentCheck");
        setDrawableColorByKey(Theme.dialogs_clockDrawable, "chats_sentClock");
        setDrawableColorByKey(Theme.dialogs_errorDrawable, "chats_sentErrorIcon");
        setDrawableColorByKey(Theme.dialogs_groupDrawable, "chats_nameIcon");
        setDrawableColorByKey(Theme.dialogs_broadcastDrawable, "chats_nameIcon");
        setDrawableColorByKey(Theme.dialogs_botDrawable, "chats_nameIcon");
        setDrawableColorByKey(Theme.dialogs_pinnedDrawable, "chats_pinnedIcon");
        setDrawableColorByKey(Theme.dialogs_reorderDrawable, "chats_pinnedIcon");
        setDrawableColorByKey(Theme.dialogs_muteDrawable, "chats_muteIcon");
        setDrawableColorByKey(Theme.dialogs_mentionDrawable, "chats_mentionIcon");
        setDrawableColorByKey(Theme.dialogs_verifiedDrawable, "chats_verifiedBackground");
        setDrawableColorByKey(Theme.dialogs_verifiedCheckDrawable, "chats_verifiedCheck");
        setDrawableColorByKey(Theme.dialogs_holidayDrawable, "actionBarDefaultTitle");
        setDrawableColorByKey(Theme.dialogs_scamDrawable, "chats_draft");
    }
    
    public static void applyPreviousTheme() {
        final ThemeInfo previousTheme = Theme.previousTheme;
        if (previousTheme == null) {
            return;
        }
        applyTheme(previousTheme, true, false, false);
        Theme.previousTheme = null;
        checkAutoNightThemeConditions();
    }
    
    public static void applyProfileTheme() {
        if (Theme.profile_verifiedDrawable == null) {
            return;
        }
        Theme.profile_aboutTextPaint.setColor(getColor("windowBackgroundWhiteBlackText"));
        Theme.profile_aboutTextPaint.linkColor = getColor("windowBackgroundWhiteLinkText");
        setDrawableColorByKey(Theme.profile_verifiedDrawable, "profile_verifiedBackground");
        setDrawableColorByKey(Theme.profile_verifiedCheckDrawable, "profile_verifiedCheck");
    }
    
    public static void applyTheme(final ThemeInfo themeInfo) {
        applyTheme(themeInfo, true, true, false);
    }
    
    public static void applyTheme(final ThemeInfo themeInfo, final boolean b) {
        applyTheme(themeInfo, true, true, b);
    }
    
    public static void applyTheme(final ThemeInfo currentTheme, final boolean b, final boolean b2, final boolean b3) {
        if (currentTheme == null) {
            return;
        }
        final ThemeEditorView instance = ThemeEditorView.getInstance();
        if (instance != null) {
            instance.destroy();
        }
        try {
            if (currentTheme.pathToFile == null && currentTheme.assetName == null) {
                if (!b3 && b) {
                    final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
                    edit.remove("theme");
                    if (b2) {
                        edit.remove("overrideThemeWallpaper");
                    }
                    edit.commit();
                }
                Theme.currentColors.clear();
                Theme.themedWallpaperFileOffset = 0;
                Theme.wallpaper = null;
                Theme.themedWallpaper = null;
            }
            else {
                if (!b3 && b) {
                    final SharedPreferences$Editor edit2 = MessagesController.getGlobalMainSettings().edit();
                    edit2.putString("theme", currentTheme.name);
                    if (b2) {
                        edit2.remove("overrideThemeWallpaper");
                    }
                    edit2.commit();
                }
                if (currentTheme.assetName != null) {
                    Theme.currentColors = getThemeFileValues(null, currentTheme.assetName);
                }
                else {
                    Theme.currentColors = getThemeFileValues(new File(currentTheme.pathToFile), null);
                }
            }
            Theme.currentTheme = currentTheme;
            if (!b3) {
                Theme.currentDayTheme = Theme.currentTheme;
            }
            reloadWallpaper();
            applyCommonTheme();
            applyDialogsTheme();
            applyProfileTheme();
            applyChatTheme(false);
            AndroidUtilities.runOnUIThread(new _$$Lambda$Theme$U5dmA2RnRuUehj9EUY9kmkkhUlE(b3));
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static ThemeInfo applyThemeFile(final File file, final String name, final boolean b) {
        try {
            if (name.equals("Default") || name.equals("Dark") || name.equals("Blue") || name.equals("Dark Blue") || name.equals("Graphite") || name.equals("Arctic Blue")) {
                return null;
            }
            final File file2 = new File(ApplicationLoader.getFilesDirFixed(), name);
            if (!AndroidUtilities.copyFile(file, file2)) {
                return null;
            }
            ThemeInfo e = Theme.themesDict.get(name);
            boolean b2;
            if (e == null) {
                e = new ThemeInfo();
                e.name = name;
                e.pathToFile = file2.getAbsolutePath();
                b2 = true;
            }
            else {
                b2 = false;
            }
            if (!b) {
                Theme.previousTheme = null;
                if (b2) {
                    Theme.themes.add(e);
                    Theme.themesDict.put(e.name, e);
                    Theme.otherThemes.add(e);
                    sortThemes();
                    saveOtherThemes();
                }
            }
            else {
                Theme.previousTheme = Theme.currentTheme;
            }
            applyTheme(e, !b, true, false);
            return e;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    private static void calcBackgroundColor(final Drawable drawable, int n) {
        if (n != 2) {
            final int[] calcDrawableColor = AndroidUtilities.calcDrawableColor(drawable);
            n = (Theme.serviceMessageColor = (Theme.serviceMessageColorBackup = calcDrawableColor[0]));
            n = (Theme.serviceSelectedMessageColor = (Theme.serviceSelectedMessageColorBackup = calcDrawableColor[1]));
            Theme.serviceMessage2Color = calcDrawableColor[2];
            Theme.serviceSelectedMessage2Color = calcDrawableColor[3];
        }
    }
    
    public static boolean canStartHolidayAnimation() {
        return Theme.canStartHolidayAnimation;
    }
    
    public static void checkAutoNightThemeConditions() {
        checkAutoNightThemeConditions(false);
    }
    
    public static void checkAutoNightThemeConditions(final boolean b) {
        if (Theme.previousTheme != null) {
            return;
        }
        boolean b2 = false;
        if (b) {
            if (Theme.switchNightRunnableScheduled) {
                Theme.switchNightRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(Theme.switchNightBrightnessRunnable);
            }
            if (Theme.switchDayRunnableScheduled) {
                Theme.switchDayRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(Theme.switchDayBrightnessRunnable);
            }
        }
        if (Theme.selectedAutoNightType != 2) {
            if (Theme.switchNightRunnableScheduled) {
                Theme.switchNightRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(Theme.switchNightBrightnessRunnable);
            }
            if (Theme.switchDayRunnableScheduled) {
                Theme.switchDayRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(Theme.switchDayBrightnessRunnable);
            }
            if (Theme.lightSensorRegistered) {
                Theme.lastBrightnessValue = 1.0f;
                Theme.sensorManager.unregisterListener(Theme.ambientSensorListener, Theme.lightSensor);
                Theme.lightSensorRegistered = false;
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("light sensor unregistered");
                }
            }
        }
        final int selectedAutoNightType = Theme.selectedAutoNightType;
        int n = 0;
        Label_0427: {
            Label_0420: {
                Label_0402: {
                    if (selectedAutoNightType != 1) {
                        if (selectedAutoNightType == 2) {
                            if (Theme.lightSensor == null) {
                                Theme.sensorManager = (SensorManager)ApplicationLoader.applicationContext.getSystemService("sensor");
                                Theme.lightSensor = Theme.sensorManager.getDefaultSensor(5);
                            }
                            if (!Theme.lightSensorRegistered) {
                                final Sensor lightSensor = Theme.lightSensor;
                                if (lightSensor != null) {
                                    Theme.sensorManager.registerListener(Theme.ambientSensorListener, lightSensor, 500000);
                                    Theme.lightSensorRegistered = true;
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("light sensor registered");
                                    }
                                }
                            }
                            if (!((Theme.lastBrightnessValue > Theme.autoNightBrighnessThreshold) ? Theme.switchDayRunnableScheduled : Theme.switchNightRunnableScheduled)) {
                                break Label_0402;
                            }
                        }
                        else if (selectedAutoNightType == 0) {
                            break Label_0420;
                        }
                        n = 0;
                        break Label_0427;
                    }
                    final Calendar instance = Calendar.getInstance();
                    instance.setTimeInMillis(System.currentTimeMillis());
                    final int n2 = instance.get(11) * 60 + instance.get(12);
                    int n3;
                    int n4;
                    if (Theme.autoNightScheduleByLocation) {
                        final int value = instance.get(5);
                        if (Theme.autoNightLastSunCheckDay != value) {
                            final double autoNightLocationLatitude = Theme.autoNightLocationLatitude;
                            if (autoNightLocationLatitude != 10000.0) {
                                final double autoNightLocationLongitude = Theme.autoNightLocationLongitude;
                                if (autoNightLocationLongitude != 10000.0) {
                                    final int[] calculateSunriseSunset = SunDate.calculateSunriseSunset(autoNightLocationLatitude, autoNightLocationLongitude);
                                    Theme.autoNightSunriseTime = calculateSunriseSunset[0];
                                    Theme.autoNightSunsetTime = calculateSunriseSunset[1];
                                    Theme.autoNightLastSunCheckDay = value;
                                    saveAutoNightThemeConfig();
                                }
                            }
                        }
                        n3 = Theme.autoNightSunsetTime;
                        n4 = Theme.autoNightSunriseTime;
                    }
                    else {
                        n3 = Theme.autoNightDayStartTime;
                        n4 = Theme.autoNightDayEndTime;
                    }
                    if (n3 < n4) {
                        if (n3 > n2 || n2 > n4) {
                            break Label_0420;
                        }
                    }
                    else if (n3 > n2 || n2 > 1440) {
                        if (n2 < 0 || n2 > n4) {
                            break Label_0420;
                        }
                    }
                }
                n = 2;
                break Label_0427;
            }
            n = 1;
        }
        if (n != 0) {
            if (n == 2) {
                b2 = true;
            }
            applyDayNightThemeMaybe(b2);
        }
        if (b) {
            Theme.lastThemeSwitchTime = 0L;
        }
    }
    
    public static void createChatResources(final Context p0, final boolean p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_2       
        //     4: aload_2        
        //     5: monitorenter   
        //     6: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaint:Landroid/text/TextPaint;
        //     9: ifnonnull       103
        //    12: new             Landroid/text/TextPaint;
        //    15: astore_3       
        //    16: aload_3        
        //    17: iconst_1       
        //    18: invokespecial   android/text/TextPaint.<init>:(I)V
        //    21: aload_3        
        //    22: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaint:Landroid/text/TextPaint;
        //    25: new             Landroid/text/TextPaint;
        //    28: astore_3       
        //    29: aload_3        
        //    30: iconst_1       
        //    31: invokespecial   android/text/TextPaint.<init>:(I)V
        //    34: aload_3        
        //    35: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgGameTextPaint:Landroid/text/TextPaint;
        //    38: new             Landroid/text/TextPaint;
        //    41: astore_3       
        //    42: aload_3        
        //    43: iconst_1       
        //    44: invokespecial   android/text/TextPaint.<init>:(I)V
        //    47: aload_3        
        //    48: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaintOneEmoji:Landroid/text/TextPaint;
        //    51: new             Landroid/text/TextPaint;
        //    54: astore_3       
        //    55: aload_3        
        //    56: iconst_1       
        //    57: invokespecial   android/text/TextPaint.<init>:(I)V
        //    60: aload_3        
        //    61: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaintTwoEmoji:Landroid/text/TextPaint;
        //    64: new             Landroid/text/TextPaint;
        //    67: astore_3       
        //    68: aload_3        
        //    69: iconst_1       
        //    70: invokespecial   android/text/TextPaint.<init>:(I)V
        //    73: aload_3        
        //    74: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaintThreeEmoji:Landroid/text/TextPaint;
        //    77: new             Landroid/text/TextPaint;
        //    80: astore_3       
        //    81: aload_3        
        //    82: iconst_1       
        //    83: invokespecial   android/text/TextPaint.<init>:(I)V
        //    86: aload_3        
        //    87: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgBotButtonPaint:Landroid/text/TextPaint;
        //    90: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgBotButtonPaint:Landroid/text/TextPaint;
        //    93: ldc_w           "fonts/rmedium.ttf"
        //    96: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //    99: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   102: pop            
        //   103: aload_2        
        //   104: monitorexit    
        //   105: iload_1        
        //   106: ifne            3321
        //   109: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgInDrawable:Landroid/graphics/drawable/Drawable;
        //   112: ifnonnull       3321
        //   115: new             Landroid/text/TextPaint;
        //   118: dup            
        //   119: iconst_1       
        //   120: invokespecial   android/text/TextPaint.<init>:(I)V
        //   123: putstatic       org/telegram/ui/ActionBar/Theme.chat_infoPaint:Landroid/text/TextPaint;
        //   126: new             Landroid/text/TextPaint;
        //   129: dup            
        //   130: iconst_1       
        //   131: invokespecial   android/text/TextPaint.<init>:(I)V
        //   134: putstatic       org/telegram/ui/ActionBar/Theme.chat_docNamePaint:Landroid/text/TextPaint;
        //   137: getstatic       org/telegram/ui/ActionBar/Theme.chat_docNamePaint:Landroid/text/TextPaint;
        //   140: ldc_w           "fonts/rmedium.ttf"
        //   143: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   146: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   149: pop            
        //   150: new             Landroid/graphics/Paint;
        //   153: dup            
        //   154: iconst_1       
        //   155: invokespecial   android/graphics/Paint.<init>:(I)V
        //   158: putstatic       org/telegram/ui/ActionBar/Theme.chat_docBackPaint:Landroid/graphics/Paint;
        //   161: new             Landroid/graphics/Paint;
        //   164: dup            
        //   165: iconst_1       
        //   166: invokespecial   android/graphics/Paint.<init>:(I)V
        //   169: putstatic       org/telegram/ui/ActionBar/Theme.chat_deleteProgressPaint:Landroid/graphics/Paint;
        //   172: new             Landroid/graphics/Paint;
        //   175: dup            
        //   176: iconst_1       
        //   177: invokespecial   android/graphics/Paint.<init>:(I)V
        //   180: putstatic       org/telegram/ui/ActionBar/Theme.chat_botProgressPaint:Landroid/graphics/Paint;
        //   183: getstatic       org/telegram/ui/ActionBar/Theme.chat_botProgressPaint:Landroid/graphics/Paint;
        //   186: getstatic       android/graphics/Paint$Cap.ROUND:Landroid/graphics/Paint$Cap;
        //   189: invokevirtual   android/graphics/Paint.setStrokeCap:(Landroid/graphics/Paint$Cap;)V
        //   192: getstatic       org/telegram/ui/ActionBar/Theme.chat_botProgressPaint:Landroid/graphics/Paint;
        //   195: getstatic       android/graphics/Paint$Style.STROKE:Landroid/graphics/Paint$Style;
        //   198: invokevirtual   android/graphics/Paint.setStyle:(Landroid/graphics/Paint$Style;)V
        //   201: new             Landroid/text/TextPaint;
        //   204: dup            
        //   205: iconst_1       
        //   206: invokespecial   android/text/TextPaint.<init>:(I)V
        //   209: putstatic       org/telegram/ui/ActionBar/Theme.chat_locationTitlePaint:Landroid/text/TextPaint;
        //   212: getstatic       org/telegram/ui/ActionBar/Theme.chat_locationTitlePaint:Landroid/text/TextPaint;
        //   215: ldc_w           "fonts/rmedium.ttf"
        //   218: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   221: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   224: pop            
        //   225: new             Landroid/text/TextPaint;
        //   228: dup            
        //   229: iconst_1       
        //   230: invokespecial   android/text/TextPaint.<init>:(I)V
        //   233: putstatic       org/telegram/ui/ActionBar/Theme.chat_locationAddressPaint:Landroid/text/TextPaint;
        //   236: new             Landroid/graphics/Paint;
        //   239: dup            
        //   240: invokespecial   android/graphics/Paint.<init>:()V
        //   243: putstatic       org/telegram/ui/ActionBar/Theme.chat_urlPaint:Landroid/graphics/Paint;
        //   246: new             Landroid/graphics/Paint;
        //   249: dup            
        //   250: invokespecial   android/graphics/Paint.<init>:()V
        //   253: putstatic       org/telegram/ui/ActionBar/Theme.chat_textSearchSelectionPaint:Landroid/graphics/Paint;
        //   256: new             Landroid/graphics/Paint;
        //   259: dup            
        //   260: iconst_1       
        //   261: invokespecial   android/graphics/Paint.<init>:(I)V
        //   264: putstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgressPaint:Landroid/graphics/Paint;
        //   267: getstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgressPaint:Landroid/graphics/Paint;
        //   270: getstatic       android/graphics/Paint$Cap.ROUND:Landroid/graphics/Paint$Cap;
        //   273: invokevirtual   android/graphics/Paint.setStrokeCap:(Landroid/graphics/Paint$Cap;)V
        //   276: getstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgressPaint:Landroid/graphics/Paint;
        //   279: getstatic       android/graphics/Paint$Style.STROKE:Landroid/graphics/Paint$Style;
        //   282: invokevirtual   android/graphics/Paint.setStyle:(Landroid/graphics/Paint$Style;)V
        //   285: getstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgressPaint:Landroid/graphics/Paint;
        //   288: ldc_w           -1610612737
        //   291: invokevirtual   android/graphics/Paint.setColor:(I)V
        //   294: new             Landroid/graphics/Paint;
        //   297: dup            
        //   298: iconst_1       
        //   299: invokespecial   android/graphics/Paint.<init>:(I)V
        //   302: putstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgress2Paint:Landroid/graphics/Paint;
        //   305: getstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgress2Paint:Landroid/graphics/Paint;
        //   308: getstatic       android/graphics/Paint$Cap.ROUND:Landroid/graphics/Paint$Cap;
        //   311: invokevirtual   android/graphics/Paint.setStrokeCap:(Landroid/graphics/Paint$Cap;)V
        //   314: getstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgress2Paint:Landroid/graphics/Paint;
        //   317: getstatic       android/graphics/Paint$Style.STROKE:Landroid/graphics/Paint$Style;
        //   320: invokevirtual   android/graphics/Paint.setStyle:(Landroid/graphics/Paint$Style;)V
        //   323: new             Landroid/text/TextPaint;
        //   326: dup            
        //   327: iconst_1       
        //   328: invokespecial   android/text/TextPaint.<init>:(I)V
        //   331: putstatic       org/telegram/ui/ActionBar/Theme.chat_audioTimePaint:Landroid/text/TextPaint;
        //   334: new             Landroid/text/TextPaint;
        //   337: dup            
        //   338: iconst_1       
        //   339: invokespecial   android/text/TextPaint.<init>:(I)V
        //   342: putstatic       org/telegram/ui/ActionBar/Theme.chat_livePaint:Landroid/text/TextPaint;
        //   345: getstatic       org/telegram/ui/ActionBar/Theme.chat_livePaint:Landroid/text/TextPaint;
        //   348: ldc_w           "fonts/rmedium.ttf"
        //   351: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   354: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   357: pop            
        //   358: new             Landroid/text/TextPaint;
        //   361: dup            
        //   362: iconst_1       
        //   363: invokespecial   android/text/TextPaint.<init>:(I)V
        //   366: putstatic       org/telegram/ui/ActionBar/Theme.chat_audioTitlePaint:Landroid/text/TextPaint;
        //   369: getstatic       org/telegram/ui/ActionBar/Theme.chat_audioTitlePaint:Landroid/text/TextPaint;
        //   372: ldc_w           "fonts/rmedium.ttf"
        //   375: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   378: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   381: pop            
        //   382: new             Landroid/text/TextPaint;
        //   385: dup            
        //   386: iconst_1       
        //   387: invokespecial   android/text/TextPaint.<init>:(I)V
        //   390: putstatic       org/telegram/ui/ActionBar/Theme.chat_audioPerformerPaint:Landroid/text/TextPaint;
        //   393: new             Landroid/text/TextPaint;
        //   396: dup            
        //   397: iconst_1       
        //   398: invokespecial   android/text/TextPaint.<init>:(I)V
        //   401: putstatic       org/telegram/ui/ActionBar/Theme.chat_botButtonPaint:Landroid/text/TextPaint;
        //   404: getstatic       org/telegram/ui/ActionBar/Theme.chat_botButtonPaint:Landroid/text/TextPaint;
        //   407: ldc_w           "fonts/rmedium.ttf"
        //   410: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   413: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   416: pop            
        //   417: new             Landroid/text/TextPaint;
        //   420: dup            
        //   421: iconst_1       
        //   422: invokespecial   android/text/TextPaint.<init>:(I)V
        //   425: putstatic       org/telegram/ui/ActionBar/Theme.chat_contactNamePaint:Landroid/text/TextPaint;
        //   428: getstatic       org/telegram/ui/ActionBar/Theme.chat_contactNamePaint:Landroid/text/TextPaint;
        //   431: ldc_w           "fonts/rmedium.ttf"
        //   434: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   437: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   440: pop            
        //   441: new             Landroid/text/TextPaint;
        //   444: dup            
        //   445: iconst_1       
        //   446: invokespecial   android/text/TextPaint.<init>:(I)V
        //   449: putstatic       org/telegram/ui/ActionBar/Theme.chat_contactPhonePaint:Landroid/text/TextPaint;
        //   452: new             Landroid/text/TextPaint;
        //   455: dup            
        //   456: iconst_1       
        //   457: invokespecial   android/text/TextPaint.<init>:(I)V
        //   460: putstatic       org/telegram/ui/ActionBar/Theme.chat_durationPaint:Landroid/text/TextPaint;
        //   463: new             Landroid/text/TextPaint;
        //   466: dup            
        //   467: iconst_1       
        //   468: invokespecial   android/text/TextPaint.<init>:(I)V
        //   471: putstatic       org/telegram/ui/ActionBar/Theme.chat_gamePaint:Landroid/text/TextPaint;
        //   474: getstatic       org/telegram/ui/ActionBar/Theme.chat_gamePaint:Landroid/text/TextPaint;
        //   477: ldc_w           "fonts/rmedium.ttf"
        //   480: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   483: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   486: pop            
        //   487: new             Landroid/text/TextPaint;
        //   490: dup            
        //   491: iconst_1       
        //   492: invokespecial   android/text/TextPaint.<init>:(I)V
        //   495: putstatic       org/telegram/ui/ActionBar/Theme.chat_shipmentPaint:Landroid/text/TextPaint;
        //   498: new             Landroid/text/TextPaint;
        //   501: dup            
        //   502: iconst_1       
        //   503: invokespecial   android/text/TextPaint.<init>:(I)V
        //   506: putstatic       org/telegram/ui/ActionBar/Theme.chat_timePaint:Landroid/text/TextPaint;
        //   509: new             Landroid/text/TextPaint;
        //   512: dup            
        //   513: iconst_1       
        //   514: invokespecial   android/text/TextPaint.<init>:(I)V
        //   517: putstatic       org/telegram/ui/ActionBar/Theme.chat_adminPaint:Landroid/text/TextPaint;
        //   520: new             Landroid/text/TextPaint;
        //   523: dup            
        //   524: iconst_1       
        //   525: invokespecial   android/text/TextPaint.<init>:(I)V
        //   528: putstatic       org/telegram/ui/ActionBar/Theme.chat_namePaint:Landroid/text/TextPaint;
        //   531: getstatic       org/telegram/ui/ActionBar/Theme.chat_namePaint:Landroid/text/TextPaint;
        //   534: ldc_w           "fonts/rmedium.ttf"
        //   537: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   540: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   543: pop            
        //   544: new             Landroid/text/TextPaint;
        //   547: dup            
        //   548: iconst_1       
        //   549: invokespecial   android/text/TextPaint.<init>:(I)V
        //   552: putstatic       org/telegram/ui/ActionBar/Theme.chat_forwardNamePaint:Landroid/text/TextPaint;
        //   555: new             Landroid/text/TextPaint;
        //   558: dup            
        //   559: iconst_1       
        //   560: invokespecial   android/text/TextPaint.<init>:(I)V
        //   563: putstatic       org/telegram/ui/ActionBar/Theme.chat_replyNamePaint:Landroid/text/TextPaint;
        //   566: getstatic       org/telegram/ui/ActionBar/Theme.chat_replyNamePaint:Landroid/text/TextPaint;
        //   569: ldc_w           "fonts/rmedium.ttf"
        //   572: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   575: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   578: pop            
        //   579: new             Landroid/text/TextPaint;
        //   582: dup            
        //   583: iconst_1       
        //   584: invokespecial   android/text/TextPaint.<init>:(I)V
        //   587: putstatic       org/telegram/ui/ActionBar/Theme.chat_replyTextPaint:Landroid/text/TextPaint;
        //   590: new             Landroid/text/TextPaint;
        //   593: dup            
        //   594: iconst_1       
        //   595: invokespecial   android/text/TextPaint.<init>:(I)V
        //   598: putstatic       org/telegram/ui/ActionBar/Theme.chat_instantViewPaint:Landroid/text/TextPaint;
        //   601: getstatic       org/telegram/ui/ActionBar/Theme.chat_instantViewPaint:Landroid/text/TextPaint;
        //   604: ldc_w           "fonts/rmedium.ttf"
        //   607: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   610: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   613: pop            
        //   614: new             Landroid/graphics/Paint;
        //   617: dup            
        //   618: iconst_1       
        //   619: invokespecial   android/graphics/Paint.<init>:(I)V
        //   622: putstatic       org/telegram/ui/ActionBar/Theme.chat_instantViewRectPaint:Landroid/graphics/Paint;
        //   625: getstatic       org/telegram/ui/ActionBar/Theme.chat_instantViewRectPaint:Landroid/graphics/Paint;
        //   628: getstatic       android/graphics/Paint$Style.STROKE:Landroid/graphics/Paint$Style;
        //   631: invokevirtual   android/graphics/Paint.setStyle:(Landroid/graphics/Paint$Style;)V
        //   634: new             Landroid/graphics/Paint;
        //   637: dup            
        //   638: iconst_1       
        //   639: invokespecial   android/graphics/Paint.<init>:(I)V
        //   642: putstatic       org/telegram/ui/ActionBar/Theme.chat_replyLinePaint:Landroid/graphics/Paint;
        //   645: new             Landroid/graphics/Paint;
        //   648: dup            
        //   649: iconst_1       
        //   650: invokespecial   android/graphics/Paint.<init>:(I)V
        //   653: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgErrorPaint:Landroid/graphics/Paint;
        //   656: new             Landroid/graphics/Paint;
        //   659: dup            
        //   660: iconst_1       
        //   661: invokespecial   android/graphics/Paint.<init>:(I)V
        //   664: putstatic       org/telegram/ui/ActionBar/Theme.chat_statusPaint:Landroid/graphics/Paint;
        //   667: new             Landroid/graphics/Paint;
        //   670: dup            
        //   671: iconst_1       
        //   672: invokespecial   android/graphics/Paint.<init>:(I)V
        //   675: putstatic       org/telegram/ui/ActionBar/Theme.chat_statusRecordPaint:Landroid/graphics/Paint;
        //   678: getstatic       org/telegram/ui/ActionBar/Theme.chat_statusRecordPaint:Landroid/graphics/Paint;
        //   681: getstatic       android/graphics/Paint$Style.STROKE:Landroid/graphics/Paint$Style;
        //   684: invokevirtual   android/graphics/Paint.setStyle:(Landroid/graphics/Paint$Style;)V
        //   687: getstatic       org/telegram/ui/ActionBar/Theme.chat_statusRecordPaint:Landroid/graphics/Paint;
        //   690: getstatic       android/graphics/Paint$Cap.ROUND:Landroid/graphics/Paint$Cap;
        //   693: invokevirtual   android/graphics/Paint.setStrokeCap:(Landroid/graphics/Paint$Cap;)V
        //   696: new             Landroid/text/TextPaint;
        //   699: dup            
        //   700: iconst_1       
        //   701: invokespecial   android/text/TextPaint.<init>:(I)V
        //   704: putstatic       org/telegram/ui/ActionBar/Theme.chat_actionTextPaint:Landroid/text/TextPaint;
        //   707: getstatic       org/telegram/ui/ActionBar/Theme.chat_actionTextPaint:Landroid/text/TextPaint;
        //   710: ldc_w           "fonts/rmedium.ttf"
        //   713: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   716: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   719: pop            
        //   720: new             Landroid/graphics/Paint;
        //   723: dup            
        //   724: iconst_1       
        //   725: invokespecial   android/graphics/Paint.<init>:(I)V
        //   728: putstatic       org/telegram/ui/ActionBar/Theme.chat_actionBackgroundPaint:Landroid/graphics/Paint;
        //   731: new             Landroid/graphics/Paint;
        //   734: dup            
        //   735: iconst_1       
        //   736: invokespecial   android/graphics/Paint.<init>:(I)V
        //   739: putstatic       org/telegram/ui/ActionBar/Theme.chat_timeBackgroundPaint:Landroid/graphics/Paint;
        //   742: new             Landroid/text/TextPaint;
        //   745: dup            
        //   746: iconst_1       
        //   747: invokespecial   android/text/TextPaint.<init>:(I)V
        //   750: putstatic       org/telegram/ui/ActionBar/Theme.chat_contextResult_titleTextPaint:Landroid/text/TextPaint;
        //   753: getstatic       org/telegram/ui/ActionBar/Theme.chat_contextResult_titleTextPaint:Landroid/text/TextPaint;
        //   756: ldc_w           "fonts/rmedium.ttf"
        //   759: invokestatic    org/telegram/messenger/AndroidUtilities.getTypeface:(Ljava/lang/String;)Landroid/graphics/Typeface;
        //   762: invokevirtual   android/text/TextPaint.setTypeface:(Landroid/graphics/Typeface;)Landroid/graphics/Typeface;
        //   765: pop            
        //   766: new             Landroid/text/TextPaint;
        //   769: dup            
        //   770: iconst_1       
        //   771: invokespecial   android/text/TextPaint.<init>:(I)V
        //   774: putstatic       org/telegram/ui/ActionBar/Theme.chat_contextResult_descriptionTextPaint:Landroid/text/TextPaint;
        //   777: new             Landroid/graphics/Paint;
        //   780: dup            
        //   781: invokespecial   android/graphics/Paint.<init>:()V
        //   784: putstatic       org/telegram/ui/ActionBar/Theme.chat_composeBackgroundPaint:Landroid/graphics/Paint;
        //   787: aload_0        
        //   788: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //   791: astore_2       
        //   792: aload_2        
        //   793: ldc_w           2131165635
        //   796: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   799: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   802: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInDrawable:Landroid/graphics/drawable/Drawable;
        //   805: aload_2        
        //   806: ldc_w           2131165635
        //   809: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   812: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   815: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //   818: aload_2        
        //   819: ldc_w           2131165907
        //   822: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   825: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgNoSoundDrawable:Landroid/graphics/drawable/Drawable;
        //   828: aload_2        
        //   829: ldc_w           2131165650
        //   832: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   835: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   838: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutDrawable:Landroid/graphics/drawable/Drawable;
        //   841: aload_2        
        //   842: ldc_w           2131165650
        //   845: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   848: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   851: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //   854: aload_2        
        //   855: ldc_w           2131165655
        //   858: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   861: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   864: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInMediaDrawable:Landroid/graphics/drawable/Drawable;
        //   867: aload_2        
        //   868: ldc_w           2131165655
        //   871: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   874: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   877: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInMediaSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //   880: aload_2        
        //   881: ldc_w           2131165655
        //   884: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   887: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   890: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutMediaDrawable:Landroid/graphics/drawable/Drawable;
        //   893: aload_2        
        //   894: ldc_w           2131165655
        //   897: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   900: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   903: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutMediaSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //   906: aload_2        
        //   907: ldc_w           2131165618
        //   910: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   913: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   916: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutCheckDrawable:Landroid/graphics/drawable/Drawable;
        //   919: aload_2        
        //   920: ldc_w           2131165618
        //   923: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   926: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   929: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutCheckSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //   932: aload_2        
        //   933: ldc_w           2131165618
        //   936: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   939: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   942: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgMediaCheckDrawable:Landroid/graphics/drawable/Drawable;
        //   945: aload_2        
        //   946: ldc_w           2131165618
        //   949: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   952: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   955: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgStickerCheckDrawable:Landroid/graphics/drawable/Drawable;
        //   958: aload_2        
        //   959: ldc_w           2131165632
        //   962: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   965: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   968: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutHalfCheckDrawable:Landroid/graphics/drawable/Drawable;
        //   971: aload_2        
        //   972: ldc_w           2131165632
        //   975: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   978: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   981: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutHalfCheckSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //   984: aload_2        
        //   985: ldc_w           2131165632
        //   988: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //   991: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //   994: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgMediaHalfCheckDrawable:Landroid/graphics/drawable/Drawable;
        //   997: aload_2        
        //   998: ldc_w           2131165632
        //  1001: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1004: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1007: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgStickerHalfCheckDrawable:Landroid/graphics/drawable/Drawable;
        //  1010: aload_2        
        //  1011: ldc_w           2131165620
        //  1014: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1017: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1020: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutClockDrawable:Landroid/graphics/drawable/Drawable;
        //  1023: aload_2        
        //  1024: ldc_w           2131165620
        //  1027: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1030: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1033: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutSelectedClockDrawable:Landroid/graphics/drawable/Drawable;
        //  1036: aload_2        
        //  1037: ldc_w           2131165620
        //  1040: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1043: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1046: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInClockDrawable:Landroid/graphics/drawable/Drawable;
        //  1049: aload_2        
        //  1050: ldc_w           2131165620
        //  1053: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1056: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1059: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInSelectedClockDrawable:Landroid/graphics/drawable/Drawable;
        //  1062: aload_2        
        //  1063: ldc_w           2131165620
        //  1066: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1069: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1072: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgMediaClockDrawable:Landroid/graphics/drawable/Drawable;
        //  1075: aload_2        
        //  1076: ldc_w           2131165620
        //  1079: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1082: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1085: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgStickerClockDrawable:Landroid/graphics/drawable/Drawable;
        //  1088: aload_2        
        //  1089: ldc_w           2131165682
        //  1092: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1095: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1098: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInViewsDrawable:Landroid/graphics/drawable/Drawable;
        //  1101: aload_2        
        //  1102: ldc_w           2131165682
        //  1105: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1108: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1111: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInViewsSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //  1114: aload_2        
        //  1115: ldc_w           2131165682
        //  1118: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1121: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1124: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutViewsDrawable:Landroid/graphics/drawable/Drawable;
        //  1127: aload_2        
        //  1128: ldc_w           2131165682
        //  1131: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1134: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1137: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutViewsSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //  1140: aload_2        
        //  1141: ldc_w           2131165682
        //  1144: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1147: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1150: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgMediaViewsDrawable:Landroid/graphics/drawable/Drawable;
        //  1153: aload_2        
        //  1154: ldc_w           2131165682
        //  1157: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1160: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1163: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgStickerViewsDrawable:Landroid/graphics/drawable/Drawable;
        //  1166: aload_2        
        //  1167: ldc_w           2131165610
        //  1170: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1173: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1176: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInMenuDrawable:Landroid/graphics/drawable/Drawable;
        //  1179: aload_2        
        //  1180: ldc_w           2131165610
        //  1183: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1186: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1189: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInMenuSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //  1192: aload_2        
        //  1193: ldc_w           2131165610
        //  1196: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1199: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1202: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutMenuDrawable:Landroid/graphics/drawable/Drawable;
        //  1205: aload_2        
        //  1206: ldc_w           2131165610
        //  1209: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1212: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1215: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutMenuSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //  1218: aload_2        
        //  1219: ldc_w           2131165900
        //  1222: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1225: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgMediaMenuDrawable:Landroid/graphics/drawable/Drawable;
        //  1228: aload_2        
        //  1229: ldc_w           2131165637
        //  1232: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1235: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1238: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInInstantDrawable:Landroid/graphics/drawable/Drawable;
        //  1241: aload_2        
        //  1242: ldc_w           2131165637
        //  1245: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1248: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1251: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutInstantDrawable:Landroid/graphics/drawable/Drawable;
        //  1254: aload_2        
        //  1255: ldc_w           2131165683
        //  1258: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1261: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgErrorDrawable:Landroid/graphics/drawable/Drawable;
        //  1264: aload_2        
        //  1265: ldc_w           2131165528
        //  1268: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1271: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1274: putstatic       org/telegram/ui/ActionBar/Theme.chat_muteIconDrawable:Landroid/graphics/drawable/Drawable;
        //  1277: aload_2        
        //  1278: ldc_w           2131165447
        //  1281: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1284: putstatic       org/telegram/ui/ActionBar/Theme.chat_lockIconDrawable:Landroid/graphics/drawable/Drawable;
        //  1287: aload_2        
        //  1288: ldc_w           2131165325
        //  1291: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1294: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1297: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgBroadcastDrawable:Landroid/graphics/drawable/Drawable;
        //  1300: aload_2        
        //  1301: ldc_w           2131165325
        //  1304: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1307: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1310: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgBroadcastMediaDrawable:Landroid/graphics/drawable/Drawable;
        //  1313: aload_2        
        //  1314: ldc_w           2131165429
        //  1317: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1320: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1323: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInCallDrawable:Landroid/graphics/drawable/Drawable;
        //  1326: aload_2        
        //  1327: ldc_w           2131165429
        //  1330: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1333: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1336: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInCallSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //  1339: aload_2        
        //  1340: ldc_w           2131165429
        //  1343: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1346: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1349: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutCallDrawable:Landroid/graphics/drawable/Drawable;
        //  1352: aload_2        
        //  1353: ldc_w           2131165429
        //  1356: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1359: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1362: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutCallSelectedDrawable:Landroid/graphics/drawable/Drawable;
        //  1365: aload_2        
        //  1366: ldc_w           2131165432
        //  1369: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1372: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1375: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgCallUpGreenDrawable:Landroid/graphics/drawable/Drawable;
        //  1378: aload_2        
        //  1379: ldc_w           2131165435
        //  1382: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1385: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1388: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgCallDownRedDrawable:Landroid/graphics/drawable/Drawable;
        //  1391: aload_2        
        //  1392: ldc_w           2131165435
        //  1395: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1398: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1401: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgCallDownGreenDrawable:Landroid/graphics/drawable/Drawable;
        //  1404: aload_2        
        //  1405: ldc_w           2131165432
        //  1408: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1411: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1414: putstatic       org/telegram/ui/ActionBar/Theme.calllog_msgCallUpRedDrawable:Landroid/graphics/drawable/Drawable;
        //  1417: aload_2        
        //  1418: ldc_w           2131165432
        //  1421: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1424: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1427: putstatic       org/telegram/ui/ActionBar/Theme.calllog_msgCallUpGreenDrawable:Landroid/graphics/drawable/Drawable;
        //  1430: aload_2        
        //  1431: ldc_w           2131165435
        //  1434: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1437: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1440: putstatic       org/telegram/ui/ActionBar/Theme.calllog_msgCallDownRedDrawable:Landroid/graphics/drawable/Drawable;
        //  1443: aload_2        
        //  1444: ldc_w           2131165435
        //  1447: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1450: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1453: putstatic       org/telegram/ui/ActionBar/Theme.calllog_msgCallDownGreenDrawable:Landroid/graphics/drawable/Drawable;
        //  1456: aload_2        
        //  1457: ldc_w           2131165536
        //  1460: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1463: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1466: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgAvatarLiveLocationDrawable:Landroid/graphics/drawable/Drawable;
        //  1469: aload_2        
        //  1470: ldc_w           2131165316
        //  1473: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1476: putstatic       org/telegram/ui/ActionBar/Theme.chat_inlineResultFile:Landroid/graphics/drawable/Drawable;
        //  1479: aload_2        
        //  1480: ldc_w           2131165320
        //  1483: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1486: putstatic       org/telegram/ui/ActionBar/Theme.chat_inlineResultAudio:Landroid/graphics/drawable/Drawable;
        //  1489: aload_2        
        //  1490: ldc_w           2131165319
        //  1493: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1496: putstatic       org/telegram/ui/ActionBar/Theme.chat_inlineResultLocation:Landroid/graphics/drawable/Drawable;
        //  1499: aload_2        
        //  1500: ldc_w           2131165551
        //  1503: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1506: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1509: putstatic       org/telegram/ui/ActionBar/Theme.chat_redLocationIcon:Landroid/graphics/drawable/Drawable;
        //  1512: aload_2        
        //  1513: ldc_w           2131165636
        //  1516: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1519: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInShadowDrawable:Landroid/graphics/drawable/Drawable;
        //  1522: aload_2        
        //  1523: ldc_w           2131165651
        //  1526: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1529: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutShadowDrawable:Landroid/graphics/drawable/Drawable;
        //  1532: aload_2        
        //  1533: ldc_w           2131165656
        //  1536: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1539: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgInMediaShadowDrawable:Landroid/graphics/drawable/Drawable;
        //  1542: aload_2        
        //  1543: ldc_w           2131165656
        //  1546: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1549: putstatic       org/telegram/ui/ActionBar/Theme.chat_msgOutMediaShadowDrawable:Landroid/graphics/drawable/Drawable;
        //  1552: aload_2        
        //  1553: ldc_w           2131165318
        //  1556: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1559: putstatic       org/telegram/ui/ActionBar/Theme.chat_botLinkDrawalbe:Landroid/graphics/drawable/Drawable;
        //  1562: aload_2        
        //  1563: ldc_w           2131165317
        //  1566: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1569: putstatic       org/telegram/ui/ActionBar/Theme.chat_botInlineDrawable:Landroid/graphics/drawable/Drawable;
        //  1572: aload_2        
        //  1573: ldc_w           2131165871
        //  1576: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1579: putstatic       org/telegram/ui/ActionBar/Theme.chat_systemDrawable:Landroid/graphics/drawable/Drawable;
        //  1582: aload_2        
        //  1583: ldc_w           2131165407
        //  1586: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1589: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  1592: putstatic       org/telegram/ui/ActionBar/Theme.chat_contextResult_shadowUnderSwitchDrawable:Landroid/graphics/drawable/Drawable;
        //  1595: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1598: astore_3       
        //  1599: new             Lorg/telegram/ui/ActionBar/Theme$AttachCameraDrawable;
        //  1602: dup            
        //  1603: invokespecial   org/telegram/ui/ActionBar/Theme$AttachCameraDrawable.<init>:()V
        //  1606: astore          4
        //  1608: iconst_0       
        //  1609: istore          5
        //  1611: aload_3        
        //  1612: iconst_0       
        //  1613: aload           4
        //  1615: aastore        
        //  1616: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1619: iconst_1       
        //  1620: ldc_w           54.0
        //  1623: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1626: ldc_w           2131165289
        //  1629: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1632: aastore        
        //  1633: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1636: iconst_2       
        //  1637: ldc_w           54.0
        //  1640: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1643: ldc_w           2131165293
        //  1646: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1649: aastore        
        //  1650: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1653: iconst_3       
        //  1654: ldc_w           54.0
        //  1657: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1660: ldc_w           2131165285
        //  1663: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1666: aastore        
        //  1667: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1670: iconst_4       
        //  1671: ldc_w           54.0
        //  1674: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1677: ldc_w           2131165288
        //  1680: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1683: aastore        
        //  1684: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1687: iconst_5       
        //  1688: ldc_w           54.0
        //  1691: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1694: ldc_w           2131165287
        //  1697: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1700: aastore        
        //  1701: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1704: bipush          6
        //  1706: ldc_w           54.0
        //  1709: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1712: ldc_w           2131165290
        //  1715: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1718: aastore        
        //  1719: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1722: bipush          7
        //  1724: ldc_w           54.0
        //  1727: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1730: ldc_w           2131165286
        //  1733: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1736: aastore        
        //  1737: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1740: bipush          8
        //  1742: ldc_w           54.0
        //  1745: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1748: ldc_w           2131165292
        //  1751: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1754: aastore        
        //  1755: getstatic       org/telegram/ui/ActionBar/Theme.chat_attachButtonDrawables:[Landroid/graphics/drawable/Drawable;
        //  1758: bipush          9
        //  1760: ldc_w           54.0
        //  1763: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1766: ldc_w           2131165291
        //  1769: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1772: aastore        
        //  1773: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerOuter:[Landroid/graphics/drawable/Drawable;
        //  1776: iconst_0       
        //  1777: aload_2        
        //  1778: ldc_w           2131165370
        //  1781: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1784: aastore        
        //  1785: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerOuter:[Landroid/graphics/drawable/Drawable;
        //  1788: iconst_1       
        //  1789: aload_2        
        //  1790: ldc_w           2131165371
        //  1793: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1796: aastore        
        //  1797: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerOuter:[Landroid/graphics/drawable/Drawable;
        //  1800: iconst_2       
        //  1801: aload_2        
        //  1802: ldc_w           2131165369
        //  1805: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1808: aastore        
        //  1809: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerOuter:[Landroid/graphics/drawable/Drawable;
        //  1812: iconst_3       
        //  1813: aload_2        
        //  1814: ldc_w           2131165368
        //  1817: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1820: aastore        
        //  1821: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerInner:[Landroid/graphics/drawable/Drawable;
        //  1824: iconst_0       
        //  1825: aload_2        
        //  1826: ldc_w           2131165367
        //  1829: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1832: aastore        
        //  1833: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerInner:[Landroid/graphics/drawable/Drawable;
        //  1836: iconst_1       
        //  1837: aload_2        
        //  1838: ldc_w           2131165366
        //  1841: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1844: aastore        
        //  1845: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerInner:[Landroid/graphics/drawable/Drawable;
        //  1848: iconst_2       
        //  1849: aload_2        
        //  1850: ldc_w           2131165365
        //  1853: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1856: aastore        
        //  1857: getstatic       org/telegram/ui/ActionBar/Theme.chat_cornerInner:[Landroid/graphics/drawable/Drawable;
        //  1860: iconst_3       
        //  1861: aload_2        
        //  1862: ldc_w           2131165364
        //  1865: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1868: aastore        
        //  1869: aload_2        
        //  1870: ldc_w           2131165820
        //  1873: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1876: putstatic       org/telegram/ui/ActionBar/Theme.chat_shareDrawable:Landroid/graphics/drawable/Drawable;
        //  1879: aload_2        
        //  1880: ldc_w           2131165819
        //  1883: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1886: putstatic       org/telegram/ui/ActionBar/Theme.chat_shareIconDrawable:Landroid/graphics/drawable/Drawable;
        //  1889: aload_2        
        //  1890: ldc_w           2131165377
        //  1893: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1896: putstatic       org/telegram/ui/ActionBar/Theme.chat_replyIconDrawable:Landroid/graphics/drawable/Drawable;
        //  1899: aload_2        
        //  1900: ldc_w           2131165601
        //  1903: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  1906: putstatic       org/telegram/ui/ActionBar/Theme.chat_goIconDrawable:Landroid/graphics/drawable/Drawable;
        //  1909: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  1912: iconst_0       
        //  1913: aaload         
        //  1914: iconst_0       
        //  1915: ldc_w           22.0
        //  1918: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1921: ldc_w           2131165294
        //  1924: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1927: aastore        
        //  1928: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  1931: iconst_0       
        //  1932: aaload         
        //  1933: iconst_1       
        //  1934: ldc_w           22.0
        //  1937: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1940: ldc_w           2131165294
        //  1943: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1946: aastore        
        //  1947: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  1950: iconst_1       
        //  1951: aaload         
        //  1952: iconst_0       
        //  1953: ldc_w           22.0
        //  1956: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1959: ldc_w           2131165295
        //  1962: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1965: aastore        
        //  1966: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  1969: iconst_1       
        //  1970: aaload         
        //  1971: iconst_1       
        //  1972: ldc_w           22.0
        //  1975: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1978: ldc_w           2131165295
        //  1981: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  1984: aastore        
        //  1985: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  1988: iconst_2       
        //  1989: aaload         
        //  1990: iconst_0       
        //  1991: ldc_w           22.0
        //  1994: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  1997: ldc_w           2131165294
        //  2000: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2003: aastore        
        //  2004: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  2007: iconst_2       
        //  2008: aaload         
        //  2009: iconst_1       
        //  2010: ldc_w           22.0
        //  2013: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2016: ldc_w           2131165294
        //  2019: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2022: aastore        
        //  2023: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  2026: iconst_3       
        //  2027: aaload         
        //  2028: iconst_0       
        //  2029: ldc_w           22.0
        //  2032: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2035: ldc_w           2131165295
        //  2038: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2041: aastore        
        //  2042: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  2045: iconst_3       
        //  2046: aaload         
        //  2047: iconst_1       
        //  2048: ldc_w           22.0
        //  2051: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2054: ldc_w           2131165295
        //  2057: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2060: aastore        
        //  2061: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  2064: iconst_4       
        //  2065: aaload         
        //  2066: iconst_0       
        //  2067: ldc_w           22.0
        //  2070: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2073: ldc_w           2131165904
        //  2076: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2079: aastore        
        //  2080: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  2083: iconst_4       
        //  2084: aaload         
        //  2085: iconst_1       
        //  2086: ldc_w           22.0
        //  2089: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2092: ldc_w           2131165904
        //  2095: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2098: aastore        
        //  2099: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  2102: iconst_5       
        //  2103: aaload         
        //  2104: iconst_0       
        //  2105: ldc_w           22.0
        //  2108: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2111: ldc_w           2131165905
        //  2114: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2117: aastore        
        //  2118: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileMiniStatesDrawable:[[Lorg/telegram/ui/Components/CombinedDrawable;
        //  2121: iconst_5       
        //  2122: aaload         
        //  2123: iconst_1       
        //  2124: ldc_w           22.0
        //  2127: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2130: ldc_w           2131165905
        //  2133: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2136: aastore        
        //  2137: aload_2        
        //  2138: ldc_w           2131165663
        //  2141: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2144: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  2147: putstatic       org/telegram/ui/ActionBar/Theme.chat_fileIcon:Landroid/graphics/drawable/Drawable;
        //  2150: aload_2        
        //  2151: ldc_w           2131165329
        //  2154: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2157: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  2160: putstatic       org/telegram/ui/ActionBar/Theme.chat_flameIcon:Landroid/graphics/drawable/Drawable;
        //  2163: aload_2        
        //  2164: ldc_w           2131165664
        //  2167: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2170: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  2173: putstatic       org/telegram/ui/ActionBar/Theme.chat_gifIcon:Landroid/graphics/drawable/Drawable;
        //  2176: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2179: iconst_0       
        //  2180: aaload         
        //  2181: iconst_0       
        //  2182: ldc_w           44.0
        //  2185: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2188: ldc_w           2131165667
        //  2191: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2194: aastore        
        //  2195: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2198: iconst_0       
        //  2199: aaload         
        //  2200: iconst_1       
        //  2201: ldc_w           44.0
        //  2204: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2207: ldc_w           2131165667
        //  2210: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2213: aastore        
        //  2214: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2217: iconst_1       
        //  2218: aaload         
        //  2219: iconst_0       
        //  2220: ldc_w           44.0
        //  2223: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2226: ldc_w           2131165666
        //  2229: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2232: aastore        
        //  2233: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2236: iconst_1       
        //  2237: aaload         
        //  2238: iconst_1       
        //  2239: ldc_w           44.0
        //  2242: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2245: ldc_w           2131165666
        //  2248: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2251: aastore        
        //  2252: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2255: iconst_2       
        //  2256: aaload         
        //  2257: iconst_0       
        //  2258: ldc_w           44.0
        //  2261: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2264: ldc_w           2131165665
        //  2267: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2270: aastore        
        //  2271: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2274: iconst_2       
        //  2275: aaload         
        //  2276: iconst_1       
        //  2277: ldc_w           44.0
        //  2280: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2283: ldc_w           2131165665
        //  2286: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2289: aastore        
        //  2290: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2293: iconst_3       
        //  2294: aaload         
        //  2295: iconst_0       
        //  2296: ldc_w           44.0
        //  2299: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2302: ldc_w           2131165663
        //  2305: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2308: aastore        
        //  2309: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2312: iconst_3       
        //  2313: aaload         
        //  2314: iconst_1       
        //  2315: ldc_w           44.0
        //  2318: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2321: ldc_w           2131165663
        //  2324: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2327: aastore        
        //  2328: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2331: iconst_4       
        //  2332: aaload         
        //  2333: iconst_0       
        //  2334: ldc_w           44.0
        //  2337: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2340: ldc_w           2131165662
        //  2343: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2346: aastore        
        //  2347: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2350: iconst_4       
        //  2351: aaload         
        //  2352: iconst_1       
        //  2353: ldc_w           44.0
        //  2356: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2359: ldc_w           2131165662
        //  2362: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2365: aastore        
        //  2366: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2369: iconst_5       
        //  2370: aaload         
        //  2371: iconst_0       
        //  2372: ldc_w           44.0
        //  2375: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2378: ldc_w           2131165667
        //  2381: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2384: aastore        
        //  2385: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2388: iconst_5       
        //  2389: aaload         
        //  2390: iconst_1       
        //  2391: ldc_w           44.0
        //  2394: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2397: ldc_w           2131165667
        //  2400: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2403: aastore        
        //  2404: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2407: bipush          6
        //  2409: aaload         
        //  2410: iconst_0       
        //  2411: ldc_w           44.0
        //  2414: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2417: ldc_w           2131165666
        //  2420: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2423: aastore        
        //  2424: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2427: bipush          6
        //  2429: aaload         
        //  2430: iconst_1       
        //  2431: ldc_w           44.0
        //  2434: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2437: ldc_w           2131165666
        //  2440: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2443: aastore        
        //  2444: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2447: bipush          7
        //  2449: aaload         
        //  2450: iconst_0       
        //  2451: ldc_w           44.0
        //  2454: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2457: ldc_w           2131165665
        //  2460: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2463: aastore        
        //  2464: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2467: bipush          7
        //  2469: aaload         
        //  2470: iconst_1       
        //  2471: ldc_w           44.0
        //  2474: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2477: ldc_w           2131165665
        //  2480: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2483: aastore        
        //  2484: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2487: bipush          8
        //  2489: aaload         
        //  2490: iconst_0       
        //  2491: ldc_w           44.0
        //  2494: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2497: ldc_w           2131165663
        //  2500: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2503: aastore        
        //  2504: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2507: bipush          8
        //  2509: aaload         
        //  2510: iconst_1       
        //  2511: ldc_w           44.0
        //  2514: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2517: ldc_w           2131165663
        //  2520: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2523: aastore        
        //  2524: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2527: bipush          9
        //  2529: aaload         
        //  2530: iconst_0       
        //  2531: ldc_w           44.0
        //  2534: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2537: ldc_w           2131165662
        //  2540: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2543: aastore        
        //  2544: getstatic       org/telegram/ui/ActionBar/Theme.chat_fileStatesDrawable:[[Landroid/graphics/drawable/Drawable;
        //  2547: bipush          9
        //  2549: aaload         
        //  2550: iconst_1       
        //  2551: ldc_w           44.0
        //  2554: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2557: ldc_w           2131165662
        //  2560: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2563: aastore        
        //  2564: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2567: iconst_0       
        //  2568: aaload         
        //  2569: iconst_0       
        //  2570: ldc_w           48.0
        //  2573: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2576: ldc_w           2131165665
        //  2579: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2582: aastore        
        //  2583: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2586: iconst_0       
        //  2587: aaload         
        //  2588: iconst_1       
        //  2589: ldc_w           48.0
        //  2592: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2595: ldc_w           2131165665
        //  2598: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2601: aastore        
        //  2602: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2605: iconst_1       
        //  2606: aaload         
        //  2607: iconst_0       
        //  2608: ldc_w           48.0
        //  2611: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2614: ldc_w           2131165662
        //  2617: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2620: aastore        
        //  2621: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2624: iconst_1       
        //  2625: aaload         
        //  2626: iconst_1       
        //  2627: ldc_w           48.0
        //  2630: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2633: ldc_w           2131165662
        //  2636: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2639: aastore        
        //  2640: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2643: iconst_2       
        //  2644: aaload         
        //  2645: iconst_0       
        //  2646: ldc_w           48.0
        //  2649: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2652: ldc_w           2131165664
        //  2655: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2658: aastore        
        //  2659: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2662: iconst_2       
        //  2663: aaload         
        //  2664: iconst_1       
        //  2665: ldc_w           48.0
        //  2668: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2671: ldc_w           2131165664
        //  2674: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2677: aastore        
        //  2678: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2681: iconst_3       
        //  2682: aaload         
        //  2683: iconst_0       
        //  2684: ldc_w           48.0
        //  2687: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2690: ldc_w           2131165667
        //  2693: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2696: aastore        
        //  2697: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2700: iconst_3       
        //  2701: aaload         
        //  2702: iconst_1       
        //  2703: ldc_w           48.0
        //  2706: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2709: ldc_w           2131165667
        //  2712: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2715: aastore        
        //  2716: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2719: astore          4
        //  2721: aload           4
        //  2723: iconst_4       
        //  2724: aaload         
        //  2725: astore_3       
        //  2726: aload           4
        //  2728: iconst_4       
        //  2729: aaload         
        //  2730: astore          6
        //  2732: aload_2        
        //  2733: ldc_w           2131165329
        //  2736: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2739: astore          4
        //  2741: aload           6
        //  2743: iconst_1       
        //  2744: aload           4
        //  2746: aastore        
        //  2747: aload_3        
        //  2748: iconst_0       
        //  2749: aload           4
        //  2751: aastore        
        //  2752: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2755: astore          4
        //  2757: aload           4
        //  2759: iconst_5       
        //  2760: aaload         
        //  2761: astore_3       
        //  2762: aload           4
        //  2764: iconst_5       
        //  2765: aaload         
        //  2766: astore          6
        //  2768: aload_2        
        //  2769: ldc_w           2131165356
        //  2772: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2775: astore          4
        //  2777: aload           6
        //  2779: iconst_1       
        //  2780: aload           4
        //  2782: aastore        
        //  2783: aload_3        
        //  2784: iconst_0       
        //  2785: aload           4
        //  2787: aastore        
        //  2788: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2791: astore          4
        //  2793: aload           4
        //  2795: bipush          6
        //  2797: aaload         
        //  2798: astore_3       
        //  2799: aload           4
        //  2801: bipush          6
        //  2803: aaload         
        //  2804: astore          6
        //  2806: aload_2        
        //  2807: ldc_w           2131165755
        //  2810: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2813: astore          4
        //  2815: aload           6
        //  2817: iconst_1       
        //  2818: aload           4
        //  2820: aastore        
        //  2821: aload_3        
        //  2822: iconst_0       
        //  2823: aload           4
        //  2825: aastore        
        //  2826: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2829: bipush          7
        //  2831: aaload         
        //  2832: iconst_0       
        //  2833: ldc_w           48.0
        //  2836: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2839: ldc_w           2131165665
        //  2842: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2845: aastore        
        //  2846: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2849: bipush          7
        //  2851: aaload         
        //  2852: iconst_1       
        //  2853: ldc_w           48.0
        //  2856: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2859: ldc_w           2131165665
        //  2862: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2865: aastore        
        //  2866: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2869: bipush          8
        //  2871: aaload         
        //  2872: iconst_0       
        //  2873: ldc_w           48.0
        //  2876: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2879: ldc_w           2131165662
        //  2882: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2885: aastore        
        //  2886: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2889: bipush          8
        //  2891: aaload         
        //  2892: iconst_1       
        //  2893: ldc_w           48.0
        //  2896: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2899: ldc_w           2131165662
        //  2902: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2905: aastore        
        //  2906: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2909: bipush          9
        //  2911: aaload         
        //  2912: iconst_0       
        //  2913: aload_2        
        //  2914: ldc_w           2131165374
        //  2917: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2920: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  2923: aastore        
        //  2924: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2927: bipush          9
        //  2929: aaload         
        //  2930: iconst_1       
        //  2931: aload_2        
        //  2932: ldc_w           2131165374
        //  2935: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  2938: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  2941: aastore        
        //  2942: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2945: bipush          10
        //  2947: aaload         
        //  2948: iconst_0       
        //  2949: ldc_w           48.0
        //  2952: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2955: ldc_w           2131165665
        //  2958: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2961: aastore        
        //  2962: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2965: bipush          10
        //  2967: aaload         
        //  2968: iconst_1       
        //  2969: ldc_w           48.0
        //  2972: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2975: ldc_w           2131165665
        //  2978: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  2981: aastore        
        //  2982: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  2985: bipush          11
        //  2987: aaload         
        //  2988: iconst_0       
        //  2989: ldc_w           48.0
        //  2992: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  2995: ldc_w           2131165662
        //  2998: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  3001: aastore        
        //  3002: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  3005: bipush          11
        //  3007: aaload         
        //  3008: iconst_1       
        //  3009: ldc_w           48.0
        //  3012: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3015: ldc_w           2131165662
        //  3018: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  3021: aastore        
        //  3022: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  3025: bipush          12
        //  3027: aaload         
        //  3028: iconst_0       
        //  3029: aload_2        
        //  3030: ldc_w           2131165374
        //  3033: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  3036: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  3039: aastore        
        //  3040: getstatic       org/telegram/ui/ActionBar/Theme.chat_photoStatesDrawables:[[Landroid/graphics/drawable/Drawable;
        //  3043: bipush          12
        //  3045: aaload         
        //  3046: iconst_1       
        //  3047: aload_2        
        //  3048: ldc_w           2131165374
        //  3051: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  3054: invokevirtual   android/graphics/drawable/Drawable.mutate:()Landroid/graphics/drawable/Drawable;
        //  3057: aastore        
        //  3058: getstatic       org/telegram/ui/ActionBar/Theme.chat_contactDrawable:[Landroid/graphics/drawable/Drawable;
        //  3061: iconst_0       
        //  3062: ldc_w           44.0
        //  3065: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3068: ldc_w           2131165621
        //  3071: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  3074: aastore        
        //  3075: getstatic       org/telegram/ui/ActionBar/Theme.chat_contactDrawable:[Landroid/graphics/drawable/Drawable;
        //  3078: iconst_1       
        //  3079: ldc_w           44.0
        //  3082: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3085: ldc_w           2131165621
        //  3088: invokestatic    org/telegram/ui/ActionBar/Theme.createCircleDrawableWithIcon:(II)Lorg/telegram/ui/Components/CombinedDrawable;
        //  3091: aastore        
        //  3092: getstatic       org/telegram/ui/ActionBar/Theme.chat_locationDrawable:[Landroid/graphics/drawable/Drawable;
        //  3095: iconst_0       
        //  3096: fconst_2       
        //  3097: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3100: ldc_w           2131165641
        //  3103: invokestatic    org/telegram/ui/ActionBar/Theme.createRoundRectDrawableWithIcon:(II)Landroid/graphics/drawable/Drawable;
        //  3106: aastore        
        //  3107: getstatic       org/telegram/ui/ActionBar/Theme.chat_locationDrawable:[Landroid/graphics/drawable/Drawable;
        //  3110: iconst_1       
        //  3111: fconst_2       
        //  3112: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3115: ldc_w           2131165641
        //  3118: invokestatic    org/telegram/ui/ActionBar/Theme.createRoundRectDrawableWithIcon:(II)Landroid/graphics/drawable/Drawable;
        //  3121: aastore        
        //  3122: aload_0        
        //  3123: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //  3126: ldc_w           2131165361
        //  3129: invokevirtual   android/content/res/Resources.getDrawable:(I)Landroid/graphics/drawable/Drawable;
        //  3132: putstatic       org/telegram/ui/ActionBar/Theme.chat_composeShadowDrawable:Landroid/graphics/drawable/Drawable;
        //  3135: getstatic       org/telegram/messenger/AndroidUtilities.roundMessageSize:I
        //  3138: ldc_w           6.0
        //  3141: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3144: iadd           
        //  3145: istore          7
        //  3147: iload           7
        //  3149: iload           7
        //  3151: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
        //  3154: invokestatic    android/graphics/Bitmap.createBitmap:(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
        //  3157: astore          4
        //  3159: new             Landroid/graphics/Canvas;
        //  3162: astore          6
        //  3164: aload           6
        //  3166: aload           4
        //  3168: invokespecial   android/graphics/Canvas.<init>:(Landroid/graphics/Bitmap;)V
        //  3171: new             Landroid/graphics/Paint;
        //  3174: astore_2       
        //  3175: aload_2        
        //  3176: iconst_1       
        //  3177: invokespecial   android/graphics/Paint.<init>:(I)V
        //  3180: aload_2        
        //  3181: iconst_0       
        //  3182: invokevirtual   android/graphics/Paint.setColor:(I)V
        //  3185: aload_2        
        //  3186: getstatic       android/graphics/Paint$Style.FILL:Landroid/graphics/Paint$Style;
        //  3189: invokevirtual   android/graphics/Paint.setStyle:(Landroid/graphics/Paint$Style;)V
        //  3192: new             Landroid/graphics/PorterDuffXfermode;
        //  3195: astore_0       
        //  3196: aload_0        
        //  3197: getstatic       android/graphics/PorterDuff$Mode.CLEAR:Landroid/graphics/PorterDuff$Mode;
        //  3200: invokespecial   android/graphics/PorterDuffXfermode.<init>:(Landroid/graphics/PorterDuff$Mode;)V
        //  3203: aload_2        
        //  3204: aload_0        
        //  3205: invokevirtual   android/graphics/Paint.setXfermode:(Landroid/graphics/Xfermode;)Landroid/graphics/Xfermode;
        //  3208: pop            
        //  3209: new             Landroid/graphics/Paint;
        //  3212: astore_3       
        //  3213: aload_3        
        //  3214: iconst_1       
        //  3215: invokespecial   android/graphics/Paint.<init>:(I)V
        //  3218: aload_3        
        //  3219: ldc_w           4.0
        //  3222: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3225: i2f            
        //  3226: fconst_0       
        //  3227: fconst_0       
        //  3228: ldc_w           1593835520
        //  3231: invokevirtual   android/graphics/Paint.setShadowLayer:(FFFI)V
        //  3234: iload           5
        //  3236: iconst_2       
        //  3237: if_icmpge       3297
        //  3240: iload           7
        //  3242: iconst_2       
        //  3243: idiv           
        //  3244: i2f            
        //  3245: fstore          8
        //  3247: iload           7
        //  3249: iconst_2       
        //  3250: idiv           
        //  3251: i2f            
        //  3252: fstore          9
        //  3254: getstatic       org/telegram/messenger/AndroidUtilities.roundMessageSize:I
        //  3257: iconst_2       
        //  3258: idiv           
        //  3259: fconst_1       
        //  3260: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3263: isub           
        //  3264: i2f            
        //  3265: fstore          10
        //  3267: iload           5
        //  3269: ifne            3277
        //  3272: aload_3        
        //  3273: astore_0       
        //  3274: goto            3279
        //  3277: aload_2        
        //  3278: astore_0       
        //  3279: aload           6
        //  3281: fload           8
        //  3283: fload           9
        //  3285: fload           10
        //  3287: aload_0        
        //  3288: invokevirtual   android/graphics/Canvas.drawCircle:(FFFLandroid/graphics/Paint;)V
        //  3291: iinc            5, 1
        //  3294: goto            3234
        //  3297: aload           6
        //  3299: aconst_null    
        //  3300: invokevirtual   android/graphics/Canvas.setBitmap:(Landroid/graphics/Bitmap;)V
        //  3303: new             Landroid/graphics/drawable/BitmapDrawable;
        //  3306: astore_0       
        //  3307: aload_0        
        //  3308: aload           4
        //  3310: invokespecial   android/graphics/drawable/BitmapDrawable.<init>:(Landroid/graphics/Bitmap;)V
        //  3313: aload_0        
        //  3314: putstatic       org/telegram/ui/ActionBar/Theme.chat_roundVideoShadow:Landroid/graphics/drawable/Drawable;
        //  3317: iload_1        
        //  3318: invokestatic    org/telegram/ui/ActionBar/Theme.applyChatTheme:(Z)V
        //  3321: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaintOneEmoji:Landroid/text/TextPaint;
        //  3324: ldc_w           28.0
        //  3327: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3330: i2f            
        //  3331: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3334: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaintTwoEmoji:Landroid/text/TextPaint;
        //  3337: ldc_w           24.0
        //  3340: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3343: i2f            
        //  3344: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3347: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaintThreeEmoji:Landroid/text/TextPaint;
        //  3350: ldc_w           20.0
        //  3353: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3356: i2f            
        //  3357: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3360: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgTextPaint:Landroid/text/TextPaint;
        //  3363: getstatic       org/telegram/messenger/SharedConfig.fontSize:I
        //  3366: i2f            
        //  3367: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3370: i2f            
        //  3371: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3374: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgGameTextPaint:Landroid/text/TextPaint;
        //  3377: ldc_w           14.0
        //  3380: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3383: i2f            
        //  3384: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3387: getstatic       org/telegram/ui/ActionBar/Theme.chat_msgBotButtonPaint:Landroid/text/TextPaint;
        //  3390: ldc_w           15.0
        //  3393: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3396: i2f            
        //  3397: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3400: iload_1        
        //  3401: ifne            3787
        //  3404: getstatic       org/telegram/ui/ActionBar/Theme.chat_botProgressPaint:Landroid/graphics/Paint;
        //  3407: astore_0       
        //  3408: aload_0        
        //  3409: ifnull          3787
        //  3412: aload_0        
        //  3413: fconst_2       
        //  3414: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3417: i2f            
        //  3418: invokevirtual   android/graphics/Paint.setStrokeWidth:(F)V
        //  3421: getstatic       org/telegram/ui/ActionBar/Theme.chat_infoPaint:Landroid/text/TextPaint;
        //  3424: ldc_w           12.0
        //  3427: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3430: i2f            
        //  3431: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3434: getstatic       org/telegram/ui/ActionBar/Theme.chat_docNamePaint:Landroid/text/TextPaint;
        //  3437: ldc_w           15.0
        //  3440: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3443: i2f            
        //  3444: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3447: getstatic       org/telegram/ui/ActionBar/Theme.chat_locationTitlePaint:Landroid/text/TextPaint;
        //  3450: ldc_w           15.0
        //  3453: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3456: i2f            
        //  3457: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3460: getstatic       org/telegram/ui/ActionBar/Theme.chat_locationAddressPaint:Landroid/text/TextPaint;
        //  3463: ldc_w           13.0
        //  3466: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3469: i2f            
        //  3470: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3473: getstatic       org/telegram/ui/ActionBar/Theme.chat_audioTimePaint:Landroid/text/TextPaint;
        //  3476: ldc_w           12.0
        //  3479: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3482: i2f            
        //  3483: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3486: getstatic       org/telegram/ui/ActionBar/Theme.chat_livePaint:Landroid/text/TextPaint;
        //  3489: ldc_w           12.0
        //  3492: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3495: i2f            
        //  3496: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3499: getstatic       org/telegram/ui/ActionBar/Theme.chat_audioTitlePaint:Landroid/text/TextPaint;
        //  3502: ldc_w           16.0
        //  3505: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3508: i2f            
        //  3509: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3512: getstatic       org/telegram/ui/ActionBar/Theme.chat_audioPerformerPaint:Landroid/text/TextPaint;
        //  3515: ldc_w           15.0
        //  3518: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3521: i2f            
        //  3522: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3525: getstatic       org/telegram/ui/ActionBar/Theme.chat_botButtonPaint:Landroid/text/TextPaint;
        //  3528: ldc_w           15.0
        //  3531: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3534: i2f            
        //  3535: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3538: getstatic       org/telegram/ui/ActionBar/Theme.chat_contactNamePaint:Landroid/text/TextPaint;
        //  3541: ldc_w           15.0
        //  3544: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3547: i2f            
        //  3548: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3551: getstatic       org/telegram/ui/ActionBar/Theme.chat_contactPhonePaint:Landroid/text/TextPaint;
        //  3554: ldc_w           13.0
        //  3557: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3560: i2f            
        //  3561: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3564: getstatic       org/telegram/ui/ActionBar/Theme.chat_durationPaint:Landroid/text/TextPaint;
        //  3567: ldc_w           12.0
        //  3570: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3573: i2f            
        //  3574: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3577: getstatic       org/telegram/ui/ActionBar/Theme.chat_timePaint:Landroid/text/TextPaint;
        //  3580: ldc_w           12.0
        //  3583: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3586: i2f            
        //  3587: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3590: getstatic       org/telegram/ui/ActionBar/Theme.chat_adminPaint:Landroid/text/TextPaint;
        //  3593: ldc_w           13.0
        //  3596: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3599: i2f            
        //  3600: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3603: getstatic       org/telegram/ui/ActionBar/Theme.chat_namePaint:Landroid/text/TextPaint;
        //  3606: ldc_w           14.0
        //  3609: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3612: i2f            
        //  3613: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3616: getstatic       org/telegram/ui/ActionBar/Theme.chat_forwardNamePaint:Landroid/text/TextPaint;
        //  3619: ldc_w           14.0
        //  3622: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3625: i2f            
        //  3626: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3629: getstatic       org/telegram/ui/ActionBar/Theme.chat_replyNamePaint:Landroid/text/TextPaint;
        //  3632: ldc_w           14.0
        //  3635: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3638: i2f            
        //  3639: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3642: getstatic       org/telegram/ui/ActionBar/Theme.chat_replyTextPaint:Landroid/text/TextPaint;
        //  3645: ldc_w           14.0
        //  3648: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3651: i2f            
        //  3652: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3655: getstatic       org/telegram/ui/ActionBar/Theme.chat_gamePaint:Landroid/text/TextPaint;
        //  3658: ldc_w           13.0
        //  3661: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3664: i2f            
        //  3665: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3668: getstatic       org/telegram/ui/ActionBar/Theme.chat_shipmentPaint:Landroid/text/TextPaint;
        //  3671: ldc_w           13.0
        //  3674: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3677: i2f            
        //  3678: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3681: getstatic       org/telegram/ui/ActionBar/Theme.chat_instantViewPaint:Landroid/text/TextPaint;
        //  3684: ldc_w           13.0
        //  3687: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3690: i2f            
        //  3691: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3694: getstatic       org/telegram/ui/ActionBar/Theme.chat_instantViewRectPaint:Landroid/graphics/Paint;
        //  3697: fconst_1       
        //  3698: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3701: i2f            
        //  3702: invokevirtual   android/graphics/Paint.setStrokeWidth:(F)V
        //  3705: getstatic       org/telegram/ui/ActionBar/Theme.chat_statusRecordPaint:Landroid/graphics/Paint;
        //  3708: fconst_2       
        //  3709: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3712: i2f            
        //  3713: invokevirtual   android/graphics/Paint.setStrokeWidth:(F)V
        //  3716: getstatic       org/telegram/ui/ActionBar/Theme.chat_actionTextPaint:Landroid/text/TextPaint;
        //  3719: bipush          16
        //  3721: getstatic       org/telegram/messenger/SharedConfig.fontSize:I
        //  3724: invokestatic    java/lang/Math.max:(II)I
        //  3727: iconst_2       
        //  3728: isub           
        //  3729: i2f            
        //  3730: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3733: i2f            
        //  3734: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3737: getstatic       org/telegram/ui/ActionBar/Theme.chat_contextResult_titleTextPaint:Landroid/text/TextPaint;
        //  3740: ldc_w           15.0
        //  3743: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3746: i2f            
        //  3747: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3750: getstatic       org/telegram/ui/ActionBar/Theme.chat_contextResult_descriptionTextPaint:Landroid/text/TextPaint;
        //  3753: ldc_w           13.0
        //  3756: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3759: i2f            
        //  3760: invokevirtual   android/text/TextPaint.setTextSize:(F)V
        //  3763: getstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgressPaint:Landroid/graphics/Paint;
        //  3766: ldc_w           3.0
        //  3769: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3772: i2f            
        //  3773: invokevirtual   android/graphics/Paint.setStrokeWidth:(F)V
        //  3776: getstatic       org/telegram/ui/ActionBar/Theme.chat_radialProgress2Paint:Landroid/graphics/Paint;
        //  3779: fconst_2       
        //  3780: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3783: i2f            
        //  3784: invokevirtual   android/graphics/Paint.setStrokeWidth:(F)V
        //  3787: return         
        //  3788: astore_0       
        //  3789: aload_2        
        //  3790: monitorexit    
        //  3791: aload_0        
        //  3792: athrow         
        //  3793: astore_0       
        //  3794: goto            3317
        //  3797: astore_0       
        //  3798: goto            3303
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      103    3788   3793   Any
        //  103    105    3788   3793   Any
        //  3135   3234   3793   3797   Ljava/lang/Throwable;
        //  3240   3267   3793   3797   Ljava/lang/Throwable;
        //  3279   3291   3793   3797   Ljava/lang/Throwable;
        //  3297   3303   3797   3801   Ljava/lang/Exception;
        //  3297   3303   3793   3797   Ljava/lang/Throwable;
        //  3303   3317   3793   3797   Ljava/lang/Throwable;
        //  3789   3791   3788   3793   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 1669 out-of-bounds for length 1669
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Drawable createCircleDrawable(final int n, final int color) {
        final OvalShape ovalShape = new OvalShape();
        final float n2 = (float)n;
        ovalShape.resize(n2, n2);
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)ovalShape);
        shapeDrawable.getPaint().setColor(color);
        return (Drawable)shapeDrawable;
    }
    
    public static CombinedDrawable createCircleDrawableWithIcon(final int n, final int n2) {
        return createCircleDrawableWithIcon(n, n2, 0);
    }
    
    public static CombinedDrawable createCircleDrawableWithIcon(final int n, final int n2, final int n3) {
        Drawable mutate;
        if (n2 != 0) {
            mutate = ApplicationLoader.applicationContext.getResources().getDrawable(n2).mutate();
        }
        else {
            mutate = null;
        }
        return createCircleDrawableWithIcon(n, mutate, n3);
    }
    
    public static CombinedDrawable createCircleDrawableWithIcon(final int n, final Drawable drawable, final int n2) {
        final OvalShape ovalShape = new OvalShape();
        final float n3 = (float)n;
        ovalShape.resize(n3, n3);
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)ovalShape);
        final Paint paint = shapeDrawable.getPaint();
        paint.setColor(-1);
        if (n2 == 1) {
            paint.setStyle(Paint$Style.STROKE);
            paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        }
        else if (n2 == 2) {
            paint.setAlpha(0);
        }
        final CombinedDrawable combinedDrawable = new CombinedDrawable((Drawable)shapeDrawable, drawable);
        combinedDrawable.setCustomSize(n, n);
        return combinedDrawable;
    }
    
    public static void createCommonResources(final Context context) {
        if (Theme.dividerPaint == null) {
            (Theme.dividerPaint = new Paint()).setStrokeWidth(1.0f);
            Theme.avatar_backgroundPaint = new Paint(1);
            (Theme.checkboxSquare_checkPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
            Theme.checkboxSquare_checkPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
            (Theme.checkboxSquare_eraserPaint = new Paint(1)).setColor(0);
            Theme.checkboxSquare_eraserPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            Theme.checkboxSquare_backgroundPaint = new Paint(1);
            Theme.linkSelectionPaint = new Paint();
            final Resources resources = context.getResources();
            Theme.avatar_broadcastDrawable = resources.getDrawable(2131165326);
            Theme.avatar_savedDrawable = resources.getDrawable(2131165351);
            (Theme.dialogs_archiveAvatarDrawable = new LottieDrawable()).setComposition(LottieCompositionFactory.fromRawResSync(context, 2131492865).getValue());
            if (Build$VERSION.SDK_INT == 24) {
                Theme.dialogs_archiveDrawable = resources.getDrawable(2131165341);
                Theme.dialogs_unarchiveDrawable = resources.getDrawable(2131165352);
                Theme.dialogs_pinArchiveDrawable = resources.getDrawable(2131165344);
                Theme.dialogs_unpinArchiveDrawable = resources.getDrawable(2131165347);
            }
            else {
                final LottieDrawable dialogs_archiveDrawable = new LottieDrawable();
                dialogs_archiveDrawable.setComposition(LottieCompositionFactory.fromRawResSync(context, 2131492864).getValue());
                Theme.dialogs_archiveDrawable = dialogs_archiveDrawable;
                final LottieDrawable dialogs_unarchiveDrawable = new LottieDrawable();
                dialogs_unarchiveDrawable.setComposition(LottieCompositionFactory.fromRawResSync(context, 2131492870).getValue());
                Theme.dialogs_unarchiveDrawable = dialogs_unarchiveDrawable;
                final LottieDrawable dialogs_pinArchiveDrawable = new LottieDrawable();
                dialogs_pinArchiveDrawable.setComposition(LottieCompositionFactory.fromRawResSync(context, 2131492867).getValue());
                Theme.dialogs_pinArchiveDrawable = dialogs_pinArchiveDrawable;
                final LottieDrawable dialogs_unpinArchiveDrawable = new LottieDrawable();
                dialogs_unpinArchiveDrawable.setComposition(LottieCompositionFactory.fromRawResSync(context, 2131492871).getValue());
                Theme.dialogs_unpinArchiveDrawable = dialogs_unpinArchiveDrawable;
            }
            applyCommonTheme();
        }
    }
    
    public static void createDialogsResources(final Context context) {
        createCommonResources(context);
        if (Theme.dialogs_namePaint == null) {
            final Resources resources = context.getResources();
            (Theme.dialogs_namePaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            (Theme.dialogs_nameEncryptedPaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            (Theme.dialogs_searchNamePaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            (Theme.dialogs_searchNameEncryptedPaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            Theme.dialogs_messagePaint = new TextPaint(1);
            (Theme.dialogs_messageNamePaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            Theme.dialogs_messagePrintingPaint = new TextPaint(1);
            Theme.dialogs_timePaint = new TextPaint(1);
            (Theme.dialogs_countTextPaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            (Theme.dialogs_archiveTextPaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            Theme.dialogs_onlinePaint = new TextPaint(1);
            Theme.dialogs_offlinePaint = new TextPaint(1);
            Theme.dialogs_tabletSeletedPaint = new Paint();
            Theme.dialogs_pinnedPaint = new Paint(1);
            Theme.dialogs_onlineCirclePaint = new Paint(1);
            Theme.dialogs_countPaint = new Paint(1);
            Theme.dialogs_countGrayPaint = new Paint(1);
            Theme.dialogs_errorPaint = new Paint(1);
            Theme.dialogs_lockDrawable = resources.getDrawable(2131165531);
            Theme.dialogs_checkDrawable = resources.getDrawable(2131165524);
            Theme.dialogs_halfCheckDrawable = resources.getDrawable(2131165527);
            Theme.dialogs_clockDrawable = resources.getDrawable(2131165620).mutate();
            Theme.dialogs_errorDrawable = resources.getDrawable(2131165533);
            Theme.dialogs_reorderDrawable = resources.getDrawable(2131165530);
            Theme.dialogs_groupDrawable = resources.getDrawable(2131165526);
            Theme.dialogs_broadcastDrawable = resources.getDrawable(2131165523);
            Theme.dialogs_muteDrawable = resources.getDrawable(2131165528).mutate();
            Theme.dialogs_verifiedDrawable = resources.getDrawable(2131165893);
            Theme.dialogs_scamDrawable = new ScamDrawable(11);
            Theme.dialogs_verifiedCheckDrawable = resources.getDrawable(2131165894);
            Theme.dialogs_mentionDrawable = resources.getDrawable(2131165565);
            Theme.dialogs_botDrawable = resources.getDrawable(2131165522);
            Theme.dialogs_pinnedDrawable = resources.getDrawable(2131165529);
            Theme.moveUpDrawable = resources.getDrawable(2131165780);
            applyDialogsTheme();
        }
        Theme.dialogs_messageNamePaint.setTextSize((float)AndroidUtilities.dp(14.0f));
        Theme.dialogs_timePaint.setTextSize((float)AndroidUtilities.dp(13.0f));
        Theme.dialogs_countTextPaint.setTextSize((float)AndroidUtilities.dp(13.0f));
        Theme.dialogs_archiveTextPaint.setTextSize((float)AndroidUtilities.dp(13.0f));
        Theme.dialogs_onlinePaint.setTextSize((float)AndroidUtilities.dp(15.0f));
        Theme.dialogs_offlinePaint.setTextSize((float)AndroidUtilities.dp(15.0f));
        Theme.dialogs_searchNamePaint.setTextSize((float)AndroidUtilities.dp(16.0f));
        Theme.dialogs_searchNameEncryptedPaint.setTextSize((float)AndroidUtilities.dp(16.0f));
    }
    
    public static Drawable createEditTextDrawable(final Context context, final boolean b) {
        final Resources resources = context.getResources();
        final Drawable mutate = resources.getDrawable(2131165810).mutate();
        String s;
        if (b) {
            s = "dialogInputField";
        }
        else {
            s = "windowBackgroundWhiteInputField";
        }
        mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(getColor(s), PorterDuff$Mode.MULTIPLY));
        final Drawable mutate2 = resources.getDrawable(2131165811).mutate();
        String s2;
        if (b) {
            s2 = "dialogInputFieldActivated";
        }
        else {
            s2 = "windowBackgroundWhiteInputFieldActivated";
        }
        mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(getColor(s2), PorterDuff$Mode.MULTIPLY));
        final StateListDrawable stateListDrawable = new StateListDrawable() {
            public boolean selectDrawable(final int n) {
                if (Build$VERSION.SDK_INT < 21) {
                    final Drawable access$300 = getStateDrawable((Drawable)this, n);
                    ColorFilter colorFilter = null;
                    if (access$300 instanceof BitmapDrawable) {
                        colorFilter = ((BitmapDrawable)access$300).getPaint().getColorFilter();
                    }
                    else if (access$300 instanceof NinePatchDrawable) {
                        colorFilter = ((NinePatchDrawable)access$300).getPaint().getColorFilter();
                    }
                    final boolean selectDrawable = super.selectDrawable(n);
                    if (colorFilter != null) {
                        access$300.setColorFilter(colorFilter);
                    }
                    return selectDrawable;
                }
                return super.selectDrawable(n);
            }
        };
        stateListDrawable.addState(new int[] { 16842910, 16842908 }, mutate2);
        stateListDrawable.addState(new int[] { 16842908 }, mutate2);
        stateListDrawable.addState(StateSet.WILD_CARD, mutate);
        return (Drawable)stateListDrawable;
    }
    
    public static Drawable createEmojiIconSelectorDrawable(final Context context, final int n, final int n2, final int n3) {
        final Resources resources = context.getResources();
        final Drawable mutate = resources.getDrawable(n).mutate();
        if (n2 != 0) {
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(n2, PorterDuff$Mode.MULTIPLY));
        }
        final Drawable mutate2 = resources.getDrawable(n).mutate();
        if (n3 != 0) {
            mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(n3, PorterDuff$Mode.MULTIPLY));
        }
        final StateListDrawable stateListDrawable = new StateListDrawable() {
            public boolean selectDrawable(final int n) {
                if (Build$VERSION.SDK_INT < 21) {
                    final Drawable access$300 = getStateDrawable((Drawable)this, n);
                    ColorFilter colorFilter = null;
                    if (access$300 instanceof BitmapDrawable) {
                        colorFilter = ((BitmapDrawable)access$300).getPaint().getColorFilter();
                    }
                    else if (access$300 instanceof NinePatchDrawable) {
                        colorFilter = ((NinePatchDrawable)access$300).getPaint().getColorFilter();
                    }
                    final boolean selectDrawable = super.selectDrawable(n);
                    if (colorFilter != null) {
                        access$300.setColorFilter(colorFilter);
                    }
                    return selectDrawable;
                }
                return super.selectDrawable(n);
            }
        };
        stateListDrawable.setEnterFadeDuration(1);
        stateListDrawable.setExitFadeDuration(200);
        stateListDrawable.addState(new int[] { 16842913 }, mutate2);
        stateListDrawable.addState(new int[0], mutate);
        return (Drawable)stateListDrawable;
    }
    
    public static void createProfileResources(final Context context) {
        if (Theme.profile_verifiedDrawable == null) {
            Theme.profile_aboutTextPaint = new TextPaint(1);
            final Resources resources = context.getResources();
            Theme.profile_verifiedDrawable = resources.getDrawable(2131165893).mutate();
            Theme.profile_verifiedCheckDrawable = resources.getDrawable(2131165894).mutate();
            applyProfileTheme();
        }
        Theme.profile_aboutTextPaint.setTextSize((float)AndroidUtilities.dp(16.0f));
    }
    
    public static Drawable createRoundRectDrawable(final int n, final int color) {
        final float n2 = (float)n;
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)new RoundRectShape(new float[] { n2, n2, n2, n2, n2, n2, n2, n2 }, (RectF)null, (float[])null));
        shapeDrawable.getPaint().setColor(color);
        return (Drawable)shapeDrawable;
    }
    
    public static Drawable createRoundRectDrawableWithIcon(final int n, final int n2) {
        final float n3 = (float)n;
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)new RoundRectShape(new float[] { n3, n3, n3, n3, n3, n3, n3, n3 }, (RectF)null, (float[])null));
        shapeDrawable.getPaint().setColor(-1);
        return new CombinedDrawable((Drawable)shapeDrawable, ApplicationLoader.applicationContext.getResources().getDrawable(n2).mutate());
    }
    
    public static Drawable createSelectorDrawable(final int n) {
        return createSelectorDrawable(n, 1);
    }
    
    public static Drawable createSelectorDrawable(final int n, final int n2) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 21) {
            Object o = null;
            Label_0077: {
                if (n2 != 1 || sdk_INT < 23) {
                    if (n2 == 1 || n2 == 3 || n2 == 4) {
                        Theme.maskPaint.setColor(-1);
                        o = new Drawable() {
                            public void draw(final Canvas canvas) {
                                final Rect bounds = this.getBounds();
                                final int val$maskType = n2;
                                int dp;
                                if (val$maskType == 1) {
                                    dp = AndroidUtilities.dp(20.0f);
                                }
                                else if (val$maskType == 3) {
                                    dp = Math.max(bounds.width(), bounds.height()) / 2;
                                }
                                else {
                                    dp = (int)Math.ceil(Math.sqrt((bounds.left - bounds.centerX()) * (bounds.left - bounds.centerX()) + (bounds.top - bounds.centerY()) * (bounds.top - bounds.centerY())));
                                }
                                canvas.drawCircle((float)bounds.centerX(), (float)bounds.centerY(), (float)dp, Theme.maskPaint);
                            }
                            
                            public int getOpacity() {
                                return 0;
                            }
                            
                            public void setAlpha(final int n) {
                            }
                            
                            public void setColorFilter(final ColorFilter colorFilter) {
                            }
                        };
                        break Label_0077;
                    }
                    if (n2 == 2) {
                        o = new ColorDrawable(-1);
                        break Label_0077;
                    }
                }
                o = null;
            }
            final RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { n }), (Drawable)null, (Drawable)o);
            if (n2 == 1 && Build$VERSION.SDK_INT >= 23) {
                rippleDrawable.setRadius(AndroidUtilities.dp(20.0f));
            }
            return (Drawable)rippleDrawable;
        }
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { 16842919 }, (Drawable)new ColorDrawable(n));
        stateListDrawable.addState(new int[] { 16842913 }, (Drawable)new ColorDrawable(n));
        stateListDrawable.addState(StateSet.WILD_CARD, (Drawable)new ColorDrawable(0));
        return (Drawable)stateListDrawable;
    }
    
    public static Drawable createSelectorWithBackgroundDrawable(final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            return (Drawable)new RippleDrawable(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { n2 }), (Drawable)new ColorDrawable(n), (Drawable)new ColorDrawable(n));
        }
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { 16842919 }, (Drawable)new ColorDrawable(n2));
        stateListDrawable.addState(new int[] { 16842913 }, (Drawable)new ColorDrawable(n2));
        stateListDrawable.addState(StateSet.WILD_CARD, (Drawable)new ColorDrawable(n));
        return (Drawable)stateListDrawable;
    }
    
    public static Drawable createSimpleSelectorCircleDrawable(final int n, final int color, final int color2) {
        final OvalShape ovalShape = new OvalShape();
        final float n2 = (float)n;
        ovalShape.resize(n2, n2);
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)ovalShape);
        shapeDrawable.getPaint().setColor(color);
        final ShapeDrawable shapeDrawable2 = new ShapeDrawable((Shape)ovalShape);
        if (Build$VERSION.SDK_INT >= 21) {
            shapeDrawable2.getPaint().setColor(-1);
            return (Drawable)new RippleDrawable(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { color2 }), (Drawable)shapeDrawable, (Drawable)shapeDrawable2);
        }
        shapeDrawable2.getPaint().setColor(color2);
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { 16842919 }, (Drawable)shapeDrawable2);
        stateListDrawable.addState(new int[] { 16842908 }, (Drawable)shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, (Drawable)shapeDrawable);
        return (Drawable)stateListDrawable;
    }
    
    public static Drawable createSimpleSelectorDrawable(final Context context, final int n, final int n2, final int n3) {
        final Resources resources = context.getResources();
        final Drawable mutate = resources.getDrawable(n).mutate();
        if (n2 != 0) {
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(n2, PorterDuff$Mode.MULTIPLY));
        }
        final Drawable mutate2 = resources.getDrawable(n).mutate();
        if (n3 != 0) {
            mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(n3, PorterDuff$Mode.MULTIPLY));
        }
        final StateListDrawable stateListDrawable = new StateListDrawable() {
            public boolean selectDrawable(final int n) {
                if (Build$VERSION.SDK_INT < 21) {
                    final Drawable access$300 = getStateDrawable((Drawable)this, n);
                    ColorFilter colorFilter = null;
                    if (access$300 instanceof BitmapDrawable) {
                        colorFilter = ((BitmapDrawable)access$300).getPaint().getColorFilter();
                    }
                    else if (access$300 instanceof NinePatchDrawable) {
                        colorFilter = ((NinePatchDrawable)access$300).getPaint().getColorFilter();
                    }
                    final boolean selectDrawable = super.selectDrawable(n);
                    if (colorFilter != null) {
                        access$300.setColorFilter(colorFilter);
                    }
                    return selectDrawable;
                }
                return super.selectDrawable(n);
            }
        };
        stateListDrawable.addState(new int[] { 16842919 }, mutate2);
        stateListDrawable.addState(new int[] { 16842913 }, mutate2);
        stateListDrawable.addState(StateSet.WILD_CARD, mutate);
        return (Drawable)stateListDrawable;
    }
    
    public static Drawable createSimpleSelectorRoundRectDrawable(final int n, final int color, final int color2) {
        final float n2 = (float)n;
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)new RoundRectShape(new float[] { n2, n2, n2, n2, n2, n2, n2, n2 }, (RectF)null, (float[])null));
        shapeDrawable.getPaint().setColor(color);
        final ShapeDrawable shapeDrawable2 = new ShapeDrawable((Shape)new RoundRectShape(new float[] { n2, n2, n2, n2, n2, n2, n2, n2 }, (RectF)null, (float[])null));
        shapeDrawable2.getPaint().setColor(color2);
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { 16842919 }, (Drawable)shapeDrawable2);
        stateListDrawable.addState(new int[] { 16842913 }, (Drawable)shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, (Drawable)shapeDrawable);
        return (Drawable)stateListDrawable;
    }
    
    public static boolean deleteTheme(final ThemeInfo themeInfo) {
        final String pathToFile = themeInfo.pathToFile;
        boolean b = false;
        if (pathToFile == null) {
            return false;
        }
        if (Theme.currentTheme == themeInfo) {
            applyTheme(Theme.defaultTheme, true, false, false);
            b = true;
        }
        Theme.otherThemes.remove(themeInfo);
        Theme.themesDict.remove(themeInfo.name);
        Theme.themes.remove(themeInfo);
        new File(themeInfo.pathToFile).delete();
        saveOtherThemes();
        return b;
    }
    
    public static void destroyResources() {
        int n = 0;
        while (true) {
            final Drawable[] chat_attachButtonDrawables = Theme.chat_attachButtonDrawables;
            if (n >= chat_attachButtonDrawables.length) {
                break;
            }
            if (chat_attachButtonDrawables[n] != null) {
                chat_attachButtonDrawables[n].setCallback((Drawable$Callback)null);
            }
            ++n;
        }
    }
    
    public static File getAssetFile(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokestatic    org/telegram/messenger/ApplicationLoader.getFilesDirFixed:()Ljava/io/File;
        //     7: aload_0        
        //     8: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    11: astore_1       
        //    12: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    15: invokevirtual   android/content/Context.getAssets:()Landroid/content/res/AssetManager;
        //    18: aload_0        
        //    19: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;)Ljava/io/InputStream;
        //    22: astore_2       
        //    23: aload_2        
        //    24: invokevirtual   java/io/InputStream.available:()I
        //    27: i2l            
        //    28: lstore_3       
        //    29: aload_2        
        //    30: invokevirtual   java/io/InputStream.close:()V
        //    33: goto            43
        //    36: astore_2       
        //    37: aload_2        
        //    38: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //    41: lconst_0       
        //    42: lstore_3       
        //    43: aload_1        
        //    44: invokevirtual   java/io/File.exists:()Z
        //    47: ifeq            65
        //    50: lload_3        
        //    51: lconst_0       
        //    52: lcmp           
        //    53: ifeq            137
        //    56: aload_1        
        //    57: invokevirtual   java/io/File.length:()J
        //    60: lload_3        
        //    61: lcmp           
        //    62: ifeq            137
        //    65: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    68: invokevirtual   android/content/Context.getAssets:()Landroid/content/res/AssetManager;
        //    71: aload_0        
        //    72: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;)Ljava/io/InputStream;
        //    75: astore          5
        //    77: aconst_null    
        //    78: astore_0       
        //    79: aload           5
        //    81: aload_1        
        //    82: invokestatic    org/telegram/messenger/AndroidUtilities.copyFile:(Ljava/io/InputStream;Ljava/io/File;)Z
        //    85: pop            
        //    86: aload           5
        //    88: ifnull          137
        //    91: aload           5
        //    93: invokevirtual   java/io/InputStream.close:()V
        //    96: goto            137
        //    99: astore_2       
        //   100: goto            108
        //   103: astore_2       
        //   104: aload_2        
        //   105: astore_0       
        //   106: aload_2        
        //   107: athrow         
        //   108: aload           5
        //   110: ifnull          130
        //   113: aload_0        
        //   114: ifnull          125
        //   117: aload           5
        //   119: invokevirtual   java/io/InputStream.close:()V
        //   122: goto            130
        //   125: aload           5
        //   127: invokevirtual   java/io/InputStream.close:()V
        //   130: aload_2        
        //   131: athrow         
        //   132: astore_0       
        //   133: aload_0        
        //   134: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   137: aload_1        
        //   138: areturn        
        //   139: astore_0       
        //   140: goto            130
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  12     33     36     43     Ljava/lang/Exception;
        //  65     77     132    137    Ljava/lang/Exception;
        //  79     86     103    108    Ljava/lang/Throwable;
        //  79     86     99     132    Any
        //  91     96     132    137    Ljava/lang/Exception;
        //  106    108    99     132    Any
        //  117    122    139    143    Ljava/lang/Throwable;
        //  125    130    132    137    Ljava/lang/Exception;
        //  130    132    132    137    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0125:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static long getAutoNightSwitchThemeDelay() {
        if (Math.abs(Theme.lastThemeSwitchTime - SystemClock.elapsedRealtime()) >= 12000L) {
            return 1800L;
        }
        return 12000L;
    }
    
    public static Drawable getCachedWallpaper() {
        synchronized (Theme.wallpaperSync) {
            if (Theme.themedWallpaper != null) {
                return Theme.themedWallpaper;
            }
            return Theme.wallpaper;
        }
    }
    
    public static Drawable getCachedWallpaperNonBlocking() {
        final Drawable themedWallpaper = Theme.themedWallpaper;
        if (themedWallpaper != null) {
            return themedWallpaper;
        }
        return Theme.wallpaper;
    }
    
    public static int getColor(final String s) {
        return getColor(s, null);
    }
    
    public static int getColor(final String key, final boolean[] array) {
        final HashMap<String, Integer> animatingColors = Theme.animatingColors;
        if (animatingColors != null) {
            final Integer n = animatingColors.get(key);
            if (n != null) {
                return n;
            }
        }
        if (!isCurrentThemeDefault()) {
            Integer n2 = Theme.currentColors.get(key);
            Integer n3;
            if ((n3 = n2) == null) {
                final String key2 = Theme.fallbackKeys.get(key);
                if (key2 != null) {
                    n2 = Theme.currentColors.get(key2);
                }
                if ((n3 = n2) == null) {
                    if (array != null) {
                        array[0] = true;
                    }
                    if (key.equals("chat_serviceBackground")) {
                        return Theme.serviceMessageColor;
                    }
                    if (key.equals("chat_serviceBackgroundSelected")) {
                        return Theme.serviceSelectedMessageColor;
                    }
                    return getDefaultColor(key);
                }
            }
            return n3;
        }
        if (key.equals("chat_serviceBackground")) {
            return Theme.serviceMessageColor;
        }
        if (key.equals("chat_serviceBackgroundSelected")) {
            return Theme.serviceSelectedMessageColor;
        }
        return getDefaultColor(key);
    }
    
    public static Integer getColorOrNull(final String s) {
        Integer n2;
        Integer n = n2 = Theme.currentColors.get(s);
        if (n == null) {
            if (Theme.fallbackKeys.get(s) != null) {
                n = Theme.currentColors.get(s);
            }
            if ((n2 = n) == null) {
                n2 = Theme.defaultColors.get(s);
            }
        }
        return n2;
    }
    
    public static Drawable getCurrentHolidayDrawable() {
        if (System.currentTimeMillis() - Theme.lastHolidayCheckTime >= 60000L) {
            Theme.lastHolidayCheckTime = System.currentTimeMillis();
            final Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(System.currentTimeMillis());
            final int value = instance.get(2);
            final int value2 = instance.get(5);
            final int value3 = instance.get(12);
            final int value4 = instance.get(11);
            if (value == 0 && value2 == 1 && value3 <= 10 && value4 == 0) {
                Theme.canStartHolidayAnimation = true;
            }
            else {
                Theme.canStartHolidayAnimation = false;
            }
            if (Theme.dialogs_holidayDrawable == null) {
                Label_0139: {
                    if (value == 11) {
                        int n;
                        if (BuildVars.DEBUG_PRIVATE_VERSION) {
                            n = 29;
                        }
                        else {
                            n = 31;
                        }
                        if (value2 >= n && value2 <= 31) {
                            break Label_0139;
                        }
                    }
                    if (value != 0 || value2 != 1) {
                        return Theme.dialogs_holidayDrawable;
                    }
                }
                Theme.dialogs_holidayDrawable = ApplicationLoader.applicationContext.getResources().getDrawable(2131165689);
                Theme.dialogs_holidayDrawableOffsetX = -AndroidUtilities.dp(3.0f);
                Theme.dialogs_holidayDrawableOffsetY = -AndroidUtilities.dp(1.0f);
            }
        }
        return Theme.dialogs_holidayDrawable;
    }
    
    public static int getCurrentHolidayDrawableXOffset() {
        return Theme.dialogs_holidayDrawableOffsetX;
    }
    
    public static int getCurrentHolidayDrawableYOffset() {
        return Theme.dialogs_holidayDrawableOffsetY;
    }
    
    public static ThemeInfo getCurrentNightTheme() {
        return Theme.currentNightTheme;
    }
    
    public static String getCurrentNightThemeName() {
        final ThemeInfo currentNightTheme = Theme.currentNightTheme;
        if (currentNightTheme == null) {
            return "";
        }
        String s2;
        final String s = s2 = currentNightTheme.getName();
        if (s.toLowerCase().endsWith(".attheme")) {
            s2 = s.substring(0, s.lastIndexOf(46));
        }
        return s2;
    }
    
    public static ThemeInfo getCurrentTheme() {
        ThemeInfo themeInfo = Theme.currentDayTheme;
        if (themeInfo == null) {
            themeInfo = Theme.defaultTheme;
        }
        return themeInfo;
    }
    
    public static String getCurrentThemeName() {
        String s2;
        final String s = s2 = Theme.currentDayTheme.getName();
        if (s.toLowerCase().endsWith(".attheme")) {
            s2 = s.substring(0, s.lastIndexOf(46));
        }
        return s2;
    }
    
    public static int getDefaultColor(final String key) {
        final Integer n = Theme.defaultColors.get(key);
        if (n != null) {
            return n;
        }
        if (key.equals("chats_menuTopShadow")) {
            return 0;
        }
        return -65536;
    }
    
    public static HashMap<String, Integer> getDefaultColors() {
        return Theme.defaultColors;
    }
    
    public static int getEventType() {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        final int value = instance.get(2);
        final int value2 = instance.get(5);
        instance.get(12);
        instance.get(11);
        int n;
        if ((value == 11 && value2 >= 24 && value2 <= 31) || (value == 0 && value2 == 1)) {
            n = 0;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    public static Drawable getRoundRectSelectorDrawable(int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return (Drawable)new RippleDrawable(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { (n & 0xFFFFFF) | 0x19000000 }), (Drawable)null, createRoundRectDrawable(AndroidUtilities.dp(3.0f), -1));
        }
        final StateListDrawable stateListDrawable = new StateListDrawable();
        final int dp = AndroidUtilities.dp(3.0f);
        n = ((n & 0xFFFFFF) | 0x19000000);
        stateListDrawable.addState(new int[] { 16842919 }, createRoundRectDrawable(dp, n));
        stateListDrawable.addState(new int[] { 16842913 }, createRoundRectDrawable(AndroidUtilities.dp(3.0f), n));
        stateListDrawable.addState(StateSet.WILD_CARD, (Drawable)new ColorDrawable(0));
        return (Drawable)stateListDrawable;
    }
    
    public static long getSelectedBackgroundId() {
        final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        final long n = globalMainSettings.getInt("selectedBackground", 1000001);
        if (n != 1000001L) {
            globalMainSettings.edit().putLong("selectedBackground2", n).remove("selectedBackground").commit();
        }
        final long long1 = globalMainSettings.getLong("selectedBackground2", 1000001L);
        if (hasWallpaperFromTheme() && !globalMainSettings.getBoolean("overrideThemeWallpaper", false)) {
            return -2L;
        }
        if (long1 == -2L) {
            return 1000001L;
        }
        return long1;
    }
    
    public static int getSelectedColor() {
        return Theme.selectedColor;
    }
    
    public static Drawable getSelectorDrawable(final int n, final boolean b) {
        if (!b) {
            return createSelectorDrawable(n, 2);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            return (Drawable)new RippleDrawable(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { n }), (Drawable)new ColorDrawable(getColor("windowBackgroundWhite")), (Drawable)new ColorDrawable(-1));
        }
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { 16842919 }, (Drawable)new ColorDrawable(n));
        stateListDrawable.addState(new int[] { 16842913 }, (Drawable)new ColorDrawable(n));
        stateListDrawable.addState(StateSet.WILD_CARD, (Drawable)new ColorDrawable(getColor("windowBackgroundWhite")));
        return (Drawable)stateListDrawable;
    }
    
    public static Drawable getSelectorDrawable(final boolean b) {
        return getSelectorDrawable(getColor("listSelectorSDK21"), b);
    }
    
    public static int getServiceMessageColor() {
        final Integer n = Theme.currentColors.get("chat_serviceBackground");
        int n2;
        if (n == null) {
            n2 = Theme.serviceMessageColor;
        }
        else {
            n2 = n;
        }
        return n2;
    }
    
    public static ColorFilter getShareColorFilter(final int n, final boolean b) {
        if (b) {
            if (Theme.currentShareSelectedColorFilter == null || Theme.currentShareSelectedColorFilterColor != n) {
                Theme.currentShareSelectedColorFilterColor = n;
                Theme.currentShareSelectedColorFilter = (ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY);
            }
            return Theme.currentShareSelectedColorFilter;
        }
        if (Theme.currentShareColorFilter == null || Theme.currentShareColorFilterColor != n) {
            Theme.currentShareColorFilterColor = n;
            Theme.currentShareColorFilter = (ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY);
        }
        return Theme.currentShareColorFilter;
    }
    
    @SuppressLint({ "PrivateApi" })
    private static Drawable getStateDrawable(Drawable obj, final int i) {
        if (Theme.StateListDrawable_getStateDrawableMethod == null) {
            try {
                Theme.StateListDrawable_getStateDrawableMethod = StateListDrawable.class.getDeclaredMethod("getStateDrawable", Integer.TYPE);
            }
            catch (Throwable t) {}
        }
        final Method stateListDrawable_getStateDrawableMethod = Theme.StateListDrawable_getStateDrawableMethod;
        if (stateListDrawable_getStateDrawableMethod == null) {
            return null;
        }
        try {
            obj = (Drawable)stateListDrawable_getStateDrawableMethod.invoke(obj, i);
            return obj;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private static HashMap<String, Integer> getThemeFileValues(final File p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/HashMap.<init>:()V
        //     7: astore_2       
        //     8: aconst_null    
        //     9: astore_3       
        //    10: aconst_null    
        //    11: astore          4
        //    13: aload           4
        //    15: astore          5
        //    17: sipush          1024
        //    20: newarray        B
        //    22: astore          6
        //    24: aload_0        
        //    25: astore          7
        //    27: aload_1        
        //    28: ifnull          41
        //    31: aload           4
        //    33: astore          5
        //    35: aload_1        
        //    36: invokestatic    org/telegram/ui/ActionBar/Theme.getAssetFile:(Ljava/lang/String;)Ljava/io/File;
        //    39: astore          7
        //    41: aload           4
        //    43: astore          5
        //    45: new             Ljava/io/FileInputStream;
        //    48: astore_0       
        //    49: aload           4
        //    51: astore          5
        //    53: aload_0        
        //    54: aload           7
        //    56: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    59: iconst_m1      
        //    60: putstatic       org/telegram/ui/ActionBar/Theme.themedWallpaperFileOffset:I
        //    63: iconst_0       
        //    64: istore          8
        //    66: iconst_0       
        //    67: istore          9
        //    69: aload_0        
        //    70: aload           6
        //    72: invokevirtual   java/io/FileInputStream.read:([B)I
        //    75: istore          10
        //    77: iload           10
        //    79: iconst_m1      
        //    80: if_icmpeq       346
        //    83: iload           8
        //    85: istore          11
        //    87: iconst_0       
        //    88: istore          12
        //    90: iconst_0       
        //    91: istore          13
        //    93: iload           9
        //    95: istore          14
        //    97: iload           12
        //    99: iload           10
        //   101: if_icmpge       306
        //   104: iload           13
        //   106: istore          15
        //   108: iload           11
        //   110: istore          14
        //   112: aload           6
        //   114: iload           12
        //   116: baload         
        //   117: bipush          10
        //   119: if_icmpne       292
        //   122: iload           12
        //   124: iload           13
        //   126: isub           
        //   127: iconst_1       
        //   128: iadd           
        //   129: istore          16
        //   131: new             Ljava/lang/String;
        //   134: astore          5
        //   136: aload           5
        //   138: aload           6
        //   140: iload           13
        //   142: iload           16
        //   144: iconst_1       
        //   145: isub           
        //   146: invokespecial   java/lang/String.<init>:([BII)V
        //   149: aload           5
        //   151: ldc_w           "WPS"
        //   154: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   157: ifeq            174
        //   160: iload           16
        //   162: iload           11
        //   164: iadd           
        //   165: putstatic       org/telegram/ui/ActionBar/Theme.themedWallpaperFileOffset:I
        //   168: iconst_1       
        //   169: istore          14
        //   171: goto            306
        //   174: aload           5
        //   176: bipush          61
        //   178: invokevirtual   java/lang/String.indexOf:(I)I
        //   181: istore          14
        //   183: iload           14
        //   185: iconst_m1      
        //   186: if_icmpeq       278
        //   189: aload           5
        //   191: iconst_0       
        //   192: iload           14
        //   194: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   197: astore_1       
        //   198: aload           5
        //   200: iload           14
        //   202: iconst_1       
        //   203: iadd           
        //   204: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   207: astore          7
        //   209: aload           7
        //   211: invokevirtual   java/lang/String.length:()I
        //   214: ifle            257
        //   217: aload           7
        //   219: iconst_0       
        //   220: invokevirtual   java/lang/String.charAt:(I)C
        //   223: istore          14
        //   225: iload           14
        //   227: bipush          35
        //   229: if_icmpne       257
        //   232: aload           7
        //   234: invokestatic    android/graphics/Color.parseColor:(Ljava/lang/String;)I
        //   237: istore          14
        //   239: goto            267
        //   242: astore          5
        //   244: aload           7
        //   246: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //   249: invokevirtual   java/lang/Integer.intValue:()I
        //   252: istore          14
        //   254: goto            267
        //   257: aload           7
        //   259: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //   262: invokevirtual   java/lang/Integer.intValue:()I
        //   265: istore          14
        //   267: aload_2        
        //   268: aload_1        
        //   269: iload           14
        //   271: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   274: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   277: pop            
        //   278: iload           13
        //   280: iload           16
        //   282: iadd           
        //   283: istore          15
        //   285: iload           11
        //   287: iload           16
        //   289: iadd           
        //   290: istore          14
        //   292: iinc            12, 1
        //   295: iload           15
        //   297: istore          13
        //   299: iload           14
        //   301: istore          11
        //   303: goto            93
        //   306: iload           8
        //   308: iload           11
        //   310: if_icmpne       316
        //   313: goto            346
        //   316: aload_0        
        //   317: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //   320: iload           11
        //   322: i2l            
        //   323: invokevirtual   java/nio/channels/FileChannel.position:(J)Ljava/nio/channels/FileChannel;
        //   326: pop            
        //   327: iload           14
        //   329: ifeq            335
        //   332: goto            346
        //   335: iload           11
        //   337: istore          8
        //   339: iload           14
        //   341: istore          9
        //   343: goto            69
        //   346: aload_0        
        //   347: invokevirtual   java/io/FileInputStream.close:()V
        //   350: goto            394
        //   353: astore_1       
        //   354: goto            396
        //   357: astore_1       
        //   358: goto            371
        //   361: astore_1       
        //   362: aload           5
        //   364: astore_0       
        //   365: goto            396
        //   368: astore_1       
        //   369: aload_3        
        //   370: astore_0       
        //   371: aload_0        
        //   372: astore          5
        //   374: aload_1        
        //   375: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   378: aload_0        
        //   379: ifnull          394
        //   382: aload_0        
        //   383: invokevirtual   java/io/FileInputStream.close:()V
        //   386: goto            394
        //   389: astore_0       
        //   390: aload_0        
        //   391: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   394: aload_2        
        //   395: areturn        
        //   396: aload_0        
        //   397: ifnull          412
        //   400: aload_0        
        //   401: invokevirtual   java/io/FileInputStream.close:()V
        //   404: goto            412
        //   407: astore_0       
        //   408: aload_0        
        //   409: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   412: aload_1        
        //   413: athrow         
        //    Signature:
        //  (Ljava/io/File;Ljava/lang/String;)Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/Integer;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  17     24     368    371    Ljava/lang/Throwable;
        //  17     24     361    368    Any
        //  35     41     368    371    Ljava/lang/Throwable;
        //  35     41     361    368    Any
        //  45     49     368    371    Ljava/lang/Throwable;
        //  45     49     361    368    Any
        //  53     59     368    371    Ljava/lang/Throwable;
        //  53     59     361    368    Any
        //  59     63     357    361    Ljava/lang/Throwable;
        //  59     63     353    357    Any
        //  69     77     357    361    Ljava/lang/Throwable;
        //  69     77     353    357    Any
        //  131    168    357    361    Ljava/lang/Throwable;
        //  131    168    353    357    Any
        //  174    183    357    361    Ljava/lang/Throwable;
        //  174    183    353    357    Any
        //  189    225    357    361    Ljava/lang/Throwable;
        //  189    225    353    357    Any
        //  232    239    242    257    Ljava/lang/Exception;
        //  232    239    357    361    Ljava/lang/Throwable;
        //  232    239    353    357    Any
        //  244    254    357    361    Ljava/lang/Throwable;
        //  244    254    353    357    Any
        //  257    267    357    361    Ljava/lang/Throwable;
        //  257    267    353    357    Any
        //  267    278    357    361    Ljava/lang/Throwable;
        //  267    278    353    357    Any
        //  316    327    357    361    Ljava/lang/Throwable;
        //  316    327    353    357    Any
        //  346    350    389    394    Ljava/lang/Exception;
        //  374    378    361    368    Any
        //  382    386    389    394    Ljava/lang/Exception;
        //  400    404    407    412    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0346:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Drawable getThemedDrawable(final Context context, final int n, final int n2) {
        if (context == null) {
            return null;
        }
        final Drawable mutate = context.getResources().getDrawable(n).mutate();
        mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(n2, PorterDuff$Mode.MULTIPLY));
        return mutate;
    }
    
    public static Drawable getThemedDrawable(final Context context, final int n, final String s) {
        return getThemedDrawable(context, n, getColor(s));
    }
    
    public static Drawable getThemedWallpaper(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc_w           "chat_wallpaper"
        //     6: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //     9: checkcast       Ljava/lang/Integer;
        //    12: astore_1       
        //    13: aload_1        
        //    14: ifnull          29
        //    17: new             Landroid/graphics/drawable/ColorDrawable;
        //    20: dup            
        //    21: aload_1        
        //    22: invokevirtual   java/lang/Integer.intValue:()I
        //    25: invokespecial   android/graphics/drawable/ColorDrawable.<init>:(I)V
        //    28: areturn        
        //    29: getstatic       org/telegram/ui/ActionBar/Theme.themedWallpaperFileOffset:I
        //    32: ifle            332
        //    35: getstatic       org/telegram/ui/ActionBar/Theme.currentTheme:Lorg/telegram/ui/ActionBar/Theme$ThemeInfo;
        //    38: astore_1       
        //    39: aload_1        
        //    40: getfield        org/telegram/ui/ActionBar/Theme$ThemeInfo.pathToFile:Ljava/lang/String;
        //    43: ifnonnull       53
        //    46: aload_1        
        //    47: getfield        org/telegram/ui/ActionBar/Theme$ThemeInfo.assetName:Ljava/lang/String;
        //    50: ifnull          332
        //    53: getstatic       org/telegram/ui/ActionBar/Theme.currentTheme:Lorg/telegram/ui/ActionBar/Theme$ThemeInfo;
        //    56: getfield        org/telegram/ui/ActionBar/Theme$ThemeInfo.assetName:Ljava/lang/String;
        //    59: ifnull          75
        //    62: getstatic       org/telegram/ui/ActionBar/Theme.currentTheme:Lorg/telegram/ui/ActionBar/Theme$ThemeInfo;
        //    65: getfield        org/telegram/ui/ActionBar/Theme$ThemeInfo.assetName:Ljava/lang/String;
        //    68: invokestatic    org/telegram/ui/ActionBar/Theme.getAssetFile:(Ljava/lang/String;)Ljava/io/File;
        //    71: astore_1       
        //    72: goto            89
        //    75: new             Ljava/io/File;
        //    78: dup            
        //    79: getstatic       org/telegram/ui/ActionBar/Theme.currentTheme:Lorg/telegram/ui/ActionBar/Theme$ThemeInfo;
        //    82: getfield        org/telegram/ui/ActionBar/Theme$ThemeInfo.pathToFile:Ljava/lang/String;
        //    85: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    88: astore_1       
        //    89: new             Ljava/io/FileInputStream;
        //    92: astore_2       
        //    93: aload_2        
        //    94: aload_1        
        //    95: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    98: aload_2        
        //    99: astore_1       
        //   100: aload_2        
        //   101: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //   104: getstatic       org/telegram/ui/ActionBar/Theme.themedWallpaperFileOffset:I
        //   107: i2l            
        //   108: invokevirtual   java/nio/channels/FileChannel.position:(J)Ljava/nio/channels/FileChannel;
        //   111: pop            
        //   112: aload_2        
        //   113: astore_1       
        //   114: new             Landroid/graphics/BitmapFactory$Options;
        //   117: astore_3       
        //   118: aload_2        
        //   119: astore_1       
        //   120: aload_3        
        //   121: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
        //   124: iconst_1       
        //   125: istore          4
        //   127: iconst_1       
        //   128: istore          5
        //   130: iload_0        
        //   131: ifeq            215
        //   134: aload_2        
        //   135: astore_1       
        //   136: aload_3        
        //   137: iconst_1       
        //   138: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
        //   141: aload_2        
        //   142: astore_1       
        //   143: aload_3        
        //   144: getfield        android/graphics/BitmapFactory$Options.outWidth:I
        //   147: i2f            
        //   148: fstore          6
        //   150: aload_2        
        //   151: astore_1       
        //   152: aload_3        
        //   153: getfield        android/graphics/BitmapFactory$Options.outHeight:I
        //   156: i2f            
        //   157: fstore          7
        //   159: aload_2        
        //   160: astore_1       
        //   161: ldc_w           100.0
        //   164: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //   167: istore          8
        //   169: iload           8
        //   171: i2f            
        //   172: fstore          9
        //   174: fload           6
        //   176: fload           9
        //   178: fcmpl          
        //   179: ifgt            194
        //   182: iload           5
        //   184: istore          4
        //   186: fload           7
        //   188: fload           9
        //   190: fcmpl          
        //   191: ifle            215
        //   194: iload           5
        //   196: iconst_2       
        //   197: imul           
        //   198: istore          5
        //   200: fload           6
        //   202: fconst_2       
        //   203: fdiv           
        //   204: fstore          6
        //   206: fload           7
        //   208: fconst_2       
        //   209: fdiv           
        //   210: fstore          7
        //   212: goto            169
        //   215: aload_2        
        //   216: astore_1       
        //   217: aload_3        
        //   218: iconst_0       
        //   219: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
        //   222: aload_2        
        //   223: astore_1       
        //   224: aload_3        
        //   225: iload           4
        //   227: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //   230: aload_2        
        //   231: astore_1       
        //   232: aload_2        
        //   233: aconst_null    
        //   234: aload_3        
        //   235: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   238: astore_3       
        //   239: aload_3        
        //   240: ifnull          268
        //   243: aload_2        
        //   244: astore_1       
        //   245: new             Landroid/graphics/drawable/BitmapDrawable;
        //   248: dup            
        //   249: aload_3        
        //   250: invokespecial   android/graphics/drawable/BitmapDrawable.<init>:(Landroid/graphics/Bitmap;)V
        //   253: astore_3       
        //   254: aload_2        
        //   255: invokevirtual   java/io/FileInputStream.close:()V
        //   258: goto            266
        //   261: astore_1       
        //   262: aload_1        
        //   263: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   266: aload_3        
        //   267: areturn        
        //   268: aload_2        
        //   269: invokevirtual   java/io/FileInputStream.close:()V
        //   272: goto            332
        //   275: astore_3       
        //   276: goto            288
        //   279: astore_2       
        //   280: aconst_null    
        //   281: astore_1       
        //   282: goto            314
        //   285: astore_3       
        //   286: aconst_null    
        //   287: astore_2       
        //   288: aload_2        
        //   289: astore_1       
        //   290: aload_3        
        //   291: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   294: aload_2        
        //   295: ifnull          332
        //   298: aload_2        
        //   299: invokevirtual   java/io/FileInputStream.close:()V
        //   302: goto            332
        //   305: astore_1       
        //   306: aload_1        
        //   307: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   310: goto            332
        //   313: astore_2       
        //   314: aload_1        
        //   315: ifnull          330
        //   318: aload_1        
        //   319: invokevirtual   java/io/FileInputStream.close:()V
        //   322: goto            330
        //   325: astore_1       
        //   326: aload_1        
        //   327: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   330: aload_2        
        //   331: athrow         
        //   332: aconst_null    
        //   333: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  53     72     285    288    Ljava/lang/Throwable;
        //  53     72     279    285    Any
        //  75     89     285    288    Ljava/lang/Throwable;
        //  75     89     279    285    Any
        //  89     98     285    288    Ljava/lang/Throwable;
        //  89     98     279    285    Any
        //  100    112    275    279    Ljava/lang/Throwable;
        //  100    112    313    314    Any
        //  114    118    275    279    Ljava/lang/Throwable;
        //  114    118    313    314    Any
        //  120    124    275    279    Ljava/lang/Throwable;
        //  120    124    313    314    Any
        //  136    141    275    279    Ljava/lang/Throwable;
        //  136    141    313    314    Any
        //  143    150    275    279    Ljava/lang/Throwable;
        //  143    150    313    314    Any
        //  152    159    275    279    Ljava/lang/Throwable;
        //  152    159    313    314    Any
        //  161    169    275    279    Ljava/lang/Throwable;
        //  161    169    313    314    Any
        //  217    222    275    279    Ljava/lang/Throwable;
        //  217    222    313    314    Any
        //  224    230    275    279    Ljava/lang/Throwable;
        //  224    230    313    314    Any
        //  232    239    275    279    Ljava/lang/Throwable;
        //  232    239    313    314    Any
        //  245    254    275    279    Ljava/lang/Throwable;
        //  245    254    313    314    Any
        //  254    258    261    266    Ljava/lang/Exception;
        //  268    272    305    313    Ljava/lang/Exception;
        //  290    294    313    314    Any
        //  298    302    305    313    Ljava/lang/Exception;
        //  318    322    325    330    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0169:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static boolean hasThemeKey(final String key) {
        return Theme.currentColors.containsKey(key);
    }
    
    public static boolean hasWallpaperFromTheme() {
        return Theme.currentColors.containsKey("chat_wallpaper") || Theme.themedWallpaperFileOffset > 0;
    }
    
    public static boolean isAnimatingColor() {
        return Theme.animatingColors != null;
    }
    
    public static boolean isCurrentThemeDefault() {
        return Theme.currentTheme == Theme.defaultTheme;
    }
    
    public static boolean isCurrentThemeNight() {
        return Theme.currentTheme == Theme.currentNightTheme;
    }
    
    public static boolean isCustomTheme() {
        return Theme.isCustomTheme;
    }
    
    public static boolean isPatternWallpaper() {
        return Theme.isPatternWallpaper;
    }
    
    public static boolean isWallpaperMotion() {
        return Theme.isWallpaperMotion;
    }
    
    public static void loadWallpaper() {
        if (Theme.wallpaper != null) {
            return;
        }
        Utilities.searchQueue.postRunnable((Runnable)_$$Lambda$Theme$g9IkSg8DwYzdCeZKlfMvOXD2f9I.INSTANCE);
    }
    
    public static void reloadAllResources(final Context context) {
        destroyResources();
        if (Theme.chat_msgInDrawable != null) {
            Theme.chat_msgInDrawable = null;
            Theme.currentColor = 0;
            Theme.currentSelectedColor = 0;
            createChatResources(context, false);
        }
        if (Theme.dialogs_namePaint != null) {
            Theme.dialogs_namePaint = null;
            createDialogsResources(context);
        }
        if (Theme.profile_verifiedDrawable != null) {
            Theme.profile_verifiedDrawable = null;
            createProfileResources(context);
        }
    }
    
    public static void reloadWallpaper() {
        Theme.wallpaper = null;
        Theme.themedWallpaper = null;
        loadWallpaper();
    }
    
    public static void saveAutoNightThemeConfig() {
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("selectedAutoNightType", Theme.selectedAutoNightType);
        edit.putBoolean("autoNightScheduleByLocation", Theme.autoNightScheduleByLocation);
        edit.putFloat("autoNightBrighnessThreshold", Theme.autoNightBrighnessThreshold);
        edit.putInt("autoNightDayStartTime", Theme.autoNightDayStartTime);
        edit.putInt("autoNightDayEndTime", Theme.autoNightDayEndTime);
        edit.putInt("autoNightSunriseTime", Theme.autoNightSunriseTime);
        edit.putString("autoNightCityName", Theme.autoNightCityName);
        edit.putInt("autoNightSunsetTime", Theme.autoNightSunsetTime);
        edit.putLong("autoNightLocationLatitude3", Double.doubleToRawLongBits(Theme.autoNightLocationLatitude));
        edit.putLong("autoNightLocationLongitude3", Double.doubleToRawLongBits(Theme.autoNightLocationLongitude));
        edit.putInt("autoNightLastSunCheckDay", Theme.autoNightLastSunCheckDay);
        final ThemeInfo currentNightTheme = Theme.currentNightTheme;
        if (currentNightTheme != null) {
            edit.putString("nighttheme", currentNightTheme.name);
        }
        else {
            edit.remove("nighttheme");
        }
        edit.commit();
    }
    
    public static void saveCurrentTheme(final String name, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, Integer> entry : Theme.currentColors.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        final File file = new File(ApplicationLoader.getFilesDirFixed(), name);
        Object o = null;
        FileOutputStream fileOutputStream = null;
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = fileOutputStream;
                fileOutputStream2 = new FileOutputStream(file);
                try {
                    fileOutputStream2.write(AndroidUtilities.getStringBytes(sb.toString()));
                    if (Theme.themedWallpaper instanceof BitmapDrawable) {
                        final Bitmap bitmap = ((BitmapDrawable)Theme.themedWallpaper).getBitmap();
                        if (bitmap != null) {
                            fileOutputStream2.write(new byte[] { 87, 80, 83, 10 });
                            bitmap.compress(Bitmap$CompressFormat.JPEG, 87, (OutputStream)fileOutputStream2);
                            fileOutputStream2.write(new byte[] { 10, 87, 80, 69, 10 });
                        }
                        if (b) {
                            calcBackgroundColor(Theme.wallpaper = Theme.themedWallpaper, 2);
                        }
                    }
                    o = Theme.themesDict.get(name);
                    ThemeInfo themeInfo;
                    if ((themeInfo = (ThemeInfo)o) == null) {
                        themeInfo = new ThemeInfo();
                        themeInfo.pathToFile = file.getAbsolutePath();
                        themeInfo.name = name;
                        Theme.themes.add(themeInfo);
                        Theme.themesDict.put(themeInfo.name, themeInfo);
                        Theme.otherThemes.add(themeInfo);
                        saveOtherThemes();
                        sortThemes();
                    }
                    Theme.currentTheme = themeInfo;
                    if (Theme.currentTheme != Theme.currentNightTheme) {
                        Theme.currentDayTheme = Theme.currentTheme;
                    }
                    final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
                    edit.putString("theme", Theme.currentDayTheme.name);
                    edit.commit();
                    final FileOutputStream fileOutputStream3 = fileOutputStream2;
                    fileOutputStream3.close();
                }
                catch (Exception ex3) {}
                finally {
                    final Object o3;
                    final Object o2 = o3;
                }
            }
            finally {
                fileOutputStream2 = fileOutputStream;
            }
        }
        catch (Exception fileOutputStream2) {
            final Object o2 = o;
        }
        try {
            final FileOutputStream fileOutputStream3 = fileOutputStream2;
            fileOutputStream3.close();
            return;
            final Object o2;
            ((FileOutputStream)o2).close();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return;
        if (fileOutputStream2 != null) {
            try {
                fileOutputStream2.close();
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
        }
    }
    
    private static void saveOtherThemes() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        int i = 0;
        final SharedPreferences$Editor edit = applicationContext.getSharedPreferences("themeconfig", 0).edit();
        final JSONArray jsonArray = new JSONArray();
        while (i < Theme.otherThemes.size()) {
            final JSONObject saveJson = Theme.otherThemes.get(i).getSaveJson();
            if (saveJson != null) {
                jsonArray.put((Object)saveJson);
            }
            ++i;
        }
        edit.putString("themes2", jsonArray.toString());
        edit.commit();
    }
    
    public static void setAnimatedColor(final String key, final int i) {
        final HashMap<String, Integer> animatingColors = Theme.animatingColors;
        if (animatingColors == null) {
            return;
        }
        animatingColors.put(key, i);
    }
    
    public static void setAnimatingColor(final boolean b) {
        HashMap<String, Integer> animatingColors;
        if (b) {
            animatingColors = new HashMap<String, Integer>();
        }
        else {
            animatingColors = null;
        }
        Theme.animatingColors = animatingColors;
    }
    
    public static void setColor(final String s, final int n, final boolean b) {
        int i = n;
        if (s.equals("chat_wallpaper")) {
            i = (n | 0xFF000000);
        }
        if (b) {
            Theme.currentColors.remove(s);
        }
        else {
            Theme.currentColors.put(s, i);
        }
        if (!s.equals("chat_serviceBackground") && !s.equals("chat_serviceBackgroundSelected")) {
            if (s.equals("chat_wallpaper")) {
                reloadWallpaper();
            }
        }
        else {
            applyChatServiceMessageColor();
        }
    }
    
    public static void setCombinedDrawableColor(Drawable drawable, final int color, final boolean b) {
        if (!(drawable instanceof CombinedDrawable)) {
            return;
        }
        if (b) {
            drawable = ((CombinedDrawable)drawable).getIcon();
        }
        else {
            drawable = ((CombinedDrawable)drawable).getBackground();
        }
        if (drawable instanceof ColorDrawable) {
            ((ColorDrawable)drawable).setColor(color);
        }
        else {
            drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(color, PorterDuff$Mode.MULTIPLY));
        }
    }
    
    public static void setCurrentNightTheme(final ThemeInfo currentNightTheme) {
        final boolean b = Theme.currentTheme == Theme.currentNightTheme;
        Theme.currentNightTheme = currentNightTheme;
        if (b) {
            applyDayNightThemeMaybe(true);
        }
    }
    
    public static void setDrawableColor(final Drawable drawable, final int n) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable)drawable).getPaint().setColor(n);
        }
        else if (drawable instanceof ScamDrawable) {
            ((ScamDrawable)drawable).setColor(n);
        }
        else {
            drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
        }
    }
    
    public static void setDrawableColorByKey(final Drawable drawable, final String s) {
        if (s == null) {
            return;
        }
        setDrawableColor(drawable, getColor(s));
    }
    
    public static void setEmojiDrawableColor(Drawable stateDrawable, final int n, final boolean b) {
        if (!(stateDrawable instanceof StateListDrawable)) {
            return;
        }
        Label_0058: {
            if (!b) {
                break Label_0058;
            }
            try {
                stateDrawable = getStateDrawable(stateDrawable, 0);
                if (stateDrawable instanceof ShapeDrawable) {
                    ((ShapeDrawable)stateDrawable).getPaint().setColor(n);
                }
                else {
                    stateDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                }
                return;
                // iftrue(Label_0085:, !stateDrawable2 instanceof ShapeDrawable)
                Drawable stateDrawable2 = null;
                while (true) {
                    ((ShapeDrawable)stateDrawable2).getPaint().setColor(n);
                    return;
                    stateDrawable2 = getStateDrawable(stateDrawable, 1);
                    continue;
                }
                Label_0085: {
                    stateDrawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                }
            }
            catch (Throwable t) {}
        }
    }
    
    public static void setSelectorDrawableColor(Drawable drawable, final int n, final boolean b) {
        Label_0160: {
            if (!(drawable instanceof StateListDrawable)) {
                break Label_0160;
            }
            Label_0109: {
                if (!b) {
                    break Label_0109;
                }
                try {
                    final Drawable stateDrawable = getStateDrawable(drawable, 0);
                    if (stateDrawable instanceof ShapeDrawable) {
                        ((ShapeDrawable)stateDrawable).getPaint().setColor(n);
                    }
                    else {
                        stateDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                    }
                    final Drawable stateDrawable2 = getStateDrawable(drawable, 1);
                    if (stateDrawable2 instanceof ShapeDrawable) {
                        ((ShapeDrawable)stateDrawable2).getPaint().setColor(n);
                    }
                    else {
                        stateDrawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                    }
                    Label_0264: {
                        return;
                    }
                    // iftrue(Label_0264:, Build$VERSION.SDK_INT < 21 || !drawable instanceof RippleDrawable)
                    // iftrue(Label_0249:, !drawable instanceof ShapeDrawable)
                    // iftrue(Label_0215:, !b)
                Block_6_Outer:
                    while (true) {
                        while (true) {
                        Block_11_Outer:
                            while (true) {
                                final RippleDrawable rippleDrawable;
                                rippleDrawable.setColor(new ColorStateList(new int[][] { StateSet.WILD_CARD }, new int[] { n }));
                                return;
                                Block_8: {
                                    while (true) {
                                        ((ShapeDrawable)drawable).getPaint().setColor(n);
                                        return;
                                        break Block_8;
                                        drawable = rippleDrawable.getDrawable(0);
                                        continue Block_6_Outer;
                                    }
                                    final Drawable stateDrawable3;
                                    Label_0139:
                                    stateDrawable3.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                                    return;
                                    ((ShapeDrawable)stateDrawable3).getPaint().setColor(n);
                                    return;
                                }
                                rippleDrawable = (RippleDrawable)drawable;
                                continue Block_11_Outer;
                            }
                            Label_0249:
                            drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
                            return;
                            final Drawable stateDrawable3 = getStateDrawable(drawable, 2);
                            continue;
                        }
                        Label_0215:
                        continue Block_6_Outer;
                    }
                }
                // iftrue(Label_0139:, !stateDrawable3 instanceof ShapeDrawable)
                // iftrue(Label_0264:, rippleDrawable.getNumberOfLayers() <= 0)
                catch (Throwable t) {}
            }
        }
    }
    
    public static void setThemeWallpaper(final String s, final Bitmap bitmap, final File file) {
        Theme.currentColors.remove("chat_wallpaper");
        MessagesController.getGlobalMainSettings().edit().remove("overrideThemeWallpaper").commit();
        if (bitmap != null) {
            Theme.themedWallpaper = (Drawable)new BitmapDrawable(bitmap);
            saveCurrentTheme(s, false);
            calcBackgroundColor(Theme.themedWallpaper, 0);
            applyChatServiceMessageColor();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
        }
        else {
            Theme.themedWallpaper = null;
            Theme.wallpaper = null;
            saveCurrentTheme(s, false);
            reloadWallpaper();
        }
    }
    
    private static void sortThemes() {
        Collections.sort(Theme.themes, (Comparator<? super ThemeInfo>)_$$Lambda$Theme$CDAxGNnEyNa6tkvecQwKSXq77_I.INSTANCE);
    }
    
    private static class AttachCameraDrawable extends Drawable
    {
        private Paint paint;
        private Path segment;
        
        public AttachCameraDrawable() {
            this.paint = new Paint(1);
            final float n = (float)AndroidUtilities.dp(54.0f);
            final RectF rectF = new RectF(0.0f, 0.0f, n, n);
            (this.segment = new Path()).moveTo((float)AndroidUtilities.dp(23.0f), (float)AndroidUtilities.dp(20.0f));
            this.segment.lineTo((float)AndroidUtilities.dp(23.0f), 0.0f);
            this.segment.arcTo(rectF, -98.0f, 50.0f, false);
            this.segment.close();
        }
        
        public void draw(final Canvas canvas) {
            canvas.save();
            final float n = (float)AndroidUtilities.dp(27.0f);
            canvas.rotate(-90.0f, n, n);
            for (int i = 0; i < 6; ++i) {
                if (i != 0) {
                    if (i != 1) {
                        if (i != 2) {
                            if (i != 3) {
                                if (i != 4) {
                                    if (i == 5) {
                                        this.paint.setColor(Theme.getColor("chat_attachCameraIcon6"));
                                    }
                                }
                                else {
                                    this.paint.setColor(Theme.getColor("chat_attachCameraIcon5"));
                                }
                            }
                            else {
                                this.paint.setColor(Theme.getColor("chat_attachCameraIcon4"));
                            }
                        }
                        else {
                            this.paint.setColor(Theme.getColor("chat_attachCameraIcon3"));
                        }
                    }
                    else {
                        this.paint.setColor(Theme.getColor("chat_attachCameraIcon2"));
                    }
                }
                else {
                    this.paint.setColor(Theme.getColor("chat_attachCameraIcon1"));
                }
                canvas.rotate(60.0f, n, n);
                canvas.drawPath(this.segment, this.paint);
            }
            canvas.restore();
        }
        
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(54.0f);
        }
        
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(54.0f);
        }
        
        public int getMinimumHeight() {
            return AndroidUtilities.dp(54.0f);
        }
        
        public int getMinimumWidth() {
            return AndroidUtilities.dp(54.0f);
        }
        
        public int getOpacity() {
            return -2;
        }
        
        public void setAlpha(final int n) {
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
            this.invalidateSelf();
        }
    }
    
    public static class ThemeInfo
    {
        public String assetName;
        public String name;
        public String pathToFile;
        public int previewBackgroundColor;
        public int previewInColor;
        public int previewOutColor;
        public int sortIndex;
        
        public static ThemeInfo createWithJson(final JSONObject jsonObject) {
            if (jsonObject == null) {
                return null;
            }
            try {
                final ThemeInfo themeInfo = new ThemeInfo();
                themeInfo.name = jsonObject.getString("name");
                themeInfo.pathToFile = jsonObject.getString("path");
                return themeInfo;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return null;
            }
        }
        
        public static ThemeInfo createWithString(final String s) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                return null;
            }
            final String[] split = s.split("\\|");
            if (split.length != 2) {
                return null;
            }
            final ThemeInfo themeInfo = new ThemeInfo();
            themeInfo.name = split[0];
            themeInfo.pathToFile = split[1];
            return themeInfo;
        }
        
        public String getName() {
            if ("Default".equals(this.name)) {
                return LocaleController.getString("Default", 2131559225);
            }
            if ("Blue".equals(this.name)) {
                return LocaleController.getString("ThemeBlue", 2131560891);
            }
            if ("Dark".equals(this.name)) {
                return LocaleController.getString("ThemeDark", 2131560892);
            }
            if ("Dark Blue".equals(this.name)) {
                return LocaleController.getString("ThemeDarkBlue", 2131560893);
            }
            if ("Graphite".equals(this.name)) {
                return LocaleController.getString("ThemeGraphite", 2131560894);
            }
            if ("Arctic Blue".equals(this.name)) {
                return LocaleController.getString("ThemeArcticBlue", 2131560890);
            }
            return this.name;
        }
        
        public JSONObject getSaveJson() {
            try {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", (Object)this.name);
                jsonObject.put("path", (Object)this.pathToFile);
                return jsonObject;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return null;
            }
        }
        
        public boolean isDark() {
            return "Dark".equals(this.name) || "Dark Blue".equals(this.name) || "Graphite".equals(this.name);
        }
        
        public boolean isLight() {
            return this.pathToFile == null && !this.isDark();
        }
    }
}
