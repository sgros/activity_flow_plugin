// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;

public class LegendEntry
{
    public Legend.LegendForm form;
    public int formColor;
    public DashPathEffect formLineDashEffect;
    public float formLineWidth;
    public float formSize;
    public String label;
    
    public LegendEntry() {
        this.form = Legend.LegendForm.DEFAULT;
        this.formSize = Float.NaN;
        this.formLineWidth = Float.NaN;
        this.formLineDashEffect = null;
        this.formColor = 1122867;
    }
    
    public LegendEntry(final String label, final Legend.LegendForm form, final float formSize, final float formLineWidth, final DashPathEffect formLineDashEffect, final int formColor) {
        this.form = Legend.LegendForm.DEFAULT;
        this.formSize = Float.NaN;
        this.formLineWidth = Float.NaN;
        this.formLineDashEffect = null;
        this.formColor = 1122867;
        this.label = label;
        this.form = form;
        this.formSize = formSize;
        this.formLineWidth = formLineWidth;
        this.formLineDashEffect = formLineDashEffect;
        this.formColor = formColor;
    }
}
