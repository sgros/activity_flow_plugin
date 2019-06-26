// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

public final class AuxEffectInfo
{
    public final int effectId;
    public final float sendLevel;
    
    public AuxEffectInfo(final int effectId, final float sendLevel) {
        this.effectId = effectId;
        this.sendLevel = sendLevel;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && AuxEffectInfo.class == o.getClass()) {
            final AuxEffectInfo auxEffectInfo = (AuxEffectInfo)o;
            if (this.effectId != auxEffectInfo.effectId || Float.compare(auxEffectInfo.sendLevel, this.sendLevel) != 0) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (527 + this.effectId) * 31 + Float.floatToIntBits(this.sendLevel);
    }
}
