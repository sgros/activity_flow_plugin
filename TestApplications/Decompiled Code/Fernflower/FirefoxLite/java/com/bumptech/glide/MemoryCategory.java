package com.bumptech.glide;

public enum MemoryCategory {
   HIGH(1.5F),
   LOW(0.5F),
   NORMAL(1.0F);

   private float multiplier;

   private MemoryCategory(float var3) {
      this.multiplier = var3;
   }
}
