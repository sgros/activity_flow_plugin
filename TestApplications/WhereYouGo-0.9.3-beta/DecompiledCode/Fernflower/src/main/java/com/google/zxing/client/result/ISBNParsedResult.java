package com.google.zxing.client.result;

public final class ISBNParsedResult extends ParsedResult {
   private final String isbn;

   ISBNParsedResult(String var1) {
      super(ParsedResultType.ISBN);
      this.isbn = var1;
   }

   public String getDisplayResult() {
      return this.isbn;
   }

   public String getISBN() {
      return this.isbn;
   }
}
