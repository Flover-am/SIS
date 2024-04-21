package com.seciii.prism030.core.pojo.vo.news;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsSourceCountVO {
    private String source;
    private int count;
}
