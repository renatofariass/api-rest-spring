package com.api.spring.service;

import com.api.spring.controller.PersonController;
import com.api.spring.exceptions.RequiredObjectIsNullException;
import com.api.spring.exceptions.ResourceNotFoundException;
import com.api.spring.mapper.ModelMapperConverter;
import com.api.spring.model.Person;
import com.api.spring.repository.PersonRepository;
import com.api.spring.vo.PersonVO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices implements Serializable {
    private final PersonRepository personRepository;
    private final PagedResourcesAssembler<PersonVO> assembler;

    public PersonServices(PersonRepository personRepository, PagedResourcesAssembler<PersonVO> assembler) {
        this.personRepository = personRepository;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        var personPage = personRepository.findAll(pageable);
        var personsVOsPage = personPage.map(p -> ModelMapperConverter.parseObject(p, PersonVO.class));
        personsVOsPage.map(person -> person.add(linkTo(methodOn(PersonController.class)
                .findById(person.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(),
                "asc")).withSelfRel();

        return assembler.toModel(personsVOsPage, link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
        var personPage = personRepository.findPersonByName(firstName, pageable);

        var personsVOsPage = personPage.map(p -> ModelMapperConverter.parseObject(p, PersonVO.class));
        personsVOsPage.map(person -> person.add(linkTo(methodOn(PersonController.class)
                .findById(person.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(),
                "asc")).withSelfRel();

        return assembler.toModel(personsVOsPage, link);
    }

    public PersonVO findById(Long id) {
        var entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Person ID not found."));
        var vo = ModelMapperConverter.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();
        var entity = ModelMapperConverter.parseObject(person, Person.class);
        var vo = ModelMapperConverter.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();
        var entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person ID not found."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = ModelMapperConverter.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person ID not found."));
        personRepository.delete(entity);
    }

    @Transactional
    public PersonVO disablePerson(Long id) {
        personRepository.disablePerson(id);

        var entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Person ID not found."));
        var vo = ModelMapperConverter.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }
}
