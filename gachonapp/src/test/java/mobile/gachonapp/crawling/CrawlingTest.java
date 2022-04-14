package mobile.gachonapp.crawling;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class CrawlingTest {

    Crawling crawling = new Crawling();
    static String loginURL = "https://cyber.gachon.ac.kr/login/index.php";
    static String parsingURL = "https://cyber.gachon.ac.kr/";
//    static String id = "jmk7117";
//    static String password = "als968574@";

    @Test
    void 로그인_성공시_세션발급() throws IOException {
        String id = "jmk7117";
        String password = "als968574@";

        Connection.Response login = Jsoup.connect(loginURL)
                .data("username", id, "password", password)
                .method(Connection.Method.POST)
                .timeout(3000)
                .execute();

        Assertions.assertThat(login.cookies().containsKey("MoodleSession")).isTrue();

    }

    @Test()
    void 로그인정보가_잘못된_사용자시_에러던지기() throws IOException {

        //given
        Crawling crawling = new Crawling();
        String wrongUsername = "123sfsdfs3";
        String wrongPassword = "sfsfsfsddf";

        assertThrows(IOException.class, () -> {
            crawling.checkLogin(wrongUsername,wrongPassword);
        });
    }



    @Test
    void getCrawledData() {
    }

    @Test
    void checkLogin() {
    }
}