# Angular Frontend (PrimeNG Tailwind Quickstart)

This is the Angular frontend for the **J2EE Membership Management System**. It provides a modern UI for member management, authentication, and Malaysian postcode/state lookup, communicating with a Jakarta EE backend via RESTful APIs.

---

## Features

- **User Authentication:** Register and login using JWT-based authentication.
- **Member Management:** Create, read, update, and delete member records.
- **Malaysian Data Integration:** Lookup for postcodes and states.
- **Protected Routes:** Only authenticated users can access member and dashboard pages.
- **Error Handling:** Automatic logout and redirect to login on authentication errors.
- **Responsive UI:** Built with PrimeNG and Angular standalone components.

---

### Main API Endpoints Used

| Endpoint             | Method | Description                      |
| -------------------- | ------ | -------------------------------- |
| `/api/auth/register` | POST   | Register a new user              |
| `/api/auth/login`    | POST   | Login and receive JWT            |
| `/api/members`       | GET    | List members (pagination/search) |
| `/api/members/:id`   | GET    | Get member by ID                 |
| `/api/members`       | POST   | Create a new member              |
| `/api/members/:id`   | PUT    | Update member                    |
| `/api/members/:id`   | DELETE | Delete member                    |
| `/api/postcodes`     | GET    | List/search postcodes            |
| `/api/states`        | GET    | List all states                  |

---

## Authentication & Route Protection

- The app uses a functional HTTP interceptor (`authInterceptor`) to:
  - Attach the JWT token to all outgoing API requests.
  - Handle 401 Unauthorized responses by logging out and redirecting to `/login`.
- Route guards (`authGuard`) ensure only authenticated users can access protected pages.

---

## API Integration

- All API requests use `/api/...` endpoints.
- In development, requests are proxied to the backend using `proxy.conf.json`.
- In production, Nginx (see `nginx.conf`) proxies `/api` to the backend.
- **JWT Authentication:**
  - On login, a JWT token is stored in `localStorage` and automatically attached to all outgoing API requests via an HTTP interceptor.
  - If a 401 Unauthorized error is received from the backend, the user is logged out and redirected to the login page.

---

### Development server (with API proxy)

Start the backend (e.g., `docker compose up` from project root), then:

```bash
npm run dev
```

- App runs at [http://localhost:4200](http://localhost:4200)
- API requests to `/api` are proxied to the backend

---

## Building for Production

```bash
npm run build
```

Build artifacts will be stored in the `dist/` directory.

---
