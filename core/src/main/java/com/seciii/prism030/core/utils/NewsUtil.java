package com.seciii.prism030.core.utils;

import com.seciii.prism030.core.enums.CategoryType;
import com.seciii.prism030.core.enums.SpeechPart;
import com.seciii.prism030.core.pojo.dto.NewsWordDetail;
import com.seciii.prism030.core.pojo.po.news.NewsPO;
import com.seciii.prism030.core.pojo.po.news.NewsSegmentPO;
import com.seciii.prism030.core.pojo.vo.news.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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


    public final static List<SpeechPart> ignoredParts = Arrays.asList(
            SpeechPart.ADVERB,
            SpeechPart.PRONOUN,
            SpeechPart.PREPOSITION,
            SpeechPart.CONJUNCTION,
            SpeechPart.AUXILIARY,
            SpeechPart.FUNCTION,
            SpeechPart.PUNCTUATION,
            SpeechPart.AMOUNT,
            SpeechPart.QUANTIFIER,
            SpeechPart.TIME
    );

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
                x -> CategoryType.of(x).toInt()
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
                        .sourceTime(DateTimeUtil.fromMongoStdToDefault(newsPO.getSourceTime()))
                        .category(CategoryType.of(newsPO.getCategory()).toString())
                        .link(newsPO.getLink())
                        .updateTime(DateTimeUtil.fromMongoStdToDefault(newsPO.getUpdateTime()))
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
                .sourceTime(DateTimeUtil.fromMongoStdToDefault(newsPO.getSourceTime()))
                .link(newsPO.getLink())
                .sourceLink(newsPO.getSourceLink())
                .category(CategoryType.of(newsPO.getCategory()).getCategoryEN())
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
        Integer category = null;
        if (CategoryType.of(newNews.getCategory()) != null) {
            category = CategoryType.of(newNews.getCategory()).toInt();
        }
        return NewsPO.builder()
                .title(newNews.getTitle())
                .content(newNews.getContent())
                .originSource(newNews.getOriginSource())
                .sourceTime(DateTimeUtil.toMongoStandardFormat(DateTimeUtil.defaultParse(newNews.getSourceTime())))
                .link(newNews.getLink())
                .sourceLink(newNews.getSourceLink())
                .category(category)
                .build();
    }

    /**
     * 将新闻分词PO转换为新闻分词VO
     *
     * @param newsSegmentPO 新闻分词PO
     * @return 新闻分词VO
     */
    public static NewsSegmentVO toNewsSegmentVO(NewsSegmentPO newsSegmentPO) {
        NewsWordVO[] newsWordVOS = Arrays.stream(newsSegmentPO.getContent()).map(
                x -> {
                    return NewsWordVO.builder()
                            .text(x.getText())
                            .count(x.getCount())
                            .build();
                }
        ).toArray(NewsWordVO[]::new);
        return NewsSegmentVO.builder()
                .id(newsSegmentPO.getId())
                .content(newsWordVOS)
                .build();
    }

    /**
     * 过滤新闻分词结果
     *
     * @param newsWordDetails 新闻分词结果
     * @return 过滤后的新闻分词结果
     */
    public static List<NewsWordDetail> filterNewsWordDetail(NewsWordDetail[] newsWordDetails) {
        List<NewsWordDetail> resultList = new ArrayList<>();
        for (NewsWordDetail newsWordDetail : newsWordDetails) {
            if (!(newsWordDetail.getText().contains("\n") // 含有换行符
                    || Pattern.compile("[\\p{P}\\p{S}]").matcher(newsWordDetail.getText()).find() // 含有标点符号
                    || newsWordDetail.getRank() <= 1 // 词语重要性小于等于1
                    || newsWordDetail.getPartOfSpeech() == null // 词性为空
                    || ignoredParts.contains(newsWordDetail.getPartOfSpeech()) // 词性为忽略词性
            )
            ) {
                newsWordDetail.setText(newsWordDetail.getText().replaceAll("\\s", ""));
                if (newsWordDetail.getText().length() > 1) {
                    resultList.add(newsWordDetail);
                }
            }
        }
        return resultList;
    }
}
