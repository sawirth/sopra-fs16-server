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
        List<User> usersBefore = template.getForObject(base + "/user", List.class);
        Assert.assertEquals(0, usersBefore.size());

        User request = new User();
        request.setName("Mike Meyers");
        request.setUsername("mm");

        HttpEntity<User> httpEntity = new HttpEntity<User>(request);

        ResponseEntity<User> response = template.exchange(base + "/user/", HttpMethod.POST, httpEntity, User.class);
        Assert.assertSame(1L, response.getBody().getId());

        List<User> usersAfter = template.getForObject(base + "/user", List.class);
        Assert.assertEquals(1, usersAfter.size());

        ResponseEntity<User> userResponseEntity = template.getForEntity(base + "/user/" + response.getBody().getId(), User.class);
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
        ResponseEntity<User> userResponseEntity = template.exchange(base + "/user/", HttpMethod.POST, httpEntity, User.class);

        //set status to offline to test login properly
        userResponseEntity.getBody().setStatus(UserStatus.OFFLINE);

        //Test login
        userResponseEntity = template.exchange(base + "/user/login?username=" + request.getUsername(),
                HttpMethod.POST, httpEntity, User.class);
        Assert.assertEquals(UserStatus.ONLINE, userResponseEntity.getBody().getStatus());

        //Test logout
        userResponseEntity = template.exchange(base + "/user/logout?username=" + request.getUsername(),
                HttpMethod.POST, httpEntity, User.class);
        Assert.assertEquals(UserStatus.OFFLINE, userResponseEntity.getBody().getStatus());
    }
}
