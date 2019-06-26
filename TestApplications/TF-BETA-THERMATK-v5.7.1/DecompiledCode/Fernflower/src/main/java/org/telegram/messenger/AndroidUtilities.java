package org.telegram.messenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.webkit.MimeTypeMap;
import android.widget.EdgeEffect;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import com.android.internal.telephony.ITelephony;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.ui.WallpapersListActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Components.ForegroundDetector;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Components.TypefaceSpan;

public class AndroidUtilities {
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
   private static int prevOrientation = -10;
   public static int roundMessageSize;
   private static Paint roundPaint;
   private static final Object smsLock;
   public static int statusBarHeight;
   private static final Hashtable typefaceCache = new Hashtable();
   public static boolean usingHardwareInput;
   private static boolean waitingForCall;
   private static boolean waitingForSms;

   static {
      boolean var0 = false;
      waitingForSms = false;
      waitingForCall = false;
      smsLock = new Object();
      callLock = new Object();
      statusBarHeight = 0;
      density = 1.0F;
      displaySize = new Point();
      photoSize = null;
      displayMetrics = new DisplayMetrics();
      decelerateInterpolator = new DecelerateInterpolator();
      accelerateInterpolator = new AccelerateInterpolator();
      overshootInterpolator = new OvershootInterpolator();
      isTablet = null;
      adjustOwnerClassGuid = 0;
      WEB_URL = null;

      try {
         Pattern var1 = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))");
         StringBuilder var2 = new StringBuilder();
         var2.append("(([a-zA-Z0-9 -\ud7ff豈-\ufdcfﷰ-\uffef]([a-zA-Z0-9 -\ud7ff豈-\ufdcfﷰ-\uffef\\-]{0,61}[a-zA-Z0-9 -\ud7ff豈-\ufdcfﷰ-\uffef]){0,1}\\.)+[a-zA-Z -\ud7ff豈-\ufdcfﷰ-\uffef]{2,63}|");
         var2.append(var1);
         var2.append(")");
         Pattern var6 = Pattern.compile(var2.toString());
         StringBuilder var5 = new StringBuilder();
         var5.append("((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?(?:");
         var5.append(var6);
         var5.append(")(?:\\:\\d{1,5})?)(\\/(?:(?:[");
         var5.append("a-zA-Z0-9 -\ud7ff豈-\ufdcfﷰ-\uffef");
         var5.append("\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)");
         WEB_URL = Pattern.compile(var5.toString());
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      byte var3;
      if (isTablet()) {
         var3 = 80;
      } else {
         var3 = 72;
      }

      leftBaseline = var3;
      checkDisplaySize(ApplicationLoader.applicationContext, (Configuration)null);
      documentIcons = new int[]{2131165554, 2131165556, 2131165560, 2131165562};
      documentMediaIcons = new int[]{2131165555, 2131165557, 2131165561, 2131165563};
      if (VERSION.SDK_INT >= 23) {
         var0 = true;
      }

      hasCallPermissions = var0;
   }

   public static int HSBtoRGB(float var0, float var1, float var2) {
      int var3 = 0;
      int var4;
      int var5;
      if (var1 == 0.0F) {
         var3 = (int)(var2 * 255.0F + 0.5F);
         var4 = var3;
         var5 = var3;
      } else {
         var0 = (var0 - (float)Math.floor((double)var0)) * 6.0F;
         float var6 = var0 - (float)Math.floor((double)var0);
         float var7 = (1.0F - var1) * var2;
         float var8 = (1.0F - var1 * var6) * var2;
         var1 = (1.0F - var1 * (1.0F - var6)) * var2;
         var4 = (int)var0;
         if (var4 != 0) {
            if (var4 != 1) {
               if (var4 != 2) {
                  if (var4 != 3) {
                     if (var4 != 4) {
                        if (var4 != 5) {
                           var4 = 0;
                           var5 = 0;
                        } else {
                           var3 = (int)(var2 * 255.0F + 0.5F);
                           var4 = (int)(var7 * 255.0F + 0.5F);
                           var5 = (int)(var8 * 255.0F + 0.5F);
                        }

                        return (var4 & 255) << 8 | -16777216 | (var3 & 255) << 16 | var5 & 255;
                     }

                     var3 = (int)(var1 * 255.0F + 0.5F);
                     var4 = (int)(var7 * 255.0F + 0.5F);
                  } else {
                     var3 = (int)(var7 * 255.0F + 0.5F);
                     var4 = (int)(var8 * 255.0F + 0.5F);
                  }

                  var5 = (int)(var2 * 255.0F + 0.5F);
               } else {
                  var3 = (int)(var7 * 255.0F + 0.5F);
                  var4 = (int)(var2 * 255.0F + 0.5F);
                  var5 = (int)(var1 * 255.0F + 0.5F);
               }

               return (var4 & 255) << 8 | -16777216 | (var3 & 255) << 16 | var5 & 255;
            }

            var3 = (int)(var8 * 255.0F + 0.5F);
            var4 = (int)(var2 * 255.0F + 0.5F);
         } else {
            var3 = (int)(var2 * 255.0F + 0.5F);
            var4 = (int)(var1 * 255.0F + 0.5F);
         }

         var5 = (int)(var7 * 255.0F + 0.5F);
      }

      return (var4 & 255) << 8 | -16777216 | (var3 & 255) << 16 | var5 & 255;
   }

   public static float[] RGBtoHSB(int var0, int var1, int var2) {
      int var3;
      if (var0 > var1) {
         var3 = var0;
      } else {
         var3 = var1;
      }

      int var4 = var3;
      if (var2 > var3) {
         var4 = var2;
      }

      if (var0 < var1) {
         var3 = var0;
      } else {
         var3 = var1;
      }

      int var5 = var3;
      if (var2 < var3) {
         var5 = var2;
      }

      float var6 = (float)var4;
      float var7 = var6 / 255.0F;
      float var8 = 0.0F;
      if (var4 != 0) {
         var6 = (float)(var4 - var5) / var6;
      } else {
         var6 = 0.0F;
      }

      if (var6 != 0.0F) {
         float var9 = (float)(var4 - var0);
         var8 = (float)(var4 - var5);
         float var10 = var9 / var8;
         var9 = (float)(var4 - var1) / var8;
         var8 = (float)(var4 - var2) / var8;
         if (var0 == var4) {
            var8 -= var9;
         } else if (var1 == var4) {
            var8 = var10 + 2.0F - var8;
         } else {
            var8 = var9 + 4.0F - var10;
         }

         var8 /= 6.0F;
         if (var8 < 0.0F) {
            ++var8;
         }
      }

      return new float[]{var8, var6, var7};
   }

   public static void addMediaToGallery(Uri var0) {
      if (var0 != null) {
         try {
            Intent var1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            var1.setData(var0);
            ApplicationLoader.applicationContext.sendBroadcast(var1);
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }
   }

   public static void addMediaToGallery(String var0) {
      if (var0 != null) {
         addMediaToGallery(Uri.fromFile(new File(var0)));
      }
   }

   public static void addToClipboard(CharSequence var0) {
      try {
         ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", var0));
      } catch (Exception var1) {
         FileLog.e((Throwable)var1);
      }

   }

   public static byte[] calcAuthKeyHash(byte[] var0) {
      var0 = Utilities.computeSHA1(var0);
      byte[] var1 = new byte[16];
      System.arraycopy(var0, 0, var1, 0, 16);
      return var1;
   }

   public static int[] calcDrawableColor(Drawable var0) {
      int var1 = -16777216;
      int var2 = var1;

      int var3;
      label77: {
         Exception var10000;
         label81: {
            boolean var10001;
            label82: {
               try {
                  if (!(var0 instanceof BitmapDrawable)) {
                     break label82;
                  }
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label81;
               }

               var2 = var1;

               Bitmap var14;
               try {
                  var14 = ((BitmapDrawable)var0).getBitmap();
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label81;
               }

               var3 = var1;
               if (var14 == null) {
                  break label77;
               }

               var2 = var1;

               Bitmap var4;
               try {
                  var4 = Bitmaps.createScaledBitmap(var14, 1, 1, true);
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label81;
               }

               var3 = var1;
               if (var4 == null) {
                  break label77;
               }

               var2 = var1;

               try {
                  var1 = var4.getPixel(0, 0);
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label81;
               }

               var3 = var1;
               if (var14 == var4) {
                  break label77;
               }

               var2 = var1;

               try {
                  var4.recycle();
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label81;
               }

               var3 = var1;
               break label77;
            }

            var2 = var1;
            var3 = var1;

            try {
               if (!(var0 instanceof ColorDrawable)) {
                  break label77;
               }
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label81;
            }

            var2 = var1;

            try {
               var3 = ((ColorDrawable)var0).getColor();
               break label77;
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
            }
         }

         Exception var15 = var10000;
         FileLog.e((Throwable)var15);
         var3 = var2;
      }

      double[] var17 = rgbToHsv(var3 >> 16 & 255, var3 >> 8 & 255, var3 & 255);
      var17[1] = Math.min(1.0D, var17[1] + 0.05D + (1.0D - var17[1]) * 0.1D);
      double var5 = Math.max(0.0D, var17[2] * 0.65D);
      int[] var16 = hsvToRgb(var17[0], var17[1], var5);
      var2 = Color.argb(102, var16[0], var16[1], var16[2]);
      var3 = Color.argb(136, var16[0], var16[1], var16[2]);
      var5 = Math.max(0.0D, var17[2] * 0.72D);
      var16 = hsvToRgb(var17[0], var17[1], var5);
      return new int[]{var2, var3, Color.argb(102, var16[0], var16[1], var16[2]), Color.argb(136, var16[0], var16[1], var16[2])};
   }

   public static void cancelRunOnUIThread(Runnable var0) {
      ApplicationLoader.applicationHandler.removeCallbacks(var0);
   }

   public static void checkDisplaySize(Context var0, Configuration var1) {
      Exception var10000;
      label132: {
         int var2;
         boolean var10001;
         label126: {
            int var3;
            try {
               var2 = (int)density;
               density = var0.getResources().getDisplayMetrics().density;
               var3 = (int)density;
               if (!firstConfigurationWas) {
                  break label126;
               }
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label132;
            }

            if (var2 != var3) {
               try {
                  Theme.reloadAllResources(var0);
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label132;
               }
            }
         }

         boolean var4 = true;

         try {
            firstConfigurationWas = true;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label132;
         }

         Configuration var5 = var1;
         if (var1 == null) {
            try {
               var5 = var0.getResources().getConfiguration();
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label132;
            }
         }

         label109: {
            try {
               if (var5.keyboard != 1 && var5.hardKeyboardHidden == 1) {
                  break label109;
               }
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label132;
            }

            var4 = false;
         }

         WindowManager var19;
         try {
            usingHardwareInput = var4;
            var19 = (WindowManager)var0.getSystemService("window");
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label132;
         }

         if (var19 != null) {
            Display var20;
            try {
               var20 = var19.getDefaultDisplay();
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label132;
            }

            if (var20 != null) {
               try {
                  var20.getMetrics(displayMetrics);
                  var20.getSize(displaySize);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label132;
               }
            }
         }

         try {
            if (var5.screenWidthDp != 0) {
               var2 = (int)Math.ceil((double)((float)var5.screenWidthDp * density));
               if (Math.abs(displaySize.x - var2) > 3) {
                  displaySize.x = var2;
               }
            }
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label132;
         }

         try {
            if (var5.screenHeightDp != 0) {
               var2 = (int)Math.ceil((double)((float)var5.screenHeightDp * density));
               if (Math.abs(displaySize.y - var2) > 3) {
                  displaySize.y = var2;
               }
            }
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label132;
         }

         label134: {
            label81:
            try {
               if (roundMessageSize == 0) {
                  if (!isTablet()) {
                     break label81;
                  }

                  roundMessageSize = (int)((float)getMinTabletSide() * 0.6F);
               }
               break label134;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label132;
            }

            try {
               roundMessageSize = (int)((float)Math.min(displaySize.x, displaySize.y) * 0.6F);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label132;
            }
         }

         try {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var22 = new StringBuilder();
               var22.append("display size = ");
               var22.append(displaySize.x);
               var22.append(" ");
               var22.append(displaySize.y);
               var22.append(" ");
               var22.append(displayMetrics.xdpi);
               var22.append("x");
               var22.append(displayMetrics.ydpi);
               FileLog.e(var22.toString());
            }

            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      }

      Exception var21 = var10000;
      FileLog.e((Throwable)var21);
   }

   public static void checkForCrashes(Activity var0) {
   }

   public static void checkForUpdates(Activity var0) {
   }

   public static boolean checkPhonePattern(String var0, String var1) {
      if (!TextUtils.isEmpty(var0) && !var0.equals("*")) {
         String[] var6 = var0.split("\\*");
         String var2 = PhoneFormat.stripExceptNumbers(var1);
         int var3 = 0;

         int var5;
         for(int var4 = 0; var3 < var6.length; var4 = var5) {
            var1 = var6[var3];
            var5 = var4;
            if (!TextUtils.isEmpty(var1)) {
               var4 = var2.indexOf(var1, var4);
               if (var4 == -1) {
                  return false;
               }

               var5 = var4 + var1.length();
            }

            ++var3;
         }
      }

      return true;
   }

   @SuppressLint({"NewApi"})
   public static void clearDrawableAnimation(View var0) {
      if (VERSION.SDK_INT >= 21 && var0 != null) {
         Drawable var1;
         if (var0 instanceof ListView) {
            var1 = ((ListView)var0).getSelector();
            if (var1 != null) {
               var1.setState(StateSet.NOTHING);
            }
         } else {
            var1 = var0.getBackground();
            if (var1 != null) {
               var1.setState(StateSet.NOTHING);
               var1.jumpToCurrentState();
            }
         }
      }

   }

   public static int compare(int var0, int var1) {
      if (var0 == var1) {
         return 0;
      } else {
         return var0 > var1 ? 1 : -1;
      }
   }

   public static CharSequence concat(CharSequence... var0) {
      if (var0.length == 0) {
         return "";
      } else {
         int var1 = var0.length;
         byte var2 = 0;
         byte var3 = 0;
         boolean var4 = true;
         if (var1 == 1) {
            return var0[0];
         } else {
            int var5 = var0.length;
            var1 = 0;

            boolean var9;
            while(true) {
               if (var1 >= var5) {
                  var9 = false;
                  break;
               }

               if (var0[var1] instanceof Spanned) {
                  var9 = var4;
                  break;
               }

               ++var1;
            }

            if (var9) {
               SpannableStringBuilder var6 = new SpannableStringBuilder();
               int var10 = var0.length;

               for(var1 = var3; var1 < var10; ++var1) {
                  CharSequence var7 = var0[var1];
                  Object var12 = var7;
                  if (var7 == null) {
                     var12 = "null";
                  }

                  var6.append((CharSequence)var12);
               }

               return new SpannedString(var6);
            } else {
               StringBuilder var8 = new StringBuilder();
               int var11 = var0.length;

               for(var1 = var2; var1 < var11; ++var1) {
                  var8.append(var0[var1]);
               }

               return var8.toString();
            }
         }
      }
   }

   public static boolean copyFile(File param0, File param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static boolean copyFile(InputStream var0, File var1) throws IOException {
      FileOutputStream var4 = new FileOutputStream(var1);
      byte[] var2 = new byte[4096];

      while(true) {
         int var3 = var0.read(var2);
         if (var3 <= 0) {
            var4.close();
            return true;
         }

         Thread.yield();
         var4.write(var2, 0, var3);
      }
   }

   public static byte[] decodeQuotedPrintable(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            byte var3 = var0[var2];
            if (var3 == 61) {
               ++var2;

               Exception var10000;
               label54: {
                  boolean var10001;
                  int var8;
                  try {
                     var8 = Character.digit((char)var0[var2], 16);
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label54;
                  }

                  ++var2;

                  try {
                     var1.write((char)((var8 << 4) + Character.digit((char)var0[var2], 16)));
                     continue;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }

               Exception var7 = var10000;
               FileLog.e((Throwable)var7);
               return null;
            } else {
               var1.write(var3);
            }
         }

         var0 = var1.toByteArray();

         try {
            var1.close();
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         return var0;
      }
   }

   public static float distanceInfluenceForSnapDuration(float var0) {
      return (float)Math.sin((double)((var0 - 0.5F) * 0.47123894F));
   }

   public static int dp(float var0) {
      return var0 == 0.0F ? 0 : (int)Math.ceil((double)(density * var0));
   }

   public static int dp2(float var0) {
      return var0 == 0.0F ? 0 : (int)Math.floor((double)(density * var0));
   }

   public static float dpf2(float var0) {
      return var0 == 0.0F ? 0.0F : density * var0;
   }

   public static int dpr(float var0) {
      return var0 == 0.0F ? 0 : Math.round(density * var0);
   }

   public static void endIncomingCall() {
      if (hasCallPermissions) {
         try {
            TelephonyManager var0 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            Method var1 = Class.forName(var0.getClass().getName()).getDeclaredMethod("getITelephony");
            var1.setAccessible(true);
            ITelephony var2 = (ITelephony)var1.invoke(var0);
            ITelephony var4 = (ITelephony)var1.invoke(var0);
            var4.silenceRinger();
            var4.endCall();
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

      }
   }

   public static double fixLocationCoord(double var0) {
      var0 = (double)((long)(var0 * 1000000.0D));
      Double.isNaN(var0);
      return var0 / 1000000.0D;
   }

   public static String formapMapUrl(int var0, double var1, double var3, int var5, int var6, boolean var7, int var8) {
      int var9 = Math.min(2, (int)Math.ceil((double)density));
      int var10 = MessagesController.getInstance(var0).mapProvider;
      String var11;
      if (var10 != 1 && var10 != 3) {
         var11 = MessagesController.getInstance(var0).mapKey;
         if (!TextUtils.isEmpty(var11)) {
            return var7 ? String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d&markers=color:red%%7Csize:mid%%7C%.6f,%.6f&sensor=false&key=%s", var1, var3, var8, var5, var6, var9, var1, var3, var11) : String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d&key=%s", var1, var3, var8, var5, var6, var9, var11);
         } else {
            return var7 ? String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d&markers=color:red%%7Csize:mid%%7C%.6f,%.6f&sensor=false", var1, var3, var8, var5, var6, var9, var1, var3) : String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=%dx%d&maptype=roadmap&scale=%d", var1, var3, var8, var5, var6, var9);
         }
      } else {
         String[] var12 = new String[]{"ru_RU", "tr_TR"};
         LocaleController.LocaleInfo var13 = LocaleController.getInstance().getCurrentLocaleInfo();
         var11 = null;

         for(var0 = 0; var0 < var12.length; ++var0) {
            if (var12[var0].toLowerCase().contains(var13.shortName)) {
               var11 = var12[var0];
            }
         }

         String var14 = var11;
         if (var11 == null) {
            var14 = "en_US";
         }

         if (var7) {
            return String.format(Locale.US, "https://static-maps.yandex.ru/1.x/?ll=%.6f,%.6f&z=%d&size=%d,%d&l=map&scale=%d&pt=%.6f,%.6f,vkbkm&lang=%s", var3, var1, var8, var5 * var9, var6 * var9, var9, var3, var1, var14);
         } else {
            return String.format(Locale.US, "https://static-maps.yandex.ru/1.x/?ll=%.6f,%.6f&z=%d&size=%d,%d&l=map&scale=%d&lang=%s", var3, var1, var8, var5 * var9, var6 * var9, var9, var14);
         }
      }
   }

   public static String formatFileSize(long var0) {
      return formatFileSize(var0, false);
   }

   public static String formatFileSize(long var0, boolean var2) {
      if (var0 < 1024L) {
         return String.format("%d B", var0);
      } else {
         float var3;
         int var4;
         if (var0 < 1048576L) {
            var3 = (float)var0 / 1024.0F;
            if (var2) {
               var4 = (int)var3;
               if ((var3 - (float)var4) * 10.0F == 0.0F) {
                  return String.format("%d KB", var4);
               }
            }

            return String.format("%.1f KB", var3);
         } else if (var0 < 1073741824L) {
            var3 = (float)var0 / 1024.0F / 1024.0F;
            if (var2) {
               var4 = (int)var3;
               if ((var3 - (float)var4) * 10.0F == 0.0F) {
                  return String.format("%d MB", var4);
               }
            }

            return String.format("%.1f MB", var3);
         } else {
            var3 = (float)var0 / 1024.0F / 1024.0F / 1024.0F;
            if (var2) {
               var4 = (int)var3;
               if ((var3 - (float)var4) * 10.0F == 0.0F) {
                  return String.format("%d GB", var4);
               }
            }

            return String.format("%.1f GB", var3);
         }
      }
   }

   public static File generatePicturePath() {
      return generatePicturePath(false);
   }

   public static File generatePicturePath(boolean var0) {
      try {
         File var1 = getAlbumDir(var0);
         Date var2 = new Date();
         var2.setTime(System.currentTimeMillis() + (long)Utilities.random.nextInt(1000) + 1L);
         SimpleDateFormat var3 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US);
         String var6 = var3.format(var2);
         StringBuilder var5 = new StringBuilder();
         var5.append("IMG_");
         var5.append(var6);
         var5.append(".jpg");
         var1 = new File(var1, var5.toString());
         return var1;
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
         return null;
      }
   }

   public static CharSequence generateSearchName(String var0, String var1, String var2) {
      if (var0 == null && var1 == null) {
         return "";
      } else {
         SpannableStringBuilder var3 = new SpannableStringBuilder();
         String var4;
         StringBuilder var12;
         if (var0 != null && var0.length() != 0) {
            var4 = var0;
            if (var1 != null) {
               var4 = var0;
               if (var1.length() != 0) {
                  var12 = new StringBuilder();
                  var12.append(var0);
                  var12.append(" ");
                  var12.append(var1);
                  var4 = var12.toString();
               }
            }
         } else {
            var4 = var1;
         }

         var0 = var4.trim();
         StringBuilder var11 = new StringBuilder();
         var11.append(" ");
         var11.append(var0.toLowerCase());
         var1 = var11.toString();
         int var5 = 0;

         while(true) {
            var12 = new StringBuilder();
            var12.append(" ");
            var12.append(var2);
            int var6 = var1.indexOf(var12.toString(), var5);
            if (var6 == -1) {
               if (var5 != -1 && var5 < var0.length()) {
                  var3.append(var0.substring(var5));
               }

               return var3;
            }

            byte var7 = 1;
            byte var8;
            if (var6 == 0) {
               var8 = 0;
            } else {
               var8 = 1;
            }

            int var9 = var6 - var8;
            int var10 = var2.length();
            var8 = var7;
            if (var6 == 0) {
               var8 = 0;
            }

            int var13 = var10 + var8 + var9;
            if (var5 != 0 && var5 != var9 + 1) {
               var3.append(var0.substring(var5, var9));
            } else if (var5 == 0 && var9 != 0) {
               var3.append(var0.substring(0, var9));
            }

            var4 = var0.substring(var9, Math.min(var0.length(), var13));
            if (var4.startsWith(" ")) {
               var3.append(" ");
            }

            var4 = var4.trim();
            var5 = var3.length();
            var3.append(var4);
            var3.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), var5, var4.length() + var5, 33);
            var5 = var13;
         }
      }
   }

   public static File generateVideoPath() {
      return generateVideoPath(false);
   }

   public static File generateVideoPath(boolean var0) {
      try {
         File var1 = getAlbumDir(var0);
         Date var2 = new Date();
         var2.setTime(System.currentTimeMillis() + (long)Utilities.random.nextInt(1000) + 1L);
         SimpleDateFormat var3 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US);
         String var6 = var3.format(var2);
         StringBuilder var5 = new StringBuilder();
         var5.append("VID_");
         var5.append(var6);
         var5.append(".mp4");
         var1 = new File(var1, var5.toString());
         return var1;
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
         return null;
      }
   }

   private static File getAlbumDir(boolean var0) {
      if (var0 || VERSION.SDK_INT >= 23 && ApplicationLoader.applicationContext.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
         return FileLoader.getDirectory(4);
      } else {
         File var2;
         if ("mounted".equals(Environment.getExternalStorageState())) {
            File var1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Telegram");
            var2 = var1;
            if (!var1.mkdirs()) {
               var2 = var1;
               if (!var1.exists()) {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("failed to create directory");
                  }

                  return null;
               }
            }
         } else {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("External storage is not mounted READ/WRITE.");
            }

            var2 = null;
         }

         return var2;
      }
   }

   public static File getCacheDir() {
      String var0;
      try {
         var0 = Environment.getExternalStorageState();
      } catch (Exception var1) {
         FileLog.e((Throwable)var1);
         var0 = null;
      }

      File var4;
      if (var0 == null || var0.startsWith("mounted")) {
         label40: {
            try {
               var4 = ApplicationLoader.applicationContext.getExternalCacheDir();
            } catch (Exception var3) {
               FileLog.e((Throwable)var3);
               break label40;
            }

            if (var4 != null) {
               return var4;
            }
         }
      }

      try {
         var4 = ApplicationLoader.applicationContext.getCacheDir();
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
         return new File("");
      }

      if (var4 != null) {
         return var4;
      } else {
         return new File("");
      }
   }

   public static String[] getCurrentKeyboardLanguage() {
      InputMethodManager var0;
      InputMethodSubtype var1;
      boolean var10001;
      try {
         var0 = (InputMethodManager)ApplicationLoader.applicationContext.getSystemService("input_method");
         var1 = var0.getCurrentInputMethodSubtype();
      } catch (Exception var6) {
         var10001 = false;
         return new String[]{"en"};
      }

      String var16;
      String var2;
      if (var1 != null) {
         label138: {
            try {
               if (VERSION.SDK_INT >= 24) {
                  var2 = var1.getLanguageTag();
                  break label138;
               }
            } catch (Exception var15) {
               var10001 = false;
               return new String[]{"en"};
            }

            var2 = null;
         }

         var16 = var2;

         try {
            if (TextUtils.isEmpty(var2)) {
               var16 = var1.getLocale();
            }
         } catch (Exception var14) {
            var10001 = false;
            return new String[]{"en"};
         }
      } else {
         InputMethodSubtype var18;
         try {
            var18 = var0.getLastInputMethodSubtype();
         } catch (Exception var5) {
            var10001 = false;
            return new String[]{"en"};
         }

         if (var18 != null) {
            label151: {
               try {
                  if (VERSION.SDK_INT >= 24) {
                     var16 = var18.getLanguageTag();
                     break label151;
                  }
               } catch (Exception var13) {
                  var10001 = false;
                  return new String[]{"en"};
               }

               var16 = null;
            }

            try {
               if (TextUtils.isEmpty(var16)) {
                  var16 = var18.getLocale();
               }
            } catch (Exception var12) {
               var10001 = false;
               return new String[]{"en"};
            }
         } else {
            var16 = null;
         }
      }

      String var17;
      label152: {
         try {
            if (TextUtils.isEmpty(var16)) {
               var17 = LocaleController.getSystemLocaleStringIso639();
               LocaleController.LocaleInfo var19 = LocaleController.getInstance().getCurrentLocaleInfo();
               var16 = var19.getBaseLangCode();
               if (TextUtils.isEmpty(var16)) {
                  var16 = var19.getLangCode();
               }
               break label152;
            }
         } catch (Exception var11) {
            var10001 = false;
            return new String[]{"en"};
         }

         try {
            var16 = var16.replace('_', '-');
         } catch (Exception var4) {
            var10001 = false;
            return new String[]{"en"};
         }

         return new String[]{var16};
      }

      label149: {
         label150: {
            try {
               if (var17.contains(var16)) {
                  break label150;
               }
            } catch (Exception var10) {
               var10001 = false;
               return new String[]{"en"};
            }

            var2 = var16;

            try {
               if (!var16.contains(var17)) {
                  break label149;
               }
            } catch (Exception var9) {
               var10001 = false;
               return new String[]{"en"};
            }
         }

         label86: {
            try {
               if (!var17.contains("en")) {
                  break label86;
               }
            } catch (Exception var8) {
               var10001 = false;
               return new String[]{"en"};
            }

            var2 = null;
            break label149;
         }

         var2 = "en";
      }

      try {
         if (!TextUtils.isEmpty(var2)) {
            return new String[]{var17.replace('_', '-'), var2};
         }
      } catch (Exception var7) {
         var10001 = false;
         return new String[]{"en"};
      }

      try {
         return new String[]{var17.replace('_', '-')};
      } catch (Exception var3) {
         var10001 = false;
         return new String[]{"en"};
      }
   }

   public static String getDataColumn(Context var0, Uri var1, String var2, String[] var3) {
      boolean var10001;
      Cursor var38;
      try {
         var38 = var0.getContentResolver().query(var1, new String[]{"_data"}, var2, var3, (String)null);
      } catch (Exception var34) {
         var10001 = false;
         return null;
      }

      Throwable var35;
      label289: {
         String var36;
         label315: {
            if (var38 != null) {
               label293: {
                  label277: {
                     boolean var4;
                     try {
                        if (!var38.moveToFirst()) {
                           break label293;
                        }

                        var36 = var38.getString(var38.getColumnIndexOrThrow("_data"));
                        if (var36.startsWith("content://")) {
                           break label277;
                        }

                        if (var36.startsWith("/")) {
                           break label315;
                        }

                        var4 = var36.startsWith("file://");
                     } catch (Throwable var32) {
                        var35 = var32;
                        break label289;
                     } finally {
                        ;
                     }

                     if (var4) {
                        break label315;
                     }
                  }

                  if (var38 != null) {
                     try {
                        var38.close();
                     } catch (Exception var29) {
                        var10001 = false;
                        return null;
                     }
                  }

                  return null;
               }
            }

            if (var38 != null) {
               try {
                  var38.close();
               } catch (Exception var31) {
                  var10001 = false;
               }

               return null;
            }

            return null;
         }

         if (var38 != null) {
            try {
               var38.close();
            } catch (Exception var27) {
               var10001 = false;
               return null;
            }
         }

         return var36;
      }

      try {
         throw var35;
      } catch (Throwable var28) {
         Throwable var37 = var35;
         var35 = var28;
         if (var38 != null) {
            if (var37 != null) {
               try {
                  var38.close();
               } catch (Throwable var25) {
               }
            } else {
               try {
                  var38.close();
               } catch (Exception var30) {
                  var10001 = false;
                  return null;
               }
            }
         }

         try {
            throw var35;
         } catch (Exception var26) {
            var10001 = false;
            return null;
         }
      }
   }

   public static int getMinTabletSide() {
      Point var0;
      int var1;
      int var2;
      int var3;
      if (!isSmallTablet()) {
         var0 = displaySize;
         var1 = Math.min(var0.x, var0.y);
         var2 = var1 * 35 / 100;
         var3 = var2;
         if (var2 < dp(320.0F)) {
            var3 = dp(320.0F);
         }

         return var1 - var3;
      } else {
         var0 = displaySize;
         int var4 = Math.min(var0.x, var0.y);
         var0 = displaySize;
         var1 = Math.max(var0.x, var0.y);
         var2 = var1 * 35 / 100;
         var3 = var2;
         if (var2 < dp(320.0F)) {
            var3 = dp(320.0F);
         }

         return Math.min(var4, var1 - var3);
      }
   }

   public static int getMyLayerVersion(int var0) {
      return var0 & '\uffff';
   }

   public static int getOffsetColor(int var0, int var1, float var2, float var3) {
      int var4 = Color.red(var1);
      int var5 = Color.green(var1);
      int var6 = Color.blue(var1);
      int var7 = Color.alpha(var1);
      int var8 = Color.red(var0);
      int var9 = Color.green(var0);
      var1 = Color.blue(var0);
      var0 = Color.alpha(var0);
      return Color.argb((int)(((float)var0 + (float)(var7 - var0) * var2) * var3), (int)((float)var8 + (float)(var4 - var8) * var2), (int)((float)var9 + (float)(var5 - var9) * var2), (int)((float)var1 + (float)(var6 - var1) * var2));
   }

   @SuppressLint({"NewApi"})
   public static String getPath(Uri var0) {
      Exception var10000;
      label156: {
         boolean var1;
         boolean var10001;
         label148: {
            label147: {
               try {
                  if (VERSION.SDK_INT >= 19) {
                     break label147;
                  }
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label156;
               }

               var1 = false;
               break label148;
            }

            var1 = true;
         }

         String var21;
         if (var1) {
            label155: {
               boolean var2;
               try {
                  if (!DocumentsContract.isDocumentUri(ApplicationLoader.applicationContext, var0)) {
                     break label155;
                  }

                  var2 = isExternalStorageDocument(var0);
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label156;
               }

               if (var2) {
                  try {
                     String[] var20 = DocumentsContract.getDocumentId(var0).split(":");
                     if ("primary".equalsIgnoreCase(var20[0])) {
                        StringBuilder var3 = new StringBuilder();
                        var3.append(Environment.getExternalStorageDirectory());
                        var3.append("/");
                        var3.append(var20[1]);
                        return var3.toString();
                     }

                     return null;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label156;
                  }
               } else {
                  try {
                     if (isDownloadsDocument(var0)) {
                        var21 = DocumentsContract.getDocumentId(var0);
                        var0 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(var21));
                        return getDataColumn(ApplicationLoader.applicationContext, var0, (String)null, (String[])null);
                     }
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label156;
                  }

                  String[] var24;
                  try {
                     if (!isMediaDocument(var0)) {
                        return null;
                     }

                     var24 = DocumentsContract.getDocumentId(var0).split(":");
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label156;
                  }

                  var21 = var24[0];
                  byte var22 = -1;

                  int var4;
                  try {
                     var4 = var21.hashCode();
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label156;
                  }

                  if (var4 != 93166550) {
                     if (var4 != 100313435) {
                        if (var4 == 112202875) {
                           label107: {
                              try {
                                 if (!var21.equals("video")) {
                                    break label107;
                                 }
                              } catch (Exception var11) {
                                 var10000 = var11;
                                 var10001 = false;
                                 break label156;
                              }

                              var22 = 1;
                           }
                        }
                     } else {
                        label103: {
                           try {
                              if (!var21.equals("image")) {
                                 break label103;
                              }
                           } catch (Exception var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label156;
                           }

                           var22 = 0;
                        }
                     }
                  } else {
                     label99: {
                        try {
                           if (!var21.equals("audio")) {
                              break label99;
                           }
                        } catch (Exception var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label156;
                        }

                        var22 = 2;
                     }
                  }

                  if (var22 != 0) {
                     if (var22 != 1) {
                        if (var22 != 2) {
                           var0 = null;
                        } else {
                           try {
                              var0 = Media.EXTERNAL_CONTENT_URI;
                           } catch (Exception var8) {
                              var10000 = var8;
                              var10001 = false;
                              break label156;
                           }
                        }
                     } else {
                        try {
                           var0 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } catch (Exception var7) {
                           var10000 = var7;
                           var10001 = false;
                           break label156;
                        }
                     }
                  } else {
                     try {
                        var0 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                     } catch (Exception var6) {
                        var10000 = var6;
                        var10001 = false;
                        break label156;
                     }
                  }

                  String var25 = var24[1];

                  try {
                     return getDataColumn(ApplicationLoader.applicationContext, var0, "_id=?", new String[]{var25});
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label156;
                  }
               }
            }
         }

         try {
            if ("content".equalsIgnoreCase(var0.getScheme())) {
               return getDataColumn(ApplicationLoader.applicationContext, var0, (String)null, (String[])null);
            }
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label156;
         }

         try {
            if ("file".equalsIgnoreCase(var0.getScheme())) {
               var21 = var0.getPath();
               return var21;
            }

            return null;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
         }
      }

      Exception var23 = var10000;
      FileLog.e((Throwable)var23);
      return null;
   }

   public static int getPatternColor(int var0) {
      float[] var1 = RGBtoHSB(Color.red(var0), Color.green(var0), Color.blue(var0));
      if (var1[1] > 0.0F || var1[2] < 1.0F && var1[2] > 0.0F) {
         var1[1] = Math.min(1.0F, var1[1] + 0.05F + (1.0F - var1[1]) * 0.1F);
      }

      if (var1[2] > 0.5F) {
         var1[2] = Math.max(0.0F, var1[2] * 0.65F);
      } else {
         var1[2] = Math.max(0.0F, Math.min(1.0F, 1.0F - var1[2] * 0.65F));
      }

      return HSBtoRGB(var1[0], var1[1], var1[2]) & 1728053247;
   }

   public static int getPatternSideColor(int var0) {
      float[] var1 = RGBtoHSB(Color.red(var0), Color.green(var0), Color.blue(var0));
      var1[1] = Math.min(1.0F, var1[1] + 0.05F);
      if (var1[2] > 0.5F) {
         var1[2] = Math.max(0.0F, var1[2] * 0.9F);
      } else {
         var1[2] = Math.max(0.0F, var1[2] * 0.9F);
      }

      return HSBtoRGB(var1[0], var1[1], var1[2]) | -16777216;
   }

   public static int getPeerLayerVersion(int var0) {
      return var0 >> 16 & '\uffff';
   }

   public static int getPhotoSize() {
      if (photoSize == null) {
         photoSize = 1280;
      }

      return photoSize;
   }

   public static float getPixelsInCM(float var0, boolean var1) {
      float var2 = var0 / 2.54F;
      if (var1) {
         var0 = displayMetrics.xdpi;
      } else {
         var0 = displayMetrics.ydpi;
      }

      return var2 * var0;
   }

   public static Point getRealScreenSize() {
      Point var0 = new Point();

      Exception var10000;
      label32: {
         WindowManager var1;
         boolean var10001;
         try {
            var1 = (WindowManager)ApplicationLoader.applicationContext.getSystemService("window");
            if (VERSION.SDK_INT >= 17) {
               var1.getDefaultDisplay().getRealSize(var0);
               return var0;
            }
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label32;
         }

         try {
            Method var2 = Display.class.getMethod("getRawWidth");
            Method var8 = Display.class.getMethod("getRawHeight");
            var0.set((Integer)var2.invoke(var1.getDefaultDisplay()), (Integer)var8.invoke(var1.getDefaultDisplay()));
            return var0;
         } catch (Exception var5) {
            Exception var3 = var5;

            try {
               var0.set(var1.getDefaultDisplay().getWidth(), var1.getDefaultDisplay().getHeight());
               FileLog.e((Throwable)var3);
               return var0;
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
            }
         }
      }

      Exception var7 = var10000;
      FileLog.e((Throwable)var7);
      return var0;
   }

   public static int getShadowHeight() {
      float var0 = density;
      if (var0 >= 4.0F) {
         return 3;
      } else {
         return var0 >= 2.0F ? 2 : 1;
      }
   }

   public static byte[] getStringBytes(String var0) {
      try {
         byte[] var2 = var0.getBytes("UTF-8");
         return var2;
      } catch (Exception var1) {
         return new byte[0];
      }
   }

   @SuppressLint({"PrivateApi"})
   public static String getSystemProperty(String var0) {
      try {
         var0 = (String)Class.forName("android.os.SystemProperties").getMethod("get", String.class).invoke((Object)null, var0);
         return var0;
      } catch (Exception var1) {
         return null;
      }
   }

   public static int getThumbForNameOrMime(String var0, String var1, boolean var2) {
      int var3;
      if (var0 != null && var0.length() != 0) {
         byte var5;
         if (!var0.contains(".doc") && !var0.contains(".txt") && !var0.contains(".psd")) {
            if (!var0.contains(".xls") && !var0.contains(".csv")) {
               if (!var0.contains(".pdf") && !var0.contains(".ppt") && !var0.contains(".key")) {
                  if (!var0.contains(".zip") && !var0.contains(".rar") && !var0.contains(".ai") && !var0.contains(".mp3") && !var0.contains(".mov") && !var0.contains(".avi")) {
                     var5 = -1;
                  } else {
                     var5 = 3;
                  }
               } else {
                  var5 = 2;
               }
            } else {
               var5 = 1;
            }
         } else {
            var5 = 0;
         }

         int var4 = var5;
         if (var5 == -1) {
            var3 = var0.lastIndexOf(46);
            if (var3 == -1) {
               var1 = "";
            } else {
               var1 = var0.substring(var3 + 1);
            }

            if (var1.length() != 0) {
               var4 = var1.charAt(0) % documentIcons.length;
            } else {
               var4 = var0.charAt(0) % documentIcons.length;
            }
         }

         if (var2) {
            var3 = documentMediaIcons[var4];
         } else {
            var3 = documentIcons[var4];
         }

         return var3;
      } else {
         if (var2) {
            var3 = documentMediaIcons[0];
         } else {
            var3 = documentIcons[0];
         }

         return var3;
      }
   }

   public static CharSequence getTrimmedString(CharSequence var0) {
      CharSequence var1 = var0;
      if (var0 != null) {
         var1 = var0;
         if (var0.length() == 0) {
            var1 = var0;
         } else {
            while(true) {
               var0 = var1;
               if (var1.length() <= 0) {
                  break;
               }

               if (var1.charAt(0) != '\n') {
                  var0 = var1;
                  if (var1.charAt(0) != ' ') {
                     break;
                  }
               }

               var1 = var1.subSequence(1, var1.length());
            }

            while(true) {
               var1 = var0;
               if (var0.length() <= 0) {
                  break;
               }

               if (var0.charAt(var0.length() - 1) != '\n') {
                  var1 = var0;
                  if (var0.charAt(var0.length() - 1) != ' ') {
                     break;
                  }
               }

               var0 = var0.subSequence(0, var0.length() - 1);
            }
         }
      }

      return var1;
   }

   public static Typeface getTypeface(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static int getViewInset(View var0) {
      if (var0 != null && VERSION.SDK_INT >= 21 && var0.getHeight() != displaySize.y && var0.getHeight() != displaySize.y - statusBarHeight) {
         Exception var10000;
         label54: {
            boolean var10001;
            try {
               if (mAttachInfoField == null) {
                  mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
                  mAttachInfoField.setAccessible(true);
               }
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label54;
            }

            Object var6;
            try {
               var6 = mAttachInfoField.get(var0);
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label54;
            }

            if (var6 == null) {
               return 0;
            }

            try {
               if (mStableInsetsField == null) {
                  mStableInsetsField = var6.getClass().getDeclaredField("mStableInsets");
                  mStableInsetsField.setAccessible(true);
               }
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
               break label54;
            }

            try {
               int var1 = ((Rect)mStableInsetsField.get(var6)).bottom;
               return var1;
            } catch (Exception var2) {
               var10000 = var2;
               var10001 = false;
            }
         }

         Exception var7 = var10000;
         FileLog.e((Throwable)var7);
      }

      return 0;
   }

   public static String getWallPaperUrl(Object var0, int var1) {
      StringBuilder var2;
      String var6;
      if (var0 instanceof TLRPC.TL_wallPaper) {
         TLRPC.TL_wallPaper var5 = (TLRPC.TL_wallPaper)var0;
         var2 = new StringBuilder();
         var2.append("https://");
         var2.append(MessagesController.getInstance(var1).linkPrefix);
         var2.append("/bg/");
         var2.append(var5.slug);
         String var8 = var2.toString();
         StringBuilder var3 = new StringBuilder();
         TLRPC.TL_wallPaperSettings var4 = var5.settings;
         if (var4 != null) {
            if (var4.blur) {
               var3.append("blur");
            }

            if (var5.settings.motion) {
               if (var3.length() > 0) {
                  var3.append("+");
               }

               var3.append("motion");
            }
         }

         var6 = var8;
         if (var3.length() > 0) {
            StringBuilder var7 = new StringBuilder();
            var7.append(var8);
            var7.append("?mode=");
            var7.append(var3.toString());
            var6 = var7.toString();
         }
      } else if (var0 instanceof WallpapersListActivity.ColorWallpaper) {
         WallpapersListActivity.ColorWallpaper var9 = (WallpapersListActivity.ColorWallpaper)var0;
         var6 = String.format("%02x%02x%02x", (byte)(var9.color >> 16) & 255, (byte)(var9.color >> 8) & 255, (byte)(var9.color & 255)).toLowerCase();
         if (var9.pattern != null) {
            var2 = new StringBuilder();
            var2.append("https://");
            var2.append(MessagesController.getInstance(var1).linkPrefix);
            var2.append("/bg/");
            var2.append(var9.pattern.slug);
            var2.append("?intensity=");
            var2.append((int)(var9.intensity * 100.0F));
            var2.append("&bg_color=");
            var2.append(var6);
            var6 = var2.toString();
         } else {
            var2 = new StringBuilder();
            var2.append("https://");
            var2.append(MessagesController.getInstance(var1).linkPrefix);
            var2.append("/bg/");
            var2.append(var6);
            var6 = var2.toString();
         }
      } else {
         var6 = null;
      }

      return var6;
   }

   public static boolean handleProxyIntent(Activity var0, Intent var1) {
      if (var1 == null) {
         return false;
      } else {
         boolean var10001;
         try {
            if ((var1.getFlags() & 1048576) != 0) {
               return false;
            }
         } catch (Exception var21) {
            var10001 = false;
            return false;
         }

         Uri var2;
         try {
            var2 = var1.getData();
         } catch (Exception var20) {
            var10001 = false;
            return false;
         }

         if (var2 != null) {
            String var22;
            try {
               var22 = var2.getScheme();
            } catch (Exception var19) {
               var10001 = false;
               return false;
            }

            String var3;
            boolean var5;
            String var6;
            String var7;
            String var23;
            label179: {
               var3 = null;
               String var4 = null;
               if (var22 != null) {
                  label180: {
                     try {
                        var5 = var22.equals("http");
                     } catch (Exception var13) {
                        var10001 = false;
                        return false;
                     }

                     if (!var5) {
                        label184: {
                           label149:
                           try {
                              if (!var22.equals("https")) {
                                 break label149;
                              }
                              break label184;
                           } catch (Exception var18) {
                              var10001 = false;
                              return false;
                           }

                           try {
                              if (!var22.equals("tg")) {
                                 break label180;
                              }

                              var22 = var2.toString();
                              var5 = var22.startsWith("tg:proxy");
                           } catch (Exception var17) {
                              var10001 = false;
                              return false;
                           }

                           if (!var5) {
                              try {
                                 if (!var22.startsWith("tg://proxy") && !var22.startsWith("tg:socks") && !var22.startsWith("tg://socks")) {
                                    break label180;
                                 }
                              } catch (Exception var16) {
                                 var10001 = false;
                                 return false;
                              }
                           }

                           try {
                              var2 = Uri.parse(var22.replace("tg:proxy", "tg://telegram.org").replace("tg://proxy", "tg://telegram.org").replace("tg://socks", "tg://telegram.org").replace("tg:socks", "tg://telegram.org"));
                              var6 = var2.getQueryParameter("server");
                              var7 = var2.getQueryParameter("port");
                              var3 = var2.getQueryParameter("user");
                              var22 = var2.getQueryParameter("pass");
                              var23 = var2.getQueryParameter("secret");
                              break label179;
                           } catch (Exception var12) {
                              var10001 = false;
                              return false;
                           }
                        }
                     }

                     label127: {
                        label126: {
                           label174: {
                              try {
                                 var22 = var2.getHost().toLowerCase();
                                 if (!var22.equals("telegram.me") && !var22.equals("t.me") && !var22.equals("telegram.dog")) {
                                    break label174;
                                 }
                              } catch (Exception var15) {
                                 var10001 = false;
                                 return false;
                              }

                              try {
                                 var22 = var2.getPath();
                              } catch (Exception var11) {
                                 var10001 = false;
                                 return false;
                              }

                              if (var22 != null) {
                                 try {
                                    if (var22.startsWith("/socks") || var22.startsWith("/proxy")) {
                                       break label126;
                                    }
                                 } catch (Exception var14) {
                                    var10001 = false;
                                    return false;
                                 }
                              }
                           }

                           var7 = null;
                           var6 = var7;
                           var23 = var7;
                           var22 = var7;
                           var3 = var4;
                           break label127;
                        }

                        try {
                           var7 = var2.getQueryParameter("server");
                           var6 = var2.getQueryParameter("port");
                           var3 = var2.getQueryParameter("user");
                           var22 = var2.getQueryParameter("pass");
                           var23 = var2.getQueryParameter("secret");
                        } catch (Exception var10) {
                           var10001 = false;
                           return false;
                        }
                     }

                     var4 = var7;
                     var7 = var6;
                     var6 = var4;
                     break label179;
                  }
               }

               var23 = null;
               var22 = var23;
               var6 = var23;
               var7 = var23;
            }

            try {
               if (TextUtils.isEmpty(var6)) {
                  return false;
               }

               var5 = TextUtils.isEmpty(var7);
            } catch (Exception var9) {
               var10001 = false;
               return false;
            }

            if (!var5) {
               if (var3 == null) {
                  var3 = "";
               }

               if (var22 == null) {
                  var22 = "";
               }

               if (var23 == null) {
                  var23 = "";
               }

               try {
                  showProxyAlert(var0, var6, var7, var3, var22, var23);
                  return true;
               } catch (Exception var8) {
                  var10001 = false;
               }
            }
         }

         return false;
      }
   }

   public static void hideKeyboard(View var0) {
      if (var0 != null) {
         try {
            InputMethodManager var1 = (InputMethodManager)var0.getContext().getSystemService("input_method");
            if (!var1.isActive()) {
               return;
            }

            var1.hideSoftInputFromWindow(var0.getWindowToken(), 0);
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }
   }

   private static int[] hsvToRgb(double var0, double var2, double var4) {
      var0 = 6.0D * var0;
      double var6 = (double)((int)Math.floor(var0));
      Double.isNaN(var6);
      double var8 = var0 - var6;
      double var10 = (1.0D - var2) * var4;
      var0 = (1.0D - var8 * var2) * var4;
      var2 = var4 * (1.0D - (1.0D - var8) * var2);
      int var12 = (int)var6 % 6;
      var6 = 0.0D;
      if (var12 != 0) {
         if (var12 == 1) {
            var2 = var4;
            var4 = var10;
            var10 = var0;
            var0 = var4;
            return new int[]{(int)(var10 * 255.0D), (int)(var2 * 255.0D), (int)(var0 * 255.0D)};
         }

         if (var12 == 2) {
            var0 = var2;
            var2 = var4;
            return new int[]{(int)(var10 * 255.0D), (int)(var2 * 255.0D), (int)(var0 * 255.0D)};
         }

         if (var12 == 3) {
            var2 = var0;
            var0 = var4;
            return new int[]{(int)(var10 * 255.0D), (int)(var2 * 255.0D), (int)(var0 * 255.0D)};
         }

         if (var12 == 4) {
            var6 = var10;
            var0 = var4;
            var10 = var2;
            var2 = var6;
            return new int[]{(int)(var10 * 255.0D), (int)(var2 * 255.0D), (int)(var0 * 255.0D)};
         }

         if (var12 != 5) {
            var0 = 0.0D;
            var10 = var0;
            var2 = var6;
            return new int[]{(int)(var10 * 255.0D), (int)(var2 * 255.0D), (int)(var0 * 255.0D)};
         }

         var2 = var10;
      } else {
         var0 = var10;
      }

      var10 = var4;
      return new int[]{(int)(var10 * 255.0D), (int)(var2 * 255.0D), (int)(var0 * 255.0D)};
   }

   public static boolean isAirplaneModeOn() {
      int var0 = VERSION.SDK_INT;
      boolean var1 = true;
      boolean var2 = true;
      if (var0 < 17) {
         if (android.provider.Settings.System.getInt(ApplicationLoader.applicationContext.getContentResolver(), "airplane_mode_on", 0) == 0) {
            var2 = false;
         }

         return var2;
      } else {
         if (Global.getInt(ApplicationLoader.applicationContext.getContentResolver(), "airplane_mode_on", 0) != 0) {
            var2 = var1;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   public static boolean isBannedForever(TLRPC.TL_chatBannedRights var0) {
      boolean var1;
      if (var0 != null && Math.abs((long)var0.until_date - System.currentTimeMillis() / 1000L) <= 157680000L) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isDownloadsDocument(Uri var0) {
      return "com.android.providers.downloads.documents".equals(var0.getAuthority());
   }

   public static boolean isExternalStorageDocument(Uri var0) {
      return "com.android.externalstorage.documents".equals(var0.getAuthority());
   }

   public static boolean isGoogleMapsInstalled(BaseFragment var0) {
      return true;
   }

   public static boolean isInternalUri(Uri var0) {
      String var7 = var0.getPath();
      boolean var1 = false;
      if (var7 == null) {
         return false;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(Pattern.quote((new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_logs")).getAbsolutePath()));
         var2.append("/\\d+\\.log");
         if (var7.matches(var2.toString())) {
            return false;
         } else {
            String var9;
            for(int var3 = 0; var7 == null || var7.length() <= 4096; var7 = var9) {
               var9 = Utilities.readlink(var7);
               if (var9 == null || var9.equals(var7)) {
                  var9 = var7;
                  if (var7 != null) {
                     label43: {
                        String var4;
                        try {
                           File var10 = new File(var7);
                           var4 = var10.getCanonicalPath();
                        } catch (Exception var6) {
                           var7.replace("/./", "/");
                           var9 = var7;
                           break label43;
                        }

                        var9 = var7;
                        if (var4 != null) {
                           var9 = var4;
                        }
                     }
                  }

                  boolean var5 = var1;
                  if (var9 != null) {
                     var9 = var9.toLowerCase();
                     StringBuilder var8 = new StringBuilder();
                     var8.append("/data/data/");
                     var8.append(ApplicationLoader.applicationContext.getPackageName());
                     var5 = var1;
                     if (var9.contains(var8.toString())) {
                        var5 = true;
                     }
                  }

                  return var5;
               }

               ++var3;
               if (var3 >= 10) {
                  return true;
               }
            }

            return true;
         }
      }
   }

   public static boolean isKeyboardShowed(View var0) {
      if (var0 == null) {
         return false;
      } else {
         try {
            boolean var1 = ((InputMethodManager)var0.getContext().getSystemService("input_method")).isActive(var0);
            return var1;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
            return false;
         }
      }
   }

   public static boolean isMediaDocument(Uri var0) {
      return "com.android.providers.media.documents".equals(var0.getAuthority());
   }

   public static boolean isSmallTablet() {
      Point var0 = displaySize;
      boolean var1;
      if ((float)Math.min(var0.x, var0.y) / density <= 700.0F) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isTablet() {
      if (isTablet == null) {
         isTablet = ApplicationLoader.applicationContext.getResources().getBoolean(2130968579);
      }

      return isTablet;
   }

   public static boolean isWaitingForCall() {
      // $FF: Couldn't be decompiled
   }

   public static boolean isWaitingForSms() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static void lambda$openForView$0(Activity var0, DialogInterface var1, int var2) {
      try {
         StringBuilder var3 = new StringBuilder();
         var3.append("package:");
         var3.append(var0.getPackageName());
         Intent var5 = new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES", Uri.parse(var3.toString()));
         var0.startActivity(var5);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   static void lambda$showProxyAlert$1(Runnable var0, View var1) {
      var0.run();
   }

   // $FF: synthetic method
   static void lambda$showProxyAlert$2(String var0, String var1, String var2, String var3, String var4, Runnable var5, View var6) {
      Editor var9 = MessagesController.getGlobalMainSettings().edit();
      var9.putBoolean("proxy_enabled", true);
      var9.putString("proxy_ip", var0);
      int var7 = Utilities.parseInt(var1);
      var9.putInt("proxy_port", var7);
      SharedConfig.ProxyInfo var8;
      if (TextUtils.isEmpty(var2)) {
         var9.remove("proxy_secret");
         if (TextUtils.isEmpty(var3)) {
            var9.remove("proxy_pass");
         } else {
            var9.putString("proxy_pass", var3);
         }

         if (TextUtils.isEmpty(var4)) {
            var9.remove("proxy_user");
         } else {
            var9.putString("proxy_user", var4);
         }

         var8 = new SharedConfig.ProxyInfo(var0, var7, var4, var3, "");
      } else {
         var9.remove("proxy_pass");
         var9.remove("proxy_user");
         var9.putString("proxy_secret", var2);
         var8 = new SharedConfig.ProxyInfo(var0, var7, "", "", var2);
      }

      var9.commit();
      SharedConfig.currentProxy = SharedConfig.addProxy(var8);
      ConnectionsManager.setProxySettings(true, var0, var7, var4, var3, var2);
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged);
      var5.run();
   }

   public static ArrayList loadVCardFromStream(Uri param0, int param1, boolean param2, ArrayList param3, String param4) {
      // $FF: Couldn't be decompiled
   }

   @SuppressLint({"WrongConstant"})
   public static void lockOrientation(Activity var0) {
      if (var0 != null && prevOrientation == -10) {
         Exception var10000;
         label95: {
            WindowManager var1;
            boolean var10001;
            try {
               prevOrientation = var0.getRequestedOrientation();
               var1 = (WindowManager)var0.getSystemService("window");
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label95;
            }

            if (var1 == null) {
               return;
            }

            int var2;
            int var3;
            try {
               if (var1.getDefaultDisplay() == null) {
                  return;
               }

               var2 = var1.getDefaultDisplay().getRotation();
               var3 = var0.getResources().getConfiguration().orientation;
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label95;
            }

            if (var2 == 3) {
               if (var3 == 1) {
                  try {
                     var0.setRequestedOrientation(1);
                     return;
                  } catch (Exception var4) {
                     var10000 = var4;
                     var10001 = false;
                  }
               } else {
                  try {
                     var0.setRequestedOrientation(8);
                     return;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }
            } else if (var2 == 1) {
               if (var3 == 1) {
                  try {
                     var0.setRequestedOrientation(9);
                     return;
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                  }
               } else {
                  try {
                     var0.setRequestedOrientation(0);
                     return;
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                  }
               }
            } else if (var2 == 0) {
               if (var3 == 2) {
                  try {
                     var0.setRequestedOrientation(0);
                     return;
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               } else {
                  try {
                     var0.setRequestedOrientation(1);
                     return;
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                  }
               }
            } else if (var3 == 2) {
               try {
                  var0.setRequestedOrientation(8);
                  return;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
               }
            } else {
               try {
                  var0.setRequestedOrientation(9);
                  return;
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
               }
            }
         }

         Exception var14 = var10000;
         FileLog.e((Throwable)var14);
      }

   }

   public static void makeAccessibilityAnnouncement(CharSequence var0) {
      AccessibilityManager var1 = (AccessibilityManager)ApplicationLoader.applicationContext.getSystemService("accessibility");
      if (var1.isEnabled()) {
         AccessibilityEvent var2 = AccessibilityEvent.obtain();
         var2.setEventType(16384);
         var2.getText().add(var0);
         var1.sendAccessibilityEvent(var2);
      }

   }

   public static long makeBroadcastId(int var0) {
      return (long)var0 & 4294967295L | 4294967296L;
   }

   public static boolean needShowPasscode(boolean var0) {
      boolean var1 = ForegroundDetector.getInstance().isWasInBackground(var0);
      if (var0) {
         ForegroundDetector.getInstance().resetBackgroundVar();
      }

      if (SharedConfig.passcodeHash.length() <= 0 || !var1 || !SharedConfig.appLocked && (SharedConfig.autoLockIn == 0 || SharedConfig.lastPauseTime == 0 || SharedConfig.appLocked || SharedConfig.lastPauseTime + SharedConfig.autoLockIn > ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime()) && ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() + 5 >= SharedConfig.lastPauseTime) {
         var0 = false;
      } else {
         var0 = true;
      }

      return var0;
   }

   public static void openDocument(MessageObject var0, Activity var1, BaseFragment var2) {
      if (var0 != null) {
         TLRPC.Document var3 = var0.getDocument();
         if (var3 != null) {
            String var4;
            if (var0.messageOwner.media != null) {
               var4 = FileLoader.getAttachFileName(var3);
            } else {
               var4 = "";
            }

            String var5 = var0.messageOwner.attachPath;
            File var27;
            if (var5 != null && var5.length() != 0) {
               var27 = new File(var0.messageOwner.attachPath);
            } else {
               var27 = null;
            }

            File var6;
            label169: {
               if (var27 != null) {
                  var6 = var27;
                  if (var27 == null) {
                     break label169;
                  }

                  var6 = var27;
                  if (var27.exists()) {
                     break label169;
                  }
               }

               var6 = FileLoader.getPathToMessage(var0.messageOwner);
            }

            if (var6 != null && var6.exists()) {
               if (var2 != null && var6.getName().toLowerCase().endsWith("attheme")) {
                  Theme.ThemeInfo var23 = Theme.applyThemeFile(var6, var0.getDocumentName(), true);
                  if (var23 != null) {
                     var2.presentFragment(new ThemePreviewActivity(var6, var23));
                  } else {
                     AlertDialog.Builder var24 = new AlertDialog.Builder(var1);
                     var24.setTitle(LocaleController.getString("AppName", 2131558635));
                     var24.setMessage(LocaleController.getString("IncorrectTheme", 2131559664));
                     var24.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                     var2.showDialog(var24.create());
                  }
               } else {
                  label182: {
                     Intent var7;
                     int var8;
                     boolean var10001;
                     MimeTypeMap var28;
                     try {
                        var7 = new Intent("android.intent.action.VIEW");
                        var7.setFlags(1);
                        var28 = MimeTypeMap.getSingleton();
                        var8 = var4.lastIndexOf(46);
                     } catch (Exception var22) {
                        var10001 = false;
                        break label182;
                     }

                     label151: {
                        if (var8 != -1) {
                           try {
                              var5 = var28.getMimeTypeFromExtension(var4.substring(var8 + 1).toLowerCase());
                           } catch (Exception var20) {
                              var10001 = false;
                              break label182;
                           }

                           if (var5 != null) {
                              break label151;
                           }

                           try {
                              var4 = var3.mime_type;
                           } catch (Exception var19) {
                              var10001 = false;
                              break label182;
                           }

                           if (var4 != null) {
                              var5 = var4;

                              try {
                                 if (var4.length() != 0) {
                                    break label151;
                                 }
                              } catch (Exception var21) {
                                 var10001 = false;
                                 break label182;
                              }
                           }
                        }

                        var5 = null;
                     }

                     try {
                        var8 = VERSION.SDK_INT;
                     } catch (Exception var18) {
                        var10001 = false;
                        break label182;
                     }

                     Uri var26;
                     if (var8 >= 24) {
                        try {
                           var26 = FileProvider.getUriForFile(var1, "org.telegram.messenger.provider", var6);
                        } catch (Exception var17) {
                           var10001 = false;
                           break label182;
                        }

                        if (var5 != null) {
                           var4 = var5;
                        } else {
                           var4 = "text/plain";
                        }

                        try {
                           var7.setDataAndType(var26, var4);
                        } catch (Exception var16) {
                           var10001 = false;
                           break label182;
                        }
                     } else {
                        try {
                           var26 = Uri.fromFile(var6);
                        } catch (Exception var15) {
                           var10001 = false;
                           break label182;
                        }

                        if (var5 != null) {
                           var4 = var5;
                        } else {
                           var4 = "text/plain";
                        }

                        try {
                           var7.setDataAndType(var26, var4);
                        } catch (Exception var14) {
                           var10001 = false;
                           break label182;
                        }
                     }

                     if (var5 != null) {
                        try {
                           var1.startActivityForResult(var7, 500);
                           return;
                        } catch (Exception var12) {
                           label187: {
                              label115: {
                                 try {
                                    if (VERSION.SDK_INT >= 24) {
                                       var7.setDataAndType(FileProvider.getUriForFile(var1, "org.telegram.messenger.provider", var6), "text/plain");
                                       break label115;
                                    }
                                 } catch (Exception var11) {
                                    var10001 = false;
                                    break label187;
                                 }

                                 try {
                                    var7.setDataAndType(Uri.fromFile(var6), "text/plain");
                                 } catch (Exception var10) {
                                    var10001 = false;
                                    break label187;
                                 }
                              }

                              try {
                                 var1.startActivityForResult(var7, 500);
                                 return;
                              } catch (Exception var9) {
                                 var10001 = false;
                              }
                           }
                        }
                     } else {
                        try {
                           var1.startActivityForResult(var7, 500);
                           return;
                        } catch (Exception var13) {
                           var10001 = false;
                        }
                     }
                  }

                  if (var1 == null) {
                     return;
                  }

                  AlertDialog.Builder var25 = new AlertDialog.Builder(var1);
                  var25.setTitle(LocaleController.getString("AppName", 2131558635));
                  var25.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var25.setMessage(LocaleController.formatString("NoHandleAppInstalled", 2131559926, var0.getDocument().mime_type));
                  if (var2 != null) {
                     var2.showDialog(var25.create());
                  } else {
                     var25.show();
                  }
               }
            }

         }
      }
   }

   public static void openForView(MessageObject var0, Activity var1) {
      String var2 = var0.getFileName();
      String var3 = var0.messageOwner.attachPath;
      File var11;
      if (var3 != null && var3.length() != 0) {
         var11 = new File(var0.messageOwner.attachPath);
      } else {
         var11 = null;
      }

      File var4;
      label86: {
         if (var11 != null) {
            var4 = var11;
            if (var11.exists()) {
               break label86;
            }
         }

         var4 = FileLoader.getPathToMessage(var0.messageOwner);
      }

      if (var4 != null && var4.exists()) {
         Intent var5;
         String var8;
         label78: {
            var5 = new Intent("android.intent.action.VIEW");
            var5.setFlags(1);
            MimeTypeMap var12 = MimeTypeMap.getSingleton();
            int var6 = var2.lastIndexOf(46);
            if (var6 != -1) {
               var3 = var12.getMimeTypeFromExtension(var2.substring(var6 + 1).toLowerCase());
               if (var3 != null) {
                  var8 = var3;
                  break label78;
               }

               var6 = var0.type;
               if (var6 == 9 || var6 == 0) {
                  var3 = var0.getDocument().mime_type;
               }

               if (var3 != null) {
                  var8 = var3;
                  if (var3.length() != 0) {
                     break label78;
                  }
               }
            }

            var8 = null;
         }

         if (VERSION.SDK_INT >= 26 && var8 != null && var8.equals("application/vnd.android.package-archive") && !ApplicationLoader.applicationContext.getPackageManager().canRequestPackageInstalls()) {
            AlertDialog.Builder var10 = new AlertDialog.Builder(var1);
            var10.setTitle(LocaleController.getString("AppName", 2131558635));
            var10.setMessage(LocaleController.getString("ApkRestricted", 2131558634));
            var10.setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$AndroidUtilities$K8m_Yy_Pa5ZuhXPQCq3Vou7f1Z0(var1));
            var10.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            var10.show();
            return;
         }

         Uri var9;
         if (VERSION.SDK_INT >= 24) {
            var9 = FileProvider.getUriForFile(var1, "org.telegram.messenger.provider", var4);
            if (var8 != null) {
               var3 = var8;
            } else {
               var3 = "text/plain";
            }

            var5.setDataAndType(var9, var3);
         } else {
            var9 = Uri.fromFile(var4);
            if (var8 != null) {
               var3 = var8;
            } else {
               var3 = "text/plain";
            }

            var5.setDataAndType(var9, var3);
         }

         if (var8 != null) {
            try {
               var1.startActivityForResult(var5, 500);
            } catch (Exception var7) {
               if (VERSION.SDK_INT >= 24) {
                  var5.setDataAndType(FileProvider.getUriForFile(var1, "org.telegram.messenger.provider", var4), "text/plain");
               } else {
                  var5.setDataAndType(Uri.fromFile(var4), "text/plain");
               }

               var1.startActivityForResult(var5, 500);
            }
         } else {
            var1.startActivityForResult(var5, 500);
         }
      }

   }

   public static void openForView(TLObject var0, Activity var1) {
      if (var0 != null && var1 != null) {
         String var2 = FileLoader.getAttachFileName(var0);
         File var3 = FileLoader.getPathToAttach(var0, true);
         if (var3 != null && var3.exists()) {
            Intent var4 = new Intent("android.intent.action.VIEW");
            var4.setFlags(1);
            MimeTypeMap var5 = MimeTypeMap.getSingleton();
            int var6 = var2.lastIndexOf(46);
            Uri var7 = null;
            String var8 = var7;
            String var10;
            if (var6 != -1) {
               var8 = var5.getMimeTypeFromExtension(var2.substring(var6 + 1).toLowerCase());
               if (var8 == null) {
                  if (var0 instanceof TLRPC.TL_document) {
                     var10 = ((TLRPC.TL_document)var0).mime_type;
                  } else {
                     var10 = var8;
                  }

                  var8 = var7;
                  if (var10 != null) {
                     if (var10.length() == 0) {
                        var8 = var7;
                     } else {
                        var8 = var10;
                     }
                  }
               }
            }

            if (VERSION.SDK_INT >= 24) {
               var7 = FileProvider.getUriForFile(var1, "org.telegram.messenger.provider", var3);
               if (var8 != null) {
                  var10 = var8;
               } else {
                  var10 = "text/plain";
               }

               var4.setDataAndType(var7, var10);
            } else {
               var7 = Uri.fromFile(var3);
               if (var8 != null) {
                  var10 = var8;
               } else {
                  var10 = "text/plain";
               }

               var4.setDataAndType(var7, var10);
            }

            if (var8 != null) {
               try {
                  var1.startActivityForResult(var4, 500);
               } catch (Exception var9) {
                  if (VERSION.SDK_INT >= 24) {
                     var4.setDataAndType(FileProvider.getUriForFile(var1, "org.telegram.messenger.provider", var3), "text/plain");
                  } else {
                     var4.setDataAndType(Uri.fromFile(var3), "text/plain");
                  }

                  var1.startActivityForResult(var4, 500);
               }
            } else {
               var1.startActivityForResult(var4, 500);
            }
         }
      }

   }

   public static void removeAdjustResize(Activity var0, int var1) {
      if (var0 != null && !isTablet() && adjustOwnerClassGuid == var1) {
         var0.getWindow().setSoftInputMode(32);
      }

   }

   public static SpannableStringBuilder replaceTags(String var0) {
      return replaceTags(var0, 11);
   }

   public static SpannableStringBuilder replaceTags(String var0, int var1, Object... var2) {
      Exception var10000;
      label174: {
         StringBuilder var3;
         boolean var10001;
         try {
            var3 = new StringBuilder(var0);
         } catch (Exception var26) {
            var10000 = var26;
            var10001 = false;
            break label174;
         }

         int var4;
         if ((var1 & 1) != 0) {
            label159:
            while(true) {
               try {
                  var4 = var3.indexOf("<br>");
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label174;
               }

               if (var4 == -1) {
                  while(true) {
                     try {
                        var4 = var3.indexOf("<br/>");
                     } catch (Exception var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label174;
                     }

                     if (var4 == -1) {
                        break label159;
                     }

                     try {
                        var3.replace(var4, var4 + 5, "\n");
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label174;
                     }
                  }
               }

               try {
                  var3.replace(var4, var4 + 4, "\n");
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label174;
               }
            }
         }

         ArrayList var5;
         try {
            var5 = new ArrayList();
         } catch (Exception var21) {
            var10000 = var21;
            var10001 = false;
            break label174;
         }

         if ((var1 & 2) != 0) {
            label137:
            while(true) {
               int var6;
               try {
                  var6 = var3.indexOf("<b>");
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label174;
               }

               int var7;
               if (var6 == -1) {
                  while(true) {
                     try {
                        var4 = var3.indexOf("**");
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label174;
                     }

                     if (var4 == -1) {
                        break label137;
                     }

                     try {
                        var3.replace(var4, var4 + 2, "");
                        var7 = var3.indexOf("**");
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label174;
                     }

                     if (var7 >= 0) {
                        try {
                           var3.replace(var7, var7 + 2, "");
                           var5.add(var4);
                           var5.add(var7);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label174;
                        }
                     }
                  }
               }

               try {
                  var3.replace(var6, var6 + 3, "");
                  var7 = var3.indexOf("</b>");
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label174;
               }

               var4 = var7;
               if (var7 == -1) {
                  try {
                     var4 = var3.indexOf("<b>");
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label174;
                  }
               }

               try {
                  var3.replace(var4, var4 + 4, "");
                  var5.add(var6);
                  var5.add(var4);
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label174;
               }
            }
         }

         if ((var1 & 8) != 0) {
            while(true) {
               try {
                  var4 = var3.indexOf("**");
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label174;
               }

               if (var4 == -1) {
                  break;
               }

               try {
                  var3.replace(var4, var4 + 2, "");
                  var1 = var3.indexOf("**");
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label174;
               }

               if (var1 >= 0) {
                  try {
                     var3.replace(var1, var1 + 2, "");
                     var5.add(var4);
                     var5.add(var1);
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label174;
                  }
               }
            }
         }

         SpannableStringBuilder var27;
         try {
            var27 = new SpannableStringBuilder(var3);
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label174;
         }

         var1 = 0;

         while(true) {
            TypefaceSpan var29;
            try {
               if (var1 >= var5.size() / 2) {
                  return var27;
               }

               var29 = new TypefaceSpan(getTypeface("fonts/rmedium.ttf"));
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }

            var4 = var1 * 2;

            try {
               var27.setSpan(var29, (Integer)var5.get(var4), (Integer)var5.get(var4 + 1), 33);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      Exception var28 = var10000;
      FileLog.e((Throwable)var28);
      return new SpannableStringBuilder(var0);
   }

   public static void requestAdjustResize(Activity var0, int var1) {
      if (var0 != null && !isTablet()) {
         var0.getWindow().setSoftInputMode(16);
         adjustOwnerClassGuid = var1;
      }

   }

   private static double[] rgbToHsv(int var0, int var1, int var2) {
      double var3 = (double)var0;
      Double.isNaN(var3);
      double var5 = var3 / 255.0D;
      var3 = (double)var1;
      Double.isNaN(var3);
      double var7 = var3 / 255.0D;
      var3 = (double)var2;
      Double.isNaN(var3);
      double var9 = var3 / 255.0D;
      if (var5 > var7 && var5 > var9) {
         var3 = var5;
      } else if (var7 > var9) {
         var3 = var7;
      } else {
         var3 = var9;
      }

      double var11;
      if (var5 < var7 && var5 < var9) {
         var11 = var5;
      } else if (var7 < var9) {
         var11 = var7;
      } else {
         var11 = var9;
      }

      double var13 = var3 - var11;
      double var15 = 0.0D;
      double var17;
      if (var3 == 0.0D) {
         var17 = 0.0D;
      } else {
         var17 = var13 / var3;
      }

      if (var3 == var11) {
         var11 = var15;
      } else {
         label64: {
            if (var5 > var7 && var5 > var9) {
               var11 = (var7 - var9) / var13;
               byte var19;
               if (var7 < var9) {
                  var19 = 6;
               } else {
                  var19 = 0;
               }

               var5 = (double)var19;
               Double.isNaN(var5);
            } else {
               if (var7 > var9) {
                  var11 = 2.0D + (var9 - var5) / var13;
                  break label64;
               }

               var11 = (var5 - var7) / var13;
               var5 = 4.0D;
            }

            var11 += var5;
         }

         var11 /= 6.0D;
      }

      return new double[]{var11, var17, var3};
   }

   public static void runOnUIThread(Runnable var0) {
      runOnUIThread(var0, 0L);
   }

   public static void runOnUIThread(Runnable var0, long var1) {
      if (var1 == 0L) {
         ApplicationLoader.applicationHandler.post(var0);
      } else {
         ApplicationLoader.applicationHandler.postDelayed(var0, var1);
      }

   }

   public static void setAdjustResizeToNothing(Activity var0, int var1) {
      if (var0 != null && !isTablet() && adjustOwnerClassGuid == var1) {
         var0.getWindow().setSoftInputMode(48);
      }

   }

   public static void setEnabled(View var0, boolean var1) {
      if (var0 != null) {
         var0.setEnabled(var1);
         if (var0 instanceof ViewGroup) {
            ViewGroup var3 = (ViewGroup)var0;

            for(int var2 = 0; var2 < var3.getChildCount(); ++var2) {
               setEnabled(var3.getChildAt(var2), var1);
            }
         }

      }
   }

   public static int setMyLayerVersion(int var0, int var1) {
      return var0 & -65536 | var1;
   }

   public static int setPeerLayerVersion(int var0, int var1) {
      return var0 & '\uffff' | var1 << 16;
   }

   public static void setRectToRect(Matrix var0, RectF var1, RectF var2, int var3, boolean var4) {
      float var5;
      float var6;
      float var7;
      if (var3 != 90 && var3 != 270) {
         var5 = var2.width() / var1.width();
         var6 = var2.height();
         var7 = var1.height();
      } else {
         var5 = var2.height() / var1.width();
         var6 = var2.width();
         var7 = var1.height();
      }

      var6 /= var7;
      boolean var8;
      if (var5 < var6) {
         var8 = true;
      } else {
         var8 = false;
         var6 = var5;
      }

      if (var4) {
         var0.setTranslate(var2.left, var2.top);
      }

      if (var3 == 90) {
         var0.preRotate(90.0F);
         var0.preTranslate(0.0F, -var2.width());
      } else if (var3 == 180) {
         var0.preRotate(180.0F);
         var0.preTranslate(-var2.width(), -var2.height());
      } else if (var3 == 270) {
         var0.preRotate(270.0F);
         var0.preTranslate(-var2.height(), 0.0F);
      }

      if (var4) {
         var5 = -var1.left * var6;
         var7 = -var1.top * var6;
      } else {
         var5 = var2.left - var1.left * var6;
         var7 = var2.top - var1.top * var6;
      }

      float var9;
      float var10;
      if (var8) {
         var9 = var2.width();
         var10 = var1.width();
      } else {
         var9 = var2.height();
         var10 = var1.height();
      }

      var9 = (var9 - var10 * var6) / 2.0F;
      if (var8) {
         var5 += var9;
      } else {
         var7 += var9;
      }

      var0.preScale(var6, var6);
      if (var4) {
         var0.preTranslate(var5, var7);
      }

   }

   public static void setScrollViewEdgeEffectColor(HorizontalScrollView var0, int var1) {
      if (VERSION.SDK_INT >= 21) {
         Exception var10000;
         label41: {
            boolean var10001;
            Field var2;
            EdgeEffect var9;
            try {
               var2 = HorizontalScrollView.class.getDeclaredField("mEdgeGlowLeft");
               var2.setAccessible(true);
               var9 = (EdgeEffect)var2.get(var0);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label41;
            }

            if (var9 != null) {
               try {
                  var9.setColor(var1);
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label41;
               }
            }

            EdgeEffect var7;
            try {
               var2 = HorizontalScrollView.class.getDeclaredField("mEdgeGlowRight");
               var2.setAccessible(true);
               var7 = (EdgeEffect)var2.get(var0);
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label41;
            }

            if (var7 == null) {
               return;
            }

            try {
               var7.setColor(var1);
               return;
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         Exception var8 = var10000;
         FileLog.e((Throwable)var8);
      }

   }

   public static void setScrollViewEdgeEffectColor(ScrollView var0, int var1) {
      if (VERSION.SDK_INT >= 21) {
         Exception var10000;
         label41: {
            boolean var10001;
            Field var2;
            EdgeEffect var9;
            try {
               var2 = ScrollView.class.getDeclaredField("mEdgeGlowTop");
               var2.setAccessible(true);
               var9 = (EdgeEffect)var2.get(var0);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label41;
            }

            if (var9 != null) {
               try {
                  var9.setColor(var1);
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label41;
               }
            }

            EdgeEffect var7;
            try {
               var2 = ScrollView.class.getDeclaredField("mEdgeGlowBottom");
               var2.setAccessible(true);
               var7 = (EdgeEffect)var2.get(var0);
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label41;
            }

            if (var7 == null) {
               return;
            }

            try {
               var7.setColor(var1);
               return;
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         Exception var8 = var10000;
         FileLog.e((Throwable)var8);
      }

   }

   public static void setViewPagerEdgeEffectColor(ViewPager var0, int var1) {
      if (VERSION.SDK_INT >= 21) {
         boolean var10001;
         Field var2;
         EdgeEffect var8;
         try {
            var2 = ViewPager.class.getDeclaredField("mLeftEdge");
            var2.setAccessible(true);
            var8 = (EdgeEffect)var2.get(var0);
         } catch (Exception var6) {
            var10001 = false;
            return;
         }

         if (var8 != null) {
            try {
               var8.setColor(var1);
            } catch (Exception var5) {
               var10001 = false;
               return;
            }
         }

         EdgeEffect var7;
         try {
            var2 = ViewPager.class.getDeclaredField("mRightEdge");
            var2.setAccessible(true);
            var7 = (EdgeEffect)var2.get(var0);
         } catch (Exception var4) {
            var10001 = false;
            return;
         }

         if (var7 != null) {
            try {
               var7.setColor(var1);
            } catch (Exception var3) {
               var10001 = false;
            }
         }
      }

   }

   public static void setWaitingForCall(boolean param0) {
      // $FF: Couldn't be decompiled
   }

   public static void setWaitingForSms(boolean var0) {
   }

   public static void shakeView(final View var0, final float var1, final int var2) {
      if (var0 != null) {
         if (var2 == 6) {
            var0.setTranslationX(0.0F);
         } else {
            AnimatorSet var3 = new AnimatorSet();
            var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(var0, "translationX", new float[]{(float)dp(var1)})});
            var3.setDuration(50L);
            var3.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  View var3 = var0;
                  float var2x;
                  if (var2 == 5) {
                     var2x = 0.0F;
                  } else {
                     var2x = -var1;
                  }

                  AndroidUtilities.shakeView(var3, var2x, var2 + 1);
               }
            });
            var3.start();
         }
      }
   }

   public static boolean shouldEnableAnimation() {
      int var0 = VERSION.SDK_INT;
      if (var0 >= 26 && var0 < 28) {
         if (((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).isPowerSaveMode()) {
            return false;
         }

         if (Global.getFloat(ApplicationLoader.applicationContext.getContentResolver(), "animator_duration_scale", 1.0F) <= 0.0F) {
            return false;
         }
      }

      return true;
   }

   public static boolean showKeyboard(View var0) {
      if (var0 == null) {
         return false;
      } else {
         try {
            boolean var1 = ((InputMethodManager)var0.getContext().getSystemService("input_method")).showSoftInput(var0, 1);
            return var1;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
            return false;
         }
      }
   }

   public static void showProxyAlert(Activity var0, String var1, String var2, String var3, String var4, String var5) {
      BottomSheet.Builder var6 = new BottomSheet.Builder(var0);
      Runnable var7 = var6.getDismissRunnable();
      var6.setApplyTopPadding(false);
      var6.setApplyBottomPadding(false);
      LinearLayout var8 = new LinearLayout(var0);
      var6.setCustomView(var8);
      var8.setOrientation(1);
      if (!TextUtils.isEmpty(var5)) {
         TextView var9 = new TextView(var0);
         var9.setText(LocaleController.getString("UseProxyTelegramInfo2", 2131560985));
         var9.setTextColor(Theme.getColor("dialogTextGray4"));
         var9.setTextSize(1, 14.0F);
         var9.setGravity(49);
         byte var10;
         if (LocaleController.isRTL) {
            var10 = 5;
         } else {
            var10 = 3;
         }

         var8.addView(var9, LayoutHelper.createLinear(-2, -2, var10 | 48, 17, 8, 17, 8));
         View var14 = new View(var0);
         var14.setBackgroundColor(Theme.getColor("divider"));
         var8.addView(var14, new LayoutParams(-1, 1));
      }

      for(int var17 = 0; var17 < 5; ++var17) {
         String var15 = null;
         String var11;
         if (var17 == 0) {
            var11 = LocaleController.getString("UseProxyAddress", 2131560971);
            var15 = var1;
         } else if (var17 == 1) {
            StringBuilder var16 = new StringBuilder();
            var16.append("");
            var16.append(var2);
            var15 = var16.toString();
            var11 = LocaleController.getString("UseProxyPort", 2131560976);
         } else if (var17 == 2) {
            var11 = LocaleController.getString("UseProxySecret", 2131560977);
            var15 = var5;
         } else if (var17 == 3) {
            var11 = LocaleController.getString("UseProxyUsername", 2131560986);
            var15 = var3;
         } else if (var17 == 4) {
            var11 = LocaleController.getString("UseProxyPassword", 2131560975);
            var15 = var4;
         } else {
            var11 = null;
         }

         if (!TextUtils.isEmpty(var15)) {
            TextDetailSettingsCell var12 = new TextDetailSettingsCell(var0);
            var12.setTextAndValue(var15, var11, true);
            var12.getTextView().setTextColor(Theme.getColor("dialogTextBlack"));
            var12.getValueTextView().setTextColor(Theme.getColor("dialogTextGray3"));
            var8.addView(var12, LayoutHelper.createLinear(-1, -2));
            if (var17 == 2) {
               break;
            }
         }
      }

      PickerBottomLayout var13 = new PickerBottomLayout(var0, false);
      var13.setBackgroundColor(Theme.getColor("dialogBackground"));
      var8.addView(var13, LayoutHelper.createFrame(-1, 48, 83));
      var13.cancelButton.setPadding(dp(18.0F), 0, dp(18.0F), 0);
      var13.cancelButton.setTextColor(Theme.getColor("dialogTextBlue2"));
      var13.cancelButton.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      var13.cancelButton.setOnClickListener(new _$$Lambda$AndroidUtilities$0wQAWULSlGGvo31HEQI8eN6AXOQ(var7));
      var13.doneButtonTextView.setTextColor(Theme.getColor("dialogTextBlue2"));
      var13.doneButton.setPadding(dp(18.0F), 0, dp(18.0F), 0);
      var13.doneButtonBadgeTextView.setVisibility(8);
      var13.doneButtonTextView.setText(LocaleController.getString("ConnectingConnectProxy", 2131559138).toUpperCase());
      var13.doneButton.setOnClickListener(new _$$Lambda$AndroidUtilities$N3FZjh44dp7TBj2YD5HgSuaQoW4(var1, var2, var5, var4, var3, var7));
      var6.show();
   }

   public static int[] toIntArray(List var0) {
      int[] var1 = new int[var0.size()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = (Integer)var0.get(var2);
      }

      return var1;
   }

   @SuppressLint({"WrongConstant"})
   public static void unlockOrientation(Activity var0) {
      if (var0 != null) {
         try {
            if (prevOrientation != -10) {
               var0.setRequestedOrientation(prevOrientation);
               prevOrientation = -10;
            }
         } catch (Exception var1) {
            FileLog.e((Throwable)var1);
         }

      }
   }

   public static void unregisterUpdates() {
   }

   public static class LinkMovementMethodMy extends LinkMovementMethod {
      public boolean onTouchEvent(TextView var1, Spannable var2, MotionEvent var3) {
         try {
            boolean var4 = super.onTouchEvent(var1, var2, var3);
            if (var3.getAction() == 1 || var3.getAction() == 3) {
               Selection.removeSelection(var2);
            }

            return var4;
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
            return false;
         }
      }
   }

   private static class VcardData {
      String name;
      ArrayList phones;
      StringBuilder vcard;

      private VcardData() {
         this.phones = new ArrayList();
         this.vcard = new StringBuilder();
      }

      // $FF: synthetic method
      VcardData(Object var1) {
         this();
      }
   }

   public static class VcardItem {
      public boolean checked = true;
      public String fullData = "";
      public int type;
      public ArrayList vcardData = new ArrayList();

      public String getRawType(boolean var1) {
         int var2 = this.fullData.indexOf(58);
         String var3 = "";
         if (var2 < 0) {
            return "";
         } else {
            String var4 = this.fullData;
            int var5 = 0;
            var4 = var4.substring(0, var2);
            if (this.type == 20) {
               String[] var7 = var4.substring(2).split(";");
               if (var1) {
                  var3 = var7[0];
               } else if (var7.length > 1) {
                  var3 = var7[var7.length - 1];
               }

               return var3;
            } else {
               String[] var6 = var4.split(";");

               for(var3 = var4; var5 < var6.length; ++var5) {
                  if (var6[var5].indexOf(61) < 0) {
                     var3 = var6[var5];
                  }
               }

               return var3;
            }
         }
      }

      public String[] getRawValue() {
         int var1 = this.fullData.indexOf(58);
         byte var2 = 0;
         if (var1 < 0) {
            return new String[0];
         } else {
            String var3 = this.fullData.substring(0, var1);
            String var4 = this.fullData.substring(var1 + 1);
            String[] var5 = var3.split(";");
            var3 = "UTF-8";
            String var6 = null;

            String var8;
            for(var1 = 0; var1 < var5.length; var3 = var8) {
               String[] var7 = var5[var1].split("=");
               if (var7.length != 2) {
                  var8 = var3;
               } else if (var7[0].equals("CHARSET")) {
                  var8 = var7[1];
               } else {
                  var8 = var3;
                  if (var7[0].equals("ENCODING")) {
                     var6 = var7[1];
                     var8 = var3;
                  }
               }

               ++var1;
            }

            String[] var12 = var4.split(";");

            for(var1 = var2; var1 < var12.length; ++var1) {
               if (!TextUtils.isEmpty(var12[var1]) && var6 != null && var6.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                  byte[] var10 = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(var12[var1]));
                  if (var10 != null && var10.length != 0) {
                     String var11;
                     try {
                        var11 = new String(var10, var3);
                     } catch (Exception var9) {
                        continue;
                     }

                     var12[var1] = var11;
                  }
               }
            }

            return var12;
         }
      }

      public String getType() {
         int var1 = this.type;
         if (var1 == 5) {
            return LocaleController.getString("ContactBirthday", 2131559141);
         } else if (var1 == 6) {
            return "ORG".equalsIgnoreCase(this.getRawType(true)) ? LocaleController.getString("ContactJob", 2131559142) : LocaleController.getString("ContactJobTitle", 2131559143);
         } else {
            var1 = this.fullData.indexOf(58);
            if (var1 < 0) {
               return "";
            } else {
               String var2 = this.fullData.substring(0, var1);
               if (this.type == 20) {
                  var2 = var2.substring(2).split(";")[0];
               } else {
                  String[] var3 = var2.split(";");

                  for(var1 = 0; var1 < var3.length; ++var1) {
                     if (var3[var1].indexOf(61) < 0) {
                        var2 = var3[var1];
                     }
                  }

                  if (var2.startsWith("X-")) {
                     var2 = var2.substring(2);
                  }

                  byte var4 = -1;
                  switch(var2.hashCode()) {
                  case -2015525726:
                     if (var2.equals("MOBILE")) {
                        var4 = 2;
                     }
                     break;
                  case 2064738:
                     if (var2.equals("CELL")) {
                        var4 = 3;
                     }
                     break;
                  case 2223327:
                     if (var2.equals("HOME")) {
                        var4 = 1;
                     }
                     break;
                  case 2464291:
                     if (var2.equals("PREF")) {
                        var4 = 0;
                     }
                     break;
                  case 2670353:
                     if (var2.equals("WORK")) {
                        var4 = 5;
                     }
                     break;
                  case 75532016:
                     if (var2.equals("OTHER")) {
                        var4 = 4;
                     }
                  }

                  if (var4 != 0) {
                     if (var4 != 1) {
                        if (var4 != 2 && var4 != 3) {
                           if (var4 != 4) {
                              if (var4 == 5) {
                                 var2 = LocaleController.getString("PhoneWork", 2131560433);
                              }
                           } else {
                              var2 = LocaleController.getString("PhoneOther", 2131560432);
                           }
                        } else {
                           var2 = LocaleController.getString("PhoneMobile", 2131560427);
                        }
                     } else {
                        var2 = LocaleController.getString("PhoneHome", 2131560425);
                     }
                  } else {
                     var2 = LocaleController.getString("PhoneMain", 2131560426);
                  }
               }

               StringBuilder var5 = new StringBuilder();
               var5.append(var2.substring(0, 1).toUpperCase());
               var5.append(var2.substring(1).toLowerCase());
               return var5.toString();
            }
         }
      }

      public String getValue(boolean var1) {
         StringBuilder var2 = new StringBuilder();
         int var3 = this.fullData.indexOf(58);
         if (var3 < 0) {
            return "";
         } else {
            if (var2.length() > 0) {
               var2.append(", ");
            }

            String var4 = this.fullData.substring(0, var3);
            String var5 = this.fullData.substring(var3 + 1);
            String[] var6 = var4.split(";");
            var4 = "UTF-8";
            String var7 = null;

            String var9;
            for(var3 = 0; var3 < var6.length; var4 = var9) {
               String[] var8 = var6[var3].split("=");
               if (var8.length != 2) {
                  var9 = var4;
               } else if (var8[0].equals("CHARSET")) {
                  var9 = var8[1];
               } else {
                  var9 = var4;
                  if (var8[0].equals("ENCODING")) {
                     var7 = var8[1];
                     var9 = var4;
                  }
               }

               ++var3;
            }

            String[] var17 = var5.split(";");
            int var10 = 0;

            boolean var13;
            for(boolean var11 = false; var10 < var17.length; var11 = var13) {
               if (TextUtils.isEmpty(var17[var10])) {
                  var13 = var11;
               } else {
                  if (var7 != null && var7.equalsIgnoreCase("QUOTED-PRINTABLE")) {
                     byte[] var15 = AndroidUtilities.decodeQuotedPrintable(AndroidUtilities.getStringBytes(var17[var10]));
                     if (var15 != null && var15.length != 0) {
                        label72: {
                           try {
                              var5 = new String(var15, var4);
                           } catch (Exception var12) {
                              break label72;
                           }

                           var17[var10] = var5;
                        }
                     }
                  }

                  if (var11 && var2.length() > 0) {
                     var2.append(" ");
                  }

                  var2.append(var17[var10]);
                  var13 = var11;
                  if (!var11) {
                     if (var17[var10].length() > 0) {
                        var13 = true;
                     } else {
                        var13 = false;
                     }
                  }
               }

               ++var10;
            }

            if (var1) {
               var3 = this.type;
               if (var3 == 0) {
                  return PhoneFormat.getInstance().format(var2.toString());
               }

               if (var3 == 5) {
                  String[] var14 = var2.toString().split("T");
                  if (var14.length > 0) {
                     var14 = var14[0].split("-");
                     if (var14.length == 3) {
                        Calendar var16 = Calendar.getInstance();
                        var16.set(1, Utilities.parseInt(var14[0]));
                        var16.set(2, Utilities.parseInt(var14[1]) - 1);
                        var16.set(5, Utilities.parseInt(var14[2]));
                        return LocaleController.getInstance().formatterYearMax.format(var16.getTime());
                     }
                  }
               }
            }

            return var2.toString();
         }
      }
   }
}
