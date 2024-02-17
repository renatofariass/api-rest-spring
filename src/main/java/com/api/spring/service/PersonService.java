package com.api.spring.service;

import com.api.spring.Exceptions.ResourceNotFoundException;
import com.api.spring.model.Person;
import com.api.spring.repository.PersonRepository;
import com.api.spring.mapper.ModelMapperConvert;
import org.springframework.stereotype.Service;
import com.api.spring.vo.PersonVO;

import java.io.Serializable;
import java.util.List;

@Service
public class PersonService implements Serializable {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonVO> findAll() {
        return ModelMapperConvert.parseListObjects(personRepository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long Id) {
        var entity = personRepository.findById(Id).orElseThrow(
                () -> new ResourceNotFoundException("Person ID not found."));
        return ModelMapperConvert.parseObject(entity, PersonVO.class);
    }

    public PersonVO create(PersonVO personVO) {
        var entity = ModelMapperConvert.parseObject(personVO, Person.class);
        return ModelMapperConvert.parseObject(personRepository.save(entity), PersonVO.class);
    }

    public PersonVO update(PersonVO person) {
        var entity = personRepository.findById(person.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Person ID not found."));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return ModelMapperConvert.parseObject(personRepository.save(entity), PersonVO.class);
    }

    public void delete(Long id) {
        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person ID not found."));
        personRepository.delete(entity);
    }
}
