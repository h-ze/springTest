package com.hz.book.service.impl;

import com.hz.book.dao.BookDao;
import com.hz.book.service.BookService;
import com.hz.demo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public int addBook(Book book) {
        book.setBookId(0);
        int i = bookDao.addBook(book);
        return i;
    }

    @Override
    public int updateBook(Book book) {
        int i = bookDao.updateBook(book);
        return i;
    }

    @Override
    public Book selectBook(String bookId) {
        Book book = bookDao.getBook(bookId);
        return book;
    }

    @Override
    public int deleteBook(String bookId) {
        int i = bookDao.deleteBook(bookId);
        return i;
    }
}
