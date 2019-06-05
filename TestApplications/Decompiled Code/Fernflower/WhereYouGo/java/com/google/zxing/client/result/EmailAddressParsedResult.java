package com.google.zxing.client.result;

public final class EmailAddressParsedResult extends ParsedResult {
   private final String[] bccs;
   private final String body;
   private final String[] ccs;
   private final String subject;
   private final String[] tos;

   EmailAddressParsedResult(String var1) {
      this(new String[]{var1}, (String[])null, (String[])null, (String)null, (String)null);
   }

   EmailAddressParsedResult(String[] var1, String[] var2, String[] var3, String var4, String var5) {
      super(ParsedResultType.EMAIL_ADDRESS);
      this.tos = var1;
      this.ccs = var2;
      this.bccs = var3;
      this.subject = var4;
      this.body = var5;
   }

   public String[] getBCCs() {
      return this.bccs;
   }

   public String getBody() {
      return this.body;
   }

   public String[] getCCs() {
      return this.ccs;
   }

   public String getDisplayResult() {
      StringBuilder var1 = new StringBuilder(30);
      maybeAppend(this.tos, var1);
      maybeAppend(this.ccs, var1);
      maybeAppend(this.bccs, var1);
      maybeAppend(this.subject, var1);
      maybeAppend(this.body, var1);
      return var1.toString();
   }

   @Deprecated
   public String getEmailAddress() {
      String var1;
      if (this.tos != null && this.tos.length != 0) {
         var1 = this.tos[0];
      } else {
         var1 = null;
      }

      return var1;
   }

   @Deprecated
   public String getMailtoURI() {
      return "mailto:";
   }

   public String getSubject() {
      return this.subject;
   }

   public String[] getTos() {
      return this.tos;
   }
}
