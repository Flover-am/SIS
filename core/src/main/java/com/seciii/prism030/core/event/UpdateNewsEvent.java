package com.seciii.prism030.core.event;

import com.seciii.prism030.core.enums.UpdateType;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;
import com.seciii.prism030.core.utils.NewsUtil;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 修改新闻事件
 *
 * @author wangmingsong
 * @date 2024.04.30
 */
public class UpdateNewsEvent extends ApplicationEvent {

    private final NewsPO newsPO;
    private NewsVO newsVO;
    @Getter
    private final UpdateType updateType;

    public UpdateNewsEvent(Object source, NewsPO newsPO, UpdateType updateType) {
        super(source);
        this.newsPO = newsPO;
        this.updateType = updateType;
        this.newsVO = null;
    }

    public UpdateNewsEvent(Object source, NewsVO newsVO, UpdateType updateType) {
        super(source);
        this.newsVO = newsVO;
        this.updateType = updateType;
        this.newsPO = null;
    }

    /**
     * 获取更新时间的新闻VO
     *
     * @return 新闻VO
     */
    public NewsVO getNewsVO() {
        if (newsVO == null) {
            newsVO = NewsUtil.toNewsVO(newsPO);
        }
        return newsVO;
    }

}
