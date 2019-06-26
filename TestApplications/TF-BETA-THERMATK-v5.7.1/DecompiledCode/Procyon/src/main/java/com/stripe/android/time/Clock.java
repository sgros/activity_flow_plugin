// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.time;

import java.util.Calendar;

public class Clock
{
    private static Clock instance;
    protected Calendar calendarInstance;
    
    private Calendar _calendarInstance() {
        final Calendar calendarInstance = this.calendarInstance;
        Calendar instance;
        if (calendarInstance != null) {
            instance = (Calendar)calendarInstance.clone();
        }
        else {
            instance = Calendar.getInstance();
        }
        return instance;
    }
    
    public static Calendar getCalendarInstance() {
        return getInstance()._calendarInstance();
    }
    
    protected static Clock getInstance() {
        if (Clock.instance == null) {
            Clock.instance = new Clock();
        }
        return Clock.instance;
    }
}
