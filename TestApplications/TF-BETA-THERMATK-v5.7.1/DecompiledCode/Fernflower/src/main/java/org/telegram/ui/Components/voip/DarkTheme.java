package org.telegram.ui.Components.voip;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;

public class DarkTheme {
   public static int getColor(String var0) {
      short var1;
      label1058: {
         switch(var0.hashCode()) {
         case -2147269658:
            if (var0.equals("chat_outMenu")) {
               var1 = 23;
               break label1058;
            }
            break;
         case -2139469579:
            if (var0.equals("chat_emojiPanelEmptyText")) {
               var1 = 99;
               break label1058;
            }
            break;
         case -2132427577:
            if (var0.equals("chat_outViews")) {
               var1 = 40;
               break label1058;
            }
            break;
         case -2103805301:
            if (var0.equals("actionBarActionModeDefault")) {
               var1 = 24;
               break label1058;
            }
            break;
         case -2102232027:
            if (var0.equals("profile_actionIcon")) {
               var1 = 150;
               break label1058;
            }
            break;
         case -2019587427:
            if (var0.equals("listSelector")) {
               var1 = 101;
               break label1058;
            }
            break;
         case -1992864503:
            if (var0.equals("actionBarDefaultSubmenuBackground")) {
               var1 = 88;
               break label1058;
            }
            break;
         case -1992639563:
            if (var0.equals("avatar_actionBarSelectorViolet")) {
               var1 = 195;
               break label1058;
            }
            break;
         case -1975862704:
            if (var0.equals("player_button")) {
               var1 = 33;
               break label1058;
            }
            break;
         case -1974166005:
            if (var0.equals("chat_outFileProgressSelected")) {
               var1 = 30;
               break label1058;
            }
            break;
         case -1961633574:
            if (var0.equals("chat_outLoader")) {
               var1 = 179;
               break label1058;
            }
            break;
         case -1942198229:
            if (var0.equals("chats_menuPhone")) {
               var1 = 196;
               break label1058;
            }
            break;
         case -1927175348:
            if (var0.equals("chat_outFileBackgroundSelected")) {
               var1 = 208;
               break label1058;
            }
            break;
         case -1926854985:
            if (var0.equals("windowBackgroundWhiteGrayText2")) {
               var1 = 148;
               break label1058;
            }
            break;
         case -1926854984:
            if (var0.equals("windowBackgroundWhiteGrayText3")) {
               var1 = 87;
               break label1058;
            }
            break;
         case -1926854983:
            if (var0.equals("windowBackgroundWhiteGrayText4")) {
               var1 = 159;
               break label1058;
            }
            break;
         case -1924841028:
            if (var0.equals("actionBarDefaultSubtitle")) {
               var1 = 105;
               break label1058;
            }
            break;
         case -1891930735:
            if (var0.equals("chat_outFileBackground")) {
               var1 = 162;
               break label1058;
            }
            break;
         case -1878988531:
            if (var0.equals("avatar_actionBarSelectorGreen")) {
               var1 = 108;
               break label1058;
            }
            break;
         case -1853661732:
            if (var0.equals("chat_outTimeSelectedText")) {
               var1 = 90;
               break label1058;
            }
            break;
         case -1850167367:
            if (var0.equals("chat_emojiPanelShadowLine")) {
               var1 = 25;
               break label1058;
            }
            break;
         case -1849805674:
            if (var0.equals("dialogBackground")) {
               var1 = 26;
               break label1058;
            }
            break;
         case -1787129273:
            if (var0.equals("chat_outContactBackground")) {
               var1 = 211;
               break label1058;
            }
            break;
         case -1779173263:
            if (var0.equals("chat_topPanelMessage")) {
               var1 = 132;
               break label1058;
            }
            break;
         case -1777297962:
            if (var0.equals("chats_muteIcon")) {
               var1 = 244;
               break label1058;
            }
            break;
         case -1767675171:
            if (var0.equals("chat_inViaBotNameText")) {
               var1 = 156;
               break label1058;
            }
            break;
         case -1758608141:
            if (var0.equals("windowBackgroundWhiteValueText")) {
               var1 = 232;
               break label1058;
            }
            break;
         case -1733632792:
            if (var0.equals("emptyListPlaceholder")) {
               var1 = 241;
               break label1058;
            }
            break;
         case -1724033454:
            if (var0.equals("chat_inPreviewInstantText")) {
               var1 = 27;
               break label1058;
            }
            break;
         case -1719903102:
            if (var0.equals("chat_outViewsSelected")) {
               var1 = 145;
               break label1058;
            }
            break;
         case -1719839798:
            if (var0.equals("avatar_backgroundInProfileBlue")) {
               var1 = 189;
               break label1058;
            }
            break;
         case -1683744660:
            if (var0.equals("profile_verifiedBackground")) {
               var1 = 161;
               break label1058;
            }
            break;
         case -1654302575:
            if (var0.equals("chats_menuBackground")) {
               var1 = 173;
               break label1058;
            }
            break;
         case -1633591792:
            if (var0.equals("chat_emojiPanelStickerPackSelector")) {
               var1 = 225;
               break label1058;
            }
            break;
         case -1625862693:
            if (var0.equals("chat_wallpaper")) {
               var1 = 224;
               break label1058;
            }
            break;
         case -1623818608:
            if (var0.equals("chat_inForwardedNameText")) {
               var1 = 165;
               break label1058;
            }
            break;
         case -1604008580:
            if (var0.equals("chat_outAudioProgress")) {
               var1 = 54;
               break label1058;
            }
            break;
         case -1589702002:
            if (var0.equals("chat_inLoaderPhotoSelected")) {
               var1 = 60;
               break label1058;
            }
            break;
         case -1565843249:
            if (var0.equals("files_folderIcon")) {
               var1 = 139;
               break label1058;
            }
            break;
         case -1543133775:
            if (var0.equals("chat_outContactNameText")) {
               var1 = 222;
               break label1058;
            }
            break;
         case -1542353776:
            if (var0.equals("chat_outVoiceSeekbar")) {
               var1 = 133;
               break label1058;
            }
            break;
         case -1533503664:
            if (var0.equals("chat_outFileProgress")) {
               var1 = 124;
               break label1058;
            }
            break;
         case -1530345450:
            if (var0.equals("chat_inReplyMessageText")) {
               var1 = 107;
               break label1058;
            }
            break;
         case -1496224782:
            if (var0.equals("chat_inReplyLine")) {
               var1 = 104;
               break label1058;
            }
            break;
         case -1415980195:
            if (var0.equals("files_folderIconBackground")) {
               var1 = 160;
               break label1058;
            }
            break;
         case -1407570354:
            if (var0.equals("chat_inReplyMediaMessageText")) {
               var1 = 176;
               break label1058;
            }
            break;
         case -1397026623:
            if (var0.equals("windowBackgroundGray")) {
               var1 = 9;
               break label1058;
            }
            break;
         case -1385379359:
            if (var0.equals("dialogIcon")) {
               var1 = 93;
               break label1058;
            }
            break;
         case -1316415606:
            if (var0.equals("actionBarActionModeDefaultSelector")) {
               var1 = 5;
               break label1058;
            }
            break;
         case -1310183623:
            if (var0.equals("chat_muteIcon")) {
               var1 = 20;
               break label1058;
            }
            break;
         case -1262649070:
            if (var0.equals("avatar_nameInMessageGreen")) {
               var1 = 66;
               break label1058;
            }
            break;
         case -1240647597:
            if (var0.equals("chat_outBubbleShadow")) {
               var1 = 55;
               break label1058;
            }
            break;
         case -1229478359:
            if (var0.equals("chats_unreadCounter")) {
               var1 = 85;
               break label1058;
            }
            break;
         case -1213387098:
            if (var0.equals("chat_inMenuSelected")) {
               var1 = 56;
               break label1058;
            }
            break;
         case -1147596450:
            if (var0.equals("chat_inFileInfoSelectedText")) {
               var1 = 79;
               break label1058;
            }
            break;
         case -1106471792:
            if (var0.equals("chat_outAudioPerfomerText")) {
               var1 = 94;
               break label1058;
            }
            break;
         case -1078554766:
            if (var0.equals("windowBackgroundWhiteBlueHeader")) {
               var1 = 97;
               break label1058;
            }
            break;
         case -1074293766:
            if (var0.equals("avatar_backgroundActionBarGreen")) {
               var1 = 143;
               break label1058;
            }
            break;
         case -1063762099:
            if (var0.equals("windowBackgroundWhiteGreenText2")) {
               var1 = 10;
               break label1058;
            }
            break;
         case -1062379852:
            if (var0.equals("chat_messageLinkOut")) {
               var1 = 204;
               break label1058;
            }
            break;
         case -1046600742:
            if (var0.equals("profile_actionBackground")) {
               var1 = 64;
               break label1058;
            }
            break;
         case -1019316079:
            if (var0.equals("chat_outReplyMessageText")) {
               var1 = 210;
               break label1058;
            }
            break;
         case -1012016554:
            if (var0.equals("chat_inFileBackground")) {
               var1 = 3;
               break label1058;
            }
            break;
         case -1006953508:
            if (var0.equals("chat_secretTimerBackground")) {
               var1 = 1;
               break label1058;
            }
            break;
         case -1005376655:
            if (var0.equals("chat_inAudioSeekbar")) {
               var1 = 187;
               break label1058;
            }
            break;
         case -1005120019:
            if (var0.equals("chats_secretIcon")) {
               var1 = 92;
               break label1058;
            }
            break;
         case -1004973057:
            if (var0.equals("chats_secretName")) {
               var1 = 103;
               break label1058;
            }
            break;
         case -960321732:
            if (var0.equals("chat_mediaMenu")) {
               var1 = 21;
               break label1058;
            }
            break;
         case -955211830:
            if (var0.equals("chat_topPanelLine")) {
               var1 = 49;
               break label1058;
            }
            break;
         case -938826921:
            if (var0.equals("player_actionBarSubtitle")) {
               var1 = 223;
               break label1058;
            }
            break;
         case -901363160:
            if (var0.equals("chats_menuPhoneCats")) {
               var1 = 226;
               break label1058;
            }
            break;
         case -834035478:
            if (var0.equals("chat_outSentClockSelected")) {
               var1 = 65;
               break label1058;
            }
            break;
         case -810517465:
            if (var0.equals("chat_outAudioSeekbarSelected")) {
               var1 = 129;
               break label1058;
            }
            break;
         case -805096120:
            if (var0.equals("chats_nameIcon")) {
               var1 = 72;
               break label1058;
            }
            break;
         case -792942846:
            if (var0.equals("graySection")) {
               var1 = 71;
               break label1058;
            }
            break;
         case -779362418:
            if (var0.equals("chat_emojiPanelTrendingTitle")) {
               var1 = 77;
               break label1058;
            }
            break;
         case -763385518:
            if (var0.equals("chats_date")) {
               var1 = 123;
               break label1058;
            }
            break;
         case -763087825:
            if (var0.equals("chats_name")) {
               var1 = 168;
               break label1058;
            }
            break;
         case -756337980:
            if (var0.equals("profile_actionPressedBackground")) {
               var1 = 116;
               break label1058;
            }
            break;
         case -712338357:
            if (var0.equals("chat_inSiteNameText")) {
               var1 = 130;
               break label1058;
            }
            break;
         case -687452692:
            if (var0.equals("chat_inLoaderPhotoIcon")) {
               var1 = 193;
               break label1058;
            }
            break;
         case -654429213:
            if (var0.equals("chats_message")) {
               var1 = 238;
               break label1058;
            }
            break;
         case -652337344:
            if (var0.equals("chat_outVenueNameText")) {
               var1 = 240;
               break label1058;
            }
            break;
         case -629209323:
            if (var0.equals("chats_pinnedIcon")) {
               var1 = 18;
               break label1058;
            }
            break;
         case -608456434:
            if (var0.equals("chat_outBubbleSelected")) {
               var1 = 125;
               break label1058;
            }
            break;
         case -603597494:
            if (var0.equals("chat_inSentClock")) {
               var1 = 166;
               break label1058;
            }
            break;
         case -570274322:
            if (var0.equals("chat_outReplyMediaMessageSelectedText")) {
               var1 = 248;
               break label1058;
            }
            break;
         case -564899147:
            if (var0.equals("chat_outInstantSelected")) {
               var1 = 190;
               break label1058;
            }
            break;
         case -560721948:
            if (var0.equals("chat_outSentCheckSelected")) {
               var1 = 89;
               break label1058;
            }
            break;
         case -552118908:
            if (var0.equals("actionBarDefault")) {
               var1 = 113;
               break label1058;
            }
            break;
         case -493564645:
            if (var0.equals("avatar_actionBarSelectorRed")) {
               var1 = 32;
               break label1058;
            }
            break;
         case -450514995:
            if (var0.equals("chats_actionMessage")) {
               var1 = 42;
               break label1058;
            }
            break;
         case -427186938:
            if (var0.equals("chat_inAudioDurationSelectedText")) {
               var1 = 111;
               break label1058;
            }
            break;
         case -391617936:
            if (var0.equals("chat_selectedBackground")) {
               var1 = 17;
               break label1058;
            }
            break;
         case -354489314:
            if (var0.equals("chat_outFileInfoText")) {
               var1 = 51;
               break label1058;
            }
            break;
         case -343666293:
            if (var0.equals("windowBackgroundWhite")) {
               var1 = 171;
               break label1058;
            }
            break;
         case -294026410:
            if (var0.equals("chat_inReplyNameText")) {
               var1 = 69;
               break label1058;
            }
            break;
         case -264184037:
            if (var0.equals("inappPlayerClose")) {
               var1 = 206;
               break label1058;
            }
            break;
         case -260428237:
            if (var0.equals("chat_outVoiceSeekbarFill")) {
               var1 = 197;
               break label1058;
            }
            break;
         case -258492929:
            if (var0.equals("avatar_nameInMessageOrange")) {
               var1 = 158;
               break label1058;
            }
            break;
         case -251079667:
            if (var0.equals("chat_outPreviewInstantText")) {
               var1 = 76;
               break label1058;
            }
            break;
         case -249481380:
            if (var0.equals("listSelectorSDK21")) {
               var1 = 213;
               break label1058;
            }
            break;
         case -248568965:
            if (var0.equals("inappPlayerTitle")) {
               var1 = 229;
               break label1058;
            }
            break;
         case -212237793:
            if (var0.equals("player_actionBar")) {
               var1 = 50;
               break label1058;
            }
            break;
         case -185786131:
            if (var0.equals("chat_unreadMessagesStartText")) {
               var1 = 205;
               break label1058;
            }
            break;
         case -176488427:
            if (var0.equals("chat_replyPanelLine")) {
               var1 = 175;
               break label1058;
            }
            break;
         case -143547632:
            if (var0.equals("chat_inFileProgressSelected")) {
               var1 = 131;
               break label1058;
            }
            break;
         case -127673038:
            if (var0.equals("key_chats_menuTopShadow")) {
               var1 = 170;
               break label1058;
            }
            break;
         case -108292334:
            if (var0.equals("chats_menuTopShadow")) {
               var1 = 136;
               break label1058;
            }
            break;
         case -71280336:
            if (var0.equals("switchTrackChecked")) {
               var1 = 146;
               break label1058;
            }
            break;
         case -65607089:
            if (var0.equals("chats_menuItemIcon")) {
               var1 = 6;
               break label1058;
            }
            break;
         case -65277181:
            if (var0.equals("chats_menuItemText")) {
               var1 = 37;
               break label1058;
            }
            break;
         case -35597940:
            if (var0.equals("chat_inContactNameText")) {
               var1 = 218;
               break label1058;
            }
            break;
         case -18073397:
            if (var0.equals("chats_tabletSelectedOverlay")) {
               var1 = 36;
               break label1058;
            }
            break;
         case -12871922:
            if (var0.equals("chat_secretChatStatusText")) {
               var1 = 151;
               break label1058;
            }
            break;
         case 6289575:
            if (var0.equals("chat_inLoaderPhotoIconSelected")) {
               var1 = 216;
               break label1058;
            }
            break;
         case 27337780:
            if (var0.equals("chats_pinnedOverlay")) {
               var1 = 95;
               break label1058;
            }
            break;
         case 49148112:
            if (var0.equals("chat_inPreviewLine")) {
               var1 = 153;
               break label1058;
            }
            break;
         case 51359814:
            if (var0.equals("chat_replyPanelMessage")) {
               var1 = 75;
               break label1058;
            }
            break;
         case 57332012:
            if (var0.equals("chats_sentCheck")) {
               var1 = 200;
               break label1058;
            }
            break;
         case 57460786:
            if (var0.equals("chats_sentClock")) {
               var1 = 203;
               break label1058;
            }
            break;
         case 89466127:
            if (var0.equals("chat_outAudioSeekbarFill")) {
               var1 = 67;
               break label1058;
            }
            break;
         case 117743477:
            if (var0.equals("chat_outPreviewLine")) {
               var1 = 199;
               break label1058;
            }
            break;
         case 141076636:
            if (var0.equals("groupcreate_spanBackground")) {
               var1 = 43;
               break label1058;
            }
            break;
         case 141894978:
            if (var0.equals("windowBackgroundWhiteRedText5")) {
               var1 = 194;
               break label1058;
            }
            break;
         case 185438775:
            if (var0.equals("chat_outAudioSelectedProgress")) {
               var1 = 182;
               break label1058;
            }
            break;
         case 216441603:
            if (var0.equals("chat_goDownButton")) {
               var1 = 114;
               break label1058;
            }
            break;
         case 231486891:
            if (var0.equals("chat_inAudioPerfomerText")) {
               var1 = 82;
               break label1058;
            }
            break;
         case 243668262:
            if (var0.equals("chat_inTimeText")) {
               var1 = 7;
               break label1058;
            }
            break;
         case 257089712:
            if (var0.equals("chat_outAudioDurationText")) {
               var1 = 119;
               break label1058;
            }
            break;
         case 271457747:
            if (var0.equals("chat_inBubbleSelected")) {
               var1 = 122;
               break label1058;
            }
            break;
         case 303350244:
            if (var0.equals("chat_reportSpam")) {
               var1 = 227;
               break label1058;
            }
            break;
         case 316847509:
            if (var0.equals("chat_outLoaderSelected")) {
               var1 = 15;
               break label1058;
            }
            break;
         case 339397761:
            if (var0.equals("windowBackgroundWhiteBlackText")) {
               var1 = 246;
               break label1058;
            }
            break;
         case 371859081:
            if (var0.equals("chat_inReplyMediaMessageSelectedText")) {
               var1 = 140;
               break label1058;
            }
            break;
         case 415452907:
            if (var0.equals("chat_outAudioDurationSelectedText")) {
               var1 = 142;
               break label1058;
            }
            break;
         case 421601145:
            if (var0.equals("chat_emojiPanelIconSelected")) {
               var1 = 4;
               break label1058;
            }
            break;
         case 421601469:
            if (var0.equals("chat_emojiPanelIconSelector")) {
               var1 = 74;
               break label1058;
            }
            break;
         case 426061980:
            if (var0.equals("chat_serviceBackground")) {
               var1 = 147;
               break label1058;
            }
            break;
         case 429680544:
            if (var0.equals("avatar_subtitleInProfileBlue")) {
               var1 = 239;
               break label1058;
            }
            break;
         case 429722217:
            if (var0.equals("avatar_subtitleInProfileCyan")) {
               var1 = 63;
               break label1058;
            }
            break;
         case 430094524:
            if (var0.equals("avatar_subtitleInProfilePink")) {
               var1 = 0;
               break label1058;
            }
            break;
         case 435303214:
            if (var0.equals("actionBarDefaultSubmenuItem")) {
               var1 = 198;
               break label1058;
            }
            break;
         case 439976061:
            if (var0.equals("avatar_subtitleInProfileGreen")) {
               var1 = 228;
               break label1058;
            }
            break;
         case 444983522:
            if (var0.equals("chat_topPanelClose")) {
               var1 = 250;
               break label1058;
            }
            break;
         case 446162770:
            if (var0.equals("windowBackgroundWhiteBlueText")) {
               var1 = 247;
               break label1058;
            }
            break;
         case 460598594:
            if (var0.equals("chat_topPanelTitle")) {
               var1 = 219;
               break label1058;
            }
            break;
         case 484353662:
            if (var0.equals("chat_inVenueInfoText")) {
               var1 = 118;
               break label1058;
            }
            break;
         case 503923205:
            if (var0.equals("chat_inSentClockSelected")) {
               var1 = 183;
               break label1058;
            }
            break;
         case 527405547:
            if (var0.equals("inappPlayerBackground")) {
               var1 = 48;
               break label1058;
            }
            break;
         case 556028747:
            if (var0.equals("chat_outVoiceSeekbarSelected")) {
               var1 = 28;
               break label1058;
            }
            break;
         case 589961756:
            if (var0.equals("chat_goDownButtonIcon")) {
               var1 = 214;
               break label1058;
            }
            break;
         case 613458991:
            if (var0.equals("dialogTextLink")) {
               var1 = 164;
               break label1058;
            }
            break;
         case 626157205:
            if (var0.equals("chat_inVoiceSeekbar")) {
               var1 = 34;
               break label1058;
            }
            break;
         case 634019162:
            if (var0.equals("chat_emojiPanelBackspace")) {
               var1 = 11;
               break label1058;
            }
            break;
         case 635007317:
            if (var0.equals("chat_inFileProgress")) {
               var1 = 242;
               break label1058;
            }
            break;
         case 648238646:
            if (var0.equals("chat_outAudioTitleText")) {
               var1 = 59;
               break label1058;
            }
            break;
         case 655457041:
            if (var0.equals("chat_inFileBackgroundSelected")) {
               var1 = 234;
               break label1058;
            }
            break;
         case 676996437:
            if (var0.equals("chat_outLocationIcon")) {
               var1 = 81;
               break label1058;
            }
            break;
         case 716656587:
            if (var0.equals("avatar_backgroundGroupCreateSpanBlue")) {
               var1 = 8;
               break label1058;
            }
            break;
         case 732262561:
            if (var0.equals("chat_outTimeText")) {
               var1 = 258;
               break label1058;
            }
            break;
         case 759679774:
            if (var0.equals("chat_outVenueInfoSelectedText")) {
               var1 = 135;
               break label1058;
            }
            break;
         case 765296599:
            if (var0.equals("chat_outReplyLine")) {
               var1 = 254;
               break label1058;
            }
            break;
         case 803672502:
            if (var0.equals("chat_messagePanelIcons")) {
               var1 = 70;
               break label1058;
            }
            break;
         case 826015922:
            if (var0.equals("chat_emojiPanelTrendingDescription")) {
               var1 = 2;
               break label1058;
            }
            break;
         case 850854541:
            if (var0.equals("chat_inPreviewInstantSelectedText")) {
               var1 = 78;
               break label1058;
            }
            break;
         case 890367586:
            if (var0.equals("chat_inViews")) {
               var1 = 100;
               break label1058;
            }
            break;
         case 911091978:
            if (var0.equals("chat_outLocationBackground")) {
               var1 = 243;
               break label1058;
            }
            break;
         case 913069217:
            if (var0.equals("chat_outMenuSelected")) {
               var1 = 252;
               break label1058;
            }
            break;
         case 927863384:
            if (var0.equals("chat_inBubbleShadow")) {
               var1 = 184;
               break label1058;
            }
            break;
         case 939137799:
            if (var0.equals("chat_inContactPhoneText")) {
               var1 = 188;
               break label1058;
            }
            break;
         case 939824634:
            if (var0.equals("chat_outInstant")) {
               var1 = 209;
               break label1058;
            }
            break;
         case 946144034:
            if (var0.equals("windowBackgroundWhiteBlueText4")) {
               var1 = 217;
               break label1058;
            }
            break;
         case 962085693:
            if (var0.equals("chats_menuCloudBackgroundCats")) {
               var1 = 215;
               break label1058;
            }
            break;
         case 983278580:
            if (var0.equals("avatar_subtitleInProfileOrange")) {
               var1 = 261;
               break label1058;
            }
            break;
         case 993048796:
            if (var0.equals("chat_inFileSelectedIcon")) {
               var1 = 149;
               break label1058;
            }
            break;
         case 1008947016:
            if (var0.equals("avatar_backgroundActionBarRed")) {
               var1 = 231;
               break label1058;
            }
            break;
         case 1020100908:
            if (var0.equals("chat_inAudioSeekbarSelected")) {
               var1 = 167;
               break label1058;
            }
            break;
         case 1045892135:
            if (var0.equals("windowBackgroundWhiteGrayIcon")) {
               var1 = 186;
               break label1058;
            }
            break;
         case 1046222043:
            if (var0.equals("windowBackgroundWhiteGrayText")) {
               var1 = 86;
               break label1058;
            }
            break;
         case 1079427869:
            if (var0.equals("chat_inViewsSelected")) {
               var1 = 141;
               break label1058;
            }
            break;
         case 1100033490:
            if (var0.equals("chat_inAudioSelectedProgress")) {
               var1 = 115;
               break label1058;
            }
            break;
         case 1106068251:
            if (var0.equals("groupcreate_spanText")) {
               var1 = 245;
               break label1058;
            }
            break;
         case 1121079660:
            if (var0.equals("chat_outAudioSeekbar")) {
               var1 = 192;
               break label1058;
            }
            break;
         case 1122192435:
            if (var0.equals("chat_outLoaderPhotoSelected")) {
               var1 = 220;
               break label1058;
            }
            break;
         case 1175786053:
            if (var0.equals("avatar_subtitleInProfileViolet")) {
               var1 = 181;
               break label1058;
            }
            break;
         case 1195322391:
            if (var0.equals("chat_inAudioProgress")) {
               var1 = 207;
               break label1058;
            }
            break;
         case 1199344772:
            if (var0.equals("chat_topPanelBackground")) {
               var1 = 134;
               break label1058;
            }
            break;
         case 1201609915:
            if (var0.equals("chat_outReplyNameText")) {
               var1 = 180;
               break label1058;
            }
            break;
         case 1202885960:
            if (var0.equals("chat_outPreviewInstantSelectedText")) {
               var1 = 12;
               break label1058;
            }
            break;
         case 1212117123:
            if (var0.equals("avatar_backgroundActionBarBlue")) {
               var1 = 155;
               break label1058;
            }
            break;
         case 1212158796:
            if (var0.equals("avatar_backgroundActionBarCyan")) {
               var1 = 249;
               break label1058;
            }
            break;
         case 1212531103:
            if (var0.equals("avatar_backgroundActionBarPink")) {
               var1 = 178;
               break label1058;
            }
            break;
         case 1231763334:
            if (var0.equals("chat_addContact")) {
               var1 = 22;
               break label1058;
            }
            break;
         case 1239758101:
            if (var0.equals("player_placeholder")) {
               var1 = 68;
               break label1058;
            }
            break;
         case 1265168609:
            if (var0.equals("player_actionBarItems")) {
               var1 = 138;
               break label1058;
            }
            break;
         case 1269980952:
            if (var0.equals("chat_inBubble")) {
               var1 = 13;
               break label1058;
            }
            break;
         case 1275014009:
            if (var0.equals("player_actionBarTitle")) {
               var1 = 19;
               break label1058;
            }
            break;
         case 1285554199:
            if (var0.equals("avatar_backgroundActionBarOrange")) {
               var1 = 233;
               break label1058;
            }
            break;
         case 1288729698:
            if (var0.equals("chat_unreadMessagesStartArrowIcon")) {
               var1 = 53;
               break label1058;
            }
            break;
         case 1308150651:
            if (var0.equals("chat_outFileNameText")) {
               var1 = 38;
               break label1058;
            }
            break;
         case 1316752473:
            if (var0.equals("chat_outFileInfoSelectedText")) {
               var1 = 14;
               break label1058;
            }
            break;
         case 1327229315:
            if (var0.equals("actionBarDefaultSelector")) {
               var1 = 98;
               break label1058;
            }
            break;
         case 1333190005:
            if (var0.equals("chat_outForwardedNameText")) {
               var1 = 29;
               break label1058;
            }
            break;
         case 1372411761:
            if (var0.equals("inappPlayerPerformer")) {
               var1 = 61;
               break label1058;
            }
            break;
         case 1381159341:
            if (var0.equals("chat_inContactIcon")) {
               var1 = 57;
               break label1058;
            }
            break;
         case 1411374187:
            if (var0.equals("chat_messagePanelHint")) {
               var1 = 174;
               break label1058;
            }
            break;
         case 1411728145:
            if (var0.equals("chat_messagePanelText")) {
               var1 = 253;
               break label1058;
            }
            break;
         case 1414117958:
            if (var0.equals("chat_outSiteNameText")) {
               var1 = 121;
               break label1058;
            }
            break;
         case 1449754706:
            if (var0.equals("chat_outContactIcon")) {
               var1 = 96;
               break label1058;
            }
            break;
         case 1450167170:
            if (var0.equals("chat_outContactPhoneText")) {
               var1 = 117;
               break label1058;
            }
            break;
         case 1456911705:
            if (var0.equals("player_progressBackground")) {
               var1 = 31;
               break label1058;
            }
            break;
         case 1478061672:
            if (var0.equals("avatar_backgroundActionBarViolet")) {
               var1 = 73;
               break label1058;
            }
            break;
         case 1491567659:
            if (var0.equals("player_seekBarBackground")) {
               var1 = 202;
               break label1058;
            }
            break;
         case 1504078167:
            if (var0.equals("chat_outFileSelectedIcon")) {
               var1 = 91;
               break label1058;
            }
            break;
         case 1528152827:
            if (var0.equals("chat_inAudioTitleText")) {
               var1 = 110;
               break label1058;
            }
            break;
         case 1549064140:
            if (var0.equals("chat_outLoaderPhotoIconSelected")) {
               var1 = 191;
               break label1058;
            }
            break;
         case 1573464919:
            if (var0.equals("chat_serviceBackgroundSelected")) {
               var1 = 47;
               break label1058;
            }
            break;
         case 1585168289:
            if (var0.equals("chat_inFileIcon")) {
               var1 = 109;
               break label1058;
            }
            break;
         case 1595048395:
            if (var0.equals("chat_inAudioDurationText")) {
               var1 = 212;
               break label1058;
            }
            break;
         case 1628297471:
            if (var0.equals("chat_messageLinkIn")) {
               var1 = 84;
               break label1058;
            }
            break;
         case 1635685130:
            if (var0.equals("profile_verifiedCheck")) {
               var1 = 144;
               break label1058;
            }
            break;
         case 1637669025:
            if (var0.equals("chat_messageTextOut")) {
               var1 = 58;
               break label1058;
            }
            break;
         case 1647377944:
            if (var0.equals("chat_outViaBotNameText")) {
               var1 = 230;
               break label1058;
            }
            break;
         case 1657795113:
            if (var0.equals("chat_outSentCheck")) {
               var1 = 251;
               break label1058;
            }
            break;
         case 1657923887:
            if (var0.equals("chat_outSentClock")) {
               var1 = 112;
               break label1058;
            }
            break;
         case 1663688926:
            if (var0.equals("chats_attachMessage")) {
               var1 = 83;
               break label1058;
            }
            break;
         case 1674274489:
            if (var0.equals("chat_inVenueInfoSelectedText")) {
               var1 = 236;
               break label1058;
            }
            break;
         case 1674318617:
            if (var0.equals("divider")) {
               var1 = 39;
               break label1058;
            }
            break;
         case 1676443787:
            if (var0.equals("avatar_subtitleInProfileRed")) {
               var1 = 80;
               break label1058;
            }
            break;
         case 1682961989:
            if (var0.equals("switchThumbChecked")) {
               var1 = 106;
               break label1058;
            }
            break;
         case 1687612836:
            if (var0.equals("actionBarActionModeDefaultIcon")) {
               var1 = 237;
               break label1058;
            }
            break;
         case 1714118894:
            if (var0.equals("chat_unreadMessagesStartBackground")) {
               var1 = 154;
               break label1058;
            }
            break;
         case 1743255577:
            if (var0.equals("dialogBackgroundGray")) {
               var1 = 255;
               break label1058;
            }
            break;
         case 1809914009:
            if (var0.equals("dialogButtonSelector")) {
               var1 = 256;
               break label1058;
            }
            break;
         case 1814021667:
            if (var0.equals("chat_inFileInfoText")) {
               var1 = 185;
               break label1058;
            }
            break;
         case 1828201066:
            if (var0.equals("dialogTextBlack")) {
               var1 = 137;
               break label1058;
            }
            break;
         case 1829565163:
            if (var0.equals("chat_inMenu")) {
               var1 = 201;
               break label1058;
            }
            break;
         case 1853943154:
            if (var0.equals("chat_messageTextIn")) {
               var1 = 44;
               break label1058;
            }
            break;
         case 1878895888:
            if (var0.equals("avatar_actionBarSelectorBlue")) {
               var1 = 41;
               break label1058;
            }
            break;
         case 1878937561:
            if (var0.equals("avatar_actionBarSelectorCyan")) {
               var1 = 157;
               break label1058;
            }
            break;
         case 1879309868:
            if (var0.equals("avatar_actionBarSelectorPink")) {
               var1 = 221;
               break label1058;
            }
            break;
         case 1921699010:
            if (var0.equals("chats_unreadCounterMuted")) {
               var1 = 127;
               break label1058;
            }
            break;
         case 1929729373:
            if (var0.equals("progressCircle")) {
               var1 = 126;
               break label1058;
            }
            break;
         case 1930276193:
            if (var0.equals("chat_inTimeSelectedText")) {
               var1 = 259;
               break label1058;
            }
            break;
         case 1947549395:
            if (var0.equals("chat_inLoaderPhoto")) {
               var1 = 163;
               break label1058;
            }
            break;
         case 1972802227:
            if (var0.equals("chat_outReplyMediaMessageText")) {
               var1 = 177;
               break label1058;
            }
            break;
         case 1979989987:
            if (var0.equals("chat_outVenueInfoText")) {
               var1 = 257;
               break label1058;
            }
            break;
         case 1994112714:
            if (var0.equals("actionBarActionModeDefaultTop")) {
               var1 = 62;
               break label1058;
            }
            break;
         case 2016144760:
            if (var0.equals("chat_outLoaderPhoto")) {
               var1 = 45;
               break label1058;
            }
            break;
         case 2016511272:
            if (var0.equals("stickers_menu")) {
               var1 = 128;
               break label1058;
            }
            break;
         case 2052611411:
            if (var0.equals("chat_outBubble")) {
               var1 = 172;
               break label1058;
            }
            break;
         case 2067556030:
            if (var0.equals("chat_emojiPanelIcon")) {
               var1 = 16;
               break label1058;
            }
            break;
         case 2073762588:
            if (var0.equals("chat_outFileIcon")) {
               var1 = 46;
               break label1058;
            }
            break;
         case 2090082520:
            if (var0.equals("chats_nameMessage")) {
               var1 = 169;
               break label1058;
            }
            break;
         case 2099978769:
            if (var0.equals("chat_outLoaderPhotoIcon")) {
               var1 = 52;
               break label1058;
            }
            break;
         case 2109820260:
            if (var0.equals("avatar_actionBarSelectorOrange")) {
               var1 = 235;
               break label1058;
            }
            break;
         case 2118871810:
            if (var0.equals("switchThumb")) {
               var1 = 35;
               break label1058;
            }
            break;
         case 2119150199:
            if (var0.equals("switchTrack")) {
               var1 = 260;
               break label1058;
            }
            break;
         case 2131990258:
            if (var0.equals("windowBackgroundWhiteLinkText")) {
               var1 = 120;
               break label1058;
            }
            break;
         case 2133456819:
            if (var0.equals("chat_emojiPanelBackground")) {
               var1 = 152;
               break label1058;
            }
            break;
         case 2141345810:
            if (var0.equals("chat_messagePanelBackground")) {
               var1 = 102;
               break label1058;
            }
         }

         var1 = -1;
      }

      switch(var1) {
      case 0:
         return -7697782;
      case 1:
         return -1239540194;
      case 2:
         return -9342607;
      case 3:
         return -10653824;
      case 4:
         return -11167525;
      case 5:
         return 2047809827;
      case 6:
         return -8224126;
      case 7:
         return -645885536;
      case 8:
         return -13803892;
      case 9:
         return -15921907;
      case 10:
         return -12401818;
      case 11:
         return -9276814;
      case 12:
         return -1;
      case 13:
         return -14339006;
      case 14:
      case 15:
         return -1;
      case 16:
         return -9342607;
      case 17:
         return 1276090861;
      case 18:
         return -8882056;
      case 19:
         return -1579033;
      case 20:
         return -8487298;
      case 21:
         return -1;
      case 22:
         return -11164709;
      case 23:
         return -9594162;
      case 24:
         return -14339006;
      case 25:
         return 251658239;
      case 26:
         return -14605274;
      case 27:
         return -11164965;
      case 28:
         return -1313793;
      case 29:
         return -3019777;
      case 30:
         return -1;
      case 31:
         return -1979711488;
      case 32:
         return -11972268;
      case 33:
         return -7960954;
      case 34:
         return -10653824;
      case 35:
         return -12829636;
      case 36:
         return 268435455;
      case 37:
         return -986896;
      case 38:
         return -2954241;
      case 39:
         return 402653183;
      case 40:
         return -8211748;
      case 41:
         return -11972524;
      case 42:
         return -11234874;
      case 43:
         return -14143949;
      case 44:
         return -328966;
      case 45:
         return -13077852;
      case 46:
         return -13143396;
      case 47:
         return 1615417684;
      case 48:
         return -668259541;
      case 49:
         return -11108183;
      case 50:
         return -14935012;
      case 51:
         return -5582866;
      case 52:
         return -9263664;
      case 53:
         return -10851462;
      case 54:
         return -13077596;
      case 55:
         return -16777216;
      case 56:
         return -2102800402;
      case 57:
         return -14338750;
      case 58:
         return -328966;
      case 59:
         return -3019777;
      case 60:
         return -14925725;
      case 61:
         return -328966;
      case 62:
         return -1543503872;
      case 63:
         return -7697782;
      case 64:
         return -13091262;
      case 65:
         return -1;
      case 66:
         return -9652901;
      case 67:
         return -3874313;
      case 68:
         return -13948117;
      case 69:
         return -11164965;
      case 70:
         return -9868951;
      case 71:
         return -14540254;
      case 72:
         return -2236963;
      case 73:
         return -14605274;
      case 74:
         return -11167525;
      case 75:
         return -7105645;
      case 76:
         return -3019777;
      case 77:
         return -723724;
      case 78:
         return -11099429;
      case 79:
         return -5648402;
      case 80:
         return -7697782;
      case 81:
         return -10052929;
      case 82:
         return -8812393;
      case 83:
         return -11234874;
      case 84:
         return -11099173;
      case 85:
         return -14183202;
      case 86:
         return -10132123;
      case 87:
         return -9408400;
      case 88:
         return -81911774;
      case 89:
      case 90:
         return -1;
      case 91:
         return -13925429;
      case 92:
         return -9316522;
      case 93:
         return -8747891;
      case 94:
         return -7028510;
      case 95:
         return 167772159;
      case 96:
         return -5452289;
      case 97:
         return -9851917;
      case 98:
         return -11972268;
      case 99:
         return -10658467;
      case 100:
         return -8812137;
      case 101:
         return 771751936;
      case 102:
         return -14803426;
      case 103:
         return -9316522;
      case 104:
         return -11230501;
      case 105:
         return -7368817;
      case 106:
         return -13600600;
      case 107:
         return -1;
      case 108:
         return -11972268;
      case 109:
         return -14470078;
      case 110:
         return -11099173;
      case 111:
         return -5648402;
      case 112:
         return -8211748;
      case 113:
         return -14407896;
      case 114:
         return -11711155;
      case 115:
         return -14925469;
      case 116:
         return -11972524;
      case 117:
         return -4792321;
      case 118:
         return -10653824;
      case 119:
         return -3019777;
      case 120:
         return -12741934;
      case 121:
         return -3019777;
      case 122:
         return -14925725;
      case 123:
         return -10592674;
      case 124:
         return -9263664;
      case 125:
         return -13859893;
      case 126:
         return -13221820;
      case 127:
         return -12303292;
      case 128:
         return -11710381;
      case 129:
         return -1;
      case 130:
         return -11164965;
      case 131:
         return -5845010;
      case 132:
         return -9803158;
      case 133:
         return -9263664;
      case 134:
         return -98821092;
      case 135:
         return -1;
      case 136:
         return -15724528;
      case 137:
         return -394759;
      case 138:
         return -1;
      case 139:
         return -5855578;
      case 140:
         return -9590561;
      case 141:
         return -5648402;
      case 142:
         return -1;
      case 143:
         return -14605274;
      case 144:
      case 145:
         return -1;
      case 146:
         return -15316366;
      case 147:
         return 1713910333;
      case 148:
         return -8816263;
      case 149:
         return -15056797;
      case 150:
         return -1;
      case 151:
         return -9934744;
      case 152:
         return -14474461;
      case 153:
         return -11230501;
      case 154:
         return -14339006;
      case 155:
         return -14605274;
      case 156:
         return -11164965;
      case 157:
         return -11972268;
      case 158:
         return -2324391;
      case 159:
         return -9539986;
      case 160:
         return -13619152;
      case 161:
         return -11416584;
      case 162:
         return -9263664;
      case 163:
         return -14404542;
      case 164:
         return -13007663;
      case 165:
         return -11164965;
      case 166:
         return -10653824;
      case 167:
         return -5648402;
      case 168:
         return -1644826;
      case 169:
         return -11696202;
      case 170:
         return 789516;
      case 171:
         return -15263719;
      case 172:
         return -13077852;
      case 173:
         return -14868445;
      case 174:
         return -11776948;
      case 175:
         return -14869219;
      case 176:
         return -8812393;
      case 177:
         return -3019777;
      case 178:
         return -14605274;
      case 179:
         return -7421976;
      case 180:
         return -3019777;
      case 181:
         return -7697782;
      case 182:
         return -14187829;
      case 183:
         return -5648146;
      case 184:
         return -16777216;
      case 185:
         return -8812137;
      case 186:
         return -8224126;
      case 187:
         return -11443856;
      case 188:
         return -8812393;
      case 189:
         return -11232035;
      case 190:
      case 191:
         return -1;
      case 192:
         return -1770871344;
      case 193:
         return -10915968;
      case 194:
         return -45994;
      case 195:
         return -11972268;
      case 196:
         return 1627389951;
      case 197:
         return -3874313;
      case 198:
         return -657931;
      case 199:
         return -3019777;
      case 200:
         return -10574624;
      case 201:
         return 2036100992;
      case 202:
         return 1196577362;
      case 203:
         return -10452291;
      case 204:
         return -4792577;
      case 205:
         return -620756993;
      case 206:
         return -10987432;
      case 207:
         return -14338750;
      case 208:
         return -1;
      case 209:
         return -4792321;
      case 210:
         return -1;
      case 211:
         return -10910270;
      case 212:
         return -8746857;
      case 213:
         return 301989887;
      case 214:
         return -1776412;
      case 215:
         return -11232035;
      case 216:
         return -5648402;
      case 217:
         return -11890739;
      case 218:
         return -11099173;
      case 219:
         return -11164709;
      case 220:
         return -13208924;
      case 221:
         return -11972268;
      case 222:
         return -3019777;
      case 223:
         return -10526881;
      case 224:
         return -15526377;
      case 225:
         return 217775871;
      case 226:
         return -7434610;
      case 227:
         return -1481631;
      case 228:
         return -7697782;
      case 229:
         return -6513508;
      case 230:
         return -3019777;
      case 231:
         return -14605274;
      case 232:
         return -12214815;
      case 233:
         return -14605274;
      case 234:
         return -1;
      case 235:
         return -11972268;
      case 236:
         return -5648402;
      case 237:
         return -1;
      case 238:
         return -9934744;
      case 239:
         return -7697782;
      case 240:
         return -3019777;
      case 241:
         return -11447983;
      case 242:
         return -10653824;
      case 243:
         return -6234891;
      case 244:
         return -10790053;
      case 245:
         return -657931;
      case 246:
         return -855310;
      case 247:
         return -12413479;
      case 248:
         return -1;
      case 249:
         return -14605274;
      case 250:
         return -11184811;
      case 251:
         return -6831126;
      case 252:
         return -1;
      case 253:
         return -1118482;
      case 254:
         return -3019777;
      case 255:
         return -11840163;
      case 256:
         return 352321535;
      case 257:
         return -4792321;
      case 258:
         return -693579794;
      case 259:
         return -5582866;
      case 260:
         return -13948117;
      case 261:
         return -7697782;
      default:
         StringBuilder var2 = new StringBuilder();
         var2.append("returning color for key ");
         var2.append(var0);
         var2.append(" from current theme");
         FileLog.w(var2.toString());
         return Theme.getColor(var0);
      }
   }

   public static Drawable getThemedDrawable(Context var0, int var1, String var2) {
      Drawable var3 = var0.getResources().getDrawable(var1).mutate();
      var3.setColorFilter(new PorterDuffColorFilter(getColor(var2), Mode.MULTIPLY));
      return var3;
   }
}
