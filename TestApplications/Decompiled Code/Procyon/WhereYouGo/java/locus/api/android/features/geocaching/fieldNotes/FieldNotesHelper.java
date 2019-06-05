// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android.features.geocaching.fieldNotes;

import java.util.Iterator;
import android.content.Intent;
import locus.api.android.ActionTools;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import locus.api.android.utils.Utils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.android.utils.LocusUtils;
import android.content.Context;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

public class FieldNotesHelper
{
    public static final String PATH_FIELD_NOTES = "fieldNotes";
    public static final String PATH_FIELD_NOTE_IMAGES = "fieldNoteImages";
    
    private static List<FieldNote> create(final Cursor cursor) {
        final ArrayList<FieldNote> list = new ArrayList<FieldNote>();
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be 'null'");
        }
        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToPosition(i);
            final FieldNote fieldNote = new FieldNote();
            fieldNote.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            fieldNote.setCacheCode(cursor.getString(cursor.getColumnIndexOrThrow("cache_code")));
            fieldNote.setCacheName(cursor.getString(cursor.getColumnIndexOrThrow("cache_name")));
            fieldNote.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
            fieldNote.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            final int columnIndex = cursor.getColumnIndex("note");
            if (columnIndex >= 0) {
                fieldNote.setNote(cursor.getString(columnIndex));
            }
            final int columnIndex2 = cursor.getColumnIndex("favorite");
            if (columnIndex2 >= 0) {
                fieldNote.setFavorite(cursor.getInt(columnIndex2) == 1);
            }
            final int columnIndex3 = cursor.getColumnIndex("logged");
            if (columnIndex3 >= 0) {
                fieldNote.setLogged(cursor.getInt(columnIndex3) == 1);
            }
            list.add(fieldNote);
        }
        return list;
    }
    
    private static ContentValues createContentValues(final FieldNote fieldNote) {
        if (fieldNote == null) {
            throw new IllegalArgumentException("Field note cannot be 'null'");
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put("cache_code", fieldNote.getCacheCode());
        contentValues.put("cache_name", fieldNote.getCacheName());
        contentValues.put("type", Integer.valueOf(fieldNote.getType()));
        contentValues.put("time", Long.valueOf(fieldNote.getTime()));
        contentValues.put("note", fieldNote.getNote());
        contentValues.put("favorite", Boolean.valueOf(fieldNote.isFavorite()));
        contentValues.put("logged", Boolean.valueOf(fieldNote.isLogged()));
        return contentValues;
    }
    
    private static ContentValues createContentValues(final FieldNoteImage fieldNoteImage, final boolean b) {
        if (fieldNoteImage == null || fieldNoteImage.getImage() == null) {
            throw new IllegalArgumentException("Field note image cannot be 'null'");
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put("field_note_id", Long.valueOf(fieldNoteImage.getFieldNoteId()));
        contentValues.put("caption", fieldNoteImage.getCaption());
        contentValues.put("description", fieldNoteImage.getDescription());
        if (b) {
            contentValues.put("data", fieldNoteImage.getImage());
        }
        return contentValues;
    }
    
    private static List<FieldNoteImage> createFieldNoteImages(final Cursor cursor) {
        final ArrayList<FieldNoteImage> list = new ArrayList<FieldNoteImage>();
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor cannot be 'null'");
        }
        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToPosition(i);
            final FieldNoteImage fieldNoteImage = new FieldNoteImage();
            fieldNoteImage.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            final int columnIndex = cursor.getColumnIndex("field_note_id");
            if (columnIndex >= 0) {
                fieldNoteImage.setFieldNoteId(cursor.getLong(columnIndex));
            }
            final int columnIndex2 = cursor.getColumnIndex("caption");
            if (columnIndex2 >= 0) {
                fieldNoteImage.setCaption(cursor.getString(columnIndex2));
            }
            final int columnIndex3 = cursor.getColumnIndex("description");
            if (columnIndex3 >= 0) {
                fieldNoteImage.setDescription(cursor.getString(columnIndex3));
            }
            final int columnIndex4 = cursor.getColumnIndex("data");
            if (columnIndex4 >= 0) {
                fieldNoteImage.setImage(cursor.getBlob(columnIndex4));
            }
            list.add(fieldNoteImage);
        }
        return list;
    }
    
    public static boolean delete(final Context context, final LocusUtils.LocusVersion locusVersion, final long i) throws RequiredVersionMissingException {
        boolean b = true;
        final int delete = context.getContentResolver().delete(getUriFieldNoteTable(locusVersion), "_id=?", new String[] { Long.toString(i) });
        deleteImages(context, locusVersion, i);
        if (delete != 1) {
            b = false;
        }
        return b;
    }
    
    public static int deleteAll(final Context context, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        final int delete = context.getContentResolver().delete(getUriFieldNoteTable(locusVersion), (String)null, (String[])null);
        deleteImagesAll(context, locusVersion);
        return delete;
    }
    
    private static void deleteImages(final Context context, final LocusUtils.LocusVersion locusVersion, final long i) throws RequiredVersionMissingException {
        context.getContentResolver().delete(getUriFieldNoteImagesTable(locusVersion), "field_note_id=?", new String[] { Long.toString(i) });
    }
    
    private static void deleteImagesAll(final Context context, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        context.getContentResolver().delete(getUriFieldNoteImagesTable(locusVersion), (String)null, (String[])null);
    }
    
    public static List<FieldNote> get(final Context context, LocusUtils.LocusVersion create, final String s) throws RequiredVersionMissingException {
        final Uri uriFieldNoteTable = getUriFieldNoteTable(create);
        final LocusUtils.LocusVersion locusVersion = null;
        Label_0022: {
            if (s == null) {
                break Label_0022;
            }
            create = locusVersion;
            try {
                Object o;
                if (s.length() == 0) {
                    create = locusVersion;
                    o = context.getContentResolver().query(uriFieldNoteTable, (String[])null, (String)null, (String[])null, (String)null);
                }
                else {
                    create = locusVersion;
                    o = context.getContentResolver().query(uriFieldNoteTable, (String[])null, "cache_code=?", new String[] { s }, (String)null);
                }
                if (o == null) {
                    create = (LocusUtils.LocusVersion)o;
                    final Object o2 = new ArrayList();
                    Utils.closeQuietly((Cursor)o);
                    create = (LocusUtils.LocusVersion)o2;
                }
                else {
                    create = (LocusUtils.LocusVersion)o;
                    create = (LocusUtils.LocusVersion)create((Cursor)o);
                    Utils.closeQuietly((Cursor)o);
                }
                return (List<FieldNote>)create;
            }
            finally {
                Utils.closeQuietly((Cursor)create);
            }
        }
    }
    
    public static FieldNote get(final Context context, final LocusUtils.LocusVersion locusVersion, final long n) throws RequiredVersionMissingException {
        final FieldNote fieldNote = null;
        final Uri withAppendedId = ContentUris.withAppendedId(getUriFieldNoteTable(locusVersion), n);
        Cursor cursor = null;
        try {
            final Cursor query = context.getContentResolver().query(withAppendedId, (String[])null, (String)null, (String[])null, (String)null);
            if (query != null) {
                cursor = query;
                if (query.getCount() == 1) {
                    cursor = query;
                    final FieldNote fieldNote2 = create(query).get(0);
                    cursor = query;
                    getImages(context, locusVersion, fieldNote2);
                    Utils.closeQuietly(query);
                    return fieldNote2;
                }
            }
            Utils.closeQuietly(query);
            return fieldNote;
        }
        finally {
            Utils.closeQuietly(cursor);
        }
    }
    
    public static List<FieldNote> getAll(final Context context, final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        return get(context, locusVersion, "");
    }
    
    public static int getCount(final Context context, LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        final Uri uriFieldNoteTable = getUriFieldNoteTable(locusVersion);
        locusVersion = null;
        try {
            final Object query = context.getContentResolver().query(uriFieldNoteTable, new String[] { "_id" }, (String)null, (String[])null, (String)null);
            int count;
            if (query == null) {
                Utils.closeQuietly((Cursor)query);
                count = 0;
            }
            else {
                locusVersion = (LocusUtils.LocusVersion)query;
                count = ((Cursor)query).getCount();
                Utils.closeQuietly((Cursor)query);
            }
            return count;
        }
        finally {
            Utils.closeQuietly((Cursor)locusVersion);
        }
    }
    
    public static FieldNoteImage getImage(final Context context, LocusUtils.LocusVersion locusVersion, final long n) throws RequiredVersionMissingException {
        final Uri withAppendedId = ContentUris.withAppendedId(getUriFieldNoteImagesTable(locusVersion), n);
        locusVersion = null;
        try {
            final Object query = context.getContentResolver().query(withAppendedId, (String[])null, (String)null, (String[])null, (String)null);
            if (query != null) {
                locusVersion = (LocusUtils.LocusVersion)query;
                if (((Cursor)query).getCount() == 1) {
                    locusVersion = (LocusUtils.LocusVersion)query;
                    final FieldNoteImage fieldNoteImage = createFieldNoteImages((Cursor)query).get(0);
                    Utils.closeQuietly((Cursor)query);
                    return fieldNoteImage;
                }
            }
            Utils.closeQuietly((Cursor)query);
            return null;
        }
        finally {
            Utils.closeQuietly((Cursor)locusVersion);
        }
    }
    
    private static void getImages(final Context context, LocusUtils.LocusVersion locusVersion, final FieldNote fieldNote) throws RequiredVersionMissingException {
        final Uri uriFieldNoteImagesTable = getUriFieldNoteImagesTable(locusVersion);
        final LocusUtils.LocusVersion locusVersion2 = locusVersion = null;
        try {
            final ContentResolver contentResolver = context.getContentResolver();
            locusVersion = locusVersion2;
            final String string = Long.toString(fieldNote.getId());
            locusVersion = locusVersion2;
            final Object query = contentResolver.query(uriFieldNoteImagesTable, new String[] { "_id" }, "field_note_id=?", new String[] { string }, (String)null);
            if (query != null) {
                locusVersion = (LocusUtils.LocusVersion)query;
                final List<FieldNoteImage> fieldNoteImages = createFieldNoteImages((Cursor)query);
                int i = 0;
                locusVersion = (LocusUtils.LocusVersion)query;
                while (i < fieldNoteImages.size()) {
                    locusVersion = (LocusUtils.LocusVersion)query;
                    fieldNote.addImage(fieldNoteImages.get(i));
                    ++i;
                }
            }
        }
        finally {
            Utils.closeQuietly((Cursor)locusVersion);
        }
    }
    
    private static Uri getUriFieldNoteImagesTable(final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        final Uri contentProviderGeocaching = ActionTools.getContentProviderGeocaching(locusVersion, LocusUtils.VersionCode.UPDATE_05, "fieldNoteImages");
        if (contentProviderGeocaching == null) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_05);
        }
        return contentProviderGeocaching;
    }
    
    private static Uri getUriFieldNoteTable(final LocusUtils.LocusVersion locusVersion) throws RequiredVersionMissingException {
        final Uri contentProviderGeocaching = ActionTools.getContentProviderGeocaching(locusVersion, LocusUtils.VersionCode.UPDATE_05, "fieldNotes");
        if (contentProviderGeocaching == null) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_05);
        }
        return contentProviderGeocaching;
    }
    
    public static boolean insert(final Context context, final LocusUtils.LocusVersion locusVersion, final FieldNote fieldNote) throws RequiredVersionMissingException {
        final Uri insert = context.getContentResolver().insert(getUriFieldNoteTable(locusVersion), createContentValues(fieldNote));
        boolean b;
        if (insert != null) {
            fieldNote.setId(locus.api.utils.Utils.parseLong(insert.getLastPathSegment()));
            storeAllImages(context, locusVersion, fieldNote);
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private static boolean insertImage(final Context context, final LocusUtils.LocusVersion locusVersion, final FieldNoteImage fieldNoteImage) throws RequiredVersionMissingException {
        boolean b = true;
        if (context.getContentResolver().insert(getUriFieldNoteImagesTable(locusVersion), createContentValues(fieldNoteImage, true)) == null) {
            b = false;
        }
        return b;
    }
    
    public static void logOnline(final Context obj, final LocusUtils.LocusVersion obj2, final long[] obj3, final boolean b) throws RequiredVersionMissingException {
        if (obj == null || obj2 == null || obj3 == null || obj3.length == 0) {
            throw new IllegalArgumentException("logOnline(" + obj + ", " + obj2 + ", " + obj3 + "), " + "invalid parameters");
        }
        if (!obj2.isVersionValid(LocusUtils.VersionCode.UPDATE_05)) {
            throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_05);
        }
        final Intent intent = new Intent("locus.api.android.LOG_FIELD_NOTES");
        intent.putExtra("INTENT_EXTRA_FIELD_NOTES_IDS", obj3);
        intent.putExtra("INTENT_EXTRA_FIELD_NOTES_CREATE_LOG", b);
        obj.startActivity(intent);
    }
    
    private static void storeAllImages(final Context context, final LocusUtils.LocusVersion locusVersion, final FieldNote fieldNote) throws RequiredVersionMissingException {
        final Iterator<FieldNoteImage> images = fieldNote.getImages();
        while (images.hasNext()) {
            final FieldNoteImage fieldNoteImage = images.next();
            fieldNoteImage.setFieldNoteId(fieldNote.getId());
            if (fieldNoteImage.getId() >= 0L) {
                updateImage(context, locusVersion, fieldNoteImage);
            }
            else {
                insertImage(context, locusVersion, fieldNoteImage);
            }
        }
    }
    
    public static boolean update(final Context context, final LocusUtils.LocusVersion locusVersion, final FieldNote fieldNote) throws RequiredVersionMissingException {
        boolean b;
        if (update(context, locusVersion, fieldNote, createContentValues(fieldNote))) {
            storeAllImages(context, locusVersion, fieldNote);
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public static boolean update(final Context context, final LocusUtils.LocusVersion locusVersion, final FieldNote fieldNote, final ContentValues contentValues) throws RequiredVersionMissingException {
        boolean b = true;
        if (context.getContentResolver().update(getUriFieldNoteTable(locusVersion), contentValues, "_id=?", new String[] { Long.toString(fieldNote.getId()) }) != 1) {
            b = false;
        }
        return b;
    }
    
    private static boolean updateImage(final Context context, final LocusUtils.LocusVersion locusVersion, final FieldNoteImage fieldNoteImage) throws RequiredVersionMissingException {
        boolean b = true;
        if (context.getContentResolver().update(getUriFieldNoteImagesTable(locusVersion), createContentValues(fieldNoteImage, false), "_id=?", new String[] { Long.toString(fieldNoteImage.getId()) }) != 1) {
            b = false;
        }
        return b;
    }
    
    public static class ColFieldNote
    {
        public static final String CACHE_CODE = "cache_code";
        public static final String CACHE_NAME = "cache_name";
        public static final String FAVORITE = "favorite";
        public static final String ID = "_id";
        public static final String LOGGED = "logged";
        public static final String NOTE = "note";
        public static final String TIME = "time";
        public static final String TYPE = "type";
    }
    
    public static class ColFieldNoteImage
    {
        public static final String CAPTION = "caption";
        public static final String DATA = "data";
        public static final String DESCRIPTION = "description";
        public static final String FIELD_NOTE_ID = "field_note_id";
        public static final String ID = "_id";
    }
}
