package com.airbnb.lottie.network;

public enum FileExtension {
    JSON(".json"),
    ZIP(".zip");
    
    public final String extension;

    private FileExtension(String str) {
        this.extension = str;
    }

    public String tempExtension() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".temp");
        stringBuilder.append(this.extension);
        return stringBuilder.toString();
    }

    public String toString() {
        return this.extension;
    }
}
