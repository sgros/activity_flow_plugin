// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.graphics.drawable;

import android.os.Bundle;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
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
        this.mType = -1;
        this.mData = null;
        this.mParcelable = null;
        this.mInt1 = 0;
        this.mInt2 = 0;
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
        this.mTintModeStr = null;
    }
    
    private IconCompat(final int mType) {
        this.mType = -1;
        this.mData = null;
        this.mParcelable = null;
        this.mInt1 = 0;
        this.mInt2 = 0;
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
        this.mTintModeStr = null;
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
        final Shader$TileMode clamp = Shader$TileMode.CLAMP;
        final BitmapShader shader = new BitmapShader(bitmap, clamp, clamp);
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
    
    private static String typeToString(final int n) {
        if (n == 1) {
            return "BITMAP";
        }
        if (n == 2) {
            return "RESOURCE";
        }
        if (n == 3) {
            return "DATA";
        }
        if (n == 4) {
            return "URI";
        }
        if (n != 5) {
            return "UNKNOWN";
        }
        return "BITMAP_MASKABLE";
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
    
    public void onPostParceling() {
        this.mTintMode = PorterDuff$Mode.valueOf(this.mTintModeStr);
        final int mType = this.mType;
        if (mType != -1) {
            Label_0084: {
                if (mType != 1) {
                    if (mType != 2) {
                        if (mType == 3) {
                            this.mObj1 = this.mData;
                            return;
                        }
                        if (mType != 4) {
                            if (mType != 5) {
                                return;
                            }
                            break Label_0084;
                        }
                    }
                    this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                    return;
                }
            }
            final Parcelable mParcelable = this.mParcelable;
            if (mParcelable != null) {
                this.mObj1 = mParcelable;
            }
            else {
                final byte[] mData = this.mData;
                this.mObj1 = mData;
                this.mType = 3;
                this.mInt1 = 0;
                this.mInt2 = mData.length;
            }
        }
        else {
            final Parcelable mParcelable2 = this.mParcelable;
            if (mParcelable2 == null) {
                throw new IllegalArgumentException("Invalid icon");
            }
            this.mObj1 = mParcelable2;
        }
    }
    
    public void onPreParceling(final boolean b) {
        this.mTintModeStr = this.mTintMode.name();
        final int mType = this.mType;
        if (mType != -1) {
            if (mType != 1) {
                if (mType == 2) {
                    this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
                    return;
                }
                if (mType == 3) {
                    this.mData = (byte[])this.mObj1;
                    return;
                }
                if (mType == 4) {
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                    return;
                }
                if (mType != 5) {
                    return;
                }
            }
            if (b) {
                final Bitmap bitmap = (Bitmap)this.mObj1;
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap$CompressFormat.PNG, 90, (OutputStream)byteArrayOutputStream);
                this.mData = byteArrayOutputStream.toByteArray();
            }
            else {
                this.mParcelable = (Parcelable)this.mObj1;
            }
        }
        else {
            if (b) {
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            }
            this.mParcelable = (Parcelable)this.mObj1;
        }
    }
    
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        final int mType = this.mType;
        Label_0122: {
            if (mType != -1) {
                Label_0091: {
                    if (mType != 1) {
                        if (mType != 2) {
                            if (mType == 3) {
                                bundle.putByteArray("obj", (byte[])this.mObj1);
                                break Label_0122;
                            }
                            if (mType != 4) {
                                if (mType == 5) {
                                    break Label_0091;
                                }
                                throw new IllegalArgumentException("Invalid icon");
                            }
                        }
                        bundle.putString("obj", (String)this.mObj1);
                        break Label_0122;
                    }
                }
                bundle.putParcelable("obj", (Parcelable)this.mObj1);
            }
            else {
                bundle.putParcelable("obj", (Parcelable)this.mObj1);
            }
        }
        bundle.putInt("type", this.mType);
        bundle.putInt("int1", this.mInt1);
        bundle.putInt("int2", this.mInt2);
        final ColorStateList mTintList = this.mTintList;
        if (mTintList != null) {
            bundle.putParcelable("tint_list", (Parcelable)mTintList);
        }
        final PorterDuff$Mode mTintMode = this.mTintMode;
        if (mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            bundle.putString("tint_mode", mTintMode.name());
        }
        return bundle;
    }
    
    public Icon toIcon() {
        final int mType = this.mType;
        if (mType != -1) {
            Icon icon;
            if (mType != 1) {
                if (mType != 2) {
                    if (mType != 3) {
                        if (mType != 4) {
                            if (mType != 5) {
                                throw new IllegalArgumentException("Unknown type");
                            }
                            if (Build$VERSION.SDK_INT >= 26) {
                                icon = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                            }
                            else {
                                icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                            }
                        }
                        else {
                            icon = Icon.createWithContentUri((String)this.mObj1);
                        }
                    }
                    else {
                        icon = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
                    }
                }
                else {
                    icon = Icon.createWithResource(this.getResPackage(), this.mInt1);
                }
            }
            else {
                icon = Icon.createWithBitmap((Bitmap)this.mObj1);
            }
            final ColorStateList mTintList = this.mTintList;
            if (mTintList != null) {
                icon.setTintList(mTintList);
            }
            final PorterDuff$Mode mTintMode = this.mTintMode;
            if (mTintMode != IconCompat.DEFAULT_TINT_MODE) {
                icon.setTintMode(mTintMode);
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
        final int mType = this.mType;
        Label_0235: {
            if (mType != 1) {
                if (mType == 2) {
                    sb.append(" pkg=");
                    sb.append(this.getResPackage());
                    sb.append(" id=");
                    sb.append(String.format("0x%08x", this.getResId()));
                    break Label_0235;
                }
                if (mType != 3) {
                    if (mType == 4) {
                        sb.append(" uri=");
                        sb.append(this.mObj1);
                        break Label_0235;
                    }
                    if (mType != 5) {
                        break Label_0235;
                    }
                }
                else {
                    sb.append(" len=");
                    sb.append(this.mInt1);
                    if (this.mInt2 != 0) {
                        sb.append(" off=");
                        sb.append(this.mInt2);
                    }
                    break Label_0235;
                }
            }
            sb.append(" size=");
            sb.append(((Bitmap)this.mObj1).getWidth());
            sb.append("x");
            sb.append(((Bitmap)this.mObj1).getHeight());
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
