// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.ArrayList;
import android.util.SparseArray;

public class NotificationCenter
{
    public static final int FileDidFailUpload;
    public static final int FileDidUpload;
    public static final int FileLoadProgressChanged;
    public static final int FileUploadProgressChanged;
    private static volatile NotificationCenter[] Instance;
    public static final int albumsDidLoad;
    public static final int appDidLogout;
    public static final int archivedStickersCountDidLoad;
    public static final int audioDidSent;
    public static final int audioRecordTooShort;
    public static final int audioRouteChanged;
    public static final int blockedUsersDidLoad;
    public static final int botInfoDidLoad;
    public static final int botKeyboardDidLoad;
    public static final int cameraInitied;
    public static final int channelRightsUpdated;
    public static final int chatDidCreated;
    public static final int chatDidFailCreate;
    public static final int chatInfoCantLoad;
    public static final int chatInfoDidLoad;
    public static final int chatOnlineCountDidLoad;
    public static final int chatSearchResultsAvailable;
    public static final int chatSearchResultsLoading;
    public static final int closeChats;
    public static final int closeInCallActivity;
    public static final int closeOtherAppActivities;
    public static final int closeSearchByActiveAction;
    public static final int configLoaded;
    public static final int contactsDidLoad;
    public static final int contactsImported;
    public static final int dialogPhotosLoaded;
    public static final int dialogsNeedReload;
    public static final int dialogsUnreadCounterChanged;
    public static final int didCreatedNewDeleteTask;
    public static final int didEndedCall;
    public static final int didReceiveCall;
    public static final int didReceiveNewMessages;
    public static final int didReceiveSmsCode;
    public static final int didReceivedWebpages;
    public static final int didReceivedWebpagesInUpdates;
    public static final int didRemoveTwoStepPassword;
    public static final int didReplacedPhotoInMemCache;
    public static final int didSetNewTheme;
    public static final int didSetNewWallpapper;
    public static final int didSetPasscode;
    public static final int didSetTwoStepPassword;
    public static final int didStartedCall;
    public static final int didUpdateConnectionState;
    public static final int didUpdatePollResults;
    public static final int didUpdatedMessagesViews;
    public static final int emojiDidLoad;
    public static final int encryptedChatCreated;
    public static final int encryptedChatUpdated;
    public static final int featuredStickersDidLoad;
    public static final int fileDidFailedLoad;
    public static final int fileDidLoad;
    public static final int fileNewChunkAvailable;
    public static final int filePreparingFailed;
    public static final int filePreparingStarted;
    public static final int folderBecomeEmpty;
    private static volatile NotificationCenter globalInstance;
    public static final int groupStickersDidLoad;
    public static final int hasNewContactsToImport;
    public static final int historyCleared;
    public static final int httpFileDidFailedLoad;
    public static final int httpFileDidLoad;
    public static final int liveLocationsCacheChanged;
    public static final int liveLocationsChanged;
    public static final int locationPermissionGranted;
    public static final int mainUserInfoChanged;
    public static final int mediaCountDidLoad;
    public static final int mediaCountsDidLoad;
    public static final int mediaDidLoad;
    public static final int messagePlayingDidReset;
    public static final int messagePlayingDidSeek;
    public static final int messagePlayingDidStart;
    public static final int messagePlayingGoingToStop;
    public static final int messagePlayingPlayStateChanged;
    public static final int messagePlayingProgressDidChanged;
    public static final int messageReceivedByAck;
    public static final int messageReceivedByServer;
    public static final int messageSendError;
    public static final int messagesDeleted;
    public static final int messagesDidLoad;
    public static final int messagesRead;
    public static final int messagesReadContent;
    public static final int messagesReadEncrypted;
    public static final int musicDidLoad;
    public static final int needDeleteDialog;
    public static final int needReloadArchivedStickers;
    public static final int needReloadRecentDialogsSearch;
    public static final int needSetDayNightTheme;
    public static final int needShowAlert;
    public static final int newDraftReceived;
    public static final int newEmojiSuggestionsAvailable;
    public static final int newSessionReceived;
    public static final int notificationsCountUpdated;
    public static final int notificationsSettingsUpdated;
    public static final int openArticle;
    public static final int openedChatChanged;
    public static final int paymentFinished;
    public static final int peerSettingsDidLoad;
    public static final int pinnedMessageDidLoad;
    public static final int playerDidStartPlaying;
    public static final int privacyRulesUpdated;
    public static final int proxyCheckDone;
    public static final int proxySettingsChanged;
    public static final int pushMessagesUpdated;
    public static final int recentDocumentsDidLoad;
    public static final int recentImagesDidLoad;
    public static final int recordProgressChanged;
    public static final int recordStartError;
    public static final int recordStarted;
    public static final int recordStopped;
    public static final int reloadHints;
    public static final int reloadInlineHints;
    public static final int reloadInterface;
    public static final int removeAllMessagesFromDialog;
    public static final int replaceMessagesObjects;
    public static final int replyMessagesDidLoad;
    public static final int screenshotTook;
    public static final int stickersDidLoad;
    public static final int stopEncodingService;
    public static final int suggestedLangpack;
    public static final int themeListUpdated;
    private static int totalEvents = 1;
    public static final int updateInterfaces;
    public static final int updateMentionsCount;
    public static final int updateMessageMedia;
    public static final int userInfoDidLoad;
    public static final int videoLoadingStateChanged;
    public static final int wallpapersDidLoad;
    public static final int wallpapersNeedReload;
    public static final int wasUnableToFindCurrentLocation;
    private SparseArray<ArrayList<Object>> addAfterBroadcast;
    private int[] allowedNotifications;
    private boolean animationInProgress;
    private int broadcasting;
    private int currentAccount;
    private ArrayList<DelayedPost> delayedPosts;
    private SparseArray<ArrayList<Object>> observers;
    private SparseArray<ArrayList<Object>> removeAfterBroadcast;
    
    static {
        final int totalEvents = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents + 1;
        didReceiveNewMessages = totalEvents;
        final int totalEvents2 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents2 + 1;
        updateInterfaces = totalEvents2;
        final int totalEvents3 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents3 + 1;
        dialogsNeedReload = totalEvents3;
        final int totalEvents4 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents4 + 1;
        closeChats = totalEvents4;
        final int totalEvents5 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents5 + 1;
        messagesDeleted = totalEvents5;
        final int totalEvents6 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents6 + 1;
        historyCleared = totalEvents6;
        final int totalEvents7 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents7 + 1;
        messagesRead = totalEvents7;
        final int totalEvents8 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents8 + 1;
        messagesDidLoad = totalEvents8;
        final int totalEvents9 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents9 + 1;
        messageReceivedByAck = totalEvents9;
        final int totalEvents10 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents10 + 1;
        messageReceivedByServer = totalEvents10;
        final int totalEvents11 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents11 + 1;
        messageSendError = totalEvents11;
        final int totalEvents12 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents12 + 1;
        contactsDidLoad = totalEvents12;
        final int totalEvents13 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents13 + 1;
        contactsImported = totalEvents13;
        final int totalEvents14 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents14 + 1;
        hasNewContactsToImport = totalEvents14;
        final int totalEvents15 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents15 + 1;
        chatDidCreated = totalEvents15;
        final int totalEvents16 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents16 + 1;
        chatDidFailCreate = totalEvents16;
        final int totalEvents17 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents17 + 1;
        chatInfoDidLoad = totalEvents17;
        final int totalEvents18 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents18 + 1;
        chatInfoCantLoad = totalEvents18;
        final int totalEvents19 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents19 + 1;
        mediaDidLoad = totalEvents19;
        final int totalEvents20 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents20 + 1;
        mediaCountDidLoad = totalEvents20;
        final int totalEvents21 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents21 + 1;
        mediaCountsDidLoad = totalEvents21;
        final int totalEvents22 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents22 + 1;
        encryptedChatUpdated = totalEvents22;
        final int totalEvents23 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents23 + 1;
        messagesReadEncrypted = totalEvents23;
        final int totalEvents24 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents24 + 1;
        encryptedChatCreated = totalEvents24;
        final int totalEvents25 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents25 + 1;
        dialogPhotosLoaded = totalEvents25;
        final int totalEvents26 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents26 + 1;
        folderBecomeEmpty = totalEvents26;
        final int totalEvents27 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents27 + 1;
        removeAllMessagesFromDialog = totalEvents27;
        final int totalEvents28 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents28 + 1;
        notificationsSettingsUpdated = totalEvents28;
        final int totalEvents29 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents29 + 1;
        blockedUsersDidLoad = totalEvents29;
        final int totalEvents30 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents30 + 1;
        openedChatChanged = totalEvents30;
        final int totalEvents31 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents31 + 1;
        didCreatedNewDeleteTask = totalEvents31;
        final int totalEvents32 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents32 + 1;
        mainUserInfoChanged = totalEvents32;
        final int totalEvents33 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents33 + 1;
        privacyRulesUpdated = totalEvents33;
        final int totalEvents34 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents34 + 1;
        updateMessageMedia = totalEvents34;
        final int totalEvents35 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents35 + 1;
        recentImagesDidLoad = totalEvents35;
        final int totalEvents36 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents36 + 1;
        replaceMessagesObjects = totalEvents36;
        final int totalEvents37 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents37 + 1;
        didSetPasscode = totalEvents37;
        final int totalEvents38 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents38 + 1;
        didSetTwoStepPassword = totalEvents38;
        final int totalEvents39 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents39 + 1;
        didRemoveTwoStepPassword = totalEvents39;
        final int totalEvents40 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents40 + 1;
        replyMessagesDidLoad = totalEvents40;
        final int totalEvents41 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents41 + 1;
        pinnedMessageDidLoad = totalEvents41;
        final int totalEvents42 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents42 + 1;
        newSessionReceived = totalEvents42;
        final int totalEvents43 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents43 + 1;
        didReceivedWebpages = totalEvents43;
        final int totalEvents44 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents44 + 1;
        didReceivedWebpagesInUpdates = totalEvents44;
        final int totalEvents45 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents45 + 1;
        stickersDidLoad = totalEvents45;
        final int totalEvents46 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents46 + 1;
        featuredStickersDidLoad = totalEvents46;
        final int totalEvents47 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents47 + 1;
        groupStickersDidLoad = totalEvents47;
        final int totalEvents48 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents48 + 1;
        messagesReadContent = totalEvents48;
        final int totalEvents49 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents49 + 1;
        botInfoDidLoad = totalEvents49;
        final int totalEvents50 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents50 + 1;
        userInfoDidLoad = totalEvents50;
        final int totalEvents51 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents51 + 1;
        botKeyboardDidLoad = totalEvents51;
        final int totalEvents52 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents52 + 1;
        chatSearchResultsAvailable = totalEvents52;
        final int totalEvents53 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents53 + 1;
        chatSearchResultsLoading = totalEvents53;
        final int totalEvents54 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents54 + 1;
        musicDidLoad = totalEvents54;
        final int totalEvents55 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents55 + 1;
        needShowAlert = totalEvents55;
        final int totalEvents56 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents56 + 1;
        didUpdatedMessagesViews = totalEvents56;
        final int totalEvents57 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents57 + 1;
        needReloadRecentDialogsSearch = totalEvents57;
        final int totalEvents58 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents58 + 1;
        peerSettingsDidLoad = totalEvents58;
        final int totalEvents59 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents59 + 1;
        wasUnableToFindCurrentLocation = totalEvents59;
        final int totalEvents60 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents60 + 1;
        reloadHints = totalEvents60;
        final int totalEvents61 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents61 + 1;
        reloadInlineHints = totalEvents61;
        final int totalEvents62 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents62 + 1;
        newDraftReceived = totalEvents62;
        final int totalEvents63 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents63 + 1;
        recentDocumentsDidLoad = totalEvents63;
        final int totalEvents64 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents64 + 1;
        needReloadArchivedStickers = totalEvents64;
        final int totalEvents65 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents65 + 1;
        archivedStickersCountDidLoad = totalEvents65;
        final int totalEvents66 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents66 + 1;
        paymentFinished = totalEvents66;
        final int totalEvents67 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents67 + 1;
        channelRightsUpdated = totalEvents67;
        final int totalEvents68 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents68 + 1;
        openArticle = totalEvents68;
        final int totalEvents69 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents69 + 1;
        updateMentionsCount = totalEvents69;
        final int totalEvents70 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents70 + 1;
        didUpdatePollResults = totalEvents70;
        final int totalEvents71 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents71 + 1;
        chatOnlineCountDidLoad = totalEvents71;
        final int totalEvents72 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents72 + 1;
        videoLoadingStateChanged = totalEvents72;
        final int totalEvents73 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents73 + 1;
        httpFileDidLoad = totalEvents73;
        final int totalEvents74 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents74 + 1;
        httpFileDidFailedLoad = totalEvents74;
        final int totalEvents75 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents75 + 1;
        didUpdateConnectionState = totalEvents75;
        final int totalEvents76 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents76 + 1;
        FileDidUpload = totalEvents76;
        final int totalEvents77 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents77 + 1;
        FileDidFailUpload = totalEvents77;
        final int totalEvents78 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents78 + 1;
        FileUploadProgressChanged = totalEvents78;
        final int totalEvents79 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents79 + 1;
        FileLoadProgressChanged = totalEvents79;
        final int totalEvents80 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents80 + 1;
        fileDidLoad = totalEvents80;
        final int totalEvents81 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents81 + 1;
        fileDidFailedLoad = totalEvents81;
        final int totalEvents82 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents82 + 1;
        filePreparingStarted = totalEvents82;
        final int totalEvents83 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents83 + 1;
        fileNewChunkAvailable = totalEvents83;
        final int totalEvents84 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents84 + 1;
        filePreparingFailed = totalEvents84;
        final int totalEvents85 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents85 + 1;
        dialogsUnreadCounterChanged = totalEvents85;
        final int totalEvents86 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents86 + 1;
        messagePlayingProgressDidChanged = totalEvents86;
        final int totalEvents87 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents87 + 1;
        messagePlayingDidReset = totalEvents87;
        final int totalEvents88 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents88 + 1;
        messagePlayingPlayStateChanged = totalEvents88;
        final int totalEvents89 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents89 + 1;
        messagePlayingDidStart = totalEvents89;
        final int totalEvents90 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents90 + 1;
        messagePlayingDidSeek = totalEvents90;
        final int totalEvents91 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents91 + 1;
        messagePlayingGoingToStop = totalEvents91;
        final int totalEvents92 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents92 + 1;
        recordProgressChanged = totalEvents92;
        final int totalEvents93 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents93 + 1;
        recordStarted = totalEvents93;
        final int totalEvents94 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents94 + 1;
        recordStartError = totalEvents94;
        final int totalEvents95 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents95 + 1;
        recordStopped = totalEvents95;
        final int totalEvents96 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents96 + 1;
        screenshotTook = totalEvents96;
        final int totalEvents97 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents97 + 1;
        albumsDidLoad = totalEvents97;
        final int totalEvents98 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents98 + 1;
        audioDidSent = totalEvents98;
        final int totalEvents99 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents99 + 1;
        audioRecordTooShort = totalEvents99;
        final int totalEvents100 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents100 + 1;
        audioRouteChanged = totalEvents100;
        final int totalEvents101 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents101 + 1;
        didStartedCall = totalEvents101;
        final int totalEvents102 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents102 + 1;
        didEndedCall = totalEvents102;
        final int totalEvents103 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents103 + 1;
        closeInCallActivity = totalEvents103;
        final int totalEvents104 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents104 + 1;
        appDidLogout = totalEvents104;
        final int totalEvents105 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents105 + 1;
        configLoaded = totalEvents105;
        final int totalEvents106 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents106 + 1;
        needDeleteDialog = totalEvents106;
        final int totalEvents107 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents107 + 1;
        newEmojiSuggestionsAvailable = totalEvents107;
        final int totalEvents108 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents108 + 1;
        pushMessagesUpdated = totalEvents108;
        final int totalEvents109 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents109 + 1;
        stopEncodingService = totalEvents109;
        final int totalEvents110 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents110 + 1;
        wallpapersDidLoad = totalEvents110;
        final int totalEvents111 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents111 + 1;
        wallpapersNeedReload = totalEvents111;
        final int totalEvents112 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents112 + 1;
        didReceiveSmsCode = totalEvents112;
        final int totalEvents113 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents113 + 1;
        didReceiveCall = totalEvents113;
        final int totalEvents114 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents114 + 1;
        emojiDidLoad = totalEvents114;
        final int totalEvents115 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents115 + 1;
        closeOtherAppActivities = totalEvents115;
        final int totalEvents116 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents116 + 1;
        cameraInitied = totalEvents116;
        final int totalEvents117 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents117 + 1;
        didReplacedPhotoInMemCache = totalEvents117;
        final int totalEvents118 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents118 + 1;
        didSetNewTheme = totalEvents118;
        final int totalEvents119 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents119 + 1;
        themeListUpdated = totalEvents119;
        final int totalEvents120 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents120 + 1;
        needSetDayNightTheme = totalEvents120;
        final int totalEvents121 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents121 + 1;
        locationPermissionGranted = totalEvents121;
        final int totalEvents122 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents122 + 1;
        reloadInterface = totalEvents122;
        final int totalEvents123 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents123 + 1;
        suggestedLangpack = totalEvents123;
        final int totalEvents124 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents124 + 1;
        didSetNewWallpapper = totalEvents124;
        final int totalEvents125 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents125 + 1;
        proxySettingsChanged = totalEvents125;
        final int totalEvents126 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents126 + 1;
        proxyCheckDone = totalEvents126;
        final int totalEvents127 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents127 + 1;
        liveLocationsChanged = totalEvents127;
        final int totalEvents128 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents128 + 1;
        liveLocationsCacheChanged = totalEvents128;
        final int totalEvents129 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents129 + 1;
        notificationsCountUpdated = totalEvents129;
        final int totalEvents130 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents130 + 1;
        playerDidStartPlaying = totalEvents130;
        final int totalEvents131 = NotificationCenter.totalEvents;
        NotificationCenter.totalEvents = totalEvents131 + 1;
        closeSearchByActiveAction = totalEvents131;
        NotificationCenter.Instance = new NotificationCenter[3];
    }
    
    public NotificationCenter(final int currentAccount) {
        this.observers = (SparseArray<ArrayList<Object>>)new SparseArray();
        this.removeAfterBroadcast = (SparseArray<ArrayList<Object>>)new SparseArray();
        this.addAfterBroadcast = (SparseArray<ArrayList<Object>>)new SparseArray();
        this.delayedPosts = new ArrayList<DelayedPost>(10);
        this.broadcasting = 0;
        this.currentAccount = currentAccount;
    }
    
    public static NotificationCenter getGlobalInstance() {
        final NotificationCenter globalInstance;
        if ((globalInstance = NotificationCenter.globalInstance) == null) {
            synchronized (NotificationCenter.class) {
                if (NotificationCenter.globalInstance == null) {
                    NotificationCenter.globalInstance = new NotificationCenter(-1);
                }
            }
        }
        return globalInstance;
    }
    
    public static NotificationCenter getInstance(final int n) {
        final NotificationCenter notificationCenter;
        if ((notificationCenter = NotificationCenter.Instance[n]) == null) {
            synchronized (NotificationCenter.class) {
                if (NotificationCenter.Instance[n] == null) {
                    NotificationCenter.Instance[n] = new NotificationCenter(n);
                }
            }
        }
        return notificationCenter;
    }
    
    public void addObserver(final Object e, final int n) {
        if (BuildVars.DEBUG_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            throw new RuntimeException("addObserver allowed only from MAIN thread");
        }
        if (this.broadcasting != 0) {
            ArrayList<Object> list;
            if ((list = (ArrayList<Object>)this.addAfterBroadcast.get(n)) == null) {
                list = new ArrayList<Object>();
                this.addAfterBroadcast.put(n, (Object)list);
            }
            list.add(e);
            return;
        }
        ArrayList<Object> list2;
        if ((list2 = (ArrayList<Object>)this.observers.get(n)) == null) {
            final SparseArray<ArrayList<Object>> observers = this.observers;
            list2 = new ArrayList<Object>();
            observers.put(n, (Object)list2);
        }
        if (list2.contains(e)) {
            return;
        }
        list2.add(e);
    }
    
    public boolean hasObservers(final int n) {
        return this.observers.indexOfKey(n) >= 0;
    }
    
    public boolean isAnimationInProgress() {
        return this.animationInProgress;
    }
    
    public void postNotificationName(final int n, final Object... array) {
        final int[] allowedNotifications = this.allowedNotifications;
        boolean b = false;
        if (allowedNotifications != null) {
            int n2 = 0;
            while (true) {
                final int[] allowedNotifications2 = this.allowedNotifications;
                b = b;
                if (n2 >= allowedNotifications2.length) {
                    break;
                }
                if (allowedNotifications2[n2] == n) {
                    b = true;
                    break;
                }
                ++n2;
            }
        }
        this.postNotificationNameInternal(n, b, array);
    }
    
    public void postNotificationNameInternal(int i, final boolean b, final Object... array) {
        if (BuildVars.DEBUG_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            throw new RuntimeException("postNotificationName allowed only from MAIN thread");
        }
        if (!b && this.animationInProgress) {
            this.delayedPosts.add(new DelayedPost(i, array));
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("delay post notification ");
                sb.append(i);
                sb.append(" with args count = ");
                sb.append(array.length);
                FileLog.e(sb.toString());
            }
            return;
        }
        ++this.broadcasting;
        final ArrayList list = (ArrayList)this.observers.get(i);
        if (list != null && !list.isEmpty()) {
            for (int j = 0; j < list.size(); ++j) {
                list.get(j).didReceivedNotification(i, this.currentAccount, array);
            }
        }
        --this.broadcasting;
        if (this.broadcasting == 0) {
            if (this.removeAfterBroadcast.size() != 0) {
                int key;
                ArrayList list2;
                int k;
                for (i = 0; i < this.removeAfterBroadcast.size(); ++i) {
                    key = this.removeAfterBroadcast.keyAt(i);
                    for (list2 = (ArrayList)this.removeAfterBroadcast.get(key), k = 0; k < list2.size(); ++k) {
                        this.removeObserver(list2.get(k), key);
                    }
                }
                this.removeAfterBroadcast.clear();
            }
            if (this.addAfterBroadcast.size() != 0) {
                int key2;
                ArrayList list3;
                int l;
                for (i = 0; i < this.addAfterBroadcast.size(); ++i) {
                    key2 = this.addAfterBroadcast.keyAt(i);
                    for (list3 = (ArrayList)this.addAfterBroadcast.get(key2), l = 0; l < list3.size(); ++l) {
                        this.addObserver(list3.get(l), key2);
                    }
                }
                this.addAfterBroadcast.clear();
            }
        }
    }
    
    public void removeObserver(final Object o, final int n) {
        if (BuildVars.DEBUG_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            throw new RuntimeException("removeObserver allowed only from MAIN thread");
        }
        if (this.broadcasting != 0) {
            ArrayList<Object> list;
            if ((list = (ArrayList<Object>)this.removeAfterBroadcast.get(n)) == null) {
                list = new ArrayList<Object>();
                this.removeAfterBroadcast.put(n, (Object)list);
            }
            list.add(o);
            return;
        }
        final ArrayList list2 = (ArrayList)this.observers.get(n);
        if (list2 != null) {
            list2.remove(o);
        }
    }
    
    public void setAllowedNotificationsDutingAnimation(final int[] allowedNotifications) {
        this.allowedNotifications = allowedNotifications;
    }
    
    public void setAnimationInProgress(final boolean animationInProgress) {
        this.animationInProgress = animationInProgress;
        if (!this.animationInProgress && !this.delayedPosts.isEmpty()) {
            for (int i = 0; i < this.delayedPosts.size(); ++i) {
                final DelayedPost delayedPost = this.delayedPosts.get(i);
                this.postNotificationNameInternal(delayedPost.id, true, delayedPost.args);
            }
            this.delayedPosts.clear();
        }
    }
    
    private class DelayedPost
    {
        private Object[] args;
        private int id;
        
        private DelayedPost(final int id, final Object[] args) {
            this.id = id;
            this.args = args;
        }
    }
    
    public interface NotificationCenterDelegate
    {
        void didReceivedNotification(final int p0, final int p1, final Object... p2);
    }
}
