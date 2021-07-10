package com.hz.book.service;

import com.hz.demo.entity.Book;

public interface BookService {
    int addBook(Book book);

    int updateBook(Book book);

    Book selectBook(String bookId);

    int deleteBook(String bookId);
}
