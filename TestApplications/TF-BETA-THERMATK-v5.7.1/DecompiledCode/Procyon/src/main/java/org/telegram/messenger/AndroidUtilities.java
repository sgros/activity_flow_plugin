// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.util.Calendar;
import android.text.Selection;
import android.view.MotionEvent;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import java.util.List;
import android.view.View$OnClickListener;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import android.widget.LinearLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.TextView;
import android.widget.LinearLayout;
import org.telegram.ui.ActionBar.BottomSheet;
import android.os.PowerManager;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.viewpager.widget.ViewPager;
import android.widget.ScrollView;
import android.widget.EdgeEffect;
import android.widget.HorizontalScrollView;
import android.graphics.Matrix;
import android.view.ViewGroup;
import org.telegram.ui.Components.TypefaceSpan;
import androidx.core.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.ForegroundDetector;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.SharedPreferences$Editor;
import org.telegram.tgnet.ConnectionsManager;
import android.content.DialogInterface;
import org.telegram.ui.ActionBar.BaseFragment;
import android.provider.Settings$Global;
import android.provider.Settings$System;
import org.telegram.ui.WallpapersListActivity;
import org.telegram.tgnet.TLRPC;
import android.graphics.Rect;
import android.graphics.Typeface$Builder;
import android.provider.MediaStore$Images$Media;
import android.provider.MediaStore$Video$Media;
import android.provider.MediaStore$Audio$Media;
import android.content.ContentUris;
import android.provider.DocumentsContract;
import android.view.inputmethod.InputMethodSubtype;
import android.view.inputmethod.InputMethodManager;
import android.os.Environment;
import android.text.style.ForegroundColorSpan;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.lang.reflect.Method;
import com.android.internal.telephony.ITelephony;
import android.telephony.TelephonyManager;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import android.text.SpannedString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.annotation.SuppressLint;
import android.util.StateSet;
import android.widget.ListView;
import android.view.View;
import org.telegram.PhoneFormat.PhoneFormat;
import android.text.TextUtils;
import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.ClipData;
import android.content.ClipboardManager;
import java.io.File;
import android.content.Intent;
import android.net.Uri;
import android.os.Build$VERSION;
import android.content.res.Configuration;
import android.graphics.Typeface;
import java.util.Hashtable;
import android.graphics.Paint;
import android.view.animation.OvershootInterpolator;
import java.lang.reflect.Field;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.animation.DecelerateInterpolator;
import android.graphics.RectF;
import android.view.animation.AccelerateInterpolator;
import java.util.regex.Pattern;

public class AndroidUtilities
{
    public static final int FLAG_TAG_ALL = 11;
    public static final int FLAG_TAG_BOLD = 2;
    public static final int FLAG_TAG_BR = 1;
    public static final int FLAG_TAG_COLOR = 4;
    public static final int FLAG_TAG_URL = 8;
    public static Pattern WEB_URL;
    public static AccelerateInterpolator accelerateInterpolator;
    private static int adjustOwnerClassGuid;
    private static RectF bitmapRect;
    private static final Object callLock;
    private static CallReceiver callReceiver;
    public static DecelerateInterpolator decelerateInterpolator;
    public static float density;
    public static DisplayMetrics displayMetrics;
    public static Point displaySize;
    private static int[] documentIcons;
    private static int[] documentMediaIcons;
    public static boolean firstConfigurationWas;
    private static boolean hasCallPermissions;
    public static boolean incorrectDisplaySizeFix;
    public static boolean isInMultiwindow;
    private static Boolean isTablet;
    public static int leftBaseline;
    private static Field mAttachInfoField;
    private static Field mStableInsetsField;
    public static OvershootInterpolator overshootInterpolator;
    public static Integer photoSize;
    private static int prevOrientation;
    public static int roundMessageSize;
    private static Paint roundPaint;
    private static final Object smsLock;
    public static int statusBarHeight;
    private static final Hashtable<String, Typeface> typefaceCache;
    public static boolean usingHardwareInput;
    private static boolean waitingForCall;
    private static boolean waitingForSms;
    
    static {
        typefaceCache = new Hashtable<String, Typeface>();
        AndroidUtilities.prevOrientation = -10;
        boolean hasCallPermissions = false;
        AndroidUtilities.waitingForSms = false;
        AndroidUtilities.waitingForCall = false;
        smsLock = new Object();
        callLock = new Object();
        AndroidUtilities.statusBarHeight = 0;
        AndroidUtilities.density = 1.0f;
        AndroidUtilities.displaySize = new Point();
        AndroidUtilities.photoSize = null;
        AndroidUtilities.displayMetrics = new DisplayMetrics();
        AndroidUtilities.decelerateInterpolator = new DecelerateInterpolator();
        AndroidUtilities.accelerateInterpolator = new AccelerateInterpolator();
        AndroidUtilities.overshootInterpolator = new OvershootInterpolator();
        AndroidUtilities.isTablet = null;
        AndroidUtilities.adjustOwnerClassGuid = 0;
        AndroidUtilities.WEB_URL = null;
        try {
            final Pattern compile = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))");
            final StringBuilder sb = new StringBuilder();
            sb.append("(([a-zA-Z0-9 -\ud7ff\uf900-\ufdcf\ufdf0-\uffef]([a-zA-Z0-9 -\ud7ff\uf900-\ufdcf\ufdf0-\uffef\\-]{0,61}[a-zA-Z0-9 -\ud7ff\uf900-\ufdcf\ufdf0-\uffef]){0,1}\\.)+[a-zA-Z -\ud7ff\uf900-\ufdcf\ufdf0-\uffef]{2,63}|");
            sb.append(compile);
            sb.append(")");
            final Pattern compile2 = Pattern.compile(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?(?:");
            sb2.append(compile2);
            sb2.append(")(?:\\:\\d{1,5})?)(\\/(?:(?:[");
            sb2.append("a-zA-Z0-9 -\ud7ff\uf900-\ufdcf\ufdf0-\uffef");
            sb2.append("\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)");
            AndroidUtilities.WEB_URL = Pattern.compile(sb2.toString());
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        int leftBaseline;
        if (isTablet()) {
            leftBaseline = 80;
        }
        else {
            leftBaseline = 72;
        }
        AndroidUtilities.leftBaseline = leftBaseline;
        checkDisplaySize(ApplicationLoader.applicationContext, null);
        AndroidUtilities.documentIcons = new int[] { 2131165554, 2131165556, 2131165560, 2131165562 };
        AndroidUtilities.documentMediaIcons = new int[] { 2131165555, 2131165557, 2131165561, 2131165563 };
        if (Build$VERSION.SDK_INT >= 23) {
            hasCallPermissions = true;
        }
        AndroidUtilities.hasCallPermissions = hasCallPermissions;
    }
    
    public static int HSBtoRGB(float n, float n2, final float n3) {
        int n4 = 0;
        int n6;
        int n5;
        if (n2 == 0.0f) {
            n4 = (int)(n3 * 255.0f + 0.5f);
            n5 = (n6 = n4);
        }
        else {
            n = (n - (float)Math.floor(n)) * 6.0f;
            final float n7 = n - (float)Math.floor(n);
            final float n8 = (1.0f - n2) * n3;
            final float n9 = (1.0f - n2 * n7) * n3;
            n2 = (1.0f - n2 * (1.0f - n7)) * n3;
            final int n10 = (int)n;
            if (n10 != 0) {
                if (n10 != 1) {
                    if (n10 != 2) {
                        if (n10 != 3) {
                            if (n10 != 4) {
                                if (n10 != 5) {
                                    n5 = 0;
                                    n6 = 0;
                                    return (n5 & 0xFF) << 8 | (0xFF000000 | (n4 & 0xFF) << 16) | (n6 & 0xFF);
                                }
                                n4 = (int)(n3 * 255.0f + 0.5f);
                                n5 = (int)(n8 * 255.0f + 0.5f);
                                n6 = (int)(n9 * 255.0f + 0.5f);
                                return (n5 & 0xFF) << 8 | (0xFF000000 | (n4 & 0xFF) << 16) | (n6 & 0xFF);
                            }
                            else {
                                n4 = (int)(n2 * 255.0f + 0.5f);
                                n5 = (int)(n8 * 255.0f + 0.5f);
                            }
                        }
                        else {
                            n4 = (int)(n8 * 255.0f + 0.5f);
                            n5 = (int)(n9 * 255.0f + 0.5f);
                        }
                        n6 = (int)(n3 * 255.0f + 0.5f);
                        return (n5 & 0xFF) << 8 | (0xFF000000 | (n4 & 0xFF) << 16) | (n6 & 0xFF);
                    }
                    n4 = (int)(n8 * 255.0f + 0.5f);
                    n5 = (int)(n3 * 255.0f + 0.5f);
                    n6 = (int)(n2 * 255.0f + 0.5f);
                    return (n5 & 0xFF) << 8 | (0xFF000000 | (n4 & 0xFF) << 16) | (n6 & 0xFF);
                }
                else {
                    n4 = (int)(n9 * 255.0f + 0.5f);
                    n5 = (int)(n3 * 255.0f + 0.5f);
                }
            }
            else {
                n4 = (int)(n3 * 255.0f + 0.5f);
                n5 = (int)(n2 * 255.0f + 0.5f);
            }
            n6 = (int)(n8 * 255.0f + 0.5f);
        }
        return (n5 & 0xFF) << 8 | (0xFF000000 | (n4 & 0xFF) << 16) | (n6 & 0xFF);
    }
    
    public static float[] RGBtoHSB(final int n, final int n2, final int n3) {
        int n4;
        if (n > n2) {
            n4 = n;
        }
        else {
            n4 = n2;
        }
        int n5 = n4;
        if (n3 > n4) {
            n5 = n3;
        }
        int n6;
        if (n < n2) {
            n6 = n;
        }
        else {
            n6 = n2;
        }
        int n7 = n6;
        if (n3 < n6) {
            n7 = n3;
        }
        final float n8 = (float)n5;
        final float n9 = n8 / 255.0f;
        float n10 = 0.0f;
        float n11;
        if (n5 != 0) {
            n11 = (n5 - n7) / n8;
        }
        else {
            n11 = 0.0f;
        }
        if (n11 != 0.0f) {
            final float n12 = (float)(n5 - n);
            final float n13 = (float)(n5 - n7);
            final float n14 = n12 / n13;
            final float n15 = (n5 - n2) / n13;
            final float n16 = (n5 - n3) / n13;
            float n17;
            if (n == n5) {
                n17 = n16 - n15;
            }
            else if (n2 == n5) {
                n17 = n14 + 2.0f - n16;
            }
            else {
                n17 = n15 + 4.0f - n14;
            }
            n10 = n17 / 6.0f;
            if (n10 < 0.0f) {
                ++n10;
            }
        }
        return new float[] { n10, n11, n9 };
    }
    
    public static void addMediaToGallery(final Uri data) {
        if (data == null) {
            return;
        }
        try {
            final Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(data);
            ApplicationLoader.applicationContext.sendBroadcast(intent);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void addMediaToGallery(final String pathname) {
        if (pathname == null) {
            return;
        }
        addMediaToGallery(Uri.fromFile(new File(pathname)));
    }
    
    public static void addToClipboard(final CharSequence charSequence) {
        try {
            ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText((CharSequence)"label", charSequence));
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static byte[] calcAuthKeyHash(byte[] computeSHA1) {
        computeSHA1 = Utilities.computeSHA1(computeSHA1);
        final byte[] array = new byte[16];
        System.arraycopy(computeSHA1, 0, array, 0, 16);
        return array;
    }
    
    public static int[] calcDrawableColor(final Drawable drawable) {
        int n2;
        final int n = n2 = -16777216;
        int n3;
        try {
            if (drawable instanceof BitmapDrawable) {
                n2 = n;
                final Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                n3 = n;
                if (bitmap != null) {
                    n2 = n;
                    final Bitmap scaledBitmap = Bitmaps.createScaledBitmap(bitmap, 1, 1, true);
                    n3 = n;
                    if (scaledBitmap != null) {
                        n2 = n;
                        final int n4 = n3 = scaledBitmap.getPixel(0, 0);
                        if (bitmap != scaledBitmap) {
                            n2 = n4;
                            scaledBitmap.recycle();
                            n3 = n4;
                        }
                    }
                }
            }
            else {
                n2 = n;
                n3 = n;
                if (drawable instanceof ColorDrawable) {
                    n2 = n;
                    n3 = ((ColorDrawable)drawable).getColor();
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            n3 = n2;
        }
        final double[] rgbToHsv = rgbToHsv(n3 >> 16 & 0xFF, n3 >> 8 & 0xFF, n3 & 0xFF);
        rgbToHsv[1] = Math.min(1.0, rgbToHsv[1] + 0.05 + (1.0 - rgbToHsv[1]) * 0.1);
        final int[] hsvToRgb = hsvToRgb(rgbToHsv[0], rgbToHsv[1], Math.max(0.0, rgbToHsv[2] * 0.65));
        final int argb = Color.argb(102, hsvToRgb[0], hsvToRgb[1], hsvToRgb[2]);
        final int argb2 = Color.argb(136, hsvToRgb[0], hsvToRgb[1], hsvToRgb[2]);
        final int[] hsvToRgb2 = hsvToRgb(rgbToHsv[0], rgbToHsv[1], Math.max(0.0, rgbToHsv[2] * 0.72));
        return new int[] { argb, argb2, Color.argb(102, hsvToRgb2[0], hsvToRgb2[1], hsvToRgb2[2]), Color.argb(136, hsvToRgb2[0], hsvToRgb2[1], hsvToRgb2[2]) };
    }
    
    public static void cancelRunOnUIThread(final Runnable runnable) {
        ApplicationLoader.applicationHandler.removeCallbacks(runnable);
    }
    
    public static void checkDisplaySize(final Context context, final Configuration configuration) {
        try {
            final int n = (int)AndroidUtilities.density;
            AndroidUtilities.density = context.getResources().getDisplayMetrics().density;
            final int n2 = (int)AndroidUtilities.density;
            if (AndroidUtilities.firstConfigurationWas && n != n2) {
                Theme.reloadAllResources(context);
            }
            boolean usingHardwareInput = true;
            AndroidUtilities.firstConfigurationWas = true;
            Configuration configuration2;
            if ((configuration2 = configuration) == null) {
                configuration2 = context.getResources().getConfiguration();
            }
            if (configuration2.keyboard == 1 || configuration2.hardKeyboardHidden != 1) {
                usingHardwareInput = false;
            }
            AndroidUtilities.usingHardwareInput = usingHardwareInput;
            final WindowManager windowManager = (WindowManager)context.getSystemService("window");
            if (windowManager != null) {
                final Display defaultDisplay = windowManager.getDefaultDisplay();
                if (defaultDisplay != null) {
                    defaultDisplay.getMetrics(AndroidUtilities.displayMetrics);
                    defaultDisplay.getSize(AndroidUtilities.displaySize);
                }
            }
            if (configuration2.screenWidthDp != 0) {
                final int x = (int)Math.ceil(configuration2.screenWidthDp * AndroidUtilities.density);
                if (Math.abs(AndroidUtilities.displaySize.x - x) > 3) {
                    AndroidUtilities.displaySize.x = x;
                }
            }
            if (configuration2.screenHeightDp != 0) {
                final int y = (int)Math.ceil(configuration2.screenHeightDp * AndroidUtilities.density);
                if (Math.abs(AndroidUtilities.displaySize.y - y) > 3) {
                    AndroidUtilities.displaySize.y = y;
                }
            }
            if (AndroidUtilities.roundMessageSize == 0) {
                if (isTablet()) {
                    AndroidUtilities.roundMessageSize = (int)(getMinTabletSide() * 0.6f);
                }
                else {
                    AndroidUtilities.roundMessageSize = (int)(Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) * 0.6f);
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("display size = ");
                sb.append(AndroidUtilities.displaySize.x);
                sb.append(" ");
                sb.append(AndroidUtilities.displaySize.y);
                sb.append(" ");
                sb.append(AndroidUtilities.displayMetrics.xdpi);
                sb.append("x");
                sb.append(AndroidUtilities.displayMetrics.ydpi);
                FileLog.e(sb.toString());
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void checkForCrashes(final Activity activity) {
    }
    
    public static void checkForUpdates(final Activity activity) {
    }
    
    public static boolean checkPhonePattern(final String s, String str) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            if (!s.equals("*")) {
                final String[] split = s.split("\\*");
                final String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str);
                int i = 0;
                int fromIndex = 0;
                while (i < split.length) {
                    str = split[i];
                    int n = fromIndex;
                    if (!TextUtils.isEmpty((CharSequence)str)) {
                        final int index = stripExceptNumbers.indexOf(str, fromIndex);
                        if (index == -1) {
                            return false;
                        }
                        n = index + str.length();
                    }
                    ++i;
                    fromIndex = n;
                }
            }
        }
        return true;
    }
    
    @SuppressLint({ "NewApi" })
    public static void clearDrawableAnimation(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            if (view != null) {
                if (view instanceof ListView) {
                    final Drawable selector = ((ListView)view).getSelector();
                    if (selector != null) {
                        selector.setState(StateSet.NOTHING);
                    }
                }
                else {
                    final Drawable background = view.getBackground();
                    if (background != null) {
                        background.setState(StateSet.NOTHING);
                        background.jumpToCurrentState();
                    }
                }
            }
        }
    }
    
    public static int compare(final int n, final int n2) {
        if (n == n2) {
            return 0;
        }
        if (n > n2) {
            return 1;
        }
        return -1;
    }
    
    public static CharSequence concat(final CharSequence... array) {
        if (array.length == 0) {
            return "";
        }
        final int length = array.length;
        final int n = 0;
        final int n2 = 0;
        final int n3 = 1;
        if (length == 1) {
            return array[0];
        }
        final int length2 = array.length;
        int i = 0;
        while (true) {
            while (i < length2) {
                if (array[i] instanceof Spanned) {
                    final int n4 = n3;
                    if (n4 != 0) {
                        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        for (int length3 = array.length, j = n2; j < length3; ++j) {
                            CharSequence charSequence;
                            if ((charSequence = array[j]) == null) {
                                charSequence = "null";
                            }
                            spannableStringBuilder.append(charSequence);
                        }
                        return (CharSequence)new SpannedString((CharSequence)spannableStringBuilder);
                    }
                    final StringBuilder sb = new StringBuilder();
                    for (int length4 = array.length, k = n; k < length4; ++k) {
                        sb.append(array[k]);
                    }
                    return sb.toString();
                }
                else {
                    ++i;
                }
            }
            final int n4 = 0;
            continue;
        }
    }
    
    public static boolean copyFile(final File p0, final File p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/io/File.exists:()Z
        //     4: ifne            12
        //     7: aload_1        
        //     8: invokevirtual   java/io/File.createNewFile:()Z
        //    11: pop            
        //    12: new             Ljava/io/FileInputStream;
        //    15: astore_2       
        //    16: aload_2        
        //    17: aload_0        
        //    18: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    21: aconst_null    
        //    22: astore_3       
        //    23: aload_3        
        //    24: astore          4
        //    26: new             Ljava/io/FileOutputStream;
        //    29: astore          5
        //    31: aload_3        
        //    32: astore          4
        //    34: aload           5
        //    36: aload_1        
        //    37: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    40: aload           5
        //    42: invokevirtual   java/io/FileOutputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    45: aload_2        
        //    46: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    49: lconst_0       
        //    50: aload_2        
        //    51: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //    54: invokevirtual   java/nio/channels/FileChannel.size:()J
        //    57: invokevirtual   java/nio/channels/FileChannel.transferFrom:(Ljava/nio/channels/ReadableByteChannel;JJ)J
        //    60: pop2           
        //    61: aload_3        
        //    62: astore          4
        //    64: aload           5
        //    66: invokevirtual   java/io/FileOutputStream.close:()V
        //    69: aload_2        
        //    70: invokevirtual   java/io/FileInputStream.close:()V
        //    73: iconst_1       
        //    74: ireturn        
        //    75: astore_0       
        //    76: aconst_null    
        //    77: astore_1       
        //    78: goto            85
        //    81: astore_1       
        //    82: aload_1        
        //    83: athrow         
        //    84: astore_0       
        //    85: aload_1        
        //    86: ifnull          100
        //    89: aload_3        
        //    90: astore          4
        //    92: aload           5
        //    94: invokevirtual   java/io/FileOutputStream.close:()V
        //    97: goto            108
        //   100: aload_3        
        //   101: astore          4
        //   103: aload           5
        //   105: invokevirtual   java/io/FileOutputStream.close:()V
        //   108: aload_3        
        //   109: astore          4
        //   111: aload_0        
        //   112: athrow         
        //   113: astore_0       
        //   114: goto            123
        //   117: astore_0       
        //   118: aload_0        
        //   119: astore          4
        //   121: aload_0        
        //   122: athrow         
        //   123: aload           4
        //   125: ifnull          135
        //   128: aload_2        
        //   129: invokevirtual   java/io/FileInputStream.close:()V
        //   132: goto            139
        //   135: aload_2        
        //   136: invokevirtual   java/io/FileInputStream.close:()V
        //   139: aload_0        
        //   140: athrow         
        //   141: astore_0       
        //   142: aload_0        
        //   143: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   146: iconst_0       
        //   147: ireturn        
        //   148: astore_1       
        //   149: goto            108
        //   152: astore_1       
        //   153: goto            139
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  12     21     141    148    Ljava/lang/Exception;
        //  26     31     117    123    Ljava/lang/Throwable;
        //  26     31     113    141    Any
        //  34     40     117    123    Ljava/lang/Throwable;
        //  34     40     113    141    Any
        //  40     61     81     85     Ljava/lang/Throwable;
        //  40     61     75     81     Any
        //  64     69     117    123    Ljava/lang/Throwable;
        //  64     69     113    141    Any
        //  69     73     141    148    Ljava/lang/Exception;
        //  82     84     84     85     Any
        //  92     97     148    152    Ljava/lang/Throwable;
        //  92     97     113    141    Any
        //  103    108    117    123    Ljava/lang/Throwable;
        //  103    108    113    141    Any
        //  111    113    117    123    Ljava/lang/Throwable;
        //  111    113    113    141    Any
        //  121    123    113    141    Any
        //  128    132    152    156    Ljava/lang/Throwable;
        //  135    139    141    148    Ljava/lang/Exception;
        //  139    141    141    148    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 88 out-of-bounds for length 88
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
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static boolean copyFile(final InputStream inputStream, final File file) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        final byte[] array = new byte[4096];
        while (true) {
            final int read = inputStream.read(array);
            if (read <= 0) {
                break;
            }
            Thread.yield();
            fileOutputStream.write(array, 0, read);
        }
        fileOutputStream.close();
        return true;
    }
    
    public static byte[] decodeQuotedPrintable(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < byteArray.length; ++i) {
            final byte b = byteArray[i];
            if (b == 61) {
                ++i;
                try {
                    final int digit = Character.digit((char)byteArray[i], 16);
                    ++i;
                    try {
                        byteArrayOutputStream.write((char)((digit << 4) + Character.digit((char)byteArray[i], 16)));
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                        return null;
                    }
                }
                catch (Exception ex3) {}
            }
            byteArrayOutputStream.write(b);
        }
        byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        return byteArray;
    }
    
    public static float distanceInfluenceForSnapDuration(final float n) {
        return (float)Math.sin((n - 0.5f) * 0.47123894f);
    }
    
    public static int dp(final float n) {
        if (n == 0.0f) {
            return 0;
        }
        return (int)Math.ceil(AndroidUtilities.density * n);
    }
    
    public static int dp2(final float n) {
        if (n == 0.0f) {
            return 0;
        }
        return (int)Math.floor(AndroidUtilities.density * n);
    }
    
    public static float dpf2(final float n) {
        if (n == 0.0f) {
            return 0.0f;
        }
        return AndroidUtilities.density * n;
    }
    
    public static int dpr(final float n) {
        if (n == 0.0f) {
            return 0;
        }
        return Math.round(AndroidUtilities.density * n);
    }
    
    public static void endIncomingCall() {
        if (!AndroidUtilities.hasCallPermissions) {
            return;
        }
        try {
            final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            final Method declaredMethod = Class.forName(telephonyManager.getClass().getName()).getDeclaredMethod("getITelephony", (Class<?>[])new Class[0]);
            declaredMethod.setAccessible(true);
            final ITelephony telephony = (ITelephony)declaredMethod.invoke(telephonyManager, new Object[0]);
            final ITelephony telephony2 = (ITelephony)declaredMethod.invoke(telephonyManager, new Object[0]);
            telephony2.silenceRinger();
            telephony2.endCall();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static double fixLocationCoord(double v) {
        v = (double)(long)(v * 1000000.0);
        Double.isNaN(v);
        return v / 1000000.0;
    }
    
    public static String formapMapUrl(int i, final double d, final double d2, final int n, final int n2, final boolean b, final int n3) {
        final int min = Math.min(2, (int)Math.ceil(AndroidUtilities.density));
        final int mapProvider = MessagesController.getInstance(i).mapProvider;
        if (mapProvider != 1 && mapProvider != 3) {
            final String mapKey = MessagesController.getInstance(i).mapKey;
            if (!TextUtils.isEmpty((CharSequence)mapKey)) {
                if (b) {
                    return String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d&markers=color:red%%7Csize:mid%%7C%.6f,%.6f&sensor=false&key=%s", d, d2, n3, n, n2, min, d, d2, mapKey);
                }
                return String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d&key=%s", d, d2, n3, n, n2, min, mapKey);
            }
            else {
                if (b) {
                    return String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d&markers=color:red%%7Csize:mid%%7C%.6f,%.6f&sensor=false", d, d2, n3, n, n2, min, d, d2);
                }
                return String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d", d, d2, n3, n, n2, min);
            }
        }
        else {
            final String[] array = { "ru_RU", "tr_TR" };
            final LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
            String s = null;
            for (i = 0; i < array.length; ++i) {
                if (array[i].toLowerCase().contains(currentLocaleInfo.shortName)) {
                    s = array[i];
                }
            }
            String s2;
            if ((s2 = s) == null) {
                s2 = "en_US";
            }
            if (b) {
                return String.format(Locale.US, "https://static-maps.yandex.ru/1.x/?ll=%.6f,%.6f&z=%d&size=%d,%d&l=map&scale=%d&pt=%.6f,%.6f,vkbkm&lang=%s", d2, d, n3, n * min, n2 * min, min, d2, d, s2);
            }
            return String.format(Locale.US, "https://static-maps.yandex.ru/1.x/?ll=%.6f,%.6f&z=%d&size=%d,%d&l=map&scale=%d&lang=%s", d2, d, n3, n * min, n2 * min, min, s2);
        }
    }
    
    public static String formatFileSize(final long n) {
        return formatFileSize(n, false);
    }
    
    public static String formatFileSize(final long l, final boolean b) {
        if (l < 1024L) {
            return String.format("%d B", l);
        }
        if (l < 1048576L) {
            final float f = l / 1024.0f;
            if (b) {
                final int i = (int)f;
                if ((f - i) * 10.0f == 0.0f) {
                    return String.format("%d KB", i);
                }
            }
            return String.format("%.1f KB", f);
        }
        if (l < 1073741824L) {
            final float f2 = l / 1024.0f / 1024.0f;
            if (b) {
                final int j = (int)f2;
                if ((f2 - j) * 10.0f == 0.0f) {
                    return String.format("%d MB", j);
                }
            }
            return String.format("%.1f MB", f2);
        }
        final float f3 = l / 1024.0f / 1024.0f / 1024.0f;
        if (b) {
            final int k = (int)f3;
            if ((f3 - k) * 10.0f == 0.0f) {
                return String.format("%d GB", k);
            }
        }
        return String.format("%.1f GB", f3);
    }
    
    public static File generatePicturePath() {
        return generatePicturePath(false);
    }
    
    public static File generatePicturePath(final boolean b) {
        try {
            final File albumDir = getAlbumDir(b);
            final Date date = new Date();
            date.setTime(System.currentTimeMillis() + Utilities.random.nextInt(1000) + 1L);
            final String format = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);
            final StringBuilder sb = new StringBuilder();
            sb.append("IMG_");
            sb.append(format);
            sb.append(".jpg");
            return new File(albumDir, sb.toString());
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    public static CharSequence generateSearchName(String trim, String string, final String str) {
        if (trim == null && string == null) {
            return "";
        }
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        String string2;
        if (trim != null && trim.length() != 0) {
            string2 = trim;
            if (string != null) {
                string2 = trim;
                if (string.length() != 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(trim);
                    sb.append(" ");
                    sb.append(string);
                    string2 = sb.toString();
                }
            }
        }
        else {
            string2 = string;
        }
        trim = string2.trim();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(" ");
        sb2.append(trim.toLowerCase());
        string = sb2.toString();
        int beginIndex = 0;
        while (true) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(" ");
            sb3.append(str);
            final int index = string.indexOf(sb3.toString(), beginIndex);
            if (index == -1) {
                break;
            }
            final int n = 1;
            int n2;
            if (index == 0) {
                n2 = 0;
            }
            else {
                n2 = 1;
            }
            final int beginIndex2 = index - n2;
            final int length = str.length();
            int n3 = n;
            if (index == 0) {
                n3 = 0;
            }
            final int b = length + n3 + beginIndex2;
            if (beginIndex != 0 && beginIndex != beginIndex2 + 1) {
                spannableStringBuilder.append((CharSequence)trim.substring(beginIndex, beginIndex2));
            }
            else if (beginIndex == 0 && beginIndex2 != 0) {
                spannableStringBuilder.append((CharSequence)trim.substring(0, beginIndex2));
            }
            final String substring = trim.substring(beginIndex2, Math.min(trim.length(), b));
            if (substring.startsWith(" ")) {
                spannableStringBuilder.append((CharSequence)" ");
            }
            final String trim2 = substring.trim();
            final int length2 = spannableStringBuilder.length();
            spannableStringBuilder.append((CharSequence)trim2);
            spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), length2, trim2.length() + length2, 33);
            beginIndex = b;
        }
        if (beginIndex != -1 && beginIndex < trim.length()) {
            spannableStringBuilder.append((CharSequence)trim.substring(beginIndex));
        }
        return (CharSequence)spannableStringBuilder;
    }
    
    public static File generateVideoPath() {
        return generateVideoPath(false);
    }
    
    public static File generateVideoPath(final boolean b) {
        try {
            final File albumDir = getAlbumDir(b);
            final Date date = new Date();
            date.setTime(System.currentTimeMillis() + Utilities.random.nextInt(1000) + 1L);
            final String format = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);
            final StringBuilder sb = new StringBuilder();
            sb.append("VID_");
            sb.append(format);
            sb.append(".mp4");
            return new File(albumDir, sb.toString());
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    private static File getAlbumDir(final boolean b) {
        if (!b && (Build$VERSION.SDK_INT < 23 || ApplicationLoader.applicationContext.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0)) {
            File file2;
            if ("mounted".equals(Environment.getExternalStorageState())) {
                final File file = file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Telegram");
                if (!file.mkdirs()) {
                    file2 = file;
                    if (!file.exists()) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("failed to create directory");
                        }
                        return null;
                    }
                }
            }
            else {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("External storage is not mounted READ/WRITE.");
                }
                file2 = null;
            }
            return file2;
        }
        return FileLoader.getDirectory(4);
    }
    
    public static File getCacheDir() {
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        }
        catch (Exception ex) {
            FileLog.e(ex);
            externalStorageState = null;
        }
        Label_0046: {
            if (externalStorageState != null) {
                if (!externalStorageState.startsWith("mounted")) {
                    break Label_0046;
                }
            }
            try {
                final File externalCacheDir = ApplicationLoader.applicationContext.getExternalCacheDir();
                if (externalCacheDir != null) {
                    return externalCacheDir;
                }
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
            try {
                final File cacheDir = ApplicationLoader.applicationContext.getCacheDir();
                if (cacheDir != null) {
                    return cacheDir;
                }
            }
            catch (Exception ex3) {
                FileLog.e(ex3);
            }
        }
        return new File("");
    }
    
    public static String[] getCurrentKeyboardLanguage() {
        try {
            final InputMethodManager inputMethodManager = (InputMethodManager)ApplicationLoader.applicationContext.getSystemService("input_method");
            final InputMethodSubtype currentInputMethodSubtype = inputMethodManager.getCurrentInputMethodSubtype();
            String s;
            if (currentInputMethodSubtype != null) {
                String languageTag;
                if (Build$VERSION.SDK_INT >= 24) {
                    languageTag = currentInputMethodSubtype.getLanguageTag();
                }
                else {
                    languageTag = null;
                }
                s = languageTag;
                if (TextUtils.isEmpty((CharSequence)languageTag)) {
                    s = currentInputMethodSubtype.getLocale();
                }
            }
            else {
                final InputMethodSubtype lastInputMethodSubtype = inputMethodManager.getLastInputMethodSubtype();
                if (lastInputMethodSubtype != null) {
                    if (Build$VERSION.SDK_INT >= 24) {
                        s = lastInputMethodSubtype.getLanguageTag();
                    }
                    else {
                        s = null;
                    }
                    if (TextUtils.isEmpty((CharSequence)s)) {
                        s = lastInputMethodSubtype.getLocale();
                    }
                }
                else {
                    s = null;
                }
            }
            if (!TextUtils.isEmpty((CharSequence)s)) {
                return new String[] { s.replace('_', '-') };
            }
            final String systemLocaleStringIso639 = LocaleController.getSystemLocaleStringIso639();
            final LocaleController.LocaleInfo currentLocaleInfo = LocaleController.getInstance().getCurrentLocaleInfo();
            String s2 = currentLocaleInfo.getBaseLangCode();
            if (TextUtils.isEmpty((CharSequence)s2)) {
                s2 = currentLocaleInfo.getLangCode();
            }
            String s3 = null;
            Label_0179: {
                if (!systemLocaleStringIso639.contains(s2)) {
                    s3 = s2;
                    if (!s2.contains(systemLocaleStringIso639)) {
                        break Label_0179;
                    }
                }
                if (!systemLocaleStringIso639.contains("en")) {
                    s3 = "en";
                }
                else {
                    s3 = null;
                }
            }
            if (!TextUtils.isEmpty((CharSequence)s3)) {
                return new String[] { systemLocaleStringIso639.replace('_', '-'), s3 };
            }
            return new String[] { systemLocaleStringIso639.replace('_', '-') };
        }
        catch (Exception ex) {
            return new String[] { "en" };
        }
    }
    
    public static String getDataColumn(final Context p0, final Uri p1, final String p2, final String[] p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //     4: aload_1        
        //     5: iconst_1       
        //     6: anewarray       Ljava/lang/String;
        //     9: dup            
        //    10: iconst_0       
        //    11: ldc_w           "_data"
        //    14: aastore        
        //    15: aload_2        
        //    16: aload_3        
        //    17: aconst_null    
        //    18: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    21: astore_3       
        //    22: aload_3        
        //    23: ifnull          151
        //    26: aload_3        
        //    27: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    32: ifeq            151
        //    35: aload_3        
        //    36: aload_3        
        //    37: ldc_w           "_data"
        //    40: invokeinterface android/database/Cursor.getColumnIndexOrThrow:(Ljava/lang/String;)I
        //    45: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //    50: astore_0       
        //    51: aload_0        
        //    52: ldc_w           "content://"
        //    55: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    58: ifne            100
        //    61: aload_0        
        //    62: ldc_w           "/"
        //    65: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    68: ifne            88
        //    71: aload_0        
        //    72: ldc_w           "file://"
        //    75: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    78: istore          4
        //    80: iload           4
        //    82: ifne            88
        //    85: goto            100
        //    88: aload_3        
        //    89: ifnull          98
        //    92: aload_3        
        //    93: invokeinterface android/database/Cursor.close:()V
        //    98: aload_0        
        //    99: areturn        
        //   100: aload_3        
        //   101: ifnull          110
        //   104: aload_3        
        //   105: invokeinterface android/database/Cursor.close:()V
        //   110: aconst_null    
        //   111: areturn        
        //   112: astore_0       
        //   113: aconst_null    
        //   114: astore_2       
        //   115: goto            126
        //   118: astore_0       
        //   119: aload_0        
        //   120: athrow         
        //   121: astore_1       
        //   122: aload_0        
        //   123: astore_2       
        //   124: aload_1        
        //   125: astore_0       
        //   126: aload_3        
        //   127: ifnull          149
        //   130: aload_2        
        //   131: ifnull          143
        //   134: aload_3        
        //   135: invokeinterface android/database/Cursor.close:()V
        //   140: goto            149
        //   143: aload_3        
        //   144: invokeinterface android/database/Cursor.close:()V
        //   149: aload_0        
        //   150: athrow         
        //   151: aload_3        
        //   152: ifnull          161
        //   155: aload_3        
        //   156: invokeinterface android/database/Cursor.close:()V
        //   161: aconst_null    
        //   162: areturn        
        //   163: astore_0       
        //   164: goto            161
        //   167: astore_1       
        //   168: goto            149
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      22     163    167    Ljava/lang/Exception;
        //  26     80     118    126    Ljava/lang/Throwable;
        //  26     80     112    118    Any
        //  92     98     163    167    Ljava/lang/Exception;
        //  104    110    163    167    Ljava/lang/Exception;
        //  119    121    121    126    Any
        //  134    140    167    171    Ljava/lang/Throwable;
        //  143    149    163    167    Ljava/lang/Exception;
        //  149    151    163    167    Ljava/lang/Exception;
        //  155    161    163    167    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0143:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
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
    
    public static int getMinTabletSide() {
        if (!isSmallTablet()) {
            final Point displaySize = AndroidUtilities.displaySize;
            final int min = Math.min(displaySize.x, displaySize.y);
            int dp;
            if ((dp = min * 35 / 100) < dp(320.0f)) {
                dp = dp(320.0f);
            }
            return min - dp;
        }
        final Point displaySize2 = AndroidUtilities.displaySize;
        final int min2 = Math.min(displaySize2.x, displaySize2.y);
        final Point displaySize3 = AndroidUtilities.displaySize;
        final int max = Math.max(displaySize3.x, displaySize3.y);
        int dp2;
        if ((dp2 = max * 35 / 100) < dp(320.0f)) {
            dp2 = dp(320.0f);
        }
        return Math.min(min2, max - dp2);
    }
    
    public static int getMyLayerVersion(final int n) {
        return n & 0xFFFF;
    }
    
    public static int getOffsetColor(int alpha, int blue, final float n, final float n2) {
        final int red = Color.red(blue);
        final int green = Color.green(blue);
        final int blue2 = Color.blue(blue);
        final int alpha2 = Color.alpha(blue);
        final int red2 = Color.red(alpha);
        final int green2 = Color.green(alpha);
        blue = Color.blue(alpha);
        alpha = Color.alpha(alpha);
        return Color.argb((int)((alpha + (alpha2 - alpha) * n) * n2), (int)(red2 + (red - red2) * n), (int)(green2 + (green - green2) * n), (int)(blue + (blue2 - blue) * n));
    }
    
    @SuppressLint({ "NewApi" })
    public static String getPath(Uri uri) {
        try {
            if (Build$VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(ApplicationLoader.applicationContext, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String[] split = DocumentsContract.getDocumentId(uri).split(":");
                    if ("primary".equalsIgnoreCase(split[0])) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(Environment.getExternalStorageDirectory());
                        sb.append("/");
                        sb.append(split[1]);
                        return sb.toString();
                    }
                }
                else {
                    if (isDownloadsDocument(uri)) {
                        uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), (long)Long.valueOf(DocumentsContract.getDocumentId(uri)));
                        return getDataColumn(ApplicationLoader.applicationContext, uri, null, null);
                    }
                    if (isMediaDocument(uri)) {
                        final String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                        final String s = split2[0];
                        int n = -1;
                        final int hashCode = s.hashCode();
                        if (hashCode != 93166550) {
                            if (hashCode != 100313435) {
                                if (hashCode == 112202875) {
                                    if (s.equals("video")) {
                                        n = 1;
                                    }
                                }
                            }
                            else if (s.equals("image")) {
                                n = 0;
                            }
                        }
                        else if (s.equals("audio")) {
                            n = 2;
                        }
                        if (n != 0) {
                            if (n != 1) {
                                if (n != 2) {
                                    uri = null;
                                }
                                else {
                                    uri = MediaStore$Audio$Media.EXTERNAL_CONTENT_URI;
                                }
                            }
                            else {
                                uri = MediaStore$Video$Media.EXTERNAL_CONTENT_URI;
                            }
                        }
                        else {
                            uri = MediaStore$Images$Media.EXTERNAL_CONTENT_URI;
                        }
                        return getDataColumn(ApplicationLoader.applicationContext, uri, "_id=?", new String[] { split2[1] });
                    }
                }
            }
            else {
                if ("content".equalsIgnoreCase(uri.getScheme())) {
                    return getDataColumn(ApplicationLoader.applicationContext, uri, null, null);
                }
                if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return null;
    }
    
    public static int getPatternColor(final int n) {
        final float[] rgBtoHSB = RGBtoHSB(Color.red(n), Color.green(n), Color.blue(n));
        if (rgBtoHSB[1] > 0.0f || (rgBtoHSB[2] < 1.0f && rgBtoHSB[2] > 0.0f)) {
            rgBtoHSB[1] = Math.min(1.0f, rgBtoHSB[1] + 0.05f + (1.0f - rgBtoHSB[1]) * 0.1f);
        }
        if (rgBtoHSB[2] > 0.5f) {
            rgBtoHSB[2] = Math.max(0.0f, rgBtoHSB[2] * 0.65f);
        }
        else {
            rgBtoHSB[2] = Math.max(0.0f, Math.min(1.0f, 1.0f - rgBtoHSB[2] * 0.65f));
        }
        return HSBtoRGB(rgBtoHSB[0], rgBtoHSB[1], rgBtoHSB[2]) & 0x66FFFFFF;
    }
    
    public static int getPatternSideColor(final int n) {
        final float[] rgBtoHSB = RGBtoHSB(Color.red(n), Color.green(n), Color.blue(n));
        rgBtoHSB[1] = Math.min(1.0f, rgBtoHSB[1] + 0.05f);
        if (rgBtoHSB[2] > 0.5f) {
            rgBtoHSB[2] = Math.max(0.0f, rgBtoHSB[2] * 0.9f);
        }
        else {
            rgBtoHSB[2] = Math.max(0.0f, rgBtoHSB[2] * 0.9f);
        }
        return HSBtoRGB(rgBtoHSB[0], rgBtoHSB[1], rgBtoHSB[2]) | 0xFF000000;
    }
    
    public static int getPeerLayerVersion(final int n) {
        return n >> 16 & 0xFFFF;
    }
    
    public static int getPhotoSize() {
        if (AndroidUtilities.photoSize == null) {
            AndroidUtilities.photoSize = 1280;
        }
        return AndroidUtilities.photoSize;
    }
    
    public static float getPixelsInCM(float n, final boolean b) {
        final float n2 = n / 2.54f;
        if (b) {
            n = AndroidUtilities.displayMetrics.xdpi;
        }
        else {
            n = AndroidUtilities.displayMetrics.ydpi;
        }
        return n2 * n;
    }
    
    public static Point getRealScreenSize() {
        final Point point = new Point();
        try {
            final WindowManager windowManager = (WindowManager)ApplicationLoader.applicationContext.getSystemService("window");
            if (Build$VERSION.SDK_INT >= 17) {
                windowManager.getDefaultDisplay().getRealSize(point);
            }
            else {
                try {
                    point.set((int)Display.class.getMethod("getRawWidth", (Class<?>[])new Class[0]).invoke(windowManager.getDefaultDisplay(), new Object[0]), (int)Display.class.getMethod("getRawHeight", (Class<?>[])new Class[0]).invoke(windowManager.getDefaultDisplay(), new Object[0]));
                }
                catch (Exception ex) {
                    point.set(windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight());
                    FileLog.e(ex);
                }
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        return point;
    }
    
    public static int getShadowHeight() {
        final float density = AndroidUtilities.density;
        if (density >= 4.0f) {
            return 3;
        }
        if (density >= 2.0f) {
            return 2;
        }
        return 1;
    }
    
    public static byte[] getStringBytes(final String s) {
        try {
            return s.getBytes("UTF-8");
        }
        catch (Exception ex) {
            return new byte[0];
        }
    }
    
    @SuppressLint({ "PrivateApi" })
    public static String getSystemProperty(String s) {
        try {
            s = (String)Class.forName("android.os.SystemProperties").getMethod("get", String.class).invoke(null, s);
            return s;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static int getThumbForNameOrMime(final String s, String substring, final boolean b) {
        if (s != null && s.length() != 0) {
            int n;
            if (!s.contains(".doc") && !s.contains(".txt") && !s.contains(".psd")) {
                if (!s.contains(".xls") && !s.contains(".csv")) {
                    if (!s.contains(".pdf") && !s.contains(".ppt") && !s.contains(".key")) {
                        if (!s.contains(".zip") && !s.contains(".rar") && !s.contains(".ai") && !s.contains(".mp3") && !s.contains(".mov") && !s.contains(".avi")) {
                            n = -1;
                        }
                        else {
                            n = 3;
                        }
                    }
                    else {
                        n = 2;
                    }
                }
                else {
                    n = 1;
                }
            }
            else {
                n = 0;
            }
            int n2 = n;
            if (n == -1) {
                final int lastIndex = s.lastIndexOf(46);
                if (lastIndex == -1) {
                    substring = "";
                }
                else {
                    substring = s.substring(lastIndex + 1);
                }
                if (substring.length() != 0) {
                    n2 = substring.charAt(0) % AndroidUtilities.documentIcons.length;
                }
                else {
                    n2 = s.charAt(0) % AndroidUtilities.documentIcons.length;
                }
            }
            int n3;
            if (b) {
                n3 = AndroidUtilities.documentMediaIcons[n2];
            }
            else {
                n3 = AndroidUtilities.documentIcons[n2];
            }
            return n3;
        }
        int n4;
        if (b) {
            n4 = AndroidUtilities.documentMediaIcons[0];
        }
        else {
            n4 = AndroidUtilities.documentIcons[0];
        }
        return n4;
    }
    
    public static CharSequence getTrimmedString(CharSequence subSequence) {
        CharSequence charSequence = subSequence;
        if (subSequence != null) {
            CharSequence subSequence2 = subSequence;
            if (subSequence.length() == 0) {
                charSequence = subSequence;
            }
            else {
                while (true) {
                    subSequence = subSequence2;
                    if (subSequence2.length() <= 0) {
                        break;
                    }
                    if (subSequence2.charAt(0) != '\n') {
                        subSequence = subSequence2;
                        if (subSequence2.charAt(0) != ' ') {
                            break;
                        }
                    }
                    subSequence2 = subSequence2.subSequence(1, subSequence2.length());
                }
                while (true) {
                    charSequence = subSequence;
                    if (subSequence.length() <= 0) {
                        break;
                    }
                    if (subSequence.charAt(subSequence.length() - 1) != '\n') {
                        charSequence = subSequence;
                        if (subSequence.charAt(subSequence.length() - 1) != ' ') {
                            break;
                        }
                    }
                    subSequence = subSequence.subSequence(0, subSequence.length() - 1);
                }
            }
        }
        return charSequence;
    }
    
    public static Typeface getTypeface(final String s) {
        synchronized (AndroidUtilities.typefaceCache) {
            if (!AndroidUtilities.typefaceCache.containsKey(s)) {
                try {
                    Typeface value;
                    if (Build$VERSION.SDK_INT >= 26) {
                        final Typeface$Builder typeface$Builder = new Typeface$Builder(ApplicationLoader.applicationContext.getAssets(), s);
                        if (s.contains("medium")) {
                            typeface$Builder.setWeight(700);
                        }
                        if (s.contains("italic")) {
                            typeface$Builder.setItalic(true);
                        }
                        value = typeface$Builder.build();
                    }
                    else {
                        value = Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), s);
                    }
                    AndroidUtilities.typefaceCache.put(s, value);
                }
                catch (Exception ex) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Could not get typeface '");
                        sb.append(s);
                        sb.append("' because ");
                        sb.append(ex.getMessage());
                        FileLog.e(sb.toString());
                    }
                    return null;
                }
            }
            return AndroidUtilities.typefaceCache.get(s);
        }
    }
    
    public static int getViewInset(final View obj) {
        if (obj != null && Build$VERSION.SDK_INT >= 21 && obj.getHeight() != AndroidUtilities.displaySize.y) {
            if (obj.getHeight() != AndroidUtilities.displaySize.y - AndroidUtilities.statusBarHeight) {
                try {
                    if (AndroidUtilities.mAttachInfoField == null) {
                        (AndroidUtilities.mAttachInfoField = View.class.getDeclaredField("mAttachInfo")).setAccessible(true);
                    }
                    final Object value = AndroidUtilities.mAttachInfoField.get(obj);
                    if (value != null) {
                        if (AndroidUtilities.mStableInsetsField == null) {
                            (AndroidUtilities.mStableInsetsField = value.getClass().getDeclaredField("mStableInsets")).setAccessible(true);
                        }
                        return ((Rect)AndroidUtilities.mStableInsetsField.get(value)).bottom;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        return 0;
    }
    
    public static String getWallPaperUrl(final Object o, final int n) {
        String s;
        if (o instanceof TLRPC.TL_wallPaper) {
            final TLRPC.TL_wallPaper tl_wallPaper = (TLRPC.TL_wallPaper)o;
            final StringBuilder sb = new StringBuilder();
            sb.append("https://");
            sb.append(MessagesController.getInstance(n).linkPrefix);
            sb.append("/bg/");
            sb.append(tl_wallPaper.slug);
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            final TLRPC.TL_wallPaperSettings settings = tl_wallPaper.settings;
            if (settings != null) {
                if (settings.blur) {
                    sb2.append("blur");
                }
                if (tl_wallPaper.settings.motion) {
                    if (sb2.length() > 0) {
                        sb2.append("+");
                    }
                    sb2.append("motion");
                }
            }
            s = string;
            if (sb2.length() > 0) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(string);
                sb3.append("?mode=");
                sb3.append(sb2.toString());
                s = sb3.toString();
            }
        }
        else if (o instanceof WallpapersListActivity.ColorWallpaper) {
            final WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper)o;
            final String lowerCase = String.format("%02x%02x%02x", (byte)(colorWallpaper.color >> 16) & 0xFF, (byte)(colorWallpaper.color >> 8) & 0xFF, (byte)(colorWallpaper.color & 0xFF)).toLowerCase();
            if (colorWallpaper.pattern != null) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("https://");
                sb4.append(MessagesController.getInstance(n).linkPrefix);
                sb4.append("/bg/");
                sb4.append(colorWallpaper.pattern.slug);
                sb4.append("?intensity=");
                sb4.append((int)(colorWallpaper.intensity * 100.0f));
                sb4.append("&bg_color=");
                sb4.append(lowerCase);
                s = sb4.toString();
            }
            else {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("https://");
                sb5.append(MessagesController.getInstance(n).linkPrefix);
                sb5.append("/bg/");
                sb5.append(lowerCase);
                s = sb5.toString();
            }
        }
        else {
            s = null;
        }
        return s;
    }
    
    public static boolean handleProxyIntent(final Activity activity, final Intent intent) {
        if (intent == null) {
            return false;
        }
        try {
            if ((intent.getFlags() & 0x100000) != 0x0) {
                return false;
            }
            final Uri data = intent.getData();
            if (data != null) {
                final String scheme = data.getScheme();
                String s = null;
                final String s2 = null;
                String s3 = null;
                String s4 = null;
                String queryParameter3 = null;
                String queryParameter4 = null;
                Label_0367: {
                    if (scheme != null) {
                        if (scheme.equals("http") || scheme.equals("https")) {
                            final String lowerCase = data.getHost().toLowerCase();
                            String queryParameter = null;
                            String queryParameter2 = null;
                            Label_0341: {
                                if (lowerCase.equals("telegram.me") || lowerCase.equals("t.me") || lowerCase.equals("telegram.dog")) {
                                    final String path = data.getPath();
                                    if (path != null && (path.startsWith("/socks") || path.startsWith("/proxy"))) {
                                        queryParameter = data.getQueryParameter("server");
                                        queryParameter2 = data.getQueryParameter("port");
                                        s = data.getQueryParameter("user");
                                        s3 = data.getQueryParameter("pass");
                                        s4 = data.getQueryParameter("secret");
                                        break Label_0341;
                                    }
                                }
                                queryParameter = (queryParameter2 = null);
                                s4 = (s3 = queryParameter2);
                                s = s2;
                            }
                            final String s5 = queryParameter;
                            queryParameter3 = queryParameter2;
                            queryParameter4 = s5;
                            break Label_0367;
                        }
                        if (scheme.equals("tg")) {
                            final String string = data.toString();
                            if (string.startsWith("tg:proxy") || string.startsWith("tg://proxy") || string.startsWith("tg:socks") || string.startsWith("tg://socks")) {
                                final Uri parse = Uri.parse(string.replace("tg:proxy", "tg://telegram.org").replace("tg://proxy", "tg://telegram.org").replace("tg://socks", "tg://telegram.org").replace("tg:socks", "tg://telegram.org"));
                                queryParameter4 = parse.getQueryParameter("server");
                                queryParameter3 = parse.getQueryParameter("port");
                                s = parse.getQueryParameter("user");
                                s3 = parse.getQueryParameter("pass");
                                s4 = parse.getQueryParameter("secret");
                                break Label_0367;
                            }
                        }
                    }
                    s4 = (s3 = null);
                    queryParameter4 = (queryParameter3 = s3);
                }
                if (!TextUtils.isEmpty((CharSequence)queryParameter4) && !TextUtils.isEmpty((CharSequence)queryParameter3)) {
                    if (s == null) {
                        s = "";
                    }
                    if (s3 == null) {
                        s3 = "";
                    }
                    if (s4 == null) {
                        s4 = "";
                    }
                    showProxyAlert(activity, queryParameter4, queryParameter3, s, s3, s4);
                    return true;
                }
            }
            return false;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static void hideKeyboard(final View view) {
        if (view == null) {
            return;
        }
        try {
            final InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService("input_method");
            if (!inputMethodManager.isActive()) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private static int[] hsvToRgb(double a, double n, double n2) {
        a *= 6.0;
        final double v = (int)Math.floor(a);
        Double.isNaN(v);
        final double n3 = a - v;
        double n4 = (1.0 - n) * n2;
        a = (1.0 - n3 * n) * n2;
        n = n2 * (1.0 - (1.0 - n3) * n);
        final int n5 = (int)v % 6;
        final double n6 = 0.0;
        if (n5 != 0) {
            if (n5 == 1) {
                n = n2;
                n2 = n4;
                n4 = a;
                a = n2;
                return new int[] { (int)(n4 * 255.0), (int)(n * 255.0), (int)(a * 255.0) };
            }
            if (n5 == 2) {
                a = n;
                n = n2;
                return new int[] { (int)(n4 * 255.0), (int)(n * 255.0), (int)(a * 255.0) };
            }
            if (n5 == 3) {
                n = a;
                a = n2;
                return new int[] { (int)(n4 * 255.0), (int)(n * 255.0), (int)(a * 255.0) };
            }
            if (n5 == 4) {
                final double n7 = n4;
                a = n2;
                n4 = n;
                n = n7;
                return new int[] { (int)(n4 * 255.0), (int)(n * 255.0), (int)(a * 255.0) };
            }
            if (n5 != 5) {
                a = (n4 = 0.0);
                n = n6;
                return new int[] { (int)(n4 * 255.0), (int)(n * 255.0), (int)(a * 255.0) };
            }
            n = n4;
        }
        else {
            a = n4;
        }
        n4 = n2;
        return new int[] { (int)(n4 * 255.0), (int)(n * 255.0), (int)(a * 255.0) };
    }
    
    public static boolean isAirplaneModeOn() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = true;
        boolean b2 = true;
        if (sdk_INT < 17) {
            if (Settings$System.getInt(ApplicationLoader.applicationContext.getContentResolver(), "airplane_mode_on", 0) == 0) {
                b2 = false;
            }
            return b2;
        }
        return Settings$Global.getInt(ApplicationLoader.applicationContext.getContentResolver(), "airplane_mode_on", 0) != 0 && b;
    }
    
    public static boolean isBannedForever(final TLRPC.TL_chatBannedRights tl_chatBannedRights) {
        return tl_chatBannedRights == null || Math.abs(tl_chatBannedRights.until_date - System.currentTimeMillis() / 1000L) > 157680000L;
    }
    
    public static boolean isDownloadsDocument(final Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    
    public static boolean isExternalStorageDocument(final Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    
    public static boolean isGoogleMapsInstalled(final BaseFragment baseFragment) {
        return true;
    }
    
    public static boolean isInternalUri(Uri path) {
        path = ((Uri)path).getPath();
        final boolean b = false;
        if (path == null) {
            return false;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(Pattern.quote(new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_logs").getAbsolutePath()));
        sb.append("/\\d+\\.log");
        if (((String)path).matches(sb.toString())) {
            return false;
        }
        int n = 0;
        while (path == null || ((String)path).length() <= 4096) {
            final String readlink = Utilities.readlink((String)path);
            if (readlink == null || readlink.equals(path)) {
                Object o;
                if ((o = path) != null) {
                    try {
                        final String canonicalPath = new File((String)path).getCanonicalPath();
                        o = path;
                        if (canonicalPath != null) {
                            o = canonicalPath;
                        }
                    }
                    catch (Exception ex) {
                        ((String)path).replace("/./", "/");
                        o = path;
                    }
                }
                boolean b2 = b;
                if (o != null) {
                    final String lowerCase = ((String)o).toLowerCase();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("/data/data/");
                    sb2.append(ApplicationLoader.applicationContext.getPackageName());
                    b2 = b;
                    if (lowerCase.contains(sb2.toString())) {
                        b2 = true;
                    }
                }
                return b2;
            }
            if (++n >= 10) {
                return true;
            }
            path = readlink;
        }
        return true;
    }
    
    public static boolean isKeyboardShowed(final View view) {
        if (view == null) {
            return false;
        }
        try {
            return ((InputMethodManager)view.getContext().getSystemService("input_method")).isActive(view);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return false;
        }
    }
    
    public static boolean isMediaDocument(final Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    
    public static boolean isSmallTablet() {
        final Point displaySize = AndroidUtilities.displaySize;
        return Math.min(displaySize.x, displaySize.y) / AndroidUtilities.density <= 700.0f;
    }
    
    public static boolean isTablet() {
        if (AndroidUtilities.isTablet == null) {
            AndroidUtilities.isTablet = ApplicationLoader.applicationContext.getResources().getBoolean(2130968579);
        }
        return AndroidUtilities.isTablet;
    }
    
    public static boolean isWaitingForCall() {
        synchronized (AndroidUtilities.callLock) {
            return AndroidUtilities.waitingForCall;
        }
    }
    
    public static boolean isWaitingForSms() {
        synchronized (AndroidUtilities.smsLock) {
            return AndroidUtilities.waitingForSms;
        }
    }
    
    public static ArrayList<TLRPC.User> loadVCardFromStream(final Uri uri, final int n, final boolean b, final ArrayList<VcardItem> list, String name) {
        ArrayList<TLRPC.User> list2 = null;
        Label_0044: {
            if (b) {
                try {
                    final InputStream in = ApplicationLoader.applicationContext.getContentResolver().openAssetFileDescriptor(uri, "r").createInputStream();
                    break Label_0044;
                }
                catch (Throwable t) {
                    break Label_0044;
                }
            }
            try {
                final InputStream in = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                final ArrayList<String> list3 = new ArrayList<String>();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                Object e = null;
                Object e2;
                Object substring = e2 = e;
                boolean b2 = false;
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.startsWith("PHOTO")) {
                        b2 = true;
                    }
                    else {
                        if (line.indexOf(58) >= 0) {
                            Label_0731: {
                                if (line.startsWith("BEGIN:VCARD")) {
                                    e = new VcardData();
                                    list3.add((String)e);
                                    ((VcardData)e).name = name;
                                    e2 = null;
                                }
                                else {
                                    if (!line.startsWith("END:VCARD")) {
                                        if (list != null) {
                                            Label_0700: {
                                                if (line.startsWith("TEL")) {
                                                    e2 = new VcardItem();
                                                    ((VcardItem)e2).type = 0;
                                                }
                                                else if (line.startsWith("EMAIL")) {
                                                    e2 = new VcardItem();
                                                    ((VcardItem)e2).type = 1;
                                                }
                                                else if (!line.startsWith("ADR") && !line.startsWith("LABEL") && !line.startsWith("GEO")) {
                                                    if (line.startsWith("URL")) {
                                                        e2 = new VcardItem();
                                                        ((VcardItem)e2).type = 3;
                                                    }
                                                    else if (line.startsWith("NOTE")) {
                                                        e2 = new VcardItem();
                                                        ((VcardItem)e2).type = 4;
                                                    }
                                                    else if (line.startsWith("BDAY")) {
                                                        e2 = new VcardItem();
                                                        ((VcardItem)e2).type = 5;
                                                    }
                                                    else if (!line.startsWith("ORG") && !line.startsWith("TITLE") && !line.startsWith("ROLE")) {
                                                        if (line.startsWith("X-ANDROID")) {
                                                            e2 = new VcardItem();
                                                            ((VcardItem)e2).type = -1;
                                                        }
                                                        else {
                                                            if (!line.startsWith("X-PHONETIC")) {
                                                                if (line.startsWith("X-")) {
                                                                    e2 = new VcardItem();
                                                                    ((VcardItem)e2).type = 20;
                                                                    break Label_0700;
                                                                }
                                                            }
                                                            e2 = null;
                                                        }
                                                    }
                                                    else {
                                                        e2 = new VcardItem();
                                                        ((VcardItem)e2).type = 6;
                                                    }
                                                }
                                                else {
                                                    e2 = new VcardItem();
                                                    ((VcardItem)e2).type = 2;
                                                }
                                            }
                                            if (e2 != null && ((VcardItem)e2).type >= 0) {
                                                list.add((VcardItem)e2);
                                            }
                                            break Label_0731;
                                        }
                                    }
                                    e2 = null;
                                }
                            }
                            b2 = false;
                        }
                        if (!b2 && e != null) {
                            if (e2 == null) {
                                if (((VcardData)e).vcard.length() > 0) {
                                    ((VcardData)e).vcard.append('\n');
                                }
                                ((VcardData)e).vcard.append(line);
                            }
                            else {
                                ((VcardItem)e2).vcardData.add(line);
                            }
                        }
                        String s = (String)substring;
                        String string = line;
                        if (substring != null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append((String)substring);
                            sb.append(line);
                            string = sb.toString();
                            s = null;
                        }
                        if (string.contains("=QUOTED-PRINTABLE") && string.endsWith("=")) {
                            substring = string.substring(0, string.length() - 1);
                        }
                        else {
                            if (!b2 && e != null && e2 != null) {
                                ((VcardItem)e2).fullData = string;
                            }
                            final int index = string.indexOf(":");
                            String[] array;
                            if (index >= 0) {
                                array = new String[] { string.substring(0, index), string.substring(index + 1).trim() };
                            }
                            else {
                                array = new String[] { string.trim() };
                            }
                            if (array.length >= 2) {
                                if (e != null) {
                                    if (!array[0].startsWith("FN") && (!array[0].startsWith("ORG") || !TextUtils.isEmpty((CharSequence)((VcardData)e).name))) {
                                        if (array[0].startsWith("TEL")) {
                                            ((VcardData)e).phones.add(array[1]);
                                        }
                                    }
                                    else {
                                        final String[] split = array[0].split(";");
                                        final int length = split.length;
                                        int i = 0;
                                        String s2 = null;
                                        String charsetName = null;
                                        while (i < length) {
                                            final String[] split2 = split[i].split("=");
                                            String s3;
                                            if (split2.length != 2) {
                                                s3 = charsetName;
                                            }
                                            else if (split2[0].equals("CHARSET")) {
                                                s3 = split2[1];
                                            }
                                            else {
                                                s3 = charsetName;
                                                if (split2[0].equals("ENCODING")) {
                                                    s2 = split2[1];
                                                    s3 = charsetName;
                                                }
                                            }
                                            ++i;
                                            charsetName = s3;
                                        }
                                        ((VcardData)e).name = array[1];
                                        if (s2 != null && s2.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                                            final byte[] decodeQuotedPrintable = decodeQuotedPrintable(getStringBytes(((VcardData)e).name));
                                            if (decodeQuotedPrintable != null && decodeQuotedPrintable.length != 0) {
                                                ((VcardData)e).name = new String(decodeQuotedPrintable, charsetName);
                                            }
                                        }
                                    }
                                }
                            }
                            substring = s;
                        }
                    }
                }
                try {
                    bufferedReader.close();
                    in.close();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                int index2 = 0;
                ArrayList<TLRPC.TL_userContact_old2> list4 = null;
                while (true) {
                    final ArrayList<TLRPC.User> list5 = (ArrayList<TLRPC.User>)list4;
                    if (index2 >= list3.size()) {
                        return list5;
                    }
                    final VcardData vcardData = (VcardData)list3.get(index2);
                    if (vcardData.name != null && !vcardData.phones.isEmpty()) {
                        ArrayList<TLRPC.TL_userContact_old2> list6;
                        if ((list6 = list4) == null) {
                            list6 = new ArrayList<TLRPC.TL_userContact_old2>();
                        }
                        name = vcardData.phones.get(0);
                        int index3 = 0;
                        String phone;
                        while (true) {
                            phone = name;
                            if (index3 >= vcardData.phones.size()) {
                                break;
                            }
                            phone = vcardData.phones.get(index3);
                            if (ContactsController.getInstance(n).contactsByShortPhone.get(phone.substring(Math.max(0, phone.length() - 7))) != null) {
                                break;
                            }
                            ++index3;
                        }
                        final TLRPC.TL_userContact_old2 e3 = new TLRPC.TL_userContact_old2();
                        e3.phone = phone;
                        e3.first_name = vcardData.name;
                        e3.last_name = "";
                        e3.id = 0;
                        e3.restriction_reason = vcardData.vcard.toString();
                        list6.add(e3);
                        list4 = list6;
                    }
                    ++index2;
                }
            }
            catch (Throwable t) {
                list2 = null;
            }
        }
        final Throwable t;
        FileLog.e(t);
        return list2;
    }
    
    @SuppressLint({ "WrongConstant" })
    public static void lockOrientation(final Activity activity) {
        if (activity != null) {
            if (AndroidUtilities.prevOrientation == -10) {
                try {
                    AndroidUtilities.prevOrientation = activity.getRequestedOrientation();
                    final WindowManager windowManager = (WindowManager)activity.getSystemService("window");
                    if (windowManager != null && windowManager.getDefaultDisplay() != null) {
                        final int rotation = windowManager.getDefaultDisplay().getRotation();
                        final int orientation = activity.getResources().getConfiguration().orientation;
                        if (rotation == 3) {
                            if (orientation == 1) {
                                activity.setRequestedOrientation(1);
                            }
                            else {
                                activity.setRequestedOrientation(8);
                            }
                        }
                        else if (rotation == 1) {
                            if (orientation == 1) {
                                activity.setRequestedOrientation(9);
                            }
                            else {
                                activity.setRequestedOrientation(0);
                            }
                        }
                        else if (rotation == 0) {
                            if (orientation == 2) {
                                activity.setRequestedOrientation(0);
                            }
                            else {
                                activity.setRequestedOrientation(1);
                            }
                        }
                        else if (orientation == 2) {
                            activity.setRequestedOrientation(8);
                        }
                        else {
                            activity.setRequestedOrientation(9);
                        }
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
    }
    
    public static void makeAccessibilityAnnouncement(final CharSequence charSequence) {
        final AccessibilityManager accessibilityManager = (AccessibilityManager)ApplicationLoader.applicationContext.getSystemService("accessibility");
        if (accessibilityManager.isEnabled()) {
            final AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(16384);
            obtain.getText().add(charSequence);
            accessibilityManager.sendAccessibilityEvent(obtain);
        }
    }
    
    public static long makeBroadcastId(final int n) {
        return ((long)n & 0xFFFFFFFFL) | 0x100000000L;
    }
    
    public static boolean needShowPasscode(final boolean b) {
        final boolean wasInBackground = ForegroundDetector.getInstance().isWasInBackground(b);
        if (b) {
            ForegroundDetector.getInstance().resetBackgroundVar();
        }
        return SharedConfig.passcodeHash.length() > 0 && wasInBackground && (SharedConfig.appLocked || (SharedConfig.autoLockIn != 0 && SharedConfig.lastPauseTime != 0 && !SharedConfig.appLocked && SharedConfig.lastPauseTime + SharedConfig.autoLockIn <= ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime()) || ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() + 5 < SharedConfig.lastPauseTime);
    }
    
    public static void openDocument(final MessageObject messageObject, final Activity activity, final BaseFragment baseFragment) {
        if (messageObject == null) {
            return;
        }
        final TLRPC.Document document = messageObject.getDocument();
        if (document == null) {
            return;
        }
        String attachFileName;
        if (messageObject.messageOwner.media != null) {
            attachFileName = FileLoader.getAttachFileName(document);
        }
        else {
            attachFileName = "";
        }
        final String attachPath = messageObject.messageOwner.attachPath;
        File file;
        if (attachPath != null && attachPath.length() != 0) {
            file = new File(messageObject.messageOwner.attachPath);
        }
        else {
            file = null;
        }
        File pathToMessage = null;
        Label_0118: {
            if (file != null) {
                if ((pathToMessage = file) == null) {
                    break Label_0118;
                }
                pathToMessage = file;
                if (file.exists()) {
                    break Label_0118;
                }
            }
            pathToMessage = FileLoader.getPathToMessage(messageObject.messageOwner);
        }
        if (pathToMessage != null && pathToMessage.exists()) {
            if (baseFragment != null && pathToMessage.getName().toLowerCase().endsWith("attheme")) {
                final Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(pathToMessage, messageObject.getDocumentName(), true);
                if (applyThemeFile != null) {
                    baseFragment.presentFragment(new ThemePreviewActivity(pathToMessage, applyThemeFile));
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("IncorrectTheme", 2131559664));
                    builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    baseFragment.showDialog(builder.create());
                }
            }
            else {
                try {
                    final Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setFlags(1);
                    final MimeTypeMap singleton = MimeTypeMap.getSingleton();
                    final int lastIndex = attachFileName.lastIndexOf(46);
                    String mimeTypeFromExtension = null;
                    Label_0345: {
                        if (lastIndex != -1) {
                            mimeTypeFromExtension = singleton.getMimeTypeFromExtension(attachFileName.substring(lastIndex + 1).toLowerCase());
                            if (mimeTypeFromExtension != null) {
                                break Label_0345;
                            }
                            final String mime_type = document.mime_type;
                            if (mime_type != null) {
                                mimeTypeFromExtension = mime_type;
                                if (mime_type.length() != 0) {
                                    break Label_0345;
                                }
                            }
                        }
                        mimeTypeFromExtension = null;
                    }
                    if (Build$VERSION.SDK_INT >= 24) {
                        final Uri uriForFile = FileProvider.getUriForFile((Context)activity, "org.telegram.messenger.provider", pathToMessage);
                        String s;
                        if (mimeTypeFromExtension != null) {
                            s = mimeTypeFromExtension;
                        }
                        else {
                            s = "text/plain";
                        }
                        intent.setDataAndType(uriForFile, s);
                    }
                    else {
                        final Uri fromFile = Uri.fromFile(pathToMessage);
                        String s2;
                        if (mimeTypeFromExtension != null) {
                            s2 = mimeTypeFromExtension;
                        }
                        else {
                            s2 = "text/plain";
                        }
                        intent.setDataAndType(fromFile, s2);
                    }
                    if (mimeTypeFromExtension != null) {
                        try {
                            activity.startActivityForResult(intent, 500);
                        }
                        catch (Exception ex) {
                            if (Build$VERSION.SDK_INT >= 24) {
                                intent.setDataAndType(FileProvider.getUriForFile((Context)activity, "org.telegram.messenger.provider", pathToMessage), "text/plain");
                            }
                            else {
                                intent.setDataAndType(Uri.fromFile(pathToMessage), "text/plain");
                            }
                            activity.startActivityForResult(intent, 500);
                        }
                    }
                    else {
                        activity.startActivityForResult(intent, 500);
                    }
                }
                catch (Exception ex2) {
                    if (activity == null) {
                        return;
                    }
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)activity);
                    builder2.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder2.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    builder2.setMessage(LocaleController.formatString("NoHandleAppInstalled", 2131559926, messageObject.getDocument().mime_type));
                    if (baseFragment != null) {
                        baseFragment.showDialog(builder2.create());
                    }
                    else {
                        builder2.show();
                    }
                }
            }
        }
    }
    
    public static void openForView(final MessageObject messageObject, final Activity activity) {
        final String fileName = messageObject.getFileName();
        final String attachPath = messageObject.messageOwner.attachPath;
        File file;
        if (attachPath != null && attachPath.length() != 0) {
            file = new File(messageObject.messageOwner.attachPath);
        }
        else {
            file = null;
        }
        File pathToMessage = null;
        Label_0067: {
            if (file != null) {
                pathToMessage = file;
                if (file.exists()) {
                    break Label_0067;
                }
            }
            pathToMessage = FileLoader.getPathToMessage(messageObject.messageOwner);
        }
        if (pathToMessage != null && pathToMessage.exists()) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(1);
            final MimeTypeMap singleton = MimeTypeMap.getSingleton();
            final int lastIndex = fileName.lastIndexOf(46);
            String s2 = null;
            Label_0192: {
                if (lastIndex != -1) {
                    String s = singleton.getMimeTypeFromExtension(fileName.substring(lastIndex + 1).toLowerCase());
                    if (s != null) {
                        s2 = s;
                        break Label_0192;
                    }
                    final int type = messageObject.type;
                    if (type == 9 || type == 0) {
                        s = messageObject.getDocument().mime_type;
                    }
                    if (s != null) {
                        s2 = s;
                        if (s.length() != 0) {
                            break Label_0192;
                        }
                    }
                }
                s2 = null;
            }
            if (Build$VERSION.SDK_INT >= 26 && s2 != null && s2.equals("application/vnd.android.package-archive") && !ApplicationLoader.applicationContext.getPackageManager().canRequestPackageInstalls()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage(LocaleController.getString("ApkRestricted", 2131558634));
                builder.setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$AndroidUtilities$K8m_Yy_Pa5ZuhXPQCq3Vou7f1Z0(activity));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                builder.show();
                return;
            }
            if (Build$VERSION.SDK_INT >= 24) {
                final Uri uriForFile = FileProvider.getUriForFile((Context)activity, "org.telegram.messenger.provider", pathToMessage);
                String s3;
                if (s2 != null) {
                    s3 = s2;
                }
                else {
                    s3 = "text/plain";
                }
                intent.setDataAndType(uriForFile, s3);
            }
            else {
                final Uri fromFile = Uri.fromFile(pathToMessage);
                String s4;
                if (s2 != null) {
                    s4 = s2;
                }
                else {
                    s4 = "text/plain";
                }
                intent.setDataAndType(fromFile, s4);
            }
            if (s2 != null) {
                try {
                    activity.startActivityForResult(intent, 500);
                }
                catch (Exception ex) {
                    if (Build$VERSION.SDK_INT >= 24) {
                        intent.setDataAndType(FileProvider.getUriForFile((Context)activity, "org.telegram.messenger.provider", pathToMessage), "text/plain");
                    }
                    else {
                        intent.setDataAndType(Uri.fromFile(pathToMessage), "text/plain");
                    }
                    activity.startActivityForResult(intent, 500);
                }
            }
            else {
                activity.startActivityForResult(intent, 500);
            }
        }
    }
    
    public static void openForView(final TLObject tlObject, final Activity activity) {
        if (tlObject != null) {
            if (activity != null) {
                final String attachFileName = FileLoader.getAttachFileName(tlObject);
                final File pathToAttach = FileLoader.getPathToAttach(tlObject, true);
                if (pathToAttach != null && pathToAttach.exists()) {
                    final Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setFlags(1);
                    final MimeTypeMap singleton = MimeTypeMap.getSingleton();
                    final int lastIndex = attachFileName.lastIndexOf(46);
                    String mimeTypeFromExtension;
                    final String s = mimeTypeFromExtension = null;
                    if (lastIndex != -1) {
                        mimeTypeFromExtension = singleton.getMimeTypeFromExtension(attachFileName.substring(lastIndex + 1).toLowerCase());
                        if (mimeTypeFromExtension == null) {
                            String mime_type;
                            if (tlObject instanceof TLRPC.TL_document) {
                                mime_type = ((TLRPC.TL_document)tlObject).mime_type;
                            }
                            else {
                                mime_type = mimeTypeFromExtension;
                            }
                            mimeTypeFromExtension = s;
                            if (mime_type != null) {
                                if (mime_type.length() == 0) {
                                    mimeTypeFromExtension = s;
                                }
                                else {
                                    mimeTypeFromExtension = mime_type;
                                }
                            }
                        }
                    }
                    if (Build$VERSION.SDK_INT >= 24) {
                        final Uri uriForFile = FileProvider.getUriForFile((Context)activity, "org.telegram.messenger.provider", pathToAttach);
                        String s2;
                        if (mimeTypeFromExtension != null) {
                            s2 = mimeTypeFromExtension;
                        }
                        else {
                            s2 = "text/plain";
                        }
                        intent.setDataAndType(uriForFile, s2);
                    }
                    else {
                        final Uri fromFile = Uri.fromFile(pathToAttach);
                        String s3;
                        if (mimeTypeFromExtension != null) {
                            s3 = mimeTypeFromExtension;
                        }
                        else {
                            s3 = "text/plain";
                        }
                        intent.setDataAndType(fromFile, s3);
                    }
                    if (mimeTypeFromExtension != null) {
                        try {
                            activity.startActivityForResult(intent, 500);
                        }
                        catch (Exception ex) {
                            if (Build$VERSION.SDK_INT >= 24) {
                                intent.setDataAndType(FileProvider.getUriForFile((Context)activity, "org.telegram.messenger.provider", pathToAttach), "text/plain");
                            }
                            else {
                                intent.setDataAndType(Uri.fromFile(pathToAttach), "text/plain");
                            }
                            activity.startActivityForResult(intent, 500);
                        }
                    }
                    else {
                        activity.startActivityForResult(intent, 500);
                    }
                }
            }
        }
    }
    
    public static void removeAdjustResize(final Activity activity, final int n) {
        if (activity != null) {
            if (!isTablet()) {
                if (AndroidUtilities.adjustOwnerClassGuid == n) {
                    activity.getWindow().setSoftInputMode(32);
                }
            }
        }
    }
    
    public static SpannableStringBuilder replaceTags(final String s) {
        return replaceTags(s, 11, new Object[0]);
    }
    
    public static SpannableStringBuilder replaceTags(final String str, int i, final Object... array) {
        try {
            final StringBuilder sb = new StringBuilder(str);
            if ((i & 0x1) != 0x0) {
                while (true) {
                    final int index = sb.indexOf("<br>");
                    if (index == -1) {
                        break;
                    }
                    sb.replace(index, index + 4, "\n");
                }
                while (true) {
                    final int index2 = sb.indexOf("<br/>");
                    if (index2 == -1) {
                        break;
                    }
                    sb.replace(index2, index2 + 5, "\n");
                }
            }
            final ArrayList<Integer> list = new ArrayList<Integer>();
            if ((i & 0x2) != 0x0) {
                while (true) {
                    final int index3 = sb.indexOf("<b>");
                    if (index3 == -1) {
                        break;
                    }
                    sb.replace(index3, index3 + 3, "");
                    int n;
                    if ((n = sb.indexOf("</b>")) == -1) {
                        n = sb.indexOf("<b>");
                    }
                    sb.replace(n, n + 4, "");
                    list.add(index3);
                    list.add(n);
                }
                while (true) {
                    final int index4 = sb.indexOf("**");
                    if (index4 == -1) {
                        break;
                    }
                    sb.replace(index4, index4 + 2, "");
                    final int index5 = sb.indexOf("**");
                    if (index5 < 0) {
                        continue;
                    }
                    sb.replace(index5, index5 + 2, "");
                    list.add(index4);
                    list.add(index5);
                }
            }
            if ((i & 0x8) != 0x0) {
                while (true) {
                    final int index6 = sb.indexOf("**");
                    if (index6 == -1) {
                        break;
                    }
                    sb.replace(index6, index6 + 2, "");
                    i = sb.indexOf("**");
                    if (i < 0) {
                        continue;
                    }
                    sb.replace(i, i + 2, "");
                    list.add(index6);
                    list.add(i);
                }
            }
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)sb);
            TypefaceSpan typefaceSpan;
            int index7;
            for (i = 0; i < list.size() / 2; ++i) {
                typefaceSpan = new TypefaceSpan(getTypeface("fonts/rmedium.ttf"));
                index7 = i * 2;
                spannableStringBuilder.setSpan((Object)typefaceSpan, (int)list.get(index7), (int)list.get(index7 + 1), 33);
            }
            return spannableStringBuilder;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new SpannableStringBuilder((CharSequence)str);
        }
    }
    
    public static void requestAdjustResize(final Activity activity, final int adjustOwnerClassGuid) {
        if (activity != null) {
            if (!isTablet()) {
                activity.getWindow().setSoftInputMode(16);
                AndroidUtilities.adjustOwnerClassGuid = adjustOwnerClassGuid;
            }
        }
    }
    
    private static double[] rgbToHsv(int n, final int n2, final int n3) {
        final double v = n;
        Double.isNaN(v);
        final double n4 = v / 255.0;
        final double v2 = n2;
        Double.isNaN(v2);
        final double n5 = v2 / 255.0;
        final double v3 = n3;
        Double.isNaN(v3);
        final double n6 = v3 / 255.0;
        double n7;
        if (n4 > n5 && n4 > n6) {
            n7 = n4;
        }
        else if (n5 > n6) {
            n7 = n5;
        }
        else {
            n7 = n6;
        }
        double n8;
        if (n4 < n5 && n4 < n6) {
            n8 = n4;
        }
        else if (n5 < n6) {
            n8 = n5;
        }
        else {
            n8 = n6;
        }
        final double n9 = n7 - n8;
        final double n10 = 0.0;
        double n11;
        if (n7 == 0.0) {
            n11 = 0.0;
        }
        else {
            n11 = n9 / n7;
        }
        double n12;
        if (n7 == n8) {
            n12 = n10;
        }
        else {
            double n14 = 0.0;
            Label_0269: {
                double n13;
                double v4;
                if (n4 > n5 && n4 > n6) {
                    n13 = (n5 - n6) / n9;
                    if (n5 < n6) {
                        n = 6;
                    }
                    else {
                        n = 0;
                    }
                    v4 = n;
                    Double.isNaN(v4);
                }
                else {
                    if (n5 > n6) {
                        n14 = 2.0 + (n6 - n4) / n9;
                        break Label_0269;
                    }
                    n13 = (n4 - n5) / n9;
                    v4 = 4.0;
                }
                n14 = n13 + v4;
            }
            n12 = n14 / 6.0;
        }
        return new double[] { n12, n11, n7 };
    }
    
    public static void runOnUIThread(final Runnable runnable) {
        runOnUIThread(runnable, 0L);
    }
    
    public static void runOnUIThread(final Runnable runnable, final long n) {
        if (n == 0L) {
            ApplicationLoader.applicationHandler.post(runnable);
        }
        else {
            ApplicationLoader.applicationHandler.postDelayed(runnable, n);
        }
    }
    
    public static void setAdjustResizeToNothing(final Activity activity, final int n) {
        if (activity != null) {
            if (!isTablet()) {
                if (AndroidUtilities.adjustOwnerClassGuid == n) {
                    activity.getWindow().setSoftInputMode(48);
                }
            }
        }
    }
    
    public static void setEnabled(final View view, final boolean enabled) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                setEnabled(viewGroup.getChildAt(i), enabled);
            }
        }
    }
    
    public static int setMyLayerVersion(final int n, final int n2) {
        return (n & 0xFFFF0000) | n2;
    }
    
    public static int setPeerLayerVersion(final int n, final int n2) {
        return (n & 0xFFFF) | n2 << 16;
    }
    
    public static void setRectToRect(final Matrix matrix, final RectF rectF, final RectF rectF2, final int n, final boolean b) {
        float n2;
        float n3;
        float n4;
        if (n != 90 && n != 270) {
            n2 = rectF2.width() / rectF.width();
            n3 = rectF2.height();
            n4 = rectF.height();
        }
        else {
            n2 = rectF2.height() / rectF.width();
            n3 = rectF2.width();
            n4 = rectF.height();
        }
        float n5 = n3 / n4;
        boolean b2;
        if (n2 < n5) {
            b2 = true;
        }
        else {
            b2 = false;
            n5 = n2;
        }
        if (b) {
            matrix.setTranslate(rectF2.left, rectF2.top);
        }
        if (n == 90) {
            matrix.preRotate(90.0f);
            matrix.preTranslate(0.0f, -rectF2.width());
        }
        else if (n == 180) {
            matrix.preRotate(180.0f);
            matrix.preTranslate(-rectF2.width(), -rectF2.height());
        }
        else if (n == 270) {
            matrix.preRotate(270.0f);
            matrix.preTranslate(-rectF2.height(), 0.0f);
        }
        float n6;
        float n7;
        if (b) {
            n6 = -rectF.left * n5;
            n7 = -rectF.top * n5;
        }
        else {
            n6 = rectF2.left - rectF.left * n5;
            n7 = rectF2.top - rectF.top * n5;
        }
        float n8;
        float n9;
        if (b2) {
            n8 = rectF2.width();
            n9 = rectF.width();
        }
        else {
            n8 = rectF2.height();
            n9 = rectF.height();
        }
        final float n10 = (n8 - n9 * n5) / 2.0f;
        if (b2) {
            n6 += n10;
        }
        else {
            n7 += n10;
        }
        matrix.preScale(n5, n5);
        if (b) {
            matrix.preTranslate(n6, n7);
        }
    }
    
    public static void setScrollViewEdgeEffectColor(final HorizontalScrollView horizontalScrollView, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                final Field declaredField = HorizontalScrollView.class.getDeclaredField("mEdgeGlowLeft");
                declaredField.setAccessible(true);
                final EdgeEffect edgeEffect = (EdgeEffect)declaredField.get(horizontalScrollView);
                if (edgeEffect != null) {
                    edgeEffect.setColor(n);
                }
                final Field declaredField2 = HorizontalScrollView.class.getDeclaredField("mEdgeGlowRight");
                declaredField2.setAccessible(true);
                final EdgeEffect edgeEffect2 = (EdgeEffect)declaredField2.get(horizontalScrollView);
                if (edgeEffect2 != null) {
                    edgeEffect2.setColor(n);
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    public static void setScrollViewEdgeEffectColor(final ScrollView scrollView, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                final Field declaredField = ScrollView.class.getDeclaredField("mEdgeGlowTop");
                declaredField.setAccessible(true);
                final EdgeEffect edgeEffect = (EdgeEffect)declaredField.get(scrollView);
                if (edgeEffect != null) {
                    edgeEffect.setColor(n);
                }
                final Field declaredField2 = ScrollView.class.getDeclaredField("mEdgeGlowBottom");
                declaredField2.setAccessible(true);
                final EdgeEffect edgeEffect2 = (EdgeEffect)declaredField2.get(scrollView);
                if (edgeEffect2 != null) {
                    edgeEffect2.setColor(n);
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    public static void setViewPagerEdgeEffectColor(final ViewPager viewPager, final int n) {
        if (Build$VERSION.SDK_INT < 21) {
            return;
        }
        try {
            final Field declaredField = ViewPager.class.getDeclaredField("mLeftEdge");
            declaredField.setAccessible(true);
            final EdgeEffect edgeEffect = (EdgeEffect)declaredField.get(viewPager);
            if (edgeEffect != null) {
                edgeEffect.setColor(n);
            }
            final Field declaredField2 = ViewPager.class.getDeclaredField("mRightEdge");
            declaredField2.setAccessible(true);
            final EdgeEffect edgeEffect2 = (EdgeEffect)declaredField2.get(viewPager);
            if (edgeEffect2 != null) {
                edgeEffect2.setColor(n);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void setWaitingForCall(final boolean waitingForCall) {
        final Object callLock = AndroidUtilities.callLock;
        // monitorenter(callLock)
        Label_0057: {
            if (!waitingForCall) {
                break Label_0057;
            }
            while (true) {
                try {
                    try {
                        if (AndroidUtilities.callReceiver == null) {
                            ApplicationLoader.applicationContext.registerReceiver((BroadcastReceiver)(AndroidUtilities.callReceiver = new CallReceiver()), new IntentFilter("android.intent.action.PHONE_STATE"));
                        }
                        break Label_0084;
                        // iftrue(Label_0084:, AndroidUtilities.callReceiver == null)
                        ApplicationLoader.applicationContext.unregisterReceiver((BroadcastReceiver)AndroidUtilities.callReceiver);
                        AndroidUtilities.callReceiver = null;
                    }
                    finally {
                        // monitorexit(callLock)
                        AndroidUtilities.waitingForCall = waitingForCall;
                    }
                    // monitorexit(callLock)
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public static void setWaitingForSms(final boolean b) {
    }
    
    public static void shakeView(final View view, final float n, final int n2) {
        if (view == null) {
            return;
        }
        if (n2 == 6) {
            view.setTranslationX(0.0f);
            return;
        }
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)view, "translationX", new float[] { (float)dp(n) }) });
        set.setDuration(50L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                final View val$view = view;
                float n;
                if (n2 == 5) {
                    n = 0.0f;
                }
                else {
                    n = -n;
                }
                AndroidUtilities.shakeView(val$view, n, n2 + 1);
            }
        });
        set.start();
    }
    
    public static boolean shouldEnableAnimation() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 26) {
            if (sdk_INT < 28) {
                if (((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).isPowerSaveMode()) {
                    return false;
                }
                if (Settings$Global.getFloat(ApplicationLoader.applicationContext.getContentResolver(), "animator_duration_scale", 1.0f) <= 0.0f) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean showKeyboard(final View view) {
        if (view == null) {
            return false;
        }
        try {
            return ((InputMethodManager)view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return false;
        }
    }
    
    public static void showProxyAlert(final Activity activity, final String s, final String str, final String s2, final String s3, final String s4) {
        final BottomSheet.Builder builder = new BottomSheet.Builder((Context)activity);
        final Runnable dismissRunnable = builder.getDismissRunnable();
        builder.setApplyTopPadding(false);
        builder.setApplyBottomPadding(false);
        final LinearLayout customView = new LinearLayout((Context)activity);
        builder.setCustomView((View)customView);
        customView.setOrientation(1);
        if (!TextUtils.isEmpty((CharSequence)s4)) {
            final TextView textView = new TextView((Context)activity);
            textView.setText((CharSequence)LocaleController.getString("UseProxyTelegramInfo2", 2131560985));
            textView.setTextColor(Theme.getColor("dialogTextGray4"));
            textView.setTextSize(1, 14.0f);
            textView.setGravity(49);
            int n;
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            customView.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n | 0x30, 17, 8, 17, 8));
            final View view = new View((Context)activity);
            view.setBackgroundColor(Theme.getColor("divider"));
            customView.addView(view, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, 1));
        }
        for (int i = 0; i < 5; ++i) {
            String string = null;
            String s5;
            if (i == 0) {
                s5 = LocaleController.getString("UseProxyAddress", 2131560971);
                string = s;
            }
            else if (i == 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(str);
                string = sb.toString();
                s5 = LocaleController.getString("UseProxyPort", 2131560976);
            }
            else if (i == 2) {
                s5 = LocaleController.getString("UseProxySecret", 2131560977);
                string = s4;
            }
            else if (i == 3) {
                s5 = LocaleController.getString("UseProxyUsername", 2131560986);
                string = s2;
            }
            else if (i == 4) {
                s5 = LocaleController.getString("UseProxyPassword", 2131560975);
                string = s3;
            }
            else {
                s5 = null;
            }
            if (!TextUtils.isEmpty((CharSequence)string)) {
                final TextDetailSettingsCell textDetailSettingsCell = new TextDetailSettingsCell((Context)activity);
                textDetailSettingsCell.setTextAndValue(string, s5, true);
                textDetailSettingsCell.getTextView().setTextColor(Theme.getColor("dialogTextBlack"));
                textDetailSettingsCell.getValueTextView().setTextColor(Theme.getColor("dialogTextGray3"));
                customView.addView((View)textDetailSettingsCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                if (i == 2) {
                    break;
                }
            }
        }
        final PickerBottomLayout pickerBottomLayout = new PickerBottomLayout((Context)activity, false);
        pickerBottomLayout.setBackgroundColor(Theme.getColor("dialogBackground"));
        customView.addView((View)pickerBottomLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        pickerBottomLayout.cancelButton.setPadding(dp(18.0f), 0, dp(18.0f), 0);
        pickerBottomLayout.cancelButton.setTextColor(Theme.getColor("dialogTextBlue2"));
        pickerBottomLayout.cancelButton.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        pickerBottomLayout.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AndroidUtilities$0wQAWULSlGGvo31HEQI8eN6AXOQ(dismissRunnable));
        pickerBottomLayout.doneButtonTextView.setTextColor(Theme.getColor("dialogTextBlue2"));
        pickerBottomLayout.doneButton.setPadding(dp(18.0f), 0, dp(18.0f), 0);
        pickerBottomLayout.doneButtonBadgeTextView.setVisibility(8);
        pickerBottomLayout.doneButtonTextView.setText((CharSequence)LocaleController.getString("ConnectingConnectProxy", 2131559138).toUpperCase());
        pickerBottomLayout.doneButton.setOnClickListener((View$OnClickListener)new _$$Lambda$AndroidUtilities$N3FZjh44dp7TBj2YD5HgSuaQoW4(s, str, s4, s3, s2, dismissRunnable));
        builder.show();
    }
    
    public static int[] toIntArray(final List<Integer> list) {
        final int[] array = new int[list.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    @SuppressLint({ "WrongConstant" })
    public static void unlockOrientation(final Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            if (AndroidUtilities.prevOrientation != -10) {
                activity.setRequestedOrientation(AndroidUtilities.prevOrientation);
                AndroidUtilities.prevOrientation = -10;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void unregisterUpdates() {
    }
    
    public static class LinkMovementMethodMy extends LinkMovementMethod
    {
        public boolean onTouchEvent(final TextView textView, final Spannable spannable, final MotionEvent motionEvent) {
            try {
                final boolean onTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    Selection.removeSelection(spannable);
                }
                return onTouchEvent;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return false;
            }
        }
    }
    
    private static class VcardData
    {
        String name;
        ArrayList<String> phones;
        StringBuilder vcard;
        
        private VcardData() {
            this.phones = new ArrayList<String>();
            this.vcard = new StringBuilder();
        }
    }
    
    public static class VcardItem
    {
        public boolean checked;
        public String fullData;
        public int type;
        public ArrayList<String> vcardData;
        
        public VcardItem() {
            this.vcardData = new ArrayList<String>();
            this.fullData = "";
            this.checked = true;
        }
        
        public String getRawType(final boolean b) {
            final int index = this.fullData.indexOf(58);
            String s = "";
            if (index < 0) {
                return "";
            }
            final String fullData = this.fullData;
            int i = 0;
            final String substring = fullData.substring(0, index);
            if (this.type == 20) {
                final String[] split = substring.substring(2).split(";");
                if (b) {
                    s = split[0];
                }
                else if (split.length > 1) {
                    s = split[split.length - 1];
                }
                return s;
            }
            final String[] split2 = substring.split(";");
            String s2 = substring;
            while (i < split2.length) {
                if (split2[i].indexOf(61) < 0) {
                    s2 = split2[i];
                }
                ++i;
            }
            return s2;
        }
        
        public String[] getRawValue() {
            final int index = this.fullData.indexOf(58);
            final int n = 0;
            if (index < 0) {
                return new String[0];
            }
            final String substring = this.fullData.substring(0, index);
            final String substring2 = this.fullData.substring(index + 1);
            final String[] split = substring.split(";");
            String charsetName = "UTF-8";
            String s = null;
            String s2;
            for (int i = 0; i < split.length; ++i, charsetName = s2) {
                final String[] split2 = split[i].split("=");
                if (split2.length != 2) {
                    s2 = charsetName;
                }
                else if (split2[0].equals("CHARSET")) {
                    s2 = split2[1];
                }
                else {
                    s2 = charsetName;
                    if (split2[0].equals("ENCODING")) {
                        s = split2[1];
                        s2 = charsetName;
                    }
                }
            }
            final String[] split3 = substring2.split(";");
            int n2 = n;
        Label_0232_Outer:
            while (true) {
                if (n2 >= split3.length) {
                    return split3;
                }
                while (true) {
                    if (TextUtils.isEmpty((CharSequence)split3[n2])) {
                        break Label_0232;
                    }
                    if (s == null || !s.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                        break Label_0232;
                    }
                    final byte[] decodeQuotedPrintable = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(split3[n2]));
                    if (decodeQuotedPrintable == null || decodeQuotedPrintable.length == 0) {
                        break Label_0232;
                    }
                    try {
                        split3[n2] = new String(decodeQuotedPrintable, charsetName);
                        ++n2;
                        continue Label_0232_Outer;
                    }
                    catch (Exception ex) {
                        continue;
                    }
                    break;
                }
            }
        }
        
        public String getType() {
            final int type = this.type;
            if (type == 5) {
                return LocaleController.getString("ContactBirthday", 2131559141);
            }
            if (type == 6) {
                if ("ORG".equalsIgnoreCase(this.getRawType(true))) {
                    return LocaleController.getString("ContactJob", 2131559142);
                }
                return LocaleController.getString("ContactJobTitle", 2131559143);
            }
            else {
                final int index = this.fullData.indexOf(58);
                if (index < 0) {
                    return "";
                }
                String s = this.fullData.substring(0, index);
                if (this.type == 20) {
                    s = s.substring(2).split(";")[0];
                }
                else {
                    final String[] split = s.split(";");
                    for (int i = 0; i < split.length; ++i) {
                        if (split[i].indexOf(61) < 0) {
                            s = split[i];
                        }
                    }
                    if (s.startsWith("X-")) {
                        s = s.substring(2);
                    }
                    int n = -1;
                    switch (s.hashCode()) {
                        case 75532016: {
                            if (s.equals("OTHER")) {
                                n = 4;
                                break;
                            }
                            break;
                        }
                        case 2670353: {
                            if (s.equals("WORK")) {
                                n = 5;
                                break;
                            }
                            break;
                        }
                        case 2464291: {
                            if (s.equals("PREF")) {
                                n = 0;
                                break;
                            }
                            break;
                        }
                        case 2223327: {
                            if (s.equals("HOME")) {
                                n = 1;
                                break;
                            }
                            break;
                        }
                        case 2064738: {
                            if (s.equals("CELL")) {
                                n = 3;
                                break;
                            }
                            break;
                        }
                        case -2015525726: {
                            if (s.equals("MOBILE")) {
                                n = 2;
                                break;
                            }
                            break;
                        }
                    }
                    if (n != 0) {
                        if (n != 1) {
                            if (n != 2 && n != 3) {
                                if (n != 4) {
                                    if (n == 5) {
                                        s = LocaleController.getString("PhoneWork", 2131560433);
                                    }
                                }
                                else {
                                    s = LocaleController.getString("PhoneOther", 2131560432);
                                }
                            }
                            else {
                                s = LocaleController.getString("PhoneMobile", 2131560427);
                            }
                        }
                        else {
                            s = LocaleController.getString("PhoneHome", 2131560425);
                        }
                    }
                    else {
                        s = LocaleController.getString("PhoneMain", 2131560426);
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(s.substring(0, 1).toUpperCase());
                sb.append(s.substring(1).toLowerCase());
                return sb.toString();
            }
        }
        
        public String getValue(final boolean b) {
            final StringBuilder sb = new StringBuilder();
            final int index = this.fullData.indexOf(58);
            if (index < 0) {
                return "";
            }
            if (sb.length() > 0) {
                sb.append(", ");
            }
            final String substring = this.fullData.substring(0, index);
            final String substring2 = this.fullData.substring(index + 1);
            final String[] split = substring.split(";");
            String charsetName = "UTF-8";
            String s = null;
            String s2;
            for (int i = 0; i < split.length; ++i, charsetName = s2) {
                final String[] split2 = split[i].split("=");
                if (split2.length != 2) {
                    s2 = charsetName;
                }
                else if (split2[0].equals("CHARSET")) {
                    s2 = split2[1];
                }
                else {
                    s2 = charsetName;
                    if (split2[0].equals("ENCODING")) {
                        s = split2[1];
                        s2 = charsetName;
                    }
                }
            }
            final String[] split3 = substring2.split(";");
            int j = 0;
            int n = 0;
            while (j < split3.length) {
                int n2;
                if (TextUtils.isEmpty((CharSequence)split3[j])) {
                    n2 = n;
                }
                else {
                    if (s != null && s.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                        final byte[] decodeQuotedPrintable = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(split3[j]));
                        if (decodeQuotedPrintable != null && decodeQuotedPrintable.length != 0) {
                            try {
                                split3[j] = new String(decodeQuotedPrintable, charsetName);
                            }
                            catch (Exception ex) {}
                        }
                    }
                    if (n != 0 && sb.length() > 0) {
                        sb.append(" ");
                    }
                    sb.append(split3[j]);
                    if ((n2 = n) == 0) {
                        if (split3[j].length() > 0) {
                            n2 = 1;
                        }
                        else {
                            n2 = 0;
                        }
                    }
                }
                ++j;
                n = n2;
            }
            if (b) {
                final int type = this.type;
                if (type == 0) {
                    return PhoneFormat.getInstance().format(sb.toString());
                }
                if (type == 5) {
                    final String[] split4 = sb.toString().split("T");
                    if (split4.length > 0) {
                        final String[] split5 = split4[0].split("-");
                        if (split5.length == 3) {
                            final Calendar instance = Calendar.getInstance();
                            instance.set(1, Utilities.parseInt(split5[0]));
                            instance.set(2, Utilities.parseInt(split5[1]) - 1);
                            instance.set(5, Utilities.parseInt(split5[2]));
                            return LocaleController.getInstance().formatterYearMax.format(instance.getTime());
                        }
                    }
                }
            }
            return sb.toString();
        }
    }
}
