package org.mapsforge.map.reader;

final class Deserializer {
    static long getFiveBytesLong(byte[] buffer, int offset) {
        return (((((((long) buffer[offset]) & 255) << 32) | ((((long) buffer[offset + 1]) & 255) << 24)) | ((((long) buffer[offset + 2]) & 255) << 16)) | ((((long) buffer[offset + 3]) & 255) << 8)) | (((long) buffer[offset + 4]) & 255);
    }

    static int getInt(byte[] buffer, int offset) {
        return (((buffer[offset] << 24) | ((buffer[offset + 1] & 255) << 16)) | ((buffer[offset + 2] & 255) << 8)) | (buffer[offset + 3] & 255);
    }

    static long getLong(byte[] buffer, int offset) {
        return ((((((((((long) buffer[offset]) & 255) << 56) | ((((long) buffer[offset + 1]) & 255) << 48)) | ((((long) buffer[offset + 2]) & 255) << 40)) | ((((long) buffer[offset + 3]) & 255) << 32)) | ((((long) buffer[offset + 4]) & 255) << 24)) | ((((long) buffer[offset + 5]) & 255) << 16)) | ((((long) buffer[offset + 6]) & 255) << 8)) | (((long) buffer[offset + 7]) & 255);
    }

    static int getShort(byte[] buffer, int offset) {
        return (buffer[offset] << 8) | (buffer[offset + 1] & 255);
    }

    private Deserializer() {
        throw new IllegalStateException();
    }
}
