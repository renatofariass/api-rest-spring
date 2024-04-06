package com.api.spring.unittest.mapper.mock;

import com.api.spring.model.Book;
import com.api.spring.vo.BookVO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {
    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookVO mockVO() {
        return mockVO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Author Name Test" + number);
        book.setTitle("Title Name Test" + number);
        book.setPrice(number.doubleValue());
        book.setLaunchDate(Date.from(Instant.now()));
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setId(number.longValue());
        book.setAuthor("Author Name Test" + number);
        book.setTitle("Title Name Test" + number);
        book.setPrice(number.doubleValue());
        book.setLaunchDate(Date.from(Instant.now()));
        return book;
    }
}