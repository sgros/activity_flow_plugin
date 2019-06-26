package locus.api.android.features.geocaching.fieldNotes;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import locus.api.android.ActionTools;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.Utils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;

public class FieldNotesHelper {
   public static final String PATH_FIELD_NOTES = "fieldNotes";
   public static final String PATH_FIELD_NOTE_IMAGES = "fieldNoteImages";

   private static List create(Cursor var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         throw new IllegalArgumentException("Cursor cannot be 'null'");
      } else {
         int var2 = 0;

         for(int var3 = var0.getCount(); var2 < var3; ++var2) {
            var0.moveToPosition(var2);
            FieldNote var4 = new FieldNote();
            var4.setId(var0.getLong(var0.getColumnIndexOrThrow("_id")));
            var4.setCacheCode(var0.getString(var0.getColumnIndexOrThrow("cache_code")));
            var4.setCacheName(var0.getString(var0.getColumnIndexOrThrow("cache_name")));
            var4.setType(var0.getInt(var0.getColumnIndexOrThrow("type")));
            var4.setTime(var0.getLong(var0.getColumnIndex("time")));
            int var5 = var0.getColumnIndex("note");
            if (var5 >= 0) {
               var4.setNote(var0.getString(var5));
            }

            var5 = var0.getColumnIndex("favorite");
            boolean var6;
            if (var5 >= 0) {
               if (var0.getInt(var5) == 1) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var4.setFavorite(var6);
            }

            var5 = var0.getColumnIndex("logged");
            if (var5 >= 0) {
               if (var0.getInt(var5) == 1) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var4.setLogged(var6);
            }

            var1.add(var4);
         }

         return var1;
      }
   }

   private static ContentValues createContentValues(FieldNote var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Field note cannot be 'null'");
      } else {
         ContentValues var1 = new ContentValues();
         var1.put("cache_code", var0.getCacheCode());
         var1.put("cache_name", var0.getCacheName());
         var1.put("type", var0.getType());
         var1.put("time", var0.getTime());
         var1.put("note", var0.getNote());
         var1.put("favorite", var0.isFavorite());
         var1.put("logged", var0.isLogged());
         return var1;
      }
   }

   private static ContentValues createContentValues(FieldNoteImage var0, boolean var1) {
      if (var0 != null && var0.getImage() != null) {
         ContentValues var2 = new ContentValues();
         var2.put("field_note_id", var0.getFieldNoteId());
         var2.put("caption", var0.getCaption());
         var2.put("description", var0.getDescription());
         if (var1) {
            var2.put("data", var0.getImage());
         }

         return var2;
      } else {
         throw new IllegalArgumentException("Field note image cannot be 'null'");
      }
   }

   private static List createFieldNoteImages(Cursor var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         throw new IllegalArgumentException("Cursor cannot be 'null'");
      } else {
         int var2 = 0;

         for(int var3 = var0.getCount(); var2 < var3; ++var2) {
            var0.moveToPosition(var2);
            FieldNoteImage var4 = new FieldNoteImage();
            var4.setId(var0.getLong(var0.getColumnIndexOrThrow("_id")));
            int var5 = var0.getColumnIndex("field_note_id");
            if (var5 >= 0) {
               var4.setFieldNoteId(var0.getLong(var5));
            }

            var5 = var0.getColumnIndex("caption");
            if (var5 >= 0) {
               var4.setCaption(var0.getString(var5));
            }

            var5 = var0.getColumnIndex("description");
            if (var5 >= 0) {
               var4.setDescription(var0.getString(var5));
            }

            var5 = var0.getColumnIndex("data");
            if (var5 >= 0) {
               var4.setImage(var0.getBlob(var5));
            }

            var1.add(var4);
         }

         return var1;
      }
   }

   public static boolean delete(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      boolean var4 = true;
      Uri var5 = getUriFieldNoteTable(var1);
      int var6 = var0.getContentResolver().delete(var5, "_id=?", new String[]{Long.toString(var2)});
      deleteImages(var0, var1, var2);
      if (var6 != 1) {
         var4 = false;
      }

      return var4;
   }

   public static int deleteAll(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      Uri var2 = getUriFieldNoteTable(var1);
      int var3 = var0.getContentResolver().delete(var2, (String)null, (String[])null);
      deleteImagesAll(var0, var1);
      return var3;
   }

   private static void deleteImages(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      Uri var4 = getUriFieldNoteImagesTable(var1);
      var0.getContentResolver().delete(var4, "field_note_id=?", new String[]{Long.toString(var2)});
   }

   private static void deleteImagesAll(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      Uri var2 = getUriFieldNoteImagesTable(var1);
      var0.getContentResolver().delete(var2, (String)null, (String[])null);
   }

   public static List get(Context var0, LocusUtils.LocusVersion var1, String var2) throws RequiredVersionMissingException {
      Cursor var36;
      Throwable var10000;
      label336: {
         Cursor var35;
         boolean var10001;
         label329: {
            Uri var3;
            Object var4;
            label330: {
               var3 = getUriFieldNoteTable(var1);
               var4 = null;
               if (var2 != null) {
                  var36 = (Cursor)var4;

                  try {
                     if (var2.length() != 0) {
                        break label330;
                     }
                  } catch (Throwable var34) {
                     var10000 = var34;
                     var10001 = false;
                     break label336;
                  }
               }

               var36 = (Cursor)var4;

               try {
                  var35 = var0.getContentResolver().query(var3, (String[])null, (String)null, (String[])null, (String)null);
                  break label329;
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label336;
               }
            }

            var36 = (Cursor)var4;

            try {
               var35 = var0.getContentResolver().query(var3, (String[])null, "cache_code=?", new String[]{var2}, (String)null);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label336;
            }
         }

         Object var40;
         if (var35 == null) {
            var36 = var35;

            ArrayList var38;
            try {
               var38 = new ArrayList();
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label336;
            }

            Utils.closeQuietly(var35);
            var40 = var38;
         } else {
            var36 = var35;

            List var39;
            try {
               var39 = create(var35);
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label336;
            }

            var40 = var39;
            Utils.closeQuietly(var35);
         }

         return (List)var40;
      }

      Throwable var37 = var10000;
      Utils.closeQuietly(var36);
      throw var37;
   }

   public static FieldNote get(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      FieldNote var4 = null;
      Uri var5 = ContentUris.withAppendedId(getUriFieldNoteTable(var1), var2);
      Cursor var6 = null;

      Throwable var10000;
      label242: {
         boolean var10001;
         Cursor var30;
         try {
            var30 = var0.getContentResolver().query(var5, (String[])null, (String)null, (String[])null, (String)null);
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            break label242;
         }

         FieldNote var28;
         if (var30 != null) {
            var6 = var30;

            int var7;
            try {
               var7 = var30.getCount();
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label242;
            }

            if (var7 == 1) {
               var6 = var30;

               try {
                  var4 = (FieldNote)create(var30).get(0);
               } catch (Throwable var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label242;
               }

               var6 = var30;

               try {
                  getImages(var0, var1, var4);
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label242;
               }

               Utils.closeQuietly(var30);
               var28 = var4;
               return var28;
            }
         }

         Utils.closeQuietly(var30);
         var28 = var4;
         return var28;
      }

      Throwable var29 = var10000;
      Utils.closeQuietly(var6);
      throw var29;
   }

   public static List getAll(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      return get(var0, var1, "");
   }

   public static int getCount(Context var0, LocusUtils.LocusVersion var1) throws RequiredVersionMissingException {
      Uri var2 = getUriFieldNoteTable(var1);
      Cursor var12 = null;

      Throwable var10000;
      label97: {
         boolean var10001;
         Cursor var10;
         try {
            var10 = var0.getContentResolver().query(var2, new String[]{"_id"}, (String)null, (String[])null, (String)null);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label97;
         }

         int var3;
         if (var10 == null) {
            Utils.closeQuietly(var10);
            var3 = 0;
            return var3;
         }

         var12 = var10;

         try {
            var3 = var10.getCount();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label97;
         }

         Utils.closeQuietly(var10);
         return var3;
      }

      Throwable var11 = var10000;
      Utils.closeQuietly(var12);
      throw var11;
   }

   public static FieldNoteImage getImage(Context var0, LocusUtils.LocusVersion var1, long var2) throws RequiredVersionMissingException {
      Uri var4 = ContentUris.withAppendedId(getUriFieldNoteImagesTable(var1), var2);
      Cursor var21 = null;

      Throwable var10000;
      label167: {
         boolean var10001;
         Cursor var18;
         try {
            var18 = var0.getContentResolver().query(var4, (String[])null, (String)null, (String[])null, (String)null);
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label167;
         }

         FieldNoteImage var19;
         if (var18 != null) {
            var21 = var18;

            int var5;
            try {
               var5 = var18.getCount();
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label167;
            }

            if (var5 == 1) {
               var21 = var18;

               FieldNoteImage var22;
               try {
                  var22 = (FieldNoteImage)createFieldNoteImages(var18).get(0);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label167;
               }

               Utils.closeQuietly(var18);
               var19 = var22;
               return var19;
            }
         }

         Utils.closeQuietly(var18);
         var19 = null;
         return var19;
      }

      Throwable var20 = var10000;
      Utils.closeQuietly(var21);
      throw var20;
   }

   private static void getImages(Context var0, LocusUtils.LocusVersion var1, FieldNote var2) throws RequiredVersionMissingException {
      Uri var3 = getUriFieldNoteImagesTable(var1);
      List var4 = null;
      Cursor var53 = var4;

      Cursor var51;
      label429: {
         Throwable var10000;
         label433: {
            boolean var10001;
            ContentResolver var50;
            try {
               var50 = var0.getContentResolver();
            } catch (Throwable var49) {
               var10000 = var49;
               var10001 = false;
               break label433;
            }

            var53 = var4;

            String var5;
            try {
               var5 = Long.toString(var2.getId());
            } catch (Throwable var48) {
               var10000 = var48;
               var10001 = false;
               break label433;
            }

            var53 = var4;

            try {
               var51 = var50.query(var3, new String[]{"_id"}, "field_note_id=?", new String[]{var5}, (String)null);
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label433;
            }

            if (var51 == null) {
               break label429;
            }

            var53 = var51;

            try {
               var4 = createFieldNoteImages(var51);
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label433;
            }

            int var6 = 0;
            var53 = var51;

            int var7;
            try {
               var7 = var4.size();
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label433;
            }

            while(true) {
               if (var6 >= var7) {
                  break label429;
               }

               var53 = var51;

               try {
                  var2.addImage((FieldNoteImage)var4.get(var6));
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break;
               }

               ++var6;
            }
         }

         Throwable var52 = var10000;
         Utils.closeQuietly(var53);
         throw var52;
      }

      Utils.closeQuietly(var51);
   }

   private static Uri getUriFieldNoteImagesTable(LocusUtils.LocusVersion var0) throws RequiredVersionMissingException {
      Uri var1 = ActionTools.getContentProviderGeocaching(var0, LocusUtils.VersionCode.UPDATE_05, "fieldNoteImages");
      if (var1 == null) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_05);
      } else {
         return var1;
      }
   }

   private static Uri getUriFieldNoteTable(LocusUtils.LocusVersion var0) throws RequiredVersionMissingException {
      Uri var1 = ActionTools.getContentProviderGeocaching(var0, LocusUtils.VersionCode.UPDATE_05, "fieldNotes");
      if (var1 == null) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_05);
      } else {
         return var1;
      }
   }

   public static boolean insert(Context var0, LocusUtils.LocusVersion var1, FieldNote var2) throws RequiredVersionMissingException {
      Uri var3 = getUriFieldNoteTable(var1);
      ContentValues var4 = createContentValues(var2);
      Uri var6 = var0.getContentResolver().insert(var3, var4);
      boolean var5;
      if (var6 != null) {
         var2.setId(Utils.parseLong(var6.getLastPathSegment()));
         storeAllImages(var0, var1, var2);
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   private static boolean insertImage(Context var0, LocusUtils.LocusVersion var1, FieldNoteImage var2) throws RequiredVersionMissingException {
      boolean var3 = true;
      Uri var4 = getUriFieldNoteImagesTable(var1);
      ContentValues var5 = createContentValues(var2, true);
      if (var0.getContentResolver().insert(var4, var5) == null) {
         var3 = false;
      }

      return var3;
   }

   public static void logOnline(Context var0, LocusUtils.LocusVersion var1, long[] var2, boolean var3) throws RequiredVersionMissingException {
      if (var0 != null && var1 != null && var2 != null && var2.length != 0) {
         if (!var1.isVersionValid(LocusUtils.VersionCode.UPDATE_05)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_05);
         } else {
            Intent var4 = new Intent("locus.api.android.LOG_FIELD_NOTES");
            var4.putExtra("INTENT_EXTRA_FIELD_NOTES_IDS", var2);
            var4.putExtra("INTENT_EXTRA_FIELD_NOTES_CREATE_LOG", var3);
            var0.startActivity(var4);
         }
      } else {
         throw new IllegalArgumentException("logOnline(" + var0 + ", " + var1 + ", " + var2 + "), " + "invalid parameters");
      }
   }

   private static void storeAllImages(Context var0, LocusUtils.LocusVersion var1, FieldNote var2) throws RequiredVersionMissingException {
      Iterator var3 = var2.getImages();

      while(var3.hasNext()) {
         FieldNoteImage var4 = (FieldNoteImage)var3.next();
         var4.setFieldNoteId(var2.getId());
         if (var4.getId() >= 0L) {
            updateImage(var0, var1, var4);
         } else {
            insertImage(var0, var1, var4);
         }
      }

   }

   public static boolean update(Context var0, LocusUtils.LocusVersion var1, FieldNote var2) throws RequiredVersionMissingException {
      boolean var3;
      if (update(var0, var1, var2, createContentValues(var2))) {
         storeAllImages(var0, var1, var2);
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public static boolean update(Context var0, LocusUtils.LocusVersion var1, FieldNote var2, ContentValues var3) throws RequiredVersionMissingException {
      boolean var4 = true;
      Uri var5 = getUriFieldNoteTable(var1);
      if (var0.getContentResolver().update(var5, var3, "_id=?", new String[]{Long.toString(var2.getId())}) != 1) {
         var4 = false;
      }

      return var4;
   }

   private static boolean updateImage(Context var0, LocusUtils.LocusVersion var1, FieldNoteImage var2) throws RequiredVersionMissingException {
      boolean var3 = true;
      Uri var4 = getUriFieldNoteImagesTable(var1);
      ContentValues var5 = createContentValues(var2, false);
      if (var0.getContentResolver().update(var4, var5, "_id=?", new String[]{Long.toString(var2.getId())}) != 1) {
         var3 = false;
      }

      return var3;
   }

   public static class ColFieldNote {
      public static final String CACHE_CODE = "cache_code";
      public static final String CACHE_NAME = "cache_name";
      public static final String FAVORITE = "favorite";
      public static final String ID = "_id";
      public static final String LOGGED = "logged";
      public static final String NOTE = "note";
      public static final String TIME = "time";
      public static final String TYPE = "type";
   }

   public static class ColFieldNoteImage {
      public static final String CAPTION = "caption";
      public static final String DATA = "data";
      public static final String DESCRIPTION = "description";
      public static final String FIELD_NOTE_ID = "field_note_id";
      public static final String ID = "_id";
   }
}
