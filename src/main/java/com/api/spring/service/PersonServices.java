package com.api.spring.service;

import com.api.spring.Exceptions.ResourceNotFoundException;
import com.api.spring.controller.PersonController;
import com.api.spring.model.Person;
import com.api.spring.repository.PersonRepository;
import com.api.spring.mapper.ModelMapperConverter;
import org.springframework.stereotype.Service;
import com.api.spring.vo.PersonVO;

import java.io.Serializable;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices implements Serializable {
    private final PersonRepository personRepository;

    public PersonServices(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonVO> findAll() {
        var persons = ModelMapperConverter.parseListObjects(personRepository.findAll(), PersonVO.class);
        persons.forEach(person -> person.add(linkTo(methodOn(PersonController.class)
                .findById(person.getKey())).withSelfRel()));
        return persons;
    }

    public PersonVO findById(Long id) {
        var entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Person ID not found."));
        var vo = ModelMapperConverter.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO personVO) {
        var entity = ModelMapperConverter.parseObject(personVO, Person.class);
        var vo = ModelMapperConverter.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {
        var entity = personRepository.findById(person.getKey())
        .orElseThrow(() -> new ResourceNotFoundException("Person ID not found."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = ModelMapperConverter.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person ID not found."));
        personRepository.delete(entity);
    }
}
