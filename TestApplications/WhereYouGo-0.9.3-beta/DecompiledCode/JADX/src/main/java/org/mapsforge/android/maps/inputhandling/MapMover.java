package org.mapsforge.android.maps.inputhandling;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.MotionEvent;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.PausableThread;

public class MapMover extends PausableThread implements Callback {
    private static final int DEFAULT_MOVE_SPEED_FACTOR = 8;
    private static final int FRAME_LENGTH_IN_MS = 15;
    private static final float MOVE_SPEED = 0.2f;
    private static final String THREAD_NAME = "MapMover";
    private static final float TRACKBALL_MOVE_SPEED_FACTOR = 40.0f;
    private final MapView mapView;
    private float moveSpeedFactor = 8.0f;
    private float moveX;
    private float moveY;
    private long timePrevious;

    public MapMover(MapView mapView) {
        this.mapView = mapView;
    }

    public float getMoveSpeedFactor() {
        return this.moveSpeedFactor;
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (!this.mapView.isClickable()) {
            return false;
        }
        if (keyCode == 21) {
            moveLeft();
            return true;
        } else if (keyCode == 22) {
            moveRight();
            return true;
        } else if (keyCode == 19) {
            moveUp();
            return true;
        } else if (keyCode != 20) {
            return false;
        } else {
            moveDown();
            return true;
        }
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int count, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (!this.mapView.isClickable()) {
            return false;
        }
        if (keyCode == 21 || keyCode == 22) {
            this.moveX = 0.0f;
            return true;
        } else if (keyCode != 19 && keyCode != 20) {
            return false;
        } else {
            this.moveY = 0.0f;
            return true;
        }
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (!this.mapView.isClickable() || motionEvent.getAction() != 2) {
            return false;
        }
        this.mapView.getMapViewPosition().moveCenter((motionEvent.getX() * TRACKBALL_MOVE_SPEED_FACTOR) * getMoveSpeedFactor(), (motionEvent.getY() * TRACKBALL_MOVE_SPEED_FACTOR) * getMoveSpeedFactor());
        return true;
    }

    public void setMoveSpeedFactor(float moveSpeedFactor) {
        if (moveSpeedFactor < 0.0f) {
            throw new IllegalArgumentException();
        }
        this.moveSpeedFactor = moveSpeedFactor;
    }

    public void stopMove() {
        this.moveX = 0.0f;
        this.moveY = 0.0f;
    }

    private void moveDown() {
        if (this.moveY > 0.0f) {
            this.moveY = 0.0f;
        } else if (this.moveY == 0.0f) {
            this.moveY = -0.2f * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                notify();
            }
        }
    }

    private void moveLeft() {
        if (this.moveX < 0.0f) {
            this.moveX = 0.0f;
        } else if (this.moveX == 0.0f) {
            this.moveX = MOVE_SPEED * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                notify();
            }
        }
    }

    private void moveRight() {
        if (this.moveX > 0.0f) {
            this.moveX = 0.0f;
        } else if (this.moveX == 0.0f) {
            this.moveX = -0.2f * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                notify();
            }
        }
    }

    private void moveUp() {
        if (this.moveY < 0.0f) {
            this.moveY = 0.0f;
        } else if (this.moveY == 0.0f) {
            this.moveY = MOVE_SPEED * this.moveSpeedFactor;
            this.timePrevious = SystemClock.uptimeMillis();
            synchronized (this) {
                notify();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void afterPause() {
        this.timePrevious = SystemClock.uptimeMillis();
    }

    /* Access modifiers changed, original: protected */
    public void doWork() throws InterruptedException {
        long timeCurrent = SystemClock.uptimeMillis();
        long timeElapsed = timeCurrent - this.timePrevious;
        this.timePrevious = timeCurrent;
        this.mapView.getMapViewPosition().moveCenter(((float) timeElapsed) * this.moveX, ((float) timeElapsed) * this.moveY);
        sleep(15);
    }

    /* Access modifiers changed, original: protected */
    public String getThreadName() {
        return THREAD_NAME;
    }

    /* Access modifiers changed, original: protected */
    public ThreadPriority getThreadPriority() {
        return ThreadPriority.NORMAL;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasWork() {
        return (this.moveX == 0.0f && this.moveY == 0.0f) ? false : true;
    }
}
