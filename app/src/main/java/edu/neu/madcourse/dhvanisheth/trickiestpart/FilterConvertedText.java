package edu.neu.madcourse.dhvanisheth.trickiestpart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhvani on 4/12/2016.
 */
public class FilterConvertedText {

    public static List<String> getFileData(InputStream is) throws Exception{
        List<String> ret = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;

        while((line = reader.readLine()) != null) {
            if(line.trim().equals(""))
                continue;
            ret.add(line.toLowerCase());
        }
        reader.close();
        return ret;
    }

}
