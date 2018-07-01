package com.redhat.springboot.vacationleave.employee.controller;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.redhat.springboot.vacationleave.employee.service.SickRequestService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

    @Autowired
    private SickRequestService sickRequestService;

    @LocalServerPort
    private int port;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() throws Exception {
        RestAssured.baseURI = String.format("http://localhost:%d/api/employees", port);
        ReflectionTestUtils.setField(sickRequestService, null, "sickRequestsServiceUrl", "http://localhost:" + wireMockRule.port(), null);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("sickRequest-response.json");

        stubFor(post(urlEqualTo("/sickRequest")).willReturn(
                aResponse().withStatus(200).withHeader("Content-type", "application/json").withBody(IOUtils.toString(is, Charset.defaultCharset()))));

    }

    @Test
    public void list() throws Exception {
        Response response = given()
                .get("/?page=0&pageSize=100")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        int sizeOfList = response.body().path("list.size()");
        Assert.assertEquals(sizeOfList, 2);
        String jsonStr = response.getBody().asString();
        Integer id0 = response.jsonPath().getInt("id[0]");
        Integer id1 = response.jsonPath().getInt("id[1]");
        Assert.assertEquals(String.valueOf(id0), "1");
        Assert.assertEquals(String.valueOf(id1), "2");

    }
}
