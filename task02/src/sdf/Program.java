package sdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Program {

    public static final int DEFAULT_PORT = 3000;
    public static final String DEFAULT_NAME = "localhost";
    private static Map<String, Item> productList = new HashMap<>();
    private static List<Item> itemList = new ArrayList<>();
    private static List<String> selectedItems = new LinkedList<>();

    public static void main(String[] args) throws Exception {

        Socket socket;
        // no parameter given
        if (args.length == 0){
            System.out.printf("Attempting to connect to 127.0.0.1 on port %d\n", DEFAULT_PORT);
            socket = new Socket (DEFAULT_NAME, DEFAULT_PORT);
            System.out.println("Connected to server");
        }
        // one parameter
        else if (args.length == 1){
            int port = Integer.parseInt(args[0]);
            System.out.printf("Attempting to connect to 127.0.0.1 on port %d\n", port);
            socket = new Socket (DEFAULT_NAME, port);
            System.out.println("Connected to server");
        }
        // two parameter
        else {
            int port = Integer.parseInt(args[1]);
            System.out.printf("Attempting to %s on port %d\n",args[0], port);
            socket = new Socket (args[0], port);
            socket.getInetAddress();
            System.out.println("Connected to server");
        }

        try (InputStream is = socket.getInputStream(); OutputStream os = socket.getOutputStream();){
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            
            String line;
            double budget = 0;
            double spend = 0;
            String items ="";
            String request = "";
            Item item = null;
            
            while (null != (line = br.readLine())){
                System.out.println(line);

                String[] array = line.trim().split(":");
                for (int i = 0; i < array.length; i++){
                    if (array[i].length() > 0){
                        array[i] = array[i].trim();
                    }
                    // System.out.println(">>" + array[i]);
                }
                if (!(line.length() > 0)){
                    continue;
                }
                switch (array[0]) {
                    case "request_id":
                        request = array[1];
                        break;

                    case "budget":
                        budget = Double.parseDouble(array[1]);
                        break;
                        
                    case "prod_id":
                        item = new Item(array[1]);
                        itemList.add(item);
                        break;

                    case "title":
                        item.setProductTitle(array[1]);
                        break;

                    case "price":
                        item.setProductPrice(Double.parseDouble(array[1]));
                        break;

                    case "rating":
                        item.setProductRating(Double.parseDouble(array[1]));
                        break;
                }

                Comparator<Item> comparator = Comparator.comparing(s -> s.getProductRating());

                //second comparison
                comparator = comparator.thenComparing(Comparator.comparing(s-> s.getProductPrice()));

                //sorting using stream
                List<Item> sorted = new ArrayList<>();
                sorted = itemList.stream().sorted(comparator).collect(Collectors.toList());

                for (Item i:itemList){
                    if (i.getProductPrice() > budget){
                        continue; //end curr iteration
                    }
                    else {
                        selectedItems.add(i.getProductID());
                        budget -= i.getProductPrice();
                        spend += i.getProductPrice();
                    }
                }

                for (String str:selectedItems){
                    items.concat(str+",");
                }

                bw.write("request_id: " + request + "\n");
                bw.write("name: Ong Si Ying Alicia\n");
                bw.write("email: aliciaongsiying@gmail.com\n");
                bw.write("items: " + items +"\n");
                bw.write("spent: " + String.valueOf(spend) + "\n");
                bw.write("remaining: " + String.valueOf(budget) + "\n");
                bw.write("client_end\n");
                

            }
            bw.flush();
        }

    }
}
