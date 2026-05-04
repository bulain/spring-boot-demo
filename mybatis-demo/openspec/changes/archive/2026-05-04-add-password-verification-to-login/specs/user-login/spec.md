## ADDED Requirements

### Requirement: Password Verification

The login endpoint SHALL verify that the provided password matches the stored BCrypt hash.

#### Scenario: Correct password

- **WHEN** a user submits a valid username and the correct password
- **THEN** the login succeeds and returns a token
- **AND** the user info and permission codes are included

#### Scenario: Incorrect password

- **WHEN** a user submits a valid username but an incorrect password
- **THEN** the login fails with error "用户名或密码错误"
- **AND** no token is returned
