// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import org.mapsforge.map.reader.header.SubFileParameter;

class IndexCacheEntryKey
{
    private final int hashCodeValue;
    private final long indexBlockNumber;
    private final SubFileParameter subFileParameter;
    
    IndexCacheEntryKey(final SubFileParameter subFileParameter, final long indexBlockNumber) {
        this.subFileParameter = subFileParameter;
        this.indexBlockNumber = indexBlockNumber;
        this.hashCodeValue = this.calculateHashCode();
    }
    
    private int calculateHashCode() {
        int hashCode;
        if (this.subFileParameter == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.subFileParameter.hashCode();
        }
        return (hashCode + 217) * 31 + (int)(this.indexBlockNumber ^ this.indexBlockNumber >>> 32);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof IndexCacheEntryKey)) {
                b = false;
            }
            else {
                final IndexCacheEntryKey indexCacheEntryKey = (IndexCacheEntryKey)o;
                if (this.subFileParameter == null && indexCacheEntryKey.subFileParameter != null) {
                    b = false;
                }
                else if (this.subFileParameter != null && !this.subFileParameter.equals(indexCacheEntryKey.subFileParameter)) {
                    b = false;
                }
                else if (this.indexBlockNumber != indexCacheEntryKey.indexBlockNumber) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        return this.hashCodeValue;
    }
}
