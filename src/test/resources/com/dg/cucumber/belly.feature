Feature: Posts
  Background: 
	Given I am using host "https://jsonplaceholder.typicode.com"

#################################################################### Reworked Section ############################################################################

#Attempt to create a post with valid details - should pass
  Scenario: I create a post success
		When I create a post with
		|userID|title|body|
		|1|test post title DG|test post body DG|
		Then the response code should be 201

#Attempt to create post with a userID that does not exist - should fail
  Scenario: I create a post fail
		When I create a post with
		|userID|title|body|
		|12|test post title DG|test post body DG|
		Then the response code should NOT be 201 # Not sure what error code should be thrown but I know it shouldn't be a 201		

#Attemp to create a post then retrieve this post from the server as a get - should pass (but won't due to lack of persistence
  Scenario: I create a post and then retrieve from server
		When I create a post with
		|userID|title|body|
		|1|test post title for retrieval|test post body for retrieval|
		Then the response code should be 201
		When I get the created post from the server
		Then the response code should be 201
		And the post should have 
		|userID|title|body|
		|1|test post title for retrieval|test post body for retrieval|

#############################################################################################################################################################################
		
  Scenario: I retrieve a post
    When I get post id 1 into post entity "simpleRetrieve"
    Then the post entity "simpleRetrieve" should have a title of "sunt aut facere repellat provident occaecati excepturi optio reprehenderit"		
		
	Scenario: I update a post
	  When I get post id 57 into post entity "simplePutBefore"
    Then the post entity "simplePutBefore" should have a userID of 6
		When I make a post entity "simplePutAfter"
		And the post entity "simplePutAfter" id is 57
		And the post entity "simplePutAfter" userID is 66
		Then I should be able to update the post with post entity "simplePutAfter"
		And the post entity "simplePutAfter_response" should have a userID of 66
		
	Scenario: I delete a post
		When I get post id 59 into post entity "simpleDeleteBefore"			
		Then I should be able to delete the post with post entity "simpleDeleteBefore" 	 