package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"
    }, config = @SqlConfig(encoding = "utf-8"))
class FetchJeepTest {
  
  @LocalServerPort
  private int serverPort;
  
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    JeepModel model = JeepModel.WRANGLER;
    String trim = "Sport";
    String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", 
        serverPort, model, trim);
    List<Jeep> expected = buildExpected(); 
    
    
    ResponseEntity<List<Jeep>> response = restTemplate.exchange(uri, 
        HttpMethod.GET, null, new ParameterizedTypeReference<List<Jeep>>(){});
    
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    
    
    assertThat(response.getBody()).isEqualTo(expected);
  }
  
  
  private List<Jeep> buildExpected() {
    // formatter:off
    return List.of(
        Jeep.builder()
            .modelId(JeepModel.WRANGLER)
            .trimLevel("Sport")
            .numDoors(2)
            .wheelSize(17)
            .basePrice(new BigDecimal("28475.00"))
            .build(),
        Jeep.builder()
            .modelId(JeepModel.WRANGLER)
            .trimLevel("Sport")
            .numDoors(4)
            .wheelSize(17)
            .basePrice(new BigDecimal("31975.00"))
            .build()
        );
    //formatter:on
  }

}