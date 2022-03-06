import java.sql.* ;
import java.util.Scanner;

public class VaccineApp
{
  public static Connection con;
  public static Statement statement;
  public static int sqlCode=0;
  public static String sqlState="00000";
  public static Scanner scanner = new Scanner(System.in);
  
  public static void main(String args[]) throws SQLException
  {
    connect();
    String input="";
    
    //looping menu
    while(true)
    {
      System.out.println("VaccineApp Main Menu"+"\n"+"1. Add a Person"+"\n"+"2. Assign a slot to a Person"+"\n"
                           +"3. Enter Vaccination information"+"\n"+"4. Exit Application"+"\n"+"Please Enter Your Option:");
      input=scanner.nextLine();
      if(input.equals("1"))
      {
        System.out.println("Enter your insert statement: ");
        input=scanner.nextLine();
        addAPerson(input);
      }
      else if(input.equals("2"))
      {
        System.out.println("Enter the slot key and the registrant key: (name,slotNo,date,time,healthInsuranceNumber)");
        input=scanner.nextLine();
        assignASlot(input);
      }
      else if(input.equals("3"))
      {
        System.out.println("Enter the slot key, nurse licenseNo, and vial info:");
        input=scanner.nextLine();
        vaccineInfo(input);
      }
      else if(input.equals("4"))
      {
        statement.close ( ) ;
        con.close ( ) ;
        break;
      }
      else
      {
        System.out.println("Invalid argument. Please enter a number between 1 to 4.");
      }
    }
    scanner.close();
  }
  
  //insert or update a record in Registrant
  public static void addAPerson(String input)
  {
    String sql[]=input.split("[( , )]");
    String tableName=sql[2];
    int hin=Integer.parseInt(sql[4]);
    //check if a record with the given healthInsuranceNumber already exists
    try
    {
      String querySQL = "SELECT * from " + tableName + " WHERE healthInsuranceNumber  = "+hin;
      //System.out.println (querySQL) ;
      java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
      
      //if already exists, ask if the user would like to update the record
      if ( rs.next ( ) )
      {
        System.out.println("Record already exists. Update the record? (Y/N)");
        String answer=scanner.nextLine();
        if(answer.equals("Y"))
        {
          update(input);
        }
      }
      else //if no such a record, insert one
      {
        insert(input);
      }
    }
    catch (SQLException e)
    {
      sqlCode = e.getErrorCode(); // Get SQLCODE
      sqlState = e.getSQLState(); // Get SQLSTATE
      
      System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
      System.out.println(e);
    }
    
  }
  
  public static void assignASlot(String input) throws SQLException
  {
    //get the slot key and the registrant key from user input
    String param[]=input.split(",");
    String location=param[0];
    int slotNo=Integer.parseInt(param[1]);
    Date date=java.sql.Date.valueOf(param[2]);
    String time=param[3];
    int hin=Integer.parseInt(param[4]);
    String querySQL = "SELECT vaccineName from Slot WHERE healthInsuranceNumber  = "+hin;
    //System.out.println (querySQL) ;
    java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
    String brand=null;
    int dosesAdministered=0;
    int dosesRequired=0;
    //check if the registrant has received any dose before
    while ( rs.next ( ) )
    {
      brand = rs.getString ( "VaccineName" ) ;
      dosesAdministered++;
      //System.out.println ("VaccineName:  " + brand);
    }
    //if the registrant has received doses before, then check if the registrant has already received enough doses
    if(dosesAdministered>0)
    {
      querySQL = "SELECT doses from Vaccine WHERE vaccineName  = '"+brand+"'";
      //System.out.println (querySQL) ;
      rs = statement.executeQuery ( querySQL ) ;
      rs.next();
      dosesRequired=rs.getInt("doses");
      //System.out.println ("Required number of doses:  " + dosesRequired);
      if(dosesAdministered==dosesRequired)
      {
        System.out.println("The registrant has already been administered required number of doses");
        return;
      }
    }
    //if the registrant hasn't received required number of doses,then check if the slot is valid
    //get slot info
    querySQL = "SELECT * from Slot WHERE name='"+location+"' and slotNo="+slotNo+" and date='"+date+"' and time='"+time+"'";
    //System.out.println (querySQL) ;
    rs = statement.executeQuery ( querySQL ) ;
    rs.next();
    Date allocationDate =rs.getDate("allocationDate");
    //check if the slot date has passed
    Date currentDate;
    querySQL = "SELECT DATE (CURRENT_TIMESTAMP) FROM sysibm.sysdummy1";
    //System.out.println (querySQL) ;
    rs = statement.executeQuery ( querySQL ) ;
    rs.next();
    currentDate=rs.getDate(1);
    Time currentTime;
    querySQL = "SELECT CURRENT_TIME \"Current time\" FROM sysibm.sysdummy1;";
    //System.out.println (querySQL) ;
    rs = statement.executeQuery ( querySQL ) ;
    rs.next();
    currentTime=rs.getTime(1);
    //System.out.println("current date: "+currentDate);
    if(currentDate.compareTo(date)>0)
    {
      System.out.println("The slot has expired");
      return;
    }
    //check if the slot has already been assigned to someone else
    if(allocationDate!=null)
    {
      System.out.println("The slot has already been assigned to someone else");
      return;
    }
    //assign the slot to the registrant
    String updateSQL="UPDATE Slot" + " SET (allocationDate,allocationTime,healthInsuranceNumber) = ('"+currentDate+"','"+currentTime+"',"+hin+")"+" WHERE name='"+location+"' and slotNo="+slotNo+" and date='"+date+"' and time='"+time+"'";
    //System.out.println(updateSQL);
    statement.executeUpdate(updateSQL);
    System.out.println("DONE");
  }
  
  public static void vaccineInfo(String input)throws SQLException
  {
    //get the slot key, nurse license, and vial info from user input
    String param[]=input.split(",");
    String location=param[0];
    int slotNo=Integer.parseInt(param[1]);
    Date date=java.sql.Date.valueOf(param[2]);
    String time=param[3];
    int licenseNo=Integer.parseInt(param[4]);
    String vaccineName=param[5];
    int batchNo=Integer.parseInt(param[6]);
    int vialNo=Integer.parseInt(param[7]);
    //find the health insurance number of the registrant that is going to receive the vial
    String querySQL = "SELECT healthInsuranceNumber from Slot WHERE name='"+location+"' and slotNo="+slotNo+" and date='"+date+"' and time='"+time+"'";
    //System.out.println (querySQL) ;
    java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
    rs.next();
    int hin=rs.getInt(1);
    //find the brand of vaccine that the registrant has previously received
    querySQL = "SELECT vaccineName from Slot WHERE healthInsuranceNumber  = "+hin;
    //System.out.println (querySQL) ;
    rs = statement.executeQuery ( querySQL ) ;
    String prevBrand=null;
    while ( rs.next ( ) )
    {
      String temp = rs.getString ( "VaccineName" ) ;
      //System.out.println ("Last vaccine brand:  " + temp);
      if(temp!=null)
      {
        prevBrand=temp;
      }
    }
    //make sure that the vaccine brand is consistent
    if(prevBrand!=null&&!prevBrand.equals(vaccineName))
    {
      System.out.println("Current vaccine brand is different from previous shot");
      return;
    }
    //create vial and update slot
    String stmt="insert into Vial values('"+vaccineName+"',"+batchNo+","+vialNo+","+licenseNo+","+hin+")";
    insert(stmt);
    String updateSQL="UPDATE Slot" + " SET (vaccineName,batchNo,vialNo) = ('"+vaccineName+"',"+batchNo+","+vialNo+")"+" WHERE name='"+location+"' and slotNo="+slotNo+" and date='"+date+"' and time='"+time+"'";
    //System.out.println(updateSQL);
    statement.executeUpdate(updateSQL);
    System.out.println("DONE");
  }
  
  private static void connect() throws SQLException
  {
    //set up connection
    try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
    catch (Exception cnfe){ System.out.println("Class not found"); }
    String url = "jdbc:db2://winter2021-comp421.cs.mcgill.ca:50000/cs421";
    String your_userid = null;
    String your_password = null;
    if(your_userid == null && (your_userid = System.getenv("SOCSUSER")) == null)
    {
      System.err.println("Error!! do not have a password to connect to the database!");
      System.exit(1);
    }
    if(your_password == null && (your_password = System.getenv("SOCSPASSWD")) == null)
    {
      System.err.println("Error!! do not have a password to connect to the database!");
      System.exit(1);
    }
    con = DriverManager.getConnection (url,your_userid,your_password) ;
    statement = con.createStatement ( ) ;
  }
  
  private static void insert(String input)
  {
    try
    {
      //System.out.println ( input ) ;
      statement.executeUpdate ( input ) ;
      System.out.println ( "DONE" ) ;
    }
    catch (SQLException e)
    {
      sqlCode = e.getErrorCode(); 
      sqlState = e.getSQLState(); 
      System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
      System.out.println(e);
    }
  }
  
  private static void update(String input)
  {
    String sql[]=input.split("[( , )]");
    String tableName=sql[2];
    int hin=Integer.parseInt(sql[4]);
    try
    {
      String updateSQL = "delete from " + tableName + " WHERE healthInsuranceNumber = "+ hin;
      //System.out.println(updateSQL);
      statement.executeUpdate(updateSQL);
      System.out.println("DONE");
      insert(input);
    }
    catch (SQLException e)
    {
      sqlCode = e.getErrorCode(); // Get SQLCODE
      sqlState = e.getSQLState(); // Get SQLSTATE
      
      // Your code to handle errors comes here;
      // something more meaningful than a print would be good
      System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
      System.out.println(e);
    }
  }
  
}