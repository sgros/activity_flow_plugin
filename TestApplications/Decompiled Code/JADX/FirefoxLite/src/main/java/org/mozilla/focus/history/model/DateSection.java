package org.mozilla.focus.history.model;

public class DateSection {
    private long timestamp;

    public DateSection(long j) {
        this.timestamp = j;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
