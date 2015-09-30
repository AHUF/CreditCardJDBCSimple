/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbconsoleself;

import ccdblib.*;
import java.io.*;
import java.util.*;

public class CCClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.util.Date x = new java.util.Date("12/12/2010");
        PrintStream cout = System.out;
        Scanner cin = new Scanner(System.in);
        
        cout.println("Please enter your password");
        crud db = null;
        int count = 0;
        String pass = cin.nextLine();
        db = new crud("CreditCardAccounts", "admin1", pass);
        while(count < 3){
            if(!db.isConnected()){
                cout.println("incorrect password. please enter your password");
                count++;
            }
            else{
                break;
            }
            pass = cin.nextLine();
            db.setConnection("CreditCardAccounts", "admin1", pass);
        }
        if(count == 3){
            cout.println("Your account has been locked. Please contact your DBA");
            System.exit(-1);
        }
        
        List<String> aclist = db.Read("SELECT AccountNo, Balance, Limit, expirationDate, name FROM Account ORDER BY AccountNo");
        for(int i=0;i<aclist.size();i++){
            cout.println(aclist.get(i));
        }
        
        cout.println();
        cout.println("Enter U to update an account credit limit, T to verify a transaction, L to list transactions, Q to quit");
        cout.flush();
        String input = cin.nextLine();
        boolean quit = false;
        
        while(!quit){
            int c = input.charAt(0);
            switch(c){
                case 'u':
                case 'U':
                    cout.println("Enter Account No: ");
                    cout.flush();
                    String accno = cin.nextLine();
                    cout.println("Enter the new amount: ");
                    cout.flush();
                    String amt = cin.nextLine();
                    int n = db.SetLimit(accno, amt);
                    cout.println(n + " records got updated");
                    break;
                case 'T':
                case 't':
                    cout.println("Enter Account No: ");
                    cout.flush();
                    accno = cin.nextLine();
                    cout.println("Enter the amount: ");
                    cout.flush();
                    amt = cin.nextLine();
                    String appcode = db.Charge(accno, amt);
                    System.out.println("The approval code is " + appcode);
                    break;
                case 'L':
                case 'l':
                    cout.println("Enter Account No: ");
                    cout.flush();
                    accno = cin.nextLine();
                    List<String> l = (List<String>) db.List(accno);
                    for(int i=0;i<l.size();i++){
                        System.out.println(l.get(i));
                    }
                    break;
                default:
                    quit = true;
                    
            }
            if(!quit){
                cout.println("Enter U to update an account redit limit, T to verify a transaction. L to list transactions, Q to quit");
                cout.flush();
                input = cin.nextLine();
            }
        }
    }
    
}
