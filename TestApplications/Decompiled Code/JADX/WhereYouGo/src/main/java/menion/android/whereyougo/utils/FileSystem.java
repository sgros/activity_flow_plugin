package menion.android.whereyougo.utils;

import android.os.Environment;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;
import menion.android.whereyougo.MainApplication;

public class FileSystem {
    public static String CACHE = "cache/";
    public static String CACHE_AUDIO = (CACHE + "audio/");
    private static final String CARD_ROOT = "{CARD_ROOT}";
    private static final String[] EXTERNAL_DIRECTORIES = new String[]{"{CARD_ROOT}external_sd", "{CARD_ROOT}_externalsd", "{CARD_ROOT}sd", "{CARD_ROOT}emmc", "{CARD_ROOT}ext_sd", "/Removable/MicroSD", "/mnt/emms", "/mnt/external1"};
    public static String ROOT = null;
    private static final String TAG = "FileSystem";

    public static boolean createRoot(String appDirName) {
        if (ROOT != null && new File(ROOT).exists()) {
            return true;
        }
        try {
            String cardRoot = getExternalStorageDir();
            if (cardRoot == null) {
                return false;
            }
            String externalCardRoot = null;
            for (String cardTestDir : EXTERNAL_DIRECTORIES) {
                String cardTestDir2;
                if (cardTestDir2.contains(CARD_ROOT)) {
                    cardTestDir2 = cardTestDir2.replace(CARD_ROOT, cardRoot);
                }
                if (new File(cardTestDir2).exists()) {
                    externalCardRoot = cardTestDir2 + "/";
                    break;
                }
            }
            File appOnCard = new File(cardRoot + appDirName);
            if (externalCardRoot == null) {
                return setRootDirectory(cardRoot, appOnCard.getAbsolutePath());
            }
            File appOnExternalCard = new File(externalCardRoot + appDirName);
            if (appOnExternalCard.exists()) {
                return setRootDirectory(externalCardRoot, appOnExternalCard.getAbsolutePath());
            }
            if (appOnCard.exists()) {
                return setRootDirectory(cardRoot, appOnCard.getAbsolutePath());
            }
            return setRootDirectory(externalCardRoot, appOnExternalCard.getAbsolutePath());
        } catch (Exception ex) {
            Logger.m21e(TAG, "createRoot(), ex: " + ex.toString());
            return false;
        }
    }

    public static String getExternalStorageDir() {
        String cardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (cardRoot == null) {
            return null;
        }
        if (cardRoot.endsWith("/")) {
            return cardRoot;
        }
        return cardRoot + "/";
    }

    public static File[] getFiles(String folder, final String filter) {
        return getFiles2(folder, new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.getDefault()).endsWith(filter);
            }
        });
    }

    public static File[] getFiles2(String folder, FileFilter filter) {
        try {
            File file = new File(folder);
            if (file.exists()) {
                return file.listFiles(filter);
            }
            return new File[0];
        } catch (Exception e) {
            Logger.m21e(TAG, "getFiles2(), folder: " + folder);
            return new File[0];
        }
    }

    public static String getRoot() {
        if (ROOT == null) {
            createRoot(MainApplication.APP_NAME);
        }
        return ROOT;
    }

    public static void checkFolders(String fileName) {
        try {
            new File(fileName).getParentFile().mkdirs();
        } catch (Exception e) {
            Logger.m21e(TAG, "checkFolders(" + fileName + "), ex: " + e.toString());
        }
    }

    public static synchronized void saveBytes(String fileName, byte[] data) {
        synchronized (FileSystem.class) {
            try {
                if (data.length != 0) {
                    FileSystemDataWritter fileSystemDataWritter = new FileSystemDataWritter(fileName, data, -1);
                }
            } catch (Exception e) {
                Logger.m21e(TAG, "saveBytes(" + fileName + "), e: " + e.toString());
            }
        }
        return;
    }

    public static boolean setRootDirectory(String appRoot) {
        return setRootDirectory(null, appRoot);
    }

    private static boolean setRootDirectory(String cardRoot, String appRoot) {
        if (appRoot == null || appRoot.equals("")) {
            return false;
        }
        if (!appRoot.endsWith("/")) {
            appRoot = appRoot + "/";
        }
        File rootAppDir = new File(appRoot);
        if (!rootAppDir.exists() && !rootAppDir.mkdir()) {
            return false;
        }
        ROOT = appRoot;
        return true;
    }

    public static File findFile(String prefix, String extension) {
        File[] files = getFiles(ROOT, extension);
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (file.getName().startsWith(prefix)) {
                return file;
            }
        }
        return null;
    }

    public static File findFile(String prefix) {
        return findFile(prefix, "gwc");
    }

    public static boolean backupFile(File file) {
        try {
            if (file.length() > 0) {
                copyFile(file, new File(file.getAbsolutePath() + ".bak"));
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void copyFile(File source, File dest) throws IOException {
        if (!source.equals(dest)) {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            FileChannel sourceChannel = null;
            FileChannel destChannel = null;
            try {
                sourceChannel = new FileInputStream(source).getChannel();
                destChannel = new FileOutputStream(dest).getChannel();
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                if (sourceChannel != null) {
                    try {
                        sourceChannel.close();
                    } catch (IOException e) {
                    }
                }
                if (destChannel != null) {
                    try {
                        destChannel.close();
                    } catch (IOException e2) {
                    }
                }
                dest.setLastModified(source.lastModified());
            } catch (Throwable th) {
                if (sourceChannel != null) {
                    try {
                        sourceChannel.close();
                    } catch (IOException e3) {
                    }
                }
                if (destChannel != null) {
                    try {
                        destChannel.close();
                    } catch (IOException e4) {
                    }
                }
            }
        }
    }
}
