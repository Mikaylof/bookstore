package www.bookstore.com.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class RespResult {
    private String card;
    private String service;
    private String CardNumber;
    private Date cardDate;
    private Double amount;
}
