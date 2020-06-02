package com.derrickshimada.techchallenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.BDDMockito.given;
import static java.util.Collections.singletonList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc()
class UserApplicationTests {

	@Autowired
	private UserController controller;

	@Autowired
	private MockMvc mvc;

	@MockBean
	UserRepository repository;

	@Autowired
	ObjectMapper objectMapper;

	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	@Test
	public void shouldReturnListOfUsers() throws Exception {
		User user = new User();
		user.setUsername("dshimada");
		user.setName("Derrick Shimada");
		user.setEmail("dshimada@example.com");

		List<User> allUsers = singletonList(user);

		given(controller.all()).willReturn(allUsers);

		mvc.perform(get("/User").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].username", is("dshimada")))
				.andExpect(jsonPath("$[0].name", is("Derrick Shimada")))
				.andExpect(jsonPath("$[0].email", is("dshimada@example.com")));
	}

	@Test
	public void canAddUser() throws Exception {
		User user = new User();
		user.setUsername("dshimada");
		user.setName("Derrick Shimada");
		user.setEmail("dshimada@example.com");

		List<User> allUsers = singletonList(user);
		given(controller.all()).willReturn(allUsers);

		given(controller.newUser(user)).willReturn(user);

		mvc.perform(post("/AddUser").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		mvc.perform(get("/User").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].username", is("dshimada")))
				.andExpect(jsonPath("$[0].name", is("Derrick Shimada")))
				.andExpect(jsonPath("$[0].email", is("dshimada@example.com")));
	}

	@Test
	public void canFindUserByUsername() throws Exception {
		User user = new User();
		user.setUsername("dshimada");
		user.setName("Derrick Shimada");
		user.setEmail("dshimada@example.com");

		List<User> allUsers = singletonList(user);
		given(controller.all()).willReturn(allUsers);

		given(controller.newUser(user)).willReturn(user);
		given(controller.findByUsername("dshimada")).willReturn(user);

		given(repository.findById("dshimada")).willReturn(Optional.of(user));
 
		mvc.perform(post("/AddUser").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());

		mvc.perform(get("/GetUserByUsername/dshimada").contentType(APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void cannotFindUserByUsername() throws Exception {
 
		thrown.expect(UserNotFoundException.class);
		thrown.expectMessage("Could not find user: dshimada");

		mvc.perform(get("/GetUserByUsername/dshimada").contentType(APPLICATION_JSON));

	}

	@Test
	public void canUpdateUser() throws Exception {
		User user = new User();
		user.setUsername("dshimada");
		user.setName("Derrick Shimada");
		user.setEmail("dshimada@example.com");

		User updatedUser = new User();
		updatedUser.setUsername("dshimada");
		updatedUser.setName("Test");
		updatedUser.setEmail("test@example.com");

		given(controller.newUser(user)).willReturn(user);
		
		List<User> allUsers = singletonList(user);

		given(controller.all()).willReturn(allUsers);

		given(controller.updateUser(updatedUser, "dshimada")).willReturn(updatedUser);
		
		mvc.perform(post("/AddUser").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());

		mvc.perform(put("/UpdateUser/dshimada").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		mvc.perform(get("/User").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].username", is("dshimada")))
				.andExpect(jsonPath("$[0].name", is("Derrick Shimada")))
				.andExpect(jsonPath("$[0].email", is("dshimada@example.com")));
	}

	@Test
	public void canDeleteUser() throws Exception {
		User user = new User();
		user.setUsername("dshimada");
		user.setName("Derrick Shimada");
		user.setEmail("dshimada@example.com");

		User updatedUser = new User();
		updatedUser.setUsername("dshimada");
		updatedUser.setName("Test");
		updatedUser.setEmail("test@example.com");

		given(controller.newUser(user)).willReturn(user);
		
		List<User> allUsers = singletonList(user);

		given(controller.all()).willReturn(allUsers);

		given(controller.updateUser(updatedUser, "dshimada")).willReturn(updatedUser);
		
		mvc.perform(post("/AddUser").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
		mvc.perform(delete("/DeleteUser/dshimada").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
	}
}
