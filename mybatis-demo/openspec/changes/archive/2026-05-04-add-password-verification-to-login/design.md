## Context

The `/api/sys/auth/login` endpoint currently:
1. Looks up user by username
2. Returns token + user info + permissions if user exists

The password from `LoginDTO` is not checked against the stored BCrypt hash.

## Goals / Non-Goals

**Goals:**
- Add password verification to prevent unauthorized access
- Use existing BCrypt pattern already used in `SysUserServiceImpl`

**Non-Goals:**
- Refactor auth flow
- Add new authentication methods
- Change password hashing algorithm

## Decisions

### Decision 1: Use BCryptPasswordEncoder directly in controller

The password encoder is already instantiated in `SysUserServiceImpl`. We'll add it to `SysAuthController` to keep the change minimal and localized.

- Rationale: Smallest change, consistent with existing pattern
- Tradeoff: Duplicate encoder instance - acceptable for this small fix
