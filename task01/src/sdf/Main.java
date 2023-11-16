package sdf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main{

    //extracting relevant columns
    public static final int APP_NAME = 0;
    public static final int CATEGORY = 1;
    public static final int RATING = 2;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length <= 0){
            System.out.println("Error, missing filename. Please input filename.");
        }

        System.out.printf("Processing file: %s\n",args[0]);

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
                .filter (l -> l[RATING].matches("[0-9\\.*]+"))
                .map(l -> new Applications(l[APP_NAME], l[CATEGORY], l[RATING]))
                .collect(Collectors.groupingBy(i -> i.getCategory()));

            // count processed data
            Map<Object, Long> processedCount = data.stream()
                // skip columns
                .skip(1)
                // cleaning - remove spaces, convert to lower case
                .map(l -> l.trim().toUpperCase())
                .filter (l -> l.length() > 0)
                // split by comma
                .map(l -> l.split(","))
                // filter out the invalid ratings
                .filter (l -> l[RATING].matches("[0-9\\.*]+")) // [0-9]+\\.?[0-9]+
                .map(l -> new Applications(l[APP_NAME], l[CATEGORY], l[RATING]))
                .collect(Collectors.groupingBy(i -> i.getCategory(), Collectors.counting()));

            // count discarded count
            Map<Object, Long> discardedCount = data.stream()
                // skip columns
                .skip(1)
                // cleaning - remove spaces, convert to lower case
                .map(l -> l.trim().toUpperCase())
                .filter (l -> l.length() > 0)
                // split by comma
                .map(l -> l.split(","))
                // filter out the invalid ratings
                .filter (l -> !l[RATING].matches("[0-9\\.*]+")) // [0-9]+\\.?[0-9]+
                .map(l -> new Applications(l[APP_NAME], l[CATEGORY], l[RATING]))
                .collect(Collectors.groupingBy(i -> i.getCategory(), Collectors.counting()));  
            
            // print out app names in each category
            for (Map.Entry<String, List<Applications>> entry : sort.entrySet()) {
                System.out.printf("\nCategory: %s\n", entry.getKey().toUpperCase());
                // map application name to ratings
                Map<Double, String> ratingApplications = new HashMap<>();
                // list of ratings
                List<Double> ratingList = new LinkedList<>();
                
                // loop through the applications within each list
                for (Applications app: entry.getValue()){
                    //System.out.printf("   %s, rating: %s\n", app.getName(), app.getRating());
                    Double ratings = Double.parseDouble(app.getRating());
                    ratingList.add(ratings);
                    ratingApplications.put(ratings, app.getName());
                }
                // sort rating list for each category
                Collections.sort(ratingList);
                Double highest = ratingList.get(ratingList.size()-1);
                Double lowest = ratingList.get(0);
                
                System.out.printf("Highest: %s, %.1f\n",ratingApplications.get(highest), highest);
                System.out.printf("Lowest: %s, %.1f\n",ratingApplications.get(lowest), lowest);
                
                // calculate average ratings
                Double totalRating = 0d;
                for (int i = 0; i < ratingList.size(); i++){
                    totalRating += ratingList.get(i);
                }
                Double averageRating = totalRating/ratingList.size();
                System.out.printf("Average: %f\n",averageRating);

                // count
                System.out.printf("Count: %o\n", processedCount.get(entry.getKey().toUpperCase()));
                System.out.printf("Discarded: %o\n",discardedCount.get(entry.getKey().toUpperCase()));
            }

            System.out.printf("\nTotal lines in file: %d\n",linesRead);
        }
    }
}