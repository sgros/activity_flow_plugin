package org.telegram.ui;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AlertsCreator;

class PassportActivity$8 implements RequestDelegate {
   // $FF: synthetic field
   final PassportActivity this$0;
   // $FF: synthetic field
   final TLRPC.TL_account_getPasswordSettings val$req;
   // $FF: synthetic field
   final boolean val$saved;
   // $FF: synthetic field
   final String val$textPassword;
   // $FF: synthetic field
   final byte[] val$x_bytes;

   PassportActivity$8(PassportActivity var1, boolean var2, byte[] var3, TLRPC.TL_account_getPasswordSettings var4, String var5) {
      this.this$0 = var1;
      this.val$saved = var2;
      this.val$x_bytes = var3;
      this.val$req = var4;
      this.val$textPassword = var5;
   }

   private void generateNewSecret() {
      Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$8$HOfaQC0wD4xlIJZiYrbf0Jd58uE(this, this.val$x_bytes, this.val$textPassword));
   }

   private void openRequestInterface() {
      if (PassportActivity.access$2500(this.this$0) != null) {
         if (!this.val$saved) {
            UserConfig.getInstance(PassportActivity.access$12400(this.this$0)).savePassword(this.val$x_bytes, PassportActivity.access$7600(this.this$0));
         }

         AndroidUtilities.hideKeyboard(PassportActivity.access$2500(this.this$0)[0]);
         PassportActivity.access$12502(this.this$0, true);
         byte var1;
         if (PassportActivity.access$12600(this.this$0) == 0) {
            var1 = 8;
         } else {
            var1 = 0;
         }

         PassportActivity var2 = new PassportActivity(var1, PassportActivity.access$12600(this.this$0), PassportActivity.access$12700(this.this$0), PassportActivity.access$12800(this.this$0), PassportActivity.access$12900(this.this$0), PassportActivity.access$13000(this.this$0), PassportActivity.access$13100(this.this$0), PassportActivity.access$1300(this.this$0), PassportActivity.access$7300(this.this$0));
         PassportActivity.access$5102(var2, PassportActivity.access$5100(this.this$0));
         PassportActivity.access$13202(var2, PassportActivity.access$13300(this.this$0));
         PassportActivity.access$7602(var2, PassportActivity.access$7600(this.this$0));
         PassportActivity.access$7702(var2, PassportActivity.access$7700(this.this$0));
         PassportActivity.access$7002(var2, PassportActivity.access$7000(this.this$0));
         PassportActivity.access$13402(var2, PassportActivity.access$13400(this.this$0));
         if (PassportActivity.access$13500(this.this$0) != null && PassportActivity.access$13600(this.this$0).checkTransitionAnimation()) {
            PassportActivity.access$13702(this.this$0, var2);
         } else {
            this.this$0.presentFragment(var2, true);
         }

      }
   }

   private void resetSecret() {
      TLRPC.TL_account_updatePasswordSettings var1 = new TLRPC.TL_account_updatePasswordSettings();
      if (PassportActivity.access$7300(this.this$0).current_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var2 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)PassportActivity.access$7300(this.this$0).current_algo;
         var1.password = SRPHelper.startCheck(this.val$x_bytes, PassportActivity.access$7300(this.this$0).srp_id, PassportActivity.access$7300(this.this$0).srp_B, var2);
      }

      var1.new_settings = new TLRPC.TL_account_passwordInputSettings();
      var1.new_settings.new_secure_settings = new TLRPC.TL_secureSecretSettings();
      TLRPC.TL_secureSecretSettings var4 = var1.new_settings.new_secure_settings;
      var4.secure_secret = new byte[0];
      var4.secure_algo = new TLRPC.TL_securePasswordKdfAlgoUnknown();
      TLRPC.TL_account_passwordInputSettings var3 = var1.new_settings;
      var3.new_secure_settings.secure_secret_id = 0L;
      var3.flags |= 4;
      ConnectionsManager.getInstance(PassportActivity.access$13800(this.this$0)).sendRequest(this.val$req, new _$$Lambda$PassportActivity$8$9UB99IqI_ykix5TDiaN6Iu5MpNI(this));
   }

   // $FF: synthetic method
   public void lambda$generateNewSecret$8$PassportActivity$8(byte[] var1, String var2) {
      Utilities.random.setSeed(PassportActivity.access$7300(this.this$0).secure_random);
      TLRPC.TL_account_updatePasswordSettings var3 = new TLRPC.TL_account_updatePasswordSettings();
      if (PassportActivity.access$7300(this.this$0).current_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
         TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow var4 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)PassportActivity.access$7300(this.this$0).current_algo;
         var3.password = SRPHelper.startCheck(var1, PassportActivity.access$7300(this.this$0).srp_id, PassportActivity.access$7300(this.this$0).srp_B, var4);
      }

      var3.new_settings = new TLRPC.TL_account_passwordInputSettings();
      PassportActivity var5 = this.this$0;
      PassportActivity.access$7702(var5, PassportActivity.access$14900(var5));
      var5 = this.this$0;
      PassportActivity.access$7002(var5, Utilities.bytesToLong(Utilities.computeSHA256(PassportActivity.access$7700(var5))));
      if (PassportActivity.access$7300(this.this$0).new_secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
         TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 var6 = (TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)PassportActivity.access$7300(this.this$0).new_secure_algo;
         PassportActivity.access$7602(this.this$0, Utilities.computePBKDF2(AndroidUtilities.getStringBytes(var2), var6.salt));
         byte[] var7 = new byte[32];
         System.arraycopy(PassportActivity.access$7600(this.this$0), 0, var7, 0, 32);
         byte[] var10 = new byte[16];
         System.arraycopy(PassportActivity.access$7600(this.this$0), 32, var10, 0, 16);
         Utilities.aesCbcEncryptionByteArraySafe(PassportActivity.access$7700(this.this$0), var7, var10, 0, PassportActivity.access$7700(this.this$0).length, 0, 1);
         var3.new_settings.new_secure_settings = new TLRPC.TL_secureSecretSettings();
         TLRPC.TL_secureSecretSettings var9 = var3.new_settings.new_secure_settings;
         var9.secure_algo = var6;
         var9.secure_secret = PassportActivity.access$7700(this.this$0);
         var3.new_settings.new_secure_settings.secure_secret_id = PassportActivity.access$7000(this.this$0);
         TLRPC.TL_account_passwordInputSettings var8 = var3.new_settings;
         var8.flags |= 4;
      }

      ConnectionsManager.getInstance(PassportActivity.access$15000(this.this$0)).sendRequest(var3, new _$$Lambda$PassportActivity$8$J6lVpdy_BHy4va_1ekF1Ao5VDsM(this));
   }

   // $FF: synthetic method
   public void lambda$null$0$PassportActivity$8(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         PassportActivity.access$7302(this.this$0, (TLRPC.TL_account_password)var2);
         TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.access$7300(this.this$0));
         this.resetSecret();
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$PassportActivity$8(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$SSg0p24LM5JFEWsSHRGL6ISVQl0(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$11$PassportActivity$8() {
      AlertsCreator.showUpdateAppAlert(this.this$0.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
   }

   // $FF: synthetic method
   public void lambda$null$12$PassportActivity$8(TLObject var1, TLRPC.TL_error var2) {
      int var3 = 0;
      if (var1 != null) {
         PassportActivity.access$1302(this.this$0, new TLRPC.TL_account_authorizationForm());
         TLRPC.Vector var5 = (TLRPC.Vector)var1;

         for(int var4 = var5.objects.size(); var3 < var4; ++var3) {
            PassportActivity.access$1300(this.this$0).values.add((TLRPC.TL_secureValue)var5.objects.get(var3));
         }

         this.openRequestInterface();
      } else {
         if ("APP_VERSION_OUTDATED".equals(var2.text)) {
            AlertsCreator.showUpdateAppAlert(this.this$0.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
         } else {
            PassportActivity.access$7800(this.this$0, LocaleController.getString("AppName", 2131558635), var2.text);
         }

         PassportActivity.access$4900(this.this$0, true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$13$PassportActivity$8(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$e2tFNKG4r_Lr1pJO_sCtfLxqxq0(this, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$null$14$PassportActivity$8(TLRPC.TL_account_passwordSettings var1, boolean var2, byte[] var3) {
      PassportActivity.access$5102(this.this$0, var1.email);
      PassportActivity var4;
      if (var2) {
         var4 = this.this$0;
         PassportActivity.access$7602(var4, PassportActivity.access$14500(var4));
      }

      var4 = this.this$0;
      if (PassportActivity.checkSecret(PassportActivity.access$14600(var4, PassportActivity.access$7700(var4), PassportActivity.access$7600(this.this$0)), PassportActivity.access$7000(this.this$0)) && var3.length != 0 && PassportActivity.access$7000(this.this$0) != 0L) {
         if (PassportActivity.access$12600(this.this$0) == 0) {
            TLRPC.TL_account_getAllSecureValues var5 = new TLRPC.TL_account_getAllSecureValues();
            ConnectionsManager.getInstance(PassportActivity.access$14800(this.this$0)).sendRequest(var5, new _$$Lambda$PassportActivity$8$GEtK3gkRhuC6c7K_GJeUQ2LVqtM(this));
         } else {
            this.openRequestInterface();
         }
      } else if (var2) {
         UserConfig.getInstance(PassportActivity.access$14700(this.this$0)).resetSavedPassword();
         PassportActivity.access$14102(this.this$0, 0);
         PassportActivity.access$14200(this.this$0);
      } else {
         if (PassportActivity.access$1300(this.this$0) != null) {
            PassportActivity.access$1300(this.this$0).values.clear();
            PassportActivity.access$1300(this.this$0).errors.clear();
         }

         if (PassportActivity.access$7700(this.this$0) != null && PassportActivity.access$7700(this.this$0).length != 0) {
            this.resetSecret();
         } else {
            this.generateNewSecret();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$PassportActivity$8(TLRPC.TL_error var1) {
      if (var1 != null && "SRP_ID_INVALID".equals(var1.text)) {
         TLRPC.TL_account_getPassword var2 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(PassportActivity.access$15200(this.this$0)).sendRequest(var2, new _$$Lambda$PassportActivity$8$pFG1BmsHOumiucZunk9h6NfKoS4(this), 8);
      } else {
         this.generateNewSecret();
      }
   }

   // $FF: synthetic method
   public void lambda$null$4$PassportActivity$8(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         PassportActivity.access$7302(this.this$0, (TLRPC.TL_account_password)var2);
         TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.access$7300(this.this$0));
         this.generateNewSecret();
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$PassportActivity$8(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$F930AuW5Rv7yo4GWvOpc42nEhi4(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$6$PassportActivity$8(TLRPC.TL_error var1) {
      if (var1 != null && "SRP_ID_INVALID".equals(var1.text)) {
         TLRPC.TL_account_getPassword var2 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(PassportActivity.access$15100(this.this$0)).sendRequest(var2, new _$$Lambda$PassportActivity$8$P2mDhg3nUkr1ekIIbgmzMqBI8ks(this), 8);
      } else {
         if (PassportActivity.access$1300(this.this$0) == null) {
            PassportActivity.access$1302(this.this$0, new TLRPC.TL_account_authorizationForm());
         }

         this.openRequestInterface();
      }
   }

   // $FF: synthetic method
   public void lambda$null$7$PassportActivity$8(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$gLEjuBEZITILtR2_nh5gzkziIDI(this, var2));
   }

   // $FF: synthetic method
   public void lambda$null$9$PassportActivity$8(TLRPC.TL_error var1, TLObject var2, boolean var3) {
      if (var1 == null) {
         PassportActivity.access$7302(this.this$0, (TLRPC.TL_account_password)var2);
         TwoStepVerificationActivity.initPasswordNewAlgo(PassportActivity.access$7300(this.this$0));
         PassportActivity.access$4500(this.this$0, var3);
      }

   }

   // $FF: synthetic method
   public void lambda$resetSecret$3$PassportActivity$8(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$p3qTfsl6KbDZXI6h8rIQE3R65xM(this, var2));
   }

   // $FF: synthetic method
   public void lambda$run$10$PassportActivity$8(boolean var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$fGSfiU3IUevtwN_S_OLLsXgl_Vw(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$run$15$PassportActivity$8(TLObject var1, String var2, boolean var3) {
      TLRPC.TL_account_passwordSettings var4 = (TLRPC.TL_account_passwordSettings)var1;
      TLRPC.TL_secureSecretSettings var6 = var4.secure_settings;
      TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 var5;
      byte[] var8;
      if (var6 != null) {
         PassportActivity.access$7702(this.this$0, var6.secure_secret);
         PassportActivity.access$7002(this.this$0, var4.secure_settings.secure_secret_id);
         TLRPC.SecurePasswordKdfAlgo var7 = var4.secure_settings.secure_algo;
         if (var7 instanceof TLRPC.TL_securePasswordKdfAlgoSHA512) {
            var8 = ((TLRPC.TL_securePasswordKdfAlgoSHA512)var7).salt;
            PassportActivity.access$7602(this.this$0, Utilities.computeSHA512(var8, AndroidUtilities.getStringBytes(var2), var8));
         } else if (var7 instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
            var5 = (TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)var7;
            var8 = var5.salt;
            PassportActivity.access$7602(this.this$0, Utilities.computePBKDF2(AndroidUtilities.getStringBytes(var2), var5.salt));
         } else {
            if (var7 instanceof TLRPC.TL_securePasswordKdfAlgoUnknown) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$_akr0Pvo7nj0l1SgNy6zVBfPjAI(this));
               return;
            }

            var8 = new byte[0];
         }
      } else {
         if (PassportActivity.access$7300(this.this$0).new_secure_algo instanceof TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
            var5 = (TLRPC.TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000)PassportActivity.access$7300(this.this$0).new_secure_algo;
            var8 = var5.salt;
            PassportActivity.access$7602(this.this$0, Utilities.computePBKDF2(AndroidUtilities.getStringBytes(var2), var5.salt));
         } else {
            var8 = new byte[0];
         }

         PassportActivity.access$7702(this.this$0, (byte[])null);
         PassportActivity.access$7002(this.this$0, 0L);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$kFdsOPJ6Cg4p6X_mTpt766bfF7o(this, var4, var3, var8));
   }

   // $FF: synthetic method
   public void lambda$run$16$PassportActivity$8(boolean var1, TLRPC.TL_error var2) {
      if (var1) {
         UserConfig.getInstance(PassportActivity.access$14000(this.this$0)).resetSavedPassword();
         PassportActivity.access$14102(this.this$0, 0);
         PassportActivity.access$14200(this.this$0);
         if (PassportActivity.access$14300(this.this$0) != null && PassportActivity.access$14300(this.this$0)[0].getVisibility() == 0) {
            PassportActivity.access$2500(this.this$0)[0].requestFocus();
            AndroidUtilities.showKeyboard(PassportActivity.access$2500(this.this$0)[0]);
         }
      } else {
         PassportActivity.access$4900(this.this$0, true, false);
         if (var2.text.equals("PASSWORD_HASH_INVALID")) {
            PassportActivity.access$14400(this.this$0, true);
         } else if (var2.text.startsWith("FLOOD_WAIT")) {
            int var3 = Utilities.parseInt(var2.text);
            String var4;
            if (var3 < 60) {
               var4 = LocaleController.formatPluralString("Seconds", var3);
            } else {
               var4 = LocaleController.formatPluralString("Minutes", var3 / 60);
            }

            PassportActivity.access$7800(this.this$0, LocaleController.getString("AppName", 2131558635), LocaleController.formatString("FloodWaitTime", 2131559496, var4));
         } else {
            PassportActivity.access$7800(this.this$0, LocaleController.getString("AppName", 2131558635), var2.text);
         }
      }

   }

   public void run(TLObject var1, TLRPC.TL_error var2) {
      if (var2 != null && "SRP_ID_INVALID".equals(var2.text)) {
         TLRPC.TL_account_getPassword var3 = new TLRPC.TL_account_getPassword();
         ConnectionsManager.getInstance(PassportActivity.access$13900(this.this$0)).sendRequest(var3, new _$$Lambda$PassportActivity$8$mGdIMbtzY2Oz5TSSjAmA4uEPy4U(this, this.val$saved), 8);
      } else {
         if (var2 == null) {
            Utilities.globalQueue.postRunnable(new _$$Lambda$PassportActivity$8$uJcZ25nIuoS6ZH6mTt6S6kuhaAw(this, var1, this.val$textPassword, this.val$saved));
         } else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$PassportActivity$8$f57rMytRL_jx0gvnoAw93D9zzYQ(this, this.val$saved, var2));
         }

      }
   }
}
