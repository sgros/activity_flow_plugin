// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

import java.util.Iterator;
import java.util.Collection;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.Set;
import com.bumptech.glide.request.Request;
import java.util.List;

public class RequestTracker
{
    private boolean isPaused;
    private final List<Request> pendingRequests;
    private final Set<Request> requests;
    
    public RequestTracker() {
        this.requests = Collections.newSetFromMap(new WeakHashMap<Request, Boolean>());
        this.pendingRequests = new ArrayList<Request>();
    }
    
    public boolean clearRemoveAndRecycle(final Request request) {
        boolean b = false;
        if (request == null) {
            return false;
        }
        final boolean remove = this.requests.remove(request);
        if (this.pendingRequests.remove(request) || remove) {
            b = true;
        }
        if (b) {
            request.clear();
            request.recycle();
        }
        return b;
    }
    
    public void clearRequests() {
        final Iterator<Request> iterator = Util.getSnapshot(this.requests).iterator();
        while (iterator.hasNext()) {
            this.clearRemoveAndRecycle(iterator.next());
        }
        this.pendingRequests.clear();
    }
    
    public void pauseRequests() {
        this.isPaused = true;
        for (final Request request : Util.getSnapshot(this.requests)) {
            if (request.isRunning()) {
                request.pause();
                this.pendingRequests.add(request);
            }
        }
    }
    
    public void restartRequests() {
        for (final Request request : Util.getSnapshot(this.requests)) {
            if (!request.isComplete() && !request.isCancelled()) {
                request.pause();
                if (!this.isPaused) {
                    request.begin();
                }
                else {
                    this.pendingRequests.add(request);
                }
            }
        }
    }
    
    public void resumeRequests() {
        this.isPaused = false;
        for (final Request request : Util.getSnapshot(this.requests)) {
            if (!request.isComplete() && !request.isCancelled() && !request.isRunning()) {
                request.begin();
            }
        }
        this.pendingRequests.clear();
    }
    
    public void runRequest(final Request request) {
        this.requests.add(request);
        if (!this.isPaused) {
            request.begin();
        }
        else {
            this.pendingRequests.add(request);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("{numRequests=");
        sb.append(this.requests.size());
        sb.append(", isPaused=");
        sb.append(this.isPaused);
        sb.append("}");
        return sb.toString();
    }
}
