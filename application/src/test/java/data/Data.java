package data;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class Data {

  public static Stream<Arguments> catCreationSuccessArguments() {
    // args: Json of a cat, creation request result
    return Stream.of(
        Arguments.of(
            """
              {
                "birthDate": "2004-03-04",
                "name": "Nick",
                "breed": "pug",
                "color": "RED"
              }
            """),
        Arguments.of(
            """
              {
                "birthDate": "2004-03-04",
                "name": "Nick"
              }
            """));
  }

  public static Stream<Arguments> catCreationFailArguments() {
    // args: Json of a cat, success status, error message

    return Stream.of(
        Arguments.of(
            """
                {
                  "birthDate": "2004-03-04",
                  "breed": "pug",
                  "color": "RED"
                }
              """,
            "Cat must have name"),
        Arguments.of(
            """
                {
                  "name": "Nick",
                  "breed": "pug",
                  "color": "RED"
                }
              """,
            "Cat must have birth date"),
        Arguments.of(
            """
                {
                  "birthDate": "2004-03-04",
                  "name": "Nick",
                  "breed": "pug",
                  "color": "Wrong color"
                }
              """,
            "Invalid color"),
        Arguments.of(
            """
                {
                  "birthDate": "Wrong birth date",
                  "name": "Nick",
                  "breed": "pug",
                  "color": "RED"
                }
              """,
            "Invalid birth date"),
        Arguments.of(
            """
                {
                  "birthDate": "2004-03-04",
                  "name": "Nick",
                  "breed": "pug",
                  "color": "RED",
                  "owner": {"id": "8e49048e-da44-4d3a-b7b8-937917052dfc"}
                }
              """,
            "No such owner with id: 8e49048e-da44-4d3a-b7b8-937917052dfc"),
        Arguments.of(
            """
                {
                  "birthDate": "2004-03-04",
                  "name": "Nick",
                  "breed": "pug",
                  "color": "RED",
                  "friendCats": [{"id": "8e49048e-da44-4d3a-b7b8-937917052dfc"}]
                }
              """,
            "No friend-cat with id: 8e49048e-da44-4d3a-b7b8-937917052dfc"));
  }

  public static Stream<Arguments> catFriendFailArguments() {

    return Stream.of(
        Arguments.of(
            "797160a3-df79-4dd8-9109-8edd75150f38",
            "797160a3-df79-4dd8-9109-8edd75150f38",
            "Can't friend cat to itself (id1 == id2)"),
        Arguments.of(
            "c794c2bc-fac1-4229-870e-1b2bf8a706f3",
            "90dd496d-cb25-44fb-bec7-0fc22c6d57b8",
            "Cats with id: c794c2bc-fac1-4229-870e-1b2bf8a706f3 is already "
                + "friend with cat with id: 90dd496d-cb25-44fb-bec7-0fc22c6d57b8"),
        Arguments.of(
            "c794c2bc-fac1-4229-870e-1b2bf8a706f3",
            "90dd496d-cb25-44fb-bec7-0fc22c67382f", // no cat with such id
            "No such cat with id: 90dd496d-cb25-44fb-bec7-0fc22c67382f"));
  }

  // Owner
  public static Stream<Arguments> ownerCreationSuccessArguments() {
    // args: Json of a cat, creation request result
    return Stream.of(
        Arguments.of(
            """
              {
                "birthDate": "2004-03-04",
                "name": "Nick"
              }
            """),
        Arguments.of(
            """
              {
                "birthDate": "2004-03-04",
                "name": "Nick",
                "ownedCats": [
                  {"id": "797160a3-df79-4dd8-9109-8edd75150f38",
                   "name": "Nick", "birthDate": "2004-03-04"}
                   ]
              }
            """));
  }

  public static Stream<Arguments> ownerCreationFailArguments() {
    // args: Json of an owner, error message
    return Stream.of(
        Arguments.of(
            """
                {
                  "birthDate": "2004-03-04"
                }
              """,
            "Owner must have name"),
        Arguments.of(
            """
                {
                  "name": "Nick"
                }
              """,
            "Owner must have birth date"),
        Arguments.of(
            """
                {
                  "birthDate": "Wrong birth date",
                  "name": "Nick"
                }
              """,
            "Invalid birth date"),
        Arguments.of(
            """
                {
                  "birthDate": "2004-03-04",
                  "name": "Nick",
                  "ownedCats": [
                  {"id": "8e49048e-da44-4d3a-b7b8-937917052dfc",
                   "name": "Kek", "birthDate": "2004-03-04"}
                   ]
                }
              """,
            "No such owned cat"));
  }
}
