// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.print;

import android.os.CancellationSignal$OnCancelListener;
import android.print.PrintDocumentInfo$Builder;
import android.os.Bundle;
import android.print.PrintDocumentAdapter$LayoutResultCallback;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes$MediaSize;
import android.print.PrintManager;
import android.print.PrintAttributes$Builder;
import android.print.PageRange;
import android.os.AsyncTask;
import android.print.PrintAttributes$Margins;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.print.PrintDocumentAdapter$WriteResultCallback;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PrintAttributes;
import android.graphics.BitmapFactory$Options;
import android.support.annotation.RequiresApi;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Build$VERSION;
import android.content.Context;

public final class PrintHelper
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    private final PrintHelperVersionImpl mImpl;
    
    public PrintHelper(final Context context) {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi24(context);
        }
        else if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi23(context);
        }
        else if (Build$VERSION.SDK_INT >= 20) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi20(context);
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi19(context);
        }
        else {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperStub();
        }
    }
    
    public static boolean systemSupportsPrint() {
        return Build$VERSION.SDK_INT >= 19;
    }
    
    public int getColorMode() {
        return this.mImpl.getColorMode();
    }
    
    public int getOrientation() {
        return this.mImpl.getOrientation();
    }
    
    public int getScaleMode() {
        return this.mImpl.getScaleMode();
    }
    
    public void printBitmap(final String s, final Bitmap bitmap) {
        this.mImpl.printBitmap(s, bitmap, null);
    }
    
    public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        this.mImpl.printBitmap(s, bitmap, onPrintFinishCallback);
    }
    
    public void printBitmap(final String s, final Uri uri) throws FileNotFoundException {
        this.mImpl.printBitmap(s, uri, null);
    }
    
    public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        this.mImpl.printBitmap(s, uri, onPrintFinishCallback);
    }
    
    public void setColorMode(final int colorMode) {
        this.mImpl.setColorMode(colorMode);
    }
    
    public void setOrientation(final int orientation) {
        this.mImpl.setOrientation(orientation);
    }
    
    public void setScaleMode(final int scaleMode) {
        this.mImpl.setScaleMode(scaleMode);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface ColorMode {
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface Orientation {
    }
    
    @RequiresApi(19)
    private static class PrintHelperApi19 implements PrintHelperVersionImpl
    {
        private static final String LOG_TAG = "PrintHelperApi19";
        private static final int MAX_PRINT_SIZE = 3500;
        int mColorMode;
        final Context mContext;
        BitmapFactory$Options mDecodeOptions;
        protected boolean mIsMinMarginsHandlingCorrect;
        private final Object mLock;
        int mOrientation;
        protected boolean mPrintActivityRespectsOrientation;
        int mScaleMode;
        
        PrintHelperApi19(final Context mContext) {
            this.mDecodeOptions = null;
            this.mLock = new Object();
            this.mScaleMode = 2;
            this.mColorMode = 2;
            this.mPrintActivityRespectsOrientation = true;
            this.mIsMinMarginsHandlingCorrect = true;
            this.mContext = mContext;
        }
        
        private Bitmap convertBitmapForColorMode(final Bitmap bitmap, final int n) {
            if (n != 1) {
                return bitmap;
            }
            final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap2);
            final Paint paint = new Paint();
            final ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            canvas.setBitmap((Bitmap)null);
            return bitmap2;
        }
        
        private Matrix getMatrix(final int n, final int n2, final RectF rectF, final int n3) {
            final Matrix matrix = new Matrix();
            final float width = rectF.width();
            final float n4 = (float)n;
            final float n5 = width / n4;
            float n6;
            if (n3 == 2) {
                n6 = Math.max(n5, rectF.height() / n2);
            }
            else {
                n6 = Math.min(n5, rectF.height() / n2);
            }
            matrix.postScale(n6, n6);
            matrix.postTranslate((rectF.width() - n4 * n6) / 2.0f, (rectF.height() - n2 * n6) / 2.0f);
            return matrix;
        }
        
        private static boolean isPortrait(final Bitmap bitmap) {
            return bitmap.getWidth() <= bitmap.getHeight();
        }
        
        private Bitmap loadBitmap(Uri decodeStream, final BitmapFactory$Options bitmapFactory$Options) throws FileNotFoundException {
            if (decodeStream == null || this.mContext == null) {
                throw new IllegalArgumentException("bad argument to loadBitmap");
            }
            final InputStream inputStream = null;
            InputStream inputStream2;
            try {
                final InputStream openInputStream = this.mContext.getContentResolver().openInputStream(decodeStream);
                try {
                    decodeStream = (Uri)BitmapFactory.decodeStream(openInputStream, (Rect)null, bitmapFactory$Options);
                    if (openInputStream != null) {
                        try {
                            openInputStream.close();
                        }
                        catch (IOException ex) {
                            Log.w("PrintHelperApi19", "close fail ", (Throwable)ex);
                        }
                    }
                    return (Bitmap)decodeStream;
                }
                finally {}
            }
            finally {
                inputStream2 = inputStream;
            }
            if (inputStream2 != null) {
                try {
                    inputStream2.close();
                }
                catch (IOException ex2) {
                    Log.w("PrintHelperApi19", "close fail ", (Throwable)ex2);
                }
            }
        }
        
        private Bitmap loadConstrainedBitmap(final Uri uri) throws FileNotFoundException {
            if (uri == null || this.mContext == null) {
                throw new IllegalArgumentException("bad argument to getScaledBitmap");
            }
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            this.loadBitmap(uri, bitmapFactory$Options);
            final int outWidth = bitmapFactory$Options.outWidth;
            final int outHeight = bitmapFactory$Options.outHeight;
            if (outWidth > 0 && outHeight > 0) {
                int i;
                int inSampleSize;
                for (i = Math.max(outWidth, outHeight), inSampleSize = 1; i > 3500; i >>>= 1, inSampleSize <<= 1) {}
                if (inSampleSize > 0) {
                    if (Math.min(outWidth, outHeight) / inSampleSize > 0) {
                        final Object mLock = this.mLock;
                        synchronized (mLock) {
                            this.mDecodeOptions = new BitmapFactory$Options();
                            this.mDecodeOptions.inMutable = true;
                            this.mDecodeOptions.inSampleSize = inSampleSize;
                            final BitmapFactory$Options mDecodeOptions = this.mDecodeOptions;
                            // monitorexit(mLock)
                            try {
                                final Bitmap loadBitmap = this.loadBitmap(uri, mDecodeOptions);
                                synchronized (this.mLock) {
                                    this.mDecodeOptions = null;
                                    return loadBitmap;
                                }
                            }
                            finally {
                                synchronized (this.mLock) {
                                    this.mDecodeOptions = null;
                                }
                                // monitorexit(this.mLock)
                            }
                        }
                    }
                }
                return null;
            }
            return null;
        }
        
        private void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
            PrintAttributes build;
            if (this.mIsMinMarginsHandlingCorrect) {
                build = printAttributes;
            }
            else {
                build = this.copyAttributes(printAttributes).setMinMargins(new PrintAttributes$Margins(0, 0, 0, 0)).build();
            }
            new AsyncTask<Void, Void, Throwable>() {
                protected Throwable doInBackground(final Void... p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                    //     4: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                    //     7: ifeq            12
                    //    10: aconst_null    
                    //    11: areturn        
                    //    12: new             Landroid/print/pdf/PrintedPdfDocument;
                    //    15: astore_2       
                    //    16: aload_2        
                    //    17: aload_0        
                    //    18: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.this$0:Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
                    //    21: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19.mContext:Landroid/content/Context;
                    //    24: aload_0        
                    //    25: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$pdfAttributes:Landroid/print/PrintAttributes;
                    //    28: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                    //    31: aload_0        
                    //    32: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.this$0:Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
                    //    35: aload_0        
                    //    36: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$bitmap:Landroid/graphics/Bitmap;
                    //    39: aload_0        
                    //    40: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$pdfAttributes:Landroid/print/PrintAttributes;
                    //    43: invokevirtual   android/print/PrintAttributes.getColorMode:()I
                    //    46: invokestatic    android/support/v4/print/PrintHelper$PrintHelperApi19.access$100:(Landroid/support/v4/print/PrintHelper$PrintHelperApi19;Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
                    //    49: astore_3       
                    //    50: aload_0        
                    //    51: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                    //    54: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                    //    57: istore          4
                    //    59: iload           4
                    //    61: ifeq            66
                    //    64: aconst_null    
                    //    65: areturn        
                    //    66: aload_2        
                    //    67: iconst_1       
                    //    68: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                    //    71: astore          5
                    //    73: aload_0        
                    //    74: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.this$0:Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
                    //    77: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19.mIsMinMarginsHandlingCorrect:Z
                    //    80: ifeq            102
                    //    83: new             Landroid/graphics/RectF;
                    //    86: astore_1       
                    //    87: aload_1        
                    //    88: aload           5
                    //    90: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                    //    93: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                    //    96: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                    //    99: goto            159
                    //   102: new             Landroid/print/pdf/PrintedPdfDocument;
                    //   105: astore          6
                    //   107: aload           6
                    //   109: aload_0        
                    //   110: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.this$0:Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
                    //   113: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19.mContext:Landroid/content/Context;
                    //   116: aload_0        
                    //   117: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$attributes:Landroid/print/PrintAttributes;
                    //   120: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                    //   123: aload           6
                    //   125: iconst_1       
                    //   126: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                    //   129: astore          7
                    //   131: new             Landroid/graphics/RectF;
                    //   134: astore_1       
                    //   135: aload_1        
                    //   136: aload           7
                    //   138: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                    //   141: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                    //   144: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                    //   147: aload           6
                    //   149: aload           7
                    //   151: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                    //   154: aload           6
                    //   156: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                    //   159: aload_0        
                    //   160: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.this$0:Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
                    //   163: aload_3        
                    //   164: invokevirtual   android/graphics/Bitmap.getWidth:()I
                    //   167: aload_3        
                    //   168: invokevirtual   android/graphics/Bitmap.getHeight:()I
                    //   171: aload_1        
                    //   172: aload_0        
                    //   173: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fittingMode:I
                    //   176: invokestatic    android/support/v4/print/PrintHelper$PrintHelperApi19.access$200:(Landroid/support/v4/print/PrintHelper$PrintHelperApi19;IILandroid/graphics/RectF;I)Landroid/graphics/Matrix;
                    //   179: astore          7
                    //   181: aload_0        
                    //   182: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.this$0:Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
                    //   185: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19.mIsMinMarginsHandlingCorrect:Z
                    //   188: ifeq            194
                    //   191: goto            218
                    //   194: aload           7
                    //   196: aload_1        
                    //   197: getfield        android/graphics/RectF.left:F
                    //   200: aload_1        
                    //   201: getfield        android/graphics/RectF.top:F
                    //   204: invokevirtual   android/graphics/Matrix.postTranslate:(FF)Z
                    //   207: pop            
                    //   208: aload           5
                    //   210: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                    //   213: aload_1        
                    //   214: invokevirtual   android/graphics/Canvas.clipRect:(Landroid/graphics/RectF;)Z
                    //   217: pop            
                    //   218: aload           5
                    //   220: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                    //   223: aload_3        
                    //   224: aload           7
                    //   226: aconst_null    
                    //   227: invokevirtual   android/graphics/Canvas.drawBitmap:(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
                    //   230: aload_2        
                    //   231: aload           5
                    //   233: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                    //   236: aload_0        
                    //   237: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                    //   240: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                    //   243: istore          4
                    //   245: iload           4
                    //   247: ifeq            284
                    //   250: aload_2        
                    //   251: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                    //   254: aload_0        
                    //   255: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                    //   258: astore_1       
                    //   259: aload_1        
                    //   260: ifnull          270
                    //   263: aload_0        
                    //   264: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                    //   267: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                    //   270: aload_3        
                    //   271: aload_0        
                    //   272: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$bitmap:Landroid/graphics/Bitmap;
                    //   275: if_acmpeq       282
                    //   278: aload_3        
                    //   279: invokevirtual   android/graphics/Bitmap.recycle:()V
                    //   282: aconst_null    
                    //   283: areturn        
                    //   284: new             Ljava/io/FileOutputStream;
                    //   287: astore_1       
                    //   288: aload_1        
                    //   289: aload_0        
                    //   290: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                    //   293: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
                    //   296: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/FileDescriptor;)V
                    //   299: aload_2        
                    //   300: aload_1        
                    //   301: invokevirtual   android/print/pdf/PrintedPdfDocument.writeTo:(Ljava/io/OutputStream;)V
                    //   304: aload_2        
                    //   305: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                    //   308: aload_0        
                    //   309: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                    //   312: astore_1       
                    //   313: aload_1        
                    //   314: ifnull          324
                    //   317: aload_0        
                    //   318: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                    //   321: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                    //   324: aload_3        
                    //   325: aload_0        
                    //   326: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$bitmap:Landroid/graphics/Bitmap;
                    //   329: if_acmpeq       336
                    //   332: aload_3        
                    //   333: invokevirtual   android/graphics/Bitmap.recycle:()V
                    //   336: aconst_null    
                    //   337: areturn        
                    //   338: astore_1       
                    //   339: aload_2        
                    //   340: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                    //   343: aload_0        
                    //   344: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                    //   347: astore_2       
                    //   348: aload_2        
                    //   349: ifnull          359
                    //   352: aload_0        
                    //   353: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                    //   356: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                    //   359: aload_3        
                    //   360: aload_0        
                    //   361: getfield        android/support/v4/print/PrintHelper$PrintHelperApi19$2.val$bitmap:Landroid/graphics/Bitmap;
                    //   364: if_acmpeq       371
                    //   367: aload_3        
                    //   368: invokevirtual   android/graphics/Bitmap.recycle:()V
                    //   371: aload_1        
                    //   372: athrow         
                    //   373: astore_1       
                    //   374: aload_1        
                    //   375: areturn        
                    //   376: astore_1       
                    //   377: goto            270
                    //   380: astore_1       
                    //   381: goto            324
                    //   384: astore_2       
                    //   385: goto            359
                    //    Exceptions:
                    //  Try           Handler
                    //  Start  End    Start  End    Type                 
                    //  -----  -----  -----  -----  ---------------------
                    //  0      10     373    376    Ljava/lang/Throwable;
                    //  12     59     373    376    Ljava/lang/Throwable;
                    //  66     99     338    373    Any
                    //  102    159    338    373    Any
                    //  159    191    338    373    Any
                    //  194    218    338    373    Any
                    //  218    245    338    373    Any
                    //  250    259    373    376    Ljava/lang/Throwable;
                    //  263    270    376    380    Ljava/io/IOException;
                    //  263    270    373    376    Ljava/lang/Throwable;
                    //  270    282    373    376    Ljava/lang/Throwable;
                    //  284    304    338    373    Any
                    //  304    313    373    376    Ljava/lang/Throwable;
                    //  317    324    380    384    Ljava/io/IOException;
                    //  317    324    373    376    Ljava/lang/Throwable;
                    //  324    336    373    376    Ljava/lang/Throwable;
                    //  339    348    373    376    Ljava/lang/Throwable;
                    //  352    359    384    388    Ljava/io/IOException;
                    //  352    359    373    376    Ljava/lang/Throwable;
                    //  359    371    373    376    Ljava/lang/Throwable;
                    //  371    373    373    376    Ljava/lang/Throwable;
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.IndexOutOfBoundsException: Index 190 out-of-bounds for length 190
                    //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
                    //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
                    //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
                    //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
                    //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
                    //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
                    //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                    //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                    //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                    //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                    //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                    //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                protected void onPostExecute(final Throwable t) {
                    if (cancellationSignal.isCanceled()) {
                        printDocumentAdapter$WriteResultCallback.onWriteCancelled();
                    }
                    else if (t == null) {
                        printDocumentAdapter$WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                    }
                    else {
                        Log.e("PrintHelperApi19", "Error writing printed content", t);
                        printDocumentAdapter$WriteResultCallback.onWriteFailed((CharSequence)null);
                    }
                }
            }.execute((Object[])new Void[0]);
        }
        
        protected PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
            final PrintAttributes$Builder setMinMargins = new PrintAttributes$Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
            if (printAttributes.getColorMode() != 0) {
                setMinMargins.setColorMode(printAttributes.getColorMode());
            }
            return setMinMargins;
        }
        
        @Override
        public int getColorMode() {
            return this.mColorMode;
        }
        
        @Override
        public int getOrientation() {
            if (this.mOrientation == 0) {
                return 1;
            }
            return this.mOrientation;
        }
        
        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
            if (bitmap == null) {
                return;
            }
            final int mScaleMode = this.mScaleMode;
            final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes$MediaSize mediaSize;
            if (isPortrait(bitmap)) {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
            }
            else {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
            }
            printManager.print(s, (PrintDocumentAdapter)new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;
                
                public void onFinish() {
                    if (onPrintFinishCallback != null) {
                        onPrintFinishCallback.onFinish();
                    }
                }
                
                public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                    this.mAttributes = mAttributes;
                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), mAttributes.equals((Object)printAttributes) ^ true);
                }
                
                public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                    PrintHelperApi19.this.writeBitmap(this.mAttributes, mScaleMode, bitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
                }
            }, new PrintAttributes$Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
            final PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;
                Bitmap mBitmap = null;
                AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
                final /* synthetic */ int val$fittingMode = PrintHelperApi19.this.mScaleMode;
                
                private void cancelLoad() {
                    synchronized (PrintHelperApi19.this.mLock) {
                        if (PrintHelperApi19.this.mDecodeOptions != null) {
                            PrintHelperApi19.this.mDecodeOptions.requestCancelDecode();
                            PrintHelperApi19.this.mDecodeOptions = null;
                        }
                    }
                }
                
                public void onFinish() {
                    super.onFinish();
                    this.cancelLoad();
                    if (this.mLoadBitmap != null) {
                        this.mLoadBitmap.cancel(true);
                    }
                    if (onPrintFinishCallback != null) {
                        onPrintFinishCallback.onFinish();
                    }
                    if (this.mBitmap != null) {
                        this.mBitmap.recycle();
                        this.mBitmap = null;
                    }
                }
                
                public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                    synchronized (this) {
                        this.mAttributes = mAttributes;
                        // monitorexit(this)
                        if (cancellationSignal.isCanceled()) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                            return;
                        }
                        if (this.mBitmap != null) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), mAttributes.equals((Object)printAttributes) ^ true);
                            return;
                        }
                        this.mLoadBitmap = (AsyncTask<Uri, Boolean, Bitmap>)new AsyncTask<Uri, Boolean, Bitmap>() {
                            protected Bitmap doInBackground(final Uri... array) {
                                try {
                                    return PrintHelperApi19.this.loadConstrainedBitmap(uri);
                                }
                                catch (FileNotFoundException ex) {
                                    return null;
                                }
                            }
                            
                            protected void onCancelled(final Bitmap bitmap) {
                                printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                                PrintDocumentAdapter.this.mLoadBitmap = null;
                            }
                            
                            protected void onPostExecute(final Bitmap bitmap) {
                                super.onPostExecute((Object)bitmap);
                                Object bitmap2 = bitmap;
                                Label_0113: {
                                    if (bitmap != null) {
                                        if (PrintHelperApi19.this.mPrintActivityRespectsOrientation) {
                                            bitmap2 = bitmap;
                                            if (PrintHelperApi19.this.mOrientation != 0) {
                                                break Label_0113;
                                            }
                                        }
                                        synchronized (this) {
                                            final PrintAttributes$MediaSize mediaSize = PrintDocumentAdapter.this.mAttributes.getMediaSize();
                                            // monitorexit(this)
                                            bitmap2 = bitmap;
                                            if (mediaSize != null) {
                                                bitmap2 = bitmap;
                                                if (mediaSize.isPortrait() != isPortrait(bitmap)) {
                                                    bitmap2 = new Matrix();
                                                    ((Matrix)bitmap2).postRotate(90.0f);
                                                    bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), (Matrix)bitmap2, true);
                                                }
                                            }
                                        }
                                    }
                                }
                                if ((PrintDocumentAdapter.this.mBitmap = (Bitmap)bitmap2) != null) {
                                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                                }
                                else {
                                    printDocumentAdapter$LayoutResultCallback.onLayoutFailed((CharSequence)null);
                                }
                                PrintDocumentAdapter.this.mLoadBitmap = null;
                            }
                            
                            protected void onPreExecute() {
                                cancellationSignal.setOnCancelListener((CancellationSignal$OnCancelListener)new CancellationSignal$OnCancelListener() {
                                    public void onCancel() {
                                        PrintHelper$PrintHelperApi19$3.this.cancelLoad();
                                        AsyncTask.this.cancel(false);
                                    }
                                });
                            }
                        }.execute((Object[])new Uri[0]);
                    }
                }
                
                public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                    PrintHelperApi19.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
                }
            };
            final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            final PrintAttributes$Builder printAttributes$Builder = new PrintAttributes$Builder();
            printAttributes$Builder.setColorMode(this.mColorMode);
            if (this.mOrientation != 1 && this.mOrientation != 0) {
                if (this.mOrientation == 2) {
                    printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_PORTRAIT);
                }
            }
            else {
                printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE);
            }
            printManager.print(s, (PrintDocumentAdapter)printDocumentAdapter, printAttributes$Builder.build());
        }
        
        @Override
        public void setColorMode(final int mColorMode) {
            this.mColorMode = mColorMode;
        }
        
        @Override
        public void setOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void setScaleMode(final int mScaleMode) {
            this.mScaleMode = mScaleMode;
        }
    }
    
    @RequiresApi(20)
    private static class PrintHelperApi20 extends PrintHelperApi19
    {
        PrintHelperApi20(final Context context) {
            super(context);
            this.mPrintActivityRespectsOrientation = false;
        }
    }
    
    @RequiresApi(23)
    private static class PrintHelperApi23 extends PrintHelperApi20
    {
        PrintHelperApi23(final Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = false;
        }
        
        @Override
        protected PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
            final PrintAttributes$Builder copyAttributes = super.copyAttributes(printAttributes);
            if (printAttributes.getDuplexMode() != 0) {
                copyAttributes.setDuplexMode(printAttributes.getDuplexMode());
            }
            return copyAttributes;
        }
    }
    
    @RequiresApi(24)
    private static class PrintHelperApi24 extends PrintHelperApi23
    {
        PrintHelperApi24(final Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = true;
            this.mPrintActivityRespectsOrientation = true;
        }
    }
    
    private static final class PrintHelperStub implements PrintHelperVersionImpl
    {
        int mColorMode;
        int mOrientation;
        int mScaleMode;
        
        private PrintHelperStub() {
            this.mScaleMode = 2;
            this.mColorMode = 2;
            this.mOrientation = 1;
        }
        
        @Override
        public int getColorMode() {
            return this.mColorMode;
        }
        
        @Override
        public int getOrientation() {
            return this.mOrientation;
        }
        
        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) {
        }
        
        @Override
        public void setColorMode(final int mColorMode) {
            this.mColorMode = mColorMode;
        }
        
        @Override
        public void setOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void setScaleMode(final int mScaleMode) {
            this.mScaleMode = mScaleMode;
        }
    }
    
    interface PrintHelperVersionImpl
    {
        int getColorMode();
        
        int getOrientation();
        
        int getScaleMode();
        
        void printBitmap(final String p0, final Bitmap p1, final OnPrintFinishCallback p2);
        
        void printBitmap(final String p0, final Uri p1, final OnPrintFinishCallback p2) throws FileNotFoundException;
        
        void setColorMode(final int p0);
        
        void setOrientation(final int p0);
        
        void setScaleMode(final int p0);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface ScaleMode {
    }
}
