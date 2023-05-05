package com.fpdual.web.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Notification {
    private int id;
    private String title;
    private String body;
}

