import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.*;

public class TestSber extends BaseTest {

    @Test
    public void example() throws InterruptedException {
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
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "lastName"))), "Бурлаков");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "firstName"))), "Алексей");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "middleName"))), "Александрович");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "cardName"))), "ALEXEY BURLAKOV");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate"))), "11.07.1985");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "email"))), "burlakovAlexey@gmail.com");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), "(930) 702-18-03");

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
