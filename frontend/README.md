Chatbot Platform - Frontend (Angular skeleton)

Quick start

1. Install dependencies:
   npm install

2. Run dev server with proxy (recommended):
   ng serve --proxy-config proxy.conf.json
   or using npm script:
   npm start

3. Use the app:
   - Open http://localhost:4200
   - Visit /login to authenticate (default admin/admin created by backend DataLoader)
   - After login go to /bots to view/create bots

Notes

- This is a minimal skeleton with an AuthService, AuthGuard, an HTTP interceptor, and a Bots module that consumes /api/bots endpoints.
- Proxy config `proxy.conf.json` forwards /api to http://localhost:8080 to avoid CORS in development.

Proxy vs direct API URL

- By default `environment.apiUrl` is empty which makes the frontend request the same origin (useful when using the `proxy.conf.json` during development or serving frontend together with backend).
- To target a backend running on another host in development or production, set `environment.apiUrl` (or replace at build time) with the backend base URL, for example:

```ts
// src/environments/environment.ts
export const environment = { production: false, apiUrl: 'http://localhost:8080' };
```

CORS notes (backend)

- The backend has a global CORS config with a property `cors.allowed-origins` (defaults to `http://localhost:4200`).
- To allow multiple origins, set a comma-separated value:
  `cors.allowed-origins=http://localhost:4200,http://example.com`

Docker

- Build image:

```bash
# from frontend/ directory
docker build -t chatbot-frontend:local .
```

- Run container:

```bash
docker run -p 8080:80 chatbot-frontend:local
```

Production

- Update API base URLs as needed; consider environment variables or config file to point to backend API host.

Tests

- Run unit tests (Karma/Jasmine):

```bash
npm test
```

Angular Material

- This skeleton uses Angular Material components. After installing dependencies (npm install), you can run `ng add @angular/material` to configure a theme and animations properly in a real project. Here we included basic Material modules and a `MaterialModule` aggregator.
