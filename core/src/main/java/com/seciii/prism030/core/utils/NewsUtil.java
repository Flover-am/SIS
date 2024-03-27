package com.seciii.prism030.core.utils;

import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.vo.news.NewNews;
import com.seciii.prism030.core.pojo.vo.news.NewsItemVO;
import com.seciii.prism030.core.pojo.vo.news.NewsVO;

import java.util.List;

/**
 * 新闻工具类
 *
 * @author wang mingsong
 * @date 2024.03.22
 */
public class NewsUtil {
    public final static String ID = "_id";
    public final static String TITLE = "title";
    public final static String CONTENT = "content";
    public final static String SOURCE = "origin_source";
    public final static String SOURCE_TIME = "source_time";
    public final static String CATEGORY = "category";
    public final static String UPDATE_TIME = "update_time";
    public final static String CREATE_TIME = "create_time";
    private NewsUtil() {
    }

    /**
     * 将字符串拆分为单字
     *
     * @param s 待拆分的字符串
     * @return 拆分后的字符串
     */
    public static String splitString(String s) {
        if (s == null) {
            return null;
        }
        String[] split = s.split("");
        return String.join("%", split);
    }

    /**
     * 将字符串类别列表转换为其对应下标列表
     *
     * @param category 字符串类别列表
     * @return 下标列表
     */
    public static List<Integer> getCategoryTypeList(List<String> category) {
        if (category == null || category.isEmpty()) {
            return null;
        }
        return category.stream().map(
                x -> CategoryType.getCategoryType(x).toInt()
        ).toList();
    }

    /**
     * 将新闻PO列表转换为新闻条目VO列表
     *
     * @param newsPOList 新闻PO列表
     * @return 新闻条目VO列表
     */
    public static List<NewsItemVO> toNewsVO(List<NewsPO> newsPOList) {
        return newsPOList.stream().map(
                newsPO -> NewsItemVO.builder()
                        .id(newsPO.getId())
                        .title(newsPO.getTitle())
                        .originSource(newsPO.getOriginSource())
                        .sourceTime(DateTimeUtil.defaultFormat(newsPO.getSourceTime()))
                        .category(CategoryType.getCategoryType(newsPO.getCategory()).getCategoryEN())
                        .link(newsPO.getLink())
                        .updateTime(DateTimeUtil.defaultFormat(newsPO.getUpdateTime()))
                        .build()
        ).toList();
    }

    /**
     * 将新闻PO转换为新闻VO
     *
     * @param newsPO 新闻PO
     * @return 新闻VO
     */
    public static NewsVO toNewsVO(NewsPO newsPO) {
        return NewsVO.builder()
                .id(newsPO.getId())
                .title(newsPO.getTitle())
                .content(newsPO.getContent())
                .originSource(newsPO.getOriginSource())
                .sourceTime(DateTimeUtil.defaultFormat(newsPO.getSourceTime()))
                .link(newsPO.getLink())
                .sourceLink(newsPO.getSourceLink())
                .category(CategoryType.getCategoryType(newsPO.getCategory()).getCategoryEN())
                .createTime(newsPO.getCreateTime())
                .updateTime(newsPO.getUpdateTime())
                .build();
    }

    /**
     * 将新闻NewNews对象转换为新闻PO
     *
     * @param newNews 新闻NewNews对象
     * @return 新闻PO
     */
    public static NewsPO toNewsPO(NewNews newNews) {
        return NewsPO.builder()
                .title(newNews.getTitle())
                .content(newNews.getContent())
                .originSource(newNews.getOriginSource())
                .sourceTime(DateTimeUtil.defaultParse(newNews.getSourceTime()))
                .link(newNews.getLink())
                .sourceLink(newNews.getSourceLink())
                .category(CategoryType.getCategoryType(newNews.getCategory()).toInt())
                .build();
    }
}
