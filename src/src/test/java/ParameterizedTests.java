
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class ParameterizedTests {

    @BeforeEach
    void setUp() {
        open("https://github.com/");
    }

    @ValueSource(strings = {
        "java", "selenide", "selenium"
    })
    @ParameterizedTest (name = "Для поиского запроса {0} не должна возвращаться пустая страница")
    @Tag("WEB")
    void githubSearchResultsShouldNotBeEmpty(String searchQuery){
    $("[data-target='qbsearch-input.inputButtonText']").click();
    $("#query-builder-test").setValue(searchQuery).pressEnter();
    $("[data-testid='results-list']").shouldBe(visible);
    }

    @CsvSource(value = {
            "java , TheAlgorithms/",
            "selenide , selenide/",
            "selenium , SeleniumHQ/"
    })
    @ParameterizedTest (name = "Для поиского запроса {0} в первой карточке должна быть текст {1}")
    @Tag("WEB")
    void githubSearchShouldContainExpectedUrl(String searchQuery, String expectedLink){
        $("[data-target='qbsearch-input.inputButtonText']").click();
        $("#query-builder-test").setValue(searchQuery).pressEnter();
        $("[data-testid='results-list']").shouldHave(text(expectedLink));
    }

    static Stream<Arguments> githubRepoHeaderShouldBeCorrect() {
        return Stream.of(
                Arguments.of("TheAlgorithms/Java", "Java"),
                Arguments.of("selenide/selenide", "selenide"),
                Arguments.of("SeleniumHQ/selenium", "selenium")
        );
    }

    @MethodSource()
    @ParameterizedTest(name = "Для репозитория {0} должен отображаться заголовок {1}")
    @Tag("WEB")
    void githubRepoHeaderShouldBeCorrect(String url, String correctTitle) {
        open("https://github.com/" + url);
        $("strong.mr-2").shouldHave(text(correctTitle));
    }
}
