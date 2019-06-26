package org.telegram.ui.ActionBar;

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
import android.graphics.Path;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.StateSet;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import java.io.File;
import java.io.InputStream;
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
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.time.SunDate;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.ThemeEditorView;

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
   private static Field BitmapDrawable_mColorFilter;
   public static final long DEFAULT_BACKGROUND_ID = 1000001L;
   private static final int LIGHT_SENSOR_THEME_SWITCH_DELAY = 1800;
   private static final int LIGHT_SENSOR_THEME_SWITCH_NEAR_DELAY = 12000;
   private static final int LIGHT_SENSOR_THEME_SWITCH_NEAR_THRESHOLD = 12000;
   private static final float MAXIMUM_LUX_BREAKPOINT = 500.0F;
   private static Method StateListDrawable_getStateDrawableMethod;
   public static final long THEME_BACKGROUND_ID = -2L;
   private static SensorEventListener ambientSensorListener;
   private static HashMap animatingColors;
   public static float autoNightBrighnessThreshold = 0.25F;
   public static String autoNightCityName;
   public static int autoNightDayEndTime = 480;
   public static int autoNightDayStartTime = 1320;
   public static int autoNightLastSunCheckDay;
   public static double autoNightLocationLatitude;
   public static double autoNightLocationLongitude;
   public static boolean autoNightScheduleByLocation;
   public static int autoNightSunriseTime;
   public static int autoNightSunsetTime = 1320;
   public static Paint avatar_backgroundPaint;
   public static Drawable avatar_broadcastDrawable;
   public static Drawable avatar_savedDrawable;
   public static Drawable calllog_msgCallDownGreenDrawable;
   public static Drawable calllog_msgCallDownRedDrawable;
   public static Drawable calllog_msgCallUpGreenDrawable;
   public static Drawable calllog_msgCallUpRedDrawable;
   private static boolean canStartHolidayAnimation;
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
   private static int currentColor;
   private static HashMap currentColors;
   private static Theme.ThemeInfo currentDayTheme;
   private static Theme.ThemeInfo currentNightTheme;
   private static int currentSelectedColor;
   private static ColorFilter currentShareColorFilter;
   private static int currentShareColorFilterColor;
   private static ColorFilter currentShareSelectedColorFilter;
   private static int currentShareSelectedColorFilterColor;
   private static Theme.ThemeInfo currentTheme;
   private static HashMap defaultColors;
   private static Theme.ThemeInfo defaultTheme;
   public static LottieDrawable dialogs_archiveAvatarDrawable;
   public static boolean dialogs_archiveAvatarDrawableRecolored;
   public static Drawable dialogs_archiveDrawable;
   public static boolean dialogs_archiveDrawableRecolored;
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
   private static int dialogs_holidayDrawableOffsetX;
   private static int dialogs_holidayDrawableOffsetY;
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
   private static HashMap fallbackKeys;
   private static boolean isCustomTheme;
   private static boolean isPatternWallpaper;
   private static boolean isWallpaperMotion;
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
   private static float lastBrightnessValue = 1.0F;
   private static long lastHolidayCheckTime;
   private static long lastThemeSwitchTime;
   private static Sensor lightSensor;
   private static boolean lightSensorRegistered;
   public static Paint linkSelectionPaint;
   public static Drawable listSelector;
   private static Paint maskPaint;
   public static Drawable moveUpDrawable;
   private static ArrayList otherThemes;
   private static Theme.ThemeInfo previousTheme;
   public static TextPaint profile_aboutTextPaint;
   public static Drawable profile_verifiedCheckDrawable;
   public static Drawable profile_verifiedDrawable;
   public static int selectedAutoNightType = 0;
   private static int selectedColor;
   private static SensorManager sensorManager;
   private static int serviceMessage2Color;
   private static int serviceMessageColor;
   public static int serviceMessageColorBackup;
   private static int serviceSelectedMessage2Color;
   private static int serviceSelectedMessageColor;
   public static int serviceSelectedMessageColorBackup;
   private static Runnable switchDayBrightnessRunnable = new Runnable() {
      public void run() {
         Theme.switchDayRunnableScheduled = false;
         Theme.applyDayNightThemeMaybe(false);
      }
   };
   private static boolean switchDayRunnableScheduled;
   private static Runnable switchNightBrightnessRunnable = new Runnable() {
      public void run() {
         Theme.switchNightRunnableScheduled = false;
         Theme.applyDayNightThemeMaybe(true);
      }
   };
   private static boolean switchNightRunnableScheduled;
   private static final Object sync = new Object();
   private static Drawable themedWallpaper;
   private static int themedWallpaperFileOffset;
   public static ArrayList themes;
   private static HashMap themesDict;
   private static Drawable wallpaper;
   private static final Object wallpaperSync = new Object();

   static {
      Integer var0 = -1;
      autoNightLastSunCheckDay = -1;
      autoNightSunriseTime = 480;
      autoNightCityName = "";
      autoNightLocationLatitude = 10000.0D;
      autoNightLocationLongitude = 10000.0D;
      maskPaint = new Paint(1);
      chat_attachButtonDrawables = new Drawable[10];
      chat_locationDrawable = new Drawable[2];
      chat_contactDrawable = new Drawable[2];
      chat_cornerOuter = new Drawable[4];
      chat_cornerInner = new Drawable[4];
      chat_fileStatesDrawable = new Drawable[10][2];
      chat_fileMiniStatesDrawable = new CombinedDrawable[6][2];
      chat_photoStatesDrawables = new Drawable[13][2];
      keys_avatar_background = new String[]{"avatar_backgroundRed", "avatar_backgroundOrange", "avatar_backgroundViolet", "avatar_backgroundGreen", "avatar_backgroundCyan", "avatar_backgroundBlue", "avatar_backgroundPink"};
      keys_avatar_nameInMessage = new String[]{"avatar_nameInMessageRed", "avatar_nameInMessageOrange", "avatar_nameInMessageViolet", "avatar_nameInMessageGreen", "avatar_nameInMessageCyan", "avatar_nameInMessageBlue", "avatar_nameInMessagePink"};
      defaultColors = new HashMap();
      fallbackKeys = new HashMap();
      defaultColors.put("dialogBackground", var0);
      defaultColors.put("dialogBackgroundGray", -986896);
      HashMap var1 = defaultColors;
      Integer var2 = -14540254;
      var1.put("dialogTextBlack", var2);
      defaultColors.put("dialogTextLink", -14255946);
      defaultColors.put("dialogLinkSelection", 862104035);
      defaultColors.put("dialogTextRed", -3319206);
      defaultColors.put("dialogTextRed2", -2213318);
      defaultColors.put("dialogTextBlue", -13660983);
      defaultColors.put("dialogTextBlue2", -12937771);
      defaultColors.put("dialogTextBlue3", -12664327);
      defaultColors.put("dialogTextBlue4", -15095832);
      defaultColors.put("dialogTextGray", -13333567);
      defaultColors.put("dialogTextGray2", -9079435);
      defaultColors.put("dialogTextGray3", -6710887);
      defaultColors.put("dialogTextGray4", -5000269);
      defaultColors.put("dialogTextHint", -6842473);
      defaultColors.put("dialogIcon", -9999504);
      defaultColors.put("dialogRedIcon", -2011827);
      defaultColors.put("dialogGrayLine", -2960686);
      defaultColors.put("dialogTopBackground", -9456923);
      defaultColors.put("dialogInputField", -2368549);
      defaultColors.put("dialogInputFieldActivated", -13129232);
      defaultColors.put("dialogCheckboxSquareBackground", -12345121);
      defaultColors.put("dialogCheckboxSquareCheck", var0);
      defaultColors.put("dialogCheckboxSquareUnchecked", -9211021);
      defaultColors.put("dialogCheckboxSquareDisabled", -5197648);
      defaultColors.put("dialogRadioBackground", -5000269);
      defaultColors.put("dialogRadioBackgroundChecked", -13129232);
      defaultColors.put("dialogProgressCircle", -11371101);
      defaultColors.put("dialogLineProgress", -11371101);
      defaultColors.put("dialogLineProgressBackground", -2368549);
      defaultColors.put("dialogButton", -11955764);
      defaultColors.put("dialogButtonSelector", 251658240);
      defaultColors.put("dialogScrollGlow", -657673);
      defaultColors.put("dialogRoundCheckBox", -11750155);
      defaultColors.put("dialogRoundCheckBoxCheck", var0);
      defaultColors.put("dialogBadgeBackground", -12664327);
      defaultColors.put("dialogBadgeText", var0);
      defaultColors.put("dialogCameraIcon", var0);
      defaultColors.put("dialog_inlineProgressBackground", -151981323);
      defaultColors.put("dialog_inlineProgress", -9735304);
      defaultColors.put("dialogSearchBackground", -854795);
      defaultColors.put("dialogSearchHint", -6774617);
      defaultColors.put("dialogSearchIcon", -6182737);
      defaultColors.put("dialogSearchText", var2);
      defaultColors.put("dialogFloatingButton", -11750155);
      defaultColors.put("dialogFloatingButtonPressed", -11750155);
      defaultColors.put("dialogFloatingIcon", var0);
      defaultColors.put("dialogShadowLine", 301989888);
      defaultColors.put("windowBackgroundWhite", var0);
      defaultColors.put("windowBackgroundUnchecked", -6445135);
      defaultColors.put("windowBackgroundChecked", -11034919);
      defaultColors.put("windowBackgroundCheckText", var0);
      defaultColors.put("progressCircle", -11371101);
      defaultColors.put("windowBackgroundWhiteGrayIcon", -8288629);
      defaultColors.put("windowBackgroundWhiteBlueText", -12545331);
      defaultColors.put("windowBackgroundWhiteBlueText2", -12937771);
      defaultColors.put("windowBackgroundWhiteBlueText3", -14255946);
      defaultColors.put("windowBackgroundWhiteBlueText4", -11697229);
      defaultColors.put("windowBackgroundWhiteBlueText5", -11759926);
      HashMap var3 = defaultColors;
      Integer var37 = -12940081;
      var3.put("windowBackgroundWhiteBlueText6", var37);
      defaultColors.put("windowBackgroundWhiteBlueText7", -13141330);
      defaultColors.put("windowBackgroundWhiteBlueButton", -14776109);
      defaultColors.put("windowBackgroundWhiteBlueIcon", -13132315);
      defaultColors.put("windowBackgroundWhiteGreenText", -14248148);
      defaultColors.put("windowBackgroundWhiteGreenText2", -13129704);
      defaultColors.put("windowBackgroundWhiteRedText", -3319206);
      defaultColors.put("windowBackgroundWhiteRedText2", -2404015);
      defaultColors.put("windowBackgroundWhiteRedText3", -2995895);
      defaultColors.put("windowBackgroundWhiteRedText4", -3198928);
      defaultColors.put("windowBackgroundWhiteRedText5", -1230535);
      defaultColors.put("windowBackgroundWhiteRedText6", -39322);
      defaultColors.put("windowBackgroundWhiteGrayText", -8156010);
      defaultColors.put("windowBackgroundWhiteGrayText2", -7697782);
      defaultColors.put("windowBackgroundWhiteGrayText3", -6710887);
      defaultColors.put("windowBackgroundWhiteGrayText4", -8355712);
      defaultColors.put("windowBackgroundWhiteGrayText5", -6052957);
      defaultColors.put("windowBackgroundWhiteGrayText6", -9079435);
      defaultColors.put("windowBackgroundWhiteGrayText7", -3750202);
      defaultColors.put("windowBackgroundWhiteGrayText8", -9605774);
      defaultColors.put("windowBackgroundWhiteGrayLine", -2368549);
      defaultColors.put("windowBackgroundWhiteBlackText", var2);
      defaultColors.put("windowBackgroundWhiteHintText", -5723992);
      defaultColors.put("windowBackgroundWhiteValueText", -12937771);
      defaultColors.put("windowBackgroundWhiteLinkText", -14255946);
      defaultColors.put("windowBackgroundWhiteLinkSelection", 862104035);
      defaultColors.put("windowBackgroundWhiteBlueHeader", -12937771);
      defaultColors.put("windowBackgroundWhiteInputField", -2368549);
      defaultColors.put("windowBackgroundWhiteInputFieldActivated", -13129232);
      defaultColors.put("switchTrack", -5196358);
      defaultColors.put("switchTrackChecked", -11358743);
      defaultColors.put("switchTrackBlue", -8221031);
      defaultColors.put("switchTrackBlueChecked", -12810041);
      defaultColors.put("switchTrackBlueThumb", var0);
      defaultColors.put("switchTrackBlueThumbChecked", var0);
      defaultColors.put("switchTrackBlueSelector", 390089299);
      defaultColors.put("switchTrackBlueSelectorChecked", 553797505);
      defaultColors.put("switch2Track", -688514);
      defaultColors.put("switch2TrackChecked", -11358743);
      defaultColors.put("checkboxSquareBackground", -12345121);
      defaultColors.put("checkboxSquareCheck", var0);
      defaultColors.put("checkboxSquareUnchecked", -9211021);
      defaultColors.put("checkboxSquareDisabled", -5197648);
      defaultColors.put("listSelectorSDK21", 251658240);
      defaultColors.put("radioBackground", -5000269);
      defaultColors.put("radioBackgroundChecked", -13129232);
      defaultColors.put("windowBackgroundGray", -986896);
      HashMap var4 = defaultColors;
      Integer var43 = -16777216;
      var4.put("windowBackgroundGrayShadow", var43);
      defaultColors.put("emptyListPlaceholder", -6974059);
      defaultColors.put("divider", -2500135);
      defaultColors.put("graySection", -1117195);
      defaultColors.put("key_graySectionText", -8418927);
      defaultColors.put("contextProgressInner1", -4202506);
      defaultColors.put("contextProgressOuter1", -13920542);
      defaultColors.put("contextProgressInner2", -4202506);
      defaultColors.put("contextProgressOuter2", var0);
      defaultColors.put("contextProgressInner3", -5000269);
      defaultColors.put("contextProgressOuter3", var0);
      defaultColors.put("contextProgressInner4", -3486256);
      HashMap var5 = defaultColors;
      Integer var46 = -13683656;
      var5.put("contextProgressOuter4", var46);
      defaultColors.put("fastScrollActive", -11361317);
      defaultColors.put("fastScrollInactive", -3551791);
      defaultColors.put("fastScrollText", var0);
      defaultColors.put("avatar_text", var0);
      defaultColors.put("avatar_backgroundSaved", -10043398);
      defaultColors.put("avatar_backgroundArchived", -5654847);
      defaultColors.put("avatar_backgroundArchivedHidden", -3749428);
      defaultColors.put("avatar_backgroundRed", -1743531);
      defaultColors.put("avatar_backgroundOrange", -881592);
      defaultColors.put("avatar_backgroundViolet", -7436818);
      defaultColors.put("avatar_backgroundGreen", -8992691);
      defaultColors.put("avatar_backgroundCyan", -10502443);
      defaultColors.put("avatar_backgroundBlue", -11232035);
      defaultColors.put("avatar_backgroundPink", -887654);
      defaultColors.put("avatar_backgroundGroupCreateSpanBlue", -1642505);
      defaultColors.put("avatar_backgroundInProfileBlue", -11500111);
      defaultColors.put("avatar_backgroundActionBarBlue", -10907718);
      defaultColors.put("avatar_subtitleInProfileBlue", -2626822);
      defaultColors.put("avatar_actionBarSelectorBlue", -11959891);
      defaultColors.put("avatar_actionBarIconBlue", var0);
      defaultColors.put("avatar_nameInMessageRed", -3516848);
      defaultColors.put("avatar_nameInMessageOrange", -2589911);
      defaultColors.put("avatar_nameInMessageViolet", -11627828);
      defaultColors.put("avatar_nameInMessageGreen", -11488718);
      defaultColors.put("avatar_nameInMessageCyan", -13132104);
      defaultColors.put("avatar_nameInMessageBlue", -11627828);
      defaultColors.put("avatar_nameInMessagePink", -11627828);
      defaultColors.put("actionBarDefault", -11371101);
      defaultColors.put("actionBarDefaultIcon", var0);
      defaultColors.put("actionBarActionModeDefault", var0);
      defaultColors.put("actionBarActionModeDefaultTop", 268435456);
      defaultColors.put("actionBarActionModeDefaultIcon", -9999761);
      defaultColors.put("actionBarDefaultTitle", var0);
      defaultColors.put("actionBarDefaultSubtitle", -2758409);
      defaultColors.put("actionBarDefaultSelector", -12554860);
      defaultColors.put("actionBarWhiteSelector", 788529152);
      defaultColors.put("actionBarDefaultSearch", var0);
      defaultColors.put("actionBarDefaultSearchPlaceholder", -1996488705);
      defaultColors.put("actionBarDefaultSubmenuItem", var2);
      defaultColors.put("actionBarDefaultSubmenuItemIcon", -9999504);
      defaultColors.put("actionBarDefaultSubmenuBackground", var0);
      defaultColors.put("actionBarActionModeDefaultSelector", -986896);
      defaultColors.put("actionBarTabActiveText", var0);
      defaultColors.put("actionBarTabUnactiveText", -2758409);
      defaultColors.put("actionBarTabLine", var0);
      defaultColors.put("actionBarTabSelector", -12554860);
      defaultColors.put("actionBarDefaultArchived", -9471353);
      defaultColors.put("actionBarDefaultArchivedSelector", -10590350);
      defaultColors.put("actionBarDefaultArchivedIcon", var0);
      defaultColors.put("actionBarDefaultArchivedTitle", var0);
      defaultColors.put("actionBarDefaultArchivedSearch", var0);
      defaultColors.put("actionBarDefaultSearchArchivedPlaceholder", -1996488705);
      defaultColors.put("chats_onlineCircle", -11810020);
      defaultColors.put("chats_unreadCounter", -11613090);
      defaultColors.put("chats_unreadCounterMuted", -3749428);
      defaultColors.put("chats_unreadCounterText", var0);
      defaultColors.put("chats_archiveBackground", -10049056);
      defaultColors.put("chats_archivePinBackground", -6313293);
      defaultColors.put("chats_archiveIcon", var0);
      defaultColors.put("chats_archiveText", var0);
      defaultColors.put("chats_name", var2);
      defaultColors.put("chats_nameArchived", -11382190);
      defaultColors.put("chats_secretName", -16734706);
      defaultColors.put("chats_secretIcon", -15093466);
      defaultColors.put("chats_nameIcon", -14408668);
      defaultColors.put("chats_pinnedIcon", -5723992);
      defaultColors.put("chats_message", -7631473);
      defaultColors.put("chats_messageArchived", -7237231);
      defaultColors.put("chats_message_threeLines", -7434095);
      defaultColors.put("chats_draft", -2274503);
      defaultColors.put("chats_nameMessage", -12812624);
      defaultColors.put("chats_nameMessageArchived", -7631473);
      defaultColors.put("chats_nameMessage_threeLines", -12434359);
      defaultColors.put("chats_nameMessageArchived_threeLines", -10592674);
      defaultColors.put("chats_attachMessage", -12812624);
      defaultColors.put("chats_actionMessage", -12812624);
      defaultColors.put("chats_date", -6973028);
      defaultColors.put("chats_pinnedOverlay", 134217728);
      defaultColors.put("chats_tabletSelectedOverlay", 251658240);
      defaultColors.put("chats_sentCheck", -12146122);
      defaultColors.put("chats_sentClock", -9061026);
      defaultColors.put("chats_sentError", -2796974);
      defaultColors.put("chats_sentErrorIcon", var0);
      defaultColors.put("chats_verifiedBackground", -13391642);
      defaultColors.put("chats_verifiedCheck", var0);
      defaultColors.put("chats_muteIcon", -4341308);
      defaultColors.put("chats_mentionIcon", var0);
      defaultColors.put("chats_menuBackground", var0);
      defaultColors.put("chats_menuItemText", -12303292);
      defaultColors.put("chats_menuItemCheck", -10907718);
      defaultColors.put("chats_menuItemIcon", -7827048);
      defaultColors.put("chats_menuName", var0);
      defaultColors.put("chats_menuPhone", var0);
      defaultColors.put("chats_menuPhoneCats", -4004353);
      defaultColors.put("chats_menuCloud", var0);
      defaultColors.put("chats_menuCloudBackgroundCats", -12420183);
      defaultColors.put("chats_actionIcon", var0);
      defaultColors.put("chats_actionBackground", -10114592);
      defaultColors.put("chats_actionPressedBackground", -11100714);
      defaultColors.put("chats_actionUnreadIcon", -9211021);
      defaultColors.put("chats_actionUnreadBackground", var0);
      defaultColors.put("chats_actionUnreadPressedBackground", -855310);
      defaultColors.put("chats_menuTopBackgroundCats", -10907718);
      defaultColors.put("chat_attachCameraIcon1", -33488);
      defaultColors.put("chat_attachCameraIcon2", -1353648);
      defaultColors.put("chat_attachCameraIcon3", -12342798);
      defaultColors.put("chat_attachCameraIcon4", -4958752);
      defaultColors.put("chat_attachCameraIcon5", -10366879);
      defaultColors.put("chat_attachCameraIcon6", -81627);
      defaultColors.put("chat_attachMediaBanBackground", -12171706);
      defaultColors.put("chat_attachMediaBanText", var0);
      defaultColors.put("chat_attachGalleryBackground", -5997863);
      defaultColors.put("chat_attachGalleryIcon", var0);
      defaultColors.put("chat_attachVideoBackground", -1871495);
      defaultColors.put("chat_attachVideoIcon", var0);
      defaultColors.put("chat_attachAudioBackground", -620719);
      defaultColors.put("chat_attachAudioIcon", var0);
      defaultColors.put("chat_attachFileBackground", -13328140);
      defaultColors.put("chat_attachFileIcon", var0);
      defaultColors.put("chat_attachContactBackground", -12664838);
      defaultColors.put("chat_attachContactIcon", var0);
      defaultColors.put("chat_attachLocationBackground", -12597126);
      defaultColors.put("chat_attachLocationIcon", var0);
      defaultColors.put("chat_attachHideBackground", -5330248);
      defaultColors.put("chat_attachHideIcon", var0);
      defaultColors.put("chat_attachSendBackground", -12664838);
      defaultColors.put("chat_attachPollBackground", -670899);
      defaultColors.put("chat_attachPollIcon", var0);
      defaultColors.put("chat_status", -2758409);
      defaultColors.put("chat_inDownCall", -16725933);
      defaultColors.put("chat_inUpCall", -47032);
      defaultColors.put("chat_outUpCall", -16725933);
      defaultColors.put("chat_attachSendIcon", var0);
      defaultColors.put("chat_shareBackground", 1718783910);
      defaultColors.put("chat_shareBackgroundSelected", -1720545370);
      defaultColors.put("chat_lockIcon", var0);
      defaultColors.put("chat_muteIcon", -5124893);
      defaultColors.put("chat_inBubble", var0);
      defaultColors.put("chat_inBubbleSelected", -1247235);
      defaultColors.put("chat_inBubbleShadow", -14862509);
      defaultColors.put("chat_outBubble", -1048610);
      defaultColors.put("chat_outBubbleSelected", -2492475);
      defaultColors.put("chat_outBubbleShadow", -14781172);
      defaultColors.put("chat_inMediaIcon", var0);
      defaultColors.put("chat_inMediaIconSelected", -1050370);
      defaultColors.put("chat_outMediaIcon", -1048610);
      defaultColors.put("chat_outMediaIconSelected", -1967921);
      defaultColors.put("chat_messageTextIn", var43);
      defaultColors.put("chat_messageTextOut", var43);
      defaultColors.put("chat_messageLinkIn", -14255946);
      defaultColors.put("chat_messageLinkOut", -14255946);
      defaultColors.put("chat_serviceText", var0);
      defaultColors.put("chat_serviceLink", var0);
      defaultColors.put("chat_serviceIcon", var0);
      defaultColors.put("chat_mediaTimeBackground", 1711276032);
      defaultColors.put("chat_outSentCheck", -10637232);
      defaultColors.put("chat_outSentCheckSelected", -10637232);
      defaultColors.put("chat_outSentClock", -9061026);
      defaultColors.put("chat_outSentClockSelected", -9061026);
      HashMap var6 = defaultColors;
      Integer var51 = -6182221;
      var6.put("chat_inSentClock", var51);
      defaultColors.put("chat_inSentClockSelected", -7094838);
      defaultColors.put("chat_mediaSentCheck", var0);
      defaultColors.put("chat_mediaSentClock", var0);
      defaultColors.put("chat_inViews", var51);
      defaultColors.put("chat_inViewsSelected", -7094838);
      defaultColors.put("chat_outViews", -9522601);
      defaultColors.put("chat_outViewsSelected", -9522601);
      defaultColors.put("chat_mediaViews", var0);
      defaultColors.put("chat_inMenu", -4801083);
      defaultColors.put("chat_inMenuSelected", -6766130);
      defaultColors.put("chat_outMenu", -7221634);
      defaultColors.put("chat_outMenuSelected", -7221634);
      defaultColors.put("chat_mediaMenu", var0);
      HashMap var7 = defaultColors;
      Integer var52 = -11162801;
      var7.put("chat_outInstant", var52);
      defaultColors.put("chat_outInstantSelected", -12019389);
      defaultColors.put("chat_inInstant", var37);
      defaultColors.put("chat_inInstantSelected", -13600331);
      defaultColors.put("chat_sentError", -2411211);
      defaultColors.put("chat_sentErrorIcon", var0);
      defaultColors.put("chat_selectedBackground", 671781104);
      defaultColors.put("chat_previewDurationText", var0);
      defaultColors.put("chat_previewGameText", var0);
      defaultColors.put("chat_inPreviewInstantText", var37);
      defaultColors.put("chat_outPreviewInstantText", var52);
      defaultColors.put("chat_inPreviewInstantSelectedText", -13600331);
      defaultColors.put("chat_outPreviewInstantSelectedText", -12019389);
      defaultColors.put("chat_secretTimeText", -1776928);
      defaultColors.put("chat_stickerNameText", var0);
      defaultColors.put("chat_botButtonText", var0);
      defaultColors.put("chat_botProgress", var0);
      defaultColors.put("chat_inForwardedNameText", -13072697);
      defaultColors.put("chat_outForwardedNameText", var52);
      defaultColors.put("chat_inViaBotNameText", var37);
      defaultColors.put("chat_outViaBotNameText", var52);
      defaultColors.put("chat_stickerViaBotNameText", var0);
      defaultColors.put("chat_inReplyLine", -10903592);
      defaultColors.put("chat_outReplyLine", -9520791);
      defaultColors.put("chat_stickerReplyLine", var0);
      defaultColors.put("chat_inReplyNameText", var37);
      defaultColors.put("chat_outReplyNameText", var52);
      defaultColors.put("chat_stickerReplyNameText", var0);
      defaultColors.put("chat_inReplyMessageText", var43);
      defaultColors.put("chat_outReplyMessageText", var43);
      defaultColors.put("chat_inReplyMediaMessageText", var51);
      defaultColors.put("chat_outReplyMediaMessageText", -10112933);
      defaultColors.put("chat_inReplyMediaMessageSelectedText", -7752511);
      defaultColors.put("chat_outReplyMediaMessageSelectedText", -10112933);
      defaultColors.put("chat_stickerReplyMessageText", var0);
      defaultColors.put("chat_inPreviewLine", -9390872);
      defaultColors.put("chat_outPreviewLine", -7812741);
      defaultColors.put("chat_inSiteNameText", var37);
      defaultColors.put("chat_outSiteNameText", var52);
      defaultColors.put("chat_inContactNameText", -11625772);
      defaultColors.put("chat_outContactNameText", var52);
      defaultColors.put("chat_inContactPhoneText", var46);
      defaultColors.put("chat_inContactPhoneSelectedText", var46);
      defaultColors.put("chat_outContactPhoneText", -13286860);
      defaultColors.put("chat_outContactPhoneSelectedText", -13286860);
      defaultColors.put("chat_mediaProgress", var0);
      defaultColors.put("chat_inAudioProgress", var0);
      defaultColors.put("chat_outAudioProgress", -1048610);
      defaultColors.put("chat_inAudioSelectedProgress", -1050370);
      defaultColors.put("chat_outAudioSelectedProgress", -1967921);
      defaultColors.put("chat_mediaTimeText", var0);
      defaultColors.put("chat_inTimeText", var51);
      defaultColors.put("chat_outTimeText", -9391780);
      defaultColors.put("chat_adminText", -4143413);
      defaultColors.put("chat_adminSelectedText", -7752511);
      defaultColors.put("chat_inTimeSelectedText", -7752511);
      defaultColors.put("chat_outTimeSelectedText", -9391780);
      defaultColors.put("chat_inAudioPerfomerText", var46);
      defaultColors.put("chat_inAudioPerfomerSelectedText", var46);
      defaultColors.put("chat_outAudioPerfomerText", -13286860);
      defaultColors.put("chat_outAudioPerfomerSelectedText", -13286860);
      defaultColors.put("chat_inAudioTitleText", -11625772);
      defaultColors.put("chat_outAudioTitleText", var52);
      defaultColors.put("chat_inAudioDurationText", var51);
      defaultColors.put("chat_outAudioDurationText", -10112933);
      defaultColors.put("chat_inAudioDurationSelectedText", -7752511);
      defaultColors.put("chat_outAudioDurationSelectedText", -10112933);
      defaultColors.put("chat_inAudioSeekbar", -1774864);
      defaultColors.put("chat_inAudioCacheSeekbar", 1071966960);
      defaultColors.put("chat_outAudioSeekbar", -4463700);
      defaultColors.put("chat_outAudioCacheSeekbar", 1069278124);
      defaultColors.put("chat_inAudioSeekbarSelected", -4399384);
      defaultColors.put("chat_outAudioSeekbarSelected", -5644906);
      defaultColors.put("chat_inAudioSeekbarFill", -9259544);
      defaultColors.put("chat_outAudioSeekbarFill", -8863118);
      defaultColors.put("chat_inVoiceSeekbar", -2169365);
      defaultColors.put("chat_outVoiceSeekbar", -4463700);
      defaultColors.put("chat_inVoiceSeekbarSelected", -4399384);
      defaultColors.put("chat_outVoiceSeekbarSelected", -5644906);
      defaultColors.put("chat_inVoiceSeekbarFill", -9259544);
      defaultColors.put("chat_outVoiceSeekbarFill", -8863118);
      defaultColors.put("chat_inFileProgress", -1314571);
      defaultColors.put("chat_outFileProgress", -2427453);
      defaultColors.put("chat_inFileProgressSelected", -3413258);
      defaultColors.put("chat_outFileProgressSelected", -3806041);
      defaultColors.put("chat_inFileNameText", -11625772);
      defaultColors.put("chat_outFileNameText", var52);
      defaultColors.put("chat_inFileInfoText", var51);
      defaultColors.put("chat_outFileInfoText", -10112933);
      defaultColors.put("chat_inFileInfoSelectedText", -7752511);
      defaultColors.put("chat_outFileInfoSelectedText", -10112933);
      defaultColors.put("chat_inFileBackground", -1314571);
      defaultColors.put("chat_outFileBackground", -2427453);
      defaultColors.put("chat_inFileBackgroundSelected", -3413258);
      defaultColors.put("chat_outFileBackgroundSelected", -3806041);
      defaultColors.put("chat_inVenueInfoText", var51);
      defaultColors.put("chat_outVenueInfoText", -10112933);
      defaultColors.put("chat_inVenueInfoSelectedText", -7752511);
      defaultColors.put("chat_outVenueInfoSelectedText", -10112933);
      defaultColors.put("chat_mediaInfoText", var0);
      defaultColors.put("chat_linkSelectBackground", 862104035);
      defaultColors.put("chat_textSelectBackground", 1717742051);
      defaultColors.put("chat_emojiPanelBackground", -986379);
      defaultColors.put("chat_emojiPanelBadgeBackground", -11688214);
      defaultColors.put("chat_emojiPanelBadgeText", var0);
      defaultColors.put("chat_emojiSearchBackground", -1709586);
      defaultColors.put("chat_emojiSearchIcon", -7036497);
      defaultColors.put("chat_emojiPanelShadowLine", 301989888);
      defaultColors.put("chat_emojiPanelEmptyText", -7038047);
      defaultColors.put("chat_emojiPanelIcon", -6445909);
      defaultColors.put("chat_emojiBottomPanelIcon", -7564905);
      defaultColors.put("chat_emojiPanelIconSelected", -13920286);
      defaultColors.put("chat_emojiPanelStickerPackSelector", -1907225);
      defaultColors.put("chat_emojiPanelStickerPackSelectorLine", -11097104);
      defaultColors.put("chat_emojiPanelBackspace", -7564905);
      defaultColors.put("chat_emojiPanelMasksIcon", var0);
      defaultColors.put("chat_emojiPanelMasksIconSelected", -10305560);
      defaultColors.put("chat_emojiPanelTrendingTitle", var2);
      defaultColors.put("chat_emojiPanelStickerSetName", -8221804);
      defaultColors.put("chat_emojiPanelStickerSetNameHighlight", -14184997);
      defaultColors.put("chat_emojiPanelStickerSetNameIcon", -5130564);
      defaultColors.put("chat_emojiPanelTrendingDescription", -7697782);
      defaultColors.put("chat_botKeyboardButtonText", -13220017);
      defaultColors.put("chat_botKeyboardButtonBackground", -1775639);
      defaultColors.put("chat_botKeyboardButtonBackgroundPressed", -3354156);
      defaultColors.put("chat_unreadMessagesStartArrowIcon", -6113849);
      defaultColors.put("chat_unreadMessagesStartText", -11102772);
      defaultColors.put("chat_unreadMessagesStartBackground", var0);
      defaultColors.put("chat_inFileIcon", -6113849);
      defaultColors.put("chat_inFileSelectedIcon", -7883067);
      defaultColors.put("chat_outFileIcon", -8011912);
      defaultColors.put("chat_outFileSelectedIcon", -8011912);
      defaultColors.put("chat_inLocationBackground", -1314571);
      defaultColors.put("chat_inLocationIcon", -6113849);
      defaultColors.put("chat_outLocationBackground", -2427453);
      defaultColors.put("chat_outLocationIcon", -7880840);
      defaultColors.put("chat_inContactBackground", -9259544);
      defaultColors.put("chat_inContactIcon", var0);
      defaultColors.put("chat_outContactBackground", -8863118);
      defaultColors.put("chat_outContactIcon", -1048610);
      defaultColors.put("chat_outBroadcast", -12146122);
      defaultColors.put("chat_mediaBroadcast", var0);
      defaultColors.put("chat_searchPanelIcons", -9999761);
      defaultColors.put("chat_searchPanelText", -9999761);
      defaultColors.put("chat_secretChatStatusText", -8421505);
      defaultColors.put("chat_fieldOverlayText", var37);
      defaultColors.put("chat_stickersHintPanel", var0);
      defaultColors.put("chat_replyPanelIcons", -11032346);
      defaultColors.put("chat_replyPanelClose", -7432805);
      defaultColors.put("chat_replyPanelName", var37);
      defaultColors.put("chat_replyPanelMessage", var2);
      defaultColors.put("chat_replyPanelLine", -1513240);
      defaultColors.put("chat_messagePanelBackground", var0);
      defaultColors.put("chat_messagePanelText", var43);
      defaultColors.put("chat_messagePanelHint", -5985101);
      defaultColors.put("chat_messagePanelShadow", var43);
      defaultColors.put("chat_messagePanelIcons", -7432805);
      defaultColors.put("chat_recordedVoicePlayPause", var0);
      defaultColors.put("chat_recordedVoicePlayPausePressed", -2495749);
      defaultColors.put("chat_recordedVoiceDot", -2468275);
      defaultColors.put("chat_recordedVoiceBackground", -11165981);
      defaultColors.put("chat_recordedVoiceProgress", -6107400);
      defaultColors.put("chat_recordedVoiceProgressInner", var0);
      defaultColors.put("chat_recordVoiceCancel", -6710887);
      defaultColors.put("chat_messagePanelSend", -10309397);
      defaultColors.put("key_chat_messagePanelVoiceLock", -5987164);
      defaultColors.put("key_chat_messagePanelVoiceLockBackground", var0);
      defaultColors.put("key_chat_messagePanelVoiceLockShadow", var43);
      defaultColors.put("chat_recordTime", -11711413);
      defaultColors.put("chat_emojiPanelNewTrending", -11688214);
      defaultColors.put("chat_gifSaveHintText", var0);
      defaultColors.put("chat_gifSaveHintBackground", -871296751);
      defaultColors.put("chat_goDownButton", var0);
      defaultColors.put("chat_goDownButtonShadow", var43);
      defaultColors.put("chat_goDownButtonIcon", -7432805);
      defaultColors.put("chat_goDownButtonCounter", var0);
      defaultColors.put("chat_goDownButtonCounterBackground", -11689240);
      defaultColors.put("chat_messagePanelCancelInlineBot", -5395027);
      defaultColors.put("chat_messagePanelVoicePressed", var0);
      defaultColors.put("chat_messagePanelVoiceBackground", -11037236);
      defaultColors.put("chat_messagePanelVoiceShadow", 218103808);
      defaultColors.put("chat_messagePanelVoiceDelete", -9211021);
      defaultColors.put("chat_messagePanelVoiceDuration", var0);
      defaultColors.put("chat_inlineResultIcon", -11037236);
      defaultColors.put("chat_topPanelBackground", var0);
      defaultColors.put("chat_topPanelClose", -5723992);
      defaultColors.put("chat_topPanelLine", -9658414);
      defaultColors.put("chat_topPanelTitle", var37);
      defaultColors.put("chat_topPanelMessage", -6710887);
      defaultColors.put("chat_reportSpam", -3188393);
      defaultColors.put("chat_addContact", -11894091);
      defaultColors.put("chat_inLoader", -9259544);
      defaultColors.put("chat_inLoaderSelected", -10114080);
      defaultColors.put("chat_outLoader", -8863118);
      defaultColors.put("chat_outLoaderSelected", -9783964);
      defaultColors.put("chat_inLoaderPhoto", -6113080);
      defaultColors.put("chat_inLoaderPhotoSelected", -6113849);
      defaultColors.put("chat_inLoaderPhotoIcon", -197380);
      defaultColors.put("chat_inLoaderPhotoIconSelected", -1314571);
      defaultColors.put("chat_outLoaderPhoto", -8011912);
      defaultColors.put("chat_outLoaderPhotoSelected", -8538000);
      defaultColors.put("chat_outLoaderPhotoIcon", -2427453);
      defaultColors.put("chat_outLoaderPhotoIconSelected", -4134748);
      defaultColors.put("chat_mediaLoaderPhoto", 1711276032);
      defaultColors.put("chat_mediaLoaderPhotoSelected", 2130706432);
      defaultColors.put("chat_mediaLoaderPhotoIcon", var0);
      defaultColors.put("chat_mediaLoaderPhotoIconSelected", -2500135);
      defaultColors.put("chat_secretTimerBackground", -868326258);
      defaultColors.put("chat_secretTimerText", var0);
      defaultColors.put("profile_creatorIcon", -12937771);
      defaultColors.put("profile_actionIcon", -8288630);
      defaultColors.put("profile_actionBackground", var0);
      defaultColors.put("profile_actionPressedBackground", -855310);
      defaultColors.put("profile_verifiedBackground", -5056776);
      defaultColors.put("profile_verifiedCheck", -11959368);
      defaultColors.put("profile_title", var0);
      defaultColors.put("profile_status", -2626822);
      defaultColors.put("player_actionBar", var0);
      defaultColors.put("player_actionBarSelector", 251658240);
      defaultColors.put("player_actionBarTitle", var46);
      defaultColors.put("player_actionBarTop", -1728053248);
      defaultColors.put("player_actionBarSubtitle", -7697782);
      defaultColors.put("player_actionBarItems", -7697782);
      defaultColors.put("player_background", var0);
      defaultColors.put("player_time", -7564650);
      defaultColors.put("player_progressBackground", -1445899);
      defaultColors.put("key_player_progressCachedBackground", -1445899);
      defaultColors.put("player_progress", -11821085);
      defaultColors.put("player_placeholder", -5723992);
      defaultColors.put("player_placeholderBackground", -986896);
      defaultColors.put("player_button", -13421773);
      defaultColors.put("player_buttonActive", -11753238);
      defaultColors.put("key_sheet_scrollUp", -1973016);
      defaultColors.put("key_sheet_other", -3551789);
      defaultColors.put("files_folderIcon", -6710887);
      defaultColors.put("files_folderIconBackground", -986896);
      defaultColors.put("files_iconText", var0);
      defaultColors.put("sessions_devicesImage", -6908266);
      defaultColors.put("passport_authorizeBackground", -12211217);
      defaultColors.put("passport_authorizeBackgroundSelected", -12542501);
      defaultColors.put("passport_authorizeText", var0);
      defaultColors.put("location_markerX", -8355712);
      defaultColors.put("location_sendLocationBackground", -9592620);
      defaultColors.put("location_sendLiveLocationBackground", -39836);
      defaultColors.put("location_sendLocationIcon", var0);
      defaultColors.put("location_sendLiveLocationIcon", var0);
      defaultColors.put("location_liveLocationProgress", -13262875);
      defaultColors.put("location_placeLocationBackground", -11753238);
      defaultColors.put("dialog_liveLocationProgress", -13262875);
      defaultColors.put("calls_callReceivedGreenIcon", -16725933);
      defaultColors.put("calls_callReceivedRedIcon", -47032);
      defaultColors.put("featuredStickers_addedIcon", -11491093);
      defaultColors.put("featuredStickers_buttonProgress", var0);
      defaultColors.put("featuredStickers_addButton", -11491093);
      defaultColors.put("featuredStickers_addButtonPressed", -12346402);
      defaultColors.put("featuredStickers_delButton", -2533545);
      defaultColors.put("featuredStickers_delButtonPressed", -3782327);
      defaultColors.put("featuredStickers_buttonText", var0);
      defaultColors.put("featuredStickers_unread", -11688214);
      defaultColors.put("inappPlayerPerformer", var46);
      defaultColors.put("inappPlayerTitle", var46);
      defaultColors.put("inappPlayerBackground", var0);
      defaultColors.put("inappPlayerPlayPause", -10309397);
      defaultColors.put("inappPlayerClose", -5723992);
      defaultColors.put("returnToCallBackground", -12279325);
      defaultColors.put("returnToCallText", var0);
      defaultColors.put("sharedMedia_startStopLoadIcon", -13196562);
      defaultColors.put("sharedMedia_linkPlaceholder", -986123);
      defaultColors.put("sharedMedia_linkPlaceholderText", -4735293);
      defaultColors.put("sharedMedia_photoPlaceholder", -1182729);
      defaultColors.put("sharedMedia_actionMode", -12154957);
      defaultColors.put("checkbox", -10567099);
      defaultColors.put("checkboxCheck", var0);
      defaultColors.put("checkboxDisabled", -5195326);
      defaultColors.put("stickers_menu", -4801083);
      defaultColors.put("stickers_menuSelector", 251658240);
      defaultColors.put("changephoneinfo_image", -5723992);
      defaultColors.put("key_changephoneinfo_changeText", -11697229);
      defaultColors.put("groupcreate_hintText", var51);
      defaultColors.put("groupcreate_cursor", -11361317);
      defaultColors.put("groupcreate_sectionShadow", var43);
      defaultColors.put("groupcreate_sectionText", -8617336);
      defaultColors.put("groupcreate_spanText", var2);
      defaultColors.put("groupcreate_spanBackground", -855310);
      defaultColors.put("groupcreate_spanDelete", var0);
      defaultColors.put("contacts_inviteBackground", -11157919);
      defaultColors.put("contacts_inviteText", var0);
      defaultColors.put("login_progressInner", -1971470);
      defaultColors.put("login_progressOuter", -10313520);
      defaultColors.put("musicPicker_checkbox", -14043401);
      defaultColors.put("musicPicker_checkboxCheck", var0);
      defaultColors.put("musicPicker_buttonBackground", -10702870);
      defaultColors.put("musicPicker_buttonIcon", var0);
      defaultColors.put("picker_enabledButton", -15095832);
      defaultColors.put("picker_disabledButton", -6710887);
      defaultColors.put("picker_badge", -14043401);
      defaultColors.put("picker_badgeText", var0);
      defaultColors.put("chat_botSwitchToInlineText", -12348980);
      defaultColors.put("undo_background", -366530760);
      defaultColors.put("undo_cancelColor", -8008961);
      defaultColors.put("undo_infoColor", var0);
      fallbackKeys.put("chat_adminText", "chat_inTimeText");
      fallbackKeys.put("chat_adminSelectedText", "chat_inTimeSelectedText");
      fallbackKeys.put("key_player_progressCachedBackground", "player_progressBackground");
      fallbackKeys.put("chat_inAudioCacheSeekbar", "chat_inAudioSeekbar");
      fallbackKeys.put("chat_outAudioCacheSeekbar", "chat_outAudioSeekbar");
      fallbackKeys.put("chat_emojiSearchBackground", "chat_emojiPanelStickerPackSelector");
      fallbackKeys.put("location_sendLiveLocationIcon", "location_sendLocationIcon");
      fallbackKeys.put("key_changephoneinfo_changeText", "windowBackgroundWhiteBlueText4");
      fallbackKeys.put("key_graySectionText", "windowBackgroundWhiteGrayText2");
      fallbackKeys.put("chat_inMediaIcon", "chat_inBubble");
      fallbackKeys.put("chat_outMediaIcon", "chat_outBubble");
      fallbackKeys.put("chat_inMediaIconSelected", "chat_inBubbleSelected");
      fallbackKeys.put("chat_outMediaIconSelected", "chat_outBubbleSelected");
      fallbackKeys.put("chats_actionUnreadIcon", "profile_actionIcon");
      fallbackKeys.put("chats_actionUnreadBackground", "profile_actionBackground");
      fallbackKeys.put("chats_actionUnreadPressedBackground", "profile_actionPressedBackground");
      fallbackKeys.put("dialog_inlineProgressBackground", "windowBackgroundGray");
      fallbackKeys.put("dialog_inlineProgress", "chats_menuItemIcon");
      fallbackKeys.put("groupcreate_spanDelete", "chats_actionIcon");
      fallbackKeys.put("sharedMedia_photoPlaceholder", "windowBackgroundGray");
      fallbackKeys.put("chat_attachPollBackground", "chat_attachAudioBackground");
      fallbackKeys.put("chat_attachPollIcon", "chat_attachAudioIcon");
      fallbackKeys.put("chats_onlineCircle", "windowBackgroundWhiteBlueText");
      fallbackKeys.put("windowBackgroundWhiteBlueButton", "windowBackgroundWhiteValueText");
      fallbackKeys.put("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteValueText");
      fallbackKeys.put("undo_background", "chat_gifSaveHintBackground");
      fallbackKeys.put("undo_cancelColor", "chat_gifSaveHintText");
      fallbackKeys.put("undo_infoColor", "chat_gifSaveHintText");
      fallbackKeys.put("windowBackgroundUnchecked", "windowBackgroundWhite");
      fallbackKeys.put("windowBackgroundChecked", "windowBackgroundWhite");
      fallbackKeys.put("switchTrackBlue", "switchTrack");
      fallbackKeys.put("switchTrackBlueChecked", "switchTrackChecked");
      fallbackKeys.put("switchTrackBlueThumb", "windowBackgroundWhite");
      fallbackKeys.put("switchTrackBlueThumbChecked", "windowBackgroundWhite");
      fallbackKeys.put("windowBackgroundCheckText", "windowBackgroundWhiteBlackText");
      fallbackKeys.put("contextProgressInner4", "contextProgressInner1");
      fallbackKeys.put("contextProgressOuter4", "contextProgressOuter1");
      fallbackKeys.put("switchTrackBlueSelector", "listSelectorSDK21");
      fallbackKeys.put("switchTrackBlueSelectorChecked", "listSelectorSDK21");
      fallbackKeys.put("chat_emojiBottomPanelIcon", "chat_emojiPanelIcon");
      fallbackKeys.put("chat_emojiSearchIcon", "chat_emojiPanelIcon");
      fallbackKeys.put("chat_emojiPanelStickerSetNameHighlight", "windowBackgroundWhiteBlueText4");
      fallbackKeys.put("chat_emojiPanelStickerPackSelectorLine", "chat_emojiPanelIconSelected");
      fallbackKeys.put("sharedMedia_actionMode", "actionBarDefault");
      fallbackKeys.put("key_sheet_scrollUp", "chat_emojiPanelStickerPackSelector");
      fallbackKeys.put("key_sheet_other", "player_actionBarItems");
      fallbackKeys.put("dialogSearchBackground", "chat_emojiPanelStickerPackSelector");
      fallbackKeys.put("dialogSearchHint", "chat_emojiPanelIcon");
      fallbackKeys.put("dialogSearchIcon", "chat_emojiPanelIcon");
      fallbackKeys.put("dialogSearchText", "windowBackgroundWhiteBlackText");
      fallbackKeys.put("dialogFloatingButton", "dialogRoundCheckBox");
      fallbackKeys.put("dialogFloatingButtonPressed", "dialogRoundCheckBox");
      fallbackKeys.put("dialogFloatingIcon", "dialogRoundCheckBoxCheck");
      fallbackKeys.put("dialogShadowLine", "chat_emojiPanelShadowLine");
      fallbackKeys.put("actionBarDefaultArchived", "actionBarDefault");
      fallbackKeys.put("actionBarDefaultArchivedSelector", "actionBarDefaultSelector");
      fallbackKeys.put("actionBarDefaultArchivedIcon", "actionBarDefaultIcon");
      fallbackKeys.put("actionBarDefaultArchivedTitle", "actionBarDefaultTitle");
      fallbackKeys.put("actionBarDefaultArchivedSearch", "actionBarDefaultSearch");
      fallbackKeys.put("actionBarDefaultSearchArchivedPlaceholder", "actionBarDefaultSearchPlaceholder");
      fallbackKeys.put("chats_message_threeLines", "chats_message");
      fallbackKeys.put("chats_nameMessage_threeLines", "chats_nameMessage");
      fallbackKeys.put("chats_nameArchived", "chats_name");
      fallbackKeys.put("chats_nameMessageArchived", "chats_nameMessage");
      fallbackKeys.put("chats_nameMessageArchived_threeLines", "chats_nameMessage");
      fallbackKeys.put("chats_messageArchived", "chats_message");
      fallbackKeys.put("avatar_backgroundArchived", "chats_unreadCounterMuted");
      fallbackKeys.put("avatar_backgroundArchivedHidden", "chats_unreadCounterMuted");
      fallbackKeys.put("chats_archiveBackground", "chats_actionBackground");
      fallbackKeys.put("chats_archivePinBackground", "chats_unreadCounterMuted");
      fallbackKeys.put("chats_archiveIcon", "chats_actionIcon");
      fallbackKeys.put("chats_archiveText", "chats_actionIcon");
      fallbackKeys.put("actionBarDefaultSubmenuItemIcon", "dialogIcon");
      fallbackKeys.put("checkboxDisabled", "chats_unreadCounterMuted");
      fallbackKeys.put("chat_status", "actionBarDefaultSubtitle");
      fallbackKeys.put("chat_inDownCall", "calls_callReceivedGreenIcon");
      fallbackKeys.put("chat_inUpCall", "calls_callReceivedRedIcon");
      fallbackKeys.put("chat_outUpCall", "calls_callReceivedGreenIcon");
      fallbackKeys.put("actionBarTabActiveText", "actionBarDefaultTitle");
      fallbackKeys.put("actionBarTabUnactiveText", "actionBarDefaultSubtitle");
      fallbackKeys.put("actionBarTabLine", "actionBarDefaultTitle");
      fallbackKeys.put("actionBarTabSelector", "actionBarDefaultSelector");
      fallbackKeys.put("profile_status", "avatar_subtitleInProfileBlue");
      fallbackKeys.put("chats_menuTopBackgroundCats", "avatar_backgroundActionBarBlue");
      themes = new ArrayList();
      otherThemes = new ArrayList();
      themesDict = new HashMap();
      currentColors = new HashMap();
      Theme.ThemeInfo var35 = new Theme.ThemeInfo();
      var35.name = "Default";
      var35.previewBackgroundColor = -3155485;
      var35.previewInColor = -1;
      var35.previewOutColor = -983328;
      var35.sortIndex = 0;
      ArrayList var40 = themes;
      defaultTheme = var35;
      currentTheme = var35;
      currentDayTheme = var35;
      var40.add(var35);
      themesDict.put("Default", defaultTheme);
      Theme.ThemeInfo var42 = new Theme.ThemeInfo();
      var42.name = "Dark";
      var42.assetName = "dark.attheme";
      var42.previewBackgroundColor = -10855071;
      var42.previewInColor = -9143676;
      var42.previewOutColor = -8214301;
      var42.sortIndex = 3;
      themes.add(var42);
      themesDict.put("Dark", var42);
      var42 = new Theme.ThemeInfo();
      var42.name = "Blue";
      var42.assetName = "bluebubbles.attheme";
      var42.previewBackgroundColor = -6963476;
      var42.previewInColor = -1;
      var42.previewOutColor = -3086593;
      var42.sortIndex = 1;
      themes.add(var42);
      themesDict.put("Blue", var42);
      var35 = new Theme.ThemeInfo();
      var35.name = "Dark Blue";
      var35.assetName = "darkblue.attheme";
      var35.previewBackgroundColor = -10523006;
      var35.previewInColor = -9009508;
      var35.previewOutColor = -8214301;
      var35.sortIndex = 2;
      themes.add(var35);
      HashMap var44 = themesDict;
      currentNightTheme = var35;
      var44.put("Dark Blue", var35);
      if (BuildVars.DEBUG_VERSION) {
         var42 = new Theme.ThemeInfo();
         var42.name = "Graphite";
         var42.assetName = "graphite.attheme";
         var42.previewBackgroundColor = -8749431;
         var42.previewInColor = -6775901;
         var42.previewOutColor = -5980167;
         var42.sortIndex = 4;
         themes.add(var42);
         themesDict.put("Graphite", var42);
      }

      var42 = new Theme.ThemeInfo();
      var42.name = "Arctic Blue";
      var42.assetName = "arctic.attheme";
      var42.previewBackgroundColor = -1;
      var42.previewInColor = -1315084;
      var42.previewOutColor = -8604930;
      var42.sortIndex = 5;
      themes.add(var42);
      themesDict.put("Arctic Blue", var42);
      SharedPreferences var47 = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
      String var36 = var47.getString("themes2", (String)null);
      int var8;
      Exception var10000;
      boolean var10001;
      Theme.ThemeInfo var38;
      Exception var50;
      if (!TextUtils.isEmpty(var36)) {
         label239: {
            label244: {
               JSONArray var48;
               try {
                  var48 = new JSONArray(var36);
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label244;
               }

               var8 = 0;

               while(true) {
                  try {
                     if (var8 >= var48.length()) {
                        break label239;
                     }

                     var35 = Theme.ThemeInfo.createWithJson(var48.getJSONObject(var8));
                  } catch (Exception var33) {
                     var10000 = var33;
                     var10001 = false;
                     break;
                  }

                  if (var35 != null) {
                     try {
                        otherThemes.add(var35);
                        themes.add(var35);
                        themesDict.put(var35.name, var35);
                     } catch (Exception var32) {
                        var10000 = var32;
                        var10001 = false;
                        break;
                     }
                  }

                  ++var8;
               }
            }

            var50 = var10000;
            FileLog.e((Throwable)var50);
         }
      } else {
         var36 = var47.getString("themes", (String)null);
         if (!TextUtils.isEmpty(var36)) {
            String[] var39 = var36.split("&");

            for(var8 = 0; var8 < var39.length; ++var8) {
               var38 = Theme.ThemeInfo.createWithString(var39[var8]);
               if (var38 != null) {
                  otherThemes.add(var38);
                  themes.add(var38);
                  themesDict.put(var38.name, var38);
               }
            }
         }

         saveOtherThemes();
         var47.edit().remove("themes").commit();
      }

      sortThemes();
      var1 = null;
      var42 = null;
      var35 = var1;

      label214: {
         label248: {
            SharedPreferences var45;
            try {
               var45 = MessagesController.getGlobalMainSettings();
            } catch (Exception var31) {
               var10000 = var31;
               var10001 = false;
               break label248;
            }

            var35 = var1;

            String var49;
            try {
               var49 = var45.getString("theme", (String)null);
            } catch (Exception var30) {
               var10000 = var30;
               var10001 = false;
               break label248;
            }

            if (var49 != null) {
               var35 = var1;

               try {
                  var42 = (Theme.ThemeInfo)themesDict.get(var49);
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label248;
               }
            }

            var35 = var42;

            String var41;
            try {
               var41 = var45.getString("nighttheme", (String)null);
            } catch (Exception var28) {
               var10000 = var28;
               var10001 = false;
               break label248;
            }

            if (var41 != null) {
               var35 = var42;

               try {
                  var38 = (Theme.ThemeInfo)themesDict.get(var41);
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label248;
               }

               if (var38 != null) {
                  var35 = var42;

                  try {
                     currentNightTheme = var38;
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label248;
                  }
               }
            }

            var35 = var42;

            try {
               selectedAutoNightType = var45.getInt("selectedAutoNightType", 0);
            } catch (Exception var25) {
               var10000 = var25;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            try {
               autoNightScheduleByLocation = var45.getBoolean("autoNightScheduleByLocation", false);
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            try {
               autoNightBrighnessThreshold = var45.getFloat("autoNightBrighnessThreshold", 0.25F);
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            try {
               autoNightDayStartTime = var45.getInt("autoNightDayStartTime", 1320);
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            try {
               autoNightDayEndTime = var45.getInt("autoNightDayEndTime", 480);
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            try {
               autoNightSunsetTime = var45.getInt("autoNightSunsetTime", 1320);
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            try {
               autoNightSunriseTime = var45.getInt("autoNightSunriseTime", 480);
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            try {
               autoNightCityName = var45.getString("autoNightCityName", "");
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label248;
            }

            var35 = var42;

            long var9;
            try {
               var9 = var45.getLong("autoNightLocationLatitude3", 10000L);
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label248;
            }

            if (var9 != 10000L) {
               var35 = var42;

               try {
                  autoNightLocationLatitude = Double.longBitsToDouble(var9);
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label248;
               }
            } else {
               var35 = var42;

               try {
                  autoNightLocationLatitude = 10000.0D;
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label248;
               }
            }

            var35 = var42;

            try {
               var9 = var45.getLong("autoNightLocationLongitude3", 10000L);
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label248;
            }

            if (var9 != 10000L) {
               var35 = var42;

               try {
                  autoNightLocationLongitude = Double.longBitsToDouble(var9);
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label248;
               }
            } else {
               var35 = var42;

               try {
                  autoNightLocationLongitude = 10000.0D;
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label248;
               }
            }

            var35 = var42;

            try {
               autoNightLastSunCheckDay = var45.getInt("autoNightLastSunCheckDay", -1);
               break label214;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
            }
         }

         var50 = var10000;
         FileLog.e((Throwable)var50);
         var42 = var35;
      }

      if (var42 == null) {
         var42 = defaultTheme;
      } else {
         currentDayTheme = var42;
      }

      applyTheme(var42, false, false, false);
      AndroidUtilities.runOnUIThread(_$$Lambda$RQB0Jwr1FTqp6hrbGUHuOs_9k1I.INSTANCE);
      ambientSensorListener = new SensorEventListener() {
         public void onAccuracyChanged(Sensor var1, int var2) {
         }

         public void onSensorChanged(SensorEvent var1) {
            float var2 = var1.values[0];
            float var3 = var2;
            if (var2 <= 0.0F) {
               var3 = 0.1F;
            }

            if (!ApplicationLoader.mainInterfacePaused && ApplicationLoader.isScreenOn) {
               if (var3 > 500.0F) {
                  Theme.lastBrightnessValue = 1.0F;
               } else {
                  Theme.lastBrightnessValue = (float)Math.ceil(Math.log((double)var3) * 9.932299613952637D + 27.05900001525879D) / 100.0F;
               }

               if (Theme.lastBrightnessValue <= Theme.autoNightBrighnessThreshold) {
                  if (!MediaController.getInstance().isRecordingOrListeningByProximity()) {
                     if (Theme.switchDayRunnableScheduled) {
                        Theme.switchDayRunnableScheduled = false;
                        AndroidUtilities.cancelRunOnUIThread(Theme.switchDayBrightnessRunnable);
                     }

                     if (!Theme.switchNightRunnableScheduled) {
                        Theme.switchNightRunnableScheduled = true;
                        AndroidUtilities.runOnUIThread(Theme.switchNightBrightnessRunnable, Theme.getAutoNightSwitchThemeDelay());
                     }
                  }
               } else {
                  if (Theme.switchNightRunnableScheduled) {
                     Theme.switchNightRunnableScheduled = false;
                     AndroidUtilities.cancelRunOnUIThread(Theme.switchNightBrightnessRunnable);
                  }

                  if (!Theme.switchDayRunnableScheduled) {
                     Theme.switchDayRunnableScheduled = true;
                     AndroidUtilities.runOnUIThread(Theme.switchDayBrightnessRunnable, Theme.getAutoNightSwitchThemeDelay());
                  }
               }
            }

         }
      };
   }

   public static void applyChatServiceMessageColor() {
      applyChatServiceMessageColor((int[])null);
   }

   public static void applyChatServiceMessageColor(int[] var0) {
      if (chat_actionBackgroundPaint != null) {
         serviceMessageColor = serviceMessageColorBackup;
         serviceSelectedMessageColor = serviceSelectedMessageColorBackup;
         int var1 = 0;
         Integer var2;
         Integer var3;
         Integer var5;
         if (var0 != null && var0.length >= 2) {
            var2 = var0[0];
            var3 = var0[1];
            serviceMessageColor = var0[0];
            serviceSelectedMessageColor = var0[1];
            var5 = var3;
         } else {
            var2 = (Integer)currentColors.get("chat_serviceBackground");
            var5 = (Integer)currentColors.get("chat_serviceBackgroundSelected");
         }

         if (var2 == null) {
            var2 = serviceMessageColor;
            var3 = serviceMessage2Color;
         } else {
            var3 = var2;
         }

         Integer var4;
         if (var5 == null) {
            var5 = serviceSelectedMessageColor;
            var4 = serviceSelectedMessage2Color;
         } else {
            var4 = var5;
         }

         if (currentColor != var2) {
            chat_actionBackgroundPaint.setColor(var2);
            colorFilter = new PorterDuffColorFilter(var2, Mode.MULTIPLY);
            colorFilter2 = new PorterDuffColorFilter(var3, Mode.MULTIPLY);
            currentColor = var2;
            if (chat_cornerOuter[0] != null) {
               while(var1 < 4) {
                  chat_cornerOuter[var1].setColorFilter(colorFilter);
                  chat_cornerInner[var1].setColorFilter(colorFilter);
                  ++var1;
               }
            }
         }

         if (currentSelectedColor != var5) {
            currentSelectedColor = var5;
            colorPressedFilter = new PorterDuffColorFilter(var5, Mode.MULTIPLY);
            colorPressedFilter2 = new PorterDuffColorFilter(var4, Mode.MULTIPLY);
         }

      }
   }

   public static void applyChatTheme(boolean var0) {
      if (chat_msgTextPaint != null) {
         if (chat_msgInDrawable != null && !var0) {
            chat_gamePaint.setColor(getColor("chat_previewGameText"));
            chat_durationPaint.setColor(getColor("chat_previewDurationText"));
            chat_botButtonPaint.setColor(getColor("chat_botButtonText"));
            chat_urlPaint.setColor(getColor("chat_linkSelectBackground"));
            chat_botProgressPaint.setColor(getColor("chat_botProgress"));
            chat_deleteProgressPaint.setColor(getColor("chat_secretTimeText"));
            chat_textSearchSelectionPaint.setColor(getColor("chat_textSelectBackground"));
            chat_msgErrorPaint.setColor(getColor("chat_sentError"));
            chat_statusPaint.setColor(getColor("chat_status"));
            chat_statusRecordPaint.setColor(getColor("chat_status"));
            chat_actionTextPaint.setColor(getColor("chat_serviceText"));
            chat_actionTextPaint.linkColor = getColor("chat_serviceLink");
            chat_contextResult_titleTextPaint.setColor(getColor("windowBackgroundWhiteBlackText"));
            chat_composeBackgroundPaint.setColor(getColor("chat_messagePanelBackground"));
            chat_timeBackgroundPaint.setColor(getColor("chat_mediaTimeBackground"));
            setDrawableColorByKey(chat_msgNoSoundDrawable, "chat_mediaTimeText");
            setDrawableColorByKey(chat_msgInDrawable, "chat_inBubble");
            setDrawableColorByKey(chat_msgInSelectedDrawable, "chat_inBubbleSelected");
            setDrawableColorByKey(chat_msgInShadowDrawable, "chat_inBubbleShadow");
            setDrawableColorByKey(chat_msgOutDrawable, "chat_outBubble");
            setDrawableColorByKey(chat_msgOutSelectedDrawable, "chat_outBubbleSelected");
            setDrawableColorByKey(chat_msgOutShadowDrawable, "chat_outBubbleShadow");
            setDrawableColorByKey(chat_msgInMediaDrawable, "chat_inBubble");
            setDrawableColorByKey(chat_msgInMediaSelectedDrawable, "chat_inBubbleSelected");
            setDrawableColorByKey(chat_msgInMediaShadowDrawable, "chat_inBubbleShadow");
            setDrawableColorByKey(chat_msgOutMediaDrawable, "chat_outBubble");
            setDrawableColorByKey(chat_msgOutMediaSelectedDrawable, "chat_outBubbleSelected");
            setDrawableColorByKey(chat_msgOutMediaShadowDrawable, "chat_outBubbleShadow");
            setDrawableColorByKey(chat_msgOutCheckDrawable, "chat_outSentCheck");
            setDrawableColorByKey(chat_msgOutCheckSelectedDrawable, "chat_outSentCheckSelected");
            setDrawableColorByKey(chat_msgOutHalfCheckDrawable, "chat_outSentCheck");
            setDrawableColorByKey(chat_msgOutHalfCheckSelectedDrawable, "chat_outSentCheckSelected");
            setDrawableColorByKey(chat_msgOutClockDrawable, "chat_outSentClock");
            setDrawableColorByKey(chat_msgOutSelectedClockDrawable, "chat_outSentClockSelected");
            setDrawableColorByKey(chat_msgInClockDrawable, "chat_inSentClock");
            setDrawableColorByKey(chat_msgInSelectedClockDrawable, "chat_inSentClockSelected");
            setDrawableColorByKey(chat_msgMediaCheckDrawable, "chat_mediaSentCheck");
            setDrawableColorByKey(chat_msgMediaHalfCheckDrawable, "chat_mediaSentCheck");
            setDrawableColorByKey(chat_msgMediaClockDrawable, "chat_mediaSentClock");
            setDrawableColorByKey(chat_msgStickerCheckDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_msgStickerHalfCheckDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_msgStickerClockDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_msgStickerViewsDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_shareIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_replyIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_goIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_botInlineDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_botLinkDrawalbe, "chat_serviceIcon");
            setDrawableColorByKey(chat_msgInViewsDrawable, "chat_inViews");
            setDrawableColorByKey(chat_msgInViewsSelectedDrawable, "chat_inViewsSelected");
            setDrawableColorByKey(chat_msgOutViewsDrawable, "chat_outViews");
            setDrawableColorByKey(chat_msgOutViewsSelectedDrawable, "chat_outViewsSelected");
            setDrawableColorByKey(chat_msgMediaViewsDrawable, "chat_mediaViews");
            setDrawableColorByKey(chat_msgInMenuDrawable, "chat_inMenu");
            setDrawableColorByKey(chat_msgInMenuSelectedDrawable, "chat_inMenuSelected");
            setDrawableColorByKey(chat_msgOutMenuDrawable, "chat_outMenu");
            setDrawableColorByKey(chat_msgOutMenuSelectedDrawable, "chat_outMenuSelected");
            setDrawableColorByKey(chat_msgMediaMenuDrawable, "chat_mediaMenu");
            setDrawableColorByKey(chat_msgOutInstantDrawable, "chat_outInstant");
            setDrawableColorByKey(chat_msgInInstantDrawable, "chat_inInstant");
            setDrawableColorByKey(chat_msgErrorDrawable, "chat_sentErrorIcon");
            setDrawableColorByKey(chat_muteIconDrawable, "chat_muteIcon");
            setDrawableColorByKey(chat_lockIconDrawable, "chat_lockIcon");
            setDrawableColorByKey(chat_msgBroadcastDrawable, "chat_outBroadcast");
            setDrawableColorByKey(chat_msgBroadcastMediaDrawable, "chat_mediaBroadcast");
            setDrawableColorByKey(chat_inlineResultFile, "chat_inlineResultIcon");
            setDrawableColorByKey(chat_inlineResultAudio, "chat_inlineResultIcon");
            setDrawableColorByKey(chat_inlineResultLocation, "chat_inlineResultIcon");
            setDrawableColorByKey(chat_msgInCallDrawable, "chat_inInstant");
            setDrawableColorByKey(chat_msgInCallSelectedDrawable, "chat_inInstantSelected");
            setDrawableColorByKey(chat_msgOutCallDrawable, "chat_outInstant");
            setDrawableColorByKey(chat_msgOutCallSelectedDrawable, "chat_outInstantSelected");
            setDrawableColorByKey(chat_msgCallUpGreenDrawable, "chat_outUpCall");
            setDrawableColorByKey(chat_msgCallDownRedDrawable, "chat_inUpCall");
            setDrawableColorByKey(chat_msgCallDownGreenDrawable, "chat_inDownCall");
            setDrawableColorByKey(calllog_msgCallUpRedDrawable, "calls_callReceivedRedIcon");
            setDrawableColorByKey(calllog_msgCallUpGreenDrawable, "calls_callReceivedGreenIcon");
            setDrawableColorByKey(calllog_msgCallDownRedDrawable, "calls_callReceivedRedIcon");
            setDrawableColorByKey(calllog_msgCallDownGreenDrawable, "calls_callReceivedGreenIcon");

            int var1;
            int var3;
            for(var1 = 0; var1 < 2; ++var1) {
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var1][0], getColor("chat_outLoader"), false);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var1][0], getColor("chat_outMediaIcon"), true);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var1][1], getColor("chat_outLoaderSelected"), false);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var1][1], getColor("chat_outMediaIconSelected"), true);
               CombinedDrawable[][] var2 = chat_fileMiniStatesDrawable;
               var3 = var1 + 2;
               setCombinedDrawableColor(var2[var3][0], getColor("chat_inLoader"), false);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var3][0], getColor("chat_inMediaIcon"), true);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var3][1], getColor("chat_inLoaderSelected"), false);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var3][1], getColor("chat_inMediaIconSelected"), true);
               var2 = chat_fileMiniStatesDrawable;
               var3 = var1 + 4;
               setCombinedDrawableColor(var2[var3][0], getColor("chat_mediaLoaderPhoto"), false);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var3][0], getColor("chat_mediaLoaderPhotoIcon"), true);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var3][1], getColor("chat_mediaLoaderPhotoSelected"), false);
               setCombinedDrawableColor(chat_fileMiniStatesDrawable[var3][1], getColor("chat_mediaLoaderPhotoIconSelected"), true);
            }

            Drawable[][] var4;
            for(var1 = 0; var1 < 5; ++var1) {
               setCombinedDrawableColor(chat_fileStatesDrawable[var1][0], getColor("chat_outLoader"), false);
               setCombinedDrawableColor(chat_fileStatesDrawable[var1][0], getColor("chat_outMediaIcon"), true);
               setCombinedDrawableColor(chat_fileStatesDrawable[var1][1], getColor("chat_outLoaderSelected"), false);
               setCombinedDrawableColor(chat_fileStatesDrawable[var1][1], getColor("chat_outMediaIconSelected"), true);
               var4 = chat_fileStatesDrawable;
               var3 = var1 + 5;
               setCombinedDrawableColor(var4[var3][0], getColor("chat_inLoader"), false);
               setCombinedDrawableColor(chat_fileStatesDrawable[var3][0], getColor("chat_inMediaIcon"), true);
               setCombinedDrawableColor(chat_fileStatesDrawable[var3][1], getColor("chat_inLoaderSelected"), false);
               setCombinedDrawableColor(chat_fileStatesDrawable[var3][1], getColor("chat_inMediaIconSelected"), true);
            }

            for(var1 = 0; var1 < 4; ++var1) {
               setCombinedDrawableColor(chat_photoStatesDrawables[var1][0], getColor("chat_mediaLoaderPhoto"), false);
               setCombinedDrawableColor(chat_photoStatesDrawables[var1][0], getColor("chat_mediaLoaderPhotoIcon"), true);
               setCombinedDrawableColor(chat_photoStatesDrawables[var1][1], getColor("chat_mediaLoaderPhotoSelected"), false);
               setCombinedDrawableColor(chat_photoStatesDrawables[var1][1], getColor("chat_mediaLoaderPhotoIconSelected"), true);
            }

            for(var1 = 0; var1 < 2; ++var1) {
               var4 = chat_photoStatesDrawables;
               var3 = var1 + 7;
               setCombinedDrawableColor(var4[var3][0], getColor("chat_outLoaderPhoto"), false);
               setCombinedDrawableColor(chat_photoStatesDrawables[var3][0], getColor("chat_outLoaderPhotoIcon"), true);
               setCombinedDrawableColor(chat_photoStatesDrawables[var3][1], getColor("chat_outLoaderPhotoSelected"), false);
               setCombinedDrawableColor(chat_photoStatesDrawables[var3][1], getColor("chat_outLoaderPhotoIconSelected"), true);
               var4 = chat_photoStatesDrawables;
               var3 = var1 + 10;
               setCombinedDrawableColor(var4[var3][0], getColor("chat_inLoaderPhoto"), false);
               setCombinedDrawableColor(chat_photoStatesDrawables[var3][0], getColor("chat_inLoaderPhotoIcon"), true);
               setCombinedDrawableColor(chat_photoStatesDrawables[var3][1], getColor("chat_inLoaderPhotoSelected"), false);
               setCombinedDrawableColor(chat_photoStatesDrawables[var3][1], getColor("chat_inLoaderPhotoIconSelected"), true);
            }

            setDrawableColorByKey(chat_photoStatesDrawables[9][0], "chat_outFileIcon");
            setDrawableColorByKey(chat_photoStatesDrawables[9][1], "chat_outFileSelectedIcon");
            setDrawableColorByKey(chat_photoStatesDrawables[12][0], "chat_inFileIcon");
            setDrawableColorByKey(chat_photoStatesDrawables[12][1], "chat_inFileSelectedIcon");
            setCombinedDrawableColor(chat_contactDrawable[0], getColor("chat_inContactBackground"), false);
            setCombinedDrawableColor(chat_contactDrawable[0], getColor("chat_inContactIcon"), true);
            setCombinedDrawableColor(chat_contactDrawable[1], getColor("chat_outContactBackground"), false);
            setCombinedDrawableColor(chat_contactDrawable[1], getColor("chat_outContactIcon"), true);
            setCombinedDrawableColor(chat_locationDrawable[0], getColor("chat_inLocationBackground"), false);
            setCombinedDrawableColor(chat_locationDrawable[0], getColor("chat_inLocationIcon"), true);
            setCombinedDrawableColor(chat_locationDrawable[1], getColor("chat_outLocationBackground"), false);
            setCombinedDrawableColor(chat_locationDrawable[1], getColor("chat_outLocationIcon"), true);
            setDrawableColorByKey(chat_composeShadowDrawable, "chat_messagePanelShadow");
            setCombinedDrawableColor(chat_attachButtonDrawables[1], getColor("chat_attachGalleryBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[1], getColor("chat_attachGalleryIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[2], getColor("chat_attachVideoBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[2], getColor("chat_attachVideoIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[3], getColor("chat_attachAudioBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[3], getColor("chat_attachAudioIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[4], getColor("chat_attachFileBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[4], getColor("chat_attachFileIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[5], getColor("chat_attachContactBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[5], getColor("chat_attachContactIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[6], getColor("chat_attachLocationBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[6], getColor("chat_attachLocationIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[7], getColor("chat_attachHideBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[7], getColor("chat_attachHideIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[8], getColor("chat_attachSendBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[8], getColor("chat_attachSendIcon"), true);
            setCombinedDrawableColor(chat_attachButtonDrawables[9], getColor("chat_attachPollBackground"), false);
            setCombinedDrawableColor(chat_attachButtonDrawables[9], getColor("chat_attachPollIcon"), true);
            applyChatServiceMessageColor();
         }

      }
   }

   public static void applyCommonTheme() {
      Paint var0 = dividerPaint;
      if (var0 != null) {
         var0.setColor(getColor("divider"));
         linkSelectionPaint.setColor(getColor("windowBackgroundWhiteLinkSelection"));
         setDrawableColorByKey(avatar_broadcastDrawable, "avatar_text");
         setDrawableColorByKey(avatar_savedDrawable, "avatar_text");
         dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"**"}), LottieProperty.COLOR_FILTER, (LottieValueCallback)null);
         dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Arrow1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("avatar_backgroundArchived"))));
         dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Arrow2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("avatar_backgroundArchived"))));
         dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Box2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("avatar_text"))));
         dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[]{"Box1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("avatar_text"))));
         dialogs_archiveAvatarDrawableRecolored = false;
         Drawable var1 = dialogs_pinArchiveDrawable;
         LottieDrawable var2;
         if (var1 instanceof LottieDrawable) {
            var2 = (LottieDrawable)var1;
            var2.addValueCallback(new KeyPath(new String[]{"**"}), LottieProperty.COLOR_FILTER, (LottieValueCallback)null);
            var2.addValueCallback(new KeyPath(new String[]{"Arrow", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
            var2.addValueCallback(new KeyPath(new String[]{"Line", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
         } else {
            setDrawableColorByKey(var1, "chats_archiveIcon");
         }

         var1 = dialogs_unpinArchiveDrawable;
         if (var1 instanceof LottieDrawable) {
            var2 = (LottieDrawable)var1;
            var2.addValueCallback(new KeyPath(new String[]{"**"}), LottieProperty.COLOR_FILTER, (LottieValueCallback)null);
            var2.addValueCallback(new KeyPath(new String[]{"Arrow", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
            var2.addValueCallback(new KeyPath(new String[]{"Line", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
         } else {
            setDrawableColorByKey(var1, "chats_archiveIcon");
         }

         var1 = dialogs_archiveDrawable;
         if (var1 instanceof LottieDrawable) {
            var2 = (LottieDrawable)var1;
            var2.addValueCallback(new KeyPath(new String[]{"**"}), LottieProperty.COLOR_FILTER, (LottieValueCallback)null);
            var2.addValueCallback(new KeyPath(new String[]{"Arrow", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveBackground"))));
            var2.addValueCallback(new KeyPath(new String[]{"Box2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
            var2.addValueCallback(new KeyPath(new String[]{"Box1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
            dialogs_archiveDrawableRecolored = false;
         } else {
            setDrawableColorByKey(var1, "chats_archiveIcon");
         }

         var1 = dialogs_unarchiveDrawable;
         if (var1 instanceof LottieDrawable) {
            var2 = (LottieDrawable)var1;
            var2.addValueCallback(new KeyPath(new String[]{"**"}), LottieProperty.COLOR_FILTER, (LottieValueCallback)null);
            var2.addValueCallback(new KeyPath(new String[]{"Arrow1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
            var2.addValueCallback(new KeyPath(new String[]{"Arrow2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archivePinBackground"))));
            var2.addValueCallback(new KeyPath(new String[]{"Box2", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
            var2.addValueCallback(new KeyPath(new String[]{"Box1", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(getColor("chats_archiveIcon"))));
         } else {
            setDrawableColorByKey(var1, "chats_archiveIcon");
         }

      }
   }

   private static void applyDayNightThemeMaybe(boolean var0) {
      Boolean var1 = true;
      if (var0) {
         if (currentTheme != currentNightTheme) {
            lastThemeSwitchTime = SystemClock.elapsedRealtime();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentNightTheme, var1);
         }
      } else if (currentTheme != currentDayTheme) {
         lastThemeSwitchTime = SystemClock.elapsedRealtime();
         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentDayTheme, var1);
      }

   }

   public static void applyDialogsTheme() {
      TextPaint var0 = dialogs_namePaint;
      if (var0 != null) {
         var0.setColor(getColor("chats_name"));
         dialogs_nameEncryptedPaint.setColor(getColor("chats_secretName"));
         dialogs_searchNamePaint.setColor(getColor("chats_name"));
         dialogs_searchNameEncryptedPaint.setColor(getColor("chats_secretName"));
         var0 = dialogs_messagePaint;
         int var1 = getColor("chats_message");
         var0.linkColor = var1;
         var0.setColor(var1);
         var0 = dialogs_messageNamePaint;
         var1 = getColor("chats_nameMessage_threeLines");
         var0.linkColor = var1;
         var0.setColor(var1);
         dialogs_tabletSeletedPaint.setColor(getColor("chats_tabletSelectedOverlay"));
         dialogs_pinnedPaint.setColor(getColor("chats_pinnedOverlay"));
         dialogs_timePaint.setColor(getColor("chats_date"));
         dialogs_countTextPaint.setColor(getColor("chats_unreadCounterText"));
         dialogs_archiveTextPaint.setColor(getColor("chats_archiveText"));
         dialogs_messagePrintingPaint.setColor(getColor("chats_actionMessage"));
         dialogs_countPaint.setColor(getColor("chats_unreadCounter"));
         dialogs_countGrayPaint.setColor(getColor("chats_unreadCounterMuted"));
         dialogs_errorPaint.setColor(getColor("chats_sentError"));
         dialogs_onlinePaint.setColor(getColor("windowBackgroundWhiteBlueText3"));
         dialogs_offlinePaint.setColor(getColor("windowBackgroundWhiteGrayText3"));
         setDrawableColorByKey(dialogs_lockDrawable, "chats_secretIcon");
         setDrawableColorByKey(dialogs_checkDrawable, "chats_sentCheck");
         setDrawableColorByKey(dialogs_halfCheckDrawable, "chats_sentCheck");
         setDrawableColorByKey(dialogs_clockDrawable, "chats_sentClock");
         setDrawableColorByKey(dialogs_errorDrawable, "chats_sentErrorIcon");
         setDrawableColorByKey(dialogs_groupDrawable, "chats_nameIcon");
         setDrawableColorByKey(dialogs_broadcastDrawable, "chats_nameIcon");
         setDrawableColorByKey(dialogs_botDrawable, "chats_nameIcon");
         setDrawableColorByKey(dialogs_pinnedDrawable, "chats_pinnedIcon");
         setDrawableColorByKey(dialogs_reorderDrawable, "chats_pinnedIcon");
         setDrawableColorByKey(dialogs_muteDrawable, "chats_muteIcon");
         setDrawableColorByKey(dialogs_mentionDrawable, "chats_mentionIcon");
         setDrawableColorByKey(dialogs_verifiedDrawable, "chats_verifiedBackground");
         setDrawableColorByKey(dialogs_verifiedCheckDrawable, "chats_verifiedCheck");
         setDrawableColorByKey(dialogs_holidayDrawable, "actionBarDefaultTitle");
         setDrawableColorByKey(dialogs_scamDrawable, "chats_draft");
      }
   }

   public static void applyPreviousTheme() {
      Theme.ThemeInfo var0 = previousTheme;
      if (var0 != null) {
         applyTheme(var0, true, false, false);
         previousTheme = null;
         checkAutoNightThemeConditions();
      }
   }

   public static void applyProfileTheme() {
      if (profile_verifiedDrawable != null) {
         profile_aboutTextPaint.setColor(getColor("windowBackgroundWhiteBlackText"));
         profile_aboutTextPaint.linkColor = getColor("windowBackgroundWhiteLinkText");
         setDrawableColorByKey(profile_verifiedDrawable, "profile_verifiedBackground");
         setDrawableColorByKey(profile_verifiedCheckDrawable, "profile_verifiedCheck");
      }
   }

   public static void applyTheme(Theme.ThemeInfo var0) {
      applyTheme(var0, true, true, false);
   }

   public static void applyTheme(Theme.ThemeInfo var0, boolean var1) {
      applyTheme(var0, true, true, var1);
   }

   public static void applyTheme(Theme.ThemeInfo var0, boolean var1, boolean var2, boolean var3) {
      if (var0 != null) {
         ThemeEditorView var4 = ThemeEditorView.getInstance();
         if (var4 != null) {
            var4.destroy();
         }

         Exception var10000;
         label124: {
            boolean var10001;
            String var21;
            try {
               var21 = var0.pathToFile;
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label124;
            }

            label125: {
               Editor var22;
               label129: {
                  if (var21 == null) {
                     try {
                        if (var0.assetName == null) {
                           break label129;
                        }
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label124;
                     }
                  }

                  if (!var3 && var1) {
                     try {
                        var22 = MessagesController.getGlobalMainSettings().edit();
                        var22.putString("theme", var0.name);
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label124;
                     }

                     if (var2) {
                        try {
                           var22.remove("overrideThemeWallpaper");
                        } catch (Exception var10) {
                           var10000 = var10;
                           var10001 = false;
                           break label124;
                        }
                     }

                     try {
                        var22.commit();
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label124;
                     }
                  }

                  try {
                     if (var0.assetName != null) {
                        currentColors = getThemeFileValues((File)null, var0.assetName);
                        break label125;
                     }
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label124;
                  }

                  try {
                     File var23 = new File(var0.pathToFile);
                     currentColors = getThemeFileValues(var23, (String)null);
                     break label125;
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label124;
                  }
               }

               if (!var3 && var1) {
                  try {
                     var22 = MessagesController.getGlobalMainSettings().edit();
                     var22.remove("theme");
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label124;
                  }

                  if (var2) {
                     try {
                        var22.remove("overrideThemeWallpaper");
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label124;
                     }
                  }

                  try {
                     var22.commit();
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label124;
                  }
               }

               try {
                  currentColors.clear();
                  themedWallpaperFileOffset = 0;
                  wallpaper = null;
                  themedWallpaper = null;
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label124;
               }
            }

            try {
               currentTheme = var0;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label124;
            }

            if (!var3) {
               try {
                  currentDayTheme = currentTheme;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label124;
               }
            }

            try {
               reloadWallpaper();
               applyCommonTheme();
               applyDialogsTheme();
               applyProfileTheme();
               applyChatTheme(false);
               _$$Lambda$Theme$U5dmA2RnRuUehj9EUY9kmkkhUlE var20 = new _$$Lambda$Theme$U5dmA2RnRuUehj9EUY9kmkkhUlE(var3);
               AndroidUtilities.runOnUIThread(var20);
               return;
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
            }
         }

         Exception var19 = var10000;
         FileLog.e((Throwable)var19);
      }
   }

   public static Theme.ThemeInfo applyThemeFile(File var0, String var1, boolean var2) {
      Exception var10000;
      label86: {
         boolean var10001;
         try {
            if (var1.equals("Default") || var1.equals("Dark") || var1.equals("Blue") || var1.equals("Dark Blue") || var1.equals("Graphite") || var1.equals("Arctic Blue")) {
               return null;
            }
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label86;
         }

         File var3;
         try {
            var3 = new File(ApplicationLoader.getFilesDirFixed(), var1);
            if (!AndroidUtilities.copyFile(var0, var3)) {
               return null;
            }
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label86;
         }

         Theme.ThemeInfo var13;
         try {
            var13 = (Theme.ThemeInfo)themesDict.get(var1);
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label86;
         }

         boolean var4;
         if (var13 == null) {
            try {
               var13 = new Theme.ThemeInfo();
               var13.name = var1;
               var13.pathToFile = var3.getAbsolutePath();
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label86;
            }

            var4 = true;
         } else {
            var4 = false;
         }

         if (!var2) {
            try {
               previousTheme = null;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label86;
            }

            if (var4) {
               try {
                  themes.add(var13);
                  themesDict.put(var13.name, var13);
                  otherThemes.add(var13);
                  sortThemes();
                  saveOtherThemes();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label86;
               }
            }
         } else {
            try {
               previousTheme = currentTheme;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label86;
            }
         }

         if (!var2) {
            var2 = true;
         } else {
            var2 = false;
         }

         try {
            applyTheme(var13, var2, true, false);
            return var13;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
      return null;
   }

   private static void calcBackgroundColor(Drawable var0, int var1) {
      if (var1 != 2) {
         int[] var2 = AndroidUtilities.calcDrawableColor(var0);
         var1 = var2[0];
         serviceMessageColorBackup = var1;
         serviceMessageColor = var1;
         var1 = var2[1];
         serviceSelectedMessageColorBackup = var1;
         serviceSelectedMessageColor = var1;
         serviceMessage2Color = var2[2];
         serviceSelectedMessage2Color = var2[3];
      }

   }

   public static boolean canStartHolidayAnimation() {
      return canStartHolidayAnimation;
   }

   public static void checkAutoNightThemeConditions() {
      checkAutoNightThemeConditions(false);
   }

   public static void checkAutoNightThemeConditions(boolean var0) {
      if (previousTheme == null) {
         boolean var1 = false;
         if (var0) {
            if (switchNightRunnableScheduled) {
               switchNightRunnableScheduled = false;
               AndroidUtilities.cancelRunOnUIThread(switchNightBrightnessRunnable);
            }

            if (switchDayRunnableScheduled) {
               switchDayRunnableScheduled = false;
               AndroidUtilities.cancelRunOnUIThread(switchDayBrightnessRunnable);
            }
         }

         if (selectedAutoNightType != 2) {
            if (switchNightRunnableScheduled) {
               switchNightRunnableScheduled = false;
               AndroidUtilities.cancelRunOnUIThread(switchNightBrightnessRunnable);
            }

            if (switchDayRunnableScheduled) {
               switchDayRunnableScheduled = false;
               AndroidUtilities.cancelRunOnUIThread(switchDayBrightnessRunnable);
            }

            if (lightSensorRegistered) {
               lastBrightnessValue = 1.0F;
               sensorManager.unregisterListener(ambientSensorListener, lightSensor);
               lightSensorRegistered = false;
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("light sensor unregistered");
               }
            }
         }

         byte var10;
         label107: {
            label119: {
               int var2 = selectedAutoNightType;
               if (var2 == 1) {
                  Calendar var11 = Calendar.getInstance();
                  var11.setTimeInMillis(System.currentTimeMillis());
                  int var4 = var11.get(11) * 60 + var11.get(12);
                  int var9;
                  if (autoNightScheduleByLocation) {
                     var2 = var11.get(5);
                     if (autoNightLastSunCheckDay != var2) {
                        double var5 = autoNightLocationLatitude;
                        if (var5 != 10000.0D) {
                           double var7 = autoNightLocationLongitude;
                           if (var7 != 10000.0D) {
                              int[] var12 = SunDate.calculateSunriseSunset(var5, var7);
                              autoNightSunriseTime = var12[0];
                              autoNightSunsetTime = var12[1];
                              autoNightLastSunCheckDay = var2;
                              saveAutoNightThemeConfig();
                           }
                        }
                     }

                     var2 = autoNightSunsetTime;
                     var9 = autoNightSunriseTime;
                  } else {
                     var2 = autoNightDayStartTime;
                     var9 = autoNightDayEndTime;
                  }

                  if (var2 < var9) {
                     if (var2 <= var4 && var4 <= var9) {
                        break label119;
                     }
                  } else if (var2 <= var4 && var4 <= 1440 || var4 >= 0 && var4 <= var9) {
                     break label119;
                  }
               } else {
                  label118: {
                     if (var2 == 2) {
                        if (lightSensor == null) {
                           sensorManager = (SensorManager)ApplicationLoader.applicationContext.getSystemService("sensor");
                           lightSensor = sensorManager.getDefaultSensor(5);
                        }

                        if (!lightSensorRegistered) {
                           Sensor var3 = lightSensor;
                           if (var3 != null) {
                              sensorManager.registerListener(ambientSensorListener, var3, 500000);
                              lightSensorRegistered = true;
                              if (BuildVars.LOGS_ENABLED) {
                                 FileLog.d("light sensor registered");
                              }
                           }
                        }

                        if (lastBrightnessValue <= autoNightBrighnessThreshold) {
                           if (!switchNightRunnableScheduled) {
                              break label119;
                           }
                        } else if (!switchDayRunnableScheduled) {
                           break label118;
                        }
                     } else if (var2 == 0) {
                        break label118;
                     }

                     var10 = 0;
                     break label107;
                  }
               }

               var10 = 1;
               break label107;
            }

            var10 = 2;
         }

         if (var10 != 0) {
            if (var10 == 2) {
               var1 = true;
            }

            applyDayNightThemeMaybe(var1);
         }

         if (var0) {
            lastThemeSwitchTime = 0L;
         }

      }
   }

   public static void createChatResources(Context param0, boolean param1) {
      // $FF: Couldn't be decompiled
   }

   public static Drawable createCircleDrawable(int var0, int var1) {
      OvalShape var2 = new OvalShape();
      float var3 = (float)var0;
      var2.resize(var3, var3);
      ShapeDrawable var4 = new ShapeDrawable(var2);
      var4.getPaint().setColor(var1);
      return var4;
   }

   public static CombinedDrawable createCircleDrawableWithIcon(int var0, int var1) {
      return createCircleDrawableWithIcon(var0, var1, 0);
   }

   public static CombinedDrawable createCircleDrawableWithIcon(int var0, int var1, int var2) {
      Drawable var3;
      if (var1 != 0) {
         var3 = ApplicationLoader.applicationContext.getResources().getDrawable(var1).mutate();
      } else {
         var3 = null;
      }

      return createCircleDrawableWithIcon(var0, var3, var2);
   }

   public static CombinedDrawable createCircleDrawableWithIcon(int var0, Drawable var1, int var2) {
      OvalShape var3 = new OvalShape();
      float var4 = (float)var0;
      var3.resize(var4, var4);
      ShapeDrawable var5 = new ShapeDrawable(var3);
      Paint var7 = var5.getPaint();
      var7.setColor(-1);
      if (var2 == 1) {
         var7.setStyle(Style.STROKE);
         var7.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      } else if (var2 == 2) {
         var7.setAlpha(0);
      }

      CombinedDrawable var6 = new CombinedDrawable(var5, var1);
      var6.setCustomSize(var0, var0);
      return var6;
   }

   public static void createCommonResources(Context var0) {
      if (dividerPaint == null) {
         dividerPaint = new Paint();
         dividerPaint.setStrokeWidth(1.0F);
         avatar_backgroundPaint = new Paint(1);
         checkboxSquare_checkPaint = new Paint(1);
         checkboxSquare_checkPaint.setStyle(Style.STROKE);
         checkboxSquare_checkPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
         checkboxSquare_eraserPaint = new Paint(1);
         checkboxSquare_eraserPaint.setColor(0);
         checkboxSquare_eraserPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
         checkboxSquare_backgroundPaint = new Paint(1);
         linkSelectionPaint = new Paint();
         Resources var1 = var0.getResources();
         avatar_broadcastDrawable = var1.getDrawable(2131165326);
         avatar_savedDrawable = var1.getDrawable(2131165351);
         dialogs_archiveAvatarDrawable = new LottieDrawable();
         dialogs_archiveAvatarDrawable.setComposition((LottieComposition)LottieCompositionFactory.fromRawResSync(var0, 2131492865).getValue());
         if (VERSION.SDK_INT == 24) {
            dialogs_archiveDrawable = var1.getDrawable(2131165341);
            dialogs_unarchiveDrawable = var1.getDrawable(2131165352);
            dialogs_pinArchiveDrawable = var1.getDrawable(2131165344);
            dialogs_unpinArchiveDrawable = var1.getDrawable(2131165347);
         } else {
            LottieDrawable var2 = new LottieDrawable();
            var2.setComposition((LottieComposition)LottieCompositionFactory.fromRawResSync(var0, 2131492864).getValue());
            dialogs_archiveDrawable = var2;
            var2 = new LottieDrawable();
            var2.setComposition((LottieComposition)LottieCompositionFactory.fromRawResSync(var0, 2131492870).getValue());
            dialogs_unarchiveDrawable = var2;
            var2 = new LottieDrawable();
            var2.setComposition((LottieComposition)LottieCompositionFactory.fromRawResSync(var0, 2131492867).getValue());
            dialogs_pinArchiveDrawable = var2;
            var2 = new LottieDrawable();
            var2.setComposition((LottieComposition)LottieCompositionFactory.fromRawResSync(var0, 2131492871).getValue());
            dialogs_unpinArchiveDrawable = var2;
         }

         applyCommonTheme();
      }

   }

   public static void createDialogsResources(Context var0) {
      createCommonResources(var0);
      if (dialogs_namePaint == null) {
         Resources var1 = var0.getResources();
         dialogs_namePaint = new TextPaint(1);
         dialogs_namePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         dialogs_nameEncryptedPaint = new TextPaint(1);
         dialogs_nameEncryptedPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         dialogs_searchNamePaint = new TextPaint(1);
         dialogs_searchNamePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         dialogs_searchNameEncryptedPaint = new TextPaint(1);
         dialogs_searchNameEncryptedPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         dialogs_messagePaint = new TextPaint(1);
         dialogs_messageNamePaint = new TextPaint(1);
         dialogs_messageNamePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         dialogs_messagePrintingPaint = new TextPaint(1);
         dialogs_timePaint = new TextPaint(1);
         dialogs_countTextPaint = new TextPaint(1);
         dialogs_countTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         dialogs_archiveTextPaint = new TextPaint(1);
         dialogs_archiveTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         dialogs_onlinePaint = new TextPaint(1);
         dialogs_offlinePaint = new TextPaint(1);
         dialogs_tabletSeletedPaint = new Paint();
         dialogs_pinnedPaint = new Paint(1);
         dialogs_onlineCirclePaint = new Paint(1);
         dialogs_countPaint = new Paint(1);
         dialogs_countGrayPaint = new Paint(1);
         dialogs_errorPaint = new Paint(1);
         dialogs_lockDrawable = var1.getDrawable(2131165531);
         dialogs_checkDrawable = var1.getDrawable(2131165524);
         dialogs_halfCheckDrawable = var1.getDrawable(2131165527);
         dialogs_clockDrawable = var1.getDrawable(2131165620).mutate();
         dialogs_errorDrawable = var1.getDrawable(2131165533);
         dialogs_reorderDrawable = var1.getDrawable(2131165530);
         dialogs_groupDrawable = var1.getDrawable(2131165526);
         dialogs_broadcastDrawable = var1.getDrawable(2131165523);
         dialogs_muteDrawable = var1.getDrawable(2131165528).mutate();
         dialogs_verifiedDrawable = var1.getDrawable(2131165893);
         dialogs_scamDrawable = new ScamDrawable(11);
         dialogs_verifiedCheckDrawable = var1.getDrawable(2131165894);
         dialogs_mentionDrawable = var1.getDrawable(2131165565);
         dialogs_botDrawable = var1.getDrawable(2131165522);
         dialogs_pinnedDrawable = var1.getDrawable(2131165529);
         moveUpDrawable = var1.getDrawable(2131165780);
         applyDialogsTheme();
      }

      dialogs_messageNamePaint.setTextSize((float)AndroidUtilities.dp(14.0F));
      dialogs_timePaint.setTextSize((float)AndroidUtilities.dp(13.0F));
      dialogs_countTextPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
      dialogs_archiveTextPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
      dialogs_onlinePaint.setTextSize((float)AndroidUtilities.dp(15.0F));
      dialogs_offlinePaint.setTextSize((float)AndroidUtilities.dp(15.0F));
      dialogs_searchNamePaint.setTextSize((float)AndroidUtilities.dp(16.0F));
      dialogs_searchNameEncryptedPaint.setTextSize((float)AndroidUtilities.dp(16.0F));
   }

   public static Drawable createEditTextDrawable(Context var0, boolean var1) {
      Resources var2 = var0.getResources();
      Drawable var3 = var2.getDrawable(2131165810).mutate();
      String var4;
      if (var1) {
         var4 = "dialogInputField";
      } else {
         var4 = "windowBackgroundWhiteInputField";
      }

      var3.setColorFilter(new PorterDuffColorFilter(getColor(var4), Mode.MULTIPLY));
      Drawable var5 = var2.getDrawable(2131165811).mutate();
      if (var1) {
         var4 = "dialogInputFieldActivated";
      } else {
         var4 = "windowBackgroundWhiteInputFieldActivated";
      }

      var5.setColorFilter(new PorterDuffColorFilter(getColor(var4), Mode.MULTIPLY));
      StateListDrawable var6 = new StateListDrawable() {
         public boolean selectDrawable(int var1) {
            if (VERSION.SDK_INT < 21) {
               Drawable var2 = Theme.getStateDrawable(this, var1);
               ColorFilter var3 = null;
               if (var2 instanceof BitmapDrawable) {
                  var3 = ((BitmapDrawable)var2).getPaint().getColorFilter();
               } else if (var2 instanceof NinePatchDrawable) {
                  var3 = ((NinePatchDrawable)var2).getPaint().getColorFilter();
               }

               boolean var4 = super.selectDrawable(var1);
               if (var3 != null) {
                  var2.setColorFilter(var3);
               }

               return var4;
            } else {
               return super.selectDrawable(var1);
            }
         }
      };
      var6.addState(new int[]{16842910, 16842908}, var5);
      var6.addState(new int[]{16842908}, var5);
      var6.addState(StateSet.WILD_CARD, var3);
      return var6;
   }

   public static Drawable createEmojiIconSelectorDrawable(Context var0, int var1, int var2, int var3) {
      Resources var4 = var0.getResources();
      Drawable var6 = var4.getDrawable(var1).mutate();
      if (var2 != 0) {
         var6.setColorFilter(new PorterDuffColorFilter(var2, Mode.MULTIPLY));
      }

      Drawable var7 = var4.getDrawable(var1).mutate();
      if (var3 != 0) {
         var7.setColorFilter(new PorterDuffColorFilter(var3, Mode.MULTIPLY));
      }

      StateListDrawable var5 = new StateListDrawable() {
         public boolean selectDrawable(int var1) {
            if (VERSION.SDK_INT < 21) {
               Drawable var2 = Theme.getStateDrawable(this, var1);
               ColorFilter var3 = null;
               if (var2 instanceof BitmapDrawable) {
                  var3 = ((BitmapDrawable)var2).getPaint().getColorFilter();
               } else if (var2 instanceof NinePatchDrawable) {
                  var3 = ((NinePatchDrawable)var2).getPaint().getColorFilter();
               }

               boolean var4 = super.selectDrawable(var1);
               if (var3 != null) {
                  var2.setColorFilter(var3);
               }

               return var4;
            } else {
               return super.selectDrawable(var1);
            }
         }
      };
      var5.setEnterFadeDuration(1);
      var5.setExitFadeDuration(200);
      var5.addState(new int[]{16842913}, var7);
      var5.addState(new int[0], var6);
      return var5;
   }

   public static void createProfileResources(Context var0) {
      if (profile_verifiedDrawable == null) {
         profile_aboutTextPaint = new TextPaint(1);
         Resources var1 = var0.getResources();
         profile_verifiedDrawable = var1.getDrawable(2131165893).mutate();
         profile_verifiedCheckDrawable = var1.getDrawable(2131165894).mutate();
         applyProfileTheme();
      }

      profile_aboutTextPaint.setTextSize((float)AndroidUtilities.dp(16.0F));
   }

   public static Drawable createRoundRectDrawable(int var0, int var1) {
      float var2 = (float)var0;
      ShapeDrawable var3 = new ShapeDrawable(new RoundRectShape(new float[]{var2, var2, var2, var2, var2, var2, var2, var2}, (RectF)null, (float[])null));
      var3.getPaint().setColor(var1);
      return var3;
   }

   public static Drawable createRoundRectDrawableWithIcon(int var0, int var1) {
      float var2 = (float)var0;
      ShapeDrawable var3 = new ShapeDrawable(new RoundRectShape(new float[]{var2, var2, var2, var2, var2, var2, var2, var2}, (RectF)null, (float[])null));
      var3.getPaint().setColor(-1);
      return new CombinedDrawable(var3, ApplicationLoader.applicationContext.getResources().getDrawable(var1).mutate());
   }

   public static Drawable createSelectorDrawable(int var0) {
      return createSelectorDrawable(var0, 1);
   }

   public static Drawable createSelectorDrawable(int var0, final int var1) {
      int var2 = VERSION.SDK_INT;
      if (var2 < 21) {
         StateListDrawable var6 = new StateListDrawable();
         ColorDrawable var4 = new ColorDrawable(var0);
         var6.addState(new int[]{16842919}, var4);
         var4 = new ColorDrawable(var0);
         var6.addState(new int[]{16842913}, var4);
         var6.addState(StateSet.WILD_CARD, new ColorDrawable(0));
         return var6;
      } else {
         Object var3;
         label41: {
            if (var1 != 1 || var2 < 23) {
               if (var1 == 1 || var1 == 3 || var1 == 4) {
                  maskPaint.setColor(-1);
                  var3 = new Drawable() {
                     public void draw(Canvas var1x) {
                        Rect var2 = this.getBounds();
                        int var3 = var1;
                        if (var3 == 1) {
                           var3 = AndroidUtilities.dp(20.0F);
                        } else if (var3 == 3) {
                           var3 = Math.max(var2.width(), var2.height()) / 2;
                        } else {
                           var3 = (int)Math.ceil(Math.sqrt((double)((var2.left - var2.centerX()) * (var2.left - var2.centerX()) + (var2.top - var2.centerY()) * (var2.top - var2.centerY()))));
                        }

                        var1x.drawCircle((float)var2.centerX(), (float)var2.centerY(), (float)var3, Theme.maskPaint);
                     }

                     public int getOpacity() {
                        return 0;
                     }

                     public void setAlpha(int var1x) {
                     }

                     public void setColorFilter(ColorFilter var1x) {
                     }
                  };
                  break label41;
               }

               if (var1 == 2) {
                  var3 = new ColorDrawable(-1);
                  break label41;
               }
            }

            var3 = null;
         }

         RippleDrawable var5 = new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{var0}), (Drawable)null, (Drawable)var3);
         if (var1 == 1 && VERSION.SDK_INT >= 23) {
            var5.setRadius(AndroidUtilities.dp(20.0F));
         }

         return var5;
      }
   }

   public static Drawable createSelectorWithBackgroundDrawable(int var0, int var1) {
      if (VERSION.SDK_INT >= 21) {
         ColorDrawable var4 = new ColorDrawable(var0);
         return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{var1}), new ColorDrawable(var0), var4);
      } else {
         StateListDrawable var2 = new StateListDrawable();
         ColorDrawable var3 = new ColorDrawable(var1);
         var2.addState(new int[]{16842919}, var3);
         var3 = new ColorDrawable(var1);
         var2.addState(new int[]{16842913}, var3);
         var2.addState(StateSet.WILD_CARD, new ColorDrawable(var0));
         return var2;
      }
   }

   public static Drawable createSimpleSelectorCircleDrawable(int var0, int var1, int var2) {
      OvalShape var3 = new OvalShape();
      float var4 = (float)var0;
      var3.resize(var4, var4);
      ShapeDrawable var5 = new ShapeDrawable(var3);
      var5.getPaint().setColor(var1);
      ShapeDrawable var7 = new ShapeDrawable(var3);
      if (VERSION.SDK_INT >= 21) {
         var7.getPaint().setColor(-1);
         return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{var2}), var5, var7);
      } else {
         var7.getPaint().setColor(var2);
         StateListDrawable var6 = new StateListDrawable();
         var6.addState(new int[]{16842919}, var7);
         var6.addState(new int[]{16842908}, var7);
         var6.addState(StateSet.WILD_CARD, var5);
         return var6;
      }
   }

   public static Drawable createSimpleSelectorDrawable(Context var0, int var1, int var2, int var3) {
      Resources var4 = var0.getResources();
      Drawable var6 = var4.getDrawable(var1).mutate();
      if (var2 != 0) {
         var6.setColorFilter(new PorterDuffColorFilter(var2, Mode.MULTIPLY));
      }

      Drawable var5 = var4.getDrawable(var1).mutate();
      if (var3 != 0) {
         var5.setColorFilter(new PorterDuffColorFilter(var3, Mode.MULTIPLY));
      }

      StateListDrawable var7 = new StateListDrawable() {
         public boolean selectDrawable(int var1) {
            if (VERSION.SDK_INT < 21) {
               Drawable var2 = Theme.getStateDrawable(this, var1);
               ColorFilter var3 = null;
               if (var2 instanceof BitmapDrawable) {
                  var3 = ((BitmapDrawable)var2).getPaint().getColorFilter();
               } else if (var2 instanceof NinePatchDrawable) {
                  var3 = ((NinePatchDrawable)var2).getPaint().getColorFilter();
               }

               boolean var4 = super.selectDrawable(var1);
               if (var3 != null) {
                  var2.setColorFilter(var3);
               }

               return var4;
            } else {
               return super.selectDrawable(var1);
            }
         }
      };
      var7.addState(new int[]{16842919}, var5);
      var7.addState(new int[]{16842913}, var5);
      var7.addState(StateSet.WILD_CARD, var6);
      return var7;
   }

   public static Drawable createSimpleSelectorRoundRectDrawable(int var0, int var1, int var2) {
      float var3 = (float)var0;
      ShapeDrawable var4 = new ShapeDrawable(new RoundRectShape(new float[]{var3, var3, var3, var3, var3, var3, var3, var3}, (RectF)null, (float[])null));
      var4.getPaint().setColor(var1);
      ShapeDrawable var5 = new ShapeDrawable(new RoundRectShape(new float[]{var3, var3, var3, var3, var3, var3, var3, var3}, (RectF)null, (float[])null));
      var5.getPaint().setColor(var2);
      StateListDrawable var6 = new StateListDrawable();
      var6.addState(new int[]{16842919}, var5);
      var6.addState(new int[]{16842913}, var5);
      var6.addState(StateSet.WILD_CARD, var4);
      return var6;
   }

   public static boolean deleteTheme(Theme.ThemeInfo var0) {
      String var1 = var0.pathToFile;
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         if (currentTheme == var0) {
            applyTheme(defaultTheme, true, false, false);
            var2 = true;
         }

         otherThemes.remove(var0);
         themesDict.remove(var0.name);
         themes.remove(var0);
         (new File(var0.pathToFile)).delete();
         saveOtherThemes();
         return var2;
      }
   }

   public static void destroyResources() {
      int var0 = 0;

      while(true) {
         Drawable[] var1 = chat_attachButtonDrawables;
         if (var0 >= var1.length) {
            return;
         }

         if (var1[var0] != null) {
            var1[var0].setCallback((Callback)null);
         }

         ++var0;
      }
   }

   public static File getAssetFile(String var0) {
      File var1 = new File(ApplicationLoader.getFilesDirFixed(), var0);

      long var3;
      try {
         InputStream var2 = ApplicationLoader.applicationContext.getAssets().open(var0);
         var3 = (long)var2.available();
         var2.close();
      } catch (Exception var25) {
         FileLog.e((Throwable)var25);
         var3 = 0L;
      }

      if (!var1.exists() || var3 != 0L && var1.length() != var3) {
         Exception var10000;
         label240: {
            InputStream var5;
            boolean var10001;
            try {
               var5 = ApplicationLoader.applicationContext.getAssets().open(var0);
            } catch (Exception var32) {
               var10000 = var32;
               var10001 = false;
               break label240;
            }

            Throwable var33 = null;

            label241: {
               Throwable var35;
               Throwable var36;
               label242: {
                  try {
                     try {
                        AndroidUtilities.copyFile(var5, var1);
                        break label241;
                     } catch (Throwable var30) {
                        var35 = var30;
                     }
                  } catch (Throwable var31) {
                     var36 = var31;
                     var10001 = false;
                     break label242;
                  }

                  var33 = var35;

                  label215:
                  try {
                     throw var35;
                  } catch (Throwable var28) {
                     var36 = var28;
                     var10001 = false;
                     break label215;
                  }
               }

               var35 = var36;
               if (var5 != null) {
                  if (var33 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var24) {
                     }
                  } else {
                     try {
                        var5.close();
                     } catch (Exception var27) {
                        var10000 = var27;
                        var10001 = false;
                        break label240;
                     }
                  }
               }

               try {
                  throw var35;
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label240;
               }
            }

            if (var5 == null) {
               return var1;
            }

            try {
               var5.close();
               return var1;
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
            }
         }

         Exception var34 = var10000;
         FileLog.e((Throwable)var34);
      }

      return var1;
   }

   private static long getAutoNightSwitchThemeDelay() {
      long var0 = SystemClock.elapsedRealtime();
      return Math.abs(lastThemeSwitchTime - var0) >= 12000L ? 1800L : 12000L;
   }

   public static Drawable getCachedWallpaper() {
      Object var0 = wallpaperSync;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label123: {
         Drawable var14;
         try {
            if (themedWallpaper != null) {
               var14 = themedWallpaper;
               return var14;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            var14 = wallpaper;
            return var14;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public static Drawable getCachedWallpaperNonBlocking() {
      Drawable var0 = themedWallpaper;
      return var0 != null ? var0 : wallpaper;
   }

   public static int getColor(String var0) {
      return getColor(var0, (boolean[])null);
   }

   public static int getColor(String var0, boolean[] var1) {
      HashMap var2 = animatingColors;
      Integer var4;
      if (var2 != null) {
         var4 = (Integer)var2.get(var0);
         if (var4 != null) {
            return var4;
         }
      }

      if (isCurrentThemeDefault()) {
         if (var0.equals("chat_serviceBackground")) {
            return serviceMessageColor;
         } else {
            return var0.equals("chat_serviceBackgroundSelected") ? serviceSelectedMessageColor : getDefaultColor(var0);
         }
      } else {
         var4 = (Integer)currentColors.get(var0);
         Integer var3 = var4;
         if (var4 == null) {
            String var5 = (String)fallbackKeys.get(var0);
            if (var5 != null) {
               var4 = (Integer)currentColors.get(var5);
            }

            var3 = var4;
            if (var4 == null) {
               if (var1 != null) {
                  var1[0] = true;
               }

               if (var0.equals("chat_serviceBackground")) {
                  return serviceMessageColor;
               }

               if (var0.equals("chat_serviceBackgroundSelected")) {
                  return serviceSelectedMessageColor;
               }

               return getDefaultColor(var0);
            }
         }

         return var3;
      }
   }

   public static Integer getColorOrNull(String var0) {
      Integer var1 = (Integer)currentColors.get(var0);
      Integer var2 = var1;
      if (var1 == null) {
         if ((String)fallbackKeys.get(var0) != null) {
            var1 = (Integer)currentColors.get(var0);
         }

         var2 = var1;
         if (var1 == null) {
            var2 = (Integer)defaultColors.get(var0);
         }
      }

      return var2;
   }

   public static Drawable getCurrentHolidayDrawable() {
      if (System.currentTimeMillis() - lastHolidayCheckTime >= 60000L) {
         lastHolidayCheckTime = System.currentTimeMillis();
         Calendar var0 = Calendar.getInstance();
         var0.setTimeInMillis(System.currentTimeMillis());
         int var1 = var0.get(2);
         int var2 = var0.get(5);
         int var3 = var0.get(12);
         int var4 = var0.get(11);
         if (var1 == 0 && var2 == 1 && var3 <= 10 && var4 == 0) {
            canStartHolidayAnimation = true;
         } else {
            canStartHolidayAnimation = false;
         }

         if (dialogs_holidayDrawable == null) {
            label44: {
               if (var1 == 11) {
                  byte var5;
                  if (BuildVars.DEBUG_PRIVATE_VERSION) {
                     var5 = 29;
                  } else {
                     var5 = 31;
                  }

                  if (var2 >= var5 && var2 <= 31) {
                     break label44;
                  }
               }

               if (var1 != 0 || var2 != 1) {
                  return dialogs_holidayDrawable;
               }
            }

            dialogs_holidayDrawable = ApplicationLoader.applicationContext.getResources().getDrawable(2131165689);
            dialogs_holidayDrawableOffsetX = -AndroidUtilities.dp(3.0F);
            dialogs_holidayDrawableOffsetY = -AndroidUtilities.dp(1.0F);
         }
      }

      return dialogs_holidayDrawable;
   }

   public static int getCurrentHolidayDrawableXOffset() {
      return dialogs_holidayDrawableOffsetX;
   }

   public static int getCurrentHolidayDrawableYOffset() {
      return dialogs_holidayDrawableOffsetY;
   }

   public static Theme.ThemeInfo getCurrentNightTheme() {
      return currentNightTheme;
   }

   public static String getCurrentNightThemeName() {
      Theme.ThemeInfo var0 = currentNightTheme;
      if (var0 == null) {
         return "";
      } else {
         String var1 = var0.getName();
         String var2 = var1;
         if (var1.toLowerCase().endsWith(".attheme")) {
            var2 = var1.substring(0, var1.lastIndexOf(46));
         }

         return var2;
      }
   }

   public static Theme.ThemeInfo getCurrentTheme() {
      Theme.ThemeInfo var0 = currentDayTheme;
      if (var0 == null) {
         var0 = defaultTheme;
      }

      return var0;
   }

   public static String getCurrentThemeName() {
      String var0 = currentDayTheme.getName();
      String var1 = var0;
      if (var0.toLowerCase().endsWith(".attheme")) {
         var1 = var0.substring(0, var0.lastIndexOf(46));
      }

      return var1;
   }

   public static int getDefaultColor(String var0) {
      Integer var1 = (Integer)defaultColors.get(var0);
      if (var1 == null) {
         return var0.equals("chats_menuTopShadow") ? 0 : -65536;
      } else {
         return var1;
      }
   }

   public static HashMap getDefaultColors() {
      return defaultColors;
   }

   public static int getEventType() {
      Calendar var0 = Calendar.getInstance();
      var0.setTimeInMillis(System.currentTimeMillis());
      int var1 = var0.get(2);
      int var2 = var0.get(5);
      var0.get(12);
      var0.get(11);
      byte var3;
      if ((var1 != 11 || var2 < 24 || var2 > 31) && (var1 != 0 || var2 != 1)) {
         var3 = -1;
      } else {
         var3 = 0;
      }

      return var3;
   }

   public static Drawable getRoundRectSelectorDrawable(int var0) {
      if (VERSION.SDK_INT >= 21) {
         Drawable var4 = createRoundRectDrawable(AndroidUtilities.dp(3.0F), -1);
         return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{var0 & 16777215 | 419430400}), (Drawable)null, var4);
      } else {
         StateListDrawable var1 = new StateListDrawable();
         int var2 = AndroidUtilities.dp(3.0F);
         var0 = var0 & 16777215 | 419430400;
         Drawable var3 = createRoundRectDrawable(var2, var0);
         var1.addState(new int[]{16842919}, var3);
         var3 = createRoundRectDrawable(AndroidUtilities.dp(3.0F), var0);
         var1.addState(new int[]{16842913}, var3);
         var1.addState(StateSet.WILD_CARD, new ColorDrawable(0));
         return var1;
      }
   }

   public static long getSelectedBackgroundId() {
      SharedPreferences var0 = MessagesController.getGlobalMainSettings();
      long var1 = (long)var0.getInt("selectedBackground", 1000001);
      if (var1 != 1000001L) {
         var0.edit().putLong("selectedBackground2", var1).remove("selectedBackground").commit();
      }

      var1 = var0.getLong("selectedBackground2", 1000001L);
      if (hasWallpaperFromTheme() && !var0.getBoolean("overrideThemeWallpaper", false)) {
         return -2L;
      } else {
         return var1 == -2L ? 1000001L : var1;
      }
   }

   public static int getSelectedColor() {
      return selectedColor;
   }

   public static Drawable getSelectorDrawable(int var0, boolean var1) {
      if (var1) {
         if (VERSION.SDK_INT >= 21) {
            ColorDrawable var4 = new ColorDrawable(-1);
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{var0}), new ColorDrawable(getColor("windowBackgroundWhite")), var4);
         } else {
            StateListDrawable var2 = new StateListDrawable();
            ColorDrawable var3 = new ColorDrawable(var0);
            var2.addState(new int[]{16842919}, var3);
            var3 = new ColorDrawable(var0);
            var2.addState(new int[]{16842913}, var3);
            var2.addState(StateSet.WILD_CARD, new ColorDrawable(getColor("windowBackgroundWhite")));
            return var2;
         }
      } else {
         return createSelectorDrawable(var0, 2);
      }
   }

   public static Drawable getSelectorDrawable(boolean var0) {
      return getSelectorDrawable(getColor("listSelectorSDK21"), var0);
   }

   public static int getServiceMessageColor() {
      Integer var0 = (Integer)currentColors.get("chat_serviceBackground");
      int var1;
      if (var0 == null) {
         var1 = serviceMessageColor;
      } else {
         var1 = var0;
      }

      return var1;
   }

   public static ColorFilter getShareColorFilter(int var0, boolean var1) {
      if (var1) {
         if (currentShareSelectedColorFilter == null || currentShareSelectedColorFilterColor != var0) {
            currentShareSelectedColorFilterColor = var0;
            currentShareSelectedColorFilter = new PorterDuffColorFilter(var0, Mode.MULTIPLY);
         }

         return currentShareSelectedColorFilter;
      } else {
         if (currentShareColorFilter == null || currentShareColorFilterColor != var0) {
            currentShareColorFilterColor = var0;
            currentShareColorFilter = new PorterDuffColorFilter(var0, Mode.MULTIPLY);
         }

         return currentShareColorFilter;
      }
   }

   @SuppressLint({"PrivateApi"})
   private static Drawable getStateDrawable(Drawable var0, int var1) {
      if (StateListDrawable_getStateDrawableMethod == null) {
         try {
            StateListDrawable_getStateDrawableMethod = StateListDrawable.class.getDeclaredMethod("getStateDrawable", Integer.TYPE);
         } catch (Throwable var4) {
         }
      }

      Method var2 = StateListDrawable_getStateDrawableMethod;
      if (var2 == null) {
         return null;
      } else {
         try {
            var0 = (Drawable)var2.invoke(var0, var1);
            return var0;
         } catch (Exception var3) {
            return null;
         }
      }
   }

   private static HashMap getThemeFileValues(File param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static Drawable getThemedDrawable(Context var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         Drawable var3 = var0.getResources().getDrawable(var1).mutate();
         var3.setColorFilter(new PorterDuffColorFilter(var2, Mode.MULTIPLY));
         return var3;
      }
   }

   public static Drawable getThemedDrawable(Context var0, int var1, String var2) {
      return getThemedDrawable(var0, var1, getColor(var2));
   }

   public static Drawable getThemedWallpaper(boolean param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean hasThemeKey(String var0) {
      return currentColors.containsKey(var0);
   }

   public static boolean hasWallpaperFromTheme() {
      boolean var0;
      if (!currentColors.containsKey("chat_wallpaper") && themedWallpaperFileOffset <= 0) {
         var0 = false;
      } else {
         var0 = true;
      }

      return var0;
   }

   public static boolean isAnimatingColor() {
      boolean var0;
      if (animatingColors != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isCurrentThemeDefault() {
      boolean var0;
      if (currentTheme == defaultTheme) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isCurrentThemeNight() {
      boolean var0;
      if (currentTheme == currentNightTheme) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isCustomTheme() {
      return isCustomTheme;
   }

   public static boolean isPatternWallpaper() {
      return isPatternWallpaper;
   }

   public static boolean isWallpaperMotion() {
      return isWallpaperMotion;
   }

   // $FF: synthetic method
   static void lambda$applyTheme$1(boolean var0) {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, var0);
   }

   // $FF: synthetic method
   static void lambda$loadWallpaper$3() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static void lambda$null$2() {
      applyChatServiceMessageColor();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper);
   }

   // $FF: synthetic method
   static int lambda$sortThemes$0(Theme.ThemeInfo var0, Theme.ThemeInfo var1) {
      if (var0.pathToFile == null && var0.assetName == null) {
         return -1;
      } else {
         return var1.pathToFile == null && var1.assetName == null ? 1 : var0.name.compareTo(var1.name);
      }
   }

   public static void loadWallpaper() {
      if (wallpaper == null) {
         Utilities.searchQueue.postRunnable(_$$Lambda$Theme$g9IkSg8DwYzdCeZKlfMvOXD2f9I.INSTANCE);
      }
   }

   public static void reloadAllResources(Context var0) {
      destroyResources();
      if (chat_msgInDrawable != null) {
         chat_msgInDrawable = null;
         currentColor = 0;
         currentSelectedColor = 0;
         createChatResources(var0, false);
      }

      if (dialogs_namePaint != null) {
         dialogs_namePaint = null;
         createDialogsResources(var0);
      }

      if (profile_verifiedDrawable != null) {
         profile_verifiedDrawable = null;
         createProfileResources(var0);
      }

   }

   public static void reloadWallpaper() {
      wallpaper = null;
      themedWallpaper = null;
      loadWallpaper();
   }

   public static void saveAutoNightThemeConfig() {
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putInt("selectedAutoNightType", selectedAutoNightType);
      var0.putBoolean("autoNightScheduleByLocation", autoNightScheduleByLocation);
      var0.putFloat("autoNightBrighnessThreshold", autoNightBrighnessThreshold);
      var0.putInt("autoNightDayStartTime", autoNightDayStartTime);
      var0.putInt("autoNightDayEndTime", autoNightDayEndTime);
      var0.putInt("autoNightSunriseTime", autoNightSunriseTime);
      var0.putString("autoNightCityName", autoNightCityName);
      var0.putInt("autoNightSunsetTime", autoNightSunsetTime);
      var0.putLong("autoNightLocationLatitude3", Double.doubleToRawLongBits(autoNightLocationLatitude));
      var0.putLong("autoNightLocationLongitude3", Double.doubleToRawLongBits(autoNightLocationLongitude));
      var0.putInt("autoNightLastSunCheckDay", autoNightLastSunCheckDay);
      Theme.ThemeInfo var1 = currentNightTheme;
      if (var1 != null) {
         var0.putString("nighttheme", var1.name);
      } else {
         var0.remove("nighttheme");
      }

      var0.commit();
   }

   public static void saveCurrentTheme(String param0, boolean param1) {
      // $FF: Couldn't be decompiled
   }

   private static void saveOtherThemes() {
      Context var0 = ApplicationLoader.applicationContext;
      int var1 = 0;
      Editor var2 = var0.getSharedPreferences("themeconfig", 0).edit();

      JSONArray var4;
      for(var4 = new JSONArray(); var1 < otherThemes.size(); ++var1) {
         JSONObject var3 = ((Theme.ThemeInfo)otherThemes.get(var1)).getSaveJson();
         if (var3 != null) {
            var4.put(var3);
         }
      }

      var2.putString("themes2", var4.toString());
      var2.commit();
   }

   public static void setAnimatedColor(String var0, int var1) {
      HashMap var2 = animatingColors;
      if (var2 != null) {
         var2.put(var0, var1);
      }
   }

   public static void setAnimatingColor(boolean var0) {
      HashMap var1;
      if (var0) {
         var1 = new HashMap();
      } else {
         var1 = null;
      }

      animatingColors = var1;
   }

   public static void setColor(String var0, int var1, boolean var2) {
      int var3 = var1;
      if (var0.equals("chat_wallpaper")) {
         var3 = var1 | -16777216;
      }

      if (var2) {
         currentColors.remove(var0);
      } else {
         currentColors.put(var0, var3);
      }

      if (!var0.equals("chat_serviceBackground") && !var0.equals("chat_serviceBackgroundSelected")) {
         if (var0.equals("chat_wallpaper")) {
            reloadWallpaper();
         }
      } else {
         applyChatServiceMessageColor();
      }

   }

   public static void setCombinedDrawableColor(Drawable var0, int var1, boolean var2) {
      if (var0 instanceof CombinedDrawable) {
         if (var2) {
            var0 = ((CombinedDrawable)var0).getIcon();
         } else {
            var0 = ((CombinedDrawable)var0).getBackground();
         }

         if (var0 instanceof ColorDrawable) {
            ((ColorDrawable)var0).setColor(var1);
         } else {
            var0.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
         }

      }
   }

   public static void setCurrentNightTheme(Theme.ThemeInfo var0) {
      boolean var1;
      if (currentTheme == currentNightTheme) {
         var1 = true;
      } else {
         var1 = false;
      }

      currentNightTheme = var0;
      if (var1) {
         applyDayNightThemeMaybe(true);
      }

   }

   public static void setDrawableColor(Drawable var0, int var1) {
      if (var0 != null) {
         if (var0 instanceof ShapeDrawable) {
            ((ShapeDrawable)var0).getPaint().setColor(var1);
         } else if (var0 instanceof ScamDrawable) {
            ((ScamDrawable)var0).setColor(var1);
         } else {
            var0.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
         }

      }
   }

   public static void setDrawableColorByKey(Drawable var0, String var1) {
      if (var1 != null) {
         setDrawableColor(var0, getColor(var1));
      }
   }

   public static void setEmojiDrawableColor(Drawable var0, int var1, boolean var2) {
      if (var0 instanceof StateListDrawable) {
         boolean var10001;
         if (var2) {
            try {
               var0 = getStateDrawable(var0, 0);
               if (var0 instanceof ShapeDrawable) {
                  ((ShapeDrawable)var0).getPaint().setColor(var1);
                  return;
               }
            } catch (Throwable var6) {
               var10001 = false;
               return;
            }

            try {
               PorterDuffColorFilter var9 = new PorterDuffColorFilter(var1, Mode.MULTIPLY);
               var0.setColorFilter(var9);
            } catch (Throwable var4) {
               var10001 = false;
            }
         } else {
            Drawable var3;
            try {
               var3 = getStateDrawable(var0, 1);
               if (var3 instanceof ShapeDrawable) {
                  ((ShapeDrawable)var3).getPaint().setColor(var1);
                  return;
               }
            } catch (Throwable var7) {
               var10001 = false;
               return;
            }

            try {
               PorterDuffColorFilter var8 = new PorterDuffColorFilter(var1, Mode.MULTIPLY);
               var3.setColorFilter(var8);
            } catch (Throwable var5) {
               var10001 = false;
            }
         }
      }

   }

   public static void setSelectorDrawableColor(Drawable var0, int var1, boolean var2) {
      if (var0 instanceof StateListDrawable) {
         boolean var10001;
         Drawable var4;
         PorterDuffColorFilter var11;
         if (var2) {
            label67: {
               Drawable var3;
               try {
                  var3 = getStateDrawable(var0, 0);
                  if (var3 instanceof ShapeDrawable) {
                     ((ShapeDrawable)var3).getPaint().setColor(var1);
                     break label67;
                  }
               } catch (Throwable var9) {
                  var10001 = false;
                  return;
               }

               try {
                  PorterDuffColorFilter var13 = new PorterDuffColorFilter(var1, Mode.MULTIPLY);
                  var3.setColorFilter(var13);
               } catch (Throwable var7) {
                  var10001 = false;
                  return;
               }
            }

            try {
               var4 = getStateDrawable(var0, 1);
               if (var4 instanceof ShapeDrawable) {
                  ((ShapeDrawable)var4).getPaint().setColor(var1);
                  return;
               }
            } catch (Throwable var8) {
               var10001 = false;
               return;
            }

            try {
               var11 = new PorterDuffColorFilter(var1, Mode.MULTIPLY);
               var4.setColorFilter(var11);
            } catch (Throwable var5) {
               var10001 = false;
            }
         } else {
            try {
               var4 = getStateDrawable(var0, 2);
               if (var4 instanceof ShapeDrawable) {
                  ((ShapeDrawable)var4).getPaint().setColor(var1);
                  return;
               }
            } catch (Throwable var10) {
               var10001 = false;
               return;
            }

            try {
               var11 = new PorterDuffColorFilter(var1, Mode.MULTIPLY);
               var4.setColorFilter(var11);
            } catch (Throwable var6) {
               var10001 = false;
            }
         }
      } else if (VERSION.SDK_INT >= 21 && var0 instanceof RippleDrawable) {
         RippleDrawable var12 = (RippleDrawable)var0;
         if (var2) {
            var12.setColor(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{var1}));
         } else if (var12.getNumberOfLayers() > 0) {
            var0 = var12.getDrawable(0);
            if (var0 instanceof ShapeDrawable) {
               ((ShapeDrawable)var0).getPaint().setColor(var1);
            } else {
               var0.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
            }
         }
      }

   }

   public static void setThemeWallpaper(String var0, Bitmap var1, File var2) {
      currentColors.remove("chat_wallpaper");
      MessagesController.getGlobalMainSettings().edit().remove("overrideThemeWallpaper").commit();
      if (var1 != null) {
         themedWallpaper = new BitmapDrawable(var1);
         saveCurrentTheme(var0, false);
         calcBackgroundColor(themedWallpaper, 0);
         applyChatServiceMessageColor();
         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper);
      } else {
         themedWallpaper = null;
         wallpaper = null;
         saveCurrentTheme(var0, false);
         reloadWallpaper();
      }

   }

   private static void sortThemes() {
      Collections.sort(themes, _$$Lambda$Theme$CDAxGNnEyNa6tkvecQwKSXq77_I.INSTANCE);
   }

   private static class AttachCameraDrawable extends Drawable {
      private Paint paint = new Paint(1);
      private Path segment;

      public AttachCameraDrawable() {
         float var1 = (float)AndroidUtilities.dp(54.0F);
         RectF var2 = new RectF(0.0F, 0.0F, var1, var1);
         this.segment = new Path();
         this.segment.moveTo((float)AndroidUtilities.dp(23.0F), (float)AndroidUtilities.dp(20.0F));
         this.segment.lineTo((float)AndroidUtilities.dp(23.0F), 0.0F);
         this.segment.arcTo(var2, -98.0F, 50.0F, false);
         this.segment.close();
      }

      public void draw(Canvas var1) {
         var1.save();
         float var2 = (float)AndroidUtilities.dp(27.0F);
         var1.rotate(-90.0F, var2, var2);

         for(int var3 = 0; var3 < 6; ++var3) {
            if (var3 != 0) {
               if (var3 != 1) {
                  if (var3 != 2) {
                     if (var3 != 3) {
                        if (var3 != 4) {
                           if (var3 == 5) {
                              this.paint.setColor(Theme.getColor("chat_attachCameraIcon6"));
                           }
                        } else {
                           this.paint.setColor(Theme.getColor("chat_attachCameraIcon5"));
                        }
                     } else {
                        this.paint.setColor(Theme.getColor("chat_attachCameraIcon4"));
                     }
                  } else {
                     this.paint.setColor(Theme.getColor("chat_attachCameraIcon3"));
                  }
               } else {
                  this.paint.setColor(Theme.getColor("chat_attachCameraIcon2"));
               }
            } else {
               this.paint.setColor(Theme.getColor("chat_attachCameraIcon1"));
            }

            var1.rotate(60.0F, var2, var2);
            var1.drawPath(this.segment, this.paint);
         }

         var1.restore();
      }

      public int getIntrinsicHeight() {
         return AndroidUtilities.dp(54.0F);
      }

      public int getIntrinsicWidth() {
         return AndroidUtilities.dp(54.0F);
      }

      public int getMinimumHeight() {
         return AndroidUtilities.dp(54.0F);
      }

      public int getMinimumWidth() {
         return AndroidUtilities.dp(54.0F);
      }

      public int getOpacity() {
         return -2;
      }

      public void setAlpha(int var1) {
      }

      public void setColorFilter(ColorFilter var1) {
         this.invalidateSelf();
      }
   }

   public static class ThemeInfo {
      public String assetName;
      public String name;
      public String pathToFile;
      public int previewBackgroundColor;
      public int previewInColor;
      public int previewOutColor;
      public int sortIndex;

      public static Theme.ThemeInfo createWithJson(JSONObject var0) {
         if (var0 == null) {
            return null;
         } else {
            try {
               Theme.ThemeInfo var1 = new Theme.ThemeInfo();
               var1.name = var0.getString("name");
               var1.pathToFile = var0.getString("path");
               return var1;
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
               return null;
            }
         }
      }

      public static Theme.ThemeInfo createWithString(String var0) {
         if (TextUtils.isEmpty(var0)) {
            return null;
         } else {
            String[] var2 = var0.split("\\|");
            if (var2.length != 2) {
               return null;
            } else {
               Theme.ThemeInfo var1 = new Theme.ThemeInfo();
               var1.name = var2[0];
               var1.pathToFile = var2[1];
               return var1;
            }
         }
      }

      public String getName() {
         if ("Default".equals(this.name)) {
            return LocaleController.getString("Default", 2131559225);
         } else if ("Blue".equals(this.name)) {
            return LocaleController.getString("ThemeBlue", 2131560891);
         } else if ("Dark".equals(this.name)) {
            return LocaleController.getString("ThemeDark", 2131560892);
         } else if ("Dark Blue".equals(this.name)) {
            return LocaleController.getString("ThemeDarkBlue", 2131560893);
         } else if ("Graphite".equals(this.name)) {
            return LocaleController.getString("ThemeGraphite", 2131560894);
         } else {
            return "Arctic Blue".equals(this.name) ? LocaleController.getString("ThemeArcticBlue", 2131560890) : this.name;
         }
      }

      public JSONObject getSaveJson() {
         try {
            JSONObject var1 = new JSONObject();
            var1.put("name", this.name);
            var1.put("path", this.pathToFile);
            return var1;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
            return null;
         }
      }

      public boolean isDark() {
         boolean var1;
         if (!"Dark".equals(this.name) && !"Dark Blue".equals(this.name) && !"Graphite".equals(this.name)) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      public boolean isLight() {
         boolean var1;
         if (this.pathToFile == null && !this.isDark()) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }
}
