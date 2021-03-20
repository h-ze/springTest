package com.hz.config;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 对密码进行加密
 */
public class TestShiroMd5 {
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123","123",1024);
        System.out.println(md5Hash.toHex());
    }
}
