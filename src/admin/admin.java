package admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date; //To retrieve the Current Date

import person.Person;
import client.client;
import employee.employee;
import gui.secret;


public class admin extends Person {
    secret s = new secret();
    private String adminID;
    private String password;

    public admin(){
        super();
        adminID = "ADM000";
        password = "admin";
    }

    
    public String getID() {
        return adminID;
    }

    public void setID(String adminID) {
        this.adminID = adminID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String assignID(String type){
        int count = 0;
        String ogtype = type.toLowerCase();
        String queryid = ogtype;
        if(type == "employee"){
            queryid = "emp_id";
        }else if(type == "client"){
            queryid = "client_id";
        }else if(type=="project"){
            queryid = "project_id";
        }
        String countString;
        String query = "select count(distinct(" +queryid+")) from " + ogtype;
        Statement stmt = null;
        Connection c = null;
        ResultSet rs = null;
        try{
            c = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
            count = rs.getInt("count");
        }catch(Exception e){
            e.printStackTrace();
        }
        count++;
        countString = String.format("%d", count);
        if(countString.length()<3){
            for (int i = countString.length(); i < 3; i++){
                countString = "0"+countString;
            }
        }
        String finalID = "";
        //compareToIgnoreCase() -> 0 = same string, 1 = different strings
        if(type.compareToIgnoreCase("Client") == 0){
            finalID = "CLI" + countString;
        }else if(type.compareToIgnoreCase("Employee") == 0){
            finalID = "EMP" + countString;
        }else if(type.compareToIgnoreCase("Project") == 0){
            finalID = "PRO" + countString;
        }
        return finalID;
    }

    public String[] PersonList(client c){
        String[] list = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query  = "select client_id from client";
        String query2 = "select count(distinct(client_id)) from client";
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query2);
            rs.next();
            list = new String[rs.getInt(1)];
            rs = stmt.executeQuery(query);
            
            int i = 0;
            while(rs.next()){
                /* test = rs.getString(1);
                System.out.println(test); */
                list[i] = rs.getString(1);
                i++;
            }
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return list;
    }
    
    public String[] PersonList(employee emp){
        String[] list = null;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query  = "select emp_id from employee";
        String query2 = "select count(distinct(emp_id)) from employee";
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query2);
            rs.next();
            list = new String[rs.getInt(1)];
            rs = stmt.executeQuery(query);
            
            int i = 0;
            while(rs.next()){
                /* test = rs.getString(1);
                System.out.println(test); */
                list[i] = rs.getString(1);
                i++;
            }
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error");
        }
        return list;
    }

    public int AddPerson(employee E){
        Connection c = null;
        Statement stmt = null;
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        System.out.println(sqldate);

        //We need to add the data in the Person table first
        String personQuery = "insert into person values (?,?,?,?,?,?,?,?,?)";
        
        String Empquery = "insert into employee values" + 
        "( ?, ?, ?, 'N',? )";
        
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            stmt = c.createStatement();

            //Add the information in the Person table
            PreparedStatement ps = c.prepareStatement(personQuery);
            ps.setString(1, assignID("Employee"));;
            ps.setString(2, E.getName());
            ps.setString(3, E.getAddress()[0]);
            ps.setString(4, E.getAddress()[1]);
            ps.setString(5, E.getAddress()[2]);
            ps.setString(6, E.getAddress()[3]);
            ps.setString(7, String.format("%d", E.getPINCODE()));
            ps.setString(8, E.getNationality());
            ps.setDate(9, java.sql.Date.valueOf(E.getDOB()));
            
            int output = ps.executeUpdate();
            System.out.println(output + " Rows Updated");
            
            //Add the Employee information in the Employee Table
            ps = c.prepareStatement(Empquery);
            ps.setString(1, assignID("Employee"));
            ps.setInt(2,E.getExperience());
            ps.setString(3,E.getDomain());
            //To set the date as the current date
            ps.setDate(4, new java.sql.Date(date.getTime()));
            output = ps.executeUpdate();
            System.out.println(output + " Rows Updated");
            stmt.close();
            c.close();
            return 0;

        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    public int AddPerson(client C){
        Connection c = null;
        Statement stmt = null;
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        System.out.println(sqldate);

        //We need to add the data in the Person table first
        String personQuery = "insert into person values (?,?,?,?,?,?,?,?,?)";
        
        String Cliquery = "insert into client values" + 
        "(?, ?, 0)";
        
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            stmt = c.createStatement();

            //Add the information in the Person table
            PreparedStatement ps = c.prepareStatement(personQuery);
            ps.setString(1, assignID("Client"));;
            ps.setString(2, C.getName());
            ps.setString(3, C.getAddress()[0]);
            ps.setString(4, C.getAddress()[1]);
            ps.setString(5, C.getAddress()[2]);
            ps.setString(6, C.getAddress()[3]);
            ps.setString(7, String.format("%d", C.getPINCODE()));
            ps.setString(8, C.getNationality());
            ps.setDate(9, java.sql.Date.valueOf(C.getDOB()));
            
            int output = ps.executeUpdate();
            System.out.println(output + " Rows Updated");
            
            ps = c.prepareStatement(Cliquery);
            ps.setString(1, assignID("Client"));
            ps.setString(2, C.getCompany());
            output = ps.executeUpdate();
            System.out.println(output + " Row(s) Updated");
            stmt.close();
            c.close();
            return 0;

        }catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    public client showPrimaryDetails(client C){
        Connection c = null;
        client retreiveClient = new client();
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.sql.Date date;
        String query1 = "select p.id, p.name, p.dob, c.company ,c.total_orders" + 
                         " from person p, client c " +
                         "where p.id = c.client_id and c.client_id = ?";
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            ps = c.prepareStatement(query1);
            ps.setString(1, C.getID());
            rs = ps.executeQuery();
            rs.next();
            retreiveClient.setID(rs.getString(1));
            retreiveClient.setName(rs.getString(2));
            date  = rs.getDate(3);
            retreiveClient.setDOB(date.toString());
            retreiveClient.setCompany(rs.getString(4));
            retreiveClient.setTotal_Orders(rs.getInt(5));
        }catch(Exception e){
            e.printStackTrace();
        }
        return retreiveClient;
    }

    public employee showPrimaryDetails(employee E){
        Connection c = null;
        employee retreiveEmployee = new employee();
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.sql.Date date;
        String query1 = "select p.id, p.name, p.dob, e.experience ,e.specialisation_id " + 
                         " from person p, employee e " +
                         "where p.id = e.emp_id and e.emp_id = ?";
        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            ps = c.prepareStatement(query1);
            ps.setString(1, E.getID());
            rs = ps.executeQuery();
            rs.next();
            retreiveEmployee.setID(rs.getString(1));
            retreiveEmployee.setName(rs.getString(2));
            date  = rs.getDate(3);
            retreiveEmployee.setDOB(date.toString());
            retreiveEmployee.setExperience(rs.getInt(4));
            retreiveEmployee.setDomain(rs.getString(5));
        }catch(Exception e){
            e.printStackTrace();
        }
        return retreiveEmployee;
    }

    public void removePerson(client C){
        Connection c = null;
        String query = "delete from person where id = ?";
        //The following lines of code are temporary:
        C.setID("CLI001");
        String client_id = C.getID();

        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, client_id);
            int output = ps.executeUpdate();
            System.out.println(output + " Row(s) Removed");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void removePerson(employee E){
        Connection c = null;
        String query = "delete from person where id = ?";
        //The following line of code is temporary:
        E.setID("EMP001");
        String emp_id = E.getID();

        try{
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(s.url, s.dbUser, s.dbPass);
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, emp_id);
            int output = ps.executeUpdate();
            System.out.println(output + " Row(s) Removed");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    
}

//For Testing 
class Driver{
    public static void main(String[] args) {
        admin a = new admin();
        employee E = new employee();
        client Cli = new client();
        Cli.setID("CLI002");
        E.setID("EMP002");
        /* a.AddPerson(E);
        a.AddPerson(Cli);
        a.removePerson(Cli);
        a.removePerson(E); */
        //a.PersonList(Cli);
        /* String[] list = a.PersonList(Cli);
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        } */
        //  a.assignID("Employee");
        Cli = a.showPrimaryDetails(Cli);
        E = a.showPrimaryDetails(E);
        System.out.println("Client ID: " + Cli.getID() + "\nClient Name: " + Cli.getName());
        System.out.println("Employee ID: " + E.getID() + "\nEmpl Name: " + E.getName());
    } 
}