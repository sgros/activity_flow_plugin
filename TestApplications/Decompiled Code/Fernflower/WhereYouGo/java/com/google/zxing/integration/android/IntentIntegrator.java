package com.google.zxing.integration.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import com.journeyapps.barcodescanner.CaptureActivity;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IntentIntegrator {
   public static final Collection ALL_CODE_TYPES = null;
   public static final Collection DATA_MATRIX_TYPES = Collections.singleton("DATA_MATRIX");
   public static final Collection ONE_D_CODE_TYPES = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "CODE_39", "CODE_93", "CODE_128", "ITF", "RSS_14", "RSS_EXPANDED");
   public static final Collection PRODUCT_CODE_TYPES = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "RSS_14");
   public static final Collection QR_CODE_TYPES = Collections.singleton("QR_CODE");
   public static int REQUEST_CODE = 49374;
   private static final String TAG = IntentIntegrator.class.getSimpleName();
   private final Activity activity;
   private Class captureActivity;
   private Collection desiredBarcodeFormats;
   private Fragment fragment;
   private final Map moreExtras = new HashMap(3);
   private android.support.v4.app.Fragment supportFragment;

   public IntentIntegrator(Activity var1) {
      this.activity = var1;
   }

   private void attachMoreExtras(Intent var1) {
      Iterator var2 = this.moreExtras.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = (String)var3.getKey();
         Object var5 = var3.getValue();
         if (var5 instanceof Integer) {
            var1.putExtra(var4, (Integer)var5);
         } else if (var5 instanceof Long) {
            var1.putExtra(var4, (Long)var5);
         } else if (var5 instanceof Boolean) {
            var1.putExtra(var4, (Boolean)var5);
         } else if (var5 instanceof Double) {
            var1.putExtra(var4, (Double)var5);
         } else if (var5 instanceof Float) {
            var1.putExtra(var4, (Float)var5);
         } else if (var5 instanceof Bundle) {
            var1.putExtra(var4, (Bundle)var5);
         } else {
            var1.putExtra(var4, var5.toString());
         }
      }

   }

   @TargetApi(11)
   public static IntentIntegrator forFragment(Fragment var0) {
      IntentIntegrator var1 = new IntentIntegrator(var0.getActivity());
      var1.fragment = var0;
      return var1;
   }

   public static IntentIntegrator forSupportFragment(android.support.v4.app.Fragment var0) {
      IntentIntegrator var1 = new IntentIntegrator(var0.getActivity());
      var1.supportFragment = var0;
      return var1;
   }

   private static List list(String... var0) {
      return Collections.unmodifiableList(Arrays.asList(var0));
   }

   public static IntentResult parseActivityResult(int var0, int var1, Intent var2) {
      Integer var3 = null;
      IntentResult var7;
      if (var0 == REQUEST_CODE) {
         if (var1 == -1) {
            String var4 = var2.getStringExtra("SCAN_RESULT");
            String var5 = var2.getStringExtra("SCAN_RESULT_FORMAT");
            byte[] var6 = var2.getByteArrayExtra("SCAN_RESULT_BYTES");
            var0 = var2.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
            if (var0 != Integer.MIN_VALUE) {
               var3 = var0;
            }

            var7 = new IntentResult(var4, var5, var6, var3, var2.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL"), var2.getStringExtra("SCAN_RESULT_IMAGE_PATH"));
         } else {
            var7 = new IntentResult();
         }
      } else {
         var7 = null;
      }

      return var7;
   }

   public final IntentIntegrator addExtra(String var1, Object var2) {
      this.moreExtras.put(var1, var2);
      return this;
   }

   public Intent createScanIntent() {
      Intent var1 = new Intent(this.activity, this.getCaptureActivity());
      var1.setAction("com.google.zxing.client.android.SCAN");
      if (this.desiredBarcodeFormats != null) {
         StringBuilder var2 = new StringBuilder();

         String var4;
         for(Iterator var3 = this.desiredBarcodeFormats.iterator(); var3.hasNext(); var2.append(var4)) {
            var4 = (String)var3.next();
            if (var2.length() > 0) {
               var2.append(',');
            }
         }

         var1.putExtra("SCAN_FORMATS", var2.toString());
      }

      var1.addFlags(67108864);
      var1.addFlags(524288);
      this.attachMoreExtras(var1);
      return var1;
   }

   public Class getCaptureActivity() {
      if (this.captureActivity == null) {
         this.captureActivity = this.getDefaultCaptureActivity();
      }

      return this.captureActivity;
   }

   protected Class getDefaultCaptureActivity() {
      return CaptureActivity.class;
   }

   public Map getMoreExtras() {
      return this.moreExtras;
   }

   public final void initiateScan() {
      this.startActivityForResult(this.createScanIntent(), REQUEST_CODE);
   }

   public final void initiateScan(Collection var1) {
      this.setDesiredBarcodeFormats(var1);
      this.initiateScan();
   }

   public IntentIntegrator setBarcodeImageEnabled(boolean var1) {
      this.addExtra("BARCODE_IMAGE_ENABLED", var1);
      return this;
   }

   public IntentIntegrator setBeepEnabled(boolean var1) {
      this.addExtra("BEEP_ENABLED", var1);
      return this;
   }

   public IntentIntegrator setCameraId(int var1) {
      if (var1 >= 0) {
         this.addExtra("SCAN_CAMERA_ID", var1);
      }

      return this;
   }

   public IntentIntegrator setCaptureActivity(Class var1) {
      this.captureActivity = var1;
      return this;
   }

   public IntentIntegrator setDesiredBarcodeFormats(Collection var1) {
      this.desiredBarcodeFormats = var1;
      return this;
   }

   public IntentIntegrator setOrientationLocked(boolean var1) {
      this.addExtra("SCAN_ORIENTATION_LOCKED", var1);
      return this;
   }

   public final IntentIntegrator setPrompt(String var1) {
      if (var1 != null) {
         this.addExtra("PROMPT_MESSAGE", var1);
      }

      return this;
   }

   public IntentIntegrator setTimeout(long var1) {
      this.addExtra("TIMEOUT", var1);
      return this;
   }

   protected void startActivity(Intent var1) {
      if (this.fragment != null) {
         if (VERSION.SDK_INT >= 11) {
            this.fragment.startActivity(var1);
         }
      } else if (this.supportFragment != null) {
         this.supportFragment.startActivity(var1);
      } else {
         this.activity.startActivity(var1);
      }

   }

   protected void startActivityForResult(Intent var1, int var2) {
      if (this.fragment != null) {
         if (VERSION.SDK_INT >= 11) {
            this.fragment.startActivityForResult(var1, var2);
         }
      } else if (this.supportFragment != null) {
         this.supportFragment.startActivityForResult(var1, var2);
      } else {
         this.activity.startActivityForResult(var1, var2);
      }

   }
}
