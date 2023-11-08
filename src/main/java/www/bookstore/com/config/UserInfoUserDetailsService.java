package www.bookstore.com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import www.bookstore.com.entity.UserInfo;
import www.bookstore.com.exception.EnumCode;
import www.bookstore.com.repository.UserInfoRepository;


@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findUserInfoByEmailAndActive(email, EnumCode.Active.getValue());
        if(userInfo==null){
            throw new UsernameNotFoundException("user not found: "+userInfo);
        }
        return new UserInfoUserDetails(userInfo);

    }
}
