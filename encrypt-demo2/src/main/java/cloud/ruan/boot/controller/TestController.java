package cloud.ruan.boot.controller;

import cloud.ruan.boot.annotation.Encrypt;
import cloud.ruan.boot.pojo.Result;
import cloud.ruan.boot.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @author ruanzh.eth
 */
@Slf4j
@RestController
@RequestMapping("/")
public class TestController {

    @Encrypt
    @GetMapping("/get")
    public Result get(){
        return ResultUtil.data("123");
    }

    @PostMapping("/xboot/auth/terminal/login")
    public Result login() {
        return ResultUtil.data("qeqweqwe");
    }

    // @EncryptResponse
    // @DecryptRequest


    @RequestMapping(value = "/api/auto/terminal/info", method = RequestMethod.POST)
    public Result info(String accountId) {
        log.info(accountId);
        if (accountId == null) {
            throw new IllegalArgumentException("Invalid account id");
        }
        return ResultUtil.data("11111");
    }
}
