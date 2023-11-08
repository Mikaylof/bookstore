package www.bookstore.com.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespUserDetail {

    private String imagePath;
    private String displayName;
    private String email;
    private RespBook respBook;
    private Collection<String> roles=new ArrayList<>();
}
