// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.icon;

import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap$CompressFormat;
import org.mozilla.urlutils.UrlUtils;
import android.text.TextUtils;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Rect;
import android.graphics.Paint$Align;
import android.graphics.Paint;
import android.content.res.AssetManager;
import java.io.IOException;
import android.graphics.Color;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import android.net.Uri;
import android.graphics.Bitmap;
import android.content.Context;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.io.File;

public class FavIconUtils
{
    public static boolean ensureDir(final File file) {
        final boolean mkdirs = file.mkdirs();
        boolean b = true;
        if (mkdirs) {
            return true;
        }
        if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
            b = false;
        }
        return b;
    }
    
    public static String generateMD5(final String s) throws NoSuchAlgorithmException {
        final MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(s.getBytes(Charset.defaultCharset()));
        final byte[] digest = instance.digest();
        final StringBuilder sb = new StringBuilder();
        for (int length = digest.length, i = 0; i < length; ++i) {
            sb.append(String.format("%02X", digest[i]));
        }
        return sb.toString();
    }
    
    public static Bitmap getBitmapFromUri(final Context context, final String s) {
        if (s.contains("//android_asset/")) {
            return getIconFromAssets(context, s.substring(s.indexOf("//android_asset/") + "//android_asset/".length()));
        }
        return BitmapFactory.decodeFile(Uri.parse(s).getPath(), new BitmapFactory$Options());
    }
    
    private static int getContractColor(int n) {
        if (1.0 - (Color.red(n) * 0.299 + Color.green(n) * 0.587 + Color.blue(n) * 0.114) / 255.0 < 0.5) {
            n = -16777216;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    public static int getDominantColor(final Bitmap bitmap) {
        return getDominantColor(bitmap, true);
    }
    
    private static int getDominantColor(final Bitmap bitmap, final boolean b) {
        if (bitmap == null) {
            return Color.argb(255, 255, 255, 255);
        }
        final int[] array = new int[36];
        final float[] array2 = new float[36];
        final float[] array3 = new float[36];
        final float[] array4 = new float[36];
        final float[] array5 = new float[3];
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        final int[] array6 = new int[width * height];
        bitmap.getPixels(array6, 0, width, 0, 0, width, height);
        int i = 0;
        int n = -1;
        while (i < height) {
            int n3;
            for (int j = 0; j < width; ++j, n = n3) {
                final int n2 = array6[i * width + j];
                if (Color.alpha(n2) < 128) {
                    n3 = n;
                }
                else {
                    Color.colorToHSV(n2, array5);
                    if (b) {
                        n3 = n;
                        if (array5[1] <= 0.35f) {
                            continue;
                        }
                        if (array5[2] <= 0.35f) {
                            n3 = n;
                            continue;
                        }
                    }
                    final int n4 = (int)Math.floor(array5[0] / 10.0f);
                    array2[n4] += array5[0];
                    array3[n4] += array5[1];
                    array4[n4] += array5[2];
                    ++array[n4];
                    if (n >= 0) {
                        n3 = n;
                        if (array[n4] <= array[n]) {
                            continue;
                        }
                    }
                    n3 = n4;
                }
            }
            ++i;
        }
        if (n < 0) {
            return Color.argb(255, 255, 255, 255);
        }
        array5[0] = array2[n] / array[n];
        array5[1] = array3[n] / array[n];
        array5[2] = array4[n] / array[n];
        return Color.HSVToColor(array5);
    }
    
    public static Bitmap getIconFromAssets(final Context context, final String s) {
        final AssetManager assets = context.getAssets();
        Bitmap decodeStream;
        try {
            decodeStream = BitmapFactory.decodeStream(assets.open(s));
        }
        catch (IOException ex) {
            decodeStream = null;
        }
        return decodeStream;
    }
    
    public static Bitmap getInitialBitmap(final char c, final int n, final float textSize, final int n2) {
        final char[] array = { c };
        final int contractColor = getContractColor(n);
        final Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(contractColor);
        paint.setTextAlign(Paint$Align.CENTER);
        paint.setAntiAlias(true);
        final Rect rect = new Rect();
        paint.getTextBounds(array, 0, 1, rect);
        final Bitmap bitmap = Bitmap.createBitmap(n2, n2, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(n);
        canvas.drawText(array, 0, 1, n2 / 2.0f, (n2 + rect.height()) / 2.0f, paint);
        return bitmap;
    }
    
    public static Bitmap getInitialBitmap(final Bitmap bitmap, final char c, final float n, final int n2) {
        return getInitialBitmap(c, getDominantColor(bitmap), n, n2);
    }
    
    public static char getRepresentativeCharacter(String representativeSnippet) {
        if (TextUtils.isEmpty((CharSequence)representativeSnippet)) {
            return '?';
        }
        representativeSnippet = getRepresentativeSnippet(representativeSnippet);
        for (int i = 0; i < representativeSnippet.length(); ++i) {
            final char char1 = representativeSnippet.charAt(i);
            if (Character.isLetterOrDigit(char1)) {
                return Character.toUpperCase(char1);
            }
        }
        return '?';
    }
    
    private static String getRepresentativeSnippet(String s) {
        final Uri parse = Uri.parse(s);
        if (TextUtils.isEmpty((CharSequence)(s = parse.getHost()))) {
            s = parse.getPath();
        }
        if (TextUtils.isEmpty((CharSequence)s)) {
            return "?";
        }
        return UrlUtils.stripCommonSubdomains(s);
    }
    
    public static String saveBitmapToDirectory(final File file, String generateMD5, final Bitmap bitmap, final Bitmap$CompressFormat bitmap$CompressFormat, final int n) {
        final String encode = Uri.encode(generateMD5);
        try {
            generateMD5 = generateMD5(generateMD5);
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            generateMD5 = encode;
        }
        return saveBitmapToFile(file, generateMD5, bitmap, bitmap$CompressFormat, n);
    }
    
    private static String saveBitmapToFile(final File parent, String child, Bitmap bitmap, final Bitmap$CompressFormat bitmap$CompressFormat, final int n) {
        ensureDir(parent);
        try {
            final File file = new File(parent, (String)child);
            if (file.exists() && !file.delete()) {
                return Uri.fromFile(file).toString();
            }
            if (file.createNewFile()) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(bitmap$CompressFormat, n, (OutputStream)byteArrayOutputStream);
                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                bitmap = (Bitmap)new FileOutputStream(file);
                try {
                    ((FileOutputStream)bitmap).write(byteArray);
                    ((OutputStream)bitmap).flush();
                    ((FileOutputStream)bitmap).close();
                    return Uri.fromFile(file).toString();
                }
                catch (Throwable child) {
                    try {
                        throw child;
                    }
                    finally {}
                }
                finally {
                    child = null;
                }
                if (child != null) {
                    try {
                        ((FileOutputStream)bitmap).close();
                    }
                    catch (Throwable exception) {
                        child.addSuppressed(exception);
                    }
                }
                else {
                    ((FileOutputStream)bitmap).close();
                }
                throw parent;
            }
            if (file.exists()) {
                return Uri.fromFile(file).toString();
            }
            return null;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public interface Consumer<T>
    {
        void accept(final T p0);
    }
    
    public static class SaveBitmapTask extends AsyncTask<Void, Void, String>
    {
        private Bitmap bitmap;
        private Consumer<String> callback;
        private final Bitmap$CompressFormat compressFormat;
        private File directory;
        private final int quality;
        private String url;
        
        public SaveBitmapTask(final File directory, final String url, final Bitmap bitmap, final Consumer<String> callback, final Bitmap$CompressFormat compressFormat, final int quality) {
            this.directory = directory;
            this.url = url;
            this.bitmap = bitmap;
            this.callback = callback;
            this.compressFormat = compressFormat;
            this.quality = quality;
        }
        
        protected String doInBackground(final Void... array) {
            return FavIconUtils.saveBitmapToDirectory(this.directory, this.url, this.bitmap, this.compressFormat, this.quality);
        }
        
        protected void onPostExecute(final String s) {
            this.callback.accept(s);
        }
    }
    
    public static class SaveBitmapsTask extends AsyncTask<Void, Void, List<String>>
    {
        private List<byte[]> bytesList;
        private Consumer<List<String>> callback;
        private final Bitmap$CompressFormat compressFormat;
        private File directory;
        private final int quality;
        private List<String> urls;
        
        public SaveBitmapsTask(final File directory, final List<String> urls, final List<byte[]> bytesList, final Consumer<List<String>> callback, final Bitmap$CompressFormat compressFormat, final int quality) {
            this.directory = directory;
            this.urls = urls;
            this.bytesList = bytesList;
            this.callback = callback;
            this.compressFormat = compressFormat;
            this.quality = quality;
        }
        
        protected List<String> doInBackground(final Void... array) {
            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < this.urls.size(); ++i) {
                final byte[] array2 = this.bytesList.get(i);
                list.add(FavIconUtils.saveBitmapToDirectory(this.directory, this.urls.get(i), BitmapFactory.decodeByteArray(array2, 0, array2.length), this.compressFormat, this.quality));
            }
            return list;
        }
        
        protected void onPostExecute(final List<String> list) {
            this.callback.accept(list);
        }
    }
}
