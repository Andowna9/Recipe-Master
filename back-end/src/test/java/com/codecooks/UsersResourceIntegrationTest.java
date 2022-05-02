package com.codecooks;

import com.codecooks.authentication.SessionManager;
import com.codecooks.dao.RecipeDAO;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.CookingExperience;
import com.codecooks.domain.Gender;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.serialize.ProfileData;
import com.codecooks.serialize.ProfileEditionData;
import com.codecooks.serialize.RecipeBriefData;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.*;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

public class UsersResourceIntegrationTest {

    private static HttpServer server;
    private static WebTarget target;
    private static String token;

    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final UserDAO userDAO = new UserDAO();
    private User user;

    @BeforeClass
    public static void setUp() {

        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);

        token = SessionManager.getInstance().generateToken();
        SessionManager.getInstance().startSession(token, "test");

    }

    @Before
    public void prepareData() {

        user = new User("test", "test@gmail.com", "test");
        user.setName("Test name");
        user.setAboutMe("Testing about me");
        user.setBirthDate(LocalDate.of(2001, 1, 1));
        user.setCookingExp(CookingExperience.BEGINNER);
        user.setCountryCode("ES");
        user.setGender(Gender.OTHER);

        userDAO.save(user);

        Recipe recipe = new Recipe("Test Recipe 1", "Test Content 1", "ES");
        user.addRecipePost(recipe);
        recipe.setCreator(user);

        recipeDAO.save(recipe);

    }

    @Test
    public void getMyProfile() {

        WebTarget meTarget = target.path("users/me");

        Response response = meTarget.request(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ProfileData profileData = response.readEntity(ProfileData.class);
        assertNotNull(profileData);

        assertEquals(profileData.getUsername(), user.getUsername());
        assertEquals(profileData.getCountryCode(), user.getCountryCode());
        assertEquals(profileData.getCookingExperience(), user.getCookingExp());

        List<RecipeBriefData> postedRecipeBriefs = profileData.getPostedRecipeBriefs();
        assertEquals(1, postedRecipeBriefs.size());
    }

    @Test
    public void getProfileEdition() {

        WebTarget getTarget = target.path("users/me/edit");

        Response response = getTarget.request(MediaType.APPLICATION_JSON)
                                     .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                     .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ProfileEditionData profileEditionData = response.readEntity(ProfileEditionData.class);
        assertNotNull(profileEditionData);

        assertEquals(profileEditionData.getAboutMe(), user.getAboutMe());
        assertEquals(profileEditionData.getName(), user.getName());
        assertEquals(profileEditionData.getBirthDate(), user.getBirthDate());
        assertEquals(profileEditionData.getCountryCode(), user.getCountryCode());
        assertEquals(profileEditionData.getCookingExp(), user.getCookingExp());
        assertEquals(profileEditionData.getGender(), user.getGender());

    }

   @Test
    public void editProfile() {

        WebTarget editTarget = target.path("users/me/edit");

        String modAboutMe = "Mofified about me";
        String modName = "Modified name";
        LocalDate modBirthDate = user.getBirthDate().plusDays(20);
        String modCountryCode = "GM";
        CookingExperience modCookingExp = CookingExperience.MASTER;
        Gender modGender = Gender.FEMALE;

        ProfileEditionData profileEditionData = new ProfileEditionData();
        profileEditionData.setAboutMe(modAboutMe);
        profileEditionData.setName(modName);
        profileEditionData.setBirthDate(modBirthDate);
        profileEditionData.setCountryCode(modCountryCode);
        profileEditionData.setCookingExp(modCookingExp);
        profileEditionData.setGender(modGender);

        Response response = editTarget.request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .put(Entity.entity(profileEditionData, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();

        User dbUser = userDAO.findBy("username", user.getUsername());
        assertEquals(profileEditionData.getAboutMe(), dbUser.getAboutMe());
        assertEquals(profileEditionData.getName(), dbUser.getName());
        assertEquals(profileEditionData.getBirthDate(), dbUser.getBirthDate());
        assertEquals(profileEditionData.getCountryCode(), dbUser.getCountryCode());
        assertEquals(profileEditionData.getCookingExp(), dbUser.getCookingExp());
        assertEquals(profileEditionData.getGender(), dbUser.getGender());

    }

    @Test
    public void searchProfile() {

        WebTarget searchTarget = target.path("users");

        Response response = searchTarget.queryParam("username", user.getUsername().substring(0,2))
                            .request(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<String> usernames = response.readEntity(new GenericType<List<String>>() {});
        assertNotNull(usernames);

        assertTrue(usernames.contains(user.getUsername()));

    }

    @After
    public void removeData() {

        userDAO.deleteAll();
        recipeDAO.deleteAll();
    }

    @AfterClass
    public static void tearDown() {

        SessionManager.getInstance().endSession("test");
        server.shutdown();
    }
}
