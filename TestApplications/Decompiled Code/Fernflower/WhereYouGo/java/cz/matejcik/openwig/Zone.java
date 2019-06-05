package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import se.krka.kahlua.stdlib.TableLib;
import se.krka.kahlua.vm.LuaState;
import se.krka.kahlua.vm.LuaTable;

public class Zone extends Thing {
   private static final double DEFAULT_PROXIMITY = 1500.0D;
   public static final int DISTANT = 0;
   public static final int INSIDE = 2;
   public static final int NOWHERE = -1;
   public static final int PROXIMITY = 1;
   public static final int S_ALWAYS = 0;
   public static final int S_NEVER = 3;
   public static final int S_ONENTER = 1;
   public static final int S_ONPROXIMITY = 2;
   private boolean active = false;
   public double bbBottom;
   public ZonePoint bbCenter = new ZonePoint(0.0D, 0.0D, 0.0D);
   public double bbLeft;
   public double bbRight;
   public double bbTop;
   public int contain = -1;
   private double diameter;
   public double distance = Double.MAX_VALUE;
   private double distanceRange = -1.0D;
   private double distantTolerance = 20.0D;
   private double insideTolerance = 5.0D;
   private int ncontain = -1;
   public ZonePoint nearestPoint = new ZonePoint(0.0D, 0.0D, 0.0D);
   public double pbbBottom;
   public double pbbLeft;
   public double pbbRight;
   public double pbbTop;
   public ZonePoint[] points;
   private double proximityRange = -1.0D;
   private double proximityTolerance = 10.0D;
   private int showObjects = 1;

   private void preprocess() {
      if (this.points != null && this.points.length != 0) {
         this.bbTop = Double.NEGATIVE_INFINITY;
         this.bbBottom = Double.POSITIVE_INFINITY;
         this.bbLeft = Double.POSITIVE_INFINITY;
         this.bbRight = Double.NEGATIVE_INFINITY;

         int var1;
         for(var1 = 0; var1 < this.points.length; ++var1) {
            this.bbTop = Math.max(this.bbTop, this.points[var1].latitude);
            this.bbBottom = Math.min(this.bbBottom, this.points[var1].latitude);
            this.bbLeft = Math.min(this.bbLeft, this.points[var1].longitude);
            this.bbRight = Math.max(this.bbRight, this.points[var1].longitude);
         }

         this.bbCenter.latitude = this.bbBottom + (this.bbTop - this.bbBottom) / 2.0D;
         this.bbCenter.longitude = this.bbLeft + (this.bbRight - this.bbLeft) / 2.0D;
         double var2;
         if (this.proximityRange < 1500.0D) {
            var2 = 1500.0D;
         } else {
            var2 = this.proximityRange;
         }

         double var4 = ZonePoint.m2lat(var2);
         double var6 = this.bbCenter.latitude;
         if (this.proximityRange < 1500.0D) {
            var2 = 1500.0D;
         } else {
            var2 = this.proximityRange;
         }

         var2 = ZonePoint.m2lon(var6, var2);
         this.pbbTop = this.bbTop + var4;
         this.pbbBottom = this.bbBottom - var4;
         this.pbbLeft = this.bbLeft - var2;
         this.pbbRight = this.bbRight + var2;
         var6 = 0.0D;
         var4 = 0.0D;
         var2 = 0.0D;

         double var10;
         for(var1 = 0; var1 < this.points.length; var6 = var10) {
            double var8 = this.points[var1].latitude - this.bbCenter.latitude;
            var10 = this.points[var1].longitude - this.bbCenter.longitude;
            var8 = var8 * var8 + var10 * var10;
            var10 = var6;
            if (var8 > var6) {
               var4 = this.points[var1].latitude;
               var2 = this.points[var1].longitude;
               var10 = var8;
            }

            ++var1;
         }

         this.diameter = this.bbCenter.distance(var4, var2);
      }

   }

   private void setcontain() {
      if (this.contain != this.ncontain) {
         if (this.contain == 2) {
            Engine.instance.player.leaveZone(this);
            Engine.callEvent(this, "OnExit", (Object)null);
         }

         this.contain = this.ncontain;
         if (this.contain == 2) {
            Engine.instance.player.enterZone(this);
         }

         switch(this.contain) {
         case -1:
            Engine.log("ZONE: out-of-range " + this.name, 0);
            Engine.callEvent(this, "OnNotInRange", (Object)null);
            break;
         case 0:
            Engine.log("ZONE: distant " + this.name, 0);
            Engine.callEvent(this, "OnDistant", (Object)null);
            break;
         case 1:
            Engine.log("ZONE: proximity " + this.name, 0);
            Engine.callEvent(this, "OnProximity", (Object)null);
            break;
         case 2:
            Engine.log("ZONE: inside " + this.name, 0);
            Engine.callEvent(this, "OnEnter", (Object)null);
            break;
         default:
            return;
         }

         Engine.refreshUI();
      }

   }

   public void collectThings(LuaTable var1) {
      if (this.showThings()) {
         Object var2 = null;

         while(true) {
            Object var3 = this.inventory.next(var2);
            if (var3 == null) {
               break;
            }

            Object var4 = this.inventory.rawget(var3);
            var2 = var3;
            if (var4 instanceof Thing) {
               var2 = var3;
               if (((Thing)var4).isVisible()) {
                  TableLib.rawappend(var1, var4);
                  var2 = var3;
               }
            }
         }
      }

   }

   public boolean contains(Thing var1) {
      boolean var2;
      if (var1 == Engine.instance.player) {
         if (this.contain == 2) {
            var2 = true;
         } else {
            var2 = false;
         }
      } else {
         var2 = super.contains(var1);
      }

      return var2;
   }

   public void deserialize(DataInputStream var1) throws IOException {
      this.contain = var1.readInt();
      this.ncontain = var1.readInt();
      super.deserialize(var1);
   }

   public boolean isLocated() {
      return true;
   }

   public boolean isVisible() {
      boolean var1;
      if (this.active && this.visible && this.contain > -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected String luaTostring() {
      return "a Zone instance";
   }

   public void serialize(DataOutputStream var1) throws IOException {
      var1.writeInt(this.contain);
      var1.writeInt(this.ncontain);
      super.serialize(var1);
   }

   protected void setItem(String var1, Object var2) {
      byte var3 = 0;
      if ("Points".equals(var1) && var2 != null) {
         LuaTable var7 = (LuaTable)var2;
         int var4 = var7.len();
         this.points = new ZonePoint[var4];

         for(int var8 = 1; var8 <= var4; ++var8) {
            ZonePoint var6 = (ZonePoint)var7.rawget(new Double((double)var8));
            this.points[var8 - 1] = var6;
         }

         if (this.active) {
            this.preprocess();
            this.walk(Engine.instance.player.position);
         }
      } else {
         boolean var5;
         if ("Active".equals(var1)) {
            var5 = LuaState.boolEval(var2);
            if (var5 != this.active) {
               this.callEvent("OnZoneState", (Object)null);
            }

            this.active = var5;
            if (var5) {
               this.preprocess();
            }

            if (this.active) {
               this.walk(Engine.instance.player.position);
            } else {
               if (this.distanceRange >= 0.0D) {
                  var3 = -1;
               }

               this.ncontain = var3;
               this.contain = var3;
               Engine.instance.player.leaveZone(this);
            }
         } else if ("Visible".equals(var1)) {
            var5 = LuaState.boolEval(var2);
            if (var5 != this.visible) {
               this.callEvent("OnZoneState", (Object)null);
            }

            this.visible = var5;
         } else if ("DistanceRange".equals(var1) && var2 instanceof Double) {
            this.distanceRange = LuaState.fromDouble(var2);
            this.preprocess();
            if (this.distanceRange < 0.0D && this.contain == -1) {
               this.ncontain = 0;
               this.contain = 0;
            }
         } else if ("ProximityRange".equals(var1) && var2 instanceof Double) {
            this.preprocess();
            this.proximityRange = LuaState.fromDouble(var2);
         } else if ("ShowObjects".equals(var1)) {
            var1 = (String)var2;
            if ("Always".equals(var1)) {
               this.showObjects = 0;
            } else if ("OnProximity".equals(var1)) {
               this.showObjects = 2;
            } else if ("OnEnter".equals(var1)) {
               this.showObjects = 1;
            } else if ("Never".equals(var1)) {
               this.showObjects = 3;
            }
         } else if ("OriginalPoint".equals(var1)) {
            this.position = (ZonePoint)var2;
         } else {
            super.setItem(var1, var2);
         }
      }

   }

   public boolean showThings() {
      boolean var1 = true;
      boolean var2 = true;
      boolean var3 = false;
      boolean var4;
      if (!this.active) {
         var4 = var3;
      } else {
         var4 = var3;
         switch(this.showObjects) {
         case 0:
            var4 = true;
            break;
         case 1:
            if (this.contain == 2) {
               var4 = var1;
            } else {
               var4 = false;
            }
            break;
         case 2:
            if (this.contain >= 1) {
               var4 = var2;
            } else {
               var4 = false;
            }
         case 3:
            break;
         default:
            var4 = var3;
         }
      }

      return var4;
   }

   public void tick() {
      if (this.active && this.contain != this.ncontain) {
         this.setcontain();
      }

   }

   public int visibleThings() {
      int var1;
      if (!this.showThings()) {
         var1 = 0;
      } else {
         int var2 = 0;
         Object var3 = null;

         while(true) {
            Object var4 = this.inventory.next(var3);
            var1 = var2;
            if (var4 == null) {
               break;
            }

            Object var5 = this.inventory.rawget(var4);
            var3 = var4;
            if (!(var5 instanceof Player)) {
               var3 = var4;
               if (var5 instanceof Thing) {
                  var3 = var4;
                  if (((Thing)var5).isVisible()) {
                     ++var2;
                     var3 = var4;
                  }
               }
            }
         }
      }

      return var1;
   }

   public boolean visibleToPlayer() {
      return this.isVisible();
   }

   public void walk(ZonePoint var1) {
      if (this.active && this.points != null && this.points.length != 0 && var1 != null) {
         double var8;
         if (var1.latitude > this.pbbBottom && var1.latitude < this.pbbTop && var1.longitude > this.pbbLeft && var1.longitude < this.pbbRight) {
            this.ncontain = 1;
            double var2;
            double var4;
            double var6;
            double var12;
            double var14;
            double var16;
            double var18;
            double var20;
            if (var1.latitude > this.bbBottom && var1.latitude < this.bbTop && var1.longitude > this.bbLeft && var1.longitude < this.bbRight && this.points.length > 2) {
               var2 = var1.latitude;
               var4 = var1.longitude;
               var6 = this.points[this.points.length - 1].latitude;
               var8 = this.points[this.points.length - 1].longitude;
               boolean var10 = false;

               boolean var22;
               for(int var11 = 0; var11 < this.points.length; var10 = var22) {
                  var12 = this.points[var11].latitude;
                  var14 = this.points[var11].longitude;
                  if (var12 > var6) {
                     var16 = var6;
                     var18 = var8;
                     var20 = var12;
                     var6 = var14;
                  } else {
                     var16 = var12;
                     var18 = var14;
                     var20 = var6;
                     var6 = var8;
                  }

                  var22 = var10;
                  if (var16 < var2) {
                     var22 = var10;
                     if (var2 <= var20) {
                        if (var8 > var4 && var14 > var4) {
                           if (!var10) {
                              var22 = true;
                           } else {
                              var22 = false;
                           }
                        } else {
                           label150: {
                              if (var8 < var4) {
                                 var22 = var10;
                                 if (var14 < var4) {
                                    break label150;
                                 }
                              }

                              var22 = var10;
                              if ((var4 - var18) * (var20 - var16) < (var6 - var18) * (var2 - var16)) {
                                 if (!var10) {
                                    var22 = true;
                                 } else {
                                    var22 = false;
                                 }
                              }
                           }
                        }
                     }
                  }

                  var6 = var12;
                  var8 = var14;
                  ++var11;
               }

               if (var10) {
                  this.ncontain = 2;
                  this.distance = 0.0D;
                  this.nearestPoint.sync(var1);
               }
            }

            var8 = 0.0D;
            if (this.ncontain != 2) {
               var6 = this.points[this.points.length - 1].latitude;
               var8 = this.points[this.points.length - 1].longitude;
               var18 = var6;
               var12 = var8;
               var20 = Double.POSITIVE_INFINITY;

               for(int var23 = 0; var23 < this.points.length; var20 = var4) {
                  var16 = this.points[var23].latitude;
                  var14 = this.points[var23].longitude;
                  var2 = (var1.latitude - var6) * (var16 - var6) + (var1.longitude - var8) * (var14 - var8);
                  if (var2 > 0.0D) {
                     var4 = (var1.latitude - var16) * (var6 - var16) + (var1.longitude - var14) * (var8 - var14);
                     if (var4 <= 0.0D) {
                        var6 = var16;
                        var8 = var14;
                     } else {
                        var6 += (var16 - var6) * var2 / (var2 + var4);
                        var8 += (var14 - var8) * var2 / (var2 + var4);
                     }
                  }

                  var2 = (var6 - var1.latitude) * (var6 - var1.latitude) + (var8 - var1.longitude) * (var8 - var1.longitude);
                  var4 = var20;
                  if (var2 < var20) {
                     var4 = var2;
                     var12 = var8;
                     var18 = var6;
                  }

                  var6 = var16;
                  var8 = var14;
                  ++var23;
               }

               this.nearestPoint.latitude = var18;
               this.nearestPoint.longitude = var12;
               var8 = var1.distance(var18, var12);
               this.distance = var8;
               if (this.distance >= this.proximityRange && this.proximityRange >= 0.0D) {
                  if (this.distance >= this.distanceRange && this.distanceRange >= 0.0D) {
                     this.ncontain = -1;
                  } else {
                     this.ncontain = 0;
                  }
               } else {
                  this.ncontain = 1;
               }
            }
         } else {
            this.distance = this.bbCenter.distance(var1);
            var8 = this.distance - this.diameter;
            this.nearestPoint.sync(this.bbCenter);
            if (var8 >= this.distanceRange && this.distanceRange >= 0.0D) {
               this.ncontain = -1;
            } else {
               this.ncontain = 0;
            }
         }

         if (this.ncontain < this.contain) {
            switch(this.contain) {
            case 0:
               if (var8 - this.distantTolerance < this.distanceRange) {
                  this.ncontain = 0;
               }
            case 1:
               if (var8 - this.proximityTolerance < this.proximityRange) {
                  this.ncontain = 1;
               }
            case 2:
               if (var8 - this.insideTolerance < 0.0D) {
                  this.ncontain = 2;
               }
            }
         }
      }

   }
}
