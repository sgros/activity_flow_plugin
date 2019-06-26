// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import android.text.TextUtils;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import android.util.SparseIntArray;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.File;
import android.util.SparseArray;

public class FileLoader
{
    private static volatile FileLoader[] Instance;
    public static final int MEDIA_DIR_AUDIO = 1;
    public static final int MEDIA_DIR_CACHE = 4;
    public static final int MEDIA_DIR_DOCUMENT = 3;
    public static final int MEDIA_DIR_IMAGE = 0;
    public static final int MEDIA_DIR_VIDEO = 2;
    private static volatile DispatchQueue fileLoaderQueue;
    private static SparseArray<File> mediaDirs;
    private ArrayList<FileLoadOperation> activeFileLoadOperation;
    private SparseArray<LinkedList<FileLoadOperation>> audioLoadOperationQueues;
    private int currentAccount;
    private SparseIntArray currentAudioLoadOperationsCount;
    private SparseIntArray currentLoadOperationsCount;
    private SparseIntArray currentPhotoLoadOperationsCount;
    private int currentUploadOperationsCount;
    private int currentUploadSmallOperationsCount;
    private FileLoaderDelegate delegate;
    private int lastReferenceId;
    private ConcurrentHashMap<String, FileLoadOperation> loadOperationPaths;
    private ConcurrentHashMap<String, Boolean> loadOperationPathsUI;
    private SparseArray<LinkedList<FileLoadOperation>> loadOperationQueues;
    private HashMap<String, Boolean> loadingVideos;
    private ConcurrentHashMap<Integer, Object> parentObjectReferences;
    private SparseArray<LinkedList<FileLoadOperation>> photoLoadOperationQueues;
    private ConcurrentHashMap<String, FileUploadOperation> uploadOperationPaths;
    private ConcurrentHashMap<String, FileUploadOperation> uploadOperationPathsEnc;
    private LinkedList<FileUploadOperation> uploadOperationQueue;
    private HashMap<String, Long> uploadSizes;
    private LinkedList<FileUploadOperation> uploadSmallOperationQueue;
    
    static {
        FileLoader.fileLoaderQueue = new DispatchQueue("fileUploadQueue");
        FileLoader.mediaDirs = null;
        FileLoader.Instance = new FileLoader[3];
    }
    
    public FileLoader(final int currentAccount) {
        this.uploadOperationQueue = new LinkedList<FileUploadOperation>();
        this.uploadSmallOperationQueue = new LinkedList<FileUploadOperation>();
        this.uploadOperationPaths = new ConcurrentHashMap<String, FileUploadOperation>();
        this.uploadOperationPathsEnc = new ConcurrentHashMap<String, FileUploadOperation>();
        this.currentUploadOperationsCount = 0;
        this.currentUploadSmallOperationsCount = 0;
        this.loadOperationQueues = (SparseArray<LinkedList<FileLoadOperation>>)new SparseArray();
        this.audioLoadOperationQueues = (SparseArray<LinkedList<FileLoadOperation>>)new SparseArray();
        this.photoLoadOperationQueues = (SparseArray<LinkedList<FileLoadOperation>>)new SparseArray();
        this.currentLoadOperationsCount = new SparseIntArray();
        this.currentAudioLoadOperationsCount = new SparseIntArray();
        this.currentPhotoLoadOperationsCount = new SparseIntArray();
        this.loadOperationPaths = new ConcurrentHashMap<String, FileLoadOperation>();
        this.activeFileLoadOperation = new ArrayList<FileLoadOperation>();
        this.loadOperationPathsUI = new ConcurrentHashMap<String, Boolean>(10, 1.0f, 2);
        this.uploadSizes = new HashMap<String, Long>();
        this.loadingVideos = new HashMap<String, Boolean>();
        this.delegate = null;
        this.parentObjectReferences = new ConcurrentHashMap<Integer, Object>();
        this.currentAccount = currentAccount;
    }
    
    private void addOperationToQueue(final FileLoadOperation fileLoadOperation, final LinkedList<FileLoadOperation> list) {
        final int priority = fileLoadOperation.getPriority();
        if (priority > 0) {
            final int size = list.size();
            int index = 0;
            final int size2 = list.size();
            int index2;
            while (true) {
                index2 = size;
                if (index >= size2) {
                    break;
                }
                if (list.get(index).getPriority() < priority) {
                    index2 = index;
                    break;
                }
                ++index;
            }
            list.add(index2, fileLoadOperation);
        }
        else {
            list.add(fileLoadOperation);
        }
    }
    
    private void cancelLoadFile(final TLRPC.Document document, final SecureDocument secureDocument, final WebFile webFile, final TLRPC.FileLocation fileLocation, String key) {
        if (fileLocation == null && document == null && webFile == null && secureDocument == null) {
            return;
        }
        if (fileLocation != null) {
            key = getAttachFileName(fileLocation, key);
        }
        else if (document != null) {
            key = getAttachFileName(document);
        }
        else if (secureDocument != null) {
            key = getAttachFileName(secureDocument);
        }
        else if (webFile != null) {
            key = getAttachFileName(webFile);
        }
        else {
            key = null;
        }
        if (key == null) {
            return;
        }
        this.loadOperationPathsUI.remove(key);
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$5pIYqCqDZLULELsU2WZGVaPNvhc(this, key, document, webFile, secureDocument, fileLocation));
    }
    
    public static File checkDirectory(final int n) {
        return (File)FileLoader.mediaDirs.get(n);
    }
    
    private void checkDownloadQueue(final int n, final TLRPC.Document document, final WebFile webFile, final TLRPC.FileLocation fileLocation, final String s) {
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$zxmFDKJZWcEsmPLtOCvxbxeqvMw(this, n, s, document, webFile, fileLocation));
    }
    
    public static boolean copyFile(final InputStream inputStream, final File file) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        final byte[] array = new byte[4096];
        while (true) {
            final int read = inputStream.read(array);
            if (read <= 0) {
                break;
            }
            Thread.yield();
            fileOutputStream.write(array, 0, read);
        }
        fileOutputStream.close();
        return true;
    }
    
    public static String fixFileName(final String s) {
        String trim = s;
        if (s != null) {
            trim = s.replaceAll("[\u0001-\u001f<>:\"/\\\\|?*\u007f]+", "").trim();
        }
        return trim;
    }
    
    public static String getAttachFileName(final TLObject tlObject) {
        return getAttachFileName(tlObject, null);
    }
    
    public static String getAttachFileName(final TLObject tlObject, String str) {
        final boolean b = tlObject instanceof TLRPC.Document;
        final String s = "";
        if (b) {
            final TLRPC.Document document = (TLRPC.Document)tlObject;
            str = getDocumentFileName(document);
            String substring = s;
            if (str != null) {
                final int lastIndex = str.lastIndexOf(46);
                if (lastIndex == -1) {
                    substring = s;
                }
                else {
                    substring = str.substring(lastIndex);
                }
            }
            str = substring;
            if (substring.length() <= 1) {
                str = getExtensionByMimeType(document.mime_type);
            }
            if (str.length() > 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(document.dc_id);
                sb.append("_");
                sb.append(document.id);
                sb.append(str);
                return sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(document.dc_id);
            sb2.append("_");
            sb2.append(document.id);
            return sb2.toString();
        }
        else {
            if (tlObject instanceof SecureDocument) {
                final SecureDocument secureDocument = (SecureDocument)tlObject;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(secureDocument.secureFile.dc_id);
                sb3.append("_");
                sb3.append(secureDocument.secureFile.id);
                sb3.append(".jpg");
                return sb3.toString();
            }
            if (tlObject instanceof TLRPC.TL_secureFile) {
                final TLRPC.TL_secureFile tl_secureFile = (TLRPC.TL_secureFile)tlObject;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(tl_secureFile.dc_id);
                sb4.append("_");
                sb4.append(tl_secureFile.id);
                sb4.append(".jpg");
                return sb4.toString();
            }
            if (tlObject instanceof WebFile) {
                final WebFile webFile = (WebFile)tlObject;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(Utilities.MD5(webFile.url));
                sb5.append(".");
                sb5.append(ImageLoader.getHttpUrlExtension(webFile.url, getMimeTypePart(webFile.mime_type)));
                return sb5.toString();
            }
            if (tlObject instanceof TLRPC.PhotoSize) {
                final TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize)tlObject;
                final TLRPC.FileLocation location = photoSize.location;
                if (location != null && !(location instanceof TLRPC.TL_fileLocationUnavailable)) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(photoSize.location.volume_id);
                    sb6.append("_");
                    sb6.append(photoSize.location.local_id);
                    sb6.append(".");
                    if (str == null) {
                        str = "jpg";
                    }
                    sb6.append(str);
                    return sb6.toString();
                }
                return "";
            }
            else {
                if (!(tlObject instanceof TLRPC.FileLocation)) {
                    return "";
                }
                if (tlObject instanceof TLRPC.TL_fileLocationUnavailable) {
                    return "";
                }
                final TLRPC.FileLocation fileLocation = (TLRPC.FileLocation)tlObject;
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(fileLocation.volume_id);
                sb7.append("_");
                sb7.append(fileLocation.local_id);
                sb7.append(".");
                if (str == null) {
                    str = "jpg";
                }
                sb7.append(str);
                return sb7.toString();
            }
        }
    }
    
    private LinkedList<FileLoadOperation> getAudioLoadOperationQueue(final int n) {
        LinkedList<FileLoadOperation> list;
        if ((list = (LinkedList<FileLoadOperation>)this.audioLoadOperationQueues.get(n)) == null) {
            list = new LinkedList<FileLoadOperation>();
            this.audioLoadOperationQueues.put(n, (Object)list);
        }
        return list;
    }
    
    public static TLRPC.PhotoSize getClosestPhotoSizeWithSize(final ArrayList<TLRPC.PhotoSize> list, final int n) {
        return getClosestPhotoSizeWithSize(list, n, false);
    }
    
    public static TLRPC.PhotoSize getClosestPhotoSizeWithSize(final ArrayList<TLRPC.PhotoSize> list, final int n, final boolean b) {
        final TLRPC.PhotoSize photoSize = null;
        TLRPC.PhotoSize photoSize2 = null;
        TLRPC.PhotoSize photoSize3 = photoSize;
        if (list != null) {
            if (list.isEmpty()) {
                photoSize3 = photoSize;
            }
            else {
                int index = 0;
                int n2 = 0;
                while (true) {
                    photoSize3 = photoSize2;
                    if (index >= list.size()) {
                        break;
                    }
                    final TLRPC.PhotoSize photoSize4 = list.get(index);
                    TLRPC.PhotoSize photoSize5 = photoSize2;
                    int n3 = n2;
                    Label_0319: {
                        if (photoSize4 != null) {
                            if (photoSize4 instanceof TLRPC.TL_photoSizeEmpty) {
                                photoSize5 = photoSize2;
                                n3 = n2;
                            }
                            else {
                                Label_0316: {
                                    if (b) {
                                        final int h = photoSize4.h;
                                        final int w = photoSize4.w;
                                        int n4;
                                        if ((n4 = h) >= w) {
                                            n4 = w;
                                        }
                                        n3 = n4;
                                        if (photoSize2 != null) {
                                            if (n > 100) {
                                                final TLRPC.FileLocation location = photoSize2.location;
                                                if (location != null) {
                                                    n3 = n4;
                                                    if (location.dc_id == Integer.MIN_VALUE) {
                                                        break Label_0316;
                                                    }
                                                }
                                            }
                                            n3 = n4;
                                            if (!(photoSize4 instanceof TLRPC.TL_photoCachedSize)) {
                                                photoSize5 = photoSize2;
                                                if (n <= (n3 = n2)) {
                                                    break Label_0319;
                                                }
                                                photoSize5 = photoSize2;
                                                if ((n3 = n2) >= n4) {
                                                    break Label_0319;
                                                }
                                                n3 = n4;
                                            }
                                        }
                                    }
                                    else {
                                        int w2 = photoSize4.w;
                                        final int h2 = photoSize4.h;
                                        if (w2 < h2) {
                                            w2 = h2;
                                        }
                                        n3 = w2;
                                        if (photoSize2 != null) {
                                            if (n > 100) {
                                                final TLRPC.FileLocation location2 = photoSize2.location;
                                                if (location2 != null) {
                                                    n3 = w2;
                                                    if (location2.dc_id == Integer.MIN_VALUE) {
                                                        break Label_0316;
                                                    }
                                                }
                                            }
                                            n3 = w2;
                                            if (!(photoSize4 instanceof TLRPC.TL_photoCachedSize)) {
                                                photoSize5 = photoSize2;
                                                n3 = n2;
                                                if (w2 > n) {
                                                    break Label_0319;
                                                }
                                                photoSize5 = photoSize2;
                                                if ((n3 = n2) >= w2) {
                                                    break Label_0319;
                                                }
                                                n3 = w2;
                                            }
                                        }
                                    }
                                }
                                photoSize5 = photoSize4;
                            }
                        }
                    }
                    ++index;
                    photoSize2 = photoSize5;
                    n2 = n3;
                }
            }
        }
        return photoSize3;
    }
    
    public static File getDirectory(final int n) {
        File file2;
        final File file = file2 = (File)FileLoader.mediaDirs.get(n);
        if (file == null) {
            file2 = file;
            if (n != 4) {
                file2 = (File)FileLoader.mediaDirs.get(4);
            }
        }
        try {
            if (!file2.isDirectory()) {
                file2.mkdirs();
            }
            return file2;
        }
        catch (Exception ex) {
            return file2;
        }
    }
    
    public static String getDocumentExtension(final TLRPC.Document document) {
        final String documentFileName = getDocumentFileName(document);
        final int lastIndex = documentFileName.lastIndexOf(46);
        String substring;
        if (lastIndex != -1) {
            substring = documentFileName.substring(lastIndex + 1);
        }
        else {
            substring = null;
        }
        String mime_type = null;
        Label_0048: {
            if (substring != null) {
                mime_type = substring;
                if (substring.length() != 0) {
                    break Label_0048;
                }
            }
            mime_type = document.mime_type;
        }
        String s;
        if ((s = mime_type) == null) {
            s = "";
        }
        return s.toUpperCase();
    }
    
    public static String getDocumentFileName(final TLRPC.Document document) {
        String file_name = null;
        String file_name2 = null;
        if (document != null) {
            file_name = document.file_name;
            if (file_name == null) {
                int index = 0;
                while (true) {
                    file_name = file_name2;
                    if (index >= document.attributes.size()) {
                        break;
                    }
                    final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(index);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeFilename) {
                        file_name2 = documentAttribute.file_name;
                    }
                    ++index;
                }
            }
        }
        String fixFileName = fixFileName(file_name);
        if (fixFileName == null) {
            fixFileName = "";
        }
        return fixFileName;
    }
    
    public static String getExtensionByMimeType(final String s) {
        if (s != null) {
            int n = -1;
            final int hashCode = s.hashCode();
            if (hashCode != 187091926) {
                if (hashCode != 1331848029) {
                    if (hashCode == 2039520277) {
                        if (s.equals("video/x-matroska")) {
                            n = 1;
                        }
                    }
                }
                else if (s.equals("video/mp4")) {
                    n = 0;
                }
            }
            else if (s.equals("audio/ogg")) {
                n = 2;
            }
            if (n == 0) {
                return ".mp4";
            }
            if (n == 1) {
                return ".mkv";
            }
            if (n == 2) {
                return ".ogg";
            }
        }
        return "";
    }
    
    public static String getFileExtension(final File file) {
        final String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(46) + 1);
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public static FileLoader getInstance(final int n) {
        final FileLoader fileLoader;
        if ((fileLoader = FileLoader.Instance[n]) == null) {
            synchronized (FileLoader.class) {
                if (FileLoader.Instance[n] == null) {
                    FileLoader.Instance[n] = new FileLoader(n);
                }
            }
        }
        return fileLoader;
    }
    
    public static File getInternalCacheDir() {
        return ApplicationLoader.applicationContext.getCacheDir();
    }
    
    private LinkedList<FileLoadOperation> getLoadOperationQueue(final int n) {
        LinkedList<FileLoadOperation> list;
        if ((list = (LinkedList<FileLoadOperation>)this.loadOperationQueues.get(n)) == null) {
            list = new LinkedList<FileLoadOperation>();
            this.loadOperationQueues.put(n, (Object)list);
        }
        return list;
    }
    
    public static String getMessageFileName(final TLRPC.Message message) {
        if (message == null) {
            return "";
        }
        if (message instanceof TLRPC.TL_messageService) {
            final TLRPC.Photo photo = message.action.photo;
            if (photo != null) {
                final ArrayList<TLRPC.PhotoSize> sizes = photo.sizes;
                if (sizes.size() > 0) {
                    final TLRPC.PhotoSize closestPhotoSizeWithSize = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (closestPhotoSizeWithSize != null) {
                        return getAttachFileName(closestPhotoSizeWithSize);
                    }
                }
            }
        }
        else {
            final TLRPC.MessageMedia media = message.media;
            if (media instanceof TLRPC.TL_messageMediaDocument) {
                return getAttachFileName(media.document);
            }
            if (media instanceof TLRPC.TL_messageMediaPhoto) {
                final ArrayList<TLRPC.PhotoSize> sizes2 = media.photo.sizes;
                if (sizes2.size() > 0) {
                    final TLRPC.PhotoSize closestPhotoSizeWithSize2 = getClosestPhotoSizeWithSize(sizes2, AndroidUtilities.getPhotoSize());
                    if (closestPhotoSizeWithSize2 != null) {
                        return getAttachFileName(closestPhotoSizeWithSize2);
                    }
                }
            }
            else if (media instanceof TLRPC.TL_messageMediaWebPage) {
                final TLRPC.WebPage webpage = media.webpage;
                final TLRPC.Document document = webpage.document;
                if (document != null) {
                    return getAttachFileName(document);
                }
                final TLRPC.Photo photo2 = webpage.photo;
                if (photo2 != null) {
                    final ArrayList<TLRPC.PhotoSize> sizes3 = photo2.sizes;
                    if (sizes3.size() > 0) {
                        final TLRPC.PhotoSize closestPhotoSizeWithSize3 = getClosestPhotoSizeWithSize(sizes3, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize3 != null) {
                            return getAttachFileName(closestPhotoSizeWithSize3);
                        }
                    }
                }
                else if (media instanceof TLRPC.TL_messageMediaInvoice) {
                    return getAttachFileName(((TLRPC.TL_messageMediaInvoice)media).photo);
                }
            }
            else if (media instanceof TLRPC.TL_messageMediaInvoice) {
                final TLRPC.WebDocument photo3 = ((TLRPC.TL_messageMediaInvoice)media).photo;
                if (photo3 != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(Utilities.MD5(photo3.url));
                    sb.append(".");
                    sb.append(ImageLoader.getHttpUrlExtension(photo3.url, getMimeTypePart(photo3.mime_type)));
                    return sb.toString();
                }
            }
        }
        return "";
    }
    
    public static String getMimeTypePart(final String s) {
        final int lastIndex = s.lastIndexOf(47);
        if (lastIndex != -1) {
            return s.substring(lastIndex + 1);
        }
        return "";
    }
    
    public static File getPathToAttach(final TLObject tlObject) {
        return getPathToAttach(tlObject, null, false);
    }
    
    public static File getPathToAttach(final TLObject tlObject, final String s, final boolean b) {
        File parent = null;
        if (b) {
            parent = getDirectory(4);
        }
        else if (tlObject instanceof TLRPC.Document) {
            final TLRPC.Document document = (TLRPC.Document)tlObject;
            if (document.key != null) {
                parent = getDirectory(4);
            }
            else if (MessageObject.isVoiceDocument(document)) {
                parent = getDirectory(1);
            }
            else if (MessageObject.isVideoDocument(document)) {
                parent = getDirectory(2);
            }
            else {
                parent = getDirectory(3);
            }
        }
        else {
            if (tlObject instanceof TLRPC.Photo) {
                return getPathToAttach(getClosestPhotoSizeWithSize(((TLRPC.Photo)tlObject).sizes, AndroidUtilities.getPhotoSize()), s, b);
            }
            if (tlObject instanceof TLRPC.PhotoSize) {
                final TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize)tlObject;
                if (!(photoSize instanceof TLRPC.TL_photoStrippedSize)) {
                    final TLRPC.FileLocation location = photoSize.location;
                    if (location != null && location.key == null && (location.volume_id != -2147483648L || location.local_id >= 0) && photoSize.size >= 0) {
                        parent = getDirectory(0);
                    }
                    else {
                        parent = getDirectory(4);
                    }
                }
            }
            else if (tlObject instanceof TLRPC.FileLocation) {
                final TLRPC.FileLocation fileLocation = (TLRPC.FileLocation)tlObject;
                if (fileLocation.key == null && (fileLocation.volume_id != -2147483648L || fileLocation.local_id >= 0)) {
                    parent = getDirectory(0);
                }
                else {
                    parent = getDirectory(4);
                }
            }
            else if (tlObject instanceof WebFile) {
                final WebFile webFile = (WebFile)tlObject;
                if (webFile.mime_type.startsWith("image/")) {
                    parent = getDirectory(0);
                }
                else if (webFile.mime_type.startsWith("audio/")) {
                    parent = getDirectory(1);
                }
                else if (webFile.mime_type.startsWith("video/")) {
                    parent = getDirectory(2);
                }
                else {
                    parent = getDirectory(3);
                }
            }
            else if (tlObject instanceof TLRPC.TL_secureFile || tlObject instanceof SecureDocument) {
                parent = getDirectory(4);
            }
        }
        if (parent == null) {
            return new File("");
        }
        return new File(parent, getAttachFileName(tlObject, s));
    }
    
    public static File getPathToAttach(final TLObject tlObject, final boolean b) {
        return getPathToAttach(tlObject, null, b);
    }
    
    public static File getPathToMessage(final TLRPC.Message message) {
        if (message == null) {
            return new File("");
        }
        if (message instanceof TLRPC.TL_messageService) {
            final TLRPC.Photo photo = message.action.photo;
            if (photo != null) {
                final ArrayList<TLRPC.PhotoSize> sizes = photo.sizes;
                if (sizes.size() > 0) {
                    final TLRPC.PhotoSize closestPhotoSizeWithSize = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (closestPhotoSizeWithSize != null) {
                        return getPathToAttach(closestPhotoSizeWithSize);
                    }
                }
            }
        }
        else {
            final TLRPC.MessageMedia media = message.media;
            final boolean b = media instanceof TLRPC.TL_messageMediaDocument;
            final boolean b2 = false;
            boolean b3 = false;
            if (b) {
                final TLRPC.Document document = media.document;
                if (media.ttl_seconds != 0) {
                    b3 = true;
                }
                return getPathToAttach(document, b3);
            }
            if (media instanceof TLRPC.TL_messageMediaPhoto) {
                final ArrayList<TLRPC.PhotoSize> sizes2 = media.photo.sizes;
                if (sizes2.size() > 0) {
                    final TLRPC.PhotoSize closestPhotoSizeWithSize2 = getClosestPhotoSizeWithSize(sizes2, AndroidUtilities.getPhotoSize());
                    if (closestPhotoSizeWithSize2 != null) {
                        boolean b4 = b2;
                        if (message.media.ttl_seconds != 0) {
                            b4 = true;
                        }
                        return getPathToAttach(closestPhotoSizeWithSize2, b4);
                    }
                }
            }
            else if (media instanceof TLRPC.TL_messageMediaWebPage) {
                final TLRPC.WebPage webpage = media.webpage;
                final TLRPC.Document document2 = webpage.document;
                if (document2 != null) {
                    return getPathToAttach(document2);
                }
                final TLRPC.Photo photo2 = webpage.photo;
                if (photo2 != null) {
                    final ArrayList<TLRPC.PhotoSize> sizes3 = photo2.sizes;
                    if (sizes3.size() > 0) {
                        final TLRPC.PhotoSize closestPhotoSizeWithSize3 = getClosestPhotoSizeWithSize(sizes3, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize3 != null) {
                            return getPathToAttach(closestPhotoSizeWithSize3);
                        }
                    }
                }
            }
            else if (media instanceof TLRPC.TL_messageMediaInvoice) {
                return getPathToAttach(((TLRPC.TL_messageMediaInvoice)media).photo, true);
            }
        }
        return new File("");
    }
    
    private LinkedList<FileLoadOperation> getPhotoLoadOperationQueue(final int n) {
        LinkedList<FileLoadOperation> list;
        if ((list = (LinkedList<FileLoadOperation>)this.photoLoadOperationQueues.get(n)) == null) {
            list = new LinkedList<FileLoadOperation>();
            this.photoLoadOperationQueues.put(n, (Object)list);
        }
        return list;
    }
    
    public static boolean isVideoMimeType(final String s) {
        return "video/mp4".equals(s) || (SharedConfig.streamMkv && "video/x-matroska".equals(s));
    }
    
    private void loadFile(final TLRPC.Document document, final SecureDocument secureDocument, final WebFile webFile, final TLRPC.TL_fileLocationToBeDeprecated tl_fileLocationToBeDeprecated, final ImageLocation imageLocation, final Object o, final String s, final int n, final int n2, final int n3) {
        String key;
        if (tl_fileLocationToBeDeprecated != null) {
            key = getAttachFileName(tl_fileLocationToBeDeprecated, s);
        }
        else if (document != null) {
            key = getAttachFileName(document);
        }
        else if (webFile != null) {
            key = getAttachFileName(webFile);
        }
        else {
            key = null;
        }
        if (n3 != 10 && !TextUtils.isEmpty((CharSequence)key) && !key.contains("-2147483648")) {
            this.loadOperationPathsUI.put(key, true);
        }
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$QveLJ1Gqcvj_9l_dGSaDY1G3t6s(this, document, secureDocument, webFile, tl_fileLocationToBeDeprecated, imageLocation, o, s, n, n2, n3));
    }
    
    private FileLoadOperation loadFileInternal(final TLRPC.Document document, final SecureDocument secureDocument, final WebFile webFile, final TLRPC.TL_fileLocationToBeDeprecated tl_fileLocationToBeDeprecated, final ImageLocation imageLocation, final Object o, final String s, int index, int priority, final FileLoadOperationStream fileLoadOperationStream, final int n, int datacenterId) {
        String key;
        if (tl_fileLocationToBeDeprecated != null) {
            key = getAttachFileName(tl_fileLocationToBeDeprecated, s);
        }
        else if (secureDocument != null) {
            key = getAttachFileName(secureDocument);
        }
        else if (document != null) {
            key = getAttachFileName(document);
        }
        else if (webFile != null) {
            key = getAttachFileName(webFile);
        }
        else {
            key = null;
        }
        if (key == null || key.contains("-2147483648")) {
            return null;
        }
        if (datacenterId != 10 && !TextUtils.isEmpty((CharSequence)key) && !key.contains("-2147483648")) {
            this.loadOperationPathsUI.put(key, true);
        }
        final FileLoadOperation fileLoadOperation = this.loadOperationPaths.get(key);
        if (fileLoadOperation != null) {
            if (datacenterId != 10 && fileLoadOperation.isPreloadVideoOperation()) {
                fileLoadOperation.setIsPreloadVideoOperation(false);
            }
            if (fileLoadOperationStream != null || priority > 0) {
                priority = fileLoadOperation.getDatacenterId();
                final LinkedList<FileLoadOperation> audioLoadOperationQueue = this.getAudioLoadOperationQueue(priority);
                final LinkedList<FileLoadOperation> photoLoadOperationQueue = this.getPhotoLoadOperationQueue(priority);
                final LinkedList<FileLoadOperation> loadOperationQueue = this.getLoadOperationQueue(priority);
                fileLoadOperation.setForceRequest(true);
                LinkedList<FileLoadOperation> list;
                if (!MessageObject.isVoiceDocument(document) && !MessageObject.isVoiceWebDocument(webFile)) {
                    if (secureDocument == null && tl_fileLocationToBeDeprecated == null && !MessageObject.isImageWebDocument(webFile)) {
                        list = loadOperationQueue;
                    }
                    else {
                        list = photoLoadOperationQueue;
                    }
                }
                else {
                    list = audioLoadOperationQueue;
                }
                if (list != null) {
                    index = list.indexOf(fileLoadOperation);
                    if (index >= 0) {
                        list.remove(index);
                        if (fileLoadOperationStream != null) {
                            if (list == audioLoadOperationQueue) {
                                if (fileLoadOperation.start(fileLoadOperationStream, n)) {
                                    final SparseIntArray currentAudioLoadOperationsCount = this.currentAudioLoadOperationsCount;
                                    currentAudioLoadOperationsCount.put(priority, currentAudioLoadOperationsCount.get(priority) + 1);
                                }
                            }
                            else if (list == photoLoadOperationQueue) {
                                if (fileLoadOperation.start(fileLoadOperationStream, n)) {
                                    final SparseIntArray currentPhotoLoadOperationsCount = this.currentPhotoLoadOperationsCount;
                                    currentPhotoLoadOperationsCount.put(priority, currentPhotoLoadOperationsCount.get(priority) + 1);
                                }
                            }
                            else {
                                if (fileLoadOperation.start(fileLoadOperationStream, n)) {
                                    final SparseIntArray currentLoadOperationsCount = this.currentLoadOperationsCount;
                                    currentLoadOperationsCount.put(priority, currentLoadOperationsCount.get(priority) + 1);
                                }
                                if (fileLoadOperation.wasStarted() && !this.activeFileLoadOperation.contains(fileLoadOperation)) {
                                    if (fileLoadOperationStream != null) {
                                        this.pauseCurrentFileLoadOperations(fileLoadOperation);
                                    }
                                    this.activeFileLoadOperation.add(fileLoadOperation);
                                }
                            }
                        }
                        else {
                            list.add(0, fileLoadOperation);
                        }
                    }
                    else {
                        if (fileLoadOperationStream != null) {
                            this.pauseCurrentFileLoadOperations(fileLoadOperation);
                        }
                        fileLoadOperation.start(fileLoadOperationStream, n);
                        if (list == loadOperationQueue && !this.activeFileLoadOperation.contains(fileLoadOperation)) {
                            this.activeFileLoadOperation.add(fileLoadOperation);
                        }
                    }
                }
            }
            return fileLoadOperation;
        }
        final File directory = getDirectory(4);
        FileLoadOperation fileLoadOperation3 = null;
        Label_0671: {
            Label_0521: {
                if (secureDocument == null) {
                    Label_0548: {
                        if (tl_fileLocationToBeDeprecated == null) {
                            Label_0599: {
                                if (document != null) {
                                    final FileLoadOperation fileLoadOperation2 = new FileLoadOperation(document, o);
                                    if (MessageObject.isVoiceDocument(document)) {
                                        fileLoadOperation3 = fileLoadOperation2;
                                    }
                                    else {
                                        fileLoadOperation3 = fileLoadOperation2;
                                        if (MessageObject.isVideoDocument(document)) {
                                            fileLoadOperation3 = fileLoadOperation2;
                                            break Label_0599;
                                        }
                                        break Label_0521;
                                    }
                                }
                                else {
                                    if (webFile == null) {
                                        index = 4;
                                        fileLoadOperation3 = fileLoadOperation;
                                        break Label_0671;
                                    }
                                    final FileLoadOperation fileLoadOperation4 = new FileLoadOperation(this.currentAccount, webFile);
                                    if (MessageObject.isVoiceWebDocument(webFile)) {
                                        fileLoadOperation3 = fileLoadOperation4;
                                    }
                                    else {
                                        if (MessageObject.isVideoWebDocument(webFile)) {
                                            fileLoadOperation3 = fileLoadOperation4;
                                            break Label_0599;
                                        }
                                        fileLoadOperation3 = fileLoadOperation4;
                                        if (MessageObject.isImageWebDocument(webFile)) {
                                            fileLoadOperation3 = fileLoadOperation4;
                                            break Label_0548;
                                        }
                                        break Label_0521;
                                    }
                                }
                                index = 1;
                                break Label_0671;
                            }
                            index = 2;
                            break Label_0671;
                        }
                        fileLoadOperation3 = new FileLoadOperation(imageLocation, o, s, index);
                    }
                    index = 0;
                    break Label_0671;
                }
                fileLoadOperation3 = new FileLoadOperation(secureDocument);
            }
            index = 3;
        }
        File directory2;
        if (datacenterId != 0 && datacenterId != 10) {
            if (datacenterId == 2) {
                fileLoadOperation3.setEncryptFile(true);
            }
            directory2 = directory;
        }
        else {
            directory2 = getDirectory(index);
        }
        fileLoadOperation3.setPaths(this.currentAccount, directory2, directory);
        if (datacenterId == 10) {
            fileLoadOperation3.setIsPreloadVideoOperation(true);
        }
        fileLoadOperation3.setDelegate((FileLoadOperation.FileLoadOperationDelegate)new FileLoadOperation.FileLoadOperationDelegate() {
            @Override
            public void didChangedLoadProgress(final FileLoadOperation fileLoadOperation, final float n) {
                if (FileLoader.this.delegate != null) {
                    FileLoader.this.delegate.fileLoadProgressChanged(key, n);
                }
            }
            
            @Override
            public void didFailedLoadingFile(final FileLoadOperation fileLoadOperation, final int n) {
                FileLoader.this.loadOperationPathsUI.remove(key);
                FileLoader.this.checkDownloadQueue(fileLoadOperation.getDatacenterId(), document, webFile, tl_fileLocationToBeDeprecated, key);
                if (FileLoader.this.delegate != null) {
                    FileLoader.this.delegate.fileDidFailedLoad(key, n);
                }
            }
            
            @Override
            public void didFinishLoadingFile(final FileLoadOperation fileLoadOperation, final File file) {
                if (!fileLoadOperation.isPreloadVideoOperation() && fileLoadOperation.isPreloadFinished()) {
                    return;
                }
                if (!fileLoadOperation.isPreloadVideoOperation()) {
                    FileLoader.this.loadOperationPathsUI.remove(key);
                    if (FileLoader.this.delegate != null) {
                        FileLoader.this.delegate.fileDidLoaded(key, file, index);
                    }
                }
                FileLoader.this.checkDownloadQueue(fileLoadOperation.getDatacenterId(), document, webFile, tl_fileLocationToBeDeprecated, key);
            }
        });
        datacenterId = fileLoadOperation3.getDatacenterId();
        final LinkedList<FileLoadOperation> audioLoadOperationQueue2 = this.getAudioLoadOperationQueue(datacenterId);
        final LinkedList<FileLoadOperation> photoLoadOperationQueue2 = this.getPhotoLoadOperationQueue(datacenterId);
        final LinkedList<FileLoadOperation> loadOperationQueue2 = this.getLoadOperationQueue(datacenterId);
        this.loadOperationPaths.put(key, fileLoadOperation3);
        fileLoadOperation3.setPriority(priority);
        if (index == 1) {
            if (priority > 0) {
                index = 3;
            }
            else {
                index = 1;
            }
            priority = this.currentAudioLoadOperationsCount.get(datacenterId);
            if (fileLoadOperationStream == null && priority >= index) {
                this.addOperationToQueue(fileLoadOperation3, audioLoadOperationQueue2);
            }
            else if (fileLoadOperation3.start(fileLoadOperationStream, n)) {
                this.currentAudioLoadOperationsCount.put(datacenterId, priority + 1);
            }
        }
        else if (tl_fileLocationToBeDeprecated == null && !MessageObject.isImageWebDocument(webFile)) {
            if (priority > 0) {
                index = 4;
            }
            else {
                index = 1;
            }
            priority = this.currentLoadOperationsCount.get(datacenterId);
            if (fileLoadOperationStream == null && priority >= index) {
                this.addOperationToQueue(fileLoadOperation3, loadOperationQueue2);
            }
            else {
                if (fileLoadOperation3.start(fileLoadOperationStream, n)) {
                    this.currentLoadOperationsCount.put(datacenterId, priority + 1);
                    this.activeFileLoadOperation.add(fileLoadOperation3);
                }
                if (fileLoadOperation3.wasStarted() && fileLoadOperationStream != null) {
                    this.pauseCurrentFileLoadOperations(fileLoadOperation3);
                }
            }
        }
        else {
            if (priority > 0) {
                index = 6;
            }
            else {
                index = 2;
            }
            priority = this.currentPhotoLoadOperationsCount.get(datacenterId);
            if (fileLoadOperationStream == null && priority >= index) {
                this.addOperationToQueue(fileLoadOperation3, photoLoadOperationQueue2);
            }
            else if (fileLoadOperation3.start(fileLoadOperationStream, n)) {
                this.currentPhotoLoadOperationsCount.put(datacenterId, priority + 1);
            }
        }
        return fileLoadOperation3;
    }
    
    private void pauseCurrentFileLoadOperations(final FileLoadOperation fileLoadOperation) {
        int n;
        for (int i = 0; i < this.activeFileLoadOperation.size(); i = n + 1) {
            final FileLoadOperation fileLoadOperation2 = this.activeFileLoadOperation.get(i);
            n = i;
            if (fileLoadOperation2 != fileLoadOperation) {
                if (fileLoadOperation2.getDatacenterId() != fileLoadOperation.getDatacenterId()) {
                    n = i;
                }
                else {
                    this.activeFileLoadOperation.remove(fileLoadOperation2);
                    n = i - 1;
                    final int datacenterId = fileLoadOperation2.getDatacenterId();
                    this.getLoadOperationQueue(datacenterId).add(0, fileLoadOperation2);
                    if (fileLoadOperation2.wasStarted()) {
                        final SparseIntArray currentLoadOperationsCount = this.currentLoadOperationsCount;
                        currentLoadOperationsCount.put(datacenterId, currentLoadOperationsCount.get(datacenterId) - 1);
                    }
                    fileLoadOperation2.pause();
                }
            }
        }
    }
    
    private void removeLoadingVideoInternal(final TLRPC.Document document, final boolean b) {
        final String attachFileName = getAttachFileName(document);
        final StringBuilder sb = new StringBuilder();
        sb.append(attachFileName);
        String str;
        if (b) {
            str = "p";
        }
        else {
            str = "";
        }
        sb.append(str);
        if (this.loadingVideos.remove(sb.toString()) != null) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.videoLoadingStateChanged, attachFileName);
        }
    }
    
    public static void setMediaDirs(final SparseArray<File> mediaDirs) {
        FileLoader.mediaDirs = mediaDirs;
    }
    
    public void cancelLoadFile(final SecureDocument secureDocument) {
        this.cancelLoadFile(null, secureDocument, null, null, null);
    }
    
    public void cancelLoadFile(final WebFile webFile) {
        this.cancelLoadFile(null, null, webFile, null, null);
    }
    
    public void cancelLoadFile(final TLRPC.Document document) {
        this.cancelLoadFile(document, null, null, null, null);
    }
    
    public void cancelLoadFile(final TLRPC.FileLocation fileLocation, final String s) {
        this.cancelLoadFile(null, null, null, fileLocation, s);
    }
    
    public void cancelLoadFile(final TLRPC.PhotoSize photoSize) {
        this.cancelLoadFile(null, null, null, photoSize.location, null);
    }
    
    public void cancelUploadFile(final String s, final boolean b) {
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$iP93DCpFk_1vNZP_nXjq8znzYAg(this, b, s));
    }
    
    public void checkUploadNewDataAvailable(final String s, final boolean b, final long n, final long n2) {
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$BtLqmhj036PHY9Oj2RFTl8bO4mc(this, b, s, n, n2));
    }
    
    public void deleteFiles(final ArrayList<File> list, final int n) {
        if (list != null) {
            if (!list.isEmpty()) {
                FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$4dX6FY75qVi0nYcXFAFSA0OGeOs(list, n));
            }
        }
    }
    
    public float getBufferedProgressFromPosition(final float n, final String key) {
        if (TextUtils.isEmpty((CharSequence)key)) {
            return 0.0f;
        }
        final FileLoadOperation fileLoadOperation = this.loadOperationPaths.get(key);
        if (fileLoadOperation != null) {
            return fileLoadOperation.getDownloadedLengthFromOffset(n);
        }
        return 0.0f;
    }
    
    public int getFileReference(final Object value) {
        final int i = this.lastReferenceId++;
        this.parentObjectReferences.put(i, value);
        return i;
    }
    
    public Object getParentObject(final int i) {
        return this.parentObjectReferences.get(i);
    }
    
    public boolean isLoadingFile(final String key) {
        return this.loadOperationPathsUI.containsKey(key);
    }
    
    public boolean isLoadingVideo(final TLRPC.Document document, final boolean b) {
        if (document != null) {
            final HashMap<String, Boolean> loadingVideos = this.loadingVideos;
            final StringBuilder sb = new StringBuilder();
            sb.append(getAttachFileName(document));
            String str;
            if (b) {
                str = "p";
            }
            else {
                str = "";
            }
            sb.append(str);
            if (loadingVideos.containsKey(sb.toString())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLoadingVideoAny(final TLRPC.Document document) {
        boolean b = false;
        if (this.isLoadingVideo(document, false) || this.isLoadingVideo(document, true)) {
            b = true;
        }
        return b;
    }
    
    public void loadFile(final ImageLocation imageLocation, final Object o, final String s, final int n, int n2) {
        if (imageLocation == null) {
            return;
        }
        if (n2 == 0 && (imageLocation.isEncrypted() || (imageLocation.photoSize != null && imageLocation.getSize() == 0))) {
            n2 = 1;
        }
        this.loadFile(imageLocation.document, imageLocation.secureDocument, imageLocation.webFile, imageLocation.location, imageLocation, o, s, imageLocation.getSize(), n, n2);
    }
    
    public void loadFile(final SecureDocument secureDocument, final int n) {
        if (secureDocument == null) {
            return;
        }
        this.loadFile(null, secureDocument, null, null, null, null, null, 0, n, 1);
    }
    
    public void loadFile(final WebFile webFile, final int n, final int n2) {
        this.loadFile(null, null, webFile, null, null, null, null, 0, n, n2);
    }
    
    public void loadFile(final TLRPC.Document document, final Object o, final int n, int n2) {
        if (document == null) {
            return;
        }
        if (n2 == 0 && document.key != null) {
            n2 = 1;
        }
        if (n2 == 2) {
            FileLog.d("test");
        }
        this.loadFile(document, null, null, null, null, o, null, 0, n, n2);
    }
    
    protected FileLoadOperation loadStreamFile(final FileLoadOperationStream fileLoadOperationStream, final TLRPC.Document document, final Object o, final int n) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final FileLoadOperation[] array = { null };
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$DMcEynaB8xpg04Y8QygQNaKjBb4(this, array, document, o, fileLoadOperationStream, n, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public void onNetworkChanged(final boolean b) {
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$YYo8hp0C6_EIkcQEvJqmJMnz1R4(this, b));
    }
    
    public void removeLoadingVideo(final TLRPC.Document document, final boolean b, final boolean b2) {
        if (document == null) {
            return;
        }
        if (b2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$FileLoader$hhzyW9_Gs7B9dgoeAzy05Nfkj9g(this, document, b));
        }
        else {
            this.removeLoadingVideoInternal(document, b);
        }
    }
    
    public void setDelegate(final FileLoaderDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setLoadingVideo(final TLRPC.Document document, final boolean b, final boolean b2) {
        if (document == null) {
            return;
        }
        if (b2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$FileLoader$5K2sGZscq7bEKvLM0V2ywbk86iM(this, document, b));
        }
        else {
            this.setLoadingVideoInternal(document, b);
        }
    }
    
    public void setLoadingVideoForPlayer(final TLRPC.Document document, final boolean b) {
        if (document == null) {
            return;
        }
        final String attachFileName = getAttachFileName(document);
        final HashMap<String, Boolean> loadingVideos = this.loadingVideos;
        final StringBuilder sb = new StringBuilder();
        sb.append(attachFileName);
        final String s = "";
        String str;
        if (b) {
            str = "";
        }
        else {
            str = "p";
        }
        sb.append(str);
        if (loadingVideos.containsKey(sb.toString())) {
            final HashMap<String, Boolean> loadingVideos2 = this.loadingVideos;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(attachFileName);
            String str2 = s;
            if (b) {
                str2 = "p";
            }
            sb2.append(str2);
            loadingVideos2.put(sb2.toString(), true);
        }
    }
    
    public void setLoadingVideoInternal(final TLRPC.Document document, final boolean b) {
        final String attachFileName = getAttachFileName(document);
        final StringBuilder sb = new StringBuilder();
        sb.append(attachFileName);
        String str;
        if (b) {
            str = "p";
        }
        else {
            str = "";
        }
        sb.append(str);
        this.loadingVideos.put(sb.toString(), true);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.videoLoadingStateChanged, attachFileName);
    }
    
    public void uploadFile(final String s, final boolean b, final boolean b2, final int n) {
        this.uploadFile(s, b, b2, 0, n);
    }
    
    public void uploadFile(final String s, final boolean b, final boolean b2, final int n, final int n2) {
        if (s == null) {
            return;
        }
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$VC4JseGAlGdgB_OxKhWFev1MyY0(this, b, s, n, n2, b2));
    }
    
    public interface FileLoaderDelegate
    {
        void fileDidFailedLoad(final String p0, final int p1);
        
        void fileDidFailedUpload(final String p0, final boolean p1);
        
        void fileDidLoaded(final String p0, final File p1, final int p2);
        
        void fileDidUploaded(final String p0, final TLRPC.InputFile p1, final TLRPC.InputEncryptedFile p2, final byte[] p3, final byte[] p4, final long p5);
        
        void fileLoadProgressChanged(final String p0, final float p1);
        
        void fileUploadProgressChanged(final String p0, final float p1, final boolean p2);
    }
}
