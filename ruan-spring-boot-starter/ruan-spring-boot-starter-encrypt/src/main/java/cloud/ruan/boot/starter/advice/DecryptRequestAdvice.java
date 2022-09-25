package cloud.ruan.boot.starter.advice;

import cloud.ruan.boot.starter.annotation.Decrypt;
import cloud.ruan.boot.starter.config.EncryptProperties;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {

    @Resource
    private EncryptProperties properties;

    public static void main(String[] args) {
        System.out.println(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()));
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        byte[] body = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(body);
        try {
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, properties.getKey().getBytes());
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(aes.decrypt(body));
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return byteArrayInputStream;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
