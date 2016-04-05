package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;
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

import java.net.URL;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class GameServiceControllerIT {

    @Value("${local.server.port}")
    private int          port;

    private URL base;
    private RestTemplate template;

    @Before
    public void setUp()
            throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();

        addUser();
    }

    @Test
    public void testAddGame() {
        User user = addUser();

        HttpEntity<User> requestBody = new HttpEntity<>(user);
        ResponseEntity<Game> response = template.exchange(base + "/game/new?token="+user.getToken(), HttpMethod.POST, requestBody, Game.class);
        Assert.assertSame(1L, response.getBody().getId());
    }

    @Test
    public void testStartGame() {
        User owner = addUser();
        HttpEntity<User> requestBody = new HttpEntity<>(owner);
        ResponseEntity<Game> response = template.exchange(base + "/game/new?token=" + owner.getToken(), HttpMethod.POST, requestBody, Game.class);

        Assert.assertEquals(GameStatus.PENDING, response.getBody().getStatus());

        //Trying to start the game with one player which should not be possible
        template.postForLocation(base + "/game/" + response.getBody().getId() + "/start?token=" + owner.getToken(), response.getBody().getNextPlayer());
        response = template.getForEntity(base + "/game/" + response.getBody().getId(), Game.class);
        Assert.assertNotEquals(GameStatus.RUNNING, response.getBody().getStatus());

        //Add a second player and check if adding was successful
        User secondPlayer = addUser();
        template.postForLocation(base +  "/game/" + response.getBody().getId() + "/player?token=" + secondPlayer.getToken(), null);
        response = template.getForEntity(base + "/game/" + response.getBody().getId(), Game.class);
        Assert.assertEquals(2, response.getBody().getPlayers().size());

        //Now the owner tries to start the game which must be possible
        template.postForLocation(base + "/game/" + response.getBody().getId() + "/start?token=" + owner.getToken(), response.getBody().getNextPlayer());
        response = template.getForEntity(base + "/game/" + response.getBody().getId(), Game.class);
        Assert.assertEquals(GameStatus.RUNNING, response.getBody().getStatus());
    }

    private User addUser() {
        User request = new User();
        request.setName(String.valueOf(Math.random()));
        request.setUsername(String.valueOf(Math.random()));

        HttpEntity<User> httpEntity = new HttpEntity<>(request);
        ResponseEntity<User> response = template.exchange(base + "/user/", HttpMethod.POST, httpEntity, User.class);
        return response.getBody();
    }
}
