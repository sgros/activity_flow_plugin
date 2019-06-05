package com.airbnb.lottie;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.Log;
import com.airbnb.lottie.model.LottieCompositionCache;
import com.airbnb.lottie.network.NetworkFetcher;
import com.airbnb.lottie.parser.LottieCompositionParser;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LottieCompositionFactory {
    private static final Map<String, LottieTask<LottieComposition>> taskCache = new HashMap();

    public static LottieTask<LottieComposition> fromUrl(Context context, String str) {
        return NetworkFetcher.fetch(context, str);
    }

    public static LottieTask<LottieComposition> fromAsset(Context context, final String str) {
        context = context.getApplicationContext();
        return cache(str, new Callable<LottieResult<LottieComposition>>() {
            public LottieResult<LottieComposition> call() {
                return LottieCompositionFactory.fromAssetSync(context, str);
            }
        });
    }

    public static LottieResult<LottieComposition> fromAssetSync(Context context, String str) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("asset_");
            stringBuilder.append(str);
            String stringBuilder2 = stringBuilder.toString();
            if (str.endsWith(".zip")) {
                return fromZipStreamSync(new ZipInputStream(context.getAssets().open(str)), stringBuilder2);
            }
            return fromJsonInputStreamSync(context.getAssets().open(str), stringBuilder2);
        } catch (IOException e) {
            return new LottieResult(e);
        }
    }

    public static LottieTask<LottieComposition> fromRawRes(Context context, final int i) {
        context = context.getApplicationContext();
        return cache(rawResCacheKey(i), new Callable<LottieResult<LottieComposition>>() {
            public LottieResult<LottieComposition> call() {
                return LottieCompositionFactory.fromRawResSync(context, i);
            }
        });
    }

    public static LottieResult<LottieComposition> fromRawResSync(Context context, int i) {
        try {
            return fromJsonInputStreamSync(context.getResources().openRawResource(i), rawResCacheKey(i));
        } catch (NotFoundException e) {
            return new LottieResult(e);
        }
    }

    private static String rawResCacheKey(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rawRes_");
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    public static LottieResult<LottieComposition> fromJsonInputStreamSync(InputStream inputStream, String str) {
        return fromJsonInputStreamSync(inputStream, str, true);
    }

    private static LottieResult<LottieComposition> fromJsonInputStreamSync(InputStream inputStream, String str, boolean z) {
        try {
            LottieResult<LottieComposition> fromJsonReaderSync = fromJsonReaderSync(new JsonReader(new InputStreamReader(inputStream)), str);
            return fromJsonReaderSync;
        } finally {
            if (z) {
                Utils.closeQuietly(inputStream);
            }
        }
    }

    public static LottieTask<LottieComposition> fromJsonReader(final JsonReader jsonReader, final String str) {
        return cache(str, new Callable<LottieResult<LottieComposition>>() {
            public LottieResult<LottieComposition> call() {
                return LottieCompositionFactory.fromJsonReaderSync(jsonReader, str);
            }
        });
    }

    public static LottieResult<LottieComposition> fromJsonReaderSync(JsonReader jsonReader, String str) {
        try {
            Object parse = LottieCompositionParser.parse(jsonReader);
            LottieCompositionCache.getInstance().put(str, parse);
            return new LottieResult(parse);
        } catch (Exception e) {
            return new LottieResult(e);
        }
    }

    public static LottieResult<LottieComposition> fromZipStreamSync(ZipInputStream zipInputStream, String str) {
        try {
            LottieResult<LottieComposition> fromZipStreamSyncInternal = fromZipStreamSyncInternal(zipInputStream, str);
            return fromZipStreamSyncInternal;
        } finally {
            Utils.closeQuietly(zipInputStream);
        }
    }

    private static LottieResult<LottieComposition> fromZipStreamSyncInternal(ZipInputStream zipInputStream, String str) {
        HashMap hashMap = new HashMap();
        try {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            Object obj = null;
            while (nextEntry != null) {
                if (nextEntry.getName().contains("__MACOSX")) {
                    zipInputStream.closeEntry();
                } else if (nextEntry.getName().contains(".json")) {
                    obj = (LottieComposition) fromJsonInputStreamSync(zipInputStream, str, false).getValue();
                } else if (nextEntry.getName().contains(".png")) {
                    String[] split = nextEntry.getName().split("/");
                    hashMap.put(split[split.length - 1], BitmapFactory.decodeStream(zipInputStream));
                } else {
                    zipInputStream.closeEntry();
                }
                nextEntry = zipInputStream.getNextEntry();
            }
            if (obj == null) {
                return new LottieResult(new IllegalArgumentException("Unable to parse composition"));
            }
            for (Entry entry : hashMap.entrySet()) {
                LottieImageAsset findImageAssetForFileName = findImageAssetForFileName(obj, (String) entry.getKey());
                if (findImageAssetForFileName != null) {
                    findImageAssetForFileName.setBitmap((Bitmap) entry.getValue());
                }
            }
            for (Entry entry2 : obj.getImages().entrySet()) {
                if (((LottieImageAsset) entry2.getValue()).getBitmap() == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("There is no image for ");
                    stringBuilder.append(((LottieImageAsset) entry2.getValue()).getFileName());
                    return new LottieResult(new IllegalStateException(stringBuilder.toString()));
                }
            }
            LottieCompositionCache.getInstance().put(str, obj);
            return new LottieResult(obj);
        } catch (IOException e) {
            return new LottieResult(e);
        }
    }

    private static LottieImageAsset findImageAssetForFileName(LottieComposition lottieComposition, String str) {
        for (LottieImageAsset lottieImageAsset : lottieComposition.getImages().values()) {
            if (lottieImageAsset.getFileName().equals(str)) {
                return lottieImageAsset;
            }
        }
        return null;
    }

    private static LottieTask<LottieComposition> cache(final String str, Callable<LottieResult<LottieComposition>> callable) {
        final LottieComposition lottieComposition = LottieCompositionCache.getInstance().get(str);
        if (lottieComposition != null) {
            return new LottieTask(new Callable<LottieResult<LottieComposition>>() {
                public LottieResult<LottieComposition> call() {
                    Log.d("Gabe", "call\treturning from cache");
                    return new LottieResult(lottieComposition);
                }
            });
        }
        if (taskCache.containsKey(str)) {
            return (LottieTask) taskCache.get(str);
        }
        LottieTask lottieTask = new LottieTask(callable);
        lottieTask.addListener(new LottieListener<LottieComposition>() {
            public void onResult(LottieComposition lottieComposition) {
                if (str != null) {
                    LottieCompositionCache.getInstance().put(str, lottieComposition);
                }
                LottieCompositionFactory.taskCache.remove(str);
            }
        });
        lottieTask.addFailureListener(new LottieListener<Throwable>() {
            public void onResult(Throwable th) {
                LottieCompositionFactory.taskCache.remove(str);
            }
        });
        taskCache.put(str, lottieTask);
        return lottieTask;
    }
}
