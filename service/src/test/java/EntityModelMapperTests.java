import data.EntityModelMapperTestData;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.valerochka1337.entity.Cat;
import org.valerochka1337.entity.Owner;
import org.valerochka1337.mapper.CatModelEntityMapper;
import org.valerochka1337.mapper.CatModelEntityMapperImpl;
import org.valerochka1337.mapper.OwnerModelEntityMapper;
import org.valerochka1337.mapper.OwnerModelEntityMapperImpl;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;

public class EntityModelMapperTests {
  private final CatModelEntityMapper catModelEntityMapper;
  private final OwnerModelEntityMapper ownerModelEntityMapper;

  public EntityModelMapperTests() {
    this.ownerModelEntityMapper = new OwnerModelEntityMapperImpl();
    this.catModelEntityMapper = new CatModelEntityMapperImpl();
  }

  // Owner
  @Test
  public void testMappingOwnerModelToEntitySuccess() {
    // Set up
    OwnerModel ownerModel = EntityModelMapperTestData.getOwnerModel();
    Owner expectedOwnerEntity = EntityModelMapperTestData.getOwnerEntity();

    // Act
    Owner gotOwnerEntity = ownerModelEntityMapper.toEntity(ownerModel);

    // Assert
    Assertions.assertEquals(expectedOwnerEntity, gotOwnerEntity);
  }

  @Test
  public void testMappingOwnerEntityToModelSuccess() {
    // Set up
    Owner ownerEntity = EntityModelMapperTestData.getOwnerEntity();
    OwnerModel expectedOwnerModel = EntityModelMapperTestData.getOwnerModel();

    // Act
    OwnerModel gotOwnerModel = ownerModelEntityMapper.toModel(ownerEntity);

    // Assert
    Assertions.assertEquals(expectedOwnerModel, gotOwnerModel);
  }

  // Cat
  @Test
  public void testMappingCatModelToEntitySuccess() {
    // Set up
    CatModel catModel = EntityModelMapperTestData.getCatModel();
    Cat expectedCatEntity = EntityModelMapperTestData.getCatEntity();

    // Act
    Cat gotCatEntity = catModelEntityMapper.toEntity(catModel);

    // Assert
    Assertions.assertEquals(expectedCatEntity, gotCatEntity);
  }

  @Test
  public void EntityToModelAllArgsTestSuccess() {
    // Set up
    Cat catEntity = EntityModelMapperTestData.getCatEntity();
    CatModel expectedCatModel = EntityModelMapperTestData.getCatModel();

    // Act
    CatModel gotCatModel = catModelEntityMapper.toModel(catEntity);

    // Assert
    Assertions.assertEquals(expectedCatModel, gotCatModel);
  }
}
