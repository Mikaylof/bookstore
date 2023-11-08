package www.bookstore.com.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import www.bookstore.com.dto.request.ReqBook;
import www.bookstore.com.dto.response.*;
import www.bookstore.com.entity.Author;
import www.bookstore.com.entity.Book;
import www.bookstore.com.entity.UserInfo;
import www.bookstore.com.exception.EnumCode;
import www.bookstore.com.exception.ExceptionConstants;
import www.bookstore.com.exception.MyException;
import www.bookstore.com.repository.AuthorRepository;
import www.bookstore.com.repository.BookRepository;
import www.bookstore.com.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private JavaMailSender javaMailSender;


    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mikayilzademusviq98@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public Response<List<RespBook>> getBookList() {
        Response<List<RespBook>> response = new Response<>();
        List<RespBook> list = new ArrayList<>();
        try {
            List<Book> books = bookRepository.findByActive(EnumCode.Active.getValue());
            if (books == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "Data not found");
            }
            for (Book book : books) {
                RespBook respBook = new RespBook();
                respBook.setName(book.getName());
                respBook.setAuthorId(book.getAuthorId());
                list.add(respBook);
            }
            response.setT(list);
            response.setRespStatus(RespStatus.getMessage());
        } catch (MyException e) {
            e.printStackTrace();
            response.setRespStatus(new RespStatus(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal server error"));
        }
        return response;
    }

    @Override
    public RespStatusList addBook(ReqBook reqBook) {
        RespStatusList respStatusList = new RespStatusList();
        String bookName = reqBook.getName();
        try {
            if (bookName == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "request fail");
            }
            Optional<Book> bk = bookRepository.findByNameAndActive(bookName, EnumCode.Active.getValue());
            if (bk.isPresent()) {
                throw new MyException(ExceptionConstants.DATA_TAKEN, "data taken");
            }
            Book book = new Book();
            book.setName(reqBook.getName());
            book.setAuthorId(reqBook.getAuthorId());
            bookRepository.save(book);
            respStatusList.setStatus(RespStatus.getMessage());
            List<UserInfo> userInfos = userInfoRepository.findAll();
            for (UserInfo userInfo : userInfos) {
                for (Author author : userInfo.getAuthorsId()) {
                    if (author.getId() == reqBook.getAuthorId().getId()) {
                        log.info("success mail will send" + userInfo.getEmail());
                        sendSimpleEmail(userInfo.getEmail(), "Subject", "New book: " + reqBook.getName() + " Author: " + author.getName());
                        log.info("success mail will send");
                    }
                }
            }

        } catch (MyException e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal error"));
        }
        return respStatusList;
    }
    @Override
    public Response deleteBookByAuthorId(Long authorId) {
        Response response = new Response<>();
        try {
            if (authorId == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "request fail");
            }
            Author author = authorRepository.findByIdAndActive(authorId, EnumCode.Active.getValue());
            if (author == null) {
                throw new MyException(ExceptionConstants.AUTHOR_NOT_FOUND, "Author_Not_Found");
            }
            List<Book> bookList = bookRepository.findAllByAuthorAndActive(author, EnumCode.Active.getValue());
            if (bookList.isEmpty()) {
                throw new MyException(ExceptionConstants.BOOK_NOT_FOUND, "Book_Not_Found");
            }
            for (Book book : bookList) {
                book.setActive(EnumCode.DeActive.getValue());
                bookRepository.save(book);
            }
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

    @Override
    public Response<List<RespBook>> getBookListByUserId(Long userId) {
        Response<List<RespBook>> response = new Response<>();
        try {
            if (userId == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "Invalid Request Data");
            }
            UserInfo userInfo = userInfoRepository.findUserInfoByIdAndActive(userId, EnumCode.Active.getValue());
            if (userInfo == null) {
                throw new MyException(ExceptionConstants.READER_NOT_FOUND, "Reader not found");
            }
            List<Book> bookList = bookRepository.findAllByUserInfoAndActive(userInfo, EnumCode.Active.getValue());
            if (bookList.isEmpty()) {
                throw new MyException(ExceptionConstants.BOOK_NOT_FOUND, "Book not found");
            }
            List<RespBook> respBookList = bookList.stream().map(this::mapping).collect(Collectors.toList());
            response.setT(respBookList);
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

    private RespBook mapping(Book book) {
        RespUserDetail respUserDetail = RespUserDetail.builder()
                .displayName(book.getUserInfo().getDisplayName())
                .email(book.getUserInfo().getEmail())
                .build();
        RespBook respBook = RespBook.builder()
                .name(book.getName())
                .respUserDetail(respUserDetail)
                .build();
        return respBook;
    }
}
