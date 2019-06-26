// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import java.util.Iterator;
import java.util.Map;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import android.content.SharedPreferences$Editor;
import java.io.Serializable;
import android.content.SharedPreferences;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadController implements NotificationCenterDelegate
{
    public static final int AUTODOWNLOAD_TYPE_AUDIO = 2;
    public static final int AUTODOWNLOAD_TYPE_DOCUMENT = 8;
    public static final int AUTODOWNLOAD_TYPE_PHOTO = 1;
    public static final int AUTODOWNLOAD_TYPE_VIDEO = 4;
    private static volatile DownloadController[] Instance;
    public static final int PRESET_NUM_CHANNEL = 3;
    public static final int PRESET_NUM_CONTACT = 0;
    public static final int PRESET_NUM_GROUP = 2;
    public static final int PRESET_NUM_PM = 1;
    public static final int PRESET_SIZE_NUM_AUDIO = 3;
    public static final int PRESET_SIZE_NUM_DOCUMENT = 2;
    public static final int PRESET_SIZE_NUM_PHOTO = 0;
    public static final int PRESET_SIZE_NUM_VIDEO = 1;
    private HashMap<String, FileDownloadProgressListener> addLaterArray;
    private ArrayList<DownloadObject> audioDownloadQueue;
    private int currentAccount;
    public int currentMobilePreset;
    public int currentRoamingPreset;
    public int currentWifiPreset;
    private ArrayList<FileDownloadProgressListener> deleteLaterArray;
    private ArrayList<DownloadObject> documentDownloadQueue;
    private HashMap<String, DownloadObject> downloadQueueKeys;
    public Preset highPreset;
    private int lastCheckMask;
    private int lastTag;
    private boolean listenerInProgress;
    private boolean loadingAutoDownloadConfig;
    private HashMap<String, ArrayList<MessageObject>> loadingFileMessagesObservers;
    private HashMap<String, ArrayList<WeakReference<FileDownloadProgressListener>>> loadingFileObservers;
    public Preset lowPreset;
    public Preset mediumPreset;
    public Preset mobilePreset;
    private SparseArray<String> observersByTag;
    private ArrayList<DownloadObject> photoDownloadQueue;
    public Preset roamingPreset;
    private LongSparseArray<Long> typingTimes;
    private ArrayList<DownloadObject> videoDownloadQueue;
    public Preset wifiPreset;
    
    static {
        DownloadController.Instance = new DownloadController[3];
    }
    
    public DownloadController(int i) {
        this.lastCheckMask = 0;
        this.photoDownloadQueue = new ArrayList<DownloadObject>();
        this.audioDownloadQueue = new ArrayList<DownloadObject>();
        this.documentDownloadQueue = new ArrayList<DownloadObject>();
        this.videoDownloadQueue = new ArrayList<DownloadObject>();
        this.downloadQueueKeys = new HashMap<String, DownloadObject>();
        this.loadingFileObservers = new HashMap<String, ArrayList<WeakReference<FileDownloadProgressListener>>>();
        this.loadingFileMessagesObservers = new HashMap<String, ArrayList<MessageObject>>();
        this.observersByTag = (SparseArray<String>)new SparseArray();
        this.listenerInProgress = false;
        this.addLaterArray = new HashMap<String, FileDownloadProgressListener>();
        this.deleteLaterArray = new ArrayList<FileDownloadProgressListener>();
        this.lastTag = 0;
        this.typingTimes = (LongSparseArray<Long>)new LongSparseArray();
        this.currentAccount = i;
        final SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
        this.lowPreset = new Preset(mainSettings.getString("preset0", "1_1_1_1_1048576_512000_512000_524288_0_0_1_1"));
        this.mediumPreset = new Preset(mainSettings.getString("preset1", "13_13_13_13_1048576_10485760_1048576_524288_1_1_1_0"));
        this.highPreset = new Preset(mainSettings.getString("preset2", "13_13_13_13_1048576_15728640_3145728_524288_1_1_1_0"));
        if (!mainSettings.contains("newConfig") && UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            i = 0;
        }
        else {
            i = 1;
        }
        final String s = "currentWifiPreset";
        if (i != 0) {
            this.mobilePreset = new Preset(mainSettings.getString("mobilePreset", this.mediumPreset.toString()));
            this.wifiPreset = new Preset(mainSettings.getString("wifiPreset", this.highPreset.toString()));
            this.roamingPreset = new Preset(mainSettings.getString("roamingPreset", this.lowPreset.toString()));
            this.currentMobilePreset = mainSettings.getInt("currentMobilePreset", 3);
            this.currentWifiPreset = mainSettings.getInt("currentWifiPreset", 3);
            this.currentRoamingPreset = mainSettings.getInt("currentRoamingPreset", 3);
            if (i == 0) {
                mainSettings.edit().putBoolean("newConfig", true).commit();
            }
        }
        else {
            final int[] array = new int[4];
            final int[] array2 = new int[4];
            final int[] array3 = new int[4];
            final int[] array4 = new int[7];
            final int[] array5 = new int[7];
            final int[] array6 = new int[7];
            StringBuilder sb;
            Serializable value;
            String string;
            StringBuilder sb2;
            Serializable value2;
            StringBuilder sb3;
            Serializable value3;
            for (i = 0; i < 4; ++i) {
                sb = new StringBuilder();
                sb.append("mobileDataDownloadMask");
                value = "";
                if (i != 0) {
                    value = i;
                }
                sb.append(value);
                string = sb.toString();
                if (i != 0 && !mainSettings.contains(string)) {
                    array[i] = array[0];
                    array2[i] = array2[0];
                    array3[i] = array3[0];
                }
                else {
                    array[i] = mainSettings.getInt(string, 13);
                    sb2 = new StringBuilder();
                    sb2.append("wifiDownloadMask");
                    if (i == 0) {
                        value2 = "";
                    }
                    else {
                        value2 = i;
                    }
                    sb2.append(value2);
                    array2[i] = mainSettings.getInt(sb2.toString(), 13);
                    sb3 = new StringBuilder();
                    sb3.append("roamingDownloadMask");
                    if (i == 0) {
                        value3 = "";
                    }
                    else {
                        value3 = i;
                    }
                    sb3.append(value3);
                    array3[i] = mainSettings.getInt(sb3.toString(), 1);
                }
            }
            array4[2] = mainSettings.getInt("mobileMaxDownloadSize2", this.mediumPreset.sizes[1]);
            array4[3] = mainSettings.getInt("mobileMaxDownloadSize3", this.mediumPreset.sizes[2]);
            array5[2] = mainSettings.getInt("wifiMaxDownloadSize2", this.highPreset.sizes[1]);
            array5[3] = mainSettings.getInt("wifiMaxDownloadSize3", this.highPreset.sizes[2]);
            array6[2] = mainSettings.getInt("roamingMaxDownloadSize2", this.lowPreset.sizes[1]);
            array6[3] = mainSettings.getInt("roamingMaxDownloadSize3", this.lowPreset.sizes[2]);
            final boolean boolean1 = mainSettings.getBoolean("globalAutodownloadEnabled", true);
            this.mobilePreset = new Preset(array, this.mediumPreset.sizes[0], array4[2], array4[3], true, true, boolean1, false);
            this.wifiPreset = new Preset(array2, this.highPreset.sizes[0], array5[2], array5[3], true, true, boolean1, false);
            this.roamingPreset = new Preset(array3, this.lowPreset.sizes[0], array6[2], array6[3], false, false, boolean1, true);
            final SharedPreferences$Editor edit = mainSettings.edit();
            edit.putBoolean("newConfig", true);
            edit.putString("mobilePreset", this.mobilePreset.toString());
            edit.putString("wifiPreset", this.wifiPreset.toString());
            edit.putString("roamingPreset", this.roamingPreset.toString());
            edit.putInt("currentMobilePreset", this.currentMobilePreset = 3);
            edit.putInt(s, this.currentWifiPreset = 3);
            edit.putInt("currentRoamingPreset", this.currentRoamingPreset = 3);
            edit.commit();
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$DownloadController$TvQOK4BckOSg64NROgC4NLSY7xY(this));
        ApplicationLoader.applicationContext.registerReceiver((BroadcastReceiver)new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                DownloadController.this.checkAutodownloadSettings();
            }
        }, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            this.checkAutodownloadSettings();
        }
    }
    
    private void checkDownloadFinished(final String s, int type) {
        final DownloadObject downloadObject = this.downloadQueueKeys.get(s);
        if (downloadObject != null) {
            this.downloadQueueKeys.remove(s);
            if (type == 0 || type == 2) {
                MessagesStorage.getInstance(this.currentAccount).removeFromDownloadQueue(downloadObject.id, downloadObject.type, false);
            }
            type = downloadObject.type;
            if (type == 1) {
                this.photoDownloadQueue.remove(downloadObject);
                if (this.photoDownloadQueue.isEmpty()) {
                    this.newDownloadObjectsAvailable(1);
                }
            }
            else if (type == 2) {
                this.audioDownloadQueue.remove(downloadObject);
                if (this.audioDownloadQueue.isEmpty()) {
                    this.newDownloadObjectsAvailable(2);
                }
            }
            else if (type == 4) {
                this.videoDownloadQueue.remove(downloadObject);
                if (this.videoDownloadQueue.isEmpty()) {
                    this.newDownloadObjectsAvailable(4);
                }
            }
            else if (type == 8) {
                this.documentDownloadQueue.remove(downloadObject);
                if (this.documentDownloadQueue.isEmpty()) {
                    this.newDownloadObjectsAvailable(8);
                }
            }
        }
    }
    
    public static DownloadController getInstance(final int n) {
        final DownloadController downloadController;
        if ((downloadController = DownloadController.Instance[n]) == null) {
            synchronized (DownloadController.class) {
                if (DownloadController.Instance[n] == null) {
                    DownloadController.Instance[n] = new DownloadController(n);
                }
            }
        }
        return downloadController;
    }
    
    private void processLaterArrays() {
        for (final Map.Entry<String, FileDownloadProgressListener> entry : this.addLaterArray.entrySet()) {
            this.addLoadingFileObserver(entry.getKey(), entry.getValue());
        }
        this.addLaterArray.clear();
        final Iterator<FileDownloadProgressListener> iterator2 = this.deleteLaterArray.iterator();
        while (iterator2.hasNext()) {
            this.removeLoadingFileObserver(iterator2.next());
        }
        this.deleteLaterArray.clear();
    }
    
    public static int typeToIndex(final int n) {
        if (n == 1) {
            return 0;
        }
        if (n == 2) {
            return 3;
        }
        if (n == 4) {
            return 1;
        }
        if (n == 8) {
            return 2;
        }
        return 0;
    }
    
    public void addLoadingFileObserver(final String s, final FileDownloadProgressListener fileDownloadProgressListener) {
        this.addLoadingFileObserver(s, null, fileDownloadProgressListener);
    }
    
    public void addLoadingFileObserver(final String key, final MessageObject e, final FileDownloadProgressListener fileDownloadProgressListener) {
        if (this.listenerInProgress) {
            this.addLaterArray.put(key, fileDownloadProgressListener);
            return;
        }
        this.removeLoadingFileObserver(fileDownloadProgressListener);
        ArrayList<WeakReference<FileDownloadProgressListener>> value;
        if ((value = this.loadingFileObservers.get(key)) == null) {
            value = new ArrayList<WeakReference<FileDownloadProgressListener>>();
            this.loadingFileObservers.put(key, value);
        }
        value.add(new WeakReference<FileDownloadProgressListener>(fileDownloadProgressListener));
        if (e != null) {
            ArrayList<MessageObject> value2;
            if ((value2 = this.loadingFileMessagesObservers.get(key)) == null) {
                value2 = new ArrayList<MessageObject>();
                this.loadingFileMessagesObservers.put(key, value2);
            }
            value2.add(e);
        }
        this.observersByTag.put(fileDownloadProgressListener.getObserverTag(), (Object)key);
    }
    
    public int canDownloadMedia(final TLRPC.Message message) {
        final int n = 0;
        final int n2 = 0;
        if (message == null) {
            return 0;
        }
        final boolean videoMessage = MessageObject.isVideoMessage(message);
        int n3;
        if (!videoMessage && !MessageObject.isGifMessage(message) && !MessageObject.isRoundVideoMessage(message) && !MessageObject.isGameMessage(message)) {
            if (MessageObject.isVoiceMessage(message)) {
                n3 = 2;
            }
            else if (!MessageObject.isPhoto(message) && !MessageObject.isStickerMessage(message)) {
                if (MessageObject.getDocument(message) == null) {
                    return 0;
                }
                n3 = 8;
            }
            else {
                n3 = 1;
            }
        }
        else {
            n3 = 4;
        }
        final TLRPC.Peer to_id = message.to_id;
        int n4 = 0;
        Label_0245: {
            Label_0242: {
                if (to_id != null) {
                    Label_0143: {
                        if (to_id.user_id == 0) {
                            if (to_id.chat_id != 0) {
                                if (message.from_id != 0 && ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(message.from_id)) {
                                    break Label_0143;
                                }
                            }
                            else {
                                if (!MessageObject.isMegagroup(message)) {
                                    n4 = 3;
                                    break Label_0245;
                                }
                                if (message.from_id != 0 && ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(message.from_id)) {
                                    break Label_0143;
                                }
                            }
                            n4 = 2;
                            break Label_0245;
                        }
                        if (!ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(to_id.user_id)) {
                            break Label_0242;
                        }
                    }
                    n4 = 0;
                    break Label_0245;
                }
            }
            n4 = 1;
        }
        Preset preset;
        if (ApplicationLoader.isConnectedToWiFi()) {
            if (!this.wifiPreset.enabled) {
                return 0;
            }
            preset = this.getCurrentWiFiPreset();
        }
        else if (ApplicationLoader.isRoaming()) {
            if (!this.roamingPreset.enabled) {
                return 0;
            }
            preset = this.getCurrentRoamingPreset();
        }
        else {
            if (!this.mobilePreset.enabled) {
                return 0;
            }
            preset = this.getCurrentMobilePreset();
        }
        final int n5 = preset.mask[n4];
        final int n6 = preset.sizes[typeToIndex(n3)];
        final int messageSize = MessageObject.getMessageSize(message);
        if (videoMessage && preset.preloadVideo && messageSize > n6 && n6 > 2097152) {
            int n7 = n2;
            if ((n5 & n3) != 0x0) {
                n7 = 2;
            }
            return n7;
        }
        if (n3 != 1) {
            int n8 = n;
            if (messageSize == 0) {
                return n8;
            }
            n8 = n;
            if (messageSize > n6) {
                return n8;
            }
        }
        if (n3 != 2) {
            final int n8 = n;
            if ((n5 & n3) == 0x0) {
                return n8;
            }
        }
        return 1;
    }
    
    public boolean canDownloadMedia(final int n, final int n2) {
        final boolean connectedToWiFi = ApplicationLoader.isConnectedToWiFi();
        final boolean b = false;
        Preset preset;
        if (connectedToWiFi) {
            if (!this.wifiPreset.enabled) {
                return false;
            }
            preset = this.getCurrentWiFiPreset();
        }
        else if (ApplicationLoader.isRoaming()) {
            if (!this.roamingPreset.enabled) {
                return false;
            }
            preset = this.getCurrentRoamingPreset();
        }
        else {
            if (!this.mobilePreset.enabled) {
                return false;
            }
            preset = this.getCurrentMobilePreset();
        }
        final int n3 = preset.mask[1];
        final int n4 = preset.sizes[typeToIndex(n)];
        if (n != 1) {
            boolean b2 = b;
            if (n2 == 0) {
                return b2;
            }
            b2 = b;
            if (n2 > n4) {
                return b2;
            }
        }
        if (n != 2) {
            final boolean b2 = b;
            if ((n & n3) == 0x0) {
                return b2;
            }
        }
        return true;
    }
    
    public boolean canDownloadMedia(final MessageObject messageObject) {
        final int canDownloadMedia = this.canDownloadMedia(messageObject.messageOwner);
        boolean b = true;
        if (canDownloadMedia != 1) {
            b = false;
        }
        return b;
    }
    
    protected boolean canDownloadNextTrack() {
        final boolean connectedToWiFi = ApplicationLoader.isConnectedToWiFi();
        final boolean b = true;
        final boolean b2 = true;
        boolean b3 = true;
        if (connectedToWiFi) {
            if (!this.wifiPreset.enabled || !this.getCurrentWiFiPreset().preloadMusic) {
                b3 = false;
            }
            return b3;
        }
        if (ApplicationLoader.isRoaming()) {
            return this.roamingPreset.enabled && this.getCurrentRoamingPreset().preloadMusic && b;
        }
        return this.mobilePreset.enabled && this.getCurrentMobilePreset().preloadMusic && b2;
    }
    
    public void checkAutodownloadSettings() {
        final int currentDownloadMask = this.getCurrentDownloadMask();
        if (currentDownloadMask == this.lastCheckMask) {
            return;
        }
        this.lastCheckMask = currentDownloadMask;
        if ((currentDownloadMask & 0x1) != 0x0) {
            if (this.photoDownloadQueue.isEmpty()) {
                this.newDownloadObjectsAvailable(1);
            }
        }
        else {
            for (int i = 0; i < this.photoDownloadQueue.size(); ++i) {
                final DownloadObject downloadObject = this.photoDownloadQueue.get(i);
                final TLObject object = downloadObject.object;
                if (object instanceof TLRPC.Photo) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)object).sizes, AndroidUtilities.getPhotoSize()));
                }
                else if (object instanceof TLRPC.Document) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile((TLRPC.Document)downloadObject.object);
                }
            }
            this.photoDownloadQueue.clear();
        }
        if ((currentDownloadMask & 0x2) != 0x0) {
            if (this.audioDownloadQueue.isEmpty()) {
                this.newDownloadObjectsAvailable(2);
            }
        }
        else {
            for (int j = 0; j < this.audioDownloadQueue.size(); ++j) {
                FileLoader.getInstance(this.currentAccount).cancelLoadFile((TLRPC.Document)this.audioDownloadQueue.get(j).object);
            }
            this.audioDownloadQueue.clear();
        }
        if ((currentDownloadMask & 0x8) != 0x0) {
            if (this.documentDownloadQueue.isEmpty()) {
                this.newDownloadObjectsAvailable(8);
            }
        }
        else {
            for (int k = 0; k < this.documentDownloadQueue.size(); ++k) {
                FileLoader.getInstance(this.currentAccount).cancelLoadFile((TLRPC.Document)this.documentDownloadQueue.get(k).object);
            }
            this.documentDownloadQueue.clear();
        }
        if ((currentDownloadMask & 0x4) != 0x0) {
            if (this.videoDownloadQueue.isEmpty()) {
                this.newDownloadObjectsAvailable(4);
            }
        }
        else {
            for (int l = 0; l < this.videoDownloadQueue.size(); ++l) {
                FileLoader.getInstance(this.currentAccount).cancelLoadFile((TLRPC.Document)this.videoDownloadQueue.get(l).object);
            }
            this.videoDownloadQueue.clear();
        }
        final int autodownloadMaskAll = this.getAutodownloadMaskAll();
        if (autodownloadMaskAll == 0) {
            MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(0);
        }
        else {
            if ((autodownloadMaskAll & 0x1) == 0x0) {
                MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(1);
            }
            if ((autodownloadMaskAll & 0x2) == 0x0) {
                MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(2);
            }
            if ((autodownloadMaskAll & 0x4) == 0x0) {
                MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(4);
            }
            if ((autodownloadMaskAll & 0x8) == 0x0) {
                MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(8);
            }
        }
    }
    
    public void cleanup() {
        this.photoDownloadQueue.clear();
        this.audioDownloadQueue.clear();
        this.documentDownloadQueue.clear();
        this.videoDownloadQueue.clear();
        this.downloadQueueKeys.clear();
        this.typingTimes.clear();
    }
    
    @Override
    public void didReceivedNotification(int i, int n, final Object... array) {
        if (i != NotificationCenter.fileDidFailedLoad && i != NotificationCenter.httpFileDidFailedLoad) {
            if (i != NotificationCenter.fileDidLoad) {
                if (i != NotificationCenter.httpFileDidLoad) {
                    if (i == NotificationCenter.FileLoadProgressChanged) {
                        this.listenerInProgress = true;
                        final String key = (String)array[0];
                        final ArrayList<WeakReference<FileDownloadProgressListener>> list = this.loadingFileObservers.get(key);
                        if (list != null) {
                            final Float n2 = (Float)array[1];
                            WeakReference<FileDownloadProgressListener> weakReference;
                            for (n = list.size(), i = 0; i < n; ++i) {
                                weakReference = list.get(i);
                                if (weakReference.get() != null) {
                                    ((FileDownloadProgressListener)weakReference.get()).onProgressDownload(key, n2);
                                }
                            }
                        }
                        this.listenerInProgress = false;
                        this.processLaterArrays();
                        return;
                    }
                    if (i != NotificationCenter.FileUploadProgressChanged) {
                        return;
                    }
                    this.listenerInProgress = true;
                    final String s = (String)array[0];
                    final ArrayList<WeakReference<FileDownloadProgressListener>> list2 = this.loadingFileObservers.get(s);
                    if (list2 != null) {
                        final Float n3 = (Float)array[1];
                        final Boolean b = (Boolean)array[2];
                        WeakReference<FileDownloadProgressListener> weakReference2;
                        for (n = list2.size(), i = 0; i < n; ++i) {
                            weakReference2 = list2.get(i);
                            if (weakReference2.get() != null) {
                                ((FileDownloadProgressListener)weakReference2.get()).onProgressUpload(s, n3, b);
                            }
                        }
                    }
                    this.listenerInProgress = false;
                    this.processLaterArrays();
                    try {
                        final ArrayList<SendMessagesHelper.DelayedMessage> delayedMessages = SendMessagesHelper.getInstance(this.currentAccount).getDelayedMessages(s);
                        if (delayedMessages != null) {
                            SendMessagesHelper.DelayedMessage delayedMessage;
                            long peer;
                            Long n4;
                            HashMap<Object, Object> extraHashMap;
                            StringBuilder sb;
                            MessageObject messageObject;
                            Long n5;
                            for (i = 0; i < delayedMessages.size(); ++i) {
                                delayedMessage = delayedMessages.get(i);
                                if (delayedMessage.encryptedChat == null) {
                                    peer = delayedMessage.peer;
                                    if (delayedMessage.type == 4) {
                                        n4 = (Long)this.typingTimes.get(peer);
                                        if (n4 == null || n4 + 4000L < System.currentTimeMillis()) {
                                            extraHashMap = delayedMessage.extraHashMap;
                                            sb = new StringBuilder();
                                            sb.append(s);
                                            sb.append("_i");
                                            messageObject = extraHashMap.get(sb.toString());
                                            if (messageObject != null && messageObject.isVideo()) {
                                                MessagesController.getInstance(this.currentAccount).sendTyping(peer, 5, 0);
                                            }
                                            else {
                                                MessagesController.getInstance(this.currentAccount).sendTyping(peer, 4, 0);
                                            }
                                            this.typingTimes.put(peer, (Object)System.currentTimeMillis());
                                        }
                                    }
                                    else {
                                        n5 = (Long)this.typingTimes.get(peer);
                                        delayedMessage.obj.getDocument();
                                        if (n5 == null || n5 + 4000L < System.currentTimeMillis()) {
                                            if (delayedMessage.obj.isRoundVideo()) {
                                                MessagesController.getInstance(this.currentAccount).sendTyping(peer, 8, 0);
                                            }
                                            else if (delayedMessage.obj.isVideo()) {
                                                MessagesController.getInstance(this.currentAccount).sendTyping(peer, 5, 0);
                                            }
                                            else if (delayedMessage.obj.isVoice()) {
                                                MessagesController.getInstance(this.currentAccount).sendTyping(peer, 9, 0);
                                            }
                                            else if (delayedMessage.obj.getDocument() != null) {
                                                MessagesController.getInstance(this.currentAccount).sendTyping(peer, 3, 0);
                                            }
                                            else if (delayedMessage.photoSize != null) {
                                                MessagesController.getInstance(this.currentAccount).sendTyping(peer, 4, 0);
                                            }
                                            this.typingTimes.put(peer, (Object)System.currentTimeMillis());
                                        }
                                    }
                                }
                            }
                        }
                        return;
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                        return;
                    }
                }
            }
            this.listenerInProgress = true;
            final String s2 = (String)array[0];
            final ArrayList<MessageObject> list3 = this.loadingFileMessagesObservers.get(s2);
            if (list3 != null) {
                for (n = list3.size(), i = 0; i < n; ++i) {
                    list3.get(i).mediaExists = true;
                }
                this.loadingFileMessagesObservers.remove(s2);
            }
            final ArrayList<WeakReference<FileDownloadProgressListener>> list4 = this.loadingFileObservers.get(s2);
            if (list4 != null) {
                WeakReference<FileDownloadProgressListener> weakReference3;
                for (n = list4.size(), i = 0; i < n; ++i) {
                    weakReference3 = list4.get(i);
                    if (weakReference3.get() != null) {
                        ((FileDownloadProgressListener)weakReference3.get()).onSuccessDownload(s2);
                        this.observersByTag.remove(((FileDownloadProgressListener)weakReference3.get()).getObserverTag());
                    }
                }
                this.loadingFileObservers.remove(s2);
            }
            this.listenerInProgress = false;
            this.processLaterArrays();
            this.checkDownloadFinished(s2, 0);
        }
        else {
            final String s3 = (String)array[0];
            final Integer n6 = (Integer)array[1];
            this.listenerInProgress = true;
            final ArrayList<WeakReference<FileDownloadProgressListener>> list5 = this.loadingFileObservers.get(s3);
            if (list5 != null) {
                WeakReference<FileDownloadProgressListener> weakReference4;
                for (n = list5.size(), i = 0; i < n; ++i) {
                    weakReference4 = list5.get(i);
                    if (weakReference4.get() != null) {
                        ((FileDownloadProgressListener)weakReference4.get()).onFailedDownload(s3, n6 == 1);
                        if (n6 != 1) {
                            this.observersByTag.remove(((FileDownloadProgressListener)weakReference4.get()).getObserverTag());
                        }
                    }
                }
                if (n6 != 1) {
                    this.loadingFileObservers.remove(s3);
                }
            }
            this.listenerInProgress = false;
            this.processLaterArrays();
            this.checkDownloadFinished(s3, n6);
        }
    }
    
    public int generateObserverTag() {
        return this.lastTag++;
    }
    
    public int getAutodownloadMask() {
        int[] array;
        if (ApplicationLoader.isConnectedToWiFi()) {
            if (!this.wifiPreset.enabled) {
                return 0;
            }
            array = this.getCurrentWiFiPreset().mask;
        }
        else if (ApplicationLoader.isRoaming()) {
            if (!this.roamingPreset.enabled) {
                return 0;
            }
            array = this.getCurrentRoamingPreset().mask;
        }
        else {
            if (!this.mobilePreset.enabled) {
                return 0;
            }
            array = this.getCurrentMobilePreset().mask;
        }
        int i = 0;
        int n = 0;
        while (i < array.length) {
            final int n2 = array[i];
            boolean b = true;
            if ((n2 & 0x1) == 0x0) {
                b = false;
            }
            int n3 = b ? 1 : 0;
            if ((array[i] & 0x2) != 0x0) {
                n3 = ((b ? 1 : 0) | 0x2);
            }
            int n4 = n3;
            if ((array[i] & 0x4) != 0x0) {
                n4 = (n3 | 0x4);
            }
            int n5 = n4;
            if ((array[i] & 0x8) != 0x0) {
                n5 = (n4 | 0x8);
            }
            n |= n5 << i * 8;
            ++i;
        }
        return n;
    }
    
    protected int getAutodownloadMaskAll() {
        final boolean enabled = this.mobilePreset.enabled;
        int i = 0;
        if (!enabled && !this.roamingPreset.enabled && !this.wifiPreset.enabled) {
            return 0;
        }
        int n = 0;
        while (i < 4) {
            int n2 = 0;
            Label_0093: {
                if ((this.getCurrentMobilePreset().mask[i] & 0x1) == 0x0 && (this.getCurrentWiFiPreset().mask[i] & 0x1) == 0x0) {
                    n2 = n;
                    if ((this.getCurrentRoamingPreset().mask[i] & 0x1) == 0x0) {
                        break Label_0093;
                    }
                }
                n2 = (n | 0x1);
            }
            int n3 = 0;
            Label_0143: {
                if ((this.getCurrentMobilePreset().mask[i] & 0x2) == 0x0 && (this.getCurrentWiFiPreset().mask[i] & 0x2) == 0x0) {
                    n3 = n2;
                    if ((this.getCurrentRoamingPreset().mask[i] & 0x2) == 0x0) {
                        break Label_0143;
                    }
                }
                n3 = (n2 | 0x2);
            }
            int n4 = 0;
            Label_0193: {
                if ((this.getCurrentMobilePreset().mask[i] & 0x4) == 0x0 && (this.getCurrentWiFiPreset().mask[i] & 0x4) == 0x0) {
                    n4 = n3;
                    if ((0x4 & this.getCurrentRoamingPreset().mask[i]) == 0x0) {
                        break Label_0193;
                    }
                }
                n4 = (n3 | 0x4);
            }
            Label_0247: {
                if ((this.getCurrentMobilePreset().mask[i] & 0x8) == 0x0 && (this.getCurrentWiFiPreset().mask[i] & 0x8) == 0x0) {
                    n = n4;
                    if ((this.getCurrentRoamingPreset().mask[i] & 0x8) == 0x0) {
                        break Label_0247;
                    }
                }
                n = (n4 | 0x8);
            }
            ++i;
        }
        return n;
    }
    
    public int getCurrentDownloadMask() {
        final boolean connectedToWiFi = ApplicationLoader.isConnectedToWiFi();
        final int n = 0;
        final int n2 = 0;
        int i = 0;
        if (connectedToWiFi) {
            if (!this.wifiPreset.enabled) {
                return 0;
            }
            int n3 = 0;
            while (i < 4) {
                n3 |= this.getCurrentWiFiPreset().mask[i];
                ++i;
            }
            return n3;
        }
        else if (ApplicationLoader.isRoaming()) {
            if (!this.roamingPreset.enabled) {
                return 0;
            }
            final int n4 = 0;
            int j = n;
            int n5 = n4;
            while (j < 4) {
                n5 |= this.getCurrentRoamingPreset().mask[j];
                ++j;
            }
            return n5;
        }
        else {
            if (!this.mobilePreset.enabled) {
                return 0;
            }
            int n6 = 0;
            for (int k = n2; k < 4; ++k) {
                n6 |= this.getCurrentMobilePreset().mask[k];
            }
            return n6;
        }
    }
    
    public Preset getCurrentMobilePreset() {
        final int currentMobilePreset = this.currentMobilePreset;
        if (currentMobilePreset == 0) {
            return this.lowPreset;
        }
        if (currentMobilePreset == 1) {
            return this.mediumPreset;
        }
        if (currentMobilePreset == 2) {
            return this.highPreset;
        }
        return this.mobilePreset;
    }
    
    public Preset getCurrentRoamingPreset() {
        final int currentRoamingPreset = this.currentRoamingPreset;
        if (currentRoamingPreset == 0) {
            return this.lowPreset;
        }
        if (currentRoamingPreset == 1) {
            return this.mediumPreset;
        }
        if (currentRoamingPreset == 2) {
            return this.highPreset;
        }
        return this.roamingPreset;
    }
    
    public Preset getCurrentWiFiPreset() {
        final int currentWifiPreset = this.currentWifiPreset;
        if (currentWifiPreset == 0) {
            return this.lowPreset;
        }
        if (currentWifiPreset == 1) {
            return this.mediumPreset;
        }
        if (currentWifiPreset == 2) {
            return this.highPreset;
        }
        return this.wifiPreset;
    }
    
    public void loadAutoDownloadConfig(final boolean b) {
        if (!this.loadingAutoDownloadConfig) {
            if (b || Math.abs(System.currentTimeMillis() - UserConfig.getInstance(this.currentAccount).autoDownloadConfigLoadTime) >= 86400000L) {
                this.loadingAutoDownloadConfig = true;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_account_getAutoDownloadSettings(), new _$$Lambda$DownloadController$Vy_RFVunDaT6j2u2tHT0TGLKrLk(this));
            }
        }
    }
    
    protected void newDownloadObjectsAvailable(final int n) {
        final int currentDownloadMask = this.getCurrentDownloadMask();
        if ((currentDownloadMask & 0x1) != 0x0 && (n & 0x1) != 0x0 && this.photoDownloadQueue.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(1);
        }
        if ((currentDownloadMask & 0x2) != 0x0 && (n & 0x2) != 0x0 && this.audioDownloadQueue.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(2);
        }
        if ((currentDownloadMask & 0x4) != 0x0 && (n & 0x4) != 0x0 && this.videoDownloadQueue.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(4);
        }
        if ((currentDownloadMask & 0x8) != 0x0 && (n & 0x8) != 0x0 && this.documentDownloadQueue.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(8);
        }
    }
    
    protected void processDownloadObjects(int n, final ArrayList<DownloadObject> list) {
        if (list.isEmpty()) {
            return;
        }
        ArrayList<DownloadObject> list2;
        if (n == 1) {
            list2 = this.photoDownloadQueue;
        }
        else if (n == 2) {
            list2 = this.audioDownloadQueue;
        }
        else if (n == 4) {
            list2 = this.videoDownloadQueue;
        }
        else if (n == 8) {
            list2 = this.documentDownloadQueue;
        }
        else {
            list2 = null;
        }
        for (int i = 0; i < list.size(); ++i) {
            final DownloadObject downloadObject = list.get(i);
            final TLObject object = downloadObject.object;
            Object o;
            TLRPC.PhotoSize closestPhotoSizeWithSize;
            if (object instanceof TLRPC.Document) {
                o = FileLoader.getAttachFileName(object);
                closestPhotoSizeWithSize = null;
            }
            else if (object instanceof TLRPC.Photo) {
                o = FileLoader.getAttachFileName(object);
                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)downloadObject.object).sizes, AndroidUtilities.getPhotoSize());
            }
            else {
                o = (closestPhotoSizeWithSize = null);
            }
            if (o != null) {
                if (!this.downloadQueueKeys.containsKey(o)) {
                    Label_0323: {
                        if (closestPhotoSizeWithSize != null) {
                            final TLRPC.Photo photo = (TLRPC.Photo)downloadObject.object;
                            if (downloadObject.secret) {
                                n = 2;
                            }
                            else if (downloadObject.forceCache) {
                                n = 1;
                            }
                            else {
                                n = 0;
                            }
                            FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), downloadObject.parent, null, 0, n);
                        }
                        else {
                            final TLObject object2 = downloadObject.object;
                            if (!(object2 instanceof TLRPC.Document)) {
                                n = 0;
                                break Label_0323;
                            }
                            final TLRPC.Document document = (TLRPC.Document)object2;
                            final FileLoader instance = FileLoader.getInstance(this.currentAccount);
                            final String parent = downloadObject.parent;
                            if (downloadObject.secret) {
                                n = 2;
                            }
                            else {
                                n = 0;
                            }
                            instance.loadFile(document, parent, 0, n);
                        }
                        n = 1;
                    }
                    if (n != 0) {
                        list2.add(downloadObject);
                        this.downloadQueueKeys.put((String)o, downloadObject);
                    }
                }
            }
        }
    }
    
    public void removeLoadingFileObserver(final FileDownloadProgressListener e) {
        if (this.listenerInProgress) {
            this.deleteLaterArray.add(e);
            return;
        }
        final String s = (String)this.observersByTag.get(e.getObserverTag());
        if (s != null) {
            final ArrayList<WeakReference<FileDownloadProgressListener>> list = this.loadingFileObservers.get(s);
            if (list != null) {
                int n;
                for (int i = 0; i < list.size(); i = n + 1) {
                    final WeakReference<FileDownloadProgressListener> weakReference = list.get(i);
                    if (weakReference.get() != null) {
                        n = i;
                        if (weakReference.get() != e) {
                            continue;
                        }
                    }
                    list.remove(i);
                    n = i - 1;
                }
                if (list.isEmpty()) {
                    this.loadingFileObservers.remove(s);
                }
            }
            this.observersByTag.remove(e.getObserverTag());
        }
    }
    
    public void savePresetToServer(int file_size_max) {
        final TLRPC.TL_account_saveAutoDownloadSettings tl_account_saveAutoDownloadSettings = new TLRPC.TL_account_saveAutoDownloadSettings();
        Preset preset;
        boolean b;
        if (file_size_max == 0) {
            preset = this.getCurrentMobilePreset();
            b = this.mobilePreset.enabled;
        }
        else if (file_size_max == 1) {
            preset = this.getCurrentWiFiPreset();
            b = this.wifiPreset.enabled;
        }
        else {
            preset = this.getCurrentRoamingPreset();
            b = this.roamingPreset.enabled;
        }
        tl_account_saveAutoDownloadSettings.settings = new TLRPC.TL_autoDownloadSettings();
        final TLRPC.TL_autoDownloadSettings settings = tl_account_saveAutoDownloadSettings.settings;
        settings.audio_preload_next = preset.preloadMusic;
        settings.video_preload_large = preset.preloadVideo;
        settings.phonecalls_less_data = preset.lessCallData;
        final int n = 0;
        settings.disabled = !b;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        file_size_max = 0;
        int n5;
        int n6;
        int n7;
        while (true) {
            final int[] mask = preset.mask;
            n5 = n3;
            n6 = n4;
            n7 = file_size_max;
            if (n2 >= mask.length) {
                break;
            }
            if ((mask[n2] & 0x1) != 0x0) {
                n3 = 1;
            }
            if ((preset.mask[n2] & 0x4) != 0x0) {
                n4 = 1;
            }
            if ((preset.mask[n2] & 0x8) != 0x0) {
                file_size_max = 1;
            }
            if (n3 != 0 && n4 != 0 && file_size_max != 0) {
                n5 = n3;
                n6 = n4;
                n7 = file_size_max;
                break;
            }
            ++n2;
        }
        final TLRPC.TL_autoDownloadSettings settings2 = tl_account_saveAutoDownloadSettings.settings;
        if (n5 != 0) {
            file_size_max = preset.sizes[0];
        }
        else {
            file_size_max = 0;
        }
        settings2.photo_size_max = file_size_max;
        final TLRPC.TL_autoDownloadSettings settings3 = tl_account_saveAutoDownloadSettings.settings;
        if (n6 != 0) {
            file_size_max = preset.sizes[1];
        }
        else {
            file_size_max = 0;
        }
        settings3.video_size_max = file_size_max;
        final TLRPC.TL_autoDownloadSettings settings4 = tl_account_saveAutoDownloadSettings.settings;
        file_size_max = n;
        if (n7 != 0) {
            file_size_max = preset.sizes[2];
        }
        settings4.file_size_max = file_size_max;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_saveAutoDownloadSettings, (RequestDelegate)_$$Lambda$DownloadController$0LtKveHOl8NLZKx_EDiX80oSJa0.INSTANCE);
    }
    
    public interface FileDownloadProgressListener
    {
        int getObserverTag();
        
        void onFailedDownload(final String p0, final boolean p1);
        
        void onProgressDownload(final String p0, final float p1);
        
        void onProgressUpload(final String p0, final float p1, final boolean p2);
        
        void onSuccessDownload(final String p0);
    }
    
    public static class Preset
    {
        public boolean enabled;
        public boolean lessCallData;
        public int[] mask;
        public boolean preloadMusic;
        public boolean preloadVideo;
        public int[] sizes;
        
        public Preset(final String s) {
            this.mask = new int[4];
            this.sizes = new int[4];
            final String[] split = s.split("_");
            if (split.length >= 11) {
                final int[] mask = this.mask;
                final boolean b = false;
                mask[0] = Utilities.parseInt(split[0]);
                this.mask[1] = Utilities.parseInt(split[1]);
                this.mask[2] = Utilities.parseInt(split[2]);
                this.mask[3] = Utilities.parseInt(split[3]);
                this.sizes[0] = Utilities.parseInt(split[4]);
                this.sizes[1] = Utilities.parseInt(split[5]);
                this.sizes[2] = Utilities.parseInt(split[6]);
                this.sizes[3] = Utilities.parseInt(split[7]);
                this.preloadVideo = (Utilities.parseInt(split[8]) == 1);
                this.preloadMusic = (Utilities.parseInt(split[9]) == 1);
                this.enabled = (Utilities.parseInt(split[10]) == 1);
                if (split.length >= 12) {
                    boolean lessCallData = b;
                    if (Utilities.parseInt(split[11]) == 1) {
                        lessCallData = true;
                    }
                    this.lessCallData = lessCallData;
                }
            }
        }
        
        public Preset(int[] sizes, final int n, final int n2, final int n3, final boolean preloadVideo, final boolean preloadMusic, final boolean enabled, final boolean lessCallData) {
            this.mask = new int[4];
            this.sizes = new int[4];
            final int[] mask = this.mask;
            System.arraycopy(sizes, 0, mask, 0, mask.length);
            sizes = this.sizes;
            sizes[0] = n;
            sizes[1] = n2;
            sizes[2] = n3;
            sizes[3] = 524288;
            this.preloadVideo = preloadVideo;
            this.preloadMusic = preloadMusic;
            this.lessCallData = lessCallData;
            this.enabled = enabled;
        }
        
        public boolean equals(final Preset preset) {
            final int[] mask = this.mask;
            final boolean b = false;
            final int n = mask[0];
            final int[] mask2 = preset.mask;
            boolean b2 = b;
            if (n == mask2[0]) {
                b2 = b;
                if (mask[1] == mask2[1]) {
                    b2 = b;
                    if (mask[2] == mask2[2]) {
                        b2 = b;
                        if (mask[3] == mask2[3]) {
                            final int[] sizes = this.sizes;
                            final int n2 = sizes[0];
                            final int[] sizes2 = preset.sizes;
                            b2 = b;
                            if (n2 == sizes2[0]) {
                                b2 = b;
                                if (sizes[1] == sizes2[1]) {
                                    b2 = b;
                                    if (sizes[2] == sizes2[2]) {
                                        b2 = b;
                                        if (sizes[3] == sizes2[3]) {
                                            b2 = b;
                                            if (this.preloadVideo == preset.preloadVideo) {
                                                b2 = b;
                                                if (this.preloadMusic == preset.preloadMusic) {
                                                    b2 = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return b2;
        }
        
        public boolean isEnabled() {
            int n = 0;
            while (true) {
                final int[] mask = this.mask;
                if (n >= mask.length) {
                    return false;
                }
                if (mask[n] != 0) {
                    return true;
                }
                ++n;
            }
        }
        
        public void set(final Preset preset) {
            final int[] mask = preset.mask;
            final int[] mask2 = this.mask;
            System.arraycopy(mask, 0, mask2, 0, mask2.length);
            final int[] sizes = preset.sizes;
            final int[] sizes2 = this.sizes;
            System.arraycopy(sizes, 0, sizes2, 0, sizes2.length);
            this.preloadVideo = preset.preloadVideo;
            this.preloadMusic = preset.preloadMusic;
            this.lessCallData = preset.lessCallData;
        }
        
        public void set(final TLRPC.TL_autoDownloadSettings tl_autoDownloadSettings) {
            this.preloadMusic = tl_autoDownloadSettings.audio_preload_next;
            this.preloadVideo = tl_autoDownloadSettings.video_preload_large;
            this.lessCallData = tl_autoDownloadSettings.phonecalls_less_data;
            final int[] sizes = this.sizes;
            final int max = Math.max(512000, tl_autoDownloadSettings.photo_size_max);
            int n = 0;
            sizes[0] = max;
            this.sizes[1] = Math.max(512000, tl_autoDownloadSettings.video_size_max);
            this.sizes[2] = Math.max(512000, tl_autoDownloadSettings.file_size_max);
            while (true) {
                final int[] mask = this.mask;
                if (n >= mask.length) {
                    break;
                }
                if (tl_autoDownloadSettings.photo_size_max != 0 && !tl_autoDownloadSettings.disabled) {
                    mask[n] |= 0x1;
                }
                else {
                    final int[] mask2 = this.mask;
                    mask2[n] &= 0xFFFFFFFE;
                }
                if (tl_autoDownloadSettings.video_size_max != 0 && !tl_autoDownloadSettings.disabled) {
                    final int[] mask3 = this.mask;
                    mask3[n] |= 0x4;
                }
                else {
                    final int[] mask4 = this.mask;
                    mask4[n] &= 0xFFFFFFFB;
                }
                if (tl_autoDownloadSettings.file_size_max != 0 && !tl_autoDownloadSettings.disabled) {
                    final int[] mask5 = this.mask;
                    mask5[n] |= 0x8;
                }
                else {
                    final int[] mask6 = this.mask;
                    mask6[n] &= 0xFFFFFFF7;
                }
                ++n;
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mask[0]);
            sb.append("_");
            sb.append(this.mask[1]);
            sb.append("_");
            sb.append(this.mask[2]);
            sb.append("_");
            sb.append(this.mask[3]);
            sb.append("_");
            sb.append(this.sizes[0]);
            sb.append("_");
            sb.append(this.sizes[1]);
            sb.append("_");
            sb.append(this.sizes[2]);
            sb.append("_");
            sb.append(this.sizes[3]);
            sb.append("_");
            sb.append(this.preloadVideo ? 1 : 0);
            sb.append("_");
            sb.append(this.preloadMusic ? 1 : 0);
            sb.append("_");
            sb.append(this.enabled ? 1 : 0);
            sb.append("_");
            sb.append(this.lessCallData ? 1 : 0);
            return sb.toString();
        }
    }
}
