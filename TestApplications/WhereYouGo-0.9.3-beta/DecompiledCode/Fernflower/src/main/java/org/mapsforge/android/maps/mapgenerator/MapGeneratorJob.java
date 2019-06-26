package org.mapsforge.android.maps.mapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.mapsforge.android.maps.DebugSettings;
import org.mapsforge.core.model.Tile;

public class MapGeneratorJob implements Comparable, Serializable {
   private static final long serialVersionUID = 1L;
   public final DebugSettings debugSettings;
   private transient int hashCodeValue;
   public final JobParameters jobParameters;
   private final File mapFile;
   private final MapGenerator mapGenerator;
   private final int mapGeneratorId;
   private transient double priority;
   public final Tile tile;

   public MapGeneratorJob(Tile var1, MapGenerator var2, int var3, File var4, JobParameters var5, DebugSettings var6) {
      this.tile = var1;
      this.mapGeneratorId = var3;
      this.mapGenerator = var2;
      this.mapFile = var4;
      this.jobParameters = var5;
      this.debugSettings = var6;
      this.calculateTransientValues();
   }

   private int calculateHashCode() {
      int var1 = 0;
      int var2;
      if (this.debugSettings == null) {
         var2 = 0;
      } else {
         var2 = this.debugSettings.hashCode();
      }

      int var3;
      if (this.jobParameters == null) {
         var3 = 0;
      } else {
         var3 = this.jobParameters.hashCode();
      }

      int var4;
      if (this.mapFile == null) {
         var4 = 0;
      } else {
         var4 = this.mapFile.hashCode();
      }

      if (this.tile != null) {
         var1 = this.tile.hashCode();
      }

      return (((var2 + 31) * 31 + var3) * 31 + var4) * 31 + var1;
   }

   private void calculateTransientValues() {
      this.hashCodeValue = this.calculateHashCode();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.calculateTransientValues();
   }

   public int compareTo(MapGeneratorJob var1) {
      byte var2;
      if (this.priority < var1.priority) {
         var2 = -1;
      } else if (this.priority > var1.priority) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      return var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof MapGeneratorJob)) {
            var2 = false;
         } else {
            MapGeneratorJob var3 = (MapGeneratorJob)var1;
            if (this.debugSettings == null) {
               if (var3.debugSettings != null) {
                  var2 = false;
                  return var2;
               }
            } else if (!this.debugSettings.equals(var3.debugSettings)) {
               var2 = false;
               return var2;
            }

            if (this.jobParameters == null) {
               if (var3.jobParameters != null) {
                  var2 = false;
                  return var2;
               }
            } else if (!this.jobParameters.equals(var3.jobParameters)) {
               var2 = false;
               return var2;
            }

            if (this.mapGenerator == null) {
               if (var3.mapGenerator != null) {
                  var2 = false;
                  return var2;
               }
            } else {
               if (var3.mapGenerator == null) {
                  var2 = false;
                  return var2;
               }

               if (this.mapGeneratorId != var3.mapGeneratorId) {
                  var2 = false;
                  return var2;
               }

               if (!this.mapGenerator.requiresInternetConnection()) {
                  if (this.mapFile == null) {
                     if (var3.mapFile != null) {
                        var2 = false;
                        return var2;
                     }
                  } else if (!this.mapFile.equals(var3.mapFile)) {
                     var2 = false;
                     return var2;
                  }
               }
            }

            if (this.tile == null) {
               if (var3.tile != null) {
                  var2 = false;
               }
            } else if (!this.tile.equals(var3.tile)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      return this.hashCodeValue;
   }

   void setPriority(double var1) {
      this.priority = var1;
   }
}
