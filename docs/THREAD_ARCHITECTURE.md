# Q&A Thread Architecture (StackOverflow-Style)

This document outlines how the backend structures the Q&A feature to resemble a modern, StackOverflow-style discussion thread.

## 1. Modular Metadata (Authoring)
In modern thread-based systems, every piece of content (Question, Answer, Comment) must be able to uniquely identify its author while keeping the frontend payload clean.
Instead of exposing raw strings like `adminId` or `userId` in the API payload, we encapsulate all user details into a standardized `AuthorDTO`:

```json
"author": {
  "id": "60d5ecb8b392d7001f5f3e9a",
  "name": "John Doe",
  "role": "STUDENT" // or "ADMIN"
}
```

This guarantees that whether a user is looking at a Question or an Answer, the frontend will always find the author information in `item.author.name` and `item.author.role`.

## 2. Chronological Ordering
To ensure threads flow logically from top to bottom (or bottom to top), every Question and Answer includes:
- `createdAt`: The precise time the entity was created.
- `updatedAt`: The time of the last edit.

The API endpoints inherently support sorting by these fields. 
For example, fetching answers sorted chronologically:
`GET /api/answers/question/{id}?sort=createdAt&direction=asc` (Oldest answers first, natural reading order).
`GET /api/answers/question/{id}?sort=createdAt&direction=desc` (Newest answers first).

## 3. Workflow & Verification States
A thread is a living document. It transitions through statuses:

### Questions
- **`PENDING`**: Awaiting admin moderation. Hidden from the public thread.
- **`APPROVED`**: Visible on the main feed and open to answers.
- **`CLOSED`**: The thread is locked; no new answers can be submitted.
- **`ANSWERED`**: The thread has a verified, accepted answer.

### Answers
- **`PENDING`**: Awaiting admin moderation.
- **`APPROVED`**: Visible on the thread under the question.
- **`REJECTED`**: Hidden, typically with a `rejectionReason`.
- **`isCorrect`**: True if this answer is the accepted solution. When an answer is marked correct, the parent Question status automatically upgrades to `ANSWERED`.

## 4. Scalability (Pagination)
Like StackOverflow, threads can grow infinitely. The backend returns paginated results (`Page<QuestionResponse>` and `Page<AnswerResponse>`) out of the box. This prevents massive payload bottlenecks.

## 5. Fetching Threads (Question + Answers)
In the database, every `Answer` strictly references its parent via a `questionId`. 

To load a full thread without causing a performance bottleneck, the frontend makes two simultaneous REST calls:
1. **Fetch the Question**: `GET /api/questions/{id}`
2. **Fetch the Answers**: `GET /api/answers/question/{id}?page=0&size=20`

This decoupled approach ensures the main question renders instantly, while the answers can be infinitely loaded or paginated as the user scrolls.

## 6. Search and Filtering
The main public endpoint (`GET /api/questions`) is natively equipped with deep filtering capabilities. It allows the frontend to dynamically narrow down the feed using optional query parameters:
- `keyword`: Searches text inside the question's title and HTML description.
- `gradeId`: Filters the feed to a specific class (e.g., Grade 10).
- `subjectId`: Filters the feed to a specific subject (e.g., Physics).

**Example Usage**:  
`GET /api/questions?keyword=algebra&gradeId=60d5ec&subjectId=70e6fc&sort=createdAt&direction=desc`  
This instantly queries the database for the newest Algebra questions strictly within the selected Grade and Subject.
