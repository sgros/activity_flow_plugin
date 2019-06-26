package com.google.zxing.oned;

import java.util.ArrayList;
import java.util.List;

final class EANManufacturerOrgSupport {
   private final List countryIdentifiers = new ArrayList();
   private final List ranges = new ArrayList();

   private void add(int[] var1, String var2) {
      this.ranges.add(var1);
      this.countryIdentifiers.add(var2);
   }

   private void initIfNeeded() {
      synchronized(this){}

      Throwable var10000;
      label76: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.ranges.isEmpty();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label76;
         }

         if (!var1) {
            return;
         }

         label67:
         try {
            this.add(new int[]{0, 19}, "US/CA");
            this.add(new int[]{30, 39}, "US");
            this.add(new int[]{60, 139}, "US/CA");
            this.add(new int[]{300, 379}, "FR");
            this.add(new int[]{380}, "BG");
            this.add(new int[]{383}, "SI");
            this.add(new int[]{385}, "HR");
            this.add(new int[]{387}, "BA");
            this.add(new int[]{400, 440}, "DE");
            this.add(new int[]{450, 459}, "JP");
            this.add(new int[]{460, 469}, "RU");
            this.add(new int[]{471}, "TW");
            this.add(new int[]{474}, "EE");
            this.add(new int[]{475}, "LV");
            this.add(new int[]{476}, "AZ");
            this.add(new int[]{477}, "LT");
            this.add(new int[]{478}, "UZ");
            this.add(new int[]{479}, "LK");
            this.add(new int[]{480}, "PH");
            this.add(new int[]{481}, "BY");
            this.add(new int[]{482}, "UA");
            this.add(new int[]{484}, "MD");
            this.add(new int[]{485}, "AM");
            this.add(new int[]{486}, "GE");
            this.add(new int[]{487}, "KZ");
            this.add(new int[]{489}, "HK");
            this.add(new int[]{490, 499}, "JP");
            this.add(new int[]{500, 509}, "GB");
            this.add(new int[]{520}, "GR");
            this.add(new int[]{528}, "LB");
            this.add(new int[]{529}, "CY");
            this.add(new int[]{531}, "MK");
            this.add(new int[]{535}, "MT");
            this.add(new int[]{539}, "IE");
            this.add(new int[]{540, 549}, "BE/LU");
            this.add(new int[]{560}, "PT");
            this.add(new int[]{569}, "IS");
            this.add(new int[]{570, 579}, "DK");
            this.add(new int[]{590}, "PL");
            this.add(new int[]{594}, "RO");
            this.add(new int[]{599}, "HU");
            this.add(new int[]{600, 601}, "ZA");
            this.add(new int[]{603}, "GH");
            this.add(new int[]{608}, "BH");
            this.add(new int[]{609}, "MU");
            this.add(new int[]{611}, "MA");
            this.add(new int[]{613}, "DZ");
            this.add(new int[]{616}, "KE");
            this.add(new int[]{618}, "CI");
            this.add(new int[]{619}, "TN");
            this.add(new int[]{621}, "SY");
            this.add(new int[]{622}, "EG");
            this.add(new int[]{624}, "LY");
            this.add(new int[]{625}, "JO");
            this.add(new int[]{626}, "IR");
            this.add(new int[]{627}, "KW");
            this.add(new int[]{628}, "SA");
            this.add(new int[]{629}, "AE");
            this.add(new int[]{640, 649}, "FI");
            this.add(new int[]{690, 695}, "CN");
            this.add(new int[]{700, 709}, "NO");
            this.add(new int[]{729}, "IL");
            this.add(new int[]{730, 739}, "SE");
            this.add(new int[]{740}, "GT");
            this.add(new int[]{741}, "SV");
            this.add(new int[]{742}, "HN");
            this.add(new int[]{743}, "NI");
            this.add(new int[]{744}, "CR");
            this.add(new int[]{745}, "PA");
            this.add(new int[]{746}, "DO");
            this.add(new int[]{750}, "MX");
            this.add(new int[]{754, 755}, "CA");
            this.add(new int[]{759}, "VE");
            this.add(new int[]{760, 769}, "CH");
            this.add(new int[]{770}, "CO");
            this.add(new int[]{773}, "UY");
            this.add(new int[]{775}, "PE");
            this.add(new int[]{777}, "BO");
            this.add(new int[]{779}, "AR");
            this.add(new int[]{780}, "CL");
            this.add(new int[]{784}, "PY");
            this.add(new int[]{785}, "PE");
            this.add(new int[]{786}, "EC");
            this.add(new int[]{789, 790}, "BR");
            this.add(new int[]{800, 839}, "IT");
            this.add(new int[]{840, 849}, "ES");
            this.add(new int[]{850}, "CU");
            this.add(new int[]{858}, "SK");
            this.add(new int[]{859}, "CZ");
            this.add(new int[]{860}, "YU");
            this.add(new int[]{865}, "MN");
            this.add(new int[]{867}, "KP");
            this.add(new int[]{868, 869}, "TR");
            this.add(new int[]{870, 879}, "NL");
            this.add(new int[]{880}, "KR");
            this.add(new int[]{885}, "TH");
            this.add(new int[]{888}, "SG");
            this.add(new int[]{890}, "IN");
            this.add(new int[]{893}, "VN");
            this.add(new int[]{896}, "PK");
            this.add(new int[]{899}, "ID");
            this.add(new int[]{900, 919}, "AT");
            this.add(new int[]{930, 939}, "AU");
            this.add(new int[]{940, 949}, "AZ");
            this.add(new int[]{955}, "MY");
            this.add(new int[]{958}, "MO");
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label67;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   String lookupCountryIdentifier(String var1) {
      Object var2 = null;
      this.initIfNeeded();
      int var3 = Integer.parseInt(var1.substring(0, 3));
      int var4 = this.ranges.size();
      int var5 = 0;

      while(true) {
         var1 = (String)var2;
         if (var5 >= var4) {
            break;
         }

         int[] var7 = (int[])this.ranges.get(var5);
         int var6 = var7[0];
         if (var3 < var6) {
            var1 = (String)var2;
            break;
         }

         if (var7.length != 1) {
            var6 = var7[1];
         }

         if (var3 <= var6) {
            var1 = (String)this.countryIdentifiers.get(var5);
            break;
         }

         ++var5;
      }

      return var1;
   }
}
