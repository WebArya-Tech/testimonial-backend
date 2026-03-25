# Frontend Integration Guide: Ask (Q&A) & Auth System

This document provides a deep, comprehensive guide for frontend developers integrating the **Ask (Q&A) Module** and its robust **Authentication System**. It covers step-by-step user and admin journeys, detailed endpoint explanations, expected JSON structures, and security handling.

---

## 1. Authentication Architecture

The application has two distinct types of users, each with their own authentication flow. Both flows ultimately provide a **JWT (JSON Web Token)** that must be attached to the `Authorization` header as `Bearer <token>` for secured requests.

### A. The User Journey (OTP Passwordless Login)
Standard users do not use passwords. They log in exclusively via email OTP.

#### Step 1: Request OTP
- **Endpoint**: `POST /api/auth/start`
- **Description**: The user enters their email address. The backend checks if the user exists (creates them if not) and sends a 6-digit OTP to that email.
- **Request Body**:
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response** (200 OK): `{ "message": "OTP sent to user@example.com" }`

#### Step 2: Verify OTP & Get Token
- **Endpoint**: `POST /api/auth/verify`
- **Description**: The user submits the OTP. If valid, the backend returns a JWT token. If this is a new user, the frontend can optionally pass `name` and `mobile` in this payload to create their profile simultaneously.
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "otp": "123456",
    "name": "Jane Doe",         // (Optional) Updates profile
    "mobile": "+1234567890"     // (Optional) Updates profile
  }
  ```
- **Response** (200 OK): 
  ```json
  {
    "token": "eyJhbGciOiJIUz...",
    "tokenType": "Bearer",
    "user": {
      "id": "abc-123",
      "email": "user@example.com",
      "name": "Jane Doe",
      "emailVerified": true
    }
  }
  ```
*(Store this `token` in `localStorage` or `sessionStorage` and append it to requests requiring auth).*

---

### B. The Admin Journey (Email/Password & OTP Reset)
Admins have high privileges and authenticate using standard passwords. A default admin is seeded into the database on startup.

#### Admin Login
- **Endpoint**: `POST /api/admin/auth/login`
- **Description**: The admin inputs their email and password. Returns a JWT token packed with the `ROLE_ADMIN` authority.
- **Request Body**:
  ```json
  {
    "email": "admin@example.com",
    "password": "securepassword123"
  }
  ```
- **Response** (200 OK):
  ```json
  {
    "token": "eyJhbGciOi...",
    "tokenType": "Bearer",
    "email": "admin@example.com"
  }
  ```

#### Admin Forgot Password
- **Endpoint**: `POST /api/admin/auth/forgot-password`
- **Description**: If the admin gets locked out, they can request an OTP. This safely dispatches a 6-digit OTP specifically tagged for `ADMIN_PASSWORD_RESET`.
- **Request Body**: `{ "email": "admin@example.com" }`

#### Admin Reset Password
- **Endpoint**: `POST /api/admin/auth/reset-password`
- **Description**: Using the OTP fetched from their email, the admin securely assigns a new password to their account.
- **Request Body**:
  ```json
  {
    "email": "admin@example.com",
    "otp": "654321",
    "newPassword": "newSecurePassword!99"
  }
  ```

---

## 2. The Ask (Q&A) System Journeys

The Q&A system mirrors a StackOverflow-like experience. Questions are organized into Categories. Users can answer questions, but answers must be approved by Admins before becoming publicly visible.

### Journey 1: Admin Sets Up The Ecosystem
Before users can interact, an Admin configures categories and posts the questions.
1. The frontend authenticates the Admin and captures the `ROLE_ADMIN` token.
2. Admin hits `POST /api/admin/categories` to create subjects (e.g., "Mathematics").
3. Admin hits `POST /api/admin/questions` containing the `categoryId`, a `title`, and a rich `descriptionHtml` containing their core question. This equation/HTML is safely sanitized by the backend.

### Journey 2: User Browses & Answers
1. Any public visitor can fetch `GET /api/categories` to populate a category filter dropdown.
2. The user calls `GET /api/questions?categoryId={id}` to view the questions inside that category.
3. The user opens a specific question (`GET /api/questions/{id}`) and simultaneously calls `GET /api/answers/question/{id}` to fetch all **APPROVED** answers to read.
4. **Submitting an Answer**: If the user wants to contribute, they must log in (Journey 1A). They write their answer in a rich-text editor. If they want to upload an image into their answer, the frontend must first hit `POST /api/media/upload` with the image file to get a secure Cloudinary URL, and embed that URL into their HTML. Finally, they submit the payload to `POST /api/answers` (attaching the JWT). The backend marks this answer as `PENDING`.

### Journey 3: User Submits a Testimonial
1. The user navigates to a Teacher's profile and clicks "Write a Review".
2. **Authentication Required**: Just like submitting answers, the user **must** log in via the OTP flow first (Journey 1A) to get a JWT token.
3. **Media Upload**: If their testimonial is a Video or Image, the frontend must first submit the `file` to `POST /api/media/upload` (with the JWT). The backend securely proxies the file to Cloudinary and returns the secure URL.
4. The frontend calls `POST /api/testimonials` containing their review text (or the newly acquired media URL).

### Journey 4: Admin Moderation
1. The Admin checks the dashboard by calling `GET /api/admin/answers?status=PENDING`.
2. The Admin evaluates the content.
3. If good: The Admin calls `PATCH /api/admin/answers/{id}/approve`. This instantly switches the status to `APPROVED`, sends an automated congratulatory email to the user, and makes the answer visible to the public.
4. If bad/spam: The Admin calls `PATCH /api/admin/answers/{id}/reject?reason=Your answer lacked detail`. The backend switches the status to `REJECTED` and safely emails the user the exact reason.

---

## 3. Comprehensive Endpoint Dictionary

### Categories
| Method | Endpoint | Auth | Body / Params | Description |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/categories` | None | - | Returns a list of all taxonomy categories. |
| **GET** | `/api/categories/{id}` | None | - | Returns a single Category object. |
| **POST** | `/api/admin/categories` | Admin | `{ "name": "..." }` | Creates a new category. Auto-generates the URL slug. |
| **PUT** | `/api/admin/categories/{id}` | Admin | `{ "name": "..." }` | Updates category name and refreshes the slug. |
| **DELETE**| `/api/admin/categories/{id}` | Admin | - | Deletes the category. |

### Questions
| Method | Endpoint | Auth | Body / Params | Description |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/questions` | None | `?categoryId=ID` `?page=0&size=10` `?sort=createdAt` `?direction=desc` | Returns a Page of questions. Supports infinite scrolling pagination and optional filtering by category. |
| **GET** | `/api/questions/{id}` | None | - | Returns the standalone Question object with its parsed HTML. |
| **GET** | `/api/questions/slug/{slug}`| None | - | Similar to above but uses the SEO-friendly URL slug. |
| **POST** | `/api/admin/questions` | Admin | `{ "title": "...", "descriptionHtml": "...", "categoryId": "..." }` | Admin creates a new question. |
| **PUT** | `/api/admin/questions/{id}` | Admin | `{ "title": "...", "descriptionHtml": "...", "categoryId": "..." }` | Updates title, HTML, or moves the question to a new category. |
| **DELETE**| `/api/admin/questions/{id}` | Admin | - | Wipes the question. |

### Testimonials
| Method | Endpoint | Auth | Body / Params | Description |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/testimonials` | None | - | Lists all globally approved testimonials. |
| **GET** | `/api/testimonials/teacher/{teacherId}` | None | - | Lists approved testimonials for a specific teacher. |
| **POST** | `/api/testimonials` | User | `{ "teacherId": "...", "reviewerName": "...", "content": "..." }` | A logged-in user submits a testimonial. If they uploaded media first, `content` is the URL. |

### Media Upload (Cloudinary)
| Method | Endpoint | Auth | Body / Params | Description |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/media/upload` | User / Admin | `multipart/form-data` containing `file` | The user MUST be logged in. Backend accepts the raw file, uploads it securely to Cloudinary, and returns `{ "url": "https://res...." }`. Use this URL inside Testimonials or Q&A Answers. |

### Answers
| Method | Endpoint | Auth | Body / Params | Description |
| :--- | :--- | :--- | :--- | :--- |
| **GET** | `/api/answers/question/{id}` | None | - | Extremely important frontend route. Fetches **ONLY** the `APPROVED` answers for a specific question ID to display publicly. |
| **POST** | `/api/answers` | User | `{ "questionId": "...", "contentHtml": "..." }` | A logged-in user submits an answer. It defaults to `PENDING` internally. |
| **GET** | `/api/admin/answers` | Admin | `?status=PENDING` `?questionId=ID` `?page=0&size=10` | Admin dashboard queue for reviewing answers. |
| **PATCH** | `/api/admin/answers/{id}/approve` | Admin | - | Switches answer to `APPROVED` and emails the author. |
| **PATCH** | `/api/admin/answers/{id}/reject` | Admin | `?reason=Explain...` | Switches answer to `REJECTED` and emails the author with the reason. |
| **DELETE**| `/api/admin/answers/{id}` | Admin | - | Hard deletes an answer. |

---

### Expected Response Format (Pagination example)
Whenever you call an endpoint that supports `page` and `size` (like `GET /api/questions`), Spring Boot returns a `Page` object structured like this:
```json
{
  "content": [
    {
      "id": "q-123",
      "title": "How to resolve integrals?",
      "category": { "id": "c-1", "name": "Maths", "slug": "maths" },
      ...
    }
  ],
  "pageable": { ... },
  "totalElements": 25,
  "totalPages": 3,
  "last": false,
  "size": 10,
  "number": 0
}
```
*Frontend implication*: You can use `totalElements` for building traditional paginators (`1 2 3 4`), or you can simply check `last === false` to render a "Load More" button.
