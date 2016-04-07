package ch.uzh.ifi.seal.soprafs16.controller;

import java.net.URL;
import java.util.List;

//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class UserServiceControllerIT {

    @Value("${local.server.port}")
    private int          port;

    private URL          base;
    private RestTemplate template;

    @Before
    public void setUp()
            throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateUserSuccess() {
        List<User> usersBefore = template.getForObject(base + "/users", List.class);

        User request = new User();
        request.setName("Mike Meyers");
        request.setUsername("mm");
        HttpEntity<User> httpEntity = new HttpEntity<>(request);
        ResponseEntity<User> response = template.exchange(base + "/users/", HttpMethod.POST, httpEntity, User.class);

        List<User> usersAfter = template.getForObject(base + "/users", List.class);
        Assert.assertEquals(usersBefore.size() + 1, usersAfter.size());

        ResponseEntity<User> userResponseEntity = template.getForEntity(base + "/users/" + response.getBody().getId(), User.class);
        User userResponse = userResponseEntity.getBody();
        Assert.assertEquals(request.getName(), userResponse.getName());
        Assert.assertEquals(request.getUsername(), userResponse.getUsername());
    }

    @Test
    public void testLoginLogout() {
        //Create new user on server
        User request = new User();
        request.setName("Horst");
        request.setUsername("horst");
        HttpEntity<User> httpEntity = new HttpEntity<>(request);
        ResponseEntity<User> userResponseEntity = template.exchange(base + "/users/", HttpMethod.POST, httpEntity, User.class);

        //since a new user created user is directly logged in we test the logout first
        userResponseEntity = template.exchange(base + "/users/logout?username=" + userResponseEntity.getBody().getUsername(),
                HttpMethod.POST, httpEntity, User.class);
        Assert.assertEquals(UserStatus.OFFLINE, userResponseEntity.getBody().getStatus());

        //now we test the login
        template.postForLocation(base + "/users/login?username=" + userResponseEntity.getBody().getUsername(), User.class);
        userResponseEntity = template.getForEntity(base + "/users/" + userResponseEntity.getBody().getId(), User.class);
        Assert.assertEquals(UserStatus.ONLINE, userResponseEntity.getBody().getStatus());
    }
}
