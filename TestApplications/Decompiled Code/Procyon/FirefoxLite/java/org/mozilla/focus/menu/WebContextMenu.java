// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.menu;

import android.content.DialogInterface;
import android.content.DialogInterface$OnCancelListener;
import android.support.v7.app.AlertDialog;
import android.content.ClipData;
import android.net.Uri;
import android.content.ClipboardManager;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.focus.download.GetImgHeaderTask;
import android.webkit.URLUtil;
import android.content.Intent;
import android.view.MenuItem;
import org.mozilla.urlutils.UrlUtils;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import java.util.Iterator;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.Session;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.View;
import android.content.Context;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import android.text.TextUtils;
import android.app.Activity;
import android.app.Dialog;
import org.mozilla.rocket.tabs.TabView;

public class WebContextMenu
{
    private static boolean canOpenInNewTab(final Activity activity, final String s) {
        return activity != null && !TextUtils.isEmpty((CharSequence)s) && TabsSessionProvider.getOrNull(activity) != null;
    }
    
    private static View createTitleView(final Context context, final String text) {
        final TextView textView = (TextView)LayoutInflater.from(context).inflate(2131492925, (ViewGroup)null);
        textView.setText((CharSequence)text);
        return (View)textView;
    }
    
    private static void openInNewTab(final TabView tabView, final Dialog dialog, final String s) {
        final SessionManager orThrow = TabsSessionProvider.getOrThrow(dialog.getOwnerActivity());
        while (true) {
            for (final Session session : orThrow.getTabs()) {
                if (session.getEngineSession().getTabView() == tabView) {
                    final String id = session.getId();
                    final Bundle argument = TabUtil.argument(id, false, false);
                    argument.putInt("extra_bkg_tab_src", 0);
                    orThrow.addTab(s, argument);
                    TelemetryWrapper.addNewTabFromContextMenu();
                    return;
                }
            }
            final String id = null;
            continue;
        }
    }
    
    private static void setupMenuForHitTarget(final boolean b, final Dialog dialog, final NavigationView navigationView, final DownloadCallback downloadCallback, final TabView.HitTarget hitTarget) {
        navigationView.inflateMenu(2131558401);
        String s;
        if (hitTarget.isLink) {
            s = hitTarget.linkURL;
        }
        else {
            s = hitTarget.imageURL;
        }
        final boolean canOpenInNewTab = canOpenInNewTab(dialog.getOwnerActivity(), s);
        final boolean b2 = true;
        final boolean b3 = canOpenInNewTab && !b;
        navigationView.getMenu().findItem(2131296518).setVisible(b3 && hitTarget.isLink);
        navigationView.getMenu().findItem(2131296519).setVisible(b3 && !hitTarget.isLink && hitTarget.isImage);
        navigationView.getMenu().findItem(2131296516).setVisible(hitTarget.isLink);
        navigationView.getMenu().findItem(2131296515).setVisible(hitTarget.isLink);
        navigationView.getMenu().findItem(2131296514).setVisible(hitTarget.isImage);
        navigationView.getMenu().findItem(2131296512).setVisible(hitTarget.isImage);
        navigationView.getMenu().findItem(2131296513).setVisible(hitTarget.isImage && UrlUtils.isHttpOrHttps(hitTarget.imageURL) && b2);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener)new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                dialog.dismiss();
                switch (menuItem.getItemId()) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unhandled menu item id=");
                        sb.append(menuItem.getItemId());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    case 2131296518:
                    case 2131296519: {
                        openInNewTab(hitTarget.source, dialog, s);
                        return true;
                    }
                    case 2131296516: {
                        TelemetryWrapper.shareLinkEvent();
                        final Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.TEXT", hitTarget.linkURL);
                        dialog.getContext().startActivity(Intent.createChooser(intent, (CharSequence)dialog.getContext().getString(2131755412)));
                        return true;
                    }
                    case 2131296514: {
                        TelemetryWrapper.shareImageEvent();
                        final Intent intent2 = new Intent("android.intent.action.SEND");
                        intent2.setType("text/plain");
                        intent2.putExtra("android.intent.extra.TEXT", hitTarget.imageURL);
                        dialog.getContext().startActivity(Intent.createChooser(intent2, (CharSequence)dialog.getContext().getString(2131755412)));
                        return true;
                    }
                    case 2131296513: {
                        if (URLUtil.guessFileName(hitTarget.imageURL, (String)null, (String)null).endsWith(".bin")) {
                            final GetImgHeaderTask getImgHeaderTask = new GetImgHeaderTask();
                            getImgHeaderTask.setCallback((GetImgHeaderTask.Callback)new GetImgHeaderTask.Callback() {
                                @Override
                                public void setMIMEType(final String s) {
                                    downloadCallback.onDownloadStart(new Download(hitTarget.imageURL, null, null, null, s, -1L, true));
                                }
                            });
                            getImgHeaderTask.execute((Object[])new String[] { hitTarget.imageURL });
                        }
                        else {
                            downloadCallback.onDownloadStart(new Download(hitTarget.imageURL, null, null, null, null, -1L, true));
                        }
                        TelemetryWrapper.saveImageEvent();
                        return true;
                    }
                    case 2131296512:
                    case 2131296515: {
                        final ClipboardManager clipboardManager = (ClipboardManager)dialog.getContext().getSystemService("clipboard");
                        Uri uri;
                        if (menuItem.getItemId() == 2131296515) {
                            TelemetryWrapper.copyLinkEvent();
                            uri = Uri.parse(hitTarget.linkURL);
                        }
                        else {
                            if (menuItem.getItemId() != 2131296512) {
                                throw new IllegalStateException("Unknown hitTarget type - cannot copy to clipboard");
                            }
                            TelemetryWrapper.copyImageEvent();
                            uri = Uri.parse(hitTarget.imageURL);
                        }
                        clipboardManager.setPrimaryClip(ClipData.newUri(dialog.getContext().getContentResolver(), (CharSequence)"URI", uri));
                        return true;
                    }
                }
            }
        });
    }
    
    public static Dialog show(final boolean b, final Activity ownerActivity, final DownloadCallback downloadCallback, final TabView.HitTarget hitTarget) {
        if (!hitTarget.isLink && !hitTarget.isImage) {
            throw new IllegalStateException("WebContextMenu can only handle long-press on images and/or links.");
        }
        TelemetryWrapper.openWebContextMenuEvent();
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ownerActivity);
        View customTitle;
        if (hitTarget.isLink) {
            customTitle = createTitleView((Context)ownerActivity, hitTarget.linkURL);
        }
        else {
            if (!hitTarget.isImage) {
                throw new IllegalStateException("Unhandled long press target type");
            }
            customTitle = createTitleView((Context)ownerActivity, hitTarget.imageURL);
        }
        builder.setCustomTitle(customTitle);
        final View inflate = LayoutInflater.from((Context)ownerActivity).inflate(2131492924, (ViewGroup)null);
        builder.setView(inflate);
        builder.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                TelemetryWrapper.cancelWebContextMenuEvent();
            }
        });
        final AlertDialog create = builder.create();
        create.setOwnerActivity(ownerActivity);
        setupMenuForHitTarget(b, create, (NavigationView)inflate.findViewById(2131296380), downloadCallback, hitTarget);
        create.show();
        return create;
    }
}
