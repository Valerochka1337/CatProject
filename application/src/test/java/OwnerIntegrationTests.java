import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.valerochka1337.Application;
import org.valerochka1337.repository.CatRepository;
import org.valerochka1337.repository.OwnerRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
@WithUserDetails(value = "admin")
@Testcontainers
public class OwnerIntegrationTests {

  @Container
  private static final PostgreSQLContainer<?> container =
      new PostgreSQLContainer<>("postgres:13.3")
          .withDatabaseName("db")
          .withUsername("postgres")
          .withPassword("postgres");

  @Autowired private MockMvc mvc;
  @Autowired private CatRepository catRepository;
  @Autowired private OwnerRepository ownerRepository;

  @DynamicPropertySource
  public static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }

  @AfterEach
  public void cleanUpDB() {
    ownerRepository.deleteAll();
    catRepository.deleteAll();
  }

  @ParameterizedTest
  @Sql("/sql/cat_init_1.sql")
  @MethodSource("data.Data#ownerCreationSuccessArguments")
  public void testOwnerCreationSuccess(String inputData) throws Exception {
    // Act
    String result =
        mvc.perform(
                post("/api/v1/owners").contentType(MediaType.APPLICATION_JSON).content(inputData))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject gotOwner = new JSONObject(result);
    String generatedId = gotOwner.get("id").toString();

    JSONObject expectedOwner = new JSONObject(inputData).put("id", generatedId);

    // Assert
    Assertions.assertTrue(ownerRepository.existsById(UUID.fromString(generatedId)));
    JSONAssert.assertEquals(expectedOwner, gotOwner, JSONCompareMode.LENIENT);
  }

  @ParameterizedTest
  @MethodSource("data.Data#ownerCreationFailArguments")
  public void testOwnerCreationFail(String inputData, String errorMessage) throws Exception {
    // Act
    String result =
        mvc.perform(
                post("/api/v1/owners").contentType(MediaType.APPLICATION_JSON).content(inputData))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals(0, ownerRepository.findAll().size());
    Assertions.assertEquals(errorMessage, response.get("message"));
  }

  @Test
  @Sql("/sql/owner_init_1.sql")
  public void testOwnerGetSuccess() throws Exception {
    // Set up
    String expectedOwner =
        """
          {
            "id":  "c39057fb-740e-4cb4-8f8b-8e7f1663f818",
            "birthDate": "2003-10-05",
            "name": "Julia"
          }
        """;

    // Act
    String gotOwner =
        mvc.perform(get("/api/v1/owners/c39057fb-740e-4cb4-8f8b-8e7f1663f818"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JSONAssert.assertEquals(expectedOwner, gotOwner, JSONCompareMode.LENIENT);
  }

  @Test
  public void testOwnerGetFail() throws Exception {
    // Act
    String result =
        mvc.perform(get("/api/v1/owners/8e49048e-da44-4d3a-b7b8-937917052dfc"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals(
        "No such owner with id: 8e49048e-da44-4d3a-b7b8-937917052dfc", response.get("message"));
  }

  @Test
  @Sql("/sql/owner_init_1.sql")
  public void testOwnerGetAllSuccess() throws Exception {
    // Act
    String result =
        mvc.perform(get("/api/v1/owners"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONArray response = new JSONArray(result);

    // Assert
    Assertions.assertEquals(ownerRepository.findAll().size(), response.length());
  }

  @Test
  @Sql("/sql/owner_init_1.sql")
  public void testOwnerDeleteSuccess() throws Exception {
    // Act
    int ownerSize = ownerRepository.findAll().size();

    mvc.perform(delete("/api/v1/owners/678f1a48-1017-490c-a3c7-eab4ec6b4444"))
        .andExpect(status().isOk());

    // Assert
    Assertions.assertEquals(ownerSize - 1, ownerRepository.findAll().size());
  }

  @Test
  @Sql("/sql/owner_init_1.sql")
  public void testOwnerDeleteFail() throws Exception {
    // Act
    int ownerSize = ownerRepository.findAll().size();

    String result =
        mvc.perform(delete("/api/v1/owners/797160a3-df79-4dd8-9109-8edd75150f38")) // Wrong id
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals(ownerSize, ownerRepository.findAll().size());
    Assertions.assertEquals(
        "No such owner with id: 797160a3-df79-4dd8-9109-8edd75150f38", response.get("message"));
  }

  @Test
  @Sql(scripts = {"/sql/cat_init_1.sql", "/sql/owner_init_1.sql"})
  public void testOwnerGetOwnedCatsSuccess() throws Exception {
    // Set up
    String expectedOwnedCats =
        """
          [
            {
              "id": "797160a3-df79-4dd8-9109-8edd75150f38",
              "name": "Nick"
            },
            {
              "id": "4cd44ae8-8e3d-477c-9bdc-fd90cb5a5b5e",
              "name": "Polly"
            }
          ]
        """;

    // Act
    mvc.perform(
            put(
                "/api/v1/cats/797160a3-df79-4dd8-9109-8edd75150f38?ownerID=c39057fb-740e-4cb4-8f8b-8e7f1663f818"))
        .andExpect(status().isOk());
    mvc.perform(
            put(
                "/api/v1/cats/4cd44ae8-8e3d-477c-9bdc-fd90cb5a5b5e?ownerID=c39057fb-740e-4cb4-8f8b-8e7f1663f818"))
        .andExpect(status().isOk());

    String result =
        mvc.perform(
                get("/api/v1/owners/c39057fb-740e-4cb4-8f8b-8e7f1663f818/ownedCats")) // Wrong id
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JSONAssert.assertEquals(expectedOwnedCats, result, JSONCompareMode.LENIENT);
  }
}
