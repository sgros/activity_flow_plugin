package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public final class ShareCompat {
   public static final String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
   public static final String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
   static ShareCompat.ShareCompatImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 16) {
         IMPL = new ShareCompat.ShareCompatImplJB();
      } else if (VERSION.SDK_INT >= 14) {
         IMPL = new ShareCompat.ShareCompatImplICS();
      } else {
         IMPL = new ShareCompat.ShareCompatImplBase();
      }

   }

   private ShareCompat() {
   }

   public static void configureMenuItem(Menu var0, int var1, ShareCompat.IntentBuilder var2) {
      MenuItem var3 = var0.findItem(var1);
      if (var3 == null) {
         throw new IllegalArgumentException("Could not find menu item with id " + var1 + " in the supplied menu");
      } else {
         configureMenuItem(var3, var2);
      }
   }

   public static void configureMenuItem(MenuItem var0, ShareCompat.IntentBuilder var1) {
      IMPL.configureMenuItem(var0, var1);
   }

   public static ComponentName getCallingActivity(Activity var0) {
      ComponentName var1 = var0.getCallingActivity();
      ComponentName var2 = var1;
      if (var1 == null) {
         var2 = (ComponentName)var0.getIntent().getParcelableExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY");
      }

      return var2;
   }

   public static String getCallingPackage(Activity var0) {
      String var1 = var0.getCallingPackage();
      String var2 = var1;
      if (var1 == null) {
         var2 = var0.getIntent().getStringExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE");
      }

      return var2;
   }

   public static class IntentBuilder {
      private Activity mActivity;
      private ArrayList mBccAddresses;
      private ArrayList mCcAddresses;
      private CharSequence mChooserTitle;
      private Intent mIntent;
      private ArrayList mStreams;
      private ArrayList mToAddresses;

      private IntentBuilder(Activity var1) {
         this.mActivity = var1;
         this.mIntent = (new Intent()).setAction("android.intent.action.SEND");
         this.mIntent.putExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE", var1.getPackageName());
         this.mIntent.putExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY", var1.getComponentName());
         this.mIntent.addFlags(524288);
      }

      private void combineArrayExtra(String var1, ArrayList var2) {
         String[] var3 = this.mIntent.getStringArrayExtra(var1);
         int var4;
         if (var3 != null) {
            var4 = var3.length;
         } else {
            var4 = 0;
         }

         String[] var5 = new String[var2.size() + var4];
         var2.toArray(var5);
         if (var3 != null) {
            System.arraycopy(var3, 0, var5, var2.size(), var4);
         }

         this.mIntent.putExtra(var1, var5);
      }

      private void combineArrayExtra(String var1, String[] var2) {
         Intent var3 = this.getIntent();
         String[] var4 = var3.getStringArrayExtra(var1);
         int var5;
         if (var4 != null) {
            var5 = var4.length;
         } else {
            var5 = 0;
         }

         String[] var6 = new String[var2.length + var5];
         if (var4 != null) {
            System.arraycopy(var4, 0, var6, 0, var5);
         }

         System.arraycopy(var2, 0, var6, var5, var2.length);
         var3.putExtra(var1, var6);
      }

      public static ShareCompat.IntentBuilder from(Activity var0) {
         return new ShareCompat.IntentBuilder(var0);
      }

      public ShareCompat.IntentBuilder addEmailBcc(String var1) {
         if (this.mBccAddresses == null) {
            this.mBccAddresses = new ArrayList();
         }

         this.mBccAddresses.add(var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailBcc(String[] var1) {
         this.combineArrayExtra("android.intent.extra.BCC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailCc(String var1) {
         if (this.mCcAddresses == null) {
            this.mCcAddresses = new ArrayList();
         }

         this.mCcAddresses.add(var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailCc(String[] var1) {
         this.combineArrayExtra("android.intent.extra.CC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailTo(String var1) {
         if (this.mToAddresses == null) {
            this.mToAddresses = new ArrayList();
         }

         this.mToAddresses.add(var1);
         return this;
      }

      public ShareCompat.IntentBuilder addEmailTo(String[] var1) {
         this.combineArrayExtra("android.intent.extra.EMAIL", var1);
         return this;
      }

      public ShareCompat.IntentBuilder addStream(Uri var1) {
         Uri var2 = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
         ShareCompat.IntentBuilder var3;
         if (this.mStreams == null && var2 == null) {
            var3 = this.setStream(var1);
         } else {
            if (this.mStreams == null) {
               this.mStreams = new ArrayList();
            }

            if (var2 != null) {
               this.mIntent.removeExtra("android.intent.extra.STREAM");
               this.mStreams.add(var2);
            }

            this.mStreams.add(var1);
            var3 = this;
         }

         return var3;
      }

      public Intent createChooserIntent() {
         return Intent.createChooser(this.getIntent(), this.mChooserTitle);
      }

      Activity getActivity() {
         return this.mActivity;
      }

      public Intent getIntent() {
         boolean var1 = true;
         if (this.mToAddresses != null) {
            this.combineArrayExtra("android.intent.extra.EMAIL", this.mToAddresses);
            this.mToAddresses = null;
         }

         if (this.mCcAddresses != null) {
            this.combineArrayExtra("android.intent.extra.CC", this.mCcAddresses);
            this.mCcAddresses = null;
         }

         if (this.mBccAddresses != null) {
            this.combineArrayExtra("android.intent.extra.BCC", this.mBccAddresses);
            this.mBccAddresses = null;
         }

         if (this.mStreams == null || this.mStreams.size() <= 1) {
            var1 = false;
         }

         boolean var2 = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
         if (!var1 && var2) {
            this.mIntent.setAction("android.intent.action.SEND");
            if (this.mStreams != null && !this.mStreams.isEmpty()) {
               this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)this.mStreams.get(0));
            } else {
               this.mIntent.removeExtra("android.intent.extra.STREAM");
            }

            this.mStreams = null;
         }

         if (var1 && !var2) {
            this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
            if (this.mStreams != null && !this.mStreams.isEmpty()) {
               this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
            } else {
               this.mIntent.removeExtra("android.intent.extra.STREAM");
            }
         }

         return this.mIntent;
      }

      public ShareCompat.IntentBuilder setChooserTitle(@StringRes int var1) {
         return this.setChooserTitle(this.mActivity.getText(var1));
      }

      public ShareCompat.IntentBuilder setChooserTitle(CharSequence var1) {
         this.mChooserTitle = var1;
         return this;
      }

      public ShareCompat.IntentBuilder setEmailBcc(String[] var1) {
         this.mIntent.putExtra("android.intent.extra.BCC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setEmailCc(String[] var1) {
         this.mIntent.putExtra("android.intent.extra.CC", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setEmailTo(String[] var1) {
         if (this.mToAddresses != null) {
            this.mToAddresses = null;
         }

         this.mIntent.putExtra("android.intent.extra.EMAIL", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setHtmlText(String var1) {
         this.mIntent.putExtra("android.intent.extra.HTML_TEXT", var1);
         if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
            this.setText(Html.fromHtml(var1));
         }

         return this;
      }

      public ShareCompat.IntentBuilder setStream(Uri var1) {
         if (!this.mIntent.getAction().equals("android.intent.action.SEND")) {
            this.mIntent.setAction("android.intent.action.SEND");
         }

         this.mStreams = null;
         this.mIntent.putExtra("android.intent.extra.STREAM", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setSubject(String var1) {
         this.mIntent.putExtra("android.intent.extra.SUBJECT", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setText(CharSequence var1) {
         this.mIntent.putExtra("android.intent.extra.TEXT", var1);
         return this;
      }

      public ShareCompat.IntentBuilder setType(String var1) {
         this.mIntent.setType(var1);
         return this;
      }

      public void startChooser() {
         this.mActivity.startActivity(this.createChooserIntent());
      }
   }

   public static class IntentReader {
      private static final String TAG = "IntentReader";
      private Activity mActivity;
      private ComponentName mCallingActivity;
      private String mCallingPackage;
      private Intent mIntent;
      private ArrayList mStreams;

      private IntentReader(Activity var1) {
         this.mActivity = var1;
         this.mIntent = var1.getIntent();
         this.mCallingPackage = ShareCompat.getCallingPackage(var1);
         this.mCallingActivity = ShareCompat.getCallingActivity(var1);
      }

      public static ShareCompat.IntentReader from(Activity var0) {
         return new ShareCompat.IntentReader(var0);
      }

      public ComponentName getCallingActivity() {
         return this.mCallingActivity;
      }

      public Drawable getCallingActivityIcon() {
         Drawable var1 = null;
         if (this.mCallingActivity != null) {
            PackageManager var2 = this.mActivity.getPackageManager();

            Drawable var4;
            try {
               var4 = var2.getActivityIcon(this.mCallingActivity);
            } catch (NameNotFoundException var3) {
               Log.e("IntentReader", "Could not retrieve icon for calling activity", var3);
               return var1;
            }

            var1 = var4;
         }

         return var1;
      }

      public Drawable getCallingApplicationIcon() {
         Drawable var1 = null;
         if (this.mCallingPackage != null) {
            PackageManager var2 = this.mActivity.getPackageManager();

            Drawable var4;
            try {
               var4 = var2.getApplicationIcon(this.mCallingPackage);
            } catch (NameNotFoundException var3) {
               Log.e("IntentReader", "Could not retrieve icon for calling application", var3);
               return var1;
            }

            var1 = var4;
         }

         return var1;
      }

      public CharSequence getCallingApplicationLabel() {
         CharSequence var1 = null;
         if (this.mCallingPackage != null) {
            PackageManager var2 = this.mActivity.getPackageManager();

            CharSequence var4;
            try {
               var4 = var2.getApplicationLabel(var2.getApplicationInfo(this.mCallingPackage, 0));
            } catch (NameNotFoundException var3) {
               Log.e("IntentReader", "Could not retrieve label for calling application", var3);
               return var1;
            }

            var1 = var4;
         }

         return var1;
      }

      public String getCallingPackage() {
         return this.mCallingPackage;
      }

      public String[] getEmailBcc() {
         return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
      }

      public String[] getEmailCc() {
         return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
      }

      public String[] getEmailTo() {
         return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
      }

      public String getHtmlText() {
         String var1 = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
         String var2 = var1;
         if (var1 == null) {
            CharSequence var3 = this.getText();
            if (var3 instanceof Spanned) {
               var2 = Html.toHtml((Spanned)var3);
            } else {
               var2 = var1;
               if (var3 != null) {
                  var2 = ShareCompat.IMPL.escapeHtml(var3);
               }
            }
         }

         return var2;
      }

      public Uri getStream() {
         return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
      }

      public Uri getStream(int var1) {
         if (this.mStreams == null && this.isMultipleShare()) {
            this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
         }

         Uri var2;
         if (this.mStreams != null) {
            var2 = (Uri)this.mStreams.get(var1);
         } else {
            if (var1 != 0) {
               throw new IndexOutOfBoundsException("Stream items available: " + this.getStreamCount() + " index requested: " + var1);
            }

            var2 = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
         }

         return var2;
      }

      public int getStreamCount() {
         if (this.mStreams == null && this.isMultipleShare()) {
            this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
         }

         int var1;
         if (this.mStreams != null) {
            var1 = this.mStreams.size();
         } else if (this.mIntent.hasExtra("android.intent.extra.STREAM")) {
            var1 = 1;
         } else {
            var1 = 0;
         }

         return var1;
      }

      public String getSubject() {
         return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
      }

      public CharSequence getText() {
         return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
      }

      public String getType() {
         return this.mIntent.getType();
      }

      public boolean isMultipleShare() {
         return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
      }

      public boolean isShareIntent() {
         String var1 = this.mIntent.getAction();
         boolean var2;
         if (!"android.intent.action.SEND".equals(var1) && !"android.intent.action.SEND_MULTIPLE".equals(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public boolean isSingleShare() {
         return "android.intent.action.SEND".equals(this.mIntent.getAction());
      }
   }

   interface ShareCompatImpl {
      void configureMenuItem(MenuItem var1, ShareCompat.IntentBuilder var2);

      String escapeHtml(CharSequence var1);
   }

   static class ShareCompatImplBase implements ShareCompat.ShareCompatImpl {
      private static void withinStyle(StringBuilder var0, CharSequence var1, int var2, int var3) {
         for(; var2 < var3; ++var2) {
            char var4 = var1.charAt(var2);
            if (var4 == '<') {
               var0.append("&lt;");
            } else if (var4 == '>') {
               var0.append("&gt;");
            } else if (var4 == '&') {
               var0.append("&amp;");
            } else if (var4 <= '~' && var4 >= ' ') {
               if (var4 != ' ') {
                  var0.append(var4);
               } else {
                  while(var2 + 1 < var3 && var1.charAt(var2 + 1) == ' ') {
                     var0.append("&nbsp;");
                     ++var2;
                  }

                  var0.append(' ');
               }
            } else {
               var0.append("&#" + var4 + ";");
            }
         }

      }

      public void configureMenuItem(MenuItem var1, ShareCompat.IntentBuilder var2) {
         var1.setIntent(var2.createChooserIntent());
      }

      public String escapeHtml(CharSequence var1) {
         StringBuilder var2 = new StringBuilder();
         withinStyle(var2, var1, 0, var1.length());
         return var2.toString();
      }
   }

   static class ShareCompatImplICS extends ShareCompat.ShareCompatImplBase {
      public void configureMenuItem(MenuItem var1, ShareCompat.IntentBuilder var2) {
         ShareCompatICS.configureMenuItem(var1, var2.getActivity(), var2.getIntent());
         if (this.shouldAddChooserIntent(var1)) {
            var1.setIntent(var2.createChooserIntent());
         }

      }

      boolean shouldAddChooserIntent(MenuItem var1) {
         boolean var2;
         if (!var1.hasSubMenu()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   static class ShareCompatImplJB extends ShareCompat.ShareCompatImplICS {
      public String escapeHtml(CharSequence var1) {
         return ShareCompatJB.escapeHtml(var1);
      }

      boolean shouldAddChooserIntent(MenuItem var1) {
         return false;
      }
   }
}
