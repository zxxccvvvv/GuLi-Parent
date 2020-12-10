package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.common.util.StreamUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.WeixinPayService;
import com.atguigu.guli.service.trade.util.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(tags = "网站微信支付")
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @Autowired
    private OrderService orderService;

    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        Map map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    /**
     * 支付回调
     */
    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request) throws Exception {

        System.out.println("微信发送的回调");

        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");

        //验签：校验签名
        if (WXPayUtil.isSignatureValid(notifyXml, weixinPayProperties.getPartnerKey())) {

            System.out.println("验签成功");
            //验签成功，执行下一步

            //解析xml数据
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);

            //判断通信和业务是否成功
            if ("SUCCESS".equals(notifyMap.get("return_code")) && "SUCCESS".equals(notifyMap.get("result_code"))) {
                //获取商户订单号
                String orderNo = notifyMap.get("out_trade_no");
                //根据订单号获取商户订单数据
                Order order = orderService.getOrderByOrderNo(orderNo);
                //并校验返回的订单金额是否与商户侧的订单金额一致
                if (order != null && order.getTotalFee() == Long.parseLong(notifyMap.get("total_fee"))) {

                    //幂等性：无论接口被调用多少次，产生的结果是一致的
                    //接口调用的幂等性原则（保持接口调用的幂等性）
                    //首先检查订单状态（目的：防止应答微信端没有及时收到）
                    synchronized (this) {
                        if (order.getStatus() == 0 ) {
                            //修改订单状态 //记录支付日志
                            orderService.updateOrderStatus(notifyMap);
                        }
                    }

                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("return_code", "SUCCESS");
                    resultMap.put("return_msg", "OK");
                    String returnXml = WXPayUtil.mapToXml(resultMap);
                    System.out.println("支付成功，已应答");
                    return returnXml;
                }
            }
        }

        //给微信返回失败应答
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code", "FAIL");
        returnMap.put("return_msg", "失败");
        String returnXml = WXPayUtil.mapToXml(returnMap);
        return returnXml;
    }
}
