package edu.neu.madcourse.dhvanisheth;

import java.io.InputStream;

//public class SearchFile {
//
//    private static SearchFile instance = null;
//
//    private static String FileData = null;
//
//    public static SearchFile getInstance(InputStream is) {
//        if (instance == null) {
//            instance = new SearchFile();
//            init(is);
//        }
//        return instance;
//    }
//
//
//    private SearchFile() {
//    }
//
//    public static void init(InputStream is) {
//        try {
//
//            byte[] dt = new byte[is.available()];
//            int read = is.read(dt);
//            is.close();
//            FileData = "\n" + new String(dt, "UTF-8");
//
//        } catch (Exception x) {
//            FileData = null;
//            System.out.println("Error: " + x);
//        }
//    }
//
//
//    public static boolean search(String search) {
//        if (FileData == null) return false;
//        return FileData.replace("\r", "").contains("\n" + search + "\n");
//
//    }
//
//
//}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFile {
    // Change the extension of the smaller files if you want..
    private static final String EXTENSION = ".txt";

    // An instance to hold the Singleton Class..
    private static SearchFile instance = null;


    // Added a cache to make it work a bit faster.. ;)
    private Map<String, List<String>> cache;
    private List<String> searchedFiles;

    /**
     * Returns the Instance to this singleton class.
     * @return Instance of SearchFile
     */
    public static SearchFile getInstance() {
        if (instance == null) {
            instance = new SearchFile();
        }

        return instance;
    }



    private SearchFile() {
        // Create the cache object.
        cache = new HashMap<String, List<String>>();
        searchedFiles = new ArrayList<String>();
    }

    /**
     * Search has changed slightly..
     * it now takes the keyword, opens the file to read
     *  and from the data from the file, if a match is found,
     *  returns true, if no match is found till the end of all the words,
     *  returns false.
     *
     *  I have implemented caching. If a search request is made for the
     *  same word again, this time it won't open the file to read it and
     *  returns true from the cache (if it's there in the cache)
     * @param search
     * @return
     */
    public boolean search(final String search, final InputStream is) {
        String key = search.substring(0, 2);
        BufferedReader reader = null;

        // Check in cache
        if(cache.containsKey(key)) {
            // If this file was queried before and if the word
            // is found in the cache, return true!
            if(cache.get(key).contains(search))
                return true;

            // If this file was searched before and if the file was completely searched
            // and the word does not exist in the cache, then this word definitely doesn't exist
            // in the file as well, so save the trouble of reading hte file and return false.
            if(searchedFiles.contains(key))
                return false;
        }

        // Either this file was not fully read or not read at all, so read the file and add
        // words that we read to cache.
        try {

            String line = null;
            reader = new BufferedReader(new InputStreamReader(is));

            while((line = reader.readLine()) != null) {
                if(line.equals("") || line.length() <= 2)
                    continue;

                // Add to the cache so next time, we don't read the file..
                addToCache(key, line);

                // Now compare this line to the input search word.
                if(line.replace("\r", "").trim().equalsIgnoreCase(search)) {
                    return true;
                }
            }

            // Post searching through the entire file, add its name to the list.
            searchedFiles.add(key);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        // If we didn't find a match till now, return false.
        return false;
    }

    /**
     * Add the key and the word to teh cache.
     *
     * @param key - The key which is the file name of the split file we searched so far..
     * @param word - The word we are looking for..
     */
    private void addToCache(final String key, final String word) {
        if(cache.containsKey(key)) {
            cache.get(key).add(word);
        } else {
            List<String> l = new ArrayList<String>();
            l.add(word);
            cache.put(key, l);
        }
    }


}

