package com.binzaijun.stock.service;

import com.binzaijun.stock.domain.AlarmParamsDTO;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PushMessageService{

    private  String appid="wx86544b4e9df75806";
    private  String secret="7d88afbf1237f460834cdd5902735aaa";

    /**
     * 给微信公众号某个用户推送信息
     * @param alarmParamsDTO
     */
    public void pushMessage(AlarmParamsDTO alarmParamsDTO) {
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appid);
        wxStorage.setSecret(secret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        List<WxMpTemplateData> wxMpTemplateData = Arrays.asList(
                new WxMpTemplateData("first",alarmParamsDTO.getFirst(),"#000000"),
                new WxMpTemplateData("keyword1",alarmParamsDTO.getKeyword1(),"#000080"),
                new WxMpTemplateData("keyword2",alarmParamsDTO.getKeyword2(),"#0000FF"),
                new WxMpTemplateData("keyword3",alarmParamsDTO.getKeyword3(),"#FFD700"),
                new WxMpTemplateData("remark",alarmParamsDTO.getRemark(),"#00FF00")
        );
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(alarmParamsDTO.getOpenId())
                .templateId("gEFCL7TfdTZne9tgjeIcOFNzS6zayHmQUe7LpieaHd4")
                .data(wxMpTemplateData)
                .url("http://www.baidu.com")
                .build();
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
        }

    }
}

