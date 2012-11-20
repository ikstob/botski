/*
 * Copyright 2012 ikstob.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package botski.selenium;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import botski.util.SeleniumUtils;
import botski.util.Utils;

/**
 * This base-class provides the basis for more sophisticated bots 
 */
public abstract class SocialBot
    implements Runnable
{
    /** */
    public WebDriver browser;
    
    /** */
    public JavascriptExecutor javascript;
    
    /** */
    public String proxyHost = null;
    
    /** */
    public int proxyPort = -1;
    
    /** */
    public long timeout = 60000;
    
    /**
     * Initialises Firefox as the default browser
     */
    public void initializeFirefox()
    {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("webdriver.load.strategy", "fast");
        profile.setPreference("browser.tabs.loadInBackground", false);
        profile.setPreference("browser.tabs.warnOnClose", false);
        profile.setPreference("browser.tabs.warnOnOpen", false);
        if ( proxyHost != null )
        {
            profile.setPreference("network.proxy.type", 1);
            profile.setPreference("network.proxy.http", proxyHost);
            profile.setPreference("network.proxy.http_port", proxyPort);
            profile.setPreference("network.proxy.ssl", proxyHost);
            profile.setPreference("network.proxy.ssl_port", proxyPort);
        }
        browser = new FirefoxDriver(profile);
        browser.manage().timeouts().pageLoadTimeout( timeout, TimeUnit.MILLISECONDS );
        browser.manage().timeouts().setScriptTimeout( timeout, TimeUnit.MILLISECONDS );
        browser.manage().timeouts().implicitlyWait( timeout, TimeUnit.MILLISECONDS );
        javascript = (JavascriptExecutor) browser;
        browser.manage().window().setSize( new Dimension(1024,768) );
    }
    
    /**
     * Initialises Chrome as the default browser
     */
    public void initializeChrome()
    {
        SeleniumUtils.setChromeDriver();
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        if ( proxyHost != null )
        {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy( proxyHost + ":" + proxyPort );
            proxy.setHttpsProxy( proxyHost + ":" + proxyPort );
            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
        browser = new ChromeDriver(capabilities);
        javascript = (JavascriptExecutor) browser;
        browser.manage().window().setSize( new Dimension(1024,768) );
    }
    
    /**
     * @throws Exception
     */
    public void facebookLogin(String email, String password)
        throws Exception
    {
        browser.get( "http://www.facebook.com/" );
        WebElement formEmail = browser.findElement(By.name("email"));
        formEmail.sendKeys(email);
        WebElement formPassword = browser.findElement(By.name("pass"));
        formPassword.sendKeys(password);
        WebElement formRemember = browser.findElement(By.name("persistent"));
        formRemember.sendKeys(" ");
        String url = browser.getCurrentUrl();
        if ( url.contains("login.php") || url.contains("checkpoint") )
        {
            throw new Exception("Failed to login Facebook as '" + email + "' using password '" + password + "', I ended up here '" + url + "'");
        }
    }
    
    /**
     * @throws Exception 
     */
    public void twitterLogin(String email, String password)
        throws Exception
    {
        browser.get( "https://twitter.com/" );
        WebElement formEmail = browser.findElement(By.id("signin-email"));
        formEmail.sendKeys(email);
        WebElement formPassword = browser.findElement(By.id("signin-password"));
        formPassword.sendKeys(password);
        formPassword.submit();
        String url = browser.getCurrentUrl();
        if ( url.contains("/login/error") )
        {
            throw new Exception("Failed to login Twitter as '" + email + "' using password '" + password + "', I ended up here '" + url + "'");
        }
    }
    
    
    /**
     * @throws Exception 
     */
    public void pinterestLogin(String email, String password)
        throws Exception
    {
        browser.get( "https://pinterest.com/login/?next=%2F" );
        WebElement formEmail = browser.findElement(By.id("id_email"));
        formEmail.sendKeys(email);
        WebElement formPassword = browser.findElement(By.id("id_password"));
        formPassword.sendKeys(password);
        formPassword.submit();
        String url = browser.getCurrentUrl();
        if ( url.contains("/login/") )
        {
            throw new Exception("Failed to login Pinterest as '" + email + "' using password '" + password + "', I ended up here '" + url + "'");
        }
    }
    
    /**
     * Sets the proxy to use, doesn't support authentication yet
     * @param proxyHost
     * @param proxyPort
     */
    public void useProxy( String proxyHost, int proxyPort )
    {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }
    
    /**
     * Sets the default timeout for page-load and javascript execution
     * @param millis
     */
    public void setTimeout( long millis )
    {
        this.timeout = millis;
    }
    
    /**
     * Utility method
     * @param millis
     */
    public void sleep( long millis )
    {
        Utils.sleep( millis );
    }
    
}
