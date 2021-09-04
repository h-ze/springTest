package com.hz.design;

import com.hz.design.builder.abstractfactory.CacheService;
import com.hz.design.builder.abstractfactory.factory.JDKProxy;
import com.hz.design.builder.abstractfactory.factory.impl.EGMCacheAdapter;
import com.hz.design.builder.abstractfactory.factory.impl.IIRCacheAdapter;
import com.hz.design.builder.abstractfactory.impl.CacheServiceImpl;
import com.hz.design.builder.builder.Builder;
import com.hz.design.builder.factory.ICommodity;
import com.hz.design.builder.factory.StoreFactory;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
public class BuilderTest {

    //工厂模式
    @Test
    public void test_commodity() throws Exception {
        StoreFactory storeFactory = new StoreFactory();
        //1.优惠券
        ICommodity commodityService_1 = storeFactory.getCommodityService(1);
        commodityService_1.sendCommodity("10001", "EGM1023938910232121323432",
                "791098764902132", null);
        //2.实物商品
        ICommodity commodityService_2 = storeFactory.getCommodityService(2);
        Map<String,String> extMap = new HashMap<String,String>();
        extMap.put("consigneeUserName", "谢飞机");
        extMap.put("consigneeUserPhone", "15200292123");
        extMap.put("consigneeUserAddress", "测试地址");
        commodityService_2.sendCommodity("10001","9820198721311","102300002011222 1113", extMap);
        // 3.第三方兑换卡(爱奇艺)
        ICommodity commodityService_3 = storeFactory.getCommodityService(3);
        commodityService_3.sendCommodity("10001","AQY1xjkUodl8LO975GdfrYUio",null,null);
    }

    //抽象工厂模式
    @Test
    public void test_CacheService() throws Exception {
        CacheService proxy_EGM = JDKProxy.getProxy(CacheServiceImpl.class, new EGMCacheAdapter());
        proxy_EGM.set("user_name_01","小付哥");
        String val01 = proxy_EGM.get("user_name_01");
        System.out.println(val01);
        CacheService proxy_IIR = JDKProxy.getProxy(CacheServiceImpl.class, new IIRCacheAdapter());
        proxy_IIR.set("user_name_01","小付哥");
        String val02 = proxy_IIR.get("user_name_01");
        System.out.println(val02);
    }

    //创建者模式
    @Test
    public void test_Builder(){
        Builder builder = new Builder();
        // 豪华欧式
        System.out.println(builder.levelOne(132.52D).getDetail());
        // 轻奢田园
        System.out.println(builder.levelTwo(98.25D).getDetail());
        // 现代简约
        System.out.println(builder.levelThree(85.43D).getDetail());
    }


}
