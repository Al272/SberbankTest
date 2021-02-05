import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.stream.Stream;


public class TestSberParam extends BaseTest{

    @ParameterizedTest
   @MethodSource("ms")
    public void exampleParam(String[] strings) throws InterruptedException {
        System.out.println(strings);
        String lastName= strings[0];
        String firstName= strings[1];
        String middleName=strings[2];
        String cardName=strings[3];
        String birthDate=strings[4];
        String email=strings[5];
        String phone=strings[6];

        //выбрать пункт меню "Карты"
        String MenuCardsButtonXPath = ("//a[contains(text(), 'Карты')]");
        WebElement MenuCardsButton = driver.findElement(By.xpath(MenuCardsButtonXPath));
        waitUtilElementToBeClickable(MenuCardsButton);
        MenuCardsButton.click();

        //выбрать пункт "Дебетовые карты
        String DebetCardsButtonXPath = ("//li/a[contains(text(), 'Дебетовые карты')]");
        WebElement DebetCardsButton = driver.findElement(By.xpath(DebetCardsButtonXPath));
        waitUtilElementToBeClickable(DebetCardsButton);
        DebetCardsButton.click();
        // проверка открытия страницы "Дебетовые карты"
        Assertions.assertEquals(driver.getTitle(),
                "Подобрать и заказать дебетовую карту онлайн — СберБанк");

        //выбрать пункт "Молодежная карта" и нажать на кнопку "Заказать онлайн"
        String OrderYoungCardButtonXPath = ("//span[contains(text(),'Заказать онлайн')]/parent::a[@data-product=\"Молодёжная карта\"]");
        WebElement OrderYoungCardButton = driver.findElement(By.xpath(OrderYoungCardButtonXPath));
        scrollToElementJs(OrderYoungCardButton);
        waitUtilElementToBeClickable(OrderYoungCardButton);
        OrderYoungCardButton.click();
        // проверка открытия страницы "Дебетовые карты"
        Assertions.assertEquals(driver.getTitle(),
                "Молодёжная карта СберБанка — СберБанк");

        // Нажимаем на кнопку "Оформить онлайн"
        String GotYoungCardButtonXPath = ("//span[contains(text(),'Оформить онлайн')]/parent::a[@data-test-id]");
        WebElement GotYoungCardButton = driver.findElement(By.xpath(GotYoungCardButtonXPath));
        scrollToElementJs(GotYoungCardButton);
        waitUtilElementToBeVisible(GotYoungCardButton);
        waitUtilElementToBeClickable(GotYoungCardButton);
        GotYoungCardButton.click();

        //Заполняем поля данными
        String fieldXPath = "//Input[@data-name='%s']";
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "lastName"))), lastName);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "firstName"))), firstName);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "middleName"))), middleName);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "cardName"))), cardName);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate"))), birthDate);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "email"))), email);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), phone);

        //Нажимает кнопку "Далее"
        String NextButtonXPath = ("//span[contains(text(), 'Далее')]/parent::*");
        WebElement NextButton = driver.findElement(By.xpath(NextButtonXPath));
        waitUtilElementToBeClickable(NextButton);
        NextButton.click();

        // проверить сообщение об ошибке
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "series"))), "Обязательное поле");
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "number"))), "Обязательное поле");
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "issueDate"))), "Обязательное поле");


        //endMethod
    }

    public static Stream<Arguments> ms(){
        String[] arr1 = new String[]{"Иванов","Иван","Иванович","IVAN IVANOV","01.02.1990","ivanov@yandex.ru","(111) 222-33-33"};
        String[] arr2 = new String[]{"Петров","Петр","Петрович","PETR PERTOV","02.03.1991","petrov@mail.ru","(222) 333-44-55"};
        String[] arr3 = new String[]{"Сидоров","Сидор","Сидорович","SIDOR SIDOROV","03.04.1992","sidorov@gmail.com","(333) 444-55-66"};
        return Stream.of( Arguments.of((Object) arr1),
                Arguments.of((Object) arr2),Arguments.of((Object) arr3));
    }
    private void scrollToElementJs(WebElement element) throws InterruptedException {

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void fillInputField(WebElement element, String value) throws InterruptedException {
        scrollToElementJs(element);
        waitUtilElementToBeClickable(element);
        element.clear();
        element.click();
        element.sendKeys(value);


        if(element.toString().contains("phone"))
            value = "+7 "+value;
        Assertions.assertEquals( element.getAttribute("value"), value,"Поле было заполнено некорректно");
    }

    private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        String path = "./..//div";
        if(element.toString().contains("issueDate"))
            path ="//..//..//../div[contains(text(), 'Обязательное поле')]";
        element = element.findElement(By.xpath(path));
        Assertions.assertEquals(errorMessage
                , element.getText(),"Поле было заполнено некорректно");
    }

}
