# Photogram - backend [![CircleCI](https://circleci.com/gh/tscript3r/photogram.svg?style=svg)](https://circleci.com/gh/tscript3r/photogram)

### Table of Contents
*    [General info](#general-info)
*    [Technologies](#technologies)
*    [Running locally](#running-photogram-backend-locally)
*    [Project tree](#project-tree)
*    [Endpoints](#endpoints)

### General info
REST API web service - current, main functionalities: user registration (with the requirement to confirm the email),
logging in; CRUD operations for posts (to which images can be sent), and comments.

### Technologies
*    Java 11
*    Spring Boot 2.1.4
*    Spring MVC
*    Spring Data JPA
*    Spring Security
*    Spring Mail
*    Spring Thymeleaf
*    Spring Actuator
*    Spring Devtools
*    Mockito 2.22.0
*    JUnit 5
*    Lombok
*    JWT
*    H2

#### Running photogram-backend locally
```
git clone https://github.com/tscript3r/photogram.git
cd photogram/backend
./mvnw package
java -jar target/*.jar
```

### Endpoints
| Method | URI | Login required | Description |
| --- | --- | --- | --- | 
| GET | api/v1/users | yes | Returns all added users
| POST | api/v1/users | no | Adds new user 
| POST | api/v1/users/login | no | Authenticates, returns JWT token
| PUT | api/v1/users/confirm_email?token | no | Confirms new added email
| PUT | api/v1/users/reset_password?email | no | Resets, and sends new password via email
| GET | api/v1/users/{id}/avatar | yes | Returns users avatar
| POST | api/v1/users/{id}/avatar | yes | Replaces users avatar with uploaded image
| GET | api/v1/users/find?username | yes | Searches for user with given username
| GET | api/v1/users/find?email | yes | Searches for user with given email
| GET | api/v1/users/find?username | yes | Searches for user with given username
| GET | api/v1/users/find?id | yes | Searches for user with given id
| GET | api/v1/users/{id}/follows | yes| Returns followed users by given user id (pageable)
| GET | api/v1/users/{id}/followers | yes | Returns followers from given user (pageable)
| GET | api/v1/posts | yes | Returns recent posts (pageable)
| POST | api/v1/posts | yes | Adds new post
| GET | api/v1/posts/{id} | yes | Returns given post
| PUT | api/v1/posts/{id} | yes | Updates given post
| DELETE | api/v1/posts/{id} | yes | Deletes given post
| PUT | api/v1/posts/{id}/like | yes | Likes given post
| PUT | api/v1/posts/{id}/unlike | yes | Unlikes (if liked) given post
| PUT | api/v1/posts/{id}/dislike | yes | Dislikes given post
| PUT | api/v1/posts/{id}/undislike | yes | Undislikes (if liked) given post
| GET | api/v1/posts/{id}/comments | yes | Returns recent comments (pageable)
| POST | api/v1/posts/{id}/comments | yes | Adds new comment
| GET | api/v1/posts/{id}/comments/{id} | yes | Returns given comment
| PUT | api/v1/posts/{id}/comments/{id} | yes | Updates given comment
| DELETE | api/v1/posts/{id}/comments/{id} | yes | Deletes given comment
| POST | api/v1/posts/{id}/images/upload | yes | Adds new image
| GET | api/v1/posts/{id}/images/{imageId} | yes | Returns given image


#### Project tree
```
├───main
│   ├───java
│   │   └───pl
│   │       └───tscript3r
│   │           └───photogram
│   │               │   Application.java
│   │               │
│   │               ├───infrastructure
│   │               │   │   AbstractEntity.java
│   │               │   │   MappingsConsts.java
│   │               │   │
│   │               │   ├───aop
│   │               │   │       LoggingAspect.java
│   │               │   │
│   │               │   ├───configuration
│   │               │   │       EmailConfig.java
│   │               │   │       JwtAuthentication.java
│   │               │   │       JwtAuthorization.java
│   │               │   │       PasswordEncoderConfig.java
│   │               │   │       SecurityConfig.java
│   │               │   │       SecurityConstants.java
│   │               │   │
│   │               │   ├───exception
│   │               │   │       BadRequestPhotogramException.java
│   │               │   │       ForbiddenPhotogramException.java
│   │               │   │       IgnoredPhotogramException.java
│   │               │   │       InternalErrorPhotogramException.java
│   │               │   │       NotFoundPhotogramException.java
│   │               │   │       PhotogramException.java
│   │               │   │
│   │               │   └───mapper
│   │               │           AbstractMapper.java
│   │               │           CollectionMapper.java
│   │               │           DataStructure.java
│   │               │           Dto.java
│   │               │           Mapper.java
│   │               │           MapperService.java
│   │               │           MapperServiceBean.java
│   │               │
│   │               ├───post
│   │               │   │   ExpiredNonValidPostRemoveSchedule.java
│   │               │   │   Post.java
│   │               │   │   PostRepository.java
│   │               │   │   PostService.java
│   │               │   │   Reactions.java
│   │               │   │   Visibility.java
│   │               │   │
│   │               │   ├───api
│   │               │   │   └───v1
│   │               │   │           PostController.java
│   │               │   │           PostDto.java
│   │               │   │           PostDtoList.java
│   │               │   │           PostMapper.java
│   │               │   │
│   │               │   ├───comment
│   │               │   │   │   Comment.java
│   │               │   │   │   CommentRepository.java
│   │               │   │   │   CommentService.java
│   │               │   │   │
│   │               │   │   └───api
│   │               │   │       └───v1
│   │               │   │               CommentController.java
│   │               │   │               CommentDto.java
│   │               │   │               CommentDtoList.java
│   │               │   │               CommentMapper.java
│   │               │   │
│   │               │   └───image
│   │               │       │   Image.java
│   │               │       │   ImageService.java
│   │               │       │
│   │               │       └───api
│   │               │           └───v1
│   │               │                   ImageDto.java
│   │               │
│   │               └───user
│   │                   │   AuthorizationService.java
│   │                   │   User.java
│   │                   │   UserDetailsServiceImpl.java
│   │                   │   UserRepository.java
│   │                   │   UserService.java
│   │                   │
│   │                   ├───api
│   │                   │   └───v1
│   │                   │           LoginUserDto.java
│   │                   │           RoleDto.java
│   │                   │           UserController.java
│   │                   │           UserDto.java
│   │                   │           UserMapper.java
│   │                   │           UUID.java
│   │                   │
│   │                   ├───email
│   │                   │       EmailConfirmation.java
│   │                   │       EmailConfirmationRepository.java
│   │                   │       EmailMessageType.java
│   │                   │       EmailService.java
│   │                   │
│   │                   └───role
│   │                           Role.java
│   │                           RoleMapper.java
│   │                           RoleRepository.java
│   │                           RoleService.java
│   │
│   └───resources
│       │   application.properties
│       │   data.sql
│       │
│       ├───posts
│       │   ├───1
│       │   │   └───images
│       │   │           1000
│       │   │
│       │   ├───2
│       │   │   └───images
│       │   │           1000
│       │   │
│       │   ├───3
│       │   │   └───images
│       │   │           1000
│       │   │
│       │   ├───4
│       │   │   └───images
│       │   │           1000
│       │   │
│       │   ├───5
│       │   │   └───images
│       │   │           1000
│       │   │
│       │   └───6
│       │       └───images
│       │               1000
│       │
│       ├───templates
│       │       EmailConfirmationTemplate.html
│       │       PasswordResetTemplate.html
│       │
│       └───users
│           │   default_avatar.png
│           │
│           ├───1
│           │       avatar
│           │
│           └───2
│                   avatar
│
└───test
    └───java
        └───pl
            └───tscript3r
                └───photogram
                    │   ApplicationTests.java
                    │   Consts.java
                    │
                    ├───api
                    │   └───v1
                    │       ├───controllers
                    │       │       CommentControllerTest.java
                    │       │       PostControllerTest.java
                    │       │       UserControllerTest.java
                    │       │
                    │       ├───dtos
                    │       │       CommentDtoTest.java
                    │       │       PostDtoTest.java
                    │       │       UserDtoTest.java
                    │       │
                    │       ├───mappers
                    │       │       CommentMapperTest.java
                    │       │       PostMapperTest.java
                    │       │       RoleMapperTest.java
                    │       │       UserMapperTest.java
                    │       │
                    │       └───services
                    │           └───beans
                    │                   MapperServiceBeanTest.java
                    │
                    ├───domains
                    │       CommentTest.java
                    │       ImageTest.java
                    │       PostTest.java
                    │       RoleTest.java
                    │       UserTest.java
                    │
                    ├───infrastructure
                    │   └───configuration
                    │           JwtAuthenticationTest.java
                    │
                    ├───it
                    │       PostIT.java
                    │       UserIT.java
                    │
                    └───services
                        ├───beans
                        │       AuthorizationServiceBeanTest.java
                        │       CommentServiceTest.java
                        │       EmailServiceTest.java
                        │       ImageServiceTest.java
                        │       PostServiceTest.java
                        │       RoleServiceTest.java
                        │       UserDetailsServiceImplTest.java
                        │       UserServiceTest.java
                        │
                        └───utils
                                EmailMessageTypeTest.java
                                ExpiredNonValidPostRemoveScheduleTest.java
                                
```