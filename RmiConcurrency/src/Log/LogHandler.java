/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author a1729756
 */
public class LogHandler {
    
    String fileName;

    public LogHandler(String fileName) {
        this.fileName = fileName;
    }
    
    public void write(String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.append(' ');
        writer.append(str);

        writer.close();
    }
    
    public String read(int idTransaction) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        String id = Integer.toString(idTransaction);
        while ((line = br.readLine()) != null) {
           if (line.split(",")[0] == id)
               return line;
        }
          
        return null;
    }    
}
