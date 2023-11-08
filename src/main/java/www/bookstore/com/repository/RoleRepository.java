package www.bookstore.com.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.bookstore.com.entity.Role;


import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findRoleByRoleAndActive(String role, Integer active);

    Optional<Role> findRoleByRole(String roleName);

    ;

}
