# Quick Testing Guide - CORS Fix

## What Was Fixed

The CORS error blocking login requests from the Angular frontend to the Spring Boot backend has been resolved by implementing a dedicated CORS filter that handles preflight requests before Spring Security can reject them.

## Step 1: Start the Backend

```bash
cd /home/robertojr/chatbot-platform-skeleton
java -jar target/chatbot-platform-skeleton-0.0.1-SNAPSHOT.jar
```

Expected output:
```
...
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
o.s.b.SpringApplication                  : Started ...
```

The backend will be available at: `http://localhost:8080`

## Step 2: Start the Frontend

In a new terminal:

```bash
cd /home/robertojr/chatbot-platform-skeleton/frontend
ng serve --open
```

This will open your browser to: `http://localhost:4200`

## Step 3: Test the Login

1. Navigate to the login page (should be automatic)
2. Enter credentials:
   - **Username:** admin
   - **Password:** admin
3. Click Login

### Expected Results - BEFORE Fix
❌ CORS Error in console:
```
Access to XMLHttpRequest at 'http://localhost:8080/api/auth/login' from origin 'http://localhost:4200' 
has been blocked by CORS policy...
```

### Expected Results - AFTER Fix
✅ No CORS error
✅ Login succeeds (or fails with authentication error if credentials are wrong, which is correct)
✅ Browser console is clean

## Browser Console Inspection

Open DevTools (F12) and go to **Network** tab:

### Look for the preflight request:
1. Find a request to `api/auth/login` with method `OPTIONS`
2. Check the Response Headers - should include:
   ```
   Access-Control-Allow-Origin: http://localhost:4200
   Access-Control-Allow-Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD
   Access-Control-Allow-Credentials: true
   ```

### Check the actual POST request:
1. Find the second request to `api/auth/login` with method `POST`
2. Response should be JSON with either:
   - Success: `{ "accessToken": "...", ... }`
   - Auth Error: `{ "error": "Invalid credentials" }`

## Troubleshooting

### Still getting CORS errors?

1. **Clear browser cache:**
   ```bash
   # In DevTools, right-click refresh and select "Clear site data"
   ```

2. **Check backend is running:**
   ```bash
   curl -X OPTIONS http://localhost:8080/api/auth/login -H "Origin: http://localhost:4200" -v
   ```

3. **Verify CORS configuration:**
   - Check `src/main/resources/application.properties` has:
     ```properties
     cors.allowed-origins=http://localhost:4200
     ```

### Login fails with 401 Unauthorized?

This is **normal** if:
- Credentials are wrong
- User doesn't exist in database
- JWT token is invalid

This is a **security error, not a CORS error** - the CORS fix is working!

To verify credentials are correct:
1. Check the backend logs for authentication messages
2. Try registering a new user with the register endpoint

## Files That Were Changed

✅ **Created:**
- `src/main/java/com/br/chatbotplatformskeleton/filter/CorsFilter.java`

✅ **Modified:**
- `src/main/java/com/br/chatbotplatformskeleton/config/SecurityConfig.java`
  - Added CorsFilter as a bean dependency
  - Registered it in the filter chain before JwtFilter
  - Added exception handling for auth errors

## Configuration References

### For development (default):
File: `src/main/resources/application.properties`
```properties
cors.allowed-origins=http://localhost:4200
```

### For production:
File: `src/main/resources/application-prod.properties`
```properties
cors.allowed-origins=https://yourdomain.com
```

Run with: `--spring.profiles.active=prod`

## Next Steps

After confirming the login works:

1. Test other API endpoints from the frontend
2. Configure production CORS origins in `application-prod.properties`
3. Deploy to production with the appropriate domain names

---

**The CORS issue is now fixed! Your frontend can now communicate with the backend API.**

