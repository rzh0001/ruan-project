package cloud.ruan.boot.pojo;



import lombok.Data;

import java.io.Serializable;

/**
 * @author Exrickx
 * 前后端交互数据标准
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;


    private boolean success;


    private String message;


    private Integer code;


    private long timestamp = System.currentTimeMillis();


    private T result;
}
