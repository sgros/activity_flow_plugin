package org.osmdroid.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTileAreaList implements MapTileContainer, IterableWithSize<Long> {
    private final List<MapTileArea> mList = new ArrayList();

    /* renamed from: org.osmdroid.util.MapTileAreaList$1 */
    class C02661 implements Iterator<Long> {
        private Iterator<Long> mCurrent;
        private int mIndex;

        C02661() {
        }

        public boolean hasNext() {
            Iterator current = getCurrent();
            return current != null && current.hasNext();
        }

        public Long next() {
            long longValue = ((Long) getCurrent().next()).longValue();
            if (!getCurrent().hasNext()) {
                this.mCurrent = null;
            }
            return Long.valueOf(longValue);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private Iterator<Long> getCurrent() {
            Iterator it = this.mCurrent;
            if (it != null) {
                return it;
            }
            if (this.mIndex >= MapTileAreaList.this.mList.size()) {
                return null;
            }
            List access$000 = MapTileAreaList.this.mList;
            int i = this.mIndex;
            this.mIndex = i + 1;
            it = ((MapTileArea) access$000.get(i)).iterator();
            this.mCurrent = it;
            return it;
        }
    }

    public List<MapTileArea> getList() {
        return this.mList;
    }

    public int size() {
        int i = 0;
        for (MapTileArea size : this.mList) {
            i += size.size();
        }
        return i;
    }

    public Iterator<Long> iterator() {
        return new C02661();
    }

    public boolean contains(long j) {
        for (MapTileArea contains : this.mList) {
            if (contains.contains(j)) {
                return true;
            }
        }
        return false;
    }
}
