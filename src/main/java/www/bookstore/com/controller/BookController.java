package www.bookstore.com.controller;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import www.bookstore.com.dto.request.ReqBook;
import www.bookstore.com.dto.response.RespBook;
import www.bookstore.com.dto.response.RespStatusList;
import www.bookstore.com.dto.response.RespUserDetail;
import www.bookstore.com.dto.response.Response;
import www.bookstore.com.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/auth/v2")
@Slf4j
@Transactional
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public Response<List<RespBook>> getBookList() {
        return bookService.getBookList();
    }
    @PostMapping("/AddBook")
    public RespStatusList addBook(@RequestBody ReqBook reqBook) {
        return bookService.addBook(reqBook);
    }
    @PutMapping("/DeleteBookByAuthorId/{authorId}")
    public Response deleteBookByAuthorId(@PathVariable Long authorId){
       return bookService.deleteBookByAuthorId(authorId);
    }

    //getBooksByUserId
    @GetMapping("GetBookListByUserId/{userId}")
    public Response<List<RespBook>> getBookListByUserId(@PathVariable Long userId){
        return bookService.getBookListByUserId(userId);
    }



}