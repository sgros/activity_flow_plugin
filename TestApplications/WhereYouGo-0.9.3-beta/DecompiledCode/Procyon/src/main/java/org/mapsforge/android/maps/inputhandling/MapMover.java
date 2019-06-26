// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.inputhandling;

import android.view.MotionEvent;
import android.view.KeyEvent;
import android.os.SystemClock;
import org.mapsforge.android.maps.MapView;
import android.view.KeyEvent$Callback;
import org.mapsforge.android.maps.PausableThread;

public class MapMover extends PausableThread implements KeyEvent$Callback
{
    private static final int DEFAULT_MOVE_SPEED_FACTOR = 8;
    private static final int FRAME_LENGTH_IN_MS = 15;
    private static final float MOVE_SPEED = 0.2f;
    private static final String THREAD_NAME = "MapMover";
    private static final float TRACKBALL_MOVE_SPEED_FACTOR = 40.0f;
    private final MapView mapView;
    private float moveSpeedFactor;
    private float moveX;
    private float moveY;
    private long timePrevious;
    
    public MapMover(final MapView mapView) {
        this.mapView = mapView;
        this.moveSpeedFactor = 8.0f;
    }
    
    private void moveDown() {
        if (this.moveY > 0.0f) {
            this.moveY = 0.0f;
        }
        else if (this.moveY == 0.0f) {
            this.moveY = -0.2f * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                this.notify();
            }
        }
    }
    
    private void moveLeft() {
        if (this.moveX < 0.0f) {
            this.moveX = 0.0f;
        }
        else if (this.moveX == 0.0f) {
            this.moveX = 0.2f * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                this.notify();
            }
        }
    }
    
    private void moveRight() {
        if (this.moveX > 0.0f) {
            this.moveX = 0.0f;
        }
        else if (this.moveX == 0.0f) {
            this.moveX = -0.2f * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                this.notify();
            }
        }
    }
    
    private void moveUp() {
        if (this.moveY < 0.0f) {
            this.moveY = 0.0f;
        }
        else if (this.moveY == 0.0f) {
            this.moveY = 0.2f * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                this.notify();
            }
        }
    }
    
    @Override
    protected void afterPause() {
        this.timePrevious = SystemClock.uptimeMillis();
    }
    
    @Override
    protected void doWork() throws InterruptedException {
        final long uptimeMillis = SystemClock.uptimeMillis();
        final long n = uptimeMillis - this.timePrevious;
        this.timePrevious = uptimeMillis;
        this.mapView.getMapViewPosition().moveCenter(n * this.moveX, n * this.moveY);
        Thread.sleep(15L);
    }
    
    public float getMoveSpeedFactor() {
        return this.moveSpeedFactor;
    }
    
    @Override
    protected String getThreadName() {
        return "MapMover";
    }
    
    @Override
    protected ThreadPriority getThreadPriority() {
        return ThreadPriority.NORMAL;
    }
    
    @Override
    protected boolean hasWork() {
        return this.moveX != 0.0f || this.moveY != 0.0f;
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        boolean b = false;
        if (this.mapView.isClickable()) {
            if (n == 21) {
                this.moveLeft();
                b = true;
            }
            else if (n == 22) {
                this.moveRight();
                b = true;
            }
            else if (n == 19) {
                this.moveUp();
                b = true;
            }
            else if (n == 20) {
                this.moveDown();
                b = true;
            }
        }
        return b;
    }
    
    public boolean onKeyLongPress(final int n, final KeyEvent keyEvent) {
        return false;
    }
    
    public boolean onKeyMultiple(final int n, final int n2, final KeyEvent keyEvent) {
        return false;
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        boolean b = false;
        if (this.mapView.isClickable()) {
            if (n == 21 || n == 22) {
                this.moveX = 0.0f;
                b = true;
            }
            else if (n == 19 || n == 20) {
                this.moveY = 0.0f;
                b = true;
            }
        }
        return b;
    }
    
    public boolean onTrackballEvent(final MotionEvent motionEvent) {
        boolean b = false;
        if (this.mapView.isClickable() && motionEvent.getAction() == 2) {
            this.mapView.getMapViewPosition().moveCenter(motionEvent.getX() * 40.0f * this.getMoveSpeedFactor(), motionEvent.getY() * 40.0f * this.getMoveSpeedFactor());
            b = true;
        }
        return b;
    }
    
    public void setMoveSpeedFactor(final float moveSpeedFactor) {
        if (moveSpeedFactor < 0.0f) {
            throw new IllegalArgumentException();
        }
        this.moveSpeedFactor = moveSpeedFactor;
    }
    
    public void stopMove() {
        this.moveX = 0.0f;
        this.moveY = 0.0f;
    }
}
