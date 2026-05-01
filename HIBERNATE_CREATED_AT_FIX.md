# Hibernate Query Attribute Resolution Fix

## Problem
The application was throwing an `UnknownPathException` when searching departments:

```
org.hibernate.query.sqm.UnknownPathException: Could not resolve attribute 'created_at' of 'com.br.chatbotplatformskeleton.domain.Department'
```

The error occurred in the `DepartmentRepository.searchByNameOrCode()` method at the ORDER BY clause:
```sql
SELECT d FROM Department d 
WHERE d.isActive = true 
AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) 
     OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%'))) 
ORDER BY d.created_at DESC
```

## Root Cause
When using Spring Data JPA with custom `@Query` annotations and a `Pageable` parameter:
- The `Department` entity has a **JPA property** named `createdAt` (camelCase)
- The database column is named `created_at` (snake_case)
- Hibernate was trying to resolve `created_at` as a JPA property (not database column), which failed
- The Pageable sorting specification was using the database column name instead of the JPA property name

## Solution
Updated the `searchByNameOrCode()` query in `DepartmentRepository` to use the correct JPA property name:

**Before:**
```java
@Query("SELECT d FROM Department d WHERE d.isActive = true AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%')))")
Page<Department> searchByNameOrCode(@Param("search") String search, Pageable pageable);
```

**After:**
```java
@Query("SELECT d FROM Department d WHERE d.isActive = true AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY d.createdAt DESC")
Page<Department> searchByNameOrCode(@Param("search") String search, Pageable pageable);
```

### Key Changes:
1. Added explicit `ORDER BY d.createdAt DESC` clause to the query
2. Used the **JPA property name** `createdAt` (camelCase) instead of the database column name `created_at`
3. This prevents Pageable from appending potentially incorrect sort specifications

## Files Modified
- `/home/robertojr/chatbot-platform-skeleton/src/main/java/com/br/chatbotplatformskeleton/repository/DepartmentRepository.java`

## Verification
✅ Code compiles successfully with no errors
✅ Full Maven build completes successfully
✅ JAR file generated: `chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar`

## Best Practices for Future Development
1. Always use **JPA property names** (camelCase) in JPQL queries, not database column names (snake_case)
2. When using a `@Query` with `Pageable`, consider adding explicit ORDER BY to avoid ambiguous sort specifications
3. Database column names are only needed in native SQL queries (`@Query(nativeQuery = true)`)
4. For simple queries, prefer Spring Data's derived query methods or explicit method names to avoid these issues

