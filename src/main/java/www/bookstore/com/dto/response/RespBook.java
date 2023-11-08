package www.bookstore.com.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import www.bookstore.com.entity.Author;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespBook {
    private Long bookId;
    private String name;
    private Author authorId;
    private RespUserDetail respUserDetail;
}
