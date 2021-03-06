package com.dg.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.dg.cucumber.entities.Post;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Stepdefs {
	private String globalHostName;
	
	//placeholder - picocontainer to be implemented to create a simple global scope
	private HashMap<String, Post> postHolder = new HashMap<String, Post>();
	private ResponseEntity<Post> lastResponse;
	RestTemplate restTemplate = new RestTemplate();
	
//Preconditions	
	@Given("I am using host {string}")
	public void i_am_using_host(String hostName) {
		globalHostName = hostName;
	}

//Actions	
	@When("I get post id {int} into post entity {string}")
	public void i_get_post_number(Integer postId, String postEntityName) {
		ResponseEntity<Post> result = getEntity(HttpMethod.GET,postId);	
		postHolder.put(postEntityName, result.getBody());
	}

	@When("I make a post entity {string}")
	public void i_make_a_post_entity(String postEntityName) {
		postHolder.put(postEntityName, new Post());
	}
	
	@When("the post entity {string} id is {int}")
	public void the_post_id_is(String postEntityName, Integer id) {
		postHolder.get(postEntityName).setId(id);
	}

	@When("the post entity {string} userID is {int}")
	public void the_post_userID_is(String postEntityName, Integer userId) {
		postHolder.get(postEntityName).setUserId(userId);
	}

	@When("the post entity {string} title is {string}")
	public void the_post_title_is(String postEntityName, String title) {
	    postHolder.get(postEntityName).setTitle(title);
	}

	@When("the post entity {string} body is {string}")
	public void the_post_body_is(String postEntityName, String body) {
		postHolder.get(postEntityName).setBody(body);
	}

	@When("I get post id from {string} into post entity {string}")
	public void i_get_post_id_from_into_post_entity(String existingPostEntityName, String postEntityName) {
		int postID = postHolder.get(existingPostEntityName).getId().intValue();
		ResponseEntity<Post> result = getEntity(HttpMethod.GET,postID);	
		postHolder.put(postEntityName, result.getBody());
	}
	
	@When("I create a post with")
	public void i_create_a_post_with(List<Map<String, String>> dataTable) {
		Post newPost = new Post();
		newPost.setUserId(Integer.valueOf(dataTable.get(0).get("userID")));
		newPost.setTitle(dataTable.get(0).get("title"));
		newPost.setBody(dataTable.get(0).get("body"));
		ResponseEntity<Post> result = (ResponseEntity<Post>) sendEntityByPost(HttpMethod.POST, newPost, Post.class, "posts");
		lastResponse = result;
	}
	
	@When("I get the created post from the server")
	public void i_get_the_created_post_from_the_server() {
		ResponseEntity<Post> result = getEntity(HttpMethod.GET,lastResponse.getBody().getId());
		lastResponse = result;
	}

//Assertions
	@Then("the post should have")
	public void the_post_should_have(List<Map<String, String>> dataTable) {
		assertEquals(Integer.valueOf(dataTable.get(0).get("userID")), lastResponse.getBody().getId());
		assertEquals(dataTable.get(0).get("title"), lastResponse.getBody().getId());
		assertEquals(dataTable.get(0).get("title"), lastResponse.getBody().getId());
	}
	
	@Then("the response code should be {int}")
	public void the_response_code_should_be(Integer statusCode) {
		assertEquals((int)statusCode, (int)lastResponse.getStatusCode().value());
	}
	
	@Then("the response code should NOT be {int}")
	public void the_response_code_should_not_be(Integer statusCode) {
		assertNotEquals((int)statusCode, (int)lastResponse.getStatusCode().value());
	}

	@Then("the title of post entity {string} should be {string}")
	public void the_title_should_be(String postEntityName, String expectedTitle) {
		assertEquals(expectedTitle, postHolder.get(postEntityName).getTitle());
	}
	
	@Then("I should be able to create the post with post entity {string}")
	public void i_should_be_able_to_create_the_post(String postEntityName) {
		ResponseEntity<Post> result = (ResponseEntity<Post>) sendEntityByName(HttpMethod.POST, postEntityName, Post.class, "posts");	
		postHolder.put(postEntityName+"_response", result.getBody());
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}
	
	@Then("the post entity {string} should have a userID of {int}")
	public void the_post_entity_should_have_a_userID_of(String postEntityName, int userID) {
		assertEquals(userID, postHolder.get(postEntityName).getUserId().intValue());
	}

	@Then("the post entity {string} should have a title of {string}")
	public void the_post_entity_should_have_a_title_of(String postEntityName, String title) {
		assertEquals(title, postHolder.get(postEntityName).getTitle());
	}

	@Then("the post entity {string} should have a body of {string}")
	public void the_post_entity_should_have_a_body_of(String postEntityName, String body) {
		assertEquals(body, postHolder.get(postEntityName).getBody());
	}
	

	@Then("I should be able to update the post with post entity {string}")
	public void i_should_be_able_to_update_the_post_with_post_entity(String postHolderEntityName) {		
		ResponseEntity<Post> putResponse = (ResponseEntity<Post>) sendEntityByName(HttpMethod.PUT, postHolderEntityName, Post.class, "posts/"+postHolder.get(postHolderEntityName).getId());
		postHolder.put(postHolderEntityName+"_response", putResponse.getBody());
		assertEquals(HttpStatus.OK, putResponse.getStatusCode());
	}
	
	@Then("I should be able to delete the post with post entity {string}")
	public void i_should_be_able_to_delete_the_post_with_post_entity(String postHolderEntityName) {		
		ResponseEntity<Post> putResponse = getEntity(HttpMethod.DELETE,postHolder.get(postHolderEntityName).getId()); 
		postHolder.put(postHolderEntityName+"_response", putResponse.getBody());
		assertEquals(HttpStatus.OK, putResponse.getStatusCode());
	}

	
	
	//reusable methods
	private ResponseEntity<?> sendEntityByName(HttpMethod method, String postHolderEntityName, Class<?> inputClass, String entityName) {
		return sendEntityByPost(method, postHolder.get(postHolderEntityName),inputClass, entityName);
	}

	private ResponseEntity<?> sendEntityByPost(HttpMethod method, Post post, Class<?> inputClass, String entityName) {
		//to be extracted a made generic across all entities
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<Post> httpEntity = new HttpEntity<>(post, headers);
		//current fudge that allows put requests to specify a entity id needs rework
		ResponseEntity<?> result = restTemplate.exchange(globalHostName+"/"+entityName,
				method, httpEntity, inputClass);	
		return result;
	}
	
	private ResponseEntity<Post> getEntity(HttpMethod method, Integer postId) {
		//to be extracted a made generic across all entities
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<Post> result = restTemplate.exchange(globalHostName+"/posts/{PostId}",
				method, httpEntity, Post.class,postId);
		return result;
	}
}
