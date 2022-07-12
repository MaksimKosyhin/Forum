# Forum
Anonymous forum where users can create their discussion and reply to another users' discussions  

## Forum Structure 
The forum consists of themes. Theme is a general description of what you can discuss in it (e.g. sport, programming, cooking).
Under each theme users can create their discussions (for example, uder programming theme user can create discussion as "What programming language to choose") 
and other users can reply to them.

## Functionality
Themes, discussions, and messages to each discussion can be created and deleted. 
If users are entering invalid data, then they're redirected to page that informs them what errors they have done.  
Users can denote what messages in discussion they are answering to.  
Additionaly, themes and discussions can be closed. It means that users can't create discussions for a closed theme and can't post messages for a closed discussion.  
Users don't need to be authorized to create discussions and leave comments.

## Technologies used
Spring Data JPA (with PostgreSQL), Spring Data REST, Thymeleaf, JUnit5.  
Additionally used validation for user requests and model mapper for mapping between models and DTO objects.
