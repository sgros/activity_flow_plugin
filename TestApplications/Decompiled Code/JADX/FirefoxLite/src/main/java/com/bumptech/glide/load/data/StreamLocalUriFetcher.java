package com.bumptech.glide.load.data;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StreamLocalUriFetcher extends LocalUriFetcher<InputStream> {
    private static final UriMatcher URI_MATCHER = new UriMatcher(-1);

    static {
        URI_MATCHER.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
        URI_MATCHER.addURI("com.android.contacts", "contacts/lookup/*", 1);
        URI_MATCHER.addURI("com.android.contacts", "contacts/#/photo", 2);
        URI_MATCHER.addURI("com.android.contacts", "contacts/#", 3);
        URI_MATCHER.addURI("com.android.contacts", "contacts/#/display_photo", 4);
        URI_MATCHER.addURI("com.android.contacts", "phone_lookup/*", 5);
    }

    public StreamLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
    }

    /* Access modifiers changed, original: protected */
    public InputStream loadResource(Uri uri, ContentResolver contentResolver) throws FileNotFoundException {
        InputStream loadResourceFromUri = loadResourceFromUri(uri, contentResolver);
        if (loadResourceFromUri != null) {
            return loadResourceFromUri;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("InputStream is null for ");
        stringBuilder.append(uri);
        throw new FileNotFoundException(stringBuilder.toString());
    }

    private InputStream loadResourceFromUri(Uri uri, ContentResolver contentResolver) throws FileNotFoundException {
        int match = URI_MATCHER.match(uri);
        if (match != 1) {
            if (match == 3) {
                return openContactPhotoInputStream(contentResolver, uri);
            }
            if (match != 5) {
                return contentResolver.openInputStream(uri);
            }
        }
        uri = Contacts.lookupContact(contentResolver, uri);
        if (uri != null) {
            return openContactPhotoInputStream(contentResolver, uri);
        }
        throw new FileNotFoundException("Contact cannot be found");
    }

    @TargetApi(14)
    private InputStream openContactPhotoInputStream(ContentResolver contentResolver, Uri uri) {
        return Contacts.openContactPhotoInputStream(contentResolver, uri, true);
    }

    /* Access modifiers changed, original: protected */
    public void close(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
}
