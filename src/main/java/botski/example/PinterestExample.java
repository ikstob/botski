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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import botski.selenium.SocialBot;

/**
 * Simple example of automating Pinterest
 */
public class PinterestExample
    extends SocialBot
{
    public void run()
    {
        try
        {
            // Set your Proxy here
            useProxy("123.123.123.123", 8080);
            
            // Default to Firefox, but could use Chrome
            initializeFirefox();
            
            // Login and goto the homepage
            pinterestLogin("email@address.com", "password");
            
            // Search the DOM to find all visible Like buttons
            List<WebElement> list = browser.findElements( By.className("likebutton") );
            System.out.println("Found " + list.size() + " 'Like' buttons!");
            
            // Randomly 'Like' ~ 10% of them
            int count = 0;
            for ( WebElement element : list )
            {
                if ( Math.random() <= 0.10 ) // roughly 10%
                {
                    // Click the 'Like' button using Javascript
                    javascript.executeScript("arguments[0].click();", element);
                    System.out.println("   ... Liked pin " + element.getAttribute("data-id"));
                    count++;
                }
            }
            System.out.println("Liked " + count + " pins!");
            
            // Sleep for 5 seconds then quit
            sleep(5000);
            browser.quit();
        }
        catch ( Exception e )
        {
            System.err.println("Something has gone horribly wrong!");
            e.printStackTrace();
        }
    }

    /**
     * This just starts the program and calls the run() function
     */
    public static void main( String[] args )
    {
        (new PinterestExample()).run();
    }
}
