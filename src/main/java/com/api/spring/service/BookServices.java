package com.api.spring.service;

import com.api.spring.controller.BookController;
import com.api.spring.exceptions.RequiredObjectIsNullException;
import com.api.spring.exceptions.ResourceNotFoundException;
import com.api.spring.mapper.ModelMapperConverter;
import com.api.spring.model.Book;
import com.api.spring.repository.BookRepository;
import com.api.spring.vo.BookVO;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices implements Serializable {
    private final BookRepository bookRepository;

    public BookServices(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookVO> findAll() {
        var books = ModelMapperConverter.parseListObjects(bookRepository.findAll(), BookVO.class);
        books.forEach(book -> book.add(linkTo(methodOn(BookController.class)
                .findById(book.getId())).withSelfRel()));
        return books;
    }

    public BookVO findById(Long id) {
        var entity = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book ID not found."));
        var vo = ModelMapperConverter.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();
        var entity = ModelMapperConverter.parseObject(book, Book.class);
        var vo = ModelMapperConverter.parseObject(bookRepository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();
        var entity = bookRepository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book ID not found."));

        entity.setAuthor(book.getAuthor());
        entity.setTitle(book.getTitle());
        entity.setPrice(book.getPrice());
        entity.setLaunchDate(book.getLaunchDate());

        var vo = ModelMapperConverter.parseObject(bookRepository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book ID not found."));
        bookRepository.delete(entity);
    }
}
