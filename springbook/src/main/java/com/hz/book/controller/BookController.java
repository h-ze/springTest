package com.hz.book.controller;


import com.hz.book.service.BookService;
import com.hz.demo.entity.Book;
import com.hz.demo.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("book")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @RequestMapping("addBook")
    public ResponseResult<String> addBook(@RequestBody Book book) throws ParseException {
        log.info("book: {}",book);
        //SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        //Date createDate = book.getCreateDate();
        //log.debug("日期: {}",createDate);
        //String time = "2019-09-19";
        //Date date = ft.parse(time);
        //book.setCreateDate(date);
        int i = bookService.addBook(book);
        if (i>0){
            return new ResponseResult(100000,"书籍信息已添加","添加成功");
        }else {
            return new ResponseResult(999999,"书籍信息添加失败","添加失败");
        }
    }

    @GetMapping
    @RequestMapping("getBook")
    public ResponseResult<Book> getBook(String bookId){
        Book book = bookService.selectBook(bookId);
        return new ResponseResult(100000,"获取书籍信息成功",book);
    }

    @DeleteMapping
    @RequestMapping("deleteBook")
    public ResponseResult<String> deleteBook(String bookId){
        int i = bookService.deleteBook(bookId);
        if (i>0){
            return new ResponseResult(100000,"书籍信息已删除","删除成功");
        }else {
            return new ResponseResult(999999,"书籍信息删除失败","删除失败");
        }
    }

    @PutMapping
    @RequestMapping("updateBook")
    public ResponseResult<Book> updateBook(@RequestBody Book book){
        int i = bookService.updateBook(book);
        if (i>0){
            return new ResponseResult(100000,"书籍信息已更新","更新成功");
        }else {
            return new ResponseResult(999999,"书籍信息更新失败","更新失败");
        }
    }
}
