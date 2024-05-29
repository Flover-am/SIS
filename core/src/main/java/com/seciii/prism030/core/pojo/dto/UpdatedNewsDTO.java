package com.seciii.prism030.core.pojo.dto;

import com.seciii.prism030.core.enums.UpdateType;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import lombok.Data;

/**
 * 更新后的新闻DTO
 *
 * @author wang mingsong
 * @date 2024.05.13
 */
@Data
public class UpdatedNewsDTO {
    /**
     * 更新的新闻VO
     */
    private final NewsVO newsVO;

    /**
     * 更新类型
     */
    private final UpdateType updateType;

    public UpdatedNewsDTO(NewsVO newsVO, UpdateType updateType) {
        this.newsVO = newsVO;
        this.updateType = updateType;
    }
}
