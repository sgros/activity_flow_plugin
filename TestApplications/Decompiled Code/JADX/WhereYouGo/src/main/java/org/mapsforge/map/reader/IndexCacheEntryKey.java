package org.mapsforge.map.reader;

import org.mapsforge.map.reader.header.SubFileParameter;

class IndexCacheEntryKey {
    private final int hashCodeValue = calculateHashCode();
    private final long indexBlockNumber;
    private final SubFileParameter subFileParameter;

    IndexCacheEntryKey(SubFileParameter subFileParameter, long indexBlockNumber) {
        this.subFileParameter = subFileParameter;
        this.indexBlockNumber = indexBlockNumber;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IndexCacheEntryKey)) {
            return false;
        }
        IndexCacheEntryKey other = (IndexCacheEntryKey) obj;
        if (this.subFileParameter == null && other.subFileParameter != null) {
            return false;
        }
        if (this.subFileParameter != null && !this.subFileParameter.equals(other.subFileParameter)) {
            return false;
        }
        if (this.indexBlockNumber != other.indexBlockNumber) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    private int calculateHashCode() {
        return (((this.subFileParameter == null ? 0 : this.subFileParameter.hashCode()) + 217) * 31) + ((int) (this.indexBlockNumber ^ (this.indexBlockNumber >>> 32)));
    }
}
