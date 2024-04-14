package com.seciii.prism030.core.decorator.segment;

import com.seciii.prism030.core.pojo.dto.NewsWordDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

@SpringBootTest
public class SegmentTest {
    @Autowired
    TextSegment textSegment;
    @Test
    void segmentTest() {
        String text="万科于4月14日下午召开线下投资者会议，就舆情提及出国不回的领导蔡平、王润川是什么情况？有传闻华中区首蔡平去美不归之后，万科所有集团副总裁及以上级别，目前都被边控了。万科方面称，“实际情况是，原华中区域首席合伙人蔡平，孩子在美国出生，现在到了接受教育的阶段，需要家庭陪伴。其于 2023 年提出辞职并获得公司同意。原总部协同中心牵头合伙人王润川，因前往香港深造提出离职，目前人常住深圳。集团管理层境外公务出行正常进行。集团总裁 祝九胜今天刚刚从香港考察项目回来。集团联席总 裁朱保全今日（4月14日）中午飞往日本进行业务 考察。”";
        String[] res=textSegment.segment(text);
        System.out.println(Arrays.toString(res));
        NewsWordDetail[] rankRes=textSegment.rank(text);

        for(NewsWordDetail word:rankRes){
            System.out.println("["+word.getText()+","+word.getPartOfSpeech()+","+word.getRank()+"]");
        }
    }
}
