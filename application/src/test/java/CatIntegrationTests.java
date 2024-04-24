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
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@Testcontainers
public class CatIntegrationTests {

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
    catRepository.deleteAll();
    ownerRepository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("data.Data#catCreationSuccessArguments")
  public void testCatCreationSuccess(String inputData) throws Exception {
    // Act
    String result =
        mvc.perform(post("/api/v1/cats").contentType(MediaType.APPLICATION_JSON).content(inputData))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject gotCat = new JSONObject(result);
    String generatedId = gotCat.get("id").toString();

    JSONObject expectedCat = new JSONObject(inputData).put("id", generatedId);

    // Assert
    Assertions.assertTrue(catRepository.existsById(UUID.fromString(generatedId)));
    JSONAssert.assertEquals(expectedCat, gotCat, JSONCompareMode.LENIENT);
  }

  @ParameterizedTest
  @MethodSource("data.Data#catCreationFailArguments")
  public void testCatCreationFail(String inputData, String errorMessage) throws Exception {
    // Act
    String result =
        mvc.perform(post("/api/v1/cats").contentType(MediaType.APPLICATION_JSON).content(inputData))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals(0, catRepository.findAll().size());
    Assertions.assertEquals(errorMessage, response.get("message"));
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testCatGetSuccess() throws Exception {
    // Set up
    String expectedCat =
        """
          {
            "id":  "797160a3-df79-4dd8-9109-8edd75150f38",
            "birthDate": "2004-03-04",
            "name": "Nick",
            "breed": "pug",
            "color": "RED"
          }
        """;

    // Act

    String resultCat =
        mvc.perform(get("/api/v1/cats/797160a3-df79-4dd8-9109-8edd75150f38"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JSONAssert.assertEquals(expectedCat, resultCat, JSONCompareMode.LENIENT);
  }

  @Test
  public void testCatGetFail() throws Exception {
    // Act
    String result =
        mvc.perform(get("/api/v1/cats/8e49048e-da44-4d3a-b7b8-937917052dfc"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals(
        "No such cat with id: 8e49048e-da44-4d3a-b7b8-937917052dfc", response.get("message"));
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testCatGetAllSuccess() throws Exception {
    // Act
    String result =
        mvc.perform(get("/api/v1/cats"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONArray response = new JSONArray(result);

    // Assert
    Assertions.assertEquals(catRepository.findAll().size(), response.length());
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testCatDeleteSuccess() throws Exception {
    // Act
    int catSize = catRepository.findAll().size();

    mvc.perform(delete("/api/v1/cats/797160a3-df79-4dd8-9109-8edd75150f38"))
        .andExpect(status().isOk());

    // Assert
    Assertions.assertEquals(catSize - 1, catRepository.findAll().size());
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testCatDeleteFail() throws Exception {
    // Act
    int catSize = catRepository.findAll().size();

    String result =
        mvc.perform(delete("/api/v1/cats/678f1a48-1017-490c-a3c7-eab4ec6b4444")) // Wrong id
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals(catSize, catRepository.findAll().size());
    Assertions.assertEquals(
        "No such cat with id: 678f1a48-1017-490c-a3c7-eab4ec6b4444", response.get("message"));
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  @Sql("/sql/owner_init_1.sql")
  public void testAddOwnerSuccess() throws Exception {
    // Set up
    String expectedCat =
        """
          {
            "id": "797160a3-df79-4dd8-9109-8edd75150f38",
            "birthDate": "2004-03-04",
            "name": "Nick",
            "breed": "pug",
            "color": "RED",
            "owner": {
              "id": "c39057fb-740e-4cb4-8f8b-8e7f1663f818",
              "name": "Julia"
            }
          }
        """;

    // Act
    String resultCat =
        mvc.perform(
                put(
                    "/api/v1/cats/797160a3-df79-4dd8-9109-8edd75150f38?ownerID="
                        + "c39057fb-740e-4cb4-8f8b-8e7f1663f818"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JSONAssert.assertEquals(expectedCat, resultCat, JSONCompareMode.LENIENT);
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testFriendCatsSuccess() throws Exception {
    // Set up
    String cat1Id = "797160a3-df79-4dd8-9109-8edd75150f38";
    String cat2Id = "4cd44ae8-8e3d-477c-9bdc-fd90cb5a5b5e";
    String expectedCat1 =
        """
              {
                "id": "797160a3-df79-4dd8-9109-8edd75150f38",
                "birthDate": "2004-03-04",
                "name": "Nick",
                "breed": "pug",
                "color": "RED",
                "friendCats": [
                {"id":  "4cd44ae8-8e3d-477c-9bdc-fd90cb5a5b5e", "name":  "Polly"}
                ]
              }
            """;

    // Act
    mvc.perform(put("/api/v1/cats/friend?id1=" + cat1Id + "&id2=" + cat2Id))
        .andExpect(status().isOk());

    String resultCat1 =
        mvc.perform(get("/api/v1/cats/" + cat1Id))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JSONAssert.assertEquals(expectedCat1, resultCat1, JSONCompareMode.LENIENT);
  }

  @ParameterizedTest
  @MethodSource("data.Data#catFriendFailArguments")
  @Sql("/sql/cat_init_1.sql")
  public void testFriendCatsFail(String cat1Id, String cat2Id, String errorMessage)
      throws Exception {
    // Act
    String result =
        mvc.perform(put("/api/v1/cats/friend?id1=" + cat1Id + "&id2=" + cat2Id))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals(errorMessage, response.get("message"));
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testUnfriendCatsSuccess() throws Exception {
    // Set up
    String cat1Id = "c794c2bc-fac1-4229-870e-1b2bf8a706f3";
    String cat2Id = "90dd496d-cb25-44fb-bec7-0fc22c6d57b8";
    String expectedCat1 =
        """
              {
                "id": "c794c2bc-fac1-4229-870e-1b2bf8a706f3",
                "birthDate": "2013-02-05",
                "name": "Peppe",
                "breed": "pug",
                "color": "BLUE",
                "friendCats": []
              }
            """;

    // Act
    mvc.perform(put("/api/v1/cats/unfriend?id1=" + cat1Id + "&id2=" + cat2Id))
        .andExpect(status().isOk());

    String resultCat1 =
        mvc.perform(get("/api/v1/cats/" + cat1Id))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JSONAssert.assertEquals(expectedCat1, resultCat1, JSONCompareMode.LENIENT);
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testGetCatsByBreedSuccess() throws Exception {
    // Set up
    int expectedPugsAmount = 3; // Check in cat_init_1.sql

    // Act
    String result =
        mvc.perform(get("/api/v1/cats?breed=pug"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONArray gotPugs = new JSONArray(result);

    // Assert
    Assertions.assertEquals(expectedPugsAmount, gotPugs.length());
  }

  @Test
  @Sql("/sql/cat_init_1.sql")
  public void testGetCatsByColorSuccess() throws Exception {
    // Set up
    int expectedRedAmount = 4; // Check in cat_init_1.sql

    // Act
    String result =
        mvc.perform(get("/api/v1/cats?color=red"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONArray gotReds = new JSONArray(result);

    // Assert
    Assertions.assertEquals(expectedRedAmount, gotReds.length());
  }

  @Test
  public void testGetCatsByColorFailInvalidColor() throws Exception {
    // Act
    String result =
        mvc.perform(get("/api/v1/cats?color=wrong"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JSONObject response = new JSONObject(result);

    // Assert
    Assertions.assertEquals("Invalid color", response.get("message"));
  }
}
