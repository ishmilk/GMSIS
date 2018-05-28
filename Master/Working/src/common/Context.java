/*
 * Author: Amy Dowse
 * Singleton Object that holds search critera to be used by the different search controllers 
 */
package common;

/**
 *
 * @author User
 */
public class Context 
{
    private final static Context instance = new Context();
    
    public static Context getInstance() 
    {
        return instance;
    }

    private String searchFor;
    private String criteria;

    public String getSearch()
    {
        return searchFor;
    }
    
    public void setSearch(String search)
    {
        searchFor = search;
    }
    
    public String getCriteria()
    {
        return criteria;
    }
    
    public void setCriteria(String criteria)
    {
        this.criteria = criteria;
    }
    
    
}
