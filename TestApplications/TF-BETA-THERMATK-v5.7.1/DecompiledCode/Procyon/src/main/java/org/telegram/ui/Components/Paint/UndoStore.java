// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import org.telegram.messenger.AndroidUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public class UndoStore
{
    private UndoStoreDelegate delegate;
    private List<UUID> operations;
    private Map<UUID, Runnable> uuidToOperationMap;
    
    public UndoStore() {
        this.uuidToOperationMap = new HashMap<UUID, Runnable>();
        this.operations = new ArrayList<UUID>();
    }
    
    private void notifyOfHistoryChanges() {
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (UndoStore.this.delegate != null) {
                    UndoStore.this.delegate.historyChanged();
                }
            }
        });
    }
    
    public boolean canUndo() {
        return this.operations.isEmpty() ^ true;
    }
    
    public void registerUndo(final UUID uuid, final Runnable runnable) {
        this.uuidToOperationMap.put(uuid, runnable);
        this.operations.add(uuid);
        this.notifyOfHistoryChanges();
    }
    
    public void reset() {
        this.operations.clear();
        this.uuidToOperationMap.clear();
        this.notifyOfHistoryChanges();
    }
    
    public void setDelegate(final UndoStoreDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void undo() {
        if (this.operations.size() == 0) {
            return;
        }
        final int n = this.operations.size() - 1;
        final UUID uuid = this.operations.get(n);
        final Runnable runnable = this.uuidToOperationMap.get(uuid);
        this.uuidToOperationMap.remove(uuid);
        this.operations.remove(n);
        runnable.run();
        this.notifyOfHistoryChanges();
    }
    
    public void unregisterUndo(final UUID uuid) {
        this.uuidToOperationMap.remove(uuid);
        this.operations.remove(uuid);
        this.notifyOfHistoryChanges();
    }
    
    public interface UndoStoreDelegate
    {
        void historyChanged();
    }
}
