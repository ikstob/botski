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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class ListUtils
{
    /** Defaults to Mac/UNIX new lines */
    public static String lineSeparator = System.getProperty("line.separator","\n");
    
    /**
     * Read a file into a list of strings, one for each line
     * @param file
     * @return
     * @throws IOException 
     */
    public static List<String> readList(File file)
        throws IOException
    {
        List<String> list = new ArrayList<String>();
        if ( file.exists() )
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null )
            {
                list.add( line.trim() );
            }
            br.close();
        }
        return list;
    }
    
    /**
     * Attempt to read file into a list of strings, return an empty list if fails
     * @param file
     * @return
     */
    public static List<String> readListQuietly(File file)
    {
        try
        {
            return readList(file);
        }
        catch ( Exception ignore ) { }
        return new ArrayList<String>();
    }
    
    /**
     * Writes the list to file!
     * @param file
     * @param list
     * @throws IOException 
     */
    public static void writeList(File file, List<String> list)
        throws IOException
    {
        FileWriter fw = new FileWriter(file);
        for ( String line : list )
        {
            fw.write( line + lineSeparator );
        }
        fw.close();
    }
    
    /**
     * Writes the list to file, ignoring any exceptions along the way!
     * @param file
     * @param list
     */
    public static void writeListQuietly(File file, List<String> list)
    {
        try
        {
            writeList(file, list);
        }
        catch ( Exception ignore ) { }
    }

}
