// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.hardware.SensorEvent;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.view.WindowManager;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;

public class WallpaperParallaxEffect implements SensorEventListener
{
    private Sensor accelerometer;
    private int bufferOffset;
    private Callback callback;
    private boolean enabled;
    private float[] pitchBuffer;
    private float[] rollBuffer;
    private SensorManager sensorManager;
    private WindowManager wm;
    
    public WallpaperParallaxEffect(final Context context) {
        this.rollBuffer = new float[3];
        this.pitchBuffer = new float[3];
        this.wm = (WindowManager)context.getSystemService("window");
        this.sensorManager = (SensorManager)context.getSystemService("sensor");
        this.accelerometer = this.sensorManager.getDefaultSensor(1);
    }
    
    public float getScale(final int n, final int n2) {
        final int dp = AndroidUtilities.dp(16.0f);
        final float n3 = (float)n;
        final float n4 = (float)(dp * 2);
        final float a = (n3 + n4) / n3;
        final float n5 = (float)n2;
        return Math.max(a, (n4 + n5) / n5);
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        final int rotation = this.wm.getDefaultDisplay().getRotation();
        final float[] values = sensorEvent.values;
        final float n = values[0] / 9.80665f;
        final float n2 = values[1] / 9.80665f;
        final float n3 = values[2] / 9.80665f;
        final double y = n;
        final float n4 = n3 * n3;
        final float n5 = (float)(Math.atan2(y, Math.sqrt(n2 * n2 + n4)) / 3.141592653589793 * 2.0);
        final float n6 = (float)(Math.atan2(n2, Math.sqrt(n * n + n4)) / 3.141592653589793 * 2.0);
        float n7 = 0.0f;
        float n8 = 0.0f;
        Label_0166: {
            if (rotation != 0) {
                n7 = n6;
                n8 = n5;
                if (rotation == 1) {
                    break Label_0166;
                }
                if (rotation == 2) {
                    n8 = -n6;
                    n7 = -n5;
                    break Label_0166;
                }
                if (rotation == 3) {
                    n8 = -n5;
                    n7 = n6;
                    break Label_0166;
                }
            }
            n8 = n6;
            n7 = n5;
        }
        final float[] rollBuffer = this.rollBuffer;
        final int bufferOffset = this.bufferOffset;
        rollBuffer[bufferOffset] = n8;
        this.pitchBuffer[bufferOffset] = n7;
        this.bufferOffset = (bufferOffset + 1) % rollBuffer.length;
        int n9 = 0;
        float n10 = 0.0f;
        float n11 = 0.0f;
        float[] rollBuffer2;
        while (true) {
            rollBuffer2 = this.rollBuffer;
            if (n9 >= rollBuffer2.length) {
                break;
            }
            n10 += rollBuffer2[n9];
            n11 += this.pitchBuffer[n9];
            ++n9;
        }
        final float n12 = n10 / rollBuffer2.length;
        final float n13 = n11 / rollBuffer2.length;
        float n15 = 0.0f;
        Label_0288: {
            float n14;
            if (n12 > 1.0f) {
                n14 = 2.0f;
            }
            else {
                n15 = n12;
                if (n12 >= -1.0f) {
                    break Label_0288;
                }
                n14 = -2.0f;
            }
            n15 = n14 - n12;
        }
        final int round = Math.round(n13 * AndroidUtilities.dpf2(16.0f));
        final int round2 = Math.round(n15 * AndroidUtilities.dpf2(16.0f));
        final Callback callback = this.callback;
        if (callback != null) {
            callback.onOffsetsChanged(round, round2);
        }
    }
    
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }
    
    public void setEnabled(final boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            final Sensor accelerometer = this.accelerometer;
            if (accelerometer == null) {
                return;
            }
            if (enabled) {
                this.sensorManager.registerListener((SensorEventListener)this, accelerometer, 1);
            }
            else {
                this.sensorManager.unregisterListener((SensorEventListener)this);
            }
        }
    }
    
    public interface Callback
    {
        void onOffsetsChanged(final int p0, final int p1);
    }
}
