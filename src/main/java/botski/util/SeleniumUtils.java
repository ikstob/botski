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

package botski.util;

/**
 * Misc utilities to ease the pain of working with Selenium
 */
public class SeleniumUtils
{
    /**
     * Sets the appropriate Chrome driver:
     *   Linux x64 - ./drivers/chromedriver-linux-x86_64-23.0.1240.0
     *   Mac OS X - ./drivers/chromedriver-mac_os_x-x86_64-23.0.1240.0
     *   Windows - ./drivers/chromedriver-windows-x86-23.0.1240.0
     * Download from here:
     *   http://code.google.com/p/chromedriver/downloads/list
     */
    public static void setChromeDriver()
    {
        String os_arch = System.getProperty("os.arch","").replace("amd64", "x86_64");
        String os_name = System.getProperty("os.name","").toLowerCase().replace(" ", "_");          
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-" + os_name + "-" + os_arch + "-23.0.1240.0");
    }
}
