package com.binzaijun.stock.controller;

import com.binzaijun.stock.domain.AlarmParamsDTO;
import com.binzaijun.stock.service.PushMessageService;
import com.binzaijun.stock.util.CheckoutUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping(value = "wx")
public class WxController{

    @Resource
    PushMessageService pushMessageService;

    /**
     * 公众号appid
     */
    @Value("${wx.appid}")
    private  String appid;

    /**
     * 公众号appSecret
     */
    @Value("${wx.secret}")
    private  String secret;

    /**
     * 向每个用户推送消息
     * @return
     */
    @GetMapping("/sendMessage")
    @ResponseBody
    public String sendMessage() {
        String openId = "oA1Ee66X_GxHG8ZvZGtO6LfKw1eY";
        if (!"".equals(openId)) {
            AlarmParamsDTO dto = new AlarmParamsDTO("申请进度", "国家奖学金", "申请通过", "", "成功");
            dto.setOpenId(openId);
            pushMessageService.pushMessage(dto);
        }
        return "success";
    }

    /**
     * 微信消息接收和token验证
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/weChatToken")
    public void weChat(HttpServletRequest request, HttpServletResponse response) {
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
                try {
                    PrintWriter print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}