package com.squareup.leakcanary;

public final class RefWatcher {
    public static final RefWatcher DISABLED = new RefWatcher();

    public void watch(Object obj) {
    }

    public void watch(Object obj, String str) {
    }

    private RefWatcher() {
    }
}
