package com.api.spring.integrationtests.repositories;

import com.api.spring.configs.TestConfigs;
import com.api.spring.integrationtests.testcontainers.AbstractIntegrationTest;
import com.api.spring.integrationtests.vo.PersonVO;
import com.api.spring.integrationtests.vo.wrappers.person.WrapperPersonVO;
import com.api.spring.model.Person;
import com.api.spring.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private PersonRepository repository;

    private static Person person;

    @BeforeAll
    public static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    public void testFindByName() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC,
                "firstName"));
        person = repository.findPersonByName("a", pageable).getContent().get(1);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getEnabled());

        assertEquals(189, person.getId());

        assertEquals("Abra", person.getFirstName());
        assertEquals("Thebe", person.getLastName());
        assertEquals("134 Raven Lane", person.getAddress());
        assertEquals("Female", person.getGender());
    }

    @Test
    @Order(2)
    public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
        repository.disablePerson(person.getId());

        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC,
                "firstName"));
         person = repository.findPersonByName("a", pageable).getContent().get(1);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertFalse(person.getEnabled());

        assertEquals(189, person.getId());

        assertEquals("Abra", person.getFirstName());
        assertEquals("Thebe", person.getLastName());
        assertEquals("134 Raven Lane", person.getAddress());
        assertEquals("Female", person.getGender());
    }

}
