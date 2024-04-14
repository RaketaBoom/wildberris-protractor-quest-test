package org.example.com;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.List;

public class WildberrisSearchTest {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

            driver.get("https://www.wildberries.ru/");
            Thread.sleep(2000);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.findElement(By.id("searchInput")).sendKeys("транспортир", Keys.ENTER);
            Thread.sleep(1000);

            driver.findElement(By.cssSelector(".dropdown-filter__btn--sorter")).click();
            driver.findElement(By.xpath("//span[@class='radio-with-text__text' and text()='По возрастанию цены']")).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-card-list")));
            List<WebElement> products = driver.findElements(By.cssSelector(".product-card.product-card--hoverable.j-card-item"));

            for (int i = 0; i < 10; i++) {
                WebElement product = products.get(i);
                String name = product.findElement(By.cssSelector("[aria-label]"))
                        .getAttribute("aria-label");
                String price = product.findElement(By.cssSelector("ins.price__lower-price")).getText();
                String line = (i + 1) + ". " + name + " - Цена: " + price;
                writer.write(line);
                writer.newLine();
                System.out.println(line);
            }
            writer.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }
}
