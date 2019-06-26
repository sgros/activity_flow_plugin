// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import android.widget.FrameLayout;
import android.view.View;
import android.view.ViewGroup;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Cells.EmojiReplacementCell;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Emoji;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import java.util.Arrays;
import org.telegram.messenger.ImageLocation;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;
import android.content.Context;
import org.telegram.messenger.DataQuery;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Components.RecyclerListView;

public class StickersAdapter extends SelectionAdapter implements NotificationCenterDelegate
{
    private int currentAccount;
    private boolean delayLocalResults;
    private StickersAdapterDelegate delegate;
    private ArrayList<DataQuery.KeywordResult> keywordResults;
    private int lastReqId;
    private String[] lastSearchKeyboardLanguage;
    private String lastSticker;
    private Context mContext;
    private Runnable searchRunnable;
    private ArrayList<TLRPC.Document> stickers;
    private HashMap<String, TLRPC.Document> stickersMap;
    private ArrayList<Object> stickersParents;
    private ArrayList<String> stickersToLoad;
    private boolean visible;
    
    public StickersAdapter(final Context mContext, final StickersAdapterDelegate delegate) {
        this.currentAccount = UserConfig.selectedAccount;
        this.stickersToLoad = new ArrayList<String>();
        this.mContext = mContext;
        this.delegate = delegate;
        DataQuery.getInstance(this.currentAccount).checkStickers(0);
        DataQuery.getInstance(this.currentAccount).checkStickers(1);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
    }
    
    private void addStickerToResult(final TLRPC.Document document, final Object e) {
        if (document == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(document.dc_id);
        sb.append("_");
        sb.append(document.id);
        final String string = sb.toString();
        final HashMap<String, TLRPC.Document> stickersMap = this.stickersMap;
        if (stickersMap != null && stickersMap.containsKey(string)) {
            return;
        }
        if (this.stickers == null) {
            this.stickers = new ArrayList<TLRPC.Document>();
            this.stickersParents = new ArrayList<Object>();
            this.stickersMap = new HashMap<String, TLRPC.Document>();
        }
        this.stickers.add(document);
        this.stickersParents.add(e);
        this.stickersMap.put(string, document);
    }
    
    private void addStickersToResult(final ArrayList<TLRPC.Document> list, final Object e) {
        if (list != null) {
            if (!list.isEmpty()) {
            Label_0256:
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final TLRPC.Document document = list.get(i);
                    final StringBuilder sb = new StringBuilder();
                    sb.append(document.dc_id);
                    sb.append("_");
                    sb.append(document.id);
                    final String string = sb.toString();
                    final HashMap<String, TLRPC.Document> stickersMap = this.stickersMap;
                    if (stickersMap == null || !stickersMap.containsKey(string)) {
                        if (this.stickers == null) {
                            this.stickers = new ArrayList<TLRPC.Document>();
                            this.stickersParents = new ArrayList<Object>();
                            this.stickersMap = new HashMap<String, TLRPC.Document>();
                        }
                        this.stickers.add(document);
                        while (true) {
                            for (int size2 = document.attributes.size(), j = 0; j < size2; ++j) {
                                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(j);
                                if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                                    this.stickersParents.add(documentAttribute.stickerset);
                                    final boolean b = true;
                                    if (!b) {
                                        this.stickersParents.add(e);
                                    }
                                    this.stickersMap.put(string, document);
                                    continue Label_0256;
                                }
                            }
                            final boolean b = false;
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    private void cancelEmojiSearch() {
        final Runnable searchRunnable = this.searchRunnable;
        if (searchRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(searchRunnable);
            this.searchRunnable = null;
        }
    }
    
    private boolean checkStickerFilesExistAndDownload() {
        final ArrayList<TLRPC.Document> stickers = this.stickers;
        int i = 0;
        if (stickers == null) {
            return false;
        }
        this.stickersToLoad.clear();
        while (i < Math.min(6, this.stickers.size())) {
            final TLRPC.Document document = this.stickers.get(i);
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
            if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize && !FileLoader.getPathToAttach(closestPhotoSizeWithSize, "webp", true).exists()) {
                this.stickersToLoad.add(FileLoader.getAttachFileName(closestPhotoSizeWithSize, "webp"));
                FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForDocument(closestPhotoSizeWithSize, document), this.stickersParents.get(i), "webp", 1, 1);
            }
            ++i;
        }
        return this.stickersToLoad.isEmpty();
    }
    
    private boolean isValidSticker(final TLRPC.Document document, final String s) {
        final int size = document.attributes.size();
        int i = 0;
        while (i < size) {
            final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                final String alt = documentAttribute.alt;
                if (alt != null && alt.contains(s)) {
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    private void searchEmojiByKeyword() {
        final String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
        if (!Arrays.equals(currentKeyboardLanguage, this.lastSearchKeyboardLanguage)) {
            DataQuery.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
        }
        this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
        final String lastSticker = this.lastSticker;
        this.cancelEmojiSearch();
        this.searchRunnable = new _$$Lambda$StickersAdapter$D58rW58m_jlo8ZaeUedvcgRz2Fw(this, lastSticker);
        final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
        if (keywordResults != null && !keywordResults.isEmpty()) {
            this.searchRunnable.run();
        }
        else {
            AndroidUtilities.runOnUIThread(this.searchRunnable, 1000L);
        }
    }
    
    private void searchServerStickers(final String s, final String emoticon) {
        final TLRPC.TL_messages_getStickers tl_messages_getStickers = new TLRPC.TL_messages_getStickers();
        tl_messages_getStickers.emoticon = emoticon;
        tl_messages_getStickers.hash = 0;
        this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getStickers, new _$$Lambda$StickersAdapter$XAmkAEU9b5B6bSCEO_rxuKc1F8U(this, s));
    }
    
    public void clearStickers() {
        if (!this.delayLocalResults) {
            if (this.lastReqId == 0) {
                this.lastSticker = null;
                this.stickers = null;
                this.stickersParents = null;
                this.stickersMap = null;
                this.keywordResults = null;
                this.stickersToLoad.clear();
                this.notifyDataSetChanged();
                if (this.lastReqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
                    this.lastReqId = 0;
                }
            }
        }
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n != NotificationCenter.fileDidLoad && n != NotificationCenter.fileDidFailedLoad) {
            if (n == NotificationCenter.newEmojiSuggestionsAvailable) {
                final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
                if ((keywordResults == null || keywordResults.isEmpty()) && !TextUtils.isEmpty((CharSequence)this.lastSticker) && this.getItemCount() == 0) {
                    this.searchEmojiByKeyword();
                }
            }
        }
        else {
            final ArrayList<TLRPC.Document> stickers = this.stickers;
            if (stickers != null && !stickers.isEmpty() && !this.stickersToLoad.isEmpty() && this.visible) {
                final boolean b = false;
                this.stickersToLoad.remove(array[0]);
                if (this.stickersToLoad.isEmpty()) {
                    final ArrayList<TLRPC.Document> stickers2 = this.stickers;
                    boolean b2 = b;
                    if (stickers2 != null) {
                        b2 = b;
                        if (!stickers2.isEmpty()) {
                            b2 = b;
                            if (this.stickersToLoad.isEmpty()) {
                                b2 = true;
                            }
                        }
                    }
                    if (b2) {
                        this.keywordResults = null;
                    }
                    this.delegate.needChangePanelVisibility(b2);
                }
            }
        }
    }
    
    public Object getItem(final int n) {
        final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
        if (keywordResults != null && !keywordResults.isEmpty()) {
            return this.keywordResults.get(n).emoji;
        }
        final ArrayList<TLRPC.Document> stickers = this.stickers;
        Object value;
        if (stickers != null && n >= 0 && n < stickers.size()) {
            value = this.stickers.get(n);
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Override
    public int getItemCount() {
        final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
        if (keywordResults != null && !keywordResults.isEmpty()) {
            return this.keywordResults.size();
        }
        if (!this.delayLocalResults) {
            final ArrayList<TLRPC.Document> stickers = this.stickers;
            if (stickers != null) {
                return stickers.size();
            }
        }
        return 0;
    }
    
    public Object getItemParent(final int index) {
        final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
        final Object o = null;
        if (keywordResults != null && !keywordResults.isEmpty()) {
            return null;
        }
        final ArrayList<Object> stickersParents = this.stickersParents;
        Object value = o;
        if (stickersParents != null) {
            value = o;
            if (index >= 0) {
                value = o;
                if (index < stickersParents.size()) {
                    value = this.stickersParents.get(index);
                }
            }
        }
        return value;
    }
    
    @Override
    public int getItemViewType(final int n) {
        final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
        if (keywordResults != null && !keywordResults.isEmpty()) {
            return 1;
        }
        return 0;
    }
    
    public void hide() {
        if (this.visible) {
            if (this.stickers == null) {
                final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
                if (keywordResults == null || keywordResults.isEmpty()) {
                    return;
                }
            }
            this.visible = false;
            this.delegate.needChangePanelVisibility(false);
        }
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        return false;
    }
    
    public boolean isShowingKeywords() {
        final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
        return keywordResults != null && !keywordResults.isEmpty();
    }
    
    public void loadStikersForEmoji(CharSequence charSequence, final boolean b) {
        final boolean b2 = false;
        final boolean b3 = charSequence != null && charSequence.length() > 0 && charSequence.length() <= 14;
        final String string = charSequence.toString();
        int n3;
        CharSequence charSequence2;
        int n4;
        for (int length = charSequence.length(), i = 0; i < length; i = n4 + 1, length = n3, charSequence = charSequence2) {
            Label_0278: {
                Label_0211: {
                    if (i < length - 1) {
                        Label_0166: {
                            if (charSequence.charAt(i) == '\ud83c') {
                                final int n = i + 1;
                                if (charSequence.charAt(n) >= '\udffb' && charSequence.charAt(n) <= '\udfff') {
                                    break Label_0166;
                                }
                            }
                            if (charSequence.charAt(i) != '\u200d') {
                                break Label_0211;
                            }
                            final int n2 = i + 1;
                            if (charSequence.charAt(n2) != '\u2640' && charSequence.charAt(n2) != '\u2642') {
                                break Label_0211;
                            }
                        }
                        charSequence = TextUtils.concat(new CharSequence[] { charSequence.subSequence(0, i), charSequence.subSequence(i + 2, charSequence.length()) });
                        length -= 2;
                        break Label_0278;
                    }
                }
                n3 = length;
                charSequence2 = charSequence;
                n4 = i;
                if (charSequence.charAt(i) != '\ufe0f') {
                    continue;
                }
                charSequence = TextUtils.concat(new CharSequence[] { charSequence.subSequence(0, i), charSequence.subSequence(i + 1, charSequence.length()) });
                --length;
            }
            n4 = i - 1;
            charSequence2 = charSequence;
            n3 = length;
        }
        this.lastSticker = charSequence.toString();
        final boolean b4 = b3 && (Emoji.isValidEmoji(string) || Emoji.isValidEmoji(this.lastSticker));
        if (!b && SharedConfig.suggestStickers != 2 && b4) {
            this.cancelEmojiSearch();
            this.stickers = null;
            this.stickersParents = null;
            this.stickersMap = null;
            if (this.lastReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
                this.lastReqId = 0;
            }
            this.delayLocalResults = false;
            final ArrayList<TLRPC.Document> recentStickersNoCopy = DataQuery.getInstance(this.currentAccount).getRecentStickersNoCopy(0);
            final ArrayList<TLRPC.Document> recentStickersNoCopy2 = DataQuery.getInstance(this.currentAccount).getRecentStickersNoCopy(2);
            final int size = recentStickersNoCopy.size();
            int j = 0;
            int n5 = 0;
            while (j < size) {
                final TLRPC.Document document = recentStickersNoCopy.get(j);
                int n6 = n5;
                if (this.isValidSticker(document, this.lastSticker)) {
                    this.addStickerToResult(document, "recent");
                    n6 = ++n5;
                    if (n5 >= 5) {
                        break;
                    }
                }
                ++j;
                n5 = n6;
            }
            for (int size2 = recentStickersNoCopy2.size(), k = 0; k < size2; ++k) {
                final TLRPC.Document document2 = recentStickersNoCopy2.get(k);
                if (this.isValidSticker(document2, this.lastSticker)) {
                    this.addStickerToResult(document2, "fav");
                }
            }
            final HashMap<String, ArrayList<TLRPC.Document>> allStickers = DataQuery.getInstance(this.currentAccount).getAllStickers();
            Object c;
            if (allStickers != null) {
                c = allStickers.get(this.lastSticker);
            }
            else {
                c = null;
            }
            if (c != null && !((ArrayList)c).isEmpty()) {
                final ArrayList list = new ArrayList<Object>((Collection<?>)c);
                if (!recentStickersNoCopy.isEmpty()) {
                    Collections.sort((List<E>)list, (Comparator<? super E>)new Comparator<TLRPC.Document>() {
                        private int getIndex(final long n) {
                            final int n2 = 0;
                            int index = 0;
                            int i;
                            while (true) {
                                i = n2;
                                if (index >= recentStickersNoCopy2.size()) {
                                    break;
                                }
                                if (((TLRPC.Document)recentStickersNoCopy2.get(index)).id == n) {
                                    return index + 1000;
                                }
                                ++index;
                            }
                            while (i < recentStickersNoCopy.size()) {
                                if (((TLRPC.Document)recentStickersNoCopy.get(i)).id == n) {
                                    return i;
                                }
                                ++i;
                            }
                            return -1;
                        }
                        
                        @Override
                        public int compare(final TLRPC.Document document, final TLRPC.Document document2) {
                            final int index = this.getIndex(document.id);
                            final int index2 = this.getIndex(document2.id);
                            if (index > index2) {
                                return -1;
                            }
                            if (index < index2) {
                                return 1;
                            }
                            return 0;
                        }
                    });
                }
                this.addStickersToResult((ArrayList<TLRPC.Document>)list, null);
            }
            if (SharedConfig.suggestStickers == 0) {
                this.searchServerStickers(this.lastSticker, string);
            }
            final ArrayList<TLRPC.Document> stickers = this.stickers;
            if (stickers != null && !stickers.isEmpty()) {
                if (SharedConfig.suggestStickers == 0 && this.stickers.size() < 5) {
                    this.delayLocalResults = true;
                    this.delegate.needChangePanelVisibility(false);
                    this.visible = false;
                }
                else {
                    this.checkStickerFilesExistAndDownload();
                    final ArrayList<TLRPC.Document> stickers2 = this.stickers;
                    boolean b5 = b2;
                    if (stickers2 != null) {
                        b5 = b2;
                        if (!stickers2.isEmpty()) {
                            b5 = b2;
                            if (this.stickersToLoad.isEmpty()) {
                                b5 = true;
                            }
                        }
                    }
                    if (b5) {
                        this.keywordResults = null;
                    }
                    this.delegate.needChangePanelVisibility(b5);
                    this.visible = true;
                }
                this.notifyDataSetChanged();
            }
            else if (this.visible) {
                this.delegate.needChangePanelVisibility(false);
                this.visible = false;
            }
            return;
        }
        if (this.visible) {
            final ArrayList<DataQuery.KeywordResult> keywordResults = this.keywordResults;
            if (keywordResults == null || keywordResults.isEmpty()) {
                this.visible = false;
                this.delegate.needChangePanelVisibility(false);
                this.notifyDataSetChanged();
            }
        }
        if (!b4) {
            this.searchEmojiByKeyword();
        }
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        final int itemViewType = viewHolder.getItemViewType();
        int n = 2;
        if (itemViewType != 0) {
            if (itemViewType == 1) {
                if (index == 0) {
                    if (this.keywordResults.size() != 1) {
                        n = -1;
                    }
                }
                else if (index == this.keywordResults.size() - 1) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                ((EmojiReplacementCell)viewHolder.itemView).setEmoji(this.keywordResults.get(index).emoji, n);
            }
        }
        else {
            if (index == 0) {
                if (this.stickers.size() != 1) {
                    n = -1;
                }
            }
            else if (index == this.stickers.size() - 1) {
                n = 1;
            }
            else {
                n = 0;
            }
            final StickerCell stickerCell = (StickerCell)viewHolder.itemView;
            stickerCell.setSticker(this.stickers.get(index), this.stickersParents.get(index), n);
            stickerCell.setClearsInputField(true);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        FrameLayout frameLayout;
        if (n != 0) {
            frameLayout = new EmojiReplacementCell(this.mContext);
        }
        else {
            frameLayout = new StickerCell(this.mContext);
        }
        return new RecyclerListView.Holder((View)frameLayout);
    }
    
    public void onDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileDidFailedLoad);
    }
    
    public interface StickersAdapterDelegate
    {
        void needChangePanelVisibility(final boolean p0);
    }
}
