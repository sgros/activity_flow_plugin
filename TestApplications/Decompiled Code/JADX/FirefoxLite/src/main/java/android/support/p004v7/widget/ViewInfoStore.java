package android.support.p004v7.widget;

import android.support.p001v4.util.ArrayMap;
import android.support.p001v4.util.LongSparseArray;
import android.support.p001v4.util.Pools.Pool;
import android.support.p001v4.util.Pools.SimplePool;
import android.support.p004v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo;
import android.support.p004v7.widget.RecyclerView.ViewHolder;

/* renamed from: android.support.v7.widget.ViewInfoStore */
class ViewInfoStore {
    final ArrayMap<ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap();
    final LongSparseArray<ViewHolder> mOldChangedHolders = new LongSparseArray();

    /* renamed from: android.support.v7.widget.ViewInfoStore$InfoRecord */
    static class InfoRecord {
        static Pool<InfoRecord> sPool = new SimplePool(20);
        int flags;
        ItemHolderInfo postInfo;
        ItemHolderInfo preInfo;

        private InfoRecord() {
        }

        static InfoRecord obtain() {
            InfoRecord infoRecord = (InfoRecord) sPool.acquire();
            return infoRecord == null ? new InfoRecord() : infoRecord;
        }

        static void recycle(InfoRecord infoRecord) {
            infoRecord.flags = 0;
            infoRecord.preInfo = null;
            infoRecord.postInfo = null;
            sPool.release(infoRecord);
        }

        static void drainCache() {
            while (sPool.acquire() != null) {
            }
        }
    }

    /* renamed from: android.support.v7.widget.ViewInfoStore$ProcessCallback */
    interface ProcessCallback {
        void processAppeared(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        void processDisappeared(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        void processPersistent(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        void unused(ViewHolder viewHolder);
    }

    ViewInfoStore() {
    }

    /* Access modifiers changed, original: 0000 */
    public void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    /* Access modifiers changed, original: 0000 */
    public void addToPreLayout(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.preInfo = itemHolderInfo;
        infoRecord.flags |= 4;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isDisappearing(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null || (infoRecord.flags & 1) == 0) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public ItemHolderInfo popFromPreLayout(ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 4);
    }

    /* Access modifiers changed, original: 0000 */
    public ItemHolderInfo popFromPostLayout(ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 8);
    }

    private ItemHolderInfo popFromLayoutStep(ViewHolder viewHolder, int i) {
        int indexOfKey = this.mLayoutHolderMap.indexOfKey(viewHolder);
        if (indexOfKey < 0) {
            return null;
        }
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.valueAt(indexOfKey);
        if (infoRecord == null || (infoRecord.flags & i) == 0) {
            return null;
        }
        ItemHolderInfo itemHolderInfo;
        infoRecord.flags &= ~i;
        if (i == 4) {
            itemHolderInfo = infoRecord.preInfo;
        } else if (i == 8) {
            itemHolderInfo = infoRecord.postInfo;
        } else {
            throw new IllegalArgumentException("Must provide flag PRE or POST");
        }
        if ((infoRecord.flags & 12) == 0) {
            this.mLayoutHolderMap.removeAt(indexOfKey);
            InfoRecord.recycle(infoRecord);
        }
        return itemHolderInfo;
    }

    /* Access modifiers changed, original: 0000 */
    public void addToOldChangeHolders(long j, ViewHolder viewHolder) {
        this.mOldChangedHolders.put(j, viewHolder);
    }

    /* Access modifiers changed, original: 0000 */
    public void addToAppearedInPreLayoutHolders(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.flags |= 2;
        infoRecord.preInfo = itemHolderInfo;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isInPreLayout(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        return (infoRecord == null || (infoRecord.flags & 4) == 0) ? false : true;
    }

    /* Access modifiers changed, original: 0000 */
    public ViewHolder getFromOldChangeHolders(long j) {
        return (ViewHolder) this.mOldChangedHolders.get(j);
    }

    /* Access modifiers changed, original: 0000 */
    public void addToPostLayout(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.postInfo = itemHolderInfo;
        infoRecord.flags |= 8;
    }

    /* Access modifiers changed, original: 0000 */
    public void addToDisappearedInLayout(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecord);
        }
        infoRecord.flags |= 1;
    }

    /* Access modifiers changed, original: 0000 */
    public void removeFromDisappearedInLayout(ViewHolder viewHolder) {
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord != null) {
            infoRecord.flags &= -2;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void process(ProcessCallback processCallback) {
        for (int size = this.mLayoutHolderMap.size() - 1; size >= 0; size--) {
            ViewHolder viewHolder = (ViewHolder) this.mLayoutHolderMap.keyAt(size);
            InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.removeAt(size);
            if ((infoRecord.flags & 3) == 3) {
                processCallback.unused(viewHolder);
            } else if ((infoRecord.flags & 1) != 0) {
                if (infoRecord.preInfo == null) {
                    processCallback.unused(viewHolder);
                } else {
                    processCallback.processDisappeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
                }
            } else if ((infoRecord.flags & 14) == 14) {
                processCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
            } else if ((infoRecord.flags & 12) == 12) {
                processCallback.processPersistent(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
            } else if ((infoRecord.flags & 4) != 0) {
                processCallback.processDisappeared(viewHolder, infoRecord.preInfo, null);
            } else if ((infoRecord.flags & 8) != 0) {
                processCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
            } else {
                int i = infoRecord.flags;
            }
            InfoRecord.recycle(infoRecord);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeViewHolder(ViewHolder viewHolder) {
        for (int size = this.mOldChangedHolders.size() - 1; size >= 0; size--) {
            if (viewHolder == this.mOldChangedHolders.valueAt(size)) {
                this.mOldChangedHolders.removeAt(size);
                break;
            }
        }
        InfoRecord infoRecord = (InfoRecord) this.mLayoutHolderMap.remove(viewHolder);
        if (infoRecord != null) {
            InfoRecord.recycle(infoRecord);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }
}
