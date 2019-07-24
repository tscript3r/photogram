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
| POST | api/v1/users/login | no | Authentication, returns JWT token
| GET | api/v1/users | yes | Returns all added users
| POST | api/v1/users | no | Adds new user 
| PUT | api/v1/users/confirm_email?token= | no | Confirms new added email
| PUT | api/v1/users/reset_password?email= | no | Resets, and sends new password
| GET | api/v1/users/{id}/avatar | yes | Returns users avatar
| POST | api/v1/users/{id}/avatar | yes | Replaces users avatar with uploaded image
| GET | api/v1/users/find?username= | yes | Searches for user with given username
| GET | api/v1/users/find?email= | yes | Searches for user with given email
| GET | api/v1/users/find?username= | yes | Searches for user with given username
| GET | api/v1/users/find?id= | yes | Searches for user with given id
| GET | api/v1/users/{id}/follows | yes| Returns followed users from given user id (pageable)
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
+---main
|   +---java
|   |   \---pl
|   |       \---tscript3r
|   |           \---photogram
|   |               |   Application.java
|   |               |   
|   |               +---api
|   |               |   \---v1
|   |               |       +---controllers
|   |               |       |       CommentController.java
|   |               |       |       MappingsConsts.java
|   |               |       |       PostController.java
|   |               |       |       UserController.java
|   |               |       |       
|   |               |       +---dtos
|   |               |       |       CommentDto.java
|   |               |       |       CommentDtoList.java
|   |               |       |       Dto.java
|   |               |       |       ImageDto.java
|   |               |       |       LoginUserDto.java
|   |               |       |       PostDto.java
|   |               |       |       PostDtoList.java
|   |               |       |       RoleDto.java
|   |               |       |       UserDto.java
|   |               |       |       
|   |               |       +---mappers
|   |               |       |       AbstractMapper.java
|   |               |       |       CollectionMapper.java
|   |               |       |       CommentMapper.java
|   |               |       |       Mapper.java
|   |               |       |       PostMapper.java
|   |               |       |       RoleMapper.java
|   |               |       |       UserMapper.java
|   |               |       |       
|   |               |       \---services
|   |               |           |   MapperService.java
|   |               |           |   
|   |               |           \---beans
|   |               |                   MapperServiceBean.java
|   |               |                   
|   |               +---configs
|   |               |       EmailConfig.java
|   |               |       JwtAuthentication.java
|   |               |       JwtAuthorization.java
|   |               |       SecurityConfig.java
|   |               |       SecurityConstants.java
|   |               |       
|   |               +---domains
|   |               |       Comment.java
|   |               |       DataStructure.java
|   |               |       DomainEntity.java
|   |               |       EmailConfirmation.java
|   |               |       Image.java
|   |               |       Post.java
|   |               |       Role.java
|   |               |       User.java
|   |               |       Visibility.java
|   |               |       
|   |               +---exceptions
|   |               |       BadRequestPhotogramException.java
|   |               |       ForbiddenPhotogramException.java
|   |               |       IgnoredPhotogramException.java
|   |               |       InternalErrorPhotogramException.java
|   |               |       NotFoundPhotogramException.java
|   |               |       PhotogramException.java
|   |               |       
|   |               +---repositories
|   |               |       CommentRepository.java
|   |               |       EmailConfirmationRepository.java
|   |               |       PostRepository.java
|   |               |       RoleRepository.java
|   |               |       UserRepository.java
|   |               |       
|   |               +---services
|   |               |   |   AuthorizationService.java
|   |               |   |   CommentService.java
|   |               |   |   EmailService.java
|   |               |   |   ImageService.java
|   |               |   |   PostService.java
|   |               |   |   RoleService.java
|   |               |   |   UserService.java
|   |               |   |   VisibilityFilterService.java
|   |               |   |   
|   |               |   +---beans
|   |               |   |       AuthorizationServiceBean.java
|   |               |   |       CommentServiceBean.java
|   |               |   |       EmailServiceBean.java
|   |               |   |       ImageServiceBean.java
|   |               |   |       PostServiceBean.java
|   |               |   |       RoleServiceBean.java
|   |               |   |       UserDetailsServiceBean.java
|   |               |   |       UserServiceBean.java
|   |               |   |       
|   |               |   \---utils
|   |               |           EmailMessageType.java
|   |               |           ExpiredNonValidPostRemoveSchedule.java
|   |               |           
|   |               \---validators
|   |                       UUID.java
|   |                       
|   \---resources
|       |   application.properties
|       |   data.sql

|       +---templates
|       |       EmailConfirmationTemplate.html
|       |       PasswordResetTemplate.html
|                   
\---test
    \---java
        \---pl
            \---tscript3r
                \---photogram
                    |   ApplicationTests.java
                    |   Consts.java
                    |   
                    +---api
                    |   \---v1
                    |       +---controllers
                    |       |       CommentControllerTest.java
                    |       |       PostControllerTest.java
                    |       |       UserControllerTest.java
                    |       |       
                    |       +---dtos
                    |       |       CommentDtoTest.java
                    |       |       PostDtoTest.java
                    |       |       UserDtoTest.java
                    |       |       
                    |       +---mappers
                    |       |       CommentMapperTest.java
                    |       |       PostMapperTest.java
                    |       |       RoleMapperTest.java
                    |       |       UserMapperTest.java
                    |       |       
                    |       \---services
                    |           \---beans
                    |                   MapperServiceBeanTest.java
                    |                   
                    +---configs
                    |       JwtAuthenticationTest.java
                    |       
                    +---domains
                    |       CommentTest.java
                    |       ImageTest.java
                    |       PostTest.java
                    |       RoleTest.java
                    |       UserTest.java
                    |       
                    +---it
                    |       PostIT.java
                    |       UserIT.java
                    |       
                    \---services
                        +---beans
                        |       AuthorizationServiceBeanTest.java
                        |       CommentServiceBeanTest.java
                        |       EmailServiceBeanTest.java
                        |       ImageServiceBeanTest.java
                        |       PostServiceBeanTest.java
                        |       RoleServiceBeanTest.java
                        |       UserDetailsServiceBeanTest.java
                        |       UserServiceBeanTest.java
                        |       
                        \---utils
                                EmailMessageTypeTest.java
                                ExpiredNonValidPostRemoveScheduleTest.java
                                
```