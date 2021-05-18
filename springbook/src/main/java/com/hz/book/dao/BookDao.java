package com.hz.book.dao;


import com.hz.demo.entity.Book;

public interface BookDao {

    int addBook(Book book);

    int deleteBook(String bookId);

    Book getBook(String bookId);

    int updateBook(Book book);

}
