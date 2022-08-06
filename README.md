# Forum
Anonymous forum where users can create their discussion and reply to another users' discussions  

## Structure 
The forum consists of themes. Theme is a general description of what you can discuss in it (e.g. sport, programming, cooking).
Under each theme users can create their discussions (for example, under programming theme user can create discussion as "What programming language to choose") 
and other users can reply to them. 

## Functionality
All users can create discussions and leave comments under them. Registered users perform moderation role. They can have either ADMIN or MODERATOR roles. Users with role ADMIN have all forum functionality available for them. It is: ability to create/delete/close themes and dlscussions and delete/close users with MODERATOR role. Users wiht MODERATOR role can only delete/close discussions.

When theme is closed - no one can add new discussion under it. The same is for closed discussion but with comments.
Closed registered users can`t be authorized.

If users enter invalid data anywhere in the forum, they`ll be redirected to page that informs them about made errors.

In discussion, users can denote comments they're replying to.

## Technologies used
Spring Data JPA (with PostgreSQL), Spring Data REST, Spring Security, Thymeleaf, JUnit5.  

## Screenshots of my project

Index page for unregistered/MODERATOR/ADMIN user.

![themes](https://github.com/MaksimKosyhin/Forum/blob/img/themes.png)

Theme for unregistered/MODERATOR/ADMIN user.
First image is example of unclosed theme, the last two are closed.

![theme](https://github.com/MaksimKosyhin/Forum/blob/img/theme.png)

Closed and unclosed discussion.

![discussion](https://github.com/MaksimKosyhin/Forum/blob/img/discussion.png)

The second image shows input errors made in the first.

![error](https://github.com/MaksimKosyhin/Forum/blob/img/error.png)

Page representing registered users.

![workstaff](https://github.com/MaksimKosyhin/Forum/blob/img/workstaff.png)
