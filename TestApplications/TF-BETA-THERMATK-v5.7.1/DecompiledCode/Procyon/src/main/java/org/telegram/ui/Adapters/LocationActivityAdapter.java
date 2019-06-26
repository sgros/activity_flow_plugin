// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import org.telegram.messenger.AndroidUtilities;
import java.util.Collection;
import android.widget.FrameLayout;
import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Cells.LocationPoweredCell;
import android.view.ViewGroup;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Cells.LocationLoadingCell;
import org.telegram.ui.Cells.SharingLiveLocationCell;
import org.telegram.messenger.LocationController;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Locale;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Cells.SendLocationCell;
import android.content.Context;
import android.location.Location;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.LocationActivity;
import java.util.ArrayList;

public class LocationActivityAdapter extends BaseLocationAdapter
{
    private int currentAccount;
    private ArrayList<LocationActivity.LiveLocation> currentLiveLocations;
    private MessageObject currentMessageObject;
    private Location customLocation;
    private long dialogId;
    private Location gpsLocation;
    private int liveLocationType;
    private Context mContext;
    private int overScrollHeight;
    private boolean pulledUp;
    private SendLocationCell sendLocationCell;
    private int shareLiveLocationPotistion;
    
    public LocationActivityAdapter(final Context mContext, final int liveLocationType, final long dialogId) {
        this.currentAccount = UserConfig.selectedAccount;
        this.shareLiveLocationPotistion = -1;
        this.currentLiveLocations = new ArrayList<LocationActivity.LiveLocation>();
        this.mContext = mContext;
        this.liveLocationType = liveLocationType;
        this.dialogId = dialogId;
    }
    
    private void updateCell() {
        final SendLocationCell sendLocationCell = this.sendLocationCell;
        if (sendLocationCell != null) {
            if (this.customLocation != null) {
                sendLocationCell.setText(LocaleController.getString("SendSelectedLocation", 2131560706), String.format(Locale.US, "(%f,%f)", this.customLocation.getLatitude(), this.customLocation.getLongitude()));
            }
            else if (this.gpsLocation != null) {
                sendLocationCell.setText(LocaleController.getString("SendLocation", 2131560700), LocaleController.formatString("AccurateTo", 2131558490, LocaleController.formatPluralString("Meters", (int)this.gpsLocation.getAccuracy())));
            }
            else {
                sendLocationCell.setText(LocaleController.getString("SendLocation", 2131560700), LocaleController.getString("Loading", 2131559768));
            }
        }
    }
    
    public Object getItem(final int n) {
        final MessageObject currentMessageObject = this.currentMessageObject;
        if (currentMessageObject != null) {
            if (n == 1) {
                return currentMessageObject;
            }
            if (n > 3 && n < super.places.size() + 3) {
                return this.currentLiveLocations.get(n - 4);
            }
        }
        else {
            final int liveLocationType = this.liveLocationType;
            if (liveLocationType == 2) {
                if (n >= 2) {
                    return this.currentLiveLocations.get(n - 2);
                }
                return null;
            }
            else if (liveLocationType == 1) {
                if (n > 3 && n < super.places.size() + 4) {
                    return super.places.get(n - 4);
                }
            }
            else if (n > 2 && n < super.places.size() + 3) {
                return super.places.get(n - 3);
            }
        }
        return null;
    }
    
    @Override
    public int getItemCount() {
        if (this.currentMessageObject != null) {
            int n;
            if (this.currentLiveLocations.isEmpty()) {
                n = 0;
            }
            else {
                n = this.currentLiveLocations.size() + 2;
            }
            return n + 2;
        }
        if (this.liveLocationType == 2) {
            return this.currentLiveLocations.size() + 2;
        }
        final boolean searching = super.searching;
        int n2 = 4;
        if (searching || (!searching && super.places.isEmpty())) {
            if (this.liveLocationType != 0) {
                n2 = 5;
            }
            return n2;
        }
        if (this.liveLocationType == 1) {
            return super.places.size() + 4 + ((super.places.isEmpty() ^ true) ? 1 : 0);
        }
        return super.places.size() + 3 + ((super.places.isEmpty() ^ true) ? 1 : 0);
    }
    
    @Override
    public int getItemViewType(final int shareLiveLocationPotistion) {
        if (shareLiveLocationPotistion == 0) {
            return 0;
        }
        if (this.currentMessageObject != null) {
            if (shareLiveLocationPotistion == 2) {
                return 2;
            }
            if (shareLiveLocationPotistion == 3) {
                this.shareLiveLocationPotistion = shareLiveLocationPotistion;
                return 6;
            }
            return 7;
        }
        else {
            final int liveLocationType = this.liveLocationType;
            if (liveLocationType != 2) {
                if (liveLocationType == 1) {
                    if (shareLiveLocationPotistion == 1) {
                        return 1;
                    }
                    if (shareLiveLocationPotistion == 2) {
                        this.shareLiveLocationPotistion = shareLiveLocationPotistion;
                        return 6;
                    }
                    if (shareLiveLocationPotistion == 3) {
                        return 2;
                    }
                    final boolean searching = super.searching;
                    if (searching || (!searching && super.places.isEmpty())) {
                        return 4;
                    }
                    if (shareLiveLocationPotistion == super.places.size() + 4) {
                        return 5;
                    }
                }
                else {
                    if (shareLiveLocationPotistion == 1) {
                        return 1;
                    }
                    if (shareLiveLocationPotistion == 2) {
                        return 2;
                    }
                    final boolean searching2 = super.searching;
                    if (searching2 || (!searching2 && super.places.isEmpty())) {
                        return 4;
                    }
                    if (shareLiveLocationPotistion == super.places.size() + 3) {
                        return 5;
                    }
                }
                return 3;
            }
            if (shareLiveLocationPotistion == 1) {
                this.shareLiveLocationPotistion = shareLiveLocationPotistion;
                return 6;
            }
            return 7;
        }
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        final int itemViewType = viewHolder.getItemViewType();
        final boolean b = false;
        boolean b2 = false;
        if (itemViewType == 6) {
            if (LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId) != null || this.gpsLocation != null) {
                b2 = true;
            }
            return b2;
        }
        if (itemViewType != 1 && itemViewType != 3) {
            final boolean b3 = b;
            if (itemViewType != 7) {
                return b3;
            }
        }
        return true;
    }
    
    public boolean isPulledUp() {
        return this.pulledUp;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int n) {
        final int itemViewType = viewHolder.getItemViewType();
        if (itemViewType != 0) {
            boolean hasLocation = true;
            if (itemViewType != 1) {
                int n2 = 2;
                if (itemViewType != 2) {
                    if (itemViewType != 3) {
                        if (itemViewType != 4) {
                            if (itemViewType != 6) {
                                if (itemViewType == 7) {
                                    final MessageObject currentMessageObject = this.currentMessageObject;
                                    if (currentMessageObject != null && n == 1) {
                                        ((SharingLiveLocationCell)viewHolder.itemView).setDialog(currentMessageObject, this.gpsLocation);
                                    }
                                    else {
                                        final SharingLiveLocationCell sharingLiveLocationCell = (SharingLiveLocationCell)viewHolder.itemView;
                                        final ArrayList<LocationActivity.LiveLocation> currentLiveLocations = this.currentLiveLocations;
                                        if (this.currentMessageObject != null) {
                                            n2 = 4;
                                        }
                                        sharingLiveLocationCell.setDialog(currentLiveLocations.get(n - n2), this.gpsLocation);
                                    }
                                }
                            }
                            else {
                                final SendLocationCell sendLocationCell = (SendLocationCell)viewHolder.itemView;
                                if (this.gpsLocation == null) {
                                    hasLocation = false;
                                }
                                sendLocationCell.setHasLocation(hasLocation);
                            }
                        }
                        else {
                            ((LocationLoadingCell)viewHolder.itemView).setLoading(super.searching);
                        }
                    }
                    else if (this.liveLocationType == 0) {
                        final LocationCell locationCell = (LocationCell)viewHolder.itemView;
                        final ArrayList<TLRPC.TL_messageMediaVenue> places = (ArrayList<TLRPC.TL_messageMediaVenue>)super.places;
                        n -= 3;
                        locationCell.setLocation(places.get(n), super.iconUrls.get(n), true);
                    }
                    else {
                        final LocationCell locationCell2 = (LocationCell)viewHolder.itemView;
                        final ArrayList<TLRPC.TL_messageMediaVenue> places2 = (ArrayList<TLRPC.TL_messageMediaVenue>)super.places;
                        n -= 4;
                        locationCell2.setLocation(places2.get(n), super.iconUrls.get(n), true);
                    }
                }
                else if (this.currentMessageObject != null) {
                    ((GraySectionCell)viewHolder.itemView).setText(LocaleController.getString("LiveLocations", 2131559767));
                }
                else if (this.pulledUp) {
                    ((GraySectionCell)viewHolder.itemView).setText(LocaleController.getString("NearbyPlaces", 2131559888));
                }
                else {
                    ((GraySectionCell)viewHolder.itemView).setText(LocaleController.getString("ShowNearbyPlaces", 2131560781));
                }
            }
            else {
                this.sendLocationCell = (SendLocationCell)viewHolder.itemView;
                this.updateCell();
            }
        }
        else {
            ((EmptyCell)viewHolder.itemView).setHeight(this.overScrollHeight);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        FrameLayout frameLayout = null;
        switch (n) {
            default: {
                frameLayout = new SharingLiveLocationCell(this.mContext, true);
                break;
            }
            case 6: {
                frameLayout = new SendLocationCell(this.mContext, true);
                ((SendLocationCell)frameLayout).setDialogId(this.dialogId);
                break;
            }
            case 5: {
                frameLayout = new LocationPoweredCell(this.mContext);
                break;
            }
            case 4: {
                frameLayout = new LocationLoadingCell(this.mContext);
                break;
            }
            case 3: {
                frameLayout = new LocationCell(this.mContext);
                break;
            }
            case 2: {
                frameLayout = new GraySectionCell(this.mContext);
                break;
            }
            case 1: {
                frameLayout = new SendLocationCell(this.mContext, false);
                break;
            }
            case 0: {
                frameLayout = new EmptyCell(this.mContext);
                break;
            }
        }
        return new RecyclerListView.Holder((View)frameLayout);
    }
    
    public void setCustomLocation(final Location customLocation) {
        this.customLocation = customLocation;
        this.updateCell();
    }
    
    public void setGpsLocation(final Location gpsLocation) {
        final boolean b = this.gpsLocation == null;
        this.gpsLocation = gpsLocation;
        if (b) {
            final int shareLiveLocationPotistion = this.shareLiveLocationPotistion;
            if (shareLiveLocationPotistion > 0) {
                this.notifyItemChanged(shareLiveLocationPotistion);
            }
        }
        if (this.currentMessageObject != null) {
            this.notifyItemChanged(1);
            this.updateLiveLocations();
        }
        else if (this.liveLocationType != 2) {
            this.updateCell();
        }
        else {
            this.updateLiveLocations();
        }
    }
    
    public void setLiveLocations(final ArrayList<LocationActivity.LiveLocation> c) {
        this.currentLiveLocations = new ArrayList<LocationActivity.LiveLocation>(c);
        final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        for (int i = 0; i < this.currentLiveLocations.size(); ++i) {
            if (this.currentLiveLocations.get(i).id == clientUserId) {
                this.currentLiveLocations.remove(i);
                break;
            }
        }
        this.notifyDataSetChanged();
    }
    
    public void setMessageObject(final MessageObject currentMessageObject) {
        this.currentMessageObject = currentMessageObject;
        this.notifyDataSetChanged();
    }
    
    public void setOverScrollHeight(final int overScrollHeight) {
        this.overScrollHeight = overScrollHeight;
    }
    
    public void setPulledUp() {
        if (this.pulledUp) {
            return;
        }
        this.pulledUp = true;
        AndroidUtilities.runOnUIThread(new _$$Lambda$LocationActivityAdapter$kPn2n4u9DAIYsBitfbC_1mB05AA(this));
    }
    
    public void updateLiveLocations() {
        if (!this.currentLiveLocations.isEmpty()) {
            this.notifyItemRangeChanged(2, this.currentLiveLocations.size());
        }
    }
}
