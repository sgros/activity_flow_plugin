// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request;

public class ThumbnailRequestCoordinator implements Request, RequestCoordinator
{
    private RequestCoordinator coordinator;
    private Request full;
    private boolean isRunning;
    private Request thumb;
    
    public ThumbnailRequestCoordinator() {
        this(null);
    }
    
    public ThumbnailRequestCoordinator(final RequestCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    
    private boolean parentCanNotifyStatusChanged() {
        return this.coordinator == null || this.coordinator.canNotifyStatusChanged(this);
    }
    
    private boolean parentCanSetImage() {
        return this.coordinator == null || this.coordinator.canSetImage(this);
    }
    
    private boolean parentIsAnyResourceSet() {
        return this.coordinator != null && this.coordinator.isAnyResourceSet();
    }
    
    @Override
    public void begin() {
        this.isRunning = true;
        if (!this.thumb.isRunning()) {
            this.thumb.begin();
        }
        if (this.isRunning && !this.full.isRunning()) {
            this.full.begin();
        }
    }
    
    @Override
    public boolean canNotifyStatusChanged(final Request request) {
        return this.parentCanNotifyStatusChanged() && request.equals(this.full) && !this.isAnyResourceSet();
    }
    
    @Override
    public boolean canSetImage(final Request request) {
        return this.parentCanSetImage() && (request.equals(this.full) || !this.full.isResourceSet());
    }
    
    @Override
    public void clear() {
        this.isRunning = false;
        this.thumb.clear();
        this.full.clear();
    }
    
    @Override
    public boolean isAnyResourceSet() {
        return this.parentIsAnyResourceSet() || this.isResourceSet();
    }
    
    @Override
    public boolean isCancelled() {
        return this.full.isCancelled();
    }
    
    @Override
    public boolean isComplete() {
        return this.full.isComplete() || this.thumb.isComplete();
    }
    
    @Override
    public boolean isEquivalentTo(final Request request) {
        final boolean b = request instanceof ThumbnailRequestCoordinator;
        final boolean b2 = false;
        if (b) {
            final ThumbnailRequestCoordinator thumbnailRequestCoordinator = (ThumbnailRequestCoordinator)request;
            if (this.full == null) {
                final boolean b3 = b2;
                if (thumbnailRequestCoordinator.full != null) {
                    return b3;
                }
            }
            else {
                final boolean b3 = b2;
                if (!this.full.isEquivalentTo(thumbnailRequestCoordinator.full)) {
                    return b3;
                }
            }
            if (this.thumb == null) {
                final boolean b3 = b2;
                if (thumbnailRequestCoordinator.thumb != null) {
                    return b3;
                }
            }
            else {
                final boolean b3 = b2;
                if (!this.thumb.isEquivalentTo(thumbnailRequestCoordinator.thumb)) {
                    return b3;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isResourceSet() {
        return this.full.isResourceSet() || this.thumb.isResourceSet();
    }
    
    @Override
    public boolean isRunning() {
        return this.full.isRunning();
    }
    
    @Override
    public void onRequestSuccess(final Request request) {
        if (request.equals(this.thumb)) {
            return;
        }
        if (this.coordinator != null) {
            this.coordinator.onRequestSuccess(this);
        }
        if (!this.thumb.isComplete()) {
            this.thumb.clear();
        }
    }
    
    @Override
    public void pause() {
        this.isRunning = false;
        this.full.pause();
        this.thumb.pause();
    }
    
    @Override
    public void recycle() {
        this.full.recycle();
        this.thumb.recycle();
    }
    
    public void setRequests(final Request full, final Request thumb) {
        this.full = full;
        this.thumb = thumb;
    }
}
