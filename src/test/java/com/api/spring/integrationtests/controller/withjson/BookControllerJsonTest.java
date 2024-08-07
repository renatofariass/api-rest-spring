package com.api.spring.integrationtests.controller.withjson;

import com.api.spring.configs.TestConfigs;
import com.api.spring.integrationtests.testcontainers.AbstractIntegrationTest;
import com.api.spring.integrationtests.vo.AccountCredentialsVO;
import com.api.spring.integrationtests.vo.BookVO;
import com.api.spring.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("renato", "senha");

        var accessToken = given()
                .basePath("auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                    .when()
                .post()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION,  "Bearer " + accessToken)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(book)
                .when()
                .post()
                .then()
                    .statusCode(201)
                        .extract()
                            .body()
                                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());

        assertTrue(persistedBook.getId() > 0);

        assertEquals("Domain Driven Design", persistedBook.getTitle());
        assertEquals("Eric Evans", persistedBook.getAuthor());
        assertEquals(92.00, persistedBook.getPrice());
    }

    @Test
    @Order(1)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        book.setAuthor("Renato");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());

        assertEquals(book.getId(), persistedBook.getId());

        assertEquals("Domain Driven Design", persistedBook.getTitle());
        assertEquals("Renato", persistedBook.getAuthor());
        assertEquals(92.00, persistedBook.getPrice());
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());

        assertTrue(persistedBook.getId() > 0);

        assertEquals("Domain Driven Design", persistedBook.getTitle());
        assertEquals("Renato", persistedBook.getAuthor());
        assertEquals(92.00, persistedBook.getPrice());
    }

    @Test
    @Order(4)
    public void testDelete() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        List<BookVO> book = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {});

        BookVO foundBookOne = book.get(0);

        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getLaunchDate());
        assertNotNull(foundBookOne.getPrice());

        assertEquals(1, foundBookOne.getId());

        assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
        assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
        assertEquals(49.00, foundBookOne.getPrice());

        BookVO foundBookThree = book.get(2);

        assertNotNull(foundBookThree.getId());
        assertNotNull(foundBookThree.getTitle());
        assertNotNull(foundBookThree.getAuthor());
        assertNotNull(foundBookThree.getLaunchDate());
        assertNotNull(foundBookThree.getPrice());

        assertEquals(3, foundBookThree.getId());

        assertEquals("Clean Code", foundBookThree.getTitle());
        assertEquals("Robert C. Martin", foundBookThree.getAuthor());
        assertEquals(77.00, foundBookThree.getPrice());
    }

    @Test
    @Order(6)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockBook() {
        book.setTitle("Domain Driven Design");
        book.setAuthor("Eric Evans");
        book.setLaunchDate(new Date());
        book.setPrice(92.00);
    }
}