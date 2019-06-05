package com.bumptech.glide.load;

public enum DecodeFormat {
   public static final DecodeFormat DEFAULT = PREFER_ARGB_8888_DISALLOW_HARDWARE;
   PREFER_ARGB_8888,
   @Deprecated
   PREFER_ARGB_8888_DISALLOW_HARDWARE,
   PREFER_RGB_565;
}
