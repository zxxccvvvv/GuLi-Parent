package com.atguigu.guli.service.trade.mapper;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2020-10-16
 */
public interface OrderMapper extends BaseMapper<Order> {

    void updateStatusByOrderNo(String orderNo);
}
