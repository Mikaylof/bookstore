package www.bookstore.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import www.bookstore.com.dto.request.ReqAuthor;
import www.bookstore.com.dto.response.RespAuthor;
import www.bookstore.com.dto.response.RespStatus;
import www.bookstore.com.dto.response.RespStatusList;
import www.bookstore.com.dto.response.Response;
import www.bookstore.com.entity.Author;
import www.bookstore.com.entity.Role;
import www.bookstore.com.exception.EnumCode;
import www.bookstore.com.exception.ExceptionConstants;
import www.bookstore.com.exception.MyException;
import www.bookstore.com.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Response<List<RespAuthor>> getAuthorList() {

        Response<List<RespAuthor>> response = new Response<>();
        List<RespAuthor> list = new ArrayList<>();
        try {
            List<Author> authors = authorRepository.findByActive(EnumCode.Active.getValue());
            if (authors == null) {
                throw new MyException(ExceptionConstants.NOT_FOUND, "Data not found");
            }
            for (Author author : authors) {
                RespAuthor respAuthor = new RespAuthor();
                 respAuthor.setName(author.getName());
                 respAuthor.setAge(author.getAge());
                list.add(respAuthor);
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
    public RespStatusList addAuthor(ReqAuthor reqAuthor) {
        RespStatusList respStatusList = new RespStatusList();
        String authorName = reqAuthor.getName();
        try {
            if (authorName == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "request fail");
            }
            Optional<Author> author = authorRepository.findAuthorByName(authorName);
            if (author.isPresent()) {
                throw new MyException(ExceptionConstants.DATA_TAKEN, "data taken");
            }
            Author author1 = new Author();
            author1.setName(reqAuthor.getName());
            author1.setAge(reqAuthor.getAge());
            authorRepository.save(author1);
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
}
