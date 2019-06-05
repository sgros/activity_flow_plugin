// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import java.util.zip.ZipEntry;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import com.airbnb.lottie.network.NetworkFetcher;
import android.content.res.Resources$NotFoundException;
import com.airbnb.lottie.parser.LottieCompositionParser;
import java.io.Closeable;
import com.airbnb.lottie.utils.Utils;
import java.io.Reader;
import android.util.JsonReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import android.content.Context;
import java.util.Iterator;
import android.util.Log;
import com.airbnb.lottie.model.LottieCompositionCache;
import java.util.concurrent.Callable;
import java.util.HashMap;
import java.util.Map;

public class LottieCompositionFactory
{
    private static final Map<String, LottieTask<LottieComposition>> taskCache;
    
    static {
        taskCache = new HashMap<String, LottieTask<LottieComposition>>();
    }
    
    private static LottieTask<LottieComposition> cache(final String s, final Callable<LottieResult<LottieComposition>> callable) {
        final LottieComposition value = LottieCompositionCache.getInstance().get(s);
        if (value != null) {
            return new LottieTask<LottieComposition>((Callable<LottieResult<Object>>)new Callable<LottieResult<LottieComposition>>() {
                @Override
                public LottieResult<LottieComposition> call() {
                    Log.d("Gabe", "call\treturning from cache");
                    return new LottieResult<LottieComposition>(value);
                }
            });
        }
        if (LottieCompositionFactory.taskCache.containsKey(s)) {
            return LottieCompositionFactory.taskCache.get(s);
        }
        final LottieTask<LottieComposition> lottieTask = new LottieTask<LottieComposition>(callable);
        lottieTask.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(final LottieComposition lottieComposition) {
                if (s != null) {
                    LottieCompositionCache.getInstance().put(s, lottieComposition);
                }
                LottieCompositionFactory.taskCache.remove(s);
            }
        });
        lottieTask.addFailureListener(new LottieListener<Throwable>() {
            @Override
            public void onResult(final Throwable t) {
                LottieCompositionFactory.taskCache.remove(s);
            }
        });
        LottieCompositionFactory.taskCache.put(s, lottieTask);
        return lottieTask;
    }
    
    private static LottieImageAsset findImageAssetForFileName(final LottieComposition lottieComposition, final String anObject) {
        for (final LottieImageAsset lottieImageAsset : lottieComposition.getImages().values()) {
            if (lottieImageAsset.getFileName().equals(anObject)) {
                return lottieImageAsset;
            }
        }
        return null;
    }
    
    public static LottieTask<LottieComposition> fromAsset(final Context context, final String s) {
        return cache(s, new Callable<LottieResult<LottieComposition>>() {
            final /* synthetic */ Context val$appContext = context.getApplicationContext();
            
            @Override
            public LottieResult<LottieComposition> call() {
                return LottieCompositionFactory.fromAssetSync(this.val$appContext, s);
            }
        });
    }
    
    public static LottieResult<LottieComposition> fromAssetSync(final Context context, final String str) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("asset_");
            sb.append(str);
            final String string = sb.toString();
            if (str.endsWith(".zip")) {
                return fromZipStreamSync(new ZipInputStream(context.getAssets().open(str)), string);
            }
            return fromJsonInputStreamSync(context.getAssets().open(str), string);
        }
        catch (IOException ex) {
            return new LottieResult<LottieComposition>(ex);
        }
    }
    
    public static LottieResult<LottieComposition> fromJsonInputStreamSync(final InputStream inputStream, final String s) {
        return fromJsonInputStreamSync(inputStream, s, true);
    }
    
    private static LottieResult<LottieComposition> fromJsonInputStreamSync(final InputStream in, final String s, final boolean b) {
        try {
            return fromJsonReaderSync(new JsonReader((Reader)new InputStreamReader(in)), s);
        }
        finally {
            if (b) {
                Utils.closeQuietly(in);
            }
        }
    }
    
    public static LottieTask<LottieComposition> fromJsonReader(final JsonReader jsonReader, final String s) {
        return cache(s, new Callable<LottieResult<LottieComposition>>() {
            @Override
            public LottieResult<LottieComposition> call() {
                return LottieCompositionFactory.fromJsonReaderSync(jsonReader, s);
            }
        });
    }
    
    public static LottieResult<LottieComposition> fromJsonReaderSync(final JsonReader jsonReader, final String s) {
        try {
            final LottieComposition parse = LottieCompositionParser.parse(jsonReader);
            LottieCompositionCache.getInstance().put(s, parse);
            return new LottieResult<LottieComposition>(parse);
        }
        catch (Exception ex) {
            return new LottieResult<LottieComposition>(ex);
        }
    }
    
    public static LottieTask<LottieComposition> fromRawRes(Context applicationContext, final int n) {
        applicationContext = applicationContext.getApplicationContext();
        return cache(rawResCacheKey(n), new Callable<LottieResult<LottieComposition>>() {
            @Override
            public LottieResult<LottieComposition> call() {
                return LottieCompositionFactory.fromRawResSync(applicationContext, n);
            }
        });
    }
    
    public static LottieResult<LottieComposition> fromRawResSync(final Context context, final int n) {
        try {
            return fromJsonInputStreamSync(context.getResources().openRawResource(n), rawResCacheKey(n));
        }
        catch (Resources$NotFoundException ex) {
            return new LottieResult<LottieComposition>((Throwable)ex);
        }
    }
    
    public static LottieTask<LottieComposition> fromUrl(final Context context, final String s) {
        return NetworkFetcher.fetch(context, s);
    }
    
    public static LottieResult<LottieComposition> fromZipStreamSync(final ZipInputStream zipInputStream, final String s) {
        try {
            return fromZipStreamSyncInternal(zipInputStream, s);
        }
        finally {
            Utils.closeQuietly(zipInputStream);
        }
    }
    
    private static LottieResult<LottieComposition> fromZipStreamSyncInternal(final ZipInputStream zipInputStream, final String s) {
        final HashMap<String, Bitmap> hashMap = new HashMap<String, Bitmap>();
        try {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            LottieComposition lottieComposition = null;
            while (zipEntry != null) {
                if (zipEntry.getName().contains("__MACOSX")) {
                    zipInputStream.closeEntry();
                }
                else if (zipEntry.getName().contains(".json")) {
                    lottieComposition = fromJsonInputStreamSync(zipInputStream, s, false).getValue();
                }
                else if (zipEntry.getName().contains(".png")) {
                    final String[] split = zipEntry.getName().split("/");
                    hashMap.put(split[split.length - 1], BitmapFactory.decodeStream((InputStream)zipInputStream));
                }
                else {
                    zipInputStream.closeEntry();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            if (lottieComposition == null) {
                return new LottieResult<LottieComposition>(new IllegalArgumentException("Unable to parse composition"));
            }
            for (final Map.Entry<String, Bitmap> entry : hashMap.entrySet()) {
                final LottieImageAsset imageAssetForFileName = findImageAssetForFileName(lottieComposition, entry.getKey());
                if (imageAssetForFileName != null) {
                    imageAssetForFileName.setBitmap(entry.getValue());
                }
            }
            for (final Map.Entry<String, LottieImageAsset> entry2 : lottieComposition.getImages().entrySet()) {
                if (entry2.getValue().getBitmap() == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("There is no image for ");
                    sb.append(entry2.getValue().getFileName());
                    return new LottieResult<LottieComposition>(new IllegalStateException(sb.toString()));
                }
            }
            LottieCompositionCache.getInstance().put(s, lottieComposition);
            return new LottieResult<LottieComposition>(lottieComposition);
        }
        catch (IOException ex) {
            return new LottieResult<LottieComposition>(ex);
        }
    }
    
    private static String rawResCacheKey(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("rawRes_");
        sb.append(i);
        return sb.toString();
    }
}
