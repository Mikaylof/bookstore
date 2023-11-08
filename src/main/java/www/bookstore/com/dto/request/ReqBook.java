package www.bookstore.com.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import www.bookstore.com.entity.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqBook {
    private Long bookId;
    private String name;
    private Author authorId;
}
