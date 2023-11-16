package sdf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main{

    //extracting relevant columns
    public static final int APP_NAME = 0;
    public static final int CATEGORY = 1;
    public static final int RATING = 2;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length <= 0){
            System.out.println("Error, missing filename. Please input filename.");
        }

        System.out.printf("System is processing file: %s",args[0]);

        try (FileReader fr = new FileReader(args[0])){
            
            BufferedReader br = new BufferedReader(fr);

            String strLine;
            ArrayList<String> data= new ArrayList<String>();

            // add rows into data list
            while ((strLine = br.readLine()) != null) {
                data.add(strLine);
            }

            // count total lines read
            int linesRead = data.size() - 1; // -1 to remove header row

            // map category to list of applications
            Map<String, List<Applications>> sort = data.stream()
                // skip columns
                .skip(1)
                // cleaning - remove spaces, convert to lower case
                .map(l -> l.trim().toLowerCase())
                .filter (l -> l.length() > 0)
                // split by comma
                .map(l -> l.split(","))
                // filter out the invalid ratings
                .filter (l -> l[RATING].matches("[0-9]+"))
                .map(l -> new Applications(l[APP_NAME], l[CATEGORY], l[RATING]))
                .collect(Collectors.groupingBy(i -> i.getCategory()));
            
            // br.lines()
            //     .skip(1)
            //     // cleaning - remove spaces, convert to lower case
            //     .map(l -> l.trim().toLowerCase())
            //     .filter (l -> l.length() > 0)
            //     .map(l -> l.split(","))
            //     .toList();
            
            // print out app names in each category
            for (Map.Entry<String, List<Applications>> entry : sort.entrySet()) {
                System.out.printf("\nCATEGORY: %s\n", entry.getKey().toUpperCase());
                for (Applications app: entry.getValue()){
                    System.out.printf("   %s\n", app.getName());
                }

            

            // System.out.println("count:" + lines);
                
            }
        }
    }
}