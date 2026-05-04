## Why

The username/password login endpoint currently only looks up the user by username but does NOT verify that the provided password matches the stored hash. This is a security vulnerability—any password would grant access to an account if the username exists.

## What Changes

- Add password verification using BCrypt in the `/api/sys/auth/login` endpoint
- Remove outdated TODO comment

## Capabilities

### Modified Capabilities
- `user-login`: Username/password login now properly verifies credentials

## Impact

- `src/main/java/com/bulain/mybatis/sys/controller/SysAuthController.java`: Add password check logic
