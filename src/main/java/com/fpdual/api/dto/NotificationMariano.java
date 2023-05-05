package com.fpdual.api.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class NotificationMariano {
    private int id;
    private String title;
    private String body;
}

