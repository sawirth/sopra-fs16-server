package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.TestModel;
import ch.uzh.ifi.seal.soprafs16.model.repositories.TestModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

	@Autowired
	private TestModelRepository testModelRepo;

	@RequestMapping(value="/")
	@ResponseBody
	public String index() {
		return "SoPra 2016 with Codeship!";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{number}")
	@ResponseBody
	public String test(@PathVariable String number) {
		return "You typed " + number;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/testModel")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public TestModel addTestModel(@RequestBody TestModel testModel) {

		testModel = testModelRepo.save(testModel);
		return testModel;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/testModel")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<TestModel> getTestModels() {
		List<TestModel> list = new ArrayList<>();
		testModelRepo.findAll().forEach(list::add);
		return list;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/testModel/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public String deleteTestModel(@PathVariable Long id) {
		testModelRepo.delete(id);
		return "TestModel with ID " + id + " deleted";
	}
}
