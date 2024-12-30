package com.mdd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String username;
    private Date createdAt;
}
