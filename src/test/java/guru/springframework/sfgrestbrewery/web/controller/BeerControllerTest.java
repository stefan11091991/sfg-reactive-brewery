package guru.springframework.sfgrestbrewery.web.controller;

import guru.springframework.sfgrestbrewery.bootstrap.BeerLoader;
import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.UUID;

import static guru.springframework.sfgrestbrewery.bootstrap.BeerLoader.BEER_1_UPC;
import static guru.springframework.sfgrestbrewery.bootstrap.BeerLoader.BEER_3_UPC;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    BeerService beerService;

    BeerDto validBeer;

    @BeforeEach
    void setUp() {
        validBeer = BeerDto.builder()
                .beerName("test beer")
                .beerStyle("PALE ALE")
                .upc(BEER_1_UPC)
                .build();
    }

    @Test
    void getBeerById() {
        UUID beerId = UUID.randomUUID();
        given(beerService.getById(any(), any())).willReturn(validBeer);

        webTestClient.get()
                .uri("/api/v1/beer/" + beerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(beerDto -> beerDto.getBeerName(), equalTo(validBeer.getBeerName()));

    }

    @Test
    void listBeers() {
        BeerPagedList validBeerPagedList = new BeerPagedList(Arrays.asList(validBeer));
        given(beerService.listBeers(any(), any(), any(), any())).willReturn(validBeerPagedList);

        webTestClient.get()
                .uri("/api/v1/beer/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerPagedList.class)
                .value(beerPagedList -> beerPagedList.getSize(), equalTo(validBeerPagedList.getSize()));
    }

    @Test
    void getBeerByUpc() {
        String beerUpc = BEER_1_UPC;
        given(beerService.getByUpc(any())).willReturn(validBeer);

        webTestClient.get()
                .uri("/api/v1/beerUpc/" + beerUpc)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(beerDto -> beerDto.getBeerName(), equalTo(validBeer.getBeerName()));

    }
}