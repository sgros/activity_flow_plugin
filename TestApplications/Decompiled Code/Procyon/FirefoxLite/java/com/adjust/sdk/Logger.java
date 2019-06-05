// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.util.Arrays;
import android.util.Log;
import java.util.Locale;

public class Logger implements ILogger
{
    private static String formatErrorMessage = "Error formating log message: %s, with params: %s";
    private boolean isProductionEnvironment;
    private LogLevel logLevel;
    private boolean logLevelLocked;
    
    public Logger() {
        this.isProductionEnvironment = false;
        this.logLevelLocked = false;
        this.setLogLevel(LogLevel.INFO, this.isProductionEnvironment);
    }
    
    @Override
    public void Assert(final String format, final Object... array) {
        if (this.isProductionEnvironment) {
            return;
        }
        if (this.logLevel.androidLogLevel <= 7) {
            try {
                Log.println(7, "Adjust", String.format(Locale.US, format, array));
            }
            catch (Exception ex) {
                Log.e("Adjust", String.format(Locale.US, Logger.formatErrorMessage, format, Arrays.toString(array)));
            }
        }
    }
    
    @Override
    public void debug(final String format, final Object... array) {
        if (this.isProductionEnvironment) {
            return;
        }
        if (this.logLevel.androidLogLevel <= 3) {
            try {
                Log.d("Adjust", String.format(Locale.US, format, array));
            }
            catch (Exception ex) {
                Log.e("Adjust", String.format(Locale.US, Logger.formatErrorMessage, format, Arrays.toString(array)));
            }
        }
    }
    
    @Override
    public void error(final String format, final Object... array) {
        if (this.isProductionEnvironment) {
            return;
        }
        if (this.logLevel.androidLogLevel <= 6) {
            try {
                Log.e("Adjust", String.format(Locale.US, format, array));
            }
            catch (Exception ex) {
                Log.e("Adjust", String.format(Locale.US, Logger.formatErrorMessage, format, Arrays.toString(array)));
            }
        }
    }
    
    @Override
    public void info(final String format, final Object... array) {
        if (this.isProductionEnvironment) {
            return;
        }
        if (this.logLevel.androidLogLevel <= 4) {
            try {
                Log.i("Adjust", String.format(Locale.US, format, array));
            }
            catch (Exception ex) {
                Log.e("Adjust", String.format(Locale.US, Logger.formatErrorMessage, format, Arrays.toString(array)));
            }
        }
    }
    
    @Override
    public void lockLogLevel() {
        this.logLevelLocked = true;
    }
    
    @Override
    public void setLogLevel(final LogLevel logLevel, final boolean isProductionEnvironment) {
        if (this.logLevelLocked) {
            return;
        }
        this.logLevel = logLevel;
        this.isProductionEnvironment = isProductionEnvironment;
    }
    
    @Override
    public void setLogLevelString(final String s, final boolean b) {
        if (s != null) {
            try {
                this.setLogLevel(LogLevel.valueOf(s.toUpperCase(Locale.US)), b);
            }
            catch (IllegalArgumentException ex) {
                this.error("Malformed logLevel '%s', falling back to 'info'", s);
            }
        }
    }
    
    @Override
    public void verbose(final String format, final Object... array) {
        if (this.isProductionEnvironment) {
            return;
        }
        if (this.logLevel.androidLogLevel <= 2) {
            try {
                Log.v("Adjust", String.format(Locale.US, format, array));
            }
            catch (Exception ex) {
                Log.e("Adjust", String.format(Locale.US, Logger.formatErrorMessage, format, Arrays.toString(array)));
            }
        }
    }
    
    @Override
    public void warn(final String format, final Object... array) {
        if (this.isProductionEnvironment) {
            return;
        }
        if (this.logLevel.androidLogLevel <= 5) {
            try {
                Log.w("Adjust", String.format(Locale.US, format, array));
            }
            catch (Exception ex) {
                Log.e("Adjust", String.format(Locale.US, Logger.formatErrorMessage, format, Arrays.toString(array)));
            }
        }
    }
    
    @Override
    public void warnInProduction(final String format, final Object... array) {
        if (this.logLevel.androidLogLevel <= 5) {
            try {
                Log.w("Adjust", String.format(Locale.US, format, array));
            }
            catch (Exception ex) {
                Log.e("Adjust", String.format(Locale.US, Logger.formatErrorMessage, format, Arrays.toString(array)));
            }
        }
    }
}
