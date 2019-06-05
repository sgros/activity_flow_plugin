package org.mozilla.focus.utils;

public class NoRemovableStorageException extends RuntimeException {
    public NoRemovableStorageException(String str) {
        super(str);
    }
}
