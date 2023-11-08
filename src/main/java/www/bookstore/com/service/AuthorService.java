package www.bookstore.com.service;

import www.bookstore.com.dto.request.ReqAuthor;
import www.bookstore.com.dto.response.RespAuthor;
import www.bookstore.com.dto.response.RespStatusList;
import www.bookstore.com.dto.response.Response;

import java.util.List;

public interface AuthorService {


    Response<List<RespAuthor>> getAuthorList();
    RespStatusList addAuthor(ReqAuthor reqAuthor);
}
