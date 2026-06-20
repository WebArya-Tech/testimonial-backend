# Ask Feature - Complete Integration Journey

This module enables a structured Ask/Answer system where students can ask questions, upload media, and receive answers. It supports an engagement flow where non-enrolled visitors get 3 free questions/answers before being prompted to fill out a Lead form.

## Workflow for Frontend Integration

### Step 1. Initial State & Configuration
- Ensure Minio is running locally or configured correctly.
- Ensure the Admin has pre-populated the `Grade` and `Subject` dropdown options.

**Endpoint: Get Grades**
```http
GET /api/admin/grades
```
**Response Example:**
```json
[
  {
    "id": "60d5ecb8b31f7a001550c123",
    "name": "Primary (Grades 1-5)",
    "order": 1
  }
]
```

**Endpoint: Get Subjects**
```http
GET /api/admin/subjects
```

---

### Step 2. User Authentication
All Ask/Answer interactions require the user to be logged in (they have a JWT token). If a user is not logged in, prompt them to log in first.

---

### Step 3. File Uploads (Minio)
To upload an attachment (worksheet, question paper, screenshot):
1. User selects a file.
2. Call `POST /api/media/minio/upload` using `multipart/form-data` with the key `file`.
3. Pass the JWT Token in the `Authorization` header.
> [!NOTE]
> The maximum allowed file size is 5 MB. If it exceeds, a `400 Bad Request` will be returned.

**Endpoint: Upload to Minio**
```http
POST /api/media/minio/upload
Authorization: Bearer <JWT_TOKEN>
Content-Type: multipart/form-data
```
**Payload Example:**
- Form Data Key: `file`
- Form Data Value: `[Selected File]`

**Response Example:**
```json
{
  "url": "http://127.0.0.1:9000/weeblog/c8f2b3e4-1234-5678-abcd-ef0123456789.png"
}
```

---

### Step 4. Submitting a Question/Answer
When a user calls `POST /api/questions` or `POST /api/answers`, the backend checks if their `isEnrolled` flag is true.
- If **false**, it checks if `freeAskOrAnswerCount < 3`. If so, it increments the count and accepts the submission.
- If the quota is exceeded (3 or more), the API returns a `400 Bad Request` with message `"Free quota exceeded. Please submit a Lead Form to continue."`

**Endpoint: Ask a Question**
```http
POST /api/questions
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```
**Payload Example:**
```json
{
  "title": "Integration Doubt",
  "descriptionHtml": "<p>I am struggling with inverse power rules. Please help.</p>",
  "gradeId": "60d5ecb8b31f7a001550c123",
  "subjectId": "60d5ecc2b31f7a001550c124",
  "attachments": [
    "http://127.0.0.1:9000/weeblog/c8f2b3e4-1234-5678-abcd-ef0123456789.png"
  ]
}
```

**Endpoint: Submit an Answer**
```http
POST /api/answers
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```
**Payload Example:**
```json
{
  "questionId": "60d5eccdb31f7a001550c125",
  "contentHtml": "<p>You use the power rule reversed.</p>",
  "attachments": []
}
```

---

### Step 5. Lead Form Flow
When the frontend receives the `"Free quota exceeded. Please submit a Lead Form to continue."` error, redirect the user to the **Lead Form**:

1. User enters their Email.
2. Call `POST /api/leads/send-otp` to send a verification code.

**Endpoint: Send Lead OTP**
```http
POST /api/leads/send-otp
Content-Type: application/json
```
**Payload Example:**
```json
{
  "email": "user@example.com",
  "resend": false
}
```

3. User receives OTP and enters it along with Name, Mobile, and Grade.
4. Call `POST /api/leads/submit` to verify the OTP and save the lead. The lead will now appear in the Admin Panel.

**Endpoint: Submit Lead Form**
```http
POST /api/leads/submit
Content-Type: application/json
```
**Payload Example:**
```json
{
  "email": "user@example.com",
  "otp": "123456",
  "name": "John Doe",
  "mobile": "9876543210",
  "grade": "Primary"
}
```

---

### Step 6. Searching Questions (Public)
Publicly visible questions are automatically filtered to only include `APPROVED` questions.

**Endpoint: Search/List Questions**
```http
GET /api/questions?gradeId=60d5ecb8b31f7a001550c123&subjectId=60d5ecc2b31f7a001550c124&keyword=Integration
```
**Response Example:**
```json
{
  "content": [
    {
      "id": "60d5eccdb31f7a001550c125",
      "title": "Integration Doubt",
      "slug": "integration-doubt",
      "descriptionHtml": "<p>I am struggling with inverse power rules. Please help.</p>",
      "grade": {
        "id": "60d5ecb8b31f7a001550c123",
        "name": "Primary (Grades 1-5)",
        "order": 1
      },
      "subject": {
        "id": "60d5ecc2b31f7a001550c124",
        "name": "Mathematics",
        "gradeId": "60d5ecb8b31f7a001550c123",
        "order": 1
      },
      "attachments": [
        "http://127.0.0.1:9000/weeblog/c8f2b3e4-1234-5678-abcd-ef0123456789.png"
      ],
      "status": "OPEN_TO_ANSWER",
      "approvalStatus": "APPROVED",
      "createdAt": "2026-06-16T12:00:00.000",
      "updatedAt": "2026-06-16T12:00:00.000"
    }
  ],
  "pageable": { ... },
  "totalElements": 1,
  "totalPages": 1
}
```

---

### Step 7. Moderation & Admin Actions

**Endpoint: Enroll a User (Bypass Limits)**
```http
POST /api/admin/users/{userId}/enroll
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json
```
**Payload Example:**
```json
{
  "grade": "IGCSE",
  "mobile": "1234567890",
  "name": "Jane Doe"
}
```

**Endpoint: Approve a Question**
```http
PATCH /api/admin/questions/{id}/approve?status=APPROVED
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Endpoint: Mark Answer Correct**
```http
PATCH /api/admin/answers/{id}/correct
Authorization: Bearer <ADMIN_JWT_TOKEN>
```
> [!TIP]
> Marking an answer as correct automatically updates the parent question's status to `ANSWERED`.
