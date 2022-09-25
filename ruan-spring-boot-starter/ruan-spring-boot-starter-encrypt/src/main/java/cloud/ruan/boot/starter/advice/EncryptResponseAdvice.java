package cloud.ruan.boot.starter.advice;

import cloud.ruan.boot.starter.annotation.Encrypt;
import cloud.ruan.boot.starter.config.EncryptProperties;
import cloud.ruan.boot.starter.pojo.RespBean;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<RespBean> {
    @Resource
    EncryptProperties encryptProperties;
//    private ObjectMapper om = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(Encrypt.class);
    }

    @Override
    public RespBean beforeBodyWrite(RespBean body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, encryptProperties.getKey().getBytes());
        byte[] keyBytes = encryptProperties.getKey().getBytes();
        try {
            if (body.getMsg() != null) {
                body.setMsg(aes.encryptHex(body.getMsg()));
            }
            if (body.getObj() != null) {
                body.setObj(aes.encryptHex(JSONUtil.toJsonPrettyStr(body.getObj())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }
}
