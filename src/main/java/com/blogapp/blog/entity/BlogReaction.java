package com.blogapp.blog.entity;

import com.blogapp.blog.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blog_reactions")
@CompoundIndexes({
    @CompoundIndex(name = "blogId_visitorKey_idx", def = "{'blogId': 1, 'visitorKey': 1}", unique = true)
})
public class BlogReaction {

    @Id
    private String id;

    private String blogId;

    private String visitorKey;

    private ReactionType type;

    private String ipAddress;

    @org.springframework.data.annotation.CreatedDate
    private java.time.LocalDateTime createdAt;
}
