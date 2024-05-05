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
    /**
     * 更新后的新闻PO
     */
    private final NewsPO newsPO;
    /**
     * 更新后的新闻VO
     */
    private NewsVO newsVO;
    /**
     * 更新类型
     */
    @Getter
    private final UpdateType updateType;

    /**
     * 使用NewsPO的构造方法
     *
     * @param source     发布者
     * @param newsPO     待发布的修改后新闻PO
     * @param updateType 更新类型
     */
    public UpdateNewsEvent(Object source, NewsPO newsPO, UpdateType updateType) {
        super(source);
        this.newsPO = newsPO;
        this.updateType = updateType;
        this.newsVO = null;
    }

    /**
     * 使用NewsVO的构造方法
     *
     * @param source     发布者
     * @param newsVO     待发布的修改后新闻VO
     * @param updateType 更新类型
     */
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
