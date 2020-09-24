package com.kf.stests.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;

@Test(threadPoolSize = 2)
public class MainPageTest extends BaseTest {
    private static final String LOGO_IMG = ".fusion-logo>.fusion-logo-link";
    private static final String HOME_PAGE_URL = "https://kolsfurniture.com/";
    private static final By MENU_FORM = By.id("menu-main_menu");

    private static final String FORM_INPUT_YOUR_NAME = "input[name='your-name']";
    private static final String FORM_INPUT_SUBMIT = "input[type='submit']";
    private static final String FORM_INPUT_ACCEPT_CHECKBOX = "input[type='checkbox']";
    private static final String FORM_INPUT_ACCEPT_CHECKBOX_TERMS_LINK = "a[href='https://kolsfurniture.com/soglasie/']";
    private static final By FORM_CLOSE = By.id("fancybox-close");

    private static final By CONTACT_FORM = By.id("contact_form");
    private static final String CONTACT_FORM_EMAIL = "input[name='your-email']";
    private static final String CONTACT_FORM_MESSAGE = "textarea[name='your-message']";
    private static final String CONTACT_FORM_BUTTON = "a[href='#contact_form']>span.fusion-button-text";

    private static final By CALL_FROM = By.id("fancybox-wrap");
    private static final String CALL_FORM_BUTTON = "a[href='#contact_form_zvonok']>span.fusion-button-text";
    private static final String CALL_FORM_PHONE = "input[type='tel']";

    private static final String PHONE_LINK = "a[href='tel:+74955653557']";
    private static final String PHONE_LINK_UPPER = "div.fusion-alignleft>" + PHONE_LINK;
    private static final String PHONE_LINK_FOOTER = "footer " + PHONE_LINK;
    private static final String PHONE_NUMBER = "+7 (495) 565-35-57";

    private static final String EMAIL_LINK = "a[href='mailto:hello@kolsfurniture.com']";


    public MainPageTest() {
    }

    @BeforeMethod
    public void setUp() {
        open(HOME_PAGE_URL);
    }

    @Test
    public void menuTest() {
        $(MENU_FORM).$$(By.className("menu-text")).shouldHave(CollectionCondition.size(8));
    }

    @Test
    public void logoTest() {
        var logo = $(LOGO_IMG);
        logo.shouldBe(visible);
        logo.click();
        var currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(currentUrl, HOME_PAGE_URL, String.format("Клик по логотипу привел не на главную: %s", currentUrl));
    }

    @DataProvider
    public Object[][] data() {
        return new Object[][]{
                {PHONE_LINK_UPPER},
                {PHONE_LINK_FOOTER},
        };
    }

    @Test(dataProvider = "data")
    public void phoneTest(String linkLocator) {
        $(linkLocator).shouldBe(visible).shouldHave(text(PHONE_NUMBER));
    }

    @Test
    public void mailTest() {
        $(EMAIL_LINK).shouldBe(visible).shouldHave(text("hello@kolsfurniture.com"));
    }

    @Test
    public void emailFormTest() {
        var contactForm = $(CONTACT_FORM);

        formChecker(contactForm, CONTACT_FORM_BUTTON, "Отправить вопрос");

        var emailField = contactForm.$(CONTACT_FORM_EMAIL);
        var email = "test@example.com";
        emailField.setValue(email);
        emailField.shouldHave(exactValue(email));

        var questionFiled = contactForm.$(CONTACT_FORM_MESSAGE);
        var question = "lorem ipsum \n dolor sit amet?";
        questionFiled.setValue(question);
        questionFiled.shouldHave(exactValue(question));

        formCloser(contactForm);
    }

    @Test
    public void callFormTest() {
        var callForm = $(CALL_FROM);

        formChecker(callForm, CALL_FORM_BUTTON, "Заказать звонок");

        var phoneField = callForm.$(CALL_FORM_PHONE);
        var phone = "+7 (926) 012-34-56";
        phoneField.setValue(phone);
        phoneField.shouldHave(exactValue(phone));

        formCloser(callForm);
    }

    private void formChecker(SelenideElement form, String openFormLocator, String buttonText) {
        form.shouldNotBe(visible);

        $(openFormLocator).scrollTo().click();
        form.should(appear);

        var nameField = form.$(FORM_INPUT_YOUR_NAME);
        var name = "Name Surname Middlename";
        nameField.setValue(name);
        nameField.shouldHave(exactValue(name));

        var submitButton = form.$(FORM_INPUT_SUBMIT);
        submitButton.shouldBe(disabled);
        submitButton.shouldHave(value(buttonText));

        var checkBox = form.$(FORM_INPUT_ACCEPT_CHECKBOX);
        form.$(FORM_INPUT_ACCEPT_CHECKBOX_TERMS_LINK).shouldBe(visible);

        checkBox.setSelected(true);
        checkBox.shouldBe(selected);
        submitButton.shouldBe(enabled);
    }

    private void formCloser(SelenideElement form) {
        $(FORM_CLOSE).click();
        form.should(disappear);
    }

}