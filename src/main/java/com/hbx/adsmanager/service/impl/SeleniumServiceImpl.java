package com.hbx.adsmanager.service.impl;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.AdAccountService;
import com.hbx.adsmanager.service.SeleniumService;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class SeleniumServiceImpl implements SeleniumService {

    WebDriver driver;

    @Autowired
    AccountSystemService accountSystemService;

    @Autowired
    AccountCookieService accountCookieService;

    /**
     * 登录账户后台（显示页面）
     * @param accountSystem
     */
    @Override
    public void loginAccountSystem(AccountSystem accountSystem) {

        try{
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");

            ChromeOptions option = new ChromeOptions();
            //option.addArguments("no-sandbox");//禁用沙盒
            //option.addArguments("--headless");//无头浏览器，不打开窗口
            //通过ChromeOptions的setExperimentalOption方法，传下面两个参数来禁止掉谷歌受自动化控制的信息栏
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            driver = new ChromeDriver(option);
            driver.get("https://passport.sinoclick.com/login");
            /*String html=driver.getPageSource();
            // 这里只是打印源码，后续可以根据自己的需求来解析相关的数据
            System.out.println(html);*/

            //Thread.sleep(1000);
            WebDriverWait webDriverWait = new WebDriverWait(driver, 20,1);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/lang/div/div/div/div/div[2]/div[1]/div[2]/form/div[3]/div/div/div/button")));
            WebElement account = driver.findElement(By.id("account"));
            account.sendKeys(accountSystem.getAccount());
            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys(accountSystem.getPassword());
            //Thread.sleep(1000L);
            WebElement loginButton = driver.findElement(By.xpath("/html/body/lang/div/div/div/div/div[2]/div[1]/div[2]/form/div[3]/div/div/div/button"));
            loginButton.click();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void closeChrome() {
        driver.close();
        driver.quit();
    }

    /**
     * 账户充值
     * @param accountSystem
     * @param id
     * @param amount
     */
    @Override
    public void adAccountRecharge(String accountSystem, String id,String payMethod, String amount){
        try {
            AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
            loginAccountSystem(system);
            driver.manage().window().maximize();
            WebDriverWait webDriverWait = new WebDriverWait(driver, 20,1);
            webDriverWait. until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/section/section/section/main/div/div/div[3]/div/div[2]/div/div[1]/div[2]/button[2]")));
//            Thread.sleep(500);
            driver.get("https://business.sinoclick.com/client/myorder/recharge/ad-account?account="+id+"&channel=1&user="+system.getUserId()+"");
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div[2]/div[2]/button")));
            WebElement rechargeButton = driver.findElement(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div[2]/div[2]/button"));
            WebElement rechargeAmount = driver.findElement(By.id("recharge_amount"));
            rechargeAmount.sendKeys(amount);
            rechargeButton.click();
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div/div/div/ul/li/span/button")));
            if (payMethod.equals("2")){
                WebElement payMethodBt = driver.findElement(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div/div/div/div[2]/div[3]/div/div/div[1]/label[2]/span[2]/img"));
                payMethodBt.click();
                WebElement goPaymentBt = driver.findElement(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div/div/div/ul/li/span/button"));
                goPaymentBt.click();
            }
            if(payMethod.equals("1")){
                WebElement payMethodBt = driver.findElement(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div/div/div/div[2]/div[3]/div/div/div[1]/label[3]/span[2]/img"));
                payMethodBt.click();
                WebElement goPaymentBt = driver.findElement(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div/div/div/ul/li/span/button"));
                goPaymentBt.click();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取账户cookie（不显示页面）
     * @param accountSystem
     * @return
     */
    @Override
    public String getAccountSystemCookie(AccountSystem accountSystem){

        String cookieStr ="";
        try{
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");

            ChromeOptions option = new ChromeOptions();
            option.addArguments("no-sandbox");//禁用沙盒
            option.addArguments("--headless");//无头浏览器，不打开窗口
            //通过ChromeOptions的setExperimentalOption方法，传下面两个参数来禁止掉谷歌受自动化控制的信息栏
            option.setExperimentalOption("useAutomationExtension", false);
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            driver = new ChromeDriver(option);
            driver.get("https://passport.sinoclick.com/login");
            /* String html=driver.getPageSource();
            // 这里只是打印源码，后续可以根据自己的需求来解析相关的数据
                System.out.println(html);*/
            //Thread.sleep(1000);
            WebDriverWait webDriverWait = new WebDriverWait(driver, 20,1);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/lang/div[@id='root']/div[@class='App ']/div/div[@class='passport-wrapper login-wrapper']/div[@class='login-wrapper-right']/div[@class='login-component ']/div[@class='login-box']/form[@class='m-design-form m-design-form-horizontal m-design-form-large login-form']/div[@class='m-design-row m-design-form-item']/div[@class='m-design-col m-design-form-item-control']/div[@class='m-design-form-item-control-input']/div[@class='m-design-form-item-control-input-content']/button[@class='m-design-btn m-design-btn-primary m-design-btn-lg m-design-btn-block']")));
            WebElement account = driver.findElement(By.id("account"));
            account.sendKeys(accountSystem.getAccount());
            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys(accountSystem.getPassword());
            //Thread.sleep(1000L);
            WebElement loginButton = driver.findElement(By.xpath("/html/body/lang/div[@id='root']/div[@class='App ']/div/div[@class='passport-wrapper login-wrapper']/div[@class='login-wrapper-right']/div[@class='login-component ']/div[@class='login-box']/form[@class='m-design-form m-design-form-horizontal m-design-form-large login-form']/div[@class='m-design-row m-design-form-item']/div[@class='m-design-col m-design-form-item-control']/div[@class='m-design-form-item-control-input']/div[@class='m-design-form-item-control-input-content']/button[@class='m-design-btn m-design-btn-primary m-design-btn-lg m-design-btn-block']"));
            loginButton.click();
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/section/section/section/main/div/div/div[2]/div[2]/div[1]/div[1]")));
            Set<Cookie> cookies=driver.manage().getCookies();
            driver.close();
            driver.quit();
            for (Cookie cookie : cookies) {
                cookieStr =cookieStr+cookie.getName()+"="+cookie.getValue()+";";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
        return cookieStr;
    }

}
