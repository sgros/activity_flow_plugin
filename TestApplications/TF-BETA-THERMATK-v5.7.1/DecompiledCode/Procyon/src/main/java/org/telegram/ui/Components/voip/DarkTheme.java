// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.voip;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.Drawable;
import android.content.Context;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.FileLog;

public class DarkTheme
{
    public static int getColor(final String str) {
        int n = 0;
        Label_6320: {
            switch (str.hashCode()) {
                case 2141345810: {
                    if (str.equals("chat_messagePanelBackground")) {
                        n = 102;
                        break Label_6320;
                    }
                    break;
                }
                case 2133456819: {
                    if (str.equals("chat_emojiPanelBackground")) {
                        n = 152;
                        break Label_6320;
                    }
                    break;
                }
                case 2131990258: {
                    if (str.equals("windowBackgroundWhiteLinkText")) {
                        n = 120;
                        break Label_6320;
                    }
                    break;
                }
                case 2119150199: {
                    if (str.equals("switchTrack")) {
                        n = 260;
                        break Label_6320;
                    }
                    break;
                }
                case 2118871810: {
                    if (str.equals("switchThumb")) {
                        n = 35;
                        break Label_6320;
                    }
                    break;
                }
                case 2109820260: {
                    if (str.equals("avatar_actionBarSelectorOrange")) {
                        n = 235;
                        break Label_6320;
                    }
                    break;
                }
                case 2099978769: {
                    if (str.equals("chat_outLoaderPhotoIcon")) {
                        n = 52;
                        break Label_6320;
                    }
                    break;
                }
                case 2090082520: {
                    if (str.equals("chats_nameMessage")) {
                        n = 169;
                        break Label_6320;
                    }
                    break;
                }
                case 2073762588: {
                    if (str.equals("chat_outFileIcon")) {
                        n = 46;
                        break Label_6320;
                    }
                    break;
                }
                case 2067556030: {
                    if (str.equals("chat_emojiPanelIcon")) {
                        n = 16;
                        break Label_6320;
                    }
                    break;
                }
                case 2052611411: {
                    if (str.equals("chat_outBubble")) {
                        n = 172;
                        break Label_6320;
                    }
                    break;
                }
                case 2016511272: {
                    if (str.equals("stickers_menu")) {
                        n = 128;
                        break Label_6320;
                    }
                    break;
                }
                case 2016144760: {
                    if (str.equals("chat_outLoaderPhoto")) {
                        n = 45;
                        break Label_6320;
                    }
                    break;
                }
                case 1994112714: {
                    if (str.equals("actionBarActionModeDefaultTop")) {
                        n = 62;
                        break Label_6320;
                    }
                    break;
                }
                case 1979989987: {
                    if (str.equals("chat_outVenueInfoText")) {
                        n = 257;
                        break Label_6320;
                    }
                    break;
                }
                case 1972802227: {
                    if (str.equals("chat_outReplyMediaMessageText")) {
                        n = 177;
                        break Label_6320;
                    }
                    break;
                }
                case 1947549395: {
                    if (str.equals("chat_inLoaderPhoto")) {
                        n = 163;
                        break Label_6320;
                    }
                    break;
                }
                case 1930276193: {
                    if (str.equals("chat_inTimeSelectedText")) {
                        n = 259;
                        break Label_6320;
                    }
                    break;
                }
                case 1929729373: {
                    if (str.equals("progressCircle")) {
                        n = 126;
                        break Label_6320;
                    }
                    break;
                }
                case 1921699010: {
                    if (str.equals("chats_unreadCounterMuted")) {
                        n = 127;
                        break Label_6320;
                    }
                    break;
                }
                case 1879309868: {
                    if (str.equals("avatar_actionBarSelectorPink")) {
                        n = 221;
                        break Label_6320;
                    }
                    break;
                }
                case 1878937561: {
                    if (str.equals("avatar_actionBarSelectorCyan")) {
                        n = 157;
                        break Label_6320;
                    }
                    break;
                }
                case 1878895888: {
                    if (str.equals("avatar_actionBarSelectorBlue")) {
                        n = 41;
                        break Label_6320;
                    }
                    break;
                }
                case 1853943154: {
                    if (str.equals("chat_messageTextIn")) {
                        n = 44;
                        break Label_6320;
                    }
                    break;
                }
                case 1829565163: {
                    if (str.equals("chat_inMenu")) {
                        n = 201;
                        break Label_6320;
                    }
                    break;
                }
                case 1828201066: {
                    if (str.equals("dialogTextBlack")) {
                        n = 137;
                        break Label_6320;
                    }
                    break;
                }
                case 1814021667: {
                    if (str.equals("chat_inFileInfoText")) {
                        n = 185;
                        break Label_6320;
                    }
                    break;
                }
                case 1809914009: {
                    if (str.equals("dialogButtonSelector")) {
                        n = 256;
                        break Label_6320;
                    }
                    break;
                }
                case 1743255577: {
                    if (str.equals("dialogBackgroundGray")) {
                        n = 255;
                        break Label_6320;
                    }
                    break;
                }
                case 1714118894: {
                    if (str.equals("chat_unreadMessagesStartBackground")) {
                        n = 154;
                        break Label_6320;
                    }
                    break;
                }
                case 1687612836: {
                    if (str.equals("actionBarActionModeDefaultIcon")) {
                        n = 237;
                        break Label_6320;
                    }
                    break;
                }
                case 1682961989: {
                    if (str.equals("switchThumbChecked")) {
                        n = 106;
                        break Label_6320;
                    }
                    break;
                }
                case 1676443787: {
                    if (str.equals("avatar_subtitleInProfileRed")) {
                        n = 80;
                        break Label_6320;
                    }
                    break;
                }
                case 1674318617: {
                    if (str.equals("divider")) {
                        n = 39;
                        break Label_6320;
                    }
                    break;
                }
                case 1674274489: {
                    if (str.equals("chat_inVenueInfoSelectedText")) {
                        n = 236;
                        break Label_6320;
                    }
                    break;
                }
                case 1663688926: {
                    if (str.equals("chats_attachMessage")) {
                        n = 83;
                        break Label_6320;
                    }
                    break;
                }
                case 1657923887: {
                    if (str.equals("chat_outSentClock")) {
                        n = 112;
                        break Label_6320;
                    }
                    break;
                }
                case 1657795113: {
                    if (str.equals("chat_outSentCheck")) {
                        n = 251;
                        break Label_6320;
                    }
                    break;
                }
                case 1647377944: {
                    if (str.equals("chat_outViaBotNameText")) {
                        n = 230;
                        break Label_6320;
                    }
                    break;
                }
                case 1637669025: {
                    if (str.equals("chat_messageTextOut")) {
                        n = 58;
                        break Label_6320;
                    }
                    break;
                }
                case 1635685130: {
                    if (str.equals("profile_verifiedCheck")) {
                        n = 144;
                        break Label_6320;
                    }
                    break;
                }
                case 1628297471: {
                    if (str.equals("chat_messageLinkIn")) {
                        n = 84;
                        break Label_6320;
                    }
                    break;
                }
                case 1595048395: {
                    if (str.equals("chat_inAudioDurationText")) {
                        n = 212;
                        break Label_6320;
                    }
                    break;
                }
                case 1585168289: {
                    if (str.equals("chat_inFileIcon")) {
                        n = 109;
                        break Label_6320;
                    }
                    break;
                }
                case 1573464919: {
                    if (str.equals("chat_serviceBackgroundSelected")) {
                        n = 47;
                        break Label_6320;
                    }
                    break;
                }
                case 1549064140: {
                    if (str.equals("chat_outLoaderPhotoIconSelected")) {
                        n = 191;
                        break Label_6320;
                    }
                    break;
                }
                case 1528152827: {
                    if (str.equals("chat_inAudioTitleText")) {
                        n = 110;
                        break Label_6320;
                    }
                    break;
                }
                case 1504078167: {
                    if (str.equals("chat_outFileSelectedIcon")) {
                        n = 91;
                        break Label_6320;
                    }
                    break;
                }
                case 1491567659: {
                    if (str.equals("player_seekBarBackground")) {
                        n = 202;
                        break Label_6320;
                    }
                    break;
                }
                case 1478061672: {
                    if (str.equals("avatar_backgroundActionBarViolet")) {
                        n = 73;
                        break Label_6320;
                    }
                    break;
                }
                case 1456911705: {
                    if (str.equals("player_progressBackground")) {
                        n = 31;
                        break Label_6320;
                    }
                    break;
                }
                case 1450167170: {
                    if (str.equals("chat_outContactPhoneText")) {
                        n = 117;
                        break Label_6320;
                    }
                    break;
                }
                case 1449754706: {
                    if (str.equals("chat_outContactIcon")) {
                        n = 96;
                        break Label_6320;
                    }
                    break;
                }
                case 1414117958: {
                    if (str.equals("chat_outSiteNameText")) {
                        n = 121;
                        break Label_6320;
                    }
                    break;
                }
                case 1411728145: {
                    if (str.equals("chat_messagePanelText")) {
                        n = 253;
                        break Label_6320;
                    }
                    break;
                }
                case 1411374187: {
                    if (str.equals("chat_messagePanelHint")) {
                        n = 174;
                        break Label_6320;
                    }
                    break;
                }
                case 1381159341: {
                    if (str.equals("chat_inContactIcon")) {
                        n = 57;
                        break Label_6320;
                    }
                    break;
                }
                case 1372411761: {
                    if (str.equals("inappPlayerPerformer")) {
                        n = 61;
                        break Label_6320;
                    }
                    break;
                }
                case 1333190005: {
                    if (str.equals("chat_outForwardedNameText")) {
                        n = 29;
                        break Label_6320;
                    }
                    break;
                }
                case 1327229315: {
                    if (str.equals("actionBarDefaultSelector")) {
                        n = 98;
                        break Label_6320;
                    }
                    break;
                }
                case 1316752473: {
                    if (str.equals("chat_outFileInfoSelectedText")) {
                        n = 14;
                        break Label_6320;
                    }
                    break;
                }
                case 1308150651: {
                    if (str.equals("chat_outFileNameText")) {
                        n = 38;
                        break Label_6320;
                    }
                    break;
                }
                case 1288729698: {
                    if (str.equals("chat_unreadMessagesStartArrowIcon")) {
                        n = 53;
                        break Label_6320;
                    }
                    break;
                }
                case 1285554199: {
                    if (str.equals("avatar_backgroundActionBarOrange")) {
                        n = 233;
                        break Label_6320;
                    }
                    break;
                }
                case 1275014009: {
                    if (str.equals("player_actionBarTitle")) {
                        n = 19;
                        break Label_6320;
                    }
                    break;
                }
                case 1269980952: {
                    if (str.equals("chat_inBubble")) {
                        n = 13;
                        break Label_6320;
                    }
                    break;
                }
                case 1265168609: {
                    if (str.equals("player_actionBarItems")) {
                        n = 138;
                        break Label_6320;
                    }
                    break;
                }
                case 1239758101: {
                    if (str.equals("player_placeholder")) {
                        n = 68;
                        break Label_6320;
                    }
                    break;
                }
                case 1231763334: {
                    if (str.equals("chat_addContact")) {
                        n = 22;
                        break Label_6320;
                    }
                    break;
                }
                case 1212531103: {
                    if (str.equals("avatar_backgroundActionBarPink")) {
                        n = 178;
                        break Label_6320;
                    }
                    break;
                }
                case 1212158796: {
                    if (str.equals("avatar_backgroundActionBarCyan")) {
                        n = 249;
                        break Label_6320;
                    }
                    break;
                }
                case 1212117123: {
                    if (str.equals("avatar_backgroundActionBarBlue")) {
                        n = 155;
                        break Label_6320;
                    }
                    break;
                }
                case 1202885960: {
                    if (str.equals("chat_outPreviewInstantSelectedText")) {
                        n = 12;
                        break Label_6320;
                    }
                    break;
                }
                case 1201609915: {
                    if (str.equals("chat_outReplyNameText")) {
                        n = 180;
                        break Label_6320;
                    }
                    break;
                }
                case 1199344772: {
                    if (str.equals("chat_topPanelBackground")) {
                        n = 134;
                        break Label_6320;
                    }
                    break;
                }
                case 1195322391: {
                    if (str.equals("chat_inAudioProgress")) {
                        n = 207;
                        break Label_6320;
                    }
                    break;
                }
                case 1175786053: {
                    if (str.equals("avatar_subtitleInProfileViolet")) {
                        n = 181;
                        break Label_6320;
                    }
                    break;
                }
                case 1122192435: {
                    if (str.equals("chat_outLoaderPhotoSelected")) {
                        n = 220;
                        break Label_6320;
                    }
                    break;
                }
                case 1121079660: {
                    if (str.equals("chat_outAudioSeekbar")) {
                        n = 192;
                        break Label_6320;
                    }
                    break;
                }
                case 1106068251: {
                    if (str.equals("groupcreate_spanText")) {
                        n = 245;
                        break Label_6320;
                    }
                    break;
                }
                case 1100033490: {
                    if (str.equals("chat_inAudioSelectedProgress")) {
                        n = 115;
                        break Label_6320;
                    }
                    break;
                }
                case 1079427869: {
                    if (str.equals("chat_inViewsSelected")) {
                        n = 141;
                        break Label_6320;
                    }
                    break;
                }
                case 1046222043: {
                    if (str.equals("windowBackgroundWhiteGrayText")) {
                        n = 86;
                        break Label_6320;
                    }
                    break;
                }
                case 1045892135: {
                    if (str.equals("windowBackgroundWhiteGrayIcon")) {
                        n = 186;
                        break Label_6320;
                    }
                    break;
                }
                case 1020100908: {
                    if (str.equals("chat_inAudioSeekbarSelected")) {
                        n = 167;
                        break Label_6320;
                    }
                    break;
                }
                case 1008947016: {
                    if (str.equals("avatar_backgroundActionBarRed")) {
                        n = 231;
                        break Label_6320;
                    }
                    break;
                }
                case 993048796: {
                    if (str.equals("chat_inFileSelectedIcon")) {
                        n = 149;
                        break Label_6320;
                    }
                    break;
                }
                case 983278580: {
                    if (str.equals("avatar_subtitleInProfileOrange")) {
                        n = 261;
                        break Label_6320;
                    }
                    break;
                }
                case 962085693: {
                    if (str.equals("chats_menuCloudBackgroundCats")) {
                        n = 215;
                        break Label_6320;
                    }
                    break;
                }
                case 946144034: {
                    if (str.equals("windowBackgroundWhiteBlueText4")) {
                        n = 217;
                        break Label_6320;
                    }
                    break;
                }
                case 939824634: {
                    if (str.equals("chat_outInstant")) {
                        n = 209;
                        break Label_6320;
                    }
                    break;
                }
                case 939137799: {
                    if (str.equals("chat_inContactPhoneText")) {
                        n = 188;
                        break Label_6320;
                    }
                    break;
                }
                case 927863384: {
                    if (str.equals("chat_inBubbleShadow")) {
                        n = 184;
                        break Label_6320;
                    }
                    break;
                }
                case 913069217: {
                    if (str.equals("chat_outMenuSelected")) {
                        n = 252;
                        break Label_6320;
                    }
                    break;
                }
                case 911091978: {
                    if (str.equals("chat_outLocationBackground")) {
                        n = 243;
                        break Label_6320;
                    }
                    break;
                }
                case 890367586: {
                    if (str.equals("chat_inViews")) {
                        n = 100;
                        break Label_6320;
                    }
                    break;
                }
                case 850854541: {
                    if (str.equals("chat_inPreviewInstantSelectedText")) {
                        n = 78;
                        break Label_6320;
                    }
                    break;
                }
                case 826015922: {
                    if (str.equals("chat_emojiPanelTrendingDescription")) {
                        n = 2;
                        break Label_6320;
                    }
                    break;
                }
                case 803672502: {
                    if (str.equals("chat_messagePanelIcons")) {
                        n = 70;
                        break Label_6320;
                    }
                    break;
                }
                case 765296599: {
                    if (str.equals("chat_outReplyLine")) {
                        n = 254;
                        break Label_6320;
                    }
                    break;
                }
                case 759679774: {
                    if (str.equals("chat_outVenueInfoSelectedText")) {
                        n = 135;
                        break Label_6320;
                    }
                    break;
                }
                case 732262561: {
                    if (str.equals("chat_outTimeText")) {
                        n = 258;
                        break Label_6320;
                    }
                    break;
                }
                case 716656587: {
                    if (str.equals("avatar_backgroundGroupCreateSpanBlue")) {
                        n = 8;
                        break Label_6320;
                    }
                    break;
                }
                case 676996437: {
                    if (str.equals("chat_outLocationIcon")) {
                        n = 81;
                        break Label_6320;
                    }
                    break;
                }
                case 655457041: {
                    if (str.equals("chat_inFileBackgroundSelected")) {
                        n = 234;
                        break Label_6320;
                    }
                    break;
                }
                case 648238646: {
                    if (str.equals("chat_outAudioTitleText")) {
                        n = 59;
                        break Label_6320;
                    }
                    break;
                }
                case 635007317: {
                    if (str.equals("chat_inFileProgress")) {
                        n = 242;
                        break Label_6320;
                    }
                    break;
                }
                case 634019162: {
                    if (str.equals("chat_emojiPanelBackspace")) {
                        n = 11;
                        break Label_6320;
                    }
                    break;
                }
                case 626157205: {
                    if (str.equals("chat_inVoiceSeekbar")) {
                        n = 34;
                        break Label_6320;
                    }
                    break;
                }
                case 613458991: {
                    if (str.equals("dialogTextLink")) {
                        n = 164;
                        break Label_6320;
                    }
                    break;
                }
                case 589961756: {
                    if (str.equals("chat_goDownButtonIcon")) {
                        n = 214;
                        break Label_6320;
                    }
                    break;
                }
                case 556028747: {
                    if (str.equals("chat_outVoiceSeekbarSelected")) {
                        n = 28;
                        break Label_6320;
                    }
                    break;
                }
                case 527405547: {
                    if (str.equals("inappPlayerBackground")) {
                        n = 48;
                        break Label_6320;
                    }
                    break;
                }
                case 503923205: {
                    if (str.equals("chat_inSentClockSelected")) {
                        n = 183;
                        break Label_6320;
                    }
                    break;
                }
                case 484353662: {
                    if (str.equals("chat_inVenueInfoText")) {
                        n = 118;
                        break Label_6320;
                    }
                    break;
                }
                case 460598594: {
                    if (str.equals("chat_topPanelTitle")) {
                        n = 219;
                        break Label_6320;
                    }
                    break;
                }
                case 446162770: {
                    if (str.equals("windowBackgroundWhiteBlueText")) {
                        n = 247;
                        break Label_6320;
                    }
                    break;
                }
                case 444983522: {
                    if (str.equals("chat_topPanelClose")) {
                        n = 250;
                        break Label_6320;
                    }
                    break;
                }
                case 439976061: {
                    if (str.equals("avatar_subtitleInProfileGreen")) {
                        n = 228;
                        break Label_6320;
                    }
                    break;
                }
                case 435303214: {
                    if (str.equals("actionBarDefaultSubmenuItem")) {
                        n = 198;
                        break Label_6320;
                    }
                    break;
                }
                case 430094524: {
                    if (str.equals("avatar_subtitleInProfilePink")) {
                        n = 0;
                        break Label_6320;
                    }
                    break;
                }
                case 429722217: {
                    if (str.equals("avatar_subtitleInProfileCyan")) {
                        n = 63;
                        break Label_6320;
                    }
                    break;
                }
                case 429680544: {
                    if (str.equals("avatar_subtitleInProfileBlue")) {
                        n = 239;
                        break Label_6320;
                    }
                    break;
                }
                case 426061980: {
                    if (str.equals("chat_serviceBackground")) {
                        n = 147;
                        break Label_6320;
                    }
                    break;
                }
                case 421601469: {
                    if (str.equals("chat_emojiPanelIconSelector")) {
                        n = 74;
                        break Label_6320;
                    }
                    break;
                }
                case 421601145: {
                    if (str.equals("chat_emojiPanelIconSelected")) {
                        n = 4;
                        break Label_6320;
                    }
                    break;
                }
                case 415452907: {
                    if (str.equals("chat_outAudioDurationSelectedText")) {
                        n = 142;
                        break Label_6320;
                    }
                    break;
                }
                case 371859081: {
                    if (str.equals("chat_inReplyMediaMessageSelectedText")) {
                        n = 140;
                        break Label_6320;
                    }
                    break;
                }
                case 339397761: {
                    if (str.equals("windowBackgroundWhiteBlackText")) {
                        n = 246;
                        break Label_6320;
                    }
                    break;
                }
                case 316847509: {
                    if (str.equals("chat_outLoaderSelected")) {
                        n = 15;
                        break Label_6320;
                    }
                    break;
                }
                case 303350244: {
                    if (str.equals("chat_reportSpam")) {
                        n = 227;
                        break Label_6320;
                    }
                    break;
                }
                case 271457747: {
                    if (str.equals("chat_inBubbleSelected")) {
                        n = 122;
                        break Label_6320;
                    }
                    break;
                }
                case 257089712: {
                    if (str.equals("chat_outAudioDurationText")) {
                        n = 119;
                        break Label_6320;
                    }
                    break;
                }
                case 243668262: {
                    if (str.equals("chat_inTimeText")) {
                        n = 7;
                        break Label_6320;
                    }
                    break;
                }
                case 231486891: {
                    if (str.equals("chat_inAudioPerfomerText")) {
                        n = 82;
                        break Label_6320;
                    }
                    break;
                }
                case 216441603: {
                    if (str.equals("chat_goDownButton")) {
                        n = 114;
                        break Label_6320;
                    }
                    break;
                }
                case 185438775: {
                    if (str.equals("chat_outAudioSelectedProgress")) {
                        n = 182;
                        break Label_6320;
                    }
                    break;
                }
                case 141894978: {
                    if (str.equals("windowBackgroundWhiteRedText5")) {
                        n = 194;
                        break Label_6320;
                    }
                    break;
                }
                case 141076636: {
                    if (str.equals("groupcreate_spanBackground")) {
                        n = 43;
                        break Label_6320;
                    }
                    break;
                }
                case 117743477: {
                    if (str.equals("chat_outPreviewLine")) {
                        n = 199;
                        break Label_6320;
                    }
                    break;
                }
                case 89466127: {
                    if (str.equals("chat_outAudioSeekbarFill")) {
                        n = 67;
                        break Label_6320;
                    }
                    break;
                }
                case 57460786: {
                    if (str.equals("chats_sentClock")) {
                        n = 203;
                        break Label_6320;
                    }
                    break;
                }
                case 57332012: {
                    if (str.equals("chats_sentCheck")) {
                        n = 200;
                        break Label_6320;
                    }
                    break;
                }
                case 51359814: {
                    if (str.equals("chat_replyPanelMessage")) {
                        n = 75;
                        break Label_6320;
                    }
                    break;
                }
                case 49148112: {
                    if (str.equals("chat_inPreviewLine")) {
                        n = 153;
                        break Label_6320;
                    }
                    break;
                }
                case 27337780: {
                    if (str.equals("chats_pinnedOverlay")) {
                        n = 95;
                        break Label_6320;
                    }
                    break;
                }
                case 6289575: {
                    if (str.equals("chat_inLoaderPhotoIconSelected")) {
                        n = 216;
                        break Label_6320;
                    }
                    break;
                }
                case -12871922: {
                    if (str.equals("chat_secretChatStatusText")) {
                        n = 151;
                        break Label_6320;
                    }
                    break;
                }
                case -18073397: {
                    if (str.equals("chats_tabletSelectedOverlay")) {
                        n = 36;
                        break Label_6320;
                    }
                    break;
                }
                case -35597940: {
                    if (str.equals("chat_inContactNameText")) {
                        n = 218;
                        break Label_6320;
                    }
                    break;
                }
                case -65277181: {
                    if (str.equals("chats_menuItemText")) {
                        n = 37;
                        break Label_6320;
                    }
                    break;
                }
                case -65607089: {
                    if (str.equals("chats_menuItemIcon")) {
                        n = 6;
                        break Label_6320;
                    }
                    break;
                }
                case -71280336: {
                    if (str.equals("switchTrackChecked")) {
                        n = 146;
                        break Label_6320;
                    }
                    break;
                }
                case -108292334: {
                    if (str.equals("chats_menuTopShadow")) {
                        n = 136;
                        break Label_6320;
                    }
                    break;
                }
                case -127673038: {
                    if (str.equals("key_chats_menuTopShadow")) {
                        n = 170;
                        break Label_6320;
                    }
                    break;
                }
                case -143547632: {
                    if (str.equals("chat_inFileProgressSelected")) {
                        n = 131;
                        break Label_6320;
                    }
                    break;
                }
                case -176488427: {
                    if (str.equals("chat_replyPanelLine")) {
                        n = 175;
                        break Label_6320;
                    }
                    break;
                }
                case -185786131: {
                    if (str.equals("chat_unreadMessagesStartText")) {
                        n = 205;
                        break Label_6320;
                    }
                    break;
                }
                case -212237793: {
                    if (str.equals("player_actionBar")) {
                        n = 50;
                        break Label_6320;
                    }
                    break;
                }
                case -248568965: {
                    if (str.equals("inappPlayerTitle")) {
                        n = 229;
                        break Label_6320;
                    }
                    break;
                }
                case -249481380: {
                    if (str.equals("listSelectorSDK21")) {
                        n = 213;
                        break Label_6320;
                    }
                    break;
                }
                case -251079667: {
                    if (str.equals("chat_outPreviewInstantText")) {
                        n = 76;
                        break Label_6320;
                    }
                    break;
                }
                case -258492929: {
                    if (str.equals("avatar_nameInMessageOrange")) {
                        n = 158;
                        break Label_6320;
                    }
                    break;
                }
                case -260428237: {
                    if (str.equals("chat_outVoiceSeekbarFill")) {
                        n = 197;
                        break Label_6320;
                    }
                    break;
                }
                case -264184037: {
                    if (str.equals("inappPlayerClose")) {
                        n = 206;
                        break Label_6320;
                    }
                    break;
                }
                case -294026410: {
                    if (str.equals("chat_inReplyNameText")) {
                        n = 69;
                        break Label_6320;
                    }
                    break;
                }
                case -343666293: {
                    if (str.equals("windowBackgroundWhite")) {
                        n = 171;
                        break Label_6320;
                    }
                    break;
                }
                case -354489314: {
                    if (str.equals("chat_outFileInfoText")) {
                        n = 51;
                        break Label_6320;
                    }
                    break;
                }
                case -391617936: {
                    if (str.equals("chat_selectedBackground")) {
                        n = 17;
                        break Label_6320;
                    }
                    break;
                }
                case -427186938: {
                    if (str.equals("chat_inAudioDurationSelectedText")) {
                        n = 111;
                        break Label_6320;
                    }
                    break;
                }
                case -450514995: {
                    if (str.equals("chats_actionMessage")) {
                        n = 42;
                        break Label_6320;
                    }
                    break;
                }
                case -493564645: {
                    if (str.equals("avatar_actionBarSelectorRed")) {
                        n = 32;
                        break Label_6320;
                    }
                    break;
                }
                case -552118908: {
                    if (str.equals("actionBarDefault")) {
                        n = 113;
                        break Label_6320;
                    }
                    break;
                }
                case -560721948: {
                    if (str.equals("chat_outSentCheckSelected")) {
                        n = 89;
                        break Label_6320;
                    }
                    break;
                }
                case -564899147: {
                    if (str.equals("chat_outInstantSelected")) {
                        n = 190;
                        break Label_6320;
                    }
                    break;
                }
                case -570274322: {
                    if (str.equals("chat_outReplyMediaMessageSelectedText")) {
                        n = 248;
                        break Label_6320;
                    }
                    break;
                }
                case -603597494: {
                    if (str.equals("chat_inSentClock")) {
                        n = 166;
                        break Label_6320;
                    }
                    break;
                }
                case -608456434: {
                    if (str.equals("chat_outBubbleSelected")) {
                        n = 125;
                        break Label_6320;
                    }
                    break;
                }
                case -629209323: {
                    if (str.equals("chats_pinnedIcon")) {
                        n = 18;
                        break Label_6320;
                    }
                    break;
                }
                case -652337344: {
                    if (str.equals("chat_outVenueNameText")) {
                        n = 240;
                        break Label_6320;
                    }
                    break;
                }
                case -654429213: {
                    if (str.equals("chats_message")) {
                        n = 238;
                        break Label_6320;
                    }
                    break;
                }
                case -687452692: {
                    if (str.equals("chat_inLoaderPhotoIcon")) {
                        n = 193;
                        break Label_6320;
                    }
                    break;
                }
                case -712338357: {
                    if (str.equals("chat_inSiteNameText")) {
                        n = 130;
                        break Label_6320;
                    }
                    break;
                }
                case -756337980: {
                    if (str.equals("profile_actionPressedBackground")) {
                        n = 116;
                        break Label_6320;
                    }
                    break;
                }
                case -763087825: {
                    if (str.equals("chats_name")) {
                        n = 168;
                        break Label_6320;
                    }
                    break;
                }
                case -763385518: {
                    if (str.equals("chats_date")) {
                        n = 123;
                        break Label_6320;
                    }
                    break;
                }
                case -779362418: {
                    if (str.equals("chat_emojiPanelTrendingTitle")) {
                        n = 77;
                        break Label_6320;
                    }
                    break;
                }
                case -792942846: {
                    if (str.equals("graySection")) {
                        n = 71;
                        break Label_6320;
                    }
                    break;
                }
                case -805096120: {
                    if (str.equals("chats_nameIcon")) {
                        n = 72;
                        break Label_6320;
                    }
                    break;
                }
                case -810517465: {
                    if (str.equals("chat_outAudioSeekbarSelected")) {
                        n = 129;
                        break Label_6320;
                    }
                    break;
                }
                case -834035478: {
                    if (str.equals("chat_outSentClockSelected")) {
                        n = 65;
                        break Label_6320;
                    }
                    break;
                }
                case -901363160: {
                    if (str.equals("chats_menuPhoneCats")) {
                        n = 226;
                        break Label_6320;
                    }
                    break;
                }
                case -938826921: {
                    if (str.equals("player_actionBarSubtitle")) {
                        n = 223;
                        break Label_6320;
                    }
                    break;
                }
                case -955211830: {
                    if (str.equals("chat_topPanelLine")) {
                        n = 49;
                        break Label_6320;
                    }
                    break;
                }
                case -960321732: {
                    if (str.equals("chat_mediaMenu")) {
                        n = 21;
                        break Label_6320;
                    }
                    break;
                }
                case -1004973057: {
                    if (str.equals("chats_secretName")) {
                        n = 103;
                        break Label_6320;
                    }
                    break;
                }
                case -1005120019: {
                    if (str.equals("chats_secretIcon")) {
                        n = 92;
                        break Label_6320;
                    }
                    break;
                }
                case -1005376655: {
                    if (str.equals("chat_inAudioSeekbar")) {
                        n = 187;
                        break Label_6320;
                    }
                    break;
                }
                case -1006953508: {
                    if (str.equals("chat_secretTimerBackground")) {
                        n = 1;
                        break Label_6320;
                    }
                    break;
                }
                case -1012016554: {
                    if (str.equals("chat_inFileBackground")) {
                        n = 3;
                        break Label_6320;
                    }
                    break;
                }
                case -1019316079: {
                    if (str.equals("chat_outReplyMessageText")) {
                        n = 210;
                        break Label_6320;
                    }
                    break;
                }
                case -1046600742: {
                    if (str.equals("profile_actionBackground")) {
                        n = 64;
                        break Label_6320;
                    }
                    break;
                }
                case -1062379852: {
                    if (str.equals("chat_messageLinkOut")) {
                        n = 204;
                        break Label_6320;
                    }
                    break;
                }
                case -1063762099: {
                    if (str.equals("windowBackgroundWhiteGreenText2")) {
                        n = 10;
                        break Label_6320;
                    }
                    break;
                }
                case -1074293766: {
                    if (str.equals("avatar_backgroundActionBarGreen")) {
                        n = 143;
                        break Label_6320;
                    }
                    break;
                }
                case -1078554766: {
                    if (str.equals("windowBackgroundWhiteBlueHeader")) {
                        n = 97;
                        break Label_6320;
                    }
                    break;
                }
                case -1106471792: {
                    if (str.equals("chat_outAudioPerfomerText")) {
                        n = 94;
                        break Label_6320;
                    }
                    break;
                }
                case -1147596450: {
                    if (str.equals("chat_inFileInfoSelectedText")) {
                        n = 79;
                        break Label_6320;
                    }
                    break;
                }
                case -1213387098: {
                    if (str.equals("chat_inMenuSelected")) {
                        n = 56;
                        break Label_6320;
                    }
                    break;
                }
                case -1229478359: {
                    if (str.equals("chats_unreadCounter")) {
                        n = 85;
                        break Label_6320;
                    }
                    break;
                }
                case -1240647597: {
                    if (str.equals("chat_outBubbleShadow")) {
                        n = 55;
                        break Label_6320;
                    }
                    break;
                }
                case -1262649070: {
                    if (str.equals("avatar_nameInMessageGreen")) {
                        n = 66;
                        break Label_6320;
                    }
                    break;
                }
                case -1310183623: {
                    if (str.equals("chat_muteIcon")) {
                        n = 20;
                        break Label_6320;
                    }
                    break;
                }
                case -1316415606: {
                    if (str.equals("actionBarActionModeDefaultSelector")) {
                        n = 5;
                        break Label_6320;
                    }
                    break;
                }
                case -1385379359: {
                    if (str.equals("dialogIcon")) {
                        n = 93;
                        break Label_6320;
                    }
                    break;
                }
                case -1397026623: {
                    if (str.equals("windowBackgroundGray")) {
                        n = 9;
                        break Label_6320;
                    }
                    break;
                }
                case -1407570354: {
                    if (str.equals("chat_inReplyMediaMessageText")) {
                        n = 176;
                        break Label_6320;
                    }
                    break;
                }
                case -1415980195: {
                    if (str.equals("files_folderIconBackground")) {
                        n = 160;
                        break Label_6320;
                    }
                    break;
                }
                case -1496224782: {
                    if (str.equals("chat_inReplyLine")) {
                        n = 104;
                        break Label_6320;
                    }
                    break;
                }
                case -1530345450: {
                    if (str.equals("chat_inReplyMessageText")) {
                        n = 107;
                        break Label_6320;
                    }
                    break;
                }
                case -1533503664: {
                    if (str.equals("chat_outFileProgress")) {
                        n = 124;
                        break Label_6320;
                    }
                    break;
                }
                case -1542353776: {
                    if (str.equals("chat_outVoiceSeekbar")) {
                        n = 133;
                        break Label_6320;
                    }
                    break;
                }
                case -1543133775: {
                    if (str.equals("chat_outContactNameText")) {
                        n = 222;
                        break Label_6320;
                    }
                    break;
                }
                case -1565843249: {
                    if (str.equals("files_folderIcon")) {
                        n = 139;
                        break Label_6320;
                    }
                    break;
                }
                case -1589702002: {
                    if (str.equals("chat_inLoaderPhotoSelected")) {
                        n = 60;
                        break Label_6320;
                    }
                    break;
                }
                case -1604008580: {
                    if (str.equals("chat_outAudioProgress")) {
                        n = 54;
                        break Label_6320;
                    }
                    break;
                }
                case -1623818608: {
                    if (str.equals("chat_inForwardedNameText")) {
                        n = 165;
                        break Label_6320;
                    }
                    break;
                }
                case -1625862693: {
                    if (str.equals("chat_wallpaper")) {
                        n = 224;
                        break Label_6320;
                    }
                    break;
                }
                case -1633591792: {
                    if (str.equals("chat_emojiPanelStickerPackSelector")) {
                        n = 225;
                        break Label_6320;
                    }
                    break;
                }
                case -1654302575: {
                    if (str.equals("chats_menuBackground")) {
                        n = 173;
                        break Label_6320;
                    }
                    break;
                }
                case -1683744660: {
                    if (str.equals("profile_verifiedBackground")) {
                        n = 161;
                        break Label_6320;
                    }
                    break;
                }
                case -1719839798: {
                    if (str.equals("avatar_backgroundInProfileBlue")) {
                        n = 189;
                        break Label_6320;
                    }
                    break;
                }
                case -1719903102: {
                    if (str.equals("chat_outViewsSelected")) {
                        n = 145;
                        break Label_6320;
                    }
                    break;
                }
                case -1724033454: {
                    if (str.equals("chat_inPreviewInstantText")) {
                        n = 27;
                        break Label_6320;
                    }
                    break;
                }
                case -1733632792: {
                    if (str.equals("emptyListPlaceholder")) {
                        n = 241;
                        break Label_6320;
                    }
                    break;
                }
                case -1758608141: {
                    if (str.equals("windowBackgroundWhiteValueText")) {
                        n = 232;
                        break Label_6320;
                    }
                    break;
                }
                case -1767675171: {
                    if (str.equals("chat_inViaBotNameText")) {
                        n = 156;
                        break Label_6320;
                    }
                    break;
                }
                case -1777297962: {
                    if (str.equals("chats_muteIcon")) {
                        n = 244;
                        break Label_6320;
                    }
                    break;
                }
                case -1779173263: {
                    if (str.equals("chat_topPanelMessage")) {
                        n = 132;
                        break Label_6320;
                    }
                    break;
                }
                case -1787129273: {
                    if (str.equals("chat_outContactBackground")) {
                        n = 211;
                        break Label_6320;
                    }
                    break;
                }
                case -1849805674: {
                    if (str.equals("dialogBackground")) {
                        n = 26;
                        break Label_6320;
                    }
                    break;
                }
                case -1850167367: {
                    if (str.equals("chat_emojiPanelShadowLine")) {
                        n = 25;
                        break Label_6320;
                    }
                    break;
                }
                case -1853661732: {
                    if (str.equals("chat_outTimeSelectedText")) {
                        n = 90;
                        break Label_6320;
                    }
                    break;
                }
                case -1878988531: {
                    if (str.equals("avatar_actionBarSelectorGreen")) {
                        n = 108;
                        break Label_6320;
                    }
                    break;
                }
                case -1891930735: {
                    if (str.equals("chat_outFileBackground")) {
                        n = 162;
                        break Label_6320;
                    }
                    break;
                }
                case -1924841028: {
                    if (str.equals("actionBarDefaultSubtitle")) {
                        n = 105;
                        break Label_6320;
                    }
                    break;
                }
                case -1926854983: {
                    if (str.equals("windowBackgroundWhiteGrayText4")) {
                        n = 159;
                        break Label_6320;
                    }
                    break;
                }
                case -1926854984: {
                    if (str.equals("windowBackgroundWhiteGrayText3")) {
                        n = 87;
                        break Label_6320;
                    }
                    break;
                }
                case -1926854985: {
                    if (str.equals("windowBackgroundWhiteGrayText2")) {
                        n = 148;
                        break Label_6320;
                    }
                    break;
                }
                case -1927175348: {
                    if (str.equals("chat_outFileBackgroundSelected")) {
                        n = 208;
                        break Label_6320;
                    }
                    break;
                }
                case -1942198229: {
                    if (str.equals("chats_menuPhone")) {
                        n = 196;
                        break Label_6320;
                    }
                    break;
                }
                case -1961633574: {
                    if (str.equals("chat_outLoader")) {
                        n = 179;
                        break Label_6320;
                    }
                    break;
                }
                case -1974166005: {
                    if (str.equals("chat_outFileProgressSelected")) {
                        n = 30;
                        break Label_6320;
                    }
                    break;
                }
                case -1975862704: {
                    if (str.equals("player_button")) {
                        n = 33;
                        break Label_6320;
                    }
                    break;
                }
                case -1992639563: {
                    if (str.equals("avatar_actionBarSelectorViolet")) {
                        n = 195;
                        break Label_6320;
                    }
                    break;
                }
                case -1992864503: {
                    if (str.equals("actionBarDefaultSubmenuBackground")) {
                        n = 88;
                        break Label_6320;
                    }
                    break;
                }
                case -2019587427: {
                    if (str.equals("listSelector")) {
                        n = 101;
                        break Label_6320;
                    }
                    break;
                }
                case -2102232027: {
                    if (str.equals("profile_actionIcon")) {
                        n = 150;
                        break Label_6320;
                    }
                    break;
                }
                case -2103805301: {
                    if (str.equals("actionBarActionModeDefault")) {
                        n = 24;
                        break Label_6320;
                    }
                    break;
                }
                case -2132427577: {
                    if (str.equals("chat_outViews")) {
                        n = 40;
                        break Label_6320;
                    }
                    break;
                }
                case -2139469579: {
                    if (str.equals("chat_emojiPanelEmptyText")) {
                        n = 99;
                        break Label_6320;
                    }
                    break;
                }
                case -2147269658: {
                    if (str.equals("chat_outMenu")) {
                        n = 23;
                        break Label_6320;
                    }
                    break;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("returning color for key ");
                sb.append(str);
                sb.append(" from current theme");
                FileLog.w(sb.toString());
                return Theme.getColor(str);
            }
            case 261: {
                return -7697782;
            }
            case 260: {
                return -13948117;
            }
            case 259: {
                return -5582866;
            }
            case 258: {
                return -693579794;
            }
            case 257: {
                return -4792321;
            }
            case 256: {
                return 352321535;
            }
            case 255: {
                return -11840163;
            }
            case 254: {
                return -3019777;
            }
            case 253: {
                return -1118482;
            }
            case 252: {
                return -1;
            }
            case 251: {
                return -6831126;
            }
            case 250: {
                return -11184811;
            }
            case 249: {
                return -14605274;
            }
            case 248: {
                return -1;
            }
            case 247: {
                return -12413479;
            }
            case 246: {
                return -855310;
            }
            case 245: {
                return -657931;
            }
            case 244: {
                return -10790053;
            }
            case 243: {
                return -6234891;
            }
            case 242: {
                return -10653824;
            }
            case 241: {
                return -11447983;
            }
            case 240: {
                return -3019777;
            }
            case 239: {
                return -7697782;
            }
            case 238: {
                return -9934744;
            }
            case 237: {
                return -1;
            }
            case 236: {
                return -5648402;
            }
            case 235: {
                return -11972268;
            }
            case 234: {
                return -1;
            }
            case 233: {
                return -14605274;
            }
            case 232: {
                return -12214815;
            }
            case 231: {
                return -14605274;
            }
            case 230: {
                return -3019777;
            }
            case 229: {
                return -6513508;
            }
            case 228: {
                return -7697782;
            }
            case 227: {
                return -1481631;
            }
            case 226: {
                return -7434610;
            }
            case 225: {
                return 217775871;
            }
            case 224: {
                return -15526377;
            }
            case 223: {
                return -10526881;
            }
            case 222: {
                return -3019777;
            }
            case 221: {
                return -11972268;
            }
            case 220: {
                return -13208924;
            }
            case 219: {
                return -11164709;
            }
            case 218: {
                return -11099173;
            }
            case 217: {
                return -11890739;
            }
            case 216: {
                return -5648402;
            }
            case 215: {
                return -11232035;
            }
            case 214: {
                return -1776412;
            }
            case 213: {
                return 301989887;
            }
            case 212: {
                return -8746857;
            }
            case 211: {
                return -10910270;
            }
            case 210: {
                return -1;
            }
            case 209: {
                return -4792321;
            }
            case 208: {
                return -1;
            }
            case 207: {
                return -14338750;
            }
            case 206: {
                return -10987432;
            }
            case 205: {
                return -620756993;
            }
            case 204: {
                return -4792577;
            }
            case 203: {
                return -10452291;
            }
            case 202: {
                return 1196577362;
            }
            case 201: {
                return 2036100992;
            }
            case 200: {
                return -10574624;
            }
            case 199: {
                return -3019777;
            }
            case 198: {
                return -657931;
            }
            case 197: {
                return -3874313;
            }
            case 196: {
                return 1627389951;
            }
            case 195: {
                return -11972268;
            }
            case 194: {
                return -45994;
            }
            case 193: {
                return -10915968;
            }
            case 192: {
                return -1770871344;
            }
            case 190:
            case 191: {
                return -1;
            }
            case 189: {
                return -11232035;
            }
            case 188: {
                return -8812393;
            }
            case 187: {
                return -11443856;
            }
            case 186: {
                return -8224126;
            }
            case 185: {
                return -8812137;
            }
            case 184: {
                return -16777216;
            }
            case 183: {
                return -5648146;
            }
            case 182: {
                return -14187829;
            }
            case 181: {
                return -7697782;
            }
            case 180: {
                return -3019777;
            }
            case 179: {
                return -7421976;
            }
            case 178: {
                return -14605274;
            }
            case 177: {
                return -3019777;
            }
            case 176: {
                return -8812393;
            }
            case 175: {
                return -14869219;
            }
            case 174: {
                return -11776948;
            }
            case 173: {
                return -14868445;
            }
            case 172: {
                return -13077852;
            }
            case 171: {
                return -15263719;
            }
            case 170: {
                return 789516;
            }
            case 169: {
                return -11696202;
            }
            case 168: {
                return -1644826;
            }
            case 167: {
                return -5648402;
            }
            case 166: {
                return -10653824;
            }
            case 165: {
                return -11164965;
            }
            case 164: {
                return -13007663;
            }
            case 163: {
                return -14404542;
            }
            case 162: {
                return -9263664;
            }
            case 161: {
                return -11416584;
            }
            case 160: {
                return -13619152;
            }
            case 159: {
                return -9539986;
            }
            case 158: {
                return -2324391;
            }
            case 157: {
                return -11972268;
            }
            case 156: {
                return -11164965;
            }
            case 155: {
                return -14605274;
            }
            case 154: {
                return -14339006;
            }
            case 153: {
                return -11230501;
            }
            case 152: {
                return -14474461;
            }
            case 151: {
                return -9934744;
            }
            case 150: {
                return -1;
            }
            case 149: {
                return -15056797;
            }
            case 148: {
                return -8816263;
            }
            case 147: {
                return 1713910333;
            }
            case 146: {
                return -15316366;
            }
            case 144:
            case 145: {
                return -1;
            }
            case 143: {
                return -14605274;
            }
            case 142: {
                return -1;
            }
            case 141: {
                return -5648402;
            }
            case 140: {
                return -9590561;
            }
            case 139: {
                return -5855578;
            }
            case 138: {
                return -1;
            }
            case 137: {
                return -394759;
            }
            case 136: {
                return -15724528;
            }
            case 135: {
                return -1;
            }
            case 134: {
                return -98821092;
            }
            case 133: {
                return -9263664;
            }
            case 132: {
                return -9803158;
            }
            case 131: {
                return -5845010;
            }
            case 130: {
                return -11164965;
            }
            case 129: {
                return -1;
            }
            case 128: {
                return -11710381;
            }
            case 127: {
                return -12303292;
            }
            case 126: {
                return -13221820;
            }
            case 125: {
                return -13859893;
            }
            case 124: {
                return -9263664;
            }
            case 123: {
                return -10592674;
            }
            case 122: {
                return -14925725;
            }
            case 121: {
                return -3019777;
            }
            case 120: {
                return -12741934;
            }
            case 119: {
                return -3019777;
            }
            case 118: {
                return -10653824;
            }
            case 117: {
                return -4792321;
            }
            case 116: {
                return -11972524;
            }
            case 115: {
                return -14925469;
            }
            case 114: {
                return -11711155;
            }
            case 113: {
                return -14407896;
            }
            case 112: {
                return -8211748;
            }
            case 111: {
                return -5648402;
            }
            case 110: {
                return -11099173;
            }
            case 109: {
                return -14470078;
            }
            case 108: {
                return -11972268;
            }
            case 107: {
                return -1;
            }
            case 106: {
                return -13600600;
            }
            case 105: {
                return -7368817;
            }
            case 104: {
                return -11230501;
            }
            case 103: {
                return -9316522;
            }
            case 102: {
                return -14803426;
            }
            case 101: {
                return 771751936;
            }
            case 100: {
                return -8812137;
            }
            case 99: {
                return -10658467;
            }
            case 98: {
                return -11972268;
            }
            case 97: {
                return -9851917;
            }
            case 96: {
                return -5452289;
            }
            case 95: {
                return 167772159;
            }
            case 94: {
                return -7028510;
            }
            case 93: {
                return -8747891;
            }
            case 92: {
                return -9316522;
            }
            case 91: {
                return -13925429;
            }
            case 89:
            case 90: {
                return -1;
            }
            case 88: {
                return -81911774;
            }
            case 87: {
                return -9408400;
            }
            case 86: {
                return -10132123;
            }
            case 85: {
                return -14183202;
            }
            case 84: {
                return -11099173;
            }
            case 83: {
                return -11234874;
            }
            case 82: {
                return -8812393;
            }
            case 81: {
                return -10052929;
            }
            case 80: {
                return -7697782;
            }
            case 79: {
                return -5648402;
            }
            case 78: {
                return -11099429;
            }
            case 77: {
                return -723724;
            }
            case 76: {
                return -3019777;
            }
            case 75: {
                return -7105645;
            }
            case 74: {
                return -11167525;
            }
            case 73: {
                return -14605274;
            }
            case 72: {
                return -2236963;
            }
            case 71: {
                return -14540254;
            }
            case 70: {
                return -9868951;
            }
            case 69: {
                return -11164965;
            }
            case 68: {
                return -13948117;
            }
            case 67: {
                return -3874313;
            }
            case 66: {
                return -9652901;
            }
            case 65: {
                return -1;
            }
            case 64: {
                return -13091262;
            }
            case 63: {
                return -7697782;
            }
            case 62: {
                return -1543503872;
            }
            case 61: {
                return -328966;
            }
            case 60: {
                return -14925725;
            }
            case 59: {
                return -3019777;
            }
            case 58: {
                return -328966;
            }
            case 57: {
                return -14338750;
            }
            case 56: {
                return -2102800402;
            }
            case 55: {
                return -16777216;
            }
            case 54: {
                return -13077596;
            }
            case 53: {
                return -10851462;
            }
            case 52: {
                return -9263664;
            }
            case 51: {
                return -5582866;
            }
            case 50: {
                return -14935012;
            }
            case 49: {
                return -11108183;
            }
            case 48: {
                return -668259541;
            }
            case 47: {
                return 1615417684;
            }
            case 46: {
                return -13143396;
            }
            case 45: {
                return -13077852;
            }
            case 44: {
                return -328966;
            }
            case 43: {
                return -14143949;
            }
            case 42: {
                return -11234874;
            }
            case 41: {
                return -11972524;
            }
            case 40: {
                return -8211748;
            }
            case 39: {
                return 402653183;
            }
            case 38: {
                return -2954241;
            }
            case 37: {
                return -986896;
            }
            case 36: {
                return 268435455;
            }
            case 35: {
                return -12829636;
            }
            case 34: {
                return -10653824;
            }
            case 33: {
                return -7960954;
            }
            case 32: {
                return -11972268;
            }
            case 31: {
                return -1979711488;
            }
            case 30: {
                return -1;
            }
            case 29: {
                return -3019777;
            }
            case 28: {
                return -1313793;
            }
            case 27: {
                return -11164965;
            }
            case 26: {
                return -14605274;
            }
            case 25: {
                return 251658239;
            }
            case 24: {
                return -14339006;
            }
            case 23: {
                return -9594162;
            }
            case 22: {
                return -11164709;
            }
            case 21: {
                return -1;
            }
            case 20: {
                return -8487298;
            }
            case 19: {
                return -1579033;
            }
            case 18: {
                return -8882056;
            }
            case 17: {
                return 1276090861;
            }
            case 16: {
                return -9342607;
            }
            case 14:
            case 15: {
                return -1;
            }
            case 13: {
                return -14339006;
            }
            case 12: {
                return -1;
            }
            case 11: {
                return -9276814;
            }
            case 10: {
                return -12401818;
            }
            case 9: {
                return -15921907;
            }
            case 8: {
                return -13803892;
            }
            case 7: {
                return -645885536;
            }
            case 6: {
                return -8224126;
            }
            case 5: {
                return 2047809827;
            }
            case 4: {
                return -11167525;
            }
            case 3: {
                return -10653824;
            }
            case 2: {
                return -9342607;
            }
            case 1: {
                return -1239540194;
            }
            case 0: {
                return -7697782;
            }
        }
    }
    
    public static Drawable getThemedDrawable(final Context context, final int n, final String s) {
        final Drawable mutate = context.getResources().getDrawable(n).mutate();
        mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(getColor(s), PorterDuff$Mode.MULTIPLY));
        return mutate;
    }
}
