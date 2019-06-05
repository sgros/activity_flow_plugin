// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics.drawable;

import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import android.app.ActivityManager;
import android.support.v4.content.ContextCompat;
import android.content.Intent$ShortcutIconResource;
import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.res.Resources;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.os.Build$VERSION;
import android.graphics.drawable.Icon;
import android.graphics.Shader;
import android.graphics.Matrix;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.content.res.ColorStateList;
import android.os.Parcelable;
import android.graphics.PorterDuff$Mode;
import androidx.versionedparcelable.CustomVersionedParcelable;

public class IconCompat extends CustomVersionedParcelable
{
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    public byte[] mData;
    public int mInt1;
    public int mInt2;
    Object mObj1;
    public Parcelable mParcelable;
    public ColorStateList mTintList;
    PorterDuff$Mode mTintMode;
    public String mTintModeStr;
    public int mType;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    public IconCompat() {
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
    }
    
    private IconCompat(final int mType) {
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
        this.mType = mType;
    }
    
    static Bitmap createLegacyIconFromAdaptiveIcon(final Bitmap bitmap, final boolean b) {
        final int n = (int)(Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.6666667f);
        final Bitmap bitmap2 = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint(3);
        final float n2 = (float)n;
        final float n3 = 0.5f * n2;
        final float n4 = 0.9166667f * n3;
        if (b) {
            final float n5 = 0.010416667f * n2;
            paint.setColor(0);
            paint.setShadowLayer(n5, 0.0f, n2 * 0.020833334f, 1023410176);
            canvas.drawCircle(n3, n3, n4, paint);
            paint.setShadowLayer(n5, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(n3, n3, n4, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        final BitmapShader shader = new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
        final Matrix localMatrix = new Matrix();
        localMatrix.setTranslate((float)(-(bitmap.getWidth() - n) / 2), (float)(-(bitmap.getHeight() - n) / 2));
        shader.setLocalMatrix(localMatrix);
        paint.setShader((Shader)shader);
        canvas.drawCircle(n3, n3, n4, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    public static IconCompat createWithBitmap(final Bitmap mObj1) {
        if (mObj1 != null) {
            final IconCompat iconCompat = new IconCompat(1);
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }
    
    private static int getResId(final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getResId();
        }
        try {
            return (int)obj.getClass().getMethod("getResId", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex);
            return 0;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex2);
            return 0;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex3);
            return 0;
        }
    }
    
    private static String getResPackage(final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getResPackage();
        }
        try {
            return (String)obj.getClass().getMethod("getResPackage", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex);
            return null;
        }
        catch (InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex2);
            return null;
        }
        catch (IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex3);
            return null;
        }
    }
    
    private static Resources getResources(final Context context, final String anObject) {
        if ("android".equals(anObject)) {
            return Resources.getSystem();
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(anObject, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", anObject), (Throwable)ex);
            return null;
        }
    }
    
    private static String typeToString(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 5: {
                return "BITMAP_MASKABLE";
            }
            case 4: {
                return "URI";
            }
            case 3: {
                return "DATA";
            }
            case 2: {
                return "RESOURCE";
            }
            case 1: {
                return "BITMAP";
            }
        }
    }
    
    public void addToShortcutIntent(final Intent intent, final Drawable drawable, Context packageContext) {
        this.checkResource(packageContext);
        final int mType = this.mType;
        Bitmap bitmap = null;
        if (mType != 5) {
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
                }
                case 2: {
                    try {
                        packageContext = packageContext.createPackageContext(this.getResPackage(), 0);
                        if (drawable == null) {
                            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent$ShortcutIconResource.fromContext(packageContext, this.mInt1));
                            return;
                        }
                        final Drawable drawable2 = ContextCompat.getDrawable(packageContext, this.mInt1);
                        if (drawable2.getIntrinsicWidth() > 0 && drawable2.getIntrinsicHeight() > 0) {
                            bitmap = Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Bitmap$Config.ARGB_8888);
                        }
                        else {
                            final int launcherLargeIconSize = ((ActivityManager)packageContext.getSystemService("activity")).getLauncherLargeIconSize();
                            bitmap = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Bitmap$Config.ARGB_8888);
                        }
                        drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        drawable2.draw(new Canvas(bitmap));
                        break;
                    }
                    catch (PackageManager$NameNotFoundException cause) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Can't find package ");
                        sb.append(this.mObj1);
                        throw new IllegalArgumentException(sb.toString(), (Throwable)cause);
                    }
                }
                case 1: {
                    final Bitmap bitmap2 = bitmap = (Bitmap)this.mObj1;
                    if (drawable != null) {
                        bitmap = bitmap2.copy(bitmap2.getConfig(), true);
                        break;
                    }
                    break;
                }
            }
        }
        else {
            bitmap = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
        }
        if (drawable != null) {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            drawable.setBounds(width / 2, height / 2, width, height);
            drawable.draw(new Canvas(bitmap));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmap);
    }
    
    public void checkResource(final Context context) {
        if (this.mType == 2) {
            final String s = (String)this.mObj1;
            if (!s.contains(":")) {
                return;
            }
            final String s2 = s.split(":", -1)[1];
            final String s3 = s2.split("/", -1)[0];
            final String str = s2.split("/", -1)[1];
            final String str2 = s.split(":", -1)[0];
            final int identifier = getResources(context, str2).getIdentifier(str, s3, str2);
            if (this.mInt1 != identifier) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Id has changed for ");
                sb.append(str2);
                sb.append("/");
                sb.append(str);
                Log.i("IconCompat", sb.toString());
                this.mInt1 = identifier;
            }
        }
    }
    
    public int getResId() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResId((Icon)this.mObj1);
        }
        if (this.mType == 2) {
            return this.mInt1;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("called getResId() on ");
        sb.append(this);
        throw new IllegalStateException(sb.toString());
    }
    
    public String getResPackage() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResPackage((Icon)this.mObj1);
        }
        if (this.mType == 2) {
            return ((String)this.mObj1).split(":", -1)[0];
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("called getResPackage() on ");
        sb.append(this);
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void onPostParceling() {
        this.mTintMode = PorterDuff$Mode.valueOf(this.mTintModeStr);
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                case 3: {
                    this.mObj1 = this.mData;
                    break;
                }
                case 2:
                case 4: {
                    this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                    break;
                }
                case 1:
                case 5: {
                    if (this.mParcelable != null) {
                        this.mObj1 = this.mParcelable;
                        break;
                    }
                    this.mObj1 = this.mData;
                    this.mType = 3;
                    this.mInt1 = 0;
                    this.mInt2 = this.mData.length;
                    break;
                }
            }
        }
        else {
            if (this.mParcelable == null) {
                throw new IllegalArgumentException("Invalid icon");
            }
            this.mObj1 = this.mParcelable;
        }
    }
    
    @Override
    public void onPreParceling(final boolean b) {
        this.mTintModeStr = this.mTintMode.name();
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                case 4: {
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                    break;
                }
                case 3: {
                    this.mData = (byte[])this.mObj1;
                    break;
                }
                case 2: {
                    this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
                    break;
                }
                case 1:
                case 5: {
                    if (b) {
                        final Bitmap bitmap = (Bitmap)this.mObj1;
                        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap$CompressFormat.PNG, 90, (OutputStream)byteArrayOutputStream);
                        this.mData = byteArrayOutputStream.toByteArray();
                        break;
                    }
                    this.mParcelable = (Parcelable)this.mObj1;
                    break;
                }
            }
        }
        else {
            if (b) {
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            }
            this.mParcelable = (Parcelable)this.mObj1;
        }
    }
    
    public Icon toIcon() {
        final int mType = this.mType;
        if (mType != -1) {
            Icon icon = null;
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Unknown type");
                }
                case 5: {
                    if (Build$VERSION.SDK_INT >= 26) {
                        icon = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                        break;
                    }
                    icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                    break;
                }
                case 4: {
                    icon = Icon.createWithContentUri((String)this.mObj1);
                    break;
                }
                case 3: {
                    icon = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
                    break;
                }
                case 2: {
                    icon = Icon.createWithResource(this.getResPackage(), this.mInt1);
                    break;
                }
                case 1: {
                    icon = Icon.createWithBitmap((Bitmap)this.mObj1);
                    break;
                }
            }
            if (this.mTintList != null) {
                icon.setTintList(this.mTintList);
            }
            if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
                icon.setTintMode(this.mTintMode);
            }
            return icon;
        }
        return (Icon)this.mObj1;
    }
    
    @Override
    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        final StringBuilder sb = new StringBuilder("Icon(typ=");
        sb.append(typeToString(this.mType));
        switch (this.mType) {
            case 4: {
                sb.append(" uri=");
                sb.append(this.mObj1);
                break;
            }
            case 3: {
                sb.append(" len=");
                sb.append(this.mInt1);
                if (this.mInt2 != 0) {
                    sb.append(" off=");
                    sb.append(this.mInt2);
                    break;
                }
                break;
            }
            case 2: {
                sb.append(" pkg=");
                sb.append(this.getResPackage());
                sb.append(" id=");
                sb.append(String.format("0x%08x", this.getResId()));
                break;
            }
            case 1:
            case 5: {
                sb.append(" size=");
                sb.append(((Bitmap)this.mObj1).getWidth());
                sb.append("x");
                sb.append(((Bitmap)this.mObj1).getHeight());
                break;
            }
        }
        if (this.mTintList != null) {
            sb.append(" tint=");
            sb.append(this.mTintList);
        }
        if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            sb.append(" mode=");
            sb.append(this.mTintMode);
        }
        sb.append(")");
        return sb.toString();
    }
}
