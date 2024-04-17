package com.seciii.prism030.core.enums;

/**
 * 词性标签枚举类
 * 参考: <a href="https://github.com/baidu/lac">lac</a>
 *
 * @author wang mingsong
 * @date 2024.04.14
 */
public enum SpeechPart {
    /* 名词类标签 */
    NOUN("n", "noun", "普通名词"),
    NOUN_DIRECTION("f", "noun_direction", "方位名词"),
    NOUN_LOCATION("s", "noun_location", "处所名词"),
    NOUN_WORKS("nw", "noun_works", "作品名词"),
    NOUN_OTHER("nz", "noun_other", "其他专名"),

    /* 动词类标签 */
    VERB("v", "verb", "普通动词"),
    VERB_AUXILIARY("vd", "verb_auxiliary", "动副词"),
    VERB_NOUN("vn", "verb_noun", "名动词"),

    /* 形容词类标签 */
    ADJECTIVE("a", "adjective", "形容词"),
    ADJECTIVE_ADVERB("ad", "adjective_adverb", "副形词"),
    ADJECTIVE_NOUN("an", "adjective_noun", "名形词"),
    ADVERB("d", "adverb", "副词"),

    /* 其他标签 */
    AMOUNT("m", "amount", "数量词"),
    QUANTIFIER("q", "quantifier", "量词"),

    PRONOUN("r", "pronoun", "代词"),
    PREPOSITION("p", "preposition", "介词"),
    CONJUNCTION("c", "conjunction", "连词"),
    AUXILIARY("u", "auxiliary", "助词"),
    FUNCTION("xc", "function", "其他虚词"),

    PUNCTUATION("w", "punctuation", "标点符号"),

    /* 特殊标签 */
    PERSONAL("PER", "personal", "人名"),
    LOCATION("LOC", "location", "地名"),
    ORGANIZATION("ORG", "organization", "机构名"),
    TIME("TIME", "time", "时间");


    SpeechPart(String tag, String name, String description) {
        this.tag = tag;
        this.name = name;
        this.description = description;
    }

    private final String tag;
    private final String name;
    private final String description;

    /**
     * 获取词性枚举
     *
     * @param tag 词性标签
     * @return 词性枚举
     */
    public static SpeechPart ofTag(String tag) {
        for (SpeechPart speechPart : SpeechPart.values()) {
            if (speechPart.tag.equals(tag)) {
                return speechPart;
            }
        }
        return null;
    }

    /**
     * 获取词性名称
     * @return 词性名称
     */
    public String toString() {
        return this.name;
    }
}
