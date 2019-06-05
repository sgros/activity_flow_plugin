package com.google.zxing.client.result;

public final class VINParsedResult extends ParsedResult {
   private final String countryCode;
   private final int modelYear;
   private final char plantCode;
   private final String sequentialNumber;
   private final String vehicleAttributes;
   private final String vehicleDescriptorSection;
   private final String vehicleIdentifierSection;
   private final String vin;
   private final String worldManufacturerID;

   public VINParsedResult(String var1, String var2, String var3, String var4, String var5, String var6, int var7, char var8, String var9) {
      super(ParsedResultType.VIN);
      this.vin = var1;
      this.worldManufacturerID = var2;
      this.vehicleDescriptorSection = var3;
      this.vehicleIdentifierSection = var4;
      this.countryCode = var5;
      this.vehicleAttributes = var6;
      this.modelYear = var7;
      this.plantCode = (char)var8;
      this.sequentialNumber = var9;
   }

   public String getCountryCode() {
      return this.countryCode;
   }

   public String getDisplayResult() {
      StringBuilder var1 = new StringBuilder(50);
      var1.append(this.worldManufacturerID).append(' ');
      var1.append(this.vehicleDescriptorSection).append(' ');
      var1.append(this.vehicleIdentifierSection).append('\n');
      if (this.countryCode != null) {
         var1.append(this.countryCode).append(' ');
      }

      var1.append(this.modelYear).append(' ');
      var1.append(this.plantCode).append(' ');
      var1.append(this.sequentialNumber).append('\n');
      return var1.toString();
   }

   public int getModelYear() {
      return this.modelYear;
   }

   public char getPlantCode() {
      return this.plantCode;
   }

   public String getSequentialNumber() {
      return this.sequentialNumber;
   }

   public String getVIN() {
      return this.vin;
   }

   public String getVehicleAttributes() {
      return this.vehicleAttributes;
   }

   public String getVehicleDescriptorSection() {
      return this.vehicleDescriptorSection;
   }

   public String getVehicleIdentifierSection() {
      return this.vehicleIdentifierSection;
   }

   public String getWorldManufacturerID() {
      return this.worldManufacturerID;
   }
}
