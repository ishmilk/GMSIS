import javax.swing.*;
import java.util.*;
class database
{
    ArrayList <SystemUser> UserData=new ArrayList<SystemUser>();
    database()
    {
        UserData.add((new Admin(95545,"Nakarit","Pujumsin")).credentials("Want10","12345"));
        UserData.add((new User(94621,"Sanjeev","Ponnapula")).credentials("Alcor14","1234567"));
    }
    SystemUser loginCheck(String entryID,String entryPass) // checks login - return what?
    {
        for(SystemUser check : UserData)
        {
            if(check.IDcheck(entryID))
            {
                if(check.Passcheck(entryPass)) // renewed ivitation to recheck?
                {
                    return check;
                }
            }                    
        }
        return null;
    }
    String UserListQuery()
    {
        String list="";
        for(SystemUser check : UserData)
        {
            list=list+check.firstName+"  "+check.surname+"     "+check.employeeID+"\n";
        }
        System.out.println(list);
        return list;
    }
    void removeUser(int ID) // removes user ? why count? 
    {
        int count=0;
        for(SystemUser check : UserData)
        {
           if(check.employeeID==ID)
           {
               UserData.remove(count);
               break;
           }
           count++;
        }
    }
    void addUser(String a,String b,int c)
    {
        String ID=JOptionPane.showInputDialog(null,"Enter User Name ");
        String pass=JOptionPane.showInputDialog(null,"Enter Password");
        UserData.add((new User(c,a,b)).credentials(ID,pass));
    }
}
    
    
