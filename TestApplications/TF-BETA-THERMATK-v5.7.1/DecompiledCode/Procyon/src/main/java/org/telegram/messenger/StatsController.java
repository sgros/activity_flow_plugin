// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.io.RandomAccessFile;

public class StatsController
{
    private static volatile StatsController[] Instance;
    private static final int TYPES_COUNT = 7;
    public static final int TYPE_AUDIOS = 3;
    public static final int TYPE_CALLS = 0;
    public static final int TYPE_FILES = 5;
    public static final int TYPE_MESSAGES = 1;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_PHOTOS = 4;
    public static final int TYPE_ROAMING = 2;
    public static final int TYPE_TOTAL = 6;
    public static final int TYPE_VIDEOS = 2;
    public static final int TYPE_WIFI = 1;
    private static final ThreadLocal<Long> lastStatsSaveTime;
    private static DispatchQueue statsSaveQueue;
    private byte[] buffer;
    private int[] callsTotalTime;
    private long lastInternalStatsSaveTime;
    private long[][] receivedBytes;
    private int[][] receivedItems;
    private long[] resetStatsDate;
    private Runnable saveRunnable;
    private long[][] sentBytes;
    private int[][] sentItems;
    private RandomAccessFile statsFile;
    
    static {
        StatsController.statsSaveQueue = new DispatchQueue("statsSaveQueue");
        lastStatsSaveTime = new ThreadLocal<Long>() {
            @Override
            protected Long initialValue() {
                return System.currentTimeMillis() - 1000L;
            }
        };
        StatsController.Instance = new StatsController[3];
    }
    
    private StatsController(int i) {
        this.buffer = new byte[8];
        this.sentBytes = new long[3][7];
        this.receivedBytes = new long[3][7];
        this.sentItems = new int[3][7];
        this.receivedItems = new int[3][7];
        this.resetStatsDate = new long[3];
        this.callsTotalTime = new int[3];
        this.saveRunnable = new Runnable() {
            @Override
            public void run() {
                final long currentTimeMillis = System.currentTimeMillis();
                if (Math.abs(currentTimeMillis - StatsController.this.lastInternalStatsSaveTime) < 2000L) {
                    return;
                }
                StatsController.this.lastInternalStatsSaveTime = currentTimeMillis;
                try {
                    StatsController.this.statsFile.seek(0L);
                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 7; ++j) {
                            StatsController.this.statsFile.write(StatsController.this.longToBytes(StatsController.this.sentBytes[i][j]), 0, 8);
                            StatsController.this.statsFile.write(StatsController.this.longToBytes(StatsController.this.receivedBytes[i][j]), 0, 8);
                            StatsController.this.statsFile.write(StatsController.this.intToBytes(StatsController.this.sentItems[i][j]), 0, 4);
                            StatsController.this.statsFile.write(StatsController.this.intToBytes(StatsController.this.receivedItems[i][j]), 0, 4);
                        }
                        StatsController.this.statsFile.write(StatsController.this.intToBytes(StatsController.this.callsTotalTime[i]), 0, 4);
                        StatsController.this.statsFile.write(StatsController.this.longToBytes(StatsController.this.resetStatsDate[i]), 0, 8);
                    }
                    StatsController.this.statsFile.getFD().sync();
                }
                catch (Exception ex) {}
            }
        };
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (i != 0) {
            final File filesDirFixed2 = ApplicationLoader.getFilesDirFixed();
            final StringBuilder sb = new StringBuilder();
            sb.append("account");
            sb.append(i);
            sb.append("/");
            filesDirFixed = new File(filesDirFixed2, sb.toString());
            filesDirFixed.mkdirs();
        }
        while (true) {
            try {
                this.statsFile = new RandomAccessFile(new File(filesDirFixed, "stats2.dat"), "rw");
                boolean b2;
                if (this.statsFile.length() > 0L) {
                    int j = 0;
                    boolean b = false;
                    while (j < 3) {
                        for (int k = 0; k < 7; ++k) {
                            this.statsFile.readFully(this.buffer, 0, 8);
                            this.sentBytes[j][k] = this.bytesToLong(this.buffer);
                            this.statsFile.readFully(this.buffer, 0, 8);
                            this.receivedBytes[j][k] = this.bytesToLong(this.buffer);
                            this.statsFile.readFully(this.buffer, 0, 4);
                            this.sentItems[j][k] = this.bytesToInt(this.buffer);
                            this.statsFile.readFully(this.buffer, 0, 4);
                            this.receivedItems[j][k] = this.bytesToInt(this.buffer);
                        }
                        this.statsFile.readFully(this.buffer, 0, 4);
                        this.callsTotalTime[j] = this.bytesToInt(this.buffer);
                        this.statsFile.readFully(this.buffer, 0, 8);
                        this.resetStatsDate[j] = this.bytesToLong(this.buffer);
                        if (this.resetStatsDate[j] == 0L) {
                            this.resetStatsDate[j] = System.currentTimeMillis();
                            b = true;
                        }
                        ++j;
                    }
                    if (b) {
                        this.saveStats();
                    }
                    b2 = false;
                }
                else {
                    b2 = true;
                }
                if (b2) {
                    SharedPreferences sharedPreferences;
                    if (i == 0) {
                        sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("stats", 0);
                    }
                    else {
                        final Context applicationContext = ApplicationLoader.applicationContext;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("stats");
                        sb2.append(i);
                        sharedPreferences = applicationContext.getSharedPreferences(sb2.toString(), 0);
                    }
                    i = 0;
                    boolean b3 = false;
                    while (i < 3) {
                        final int[] callsTotalTime = this.callsTotalTime;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("callsTotalTime");
                        sb3.append(i);
                        callsTotalTime[i] = sharedPreferences.getInt(sb3.toString(), 0);
                        final long[] resetStatsDate = this.resetStatsDate;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("resetStatsDate");
                        sb4.append(i);
                        resetStatsDate[i] = sharedPreferences.getLong(sb4.toString(), 0L);
                        for (int l = 0; l < 7; ++l) {
                            final long[] array = this.sentBytes[i];
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("sentBytes");
                            sb5.append(i);
                            sb5.append("_");
                            sb5.append(l);
                            array[l] = sharedPreferences.getLong(sb5.toString(), 0L);
                            final long[] array2 = this.receivedBytes[i];
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("receivedBytes");
                            sb6.append(i);
                            sb6.append("_");
                            sb6.append(l);
                            array2[l] = sharedPreferences.getLong(sb6.toString(), 0L);
                            final int[] array3 = this.sentItems[i];
                            final StringBuilder sb7 = new StringBuilder();
                            sb7.append("sentItems");
                            sb7.append(i);
                            sb7.append("_");
                            sb7.append(l);
                            array3[l] = sharedPreferences.getInt(sb7.toString(), 0);
                            final int[] array4 = this.receivedItems[i];
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append("receivedItems");
                            sb8.append(i);
                            sb8.append("_");
                            sb8.append(l);
                            array4[l] = sharedPreferences.getInt(sb8.toString(), 0);
                        }
                        final long[] resetStatsDate2 = this.resetStatsDate;
                        if (resetStatsDate2[i] == 0L) {
                            resetStatsDate2[i] = System.currentTimeMillis();
                            b3 = true;
                        }
                        ++i;
                    }
                    if (b3) {
                        this.saveStats();
                    }
                }
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    private int bytesToInt(final byte[] array) {
        return (array[3] & 0xFF) | (array[0] << 24 | (array[1] & 0xFF) << 16 | (array[2] & 0xFF) << 8);
    }
    
    private long bytesToLong(final byte[] array) {
        return ((long)array[0] & 0xFFL) << 56 | ((long)array[1] & 0xFFL) << 48 | ((long)array[2] & 0xFFL) << 40 | ((long)array[3] & 0xFFL) << 32 | ((long)array[4] & 0xFFL) << 24 | ((long)array[5] & 0xFFL) << 16 | ((long)array[6] & 0xFFL) << 8 | (0xFFL & (long)array[7]);
    }
    
    public static StatsController getInstance(final int n) {
        final StatsController statsController;
        if ((statsController = StatsController.Instance[n]) == null) {
            synchronized (StatsController.class) {
                if (StatsController.Instance[n] == null) {
                    StatsController.Instance[n] = new StatsController(n);
                }
            }
        }
        return statsController;
    }
    
    private byte[] intToBytes(final int n) {
        final byte[] buffer = this.buffer;
        buffer[0] = (byte)(n >>> 24);
        buffer[1] = (byte)(n >>> 16);
        buffer[2] = (byte)(n >>> 8);
        buffer[3] = (byte)n;
        return buffer;
    }
    
    private byte[] longToBytes(final long n) {
        final byte[] buffer = this.buffer;
        buffer[0] = (byte)(n >>> 56);
        buffer[1] = (byte)(n >>> 48);
        buffer[2] = (byte)(n >>> 40);
        buffer[3] = (byte)(n >>> 32);
        buffer[4] = (byte)(n >>> 24);
        buffer[5] = (byte)(n >>> 16);
        buffer[6] = (byte)(n >>> 8);
        buffer[7] = (byte)n;
        return buffer;
    }
    
    private void saveStats() {
        final long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - StatsController.lastStatsSaveTime.get()) >= 2000L) {
            StatsController.lastStatsSaveTime.set(currentTimeMillis);
            StatsController.statsSaveQueue.postRunnable(this.saveRunnable);
        }
    }
    
    public int getCallsTotalTime(final int n) {
        return this.callsTotalTime[n];
    }
    
    public long getReceivedBytesCount(final int n, final int n2) {
        if (n2 == 1) {
            final long[][] receivedBytes = this.receivedBytes;
            return receivedBytes[n][6] - receivedBytes[n][5] - receivedBytes[n][3] - receivedBytes[n][2] - receivedBytes[n][4];
        }
        return this.receivedBytes[n][n2];
    }
    
    public int getRecivedItemsCount(final int n, final int n2) {
        return this.receivedItems[n][n2];
    }
    
    public long getResetStatsDate(final int n) {
        return this.resetStatsDate[n];
    }
    
    public long getSentBytesCount(final int n, final int n2) {
        if (n2 == 1) {
            final long[][] sentBytes = this.sentBytes;
            return sentBytes[n][6] - sentBytes[n][5] - sentBytes[n][3] - sentBytes[n][2] - sentBytes[n][4];
        }
        return this.sentBytes[n][n2];
    }
    
    public int getSentItemsCount(final int n, final int n2) {
        return this.sentItems[n][n2];
    }
    
    public void incrementReceivedBytesCount(final int n, final int n2, final long n3) {
        final long[] array = this.receivedBytes[n];
        array[n2] += n3;
        this.saveStats();
    }
    
    public void incrementReceivedItemsCount(final int n, final int n2, final int n3) {
        final int[] array = this.receivedItems[n];
        array[n2] += n3;
        this.saveStats();
    }
    
    public void incrementSentBytesCount(final int n, final int n2, final long n3) {
        final long[] array = this.sentBytes[n];
        array[n2] += n3;
        this.saveStats();
    }
    
    public void incrementSentItemsCount(final int n, final int n2, final int n3) {
        final int[] array = this.sentItems[n];
        array[n2] += n3;
        this.saveStats();
    }
    
    public void incrementTotalCallsTime(final int n, final int n2) {
        final int[] callsTotalTime = this.callsTotalTime;
        callsTotalTime[n] += n2;
        this.saveStats();
    }
    
    public void resetStats(final int n) {
        this.resetStatsDate[n] = System.currentTimeMillis();
        for (int i = 0; i < 7; ++i) {
            this.sentBytes[n][i] = 0L;
            this.receivedBytes[n][i] = 0L;
            this.sentItems[n][i] = 0;
            this.receivedItems[n][i] = 0;
        }
        this.callsTotalTime[n] = 0;
        this.saveStats();
    }
}
