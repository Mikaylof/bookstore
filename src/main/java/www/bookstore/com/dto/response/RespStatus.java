package www.bookstore.com.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespStatus {
    private Integer code;
    private String msj;
    private static Integer SUCCESS_CODE = 1;
    private static String MESSAGE_CODE = "SUCCESS";

    public static RespStatus getMessage() {

        return new RespStatus(SUCCESS_CODE, MESSAGE_CODE);
    }
}
