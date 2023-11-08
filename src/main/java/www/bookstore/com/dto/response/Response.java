package www.bookstore.com.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Response <T>{
    @JsonProperty(value = "resp")
    private T t;
    @JsonProperty(value = "status")
    private RespStatus respStatus;


}
