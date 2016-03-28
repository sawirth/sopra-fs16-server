package ch.uzh.ifi.seal.soprafs16.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.uzh.ifi.seal.soprafs16.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;


@RestController
@RequestMapping(UserServiceController.CONTEXT)
public class UserServiceController
        extends GenericService {

    Logger                 logger  = LoggerFactory.getLogger(UserServiceController.class);

    static final String    CONTEXT = "/user";

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> listUsers() {
        logger.debug("listUsers");

        List<User> result = new ArrayList<>();
        userRepo.findAll().forEach(result::add);

        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addUser(@RequestBody User user) {
        logger.debug("addUser: " + user);

        user.setStatus(UserStatus.ONLINE);
        user.setToken(UUID.randomUUID().toString());
        try {
            user = userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            logger.error("Username " + user.getUsername() + " already exists");
            throw e;
        }

        return user;
    }


    @RequestMapping(method = RequestMethod.GET, value = "{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long userId) {
        logger.debug("getUser: " + userId);

        return userRepo.findOne(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public User login(@RequestParam String username) {
        logger.debug("login: " + username);

        User user = userRepo.findByUsername(username);
        userRepo.save(UserService.login(user));

        return user;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    @ResponseStatus(HttpStatus.OK)
    public User logout(@RequestParam("username") String username) {
        logger.debug("Logout: " + username);

        User user = userRepo.findByUsername(username);
        userRepo.save(UserService.logout(user));

        return user;
    }
}
