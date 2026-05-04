## ADDED Requirements

### Requirement: User can export all users by query
The system SHALL allow users to export all user data that matches the given query criteria in Excel format. The export SHALL be performed in a streaming manner to avoid OOM issues.

#### Scenario: Export all users with empty query
- **WHEN** user calls the export endpoint with no query parameters
- **THEN** system exports all users in Excel format
- **AND** the export uses streaming to avoid OOM

#### Scenario: Export filtered users by username
- **WHEN** user calls the export endpoint with a username filter
- **THEN** system exports only users matching the username criteria
- **AND** the export uses streaming to avoid OOM

#### Scenario: Export filtered users by name
- **WHEN** user calls the export endpoint with a name filter
- **THEN** system exports only users matching the name criteria
- **AND** the export uses streaming to avoid OOM

### Requirement: User can export selected users by ID
The system SHALL allow users to export specific users by providing a list of user IDs. The export SHALL be performed in a streaming manner.

#### Scenario: Export selected users by multiple IDs
- **WHEN** user calls the export endpoint with a list of valid user IDs
- **THEN** system exports only the users matching those IDs
- **AND** the export uses streaming to avoid OOM

#### Scenario: Export with empty ID list
- **WHEN** user calls the export endpoint with an empty ID list
- **THEN** system SHALL return an error indicating no users were selected

#### Scenario: Export with some non-existent IDs
- **WHEN** user calls the export endpoint with a list containing some non-existent IDs
- **THEN** system SHALL export only the existing users without error
