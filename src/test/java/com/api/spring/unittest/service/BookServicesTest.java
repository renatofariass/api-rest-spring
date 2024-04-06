package com.api.spring.unittest.service;

import com.api.spring.exceptions.RequiredObjectIsNullException;
import com.api.spring.model.Book;
import com.api.spring.repository.BookRepository;
import com.api.spring.repository.PersonRepository;
import com.api.spring.service.BookServices;
import com.api.spring.service.PersonServices;
import com.api.spring.unittest.mapper.mock.MockBook;
import com.api.spring.unittest.mapper.mock.MockPerson;
import com.api.spring.vo.BookVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookServicesTest {
    @InjectMocks
    private BookServices service;
    @Mock
    private BookRepository repository;
    private MockBook input;


    @BeforeEach
    void setUpMocks() {
        MockitoAnnotations.openMocks(this);
        input = new MockBook();
    }

    @Test
    void findById() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Name Test1", result.getAuthor());
        assertEquals("Title Name Test1", result.getTitle());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());
    }


    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);

        var books = service.findAll();
        assertNotNull(books);
        assertEquals(14, books.size());

        var bookOne = books.get(1);
        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Name Test1", bookOne.getAuthor());
        assertEquals("Title Name Test1", bookOne.getTitle());
        assertNotNull(bookOne.getLaunchDate());
        assertEquals(1, bookOne.getPrice());

        var bookSeven = books.get(7);
        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());

        assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Author Name Test7", bookSeven.getAuthor());
        assertEquals("Title Name Test7", bookSeven.getTitle());
        assertNotNull(bookSeven.getLaunchDate());
        assertEquals(7, bookSeven.getPrice());

        var bookTwelve = books.get(12);
        assertNotNull(bookTwelve);
        assertNotNull(bookTwelve.getId());
        assertNotNull(bookTwelve.getLinks());

        assertTrue(bookTwelve.toString().contains("links: [</api/book/v1/12>;rel=\"self\"]"));
        assertEquals("Author Name Test12", bookTwelve.getAuthor());
        assertEquals("Title Name Test12", bookTwelve.getTitle());
        assertNotNull(bookTwelve.getLaunchDate());
        assertEquals(12, bookTwelve.getPrice());
    }

    @Test
    void create() {
        Book entity = input.mockEntity(1);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Name Test1", result.getAuthor());
        assertEquals("Title Name Test1", result.getTitle());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }


    @Test
    void update() {
        Book entity = input.mockEntity(1);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Name Test1", result.getAuthor());
        assertEquals("Title Name Test1", result.getTitle());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }

    @Test
    void delete() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        service.delete(1L);
    }
}