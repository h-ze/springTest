package com.hz.design.builder.factory;

import java.util.Map;

/**
 * 工厂模式 (创建型设计模式) 在父类中提供一个创建对象的方法，允许子类决定实例化对象的类型
 *
 *
 */

public interface ICommodity {

    /**
     * 所有的奖品无论是实物 虚拟还是第三方 都需要通过我们的程序实现此接口进行处理，以保证最终入参出参的统一性
     * @param uId 用户id
     * @param commodityId 奖品id
     * @param bizId 业务id
     * @param extMap 扩展字段用于处理发放实物商品时的收获地址
     * @throws Exception
     */
    void sendCommodity(String uId, String commodityId, String bizId,
                       Map<String, String> extMap) throws Exception;
}
