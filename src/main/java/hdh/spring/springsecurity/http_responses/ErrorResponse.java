package hdh.spring.springsecurity.http_responses;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class ErrorResponse<T> extends BaseResponse<T> {
    public ErrorResponse(String message) {
        super(LocalDateTime.now(), "Error", List.of(message), null);
    }

    public ErrorResponse(List<String> messages) {
        super(LocalDateTime.now(), "Error", messages, null);
    }

    public ErrorResponse(T data, String message) {
        super(LocalDateTime.now(), "Error", List.of(message), data);
    }

    public ErrorResponse(T data, List<String> message) {
        super(LocalDateTime.now(), "Error", message, data);
    }

}
