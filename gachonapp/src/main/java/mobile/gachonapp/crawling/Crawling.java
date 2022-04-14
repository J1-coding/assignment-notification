package mobile.gachonapp.crawling;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Crawling {

    private CrawlingAssignment assignment;
    private CrawlingSubject subject;
    private static final String loginURL = "https://cyber.gachon.ac.kr/login/index.php";
    private static final String parsingURL = "https://cyber.gachon.ac.kr/";

    public void getCrawledData(Map<String,String> cookies) throws IOException {

        //로그인 후 사이버 캠퍼스로부터 받은 쿠키(아이디, 비밀번호) 사용한다.
        Document document = Jsoup.connect(parsingURL)
                .cookies(cookies)
                .timeout(3000)
                .get();


        //수강 강의 리스트 만들어 저장
        List<CrawlingSubject> subjectList = new ArrayList<>();

        Elements elements = document.select("a.course_link");

        //Course 클래스에 강의 이름과 URL insert
        for (Element element : elements) {
            String courseName = element.select("h3").text();
            String courseURL = element.attr("abs:href");
            CrawlingSubject course = new CrawlingSubject(courseName, courseURL);
            subjectList.add(course);
        }

        for (CrawlingSubject course : subjectList) {
            System.out.println(course.toString());
        }

        for (CrawlingSubject course : subjectList) {
            try {
                Document document1 = Jsoup.connect(course.getURL())
                        .cookies(cookies)
                        .timeout(1000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //세션반환
    public Map<String,String> checkLogin(String id, String password) throws IOException {

        Connection.Response loginConnection = Jsoup.connect(loginURL)
                .data("username", id, "password", password)
                .method(Connection.Method.POST)
                .timeout(3000)
                .execute();

        if (isWrongUser(loginConnection)) {
            throw new IOException();
        }

        return loginConnection.cookies();
    }

    private boolean isWrongUser(Connection.Response login) {
        return login.body().contains("error_message text-danger");
    }
}