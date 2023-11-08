package www.bookstore.com.dto.request;


import lombok.Data;

@Data
public class ReqUserInfo {

    private String imagePath;
    private String displayName;
    private String email;
    private String password;


}
