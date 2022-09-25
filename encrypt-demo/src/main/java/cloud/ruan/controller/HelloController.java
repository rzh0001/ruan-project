package cloud.ruan.controller;

import cloud.ruan.boot.starter.annotation.Decrypt;
import cloud.ruan.boot.starter.annotation.Encrypt;
import cloud.ruan.boot.starter.pojo.RespBean;
import cloud.ruan.entity.User;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    public static void main(String[] args) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, "*www.ruan.cloud*".getBytes());
    }

    @GetMapping("/user")
    @Encrypt
    public RespBean getUser() {
        User user = new User();
        user.setId((long) 99);
        user.setUsername("javaboy");
        return RespBean.ok("ok", user);
    }

    @PostMapping("/user")
    public RespBean addUser(@RequestBody @Decrypt User user) {
        System.out.println("user = " + user);
        return RespBean.ok("ok", user);
    }
}