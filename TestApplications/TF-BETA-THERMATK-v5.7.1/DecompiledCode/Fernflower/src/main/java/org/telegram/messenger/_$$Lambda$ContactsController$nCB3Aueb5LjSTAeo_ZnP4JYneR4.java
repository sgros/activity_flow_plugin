package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$nCB3Aueb5LjSTAeo_ZnP4JYneR4 implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final HashMap f$1;
   // $FF: synthetic field
   private final ArrayList f$10;
   // $FF: synthetic field
   private final HashMap f$11;
   // $FF: synthetic field
   private final SparseArray f$2;
   // $FF: synthetic field
   private final boolean[] f$3;
   // $FF: synthetic field
   private final HashMap f$4;
   // $FF: synthetic field
   private final TLRPC.TL_contacts_importContacts f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final HashMap f$7;
   // $FF: synthetic field
   private final boolean f$8;
   // $FF: synthetic field
   private final HashMap f$9;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$nCB3Aueb5LjSTAeo_ZnP4JYneR4(ContactsController var1, HashMap var2, SparseArray var3, boolean[] var4, HashMap var5, TLRPC.TL_contacts_importContacts var6, int var7, HashMap var8, boolean var9, HashMap var10, ArrayList var11, HashMap var12) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
      this.f$10 = var11;
      this.f$11 = var12;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$19$ContactsController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, var1, var2);
   }
}
