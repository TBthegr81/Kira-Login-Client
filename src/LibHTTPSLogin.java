/*
 * LibHTTPSLogin
 * @Authur: Erik Welander, William Alexander
 * @Version: 2012-12-11
 * @JRE: JDK 1.6.0_32 X64
 * @Encoding: UTF-8
 * @IDE: Netbeans 7.2.1
 * 
 * Description: This is a simple java HTTPS login library based upon rewritten and modified
 * code from William Alexander @ http://www.java-samples.com.
 *
 * I made this library as an extension to my program "Kira Login Client".
 * A client that handles internet login and access to everyone inside the
 * "PROXXI" domain.
 **/
import java.io.*;
import java.net.*;
import java.security.Security.*;
import com.sun.net.ssl.*;
public class LibHTTPSLogin{
    
    public static String[] performHttpsPOST(String s_url, String encoding,
                                          String username_php_var, String username,
                                          String password_php_var, String password){
        
    String query = "";  //This will be the final query to be sent
    //This string will be returned so that we can parse results.
    //ID 0 represents the result code, and ID 1 the html response
    String[] result = new String[2];
    try{
        //The following section modifies how the URL and HttpsURLConnection should behave.
        //This code is somewhat legacy from the java 1.4 API, but works quite exellent
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        URL url = new URL(s_url); 
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        //These needs to be declared, since they are diabled by default.
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        //By default we will allow automatic URL transers by sending new headers.
        conn.setFollowRedirects(true);
        
        query = "&" + username_php_var + "=" + URLEncoder.encode(username,encoding)
              + "&" + password_php_var + "=" + URLEncoder.encode(password,encoding);
        
        conn.setRequestProperty("Content-length",String.valueOf (query.length())); 
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)"); 
        
        //Creates an outputstream to send our request 
        DataOutputStream da_output = new DataOutputStream(conn.getOutputStream());
        //Send our request to the web server
        da_output.writeBytes(query);
        //Close our dataoutputstream
        da_output.close();  
        
        //Stores the web server response code
        result[0] = ""+conn.getResponseMessage();
        
        //Create a data input stream in order to read the result
        DataInputStream da_input = new DataInputStream(conn.getInputStream());
        //Read and store each encoded character until end-of-stream(-1) is detected 
        for(int c = da_input.read(); c != -1; c = da_input.read()) {
            result[1] += (char)c;
        }
        //All the data has been read, close the input stream
        da_input.close(); 
        
    }catch(Exception ex){
        System.err.println("ERROR: performHttpsPOST() : "+ex);
    }
    
  return result;
  } //End of performHttpsPOST()
}   //End of LibHTTPSLogin