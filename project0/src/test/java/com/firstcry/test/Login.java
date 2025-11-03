package com.firstcry.test;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;
import com.firstcry.base.BaseTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Login extends BaseTest {

    @Test
    public void firstCryFullFlow() {
        ExtentTest test1 = extent.createTest("TC_FC_001 - Open Website & Login");
        ExtentTest test2 = extent.createTest("TC_FC_002 - Search Product");
        ExtentTest test3 = extent.createTest("TC_FC_003 - Add to Wishlist & Cart");
        ExtentTest test4 = extent.createTest("TC_FC_004 - Open Cart & Wishlist");
        ExtentTest test5 = extent.createTest("TC_FC_005 - Remove Item from Wishlist");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // 1️⃣ Open website and login
            test1.info("Navigating to FirstCry website...");
            driver.get("https://www.firstcry.com/");
            driver.manage().window().maximize();
            test1.pass("Website opened successfully");

            test1.info("Waiting for Login/Register icon to appear...");
            WebElement loginIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class,'poplogin_main')]")));
            js.executeScript("arguments[0].click();", loginIcon);
            test1.pass("Clicked Login/Register button");

            test1.info("Locating mobile number input field...");
            WebElement mobileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lemail")));
            mobileInput.clear();
            mobileInput.sendKeys("9894652765");
            test1.pass("Entered mobile number");

            test1.info("Clicking Continue button...");
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[normalize-space()='CONTINUE']")));
            js.executeScript("arguments[0].click();", continueBtn);
            test1.pass("Clicked Continue successfully");

            test1.warning("Please enter OTP manually in the browser");
            WebDriverWait otpWait = new WebDriverWait(driver, Duration.ofSeconds(90));
            test1.info("Waiting for OTP submission...");
            otpWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='search_box']")));
            test1.pass("OTP entered and login successful");

            // 2️⃣ Search for product
            test2.info("Clearing and entering product in search box...");
            WebElement searchBox = driver.findElement(By.id("search_box"));
            searchBox.clear();
            searchBox.sendKeys("baby carrier");
            test2.info("Clicking on search button...");
            WebElement searchBtn = driver.findElement(By.xpath("//span[@class='search-button']"));
            js.executeScript("arguments[0].click();", searchBtn);
            test2.pass("Searched for 'baby carrier' successfully");

            // 3️⃣ Click first product & add to wishlist/cart
            test3.info("Selecting first product from search results...");
            WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//a[contains(@href,'babyhug-harmony')])[1]")));
            js.executeScript("arguments[0].click();", firstProduct);
            test3.pass("Opened first product successfully");

            test3.info("Switching to new product tab...");
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));
            driver.manage().window().maximize();

            test3.info("Waiting for product detail page to load...");
            wait.until(ExpectedConditions.urlContains("product-detail"));
            WebElement wishlist = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[@data-fc-ricon='y']")));
            js.executeScript("arguments[0].scrollIntoView(true);", wishlist);
            Thread.sleep(1000);
            test3.info("Clicking on wishlist icon...");
            js.executeScript("arguments[0].click();", wishlist);
            test3.pass("Added product to wishlist");

            test3.info("Scrolling to Add to Cart button...");
            js.executeScript("window.scrollBy(0, 400);");
            WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ADD TO CART')]")));
            js.executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
            Thread.sleep(1000);
            test3.info("Clicking on Add to Cart...");
            js.executeScript("arguments[0].click();", addToCartBtn);
            test3.pass("Added product to cart");

            // 4️⃣ Go to cart and wishlist
            test4.info("Opening cart icon...");
            WebElement viewCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class,'cart-icon')]")));
            js.executeScript("arguments[0].click();", viewCartBtn);
            test4.pass("Opened Cart page");

            Thread.sleep(4000);
            test4.info("Switching to Wishlist tab...");
            WebElement heartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='ShortlistTab1']/a/span[1]")));
            js.executeScript("arguments[0].scrollIntoView(true);", heartIcon);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", heartIcon);
            test4.pass("Opened Wishlist page");

            // 5️⃣ Remove from wishlist
            Thread.sleep(4000);
            test5.info("Locating remove icon in wishlist...");
            WebElement removeWishlistItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='listing']/div/div/div/div/div[3]/div/section/div[2]/p/label/img")));
            js.executeScript("arguments[0].scrollIntoView(true);", removeWishlistItem);
            Thread.sleep(1000);
            test5.info("Clicking remove icon...");
            js.executeScript("arguments[0].click();", removeWishlistItem);
            test5.pass("Removed item from Wishlist successfully");

        } catch (Exception e) {
            captureScreenshot("FirstCry_Flow_Error");
            test1.fail("Test failed: " + e.getMessage());
            test2.fail("Test failed due to previous step.");
            test3.fail("Test failed due to previous step.");
            test4.fail("Test failed due to previous step.");
            test5.fail("Test failed due to previous step.");
            e.printStackTrace();
        }
    }

    private void captureScreenshot(String name) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] src = ts.getScreenshotAs(OutputType.BYTES);
            String base64 = java.util.Base64.getEncoder().encodeToString(src);
            extent.createTest("Screenshot: " + name)
                    .log(Status.INFO, "Captured Screenshot")
                    .addScreenCaptureFromBase64String(base64, name);
        } catch (Exception e) {
            extent.createTest("Screenshot Failed").warning("⚠️ Screenshot error: " + e.getMessage());
        }
    }
}