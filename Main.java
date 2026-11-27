import java.util.Scanner;
import java.io.*;
import java.nio.file.*;

public class Main 
{
    
    static final String file_dir  =  "case_doc";
    static final String client    =  "case_doc/client_info.csv";
    static final String advocate  =  "case_doc/advocate_info.csv";
    static final String case_i    =  "case_doc/case_info.csv";
    static final String hearings  =  "case_doc/hearing_info.csv";
    static final String documents =  "case_doc/legal_info.csv";
    static final String report    =  "case_doc/report_info.csv";

    static Scanner sc = new Scanner(System.in);
    static String[] parts;
    static String line, person_name, email, person_details, person_info;
    static int id, current_id, option, case_id;
    static long ph_no;
    static boolean found = false;
    static Scanner read;
    static FileWriter fw;
    static File file, temp;


    public static void file_initialize() throws IOException
    {
        File dirc = new File(file_dir);
        if(!dirc.exists())
        {
            dirc.mkdirs();
        }
        file_create(client);
        file_create(advocate);
        file_create(case_i);
        file_create(hearings);
        file_create(documents);
        file_create(report);
    }   


    public static void file_create(String file_way) throws IOException
    {
        file = new File(file_way);
        if(!file.exists())
        {
            file.createNewFile();
        }
    }


    public static boolean phone_number_check()
    {
        System.out.print("Enter Phone Number: ");
        ph_no = sc.nextLong();
        sc.nextLine();
        if(String.valueOf(ph_no).length() == 10)
        {
            return false;
        }
        System.out.println("Enter Valid Phone Number");
        return true;
    }


    public static boolean email_check()
    {
        System.out.print("Enter Email: ");
        email = sc.next();
        sc.nextLine();
        if(email.contains("@") && email.contains("."))
        {
            return false;
        }
        System.out.println("Enter Valid Email Address");
        return true;
    }
    
    public static int exists(String person) throws IOException
    {
        while(is_existed(person))
        {
            System.out.println("Wrong: Already Exists");
        }
        return (id == -1) ? -1 : 1;
    }

    public static int not_exists(String person) throws IOException
    {
        while(!is_existed(person))
        {
            if(id == -1)
            {
                return -1;
            }
            System.out.println("Wrong: Enter Valid ID"); 
        }
        return 0;
    }

    public static void line_check(String line1) throws IOException
    {        
        try
        {
            read = new Scanner(file);
            fw = new FileWriter("case_doc/temp.csv");        
            while(read.hasNextLine())
            {
                line = read.nextLine();
                if(line.trim().isEmpty())
                {
                    continue;
                }
                parts = line.split(",");
                current_id = Integer.parseInt(parts[0].trim());
                if(id == current_id)
                {
                    if(line1.equals(""))
                    {
                        continue;
                    }
                    fw.write(line1 + "\n");                
                }
                else
                {
                    fw.write(line + "\n");
                }
            }
            read.close();
            fw.close();
        }
        finally
        {
            System.out.print("");
        }        
        Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);     
    }

    public static boolean empty_file()
    {
        if(file.length() == 0)
        {
            System.out.println("File is Empty");
            return true;
        }
        return false;
    }

    public static void display_person_details() 
    {        
        System.out.println("{\nPerson_id: " + parts[0]);
        System.out.println("Person_Name: " + parts[1]);
        System.out.println("ph_no: " + parts[2]);
        System.out.println("email: " + parts[3]);
        System.out.println("Person_Details: " + parts[4]);
        System.out.println("Person_Info: " + parts[5] + "\n}");
    }

    public static void display_case_details()
    {
        System.out.println("{\n   Case_id: " + parts[0]);
        System.out.println("   Case_type: " + parts[1]);
        System.out.println("   Case_status: " + parts[2]);
        System.out.println("   Advocate_id: " + parts[3]);
        System.out.println("   Client_id: " + parts[4]);
        System.out.println("   Case_details: " + parts[5] + "\n}");
    }

    public static void display_hearings_details()
    {
        System.out.println("Hearing_id: " + parts[0]);
        System.out.println("Case_id: " + parts[1]);
        System.out.println("Hearing_date: " + parts[2]);
        System.out.println("Hearing_time: " + parts[3]);
        System.out.println("Case_des: " + parts[4]);
        System.out.println("Court_Room_no: " + parts[5]);
    }

    public static boolean is_existed(String person) throws IOException
    {
        found = false;
        file = new File(person);
        read = new Scanner(file);
        System.out.print("Enter the ID / Enter -1 to Exit:  ");
        id = sc.nextInt();
        sc.nextLine();
        if(id == -1)
        {
            System.out.println("Search Cancelled");
            read.close();
            return found;
        }
        while(read.hasNextLine())
        {
            line = read.nextLine();
            if(line.trim().isEmpty())
            {
                continue;
            }
            parts = line.split(",");
            current_id = Integer.parseInt(parts[0].trim());
            if(current_id == id)
            {
                found = true;
                break;
            }
        }
        read.close();
        return found;
    }


    static void client_advocate_files(int opt, String person) throws IOException
    {
        String in_place1 = (person.equals(client)) ? "Occupation" : "Specialization";
        String in_place2 = (person.equals(client)) ? "Address" : "Experience (in Years)";
        switch(opt)
        {
            case 1 ->
            {
                if( exists(person) ==  -1)
                {
                    return;
                }
                System.out.print("Enter the Name: ");
                person_name = sc.nextLine();
                while(phone_number_check()) {}
                while(email_check()) {}
                System.out.print("Enter " + in_place1 + ": ");
                person_details = sc.nextLine();
                System.out.print("Enter " + in_place2 + ": ");
                person_info = (person.equals(client)) ? sc.nextLine() : sc.nextLine().trim() + " Years";
                fw = new FileWriter(person, true);
                fw.write(id + "," + person_name + "," + ph_no + "," + email + "," + person_details + "," + person_info + "\n");
                fw.close();
                System.out.println("Details Added");
            }
            case 2 -> 
            {
                file = new File(person);
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                parts[0] = String.valueOf(current_id);
                display_person_details();
            }       
            case 3 -> 
            {
                file = new File(person);
                temp = new File("case_doc/temp.csv");
                found = false;
                String line1;
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                System.out.println("Enter Choice:\n1. Update ph_no\n2. Update email\n3. " + in_place2);
                int ch = sc.nextInt();
                sc.nextLine();
                switch(ch)
                {
                    case 1 -> 
                    {
                        while(phone_number_check()) {}
                        parts[2] = String.valueOf(ph_no);
                    }
                    case 2 -> 
                    {
                        while(email_check()) {}
                        parts[3] = email;
                    }
                    case 3 -> 
                    {
                        System.out.print("Enter " + in_place2 + ": ");
                        parts[5] = (person.equals(client)) ? sc.nextLine() : sc.nextInt() + " Years";
                    }
                }
                line1 = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + parts[5];
                line_check(line1);
            }
            case 4 ->
            {
                file = new File(person);
                temp = new File("case_doc/temp.csv");
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                line_check("");  
            }
            default  ->  
            {
                System.out.println("Wrong Option");
                return;
            }
        }
    }


    static void caseManagement(String person) throws IOException 
    {  
        System.out.println("Case Management Options:"); 
        System.out.println("1. Add new legal case"); 
        System.out.println("2. Search and view case details"); 
        System.out.println("3. Assign client and advocate / Update case status"); 

        option = sc.nextInt();
        sc.nextLine();

        int cl_id, adv_id;
        String case_type, case_st, case_dt;

        switch(option)
        {           
            case 1 ->
            {
                if( exists(person) == -1 )
                {
                    return;
                }
                System.out.print("Enter Type of Case: ");
                case_type = sc.nextLine();
                System.out.print("Enter Case Status \n1. Open\n2. Close");
                int chc = sc.nextInt();
                switch(chc)
                {
                    case 1   ->  case_st = "Open";
                    case 2   ->  case_st = "Close";
                    default  ->  
                    {
                        System.out.println("Wrong Option");
                        return;
                    }
                }    
                sc.nextLine();                
                System.out.print("Enter Client ID: ");
                cl_id = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter File Date: ");
                case_dt = sc.nextLine();
                fw = new FileWriter(person, true);
                fw.write(id + "," + case_type + "," + case_st + "," + "---" + "," + cl_id + "," + case_dt + "\n");
                fw.close();
                System.out.println("Details Added");
            }
            case 2 ->
            {
                file = new File(person);
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                parts[0] = String.valueOf(current_id);
                display_case_details();
            }
            case 3 ->
            {
                String upd;
                file = new File(person);
                temp = new File("case_doc/temp.csv");
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                System.out.println("Enter the Choice \n1. Assign client and advocate\n2. Update case status");
                int chc = sc.nextInt();
                switch(chc)
                {
                    case 1   ->  
                    {
                        System.out.print("Enter Advocate ID: ");
                        adv_id = sc.nextInt();
                        upd = parts[0] + "," + parts[1] + "," + parts[2] + "," + adv_id + "," + parts[4] + "," + parts[5];
                    }
                    case 2   ->  
                    {
                        System.out.print("Enter Case Status: ");
                        case_st = sc.next();
                        upd = parts[0] + "," + parts[1] + "," + case_st + "," + parts[3] + "," + parts[4] + "," + parts[5];
                    }
                    default  ->  
                    {
                        System.out.println("Wrong Option");
                        return;
                    }
                }
                sc.nextLine(); 
                line_check(upd); 
            }
            default  ->  
            {
                System.out.println("Wrong Option");
                return;
            }    
        }
    }


    static void hearingScheduleManagement(String person) throws IOException 
    {
        System.out.println("Hearing Schedule Management Options:"); 
        System.out.println("1. Schedule hearing dates"); 
        System.out.println("2. View upcoming hearings"); 
        System.out.println("3. Update hearing details");

        int option = sc.nextInt(); 

        int hr_id, room_no;
        String hr_time, hr_date, hr_des;
        sc.nextLine();
        switch(option)
        {           
            case 1 ->
            {
                if( exists(person) ==  -1)
                {
                    return;
                }
                hr_id = id;
                if( not_exists(case_i) == -1 )
                {
                    return;
                }
                System.out.print("Enter Hearing Date: ");
                hr_date = sc.next(); 
                System.out.print("Enter Hearing Time: ");
                hr_time = sc.next();
                sc.nextLine();
                System.out.print("Enter Purpose: ");
                hr_des = sc.nextLine();
                System.out.print("Enter Room Number: ");
                room_no = sc.nextInt();
                sc.nextLine();
                fw = new FileWriter(person, true);
                fw.write(hr_id + "," + id + "," + hr_date + "," + hr_time + "," + hr_des + "," + room_no + "\n");
                fw.close();
                System.out.println("Details Added");
            }
            case 2 ->
            {
                file = new File(person);
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                display_hearings_details();
            }
            case 3 ->
            {
                String upd;
                file = new File(person);
                temp = new File("case_doc/temp.csv");
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                System.out.println("Enter the Choice: \n1. Date\n2. Time\n3. Room Number");
                int chc = sc.nextInt();
                sc.nextLine();
                switch(chc)
                {
                    case 1   ->  
                    {
                        System.out.print("Enter Updated Date: ");
                        hr_date = sc.next();
                        parts[2] = hr_date;
                    }
                    case 2   ->  
                    {
                        System.out.print("Enter Updated Time: ");
                        hr_time = sc.next();
                        parts[3] = hr_time;
                    }
                    case 3 ->
                    {
                        System.out.print("Enter Room Number: ");
                        room_no = sc.nextInt();
                        sc.nextLine();
                        parts[5] = String.valueOf(room_no);
                    }
                    default  ->  
                    {
                        System.out.println("Wrong Option");
                        return;
                    }
                }
                upd = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + parts[5]; 
                line_check(upd); 
            }
            default  ->  
            {
                System.out.println("Wrong Option");
                return;
            }    
        }
    }


    static void legalDocumentManagement(String person) throws IOException 
    {
        System.out.println("Legal Document Management Options:"); 
        System.out.println("1. Store case documents"); 
        System.out.println("2. View document details"); 
        System.out.println("3. Update document records"); 
        System.out.println("4. Delete unnecessary documents"); 

        int doc_id;
        String doc_date, doc_des, doc_name, doc_type;
        option = sc.nextInt();
        sc.nextLine();
        switch(option)
        {
            case 1 ->
            {
                if( exists(person) ==  -1)
                {
                    return;
                }
                doc_id = id;
                System.out.print("Enter the Name: ");
                doc_name = sc.nextLine();                
                if( not_exists(person) == -1 )
                {
                    return;
                }
                System.out.print("Enter the Type: ");                
                doc_type = sc.nextLine();
                System.out.print("Enter Updated Date: ");
                doc_date = sc.next();
                sc.nextLine();
                System.out.print("Enter the Description: ");
                doc_des = sc.nextLine();
                fw = new FileWriter(person, true);
                fw.write(doc_id + "," + doc_name + "," + id + "," + doc_type + "," + doc_date + "," + doc_des + "\n");
                fw.close();
                System.out.println("Details Added");
            }
            case 2 -> 
            {
                file = new File(person);
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                parts[0] = String.valueOf(current_id);
                System.out.println("Document_id: " + parts[0]);
                System.out.println("Document_Name: " + parts[1]);
                System.out.println("Case_id: " + parts[2]);
                System.out.println("Case_type: " + parts[3]);
                System.out.println("Updated_date: " + parts[4]);
                System.out.println("Document_description: " + parts[5]);
            }       
            case 3 -> 
            {
                file = new File(person);
                temp = new File("case_doc/temp.csv");
                found = false;
                String line1;
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                }
                System.out.print("Enter Updated Date: ");
                doc_date = sc.next();
                line1 = parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + doc_date + "," + parts[5]; 
                line_check(line1); 
            }
            case 4 ->
            {
                file = new File(person);
                temp = new File("case_doc/temp.csv");
                if( empty_file() || ( not_exists(person) == -1 ) )
                {
                    return;
                } 
                line_check(""); 
            }
            default  ->  
            {
                System.out.println("Wrong Option");
                return;
            }
        }
    }


    static void reportModule() throws IOException 
    {    
        System.out.println("Report Module Options:"); 
        System.out.println("1. Display all clients"); 
        System.out.println("2. Display all advocates"); 
        System.out.println("3. Display all active cases"); 
        System.out.println("4. Generate case summary");

        option = sc.nextInt();
        sc.nextLine();
        switch(option)
        {
            case 1 -> 
            {
                file = new File(client);
                read = new Scanner(file);
                while(read.hasNextLine())
                {
                    line = read.nextLine();
                    if(line.trim().isEmpty())
                    {
                        continue;
                    }
                    parts = line.split(",");
                    display_person_details();
                }
                read.close();
            } 
            case 2 ->
            {
                file = new File(advocate);
                read = new Scanner(file);
                while(read.hasNextLine())
                {
                    line = read.nextLine();
                    if(line.trim().isEmpty())
                    {
                        continue;
                    }
                    parts = line.split(",");
                    display_person_details();
                }
                read.close();
            }
            case 3 ->
            {
                file = new File(case_i);
                read = new Scanner(file);
                while(read.hasNextLine())
                {
                    line = read.nextLine();
                    if(line.trim().isEmpty())
                    {
                        continue;
                    }
                    parts = line.split(",");
                    display_case_details();
                }
                read.close();
            }
            case 4 ->
            {
                file = new File(hearings);
                read = new Scanner(file);
                while(read.hasNextLine())
                {
                    line = read.nextLine();
                    if(line.trim().isEmpty())
                    {
                        continue;
                    }
                    parts = line.split(",");
                    display_hearings_details();
                }
                read.close();
            } 
            default  ->  
            {
                System.out.println("Wrong Option");
                return;
            }
        
        }
    }


    public static void main(String[] args) throws IOException 
    {

        file_initialize();   

        System.out.println("Select an option:");
        System.out.println("1. Client Management");
        System.out.println("2. Advocate Management");
        System.out.println("3. Case Management");
        System.out.println("4. Hearing Schedule Management");
        System.out.println("5. Legal Document Management");
        System.out.println("6. Report Module");

        int choice = sc.nextInt();

        switch (choice) 
        {
            case 1   -> 
            {
                System.out.println("Client Management Options:"); 
                System.out.println("1. Add new client"); 
                System.out.println("2. View client information"); 
                System.out.println("3. Update client details"); 
                System.out.println("4. Delete client");
                option = sc.nextInt();
                sc.nextLine();

                client_advocate_files(option, client);
           
            }
            case 2   ->  
            {
                System.out.println("Advocate Management Options:"); 
                System.out.println("1. Register advocate"); 
                System.out.println("2. View advocate information"); 
                System.out.println("3. Update advocate details"); 
                System.out.println("4. Assign advocates to cases"); 
                option = sc.nextInt();
                sc.nextLine();

                client_advocate_files(option, advocate);
           
            }
            case 3   ->  caseManagement(case_i);                                   
            case 4   ->  hearingScheduleManagement(hearings);                       
            case 5   ->  legalDocumentManagement(documents);                        
            case 6   ->  reportModule();                                    
            default  ->  System.out.println("Invalid option");
        }

        sc.close();
    }
}
