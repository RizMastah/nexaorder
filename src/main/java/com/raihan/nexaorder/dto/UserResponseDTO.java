package com.raihan.nexaorder.dto;

import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public String getDisplayName() {
        if (name == null || name.isBlank()) {
            return "NexaOrder User";
        }

        if ("NexaOrder Customer".equalsIgnoreCase(name.trim())) {
            return "Ariel Hikmat";
        }

        return name;
    }

    public String getInitial() {
        String displayName = getDisplayName();
        if (displayName.isBlank()) {
            return "?";
        }

        return displayName.substring(0, 1).toUpperCase(Locale.ROOT);
    }
}
