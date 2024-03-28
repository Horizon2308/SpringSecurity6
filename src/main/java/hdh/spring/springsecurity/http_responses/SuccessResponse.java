package hdh.spring.springsecurity.http_responses;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class SuccessResponse<T> extends BaseResponse<T> {
    public SuccessResponse(List<String> messages) {
        super(LocalDateTime.now(), "Success", messages, null);
    }

    public SuccessResponse(String message) {
        super(LocalDateTime.now(), "Success", List.of(message), null);
    }

    public SuccessResponse(T data, String message) {
        super(LocalDateTime.now(), "Success", List.of(message), data);
    }

    public SuccessResponse(T data, List<String> messages) {
        super(LocalDateTime.now(), "Success", messages, data);
    }
}
