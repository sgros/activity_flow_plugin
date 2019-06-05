// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import java.io.IOException;
import android.annotation.TargetApi;
import java.io.FileNotFoundException;
import android.provider.ContactsContract$Contacts;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.UriMatcher;
import java.io.InputStream;

public class StreamLocalUriFetcher extends LocalUriFetcher<InputStream>
{
    private static final UriMatcher URI_MATCHER;
    
    static {
        (URI_MATCHER = new UriMatcher(-1)).addURI("com.android.contacts", "contacts/lookup/*/#", 1);
        StreamLocalUriFetcher.URI_MATCHER.addURI("com.android.contacts", "contacts/lookup/*", 1);
        StreamLocalUriFetcher.URI_MATCHER.addURI("com.android.contacts", "contacts/#/photo", 2);
        StreamLocalUriFetcher.URI_MATCHER.addURI("com.android.contacts", "contacts/#", 3);
        StreamLocalUriFetcher.URI_MATCHER.addURI("com.android.contacts", "contacts/#/display_photo", 4);
        StreamLocalUriFetcher.URI_MATCHER.addURI("com.android.contacts", "phone_lookup/*", 5);
    }
    
    public StreamLocalUriFetcher(final ContentResolver contentResolver, final Uri uri) {
        super(contentResolver, uri);
    }
    
    private InputStream loadResourceFromUri(Uri lookupContact, final ContentResolver contentResolver) throws FileNotFoundException {
        final int match = StreamLocalUriFetcher.URI_MATCHER.match(lookupContact);
        if (match != 1) {
            if (match == 3) {
                return this.openContactPhotoInputStream(contentResolver, lookupContact);
            }
            if (match != 5) {
                return contentResolver.openInputStream(lookupContact);
            }
        }
        lookupContact = ContactsContract$Contacts.lookupContact(contentResolver, lookupContact);
        if (lookupContact != null) {
            return this.openContactPhotoInputStream(contentResolver, lookupContact);
        }
        throw new FileNotFoundException("Contact cannot be found");
    }
    
    @TargetApi(14)
    private InputStream openContactPhotoInputStream(final ContentResolver contentResolver, final Uri uri) {
        return ContactsContract$Contacts.openContactPhotoInputStream(contentResolver, uri, true);
    }
    
    @Override
    protected void close(final InputStream inputStream) throws IOException {
        inputStream.close();
    }
    
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
    
    @Override
    protected InputStream loadResource(final Uri obj, final ContentResolver contentResolver) throws FileNotFoundException {
        final InputStream loadResourceFromUri = this.loadResourceFromUri(obj, contentResolver);
        if (loadResourceFromUri != null) {
            return loadResourceFromUri;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("InputStream is null for ");
        sb.append(obj);
        throw new FileNotFoundException(sb.toString());
    }
}
