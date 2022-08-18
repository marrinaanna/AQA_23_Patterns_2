package test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $(By.cssSelector("[data-test-id='login'] input")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(registeredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $(By.cssSelector("[data-test-id='login'] input")).setValue(notRegisteredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(notRegisteredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(By.cssSelector("[data-test-id='error-notification']"))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $(By.cssSelector("[data-test-id='login'] input")).setValue(blockedUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(blockedUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(By.cssSelector("[data-test-id='error-notification']"))
                .shouldHave(text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $(By.cssSelector("[data-test-id='login'] input")).setValue(wrongLogin);
        $(By.cssSelector("[data-test-id='password'] input")).setValue(registeredUser.getPassword());
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(By.cssSelector("[data-test-id='error-notification']"))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(visible);

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $(By.cssSelector("[data-test-id='login'] input")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input")).setValue(wrongPassword);
        $(By.cssSelector("[data-test-id='action-login']")).click();
        $(By.cssSelector("[data-test-id='error-notification']"))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(visible);
    }
}
