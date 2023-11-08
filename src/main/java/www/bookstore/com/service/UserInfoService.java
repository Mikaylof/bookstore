package www.bookstore.com.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import www.bookstore.com.dto.request.ReqUserInfo;
import www.bookstore.com.dto.request.ReqUserMail;
import www.bookstore.com.dto.response.*;
import www.bookstore.com.entity.Author;
import www.bookstore.com.entity.Book;
import www.bookstore.com.entity.Role;
import www.bookstore.com.entity.UserInfo;
import www.bookstore.com.exception.EnumCode;
import www.bookstore.com.exception.ExceptionConstants;
import www.bookstore.com.exception.MyException;
import www.bookstore.com.repository.AuthorRepository;
import www.bookstore.com.repository.BookRepository;
import www.bookstore.com.repository.RoleRepository;
import www.bookstore.com.repository.UserInfoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserInfoService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private BookRepository bookRepository;


    @Autowired
    private AuthorRepository authorRepository;

    public RespStatusList saveUserInfo(ReqUserInfo reqUserInfo) {
        RespStatusList respStatusList = new RespStatusList();
        String name = reqUserInfo.getDisplayName();
        String username = reqUserInfo.getEmail();
        try {
            if (name == null || username == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "request fail");
            }
            Optional<UserInfo> optional = userInfoRepository.findUserInfoByEmail(username);
            if (optional.isPresent()) {
                throw new MyException(ExceptionConstants.DATA_TAKEN, "account is available");
            }
            UserInfo user = new UserInfo();
            user.setImagePath(reqUserInfo.getImagePath());
            user.setDisplayName(reqUserInfo.getDisplayName());
            user.setEmail(reqUserInfo.getEmail());
            user.setPassword(passwordEncoder.encode(reqUserInfo.getPassword()));
            userInfoRepository.save(user);
            respStatusList.setStatus(RespStatus.getMessage());
        } catch (MyException e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal error"));
        }
        return respStatusList;
    }

    public RespStatusList addRoleToUser(String email, String role) throws MyException {
        RespStatusList respStatusList = new RespStatusList();
        try {
            UserInfo user = userInfoRepository.findUserInfoByEmailAndActive(email, EnumCode.Active.getValue());
            if (user == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "user not found");
            }
            Role r = roleRepository.findRoleByRoleAndActive(role, EnumCode.Active.getValue());
            if (r == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "role not found");
            }
            user.getRoles().add(r);
            userInfoRepository.save(user);
            respStatusList.setStatus(RespStatus.getMessage());
        } catch (MyException e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal error"));
        }
        return respStatusList;

    }


    public Response<RespUserDetail> getUserInfo(ReqUserMail reqUserMail) {

        Response<RespUserDetail> response = new Response<>();
        log.info("get username ..... .... .... " + reqUserMail.getUsername());

        try {
            if (reqUserMail == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "invalid request");
            }
            Optional<UserInfo> userInfo = userInfoRepository.findUserInfoByEmail(reqUserMail.getUsername());
            RespUserDetail respUserDetail = new RespUserDetail();
            respUserDetail.setImagePath(userInfo.get().getImagePath());
            respUserDetail.setDisplayName(userInfo.get().getDisplayName());
            respUserDetail.setRoles(userInfo.get().getRoles().stream()
                    .map(role -> new String(role.getRole())).collect(Collectors.toList()));
            response.setT(respUserDetail);
            response.setRespStatus(RespStatus.getMessage());
        } catch (MyException e) {
            e.printStackTrace();
            response.setRespStatus(new RespStatus(e.getCode(), e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            response.setRespStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "invalid request"));
        }
        return response;
    }

    public RespStatusList addBookToUser(String email, Long bookId) {
        RespStatusList respStatusList = new RespStatusList();
        try {
            UserInfo u = userInfoRepository.findUserInfoByEmailAndActive(email, EnumCode.Active.getValue());
            if (u == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "user not found");
            }
            Book book = bookRepository.findByIdAndActive(bookId, EnumCode.Active.getValue());
            if (book == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "book not found");
            }
            u.getBooksId().add(book);
            userInfoRepository.save(u);
            respStatusList.setStatus(RespStatus.getMessage());
        } catch (MyException e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal error"));
        }
        return respStatusList;

    }

    public RespStatusList addAuthorToUser(String email, Long authorId) {
        RespStatusList respStatusList = new RespStatusList();
        try {
            UserInfo userInfo = userInfoRepository.findUserInfoByEmailAndActive(email, EnumCode.Active.getValue());
            if (userInfo == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "user not found");
            }
            Author author = authorRepository.findByIdAndActive(authorId, EnumCode.Active.getValue());
            if (author == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "author not found");
            }
            userInfo.getAuthorsId().add(author);
            userInfoRepository.save(userInfo);
            respStatusList.setStatus(RespStatus.getMessage());
        } catch (MyException e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal error"));
        }
        return respStatusList;
    }

    public Response<List<RespUserDetail>> getUserListByBookId(Long bookId) {
        Response<List<RespUserDetail>> response = new Response<>();
        try {
            if (bookId == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "Invalid Request Data");
            }
            Book book = bookRepository.findByIdAndActive(bookId, EnumCode.Active.getValue());
            if (book == null) {
                throw new MyException(ExceptionConstants.BOOK_NOT_FOUND, "Book not found");
            }
            List<UserInfo> userList = userInfoRepository.findAllByBookAndActive(book, EnumCode.Active.getValue());
            if (userList.isEmpty()) {
                throw new MyException(ExceptionConstants.READER_NOT_FOUND, "Reader not found");
            }
            List<RespUserDetail> respUserDetailList = userList.stream().map(this::mapping).collect(Collectors.toList());
            response.setT(respUserDetailList);
            response.setRespStatus(RespStatus.getMessage());
        } catch (MyException e) {
            e.printStackTrace();
            response.setRespStatus(new RespStatus(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal error"));
        }
        return response;
    }

    private RespUserDetail mapping(UserInfo userInfo) {
        RespBook respBook = RespBook.builder()
                .name(userInfo.getBook().getName())
                .build();
        RespUserDetail respUserDetail = RespUserDetail.builder()
                .displayName(userInfo.getDisplayName())
                .email(userInfo.getEmail())
                .respBook(respBook)
                .build();
        return respUserDetail;
    }
}
