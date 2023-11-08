package www.bookstore.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.bookstore.com.entity.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author>findAuthorByName(String name);

    List<Author> findByActive(Integer value);

    Author findByIdAndActive(Long authorId, Integer value);
}
