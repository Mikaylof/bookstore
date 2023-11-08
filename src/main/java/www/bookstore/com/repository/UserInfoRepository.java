package www.bookstore.com.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.bookstore.com.entity.Book;
import www.bookstore.com.entity.UserInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

    Optional<UserInfo> findUserInfoByEmail(String email);
    UserInfo findUserInfoByEmailAndActive(String email, Integer active);
    List<UserInfo>findAllByBookAndActive(Book book, Integer active);
    UserInfo findUserInfoByIdAndActive(Long id, Integer active);
}
