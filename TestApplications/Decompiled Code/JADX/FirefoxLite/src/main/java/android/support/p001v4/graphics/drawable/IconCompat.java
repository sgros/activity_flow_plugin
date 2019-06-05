package android.support.p001v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.p001v4.content.ContextCompat;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

/* renamed from: android.support.v4.graphics.drawable.IconCompat */
public class IconCompat extends CustomVersionedParcelable {
    static final Mode DEFAULT_TINT_MODE = Mode.SRC_IN;
    public byte[] mData;
    public int mInt1;
    public int mInt2;
    Object mObj1;
    public Parcelable mParcelable;
    public ColorStateList mTintList = null;
    Mode mTintMode = DEFAULT_TINT_MODE;
    public String mTintModeStr;
    public int mType;

    private static String typeToString(int i) {
        switch (i) {
            case 1:
                return "BITMAP";
            case 2:
                return "RESOURCE";
            case 3:
                return "DATA";
            case 4:
                return "URI";
            case 5:
                return "BITMAP_MASKABLE";
            default:
                return "UNKNOWN";
        }
    }

    public static IconCompat createWithBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            IconCompat iconCompat = new IconCompat(1);
            iconCompat.mObj1 = bitmap;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    private IconCompat(int i) {
        this.mType = i;
    }

    public String getResPackage() {
        if (this.mType == -1 && VERSION.SDK_INT >= 23) {
            return IconCompat.getResPackage((Icon) this.mObj1);
        }
        if (this.mType == 2) {
            return ((String) this.mObj1).split(":", -1)[0];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResPackage() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int getResId() {
        if (this.mType == -1 && VERSION.SDK_INT >= 23) {
            return IconCompat.getResId((Icon) this.mObj1);
        }
        if (this.mType == 2) {
            return this.mInt1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResId() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public Icon toIcon() {
        int i = this.mType;
        if (i == -1) {
            return (Icon) this.mObj1;
        }
        Icon createWithBitmap;
        switch (i) {
            case 1:
                createWithBitmap = Icon.createWithBitmap((Bitmap) this.mObj1);
                break;
            case 2:
                createWithBitmap = Icon.createWithResource(getResPackage(), this.mInt1);
                break;
            case 3:
                createWithBitmap = Icon.createWithData((byte[]) this.mObj1, this.mInt1, this.mInt2);
                break;
            case 4:
                createWithBitmap = Icon.createWithContentUri((String) this.mObj1);
                break;
            case 5:
                if (VERSION.SDK_INT < 26) {
                    createWithBitmap = Icon.createWithBitmap(IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, false));
                    break;
                }
                createWithBitmap = Icon.createWithAdaptiveBitmap((Bitmap) this.mObj1);
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }
        if (this.mTintList != null) {
            createWithBitmap.setTintList(this.mTintList);
        }
        if (this.mTintMode != DEFAULT_TINT_MODE) {
            createWithBitmap.setTintMode(this.mTintMode);
        }
        return createWithBitmap;
    }

    public void checkResource(Context context) {
        if (this.mType == 2) {
            String str = (String) this.mObj1;
            if (str.contains(":")) {
                String str2 = str.split(":", -1)[1];
                String str3 = str2.split("/", -1)[0];
                str2 = str2.split("/", -1)[1];
                str = str.split(":", -1)[0];
                int identifier = IconCompat.getResources(context, str).getIdentifier(str2, str3, str);
                if (this.mInt1 != identifier) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Id has changed for ");
                    stringBuilder.append(str);
                    stringBuilder.append("/");
                    stringBuilder.append(str2);
                    Log.i("IconCompat", stringBuilder.toString());
                    this.mInt1 = identifier;
                }
            }
        }
    }

    private static Resources getResources(Context context, String str) {
        if ("android".equals(str)) {
            return Resources.getSystem();
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        } catch (NameNotFoundException e) {
            Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", new Object[]{str}), e);
            return null;
        }
    }

    public void addToShortcutIntent(Intent intent, Drawable drawable, Context context) {
        Bitmap bitmap;
        checkResource(context);
        int i = this.mType;
        if (i != 5) {
            switch (i) {
                case 1:
                    bitmap = (Bitmap) this.mObj1;
                    if (drawable != null) {
                        bitmap = bitmap.copy(bitmap.getConfig(), true);
                        break;
                    }
                    break;
                case 2:
                    try {
                        context = context.createPackageContext(getResPackage(), 0);
                        if (drawable == null) {
                            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(context, this.mInt1));
                            return;
                        }
                        Drawable drawable2 = ContextCompat.getDrawable(context, this.mInt1);
                        if (drawable2.getIntrinsicWidth() > 0) {
                            if (drawable2.getIntrinsicHeight() > 0) {
                                bitmap = Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Config.ARGB_8888);
                                drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                                drawable2.draw(new Canvas(bitmap));
                                break;
                            }
                        }
                        int launcherLargeIconSize = ((ActivityManager) context.getSystemService("activity")).getLauncherLargeIconSize();
                        bitmap = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Config.ARGB_8888);
                        drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        drawable2.draw(new Canvas(bitmap));
                    } catch (NameNotFoundException e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Can't find package ");
                        stringBuilder.append(this.mObj1);
                        throw new IllegalArgumentException(stringBuilder.toString(), e);
                    }
                default:
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
            }
        }
        bitmap = IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap) this.mObj1, true);
        if (drawable != null) {
            i = bitmap.getWidth();
            int height = bitmap.getHeight();
            drawable.setBounds(i / 2, height / 2, i, height);
            drawable.draw(new Canvas(bitmap));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
    }

    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        StringBuilder stringBuilder = new StringBuilder("Icon(typ=");
        stringBuilder.append(IconCompat.typeToString(this.mType));
        switch (this.mType) {
            case 1:
            case 5:
                stringBuilder.append(" size=");
                stringBuilder.append(((Bitmap) this.mObj1).getWidth());
                stringBuilder.append("x");
                stringBuilder.append(((Bitmap) this.mObj1).getHeight());
                break;
            case 2:
                stringBuilder.append(" pkg=");
                stringBuilder.append(getResPackage());
                stringBuilder.append(" id=");
                stringBuilder.append(String.format("0x%08x", new Object[]{Integer.valueOf(getResId())}));
                break;
            case 3:
                stringBuilder.append(" len=");
                stringBuilder.append(this.mInt1);
                if (this.mInt2 != 0) {
                    stringBuilder.append(" off=");
                    stringBuilder.append(this.mInt2);
                    break;
                }
                break;
            case 4:
                stringBuilder.append(" uri=");
                stringBuilder.append(this.mObj1);
                break;
        }
        if (this.mTintList != null) {
            stringBuilder.append(" tint=");
            stringBuilder.append(this.mTintList);
        }
        if (this.mTintMode != DEFAULT_TINT_MODE) {
            stringBuilder.append(" mode=");
            stringBuilder.append(this.mTintMode);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void onPreParceling(boolean z) {
        this.mTintModeStr = this.mTintMode.name();
        int i = this.mType;
        if (i != -1) {
            switch (i) {
                case 1:
                case 5:
                    if (z) {
                        Bitmap bitmap = (Bitmap) this.mObj1;
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(CompressFormat.PNG, 90, byteArrayOutputStream);
                        this.mData = byteArrayOutputStream.toByteArray();
                        return;
                    }
                    this.mParcelable = (Parcelable) this.mObj1;
                    return;
                case 2:
                    this.mData = ((String) this.mObj1).getBytes(Charset.forName("UTF-16"));
                    return;
                case 3:
                    this.mData = (byte[]) this.mObj1;
                    return;
                case 4:
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                    return;
                default:
                    return;
            }
        } else if (z) {
            throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
        } else {
            this.mParcelable = (Parcelable) this.mObj1;
        }
    }

    public void onPostParceling() {
        this.mTintMode = Mode.valueOf(this.mTintModeStr);
        int i = this.mType;
        if (i != -1) {
            switch (i) {
                case 1:
                case 5:
                    if (this.mParcelable != null) {
                        this.mObj1 = this.mParcelable;
                        return;
                    }
                    this.mObj1 = this.mData;
                    this.mType = 3;
                    this.mInt1 = 0;
                    this.mInt2 = this.mData.length;
                    return;
                case 2:
                case 4:
                    this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                    return;
                case 3:
                    this.mObj1 = this.mData;
                    return;
                default:
                    return;
            }
        } else if (this.mParcelable != null) {
            this.mObj1 = this.mParcelable;
        } else {
            throw new IllegalArgumentException("Invalid icon");
        }
    }

    private static String getResPackage(Icon icon) {
        if (VERSION.SDK_INT >= 28) {
            return icon.getResPackage();
        }
        try {
            return (String) icon.getClass().getMethod("getResPackage", new Class[0]).invoke(icon, new Object[0]);
        } catch (IllegalAccessException e) {
            Log.e("IconCompat", "Unable to get icon package", e);
            return null;
        } catch (InvocationTargetException e2) {
            Log.e("IconCompat", "Unable to get icon package", e2);
            return null;
        } catch (NoSuchMethodException e3) {
            Log.e("IconCompat", "Unable to get icon package", e3);
            return null;
        }
    }

    private static int getResId(Icon icon) {
        if (VERSION.SDK_INT >= 28) {
            return icon.getResId();
        }
        try {
            return ((Integer) icon.getClass().getMethod("getResId", new Class[0]).invoke(icon, new Object[0])).intValue();
        } catch (IllegalAccessException e) {
            Log.e("IconCompat", "Unable to get icon resource", e);
            return 0;
        } catch (InvocationTargetException e2) {
            Log.e("IconCompat", "Unable to get icon resource", e2);
            return 0;
        } catch (NoSuchMethodException e3) {
            Log.e("IconCompat", "Unable to get icon resource", e3);
            return 0;
        }
    }

    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap bitmap, boolean z) {
        int min = (int) (((float) Math.min(bitmap.getWidth(), bitmap.getHeight())) * 0.6666667f);
        Bitmap createBitmap = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(3);
        float f = (float) min;
        float f2 = 0.5f * f;
        float f3 = 0.9166667f * f2;
        if (z) {
            float f4 = 0.010416667f * f;
            paint.setColor(0);
            paint.setShadowLayer(f4, 0.0f, f * 0.020833334f, 1023410176);
            canvas.drawCircle(f2, f2, f3, paint);
            paint.setShadowLayer(f4, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(f2, f2, f3, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate((float) ((-(bitmap.getWidth() - min)) / 2), (float) ((-(bitmap.getHeight() - min)) / 2));
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(f2, f2, f3, paint);
        canvas.setBitmap(null);
        return createBitmap;
    }
}
