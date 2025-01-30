package board.article.api;

import com.board.article.ArticleApplication;
import com.board.article.service.response.ArticlePageResponse;
import com.board.article.service.response.ArticleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@SpringBootTest(classes = ArticleApplication.class)
public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&pageSize=30&page=50000")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response.getArticleCount = " + response.getArticleCount());
        for (ArticleResponse article : response.getArticles()) {
            System.out.println("article = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleResponse> articles1 = restClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        System.out.println("first page");
        for (ArticleResponse response : articles1) {
            System.out.println("response.getArticleId() = " + response.getArticleId());
        }

        Long lastArticleId = articles1.getLast().getArticleId();
        List<ArticleResponse> articles2 = restClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=%s".formatted(lastArticleId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        System.out.println("second page");
        for (ArticleResponse response : articles2) {
            System.out.println("response.getArticleId() = " + response.getArticleId());
        }

    }

}
