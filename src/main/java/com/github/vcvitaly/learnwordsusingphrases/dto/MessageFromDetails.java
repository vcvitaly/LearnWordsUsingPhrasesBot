package com.github.vcvitaly.learnwordsusingphrases.dto;

import lombok.Builder;

/**
 * MessageFromDetails.
 *
 * @author Vitalii Chura
 */
@Builder
public record MessageFromDetails(
        String username,
        String firstName,
        String lastName
) {
}
