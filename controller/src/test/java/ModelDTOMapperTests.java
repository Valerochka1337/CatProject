import data.ModelDTOMapperTestData;
import java.time.format.DateTimeParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.dto.OwnerDTO;
import org.valerochka1337.mapper.CatDTOModelMapper;
import org.valerochka1337.mapper.CatDTOModelMapperImpl;
import org.valerochka1337.mapper.OwnerDTOModelMapper;
import org.valerochka1337.mapper.OwnerDTOModelMapperImpl;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;

public class ModelDTOMapperTests {
  private final OwnerDTOModelMapper ownerDTOModelMapper;
  private final CatDTOModelMapper catDTOModelMapper;

  public ModelDTOMapperTests() {
    this.ownerDTOModelMapper = new OwnerDTOModelMapperImpl();
    this.catDTOModelMapper = new CatDTOModelMapperImpl();
  }

  // Owner
  @Test
  public void testMappingOwnerDTOToModelSuccess() {
    // Set up
    OwnerDTO ownerDTO = ModelDTOMapperTestData.getOwnerDTO();
    OwnerModel expectedOwnerModel = ModelDTOMapperTestData.getOwnerModel();

    // Act
    OwnerModel gotOwnerModel = ownerDTOModelMapper.toModel(ownerDTO);

    // Assert
    Assertions.assertEquals(expectedOwnerModel, gotOwnerModel);
  }

  @Test
  public void testMappingOwnerDTOToModelFailureInvalidDate() {
    // Set up
    OwnerDTO ownerDTO = OwnerDTO.builder().birthDate("2004-03-0").build();

    // Act && Assert
    Assert.assertThrows(DateTimeParseException.class, () -> ownerDTOModelMapper.toModel(ownerDTO));
  }

  @Test
  public void testMappingOwnerModelToDTOSuccess() {
    // Set up
    OwnerModel ownerModel = ModelDTOMapperTestData.getOwnerModel();
    OwnerDTO expectedOwnerDTO = ModelDTOMapperTestData.getOwnerDTO();

    // Act
    OwnerDTO gotOwnerDTO = ownerDTOModelMapper.toDTO(ownerModel);

    // Assert
    Assertions.assertEquals(expectedOwnerDTO, gotOwnerDTO);
  }

  // Cat
  @Test
  public void testMappingCatDTOToModelSuccess() {
    // Set up
    CatDTO catDTO = ModelDTOMapperTestData.getCatDTO();
    CatModel expectedCatModel = ModelDTOMapperTestData.getCatModel();

    // Act
    CatModel gotCatModel = catDTOModelMapper.toModel(catDTO);

    // Assert
    Assertions.assertEquals(expectedCatModel, gotCatModel);
  }

  @Test
  public void testMappingCatDTOToModelFailureInvalidDate() {
    // Set up
    CatDTO catDTO = CatDTO.builder().birthDate("2004-03-0").build();

    // Act && Assert
    Assert.assertThrows(DateTimeParseException.class, () -> catDTOModelMapper.toModel(catDTO));
  }

  @Test
  public void testMappingCatDTOToModelFailureInvalidColor() {
    // Set up
    CatDTO catDTO = CatDTO.builder().birthDate("2004-03-04").color("WRONG").build();

    // Act && Assert
    Assert.assertThrows(IllegalArgumentException.class, () -> catDTOModelMapper.toModel(catDTO));
  }

  @Test
  public void testMappingCatModelToDTOSuccess() {
    // Set up
    CatModel catModel = ModelDTOMapperTestData.getCatModel();
    CatDTO expectedCatDTO = ModelDTOMapperTestData.getCatDTO();

    // Act
    CatDTO gotCatDTO = catDTOModelMapper.toDTO(catModel);

    // Assert
    Assertions.assertEquals(expectedCatDTO, gotCatDTO);
  }
}
