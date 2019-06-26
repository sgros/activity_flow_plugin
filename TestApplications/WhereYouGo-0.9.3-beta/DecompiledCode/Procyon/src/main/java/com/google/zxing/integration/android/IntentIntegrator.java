// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.integration.android;

import android.os.Build$VERSION;
import com.journeyapps.barcodescanner.CaptureActivity;
import android.content.Context;
import java.util.Arrays;
import java.util.List;
import android.annotation.TargetApi;
import java.util.Iterator;
import android.os.Bundle;
import java.io.Serializable;
import android.content.Intent;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import android.app.Fragment;
import android.app.Activity;
import java.util.Collection;

public class IntentIntegrator
{
    public static final Collection<String> ALL_CODE_TYPES;
    public static final Collection<String> DATA_MATRIX_TYPES;
    public static final Collection<String> ONE_D_CODE_TYPES;
    public static final Collection<String> PRODUCT_CODE_TYPES;
    public static final Collection<String> QR_CODE_TYPES;
    public static int REQUEST_CODE;
    private static final String TAG;
    private final Activity activity;
    private Class<?> captureActivity;
    private Collection<String> desiredBarcodeFormats;
    private Fragment fragment;
    private final Map<String, Object> moreExtras;
    private android.support.v4.app.Fragment supportFragment;
    
    static {
        IntentIntegrator.REQUEST_CODE = 49374;
        TAG = IntentIntegrator.class.getSimpleName();
        PRODUCT_CODE_TYPES = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "RSS_14");
        ONE_D_CODE_TYPES = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "CODE_39", "CODE_93", "CODE_128", "ITF", "RSS_14", "RSS_EXPANDED");
        QR_CODE_TYPES = Collections.singleton("QR_CODE");
        DATA_MATRIX_TYPES = Collections.singleton("DATA_MATRIX");
        ALL_CODE_TYPES = null;
    }
    
    public IntentIntegrator(final Activity activity) {
        this.moreExtras = new HashMap<String, Object>(3);
        this.activity = activity;
    }
    
    private void attachMoreExtras(final Intent intent) {
        for (final Map.Entry<String, Object> entry : this.moreExtras.entrySet()) {
            final String s = entry.getKey();
            final Bundle value = entry.getValue();
            if (value instanceof Integer) {
                intent.putExtra(s, (Serializable)value);
            }
            else if (value instanceof Long) {
                intent.putExtra(s, (Serializable)value);
            }
            else if (value instanceof Boolean) {
                intent.putExtra(s, (Serializable)value);
            }
            else if (value instanceof Double) {
                intent.putExtra(s, (Serializable)value);
            }
            else if (value instanceof Float) {
                intent.putExtra(s, (Serializable)value);
            }
            else if (value instanceof Bundle) {
                intent.putExtra(s, (Bundle)value);
            }
            else {
                intent.putExtra(s, value.toString());
            }
        }
    }
    
    @TargetApi(11)
    public static IntentIntegrator forFragment(final Fragment fragment) {
        final IntentIntegrator intentIntegrator = new IntentIntegrator(fragment.getActivity());
        intentIntegrator.fragment = fragment;
        return intentIntegrator;
    }
    
    public static IntentIntegrator forSupportFragment(final android.support.v4.app.Fragment supportFragment) {
        final IntentIntegrator intentIntegrator = new IntentIntegrator(supportFragment.getActivity());
        intentIntegrator.supportFragment = supportFragment;
        return intentIntegrator;
    }
    
    private static List<String> list(final String... a) {
        return Collections.unmodifiableList((List<? extends String>)Arrays.asList((T[])a));
    }
    
    public static IntentResult parseActivityResult(int intExtra, final int n, final Intent intent) {
        Integer value = null;
        IntentResult intentResult;
        if (intExtra == IntentIntegrator.REQUEST_CODE) {
            if (n == -1) {
                final String stringExtra = intent.getStringExtra("SCAN_RESULT");
                final String stringExtra2 = intent.getStringExtra("SCAN_RESULT_FORMAT");
                final byte[] byteArrayExtra = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
                intExtra = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                if (intExtra != Integer.MIN_VALUE) {
                    value = intExtra;
                }
                intentResult = new IntentResult(stringExtra, stringExtra2, byteArrayExtra, value, intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL"), intent.getStringExtra("SCAN_RESULT_IMAGE_PATH"));
            }
            else {
                intentResult = new IntentResult();
            }
        }
        else {
            intentResult = null;
        }
        return intentResult;
    }
    
    public final IntentIntegrator addExtra(final String s, final Object o) {
        this.moreExtras.put(s, o);
        return this;
    }
    
    public Intent createScanIntent() {
        final Intent intent = new Intent((Context)this.activity, (Class)this.getCaptureActivity());
        intent.setAction("com.google.zxing.client.android.SCAN");
        if (this.desiredBarcodeFormats != null) {
            final StringBuilder sb = new StringBuilder();
            for (final String str : this.desiredBarcodeFormats) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(str);
            }
            intent.putExtra("SCAN_FORMATS", sb.toString());
        }
        intent.addFlags(67108864);
        intent.addFlags(524288);
        this.attachMoreExtras(intent);
        return intent;
    }
    
    public Class<?> getCaptureActivity() {
        if (this.captureActivity == null) {
            this.captureActivity = this.getDefaultCaptureActivity();
        }
        return this.captureActivity;
    }
    
    protected Class<?> getDefaultCaptureActivity() {
        return CaptureActivity.class;
    }
    
    public Map<String, ?> getMoreExtras() {
        return this.moreExtras;
    }
    
    public final void initiateScan() {
        this.startActivityForResult(this.createScanIntent(), IntentIntegrator.REQUEST_CODE);
    }
    
    public final void initiateScan(final Collection<String> desiredBarcodeFormats) {
        this.setDesiredBarcodeFormats(desiredBarcodeFormats);
        this.initiateScan();
    }
    
    public IntentIntegrator setBarcodeImageEnabled(final boolean b) {
        this.addExtra("BARCODE_IMAGE_ENABLED", b);
        return this;
    }
    
    public IntentIntegrator setBeepEnabled(final boolean b) {
        this.addExtra("BEEP_ENABLED", b);
        return this;
    }
    
    public IntentIntegrator setCameraId(final int i) {
        if (i >= 0) {
            this.addExtra("SCAN_CAMERA_ID", i);
        }
        return this;
    }
    
    public IntentIntegrator setCaptureActivity(final Class<?> captureActivity) {
        this.captureActivity = captureActivity;
        return this;
    }
    
    public IntentIntegrator setDesiredBarcodeFormats(final Collection<String> desiredBarcodeFormats) {
        this.desiredBarcodeFormats = desiredBarcodeFormats;
        return this;
    }
    
    public IntentIntegrator setOrientationLocked(final boolean b) {
        this.addExtra("SCAN_ORIENTATION_LOCKED", b);
        return this;
    }
    
    public final IntentIntegrator setPrompt(final String s) {
        if (s != null) {
            this.addExtra("PROMPT_MESSAGE", s);
        }
        return this;
    }
    
    public IntentIntegrator setTimeout(final long l) {
        this.addExtra("TIMEOUT", l);
        return this;
    }
    
    protected void startActivity(final Intent intent) {
        if (this.fragment != null) {
            if (Build$VERSION.SDK_INT >= 11) {
                this.fragment.startActivity(intent);
            }
        }
        else if (this.supportFragment != null) {
            this.supportFragment.startActivity(intent);
        }
        else {
            this.activity.startActivity(intent);
        }
    }
    
    protected void startActivityForResult(final Intent intent, final int n) {
        if (this.fragment != null) {
            if (Build$VERSION.SDK_INT >= 11) {
                this.fragment.startActivityForResult(intent, n);
            }
        }
        else if (this.supportFragment != null) {
            this.supportFragment.startActivityForResult(intent, n);
        }
        else {
            this.activity.startActivityForResult(intent, n);
        }
    }
}
