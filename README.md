# Hotel management app

**Description**: Basis of an app that allows for users to book hotel rooms.
**Features**: Authentication and sessions with JWT & Spring Security.

## Setup

## Database

MySQL database in a `Docker` container. Requires `Docker Desktop` running, then dependency
`spring-boot-docker-desktop` allows the MySQL container to run automatically when starting the app.

### Messaging

Using `Gmail SMTP` for emailing purposes (sign up validation, reset password, etc.). In
`application.properties`, adding the line `spring.profiles.active=dev` signals Spring Boot we're
using an `application-dev.properties` file for local set up that won't be added to the `Github`
repository (need to add `application-dev.properties` to the `.gitignore` manually, using it a
similar way to `.env` files, here). In this file, the username (Gmail email address) and password (
Gmail app password for this project) are added locally:

```properties
spring.mail.username=<my-gmail-username>
spring.mail.password=<my-gmail-app-password>
```

## TODO: Improvements & best practices

- [ ] Rework full JWT implement for access & refresh token (new project then transfer here)
- [ ] Add missing JavaDoc
- [ ] Refactor files to follow SOLID principles as much as possible
- [ ] Set up role-based routes & authorizations
- [ ] Continue basic CRUD routes & security
