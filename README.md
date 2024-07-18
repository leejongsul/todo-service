# TODO 서비스

사용자가 할 일(TODO)을 생성하고 관리를 할 수 있는 서비스 입니다.

기술 과제로 진행했던 프로젝트로 완성도를 높이는 것이 목적 입니다.

# 개발 환경

* JDK 17
* Kotlin 1.9.24
* Spring boot 3.3.0
* JJWT 0.12.5

# 서비스 흐름

1. 회원 가입 (/api/users/signUp)
2. 인증 (/api/auth)에서 accessToken, refreshToken을 발급 받는다.
3. 회원은 accessToken을 사용하여 서비스를 사용 합니다
4. 회원의 accessToken이 만료가 되었다면 재발급(/api/auth/refresh)에서 발급 받는다.
5. 회원의 refreshToken이 만료되면 2번 항목을 통해서 토큰을 발급 받는다.

# 프로젝트 구조

현재는 `Layered Architecture`로 되어있습니다.

```
.gitignore
build.gradle.kts
gradle
   |-- wrapper
   |   |-- gradle-wrapper.jar
   |   |-- gradle-wrapper.properties
gradlew
gradlew.bat
settings.gradle.kts
src
   |-- main
   |   |-- kotlin
   |   |   |-- com
   |   |   |   |-- leejongsul
   |   |   |   |   |-- todolist
   |   |   |   |   |   |-- TodoListApplication.kt
   |   |   |   |   |   |-- config
   |   |   |   |   |   |   |-- SecurityConfig.kt
   |   |   |   |   |   |-- controllers
   |   |   |   |   |   |   |-- AuthController.kt
   |   |   |   |   |   |   |-- ExceptionControllerAdvice.kt
   |   |   |   |   |   |   |-- TodoController.kt
   |   |   |   |   |   |   |-- UserController.kt
   |   |   |   |   |   |-- dto
   |   |   |   |   |   |   |-- AuthenticationRequest.kt
   |   |   |   |   |   |   |-- AuthenticationResponse.kt
   |   |   |   |   |   |   |-- CreateTodoRequest.kt
   |   |   |   |   |   |   |-- RefreshTokenRequest.kt
   |   |   |   |   |   |   |-- SignUpRequest.kt
   |   |   |   |   |   |   |-- UpdateTodoStatusRequest.kt
   |   |   |   |   |   |-- entities
   |   |   |   |   |   |   |-- Todo.kt
   |   |   |   |   |   |   |-- User.kt
   |   |   |   |   |   |-- exceptions
   |   |   |   |   |   |   |-- RefreshTokenException.kt
   |   |   |   |   |   |   |-- UpdateTodoStatusException.kt
   |   |   |   |   |   |-- repositories
   |   |   |   |   |   |   |-- TodoRepository.kt
   |   |   |   |   |   |   |-- UserRepository.kt
   |   |   |   |   |   |-- security
   |   |   |   |   |   |   |-- CustomUserDetails.kt
   |   |   |   |   |   |   |-- JwtAuthenticationFilter.kt
   |   |   |   |   |   |   |-- JwtTokenProvider.kt
   |   |   |   |   |   |-- services
   |   |   |   |   |   |   |-- AuthenticationService.kt
   |   |   |   |   |   |   |-- CustomUserDetailsService.kt
   |   |   |   |   |   |   |-- TodoService.kt
   |   |   |   |   |   |   |-- UserService.kt
   |   |-- resources
   |   |   |-- application.yml
   |-- test
   |   |-- kotlin
   |   |   |-- com
   |   |   |   |-- leejongsul
   |   |   |   |   |-- todolist
   |   |   |   |   |   |-- IntegrateControllerTest.kt
   |   |   |   |   |   |-- TodoListApplicationTests.kt

```