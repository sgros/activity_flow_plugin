package org.mozilla.focus.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.p004v7.app.AlertDialog;
import android.support.p004v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.download.GetImgHeaderTask;
import org.mozilla.focus.download.GetImgHeaderTask.Callback;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabView.HitTarget;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import org.mozilla.urlutils.UrlUtils;

public class WebContextMenu {

    /* renamed from: org.mozilla.focus.menu.WebContextMenu$1 */
    static class C04941 implements OnCancelListener {
        C04941() {
        }

        public void onCancel(DialogInterface dialogInterface) {
            TelemetryWrapper.cancelWebContextMenuEvent();
        }
    }

    private static View createTitleView(Context context, String str) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(C0769R.layout.context_menu_title, (ViewGroup) null);
        textView.setText(str);
        return textView;
    }

    public static Dialog show(boolean z, Activity activity, DownloadCallback downloadCallback, HitTarget hitTarget) {
        if (hitTarget.isLink || hitTarget.isImage) {
            View createTitleView;
            TelemetryWrapper.openWebContextMenuEvent();
            Builder builder = new Builder(activity);
            if (hitTarget.isLink) {
                createTitleView = createTitleView(activity, hitTarget.linkURL);
            } else if (hitTarget.isImage) {
                createTitleView = createTitleView(activity, hitTarget.imageURL);
            } else {
                throw new IllegalStateException("Unhandled long press target type");
            }
            builder.setCustomTitle(createTitleView);
            createTitleView = LayoutInflater.from(activity).inflate(C0769R.layout.context_menu, (ViewGroup) null);
            builder.setView(createTitleView);
            builder.setOnCancelListener(new C04941());
            AlertDialog create = builder.create();
            create.setOwnerActivity(activity);
            setupMenuForHitTarget(z, create, (NavigationView) createTitleView.findViewById(C0427R.C0426id.context_menu), downloadCallback, hitTarget);
            create.show();
            return create;
        }
        throw new IllegalStateException("WebContextMenu can only handle long-press on images and/or links.");
    }

    private static void setupMenuForHitTarget(boolean z, final Dialog dialog, NavigationView navigationView, final DownloadCallback downloadCallback, final HitTarget hitTarget) {
        navigationView.inflateMenu(2131558401);
        final String str = hitTarget.isLink ? hitTarget.linkURL : hitTarget.imageURL;
        boolean z2 = true;
        Object obj = (!canOpenInNewTab(dialog.getOwnerActivity(), str) || z) ? null : 1;
        MenuItem findItem = navigationView.getMenu().findItem(C0427R.C0426id.menu_new_tab);
        boolean z3 = obj != null && hitTarget.isLink;
        findItem.setVisible(z3);
        findItem = navigationView.getMenu().findItem(C0427R.C0426id.menu_new_tab_image);
        z = (obj == null || hitTarget.isLink || !hitTarget.isImage) ? false : true;
        findItem.setVisible(z);
        navigationView.getMenu().findItem(C0427R.C0426id.menu_link_share).setVisible(hitTarget.isLink);
        navigationView.getMenu().findItem(C0427R.C0426id.menu_link_copy).setVisible(hitTarget.isLink);
        navigationView.getMenu().findItem(C0427R.C0426id.menu_image_share).setVisible(hitTarget.isImage);
        navigationView.getMenu().findItem(C0427R.C0426id.menu_image_copy).setVisible(hitTarget.isImage);
        MenuItem findItem2 = navigationView.getMenu().findItem(C0427R.C0426id.menu_image_save);
        if (!(hitTarget.isImage && UrlUtils.isHttpOrHttps(hitTarget.imageURL))) {
            z2 = false;
        }
        findItem2.setVisible(z2);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {

            /* renamed from: org.mozilla.focus.menu.WebContextMenu$2$1 */
            class C04961 implements Callback {
                C04961() {
                }

                public void setMIMEType(String str) {
                    downloadCallback.onDownloadStart(new Download(hitTarget.imageURL, null, null, null, str, -1, true));
                }
            }

            public boolean onNavigationItemSelected(MenuItem menuItem) {
                dialog.dismiss();
                Intent intent;
                switch (menuItem.getItemId()) {
                    case C0427R.C0426id.menu_image_copy /*2131296512*/:
                    case C0427R.C0426id.menu_link_copy /*2131296515*/:
                        Uri parse;
                        ClipboardManager clipboardManager = (ClipboardManager) dialog.getContext().getSystemService("clipboard");
                        if (menuItem.getItemId() == C0427R.C0426id.menu_link_copy) {
                            TelemetryWrapper.copyLinkEvent();
                            parse = Uri.parse(hitTarget.linkURL);
                        } else if (menuItem.getItemId() == C0427R.C0426id.menu_image_copy) {
                            TelemetryWrapper.copyImageEvent();
                            parse = Uri.parse(hitTarget.imageURL);
                        } else {
                            throw new IllegalStateException("Unknown hitTarget type - cannot copy to clipboard");
                        }
                        clipboardManager.setPrimaryClip(ClipData.newUri(dialog.getContext().getContentResolver(), "URI", parse));
                        return true;
                    case C0427R.C0426id.menu_image_save /*2131296513*/:
                        if (URLUtil.guessFileName(hitTarget.imageURL, null, null).endsWith(".bin")) {
                            GetImgHeaderTask getImgHeaderTask = new GetImgHeaderTask();
                            getImgHeaderTask.setCallback(new C04961());
                            getImgHeaderTask.execute(new String[]{hitTarget.imageURL});
                        } else {
                            downloadCallback.onDownloadStart(new Download(hitTarget.imageURL, null, null, null, null, -1, true));
                        }
                        TelemetryWrapper.saveImageEvent();
                        return true;
                    case C0427R.C0426id.menu_image_share /*2131296514*/:
                        TelemetryWrapper.shareImageEvent();
                        intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.TEXT", hitTarget.imageURL);
                        dialog.getContext().startActivity(Intent.createChooser(intent, dialog.getContext().getString(C0769R.string.share_dialog_title)));
                        return true;
                    case C0427R.C0426id.menu_link_share /*2131296516*/:
                        TelemetryWrapper.shareLinkEvent();
                        intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.TEXT", hitTarget.linkURL);
                        dialog.getContext().startActivity(Intent.createChooser(intent, dialog.getContext().getString(C0769R.string.share_dialog_title)));
                        return true;
                    case C0427R.C0426id.menu_new_tab /*2131296518*/:
                    case C0427R.C0426id.menu_new_tab_image /*2131296519*/:
                        WebContextMenu.openInNewTab(hitTarget.source, dialog, str);
                        return true;
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unhandled menu item id=");
                        stringBuilder.append(menuItem.getItemId());
                        throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
        });
    }

    private static boolean canOpenInNewTab(Activity activity, String str) {
        return (activity == null || TextUtils.isEmpty(str) || TabsSessionProvider.getOrNull(activity) == null) ? false : true;
    }

    private static void openInNewTab(TabView tabView, Dialog dialog, String str) {
        String id;
        SessionManager orThrow = TabsSessionProvider.getOrThrow(dialog.getOwnerActivity());
        for (Session session : orThrow.getTabs()) {
            if (session.getEngineSession().getTabView() == tabView) {
                id = session.getId();
                break;
            }
        }
        id = null;
        Bundle argument = TabUtil.argument(id, false, false);
        argument.putInt("extra_bkg_tab_src", 0);
        orThrow.addTab(str, argument);
        TelemetryWrapper.addNewTabFromContextMenu();
    }
}
