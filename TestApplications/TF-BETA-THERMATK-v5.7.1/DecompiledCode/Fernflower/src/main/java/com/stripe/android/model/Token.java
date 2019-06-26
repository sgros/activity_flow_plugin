package com.stripe.android.model;

import java.util.Date;

public class Token {
   private final Card mCard;
   private final Date mCreated;
   private final String mId;
   private final boolean mLivemode;
   private final String mType;
   private final boolean mUsed;

   public Token(String var1, boolean var2, Date var3, Boolean var4, Card var5, String var6) {
      this.mId = var1;
      this.mType = var6;
      this.mCreated = var3;
      this.mLivemode = var2;
      this.mCard = var5;
      this.mUsed = var4;
   }

   public String getId() {
      return this.mId;
   }

   public String getType() {
      return this.mType;
   }
}
