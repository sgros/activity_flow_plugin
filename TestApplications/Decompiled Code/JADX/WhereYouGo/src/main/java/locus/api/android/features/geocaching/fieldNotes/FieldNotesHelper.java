package locus.api.android.features.geocaching.fieldNotes;

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
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils.LocusVersion;
import locus.api.android.utils.LocusUtils.VersionCode;
import locus.api.android.utils.Utils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;

public class FieldNotesHelper {
    public static final String PATH_FIELD_NOTES = "fieldNotes";
    public static final String PATH_FIELD_NOTE_IMAGES = "fieldNoteImages";

    public static class ColFieldNote {
        public static final String CACHE_CODE = "cache_code";
        public static final String CACHE_NAME = "cache_name";
        public static final String FAVORITE = "favorite";
        /* renamed from: ID */
        public static final String f40ID = "_id";
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
        /* renamed from: ID */
        public static final String f41ID = "_id";
    }

    public static int getCount(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        Uri cpUri = getUriFieldNoteTable(lv);
        Cursor c = null;
        try {
            c = ctx.getContentResolver().query(cpUri, new String[]{"_id"}, null, null, null);
            if (c == null) {
                return 0;
            }
            int count = c.getCount();
            Utils.closeQuietly(c);
            return count;
        } finally {
            Utils.closeQuietly(c);
        }
    }

    public static List<FieldNote> getAll(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        return get(ctx, lv, "");
    }

    public static FieldNote get(Context ctx, LocusVersion lv, long id) throws RequiredVersionMissingException {
        FieldNote fn = null;
        Cursor c = null;
        try {
            c = ctx.getContentResolver().query(ContentUris.withAppendedId(getUriFieldNoteTable(lv), id), null, null, null, null);
            if (c == null || c.getCount() != 1) {
                Utils.closeQuietly(c);
            } else {
                fn = (FieldNote) create(c).get(0);
                getImages(ctx, lv, fn);
                Utils.closeQuietly(c);
            }
            return fn;
        } catch (Throwable th) {
            Utils.closeQuietly(c);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0037 A:{Catch:{ all -> 0x003f }} */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x001b A:{Catch:{ all -> 0x003f }} */
    public static java.util.List<locus.api.android.features.geocaching.fieldNotes.FieldNote> get(android.content.Context r7, locus.api.android.utils.LocusUtils.LocusVersion r8, java.lang.String r9) throws locus.api.android.utils.exceptions.RequiredVersionMissingException {
        /*
        r1 = getUriFieldNoteTable(r8);
        r6 = 0;
        if (r9 == 0) goto L_0x000d;
    L_0x0007:
        r0 = r9.length();	 Catch:{ all -> 0x003f }
        if (r0 != 0) goto L_0x0024;
    L_0x000d:
        r0 = r7.getContentResolver();	 Catch:{ all -> 0x003f }
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ all -> 0x003f }
    L_0x0019:
        if (r6 != 0) goto L_0x0037;
    L_0x001b:
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x003f }
        r0.<init>();	 Catch:{ all -> 0x003f }
        locus.api.android.utils.Utils.closeQuietly(r6);
    L_0x0023:
        return r0;
    L_0x0024:
        r0 = r7.getContentResolver();	 Catch:{ all -> 0x003f }
        r2 = 0;
        r3 = "cache_code=?";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ all -> 0x003f }
        r5 = 0;
        r4[r5] = r9;	 Catch:{ all -> 0x003f }
        r5 = 0;
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ all -> 0x003f }
        goto L_0x0019;
    L_0x0037:
        r0 = create(r6);	 Catch:{ all -> 0x003f }
        locus.api.android.utils.Utils.closeQuietly(r6);
        goto L_0x0023;
    L_0x003f:
        r0 = move-exception;
        locus.api.android.utils.Utils.closeQuietly(r6);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: locus.api.android.features.geocaching.fieldNotes.FieldNotesHelper.get(android.content.Context, locus.api.android.utils.LocusUtils$LocusVersion, java.lang.String):java.util.List");
    }

    public static boolean delete(Context ctx, LocusVersion lv, long fieldNoteId) throws RequiredVersionMissingException {
        int res = ctx.getContentResolver().delete(getUriFieldNoteTable(lv), "_id=?", new String[]{Long.toString(fieldNoteId)});
        deleteImages(ctx, lv, fieldNoteId);
        if (res == 1) {
            return true;
        }
        return false;
    }

    public static int deleteAll(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        int count = ctx.getContentResolver().delete(getUriFieldNoteTable(lv), null, null);
        deleteImagesAll(ctx, lv);
        return count;
    }

    public static boolean insert(Context ctx, LocusVersion lv, FieldNote gcFn) throws RequiredVersionMissingException {
        Uri newRow = ctx.getContentResolver().insert(getUriFieldNoteTable(lv), createContentValues(gcFn));
        if (newRow == null) {
            return false;
        }
        gcFn.setId(locus.api.utils.Utils.parseLong(newRow.getLastPathSegment()));
        storeAllImages(ctx, lv, gcFn);
        return true;
    }

    public static boolean update(Context ctx, LocusVersion lv, FieldNote gcFn) throws RequiredVersionMissingException {
        if (!update(ctx, lv, gcFn, createContentValues(gcFn))) {
            return false;
        }
        storeAllImages(ctx, lv, gcFn);
        return true;
    }

    public static boolean update(Context ctx, LocusVersion lv, FieldNote fn, ContentValues cv) throws RequiredVersionMissingException {
        if (ctx.getContentResolver().update(getUriFieldNoteTable(lv), cv, "_id=?", new String[]{Long.toString(fn.getId())}) == 1) {
            return true;
        }
        return false;
    }

    private static void storeAllImages(Context ctx, LocusVersion lv, FieldNote fn) throws RequiredVersionMissingException {
        Iterator<FieldNoteImage> images = fn.getImages();
        while (images.hasNext()) {
            FieldNoteImage img = (FieldNoteImage) images.next();
            img.setFieldNoteId(fn.getId());
            if (img.getId() >= 0) {
                updateImage(ctx, lv, img);
            } else {
                insertImage(ctx, lv, img);
            }
        }
    }

    public static FieldNoteImage getImage(Context ctx, LocusVersion lv, long imgId) throws RequiredVersionMissingException {
        Cursor c = null;
        try {
            c = ctx.getContentResolver().query(ContentUris.withAppendedId(getUriFieldNoteImagesTable(lv), imgId), null, null, null, null);
            if (c == null || c.getCount() != 1) {
                Utils.closeQuietly(c);
                return null;
            }
            FieldNoteImage fieldNoteImage = (FieldNoteImage) createFieldNoteImages(c).get(0);
            Utils.closeQuietly(c);
            return fieldNoteImage;
        } catch (Throwable th) {
            Utils.closeQuietly(c);
        }
    }

    private static void getImages(Context ctx, LocusVersion lv, FieldNote fn) throws RequiredVersionMissingException {
        Uri cpUri = getUriFieldNoteImagesTable(lv);
        try {
            Cursor c = ctx.getContentResolver().query(cpUri, new String[]{"_id"}, "field_note_id=?", new String[]{Long.toString(fn.getId())}, null);
            if (c != null) {
                List<FieldNoteImage> images = createFieldNoteImages(c);
                int m = images.size();
                for (int i = 0; i < m; i++) {
                    fn.addImage((FieldNoteImage) images.get(i));
                }
            }
            Utils.closeQuietly(c);
        } catch (Throwable th) {
            Utils.closeQuietly(null);
        }
    }

    private static void deleteImages(Context ctx, LocusVersion lv, long fieldNoteId) throws RequiredVersionMissingException {
        ctx.getContentResolver().delete(getUriFieldNoteImagesTable(lv), "field_note_id=?", new String[]{Long.toString(fieldNoteId)});
    }

    private static void deleteImagesAll(Context ctx, LocusVersion lv) throws RequiredVersionMissingException {
        ctx.getContentResolver().delete(getUriFieldNoteImagesTable(lv), null, null);
    }

    private static boolean updateImage(Context ctx, LocusVersion lv, FieldNoteImage img) throws RequiredVersionMissingException {
        if (ctx.getContentResolver().update(getUriFieldNoteImagesTable(lv), createContentValues(img, false), "_id=?", new String[]{Long.toString(img.getId())}) == 1) {
            return true;
        }
        return false;
    }

    private static boolean insertImage(Context ctx, LocusVersion lv, FieldNoteImage img) throws RequiredVersionMissingException {
        if (ctx.getContentResolver().insert(getUriFieldNoteImagesTable(lv), createContentValues(img, true)) != null) {
            return true;
        }
        return false;
    }

    private static Uri getUriFieldNoteTable(LocusVersion lv) throws RequiredVersionMissingException {
        Uri uri = ActionTools.getContentProviderGeocaching(lv, VersionCode.UPDATE_05, PATH_FIELD_NOTES);
        if (uri != null) {
            return uri;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_05);
    }

    private static Uri getUriFieldNoteImagesTable(LocusVersion lv) throws RequiredVersionMissingException {
        Uri uri = ActionTools.getContentProviderGeocaching(lv, VersionCode.UPDATE_05, PATH_FIELD_NOTE_IMAGES);
        if (uri != null) {
            return uri;
        }
        throw new RequiredVersionMissingException(VersionCode.UPDATE_05);
    }

    private static List<FieldNote> create(Cursor cursor) {
        List<FieldNote> res = new ArrayList();
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be 'null'");
        }
        int m = cursor.getCount();
        for (int i = 0; i < m; i++) {
            boolean z;
            cursor.moveToPosition(i);
            FieldNote fn = new FieldNote();
            fn.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            fn.setCacheCode(cursor.getString(cursor.getColumnIndexOrThrow(ColFieldNote.CACHE_CODE)));
            fn.setCacheName(cursor.getString(cursor.getColumnIndexOrThrow(ColFieldNote.CACHE_NAME)));
            fn.setType(cursor.getInt(cursor.getColumnIndexOrThrow(ColFieldNote.TYPE)));
            fn.setTime(cursor.getLong(cursor.getColumnIndex(ColFieldNote.TIME)));
            int iNote = cursor.getColumnIndex(ColFieldNote.NOTE);
            if (iNote >= 0) {
                fn.setNote(cursor.getString(iNote));
            }
            int iFavorite = cursor.getColumnIndex(ColFieldNote.FAVORITE);
            if (iFavorite >= 0) {
                if (cursor.getInt(iFavorite) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                fn.setFavorite(z);
            }
            int iLogged = cursor.getColumnIndex(ColFieldNote.LOGGED);
            if (iLogged >= 0) {
                if (cursor.getInt(iLogged) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                fn.setLogged(z);
            }
            res.add(fn);
        }
        return res;
    }

    private static ContentValues createContentValues(FieldNote fn) {
        if (fn == null) {
            throw new IllegalArgumentException("Field note cannot be 'null'");
        }
        ContentValues cv = new ContentValues();
        cv.put(ColFieldNote.CACHE_CODE, fn.getCacheCode());
        cv.put(ColFieldNote.CACHE_NAME, fn.getCacheName());
        cv.put(ColFieldNote.TYPE, Integer.valueOf(fn.getType()));
        cv.put(ColFieldNote.TIME, Long.valueOf(fn.getTime()));
        cv.put(ColFieldNote.NOTE, fn.getNote());
        cv.put(ColFieldNote.FAVORITE, Boolean.valueOf(fn.isFavorite()));
        cv.put(ColFieldNote.LOGGED, Boolean.valueOf(fn.isLogged()));
        return cv;
    }

    private static List<FieldNoteImage> createFieldNoteImages(Cursor cursor) {
        List<FieldNoteImage> res = new ArrayList();
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be 'null'");
        }
        int m = cursor.getCount();
        for (int i = 0; i < m; i++) {
            cursor.moveToPosition(i);
            FieldNoteImage img = new FieldNoteImage();
            img.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            int iFnId = cursor.getColumnIndex(ColFieldNoteImage.FIELD_NOTE_ID);
            if (iFnId >= 0) {
                img.setFieldNoteId(cursor.getLong(iFnId));
            }
            int iCap = cursor.getColumnIndex(ColFieldNoteImage.CAPTION);
            if (iCap >= 0) {
                img.setCaption(cursor.getString(iCap));
            }
            int iDesc = cursor.getColumnIndex(ColFieldNoteImage.DESCRIPTION);
            if (iDesc >= 0) {
                img.setDescription(cursor.getString(iDesc));
            }
            int iData = cursor.getColumnIndex(ColFieldNoteImage.DATA);
            if (iData >= 0) {
                img.setImage(cursor.getBlob(iData));
            }
            res.add(img);
        }
        return res;
    }

    private static ContentValues createContentValues(FieldNoteImage img, boolean alsoData) {
        if (img == null || img.getImage() == null) {
            throw new IllegalArgumentException("Field note image cannot be 'null'");
        }
        ContentValues cv = new ContentValues();
        cv.put(ColFieldNoteImage.FIELD_NOTE_ID, Long.valueOf(img.getFieldNoteId()));
        cv.put(ColFieldNoteImage.CAPTION, img.getCaption());
        cv.put(ColFieldNoteImage.DESCRIPTION, img.getDescription());
        if (alsoData) {
            cv.put(ColFieldNoteImage.DATA, img.getImage());
        }
        return cv;
    }

    public static void logOnline(Context ctx, LocusVersion lv, long[] ids, boolean createLog) throws RequiredVersionMissingException {
        if (ctx == null || lv == null || ids == null || ids.length == 0) {
            throw new IllegalArgumentException("logOnline(" + ctx + ", " + lv + ", " + ids + "), " + "invalid parameters");
        } else if (lv.isVersionValid(VersionCode.UPDATE_05)) {
            Intent intent = new Intent(LocusConst.ACTION_LOG_FIELD_NOTES);
            intent.putExtra(LocusConst.INTENT_EXTRA_FIELD_NOTES_IDS, ids);
            intent.putExtra(LocusConst.INTENT_EXTRA_FIELD_NOTES_CREATE_LOG, createLog);
            ctx.startActivity(intent);
        } else {
            throw new RequiredVersionMissingException(VersionCode.UPDATE_05);
        }
    }
}
