package cloud.ruan.boot.aop;


import cloud.ruan.boot.annotation.Decrypt;
import cloud.ruan.boot.pojo.CryptoRequest;
import cloud.ruan.boot.util.ValidatorUtils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;


import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * API验签解密拦截器
 * 对客户端传入数据进行验签，解密，将解密后的实际数据传递给Controller
 *
 * @author ruanzh.eth
 * @see CryptoRequest 接口请求的格式
 */
@Slf4j
@ControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("Support method : " + methodParameter.hasMethodAnnotation(Decrypt.class));
        log.info("Support method parameter: " + methodParameter.hasParameterAnnotation(Decrypt.class));

        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
        // return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        // log.info("1 : " + parameter.hasMethodAnnotation(DecryptRequest.class));
        // log.info("2: " + parameter.hasParameterAnnotation(DecryptRequest.class));
        //
        // log.info("3: " + parameter.getContainingClass().isAnnotationPresent(DecryptRequest.class));
        // log.info("4: " + parameter.getMethod().isAnnotationPresent(DecryptRequest.class));
        // 在此处已完成参数绑定，在此处完成数据验签及解密即可

        Decrypt decryptRequest = parameter.getMethodAnnotation(Decrypt.class);
        if (decryptRequest != null) {
            InputStream inputStream = inputMessage.getBody();
            String requestBody = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
            CryptoRequest cryptoRequest = JSONUtil.toBean(requestBody, CryptoRequest.class);

            try {
                Set<ConstraintViolation<CryptoRequest>> set = ValidatorUtils.validateAll(cryptoRequest);
                if (set.size() > 0) {
                    throw new IllegalStateException(set.iterator().next().getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("接口数据无法解析!");
            }
            // timestamp 需在规定时间内
            long l = DateUtil.date().getTime() - cryptoRequest.getTimestamp();
            log.info("timestamp diff : {}", l);


        /*
        验签规则: signature = md5(account={}&timestamp={}&data={})
         */
            String str = StrUtil.format("account={}&timestamp={}&data={}", cryptoRequest.getAccount(), cryptoRequest.getTimestamp(), cryptoRequest.getData());
            String md5 = DigestUtil.md5Hex(str);
            if (!md5.equals(cryptoRequest.getSignature())) {
                throw new IllegalArgumentException("验签失败");
            }

            log.info("验签成功");

        /*
        解密data数据
         */
            String apiKey = "c5ad8a50d5494c48abe5a7239d97b904";
            AES aes = SecureUtil.aes(apiKey.getBytes());
            String json = aes.decryptStr(cryptoRequest.getData(), CharsetUtil.CHARSET_UTF_8);
        }

        return inputMessage;
    }


}
