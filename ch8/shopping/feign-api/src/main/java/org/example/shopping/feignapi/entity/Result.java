package org.example.shopping.feignapi.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order
 * @ClassName : .java
 * @createTime : 2023/7/14 8:35
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Data
@NoArgsConstructor
public class Result<T> {
    T data;
    @NonNull
    String errorMsg;
    @NonNull
    String errorCode;
    boolean success;
    private static final String DEFAULT_ERROR_CODE = "error.default";

    private Result(T data, @NonNull String errorMsg, @NonNull String errorCode, boolean success) {
        this.data = data;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
        this.success = success;
    }

    public static <D> Result<D> success(D data) {
        return new Result<>(data, "", "", true);
    }

    public static Result<Object> fail(String errorMsg, String errorCode) {
        return new Result<>(null, errorMsg, errorCode, false);
    }

    public static Result<Object> fail(String errorMsg) {
        return fail(errorMsg, DEFAULT_ERROR_CODE);
    }

    @SneakyThrows
    public static <D> D parseData(Result<?> result, Class<D> dataCls) {
        if (result == null || result.getData() == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result.getData());
        return objectMapper.readValue(json, dataCls);
    }
}
