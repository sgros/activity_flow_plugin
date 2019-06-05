package org.mozilla.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.adjust.sdk.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.urlutils.UrlUtils;

public class FavIconUtils {

    public interface Consumer<T> {
        void accept(T t);
    }

    public static class SaveBitmapTask extends AsyncTask<Void, Void, String> {
        private Bitmap bitmap;
        private Consumer<String> callback;
        private final CompressFormat compressFormat;
        private File directory;
        private final int quality;
        private String url;

        public SaveBitmapTask(File file, String str, Bitmap bitmap, Consumer<String> consumer, CompressFormat compressFormat, int i) {
            this.directory = file;
            this.url = str;
            this.bitmap = bitmap;
            this.callback = consumer;
            this.compressFormat = compressFormat;
            this.quality = i;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String str) {
            this.callback.accept(str);
        }

        /* Access modifiers changed, original: protected|varargs */
        public String doInBackground(Void... voidArr) {
            return FavIconUtils.saveBitmapToDirectory(this.directory, this.url, this.bitmap, this.compressFormat, this.quality);
        }
    }

    public static class SaveBitmapsTask extends AsyncTask<Void, Void, List<String>> {
        private List<byte[]> bytesList;
        private Consumer<List<String>> callback;
        private final CompressFormat compressFormat;
        private File directory;
        private final int quality;
        private List<String> urls;

        public SaveBitmapsTask(File file, List<String> list, List<byte[]> list2, Consumer<List<String>> consumer, CompressFormat compressFormat, int i) {
            this.directory = file;
            this.urls = list;
            this.bytesList = list2;
            this.callback = consumer;
            this.compressFormat = compressFormat;
            this.quality = i;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(List<String> list) {
            this.callback.accept(list);
        }

        /* Access modifiers changed, original: protected|varargs */
        public List<String> doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.urls.size(); i++) {
                byte[] bArr = (byte[]) this.bytesList.get(i);
                arrayList.add(FavIconUtils.saveBitmapToDirectory(this.directory, (String) this.urls.get(i), BitmapFactory.decodeByteArray(bArr, 0, bArr.length), this.compressFormat, this.quality));
            }
            return arrayList;
        }
    }

    public static Bitmap getInitialBitmap(Bitmap bitmap, char c, float f, int i) {
        return getInitialBitmap(c, getDominantColor(bitmap), f, i);
    }

    public static Bitmap getInitialBitmap(char c, int i, float f, int i2) {
        char[] cArr = new char[]{c};
        int contractColor = getContractColor(i);
        Paint paint = new Paint();
        paint.setTextSize(f);
        paint.setColor(contractColor);
        paint.setTextAlign(Align.CENTER);
        paint.setAntiAlias(true);
        Rect rect = new Rect();
        paint.getTextBounds(cArr, 0, 1, rect);
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(i);
        canvas.drawText(cArr, 0, 1, ((float) i2) / 2.0f, ((float) (i2 + rect.height())) / 2.0f, paint);
        return createBitmap;
    }

    public static int getDominantColor(Bitmap bitmap) {
        return getDominantColor(bitmap, true);
    }

    private static int getDominantColor(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return Color.argb(255, 255, 255, 255);
        }
        int[] iArr = new int[36];
        float[] fArr = new float[36];
        float[] fArr2 = new float[36];
        float[] fArr3 = new float[36];
        float[] fArr4 = new float[3];
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] iArr2 = new int[(width * height)];
        int[] iArr3 = iArr2;
        int i = width;
        bitmap.getPixels(iArr2, 0, width, 0, 0, width, height);
        int i2 = 0;
        int i3 = -1;
        while (true) {
            int i4 = 2;
            if (i2 >= height) {
                break;
            }
            int i5 = 0;
            while (i5 < i) {
                int i6 = iArr3[(i2 * i) + i5];
                if (Color.alpha(i6) >= 128) {
                    Color.colorToHSV(i6, fArr4);
                    if (!z || (fArr4[1] > 0.35f && fArr4[i4] > 0.35f)) {
                        int floor = (int) Math.floor((double) (fArr4[0] / 10.0f));
                        fArr[floor] = fArr[floor] + fArr4[0];
                        fArr2[floor] = fArr2[floor] + fArr4[1];
                        fArr3[floor] = fArr3[floor] + fArr4[i4];
                        iArr[floor] = iArr[floor] + 1;
                        if (i3 < 0 || iArr[floor] > iArr[i3]) {
                            i3 = floor;
                        }
                    }
                }
                i5++;
                i4 = 2;
            }
            i2++;
        }
        if (i3 < 0) {
            return Color.argb(255, 255, 255, 255);
        }
        fArr4[0] = fArr[i3] / ((float) iArr[i3]);
        fArr4[1] = fArr2[i3] / ((float) iArr[i3]);
        fArr4[2] = fArr3[i3] / ((float) iArr[i3]);
        return Color.HSVToColor(fArr4);
    }

    private static int getContractColor(int i) {
        return 1.0d - ((((((double) Color.red(i)) * 0.299d) + (((double) Color.green(i)) * 0.587d)) + (((double) Color.blue(i)) * 0.114d)) / 255.0d) < 0.5d ? -16777216 : -1;
    }

    public static char getRepresentativeCharacter(String str) {
        if (TextUtils.isEmpty(str)) {
            return '?';
        }
        str = getRepresentativeSnippet(str);
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isLetterOrDigit(charAt)) {
                return Character.toUpperCase(charAt);
            }
        }
        return '?';
    }

    private static String getRepresentativeSnippet(String str) {
        Uri parse = Uri.parse(str);
        CharSequence host = parse.getHost();
        if (TextUtils.isEmpty(host)) {
            host = parse.getPath();
        }
        if (TextUtils.isEmpty(host)) {
            return "?";
        }
        return UrlUtils.stripCommonSubdomains(host);
    }

    public static String saveBitmapToDirectory(File file, String str, Bitmap bitmap, CompressFormat compressFormat, int i) {
        String encode = Uri.encode(str);
        try {
            str = generateMD5(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            str = encode;
        }
        return saveBitmapToFile(file, str, bitmap, compressFormat, i);
    }

    private static String saveBitmapToFile(File file, String str, Bitmap bitmap, CompressFormat compressFormat, int i) {
        Throwable th;
        Throwable th2;
        ensureDir(file);
        try {
            FileOutputStream fileOutputStream;
            File file2 = new File(file, str);
            if (file2.exists() && !file2.delete()) {
                return Uri.fromFile(file2).toString();
            }
            if (file2.createNewFile()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(compressFormat, i, byteArrayOutputStream);
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                fileOutputStream = new FileOutputStream(file2);
                try {
                    fileOutputStream.write(toByteArray);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return Uri.fromFile(file2).toString();
                } catch (Throwable th22) {
                    Throwable th3 = th22;
                    th22 = th;
                    th = th3;
                }
            } else if (file2.exists()) {
                return Uri.fromFile(file2).toString();
            } else {
                return null;
            }
            if (th22 != null) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th4) {
                    th22.addSuppressed(th4);
                }
            } else {
                fileOutputStream.close();
            }
            throw th;
            throw th;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getIconFromAssets(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException unused) {
            return null;
        }
    }

    public static Bitmap getBitmapFromUri(Context context, String str) {
        if (str.contains("//android_asset/")) {
            return getIconFromAssets(context, str.substring(str.indexOf("//android_asset/") + "//android_asset/".length()));
        }
        return BitmapFactory.decodeFile(Uri.parse(str).getPath(), new Options());
    }

    public static String generateMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance(Constants.MD5);
        instance.update(str.getBytes(Charset.defaultCharset()));
        byte[] digest = instance.digest();
        StringBuilder stringBuilder = new StringBuilder();
        int length = digest.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02X", new Object[]{Byte.valueOf(digest[i])}));
        }
        return stringBuilder.toString();
    }

    public static boolean ensureDir(File file) {
        boolean z = true;
        if (file.mkdirs()) {
            return true;
        }
        if (!(file.exists() && file.isDirectory() && file.canWrite())) {
            z = false;
        }
        return z;
    }
}
