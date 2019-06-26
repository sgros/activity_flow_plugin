// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class MapTileAreaList implements MapTileContainer, IterableWithSize<Long>
{
    private final List<MapTileArea> mList;
    
    public MapTileAreaList() {
        this.mList = new ArrayList<MapTileArea>();
    }
    
    @Override
    public boolean contains(final long n) {
        final Iterator<MapTileArea> iterator = this.mList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().contains(n)) {
                return true;
            }
        }
        return false;
    }
    
    public List<MapTileArea> getList() {
        return this.mList;
    }
    
    @Override
    public Iterator<Long> iterator() {
        return new Iterator<Long>() {
            private Iterator<Long> mCurrent;
            private int mIndex;
            
            private Iterator<Long> getCurrent() {
                final Iterator<Long> mCurrent = this.mCurrent;
                if (mCurrent != null) {
                    return mCurrent;
                }
                if (this.mIndex < MapTileAreaList.this.mList.size()) {
                    return this.mCurrent = MapTileAreaList.this.mList.get(this.mIndex++).iterator();
                }
                return null;
            }
            
            @Override
            public boolean hasNext() {
                final Iterator<Long> current = this.getCurrent();
                return current != null && current.hasNext();
            }
            
            @Override
            public Long next() {
                final long longValue = this.getCurrent().next();
                if (!this.getCurrent().hasNext()) {
                    this.mCurrent = null;
                }
                return longValue;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public int size() {
        final Iterator<MapTileArea> iterator = this.mList.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            n += iterator.next().size();
        }
        return n;
    }
}
