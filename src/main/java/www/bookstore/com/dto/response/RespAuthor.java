package www.bookstore.com.dto.response;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import www.bookstore.com.entity.Book;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespAuthor {
    private String name;
    private int age;
    private Collection<Book> authoredBooksId = new ArrayList<>();
}
