# Reusable Module: Enterprise Cloudinary Signed Uploads

This document is a quick-reference guide for Backend Developers. The Cloudinary implementation in this project (`weeblog-backend`) has been perfectly modularized based on **Signed Direct Uploads**. 

If you start a brand new Spring Boot project in the future and want to instantly drag-and-drop this secure, zero-bandwidth Cloudinary upload system, this is exactly what you need to copy.

---

## 1. Maven Dependency
In your new project's `pom.xml`, add the official Cloudinary SDK:
```xml
<dependency>
    <groupId>com.cloudinary</groupId>
    <artifactId>cloudinary-http44</artifactId>
    <version>1.36.0</version>
</dependency>
```

## 2. Environment Variables
In your new project's `application.yaml`, map the environment variable:
```yaml
app:
  cloudinary:
    url: ${CLOUDINARY_URL}
```
In your `.env` or deployment pipeline, inject your actual secrets using this exact format:
```env
CLOUDINARY_URL=cloudinary://YOUR_API_KEY:YOUR_API_SECRET@YOUR_CLOUD_NAME
```

## 3. The Core Service (The Engine)
Copy the `CloudinaryService.java` class. It is completely standalone.
It contains two highly optimized enterprise features:
1. `generateSignature()`: Cryptographically hashes a timestamped token using your hidden `API_SECRET`.
2. `deleteMediaByUrl(String url)`: Natively reverse-engineers a raw HTTPS URL back into a `public_id`, and triggers an overwrite/destroy command to Cloudinary. This ensures if you delete a database row, the physical 50MB video is also wiped globally to prevent Cloudinary cost-leaks.

**File Location:** `src/main/java/com/blogapp/media/service/CloudinaryService.java`

## 4. The REST Controller
Copy the `MediaController.java` class. 
This simply exposes `GET /api/media/signature` so that the frontend can fetch the cryptographic ticket issued by the Service. 

*Security Note*: Make sure your new project's `SecurityConfig` locks this endpoint behind an `.authenticated()` filter to prevent unauthenticated bots from farming upload signatures!

**File Location:** `src/main/java/com/blogapp/media/controller/MediaController.java`
