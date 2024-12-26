package com.new_era.alpha.services.utils;

import com.new_era.alpha.services.dto.ClanInvitationNotificationDTO;
import com.new_era.alpha.services.dto.ClanKikedDTO;
import com.new_era.alpha.services.dto.NotificationDTO;
import com.new_era.alpha.services.messages.player.NotificationMessages;

public class NotificationFactory {
    
    public static NotificationDTO createClanInviteNotification(ClanInvitationNotificationDTO dto) {
        String message = String.format(
                NotificationMessages.CLAN_INVITE_MESSAGE,
                dto.clan_tag(),
                dto.clan_name(),
                dto.inviter_name());

        String title = NotificationMessages.CLAN_INVITE_TITLE;
        String callbackForAccept = String.format("/clan/accept/%d", dto.invite_id());
        String callbackForDecline = String.format("/clan/decline/%d", dto.invite_id());
        String iconUrl = "/";
        String href = "";

        return new NotificationDTO(dto.player_id(), title, message, iconUrl, href, callbackForAccept,
                callbackForDecline);
    }

    public static NotificationDTO createKikedFromClanNotification(ClanKikedDTO dto) {
        String message = String.format(NotificationMessages.CLAN_KIKED_MESSAGE,
            dto.clan_tag(),
            dto.clan_name(),
            dto.initiator_name()
            );

        String title = NotificationMessages.CLAN_KIKED_TITLE;
        String callbackForAccept = "";
        String callbackForDecline = "";
        String href = "";
        String iconUrl = "";

        return new NotificationDTO(dto.target_id(), title, message, iconUrl, href, callbackForAccept, callbackForDecline);

        
    }
}
