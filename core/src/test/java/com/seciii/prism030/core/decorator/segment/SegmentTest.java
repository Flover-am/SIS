package com.seciii.prism030.core.decorator.segment;

import com.seciii.prism030.core.pojo.dto.NewsWordDetail;
import com.seciii.prism030.core.utils.NewsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class SegmentTest {
    @Autowired
    TextSegment textSegment;

    @Test
    void segmentTest() {
        String text = "欧洲央行管委Yannis Stournaras表示，央行必须在8月夏季休假前降息两次，然后在年底前再降息两次，不要受美联储影响。\n" +
                "　　“我们需要尽快开始降息，这样我们的货币政策才不会过于限制性，”这位希腊央行行长在伦敦接受采访时说。“在夏季休假前降息两次是合适的，全年降息四次似乎也是合理的。就此而言，我认同市场的预期。”\n" +
                "　　欧洲央行上周连续第四次会议维持政策不变，官员们认为6月左右可能是启动货币宽松的合适时机。他们对通胀率朝着2%目标迈进更有信心，但在做出降息决定前还寻求进一步的保证。\n" +
                "　　欧洲央行定于4月11日、6月6日和7月18日发布政策决定。此后，要到9月12日才会再次召开政策会议。Stournaras在央行管委会中属于知名鸽派，但他最近与许多鹰派成员都认为有必要等到6月。\n" +
                "　　Stournaras称，“欧元区经济增长远弱于预期，风险偏向下行；通胀已显著回落，面临的风险平衡”。\n" +
                "　　他还淡化了名义薪资增速依然强劲的状况，强调实际薪资要到2025年才能恢复到疫情前的水平。\n" +
                "责任编辑：刘明亮";
        if(text.contains("责任编辑")){
            text=text.substring(0,text.indexOf("责任编辑"));
        }
        String[] res = textSegment.segment(text);
        System.out.println(Arrays.toString(res));
        NewsWordDetail[] rankRes = textSegment.rank(text);
        NewsUtil.filterNewsWordDetail(rankRes).forEach(
                x->{
                    System.out.println("[" + x.getText() + "," + x.getPartOfSpeech() + "," + x.getRank() + "]");
                }
        );
    }
}
