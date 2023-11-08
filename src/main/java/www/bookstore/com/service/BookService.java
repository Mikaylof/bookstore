package www.bookstore.com.service;

import www.bookstore.com.dto.request.ReqBook;
import www.bookstore.com.dto.response.RespBook;
import www.bookstore.com.dto.response.RespStatusList;
import www.bookstore.com.dto.response.RespUserDetail;
import www.bookstore.com.dto.response.Response;

import java.util.List;

public interface BookService {
    Response<List<RespBook>> getBookList();

    RespStatusList addBook(ReqBook reqBook);

    Response deleteBookByAuthorId(Long authorId);

    Response<List<RespBook>> getBookListByUserId(Long userId);
}
