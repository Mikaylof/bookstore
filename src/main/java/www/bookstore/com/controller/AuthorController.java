package www.bookstore.com.controller;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import www.bookstore.com.dto.request.ReqAuthor;
import www.bookstore.com.dto.response.RespAuthor;
import www.bookstore.com.dto.response.RespStatusList;
import www.bookstore.com.dto.response.Response;
import www.bookstore.com.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/auth/v1")
@Slf4j
@Transactional
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/list")
    public Response<List<RespAuthor>> getAuthorList(){
        return authorService.getAuthorList();
    }


    @PostMapping("/CreateAuthor")
    public RespStatusList addAuthor(@RequestBody ReqAuthor reqAuthor){
        return authorService.addAuthor(reqAuthor);
    }



}
