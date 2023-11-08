package www.bookstore.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.bookstore.com.entity.Author;
import www.bookstore.com.entity.Book;
import www.bookstore.com.entity.UserInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findByActive(Integer value);
    Optional<Book> findByNameAndActive(String bookName, Integer active);

    List<Book> findAllByAuthorAndActive(Author author, Integer active);
    Book findByIdAndActive(Long bookId,Integer active);
    List<Book>findAllByUserInfoAndActive(UserInfo userInfo, Integer active);
}
