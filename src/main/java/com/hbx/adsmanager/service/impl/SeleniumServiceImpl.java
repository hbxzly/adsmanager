package com.hbx.adsmanager.service.impl;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.RechargeQRCode;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.RechargeQRCodeService;
import com.hbx.adsmanager.service.SeleniumService;
import com.hbx.adsmanager.util.BrowserUtil;
import org.apache.commons.io.FileUtils;
import com.hbx.adsmanager.util.StringUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class SeleniumServiceImpl implements SeleniumService {

    WebDriver driver;

    @Autowired
    AccountSystemService accountSystemService;

    @Autowired
    AccountCookieService accountCookieService;

    @Autowired
    RechargeQRCodeService rechargeQRCodeService;



    /**
     * 登录账户后台（显示页面）
     *
     * @param accountSystem
     */
    @Override
    public void loginAccountSystem(AccountSystem accountSystem) {

        try {
            //参数配置
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");

            ChromeOptions option = new ChromeOptions();
            //option.addArguments("no-sandbox");//禁用沙盒
            //option.addArguments("--headless");//无头浏览器，不打开窗口
            //通过ChromeOptions的setExperimentalOption方法，传下面两个参数来禁止掉谷歌受自动化控制的信息栏
            option.setExperimentalOption("useAutomationExtension", false);// 禁用自动填充密码
            option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));//不提示被控制



            // 创建DesiredCapabilities对象，设置性能日志选项
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, java.util.logging.Level.ALL);
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);


            driver = new ChromeDriver(option);
            driver.get("https://passport.sinoclick.com/login");
//            String html=driver.getPageSource();
            // 这里只是打印源码，后续可以根据自己的需求来解析相关的数据
//            System.out.println(html);



//            Thread.sleep(1000);
            WebDriverWait webDriverWait = new WebDriverWait(driver, 20, 1);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(BrowserUtil.loginBt)));
            WebElement account = driver.findElement(By.xpath(BrowserUtil.accountInput));
            account.sendKeys(accountSystem.getAccount());
            WebElement password = driver.findElement(By.xpath(BrowserUtil.passwordInput));
            password.sendKeys(accountSystem.getPassword());
            //Thread.sleep(1000L);
            WebElement loginButton = driver.findElement(By.xpath(BrowserUtil.loginBt));
            loginButton.click();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loginAccountSystemHeadLess(AccountSystem accountSystem) {
        try {
            // 设置 ChromeDriver 路径
            System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");

            // 配置 ChromeOptions
            ChromeOptions options = new ChromeOptions();
            options.addArguments("no-sandbox"); // 禁用沙盒
            options.addArguments("--headless"); // 无头浏览器，不打开窗口
            options.setExperimentalOption("useAutomationExtension", false); // 禁止掉谷歌受自动化控制的信息栏
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation")); // 禁止掉谷歌受自动化控制的信息栏

            // 创建 ChromeDriver 实例
            driver = new ChromeDriver(options);
            driver.get("https://passport.sinoclick.com/login");

            WebDriverWait webDriverWait = new WebDriverWait(driver, 50, 1);

            // 输入账号和密码
            WebElement account = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("account")));
            account.sendKeys(accountSystem.getAccount());

            WebElement password = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
            password.sendKeys(accountSystem.getPassword());

            // 点击登录按钮
            WebElement loginButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(BrowserUtil.loginBt)));
            loginButton.click();
        }
        catch (Exception e){
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
     *
     * @param accountSystem 账户后台
     * @param id            账户id
     * @param payMethod       付款方式
     * @param amount        充值金额
     * @param isUseWallet  是否用钱包金额
     */
    @Override
    public void adAccountRecharge(String accountSystem, String id, String payMethod, String amount, String isUseWallet) {
        String cookieStr = "";
        try {
            AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
            loginAccountSystem(system);
            driver.manage().window().maximize();
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                cookieStr = cookieStr + cookie.getName() + "=" + cookie.getValue() + ";";
            }
            accountCookieService.updateAccountCookie(system.getAccount(),cookieStr);
            WebDriverWait webDriverWait = new WebDriverWait(driver, 20, 1);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(BrowserUtil.clientPageRechargeBt)));
//            Thread.sleep(500);
            driver.get("https://business.sinoclick.com/client/facebook/accountList");
            //批量充值
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/section/section/section/main/section/section/div[2]/div[3]/button/span")))
                    .click();
            Thread.sleep(2000);
            /*//表格录入
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[6]/div/div[2]/div/div/div[2]/div[1]/div/label[2]")))
                    .click();*/
            // 使用JavaScript模拟点击
            String script = "var element = document.elementFromPoint(1470, 90); element.click();";
            ((JavascriptExecutor) driver).executeScript(script);
            //输入账号__金额
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[5]/div/div[2]/div/div/div[2]/div[2]/form/div[2]/div[1]/div[2]/div/div/textarea")))
                    .sendKeys(id+" "+amount);
            //验证账户
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[5]/div/div[2]/div/div/div[2]/div[2]/form/div[3]/div/div/div/button/span")))
                    .click();
            Thread.sleep(5000);
            //去支付
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[5]/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div/div/div/form/div[3]/div/div/div/button/span")))
                    .click();
            //是否使用钱包
            if (isUseWallet.equals("1")){
                //勾选钱包付款
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/section/section/section/main/div[2]/div/div/div/div[2]/div[2]/section/section[2]/section[1]/div/div[3]/label/span/input")))
                        .click();
            }


            //微信支付
            if (payMethod.equals("2")) {
                //微信支付按钮
                WebElement payMethodBt = driver.findElement(By.xpath(BrowserUtil.weiXinPay));
                payMethodBt.click();
                //去支付
                WebElement goPayBt = driver.findElement(By.xpath(BrowserUtil.goPayBt));
                goPayBt.click();
                //二维码
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(BrowserUtil.QRCode)));
                WebElement element = driver.findElement(By.xpath(BrowserUtil.rechargeCNY));
                String cny = StringUtil.extractNumber(element.getText());
                String currentUrl = driver.getCurrentUrl();
                Map<String, String> urlMap = StringUtil.extractValuesFromURL(currentUrl);
                WebElement QRCode = driver.findElement(By.xpath(BrowserUtil.QRCode));
                String src = QRCode.getAttribute("src");
                String base64 = StringUtil.extractImageBase64(src);
                String screenshotDirectory = "src/main/resources/static/screenshot";
                File screenshotDir = new File(screenshotDirectory);

                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }

                File destinationFile = new File(screenshotDir, id+"+"+urlMap.get("tradeTid")+".png");
                RechargeQRCode rechargeQRCode = new RechargeQRCode(urlMap.get("tradeTid"),urlMap.get("tradeOrderId"),urlMap.get("payMethod"),base64,id,accountSystem,new Date(),destinationFile.getName(),amount,cny,null);
                rechargeQRCodeService.saveRechargeQRCode(rechargeQRCode);

                // 截图操作
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    // 将截图文件保存到指定路径
                    FileUtils.copyFile(screenshotFile, destinationFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                closeChrome();
            }
            //支付宝支付
            if (payMethod.equals("1")) {

                //支付宝按钮
                WebElement payMethodBt = driver.findElement(By.xpath(BrowserUtil.alipay));
                payMethodBt.click();
                //去支付
                WebElement goPayBt = driver.findElement(By.xpath(BrowserUtil.goPayBt));
                goPayBt.click();
                //二维码
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(BrowserUtil.QRCode)));
                WebElement element = driver.findElement(By.xpath(BrowserUtil.rechargeCNY));
                String cny = StringUtil.extractNumber(element.getText());
                String currentUrl = driver.getCurrentUrl();
                Map<String, String> urlMap = StringUtil.extractValuesFromURL(currentUrl);
                WebElement QRCode = driver.findElement(By.xpath(BrowserUtil.QRCode));
                String src = QRCode.getAttribute("src");
                String base64 = StringUtil.extractImageBase64(src);

                String screenshotDirectory = "src/main/resources/static/screenshot";
                File screenshotDir = new File(screenshotDirectory);

                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }

                File destinationFile = new File(screenshotDir, id+"+"+urlMap.get("tradeTid")+".png");
                RechargeQRCode rechargeQRCode = new RechargeQRCode(urlMap.get("tradeTid"),urlMap.get("tradeOrderId"),urlMap.get("payMethod"),base64,id,accountSystem,new Date(),destinationFile.getName(),amount,cny,null);
                rechargeQRCodeService.saveRechargeQRCode(rechargeQRCode);

                // 截图操作
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    // 将截图文件保存到指定路径
                    FileUtils.copyFile(screenshotFile, destinationFile);
                    System.out.println("截图已保存: " + destinationFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存充值截图
     */
    public void adAccountRechargeScreenshot(String accountSystem, String id, String payMethod, String amount){
        String cookieStr = "";
        try {
            AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
            loginAccountSystem(system);
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                cookieStr = cookieStr + cookie.getName() + "=" + cookie.getValue() + ";";
            }
            accountCookieService.updateAccountCookie(system.getAccount(),cookieStr);
            WebDriverWait webDriverWait = new WebDriverWait(driver, 20, 1);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(BrowserUtil.clientPageRechargeBt)));
//            Thread.sleep(500);
            driver.get("https://business.sinoclick.com/client/myorder/recharge/ad-account?account=" + id + "&channel=1&user=" + system.getUserId() + "");
            By accountInput = By.xpath(BrowserUtil.adAccountSelectInput);
            webDriverWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementValue(accountInput, "")));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(BrowserUtil.goRechargeBt)));
            WebElement rechargeButton = driver.findElement(By.xpath(BrowserUtil.goRechargeBt));
            WebElement rechargeAmount = driver.findElement(By.xpath(BrowserUtil.rechargeAmountInput));
            rechargeAmount.sendKeys(amount);
            rechargeButton.click();
            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(BrowserUtil.goPayBt)));
            if (payMethod.equals("2")) {
                WebElement payMethodBt = driver.findElement(By.xpath(BrowserUtil.weiXinPay));
                payMethodBt.click();
                WebElement goPayBt = driver.findElement(By.xpath(BrowserUtil.goPayBt));
                goPayBt.click();
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(BrowserUtil.QRCode)));
                WebElement element = driver.findElement(By.xpath(BrowserUtil.rechargeCNY));
                String cny = StringUtil.extractNumber(element.getText());
                String currentUrl = driver.getCurrentUrl();
                Map<String, String> urlMap = StringUtil.extractValuesFromURL(currentUrl);
                WebElement QRCode = driver.findElement(By.xpath(BrowserUtil.QRCode));
                String src = QRCode.getAttribute("src");
                String base64 = StringUtil.extractImageBase64(src);
                String screenshotDirectory = "src/main/resources/static/screenshot";
                File screenshotDir = new File(screenshotDirectory);

                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }

                File destinationFile = new File(screenshotDir, urlMap.get("tradeTid")+".png");
                RechargeQRCode rechargeQRCode = new RechargeQRCode(urlMap.get("tradeTid"),urlMap.get("tradeOrderId"),urlMap.get("payMethod"),base64,id,accountSystem,new Date(),destinationFile.getName(),amount,cny,null);
                rechargeQRCodeService.saveRechargeQRCode(rechargeQRCode);

                // 截图操作
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    // 将截图文件保存到指定路径
                    FileUtils.copyFile(screenshotFile, destinationFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                closeChrome();
            }
            if (payMethod.equals("1")) {
                WebElement payMethodBt = driver.findElement(By.xpath(BrowserUtil.alipay));
                payMethodBt.click();
                WebElement goPayBt = driver.findElement(By.xpath(BrowserUtil.goPayBt));
                goPayBt.click();
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(BrowserUtil.QRCode)));
                WebElement element = driver.findElement(By.xpath(BrowserUtil.rechargeCNY));
                String cny = StringUtil.extractNumber(element.getText());
                String currentUrl = driver.getCurrentUrl();
                Map<String, String> urlMap = StringUtil.extractValuesFromURL(currentUrl);
                WebElement QRCode = driver.findElement(By.xpath(BrowserUtil.QRCode));
                String src = QRCode.getAttribute("src");
                String base64 = StringUtil.extractImageBase64(src);

                String screenshotDirectory = "src/main/resources/static/screenshot";
                File screenshotDir = new File(screenshotDirectory);

                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }

                File destinationFile = new File(screenshotDir, urlMap.get("tradeTid")+".png");
                RechargeQRCode rechargeQRCode = new RechargeQRCode(urlMap.get("tradeTid"),urlMap.get("tradeOrderId"),urlMap.get("payMethod"),base64,id,accountSystem,new Date(),destinationFile.getName(),amount,cny,null);
                rechargeQRCodeService.saveRechargeQRCode(rechargeQRCode);

                // 截图操作
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    // 将截图文件保存到指定路径
                    FileUtils.copyFile(screenshotFile, destinationFile);
                    System.out.println("截图已保存: " + destinationFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                closeChrome();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取账户cookie（不显示页面）
     *
     * @param accountSystem
     * @return
     */
    @Override
    public String getAccountSystemCookie(AccountSystem accountSystem) {

        String cookieStr = "";

        try {
            loginAccountSystemHeadLess(accountSystem);

            // 等待页面加载完成
//            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(BrowserUtil.clientPageRechargeBt)));
            Thread.sleep(2000);
            // 获取登录后的 Cookie
            Set<Cookie> cookies = driver.manage().getCookies();

            for (Cookie cookie : cookies) {
                cookieStr = cookieStr + cookie.getName() + "=" + cookie.getValue() + ";";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }


        return cookieStr;

    }


}
