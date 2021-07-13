package com.hz.design.builder.factory;

import com.hz.design.builder.factory.service.CardCommodityService;
import com.hz.design.builder.factory.service.CouponCommodityService;
import com.hz.design.builder.factory.service.GoodCommodityService;

/**
 * 定义一个商品的工厂类 在里面按照类型实现各种商品的服务
 */
public class StoreFactory {
    public ICommodity getCommodityService(Integer commodityType) {
        if (null == commodityType) return null;
        if (1 == commodityType) return new CouponCommodityService();
        if (2 == commodityType) return new GoodCommodityService();
        if (3 == commodityType) return new CardCommodityService();
        throw new RuntimeException("不存在的商品服务类型");
    }
}

