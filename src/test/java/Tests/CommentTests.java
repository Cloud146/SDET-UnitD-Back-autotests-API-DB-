package Tests;

import Helpers.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.net.http.HttpResponse;

import static org.testng.Assert.*;

@Epic("API DB Testing")
@Feature("WordPress Comments")
public class CommentTests {
    private WordPressClient client;
    private ObjectMapper objectMapper;

    @Story("Basic Auth Set Up")
    @BeforeClass
    public void setUp() {
        String baseUrl = "http://localhost:8000";
        String username = "Firstname.LastName";
        String password = "123-Test";
        client = new WordPressClient(baseUrl, username, password);
        objectMapper = new ObjectMapper();
    }

    @Story("Создание комментария к существующему посту")
    @Severity(SeverityLevel.BLOCKER)
    @Test(description = "Тест создания комментария к существующему посту с postId")
    public void createCommentByPostIdTest() throws Exception {
        int postId = 190;

        Comment comment = new Comment(postId, "Автор", "author@example.com", "Содержимое комментария");
        HttpResponse<String> createCommentResponse = client.createComment(comment);
        assertEquals(createCommentResponse.statusCode(), 201);

        JsonNode commentResponseBody = objectMapper.readTree(createCommentResponse.body());
        int commentId = commentResponseBody.get("id").asInt();

        String commentContent = DataBaseHelper.getCommentById(commentId);
        assertEquals(commentContent, "Содержимое комментария");
    }

    @Story("Обновление комментария у существующего поста")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Тест обновления комментария у существующего поста с postId")
    public void updateCommentByIdTest() throws Exception {
        int postId = 190;
        int commentId = 30;

        Comment updatedComment = new Comment(postId, "Автор", "author@example.com", "Обновленное содержимое комментария");
        HttpResponse<String> updateCommentResponse = client.updateComment(commentId, updatedComment);
        assertEquals(updateCommentResponse.statusCode(), 200);

        String updatedCommentContent = DataBaseHelper.getCommentById(commentId);
        assertEquals(updatedCommentContent, "Обновленное содержимое комментария");
    }

    @Story("Удаление комментария у существующего поста")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Тест удаления комментария у существующего поста с postId")
    public void deleteCommentByIdTest() throws Exception {
        int postId = 190;
        int commentId = 30;

        HttpResponse<String> deleteCommentResponse = client.deleteComment(postId ,commentId);
        assertEquals(deleteCommentResponse.statusCode(), 200);

        DataBaseHelper.checkCommentDeleted(commentId);
    }

    @Story("Создание поста и добавление комментария к нему")
    @Severity(SeverityLevel.BLOCKER)
    @Test(description = "Тест создания поста и добавления комментария к нему")
    public void createPostAndCommentTest() throws Exception {
        Post post = new Post("Заголовок поста", "Содержимое поста", "publish");
        HttpResponse<String> createPostResponse = client.createPost(post);
        assertEquals(createPostResponse.statusCode(), 201);

        JsonNode postResponseBody = objectMapper.readTree(createPostResponse.body());
        int postId = postResponseBody.get("id").asInt();

        Comment comment = new Comment(postId, "Автор", "author@example.com", "Содержимое комментария");
        HttpResponse<String> createCommentResponse = client.createComment(comment);
        assertEquals(createCommentResponse.statusCode(), 201);

        JsonNode commentResponseBody = objectMapper.readTree(createCommentResponse.body());
        int commentId = commentResponseBody.get("id").asInt();

        String commentContent = DataBaseHelper.getCommentById(commentId);
        assertEquals(commentContent, "Содержимое комментария");
    }

    @Story("Создание поста, добавление и обновление комментария к нему")
    @Severity(SeverityLevel.BLOCKER)
    @Test(description = "Тест создания поста, добавления и обновления комментария к нему")
    public void createPostAndCommentAndUpdateCommentTest() throws Exception {
        Post post = new Post("Заголовок поста", "Содержимое поста", "publish");
        HttpResponse<String> createPostResponse = client.createPost(post);
        assertEquals(createPostResponse.statusCode(), 201);

        JsonNode postResponseBody = objectMapper.readTree(createPostResponse.body());
        int postId = postResponseBody.get("id").asInt();

        Comment comment = new Comment(postId, "Автор", "author@example.com", "Содержимое комментария");
        HttpResponse<String> createCommentResponse = client.createComment(comment);
        assertEquals(createCommentResponse.statusCode(), 201);

        JsonNode commentResponseBody = objectMapper.readTree(createCommentResponse.body());
        int commentId = commentResponseBody.get("id").asInt();

        Comment updatedComment = new Comment(postId, "Автор", "author@example.com", "Обновленное содержимое комментария");
        HttpResponse<String> updateCommentResponse = client.updateComment(commentId, updatedComment);
        assertEquals(updateCommentResponse.statusCode(), 200);

        String updatedCommentContent = DataBaseHelper.getCommentById(commentId);
        assertEquals(updatedCommentContent, "Обновленное содержимое комментария");
    }

    @Story("Создание поста, добавление и удаление комментария у него")
    @Severity(SeverityLevel.BLOCKER)
    @Test(description = "Тест создания поста, добавления и удаления комментария у него")
    public void cratePostAndCommentAndDeleteCommentTest() throws Exception {
        Post post = new Post("Заголовок поста", "Содержимое поста", "publish");
        HttpResponse<String> createPostResponse = client.createPost(post);
        assertEquals(createPostResponse.statusCode(), 201);

        JsonNode postResponseBody = objectMapper.readTree(createPostResponse.body());
        int postId = postResponseBody.get("id").asInt();

        Comment comment = new Comment(postId, "Автор", "author@example.com", "Содержимое комментария");
        HttpResponse<String> createCommentResponse = client.createComment(comment);
        assertEquals(createCommentResponse.statusCode(), 201);

        JsonNode commentResponseBody = objectMapper.readTree(createCommentResponse.body());
        int commentId = commentResponseBody.get("id").asInt();

        HttpResponse<String> deleteCommentResponse = client.deleteComment(postId ,commentId);
        assertEquals(deleteCommentResponse.statusCode(), 200);

        DataBaseHelper.checkCommentDeleted(commentId);
    }
}
