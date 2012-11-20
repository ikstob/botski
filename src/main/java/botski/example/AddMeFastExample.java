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

package botski.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import botski.util.Utils;

/**
 * 
 */
public class AddMeFastExample
    implements Runnable
{
    // Selenium stuff 
    WebDriver driver;
    JavascriptExecutor jse;
    
    // Config
    String facebookEmail = "you@domain.com";
    String facebookPassword = "password";
    String addmefastEmail = "you@domain.com";
    String addmefastPassword = "password";
    
    // Perform the Facebook login or throw an Exception
    public void facebookLogin()
        throws Exception
    {
        driver.get( "http://www.facebook.com/" );
        WebElement formEmail = driver.findElement(By.name("email"));
        formEmail.sendKeys(facebookEmail);
        WebElement formPassword = driver.findElement(By.name("pass"));
        formPassword.sendKeys(facebookPassword);
        WebElement formRemember = driver.findElement(By.name("persistent"));
        formRemember.sendKeys(" ");
        formPassword.submit();
        if ( driver.getCurrentUrl().contains("login.php") )
        {
            throw new Exception("Failed to login Facebook as '" + facebookEmail + "' using password '" + facebookPassword + "', I ended up here '" + driver.getCurrentUrl() + "'");
        }
    }
    
    // Perform the AddMeFast login or throw an Exception
    public void addMeFastLogin()
        throws Exception
    {
        driver.get("http://addmefast.com");
        WebElement formEmail = driver.findElement(By.name("email"));
        formEmail.sendKeys(addmefastEmail);
        WebElement formPassword = driver.findElement(By.name("password"));
        formPassword.sendKeys(addmefastPassword);
        WebElement formRemember = driver.findElement(By.name("remember"));
        formRemember.sendKeys(" ");
        WebElement formSubmit = driver.findElement(By.name("login_button"));
        formSubmit.click();
        if ( "http://addmefast.com/free_points.html".equals(driver.getCurrentUrl()) == false )
        {
            throw new Exception( "Failed to login to addmefast.com as '" + addmefastEmail + "' using password '" + addmefastPassword + "', I ended up here '" + driver.getCurrentUrl() + "'" );
        }
    }
    
    // This is where the magic happens
    public void run()
    {
        try
        {
            int likes = 0;
            int points = 0;
            long start_time = System.currentTimeMillis();
            
            // Setup Selenium
            driver = new FirefoxDriver();
            jse = (JavascriptExecutor) driver;
            
            // Perform the Login actions
            facebookLogin();
            addMeFastLogin();
            
            // Goto the Facebook Likes page
            driver.get( "http://addmefast.com/free_points/facebook_likes.html" );
            if ( "http://addmefast.com/free_points/facebook_likes.html".equals(driver.getCurrentUrl()) == false )
            {
                System.err.println( "I was trying to navigate to 'http://addmefast.com/free_points/facebook_likes.html' and ended up here '" + driver.getCurrentUrl() + "'" );
                return;
            }
            
            // Remember the main window handle
            String windowHandle = (String) driver.getWindowHandles().toArray()[0];

            // Go into the loop
            while ( true )
            {
                // Reacts to the appearance of a Like button
                (new WebDriverWait(driver, 60)).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        return (Boolean) jse.executeScript( "if(jQuery('.single_like_button').length){return true;}else{return false;}" );
                    }
                });
                
                // Click the like button by injecting JavaScript to the page
                jse.executeScript( "jQuery('.single_like_button').click();");
                
                // Allow time for the popup window to appear
                Utils.sleep(1000);
                
                // Switch to the new window
                Set<String> winSet = driver.getWindowHandles();
                List<String> winList = new ArrayList<String>(winSet);
                String newTab = winList.get(winList.size() - 1);
                driver.switchTo().window(newTab); // switch to new tab
                
                // Click the first "Like" button on the page
                jse.executeScript( "var inputs=document.getElementsByTagName('input');for(var i=0; i<inputs.length; i++){var input=inputs[i];var value=input.getAttribute('value');if(value!=null){if(value=='Like'){input.click();break;}}}" );
                
                // Allow time for the Like action to go through
                Utils.sleep(1000);
                
                // Close this window and switch back to the main one
                driver.close();
                driver.switchTo().window(windowHandle);
                
                // This delay allows AddMeFast to detect the window close and request the next page to Like
                Utils.sleep(5000);
                
                // Update counters
                likes++;
                WebElement points_count = driver.findElement(By.className("points_count"));
                try { points = Integer.parseInt( points_count.getText() ); } catch ( Exception ignore ) { }
                long diff = (System.currentTimeMillis() - start_time)/1000;
                System.out.println( "" + likes + " likes, " + points + " points in " + diff + " seconds" );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param args
     */
    public static void main( String[] args )
    {
        (new AddMeFastExample()).run();
    }
}