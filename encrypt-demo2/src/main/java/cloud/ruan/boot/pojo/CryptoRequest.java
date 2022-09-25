package cloud.ruan.boot.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 加密的接口请求数据
 *
 * @author ruanzh.eth
 */
@Getter
@Setter
public class CryptoRequest {
    /**
     * 账户
     */
    @NotBlank(message = "账户不能为空")
    private String account;
    /**
     * 时间戳
     */
    @NotNull(message = "时间不能为空")
    private Long timestamp;
    /**
     * 签名 signature = md5(account={}&timestamp={}&data={})
     */
    @NotBlank(message = "签名不能为空")
    private String signature;
    /**
     * 数据体 加密后的接口请求数据
     */
    @NotBlank(message = "数据体不能为空")
    private String data;
}
