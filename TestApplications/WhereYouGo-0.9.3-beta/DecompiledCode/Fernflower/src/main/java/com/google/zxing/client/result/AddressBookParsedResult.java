package com.google.zxing.client.result;

public final class AddressBookParsedResult extends ParsedResult {
   private final String[] addressTypes;
   private final String[] addresses;
   private final String birthday;
   private final String[] emailTypes;
   private final String[] emails;
   private final String[] geo;
   private final String instantMessenger;
   private final String[] names;
   private final String[] nicknames;
   private final String note;
   private final String org;
   private final String[] phoneNumbers;
   private final String[] phoneTypes;
   private final String pronunciation;
   private final String title;
   private final String[] urls;

   public AddressBookParsedResult(String[] var1, String[] var2, String var3, String[] var4, String[] var5, String[] var6, String[] var7, String var8, String var9, String[] var10, String[] var11, String var12, String var13, String var14, String[] var15, String[] var16) {
      super(ParsedResultType.ADDRESSBOOK);
      this.names = var1;
      this.nicknames = var2;
      this.pronunciation = var3;
      this.phoneNumbers = var4;
      this.phoneTypes = var5;
      this.emails = var6;
      this.emailTypes = var7;
      this.instantMessenger = var8;
      this.note = var9;
      this.addresses = var10;
      this.addressTypes = var11;
      this.org = var12;
      this.birthday = var13;
      this.title = var14;
      this.urls = var15;
      this.geo = var16;
   }

   public AddressBookParsedResult(String[] var1, String[] var2, String[] var3, String[] var4, String[] var5, String[] var6, String[] var7) {
      this(var1, (String[])null, (String)null, var2, var3, var4, var5, (String)null, (String)null, var6, var7, (String)null, (String)null, (String)null, (String[])null, (String[])null);
   }

   public String[] getAddressTypes() {
      return this.addressTypes;
   }

   public String[] getAddresses() {
      return this.addresses;
   }

   public String getBirthday() {
      return this.birthday;
   }

   public String getDisplayResult() {
      StringBuilder var1 = new StringBuilder(100);
      maybeAppend(this.names, var1);
      maybeAppend(this.nicknames, var1);
      maybeAppend(this.pronunciation, var1);
      maybeAppend(this.title, var1);
      maybeAppend(this.org, var1);
      maybeAppend(this.addresses, var1);
      maybeAppend(this.phoneNumbers, var1);
      maybeAppend(this.emails, var1);
      maybeAppend(this.instantMessenger, var1);
      maybeAppend(this.urls, var1);
      maybeAppend(this.birthday, var1);
      maybeAppend(this.geo, var1);
      maybeAppend(this.note, var1);
      return var1.toString();
   }

   public String[] getEmailTypes() {
      return this.emailTypes;
   }

   public String[] getEmails() {
      return this.emails;
   }

   public String[] getGeo() {
      return this.geo;
   }

   public String getInstantMessenger() {
      return this.instantMessenger;
   }

   public String[] getNames() {
      return this.names;
   }

   public String[] getNicknames() {
      return this.nicknames;
   }

   public String getNote() {
      return this.note;
   }

   public String getOrg() {
      return this.org;
   }

   public String[] getPhoneNumbers() {
      return this.phoneNumbers;
   }

   public String[] getPhoneTypes() {
      return this.phoneTypes;
   }

   public String getPronunciation() {
      return this.pronunciation;
   }

   public String getTitle() {
      return this.title;
   }

   public String[] getURLs() {
      return this.urls;
   }
}
