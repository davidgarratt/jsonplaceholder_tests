Feature: Posts
  Background: 
	Given I am using host "https://jsonplaceholder.typicode.com"

  Scenario: I retrieve a post
    When I get post id 1 into post entity "simpleRetrieve"
    Then the post entity "simpleRetrieve" should have a title of "sunt aut facere repellat provident occaecati excepturi optio reprehenderit"

  Scenario: I create a post
		When I make a post entity "simplePost"
		And the post entity "simplePost" userID is 1
		And the post entity "simplePost" title is "test post title DG"
		And the post entity "simplePost" body is "test post body DG"
		Then I should be able to create the post with post entity "simplePost"
		And the post entity "simplePost_response" should have a userID of 1
		And the post entity "simplePost_response" should have a title of "test post title DG"
		And the post entity "simplePost_response" should have a body of "test post body DG"
		
	Scenario: I update a post
	  When I get post id 57 into post entity "simplePutBefore"
    Then the post entity "simplePutBefore" should have a userID of 6
		When I make a post entity "simplePutAfter"
		And the post entity "simplePutAfter" id is 57
		And the post entity "simplePutAfter" userID is 66
		Then I should be able to update the post with post entity "simplePutAfter"
		And the post entity "simplePutAfter_response" should have a userID of 66
		
				 