package com.bumptech.glide.manager;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RequestTracker {
    private boolean isPaused;
    private final List<Request> pendingRequests = new ArrayList();
    private final Set<Request> requests = Collections.newSetFromMap(new WeakHashMap());

    public void runRequest(Request request) {
        this.requests.add(request);
        if (this.isPaused) {
            this.pendingRequests.add(request);
        } else {
            request.begin();
        }
    }

    public boolean clearRemoveAndRecycle(Request request) {
        boolean z = false;
        if (request == null) {
            return false;
        }
        boolean remove = this.requests.remove(request);
        if (this.pendingRequests.remove(request) || remove) {
            z = true;
        }
        if (z) {
            request.clear();
            request.recycle();
        }
        return z;
    }

    public void pauseRequests() {
        this.isPaused = true;
        for (Request request : Util.getSnapshot(this.requests)) {
            if (request.isRunning()) {
                request.pause();
                this.pendingRequests.add(request);
            }
        }
    }

    public void resumeRequests() {
        this.isPaused = false;
        for (Request request : Util.getSnapshot(this.requests)) {
            if (!(request.isComplete() || request.isCancelled() || request.isRunning())) {
                request.begin();
            }
        }
        this.pendingRequests.clear();
    }

    public void clearRequests() {
        for (Request clearRemoveAndRecycle : Util.getSnapshot(this.requests)) {
            clearRemoveAndRecycle(clearRemoveAndRecycle);
        }
        this.pendingRequests.clear();
    }

    public void restartRequests() {
        for (Request request : Util.getSnapshot(this.requests)) {
            if (!(request.isComplete() || request.isCancelled())) {
                request.pause();
                if (this.isPaused) {
                    this.pendingRequests.add(request);
                } else {
                    request.begin();
                }
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("{numRequests=");
        stringBuilder.append(this.requests.size());
        stringBuilder.append(", isPaused=");
        stringBuilder.append(this.isPaused);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
